package com.telus.api.account.payment;

import com.telus.api.BaseTest;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSet;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMPostpaidConsumerAccount;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.reference.TMPricePlan;



public class TestRestoreSuspendedAccount extends BaseTest {

	 private int BAN;


	static {

		 //setuplocalHost();
		// setupD3();
		 setupCHNLECA_QA();
		// setupCHNLECA_PT168();
//		System.setProperty("cmb.services.ReferenceDataHelper.url","t3://localhost:8001");		
//		System.setProperty("cmb.services.ReferenceDataFacade.url","t3://localhost:8001");
				

	}

	private TMSubscriber subscriber;

	private TMPricePlan pricePlan;

	public TestRestoreSuspendedAccount(String name) throws Throwable {
		super(name);
	}

	/*
	 * ==================================================================
	 * getRequiredPaymentForRestoral test case
	 * =================================================================
	 */

	public void testAll() throws Throwable{
		
//		setupData("d3");
		
		setupData("qa");
		
		_testRestoreSuspendedAccount();
		
	}

	
	
	/**
	 * @throws Throwable
	 */
	public void _testRestoreSuspendedAccount() throws Throwable {


		try {
			System.out
					.println("***** START OF TEST RUN testRestoreSuspendedAccount****");
						
			PostpaidAccount  postpaidAccount  = (PostpaidAccount)api.getAccountManager().findAccountByBAN(6022);
			
			
			
			System.out.println("BAN - "+postpaidAccount.getBanId());
			
			System.out.println("Account Type - "+postpaidAccount.getAccountType());
			
			
			System.out.println("Account Sub Type - "+postpaidAccount.getAccountSubType());
			

			if (((TMAccount)postpaidAccount).isCLP()) {
				System.out.println("Account is for CLP Client");
			} else {
				System.out.println("Account is for non-CLP Client");
			}
			
			
			if(postpaidAccount.getFinancialHistory().isDelinquent()){
				System.out.println("Account is delinquent");
			}else{
				System.out.println("Account is non-delinquent");
			}
			
			
			if(postpaidAccount.getStatus()==AccountSummary.STATUS_SUSPENDED){
				System.out.println("Account is suspended");
			}else{
				System.out.println("Account is not suspended");
			}
			
			
			System.out.println("Credit class - "+postpaidAccount.getCreditCheckResult().getCreditClass());
			
			
			
			double requiredPayment=  -1;
			//get minimum payment required for account restoral
			requiredPayment= postpaidAccount.getRequiredPaymentForRestoral();
			
			System.out
			.println("GetRequiredPaymentForRestoral:got minimum payment of - "+requiredPayment);
			
			assertFalse(-1==requiredPayment);
	
			//restore account with minimum payment
			//NOTE: To run a JUnit test on this method you must temporarily change the method access from 'package' to 'public'
		//((TMPostpaidConsumerAccount)postpaidAccount).restoreSuspendedAccount(requiredPayment);
			//((TMAccount)postpaidAccount).restoreSuspendedAccount(requiredPayment);		

			System.out.println("Restored suspended account successfully ");

			System.out
					.println("##### testRestoreSuspendedAccount****");

		} catch (Exception ex) {
			ex.printStackTrace();
		}


	}
	
	

	
	  /**
	 * @param env
	 */
	private void setupData(String env) {
	    	
	    	if (env.equalsIgnoreCase("d1")) {

	    	    
	    	} else if (env.equalsIgnoreCase("d2")) {
	    		
	    	} else if (env.equalsIgnoreCase("d3")) {
	    		
	    			BAN = 622;
	    		
	    	} else if (env.equalsIgnoreCase("sit")) {
	    		
	    	} else if (env.equalsIgnoreCase("qa")) {
	    		
	    		BAN = 6022;
	    		
	    	} else if (env.equalsIgnoreCase("pt168")) {

	    	 
	    	} else if (env.equalsIgnoreCase("stag")) {
	    		
	    	} else if (env.equalsIgnoreCase("csi")) {
	    		
	    	} else if (env.equalsIgnoreCase("prod")) {
	    		
	    	} else { //default to D1 settings
	   
	    		 BAN = 6022;
	    	        		
	    	}
	    }
}