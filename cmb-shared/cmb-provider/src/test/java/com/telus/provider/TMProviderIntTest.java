package com.telus.provider;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;

public class TMProviderIntTest extends BaseTest {
	
	static {
           setupEASECA_QA();
//		setupSMARTDESKTOP_D3();
//		setupEASECA_PT168();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://cmosr-custinfomgmt2-dv103.tmi.telus.com:12022");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://cmosr-custinfomgmt2-dv103.tmi.telus.com:12022");
//		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://cmosr-custinfomgmt2-dv103.tmi.telus.com:12022");

	}
	
	public TMProviderIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	
	public void testGetUserRole() throws TelusAPIException{
		String userRole=provider.getUserRole();
		assertEquals("ALL", userRole);
	}
	
	public void testChangeKnowbilityPassword() throws TelusAPIException{
		try{
		provider.changeKnowbilityPassword("123", "456", "456");
		}catch(TelusAPIException e){
			assertEquals(": Old password incorrect.", e.getMessage());
		}
		
	}
	
	
}


