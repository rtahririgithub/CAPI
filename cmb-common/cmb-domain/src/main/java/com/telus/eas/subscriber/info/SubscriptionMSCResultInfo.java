package com.telus.eas.subscriber.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class SubscriptionMSCResultInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	public static final String RETURN_CODE_MSC_NOT_BROKEN = "0";	
	public static final String RETURN_CODE_MSC_BROKEN = "1";
	
	private List<SubscriptionMSCInfo> subscriptionMSCInfoList;
	private String returnCode;

	public List<SubscriptionMSCInfo> getSubscriptionMSCInfoList() {
		return subscriptionMSCInfoList;
	}

	public void setSubscriptionMSCInfoList(List<SubscriptionMSCInfo> subscriptionMSCInfoList) {
		this.subscriptionMSCInfoList = subscriptionMSCInfoList;
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
