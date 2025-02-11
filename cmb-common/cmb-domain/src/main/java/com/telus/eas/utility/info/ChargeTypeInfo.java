/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class ChargeTypeInfo extends BillAdjustmentInfo implements ChargeType {

  static final long serialVersionUID = 1L;

  private String productType = "";
  private char level = '\0';
  private char balanceImpact = '\0';

  public ChargeTypeInfo() {
  }

  public ChargeTypeInfo(
    String code,
    String desc,
    String descFr,
    String productType,
    boolean isManual,
    double amount,
    boolean isAmountOverrideable,
    char level,
    char balanceImpact
  ) {
    super(code, desc, descFr, isManual, amount, isAmountOverrideable);

    this.productType = productType;
    this.level = level;
    this.balanceImpact = balanceImpact;
  }

  /**
    * Returns the product type code this charge can be applied to.
    *
    * @return String
    */
  public String getProductType() {
    return productType;
  }

  /**
    * Returns the charge (Account or Subscriber) level code.
    *
    * @return char -- One of the values CHARGE_LEVEL_ACCOUNT or CHARGE_LEVEL_SUBSCRIBER
    */
  public char getLevel() {
    return level;
  }

  /**
    * Returns the balance impact code.  This indicates whether the charge will apply to the balance
    * immediantly or if it will apply the next time the billing process runs.
    *
    * @return char -- One of the values:  BillAdjustment.BALANCE_IMPACT_BILL_RUN or BillAdjustment.BALANCE_IMPACT_IMMEDIANT.
    */
  public char getBalanceImpact() {
    return balanceImpact;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("ChargeTypeInfo:[\n");
      s.append("    BillAdjustment=[").append(super.toString()).append("]\n");
      s.append("    productType=[").append(productType).append("]\n");
      s.append("    level=[").append(level).append("]\n");
      s.append("    balanceImpact=[").append(balanceImpact).append("]\n");
      s.append("]");

      return s.toString();
  }
}