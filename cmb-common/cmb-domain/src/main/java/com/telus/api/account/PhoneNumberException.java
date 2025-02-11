/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import java.util.*;


public class PhoneNumberException extends TelusAPIException {

  public static final int UNKNOWN                       = 0;
  public static final int PRIMARY_RESERVATION_FAILED    = 1;
  public static final int ADDITIONAL_RESERVATION_FAILED = 2;

  private final int reason;


  public PhoneNumberException(int reason, String message, Throwable exception) {
    super(message + ": [reason=" + getReasonText(reason) + "]", exception);
    this.reason = reason;
  }

  public PhoneNumberException(int reason, Throwable exception) {
    this(reason, "", exception);
  }

  public PhoneNumberException(int reason, String message) {
    super(message + ": [reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }

  public PhoneNumberException(int reason) {
    super("[reason=" + getReasonText(reason) + "]");
    this.reason = reason;
  }

  public PhoneNumberException(String message, Throwable exception) {
    this(PhoneNumberException.UNKNOWN, message, exception);
  }

  public PhoneNumberException(Throwable exception) {
    this(PhoneNumberException.UNKNOWN, exception);
  }

  public PhoneNumberException(String message) {
    this(PhoneNumberException.UNKNOWN, message);
  }



  public int getReason() {
    return reason;
  }

  public String getReasonText() {
    return getReasonText(reason);
  }


  //---------------------------------------------------------
  // Reason to text translation.
  //---------------------------------------------------------
  protected static final Map reasons = new HashMap();

  static {
    reasons.put(new Integer(PRIMARY_RESERVATION_FAILED),     "PRIMARY_RESERVATION_FAILED");
    reasons.put(new Integer(ADDITIONAL_RESERVATION_FAILED),  "ADDITIONAL_RESERVATION_FAILED");
  }

  protected static String getReasonText(int reason) {
    Integer i = new Integer(reason);
    String text = (String)reasons.get(i);
    if(text == null) {
      return "UNKNOWN";
    }
    return text;
  }

}


