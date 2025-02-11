/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.subscriber.info;

import com.telus.api.account.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.util.*;

/**
 * <b>TODO</b>: any modification requested should result in deprecating this class and having Prepaid web services handle the requested changes.  For futher details, 
 * refer to Client API Design.doc design document for Prepaid Real Time Rating project
 */
public class PrepaidEventHistoryInfo extends Info implements
		PrepaidEventHistory {

	static final long serialVersionUID = 1L;

	private Date eventDate;
	private PrepaidEventType eventType;
	private String debitCreditFlag;
	private double amount;
	private String cardId;
	private String creditCardNumber;
	private String referenceCode;
	private double startBalance;
	private double endBalance;
	private String userID;
	private String sourceID;
	private String transactionID;
	private String relatedTransactionID;
	private String prepaidEventTypeCode;
	private String prepaidAdjustmentReasonCode;
	private double outstandingCharge;
	private Date transactionDate;
	private double confiscatedBalance;
	private String GMTOffset;
	private String preEventStatus;
	private String postEventStatus;
	private double preEventAmount;
	private double postEventAmount;
	private String unitType;
	// -- Commented the Prepaid 5.1 rel changes -- private String discountPercentage;

	public PrepaidEventHistoryInfo() {
	}

	public Date getEventDate() {
		return eventDate;
	}

	public PrepaidEventType getEventType() {
		return eventType;
	}

	public String getDebitCreditFlag() {
		return debitCreditFlag;
	}

	public double getAmount() {
		return amount;
	}

	public String getCardId() {
		return cardId;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public double getStartBalance() {
		return startBalance;
	}

	public double getEndBalance() {
		return endBalance;
	}

	public String getUserID() {
		return userID;
	}

	public String getSourceID() {
		return sourceID;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public String getRelatedTransactionID() {
		return relatedTransactionID;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public void setEventType(PrepaidEventType eventType) {
		this.eventType = eventType;
	}

	public void setDebitCreditFlag(String debitCreditFlag) {
		this.debitCreditFlag = debitCreditFlag;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public void setRelatedTransactionID(String relatedTransactionID) {
		this.relatedTransactionID = relatedTransactionID;
	}

	public String getPrepaidEventTypeCode() {
		return prepaidEventTypeCode;
	}

	public void setPrepaidEventTypeCode(String prepaidEventTypeCode) {
		this.prepaidEventTypeCode = prepaidEventTypeCode;
	}

	public String getPrepaidAdjustmentReasonCode() {
		return prepaidAdjustmentReasonCode;
	}

	public void setPrepaidAdjustmentReasonCode(
			String prepaidAdjustmentReasonCode) {
		this.prepaidAdjustmentReasonCode = prepaidAdjustmentReasonCode;
	}

	public double getOutstandingCharge() {
		return outstandingCharge;
	}

	public void setOutstandingCharge(double outstandingCharge) {
		this.outstandingCharge = outstandingCharge;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getConfiscatedBalance() {
		return confiscatedBalance;
	}

	public void setConfiscatedBalance(double confiscatedBalance) {
		this.confiscatedBalance = confiscatedBalance;
	}

	public String getGMTOffset() {
		return GMTOffset;
	}

	public void setGMTOffset(String offset) {
		this.GMTOffset = offset;
	}

	public double getPostEventAmount() {
		return postEventAmount;
	}

	public void setPostEventAmount(double postEventAmount) {
		this.postEventAmount = postEventAmount;
	}

	public String getPostEventStatus() {
		return postEventStatus;
	}

	public void setPostEventStatus(String postEventStatus) {
		this.postEventStatus = postEventStatus;
	}

	public double getPreEventAmount() {
		return preEventAmount;
	}

	public void setPreEventAmount(double preEventAmount) {
		this.preEventAmount = preEventAmount;
	}

	public String getPreEventStatus() {
		return preEventStatus;
	}

	public void setPreEventStatus(String preEventStatus) {
		this.preEventStatus = preEventStatus;
	}
	
	public String getUnitType() {
		return unitType;
	}
	
	public void setUnitType(String pUnitType) {
		unitType = pUnitType;
	}
	/* -- Commented the Prepaid 5.1 rel changes
	public String getDiscountPercentage() {
		return discountPercentage;
	}
	
	public void setDiscountPercentage(String discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	*/
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("PrepaidEventHistoryInfo:[\n");
		s.append("    eventDate=[").append(eventDate).append("]\n");
		s.append("    eventType=[").append(eventType).append("]\n");
		s.append("    debitCreditFlag=[").append(debitCreditFlag).append("]\n");
		s.append("    amount=[").append(amount).append("]\n");
		s.append("    cardId=[").append(cardId).append("]\n");
		s.append("    creditCardNumber=[").append(creditCardNumber).append("]\n");
		s.append("    referenceCode=[").append(referenceCode).append("]\n");
		s.append("    startBalance=[").append(startBalance).append("]\n");
		s.append("    endBalance=[").append(endBalance).append("]\n");
		s.append("    userID=[").append(userID).append("]\n");
		s.append("    sourceID=[").append(sourceID).append("]\n");
		s.append("    transactionID=[").append(transactionID).append("]\n");
		s.append("    relatedTransactionID=[").append(relatedTransactionID).append("]\n");
		s.append("    prepaidEventTypeCode=[").append(prepaidEventTypeCode).append("]\n");
		s.append("    prepaidAdjustmentReasonCode=[").append(prepaidAdjustmentReasonCode).append("]\n");
		s.append("    outstandingCharge=[").append(outstandingCharge).append("]\n");
		s.append("    transactionDate=[").append(transactionDate).append("]\n");
		s.append("    confiscatedBalance=[").append(confiscatedBalance).append("]\n");
		s.append("    GMTOffset=[").append(GMTOffset).append("]\n");
		s.append("    preEventStatus=[").append(preEventStatus).append("]\n");
		s.append("    postEventStatus=[").append(postEventStatus).append("]\n");
		s.append("    preEventAmount=[").append(preEventAmount).append("]\n");
		s.append("    postEventAmount=[").append(postEventAmount).append("]\n");
		s.append("    unitType=[").append(unitType).append("]\n");
		//s.append("    discountPercentage=[").append(discountPercentage).append("]\n");
		s.append("]");

		return s.toString();
	}

}
