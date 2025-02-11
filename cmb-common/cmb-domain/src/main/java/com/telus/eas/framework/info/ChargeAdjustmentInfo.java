package com.telus.eas.framework.info;

import java.io.Serializable;
import java.util.Date;

public class ChargeAdjustmentInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int ban;
	private String subscriberId;
	private String productType;
	private double chargeAmount;
	private String chargeCode;
	private String chargeMemoText;
	private Date chargeEffectiveDate;
	
	private double adjustmentAmount;
	private String adjustmentReasonCode;
	private String adjustmentMemoText;
	private boolean bypassAuthorization;
	private boolean authorizedToCreateFollowUp;
	
	private double chargeSequenceNumber;
	private double adjustmentId;
	private boolean isChargeApplied;
	private String errorCode;
	private String errorMessage;
	private String exceptionTrace;
	private String transactionId;
	private Date timestamp;
		
	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public String getChargeMemoText() {
		return chargeMemoText;
	}

	public void setChargeMemoText(String chargeMemoText) {
		this.chargeMemoText = chargeMemoText;
	}

	public Date getChargeEffectiveDate() {
		return chargeEffectiveDate;
	}

	public void setChargeEffectiveDate(Date chargeEffectiveDate) {
		this.chargeEffectiveDate = chargeEffectiveDate;
	}

	public boolean isAuthorizedToCreateFollowUp() {
		return authorizedToCreateFollowUp;
	}

	public void setAuthorizedToCreateFollowUp(boolean authorizedToCreateFollowUp) {
		this.authorizedToCreateFollowUp = authorizedToCreateFollowUp;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExceptionTrace() {
		return exceptionTrace;
	}

	public void setExceptionTrace(String exceptionTrace) {
		this.exceptionTrace = exceptionTrace;
	}

	public double getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public void setAdjustmentAmount(double adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}

	public String getAdjustmentReasonCode() {
		return adjustmentReasonCode;
	}

	public void setAdjustmentReasonCode(String adjustmentReasonCode) {
		this.adjustmentReasonCode = adjustmentReasonCode;
	}

	public String getAdjustmentMemoText() {
		return adjustmentMemoText;
	}

	public void setAdjustmentMemoText(String adjustmentMemoText) {
		this.adjustmentMemoText = adjustmentMemoText;
	}

	public boolean isBypassAuthorization() {
		return bypassAuthorization;
	}

	public void setBypassAuthorization(boolean bypassAuthorization) {
		this.bypassAuthorization = bypassAuthorization;
	}

	public boolean isChargeApplied() {
		return isChargeApplied;
	}
	public void setChargeApplied(boolean isChargeApplied) {
		this.isChargeApplied = isChargeApplied;
	}
	public double getChargeSequenceNumber() {
		return chargeSequenceNumber;
	}
	public void setChargeSequenceNumber(double chargeSequenceNumber) {
		this.chargeSequenceNumber = chargeSequenceNumber;
	}
	public double getAdjustmentId() {
		return adjustmentId;
	}
	public void setAdjustmentId(double adjustmentId) {
		this.adjustmentId = adjustmentId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public void copy(ChargeAdjustmentInfo chargeAdjustmentInfo) {
		this.adjustmentAmount = chargeAdjustmentInfo.getAdjustmentAmount();
		this.adjustmentMemoText = chargeAdjustmentInfo.getAdjustmentMemoText();
		this.adjustmentReasonCode = chargeAdjustmentInfo.getAdjustmentReasonCode();
		this.authorizedToCreateFollowUp = chargeAdjustmentInfo.isAuthorizedToCreateFollowUp();
		this.ban = chargeAdjustmentInfo.getBan();
		this.bypassAuthorization = chargeAdjustmentInfo.isBypassAuthorization();
		this.chargeAmount = chargeAdjustmentInfo.getChargeAmount();
		this.chargeCode = chargeAdjustmentInfo.getChargeCode();
		this.chargeEffectiveDate = chargeAdjustmentInfo.getChargeEffectiveDate();
		this.chargeMemoText = chargeAdjustmentInfo.getChargeMemoText();
		this.chargeSequenceNumber = chargeAdjustmentInfo.getChargeSequenceNumber();
		this.productType = chargeAdjustmentInfo.getProductType();
		this.subscriberId = chargeAdjustmentInfo.getSubscriberId();
	}

	public String toString() {
		return "ChargeAdjustmentInfo [ban=" + ban + ", subscriberId="
				+ subscriberId + ", productType=" + productType
				+ ", chargeAmount=" + chargeAmount + ", chargeCode="
				+ chargeCode + ", chargeMemoText=" + chargeMemoText
				+ ", chargeEffectiveDate=" + chargeEffectiveDate
				+ ", adjustmentAmount=" + adjustmentAmount
				+ ", adjustmentReasonCode=" + adjustmentReasonCode
				+ ", adjustmentMemoText=" + adjustmentMemoText
				+ ", bypassAuthorization=" + bypassAuthorization
				+ ", authorizedToCreateFollowUp=" + authorizedToCreateFollowUp
				+ ", chargeSequenceNumber=" + chargeSequenceNumber
				+ ", adjustmentId=" + adjustmentId + ", isChargeApplied="
				+ isChargeApplied + ", errorCode=" + errorCode
				+ ", errorMessage=" + errorMessage + ", exceptionTrace="
				+ exceptionTrace + ", transactionId=" + transactionId
				+ ", timestamp=" + timestamp + "]";
	}
	
}
