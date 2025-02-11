package com.telus.cmb.account.informationhelper.svc.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.Memo;
import com.telus.api.account.MonthlyFinancialActivity;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.Subscriber;
import com.telus.cmb.account.informationhelper.dao.AccountDao;
import com.telus.cmb.account.informationhelper.dao.AddressDao;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao.CreditInfoHolder;
import com.telus.cmb.account.informationhelper.dao.CollectionDao;
import com.telus.cmb.account.informationhelper.dao.ContactDao;
import com.telus.cmb.account.informationhelper.dao.CreditCheckDao;
import com.telus.cmb.account.informationhelper.dao.DepositDao;
import com.telus.cmb.account.informationhelper.dao.EquipmentDao;
import com.telus.cmb.account.informationhelper.dao.FleetDao;
import com.telus.cmb.account.informationhelper.dao.FollowUpDao;
import com.telus.cmb.account.informationhelper.dao.InvoiceDao;
//import com.telus.cmb.account.informationhelper.dao.LetterDao;
import com.telus.cmb.account.informationhelper.dao.MaxVoipLineDao;
import com.telus.cmb.account.informationhelper.dao.MemoDao;
import com.telus.cmb.account.informationhelper.dao.PaymentDao;
import com.telus.cmb.account.informationhelper.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.account.informationhelper.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.account.informationhelper.dao.SubscriberDao;
import com.telus.cmb.account.informationhelper.dao.UsageDao;
import com.telus.cmb.account.informationhelper.dao.WirelessCreditManagementServiceDao;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelperTestPoint;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.account.utilities.StringSanitization;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.validation.BanValidator;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.credit.info.CreditProgramInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
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
import com.telus.eas.account.info.MaxVoipLineInfo;
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
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo.DataSharingResultInfo;
import com.telus.eas.account.info.SubscriberEligibilitySupportingInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.eas.framework.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeIdentifierInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name = "AccountInformationHelper", mappedName = "AccountInformationHelper")
@Remote({ AccountInformationHelper.class, AccountInformationHelperTestPoint.class })
@RemoteHome(AccountInformationHelperHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccountInformationHelperImpl implements AccountInformationHelper, AccountInformationHelperTestPoint {

	private static final String CREDIT_CHECK = "CreditCheck";
	private static final int INTERNATIONAL_SERVICE_COLLECTION_ACTIVITY_TERM_IN_MONTHS = 6;
	private static final Logger LOGGER = Logger.getLogger(AccountInformationHelperImpl.class);

	@Autowired
	private AccountDao accountDao;

	@Autowired
	private CreditCheckDao creditCheckDao;

	@Autowired
	private SubscriberDao subscriberDao;

	@Autowired
	private UsageDao usageDao;

	@Autowired
	private MemoDao memoDao;

	@Autowired
	private InvoiceDao invoiceDao;

	@Autowired
	private FollowUpDao followUpDao;

	@Autowired
	private FleetDao fleetDao;

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private EquipmentDao equipmentDao;

	@Autowired
	private AddressDao addressDao;

	@Autowired
	private DepositDao depositDao;

	@Autowired
	private AdjustmentDao adjustmentDao;

	@Autowired
	private CollectionDao collectionDao;

//	@Autowired
//	private LetterDao letterDao;

	@Autowired
	private ContactDao contactDao;

	@Autowired
	@Qualifier("helperTestPointDao")
	private DataSourceTestPointDao testPointDao;

	@Autowired
	private PrepaidWirelessCustomerOrderServiceDao pwcosDao;

	@Autowired
	private PrepaidSubscriberServiceDao pssDao;

	@Autowired
	private MaxVoipLineDao maxVoipLineDao;

	@Autowired
	private WirelessCreditManagementServiceDao wirelessCreditManagementServiceDao;
	
	private ReferenceDataFacade refDataFacade;
	private SubscriberLifecycleHelper subscriberLifecycleHelper;
	private SubscriberLifecycleFacade subscriberLifecycleFacade;

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setCreditCheckDao(CreditCheckDao creditCheckDao) {
		this.creditCheckDao = creditCheckDao;
	}

	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}

	public void setUsageDao(UsageDao usageDao) {
		this.usageDao = usageDao;
	}

	public void setMemoDao(MemoDao memoDao) {
		this.memoDao = memoDao;
	}

	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

	public void setFollowUoDao(FollowUpDao followUpDao) {
		this.followUpDao = followUpDao;
	}

	public void setFleetDao(FleetDao fleetDao) {
		this.fleetDao = fleetDao;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setEquipmentDao(EquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}

	public void setAdjustmentDao(AdjustmentDao adjustmentDao) {
		this.adjustmentDao = adjustmentDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	public void setDepositDao(DepositDao depositDao) {
		this.depositDao = depositDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}

	public void setMaxVoipLineDao(MaxVoipLineDao maxVoipLineDaoIn) {
		this.maxVoipLineDao = maxVoipLineDaoIn;
	}

	@Override
	public List<AccountInfo> retrieveAccountsByBan(int[] banArray) {
		return accountDao.retrieveAccountsByBan(banArray);
	}

	@Override
	@Deprecated
	public AccountInfo retrieveHwAccountByBan(@BANValue int ban) throws ApplicationException {
		LOGGER.debug("Begin deprecated method retrieveHwAccountByBan for ban [" + ban + "]...");
		return retrieveHwAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
	}

	// Updated for CDA phase 1B
	@Override
	public AccountInfo retrieveHwAccountByBan(@BANValue int ban, int retrievalOption) throws ApplicationException {
		LOGGER.debug("Begin retrieveHwAccountByBan for ban [" + ban + "] with retrievalOption value [" + retrievalOption + "]..." );
		AccountInfo accountInfo = retrieveAccountByBan(ban, retrievalOption);
		accountInfo.setProductSubscriberLists(retrieveProductSubscriberLists(ban));
		LOGGER.debug("End retrieveHwAccountByBan for ban [" + ban + "] with retrievalOption value [" + retrievalOption + "]." );
		return accountInfo;
	}

	@Override
	@Deprecated
	public AccountInfo retrieveAccountByBan(@BANValue int ban) throws ApplicationException {
		LOGGER.debug("Begin deprecated method retrieveAccountByBan for ban [" + ban + "]...");
		if (AppConfiguration.useNewRetrieveAccountByBanMethod()) {
			return retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		} else {
			return retrieveAccountByBanRollback(ban);
		}
	}

	/**
	 * @param retrievalOption - this is a bitwise value where the method implementation should dynamically load different information based on the bit value. 1=Enable, 0=Disable
	 */
	@Override
	public AccountInfo retrieveAccountByBan(@BANValue int ban, int retrievalOption) throws ApplicationException {

		AccountInfo accountInfo = null;
		accountInfo = accountDao.retrieveAccountByBan(ban);
		if (accountInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN [" + ban + "] not found", "");
		}

		if ((retrievalOption & Account.ACCOUNT_LOAD_CDA) > 0) {
			// Retrieve credit check results
			accountInfo.getCreditCheckResult0().copyFrom(retrieveLastCreditCheckResultAndDepositsByBan(ban, accountInfo.isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS));
		}

		Collection<String> subscribers;
		if (!accountInfo.isPostpaid()) {
			LOGGER.debug("Begin prepaid retrieveAccountInfo for ban [" + ban + "]...");
			subscribers = subscriberDao.retrieveActiveSubscriberPhoneNumbers(ban, 1);
			// If no active subscribers found, try suspended ones
			if (subscribers.size() == 0) {
				subscribers = subscriberDao.retrieveSuspendedSubscriberPhoneNumbers(ban, 1);
			}
			if (subscribers.size() > 0) {
				for (String phoneNumber : subscribers) {
					pssDao.retrieveAccountInfo((PrepaidConsumerAccountInfo) accountInfo, phoneNumber);
				}
			}
		}

		return accountInfo;
	}

	/**
	 * @deprecated Deprecated in KB Capacity Project (2013 July). Kept for rollback purpose only.
	 * @param ban
	 * @return
	 * @throws ApplicationException
	 */
	@Deprecated
	private AccountInfo retrieveAccountByBanRollback(@BANValue int ban) throws ApplicationException {

		AccountInfo accountInfo = null;
		try {
			accountInfo = accountDao.retrieveAccountByBanRollback(ban);
		} catch (DataAccessException daex) {
			Throwable cause = daex.getMostSpecificCause();
			if (cause instanceof SQLException) {
				SQLException sqlex = (SQLException) cause;
				if (sqlex.getErrorCode() == 20101) {
					throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
				}
			}
			throw daex;
		}

		if (accountInfo == null) {
			throw new SystemException(SystemCodes.CMB_AIH_EJB, "Account information returned from package call is null however it should not be null.", "");
		}

		// Credit check results
		accountInfo.getCreditCheckResult0().copyFrom(retrieveLastCreditCheckResultByBan(ban, accountInfo.isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS));

		Collection<String> subscribers;
		if (!accountInfo.isPostpaid()) {
			subscribers = subscriberDao.retrieveActiveSubscriberPhoneNumbers(ban, 1);
			// iI no active subscribers found, try suspended ones
			if (subscribers.size() == 0) {
				subscribers = subscriberDao.retrieveSuspendedSubscriberPhoneNumbers(ban, 1);
			}
			if (subscribers.size() > 0) {
				for (String phoneNumber : subscribers) {
					pssDao.retrieveAccountInfo((PrepaidConsumerAccountInfo) accountInfo, phoneNumber);
				}
			}
		}

		return accountInfo;
	}

	/**
	 * This method is for IDEN only. Improper method name. TO DO: Rename method
	 * or change the query to support what the method name truly suggests.
	 */
	@Override
	public Map<String, String> retrievePhoneNumbersForBAN(@BANValue int ban) throws ApplicationException {
		return subscriberDao.retrievePhoneNumbersForBAN(ban);
	}

	@Override
	public String retrieveAccountStatusByBan(@BANValue int ban) throws ApplicationException {
		return accountDao.retrieveAccountStatusByBan(ban);
	}

	@Override
	public VoiceUsageSummaryInfo[] retrieveVoiceUsageSummary(@BANValue int ban, String featureCode) throws ApplicationException {

		if (featureCode == null || featureCode.length() == 0) {
			featureCode = "STD";
		}

		Collection<VoiceUsageSummaryInfo> voiceUsageSummaryInfo = this.usageDao.retrieveVoiceUsageSummary(ban, featureCode);
		if (voiceUsageSummaryInfo != null) {
			return voiceUsageSummaryInfo.toArray(new VoiceUsageSummaryInfo[voiceUsageSummaryInfo.size()]);
		} else {
			return new VoiceUsageSummaryInfo[0];
		}
	}

	@Override
	public List<AccountInfo> retrieveAccountsByPostalCode(String lastName, String postalCode, int maximum) {

		String lastBusinessName = StringSanitization.sanitize(lastName);
		if (lastBusinessName.length() > 14) {
			lastBusinessName = lastBusinessName.substring(0, 14);
		}
		String pCode = StringSanitization.sanitize(postalCode);

		return accountDao.retrieveAccountsByPostalCode(lastBusinessName, pCode, maximum);
	}

	@Override
	public AccountInfo retrieveAccountByPhoneNumber(String phoneNumber) throws ApplicationException {

		phoneNumber = StringSanitization.sanitize(phoneNumber);
		int ban = accountDao.retrieveBANByPhoneNumber(phoneNumber);
		if (!BanValidator.isValidRange(ban)) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
		}

		return retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
	}

	@Override
	public AccountInfo retrieveAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {
		return retrieveAccountByBan(searchBanByPhoneNumber(phoneNumber, phoneNumberSearchOptionInfo), Account.ACCOUNT_LOAD_ALL);
	}

	@Override
	public AccountInfo retrieveLwAccountByPhoneNumber(String phoneNumber) throws ApplicationException {

		phoneNumber = StringSanitization.sanitize(phoneNumber);
		int ban = accountDao.retrieveBANByPhoneNumber(phoneNumber);
		AccountInfo accountInfo = retrieveLwAccountByBan(ban);
		if (!accountInfo.isPostpaid()) {
			pssDao.retrieveAccountInfo((PrepaidConsumerAccountInfo) accountInfo, phoneNumber);
		}

		return accountInfo;
	}

	@Override
	public AccountInfo retrieveHwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {
		return retrieveHwAccountByBan(searchBanByPhoneNumber(phoneNumber, phoneNumberSearchOptionInfo));
	}

	@Override
	public AccountInfo retrieveLwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {

		AccountInfo accountInfo = retrieveLwAccountByBan(searchBanByPhoneNumber(phoneNumber, phoneNumberSearchOptionInfo));
		if (!accountInfo.isPostpaid()) {
			pssDao.retrieveAccountInfo((PrepaidConsumerAccountInfo) accountInfo, phoneNumber);
		}

		return accountInfo;
	}

	private int searchBanByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {

		int ban = 0;
		phoneNumber = StringSanitization.sanitize(phoneNumber);
		if (phoneNumberSearchOptionInfo.isSearchWirelessNumber()) {
			ban = accountDao.retrieveBANByPhoneNumber(phoneNumber);
		}
		if (!BanValidator.isValidRange(ban) && (phoneNumberSearchOptionInfo.isSearchHSIA() || phoneNumberSearchOptionInfo.isSearchVOIP() || phoneNumberSearchOptionInfo.isSearchTollFree())) {
			ban = accountDao.retrieveBANBySeatNumber(phoneNumber);
		}
		if (!BanValidator.isValidRange(ban)) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "Active account not found for phonenumber=[" + phoneNumber + "] in either wireless/VOIP number search.", "");
		}

		return ban;
	}

	@Override
	public List<AccountInfo> retrieveAccountsByPhoneNumber(String phoneNumber) throws ApplicationException {
		return retrieveAccountsByPhoneNumber(phoneNumber, true, false);
	}

	@Override
	public List<AccountInfo> retrieveAccountsByPhoneNumber(String phoneNumber, boolean includePastAccounts, boolean onlyLastAccount) throws ApplicationException {

		phoneNumber = StringSanitization.sanitize(phoneNumber);
		List<AccountInfo> accountList = new ArrayList<AccountInfo>();
		if (!includePastAccounts) {
			AccountInfo account = retrieveAccountByPhoneNumber(phoneNumber);
			accountList.add(account);
			// retrieveAccountByPhoneNumber always returns a value if an exception is not thrown
			return accountList;

		} else if (onlyLastAccount) {
			try {
				AccountInfo account = retrieveAccountByPhoneNumber(phoneNumber);
				accountList.add(account);
				// retrieveAccountByPhoneNumber always returns a value if an exception is not thrown
				return accountList;

			} catch (Throwable t) {
				// If throwable is caught, you move on and call the DAO
			}
		}

		return accountDao.retrieveAccountsByPhoneNumber(phoneNumber, onlyLastAccount);
	}

	@Override
	public List<AccountInfo> retrieveLatestAccountsByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {

		phoneNumber = StringSanitization.sanitize(phoneNumber);
		List<AccountInfo> accountList = new ArrayList<AccountInfo>();
		if (phoneNumberSearchOptionInfo.isSearchWirelessNumber()) {
			try {
				// [Naresh Annabathula] Search active accounts first, if active accounts present return the list, otherwise continue the search for recent cancelled ones.
				AccountInfo account = retrieveAccountByPhoneNumber(phoneNumber);
				accountList.add(account);
				return accountList;

			} catch (ApplicationException ae) {
				// If ApplicationException is caught with ban not found error code, continue the search for recent cancelled account.
				if (ErrorCodes.BAN_NOT_FOUND.equals(ae.getErrorCode())) {
					accountList = accountDao.retrieveAccountsByPhoneNumber(phoneNumber, true);
				} else {
					throw ae;
				}
			}
		}
		if (accountList.isEmpty() && (phoneNumberSearchOptionInfo.isSearchHSIA() || phoneNumberSearchOptionInfo.isSearchVOIP() || phoneNumberSearchOptionInfo.isSearchTollFree())) {
			try {
				phoneNumberSearchOptionInfo.setSearchWirelessNumber(false);
				AccountInfo account = retrieveAccountByPhoneNumber(phoneNumber, phoneNumberSearchOptionInfo);
				accountList.add(account);
				return accountList;
			} catch (ApplicationException ae) {
				// [Naresh Annabathula] If ApplicationException is caught with ban not found error code, continue the search for recent cancelled account.
				if (ErrorCodes.BAN_NOT_FOUND.equals(ae.getErrorCode())) {
					accountList = accountDao.retrieveLastAccountsBySeatNumber(phoneNumber);
				} else {
					throw ae;
				}
			}
		}

		return accountList;
	}

	@Override
	public PrepaidConsumerAccountInfo retrieveAccountInfo(@BANValue int ban, String phoneNumber) throws ApplicationException {
		return pssDao.retrieveAccountInfo(ban, phoneNumber);
	}

	@Override
	public SearchResultsInfo retrieveAccountsByBusinessName(String nameType, String legalBusinessName, boolean legalBusinessNameExactMatch, char accountStatus, char accountType, String provinceCode,
			int brandId, int maximum) throws ApplicationException {

		if (legalBusinessName == null || legalBusinessName.trim().length() == 0) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Legal Business Name should be a non-empty string.", "");
		}

		return accountDao.retrieveAccountsByName(nameType, "", false, legalBusinessName, legalBusinessNameExactMatch, accountStatus, accountType, provinceCode, brandId, maximum);
	}

	@Override
	public SearchResultsInfo retrieveAccountsByName(String nameType, String firstName, boolean firstNameExactMatch, String lastName, boolean lastNameExactMatch, char accountStatus, char accountType,
			String provinceCode, int brandId, int maximum) throws ApplicationException {

		if (lastName == null || lastName.trim().length() == 0) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Last Name should be a non-empty string.", "");
		}

		return accountDao.retrieveAccountsByName(nameType, firstName, firstNameExactMatch, lastName, lastNameExactMatch, accountStatus, accountType, provinceCode, brandId, maximum);
	}

	@Override
	public AutoTopUpInfo retrieveAutoTopUpInfoForPayAndTalkSubscriber(@BANValue int pBan, String pPhoneNumber) throws ApplicationException {
		return pssDao.retrieveAutoTopUpInfo(pBan, pPhoneNumber);
	}

	@Override
	public List<MemoInfo> retrieveMemos(@BANValue int Ban, int Count) throws ApplicationException {
		return memoDao.retrieveMemos(Ban, Count);
	}

	@Override
	public List<MemoInfo> retrieveMemos(MemoCriteriaInfo MemoCriteria) throws ApplicationException {
		return memoDao.retrieveMemos(MemoCriteria);
	}

	@Override
	public MemoInfo retrieveLastMemo(@BANValue int ban, String memoType) throws ApplicationException {

		MemoInfo memo = this.memoDao.retrieveLastMemo(ban, Info.padTo(memoType.trim(), ' ', 4));
		if (memo.getBanId() == 0) { // empty
			if (memoType.equalsIgnoreCase(CREDIT_CHECK)) {
				throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.CREDIT_CHECK_MEMO_NOT_FOUND, "Credit Check Memo Not Found", "");
			} else {
				throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.MEMO_NOT_FOUND, "Memo Not Found", "");
			}
		}

		return memo;
	}

	@Override
	public double retrieveUnpaidAirTimeTotal(@BANValue int ban) throws ApplicationException {
		return usageDao.retrieveUnpaidAirTimeTotal(ban);
	}

	@Override
	public AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(@BANValue int ban) throws ApplicationException {
		return usageDao.retrieveUnpaidAirtimeUsageChargeInfo(ban);
	}

	@Override
	public AccountInfo retrieveLwAccountByBan(@BANValue int ban) throws ApplicationException {

		if (AppConfiguration.isRetrieveLwAccountByBanRollback()) {
			return retrieveLwAccountByBanRollback(ban);
		} else {
			AccountInfo accountInfo = null;
			if (ban > 0) {
				accountInfo = this.accountDao.retrieveLwAccountByBan(ban);
			}
			if (accountInfo == null) {
				throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN [" + ban + "] not found", "");
			}

			return accountInfo;
		}
	}

	/**
	 * @deprecated
	 * @param ban
	 * @return
	 * @throws ApplicationException
	 */
	@Deprecated
	public AccountInfo retrieveLwAccountByBanRollback(@BANValue int ban) throws ApplicationException {

		AccountInfo accountInfo = null;
		try {
			if (ban > 0) {
				accountInfo = this.accountDao.retrieveLwAccountByBanRollback(ban);
			}
		} catch (DataAccessException daex) {
			Throwable cause = daex.getMostSpecificCause();
			if (cause instanceof SQLException) {
				SQLException sqlex = (SQLException) cause;
				if (sqlex.getErrorCode() == 20101) {
					throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.BAN_NOT_FOUND, "Ban not found", "");
				}
			}
			throw daex;
		}

		if (accountInfo == null) {
			throw new SystemException(SystemCodes.CMB_AIH_EJB, "Account information returned from package call is null however it should not be null.", "");
		}

		return accountInfo;
	}

	@Override
	public List<AccountInfo> retrieveAccountsByDealership(char accountStatus, String dealerCode, Date startDate, Date endDate, int maximum) {
		return accountDao.retrieveAccountsByDealership(accountStatus, dealerCode, startDate, endDate, maximum);
	}

	@Override
	public List<AccountInfo> retrieveAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, Date startDate, Date endDate, int maximum) {
		return accountDao.retrieveAccountsBySalesRep(accountStatus, dealerCode, salesRepCode, startDate, endDate, maximum);
	}

	/**
	 * @deprecated since 2011 September release
	 */
	@Deprecated
	@Override
	public BillNotificationContactInfo getLastEBillNotificationSent(@BANValue int ban) throws ApplicationException {
		return invoiceDao.getLastEBillNotificationSent(ban);
	}

	/**
	 * @deprecated since 2011 September release
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	@Override
	public List<BillNotificationHistoryRecord> getBillNotificationHistory(@BANValue int ban, String subscriptionType) throws ApplicationException {
		if (subscriptionType.equals(BillNotificationContact.NOTIFICATION_TYPE_EPOST)) {
			return invoiceDao.getBillNotificationHistory(ban, subscriptionType);
		}
		return null;
	}

	@Override
	public void expireBillNotificationDetails(@BANValue int ban) throws ApplicationException {
		invoiceDao.expireBillNotificationDetails(ban);
	}

	@Override
	public PrepaidConsumerAccountInfo retrieveAccountInfoForPayAndTalkSubscriber(@BANValue int ban, String phoneNumber) throws ApplicationException {
		return pssDao.retrieveAccountInfo(ban, phoneNumber);
	}

	@Override
	public int[] retrieveBanIds(char accountType, char accountSubType, char banStatus, int maximum) {
		return accountDao.retrieveBanIds(accountType, accountSubType, banStatus, maximum);
	}

	@Override
	public int[] retrieveBanIdByAddressType(char accountType, char accountSubType, char banStatus, char addressType, int maximum) {
		return accountDao.retrieveBanIdByAddressType(accountType, accountSubType, banStatus, addressType, maximum);
	}

	@Override
	public int retrieveAttachedSubscribersCount(@BANValue int ban, FleetIdentityInfo fleetIdentityInfo) throws ApplicationException {
		return subscriberDao.retrieveAttachedSubscribersCount(ban, fleetIdentityInfo);
	}

	@Override
	public List<FollowUpTextInfo> retrieveFollowUpAdditionalText(@BANValue int ban, int followUpId) throws ApplicationException {
		return followUpDao.retrieveFollowUpAdditionalText(ban, followUpId);
	}

	@Override
	public List<FollowUpInfo> retrieveFollowUpHistory(int followUpId) {

		List<FollowUpInfo> followUpResult = followUpDao.retrieveFollowUpHistory(followUpId);
		if (followUpResult != null && followUpResult.size() > 0) {
			for (FollowUpInfo followUpInfo : followUpResult) {
				if (followUpInfo.getProductType() != null && followUpInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					Map<String, String> phoneNumbers = subscriberDao.retrievePhoneNumbersForBAN(followUpInfo.getBan());
					followUpInfo.setPhoneNumber(phoneNumbers.get(followUpInfo.getSubscriberId()));
				} else {
					followUpInfo.setPhoneNumber(followUpInfo.getSubscriberId());
				}
			}
		}

		return followUpResult;
	}

	@Override
	public FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(@BANValue int ban, int followUpID) throws ApplicationException {

		FollowUpInfo followUpResult = followUpDao.retrieveFollowUpInfoByBanFollowUpID(ban, followUpID);
		if (followUpResult != null) {
			if (followUpResult.getProductType() != null && followUpResult.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
				Map<String, String> phoneNumbers = subscriberDao.retrievePhoneNumbersForBAN(ban);
				followUpResult.setPhoneNumber(phoneNumbers.get(followUpResult.getSubscriberId()));
			} else {
				followUpResult.setPhoneNumber(followUpResult.getSubscriberId());
			}
		}

		return followUpResult;
	}

	@Override
	public List<FollowUpInfo> retrieveFollowUps(FollowUpCriteriaInfo followUpCriteria) {

		List<FollowUpInfo> followUpResult = followUpDao.retrieveFollowUps(followUpCriteria);
		if (followUpResult != null && followUpResult.size() > 0) {
			for (FollowUpInfo followUpInfo : followUpResult) {
				if (followUpInfo.getProductType() != null && followUpInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					Map<String, String> phoneNumbers = subscriberDao.retrievePhoneNumbersForBAN(followUpInfo.getBan());
					followUpInfo.setPhoneNumber(phoneNumbers.get(followUpInfo.getSubscriberId()));
				} else {
					followUpInfo.setPhoneNumber(followUpInfo.getSubscriberId());
				}
			}
		}

		return followUpResult;
	}

	@Override
	public List<FollowUpInfo> retrieveFollowUps(@BANValue int ban, int Count) throws ApplicationException {

		List<FollowUpInfo> followUpResult = followUpDao.retrieveFollowUps(ban, Count);
		if (followUpResult != null && followUpResult.size() > 0) {
			Map<String, String> phoneNumbers = subscriberDao.retrievePhoneNumbersForBAN(ban);
			for (FollowUpInfo followUpInfo : followUpResult) {
				if (followUpInfo.getProductType() != null && followUpInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN)) {
					followUpInfo.setPhoneNumber(phoneNumbers.get(followUpInfo.getSubscriberId()));
				} else {
					followUpInfo.setPhoneNumber(followUpInfo.getSubscriberId());
				}
			}
		}

		return followUpResult;
	}

	@Override
	public FollowUpStatisticsInfo retrieveFollowUpStatistics(@BANValue int ban) throws ApplicationException {
		return followUpDao.retrieveFollowUpStatistics(ban);
	}

	@Override
	public int retrieveLastFollowUpIDByBanFollowUpType(@BANValue int ban, String followUpType) throws ApplicationException {
		return followUpDao.retrieveLastFollowUpIDByBanFollowUpType(ban, followUpType);
	}

	@Override
	public ProductSubscriberListInfo[] retrieveProductSubscriberLists(@BANValue int ban) throws ApplicationException {
		LOGGER.debug("Begin retrieveProductSubscriberLists for ban [" + ban + "]...");
		Collection<ProductSubscriberListInfo> subs = subscriberDao.retrieveProductSubscriberLists(ban);
		if (subs != null) {
			return subs.toArray(new ProductSubscriberListInfo[subs.size()]);
		} else {
			return new ProductSubscriberListInfo[0];
		}
	}

	/**
	 * Retrieves the bill parameters given a BAN
	 *
	 * @param banId BAN
	 * @returns BillParametersInfo
	 * @exception ApplicationException
	 */
	@Override
	public BillParametersInfo retrieveBillParamsInfo(@BANValue int banId) throws ApplicationException {
		return accountDao.retrieveBillParamsInfo(banId);
	}

	@Override
	public FleetInfo[] retrieveFleetsByBan(@BANValue int ban) throws ApplicationException {

		Collection<FleetInfo> fleets = fleetDao.retrieveFleetsByBan(ban);
		for (FleetInfo fleet : fleets) {
			int urbanId = fleet.getIdentity0().getUrbanId();
			int fleetId = fleet.getIdentity0().getFleetId();
			int accountsCounter = fleetDao.retrieveAssociatedAccountsCount(urbanId, fleetId);
			fleet.setAssociatedAccountsCount(accountsCounter);
		}

		return fleets.toArray(new FleetInfo[fleets.size()]);
	}

	@Override
	public AccountInfo retrieveAccountByImsi(String imsi) throws ApplicationException {

		int ban = accountDao.retrieveBanByImsi(imsi);
		try {
			BanValidator.validate(ban);
		} catch (ApplicationException e) {
			return null;
		}

		Collection<AccountInfo> accounts = this.retrieveAccountsByBan(new int[] { ban });
		if (accounts != null && accounts.size() > 0) {
			for (AccountInfo ai : accounts) {
				return ai;
			}
			return null;

		} else {
			return null;
		}
	}

	@Override
	public List<AccountInfo> retrieveAccountListByImsi(String imsi) throws ApplicationException {
		List<Integer> banList = accountDao.retrieveBanListByImsi(imsi);
		if (banList!=null && banList.isEmpty() == false) {
			List<AccountInfo> accountList = this.retrieveAccountsByBan(ArrayUtils.toPrimitive(banList.toArray(new Integer[0])));
			return accountList;
		}
		return null ;
	}
	
	@Override
	public List<String> retrieveSubscriberIdsByStatus(@BANValue int ban, char status, int maximum) throws ApplicationException {
		return subscriberDao.retrieveSubscriberIdsByStatus(ban, status, maximum);
	}

	@Override
	public List<AccountInfo> retrieveAccountsBySerialNumber(String serialNumber) throws ApplicationException {

		String[] imsi = new String[0];
		imsi = equipmentDao.getImsisByUsim(serialNumber);
		if (imsi.length == 0) {
			String usimID = equipmentDao.getUsimBySerialNumber(serialNumber);
			imsi = equipmentDao.getImsisByUsim(usimID);
		}
		if (imsi.length == 0) { // CDMA Equipment
			return accountDao.retrieveAccountsBySerialNumber(serialNumber);
		} else {
			AccountInfo account = retrieveAccountByImsi(imsi[0]);
			List<AccountInfo> accounts = new ArrayList<AccountInfo>();
			if (account != null) {
				accounts.add(account);
			}
			return accounts;
		}
	}

	@Override
	public AccountInfo retrieveAccountBySerialNumber(String serialNumber) throws ApplicationException {

		Collection<AccountInfo> accounts = retrieveAccountsBySerialNumber(serialNumber);
		if (accounts != null && accounts.size() > 0) {
			return accounts.iterator().next();
		} else {
			return null;
		}
	}

	@Override
	public MemoInfo retrieveLastCreditCheckMemoByBan(@BANValue int ban) throws ApplicationException {

		MemoInfo memo = null;
		try {
			memo = retrieveLastMemo(ban, CREDIT_CHECK);
		} catch (ApplicationException e) {
			throw e;
		}

		return memo;
	}

	@Override
	public List<String> retrieveSubscriberPhoneNumbersByStatus(@BANValue int ban, char status, int maximum) throws ApplicationException {
		return subscriberDao.retrieveSubscriberPhoneNumbersByStatus(ban, status, maximum);
	}

	@Override
	public String retrieveCorporateName(int id) throws ApplicationException {
		return accountDao.retrieveCorporateName(id);
	}

	@Override
	public int getClientAccountId(@BANValue int ban) throws ApplicationException {
		return accountDao.getClientAccountId(ban);
	}

	@Override
	public PaymentHistoryInfo retrieveLastPaymentActivity(@BANValue int ban) throws ApplicationException {
		return paymentDao.retrieveLastPaymentActivity(ban);
	}

	@Override
	public List<CreditInfo> retrieveCreditByFollowUpId(int followUpId) throws ApplicationException {
		return adjustmentDao.retrieveCreditByFollowUpId(followUpId);
	}

	@Override
	public SearchResultsInfo retrieveCredits(@BANValue int ban, Date fromDate, Date toDate, String billState, char level, String subscriber, int maximum) throws ApplicationException {
		return retrieveCredits(ban, fromDate, toDate, billState, level, "", subscriber, maximum);
	}

	@Override
	public SearchResultsInfo retrieveCredits(@BANValue int ban, Date fromDate, Date toDate, String billState, char level, String knowbilityOperatorId, String subscriber, int maximum)
			throws ApplicationException {
		return retrieveCredits(ban, fromDate, toDate, billState, knowbilityOperatorId, null, level, subscriber, maximum);
	}

	@Override
	public SearchResultsInfo retrieveCredits(@BANValue int ban, Date fromDate, Date toDate, String billState, String knowbilityOperatorId, String reasonCode, char level, String subscriber,
			int maximum) throws ApplicationException {

		SearchResultsInfo searchResultsInfo = new SearchResultsInfo();
		List<CreditInfo> returnList = new ArrayList<CreditInfo>();
		if (Account.BILL_STATE_ALL.equals(billState) || Account.BILL_STATE_UNBILLED.equals(billState)) {
			CreditInfoHolder creditsInfoHolder = adjustmentDao.retrieveUnbilledCredits(ban, fromDate, toDate, billState, knowbilityOperatorId, reasonCode, level, subscriber, maximum);
			if (creditsInfoHolder.getCreditInfo().size() > 0) {
				searchResultsInfo.setHasMore(creditsInfoHolder.hasMore());
				returnList.addAll(creditsInfoHolder.getCreditInfo());
			}
		}
		if (Account.BILL_STATE_ALL.equals(billState) || Account.BILL_STATE_BILLED.equals(billState)) {
			CreditInfoHolder creditsInfoHolder = adjustmentDao.retrieveBilledCredits(ban, fromDate, toDate, billState, knowbilityOperatorId, reasonCode, level, subscriber, maximum);
			if (creditsInfoHolder.getCreditInfo().size() > 0) {
				if (!searchResultsInfo.hasMore()) {
					searchResultsInfo.setHasMore(creditsInfoHolder.hasMore());
				}
				returnList.addAll(creditsInfoHolder.getCreditInfo());
			}
		}
		searchResultsInfo.setItems(returnList.toArray(new CreditInfo[returnList.size()]));

		return searchResultsInfo;
	}

	@Override
	public List<SubscriberInvoiceDetailInfo> retrieveSubscriberInvoiceDetails(@BANValue int banId, int billSeqNo) throws ApplicationException {
		return invoiceDao.retrieveSubscriberInvoiceDetails(banId, billSeqNo);
	}

	/**
	 * @deprecated since 2011 September release
	 */
	@Deprecated
	@Override
	public List<BillNotificationContactInfo> retrieveBillNotificationContacts(@BANValue int ban) throws ApplicationException {

		boolean hasEPost = hasEPostSubscription(ban);
		int clientId = getClientAccountId(ban);
		if (hasEPost) {
			return invoiceDao.retrieveBillNotificationContactsHasEPost(ban, clientId);
		} else {
			return invoiceDao.retrieveBillNotificationContactsHasEPostFalse(ban, clientId);
		}
	}

	@Override
	public List<ChargeInfo> retrieveBilledCharges(@BANValue int ban, int billSeqNo, String phoneNumber, Date from, Date toDate) throws ApplicationException {
		return invoiceDao.retrieveBilledCharges(ban, billSeqNo, phoneNumber, from, toDate);
	}

	@Override
	public int retrieveAssociatedTalkGroupsCount(FleetIdentityInfo fleeIdentity, @BANValue int ban) throws ApplicationException {
		return fleetDao.retrieveAssociatedTalkGroupsCount(fleeIdentity, ban);
	}

	@Override
	public AddressHistoryInfo[] retrieveAddressHistory(@BANValue int pBan, Date pFromDate, Date pToDate) throws ApplicationException {
		return addressDao.retrieveAddressHistory(pBan, pFromDate, pToDate);
	}

	/**
	 * @deprecated since 2011 September release
	 */
	@Deprecated
	@Override
	public String getPaperBillSupressionAtActivationInd(@BANValue int pBan) throws ApplicationException {
		return accountDao.getPaperBillSupressionAtActivationInd(pBan);
	}

	/**
	 * @deprecated since 2011 September release
	 */
	@Deprecated
	@Override
	public boolean hasEPostSubscription(@BANValue int ban) throws ApplicationException {
		return invoiceDao.hasEPostSubscription(ban);
	}

	@Override
	public boolean isFeatureCategoryExistOnSubscribers(@BANValue int ban, String pCategoryCode) throws ApplicationException {
		return subscriberDao.isFeatureCategoryExistOnSubscribers(ban, pCategoryCode);
	}

	@Override
	public AddressInfo retrieveAlternateAddressByBan(@BANValue int ban) throws ApplicationException {
		return addressDao.retrieveAlternateAddressByBan(ban);
	}

	@Override
	public double getPrepaidActivationCredit(String applicationId, String pUserId, PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException {
		// TODO Remove this after April/2014 release Surepay Retirement
		return pwcosDao.getPrepaidActivationCredit(applicationId, pPrepaidConsumerAccountInfo);
	}

	@Override
	public BusinessCreditIdentityInfo[] retrieveBusinessList(@BANValue int ban) throws ApplicationException {
		return accountDao.retrieveBusinessList(ban);
	}

	@Override
	public ConsumerNameInfo[] retrieveAuthorizedNames(@BANValue int ban) throws ApplicationException {
		return accountDao.retrieveAuthorizedNames(ban);
	}

	@Override
	public DepositHistoryInfo[] retrieveDepositHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {

		List<DepositHistoryInfo> depositList = depositDao.retrieveDepositHistory(ban, fromDate, toDate);
		if (depositList != null) {
			return depositList.toArray(new DepositHistoryInfo[depositList.size()]);
		} else {
			return new DepositHistoryInfo[0];
		}
	}

	@Override
	public DepositAssessedHistoryInfo[] retrieveDepositAssessedHistoryList(@BANValue int ban) throws ApplicationException {

		List<DepositAssessedHistoryInfo> depositList = depositDao.retrieveDepositAssessedHistoryList(ban);
		if (depositList != null) {
			return depositList.toArray(new DepositAssessedHistoryInfo[depositList.size()]);
		} else {
			return new DepositAssessedHistoryInfo[0];
		}
	}

	@Override
	public List<PaymentHistoryInfo> retrievePaymentHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {
		if (fromDate == null || toDate == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Date cannot be null on Retrieve Payment History call.", "");
		}
		return paymentDao.retrievePaymentHistory(ban, fromDate, toDate);
	}

	@Override
	public InvoicePropertiesInfo getInvoiceProperties(@BANValue int ban) throws ApplicationException {
		return invoiceDao.getInvoiceProperties(ban);
	}

	@Override
	public ChargeInfo[] retrieveCharges(@BANValue int ban, String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws ApplicationException {

		List<ChargeInfo> chargeList = adjustmentDao.retrieveCharges(ban, chargeCodes, billState, level, subscriberId, from, to, maximum);
		if (chargeList != null) {
			return chargeList.toArray(new ChargeInfo[chargeList.size()]);
		} else {
			return new ChargeInfo[0];
		}
	}

	@Override
	public DepositAssessedHistoryInfo[] retrieveOriginalDepositAssessedHistoryList(@BANValue int ban) throws ApplicationException {

		List<DepositAssessedHistoryInfo> historyList = depositDao.retrieveOriginalDepositAssessedHistoryList(ban);
		if (historyList != null) {
			return historyList.toArray(new DepositAssessedHistoryInfo[historyList.size()]);
		} else {
			return new DepositAssessedHistoryInfo[0];
		}
	}

	@Override
	public String retrieveHotlinedSubscriberPhoneNumber(@BANValue int ban) throws ApplicationException {
		return subscriberDao.retrieveHotlinedSubscriberPhoneNumber(ban);
	}

	@Override
	public List<InvoiceHistoryInfo> retrieveInvoiceHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {
		return invoiceDao.retrieveInvoiceHistory(ban, fromDate, toDate);
	}

	@Override
	public int retrieveAttachedSubscribersCountForTalkGroup(int urbanID, int fleetID, int talkGroupId, @BANValue int ban) throws ApplicationException {
		return fleetDao.retrieveAttachedSubscribersCountForTalkGroup(urbanID, fleetID, talkGroupId, ban);
	}

	@Override
	public CollectionHistoryInfo[] retrieveCollectionHistoryInfo(@BANValue int banId, Date fromDate, Date toDate) throws ApplicationException {
		return collectionDao.retrieveCollectionHistoryInfo(banId, fromDate, toDate);
	}

//	@Override
//	public SearchResultsInfo retrieveLetterRequests(@BANValue int banId, Date from, Date to, char level, String pSubscriber, int maximum) throws ApplicationException {
//		return letterDao.retrieveLetterRequests(banId, from, to, level, pSubscriber, maximum);
//	}

	@Override
	public List<PaymentActivityInfo> retrievePaymentActivities(@BANValue int banId, int paymentSeqNo) throws ApplicationException {
		return paymentDao.retrievePaymentActivities(banId, paymentSeqNo);
	}

	@Override
	public List<PaymentMethodChangeHistoryInfo> retrievePaymentMethodChangeHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {
		if (fromDate == null || toDate == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Date cannot be null on Retrieve Payment Method ChangeHistory call.", "");
		}
		return paymentDao.retrievePaymentMethodChangeHistory(ban, fromDate, toDate);
	}

	@Override
	public List<RefundHistoryInfo> retrieveRefundHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {
		if (fromDate == null || toDate == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Date cannot be null on Refund Payment History call.", "");
		}
		return paymentDao.retrieveRefundHistory(ban, fromDate, toDate);
	}

	@Override
	public List<ChargeInfo> retrieveRelatedChargesForCredit(@BANValue int pBan, double pChargeSeqNo) throws ApplicationException {
		return adjustmentDao.retrieveRelatedChargesForCredit(pBan, pChargeSeqNo);
	}

	@Override
	public HashMap<String, Integer> retrievePCSNetworkCountByBan(@BANValue int ban) throws ApplicationException {
		return subscriberDao.retrievePCSNetworkCountByBan(ban);
	}

	@Override
	public List<PoolingPricePlanSubscriberCountInfo> retrieveZeroMinutePoolingPricePlanSubscriberCounts(@BANValue int banId) throws ApplicationException {
		return subscriberDao.retrievePoolingPricePlanSubscriberCounts(banId, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL, true);
	}

	@Override
	public List<PoolingPricePlanSubscriberCountInfo> retrieveZeroMinutePoolingPricePlanSubscriberCounts(@BANValue int banId, int poolGroupId) throws ApplicationException {
		return subscriberDao.retrievePoolingPricePlanSubscriberCounts(banId, poolGroupId, true);
	}

	@Override
	public List<PoolingPricePlanSubscriberCountInfo> retrievePoolingPricePlanSubscriberCounts(@BANValue int banId) throws ApplicationException {
		return subscriberDao.retrievePoolingPricePlanSubscriberCounts(banId, PoolingPricePlanSubscriberCountInfo.POOLING_GROUP_ALL, false);
	}

	@Override
	public List<PoolingPricePlanSubscriberCountInfo> retrievePoolingPricePlanSubscriberCounts(@BANValue int banId, int poolGroupId) throws ApplicationException {
		return subscriberDao.retrievePoolingPricePlanSubscriberCounts(banId, poolGroupId, false);
	}

	@Override
	public List<ServiceSubscriberCount> retrieveServiceSubscriberCounts(@BANValue int banId, String[] serviceCodes, boolean includeExpired) throws ApplicationException {
		return subscriberDao.retrieveServiceSubscriberCounts(banId, serviceCodes, includeExpired);
	}

	@Override
	public List<PricePlanSubscriberCount> retrieveMinutePoolingEnabledPricePlanSubscriberCounts(@BANValue int banId, String[] poolingCoverageTypes) throws ApplicationException {

		if (poolingCoverageTypes != null) {
			return subscriberDao.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(banId, poolingCoverageTypes);
		} else {
			return subscriberDao.retrieveMinutePoolingEnabledPricePlanSubscriberCounts(banId);
		}
	}

	@Override
	public List<PricePlanSubscriberCountInfo> retrieveDollarPoolingPricePlanSubscriberCounts(@BANValue int banId, String productType) throws ApplicationException {
		return subscriberDao.retrievePricePlanSubscriberCountInfo(banId, productType);
	}

	@Override
	public List<PricePlanSubscriberCountInfo> retrieveShareablePricePlanSubscriberCount(@BANValue int ban) throws ApplicationException {
		return subscriberDao.retrieveShareablePricePlanSubscriberCount(ban);
	}

	@Override
	public List<CreditInfo> retrieveRelatedCreditsForCharge(@BANValue int pBan, double pChargeSeqNo) throws ApplicationException {
		return adjustmentDao.retrieveRelatedCreditsForCharge(pBan, pChargeSeqNo);
	}

	@Override
	public String validatePayAndTalkSubscriberActivation(String applicationId, String userId, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader)
			throws ApplicationException {
		return pwcosDao.validatePayAndTalkSubscriberActivation(applicationId, userId, prepaidConsumerAccountInfo, auditHeader);
	}

	@Override
	public SearchResultsInfo retrievePendingChargeHistory(@BANValue int pBan, Date pFromDate, Date pToDate, char level, String pSubscriber, int maximum) throws ApplicationException {
		return adjustmentDao.retrievePendingChargeHistory(pBan, pFromDate, pToDate, level, pSubscriber, maximum);
	}

	@Override
	public List<TalkGroupInfo> retrieveTalkGroupsByBan(@BANValue int ban) throws ApplicationException {
		return fleetDao.retrieveTalkGroupsByBan(ban);
	}

	@Override
	public List<StatusChangeHistoryInfo> retrieveStatusChangeHistory(@BANValue int ban, Date fromDate, Date toDate) throws ApplicationException {
		return accountDao.retrieveStatusChangeHistory(ban, fromDate, toDate);
	}

	@Override
	public ContactDetailInfo getCustomerContactInfo(@BANValue int ban) throws ApplicationException {
		return accountDao.getCustomerContactInfo(ban);
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType) throws ApplicationException {
		
		LOGGER.debug("Begin retrieveLastCreditCheckResultByBan for ban [" + ban + "]...");
		// Retrieve credit check results from KB
		CreditCheckResultInfo creditCheckResultInfo = creditCheckDao.retrieveLastCreditCheckResultByBan(ban, productType);
				
		// Retrieve and map CDA's credit worthiness attributes in place of KB credit check values via WirelessCreditManagementSvc v2.0
		LOGGER.debug("Begin CDA getCreditWorthiness(ban) for ban [" + ban + "]...");
		CreditAssessmentInfo creditAssessmentInfo = wirelessCreditManagementServiceDao.getCreditWorthiness(ban);
		creditCheckResultInfo.copyCDACreditAssessmentInfo(creditAssessmentInfo);

		return creditCheckResultInfo;
	}

	@Override
	public CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType) throws ApplicationException {
		// Retrieve credit check results from KB
		return creditCheckDao.retrieveLastCreditCheckResultByBan(ban, productType);
	}

	@Override
	public BillingPropertyInfo retrieveBillingInformation(@BANValue int billingAccountNumber) throws ApplicationException {

		BillingPropertyInfo billingPropertyInfo = contactDao.retrieveBillingInformation(billingAccountNumber);
		if (billingPropertyInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
		}

		return billingPropertyInfo;
	}

	@Override
	public ContactPropertyInfo retrieveContactInformation(@BANValue int billingAccountNumber) throws ApplicationException {

		ContactPropertyInfo contactPropertyInfo = contactDao.retrieveContactInformation(billingAccountNumber);
		if (contactPropertyInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
		}

		return contactPropertyInfo;
	}

	@Override
	public PersonalCreditInfo retrievePersonalCreditInformation(int ban) throws ApplicationException {

		PersonalCreditInfo personalCreditInfo = accountDao.retrievePersonalCreditInformation(ban);
		if (personalCreditInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
		}

		return personalCreditInfo;
	}

	@Override
	public BusinessCreditInfo retrieveBusinessCreditInformation(int ban) throws ApplicationException {
		BusinessCreditInfo businessCreditInfo = accountDao.retrieveBusinessCreditInformation(ban);
		if (businessCreditInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.BAN_NOT_FOUND, "BAN not found", "");
		}

		return businessCreditInfo;
	}

	private ReferenceDataFacade getReferenceDataFacade() {
		if (refDataFacade == null) {
			refDataFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		return refDataFacade;
	}

	private SubscriberLifecycleHelper getSubscriberLifecycleHelper() {
		if (subscriberLifecycleHelper == null) {
			subscriberLifecycleHelper = EJBUtil.getHelperProxy(SubscriberLifecycleHelper.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER);
		}
		return subscriberLifecycleHelper;
	}

	private SubscriberLifecycleFacade getSubscriberLifecycleFacade() {
		if (subscriberLifecycleFacade == null) {
			subscriberLifecycleFacade = EJBUtil.getHelperProxy(SubscriberLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE);
		}
		return subscriberLifecycleFacade;
	}

	private Date getEffectiveDate(Date effectiveDate) throws ApplicationException {

		if (effectiveDate == null) {
			try {
				return getReferenceDataFacade().getLogicalDate();
			} catch (TelusException e) {
				throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "Failed to get logicalDate", "", e);
			}
		}

		return effectiveDate;
	}

	private void checkSubscriberCountForSubscriberQuery(AccountInfo account) throws ApplicationException {

		int subCount = account.getSubscriberCount();
		int subLimit = AppConfiguration.getDataSharingSubscriberLimit();
		if (subCount > subLimit) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.SUBSCRIBER_QUERY_LIMIT_EXCEEDED,
					"subscriber count(" + subCount + ") on BAN[" + account.getBanId() + "] exceed query limit:" + subLimit, "");
		}
	}

	@Override
	public String[] retrieveSubscriberIdsByServiceFamily(AccountInfo account, String familyTypeCode, Date effectiveDate) throws ApplicationException {

		checkSubscriberCountForSubscriberQuery(account);
		if (AppConfiguration.getFamilyTypeQueryExceptions().contains(account.getAccountType() + familyTypeCode)) {
			return new String[0];
		}

		return subscriberDao.retrieveSubscriberIdsByServiceFamily(account.getBanId(), familyTypeCode, getEffectiveDate(effectiveDate));
	}

	@Override
	public String[] retrieveSubscriberIdsByServiceFamily(@BANValue int banId, String familyTypeCode, Date effectiveDate) throws ApplicationException {
		AccountInfo account = retrieveLwAccountByBan(banId);
		return retrieveSubscriberIdsByServiceFamily(account, familyTypeCode, effectiveDate);
	}

	@Override
	public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(AccountInfo account, String[] dataSharingGroupCodes, Date effectiveDate) throws ApplicationException {
		checkSubscriberCountForSubscriberQuery(account);
		return subscriberDao.retrieveSubscribersByDataSharingGroupCodes(account.getBanId(), dataSharingGroupCodes, getEffectiveDate(effectiveDate));
	}

	@Override
	public SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(@BANValue int banId, String[] dataSharingGroupCodes, Date effectiveDate) throws ApplicationException {
		AccountInfo account = retrieveLwAccountByBan(banId);
		return retrieveSubscribersByDataSharingGroupCodes(account, dataSharingGroupCodes, effectiveDate);
	}

	@Override
	public SubscriberDataSharingDetailInfo[] retrieveSubscriberDataSharingInfoList(@BANValue int banId, String[] dataSharingGroupCodes) throws ApplicationException {

		AccountInfo accountInfo = retrieveLwAccountByBan(banId);

		// Check for account status
		if (accountInfo.getStatus() != AccountSummary.STATUS_OPEN && accountInfo.getStatus() != AccountSummary.STATUS_SUSPENDED) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.ACCOUNT_STATUS_MUST_BE_OPEN_OR_SUSPENDED,
					"Account: " + banId + " has status: " + accountInfo.getStatus() + ". Its status must be either open or suspended.", "");
		}

		// Check for Customer Accounts only
		// Change for Channel Enablement APR2017 - enable Corporate Employee
		if (accountInfo.getAccountType() != AccountSummary.ACCOUNT_TYPE_CONSUMER && !accountInfo.isCorporateEmployee()) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ErrorCodes.ACCOUNT_TYPE_MUST_BE_CONSUMER, "Account: " + banId + " has type: " + accountInfo.getAccountType() + " and subtype: "
					+ accountInfo.getAccountSubType() + ". Only Consumer and Corporate Employee Accounts are allowed.", "");
		}

		// Check the number of subscribers in the account
		checkSubscriberCountForSubscriberQuery(accountInfo);

		// Retrieve active/suspended subscribers of the account
		int maxSubscriberCount = AppConfiguration.getDataSharingSubscriberLimit();
		Collection<SubscriberInfo> subscriberInfoList = getSubscriberLifecycleHelper().retrieveSubscriberListByBAN(banId, maxSubscriberCount, false);

		// Retrieve the main subscribers' data sharing info from DAO Update:
		// dataSharingGroupCodes not passed into method because we want to know
		// all of the data sharing socs
		// we will do further filtering when we map the data to the
		// SubscriberDataSharingDetailInfo below instead (Wilson, June 22, 2015)
		Collection<DataSharingResultInfo> dataSharingResultInfoList = null;
		if (AppConfiguration.useDataSharingInfoSqlPackage()) {
			dataSharingResultInfoList = subscriberDao.retrieveSubscriberDataSharingInfoListUsingSqlPackage(banId, null);
		} else {
			dataSharingResultInfoList = subscriberDao.retrieveSubscriberDataSharingInfoList(banId, null);
		}

		// Collect all price plan and service SOCs from the
		// DataSharingResultInfoList
		String[] socCodes = SubscriberDataSharingDetailInfo.collectSocCodeSetFromResultList(dataSharingResultInfoList);

		// Retrieve family types for the collected price plan and service SOC
		// codes
		Map<String, List<String>> familyTypesSocMap = subscriberDao.retrieveFamilyTypesBySocs(socCodes);

		// Map the straight result into structured data object
		Collection<SubscriberDataSharingDetailInfo> subscriberDataSharingDetailInfoList = SubscriberDataSharingInfoMapper.mapResultToDetailInfo(dataSharingResultInfoList, subscriberInfoList,
				familyTypesSocMap, dataSharingGroupCodes);

		// Change collection into array and return
		if (subscriberDataSharingDetailInfoList != null) {
			return subscriberDataSharingDetailInfoList.toArray(new SubscriberDataSharingDetailInfo[subscriberDataSharingDetailInfoList.size()]);
		} else {
			return null;
		}
	}

	/*
	 * *
	 *
	 * TODO Opportunity to optimize the query in future
	 *
	 * Need to investigate if we can build one query to return a list of memo
	 * instead of making multiple calls. Query refers to
	 * memo_utility_pkg.GetLastMemo
	 *
	 * Need to look at the opportunity to rebuild a new query to only retrieve
	 * total actual payment amount. Query refers to
	 * history_utility_pkg.getpaymenthistory
	 *
	 * Need to look at the opportunity to rebuild a new query to only retrieve
	 * total actual deposit amount. Query refer to
	 * HISTORY_UTILITY_PKG.GetAccountDepositHistory
	 *
	 */
	@Override
	public SubscriberEligibilitySupportingInfo getSubscriberEligiblitySupportingInfo(@BANValue int ban, String[] memoTypeList, Date dateFrom, Date dateTo) throws ApplicationException {

		SubscriberEligibilitySupportingInfo eligibilitySupportInfo = null;
		Memo[] memoList = new Memo[memoTypeList.length];
		double totalPaymentAmount = 0;
		double totalDepositHeld = 0;

		try {
			for (int i = 0; i < memoTypeList.length; i++) {
				memoList[i] = new MemoInfo();
				memoList[i] = retrieveLastMemo(ban, memoTypeList[i]);
			}
		} catch (ApplicationException ae) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ae.getErrorCode(), ae.getErrorMessage() + " : Exception occured while retrieving Last Memo", "", ae);
		}

		try {
			List<PaymentHistoryInfo> paymentHistoryInfoList = retrievePaymentHistory(ban, dateFrom, dateTo);
			for (PaymentHistoryInfo paymentHistoryInfo : paymentHistoryInfoList) {
				try {
					totalPaymentAmount = totalPaymentAmount + paymentHistoryInfo.getActualAmount();
				} catch (NullPointerException ne) {
					continue;
				}
			}
		} catch (ApplicationException ae) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ae.getErrorCode(), ae.getErrorMessage() + " : Exception occured while retrieving payment history", "", ae);
		}

		try {
			List<DepositHistoryInfo> depositHistoryInfoList = Arrays.asList(retrieveDepositHistory(ban, dateFrom, dateTo));
			for (DepositHistoryInfo depositHistoryInfo : depositHistoryInfoList) {
				try {
					totalDepositHeld = totalDepositHeld + depositHistoryInfo.getDepositPaidAmount();
				} catch (NullPointerException ne) {
					continue;
				}
			}
		} catch (ApplicationException ae) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, ae.getErrorCode(), ae.getErrorMessage() + " : Exception occured while retrieving deposit history: ", "", ae);
		}

		eligibilitySupportInfo = new SubscriberEligibilitySupportingInfo();
		eligibilitySupportInfo.setMemoList(memoList);
		eligibilitySupportInfo.setTotalDepositHeld(totalDepositHeld);
		eligibilitySupportInfo.setTotalPaymentAmount(totalPaymentAmount);

		return eligibilitySupportInfo;
	}

	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return testPointDao.testKnowbilityDataSource();
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}

	@Override
	public TestPointResultInfo testEcpcsDataSource() {
		return testPointDao.testEcpcsDataSource();
	}

	@Override
	public TestPointResultInfo testConeDataSource() {
		return testPointDao.testConeDataSource();
	}

	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}

	@Override
	public TestPointResultInfo testEasDataSource() {
		return testPointDao.testEasDataSource();
	}

	@Override
	public TestPointResultInfo getRaUtilityPkgVersion() {
		return testPointDao.getRaUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo testPrepaidWirelessCustomerOrderService() {
		return pwcosDao.test();
	}

	@Override
	public TestPointResultInfo testPrepaidSubscriberService() {
		return pssDao.test();
	}

	@Override
	public TestPointResultInfo getSubscriberPkgVersion() {
		return testPointDao.getSubscriberPkgVersion();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public TestPointResultInfo getMemoUtilityPkgVersion() {
		return testPointDao.getMemoUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getUsageUtilityPkgVersion() {
		return testPointDao.getUsageUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getHistoryUtilityPkgVersion() {
		return testPointDao.getHistoryUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getPortalNotificationPkgVersion() {
		return testPointDao.getPortalNotificationPkgVersion();
	}

	@Override
	public TestPointResultInfo getFleetUtilityPkgVersion() {
		return testPointDao.getFleetUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getClientEquipmentPkgVersion() {
		return testPointDao.getClientEquipmentPkgVersion();
	}

	@Override
	public TestPointResultInfo getCreditCheckResultPkgVersion() {
		return testPointDao.getCrdCheckResultPkgVersion();
	}

	@Override
	public TestPointResultInfo getAccRetrievalPkgVersion() {
		return testPointDao.getAccRetrievalPkgVersion();
	}

	@Override
	public TestPointResultInfo getAccAttribRetrievalPkgVersion() {
		return testPointDao.getAccAttrbRetrievalPkgVersion();
	}

	@Override
	public TestPointResultInfo getSubscriberCountPkgVersion() {
		return testPointDao.getSubscriberCountPkgVersion();
	}

	@Override
	public TestPointResultInfo getInvoicePkgVersion() {
		return testPointDao.getInvoicePkgVersion();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<List<CreditInfo>> retrieveRelatedCreditsForChargeList(List chargeIdentifierInfoList) throws ApplicationException {

		List<List<CreditInfo>> creditInfoList = new ArrayList<List<CreditInfo>>();
		List<ChargeIdentifierInfo> chargeIdentifierInfos = chargeIdentifierInfoList;
		for (ChargeIdentifierInfo chargeIdentifierInfo : chargeIdentifierInfos) {
			List<CreditInfo> creditInfo = adjustmentDao.retrieveRelatedCreditsForCharge(chargeIdentifierInfo.getAccountNumber(), chargeIdentifierInfo.getChargeSequenceNumber());
			creditInfoList.add(creditInfo);
		}

		return creditInfoList;
	}

	@Override
	public String retrieveChangedSubscriber(int ban, String subscriberId, String productType, Date searchFromDate, Date searchToDate) throws ApplicationException {
		return subscriberDao.retrieveChangedSubscriber(ban, subscriberId, productType, searchFromDate, searchToDate);
	}

	@Override
	public List<Double> retrieveAdjustedAmounts(int ban, String adjustmentReasonCode, String subscriberId, Date searchFromDate, Date searchToDate) throws ApplicationException {
		return adjustmentDao.retrieveAdjustedAmounts(ban, adjustmentReasonCode, subscriberId, searchFromDate, searchToDate);
	}

	@Override
	public List<CreditInfo> retrieveApprovedCreditByFollowUpId(int banId, int followUpId) throws ApplicationException {
		return adjustmentDao.retrieveApprovedCreditByFollowUpId(banId, followUpId);
	}

	@Override
	public CreditInfo retrieveCreditById(int banId, int entSeqNo) throws ApplicationException {
		return adjustmentDao.retrieveCreditById(banId, entSeqNo);
	}

	private int getTenureInMonths(Date accountstartServiceDate) {

		int tenure = 0;
		if (accountstartServiceDate != null) {
			Calendar today = Calendar.getInstance();
			Calendar startServiceDate = Calendar.getInstance();
			// requirements said subscriber.getStartServiceDate() not
			// account.getStartServiceDate()
			startServiceDate.setTime(accountstartServiceDate);
			tenure = (today.get(Calendar.YEAR) * 12 + today.get(Calendar.MONTH)) - (startServiceDate.get(Calendar.YEAR) * 12 + startServiceDate.get(Calendar.MONTH));
		}

		return tenure;
	}

	private boolean isCollectionActivityPresent(AccountInfo accountInfo, int termInMonths) {

		if (accountInfo.getFinancialHistory().isDelinquent() || accountInfo.getFinancialHistory().isHotlined()) {
			return true;
		}

		MonthlyFinancialActivity[] financialActivity = accountInfo.getFinancialHistory().getMonthlyFinancialActivity();
		if (termInMonths > financialActivity.length) {
			termInMonths = financialActivity.length;
		}

		int firstMonth = financialActivity.length - termInMonths;
		for (int i = firstMonth; i < financialActivity.length; i++) {
			if (financialActivity[i].getDishonoredPaymentCount() > 0) {
				return true;
			}
			String activityCode = financialActivity[i].getActivity();
			if (activityCode != null && ((activityCode.equals("S") || activityCode.equals("h") || activityCode.equals("b")))) {
				return true;
			}
		}

		return false;
	}

	private InternationalServiceEligibilityCheckCriteria getInternationalServiceEligibilityCheckCriteria(AccountInfo accountInfo) {

		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();
		StringBuffer buffer = new StringBuffer();
		buffer.append(accountInfo.getAccountType()).append(accountInfo.getAccountSubType());
		criteria.setAccountCombinedType(buffer.toString());
		criteria.setBrandId(accountInfo.getBrandId());
		criteria.setCreditClass(accountInfo.getCreditCheckResult().getCreditClass());
		criteria.setTenure(getTenureInMonths(accountInfo.getStartServiceDate()));
		criteria.setCollectionActivityPresent(isCollectionActivityPresent(accountInfo, INTERNATIONAL_SERVICE_COLLECTION_ACTIVITY_TERM_IN_MONTHS));
		criteria.setNewAccount(accountInfo.getStartServiceDate() == null);

		return criteria;
	}

	@Override
	public InternationalServiceEligibilityCheckResultInfo checkInternationalServiceEligibility(int ban) throws ApplicationException {
		AccountInfo accountInfo = this.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		InternationalServiceEligibilityCheckCriteria criteria = getInternationalServiceEligibilityCheckCriteria(accountInfo);
		EligibilityCheckStrategy strategy = AppConfiguration.getInternationalServiceEligibilityEvaluationStrategy();

		return (InternationalServiceEligibilityCheckResultInfo) strategy.evaluate(criteria);
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public CreditCheckResultDeposit[] retrieveDepositsByBan(@BANValue int ban) throws ApplicationException {
		
		LOGGER.debug("Begin retrieveDepositsByBan for ban [" + ban + "]...");
		// Retrieve and map CDA's credit worthiness attributes in place of KB credit check values via WirelessCreditManagementSvc v2.0
		CreditAssessmentInfo info = wirelessCreditManagementServiceDao.getCreditWorthiness(ban);
		if (info.getCreditWorthiness() != null && info.getCreditWorthiness().getCreditProgram() != null) {
			if (StringUtils.equals(CreditProgramInfo.TYPE_DEPOSIT, info.getCreditWorthiness().getCreditProgram().getCreditProgramType())
					|| StringUtils.equals(CreditProgramInfo.TYPE_DECLINED, info.getCreditWorthiness().getCreditProgram().getCreditProgramType())
					|| StringUtils.equals(CreditProgramInfo.TYPE_NDP, info.getCreditWorthiness().getCreditProgram().getCreditProgramType())) {
				CreditCheckResultDepositInfo cdaDeposit = new CreditCheckResultDepositInfo();
				cdaDeposit.setProductType(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR);
				cdaDeposit.setDeposit(info.getCreditWorthiness().getCreditProgram().getSecurityDepositAmount());

				return new CreditCheckResultDepositInfo[] { cdaDeposit };
			}
		}

		return creditCheckDao.retrieveDepositsByBan(ban);
	}
	
	// Updated for CDA phase 1B July 2018
	private CreditCheckResultInfo retrieveLastCreditCheckResultAndDepositsByBan(int ban, String productType) throws ApplicationException {
		
		LOGGER.debug("Begin retrieveLastCreditCheckResultAndDepositsByBan for ban [" + ban + "]...");
		// Retrieve credit check results from KB
		CreditCheckResultInfo creditCheckResultInfo = creditCheckDao.retrieveLastCreditCheckResultByBan(ban, productType);

		// Retrieve and map CDA's credit worthiness attributes in place of KB credit check values via WirelessCreditManagementSvc v2.0
		LOGGER.debug("Begin CDA getCreditWorthiness(ban) for ban [" + ban + "]...");
		CreditAssessmentInfo creditAssessmentInfo = wirelessCreditManagementServiceDao.getCreditWorthiness(ban);
		creditCheckResultInfo.copyCDACreditAssessmentInfo(creditAssessmentInfo);
		if (ArrayUtils.isEmpty(creditCheckResultInfo.getDeposits())) {
			creditCheckResultInfo.setDeposits(creditCheckDao.retrieveDepositsByBan(ban));
		}
		
		return creditCheckResultInfo;
	}

	@Override
	public String getNextSeatGroupId() throws ApplicationException {

		String id = maxVoipLineDao.getNextSeatGroupId();
		if (id == null) {
			throw new ApplicationException(SystemCodes.CMB_AIH_EJB, "getNextSeatGroupId(): DB select of SEQ_SERVICE_COLL_GROUP_ID.NEXTVAL failed", "");
		}

		return id;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getMaxVoipLineList(int ban, long subscriptionId) throws ApplicationException {
		return maxVoipLineDao.getMaxVoipLineList(ban, subscriptionId);
	}

	@Override
	public void createMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo) throws ApplicationException {
		maxVoipLineDao.createMaxVoipLine(maxVoipLineInfo);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updateMaxVoipLine(List maxVoipLineInfoList) throws ApplicationException {
		maxVoipLineDao.updateMaxVoipLineList(maxVoipLineInfoList);
	}

}
