/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.fleet;

import com.telus.api.*;


public interface MemberIdentity {
  /**
   * @link aggregation
   */
  FleetIdentity getFleetIdentity();

  String getMemberId();
  
  /**
   * Returns the resource status.
  */
    
  String getResourceStatus();

  /**
   * This method may involve a remote method call.
  */
  boolean isInRange() throws  TelusAPIException;

}




