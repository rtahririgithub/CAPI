package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.Subscriber;

public class TMActivationCreditIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "false");
		
/*		System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "true");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "true");
		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "true");*/
		
		setupEASECA_QA();
		//setupD3();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

		
//		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");

	}
	
	public TMActivationCreditIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	
}


