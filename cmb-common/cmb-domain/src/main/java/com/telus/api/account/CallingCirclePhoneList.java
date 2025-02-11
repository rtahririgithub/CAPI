package com.telus.api.account;

import java.util.Date;

public interface CallingCirclePhoneList {
	String[] getPhoneNumberList();
	Date getEffectiveDate();
	Date getExpiryDate();
}
