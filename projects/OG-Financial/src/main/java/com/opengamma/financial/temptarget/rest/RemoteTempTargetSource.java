/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.temptarget.rest;

import java.net.URI;

import com.opengamma.core.change.ChangeManager;
import com.opengamma.core.change.DummyChangeManager;
import com.opengamma.engine.CachingComputationTargetResolver;
import com.opengamma.financial.temptarget.TempTarget;
import com.opengamma.financial.temptarget.TempTargetResolver;
import com.opengamma.financial.temptarget.TempTargetSource;
import com.opengamma.id.UniqueId;
import com.opengamma.util.rest.AbstractRemoteClient;

/**
 * Provides remote access to a {@link TempTargetSource}. It is not normally necessary to explicitly cache instances of this; it will not normally be used directly but by a {@link TempTargetResolver}
 * which will in turn be cached by an owning {@link CachingComputationTargetResolver}.
 */
public class RemoteTempTargetSource extends AbstractRemoteClient implements TempTargetSource {

  /**
   * Creates an instance.
   *
   * @param baseUri the base target URI for all RESTful web services, not null
   */
  public RemoteTempTargetSource(final URI baseUri) {
    super(baseUri);
  }

  // TempTargetSource

  @Override
  public ChangeManager changeManager() {
    // TODO: implement change notifications
    return DummyChangeManager.INSTANCE;
  }

  @Override
  public TempTarget get(final UniqueId identifier) {
    final URI uri = DataTempTargetSourceResource.uriGet(getBaseUri(), identifier);
    return accessRemote(uri).get(TempTarget.class);
  }

}
