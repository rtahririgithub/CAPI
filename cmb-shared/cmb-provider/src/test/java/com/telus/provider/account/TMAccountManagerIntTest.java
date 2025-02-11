package com.telus.provider.account;


import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountMatchException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CreditCard;
import com.telus.api.account.IDENAccount;
import com.telus.api.account.InvalidActivationCodeException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.Memo;
import com.telus.api.account.MemoCriteria;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSPostpaidBusinessPersonalAccount;
import com.telus.api.account.PCSPostpaidBusinessRegularAccount;
import com.telus.api.account.PCSPostpaidCorporateRegularAccount;
import com.telus.api.account.PCSPrepaidConsumerAccount;
import com.telus.api.account.PhoneNumberSearchOption;
import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.account.PrepaidEventHistory;
import com.telus.api.account.SearchResults;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.reference.Brand;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

public class TMAccountManagerIntTest extends BaseTest {
	
	static {
		setupEASECA_QA();
		System.setProperty("cmb.services.SubscriberLifecycleHelper.url",
				"t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url",
				"t3://localhost:7001");
		System.setProperty("cmb.services.SubscriberLifecycleManager.url",
				"t3://localhost:7001");
	}
	
	public TMAccountManagerIntTest(String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;
	
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}

	public void testFindAccountByBAN0() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		assertEquals(197806, ai.getBanId());
		
		try {
			accountManager.findAccountByBAN0(-5);
		} catch (UnknownBANException e) {
			
		}

	}

	public void testFindAccountByBAN() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN(70701430);
		assertEquals(70701430, ai.getBanId());
		
		try {
			accountManager.findAccountByBAN(-5);
		} catch (UnknownBANException e) {
			
		}
	}

	public void testFindAccountsByBANs() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Account[] ai = accountManager.findAccountsByBANs(new int[]{197806 ,194587});
		assertEquals(2, ai.length);
		assertEquals(197806, ai[0].getBanId());
		assertEquals(194587, ai[1].getBanId());	
		
	}
	
	public void testFindAccountsBySerialNumber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		AccountSummary[] ai = accountManager.findAccountsBySerialNumber("16809070086");
		assertEquals(1, ai.length);
		assertEquals(197806, ai[0].getBanId());	
		}
	
	public void testRetrieveAccountByPostalCode() throws TelusAPIException, SecurityException, NoSuchMethodException {
		
		AccountSummary[] accounts = accountManager.findAccountsByPostalCode("MCNEIL", "63074", 5);	
		assertEquals(4, accounts.length);				
		
	}	

	public void testFindSubscriberByPhoneNumber() throws TelusAPIException,
			SecurityException, NoSuchMethodException {
		Subscriber s = accountManager.findSubscriberByPhoneNumber("6471251808");
		System.out.println("subscriberInfo"+s.getSeatData().toString());
		assertEquals("6471251808", s.getPhoneNumber());

	}

	public void testFindSubscriberByPhoneNumberwithSearchOption() throws TelusAPIException,
	SecurityException, NoSuchMethodException {
		PhoneNumberSearchOption  searchOption = accountManager.newPhoneNumberSearchOption();
		searchOption.setSearchHSIA(true);
		Subscriber s = accountManager.findSubscriberByPhoneNumber("4160000001", searchOption);
		System.out.println("subscriberInfo"+s.getSeatData().toString());
		assertEquals("6471251808", s.getPhoneNumber());
}

	
	public void testFindAccountsByBusinessName() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SearchResults result = accountManager.findAccountsByBusinessName("*",
				"a", 
				false, 
				'*', 
				'*', 
				"*", 
				Brand.BRAND_ID_TELUS, 
				100);
		AccountSummary[] as = (AccountSummary[])result.getItems();
		
		assertEquals(99, as.length);
		
	}


	public void testFindAccountsByPhoneNumberStringBooleanBoolean()
			throws TelusAPIException, SecurityException, NoSuchMethodException {
		AccountSummary[] ai = accountManager.findAccountsByPhoneNumber("8077071251", false, true);
		assertEquals(70615767, ai[0].getBanId());

	}
	public void testFindAccountsByName() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SearchResults result = accountManager.findAccountsByName("*", 
				"", 
				false, 
				"a", 
				false, 
				'*', 
				'*', 
				"*", 
				Brand.BRAND_ID_TELUS, 
				100);
		
		AccountSummary[] as = (AccountSummary[])result.getItems();
		
		assertEquals(99, as.length);
		
	}


	public void testFindAccountsByDealershipCharStringDateInt() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Account[] findAccountsByDealership = accountManager.findAccountsByDealership('O', "A0DLRINACT", new Date("01/01/1999"), 10);
		
		assertEquals(10, findAccountsByDealership.length);
		
		
	}
	
	public void testFindAccountsByDealershipCharStringDateDateInt() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Account[] findAccountsByDealership = accountManager.findAccountsByDealership('O', "A0DLRINACT", new Date("01/01/1999"), new Date(), 10);	
		assertEquals(10, findAccountsByDealership.length);
	}

	public void _testFindLwAccountByPhoneNumber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		
		Account a = accountManager.findLwAccountByPhoneNumber("8077071251");	
		assertEquals(70615767, a.getBanId());		
		System.out.println("AccountInfo = " + a);
		
	}	

	public void testFindMemosInt() throws TelusAPIException, SecurityException, NoSuchMethodException {
		
		MemoCriteria mc=new TMMemoCriteria();
		mc.setBanId(70615767);
		mc.setSubscriberId("8077071251");
		mc.setType("0002");
		mc.setManualText(null);
		mc.setSystemText(null);
		mc.setDateFrom(new Date((2002-1900),(2-1),13));//Year and Month adjustments as per Java util Date
		mc.setDateTo(new Date((2005-1900),(3-1),31));
		mc.setSearchLimit(10);
		Memo[] memos = accountManager.findMemos(mc);
		
		assertEquals("0002", memos[0].getMemoType());
		
	}
	public void testFindSubscriberByPhoneNumber0() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber0("4037109656");
		assertEquals("B00AB00352", subscriber.getDealerCode());
		assertEquals(197806, subscriber.getBanId());
	}
	
	
	
	public void testFindSubscribersBySerialNumber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersBySerialNumber("16809070086");
		assertEquals("B00AB00352", subscriber[0].getDealerCode());
		assertEquals(197806, subscriber[0].getBanId());
	}
	
	public void testFindSubscribersBySerialNumberInclude() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersBySerialNumber("16809070086",true);
		assertEquals("B00AB00352", subscriber[0].getDealerCode());
		assertEquals(197806, subscriber[0].getBanId());
		
	}
	
	public void testFindSubscribersByBAN() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByBAN(197806,10);
		assertEquals("B00AB00352", subscriber[0].getDealerCode());
		assertEquals(197806, subscriber[0].getBanId());
	}
	
	public void testFindPortedSubscribersByBAN() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findPortedSubscribersByBAN(197806,10);
		assertEquals("B00AB00352", subscriber[0].getDealerCode());
		assertEquals(197806, subscriber[0].getBanId());
	}
	
	public void testFindSubscribersByPhoneNumber0() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByPhoneNumber0("8077071251",10,true);
		assertEquals("NAC",subscriber[0].getActivityCode());
		assertEquals("CAPO",subscriber[0].getActivityReasonCode());
		assertEquals("C",subscriber[0].getProductType().trim());	
	}
	public void testFindSubscribersByPhoneNumber() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByPhoneNumber("8077071251",10,false);
		assertEquals("NAC",subscriber[0].getActivityCode());
		assertEquals("CAPO",subscriber[0].getActivityReasonCode());
		assertEquals("C",subscriber[0].getProductType().trim());
		
	}
	public void testFindSubscribersByBANInclude() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByBAN(70615767,10,false);
		assertEquals("CAPO",subscriber[0].getActivityReasonCode());
		assertEquals("C",subscriber[0].getProductType().trim());
	}
	
	public void testFindSubscribersByBanAndFleet() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByBanAndFleet(84, 905, 131077, 8);
		assertEquals("I",subscriber[0].getProductType());
		assertEquals('A',subscriber[0].getStatus());
		assertEquals("0000000014",subscriber[0].getDealerCode());
		
	}
	
	public void testFindSubscribersByBanAndTalkGroup() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Subscriber[] subscriber = accountManager.findSubscribersByBanAndTalkGroup(84, 905, 131077, 1, 1);
		assertEquals("I",subscriber[0].getProductType());
		assertEquals('A',subscriber[0].getStatus());
		assertEquals("0000000014",subscriber[0].getDealerCode());
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	public void testGetPrepaidEventHistory() throws TelusAPIException, SecurityException, NoSuchMethodException {
		PrepaidEventHistory[] peh = accountManager.getPrepaidEventHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28));
		
		for(int i = 0;i<peh.length;i++)
		{
			System.out.println("PrepaidEventHistory"+peh[i].getAmount()+"value" +i);
			System.out.println("PrepaidEventHistory"+peh[i].getDebitCreditFlag());
		}
	    assertEquals(50.00,peh[32].getAmount(),0);
		assertEquals("C", peh[32].getDebitCreditFlag());
	}

	public void testGetPrepaidEventHistoryInfo() throws TelusAPIException, SecurityException, NoSuchMethodException {
		PrepaidEventTypeInfo[] prepaidEventTypes = new PrepaidEventTypeInfo[1];
		PrepaidEventTypeInfo prepaidEventTypesob= new PrepaidEventTypeInfo();
		prepaidEventTypesob.setCode("-73");
		prepaidEventTypes[0]=prepaidEventTypesob;
		PrepaidEventHistoryInfo[] peh = (PrepaidEventHistoryInfo[])accountManager.getPrepaidEventHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28),prepaidEventTypes);
		assertEquals(0,peh[0].getAmount(),0);
		assertEquals("-73",peh[0].getPrepaidEventTypeCode());
		
	}

	public void testGetPrepaidCallHistory() throws TelusAPIException, SecurityException, NoSuchMethodException {
		PrepaidCallHistory[] pch = accountManager.getPrepaidCallHistory("4162809905", getDateInput(2002,7,29),getDateInput(2011,9,28));
		for(int i = 0;i<pch.length;i++)
		{
			System.out.println("PrepaidEventHistory"+pch[i].getChargedAmount()+"value" +i);
			System.out.println("PrepaidEventHistory"+pch[i].getCalledPhoneNumber());
		}
		assertEquals(0.0, pch[0].getChargedAmount(),0);
		assertEquals("9999050123", pch[0].getCalledPhoneNumber());
		
	}
	
	public void testFindPartiallyReservedSubscribersByBan() throws TelusAPIException, SecurityException, NoSuchMethodException {
		String[] subscribers = accountManager.findPartiallyReservedSubscribersByBan(70615767,10);
		assertEquals("7781755380", subscribers[0]);
		
	}
//	public void testFindSubscribersByImsi() throws TelusAPIException, SecurityException, NoSuchMethodException {
//		Subscriber[] subscriber = accountManager.findSubscribersByImsi("900000000880243", true);
//		assertEquals("A001000001", subscriber[0].getDealerCode());
//		assertEquals(70104822, subscriber[0].getBanId());
//	}

	public void testNewPCSPrepaidConsumerAccount() throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException, InvalidCreditCardException, TelusAPIException{
		
		String serialNumber = null;
		int activationType = 0;
		String activationCode = null;
		CreditCard creditCard = null;
		String businessRole = null;
		boolean isFidoConversion = false;
		AuditHeader auditHeader = null;
		PCSPrepaidConsumerAccount ppca = accountManager.newPCSPrepaidConsumerAccount(serialNumber, activationType, activationCode, creditCard, businessRole, isFidoConversion, auditHeader);
		assertEquals('D', ppca.getAccountType());
		
		String associatedHandsetIMEI = null;
		double activationAmount = 0;
		PCSPrepaidConsumerAccount  ppca1 = accountManager.newPCSPrepaidConsumerAccount(serialNumber, associatedHandsetIMEI, activationType, activationCode, creditCard, businessRole, activationAmount, auditHeader);
		assertEquals('D', ppca1.getAccountType());
	}
	
	public void testNewPCSPostpaidBusinessPersonalAccount() throws TelusAPIException{
		PCSPostpaidBusinessPersonalAccount  pcsPostpaidBusinessPersonalAccount =accountManager.newPCSPostpaidBusinessPersonalAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		//System.out.println(pcsPostpaidBusinessPersonalAccount.toString());
		assertEquals('B', pcsPostpaidBusinessPersonalAccount.getAccountType());
		assertEquals('N', pcsPostpaidBusinessPersonalAccount.getAccountSubType());
	}
	
	public void testNewPCSPostpaidBusinessRegularAccount() throws TelusAPIException{
		PCSPostpaidBusinessRegularAccount  pcsPostpaidBusinessRegularAccount =accountManager.newPCSPostpaidBusinessRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		assertEquals('B', pcsPostpaidBusinessRegularAccount.getAccountType());
		assertEquals('A', pcsPostpaidBusinessRegularAccount.getAccountSubType());
	}
	
	public void testNewPCSAccount() throws AccountMatchException, TelusAPIException{
		
		//Method should not allow Account Type/SubTypes other than Buisness any where 
//		accountManager.newPCSAccount(null, 'I', 'A');
//		accountManager.newPCSAccount(null, 'B', 'R');
//		accountManager.newPCSAccount(null, 'B', 'Y');
//		accountManager.newPCSAccount(null, 'C', 'A');

		IDENAccount iden = (IDENAccount) accountManager.findAccountByBAN(70381399); 
		PCSAccount account1 = accountManager.newPCSAccount(iden, 'B', 'A');
		TMPCSPostpaidBusinessRegularAccount result = (TMPCSPostpaidBusinessRegularAccount)account1;
		assertEquals('B',result.getAccountType());
		assertEquals('A',result.getAccountSubType());
		assertEquals('B',result.getDelegate0().getAccountType());
		assertEquals('A',result.getDelegate0().getAccountSubType());
//		
		IDENAccount iden1 = (IDENAccount) accountManager.findAccountByBAN(70381399); 
		PCSAccount account2 = accountManager.newPCSAccount(iden1, 'B', 'N');
		TMPCSPostpaidBusinessPersonalAccount result1 = (TMPCSPostpaidBusinessPersonalAccount)account2;
		assertEquals('B',result1.getAccountType());
		assertEquals('N',result1.getAccountSubType());
		assertEquals('B',result1.getDelegate0().getAccountType());
		assertEquals('N',result1.getDelegate0().getAccountSubType());
		

//		IDENAccount iden2 = (IDENAccount) accountManager.findAccountByBAN(70381399); 
//		PCSAccount account3 = accountManager.newPCSAccount(iden2, 'C', 'Y');
//		TMPCSPostpaidCorporateRegularAccount result2 = (TMPCSPostpaidCorporateRegularAccount)account3;
//		assertEquals('C',result2.getAccountType());
//		assertEquals('Y',result2.getAccountSubType());
//		assertEquals('C',result2.getDelegate0().getAccountType());
//		assertEquals('Y',result2.getDelegate0().getAccountSubType());
		
		IDENAccount iden3 = (IDENAccount) accountManager.findAccountByBAN(70381399); 
		PCSAccount account4 = accountManager.newPCSAccount(iden3, 'B', 'B');
		TMAccount result3 = (TMAccount)account4;
		assertEquals('B',result3.getAccountType());
		assertEquals('B',result3.getAccountSubType());
		assertEquals('B',result3.getDelegate0().getAccountType());
		assertEquals('B',result3.getDelegate0().getAccountSubType());

	}
	
	public void testBusinessAnywherePersonalToBusinessPersonal() throws TelusAPIException{
		PCSPostpaidBusinessPersonalAccount  account =accountManager.newPCSPostpaidBusinessPersonalAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_BUSINESS, AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		assertEquals(AccountSummary.ACCOUNT_TYPE_BUSINESS, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL, newAccount.getAccountSubType());
	}
	
	public void testBusinessPersonalToBusinessAnywherePersonal() throws TelusAPIException{
		PCSPostpaidBusinessPersonalAccount  account =accountManager.newPCSPostpaidBusinessPersonalAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_BUSINESS, AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL);
		assertEquals(AccountSummary.ACCOUNT_TYPE_BUSINESS, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL, newAccount.getAccountSubType());
	}
	
	public void testBusinessAnywherePersonalToConsumerRegular() throws TelusAPIException{
		PCSPostpaidBusinessPersonalAccount  account =accountManager.newPCSPostpaidBusinessPersonalAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_CONSUMER, AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		assertEquals(AccountSummary.ACCOUNT_TYPE_CONSUMER, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR, newAccount.getAccountSubType());
	}
	public void testBusinessAnywhereRegularToBusinessRegular() throws TelusAPIException{
		PCSPostpaidBusinessRegularAccount  account =accountManager.newPCSPostpaidBusinessRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_BUSINESS, AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		assertEquals(AccountSummary.ACCOUNT_TYPE_BUSINESS, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR, newAccount.getAccountSubType());
	}
	
	public void testBusinessRegularToBusinessAnywhereRegular() throws TelusAPIException{
		PCSPostpaidBusinessRegularAccount  account =accountManager.newPCSPostpaidBusinessRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_REGULAR);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_BUSINESS, AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		assertEquals(AccountSummary.ACCOUNT_TYPE_BUSINESS, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR, newAccount.getAccountSubType());
	}
	public void testCorporateAnywhereToCorporate() throws TelusAPIException{
		PCSPostpaidCorporateRegularAccount  account =accountManager.newPCSPostpaidCorporateRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_CORPORATE, AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE);
		assertEquals(AccountSummary.ACCOUNT_TYPE_CORPORATE, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE, newAccount.getAccountSubType());
	}
	public void testCorporateToCorporateAnywhere() throws TelusAPIException{
		PCSPostpaidCorporateRegularAccount  account =accountManager.newPCSPostpaidCorporateRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_CORPORATE);
		Account newAccount=accountManager.changeAccountType(account, AccountSummary.ACCOUNT_TYPE_CORPORATE, AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
		assertEquals(AccountSummary.ACCOUNT_TYPE_CORPORATE, newAccount.getAccountType());
		assertEquals(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE, newAccount.getAccountSubType());
	}
	
	public void testNewEnterpriseAddress() throws TelusAPIException{
		assertNotNull(accountManager.newEnterpriseAddress());
	}
	
	public void testNewAddress() throws TelusAPIException{
		assertNotNull(accountManager.newAddress());
	}
	
	
	
}
