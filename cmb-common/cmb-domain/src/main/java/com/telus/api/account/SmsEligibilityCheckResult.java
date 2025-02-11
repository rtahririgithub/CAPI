package com.telus.api.account;

public interface SmsEligibilityCheckResult {

	public boolean getSendSMS();
	
	public String getSmsTemplate();

	public String getContactEventTypeId();

	public String getMemoType();

	public String getMemoText();

	public double getMessageDelay();

}
