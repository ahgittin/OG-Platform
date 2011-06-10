/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.volatility.cube;

import java.util.List;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.time.Tenor;

/**
 * 
 */
@BeanDefinition
public class VolatilityCubeDefinition extends DirectBean implements UniqueIdentifiable {

  /**
   * The swap tenors
   */
  @PropertyDefinition
  private List<Tenor> _swapTenors;
  /**
   * The option expiries
   */
  @PropertyDefinition
  private List<Tenor> _optionExpiries;
  
  /**
   * The strikes relative to at the monet, in Bps
   */
  @PropertyDefinition
  private List<Double> _relativeStrikes;
  
  public UniqueIdentifier getUniqueId() {
    return null; //TODO this
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code VolatilityCubeDefinition}.
   * @return the meta-bean, not null
   */
  public static VolatilityCubeDefinition.Meta meta() {
    return VolatilityCubeDefinition.Meta.INSTANCE;
  }

  @Override
  public VolatilityCubeDefinition.Meta metaBean() {
    return VolatilityCubeDefinition.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -1091346138:  // swapTenors
        return getSwapTenors();
      case 146928806:  // optionExpiries
        return getOptionExpiries();
      case -1711425899:  // relativeStrikes
        return getRelativeStrikes();
    }
    return super.propertyGet(propertyName);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -1091346138:  // swapTenors
        setSwapTenors((List<Tenor>) newValue);
        return;
      case 146928806:  // optionExpiries
        setOptionExpiries((List<Tenor>) newValue);
        return;
      case -1711425899:  // relativeStrikes
        setRelativeStrikes((List<Double>) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      VolatilityCubeDefinition other = (VolatilityCubeDefinition) obj;
      return JodaBeanUtils.equal(getSwapTenors(), other.getSwapTenors()) &&
          JodaBeanUtils.equal(getOptionExpiries(), other.getOptionExpiries()) &&
          JodaBeanUtils.equal(getRelativeStrikes(), other.getRelativeStrikes());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getSwapTenors());
    hash += hash * 31 + JodaBeanUtils.hashCode(getOptionExpiries());
    hash += hash * 31 + JodaBeanUtils.hashCode(getRelativeStrikes());
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the swap tenors
   * @return the value of the property
   */
  public List<Tenor> getSwapTenors() {
    return _swapTenors;
  }

  /**
   * Sets the swap tenors
   * @param swapTenors  the new value of the property
   */
  public void setSwapTenors(List<Tenor> swapTenors) {
    this._swapTenors = swapTenors;
  }

  /**
   * Gets the the {@code swapTenors} property.
   * @return the property, not null
   */
  public final Property<List<Tenor>> swapTenors() {
    return metaBean().swapTenors().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the option expiries
   * @return the value of the property
   */
  public List<Tenor> getOptionExpiries() {
    return _optionExpiries;
  }

  /**
   * Sets the option expiries
   * @param optionExpiries  the new value of the property
   */
  public void setOptionExpiries(List<Tenor> optionExpiries) {
    this._optionExpiries = optionExpiries;
  }

  /**
   * Gets the the {@code optionExpiries} property.
   * @return the property, not null
   */
  public final Property<List<Tenor>> optionExpiries() {
    return metaBean().optionExpiries().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the strikes relative to at the monet, in Bps
   * @return the value of the property
   */
  public List<Double> getRelativeStrikes() {
    return _relativeStrikes;
  }

  /**
   * Sets the strikes relative to at the money, in Bps
   * @param relativeStrikes  the new value of the property
   */
  public void setRelativeStrikes(List<Double> relativeStrikes) {
    this._relativeStrikes = relativeStrikes;
  }

  /**
   * Gets the the {@code relativeStrikes} property.
   * @return the property, not null
   */
  public final Property<List<Double>> relativeStrikes() {
    return metaBean().relativeStrikes().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code VolatilityCubeDefinition}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code swapTenors} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Tenor>> _swapTenors = DirectMetaProperty.ofReadWrite(
        this, "swapTenors", VolatilityCubeDefinition.class, (Class) List.class);
    /**
     * The meta-property for the {@code optionExpiries} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Tenor>> _optionExpiries = DirectMetaProperty.ofReadWrite(
        this, "optionExpiries", VolatilityCubeDefinition.class, (Class) List.class);
    /**
     * The meta-property for the {@code relativeStrikes} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<Double>> _relativeStrikes = DirectMetaProperty.ofReadWrite(
        this, "relativeStrikes", VolatilityCubeDefinition.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map = new DirectMetaPropertyMap(
        this, null,
        "swapTenors",
        "optionExpiries",
        "relativeStrikes");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -1091346138:  // swapTenors
          return _swapTenors;
        case 146928806:  // optionExpiries
          return _optionExpiries;
        case -1711425899:  // relativeStrikes
          return _relativeStrikes;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends VolatilityCubeDefinition> builder() {
      return new DirectBeanBuilder<VolatilityCubeDefinition>(new VolatilityCubeDefinition());
    }

    @Override
    public Class<? extends VolatilityCubeDefinition> beanType() {
      return VolatilityCubeDefinition.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code swapTenors} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Tenor>> swapTenors() {
      return _swapTenors;
    }

    /**
     * The meta-property for the {@code optionExpiries} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Tenor>> optionExpiries() {
      return _optionExpiries;
    }

    /**
     * The meta-property for the {@code relativeStrikes} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<List<Double>> relativeStrikes() {
      return _relativeStrikes;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
