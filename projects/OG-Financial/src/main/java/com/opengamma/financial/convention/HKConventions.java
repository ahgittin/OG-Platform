/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import static com.opengamma.core.id.ExternalSchemes.bloombergTickerSecurityId;
import static com.opengamma.financial.convention.InMemoryConventionBundleMaster.simpleNameSecurityId;

import org.threeten.bp.Period;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;
import com.opengamma.util.time.DateUtils;

/**
 *
 */
public class HKConventions {
  private static final char BBG_DAY_CODE = 'T';
  private static final char BBG_WEEK_CODE = 'Z';
  private static final char[] BBG_MONTH_CODES = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'};
  private static final BusinessDayConvention FOLLOWING = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Following");
  private static final DayCount ACT_365 = DayCountFactory.INSTANCE.getDayCount("Actual/365");
  private static final ExternalId HK = ExternalSchemes.financialRegionId("HK");

  public static synchronized void addFixedIncomeInstrumentConventions(final ConventionBundleMaster conventionMaster) {
    final ConventionBundleMasterUtils utils = new ConventionBundleMasterUtils(conventionMaster);
    for (int i = 1; i < 4; i++) {
      final String name = "HKD Deposit " + i + "d";
      final ExternalId bbgId = bloombergTickerSecurityId("HDDR" + i + BBG_DAY_CODE + " Curncy");
      final ExternalId simpleId = simpleNameSecurityId(name);
      utils.addConventionBundle(ExternalIdBundle.of(bbgId, simpleId), name, ACT_365, FOLLOWING, Period.ofDays(i), 0, false, HK);
    }
    for (int i = 1; i < 4; i++) {
      final String name = "HKD Deposit " + i + "w";
      final ExternalId bbgId = bloombergTickerSecurityId("HDDR" + i + BBG_WEEK_CODE + " Curncy");
      final ExternalId simpleId = simpleNameSecurityId(name);
      utils.addConventionBundle(ExternalIdBundle.of(bbgId, simpleId), name, ACT_365, FOLLOWING, Period.ofDays(i * 7), 0, false, HK);
    }
    for (int i = 1; i < 12; i++) {
      final String name = "HKD Deposit " + i + "m";
      final ExternalId bbgId = bloombergTickerSecurityId("HDDR" + BBG_MONTH_CODES[i - 1] + " Curncy");
      final ExternalId simpleId = simpleNameSecurityId(name);
      utils.addConventionBundle(ExternalIdBundle.of(bbgId, simpleId), name, ACT_365, FOLLOWING, Period.ofMonths(i), 0, false, HK);
    }
  }
}
