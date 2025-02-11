package com.telus.provider.config1;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.config.UnknownObjectException;
import com.telus.provider.account.TMAccountManager;

public class TMConfigurationManagerIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
	}
	
	public TMConfigurationManagerIntTest(String name) throws Throwable {
		super(name);
	}
	private TMConfigurationManager configurationManager;
	
	private TMAccountManager accountManager;
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		configurationManager =  super.provider.getConfigurationManager0();
	}

	
	
	public void testLookup1() throws TelusAPIException, UnknownObjectException {
		try{
			String[] path={"telus", "applications"};
			com.telus.api.config1.Configuration configuration= configurationManager.lookup(path);
		}catch(TelusAPIException e){
			System.out.println(e.getMessage());
		}
    }
	public void testLookup2() throws TelusAPIException, UnknownObjectException {
		try{
			String[] path={"telus", "applications"};
			com.telus.api.config1.Configuration configuration= configurationManager.lookup(path);
		}catch(TelusAPIException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void testLogActivation() throws TelusAPIException, UnknownObjectException {
		Account ai = accountManager.findAccountByBAN0(1905787);
		Subscriber subscriber = ai.getSubscriberByPhoneNumber("4169930663");
		long portalUserID=0;
		long transactionID=0;
		long output= configurationManager.logActivation(subscriber, "18654", portalUserID, transactionID);
		System.out.println("OUT : "+output);
		
    }
	
	

	
	
}
