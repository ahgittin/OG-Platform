/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.curve.forward;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.opengamma.engine.function.config.AbstractRepositoryConfigurationBean;
import com.opengamma.engine.function.config.FunctionConfiguration;
import com.opengamma.engine.function.config.RepositoryConfigurationSource;
import com.opengamma.financial.analytics.model.curve.forward.ForwardFunctions.Defaults.CurrencyInfo;
import com.opengamma.financial.analytics.model.curve.forward.ForwardFunctions.Defaults.CurrencyPairInfo;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;

/**
 * Function repository configuration source for the functions contained in this package.
 */
public class ForwardFunctions extends AbstractRepositoryConfigurationBean {

  /**
   * Default instance of a repository configuration source exposing the functions from this package.
   *
   * @return the configuration source exposing functions from this package
   */
  public static RepositoryConfigurationSource instance() {
    return new ForwardFunctions().getObjectCreating();
  }

  public static RepositoryConfigurationSource defaults(final Map<String, CurrencyInfo> perCurrencyInfo, final Map<Pair<String, String>, CurrencyPairInfo> perCurrencyPairInfo) {
    final Defaults factory = new Defaults();
    factory.setPerCurrencyInfo(perCurrencyInfo);
    factory.setPerCurrencyPairInfo(perCurrencyPairInfo);
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  public static RepositoryConfigurationSource defaults(final Map<String, CurrencyInfo> perCurrencyInfo, final Map<Pair<String, String>, CurrencyPairInfo> perCurrencyPairInfo,
      final String interpolator, final String leftExtrapolator, final String rightExtrapolator) {
    final Defaults factory = new Defaults();
    factory.setPerCurrencyInfo(perCurrencyInfo);
    factory.setPerCurrencyPairInfo(perCurrencyPairInfo);
    factory.setInterpolator(interpolator);
    factory.setLeftExtrapolator(leftExtrapolator);
    factory.setRightExtrapolator(rightExtrapolator);
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  /**
   * Function repository configuration source for the default functions contained in this package.
   */
  public static class Defaults extends AbstractRepositoryConfigurationBean {

    /**
     * Currency specific data.
     */
    public static class CurrencyInfo implements InitializingBean {

      private String _curveConfiguration;
      private String _discountingCurve;

      public CurrencyInfo() {
      }

      public CurrencyInfo(final String curveConfiguration, final String discountingCurve) {
        setCurveConfiguration(curveConfiguration);
        setDiscountingCurve(discountingCurve);
      }

      public String getCurveConfiguration() {
        return _curveConfiguration;
      }

      public void setCurveConfiguration(final String curveConfiguration) {
        _curveConfiguration = curveConfiguration;
      }

      public String getDiscountingCurve() {
        return _discountingCurve;
      }

      public void setDiscountingCurve(final String discountingCurve) {
        _discountingCurve = discountingCurve;
      }

      @Override
      public void afterPropertiesSet() {
        ArgumentChecker.notNullInjected(getCurveConfiguration(), "curveConfiguration");
        ArgumentChecker.notNullInjected(getDiscountingCurve(), "discountingCurve");
      }

    }

    /**
     * Currency pair specific data.
     */
    public static class CurrencyPairInfo implements InitializingBean {

      private String _curveName;
      private String _curveCalculationMethod;

      public CurrencyPairInfo() {
      }

      public CurrencyPairInfo(final String curveName, final String curveCalculationMethod) {
        setCurveName(curveName);
        setCurveCalculationMethod(curveCalculationMethod);
      }

      public String getCurveName() {
        return _curveName;
      }

      public void setCurveName(final String curveName) {
        _curveName = curveName;
      }

      public String getCurveCalculationMethod() {
        return _curveCalculationMethod;
      }

      public void setCurveCalculationMethod(final String curveCalculationMethod) {
        _curveCalculationMethod = curveCalculationMethod;
      }

      @Override
      public void afterPropertiesSet() {
        ArgumentChecker.notNullInjected(getCurveName(), "curveName");
        ArgumentChecker.notNullInjected(getCurveCalculationMethod(), "curveCalculationMethod");
      }

    }

    private final Map<String, CurrencyInfo> _perCurrencyInfo = new HashMap<String, CurrencyInfo>();
    private final Map<Pair<String, String>, CurrencyPairInfo> _perCurrencyPairInfo = new HashMap<Pair<String, String>, CurrencyPairInfo>();
    private String _interpolator = "DoubleQuadratic";
    private String _leftExtrapolator = "LinearExtrapolator";
    private String _rightExtrapolator = "FlatExtrapolator";

    public void setPerCurrencyInfo(final Map<String, CurrencyInfo> perCurrencyInfo) {
      _perCurrencyInfo.clear();
      _perCurrencyInfo.putAll(perCurrencyInfo);
    }

    public Map<String, CurrencyInfo> getPerCurrencyInfo() {
      return _perCurrencyInfo;
    }

    public void setCurrencyInfo(final String currency, final CurrencyInfo info) {
      _perCurrencyInfo.put(currency, info);
    }

    public CurrencyInfo getCurrencyInfo(final String currency) {
      return _perCurrencyInfo.get(currency);
    }

    public void setPerCurrencyPairInfo(final Map<Pair<String, String>, CurrencyPairInfo> perCurrencyPairInfo) {
      _perCurrencyPairInfo.clear();
      _perCurrencyPairInfo.putAll(perCurrencyPairInfo);
    }

    public Map<Pair<String, String>, CurrencyPairInfo> getPerCurrencyPairInfo() {
      return _perCurrencyPairInfo;
    }

    public void setCurrencyPairInfo(final Pair<String, String> currencyPair, final CurrencyPairInfo info) {
      _perCurrencyPairInfo.put(currencyPair, info);
    }

    public CurrencyPairInfo getCurrencyPairInfo(final Pair<String, String> currencyPair) {
      return _perCurrencyPairInfo.get(currencyPair);
    }

    public void setInterpolator(final String interpolator) {
      _interpolator = interpolator;
    }

    public String getInterpolator() {
      return _interpolator;
    }

    public void setLeftExtrapolator(final String leftExtrapolator) {
      _leftExtrapolator = leftExtrapolator;
    }

    public String getLeftExtrapolator() {
      return _leftExtrapolator;
    }

    public void setRightExtrapolator(final String rightExtrapolator) {
      _rightExtrapolator = rightExtrapolator;
    }

    public String getRightExtrapolator() {
      return _rightExtrapolator;
    }

    @Override
    public void afterPropertiesSet() {
      ArgumentChecker.notNullInjected(getInterpolator(), "interpolator");
      ArgumentChecker.notNullInjected(getLeftExtrapolator(), "leftExtrapolator");
      ArgumentChecker.notNullInjected(getRightExtrapolator(), "rightExtrapolator");
      super.afterPropertiesSet();
    }

    protected void addForwardCurveDefaults(final List<FunctionConfiguration> functions) {
      final String[] args = new String[getPerCurrencyInfo().size() * 3];
      int i = 0;
      for (final Map.Entry<String, CurrencyInfo> e : getPerCurrencyInfo().entrySet()) {
        args[i++] = e.getKey();
        args[i++] = e.getValue().getCurveConfiguration();
        args[i++] = e.getValue().getDiscountingCurve();
      }
      functions.add(functionConfiguration(FXForwardCurveFromYieldCurvesPrimitiveDefaults.class, args));
      functions.add(functionConfiguration(FXForwardCurveFromYieldCurvesSecurityDefaults.class, args));
    }

    protected void addFXForwardCurveDefaults(final List<FunctionConfiguration> functions) {
      final String[] args = new String[getPerCurrencyPairInfo().size() * 3];
      int i = 0;
      for (final Map.Entry<Pair<String, String>, CurrencyPairInfo> e : getPerCurrencyPairInfo().entrySet()) {
        args[i++] = e.getKey().getFirst() + e.getKey().getSecond();
        args[i++] = e.getValue().getCurveName();
        args[i++] = e.getValue().getCurveCalculationMethod();
      }
      functions.add(functionConfiguration(FXForwardCurvePrimitiveDefaults.class, args));
      functions.add(functionConfiguration(FXForwardCurveSecurityDefaults.class, args));
      functions.add(functionConfiguration(FXForwardCurveTradeDefaults.class, args));
    }

    @Override
    protected void addAllConfigurations(final List<FunctionConfiguration> functions) {
      functions.add(functionConfiguration(FXForwardCurveFromMarketQuotesDefaults.class, getInterpolator(), getLeftExtrapolator(), getRightExtrapolator()));
      functions.add(functionConfiguration(InterpolatedForwardCurveDefaults.class, getInterpolator(), getLeftExtrapolator(), getRightExtrapolator()));
      addForwardCurveDefaults(functions);
      addFXForwardCurveDefaults(functions);
    }

  }

  @Override
  protected void addAllConfigurations(final List<FunctionConfiguration> functions) {
    functions.add(functionConfiguration(ForwardSwapCurveFromMarketQuotesFunction.class));
    functions.add(functionConfiguration(ForwardSwapCurveMarketDataFunction.class));
    functions.add(functionConfiguration(FXForwardCurveFromMarketQuotesFunction.class));
    functions.add(functionConfiguration(FXForwardCurveFromYieldCurvesFunction.class));
    functions.add(functionConfiguration(FXForwardCurveMarketDataFunction.class));
  }
}
