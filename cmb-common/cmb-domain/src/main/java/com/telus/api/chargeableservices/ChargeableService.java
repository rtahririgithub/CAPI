/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.chargeableservices;

import com.telus.api.*;
import com.telus.api.reference.*;


public interface ChargeableService extends Reference {
  double getCharge();


  /**
   * @link aggregation
   */
  Waiver[] getWaivers();


  /**
   * Returns the Waiver with the specified code of <CODE>null</CODE>
   * if none exists.
   *
   */
  Waiver getWaiver(String code);

  boolean containsWaiver(String code);

  void apply() throws TelusAPIException;

  void waive(Waiver waiver) throws TelusAPIException;

  boolean isApplied();

  boolean isWaived();

  boolean isAppliedOrWaived();
}




