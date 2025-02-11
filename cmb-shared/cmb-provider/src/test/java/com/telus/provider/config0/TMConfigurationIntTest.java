package com.telus.provider.config0;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.api.config.UnknownObjectException;
import com.telus.api.config0.Configuration;
import com.telus.provider.account.TMAccountManager;

public class TMConfigurationIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
	}
	
	public TMConfigurationIntTest(String name) throws Throwable {
		super(name);
	}
	private TMConfigurationManager configurationManager;
	
	public void setUp() throws Exception{
		super.setUp();
		configurationManager =  new TMConfigurationManager(provider);
	}

	
	public void testRefreshDefinedProperties() throws TelusAPIException {
		TMConfiguration tmConfiguration= configurationManager.getConfiguration0(new String[] { "telus","applications"});
		tmConfiguration.refreshDefinedProperties();
	}
	
	
	
	
}
