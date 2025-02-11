package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Charge;
import com.telus.api.account.Credit;
import com.telus.eas.framework.info.CreditInfo;

public class TMCreditIntTest extends BaseTest {

	
	
	static {
		setupEASECA_QA();
		//setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMCreditIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		
	}
		
	public void testApply() throws TelusAPIException{
		System.out.println("testApply start");
		Account account = api.getAccountManager().findAccountByBAN(70077467);
		TMCredit credit= (TMCredit)account.newCredit(Credit.TAX_OPTION_ALL_TAXES);
		credit.setReasonCode("PCR");

//		credit.apply(true);
		credit.apply(false);
       	System.out.println("testApply End");
	}
	
	public void testReverse() throws TelusAPIException{
		System.out.println("testReverse start");
		try{
			TMAccount account = (TMAccount)api.getAccountManager().findAccountByBAN(70077467);
			TMCredit credit= (TMCredit)account.newCredit(Credit.TAX_OPTION_ALL_TAXES);
			credit.setReasonCode("39CR");
			credit.getDelegate().setId(10583);
			
			String reversalReasonCode="Reversal";
			credit.reverse(reversalReasonCode, "memoText", true);
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: No record found for the input chargeSeqNo");
		}
       	System.out.println("testReverse End");
	}
	
	
	public void testGetRelatedCharges() throws TelusAPIException{
	
		System.out.println("testGetRelatedCharges start");
		TMSubscriber subscriber = (TMSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("4034850238");
		TMCredit credit= (TMCredit)subscriber.newCredit(Credit.TAX_OPTION_ALL_TAXES);
		credit.setReasonCode("VAD");
			
		Charge[] charge=credit.getRelatedCharges();
		System.out.println("Charge: "+charge.length);
	    System.out.println("testGetRelatedCharges End");
	
	}
	
	}


