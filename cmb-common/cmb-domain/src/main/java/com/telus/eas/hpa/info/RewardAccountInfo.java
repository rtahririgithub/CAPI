package com.telus.eas.hpa.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;

public class RewardAccountInfo extends Info {

	static final long serialVersionUID = 1L;

	private int ban;
	private String offerCode;
	private String applicationID;
	private long subscriptionID;
	private long rewardAccountID;
	private double accountBalance;
	private String phoneNumber;
	private String statusCode;
	private String statusDescription;
	private Date activationDate;
	private OfferInstanceInfo accountDetail;
	private List<RewardTransactionTypeInfo> rewardTransactionList;

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public String getApplicationID() {
		return applicationID;
	}

	public void setApplicationID(String applicationID) {
		this.applicationID = applicationID;
	}

	public long getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(long subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public long getRewardAccountID() {
		return rewardAccountID;
	}

	public void setRewardAccountID(long rewardAccountID) {
		this.rewardAccountID = rewardAccountID;
	}

	public double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public OfferInstanceInfo getAccountDetail() {
		return accountDetail;
	}

	public void setAccountDetail(OfferInstanceInfo accountDetail) {
		this.accountDetail = accountDetail;
	}

	public List<RewardTransactionTypeInfo> getRewardTransactionList() {
		return rewardTransactionList;
	}

	public void setRewardTransactionList(List<RewardTransactionTypeInfo> rewardTransactionList) {
		this.rewardTransactionList = rewardTransactionList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("RewardAccountInfo: {\n");
		s.append("    ban=[").append(getBan()).append("]\n");
		s.append("    offerCode=[").append(getOfferCode()).append("]\n");
		s.append("    applicationID=[").append(getApplicationID()).append("]\n");
		s.append("    subscriptionID=[").append(getSubscriptionID()).append("]\n");
		s.append("    rewardAccountID=[").append(getRewardAccountID()).append("]\n");
		s.append("    accountBalance=[").append(getAccountBalance()).append("]\n");
		s.append("    phoneNumber=[").append(getPhoneNumber()).append("]\n");
		s.append("    statusCode=[").append(getStatusCode()).append("]\n");
		s.append("    statusDescription=[").append(getStatusDescription()).append("]\n");
		s.append("    activationDate=[").append(getActivationDate()).append("]\n");
		s.append("    accountDetail=[").append(getAccountDetail()).append("]\n");
		s.append("    rewardTransactionList=[\n");
		for (RewardTransactionTypeInfo info : getRewardTransactionList()) {
			s.append(info).append("\n");
		}
		s.append("}");
		
		return s.toString();
	}

}