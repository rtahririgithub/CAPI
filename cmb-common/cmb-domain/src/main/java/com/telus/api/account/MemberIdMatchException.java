/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


/**
 * <CODE>NumberMatchException</CODE>
 *
 */
public class MemberIdMatchException extends TelusAPIException {

  private String pattern;

  public MemberIdMatchException(String message, Throwable exception, String pattern) {
    super(message, exception);
    this.pattern = pattern;
  }

  public MemberIdMatchException(Throwable exception, String pattern) {
    super(exception);
    this.pattern = pattern;
  }

  public MemberIdMatchException(String message, String pattern) {
    super(message);
    this.pattern = pattern;
  }

  public MemberIdMatchException(String message) {
    super(message);
    this.pattern = "";
  }


  public String getPattern() {
    return pattern;
  }
}



