/*
 * $Id$
 * %E% %W%  
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.w3c.dom.Document;

import com.telus.api.ApplicationException;
import com.telus.api.AuthenticationException;
import com.telus.api.HistorySearchException;
import com.telus.api.InvalidMultiSubscriberOperationException;
import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.AddressHistory;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.Cheque;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.DepositAssessedHistory;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpStatistics;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.InternationalServiceEligibilityCheckResult;
import com.telus.api.account.InvalidBillCycleChangeException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.ManualCreditCheckRequest;
import com.telus.api.account.Memo;
import com.telus.api.account.MonthlyFinancialActivity;
import com.telus.api.account.PaymentFailedException;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PaymentMethodChangeHistory;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ProrationMinutes;
import com.telus.api.account.RefundHistory;
import com.telus.api.account.RewardRedemptionResult;
import com.telus.api.account.SearchResults;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.StatusChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.account.VoiceUsageSummaryException;
import com.telus.api.account.WebUsageSummary;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.ClientConsentIndicator;
import com.telus.api.reference.CreditCheckDepositChangeReason;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.PaymentSourceType;
import com.telus.api.reference.PoolingGroup;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.Segmentation;
import com.telus.api.reference.ShareablePricePlan;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.JNDINames;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.FinancialHistoryInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PricePlanSubscriberCountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.ProrationMinutesInfo;
import com.telus.eas.account.info.RefundHistoryInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageServiceDirectionInfo;
import com.telus.eas.account.info.VoiceUsageServiceInfo;
import com.telus.eas.account.info.VoiceUsageServicePeriodInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.framework.eligibility.EligibilityCheckStrategy;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.PagerSubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.eligibility.interservice.InternationalServiceEligibilityCheckCriteria;
import com.telus.provider.portability.TMPortRequestSO;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.DOMTransformer;
import com.telus.provider.util.Logger;
import com.telus.provider.util.Mailer;
import com.telus.provider.util.MikeSharedFleetApplication;
import com.telus.provider.util.ProviderAddressExceptionTranslator;
import com.telus.provider.util.ProviderCDAExceptionTranslator;
import com.telus.provider.util.ProviderCreditCardExceptionTranslator;

public class TMAccount extends TMAccountSummary implements Account {

	private static final String FOLLOWUP_CORPORATE_DESTINATION = "12633";
	private static final String FOLLOWUP_CORPORATE_NEW_ACCOUNT = "CMCN";
	private static final String FOLLOWUP_CORPORATE_ADDON = "CMCA";

	
	//private static final String FOLLOWUP_FLEET_APPROVAL        = "CMFA";

	private static final String MEMO_SUBSCRIBER_ADDON = "ADON";
	private static final String MEMO_DEPOSIT_PAYMENT = "ADEP";
	private static final String MEMO_UPDATE_BRAND = "BRN";

	public static final String REFER_TO_CREDIT_EN = "Please refer to Credit";
	public static final String REFER_TO_CREDIT_FR = "Veuillez vous adresser au Crédit";
	public static final long ONE_DAY = 1000L * 60L * 60L * 24L;
	public static final int MIN_TENURE_FOR_PAY_ARRANG_ELIGIBILITY_IN_DAYS = 183;

	public static final String[] VALID_CREDIT_CLASS_FOR_PAY_ARRANG_ELIGIBILITY = { "C", "B", "D" };
	public static final String[] COLLECTION_ACTIVITY_CODES = { "B", "I", "J", "Z", "X", "O" };

	public static final int MAX_NUMBER_OF_MONTHS_NSF_BACKOUT = 6;
	public static final int MAX_NUMBER_OF_MONTHS_DISHONOURED = 6;

	public static final String FOLLOWUP_PAYMENT_DISHONOURED = "DHNR";
	public static final String FOLLOWUP_NOTIFICATION_DISHONOURED = "DHNR";
	public static final String[] CAM_PAYMENT_ARRANGEMENT_FOLLOWUPS = { "PYMT" };
	//public static final String REFER_TO_CREDIT_FR = "Consulter un analyste de crédit";

	// for rouding usage summary minutes.
	private static final String USAGE_MINUTE_ROUNDING_UP = "up";
	private static final String USAGE_MINUTE_ROUNDING_DOWN = "down";

	public static final String[] CLP_CREDIT_CLASSES = { "X", "L" };
	private static final String CLM_restoreAccountReasonCode = "CLMR";
	private static final String CLM_suspendReasonCode = "CLMS";

	private static final int MILLI_SEC_PER_DAY = 86400000;

	private static final String[] CollectionRelatedFollowups = { "CLGN", "CLRF" };

	//Corporate Minute Pooling
	public static final String POOLING_KEY = "Pooling";
	public static final String ZERO_MINUTE_POOLING_KEY = "ZeroMinutePooling";
	public static final String DOLLAR_POOLING_KEY = "DollarPooling";
	public static final String SHAREABLE_KEY = "Shareable";
	private HashMap pricePlanSubscriberCountHashMapCache = new HashMap();

	/**
	 * @link aggregation
	 */
	private final AccountInfo delegate;
	private final TMAddress address;
	private TMAddress oldAddress;
	private boolean createdCorporateNewAccountFollowUp;

	private CancellationPenalty cancellationPenalty;
	private HashMap phoneNumbers = new HashMap();
	private TMPortRequestSO portRequestSO = null;

	private int brandId;

	private String oldPin;

	protected TMManualCreditCheckRequest manualCreditRequest = null;

	private static final int INTERNATIONAL_SERVICE_COLLECTION_ACTIVITY_TERM_IN_MONTHS = 6;
	private EligibilityCheckStrategy internationalServiceEligibilityEvaluationStrategy = null;

	private CreditCheckResult creditCheckResult = null;
	private boolean productSubscriberListCached = false;
	private Boolean communicationSuiteEligible = null;

	/*
	 * Added by Sachin on 01-06-2010
	 * Added two new constants for follow ups
	 */
	public static final String FOLLOWUP_CLP_PAYMENT = "CLPY";
	public static final String FOLLOWUP_RESTORE_STATUS = "CLRS";

	private AccountLifecycleManager accountLifecycleManager;
	private AccountLifecycleFacade accountLifecycleFacade;
	private AccountInformationHelper accountInformationHelper;

	private boolean blockRuleStatus;

	public TMAccount(TMProvider provider, AccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
		address = new TMAddress(provider, delegate.getAddress0());
		manualCreditRequest = new TMManualCreditCheckRequest(provider, this);
		accountLifecycleManager = provider.getAccountLifecycleManager();
		accountLifecycleFacade = provider.getAccountLifecycleFacade();
		accountInformationHelper = provider.getAccountInformationHelper();
		blockRuleStatus = getBlockRuleStatus();
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------

	@Override
	public String getAdditionalLine() {
		return delegate.getAdditionalLine();
	}

	@Override
	public void setAdditionalLine(String additionalLine) {
		delegate.setAdditionalLine(additionalLine);
	}

	@Override
	public String getEmail() {
		return delegate.getEmail();
	}

	@Override
	public void setEmail(String email) {
		delegate.setEmail(email);
	}

	private boolean getBlockRuleStatus() {
		boolean block = false;
		try {
			block = provider.getAccountLifecycleFacade().isEnterpriseManagedData(getBrandId(), getAccountType(), getAccountSubType(), getProductType(), AccountSummary.PROCESS_TYPE_ACCOUNT_UPDATE);
		} catch (Throwable e) {
			Logger.warning("Error while executing EnterpriseManagedData block rule : " + e);
		}
		return block;
	}

	@Override
	public void setPin(String pin) {
		if (!(delegate.getBanId() != 0 && (isBlockDirectUpdate()) && (blockRuleStatus))) {
			if (pin != null) {
				oldPin = delegate.getPin();
			}
			delegate.setPin(pin);
		}
	}

	@Override
	public String getLanguage() {
		return delegate.getLanguage();
	}

	@Override
	public void setLanguage(String language) {
		delegate.setLanguage(language);
	}

	@Override
	public int getBillCycle() {
		return delegate.getBillCycle();
	}

	@Override
	public int getBillCycleCloseDay() {
		return delegate.getBillCycleCloseDay();
	}

	@Override
	public FinancialHistory getFinancialHistory() {
		TMFinancialHistory tm = decorate(delegate.getFinancialHistory0());
		tm.setBanId(this.getBanId());
		tm.setAccountStartServiceDate(getStartServiceDate());
		return tm;
	}

	public FinancialHistoryInfo getFinancialHistory0() {
		return delegate.getFinancialHistory0();
	}

	@Override
	public CreditCheckResult getCreditCheckResult() {

		try {
			if (creditCheckResult == null) {
				refreshCreditCheckResult();
				creditCheckResult = decorate((CreditCheckResultInfo) delegate.getCreditCheckResult());
			}
		} catch (TelusAPIException tapie) {
			Logger.debug("Error retrieving credit result for ban = " + getBanId() + ". " + tapie);
			return null;
		}

		return creditCheckResult;
	}

	@Override
	public int getActiveSubscribersCount() {
		if (delegate.getActiveSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();

			int count = 0;
			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++)
					if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
							|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS)
									&& getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
							|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
						count += productSubscriberLists[i].getActiveSubscribersCount();
			}
			delegate.setActiveSubscribersCount(count);
		}
		return delegate.getActiveSubscribersCount();
	}

	@Override
	public int getSuspendedSubscribersCount() {
		if (delegate.getSuspendedSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();
			int count = 0;

			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++)
					if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
							|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS)
									&& getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
							|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
						count += productSubscriberLists[i].getSuspendedSubscribersCount();
			}
			delegate.setSuspendedSubscribersCount(count);
		}
		return delegate.getSuspendedSubscribersCount();
	}

	@Override
	public int getReservedSubscribersCount() {
		if (delegate.getReservedSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();
			int count = 0;

			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++)
					if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
							|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS)
									&& getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
							|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
						count += productSubscriberLists[i].getReservedSubscribersCount();

			}
			delegate.setReservedSubscribersCount(count);
		}
		return delegate.getReservedSubscribersCount();
	}

	@Override
	public int getCancelledSubscribersCount() {
		if (delegate.getCancelledSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();
			int count = 0;

			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++)
					if ((productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_PCS) || productSubscriberLists[i].getProductType().equals(Subscriber.PRODUCT_TYPE_IDEN))
							|| ((getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER || getAccountType() == AccountSummary.ACCOUNT_TYPE_BUSINESS)
									&& getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_REGULAR)
							|| (getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PAGER_BOXED))
						count += productSubscriberLists[i].getCancelledSubscribersCount();

			}
			delegate.setCancelledSubscribersCount(count);
		}
		return delegate.getCancelledSubscribersCount();
	}

	@Override
	public int getSubscriberCount() {
		return getActiveSubscribersCount() + getSuspendedSubscribersCount() + getReservedSubscribersCount() + getCancelledSubscribersCount();
	}

	/**
	 * Unlike getAcitveSubscriberCount(), this method returns active subscriber counts regardless to the account type and subtype.
	 */
	@Override
	public int getAllActiveSubscribersCount() {
		if (delegate.getAllActiveSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();

			int count = 0;
			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++) {
					count += productSubscriberLists[i].getActiveSubscribersCount();
				}
			}
			delegate.setAllActiveSubscribersCount(count);
		}

		return delegate.getAllActiveSubscribersCount();
	}

	@Override
	public int getAllSuspendedSubscribersCount() {

		if (delegate.getAllSuspendedSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();

			int count = 0;
			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++) {
					count += productSubscriberLists[i].getSuspendedSubscribersCount();
				}
			}
			delegate.setAllSuspendedSubscribersCount(count);
		}

		return delegate.getAllSuspendedSubscribersCount();
	}

	@Override
	public int getAllReservedSubscribersCount() {

		if (delegate.getAllReservedSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();

			int count = 0;
			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++) {
					count += productSubscriberLists[i].getReservedSubscribersCount();
				}
			}
			delegate.setAllReservedSubscribersCount(count);
		}

		return delegate.getAllReservedSubscribersCount();
	}

	@Override
	public int getAllCancelledSubscribersCount() {

		if (delegate.getAllCancelledSubscribersCount() == -1) {
			ProductSubscriberListInfo[] productSubscriberLists = (ProductSubscriberListInfo[]) getProductSubscriberLists();

			int count = 0;
			if (productSubscriberLists != null) {
				for (int i = 0; i < productSubscriberLists.length; i++) {
					count += productSubscriberLists[i].getCancelledSubscribersCount();
				}
			}
			delegate.setAllCancelledSubscribersCount(count);
		}

		return delegate.getAllCancelledSubscribersCount();
	}

	@Override
	public int getAllSubscriberCount() {
		return getAllActiveSubscribersCount() + getAllSuspendedSubscribersCount() + getAllReservedSubscribersCount() + getAllCancelledSubscribersCount();
	}

	@Override
	public String getSpecialInstructions() {
		String specialInstructions = null;
		try {
			specialInstructions = getSpecialInstructionsMemo().getText();
		} catch (Exception e) {
			specialInstructions = "";
		}
		return specialInstructions;
	}

	@Override
	public String getCorporateId() {
		return delegate.getCorporateId();
	}

	@Override
	public boolean isHotlined() {
		return delegate.getFinancialHistory().isHotlined();
	}

	@Override
	public boolean isHandledBySubscriberOnly() {
		return delegate.isHandledBySubscriberOnly();
	}

	@Override
	public void setHotlined(boolean hotlined) {
		delegate.getFinancialHistory0().setHotlined(hotlined);
	}

	@Override
	public Date getLastChangesDate() {
		return delegate.getLastChangesDate();
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String getHomeProvince() {
		return delegate.getHomeProvince();
	}

	@Override
	public void setHomeProvince(String homeProvince) {
		delegate.setHomeProvince(homeProvince);
	}

	@Override
	public String getAccountCategory() {
		return delegate.getAccountCategory();
	}

	@Override
	public void setAccountCategory(String accountCategory) {
		delegate.setAccountCategory(accountCategory);
	}

	@Override
	public int getNextBillCycle() {
		return delegate.getNextBillCycle();
	}

	@Override
	public int getNextBillCycleCloseDay() {
		return delegate.getNextBillCycleCloseDay();
	}

	@Override
	public Date getVerifiedDate() {
		return delegate.getVerifiedDate();
	}

	@Override
	public void setVerifiedDate(Date date) {
		delegate.setVerifiedDate(date);
	}

	@Override
	public String getOtherPhoneType() {
		return delegate.getOtherPhoneType();
	}

	@Override
	public void setOtherPhoneType(String otherPhoneType) {
		delegate.setOtherPhoneType(otherPhoneType);
	}

	@Override
	public String getOtherPhone() {
		return delegate.getOtherPhone();
	}

	@Override
	public void setOtherPhone(String otherPhone) {
		delegate.setOtherPhone(otherPhone);
	}

	@Override
	public String getOtherPhoneExtension() {
		return delegate.getOtherPhoneExtension();
	}

	@Override
	public void setOtherPhoneExtension(String otherPhoneExtension) {
		delegate.setOtherPhoneExtension(otherPhoneExtension);
	}

	@Override
	public String getContactPhone() {
		return delegate.getContactPhone();
	}

	@Override
	public void setContactPhone(String contactPhone) {
		delegate.setContactPhone(contactPhone);
	}

	@Override
	public String getContactPhoneExtension() {
		return delegate.getContactPhoneExtension();
	}

	@Override
	public void setContactPhoneExtension(String contactPhoneExtension) {
		delegate.setContactPhoneExtension(contactPhoneExtension);
	}

	@Override
	public ConsumerName getContactName() {
		return delegate.getContactName();
	}

	@Override
	public String getHomePhone() {
		return delegate.getHomePhone();
	}

	@Override
	public void setHomePhone(String homePhone) {
		delegate.setHomePhone(homePhone);
	}

	@Override
	public String getBusinessPhone() {
		return delegate.getBusinessPhone();
	}

	@Override
	public void setBusinessPhone(String businessPhone) {
		delegate.setBusinessPhone(businessPhone);
	}

	@Override
	public String getBusinessPhoneExtension() {
		return delegate.getBusinessPhoneExtension();
	}

	@Override
	public void setBusinessPhoneExtension(String businessPhoneExtension) {
		delegate.setBusinessPhoneExtension(businessPhoneExtension);
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("TMAccount:[\n");
		s.append("    FOLLOWUP_CORPORATE_DESTINATION=[").append(FOLLOWUP_CORPORATE_DESTINATION).append("]\n");
		s.append("    FOLLOWUP_CORPORATE_NEW_ACCOUNT=[").append(FOLLOWUP_CORPORATE_NEW_ACCOUNT).append("]\n");
		s.append("    FOLLOWUP_CORPORATE_ADDON=[").append(FOLLOWUP_CORPORATE_ADDON).append("]\n");
		s.append("    MEMO_SUBSCRIBER_ADDON=[").append(MEMO_SUBSCRIBER_ADDON).append("]\n");
		s.append("    MEMO_DEPOSIT_PAYMENT=[").append(MEMO_DEPOSIT_PAYMENT).append("]\n");
		s.append("    REFER_TO_CREDIT_EN=[").append(REFER_TO_CREDIT_EN).append("]\n");
		s.append("    REFER_TO_CREDIT_FR=[").append(REFER_TO_CREDIT_FR).append("]\n");
		s.append("    ONE_DAY=[").append(ONE_DAY).append("]\n");
		s.append("    address=[").append(address).append("]\n");
		s.append("    oldAddress=[").append(oldAddress).append("]\n");
		s.append("    delegate=[").append(delegate).append("]\n");
		s.append("    createdCorporateNewAccountFollowUp=[").append(createdCorporateNewAccountFollowUp).append("]\n");
		s.append("]");

		return s.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	protected void validateCreditCards(String BUSINESS_ROLE_ALL) throws TelusAPIException, InvalidCreditCardException {
		Logger.debug("not implemented in here[" + this.getClass() + "]");
		// implemented in subclasses.
	}

	@Override
	public Address getAddress() {
		return address;
	}

	public TMAddress getAddress0() {
		return address;
	}

	public TMAddress getOldAddress() {
		return oldAddress;
	}

	//	public void setOldAddress(TMAddress oldAddress) {
	//	this.oldAddress = new TMAddress(provider, (AddressInfo)oldAddress.getDelegate().clone());
	//	}

	/**
	 * Records the state of this instance; allows <CODE>save()</CODE> to determine and log the changes.
	 */
	public void setupOldData() {
		oldAddress = new TMAddress(provider, (AddressInfo) address.getDelegate().clone());
	}

	@Override
	public void commit() {
		//		oldAddress = null;
		setupOldData();
	}

	@Override
	public Account getAccount() throws TelusAPIException {
		return getAccount0();
	}

	@Override
	public TMAccount getAccount0() throws TelusAPIException {
		return this;
	}

	public AccountInfo getDelegate0() throws TelusAPIException {
		return delegate;
	}

	@Override
	public AccountSummary[] getDuplicateAccounts(String duplicateSearchLevel) throws TelusAPIException {
		// As of the CDA 2017 project, there is now only the default search level - MIKE_TO_PCS_DUPLICATE_BAN_SEARCH_LEVEL is no longer required.
		if (!duplicateSearchLevel.equalsIgnoreCase(DEFAULT_DUPLICATE_BAN_SEARCH_LEVEL)) {
			throw new TelusAPIException("An unknown duplicate ban search level was specifed when attemping to retrieve duplicate accounts.");
		}
		return getDuplicateAccounts();
	}

	@Override
	public AccountSummary[] getDuplicateAccounts() throws TelusAPIException {

		try {
			// Call the other getDuplicateAccounts() method to retrieve the array of duplicate BANs
			int[] duplicateBanArray = getDuplicateAccountBANs();

			// Most accounts won't have duplicates - return an empty array in this case
			if (duplicateBanArray.length == 0) {
				return new AccountSummary[0];
			}

			return provider.getAccountManager().findAccountsByBANs(duplicateBanArray);

		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	@Override
	public int[] getDuplicateAccountBANs(String duplicateSearchLevel) throws TelusAPIException {
		// As of the CDA 2017 project, there is now only the default search level - MIKE_TO_PCS_DUPLICATE_BAN_SEARCH_LEVEL is no longer required.
		if (!duplicateSearchLevel.equalsIgnoreCase(DEFAULT_DUPLICATE_BAN_SEARCH_LEVEL)) {
			throw new TelusAPIException("An unknown duplicate ban search level was specifed when attemping to retrieve duplicate accounts.");
		}
		return getDuplicateAccountBANs();
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public int[] getDuplicateAccountBANs() throws TelusAPIException {

		try {
			// Call HCD to check for duplicate accounts and manual override memos
			List list = provider.getAccountLifecycleFacade().getDuplicateAccountList(getDelegate0());

			// Most accounts won't have duplicates - return an empty array in this case
			if (list == null || list.isEmpty()) {
				return new int[0];
			}

			int[] banIds = new int[list.size()];
			Iterator iterator = list.iterator();
			for (int i = 0; iterator.hasNext(); i++) {
				MatchedAccountInfo info = (MatchedAccountInfo) iterator.next();
				banIds[i] = info.getAccount().getBanId();
			}

			return banIds;

		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	@Override
	public void save() throws TelusAPIException, InvalidCreditCardException {
		save(false);
	}

	@Override
	public void refresh() throws TelusAPIException {

		assertAccountExists();
		TMAccount a = (TMAccount) provider.getAccountManager().findAccountByBAN(getBanId());
		// Resetting the CollectionState to null so that we can get the latest data from the DB on refresh (it's cached)
		delegate.getFinancialHistory0().setCollectionState(null);
		delegate.copyFrom(a.delegate);
		cancellationPenalty = null;
		phoneNumbers.clear();
		creditCheckResult = null;
		productSubscriberListCached = false;
		communicationSuiteEligible = null;

		// State preservation should be done at the end of refresh job.
		setupOldData();
	}

	public String getProductType() {
		return (isIDEN()) ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS;
	}

	@Override
	public void refreshCreditCheckResult() throws TelusAPIException {

		assertAccountExists();
		try {
			CreditCheckResultInfo info = null;
			info = provider.getAccountLifecycleManager().retrieveLastCreditCheckResultByBan(getBanId(), getProductType(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
			delegate.getCreditCheckResult0().copyFrom(info);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException {
		//return payDeposit(subscriberCount, amount, creditCard, "I", "DEPOSIT");
		return payDeposit(subscriberCount, amount, creditCard, "DEPOSIT", "I", businessRole, auditHeader);
	}

	@Override
	public String payDeposit(int subscriberCount, double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader)
			throws TelusAPIException, PaymentFailedException {

		assertAccountExists();
		String result = null;
		try {
			// AuditHeader should not be null;
			if (auditHeader == null) {
				throw new TelusAPIException("The required AuditHeader is missing");
			}

			sourceID = sourceID.trim();
			sourceType = sourceType.trim();
			//-------------------------------------------
			// Validate paymentSourceType.
			//-------------------------------------------
			PaymentSourceType paymentSourceType = provider.getReferenceDataManager().getPaymentSourceType(sourceID, sourceType);
			if (paymentSourceType == null) {
				throw new UnknownObjectException("Unknown PaymentSourceType: [sourceID=" + sourceID + ", sourceType=" + sourceType + "]");
			}

			//-------------------------------------------
			// Commit Transaction.
			//-------------------------------------------
			TMCreditCard c = (TMCreditCard) creditCard;

			if (c.hasToken() == false) {
				throw new TelusAPIException("CreditCard token is missing");
			}
			c.setAuditHeader(provider.appendToAuditHeader(auditHeader));

			//CreditCardTransactionInfo creditCardTransactionInfo = c.getCreditCardTransactionInfo();
			//String payDeposit( double pAmount, CreditCardTransactionInfo pCreditCardTransactionInfo, int pBan, String pPaymentSourceType, String pPaymentSourceID) throws TelusException, RemoteException;
			//String returnCode = provider.getAccountManagerEJB().payBill(amount, c.getCreditCardTransactionInfo(), getBanId(), sourceType, sourceID);
			c.getCreditCardTransactionInfo().getCreditCardHolderInfo().setBusinessRole(businessRole);
			c.getCreditCardTransactionInfo().setBrandId(delegate.getBrandId());
			result = accountLifecycleFacade.payDeposit(amount, c.getCreditCardTransactionInfo(), getBanId(), sourceType, sourceID, SessionUtil.getSessionId(accountLifecycleFacade));
			provider.getInteractionManager0().makePayment(this, 'C', amount);

			//----------------------------------------------------------------
			// Create deposit memo.
			//----------------------------------------------------------------
			createMemo(MEMO_DEPOSIT_PAYMENT, subscriberCount + " subscriber deposits paid ($" + amount + ") for an active total of " + (delegate.getActiveSubscribersCount() + subscriberCount));

		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderCreditCardExceptionTranslator(creditCard);
			provider.getExceptionHandler().handleException(t, telusExceptionTranslator);
		}

		return result;
	}

	//	public double calulateTheMinimumPayment() throws TelusAPIException {
	//		try {
	//			CLMSummary cLMSummary = ((PostpaidConsumerAccount) this).getCLMSummary();
	//			double reqMinPayment = (cLMSummary.getUnpaidAirTime() + cLMSummary.getUnpaidData() + unpaidUnBilledAmount()) - (getCreditCheckResult().getLimit() / 2);
	//			if (reqMinPayment < 0)
	//				reqMinPayment = 0;
	//			reqMinPayment = reqMinPayment + cLMSummary.getUnpaidBillCharges();
	//			if (reqMinPayment < 0)
	//				reqMinPayment = 0;
	//			return reqMinPayment;
	//		} catch (Exception e) {
	//			return 0;
	//		}
	//	}

	public double unpaidUnBilledAmount() {
		double totalAmount = 0;
		try {
			FinancialHistory financialHistory = getFinancialHistory();

			Date billedDueDate = financialHistory.getDebtSummary().getBillDueDate();
			if (billedDueDate == null) {
				Subscriber[] sub = getSubscribers(1);
				billedDueDate = sub[0].getStartServiceDate();
			}
			if (billedDueDate.before(new Date())) {
				InvoiceHistory[] invoiceHistories = getInvoiceHistory(billedDueDate, new Date());
				for (int i = 0; i < invoiceHistories.length; i++) {
					totalAmount += invoiceHistories[i].getAmountDue() - invoiceHistories[i].getPastDue();
				}
			}

		} catch (Throwable e) {
			Logger.debug(e);
		}
		return totalAmount;
	}

	private boolean isSuspendedDueToCLM() {

		if (getStatus() == AccountSummary.STATUS_SUSPENDED && (getCreditCheckResult().getCreditClass().equals("X") || getCreditCheckResult().getCreditClass().equals("L"))) {
			if (CLM_suspendReasonCode.equals(getStatusActivityReasonCode().trim())) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String payBill(double amount, CreditCard creditCard, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException {
		return payBill(amount, creditCard, "PAYMENT", "I", businessRole, auditHeader);
	}

	@Override
	public String payBill(double amount, CreditCard creditCard, String sourceID, String sourceType, String businessRole, AuditHeader auditHeader) throws TelusAPIException, PaymentFailedException {
		assertAccountExists();
		String returnCode = null;
		try {
			// payBill should not be called for Prepaid Account
			if (delegate.getAccountType() == ACCOUNT_TYPE_CONSUMER && delegate.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_PREPAID) {
				throw new TelusAPIException("Can not use payBill on Prepaid Account");
			}
			//AuditHeader should not be null;
			if (auditHeader == null)
				throw new TelusAPIException("The required AuditHeader is missing");

			sourceID = sourceID.trim();
			sourceType = sourceType.trim();
			//-------------------------------------------
			// Validate paymentSourceType.
			//-------------------------------------------
			PaymentSourceType paymentSourceType = provider.getReferenceDataManager().getPaymentSourceType(sourceID, sourceType);
			if (paymentSourceType == null) {
				throw new UnknownObjectException("Unknown PaymentSourceType: [sourceID=" + sourceID + ", sourceType=" + sourceType + "]");
			}

			//-------------------------------------------
			// Commit Transaction.
			//-------------------------------------------
			double pastDueBalance = getFinancialHistory().getDebtSummary().getPastDue();
			double payment = amount;

			// TODO: obtain values for: transactionReason, pPaymentSourceType, pPaymentSourceID   --Ludmila
			TMCreditCard c = (TMCreditCard) creditCard;

			if (c.hasToken() == false)
				throw new TelusAPIException("CreditCard token is missing");
			c.setAuditHeader(provider.appendToAuditHeader(auditHeader));

			//payBill(double pAmount, CreditCardTransactionInfo pCreditCardTransactionInfo, int pBan, String pPaymentSourceType, String pPaymentSourceID) throws TelusException, RemoteException;
			c.getCreditCardTransactionInfo().getCreditCardHolderInfo().setBusinessRole(businessRole);
			c.getCreditCardTransactionInfo().setBrandId(delegate.getBrandId());

			returnCode = accountLifecycleFacade.payBill(amount, c.getCreditCardTransactionInfo(), getBanId(), sourceType, sourceID, delegate, getTransientNotificationSuppressionInd(), null,
					SessionUtil.getSessionId(accountLifecycleFacade));

			provider.getInteractionManager0().makePayment(this, 'C', amount);
			//			provider.getInteractionManager0().makePayment(this, sourceID.charAt(0), amount);
			Boolean isPostpaidBusinessConnect = Boolean.valueOf(isPostpaidBusinessConnect());
			if (isSuspendedDueToCLM() && ((PostpaidConsumerAccount) this).getCLMSummary().getRequiredMinimumPayment() <= payment) {
				// MU-CLP new requirement from Jun @ 2014-10-06, set COLLECTION_INDICATOR=true for SuspendedDueToCLM. Refer defect 30885. 
				provider.getAccountManager0().restoreSuspendedAccount(getBanId(), new Date(), CLM_restoreAccountReasonCode,
						"Minimum payment was payed, Account is restored by CLM Rule " + provider.getApplication(), true, Integer.valueOf(delegate.getBrandId()), isPostpaidBusinessConnect);

			} else if ((!isSuspendedDueToCLM()) && amount >= pastDueBalance) {
				boolean oldHotlined = isHotlined();
				char oldStatus = getStatus();

				boolean suspendedDueToNonPayment = isSuspendedDueToNonPayment();
				if (suspendedDueToNonPayment && !getFinancialHistory().isDelinquent()) {
					provider.getAccountManager0().restoreSuspendedAccount(getBanId(), new Date(), "PY", "Balance Paid using " + provider.getApplication(), true, Integer.valueOf(delegate.getBrandId()),
							isPostpaidBusinessConnect);
				}

				if (isHotlined()) {
					setHotlined(false);
					save();
				} else if (suspendedDueToNonPayment) {
					// Since the above save would refresh the account,
					//  we only need to refresh here.
					refresh();
				}

				boolean statusChanged = oldHotlined != isHotlined() || oldStatus != getStatus();
				if (statusChanged) {
					provider.getInteractionManager0().accountStatusChange(this, oldHotlined, oldStatus);
				}
			}
		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderCreditCardExceptionTranslator(creditCard);
			provider.getExceptionHandler().handleException(t, telusExceptionTranslator);
		}
		return returnCode;
	}

	private InvoiceHistory[] decorate(InvoiceHistoryInfo[] infos) {
		TMInvoiceHistory[] tms = new TMInvoiceHistory[infos.length];
		TMInvoiceHistory tm = null;
		for (int i = 0; i < tms.length; i++) {
			tm = new TMInvoiceHistory(provider, infos[i]);
			tms[i] = tm;
		}

		return tms;
	}

	// Decorate to get Deposit Bar Code
	private TMFinancialHistory decorate(FinancialHistoryInfo info) {
		return new TMFinancialHistory(provider, info);
	}

	private CreditCheckResult decorate(CreditCheckResultInfo info) throws TelusAPIException, UnknownBANException {

		CreditCheckResultInfo amdocsResult;
		if (info.getLastCreditCheckName() == null && info.getLastCreditCheckAddress() == null) {
			try {
				amdocsResult = accountLifecycleManager.retrieveAmdocsCreditCheckResultByBan(getBanId(), SessionUtil.getSessionId(accountLifecycleManager));
				if (amdocsResult != null) {
					info.copyAmdocsInfo(amdocsResult);
				}
			} catch (Throwable t) {
				Logger.debug("Error retrieving amdocs credit check result info for ban = " + getBanId() + "," + t);
			}
		}
		TMCreditCheckResult tmCreditCheckResult = new TMCreditCheckResult(this.provider, info);
		tmCreditCheckResult.getDepositBarCode();

		return tmCreditCheckResult;
	}

	private Charge[] decorate(ChargeInfo[] charges) throws TelusAPIException {
		TMCharge[] tmCharges = new TMCharge[charges.length];
		for (int i = 0; i < charges.length; i++) {
			TMCharge tmCharge = new TMCharge(provider, charges[i]);
			tmCharges[i] = tmCharge;
		}
		return tmCharges;
	}

	private Credit[] decorate(CreditInfo[] credits) throws TelusAPIException {
		TMCredit[] tmCredits = new TMCredit[credits.length];
		for (int i = 0; i < credits.length; i++) {
			TMCredit tmCredit = new TMCredit(provider, credits[i], null);
			tmCredits[i] = tmCredit;
		}
		return tmCredits;
	}

	private PaymentHistory[] decorate(PaymentHistoryInfo[] paymentHistories) throws TelusAPIException {
		TMPaymentHistory[] tmPaymentHistories = new TMPaymentHistory[paymentHistories.length];
		for (int i = 0; i < tmPaymentHistories.length; i++) {
			TMPaymentHistory tmPaymentHistory = new TMPaymentHistory(provider, paymentHistories[i], getBanId(), getBrandId());
			tmPaymentHistories[i] = tmPaymentHistory;
		}
		return tmPaymentHistories;
	}

	protected FeeWaiver[] decorate(FeeWaiverInfo[] infos) {
		if (infos == null)
			return null;

		TMFeeWaiver[] fws = new TMFeeWaiver[infos.length];
		TMFeeWaiver fw = null;
		for (int i = 0; i < infos.length; i++) {
			fw = new TMFeeWaiver(provider, infos[i]);
			fws[i] = fw;
		}

		return fws;
	}

	// Decorate FutureStatusChangeRequests to get save and delete methods
	private FutureStatusChangeRequest[] decorate(FutureStatusChangeRequest[] requests) throws TelusAPIException {

		TMFutureStatusChangeRequest[] tmFutureStatusChangeRequests = new TMFutureStatusChangeRequest[requests.length];
		for (int i = 0; i < requests.length; i++) {
			tmFutureStatusChangeRequests[i] = new TMFutureStatusChangeRequest(provider, (FutureStatusChangeRequestInfo) requests[i]);
		}
		return tmFutureStatusChangeRequests;
	}

	private int getLastMemoTotal(String memoType) {
		try {
			Memo memo = getLastMemo(memoType);

			//provider.debug("getLastMemoTotal(): " + memo);
			boolean memoExpired = memo.getDate().getTime() + ONE_DAY < System.currentTimeMillis();
			//provider.debug("getLastMemoTotal(): memoExpired=[" + memoExpired + "]");

			if (memoExpired) {
				return -1;
			}

			String s = memo.getText();
			return Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1));
		} catch (Throwable e) {
			return -1;
		}
	}

	@Override
	public CreditCheckResult checkNewSubscriberEligibility(int subscriberCount, double thresholdAmount) throws TelusAPIException {
		CreditCheckResult creditCheckResult = null;
		try {
			CreditCheckResultInfo creditCheckResultInfo = accountLifecycleFacade.checkNewSubscriberEligibility(delegate, subscriberCount, thresholdAmount,
					SessionUtil.getSessionId(accountLifecycleFacade));
			delegate.getCreditCheckResult0().copyFrom(creditCheckResultInfo);

			// defect PROD00144019 fix, 
			// Every time after a subscriber get activated, we will refresh the account object, which result to the ActivationOption 
			// inside the CreditResultInfo get overridden.
			// To avoid this, return a clone object
			creditCheckResult = decorate((CreditCheckResultInfo) delegate.getCreditCheckResult0().clone());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return creditCheckResult;
	}

	@Override
	public RewardRedemptionResult checkRewardRedemptionEligibility() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not supported. Please call HPA services for reward redemption eligibility.");
	}

	@Override
	public InvoiceHistory[] getInvoiceHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		InvoiceHistory[] infos = null;
		try {
			List list = accountInformationHelper.retrieveInvoiceHistory(getBanId(), from, to);
			infos = decorate((InvoiceHistoryInfo[]) list.toArray(new InvoiceHistoryInfo[list.size()]));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return infos;
	}

	@Override
	public PaymentHistory[] getPaymentHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		PaymentHistory[] paymentHistories = null;
		try {
			List list = accountInformationHelper.retrievePaymentHistory(getBanId(), from, to);
			paymentHistories = decorate((PaymentHistoryInfo[]) list.toArray(new PaymentHistoryInfo[list.size()]));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return paymentHistories;
	}

	@Override
	public PaymentHistory getLastPaymentActivity() throws TelusAPIException, HistorySearchException {
		PaymentHistory paymentHistory = null;
		try {
			paymentHistory = accountInformationHelper.retrieveLastPaymentActivity(getBanId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return paymentHistory;
	}

	@Override
	public PaymentMethodChangeHistory[] getPaymentMethodChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		PaymentMethodChangeHistory[] paymentMethodChangeHistories = null;
		try {
			List list = accountInformationHelper.retrievePaymentMethodChangeHistory(getBanId(), from, to);
			paymentMethodChangeHistories = (PaymentMethodChangeHistoryInfo[]) list.toArray(new PaymentMethodChangeHistoryInfo[list.size()]);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return paymentMethodChangeHistories;
	}

	@Override
	public StatusChangeHistory[] getStatusChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		StatusChangeHistory[] statusChangeHistories = null;
		try {
			List list = accountInformationHelper.retrieveStatusChangeHistory(getBanId(), from, to);
			statusChangeHistories = (StatusChangeHistoryInfo[]) list.toArray(new StatusChangeHistoryInfo[list.size()]);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return statusChangeHistories;
	}

	/**
	 * Retrieves a phone number by SubscriberId.
	 *
	 * @param SubscriberId
	 * @return Phone Number
	 * @throws TelusAPIException
	 */

	@Override
	public String getPhoneNumberBySubscriberID(String pSubscriberId) throws TelusAPIException {
		String phoneNumber = "";
		if (!isIDEN()) {
			return pSubscriberId;
		}
		try {
			if (phoneNumbers.isEmpty()) {
				phoneNumbers = (HashMap) accountInformationHelper.retrievePhoneNumbersForBAN(getBanId());
			}
			phoneNumber = (String) phoneNumbers.get(pSubscriberId);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return phoneNumber;
	}

	@Override
	public AddressHistory[] getAddressChangeHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {
		AddressHistory[] addressHistory = null;
		try {
			AddressHistory[] history = accountInformationHelper.retrieveAddressHistory(getBanId(), from, to);
			List list = new ArrayList(history.length);

			for (int i = 0; i < history.length; i++) {
				//history[i] = new TMAddressHistory(provider, (AddressHistoryInfo) history[i]);
				list.add(new TMAddressHistory(provider, (AddressHistoryInfo) history[i]));
			}
			addressHistory = (AddressHistory[]) list.toArray(new AddressHistory[list.size()]);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		//return history;
		return addressHistory;
	}

	@Override
	public ProrationMinutes[] getProrationMinutes(int months, int totalMinutes) throws TelusAPIException {

		// Validate input arguments
		if (months <= 0 || totalMinutes <= 0) {
			return new ProrationMinutesInfo[0];
		}

		ProrationMinutesInfo[] prorationMinutesArray = new ProrationMinutesInfo[months + 1];

		try {
			// Get next Billing Cycle Date
			int billCycleCloseDay = getBillCycleCloseDay();
			Calendar billCycleCalendar = Calendar.getInstance();
			int currentDay = billCycleCalendar.get(Calendar.DAY_OF_MONTH);

			billCycleCalendar.set(Calendar.DAY_OF_MONTH, billCycleCloseDay);
			int days2BillCycle = 0;
			if (billCycleCloseDay < currentDay) {
				days2BillCycle = 30 - currentDay + billCycleCloseDay;
				billCycleCalendar.add(Calendar.MONTH, 1);
			} else {
				days2BillCycle = billCycleCloseDay - currentDay;
			}

			// Number of minutes per month
			int minutesPerMonth = totalMinutes / months;

			// Go back one day
			billCycleCalendar.add(Calendar.DATE, -1);

			// First period
			int periodMinutes = minutesPerMonth * days2BillCycle / 30;
			Date periodExpiryDate = billCycleCalendar.getTime();

			ProrationMinutesInfo prorationMinutes = new ProrationMinutesInfo();
			prorationMinutes.setExpiryDate(periodExpiryDate);
			prorationMinutes.setMinutes(periodMinutes);
			prorationMinutesArray[0] = prorationMinutes;
			int remainingMinutes = totalMinutes - periodMinutes;

			// Full month periods
			for (int i = 1; i < months; i++) {
				billCycleCalendar.add(Calendar.MONTH, 1);
				periodMinutes = minutesPerMonth;
				periodExpiryDate = billCycleCalendar.getTime();

				prorationMinutes = new ProrationMinutesInfo();

				prorationMinutes.setExpiryDate(periodExpiryDate);
				prorationMinutes.setMinutes(periodMinutes);
				prorationMinutesArray[i] = prorationMinutes;

				remainingMinutes = remainingMinutes - periodMinutes;
			}

			// Last period
			Calendar lastPeriod = Calendar.getInstance();
			lastPeriod.add(Calendar.MONTH, months);
			periodMinutes = remainingMinutes;
			periodExpiryDate = lastPeriod.getTime();

			prorationMinutes = new ProrationMinutesInfo();

			prorationMinutes.setExpiryDate(periodExpiryDate);
			prorationMinutes.setMinutes(periodMinutes);
			prorationMinutesArray[months] = prorationMinutes;

			return prorationMinutesArray;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public VoiceUsageSummary[] getVoiceUsageSummary() throws VoiceUsageSummaryException, TelusAPIException {
		return getVoiceUsageSummary(null);
	}

	/*
	See next method for latest version
	
	public VoiceUsageSummary[] getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException {
	  try {
	    return provider.getAccountHelperEJB().retrieveVoiceUsageSummary(getBanId(), featureCode);
	  } catch (VoiceUsageSummaryException e) {
	    throw e;
	  } catch (Throwable e) {
	    throw new TelusAPIException(e);
	  }
	}
	 */

	/*
	 *  Modify the Subscriber/Account providers to reset the usage minutes
	 *  with the rounded values and return the Info class as it did previously.
	 */
	public VoiceUsageSummary[] getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException {
		VoiceUsageSummaryInfo[] summaries = null;
		try {
			summaries = accountInformationHelper.retrieveVoiceUsageSummary(getBanId(), featureCode);

			if (summaries == null) {
				throw new VoiceUsageSummaryException(VoiceUsageSummaryException.NOT_AVAILABLE);
			}

			// traverse the array of voice usage summaries
			for (int n = 0; n < summaries.length; n++) {

				// traverse the array of voice usage usase services
				VoiceUsageServiceInfo[] services = (VoiceUsageServiceInfo[]) summaries[n].getVoiceUsageServices();
				for (int i = 0; i < services.length; i++) {

					// retrieve the uage rating frequency using the PricePlanSummary reference object.
					int frequency = provider.getReferenceDataManager().getPricePlan(services[i].getServiceCode()).getUsageRatingFrequency();

					// traverse the array of voice usage usase service directions
					VoiceUsageServiceDirectionInfo[] directions = (VoiceUsageServiceDirectionInfo[]) services[i].getVoiceUsageServiceDirections();
					for (int j = 0; j < directions.length; j++) {

						// traverse the array of voice usage usase service periods
						VoiceUsageServicePeriodInfo[] periods = (VoiceUsageServicePeriodInfo[]) directions[j].getVoiceUsageServicePeriods();
						for (int k = 0; k < periods.length; k++) {
							// round usage minutes using the rating frequency
							periods[k].setTotalUsed(roundMinutes(periods[k].getTotalUsed(), frequency, USAGE_MINUTE_ROUNDING_UP));
							periods[k].setIncluded(roundMinutes(periods[k].getIncluded(), frequency, USAGE_MINUTE_ROUNDING_DOWN));
							periods[k].setIncludedUsed(roundMinutes(periods[k].getIncludedUsed(), frequency, USAGE_MINUTE_ROUNDING_UP));
							periods[k].setFree(roundMinutes(periods[k].getFree(), frequency, USAGE_MINUTE_ROUNDING_DOWN));
							periods[k].setRemaining(roundMinutes(periods[k].getRemaining(), frequency, USAGE_MINUTE_ROUNDING_DOWN));
							periods[k].setChargeable(roundMinutes(periods[k].getChargeable(), frequency, USAGE_MINUTE_ROUNDING_UP));
						}
						directions[j].setVoiceUsageServicePeriods(periods);
					}
					services[i].setVoiceUsageServiceDirections(directions);
				}
				summaries[n].setVoiceUsageServices(services);
			}

			// return updated Info class with rounded minutes in child Info

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t, new TelusExceptionTranslator() {

				@Override
				public TelusAPIException translateException(Throwable throwable) {
					if (throwable instanceof VoiceUsageSummaryException) {
						return new VoiceUsageSummaryException(((VoiceUsageSummaryException) throwable).getReason());
					}
					return null;
				}
			});
		}
		return summaries;
	}

	// minutes=9999.3333 , frequency=second, UP   ==>  9999.3333
	// minutes=9999.3333 , frequency=minute, UP   ==>  9999.0
	// minutes=9999.9888 , frequency=second, UP   ==>  10000.0
	// minutes=9999.9888 , frequency=minute, UP   ==>  10000.0
	// minutes=9999.9999 , frequency=second, UP   ==>  10000.0
	// minutes=9999.9999 , frequency=minute, UP   ==>  10000.0

	private double roundMinutes(double minutes, int usageRatingFrequency) {
		return roundMinutes(minutes, usageRatingFrequency, USAGE_MINUTE_ROUNDING_UP);
	}

	private double roundMinutes(double minutes, int usageRatingFrequency, String roundingType) {

		int mins = (int) minutes;

		// convert decimal minutes to seconds -->  [0..60]
		double secs = (minutes - mins) * 60;

		// round seconds depending of the rouding function
		double roundedSeconds = 0.0;
		if (roundingType.equals(USAGE_MINUTE_ROUNDING_UP)) {
			roundedSeconds = Math.ceil(secs);
		} else if (roundingType.equals(USAGE_MINUTE_ROUNDING_DOWN)) {
			roundedSeconds = Math.floor(secs);
		} else {
			roundedSeconds = Math.round(secs);
		}

		// increment minutes if rounded seconds is 60
		if ((int) roundedSeconds == 60) {
			mins++;
			roundedSeconds = 0.0;
		}

		// clear rounded seconds if price plan is per minute
		if (usageRatingFrequency == PricePlanSummary.USAGE_RATING_FREQUENCY_MINUTE) {
			roundedSeconds = 0.0;
		}

		// convert back to decimal minutes with maximum 4 decimal digits
		int intsecs = (int) ((roundedSeconds / 60) * 10000);
		roundedSeconds = intsecs / 10000.0;

		return mins + roundedSeconds;
	}

	public WebUsageSummary[] getWebUsageSummary() throws TelusAPIException {
		throw new AbstractMethodError("TODO");
	}

	public PricePlanSubscriberCount[] getPricePlanSubscriberCount() throws TelusAPIException {
		return getShareablePricePlanSubscriberCount();
	}

	public PricePlanSubscriberCount getPricePlanSubscriberCount(String pricePlanCode) throws TelusAPIException {
		return getShareablePricePlanSubscriberCount(pricePlanCode);
	}

	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount() throws TelusAPIException {
		PricePlanSubscriberCount[] shareableCountResult = null;
		try {
			shareableCountResult = getShareablePricePlanSubscriberCount(false);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return shareableCountResult;
	}

	public PricePlanSubscriberCount[] getShareablePricePlanSubscriberCount(boolean refresh) throws TelusAPIException {

		PricePlanSubscriberCount[] shareableCountResult = null;
		if (refresh) {
			try {
				List list = accountInformationHelper.retrieveShareablePricePlanSubscriberCount(getBanId());
				shareableCountResult = (PricePlanSubscriberCountInfo[]) list.toArray(new PricePlanSubscriberCountInfo[list.size()]);
				if (shareableCountResult != null) {
					HashMap pricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);
					if (pricePlanSubscriberCountCache != null) {
						pricePlanSubscriberCountCache.clear();
					} else {
						pricePlanSubscriberCountCache = new HashMap();
					}

					for (int i = 0; i < shareableCountResult.length; i++) {
						String key = shareableCountResult[i].getPricePlanCode();
						pricePlanSubscriberCountCache.put(key, shareableCountResult[i]);
					}

					pricePlanSubscriberCountHashMapCache.put(SHAREABLE_KEY, pricePlanSubscriberCountCache);
				}

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return shareableCountResult;

		} else {
			HashMap pricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);
			if (pricePlanSubscriberCountCache != null) {
				return (PricePlanSubscriberCount[]) pricePlanSubscriberCountCache.values().toArray(new PricePlanSubscriberCount[pricePlanSubscriberCountCache.size()]);
			} else {
				//PricePlanCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getShareablePricePlanSubscriberCount(true);
			}
		}
	}

	public PricePlanSubscriberCount getShareablePricePlanSubscriberCount(String pricePlanCode) throws TelusAPIException {

		HashMap shareablePricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);
		if (shareablePricePlanSubscriberCountCache != null) {
			return (PricePlanSubscriberCount) shareablePricePlanSubscriberCountCache.get(pricePlanCode);
		} else {
			getShareablePricePlanSubscriberCount(); //call required to initialize ShareablePricePlanSubriberCountCache
			shareablePricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(SHAREABLE_KEY);

			if (shareablePricePlanSubscriberCountCache != null)
				return (PricePlanSubscriberCount) shareablePricePlanSubscriberCountCache.get(pricePlanCode);
		}

		return null;
	}

	public PricePlanSubscriberCount getShareablePricePlanSubscriberCount(ShareablePricePlan pricePlan) throws TelusAPIException {
		return getShareablePricePlanSubscriberCount(pricePlan.getCode());
	}

	@Override
	public ServiceSubscriberCount[] getServiceSubscriberCounts(String[] serviceCodes, boolean includeExpired) throws TelusAPIException {

		ServiceSubscriberCount[] serviceSubscriberCounts = null;
		try {
			List list = accountInformationHelper.retrieveServiceSubscriberCounts(getBanId(), serviceCodes, includeExpired);
			serviceSubscriberCounts = (ServiceSubscriberCount[]) list.toArray(new ServiceSubscriberCount[list.size()]);

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return serviceSubscriberCounts;
	}

	@Override
	public void suspend(Date activityDate, String reason, String memoText) throws TelusAPIException {

		try {
			Boolean isPostpaidBusinessConnect = Boolean.valueOf(isPostpaidBusinessConnect());
			accountLifecycleFacade.suspendAccount(getBanId(), activityDate, reason, memoText, Integer.valueOf(delegate.getBrandId()), isPostpaidBusinessConnect,
					SessionUtil.getSessionId(accountLifecycleFacade));
			refresh();
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public void suspend(String reason) throws TelusAPIException {
		suspend(null, reason, null);
	}

	@Override
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText) throws TelusAPIException {
		cancel(null, activityDate, reason, depositReturnMethod, waiver, memoText);
	}

	public void cancel(String phoneNumber, Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText) throws TelusAPIException {

		try {
			accountLifecycleFacade.cancelAccount(getBanId(), activityDate, reason, String.valueOf(depositReturnMethod), waiver, memoText, phoneNumber, getTransientNotificationSuppressionInd(), null,
					SessionUtil.getSessionId(accountLifecycleFacade));
			refresh();
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public void cancel(String reason, char depositReturnMethod) throws TelusAPIException {
		cancel(reason, depositReturnMethod, null);
	}

	@Override
	public void cancel(String reason, char depositReturnMethod, String waiver) throws TelusAPIException {
		cancel(null, reason, depositReturnMethod, waiver, null);
	}

	protected TMPagerSubscriber newPagerSubscriber(String productType, String serialNumber, boolean dealerHasDeposit, String activationFeeChargeCode)
			throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, TelusAPIException {

		assertAccountExists();
		try {
			Equipment equipment = provider.getEquipmentManager().validateSerialNumber(serialNumber);

			if (!equipment.isPager()) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_PAGER);
			}

			PagerEquipment pagerEquipment = (PagerEquipment) equipment;

			if (isPostpaidBoxedConsumer() && !pagerEquipment.isBoxed()) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_BOXED_PAGER);
			}

			if (pagerEquipment.isBoxed() && !isPostpaidBoxedConsumer()) {
				throw new InvalidSerialNumberException(serialNumber, InvalidSerialNumberException.EQUIPMENT_NOT_PAGER);
			}

			PagerSubscriberInfo info = new PagerSubscriberInfo();
			info.setBanId(getBanId());
			info.setProductType(productType);
			info.setSerialNumber(serialNumber);
			info.setEquipmentType(equipment.getEquipmentType());
			info.setDealerCode(getDealerCode());
			info.setSalesRepId(getSalesRepCode());
			info.setLanguage(getLanguage());

			return new TMPagerSubscriber(provider, info, true, activationFeeChargeCode, this, dealerHasDeposit, equipment);

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	/**
	 * Note:  The methods below are duplicated in TMPostpaidConsumerAccount.  They should be moved to
	 * a common super class...
	 */
	/**
	 * Returns the list of bill cycles that this account is allowed to be attached to.
	 *
	 * <p>This method may involve a remote method call.
	 *
	 * <P>This method is not yet implemented and will result in an AbstractMethodError if called.
	 *
	 * @return BillCycle[] -- Available cycles, This is never null.
	 */
	public BillCycle[] getAvailableBillCycles() throws TelusAPIException {

		BillCycle[] allBillCycles = provider.getReferenceDataManager0().getBillCycles();

		String province = delegate.getHomeProvince() != null ? delegate.getHomeProvince() : delegate.getAddress().getProvince();
		BillCycle[] filteredBillCycles = provider.getReferenceDataManager0().removeBillCyclesByProvince(allBillCycles, province);

		ArrayList availableBillCycles = new ArrayList(filteredBillCycles.length);
		BillCycle currentBillCycle = getBillingCycle();

		for (int i = 0; i < filteredBillCycles.length; i++) {
			if (!filteredBillCycles[i].getCode().equals(currentBillCycle.getCode())) {
				availableBillCycles.add(filteredBillCycles[i]);
			}
		}

		return (BillCycle[]) availableBillCycles.toArray(new BillCycle[availableBillCycles.size()]);
	}

	/**
	 * Returns the cycle this account is currently attached to.
	 *
	 * <p>Note:  This replaces Account.getBillCycle() and Account.getBillCycleCloseDay()
	 *
	 * <P>This method is not yet implemented and will result in an AbstractMethodError if called.
	 *
	 * @return BillCycle
	 */
	public BillCycle getBillingCycle() throws TelusAPIException {
		return provider.getReferenceDataManager0().getBillCycle(String.valueOf(delegate.getBillCycle()));
	}

	/**
	 * Attaches this account to the given billing cycle.  This takes effect the next billing cycle
	 * (not the current cycle).
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>This method is not yet implemented and will result in an AbstractMethodError if called.
	 *
	 * @param cycle -- The new cycle.
	 * @see #getAvailableBillCycles
	 */
	public void changeBillCycle(BillCycle cycle) throws TelusAPIException, InvalidBillCycleChangeException {
		changeBillCycle(cycle, true);
	}

	/**
	 * Overloads the changeBillCycle(BillCycle cycle) method with additional parameter testFirstCycleRun
	 * @param cycle BillCycle
	 * @param testFirstCycleRun boolean
	 * @throws TelusAPIException
	 * @throws InvalidBillCycleChangeException
	 */
	public void changeBillCycle(BillCycle cycle, boolean testFirstCycleRun) throws TelusAPIException, InvalidBillCycleChangeException {
		try {
			if (isBlockDirectUpdate()) {
				if (blockRuleStatus)
					throw new TelusAPIException("Direct update billcycle for ban[" + getBanId() + "] AccountType[" + getAccountType() + "] AccountSubType[" + getAccountSubType() + "] BrandId["
							+ getBrandId() + "] is blocked. This action should go to enterprise service");
			}

			if (testFirstCycleRun) {
				testCycleChange();
			}

			short cycleCode = Short.parseShort(cycle.getCode());
			accountLifecycleManager.updateBillCycle(getBanId(), cycleCode, SessionUtil.getSessionId(accountLifecycleManager));
			delegate.setBillCycle(cycleCode);

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}

	}

	/**
	 * Determines if this account can change billing cycles.  The success or failure of this test
	 * is dependent on the account's billing state (specifically in relation to its current billing
	 * cycle), not on any specific cycle it may be changed to.
	 *
	 * <P>If this test passes, the account can be changed to any cycle returned by <code>getAvailableCycles</code>
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * <P>This method is not yet implemented and will result in an AbstractMethodError if called.
	 *
	 * @throws InvalidBillCycleChangeException
	 * @see #getAvailableBillCycles
	 */
	public void testCycleChange() throws InvalidBillCycleChangeException, TelusAPIException {

		Date now = provider.getReferenceDataManager0().getSystemDate();
		Date startServiceDate = getStartServiceDate();
		BillCycle currentBillCycle = getBillingCycle();

		if (startServiceDate == null || !now.after(startServiceDate)) {
			if (getAccount().getStatus() != AccountSummary.STATUS_TENTATIVE) {
				throw new InvalidBillCycleChangeException("service has not yet started");
			}
		}

		//----------------------------------------------
		// This happens due to bad data in DEV.
		//----------------------------------------------
		if (currentBillCycle == null) {
			return;
		}

		//		  Date lastCloseDate = new Date(now.getTime());
		//		  lastCloseDate.setDate(currentBillCycle.getCloseDay());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		calendar.set(Calendar.DATE, currentBillCycle.getCloseDay());

		if (now.getDate() < currentBillCycle.getCloseDay()) { // last month
			calendar.add(Calendar.MONTH, -1);
		}

		calendar.set(Calendar.HOUR, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.clear(Calendar.MILLISECOND);

		Date previousCloseDate = calendar.getTime();

		if (startServiceDate == null || !previousCloseDate.after(startServiceDate)) {
			if (getAccount().getStatus() != AccountSummary.STATUS_TENTATIVE) {
				throw new InvalidBillCycleChangeException("a bill cycle has not run yet");
			}
		}
	}

	@Override
	public DepositHistory[] getDepositHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {

		DepositHistory[] depositHistories = null;
		try {
			depositHistories = accountInformationHelper.retrieveDepositHistory(getBanId(), from, to);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return depositHistories;
	}

	@Override
	public RefundHistory[] getRefundHistory(Date from, Date to) throws TelusAPIException, HistorySearchException {

		RefundHistory[] refundHistories = null;
		try {
			List list = accountInformationHelper.retrieveRefundHistory(getBanId(), from, to);
			refundHistories = (RefundHistoryInfo[]) list.toArray(new RefundHistoryInfo[list.size()]);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return refundHistories;
	}

	@Override
	public SearchResults getPendingChargeHistory(Date from, Date to, char level, String subscriberId, int maximum) throws TelusAPIException, HistorySearchException {

		SearchResultsInfo results = null;
		try {
			results = accountInformationHelper.retrievePendingChargeHistory(getBanId(), from, to, level, subscriberId, maximum);
			ChargeInfo[] info = (ChargeInfo[]) results.getItems();
			results.setItems(decorate(info));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return results;
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, char level, String subscriberId, int maximum) throws TelusAPIException {

		SearchResultsInfo results = null;
		try {
			results = accountInformationHelper.retrieveCredits(getBanId(), from, to, billState, level, subscriberId, maximum);
			CreditInfo[] info = (CreditInfo[]) results.getItems();
			results.setItems(decorate(info));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return results;
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, char level, String subscriberId, String knowbilityOperatorId, int maximum) throws TelusAPIException {

		SearchResultsInfo results = null;
		try {
			results = accountInformationHelper.retrieveCredits(getBanId(), from, to, billState, level, subscriberId, knowbilityOperatorId, maximum);
			CreditInfo[] info = (CreditInfo[]) results.getItems();
			results.setItems(decorate(info));

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return results;
	}



	@Override
	public Charge[] getBilledCharges(int billSeqNo) throws TelusAPIException {
		return getBilledCharges(billSeqNo, null, null, null);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, Date from, Date to) throws TelusAPIException {
		return getBilledCharges(billSeqNo, null, from, to);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber) throws TelusAPIException {
		return getBilledCharges(billSeqNo, phoneNumber, null, null);
	}

	@Override
	public Charge[] getBilledCharges(int billSeqNo, String phoneNumber, Date from, Date to) throws TelusAPIException {
		Charge[] charges = null;
		try {
			List list = accountInformationHelper.retrieveBilledCharges(getBanId(), billSeqNo, phoneNumber, from, to);
			charges = decorate((ChargeInfo[]) list.toArray(new ChargeInfo[list.size()]));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return charges;
	}

	/**
	 * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
	 */
	@Override
	public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		try {
			if (cancellationPenalty == null) {
				cancellationPenalty = accountLifecycleManager.retrieveCancellationPenalty(getBanId(), SessionUtil.getSessionId(accountLifecycleManager));
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return cancellationPenalty;
	}

	@Override
	public ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException {
		if (delegate.getClientConsentIndicatorCodes() == null) {
			// TODO delegate.setClientConsentIndicatorCodes(/* get codes remotely */);
			throw new AbstractMethodError("TODO");
		}

		String[] codes = delegate.getClientConsentIndicatorCodes();

		ClientConsentIndicator[] clientConsentIndicators = new ClientConsentIndicator[codes.length];
		for (int i = 0; i < codes.length; i++) {
			clientConsentIndicators[i] = provider.getReferenceDataManager0().getClientConsentIndicator(codes[i]);
		}

		return clientConsentIndicators;
	}

	@Override
	public void setClientConsentIndicators(ClientConsentIndicator[] clientConsentIndicators) throws TelusAPIException {
		delegate.setClientConsentIndicators(clientConsentIndicators);
	}

	private double getOutstandingDeposit() throws TelusAPIException {
		DepositHistory[] depositHistory = getDepositHistory(new Date(0), new Date());

		double outstandingDeposit = 0.0;
		for (int i = 0; i < depositHistory.length; i++) {
			if (Info.isEmpty(depositHistory[i].getCancellationReasonCode())) {
				outstandingDeposit += depositHistory[i].getChargesAmount() - depositHistory[i].getDepositPaidAmount();
			}
		}

		return outstandingDeposit;
	}

	public static Date createDate(int month, int dayOfMonth, int year) {
		Calendar date = Calendar.getInstance();

		date.set(Calendar.MONTH, month);
		date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		date.set(Calendar.YEAR, year);

		date.set(Calendar.HOUR, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		return date.getTime();
	}

	@Override
	public InternationalServiceEligibilityCheckResult checkInternationalServiceEligibility() throws TelusAPIException {
		InternationalServiceEligibilityCheckResult internationalServiceEligibilityCheckResult = null;
		/** [Naresh Annabathula]
		 * below if block code moved to Account helper ejb ,just keeping here for rollback purpose.
		 * so this if block code and corresponding private methods should be removed later October-2013 Release.
		 */
		if (AppConfiguration.ischeckInternationalServiceEligibilityProviderRollback()) {
			InternationalServiceEligibilityCheckCriteria criteria = getInternationalServiceEligibilityCheckCriteria();
			EligibilityCheckStrategy strategy = AppConfiguration.getInternationalServiceEligibilityEvaluationStrategy();
			internationalServiceEligibilityCheckResult = (InternationalServiceEligibilityCheckResult) strategy.evaluate(criteria);
		} else {
			try {
				internationalServiceEligibilityCheckResult = accountInformationHelper.checkInternationalServiceEligibility(getBanId());
			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}
		return internationalServiceEligibilityCheckResult;
	}

	private InternationalServiceEligibilityCheckCriteria getInternationalServiceEligibilityCheckCriteria() {
		InternationalServiceEligibilityCheckCriteria criteria = new InternationalServiceEligibilityCheckCriteria();

		// account combined type
		StringBuffer buffer = new StringBuffer();
		buffer.append(getAccountType()).append(getAccountSubType());
		criteria.setAccountCombinedType(buffer.toString());

		// brandId
		criteria.setBrandId(getBrandId());

		// credit class
		criteria.setCreditClass(getCreditCheckResult().getCreditClass());

		// tenure
		criteria.setTenure(getTenureInMonths());

		// collection activity
		criteria.setCollectionActivityPresent(isCollectionActivityPresent(INTERNATIONAL_SERVICE_COLLECTION_ACTIVITY_TERM_IN_MONTHS));

		// new account
		criteria.setNewAccount(getStartServiceDate() == null);

		return criteria;
	}

	private int getTenureInMonths() {
		int tenure = 0;

		if (getStartServiceDate() != null) {

			Calendar today = Calendar.getInstance();
			Calendar startServiceDate = Calendar.getInstance();

			startServiceDate.setTime(getStartServiceDate()); // requirements said subscriber.getStartServiceDate() not account.getStartServiceDate()

			tenure = (today.get(Calendar.YEAR) * 12 + today.get(Calendar.MONTH)) - (startServiceDate.get(Calendar.YEAR) * 12 + startServiceDate.get(Calendar.MONTH));
		}

		return tenure;
	}

	private boolean isCollectionActivityPresent(int termInMonths) {

		if (getFinancialHistory().isDelinquent() || getFinancialHistory().isHotlined()) {
			return true;
		}

		MonthlyFinancialActivity[] financialActivity = getFinancialHistory().getMonthlyFinancialActivity();

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

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public double getInternationalDialingDepositAmount() throws TelusAPIException {
		return checkInternationalServiceEligibility().getDepositAmount();
	}

	@Override
	public Account createDuplicateAccount() throws TelusAPIException {
		// return object
		Account duplicateAccount = null;

		PaymentMethodInfo paymentMethodInfo = null;
		PersonalCreditInfo personalCerditInfo = null;

		try {
			// clone the account info.
			AccountInfo accountInfo = (AccountInfo) getDelegate0().clone();
			accountInfo.copyFrom(getDelegate0());

			// set BAN number to 0.
			accountInfo.setBanId(0);

			// get payment method and personal credit info.
			if (accountInfo.getAccountType() == ACCOUNT_TYPE_CONSUMER) {
				paymentMethodInfo = ((PostpaidConsumerAccountInfo) accountInfo).getPaymentMethod0();
				personalCerditInfo = ((PostpaidConsumerAccountInfo) accountInfo).getPersonalCreditInformation0();
			} else {
				if (accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_PERSONAL || accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_IDEN_PERSONAL
						|| accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL) {
					paymentMethodInfo = ((PostpaidBusinessPersonalAccountInfo) accountInfo).getPaymentMethod0();
					personalCerditInfo = ((PostpaidBusinessPersonalAccountInfo) accountInfo).getPersonalCreditInformation0();
				} else {
					paymentMethodInfo = ((PostpaidBusinessRegularAccountInfo) accountInfo).getPaymentMethod0();
				}
			}

			// set payment method to REGULAR.
			paymentMethodInfo.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_REGULAR);

			// clean out credit card info if expired.
			paymentMethodInfo.setCreditCard0(new CreditCardInfo());

			// clean out direct debit account info if exists.
			paymentMethodInfo.setCheque0(new ChequeInfo());

			// clean out drivers license info if expired.
			if (personalCerditInfo != null) {
				if (personalCerditInfo.isDriversLicenseExpired()) {
					personalCerditInfo.setDriversLicense(null);
					personalCerditInfo.setDriversLicenseExpiry(null);
					personalCerditInfo.setDriversLicenseProvince(null);
				}

				CreditCardInfo creditCard = personalCerditInfo.getCreditCard0();
				if (creditCard.isExpired()) {
					creditCard.setToken(null, null, null);
					creditCard.setExpiryMonth(0);
					creditCard.setExpiryYear(0);
					creditCard.setHolderName("");
					creditCard.setAuthorizationCode(null);
				}
			}

			// replace contact name with the billing one if first or last names are wrong
			if (accountInfo instanceof PostpaidConsumerAccountInfo || accountInfo.getClass().getSuperclass().getName().equals("PostpaidConsumerAccountInfo")) {
				ConsumerNameInfo contactName = ((PostpaidConsumerAccountInfo) accountInfo).getContactName0();
				ConsumerNameInfo billingName = ((PostpaidConsumerAccountInfo) accountInfo).getName0();

				if (contactName.getFirstName() == null || contactName.getFirstName().trim().equals("") || contactName.getLastName() == null || contactName.getLastName().trim().equals("")) {
					contactName.setFirstName(billingName.getFirstName());
					contactName.setLastName(billingName.getLastName());
					contactName.setMiddleInitial(billingName.getMiddleInitial());
					contactName.setGeneration(billingName.getGeneration());
					contactName.setTitle(billingName.getTitle());
					contactName.setAdditionalLine(billingName.getAdditionalLine());
				}
			}

			// replace expired dealer code with the default one
			try {
				provider.getReferenceDataManager().getDealer(getDealerCode());
				// dealer is retrieved: it's valid and no additional activites are required
			} catch (Exception e) {
				// dealer is invalid or expired: set the default one
				AccountType accountType = provider.getReferenceDataManager().getAccountType(this);
				accountInfo.setDealerCode(accountType.getDefaultDealer());
				accountInfo.setSalesRepCode(accountType.getDefaultSalesCode());
			}
			Segmentation seg = provider.getReferenceDataManager().getSegmentation(accountInfo.getBrandId(), Character.toString(delegate.getAccountType()), getAddress().getProvince());
			if (seg != null) {
				accountInfo.setBanSegment(seg.getSegment());
				accountInfo.setBanSubSegment(seg.getSubSegment());
			}
			int banID = 0;
			// create new BAN.
			banID = accountLifecycleFacade.createAccount(accountInfo, SessionUtil.getSessionId(accountLifecycleFacade));
			// save new account info.
			accountInfo.setBanId(banID);

			// Check the account type and set the credit check to manual.
			saveCreditCheckInfo(accountInfo);

			// copy fleets and talk groups for Mikes
			if (isIDEN()) {
				FleetInfo[] fleets = null;
				fleets = accountInformationHelper.retrieveFleetsByBan(getBanId());
				int fleetsSz = fleets != null ? fleets.length : 0;

				for (int i = 0; i < fleetsSz; i++) {
					accountLifecycleManager.addFleet(banID, (short) fleets[i].getNetworkId(), fleets[i], fleets[i].getExpectedSubscribers(), SessionUtil.getSessionId(accountLifecycleManager));
				}
				TalkGroupInfo[] talkGroups = null;
				List list = accountInformationHelper.retrieveTalkGroupsByBan(getBanId());
				talkGroups = (TalkGroupInfo[]) list.toArray(new TalkGroupInfo[list.size()]);
				if (talkGroups != null) {
					accountLifecycleManager.addTalkGroups(banID, talkGroups, SessionUtil.getSessionId(accountLifecycleManager));
				}
			}

			// create duplicate account.
			//duplicateAccount = new TMAccount(provider, accountInfo);
			//duplicateAccount.refresh();
			//Jan-02,2007, mliao: comment out above two lines, do a lookup for the new account,
			//so that we can return more specific Account class instance rather than just TMAccount.
			duplicateAccount = provider.getAccountManager().findAccountByBAN(banID);

			// set client consent indicators.
			ClientConsentIndicator[] clientConsentIndicators = getClientConsentIndicators();
			if (clientConsentIndicators != null && clientConsentIndicators.length > 0) {
				duplicateAccount.setClientConsentIndicators(clientConsentIndicators);
				duplicateAccount.save();
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return duplicateAccount;
	}

	private void saveCreditCheckInfo(AccountInfo accountInfo) throws RemoteException, TelusException, TelusAPIException, AuthenticationException {

		try {
			char accSubType = accountInfo.getAccountSubType();
			if (!(accSubType == ACCOUNT_SUBTYPE_PAGER_BOXED || accSubType == ACCOUNT_SUBTYPE_PAGER_REGULAR || accSubType == ACCOUNT_SUBTYPE_AUTOTEL_EARS
					|| accSubType == ACCOUNT_SUBTYPE_AUTOTEL_REGULAR)) {
				// defect PROD00081023 fix: April 27,2007 - M. Liao
				// This is method was originally extracted from createDuplicateAccount(). But when it run under Copy Ban context (for M2P )
				// getDelegate0().getBanId() is the new account's banId, which will cause the following statement return a empty list.
				// businessesList = provider.getAccountHelperEJB().retrieveBusinessList(getDelegate0().getBanId())
				// The fix is: introduce a new field: originalBanId, when copy BAN, this field will be populated with original BAN id.
				// If originalBanId != 0 this is a Copy BAN scenario, we will use the originalBanId to retrieve the credit check result/business list.
				// if originalBanId == 0, that means this is a create duplicate account scenario, and this method is called on the original account instance,
				// so getDelegate0().getBanId is the original ban id.
				int banId = getDelegate0().getOriginalBanId() != 0 ? getDelegate0().getOriginalBanId() : getDelegate0().getBanId();
				// Defect 54265 fix: CDA 2017, April 15, 2017 - R. Fong
				// Retrieve the KB credit check result and save it for the copy BAN and create duplicate account scenarios, as the CDA 'credit worthiness' values do not contain the
				// original Equifax results.
				String productType = accountInfo.isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS;
				CreditCheckResultInfo existingCreditCheckResult = accountLifecycleManager.retrieveKBCreditCheckResultByBan(banId, productType, SessionUtil.getSessionId(accountLifecycleManager));
				CreditCheckResultInfo targetAccountCreditCheckResult = accountInfo.getCreditCheckResult0();
				targetAccountCreditCheckResult.copyFrom(existingCreditCheckResult);
				targetAccountCreditCheckResult.fixNoCellularDepositInfo();

				if (accountInfo.getAccountType() == ACCOUNT_TYPE_CONSUMER || (accountInfo.getAccountType() == ACCOUNT_TYPE_BUSINESS && (accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_PERSONAL)
						|| accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_IDEN_PERSONAL || accountInfo.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)) {
					accountLifecycleManager.saveCreditCheckInfo(accountInfo, targetAccountCreditCheckResult, "M", SessionUtil.getSessionId(accountLifecycleManager));
				} else {
					BusinessCreditIdentityInfo[] businessesList = null;
					businessesList = accountInformationHelper.retrieveBusinessList(banId);
					if (businessesList != null && businessesList.length > 0) {
						accountLifecycleManager.saveCreditCheckInfoForBusiness(accountInfo, businessesList, businessesList[0], targetAccountCreditCheckResult, "M",
								SessionUtil.getSessionId(accountLifecycleManager));
					} else {
						// TMProvider.debug("Could not save credit check info for the new account: list of businesses is empty.");
						Logger.debug("BAN[" + banId + "]'s list of businesses is empty.");
						// the following call won't throw any exception, it will create one entry in CREDIT_HISTORY table, but no entry in CRD_BUSINESS_LIST
						accountLifecycleManager.saveCreditCheckInfoForBusiness(accountInfo, businessesList, null, targetAccountCreditCheckResult, "M",
								SessionUtil.getSessionId(accountLifecycleManager));
					}
				}
			}
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public FutureStatusChangeRequest[] getFutureStatusChangeRequests() throws TelusAPIException {

		FutureStatusChangeRequest[] futureStatusChangeRequests = null;
		try {
			List list = accountLifecycleManager.retrieveFutureStatusChangeRequests(getBanId(), SessionUtil.getSessionId(accountLifecycleManager));
			futureStatusChangeRequests = decorate((FutureStatusChangeRequest[]) list.toArray(new FutureStatusChangeRequest[list.size()]));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}

		return futureStatusChangeRequests;
	}

	@Override
	public SearchResults getCredits(Date from, Date to, String billState, String reasonCode, char level, String subscriberId, int maximum) throws TelusAPIException {

		SearchResultsInfo results = null;
		try {
			results = accountInformationHelper.retrieveCredits(getBanId(), from, to, billState, null, reasonCode, level, subscriberId, maximum);
			CreditInfo[] info = (CreditInfo[]) results.getItems();
			results.setItems(decorate(info));

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return results;
	}

	@Override
	public FollowUpStatistics getFollowUpStatistics() throws TelusAPIException {
		return getFollowUpStatistics(false);
	}

	@Override
	public FollowUpStatistics getFollowUpStatistics(boolean refresh) throws TelusAPIException {

		FollowUpStatisticsInfo followUpStatisticsInfo = null;
		try {
			followUpStatisticsInfo = delegate.getFollowUpStatistics0();

			if (followUpStatisticsInfo == null || refresh) {

				followUpStatisticsInfo = accountInformationHelper.retrieveFollowUpStatistics(getBanId());

				delegate.setFollowUpStatistics0(followUpStatisticsInfo);
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return followUpStatisticsInfo;
	}

	@Override
	public boolean validTargetAccountType(char accountType, char accountSubType) {
		return delegate.validTargetAccountType(accountType, accountSubType);
	}

	private DOMTransformer domTransformer;
	private Mailer mailer;
	public static final String XSL_MikeSharedFleetApplicationFormFax = "MikeSharedFleetApplicationFax.xsl";
	public static final String XSL_MikeSharedFleetApplicationFormEmail = "MikeSharedFleetApplicationFormEmail.xsl";

	@Override
	public void sendFax(final int form, String faxNumber, String language) throws TelusAPIException {

		if (domTransformer == null) {
			domTransformer = new DOMTransformer();
		}
		if (mailer == null) {
			mailer = new Mailer();
		}

		if (form == 0 || faxNumber == null || language == null) {
			throw new java.lang.IllegalArgumentException("Illegal arguments: [final int form, String faxNumber, String language] cannot be null.");
		}

		if (Account.FORM_MIKE_SHARED_FLEET_APPLICATION == form) {
			MikeSharedFleetApplication application = new MikeSharedFleetApplication(this);
			application.setFaxNumber(faxNumber);

			Document document = domTransformer.newMikeSharedFleetApplicationDocument(application);
			byte[] content = domTransformer.transform(document, XSL_MikeSharedFleetApplicationFormFax, language);

			mailer.sendFax(faxNumber, content);

		} else {
			throw new TelusAPIException("Invalid argument: [int form] does not match any existing forms.");
		}
	}

	@Override
	public void sendEmail(final int form, String email, String language) throws TelusAPIException {

		if (domTransformer == null) {
			domTransformer = new DOMTransformer();
		}
		if (mailer == null) {
			mailer = new Mailer();
		}

		if (form == 0 || email == null || language == null) {
			throw new java.lang.IllegalArgumentException("Illegal arguments: [final int form, String email, String language] cannot be null.");
		}

		InternetAddress sender;
		InternetAddress[] recipients;

		try {
			sender = new InternetAddress("Telus Mobility<DoNotReply@telus.com>");
			recipients = new InternetAddress[] { new InternetAddress(email) };
		} catch (AddressException ae) {
			throw new TelusAPIException(ae);
		}

		if (Account.FORM_MIKE_SHARED_FLEET_APPLICATION == form) {
			MikeSharedFleetApplication application = new MikeSharedFleetApplication(this);

			Document document = domTransformer.newMikeSharedFleetApplicationDocument(application);
			byte[] content = domTransformer.transform(document, XSL_MikeSharedFleetApplicationFormEmail, language);

			mailer.sendEmail(sender, recipients, "Telus Mobility Mike Shared Fleet Application", content);

		} else {
			throw new TelusAPIException("Invalid argument: [int form] does not match any existing forms.");
		}
	}

	@Override
	public ProductSubscriberList[] getProductSubscriberLists() {

		if (!productSubscriberListCached) {
			try {
				delegate.setProductSubscriberLists(accountInformationHelper.retrieveProductSubscriberLists(getBanId()));
				productSubscriberListCached = true;
			} catch (Exception e) {
				Logger.debug("getProductSubscriberLists exception on ban=" + getBanId() + ": " + e);
			}
		}

		return delegate.getProductSubscriberLists();
	}

	@Override
	public Discount newDiscount() throws TelusAPIException {
		DiscountInfo info = new DiscountInfo();
		info.setBan(getBanId());
		return new TMDiscount(provider, info);
	}

	public Discount[] getDiscounts() throws TelusAPIException {

		Discount[] discounts = null;
		try {
			DiscountInfo[] discountInfos = null;
			List list = accountLifecycleManager.retrieveDiscounts(getBanId(), SessionUtil.getSessionId(accountLifecycleManager));
			discountInfos = (DiscountInfo[]) list.toArray(new DiscountInfo[list.size()]);
			discounts = provider.getAccountManager0().decorate(discountInfos);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return discounts;
	}

	public DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException {

		try {
			DiscountPlan[] discountPlans = provider.getReferenceDataManager().getDiscountPlans(true);
			ArrayList discPlansForBanList = new ArrayList();
			int accBrandId = delegate.getBrandId();
			boolean found = false;
			for (int i = 0; i < discountPlans.length; i++) {
				found = false;
				if (accBrandId == 0) {
					found = true;
				} else {
					int[] discountBrandIDs = discountPlans[i].getDiscountBrandIDs();
					if (discountBrandIDs == null || discountBrandIDs.length == 0 || (discountBrandIDs.length == 1 && discountBrandIDs[0] == 0)) {
						found = true;
					} else {
						for (int j = 0; j < discountBrandIDs.length; j++) {
							if (discountBrandIDs[j] == accBrandId) {
								found = true;
								break;
							}
						}
					}
				}
				if (discountPlans[i].getLevel().equals("B") && found) {
					discPlansForBanList.add(discountPlans[i]);
				}
			}

			return (DiscountPlan[]) discPlansForBanList.toArray(new DiscountPlan[discPlansForBanList.size()]);

		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	public void changeCreditCheckDeposits(CreditCheckResultDeposit[] creditCheckResultDeposits, CreditCheckDepositChangeReason reasonCode, String reasonText) throws TelusAPIException {

		try {
			provider.getAccountLifecycleFacade().updateCreditCheckResult(getAccount().getBanId(), getAccount().getCreditCheckResult().getCreditClass(),
					(CreditCheckResultDepositInfo[]) creditCheckResultDeposits, reasonCode.getCode(), reasonText, SessionUtil.getSessionId(accountLifecycleManager));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
		}
	}

	@Override
	public Credit[] getCreditByFollowUpId(int followUpId) throws TelusAPIException {

		Credit[] credits = null;
		try {
			List list = accountInformationHelper.retrieveCreditByFollowUpId(followUpId);
			credits = provider.getAccountManager0().decorate((CreditInfo[]) list.toArray(new CreditInfo[list.size()]), getAccount());

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return credits;
	}

	/**
	 * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
	 */
	@Override
	public CancellationPenalty[] getCancellationPenaltyList(String[] subscriberId) throws TelusAPIException, InvalidMultiSubscriberOperationException {

		CancellationPenalty[] cancellationPenalties = null;
		try {
			cancellationPenalties = accountLifecycleManager.retrieveCancellationPenaltyList(getAccount().getBanId(), subscriberId,
					SessionUtil.getSessionId(accountLifecycleManager));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return cancellationPenalties;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void cancelSubscribers(Date effectiveDate, String reason, char depMethod, String[] subscriberId, String[] waiverReason, String comment)
			throws TelusAPIException, InvalidMultiSubscriberOperationException {
		cancelSubscribers(effectiveDate, reason, depMethod, subscriberId, waiverReason, comment, null);
	}

	@Override
	public void suspendSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException, InvalidMultiSubscriberOperationException {
		try {
			accountLifecycleFacade.suspendSubscribers(getAccount().getBanId(), effectiveDate, reason, subscriberId, comment,SessionUtil.getSessionId(accountLifecycleFacade));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	@Override
	public void restoreSuspendedSubscribers(Date effectiveDate, String reason, String[] subscriberId, String comment) throws TelusAPIException, InvalidMultiSubscriberOperationException {
		try {
			Boolean isPostpaidBusinessConnect = Boolean.valueOf(isPostpaidBusinessConnect());
			accountLifecycleFacade.restoreSuspendedSubscribers(getAccount().getBanId(), effectiveDate, reason, subscriberId, comment, Integer.valueOf(delegate.getBrandId()), isPostpaidBusinessConnect,
					Character.valueOf(delegate.getStatus()), SessionUtil.getSessionId(accountLifecycleManager));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public PricePlanSubscriberCount[] getAirtimeMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException {
		PoolingPricePlanSubscriberCount poolingPricePlanSubscriberCount = getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.AIRTIME, false);
		if (poolingPricePlanSubscriberCount != null) {
			return poolingPricePlanSubscriberCount.getPricePlanSubscriberCount();
		}
		return null;
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public PricePlanSubscriberCount[] getLDMinutePoolingEnabledPricePlanSubscriberCounts() throws TelusAPIException {
		PoolingPricePlanSubscriberCount poolingPricePlanSubscriberCount = getPoolingEnabledPricePlanSubscriberCount(PoolingGroup.LONG_DISTANCE, false);
		if (poolingPricePlanSubscriberCount != null) {
			return poolingPricePlanSubscriberCount.getPricePlanSubscriberCount();
		}
		return null;
	}

	// Not used anywhere
	//	  private PricePlanSubscriberCount[] getMinutePoolingEnabledPricePlanSubscriberCounts(String[] poolingCoverageTypes) throws TelusAPIException{
	//		  try {
	//			  /*
	//			   * per defect PROD00093589, Rad's instruction, the following restriction should be removed.
	//			  if (this.isCorporate())
	//				  return new PricePlanSubscriberCount[0];
	//			  else
	//			  */
	//			  return provider.getAccountHelperEJB().retrieveMinutePoolingEnabledPricePlanSubscriberCounts(getBanId(), poolingCoverageTypes);
	//			  
	//		  } catch (Throwable e) {
	//			  throw new TelusAPIException(e);
	//		  }
	//	  }

	public PoolingPricePlanSubscriberCount[] getPoolingEnabledPricePlanSubscriberCount(boolean refresh) throws TelusAPIException {

		PoolingPricePlanSubscriberCount[] poolingCountResult = null;
		if (refresh) {
			try {
				List list = accountInformationHelper.retrievePoolingPricePlanSubscriberCounts(getBanId());
				poolingCountResult = (PoolingPricePlanSubscriberCountInfo[]) list.toArray(new PoolingPricePlanSubscriberCountInfo[list.size()]);

				HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(POOLING_KEY);
				if (poolingPricePlanSubscriberCountCache != null) {
					poolingPricePlanSubscriberCountCache.clear();
				} else {
					poolingPricePlanSubscriberCountCache = new HashMap();
				}

				// init the Map with all pool groups, key value is null
				PoolingGroup[] poolGroups = provider.getReferenceDataManager().getPoolingGroups();
				for (int i = 0; i < poolGroups.length; i++) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolGroups[i].getPoolingGroupId()), null);
				}

				// put in the real ones for those pooling groups that have results
				for (int i = 0; i < poolingCountResult.length; i++) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingCountResult[i].getPoolingGroupId()), poolingCountResult[i]);
				}
				pricePlanSubscriberCountHashMapCache.put(POOLING_KEY, poolingPricePlanSubscriberCountCache);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return poolingCountResult;

		} else {
			HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(POOLING_KEY);
			if (poolingPricePlanSubscriberCountCache == null) {
				//poolingPricePlanCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getPoolingEnabledPricePlanSubscriberCount(true);
			}

			List list = new ArrayList();
			PoolingGroup[] poolGroups = provider.getReferenceDataManager().getPoolingGroups();
			for (int i = 0; i < poolGroups.length; i++) {
				PoolingPricePlanSubscriberCount poolingPricePlanSubscriberCount = getPoolingEnabledPricePlanSubscriberCount(poolGroups[i].getPoolingGroupId(), false);
				if (poolingPricePlanSubscriberCount != null)
					list.add(poolingPricePlanSubscriberCount);
			}

			return (PoolingPricePlanSubscriberCount[]) list.toArray(new PoolingPricePlanSubscriberCount[list.size()]);
		}
	}

	public PoolingPricePlanSubscriberCount getPoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException {

		PoolingPricePlanSubscriberCount[] poolingCountResult = null;
		if (refresh) {
			try {
				List list = accountInformationHelper.retrievePoolingPricePlanSubscriberCounts(getBanId(), poolingGroupId);
				poolingCountResult = (PoolingPricePlanSubscriberCountInfo[]) list.toArray(new PoolingPricePlanSubscriberCountInfo[list.size()]);

				HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(POOLING_KEY);
				if (poolingPricePlanSubscriberCountCache == null)
					poolingPricePlanSubscriberCountCache = new HashMap();

				if (poolingCountResult.length > 0) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingCountResult[0].getPoolingGroupId()), poolingCountResult[0]);
					pricePlanSubscriberCountHashMapCache.put(POOLING_KEY, poolingPricePlanSubscriberCountCache);
					return poolingCountResult[0];
				}

				//No result returned but must log entry in HashMap to ensure subsequent calls to this function, 
				//with refresh parameter set to false, does not result in a call to DB (like in true scenario) 

				//log null key value into HashMap when no entry is found
				poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingGroupId), null);
				pricePlanSubscriberCountHashMapCache.put(POOLING_KEY, poolingPricePlanSubscriberCountCache);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return null; // no match found in database

		} else {
			HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(POOLING_KEY);
			if (poolingPricePlanSubscriberCountCache != null) {
				PoolingPricePlanSubscriberCount poolingSubscriberCountResult = (PoolingPricePlanSubscriberCount) poolingPricePlanSubscriberCountCache.get(String.valueOf(poolingGroupId));
				if (poolingSubscriberCountResult != null) {
					return poolingSubscriberCountResult;
				}

				// if key value is null, and key is there, it is not there, no need to check again.
				if (poolingSubscriberCountResult == null && poolingPricePlanSubscriberCountCache.containsKey(String.valueOf(poolingGroupId))) {
					return null;
				}
				// else if no results are retrieved from the poolingPricePlanCountCache then need to retrieve from database (using a recursive call)
				return getPoolingEnabledPricePlanSubscriberCount(poolingGroupId, true);

			} else {
				// poolingPricePlanCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getPoolingEnabledPricePlanSubscriberCount(poolingGroupId, true);
			}
		}
	}

	public PoolingPricePlanSubscriberCount[] getZeroMinutePoolingEnabledPricePlanSubscriberCount(boolean refresh) throws TelusAPIException {

		PoolingPricePlanSubscriberCount[] poolingCountResult = null;
		if (refresh) {
			try {
				List list = accountInformationHelper.retrieveZeroMinutePoolingPricePlanSubscriberCounts(getBanId());
				poolingCountResult = (PoolingPricePlanSubscriberCountInfo[]) list.toArray(new PoolingPricePlanSubscriberCountInfo[list.size()]);

				HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(ZERO_MINUTE_POOLING_KEY);
				if (poolingPricePlanSubscriberCountCache != null) {
					poolingPricePlanSubscriberCountCache.clear();
				} else {
					poolingPricePlanSubscriberCountCache = new HashMap();
				}

				// init the Map with all pool groups, key value is null
				PoolingGroup[] poolGroups = provider.getReferenceDataManager().getPoolingGroups();
				for (int i = 0; i < poolGroups.length; i++) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolGroups[i].getPoolingGroupId()), null);
				}

				// put in the real ones for those pooling groups that have results
				for (int i = 0; i < poolingCountResult.length; i++) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingCountResult[i].getPoolingGroupId()), poolingCountResult[i]);
				}
				pricePlanSubscriberCountHashMapCache.put(ZERO_MINUTE_POOLING_KEY, poolingPricePlanSubscriberCountCache);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return poolingCountResult;

		} else {
			HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(ZERO_MINUTE_POOLING_KEY);
			if (poolingPricePlanSubscriberCountCache != null) {
				return (PoolingPricePlanSubscriberCount[]) poolingPricePlanSubscriberCountCache.values().toArray(new PoolingPricePlanSubscriberCount[poolingPricePlanSubscriberCountCache.size()]);
			} else {
				//poolingPricePlanCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getZeroMinutePoolingEnabledPricePlanSubscriberCount(true);
			}
		}
	}

	public PoolingPricePlanSubscriberCount getZeroMinutePoolingEnabledPricePlanSubscriberCount(int poolingGroupId, boolean refresh) throws TelusAPIException {

		PoolingPricePlanSubscriberCount[] poolingCountResult = null;
		if (refresh) {
			try {
				List list = accountInformationHelper.retrieveZeroMinutePoolingPricePlanSubscriberCounts(getBanId(), poolingGroupId);
				poolingCountResult = (PoolingPricePlanSubscriberCountInfo[]) list.toArray(new PoolingPricePlanSubscriberCountInfo[list.size()]);

				HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(ZERO_MINUTE_POOLING_KEY);
				if (poolingPricePlanSubscriberCountCache == null) {
					poolingPricePlanSubscriberCountCache = new HashMap();
				}

				if (poolingCountResult.length > 0) {
					poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingCountResult[0].getPoolingGroupId()), poolingCountResult[0]);
					pricePlanSubscriberCountHashMapCache.put(ZERO_MINUTE_POOLING_KEY, poolingPricePlanSubscriberCountCache);
					return poolingCountResult[0];
				}

				//No result returned but must log entry in HashMap to ensure subsequent calls to this function, 
				//with refresh parameter set to false, does not result in a call to DB (like in true scenario) 

				//log null key value into HashMap when no entry is found
				poolingPricePlanSubscriberCountCache.put(String.valueOf(poolingGroupId), null);
				pricePlanSubscriberCountHashMapCache.put(ZERO_MINUTE_POOLING_KEY, poolingPricePlanSubscriberCountCache);
			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return null; // no match found in database

		} else {
			HashMap poolingPricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(ZERO_MINUTE_POOLING_KEY);
			if (poolingPricePlanSubscriberCountCache != null) {
				PoolingPricePlanSubscriberCount poolingSubscriberCountResult = (PoolingPricePlanSubscriberCount) poolingPricePlanSubscriberCountCache.get(String.valueOf(poolingGroupId));
				if (poolingSubscriberCountResult != null)
					return poolingSubscriberCountResult;

				// no such pooling group no need to try again
				if (poolingSubscriberCountResult == null && poolingPricePlanSubscriberCountCache.containsKey(String.valueOf(poolingGroupId))) {
					return null;
				}

				// else if no results are retrieved from the poolingPricePlanCountCache then need to retrieve from database (using a recursive call)
				return getZeroMinutePoolingEnabledPricePlanSubscriberCount(poolingGroupId, true);

			} else {
				// poolingPricePlanCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getZeroMinutePoolingEnabledPricePlanSubscriberCount(poolingGroupId, true);
			}
		}
	}

	public PricePlanSubscriberCount[] getDollarPoolingPricePlanSubscriberCount(boolean refresh) throws TelusAPIException {

		PricePlanSubscriberCount[] pricePlanSubscriberCounts = null;
		if (refresh) {
			try {
				if (isIDEN()) {
					List list = accountInformationHelper.retrieveDollarPoolingPricePlanSubscriberCounts(getBanId(), Subscriber.PRODUCT_TYPE_IDEN);
					pricePlanSubscriberCounts = (PricePlanSubscriberCountInfo[]) list.toArray(new PricePlanSubscriberCountInfo[list.size()]);
				} else {
					List list = accountInformationHelper.retrieveDollarPoolingPricePlanSubscriberCounts(getBanId(), Subscriber.PRODUCT_TYPE_PCS);
					pricePlanSubscriberCounts = (PricePlanSubscriberCountInfo[]) list.toArray(new PricePlanSubscriberCountInfo[list.size()]);
				}
				HashMap pricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(DOLLAR_POOLING_KEY);
				if (pricePlanSubscriberCountCache != null) {
					pricePlanSubscriberCountCache.clear();
				} else {
					pricePlanSubscriberCountCache = new HashMap();
				}

				for (int i = 0; i < pricePlanSubscriberCounts.length; i++) {
					pricePlanSubscriberCountCache.put(pricePlanSubscriberCounts[i].getPricePlanCode(), pricePlanSubscriberCounts[i]);
				}

				pricePlanSubscriberCountHashMapCache.put(DOLLAR_POOLING_KEY, pricePlanSubscriberCountCache);

			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}

			return pricePlanSubscriberCounts;

		} else {
			HashMap pricePlanSubscriberCountCache = (HashMap) pricePlanSubscriberCountHashMapCache.get(DOLLAR_POOLING_KEY);
			if (pricePlanSubscriberCountCache != null) {
				return (PricePlanSubscriberCount[]) pricePlanSubscriberCountCache.values().toArray(new PricePlanSubscriberCount[pricePlanSubscriberCountCache.size()]);
			} else {
				// pricePlanSubscriberCountCache has yet to be created.  Therefore, we need to retrieve from database (using a recursive call)
				return getDollarPoolingPricePlanSubscriberCount(true);
			}
		}
	}

	public void refreshPricePlanSubscriberCounts() throws TelusAPIException {
		//Refresh minute pooling-enabled subscriber counts
		getPoolingEnabledPricePlanSubscriberCount(true);
		//Refresh dollar pooling-enabled subscriber counts
		getDollarPoolingPricePlanSubscriberCount(true);
		//Refresh shareable price-plan subscriber counts
		getShareablePricePlanSubscriberCount(true);
		//Refresh zero-minute pooling-enabled subscriber counts
		getZeroMinutePoolingEnabledPricePlanSubscriberCount(true);
	}

	@Override
	public boolean isFeatureCategoryExistOnSubscribers(String categoryCode) throws TelusAPIException {

		boolean isFeatureCategoryExist = false;
		try {
			if (this.isCorporate())
				isFeatureCategoryExist = false;
			else {
				isFeatureCategoryExist = accountInformationHelper.isFeatureCategoryExistOnSubscribers(getBanId(), categoryCode);
			}
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return isFeatureCategoryExist;
	}

	@Override
	public DepositAssessedHistory[] getDepositAssessedHistory() throws TelusAPIException {

		DepositAssessedHistory[] depositAssessedHistories = null;
		try {
			depositAssessedHistories = accountInformationHelper.retrieveDepositAssessedHistoryList(getAccount().getBanId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return depositAssessedHistories;
	}

	@Override
	public DepositAssessedHistory[] getOriginalDepositAssessedHistory() throws TelusAPIException {

		DepositAssessedHistory[] depositAssessedHistories = null;
		try {
			depositAssessedHistories = accountInformationHelper.retrieveOriginalDepositAssessedHistoryList(getAccount().getBanId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return depositAssessedHistories;
	}

	@Override
	public ActivationOption[] getActivationOptions() throws TelusAPIException {
		try {
			return getCreditCheckResult().getActivationOptions();
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	@Override
	public PortRequest[] getPortRequests() throws PortRequestException, PRMSystemException, TelusAPIException {
		//		  PortRequest[] portRequest = getPortRequestSO().getCurrentPortRequestsByBan(this.getBanId());
		try {
			PortRequest[] portRequest = provider.getSubscriberLifecycleFacade().getCurrentPortRequestsByBan(this.getBanId());
			return portRequest;
		} catch (ApplicationException ae) {
			if (!ae.getErrorCode().equals("")) {
				throw new PortRequestException(ae, provider.getApplicationMessage(ApplicationSummary.APP_PRM, ae.getErrorCode(), ae));
			}
			throw new TelusAPIException(ae);
		}
	}

	private TMPortRequestSO getPortRequestSO() {
		if (portRequestSO == null) {
			portRequestSO = new TMPortRequestSO(provider);
		}
		return portRequestSO;
	}

	private boolean isCreditClassInSet(String[] valid_credit_classes) {

		String accounts_credit_class = this.getCreditCheckResult().getCreditClass();
		for (int x = 0; x < valid_credit_classes.length; x++) {
			if (accounts_credit_class != null && valid_credit_classes[x].equals(accounts_credit_class)) {
				return true;
			}
		}

		return false;
	}

	public boolean isCLP() {
		return isCreditClassInSet(CLP_CREDIT_CLASSES);
	}

	private boolean isTenureLessThan(int tenureComparedAgainst) {
		Date currentDate = Calendar.getInstance().getTime();
		Date accountDate = this.getCreateDate();
		long duration = currentDate.getTime() - accountDate.getTime();
		return (duration / MILLI_SEC_PER_DAY) < tenureComparedAgainst;
	}

	private boolean isNoPreviousPayments() throws TelusAPIException {

		PaymentHistory[] payments = null;
		try {
			payments = getPaymentHistory(getStartServiceDate(), new Date());
		} catch (Exception ex) {
			throw new TelusAPIException(ex);
		}

		return (payments == null) || (payments.length == 0);
	}

	private boolean isInsufficientTimeBeforeInvoiceDueDate(Date dueDate, int numberOfDays) {
		return ((dueDate.getTime() - Calendar.getInstance().getTime().getTime()) / MILLI_SEC_PER_DAY < numberOfDays);
	}

	private boolean isNSFOrBackedOut(int numberOfMonths) throws TelusAPIException {

		MonthlyFinancialActivity[] monthlyActivity = getFinancialHistory().getMonthlyFinancialActivity();
		int activitySz = monthlyActivity != null ? monthlyActivity.length : 0;
		for (int i = activitySz - 1, count = numberOfMonths - 1; i >= 0 && count >= 0; --i, --count) {
			if (monthlyActivity[i].getDishonoredPaymentCount() > 0) {
				return true;
			}
		}

		return false;
	}

	FollowUp[] followUps = null;

	private FollowUp[] getFollowUps0() {

		if (followUps == null) {
			try {
				followUps = getFollowUps(0);
			} catch (TelusAPIException e) {
				followUps = new FollowUp[0];
			}
		}

		return followUps;
	}

	private boolean isTurnedOvertoAgency() throws TelusAPIException {
		String output = getFinancialHistory().getCollectionState().getAgencyCode();
		return (output != null && output.trim().length() > 0);
	}

	private boolean isNextStepExceedPostingDate(Date nextDueDate) throws TelusAPIException {
		return getFinancialHistory().getCollectionState().getNextCollectionStep().getTreatmentDate().getTime() > nextDueDate.getTime();
	}

	@Override
	public void setBrandId(int brandId) {
		delegate.setBrandId(brandId);
	}

	@Override
	public void updateBrand(int brandId, String memoText) throws TelusAPIException {
		try {
			setBrandId(brandId);
			accountLifecycleManager.updateBrand(delegate.getBanId(), brandId, memoText, SessionUtil.getSessionId(accountLifecycleManager));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public void save(boolean saveCreditCheckInfo) throws TelusAPIException, InvalidCreditCardException {

		try {
			// ----------------------------------------------------------------
			// Validate CreditCard.
			// TODO: make this a method on credit card.
			// ----------------------------------------------------------------
			if (getBanId() == 0) {
				validateCreditCards(BusinessRole.BUSINESS_ROLE_ALL);
			}

			// ----------------------------------------------------------------
			// Update/Create Account.
			// ----------------------------------------------------------------
			if (getBanId() == 0) {
				Segmentation seg = provider.getReferenceDataManager().getSegmentation(delegate.getBrandId(), Character.toString(delegate.getAccountType()), getAddress().getProvince());
				if (seg != null) {
					delegate.setBanSegment(seg.getSegment());
					delegate.setBanSubSegment(seg.getSubSegment());
				}
				int banId = 0;
				banId = accountLifecycleFacade.createAccount(delegate, SessionUtil.getSessionId(accountLifecycleFacade));
				delegate.setBanId(banId);

				if (saveCreditCheckInfo) {
					saveCreditCheckInfo(delegate);
				}

				// ----------------------------------------------------
				// Create corporate request memo.
				// ----------------------------------------------------
				if (delegate.getAccountType() == ACCOUNT_TYPE_CORPORATE && delegate.getAccountSubType() != ACCOUNT_SUBTYPE_PCS_REGIONAL) {
					createFollowUp(FOLLOWUP_CORPORATE_NEW_ACCOUNT, FOLLOWUP_CORPORATE_DESTINATION, "corporate account created by dealer " + getDealerCode() + "-" + getSalesRepCode());
					createdCorporateNewAccountFollowUp = true;
				}

			} else {
				//				  if (oldAddress != null && !oldAddress.equals(address)){
				//				     if(provider.getBlockDirectUpdate() && blockRuleStatus){
				//					     delegate.setAddress0( oldAddress.getDelegate());
				//				     }
				//				  }
				//				  
				//				  if(delegate.isPostpaidConsumer()){
				//					  ConsumerNameInfo oldConsumerName= (ConsumerNameInfo)((TMPostpaidConsumerAccount)getAccount0()).getOldConsumerName();
				//					  ConsumerNameInfo consumerName=(ConsumerNameInfo)((TMPostpaidConsumerAccount) getAccount0()).getName();
				//					  if (oldConsumerName != null && !(oldConsumerName.toString()).equals(consumerName.toString())){
				//					     if(provider.getBlockDirectUpdate() && blockRuleStatus){
				//							 ((ConsumerNameInfo)((TMPostpaidConsumerAccount) getAccount0()).getName()).copyFrom(oldConsumerName);
				//					     }
				//					  }
				//				  }

				delegate.getAddress0().normalize();
				delegate.getAlternateCreditCheckAddress0().normalize();
				accountLifecycleFacade.updateAccount(delegate, SessionUtil.getSessionId(accountLifecycleFacade));
				if (oldAddress != null && !oldAddress.equals(address)) {
					if (!(isBlockDirectUpdate() && blockRuleStatus)) {
						provider.getInteractionManager0().changeAddress(this);
					}
				}
			}

			// ----------------------------------------------------------------
			// Reload entire account data.
			// ----------------------------------------------------------------
			refresh();
			commit();
			if (!(isBlockDirectUpdate() && blockRuleStatus)) {
				// save PIN for consumer prepaid account
				if (delegate.isPrepaidConsumer()) {
					if (oldPin != null && !delegate.getPin().equals(oldPin)) {
						Subscriber[] subs = getSubscribers(1); // prepaid only have one subscriber
						if (subs != null && subs.length > 0) { // new account won't have any subscriber.
							// the following method is only for update account PIN in prepaid platformm.
							accountLifecycleManager.updateAccountPIN(delegate.getBanId(), subs[0].getPhoneNumber(), subs[0].getEquipment().getSerialNumber(), oldPin, delegate.getPin(),
									provider.getUser());
							oldPin = null;
							Logger.debug("== PrepaidConsumer :: " + subs[0].getPhoneNumber() + " :: (new) PIN :: " + delegate.getPin());
						}
					}
				}
			}
			// Remove PP&S Services(if any) if new account is not eligible for PP&S services
			provider.getSubscriberLifecycleFacade().validatePPSServicesWhenAccountTypeChanged(delegate.getBanId(), delegate.getOldAccountType(), delegate.getOldAccountSubType(),
					delegate.getAccountType(), delegate.getAccountSubType(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

		} catch (Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderAddressExceptionTranslator(getAddress0());
			provider.getExceptionHandler().handleException(t, telusExceptionTranslator);
		}
	}

	@Override
	public String setBanSegment(String segment) {
		delegate.setBanSegment(segment);
		return delegate.getBanSegment();
	}

	@Override
	public String setBanSubSegment(String subsegment) {
		delegate.setBanSubSegment(subsegment);
		return delegate.getBanSubSegment();
	}

	@Override
	public void applyPayment(double amount, Cheque cheque, String sourceID, String sourceType) throws TelusAPIException, PaymentFailedException, UnknownObjectException {

		assertAccountExists();
		try {
			// payBill should not be called for Prepaid Account
			if (delegate.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_PREPAID) {
				throw new TelusAPIException("Can not use payBill on Prepaid Account");
			}

			sourceID = sourceID.trim();
			sourceType = sourceType.trim();
			//-------------------------------------------
			// Validate paymentSourceType.
			//-------------------------------------------
			PaymentSourceType paymentSourceType = provider.getReferenceDataManager().getPaymentSourceType(sourceID, sourceType);
			if (paymentSourceType == null) {
				throw new UnknownObjectException("Unknown PaymentSourceType: [sourceID=" + sourceID + ", sourceType=" + sourceType + "]");
			}

			//-------------------------------------------
			// Commit Transaction.
			//-------------------------------------------

			PaymentInfo paymentInfo = new PaymentInfo();
			paymentInfo.setBan(getBanId());
			paymentInfo.setAmount(amount);
			paymentInfo.setPaymentSourceID(sourceID);
			paymentInfo.setPaymentSourceType(sourceType);
			paymentInfo.setChequeInfo((ChequeInfo) cheque);
			paymentInfo.setDepositPaymentIndicator(false);
			paymentInfo.setDepositDate(new Date());
			paymentInfo.setAllowOverpayment(true);
			paymentInfo.setPaymentMethod(JNDINames.PAYMENT_METHOD_CHEQUE);

			accountLifecycleFacade.applyPaymentToAccount(delegate, paymentInfo, getTransientNotificationSuppressionInd(), null, SessionUtil.getSessionId(accountLifecycleFacade));
			//accountLifecycleManager.applyPaymentToAccount(paymentInfo,SessionUtil.getSessionId(accountLifecycleManager));
			provider.getInteractionManager0().makePayment(this, 'C', amount);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	/* Added by Sachin Sharma on 07-06-2010 */
	private Date findBusinessDays(int days) {

		Calendar calendar = Calendar.getInstance();
		int x = 0;
		while (x < days) {
			int day_of_week = calendar.get(Calendar.DAY_OF_WEEK);

			//Added by Sachin Sharma on August 23, 2010 for calculating CLP due date as per calendar days
			// if non-CLP client do not include weekend days
			if (!isCLP() && (day_of_week == Calendar.SUNDAY || day_of_week == Calendar.SATURDAY)) {
				//weekend do not count as a business day
			} else {
				x++;
			}
			calendar.add(Calendar.DATE, 1);
		}

		return calendar.getTime();
	}

	private boolean needToCallSRPDS(ServiceRequestHeader header) {
		return (header != null && AppConfiguration.isSRPDSEnabled() == true);
	}

	@Override
	public void save(ServiceRequestHeader header) throws TelusAPIException, InvalidCreditCardException {

		Address oldAccountAddress = getOldAddress();

		save();

		TMServiceRequestManager requestManager = (TMServiceRequestManager) provider.getServiceRequestManager();

		if (!needToCallSRPDS(header)) {
			return;
		}

		if (delegate.isAccountTypeChanged()) {
			requestManager.reportChangeAccountType(delegate.getBanId(), delegate.getDealerCode(), delegate.getSalesRepCode(), provider.getUser(), delegate.getStatus(), delegate.getOldAccountType(),
					delegate.getAccountType(), delegate.getOldAccountSubType(), delegate.getAccountSubType(), header);
		}

		if (oldAccountAddress != null && !oldAccountAddress.equals(getAddress())) {
			requestManager.reportChangeAccountAddress(delegate.getBanId(), delegate.getDealerCode(), delegate.getSalesRepCode(), provider.getUser(), getAddress(), header);
		}

		if (oldPin != null && !oldPin.equals(getPin())) {
			requestManager.reportChangeAccountPin(delegate.getBanId(), delegate.getDealerCode(), delegate.getSalesRepCode(), provider.getUser(), header);
		}
	}

	private void reportChangeSubscriberStatus(String[] subscriberIds, char status, String reason, Date activityDate, ServiceRequestHeader header) throws TelusAPIException {
		// ServiceRequestManager does not need old status when it log cancel activity
		// the old status is only used to differentiate the following two situations when the new status is 'Active': 
		//    - activate from reserve
		//    - restore from suspended
		//
		// so here, we don't need to send the real old status.  
		char oldStatus = ' ';
		String phoneNumber = ""; //TODO : do we really need to pass phone number? 
		for (int i = 0; i < subscriberIds.length; i++) {
			((TMServiceRequestManager) provider.getServiceRequestManager()).reportChangeSubscriberStatus(delegate.getBanId(), subscriberIds[i], delegate.getDealerCode(), delegate.getSalesRepCode(),
					provider.getUser(), phoneNumber, oldStatus, status, reason, activityDate, header);
		}
	}

	@Override
	public void cancelSubscribers(Date effectiveDate, String reason, char depMethod, String[] subscriberIds, String[] waiverReason, String comment, ServiceRequestHeader srpdsHeader)
			throws TelusAPIException, InvalidMultiSubscriberOperationException {
		try {
			Boolean isPostpaidBusinessConnect = Boolean.valueOf(isPostpaidBusinessConnect());
			accountLifecycleFacade.cancelSubscribers(getAccount().getBanId(), effectiveDate, reason, depMethod, subscriberIds, waiverReason, comment, isIDEN(), getTransientNotificationSuppressionInd(),
					 srpdsHeader, SessionUtil.getSessionId(accountLifecycleFacade));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		if (needToCallSRPDS(srpdsHeader)) {
			reportChangeSubscriberStatus(subscriberIds, Subscriber.STATUS_CANCELED, reason, effectiveDate, srpdsHeader);
		}
	}

	@Override
	public void cancel(Date activityDate, String reason, char depositReturnMethod, String waiver, String memoText, ServiceRequestHeader header) throws TelusAPIException {

		String[] subscriberIds = null;
		boolean needToCallSRPDS = needToCallSRPDS(header);
		if (needToCallSRPDS) {
			String[] suspendedSubIds = getSubscriberIdsByStatus(Subscriber.STATUS_SUSPENDED, getAllSuspendedSubscribersCount());
			String[] activeSubIds = getSubscriberIdsByStatus(Subscriber.STATUS_ACTIVE, getAllActiveSubscribersCount());
			subscriberIds = new String[suspendedSubIds.length + activeSubIds.length];
			System.arraycopy(suspendedSubIds, 0, subscriberIds, 0, suspendedSubIds.length);
			System.arraycopy(activeSubIds, 0, subscriberIds, suspendedSubIds.length, activeSubIds.length);
		}
		cancel(activityDate, reason, depositReturnMethod, waiver, memoText);
		if (needToCallSRPDS) {
			reportChangeSubscriberStatus(subscriberIds, Subscriber.STATUS_CANCELED, reason, activityDate, header);
		}
	}

	@Override
	public ManualCreditCheckRequest getManualCreditCheckRequest() {
		if (manualCreditRequest == null) {
			manualCreditRequest = new TMManualCreditCheckRequest(provider, this);
		}
		return manualCreditRequest;
	}

	@Override
	public int getHierarchyId() {
		return delegate.getHierarchyId();
	}

	@Override
	public int getNoOfInvoice() throws TelusAPIException {

		if (delegate.getNoOfInvoice() <= 0) {
			try {
				delegate.setBillParamsInfo(accountInformationHelper.retrieveBillParamsInfo(getBanId()));
			} catch (Throwable e) {
				provider.getExceptionHandler().handleException(e);
			}
		}

		return delegate.getNoOfInvoice();
	}

	@Override
	public boolean isBelongToHierarchy() {
		return delegate.isBelongToHierarchy();
	}

	@Override
	public void setNoOfInvoice(int noOfInvoice) {
		delegate.setNoOfInvoice(noOfInvoice);
	}

	/*
	 * Added by Sachin Sharma on 03-06-2010
	 * 
	 * Required payment for service restoral
	 *  
	 * Return required payment for ban restoral or unhotlined.
	 * For CLP client, if suspended due to CLM (reason code CLMS), return the required minimum payment.
	 * For other client, if suspended due to non-payment (SNP, SNP1) or hotlined, return past due balance.
	 * Otherwise return 0.  
	 */
	public double getRequiredPaymentForRestoral() throws TelusAPIException {

		try {
			//check if CLP client suspended due to CLM
			//Added the check for CLP client suspended due to SNP, SNP1 or is hotlined: return required minimum payment 
			//as  per the new requirement for IVR phase 3
			if (isSuspendedDueToCLM() || (isCLP() && ("SNP".equals(getStatusActivityReasonCode().trim()) || "SNP1".equals(getStatusActivityReasonCode().trim()) || isHotlined()))) {

				//return minimum payment
				return ((PostpaidConsumerAccount) this).getCLMSummary().getRequiredMinimumPayment();

			} else //check if other client and suspended due to SNP, SNP1 or hotlined
				if (!isSuspendedDueToCLM() && (isHotlined() || "SNP".equals(getStatusActivityReasonCode().trim()) || "SNP1".equals(getStatusActivityReasonCode().trim()))) {

				// return past due balance
				return getFinancialHistory().getDebtSummary().getPastDue();

			} else { //else return 0
				return 0;
			}
		} catch (TelusAPIException e) {
			throw new TelusAPIException(e);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	/*
	 * Added by Sachin Sharma on 03-06-2010
	 * 
	 * 1.For CLP client, if suspended due to CLM (reason code CLMS) and required
	 * minimum payment is less than or equal to the payment amount then restore
	 * account. 
	 * 2.For other client, if suspended due to non-payment (SNP, SNP1)
	 * or hotlined and payment amount is greater than or equal to the past due
	 * amount then restore account. 
	 * 3.For other client, if account is hotlined
	 * then remove hotline indicator. Else do nothing - this method should not
	 * be called if the required minimum payment amount for restoral is not met.
	 */
	void restoreSuspendedAccount(double paymentAmount) throws TelusAPIException {

		// validate account
		assertAccountExists();

		try {
			// restoreSuspendedAccount should not be called for Prepaid Account
			if (delegate.getAccountType() == ACCOUNT_TYPE_CONSUMER && delegate.getAccountSubType() == ACCOUNT_SUBTYPE_PCS_PREPAID) {
				throw new TelusAPIException("Can not use restoreSuspendedAccount on Prepaid Account");
			}

			// get past due balance
			double pastDueBalance = getFinancialHistory().getDebtSummary().getPastDue();
			//removed he payment amount check as per the new requirement for IVR phase 3
			Boolean isPostpaidBusinessConnect = Boolean.valueOf(isPostpaidBusinessConnect());
			if (isSuspendedDueToCLM()) {

				provider.getAccountManager0().restoreSuspendedAccount(getBanId(), new Date(), CLM_restoreAccountReasonCode,
						"Minimum payment was payed, Account is restored by CLM Rule " + provider.getApplication(), false, Integer.valueOf(delegate.getBrandId()), isPostpaidBusinessConnect);
				Logger.debug("Account restored successfully for CLP client");
			} else if (!isSuspendedDueToCLM()) {

				boolean suspendedDueToNonPayment = (getStatus() == AccountSummary.STATUS_SUSPENDED)
						&& ("SNP".equals(getStatusActivityReasonCode().trim()) || "SNP1".equals(getStatusActivityReasonCode().trim()));

				if (suspendedDueToNonPayment) {
					provider.getAccountManager0().restoreSuspendedAccount(getBanId(), new Date(), "PY", "Balance Paid using " + provider.getApplication(), true, Integer.valueOf(delegate.getBrandId()),
							isPostpaidBusinessConnect);
					Logger.debug("Account restored successfully for non-CLP client");
				}

				boolean oldHotlined = isHotlined();
				char oldStatus = getStatus();

				if (isHotlined()) {
					setHotlined(false);
					save();
					//Added SOP for IVR phase 3 changes

				} else if (suspendedDueToNonPayment) {
					// Since the above save would refresh the account,
					// we only need to refresh here.
					refresh();
				}

				boolean statusChanged = oldHotlined != isHotlined() || oldStatus != getStatus();

				if (statusChanged) {
					provider.getInteractionManager0().accountStatusChange(this, oldHotlined, oldStatus);
				}
			}
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	private static final int MAX_CHARGE_COUNT = 5000;

	@Override
	public Charge[] getCharges(String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws LimitExceededException, TelusAPIException {

		if (chargeCodes == null || chargeCodes.length <= 0)
			throw new IllegalArgumentException("chargeCodes cannot be empty or null");

		if (!(billState.equals(Account.BILL_STATE_UNBILLED) || billState.equals(Account.BILL_STATE_BILLED) || billState.equals(Account.BILL_STATE_ALL)))
			throw new IllegalArgumentException("billState=[" + billState + "] not a recognized value");

		if (!(level == ChargeType.CHARGE_LEVEL_ACCOUNT || level == ChargeType.CHARGE_LEVEL_SUBSCRIBER || level == ChargeType.CHARGE_LEVEL_ALL))
			throw new IllegalArgumentException("level=[" + level + "] not a recognized value.");

		if (level == ChargeType.CHARGE_LEVEL_SUBSCRIBER && (subscriberId == null || subscriberId.trim().length() == 0))
			throw new IllegalArgumentException("subscriberId cannot be empty or null");

		if (from == null || to == null)
			throw new IllegalArgumentException("from and/or to dates cannot be null");

		if (maximum > MAX_CHARGE_COUNT)
			throw new LimitExceededException("System limit reached, cannot request for more than <" + MAX_CHARGE_COUNT + "> records");

		if (maximum <= 0)
			return new Charge[0];
		Charge[] charges = null;
		try {
			//call the EJB to retrieve the charges
			ChargeInfo[] chargeInfos = accountInformationHelper.retrieveCharges(getBanId(), chargeCodes, billState, level, subscriberId, from, to, maximum);
			if (chargeInfos != null && chargeInfos.length > 0) {
				charges = decorate(chargeInfos);
			} else {
				charges = chargeInfos;
			}

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		return charges;
	}

	public double getUnpaidAirtimeTotal() throws TelusAPIException {
		double result = 0;
		try {
			result = accountInformationHelper.retrieveUnpaidAirTimeTotal(getBanId());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return result;
	}

	@Override
	public boolean getTransientNotificationSuppressionInd() {
		return provider.getAccountNotificationSuppressionIndicator(getBanId());
	}

	@Override
	public void setTransientNotificationSuppressionInd(boolean transientNotificaitonSuppressionInd) {
		provider.setAccountNotificationSuppressionIndicator(getBanId(), transientNotificaitonSuppressionInd);
	}

	public boolean isEligibleForCommunicationSuite() {
		if (communicationSuiteEligible == null) {
			try {
				communicationSuiteEligible = Boolean.valueOf(provider.getAccountLifecycleFacade().validateCommunicationSuiteEligibility(getBrandId(), this.getAccountType(), this.getAccountSubType()));
			} catch (Throwable e) {
				return Boolean.FALSE;
			}
		}
		
		return communicationSuiteEligible;
	}
	
}