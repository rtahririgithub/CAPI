package com.telus.eas.account.credit.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class SubscriberEligibilityInfo extends Info {

	static final long serialVersionUID = 1L;

	private int subscriberOrdinalNumber;
	private String subscriberID;
	private long subscriptionID;
	private List<DevicePaymentPlanInfo> devicePaymentPlanEligibilityList;

	public int getSubscriberOrdinalNumber() {
		return subscriberOrdinalNumber;
	}

	public void setSubscriberOrdinalNumber(int subscriberOrdinalNumber) {
		this.subscriberOrdinalNumber = subscriberOrdinalNumber;
	}

	public String getSubscriberID() {
		return subscriberID;
	}

	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	public long getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(long subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public List<DevicePaymentPlanInfo> getDevicePaymentPlanEligibilityList() {
		return devicePaymentPlanEligibilityList;
	}

	public void setDevicePaymentPlanEligibilityList(List<DevicePaymentPlanInfo> devicePaymentPlanEligibilityList) {
		this.devicePaymentPlanEligibilityList = devicePaymentPlanEligibilityList;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("SubscriberEligibilityInfo: {\n");
		s.append("    subscriberOrdinalNumber=[").append(getSubscriberOrdinalNumber()).append("]\n");
		s.append("    subscriberID=[").append(getSubscriberID()).append("]\n");
		s.append("    subscriptionID=[").append(getSubscriptionID()).append("]\n");
		s.append("    devicePaymentPlanEligibilityList=[\n");
		if (getDevicePaymentPlanEligibilityList() != null && !getDevicePaymentPlanEligibilityList().isEmpty()) {
			for (DevicePaymentPlanInfo info : getDevicePaymentPlanEligibilityList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("}");

		return s.toString();
	}

}