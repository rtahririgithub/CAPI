/**
 * Title:        CreditInfo<p>
 * Description:  The CreditInfo holds all attributes for a credit.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

package com.telus.eas.framework.info;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.eas.account.info.*;
import com.telus.api.reference.*;
import java.util.*;

public class CreditInfo extends Info implements Credit {


  static final long serialVersionUID = 1L;

  private int ban;
  private String subscriberId;
  private String productType;
  private String text;
  private double amount;
  private String reasonCode;
  private Date effectiveDate;
  private boolean balanceImpactFlag;
  private boolean prepaid;
  private String phoneNumber;
  private final TaxSummaryInfo taxSummary = new TaxSummaryInfo();
  private int id;
  private java.util.Date creationDate;
  private String activityCode;
  private boolean balanceIgnoreFlag;
  private int operatorId;
  private String SOC;
  private String featureCode;
  private boolean billed;
  private double relatedChargeId;
  private char approvalStatus;
  private int numberOfRecurring;
  private boolean isRecurring;
  private boolean reverseAllRecurring;
  private boolean bypassAuthorization;
  private char taxOption;
  private Charge relatedCharge;
  private double roamingTaxAmount;

  public CreditInfo() {
      setTaxOption(Credit.TAX_OPTION_NO_TAX);
  }
  
  public CreditInfo(char taxChar) {
    if (taxChar == Credit.TAX_OPTION_ALL_TAXES) {
      setTaxOption(Credit.TAX_OPTION_ALL_TAXES);
    } else if (taxChar == Credit.TAX_OPTION_GST_ONLY) {
      setTaxOption(Credit.TAX_OPTION_GST_ONLY);
    } else {
      setTaxOption(Credit.TAX_OPTION_NO_TAX);
    }
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

  public boolean isSubscriberLevel() {
    if (subscriberId == null || productType == null) {
      return false;
    }
    if (subscriberId.trim().equals("") || productType.trim().equals("")) {
      return false;
    }
    return true;
  }

  public void setBan(int ban) {
    this.ban = ban;
  }

  public int getBan() {
    return ban;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = Info.padTo(reasonCode, ' ', 6);
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public void setEffectiveDate(Date effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

  public Date getEffectiveDate() {
    return effectiveDate;
  }

  public void setBalanceImpactFlag(boolean balanceImpactFlag) {
    this.balanceImpactFlag = balanceImpactFlag;
  }

  public void setBalanceImpactFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'I') {
      balanceImpactFlag = true;
    }
  }

  public boolean isBalanceImpactFlag() {
    return balanceImpactFlag;
  }

  public void setPrepaid(boolean prepaid) {
    this.prepaid = prepaid;
  }

  public boolean isPrepaid() {
    return prepaid;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public TaxSummaryInfo getTaxSummary() {
    return taxSummary;
  }

  /**
   * Deprecated in favour of getTaxOption().  isTaxable() means all taxes will be applied.  
   * 
   * @return boolean - returns true if getTaxOption returns TAX_OPTION_ALL_TAXES, otherwise returns false.
   * 
   * @see #getTaxOption()
   */
  public boolean isTaxable() {
    return (getTaxOption() == Credit.TAX_OPTION_ALL_TAXES);
  }

  public double getPSTAmount() {
    return taxSummary.getPSTAmount();
  }

  public double getGSTAmount() {
    return taxSummary.getGSTAmount();
  }

  public double getHSTAmount() {
    return taxSummary.getHSTAmount();
  }

  public double getTotalAmount() {
    return (getTaxOption() == Credit.TAX_OPTION_ALL_TAXES) ? taxSummary.getTotal() : 0.0 + amount;
  }

  public String getGSTAdjustmentReasonCode() {
    return taxSummary.getGSTAdjustmentReasonCode();
  }

  public String getPSTAdjustmentReasonCode() {
    return taxSummary.getPSTAdjustmentReasonCode();
  }

  public String getHSTAdjustmentReasonCode() {
    return taxSummary.getHSTAdjustmentReasonCode();
  }

  public int getId() {
    return id;
  }

  public java.util.Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(java.util.Date creationDate) {
    this.creationDate = creationDate;
  }

  public String getActivityCode() {
    return activityCode;
  }

  public void setActivityCode(String activityCode) {
    this.activityCode = activityCode;
  }

  public char getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(char approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public void setApprovalStatus(String value) {
    if (value != null && value.length() > 0) {
      approvalStatus = value.charAt(0);
    }
  }

  public boolean isBalanceIgnoreFlag() {
    return balanceIgnoreFlag;
  }

  public void setBalanceIgnoreFlag(boolean balanceIgnoreFlag) {
    this.balanceIgnoreFlag = balanceIgnoreFlag;
  }

  public void setBalanceIgnoreFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'Y') {
      balanceIgnoreFlag = true;
    }
  }

  public int getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(int operatorId) {
    this.operatorId = operatorId;
  }

  public String getSOC() {
    return SOC;
  }

  public void setSOC(String SOC) {
    this.SOC = SOC;
  }

  public String getFeatureCode() {
    return featureCode;
  }

  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }

  public boolean isBilled() {
    return billed;
  }

  public void setBilled(boolean billed) {
    this.billed = billed;
  }

  public double getRelatedChargeId() {
    return relatedChargeId;
  }

  public void setRelatedChargeId(double relatedChargeId) {
    this.relatedChargeId = relatedChargeId;
  }
  
  public char getTaxOption() {
    return taxOption;
  }
  
  public void setTaxOption(char taxOption) {
    this.taxOption = taxOption;
  }
  
  public void apply(boolean overrideUserLimit) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void apply() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void reverse(String reversalReasonCode, String memoText,
                      boolean overrideUserLimit) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void reverse(String reversalReasonCode, String memoText) throws
      TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("CreditInfo:[\n");
    s.append("    id=[").append(id).append("]\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    text=[").append(text).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    reasonCode=[").append(reasonCode).append("]\n");
    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
    s.append("    balanceImpactFlag=[").append(balanceImpactFlag).append("]\n");
    s.append("    prepaid=[").append(prepaid).append("]\n");
    s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
    s.append("    taxSummary=[").append(taxSummary).append("]\n");
    s.append("    taxOption=[").append(taxOption).append("]\n");
    s.append("    creationDate=[").append(creationDate).append("]\n");
    s.append("    activityCode=[").append(activityCode).append("]\n");
    s.append("    approvalStatus=[").append(approvalStatus).append("]\n");
    s.append("    balanceIgnoreFlag=[").append(balanceIgnoreFlag).append("]\n");
    s.append("    operatorId=[").append(operatorId).append("]\n");
    s.append("    billed=[").append(billed).append("]\n");
    s.append("    relatedChargeId=[").append(relatedChargeId).append("]\n");
    s.append("    SOC=[").append(SOC).append("]\n");
    s.append("    featureCode=[").append(featureCode).append("]\n");
    s.append("]");

    return s.toString();
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Return AdjustmentReason, a reference object, associated with the Credit.
   *
   * @return AdjustmentReason
   */
  public AdjustmentReason getAdjustmentReason() {
    throw new UnsupportedOperationException("method not implemented here");
  }

  /**
   * For an open recurring credit with predefined frequency, the
   * calling program returns the number of recurring credits to be applied.
   *
   *@return numberOfRecurring int
   */
  public int getNumberOfRecurring() {
    return numberOfRecurring;
  }

  /**
   * For an open recurring credit with predefined frequency, the
   * calling program defines the number of recurring credits to be applied.
   *
   * @param numberOfRecurring int
   */
  public void setNumberOfRecurring(int numberOfRecurring) {
    this.numberOfRecurring = numberOfRecurring;

  }

  /**
   * Set to <code>true</code> if reverse for all recurring credits desired.
   * @param reverseAllRecurring boolean
   */
  public void setReverseAllRecurring(boolean reverseAllRecurring) {
    this.reverseAllRecurring = reverseAllRecurring;
  }

  /**
   * @return Returns the isRecurring.
   */
  public boolean isRecurring() {
    return isRecurring;
  }

  /**
   * @param isRecurring The isRecurring to set.
   */
  public void setRecurring(boolean isRecurring) {
    this.isRecurring = isRecurring;
  }

  /**
   * @return Returns the reverseAllRecurring.
   */
  public boolean isReverseAllRecurring() {
    return reverseAllRecurring;
  }

  public boolean isBypassAuthorization() {
    return bypassAuthorization;
  }

  public void setBypassAuthorization(boolean bypassAuthorization) {
    this.bypassAuthorization = bypassAuthorization;
  }

	public Charge[] getRelatedCharges() throws TelusAPIException{
	    throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public void setRelatedCharge(Charge relatedCharge) {
		this.relatedCharge = relatedCharge;
	}
	
	public double getRoamingTaxAmount() {
		return roamingTaxAmount;
	}
	
	public void setRoamingTaxAmount(double roamingTaxAmount) {
		this.roamingTaxAmount = roamingTaxAmount;
	}
}
