package com.telus.provider.monitoring;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;

public class MethodInvocationManagerEndToEndTest extends AMethodInvocationManagerTest{


	private AccountManager accountManager;
	private ClientAPI api = null;
	
	static {
		BaseTest.setupEASECA_QA();
	}

	public void setUp() throws Exception {		
		if (this.api == null) {
			this.api = ClientAPI.getInstance("18654", "apollo", "OLN");
			ClientAPI.Provider provider = this.api.getProvider();
			accountManager = provider.getAccountManager();
		}				
		
		
		MethodInvocationManager.getInstance(null, new AMethodInvocationManagerTest.MonitoringDataPointSleepTestDao(1000), null);
	}
	
	public void test() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException, InterruptedException {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("9021750001");
		
		Thread.sleep(15000);
		
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getAddress();		// Not included in count.
		subscriber.getActivityCode(); // Not included in count.		
		Account account = accountManager.findAccountByPhoneNumber("9021750001");		
		account.getAddress();
		
		Thread.sleep(15000);
		
		subscriber = accountManager.findSubscriberByPhoneNumber("9021750001");
		
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getDiscounts();
		subscriber.getAddress();		// Not included in count.
		subscriber.getActivityCode(); // Not included in count.		
		account = accountManager.findAccountByPhoneNumber("9021750001");		
		account.getAddress();
		
		Thread.sleep(500000);
	}

}
