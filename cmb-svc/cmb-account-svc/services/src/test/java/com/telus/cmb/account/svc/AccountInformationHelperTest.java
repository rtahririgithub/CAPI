/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.svc;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.account.Account;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.MemoInfo;

/**
 * @author Pavel Simonovsky
 *
 */
@Test
@ContextConfiguration(locations = "classpath:application-context-test.xml")
//@ActiveProfiles("standalone")
@ActiveProfiles({ "remote", "local" })
//@ActiveProfiles({ "remote", "pt168" })
//@ActiveProfiles({ "remote", "dv103" })
public class AccountInformationHelperTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");

		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
	}

	@Autowired
	private AccountInformationHelper helper;

	//2017-08-01 Wireless Resiliency Project Test Cases Start
	@Test
	public void testGetAccountInfoByBAN() throws Exception {
		AccountInfo account = helper.retrieveAccountByBan(70820591, Account.ACCOUNT_LOAD_ALL);
		assertNotNull(account.getAccountType());
//		account = helper.retrieveAccountByBan(44133395);
//		assertNotNull(account.getAccountType());
//		account = helper.retrieveAccountByBan(44133793);
//		assertNotNull(account.getAccountType());
//		account = helper.retrieveAccountByBan(44137341);
//		assertNotNull(account.getAccountType());
//		account = helper.retrieveAccountByBan(70021023);
//		assertNotNull(account.getAccountType());
	}

	@Test
	public void testGetBilledCreditsEnhanced() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		SearchResultsInfo results = helper.retrieveCredits(70426502, fromDate, toDate, Account.BILL_STATE_BILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70393254, fromDate, toDate, Account.BILL_STATE_BILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70038356, fromDate, toDate, Account.BILL_STATE_BILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70024305, fromDate, toDate, Account.BILL_STATE_BILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(44137765, fromDate, toDate, Account.BILL_STATE_BILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
	}

	@Test
	public void testGetUnbilledCreditsEnhanced() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		SearchResultsInfo results = helper.retrieveCredits(70654987, fromDate, toDate, Account.BILL_STATE_UNBILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70654997, fromDate, toDate, Account.BILL_STATE_UNBILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70655001, fromDate, toDate, Account.BILL_STATE_UNBILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70655003, fromDate, toDate, Account.BILL_STATE_UNBILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrieveCredits(70655021, fromDate, toDate, Account.BILL_STATE_UNBILLED, '0', "", 10);
		Assert.assertTrue(results.getItems().length > 0);
	}

	@Test
	public void testGetUnBilledCreditByFollowUpId() throws Exception {
		List<CreditInfo> results = helper.retrieveCreditByFollowUpId(7024999);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveCreditByFollowUpId(7025000);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveCreditByFollowUpId(7025100);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveCreditByFollowUpId(7025801);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveCreditByFollowUpId(7025907);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetRelatedChargesForCredit() throws Exception {
		List<ChargeInfo> results = helper.retrieveRelatedChargesForCredit(70782967, 2096650845);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedChargesForCredit(70798886, 2096650847);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedChargesForCredit(70798910, 2096650854);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedChargesForCredit(70783598, 2096650860);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedChargesForCredit(70776962, 2096650865);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetRelatedCreditsForCharge() throws Exception {
		List<ChargeInfo> results = helper.retrieveRelatedCreditsForCharge(70783450, 2096650817);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedCreditsForCharge(70782694, 2096650820);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedCreditsForCharge(70782967, 2096650262);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedCreditsForCharge(70783598, 2096650858);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRelatedChargesForCredit(70798815, 2096650781);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetPendingChargesEnhanced() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		char level = '*'; //CHARGE_LEVEL_ALL='*'; CHARGE_LEVEL_ACCOUNT = 'B'; CHARGE_LEVEL_SUBSCRIBER = 'C'; 
		SearchResultsInfo results = helper.retrievePendingChargeHistory(70800425, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70800452, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70770521, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70800458, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70796820, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
	}

	@Test
	public void testGetCollectionHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		CollectionHistoryInfo[] results = helper.retrieveCollectionHistoryInfo(70629483, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveCollectionHistoryInfo(70359088, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveCollectionHistoryInfo(70027069, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveCollectionHistoryInfo(197806, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveCollectionHistoryInfo(750714, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetLastCreditCheckResultByBan() throws Exception {
		CreditCheckResultInfo result = helper.retrieveLastCreditCheckResultByBan(70800488, "C");
		Assert.assertTrue(result != null);
		result = helper.retrieveLastCreditCheckResultByBan(70770521, "C");
		Assert.assertTrue(result != null);
		result = helper.retrieveLastCreditCheckResultByBan(70800425, "C");
		Assert.assertTrue(result != null);
		result = helper.retrieveLastCreditCheckResultByBan(70800361, "C");
		Assert.assertTrue(result != null);
		result = helper.retrieveLastCreditCheckResultByBan(70800293, "C");
		Assert.assertTrue(result != null);
	}

	@Test
	public void testGetAccountDepositHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		DepositHistoryInfo[] results = helper.retrieveDepositHistory(44128455, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositHistory(44131344, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositHistory(7102745, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositHistory(700954, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositHistory(70024949, fromDate, toDate);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetAccountDepAssessedHistory() throws Exception {
		DepositAssessedHistoryInfo[] results = helper.retrieveDepositAssessedHistoryList(70778530);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70774290);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70778498);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70777819);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70774290);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetOrgAccDepAssessedHistory() throws Exception {
		DepositAssessedHistoryInfo[] results = helper.retrieveOriginalDepositAssessedHistoryList(70800490);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70800478);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70793050);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70800434);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveDepositAssessedHistoryList(70761854);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetFleetsByBan() throws Exception {
		FleetInfo[] results = helper.retrieveFleetsByBan(70107493);
		Assert.assertTrue(results.length > 0);
		for (FleetInfo fleet: results) {
			System.out.println(fleet);
		}
//		results = helper.retrieveFleetsByBan(70526131);
//		Assert.assertTrue(results.length > 0);
//		results = helper.retrieveFleetsByBan(70526141);
//		Assert.assertTrue(results.length > 0);
//		results = helper.retrieveFleetsByBan(70526148);
//		Assert.assertTrue(results.length > 0);
//		results = helper.retrieveFleetsByBan(70526151);
//		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testRetrieveSubCharge() throws Exception {
		List<SubscriberInvoiceDetailInfo> results = helper.retrieveSubscriberInvoiceDetails(194587, 5);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberInvoiceDetails(194587, 59);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberInvoiceDetails(197806, 11);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberInvoiceDetails(254977, 5);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberInvoiceDetails(750714, 30);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetBilledCharges() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		List<ChargeInfo> results = helper.retrieveBilledCharges(70800890, 1, "4031652377", fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveBilledCharges(70800847, 1, "5141657242", fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveBilledCharges(70800891, 1, "6041662811", fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveBilledCharges(70800743, 1, "4031725179", fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveBilledCharges(70800731, 1, "4031725291", fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetInvoiceHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		char level = '*'; //CHARGE_LEVEL_ALL='*'; CHARGE_LEVEL_ACCOUNT = 'B'; CHARGE_LEVEL_SUBSCRIBER = 'C';		
		SearchResultsInfo results = helper.retrievePendingChargeHistory(70574614, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70574338, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70547918, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70574482, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
		results = helper.retrievePendingChargeHistory(70574451, fromDate, toDate, level, null, 10);
		Assert.assertTrue(results.getItems().length > 0);
	}

	@Test
	public void testGetMemos() throws Exception {
		List<MemoInfo> results = helper.retrieveMemos(18078654, 10);
		Assert.assertTrue(results.size() > 0);
		for (MemoInfo memo: results) {
			System.out.println(memo);
		}		
//		results = helper.retrieveMemos(70728707, 10);
//		Assert.assertTrue(results.size() > 0);
//		results = helper.retrieveMemos(70688955, 10);
//		Assert.assertTrue(results.size() > 0);
//		results = helper.retrieveMemos(194587, 10);
//		Assert.assertTrue(results.size() > 0);
//		results = helper.retrieveMemos(254977, 10);
//		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetMemosByCriteriaEnhanced() throws Exception {
		MemoCriteriaInfo criteria = new MemoCriteriaInfo();
		criteria.setBanId(18078654);
		List<MemoInfo> results = helper.retrieveMemos(criteria);
		Assert.assertTrue(results.size() > 0);
//		criteria.setBanId(70728707);
//		results = helper.retrieveMemos(70728707, 10);
//		Assert.assertTrue(results.size() > 0);
//		criteria.setBanId(70688955);
//		results = helper.retrieveMemos(70688955, 10);
//		Assert.assertTrue(results.size() > 0);
//		criteria.setBanId(194587);
//		results = helper.retrieveMemos(194587, 10);
//		Assert.assertTrue(results.size() > 0);
//		criteria.setBanId(254977);
//		results = helper.retrieveMemos(254977, 10);
//		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetLastMemo() throws Exception {
		MemoInfo result = helper.retrieveLastMemo(18078654, "FYI");
		System.out.println(result.toString());
//		result = helper.retrieveLastMemo(70728707, "AUTH");
//		Assert.assertTrue(!result.getText().isEmpty());
//		result = helper.retrieveLastMemo(70688955, "ADON");
//		Assert.assertTrue(!result.getText().isEmpty());
//		result = helper.retrieveLastMemo(194587, "3050");
//		Assert.assertTrue(!result.getText().isEmpty());
//		result = helper.retrieveLastMemo(254977, "CLWB");
//		Assert.assertTrue(!result.getText().isEmpty());
	}

	@Test
	public void testGetPaymentHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		List<PaymentHistoryInfo> results = helper.retrievePaymentHistory(70800891, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentHistory(70800890, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentHistory(70800743, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentHistory(70800731, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentHistory(70800514, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetPaymentActivities() throws Exception {
		List<PaymentActivityInfo> results = helper.retrievePaymentActivities(70800891, 89905203);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentActivities(70800890, 89905204);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentActivities(70800743, 89905200);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentActivities(70800731, 89905199);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentActivities(70800514, 89905198);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetPaymentMethodChangeHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		List<PaymentMethodChangeHistoryInfo> results = helper.retrievePaymentMethodChangeHistory(70799774, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentMethodChangeHistory(70799184, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentMethodChangeHistory(70798717, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentMethodChangeHistory(70798699, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePaymentMethodChangeHistory(70798124, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetRefundHistory() throws Exception {
		Date fromDate = new Date(0);
		Date toDate = new Date();
		List<RefundHistoryInfo> results = helper.retrieveRefundHistory(70783844, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRefundHistory(70759239, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRefundHistory(70759221, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRefundHistory(70759200, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveRefundHistory(70759186, fromDate, toDate);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetPhoneNumbersByBan() throws Exception {
		Map<String, String> results = helper.retrievePhoneNumbersForBAN(70779333);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePhoneNumbersForBAN(70779332);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePhoneNumbersForBAN(70779331);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePhoneNumbersForBAN(70687090);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePhoneNumbersForBAN(70680496);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetNumberAttachedSubscribers() throws Exception {
		FleetIdentityInfo fleetIdentity = new FleetIdentityInfo(403, 131334);
		int count = helper.retrieveAttachedSubscribersCount(70641294, fleetIdentity);
		Assert.assertTrue(count > 0);
		fleetIdentity = new FleetIdentityInfo(905, 131384);
		count = helper.retrieveAttachedSubscribersCount(70602022, fleetIdentity);
		Assert.assertTrue(count > 0);
		fleetIdentity = new FleetIdentityInfo(905, 131382);
		count = helper.retrieveAttachedSubscribersCount(70601820, fleetIdentity);
		Assert.assertTrue(count > 0);
		fleetIdentity = new FleetIdentityInfo(905, 131269);
		count = helper.retrieveAttachedSubscribersCount(70535026, fleetIdentity);
		Assert.assertTrue(count > 0);
		fleetIdentity = new FleetIdentityInfo(905, 133006);
		count = helper.retrieveAttachedSubscribersCount(6002219, fleetIdentity);
		Assert.assertTrue(count > 0);
	}

	@Test
	public void testGetProductSubscriberLists() throws Exception {
		ProductSubscriberListInfo[] results = helper.retrieveProductSubscriberLists(70800987);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveProductSubscriberLists(70800892);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveProductSubscriberLists(70800891);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveProductSubscriberLists(70800890);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveProductSubscriberLists(70800849);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetBanSubIdsBySubStatus() throws Exception {
		List<String> results = helper.retrieveSubscriberIdsByStatus(70800987, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberIdsByStatus(70800892, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberIdsByStatus(70800891, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberIdsByStatus(70800890, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberIdsByStatus(70800849, 'A', 10);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetBanSubPhoneNosBySubStatus() throws Exception {
		List<String> results = helper.retrieveSubscriberPhoneNumbersByStatus(70800987, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberPhoneNumbersByStatus(70800892, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberPhoneNumbersByStatus(70800891, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberPhoneNumbersByStatus(70800890, 'A', 10);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveSubscriberPhoneNumbersByStatus(70800849, 'A', 10);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testIsFeatureCategoryExist() throws Exception {
		boolean result = helper.isFeatureCategoryExistOnSubscribers(70801031, "TM");
		Assert.assertTrue(result);
		result = helper.isFeatureCategoryExistOnSubscribers(70801031, "RUM");
		Assert.assertTrue(result);
		result = helper.isFeatureCategoryExistOnSubscribers(70800987, "WW");
		Assert.assertTrue(result);
		result = helper.isFeatureCategoryExistOnSubscribers(70800891, "EW");
		Assert.assertTrue(result);
		result = helper.isFeatureCategoryExistOnSubscribers(70800891, "911");
		Assert.assertTrue(result);
	}

	@Test
	public void testGetHotlinedPhoneNoByBan() throws Exception {
		String result = helper.retrieveHotlinedSubscriberPhoneNumber(70798647);
		Assert.assertTrue(!result.isEmpty());
		result = helper.retrieveHotlinedSubscriberPhoneNumber(70785545);
		Assert.assertTrue(!result.isEmpty());
		result = helper.retrieveHotlinedSubscriberPhoneNumber(70785538);
		Assert.assertTrue(!result.isEmpty());
		result = helper.retrieveHotlinedSubscriberPhoneNumber(70785360);
		Assert.assertTrue(!result.isEmpty());
		result = helper.retrieveHotlinedSubscriberPhoneNumber(70785307);
		Assert.assertTrue(!result.isEmpty());
	}

	@Test
	public void testGetPCSNetworkCountByBAN() throws Exception {
		HashMap<String, Integer> results = helper.retrievePCSNetworkCountByBan(70801031);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePCSNetworkCountByBan(70800987);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePCSNetworkCountByBan(70800892);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePCSNetworkCountByBan(70800891);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePCSNetworkCountByBan(70800890);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetPoolingSubscriberCounts() throws Exception {
		List<PoolingPricePlanSubscriberCountInfo> results = helper.retrievePoolingPricePlanSubscriberCounts(70570807);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePoolingPricePlanSubscriberCounts(70570803);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePoolingPricePlanSubscriberCounts(70570802);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePoolingPricePlanSubscriberCounts(70570790);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrievePoolingPricePlanSubscriberCounts(70570789);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetServiceSubscriberCounts() throws Exception {
		String[] serviceCodes = { "S9110    " };
		List<ServiceSubscriberCount> results = helper.retrieveServiceSubscriberCounts(70800760, serviceCodes, true);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveServiceSubscriberCounts(70800098, serviceCodes, true);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveServiceSubscriberCounts(70799368, serviceCodes, true);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveServiceSubscriberCounts(70799263, serviceCodes, true);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveServiceSubscriberCounts(70798981, serviceCodes, true);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetMinutePoolingSubscribers() throws Exception {
		List<PricePlanSubscriberCount> results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70698899, null);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70698069, null);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70026110, null);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70567134, null);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70658285, null);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetMinutePoolingSubsByCoverage() throws Exception {
		String[] coverageTypes = { "O", "H" };
		List<PricePlanSubscriberCount> results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70779350, coverageTypes);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70779661, coverageTypes);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70788595, coverageTypes);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70794407, coverageTypes);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(70778858, coverageTypes);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetDollarPoolingSubCounts() throws Exception {
		List<PricePlanSubscriberCountInfo> results = helper.retrieveDollarPoolingPricePlanSubscriberCounts(70579688, "C");
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveDollarPoolingPricePlanSubscriberCounts(70550525, "C");
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveDollarPoolingPricePlanSubscriberCounts(70579686, "C");
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveDollarPoolingPricePlanSubscriberCounts(70579706, "C");
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveDollarPoolingPricePlanSubscriberCounts(70580727, "C");
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetShareablePricePlanSubscriberCount() throws Exception {
		List<PricePlanSubscriberCountInfo> results = helper.retrieveShareablePricePlanSubscriberCount(70576908);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveShareablePricePlanSubscriberCount(70435376);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveShareablePricePlanSubscriberCount(70437633);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveShareablePricePlanSubscriberCount(70486986);
		Assert.assertTrue(results.size() > 0);
		results = helper.retrieveShareablePricePlanSubscriberCount(70509986);
		Assert.assertTrue(results.size() > 0);
	}

	@Test
	public void testGetSubscribersByServiceFamily() throws Exception {
		Date today = new Date();
		String[] results = helper.retrieveSubscriberIdsByServiceFamily(70801031, "W", today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberIdsByServiceFamily(70800987, "W", today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberIdsByServiceFamily(70800892, "W", today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberIdsByServiceFamily(70800891, "W", today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberIdsByServiceFamily(70800890, "W", today);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetSubscribersBySharingGroups() throws Exception {
		Date today = new Date();
		String[] dataSharingGroupCodes = { "CAD_DATA_2013" };
		SubscribersByDataSharingGroupResultInfo[] results = helper.retrieveSubscribersByDataSharingGroupCodes(70739201, dataSharingGroupCodes, today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscribersByDataSharingGroupCodes(70726165, dataSharingGroupCodes, today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscribersByDataSharingGroupCodes(70719151, dataSharingGroupCodes, today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscribersByDataSharingGroupCodes(70705451, dataSharingGroupCodes, today);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscribersByDataSharingGroupCodes(70704776, dataSharingGroupCodes, today);
		Assert.assertTrue(results.length > 0);
	}

	@Test
	public void testGetSubscriberDataSharingInfo() throws Exception {
		Date today = new Date();
		String[] dataSharingGroupCodes = { "CAD_DATA_2013" };
		SubscriberDataSharingDetailInfo[] results = helper.retrieveSubscriberDataSharingInfoList(70739201, dataSharingGroupCodes);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberDataSharingInfoList(70726165, dataSharingGroupCodes);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberDataSharingInfoList(70719151, dataSharingGroupCodes);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberDataSharingInfoList(70739201, dataSharingGroupCodes);
		Assert.assertTrue(results.length > 0);
		results = helper.retrieveSubscriberDataSharingInfoList(70704776, dataSharingGroupCodes);
		Assert.assertTrue(results.length > 0);
	}

	//2017-08-01 Wireless Resiliency Project Test Cases End
	@Test
	public void retrieveHwAccountByBan() throws Exception {
		AccountInfo account = helper.retrieveHwAccountByBan(70773268); // 70771335 70771638 70771721 70771335 70761464 70773268
		System.out.println(account);
	}

	@Test
	public void retrieveAccountByPhoneNumber() throws Exception {
		AccountInfo account = helper.retrieveAccountByPhoneNumber("5871250855");
		System.out.println(account);
	}

	@Test
	public void retrieveHwAccountByPhoneNumber() throws Exception {
		AccountInfo account = helper.retrieveHwAccountByPhoneNumber("5871250855", new PhoneNumberSearchOptionInfo());
		System.out.println(account);
	}

	@Test
	public void checkInternationalServiceEligibility() throws Exception {
		InternationalServiceEligibilityCheckResult result = helper.checkInternationalServiceEligibility(70719126);
		System.out.println(result);
	}

	@Test
	public void retrieveSubscriberDataSharingInfoList() throws Exception {

		String[] sharingCodes = { "CAD_DATA_2013", "CAD_CORP_DATA" };
		SubscriberDataSharingDetailInfo[] dataSharingInfo = helper.retrieveSubscriberDataSharingInfoList(70691218, sharingCodes);
		for (SubscriberDataSharingDetailInfo info : dataSharingInfo) {
			System.out.println(info.toString());
		}
	}

	@Test
	public void getLastMemo() throws Exception {
		MemoInfo memo = helper.retrieveLastMemo(3720, "FYI ");
		System.out.println(memo.getBanId());
	}

	@Test
	public void retrieveLastCreditCheckResultByBan() throws Exception {
		CreditCheckResultInfo info = helper.retrieveLastCreditCheckResultByBan(70760051, "C");
		System.out.println(info.toString());
	}

	@Test
	public void retrieveDepositsByBan() throws Exception {

		CreditCheckResultDeposit[] deposits = helper.retrieveDepositsByBan(70789012);
		if (ArrayUtils.isNotEmpty(deposits)) {
			for (CreditCheckResultDeposit info : deposits) {
				System.out.println(info.toString());
			}
		}
	}

	@Test
	public void retrieveAccountListByImsi() throws Exception {
		System.out.println("start retrieveAccountListByImsi");
		List<AccountInfo> accounts = helper.retrieveAccountListByImsi("2140300004565101");
		for (AccountInfo accountInfo : accounts) {
			System.out.println("ban "+ accountInfo.getBanId());
		}
		System.out.println("end retrieveAccountListByImsi");

	}

}