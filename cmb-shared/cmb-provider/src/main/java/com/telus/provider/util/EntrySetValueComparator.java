/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import java.util.*;

public class EntrySetValueComparator implements Comparator {

  private final Comparator comparator;

  public EntrySetValueComparator(Comparator comparator) {
    this.comparator = comparator;
  }

  public int compare(Map.Entry o1, Map.Entry o2)
  {
      return comparator.compare(o1.getValue(), o2.getValue());
  }

  public int compare(Object o1, Object o2)
  {
      return compare((Map.Entry)o1, (Map.Entry)o2);
  }

  public boolean equals(Object o)
  {
      return false;
  }

}


