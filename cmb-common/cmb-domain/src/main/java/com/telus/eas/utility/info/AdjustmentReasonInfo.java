/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import java.util.Date;

import com.telus.api.reference.AdjustmentReason;
import com.telus.api.reference.ServiceSummary;

public class AdjustmentReasonInfo extends BillAdjustmentInfo implements AdjustmentReason {

  static final long serialVersionUID = 1L;

  private String adjustmentLevelCode;
  private String adjustmentActivityCode;
  private String adjustmentTaxIndicator;
  public  String adjustmentCategory;
  private String typeCode;

  public AdjustmentReasonInfo() {
  }

public String getTypeCode() {
	return typeCode;
}
public void setTypeCode(String typeCode) {
	this.typeCode = typeCode;
}
public String getAdjustmentCategory() {
	return adjustmentCategory;
}
public void setAdjustmentCategory(String adjustmentCategory) {
	this.adjustmentCategory = adjustmentCategory;
}
  public AdjustmentReasonInfo(String code, String desc, String descFr, boolean isManual, double amount, boolean isAmountOverrideable, String adjustmentLevelCode, String adjustmentActivityCode, String adjustmentTaxIndicator,String adjustmentCategory ) {
    super(code, desc, descFr, isManual, amount, isAmountOverrideable);
    setAdjustmentLevelCode(adjustmentLevelCode);
    setAdjustmentActivityCode(adjustmentActivityCode);
    setAdjustmentTaxIndicator(adjustmentTaxIndicator);
    setAdjustmentCategory(adjustmentCategory);
  }
  public String getAdjustmentLevelCode() {
    return adjustmentLevelCode;
  }
  public void setAdjustmentLevelCode(String adjustmentLevelCode) {
    this.adjustmentLevelCode = adjustmentLevelCode;
  }
  public String getAdjustmentActivityCode() {
    return adjustmentActivityCode;
  }
  public void setAdjustmentActivityCode(String adjustmentActivityCode) {
    this.adjustmentActivityCode = adjustmentActivityCode;
  }
  public String getAdjustmentTaxIndicator() {
    return adjustmentTaxIndicator;
  }
  public void setAdjustmentTaxIndicator(String adjustmentTaxIndicator) {
    this.adjustmentTaxIndicator = adjustmentTaxIndicator;
  }

  private int frequency;
  private int maxNumOfRecurringCredits;
  private Date expiryDate;
  private boolean promotional;

  // Recurring Credits
  public int getFrequency() {
    return frequency;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public int getMaxNumberOfRecurringCredits() {
    return maxNumOfRecurringCredits;
  }

  public void setMaxNumberOfRecurringCredits(int maxNumOfRecurringCredits) {
    this.maxNumOfRecurringCredits = maxNumOfRecurringCredits;
  }

  public java.util.Date getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public boolean isPromotional() {
    // this method is commented out at API
    return promotional;
  }

  public void setPromotional(boolean promotional) {
    this.promotional = promotional;
  }

  public boolean isRecurring() {
    return ACTIVITY_CODE_RECURRING.equalsIgnoreCase(getAdjustmentActivityCode());
  }

  public boolean isAssociated(ServiceSummary service, int term, boolean current) {
    throw new java.lang.UnsupportedOperationException("not implemented");
  }

  public void setAssociated(ServiceSummary service, int term, boolean current) {
    throw new java.lang.UnsupportedOperationException("not implemented");
  }

  public boolean isAssociated(String province, boolean current) {
    throw new java.lang.UnsupportedOperationException("not implemented");
  }

  public void setAssociated(String province, boolean current) {
    throw new java.lang.UnsupportedOperationException("not implemented");
  }

}
