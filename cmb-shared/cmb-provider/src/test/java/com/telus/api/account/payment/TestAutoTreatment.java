package com.telus.api.account.payment;

import com.telus.api.BaseTest;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;

public class TestAutoTreatment extends BaseTest {

	 private int BAN;


		static {

			// setupD3();
			 setupCHNLECA_QA();
	
//			System.setProperty("cmb.services.ReferenceDataHelper.url",	"t3://localhost:8001");			
//			System.setProperty("cmb.services.ReferenceDataFacade.url","t3://localhost:8001");				

		}


	public TestAutoTreatment(String name) throws Throwable {
		super(name);
		System.out.println("In constructor");
	}

	
	/*
	 * ==================================================================
	 * testPaymentArrangement test case
	 * =================================================================
	 */

	public void testAll() throws Throwable{
		
//		setupData("d3")
		setupData("qa");	
		_testAutoTreatment();
		
	}
	/**
	 * @throws Throwable
	 */
	public void _testAutoTreatment() throws Throwable {

		
		try {
			System.out.println("***** START OF TEST RUN TestAutoTreatment****");
			// provider.getAccountManagerEJB().updateAutoTreatment(BAN, true);
			AccountLifecycleManager accountLifecycleManager = provider.getAccountLifecycleManager();
			accountLifecycleManager.updateAutoTreatment(BAN, true, SessionUtil.getSessionId(accountLifecycleManager));
			System.out.println("updated auto treatment flag successfully!!");

			System.out.println("##### END of TestAutoTreatment****");		

		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}
	
	
	/**
	 * @param env
	 */
	public void setupData(String env) {
    	
    	if (env.equalsIgnoreCase("d1")) {

    	    
    	} else if (env.equalsIgnoreCase("d2")) {
    		
    	} else if (env.equalsIgnoreCase("d3"))
    	{
    		
    		BAN = 20007009;
    		
    	} else if (env.equalsIgnoreCase("sit")) {
    		
    	} else if (env.equalsIgnoreCase("qa")) {
    		
    		BAN=254977;
    		
    	} else if (env.equalsIgnoreCase("pt168")) {

    	 
    	} else if (env.equalsIgnoreCase("stag")) {
    		
    	} else if (env.equalsIgnoreCase("csi")) {
    		
    	} else if (env.equalsIgnoreCase("prod")) {
    		
    	} else { //default to D1 settings
   
    		BAN = 254977;
    	        		
    	}
    }
}