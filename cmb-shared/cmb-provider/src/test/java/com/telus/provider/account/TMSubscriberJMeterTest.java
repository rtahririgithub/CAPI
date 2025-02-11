package com.telus.provider.account;

import com.telus.api.account.Subscriber;
import com.telus.provider.TMJMeterBase;

public class TMSubscriberJMeterTest extends TMJMeterBase {
	private String phoneNum;
	private int newTerm;
	
	public void setUp() throws Exception {
		super.setUp();
		
		phoneNum = getProperties().getProperty("DATA.RENEWCONTRACT.PHONENUM");
		try { 
			newTerm = Integer.parseInt(getProperties().getProperty("DATA.RENEWCONTRACT.NEWTERM"));
		} catch (Exception e) {
			e.printStackTrace();
			newTerm = 24;
		}
	}
	 
	public void testRenewContract() {
		try {
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNum);
			subscriber.renewContract(newTerm);
		} catch (Exception te) {
			te.printStackTrace();
		}
	}
}
