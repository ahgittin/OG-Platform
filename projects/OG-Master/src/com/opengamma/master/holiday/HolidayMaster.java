/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.holiday;

import com.opengamma.master.AbstractMaster;

/**
 * A general-purpose holiday master.
 * <p>
 * The holiday master provides a uniform view over a set of holiday definitions.
 * This interface provides methods that allow the master to be searched and updated.
 */
public interface HolidayMaster extends AbstractMaster<HolidayDocument> {

  /**
   * Searches for holidays matching the specified search criteria.
   * 
   * @param request  the search request, not null
   * @return the search result, not null
   * @throws IllegalArgumentException if the request is invalid
   */
  HolidaySearchResult search(HolidaySearchRequest request);

  /**
   * Queries the history of a single holiday.
   * <p>
   * The request must contain an object identifier to identify the holiday.
   * 
   * @param request  the history request, not null
   * @return the holiday history, not null
   * @throws IllegalArgumentException if the request is invalid
   */
  HolidayHistoryResult history(HolidayHistoryRequest request);

}
