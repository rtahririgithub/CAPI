package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class SubscriptionMSCInfo extends Info {

	private static final long serialVersionUID = 1L;

	public static final String RETURN_CODE_MSC_NOT_BROKEN = "0";	
	public static final String RETURN_CODE_MSC_BROKEN = "1";
	
	private String subscriptionId;
	private Double missedMSCAmount;
	private String returnCode;

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Double getMissedMSCAmount() {
		return missedMSCAmount;
	}

	public void setMissedMSCAmount(Double missedMSCAmount) {
		this.missedMSCAmount = missedMSCAmount;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public boolean isMSCBroken() {
		return RETURN_CODE_MSC_BROKEN.equals(this.returnCode);
	}
	
}
