/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.issuer;

import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorSameMethodAdapter;
import com.opengamma.analytics.financial.interestrate.bond.definition.BillSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BillTransaction;
import com.opengamma.analytics.financial.interestrate.bond.provider.BillSecurityDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.bond.provider.BillTransactionDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.cash.derivative.DepositCounterpart;
import com.opengamma.analytics.financial.interestrate.cash.provider.DepositCounterpartDiscountingMethod;
import com.opengamma.analytics.financial.provider.calculator.discounting.PresentValueCurveSensitivityDiscountingCalculator;
import com.opengamma.analytics.financial.provider.description.IssuerProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;

/**
 * Calculates the present value of an inflation instruments by discounting for a given MarketBundle
 */
public final class PresentValueCurveSensitivityIssuerCalculator extends InstrumentDerivativeVisitorSameMethodAdapter<IssuerProviderInterface, MultipleCurrencyMulticurveSensitivity> {

  /**
   * The unique instance of the calculator.
   */
  private static final PresentValueCurveSensitivityIssuerCalculator INSTANCE = new PresentValueCurveSensitivityIssuerCalculator();

  /**
   * Gets the calculator instance.
   * @return The calculator.
   */
  public static PresentValueCurveSensitivityIssuerCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  private PresentValueCurveSensitivityIssuerCalculator() {
  }

  /**
   * Pricing methods.
   */
  private static final DepositCounterpartDiscountingMethod METHOD_DEPO_CTPY = DepositCounterpartDiscountingMethod.getInstance();
  /**
   * Composite calculator.
   */
  private static final PresentValueCurveSensitivityDiscountingCalculator PVCSDC = PresentValueCurveSensitivityDiscountingCalculator.getInstance();
  private static final BillSecurityDiscountingMethod METHOD_BILL_SEC = BillSecurityDiscountingMethod.getInstance();
  private static final BillTransactionDiscountingMethod METHOD_BILL_TR = BillTransactionDiscountingMethod.getInstance();

  @Override
  public MultipleCurrencyMulticurveSensitivity visit(final InstrumentDerivative derivative, final IssuerProviderInterface issuercurves) {
    try {
      return derivative.accept(this, issuercurves);
    } catch (final Exception e) {
      return derivative.accept(PVCSDC, issuercurves.getMulticurveProvider());
    }
  }

  //     -----     Deposit     -----

  @Override
  public MultipleCurrencyMulticurveSensitivity visitDepositCounterpart(final DepositCounterpart deposit, final IssuerProviderInterface issuercurves) {
    return METHOD_DEPO_CTPY.presentValueCurveSensitivity(deposit, issuercurves);
  }

  //     -----     Bond/Bill     -----

  @Override
  public MultipleCurrencyMulticurveSensitivity visitBillSecurity(final BillSecurity bill, final IssuerProviderInterface issuercurves) {
    return METHOD_BILL_SEC.presentValueCurveSensitivity(bill, issuercurves);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitBillTransaction(final BillTransaction bill, final IssuerProviderInterface issuercurves) {
    return METHOD_BILL_TR.presentValueCurveSensitivity(bill, issuercurves);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visit(final InstrumentDerivative derivative) {
    throw new UnsupportedOperationException();
  }

}