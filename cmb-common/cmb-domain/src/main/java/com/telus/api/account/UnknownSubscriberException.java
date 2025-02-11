/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


/**
 * <CODE>UnknownSubscriberException</CODE>
 *
 */
public class UnknownSubscriberException extends UnknownObjectException {
  public UnknownSubscriberException(String message, Throwable exception) {
    super(message, exception);
  }

  public UnknownSubscriberException(Throwable exception) {
    super(exception);
  }

  public UnknownSubscriberException(String message) {
    super(message);
  }
}


