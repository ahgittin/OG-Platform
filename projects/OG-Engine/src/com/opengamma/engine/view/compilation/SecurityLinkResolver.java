/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view.compilation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.Trade;
import com.opengamma.core.position.impl.AbstractPortfolioNodeTraversalCallback;
import com.opengamma.core.position.impl.PortfolioNodeTraverser;
import com.opengamma.core.security.Security;
import com.opengamma.core.security.SecurityLink;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.id.IdentifierBundle;
import com.opengamma.id.ObjectIdentifier;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.ArgumentChecker;

/**
 * Utility to resolve security links in bulk.
 */
public final class SecurityLinkResolver {

  /** Logger. */
  private static final Logger s_logger = LoggerFactory.getLogger(SecurityLinkResolver.class);

  /**
   * The executor service.
   */
  private final ExecutorService _executorService;
  /**
   * The caching security source.
   */
  private final CachedSecuritySource _securitySource;

  /**
   * Creates an instance.
   * 
   * @param executorService  the threading service, not null
   * @param securitySource  the security source, not null
   */
  public SecurityLinkResolver(final ExecutorService executorService, final SecuritySource securitySource) {
    ArgumentChecker.notNull(executorService, "executorService");
    ArgumentChecker.notNull(securitySource, "securitySource");
    _executorService = executorService;
    _securitySource = new CachedSecuritySource(securitySource);
  }

  /**
   * Creates an instance.
   * 
   * @param viewCompilationContext  the context, not null
   */
  public SecurityLinkResolver(final ViewCompilationContext viewCompilationContext) {
    this(viewCompilationContext.getServices().getExecutorService(), viewCompilationContext.getServices().getSecuritySource());
  }

  //-------------------------------------------------------------------------
  /**
   * Resolves security links in bulk.
   * <p>
   * Some caching of securities occurs within this instance.
   * 
   * @param securityLinks  the bundles to lookup, not null
   * @throws RuntimeException if unable to resolve all the securities
   */
  public void resolveSecurities(final Collection<SecurityLink> securityLinks) {
    ArgumentChecker.noNulls(securityLinks, "securityLinks");
    final ExecutorCompletionService<SecurityLink> completionService = new ExecutorCompletionService<SecurityLink>(_executorService);
    
    final List<Future<SecurityLink>> submitted = Lists.newArrayListWithCapacity(securityLinks.size());
    for (SecurityLink link : securityLinks) {
      Security security = link.getTarget();
      if (security == null) {
        if (link.getObjectId() != null || link.getWeakId().size() > 0) {
          SecurityResolutionJob task = new SecurityResolutionJob(link, _securitySource);
          submitted.add(completionService.submit(task));
        } else {
          throw new OpenGammaRuntimeException("Unable to resolve empty security link");
        }
      } else if (security.getUniqueId() != null) {
        _securitySource.addToCache(security);
      }
    }
    
    boolean failed = false;
    for (int i = 0; i < submitted.size(); i++) {
      try {
        final Future<SecurityLink> future = completionService.take();
        future.get();  // this ensures link is passed across concurrency boundary correctly
      } catch (InterruptedException ex) {
        Thread.interrupted();
        s_logger.warn("Interrupted, so didn't finish resolution");
        failed = true;
        break;
      } catch (Exception ex) {
        s_logger.warn("Unable to resolve security", ex);
        failed = true;
        break;
      }
    }
    if (failed) {
      for (Future<SecurityLink> future : submitted) {
        future.cancel(false);
      }
      throw new OpenGammaRuntimeException("Unable to resolve all securities");
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Resolves a security link making use of the caching of this instance.
   * <p>
   * Underlying securities are not resolved.
   * 
   * @param link  the link to resolve, not null
   * @return the resolved security, not null
   * @throws RuntimeException if unable to resolve the security
   */
  public Security resolveSecurity(final SecurityLink link) {
    return link.resolve(_securitySource);
  }

  /**
   * Resolves security links on a position and associated trades.
   * <p>
   * Underlying securities are not resolved.
   * Some caching of securities occurs within this instance.
   * 
   * @param position  the position to resolve, not null
   * @throws RuntimeException if unable to resolve all the securities
   */
  public void resolveSecurities(final Position position) {
    Collection<SecurityLink> links = new ArrayList<SecurityLink>(position.getTrades().size() + 1);
    links.add(position.getSecurityLink());
    for (Trade trade : position.getTrades()) {
      links.add(trade.getSecurityLink());
    }
    resolveSecurities(links);
  }

  /**
   * Resolves security links on the positions and trades of a portfolio node.
   * <p>
   * Underlying securities are not resolved.
   * Some caching of securities occurs within this instance.
   * 
   * @param node  the node to resolve, not null
   * @throws RuntimeException if unable to resolve all the securities
   */
  public void resolveSecurities(final PortfolioNode node) {
    final Collection<SecurityLink> links = new ArrayList<SecurityLink>(256);
    PortfolioNodeTraverser.depthFirst(new AbstractPortfolioNodeTraversalCallback() {
      @Override
      public void preOrderOperation(Position position) {
        links.add(position.getSecurityLink());
        for (Trade trade : position.getTrades()) {
          links.add(trade.getSecurityLink());
        }
      }
    }).traverse(node);
    resolveSecurities(links);
  }

  //-------------------------------------------------------------------------
  /**
   * A small job that can be run in an executor to resolve a security against a security source.
   */
  static class SecurityResolutionJob implements Callable<SecurityLink> {
    private final SecurityLink _link;
    private final SecuritySource _securitySource;

    SecurityResolutionJob(SecurityLink link, SecuritySource securitySource) {
      _securitySource = securitySource;
      _link = link;
    }

    @Override
    public SecurityLink call() {
      _link.resolve(_securitySource);
      return _link;
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Encapsulate caching.
   */
  static class CachedSecuritySource implements SecuritySource {
    private final SecuritySource _underlying;
    private final ConcurrentMap<ObjectIdentifier, Security> _objectIdCache = new ConcurrentHashMap<ObjectIdentifier, Security>();
    private final ConcurrentMap<IdentifierBundle, Security> _weakIdCache = new ConcurrentHashMap<IdentifierBundle, Security>();

    CachedSecuritySource(SecuritySource underlying) {
      _underlying = underlying;
    }

    void addToCache(Security security) {
      if (security.getUniqueId() != null) {
        _objectIdCache.put(security.getUniqueId().getObjectId(), security);
      }
    }

    @Override
    public Security getSecurity(UniqueIdentifier uniqueId) {
      Security security = _objectIdCache.get(uniqueId.getObjectId());
      if (security == null) {
        security = _underlying.getSecurity(uniqueId);
        if (security != null) {
          _objectIdCache.putIfAbsent(uniqueId.getObjectId(), security);
        }
      }
      return security;
    }

    @Override
    public Collection<Security> getSecurities(IdentifierBundle bundle) {
      return _underlying.getSecurities(bundle);
    }

    @Override
    public Security getSecurity(IdentifierBundle bundle) {
      Security security = _weakIdCache.get(bundle);
      if (security == null) {
        security = _underlying.getSecurity(bundle);
        if (security != null) {
          _weakIdCache.putIfAbsent(bundle, security);
        }
      }
      return security;
    }
  }

}
