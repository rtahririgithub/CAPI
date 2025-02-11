package com.telus.cmb.account.informationhelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.Charge;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.account.SubscribersByDataSharingGroupResult;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.DataSharingGroup;
import com.telus.api.reference.SeatType;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FollowUpTextInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.SubscriberEligibilitySupportingInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.eas.framework.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.eas.framework.info.ChargeIdentifierInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

public class AccountInformationHelperImplEJBIntTest {

	AccountInformationHelperRemote impl = null;
	//String url = "t3://ln98557:31022";  //PT168
	//String url="t3://cmosr-custinfomgmt-dv103.tmi.telus.com:12021";
	//String url="t3://ln98550.corp.ads:12022";	
	//String url="t3://ln98557.corp.ads:30022";
	//String url="t3://sn25132.corp.ads:60021"; 
	//String url = "t3://lp97396:50025";
	//String url = "t3://localhost:7001";  //local
	String url="t3://ln99244:43024";
	//String url="t3://ln99231:30022"; //PT148
	
	//String url="t3://cmosr-custinfomgmt2-pt148.tmi.telus.com:30022";


	String sessionId;
	TestPointResultInfo testPointResultInfo;
	//	@Autowired
	Integer sampleBAN = 197806;

	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getAccountLifecycleManagerRemote(context);		
		//		sessionId = managerImpl.openSession("18654", "apollo", "OLN");
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}

	private void getAccountLifecycleManagerRemote(Context context) throws Exception{
		AccountInformationHelperHome accountInformationHelperHome = (AccountInformationHelperHome) context.lookup("AccountInformationHelper#com.telus.cmb.account.informationhelper.svc.impl.AccountInformationHelperHome");
		impl = (AccountInformationHelperRemote) accountInformationHelperHome.create();
	}

	@Test
	public void testRetrieveAccountsByBan() throws ApplicationException, RemoteException {
		Collection<AccountInfo> ais = impl.retrieveAccountsByBan(new int[]{197806});

		assertEquals(1, ais.size());
		for (AccountInfo ai : ais) {
			assertEquals(197806, ai.getBanId());
		}
	}
	@Test
	public void testRetrieveAccountsByBusinessName() throws ApplicationException, RemoteException {		
		SearchResultsInfo result = impl.retrieveAccountsByBusinessName("*",
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

	@Test
	public void testRetrieveAccountByBan() throws ApplicationException, RemoteException  {
		AccountInfo ai = impl.retrieveAccountByBan(26262530);
System.out.println(ai);
		//assertEquals(313935, ai.getBanId());
	}

	@Test
	public void testRetrievePhoneNumbersForBAN() throws ApplicationException, RemoteException{
		@SuppressWarnings("unchecked")
		Map<String, String> phoneNumbers = (Map<String, String>)impl.retrievePhoneNumbersForBAN(313935);
		assertEquals(0, phoneNumbers.size());
		System.out.println(phoneNumbers);
	}

	@Test
	public void testRetrieveAccountStatusByBan() throws ApplicationException, RemoteException  {
		assertEquals("O", impl.retrieveAccountStatusByBan(313935));
	}

	@Test
	public void testRetrieveVoiceUsageSummary() throws ApplicationException, RemoteException  {
		assertEquals(0, impl.retrieveVoiceUsageSummary(313935, null).length);
	}

	@Test
	public void testRetrieveAccountsByPostalCode() throws ApplicationException, RemoteException{
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPostalCode("RIOPEL", "R3N0S3", 10);

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(816166, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveAccountByPhoneNumber() throws ApplicationException, RemoteException {
		Account account = impl.retrieveAccountByPhoneNumber("5871250542");
		assertEquals(true, account.isPostpaidConsumer());
	}

	@Test
	public void testRetrieveAccountByPhoneNumberStringBooleaBoolean() throws ApplicationException, RemoteException {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPhoneNumber("7807198318", true, false);

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(292007, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveAccountByPhoneNumberString() throws ApplicationException, RemoteException {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPhoneNumber("7807198318");

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(292007, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveMemos() throws ApplicationException, RemoteException {
		@SuppressWarnings("unchecked")
		Collection<MemoInfo> memo = (Collection<MemoInfo>)impl.retrieveMemos(292007, 500);
		assertEquals(500, memo.size());
		for (MemoInfo mi : memo) {
			assertEquals("0003", mi.getMemoType());
			break;
		}		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveMemosByCriteria() throws ApplicationException, RemoteException  {

		MemoCriteriaInfo mcinfo=new MemoCriteriaInfo();
		mcinfo.setBanId(816166);
		mcinfo.setSubscriberId("2047980182");
		mcinfo.setType("0002");
		mcinfo.setManualText(null);
		mcinfo.setSystemText(null);
		//Year and Month adjustments as per Java util Date
		mcinfo.setDateFrom(new Date((2002-1900),(2-1),13));
		mcinfo.setDateTo(new Date((2005-1900),(3-1),31));
		mcinfo.setSearchLimit(10);
		@SuppressWarnings("unchecked")
		Collection<MemoInfo> memoCriteria = (Collection<MemoInfo>)impl.retrieveMemos(mcinfo);
		assertEquals(0, memoCriteria.size());
		for (MemoInfo mi : memoCriteria) {
			assertEquals("0002", mi.getMemoType());
			break;
		}
	}

	@Test
	public void testRetrieveLastMemo()throws ApplicationException, RemoteException {
		MemoInfo memoinfo=impl.retrieveLastMemo(816166, "1040");
		assertEquals("2047980182", memoinfo.getSubscriberId());
	}

	@Test
	public void testRetrieveAccountsByName() throws ApplicationException, RemoteException{
		SearchResultsInfo result = impl.retrieveAccountsByName("*", 
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

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveAccountsByDealership()throws ApplicationException, RemoteException {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsByDealership('O', "A0010000010000"
				, new Date("01/01/1999"), new Date("01/01/2999"), 10);

		assertEquals(10, accounts.size());
	}

	@Test
	public void testRetrieveAccountsBySalesRep()throws ApplicationException, RemoteException {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);

		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySalesRep(
				'O', "A0010000010000", "", cal.getTime(), new Date(), 10);

		assertEquals(10, accounts.size());
	}

	@Test
	public void testRetrieveAccountInfoForPayAndTalkCustomer() throws ApplicationException, RemoteException {
		if (System.getProperty("env") != null && System.getProperty("env").equals("PT148")) {
			impl.retrieveAccountInfoForPayAndTalkSubscriber(1905787, "4169930663");
		}
	}
	@Test
	public void testRetrieveBanIds() throws ApplicationException, RemoteException{
		int[] bans = impl.retrieveBanIds('I', 'R', 'O', 10);

		assertEquals(10, bans.length);
	}

	@Test
	public void testRetrieveBanIdsByAddressType() throws ApplicationException, RemoteException{
		int[] bans = impl.retrieveBanIdByAddressType('I', 'R', 'O', 'C', 10);

		assertEquals(10, bans.length);
	}

	@Test
	public void testRetrieveAttachedSubscribersCount() throws ApplicationException, RemoteException {
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo();
		fleetIdentityInfo.setFleetId(131072);
		fleetIdentityInfo.setUrbanId(905);
		assertEquals(0, impl.retrieveAttachedSubscribersCount(25, fleetIdentityInfo));
	}

	@Test
	public void testretrieveFollowUpAdditionalText() throws ApplicationException, RemoteException{
		Collection<FollowUpTextInfo> followUpTextInfo=impl.retrieveFollowUpAdditionalText(20007401, 11545271);
		assertEquals(0, followUpTextInfo.size());
		for (FollowUpTextInfo fti : followUpTextInfo) {
			assertEquals("12114",fti.getOperatorId());
			break;
		}	
	} 

	@Test
	public void testretrieveFollowUpHistory()throws ApplicationException, RemoteException{
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUpHistory(9523433);
		assertEquals(0, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("901781  ",fi.getAssignedToWorkPositionId());
			assertEquals(10000283, fi.getBanId());
			assertEquals("ADJT", fi.getFollowUpType());
			break;
		}	

	}

	@Test
	public void testretrieveFollowUpInfoByBanFollowUpID() throws ApplicationException, RemoteException{
		FollowUpInfo followInfo=impl.retrieveFollowUpInfoByBanFollowUpID(12474, 9523433);
		assertEquals("M001033038", followInfo.getSubscriberId());
	}

	@Test
	public void testretrieveFollowUpsByCriteria()throws ApplicationException, RemoteException{

		FollowUpCriteriaInfo fci=new FollowUpCriteriaInfo();
		fci.setWorkPositionId("RS_C0020");
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUps(fci);
		assertEquals(1085, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("12812",fi.getOpenedBy());
			break;
		}	
	}

	@Test
	public void testretrieveFollowUps() throws ApplicationException, RemoteException{
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUps(8, 1000);
		assertEquals(20, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			/*assertEquals("RS_C0020",fi.getAssignedToWorkPositionId());
			assertEquals(22405927,fi.getFollowUpId());*/
			System.out.println(fi.getDueDate() ); 
			System.out.println(fi.getAssignedToWorkPositionId() ); 
			System.out.println(fi.getBan() ); 
			System.out.println(fi.getFollowUpType() ); 
			System.out.println(fi.getText() ); 
			System.out.println(fi.getType() ); 
			//System.out.println(fi.getFollowUpId());
			//break;
		}	

		Collection<FollowUpInfo> followUpInfo1=impl.retrieveFollowUps(70103567, -1);
		assertEquals(2, followUpInfo1.size());
		for (FollowUpInfo fi : followUpInfo1) {
			assertEquals("RS_C0020",fi.getAssignedToWorkPositionId());
			assertEquals(11747313,fi.getFollowUpId());
			break;
		}	
	}

	@Test
	public void testretrieveFollowUpStatistics() throws ApplicationException, RemoteException{
		FollowUpStatisticsInfo followUpStats=impl.retrieveFollowUpStatistics(254977);
		assertTrue(followUpStats.hasOpenFollowUps());
	}

	@Test
	public void testretrieveLastFollowUpIDByBanFollowUpType() throws ApplicationException, RemoteException{
		int followUpId=impl.retrieveLastFollowUpIDByBanFollowUpType(254977, "ADJT");
		assertEquals(11630997,followUpId);
	}

	@Test
	public void testRetrieveProductSubscribersList() throws ApplicationException, RemoteException{
		ProductSubscriberList[] productSubscriberList = impl.retrieveProductSubscriberLists(70701556);
		validateSubscriberIdListBeforeCancelOrSuspendSeats(productSubscriberList, new String[]{"6471251838"}, false);
		
		
	}
	
	private List<String> validateSubscriberIdListBeforeCancelOrSuspendSeats(ProductSubscriberList[] productSubscriberList,String[] subscriberId,boolean isValidateSuspendedSeats)  {
		List<String> subscriberIds = new ArrayList<String>();
		
		for (int i = 0; i < productSubscriberList.length; i++) {		
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberList[i].getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriberList[i].getSuspendedSubscriberIdentifiers();
			
			for (int j = 0; j < activeSubscriberIdentifiers.length; j++) {
				for (int k = 0; k < subscriberId.length; k++) {
					if (subscriberId[k].equalsIgnoreCase(activeSubscriberIdentifiers[j].getSubscriberId())) {
						if (activeSubscriberIdentifiers[j].getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
					          subscriberIds.addAll(filterSubscriberIdListBySeatGroup(productSubscriberList,activeSubscriberIdentifiers[j].getSeatGroup(),true));
						} else {
							subscriberIds.add(subscriberId[k]);
						}
					}
				}
			}

			if (isValidateSuspendedSeats == true) {
				for (int j = 0; j < suspendedsubscriberIdentifiers.length; j++) {
				for (int k = 0; k < subscriberId.length; k++) {
					if (subscriberId[k].equalsIgnoreCase(suspendedsubscriberIdentifiers[j].getSubscriberId())) {
						if (suspendedsubscriberIdentifiers[j].getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
							subscriberIds.addAll(filterSubscriberIdListBySeatGroup(productSubscriberList,suspendedsubscriberIdentifiers[j].getSeatGroup(), true));
						} else
							subscriberIds.add(subscriberId[k]);
					}
				}
				}
			}
		}
		return subscriberIds;
	}
	
	private List<String> filterSubscriberIdListBySeatGroup(ProductSubscriberList[] productSubscriberList,String seatGroup,boolean isFilterSuspenedSeats)  {
		List<String> subscriberIds = new ArrayList<String>();
		for (int i = 0; i < productSubscriberList.length; i++) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberList[i].getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriberList[i].getSuspendedSubscriberIdentifiers();
			for(int j = 0; j < activeSubscriberIdentifiers.length; j++) {
				if (activeSubscriberIdentifiers[i].getSeatGroup().equalsIgnoreCase(seatGroup)) {
					subscriberIds.add(activeSubscriberIdentifiers[i].getSubscriberId());
				}
			}

			if (isFilterSuspenedSeats == true) {
				for (int k = 0; k < suspendedsubscriberIdentifiers.length; k++) {
					if (suspendedsubscriberIdentifiers[i].getSeatGroup().equalsIgnoreCase(seatGroup)) {
						subscriberIds.add(suspendedsubscriberIdentifiers[i].getSubscriberId());
					}
				}
			}
		}
		return subscriberIds;
	}
	
	@Test
	public void testRetrieveUnpaidDataUsageTotal() throws ApplicationException, RemoteException {
		double result = impl.retrieveUnpaidDataUsageTotal(254977, new Date());
		assertEquals(0,result,0);
	}
	@Test
	public void testRetrieveUnpaidAirTimeTotal() throws ApplicationException, RemoteException {
		double result = impl.retrieveUnpaidAirTimeTotal(254977);
		assertEquals(0,result,0);
	}

	@Test
	public void testGetLastEBillNotificationSent() throws ApplicationException, RemoteException {
		BillNotificationContactInfo billNot = impl.getLastEBillNotificationSent(2794383);
		System.out.println(billNot.toString());
	
	}
	@Test
	public void testGetLastEBillRegistrationReminderSent() throws ApplicationException, RemoteException {

		EBillRegistrationReminderInfo billReg = impl.getLastEBillRegistrationReminderSent(254977);
		try{
			assertEquals(null,billReg.getActivatedPhoneNumber());
			fail("Exception Expected");
		}catch(Exception e)
		{}

	}
	@Test
	public void testGetBillNotificationHistory() throws ApplicationException, RemoteException{
		Collection<BillNotificationHistoryRecord> billNot = impl.getBillNotificationHistory(254977,"EPOST");
		assertEquals(0,billNot.size());
		for(BillNotificationHistoryRecord hisInfo : billNot){
			assertEquals("1234",hisInfo.getSrcReferenceId());
			assertEquals(true,hisInfo.getMostRecentInd());
			break;
		}	
	} 

	@Test
	public void testExpireBillNotificationDetails() throws ApplicationException, RemoteException {
		impl.expireBillNotificationDetails(12474);		
	}

	@Test
	public void testRetrieveFleetsByBan() throws ApplicationException, RemoteException {

		FleetInfo[] fleets = impl.retrieveFleetsByBan(12474);

		assertEquals(1, fleets.length);

		for (FleetInfo fleet : fleets) {
			assertEquals(2070, fleet.getAssociatedAccountsCount());
		}

		assertEquals(1, impl.retrieveFleetsByBan(12474).length);
	}

	@Test
	public void testRetrieveAccountsByImsi() throws ApplicationException, RemoteException {
		AccountInfo ai = impl.retrieveAccountByImsi("214078036613338");

		assertEquals(2083176, ai.getBanId());

		assertNull(impl.retrieveAccountByImsi(""));
	}

	@Test
	public void testRetrieveSubscriberIdsByStatus() throws ApplicationException, RemoteException{
		assertEquals(3, impl.retrieveSubscriberIdsByStatus(12474, 'C', 50).size());
	}


	@Test
	public void testRetrieveLastCreditCheckMemoByBan() throws ApplicationException, RemoteException{ 
		MemoInfo mi=impl.retrieveLastCreditCheckMemoByBan(6011739);
		assertEquals(6011739, mi.getBanId());
	}

	@Test
	public void testRetrieveSubscriberPhoneNumbersByStatus() throws ApplicationException, RemoteException{
		assertEquals(2, impl.retrieveSubscriberPhoneNumbersByStatus(12474, 'A', 50).size());
	}
	@Test
	public void testGetClientAccount()throws ApplicationException, RemoteException	{
		assertEquals(10028639,impl.getClientAccountId(6011739));

	}
	@Test
	public void testretrieveCorporateName()throws ApplicationException, RemoteException	{
		assertEquals("SUPER CORPORATION",impl.retrieveCorporateName(10000));
	}

	@Test
	public void testRetrieveLastPaymentActivity() throws ApplicationException, RemoteException {
		PaymentHistoryInfo info = impl.retrieveLastPaymentActivity(124);		
		assertNull(info); 	

		info = impl.retrieveLastPaymentActivity(12474);		
		assertNotNull(info); 
	}

	@Test
	public void testRetrieveSubscriberInvoiceDetails()throws ApplicationException, RemoteException	{
		assertEquals(3,impl.retrieveSubscriberInvoiceDetails(6000055, 8).size());
		assertEquals(0,impl.retrieveSubscriberInvoiceDetails(6001399, 1).size());
	}
	@Test
	public void testRetrieveAssociatedTalkGroupsCount() throws ApplicationException, RemoteException{
		FleetIdentityInfo flInf= new FleetIdentityInfo();
		flInf.setUrbanId(905);
		flInf.setFleetId(16389);
		assertEquals(0, impl.retrieveAssociatedTalkGroupsCount(flInf,6000055));
		flInf.setUrbanId(0);
		flInf.setFleetId(0);
		assertEquals(0, impl.retrieveAssociatedTalkGroupsCount(flInf, 12474));
	}
	@Test
	public void testRetrieveBilledCharges()throws ApplicationException, RemoteException{
		Collection<ChargeInfo> chInf = impl.retrieveBilledCharges(197806,54,"4037109656",new java.sql.Date(2002-1900,8,23),new java.sql.Date(2002-1900,8,23));
		assertEquals(0,chInf.size());
		for(ChargeInfo conInfo : chInf){
			assertEquals(0,conInfo.getAmount(),0);
			assertEquals(54 ,conInfo.getBillSequenceNo());
			assertEquals("CHG " ,conInfo.getChargeCode());
			break;
		}
	}

	@Test
	public void testRetrieveBillNotificationContacts()throws ApplicationException, RemoteException{
		Collection<BillNotificationContactInfo> chargeInf = impl.retrieveBillNotificationContacts(12474);
		assertEquals(0,chargeInf.size());
	}
	@Test
	public void testisFeatureCategoryExistOnSubscribers()throws ApplicationException, RemoteException{

		assertEquals(true,impl.isFeatureCategoryExistOnSubscribers(8,"VM"));
		assertEquals(false,impl.isFeatureCategoryExistOnSubscribers(12,"VM"));
	}
	@Test
	public void testisEBillRegistrationReminderExist()throws ApplicationException, RemoteException{
		assertEquals(false,impl.isEBillRegistrationReminderExist(12474));
	}

	@Test
	public void testhasEPostSubscription()throws ApplicationException, RemoteException{

		assertEquals(false,impl.hasEPostSubscription(8));
		assertEquals(false,impl.hasEPostSubscription(12474));
	}

	@Test
	public void testRetrieveAlternativeAddressByBan() throws ApplicationException, RemoteException {
		AddressInfo addressInfo = impl.retrieveAlternateAddressByBan(sampleBAN);

		assertEquals("T2J0W8", addressInfo.getPostalCode());

		addressInfo = null;	
		addressInfo = impl.retrieveAlternateAddressByBan(9999);
		assertNotNull(addressInfo);
	}

	@Test
	public void testRetrieveBusinessList() throws ApplicationException, RemoteException {
		BusinessCreditIdentityInfo[] retrieveBusinessList = impl.retrieveBusinessList(sampleBAN);
		assertNotNull(retrieveBusinessList);
		assertEquals(0, retrieveBusinessList.length);

		retrieveBusinessList = impl.retrieveBusinessList(12474);
		assertEquals(10, retrieveBusinessList.length);

		assertEquals("1,COMPANY OPTION 1,1301 LOUGAR AVE,SARNIA,ON", retrieveBusinessList[0].getCompanyName());
		assertEquals(1.0, retrieveBusinessList[0].getMarketAccount(), 0);		
		assertEquals("2,COMPANY OPTION 2,1302 LOUGAR AVE,SARNIA,ON", retrieveBusinessList[1].getCompanyName());
		assertEquals(2.0, retrieveBusinessList[1].getMarketAccount(), 0);

	}

	@Test
	public void testRetrieveAuthorizedNames() throws ApplicationException, RemoteException {
		ConsumerNameInfo[] results = impl.retrieveAuthorizedNames(12474);
		assertNotNull(results);
		assertEquals(0, results.length);

		results = null;
		results = impl.retrieveAuthorizedNames(8);
		assertEquals(0, results.length);

		assertEquals("MR.", results[0].getTitle());
		assertEquals("NAMEA  NAMEB   NAMEC", results[0].getFirstName());
		assertEquals("MIDNA   MIDNB   MIDNC", results[0].getMiddleInitial());
		assertEquals("LASTNA    LASTNB   LASTNC", results[0].getLastName());
		assertNull(results[0].getGeneration());
		assertEquals("OTHERNA    OTHERNB     OTHERNC", results[0].getAdditionalLine());

		assertNull(results[1].getTitle());
		assertEquals("A", results[1].getFirstName());
		assertEquals("B", results[1].getMiddleInitial());
		assertEquals("C", results[1].getLastName());
		assertNull(results[1].getGeneration());
		assertNull(results[1].getAdditionalLine());		
	}

	@Test
	public void testRetrieveDepositHistory() throws ApplicationException, RemoteException {
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		Calendar toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2006);

		DepositHistoryInfo[] list = impl.retrieveDepositHistory(816166, fromCal.getTime(), toCal.getTime());

		assertEquals(0, list.length);

		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2003);

		list = impl.retrieveDepositHistory(6016549, fromCal.getTime(), toCal.getTime());

		assertEquals(0, list.length);

		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2010);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2011);

		list = impl.retrieveDepositHistory(6012776, fromCal.getTime(), toCal.getTime());

		assertEquals(0, list.length);	
	}

	@Test
	public void testretrieveCreditByFollowUpId() throws ApplicationException, RemoteException{
		Collection<CreditInfo> creditInfo=impl.retrieveCreditByFollowUpId(7644142);
		assertEquals(1, creditInfo.size());
		for (CreditInfo ci : creditInfo) {
			//assertEquals("9057160719",ci.getSubscriberId());
			assertEquals("ADJ ", ci.getActivityCode());
			assertEquals(50,ci.getAmount(),0);
			break;
		}	
	}


	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits2() throws ApplicationException, RemoteException{
		SearchResultsInfo sri1=impl.retrieveCredits(805938, new Date(100,03,22), new Date(100,04,12), "B",  '1', "", "", 500);
		assertEquals(1, sri1.getItems().length);
		SearchResultsInfo sri2=impl.retrieveCredits(2297038, new Date(104,0,01), new Date(107,12,01), "U",  '1', "", "", 500);
		assertEquals(0, sri2.getItems().length);
		SearchResultsInfo sri3=impl.retrieveCredits(6016913, new Date(90,0,01), new Date(109,12,01), "A",  '1', "", "", 100);
		assertEquals(200, sri3.getItems().length);

	}
	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits3() throws ApplicationException, RemoteException{
		SearchResultsInfo sri1=impl.retrieveCredits(70649688, getDateInput(2011, 01, 11), getDateInput(2013, 12, 21), "A", "", null, 'C', "", 500);
		assertEquals(42, sri1.getItems().length);
	
	}

	@Test
	public void testRetrieveDepositAssessdHistoryList() throws ApplicationException, RemoteException {
		DepositAssessedHistoryInfo[] list = impl.retrieveDepositAssessedHistoryList(81);
		assertEquals(5, list.length);

		list = null;
		list = impl.retrieveDepositAssessedHistoryList(1);
		assertNotNull(list);
		assertEquals(0, list.length);
	}
	@Test
	public void testGetInvoiceProperties() throws ApplicationException, RemoteException {
		InvoicePropertiesInfo invoicePropertiesInfo = impl.getInvoiceProperties(6022);
		assertEquals("1",invoicePropertiesInfo.getInvoiceSuppressionLevel());
		assertEquals("1",invoicePropertiesInfo.getHoldRedirectDestinationCode());

	}


	@Test
	public void testRetrieveCharges() throws ApplicationException, RemoteException {
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2012);
		Charge[] charges = impl.retrieveCharges(70663935, new String[]{"STB   "}, Account.BILL_STATE_ALL, 'B', "2095773818", fromCal.getTime(), new Date(), 100);
		System.out.println("start");
		for (Charge charge : charges) {
			System.out.println("charges"+charge);
		}

		System.out.println("end");
		/**assertEquals(5, charges.length);
		Calendar fromCal1 = Calendar.getInstance();
		fromCal1.set(Calendar.YEAR, 1999);
		charges = impl.retrieveCharges(8, new String[]{"CNRD"}, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, "4033404108", fromCal1.getTime(), new Date(), 10);

		assertEquals(0, charges.length);*/
	}

	@Test
	public void testRetrievePaymentHistory()throws ApplicationException, RemoteException{
		Collection<PaymentHistoryInfo> paymentHistoryList = new ArrayList<PaymentHistoryInfo>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		paymentHistoryList =impl.retrievePaymentHistory(6093272,fromCal.getTime(),new Date());
		assertEquals(6,paymentHistoryList.size());
		for(PaymentHistoryInfo paymentHistoryInfo:paymentHistoryList){
			assertEquals("CH",paymentHistoryInfo.getPaymentMethodCode());
			assertEquals(3181.46,paymentHistoryInfo.getOriginalAmount(),0);
			break;
		}
	}

	@Test
	public void testRetrieveOriginalDepositAssessedHistoryList() throws ApplicationException, RemoteException {
		assertEquals(6, impl.retrieveOriginalDepositAssessedHistoryList(44126808).length);
		assertEquals(0, impl.retrieveOriginalDepositAssessedHistoryList(1).length);
	}
	@Test
	public void testRetrieveHotlinedSubscriberPhoneNumber()throws ApplicationException, RemoteException {
		assertEquals("7806994504",impl.retrieveHotlinedSubscriberPhoneNumber(6093272));
		assertEquals("",impl.retrieveHotlinedSubscriberPhoneNumber(2));
	}
	@Test
	public void testRetrieveInvoiceHistory()throws ApplicationException, RemoteException {
		Collection<InvoiceHistoryInfo> invoiceHistoryInfo = new ArrayList<InvoiceHistoryInfo>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1998);
		invoiceHistoryInfo = impl.retrieveInvoiceHistory(194186, fromCal.getTime(), new Date());
		assertEquals(52,invoiceHistoryInfo.size());
		for(InvoiceHistoryInfo invObj:invoiceHistoryInfo){
			assertEquals(20.4,invObj.getAmountDue(),0);
			assertEquals(373.34,invObj.getPreviousBalance(),0);
			break;
		}
	}
	@Test
	public void tesRetrieveAttachedSubscribersCountForTalkGroup()throws ApplicationException, RemoteException {
		assertEquals(1,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 70, 1, 6000055));
		assertEquals(1,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 70, 2, 6000055));
		assertEquals(0,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 3, 20004220));
	}
	@Test
	public void testRetrievePaymentActivities()throws ApplicationException, RemoteException{
		Collection<PaymentActivityInfo> paymentActivity = new ArrayList<PaymentActivityInfo>();
		paymentActivity = impl.retrievePaymentActivities(8, 884811);
		assertEquals(1,paymentActivity.size());
		for(PaymentActivityInfo payActInf:paymentActivity){
			assertEquals("PYM",payActInf.getActivityCode().trim());
			assertEquals(146.47,payActInf.getAmount(),0);
			assertEquals(null,payActInf.getCreditCardAuthorizationCode());
			assertEquals(null,payActInf.getExceptionReasonCode());
		}
	}
	@Test
	public void testRetrievePaymentMethodChangeHistory()throws ApplicationException, RemoteException {
		Collection<PaymentMethodChangeHistoryInfo> paymentActivity = new ArrayList<PaymentMethodChangeHistoryInfo>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1999);
		paymentActivity = impl.retrievePaymentMethodChangeHistory(196200, fromCal.getTime(),new Date());
		assertEquals(1,paymentActivity.size());
		for(PaymentMethodChangeHistoryInfo payActInf:paymentActivity){
			assertEquals("100000000000000006551",payActInf.getCreditCardToken());
			assertEquals("341400",payActInf.getCreditCardExpiry());
			assertEquals("0107",payActInf.getBankCode().trim());
			assertEquals("C",payActInf.getBankAccountNumber().trim());
		}
	}

	@Test
	public void testRetrieveRefundHistory()throws ApplicationException, RemoteException{
		List<RefundHistoryInfo> refundHistoryList = new ArrayList<RefundHistoryInfo>();
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1999);
		refundHistoryList =impl.retrieveRefundHistory(750714,fromCal.getTime(),new Date());
		assertEquals(1,refundHistoryList.size());
		for(RefundHistoryInfo refundHistoryInfo:refundHistoryList){
			assertEquals("RFDPRT",refundHistoryInfo.getReasonCode());
			assertEquals("RFN ",refundHistoryInfo.getCode());
			assertEquals(304.42,refundHistoryInfo.getAmount(),0);
			break;
		}
	}

	@Test
	public void testRetrievePCSNetworkCountByBan()throws ApplicationException, RemoteException{
		assertEquals(new Integer(4),impl.retrievePCSNetworkCountByBan(8).get("C"));
		assertEquals(new Integer(0),impl.retrievePCSNetworkCountByBan(8).get("H"));

	}

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts1() throws ApplicationException, RemoteException{
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(70547477,PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL);
		assertEquals(0,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());

		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(6000055,4);
		assertEquals(0,poolPP1.size());
		assertEquals(0,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts2() throws ApplicationException, RemoteException{
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= impl.retrievePoolingPricePlanSubscriberCounts(70532121, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL);
		assertEquals(4,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());

		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= impl.retrievePoolingPricePlanSubscriberCounts(70532121, 7);
		assertEquals(1,poolPP1.size());
		assertEquals(7,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}

	@Test
	public void testretrieveServiceSubscriberCounts() throws ApplicationException, RemoteException{
		Collection<ServiceSubscriberCount> scc=impl.retrieveServiceSubscriberCounts(70617029, 
				new String[]{"3PNTWD35","3S9110   ","3SFIMO   ","RIMTM    ","SDOC1    ","SE911    ","SFBC     "}, true);
		assertEquals(2,scc.size());
		assertEquals("3S9110   ", ((ServiceSubscriberCount[])scc.toArray(new ServiceSubscriberCount[scc.size()]))[0].getServiceCode());
	}

	@Test
	public void testretrieveMinutePoolingEnabledPricePlanSubscriberCounts() throws ApplicationException, RemoteException{
		Collection<PricePlanSubscriberCount> ppsc=impl.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(660647, new String[]{"H","M","O","R"});
		assertEquals(3,ppsc.size());
		assertEquals("PSHAR10MP", ((PricePlanSubscriberCount[])ppsc.toArray(new PricePlanSubscriberCount[ppsc.size()]))[0].getPricePlanCode());
	}

	@Test
	public void testretrieveDollarPoolingPricePlanSubscriberCounts() throws ApplicationException, RemoteException{
		Collection<PricePlanSubscriberCountInfo> ppsci=impl.retrieveDollarPoolingPricePlanSubscriberCounts(816166,"I");
		assertEquals(1,ppsci.size());
		assertEquals("M001956744",((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}

	@Test
	public void testretrieveShareablePricePlanSubscriberCount() throws ApplicationException, RemoteException{
		Collection<PricePlanSubscriberCountInfo> ppsci=impl.retrieveShareablePricePlanSubscriberCount(816166);
		assertEquals(1,ppsci.size());
		assertEquals("4035543200", ((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}

	@Test
	public void testvalidatePayAndTalkSubscriberActivation() throws ApplicationException, RemoteException{
		String applicationId = "notImportantAppId";
		AuditHeader auditHeader = new AuditHeaderInfo();
		String userId = "integTest";
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo=new PrepaidConsumerAccountInfo();

		try{
			impl.validatePayAndTalkSubscriberActivation(applicationId, userId, prepaidConsumerAccountInfo, auditHeader);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testRetretrieveTalkGroupsByBan()throws ApplicationException, RemoteException{
		Collection<TalkGroupInfo> talkGroup= new ArrayList<TalkGroupInfo>();
		talkGroup = impl.retrieveTalkGroupsByBan(6001954);
		assertEquals(11,talkGroup.size());
		for(TalkGroupInfo talkInfo :talkGroup){
			assertEquals(905,talkInfo.getFleetIdentity().getUrbanId());
			assertEquals(205,talkInfo.getFleetIdentity().getFleetId());
			assertEquals("Dep 3",talkInfo.getName());
			break;
		}
	}
	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveAccountsBySerialNumber() throws ApplicationException, RemoteException {		
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySerialNumber("24205462132");

		assertEquals(1, accounts.size());
		for(AccountInfo account : accounts) {
			assertEquals(816166, account.getBanId());
		}

		accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySerialNumber("0816166");
		assertEquals(0, accounts.size());

		assertEquals(0, impl.retrieveAccountsBySerialNumber("").size());
	}

	@Test
	public void testRetrieveAccountBySerialNumber() throws ApplicationException , RemoteException{		
		AccountInfo account = impl.retrieveAccountBySerialNumber("24205462132");

		assertEquals(816166, account.getBanId());	

		assertNull(impl.retrieveAccountBySerialNumber("900033300000000"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits1() throws ApplicationException, RemoteException{
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1997);
		SearchResultsInfo sri1=impl.retrieveCredits(12474, fromCal.getTime(), new Date(), "B", '1', "", 500);
		assertEquals(42, sri1.getItems().length);
		SearchResultsInfo sri2=impl.retrieveCredits(6033629, fromCal.getTime(), new Date(), "U", '1', "", 500);
		assertEquals(0, sri2.getItems().length);
		SearchResultsInfo sri3=impl.retrieveCredits(1911779, fromCal.getTime(), new Date(), "A", '1', "", 100);
		assertEquals(172, sri3.getItems().length);

	}

	@Test
	public void testRetrieveLastCreditCheckResultByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException{

		int ban = 1911779;
		String productType="C";
		CreditCheckResultInfo ccrinfo = impl.retrieveLastCreditCheckResultByBan(ban, productType);

		assertEquals("D", ccrinfo.getCreditCheckResultStatus().trim());
		System.out.println(ccrinfo.getCreditClass());
		System.out.println(ccrinfo.getLimit());
		System.out.println(ccrinfo.getMessage());
	}

	@Test
	public void testRetrieveBillingInformation() throws ApplicationException, RemoteException {		

		int billingAccountNumber=1911779;
		BillingPropertyInfo billingPropertyInfo=impl.retrieveBillingInformation(billingAccountNumber);
		assertEquals("null CHANNEL CARE",billingPropertyInfo.getFullName());
	}

	@Test
	public void testRetrieveContactInformation() throws ApplicationException, RemoteException {		

		int billingAccountNumber=1911779;
		ContactPropertyInfo contactPropertyInfo=impl.retrieveContactInformation(billingAccountNumber);
		assertEquals("4162792740",contactPropertyInfo.getBusinessPhoneNumber());
	}

	@Test
	public void testRetrievePersonalCreditInformation() throws ApplicationException, RemoteException{
		int ban=1910726;
		PersonalCreditInfo pci = impl.retrievePersonalCreditInformation(ban);
		assertEquals("100000000000000006551",pci.getCreditCard().getToken());
		assertEquals("6309607",pci.getDriversLicense());
	}

	@Test
	public void testRetrieveBusinessCreditInformation() throws ApplicationException, RemoteException{
		int ban=313935;
		BusinessCreditInfo bci = impl.retrieveBusinessCreditInformation(ban);
		assertEquals("20656338",bci.getIncorporationNumber());
	}

	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	@Test
	public void testGetSubscriberEligiblitySupportingInfo() throws ApplicationException, RemoteException{
		int ban=8;
		String[] memoTypes = new String[]{"0001","0002"};
		Date fromDate = new Date(2002-1900,01,20);
		Date toDate = new Date(2007-1900,01,20);
		SubscriberEligibilitySupportingInfo sesi = impl.getSubscriberEligiblitySupportingInfo(ban,memoTypes,fromDate,toDate);
		assertEquals(0.0,sesi.getTotalDepositHeld(),0);
	}
	@Test
	public void testRetrieveSubscriberIdsByServiceFamilybyBAn() throws ApplicationException, RemoteException{

		String []  Subscriberes = impl.retrieveSubscriberIdsByServiceFamily(70607502, "Y",getDateInput(2000,1,1));
		for(int i= 0;i<Subscriberes.length;i++)
			System.out.println("Subscriberes"+Subscriberes[i])	;
	}

	@Test
	public void testRetrieveSubscriberIdsByServiceFamilybyAccount() throws ApplicationException, RemoteException{
		AccountInfo account = impl.retrieveAccountByBan(70607502);
		String[] Subscriberes = impl.retrieveSubscriberIdsByServiceFamily(account, "Y", getDateInput(2000,1,1));
		for (int i = 0; i < Subscriberes.length; i++)
			System.out.println("Subscriberes" + Subscriberes[i]);

	}

	@Test
	public void testRetrieveSubscribersByDataSharingGroupCodes() throws ApplicationException, RemoteException{
		AccountInfo account = impl.retrieveAccountByBan(70607502);
		String [] dataSharingGroupCodes = {"US_DATA","CAD_TXT","CAD_DATA","N_America_Data"};
		SubscribersByDataSharingGroupResult [] subscribersByDataSharingGroupResult = impl.retrieveSubscribersByDataSharingGroupCodes(account, dataSharingGroupCodes, getDateInput(2000,1,1));
		for (int i = 0; i < subscribersByDataSharingGroupResult.length; i++)
			System.out.println("subscribersByDataSharingGroupResult" + subscribersByDataSharingGroupResult[i]);

	}
	@Test
	public void testRetrieveSubscribersByDataSharingGroupCodesbyAccount() throws ApplicationException, RemoteException{

		String [] dataSharingGroupCodes = {"US_DATA","CAD_TXT","CAD_DATA","N_America_Data"};
		SubscribersByDataSharingGroupResult [] subscribersByDataSharingGroupResult = impl.retrieveSubscribersByDataSharingGroupCodes(70078120, dataSharingGroupCodes, getDateInput(2000,1,1));
		for (int i = 0; i < subscribersByDataSharingGroupResult.length; i++)
			System.out.println("subscribersByDataSharingGroupResult" + subscribersByDataSharingGroupResult[i]);

	}

	@Test
	public void testretrieveRelatedCreditsForChargeList() throws ApplicationException, RemoteException{
		ChargeIdentifierInfo chargeIdentifierInfo= new ChargeIdentifierInfo();
		chargeIdentifierInfo.setAccountNumber(70663894);
		chargeIdentifierInfo.setChargeSequenceNumber(new Double(2095774855));
		List<ChargeIdentifierInfo> chargeIdentifierInfoList = new ArrayList<ChargeIdentifierInfo>();
		chargeIdentifierInfoList.add(chargeIdentifierInfo);
		List <List<CreditInfo>> listinfo= impl.retrieveRelatedCreditsForChargeList(chargeIdentifierInfoList);
		for (List<CreditInfo> list : listinfo) {
			for (CreditInfo creditInfo : list) {
				System.out.println("creditInfo"+creditInfo.toString());
			}
		}

	}

	@Test
	public void testcheckInternationalServiceEligibility() throws ApplicationException,RemoteException {
		System.out.println("start testcheckInternationalServiceEligibility");
		InternationalServiceEligibilityCheckResultInfo eligibilityInfo= impl.checkInternationalServiceEligibility(70526466);
		System.out.println("eligibilityInfo"+eligibilityInfo.toString());
		System.out.println("end testcheckInternationalServiceEligibility");
	 }

	@Test
	public void testretrieveAccountsByPhoneNumberWithPhoneNumberSearchOption() throws ApplicationException, RemoteException {
		String phoneNumber = "5144182222";
		boolean includePastAccounts = true;
		boolean onlyLastAccount = false;
		PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo =  new PhoneNumberSearchOptionInfo(); 
		phoneNumberSearchOptionInfo.setSearchWirelessNumber(true);
		phoneNumberSearchOptionInfo.setSearchVOIP(true);
		//Collection<AccountInfo> ais = impl.retrieveAccountsByPhoneNumber(phoneNumber, includePastAccounts, onlyLastAccount, phoneNumberSearchOptionInfo);
//
//		for (AccountInfo ai : ais) {
//			System.out.println("ban"+ai.getBanId());
//		}
	}
}
