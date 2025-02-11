/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;

/**
 * <CODE>InvalidAirtimeRateException</CODE>
 *
 */

public class InvalidAirtimeRateException extends TelusAPIException {
  private double airtimeRate;


  public InvalidAirtimeRateException(String message, Throwable exception, double airtimeRate) {
    super(message + "; airtimeRate=[" +airtimeRate+ "]", exception);
    this.airtimeRate = airtimeRate;
  }


  public InvalidAirtimeRateException(Throwable exception,double airtimeRate) {
    super(exception + "; airtimeRate=[" +airtimeRate+ "]");
    this.airtimeRate = airtimeRate;
  }

  public InvalidAirtimeRateException(String message,double airtimeRate) {
    super(message + "; airtimeRate=[" +airtimeRate+ "]");
    this.airtimeRate = airtimeRate;
  }

    public InvalidAirtimeRateException(double airtimeRate) {
    super("airtimeRate=[" +airtimeRate+ "]");
    this.airtimeRate = airtimeRate;
  }

  public double getAirtimeRate() {
    return airtimeRate;
  }

}
