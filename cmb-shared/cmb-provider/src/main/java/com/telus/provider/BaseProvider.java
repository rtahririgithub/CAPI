/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider;

import java.io.Serializable;

public class BaseProvider implements Serializable {
  /**
   *@link aggregation
   */
  protected final transient TMProvider provider;

  
  public BaseProvider(TMProvider provider) {
    this.provider = provider;
  }

  public static final boolean isNull(String s) {
    return s == null || s.length() == 0;
  }

  public TMProvider getProvider() {
    return provider;
  }

  
}



