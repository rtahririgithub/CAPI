/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;


public class InvalidCardChangeException extends InvalidServiceChangeException {

  private static final int REASON_OFFSET  = 1000;

  public static final int INVALID_STATUS   = REASON_OFFSET + 1;


  public InvalidCardChangeException(int reason, String message, Throwable exception) {
    super(reason, message, exception);
  }

  public InvalidCardChangeException(int reason, Throwable exception) {
    super(reason, exception);
  }

  public InvalidCardChangeException(int reason, String message) {
    super(reason, message);
  }

  public InvalidCardChangeException(int reason) {
    super(reason);
  }



  static {
    reasons.put(new Integer(INVALID_STATUS),  "INVALID_STATUS");
  }


}



