/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;


public class PrepaidAdjustmentReasonInfo extends AdjustmentReasonInfo implements PrepaidAdjustmentReason {

  static final long serialVersionUID = 1L;

  private String equipmentType;
  private boolean transactionIdRequired;
  private boolean creditAdjustment;
  private boolean debitAdjustment;
  private double minimumBalance;
  private double minimumAdjustmentAmount;
  private double maximumAdjustmentAmount;

  public PrepaidAdjustmentReasonInfo() {
  }

   public void setEquipmentType(String equipmentType) {
    this.equipmentType = equipmentType;
  }
  public String getEquipmentType() {
    return equipmentType;
  }
  public void setTransactionIdRequired(boolean transactionIdRequired) {
    this.transactionIdRequired = transactionIdRequired;
  }
  public boolean isTransactionIdRequired() {
    return transactionIdRequired;
  }
  public void setCreditAdjustment(boolean creditAdjustment) {
    this.creditAdjustment = creditAdjustment;
  }
  public boolean isCreditAdjustment() {
    return creditAdjustment;
  }
  public void setDebitAdjustment(boolean debitAdjustment) {
    this.debitAdjustment = debitAdjustment;
  }
  public boolean isDebitAdjustment() {
    return debitAdjustment;
  }
  public double getMinimumBalance() {
	    return minimumBalance;
  }
  public void setMinimumBalance(double minimumBalance) {
	    this.minimumBalance = minimumBalance;
  }
  public double getMinimumAdjustmentAmount() {
	    return minimumAdjustmentAmount;
  }
  public void setMinimumAdjustmentAmount(double minimumAdjustmentAmount) {
	    this.minimumAdjustmentAmount = minimumAdjustmentAmount;
  }
  public double getMaximumAdjustmentAmount() {
	    return maximumAdjustmentAmount;
  }
  public void setMaximumAdjustmentAmount(double maximumAdjustmentAmount) {
	    this.maximumAdjustmentAmount = maximumAdjustmentAmount;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("PrepaidEventTypeInfo:[\n");
        s.append("    super=[").append(super.toString()).append("]\n");
        s.append("    equipmentType=[").append(equipmentType).append("]\n");
        s.append("    transactionIdRequired=[").append(transactionIdRequired).append("]\n");
        s.append("    creditAdjustment=[").append(creditAdjustment).append("]\n");
        s.append("    debitAdjustment=[").append(debitAdjustment).append("]\n");
        s.append("    minimumBalance=[").append(minimumBalance).append("]\n");
        s.append("    minimumAdjustmentAmount=[").append(minimumAdjustmentAmount).append("]\n");
        s.append("    maximumAdjustmentAmount=[").append(maximumAdjustmentAmount).append("]\n");

        s.append("]");

        return s.toString();
    }


}