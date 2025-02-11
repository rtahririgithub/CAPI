/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

public interface DepositHistory {
  public Date getInvoiceCreationDate();
  public Date getInvoiceDueDate();
  public char getInvoiceStatus();
  public double getChargesAmount();
  public double getDepositPaidAmount();
  public Date getDepositPaidDate();
  public Date getDepositReturnDate();
  public char getDepositReturnMethod();
  public char getDepositTermsCode();
  public Date getCancellationDate();
  public String getCancellationReasonCode();
  public char getPaymentExpIndicator();
  public String getSubscriberId();
  public int getOperatorId();
  /**
   * Returns the amount value of the returned deposit
   * 
   */
  public double getReturnedAmount();
  
  /**
   * Returns the amount value of the cancelled deposit
   * 
   */
  public double getCancelledAmount();

}