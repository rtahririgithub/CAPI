package com.telus.eas.hpa.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.MultilingualTextInfo;

public class RewardTransactionTypeInfo extends Info {

	static final long serialVersionUID = 1L;

	private String applicationID;
	private String transactionType;
	private String reasonCode;
	private double transactionAmount;
	private double accountBalance;
	private int transactionID;
	private int rewardAccountID;
	private Date transactionDate;
	private String catalogueItemID;
	private String offerCode;
	private String creditClass;
	private List<MultilingualTextInfo> reasonCodeDescriptionList;
	private List<MultilingualTextInfo> transactionTypeDescriptionList;
	private TransactionCrossReferenceTypeInfo crossReference;

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getRewardAccountID() {
		return rewardAccountID;
	}

	public void setRewardAccountID(int rewardAccountID) {
		this.rewardAccountID = rewardAccountID;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getCatalogueItemID() {
		return catalogueItemID;
	}

	public void setCatalogueItemID(String catalogueItemID) {
		this.catalogueItemID = catalogueItemID;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public String getCreditClass() {
		return creditClass;
	}

	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	public List<MultilingualTextInfo> getReasonCodeDescriptionList() {
		return reasonCodeDescriptionList;
	}

	public void setReasonCodeDescriptionList(List<MultilingualTextInfo> reasonCodeDescriptionList) {
		this.reasonCodeDescriptionList = reasonCodeDescriptionList;
	}

	public List<MultilingualTextInfo> getTransactionTypeDescriptionList() {
		return transactionTypeDescriptionList;
	}

	public void setTransactionTypeDescriptionList(List<MultilingualTextInfo> transactionTypeDescriptionList) {
		this.transactionTypeDescriptionList = transactionTypeDescriptionList;
	}

	public TransactionCrossReferenceTypeInfo getCrossReference() {
		return crossReference;
	}

	public void setCrossReference(TransactionCrossReferenceTypeInfo crossReference) {
		this.crossReference = crossReference;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("RewardTransactionTypeInfo: {\n");
		s.append("    applicationID=[").append(getApplicationID()).append("]\n");
		s.append("    transactionType=[").append(getTransactionType()).append("]\n");
		s.append("    reasonCode=[").append(getReasonCode()).append("]\n");
		s.append("    transactionAmount=[").append(getTransactionAmount()).append("]\n");
		s.append("    accountBalance=[").append(getAccountBalance()).append("]\n");
		s.append("    transactionID=[").append(getTransactionID()).append("]\n");
		s.append("    rewardAccountID=[").append(getRewardAccountID()).append("]\n");
		s.append("    transactionDate=[").append(getTransactionDate()).append("]\n");
		s.append("    catalogueItemID=[").append(getCatalogueItemID()).append("]\n");
		s.append("    offerCode=[").append(getOfferCode()).append("]\n");
		s.append("    creditClass=[").append(getCreditClass()).append("]\n");
		s.append("    reasonCodeDescriptionList=[\n");
		for (MultilingualTextInfo info : getReasonCodeDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    transactionTypeDescriptionList=[\n");
		for (MultilingualTextInfo info : getTransactionTypeDescriptionList()) {
			s.append(info).append("\n");
		}
		s.append("    crossReference=[").append(getCrossReference()).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}