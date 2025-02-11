/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;
 
import com.telus.api.*;


/**
 * <CODE>UnknownBANException</CODE>
 *
 */
public class UnknownBANException extends UnknownObjectException {
  public UnknownBANException(String message, Throwable exception) {
    super(message, exception);
  }

  public UnknownBANException(Throwable exception) {
    super(exception);
  }

  public UnknownBANException(String message) {
    super(message);
  }
}


