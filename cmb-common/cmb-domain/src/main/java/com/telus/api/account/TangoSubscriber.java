package com.telus.api.account;

import java.util.Date;

public interface TangoSubscriber extends Subscriber {
	String getNextPhoneNumber();
	Date getNextPhoneNumberChangeDate();
}