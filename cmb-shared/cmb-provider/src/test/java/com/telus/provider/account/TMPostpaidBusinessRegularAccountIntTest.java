package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.eas.account.info.PaymentMethodInfo;

public class TMPostpaidBusinessRegularAccountIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMPostpaidBusinessRegularAccountIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	

	
	public void testGetFeeWaivers() throws TelusAPIException{
		System.out.println("testGetFeeWaivers start");
		
		Account account =  api.getAccountManager().findAccountByBAN(12474);
		
		FeeWaiver[] waivers =((TMPostpaidBusinessRegularAccount)account).getFeeWaivers();
		assertEquals(0, waivers.length);
		
	   	System.out.println("testGetFeeWaivers End");
	}
	
	public void testCheckCredit() throws TelusAPIException{
		System.out.println("testCheckCredit start");
		
		Account account =  api.getAccountManager().findAccountByBAN(12474);
		BusinessCreditIdentity[] creditIdentities = ((PostpaidBusinessRegularAccount)account).getBusinessCreditIdentities();
	
		CreditCheckResult creditcheck =((TMPostpaidBusinessRegularAccount)account).checkCredit(creditIdentities[0]);
		assertEquals(0, creditcheck.getCreditScore());
		assertEquals("D", creditcheck.getCreditCheckResultStatus());
		
	   	System.out.println("testCheckCredit End");
	}
		
	public void testSavePaymentMethod() throws TelusAPIException{
		System.out.println("testSavePaymentMethod start");
		
		Account account =  api.getAccountManager().findAccountByBAN(12474);
		PaymentMethod paymentmthd=api.getAccountManager().newPaymentMethod(account);
		paymentmthd.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		
		((TMPostpaidBusinessRegularAccount)account).savePaymentMethod(paymentmthd);
		
	   	System.out.println("testSavePaymentMethod End");
	}
}


