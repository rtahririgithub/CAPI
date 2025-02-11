/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.BillAdjustment;
import com.telus.eas.framework.info.Info;

public class BillAdjustmentInfo extends Info implements BillAdjustment {

   static final long serialVersionUID = 1L;

  private String code = "";
  private String description = "";
  private String descriptionFrench = "";

  private boolean manualCharge = false;
  private double amount = 0.0;
  private boolean amountOverrideable = true;

  public BillAdjustmentInfo() {
  }

  public BillAdjustmentInfo(String code, String desc, String descFr, boolean isManual, double amount, boolean isAmountOverrideable) {
    this.code = code;
    this.description = desc;
    this.descriptionFrench = descFr;
    this.manualCharge = isManual;
    this.amount = amount;
    this.amountOverrideable = isAmountOverrideable;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  /**
    * Returns true if this adjustment can be manually applied (applied by a user) or false
    * if this adjustment is applied automatically (by the system)
    *
    * @return boolean
    */
  public boolean isManualCharge() {
    return manualCharge;
  }

  /**
    * Returns the amount for this adjustment.
    *
    * This amount may or may not be able to be changed by the user according to the value
    * returned by isAmountOveridable
    *
    * @return double
    */
  public double getAmount() {
    return amount;
  }

  /**
    * Returns true if the user is allowed to overide the amount returned by getAmount, otherwise
    * returns false
    *
    * @return boolean
    */
  public boolean isAmountOverrideable() {
    return amountOverrideable;
  }

  public String toString()
  {
      StringBuffer s = new StringBuffer(128);

      s.append("ChargeTypeInfo:[\n");
      s.append("    description=[").append(description).append("]\n");
      s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
      s.append("    code=[").append(code).append("]\n");
      s.append("    manualCharge=[").append(manualCharge).append("]\n");
      s.append("    amount=[").append(amount).append("]\n");
      s.append("    amountOverrideable=[").append(amountOverrideable).append("]\n");
      s.append("]");

      return s.toString();
  }
  public void setCode(String code) {
    this.code = code;
  }
  public void setAmount(double amount) {
    this.amount = amount;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }
  public void setManualCharge(boolean manualCharge) {
    this.manualCharge = manualCharge;
  }
  public void setAmountOverrideable(boolean amountOverrideable) {
    this.amountOverrideable = amountOverrideable;
  }
}



