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

public class DuplicateObjectException extends TelusAPIException {
  private String name;

  public DuplicateObjectException(String message, Throwable exception, String name) {
    super(message + "; name=[" +name+ "]", exception);
    this.name = name;
  }

  public DuplicateObjectException(Throwable exception,String name) {
    super(exception + "; name=[" +name+ "]");
    this.name = name;
  }

  public DuplicateObjectException(String message,String name) {
    super(message + "; name=[" +name+ "]");
    this.name = name;
  }


  public DuplicateObjectException(String message, Throwable exception) {
    super(message, exception);
  }

  public DuplicateObjectException(Throwable exception) {
    super(exception);
  }

  public DuplicateObjectException(String message) {
    super(message);
  }


  public String getName() {
    return name;
  }

}
