/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics.blotter.swap;

import com.opengamma.financial.security.swap.SwapSecurity;
import com.opengamma.web.analytics.blotter.ValueProvider;

/**
 *
 */
public class RateProvider implements ValueProvider<SwapSecurity> {

  @Override
  public Object getValue(SwapSecurity security) {
    return new RateVisitor().visit(security).getFirst();
  }
}