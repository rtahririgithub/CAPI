package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.reference.BusinessRole;
import com.telus.eas.account.info.AuditHeaderInfo;

public class TMCreditCardIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMCreditCardIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
		
	public void testValidate() throws TelusAPIException{
		System.out.println("testValidate start");
		try{
		Account account = api.getAccountManager().findAccountByBAN(70077467);
		TMCreditCard creditCard= (TMCreditCard)api.getAccountManager().newCreditCard(account);
		creditCard.setToken("100000000000000000229", "422222", "2222");
		creditCard.setExpiryYear(2020);
		creditCard.validate("Purchace-now", BusinessRole.BUSINESS_ROLE_ALL);
		}catch(InvalidCreditCardException e){
			assertEquals("WPS experienced policy exception encountered. ", e.getReasonText());
		}
		System.out.println("testValidate End");
	}
	
	public void testValidateCard() throws TelusAPIException{
		try{
			Account account = api.getAccountManager().findAccountByBAN(194587);
			TMCreditCard creditCard= (TMCreditCard)api.getAccountManager().newCreditCard(account);
			creditCard.setToken("100000000000000006427", "490000", "0235");
			creditCard.setExpiryYear(2015);
			creditCard.validate("Purchace-now", BusinessRole.BUSINESS_ROLE_ALL);
		}catch(InvalidCreditCardException e){e.printStackTrace();
			assertEquals("Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.", e.getReasonText());
		}
	}
	
	
	
	}


