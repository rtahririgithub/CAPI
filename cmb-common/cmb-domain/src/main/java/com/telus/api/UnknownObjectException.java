/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api;


/**
 * <CODE>UnknownObjectException</CODE>
 *
 */

public class UnknownObjectException extends TelusAPIException {
  private String name;

  public UnknownObjectException(String message, Throwable exception, String name) {
    super(message + "; name=[" +name+ "]", exception);
    this.name = name;
  }

  public UnknownObjectException(Throwable exception,String name) {
    super(exception + "; name=[" +name+ "]");
    this.name = name;
  }

  public UnknownObjectException(String message,String name) {
    super(message + "; name=[" +name+ "]");
    this.name = name;
  }


  public UnknownObjectException(String message, Throwable exception) {
    super(message, exception);
  }

  public UnknownObjectException(Throwable exception) {
    super(exception);
  }

  public UnknownObjectException(String message) {
    super(message);
  }


  public String getName() {
    return name;
  }

}
