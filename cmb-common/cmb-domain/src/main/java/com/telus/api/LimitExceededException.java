/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;


public class LimitExceededException extends TelusAPIException {
  public LimitExceededException(String message, Throwable exception) {
    super(message, exception);
  }

  public LimitExceededException(Throwable exception) {
    super(exception);
  }

  public LimitExceededException(String message) {
    super(message);
  }
}



