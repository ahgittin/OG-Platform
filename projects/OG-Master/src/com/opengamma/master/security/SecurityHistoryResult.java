/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.master.AbstractHistoryResult;
import com.opengamma.util.PublicSPI;

/**
 * Result providing the history of a security.
 * <p>
 * The returned documents may be a mixture of versions and corrections.
 * The document instant fields are used to identify which are which.
 * See {@link SecurityHistoryRequest} for more details.
 */
@PublicSPI
@BeanDefinition
public class SecurityHistoryResult extends AbstractHistoryResult<SecurityDocument> {

  /**
   * Creates an instance.
   */
  public SecurityHistoryResult() {
  }

  /**
   * Creates an instance from a collection of documents.
   * 
   * @param coll  the collection of documents to add, not null
   */
  public SecurityHistoryResult(Collection<SecurityDocument> coll) {
    super(coll);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned securities from within the documents.
   * 
   * @return the securities, not null
   */
  public List<ManageableSecurity> getSecurities() {
    List<ManageableSecurity> result = new ArrayList<ManageableSecurity>();
    if (getDocuments() != null) {
      for (SecurityDocument doc : getDocuments()) {
        result.add(doc.getObject());
      }
    }
    return result;
  }

  /**
   * Gets the first security, or null if no documents.
   * 
   * @return the first security, null if none
   */
  public ManageableSecurity getFirstSecurity() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getObject() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried security.
   * 
   * @return the matching security, not null
   * @throws IllegalStateException if no security was found
   */
  public ManageableSecurity getSingleSecurity() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    } else {
      return getDocuments().get(0).getObject();
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SecurityHistoryResult}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static SecurityHistoryResult.Meta meta() {
    return SecurityHistoryResult.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(SecurityHistoryResult.Meta.INSTANCE);
  }

  @Override
  public SecurityHistoryResult.Meta metaBean() {
    return SecurityHistoryResult.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SecurityHistoryResult}.
   */
  public static class Meta extends AbstractHistoryResult.Meta<SecurityDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap());

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends SecurityHistoryResult> builder() {
      return new DirectBeanBuilder<SecurityHistoryResult>(new SecurityHistoryResult());
    }

    @Override
    public Class<? extends SecurityHistoryResult> beanType() {
      return SecurityHistoryResult.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
