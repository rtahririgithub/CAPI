/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;



/**
 * <CODE>PaymentFailedException</CODE>
 *
 */
public class UnknownSerialNumberException extends InvalidSerialNumberException {

  public UnknownSerialNumberException(String message, Throwable exception, String serialNumber) {
    super(message, exception, serialNumber);
  }

  public UnknownSerialNumberException(Throwable exception, String serialNumber) {
    super(exception, serialNumber);
  }

  public UnknownSerialNumberException(String message, String serialNumber) {
    super(message, serialNumber);
  }
  
  public UnknownSerialNumberException(Throwable exception, String serialNumber, int reason) {
	    super(exception, serialNumber, reason);
  }

  public UnknownSerialNumberException(String message, String serialNumber, int reason) {
	    super(message, serialNumber, reason);
  }
}



