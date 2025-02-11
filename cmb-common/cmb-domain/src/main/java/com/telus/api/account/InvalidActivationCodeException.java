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
public class InvalidActivationCodeException extends TelusAPIException {
  private String activationCode;

  public InvalidActivationCodeException(String message, Throwable exception, String activationCode) {
    super(message, exception);
    this.activationCode = activationCode;
  }

  public InvalidActivationCodeException(Throwable exception, String serialNumber) {
    super(exception);
  }

  public InvalidActivationCodeException(String message, String serialNumber) {
    super(message);
  }

  public java.lang.String getActivationCode() {
    return activationCode;
  }
}



