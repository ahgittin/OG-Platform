/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import com.opengamma.id.UniqueId;

/**
 * Contains the human-readable name and {@link UniqueId} of the target of a row in the analytics grid.
 */
/* package */  abstract class RowTarget {

  private final String _name;
  private final UniqueId _id;

  /* package */  RowTarget(String name, UniqueId id) {
    _name = name;
    _id = id;
  }

  /**
   * @return The name of the row's target
   */
  public String getName() {
    return _name;
  }

  /**
   * @return The ID of the row's target
   */
  public UniqueId getId() {
    return _id;
  }
}