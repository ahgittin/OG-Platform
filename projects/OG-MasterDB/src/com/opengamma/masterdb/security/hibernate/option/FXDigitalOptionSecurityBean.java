/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.masterdb.security.hibernate.option;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.financial.security.option.FXDigitalOptionSecurity;
import com.opengamma.masterdb.security.hibernate.CurrencyBean;
import com.opengamma.masterdb.security.hibernate.ExpiryBean;
import com.opengamma.masterdb.security.hibernate.SecurityBean;
import com.opengamma.masterdb.security.hibernate.ZonedDateTimeBean;

/**
 * A Hibernate bean representation of {@link FXDigitalOptionSecurity}.
 */
@BeanDefinition
public class FXDigitalOptionSecurityBean extends SecurityBean {
  
  @PropertyDefinition
  private double _putAmount;
  
  @PropertyDefinition
  private double _callAmount;
  
  @PropertyDefinition
  private ExpiryBean _expiry;
  
  @PropertyDefinition
  private CurrencyBean _putCurrency;
  
  @PropertyDefinition
  private CurrencyBean _callCurrency;
  
  @PropertyDefinition
  private CurrencyBean _paymentCurrency;
  
  @PropertyDefinition
  private ZonedDateTimeBean _settlementDate;
  
  @PropertyDefinition
  private Boolean _isLong;

  public FXDigitalOptionSecurityBean() {
    super();
  }

  //-----------------------------------------------------------------
  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof FXDigitalOptionSecurityBean)) {
      return false;
    }
    final FXDigitalOptionSecurityBean option = (FXDigitalOptionSecurityBean) other;

    return new EqualsBuilder()
      .append(getId(), option.getId())
      .append(getExpiry(), option.getExpiry())
      .append(getPutCurrency(), option.getPutCurrency())
      .append(getCallCurrency(), option.getCallCurrency())
      .append(getCallAmount(), option.getCallAmount())
      .append(getPutAmount(), option.getPutAmount())
      .append(getSettlementDate(), option.getSettlementDate())
      .append(getIsLong(), option.getIsLong())
      .isEquals();
  }

  //-----------------------------------------------------------------
  @Override
  public int hashCode() {
    return new HashCodeBuilder()
      .append(getExpiry())
      .append(getPutCurrency())
      .append(getCallCurrency())
      .append(getSettlementDate())
      .append(getPutAmount())
      .append(getCallAmount())
      .append(getIsLong())
      .toHashCode();
  }

  //-----------------------------------------------------------------
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code FXDigitalOptionSecurityBean}.
   * @return the meta-bean, not null
   */
  public static FXDigitalOptionSecurityBean.Meta meta() {
    return FXDigitalOptionSecurityBean.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(FXDigitalOptionSecurityBean.Meta.INSTANCE);
  }

  @Override
  public FXDigitalOptionSecurityBean.Meta metaBean() {
    return FXDigitalOptionSecurityBean.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -984864697:  // putAmount
        return getPutAmount();
      case 1066661974:  // callAmount
        return getCallAmount();
      case -1289159373:  // expiry
        return getExpiry();
      case 516393024:  // putCurrency
        return getPutCurrency();
      case 643534991:  // callCurrency
        return getCallCurrency();
      case -225763273:  // paymentCurrency
        return getPaymentCurrency();
      case -295948169:  // settlementDate
        return getSettlementDate();
      case -1180327226:  // isLong
        return getIsLong();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -984864697:  // putAmount
        setPutAmount((Double) newValue);
        return;
      case 1066661974:  // callAmount
        setCallAmount((Double) newValue);
        return;
      case -1289159373:  // expiry
        setExpiry((ExpiryBean) newValue);
        return;
      case 516393024:  // putCurrency
        setPutCurrency((CurrencyBean) newValue);
        return;
      case 643534991:  // callCurrency
        setCallCurrency((CurrencyBean) newValue);
        return;
      case -225763273:  // paymentCurrency
        setPaymentCurrency((CurrencyBean) newValue);
        return;
      case -295948169:  // settlementDate
        setSettlementDate((ZonedDateTimeBean) newValue);
        return;
      case -1180327226:  // isLong
        setIsLong((Boolean) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the putAmount.
   * @return the value of the property
   */
  public double getPutAmount() {
    return _putAmount;
  }

  /**
   * Sets the putAmount.
   * @param putAmount  the new value of the property
   */
  public void setPutAmount(double putAmount) {
    this._putAmount = putAmount;
  }

  /**
   * Gets the the {@code putAmount} property.
   * @return the property, not null
   */
  public final Property<Double> putAmount() {
    return metaBean().putAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the callAmount.
   * @return the value of the property
   */
  public double getCallAmount() {
    return _callAmount;
  }

  /**
   * Sets the callAmount.
   * @param callAmount  the new value of the property
   */
  public void setCallAmount(double callAmount) {
    this._callAmount = callAmount;
  }

  /**
   * Gets the the {@code callAmount} property.
   * @return the property, not null
   */
  public final Property<Double> callAmount() {
    return metaBean().callAmount().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the expiry.
   * @return the value of the property
   */
  public ExpiryBean getExpiry() {
    return _expiry;
  }

  /**
   * Sets the expiry.
   * @param expiry  the new value of the property
   */
  public void setExpiry(ExpiryBean expiry) {
    this._expiry = expiry;
  }

  /**
   * Gets the the {@code expiry} property.
   * @return the property, not null
   */
  public final Property<ExpiryBean> expiry() {
    return metaBean().expiry().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the putCurrency.
   * @return the value of the property
   */
  public CurrencyBean getPutCurrency() {
    return _putCurrency;
  }

  /**
   * Sets the putCurrency.
   * @param putCurrency  the new value of the property
   */
  public void setPutCurrency(CurrencyBean putCurrency) {
    this._putCurrency = putCurrency;
  }

  /**
   * Gets the the {@code putCurrency} property.
   * @return the property, not null
   */
  public final Property<CurrencyBean> putCurrency() {
    return metaBean().putCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the callCurrency.
   * @return the value of the property
   */
  public CurrencyBean getCallCurrency() {
    return _callCurrency;
  }

  /**
   * Sets the callCurrency.
   * @param callCurrency  the new value of the property
   */
  public void setCallCurrency(CurrencyBean callCurrency) {
    this._callCurrency = callCurrency;
  }

  /**
   * Gets the the {@code callCurrency} property.
   * @return the property, not null
   */
  public final Property<CurrencyBean> callCurrency() {
    return metaBean().callCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the paymentCurrency.
   * @return the value of the property
   */
  public CurrencyBean getPaymentCurrency() {
    return _paymentCurrency;
  }

  /**
   * Sets the paymentCurrency.
   * @param paymentCurrency  the new value of the property
   */
  public void setPaymentCurrency(CurrencyBean paymentCurrency) {
    this._paymentCurrency = paymentCurrency;
  }

  /**
   * Gets the the {@code paymentCurrency} property.
   * @return the property, not null
   */
  public final Property<CurrencyBean> paymentCurrency() {
    return metaBean().paymentCurrency().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the settlementDate.
   * @return the value of the property
   */
  public ZonedDateTimeBean getSettlementDate() {
    return _settlementDate;
  }

  /**
   * Sets the settlementDate.
   * @param settlementDate  the new value of the property
   */
  public void setSettlementDate(ZonedDateTimeBean settlementDate) {
    this._settlementDate = settlementDate;
  }

  /**
   * Gets the the {@code settlementDate} property.
   * @return the property, not null
   */
  public final Property<ZonedDateTimeBean> settlementDate() {
    return metaBean().settlementDate().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the isLong.
   * @return the value of the property
   */
  public Boolean getIsLong() {
    return _isLong;
  }

  /**
   * Sets the isLong.
   * @param isLong  the new value of the property
   */
  public void setIsLong(Boolean isLong) {
    this._isLong = isLong;
  }

  /**
   * Gets the the {@code isLong} property.
   * @return the property, not null
   */
  public final Property<Boolean> isLong() {
    return metaBean().isLong().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code FXDigitalOptionSecurityBean}.
   */
  public static class Meta extends SecurityBean.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code putAmount} property.
     */
    private final MetaProperty<Double> _putAmount = DirectMetaProperty.ofReadWrite(
        this, "putAmount", FXDigitalOptionSecurityBean.class, Double.TYPE);
    /**
     * The meta-property for the {@code callAmount} property.
     */
    private final MetaProperty<Double> _callAmount = DirectMetaProperty.ofReadWrite(
        this, "callAmount", FXDigitalOptionSecurityBean.class, Double.TYPE);
    /**
     * The meta-property for the {@code expiry} property.
     */
    private final MetaProperty<ExpiryBean> _expiry = DirectMetaProperty.ofReadWrite(
        this, "expiry", FXDigitalOptionSecurityBean.class, ExpiryBean.class);
    /**
     * The meta-property for the {@code putCurrency} property.
     */
    private final MetaProperty<CurrencyBean> _putCurrency = DirectMetaProperty.ofReadWrite(
        this, "putCurrency", FXDigitalOptionSecurityBean.class, CurrencyBean.class);
    /**
     * The meta-property for the {@code callCurrency} property.
     */
    private final MetaProperty<CurrencyBean> _callCurrency = DirectMetaProperty.ofReadWrite(
        this, "callCurrency", FXDigitalOptionSecurityBean.class, CurrencyBean.class);
    /**
     * The meta-property for the {@code paymentCurrency} property.
     */
    private final MetaProperty<CurrencyBean> _paymentCurrency = DirectMetaProperty.ofReadWrite(
        this, "paymentCurrency", FXDigitalOptionSecurityBean.class, CurrencyBean.class);
    /**
     * The meta-property for the {@code settlementDate} property.
     */
    private final MetaProperty<ZonedDateTimeBean> _settlementDate = DirectMetaProperty.ofReadWrite(
        this, "settlementDate", FXDigitalOptionSecurityBean.class, ZonedDateTimeBean.class);
    /**
     * The meta-property for the {@code isLong} property.
     */
    private final MetaProperty<Boolean> _isLong = DirectMetaProperty.ofReadWrite(
        this, "isLong", FXDigitalOptionSecurityBean.class, Boolean.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "putAmount",
        "callAmount",
        "expiry",
        "putCurrency",
        "callCurrency",
        "paymentCurrency",
        "settlementDate",
        "isLong");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -984864697:  // putAmount
          return _putAmount;
        case 1066661974:  // callAmount
          return _callAmount;
        case -1289159373:  // expiry
          return _expiry;
        case 516393024:  // putCurrency
          return _putCurrency;
        case 643534991:  // callCurrency
          return _callCurrency;
        case -225763273:  // paymentCurrency
          return _paymentCurrency;
        case -295948169:  // settlementDate
          return _settlementDate;
        case -1180327226:  // isLong
          return _isLong;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends FXDigitalOptionSecurityBean> builder() {
      return new DirectBeanBuilder<FXDigitalOptionSecurityBean>(new FXDigitalOptionSecurityBean());
    }

    @Override
    public Class<? extends FXDigitalOptionSecurityBean> beanType() {
      return FXDigitalOptionSecurityBean.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code putAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> putAmount() {
      return _putAmount;
    }

    /**
     * The meta-property for the {@code callAmount} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Double> callAmount() {
      return _callAmount;
    }

    /**
     * The meta-property for the {@code expiry} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExpiryBean> expiry() {
      return _expiry;
    }

    /**
     * The meta-property for the {@code putCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyBean> putCurrency() {
      return _putCurrency;
    }

    /**
     * The meta-property for the {@code callCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyBean> callCurrency() {
      return _callCurrency;
    }

    /**
     * The meta-property for the {@code paymentCurrency} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<CurrencyBean> paymentCurrency() {
      return _paymentCurrency;
    }

    /**
     * The meta-property for the {@code settlementDate} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ZonedDateTimeBean> settlementDate() {
      return _settlementDate;
    }

    /**
     * The meta-property for the {@code isLong} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<Boolean> isLong() {
      return _isLong;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
