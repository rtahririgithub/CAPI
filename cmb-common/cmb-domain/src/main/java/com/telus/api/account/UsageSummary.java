/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface UsageSummary {
  /**
   * Returns the feature code.
   * @return String
   */
  String getFeatureCode();

  /**
   * Returns the unit of measure code.
   * @return String
   */
  String getUnitOfMeasureCode();

  /**
   * Returns the subscriber phone number.
   * @return String
   */
  String getPhoneNumber();

}




