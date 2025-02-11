package com.telus.cmb.account.informationhelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.Charge;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ChargeType;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
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
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class AccountInformationHelperImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	AccountInformationHelper impl;
	@Autowired
	CreditInfo creditInfo;	
	@Autowired
	ChargeInfo chargeInfo;
	@Autowired
	Integer prepaidBAN;
	@Autowired
	String prepaidSub;
	@Autowired
	Integer sampleBAN;
	@Autowired
	PrepaidConsumerAccountInfo prepaidConsumerAccountInfo;


	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}	

	@Test
	public void testRetrieveAccountsByBan()  {
		Collection<AccountInfo> ais = impl.retrieveAccountsByBan(new int[]{70106788});

		assertEquals(1, ais.size());
		for (AccountInfo ai : ais) {
			assertEquals(70106788, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveAccountsByBusinessName() throws ApplicationException {		
		SearchResultsInfo result = impl.retrieveAccountsByBusinessName("*",
				"a", 
				false, 
				'*', 
				'*', 
				"*", 
				Brand.BRAND_ID_TELUS, 
				100);
		AccountSummary[] as = (AccountSummary[])result.getItems();

		assertEquals(70, as.length);
	}

	@Test
	public void testRetrieveAccountByBan() throws ApplicationException  {
		AccountInfo ai = impl.retrieveAccountByBan(70106788);

		assertEquals(70106788, ai.getBanId());
	}

	@Test
	public void testRetrievePhoneNumbersForBAN() throws ApplicationException  {
		@SuppressWarnings("unchecked")
		Map<String, String> phoneNumbers = (Map<String, String>)impl.retrievePhoneNumbersForBAN(20007869);
		assertEquals(2, phoneNumbers.size());
		System.out.println(phoneNumbers);
	}

	@Test
	public void testRetrieveAccountStatusByBan() throws ApplicationException  {
		assertEquals("O", impl.retrieveAccountStatusByBan(20007879));
	}

	@Test
	public void testRetrieveVoiceUsageSummary() throws ApplicationException  {
		assertEquals(0, impl.retrieveVoiceUsageSummary(20007879, null).length);
	}

	@Test
	public void testRetrieveAccountsByPostalCode() {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPostalCode("TEST", "M5C1G6", 10);

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(20007358, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveAccountByPhoneNumber() throws ApplicationException  {
		AccountInfo ai = impl.retrieveAccountByPhoneNumber("4033048642");

		assertEquals(1600, ai.getBanId());
	}

	@Test
	public void testRetrieveAccountByPhoneNumberStringBooleaBoolean() throws ApplicationException  {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPhoneNumber("4033048642", true, false);

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(1600, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveAccountByPhoneNumberString() throws ApplicationException  {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts =  (Collection<AccountInfo>)impl.retrieveAccountsByPhoneNumber("4033048642");

		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(1600, ai.getBanId());
		}
	}

	@Test
	public void testRetrieveMemos() throws ApplicationException  {
		@SuppressWarnings("unchecked")
		Collection<MemoInfo> memo = (Collection<MemoInfo>)impl.retrieveMemos(20007004, 500);
		assertEquals(306, memo.size());
		for (MemoInfo mi : memo) {
			assertEquals("0001", mi.getMemoType());
			break;
		}		
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveMemosByCriteria() throws ApplicationException  {

		MemoCriteriaInfo mcinfo=new MemoCriteriaInfo();
		mcinfo.setBanId(81);
		mcinfo.setSubscriberId("9057160015");
		mcinfo.setType("0002");
		mcinfo.setManualText(null);
		mcinfo.setSystemText(null);
		//Year and Month adjustments as per Java util Date
		mcinfo.setDateFrom(new Date((2002-1900),(2-1),13));
		mcinfo.setDateTo(new Date((2005-1900),(3-1),31));
		mcinfo.setSearchLimit(10);
		@SuppressWarnings("unchecked")
		Collection<MemoInfo> memoCriteria = (Collection<MemoInfo>)impl.retrieveMemos(mcinfo);
		assertEquals(1, memoCriteria.size());
		for (MemoInfo mi : memoCriteria) {
			assertEquals("0002", mi.getMemoType());
			break;
		}
	}

	@Test
	public void testRetrieveLastMemo() throws ApplicationException  {
		MemoInfo memoinfo=impl.retrieveLastMemo(20007181, "1040");
		assertEquals("9057160067", memoinfo.getSubscriberId());
	}

	@Test
	public void testRetrieveAccountsByName() throws ApplicationException {
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

		assertEquals(70, as.length);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveAccountsByDealership() throws ApplicationException {
		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsByDealership('O', "A0010000010000"
				, new Date("01/01/1999"), new Date("01/01/2999"), 10);

		assertEquals(10, accounts.size());
	}

	@Test
	public void testRetrieveAccountsBySalesRep() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);

		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySalesRep(
				'O', "A0010000010000", "", cal.getTime(), new Date(), 10);

		assertEquals(10, accounts.size());
	}

	@Test
	public void testRetrieveAccountInfoForPayAndTalkCustomer() throws ApplicationException {
		if (System.getProperty("env") != null && System.getProperty("env").equals("PT168")) {
			impl.retrieveAccountInfoForPayAndTalkSubscriber(70602349, "7781690712");
		}
	}
	@Test
	public void testRetrieveBanIds() {
		int[] bans = impl.retrieveBanIds('I', 'R', 'O', 10);

		assertEquals(10, bans.length);
	}

	@Test
	public void testRetrieveBanIdsByAddressType() {
		int[] bans = impl.retrieveBanIdByAddressType('I', 'R', 'O', 'C', 10);

		assertEquals(10, bans.length);
	}

	@Test
	public void testRetrieveAttachedSubscribersCount() throws ApplicationException {
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo();
		fleetIdentityInfo.setFleetId(131072);
		fleetIdentityInfo.setUrbanId(905);
		assertEquals(1, impl.retrieveAttachedSubscribersCount(25, fleetIdentityInfo));
	}

	@Test
	public void testretrieveFollowUpAdditionalText() throws ApplicationException{
		Collection<FollowUpTextInfo> followUpTextInfo=impl.retrieveFollowUpAdditionalText(20007401, 11545271);
		assertEquals(5, followUpTextInfo.size());
		for (FollowUpTextInfo fti : followUpTextInfo) {
			assertEquals("12114",fti.getOperatorId());
			break;
		}	
	}

	@Test
	public void testretrieveFollowUpHistory(){
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUpHistory(22405200);
		assertEquals(5, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("901781  ",fi.getAssignedToWorkPositionId());
			assertEquals(10000283, fi.getBanId());
			assertEquals("ADJT", fi.getFollowUpType());
			break;
		}	
	}

	@Test
	public void testretrieveFollowUpInfoByBanFollowUpID() throws ApplicationException{
		FollowUpInfo followInfo=impl.retrieveFollowUpInfoByBanFollowUpID(20007401, 11545271);
		assertEquals("7807183956", followInfo.getSubscriberId());

		FollowUpInfo followInfo1=impl.retrieveFollowUpInfoByBanFollowUpID(20006998, 12740559);
		assertEquals("M001005834", followInfo1.getSubscriberId());
		
	}

	@Test
	public void testretrieveFollowUpsByCriteria(){

		FollowUpCriteriaInfo fci=new FollowUpCriteriaInfo();
		fci.setWorkPositionId("RS_C0020");
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUps(fci);
		assertEquals(31, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("12857",fi.getOpenedBy());
			break;
		}	
		
		FollowUpCriteriaInfo fci1=new FollowUpCriteriaInfo();
		fci1.setWorkPositionId("18654");
		Collection<FollowUpInfo> followUpInfo1=impl.retrieveFollowUps(fci1);
		assertEquals(294, followUpInfo1.size());
		for (FollowUpInfo fi : followUpInfo1) {
			assertEquals(178,fi.getBan());
			assertEquals("9057160070",fi.getPhoneNumber());
			break;
		}

	}

	@Test
	public void testretrieveFollowUps() throws ApplicationException{
		Collection<FollowUpInfo> followUpInfo=impl.retrieveFollowUps(70103567, 75);
		assertEquals(75, followUpInfo.size());
		for (FollowUpInfo fi : followUpInfo) {
			assertEquals("RS_C0020",fi.getAssignedToWorkPositionId());
			assertEquals(22405605,fi.getFollowUpId());
			//System.out.println(mi.getMemoType()); 
			break;
		}	
		Collection<FollowUpInfo> followUpInfo1=impl.retrieveFollowUps(20006998, 1);
		assertEquals(1, followUpInfo1.size());
		for (FollowUpInfo fi : followUpInfo1) {
			assertEquals("18654   ",fi.getAssignedToWorkPositionId());
			assertEquals(12740559,fi.getFollowUpId());
			assertEquals("I",fi.getProductType());
			assertEquals("5148200002",fi.getPhoneNumber());
			//System.out.println(mi.getMemoType()); 
			break;
		}	
		
	}

	@Test
	public void testretrieveFollowUpStatistics() throws ApplicationException{
		FollowUpStatisticsInfo followUpStats=impl.retrieveFollowUpStatistics(20579111);
		assertTrue(followUpStats.hasOpenFollowUps());
	}

	@Test
	public void testretrieveLastFollowUpIDByBanFollowUpType() throws ApplicationException{
		int followUpId=impl.retrieveLastFollowUpIDByBanFollowUpType(10000283, "ADJT");
		assertEquals(22405202,followUpId);
	}

	@Test
	public void testRetrieveProductSubscribersList() throws ApplicationException {
		ProductSubscriberListInfo[] subscribers = impl.retrieveProductSubscriberLists(20007869);
		assertEquals(1, subscribers.length);
		for (ProductSubscriberListInfo sub : subscribers) {
			assertEquals(2, sub.getReservedSubscribersCount());
			assertEquals(0, sub.getActiveSubscribersCount());
			assertEquals(0, sub.getCancelledSubscribersCount());
			assertEquals(0, sub.getSuspendedSubscribersCount());
		}
	}
	@Test
	public void testRetrieveUnpaidDataUsageTotal() throws ApplicationException {
		double result = impl.retrieveUnpaidDataUsageTotal(20007181, new Date());
		assertEquals(0,result,0);
	}
	@Test
	public void testRetrieveUnpaidAirTimeTotal() throws ApplicationException {
		double result = impl.retrieveUnpaidAirTimeTotal(20007181);
		assertEquals(0,result,0);
	}

	@Test
	public void testGetLastEBillNotificationSent() throws ApplicationException {
		BillNotificationContactInfo billNot = impl.getLastEBillNotificationSent(15324300);
		assertEquals("SMS",billNot.getContactType());
		assertEquals(new java.sql.Date(2006-1900,0,01) ,billNot.getLastNotificationDate());

	}
	@Test
	public void testGetLastEBillRegistrationReminderSent() throws ApplicationException {

		EBillRegistrationReminderInfo billReg = impl.getLastEBillRegistrationReminderSent(15324300);
		try{
			assertEquals(null,billReg.getActivatedPhoneNumber());
			fail("Exception Expected");
		}catch(Exception e)
		{}

	}
	@Test
	public void testGetBillNotificationHistory() throws ApplicationException {
		Collection<BillNotificationHistoryRecord> billNot = impl.getBillNotificationHistory(15324300,"EPOST");
		assertEquals(48,billNot.size());
		for(BillNotificationHistoryRecord hisInfo : billNot){
			assertEquals("1234",hisInfo.getSrcReferenceId());
			assertEquals(true,hisInfo.getMostRecentInd());
			break;
		}	
	}

	@Test
	public void testExpireBillNotificationDetails() throws ApplicationException {
		impl.expireBillNotificationDetails(15324300);		
	}

	@Test
	public void testRetrieveFleetsByBan() throws ApplicationException {

		FleetInfo[] fleets = impl.retrieveFleetsByBan(134);

		assertEquals(1, fleets.length);

		for (FleetInfo fleet : fleets) {
			assertEquals(1, fleet.getAssociatedAccountsCount());
		}

		assertEquals(0, impl.retrieveFleetsByBan(29888837).length);
	}

	@Test
	public void testRetrieveAccountsByImsi() throws ApplicationException {
		AccountInfo ai = impl.retrieveAccountByImsi("214030000050598");

		assertEquals(70105381, ai.getBanId());

		assertNull(impl.retrieveAccountByImsi(""));
	}

	@Test
	public void testRetrieveSubscriberIdsByStatus() throws ApplicationException{
		assertEquals(50, impl.retrieveSubscriberIdsByStatus(20001552, 'C', 50).size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testRetrieveAccountsBySerialNumber() throws ApplicationException {		
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySerialNumber("000005357");

		assertEquals(1, accounts.size());
		for(AccountInfo account : accounts) {
			assertEquals(55438827, account.getBanId());
		}

		accounts = (Collection<AccountInfo>)impl.retrieveAccountsBySerialNumber("900033300000000");
		assertEquals(0, accounts.size());

		assertEquals(0, impl.retrieveAccountsBySerialNumber("").size());
	}

	@Test
	public void testRetrieveAccountBySerialNumber() throws ApplicationException {		
		AccountInfo account = impl.retrieveAccountBySerialNumber("000005357");

		assertEquals(55438827, account.getBanId());	

		assertNull(impl.retrieveAccountBySerialNumber("900033300000000"));
	}

	@Test
	public void testRetrieveLastCreditCheckMemoByBan() throws ApplicationException { 
		MemoInfo mi=impl.retrieveLastCreditCheckMemoByBan(20007181);
		assertEquals(0, mi.getBanId());
	}

	@Test
	public void testRetrieveSubscriberPhoneNumbersByStatus() throws ApplicationException{
		assertEquals(12, impl.retrieveSubscriberPhoneNumbersByStatus(20001552, 'A', 50).size());
	}
	@Test
	public void testGetClientAccount()throws ApplicationException	{
		assertEquals(7547708,impl.getClientAccountId(70063821));
		assertEquals(7547710,impl.getClientAccountId(4464648));
	}
	@Test
	public void testretrieveCorporateName()throws ApplicationException	{
		assertEquals("SUPER CORPORATION",impl.retrieveCorporateName(10000));
	}

	@Test
	public void testRetrieveLastPaymentActivity() throws ApplicationException {
		PaymentHistoryInfo info = impl.retrieveLastPaymentActivity(134);		
		assertNull(info); 	

		info = impl.retrieveLastPaymentActivity(8);		
		assertNotNull(info); 
	}

	@Test
	public void testRetrieveSubscriberInvoiceDetails()throws ApplicationException	{
		assertEquals(2,impl.retrieveSubscriberInvoiceDetails(51836, 8).size());
		assertEquals(0,impl.retrieveSubscriberInvoiceDetails(51836, 1).size());
	}
	@Test
	public void testRetrieveAssociatedTalkGroupsCount() throws ApplicationException	{
		FleetIdentityInfo flInf= new FleetIdentityInfo();
		flInf.setUrbanId(905);
		flInf.setFleetId(16389);
		assertEquals(3, impl.retrieveAssociatedTalkGroupsCount(flInf,20004220));
		flInf.setUrbanId(0);
		flInf.setFleetId(0);
		assertEquals(0, impl.retrieveAssociatedTalkGroupsCount(flInf, 20004220));
	}
	@Test
	public void testRetrieveBilledCharges()throws ApplicationException{
		Collection<ChargeInfo> chInf = impl.retrieveBilledCharges(8,54,"4033404108",new java.sql.Date(2002-1900,8,23),new java.sql.Date(2002-1900,8,23));
		assertEquals(35,chInf.size());
		for(ChargeInfo conInfo : chInf){
			assertEquals(0,conInfo.getAmount(),0);
			assertEquals(54 ,conInfo.getBillSequenceNo());
			assertEquals("CHG " ,conInfo.getChargeCode());
			break;
		}
	}

	@Test
	public void testRetrieveBillNotificationContacts()throws ApplicationException{
		Collection<BillNotificationContactInfo> chargeInf = impl.retrieveBillNotificationContacts(15324277);
		assertEquals(0,chargeInf.size());
	}
	@Test
	public void testisFeatureCategoryExistOnSubscribers()throws ApplicationException{

		assertEquals(true,impl.isFeatureCategoryExistOnSubscribers(8,"VM"));
		assertEquals(false,impl.isFeatureCategoryExistOnSubscribers(24,"VM"));
	}
	@Test
	public void testisEBillRegistrationReminderExist()throws ApplicationException{
		assertEquals(false,impl.isEBillRegistrationReminderExist(15324300));
	}

	@Test
	public void testhasEPostSubscription()throws ApplicationException{

		assertEquals(false,impl.hasEPostSubscription(8));
		assertEquals(false,impl.hasEPostSubscription(24));
	}

	@Test
	public void testRetrieveAlternativeAddressByBan() throws ApplicationException {
		AddressInfo addressInfo = impl.retrieveAlternateAddressByBan(sampleBAN);

		assertEquals("M5J1R7", addressInfo.getPostalCode());

		addressInfo = null;	
		addressInfo = impl.retrieveAlternateAddressByBan(9999);
		assertNotNull(addressInfo);
	}

	@Test
	public void testRetrieveBusinessList() throws ApplicationException {
		BusinessCreditIdentityInfo[] retrieveBusinessList = impl.retrieveBusinessList(sampleBAN);
		assertNotNull(retrieveBusinessList);
		assertEquals(0, retrieveBusinessList.length);

		retrieveBusinessList = impl.retrieveBusinessList(195);
		assertEquals(3, retrieveBusinessList.length);

		assertEquals("01,TEST,1 TEST ST,TORONTO,ON", retrieveBusinessList[0].getCompanyName());
		assertEquals(25910885, retrieveBusinessList[0].getMarketAccount(), 0);		
		assertEquals("02,S M S LTD,2235 BLOOR ST W,TORONTO,ON", retrieveBusinessList[1].getCompanyName());
		assertEquals(17580620, retrieveBusinessList[1].getMarketAccount(), 0);
		assertEquals("03,SNT LTD.,1 FIRST CANADIA,TORONTO,ON", retrieveBusinessList[2].getCompanyName());
		assertEquals(15494468, retrieveBusinessList[2].getMarketAccount(), 0);
	}

	@Test
	public void testRetrieveAuthorizedNames() throws ApplicationException {
		ConsumerNameInfo[] results = impl.retrieveAuthorizedNames(sampleBAN);
		assertNotNull(results);
		assertEquals(0, results.length);

		results = null;
		results = impl.retrieveAuthorizedNames(70104429);
		assertEquals(2, results.length);

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
	public void testRetrieveDepositHistory() throws ApplicationException {
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		Calendar toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2006);

		DepositHistoryInfo[] list = impl.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());

		assertEquals(13, list.length);

		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2000);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2003);

		list = impl.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());

		assertEquals(4, list.length);

		fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 2010);
		toCal = Calendar.getInstance();
		toCal.set(Calendar.YEAR, 2011);

		list = impl.retrieveDepositHistory(81, fromCal.getTime(), toCal.getTime());

		assertEquals(0, list.length);	
	}

	@Test
	public void testretrieveCreditByFollowUpId() throws ApplicationException{
		Collection<CreditInfo> creditInfo=impl.retrieveCreditByFollowUpId(18150420);
		assertEquals(1, creditInfo.size());
		for (CreditInfo ci : creditInfo) {
			assertEquals("9057160719",ci.getSubscriberId());
			assertEquals("ADJ ", ci.getActivityCode());
			assertEquals(400,ci.getAmount(),0);
			break;
		}	
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits1() throws ApplicationException{
		SearchResultsInfo sri1=impl.retrieveCredits(832198, new Date(100,03,22), new Date(100,04,12), "B", '1', "", 500);
		assertEquals(243, sri1.getItems().length);
		SearchResultsInfo sri2=impl.retrieveCredits(20070738, new Date(104,0,01), new Date(107,12,01), "U", '1', "", 500);
		assertEquals(357, sri2.getItems().length);
		SearchResultsInfo sri3=impl.retrieveCredits(7432741, new Date(90,0,01), new Date(109,12,01), "A", '1', "", 100);
		assertEquals(82, sri3.getItems().length);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits2() throws ApplicationException{
		SearchResultsInfo sri1=impl.retrieveCredits(832198, new Date(100,03,22), new Date(100,04,12), "B",  '1', "", "", 500);
		assertEquals(243, sri1.getItems().length);
		SearchResultsInfo sri2=impl.retrieveCredits(20070738, new Date(104,0,01), new Date(107,12,01), "U",  '1', "", "", 500);
		assertEquals(357, sri2.getItems().length);
		SearchResultsInfo sri3=impl.retrieveCredits(7432741, new Date(90,0,01), new Date(109,12,01), "A",  '1', "", "", 100);
		assertEquals(82, sri3.getItems().length);
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits3() throws ApplicationException{
		SearchResultsInfo sri1=impl.retrieveCredits(832198, new Date(100,03,22), new Date(100,04,12), "B", "", "", '1', "", 500);
		assertEquals(243, sri1.getItems().length);
		SearchResultsInfo sri2=impl.retrieveCredits(20070738, new Date(104,0,01), new Date(107,12,01), "U", "", "", '1', "", 500);
		assertEquals(357, sri2.getItems().length);
		SearchResultsInfo sri3=impl.retrieveCredits(7432741, new Date(90,0,01), new Date(109,12,01), "A", "", "", '1', "", 100);
		assertEquals(82, sri3.getItems().length);
	}

	@Test
	public void testRetrieveDepositAssessdHistoryList() throws ApplicationException {
		DepositAssessedHistoryInfo[] list = impl.retrieveDepositAssessedHistoryList(81);
		assertEquals(6, list.length);

		list = null;
		list = impl.retrieveDepositAssessedHistoryList(1);
		assertNotNull(list);
		assertEquals(0, list.length);
	}
	@Test
	public void testGetInvoiceProperties() throws ApplicationException {
		InvoicePropertiesInfo invoicePropertiesInfo = impl.getInvoiceProperties(24);
		assertEquals("2",invoicePropertiesInfo.getInvoiceSuppressionLevel());
		assertEquals("1",invoicePropertiesInfo.getHoldRedirectDestinationCode());

	}


	@Test
	public void testRetrieveCharges() throws ApplicationException {

		Charge[] charges = impl.retrieveCharges(sampleBAN, new String[]{"CNRD"}, Account.BILL_STATE_ALL, 'A', "4033404108", new Date(), new Date(), 10);

		assertEquals(0, charges.length);
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1999);
		charges = impl.retrieveCharges(8, new String[]{"CNRD"}, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, "4033404108", fromCal.getTime(), new Date(), 10);

		assertEquals(5, charges.length);
	}

	@Test
	public void testRetrievePaymentHistory()throws ApplicationException{
		Collection<PaymentHistoryInfo> paymentHistoryList = new ArrayList<PaymentHistoryInfo>();
		paymentHistoryList =impl.retrievePaymentHistory(8,new java.sql.Date(2003-1900,05,27),new java.sql.Date(2003-1900,05,27));
		assertEquals(2,paymentHistoryList.size());
		for(PaymentHistoryInfo paymentHistoryInfo:paymentHistoryList){
			assertEquals("CC",paymentHistoryInfo.getPaymentMethodCode());
			assertEquals(225,paymentHistoryInfo.getOriginalAmount(),0);
			break;
		}
	}

	@Test
	public void testRetrieveOriginalDepositAssessedHistoryList() throws ApplicationException {
		assertEquals(6, impl.retrieveOriginalDepositAssessedHistoryList(20007217).length);
		assertEquals(0, impl.retrieveOriginalDepositAssessedHistoryList(1).length);
	}
	@Test
	public void testRetrieveHotlinedSubscriberPhoneNumber()throws ApplicationException {
		assertEquals("6472065737",impl.retrieveHotlinedSubscriberPhoneNumber(10000047));
		assertEquals("",impl.retrieveHotlinedSubscriberPhoneNumber(2));
	}
	@Test
	public void testRetrieveInvoiceHistory()throws ApplicationException {
		Collection<InvoiceHistoryInfo> invoiceHistoryInfo = new ArrayList<InvoiceHistoryInfo>();
		invoiceHistoryInfo = impl.retrieveInvoiceHistory(210879, new Date(2006-1900,0,01), new Date(2006-1900,0,31));
		assertEquals(1,invoiceHistoryInfo.size());
		for(InvoiceHistoryInfo invObj:invoiceHistoryInfo){
			assertEquals(50.55,invObj.getAmountDue(),0);
			assertEquals(84.07,invObj.getPreviousBalance(),0);
			break;
		}
	}
	@Test
	public void tesRetrieveAttachedSubscribersCountForTalkGroup()throws ApplicationException {
		assertEquals(2,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 2, 20004220));
		assertEquals(1,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 1, 20004220));
		assertEquals(0,impl.retrieveAttachedSubscribersCountForTalkGroup(905, 16389, 3, 20004220));
	}
	@Test
	public void testRetrievePaymentActivities()throws ApplicationException {
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
	public void testRetrievePaymentMethodChangeHistory()throws ApplicationException {
		Collection<PaymentMethodChangeHistoryInfo> paymentActivity = new ArrayList<PaymentMethodChangeHistoryInfo>();
		paymentActivity = impl.retrievePaymentMethodChangeHistory(20007060, new Date(103,0,01),new Date(110,0,01));
		assertEquals(1,paymentActivity.size());
		for(PaymentMethodChangeHistoryInfo payActInf:paymentActivity){
			assertEquals("100000000000000006551",payActInf.getCreditCardToken());
			assertEquals("1203",payActInf.getCreditCardExpiry());
			assertEquals("@@",payActInf.getBankCode().trim());
			assertEquals("@@",payActInf.getBankAccountNumber().trim());
		}
	}

	@Test
	public void testRetrieveRefundHistory()throws ApplicationException{
		List<RefundHistoryInfo> refundHistoryList = new ArrayList<RefundHistoryInfo>();
		refundHistoryList =impl.retrieveRefundHistory(99999924,new Date(2002-1900,01,20),new Date(2006-1900,01,26));
		assertEquals(37,refundHistoryList.size());
		for(RefundHistoryInfo refundHistoryInfo:refundHistoryList){
			assertEquals("RFERPY",refundHistoryInfo.getReasonCode());
			assertEquals("RFN ",refundHistoryInfo.getCode());
			assertEquals(292.12,refundHistoryInfo.getAmount(),0);
			break;
		}
	}

	@Test
	public void testRetrievePCSNetworkCountByBan()throws ApplicationException{
		assertEquals(new Integer(21),impl.retrievePCSNetworkCountByBan(8).get("C"));
		assertEquals(new Integer(0),impl.retrievePCSNetworkCountByBan(8).get("H"));

	}

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts1() throws ApplicationException{
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(570899,PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL);
		assertEquals(2,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());

		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(570899,4);
		assertEquals(1,poolPP1.size());
		assertEquals(4,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts2() throws ApplicationException{
		Collection<PoolingPricePlanSubscriberCountInfo> poolPP= impl.retrievePoolingPricePlanSubscriberCounts(70105350, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL);
		assertEquals(4,poolPP.size());
		assertEquals(1,((PoolingPricePlanSubscriberCountInfo[])poolPP.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP.size()]))[0].getPoolingGroupId());

		Collection<PoolingPricePlanSubscriberCountInfo> poolPP1= impl.retrievePoolingPricePlanSubscriberCounts(70105350, 7);
		assertEquals(1,poolPP1.size());
		assertEquals(7,((PoolingPricePlanSubscriberCountInfo[])poolPP1.toArray(new PoolingPricePlanSubscriberCountInfo[poolPP1.size()]))[0].getPoolingGroupId());
	}

	@Test
	public void testretrieveServiceSubscriberCounts() throws ApplicationException{
		Collection<ServiceSubscriberCount> scc=impl.retrieveServiceSubscriberCounts(70105350, 
				new String[]{"USTL50F9 ","PRIM100N3","RIMSB    ","RIMTM    ","SDOC1    ","SE911    ","SFBC     "}, true);
		assertEquals(7,scc.size());
		assertEquals("PRIM100N3", ((ServiceSubscriberCount[])scc.toArray(new ServiceSubscriberCount[scc.size()]))[0].getServiceCode());
	}

	@Test
	public void testretrieveMinutePoolingEnabledPricePlanSubscriberCounts() throws ApplicationException{
		Collection<PricePlanSubscriberCount> ppsc=impl.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(570899, new String[]{"H","M","O","R"});
		assertEquals(3,ppsc.size());
		assertEquals("PSHAR10MP", ((PricePlanSubscriberCount[])ppsc.toArray(new PricePlanSubscriberCount[ppsc.size()]))[0].getPricePlanCode());
	}

	@Test
	public void testretrieveDollarPoolingPricePlanSubscriberCounts() throws ApplicationException{
		Collection<PricePlanSubscriberCountInfo> ppsci=impl.retrieveDollarPoolingPricePlanSubscriberCounts(9980102,"I");
		assertEquals(1,ppsci.size());
		assertEquals("M001956744",((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}

	@Test
	public void testretrieveShareablePricePlanSubscriberCount() throws ApplicationException{
		Collection<PricePlanSubscriberCountInfo> ppsci=impl.retrieveShareablePricePlanSubscriberCount(9346510);
		assertEquals(1,ppsci.size());
		assertEquals("4035543200", ((PricePlanSubscriberCountInfo[])ppsci.toArray(new PricePlanSubscriberCountInfo[ppsci.size()]))[0].getActiveSubscribers()[0]);
	}

	@Test
	public void testvalidatePayAndTalkSubscriberActivation() throws ApplicationException{
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
	public void testRetretrieveTalkGroupsByBan()throws ApplicationException{
		Collection<TalkGroupInfo> talkGroup= new ArrayList<TalkGroupInfo>();
		talkGroup = impl.retrieveTalkGroupsByBan(84);
		assertEquals(3,talkGroup.size());
		for(TalkGroupInfo talkInfo :talkGroup){
			assertEquals("ru",talkInfo.getName());
			assertEquals(905,talkInfo.getFleetIdentity().getUrbanId());
			assertEquals(131075,talkInfo.getFleetIdentity().getFleetId());
			break;
		}
	}

	@Test
	public void testRetrieveStatusChangeHistory() throws ApplicationException {
		int ban = 25;
		Calendar cal = Calendar.getInstance();
		cal.set(1999, 1, 1);
		Date fromDate = cal.getTime();
		cal.set(2010, 12, 7);
		Date toDate = cal.getTime();
		@SuppressWarnings("unchecked")
		List<StatusChangeHistoryInfo> results = impl.retrieveStatusChangeHistory(ban, fromDate, toDate);

		assertEquals(7, results.size());

		for (StatusChangeHistoryInfo change : results) {
			assertNotNull(change);			
		}
	}
	
	@Test
	public void testRetrieveLastCreditCheckResultByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		int ban = 8;
		String productType="I";
		CreditCheckResultInfo ccrinfo = impl.retrieveLastCreditCheckResultByBan(ban, productType);

		assertEquals("D", ccrinfo.getCreditCheckResultStatus().trim());
	}
	
	@Test
	public void testretrieveSubscriberIdsByServiceFamily() throws ApplicationException{
		String[] subIds=impl.retrieveSubscriberIdsByServiceFamily(8, "P", new Date() );
		assertEquals(2,subIds.length );
		System.out.println( Arrays.asList( subIds) );
		
		subIds=impl.retrieveSubscriberIdsByServiceFamily(8, "U", null );
		assertEquals(0,subIds.length );
		System.out.println( Arrays.asList( subIds) );
		
	}
	
}
