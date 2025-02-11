package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.PaymentMethod;

public class TMPaymentMethodIntTest extends BaseTest {

	static {
		setupEASECA_QA();
//		setupD3();
//		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMPaymentMethodIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	

	
	public void testSetSuppressReturnEnvelope() throws TelusAPIException{
		System.out.println("testSetSuppressReturnEnvelope start");
	
		Account account = api.getAccountManager().findAccountByBAN(12474);
		TMAccountManager acctManager= (TMAccountManager) api.getAccountManager();
		PaymentMethod paymentmthd=acctManager.newPaymentMethod(account);
		boolean suppressReturnEnvelope=true;
		paymentmthd.setSuppressReturnEnvelope(suppressReturnEnvelope);
		
	   	System.out.println("testSetSuppressReturnEnvelope End");
	}
	
	
	
	
	
	}


