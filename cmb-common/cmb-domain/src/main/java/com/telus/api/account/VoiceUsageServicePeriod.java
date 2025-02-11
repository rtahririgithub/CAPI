/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface VoiceUsageServicePeriod {

  /**
   * Returns period code.
   * @return String
   */
  String getPeriodCode();

  /**
   * Returns the number of calls.
   * @return int
   */
  int getCalls();

  /**
   * Returns total used minutes.
   * @return double
   */
  double getTotalUsed();

  /**
   * Returns included minutes.
   * @return double
   */
  double getIncluded();

  /**
   * Returns included used minutes.
   * @return double
   */
  double getIncludedUsed();

  /**
   * Returns free minutes.
   * @return double
   */
  double getFree();

  /**
   * Returns remaining minutes.
   * @return double
   */
  double getRemaining();

  /**
   * Returns chargeable minutes.
   * @return double
   */
  double getChargeable();

  /**
   * Returns chargeable amount.
   * @return double
   */
  double getChargeAmount();
}