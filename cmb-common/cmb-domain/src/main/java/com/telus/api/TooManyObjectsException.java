/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;


/**
 * <CODE>TooManyObjectsException</CODE>
 *
 */

public class TooManyObjectsException extends TelusAPIException {

  public TooManyObjectsException(String message, Throwable exception) {
    super(message, exception);
  }

  public TooManyObjectsException(Throwable exception) {
    super(exception);
  }

  public TooManyObjectsException(String message) {
    super(message);
  }

}
