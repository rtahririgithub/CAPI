/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;

/**
  * Base class for charges and credits (adjustments).
  *
  * Terminology:
  *
  * BillAdjustment refers to both charges and credits.
  * Adjustment refers to credits (Adjustment and Credit is generally interchangeable).
  * Charge refers to charges
  */
public interface BillAdjustment extends Reference {

  public static final char BALANCE_IMPACT_BILL_RUN = 'B';
  public static final char BALANCE_IMPACT_IMMEDIANT = 'I';

  /**
    * Returns true if this adjustment can be manually applied (applied by a user) or false
    * if this adjustment is applied automatically (by the system)
    *
    * @return boolean
    */
  public boolean isManualCharge();

  /**
    * Returns the amount for this adjustment.
    *
    * This amount may or may not be able to be changed by the user according to the value
    * returned by isAmountOveridable
    *
    * @return double
    */
  public double getAmount();

  /**
    * Returns true if the user is allowed to overide the amount returned by getAmount, otherwise
    * returns false
    *
    * @return boolean
    */
  public boolean isAmountOverrideable();
}
