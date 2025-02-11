package com.telus.cmb.account.informationhelper.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ConstantException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.fleet.Fleet;
import com.telus.cmb.account.informationhelper.dao.AccountDao;
import com.telus.cmb.account.informationhelper.dao.AddressDao;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao;
import com.telus.cmb.account.informationhelper.dao.ContactDao;
import com.telus.cmb.account.informationhelper.dao.CreditCheckDao;
import com.telus.cmb.account.informationhelper.dao.DepositDao;
import com.telus.cmb.account.informationhelper.dao.EquipmentDao;
import com.telus.cmb.account.informationhelper.dao.FleetDao;
import com.telus.cmb.account.informationhelper.dao.FollowUpDao;
import com.telus.cmb.account.informationhelper.dao.InvoiceDao;
import com.telus.cmb.account.informationhelper.dao.MemoDao;
import com.telus.cmb.account.informationhelper.dao.PaymentDao;
import com.telus.cmb.account.informationhelper.dao.SubscriberDao;
import com.telus.cmb.account.informationhelper.dao.UsageDao;
import com.telus.cmb.common.aop.utilities.BanValidatorTestHelper;
import com.telus.cmb.common.validation.BanValidator;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
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
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberCountInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;


public class AccountInformationHelperImplTest {

	public static final int PREPAID_BAN = 321;
	private static final int EXCEPTION_BAN = 111;
	public static final int EMPTY_BAN = 999999;
	AccountInformationHelperImpl impl = new AccountInformationHelperImpl();

	private TestAccountInfoDao testAccountInfoDao;
	private TestCeditCheckDao testCreditCheckDao;
	
	private TestSubscriberDao testSubscriberDao;
	private TestUsageDaoImpl usageDao;
	private TestMemoDao testMemoDao;
	private TestInvoiceDao testInvoiceDao;
	private TestFollowUpDao testFollowUpDao;
	private TestFleetDao testFleetDao;
	private TestPaymentDao testPaymentDao;
	private TestEquipmentDao testEquipmentDao;
	private TestAddressDao testAddressDao;
	private TestDepositDao testDepositDao;
	private TestAdjustmentDao testAdjustmentDao;
	private TestContactDao testContactDao;

	@Before
	public void setup() {
		testAccountInfoDao = new TestAccountInfoDao();
		impl.setAccountDao(testAccountInfoDao);

		testCreditCheckDao = new TestCeditCheckDao();
		impl.setCreditCheckDao(testCreditCheckDao);

		//testPrepaidDao = new TestPrepaidDao();
		//impl.setPrepaidDao(testPrepaidDao);	

		testSubscriberDao = new TestSubscriberDao(0, 0);
		impl.setSubscriberDao(testSubscriberDao);

		usageDao = new TestUsageDaoImpl();
		impl.setUsageDao(usageDao);

		testMemoDao = new TestMemoDao();
		impl.setMemoDao(testMemoDao);

		testFollowUpDao = new TestFollowUpDao();
		impl.setFollowUoDao(testFollowUpDao);

		testFleetDao = new TestFleetDao();
		impl.setFleetDao(testFleetDao);

		testPaymentDao = new TestPaymentDao();
		impl.setPaymentDao(testPaymentDao);		

		testEquipmentDao = new TestEquipmentDao();
		impl.setEquipmentDao(testEquipmentDao);

		testAddressDao = new TestAddressDao();
		impl.setAddressDao(testAddressDao);

		testDepositDao = new TestDepositDao();
		impl.setDepositDao(testDepositDao);

		testAdjustmentDao = new TestAdjustmentDao();
		impl.setAdjustmentDao(testAdjustmentDao);

		testInvoiceDao = new TestInvoiceDao();
		impl.setInvoiceDao(testInvoiceDao);
		
		testContactDao = new TestContactDao();
		impl.setContactDao(testContactDao);
	}


	@Test
	public void testRetrieveAccountByBan() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {		
		AccountInfo ai = impl.retrieveAccountByBan(123);

		assertEquals(123, ai.getBanId());		

		try {
			impl.retrieveAccountByBan(EXCEPTION_BAN);
			fail("Exception expected");
		} catch (ApplicationException e) {
			assertEquals(SystemCodes.CMB_AIH_EJB, e.getSystemCode());
		}

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAccountByBan", int.class)
				, new Object[]{null}, 0);

		ai = null;
		TestSubscriberDao subScriberDao = new TestSubscriberDao(0, 0);
		impl.setSubscriberDao(subScriberDao);			

		//		dao.setRetrieveAccountByBanCallableStatementCallback(new TestCallableStatementReturnsPrePaid());

		impl.retrieveAccountByBan(PREPAID_BAN);		
		assertTrue(subScriberDao.retrieveActiveCalled);
		assertTrue(subScriberDao.retrieveSuspendedCalled);

		subScriberDao = new TestSubscriberDao(0, 1);
		impl.setSubscriberDao(subScriberDao);				
		ai = impl.retrieveAccountByBan(PREPAID_BAN);		
		assertTrue(subScriberDao.retrieveActiveCalled);
		assertTrue(subScriberDao.retrieveSuspendedCalled);
		assertEquals("TEST_DAO_RUN", ai.getAccountCategory());

		subScriberDao = new TestSubscriberDao(1, 0);
		impl.setSubscriberDao(subScriberDao);		
		ai = impl.retrieveAccountByBan(PREPAID_BAN);		
		assertTrue(subScriberDao.retrieveActiveCalled);
		assertFalse(subScriberDao.retrieveSuspendedCalled);

		assertEquals("TEST_DAO_RUN", ai.getAccountCategory());
	}

	@Test
	public void testRetrieveAccountsByBans() throws  IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAccountByBan", int.class)
				, new Object[]{null}, 0);	
		assertNull(impl.retrieveAccountsByBan(null));

		assertEquals(0, impl.retrieveAccountsByBan(new int[]{}).size());

		assertEquals(3, impl.retrieveAccountsByBan(new int[]{1,2,3}).size());

		try {
			impl.retrieveAccountsByBan(new int[]{1,2,3});
		} catch (Exception e) {

		}
	}

	@Test
	public void testRetrievePhoneNumbersByBan() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {				
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePhoneNumbersForBAN", int.class)
				, new Object[]{null}, 0);	

		Map<String, String> phoneNumber = impl.retrievePhoneNumbersForBAN(5);

		Assert.assertEquals(String.valueOf(5), phoneNumber.get(String.valueOf(5)));		
	}

	@Test
	public void testRetrieveAccountStatusByBan() throws  IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAccountStatusByBan", int.class)
				, new Object[]{null}, 0);
	}

	@Test
	public void testRetrieveVoiceUsageSummary() throws  IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveVoiceUsageSummary", int.class, String.class)
				, new Object[]{null, null}, 0);
		TestUsageDaoImpl usageDao = new TestUsageDaoImpl();
		impl.setUsageDao(usageDao);

		impl.retrieveVoiceUsageSummary(123, "");
		assertEquals(123, usageDao.getBan());
		assertEquals("STD", usageDao.getFeatureCode());
		impl.retrieveVoiceUsageSummary(123, null);
		assertEquals("STD", usageDao.getFeatureCode());

		impl.retrieveVoiceUsageSummary(123, "HAH");
		assertEquals("HAH", usageDao.getFeatureCode());
	}

	@Test
	public void testRetrieveAccountsByPostalCode() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {				
		String badString = ",.-'/()    ";

		impl.retrieveAccountsByPostalCode(badString, badString, 100);

		assertEquals("", this.testAccountInfoDao.lastName);
		assertEquals("", this.testAccountInfoDao.postalCode);
		assertEquals(100, this.testAccountInfoDao.maximum);

		String longString = "12345678901234567890";

		impl.retrieveAccountsByPostalCode(longString, longString, 100);

		assertEquals("12345678901234", this.testAccountInfoDao.lastName);
		assertEquals(longString, this.testAccountInfoDao.postalCode);
		assertEquals(100, this.testAccountInfoDao.maximum);
	}

	@Test
	public void testRetrieveAccountByPhoneNumber() throws ApplicationException {
		String badPhoneNumber = " 416- ";

		impl.retrieveAccountByPhoneNumber(badPhoneNumber);		
		assertEquals("416", this.testAccountInfoDao.phoneNumber);

		try {
			impl.retrieveAccountByPhoneNumber("0");
			fail ("Exception expected.");
		} catch (ApplicationException e) {

		}
	}

	@Test
	public void testRetrieveAccountsByPhoneNumberStringBooleanBoolean() throws ApplicationException {
		TestAccountInfoDao testAccountDao = new TestAccountInfoDao() {	
			@Override
			public AccountInfo retrieveAccountByBan(int ban) {
				AccountInfo ai = new AccountInfo();
				ai.setBanId(ban);
				return ai;
			}
		};
		impl.setAccountDao(testAccountDao);

		String phoneNumber = "- 416";

		Collection<AccountInfo> accounts = impl.retrieveAccountsByPhoneNumber(phoneNumber, false, false);
		assertFalse(testAccountDao.retrieveAccountsByPhoneNumberCalled);
		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(416, ai.getBanId());
		}

		accounts = impl.retrieveAccountsByPhoneNumber(phoneNumber, false, true);
		assertFalse(testAccountDao.retrieveAccountsByPhoneNumberCalled);
		assertEquals(1, accounts.size());
		for (AccountInfo ai : accounts) {
			assertEquals(416, ai.getBanId());
		}

		TestAccountInfoDao testAccountDaoException = new TestAccountInfoDao() {	
			@Override
			public AccountInfo retrieveAccountByBan(int ban) {
				throw new RuntimeException();
			}
		};
		impl.setAccountDao(testAccountDaoException);
		accounts = impl.retrieveAccountsByPhoneNumber(phoneNumber, true, true);
		assertTrue(testAccountDaoException.retrieveAccountsByPhoneNumberCalled);
		assertEquals("416", testAccountDaoException.phoneNumber);

		testAccountDaoException.retrieveAccountsByPhoneNumberCalled = false;		
		accounts = impl.retrieveAccountsByPhoneNumber(phoneNumber, true, false);
		assertTrue(testAccountDaoException.retrieveAccountsByPhoneNumberCalled);
		assertEquals("416", testAccountDaoException.phoneNumber);		
	}

	@Test
	public void testRetrieveAccountsByPhoneNumberString() throws ApplicationException {
		impl.retrieveAccountsByPhoneNumber("123");

		assertTrue(testAccountInfoDao.retrieveAccountsByPhoneNumberCalled);
	}

	@Test
	public void testRetrieveAccountByBusinessNameSearchInfo() throws ApplicationException {
		String nameType = "nameType";
		String legalBusinessName = "businessName";
		boolean legalBusinessNameExactMatch = true;
		char accountStatus = 'A';
		char accountType = 'A';
		String provinceCode = "ON";
		int brandId = 5;
		int maximum = 6;
		impl.retrieveAccountsByBusinessName(nameType, legalBusinessName
				, legalBusinessNameExactMatch, accountStatus
				, accountType, provinceCode, brandId, maximum);

		assertEquals("", this.testAccountInfoDao.firstName);
		assertFalse(this.testAccountInfoDao.firstNameExactMatch);
		assertEquals(nameType, this.testAccountInfoDao.nameType);
		assertEquals(legalBusinessName, this.testAccountInfoDao.lastName);
		assertEquals(legalBusinessNameExactMatch, this.testAccountInfoDao.lastNameExactMatch);
		assertEquals(accountStatus, this.testAccountInfoDao.accountStatus);
		assertEquals(accountType, this.testAccountInfoDao.accountType);
		assertEquals(provinceCode, this.testAccountInfoDao.provinceCode);
		assertEquals(brandId, this.testAccountInfoDao.brandId);
		assertEquals(maximum, this.testAccountInfoDao.maximum);

		try {
			impl.retrieveAccountsByBusinessName(nameType, ""
					, legalBusinessNameExactMatch, accountStatus
					, accountType, provinceCode, brandId, maximum);
			fail ("Exception expected");
		} catch (ApplicationException e) {

		}

		try {
			impl.retrieveAccountsByBusinessName(nameType, null
					, legalBusinessNameExactMatch, accountStatus
					, accountType, provinceCode, brandId, maximum);
			fail ("Exception expected");
		} catch (ApplicationException e) {

		}
	}

	@Test
	public void testRetrieveAccountsByNameSearchInfo() throws ApplicationException {
		String nameType = "nameType";
		String legalBusinessName = "businessName";
		boolean legalBusinessNameExactMatch = true;
		char accountStatus = 'A';
		char accountType = 'A';
		String provinceCode = "ON";
		int brandId = 5;
		int maximum = 6;
		String firstName = "321321";
		boolean firstNameExactMatch = false;

		impl.retrieveAccountsByName(nameType, firstName, firstNameExactMatch
				, legalBusinessName
				, legalBusinessNameExactMatch, accountStatus
				, accountType, provinceCode, brandId, maximum);

		assertEquals(firstName, this.testAccountInfoDao.firstName);
		assertEquals(firstNameExactMatch, this.testAccountInfoDao.firstNameExactMatch);
		assertEquals(nameType, this.testAccountInfoDao.nameType);
		assertEquals(legalBusinessName, this.testAccountInfoDao.lastName);
		assertEquals(legalBusinessNameExactMatch, this.testAccountInfoDao.lastNameExactMatch);
		assertEquals(accountStatus, this.testAccountInfoDao.accountStatus);
		assertEquals(accountType, this.testAccountInfoDao.accountType);
		assertEquals(provinceCode, this.testAccountInfoDao.provinceCode);
		assertEquals(brandId, this.testAccountInfoDao.brandId);
		assertEquals(maximum, this.testAccountInfoDao.maximum);

		try {
			impl.retrieveAccountsByName(nameType, firstName, firstNameExactMatch, ""
					, legalBusinessNameExactMatch, accountStatus
					, accountType, provinceCode, brandId, maximum);
			fail ("Exception expected");
		} catch (ApplicationException e) {

		}

		try {
			impl.retrieveAccountsByName(nameType, firstName, firstNameExactMatch, null
					, legalBusinessNameExactMatch, accountStatus
					, accountType, provinceCode, brandId, maximum);
			fail ("Exception expected");
		} catch (ApplicationException e) {

		}
	}

	@Test
	public void testRetrieveUnpaidDataUsageTotal() throws ApplicationException,IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException  {
		int x=123;
		Date d= new Date();
		impl.retrieveUnpaidDataUsageTotal(x,d);
		assertEquals(x, this.usageDao.ban1);
		assertEquals(d, this.usageDao.date);
		try {
			impl.retrieveUnpaidDataUsageTotal(x,null);
			fail("Exception Expected");
		} catch (ApplicationException e) {

		}
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveUnpaidDataUsageTotal", int.class, Date.class ), new Object[]{11,d}, 0);
	}

	@Test
	public void testRetrieveUnpaidAirTimeTotal() throws ApplicationException,IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		impl.retrieveUnpaidAirTimeTotal(111);
		assertEquals(111, this.usageDao.ban2);
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveUnpaidAirTimeTotal", int.class), new Object[]{1}, 0);
	}

	@Test
	public void testRetrieveMemos() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveMemos", int.class, int.class ), new Object[]{1,1}, 0);
		impl.retrieveMemos(123,10);
		assertEquals(123, this.testMemoDao.mi.getBanId());

		try{
			impl.retrieveMemos(111,10);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 

		}

		try{
			impl.retrieveMemos(123, -1);
			fail("Exception expected");
		}catch(ConstantException ce){

		}

	}

	@Test
	public void testRetrieveMemosByCriteria() throws ApplicationException{

		MemoCriteriaInfo mcriteria=new MemoCriteriaInfo();
		mcriteria.setBanId(123);
		mcriteria.setSubscriberId("6742349282");

		impl.retrieveMemos(mcriteria);
		assertEquals("6742349282", this.testMemoDao.mi.getSubscriberId());
		assertEquals("0001", this.testMemoDao.mi.getMemoType());

		try{
			mcriteria.setBanId(0);
			impl.retrieveMemos(mcriteria);
			fail("Exception Expected");
		}catch(ConstantException ce){

		}

	}

	@Test
	public void testRetrieveLastMemo() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveLastMemo", int.class, String.class ), new Object[]{1,"TST"}, 0);
		impl.retrieveLastMemo(123,"0001");
		assertEquals("Test", this.testMemoDao.mi.getText());

		try{
			impl.retrieveLastMemo(111,"0002");
			assertEquals("Test", this.testMemoDao.mi.getText());
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 

		}

		try{
			impl.retrieveLastMemo(123, "");
			fail("Exception expected");
		}catch(ConstantException ce){

		}

	}

	@Test
	public void testRetrieveAccountsByDealership() {		
		Date startDate = new Date();
		Date endDate = new Date();
		impl.retrieveAccountsByDealership('A', "ABC", startDate, endDate, 10);

		assertTrue(this.testAccountInfoDao.retrieveAccountByDealershipCalled);
		assertEquals('A', this.testAccountInfoDao.accountStatus);
		assertEquals("ABC", this.testAccountInfoDao.dealerCode);
		assertEquals(startDate, this.testAccountInfoDao.startDate);
		assertEquals(endDate, this.testAccountInfoDao.endDate);
		assertEquals(10, this.testAccountInfoDao.maximum);
	}

	@Test
	public void testRetrieveAccountsBySalesRepCode() {
		Date startDate = new Date();
		Date endDate = new Date();
		impl.retrieveAccountsBySalesRep('B', "CBA", "123", startDate, endDate, 101);

		assertEquals('B', this.testAccountInfoDao.accountStatus);
		assertEquals("CBA", this.testAccountInfoDao.dealerCode);
		assertEquals("123", this.testAccountInfoDao.salesRepCode);
		assertEquals(startDate, this.testAccountInfoDao.startDate);
		assertEquals(endDate, this.testAccountInfoDao.endDate);
		assertEquals(101, this.testAccountInfoDao.maximum);
	}

	@Test
	public void testRetrieveAccountInfoForPayAndTalkCustomer() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		impl.retrieveAccountInfoForPayAndTalkSubscriber(123, "123");

		//assertEquals(123, this.testPrepaidDao.ban);
		//assertEquals("123", this.testPrepaidDao.phoneNumber);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAccountInfoForPayAndTalkSubscriber", int.class, String.class ), new Object[]{1,"123"}, 0);
	}

	@Test
	public void testRetrieveBanIds() {
		impl.retrieveBanIds('A', 'B', 'C', 102);
		assertEquals('A', this.testAccountInfoDao.accountType);		
		assertEquals('B', this.testAccountInfoDao.accountSubType);
		assertEquals('C', this.testAccountInfoDao.banStatus);

		assertEquals(102, this.testAccountInfoDao.maximum);
	}

	@Test
	public void testRetrieveBanIdsByAddressType() {
		impl.retrieveBanIdByAddressType('A', 'B', 'C', 'D', 102);
		assertEquals('A', this.testAccountInfoDao.accountType);		
		assertEquals('B', this.testAccountInfoDao.accountSubType);
		assertEquals('C', this.testAccountInfoDao.banStatus);
		assertEquals('D', this.testAccountInfoDao.addressType);

		assertEquals(102, this.testAccountInfoDao.maximum);
	}

	@Test
	public void testRetrieveAttachedSubscribersCount() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		FleetIdentityInfo fleetIdentityInfo = new FleetIdentityInfo();
		int fleetId = 15;
		fleetIdentityInfo.setFleetId(fleetId);
		int urbanId = 12;
		fleetIdentityInfo.setUrbanId(urbanId);
		int ban = 10;
		impl.retrieveAttachedSubscribersCount(ban, fleetIdentityInfo);

		assertEquals(this.testSubscriberDao.ban, ban);
		assertEquals(this.testSubscriberDao.fleetIdentityInfo.getFleetId(), fleetId);
		assertEquals(this.testSubscriberDao.fleetIdentityInfo.getUrbanId(), urbanId);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAttachedSubscribersCount", int.class, FleetIdentityInfo.class ), new Object[]{1,fleetIdentityInfo}, 0);
	}

	@Test
	public void testRetrieveProductSubscriberList() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		int ban = 10;
		impl.retrieveProductSubscriberLists(ban);

		assertEquals(ban, this.testSubscriberDao.ban);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveProductSubscriberLists", int.class), new Object[]{1}, 0);
	}

	@Test
	public void testretrieveFollowUpAdditionalText() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFollowUpAdditionalText", int.class, int.class ), new Object[]{null,0}, 0);
		impl.retrieveFollowUpAdditionalText(123, 11545271);
		assertEquals("12114", this.testFollowUpDao.fti.getOperatorId());

		try{
			impl.retrieveFollowUpAdditionalText(111, 11545271);
			assertEquals("12114", this.testFollowUpDao.fti.getOperatorId());
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 

		}

		try{
			impl.retrieveFollowUpAdditionalText(123, -123);
			fail("Exception expected");
		}catch(ConstantException ce){

		}
	}

	@Test
	public void testretrieveFollowUpHistory(){

		impl.retrieveFollowUpHistory(1242);
		assertEquals(1234, this.testFollowUpDao.fi1.getBanId());
		assertEquals("5678", this.testFollowUpDao.fi1.getSubscriberId());

		try{
			impl.retrieveFollowUpHistory(-1242);
			fail("Exception Expected");
		}catch(ConstantException ce){

		}
	}

	@Test
	public void testretrieveFollowUpInfoByBanFollowUpID() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFollowUpInfoByBanFollowUpID", int.class, int.class ), new Object[]{null,0}, 0);
		impl.retrieveFollowUpInfoByBanFollowUpID(123, 456);
		assertEquals("RS_200", this.testFollowUpDao.fi2.getAssignedToWorkPositionId());

		try{
			impl.retrieveFollowUpInfoByBanFollowUpID(111, 456);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 

		}

		try{
			impl.retrieveFollowUpInfoByBanFollowUpID(123, -123);
			fail("Exception expected");
		}catch(ConstantException ce){

		}
	}

	@Test
	public void testretrieveFollowUpsByCriteria() throws ApplicationException{

		FollowUpCriteriaInfo fci=new FollowUpCriteriaInfo();
		fci.setWorkPositionId("RS_C0020");

		impl.retrieveFollowUps(fci);
		assertEquals(123456, this.testFollowUpDao.fi3.getBanId());

		try{
			fci.setWorkPositionId("");
			impl.retrieveFollowUps(fci);
			fail("Exception Expected");
		}catch(ConstantException ce){

		}

	}

	@Test
	public void testretrieveFollowUps() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFollowUps", int.class, int.class ), new Object[]{null,0}, 0);
		impl.retrieveFollowUps(123, 123);
		assertEquals('o', this.testFollowUpDao.fi4.getStatus());

		try{
			impl.retrieveFollowUps(111, 123);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 

		}

		try{
			impl.retrieveFollowUps(123, -123);
			fail("Exception expected");
		}catch(ConstantException ce){

		}
	}

	@Test
	public void testretrieveFollowUpStatistics() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFollowUpStatistics", int.class ), new Object[]{null}, 0);
		impl.retrieveFollowUpStatistics(123);
		assertTrue(this.testFollowUpDao.fsi.hasDueFollowUps());

		try{
			impl.retrieveFollowUpStatistics(111);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

	}

	@Test
	public void testretrieveLastPaymentActivity() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{

		PaymentHistoryInfo info = new PaymentHistoryInfo();
		assertEquals(info.getSeqNo(),impl.retrieveLastPaymentActivity(123).getSeqNo()); 		
	}	

	@Test
	public void testretrieveLastFollowUpIDByBanFollowUpType() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveLastFollowUpIDByBanFollowUpType", int.class,String.class ), new Object[]{null,null}, 0);
		assertEquals(10,impl.retrieveLastFollowUpIDByBanFollowUpType(123,"test"));

		try{
			impl.retrieveLastFollowUpIDByBanFollowUpType(111,"test");
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

		try{
			impl.retrieveLastFollowUpIDByBanFollowUpType(111,"");
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}
	}

	@Test
	public void testRetrieveFleetsByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, TelusAPIException {
		FleetInfo[] fleets = impl.retrieveFleetsByBan(EMPTY_BAN);

		assertEquals(0, fleets.length);

		fleets = impl.retrieveFleetsByBan(2);
		assertEquals(2, fleets.length);
		for (Fleet f : fleets) {
			assertEquals(4, f.getAssociatedAccountsCount());
		}

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFleetsByBan", int.class), new Object[]{0}, 0);
	}
	@Test
	public void testRetrieveAccountsByImsi() throws ApplicationException {
		AccountInfo ai = impl.retrieveAccountByImsi("123");
		assertEquals("123", testAccountInfoDao.imsi);
		assertEquals(123, ai.getBanId());

		ai = impl.retrieveAccountByImsi("ABC");
		assertEquals("ABC", testAccountInfoDao.imsi);
		assertEquals(null, ai);
	}

	@Test
	public void testRetrieveSubscriberIdsByStatus() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {				

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveSubscriberIdsByStatus", int.class,char.class, int.class ), new Object[]{0,'x',0}, 0);

		System.out.println("Test 1: Valid input for BAN, STATUS, MAXIMUM");
		impl.retrieveSubscriberIdsByStatus(20001552, 'C', 50);
		assertEquals(20001552, this.testSubscriberDao.ban);
		assertEquals('C', this.testSubscriberDao.status);
		assertEquals(50, this.testSubscriberDao.maximum);

		System.out.println("Test 2: Invalid BAN");
		try{
			impl.retrieveSubscriberIdsByStatus(111,'X',10);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

	}

	@Test
	public void testRetrieveSubscriberPhoneNumbersByStatus() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {				

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveSubscriberPhoneNumbersByStatus", int.class,char.class, int.class ), new Object[]{0,'x',0}, 0);

		System.out.println("Test 1: Valid input for BAN, STATUS, MAXIMUM");
		impl.retrieveSubscriberPhoneNumbersByStatus(20001552, 'A', 50);
		assertEquals(20001552, this.testSubscriberDao.ban);
		assertEquals('A', this.testSubscriberDao.status);
		assertEquals(50, this.testSubscriberDao.maximum);

		System.out.println("Test 2: Invalid BAN");
		try{
			impl.retrieveSubscriberPhoneNumbersByStatus(111,'X',10);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

	}

	@Test
	public void testRetrieveAccountsBySerialNumber() throws ApplicationException {
		Collection<AccountInfo> accounts = impl.retrieveAccountsBySerialNumber(TestEquipmentDao.SERIAL_TO_USIM_TO_IMSI);

		for (AccountInfo account : accounts) {
			assertEquals(1 ,account.getBanId());
		}

		assertTrue(testEquipmentDao.getUsimBySerialNumberCalled);
		assertTrue(testEquipmentDao.getImsisByUsimCalled);

		testEquipmentDao.getUsimBySerialNumberCalled = false;
		testEquipmentDao.getImsisByUsimCalled = false;

		impl.retrieveAccountsBySerialNumber("321321");
		assertTrue(testEquipmentDao.getUsimBySerialNumberCalled);
		assertFalse(testEquipmentDao.getImsisByUsimCalled);
		assertEquals("321321", this.testAccountInfoDao.serialNumber);		
	}
	@Test
	public void testRetrieveAccountBySerialNumber() throws ApplicationException {
		AccountInfo account = impl.retrieveAccountBySerialNumber(TestEquipmentDao.SERIAL_TO_USIM_TO_IMSI);

		assertEquals(1 ,account.getBanId());

		assertTrue(testEquipmentDao.getUsimBySerialNumberCalled);
		assertTrue(testEquipmentDao.getImsisByUsimCalled);

		testEquipmentDao.getUsimBySerialNumberCalled = false;
		testEquipmentDao.getImsisByUsimCalled = false;

		impl.retrieveAccountBySerialNumber("321321");
		assertTrue(testEquipmentDao.getUsimBySerialNumberCalled);
		assertFalse(testEquipmentDao.getImsisByUsimCalled);
		assertEquals("321321", this.testAccountInfoDao.serialNumber);

	}

	@Test
	public void testRetrieveLastCreditCheckMemoByBan() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveLastCreditCheckMemoByBan", int.class), new Object[]{null}, 0);
		impl.retrieveLastCreditCheckMemoByBan(123);
		assertEquals(123,testMemoDao.mi.getBanId());
		assertEquals("CreditCheck", testMemoDao.mi.getMemoType());

		try{
			impl.retrieveLastCreditCheckMemoByBan(111);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}
	}
	@Test
	public void testretrievePaymentActivities()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePaymentActivities", int.class,int.class), new Object[]{1,1}, 0);
		impl.retrievePaymentActivities(12, 12);
		assertEquals(1,this.testPaymentDao.list.size());
		assertEquals("asdf",this.testPaymentDao.list.get(0).getActivityCode());
	}	

	@Test
	public void testRetrieveTalkGroupsByBan()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveTalkGroupsByBan", int.class), new Object[]{1}, 0);
		impl.retrieveTalkGroupsByBan(1212);
		assertEquals(1212,this.testFleetDao.ban);
	}
	@Test
	public void testRetrievePaymentMethodChangeHistory()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePaymentMethodChangeHistory", int.class,Date.class,Date.class), new Object[]{1,new Date(),new Date()}, 0);
		impl.retrievePaymentMethodChangeHistory(1221, new Date(),new Date());
		assertEquals(1,this.testPaymentDao.list1.size());
		assertEquals("ICI",this.testPaymentDao.list1.get(0).getBankCode());
		try{
			impl.retrievePaymentMethodChangeHistory(1221, null,null);
			fail("Exception Expected");
		}catch(Exception e){}
	}

	@Test
	public void testRetrieveHotlinedSubscriberPhoneNumber()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveHotlinedSubscriberPhoneNumber", int.class), new Object[]{1}, 0);		
		impl.retrieveHotlinedSubscriberPhoneNumber(123);
		assertEquals("123",impl.retrieveHotlinedSubscriberPhoneNumber(123));
	}	

	@Test
	public void testRetrieveInvoiceHistory()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveInvoiceHistory", int.class,Date.class,Date.class), new Object[]{1,null,null}, 0);		
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.retrieveInvoiceHistory(123,new Date(),new Date());
		assertEquals(123,invoiceDao.getBan());
	}	
	@Test
	public void testRetrieveAttachedSubscribersCountForTalkGroup()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAttachedSubscribersCountForTalkGroup", int.class,int.class,int.class,int.class), new Object[]{1,1,1,1}, 3);		
		assertEquals(144,impl.retrieveAttachedSubscribersCountForTalkGroup(12,12,1,2121));
	}

	@Test
	public void testRetrieveCorporateName()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		TestAccountInfoDao accInfDao= new TestAccountInfoDao();
		impl.setAccountDao(accInfDao);
		impl.retrieveCorporateName(324);
		assertEquals(324,accInfDao.id);
	}
	@Test
	public void testGetClientAccountId()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("getClientAccountId", int.class), new Object[]{1}, 0);		
		TestAccountInfoDao accInfDao= new TestAccountInfoDao();
		impl.setAccountDao(accInfDao);
		impl.getClientAccountId(123);
		assertEquals(123,accInfDao.getBan());
	}
	@Test
	public void testIsEBillRegistrationReminderExist()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("isEBillRegistrationReminderExist", int.class), new Object[]{1}, 0);		
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.isEBillRegistrationReminderExist(123);
		assertEquals(123,invoiceDao.getBan());
	}
	@Test
	public void testHasEPostSubscription()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("hasEPostSubscription", int.class), new Object[]{1}, 0);		
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.isEBillRegistrationReminderExist(312);
		assertEquals(312,invoiceDao.getBan());
	}
	@Test
	public void testIsFeatureCategoryExistOnSubscribers()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("isFeatureCategoryExistOnSubscribers", int.class,String.class), new Object[]{1,null}, 0);		
		TestSubscriberDao subscriberDao= new TestSubscriberDao(0,0);
		impl.setSubscriberDao(subscriberDao);
		impl.isFeatureCategoryExistOnSubscribers(312,"VM");
		assertEquals(312,subscriberDao.ban);
		assertEquals("VM",subscriberDao.pCategoryCode);
	}


	@Test
	public void testRetrieveBilledCharges()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.retrieveBilledCharges(123,21,"asd",null,null);
		assertEquals(123,invoiceDao.getBan());
		assertEquals("asd",invoiceDao.phoneNumber);
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveBilledCharges", int.class,int.class,String.class,Date.class,Date.class), new Object[]{1,1,"",null,null}, 0);		
	}
	@Test
	public void testRetrieveBillNotificationContacts()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.retrieveBillNotificationContacts(123);
		assertEquals(123,invoiceDao.getBan());
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveBillNotificationContacts", int.class), new Object[]{1}, 0);		

	}

	@Test
	public void testGetLastEBillNotificationSent()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("getLastEBillNotificationSent", int.class), new Object[]{1}, 0);		
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.getLastEBillNotificationSent(123);
		assertEquals(123,invoiceDao.getBan());
	}
	@Test
	public void testGetLastEBillRegistrationReminderSent()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("getLastEBillRegistrationReminderSent", int.class), new Object[]{1}, 0);
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.getLastEBillRegistrationReminderSent(211);
		assertEquals(211,invoiceDao.getBan());
	}
	@Test
	public void testGetBillNotificationHistory()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("getBillNotificationHistory", int.class,String.class), new Object[]{1,null}, 0);
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.getBillNotificationHistory(111,"EPOST");
		assertEquals(111,invoiceDao.getBan());
	}
	@Test
	public void testRetrievePaymentHistory()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePaymentHistory", int.class,Date.class,Date.class), new Object[]{1, new Date(), new Date()}, 0);		
		TestPaymentDao paymentDao= new TestPaymentDao();
		impl.setPaymentDao(paymentDao);
		impl.retrievePaymentHistory(123,new Date(),new Date());
		assertEquals(123,paymentDao.getBan());
		try{
			impl.retrievePaymentHistory(123,null,null);
			fail("Exception Expected");
		}catch(Exception e){}
	}

	@Test
	public void testRetrieveRefundHistory()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveRefundHistory", int.class,Date.class,Date.class), new Object[]{1, new Date(), new Date()}, 0);		
		TestPaymentDao paymentDao= new TestPaymentDao();
		impl.setPaymentDao(paymentDao);
		impl.retrieveRefundHistory(20007810,new Date(),new Date());
		assertEquals(20007810,paymentDao.getBan());
		try{
			impl.retrieveRefundHistory(20007810,null,null);
			fail("Exception Expected");
		}catch(Exception e){}
	}

	@Test
	public void testGetInvoiceProperties()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("getInvoiceProperties", int.class), new Object[]{1}, 0);		
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.getInvoiceProperties(123);
		assertEquals(123,invoiceDao.getBan());
	}

	@Test
	public void testRetrieveSubscriberInvoiceDetails()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveSubscriberInvoiceDetails", int.class,int.class), new Object[]{1,1}, 0);

		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		System.out.println("Test 1: Valid input for BAN, BillSeqNo");
		impl.retrieveSubscriberInvoiceDetails(51836, 8);
		assertEquals(51836, invoiceDao.getBan());
		assertEquals(8, invoiceDao.getBillSeqNo());


		System.out.println("Test 2: Invalid BAN");
		try{
			impl.retrieveSubscriberInvoiceDetails(111, 8);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}
	}
	@Test
	public void testExpireBillNotificationDetails()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("expireBillNotificationDetails", int.class), new Object[]{1}, 0);
		TestInvoiceDao invoiceDao= new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);
		impl.expireBillNotificationDetails(324);
		assertEquals(324,invoiceDao.getBan());
	}

	@Test
	public void testRetrieveAlternetAddressByBan() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAlternateAddressByBan", int.class), new Object[]{1}, 0);
		impl.retrieveAlternateAddressByBan(100);
		assertEquals(100, testAddressDao.ban);
	}

	@Test
	public void testRetrieveBusinessList() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveBusinessList", int.class), new Object[]{1}, 0);
		impl.retrieveBusinessList(100);
		assertEquals(100, testAccountInfoDao.ban);
	}

	@Test
	public void testRetrieveAuthorizedNames() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveAuthorizedNames", int.class), new Object[]{1}, 0);
		impl.retrieveAuthorizedNames(101);
		assertEquals(101, testAccountInfoDao.ban);
	}

	@Test
	public void testRetrieveDeposityHistory() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {		
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveDepositHistory", int.class, Date.class, Date.class), new Object[]{1, null, null}, 0);

		int ban = 400;
		Date fromDate = new Date();
		Date toDate = new Date();
		impl.retrieveDepositHistory(ban, fromDate, toDate);

		assertEquals(ban, this.testDepositDao.ban);
		assertEquals(fromDate, this.testDepositDao.fromDate);
		assertEquals(toDate, this.testDepositDao.toDate);

	}

	@Test
	public void testretrieveCreditByFollowUpId() throws ApplicationException{
		impl.retrieveCreditByFollowUpId(12345);
		assertEquals(100.21, testAdjustmentDao.ci.getAmount(),0);
		assertEquals("test  ",testAdjustmentDao.ci.getReasonCode());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits1() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveCredits", int.class,Date.class,Date.class,String.class,char.class,String.class,int.class), new Object[]{1,null,null,null,'1',null,1}, 0);

		SearchResultsInfo sri=null;
		CreditInfo[] ci=null;
		sri=impl.retrieveCredits(123,new Date(104,0,01), new Date(107,12,01), "U", '1', "", 100); 
		ci=(CreditInfo[]) sri.getItems();
		assertEquals(123,ci[0].getBan());
		assertEquals(2,ci.length);
		assertTrue(sri.hasMore());
		assertTrue(testAdjustmentDao.unbilledCredits);
		assertFalse(testAdjustmentDao.billedCredits);

		try{
			impl.retrieveCredits(111,new Date(104,0,01), new Date(107,12,01), "U", '1', "", 100);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

		sri=impl.retrieveCredits(1001,new Date(104,0,01), new Date(107,12,01), "U", '1', "", 100);
		ci=(CreditInfo[]) sri.getItems();
		assertEquals(0,ci.length);
		assertFalse(sri.hasMore());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits2() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveCredits", int.class,Date.class,Date.class,String.class,char.class,String.class,String.class,int.class), new Object[]{1,null,null,null,'1',null,null,1}, 0);

		SearchResultsInfo sri=null;
		CreditInfo[] ci=null;
		sri=impl.retrieveCredits(456,new Date(104,0,01), new Date(107,12,01), "B", '1',"", "", 100); 
		ci=(CreditInfo[]) sri.getItems();

		assertEquals(135,ci[1].getBan());
		assertEquals(2,ci.length);
		assertTrue(sri.hasMore());
		assertTrue(testAdjustmentDao.billedCredits);
		assertFalse(testAdjustmentDao.unbilledCredits);

		try{
			impl.retrieveCredits(111,new Date(104,0,01), new Date(107,12,01), "B", '1',"", "", 100);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

		sri=impl.retrieveCredits(1001,new Date(104,0,01), new Date(107,12,01), "B", '1',"", "", 100);
		ci=(CreditInfo[]) sri.getItems();
		assertEquals(0,ci.length);
		assertFalse(sri.hasMore());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testretrieveCredits3() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveCredits", int.class,Date.class,Date.class,String.class,String.class,String.class,char.class,String.class,int.class), new Object[]{1,null,null,null,null,null,'1',null,1}, 0);		

		SearchResultsInfo sri=null;
		CreditInfo[] ci=null;
		sri=impl.retrieveCredits(123,new Date(104,0,01), new Date(107,12,01),"A","", "", '1', "", 100); 
		ci=(CreditInfo[]) sri.getItems();
		assertEquals(789,ci[2].getBan());
		assertEquals(4,ci.length);
		assertTrue(sri.hasMore());
		assertTrue(testAdjustmentDao.unbilledCredits);
		assertTrue(testAdjustmentDao.billedCredits);

		try{
			impl.retrieveCredits(111,new Date(104,0,01), new Date(107,12,01),"A","", "", '1', "", 100);
			fail("Exception expected");
		}catch(UncategorizedSQLException e){ 
		}

		sri=impl.retrieveCredits(1001,new Date(104,0,01), new Date(107,12,01),"A","", "", '1', "", 100);
		ci=(CreditInfo[]) sri.getItems();
		assertEquals(0,ci.length);
		assertFalse(sri.hasMore());
	}

	@Test
	public void testRetrieveAssessDepositHistory() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveDepositAssessedHistoryList", int.class), new Object[]{1}, 0);
		impl.retrieveDepositAssessedHistoryList(2);
		assertEquals(2, this.testDepositDao.ban);
	}

	@Test
	public void testRetrieveCharges() throws ApplicationException, IllegalArgumentException, SecurityException, NegativeArraySizeException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveCharges", int.class,
				Array.newInstance(String.class, 0).getClass(), String.class, char.class, String.class, Date.class,
				Date.class, int.class), new Object[]{1, new String[]{}, null, 'A', null, null, null, 0}, 0);

		int ban = 123;
		String[] chargeCodes = new String[]{"123", "32321"};
		String billState = "state";
		char level = 'A';
		String subscriberId = "subscriberId";
		Date from = new Date();
		Date to = new Date();
		int maximum = 5;
		impl.retrieveCharges(ban, chargeCodes, billState, level, subscriberId, from, to, maximum);

		assertEquals(ban, this.testAdjustmentDao.ban);
		Assert.assertArrayEquals(chargeCodes, this.testAdjustmentDao.chargeCodes);
		assertEquals(billState, this.testAdjustmentDao.billState);
		assertEquals(level, this.testAdjustmentDao.level);
		assertEquals(subscriberId, this.testAdjustmentDao.subscriberId);
		assertEquals(from, this.testAdjustmentDao.from);
		assertEquals(to, this.testAdjustmentDao.to);
		assertEquals(maximum, this.testAdjustmentDao.maximum);
	}

	@Test
	public void testRetrieveOriginalDeposityAssessedHistoryList() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveOriginalDepositAssessedHistoryList", int.class), new Object[]{1}, 0);
		impl.retrieveOriginalDepositAssessedHistoryList(123);
		assertEquals(123, testDepositDao.ban);
	}

	@Test
	public void testRetrievePCSNetworkCountByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePCSNetworkCountByBan", int.class), new Object[]{1}, 0);
		impl.retrievePCSNetworkCountByBan(8);
		assertEquals(8, testSubscriberDao.ban);
	}

	@Test
	public void testretrieveZeroMinutePoolingPricePlanSubscriberCounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveZeroMinutePoolingPricePlanSubscriberCounts", int.class), new Object[]{1}, 0);
		impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(123);
		assertEquals(123, testSubscriberDao.ban);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveZeroMinutePoolingPricePlanSubscriberCounts", int.class,int.class), new Object[]{1,1}, 0);
		impl.retrieveZeroMinutePoolingPricePlanSubscriberCounts(456, 4);
		assertEquals(456, testSubscriberDao.ban);
	}

	@Test
	public void testretrievePoolingPricePlanSubscriberCounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePoolingPricePlanSubscriberCounts", int.class), new Object[]{1}, 0);
		impl.retrievePoolingPricePlanSubscriberCounts(123);
		assertEquals(123, testSubscriberDao.ban);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrievePoolingPricePlanSubscriberCounts", int.class,int.class), new Object[]{1,1}, 0);
		impl.retrievePoolingPricePlanSubscriberCounts(456, 4);
		assertEquals(456, testSubscriberDao.ban);
	}

	@Test
	public void testretrieveServiceSubscriberCounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveServiceSubscriberCounts", int.class,String[].class,boolean.class), new Object[]{1,new String[]{"",""},true}, 0);
		impl.retrieveServiceSubscriberCounts(789, new String[]{}, true);
		assertEquals(789, testSubscriberDao.ban);
	}

	@Test
	public void testretrieveMinutePoolingEnabledPricePlanSubscriberCounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveMinutePoolingEnabledPricePlanSubscriberCounts", int.class,String[].class), new Object[]{1,new String[]{"",""}}, 0);
		impl.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(012, new String[]{});
		assertEquals(012, testSubscriberDao.ban);
	}

	@Test
	public void testretrieveDollarPoolingPricePlanSubscriberCounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveDollarPoolingPricePlanSubscriberCounts", int.class,String.class), new Object[]{1,""}, 0);
		impl.retrieveDollarPoolingPricePlanSubscriberCounts(345, "");
		assertEquals(345, testSubscriberDao.ban);
	}

	@Test
	public void testretrieveShareablePricePlanSubscriberCount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveShareablePricePlanSubscriberCount", int.class), new Object[]{1}, 0);
		impl.retrieveShareablePricePlanSubscriberCount(678);
		assertEquals(678, testSubscriberDao.ban);
	}

	@Test
	public void testvalidatePayAndTalkSubscriberActivation() throws ApplicationException{
		AuditHeader auditHeader = new AuditHeaderInfo();
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo=new PrepaidConsumerAccountInfo();
		assertEquals("SimpleTest",impl.validatePayAndTalkSubscriberActivation("Simple", "Test", prepaidConsumerAccountInfo, auditHeader));
	}

	@Test
	public void testRetrieveStatusChangeHistory() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveStatusChangeHistory", 
				int.class, Date.class, Date.class),
				new Object[]{1, null, null}, 0);	

		int ban = 321;
		Date fromDate = new Date();
		Date toDate = new Date();
		impl.retrieveStatusChangeHistory(ban, fromDate, toDate);

		assertEquals(ban, testAccountInfoDao.ban);
		assertEquals(fromDate, testAccountInfoDao.fromDate);
		assertEquals(toDate, testAccountInfoDao.toDate);
	}
	
	@Test
	public void testRetrieveLastCreditCheckResultByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveLastCreditCheckResultByBan", int.class, String.class)
				, new Object[]{0, null}, 0);
		
		int ban = 123123;
		String productType="abc";
		impl.retrieveLastCreditCheckResultByBan(ban, productType);

		assertEquals(ban, testCreditCheckDao.ban);
		assertEquals(productType, testCreditCheckDao.productType);
	}
	
	@Test
	public void testRetrieveBillingInformation() throws ApplicationException, RemoteException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {		
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveBillingInformation", int.class)
				, new Object[]{0}, 0);
		
		int billingAccountNumber=8;
		impl.retrieveBillingInformation(billingAccountNumber);
		assertEquals(billingAccountNumber,testContactDao.ban);
	}
	
	@Test
	public void testRetrieveContactInformation() throws ApplicationException, RemoteException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {		
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveContactInformation", int.class)
				, new Object[]{0}, 0);
		
		int billingAccountNumber=8;
		impl.retrieveContactInformation(billingAccountNumber);
		assertEquals(billingAccountNumber,testContactDao.ban);
	}


	private static class TestUsageDaoImpl implements UsageDao {

		private int ban;
		private String featureCode;
		private int ban1;
		private Date date;
		private int ban2;

		@Override
		public Collection<VoiceUsageSummaryInfo> retrieveVoiceUsageSummary(
				int ban, String featureCode) {
			this.ban = ban;
			this.featureCode = featureCode;

			return null;
		}

		public int getBan() {
			return ban;
		}

		public String getFeatureCode() {
			return featureCode;
		}
		@Override
		public double retrieveUnpaidDataUsageTotal(int ban, Date date) {
			this.ban1=ban;
			this.date=date;			
			return 0;
		}

		@Override
		public double retrieveUnpaidAirTimeTotal(int ban) {
			this.ban2=ban;
			return 0;
		}
		
		@Override
		public AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban) throws ApplicationException {
			return null;
		}

	}

	private static class TestSubscriberDao implements SubscriberDao {
		private int activeCount;
		private int suspendedCount;

		public TestSubscriberDao(int activeCount, int suspendedCount) {
			this.activeCount = activeCount;
			this.suspendedCount = suspendedCount;
		}

		public boolean retrieveActiveCalled = false;
		public boolean retrieveSuspendedCalled = false;
		private int ban;
		private FleetIdentityInfo fleetIdentityInfo;
		private char status;
		private int maximum;
		private String pCategoryCode;

		@Override
		public Collection<String> retrieveActiveSubscriberPhoneNumbers(
				int pBan, int pMaxNumbers) {
			retrieveActiveCalled = true;
			Collection<String> strings = new ArrayList<String>();

			for (int i = 0; i < activeCount; i++) {
				strings.add(String.valueOf(i));
			}			

			return strings;
		}

		@Override
		public HashMap<String,Integer> retrievePCSNetworkCountByBan(final int ban) {
			this.ban = ban;
			return null;
		}


		@Override
		public Collection<String> retrieveSuspendedSubscriberPhoneNumbers(
				int pBan, int pMaxNumbers) {
			retrieveSuspendedCalled = true;
			Collection<String> strings = new ArrayList<String>();

			for (int i = 0; i < suspendedCount; i++) {
				strings.add(String.valueOf(i));
			}

			return strings;
		}

		@Override
		public Map<String, String> retrievePhoneNumbersForBAN(int ban) {
			Map<String, String> returnMap = new HashMap<String, String>(); 

			returnMap.put(String.valueOf(ban), String.valueOf(ban));

			return returnMap;
		}

		@Override
		public int retrieveAttachedSubscribersCount(int ban,
				FleetIdentityInfo fleetIdentityInfo) {
			this.ban = ban;
			this.fleetIdentityInfo = fleetIdentityInfo;
			return 0;
		}

		@Override
		public Collection<ProductSubscriberListInfo> retrieveProductSubscriberLists(
				int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public List<String> retrieveSubscriberIdsByStatus(
				int banId, char status, int maximum) {

			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
				this.status=status;
				this.maximum=maximum;
			}

			return null;
		}

		@Override
		public List<String> retrieveSubscriberPhoneNumbersByStatus(
				int banId, char status, int maximum) {

			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
				this.status=status;
				this.maximum=maximum;
			}

			return null;
		}

		@Override
		public boolean isFeatureCategoryExistOnSubscribers(int ban,
				String pCategoryCode) {
			this.ban=ban;
			this.pCategoryCode=pCategoryCode;
			return false;
		}

		@Override
		public String retrieveHotlinedSubscriberPhoneNumber(int ban) {
			return String.valueOf(ban);
		}

		@Override
		public List<PoolingPricePlanSubscriberCountInfo> retrievePoolingPricePlanSubscriberCounts(
				int banId, int poolGroupId, boolean zeroMinute) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public List<ServiceSubscriberCount> retrieveServiceSubscriberCounts(
				int banId, String[] serviceCodes, boolean includeExpired) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(
				int banId, String[] poolingCoverageTypes) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public List<PricePlanSubscriberCountInfo> retrievePricePlanSubscriberCountInfo(
				int banId, String productType) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public List<PricePlanSubscriberCountInfo> retrieveShareablePricePlanSubscriberCount(
				int ban) {
			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = ban;
			}
			return null;
		}

		@Override
		public List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(
				int banId) {
			this.ban=ban;
			return null;
		}

		@Override
		public String[] retrieveSubscriberIdsByServiceFamily(int banId,
				String familyTypeCode, Date effectiveDate) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(
				int banId, String[] dataSharingGroupCodes, Date effectiveDate) {
			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban = banId;
			}
			return null;
		}

		@Override
		public String retrieveChangedSubscriber(int ban, String subscriberId,
				String productType, Date searchFromDate, Date searchToDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.account.informationhelper.dao.SubscriberDao#retrieveSubscriberDataSharingInfoList(int, java.lang.String[])
		 */
		@Override
		public Collection<DataSharingResultInfo> retrieveSubscriberDataSharingInfoList(
				int banId, String[] dataSharingGroupCodes)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.account.informationhelper.dao.SubscriberDao#retrieveFamilyTypesBySocs(java.lang.String[])
		 */
		@Override
		public Map<String, List<String>> retrieveFamilyTypesBySocs(
				String[] socCodes) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	


	private static class TestCeditCheckDao implements CreditCheckDao {

		private int ban;
		private String productType;

		@Override
		public CreditCheckResultInfo retrieveLastCreditCheckResultByBan(
				int pBan, String pProductType) {
			this.ban=pBan;
			this.productType=pProductType;
			return null;
		}
		
		@Override
		public CreditCheckResultDeposit[] retrieveDepositsByBan(int ban) {
			this.ban=ban;
			return null;
		}
	}

	private static class TestAccountInfoDao implements AccountDao {

		private String lastName;
		private String postalCode;
		private int maximum;

		private boolean retrieveAccountsByPhoneNumberCalled = false;
		private String phoneNumber;
		private String firstName;
		private boolean firstNameExactMatch;
		private String nameType;
		private char accountStatus;
		private char accountType;
		private String provinceCode;
		private int brandId;
		private boolean lastNameExactMatch;
		private String dealerCode;
		private Date startDate;
		private Date endDate;
		private boolean retrieveAccountByDealershipCalled = false;
		private String salesRepCode;
		private char accountSubType;
		private char banStatus;
		private char addressType;
		private int ban;
		private String imsi;
		private String serialNumber;
		private int id;
		private Date fromDate;
		private Date toDate;


		@Override
		public AccountInfo retrieveAccountByBan(int ban) {

			AccountInfo accountInfo = new AccountInfo();
			accountInfo.setBanId(ban);
			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			} else if (ban == PREPAID_BAN) {
				accountInfo = new PrepaidConsumerAccountInfo();
				accountInfo.setBanId(ban);
				accountInfo.setAccountType(AccountInfo.ACCOUNT_TYPE_CONSUMER);
				accountInfo.setAccountSubType(AccountInfo.ACCOUNT_SUBTYPE_PCS_PREPAID);
			}
			return accountInfo;
		}

		@Override
		public List<AccountInfo> retrieveAccountsByBan(int[] banArray) {
			if (banArray == null) {
				return null;
			}

			List<AccountInfo> accountInfos = new ArrayList<AccountInfo>();
			for (int ban : banArray) {
				if (ban > 0) {
					AccountInfo ai = new AccountInfo();
					ai.setBanId(ban);
					accountInfos.add(ai);
				}
			}
			return accountInfos;
		}

		@Override
		public String retrieveAccountStatusByBan(int ban) {
			return String.valueOf(ban);
		}

		@Override
		public List<AccountInfo> retrieveAccountsByPostalCode(
				String lastName, String postalCode, int maximum) {
			this.lastName = lastName;
			this.postalCode = postalCode;
			this.maximum = maximum;

			return null;
		}

		@Override
		public List<AccountInfo> retrieveAccountsByPhoneNumber(
				String phoneNumber, boolean onlyLastAccount) {
			retrieveAccountsByPhoneNumberCalled = true;
			this.phoneNumber = phoneNumber;
			return null;
		}

		@Override
		public SearchResultsInfo retrieveAccountsByName(String nameType,
				String firstName, boolean firstNameExactMatch, String lastName,
				boolean lastNameExactMatch, char accountStatus,
				char accountType, String provinceCode, int brandId, int maximum) {
			this.nameType = nameType;
			this.firstName = firstName;
			this.firstNameExactMatch = firstNameExactMatch;
			this.lastName = lastName;
			this.lastNameExactMatch = lastNameExactMatch;
			this.accountStatus = accountStatus;
			this.accountType = accountType;
			this.provinceCode = provinceCode;
			this.brandId = brandId;
			this.maximum = maximum;

			return null;

		}

		@Override
		public AccountInfo retrieveLwAccountByBan(int pBan) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveBANByPhoneNumber(String pPhoneNumber) {
			this.phoneNumber = pPhoneNumber;
			return Integer.parseInt(this.phoneNumber);
		}

		@Override
		public List<AccountInfo> retrieveAccountsByDealership(
				char accountStatus, String dealerCode, Date startDate,
				Date endDate, int maximum) {
			retrieveAccountByDealershipCalled = true;
			this.accountStatus = accountStatus;
			this.dealerCode = dealerCode;
			this.startDate = startDate;
			this.endDate = endDate;
			this.maximum = maximum;

			return null;
		}

		@Override
		public List<AccountInfo> retrieveAccountsBySalesRep(
				char accountStatus, String dealerCode, String salesRepCode,
				Date startDate, Date endDate, int maximum) {
			this.accountStatus = accountStatus;
			this.dealerCode = dealerCode;
			this.salesRepCode = salesRepCode;
			this.startDate = startDate;
			this.endDate = endDate;
			this.maximum = maximum;
			return null;
		}

		@Override
		public int[] retrieveBanIds(char accountType, char accountSubType,
				char banStatus, int maximum) {
			this.accountType = accountType;
			this.accountSubType = accountSubType;
			this.banStatus = banStatus;
			this.maximum = maximum;
			return null;
		}

		@Override
		public int[] retrieveBanIdByAddressType(char accountType,
				char accountSubType, char banStatus, char addressType,
				int maximum) {
			this.accountType = accountType;
			this.accountSubType = accountSubType;
			this.banStatus = banStatus;
			this.addressType = addressType;
			this.maximum = maximum;

			return null;
		}

		@Override
		public BillParametersInfo retrieveBillParamsInfo(int banId) {
			this.ban = banId;
			return null;
		}

		@Override
		public int retrieveBanByImsi(String imsi) {
			this.imsi = imsi;
			try {
				return Integer.valueOf(imsi);
			} catch (Throwable t) {
				return 0;
			}
		}

		@Override
		public List<AccountInfo> retrieveAccountsBySerialNumber(
				String serialNumber) {
			this.serialNumber = serialNumber;
			return null;
		}
		@Override
		public int getClientAccountId(int ban) {
			this.ban=ban;
			return 0;
		}

		@Override
		public String retrieveCorporateName(int id) {
			this.id=id;
			return null;
		}
		public int getBan() {
			return ban;
		}

		@Override
		public String getPaperBillSupressionAtActivationInd(int pBan) {			
			return AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN;
		}

		@Override
		public BusinessCreditIdentityInfo[] retrieveBusinessList(int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public ConsumerNameInfo[] retrieveAuthorizedNames(int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public List<StatusChangeHistoryInfo> retrieveStatusChangeHistory(
				int ban, Date fromDate, Date toDate) {
			this.ban = ban;
			this.fromDate = fromDate;
			this.toDate = toDate;
			return null;
		}

		@Override
		public ContactDetailInfo getCustomerContactInfo(int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public PersonalCreditInfo retrievePersonalCreditInformation(int ban)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BusinessCreditInfo retrieveBusinessCreditInformation(int ban)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public TestPointResultInfo testKnowbilityDataSource() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByBanRollback(int ban) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void retrieveAccountExtendProperties(AccountInfo accountInfo) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public SubscriberCountInfo retrieveSubscriberCounts(int ban,
				char accountType, char accountSubType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveLwAccountByBanRollback(int ban) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveBANBySeatNumber(String seatNumber) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List<AccountInfo> retrieveLastAccountsBySeatNumber(
				String seatNumber) {
			// TODO Auto-generated method stub
			return null;
		}	
	}

	private static class TestMemoDao implements MemoDao{
		MemoInfo mi;

		@Override
		public List<MemoInfo> retrieveMemos(int Ban, int Count)  {

			if (Ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (Count <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"Count","Count cannot be 0 or less than 0");
			}else {
				mi = new MemoInfo();
				mi.setBanId(Ban);
				mi.setMemoType("0001");
				ArrayList<MemoInfo> list = new ArrayList<MemoInfo>();
				list.add(mi);
				return list;
			}			
		}

		@Override
		public List<MemoInfo> retrieveMemos(MemoCriteriaInfo MemoCriteria){


			try {
				BanValidator.validate(MemoCriteria.getBanId());
			} catch (ApplicationException e) {
				throw new org.springframework.core.ConstantException(this.getClass().getName(), "BanId in MemoCriteria", e.getErrorMessage());
			}


			mi = new MemoInfo();
			mi.setBanId(MemoCriteria.getBanId());
			mi.setSubscriberId(MemoCriteria.getSubscriberId());
			mi.setMemoType("0001");
			ArrayList<MemoInfo> list = new ArrayList<MemoInfo>();
			list.add(mi);
			return list;
		}

		@Override
		public MemoInfo retrieveLastMemo(int Ban, String MemoType) {

			if (Ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (MemoType == null || MemoType.equals("") ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"MemoType","MemoType cannot be empty or null");
			}else {
				mi = new MemoInfo();
				mi.setBanId(Ban);
				mi.setMemoType(MemoType);
				mi.setText("Test");
				return mi;
			}
		}

	}
	private static class TestInvoiceDao implements InvoiceDao{
		private int ban;
		private int billSeqNo;
		private String phoneNumber;
		private String subscriptionType;
		int clientID;

		@Override
		public BillNotificationContactInfo getLastEBillNotificationSent(
				int ban) {
			this.ban=ban;
			return null;
		}

		@Override
		public EBillRegistrationReminderInfo getLastEBillRegistrationReminderSent(
				int ban) {
			this.ban=ban;
			return null;
		}

		@Override
		public List<BillNotificationHistoryRecord> getBillNotificationHistory(
				int ban, String subscriptionType) {
			this.ban=ban;
			this.subscriptionType=subscriptionType;
			return null;
		}
		@Override
		public List<SubscriberInvoiceDetailInfo> retrieveSubscriberInvoiceDetails(
				int banId, int billSeqNo) {

			if (banId == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else{
				this.ban=banId;
				this.billSeqNo=billSeqNo;
			}
			return null;
		}


		@Override
		public void expireBillNotificationDetails(int ban) {
			this.ban=ban;

		}
		public int getBan() {
			return ban;
		}
		public int getBillSeqNo() {
			return billSeqNo;
		}

		@Override
		public List<ChargeInfo> retrieveBilledCharges(int ban,
				int pBillSeqNo, String pPhoneNumber, Date from, Date to) {
			this.ban=ban;
			this.phoneNumber=pPhoneNumber;
			return null;
		}

		@Override
		public boolean isEBillRegistrationReminderExist(int ban) {
			this.ban=ban;
			return false;
		}

		@Override
		public boolean hasEPostSubscription(int ban) {
			this.ban=ban;
			return false;
		}

		@Override
		public InvoicePropertiesInfo getInvoiceProperties(int ban) {
			this.ban=ban;
			return null;
		}

		@Override
		public List<InvoiceHistoryInfo> retrieveInvoiceHistory(int ban,
				Date fromDate, Date toDate) {
			this.ban=ban;
			return null;
		}

		@Override
		public List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPost(
				int ban, int clientID) {
			this.ban=ban;
			this.clientID=clientID;
			return null;
		}

		@Override
		public List<BillNotificationContactInfo> retrieveBillNotificationContactsHasEPostFalse(
				int ban, int clientID) {
			this.ban=ban;
			this.clientID=clientID;
			return null;
		}	
	}

	private static class TestFollowUpDao implements FollowUpDao{

		FollowUpTextInfo fti;
		FollowUpInfo fi1;
		FollowUpInfo fi2;
		FollowUpInfo fi3;
		FollowUpInfo fi4;
		FollowUpStatisticsInfo fsi;

		@Override
		public List<FollowUpTextInfo> retrieveFollowUpAdditionalText(
				int ban, int followUpId) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (followUpId <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"followUpId","followUpId cannot be 0 or less than 0");
			}else {
				fti = new FollowUpTextInfo();
				fti.setOperatorId("12114");
			}
			return null;
		}

		@Override
		public List<FollowUpInfo> retrieveFollowUpHistory(int followUpId) {

			if (followUpId <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"followUpId","followUpId cannot be 0 or less than 0");
			}else {
				fi1=new FollowUpInfo();
				fi1.setBanId(1234);fi1.setSubscriberId("5678");
			}
			return null;
		}

		@Override
		public FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int ban,
				int followUpID) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (followUpID <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"followUpId","followUpId cannot be 0 or less than 0");
			}else {
				fi2=new FollowUpInfo();
				fi2.setBanId(1234);fi2.setAssignedToWorkPositionId("RS_200");
			}
			return null;
		}

		@Override
		public List<FollowUpInfo> retrieveFollowUps(
				FollowUpCriteriaInfo followUpCriteria) {

			if (followUpCriteria.getWorkPositionId()==null || followUpCriteria.getWorkPositionId().equals("") ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"WorkPositionId","WorkPositionId is a mandatory value to be set in FollowUpCriteriaInfo");
			}else{
				fi3=new FollowUpInfo();
				fi3.setBanId(123456);
			}

			return null;
		}

		@Override
		public List<FollowUpInfo> retrieveFollowUps(int ban, int Count) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (Count <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"Count","Count cannot be 0 or less than 0");
			}else {
				fi4 = new FollowUpInfo();
				fi4.setStatus('o');
			}
			return null;
		}

		@Override
		public FollowUpStatisticsInfo retrieveFollowUpStatistics(int ban) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else {
				fsi=new FollowUpStatisticsInfo();		
				fsi.setHasDueFollowUps(true);
			}
			return null;
		}

		@Override
		public int retrieveLastFollowUpIDByBanFollowUpType(int ban,
				String followUpType) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if (followUpType==null || followUpType.equals("") ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"followUpType","followUpType cannot be null or empty");
			}

			int followUpId=10;

			return followUpId;
		}

	}

	private static class TestFleetDao implements FleetDao {
		private int ban;
		@Override
		public Collection<FleetInfo> retrieveFleetsByBan(int ban) {

			if (ban == EMPTY_BAN) {
				return new ArrayList<FleetInfo>();
			}
			Collection<FleetInfo> fleets = new ArrayList<FleetInfo>();

			for (int i = 0; i < ban; i++) {
				FleetInfo fleet = new FleetInfo();
				fleet.getIdentity0().setUrbanId(ban);
				fleet.getIdentity0().setFleetId(ban);
				fleets.add(fleet);
			}

			return fleets;
		}

		@Override
		public int retrieveAssociatedAccountsCount(int urbanId, int fleetId) {
			return urbanId + fleetId;
		}

		@Override
		public int retrieveAssociatedTalkGroupsCount(
				FleetIdentityInfo pFleeIdentity, int ban) {
			this.ban=ban;
			return 0;
		}

		@Override
		public int retrieveAttachedSubscribersCountForTalkGroup(int urbanID,
				int fleetID, int talkGroupId, int ban) {
			return urbanID*fleetID;
		}

		@Override
		public List<TalkGroupInfo> retrieveTalkGroupsByBan(int ban) {
			this.ban=ban;
			return null;
		}

	}

	private static class TestPaymentDao implements PaymentDao {
		private int ban;
		ArrayList<PaymentActivityInfo> list =null;
		ArrayList<PaymentMethodChangeHistoryInfo> list1=null;

		@Override
		public PaymentHistoryInfo retrieveLastPaymentActivity(int ban) {		
			if (ban == EMPTY_BAN) {
				return null;
			}
			return new PaymentHistoryInfo();
		}

		@Override
		public List<PaymentHistoryInfo> retrievePaymentHistory(int ban,
				Date pFromDate, Date pToDate) {
			this.ban=ban;
			return null;

		}

		@Override
		public List<RefundHistoryInfo> retrieveRefundHistory(int ban,
				Date fromDate, Date toDate) {
			this.ban=ban;
			return null;

		}

		public int getBan() {
			return ban;
		}

		@Override
		public List<PaymentActivityInfo> retrievePaymentActivities(
				int banId, int paymentSeqNo) {
			list= new ArrayList<PaymentActivityInfo>();
			PaymentActivityInfo  pinfo =new PaymentActivityInfo();
			pinfo.setActivityCode("asdf");
			pinfo.setAmount(2.3);
			list.add(pinfo);
			return null;
		}

		@Override
		public List<PaymentMethodChangeHistoryInfo> retrievePaymentMethodChangeHistory(
				int ban, Date fromDate, Date toDate) {
			list1= new ArrayList<PaymentMethodChangeHistoryInfo>();
			PaymentMethodChangeHistoryInfo  pinfo =new PaymentMethodChangeHistoryInfo();
			pinfo.setBankAccountNumber("1234");
			pinfo.setBankCode("ICI");
			list1.add(pinfo);
			return null;
		}
	}	

	private static class TestEquipmentDao implements EquipmentDao {

		static String SERIAL_TO_USIM_TO_IMSI = "SERIAL_TO_USIM_TO_IMSI";
		private boolean getUsimBySerialNumberCalled = false;
		private boolean getImsisByUsimCalled = false;

		@Override
		public String getUsimBySerialNumber(String serialNumber) {
			this.getUsimBySerialNumberCalled = true;
			if (serialNumber == SERIAL_TO_USIM_TO_IMSI) {
				return serialNumber;
			} else {
				return null;
			}
		}

		@Override
		public String[] getImsisByUsim(String usimId) {
			this.getImsisByUsimCalled = true;
			if (usimId == SERIAL_TO_USIM_TO_IMSI) {
				return new String[]{"1", "2"};
			} else {
				return new String[0];
			}
		}		
	}

	private static class TestAddressDao implements AddressDao {

		private int ban;

		@Override
		public AddressInfo retrieveAlternateAddressByBan(int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public AddressHistoryInfo[] retrieveAddressHistory(int pBan,
				Date pFromDate, Date pToDate) {
			return new AddressHistoryInfo[0];
		}		

	}

	private static class TestDepositDao implements DepositDao {

		private int ban;
		private Date fromDate;
		private Date toDate;

		@Override
		public List<DepositHistoryInfo> retrieveDepositHistory(int ban,
				Date fromDate, Date toDate) {
			this.ban = ban;
			this.fromDate = fromDate;
			this.toDate = toDate;
			return null;
		}

		@Override
		public List<DepositAssessedHistoryInfo> retrieveDepositAssessedHistoryList(
				int ban) {
			this.ban = ban;
			return null;
		}

		@Override
		public List<DepositAssessedHistoryInfo> retrieveOriginalDepositAssessedHistoryList(
				int ban) {
			this.ban = ban;
			return null;
		}

	}

	private static class TestAdjustmentDao implements AdjustmentDao {

		private CreditInfo ci;
		private CreditInfoHolder cih1=null;
		private CreditInfoHolder cih2=null;
		private int ban;
		private String[] chargeCodes=null;
		private String billState;
		private char level;
		private String subscriberId;
		private Date from;
		private Date to;
		private int maximum;
		private boolean billedCredits;
		private boolean unbilledCredits;

		@Override
		public List<CreditInfo> retrieveCreditByFollowUpId(int followUpId) {
			if (followUpId <= 0 ){
				throw  new org.springframework.core.ConstantException(this.getClass().getName(),"followUpId","followUpId cannot be 0 or less than 0");
			}else {
				ci=new CreditInfo();
				ci.setAmount(100.21);
				ci.setReasonCode("test");
			}
			return null;
		}


		@Override
		public CreditInfoHolder retrieveBilledCredits(int ban, Date fromDate,
				Date toDate, String billState, String knowbilityOperatorId,
				String reasonCode, char level, String subscriber, int maximum) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if(ban==1001){
				cih1=new CreditInfoHolder(new ArrayList<CreditInfo>(),false);
			}else{
				List<CreditInfo> ciList=new ArrayList<CreditInfo>();
				CreditInfo ci;
				ci=new CreditInfo();
				ci.setBan(789);
				ciList.add(ci);
				ci=new CreditInfo();
				ci.setBan(135);
				ciList.add(ci);
				cih1=new CreditInfoHolder(ciList,true);
			}
			this.billedCredits=true;
			return cih1;
		}


		@Override
		public CreditInfoHolder retrieveUnbilledCredits(int ban, Date fromDate,
				Date toDate, String billState, String knowbilityOperatorId,
				String reasonCode, char level, String subscriber, int maximum) {

			if (ban == EXCEPTION_BAN) {
				SQLException sqlException = new SQLException("", "", 20101);
				throw new UncategorizedSQLException("123", "123", sqlException);
			}else if(ban==1001){
				cih2=new CreditInfoHolder(new ArrayList<CreditInfo>(),false);
			}else{
				List<CreditInfo> ciList=new ArrayList<CreditInfo>();
				CreditInfo ci;
				ci=new CreditInfo();
				ci.setBan(123);
				ciList.add(ci);
				ci=new CreditInfo();
				ci.setBan(456);
				ciList.add(ci);
				cih2=new CreditInfoHolder(ciList,true);
			}
			this.unbilledCredits=true;
			return cih2;
		}	

		@Override
		public List<ChargeInfo> retrieveCharges(int ban, String[] chargeCodes,
				String billState, char level, String subscriberId, Date from,
				Date to, int maximum) {
			this.ban = ban;
			this.chargeCodes = chargeCodes;
			this.billState = billState;
			this.level = level;
			this.subscriberId = subscriberId;
			this.from = from;
			this.to = to;
			this.maximum = maximum;
			return null;
		}


		@Override
		public List<ChargeInfo> retrieveRelatedChargesForCredit(int pBan,
				double pChargeSeqNo) {			
			return null;
		}


		@Override
		public List<CreditInfo> retrieveRelatedCreditsForCharge(int pBan,
				double pChargeSeqNo) {
			return null;
		}


		@Override
		public SearchResultsInfo retrievePendingChargeHistory(int pBan,
				Date pFromDate, Date pToDate, char level, String pSubscriber,
				int maximum) {
			return null;
		}


	


		@Override
		public List<Double> retrieveAdjustedAmounts(int ban,
				String adjustmentReasonCode, String subscriberId,
				Date searchFromDate, Date searchToDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<CreditInfo> retrieveApprovedCreditByFollowUpId(int banId,
				int followUpId) {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public CreditInfo retrieveCreditById(int banId, int entSeqNo) {
			// TODO Auto-generated method stub
			return null;
		}


	}
	
	private static class TestContactDao implements  ContactDao{

		int ban;
		@Override
		public BillingPropertyInfo retrieveBillingInformation(
				int billingAccountNumber) {
			this.ban=billingAccountNumber;
			return new BillingPropertyInfo();
		}

		@Override
		public ContactPropertyInfo retrieveContactInformation(
				int billingAccountNumber) {
			this.ban=billingAccountNumber;
			return new ContactPropertyInfo();
		}
		
	}
}
