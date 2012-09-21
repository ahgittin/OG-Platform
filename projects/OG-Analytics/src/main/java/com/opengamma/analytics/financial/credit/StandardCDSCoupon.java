/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit;

/**
 * Enumerate the coupon rates that can be applied to Standard CDS contracts (European and North American)
 */
public enum StandardCDSCoupon {
  /**
   * 25bps contract
   */
  _25bps,
  /**
   * 100bps contract
   */
  _100bps,
  /**
   * 500bps contract
   */
  _500bps,
  /**
   * 750bps contract
   */
  _750bps,
  /**
   * 1000bps contract
   */
  _1000bps;
}