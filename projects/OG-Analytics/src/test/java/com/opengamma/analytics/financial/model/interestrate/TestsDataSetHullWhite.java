/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.model.interestrate;

import com.opengamma.analytics.financial.interestrate.YieldCurveBundle;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.analytics.financial.model.interestrate.definition.HullWhiteOneFactorPiecewiseConstantParameters;
import com.opengamma.analytics.math.curve.ConstantDoublesCurve;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.CombinedInterpolatorExtrapolatorFactory;
import com.opengamma.analytics.math.interpolation.Interpolator1D;
import com.opengamma.analytics.math.interpolation.Interpolator1DFactory;

/**
 * Data used for tests on the Hull-White one factor model.
 * @deprecated This class produces deprecated types.
 */
@Deprecated
public class TestsDataSetHullWhite {

  private static final double MEAN_REVERSION = 0.01;
  private static final double[] VOLATILITY = new double[] {0.01, 0.011, 0.012, 0.013, 0.014};
  private static final double[] VOLATILITY_TIME = new double[] {0.5, 1.0, 2.0, 5.0};
  private static final HullWhiteOneFactorPiecewiseConstantParameters MODEL_PARAMETERS = new HullWhiteOneFactorPiecewiseConstantParameters(MEAN_REVERSION, VOLATILITY, VOLATILITY_TIME);

  /**
   * Create a set of Hull-White parameters for testing.
   * @return The hull-White parameters.
   */
  public static HullWhiteOneFactorPiecewiseConstantParameters createHullWhiteParameters() {
    return MODEL_PARAMETERS;
  }

  /**
   * Create a set of Hull-White parameters with constant volatility for testing. The mean reversion is 1%.
   * @param sigma The Hull-White volatility.
   * @return The hull-White parameters.
   */
  public static HullWhiteOneFactorPiecewiseConstantParameters createHullWhiteParametersCst(final double sigma) {
    return new HullWhiteOneFactorPiecewiseConstantParameters(MEAN_REVERSION, new double[] {sigma}, new double[0]);
  }

  private static final Interpolator1D LINEAR_FLAT = CombinedInterpolatorExtrapolatorFactory.getInterpolator(Interpolator1DFactory.LINEAR, Interpolator1DFactory.FLAT_EXTRAPOLATOR,
      Interpolator1DFactory.FLAT_EXTRAPOLATOR);
  private static final String DSC_CURVE_NAME = "Discounting";
  private static final String FWD_CURVE_NAME = "Forward";
  private static final double[] RATES_TIME = new double[] {0.25, 0.5, 1.0, 2.0, 5.0, 10.0, 30.0};

  /**
   * Create constant discounting and forward curves.
   * @param dsc The discounting curve level.
   * @param fwd The forward curve level.
   * @return
   */
  public static YieldCurveBundle curves1(final double dsc, final double fwd) {
    final YieldCurveBundle curves = new YieldCurveBundle();
    curves.setCurve(DSC_CURVE_NAME, YieldCurve.from(ConstantDoublesCurve.from(dsc)));
    curves.setCurve(FWD_CURVE_NAME, YieldCurve.from(ConstantDoublesCurve.from(fwd)));
    return curves;
  }

  public static String[] nameCurves1() {
    return new String[] {DSC_CURVE_NAME, FWD_CURVE_NAME};
  }

  public static YieldCurveBundle curves2(final double dsc, final double fwd) {
    final double[] dscArray = new double[RATES_TIME.length];
    final double[] fwdArray = new double[RATES_TIME.length];
    for (int loopt = 0; loopt < RATES_TIME.length; loopt++) {
      dscArray[loopt] = dsc;
      fwdArray[loopt] = fwd;
    }
    final YieldCurveBundle curves = new YieldCurveBundle();
    curves.setCurve(DSC_CURVE_NAME, YieldCurve.from(new InterpolatedDoublesCurve(RATES_TIME, dscArray, LINEAR_FLAT, true)));
    curves.setCurve(FWD_CURVE_NAME, YieldCurve.from(new InterpolatedDoublesCurve(RATES_TIME, fwdArray, LINEAR_FLAT, true)));
    return curves;
  }

  public static String[] nameCurves2() {
    return new String[] {DSC_CURVE_NAME, FWD_CURVE_NAME};
  }

}
