/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

public interface PendingChargeHistory {
  public double getId();
  public String getFeatureCode();
  public String getFeatureRevenueCode();
  public boolean isBalanceImpactFlag();
  public double getAmount();
  public Date getCreationDate();
  public Date getEffectiveDate();
  public String getCode();
  public String getReasonCode();
  public double getPSTAmount();
  public double getGSTAmount();
  public double getHSTAmount();
  public char getApprovalStatus();
  public boolean isBalanceIgnoreFlag();
  public String getSubscriberId();
  public int getOperatorId();
  public String getProductType();
}