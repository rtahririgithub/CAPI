/**
 * Title:        ChargeInfo<p>
 * Description:  The ChargeInfo holds all attributes for a charge.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.framework.info;

import com.telus.api.*;
import com.telus.api.account.*;
import java.util.*;


public class ChargeInfo extends Info implements Charge {

  static final long serialVersionUID = 1L;

  private String subscriberId;
  private String productType;
  private String text;
  private double amount;
  private int ban;
  private String chargeCode;
  private Date effectiveDate;
  private boolean prepaid;
  private double id;
  private String featureCode;
  private String featureRevenueCode;
  private String serviceCode;  
  private boolean balanceImpactFlag;
  private java.util.Date creationDate;
  private String reasonCode;
  private double PSTAmount;
  private double GSTAmount;
  private double HSTAmount;
  private boolean balanceIgnoreFlag;
  private int operatorId;
  private boolean billed;
  private char approvalStatus;
  private int billSequenceNo;
  private Date periodCoverageStartDate;
  private Date periodCoverageEndDate;
  private double roamingTaxAmount;
  private boolean isGSTExempt;
  private boolean isHSTExempt;
  private boolean isPSTExempt;
  private boolean isRoamingTaxExempt;
  
  public ChargeInfo() {
  }

  public String getSubscriberId() {
    return subscriberId;
  }

  public void setSubscriberId(String newSubscriberId) {
    subscriberId = newSubscriberId;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String newProductType) {
    productType = newProductType;
  }

  public String getText() {
    return text;
  }

  public void setText(String newText) {
    text = newText;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double newAmount) {
    amount = newAmount;
  }
  
  public Date getPeriodCoverageStartDate() {
	  return periodCoverageStartDate;
  }
  
  public void setPeriodCoverageStartDate(Date startDate) {
	  this.periodCoverageStartDate = startDate;
  }
  
  public Date getPeriodCoverageEndDate() {
	  return periodCoverageEndDate;
  }
  
  public void setPeriodCoverageEndDate(Date endDate) {
	  this.periodCoverageEndDate = endDate;
  }

  public boolean isSubscriberLevel() {
    if (subscriberId == null || productType == null)  return false;
    if (subscriberId.trim().equals("") || productType.trim().equals("")) return false;
    return true;
  }

  public void setBan(int ban) {
    this.ban = ban;
  }
  public int getBan() {
    return ban;
  }
  public void setChargeCode(String chargeCode) {
    this.chargeCode = chargeCode;
  }
  public String getChargeCode() {
    return chargeCode;
  }
  public void setEffectiveDate(Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }
  public Date getEffectiveDate() {
    return effectiveDate;
  }

  public boolean isPrepaid() {
    return prepaid;
  }
  public void setPrepaid(boolean prepaid) {
    this.prepaid = prepaid;
  }
  public double getId() {
    return id;
  }
  public String getFeatureCode() {
    return featureCode;
  }
  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }
  public String getFeatureRevenueCode() {
    return featureRevenueCode;
  }
  public void setFeatureRevenueCode(String featureRevenueCode) {
    this.featureRevenueCode = featureRevenueCode;
  }
  public String getServiceCode() {
    return serviceCode;
  }
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }  
  public boolean isBalanceImpactFlag() {
    return balanceImpactFlag;
  }
  public void setBalanceImpactFlag(boolean balanceImpactFlag) {
    this.balanceImpactFlag = balanceImpactFlag;
  }
  public void setBalanceImpactFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'I')
      balanceImpactFlag = true;
  }
  public java.util.Date getCreationDate() {
    return creationDate;
  }
  public void setCreationDate(java.util.Date creationDate) {
    this.creationDate = creationDate;
  }
  public String getReasonCode() {
    return reasonCode;
  }
  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }
  public double getPSTAmount() {
    return PSTAmount;
  }
  public void setPSTAmount(double PSTAmount) {
    this.PSTAmount = PSTAmount;
  }
  public double getGSTAmount() {
    return GSTAmount;
  }
  public void setGSTAmount(double GSTAmount) {
    this.GSTAmount = GSTAmount;
  }
  public double getHSTAmount() {
    return HSTAmount;
  }
  public void setHSTAmount(double HSTAmount) {
    this.HSTAmount = HSTAmount;
  }
  public char getApprovalStatus() {
    return approvalStatus;
  }
  public void setApprovalStatus(char approvalStatus) {
    this.approvalStatus = approvalStatus;
  }
  public void setApprovalStatus(String value) {
    if (value != null && value.length() > 0)
    approvalStatus = value.charAt(0);
  }
  public boolean isBalanceIgnoreFlag() {
    return balanceIgnoreFlag;
  }
  public void setBalanceIgnoreFlag(boolean balanceIgnoreFlag) {
    this.balanceIgnoreFlag = balanceIgnoreFlag;
  }
  public void setBalanceIgnoreFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'Y')
      balanceIgnoreFlag = true;
  }
  public int getOperatorId() {
    return operatorId;
  }
  public void setOperatorId(int operatorId) {
    this.operatorId = operatorId;
  }
  public boolean isBilled() {
    return billed;
  }
  public void setBilled(boolean billed) {
    this.billed = billed;
  }

  public double apply(boolean overrideUserLimit) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public double apply() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void delete(String adjustmentReasonCode, String memoText, boolean overrideUserLimit) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void delete(String adjustmentReasonCode, String memoText) throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public Credit[] getRelatedCredits() throws TelusAPIException{
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("ChargeInfo:{\n");
    s.append("    id=[").append(id).append("]\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    chargeCode=[").append(chargeCode).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    text=[").append(text).append("]\n");
    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    prepaid=[").append(prepaid).append("]\n");
    s.append("    featureCode=[").append(featureCode).append("]\n");
    s.append("    featureRevenueCode=[").append(featureRevenueCode).append("]\n");
    s.append("    serviceCode=[").append(serviceCode).append("]\n");
    s.append("    balanceImpactFlag=[").append(balanceImpactFlag).append("]\n");
    s.append("    creationDate=[").append(creationDate).append("]\n");
    s.append("    reasonCode=[").append(reasonCode).append("]\n");
    s.append("    PSTAmount=[").append(PSTAmount).append("]\n");
    s.append("    GSTAmount=[").append(GSTAmount).append("]\n");
    s.append("    HSTAmount=[").append(HSTAmount).append("]\n");
    s.append("    approvalStatus=[").append(approvalStatus).append("]\n");
    s.append("    balanceIgnoreFlag=[").append(balanceIgnoreFlag).append("]\n");
    s.append("    operatorId=[").append(operatorId).append("]\n");
    s.append("    billed=[").append(billed).append("]\n");
    s.append("    billSequenceNo=[").append(billSequenceNo).append("]\n");
    s.append("    periodCoverageStartDate=[").append(periodCoverageStartDate).append("]\n");
    s.append("    periodCoverageEndDate=[").append(periodCoverageEndDate).append("]\n");
    s.append("}");

    return s.toString();
  }
  public void setId(double id) {
    this.id = id;
  }
  public int getBillSequenceNo() {
    return billSequenceNo;
  }
  public void setBillSequenceNo(int billSequenceNo) {
    this.billSequenceNo = billSequenceNo;
  }

public boolean isGSTExempt() {
	return isGSTExempt;
}

public void setGSTExempt(boolean isGSTExempt) {
	this.isGSTExempt = isGSTExempt;
}

public boolean isHSTExempt() {
	return isHSTExempt;
}

public void setHSTExempt(boolean isHSTExempt) {
	this.isHSTExempt = isHSTExempt;
}

public boolean isPSTExempt() {
	return isPSTExempt;
}

public void setPSTExempt(boolean isPSTExempt) {
	this.isPSTExempt = isPSTExempt;
}

public boolean isRoamingTaxExempt() {
	return isRoamingTaxExempt;
}

public void setRoamingTaxExempt(boolean isRoamingTaxExempt) {
	this.isRoamingTaxExempt = isRoamingTaxExempt;
}

public double getRoamingTaxAmount() {
	return roamingTaxAmount;
}

public void setRoamingTaxAmount(double roamingTaxAmount) {
	this.roamingTaxAmount = roamingTaxAmount;
}

}

