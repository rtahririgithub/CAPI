package com.telus.api.account;

public interface SMSEBillRegistrationReminder extends EBillRegistrationReminder {

	/**
	 * Returns Recipient Phone Number for Registration Notifications
	 * 
	 */

	String getRecipientPhoneNumber();

}
