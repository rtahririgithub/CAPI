package com.telus.eas.account.info;

import com.telus.api.account.SMSEBillRegistrationReminder;

public class SMSEBillRegistrationReminderInfo extends EBillRegistrationReminderInfo implements SMSEBillRegistrationReminder {

	static final long serialVersionUID = 1L;

	
	private String recipientPhoneNumber;

	public String getRecipientPhoneNumber() {
		return recipientPhoneNumber;
	}

	public void setRecipientPhoneNumber(String recipientPhoneNumber) {
		this.recipientPhoneNumber = recipientPhoneNumber;
	}

	
	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("SMSEBillRegistrationReminderInfo:{\n");
		s.append("    recipientPhoneNumber =[").append(recipientPhoneNumber).append("]\n");
		s.append("}");

		return s.toString();
	}

}
