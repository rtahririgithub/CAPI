package com.telus.cmb.account.lifecyclemanager.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.Charge;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.PaymentCard;
import com.telus.api.account.PaymentTransfer;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.dao.AccountDao;
import com.telus.cmb.account.lifecyclemanager.dao.AdjustmentDao;
import com.telus.cmb.account.lifecyclemanager.dao.CollectionDao;
import com.telus.cmb.account.lifecyclemanager.dao.ContactDao;
import com.telus.cmb.account.lifecyclemanager.dao.CreditBalanceTransferDao;
import com.telus.cmb.account.lifecyclemanager.dao.CreditCheckDao;
import com.telus.cmb.account.lifecyclemanager.dao.FleetDao;
import com.telus.cmb.account.lifecyclemanager.dao.FollowUpDao;
import com.telus.cmb.account.lifecyclemanager.dao.InvoiceDao;
//import com.telus.cmb.account.lifecyclemanager.dao.LetterDao;
import com.telus.cmb.account.lifecyclemanager.dao.PaymentDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.account.lifecyclemanager.dao.UserDao;
//import com.telus.cmb.common.aop.utilities.BanValidatorTestHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.CollectionStateInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
import com.telus.eas.account.info.MaxVoipLineInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberEligibilitySupportingInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeAdjustmentWithTaxInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
//import com.telus.eas.framework.info.LMSRequestInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class AccountLifecycleManagerImplTest {

	@Autowired
	AccountLifecycleManagerImpl impl;

	TestFleetDao fleetDao;
	TestInvoiceDao invoiceDao;
	TestFollowUpDao followUpDao;
	TestAccountDao accountDao;
	TestCollectionDao collectionDao;
	TestSubscriberDao subscriberDao;
	TestUserDao userDao;
	//TestLetterDao letterDao;
	TestAdjustmentDao adjustmentDao;
	TestCreditCheckDao creditCheckDao;
	TestPaymentDao paymentDao;
	//TestPrepaidDao prepaidDao;
	TestAccountInformationHelper accountInformationHelper;
	TestContactDao contactDao; 
	
	@Autowired
	private CreditBalanceTransferDao creditBalanceTransferDao;

	@Before
	public void setup() {
		impl = new AccountLifecycleManagerImpl();

		fleetDao = new TestFleetDao();
		impl.setFleetDao(fleetDao);

		invoiceDao = new TestInvoiceDao();
		impl.setInvoiceDao(invoiceDao);

		followUpDao = new TestFollowUpDao();
		impl.setFollowUpDao(followUpDao);

		accountDao = new TestAccountDao();
		impl.setAccountDao(accountDao);

		collectionDao = new TestCollectionDao();
		impl.setCollectionDao(collectionDao);

		subscriberDao = new TestSubscriberDao();
		impl.setSubscriberDao(subscriberDao);

		userDao = new TestUserDao();
		impl.setUserDao(userDao);

//		letterDao = new TestLetterDao();
//		impl.setLetterDao(letterDao);

		adjustmentDao = new TestAdjustmentDao();
		impl.setAdjustmentDao(adjustmentDao);

		creditCheckDao = new TestCreditCheckDao();
		impl.setCreditCheckDao(creditCheckDao);

		paymentDao = new TestPaymentDao();
		impl.setPaymentDao(paymentDao);
		
		
		
		contactDao = new TestContactDao();
		impl.setContactDao(contactDao);

		accountInformationHelper = new TestAccountInformationHelper();
		impl.setAccountInformationHelper(accountInformationHelper);

	}

	private static class TestAccountInformationHelper implements AccountInformationHelper {

		private int[] banArray;
		private boolean retrieveAccountsByBanCalled = false;
		private boolean isRetrieveAccountsByBanCalled = false;
		public boolean isRetrieveLastCreditCheckResultByBanCalled=false;

		@Override
		public String retrieveAccountStatusByBan(int ban)
		throws ApplicationException {
			return "T";
		}

		@Override
		public List<AccountInfo> retrieveAccountsByBan(int[] banArray) {
			this.retrieveAccountsByBanCalled = true;
			this.banArray = banArray;
			return null;
		}

		@Override
		public List retrieveAccountsByPostalCode(String lastName,
				String postalCode, int maximum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByBan(int ban)
		throws ApplicationException {
			this.isRetrieveAccountsByBanCalled=true;
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByPhoneNumber(String phoneNumber)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAccountsByPhoneNumber(String phoneNumber)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAccountsByPhoneNumber(String phoneNumber,
				boolean includePastAccounts, boolean onlyLastAccount)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Map retrievePhoneNumbersForBAN(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public VoiceUsageSummaryInfo[] retrieveVoiceUsageSummary(int ban,
				String featureCode) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PrepaidConsumerAccountInfo retrieveAccountInfo(int ban,
				String phoneNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrieveAccountsByBusinessName(
				String nameType, String legalBusinessName,
				boolean legalBusinessNameExactMatch, char accountStatus,
				char accountType, String provinceCode, int brandId, int maximum)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrieveAccountsByName(String nameType,
				String firstName, boolean firstNameExactMatch, String lastName,
				boolean lastNameExactMatch, char accountStatus,
				char accountType, String provinceCode, int brandId, int maximum)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double retrieveUnpaidDataUsageTotal(int ban, Date date)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public double retrieveUnpaidAirTimeTotal(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AutoTopUpInfo retrieveAutoTopUpInfoForPayAndTalkSubscriber(
				int pBan, String pPhoneNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveMemos(int Ban, int Count)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveMemos(MemoCriteriaInfo MemoCriteria)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MemoInfo retrieveLastMemo(int Ban, String MemoType)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveLwAccountByPhoneNumber(String pPhoneNumber)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAccountsByDealership(char accountStatus,
				String dealerCode, Date startDate, Date endDate, int maximum)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BillNotificationContactInfo getLastEBillNotificationSent(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public EBillRegistrationReminderInfo getLastEBillRegistrationReminderSent(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<BillNotificationHistoryRecord> getBillNotificationHistory(int ban,
				String subscriptionType) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void expireBillNotificationDetails(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public List retrieveAccountsBySalesRep(char accountStatus,
				String dealerCode, String salesRepCode, Date startDate,
				Date endDate, int maximum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public PrepaidConsumerAccountInfo retrieveAccountInfoForPayAndTalkSubscriber(
				int ban, String phoneNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] retrieveBanIds(char accountType, char accountSubType,
				char banStatus, int maximum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int[] retrieveBanIdByAddressType(char accountType,
				char accountSubType, char banStatus, char addressType,
				int maximum) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveAttachedSubscribersCount(int ban,
				FleetIdentityInfo fleetIdentity) throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public List retrieveFollowUpAdditionalText(int ban, int followUpId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveFollowUpHistory(int followUpId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int ban,
				int followUpID) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveFollowUps(
				FollowUpCriteriaInfo followUpCriteria) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveFollowUps(int ban, int Count)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FollowUpStatisticsInfo retrieveFollowUpStatistics(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveLastFollowUpIDByBanFollowUpType(int ban,
				String followUpType) throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public ProductSubscriberListInfo[] retrieveProductSubscriberLists(
				int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BillParametersInfo retrieveBillParamsInfo(int banId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public FleetInfo[] retrieveFleetsByBan(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByImsi(String imsi)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveSubscriberIdsByStatus(int banId, char status,
				int maximum) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAccountsBySerialNumber(String serialNumber)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountBySerialNumber(String serialNumber)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public MemoInfo retrieveLastCreditCheckMemoByBan(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveSubscriberPhoneNumbersByStatus(int banId,
				char status, int maximum) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String retrieveCorporateName(int id) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getClientAccountId(int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public PaymentHistoryInfo retrieveLastPaymentActivity(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveSubscriberInvoiceDetails(int banId,
				int billSeqNo) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<BillNotificationContactInfo> retrieveBillNotificationContacts(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ChargeInfo> retrieveBilledCharges(int ban, int pBillSeqNo,
				String pPhoneNumber, Date from, Date to)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveAssociatedTalkGroupsCount(
				FleetIdentityInfo pFleeIdentity, int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public AddressHistoryInfo[] retrieveAddressHistory(int pBan,
				Date pFromDate, Date pToDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getPaperBillSupressionAtActivationInd(int pBan)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean isEBillRegistrationReminderExist(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean hasEPostSubscription(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isFeatureCategoryExistOnSubscribers(int ban,
				String pCategoryCode) throws ApplicationException {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public AddressInfo retrieveAlternateAddressByBan(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public double getPrepaidActivationCredit(String applicationId,
				String pUserId,
				PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public BusinessCreditIdentityInfo[] retrieveBusinessList(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ConsumerNameInfo[] retrieveAuthorizedNames(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DepositHistoryInfo[] retrieveDepositHistory(int ban,
				Date fromDate, Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DepositAssessedHistoryInfo[] retrieveDepositAssessedHistoryList(
				int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrieveCredits(int ban, Date fromDate,
				Date toDate, String billState, char level, String subscriber,
				int maximum) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrieveCredits(int ban, Date fromDate,
				Date toDate, String billState, char level,
				String knowbilityOperatorId, String subscriber, int maximum)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrieveCredits(int ban, Date fromDate,
				Date toDate, String billState, String knowbilityOperatorId,
				String reasonCode, char level, String subscriber, int maximum)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveCreditByFollowUpId(int followUpId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<PaymentHistoryInfo> retrievePaymentHistory(int ban, Date fromDate,
				Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public InvoicePropertiesInfo getInvoiceProperties(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ChargeInfo[] retrieveCharges(int ban, String[] chargeCodes,
				String billState, char level, String subscriberId, Date from,
				Date to, int maximum) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public DepositAssessedHistoryInfo[] retrieveOriginalDepositAssessedHistoryList(
				int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String retrieveHotlinedSubscriberPhoneNumber(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<InvoiceHistoryInfo> retrieveInvoiceHistory(int ban, Date fromDate,
				Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int retrieveAttachedSubscribersCountForTalkGroup(int urbanID,
				int fleetID, int talkGroupId, int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public CollectionHistoryInfo[] retrieveCollectionHistoryInfo(int banId,
				Date fromDate, Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

//		@Override
//		public SearchResultsInfo retrieveLetterRequests(int banId, Date from,
//				Date to, char level, String pSubscriber, int maximum)
//		throws ApplicationException {
//			// TODO Auto-generated method stub
//			return null;
//		}

		@Override
		public List<PaymentActivityInfo> retrievePaymentActivities(int banId, int paymentSeqNo)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<PaymentMethodChangeHistoryInfo> retrievePaymentMethodChangeHistory(int ban,
				Date fromDate, Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveRefundHistory(int ban, Date fromDate,
				Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ChargeInfo> retrieveRelatedChargesForCredit(int pBan,
				double pChargeSeqNo) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public HashMap retrievePCSNetworkCountByBan(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveZeroMinutePoolingPricePlanSubscriberCounts(
				int banId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveZeroMinutePoolingPricePlanSubscriberCounts(
				int banId, int poolGroupId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrievePoolingPricePlanSubscriberCounts(int banId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrievePoolingPricePlanSubscriberCounts(int banId,
				int poolGroupId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveServiceSubscriberCounts(int banId,
				String[] serviceCodes, boolean includeExpired)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveMinutePoolingEnabledPricePlanSubscriberCounts(
				int banId, String[] poolingCoverageTypes)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveDollarPoolingPricePlanSubscriberCounts(
				int banId, String productType) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveShareablePricePlanSubscriberCount(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<CreditInfo> retrieveRelatedCreditsForCharge(int pBan,
				double pChargeSeqNo) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String validatePayAndTalkSubscriberActivation(
				String applicationId, String userId,
				PrepaidConsumerAccountInfo prepaidConsumerAccountInfo,
				AuditHeader auditHeader) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SearchResultsInfo retrievePendingChargeHistory(int pBan,
				Date pFromDate, Date pToDate, char level, String pSubscriber,
				int maximum) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<TalkGroupInfo> retrieveTalkGroupsByBan(int ban)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveStatusChangeHistory(int ban, Date fromDate,
				Date toDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ContactDetailInfo getCustomerContactInfo(int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreditCheckResultInfo retrieveLastCreditCheckResultByBan(
				int ban, String productType) throws ApplicationException {
			this.isRetrieveLastCreditCheckResultByBanCalled=true;
			return null;
		}

		@Override
		public BillingPropertyInfo retrieveBillingInformation(
				int billingAccountNumber) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ContactPropertyInfo retrieveContactInformation(
				int billingAccountNumber) throws ApplicationException {
			// TODO Auto-generated method stub
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
		public AccountInfo retrieveLwAccountByBan(int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] retrieveSubscriberIdsByServiceFamily(
				AccountInfo account, String familyTypeCode, Date effectiveDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] retrieveSubscriberIdsByServiceFamily(int banId,
				String familyTypeCode, Date effectiveDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(
				AccountInfo account, String[] dataSharingGroupCodes,
				Date effectiveDate) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(
				int banId, String[] dataSharingGroupCodes, Date effectiveDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public SubscriberEligibilitySupportingInfo getSubscriberEligiblitySupportingInfo(
				int ban, String[] memoTypeList, Date dateFrom, Date dateTo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveRelatedCreditsForChargeList(
				List chargeIdentifierInfoList) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String retrieveChangedSubscriber(int ban, String subscriberId,
				String productType, Date searchFromDate, Date searchToDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAdjustedAmounts(int ban,
				String adjustmentReasonCode, String subscriberId,
				Date searchFromDate, Date searchToDate)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveApprovedCreditByFollowUpId(int banId, int followUpId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreditInfo retrieveCreditById(int banId, int entSeqNo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public InternationalServiceEligibilityCheckResultInfo checkInternationalServiceEligibility(int ban) 
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreditCheckResultDeposit[] retrieveDepositsByBan(int ban) 
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveLatestAccountsByPhoneNumber(String phoneNumber,
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByPhoneNumber(String phoneNumber,
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveLwAccountByPhoneNumber(String phoneNumber,
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getNextSeatGroupId() throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List getMaxVoipLineList(int ban, long subscriptionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void createMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateMaxVoipLine(List maxVoipLineInfoList)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.telus.cmb.account.informationhelper.svc.AccountInformationHelper#retrieveSubscriberDataSharingInfoList(int, java.lang.String[])
		 */
		@Override
		public SubscriberDataSharingDetailInfo[] retrieveSubscriberDataSharingInfoList(
				int arg0, String[] arg1) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveHwAccountByBan(int ban) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveHwAccountByBan(int ban, int retrievalOption) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveAccountByBan(int ban, int retrievalOption) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public AccountInfo retrieveHwAccountByPhoneNumber(String phoneNumber,
				PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List retrieveAccountListByImsi(String imsi) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class TestCreditCheckDao implements CreditCheckDao {

		private int ban;
		private String sessionId;
		String newCreditClass=null;

		@Override
		public void saveCreditCheckInfo(AccountInfo pAccountInfo,
				CreditCheckResultInfo pCreditCheckResultInfo,
				String pCreditParamType, ConsumerNameInfo pConsumerNameInfo,
				AddressInfo pAddressInfo, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo,
				BusinessCreditIdentityInfo[] listOfBusinesses,
				BusinessCreditIdentityInfo selectedBusiness,
				CreditCheckResultInfo pCreditCheckResultInfo,
				String pCreditParamType, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateCreditCheckResult(int pBan, String pCreditClass,
				CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo,
				String pDepositChangedReasonCode, String pDepositChangeText,
				String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateCreditProfile(int ban, String newCreditClass,
				double newCreditLimit, String memoText, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateCreditClass(int ban, String newCreditClass,
				String memoText, String sessionId) throws ApplicationException {
			this.ban=ban;                       
			this.newCreditClass=newCreditClass; 

		}

		@Override
		public CreditCheckResultInfo retrieveAmdocsCreditCheckResult(int ban,
				String sessionId) throws ApplicationException {
			this.ban = ban;
			this.sessionId = sessionId;
			return null;
		}

	}

	private static class TestUserDao implements UserDao {

		private static final String THROW_EXCEPTION_SESSION_ID = "EXCEPTION_THROWING";
		private String userId;
		private String oldPassword;
		private String newPassword;
		private String sessionId;

		@Override
		public void changeKnowbilityPassword(String userId, String oldPassword,
				String newPassword, String sessionId)
		throws ApplicationException {
			this.userId = userId;
			this.oldPassword = oldPassword;
			this.newPassword = newPassword;
			this.sessionId = sessionId;

			if (sessionId == THROW_EXCEPTION_SESSION_ID) {
				throw new ApplicationException("TEST_SYSTEM", userId, "Exception", "Exception_FR");
			}

		}

		@Override
		public String getUserProfileID(String sessionId)
				throws ApplicationException {
			this.sessionId = sessionId;
			return null;
		}

	}
//
//	private static class TestPrepaidDao implements PrepaidDao {
//		boolean flag= false;
//		public String retValue;
//		private String ban;
//		private String MDN;
//		private boolean existingAutoTopUp;
//		private String phoneNumber;
//		public String applicationId;
//		public ActivationTopUpInfo activationTopUpInfo;
//		public PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo;
//
//		@Override
//		public String adjustBalance(String pUserId, CreditInfo pCreditInfo)
//		throws ApplicationException {
//			retValue = "adjustBalance(String, CreditInfo), " + pCreditInfo.getAmount() + ", " + pCreditInfo.getReasonCode(); 
//			return retValue;
//		}
//
//		@Override
//		public String adjustBalance(String pUserId, ChargeInfo pChargeInfo)
//		throws ApplicationException {
//			retValue = "adjustBalance(String, ChargeInfo), " + pChargeInfo.getAmount() + ", " + pChargeInfo.getReasonCode(); 
//			return retValue;
//		}
//
//		@Override
//		public String adjustBalance(int pBan, String pPhoneNumber,
//				String pUserId, double pAmount, String pReasonCode,
//				String pTransactionId) throws ApplicationException {
//			retValue = "adjustBalance(int, String, String, double, String, String), " + pReasonCode; 
//			return retValue;
//		}
//
//		@Override
//		public void updateAccountPIN(int banId, String MDN, String serialNo,
//				String prevPIN, String PIN, String user) {
//			flag=true;			
//		}
//		public boolean returnFlag() {
//			return flag;
//		}
//
//		@Override
//		public void updateTopupCreditCard(String banId, String MDN,
//				String serialNo, CreditCardInfo creditCard, String user,
//				boolean encrypted) {
//			this.ban=banId;
//			this.MDN=MDN;
//		}
//		
//		@Override
//		public void updateAutoTopUp(int ban, String phoneNumber,
//				AutoTopUpInfo autoTopUpInfo, String userId,
//				boolean existingAutoTopUp, boolean existingThresholdRecharge) {
//			this.ban=String.valueOf(ban);
//			this.existingAutoTopUp=existingAutoTopUp;
//			this.phoneNumber=autoTopUpInfo.getPhoneNumber();
//		}
//
//		@Override
//		public String applyTopUp(int pBan, String pPhoneNumber,
//				CardInfo pAirtimeCard, String pUserId, String pApplicationId)
//		throws ApplicationException {
//
//			return "applyTopUp";
//		}
//
//		@Override
//		public String applyTopUpWithCreditCard(int pBan, String pPhoneNumber,
//				double pAmount, String pUserId, String pApplicationId)
//		throws ApplicationException {
//
//			return "applyTopUpWithCreditCard";
//		}
//
//		@Override
//		public void applyTopUpWithDebitCard(int pBan, String pPhoneNumber,
//				double pAmount, String pOrderNumber, String pUserId,
//				String pApplicationId) throws ApplicationException {
//
//		}
//
//		@Override
//		public void saveActivationTopUpArrangement(String banId, String MDN,
//				String serialNo,
//				ActivationTopUpArrangement activationTopUpArrangement,
//				String user) throws ApplicationException {
//			this.ban=banId;
//			this.MDN=MDN;
//		}
//
//		@Override
//		public void applyCreditForFeatureCard(CardInfo cardInfo,
//				ServiceInfo[] cardServices, String userId) {
//		}
//		
//
//		@Override
//		public void creditSubscriberMigration(String applicationId,
//				String pUserId, ActivationTopUpInfo activationTopUpInfo,
//				String phoneNumber, String esn, String provinceCode,
//				PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo)
//		throws ApplicationException {		
//			this.applicationId=applicationId;
//			this.activationTopUpInfo=activationTopUpInfo;
//			this.pPrepaidConsumerAccountInfo=pPrepaidConsumerAccountInfo;
//		}
//
//		@Override
//		public void removeTopupCreditCard(String MDN)
//		throws ApplicationException {
//			this.MDN=MDN;
//		}
//
//		@Override
//		public void updateTopupCreditCard(String banId, String MDN,
//				CreditCardInfo creditCard, String user, boolean encrypted) {
//			this.ban=banId;
//			this.MDN=MDN;
//		}
//
//		@Override
//		public void saveActivationTopUpArrangement(String billingAccountNumber,
//				String phoneNumber, String serialNo,
//				PaymentCard[] paymentCards, String user)
//				throws ApplicationException {
//			// TODO Auto-generated method stub
//			
//		}
//		**/
////		/*
////		 * TODO 
////		 * Remove this method after Jan/2014 release Surepay Retirement
////		 */
////		@Override
////		public TestPointResultInfo test() {
////			// TODO Auto-generated method stub
////			return null;
////		}
////
////
////	}

	private static class TestPaymentDao implements PaymentDao{
		int ban;
		int paymentSeq;
		String reasonCode;
		String memoText;
		boolean isManual;
		String authorizationCode;
		@Override
		public void applyPaymentToAccount(PaymentInfo pPaymentInfo,
				String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void changePaymentMethodToRegular(int pBan, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public PaymentMethodInfo updatePaymentMethod(int pBan,
				 PaymentMethodInfo pPaymentMethodInfo, String sessionId)
		throws ApplicationException {
			return null;

		}

		@Override
		public void updateTransferPayment(int ban, int seqNo,
				PaymentTransfer[] paymentTransfers, boolean allowOverPayment,
				String memonText, String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void refundPaymentToAccount(int ban, int paymentSeq,
				String reasonCode, String memoText, boolean isManual,
				String authorizationCode, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.paymentSeq=paymentSeq;
			this.reasonCode=reasonCode;
			this.memoText=memoText;
			this.isManual=isManual;
			this.authorizationCode=authorizationCode;

		}

	}
	private static class TestSubscriberDao implements SubscriberDao {

		private int ban;
		private Date activityDate;
		private String activityReasonCode;
		private char depositReturnMethod;
		private String[] subscriberId;
		private String[] waiveReason;
		private String userMemoText;
		private String sessionId;
		private String restoreComment;
		private Date restoreDate;
		private String restoreReasonCode;

		@Override
		public void cancelSubscribers(int ban, Date activityDate,
				String activityReasonCode, char depositReturnMethod,
				String[] subscriberId, String[] waiveReason,
				String userMemoText, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.activityDate = activityDate;
			this.activityReasonCode = activityReasonCode;
			this.depositReturnMethod = depositReturnMethod;
			this.subscriberId = subscriberId;
			this.waiveReason = waiveReason;
			this.userMemoText = userMemoText;
			this.sessionId = sessionId;
		}

		@Override
		public void suspendSubscribers(int ban, Date activityDate,
				String activityReasonCode, String[] subscriberId,
				String userMemoText, String sessionId)
		throws ApplicationException {

			this.ban = ban;
			this.activityDate = activityDate;
			this.activityReasonCode = activityReasonCode;
			this.subscriberId = subscriberId;
			this.userMemoText = userMemoText;
			this.sessionId = sessionId;
		}

		@Override
		public void restoreSuspendedSubscribers(int ban, Date restoreDate,
				String restoreReasonCode, String[] subscriberId,
				String restoreComment, String sessionId)
		throws ApplicationException {

			this.ban = ban;
			this.restoreDate = restoreDate;
			this.restoreReasonCode = restoreReasonCode;
			this.subscriberId = subscriberId;
			this.restoreComment = restoreComment;
			this.sessionId = sessionId;

		}


	}

	private static class TestCollectionDao implements CollectionDao {

		private int ban;
		private String sessionId;
		private int stepNumber;
		private Date stepDate;
		private String pathCode;

		@Override
		public CollectionStateInfo retrieveBanCollectionInfo(int ban,
				String sessionId) throws ApplicationException {
			this.ban = ban;
			this.sessionId = sessionId;
			return null;
		}

		@Override
		public void updateNextStepCollection(int ban, int stepNumber,
				Date stepDate, String pathCode, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.stepNumber = stepNumber;
			this.stepDate = stepDate;
			this.pathCode = pathCode;
			this.sessionId = sessionId;

		}

	}

	private static class TestInvoiceDao implements InvoiceDao {

		private int ban;
		private InvoicePropertiesInfo invoicePropertiesInfo;
		private String sessionId;
		private boolean suppressBill;
		private Date effectiveDate;
		private Date expiryDate;
		private boolean returnEnvelopeRequested;
		String calledMethod=null;
		int clientID;
		BillNotificationContactInfo[] billNotificationContact= null;
		private String subscriptionType;
		private String phoneNumber;

		@Override
		public void updateInvoiceProperties(int ban,
				InvoicePropertiesInfo invoicePropertiesInfo, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.invoicePropertiesInfo = invoicePropertiesInfo;
			this.sessionId = sessionId;
			
		}

		@Override
		public void updateBillSuppression(int ban, boolean suppressBill,
				Date effectiveDate, Date expiryDate, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.suppressBill = suppressBill;
			this.effectiveDate = effectiveDate;
			this.expiryDate = expiryDate;
			this.sessionId = sessionId;			
		}

		public int getBan() {
			return ban;
		}

		@Override
		public void updateInvoiceSuppressionIndicator(int ban, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.sessionId = sessionId;			
		}

		@Override
		public void updateReturnEnvelopeIndicator(int ban,
				boolean returnEnvelopeRequested, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.returnEnvelopeRequested = returnEnvelopeRequested;
			this.sessionId = sessionId;			
		}	


		@Override
		public void saveEBillRegistrationReminder(int ban, String phoneNumber,
				String pRecipientPhoneNumber, String applicationCode) {
			this.ban=ban;
			this.phoneNumber= phoneNumber;			
		}

		@Override
		public void hasEPostFalseNotificationTypeNotEPost(int ban,
				long portalUserID,
				BillNotificationContactInfo[] billNotificationContact,
				String applicationCode) {
			this.ban=ban;
			this.calledMethod="1";
			this.billNotificationContact= billNotificationContact;

		}

		@Override
		public void hasEPostFalseNotificationTypeEPost(int ban,
				long portalUserID,
				BillNotificationContactInfo[] billNotificationContact,
				String applicationCode) {
			this.ban=ban;
			this.calledMethod="2";
			this.billNotificationContact= billNotificationContact;
		}

		@Override
		public void hasEPostNotificationTypeNotEPost(int ban,
				long portalUserID,
				BillNotificationContactInfo[] billNotificationContact,
				String applicationCode) {
			this.ban=ban;
			this.calledMethod="3";
			this.billNotificationContact= billNotificationContact;
		}

		@Override
		public void hasEPostNotificationTypeEPost(int ban, long portalUserID,
				BillNotificationContactInfo[] billNotificationContact,
				String applicationCode) {
			this.ban=ban;
			this.calledMethod="4";
			this.billNotificationContact= billNotificationContact;
		}

	}

	private static class TestFollowUpDao implements FollowUpDao {

		private FollowUpUpdateInfo followUpUpdateInfo;
		private FollowUpInfo followUpInfo;
		private String sessionId;

		@Override
		public void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo,
				String sessionId) throws ApplicationException {
			this.followUpUpdateInfo = followUpUpdateInfo;
			this.sessionId = sessionId;			
		}

		@Override
		public void createFollowUp(FollowUpInfo followUpInfo, String sessionId)
		throws ApplicationException {
			this.followUpInfo = followUpInfo;		
			this.sessionId = sessionId;
		}		
	}

	private static class TestFleetDao implements FleetDao {

		private int ban;
		private short network;
		private FleetInfo fleetInfo;
		private int numberOfSubscibers;
		private String sessionId;
		private TalkGroupInfo talkGroupInfo;

		@Override
		public FleetInfo createFleet(int ban, short network,
				FleetInfo fleetInfo, int numberOfSubscribers, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.network = network;
			this.fleetInfo = fleetInfo;
			this.numberOfSubscibers = numberOfSubscribers;
			this.sessionId = sessionId;
			return null;
		}

		@Override
		public void addFleet(int ban, short network, FleetInfo fleetInfo,
				int numberOfSubscribers, String sessionId)
		throws ApplicationException {
			this.ban = ban;
			this.network = network;
			this.fleetInfo = fleetInfo;
			this.numberOfSubscibers = numberOfSubscribers;
			this.sessionId = sessionId;			
		}

		@Override
		public void dissociateFleet(int ban, FleetInfo fleetInfo,
				String sessionId) throws ApplicationException {
			this.ban = ban;			
			this.fleetInfo = fleetInfo;
			this.sessionId = sessionId;			
		}

		@Override
		public void removeTalkGroup(int ban, TalkGroupInfo talkGroupInfo,
				String sessionId) throws ApplicationException {
			this.ban = ban;			
			this.talkGroupInfo = talkGroupInfo;
			this.sessionId = sessionId;					
		}

		@Override
		public void addTalkGroup(int ban, TalkGroupInfo talkGroupInfo,
				String sessionId) throws ApplicationException {
			this.ban = ban;			
			this.talkGroupInfo = talkGroupInfo;
			this.sessionId = sessionId;		
		}

		@Override
		public TalkGroupInfo createTalkGroup(int ban,
				TalkGroupInfo talkGroupInfo, String sessionId)
		throws ApplicationException {
			this.ban = ban;			
			this.talkGroupInfo = talkGroupInfo;
			this.sessionId = sessionId;		
			return null;
		}

		@Override
		public void updateTalkGroup(int ban, TalkGroupInfo talkGroupInfo,
				String sessionId) throws ApplicationException {
			this.ban = ban;			
			this.talkGroupInfo = talkGroupInfo;
			this.sessionId = sessionId;
		}		
	}

	private static class TestAccountDao implements AccountDao {

		private int ban;
		private String sessionId;
		private String homeProvince;
		private String nationalGrowthIndicator;
		private String restoreReasonCode;
		private String restoreComment;
		private FutureStatusChangeRequestInfo fscri;
		private String activityReasonCode;
		private String userMemoText;
		private String specialInstructions;
		private int brandId;
		private boolean holdAutoTreatment;
		private ConsumerNameInfo[] authorizationNames;
		public String depositReturnMethod;
		public String waiveReason;
		private int urbanId;
		private int fleetId;
		private int talkGroupId;
		private AddressInfo addressInfo;
		private short prepaidBillCycle;
		private Date activityDate;
		private boolean portOutInd;
		private boolean isBrandPort;
		private boolean isPortActivity;


		@Override
		public void updateEmailAddress(int ban, String emailAddress,
				String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateBillCycle(int ban, short billCycle, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public int createAccount(AccountInfo pAccountInfo, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void updateBillParamsInfo(int banId,
				BillParametersInfo billParametersInfo, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void updateAccountPassword(int pBan, String pAccountPassword,
				String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public CancellationPenaltyInfo retrieveCancellationPenalty(int pBan,
				String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CancellationPenaltyInfo[] retrieveCancellationPenaltyList(
				int banId, String[] subscriberId, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void updateNationalGrowth(int ban,
				String nationalGrowthIndicator, String homeProvince,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.nationalGrowthIndicator=nationalGrowthIndicator;
			this.homeProvince=homeProvince;
			this.sessionId=sessionId;

		}

		@Override
		public void restoreSuspendedAccount(int ban, Date restoreDate,
				String restoreReasonCode, String restoreComment,
				boolean collectionSuspensionsOnly, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.restoreReasonCode=restoreReasonCode;
			this.restoreComment=restoreComment;
			this.sessionId=sessionId;

		}

		@Override
		public void updateFutureStatusChangeRequest(int ban,
				FutureStatusChangeRequestInfo futureStatusChangeRequestInfo,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.fscri=futureStatusChangeRequestInfo;
			this.sessionId=sessionId;

		}

		@Override
		public void suspendAccount(int ban, Date activityDate, String activityReasonCode, 
				String userMemoText,String sessionId) throws ApplicationException {
			this.ban=ban;
			this.activityReasonCode=activityReasonCode;
			this.userMemoText=userMemoText;
			this.sessionId=sessionId;
		}

		@Override
		public List<FutureStatusChangeRequestInfo> retrieveFutureStatusChangeRequests(
				int ban, String sessionId) throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			return null;
		}

		@Override
		public void updateAuthorizationNames(int ban,
				ConsumerNameInfo[] authorizationNames, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			this.authorizationNames=authorizationNames;
		}

		@Override
		public void updateAutoTreatment(int ban, boolean holdAutoTreatment,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			this.holdAutoTreatment=holdAutoTreatment;
		}

		@Override
		public void updateBrand(int ban, int brandId, String memoText,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			this.brandId=brandId;
		}

		@Override
		public void updateSpecialInstructions(int ban,
				String specialInstructions, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			this.specialInstructions=specialInstructions;
		}

		@Override
		public void cancelAccount(int ban, Date activityDate,
				String activityReasonCode, String depositReturnMethod,
				String waiveReason, String userMemoText, boolean isPortActivity, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.activityReasonCode=activityReasonCode;
			this.depositReturnMethod=depositReturnMethod;
			this.waiveReason=waiveReason;
			this.userMemoText=userMemoText;
			this.sessionId=sessionId;
			this.isPortActivity=isPortActivity;
		}

		@Override
		public int[] retrieveAccountsByTalkGroup(int urbanId, int fleetId,
				int talkGroupId, String sessionId) throws ApplicationException {
			this.urbanId = urbanId;
			this.fleetId = fleetId;
			this.talkGroupId = talkGroupId;
			this.sessionId = sessionId;			
			return null;
		}

		@Override
		public AddressValidationResultInfo validateAddress(
				AddressInfo addressInfo, String sessionId)
		throws ApplicationException {
			this.addressInfo = addressInfo;
			this.sessionId = sessionId;
			return null;
		}

		@Override
		public void changePostpaidConsumerToPrepaidConsumer(int ban,
				short prepaidBillCycle, String sessionId)
		throws ApplicationException {
			this.ban=ban;
			this.prepaidBillCycle=prepaidBillCycle;
			this.sessionId=sessionId;

		}

		@Override
		public List<DiscountInfo> retrieveDiscounts(int ban,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.sessionId=sessionId;
			return null;
		}

		@Override
		public void cancelAccountForPortOut(int ban, String activityReasonCode,
				Date activityDate, boolean portOutInd, boolean isBrandPort,
				String sessionId) throws ApplicationException {
			this.ban=ban;
			this.activityReasonCode=activityReasonCode;
			this.activityDate=activityDate;
			this.portOutInd=portOutInd;
			this.isBrandPort=isBrandPort;
			this.sessionId=sessionId;
		}

		@Override
		public void updateAccount(AccountInfo accountInfo, String sessionId)
		throws ApplicationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void suspendAccountForPortOut(int ban,
				String activityReasonCode, Date activityDate,
				String portOutInd, String sessionId)
		throws ApplicationException {

			this.ban=ban;
			this.portOutInd=portOutInd.equalsIgnoreCase("y")?true:false;
			this.sessionId=sessionId;

		}

		@Override
		public void updatePersonalCreditInformation(int ban,
				PersonalCreditInfo personalCreditInfo, String sessionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateBusinessCreditInformation(int ban,
				BusinessCreditInfo businessCreditInfo, String sessionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateAccount(AccountInfo accountInfo,
				boolean blockDirectUpdate, String sessionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<CustomerNotificationPreferenceInfo> getCustomerNotificationPreferenceList(int ban, 
				String sessionId) 
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void updateCustomerNotificationPreferenceList(int ban, 
				List<CustomerNotificationPreferenceInfo> notificationPreferenceList,
				String sessionId) 
				throws ApplicationException {
			// TODO Auto-generated method stub
		}
		@Override
		public void updateHotlineInd(int ban, boolean hotLineInd, String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub
		}
	}
//	private static class  TestLetterDao implements LetterDao{
//		private int ban;
//		private int requestNumber;
//		private String sessionId;
//		@Override
//		public void createManualLetterRequest(
//				LMSLetterRequestInfo letterRequestInfo, String sessionId)
//		throws ApplicationException {
//			this.ban=letterRequestInfo.getBanId();
//			this.sessionId=sessionId;
//		}
//
//		@Override
//		public void removeManualLetterRequest(int ban, int requestNumber,
//				String sessionId) throws ApplicationException {
//			this.ban=ban;
//			this.requestNumber= requestNumber;
//			this.sessionId=sessionId;
//		}
//
//		@Override
//		public void createManualLetterRequest(LMSRequestInfo lmsRequestInfo,
//				String sessionId) throws ApplicationException {
//			this.ban=lmsRequestInfo.getBan();  
//			this.sessionId=sessionId;          
//		}		
//	}

	private static class TestAdjustmentDao implements AdjustmentDao{

		int ban;
		String sessionId;
		private FeeWaiverInfo feeWaiverInfo;
		public boolean isOverride=false;
		public boolean isForSubscriber=false;

		@Override
		public List<FeeWaiverInfo> retrieveFeeWaivers(int banId,
				String sessionID) throws ApplicationException {
			this.ban=banId;
			this.sessionId= sessionID;
			return null;
		}

		@Override
		public void applyFeeWaiver(FeeWaiverInfo feeWaiver, String sessionId)
		throws ApplicationException {
			this.ban=feeWaiver.getBanId();
			this.feeWaiverInfo=feeWaiver;
			this.sessionId=sessionId;

		}

		@Override
		public void reverseCreditForSubscriber(CreditInfo creditInfo,
				String reversalReasonCode, String memoText,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=creditInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=true;
		}

		@Override
		public void reverseCreditForBan(CreditInfo creditInfo,
				String reversalReasonCode, String memoText,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=creditInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=false;
		}

		@Override
		public void deleteChargeForSubscriber(ChargeInfo chargeInfo,
				String deletionReasonCode, String memoText,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=true;
		}

		@Override
		public void deleteChargeForBan(ChargeInfo chargeInfo,
				String deletionReasonCode, String memoText,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=false;
		}

		@Override
		public double applyChargeToAccountForSubscriber(ChargeInfo chargeInfo,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=true;
			return 0;
		}

		@Override
		public double applyChargeToAccountForBan(ChargeInfo chargeInfo,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {

			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=false;
			return 0;
		}

		@Override
		public double adjustChargeForSubscriber(ChargeInfo chargeInfo,
				double adjustmentAmount, String adjustmentReasonCode,
				String pMemoText, boolean overrideThreshold, String sessionId)
		throws ApplicationException {
			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=true;
			return 0;
		}

		@Override
		public double adjustChargeForBan(ChargeInfo chargeInfo,
				double adjustmentAmount, String adjustmentReasonCode,
				String pMemoText, boolean overrideThreshold, String sessionId)
		throws ApplicationException {
			this.ban=chargeInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=false;
			return 0;
		}

		@Override
		public boolean applyCreditToAccountForSubscriber(CreditInfo creditInfo,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {
			this.ban=creditInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=true;
			return true;
		}

		@Override
		public boolean applyCreditToAccountForBan(CreditInfo creditInfo,
				boolean overrideThreshold, String sessionId)
		throws ApplicationException {
			this.ban=creditInfo.getBan();
			this.isOverride=overrideThreshold;
			this.isForSubscriber=false;
			return true;
		}

		@Override
		public void applyDiscountToAccountForSubscriber(
				DiscountInfo discountInfo, String sessionId)
		throws ApplicationException {
			this.ban=discountInfo.getBan();
			this.sessionId=sessionId;

		}

		@Override
		public void applyDiscountToAccountForBan(DiscountInfo discountInfo,
				String sessionId) throws ApplicationException {
			this.ban=discountInfo.getBan();
			this.sessionId=sessionId;

		}

		@Override
		public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccount(
				List<ChargeAdjustmentInfo> chargeInfoList, String sessionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriber(
				List<ChargeAdjustmentInfo> chargeInfoList, String sessionId)
				throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountWithTax(
				List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<ChargeAdjustmentInfo> applyChargesAndAdjustmentsToAccountForSubscriberWithTax(
				List<ChargeAdjustmentWithTaxInfo> chargeInfoList, String sessionId) throws ApplicationException {
			// TODO Auto-generated method stub
			return null;
		}

		
	}
	
	private static class TestContactDao implements ContactDao{
		int ban;
		String sessionId;

		@Override
		public void updateBillingInformation(int billingAccountNumber,
				BillingPropertyInfo billingPropertyInfo, String sessionId)
				throws ApplicationException {
			this.ban=billingAccountNumber;
			this.sessionId=sessionId;
			
		}

		@Override
		public void updateContactInformation(int billingAccountNumber,
				ContactPropertyInfo contactPropertyInfo, String sessionId)
				throws ApplicationException {
			this.ban=billingAccountNumber;
			this.sessionId=sessionId;
			
		}
	}

	@Test
	public void testRetrieveFutureStatusChangeRequests() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFutureStatusChangeRequests", int.class,String.class)
				, new Object[]{1,""}, 0);
		int ban = 1221;
		String sessionId = "safdasd32";
		impl.retrieveFutureStatusChangeRequests(ban, sessionId);
		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
	}
	@Test
	public void testRetrieveFeeWaivers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("retrieveFeeWaivers", int.class,String.class)
				, new Object[]{1,""}, 0);
		int ban = 1221;
		String sessionId = "safdasd32";
		impl.retrieveFeeWaivers(ban, sessionId);
		assertEquals(ban, adjustmentDao.ban);
		assertEquals(sessionId, adjustmentDao.sessionId);
	}
//	@Test
//	public void testCreateManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//
//		int ban = 1221;
//		LMSLetterRequestInfo lmsInfo = new LMSLetterRequestInfo();
//		lmsInfo.setBanId(ban);
//		String sessionId = "12321321";
//		impl.createManualLetterRequest(lmsInfo, sessionId);
//		assertEquals(ban, letterDao.ban);
//		assertEquals(sessionId, letterDao.sessionId);
//	}
//	@Test
//	public void testRemoveManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
//		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("removeManualLetterRequest", int.class, int.class, String.class)
//				, new Object[]{1, 1, ""}, 0);	
//		int ban = 1221;
//		int reqNum=2;
//		String sessionId = "12321321";
//		impl.removeManualLetterRequest(ban,reqNum, sessionId);
//		assertEquals(ban, letterDao.ban);
//		assertEquals(reqNum, letterDao.requestNumber);
//		assertEquals(sessionId, letterDao.sessionId);
//	}
	@Test
	public void testRemoveTalkGroup() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("removeTalkGroup", int.class, TalkGroupInfo.class, String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		String sessionId = "12321321";
		impl.removeTalkGroup(ban, talkGroupInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(talkGroupInfo, fleetDao.talkGroupInfo);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testDissociateFleet() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("dissociateFleet", int.class, FleetInfo.class, String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		FleetInfo fleetInfo = new FleetInfo();
		String sessionId = "12321321";
		impl.dissociateFleet(ban, fleetInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(fleetInfo, fleetDao.fleetInfo);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testAddFleet() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("addFleet", int.class, short.class, FleetInfo.class,int.class, String.class)
				, new Object[]{0, (short)0, null, 0, null}, 0);		
		int ban = 5;
		short network = (short)321;
		FleetInfo fleetInfo = new FleetInfo();
		int numberOfSubs = 10;
		String sessionId = "12321321";		
		impl.addFleet(ban, network, fleetInfo, numberOfSubs, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(network, fleetDao.network);
		assertEquals(fleetInfo, fleetDao.fleetInfo);
		assertEquals(numberOfSubs, fleetDao.numberOfSubscibers);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testCreateFleet() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("createFleet", int.class, short.class, FleetInfo.class,int.class, String.class)
				, new Object[]{0, (short)0, null, 0, null}, 0);		
		int ban = 5;
		short network = (short)321;
		FleetInfo fleetInfo = new FleetInfo();
		int numberOfSubs = 10;
		String sessionId = "12321321";		
		impl.createFleet(ban, network, fleetInfo, numberOfSubs, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(network, fleetDao.network);
		assertEquals(fleetInfo, fleetDao.fleetInfo);
		assertEquals(numberOfSubs, fleetDao.numberOfSubscibers);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testAddTalkGroups() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("addTalkGroups", int.class, Array.newInstance(TalkGroupInfo.class, 0).getClass(), String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		TalkGroupInfo[] talkGroupInfo = new TalkGroupInfo[1];
		talkGroupInfo[0] = new TalkGroupInfo();
		String sessionId = "12321321";
		impl.addTalkGroups(ban, talkGroupInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(talkGroupInfo[0], fleetDao.talkGroupInfo);
		assertEquals(sessionId, fleetDao.sessionId);

		talkGroupInfo = new TalkGroupInfo[2];
		talkGroupInfo[0] = new TalkGroupInfo();
		talkGroupInfo[0].setName("1");
		talkGroupInfo[1] = new TalkGroupInfo();
		talkGroupInfo[1].setName("2");		
		impl.addTalkGroups(ban, talkGroupInfo, sessionId);
		assertEquals(talkGroupInfo[1], fleetDao.talkGroupInfo);

		talkGroupInfo = new TalkGroupInfo[0];
	}

	@Test
	public void testAddTalkGroup() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("addTalkGroup", int.class, TalkGroupInfo.class, String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		String sessionId = "12321321";
		impl.addTalkGroup(ban, talkGroupInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(talkGroupInfo, fleetDao.talkGroupInfo);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testCreateTalkGroup() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("createTalkGroup", int.class, TalkGroupInfo.class, String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		String sessionId = "12321321";
		impl.createTalkGroup(ban, talkGroupInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(talkGroupInfo, fleetDao.talkGroupInfo);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testUpdateTalkGroup() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateTalkGroup", int.class, TalkGroupInfo.class, String.class)
				, new Object[]{0, null, null}, 0);		
		int ban = 5;
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		String sessionId = "12321321";
		impl.updateTalkGroup(ban, talkGroupInfo, sessionId);

		assertEquals(ban, fleetDao.ban);
		assertEquals(talkGroupInfo, fleetDao.talkGroupInfo);
		assertEquals(sessionId, fleetDao.sessionId);
	}

	@Test
	public void testCreateFollowUp() throws ApplicationException {
		String sessionId = "321321";
		FollowUpInfo followUpInfo = new FollowUpInfo();
		impl.createFollowUp(followUpInfo, sessionId);

		assertEquals(followUpInfo,followUpDao.followUpInfo);
		assertEquals(sessionId, followUpDao.sessionId);
	}

	@Test
	public void testUpdateFollowUp() throws ApplicationException {
		String sessionId = "321321abc";
		FollowUpUpdateInfo followUpUpdateInfo = new FollowUpUpdateInfo();
		impl.updateFollowUp(followUpUpdateInfo, sessionId);

		assertEquals(followUpUpdateInfo,followUpDao.followUpUpdateInfo);
		assertEquals(sessionId, followUpDao.sessionId);
	}

	@Test
	public void testUpdateBillSuppression() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateBillSuppression", int.class,boolean.class, Date.class, Date.class, String.class)
				, new Object[]{0, false, null, null, null}, 0);	

		int ban = 1;
		boolean suppressBill = true;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -10);
		Date effectiveDate = cal.getTime();
		cal.add(Calendar.YEAR, 20);
		Date expiryDate = cal.getTime();
		String sessionId = "32132aabc";
		impl.updateBillSuppression(ban, suppressBill, effectiveDate, expiryDate, sessionId);

		assertEquals(ban, invoiceDao.ban);
		assertEquals(suppressBill, invoiceDao.suppressBill);
		assertEquals(effectiveDate, invoiceDao.effectiveDate);
		assertEquals(expiryDate, invoiceDao.expiryDate);
		assertEquals(sessionId, invoiceDao.sessionId);

		cal.add(Calendar.YEAR, 50);
		try {
			impl.updateBillSuppression(ban, true, cal.getTime(), expiryDate, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals("VAL10008", ae.getErrorCode());
			assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
		}

		impl.updateBillSuppression(ban, false, cal.getTime(), expiryDate, sessionId);		
	}

	@Test
	public void testUpdateInvoiceSuppressionIndicator() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateInvoiceSuppressionIndicator", int.class, String.class)
				, new Object[]{0, null}, 0);	
		int ban = 321;
		String sessionId = "jlkfda";
		impl.updateInvoiceSuppressionIndicator(ban, sessionId);

		assertEquals(ban, invoiceDao.ban);
		assertEquals(sessionId, invoiceDao.sessionId);
	}

	@Test
	public void testUpdateReturnEnvelopIndicator() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateReturnEnvelopeIndicator", int.class, boolean.class, String.class)
				, new Object[]{0, false, null}, 0);	

		int ban = 31122;
		boolean returnEnvelopeRequested = false;
		String sessionId = "321321";
		impl.updateReturnEnvelopeIndicator(ban, returnEnvelopeRequested, sessionId);

		assertEquals(ban, invoiceDao.ban);
		assertEquals(returnEnvelopeRequested, invoiceDao.returnEnvelopeRequested);
		assertEquals(sessionId, invoiceDao.sessionId);
	}

	@Test	
	public void testupdateNationalGrowth() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateNationalGrowth", int.class, String.class, String.class,String.class)
				, new Object[]{0, "", "",""}, 0);	

		int ban = 2261;
		String sessionId = "123456";
		String nationalGrowthIndicator="Simple";
		String homeProvince="Test";

		impl.updateNationalGrowth(ban, nationalGrowthIndicator, homeProvince, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(nationalGrowthIndicator+homeProvince, accountDao.nationalGrowthIndicator+accountDao.homeProvince);

	}

	@Test	
	public void testrestoreSuspendedAccount1() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("restoreSuspendedAccount", int.class, String.class, String.class)
				, new Object[]{0, null, null}, 0);	

		int ban = 2262;
		String sessionId = "456123";
		String restoreReasonCode="SimpleTest";

		impl.restoreSuspendedAccount(ban, restoreReasonCode, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(restoreReasonCode, accountDao.restoreReasonCode);
	}

	@Test	
	public void testrestoreSuspendedAccount2() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("restoreSuspendedAccount", int.class,java.util.Date.class, String.class, String.class,boolean.class,String.class)
				, new Object[]{0, null,null, null,false,null}, 0);	

		int ban = 2263;
		String sessionId = "135246";
		Date restoreDate= new Date();
		String restoreComment="Simple";
		boolean collectionSuspensionsOnly = false;
		String restoreReasonCode="Test";

		impl.restoreSuspendedAccount(ban, restoreDate, restoreReasonCode, restoreComment, collectionSuspensionsOnly, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(restoreComment+restoreReasonCode, accountDao.restoreComment+accountDao.restoreReasonCode);
	}

	@Test
	public void testupdateFutureStatusChangeRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateFutureStatusChangeRequest", int.class, FutureStatusChangeRequestInfo.class, String.class)
				, new Object[]{0, null, null}, 0);	

		int ban = 2264;
		String sessionId = "246135";
		FutureStatusChangeRequestInfo futureStatusChangeRequestInfo=new FutureStatusChangeRequestInfo();
		futureStatusChangeRequestInfo.setActivityCode("abcdef");

		impl.updateFutureStatusChangeRequest(ban, futureStatusChangeRequestInfo, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals("abcdef", accountDao.fscri.getActivityCode());
	}

	@Test
	public void testCancelSubscribers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("cancelSubscribers"
						, int.class, Date.class,String.class, char.class, Array.newInstance(String.class, 1).getClass(), 
						Array.newInstance(String.class, 1).getClass(), String.class, String.class)
						, new Object[]{0, null, null, 'A', new String[0], new String[0], null, null}, 0);	

		int ban = 321;
		Date activityDate = new Date();
		String activityReasonCode = "321321";
		char depositReturnMethod = 'A';
		String[] subscriberId = new String[1];
		String[] waiveReason = new String[1];
		String userMemoText = "321321";
		String sessionId = "fdajklfdsa";
		impl.cancelSubscribers(ban, activityDate, activityReasonCode, depositReturnMethod
				, subscriberId, waiveReason, userMemoText, sessionId);

		assertEquals(ban, subscriberDao.ban);
		assertEquals(activityDate, subscriberDao.activityDate);
		assertEquals(activityReasonCode, subscriberDao.activityReasonCode);
		assertEquals(depositReturnMethod, subscriberDao.depositReturnMethod);
		Assert.assertArrayEquals(subscriberId, subscriberDao.subscriberId);
		Assert.assertArrayEquals(waiveReason, subscriberDao.waiveReason);
		assertEquals(userMemoText, subscriberDao.userMemoText);
		assertEquals(sessionId, subscriberDao.sessionId);

		subscriberId = new String[0];
		waiveReason = new String[1];

		try {
			impl.cancelSubscribers(ban, activityDate, activityReasonCode, depositReturnMethod
					, subscriberId, waiveReason, userMemoText, sessionId);
			fail ("Exception expected.");
		} catch (ApplicationException ae) {

		}

		try {
			impl.cancelSubscribers(ban, activityDate, activityReasonCode, depositReturnMethod
					, null, waiveReason, userMemoText, sessionId);
			fail ("Exception expected.");
		} catch (ApplicationException ae) {

		}

		try {
			impl.cancelSubscribers(ban, activityDate, activityReasonCode, depositReturnMethod
					, subscriberId, null, userMemoText, sessionId);
			fail ("Exception expected.");
		} catch (ApplicationException ae) {

		}
	}
	
	@Test
	public void testGetUserProfileId() throws ApplicationException {
		String sessionId = "1234";
		impl.getUserProfileID(sessionId);
		assertEquals(sessionId, userDao.sessionId);
	}

	@Test
	public void testChangeKnowbilityPassword() throws ApplicationException {
		String userId = "userId";
		String oldPassword = "oldPassword";
		String newPassword = "newPassword";
		String sessionId = "sessionId";
		impl.changeKnowbilityPassword(userId, oldPassword, newPassword, sessionId);

		assertEquals(userId, userDao.userId);
		assertEquals(oldPassword, userDao.oldPassword);
		assertEquals(newPassword, userDao.newPassword);
		assertEquals(sessionId, userDao.sessionId);

		try {
			impl.changeKnowbilityPassword("1118020", oldPassword, newPassword, TestUserDao.THROW_EXCEPTION_SESSION_ID);
			fail ("Exception expected.");
		} catch(ApplicationException ae) {
			assertEquals("1118020", ae.getErrorCode());
		}
		try {
			impl.changeKnowbilityPassword("1118021", oldPassword, newPassword, TestUserDao.THROW_EXCEPTION_SESSION_ID);
			fail ("Exception expected.");
		} catch(ApplicationException ae) {
			assertEquals("1118021", ae.getErrorCode());
		}
		try {
			impl.changeKnowbilityPassword("1118022", oldPassword, newPassword, TestUserDao.THROW_EXCEPTION_SESSION_ID);
			fail ("Exception expected.");
		} catch(ApplicationException ae) {
			assertEquals("1118022", ae.getErrorCode());
		}
		try {
			impl.changeKnowbilityPassword("1118023", oldPassword, newPassword, TestUserDao.THROW_EXCEPTION_SESSION_ID);
			fail ("Exception expected.");
		} catch(ApplicationException ae) {
			assertEquals("1118023", ae.getErrorCode());
		}
		try {
			impl.changeKnowbilityPassword("1118024", oldPassword, newPassword, TestUserDao.THROW_EXCEPTION_SESSION_ID);
			fail ("Exception expected.");
		} catch(ApplicationException ae) {
			assertEquals("1118024", ae.getErrorCode());
		}


	}


	@Test	
	public void testSuspendAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("suspendAccount", int.class, Date.class, String.class, String.class, String.class)
				, new Object[]{0, new Date(), null,null,null}, 0);	

		int ban = 82;
		String userMemoText="memo input test";
		String activityReasonCode="NB";
		Date activityDate=new Date();
		String sessionId = "246135";

		impl.suspendAccount(ban, activityDate, activityReasonCode, userMemoText, sessionId);

		assertEquals(82, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(userMemoText, accountDao.userMemoText);
		assertEquals(activityReasonCode, accountDao.activityReasonCode);

	}

	@Test
	public void testupdateAuthorizationNames() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateAuthorizationNames", int.class, ConsumerNameInfo[].class, String.class)
				, new Object[]{0, null, null}, 0);

		int ban = 123;
		String sessionId = "246135";
		ConsumerNameInfo[] authorizationNames = new ConsumerNameInfo[1];
		authorizationNames[0]=new ConsumerNameInfo();
		authorizationNames[0].setFirstName("CustomerName");

		impl.updateAuthorizationNames(ban, authorizationNames, sessionId);

		assertEquals(123, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals("CUSTOMERNAME", accountDao.authorizationNames[0].getFirstName());
	}

	@Test
	public void testupdateAutoTreatment() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateAutoTreatment", int.class, boolean.class, String.class)
				, new Object[]{0, false, null}, 0);
		int ban = 456;
		String sessionId = "246135";
		boolean holdAutoTreatment=false;

		impl.updateAutoTreatment(ban, holdAutoTreatment, sessionId);

		assertEquals(456, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(holdAutoTreatment,accountDao.holdAutoTreatment);
	}

	@Test
	public void testupdateBrand() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateBrand", int.class, int.class, String.class, String.class)
				, new Object[]{0, 0, null, null}, 0);
		int ban = 789;
		String sessionId = "246135";
		int brandId=0;
		String memoText="SimpleTest";

		impl.updateBrand(ban, brandId, memoText, sessionId);

		assertEquals(789, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(brandId,accountDao.brandId);
	}

	@Test
	public void testupdateSpecialInstructions() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateSpecialInstructions", int.class, String.class, String.class)
				, new Object[]{0, null, null}, 0);
		int ban = 3456;
		String sessionId = "246135";
		String specialInstructions="";

		impl.updateSpecialInstructions(ban, specialInstructions, sessionId);

		assertEquals(3456, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(specialInstructions,accountDao.specialInstructions);
	}


	@Test	
	public void testCancelAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("cancelAccount", int.class, Date.class, String.class, String.class,String.class,String.class, Boolean.class, String.class)
				, new Object[]{0, new Date(), null, null, null, null, false, null}, 0);

		int ban = 82;
		String userMemoText="memo input test";
		String activityReasonCode="NB";
		Date activityDate=new Date();
		String sessionId = "246135";

		String depositReturnMethod="R";
		String waiveReason="waive";
		impl.cancelAccount(ban, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, false,sessionId);

		assertEquals(82, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(userMemoText, accountDao.userMemoText);
		assertEquals(activityReasonCode, accountDao.activityReasonCode);
		assertEquals(depositReturnMethod, accountDao.depositReturnMethod);
		assertEquals(waiveReason, accountDao.waiveReason);

	}

	@Test
	public void testapplyFeeWaiver() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		String sessionId = "246135";
		FeeWaiverInfo feeWaiverInfo=new FeeWaiverInfo();
		feeWaiverInfo.setBanId(234523);
		feeWaiverInfo.setReasonCode("ABC");

		impl.applyFeeWaiver(feeWaiverInfo, sessionId);

		assertEquals(234523, adjustmentDao.ban);
		assertEquals(sessionId, adjustmentDao.sessionId);
		assertEquals("ABC",adjustmentDao.feeWaiverInfo.getReasonCode());
	}

	@Test
	public void testValidateAddress() throws ApplicationException {
		AddressInfo addressInfo = new AddressInfo();
		String sessionId = "321";
		impl.validateAddress(addressInfo, sessionId);

		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(addressInfo, accountDao.addressInfo);
	}

	@Test
	public void testRetrieveAmdocsCreditCheckResultByBan() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException {
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveAmdocsCreditCheckResultByBan", int.class, String.class)
				, new Object[]{0, null}, 0);

		int ban = 321;
		String sessionId = "321";
		impl.retrieveAmdocsCreditCheckResultByBan(ban, sessionId);
		assertEquals(sessionId, creditCheckDao.sessionId);
		assertEquals(ban, creditCheckDao.ban);
	}

	@Test
	public void testRetrieveAccountsByTalkGroup() throws ApplicationException {
		int urbanId = 321;
		int fleetId = 123;
		int talkGroupId = 321321;
		String sessionId = "321321";
		impl.retrieveAccountsByTalkGroup(urbanId, fleetId, talkGroupId, sessionId);

		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(urbanId, accountDao.urbanId);
		assertEquals(fleetId, accountDao.fleetId);
		assertEquals(talkGroupId, accountDao.talkGroupId);

		assertTrue(accountInformationHelper.retrieveAccountsByBanCalled);
	}

	@Test
	public void testchangePostpaidConsumerToPrepaidConsumer() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("changePostpaidConsumerToPrepaidConsumer", int.class, String.class)
				, new Object[]{0, null}, 0);

		int ban = 234523;
		String sessionId = "321321";

		impl.changePostpaidConsumerToPrepaidConsumer(ban, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertTrue(accountInformationHelper.isRetrieveAccountsByBanCalled);
	}

	@Test
	public void testretrieveDiscounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveDiscounts", int.class, String.class)
				, new Object[]{0, null}, 0);

		int ban = 5423454;
		String sessionId = "345632";

		impl.retrieveDiscounts(ban, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
	}

	@Test
	public void testcancelAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("cancelAccountForPortOut", int.class, String.class, Date.class, String.class,boolean.class,String.class)
				, new Object[]{0, null,null,null,false, null}, 0);

		int ban = 768674;
		String sessionId = "687541";
		String activityReasonCode="";
		Date activityDate=new Date();
		String portOutInd="Y";
		boolean isBrandPort=false;

		impl.cancelAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, isBrandPort, sessionId);

		assertEquals(ban, accountDao.ban);
		assertEquals(sessionId, accountDao.sessionId);
		assertEquals(activityReasonCode, accountDao.activityReasonCode);
		assertEquals(activityDate, accountDao.activityDate);
		assertEquals(true, accountDao.portOutInd);
		assertEquals(isBrandPort, accountDao.isBrandPort);
	}

	@Test
	public void testreverseCredit() throws ApplicationException{

		String sessionId = "687541";
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(23454354);
		String reversalReasonCode = "";
		String memoText="";

		creditInfo.setSubscriberId("423534");
		creditInfo.setProductType("ABC");
		impl.reverseCredit(creditInfo, reversalReasonCode, memoText, sessionId);
		assertEquals(23454354, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		creditInfo.setSubscriberId(null);
		creditInfo.setProductType(null);
		impl.reverseCredit(creditInfo, reversalReasonCode, memoText, sessionId);
		assertEquals(23454354, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);

	}

	@Test
	public void testreverseCreditWithOverride() throws ApplicationException{

		String sessionId = "687541";
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(23454354);
		String reversalReasonCode = "";
		String memoText="";

		creditInfo.setSubscriberId("423534");
		creditInfo.setProductType("ABC");
		impl.reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, sessionId);
		assertEquals(23454354, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		creditInfo.setSubscriberId(null);
		creditInfo.setProductType(null);
		impl.reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, sessionId);
		assertEquals(23454354, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}

	@Test
	public void testdeleteCharge() throws ApplicationException{

		String sessionId = "754234";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		String deletionReasonCode="";
		String memoText="";

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.deleteCharge(chargeInfo, deletionReasonCode, memoText, sessionId);
		assertEquals(5646323, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.deleteCharge(chargeInfo, deletionReasonCode, memoText, sessionId);
		assertEquals(5646323, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);

	}

	@Test
	public void testdeleteChargeWithOverride() throws ApplicationException{

		String sessionId = "754234";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		String deletionReasonCode="";
		String memoText="";

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.deleteChargeWithOverride(chargeInfo, deletionReasonCode, memoText, sessionId);
		assertEquals(5646323, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.deleteChargeWithOverride(chargeInfo, deletionReasonCode, memoText, sessionId);
		assertEquals(5646323, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}
	@Test
	public void testApplyCreditToAccount() throws ApplicationException{

		String sessionId = "234678";
		CreditInfo creditInfo=new CreditInfo();
		creditInfo.setBan(67743992);

		creditInfo.setSubscriberId("423534");
		creditInfo.setProductType("ABC");
		impl.applyCreditToAccount(creditInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		creditInfo.setSubscriberId(null);
		creditInfo.setProductType(null);
		impl.applyCreditToAccount(creditInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}
	@Test
	public void testApplyCreditToAccountWithOverride() throws ApplicationException{

		String sessionId = "234678";
		CreditInfo creditInfo=new CreditInfo();
		creditInfo.setBan(67743992);

		creditInfo.setSubscriberId("423534");
		creditInfo.setProductType("ABC");
		impl.applyCreditToAccountWithOverride(creditInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		creditInfo.setSubscriberId(null);
		creditInfo.setProductType(null);
		impl.applyCreditToAccountWithOverride(creditInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}
	@Test
	public void testapplyChargeToAccount() throws ApplicationException{

		String sessionId = "234678";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(67743992);

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.applyChargeToAccount(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.applyChargeToAccount(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}

	@Test
	public void testapplyChargeToAccountWithOverride() throws ApplicationException{

		String sessionId = "234678";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(67743992);

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.applyChargeToAccountWithOverride(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.applyChargeToAccountWithOverride(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}
	@Test
	public void testAdjustCharge() throws ApplicationException{

		String sessionId = "234678";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(67743992);

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.applyChargeToAccount(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.adjustCharge(chargeInfo,1.2,"","",sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertFalse(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}

	@Test
	public void testAdjustChargeWithOverride() throws ApplicationException{

		String sessionId = "234678";
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(67743992);

		chargeInfo.setSubscriberId("423534");
		chargeInfo.setProductType("ABC");
		impl.adjustChargeWithOverride(chargeInfo,1.2,"","",sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertTrue(adjustmentDao.isForSubscriber);

		chargeInfo.setSubscriberId(null);
		chargeInfo.setProductType(null);
		impl.applyChargeToAccountWithOverride(chargeInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
		assertTrue(adjustmentDao.isOverride);
		assertFalse(adjustmentDao.isForSubscriber);
	}
	@Test
	public void testApplyDiscountToAccount() throws ApplicationException{

		String sessionId = "234678";
		DiscountInfo discountInfo=new DiscountInfo();
		discountInfo.setBan(67743992);

		discountInfo.setSubscriberId("423534");
		discountInfo.setProductType("ABC");
		impl.applyDiscountToAccount(discountInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);

		discountInfo.setSubscriberId(null);
		discountInfo.setProductType(null);
		impl.applyDiscountToAccount(discountInfo, sessionId);
		assertEquals(67743992, adjustmentDao.ban);
	}


	@Test
	public void testSuspendSubscribers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("suspendSubscribers" , int.class, Date.class,String.class 
						, Array.newInstance(String.class, 1).getClass(), String.class, String.class)
						, new Object[]{0, new Date(), null, new String[1], null, null}, 0);	

		int ban = 20002207;
		Date activityDate = new Date();
		String activityReasonCode = "SUS";
		String[] subscriberId = new String[1];
		String userMemoText = "user memo test";
		String sessionId = "session id";

		impl.suspendSubscribers(ban, activityDate, activityReasonCode, subscriberId, userMemoText, sessionId);

		assertEquals(ban, subscriberDao.ban);
		assertEquals(activityDate, subscriberDao.activityDate);
		assertEquals(activityReasonCode, subscriberDao.activityReasonCode);
		Assert.assertArrayEquals(subscriberId, subscriberDao.subscriberId);
		assertEquals(userMemoText, subscriberDao.userMemoText);
		assertEquals(sessionId, subscriberDao.sessionId);

	}

	@Test
	public void testRestoreSuspendedSubscribers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("restoreSuspendedSubscribers" , int.class, Date.class,String.class 
						, Array.newInstance(String.class, 1).getClass(), String.class, String.class)
						, new Object[]{0, new Date(), null, new String[1], null, null}, 0);	

		int ban = 20001552;
		Date restoreDate = new Date();
		String restoreReasonCode = "PVR";
		String[] subscriberId = new String[1];
		String restoreComment = "restore";
		String sessionId = "234678";

		impl.restoreSuspendedSubscribers(ban, restoreDate, restoreReasonCode, subscriberId, restoreComment, sessionId);

		assertEquals(ban, subscriberDao.ban);
		assertEquals(restoreDate, subscriberDao.restoreDate);
		assertEquals(restoreReasonCode, subscriberDao.restoreReasonCode);
		Assert.assertArrayEquals(subscriberId, subscriberDao.subscriberId);
		assertEquals(restoreComment, subscriberDao.restoreComment);
		assertEquals(sessionId, subscriberDao.sessionId);

	}
//	@Test                                                                                                                                                                                                                      
//	public void testcreateManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {                           
//
//		int ban = 1221;                                                                                                                                                                                                          
//		LMSRequestInfo lmsInfo = new LMSRequestInfo();                                                                                                                                                                           
//		lmsInfo.setBan(ban);                                                                                                                                                                                                     
//		String sessionId = "12321321";                                                                                                                                                                                           
//		impl.createManualLetterRequest(lmsInfo, sessionId);                                                                                                                                                                      
//		assertEquals(ban, letterDao.ban);                                                                                                                                                                                        
//		assertEquals(sessionId, letterDao.sessionId);                                                                                                                                                                            
//	}                                                                                                                                                                                                                          
	@Test
	public void testRefundPaymentToAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("refundPaymentToAccount" , int.class, int.class,String.class,String.class,boolean.class,String.class,String.class)
				, new Object[]{0, 1,"","",true,"",""}, 0);
		int ban = 1221;
		int paymentSeq = 23;
		String reasonCode="as";
		String memoText="test";
		boolean isManual= false;
		String authorizationCode= "dsas";
		String sessionId = "12321321";
		impl.refundPaymentToAccount(ban, paymentSeq, reasonCode, memoText, isManual, authorizationCode, sessionId);
		assertEquals(ban, paymentDao.ban);
		assertEquals(paymentSeq, paymentDao.paymentSeq);
		assertEquals(reasonCode, paymentDao.reasonCode);
		assertFalse(paymentDao.isManual);
		assertEquals(authorizationCode, paymentDao.authorizationCode);
		assertEquals(memoText, paymentDao.memoText);
	}

	@Test
	public void testperformPostAccountCreationPrepaidTasks() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("performPostAccountCreationPrepaidTasks" , int.class, PrepaidConsumerAccountInfo.class,AuditHeader.class, String.class)
				, new Object[]{0, new PrepaidConsumerAccountInfo(), new AuditHeaderInfo(), null}, 0);	

		int ban = 24234;
		AuditHeader auditHeader=new AuditHeaderInfo();
		auditHeader.setCustomerId("325382");
		auditHeader.setUserIPAddress("127.0.0.1");
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = new PrepaidConsumerAccountInfo();
		prepaidConsumerAccountInfo.setAccountType('O');
		String sessionId = "sdkfjh";
		String referenceNumber="";

		try{
			referenceNumber=impl.performPostAccountCreationPrepaidTasks(ban, prepaidConsumerAccountInfo, auditHeader, sessionId);
			fail("Exception Expected");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void testsuspendAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("suspendAccountForPortOut" , int.class, String.class,Date.class, String.class,String.class)
				, new Object[]{0, null, null, null, null}, 0);	

		int ban = 234523;
		String activityReasonCode="";
		Date activityDate=new Date();
		String portOutInd="Y";
		String sessionId = "sdkfjh";

		impl.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, sessionId);

		assertEquals(ban,accountDao.ban);
		assertEquals(sessionId,accountDao.sessionId);
		assertTrue(accountDao.portOutInd);
	}
/**
	@Test
	public void adjustBalanceForPayAndTalkSubscriber() throws ApplicationException {
		int pBan = 123;
		String pPhoneNumber = "1234567890";
		String pUserId = "usr";
		double pAmount = 0;
		String pReasonCode = "reasonCode";
		String pTransactionId = "transactId";

		// Test adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId)
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);

		// Test adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax)
		TaxSummaryInfo pTax = new TaxSummaryInfo(); 
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);
		pTax.setGSTAmount(1);	
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidGSTAdjustmentReasonCode(), prepaidDao.retValue);

		pTax.setGSTAmount(0);
		pTax.setPSTAmount(2);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidPSTAdjustmentReasonCode(), prepaidDao.retValue);

		pTax.setPSTAmount(0);
		pTax.setHSTAmount(3);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidHSTAdjustmentReasonCode(), prepaidDao.retValue);


		// Test adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax, char pTaxOption)
		pTax = new TaxSummaryInfo();
		char pTaxOption = CreditInfo.TAX_OPTION_NO_TAX;
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);

		pTaxOption = CreditInfo.TAX_OPTION_GST_ONLY;
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);
		pTax.setGSTAmount(1);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidGSTAdjustmentReasonCode(), prepaidDao.retValue);		
		pTax.setGSTAmount(0);
		pTax.setPSTAmount(2);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);
		pTax.setGSTAmount(1);
		pTax.setPSTAmount(2);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidGSTAdjustmentReasonCode(), prepaidDao.retValue);
		pTax.setHSTAmount(3);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidGSTAdjustmentReasonCode(), prepaidDao.retValue);

		pTaxOption = CreditInfo.TAX_OPTION_ALL_TAXES;
		pTax = new TaxSummaryInfo();
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pReasonCode, prepaidDao.retValue);
		pTax.setGSTAmount(1);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidGSTAdjustmentReasonCode(), prepaidDao.retValue);		
		pTax.setGSTAmount(0);
		pTax.setPSTAmount(2);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidPSTAdjustmentReasonCode(), prepaidDao.retValue);
		pTax.setGSTAmount(1);
		pTax.setPSTAmount(2);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidPSTAdjustmentReasonCode(), prepaidDao.retValue);
		pTax.setHSTAmount(3);
		impl.adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, pTaxOption);
		assertEquals("adjustBalance(int, String, String, double, String, String), " + pTax.getPrepaidHSTAdjustmentReasonCode(), prepaidDao.retValue);

		//Test adjustBalanceForPayAndTalkSubscriber(String pUserId, ChargeInfo pChargeInfo)
		ChargeInfo pChargeInfo = new ChargeInfo();
		pChargeInfo.setReasonCode(pReasonCode);
		pChargeInfo.setAmount(pAmount);
		impl.adjustBalanceForPayAndTalkSubscriber(pUserId, pChargeInfo);
		assertEquals("adjustBalance(String, ChargeInfo), " + pAmount + ", " + pReasonCode, prepaidDao.retValue);

		//Test adjustBalanceForPayAndTalkSubscriber(String pUserId, CreditInfo pCreditInfo)
		CreditInfo pCreditInfo = new CreditInfo();
		pCreditInfo.setAmount(pAmount);
		pCreditInfo.setReasonCode(pReasonCode);
		impl.adjustBalanceForPayAndTalkSubscriber(pUserId, pCreditInfo);
		assertEquals("adjustBalance(String, CreditInfo), " + pAmount + ", " + pReasonCode, prepaidDao.retValue);

		pCreditInfo.setTaxOption(CreditInfo.TAX_OPTION_NO_TAX);
		impl.adjustBalanceForPayAndTalkSubscriber(pUserId, pCreditInfo);
		assertEquals("adjustBalance(String, CreditInfo), " + pAmount + ", " + pReasonCode, prepaidDao.retValue);

		pCreditInfo.setTaxOption(CreditInfo.TAX_OPTION_ALL_TAXES);
		// there is no way to set GSTAmount, PSTAmount, HSTAmount on CreditInfo
		// The private taxSummary field in the CreditInfo class has no mutator methods
	}

	@Test
	public void testUpdateAccountPin() throws ApplicationException,IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		TestPrepaidDao prepaidDao = new TestPrepaidDao();
		impl.setPrepaidDao(prepaidDao);
		try{
			impl.updateAccountPIN(1, "", "", "", "", "");
			fail("Exception Expected");
		} catch (ApplicationException e) {		
		}
		impl.updateAccountPIN(1, "", "", "", "12", "");
		assertTrue(prepaidDao.returnFlag());
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("updateAccountPIN", int.class, String.class,String.class,String.class,String.class,String.class ), new Object[]{1,"","","","sa",""}, 0);
	}


	/**
	 * This test tests the if/else statement in 
	 * @see com.telus.cmb.account.informationhelper.svc.AccountInformationHelperImpl#applyTopUpForPayAndTalkSubscriber(int, java.lang.String, double, java.lang.String, java.lang.String, java.lang.String)
	 * 
	 * @throws ApplicationException
	 */
	@Test
	public void applyTopUpForPayAndTalkSubscriber() throws ApplicationException {
		int pBan = 123;
		String pPhoneNumber = "1234567890";
		double pAmount = 45.55;
		String pOrderNumber = "";
		String pUserId = "usr";
		String pApplicationId = "app";
		String retVal = null;

		retVal = impl.applyTopUpForPayAndTalkSubscriber(pBan, pPhoneNumber, pAmount, pOrderNumber, pUserId, pApplicationId);
		assertEquals("applyTopUpWithCreditCard", retVal);

		pOrderNumber = null;
		retVal = impl.applyTopUpForPayAndTalkSubscriber(pBan, pPhoneNumber, pAmount, pOrderNumber, pUserId, pApplicationId);
		assertEquals("applyTopUpWithCreditCard", retVal);

		pOrderNumber = "abc";
		retVal = impl.applyTopUpForPayAndTalkSubscriber(pBan, pPhoneNumber, pAmount, pOrderNumber, pUserId, pApplicationId);
		assertEquals("", retVal);				
	}

	@Test
	public void testSaveEBillRegistrationReminder()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("saveEBillRegistrationReminder", int.class,String.class,String.class,String.class), new Object[]{1,"","",""}, 0);
		impl.saveEBillRegistrationReminder(1212, "","","");
		assertEquals(1212,this.invoiceDao.getBan());
	}
	@Test
	public void testSaveBillNotificationDetails()throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,ApplicationException{
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("saveBillNotificationDetails", int.class,long.class,BillNotificationContactInfo[].class,String.class), new Object[]{1,12121,null,""}, 0);
		impl.saveBillNotificationDetails(8,1212121,null,"");
		assertEquals(0,this.invoiceDao.getBan());		
		assertEquals(null,this.invoiceDao.calledMethod);
		BillNotificationContactInfo billNotificationContactInfo = new BillNotificationContactInfo();
		billNotificationContactInfo.setBillNotificationType("");
		BillNotificationContactInfo[] billNotification= new BillNotificationContactInfo[1];
		billNotification[0]=billNotificationContactInfo;
		impl.saveBillNotificationDetails(24,1212121,billNotification,"");
		assertEquals(24,this.invoiceDao.getBan());
		assertEquals("1",this.invoiceDao.calledMethod);
		billNotificationContactInfo.setBillNotificationType("EPOST");
		BillNotificationContactInfo[] billNotification1= new BillNotificationContactInfo[1];
		billNotification1[0]=billNotificationContactInfo;
		impl.saveBillNotificationDetails(8,1212121,billNotification1,"");
		assertEquals(8,this.invoiceDao.getBan());
		assertEquals("2",this.invoiceDao.calledMethod);
	}

	@Test
	public void testapplyCreditForFeatureCard(){
		CardInfo cardInfo=new CardInfo();
		cardInfo.setAdjustmentCode("10");
		ServiceInfo[] cardServices=new ServiceInfo[0];
		String userId = "Test";
		impl.applyCreditForFeatureCard(cardInfo, cardServices, userId);
	}


//	@Test
//	public void testUpdateTopupCreditCard() throws RemoteException{
//		String MDN="12345345";
//		String serialNo="1234567890";
//		String user="integTest";
//		boolean encrypted=false;
//		CreditCardInfo creditCard=new CreditCardInfo();
//		creditCard.setToken("123");
//		creditCard.setTrailingDisplayDigits("456");
//		creditCard.setLeadingDisplayDigits("789");
//		creditCard.setExpiryMonth(11);
//		creditCard.setExpiryYear(2020);
//		String ban="2007434";
//		
//		impl.updateTopupCreditCard(ban, MDN, serialNo, creditCard, user, encrypted);
//		assertEquals(ban,this.prepaidDao.ban);
//		assertEquals(MDN,this.prepaidDao.MDN);
//	}
//	
//	@Test
//	public void testUpdateTopupCreditCard1() throws RemoteException{
//		String MDN="12345345";
//		String user="integTest";
//		boolean encrypted=false;
//		CreditCardInfo creditCard=new CreditCardInfo();
//		creditCard.setToken("123");
//		creditCard.setTrailingDisplayDigits("456");
//		creditCard.setLeadingDisplayDigits("789");
//		creditCard.setExpiryMonth(11);
//		creditCard.setExpiryYear(2020);
//		String ban="2007434";
//		
//		impl.updateTopupCreditCard(ban, MDN, creditCard, user, encrypted);
//		assertEquals(ban,this.prepaidDao.ban);
//		assertEquals(MDN,this.prepaidDao.MDN);
//	}
////	
//	@Test
//	public void testUpdateAutoTopUp() throws ApplicationException, RemoteException{
//
//		AutoTopUpInfo autoTopUpInfo = new AutoTopUpInfo();
//		int banId=2007434;
//		autoTopUpInfo.setBan(banId);
//		autoTopUpInfo.setChargeAmount(12);
//		autoTopUpInfo.setPhoneNumber("90523484773");
//		String userId = "integTest";
//		boolean existingAutoTopUp = true;
//		boolean existingThresholdRecharge = false;
//		
//		impl.updateAutoTopUp(autoTopUpInfo, userId, existingAutoTopUp, existingThresholdRecharge);
//		assertEquals(String.valueOf(banId),this.prepaidDao.ban);
//		assertEquals(existingAutoTopUp,this.prepaidDao.existingAutoTopUp);
//		assertEquals("90523484773",this.prepaidDao.phoneNumber);
//	}
	
//	@Test
//	public void testCreditSubscriberMigration() throws ApplicationException{
//		
//		String pUserId = "test_user";
//		String applicationId = "ClientAPI";
//		ActivationTopUpInfo activationTopUpInfo = new ActivationTopUpInfo();
//		activationTopUpInfo.setAmount(1000);
//		String phoneNumber = "4163452342";
//		String esn = "4163452342";
//		String provinceCode = "ON";
//		PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo = new PrepaidConsumerAccountInfo();
//		pPrepaidConsumerAccountInfo.setAccountType('A');
//		impl.creditSubscriberMigration(applicationId, pUserId, activationTopUpInfo, phoneNumber, esn, provinceCode, pPrepaidConsumerAccountInfo);
//		assertEquals(applicationId, prepaidDao.applicationId);
//		assertEquals(activationTopUpInfo.getAmount(), prepaidDao.activationTopUpInfo.getAmount(),0);
//		assertEquals(pPrepaidConsumerAccountInfo.getAccountType(), prepaidDao.pPrepaidConsumerAccountInfo.getAccountType());
//		
//	}
//	
//	@Test
//	public void testRemoveTopupCreditCard() throws ApplicationException{
//		String MDN="mdn";
//		impl.removeTopupCreditCard(MDN);
//		assertEquals(MDN, prepaidDao.MDN);
//	}
//	
//	@Test
//	public void testSaveActivationTopUpArrangement() throws ApplicationException{
//		String MDN="mdn";
//		String banId="20004875";
//		String serialNo="123";
//		ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement=new ActivationTopUpPaymentArrangementInfo();
//		String user="test_user";
//		impl.saveActivationTopUpArrangement(banId, MDN, serialNo, topUpPaymentArrangement, user);
//		assertEquals(MDN, prepaidDao.MDN);
//		assertEquals(banId, prepaidDao.ban);
//	}
	
	@Test
	public void testApplyTopUpForPayAndTalkSubscriber() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("applyTopUpForPayAndTalkSubscriber", 
				int.class, String.class, CardInfo.class, String.class, String.class),
				new Object[]{1, null, null, null, null}, 0);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("applyTopUpForPayAndTalkSubscriber",
				int.class, String.class, double.class, String.class, String.class, String.class),
				new Object[]{1, null, 1, null, null, null}, 0);
	}

	@Test
	public void testAdjustBalanceForPayAndTalkSubscriber() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("adjustBalanceForPayAndTalkSubscriber",
				int.class, String.class, String.class, double.class, String.class, String.class),
				new Object[]{1, null, null, 1, null, null}, 0);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("adjustBalanceForPayAndTalkSubscriber", 
				int.class, String.class, String.class, double.class, String.class, String.class, TaxSummaryInfo.class),
				new Object[]{1, null, null, 1, null, null, null}, 0);

		BanValidatorTestHelper.executeBanRangeTest(impl, impl.getClass().getMethod("adjustBalanceForPayAndTalkSubscriber", 
				int.class, String.class, String.class, double.class, String.class, String.class, TaxSummaryInfo.class, char.class),
				new Object[]{1, null, null, 1, null, null, null, 'a'}, 0);	
	}
	
	@Test
	public void testUpdateInvoiceProperties() throws ApplicationException{
		
		
		int ban = 123123;
		String sessionId = "abcde";
		InvoicePropertiesInfo invoicePropertiesInfo=new InvoicePropertiesInfo();
		impl.updateInvoiceProperties(ban, invoicePropertiesInfo, sessionId);

		assertEquals(ban, invoiceDao.ban);
		assertEquals(sessionId, invoiceDao.sessionId);
	}
	
	@Test
	public void testcreateCreditBalanceTransferRequest() throws ApplicationException{
		
		
		int sourceBan = 123123;
		int targetBan = 99990;
		String sessionId = "abcde";
		creditBalanceTransferDao.createCreditBalanceTransferRequest(sourceBan, targetBan, null);

		
	}
	
	@Test
	public void testRetrieveLastCreditCheckResultByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("retrieveLastCreditCheckResultByBan", int.class, String.class, String.class)
				, new Object[]{0, null, null}, 0);
		
		int ban = 123123;
		String sessionId = "abcde";
		String productType="abc";
		impl.retrieveLastCreditCheckResultByBan(ban, productType, sessionId);

		assertEquals(ban, creditCheckDao.ban);
		assertEquals(sessionId, creditCheckDao.sessionId);
		assertTrue(accountInformationHelper.isRetrieveLastCreditCheckResultByBanCalled);
	}
	
	@Test
	public void testUpdateBillingInformation() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateBillingInformation", int.class, BillingPropertyInfo.class, String.class)
				, new Object[]{0, null, null}, 0);
		
		int billingAccountNumber=8;
		String sessionId = "abcde";
		BillingPropertyInfo billingPropertyInfo=new BillingPropertyInfo();
		billingPropertyInfo.setFullName("NEW TEST");
		billingPropertyInfo.setLegalBusinessName("TEST INC.");
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("NEW");
		name.setLastName("TEST");
		name.setNameFormat("Deeedde");
		billingPropertyInfo.setName(name );
		
		impl.updateBillingInformation(billingAccountNumber, billingPropertyInfo, sessionId);
		
		assertEquals(billingAccountNumber, contactDao.ban);
		assertEquals(sessionId, contactDao.sessionId);
		
	}
	
	@Test
	public void testUpdateContactInformation() throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, ApplicationException{
		
		BanValidatorTestHelper.executeBanRangeTest(impl
				, impl.getClass().getMethod("updateContactInformation", int.class, ContactPropertyInfo.class, String.class)
				, new Object[]{0, null, null}, 0);
		
		int billingAccountNumber=8;
		String sessionId = "abcde";
		ContactPropertyInfo contactPropertyInfo=new ContactPropertyInfo();
		contactPropertyInfo.setBusinessPhoneNumber("4163842304");
		contactPropertyInfo.setBusinessPhoneExtension("4563");
		
		impl.updateContactInformation(billingAccountNumber, contactPropertyInfo, sessionId);
		
		assertEquals(billingAccountNumber, contactDao.ban);
		assertEquals(sessionId, contactDao.sessionId);
		
	}
}
