package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;

public class TMPCSAccountIntTest extends BaseTest {

	static {
//		setupD3();
		setupEASECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMPCSAccountIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	

	
	public void testGetSubscriberNetworkCount() throws TelusAPIException{
		System.out.println("testGetSubscriberNetworkCount start");
		
		TMAccount account = (TMAccount) api.getAccountManager().findAccountByBAN(12474);
		TMPCSAccount pcsacct= new TMPCSAccount(provider, account.getDelegate0(), account);
		int count=pcsacct.getPCSSubscriberCount("C", 'A');
		assertEquals(1, count);
		
	   	System.out.println("testGetSubscriberNetworkCount End");
	}
	
	
	
	
	
	}


