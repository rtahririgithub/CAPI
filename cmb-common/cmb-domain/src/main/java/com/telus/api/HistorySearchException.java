/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api;


public class HistorySearchException extends TelusAPIException {
  public HistorySearchException(String message, Throwable exception) {
    super(message, exception);
  }

  public HistorySearchException(Throwable exception) {
    super(exception);
  }

  public HistorySearchException(String message) {
    super(message);
  }
}



