/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;

public interface PrepaidAdjustmentReason extends  AdjustmentReason {

  String getEquipmentType();
  boolean isTransactionIdRequired() ;
  boolean isCreditAdjustment();
  boolean isDebitAdjustment();
  double getMinimumBalance();
  double getMinimumAdjustmentAmount();
  double getMaximumAdjustmentAmount();

}