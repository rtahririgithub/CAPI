/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.provider.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;


public abstract class TMReflexiveReferenceDataGroup extends TMReferenceDataGroup {

  public static final Class[]  ONE_STRING_CLASS = new Class[]{String.class};
  public static final Class[]  ZERO_CLASSES     = new Class[0];
  public static final Object[] ZERO_OBJECTS     = new Object[0];

  //public static final Class STRING_CLASS     = String.class;
  public static final Class REFERENCE_CLASS  = Reference.class;
  public static final Class COLLECTION_CLASS = Collection.class;

  public static final long WAIT_TIME = 1000 * 90;  // 90 seconds

  protected final TMCondition loaded = new TMCondition(false);
  protected final Class referenceClass;
  protected final Object target;
  protected transient final Method method;
  protected Throwable exception;


  public TMReflexiveReferenceDataGroup(Class referenceClass, Object target, Method method) {
    this.referenceClass = referenceClass;
    this.target = target;
    this.method = method;
  }

  public Throwable getException() {
    return exception;
  }

  protected  Reference[] newReferenceArray(int size) throws TelusAPIException {
    return (Reference[])java.lang.reflect.Array.newInstance(referenceClass, size);
  }

  protected synchronized void setData(Collection collection) throws TelusAPIException {
    Reference[] array = newReferenceArray(collection.size());
    array = (Reference[])collection.toArray(array);
    setData(array);
  }

  protected synchronized void addData(String code, Reference reference) throws TelusAPIException {
    addData(code, reference, false);
  }

  protected synchronized void addData(String code, Reference reference, boolean addNullReferences) throws TelusAPIException {
    try {
      if(addNullReferences || reference != null) {
        cache.put(code, reference);

        Reference[] array = newReferenceArray(cache.size() + 1);
        array = (Reference[])cache.values().toArray(array);

        this.cacheAsArray = array;
      }
    }catch (Throwable e) {
      throw new TelusAPIException(e);
    }
  }

  protected void waitForLoading() throws TelusAPIException {
    try {
      loaded.waitForTrue(WAIT_TIME);
    } catch (InterruptedException e) {
      Throwable e2 = exception;
      if(e2 != null) {
        throw new TelusAPIException("method timed out, nesting original failure", e2);
      } else {
        throw new TelusAPIException("method timed out", e);
      }
    }
  }

}



