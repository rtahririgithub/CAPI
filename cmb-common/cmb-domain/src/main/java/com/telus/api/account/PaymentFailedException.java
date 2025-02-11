/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


/**
 * <CODE>PaymentFailedException</CODE>
 *
 */
public class PaymentFailedException extends TelusAPIException {
  private double amount;

  public PaymentFailedException(String message, Throwable exception, double amount) {
    super(message, exception);
    this.amount = amount;
  }

  public PaymentFailedException(Throwable exception, double amount) {
    super(exception);
    this.amount = amount;
  }

  public PaymentFailedException(String message, double amount) {
    super(message);
    this.amount = amount;
  }

  public double getAmount() {
    return amount;
  }
}


