/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;


public class InvalidPINException extends TelusAPIException {

  public InvalidPINException(String message, Throwable exception) {
    super(message, exception);
  }

  public InvalidPINException(Throwable exception) {
    super(exception);
  }

  public InvalidPINException(String message) {
    super(message);
  }
}



