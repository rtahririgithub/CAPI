package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.FollowUp;
import com.telus.api.account.LightWeightSubscriber;
import com.telus.api.account.Memo;
import com.telus.api.account.SearchResults;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;

public class TMAccountSummaryIntTest extends BaseTest {
	
	private TMAccountManager accountManager;
	private TestTMProvider testTMProvider;
	private TMAccountSummary testAccountSumm;
	
	static {
//		System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "false");
		
		/*System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "true");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "true");
		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "true");*/
//		setupEASECA_PT168();
		//setupD3();
		//setupCHNLECA_QA();
		setupINTECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		
//		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");

	}
	public TMAccountSummaryIntTest(String name) throws Throwable {
		super(name);
		
	}
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		AccountInfo ai = new AccountInfo();
		ai.setBanId(12474);
		testAccountSumm = new TMAccountSummary(testTMProvider,ai);
	}
	
	
	public void testGetSubscriberPhoneNumbersByStatus() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		char status='A';
		int maximum=200;
		String[] phonenumber=account.getSubscriberPhoneNumbersByStatus(status, maximum);
		assertEquals(2, phonenumber.length);
	}

	
	public void testSavePin() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		String newPin="1234";
		account.savePin(newPin);
	}
	
	
	public void testGetLastCreditCheckMemo() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(70615311);
		Memo memo=account.getLastCreditCheckMemo();
		System.out.println("getLastCreditCheckMemo"+memo);
		assertEquals(2.001192175E9, memo.getMemoId(),2);
		
	}
	
	public void testGetLastMemo() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		Memo memo=account.getLastMemo("0002");
		assertEquals("9059995925", memo.getSubscriberId());
	}
	
	public void testGetSpecialInstructionsMemo() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(12474);
		Memo memo=account.getSpecialInstructionsMemo();
		assertEquals("", memo.getSubscriberId());
	}
	
	public void testGetMemos() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		int count=100;
		Memo[] memo=account.getMemos(count);
		System.out.println("memo"+memo.length);
		for(int i=0;i<=memo.length;i++)
		System.out.println("memo"+memo[i]);
		assertEquals(100, memo.length);
		
	}
	
	public void testgetFollowUps() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		int count=87;
		FollowUp[] followUps=account.getFollowUps(count);
		System.out.println("followUps"+followUps.length);
		assertEquals(87, followUps.length);
		
	}
	
	public void testGetAuthorizedNames() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		ConsumerName[] consumerNames=account.getAuthorizedNames();
		System.out.println("consumerNames"+consumerNames.length);
		assertEquals(2, consumerNames.length);
		
	}
	public void testSaveAuthorizedNames_1() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		ConsumerName[] authorizedNames=  new ConsumerNameInfo[1];
		authorizedNames[0]=new ConsumerNameInfo();
		authorizedNames[0].setFirstName("SIMPLE");
		authorizedNames[0].setLastName("TEST");
		account.saveAuthorizedNames(authorizedNames);
		
	}
	
//	public void testGetLMSLetterRequests() throws TelusAPIException{
//		Account account = accountManager.findAccountByBAN(266597);
//		SearchResults results  =account.getLMSLetterRequests(new Date(2002-1900,0,01), new Date(2011-1900,0,31), 'C', "4037109656", 50);
//		assertEquals(0, results.getCount());
//	}
	
	public void testGetHotlinedSubscriberPhoneNumber() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		String phoneNumber  =account.getHotlinedSubscriberPhoneNumber();
		assertEquals("6472065737", phoneNumber);
	}
	
	
	public void testGetCorporateName() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		String name  =account.getCorporateName();
		assertEquals("", name);
	}
	
	public void testGetBillNotificationContacts() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		BillNotificationContact[] info  =account.getBillNotificationContacts();
		assertEquals(0, info.length);
	}
	public void testGetLastEBillNotificationSent() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		BillNotificationContact info  =account.getLastEBillNotificationSent();
		assertNull(info);
	}
	
	public void testGetPaperBillSupressionAtActivationInd() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(197806);
		String SupressionAtActivationInd =account.getPaperBillSupressionAtActivationInd();
		assertEquals("UNKNOWN", SupressionAtActivationInd);
	}
	
	public void testGetSubscriberIdsByStatus() throws TelusAPIException{
		Account account = accountManager.findAccountByBAN(6022);
		Date activityDate=new Date();
		String reason="CAN";
		char depositReturnMethod='C';
		String waiver="FEW";
		String memoText="memo";
		ServiceRequestHeader header=new ServiceRequestHeaderInfo();
		
		account.cancel(activityDate, reason, depositReturnMethod, waiver, memoText, header);
	}
	
	
	public void testGetSubscriber() throws TelusAPIException, SecurityException, NoSuchMethodException{
		Account account = accountManager.findAccountByBAN(194587);
		Subscriber subscriber =account.getSubscriber("4037109998");
		assertEquals(7019165,subscriber.getSubscriptionId());
		
		
	}
	
	public void testGetSubscriberByPhoneNumber() throws TelusAPIException, SecurityException, NoSuchMethodException{
		Account account = accountManager.findAccountByBAN(194587);
		Subscriber subscriber =account.getSubscriber("4037109998");
		assertEquals("T3K5N9",subscriber.getAddress().getPostalCode());
		
	}
	
	public void testGetPortProtectionIndicator() throws TelusAPIException, SecurityException, NoSuchMethodException{
		Account account = accountManager.findAccountByBAN(313935);
		assertEquals("N",account.getPortProtectionIndicator());
		
	}
	
	public void testGetLightWeightSubscribers() throws TelusAPIException, SecurityException, NoSuchMethodException{
		Account account = accountManager.findAccountByBAN(194587);
		LightWeightSubscriber[] lws = account.getLightWeightSubscribers(10,true);
		assertEquals("4037109998",lws[0].getPhoneNumber());	
		
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	public void testGetSubscriberIdsByServiceGroupFamily()
			throws TelusAPIException, SecurityException, NoSuchMethodException {

		Account account = accountManager.findAccountByBAN(70615767);
		String[] sub = account.getSubscriberIdsByServiceGroupFamily(
				ServiceSummary.FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING,
				getDateInput(2011, 11, 15));
		System.out.println("values for FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING ");
		for (int i = 0; i < sub.length; i++) {
			System.out.print("\t" + sub[i].toString());
			assertEquals(sub[i].toString(), "8077071251");
		}
	}
	
	public void testTBSChanges() throws TelusAPIException{

		AccountInfo accInfo = PostpaidCorporatePersonalAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
		accInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CORPORATE);
		accInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE);
//		accInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL);
//		accInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
		
		
//		AccountInfo accInfo = PostpaidCorporateRegularAccountInfo.newInstance(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
//		accInfo.setAccountType(AccountSummary.ACCOUNT_TYPE_CORPORATE);
//		accInfo.setAccountSubType(AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR);
				
		System.out.println("isCorporate : "+accInfo.isCorporate());
		System.out.println("isCorporateIDEN : "+accInfo.isCorporateIDEN());
		System.out.println("isCorporatePCS : "+accInfo.isCorporatePCS());
		System.out.println("isCorporateRegional : "+accInfo.isCorporateRegional());
		System.out.println("isCorporateRegular : "+accInfo.isCorporateRegular());
		System.out.println("isPCSPostpaidCorporateRegularAccount : "+accInfo.isPCSPostpaidCorporateRegularAccount());
		System.out.println("isPostpaidBusinessPersonal : "+accInfo.isPostpaidBusinessPersonal());
		System.out.println("isPCS : "+accInfo.isPCS());
		System.out.println("isPostpaidCorporateRegular : "+accInfo.isPostpaidCorporateRegular());
		System.out.println("isPostpaid : "+accInfo.isPostpaid());
		System.out.println("isPostpaidBusinessRegular : "+accInfo.isPostpaidBusinessRegular());
		System.out.println("isPostpaidCorporatePersonal : "+accInfo.isPostpaidCorporatePersonal());
		
	}
	
}
