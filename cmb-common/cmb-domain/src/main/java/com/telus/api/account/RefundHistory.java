/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

public interface RefundHistory {
  public Date getDate();
  public String getCode();
  public String getReasonCode();
  public double getAmount();
  public boolean isAccountsPayableProcessFlag();
  public Date getAccountPayableProcessDate();
  public String getCouponGiftNumber();
  public String getRefPaymentMethod();
  public String getBankCode();
  public String getBankAccountNumber();
  public String getChequeNumber();
  public String getCreditCardNumber();
  public String getCreditCardAuthCode();
  public Date getCreditCardExpiryDate();
}