package com.telus.eas.utility.info;

import java.util.List;
import com.telus.eas.framework.info.Info;
public class ProvisioningLicenseInfo extends Info {

	private static final long serialVersionUID = 1L;
	int ban;
	String subscriptionId;
	List<String> switchCodes;
	private String transactionType;
	
	public static final String PROV_LICENSE_TRANSACTION_TYPE_ADD = "ADD";
	public static final String PROV_LICENSE_TRANSACTION_TYPE_REMOVE = "REMOVE";

	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public int getBan() {
		return ban;
	}
	public void setBan(int ban) {
		this.ban = ban;
	}
	public String getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	public List<String> getSwitchCodes() {
		return switchCodes;
	}
	public void setSwitchCodes(List<String> switchCodes) {
		this.switchCodes = switchCodes;
	}

}
