/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

public class PhoneNumberInUseException extends PhoneNumberException {

  public PhoneNumberInUseException(int reason, String message, Throwable exception) {
    super(reason, message, exception);
  }

  public PhoneNumberInUseException(int reason, Throwable exception) {
    super(reason, exception);
  }

  public PhoneNumberInUseException(int reason, String message) {
    super(reason, message);
  }

  public PhoneNumberInUseException(String message, Throwable exception) {
    super(PhoneNumberInUseException.UNKNOWN, message, exception);
  }

  public PhoneNumberInUseException(Throwable exception) {
    super(PhoneNumberInUseException.UNKNOWN, exception);
  }

  public PhoneNumberInUseException(String message) {
    super(PhoneNumberInUseException.UNKNOWN, message);
  }

}


