/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.reference;

public interface ChargeType extends BillAdjustment {
  public static final char CHARGE_LEVEL_ACCOUNT = 'B';
  public static final char CHARGE_LEVEL_SUBSCRIBER = 'C';
  public static final char CHARGE_LEVEL_ALL='*';

  /**
    * Returns the product type code this charge can be applied to.
    *
    * @return String
    */
  public String getProductType();

  /**
    * Returns the charge (Account or Subscriber) level code.
    *
    * @return char -- One of the values CHARGE_LEVEL_ACCOUNT or CHARGE_LEVEL_SUBSCRIBER
    */
  public char getLevel();

  /**
    * Returns the balance impact code.  This indicates whether the charge will apply to the balance
    * immediantly or if it will apply the next time the billing process runs.
    *
    * @return char -- One of the values:  BillAdjustment.BALANCE_IMPACT_BILL_RUN or BillAdjustment.BALANCE_IMPACT_IMMEDIANT.
    */
  public char getBalanceImpact();
}