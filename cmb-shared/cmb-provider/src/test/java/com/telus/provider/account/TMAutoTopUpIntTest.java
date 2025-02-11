package com.telus.provider.account;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;

public class TMAutoTopUpIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

	}
	public TMAutoTopUpIntTest(String name) throws Throwable {
		super(name);
	}
	public void setUp() throws Exception{
		super.setUp();

	}
	

	public void testUpdateAutoTopUp() throws  TelusAPIException{

		 TMPrepaidConsumerAccount targetAccount =(TMPrepaidConsumerAccount) api.getAccountManager().findAccountByBAN(4036924);
		 TMAutoTopUp autoTopUp=  (TMAutoTopUp) targetAccount.getAutoTopUp();
		 autoTopUp.apply();
	}
}
