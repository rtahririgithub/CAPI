package com.telus.provider.account;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.BaseTest;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CreditCard;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class TMContractIntTest extends BaseTest {

	private TMAccountManager accountManager;
	private ReferenceDataManager referenceDataManager;
	
	static {
		//setupD3();
		//setupEASECA_QA();
		localhostWithPT148Ldap();
//		setupEASECA_PT168();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://142.175.166.93:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://142.175.166.93:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://142.175.166.93:7001");
//		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7001");
//		
//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
		
	}
	
	public TMContractIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		referenceDataManager = super.provider.getReferenceDataManager();
		
	}
	
	
	public void testPayBill_ApplicationException() throws TelusAPIException{
		//Test Account management EJB method
		try{
			Account account = accountManager.findAccountByBAN(70565862);
			double amount=100.00;
			CreditCard creditCard= accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000000229", "123456", "0000");
			AuditHeader auditHeader = new AuditHeaderInfo();
			
			account.payBill(amount, creditCard, "businessRole", auditHeader);
		}catch(TelusAPIException e){
			assertEquals(": Credit Card Type not supported", e.getMessage());
		}
	}
	
	public void testTestAddition() throws TelusAPIException{
		System.out.println("Start testTestAddition with TM Service Object :");
		Account account = accountManager.findAccountByBAN(805938);
		Subscriber subscriber=account.getSubscriberByPhoneNumber("7807191196");	
		TMContract contract =(TMContract) subscriber.getContract();
		System.out.println("TMContarct"+contract);
		Service service = referenceDataManager.getRegularService("TESTBAS01");
		try{
		contract.testAddition(service);	
		}catch(InvalidServiceChangeException e){
			assertEquals(": [reason=UNAVAILABLE_SERVICE]: Service to be added to subscriber is not compatible with the account: AccountType[I] AccountSubType[R] Service[TESTBAS01] familyType[Y]", e.getMessage());
		}
		System.out.println("Ended testTestAddition with TM Service Object:");
	}

	public void testContractSave() throws TelusAPIException {
		System.out.println("Start testContractSave with TM Service Object :");
		Account account = accountManager.findAccountByBAN(70648172);
		Subscriber subscriber=account.getSubscriberByPhoneNumber("5141757014");	
		TMContract contract =(TMContract) subscriber.getContract();
		System.out.println("TMContarct"+contract);
		contract.save();
		System.out.println("Ended testContractSave with TM Service Object:");
	}

	

}


