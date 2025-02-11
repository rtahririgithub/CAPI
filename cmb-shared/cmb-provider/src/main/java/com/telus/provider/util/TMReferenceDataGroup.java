/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import com.telus.api.*;
import com.telus.api.reference.*;
import java.io.*;
import java.util.*;


public class TMReferenceDataGroup implements Serializable, ReferenceDataGroup {

  private static int orderSequence;

  protected final Map cache = new HashMap(64);
  protected Reference[] cacheAsArray;
  private final int order;

  protected TMReferenceDataGroup() {
    synchronized(TMReferenceDataGroup.class) {
      order = orderSequence++;
    }
  }

  public TMReferenceDataGroup(Reference[] references) {
    this();
    setData(references);
  }

  public Throwable getException() {
    return null;
  }

  public boolean contains(String code) throws TelusAPIException {
    return cache.containsKey(code);
  }

  public Reference get(String code) throws TelusAPIException {
    return (Reference)cache.get(code);
  }

  public Reference[] getAll() throws TelusAPIException {
    return cacheAsArray;
  }

  public int getSize() {
    return cache.size();
  }

  protected void setData(Reference[] references) {
    if(references != null) {
      for(int i=0; i<references.length; i++) {
        cache.put(references[i].getCode(), references[i]);
      }
      this.cacheAsArray = references;
    }
  }

  public void reload() {
  }

  public int getOrder(){
    return order;
  }
}



