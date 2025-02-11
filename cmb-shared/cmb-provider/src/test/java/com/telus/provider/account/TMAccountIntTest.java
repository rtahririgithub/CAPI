package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;
import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AddressHistory;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BankAccount;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.Cheque;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.DepositAssessedHistory;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.FollowUpStatistics;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PaymentMethodChangeHistory;
import com.telus.api.account.PaymentNotification;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.SearchResults;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.StatusChangeHistory;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.CreditCheckDepositChangeReason;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;

public class TMAccountIntTest extends BaseTest {

	private TMAccountManager accountManager;
	String avalonErrorCode = "AV100051";
	int banToTest = 12474;

	static {
		// tupD3();
		//setupEASECA_QA();
		setupCHNLECA_PT168();
		//setupEASECA_PT168();
		// setupCHNLECA_STG();
		// setupCHNLECA_STG();
		
		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		
		System.setProperty("cmb.services.AccountManagerEJBRemote.url","t3://localhost:7001");
		System.setProperty("cmb.services.AccountHelperEJBRemote.url","t3://localhost:7001");
		/* 
		 * System.setProperty("cmb.services.ReferenceDataFacade.url",
		 * "t3://localhost:7001");
		 * System.setProperty("cmb.services.ReferenceDataHelper.url",
		 * "t3://localhost:7001");
		 * System.setProperty("cmb.services.ReferenceDataFacade.usedByProvider",
		 * "true");
		 * System.setProperty("cmb.services.ReferenceDataHelper.usedByProvider",
		 * "true");
		 * 
		 * System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider"
		 * , "false");
		 * System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider"
		 * , "false");
		 * System.setProperty("cmb.services.AccountInformationHelper.usedByProvider"
		 * , "false");
		 * 
		 * System.setProperty("cmb.services.AccountManagerEJBRemote.usedByProvider"
		 * , "true");
		 * System.setProperty("cmb.services.AccountHelperEJBRemote.usedByProvider"
		 * , "true");
		 */
	}

	public TMAccountIntTest(String name) throws Throwable {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}

	private Date getDateInput(int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	public void testPayBill_ApplicationException() throws TelusAPIException {
		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(805938);
			double amount = 100.00;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000000229", "422222", "2222");
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
		} catch (TelusAPIException e) {
			assertEquals(
					": PolicyException was received from WPS service (errorCode=PYMT80003, message=Validation Failed.  Invalid value [0] for expiry year.). Please contact the WPS team for support.",
					e.getMessage());
		}
	}

	public void testPayBill_InvalidCreditCardException()
			throws TelusAPIException {
		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(805938);
			double amount = 100.00;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000000229", "422222", "2222");
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
		} catch (InvalidCreditCardException cc) {
			assertEquals(
					"PolicyException was received from WPS service (errorCode=PYMT80003, message=Validation Failed.  Invalid value [0] for expiry year.). Please contact the WPS team for support.",
					cc.getReasonText());
			assertEquals("WPS", cc.getReason());
		}
	}

	public void testPayBill_WPSPolicyException() throws TelusAPIException {
		// Test ECA method, set as
		// System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider",
		// "false");
		try {
			Account account = accountManager.findAccountByBAN(805938);
			double amount = 100.00;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000000229", "422222", "2222");
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
		} catch (TelusAPIException tae) {

			tae.printStackTrace();
			assertEquals(
					": PolicyException was received from WPS service (errorCode=PYMT80003, message=Validation Failed.  Invalid value [0] for expiry year.). Please contact the WPS team for support.",
					tae.getMessage());
		}
	}

	public void testPayDeposit_InvalidCreditCardException()
			throws TelusAPIException {
		// Test Account Management EJB method
		try {
			Account account = accountManager.findAccountByBAN(805938);
			double amount = 100.00;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000000229", "422222", "2222");
			AuditHeader auditHeader = new AuditHeaderInfo();
			String businessRole = BusinessRole.BUSINESS_ROLE_CORPORATE;

			account.payDeposit(account.getSubscriberCount(), amount,
					creditCard, businessRole, auditHeader);
		} catch (InvalidCreditCardException cc) {
			assertEquals(
					"PolicyException was received from WPS service (errorCode=PYMT80003, message=Validation Failed.  Invalid value [0] for expiry year.). Please contact the WPS team for support.",
					cc.getReasonText());
			assertEquals("WPS", cc.getReason());
		}
	}

	// public void testPayDeposit_WPSPolicyException() throws TelusAPIException{
	// //Test ECA method, set as
	// System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider",
	// "false");
	// try{
	// Account account = accountManager.findAccountByBAN(8);
	// double amount=100.00;
	// CreditCard creditCard= accountManager.newCreditCard(account);
	// creditCard.setToken("100000000000000006404", "490000", "0003");
	// AuditHeader auditHeader = new AuditHeaderInfo();
	// String businessRole= BusinessRole.BUSINESS_ROLE_CORPORATE;
	//
	// account.payDeposit(account.getSubscriberCount(), amount, creditCard,
	// businessRole, auditHeader);
	// }catch(TelusAPIException tae){
	// assertEquals(": PolicyException was received from WPS service (errorCode=PYMT80003, message=Validation Failed.  Invalid value [0] for expiry year.). Please contact the WPS team for support.",
	// tae.getMessage());
	// }
	// }

	public void testDecorate() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(805938);
		CreditCheckResult checkResult = account.getCreditCheckResult();
		assertNotNull(checkResult);
	}

	public void testCheckNewSubscriberEligibility() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(805938);
		double thresholdAmount = 500.00;
		CreditCheckResult checkResult = account.checkNewSubscriberEligibility(
				account.getSubscriberCount(), thresholdAmount);
		assertNotNull(checkResult);
	}

	public void testGetInvoiceHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(805938);
		Date fromDate = getDateInput(2006, 0, 01);
		Date toDate = getDateInput(2006, 0, 31);

		InvoiceHistory[] invoiceHistories = account.getInvoiceHistory(fromDate,
				toDate);
		assertEquals(0, invoiceHistories.length);
		for (int i = 0; i < invoiceHistories.length;) {
			assertEquals(50.55, invoiceHistories[i].getAmountDue(), 0);
			assertEquals(84.07, invoiceHistories[i].getPreviousBalance(), 0);
			break;
		}
	}

	public void testGetPaymentHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		Date fromDate = getDateInput(2010, 05, 27);
		Date toDate = getDateInput(2011, 11, 27);

		PaymentHistory[] paymentHistories = account.getPaymentHistory(fromDate,
				toDate);
		assertEquals(2, paymentHistories.length);
		for (int i = 0; i < paymentHistories.length;) {
			assertEquals("CC", paymentHistories[i].getPaymentMethodCode());
			assertEquals(225, paymentHistories[i].getOriginalAmount(), 0);
			break;
		}
	}

	public void testGetLastPaymentActivity() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(805938);

		PaymentHistory paymentHistory = account.getLastPaymentActivity();
		assertNotNull(paymentHistory);

	}

	public void testGetPaymentMethodChangeHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(805938);
		Date fromDate = getDateInput(2003, 0, 01);
		Date toDate = getDateInput(2011, 10, 01);

		PaymentMethodChangeHistory[] paymentMethodChangeHistories = account
				.getPaymentMethodChangeHistory(fromDate, toDate);
		assertEquals(1, paymentMethodChangeHistories.length);
		for (int i = 0; i < paymentMethodChangeHistories.length;) {
			assertEquals("100000000000000006551",
					paymentMethodChangeHistories[i].getCreditCardToken());
			assertEquals("341400",
					paymentMethodChangeHistories[i].getCreditCardExpiry());
			assertEquals("0112", paymentMethodChangeHistories[i].getBankCode()
					.trim());
			assertEquals("C", paymentMethodChangeHistories[i]
					.getBankAccountNumber().trim());
			break;
		}
	}

	public void testGetStatusChangeHistory() throws TelusAPIException {

		Account account = accountManager.findAccountByBAN(12474);
		Date fromDate = getDateInput(1999, 1, 1);
		Date toDate = getDateInput(2010, 12, 7);

		StatusChangeHistory[] statusChangeHistories = account
				.getStatusChangeHistory(fromDate, toDate);
		assertEquals(4, statusChangeHistories.length);
		for (int i = 0; i < statusChangeHistories.length;) {
			assertEquals("O", statusChangeHistories[i].getBanStatus());
			assertEquals("RSP", statusChangeHistories[i].getActivityTypeCode());
			break;
		}
	}

	public void testGetPhoneNumberBySubscriberID() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		String pSubscriberId = "4032130201";

		String phoneNumber = account
				.getPhoneNumberBySubscriberID(pSubscriberId);
		assertEquals("4032130201", phoneNumber);

	}

	public void testGetAddressChangeHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(8);
		Date fromDate = getDateInput(1999, 1, 1);
		Date toDate = getDateInput(2010, 12, 7);
		AddressHistory[] addressHistories = account.getAddressChangeHistory(
				fromDate, toDate);
		assertEquals(2, addressHistories.length);
		for (int i = 0; i < addressHistories.length;) {
			assertEquals("AB", addressHistories[i].getAddress().getProvince());
			break;
		}
	}

	public void testGetVoiceUsageSummary() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		VoiceUsageSummary[] voiceUsageSummaries = ((PostpaidAccount) account)
				.getVoiceUsageSummary(null);
		assertEquals(0, voiceUsageSummaries.length);

	}

	public void testGetShareablePricePlanSubscriberCount()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		PricePlanSubscriberCount[] shareableCountResult = ((TMPagerPostpaidConsumerAccount) account)
				.getShareablePricePlanSubscriberCount(true);
		assertEquals(0, shareableCountResult.length);
	}

	public void testGetServiceSubscriberCounts() throws TelusAPIException {

		Account account = accountManager.findAccountByBAN(16910);
		String[] serviceCodes = { "USTL50F9 ", "PRIM100N3", "RIMSB    ",
				"RIMTM    ", "SDOC1    ", "SE911    ", "SFBC     " };

		ServiceSubscriberCount[] serviceSubscriberCounts = account
				.getServiceSubscriberCounts(serviceCodes, true);
		assertEquals(2, serviceSubscriberCounts.length);
	}

	public void testSuspend() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(292007);
		account.suspend(new Date(), "PRTO", "memoText");
	}

	public void testCancel() throws TelusAPIException {
		try {
			Account account = accountManager.findAccountByBAN(292007);
			account.cancel(new Date(), "CAN", 'C', "FEW", "memo");
			fail("Exception expected");
		} catch (Exception e) {
			e.printStackTrace();
			// assertEquals("Activity Reason CAN is either invalid or does not exist.",
			// e.getMessage());
		}
	}

	public void testGetDepositHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(81);
		Date fromDate = getDateInput(2000, 1, 1);
		Date toDate = getDateInput(2003, 12, 7);

		DepositHistory[] depositHistories = account.getDepositHistory(fromDate,
				toDate);
		assertEquals(4, depositHistories.length);

	}

	public void testGetPendingChargeHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		Date fromDate = getDateInput(2002, 1, 20);
		Date toDate = getDateInput(2010, 1, 26);
		String subscriberId = "4033409558";
		int maximum = 50;

		SearchResults searchResults = account.getPendingChargeHistory(fromDate,
				toDate, 'A', subscriberId, maximum);
		assertEquals(1, searchResults.getCount());
	}

	public void testGetCredits() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		Date fromDate = getDateInput(2000, 3, 22);
		Date toDate = getDateInput(2000, 4, 12);
		String subscriberId = "7804708305";
		int maximum = 500;
		char level = '1';
		String billState = "B";
		SearchResults searchResults = account.getCredits(fromDate, toDate,
				billState, level, subscriberId, maximum);

		assertEquals(13, searchResults.getCount());
	}

	public void testGetCredits1() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		Date fromDate = getDateInput(2000, 3, 22);
		Date toDate = getDateInput(2000, 4, 12);
		String subscriberId = "";
		int maximum = 500;
		char level = '1';
		String billState = "B";
		String knowbilityOperatorId = "";

		SearchResults searchResults = account.getCredits(fromDate, toDate,
				billState, level, subscriberId, knowbilityOperatorId, maximum);

		assertEquals(13, searchResults.getCount());
	}

	public void testGetBilledCharges() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		Date fromDate = getDateInput(2002, 8, 23);
		Date toDate = getDateInput(2002, 8, 23);
		String phoneNumber = "4033404108";
		int billSeqNo = 54;

		Charge[] charges = account.getBilledCharges(billSeqNo, phoneNumber,
				fromDate, toDate);

		assertEquals(0, charges.length);
	}

	public void testGetCancellationPenalty() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(8);
		CancellationPenalty cancellationPenalty = account
				.getCancellationPenalty();
		assertEquals(200.00, cancellationPenalty.getDepositAmount(), 2);
	}

	public void testCreateDuplicateAccount() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(17605);
		assertEquals(false, account.isIDEN());

		Account duplicateAccount = account.createDuplicateAccount();
		assertNotNull(duplicateAccount);
	}

	public void testGetFutureStatusChangeRequests() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		FutureStatusChangeRequest[] futureStatusChangeRequests = account
				.getFutureStatusChangeRequests();
		assertEquals(0, futureStatusChangeRequests.length);
	}

	public void testGetCredits2() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		Date fromDate = getDateInput(2000, 3, 22);
		Date toDate = getDateInput(2000, 4, 12);
		String subscriberId = "";
		int maximum = 500;
		char level = '1';
		String billState = "B";
		String reasonCode = "";

		SearchResults searchResults = account.getCredits(fromDate, toDate,
				billState, reasonCode, level, subscriberId, maximum);
		assertEquals(13, searchResults.getCount());
	}

	public void testGetFollowUpStatistics() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		FollowUpStatistics followUpStatisticsInfo = account
				.getFollowUpStatistics(true);
		assertFalse(followUpStatisticsInfo.hasOpenFollowUps());
	}

	public void testGetProductSubscriberLists() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		ProductSubscriberList[] productSubscriberList = account
				.getProductSubscriberLists();
		assertEquals(2, productSubscriberList.length);
	}

	public void testGetDiscounts() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		Discount[] discounts = ((PostpaidAccount) account).getDiscounts();
		assertEquals(0, discounts.length);
		for (int i = 0; i < discounts.length;) {
			assertEquals("3MOS", discounts[0].getDiscountCode());
			assertEquals("18681", discounts[0].getDiscountByUserId());
			break;
		}
	}

	public void testChangeCreditCheckDeposits() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		CreditCheckResultDepositInfo[] creditCheckResultDepositInfo = new CreditCheckResultDepositInfo[1];
		creditCheckResultDepositInfo[0] = new CreditCheckResultDepositInfo();
		creditCheckResultDepositInfo[0].setDeposit(1.1);
		creditCheckResultDepositInfo[0].setProductType("C");
		CreditCheckDepositChangeReason[] changeReason = api
				.getReferenceDataManager().getCreditCheckDepositChangeReasons();
		CreditCheckDepositChangeReason reason = changeReason[0];
		String reasonText = "deposit change test";

		((PostpaidAccount) account).changeCreditCheckDeposits(
				creditCheckResultDepositInfo, reason, reasonText);

	}

	public void testGetCreditByFollowUpId() throws TelusAPIException {

		Account account = accountManager.findAccountByBAN(12474);
		int followUpId = 18150420;
		Credit[] credits = account.getCreditByFollowUpId(followUpId);
		assertEquals(0, credits.length);
	}

	public void testGetCancellationPenaltyList() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(194587);
		String[] subscriberIds = { "4037109998" };
		CancellationPenalty[] cancellationPenalties = account
				.getCancellationPenaltyList(subscriberIds);
		assertEquals(1, cancellationPenalties.length);

	}

	public void testCancelSubscribers() throws TelusAPIException {

		try {
			Account account = accountManager.findAccountByBAN(194587);
			Date effectiveDate = new Date();
			String reason = "AIE";
			char depMethod = 'O';
			String[] subscriberId = { "4037109998" };
			String[] waiverReason = { "FEW" };
			String comment = "test";
			account.cancelSubscribers(effectiveDate, reason, depMethod,
					subscriberId, waiverReason, comment);
		} catch (TelusAPIException e) {
			assertEquals(
					"Cannot suspend the last Active Subscriber. Please suspend the BAN.",
					e.getMessage());
		}
	}

	public void testSuspendSubscribers() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16424);
		Date effectiveDate = new Date();
		String reason = "AIE";
		String[] subscriberId = { "4036207433" };
		String comment = "test";

		account.suspendSubscribers(effectiveDate, reason, subscriberId, comment);
	}

	public void testRestoreSuspendedSubscribers() throws TelusAPIException {

		try {
			Account account = accountManager.findAccountByBAN(750714);
			Date effectiveDate = new Date();
			String reason = "CRQ";
			String[] subscriberId = { "5196351113" };
			String comment = "test";

			account.restoreSuspendedSubscribers(effectiveDate, reason,
					subscriberId, comment);
		} catch (TelusAPIException te) {
			assertEquals(": Subscriber status is illegal.", te.getMessage());
		}
	}

	public void testGetPoolingEnabledPricePlanSubscriberCount_1()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		boolean refresh = true;

		PoolingPricePlanSubscriberCount[] poolingCountResult = ((PostpaidAccount) account)
				.getPoolingEnabledPricePlanSubscriberCount(refresh);
		assertEquals(0, poolingCountResult.length);
	}

	public void testGetPoolingEnabledPricePlanSubscriberCount_2()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		boolean refresh = true;
		int poolingGroupId = PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL;

		PoolingPricePlanSubscriberCount poolingCountResult = ((PostpaidAccount) account)
				.getPoolingEnabledPricePlanSubscriberCount(poolingGroupId,
						refresh);
		// assertEquals(1, poolingCountResult.getPoolingGroupId());
	}

	public void testGetZeroMinutePoolingEnabledPricePlanSubscriberCount_1()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		((PostpaidAccount) account).refreshPricePlanSubscriberCounts();
	}

	public void testGetZeroMinutePoolingEnabledPricePlanSubscriberCount_2()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		boolean refresh = true;
		int poolingGroupId = 7;

		PoolingPricePlanSubscriberCount poolingCountResult = ((PostpaidAccount) account)
				.getZeroMinutePoolingEnabledPricePlanSubscriberCount(
						poolingGroupId, refresh);
		// assertEquals(7, poolingCountResult.getPoolingGroupId());

	}

	public void testGetDollarPoolingPricePlanSubscriberCount()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		boolean refresh = true;

		PricePlanSubscriberCount[] pricePlanSubscriberCounts = ((PostpaidAccount) account)
				.getDollarPoolingPricePlanSubscriberCount(refresh);
		assertEquals(0, pricePlanSubscriberCounts.length);
	}

	public void testIsFeatureCategoryExistOnSubscribers()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		String categoryCode = "VM";

		boolean isFeatureCategoryExist = ((PostpaidAccount) account)
				.isFeatureCategoryExistOnSubscribers(categoryCode);
		assertEquals(false, isFeatureCategoryExist);

	}

	public void testGetDepositAssessedHistory() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);

		DepositAssessedHistory[] depositAssessedHistories = ((PostpaidAccount) account)
				.getDepositAssessedHistory();
		assertEquals(12, depositAssessedHistories.length);
	}

	public void testGetOriginalDepositAssessedHistory()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);

		DepositAssessedHistory[] depositAssessedHistories = ((PostpaidAccount) account)
				.getOriginalDepositAssessedHistory();
		assertEquals(0, depositAssessedHistories.length);
	}

	public void testUpdateBrand() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(16910);
		int brandId = 1;
		String memoText = "memo";

		account.updateBrand(brandId, memoText);

	}

	/*
	 * public void testSave() throws TelusAPIException{ Account account =
	 * accountManager.findAccountByBAN(20007217);
	 * 
	 * boolean saveCreditCheckInfo=true;
	 * 
	 * account.save(saveCreditCheckInfo);
	 * 
	 * }
	 */

	public void testApplyPayment() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		double amount = 20.00;

		String sourceID = "ONLINE";
		String sourceType = "O";
		// Input Data- PAYMENT table

		PaymentMethod paymentMethod = accountManager.newPaymentMethod(account);
		CreditCardInfo cc = new CreditCardInfo();

		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("455630");
		cc.setToken("100000000000001366517");
		cc.setTrailingDisplayDigits("3821");
		cc.setType("VS");
		cc.setAuthorizationCode("123");
		BankAccount bankAccount = new BankAccountInfo();
		bankAccount.setBankAccountNumber("1000215");
		bankAccount.setBankAccountType("I");
		bankAccount.setBankBranchNumber("06562");
		bankAccount.setBankCode("003");

		Cheque cheque = paymentMethod.getCheque();
		cheque.setBankAccount(bankAccount);
		cheque.setChequeNumber("193");
		// cheque.
		account.applyPayment(amount, cheque, sourceID, sourceType);

	}

	public void testgetNoOfInvoice() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(194587);
		int invoice = account.getNoOfInvoice();
		assertEquals(0, invoice);

	}

	public void testGetCharges() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		String[] chargeCodes = { "CNRD" };
		String billState = Account.BILL_STATE_ALL;
		char level = ChargeType.CHARGE_LEVEL_SUBSCRIBER;
		String subscriberId = "4033404108";
		Date from = getDateInput(1999, 0, 01);
		Date to = getDateInput(2011, 0, 01);
		int maximum = 50;
		Charge[] charges = account.getCharges(chargeCodes, billState, level,
				subscriberId, from, to, maximum);
		assertEquals(0, charges.length);
	}

	public void testRefreshCreditCheckResult() throws TelusAPIException {
		System.out.println("testRefreshCreditCheckResult start");
		Account account = accountManager.findAccountByBAN(12474);
		account.refreshCreditCheckResult();
		System.out.println("testRefreshCreditCheckResult end");

	}

	public void testPayBill_104ErrorReason() throws TelusAPIException {
		String errorReasonCode = "104";
		String errorText = "Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.";
		String errorTextFr = "Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.";

		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(banToTest);
			double amount = 1.25;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000006551", "519121", "1111");
			creditCard
			.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) - 5);
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);

			fail("InvalidCreditCardException expected");
		} catch (InvalidCreditCardException cc) {
			assertEquals(errorReasonCode, cc.getReason());
			assertEquals(errorText, cc.getReasonText());
			assertEquals(errorTextFr, cc.getReasonTextFrench());
			assertEquals(avalonErrorCode, cc.getPolicyFaultInfo()
					.getErrorCode());
		} catch (TelusAPIException ex) {
			System.out.println("error msg: ");
			System.out.println(ex.getMessage());
			System.out
			.println("This is the expected result after rollback of PCI Wrap up Release");
		}
	}

	public void testUpdatePaymentMethod_104ErrorReason()
			throws TelusAPIException {
		String errorReasonCode = "104";
		String errorText = "Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.";
		String errorTextFr = "Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.";

		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(70902334);
			double amount = 1.25;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000004120376", "460172", "0933");
			creditCard.setExpiryYear(2025);

			AuditHeader auditHeader = new AuditHeaderInfo();
			creditCard.validate("validate test", AudienceType.CLIENT,auditHeader);

			fail("InvalidCreditCardException expected");

			assertTrue(account instanceof PostpaidAccount);
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;

			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			newPaymentMethod.setCreditCard(creditCard);
			postPaidAccount.savePaymentMethod(newPaymentMethod);

		} catch (InvalidCreditCardException cc) {
			cc.printStackTrace();
			assertEquals(errorReasonCode, cc.getReason());
			assertEquals(errorText, cc.getReasonText());
			assertEquals(errorTextFr, cc.getReasonTextFrench());
			assertEquals(avalonErrorCode, cc.getPolicyFaultInfo()
					.getErrorCode());
		}
	}

	public void testPayBill_104ErrorReason_Client() throws TelusAPIException {
		String errorReasonCode = "104";
		String errorText = "Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.";
		String errorTextFr = "Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration. Si vous éprouvez toujours des difficultés, veuillez communiquer avec le Service à la clientèle en composant le 611 à partir de votre téléphone.";

		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(banToTest);
			double amount = 1.25;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000006551", "519121", "1111");
			creditCard.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) - 5);
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, AudienceType.CLIENT,
					auditHeader);
			fail("InvalidCreditCardException expected");
		} catch (InvalidCreditCardException cc) {
			assertEquals(errorReasonCode, cc.getReason());
			assertEquals(errorText, cc.getReasonText());
			assertEquals(errorTextFr, cc.getReasonTextFrench());
			assertEquals(avalonErrorCode, cc.getPolicyFaultInfo()
					.getErrorCode());
		} catch (TelusAPIException ex) {
			System.out.println("error msg: ");
			System.out.println(ex.getMessage());
			System.out
			.println("This is the expected result after rollback of PCI Wrap up Release");
		}
	}

	public void testPayBill_104ErrorReason_Agent() throws TelusAPIException {
		String errorReasonCode = "104";
		String errorText = "Sorry, we cannot complete the credit card payment. Please check the credit card number and expiration date.";
		String errorTextFr = "Nous sommes désolés de ne pouvoir traiter votre paiement par carte de crédit. Veuillez vérifier votre numéro de carte de crédit et la date d'expiration.";

		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(banToTest);
			double amount = 1.25;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000006551", "519121", "1111");
			creditCard
			.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) - 5);
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, AudienceType.AGENT, auditHeader);
			fail("InvalidCreditCardException expected");
		} catch (InvalidCreditCardException cc) {
			assertEquals(errorReasonCode, cc.getReason());
			assertEquals(errorText, cc.getReasonText());
			assertEquals(errorTextFr, cc.getReasonTextFrench());
			assertEquals(avalonErrorCode, cc.getPolicyFaultInfo()
					.getErrorCode());
		} catch (TelusAPIException ex) {
			System.out.println("error msg: ");
			System.out.println(ex.getMessage());
			System.out
			.println("This is the expected result after rollback of PCI Wrap up Release");
		}
	}

	public void testPayBill_102ErrorReason() throws TelusAPIException {
		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(8);
			double amount = 1.00;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000000006404", "490000", "0003");
			creditCard
			.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) + 5);
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
		} catch (InvalidCreditCardException cc) {
			assertEquals("102", cc.getReason());
			assertEquals(
					"Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date. If you have an international credit card, you may need to pre-register it. If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.",
					cc.getReasonText());
			assertEquals(
					"Désolés, votre paiement par carte de crédit ne peut pas être effectué. Veuillez vérifier les renseignements sur la carte de crédit. S'il s'agit d'une carte de crédit internationale, vous aurez peut-être à la préenregistrer. Si le problème persiste, veuillez communiquer avec le service à la clientèle en composant le 611.",
					cc.getReasonTextFrench());
		}
	}

	public void testPayBill() throws TelusAPIException {
		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(banToTest);
			double amount = 1.02;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000001865670", "519111", "1111");
			creditCard
			.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) + 5);
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
			Calendar fromDate = Calendar.getInstance();
			fromDate.add(Calendar.DAY_OF_MONTH, -1);
			PaymentHistory[] payHist = account.getPaymentHistory(
					fromDate.getTime(), new Date());
			assertEquals(payHist[payHist.length - 1].getActualAmount(), amount,
					0.0);
		} catch (InvalidCreditCardException cc) {
			cc.printStackTrace();
			fail("Exception occurred");
		}
	}

	public void testPayBill_LongLastName() throws TelusAPIException {
		// Test Account management EJB method
		try {
			Account account = accountManager.findAccountByBAN(805938); // //pt148-fail
			double amount = 1.10;
			CreditCard creditCard = accountManager.newCreditCard(account);
			creditCard.setToken("100000000000001865670", "519111", "1111");
			creditCard
			.setExpiryYear(Calendar.getInstance().get(Calendar.YEAR) + 5);
			creditCard
			.setHolderName("StringLongerThan20CharactersAreSupposedToWork");
			AuditHeader auditHeader = new AuditHeaderInfo();

			account.payBill(amount, creditCard, "businessRole", auditHeader);
			Calendar fromDate = Calendar.getInstance();
			fromDate.add(Calendar.DAY_OF_MONTH, -1);
			PaymentHistory[] payHist = account.getPaymentHistory(
					fromDate.getTime(), new Date());
			assertEquals(amount, payHist[payHist.length - 1].getActualAmount(),
					0.0);
		} catch (InvalidCreditCardException cc) {
			cc.printStackTrace();
			fail("Exception occurred");
		}
	}

	public void testGetAvailableBillCycles() throws UnknownBANException,
	TelusAPIException {
		TMAccount account = (TMAccount) accountManager.findAccountByBAN(805938);// pt148-pass
		System.out.println(account.getAddress().getProvince());
		BillCycle[] bc = account.getAvailableBillCycles();
		for (int x = 0; x < bc.length; x++) {
			System.out.println(bc[x].getCode());
			assertEquals("1", bc[x].getCode().toString());

		}

	}

	public void testSave() throws TelusAPIException {

		AccountInfo accInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		accInfo.setAccountSubType('R');
		accInfo.setAccountType('I');
		accInfo.setBrandId(0);
		accInfo.setBanSegment("TCSO");
		accInfo.setBanSubSegment("OTHR");
		AddressInfo address = new AddressInfo();
		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");
		accInfo.setAddress0(address);
		accInfo.setEmail("dany.taylor@telusmobility.com");
		accInfo.setPin("5555");
		accInfo.setLanguage("EN");
		accInfo.setDealerCode("0000000008");
		accInfo.setSalesRepCode("0000");
		((PostpaidConsumerAccountInfo) accInfo).getName0().setTitle("MR.");
		((PostpaidConsumerAccountInfo) accInfo).getName0().setFirstName("Dany");
		((PostpaidConsumerAccountInfo) accInfo).getName0()
		.setLastName("Taylor");
		accInfo.setHomePhone("4165551234");
		accInfo.setBusinessPhone("4165550000");
		accInfo.setNoOfInvoice(2);
		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
		((PostpaidConsumerAccountInfo) accInfo).getPersonalCreditInformation0()
		.setBirthDate(bd);
		((PostpaidConsumerAccountInfo) accInfo).getPersonalCreditInformation0()
		.setSin("123456789");
		String pymMethod = "R";
		((PostpaidConsumerAccountInfo) accInfo).getPaymentMethod0()
		.setPaymentMethod(pymMethod);

		TMAccount account = new TMAccount(provider, accInfo);

		account.save();
	}

	public void testUpdatePaymentMethodfoPreauthorizeUpdateWithNewPaymentMethodasD()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(banToTest);
		if (account instanceof PostpaidAccount) {
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;
			account.setEmail("xyz@gmail.com");
			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			System.out.println("newPaymentMethod" + newPaymentMethod);
			newPaymentMethod.setPaymentMethod("D");
			postPaidAccount.savePaymentMethod(newPaymentMethod);
			// asserting for the xml generated
		}

	}

	public void testUpdatePaymentMethodfoPreauthorizeUpdateWithNewPaymentMethodasD2()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(70675207);
		if (account instanceof PostpaidAccount) {
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;
			account.setEmail("xyz@gmail.com");
			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			System.out.println("newPaymentMethod" + newPaymentMethod);
			newPaymentMethod.setPaymentMethod("D");

			postPaidAccount.savePaymentMethod(newPaymentMethod);
			// asserting for the xml generated
		}

	}

	public void testUpdatePaymentMethodfoPreauthorizeUpdateWithNewPaymentMethodasC()
			throws TelusAPIException {

		// C to C scenario
		Account account = accountManager.findAccountByBAN(250452);// 70648212 is
		// new
		// scenario
		if (account instanceof PostpaidAccount) {
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;
			CreditCardInfo cc = new CreditCardInfo();

			cc.setExpiryMonth(12);
			cc.setExpiryYear(2015);
			cc.setHolderName("Danny Summer");
			cc.setLeadingDisplayDigits("455630");
			cc.setToken("100000000000001366517");
			cc.setTrailingDisplayDigits("3821");
			cc.setType("VS");
			cc.setAuthorizationCode("123");

			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			System.out.println("newPaymentMethod" + newPaymentMethod);
			newPaymentMethod.setCreditCard(cc);
			newPaymentMethod.setPaymentMethod("C");
			postPaidAccount.savePaymentMethod(newPaymentMethod);
			;

		}

	}

	public void testUpdatePaymentMethodfoPreauthorizeRemovalWithOldPaymentMethodasC()
			throws TelusAPIException {

		//
		Account account = accountManager.findAccountByBAN(70648208);
		// account.set
		if (account instanceof PostpaidAccount) {
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;
			CreditCardInfo cc = new CreditCardInfo();

			cc.setExpiryMonth(12);
			cc.setExpiryYear(2015);
			cc.setHolderName("Danny Summer");
			cc.setLeadingDisplayDigits("455630");
			cc.setToken("100000000000001366517");
			cc.setTrailingDisplayDigits("3821");
			cc.setType("VS");
			cc.setAuthorizationCode("123");

			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			System.out.println("newPaymentMethod" + newPaymentMethod);
			newPaymentMethod.setCreditCard(cc);
			newPaymentMethod.setPaymentMethod("R");
			postPaidAccount.savePaymentMethod(newPaymentMethod);

		}

	}

	public void testUpdatePaymentMethodfoPreauthorizeSetUp()
			throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(12474);
		if (account instanceof PostpaidAccount) {
			PostpaidAccount postPaidAccount = (PostpaidAccount) account;

			PaymentMethod newPaymentMethod = postPaidAccount.getPaymentMethod();
			newPaymentMethod.setPaymentMethod("D");
			postPaidAccount.savePaymentMethod(newPaymentMethod);
			System.out.println(newPaymentMethod + "newPaymentMethod");
		}

	}

}
