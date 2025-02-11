package com.telus.eas.hpa.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class TransactionCrossReferenceTypeInfo extends Info {

	static final long serialVersionUID = 1L;

	private int applicationCrossReferenceWorkID;
	private int rewardTransactionID;
	private int serviceRequestHeaderID;
	private int applicationID;
	private int transactionID;
	private int transactionItemID;
	private String teaTransactionTypeID;
	private String organisationTransactionNumber;
	private String createUserID;
	private Date serviceRequestTransactionDate;
	private Date transactionDate;

	public int getApplicationCrossReferenceWorkID() {
		return applicationCrossReferenceWorkID;
	}

	public void setApplicationCrossReferenceWorkID(int applicationCrossReferenceWorkID) {
		this.applicationCrossReferenceWorkID = applicationCrossReferenceWorkID;
	}

	public int getRewardTransactionID() {
		return rewardTransactionID;
	}

	public void setRewardTransactionID(int rewardTransactionID) {
		this.rewardTransactionID = rewardTransactionID;
	}

	public int getServiceRequestHeaderID() {
		return serviceRequestHeaderID;
	}

	public void setServiceRequestHeaderID(int serviceRequestHeaderID) {
		this.serviceRequestHeaderID = serviceRequestHeaderID;
	}

	public int getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(int applicationID) {
		this.applicationID = applicationID;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public int getTransactionItemID() {
		return transactionItemID;
	}

	public void setTransactionItemID(int transactionItemID) {
		this.transactionItemID = transactionItemID;
	}

	public String getTeaTransactionTypeID() {
		return teaTransactionTypeID;
	}

	public void setTeaTransactionTypeID(String teaTransactionTypeID) {
		this.teaTransactionTypeID = teaTransactionTypeID;
	}

	public String getOrganisationTransactionNumber() {
		return organisationTransactionNumber;
	}

	public void setOrganisationTransactionNumber(String organisationTransactionNumber) {
		this.organisationTransactionNumber = organisationTransactionNumber;
	}

	public String getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(String createUserID) {
		this.createUserID = createUserID;
	}

	public Date getServiceRequestTransactionDate() {
		return serviceRequestTransactionDate;
	}

	public void setServiceRequestTransactionDate(Date serviceRequestTransactionDate) {
		this.serviceRequestTransactionDate = serviceRequestTransactionDate;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("TransactionCrossReferenceTypeInfo: {\n");
		s.append("    applicationCrossReferenceWorkID=[").append(getApplicationCrossReferenceWorkID()).append("]\n");
		s.append("    rewardTransactionID=[").append(getRewardTransactionID()).append("]\n");
		s.append("    serviceRequestHeaderID=[").append(getServiceRequestHeaderID()).append("]\n");
		s.append("    applicationID=[").append(getApplicationID()).append("]\n");
		s.append("    transactionID=[").append(getTransactionID()).append("]\n");
		s.append("    transactionItemID=[").append(getTransactionItemID()).append("]\n");
		s.append("    teaTransactionTypeID=[").append(getTeaTransactionTypeID()).append("]\n");
		s.append("    organisationTransactionNumber=[").append(getOrganisationTransactionNumber()).append("]\n");
		s.append("    createUserID=[").append(getCreateUserID()).append("]\n");
		s.append("    serviceRequestTransactionDate=[").append(getServiceRequestTransactionDate()).append("]\n");
		s.append("    transactionDate=[").append(getTransactionDate()).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}