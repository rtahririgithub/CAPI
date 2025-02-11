package com.telus.eas.eligibility.info;

import com.telus.eas.framework.info.Info;

public class SmsEligibilityCheckResultInfo extends Info {

	private static final long serialVersionUID = 7892655146252718780L;

	private boolean sendSMS = false;
	private String smsTemplate = null;
	private String contactEventTypeId = null;
	private String memoType = null;
	private String memoText = null;
	private double messageDelay = 0;

	public boolean getSendSMS() {
		return sendSMS;
	}

	public void setSendSMS(boolean sendSMS) {
		this.sendSMS = sendSMS;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	public String getContactEventTypeId() {
		return contactEventTypeId;
	}

	public void setContactEventTypeId(String contactEventTypeId) {
		this.contactEventTypeId = contactEventTypeId;
	}

	public String getMemoType() {
		return memoType;
	}

	public void setMemoType(String memoType) {
		this.memoType = memoType;
	}

	public String getMemoText() {
		return memoText;
	}

	public void setMemoText(String memoText) {
		this.memoText = memoText;
	}

	public double getMessageDelay() {
		return messageDelay;
	}

	public void setMessageDelay(double messageDelay) {
		this.messageDelay = messageDelay;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer("SmsEligibilityCheckResultInfo:[\n");

		buffer.append("    sendSMS = [").append(sendSMS).append("]\n");
		buffer.append("    smsTemplate = [").append(smsTemplate).append("]\n");
		buffer.append("    contactEventTypeId = [").append(contactEventTypeId).append("]\n");
		buffer.append("    memoType = [").append(memoType).append("]\n");
		buffer.append("    memoText = [").append(memoText).append("]\n");
		buffer.append("    messageDelay = [").append(messageDelay).append("]\n");
		buffer.append("]");

		return buffer.toString();

	}
}
