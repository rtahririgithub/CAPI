package com.telus.eas.subscriber.info;

import com.telus.eas.account.info.AccountInfo;

/**
 * @author R. Fong
 *
 */
public class MigrateSeatChangeInfo extends BaseChangeInfo {

	private static final long serialVersionUID = -586224999112741518L;
	
	private String migrationTypeCode;
	private String activityReasonCode;
	private String memoText;
	private int targetAccountNumber;
	private String targetSeatGroupId;
	private String targetSeatTypeCode;
	private String pricePlanCode;
	private String applicationId;
	private String dealerCode;
	private String salesRepCode;
	
	private AccountInfo newAccountInfo;
	private SubscriberInfo newSubscriberInfo;
	private SubscriberContractInfo newContractInfo;
	
	public AccountInfo getNewAccountInfo() {
		return newAccountInfo;
	}

	public void setNewAccountInfo(AccountInfo newAccount) {
		this.newAccountInfo = newAccount;
	}

	public SubscriberInfo getNewSubscriberInfo() {
		return newSubscriberInfo;
	}

	public void setNewSubscriberInfo(SubscriberInfo newSubscriber) {
		this.newSubscriberInfo = newSubscriber;
	}

	public SubscriberContractInfo getNewContractInfo() {
		return newContractInfo;
	}

	public void setNewContractInfo(SubscriberContractInfo newContract) {
		this.newContractInfo = newContract;
	}

	public String getMigrationTypeCode() {
		return migrationTypeCode;
	}
	
	public void setMigrationTypeCode(String migrationTypeCode) {
		this.migrationTypeCode = migrationTypeCode;
	}
	
	public String getActivityReasonCode() {
		return activityReasonCode;
	}
	
	public void setActivityReasonCode(String activityReasonCode) {
		this.activityReasonCode = activityReasonCode;
	}
	
	public String getMemoText() {
		return memoText;
	}
	
	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}
	
	public int getTargetAccountNumber() {
		return targetAccountNumber;
	}
	
	public void setTargetAccountNumber(int targetAccountNumber) {
		this.targetAccountNumber = targetAccountNumber;
	}
	
	public String getTargetSeatGroupId() {
		return targetSeatGroupId;
	}
	
	public void setTargetSeatGroupId(String targetSeatGroupId) {
		this.targetSeatGroupId = targetSeatGroupId;
	}
	
	public String getTargetSeatTypeCode() {
		return targetSeatTypeCode;
	}
	
	public void setTargetSeatTypeCode(String targetSeatTypeCode) {
		this.targetSeatTypeCode = targetSeatTypeCode;
	}

	public String getPricePlanCode() {
		return pricePlanCode;
	}
	
	public void setPricePlanCode(String pricePlanCode) {
		this.pricePlanCode = pricePlanCode;
	}
	
	public String getDealerCode() {
		return dealerCode;
	}

	public void setDealerCode(String dealerCode) {
		this.dealerCode = dealerCode;
	}

	public String getSalesRepCode() {
		return salesRepCode;
	}

	public void setSalesRepCode(String salesRepCode) {
		this.salesRepCode = salesRepCode;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}

	public String toString() {
		
		StringBuffer sb = new StringBuffer();

		sb.append("MigrateSeatChangeInfo: {\n");
		sb.append("    migrationTypeCode=["+ migrationTypeCode + "]\n");
		sb.append("    reasonCode=["+ activityReasonCode + "]\n");
		sb.append("    memoText=["+ memoText + "]\n");
		sb.append("    targetAccountNumber=[" + targetAccountNumber + "]\n");
		sb.append("    targetSeatGroupId=[" + targetSeatGroupId + "]\n");
		sb.append("    targetSeatTypeCode=[" + targetSeatTypeCode + "]\n");
		sb.append("    pricePlanCode=[" + pricePlanCode + "]\n");
		sb.append("    dealerCode=[" + dealerCode + "]\n");
		sb.append("    salesRepCode=[" + salesRepCode + "]\n");
		sb.append("    applicationId=["+ applicationId + "]\n");
		if (newAccountInfo != null) {
			sb.append("    newAccount=["+ newAccountInfo.getBanId() + "]\n");
		}		
		if (newSubscriberInfo != null) {
			sb.append("    newSubscriber=[" + newSubscriberInfo.getBanId() + "/" + newSubscriberInfo.getSubscriberId() + "/" +
					newSubscriberInfo.getPhoneNumber() + "]\n");
		}
		if (newContractInfo != null) {
			sb.append("    newContract=[" + newContractInfo.getPricePlanCode() + "]\n");
		}
		sb.append("}\n");
		sb.append(super.toString());

		return sb.toString();
	}

}