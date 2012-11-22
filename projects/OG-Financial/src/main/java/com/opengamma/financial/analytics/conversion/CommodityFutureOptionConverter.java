/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.ExerciseDecisionType;
import com.opengamma.analytics.financial.commodity.definition.AgricultureFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.AgricultureFutureOptionDefinition;
import com.opengamma.analytics.financial.commodity.definition.CommodityFutureOptionDefinition;
import com.opengamma.analytics.financial.commodity.definition.EnergyFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.EnergyFutureOptionDefinition;
import com.opengamma.analytics.financial.commodity.definition.MetalFutureDefinition;
import com.opengamma.analytics.financial.commodity.definition.MetalFutureOptionDefinition;
import com.opengamma.analytics.financial.instrument.InstrumentDefinition;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.financial.security.ExerciseTypeAnalyticsVisitorAdapter;
import com.opengamma.financial.security.FinancialSecurityVisitorAdapter;
import com.opengamma.financial.security.future.AgricultureFutureSecurity;
import com.opengamma.financial.security.future.CommodityFutureSecurity;
import com.opengamma.financial.security.future.EnergyFutureSecurity;
import com.opengamma.financial.security.future.MetalFutureSecurity;
import com.opengamma.financial.security.option.CommodityFutureOptionSecurity;
import com.opengamma.financial.security.option.OptionType;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.ArgumentChecker;

/**
 *
 */
public class CommodityFutureOptionConverter extends FinancialSecurityVisitorAdapter<InstrumentDefinition<?>> {

  /** security source */
  private SecuritySource _securitySource;
  /** Converter to get underlying future */
  private FutureSecurityConverter _futureSecurityConverter;

  /**
   * @param securitySource security source
   */
  public CommodityFutureOptionConverter(final SecuritySource securitySource) {
    ArgumentChecker.notNull(securitySource, "security source");
    _securitySource = securitySource;
    _futureSecurityConverter = new FutureSecurityConverter();
  }

  @Override
  public CommodityFutureOptionDefinition<?, ?> visitCommodityFutureOptionSecurity(final CommodityFutureOptionSecurity commodityOption) {
    ArgumentChecker.notNull(commodityOption, "security");
    ExternalIdBundle underlyingBundle = ExternalIdBundle.of(commodityOption.getUnderlyingId());
    CommodityFutureSecurity underlyingSecurity = (CommodityFutureSecurity) _securitySource.getSingle(underlyingBundle);
    if (underlyingSecurity == null) {
      throw new OpenGammaRuntimeException("No underlying future found with identifier " + commodityOption.getUnderlyingId());
    }

    final ZonedDateTime expiry = underlyingSecurity.getExpiry().getExpiry();
    final boolean isCall = (commodityOption.getOptionType().equals(OptionType.CALL));
    final ExerciseDecisionType exerciseType = commodityOption.getExerciseType().accept(ExerciseTypeAnalyticsVisitorAdapter.getInstance());
    if (underlyingSecurity instanceof AgricultureFutureSecurity) {
      AgricultureFutureDefinition underlyingDefinition = (AgricultureFutureDefinition) underlyingSecurity.accept(_futureSecurityConverter);
      return new AgricultureFutureOptionDefinition(expiry,
          underlyingDefinition,
          commodityOption.getStrike(),
          exerciseType,
          isCall);
    } else if (underlyingSecurity instanceof EnergyFutureSecurity) {
      EnergyFutureDefinition underlyingDefinition = (EnergyFutureDefinition) underlyingSecurity.accept(_futureSecurityConverter);
      return new EnergyFutureOptionDefinition(expiry,
          underlyingDefinition,
          commodityOption.getStrike(),
          exerciseType,
          isCall);
    } else if (underlyingSecurity instanceof MetalFutureSecurity) {
      MetalFutureDefinition underlyingDefinition = (MetalFutureDefinition) underlyingSecurity.accept(_futureSecurityConverter);
      return new MetalFutureOptionDefinition(expiry,
          underlyingDefinition,
          commodityOption.getStrike(),
          exerciseType,
          isCall);
    } else {
      throw new OpenGammaRuntimeException("Unknown commodity option underlying type " + underlyingSecurity.getClass().getName());
    }
  }

}