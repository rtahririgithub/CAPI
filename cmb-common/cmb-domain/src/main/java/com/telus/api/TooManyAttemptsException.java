/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;


public class TooManyAttemptsException extends TelusAPIException {

  public TooManyAttemptsException(String message, Throwable exception) {
    super(message, exception);
  }

  public TooManyAttemptsException(Throwable exception) {
    super(exception);
  }

  public TooManyAttemptsException(String message) {
    super(message);
  }
}



