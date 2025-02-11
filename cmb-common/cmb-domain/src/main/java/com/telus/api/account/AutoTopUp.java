/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.TelusAPIException;

import java.util.Date;

/**
 * When Auto Top-up is setup, the Balance is not immediately
 * affected – it will be affected 30 days from the current day.
 *
 */
public interface AutoTopUp {
  double getChargeAmount();

  void setChargeAmount(double chargeAmount);

  Date getNextChargeDate();

  boolean hasThresholdRecharge();

  void setHasThresholdRecharge(boolean value);

  double getThresholdAmount();

  void setThresholdAmount(double value);

  void apply() throws TelusAPIException;

}



