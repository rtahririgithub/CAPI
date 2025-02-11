/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;


/**
 * <CODE>UnknownBANException</CODE>
 *
 */
public class AuthenticationException extends TelusAPIException {
  public AuthenticationException(String message, Throwable exception) {
    super(message, exception);
  }

  public AuthenticationException(Throwable exception) {
    super(exception);
  }

  public AuthenticationException(String message) {
    super(message);
  }
}


