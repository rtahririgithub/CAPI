package com.telus.provider.account;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CLMSummary;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.ManualCreditCheckRequest;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.PaymentMethodInfo;

public class TMPostpaidConsumerAccountIntTest extends BaseTest {

	static {
		//setupEASECA_QA();
		setupCHNLECA_PT168();
		//System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7168");
		//System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7168");
				//System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7168");
		//setuplocalHost();
		}
	
	public TMPostpaidConsumerAccountIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	

	
	public void testGetCLMSummary() throws TelusAPIException{
		System.out.println("testGetCLMSummary start");
		
		Account account =  api.getAccountManager().findAccountByBAN(750714);
		
		CLMSummary summary =((TMPostpaidConsumerAccount)account).getCLMSummary();
		assertEquals(0.00, summary.getUnpaidUnBilledAmount(),2);
		assertEquals(0.00, summary.getRequiredMinimumPayment(),2);
		
	   	System.out.println("testGetCLMSummary End");
	}
	
	public void testGetFeeWaivers() throws TelusAPIException{
		System.out.println("testGetFeeWaivers start");
		
		Account account =  api.getAccountManager().findAccountByBAN(194587);
		
		FeeWaiver[] waivers =((TMPostpaidConsumerAccount)account).getFeeWaivers();
		assertEquals(0, waivers.length);
		
	   	System.out.println("testGetFeeWaivers End");
	}
	
	public void testCheckCredit() throws TelusAPIException{
		System.out.println("testCheckCredit start");
		
		Account account =  api.getAccountManager().findAccountByBAN(660416);
		
		AuditHeader auditHeader = new AuditHeaderInfo();
		CreditCheckResult creditcheck =((TMPostpaidConsumerAccount)account).checkCredit(auditHeader);
		assertEquals(761, creditcheck.getCreditScore());
		assertEquals("D", creditcheck.getCreditCheckResultStatus());
		
	   	System.out.println("testCheckCredit End");
	}
	public void testSavePaymentMethod() throws TelusAPIException{
		System.out.println("testSavePaymentMethod start");
		
		Account account =  api.getAccountManager().findAccountByBAN(805938);
		PaymentMethod paymentmthd=api.getAccountManager().newPaymentMethod(account);
		paymentmthd.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		
		((TMPostpaidConsumerAccount)account).savePaymentMethod(paymentmthd);
		
	   	System.out.println("testSavePaymentMethod End");
	}
	
	
	public void testManualCheckCredit_Defect() throws TelusAPIException, ParseException{
		System.out.println("testManualCheckCredit_Defect start");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = sdf.parse("21/12/1978");
		
		Account account =  api.getAccountManager().findAccountByBAN(70682068);
		
		AuditHeader auditHeader = new AuditHeaderInfo();
		ManualCreditCheckRequest ccRequest  =((TMPostpaidConsumerAccount)account).getManualCreditCheckRequest();
		
		ccRequest.getConsumerName().setFirstName("TestFirstName");
        
        ccRequest.getConsumerName().setLastName("TestLastName");
        ccRequest.getPersonalCreditInformation().setBirthDate(date);
        ccRequest.getPersonalCreditInformation().setSin("899785485");
        
        ccRequest.getAddress().copyFrom(account.getAddress());
        PostpaidConsumerAccount newManualCreditCheckAccount = ccRequest.transformToPostpaidConsumerAccount();
        newManualCreditCheckAccount.checkCredit(auditHeader);

		
	   	System.out.println("testManualCheckCredit_Defect End");
	}
}


