package com.telus.eas.account.credit.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class CreditProgramSubscriberEligibilityInfo extends Info {

	static final long serialVersionUID = 1L;

	private boolean creditProgramEligibilityInd;
	private List<String> creditProgramEligibilityReasonList;
	private CreditProgramInfo creditProgram;
	private List<SubscriberEligibilityInfo> subscriberEligibilityList;
	private List<DevicePaymentThresholdInfo> devicePaymentPlanThresholdList;
	private int maxNumberOfSubscribers;
	private int referToCreditMaxNumberOfSubscribers;

	public boolean isCreditProgramEligible() {
		return creditProgramEligibilityInd;
	}

	public void setCreditProgramEligibilityInd(boolean creditProgramEligibilityInd) {
		this.creditProgramEligibilityInd = creditProgramEligibilityInd;
	}

	public List<String> getCreditProgramEligibilityReasonList() {
		return creditProgramEligibilityReasonList;
	}

	public void setCreditProgramEligibilityReasonList(List<String> creditProgramEligibilityReasonList) {
		this.creditProgramEligibilityReasonList = creditProgramEligibilityReasonList;
	}

	public CreditProgramInfo getCreditProgram() {
		return creditProgram;
	}

	public void setCreditProgram(CreditProgramInfo creditProgram) {
		this.creditProgram = creditProgram;
	}

	public List<SubscriberEligibilityInfo> getSubscriberEligibilityList() {
		return subscriberEligibilityList;
	}

	public void setSubscriberEligibilityList(List<SubscriberEligibilityInfo> subscriberEligibilityList) {
		this.subscriberEligibilityList = subscriberEligibilityList;
	}

	public List<DevicePaymentThresholdInfo> getDevicePaymentPlanThresholdList() {
		return devicePaymentPlanThresholdList;
	}

	public void setDevicePaymentPlanThresholdList(List<DevicePaymentThresholdInfo> devicePaymentPlanThresholdList) {
		this.devicePaymentPlanThresholdList = devicePaymentPlanThresholdList;
	}
	
	public int getMaxNumberOfSubscribers() {
		return maxNumberOfSubscribers;
	}

	public void setMaxNumberOfSubscribers(int maxNumberOfSubscribers) {
		this.maxNumberOfSubscribers = maxNumberOfSubscribers;
	}

	public int getReferToCreditMaxNumberOfSubscribers() {
		return referToCreditMaxNumberOfSubscribers;
	}

	public void setReferToCreditMaxNumberOfSubscribers(int referToCreditMaxNumberOfSubscribers) {
		this.referToCreditMaxNumberOfSubscribers = referToCreditMaxNumberOfSubscribers;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("CreditProgramSubscriberEligibilityInfo: {\n");
		s.append("    creditProgramEligibilityInd=[").append(isCreditProgramEligible()).append("]\n");
		s.append("    creditProgramEligibilityReasonList=[\n");
		if (getCreditProgramEligibilityReasonList() != null && !getCreditProgramEligibilityReasonList().isEmpty()) {
			for (String reason : getCreditProgramEligibilityReasonList()) {
				s.append(reason).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    creditProgram=[").append(getCreditProgram()).append("]\n");
		s.append("    subscriberEligibilityList=[\n");
		if (getSubscriberEligibilityList() != null && !getSubscriberEligibilityList().isEmpty()) {
			for (SubscriberEligibilityInfo info : getSubscriberEligibilityList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    devicePaymentPlanThresholdList=[\n");
		if (getDevicePaymentPlanThresholdList() != null && !getDevicePaymentPlanThresholdList().isEmpty()) {
			for (DevicePaymentThresholdInfo info : getDevicePaymentPlanThresholdList()) {
				s.append(info).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    maxNumberOfSubscribers=[").append(getMaxNumberOfSubscribers()).append("]\n");
		s.append("    referToCreditMaxNumberOfSubscribers=[").append(getReferToCreditMaxNumberOfSubscribers()).append("]\n");
		s.append("}");

		return s.toString();
	}

}