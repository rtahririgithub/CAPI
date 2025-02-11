package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AutoTopUp;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.account.PrepaidEventHistory;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.Brand;
import com.telus.api.reference.PrepaidAdjustmentReason;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;
import com.telus.provider.TestTMProvider;

/**
 * This integration test is dependent on a specific prepaid BAN and
 * subscriber with airtime cards that need to be configured as active.
 * It also needs alignment with the Prepaid system so this test cannot
 * be automated.
 * 
 * BAN:  64715511
 * Subscriber Number:  2042180050
 *
 * 
 * @author Canh Tran
 *
 */
public class TMPrepaidConsumerAccountIntTest extends BaseTest {
	
	String subscriberId = "";//"4160714015";//"4160704246";//"7781664401";//"2042180050", "4163160106";
	int BANId = 0;//70691002;//70688031;//70679462;//20007120;
	String env = "PT148";
	/*
	 * PT148
	 * 70691002	4160714015
	 */
	
	private TMAccountManager accountManager;
	private TestTMProvider testTMProvider;
	private TMPrepaidConsumerAccount testPrepaidAccount;
	
	static {
		localhostWithPT148Ldap();
//		localhostWithPT168Ldap();
		//setupD3();
//		setupS();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		
		//System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		//System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "false");
//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		
	
	}
	
	public TMPrepaidConsumerAccountIntTest(String name) throws Throwable {
		super(name);
	}

	/**
	 * This overrides the AccountInformationHelper url to the locally deployed
	 * EJB on t3://localhost:7001
	 */
	protected void setUp() throws Exception {
		super.setUp();
		/*
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		*/
		//System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
		//System.setProperty("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
		//System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");		                   
		//System.setProperty("com.telus.provider.providerURL", "t3://wld3easeca:8382");		
		
		accountManager = super.provider.getAccountManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		testPrepaidAccount = new TMPrepaidConsumerAccount(testTMProvider, new PrepaidConsumerAccountInfo());
		
		if (env.equals("PT148")) {
			//70691002/4160714015, 
			subscriberId = "4160714015";
			BANId = 70691002;
		} else if (env.equals("PT168")) {
			//70679435/6478766831, 70679469/4161626678
			subscriberId = "4161626678";
			BANId = 70679469;
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
/*	*//**
	 * Ensure we can retrieve the BAN and Subscriber and make sure
	 * that it is a prepaid account
 * @throws NoSuchMethodException 
 * @throws SecurityException 
	 *//*
	public void testGetBANandSubscriber() throws TelusAPIException {
		try {
			ClientAPI capi = ClientAPI.getInstance("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
			ClientAPI.Provider provider = capi.getProvider();
			
			AccountManager acctMgr = provider.getAccountManager();	

			Account acct = acctMgr.findAccountByBAN(BANId);
			assertNotNull(acct);
			assertEquals(BANId, acct.getBanId());
			System.out.println(".............." + acct.getFullName());
			System.out.println();
			
			//System.out.println(acct.getSubscriberCount());
			
			System.out.println(acct.getClass().getName());
			
			PrepaidConsumerAccount prepaidAcct = (PrepaidConsumerAccount) acct;
			prepaidAcct.applyTopUp(1, "hello");

			Subscriber[] subs = acct.getSubscribers(50);
			for (int i = 0; i < subs.length; i++) {
				Subscriber sub = subs[i];
				System.out.println(sub.getSubscriberId());
			}

			
			Subscriber sub = acct.getSubscriber(subscriberId);
			assertNotNull(sub);
			System.out.println(sub);
			
		} catch (TelusAPIException e) {
			e.printStackTrace();
			throw e;
		}
		
	}*/
/*
	public void testApplyTopUpDouble() {
		fail("Not yet implemented");
	}

	public void testApplyTopUpDoubleString() {
		fail("Not yet implemented");
	}

	public void testApplyTopUpAirtimeCard() {
		fail("Not yet implemented");
	}

	public void testApplyAdjustmentPrepaidAdjustmentReasonDoubleStringPrepaidAdjustmentReason() {
		fail("Not yet implemented");
	}

	public void testApplyAdjustmentPrepaidAdjustmentReasonDoubleStringPrepaidAdjustmentReasonBooleanString() {
		fail("Not yet implemented");
	}

	public void testApplyAdjustmentPrepaidAdjustmentReasonDoubleStringPrepaidAdjustmentReasonCharString() {
		fail("Not yet implemented");
	}
*/
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	public void testGetPrepaidEventHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		PrepaidEventHistory[] peh = ai.getPrepaidEventHistory(getDateInput(2002,7,29),getDateInput(2011,9,28));
		assertEquals(50.00,peh[32].getAmount(),0);
	}
	public void testGetPrepaidEventHistory1() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		PrepaidEventTypeInfo[] prepaidEventTypes = new PrepaidEventTypeInfo[1];
		PrepaidEventTypeInfo prepaidEventTypesob= new PrepaidEventTypeInfo();
		prepaidEventTypesob.setCode("-73");
		prepaidEventTypes[0]=prepaidEventTypesob;
		PrepaidEventHistory[] peh = ai.getPrepaidEventHistory(getDateInput(2002,7,29),getDateInput(2011,9,28),prepaidEventTypes);
		for(int i = 0;i<peh.length;i++)
		{
			System.out.println("PrepaidEventHistory"+peh[i].getAmount()+"value" +i);
			System.out.println("PrepaidEventHistory"+peh[i].getDebitCreditFlag());
		}
		assertEquals(0.00,peh[32].getAmount(),0);
		
	}
	public void testGetPrepaidCallHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		PrepaidCallHistory[] pch = ai.getPrepaidCallHistory(getDateInput(2002,7,29),getDateInput(2011,9,28));
		assertEquals(0.00,pch[0].getRate(),0);
		assertEquals("9999050123", pch[0].getCalledPhoneNumber());
	}
	
	public void testChangeBalanceExpiryDate() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		//TC failed as of Sep/2013, remove or fix
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		ai.changeBalanceExpiryDate( new Date((2012-1900),(3-1),31));
		assertEquals(new Date((2012-1900),(3-1),31),ai.getBalanceExpiryDate());
	}
	
	public void testChangeAirtimeRate() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		//TC failed as of Sep/2013, remove or fix
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		ai.changeAirtimeRate(1.00);
		assertEquals(1.00,ai.getAirtimeRate(),0);
	}
	
	public void testChangeAirtimeRate1() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException{
		//TC failed as of Sep/2013, remove or fix
		TMPrepaidConsumerAccount ai = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		ai.changeAirtimeRate(121);
		assertEquals(100.00, ai.getAirtimeRate(), 0);
	}
	
	public void testSave() throws  TelusAPIException  {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		account.save();
	}
	
	public void testGetAutoTopUp() throws  TelusAPIException  {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		AutoTopUp autoTopUp =account.getAutoTopUp(true);
		assertEquals(0.0,autoTopUp.getThresholdAmount(),2);
	}
	
	public void testApplyTopUp() throws  TelusAPIException  {
		try{
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		account.applyTopUp(100,null);
		}catch(TelusAPIException e){
			e.printStackTrace();
			System.out.println("com.telus.prepaid.winpas.server.framework.exception.TelusPrepaidSubscriberNotFoundException: id=PR_SUB_2003; SubscriberProfile - " +
					"Min not found in Prepaid Subscriber Table (PRT_SUBTAB), or doesn't exist in Prepaid Plafrom!");
		}
	}
	
	public void testGetPrepaidActivationCredit() throws  TelusAPIException  {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		double credit= account.getPrepaidActivationCredit();
		assertEquals(0.0,credit,2);
	}
	
	public void testApplyAdjustment() throws  TelusAPIException  {
		try{
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		PrepaidAdjustmentReason[] adjustment= api.getReferenceDataManager().getPrepaidAdjustmentReason();
		
		double amount=100.00;
		String transactionId="id";
		PrepaidAdjustmentReason waive= null;
		char taxOption=Credit.TAX_OPTION_NO_TAX;
		String memoText="memo";
		account.applyAdjustment(adjustment[0], amount, transactionId, waive, taxOption, memoText);
	}catch(TelusAPIException e){
		e.printStackTrace();
		System.out.println("com.telus.prepaid.winpas.server.framework.exception.TelusPrepaidSubscriberNotFoundException: id=PR_SUB_2003; SubscriberProfile - " +
				"Min not found in Prepaid Subscriber Table (PRT_SUBTAB), or doesn't exist in Prepaid Plafrom!");
	}
		
	}
	
	public void testGetPrepaidDeviceDirectFulfillment() throws  TelusAPIException  {
		try {
			
		PrepaidAdjustmentReason[] reasons= api.getReferenceDataManager().getPrepaidDeviceDirectFulfillmentReasons();
		/*
		 * 5890 - Device payment from balance
		 * 5891 - Device Refund on balance
		 */
		System.out.println("reasons: ");
		assertTrue(reasons != null);
		for (PrepaidAdjustmentReason r:reasons) {
			System.out.println(r.toString());
		}
		assertTrue(reasons.length >= 2);
		
		PrepaidAdjustmentReason reason1= api.getReferenceDataManager().getPrepaidDeviceDirectFulfillmentReason("5890");
		assertTrue(reason1.getCode().equals("5890"));
		assertTrue(reason1.getMinimumBalance() > 0);
		assertTrue(reason1.getMinimumAdjustmentAmount() > 0);
		assertTrue(reason1.getMaximumAdjustmentAmount() > 0);
		System.out.println("reason code 5890: ");
		System.out.println(reason1.toString());
		
		PrepaidAdjustmentReason reason2= api.getReferenceDataManager().getPrepaidDeviceDirectFulfillmentReason("5891");
		assertTrue(reason2.getCode().equals("5891"));
		assertTrue(reason2.getMinimumBalance() > 0);
		assertTrue(reason2.getMinimumAdjustmentAmount() > 0);
		assertTrue(reason2.getMaximumAdjustmentAmount() > 0);
		System.out.println("reason code 5891: ");
		System.out.println(reason1.toString());
		
		PrepaidAdjustmentReason reason3= api.getReferenceDataManager().getPrepaidDeviceDirectFulfillmentReason("9999");
		System.out.println("reason code 9999 (non exist): ");
		assertTrue(reason3 == null);
		
		} catch (Throwable th) {
			th.printStackTrace();
		}
	}


	public void testGetPrepaidManualAdjustmentReasons() throws  TelusAPIException  {
		PrepaidAdjustmentReason[] reasons= api.getReferenceDataManager().getPrepaidManualAdjustmentReasons();
		assertTrue(reasons != null);
		System.out.println("reasons: ");
		for (PrepaidAdjustmentReason r:reasons) {
			System.out.println(r.toString());
		}
		assertTrue(reasons.length >= 1);
	}
	
	
	public void testApplyAdjustmentThreshold() throws  TelusAPIException  {
		/*
		 * (200, 250] - THRESHOILD_1 (THRESHOLD_AMOUNT 200)
		 * (250, 290] - THRESHOILD_2 (THRESHOLD_AMOUNT 250)
		 * (290, 300] - THRESHOILD_3 (THRESHOLD_AMOUNT 290)
		 * (300, 10000) - THRESHOILD_4 (THRESHOLD_AMOUNT 300)
		 * 
		 */
		int ban=BANId;//70688031;//70679435;
		Account account = accountManager.findAccountByBAN(ban);
		System.out.println(account.toString());
		
		if (account.isPrepaidConsumer()) {
			
			TMPrepaidConsumerAccount prepaidAccount = (TMPrepaidConsumerAccount)account;
			double balance = prepaidAccount.getBalance();
			System.out.println("Balance: " + prepaidAccount.getBalance());
			System.out.println("Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			
			PrepaidAdjustmentReason[] dfAdjustments= api.getReferenceDataManager().getPrepaidDeviceDirectFulfillmentReasons();
			PrepaidAdjustmentReason dfAdjustment = dfAdjustments[0];
			String transactionId="id";
			PrepaidAdjustmentReason waive= null;
			char taxOption=Credit.TAX_OPTION_NO_TAX;
			String memoText="memo";
			
			System.out.println("Adjusting balance with: " + dfAdjustment.toString());

			//200 - THRESHOILD_1
			balance= 200 - balance;
			prepaidAccount.applyAdjustment(dfAdjustment, balance, transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$200 Balance: " + prepaidAccount.getBalance());
			System.out.println("$200 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals("THRESHOLD_1"));
			
			//250 - THRESHOILD_2
			balance= 50;
			prepaidAccount.applyAdjustment(dfAdjustment, balance, transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$250 Balance: " + prepaidAccount.getBalance());
			System.out.println("$250 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals("THRESHOLD_2"));
			
			//290 - THRESHOILD_3
			balance= 40;
			prepaidAccount.applyAdjustment(dfAdjustment, balance, transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$290 Balance: " + prepaidAccount.getBalance());
			System.out.println("$290 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals("THRESHOLD_3"));
			
			//300 - THRESHOILD_3
			balance= 10;
			prepaidAccount.applyAdjustment(dfAdjustment, balance, transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$300 Balance: " + prepaidAccount.getBalance());
			System.out.println("$300 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals("CAP"));
			
			//1300 - THRESHOILD_3
			balance= 1000;
			prepaidAccount.applyAdjustment(dfAdjustment, balance, transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$1300 Balance: " + prepaidAccount.getBalance());
			System.out.println("$1300 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals("CAP"));
			
			//resetting account to 0 balance
			prepaidAccount.applyAdjustment(dfAdjustment, 0 - prepaidAccount.getBalance(), transactionId, waive, taxOption, memoText);
			prepaidAccount.refresh();
			System.out.println("$0 Balance: " + prepaidAccount.getBalance());
			System.out.println("$0 Balance ThresholdCode: " + prepaidAccount.getBalanceCapOrThresholdCode());
			assertTrue(prepaidAccount.getBalanceCapOrThresholdCode().equals(""));
			
		} else {
			System.out.println("Account with " + ban + " is not a prepaid account.");
		}
	}
	
	public void testFindAccountByPhoneNumber() throws TelusAPIException {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		//System.out.println(account.toString());
		assertTrue(subscriberId.equals(account.getSubscriber0().getPhoneNumber()));
	}
	
	public void testFindAccountByBAN() throws TelusAPIException {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByBAN(BANId);
		System.out.println(account.toString());
		assertTrue(BANId == account.getBanId());
	}
	
	public void testSaveTopUpCreditCard() throws TelusAPIException  {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
		CreditCard creditCard= accountManager.newCreditCard(account);
		creditCard.setToken("100000000000001865670", "519111", "1111");
		creditCard.setExpiryMonth(8);
		creditCard.setExpiryYear(2020);
		account.saveTopUpCreditCard(creditCard);
	}
	
	public void testRemoveTopUpCreditCard() throws TelusAPIException  {
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
	    account.removeTopUpCreditCard();
	}
	
	public void testTopUpCreditCard() throws TelusAPIException {
		//Remove existing CC
		TMPrepaidConsumerAccount account = (TMPrepaidConsumerAccount) accountManager.findAccountByPhoneNumber(subscriberId);
	    account.removeTopUpCreditCard();
	    
	    //Register CC
		CreditCard creditCard= accountManager.newCreditCard(account);
		creditCard.setToken("100000000000001865670", "519111", "1111");
		creditCard.setExpiryMonth(8);
		creditCard.setExpiryYear(2020);
		account.saveTopUpCreditCard(creditCard);
		
		//Update CC
		creditCard.setToken("100000000000001865688", "519111", "1111");
		creditCard.setExpiryMonth(12);
		creditCard.setExpiryYear(2023);
		account.saveTopUpCreditCard(creditCard);
		
	}
	
	
}
