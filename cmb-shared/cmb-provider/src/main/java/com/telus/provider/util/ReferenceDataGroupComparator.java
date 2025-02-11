/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.util;

import java.util.Comparator;

public class ReferenceDataGroupComparator implements Comparator {

  public int compare(ReferenceDataGroup o1, ReferenceDataGroup o2)
  {
      if(o1 == o2)
      {
          return 0;
      }
      else if(o1 == null)
      {
          return -1;
      }
      else if(o2 == null)
      {
          return 1;
      }
      else
      {
        return (o1.getOrder()<o2.getOrder() ? -1 : (o1.getOrder()==o2.getOrder() ? 0 : 1));
      }
  }

  public int compare(Object o1, Object o2)
  {
      return compare((ReferenceDataGroup)o1, (ReferenceDataGroup)o2);
  }

  public boolean equals(Object o)
  {
      return false;
  }

}


