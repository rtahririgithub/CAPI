/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.fleet;

import com.telus.api.*;


public interface FleetIdentity {
  int getUrbanId();

  int getFleetId();


  /**
   * Returns the fleet this object identifies or <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @link aggregation
   */
  Fleet getFleet() throws TelusAPIException;

  boolean equals(Object o);

  boolean equals(FleetIdentity o);

}



