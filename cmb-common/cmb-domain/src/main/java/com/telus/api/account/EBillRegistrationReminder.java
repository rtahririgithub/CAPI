package com.telus.api.account;

import java.util.Date;

public interface EBillRegistrationReminder {

	/**
	 * Returns Activated Phone Number
	 * 
	 */

	String getActivatedPhoneNumber();

	/**
	 * Returns the Date of the last EBill Registration Reminder being sent
	 * 
	 */
	Date getLastNotificationDate();

}
