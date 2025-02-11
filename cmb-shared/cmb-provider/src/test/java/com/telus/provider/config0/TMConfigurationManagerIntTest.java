package com.telus.provider.config0;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.config.UnknownObjectException;
import com.telus.api.config0.Configuration;
import com.telus.provider.account.TMAccountManager;

public class TMConfigurationManagerIntTest extends BaseTest {

	static {
		//setupSMARTDESKTOP_PT168();
		//setupD3();
		setupEASECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	private TMAccountManager accountManager;
	public TMConfigurationManagerIntTest(String name) throws Throwable {
		super(name);
	}
	private TMConfigurationManager configurationManager;
	
	

	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		configurationManager =  new TMConfigurationManager(provider);
	}

	public void testGetConfiguration0() throws TelusAPIException, SecurityException, NoSuchMethodException {
		 
        TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus", "common",
                "ClientAPI", "prepaidRates" });
        assertEquals(6, tmConfiguration.getPropertyCount());
       
	}
	
	public void testGetChildren0() throws TelusAPIException, UnknownObjectException {
//		String[] path={ "telus", "common", "ClientAPI", "prepaidRates" };
//		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(path);
//		
		String[] path1={"telus", "applications", "webactivations"};
		TMConfiguration tmConfiguration1= configurationManager.getConfiguration0(path1);
		
		TMConfiguration[] configurations=tmConfiguration1.getChildren0();
		for(int i=0;i<configurations.length;i++ ){
			System.out.println(configurations[i].toString());
		}
        assertEquals(2, configurations.length);
       
	}
	
	public void testRemoveConfiguration() throws TelusAPIException {
		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus","applications"});
		
		Configuration configuration=tmConfiguration.getChild("Test");
		int removed= configurationManager.removeConfiguration(configuration);
		
		assertEquals(1, removed);
    }

	
	public void testNewConfiguration0() throws TelusAPIException, UnknownObjectException {
		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus"});
		TMConfiguration[] configurations=tmConfiguration.getChildren0();
		TMConfiguration configuration= configurationManager.newConfiguration0(configurations[0], "Test");
		assertEquals("Test", configuration.getName());
    }
	
	
	
	public void testAddProperties() throws TelusAPIException, UnknownObjectException {
		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus","applications"});
		TMConfiguration configuration=tmConfiguration.getChild0("Test");
		configuration.setProperty("Pro1", "testing");
		
    }
	public void testRemoveProperties() throws TelusAPIException, UnknownObjectException {
		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus","applications"});
		TMConfiguration configuration=tmConfiguration.getChild0("Test");
		configuration.removeProperty("Pro1");
		
    }
	public void testLogActivation() throws TelusAPIException, UnknownObjectException {
		Account ai = accountManager.findAccountByBAN0(860942);
		Subscriber subscriber = ai.getSubscriberByPhoneNumber("7057155047");
		long portalUserID=0;
		long transactionID=0;
		long output= configurationManager.logActivation(subscriber, "18654", portalUserID, transactionID);
		System.out.println("OUT : "+output);
		
    }

	
	
}
