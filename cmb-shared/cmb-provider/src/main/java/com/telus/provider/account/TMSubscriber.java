/*
 * $Id$ %E% %W% Copyright (c) TELUS Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.w3c.dom.Document;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.HistorySearchException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.InvalidPricePlanChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.InvalidServiceException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationTopUpPaymentArrangementException;
import com.telus.api.account.Address;
import com.telus.api.account.AddressNotFoundException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallList;
import com.telus.api.account.CallingCirclePhoneList;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractChangeHistory;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.account.EquipmentChangeRequest;
import com.telus.api.account.FeatureParameterHistory;
import com.telus.api.account.FollowUp;
import com.telus.api.account.HandsetChangeHistory;
import com.telus.api.account.IncompleteSubscriberCreationProcessException;
import com.telus.api.account.InvalidAirtimeRateException;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.InvalidSubscriberStatusException;
import com.telus.api.account.InvoiceTax;
import com.telus.api.account.Memo;
import com.telus.api.account.MigrateSeatRequest;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.NumberMatchException;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberInUseException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.PricePlanChangeHistory;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.ProvisioningTransaction;
import com.telus.api.account.QueueThresholdEvent;
import com.telus.api.account.ResourceChangeHistory;
import com.telus.api.account.SeatData;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberCommitment;
import com.telus.api.account.SubscriberHistory;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.account.SubscriptionPreference;
import com.telus.api.account.SubscriptionRole;
import com.telus.api.account.TaxExemption;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.account.UnsupportedEquipmentException;
import com.telus.api.account.UsageProfileListsSummary;
import com.telus.api.account.VendorServiceChangeHistory;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.account.VoiceUsageSummaryException;
import com.telus.api.account.WebUsageSummary;
import com.telus.api.dealer.CPMSDealer;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.CellularEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.EquipmentWarrantyNotAvailableException;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.DeactivateMVNESubscriberException;
import com.telus.api.portability.InterBrandPortRequestException;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortInEligibilityException;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortOutEligibilityException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.Brand;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.Feature;
import com.telus.api.reference.InvoiceCallSortOrderType;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.ReasonType;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.SeatType;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.reference.ShareablePricePlan;
import com.telus.api.reference.VendorService;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.EsimConstants;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.account.info.VoiceUsageServiceDirectionInfo;
import com.telus.eas.account.info.VoiceUsageServiceInfo;
import com.telus.eas.account.info.VoiceUsageServicePeriodInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.DeviceSwapValidateInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.CreateSubscriberException;
import com.telus.eas.framework.exception.PhoneNumberAlreadyInUseException;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.eas.task.info.SubscriberResumedPostTaskInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.LineRangeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.NumberRangeInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ProvisioningPlatformTypeInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMCard;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMMuleEquipment;
import com.telus.provider.equipment.TMPagerEquipment;
import com.telus.provider.equipment.TMUSIMCardEquipment;
import com.telus.provider.portability.TMPortRequest;
import com.telus.provider.portability.TMPortRequestSO;
import com.telus.provider.reference.TMPricePlan;
import com.telus.provider.reference.TMReferenceDataManager;
import com.telus.provider.reference.TMService;
import com.telus.provider.reference.TMServiceSummary;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.DOMTransformer;
import com.telus.provider.util.DateUtil;
import com.telus.provider.util.EquipmentWarranty;
import com.telus.provider.util.Logger;
import com.telus.provider.util.Mailer;
import com.telus.provider.util.ProviderDefaultExceptionTranslator;
import com.telus.provider.util.ProviderWNPExceptionTranslator;
import com.telus.provider.util.transition.ValidationResult;

public class TMSubscriber extends BaseProvider implements Subscriber {

	private static final long serialVersionUID = 1L;

	// for rounding usage summary minutes
	private static final String USAGE_MINUTE_ROUNDING_UP = "up";
	private static final String USAGE_MINUTE_ROUNDING_DOWN = "down";
	
	private static final String ACTIVATION_MEMO_TYPE = "ACTR";
	private static final String CLP_ACTIVATION_MEMO_TYPE = "CLMA";
	private static final String CLP_DEPOSIT_CHANGE_REASON_CODE = "14";
	private static final String BILLING_SYSTEM_FAILED = "BSTF";
	private static final String RELEASE_FROM_RESERVED_ACTIVATION = "RRA";
	private static final String CANCEL_AND_RESUBMIT_WPR = "RESB";

	/** @link aggregation */
	private final SubscriberInfo delegate;
	private SubscriberInfo oldDelegate;
	private transient TMAccountSummary accountSummary;
	protected TMContract contract;
	protected boolean waiveSearchFee;
	private boolean activation;
	private boolean isMigration;
	protected Equipment equipment;

	private CancellationPenalty cancellationPenalty;
	private boolean dealerHasDeposit;
	private String activationFeeChargeCode;
	private CPMSDealer dealer;
	private SubscriptionRole subscriptionRole;
	private String muleNumber;
	private String oldRole;
	private Address address;

	private CommunicationSuiteInfo commSuiteInfo;
	private boolean commSuiteCached = false;
	protected boolean iscommSuiteCTNChange = false;

	// 911 service fee variables
	private HashMap _911FeeServiceCodesByProvince = new HashMap();

	// Moved the SIM_IMEI_RESERVED static variable from the
	// EquipmentHelperEJBRemote
	// interface - R. Fong, 2004/04/27.
	private static final String SIM_IMEI_RESERVED = "RESVD";

	private transient DOMTransformer domTransformer;

	private transient Mailer mailer;

	// Porting member variables and references
	protected boolean portedIn = false;
	private transient TMPortRequestSO portRequestSO;
	private transient PortRequestInfo portRequest;  
	protected String portProcess = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
	private static final String POSTPAID = "O";
	private static final String PREPAID = "R";
	private boolean isDirty = false;

	private ContractFeature[] contractFeatures;
	String originalDealerCode;

	String originalSalesCode;
	boolean lastActiveStarterSeatInd = false;
	boolean lastSuspendedStarterSeatInd = false;
	int activeStarterseatCount = 0;
	int suspendedStarterSeatCount = 0;
	
	private CreditCheckResultDeposit[] creditCheckDeposits = null;
	
	public TMSubscriber(TMProvider provider, SubscriberInfo delegate, boolean activation, String activationFeeChargeCode, TMAccountSummary accountSummary, boolean dealerHasDeposit, Equipment equipment) {
		 this(provider, delegate, activation, activationFeeChargeCode, accountSummary, dealerHasDeposit);
		 this.equipment = equipment;
		 if (equipment != null) {
		  delegate.setEquipment(((TMEquipment) equipment).getDelegate());
		 }
		 if (equipment != null && equipment.isSIMCard()) {
		  this.muleNumber = ((SIMCardEquipment) equipment).getLastMuleIMEI();
		 }
		}
	public TMSubscriber(TMProvider provider, SubscriberInfo delegate,boolean activation, String activationFeeChargeCode,TMAccountSummary accountSummary, boolean dealerHasDeposit) {
		this(provider, delegate, activation, activationFeeChargeCode);
		this.accountSummary = accountSummary;
		this.dealerHasDeposit = dealerHasDeposit;
	}

	public TMSubscriber(TMProvider provider, SubscriberInfo delegate,boolean activation, String activationFeeChargeCode) {
		super(provider);
		this.delegate = delegate;
		this.activation = activation;
		this.activationFeeChargeCode = activationFeeChargeCode;
		init();
	}

	public TMSubscriber(TMProvider provider, TMSubscriber subscriber) throws TelusAPIException {
		super(provider);	// sets the existing provider into new object
		this.delegate = subscriber.delegate; // copies the data from delegate and TM
		this.activation = subscriber.activation;
		this.waiveSearchFee = subscriber.waiveSearchFee;
		this.isMigration = subscriber.isMigration;
		this.dealerHasDeposit = subscriber.dealerHasDeposit;
		this.activationFeeChargeCode = subscriber.activationFeeChargeCode;
		this.subscriptionRole = subscriber.subscriptionRole;
		this.contract = subscriber.contract;
		this.equipment = provider.getEquipmentManager().validateSerialNumber(subscriber.getSerialNumber());
		this.dealer = subscriber.dealer;
		getAccount0(); // refreshes the account object 
		this.delegate.setBrandId(getAccount().getBrandId()); // sets the brand from account
		init();
	}


	private void init() {
		domTransformer = new DOMTransformer();
		mailer = new Mailer();
	}

	public SubscriberInfo getDelegate() {
		return delegate;
	}

	public SubscriberInfo getOldDelegate() {
		return oldDelegate;
	}

	@Override
	public int getBanId() {
		return delegate.getBanId();
	}

	@Override
	public String getSubscriberId() {
		return delegate.getSubscriberId();
	}

	@Override
	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}

	/**
	 * @deprecated @see getConsumerName
	 */
	@Deprecated
	@Override
	public String getFirstName() {
		return getConsumerName().getFirstName();
	}

	/**
	 * TODO Figure out how to preserve Activations' setModified() requirement prior to deprecating this method.
	 * @see getConsumerName
	 */
	@Override
	public void setFirstName(String firstName) {
		setModified();
		getConsumerName().setFirstName(firstName);
	}

	/**
	 * @deprecated @see getConsumerName
	 */
	@Deprecated
	@Override
	public String getMiddleInitial() {
		return getConsumerName().getMiddleInitial();
	}

	/**
	 * TODO Figure out how to preserve Activations' setModified() requirement prior to deprecating this method.
	 *
	 * @see getConsumerName
	 */
	@Override
	public void setMiddleInitial(String middleInitial) {
		setModified();
		getConsumerName().setMiddleInitial(middleInitial);
	}

	/**
	 * @deprecated @see getConsumerName
	 */
	@Deprecated
	@Override
	public String getLastName() {
		return getConsumerName().getLastName();
	}

	/**
	 * TODO Figure out how to preserve Activations' setModified() requirement
	 * prior to deprecating this method.
	 *
	 * @see getConsumerName
	 */
	@Override
	public void setLastName(String lastName) {
		setModified();
		getConsumerName().setLastName(lastName);
	}

	@Override
	public ConsumerName getConsumerName() {
		return delegate.getConsumerName();
	}

	@Override
	public String getLanguage() {
		return delegate.getLanguage();
	}

	@Override
	public void setLanguage(String language) {
		setModified();
		delegate.setLanguage(language);
	}

	@Override
	public void setVoiceMailLanguage(String voiceMailLanguage) {
		setModified();
		delegate.setVoiceMailLanguage(voiceMailLanguage);
	}

	@Override
	public String getVoiceMailLanguage(){
		return delegate.getVoiceMailLanguage();
	}

	@Override
	public void setDealerHasDeposit(boolean dealerHasDeposit) {
		this.dealerHasDeposit = dealerHasDeposit;
	}

	@Override
	public boolean getDealerHasDeposit() {
		return this.dealerHasDeposit;
	}

	@Override
	public String getSerialNumber() {
		return delegate.getSerialNumber();
	}

	@Override
	public String[] getSecondarySerialNumbers() {
		return delegate.getSecondarySerialNumbers();
	}

	@Override
	public String getUserValueRating() {
		return delegate.getUserValueRating();
	}

	/**
	 * Returns the subscriber's brand ID.
	 * If this is a new subscriber, brand ID will be zero (invalid).  In this case the brand ID will be based on the price plan, 
	 * and if the price plan info is not available, it will return the brand indicator at the BAN level.
	 * @return subscriber's brand ID - int
	 */
	@Override
	public int getBrandId() {
		try {
			if (!(ReferenceDataManager.Helper.validateBrandId(delegate.getBrandId(), provider.getReferenceDataManager().getBrands()))) {
				String pricePlan = getPricePlan();
				if (pricePlan != null && "".equals(pricePlan.trim()) == false) {
					PricePlanSummary pricePlanSummary = provider.getReferenceDataManager().getPricePlan(pricePlan);
					return (pricePlanSummary != null) ? pricePlanSummary.getBrandId() : getAccount().getBrandId();
				} else {
					return getAccount().getBrandId();
				}
			}
		} catch (Throwable t) {
			logFailure("getBrandId", "retrieving price plan or account brand on new subscriber", t, "error retrieving price plan or account brand ID");
		}
		return delegate.getBrandId();
	}

	@Override
	public char getStatus() {
		return delegate.getStatus();
	}

	@Override
	public String getMarketProvince() {
		return delegate.getMarketProvince();
	}

	@Override
	public String getProductType() {
		return delegate.getProductType();
	}

	@Override
	public String getPricePlan() {
		return delegate.getPricePlan();
	}

	@Override
	public String getDealerCode() {
		return delegate.getDealerCode();
	}

	@Override
	public void setDealerCode(String dealerCode) {
		delegate.setDealerCode(dealerCode);
	}

	@Override
	public String getSalesRepId() {
		return delegate.getSalesRepId();
	}

	@Override
	public void setSalesRepId(String salesRepId) {
		delegate.setSalesRepId(salesRepId);
	}

	/**
	 * This method returns the birthdate set in free-birthday-calling (FBC feature) only.
	 * If the subscriber doesn't have this feature or doesn't have the value set, it will return null.
	 * Since the FBC parameter-value doesn't require a year, the year returned from this method is always set to 2001.
	 * 
	 * @return birthdate set in FBC . DO NOT USE YEAR. It is always set to 2001.
	 */
	@Override
	public Date getBirthDate() {
		if (!activation && delegate.getBirthDate() == null && !isDirty) {
			try {
				if (getContract0(false) != null) {
					ContractFeature[] features = getContract0(false).getFeatures(true);
					for (int i = 0; i < (features != null ? features.length : 0); i++) {
						if ("FBC".equals(features[i].getCode().trim()) && features[i].getParameter() != null) {
							int index = features[i].getParameter().indexOf("=");
							String paramName = features[i].getParameter().substring(0, index);
							String paramValue = features[i].getParameter().substring(index + 1, features[i].getParameter().length()-1);
							if ("date-of-birth".equals(paramName.toLowerCase())) {
								Date birthdate = formatFBCBirthdate (paramValue);
								setBirthDate(birthdate);
							}
						}
					}
				}
			} catch (Exception e) {
				log("Exception in TMSubscriber.getBirthdate(): "+e);
			}
		}
		return delegate.getBirthDate();
	}


	@Override
	public void setBirthDate(Date birthDate) {
		setModified();
		delegate.setBirthDate(birthDate);
	}

	@Override
	public String getEmailAddress() {
		return delegate.getEmailAddress();
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		setModified();
		delegate.setEmailAddress(emailAddress);
	}

	@Override
	public String getFaxNumber() {
		String faxNumber = delegate.getFaxNumber();
		try {
			if ( (faxNumber == null || faxNumber.equals("")) && isIDEN()) {
				ContractFeature[] contractFeatures = getContract().getFeatures(
						true);
				for (int i = 0; i < contractFeatures.length; i++) {
					if (contractFeatures[i].getFeature().getCode().trim()
							.equals("MFAXM")) {
						faxNumber = contractFeatures[i].getAdditionalNumber();
						break;
					}
				}
			}
		}
		catch (TelusAPIException e) {
			log("Could not retrieve faxNumber from contract for IDEN subscriber");
		}
		log("s.faxNumber=["+ (faxNumber != null ? "null" : faxNumber) + "]");
		return faxNumber;
	}

	@Override
	public Date getCreateDate() {
		return delegate.getCreateDate();
	}

	@Override
	public Date getStartServiceDate() {
		return delegate.getStartServiceDate();
	}

	@Override
	public void setActivityReasonCode(String activityReasonCode) {
		delegate.setActivityReasonCode(activityReasonCode);
	}

	@Override
	public String getActivityCode() {
		return delegate.getActivityCode();
	}

	@Override
	public String getActivityReasonCode() {
		return delegate.getActivityReasonCode();
	}

	@Override
	public boolean isIDEN() {
		return delegate.isIDEN();
	}

	@Override
	public boolean isPCS() {
		return delegate.isPCS();
	}

	@Override
	public boolean isPager() {
		return delegate.isPager();
	}

	@Override
	public boolean isTango() {
		return delegate.isTango();
	}

	@Override
	public boolean isCDPD() {
		return delegate.isCDPD();
	}

	@Override
	public Date getStatusDate() {
		return delegate.getStatusDate();
	}

	@Override
	public SubscriberCommitment getCommitment() {
		return delegate.getCommitment();
	}

	public void setStatusDate(Date value) {
		delegate.setStatusDate(value);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------
	public void clear() {
		delegate.clear();
		oldDelegate = null;
		//accountSummary;
		contract = null;
		waiveSearchFee = false;
		activation = true;
		cancellationPenalty = null;
		//equipment;
		//dealerHasDeposit;
		//activationFeeChargeCode;
		portedIn = false;
		portProcess = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
	}

	public final void setModified() {
		if (!activation && oldDelegate == null) {
			oldDelegate = (SubscriberInfo) delegate.clone();
		}
	}

	public final void assertSubscriberExists() throws UnknownSubscriberException {
		if (activation) {
			throw new UnknownSubscriberException(
			"This Subscriber has not yet been created");
		}
	}

	public final void assertSubscriberKnown() throws UnknownSubscriberException {
		if (delegate.getSubscriberId() == null) {
			throw new UnknownSubscriberException(
			"This Subscriber has no Id - please call reservePhoneNumber()");
		}
	}

	public boolean isActivation() {
		return activation;
	}

	public TMAccountSummary getAccountSummary() {
		return accountSummary;
	}

	public void setAccountSummary(TMAccountSummary accountSummary) {
		this.accountSummary = accountSummary;
	}

	public TMAccount getAccount0() throws TelusAPIException {
		if (accountSummary == null) {
			accountSummary = (TMAccountSummary)provider.getAccountManager0().findAccountByBAN0(getBanId());
		}
		return accountSummary.getAccount0();
	}

	@Override
	public Account getAccount() throws TelusAPIException {
		return getAccount0();
	}

	@Override
	public void save() throws TelusAPIException {
		save(false);
	}

	@Override
	public void save(Date startServiceDate) throws TelusAPIException {
		if (activation && startServiceDate != null && startServiceDate.compareTo(provider.getReferenceDataManager().getSystemDate()) > 0) {
			delegate.setStartServiceDate(startServiceDate);
		}
		save(true);
	}

	@Override
	public void save(boolean activate) throws TelusAPIException, ActivationTopUpPaymentArrangementException {
		ServicesValidationInfo srvValidation = new ServicesValidationInfo();
		save(activate, srvValidation);
	}

	@Override
	public void refresh() throws TelusAPIException {
		assertSubscriberExists();
		// Preserve
		//---------------------------------
		Date birthDate = delegate.getBirthDate();
		String language = delegate.getLanguage();
		TMSubscriber s = (TMSubscriber) getAccount().getSubscriberByPhoneNumber(getPhoneNumber());
		delegate.copyFrom(s.delegate);
		delegate.setBirthDate(birthDate);
		delegate.setLanguage(language);

		if (!isPager()){
			this.equipment = null; // force equipment to be refreshed.
		}
		cancellationPenalty = null;
		portRequest = null;
		clearCachedCommSuite();
	}

	@Override
	public void activate() throws TelusAPIException {
		activate(delegate.getActivityReasonCode(), null, null);
	}

	@Override
	public void activate(Date startServiceDate) throws TelusAPIException {
		activate(delegate.getActivityReasonCode(), startServiceDate, null);
	}

	@Override
	public void activate(String reason) throws TelusAPIException {
		activate(reason, null, null);
	}

	@Override
	public void activate(String reason, String memoText) throws TelusAPIException {
		activate(reason, null, memoText);
	}

	@Override
	public void activate(String reason, Date startServiceDate) throws
	TelusAPIException {
		activate(reason, startServiceDate, null);
	}

	@Override
	public void activate(String reason, Date startServiceDate, String memoText) throws TelusAPIException {
		ServicesValidationInfo srvValidation = new ServicesValidationInfo();
		activate(reason, startServiceDate, memoText, srvValidation);
	}

	@Override
	public void unreserve() throws TelusAPIException {
		unreserve(false);
	}

	@Override
	public void reservePhoneNumber(PhoneNumberReservation reservation) throws TelusAPIException, NumberMatchException {
		try {
			localReservePhoneNumber(reservation, false);
		} catch (PhoneNumberInUseException e) {
			localReservePhoneNumber(reservation, false);
		} catch (PortInEligibilityException e) {
			String oldPhoneNumberPattern = reservation.getPhoneNumberPattern();
			boolean oldLikeMatch = reservation.isLikeMatch();
			try {
				AvailablePhoneNumber[] nums = findAvailablePhoneNumbers(reservation, 10);
				if (nums != null && nums.length > 0) {
					for (int i = 0; i < nums.length; i++) {
						if (nums[i].getPhoneNumber().equals(e.getPhoneNumber()) == false) {
							reservation.setPhoneNumberPattern(nums[i].getPhoneNumber());
							reservation.setLikeMatch(false);
							break;
						}
					}
				}
			} catch (TelusAPIException tapi) {
				log("reservePhoneNumber error in calling findAvailablePhoneNumbers" + tapi);
			}

			try {
				localReservePhoneNumber(reservation, true);
			} catch (PortRequestException pre) {
				throw new NumberMatchException (pre, reservation);
			} finally {
				//restore state to offset side-effects
				reservation.setPhoneNumberPattern(oldPhoneNumberPattern);
				reservation.setLikeMatch(oldLikeMatch);
			}
		}
	}

	private void saveDealer() throws TelusAPIException {
		originalDealerCode = getDealerCode();
		originalSalesCode = getSalesRepId();
	}

	private void restoreDealer() throws TelusAPIException {
		setDealerCode(originalDealerCode);
		setSalesRepId(originalSalesCode);
		originalDealerCode = null;
		originalSalesCode = null;
	}

	private void setupNumberGroupDealer(NumberGroup numberGroup) throws TelusAPIException {
		if (!getAccount().isPostpaid() && TMReferenceDataManager.NUMBER_LOCATION_POSTPAID.equals(numberGroup.getNumberLocation())) {
			setDealerCode(numberGroup.getDefaultDealerCode());
			setSalesRepId(numberGroup.getDefaultSalesCode());
		}
	}

	public void localReservePhoneNumber(PhoneNumberReservation reservation, boolean retry) throws TelusAPIException, NumberMatchException, PhoneNumberInUseException {
		String portVisibilityType;
		boolean doEligibilityCheck = (reservation.getPhoneNumberPattern().indexOf("*") >= 0 ||    //perform eligibility check when reserving 
				reservation.getPhoneNumberPattern().indexOf("%") >= 0 ||  //phone pattern contains wildcard (phone # is unknown until assigned
				reservation.getPhoneNumberPattern().length() < 10) || 
				retry ||   //on retry attempt from eligibility failure, the next avaialble # is picked and needs to be tested for eligibility  
				reservation.isLikeMatch(); //on like match reservation, the # to be reserved is also unknown until reservation time                

		saveDealer();
		try {
			setupNumberGroupDealer(reservation.getNumberGroup());
			if (!activation) {
				throw new TelusAPIException("Subscriber already saved with number: "+ getPhoneNumber());
			}
			waiveSearchFee = reservation.getWaiveSearchFee();
			reservation.setProductType(getProductType());
			try {

				SubscriberInfo info;
				if (reservation.isLikeMatch()) {
					info = provider.getSubscriberManagerBean().reserveLikePhoneNumber(delegate, ((TMPhoneNumberReservation) reservation).getPhonenumberReservation0());
				} else {
					info = provider.getSubscriberManagerBean().reservePhoneNumber(delegate,((TMPhoneNumberReservation) reservation).getPhonenumberReservation0());
				}

				info.setMarketProvince(reservation.getNumberGroup().getProvinceCode());
				info.setNumberGroup(reservation.getNumberGroup());
				SubscriberInfo oldDelegate = new SubscriberInfo();
				oldDelegate.copyFrom(delegate);
				delegate.copyFrom(info);
				//delegate.setStatus(Subscriber.STATUS_RESERVED); //set subscriber to reserved status - added for May 2010 BEAST project
				provider.registerNewSubscriber(delegate);
	

				if (doEligibilityCheck && !getEquipment().isPager()) {
					if (getEquipment().isCDMA()) {
						portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2C;
					} else if (getEquipment().isHSPA()) {
						portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2H;
					} else {
						portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2I;
					}

					try {
						PortInEligibility portEligibility = provider.getPortRequestManager().testPortInEligibility(delegate.getPhoneNumber(), portVisibilityType, getAccount().getBrandId());
						log("localReservePhoneNumber eligibility check PASSED on " + delegate.getPhoneNumber() + ". VisibilityType=" + portVisibilityType);
					} catch (PortRequestException pre) {
						log(pre + "localReservePhoneNumber eligibility check FAILED on " + delegate.getPhoneNumber() + ". VisibilityType=" + portVisibilityType + ". Now calling releaseSubscriber.");
						provider.getSubscriberManagerBean().releaseSubscriber(delegate);
						provider.unregisterNewSubscriber(delegate);
						delegate.copyFrom(oldDelegate); //restore delegate state
						throw pre;
					} catch (TelusAPIException tapie) {
						log(tapie.getMessage());
						throw tapie;
					}

				}			
			} catch (Throwable e) {
				final PhoneNumberReservation prn = reservation;

				provider.getExceptionHandler().handleException(e, new ProviderDefaultExceptionTranslator() {

					@Override
					public TelusAPIException translateException(Throwable throwable) {
						if (throwable instanceof PhoneNumberAlreadyInUseException) {
							return new PhoneNumberInUseException(throwable);
						}
						return super.translateException(throwable);
					}

					@Override
					protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
						if ("APP20001".equals(errorId) || "1110560".equals(errorId)) {
							return new NumberMatchException(cause, prn);
						}
						return new TelusAPIException(cause);
					} 

				});
			}
		} finally {
			restoreDealer();
		}

	}

	public void setContract(TMContract contract) {
		this.contract = contract;
		delegate.setPricePlan(contract.getPricePlan().getCode().trim());
	}

	@Override
	public Contract getContract() throws TelusAPIException {
		return getContract0();
	}

	public TMContract getContract0() throws TelusAPIException {
		return getContract0(false);
	}

	public TMContract getContract0(boolean forceRetrieval) throws
	TelusAPIException {
		return getContract0(forceRetrieval, true);
	}

	public TMContract getContract0(boolean forceRetrieval, boolean attachToSubscriber) throws TelusAPIException {
		try {
			if (forceRetrieval || contract == null) {
				assertSubscriberExists();
				if (this.getStatus() == Subscriber.STATUS_CANCELED) {
					throw new InvalidSubscriberStatusException("Cannot getContract on cancelled subscriber.");
				}

				if (getPhoneNumber() == null || "".equals(getPhoneNumber().trim())) {
					throw new UnknownSubscriberException ("Cannot getContract with a null or empty phone number.");
				}
				
				SubscriberContractInfo info = null;
				
				if (getSubscriberId() != null && "".equals(getSubscriberId().trim()) == false) {
					//this should be the case always and is faster than retrieveServiceAgreementByPhoneNumber
					info = provider.getSubscriberLifecycleHelper().retrieveServiceAgreementBySubscriberId(getSubscriberId()); 
				}else {
					info = provider.getSubscriberLifecycleHelper().retrieveServiceAgreementByPhoneNumber(getPhoneNumber());
				}
				info.setCommitmentReasonCode(getCommitment().getReasonCode());
				info.setCommitmentMonths(getCommitment().getMonths());
				info.setCommitmentStartDate(getCommitment().getStartDate());
				info.setCommitmentEndDate(getCommitment().getEndDate());

				Equipment equipment = getEquipment();
				// Populate Contract, ContractService, & ContractFeature
				//-------------------------------------------------------------
				Account account = getAccount();

				TMPricePlan p = (TMPricePlan) provider
				.getReferenceDataManager().getPricePlan(getPricePlan(),provider.getEquipmentManager0().translateEquipmentType(equipment),getProvince(), account.getAccountType(),
						account.getAccountSubType(), getBrandId());
				info.setPricePlanInfo(p.getDelegate0());
				//provider.getReferenceDataManager().getAccountType(getAccount()).getSocGroup());
				// Attach priceplan services & features to Contract services &
				// features.
				//------------------------------------------------------------------------------
				ServiceAgreementInfo[] services = info.getServices0(true);
				for (int i = 0; i < services.length; i++) {
					ServiceAgreementInfo s = services[i];
					ServiceInfo service = info.getPricePlan0().getService0(
							s.getServiceCode());
					if (service == null) {
						// Add non-priceplan SOC.
						TMService serviceProvider = (TMService) provider.getReferenceDataManager().getRegularService(s.getServiceCode());
						if (serviceProvider == null) {
							throw new TelusAPIException ("SOC ["+s.getServiceCode()+"] does not exist in the system.");
						}
						service = serviceProvider.getDelegate();
					}

					s.setService(service);

					ServiceFeatureInfo[] features = s.getFeatures0(true);
					for (int j = 0; j < features.length; j++) {
						ServiceFeatureInfo f = features[j];
						f.setFeature(service.getFeature0(f.getFeatureCode()));
					}
				}
				// Attach PricePlan features to Contract features.
				//------------------------------------------------------------------------------
				ServiceFeatureInfo[] features = info.getFeatures0(true);
				for (int j = 0; j < features.length; j++) {
					ServiceFeatureInfo f = features[j];
					f.setFeature(info.getPricePlan0().getFeature0(
							f.getFeatureCode()));
				}
				// Retrieve WPS services for prepaid account.
				//------------------------------------------------------------------------------
				if (!account.isPostpaid()) {
					ServiceAgreementInfo[] wpsServices = null;

					try {
						wpsServices = provider.getSubscriberLifecycleHelper().retrieveFeaturesForPrepaidSubscriber(getPhoneNumber());
					}
					catch (Exception e) {
						log("Failed to retrieve services for Prepaid Subscriber "+ getSubscriberId());
					}

					int wpsServicesNo = wpsServices != null ? wpsServices.length: 0;
					for (int i = 0; i < wpsServicesNo; i++) {
						ServiceAgreementInfo sa = wpsServices[i];
						TMService serviceProvider = (TMService) provider
						.getReferenceDataManager().getWPSService(sa.getServiceCode());
						if (serviceProvider == null) {
							throw new TelusAPIException("Couldn't find Prepaid service for code: ["+ sa.getServiceCode() + "]");
						}
						sa.setService(serviceProvider.getDelegate());
						info.addService(sa);
						if (serviceProvider.getFeatures() != null){
							ServiceFeatureInfo[] wpsFeatures = sa.getFeatures0(true);
							for (int j = 0; j < wpsFeatures.length; j++) {
								ServiceFeatureInfo wpsFtr = wpsFeatures[j];
								RatedFeatureInfo wpsRatedFtr = serviceProvider.getDelegate().getFeature0(wpsFtr.getFeatureCode().trim());
								wpsFtr.setFeature(wpsRatedFtr);
							}
						}
						sa.setTransaction(BaseAgreementInfo.NO_CHG);
						/*The corresponding  call to Prepaid API ,getPromotionBucketDetail is Deprecated.Hence commenting out.
						 
						 * if (ServiceSummary.WPS_SERVICE_TYPE_PROMOTIONAL.equals(serviceProvider.getWPSServiceType())) {

							PrepaidPromotionDetailInfo prepaidPromotionDetailInfo = provider.getSubscriberLifecycleHelper()
							.retrievePrepaidSubscriberPromotion(getPhoneNumber(), 
									Integer.parseInt(serviceProvider.getCode().trim()));
							sa.setPrepaidPromotionDetail(prepaidPromotionDetailInfo);
						}*/
					}
				}
				
				info.doPostLoadProcess();
				TMContract c = new TMContract(provider, info, p, this, false,false, false, TERM_PRESERVE_COMMITMENT, null);
				c.getDelegate().getCommitment().setModified(false);
				delegate.setMultiRingPhoneNumbers(c.retrieveMultiRingPhoneNumbers());

				if (attachToSubscriber) {
					contract = c;
				}
				else {
					return c;
				}
			}

			return contract;
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	
	public void setEquipment(TMEquipment equipment) {
		this.equipment = equipment;
		delegate.setSerialNumber(equipment.getSerialNumber());
	}

	public TMEquipment getEquipment0() throws UnknownSerialNumberException,
	TelusAPIException {
		if (equipment == null) {
			equipment = getEquipment0(getSerialNumber());
			delegate.setEquipment( ( (TMEquipment) equipment).getDelegate());
		}

		return (TMEquipment) equipment;
	}

	public TMEquipment getEquipment0(String serialNumber) throws UnknownSerialNumberException, TelusAPIException {
		if (isTango()) {
			EquipmentInfo equipmentInfo = new EquipmentInfo();
			equipmentInfo.setSerialNumber(serialNumber);
			equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_TANGO);
			equipmentInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_TANGO);
			return new TMEquipment(provider, equipmentInfo);
		}

		if (isCDPD()) {
			try {
				// Although this is a different product, this method will still populate the required field equipmentType.
				EquipmentInfo equipmentInfo = provider.getProductEquipmentHelper().retrievePagerEquipmentInfo(serialNumber);
				equipmentInfo.setSerialNumber(serialNumber);
				equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_CDPD);
				return new TMEquipment(provider, equipmentInfo);
			}catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
		}

		if (isPager()) {
			try {
				EquipmentInfo equipmentInfo = provider.getProductEquipmentHelper().retrievePagerEquipmentInfo(serialNumber);
				equipmentInfo.setFormattedCapCode(serialNumber);

				TMPagerEquipment pagerEquipment = null;

				if (equipmentInfo.getEncodingFormat().equals(PagerEquipment.ENCODING_FORMAT_NETWORK)) {
					pagerEquipment = new TMPagerEquipment(provider, equipmentInfo);
				}
				else {
					try {
						String rawCapCode = EquipmentManager.Helper.getUnformattedCapCode(serialNumber, equipmentInfo.getEncodingFormat());
						pagerEquipment = (TMPagerEquipment) provider.getEquipmentManager().
						getEquipmentByCapCode(rawCapCode,equipmentInfo.getEncodingFormat());
						pagerEquipment.getDelegate().setCurrentCoverageRegionCode(equipmentInfo.getCurrentCoverageRegionCode());
						pagerEquipment.getDelegate().setFormattedCapCode(equipmentInfo.getFormattedCapCode());
						pagerEquipment.getDelegate().setPossession(equipmentInfo.getPossession());
						pagerEquipment.getDelegate().setEquipmentType(equipmentInfo.getEquipmentType());
					}
					catch (Exception e) {
						pagerEquipment = new TMPagerEquipment(provider, equipmentInfo);
					}
				}

				return pagerEquipment;
			}catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
		}

		
		return (TMEquipment) provider.getEquipmentManager().getEquipment(
				serialNumber);
	}

	@Override
	public Equipment getEquipment() throws UnknownSerialNumberException, TelusAPIException {
		return getEquipment0();
	}

	public void testService(ServiceSummary service, Equipment equipment) throws
	InvalidServiceException, TelusAPIException {

		// --------------------------------------------------------
		//Network and/or equipment type check
		// --------------------------------------------------------

		// defect PROD00142510fix
		// Check network compatibility on Optional Service (KB term: Regular SOC) only.
		// we only want to stop adding optional service that is not network compatible up front
		// for included service, let it go. we will remove it afterwards : before the contract get saved 
		if ( "R".equalsIgnoreCase(service.getServiceType())
				//PROD00143139 fix, only validate Price plan for non-HSPA
				|| ("P".equalsIgnoreCase(service.getServiceType()) && equipment.isHSPA() == false)) { 
			int isNetworkOK = 0;

			if (service instanceof ServiceInfo) {
				isNetworkOK = ((ServiceInfo) service).testNetworkEquipmentTypeCompatibility(equipment);
			} else {
				isNetworkOK = ((TMServiceSummary) service).getDelegate().testNetworkEquipmentTypeCompatibility(equipment);
			}

			if (isNetworkOK > 0) {
				throw new InvalidServiceException(isNetworkOK, "service: " + service.getCode() + " - incompatible with equipment["+equipment.getSerialNumber()
						+ "]'s equipment/network type[" + equipment.getEquipmentType() + "/" + equipment.getNetworkType()+"]", service);
			}
		}

		// --------------------------------------------------------
		///productType check: 
		// -- postpaid SOC, compare with SOC's productType with Subscriber
		// -- prepaid SOC, make sure the Account is not postpaid 
		// --------------------------------------------------------
		if (service.isWPS()) {
			if (getAccount().isPostpaid()) {
				throw new InvalidServiceException(InvalidServiceException.BILLING_TYPE_MISMATCH, "service: " + service.getCode() + " - Prepaid soc incompatible with postpaid account.", service);
			}
		} else {
			//only for postpaid service we check productType: because prepaid service does not have product type 
			if (!service.getProductType().equals(getProductType())) {
				throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " - incompatible with proudctType[" + getProductType() + "]", service);
			}
		}

		if (equipment.isHSPA() == false) {
			// refactoring: 
			//  1) productType check is moved to above
			//  2) equipment type check is moved to above; add one more scenario for calling  ReferenceDataManager.Helper.isNetworkCompatible(service, equipment);
			/*
			if (!service.isWPS()) {
				if (!service.getProductType().equals(getProductType())) {
					throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " - incompatible with proudctType[" + getProductType() + "]");
				}
				final String EQUIPMENT_TYPE_ANY = "9";
				boolean isEquipTypeOK = false;
				String[] equipTypes = service.getEquipmentTypes(equipment.getNetworkType());
				for (int i = 0; i < equipTypes.length; i++) {
					if (equipTypes[i].equals(EQUIPMENT_TYPE_ANY) || 
						equipTypes[i].equals(String.valueOf(equipment.getEquipmentType()))) {
						isEquipTypeOK = true;
					}
				}
				if (!isEquipTypeOK) {
					throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " - incompatible with equipmentNeworkType[" + equipment.getEquipmentType()+ "/" +equipment.getNetworkType()+"]");
				}
			}
			 */

			if (service.getService().isRIM()) {
				if (equipment.isRIM()) {
					; // OK
				} else if (equipment.isSIMCard()) {
					MuleEquipment mule = ((SIMCardEquipment) equipment).getLastMule();
					if (mule != null && mule.isIDENRIM()) {
						; // OK
					} else {
						throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " RIM service incompatible with non RIM equipment", service);
					}
				} else {
					String[] equipmentTypes= service.getService().getEquipmentTypes(equipment.getNetworkType());

					if (equipmentTypes!= null){
						boolean isEquipmentTypeMatch = false;
						for (int i=0; i<equipmentTypes.length;i++){
							if(equipmentTypes[i].equals(equipment.getEquipmentType().trim())){
								isEquipmentTypeMatch = true ;
							}
						}
						if (!isEquipmentTypeMatch){
							throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " RIM service incompatible with non RIM equipment", service); 
						}
					}
				}
			}

			// --------------------------------------------------------
			// PDA Device - Cellular Digital Equipment
			// --------------------------------------------------------
			if (service.getService().isPDA()) {
				if (equipment.isCellularDigital() && ((CellularDigitalEquipment) equipment).isPDA()) {
					; // OK
				} else {
					String[] equipmentTypes= service.getService().getEquipmentTypes(equipment.getNetworkType());

					if (equipmentTypes!= null) {
						boolean isEquipmentTypeMatch = false;
						for (int i = 0; i < equipmentTypes.length; i++) {
							if (equipmentTypes[i].equals(equipment.getEquipmentType().trim())) {
								isEquipmentTypeMatch = true ;
							}
						}
						if (!isEquipmentTypeMatch) {
							throw new InvalidServiceException(InvalidServiceException.TECHNOLOGY_MISMATCH, "service: " + service.getCode() + " PDA service incompatible with non PDA equipment", service); 
						}
					}
				}
			}

		}
	}

	// ===================================================================================
	// Activations and Priceplan changes
	//===================================================================================

	/**
	 * Throws exception if there is any validation errors on a planned new contract change
	 * 
	 * @param pricePlan
	 * @param dispatchOnly
	 * @param equipment
	 * @param contractRenewal
	 * @param migrateToAccount - if null, uses the account of the current subscriber
	 * @throws InvalidPricePlanChangeException
	 * @throws TelusAPIException
	 */
	private void testNewContract(PricePlan pricePlan, boolean dispatchOnly,
			Equipment equipment, boolean contractRenewal, TMAccount migrateToAccount) throws
			InvalidPricePlanChangeException, TelusAPIException {

		if (contractRenewal) {
			assertSubscriberExists(); //TODO: enumerate as
			// InvalidPricePlanChangeException
		}
		
		AccountInfo accountToCheck;
		if (migrateToAccount == null) 
			accountToCheck = getAccount0().getDelegate0();
		else
			accountToCheck = migrateToAccount.getDelegate0();		
		checkFamilyType(pricePlan, accountToCheck);

		boolean priceplanChange = !activation
		&& ! (contractRenewal && getPricePlan().equals(pricePlan.getCode()));

		try {
			testService(pricePlan, equipment);
		}
		catch (InvalidServiceException e) {
			throw new InvalidPricePlanChangeException(e.getReason(), e);
		}
		if (!contractRenewal && priceplanChange
				&& getPricePlan().equals(pricePlan.getCode())) {
			throw new InvalidPricePlanChangeException(
					InvalidPricePlanChangeException.DUPLICATE_PRICEPLAN,"new priceplan is same as old: " + pricePlan.getCode());
		}


		if (pricePlan.isSharable()) {
			ShareablePricePlan sp = (ShareablePricePlan) pricePlan;
			if (sp.getMaximumSubscriberCount() > 0) {
				PricePlanSubscriberCount count = accountSummary.getAccount0().getShareablePricePlanSubscriberCount(sp);

				if (count != null) {
					if (count.isMaximumSubscriberReached()) {
						throw new InvalidPricePlanChangeException(InvalidPricePlanChangeException.SHARED_PLAN_LIMIT,pricePlan.getCode());
					}

					if (count.getActiveSubscribers().length + count.getReservedSubscribers().length > 0) {
						String[] phoneNumber = new String[count.getActiveSubscribers().length + count.getReservedSubscribers().length];
						System.arraycopy(count.getActiveSubscribers(), 0, phoneNumber, 0, count.getActiveSubscribers().length);
						System.arraycopy(count.getReservedSubscribers(), 0, phoneNumber, count.getActiveSubscribers().length,
								count.getReservedSubscribers().length);

						for (int i = 0; i < phoneNumber.length; i++) {
							Subscriber subscriber = provider.getAccountManager0().findSubscriberByPhoneNumber(phoneNumber[i]);
							// TODO: I can't imagine there being 2 identical
							// PricePlan codes for different terms.
							if (subscriber.getContract().getPricePlan().getTermMonths() != sp.getTermMonths()) {
								throw new InvalidPricePlanChangeException(
										InvalidPricePlanChangeException.SHARED_PLAN_TERM_MISMATCH,
										pricePlan.getCode());
							}
						}
					}
				}

			}
		}
	}

	public void testNewContract(PricePlan pricePlan, Equipment equipment, boolean contractRenewal) throws InvalidPricePlanChangeException,TelusAPIException {
		testNewContract(pricePlan, false, equipment, contractRenewal, null);
	}

	private void checkFamilyType(PricePlan pricePlan, AccountInfo account) throws InvalidPricePlanChangeException, TelusAPIException{
		
		PricePlanInfo ppInfo;
		if (pricePlan instanceof PricePlanInfo)
			ppInfo = (PricePlanInfo) pricePlan;
		else if (pricePlan instanceof TMPricePlan)
			ppInfo = ((TMPricePlan) pricePlan).getDelegate0();
		else {
			String pricePlanClassname = pricePlan.getClass().getName();
			throw new SystemException(SystemCodes.PROVIDER, ErrorCodes.GENERIC_THROWABLE_ERROR_CODE, 
					"Client API Coding Error: expecting pricePlan input parameter to be an instance of either PricePlanInfo or TMPricePlan but instead is: " + pricePlanClassname, 
					"API Client erreur de codage: attendons paramètre d'entrée pricePlan être soit une instance de PricePlanInfo ou TMPricePlan mais est plutôt: " + pricePlanClassname);
		}
		
		String[]  familyTypes= pricePlan.getFamilyTypes();
		for(int j = 0; j < familyTypes.length; j++) { 
			if(familyTypes[j].equals(ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE)){
				try{
						provider.getSubscriberLifecycleFacade().testServiceAddToBusinessAnywhereAccount(account, ppInfo);
				}catch(ApplicationException e ){
					if (e.getErrorCode().equals(ErrorCodes.ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT))
						throw new InvalidPricePlanChangeException(InvalidPricePlanChangeException.ACCOUNT_TYPE_SUBTYPE_MISMATCH, e);
					else
						provider.getExceptionHandler().handleException(e);
				}
			}
		}
	}
	
	/**
	 * Creates a TMContract instance from the input parameters
	 * 
	 * @param pricePlan
	 * @param term
	 * @param dispatchOnly
	 * @param equipmentChangeRequest
	 * @param contractRenewal
	 * @param migrateToAccount - if null, uses the account of the current subscriber. This
	 * parameter is used for validation purposes on the current account or the 
	 * account subscriber will be migrated to.
	 * @return
	 * @throws InvalidPricePlanChangeException
	 * @throws TelusAPIException
	 */
	protected TMContract newContract0(PricePlan pricePlan, int term, boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest, boolean contractRenewal, TMAccount migrateToAccount) 
			throws InvalidPricePlanChangeException, TelusAPIException {

		Equipment equipment = (equipmentChangeRequest != null) ? equipmentChangeRequest.getNewEquipment() : getEquipment();				
				
		testNewContract(pricePlan, dispatchOnly, equipment, contractRenewal, migrateToAccount);
		boolean priceplanChange = !activation && !(contractRenewal && getPricePlan().equals(pricePlan.getCode()));

		SubscriberContractInfo info;
		if (contractRenewal && !priceplanChange) {
			info = getContract0(true, false).getDelegate();
		} else {
			info = new SubscriberContractInfo();
			info.setModified();
			info.setDispatchOnly(dispatchOnly);
		}

		TMContract c = new TMContract(provider, info, (TMPricePlan) pricePlan, this, activation, priceplanChange, contractRenewal, term, equipmentChangeRequest);
		if (priceplanChange) {
			TMPricePlan oldPlan = (TMPricePlan) getContract().getPricePlan();
			TMPricePlan newPlan = (TMPricePlan) pricePlan;

			if (oldPlan.isXEW() && newPlan.isEW()) {
				Service[] optionalServices = pricePlan.getOptionalServices();
				if (optionalServices != null && optionalServices.length > 0) {
					Service service = null;
					for (int i = 0; i < optionalServices.length; i++) {
						service = optionalServices[i];
						ServiceInfo serviceInfo = ((TMService) service).getDelegate();
						if (serviceInfo.isFlexibleEvenings() && !c.containsService(service.getCode())) {
							c.addService(service);
						}
					}
				}
			} 
		} // end-if priceplanChange        

		if (!priceplanChange && !contractRenewal) {
			setContract(c);
		}
		// Multi-Ring
		if (delegate.getMultiRingPhoneNumbers() != null) {
			c.setMultiRingPhoneNumbers(delegate.getMultiRingPhoneNumbers());
		}

		return c;
	}

	public Contract newContract(PricePlan pricePlan, int term,boolean dispatchOnly) throws InvalidPricePlanChangeException,TelusAPIException {
		return newContract0(pricePlan, term, dispatchOnly, null, false, null);
	}	
	
	/**
	 * 
	 *  This method will be called only from TMMigrationRequest to process the Migration Request. 
	 *  The newAccount should be passed while validating the BA/non-BA 
	 *  priceplan/service change during Migration process. 
	 * 
	 * @param pricePlan - new priceplan code
	 * @param term - term period
	 * @param dispatchOnly
	 * @param newAccount - new account details for migration
	 * 
	 * @return contact
	 * 
	 * @throws InvalidPricePlanChangeException
	 * @throws TelusAPIException
	 */
	protected Contract newContract(PricePlan pricePlan, int term,
			boolean dispatchOnly, TMAccount newAccount) throws InvalidPricePlanChangeException, TelusAPIException {		
		Contract contract = newContract0(pricePlan, term, dispatchOnly, null, false, newAccount);
		return contract;
	}	

	@Override
	public Contract newContract(PricePlan pricePlan, int term) throws
	InvalidPricePlanChangeException, TelusAPIException {
		if (!activation) {
			return newContract(pricePlan, term, !getContract0().isTelephonyEnabled());
		}
		else {
			return newContract(pricePlan, term, false);
		}
	}

	
	public Contract newContract(PricePlan pricePlan, int term,boolean dispatchOnly,
			EquipmentChangeRequest equipmentChangeRequest) throws
			InvalidPricePlanChangeException, TelusAPIException {
		return newContract0(pricePlan, term, dispatchOnly,
				equipmentChangeRequest, false, null);
	}
	
	/**
	 *  This method will be called only from TMMigrationRequest to process the Migration Request. 
	 *  The newAccount should be passed while validating the BA/non-BA 
	 *  priceplan/service change during Migration process. 
	 * 
	 * @param pricePlan - new priceplan code
	 * @param term - term period
	 * @param dispatchOnly
	 * @param equipmentChangeRequest
	 * @param newAccount - new account information for migration
	 * 
	 * @return contract
	 * 
	 * @throws InvalidPricePlanChangeException
	 * @throws TelusAPIException
	 */
	protected Contract newContract(PricePlan pricePlan, int term,
			boolean dispatchOnly, EquipmentChangeRequest equipmentChangeRequest, TMAccount newAccount) throws
			InvalidPricePlanChangeException, TelusAPIException {
		Contract contract = newContract0(pricePlan, term, dispatchOnly,	equipmentChangeRequest, false, newAccount);
		return contract;
	}	

	@Override
	public Contract newContract(PricePlan pricePlan, int term,
			EquipmentChangeRequest equipmentChangeRequest) throws
			InvalidPricePlanChangeException, TelusAPIException {
		if (!activation) {
			return newContract(pricePlan, term, !getContract0()
					.isTelephonyEnabled(), equipmentChangeRequest);
		}
		else {
			return newContract(pricePlan, term, false, equipmentChangeRequest);
		}
	}

	@Override
	public EquipmentChangeRequest newEquipmentChangeRequest(
			Equipment newEquipment, String dealerCode, String salesRepCode,
			String requestorId, String repairId, String swapType) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return newEquipmentChangeRequest(newEquipment, dealerCode,
				salesRepCode, requestorId, repairId,
				swapType, false);

	}

	//Feb 23,2011, HT end state contract CR: 
	//change this method to delegate to the new overloaded version with flag Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW
	@Override
	public EquipmentChangeRequest newEquipmentChangeRequest(
			Equipment newEquipment, String dealerCode, String salesRepCode,
			String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices
	) throws TelusAPIException,
	SerialNumberInUseException, InvalidEquipmentChangeException {

		return newEquipmentChangeRequest(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW);

	}

	//Feb 23,2011, HT end state contract CR: 
	//overload above API to support SmartDestkop triangle swap scenario, in which case, the new equipment is in used.
	@Override
	public EquipmentChangeRequest newEquipmentChangeRequest(
			Equipment newEquipment, String dealerCode, String salesRepCode,
			String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo) throws TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {
		EquipmentChangeRequestInfo info = new EquipmentChangeRequestInfo();

		info.setNewEquipment(newEquipment);
		info.setRequestorId(requestorId);
		info.setRepairId(repairId);
		info.setSwapType(swapType);
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		info.setPreserveDigitalServices(preserveDigitalServices);

		// validate
		try{
			testChangeEquipment(newEquipment, dealerCode, salesRepCode,
					requestorId, repairId, swapType, allowDuplicateSerialNo);
		}catch(InvalidEquipmentChangeException e){
			if(isMigration && e.getReason() != InvalidEquipmentChangeException.INVALID_SWAP_FOR_PREPAID_ACCOUNT){
				throw e;
			}
		}

		return info;
	}

	//Feb 23,2011, HT end state contract CR: 
	//change this method to delegate to the new overloaded version with flag Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW
	@Deprecated
	public EquipmentChangeRequest newEquipmentChangeRequest(
			IDENEquipment newIDENEquipment, String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, MuleEquipment associatedMuleEquipment) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		return newEquipmentChangeRequest( newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType, associatedMuleEquipment, 
				Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW );
	}

	//Feb 23,2011, HT end state contract CR: 
	//overload above API to support SmartDestkop triangle swap scenario, in which case, the new equipment is in used.
	@Deprecated
	public EquipmentChangeRequest newEquipmentChangeRequest(
			IDENEquipment newIDENEquipment, String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, MuleEquipment associatedMuleEquipment,
			char allowDuplicateSerialNo ) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		EquipmentChangeRequestInfo info = new EquipmentChangeRequestInfo();

		info.setNewEquipment(newIDENEquipment);
		info.setRequestorId(requestorId);
		info.setRepairId(repairId);
		info.setSwapType(swapType);
		info.setAssociatedMuleEquipment(associatedMuleEquipment);
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);

		// validate
		testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType,
				associatedMuleEquipment, allowDuplicateSerialNo);

		if (newIDENEquipment.isSIMCard()) {
			if ( ( ( (SIMCardEquipment) newIDENEquipment).getLastMule() == null)
					&& (associatedMuleEquipment != null)) {
				( (SIMCardEquipment) newIDENEquipment)
				.setLastMule(associatedMuleEquipment);
			}
		}
		return info;
	}
	//===================================================================================
	// Contract Renewals
	//===================================================================================

	@Override
	public Contract renewContract(int term) throws TelusAPIException {
		return renewContract(getContract0().getPricePlan(), term);
	}

	@Override
	public Contract renewContract(PricePlan pricePlan, int term) throws
	InvalidPricePlanChangeException, TelusAPIException {
		return newContract0(pricePlan, term, !getContract0()
				.isTelephonyEnabled(), null, true, null);
	}

	@Override
	public Contract renewContract(PricePlan pricePlan, int term,
			EquipmentChangeRequest equipmentChangeRequest) throws
			InvalidPricePlanChangeException, TelusAPIException {
		return newContract0(pricePlan, term, !getContract0()
				.isTelephonyEnabled(), equipmentChangeRequest, true, null);
	}

	public Contract renewContract(PricePlan pricePlan, int term,
			boolean dispatchOnly) throws
			InvalidPricePlanChangeException,
			TelusAPIException {
		return newContract0(pricePlan, term, dispatchOnly, null, true, null);
	}

	public Contract renewContract(PricePlan pricePlan, int term,
			boolean dispatchOnly,
			EquipmentChangeRequest equipmentChangeRequest) throws
			InvalidPricePlanChangeException, TelusAPIException {
		return newContract0(pricePlan, term, dispatchOnly,
				equipmentChangeRequest, true, null);
	}

	@Override
	public VoiceUsageSummary getVoiceUsageSummary(String featureCode) throws VoiceUsageSummaryException, TelusAPIException {
		if (accountSummary.isPrepaidConsumer()) {
			log("Improper call of getVoiceUsageSummary for prepaid subscriber["+this.getSubscriberId()+"]");
			return null;
		}
		
		try {

			// get Info class from DAO
			VoiceUsageSummaryInfo summary = provider.getSubscriberLifecycleHelper().retrieveVoiceUsageSummary(getBanId(), getSubscriberId(),featureCode);
			if (summary == null) {
				throw new VoiceUsageSummaryException(VoiceUsageSummaryException.NOT_AVAILABLE);
			}

			// traverse the array of voice usage usase services
			VoiceUsageServiceInfo[] services = (VoiceUsageServiceInfo[]) summary.getVoiceUsageServices();
			for (int i = 0; i < services.length; i++) {

				// retrieve the uage rating frequency using the PricePlanSummary reference object.
				int frequency = provider.getReferenceDataManager()
				.getPricePlan(services[i].getServiceCode())
				.getUsageRatingFrequency();

				// traverse the array of voice usage usase service directions
				VoiceUsageServiceDirectionInfo[] directions = (VoiceUsageServiceDirectionInfo[]) services[i].getVoiceUsageServiceDirections();
				for (int j = 0; j < directions.length; j++) {

					// traverse the array of voice usage usase service periods
					VoiceUsageServicePeriodInfo[] periods = (VoiceUsageServicePeriodInfo[])
					directions[j].getVoiceUsageServicePeriods();

					for (int k = 0; k < periods.length; k++) {
						// round usage minutes using the rating frequency
						periods[k].setTotalUsed(roundMinutes(periods[k].getTotalUsed(), frequency,USAGE_MINUTE_ROUNDING_UP));
						periods[k].setIncluded(roundMinutes(periods[k].getIncluded(), frequency,USAGE_MINUTE_ROUNDING_DOWN));
						periods[k].setIncludedUsed(roundMinutes(periods[k].getIncludedUsed(), frequency, USAGE_MINUTE_ROUNDING_UP));
						periods[k].setFree(roundMinutes(periods[k].getFree(),frequency,USAGE_MINUTE_ROUNDING_DOWN));
						periods[k].setRemaining(roundMinutes(periods[k].getRemaining(), frequency,USAGE_MINUTE_ROUNDING_DOWN));
						periods[k].setChargeable(roundMinutes(periods[k].getChargeable(), frequency,USAGE_MINUTE_ROUNDING_UP));
					}

					directions[j].setVoiceUsageServicePeriods(periods);
				}
				services[i].setVoiceUsageServiceDirections(directions);
			}
			summary.setVoiceUsageServices(services);

			// return updated Info class with rounded minutes in child Info
			return summary;

		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}
	// minutes=9999.3333 , frequency=second, UP ==> 9999.3333
	// minutes=9999.3333 , frequency=minute, UP ==> 9999.0
	// minutes=9999.9888 , frequency=second, UP ==> 10000.0
	// minutes=9999.9888 , frequency=minute, UP ==> 10000.0
	// minutes=9999.9999 , frequency=second, UP ==> 10000.0
	// minutes=9999.9999 , frequency=minute, UP ==> 10000.0

	private double roundMinutes(double minutes, int usageRatingFrequency) {
		return roundMinutes(minutes, usageRatingFrequency,
				USAGE_MINUTE_ROUNDING_UP);
	}

	private double roundMinutes(double minutes, int usageRatingFrequency, String roundingType) {

		int mins = (int) minutes;

		// convert decimal minutes to seconds --> [0..60]
		double secs = (minutes - mins) * 60;

		// round seconds depending of the rouding function
		double roundedSeconds = 0.0;
		if (roundingType.equals(USAGE_MINUTE_ROUNDING_UP)) {
			roundedSeconds = Math.ceil(secs);
		}
		else if (roundingType.equals(USAGE_MINUTE_ROUNDING_DOWN)) {
			roundedSeconds = Math.floor(secs);
		}
		else {
			roundedSeconds = Math.round(secs);
		}

		// increment minutes if rounded seconds is 60
		if ( (int) roundedSeconds == 60) {
			mins++;
			roundedSeconds = 0.0;
		}

		// clear rounded seconds if price plan is per minute
		if (usageRatingFrequency == PricePlanSummary.USAGE_RATING_FREQUENCY_MINUTE) {
			roundedSeconds = 0.0;
		}

		// convert back to decimal minutes with maximum 4 decimal digits
		int intsecs = (int) ( (roundedSeconds / 60) * 10000);
		roundedSeconds = intsecs / 10000.0;

		return mins + roundedSeconds;
	}

	@Override
	public VoiceUsageSummary getVoiceUsageSummary() throws
	VoiceUsageSummaryException, TelusAPIException {
		return getVoiceUsageSummary(null);
	}

	@Override
	public WebUsageSummary getWebUsageSummary() throws TelusAPIException {
		throw new AbstractMethodError("TODO");
	}

	@Override
	public Memo newMemo() throws TelusAPIException {
		assertSubscriberKnown();
		MemoInfo info = new MemoInfo();
		info.setBanId(getBanId());
		info.setSubscriberId(getSubscriberId());
		info.setProductType(delegate.getProductType());
		info.setOperatorId(Integer.parseInt(provider.getUser()));
		return new TMMemo(provider, info);
	}

	@Override
	public FollowUp newFollowUp() throws TelusAPIException {
		assertSubscriberKnown();
		FollowUpInfo info = new FollowUpInfo();
		info.setBanId(getBanId());
		info.setSubscriberId(getSubscriberId());
		info.setProductType(delegate.getProductType());
		info.setOperatorId(Integer.parseInt(provider.getUser()));
		return new TMFollowUp(provider, info);
	}

	@Override
	public Charge newCharge() throws TelusAPIException {
		assertSubscriberKnown();
		ChargeInfo info = new ChargeInfo();
		info.setBan(getBanId());
		info.setSubscriberId(getSubscriberId());
		info.setProductType(delegate.getProductType());
		info.setPrepaid(!getAccount().isPostpaid());
		return new TMCharge(provider, info);
	}

	@Override
	public Credit newCredit() throws TelusAPIException {
		return newCredit(false);
	}

	@Override
	public Credit newCredit(boolean taxable) throws TelusAPIException {
		return taxable ? newCredit(Credit.TAX_OPTION_ALL_TAXES) : newCredit(Credit.TAX_OPTION_NO_TAX);
	}

	@Override
	public Credit newCredit(char taxOption) throws TelusAPIException {
		assertSubscriberKnown();
		CreditInfo info = new CreditInfo(taxOption);
		info.setBan(getBanId());
		info.setSubscriberId(getSubscriberId());
		info.setProductType(delegate.getProductType());
		info.setPrepaid(!getAccount().isPostpaid());
		info.setPhoneNumber(getPhoneNumber());
		info.getTaxSummary().setProvince(getAccount().getAddress().getProvince());
		return new TMCredit(provider, info, getAccount());
	}

	@Override
	public Discount newDiscount() throws TelusAPIException {
		assertSubscriberKnown();
		DiscountInfo info = new DiscountInfo();
		info.setBan(getBanId());
		info.setSubscriberId(getSubscriberId());
		info.setProductType(delegate.getProductType());
		return new TMDiscount(provider, info);
	}

	@Override
	public Discount[] getDiscounts() throws TelusAPIException {
		try {
			return provider.getAccountManager0().decorate(provider.getSubscriberManagerBean().retrieveDiscounts(getBanId(), getSubscriberId(), getProductType()));
		}
		catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	@Override
	public AvailablePhoneNumber[] findAvailablePhoneNumbers(
			PhoneNumberReservation reservation, int maximum) throws TelusAPIException,
			PhoneNumberException {
		reservation.setProductType(getProductType());
		try {
			// reservation.isLikeMatch() is ignored.
			return provider.getSubscriberManagerBean().retrieveAvailablePhoneNumbers(getBanId(),getSubscriberId(),
					( (TMPhoneNumberReservation) reservation).getPhonenumberReservation0(), maximum);
		}	
		catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderDefaultExceptionTranslator(){

				@Override
				protected TelusAPIException getExceptionForErrorId(
						String errorId, Throwable cause) {
					if ("APP20001".equals(errorId)) {
						return new PhoneNumberException(cause);
					}
					return new TelusAPIException(cause);
				}

			}) ;
			throw new TelusAPIException(e);
		}
	}

	@Override
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber,boolean changeOtherNumbers) throws TelusAPIException,PhoneNumberException, PhoneNumberInUseException {
		changePhoneNumber0(availablePhoneNumber, "CR", changeOtherNumbers,null, null);
	}

	@Override
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber,boolean changeOtherNumbers, String dealerCode,String salesRepCode) 
			throws TelusAPIException,PhoneNumberException,PhoneNumberInUseException {
		changePhoneNumber0(availablePhoneNumber, "CR", changeOtherNumbers,dealerCode, salesRepCode);
	}

	@Override
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber,boolean changeOtherNumbers, String dealerCode,String salesRepCode,String reasonCode) throws TelusAPIException,PhoneNumberException,PhoneNumberInUseException {
		changePhoneNumber0(availablePhoneNumber, reasonCode,changeOtherNumbers, dealerCode, salesRepCode);
	}

	protected void changePhoneNumber0(AvailablePhoneNumber availablePhoneNumber, String reasonCode,boolean changeOtherNumbers, String dealerCode, String salesRepCode)
			throws TelusAPIException, PhoneNumberException,	PhoneNumberInUseException {
		commSuitePhoneNumberChangePreTask();
		changePhoneNumber0(availablePhoneNumber, reasonCode,changeOtherNumbers, dealerCode, salesRepCode, false,null);
	}

	private void changePhoneNumber0(AvailablePhoneNumber availablePhoneNumber, String reasonCode,boolean changeOtherNumbers, String dealerCode, 
			String salesRepCode, boolean portIn, SubscriberInfo outgoingSub) throws TelusAPIException, PhoneNumberException,	PhoneNumberInUseException {

		String methodName = "changePhoneNumber0";
		String activity = "changePhoneNumber0";
		assertSubscriberExists();
		saveDealer();

		try {
			setupNumberGroupDealer(availablePhoneNumber.getNumberGroup());

			// get old and new number group and province info
			String oldNumberGroup = null;
			String newNumberGroup = availablePhoneNumber.getNumberGroup().getCode();
			String oldProvinceCode = null;
			String newProvinceCode = availablePhoneNumber.getNumberGroup().getProvinceCode().toUpperCase().trim();

			try {
				NumberGroupInfo oldNumberGroupInfo = provider.getReferenceDataHelperEJB().retrieveNumberGroupByPhoneNumberProductType(getPhoneNumber(), getProductType());
				oldNumberGroup = oldNumberGroupInfo.getCode();
				oldProvinceCode = oldNumberGroupInfo.getProvinceCode().toUpperCase().trim();

			} catch (TelusException te) {
				log(te.getMessage());
			} catch (Throwable t) {
				log(t.getMessage());
			}

			String existingPhoneNumber = getPhoneNumber();

			log("== Old Phone Number: " + existingPhoneNumber);
			log("== New Phone Number: " + availablePhoneNumber.getPhoneNumber());
			log("== Old Number Group: " + oldNumberGroup);
			log("== New Number Group: " + newNumberGroup);
			log("== Reason Code: " + reasonCode);

			// change phone number start...
			boolean refresh = false;

			try {
				try {
					if (!portIn) {
						activity = "change phone in KB";
						log("changing subscriber dealer => " + dealerCode + ":" + salesRepCode);
						provider.getSubscriberLifecycleFacade().changePhoneNumber(delegate,(AvailablePhoneNumberInfo) availablePhoneNumber, reasonCode,dealerCode,
								salesRepCode, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
						logSuccess( methodName, activity, "phoneNumber changed to ["+availablePhoneNumber.getPhoneNumber() + "]" );

					} else {
						// execute the inter-brand port change phone number logic
						if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
							activity = "inter-brand port change phone number";
							try {
								provider.getSubscriberLifecycleFacade().changePhoneNumberPortIn(delegate, (AvailablePhoneNumberInfo) availablePhoneNumber, reasonCode, dealerCode, salesRepCode, portProcess,
										outgoingSub.getBanId(), outgoingSub.getSubscriberId(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
								logSuccess(methodName, activity, outgoingSub.toString());

							} catch(Throwable t) {
								// if the change phone number fails, just log it and throw an exception - the calling method will perform rollback
								logFailure(methodName, activity, delegate, t, null);
								throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR008), InterBrandPortRequestException.ERR008);
							}
						} else {
							// execute the regular port change phone number logic	
							provider.getSubscriberLifecycleFacade().changePhoneNumberPortIn(delegate, (AvailablePhoneNumberInfo) availablePhoneNumber, reasonCode, dealerCode, salesRepCode, portProcess,
									0, null, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
							logSuccess(methodName, "inter carrier phone number change in KB", "phoneNumber changed to ["+availablePhoneNumber.getPhoneNumber() + "]");
						}
					}
					
					String sessionId = SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade());
					
					commSuitePhoneNumberChangePostTask(delegate.getBanId(), availablePhoneNumber.getPhoneNumber(), getPhoneNumber(), sessionId);
					
					if (isPCS()) {
						SubscriberLifecycleFacade subLifeCycleFacade=provider.getSubscriberLifecycleFacade();
						String networkType = getEquipment0().getNetworkType();
						activity = "change TN";

						// call RCM to change MIN-MDN relationship after the subscriber phone number change
						try {
							subLifeCycleFacade.changeTN(getPhoneNumber(), availablePhoneNumber.getPhoneNumber(), networkType);
							logSuccess(methodName, activity, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+networkType+"]");
						} catch (Throwable t) {
							// if the RCM update fails, just log it and continue
							logFailure(methodName, activity, t, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+networkType+"]");
						}

						// call SEMS to update MDN-Serial # relationship
						if (getEquipment().isHSPA()) {
							try {
								activity = "SEMS changePhoneNumber(HSPA)";
								provider.getProductEquipmentLifecycleFacade().asyncChangePhoneNumber(getEquipment().getSerialNumber(), availablePhoneNumber.getPhoneNumber());
								logSuccess(methodName, activity, "NewPhoneNumber["+availablePhoneNumber.getPhoneNumber()+"]");
							} catch (Throwable t) {
								// if the SEMS update fails, just log it and continue
								logFailure(methodName, activity, t, "NewPhoneNumber["+availablePhoneNumber.getPhoneNumber()+"]");
							}
						}
					}

					provider.getInteractionManager0().subscriberChangePhoneNumber(this, availablePhoneNumber, changeOtherNumbers);
					delegate.setPhoneNumber(availablePhoneNumber.getPhoneNumber());
					refresh = true;
					waitUntilProvisioned();
					
				} catch (Throwable t) {				
					provider.getExceptionHandler().handleException(t, new ProviderDefaultExceptionTranslator() {
						@Override
						protected TelusAPIException getExceptionForErrorId(
								String errorId, Throwable cause) {
							if ( "VAL20026".equals (errorId ) ) {
								return new PhoneNumberInUseException(PhoneNumberException.PRIMARY_RESERVATION_FAILED, cause);
							}
							else if ("APP20001".equals(errorId)) {
								return new PhoneNumberException(PhoneNumberException.PRIMARY_RESERVATION_FAILED, cause);
							} else {
								return new TelusAPIException(cause);
							}
						}
					});
				}


				if (changeOtherNumbers) {
					try {
						if (!portIn)
							provider.getSubscriberManagerBean().changeAdditionalPhoneNumbers(delegate);
						else
							provider.getSubscriberManagerBean().changeAdditionalPhoneNumbersForPortIn(delegate);
						refresh = true;

					} catch (TelusException e) {
						if ("VAL20026".equals(e.id)) {
							throw new PhoneNumberInUseException(PhoneNumberException.ADDITIONAL_RESERVATION_FAILED,e);
						} else if ("APP20001".equals(e.id)) {
							throw new PhoneNumberException(PhoneNumberException.ADDITIONAL_RESERVATION_FAILED,e);
						} else {
							throw new TelusAPIException(e);
						}

					} catch (Throwable e) {
						throw new TelusAPIException(e);
					}
				}

			} finally {
				if (refresh) {
					refresh(); // reload entire subscriber
				}
			}

			// process for applicable 911 provincial service fees
			process911FeeServices(oldProvinceCode, newProvinceCode);

		} finally {
			if (this.getDealerCode() == null || this.getDealerCode().equals("")) {
				restoreDealer();
			}
		}
	}
	
	/**
	 * 911 provincial service fees:
	 *
	 * Additional provincial 911 service fees apply to subscribers in certain provinces. These charges are in addition to
	 * generic 911 service. When subscriber numbers move to or from one of these provinces, the corresponding 911 service
	 * fee must be applied or removed.  Currently, these provinces are identified as follows:
	 * 
	 * 1.	Retrieve list of potential 911 fee SOCs using feature - category relation.  ClientAPI will use the existing '911'
	 * 		category code to retrieve TELUS 911 SOCs, and the new 'PT9' category code for Koodo 911 SOCs.  ClientAPI will 
	 * 		internally maintain the mapping of the category code to the appropriate brand.
	 * 2.	Filter the list of SOCs to exclude 911 service charge SOCs.  Since the above category codes will also return the
	 * 		911 service charge SOCs as part of the list, the ClientAPI will use the '911' feature code to filter TELUS SOCs
	 * 		out and the 'PT911' code to filter Koodo SOCs out.
	 * 3.	Filter the list to exclude all non-current SOCs.
	 * 4.	Filter the list to exclude all promotional SOCs.
	 * 5.	Filter the list to exclude all SOCs where the sale expiration date is not null and < the current date.
	 * 6.	Select from the remaining list of SOCs the first SOC which matches to the province required for the 911 fee.
	 */
	private void process911FeeServices(String oldProvinceCode, String newProvinceCode) throws TelusAPIException {

		/*defect PROD00160052: assumption, this method is only invoked by phone number change flow.
		 * Force the retrieve new contract, so that contract's effective date will be same as subscriber's startServiceDate, so that it 
		 * can satisfy the TMContract.save(String dealerCode, String salesRepCode)'s contract effective date validation logic.
		 */
		TMContract contract = this.getContract0(true);
		String serviceCode = null;
		AccountType accountType = provider.getReferenceDataManager0().getAccountType(getAccountSummary());
		String defaultDealerCode = accountType.getDefaultDealer();
		String defaultSalesCode = accountType.getDefaultSalesCode();

		log("== Changing phone number from: " + oldProvinceCode + " --> " + newProvinceCode);

		// process 911 fees start...
		try {
			log("== Processing removal of 911 provincial fees...");

			// cycle through all 911 province service fees and remove them if present
			log("== Moving from province: " + oldProvinceCode);			
			HashMap feeServiceCodesByProvince = get911FeeServiceCodesByProvince();
			HashSet _911FeeProvinces = get911FeeProvinces();
			// iterate through the _911FeeProvinces hashset and check each province
			Iterator i = _911FeeProvinces.iterator();
			while (i.hasNext()) {
				String province = (String)i.next();
				log("== Checking contract for 911 fees for province: " + province);
				if (province.equals(newProvinceCode)) {
					// don't bother checking for fees that are valid for the new province
					log("== Old province is the same as new province; skipping 911 fee service removal for province: " + province);
				} else {
					// get the 911 fee service code from the feeServiceCodesByProvince hashmap
					serviceCode = (String)feeServiceCodesByProvince.get(province);
					if (contract.getDelegate().containsService0(serviceCode, false)) {
						contract.removeService(serviceCode);
						contract.save(defaultDealerCode, defaultSalesCode);
						log("== 911 provincial fee service code [" + serviceCode + "] removed from contract.");
					}
				}
			}

			log("== Processing addition of 911 provincial fees...");

			// determine if 911 service fees apply
			if (_911FeeProvinces.contains(newProvinceCode)) {

				log("== Moving to 911 fee province: " + newProvinceCode);

				// check the contract to see if 911 service of any kind is present
				if (contract.get911Services().length > 0) {			
					// get the 911 fee service code from the feeServiceCodesByProvince hashmap
					serviceCode = (String)feeServiceCodesByProvince.get(newProvinceCode);
					if (serviceCode == null || serviceCode.equals("")) {
						log("== 911 provincial fee service code could not be resolved for province: " + newProvinceCode);
					} else if (contract.getDelegate().containsService0(serviceCode, false)) {
						log("== 911 provincial fee service code [" + serviceCode + "] already exists on contract.");
					} else {							
						contract.addService(serviceCode);
						contract.save(defaultDealerCode, defaultSalesCode);
						log("== 911 provincial fee service code [" + serviceCode + "] added to contract.");
					}

				} else {
					log("== Regular 911 service not found on contract.");
				}
			}

		} catch (Throwable t) {
			// if processing of 911 service fees fails, just log it - phone number changes cannot fail because of this
			log(t.getMessage());
		}
	}

	/**
	 * 911 provincial service fees:
	 *
	 * Additional provincial 911 service fees apply to subscribers in certain provinces. These charges are in addition to
	 * generic 911 service. When subscriber numbers move to or from one of these provinces, the corresponding 911 service
	 * fee must be applied or removed.  Currently, these provinces are identified as follows:
	 * 
	 * 1.	Retrieve list of potential 911 fee SOCs using feature - category relation.  ClientAPI will use the existing '911'
	 * 		category code to retrieve TELUS 911 SOCs, and the new 'PT9' category code for Koodo 911 SOCs.  ClientAPI will 
	 * 		internally maintain the mapping of the category code to the appropriate brand.
	 * 2.	Filter the list of SOCs to exclude 911 service charge SOCs.  Since the above category codes will also return the
	 * 		911 service charge SOCs as part of the list, the ClientAPI will use the '911' feature code to filter TELUS SOCs
	 * 		out and the 'PT911' code to filter Koodo SOCs out.
	 * 3.	Filter the list to exclude all non-current SOCs.
	 * 4.	Filter the list to exclude all promotional SOCs.
	 * 5.	Filter the list to exclude all SOCs where the sale expiration date is not null and < the current date.
	 * 6.	Select from the remaining list of SOCs the first SOC which matches to the province required for the 911 fee.
	 */
	private HashMap get911FeeServiceCodesByProvince() throws TelusAPIException {

		if (_911FeeServiceCodesByProvince.isEmpty()) {

			String _911FeeCategoryCode = (String)AppConfiguration.get911CategoryCodesByBrandKeyMap().get(String.valueOf(getBrandId()));			
			Service[] services = {};
				if (_911FeeCategoryCode != null)
					services = provider.getReferenceDataManager().getServicesByFeatureCategory(_911FeeCategoryCode, getProductType(), false);

			// iterate through the services to retrieve the 911 fee service codes for all provinces in the set				
			for (int i = 0 ; i < services.length; i++) {					
				// filter out all services where the province does not match
				// filter out all non-current, promotional and expired services
				if (get911FeeProvinces().contains(services[i].getProvinces()[0]) && !services[i].isNonCurrent() && !services[i].isPromotion() 
						&& !(services[i].getExpiryDate() != null && services[i].getExpiryDate().before(new Date()))) {

					RatedFeature[] features = services[i].getFeatures();		
					for (int j = 0; j < features.length; j++) {
						// if the feature code does not match the TELUS and Koodo 911 feature codes, then this
						// isn't a 911 service - it must be a 911 fee
							if (!features[j].getCode().equals(Feature.FEATURE_CODE_TELUS_911) 
									&& !features[j].getCode().equals(Feature.FEATURE_CODE_KOODO_911)
									&& !features[j].getCode().equals(Feature.FEATURE_CODE_WALMART_911)) {
								// put the province and 911 fee service code into the hashmap
								_911FeeServiceCodesByProvince.put(services[i].getProvinces()[0], services[i].getCode());
								log("== Found 911 fee service code [" + services[i].getCode() + "] for province: " + services[i].getProvinces()[0]);
							}
					}
				}
			}
		}

		return _911FeeServiceCodesByProvince;
	}

	private void log(String str) {
		Logger.debug(str);
	}

	@Override
	public void reserveAdditionalPhoneNumber(
			AvailablePhoneNumber availablePhoneNumber) throws TelusAPIException,
			PhoneNumberException,
			PhoneNumberInUseException {
		assertSubscriberExists();
		saveDealer();
		try {
			setupNumberGroupDealer(availablePhoneNumber.getNumberGroup());
			try {
				provider.getSubscriberManagerBean().reserveAdditionalPhoneNumber(getBanId(),getProductType(), getSubscriberId(),(AvailablePhoneNumberInfo)availablePhoneNumber);
			}
			catch (Throwable e) {
				provider.getExceptionHandler().handleException(e, new ProviderDefaultExceptionTranslator() {
					@Override
					public TelusAPIException translateException(Throwable throwable) {
						if ( throwable instanceof PhoneNumberAlreadyInUseException ) {
							return new PhoneNumberInUseException( throwable );
						}
						return super.translateException(throwable);
					}

					@Override
					protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
						if ( "VAL20026".equals(errorId) ) {
							return new PhoneNumberInUseException( cause );
						} else if ( "APP20001".equals(errorId) ) {
							return new PhoneNumberException( cause );
						} else {
							return new TelusAPIException(cause);
						}
					}
				});
			}
		}
		finally {
			restoreDealer();
		}
	}

	private CPMSDealer getCPMSDealer(String dealerCode, String salesRepCode) throws InvalidEquipmentChangeException, TelusAPIException {

		try {
			dealer = provider.getDealerManager().getCPMSDealer(dealerCode,salesRepCode);
		}
		catch (UnknownObjectException e) {
			String exceptionMsg ="Unknown dealer code and/or sales rep code. dealerCode=["+ dealerCode + "], salesRepCode=[" + salesRepCode + "]";
			throw new InvalidEquipmentChangeException(exceptionMsg, e,InvalidEquipmentChangeException.DEALER_INFO_NOT_FOUND);
		}
		catch (TelusAPIException e) {
			throw e;
		}
		return dealer;
	}

	@Override
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return testChangeEquipment(newEquipment, dealerCode, salesRepCode,requestorId, repairId, swapType, false);

	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return testChangeEquipment0(newEquipment, dealerCode, salesRepCode,requestorId, repairId, swapType, null, false,ignoreSerialNoInUse);
	}

	@Override
	public ApplicationMessage[] testChangeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			char allowDuplicateSerialNo) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return testChangeEquipment0(newEquipment, dealerCode, salesRepCode,requestorId, repairId, swapType, null, false,allowDuplicateSerialNo);
	}

	public ApplicationMessage[] testChangeEquipment0(Equipment newEquipment,
			String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, TMMuleEquipment oldMuleEquipment) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return testChangeEquipment0(newEquipment, dealerCode, salesRepCode,requestorId, repairId, swapType,oldMuleEquipment, false, false);
	}

	public ApplicationMessage[] testChangeEquipment0(Equipment newEquipment,
			String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, TMMuleEquipment oldMuleEquipment,
			boolean preserveDigitalServices, boolean ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		char allowDuplicateSerailNo = convertAllowDuplicateFlag( ignoreSerialNoInUse );
		return testChangeEquipment0(newEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType,
				oldMuleEquipment, preserveDigitalServices, allowDuplicateSerailNo );
	}
	public ApplicationMessage[] testChangeEquipment0(Equipment newEquipment,
			String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType, TMMuleEquipment oldMuleEquipment,
			boolean preserveDigitalServices, char allowDuplicateSerialNo) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		assertSubscriberExists(); // throws UnknownSubscriberException

		//Holborn R2
		if (newEquipment.isUSIMCard()){			
			if (newEquipment.isVirtual()){
				throw new InvalidEquipmentChangeException("USIMCard sn(" + newEquipment.getSerialNumber()+") is virtual.");
			}
			TMUSIMCardEquipment usimNewEquip = (TMUSIMCardEquipment)newEquipment;
			if (usimNewEquip.isExpired()) {
				throw new InvalidSerialNumberException("USIMCard sn(" + newEquipment.getSerialNumber()+") is expired.");
			}
			if (usimNewEquip.isPreviouslyActivated()){
				if (getSubscriptionId() != Long.valueOf(usimNewEquip.getLastAssociatedSubscriptionId()).longValue()) {
					throw new SerialNumberInUseException("USIMCard sn(" + newEquipment.getSerialNumber()+") is previously activated" +
							" with subscription id: " + usimNewEquip.getLastAssociatedSubscriptionId());
				}
			}
			// fix  for PROD00141498
			if (usimNewEquip.isStolen() && !usimNewEquip.isPreviouslyActivated()) {
				throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
			}
		} else if (!AppConfiguration.isEsimSupportEnabled()) {
			throw new InvalidEquipmentChangeException("Unsupported device for equipment change", InvalidEquipmentChangeException.UNSUPPORTED_EQUIPMENT);
		} else if (newEquipment.isStolen()) {
			throw new InvalidEquipmentChangeException("The new ESIM device is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
		}
		
		// The ApplicationMessage[] that got returned before should not be applicable anymore
		return new ApplicationMessage[0];
		
		//TMEquipment oldEquipmt = (TMEquipment) getEquipment();
		//TMEquipment newEquipmt = (TMEquipment) newEquipment;
		
		//if (oldMuleEquipment != null) {
		//	oldEquipmt = oldMuleEquipment;

		//}
		//dealer = getCPMSDealer(dealerCode, salesRepCode);
		//validateSwap(oldEquipmt, newEquipmt, requestorId, repairId, swapType, null,
		//		allowDuplicateSerialNo);

		//return getSwapWarningMessages(newEquipment, preserveDigitalServices);
	}

	@Override
	@Deprecated
	public void testChangeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment) throws
			TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {

		testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType,
				associatedMuleEquipment, false);

	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void testChangeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment,
			boolean ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType,
				associatedMuleEquipment, allowDuplicateSerialNo);
	}
	
	@Override
	@Deprecated
	public void testChangeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment,
			char ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		assertSubscriberExists(); // throws UnknownSubscriberException

		try {
			TMEquipment oldEquipmt = (TMEquipment) getEquipment();
			TMEquipment newEquipmt = (TMEquipment) newIDENEquipment;

			dealer = getCPMSDealer(dealerCode, salesRepCode);

			validateSwap(oldEquipmt, newEquipmt, requestorId, repairId, swapType,
					( (TMEquipment) associatedMuleEquipment).getDelegate(),
					ignoreSerialNoInUse);
		}
		catch (TelusAPIException e) {
			throw e;
		}
	}

	@Override
	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType) throws
			TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {

		return changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, false, false);
	}

	@Override
	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, preserveDigitalServices, false);
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			boolean ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		return changeEquipment0(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, null, preserveDigitalServices,
				allowDuplicateSerialNo);
	}

	@Override
	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		return changeEquipment0(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, null, preserveDigitalServices,
				allowDuplicateSerialNo);
	}

	@Override
	public ApplicationMessage[] changeEquipment(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo,
			ServiceRequestHeader header) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		Equipment currentEquipment = getEquipment();

		Equipment currentHandset = null;
		if ( currentEquipment.isUSIMCard() && newEquipment.isUSIMCard()==false ) {
			//HSPA to CDMA 
			currentHandset = ((USIMCardEquipment) currentEquipment).getLastAssociatedHandset();
		} else {
			//for the following type of change, the currentHandset will be null 
			//-CDMA to HSPA USIM only,
			//-CDMA to CDMA
			//-HSPA to HSPA USIM only
		}

		ApplicationMessage[] messages = changeEquipment0(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, null, preserveDigitalServices,
				allowDuplicateSerialNo);

		if ( needToCallSRPDS(header)) {
			((TMServiceRequestManager)provider.getServiceRequestManager())
			.reportChangeEquipment(delegate.getBanId(), delegate.getSubscriberId(),
					delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), 
					currentEquipment, getEquipment(), repairId, swapType, currentHandset, null, header);
		}

		return messages;
	}

	/*
	 * call hierachy shows that only IDEN entry points invoke this method
	 */
	@Deprecated
	public ApplicationMessage[] changeEquipment0(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			TMMuleEquipment oldMuleEquipmentTM,
			boolean preserveDigitalServices) throws
			TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {

		return changeEquipment0(newEquipment, dealerCode, salesRepCode, requestorId,
				repairId, swapType, oldMuleEquipmentTM,
				preserveDigitalServices, SWAP_DUPLICATESERIALNO_DONOTALLOW);

	}

	public ApplicationMessage[] changeEquipment0(Equipment newEquipment,
			String dealerCode,
			String salesRepCode,
			String requestorId,
			String repairId,
			String swapType,
			TMMuleEquipment oldMuleEquipmentTM,
			boolean preserveDigitalServices,
			char allowDuplicateSerialNo) 
	throws	TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {

		//Holborn R1 change
		if (newEquipment.isUSIMCard()) {
			return changeHSPAEquipment(newEquipment, null, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
		} else {
			// If the new equipment is not an USIM primary equipment, it must be an ESIM device. We have removed the legacy logic that is applicable
			// to no longer supported equipments, e.g., IDEN and CDMA equipments.
			return changeEsimEnabledEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
		}

		/**
		ApplicationMessage[] warningMessages = null;
		String methodName = "changeEquipment0";
		String activity = "changeEquipment0";

		try {
			TMEquipment oldEquipmentTM = (TMEquipment)getEquipment();
			TMEquipment newEquipmentTM = (TMEquipment)newEquipment;

			Equipment oldSimCard = null;
			MuleEquipment newMuleEquipment = null;

			if (oldMuleEquipmentTM != null) {
				oldSimCard = getEquipment(); // same SIM
				newMuleEquipment = (MuleEquipment)newEquipment; // new mule
				oldEquipmentTM = oldMuleEquipmentTM;
			}

			activity = "testChangeEquipment0";
			warningMessages = testChangeEquipment0(newEquipment, dealerCode,
					salesRepCode,
					requestorId, repairId, swapType,
					oldMuleEquipmentTM,
					preserveDigitalServices,
					allowDuplicateSerialNo);
			logSuccess(methodName, activity, delegate, null);

			// 2. Change equipment
			if (oldMuleEquipmentTM == null || newMuleEquipment == null) {
				// PCS and IDEN (not mule to mule) swap - call KB to change equipment
				activity = "KB equipment change";
				PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();
				provider.getSubscriberLifecycleFacade().changeEquipment(
						delegate, oldEquipmentTM.getDelegate(),
						newEquipmentTM.getDelegate(), null,
						dealer.getChannelCode(), dealer.getUserCode(),
						requestorId, swapType, null, ppValidationInfo, 
						null, true, null,
						SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));				  
				logSuccess(methodName, activity, delegate, "KB equipment change successful");	
			}

			// 3. If lost - update to found - the swap was previously checked and allowed
			if (newEquipment.isStolen()) {
				newEquipment.reportFound();
			}

			provider.getInteractionManager0().subscriberChangeEquipment(this,
					oldEquipmentTM, newEquipmentTM, dealerCode, salesRepCode,
					requestorId, repairId, swapType, null);

			this.equipment = newEquipment;

			// make sure the subscriber on the contract is fresh
			// and new equipment is on the subscriber already at this point
			if (contract == null) {
				contract = (TMContract)getContract();
			}
			contract.setSubscriber(this);

			// 4. Different Equipment Types: Add and/or remove services to/from
			// the contract as necessary
			addRemoveServices(contract, oldEquipmentTM, newEquipmentTM, preserveDigitalServices);

			// mule to mule - check non-matching services and set SIM to new mule
			if (oldMuleEquipmentTM != null && newMuleEquipment != null) {
				// remove all non-matching optional services
				removeNonMatchingServices(newMuleEquipment, contract); // warranty
				if (oldSimCard.isSIMCard()) {
					activity = "associate SIM with new mule";
					provider.getProductEquipmentManager().setSIMMule(oldSimCard.getSerialNumber(),
							newMuleEquipment.getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);

					logSuccess(methodName, activity, delegate, null);		
				}

			} else if (newEquipment.isCellular() &&
					!((oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) ||
							oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG)) &&
							newEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) &&
							preserveDigitalServices)) {
				// for cellular RIM
				removeNonMatchingServices(newEquipment, contract);
			} 		

			// SIM to SIM, associate new SIM with old / new mule
			if (oldEquipmentTM.isSIMCard() && newEquipmentTM.isSIMCard()) {

				MuleEquipment oldMule = ((TMSIMCardEquipment)oldEquipmentTM).getLastMule();
				MuleEquipment newMule = ((TMSIMCardEquipment)newEquipmentTM).getLastMule();

				if (newMule != null) {
					activity = "associate new SIM with new mule";
					removeNonMatchingServices(newMule, contract); // For New Mule
					provider.getProductEquipmentManager().setSIMMule(newEquipmentTM.getSerialNumber(),
							newMule.getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);

					logSuccess(methodName, activity, delegate, null);	

				} else if (oldMule != null) {
					activity = "associate new SIM with old mule";
					provider.getProductEquipmentManager().setSIMMule(newEquipmentTM.getSerialNumber(),
							oldMule.getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);

					logSuccess(methodName, activity, delegate, null);	
				}

				if (oldMule != null && newMule != null) {
					if (!oldMule.getSerialNumber().equals(newMule.getSerialNumber())) {
						provider.getInteractionManager0().subscriberChangeEquipment(
								this, (TMEquipment)oldMule,
								(TMEquipment)newMule, dealerCode,
								salesRepCode, requestorId, repairId,
								swapType, null);
					}
				}
			}

			// 5. Remove Dispatch only conflicts
			if (!((oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) ||
					oldEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG)) &&
					newEquipmentTM.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) &&
					preserveDigitalServices)) {
				removeDispatchOnlyConflicts(contract);
			}

			// 6. Save the contract
			try {
				activity = "save contract";
				String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				contract.save(KBDealer[0], KBDealer[1]);
			} catch (Throwable t) {
				logFailure(methodName, activity, t, "contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed");
			}

		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}

		return warningMessages;
		*/
	}

	@Override
	public void changeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment) throws
			TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {
		changeEquipment(newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType, associatedMuleEquipment, false);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	@Override
	public void changeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment,
			boolean ignoreSerialNoInUse) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {
		char allowDuplicateSerialNo = convertAllowDuplicateFlag(ignoreSerialNoInUse);
		changeEquipment(newIDENEquipment, dealerCode, salesRepCode,
				requestorId, repairId, swapType, associatedMuleEquipment, allowDuplicateSerialNo);

	}

	@Override
	public void changeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode,
			String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment,
			char allowDuplicateSerialNo) throws
			TelusAPIException, SerialNumberInUseException,
			InvalidEquipmentChangeException {

		String methodName = "changeEquipment";
		String activity = "changeEquipment";

		try {
			// 1. Test Equipment Change Ability
			activity = "testChangeEquipment";
			testChangeEquipment(newIDENEquipment, dealerCode, salesRepCode,
					requestorId, repairId, swapType,
					associatedMuleEquipment,
					allowDuplicateSerialNo);
			logSuccess(methodName, activity, delegate, null);

			TMEquipment e1 = (TMEquipment)newIDENEquipment;
			TMEquipment e2 = (TMEquipment)associatedMuleEquipment;
			TMEquipment oldEquipment = (TMEquipment)getEquipment();

			// 2. Change equipment		 	  
			if (!oldEquipment.getDelegate().isMule() || !e1.getDelegate().isMule()) {
				// IDEN (not mule to mule) swap - call KB to change equipment
				activity = "KB equipment change";
				PricePlanValidationInfo   ppValidationInfo = new PricePlanValidationInfo();

				provider.getSubscriberLifecycleFacade().changeEquipment(delegate,
						oldEquipment.getDelegate(), e1.getDelegate(), null,
						dealer.getChannelCode(), dealer.getUserCode(), requestorId,
						swapType, null, ppValidationInfo, 
						null, true, null,
						SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				logSuccess(methodName, activity, delegate, "KB equipment change successful");			  
			}

			// If lost - update to found - the swap was previously checked and allowed
			if (newIDENEquipment.isStolen()) {
				newIDENEquipment.reportFound();
			}

			provider.getInteractionManager0().subscriberChangeEquipment(this,
					oldEquipment, e1, dealerCode, salesRepCode, requestorId,
					repairId, swapType, e2);

			this.equipment = newIDENEquipment;

			if (newIDENEquipment.isSIMCard()) {
				SIMCardEquipment simcard = (SIMCardEquipment)newIDENEquipment;
				simcard.setLastMule(associatedMuleEquipment);
			}

			// 3. Retrieve Contract
			// make sure the subscriber on the contract is fresh
			// and new equipment is on the subscriber already at this point
			if (contract == null) {
				contract = (TMContract)getContract();
			}
			contract.setSubscriber(this);

			// 4. For diff equipment types, add and / or remove services to / from
			// the contract as necessary
			// for IDEN tech, all equipment has the same type.
			addRemoveServices(contract, oldEquipment, e1);

			// IDEN RIM project
			// remove RIM service
			// old equipment is handset, and new equipment is SIM / mule
			if (e1.isSIMCard() && !oldEquipment.isSIMCard()) {
				log("Calling removeNonMatchingServices. Point 4.");
				removeNonMatchingServices(e2.getDelegate(), contract);
			} else if (!e1.isSIMCard() && oldEquipment.isSIMCard()) {
				// old equipment is SIM / mule, and new equipment is handset
				log("Calling removeNonMatchingServices. Point 5.");
				removeNonMatchingServices(e1, contract);
			}
			// 5. Remove Dispatch only conflicts
			removeDispatchOnlyConflicts(contract);

			// 6. Save the contract
			try {
				activity = "save contract";
				String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
				contract.save(KBDealer[0], KBDealer[1]);
			} catch (Throwable t) {
				logFailure(methodName, activity, t, "contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed");
			}

			if (e1.isSIMCard()) {
				// associate SIM with mule
				activity = "associate SIM with mule";
				Date activationDate = new Date();
				String eventType = "ACT";

				provider.getProductEquipmentManager().setSIMMule(e1.getSerialNumber(), e2.getSerialNumber(), activationDate, eventType);

				logSuccess(methodName, activity, delegate, null);					 
			}

		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	protected String[] getKBDealer(String dealerCode, String salesRepCode) throws
	TelusAPIException {
		String[] codes = new String[2];

		if (dealer == null) {
			dealer = getCPMSDealer(dealerCode, salesRepCode);
		}
		if (dealerCode.equals(dealer.getChannelCode()) && salesRepCode.equals(dealer.getUserCode())) {
			AccountType acctType = provider.getReferenceDataManager0().getAccountType(getAccountSummary());
			codes[0] = acctType.getDefaultDealer();
			codes[1] = acctType.getDefaultSalesCode();
		}
		else {
			codes[0] = dealerCode;
			codes[1] = salesRepCode;
		}

		return codes;
	}

	public void changeEquipment(EquipmentChangeRequest request) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		changeEquipment (request, true);
	}

	protected void changeEquipment(EquipmentChangeRequest request, boolean invokeAPNFix) throws
	TelusAPIException, SerialNumberInUseException,
	InvalidEquipmentChangeException {

		if (request.getNewEquipment().isIDEN()) {
			changeEquipment( (IDENEquipment) request.getNewEquipment(), request
					.getDealerCode(), request.getSalesRepCode(), request
					.getRequestorId(), request.getRepairId(), request
					.getSwapType(), request.getAssociatedMuleEquipment());
		}
		else {
			if (request.getNewEquipment().isUSIMCard()) {
				changeHSPAEquipment(request.getNewEquipment(), 
						request.getAssociatedHandset(), 
						request.getDealerCode(), 
						request.getSalesRepCode(), 
						request.getRequestorId(), 
						request.getRepairId(), 
						request.getSwapType(), 
						false, 
						Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW,
						invokeAPNFix);
			} else {
				changeEquipment(request.getNewEquipment(), request.getDealerCode(),
						request.getSalesRepCode(), request.getRequestorId(),
						request.getRepairId(), request.getSwapType());
			}
		}
	}

	public static final String DUMMY_REPAIR_ID = "DUMMY0";

	/**
	 * Validates the eligibility of equipment swap.
	 *
	 * All possible swap types:
	 *
	 * 1. MiKE Handset to MiKE Handset 2. PCS Handset: A-A, D-D; D-A, A-D for
	 * BC, AB, SASK 3. Mule to Mule (No replacement) -- Warranty swap only 4.
	 * SIM Card to SIM Card (No loaner) 5. IDEN to SIM Card 6. SIM Card to IDEN
	 * 7. 1XRTT Handset to PCS Digital 8. PCS Digital to 1XRTT Handset 9. 1xCard
	 * to any other device and vice versa (OOM, SD, SwapTrack only) 10. RIM to
	 * any other device and vice versa (OOM, SD, SwapTrack only)
	 *
	 * @param oldEquipment
	 * @param newEquipment
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param associatedMuleEquipment
	 * @param ignoreSerialNoInUse
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 */
	private void validateSwap(TMEquipment oldEquipment,
			TMEquipment newEquipment, String requestorId,
			String repairId,
			String swapType,
			EquipmentInfo associatedMuleEquipment,
			char allowDuplicateSerialNo) throws
			TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {

		// validate general input parameters
		String newSerialNumber = newEquipment != null ? newEquipment
				.getSerialNumber() : null;

				if (requestorId == null || requestorId.trim().length() < 1
						|| swapType == null || swapType.trim().length() < 1
						|| newSerialNumber == null
						|| newSerialNumber.trim().length() < 1) {

					StringBuffer sb = new StringBuffer();

					sb.append("Mandatory field(s) are missing or invalid. <br>[dealerCode=");
					sb.append(", requestorId=");
					sb.append(requestorId);
					sb.append(", swapType=");
					sb.append(swapType);
					sb.append(", newSerialNumber=");
					sb.append(newSerialNumber);
					sb.append("]");

					String exceptionMsg = sb.toString();

					throw new InvalidEquipmentChangeException(exceptionMsg,
							InvalidEquipmentChangeException.
							MANDATORY_FIELDS_MISSING);
				}

				// check if the new equipment type is null
				if (newEquipment.getEquipmentType() == null) {
					String exceptionMsg ="The newEquipmentType is NULL - Data Problem. [newSerialNumber="+ newSerialNumber+ ", newEquipmentType="+ newEquipment.getEquipmentType() + "]";

					throw new InvalidEquipmentChangeException(exceptionMsg,
							InvalidEquipmentChangeException.
							EQUIPMENT_TYPE_IS_NULL);
				}

				// check if the new equipment's product type is the same as the subscriber's one
				if (newEquipment.getProductType() == null || !newEquipment.getProductType().equals(getProductType())) {
					String exceptionMsg = "The new equipment's ProductType, ["+ newEquipment.getProductType()+ "], is different from the subscriber's one, ["+ getProductType() + "].";

					throw new InvalidEquipmentChangeException(exceptionMsg,
							InvalidEquipmentChangeException.
							IMPOSSIBLE_SWAP_TYPES);
				}

				// get equipment DVOs
				EquipmentInfo oldEquipmentInfo = oldEquipment.getDelegate();
				EquipmentInfo newEquipmentInfo = newEquipment.getDelegate();

				if ( checkNonMuleEquipmentInUse(newEquipment, allowDuplicateSerialNo) ) {
					String exceptionMsg =
						"The new serial number is in use. [newSerialNumber = "
						+ newSerialNumber + "]";

					throw new SerialNumberInUseException(exceptionMsg, newSerialNumber);
				}

				// determine swap type
				boolean repair = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPAIR);
				boolean replacement = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPLACEMENT);
				boolean loaner = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_LOANER);

				// Fix for PROD00131124 begin
				//!!!NOTE, Mar 4,2009 M.Liao: 	
				//As business still does not have clear picture of how to handle the repair situation for: CDMA - HSPA, HSPA - CDMA, HSPA - HSPA
				//we are putting temporary work-around: skip the repair validation at all for any above swap
				//As there are multiple validation logics regarding repair, so I take a extreme shortcut: by setting repair flag to false.
				//Once business determine the validation rule, the following work-around shall be taken away.
				if ( oldEquipment.isHSPA() || newEquipment.isHSPA() ) {
					repair = false;
				}
				// Fix for PROD00131124 end


				// validate technology and product class compatibility
				checkTechnologyAndProductClassCompatibility(oldEquipment, newEquipment,swapType);

				//========================================================================
				// validation: if equipment is lost/stolen and not previously active on
				// the same account - do not allow
				//========================================================================
				if (!newEquipment.isUSIMCard()){       //	 fix  for PROD00141498
					if (newEquipment.isStolen() && !newEquipment.isInUseOnBan(getBanId(), false)) {
						throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.",InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
					}
				}

				// Equipment swaps for prepaid subscribers, delegate to CellularEquipment.isValidForPrepaid() to check Equipment prepaid eligibilty
				if (getAccount().isPrepaidConsumer()) {
					boolean equipmentValidForPrepaid = false;
					if ( newEquipment instanceof CellularEquipment )
					{
						equipmentValidForPrepaid = ((CellularEquipment) newEquipment).isValidForPrepaid();
					}
					if ( equipmentValidForPrepaid==false)
					{
						throw new InvalidEquipmentChangeException(
								"Forbidden equipment swap for prepaid account.",
								InvalidEquipmentChangeException.INVALID_SWAP_FOR_PREPAID_ACCOUNT);
					}
				}

				// validate mandatory equipment fields
				if (oldEquipment.getTechType() == null
						|| (repair && oldEquipment.getProductTypeDescription() == null)
						|| newEquipment.getTechType() == null
						|| (repair && newEquipment.getProductTypeDescription() == null)) {

					StringBuffer sb = new StringBuffer();

					sb.append("Data problem - mandatory equipment field(s) are null.<br>");
					sb.append("[<old equipment> techType=");
					sb.append(oldEquipment.getTechType());
					sb.append(", productTypeDescription=");
					sb.append(oldEquipment.getProductTypeDescription());
					sb.append("]<br>[<new equipment> techType=");
					sb.append(newEquipment.getTechType());
					sb.append(", productTypeDescription=");
					sb.append(newEquipment.getProductTypeDescription());
					sb.append("]");

					String exceptionMsg = sb.toString();
					throw new InvalidEquipmentChangeException(
							exceptionMsg,
							InvalidEquipmentChangeException.MANDATORY_EQUIPMENT_INFO_NULL);
				}

				// set mandatory equipment fields to default values if null
				if (oldEquipmentInfo.getProductCode() == null) {
					oldEquipmentInfo.setProductCode("");
				}
				if (oldEquipmentInfo.getProductStatusCode() == null) {
					oldEquipmentInfo.setProductStatusCode("");
				}
				if (oldEquipmentInfo.getProductClassCode() == null) {
					oldEquipmentInfo.setProductClassCode("");
				}
				if (oldEquipmentInfo.getProductGroupTypeCode() == null) {
					oldEquipmentInfo.setProductGroupTypeCode("");

				}
				if (newEquipmentInfo.getProductCode() == null) {
					newEquipmentInfo.setProductCode("");
				}
				if (newEquipmentInfo.getProductStatusCode() == null) {
					newEquipmentInfo.setProductStatusCode("");
				}
				if (newEquipmentInfo.getProductClassCode() == null) {
					newEquipmentInfo.setProductClassCode("");
				}
				if (newEquipmentInfo.getProductGroupTypeCode() == null) {
					newEquipmentInfo.setProductGroupTypeCode("");

					// determine swap category
				}
				boolean handToSim = oldEquipment.isHandset()
				&& newEquipment.isSIMCard();
				boolean handToMule = oldEquipment.isHandset()
				&& newEquipmentInfo.isMule();
				boolean simToSim = oldEquipment.isSIMCard() && newEquipment.isSIMCard();
				boolean simToMule = oldEquipment.isSIMCard()
				&& newEquipmentInfo.isMule();
				boolean simToHand = oldEquipment.isSIMCard()
				&& newEquipment.isHandset();
				boolean muleToSim = oldEquipmentInfo.isMule()
				&& newEquipment.isSIMCard();
				boolean muleToHand = oldEquipmentInfo.isMule()
				&& newEquipment.isHandset();

				// check for invalid combinations
				if (simToMule || handToMule || muleToSim || muleToHand) {
					String exceptionMsg =
						"Impossible swap types - [sim-to-mule, hand-to-mule, mule-to-sim, mule-to-hand]";
					throw new InvalidEquipmentChangeException(exceptionMsg,
							InvalidEquipmentChangeException.
							IMPOSSIBLE_SWAP_TYPES);
				}

				if (simToSim && (loaner || repair)) {
					String exceptionMsg = "No 'loaner repair or repair' for sim-to-sim.";
					throw new InvalidEquipmentChangeException(exceptionMsg,
							InvalidEquipmentChangeException.
							NO_LOANER_FOR_SIM2SIM);
				}

				if (!replacement
						&& !simToSim
						&& (repairId == null || repairId.trim().length() == 0
								|| repairId.trim().length() > 10 || !this
								.isLetterOrDigit(repairId))) {
					String exceptionMsg =
						"Valid repair ID is mandatory except for 'replacement' or sim-to-sim.";
					throw new InvalidEquipmentChangeException(
							exceptionMsg,
							InvalidEquipmentChangeException.
							REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM);
				}

				// repair Id must be unique for 'repair' except for sim to sim.
				// Loaner Repair IDs should not be unique as the dealer will often
				// provide the same repair ID as used for the repair.
				if (repair && !simToSim) {
					if (!repairId.equals(DUMMY_REPAIR_ID)) { // except for repair swaps
						// performed by clients

						int numOfRepairIdFound = 0;

						try {
							numOfRepairIdFound = provider.getSubscriberLifecycleHelper().getCountForRepairID(repairId.trim());
						}catch (Throwable t) {
							provider.getExceptionHandler().handleException(t);
						}

						if (numOfRepairIdFound > 0) {
							String exceptionMsg =
								"Repair ID must be unique for 'repair' except for sim to sim.";
							throw new InvalidEquipmentChangeException(
									exceptionMsg,
									InvalidEquipmentChangeException.
									REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM);
						}
					}
				}

				// old & new product types must be the same for 'repair'
				if (repair
						&& !oldEquipment.getProductTypeDescription().equals(
								newEquipment.getProductTypeDescription())) {
					String exceptionMsg =
						"Old/new product type must be the same for 'repair'. [old="
						+ oldEquipment.getProductTypeDescription()
						+ " new="
						+ newEquipment.getProductTypeDescription() + "]";
					throw new InvalidEquipmentChangeException(
							exceptionMsg,
							InvalidEquipmentChangeException.
							OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR);
				}

				// get associatedMuleSerialNumber for further verifications
				String associatedMuleSerialNumber = associatedMuleEquipment != null ?
						associatedMuleEquipment
						.getSerialNumber()
						: null;

						// associatedMuleSerialNumber is mandatory for swaps involving sim
						// (except simToSim)
						if (associatedMuleSerialNumber == null && (simToHand || handToSim)) {
							String exceptionMsg = "Associated mule serial number is mandatory for swaps involving sim, except for sim-to-sim.";
							throw new InvalidEquipmentChangeException(
									exceptionMsg,
									InvalidEquipmentChangeException.
									ASSOCIATED_MULE_MANDATORY_FOR_SIM2HAND_HAND2SIM);
						}

						// associatedMule must be a Mule for simToHand or handToSim
						if (associatedMuleSerialNumber != null
								&& !associatedMuleEquipment.isMule()
								&& (simToHand || handToSim)) {
							String exceptionMsg =
								"Associated mule must be a mule for sim-to-hand or hand-to-sim.";
							throw new InvalidEquipmentChangeException(
									exceptionMsg,
									InvalidEquipmentChangeException.
									ASSOCIATED_MULE_NOT_MULE_FOR_SIM2HAND_HAND2SIM);
						}

						// associatedMuleSerialNumber and new Serialnumber must be different
						if (associatedMuleSerialNumber != null
								&& handToSim
								&& associatedMuleSerialNumber.trim().equals(
										newSerialNumber.trim())) {
							String exceptionMsg =
								"Associated mule serial number and new serial number must be different.";
							throw new InvalidEquipmentChangeException(
									exceptionMsg,
									InvalidEquipmentChangeException.
									ASSOCIATED_MULE_AND_NEW_EQUIPMENT_SERIAL_MUST_BE_DIFF);
						}


						//Added by Roman. Walmart Equipment swaps error message: if newEquipment
						//is not available for activation, client should return it to Wal-Mart
						if (!newEquipment.isAvailableForActivation())
						{
							throw new InvalidEquipmentChangeException(
									provider.getApplicationMessage(89),
									InvalidEquipmentChangeException.UNKNOWN );
						}


						/* Holborn R2 - This piece of code will issue 2 costly WS calls and will 
						 * do nothing with the results . I commented out the code to be eventually removed
						 * or if further functionality is to be implemented , then will have to be uncommented. 

						 // check that Warranty Info (needed for populating SwapRequestInfo) is
						 // available.
						 try {
							 //Holbron R1, do not check warranty for USIM
							 if ( oldEquipment.isUSIMCard()==false) {
						 Warranty oldWarranty = oldEquipment.getWarranty();
							 }

							 if ( newEquipment.isUSIMCard()==false) {
						 Warranty newWarranty = newEquipment.getWarranty();
					 }
				 } catch (TelusException e) {
							 String exceptionMsg = "Warranty info not found.";
							 throw new InvalidEquipmentChangeException(exceptionMsg,
									 InvalidEquipmentChangeException.
									 WARRANTY_INFO_NOT_FOUND);
						 }
						 */
	}

	/**
	 * Verifies that equipment swap satisfies the business rules.
	 *
	 * @param oldEquipment
	 * @param newEquipment
	 * @param swapType
	 * @throws TelusAPIException
	 */
	private void checkTechnologyAndProductClassCompatibility(TMEquipment
			oldEquipment, TMEquipment newEquipment, String swapType) throws
			TelusAPIException {
		String errorMessage = "Incompatible technology or product class. ";

		if (this instanceof TMIDENSubscriber) { // should be also moved to the
			// TransitionMatrix
			TMContract contract = getContract0();

			if (contract.isDispatchEnabled()) {
				if ( ( (TMIDENSubscriber)this).isPTNBasedFleet()) {
					// swap cannot be performed to a legacy handset
					if ( ( (IDENEquipment) newEquipment).isLegacy()) {
						throw new InvalidEquipmentChangeException(errorMessage +
								"PTN-based: swap cannot be performed to a legacy handset.",
								InvalidEquipmentChangeException.TECH_TYPE_NOT_COMPATIBLE);
					}
				} else {
					// fix PROD00090289
					// when cross fleet is enabled, swap cannot be performed to legacy handset
					if (!contract.isCrossFleetRestricted()) {
						if (((IDENEquipment) newEquipment).isLegacy()) {
							throw new InvalidEquipmentChangeException(errorMessage +
									"Class-based::Cross-fleet-enabled: swap cannot be performed to a legacy handset.",
									InvalidEquipmentChangeException.TECH_TYPE_NOT_COMPATIBLE);
						}
					}
				}
			}
		}
		else {
			ValidationResult validationResult;

			// brand swap validation
			int oldBrandId = provider.getReferenceDataManager().getPricePlan(getPricePlan()).getBrandId();
			validationResult = provider.getBrandTransitionMatrix0().validTransition(oldBrandId, newEquipment, swapType, provider.getApplication(), false);

			if (validationResult != ValidationResult.VALID)
				throw new InvalidEquipmentChangeException(errorMessage,
						provider.getApplicationMessage(validationResult.getMessageId()), InvalidEquipmentChangeException.BRANDS_NOT_COMPATIBLE);

			// technology swap validation
			validationResult = provider.getEquipmentTransitionMatrix0().validTransition(oldEquipment, newEquipment, swapType, provider.getApplication());

			if (validationResult != ValidationResult.VALID)
				throw new InvalidEquipmentChangeException(errorMessage,
						provider.getApplicationMessage(validationResult.getMessageId()), InvalidEquipmentChangeException.TECH_TYPE_NOT_COMPATIBLE);
		}
	}

	private ApplicationMessage[] getSwapWarningMessages(Equipment newEquipment,
			boolean preserveDigitalServices) throws TelusAPIException {
		ArrayList messageList = new ArrayList();
		Contract contract = getContract();
		Equipment oldEquipment = getEquipment();

		if(newEquipment instanceof CellularDigitalEquipment){
			if (newEquipment.isCellularDigital() &&
					( (CellularDigitalEquipment) newEquipment).isPTTEnabled() &&
					!contract.isPTTServiceIncluded()) {
				messageList.add(provider.getApplicationMessage(76));

			}
		}
		if(oldEquipment instanceof CellularDigitalEquipment){
			if (oldEquipment.isCellularDigital() &&
					( (CellularDigitalEquipment) oldEquipment).isPTTEnabled() &&
					contract.isPTTServiceIncluded()) {
				messageList.add(provider.getApplicationMessage(77));


				// Digital -> Analog
			}
		}
		if (oldEquipment.isCellularDigital() && newEquipment.isAnalog()) {
			if (preserveDigitalServices) {
				messageList.add(provider.getApplicationMessage(78));
			}
			else {
				messageList.add(provider.getApplicationMessage(79));
			}
		}

		if (!newEquipment.isHSPA() && !newEquipment.getEquipmentType().equals(oldEquipment.getEquipmentType())
				&&
				! ( (oldEquipment.isAnalog() && newEquipment.isCellularDigital()) ||
						(oldEquipment.isCellularDigital() && newEquipment.isAnalog()))) {

			boolean isSupportedNewEquipment = false;

			// centralize the rule for SOC equipmentType/networkType check rule:
			// comment out the following code to use 
			// ServiceSummary.isCompatible( netwokrType, equipmentType)
			/*
			 String[] supportedEquipmentTypes = contract.getPricePlan().getEquipmentTypes(newEquipment.getNetworkType());

			 for (int i = 0;
			 i <
			 (supportedEquipmentTypes != null ? supportedEquipmentTypes.length :
				 0); i++) {				 
				 if (supportedEquipmentTypes[i].equals(Equipment.EQUIPMENT_TYPE_ALL) || 
						 supportedEquipmentTypes[i].equals(newEquipment.getEquipmentType())) {  
					 isSupportedNewEquipment = true;
					 break;	//no need to continue looping once match is found
				 }
			 }
			 */
			isSupportedNewEquipment = contract.getPricePlan().isCompatible( newEquipment.getNetworkType(), newEquipment.getEquipmentType() );			 

			if (!isSupportedNewEquipment) {
				if(oldEquipment instanceof CellularDigitalEquipment && newEquipment instanceof CellularDigitalEquipment){
					if (oldEquipment.isCellularDigital() &&
							! ( (CellularDigitalEquipment) oldEquipment).isPDA()
							&& newEquipment.isCellularDigital() &&
							( (CellularDigitalEquipment) newEquipment).isPDA())
					{
						messageList.add(provider.getApplicationMessage(85));  //changed by Dimitry
					}
					else if (oldEquipment.isCellularDigital() &&
							( (CellularDigitalEquipment) oldEquipment).isPDA()
							&& newEquipment.isCellularDigital() &&
							! ( (CellularDigitalEquipment) newEquipment).isPDA()) {
						messageList.add(provider.getApplicationMessage(86));
					}
					else {
						messageList.add(provider.getApplicationMessage(80));
					}
				}
				else
					messageList.add(provider.getApplicationMessage(80));
			}
			else if (!newEquipment.isAnalog() && !newEquipment.isCellularDigital()) {
				messageList.add(provider.getApplicationMessage(81));
			}
		}

		// Third-Part E-mail: visto -> non-visto
		if (oldEquipment.isVistoCapable() && !newEquipment.isVistoCapable()) {
			boolean isRemoveVistoServiceRequired = false;
			ContractService[] optionalServices = contract.getOptionalServices();
			for (int i = 0;
			i < (optionalServices != null ? optionalServices.length : 0); i++) {
				isRemoveVistoServiceRequired |= optionalServices[i].getService().
				isVisto();

			}
			if (isRemoveVistoServiceRequired) {
				messageList.add(provider.getApplicationMessage(83));
			}
		}

		// Third-Part E-mail: non-visto -> visto
		if (!oldEquipment.isVistoCapable() && newEquipment.isVistoCapable()) {
			boolean isAddVistoServiceRequired = true;
			ContractService[] optionalServices = contract.getOptionalServices();
			for (int i = 0;
			i < (optionalServices != null ? optionalServices.length : 0); i++) {
				isAddVistoServiceRequired &= !optionalServices[i].getService().isVisto();

			}
			if (isAddVistoServiceRequired) {
				messageList.add(provider.getApplicationMessage(84));
			}
		}

		//non GPS-> GPS (added by Roman)
		if (newEquipment.isGPS() &&
				!oldEquipment.isGPS()){
			messageList.add(provider.getApplicationMessage(87));
		}

		//GPS-> non GPS (added by Roman)
		if (!newEquipment.isGPS() &&
				oldEquipment.isGPS()){
			messageList.add(provider.getApplicationMessage(88));
		}

		return (ApplicationMessage[]) messageList.toArray(new ApplicationMessage[
		                                                                         messageList.size()]);
	}

	//=============================================================
	protected void removeDispatchOnlyConflicts(TMContract contract) throws TelusAPIException {
		log("====> removeDispatchOnlyConflicts()");

		if (!contract.isTelephonyEnabled()) { // dispatch only
			ContractService[] services = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(contract.getServices());
			ContractFeature[] features = ReferenceDataManager.Helper.retainTelephonyDisabledConflicts(contract.getFeatures());

			for (int i = 0; i < services.length; i++) {
				contract.removeService(services[i].getCode());
			}

			for (int i = 0; i < features.length; i++) {
				contract.removeFeature(features[i].getCode());
			}
		}
	}
	
	protected void removeNonMatchingServices(Equipment equipment, Contract c) throws TelusAPIException {
		log(">>>> removeNonMatchingServices()");

		ContractService[] services = c.getOptionalServices();
		if (services == null || services.length == 0) {
			log(">>>> No optional services were found.");
			return;
		}

		List serviceList = new ArrayList();
		for (int i = 0; i < services.length; i++) {
			serviceList.add(services[i].getCode());
		}
		log(">>>> optional service list " + serviceList.toString());

		ContractService[] matchingServices = ReferenceDataManager.Helper.retainServices(services, equipment);
		ContractService[] nonMatchingServices = (ContractService[]) ReferenceDataManager.Helper.difference(services, matchingServices);

		if (nonMatchingServices != null && nonMatchingServices.length > 0) {

			for (int i = 0; i < nonMatchingServices.length; i++) {
				try {
					if (serviceList.contains(nonMatchingServices[i].getCode())) {
						c.removeService(nonMatchingServices[i].getCode());
						log(">>>> optional service removed [" + nonMatchingServices[i].getCode() + "]");
					}
				} catch (TelusAPIException e) {
					log("Failed to remove the Service from the Contract, ServiceCode = " + nonMatchingServices[i].getCode());
				}
			}
		}
	}

	private void addRemoveServices(TMContract contract, Equipment oldEquipment, Equipment newEquipment) throws InvalidEquipmentChangeException, TelusAPIException {
		addRemoveServices(contract, oldEquipment, newEquipment, false);
	}

	protected void addRemoveServices(TMContract contract, Equipment oldEquipment, Equipment newEquipment, boolean preserveDigitalServices) throws InvalidEquipmentChangeException, TelusAPIException {
		String oldEquipmentType = oldEquipment.getEquipmentType();
		String newEquipmentType = newEquipment.getEquipmentType();

		log(">>>> oldEquipmentType [" + oldEquipmentType + "], oldEquipment networkType=" + oldEquipment.getNetworkType());
		log(">>>> newEquipmentType [" + newEquipmentType + "], newEquipment networkType=" + newEquipment.getNetworkType());

		if (!newEquipment.isHSPA() && !oldEquipmentType.equals(newEquipmentType)) {

			ContractService[] includedServices = contract.getIncludedServices();
			ContractService[] optionalServices = contract.getOptionalServices();

			// Add Included Services only
			log("Adding Included Services to contract.");
			Service[] predefinedIncludedServices = contract.getPricePlan().getIncludedServices();
			List predefinedServiceList = new ArrayList();
			for (int i = 0; i < predefinedIncludedServices.length; i++) {
				predefinedServiceList.add(predefinedIncludedServices[i].getCode());

			}
			log("Predefined Included Services: " + predefinedServiceList.toString());

			for (int i = 0; i < includedServices.length; i++) {
				if (predefinedServiceList.contains(includedServices[i].getCode())) {
					predefinedServiceList.remove(includedServices[i].getCode());

				}
			}
			if (predefinedServiceList.size() > 0) {
				Iterator iterator = predefinedServiceList.iterator();
				String serviceCode = null;
				while (iterator.hasNext()) {
					try {
						serviceCode = (String) iterator.next();
						Service serviceToBeAdded = contract.getPricePlan().getIncludedService(serviceCode);

						if (serviceToBeAdded.isNetworkEquipmentTypeCompatible(newEquipment)) {
							contract.addService(serviceCode, preserveDigitalServices);
							log("Successfully Added Included Service [" + serviceCode + "]");
						} else {
							log("==> Included Service [" + serviceCode + "] Does Not support New Equipment, Service will not be added");
						}
					} catch (TelusAPIException e) {
						log("Failed to add the Service to the Contract. ServiceCode [" + serviceCode + "]");
					}
				}
			} else {
				log("==> No Included Services will be added");

				// Remove Services
			}
			if (!((oldEquipmentType.equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || 
					oldEquipmentType.equals(Equipment.EQUIPMENT_TYPE_ANALOG)) && newEquipmentType.equals(Equipment.EQUIPMENT_TYPE_ANALOG) && preserveDigitalServices)) {
				log("==> Removing Services ....");

				// Remove included services
				for (int i = 0; i < includedServices.length; i++) {
					if (false == includedServices[i].getService().isNetworkEquipmentTypeCompatible(newEquipment)) {
						try {
							contract.removeService(includedServices[i].getCode());
							log("Successfully Removed Included Service [" + includedServices[i].getCode() + "] - " + includedServices[i].getDescription());
						} catch (TelusAPIException e) {
							log("Failed to remove the Service from the Contract, ServiceCode = " + includedServices[i].getCode());
						}
					}
				}

				// Remove optional services
				for (int i = 0; i < optionalServices.length; i++) {
					if (false == optionalServices[i].getService().isNetworkEquipmentTypeCompatible(newEquipment)) {
						try {
							contract.removeService(optionalServices[i].getCode());
							log("Successfully Removed Optonal Service [" + optionalServices[i].getCode() + "] - " + optionalServices[i].getDescription());
						} catch (TelusAPIException e) {
							log("Failed to remove the Service from the Contract, ServiceCode = " + optionalServices[i].getCode());
						}
					}
				}
			}
		}
	}

	private boolean isLetterOrDigit(String letterOrDigit) {

		if (letterOrDigit == null || letterOrDigit.length() < 1) {
			throw new NullPointerException(
			"A String 'letterOrDigit' parameter is required");
		}

		for (int i = 0; i < letterOrDigit.length(); i++) {
			if (!Character.isLetterOrDigit(letterOrDigit.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void resetVoiceMailPassword() throws TelusAPIException {
		assertSubscriberExists();
		try {
			provider.getSubscriberManagerBean().resetVoiceMailPassword(
					delegate.getBanId(), delegate.getSubscriberId(),
					delegate.getProductType());
		}
		catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	@Override
	public int getProvisioningPlatformId() throws TelusAPIException {
		ProvisioningPlatformTypeInfo o = provider.getReferenceDataManager0()
		.getProvisioningPlatformType(getProvisioningPlatformId0());
		if (o == null) {
			return Subscriber.PROVISIONING_PLATFORM_OTHER;
		}
		return o.getProvisioningPlatformGroup();
	}

	public int getProvisioningPlatformId0() throws TelusAPIException {
		String npanxx = getPhoneNumber().substring(0, 6);
		NumberRangeInfo numberRange = provider.getReferenceDataManager0()
		.getNumberRange(npanxx);
		if (numberRange == null) {
			return provider.getReferenceDataManager0().getProvisioningPlatformIdByCode(PROVISIONING_PLATFORM_CODE_OTHER);
		}
		LineRangeInfo lineRange = numberRange.getLineRange(getPhoneNumber());
		if (lineRange == null) {
			return provider.getReferenceDataManager0().getProvisioningPlatformIdByCode(PROVISIONING_PLATFORM_CODE_OTHER);
		}
		return lineRange.getProvisioningPlatformId();
	}

	@Override
	public ProvisioningTransaction[] getProvisioningTransactions(Date from,
			Date to) throws TelusAPIException {

		TMProvisioningTransaction[] tmTransactionInfos = new
		TMProvisioningTransaction[0];
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrieveProvisioningTransactions(
					delegate.getBanId(), delegate.getSubscriberId(), from, to);
			ProvisioningTransactionInfo[] transactionInfos = (ProvisioningTransactionInfo[])tempList.toArray(new ProvisioningTransactionInfo[tempList.size()]);
			tmTransactionInfos = new TMProvisioningTransaction[transactionInfos.length];
			for (int i = 0; i < transactionInfos.length; i++) {
				tmTransactionInfos[i] = new TMProvisioningTransaction(provider,
						transactionInfos[i]);
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return tmTransactionInfos;
	}

	@Override
	public PricePlanSummary[] getSuspensionPricePlans(String reasonCode) throws
	TelusAPIException {
		return provider.getReferenceDataManager0().findPricePlans(
				getProductType(),
				provider.getEquipmentManager0().translateEquipmentType(getEquipment()),
				getProvince(), getAccount().getAccountType(),
				getAccount().getAccountSubType(), true, false, "SUS",
				reasonCode, getBrandId(), getSubscriberNetworkType());
	}

	@Override
	public NumberGroup[] getAvailableNumberGroups() throws TelusAPIException {
		return getAvailableNumberGroups(null, null);
	}

	@Override
	public NumberGroup[] getAvailableNumberGroups(String marketArea) throws
	TelusAPIException {
		return getAvailableNumberGroups(marketArea, null);
	}

	@Override
	public NumberGroup[] getAvailableNumberGroupsGivenNumberLocation(
			String numberLocation) throws TelusAPIException {
		return getAvailableNumberGroups(null, numberLocation);
	}

	public NumberGroup[] getAvailableNumberGroups(String marketArea,
			String numberLocation) throws
			TelusAPIException {
		try {

			NumberGroupInfo[] availableNumberGroups = (NumberGroupInfo[]) Info
			.convertArrayType(provider.getReferenceDataManager()
					.getNumberGroups(
							getAccount().getAccountType(),
							getAccount().getAccountSubType(),
							getProductType(),
							String.valueOf(getEquipment()
									.getEquipmentType()), marketArea),
									NumberGroupInfo[].class);
			log("KP: before filter availableNumberGroups.count="
					+ availableNumberGroups.length);
			if (numberLocation != null) {
				availableNumberGroups = provider.getReferenceDataManager0()
				.retainNumberGroupsByNumberLocation(
						availableNumberGroups, numberLocation);
			}
			log("KP: after filter availableNumberGroups.count="
					+ availableNumberGroups.length);
			//com.telus.test.api.BaseTest.print("availableNumberGroups",
			// availableNumberGroups);

			// Apply new NumberGroup filter rules to prepaid accounts only.
			//----------------------------------------------------------------
			Account account = getAccount0();
			if (!account.isPostpaid()) {

				NumberGroup currentNumberGroup = getNumberGroup();

				if (activation) {
					availableNumberGroups = provider.getReferenceDataManager0()
					.removePostpaidPeers(availableNumberGroups);
					//com.telus.test.api.BaseTest.print("availableNumberGroups
					// (removePostpaidPeers)", availableNumberGroups);
				}
				else {
					availableNumberGroups = provider.getReferenceDataManager0()
					.retainNumberGroupsByNumberLocation(
							availableNumberGroups,
							currentNumberGroup.getNumberLocation());
					//com.telus.test.api.BaseTest.print("availableNumberGroups
					// (retainNumberGroupsByNumberLocation)",
					// availableNumberGroups);
					//3-19-2005: Win Prepaid Production Fix
					char group = (provider.getReferenceDataManager0()
							.getProvisioningPlatformType(String
									.valueOf(getProvisioningPlatformId0())))
									.getProvisioningPlatformGroup();
					//availableNumberGroups =
					// provider.getReferenceDataManager0().retainNumberGroupsByProvisioningPlatform(availableNumberGroups,
					// getProvisioningPlatformId0());
					availableNumberGroups = provider.getReferenceDataManager0()
					.retainNumberGroupsByProvisioningPlatformGroup(
							availableNumberGroups, group);

					// TODO: attach number ranges to WIN postpaid number groups
				}
				availableNumberGroups = provider.getReferenceDataManager0()
				.removeNonWinPostpaid(availableNumberGroups);
				//com.telus.test.api.BaseTest.print("availableNumberGroups
				// (removeNonWinPostpaid)", availableNumberGroups);

				availableNumberGroups = provider.getReferenceDataManager0()
				.attachNumberRangesToWinPostpaid(availableNumberGroups);
				//com.telus.test.api.BaseTest.print("availableNumberGroups
				// (attachNumberRangesToWinPostpaid)", availableNumberGroups);

				/*				Logic moved below as per PrepaidCR Changes
				 String [] provinceCodeAndGroupRestrictions = {"ON:TBY", "MB:*"};

				 if (getEquipment().isCDMA() || getEquipment().isHSPA()) {
					 availableNumberGroups = provider.getReferenceDataManager0()
					 .removeNumberGroupsByGroupAndProvinceCode(
							 availableNumberGroups, provinceCodeAndGroupRestrictions);
				 }
				 */				 
				if(getEquipment().isCDMA()){
					availableNumberGroups = provider.getReferenceDataManager0()
					.removeNumberGroupsByGroupAndProvinceCode(
							availableNumberGroups,AppConfiguration.getPrepaidCDMANGRRestrictions());
				}
			}

			return availableNumberGroups;
		}
		catch (TelusAPIException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	@Override
	public ContractChangeHistory[] getContractChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		try {
			return provider.getSubscriberLifecycleHelper().retrieveContractChangeHistory(delegate.getBanId(),
					delegate.getSubscriberId(), from, to);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	@Override
	public HandsetChangeHistory[] getHandsetChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		TMHandsetChangeHistory[] tmHistoryInfos = new TMHandsetChangeHistory[0];
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrieveHandsetChangeHistory(
					delegate.getBanId(), delegate.getSubscriberId(),from, to);
			HandsetChangeHistoryInfo[] historyInfos = (HandsetChangeHistoryInfo[])tempList.toArray(new HandsetChangeHistoryInfo[tempList.size()]);
			tmHistoryInfos = new TMHandsetChangeHistory[historyInfos.length];
			for (int i = 0; i < historyInfos.length; i++) {
				tmHistoryInfos[i] = new TMHandsetChangeHistory(provider,
						historyInfos[i]);
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return tmHistoryInfos;
	}

	@Override
	public PricePlanChangeHistory[] getPricePlanChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		TMPricePlanChangeHistory[] tmHistoryInfos = new TMPricePlanChangeHistory[0];
		try {
			PricePlanChangeHistoryInfo[] historyInfos = provider.getSubscriberLifecycleHelper().retrievePricePlanChangeHistory(
					delegate.getBanId(), delegate.getSubscriberId(), from, to);
			tmHistoryInfos = new TMPricePlanChangeHistory[historyInfos.length];
			for (int i = 0; i < historyInfos.length; i++) {
				tmHistoryInfos[i] = new TMPricePlanChangeHistory(provider,
						historyInfos[i]);
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return tmHistoryInfos;
	}

	@Override
	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		TMServiceChangeHistory[] tmHistoryInfos = new TMServiceChangeHistory[0];
		try {
			ServiceChangeHistoryInfo[] historyInfos = provider.getSubscriberLifecycleHelper().retrieveServiceChangeHistory(
					delegate.getBanId(), delegate.getSubscriberId(), from, to);
			tmHistoryInfos = new TMServiceChangeHistory[historyInfos.length];
			for (int i = 0; i < historyInfos.length; i++) {
				tmHistoryInfos[i] = new TMServiceChangeHistory(provider, historyInfos[i]);
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return tmHistoryInfos;
	}

	@Override
	public ServiceChangeHistory[] getServiceChangeHistory(Date from, Date to,
			boolean includeAllServices) throws TelusAPIException,
			HistorySearchException {
		TMServiceChangeHistory[] tmHistoryInfos = new TMServiceChangeHistory[0];
		try {
			ServiceChangeHistoryInfo[] historyInfos = provider.getSubscriberLifecycleHelper().retrieveServiceChangeHistory(
					delegate.getBanId(), delegate.getSubscriberId(), from, to, includeAllServices);
			tmHistoryInfos = new TMServiceChangeHistory[historyInfos.length];
			for (int i = 0; i < historyInfos.length; i++) {
				tmHistoryInfos[i] = new TMServiceChangeHistory(provider,
						historyInfos[i]);
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return tmHistoryInfos;
	}

	@Override
	public ResourceChangeHistory[] getResourceChangeHistory(String type,
			Date from, Date to) throws TelusAPIException,
			HistorySearchException {
		try {
			return provider.getSubscriberLifecycleHelper().retrieveResourceChangeHistory(delegate.getBanId(),
					delegate.getSubscriberId(), type, from, to);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	//    private void handleException(TelusException e, Equipment newEquipment)
	// throws TelusAPIException,
	//        SerialNumberInUseException, InvalidEquipmentChangeException {
	//            if (exceptionIds.containsKey(e.id))
	//                throw new InvalidEquipmentChangeException(e.getMessage(), e,
	// ((Integer)exceptionIds.get(e.id)).intValue());
	//            else
	//                throw new TelusAPIException(e.getMessage(), e);
	//    }

	@Override
	public void applyCredit(Card card) throws TelusAPIException {
		if (getAccount().isPostpaid()) {
			applyCredit_postpaid(card);
		}
		else {
			applyCredit_prepaid(card);
		}
	}

	public void applyCredit_prepaid(Card card) throws TelusAPIException {
		try {
			provider.getProductEquipmentManager().setCardStatus(card.getSerialNumber(), 
					Card.STATUS_PENDING, provider.getUser());

			Service[] services;
			try {
				services = card.getServices(this);
			}
			catch (Throwable e) {
				log(e.getMessage());
				services = new Service[0];
			}
			try {
				TMCard c = (TMCard) card;
				c.getDelegate().setBanId(getBanId());
				c.getDelegate().setPhoneNumber(getPhoneNumber());
				provider.getAccountLifecycleManager().applyCreditForFeatureCard(
						c.getDelegate(),
						provider.getReferenceDataManager0()
						.undecorate(services), provider.getUser());
			}
			catch (Throwable e) {
				provider.getProductEquipmentManager().setCardStatus(card.getSerialNumber(), 
						Card.STATUS_LIVE, provider.getUser());

				provider.getExceptionHandler().handleException(e);
			}
			card.setCredited(this, false);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public void applyCredit_postpaid(Card card) throws TelusAPIException {
		try {
			provider.getProductEquipmentManager().setCardStatus(card.getSerialNumber(), 
					Card.STATUS_PENDING, provider.getUser());

			try {
				Credit credit = newCredit();
				credit.setReasonCode(card.getAdjustmentCode());
				credit.setAmount(card.getAmount());
				if (card.isFeatureCard()) {
					credit.setBalanceImpactFlag(true);
				}
				credit.apply();
			}
			catch (Throwable e) {
				provider.getProductEquipmentManager().setCardStatus(card.getSerialNumber(), 
						Card.STATUS_LIVE, provider.getUser());

				provider.getExceptionHandler().handleException(e);
			}
			card.setCredited(this, false);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	@Override
	public Card[] getCards() throws TelusAPIException {
		return provider.getEquipmentManager().getCards(getPhoneNumber());
	}

	@Override
	public Card[] getCards(String cardType) throws TelusAPIException {
		return provider.getEquipmentManager().getCards(getPhoneNumber(),
				cardType);
	}

	public String getProvince() throws TelusAPIException {
		String province = getMarketProvince();
		if (province == null) {
			Logger.warning("subscriber has no privince, using account's: "
					+ getSubscriberId());
			province = getAccount().getAddress().getProvince();
		}
		return province;
	}

	private PricePlanSummary[] removeNonMatchingPricePlan(
			PricePlanSummary[] plans) throws TelusAPIException {

		Equipment equipment = getEquipment();

		if (!equipment.isHSPA()) { // do not filter for HSPA subscribers

			Set filteredPlans = new HashSet();
			Iterator iterator = null;
			PricePlanSummary pps = null;

			for (int i = 0; i < plans.length; i++) {
				filteredPlans.add(plans[i]);
			}

			if (filteredPlans.size() == 0) {
				return plans;
			}

			/**
			 * Remove Price Plans
			 */
			boolean removeRIMPlan = false;
			boolean removePTTPlan = false;
			/***** Updated for Combo plan CR- Anitha Duraisamy - start *******/			
			if(equipment.isIDEN()){

				if (equipment.isSIMCard()) {
					SIMCardEquipment sim = (SIMCardEquipment) equipment;
					MuleEquipment mule = sim.getLastMule();
					if (mule != null && mule.isIDENRIM()) {
						// OK
					} else {
						removeRIMPlan = true; // remove if IDEN and non-RIM
					}
				} 
			} else {
				// keep the service if non-IDEN
			}

			/***** Updated for Combo plan CR- Anitha Duraisamy - end *******/	

			/**
			 * PTT Price Plan
			 */
			if (equipment.isCellularDigital() && ((CellularDigitalEquipment) equipment).isPTTEnabled()) {
				// OK
			} else { // remove PTT
				removePTTPlan = true;
			}



			// Removing non-matching plans
			iterator = filteredPlans.iterator();
			while (iterator.hasNext()) {
				pps = (PricePlanSummary) iterator.next();
				if (removeRIMPlan && pps.getService().isRIM()) {
					iterator.remove();
				} else if (removePTTPlan && pps.getService().isPTT()) {
					iterator.remove();
				}
			}

			plans = (PricePlanSummary[]) filteredPlans.toArray(new PricePlanSummary[filteredPlans.size()]);
		}

		return plans;

	}

	@Override
	public PricePlanSummary[] getAvailablePricePlans() throws TelusAPIException {
		if (!activation) {
			TMContract c = getContract0();
			return getAvailablePricePlans(c.isTelephonyEnabled(), c
					.isDispatchEnabled(),
					c.isWirelessWebEnabled(),
					PricePlanSummary.CONTRACT_TERM_ALL);
		}
		else {
			return getAvailablePricePlans(true, true, true,
					PricePlanSummary.CONTRACT_TERM_ALL);
		}
	}

	/**
	 * This method is added for pass-through from getAvailablePricePlans (boolean, String)
	 * @param equipmentType
	 * @return
	 * @throws TelusAPIException
	 */
	@Override
	public PricePlanSummary[] getAvailablePricePlans(String equipmentType) throws TelusAPIException {
		if (!activation) {
			TMContract c = getContract0();
			return getAvailablePricePlans(c.isTelephonyEnabled(), c
					.isDispatchEnabled(),
					c.isWirelessWebEnabled(),
					PricePlanSummary.CONTRACT_TERM_ALL,
					equipmentType);
		}
		else {
			return getAvailablePricePlans(true, true, true,
					PricePlanSummary.CONTRACT_TERM_ALL,
					equipmentType);
		}
	}

	public PricePlanSummary[] getAvailablePricePlans(int term) throws
	TelusAPIException {
		if (!activation) {
			TMContract c = getContract0();
			return getAvailablePricePlans(c.isTelephonyEnabled(), c
					.isDispatchEnabled(),
					c.isWirelessWebEnabled(), term);
		}
		else {
			return getAvailablePricePlans(true, true, true, term);
		}
	}

	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled,
			boolean dispatchEnabled, boolean webEnabled) throws TelusAPIException {

		return getAvailablePricePlans(telephonyEnabled, dispatchEnabled,
				webEnabled, PricePlanSummary.CONTRACT_TERM_ALL);

	}


	@Override
	public PricePlanSummary[] getAvailablePricePlans(boolean getAll, String equipmentType) throws TelusAPIException {
		if (getAll) {
			if (activation) {
				return getAvailablePricePlans(true, true, true, PricePlanSummary.CONTRACT_TERM_ALL, false, false, equipmentType);
			} else {
				return getAvailablePricePlans(true, getContract0().isDispatchEnabled(), true, PricePlanSummary.CONTRACT_TERM_ALL, false, false, equipmentType);
			}
		} else {
			return getAvailablePricePlans(equipmentType);
		}
	}

	@Override
	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term, 
			boolean isCurrentOnly, boolean isActivationOnly, String equipmentType) throws TelusAPIException {

		PricePlanSummary[] pricePlans = provider.getReferenceDataManager().findPricePlans(
				getProductType(), getProvince(),
				getAccount().getAccountType(),
				getAccount().getAccountSubType(),
				equipmentType,
				isCurrentOnly, //current Price Plans
				isActivationOnly, // Available for activation only
				getBrandId(),
				term,
				getSubscriberNetworkType());

		//  Dispatch-only plans are telephony plans with telephony blocking
		// services added.

		if (isIDEN()) {
			if (dispatchEnabled && !telephonyEnabled) {
				pricePlans = (PricePlanSummary[]) ReferenceDataManager.Helper
				.removeAllNonDispatch(pricePlans);
			}

			if (!dispatchEnabled) {
				pricePlans = (PricePlanSummary[]) ReferenceDataManager.Helper
				.removeAllDispatch(pricePlans);
			}
			if (telephonyEnabled && dispatchEnabled) {
				pricePlans = (PricePlanSummary[]) ReferenceDataManager.Helper
				.removePartialResourceServices(pricePlans);
			}
		}

		pricePlans = removeNonMatchingPricePlan(pricePlans);

		return pricePlans;
	}

	public ContractService[] testContractAfterEquipmentChange(Equipment newEquipment) throws TelusAPIException {
		// TODO Implement Method
		return null;
	}

	@Override
	public PricePlan getAvailablePricePlan(String pricePlanCode) throws
	TelusAPIException {

		return provider.getReferenceDataManager().getPricePlan(
				pricePlanCode,
				provider.getEquipmentManager0().translateEquipmentType(getEquipment()),
				getProvince(), getAccount().getAccountType(),
				getAccount().getAccountSubType(), getBrandId());
	}

	@Override
	public PricePlan getAvailablePricePlan(ServiceSummary pricePlan) throws
	TelusAPIException {
		return getAvailablePricePlan(pricePlan.getCode());
	}

	@Override
	public SubscriptionRole getSubscriptionRole() throws TelusAPIException {
		if (this.subscriptionRole != null) {
			return this.subscriptionRole;
		}
		else {
			TMSubscriptionRole mySubscriptionRole = null;
			try {
				SubscriptionRoleInfo info = provider.getSubscriberLifecycleHelper().retrieveSubscriptionRole(this.getPhoneNumber());
				if (info == null) {
					return null;
				}
				mySubscriptionRole = new TMSubscriptionRole(provider, info);
				oldRole = mySubscriptionRole.getCode();
			}catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
			//Set the subscription role for previous use.
			this.subscriptionRole = mySubscriptionRole;
			return this.subscriptionRole;
		}
	}

	@Override
	public void setSubscriptionRole(SubscriptionRole subscriptionRole) {
		this.subscriptionRole = subscriptionRole;
	}

	/**
	 * Returns the termination fee. This is the fee that is charged to the
	 * account if the subscriber is cancelled. (note: this fee can be waived).
	 *
	 * <P>
	 * This method may involve a remote method call.
	 *
	 * <P>
	 * This method is not yet implemented and will result in an
	 * AbstractMethodError if called.
	 *
	 * @return double
	 * @see #cancel
	 */
	@Override
	public double getTerminationFee() throws TelusAPIException {
		return getTerminationFee(getContract0().getCommitmentStartDate(),
				getContract0().getCommitmentEndDate(), provider
				.getReferenceDataManager0().getSystemDate());

	}

	public final static double getTerminationFee(Date commitmentStart,
			Date commitmentEnd,
			Date terminationDate) {

		boolean underContract = commitmentStart != null
		&& commitmentEnd != null
		&& commitmentEnd.after(terminationDate);

		if (underContract) {
			int monthsRemaining = (commitmentEnd.getYear() - terminationDate
					.getYear())
					* 12
					- terminationDate.getMonth()
					+ commitmentEnd.getMonth();

			//-------------------------------------------------------
			// If there's a partial month of more than 15 days, add
			// it as a complete month.
			//-------------------------------------------------------
			//      if(terminationDate.getDate() < commitmentEnd.getDate()) {
			if (commitmentEnd.getDate() - terminationDate.getDate() > 15) {
				monthsRemaining++;
			}

			double fee = monthsRemaining * 20.00;
			fee = (fee == 0.00) ? 0.00 : Math.max(fee, 100.00);

			return fee;
		}
		else {
			return 0.00;
		}
	}

	/**
	 * Determines what the account status will be if this subscriber is
	 * suspended and returns it If the status will not change (or it is known it
	 * will fail), this returns the value ACCOUNT_STATUS_NO_CHANGE.
	 *
	 * @return char -- One of the AccountSummary.STATUS_XXX constants or
	 *         ACCOUNT_STATUS_NO_CHANGE
	 */
	@Override
	public char getAccountStatusChangeAfterSuspend() throws TelusAPIException {
		if (getStatus() == STATUS_SUSPENDED || getStatus() == STATUS_CANCELED) {
			return ACCOUNT_STATUS_NO_CHANGE;
		}

		/**
		 * The rules are:
		 *
		 * If there is at least one active subscriber, then the account status
		 * should remain open (the account must already be opened). If there is
		 * at least one suspended subscriber and none active, the account should
		 * be suspended. (or no change if it is already suspended) If there are
		 * only canceled subscribers then the account should be canceled. (the
		 * account must not be canceled, you can't suspend a canceled
		 * subscriber). Reserved subscribers are ignored.
		 */
		
		/**[Naresh Annabathula] , for business connect subscriber we can't depend on active subscriber count , all the seats under group will be suspended if the starter seat passed to suspend .
		 * so, using below getBusinessConnectAccountStausAfterSuspendStarterSeat to determine the account status. 
		 */
		
		if (getAccount().isPostpaidBusinessConnect() && isStarterSeat()) {
			char status = getBusinessConnectAccountStausAfterSuspendStarterSeat(getAccount().getProductSubscriberLists());
			return status;
		}
		
		int activeCount = getAccount().getAllActiveSubscribersCount();
		Logger.debug("Active sub count is " + activeCount);
		if (getStatus() == STATUS_ACTIVE) {
			activeCount--;

		}
		Logger.debug("Active sub count after adjustment is "
				+ activeCount);

		if (activeCount > 0) {
			return ACCOUNT_STATUS_NO_CHANGE;
		}
		else {
			return AccountSummary.STATUS_SUSPENDED;
		}
	}

	/**
	 * Returns what the status of the owning account will be if this subscriber
	 * is canceled, or ACCOUNT_STATUS_NO_CHANGE if the status will not change
	 * (or it is known it will fail)
	 *
	 * @return char -- One of the AccountSummary.STATUS_XXXX constants or
	 *         ACCOUNT_STATUS_NO_CHANGE.
	 */
	@Override
	public char getAccountStatusChangeAfterCancel() throws TelusAPIException {
		
		int activeCompanionSubscribersCountNowCancel = 0;
		int suspendCompanionSubscribersCountNowCancel = 0;

		// Begin comm suite Logic to check the active and suspend subscribers count.
		// Retrieve the CommunicationSuiteInfo if subscriber has any companion subscribers and cache it in local variable, if so cancel the companion subscribers and break the communicationSuite.
		getCommunicationSuite(false); //this is needed as this call may have not been triggered prior the line below.
		if(commSuiteInfo!=null && commSuiteInfo.isRetrievedAsPrimary()==true && commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList().isEmpty()==false ){
			activeCompanionSubscribersCountNowCancel += commSuiteInfo.getActiveCompanionCount();
			suspendCompanionSubscribersCountNowCancel += commSuiteInfo.getSuspendedCompanionCount();
		}
		// End Comm Suite logic
		
		if (getStatus() == STATUS_CANCELED) {
			return ACCOUNT_STATUS_NO_CHANGE;
		}

		/**
		 * The rules are:
		 *
		 * If there is at least one active subscriber, then the account status
		 * should remain the same (the account must already be opened and should
		 * remain opened). If there is at least one suspended subscriber and
		 * none active, the account should be suspended. (or remain the same if
		 * it is already suspended). If there are only canceled subscribers then
		 * the account should be canceled. Reserved subscribers are ignored.
		 */

		/**[Naresh Annabathula] , for business connect subscriber we can't depend on active/suspend subscriber count , all the seats under group will be canceled if the starter seat passed to cancel .
		 * so, using below getBusinessConnectAccountStausAfterCancelStarterSeat to determine the account status. 
		 */
		
		if (getAccount().isPostpaidBusinessConnect() && isStarterSeat()) {
			char status = getBusinessConnectAccountStausAfterCancelStarterSeat(getAccount().getProductSubscriberLists());
			return status;
		}

		int activeCount = getAccount().getAllActiveSubscribersCount();
		if (getStatus() == STATUS_ACTIVE) {
			activeCount--;

		}
		int suspendedCount = getAccount().getAllSuspendedSubscribersCount();
		if (getStatus() == STATUS_SUSPENDED) {
			suspendedCount--;

		}
		
		// Begin Comm Suite logic , check the companion subscribers count on account
		activeCount = activeCount-activeCompanionSubscribersCountNowCancel;
		suspendedCount = suspendedCount-suspendCompanionSubscribersCountNowCancel;
		//End Comm Suite logic

				
//		Logger.debug("The active amount is " + activeCount);
//		Logger.debug("The suspended amount is " + suspendedCount);
		if (activeCount > 0) {
			// if there is one active subscriber, then the account must be (and
			// remain) in opened status
			return ACCOUNT_STATUS_NO_CHANGE;
		}
		else if (suspendedCount > 0) {
			if (getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED) {
				return ACCOUNT_STATUS_NO_CHANGE;
			}
			else {
				return AccountSummary.STATUS_SUSPENDED;
			}
		}
		else {
			// both active subscriber count and suspended subscriber count will be 0.
			return AccountSummary.STATUS_CANCELED;
		}
	}

	@Override
	public void cancel(String reason, char depositReturnMethod) throws
	TelusAPIException {
		cancel(reason, depositReturnMethod, null);
	}

	//--------------------------------------------------------------------
		@Override
		public void cancel(String reason, char depositReturnMethod,String waiverReason) throws TelusAPIException {
			cancel(null, reason, depositReturnMethod, waiverReason, null);
		}

		
		@Override
		public void cancel(Date activityDate, String reason,char depositReturnMethod, String waiverReason, String memoText)throws TelusAPIException {
			cancel(activityDate, reason, depositReturnMethod, waiverReason, memoText,null);
		}
		
		//--------------------------------------------------------------------
		
		@Override
		public void cancel(Date activityDate, String reason,char depositReturnMethod, String waiverReason,String memoText,ServiceRequestHeader header) throws TelusAPIException {
			boolean shareablePricePlanPrimary = getContract().isShareablePricePlanPrimary();
			char subscriberStatusBeforeCancel = getStatus();
			clearCachedCommSuite();

			if (getStatus() == STATUS_CANCELED) {
				throw new TelusAPIException(getSubscriberId() + "Subscriber is already CANCELED");
			}

			/**
			 * @TODO This algorithm is more dependent on the algorithm in
			 *       getAccountStatusChangeAfterCancel then it should be.
			 */
			
			/**
			[Naresh Annabathula ] : introduced " seat-group" concept  for business connect project , all the BC-Subscribers should activate under group ( basically "Starter Seat" defines the grouop ( mean master seat under group),
			each ban can have multiple groups. if starter seat get canceled all the seats under the group canceled. if it is last starter seat ban will be canceled .
			ADDED validateBusinessConnectGroupStausAfterCancel..  method to validate the business connect group and seat staus , it was called from below method .
			*/
			char status;
			if (getAccount().isPostpaidBusinessConnect() && isStarterSeat()) {
				status = getBusinessConnectGroupStausAfterCancel(getAccount().getProductSubscriberLists());
			} else {
				status = getAccountStatusChangeAfterCancel();
			}
			//		Logger.debug("The account status should be " + status +" for BanId"+getBanId());
			switch (status) {
			// added below group case to cancel the group if seat is a starter seat, delegate the call to acc-ejb to cancel the list of seats.
			case AccountSummary.STATUS_SEAT_GROUP_CANCELED:
				getAccount0().cancelSubscribers(activityDate, reason, depositReturnMethod, new String[] { getPhoneNumber() }, new String[] { waiverReason }, memoText, header);
				break;
			case AccountSummary.STATUS_SUSPENDED:
				getAccount0().suspend(activityDate, "CRQ", memoText);
				cancelSub(activityDate, reason, depositReturnMethod, waiverReason, memoText, getCommunicationSuite(false), header);
				break;
			case AccountSummary.STATUS_CANCELED:
				getAccount0().cancel(getPhoneNumber(), activityDate, reason, depositReturnMethod, waiverReason, memoText);
				break;
			default:
				cancelSub(activityDate, reason, depositReturnMethod, waiverReason, memoText, getCommunicationSuite(false), header);
				break;
			}
			// delegate.setStatus(STATUS_CANCELED);
			refresh();
			getAccount().refresh();

			if (shareablePricePlanPrimary) {
				yeildShareablePricePlanPrimaryStatus(getDealerCode(), getSalesRepId());
			}

			if (needToCallSRPDS(header)) {
				reportChangeSubscriberStatus(delegate, subscriberStatusBeforeCancel, Subscriber.STATUS_CANCELED, reason, activityDate, header);
			}
	}




	//--------------------------------------------------------------------
	@Override
	public void suspend(String reason) throws TelusAPIException {
		suspend(null, reason, null);

	}

	//--------------------------------------------------------------------

		@Override
		public void suspend(Date activityDate, String reason, String memoText) throws
		TelusAPIException {

			log("Suspending sub " + getSubscriberId() + " on ban " + getAccount().getBanId());
			if (getStatus() != STATUS_ACTIVE) {
				throw new TelusAPIException("Subscriber is not ACTIVE");
			}

			boolean oldHotlined = getAccount().getFinancialHistory().isHotlined();
			char oldStatus = getStatus();
			/**
			 * @TODO this algorithm is more dependent on the algorithm in
			 *       getAccountStatusChangeAfterSuspend then it should be.
			 */
			
			/**
			[Naresh Annabathula ] : introduced " seat-group" concept  for business connect project , all the BC-Subscribers should activate under group ( basically "Starter Seat" defines the grouop ( mean master seat under group),
			each ban can have multiple groups. if starter seat get suspended all the seats under the group suspended. if it is last starter seat ban will be suspended .
			ADDED validateBusinessConnectGroupStausAfterSuspend..  method to validate the business connect group and seat staus , it was called from below method .
			*/
			char status;
			if (getAccount().isPostpaidBusinessConnect() && isStarterSeat()) {
				status =  getBusinessConnectGroupStausAfterSuspend(getAccount().getProductSubscriberLists());
			} else {
				 status = getAccountStatusChangeAfterSuspend();
				 Logger.debug("Account status after suspend " + status +"for BanId"+getBanId());
			}
			switch (status) {
			// added below group case to suspend the group if seat is a starter seat, delegate the call to acc-ejb to suspend the list of seats.
			case AccountSummary.STATUS_SEAT_GROUP_SUSPENDED:
				getAccount0().suspendSubscribers(activityDate, reason, new String[]{getPhoneNumber()}, memoText);
				break;
			case AccountSummary.STATUS_SUSPENDED:
				getAccount0().suspend(activityDate, reason, memoText);
				break;	
			default:
				suspendSub(activityDate, reason, memoText);
			}
		//    delegate.setStatus(STATUS_SUSPENDED);
		refresh();
		getAccount().refresh();

		//		 removed as per Family Plans CR requirement - Kuhan Paramsothy 03/21/2006
		//		 if (shareablePricePlanPrimary) {
		//		 yeildShareablePricePlanPrimaryStatus(getDealerCode(),
		//		 getSalesRepId());
		//		 }

		boolean statusChanged = oldStatus != getStatus();
		if (statusChanged) {
			provider.getInteractionManager0().subscriberStatusChange(this,
					oldHotlined, oldStatus);
		}

	}

	//--------------------------------------------------------------------
	@Override
	public void restore(String reason) throws TelusAPIException {
		restore(null, reason, null);
	}

	//--------------------------------------------------------------------
	@Override
	public void restore(Date activityDate, String reason, String memoText) throws
	TelusAPIException {
		String method = "restore (Date,String,String)";
		String activity="restore";
		try {

			boolean oldHotlined = getAccount().getFinancialHistory().isHotlined();
			char oldStatus = getStatus();
			ProductSubscriberList[] productSubscriberList = getAccount().getProductSubscriberLists();
			if (getStatus() == STATUS_CANCELED) {
				
				/**  Added below validation for business connect restore.
				 * 
				 * restore the subscriber from cancel state only supporting for mobile seats if account is in active status,  
				 * throwing exception if we receive request for non-mobile seats (or) mobile seats without account active  (or ) mobile seats without starter seat active on group.
				 * 
				 */
				if (getAccount().isPostpaidBusinessConnect()) {
					validateBusinessConnectSeatBeforeRestoreFromCancelState(productSubscriberList);
				}
				
				if (activityDate != null) {
					throw new TelusAPIException(
							"cannot date a resumption from cancellation: activityDate ["
							+ activityDate + "]");
				}
				provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(delegate, reason, memoText, false, PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, 0, null, new SubscriberResumedPostTaskInfo().repairCommunicationSuite(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

				if (getEquipment().isHSPA()) {
					try {
						provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(),
								getEquipment0().getDelegate().getAssociatedHandsetIMEI());

					}catch(Throwable t) {
						// if the SEMS update fails, just log it and continue
						logFailure(method, "assignEquipmentToPhoneNumber", t, "assign Equipment for phone number [" + getPhoneNumber() + "] failed; exception ignored");
					}
				}
				/*
				 * Vladimir. Rolling back the change. Postponed to the Sep 05
				 * release // make a deposit depending on the credit check
				 * results. double deposit =
				 * getAccount().getCreditCheckResult().getDeposit();
				 *
				 * if (deposit > 0) { createDeposit(deposit, "Resuming a
				 * cancelled subscriber."); }
				 */

			} else if (getStatus() == STATUS_SUSPENDED) {
				// Added below validation make sure starter seat active on group before we restore non-starter seats from suspended state.
				if (getAccount().isPostpaidBusinessConnect() && isStarterSeat() == false) {
					validateBusinessConnectSeatsBeforeRestore(productSubscriberList, new String[] { getSubscriberId() });
				}
				provider.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(delegate, activityDate, reason, memoText, false, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			} else {
				throw new TelusAPIException("SubscriberId :  "+ getSubscriberId() + " is not CANCELED or SUSPENDED");
			}

			// call RCM to assign MIN-MDN relationship after the subscriber is successfully resumed or restored 

			refresh();
			getAccount().refresh();

			boolean statusChanged = oldStatus != getStatus();
			if (statusChanged) {
				provider.getInteractionManager0().subscriberStatusChange(this,
						oldHotlined, oldStatus);
			}

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

		try {
			//			 takeShareablePricePlanSecondaryStatus(getDealerCode(),
			//			 getSalesRepId());
			//			 removed as per Family Plans CR requirement - sutha 03/21/2006
			updateShareablePricePlanStatus();
			getContract0().addShareableServicesOnOtherSubscribers(true);
		} catch (Exception e) {
			logFailure (method, activity, delegate, e, "Failed to add shareable services for the restored subscriber ["
					+ getSubscriberId() + "]");
		}
	}

	/**
	 * Persists the subscription role to CRDB/CODS. Note that an update is only
	 * attempted if the SubscriptionRole object is not null, and has been
	 * modified.
	 * <P>
	 * In the event that an error occurs, no exception will be thrown: failure
	 * to set a subscription role should not impede the activation of a
	 * subscriber.
	 */
	private void updateSubscriptionRole() {
		try {
			//Only update if the role object exists, and has been modified.
			if (subscriptionRole != null && subscriptionRole.getCode() != null
					&& !subscriptionRole.getCode().trim().equals("")
					&& subscriptionRole.isModified()) {
				provider.getSubscriberManagerBean().updateSubscriptionRole(
						this.getBanId(), this.getSubscriberId(),
						subscriptionRole.getCode(),
						subscriptionRole.getDealerCode(),
						subscriptionRole.getSalesRepCode(),
						subscriptionRole.getCsrId());
				// record role change
				provider.getInteractionManager0().subscriberChangeRole(this,
						oldRole, subscriptionRole.getCode());
			}
			else if (subscriptionRole == null) {
				System.out
				.println(
				"Calling updateSubscriptionRole() with null subscriptionRole");
			}
		}

		//Any errors that occur are to be caught and ignored.
		//Failure to set subscription role should not impede the activation of
		//a subscriber.
		catch (Throwable ex) {
			logFailure("updateSubscriptionRole","updateSubscriptionRole", ex, null );
		}
	}

	@Override
	public SubscriptionRole newSubscriptionRole() throws TelusAPIException {
		SubscriptionRoleInfo info = new SubscriptionRoleInfo();
		return new TMSubscriptionRole(provider, info);
	}

	private void setSIMMuleRelation(boolean activation, boolean activate) {
		try {
			Equipment l_equip = this.getEquipment();
			String mule = "";

			if (l_equip != null && l_equip.isSIMCard()) {
				String sim = l_equip.getSerialNumber();

				Date actDate = delegate.getStartServiceDate();
				if (actDate == null) {
					actDate = new java.util.Date();
				}

				if (this.muleNumber != null) {
					mule = this.muleNumber;
				}

				if (!isNull(mule)) {
					if (activate) {
						provider.getProductEquipmentManager().activateSIMMule(sim,
								mule, actDate);
					}
					else {
						provider.getProductEquipmentManager().setSIMMule(sim, mule,
								actDate, SIM_IMEI_RESERVED);
					}
				}
				else {
					if (activate) {
						provider.getProductEquipmentManager().startSIMMuleRelation(
								sim, actDate);
					}
				}

			}
		}
		//Any errors that occur are to be caught and ignored.
		catch (Throwable ex) {
			log(ex.getMessage());
		}
	}

	@Override
	public NumberGroup getNumberGroup() throws TelusAPIException {
		try {
			if (delegate.getNumberGroup() == null) {
				delegate.setNumberGroup(provider.getReferenceDataHelperEJB()
						.retrieveNumberGroupByPhoneNumberProductType(
								getPhoneNumber(), getProductType()));
			}

			return delegate.getNumberGroup();
		}
		catch (TelusAPIException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	@Override
	public ReasonType[] getAvailableCancellationReasons() throws
	TelusAPIException {
		return provider.getReferenceDataManager0().getActivityType("CAN")
		.getManualReasonTypes();
	}

	@Override
	public ReasonType[] getAvailableSuspensionReasons() throws TelusAPIException {
		return provider.getReferenceDataManager0().getActivityType("SUS")
		.getManualReasonTypes();
	}

	@Override
	public ReasonType[] getAvailableResumptionReasons() throws TelusAPIException {
		if (getStatus() == STATUS_CANCELED) {
			return provider.getReferenceDataManager0().getActivityType("RCL")
			.getManualReasonTypes();
		}
		else if (getStatus() == STATUS_SUSPENDED) {
			return provider.getReferenceDataManager0().getActivityType("RSP")
			.getManualReasonTypes();
		}
		else {
			return new ReasonType[0];
		}
	}

	private void cancelSub(Date activityDate, String reason,char depositReturnMethod, String waiverReason,String memoText,CommunicationSuiteInfo  commSuiteInfo,
			ServiceRequestHeader header) throws TelusAPIException {
		String methodName = "cancelSub(Date,String,char,String,String)";
		String activity="cancelSubscriber";
		try {
			log("canceling the subscriber [" + getSubscriberId()+ "] on BAN [" + getAccount().getBanId() + "]");

			provider.getSubscriberLifecycleFacade().cancelSubscriber(delegate,activityDate, reason, String.valueOf(depositReturnMethod),waiverReason, memoText, 
					provider.getAccountNotificationSuppressionIndicator(getBanId()), null,commSuiteInfo,header,SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

			// call RCM to release the MIN after the subscriber is cancelled			 
			if (isPCS() && isFutureDated(activityDate) == false) {
				String networkType = getEquipment0().getNetworkType();
				activity="releaseTNResources";
				try {
					provider.getSubscriberLifecycleFacade().releaseTNResources(getPhoneNumber(), networkType);
					logSuccess(methodName, activity,  " PhoneNumber["+getPhoneNumber()+"],NetworkType["+networkType+"]");		
				} catch(Throwable t) {
					// if the RCM update fails, just log it and continue
					logFailure (methodName, activity, t, " PhoneNumber["+getPhoneNumber()+"],NetworkType["+networkType+"]");
				}
			}

			delegate.setStatus(STATUS_CANCELED);
			refresh();

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	private void suspendSub(Date activityDate, String reason, String memoText) throws TelusAPIException {
		try {
			SubscriberLifecycleFacade subLifeCycleFacade= provider.getSubscriberLifecycleFacade();
			log("suspending subscriber [" + getSubscriberId()+ "] on BAN [" + getAccount().getBanId() + "]");
			subLifeCycleFacade.suspendSubscriber(delegate,activityDate, reason, memoText, SessionUtil.getSessionId(subLifeCycleFacade));
			delegate.setStatus(STATUS_SUSPENDED);
			refresh();
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public void yeildShareablePricePlanPrimaryStatus(String dealerCode,String salesRepCode) throws TelusAPIException {
		yeildShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, null);
	}

	public void yeildShareablePricePlanPrimaryStatus(String dealerCode, String salesRepCode, Date newContractDate) throws TelusAPIException {

		log("TMSubscriber(" + getPhoneNumber() + ").yeildShareablePricePlanPrimaryStatus()");
		TMContract c = getContract0();
		if (c.getPricePlan().isSharable()) {
			ShareablePricePlan shareablePricePlan = (ShareablePricePlan)c.getPricePlan();
			PricePlanSubscriberCount count = accountSummary.getAccount0().getShareablePricePlanSubscriberCount(shareablePricePlan);
			if (count != null) {
				String[] phoneNumbers = new String[count.getActiveSubscribers().length + count.getReservedSubscribers().length];
				System.arraycopy(count.getActiveSubscribers(), 0, phoneNumbers, 0, count.getActiveSubscribers().length);
				System.arraycopy(count.getReservedSubscribers(), 0, phoneNumbers, count.getActiveSubscribers().length,
						count.getReservedSubscribers().length);

				for (int i = 0; i < phoneNumbers.length; i++) {
					log("            -phoneNumbers[" + i + "]=" + phoneNumbers[i]);
					if (!phoneNumbers[i].equals(getPhoneNumber())) {
						TMSubscriber s = (TMSubscriber)provider.getAccountManager0().findSubscriberByPhoneNumber(phoneNumbers[i]);
						s.takeShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, newContractDate);
						return;
					}
				}
			}
		}
	}

	public void takeShareablePricePlanPrimaryStatus(String dealerCode,String salesRepCode) throws TelusAPIException {
		takeShareablePricePlanPrimaryStatus(dealerCode, salesRepCode, null);
	}

	public void takeShareablePricePlanPrimaryStatus(String dealerCode,String salesRepCode,Date newContractDate) throws TelusAPIException {
		log("        TMSubscriber(" + getPhoneNumber()+ ").takeShareablePricePlanPrimaryStatus()");
		TMContract c = getContract0();

		if (c.getPricePlan().isSharable()) {
			ShareablePricePlan shareablePricePlan = (ShareablePricePlan) c.getPricePlan();
			if (c.containsService(shareablePricePlan.getSecondarySubscriberService())) {
				// Prod Fix:
				ContractService secondarySubSoc = c.getService(shareablePricePlan.getSecondarySubscriberService());

				if (newContractDate != null && isFutureDated (newContractDate)) { // new price plan is future dated
					secondarySubSoc.setExpiryDate(newContractDate);
				} // price plan is not future dated
				else {
					c.removeService(secondarySubSoc.getCode());
				}
				c.save(dealerCode, salesRepCode);
			}
		}
	}

	public void takeShareablePricePlanSecondaryStatus(String dealerCode, String salesRepCode) throws TelusAPIException {

		log(" TMSubscriber(" + getPhoneNumber() + ").takeShareablePricePlanSecondaryStatus()");
		TMContract c = getContract0();
		if (c.getPricePlan().isSharable()) {
			ShareablePricePlan shareablePricePlan = (ShareablePricePlan) c.getPricePlan();
			PricePlanSubscriberCount count = accountSummary.getAccount0().getShareablePricePlanSubscriberCount(shareablePricePlan);
			if (count != null && (count.getActiveSubscribers().length + count.getReservedSubscribers().length + count.getFutureDatedSubscribers().length) > 1) {
				if (shareablePricePlan.getSecondarySubscriberService() != null) {
					c.addService(shareablePricePlan.getSecondarySubscriberService());
					c.save(dealerCode, salesRepCode);
				}
			}
		}
	}

	//-----------------------------------------------------------
	// Remove the secondary service from another active subscriber
	// sharing this plan if this subscriber was primary on a shareable
	// priceplan.
	//-----------------------------------------------------------
	/**
	 * * Returns tax exemption details: * whether subscriber is GST, PST or HST
	 * exempt, and the respective exemption expiry dates *
	 *
	 * @return TaxExemption *
	 * @throws TelusAPIException
	 */
	@Override
	public TaxExemption getTaxExemption() throws TelusAPIException {
		return new TMSubscriberTaxExemption(provider, delegate);
	}

	@Override
	public Memo getLastMemo(String memoType) throws TelusAPIException {

		try {
			MemoInfo info = provider.getSubscriberLifecycleHelper().retrieveLastMemo(delegate.getBanId(), delegate.getSubscriberId(), memoType);
			return new TMMemo(provider, info);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	@Override
	public SubscriberHistory[] getHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {
		try {
			List tempList=provider.getSubscriberLifecycleHelper().retrieveSubscriberHistory(delegate.getBanId(), 
					delegate.getSubscriberId(), from, to);
			return (SubscriberHistory[])tempList.toArray(new SubscriberHistory[tempList.size()]);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	/**
	 * Moves subscriber from current BAN to new BAN (TOWN) Subscriber and BANs
	 * need to be active.
	 *
	 * <P>
	 * This method may involve a remote method call.
	 *
	 * Only subscribers with active status can be moved from BAN to BAN and the
	 * target BAN for moving a subscriber can be in any status except ‘close’.
	 * transferOwnership is set to true - User name and user address of the
	 * moved subscriber will default to the billing name and billing address of
	 * the target BAN. Credit evaluation is required for the target BAN to
	 * create a new deposit accordingly. transferOwnership is set to false -The
	 * user name and user address is not affected by the move and credit
	 * evaluation need not be performed. However, a credit class has to exist in
	 * the target BAN. The deposit held in the source BAN on behalf of the moved
	 * subscriber will be moved to the target BAN.
	 */
	@Override
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText) throws TelusAPIException {
		
		try {
			log("Moving subscriber " + getSubscriberId() + " on ban " + getAccount().getBanId() + " to ban " + account.getBanId());
			
			// Restrict subscribers moving to or from Business Connect accounts from using this method
			// Moving a Business Connect subscriber requires a mandatory priceplan change - use migrate seat functionality instead
			if (getAccount().isPostpaidBusinessConnect() || account.isPostpaidBusinessConnect()) {
				throw new TelusAPIException("Business Connect subscribers are ineligible for move or TOWN. Please use migrate seat functionality instead.");
			}

			boolean shareablePricePlanPrimary = getContract().isShareablePricePlanPrimary();

			// Activity Date should be logical date
			Date activityDate = provider.getReferenceDataManager().getLogicalDate();
			String sessionId = 	SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade());
			
			provider.getSubscriberLifecycleFacade().moveSubscriber(delegate,
					account.getBanId(), activityDate, transferOwnership,
					reasonCode, memoText, 
					provider.getAccountNotificationSuppressionIndicator(getBanId()), null, //CDR confirmation notificaiton change
					sessionId);

			// If this is a last active subscriber on account, cancel the
			// account
			// Commented out based on AMDOCS Tuxedo documentation that indicates
			// that account will be cancelled by AMDOCS

			// char status = getAccountStatusChangeAfterCancel();
			// Logger.debug("The account status should be " + status);
			// if (status == AccountSummary.STATUS_CANCELED) {
			// getAccount0().cancel(reasonCode, 'O', null);
			// }
			if (shareablePricePlanPrimary) {
				yeildShareablePricePlanPrimaryStatus(getDealerCode(), getSalesRepId());
			}

			TMSubscriber s = (TMSubscriber) provider.getAccountManager0().findSubscriberByPhoneNumber(this.getPhoneNumber());			
			s.updateShareablePricePlanStatus();
			s.getContract0().addShareableServicesOnOtherSubscribers(true);
			
			delegate.setStatus(STATUS_CANCELED);

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}
	
	/**
	 * Only subscribers with active status can be moved from BAN to BAN and the
	 * target BAN for moving a subscriber can be in any status except ‘close’.
	 * transferOwnership is set to true - User name and user address of the
	 * moved subscriber will default to the billing name and billing address of
	 * the target BAN. Credit evaluation is required for the target BAN to
	 * create a new deposit accordingly. transferOwnership is set to false -The
	 * user name and user address is not affected by the move and credit
	 * evaluation need not be performed. However, a credit class has to exist in
	 * the target BAN. The deposit held in the source BAN on behalf of the moved
	 * subscriber will be moved to the target BAN.
	 *
	 * @exception TelusAPIException
	 * @param account - the Account
	 * @param transferOwnership - transfer Ownership
	 * @param reasonCode - the reason Code
	 * @param memoText - the Memo Text
	 * @param dealerCode - the Knowbility Dealer Code
	 * @param salesRepCode - the Knowbility sales Rep Code
	 */
	@Override
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode) throws TelusAPIException {
		
		try {
			// Restrict subscribers moving to or from Business Connect accounts from using this method
			// Moving a Business Connect subscriber requires a mandatory priceplan change - use migrate seat functionality instead
			if (getAccount().isPostpaidBusinessConnect() || account.isPostpaidBusinessConnect()) {
				throw new TelusAPIException("Business Connect subscribers are ineligible for move or TOWN. Please use migrate seat functionality instead.");
			}
			
			boolean shareablePricePlanPrimary = getContract().isShareablePricePlanPrimary();

			// Activity Date should be logical date
			Date activityDate = provider.getReferenceDataManager().getLogicalDate();
			String sessionId = 	SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade());
			
			provider.getSubscriberLifecycleFacade().moveSubscriber(delegate,
					account.getBanId(), activityDate, transferOwnership,
					reasonCode, memoText, dealerCode, salesRepCode, 
					provider.getAccountNotificationSuppressionIndicator(getBanId()), null, //CDR confirmation notificaiton change
					sessionId);

			// If this is a last active subscriber on account, cancel the account
			// Commented out based on AMDOCS Tuxedo documentation that indicates
			// that account will be cancelled by AMDOCS

			// char status = getAccountStatusChangeAfterCancel();
			// Logger.debug("The account status should be " + status);
			// if (status == AccountSummary.STATUS_CANCELED) {
			// getAccount0().cancel(reasonCode, 'O', null);
			// }
			if (shareablePricePlanPrimary) {
				yeildShareablePricePlanPrimaryStatus(getDealerCode(), getSalesRepId());
			}

			TMSubscriber s = (TMSubscriber) provider.getAccountManager0().findSubscriberByPhoneNumber(this.getPhoneNumber());
			s.updateShareablePricePlanStatus();
			s.getContract0().addShareableServicesOnOtherSubscribers(true);

			delegate.setStatus(STATUS_CANCELED);

		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public boolean waitUntilProvisioned() throws TelusAPIException {
		return true;
	}

	private static final String WarrantyFormFax = "warrFormFax_";
	private static final String WarrantyFormEmail = "warrFormEmail_";
	private static final String PAPForm = "pap_form_";
	private static final String Logo = "logo_";	 
	private static final String PDF_EXTENSION = ".pdf";
	private static final String XSL_EXTENSION = ".xsl";
	private static final String JPG_EXTENSION = ".jpg";

	@Override
	public void sendFax(final int form, String faxNumber, String language) throws
	EquipmentWarrantyNotAvailableException, TelusAPIException {

		if (form == 0 || faxNumber == null || language == null) {
			throw new java.lang.IllegalArgumentException(
			"{final int form, String faxNumber, String language} cannot be null");
		}

		if (Subscriber.FORM_EQUIPMENT_WARRANTY == form) {

			EquipmentWarranty warranty = new EquipmentWarranty(this, language);
			warranty.setFaxNumber(faxNumber);
			Document document = domTransformer.newWarrantyDocument(warranty);
			byte[] content = domTransformer.transform(document,
					WarrantyFormFax + getBrandId() + XSL_EXTENSION, language);
			mailer.sendFax(faxNumber, content, null,
					new URL[] {domTransformer.getRemoteResourceURL(Logo + getBrandId() + JPG_EXTENSION, "images")});

		} else if (Subscriber.FORM_PAP == form) {

			byte[] papHeader = (provider.getReferenceDataManager().getBrand(getBrandId()).getDescription()
					+ " Client Authorization for Automatic Account Withdrawals").getBytes();
			mailer.sendFax(faxNumber, papHeader, 
					new URL[] {domTransformer.getRemoteResourceURL(PAPForm + getBrandId() + PDF_EXTENSION, language)}
			);
		}
	}

	@Override
	public void sendEmail(final int form, String email, String language) throws
	EquipmentWarrantyNotAvailableException, TelusAPIException {

		if (form == 0 || email == null || language == null) {
			throw new java.lang.IllegalArgumentException(
			"{final int form, String email, String language} cannot be null");
		}

		InternetAddress sender;
		InternetAddress[] recipients;
		String brandDesc = provider.getReferenceDataManager().getBrand(getBrandId()).getDescription();

		try {
			sender = new InternetAddress((String)AppConfiguration.getReplyToEmailAddressKeyMap().get(String.valueOf(getBrandId())));			 
			recipients = new InternetAddress[] {new InternetAddress(email)};

		} catch (AddressException ae) {
			throw new TelusAPIException(ae);
		}

		if (Subscriber.FORM_EQUIPMENT_WARRANTY == form) {

			EquipmentWarranty warranty = new EquipmentWarranty(this, language);
			Document document = domTransformer.newWarrantyDocument(warranty);
			byte[] content = domTransformer.transform(document,
					WarrantyFormEmail + getBrandId() + XSL_EXTENSION, language);

			mailer.sendEmail(
					sender,
					recipients,
					brandDesc + " Warranty Validation Certificate for Repair Service Only",
					content,
					null,
					new URL[] {domTransformer.getRemoteResourceURL(Logo + getBrandId() + JPG_EXTENSION, "images")});

		} else if (Subscriber.FORM_PAP == form) {

			mailer.sendEmail(
					sender,
					recipients,
					brandDesc + " Client Authorization for Automatic Account Withdrawals",
					(brandDesc + " Client Authorization for Automatic Account Withdrawals Form Attached").getBytes(),
					new URL[] {domTransformer.getRemoteResourceURL(
							PAPForm + getBrandId() + PDF_EXTENSION, language)});
		}
	}
	
	@Override
	public void refreshSwitch() throws TelusAPIException {
		try {
			provider.getSubscriberManagerBean().refreshSwitch(
					delegate.getBanId(), delegate.getSubscriberId(),
					delegate.getProductType());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	@Override
	public String getProvisioningStatus() throws TelusAPIException {
		try {
			return provider.getSubscriberLifecycleHelper().retrieveSubscriberProvisioningStatus(delegate.getBanId(),
					delegate.getSubscriberId());
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public void refrshCancellationPenalty() throws TelusAPIException {
		cancellationPenalty = null; // will be retrieved on next request
	}

	/**
	 * This method is re-instated as part of Handset Transparency April 2011 release along with the decommissioning of Billing Inquiry Service.
	 */
	@Override
	public CancellationPenalty getCancellationPenalty() throws TelusAPIException {
		try {
			if (cancellationPenalty == null) {
				cancellationPenalty = provider.getSubscriberManagerBean().retrieveCancellationPenalty(getBanId(), getSubscriberId(), getProductType());
			}

			return cancellationPenalty;
			
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	@Override
	public String getSupportLevel() throws TelusAPIException {

		contractFeatures = getContract0().getFeatures(true);

		if (ReferenceDataManager.Helper.retainByCategoryCode(contractFeatures,
				Feature.CATEGORY_CODE_CORPORATE_HELP_DESK).length > 0) {
			return Subscriber.SUPPORT_LEVEL_CORPORATE_HELP_DESK;
		}
		else if (ReferenceDataManager.Helper.retainByCategoryCode(contractFeatures,
				Feature.CATEGORY_CODE_ENHANCED_CALL_CENTRE).length > 0) {
			return Subscriber.SUPPORT_LEVEL_ENHANCED_CALL_CENTRE;
		}
		else if (ReferenceDataManager.Helper.retainByCategoryCode(contractFeatures,
				Feature.CATEGORY_CODE_BASIC_SERVICE).length > 0) {
			return Subscriber.SUPPORT_LEVEL__BASIC;
		}
		else {
			return Subscriber.SUPPORT_LEVEL_STANDARD;
		}
	}

	@Override
	public InvoiceCallSortOrderType getInvoiceCallSortOrder() throws TelusAPIException {
		return provider.getReferenceDataManager0().getInvoiceCallSortOrderType(delegate.getInvoiceCallSortOrderCode());
	}

	@Override
	public void setInvoiceCallSortOrder(String invoiceCallSortOrder) throws TelusAPIException {
		delegate.setInvoiceCallSortOrderCode(invoiceCallSortOrder);
	}

	@Override
	public void createDeposit(double Amount, String memoText) throws TelusAPIException {
		try {
			provider.getSubscriberManagerBean().createDeposit(delegate, Amount, memoText);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

//	@Override
//	public LMSLetterRequest newLMSLetterRequest(Letter letter) throws
//	TelusAPIException {
//		return getAccount0().newLMSLetterRequest(letter, this);
//	}

	@Override
	public DepositHistory[] getDepositHistory() throws TelusAPIException, HistorySearchException {
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrieveDepositHistory(getBanId(), getSubscriberId());
			return (DepositHistory[]) tempList.toArray(new DepositHistory[tempList.size()]);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	@Override
	public DiscountPlan[] getAvailableDiscountPlans() throws TelusAPIException {
		DiscountPlan[] discountPlans = provider.getReferenceDataManager()
		.getDiscountPlans(true); //current Discount Plans

		String subscriberProdType = getProductType();

		ArrayList discPlansForSubscrList = new ArrayList();
		int subBrandId = delegate.getBrandId();
		boolean found = false;

		for (int i = 0; i < discountPlans.length; i++) {
			found = false;
			if (subBrandId == 0)
				found = true;
			else {
				int[] discountBrandIDs =  discountPlans[i].getDiscountBrandIDs();
				if (discountBrandIDs == null || discountBrandIDs.length == 0 || 
						(discountBrandIDs.length == 1 && discountBrandIDs[0] == 0 ))
					found = true;
				else {
					for (int j = 0; j < discountBrandIDs.length; j++) {
						if (discountBrandIDs[j] == subBrandId){
							found = true;
							break;
						}
					}
				}
			}

			if (!discountPlans[i].getLevel().equals("B") && found &&
					(discountPlans[i].getProductType().equals( subscriberProdType) || discountPlans[i].getProductType().equals("A"))) {
				discPlansForSubscrList.add(discountPlans[i]);
			}
		}

		return (DiscountPlan[]) discPlansForSubscrList.toArray(new DiscountPlan[discPlansForSubscrList.size()]);
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	@Deprecated
	@Override
	public CallList getBilledCalls(int billSeqNo) throws TelusAPIException {
		return getBilledCalls(billSeqNo, CallList.CALL_TYPE_ALL, null, null, false);
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	@Deprecated
	@Override
	public CallList getBilledCalls(int billSeqNo, char callType) throws TelusAPIException {
		return getBilledCalls(billSeqNo, callType, null, null, false);
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	@Deprecated
	@Override
	public CallList getBilledCalls(int billSeqNo, Date from, Date to,
			boolean getAll) throws TelusAPIException {
		return getBilledCalls(billSeqNo, CallList.CALL_TYPE_ALL, from, to, getAll);
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	@Deprecated
	@Override
	public CallList getBilledCalls(int billSeqNo, char callType, Date from,
			Date to, boolean getAll) throws
			TelusAPIException {
		try {
			return decorate(billSeqNo,
					provider.getSubscriberManagerBean().
					retrieveBilledCallsList(getBanId(), getSubscriberId(),
							getProductType(), billSeqNo,
							callType, from, to, getAll));
		}
		catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	/**
	 * @deprecated
	 * Deprecated as of October 2016, please use cis-wls-rated-airtime-usage-inquiry-svc web service instead.
	 */
	@Deprecated
	@Override
	public CallList getUnbilledCalls() throws TelusAPIException {
		try {
			// Unbilled calls have a bill sequence number = 0
			return decorate(0, provider.getSubscriberManagerBean().retrieveUnbilledCallsList(getBanId(), getSubscriberId(), getProductType(), null, null, true));
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	//  Decorate CallList to get service methods
	private CallList decorate(int billSeqNo, CallList callList) throws TelusAPIException {
		TMCallList tmCallList = new TMCallList(provider, (CallListInfo) callList, getBanId(), getSubscriberId(), getProductType(), billSeqNo);
		return tmCallList;
	}

	public Credit[] getPromotionalCredits() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	@Override
	public Credit[] getCredits(Date from, Date to, String billState) throws TelusAPIException {
		return getCreditsByReasonCode(from, to, billState, null);
	}

	@Override
	public Credit[] getCreditsByReasonCode(Date from, Date to, String billState, String reasonCode) throws TelusAPIException {
		return getCreditsByReasonCode(from, to, billState, reasonCode, provider.getUser());
	}
	
	//Overload method that will allow the consumers to pass the kb operatorId to retrieve credits according to the operator Id
	//If NULL is passed, there's no filteration done. Query will return all credits regardless of kb operator Id.
	@Override
	public Credit[] getCreditsByReasonCode(Date from, Date to,
			String billState, String reasonCode, String knowbilityOperatorId) throws
			TelusAPIException {
		SearchResultsInfo searchResult =null;
		try {
			searchResult =provider.getAccountInformationHelper().retrieveCredits(getBanId(), from, to, billState, knowbilityOperatorId,
					reasonCode, ChargeType.CHARGE_LEVEL_SUBSCRIBER, getSubscriberId(), 1000);

			return decorate( (CreditInfo[]) searchResult.getItems());
		}
		catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	private Credit[] decorate(CreditInfo[] credits) throws TelusAPIException {
		TMCredit[] tmCredits = new TMCredit[credits.length];
		for (int i = 0; i < credits.length; i++) {
			TMCredit tmCredit = new TMCredit(provider, credits[i],
					null);
			tmCredits[i] = tmCredit;
		}
		return tmCredits;
	}

	@Override
	public Address getAddress() throws TelusAPIException,AddressNotFoundException {
		return getAddress(false);
	}

	@Override
	public Address getAddress(boolean refresh) throws TelusAPIException, AddressNotFoundException {

		AddressInfo addressInfo = null;
		if (delegate.getAddress() == null || refresh) {
			try {
				addressInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriberAddress(getBanId(), getSubscriberId());
				delegate.setAddress(addressInfo);
				if (addressInfo != null) {
					address = new TMAddress(provider, addressInfo);
				} else {
					throw new AddressNotFoundException("Address not found for subscriber " + getSubscriberId());
				}
			} catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
		}

		return address;
	}

	@Override
	public void setAddress(Address newAddress) throws TelusAPIException {
		AddressInfo newAddressInfo;
		if (newAddress instanceof TMAddress) {
			newAddressInfo = ((TMAddress) newAddress).getDelegate();
		} else {
			newAddressInfo = (AddressInfo) newAddress;
		}
		delegate.setAddress(newAddressInfo);
	}

	private void updateAddress() {
		if (delegate.getAddress() != null) {
			try {
				provider.getSubscriberManagerBean().updateAddress(getBanId(), getSubscriberId(), getProductType(), (AddressInfo) delegate.getAddress());
				delegate.setAddress(null);
			} catch (Throwable e) {
				log("Failed to update subscriber's address...");
			}
		}
	}

	@Override
	public void removeFutureDatedPricePlanChange() throws TelusAPIException {
		try {
			provider.getSubscriberManagerBean().deleteFutureDatedPricePlan(
					getBanId(), getSubscriberId(), getProductType());
		}
		catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	/**
	 * Deprecated this method since SD will call SEMS new Web Service for PCS equipment change history. 
	 * 
	 * @deprecated
	 */
	@Deprecated
	@Override
	public EquipmentChangeHistory[] getEquipmentChangeHistory(Date from, Date to) throws
	TelusAPIException, HistorySearchException {

		TMEquipmentChangeHistory[] tmEquipmentHistory = new
		TMEquipmentChangeHistory[0];
		try {
			List tempList = provider.getSubscriberLifecycleHelper().retrieveEquipmentChangeHistory(
					delegate.getBanId(), delegate.getSubscriberId(), from, to);
			EquipmentChangeHistoryInfo[] equipmentHistory =  (EquipmentChangeHistoryInfo[])tempList.toArray(new EquipmentChangeHistoryInfo[tempList.size()]);

			tmEquipmentHistory = new TMEquipmentChangeHistory[equipmentHistory.length];
			for (int i = 0; i < equipmentHistory.length; i++) {
				tmEquipmentHistory[i] = new TMEquipmentChangeHistory(provider, equipmentHistory[i]);
			}
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return tmEquipmentHistory;
	}

	@Override
	public InvoiceTax getInvoiceTax(int billSeqNo) throws TelusAPIException {

		// TODO:
		InvoiceTax tax = null;

		try {
			tax = provider.getSubscriberLifecycleHelper().retrieveInvoiceTaxInfo(delegate.getBanId(), delegate.getSubscriberId(), billSeqNo);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}

		return tax;
	}

	@Override
	public UsageProfileListsSummary getUsageProfileListsSummary(int billSeqNo) throws TelusAPIException {

		try {
			return provider.getSubscriberManagerBean().getUsageProfileListsSummary(getBanId(), getSubscriberId(), billSeqNo, getProductType());
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	@Override
	public boolean isHotlined() {
		return delegate.isHotlined();
	}

	public void updateShareablePricePlanStatus() {

//		log("in updateShareablePricePlanStatus");

		try {
			if (contract == null) {
				contract = getContract0(true, true);
			}

			if (!contract.getPricePlan().isSharable()) {
				return;
			}

			PricePlanSubscriberCount pricePlanSubscriberCount = 
				accountSummary.getAccount0().getShareablePricePlanSubscriberCount((ShareablePricePlan)contract.getPricePlan());

			String shareableServiceCode = ((ShareablePricePlan)contract.getPricePlan()).getSecondarySubscriberService();
			ServiceSubscriberCount[] secondaryPPSubscriberCount = accountSummary.getAccount0().getServiceSubscriberCounts(
					new String[] {shareableServiceCode}, false);

			int numberOfSubscriberContianSecondarayService =
				secondaryPPSubscriberCount.length > 0 ? secondaryPPSubscriberCount[0].getActiveSubscribers().length : 0;
//				log("shareableServiceCode=" +	provider.getReferenceDataManager0().getRegularService(shareableServiceCode));
//				log("numberOfSubscriberContianSecondarayService=" + numberOfSubscriberContianSecondarayService);
//				log("pricePlanSubscriberCount.getActiveSubscribers()=" + pricePlanSubscriberCount.getActiveSubscribers().length);
				if (pricePlanSubscriberCount.getActiveSubscribers().length == numberOfSubscriberContianSecondarayService) {
//					log("found that shareable service code should be removed...removing");
					contract.removeService(shareableServiceCode);
					contract.save();
				} else if ((pricePlanSubscriberCount.getActiveSubscribers().length - numberOfSubscriberContianSecondarayService) > 1) {
					if (!contract.containsService(shareableServiceCode)) {
//						log("found that shareable service code should be added...adding");
						contract.addService(shareableServiceCode);
						contract.save();
					}
				}

		} catch (TelusAPIException tapie) {
			log("updateShareablePricePlanStatus exception =" + tapie.getMessage());
		}
	}

	@Override
	public boolean isValidMigrationForPhoneNumber(MigrationType migrationType, String sourceNetworkType, String targetNetworkType) throws TelusAPIException {
		boolean phoneNumberEligible = false;
		String portVisibilityType = null;

		String migrationTypeCode = migrationType.getCode(); 
		if (MigrationType.PCS_POST_TO_PCSPRE.equals(migrationTypeCode)) {
			if ( NetworkType.NETWORK_TYPE_HSPA.equals(targetNetworkType))  {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2H;
			}else {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2C;
			}
			int platformId = getProvisioningPlatformId0();
			ProvisioningPlatformTypeInfo platformInfo =
				provider.getReferenceDataManager0().getProvisioningPlatformType(platformId);
			if (platformInfo == null) {
				throw new InvalidMigrationRequestException(
						"Invalid phone number - unable to retrieve provision platform info for (phone:" + getPhoneNumber()
						+ ", platformId:" + platformId + ")", InvalidMigrationRequestException.REASON_INVALID_PHONE_NUMBER);
			}

			char platformGroup = platformInfo.getProvisioningPlatformGroup();
			if (!(platformGroup == 'W')) {
				phoneNumberEligible = false;
				return phoneNumberEligible;
			} else {
				phoneNumberEligible = true;
			}

		} else if (MigrationType.PCS_PRE_TO_PCSPOST.equals(migrationTypeCode)) {
			phoneNumberEligible = true;
			if ( NetworkType.NETWORK_TYPE_HSPA.equals(targetNetworkType))  {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2H;
			}else {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2P_2C;
			}
		} else if (MigrationType.IDEN_TO_PCSPOST.equals(migrationTypeCode)|| MigrationType.IDEN_TO_PCSPRE.equals(migrationTypeCode)){
			if ( NetworkType.NETWORK_TYPE_HSPA.equals(targetNetworkType))  {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_M2P_I2H;
			}else {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_M2P_I2C;
			}
		}else if (MigrationType.PCS_POST_TO_IDEN.equals(migrationTypeCode) || MigrationType.PCS_PRE_TO_IDEN.equals(migrationTypeCode)) {
			if (NetworkType.NETWORK_TYPE_HSPA.equals(sourceNetworkType)) {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2M_H2I;
			}else {
				portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_P2M_C2I;
			}
		}

		try {
			PortInEligibility portEligibility = provider.getPortRequestManager().testPortInEligibility(getPhoneNumber(), portVisibilityType, this.getBrandId());

			if (NetworkType.NETWORK_TYPE_CDMA.equals(targetNetworkType) && portEligibility.isCDMACoverage()) { // CDMA
				if (MigrationType.IDEN_TO_PCSPOST.equals(migrationTypeCode) || MigrationType.PCS_PRE_TO_PCSPOST.equals(migrationTypeCode)) {

					phoneNumberEligible = portEligibility.isCDMAPostpaidCoverage();

				} else if (MigrationType.IDEN_TO_PCSPRE.equals(migrationTypeCode) || MigrationType.PCS_POST_TO_PCSPRE.equals(migrationTypeCode)) {

					phoneNumberEligible = portEligibility.isCDMAPrepaidCoverage();
				}
			} else if (NetworkType.NETWORK_TYPE_HSPA.equals(targetNetworkType) && portEligibility.isHSPACoverage()) { // HSPA
				if (MigrationType.IDEN_TO_PCSPOST.equals(migrationTypeCode) || MigrationType.PCS_PRE_TO_PCSPOST.equals(migrationTypeCode)) {

					phoneNumberEligible = portEligibility.isHSPAPostpaidCoverage();

				} else if (MigrationType.IDEN_TO_PCSPRE.equals(migrationTypeCode) || MigrationType.PCS_POST_TO_PCSPRE.equals(migrationTypeCode)) {

					phoneNumberEligible = portEligibility.isHSPAPrepaidCoverage();
				}
			} else { // IDEN
				if (MigrationType.PCS_POST_TO_IDEN.equals(migrationTypeCode) || MigrationType.PCS_PRE_TO_IDEN.equals(migrationTypeCode)) {

					phoneNumberEligible = portEligibility.isIDENCoverage();
				}
			}

		} catch (PortRequestException pre) {
			phoneNumberEligible = false;
		}

		return phoneNumberEligible;
	}

	public MigrationRequest newMigrationRequest(Account account,
			Equipment newEquipment,
			String pricePlanCode) throws
			InvalidEquipmentChangeException,UnknownBANException, TelusAPIException {
		return TMMigrationRequest.newMigrationRequest(provider, this, (TMAccount)account, newEquipment, pricePlanCode);
	}
	
	@Deprecated
	public MigrationRequest newMigrationRequest(Account account,
			IDENEquipment newEquipment,
			MuleEquipment associatedMule,
			String pricePlanCode) throws
			InvalidEquipmentChangeException,UnknownBANException, TelusAPIException {
		return TMMigrationRequest.newMigrationRequest(provider, this, (TMAccount)account, newEquipment, associatedMule, pricePlanCode);
	}
	
	public MigrationRequest newMigrationRequest(Account newAccount, Equipment newEquipment, Equipment newAssociatedHandset, String pricePlanCode) throws InvalidMigrationRequestException, UnsupportedEquipmentException, TelusAPIException {
		TMMigrationRequest mRequest = (TMMigrationRequest)newMigrationRequest(newAccount, newEquipment, pricePlanCode);
		mRequest.setNewAssociatedHandset(newAssociatedHandset);
		return mRequest;
	}

	private void validateMigrationRequest(MigrationRequest migrationRequest,
			String dealerCode, String salesRepCode,
			String requestorId) throws InvalidMigrationRequestException, TelusAPIException {

		//TODO NOTE: to be removed for KB10.0, see method comment below for detail
		validatePricePlanAndEquipmentType(migrationRequest);

		((TMMigrationRequest) migrationRequest).setRequestorId(requestorId);
		((TMMigrationRequest) migrationRequest).setDealerCode(dealerCode);
		((TMMigrationRequest) migrationRequest).setSalesRepCode(salesRepCode);
		((TMMigrationRequest) migrationRequest).testMigrationRequest();
	}

	// This is a work-around for P2P + CDMA 
	// Background: KB9.9 is going to remove pricePlan and equipment validation for P2P migration due to HSPA: 
	// pricePlan's equipmentType has to be '9' or match equipment.equipmentType; But the validation is required for non HSPA equipment 
	// 
	// KB10.0 will change the API: UpdateCellularConv.migrateP2P to allow us to by pass the equipmentType validation.
	// Once that in place, we should change the code in UpdateCellularSubscriberDAO.migrate(..), and take out this method completely.
	private void validatePricePlanAndEquipmentType(MigrationRequest migrationRequest) throws TelusAPIException {

		if (((TMMigrationRequest) migrationRequest).getDelegate().isP2P() && migrationRequest.getNewEquipment().isHSPA() == false) {

			// centralize the rule for SOC equipmentType/networkType check rule:
			// comment out the following code to use 
			// ServiceSummary.isCompatible(netwokrType, equipmentType)
			/*
			String[]  ppEquipmentTypes = migrationRequest.getNewContract().getPricePlan().getEquipmentTypes(migrationRequest.getNewEquipment().getNetworkType());
			String equipmentType = migrationRequest.getNewEquipment().getEquipmentType();
			for (int i = 0; i < ppEquipmentTypes.length; i++) {
				if (ppEquipmentTypes[i].equals(ReferenceDataManager.EQUIPMENT_TYPE_ALL) 
						|| ppEquipmentTypes[i].equals(equipmentType)) {
					return;
				}
			}
			 */
			if (migrationRequest.getNewContract().getPricePlan().isCompatible(
					migrationRequest.getNewEquipment().getNetworkType(),
					migrationRequest.getNewEquipment().getEquipmentType()) ) {
				return;
			}

			//if we reach here, that means PricePlan's equipmentType is neither '9' nor match the equipment's equipmentType
			String[] ppEquipmentTypes = migrationRequest.getNewContract().getPricePlan().getEquipmentTypes(migrationRequest.getNewEquipment().getNetworkType());
			throw new InvalidMigrationRequestException(
					"PricePlan's equipmentType" + Arrays.asList(ppEquipmentTypes) + " do not match equipemnt's network/equipment type[" 
					+ migrationRequest.getNewEquipment().getNetworkType()
					+ migrationRequest.getNewEquipment().getEquipmentType() + "]",
					InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
		}
	}

	public Subscriber migrate(MigrationRequest migrationRequest, String dealerCode, String salesRepCode, String requestorId) 
			throws InvalidMigrationRequestException, TelusAPIException {

		String methodName = "migrate(MigrationRequest, String, String, String)";
		String activity = "migrate";

		validateMigrationRequest(migrationRequest, dealerCode, salesRepCode, requestorId);
		Date activityDate = provider.getReferenceDataManager().getLogicalDate();

		TMMigrationRequest t = (TMMigrationRequest) migrationRequest;
		t.createPortRequestAsNeeded();
		
		TMEquipment newEquipment = (TMEquipment) migrationRequest.getNewEquipment();		
			
		try {
			t.preMigrationTask();
			SubscriberInfo srcSubInfo = ((TMSubscriber) t.getCurrentSubscriber()).getDelegate();
			SubscriberInfo newSubInfo = ((TMSubscriber) t.getNewSubscriber0()).getDelegate();

			provider.getSubscriberLifecycleFacade().migrateSubscriber(srcSubInfo, newSubInfo, activityDate, ((TMContract) t.getNewContract()).getDelegate(), ((TMEquipment) t.getNewEquipment()).getDelegate(), null, t.getDelegate(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			t.postMigrationTask();

			t.submitPortRequestAsNeeded();
			/*
		 }catch(TelusException e){
			 t.cancelPortRequestAsNeeded();
			 throw new TelusAPIException(e);
			 */			 
		} catch (Throwable e) {
			t.cancelPortRequestAsNeeded();
			throw new TelusAPIException(e);
		}

		// Holborn changes
		
		TMEquipment oldEquipment = getEquipment0();
		if (newEquipment.isHSPA() || getEquipment().isHSPA()) {
			try {
				provider.getProductEquipmentLifecycleFacade().swapEquipmentForPhoneNumber(getPhoneNumber(),
						oldEquipment.getSerialNumber(), 
						oldEquipment.getDelegate().getAssociatedHandsetIMEI(),
						oldEquipment.getNetworkType(),
						newEquipment.getSerialNumber(), 
						newEquipment.getDelegate().getAssociatedHandsetIMEI(), 
						newEquipment.getNetworkType());

			} catch (Throwable thr) {
				// if the SEMS update fails, just log it and continue
				logFailure(methodName, activity, thr, "ProductEquipmentLifecycleFacade swapEquipmentForPhoneNumber Equipment to/for Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
			}
		}

		Subscriber newSubscriber = provider.getAccountManager().findSubscriberByPhoneNumber(getPhoneNumber());
		return newSubscriber;
	}

	public void testMigrate(MigrationRequest migrationRequest, String dealerCode,
			String salesRepCode, String requestorId) throws
			InvalidMigrationRequestException, TelusAPIException {

		Equipment equip = getEquipment0();
		if (equip.isUSIMCard()) {
			USIMCardEquipment usimEquip= (USIMCardEquipment)equip;
			if (usimEquip.isExpired()) {
				throw new InvalidSerialNumberException("USIMCard sn(" + usimEquip.getSerialNumber()+") is expired.");
			}
			if (usimEquip.isPreviouslyActivated()){
				if (getSubscriptionId() != Long.valueOf(usimEquip.getLastAssociatedSubscriptionId()).longValue()) {
					throw new SerialNumberInUseException("USIMCard sn(" + usimEquip.getSerialNumber()+") is previously activated" +
							" with subscription id: " + usimEquip.getLastAssociatedSubscriptionId());
				}
			}
		}
		validateMigrationRequest(migrationRequest, dealerCode, salesRepCode, requestorId);
	}

	@Override
	public Date getMigrationDate() {
		return delegate.getMigrationDate();
	}

	@Override
	public MigrationType getMigrationType() {

		MigrationType type = null;
		try {
			type = provider.getReferenceDataManager().getMigrationType(delegate.getMigrationTypeCode());
		}catch(Throwable t) {
			log(t.getMessage());
		}

		return type;
	}

	// Business Connect seat migration method
	public MigrateSeatRequest newMigrationRequest(Account newAccount, String pricePlanCode) throws InvalidMigrationRequestException, TelusAPIException {
		return TMMigrateSeatRequest.newMigrateSeatRequest(provider, this, (TMAccount) newAccount, pricePlanCode);
	}

	// Business Connect seat migration method
	public MigrateSeatRequest newMigrationRequest(Account newAccount, String pricePlanCode, String targetSeatTypeCode, String targetSeatGroupId) 
			throws InvalidMigrationRequestException, TelusAPIException {
		return TMMigrateSeatRequest.newMigrateSeatRequest(provider, this, (TMAccount) newAccount, pricePlanCode, targetSeatTypeCode, targetSeatGroupId);
	}
	
	// Business Connect test seat migration method
	public void testMigrate(MigrateSeatRequest migrateSeatRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException {

		((TMMigrateSeatRequest) migrateSeatRequest).setRequestorId(requestorId);
		((TMMigrateSeatRequest) migrateSeatRequest).setDealerCode(dealerCode);
		((TMMigrateSeatRequest) migrateSeatRequest).setSalesRepCode(salesRepCode);
		((TMMigrateSeatRequest) migrateSeatRequest).testMigrationRequest();
	}
	
	// Business Connect seat migration method
	public Subscriber migrate(MigrateSeatRequest migrateSeatRequest, String dealerCode, String salesRepCode, String requestorId) throws InvalidMigrationRequestException, TelusAPIException {

		TMSubscriber migratedSubscriber = null;
		TMMigrateSeatRequest request = (TMMigrateSeatRequest) migrateSeatRequest;
		request.setRequestorId(requestorId);
		request.setDealerCode(dealerCode);
		request.setSalesRepCode(salesRepCode);
		request.testMigrationRequest();
		
		Date activityDate = provider.getReferenceDataManager().getLogicalDate();
		 
		request.preMigrationTask();	
		
		// Put all activities related to moving the subscriber in this try-catch clause
		try {

			// Set this to true, as we need to execute the underlying 'change ownership' logic
			boolean transferOwnership = true;

			// Move the seat (i.e., subscriber) to the target account
			provider.getSubscriberLifecycleFacade().moveSubscriber(getDelegate(), request.getTargetBan(), activityDate, transferOwnership,
					request.getMigrationReasonCode(), request.getUserMemoText(), request.getDealerCode(), request.getSalesRepCode(),
					provider.getAccountNotificationSuppressionIndicator(getBanId()), null, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

		} catch (Throwable t) {
			// No fallout email is required at this point, since nothing has completed - just throw the exception
			throw new TelusAPIException(t);
		}
		// Put all activities related to changing the subscriber's price plan in this try-catch clause
		try {				
			// Retrieve the moved subscriber with contract
			TMSubscriber movedSubscriber = (TMSubscriber) provider.getAccountManager().findSubscriberByPhoneNumber(getPhoneNumber());

			// Create a new contract (based on the contract in the request)
			TMContract newContract = mapOptionalServices((TMContract) request.getNewContract(), 
					movedSubscriber.newContract0(request.getNewContract().getPricePlan(), Subscriber.TERM_PRESERVE_COMMITMENT, false, null, false, null));	
			
			// Change the service agreement to the new price plan
			newContract.save(request.getDealerCode(), request.getSalesRepCode());
			
			// Mark the subscriber as migrated
			migratedSubscriber = movedSubscriber;

		} catch (Throwable t) {
			// Business decided not to send the fallout email notification
			provider.getExceptionHandler().handleException(t);
		}
		
		request.postMigrationTask();

		return migratedSubscriber;
	}
	
	private TMContract mapOptionalServices(TMContract oldContract, TMContract newContract) throws TelusAPIException {
		// Map changes from the request contract to the new contract. When a new contract is created, all optional services are marked as 'DELETE'.
		// Through the oldContract, the user may have selected some of these services to be added back to the newContract.
		
		// Get all optional services in the oldContract that are not marked as 'DELETE'
		ServiceAgreementInfo[] oldContractOptionalServices = oldContract.getDelegate().getOptionalAndIncludedPromotionalServices(false);
		for (int i = 0; i < oldContractOptionalServices.length; i++) {
			ServiceAgreementInfo service = oldContractOptionalServices[i];
			// Add back all optional services which are not marked as 'DELETE'
			if (service.getTransaction() != BaseAgreementInfo.DELETE) {
				// If we're adding back a deleted service, find the corresponding service in the newContract and change the transaction accordingly 
				if (newContract.getDelegate().containsService0(service.getCode(), true)) {
					newContract.getDelegate().getService0(service.getCode(), true).setTransaction(service.getTransaction());
				} else {
					// If this is a net new add-on, simply add the service to the newContract
					newContract.getDelegate().addService(service);
					// If the effective date is null, set it to the current logical date
					if (service.getEffectiveDate() == null) {
						newContract.getDelegate().getService(service.getCode()).setEffectiveDate(getLogicalDate());
					}
				}
			}
		}
		
		return newContract;
	}
	
	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term) throws TelusAPIException {
		return getAvailablePricePlans(telephonyEnabled, dispatchEnabled, webEnabled, term, true, false, getDefaultEquipmentType());
	}

	/**
	 * This method is added for pass-through from getAvailablePricePlans (boolean, String)
	 * It is accessible from provider only.
	 * @param telephonyEnabled
	 * @param dispatchEnabled
	 * @param webEnabled
	 * @param term
	 * @param equipmentType
	 * @return
	 * @throws TelusAPIException
	 */
	public PricePlanSummary[] getAvailablePricePlans(boolean telephonyEnabled, boolean dispatchEnabled, boolean webEnabled, int term, String equipmentType) throws TelusAPIException {
		return getAvailablePricePlans(telephonyEnabled, dispatchEnabled, webEnabled, term, true, false, equipmentType);
	}

	@Override
	public PricePlanSummary[] getAvailablePricePlans(boolean getAll) throws TelusAPIException {
		if (getAll) {
			if (activation) {
				return getAvailablePricePlans(true, true, true, PricePlanSummary.CONTRACT_TERM_ALL, false, false, getDefaultEquipmentType());
			} else {
				return getAvailablePricePlans(true, getContract0().isDispatchEnabled(), true, PricePlanSummary.CONTRACT_TERM_ALL, false, false, getDefaultEquipmentType());
			}
		} else {
			return getAvailablePricePlans();
		}
	}

	public void setMigration(boolean isMigration) {
		this.isMigration = isMigration;
	}

	@Override
	public QueueThresholdEvent[] getQueueThresholdEvents(Date from, Date to) throws TelusAPIException {
		
		if (getSubscriptionId() == 0)
			return new QueueThresholdEvent[0];
		try {
			return decorate(provider.getQueueEventManagerNew().getEvents(getSubscriptionId(), from, to));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return null;
	}

	private TMQueueThresholdEvent decorate(QueueThresholdEventInfo info) {
		return new TMQueueThresholdEvent(provider, info);
	}

	private TMQueueThresholdEvent[] decorate(QueueThresholdEventInfo[] infos) {
		
		TMQueueThresholdEvent[] events = new TMQueueThresholdEvent[infos.length];
		for (int i = 0; i < infos.length; i++) {
			events[i] = decorate(infos[i]);
		}
		
		return events;
	}

	@Override
	public long getSubscriptionId() {
		return delegate.getSubscriptionId();
	}

	@Override
	public double getRequestedSecurityDeposit() {
		return delegate.getRequestedSecurityDeposit();
	}
	
	// Updated for CDA phase 1B July 2018
	@Override
	public void save(boolean activate, ActivationOption selectedOption) throws TelusAPIException {
		
		try {
			// Process the selected activation option
			setActivationOption(selectedOption, false);
			getActivationOption().apply();
			
			save(activate);

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void save(Date startServiceDate, ActivationOption selectedOption) throws TelusAPIException {
		
		try {
			// Process the selected activation option
			setActivationOption(selectedOption, false);
			getActivationOption().apply();
			
			save(startServiceDate);

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, ActivationOption selectedOption) throws TelusAPIException {

		try {
			if (selectedOption != null) {
				// Create and apply the TMActivationOption object with the target account
				TMActivationOption activationOption = new TMActivationOption(provider, selectedOption, account, this, transferOwnership);
				activationOption.apply();
				move(account, transferOwnership, reasonCode, memoText);
				activationOption.setBackOriginalDepositIfDifferentiated();
			} else {
				move(account, transferOwnership, reasonCode, memoText);
			}
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void move(Account account, boolean transferOwnership, String reasonCode, String memoText, String dealerCode, String salesRepCode, ActivationOption selectedOption) throws TelusAPIException {
		
		try {
			if (selectedOption != null) {
				// Create and apply the TMActivationOption object with the target account
				TMActivationOption activationOption = new TMActivationOption(provider, selectedOption, account, this, transferOwnership);
				activationOption.apply();
				move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode);
				activationOption.setBackOriginalDepositIfDifferentiated();
			} else {
				move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode);
			}
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	public PortRequest getPortRequest() throws PRMSystemException, TelusAPIException {
		
		if (portRequest == null) {
			PortRequestInfo[] portRequestArray = null;
			//PortRequestInfo[] portRequestArray = getPortRequestSO().getCurrentPortRequestsByPhoneNumber(getPhoneNumber(), getBrandId());
			try {
				portRequestArray = provider.getSubscriberLifecycleFacade().getCurrentPortRequestsByPhoneNumber(getPhoneNumber(), getBrandId());
			} catch (ApplicationException ae) {
				TelusExceptionTranslator telusExceptionTranslator = new ProviderWNPExceptionTranslator(provider);
				provider.getExceptionHandler().handleException(ae, telusExceptionTranslator);
			}
			if (portRequestArray != null && portRequestArray.length > 0) {
				portRequest = portRequestArray[0];
				portRequest.setSubscriber(this.getDelegate());
				portRequest.setAccount(this.getAccount0().getDelegate0());
				portRequest.setEquipment(this.getEquipment0().getDelegate());
			}
		}
		PortRequest result = null;
		if (portRequest != null) {
			result = new TMPortRequest(provider, portRequest);
		}

		return result;
	}

	public PortRequest newPortRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate) throws TelusAPIException {

		portRequest = new PortRequestInfo();
		Account account = getAccount();
		if (prePopulate) {
			PortRequest lastPortRequest = getLastPortRequest(account);
			if (lastPortRequest != null) {
				portRequest.setPortRequestName(lastPortRequest.getPortRequestName());
				portRequest.setAgencyAuthorizationName(lastPortRequest.getAgencyAuthorizationName());
				portRequest.setBusinessName(lastPortRequest.getBusinessName());
				portRequest.setPortRequestAddress(lastPortRequest.getPortRequestAddress());
				if (isPortRequestWithin24Hours(lastPortRequest.getCreationDate())) {
					portRequest.setOSPAccountNumber(lastPortRequest.getOSPAccountNumber());
					portRequest.setOSPPin(lastPortRequest.getOSPPin());
				}
			} else {
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyName(account, portRequest);
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyAddress(account, portRequest);
			}
			portRequest.setAlternateContactNumber(account.getOtherPhone());
			portRequest.setPhoneNumber(phoneNumber);
			portRequest.setPortDirectionIndicator(NPDirectionIndicator);
		} else {
			if (NPDirectionIndicator.equals(PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS)) {
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyName(account, portRequest);
				portRequest = (PortRequestInfo) PortRequestManager.Helper.copyAddress(account, portRequest);
			}
			portRequest.setPhoneNumber(phoneNumber);
			portRequest.setPortDirectionIndicator(NPDirectionIndicator);
		}

		portRequest.setSubscriber(this.getDelegate());
		portRequest.setAccount(this.getAccount0().getDelegate0());
		portRequest.setEquipment(this.getEquipment0().getDelegate());

		return new TMPortRequest(provider, portRequest);
	}

	public String getPortType() {
		return delegate.getPortType();
	}

	public Date getPortDate() {
		return delegate.getPortDate();
	}

	public PortRequestSummary getPortRequestSummary() throws PRMSystemException, TelusAPIException {
		//return getPortRequestSO().checkPortRequestStatus(getPhoneNumber(), getBrandId());
		try {
			return provider.getSubscriberLifecycleFacade().checkPortRequestStatus(getPhoneNumber(), getBrandId());
		} catch (ApplicationException ae) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderWNPExceptionTranslator(provider);
			provider.getExceptionHandler().handleException(ae, telusExceptionTranslator);
		}

		return null;
	}

	public PortRequestSummary getPortRequestSummary(String phoneNumber, int brandId) throws PRMSystemException, TelusAPIException {
//		return getPortRequestSO().checkPortRequestStatus(phoneNumber, brandId);
		try {
			return provider.getSubscriberLifecycleFacade().checkPortRequestStatus(phoneNumber, brandId);
		} catch (ApplicationException ae) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderWNPExceptionTranslator(provider);
			provider.getExceptionHandler().handleException(ae,telusExceptionTranslator);
		}
		
		return null;
	}

	private String createPortRequest() throws PortRequestException, TelusAPIException {
		// this is the default for inter-carrier port requests
		return createPortRequest(getBrandId(), Brand.BRAND_ID_NOT_APPLICABLE);
	}

	private String createPortRequest(int incomingBrandId, int outgoingBrandId) throws PortRequestException, TelusAPIException {
		String portRequestId = null;
		//		String portRequestId = getPortRequestSO().createPortInRequest(this, portProcess, incomingBrandId, outgoingBrandId, null, null);
		try {
			portRequestId = provider.getSubscriberLifecycleFacade().createPortInRequest(this.getDelegate(), portProcess, incomingBrandId, outgoingBrandId, null, null, provider.getApplication(),
					provider.getUser(), portRequest);
			Long.parseLong(portRequestId);
		} catch (ApplicationException ae) {
			TelusExceptionTranslator telusExceptionTranslator = new ProviderWNPExceptionTranslator(provider);
			provider.getExceptionHandler().handleException(ae, telusExceptionTranslator);
		} catch (NumberFormatException e) {
			throw new PortRequestException("Unable to parse port request Id [" + portRequestId + "]");
		} catch (Throwable t) {
			throw new PRMSystemException("create PortRequest - failed for id [" + portRequestId + "]; cause: " + t.getMessage(), t);
		}

		return portRequestId;
	}

	public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility) throws PortRequestException, TelusAPIException {
		ServicesValidationInfo srvValidation = new ServicesValidationInfo();
		save(activate, selectedOption, portInEligibility, srvValidation);
	}

	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers, String dealerCode, String salesRepCode, PortInEligibility portInEligibility)
			throws PhoneNumberException, PhoneNumberInUseException, PortRequestException, PRMSystemException, TelusAPIException {
		changePhoneNumber(availablePhoneNumber, reasonCode, changeOtherNumbers, dealerCode, salesRepCode, portInEligibility, null);
	}

	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, String reasonCode, boolean changeOtherNumbers, String dealerCode, String salesRepCode, PortInEligibility portInEligibility, ServiceRequestHeader header)
	throws PhoneNumberException, PhoneNumberInUseException, PortRequestException, PRMSystemException, TelusAPIException {

		String portRequestId = null;
		String oldPhoneNumber = delegate.getPhoneNumber();
		String methodName = "changePhoneNumber";
		String activity = null;
		String activityReasonCode = null;
		SubscriberInfo incomingSub = null;
		Subscriber outgoingSub = null;
		String oldSubscriberId = delegate.getSubscriberId();

		// 0.1 Determine the port process from the PortInEligibility object
		determinePortProcessType(portInEligibility);
 
		commSuitePhoneNumberChangePreTask();

		if (portedIn) {
			// 1.1 Reserve the phone number to be ported
			if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
				// check port-out eligibility 
				if (provider.getPortRequestManager().testPortOutEligibility(availablePhoneNumber.getPhoneNumber(), PortOutEligibility.NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS).isEligible() == true) {

					try {						 
						// retrieve the active subscriber on the old (outgoing) brand
						activity = "retrieve active subscriber on old brand";
						outgoingSub = provider.getAccountManager0().findSubscriberByPhoneNumber0(availablePhoneNumber.getPhoneNumber());
						logSuccess(methodName, activity, delegate, outgoingSub.toString());

						// validate OSP account, ESN or PIN information
						activity = "validate OSP port request information";
						interBrandPortValidateOSPPortRequestData(outgoingSub, getPortRequest());
						logSuccess(methodName, activity, delegate, outgoingSub.toString());

						// get the reason code
						activity = "determine the activity reason code";
						activityReasonCode = getInterBrandPortActivityReasonCode(this, outgoingSub);
						logSuccess(methodName, activity, delegate, activityReasonCode);

						// cancel the subscriber on the old (outgoing) brand and reserve the phone number on the new (incoming) brand
						activity = "cancel outgoing subscriber and reserve incoming subscriber";
						Date activityDate = provider.getReferenceDataManager().getLogicalDate();
						interBrandPortCancelOutgoingSubscriber(outgoingSub, activityReasonCode, activityDate, header);
						incomingSub = interBrandPortReserveIncomingSubscriber(
								((TMSubscriber)outgoingSub).getDelegate(), availablePhoneNumber.getNumberGroup(),
								availablePhoneNumber.getPhoneNumber(), activityReasonCode, header);
						delegate.setPhoneNumber(availablePhoneNumber.getPhoneNumber());  // TODO Why are we doing this?
						logSuccess(methodName, activity, "new subscriberId [" + incomingSub.getSubscriberId() + "]");

					} catch(PortOutEligibilityException poee) {
						logFailure(methodName, activity, delegate, poee, null);
						throw poee;
					} catch(InterBrandPortRequestException ibpre) {
						logFailure(methodName, activity, delegate, ibpre, null);
						throw ibpre;
					} catch(UnknownSubscriberException use) {
						logFailure(methodName, activity, delegate, use, null);
						throw new InterBrandPortRequestException(use, provider.getApplicationMessage(InterBrandPortRequestException.ERR006), InterBrandPortRequestException.ERR006);
					} catch(TelusAPIException tapie) {		  
						logFailure(methodName, activity, delegate, tapie, null);
						throw tapie;
					} catch(Throwable t) {
						logFailure(methodName, activity, delegate, t, null);
						throw new TelusAPIException(t);
					}

				} else {
					throw new PortOutEligibilityException(provider.getApplicationMessage(PortOutEligibilityException.ERR_OSP_TRANSFER_BLOCK));
				}
			} else {
				try {
					activity = "reserve port-in phone number [" + availablePhoneNumber.getPhoneNumber() + "]";
					TMPhoneNumberReservation phoneNumberReservation = new TMPhoneNumberReservation();
					phoneNumberReservation.setPhoneNumberPattern(availablePhoneNumber.getPhoneNumber());
					phoneNumberReservation.setNumberGroup(availablePhoneNumber.getNumberGroup());
					incomingSub = provider.getSubscriberManagerBean().reservePortedInPhoneNumber(delegate, phoneNumberReservation.getPhonenumberReservation0(), true);
					delegate.setPhoneNumber(availablePhoneNumber.getPhoneNumber()); // TODO Why are we doing this?
					logSuccess(methodName, activity, "new subscriberId [" + incomingSub.getSubscriberId() + "]");

				} catch(PhoneNumberInUseException pniue) {
					logFailure(methodName, activity, delegate, pniue, null);
					throw pniue;
				} catch(Throwable t) {
					logFailure(methodName, activity, t, "This error is ignored by the ClientAPI, we will proceed with Creating PortRequest and phone number change");
					// Per Rad, we will ignore this error and continue with the phone number change
				}
			}

			// 1.2 Create a port request in PRM after sucessfully reserving the ported phone number
			activity = "create port request [" + availablePhoneNumber.getPhoneNumber() + "]";
			try {
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					portRequestId = createPortRequest(portInEligibility.getIncomingBrandId(), portInEligibility.getOutgoingBrandId());
					logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");
				} else if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT) || portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
					portRequestId = createPortRequest();
					logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");
				} else {
					throw new PortRequestException("unknown port process exception [" + portProcess + "]");
				}
				delegate.setPhoneNumber(oldPhoneNumber); // TODO Why are we doing this?

			} catch (TelusAPIException tae) {
				logFailure(methodName, activity, tae, null);
				// 1.2.1a				 
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					interBrandPortReleaseReservedNumber(incomingSub, false);
					interBrandPortResumeCancelledSubscriber(((TMSubscriber)outgoingSub).getDelegate(), activityReasonCode, header);
				} else {
					releasePortInNumberForChangePhoneNumber(incomingSub, availablePhoneNumber.getPhoneNumber(), oldPhoneNumber);
				}
				throw tae;
			} catch (Throwable t) {
				logFailure(methodName, activity, t, null);
				// 1.2.1b
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					interBrandPortReleaseReservedNumber(incomingSub, false);
					interBrandPortResumeCancelledSubscriber(((TMSubscriber)outgoingSub).getDelegate(), activityReasonCode, header);
				} else {
					releasePortInNumberForChangePhoneNumber(incomingSub, availablePhoneNumber.getPhoneNumber(), oldPhoneNumber);
				}
				throw new TelusAPIException(t);
			}

			// May 17, 2012. As updated requirement, move deactivateMVNESubcriber after createPortRequest
			try {
				validateAndDeactivateMVNESubscriber();
			} catch (ApplicationException e) {
				activity = "validate and deactivateMVNESubscriber error";
				logFailure(methodName, activity, e, null);
				//[Naresh-Eagle production defect fix 2252]- if prePortValidation failes for MVNE subscriber then releaseing the Incoming MVNE subsciber .
				releasePortInNumberForChangePhoneNumber(incomingSub, availablePhoneNumber.getPhoneNumber(), oldPhoneNumber);
				TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
				provider.getExceptionHandler().handleException(e,telusExceptionTranslator);
			}
		}

		String changeNumberContext = "old [" + oldPhoneNumber + "], new [" + availablePhoneNumber.getPhoneNumber() + "]";
		try {
			// 2.1 Change the phone number in KB
			activity = "change phone number in KB";
			changePhoneNumber0(availablePhoneNumber, reasonCode, changeOtherNumbers, dealerCode, salesRepCode, portedIn, outgoingSub != null ? ((TMSubscriber)outgoingSub).getDelegate() : null);
			logSuccess(methodName, activity, changeNumberContext);
		} catch (Throwable t) {
			logFailure(methodName, activity, t, changeNumberContext);
			if (portedIn) {
				// 2.2 Release port-in phone number that was reserved in step 1.1
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					interBrandPortReleaseReservedNumber(incomingSub, false);
					interBrandPortResumeCancelledSubscriber(((TMSubscriber)outgoingSub).getDelegate(), activityReasonCode, header);					
				} else {
					releasePortInNumberForChangePhoneNumber(incomingSub, availablePhoneNumber.getPhoneNumber(), oldPhoneNumber);
				}

				// 2.3 If KB calls failed, we need to cancel the port request that we created in step 1.2
				activity = "cancel port request [" + portRequestId + "]";
				try {
					//getPortRequestSO().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED);
					provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED, provider.getApplication());
					logSuccess(methodName, activity, null);
				} catch (Throwable t2) {
					logFailure(methodName, activity, t2, null);
				}
			}
			// 2.4 Regardless, we need to throw the original exception from KB
			if (t instanceof InterBrandPortRequestException)
				throw (InterBrandPortRequestException) t;

			throw new TelusAPIException(t);
		}

		if (needToCallSRPDS(header)) {
			((TMServiceRequestManager)provider.getServiceRequestManager()).reportChangePhoneNumber(delegate.getBanId(), oldSubscriberId, delegate.getSubscriberId(),
					delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), oldPhoneNumber, delegate.getPhoneNumber(), header);
		}

		if (portedIn) {
			// 3.1 Update PRM's port request after successfully changing the phone number in KB
			activity = "submit port request [" + portRequestId + "]";
			try {
//				getPortRequestSO().submitPortInRequest(portRequestId);
				provider.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, provider.getApplication());
				logSuccess(methodName, activity, null);
			} catch (Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new PRMSystemException("submit port request - in changePhoneNumber() failed for id [" + portRequestId + "]; cause: " + t.toString(), t);
			}

			if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT) || PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT.equals(portProcess)) {
				// 3.2 Set the subscriber port indicator in KB and send the SMS notification
				// do this as the very last step since no rollbacks can occur now that the phone number has changed in KB  
				performInterBrandPortActivities();
			}
		}

		portRequest = null;
	}
	
	/**
	 * This method executes the inter brand port activities after subscriber creation. This would halt on error occured at any step.
	 * @throws TelusAPIException
	 */
	private void performInterBrandPortActivities() throws TelusAPIException {
		String activity = "";
		String methodName = "performInterBrandPortActivities";
		try {
			activity = "set subscriber port indicator";
			provider.getSubscriberManagerBean().setSubscriberPortIndicator(getPhoneNumber());
			logSuccess(methodName, activity, delegate, null);
		} catch (Throwable t) {
			logFailure(methodName, activity, t, null);
			throw new TelusAPIException(t);
		}		
	}
	

	/**
	 * This method is similar to performInterBrandPortActivities. It executes all steps and logs the error only. It does not
	 * throw any exception back. 
	 */
	private void performInterBrandPortActivitiesWithNoExceptionThrown(boolean activate) {
		String activity = "";
		String methodName = "performInterBrandPortActivitiesWithNoExceptionThrown";
		// set the subscriber port indicator in KB
		activity = "set subscriber port indicator";
		try {
			provider.getSubscriberManagerBean().setSubscriberPortIndicator(getPhoneNumber());
			logSuccess(methodName, activity, delegate, null);
		} catch (Throwable t) {
			// if the port indicator update fails, just log it and continue
			logFailure(methodName, activity, delegate, t, "exception ignored");
		}
	}

	private void releasePortInNumberForChangePhoneNumber(SubscriberInfo subInfo, String newPhoneNumber, String oldPhoneNumber) 
	throws TelusAPIException {

		String methodName = "releasePortInNumberForChangePhoneNumber";
		String activity = "release port in subscriber [" + newPhoneNumber + "]";
		String phoneNumberChangeContext = "old phone number [" + oldPhoneNumber + "], new phone number [" + newPhoneNumber + "]";
		try {
			// set delegate with the new phone number
			delegate.setPhoneNumber(newPhoneNumber);
			provider.getSubscriberManagerBean().releasePortedInSubscriber(subInfo);	 	 
			logSuccess(methodName, activity, subInfo, phoneNumberChangeContext);

		} catch (Throwable t) {
			logFailure(methodName, activity, subInfo, t, phoneNumberChangeContext);
		} finally {
			delegate.setPhoneNumber(oldPhoneNumber);
		}
	}

	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest) 
	throws PortRequestException, PRMSystemException, TelusAPIException {
		ServicesValidationInfo srvValidation = new ServicesValidationInfo();
		activate(reason, startServiceDate, memoText, isPortIn, modifyPortRequest, srvValidation);
	}

	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServiceRequestHeader header) 
	throws PortRequestException, PRMSystemException, TelusAPIException {
		ServicesValidationInfo srvValidation = new ServicesValidationInfo();
		char oldStatus = delegate.getStatus();
		activate(reason, startServiceDate, memoText, isPortIn, modifyPortRequest, srvValidation);
		if (needToCallSRPDS(header)) {
			reportChangeSubscriberStatus(delegate, oldStatus, Subscriber.STATUS_ACTIVE, reason, startServiceDate, header);
		}
	}

	protected void determinePortProcessType(TMPortRequest portRequest) throws TelusAPIException {	  
		Brand[] brands = provider.getReferenceDataManager().getBrands();
		
		if (portRequest.isPortInFromMVNE()) {
			this.portProcess = PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT;
		} else if (portRequest.getIncomingBrandId()!= portRequest.getOutgoingBrandId() &&
				ReferenceDataManager.Helper.validateBrandId(portRequest.getIncomingBrandId(), brands) &&
				ReferenceDataManager.Helper.validateBrandId(portRequest.getOutgoingBrandId(), brands)) {
			this.portProcess = PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT;
		} else {
			this.portProcess = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
		}	
	}

	protected void determinePortProcessType(PortInEligibility portInEligibility) throws TelusAPIException {
		Brand[] brands = provider.getReferenceDataManager().getBrands();
		if (portInEligibility != null) {
			this.portedIn = true;
			if (portInEligibility.isPortInFromMVNE()) {
				this.portProcess = PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT;
			} else if (ReferenceDataManager.Helper.validateBrandId(portInEligibility.getIncomingBrandId(), brands) &&
					ReferenceDataManager.Helper.validateBrandId(portInEligibility.getOutgoingBrandId(), brands)) {
				this.portProcess = PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT;
			} else {
				this.portProcess = PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;		
			}
		}
	}

	public void unreserve(boolean cancelPortIn) throws PRMSystemException, TelusAPIException {

		String methodName = "unreserve";
		String activity = "unreserve (release) a phone number";

		if (cancelPortIn) {

			String phoneNumber = getPhoneNumber();
			int brandId = getBrandId();
			try {
				// release the phone number
				activity = "release the phone number";
				provider.getSubscriberManagerBean().releasePortedInSubscriber(delegate);

				// if this is an inter-brand port, retrieve and resume the cancelled subscriber on the previous brand
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {

					// retrieve the cancelled subscriber on the old (outgoing) brand
					activity = "retrieve the cancelled subscriber";
					Subscriber oldSub = retrieveLastPortedOutCancelledSubscriberByPhoneNumber(phoneNumber);

					// get the reason code
					activity = "determine the activity reason code";
					String activityReasonCode = getInterBrandPortActivityReasonCode(this, oldSub);
					logSuccess(methodName, activity, delegate, activityReasonCode);

					// rollback the cancellation on the old (outgoing) brand
					activity = "resume cancelled subscriber on outgoing brand";
					interBrandPortResumeCancelledSubscriber(((TMSubscriber)oldSub).getDelegate(), activityReasonCode, null);
				}

			} catch (InterBrandPortRequestException ibpre) {
				logFailure(methodName, activity, ibpre, null);
				throw ibpre;
			} catch (Throwable t) {
				logFailure(methodName, activity, delegate, t, null);
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR005), InterBrandPortRequestException.ERR005);
				} else {
					throw new TelusAPIException(t);
				}
			}

			PortRequestSummary prSummary = getPortRequestSummary(phoneNumber, brandId);
			String portRequestId = prSummary != null ? prSummary.getPortRequestId() : null;

			if (portRequestId != null && prSummary.canBeCanceled()) {
				//			getPortRequestSO().cancelPortInRequest(portRequestId, RELEASE_FROM_RESERVED_ACTIVATION);
				try{
					provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, RELEASE_FROM_RESERVED_ACTIVATION, provider.getApplication());
					logSuccess(methodName, activity, delegate.toString());
				} catch (ApplicationException ae) {
					TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
					provider.getExceptionHandler().handleException(ae,telusExceptionTranslator);
				} catch (Throwable t) {
					logFailure(methodName, activity, t, null);
					throw new PRMSystemException("Cancel port request -  failed for id [" + portRequestId + "]; cause: " + t.getMessage(), t);
				}
			}

		} else {
			try {
				activity = "call SubscriberManagerEJB().releaseSubscriber";
				provider.getSubscriberManagerBean().releaseSubscriber(delegate);
				logSuccess(methodName, activity, null);
			} catch (Throwable e) {
				logFailure(methodName, activity, e, null);
				throw new TelusAPIException(e);
			}
		}

		if (getEquipment().isHSPA()) {
			try {
				activity = "call Sems releaseReservedEquipmentForPhoneNumber";
				provider.getProductEquipmentLifecycleFacade().asyncReleaseReservedEquipmentForPhoneNumber(
						getPhoneNumber(), 
						getEquipment().getSerialNumber(), 
						getEquipment0().getDelegate().getAssociatedHandsetIMEI());
				logSuccess(methodName, activity, null);
			} catch( Throwable t ) {
				logFailure(methodName, activity, t, null);
			}
		}

		// clear the values in the delegate
		clear();
	}

	public void restore(Date activityDate, String reason, String memoText, String portOption, PortInEligibility portInEligibility) 
	throws PortRequestException, PRMSystemException, TelusAPIException {
		restore(activityDate, reason, memoText, portOption, portInEligibility, null);
	}
	
	public void restore(Date activityDate, String reason, String memoText, String portOption, PortInEligibility portInEligibility, ServiceRequestHeader header) 
	throws PortRequestException, PRMSystemException, TelusAPIException {

		String methodName = "restore";
		String activity = "restore";
		String portRequestId = null;

		char oldStatus = getStatus();

		EquipmentInfo equipmentInfo = getEquipment0().getDelegate();

		// 0.1 Determine the port process from the PortInEligibility object
		determinePortProcessType(portInEligibility);

		if (portOption.equals(Subscriber.PORT_OPTION_WINBACK)) {

			// 1.1 Create the port request
			activity = "create port request for WINBACK";
			try {
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					if (provider.getPortRequestManager().testPortOutEligibility(getPhoneNumber(), PortOutEligibility.NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS).isEligible() == true) {
						portRequestId = createPortRequest(portInEligibility.getIncomingBrandId(), portInEligibility.getOutgoingBrandId());
						logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");

					} else {
						throw new PortOutEligibilityException("phone number [" + getPhoneNumber() + "] is not eligible to port out");
					}

				} else if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT) || portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
					portRequestId = createPortRequest();
					validateAndDeactivateMVNESubscriber();
					logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");

				} else {
					throw new PortRequestException("unknown port process exception [" + portProcess + "]");
				}

			} catch (TelusAPIException tae) {
				logFailure(methodName, activity, tae, null);
				throw tae;
			} catch (Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new TelusAPIException(t);
			}

			// 2.1 Update KB
			activity = "restore or resume subscriber in KB";
			try {
				if (getStatus() == STATUS_SUSPENDED) {
					provider.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(delegate, activityDate, reason, memoText, portedIn, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					logSuccess(methodName, activity, null);

				} else if (getStatus() == STATUS_CANCELED) {			  
					if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {	
						// execute the inter-brand port restore logic
						interBrandPortRestore(activityDate, reason, memoText, header);
						logSuccess(methodName, activity, null);

					} else {
						// execute the regular port restore logic
						provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(delegate, reason, memoText, portedIn, portProcess, 0, null,
								new SubscriberResumedPostTaskInfo().repairCommunicationSuite(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
						logSuccess(methodName, activity, null);
					}
					if (getEquipment().isHSPA()) {
						try {
							provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(), 
									equipmentInfo.getAssociatedHandsetIMEI());
						}catch(Throwable t) {
							// if the SEMS update fails, just log it and continue
							logFailure(methodName, "assignEquipmentToPhoneNumber", t, "assign Equipment to Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
						}
					}

				} else {
					throw new TelusAPIException("subscriber is not cancelled or suspended");
				}

			} catch (Throwable t) {
				logFailure(methodName, activity, t, null);

				// 2.2 If KB calls failed, we need to cancel the port request that we created in step 1.1
				activity = "cancel port request[" + portRequestId + "]";
				try {
//					getPortRequestSO().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED);
					provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED, provider.getApplication());
					logSuccess(methodName, activity, null);

				} catch(Throwable t2) {
					logFailure(methodName, activity, t2, null);
				}

				/*
				 // check for and throw the following exceptions which may be thrown by the interBrandPortRestore method call 
				 if (t instanceof PortOutEligibilityException)
					 throw (PortOutEligibilityException) t;
				 else if (t instanceof InterBrandPortRequestException)
					 throw (InterBrandPortRequestException) t;
				 else
					 throw new TelusAPIException(t);
				 */					 
				provider.getExceptionHandler().handleException(t);
			}			 

			// 3.1 Submit the port request
			activity = "submit port request [" + portRequestId + "]";
			try {
				//				getPortRequestSO().submitPortInRequest(portRequestId);
				provider.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, provider.getApplication());			
				logSuccess(methodName, activity, null);

			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new PRMSystemException(activity + " failed",t);
			}

			portRequest = null;

		} else if (portOption.equals(Subscriber.PORT_OPTION_INTER_BRAND_ROLLBACK)) {
			// this option allows for fixing inter-brand port failures which leave subscribers in limbo
			// in this case, we ignore PortInEligibility altogether and assume we're fixing an inter-brand port failure
			activity = "inter-brand rollback resume subscriber in KB";
			try {			  
				if (getStatus() == STATUS_CANCELED) {
					provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(delegate, reason, memoText, true,
							PortInEligibility.PORT_PROCESS_ROLLBACK, 0, null, null, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					logSuccess(methodName, activity, null);

					if (getEquipment().isHSPA()) {
						try {
							provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(),
									equipmentInfo.getAssociatedHandsetIMEI());
						}catch(Throwable t) {
							// if the SEMS update fails, just log it and continue
							logFailure(methodName, "assignEquipmentToPhoneNumber", t, "assign Equipment to Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
						}
					}

				} else {
					throw new TelusAPIException("subscriber is not cancelled");
				}

			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new TelusAPIException(t);
			}

		} else if (portOption.equals(Subscriber.PORT_OPTION_INADVERTENT_PORT)) {
			try {
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT)) {
					if (getStatus() == STATUS_SUSPENDED)
						provider.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(delegate, activityDate, reason, memoText, portedIn, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					else if (getStatus() == STATUS_CANCELED) {
						provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(delegate, reason, memoText, portedIn,
								PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, 0, null, new SubscriberResumedPostTaskInfo().repairCommunicationSuite(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
						if (getEquipment().isHSPA()) {
							try {
								provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(),
										equipmentInfo.getAssociatedHandsetIMEI());
							}catch(Throwable t) {
								// if the SEMS update fails, just log it and continue
								logFailure(methodName, "assignEquipmentToPhoneNumber", t, "assign Equipment to Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
							}
						}
					}
					else
						throw new TelusAPIException("subscriber is not cancelled or suspended");

				} else {
					throw new PortRequestException("invalid port process [" + portProcess + "] and port option [" + portOption + "]");
				}

			} catch(Throwable t) {
				throw new TelusAPIException(t);
			}

		} else if (portOption.equals(Subscriber.PORT_OPTION_NO_PORT)) {
			try {
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT)) {
					if (getStatus() == STATUS_SUSPENDED)
						provider.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(delegate, activityDate, reason, memoText, false, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					else if (getStatus() == STATUS_CANCELED) {
						provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(delegate, reason, memoText, false,
								PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, 0, null,  new SubscriberResumedPostTaskInfo().repairCommunicationSuite(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
						if (getEquipment().isHSPA()) {
							try {
								provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(), 
										equipmentInfo.getAssociatedHandsetIMEI());

							}catch(Throwable t) {
								// if the SEMS update fails, just log it and continue
								logFailure(methodName, "assignEquipmentToPhoneNumber", t, "assign Equipment to Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
							}
						}
					}
					else
						throw new TelusAPIException("subscriber is not cancelled or suspended");

				} else {
					throw new PortRequestException("invalid port process [" + portProcess + "] and port option [" + portOption + "]");
				}

			} catch(Throwable t) {
				throw new TelusAPIException(t);
			}

		} else {
			// if the port option is not one of the above, then throw an exception
			throw new PortRequestException("invalid port option [" + portOption + "]");
		}

		if (needToCallSRPDS(header)) {
			reportChangeSubscriberStatus(delegate, oldStatus, Subscriber.STATUS_ACTIVE, reason, activityDate, header);
		}

		// refresh the subscriber to pick up status changes
		refresh();

		// finally, call RCM to assign MIN-MDN relationship after the subscriber is successfully resumed or restored 
		if (isPCS() && getStatus() == STATUS_ACTIVE) {
			activity = "assign MIN";
			try {				 
				//provider.getRcmManager().assignMin(getPhoneNumber());
				provider.getSubscriberLifecycleFacade().assignTNResources(getPhoneNumber(), getSubscriberNetworkType(), 
						equipmentInfo.getProfile().getLocalIMSI(), equipmentInfo.getProfile().getRemoteIMSI());
				logSuccess(methodName, activity, delegate, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+getSubscriberNetworkType()+"]");
			} catch(Throwable t) {
				// if the RCM update fails, just log it and continue
				logFailure(methodName, activity, delegate, t, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+getSubscriberNetworkType()+"]; exception ignored");
			}
		}
	}

	/**
	 * This method first determines if the port is an MVNE port.  Only if it is,
	 * it will call the WNP service PrePortRequestValidationService.  If no
	 * exception is thrown, it will call the WNP service DeactivateMVNESubscriberService.
	 * The PrePortRequestValidationService is required to be called first so that
	 * WNP can validate that the subscriber exists on the MVNE system.  Then, a call
	 * is made to deactivate the subscriber from the MVNE system.
	 * 
	 * @throws PRMSystemException
	 * @throws TelusAPIException
	 * @throws ApplicationException
	 * @throws DeactivateMVNESubscriberException
	 */
	private void validateAndDeactivateMVNESubscriber()
			throws PRMSystemException, TelusAPIException, ApplicationException,
			DeactivateMVNESubscriberException {
		if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
			validateMVNESubscriber();
			deactivateMVNESubcriber(portRequest.getPhoneNumber()); //the subscriber we want to deactivate in MVNE should be the one we want to port-in
		}
	}	
	
	private void validateMVNESubscriber()
			throws PRMSystemException, TelusAPIException, ApplicationException{
		if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
			updatePlatformId(portRequest); //the platform ID is determined and only available from PortRequestRetrieval service
			verifyPortRequestInfoObj(portRequest);
			
			provider.getSubscriberLifecycleFacade().validatePortInRequest(portRequest, provider.getApplication(), provider.getUser());
		}
	}
	
	/**
	 * This method implementation can be simplified to check for the process type and set
	 * the platform ID in the code. However, for accuracy it should always retrieve from
	 * the underlying service. If there's performance consideration, we may hard code
	 * the value.
	 * 
	 * When calling this method, it's possible that the portRequestInfo is null.
	 * @param portRequestInfo
	 * @throws PortRequestException
	 * @throws TelusAPIException
	 */
	private void updatePlatformId(PortRequestInfo portRequestInfo) throws PortRequestException, TelusAPIException {
		if (portRequestInfo != null && portRequestInfo.isPlatformIdUpdated() == false && portRequestInfo.getPhoneNumber() != null) {
			PortRequestInfo[] portRequestArray = null;
			try {
				portRequestArray = provider.getSubscriberLifecycleFacade().getCurrentPortRequestsByPhoneNumber(portRequestInfo.getPhoneNumber(), getBrandId());
			} catch (ApplicationException ae) {
				TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
				provider.getExceptionHandler().handleException(ae,telusExceptionTranslator);
			}
			if (portRequestArray != null && portRequestArray.length > 0) {
				portRequestInfo.setPlatformId(portRequestArray[0].getPlatformId());
			}
		} else if (portRequestInfo == null) {
			getPortRequest();
		}
	}
	
	private void verifyPortRequestInfoObj(PortRequestInfo portRequest) throws TelusAPIException {
		if (portRequest != null) {
			if (portRequest.getSubscriber() == null) {
				portRequest.setSubscriber(this.getDelegate());
			}
			if (portRequest.getAccount() == null) {
				portRequest.setAccount(this.getAccount0().getDelegate0());
			}
			if (portRequest.getEquipment() == null) {
				portRequest.setEquipment(this.getEquipment0().getDelegate());
			}
		}
	}

	private PortRequest getLastPortRequest(Account account) throws PortRequestException, PRMSystemException, TelusAPIException{
		PortRequest lastPortRequest = null;
		PortRequest[]  portRequestArray = null;
		try{
			portRequestArray = account.getPortRequests();
		} catch (PortRequestException p) {
			return null;
		}
		if (portRequestArray != null && portRequestArray.length != 0){
			Date lastCreationDate = portRequestArray[0].getCreationDate();
			int index = 0;
			for (int i = 1; i < portRequestArray.length; i++){
				Date creationDate = portRequestArray[i].getCreationDate();
				if (lastCreationDate.before(creationDate)){
					lastCreationDate = creationDate;
					index = i;
				}
			}
			lastPortRequest = portRequestArray[index];
		}
		return lastPortRequest;
	}

	private boolean isPortRequestWithin24Hours(Date creationDate) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date todayMinusOne = cal.getTime();
		Logger.debug("creationDate = " + creationDate);
		Logger.debug("todayMinusOne = " + todayMinusOne);
		if (creationDate.after(todayMinusOne)|| creationDate.equals(todayMinusOne))
			return true;
		return false;

		/*
	Date today = new Date();
	long dayMillisecond = 86400000;

	// Get msec from each, and subtract.
	long diff = today.getTime() - creationDate.getTime();
	if (diff <= dayMillisecond)
	   	return true;
	return false;
		 */
	}

	/**
	 * @param equipment the equipment to be evaluate whether in use or not
	 * @param allowDuplicateSerialNo on of value SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
	 *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will always return false.
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, will return true if the equipment is not mule and in use in other ban, otherwise return false
	 *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, will return true if equipment is not mule and in use, otherwise return false
	 * @return false when the equipment is not in used base on the allowDuplicateSerailNo flag, true means the equipment is in use
	 * @throws TelusAPIException
	 */
	protected boolean checkNonMuleEquipmentInUse(TMEquipment equipment, char allowDuplicateSerialNo)
	throws TelusAPIException {
		boolean inUseCheckResult = false;

		if (allowDuplicateSerialNo == SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN) {
			//duplicate serial no is allowed in any ban, so bypass checking
		}
		else if (allowDuplicateSerialNo == SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN) {
			//duplicate serial no is only allow in same ban
			if (equipment.isInUse() //in use
					&& equipment.getDelegate().isMule() == false //equipment is NOT mule
					&& equipment.isInUseOnAnotherBan(getBanId(), true) // used in other ban
			) {
				inUseCheckResult = true;
			}
		}
		else { //( allowDuplicateSerialNo==SWAP_DUPLICATESERIALNO_DONOTALLOW)
			//duplicate serial no is NOT allowed at all
			if (equipment.isInUse() //in use
					&& equipment.getDelegate().isMule() == false //equipment is NOT mule
			) {
				inUseCheckResult = true;
			}
		}
		return inUseCheckResult;
	}

	/**
	 * @param equipment the equipment to be evaluate whether in use or not
	 * @param allowDuplicateSerialNo on of value SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN,SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN or
	 *  		SWAP_DUPLICATESERIALNO_DONOTALLOW
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN</tt> is set, the operation will always return false.
	 *      When <tt>SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN</tt> is set, will return true if the equipment is not mule and in use in other ban, otherwise return false
	 *      When <tt>SWAP_DUPLICATESERIALNO_DONOTALLOW</tt> is set, will return true if equipment is not mule and in use, otherwise return false
	 * @return false when the equipment is not in used base on the allowDuplicateSerailNo flag, true means the equipment is in use
	 * @throws TelusAPIException
	 */
	protected boolean checkEquipmentInUse(TMEquipment equipment, char allowDuplicateSerialNo)
	throws TelusAPIException {
		boolean inUseCheckResult = false;

		if (allowDuplicateSerialNo == SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN) {
			//duplicate serial no is allowed in any ban, so bypass checking
		}
		else if (allowDuplicateSerialNo == SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN) {
			//duplicate serial no is only allow in same ban
			if (equipment.isInUse() //in use
					&& equipment.isInUseOnAnotherBan(getBanId(), true) // used in other ban
			) {
				inUseCheckResult = true;
			}
		}
		else { //( allowDuplicateSerialNo==SWAP_DUPLICATESERIALNO_DONOTALLOW)
			//duplicate serial no is NOT allowed at all
			if (equipment.isInUse()
			) {
				inUseCheckResult = true;
			}
		}
		return inUseCheckResult;
	}

	protected static char convertAllowDuplicateFlag( boolean ignoreSerialNoInUse )
	{
		char allowDuplicateSerialNo = (ignoreSerialNoInUse)? SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN : SWAP_DUPLICATESERIALNO_DONOTALLOW;
		return allowDuplicateSerialNo;
	}

	private TMPortRequestSO getPortRequestSO() {
		if (portRequestSO == null)
			portRequestSO = new TMPortRequestSO(provider);

		return portRequestSO;
	}

	@Override
	public double getPaidSecurityDeposit() throws TelusAPIException {
		double result = 0;
		try {
			result = provider.getSubscriberLifecycleHelper().retrievePaidSecurityDeposit(
					delegate.getBanId(), delegate.getSubscriberId(), delegate.getProductType());
			//return result;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return result;
	}

	@Override
	public void setSerialNumber(String serialNumber) throws TelusAPIException {
		delegate.setSerialNumber(serialNumber);
	}

	@Override
	public void setSecondarySerialNumbers(String[ ] secondarySerialNumbers) throws TelusAPIException {
		delegate.setSecondarySerialNumbers(secondarySerialNumbers);
	}

	@Override
	public Subscriber retrieveSubscriber(String phoneNumber) throws TelusAPIException {
		
		TMPCSSubscriber subscriber = null;
		try{
			// validate if pcs account then do the next step.
			Equipment equipment =  this.equipment == null ? provider.getEquipmentManager().validateSerialNumber(this.getSerialNumber()):this.equipment;

			if (!equipment.isAnalog() && !equipment.isPCSHandset()	&& !equipment.is1xRTT() && !equipment.isRIM() && !equipment.isHSPA()) { 
				throw new InvalidSerialNumberException(	this.getSerialNumber(),InvalidSerialNumberException.EQUIPMENT_NOT_PCS);		
			}

			PCSSubscriberInfo info = new PCSSubscriberInfo();
			TMAccount account = getAccount0();
			//copy information from the current subscriber
			info.setBanId(account.getBanId());
			info.setPhoneNumber(phoneNumber);
			info.setProductType(this.getProductType());
			info.setSerialNumber(this.getSerialNumber());
			info.setEquipmentType(equipment.getEquipmentType());
			info.setDealerCode(account.getDealerCode());
			info.setSalesRepId(account.getSalesRepCode());
			info.setLanguage(account.getLanguage());
			info.getConsumerName().setFirstName(getConsumerName().getFirstName());
			info.getConsumerName().setLastName(getConsumerName().getLastName());
			info.setBirthDate(this.getBirthDate());
			//we only support pcs now
			info.setSubscriberId(phoneNumber);

			//use the phone number to set all the phone reservation object.
			AvailablePhoneNumber available =  this.provider.getReferenceDataManager().getAvailablePhoneNumber(phoneNumber, this.getProductType(), this.getDealerCode());
			info.setMarketProvince(available.getNumberGroup().getProvinceCode());
			info.setNumberGroup(available.getNumberGroup());

			subscriber = new TMPCSSubscriber(provider, info, true, this.activationFeeChargeCode, account, this.dealerHasDeposit, this.equipment);

			//also set the following information need to set on the TMsubscriber
			subscriber.setSubscriptionRole(this.subscriptionRole);

		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}

		return subscriber;
	}

	protected Equipment getVirtualEquipment() throws TelusAPIException {

		TMEquipment newEquipmentTM = null;
		TMEquipment oldEquipmentTM = (TMEquipment) getEquipment();
		String techTypeClass = oldEquipmentTM.getTechType();
		try {
			if (oldEquipmentTM != null) {
				EquipmentInfo equipmentInfo = provider.getProductEquipmentHelper().retrieveVirtualEquipment(oldEquipmentTM.getSerialNumber(), techTypeClass);
				newEquipmentTM = new TMEquipment(provider, equipmentInfo);
			}
			return newEquipmentTM;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}	
		return null;
	}

	public String getPortProtectionIndicator() throws TelusAPIException {

		try {
			return  provider.getSubscriberLifecycleHelper().getPortProtectionIndicator (getBanId(), getSubscriberId(), getPhoneNumber(),String.valueOf(getStatus()));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	@Override
	public String getSLALevel() throws TelusAPIException {

		int nbrOfFeatures = contractFeatures != null ? contractFeatures.length : 0;

		if ( nbrOfFeatures == 0 )
			contractFeatures =  getContract0().getFeatures(true);

		if (ReferenceDataManager.Helper.retainByCategoryCode(contractFeatures,
				Feature. CATEGORY_CODE_TTR_SLA).length > 0) {
			return Subscriber. TTR_SLA_LEVEL;
		}
		else {
			return Subscriber. NON_SLA;
		}
	}

	/**
	 * Applies the Transfer Blocking indicator for Wireless to Wireless port requests
	 * at Subscriber level. It's not remote call, the return value is the result of different
	 * combinations of the input parameters - accountPortProtectionIndicator and
	 * subscriberPortProtectionIndicator
	 * @throws TelusAPIException
	 */
	public boolean isPortRestricted(String accountPortProtectionIndicator, String subscriberPortProtectionIndicator) throws TelusAPIException {

		try {
			if (accountPortProtectionIndicator == null && subscriberPortProtectionIndicator == null) {
				return false;
			} else {
				if (AccountSummary.PORT_RESTRICTED.equalsIgnoreCase(subscriberPortProtectionIndicator) || AccountSummary.PORT_NOT_RESTRICTED.equalsIgnoreCase(subscriberPortProtectionIndicator)) {
					// subscriber Port Protection Indicator has non-null value
					return AccountSummary.PORT_RESTRICTED.equalsIgnoreCase(subscriberPortProtectionIndicator) ? true : false;
				} else {
					return AccountSummary.PORT_RESTRICTED.equalsIgnoreCase(accountPortProtectionIndicator) ? true : false;
				}
			}
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public void updatePortRestriction(boolean restrictPort)  throws TelusAPIException {
		try {
			provider.getSubscriberManagerBean().updatePortRestriction(getBanId(),getSubscriberId(), restrictPort, provider.getUser());
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	private DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private static FieldPosition POSITION = new FieldPosition(0);

	public StringBuffer buildLogEntryHeader(String methodName, String activity) {
		StringBuffer sb = new StringBuffer();
		DATE_FORMAT.format(new Date(), sb, POSITION ).append(" ");
		sb.append("[").append(Thread.currentThread().getName()).append("] ");

		sb.append(methodName).append("():").append(activity);
		return sb;
	}

	public StringBuffer appendSubscriberInfo(StringBuffer sb, SubscriberInfo subInfo) {
		if (subInfo != null) {
			sb.append("  subscriber[ban:").append(subInfo.getBanId())
			.append(", subId:").append( subInfo.getSubscriberId())
			.append(", phone:").append( subInfo.getPhoneNumber())
			.append("]");
		}
		return sb;
	}

	protected void logSuccess(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-succeeded; ");
		if (extraMessage != null)
			sb.append(extraMessage ).append(";");
		appendSubscriberInfo(sb, subInfo);
		log(sb.toString());
	}

	protected void logFailure(String methodName, String activity, SubscriberInfo subInfo, Throwable t, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-failed; ");
		if (extraMessage != null)
			sb.append(extraMessage).append(";");
		appendSubscriberInfo(sb, subInfo);
		log(sb.toString());
		Logger.debug(t);
	}

	protected void logMessage(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity).append("; ").append(extraMessage);
		appendSubscriberInfo(sb, subInfo);
		log(sb.toString());
	}

	protected void logMessage( String methodName, String activity, String extraMessage) {
		logMessage(methodName, activity, delegate, extraMessage);
	}

	protected void logSuccess(String methodName, String activity, String extraMessage) {
		logSuccess(methodName, activity, delegate, extraMessage);
	}

	protected void logFailure(String methodName, String activity, Throwable t, String extraMessage) {
		logFailure(methodName, activity, delegate, t, extraMessage);
	}

	@Override
	public CallingCirclePhoneList[] getCallingCirclePhoneNumberListHistory(Date from, Date to) throws TelusAPIException {
		
		try {
			if (getAccount().isPrepaidConsumer()) {

				String[] paramName = new String[2];
				paramName[0] = "CALLING-CIRCLE";
				paramName[1] = "CALLHOMEFREE";

				FeatureParameterHistoryInfo[] ftrParamHistory = provider.getSubscriberLifecycleHelper().retrieveFeatureParameterHistory(getBanId(), getSubscriberId(), getProductType(), paramName, from,to);
				if (ftrParamHistory == null) {
					return null;
				}
				CallingCirclePhoneListInfo[] ccPhoneList = new CallingCirclePhoneListInfo[ftrParamHistory.length];
				for (int i = 0; i < ftrParamHistory.length; i++) {
					ccPhoneList[i] = new CallingCirclePhoneListInfo();
					if (ftrParamHistory[i].getEffectiveDate() != null) {
						ccPhoneList[i].setEffectiveDate(ftrParamHistory[i].getEffectiveDate());
					}
					if (ftrParamHistory[i].getExpirationDate() != null) {
						ccPhoneList[i].setExpiryDate(ftrParamHistory[i].getExpirationDate());
					}
					String phoneParams = ftrParamHistory[i].getParameterValue();
					if (phoneParams != null && phoneParams != "") {
						ccPhoneList[i].setPhoneNumberList(phoneParams.split(";"));
					}
				}
				
				return ccPhoneList;

			} else {
				return provider.getSubscriberLifecycleHelper().retrieveCallingCirclePhoneNumberListHistory(getBanId(), 
						getSubscriberId(), getProductType(), from, to);
			}
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return null;
	}

	@Override
	public VendorServiceChangeHistory[] getVendorServiceChangeHistory(String soc) throws TelusAPIException {

		String[] categoryCodes = null;
		List vendorCategoryCodes = new ArrayList();
		TMVendorServiceChangeHistory[] tmVendorServiceHistories = new TMVendorServiceChangeHistory[0];

		try {			 
			Service service = provider.getReferenceDataManager().getRegularService(soc);

			if (service != null) 
				categoryCodes = service.getCategoryCodes();

			if (categoryCodes != null) {

				for (int i = 0; i < categoryCodes.length; i++) {
					VendorService vendorService = provider.getReferenceDataManager().getVendorService(categoryCodes[i]);
					if (vendorService != null && vendorService.isRestrictionRequired() == true)
						vendorCategoryCodes.add(vendorService.getCode());
				}

				if (vendorCategoryCodes.size() > 0) {
					VendorServiceChangeHistoryInfo[] infos = provider.getSubscriberLifecycleHelper().retrieveVendorServiceChangeHistory(
							getBanId(), getSubscriberId(), (String[])vendorCategoryCodes.toArray(new String[vendorCategoryCodes.size()]));

					// decorate the info objects
					tmVendorServiceHistories = new TMVendorServiceChangeHistory[infos.length];					 
					for (int i = 0; i < infos.length; i++) {
						tmVendorServiceHistories[i] = new TMVendorServiceChangeHistory(provider, infos[i]);
					}
				}
			}

			return tmVendorServiceHistories;

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	// Updated for CDA phase 1B July 2018
	public void save(boolean activate, ActivationOption selectedOption, ServicesValidation srvValidation) throws TelusAPIException {
		try {
			// Process the selected activation option
			setActivationOption(selectedOption, false);
			getActivationOption().apply();

			save(activate, srvValidation);

		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	private void save(boolean activate, ServicesValidation srvValidation) throws TelusAPIException {
		
		String methodName = "save(boolean,ServicesValidation)";
		String activity = "save";
		SubscriberInfo subscriberInfo = delegate;
		ActivationFeaturesPurchaseArrangementInfo[] activationFeaturesPurchaseArray = null;
		boolean saveTopUpArrangementForActivation = activation;
		
		if (srvValidation == null) {
			srvValidation = new ServicesValidationInfo();
		}

		if (NetworkType.NETWORK_TYPE_HSPA.equals(getSubscriberNetworkType())) {
			((ServicesValidationInfo) srvValidation).setEquipmentServiceMatch(false);
		}

		//			checkForVoicemailService();  //Ensure VTT featue is not improperly added
		//TODO: VVM - uncomment the line above and use it to replace the line immediately below this comment (call to validateVoiceToText())
		checkForVoicemailService();

		if (getAccount().isPrepaidConsumer()) {
			activationFeaturesPurchaseArray = this.populateActivationFeaturesPurchaseArrangement((ServiceAgreementInfo[]) this.getContract0().getDelegate().getServices());

			// Prepaid Calling Circle
			ServiceAgreementInfo callingCircleService = getPrepaidCallingCircleService();
			if (callingCircleService != null && (callingCircleService.getTransaction() == BaseAgreementInfo.ADD)) {
				String featureCode = Info.padTo(callingCircleService.getCode().trim(),' ', 6 );
				String prepaidCallingCircleParam = callingCircleService.getFeature(featureCode).getParameter();
				if (prepaidCallingCircleParam != null && !prepaidCallingCircleParam.equals("")) {
					String kbMappedPrepaidServiceCode = callingCircleService.getService().getWPSMappedKBSocCode();
					this.getContract().addService(kbMappedPrepaidServiceCode);
					ServiceAgreementInfo kbMappedPrepaidService = getKbMappedPrepaidService(kbMappedPrepaidServiceCode);
					ContractFeature kbCallingCircleFeature = getKbCallingCircleFeature(kbMappedPrepaidService);
					if (kbCallingCircleFeature != null) {
						kbCallingCircleFeature.setParameter(prepaidCallingCircleParam);						
					}
				}
			}
		} 
		// update prepaid system
		try {			
			if (getAccount().isPrepaidConsumer()) {
				if (oldDelegate != null && !oldDelegate.getLanguage().equals(delegate.getLanguage())) {
					PrepaidSubscriberInfo prepaidSubscriberInfo = new PrepaidSubscriberInfo();
					prepaidSubscriberInfo.setBan(delegate.getBanId());
					prepaidSubscriberInfo.setPhoneNumber(delegate.getPhoneNumber());
					prepaidSubscriberInfo.setLanguage(delegate.getLanguage());
					prepaidSubscriberInfo.setSerialNumber(getEquipment().getSerialNumber());
					provider.getSubscriberLifecycleManager().updatePrepaidSubscriber(prepaidSubscriberInfo);
					log("== PrepaidConsumer :: " + delegate.getPhoneNumber() + " :: (new) Language :: " + delegate.getLanguage());
				}

				log("TMSubscriber.save() getting ActivationTopUpPaymentArrangement");
				TMActivationTopUpPaymentArrangement topupPaymentArrangement = 
					(TMActivationTopUpPaymentArrangement) ((PrepaidConsumerAccount)getAccount()).getActivationTopUpPaymentArrangement();

				try {
					log("TMSubscriber.save() saveTopUpArrangementForActivation=" + saveTopUpArrangementForActivation);
					if (saveTopUpArrangementForActivation && topupPaymentArrangement != null && topupPaymentArrangement.validate()) {
						log("TMSubscriber.save() calling getAccountHelperEJB().saveActivationTopUpArrangement");

						provider.getAccountLifecycleManager().saveActivationTopUpArrangement(
								String.valueOf(getBanId()),
								delegate.getPhoneNumber(),
								getEquipment().getSerialNumber(),
								topupPaymentArrangement.getDelegate(),
								provider.getUser());

						log("TMSubscriber.save() returned from getAccountHelperEJB().saveActivationTopUpArrangement");

					} else {
						if (topupPaymentArrangement == null) {
							log("TMSubscriber.save() topupPaymentArrangement is null");
						}
					}

				} catch (ActivationTopUpPaymentArrangementException ae) {
					log("TMSubscriber.save() ActivationTopUpPaymentArrangement ERROR: " + ae.getMessage());
					//throw ae;
				} catch (Throwable t) {
					log("TMSubscriber.save() ActivationTopUpPaymentArrangement ERROR: " + t.getMessage());
				}

				if (activationFeaturesPurchaseArray != null) {
					log("TMSubscriber.save() calling getSubscriberHelperEJB().saveActivationFeaturesPurchaseArrangement");
					provider.getSubscriberLifecycleManager().saveActivationFeaturesPurchaseArrangement(getDelegate(), 
							activationFeaturesPurchaseArray, provider.getUser());
					log("TMSubscriber.save() returned from getSubscriberHelperEJB().saveActivationFeaturesPurchaseArrangement");
				}
			}

		} catch (Throwable t) {
			log(t.getMessage());
		}

		CreateSubscriberException delayedCreateSubscriberException = null;
		try {
			if ((getDealerCode() == null || getDealerCode().trim().length() == 0) && !getAccount().isPostpaid() &&
					TMReferenceDataManager.NUMBER_LOCATION_POSTPAID.equals(getNumberGroup().getNumberLocation())) {
				setDealerCode(getNumberGroup().getDefaultDealerCode());
				setSalesRepId(getNumberGroup().getDefaultSalesCode());
			}
			if (isNull(getPhoneNumber())) {
				throw new TelusAPIException("no phone number has been reserved");
			}
			// String[] optionalSocs = (subscriberContractInfo == null)?new
			// String[0]:subscriberContractInfo.getServiceCodes();
			// ServiceAgreementInfo[] optionalSocs = (subscriberContractInfo ==
			// null)?new
			// ServiceAgreementInfo[0]:subscriberContractInfo.getServices0();
			// ServiceAgreementInfo[] optionalSocs = (contract == null)?new
			// ServiceAgreementInfo[0]:contract.getDelegate().getServices0(true);
			if (activation) {
				if (dealerHasDeposit &&
						(subscriberInfo.getDealerCode() == null || subscriberInfo.getDealerCode().length() == 0)) {
					throw new IllegalStateException("dealerHasDeposit == true, but dealerCode is unset");
				}
				// debug("provider.getSubscriberManagerEJB().createSubscriber(subscriberInfo,
				// optionalSocs,
				// activate="+activate+", waiveSearchFee="+waiveSearchFee+",
				// activationFeeChargeCode="+activationFeeChargeCode+",
				// dealerHasDeposit="+dealerHasDeposit+");");
				// -----------------------------------------------------------------------------
				// Since createSubscriber may fail after the subscriber is
				// actually created,
				// we want to call unregisterNewSubscriber first to prevent
				// releasing active
				// subscribers.
				// -----------------------------------------------------------------------------
				provider.unregisterNewSubscriber(subscriberInfo);
				// provider.getSubscriberManagerEJB().createSubscriber(subscriberInfo,
				// optionalSocs, activate, waiveSearchFee,
				// activationFeeChargeCode, dealerHasDeposit);
				SubscriberContractInfo subscriberContractInfo = (contract == null) ? null : contract.getDelegate();

				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
					// execute the inter-brand port activation logic
					interBrandPortCreateSubscriber(subscriberInfo, subscriberContractInfo, activate, srvValidation);

				} else {
					// execute the regular activation logic
					provider.getSubscriberLifecycleFacade().createSubscriber(
							subscriberInfo, subscriberContractInfo, activate,
							waiveSearchFee, activationFeeChargeCode,
							dealerHasDeposit, portedIn, srvValidation,
							portProcess, 0, null, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
					
					if (PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT.equals(portProcess)) {
						performInterBrandPortActivitiesWithNoExceptionThrown(activate);
					}
				}

				// call RCM to assign MIN-MDN relationship after the subscriber is successfully created 
				if (activate && subscriberInfo.isPCS() &&  !getEquipment().isHSIADummyEquipment() && !getEquipment().isVOIPDummyEquipment()) {
					try {
						activity = "assignTNResources";
						//provider.getRcmManager().assignMin(getPhoneNumber());
						EquipmentInfo equipmentInfo = getEquipment0().getDelegate();
						SubscriberLifecycleFacade subLifeCycleFacade = provider.getSubscriberLifecycleFacade();
						subLifeCycleFacade.assignTNResources(getPhoneNumber(), getSubscriberNetworkType(), 
								equipmentInfo.getProfile().getLocalIMSI(), equipmentInfo.getProfile().getRemoteIMSI());
						logSuccess(methodName, activity, "assign MIN for phone number [" + getPhoneNumber() + "], NetworkType[" + getSubscriberNetworkType() + "]"); 
					} catch (Throwable t) {
						// if the RCM update fails, just log it and continue
						logFailure(methodName, activity, t, "assign MIN failed for phone number [" + getPhoneNumber() + "]; exception ignored");
					}
				}
			}
					 
		} catch (Throwable t) {
			try {
				provider.getExceptionHandler().handleException(t, new ProviderDefaultExceptionTranslator() {

					@Override
					public TelusAPIException translateException(Throwable throwable) {
						if ( throwable instanceof CreateSubscriberException ) {
							return (CreateSubscriberException)throwable ;
						}
						return super.translateException(throwable);
					}

					@Override
					protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
						if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS.equals(errorId)) {
							return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.ALL_STEPS);
						} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS.equals(errorId)) {
							return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.ALL_STEPS_EXCEPT_MEMOS);
						} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_DISCOUNTS.equals(errorId)) {
							return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.APPLY_DISCOUNTS);
						} 
						return super.getExceptionForErrorId(errorId, cause);
					}
				});
				
			} catch (CreateSubscriberException cse) {
				delayedCreateSubscriberException = cse;
			} catch (TelusAPIException innerAPIEx) {
				throw innerAPIEx;
			}
		}

		try {
			// CDR - activation
			boolean needAsyncPerformActivationAfterRefresh = checkAsyncPerformActivationCondition();

			if (activation) {
				/**
				 * Putting a try-catch block for silent failure so that it won't stop the rest of the flow
				 */
				try {
					// update home province if this is the first activated
					// subscriber on this account
					if (activate && getAccount().getStatus() == AccountSummary.STATUS_TENTATIVE && getAccount().getActiveSubscribersCount() == 0) {
						provider.getAccountLifecycleManager().updateNationalGrowth(getAccount().getBanId(), null, subscriberInfo.getNumberGroup().getProvinceCode(),
								SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
					}

				} catch (Throwable t) {
					log(t.getMessage());
				}

				try {
					if (contract != null) {
						contract.commit(getDealerCode(), getSalesRepId());
					}
				} catch (TelusException te) {
					log(te.getMessage());
				} catch (Throwable t) {
					log(t.getMessage());
				}

				activation = false;
			} else {
				// Change address
				updateAddress();
				SubscriberInfo info = provider.getSubscriberManagerBean().updateSubscriber(subscriberInfo);
				provider.getInteractionManager0().subscriberSave(this);
				subscriberInfo.copyFrom(info);
			}

			// for holborn 
			// Naresh: added isHSIAEquipment and isVOIPEquipment checks to activate the subscriber without equipemnt for business connect STARTER and OFFICE seats.

			if (getEquipment().isHSPA() && !getEquipment().isHSIADummyEquipment() && !getEquipment().isVOIPDummyEquipment()) {
				try {
					EquipmentInfo equipmentInfo = getEquipment0().getDelegate();
					provider.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(), equipmentInfo.getAssociatedHandsetIMEI());

				} catch (Throwable t) {
					// if the SEMS update fails, just log it and continue
					logFailure(methodName, "assignEquipmentToPhoneNumber", t, "assign Equipment to Phone failed for phone number [" + getPhoneNumber() + "]; exception ignored");
				}
			}

			// Update SIM/IMEI
			setSIMMuleRelation(activation, activate);

			refresh(); // reload entire subscriber

			updateSubscriptionRole();

			//moved prepaid call from here to prior to KB call

			oldDelegate = null;
			if (accountSummary != null) {
				accountSummary.getAccount().refresh();
			}
			
		} catch (TelusException te) {
			log(te.getMessage());
		} catch (Throwable t) {
			log(t.getMessage());
		}
		
		if (delayedCreateSubscriberException != null) {
			handleCreateSubscriberException(delayedCreateSubscriberException);
		}
	}

	private void handleCreateSubscriberException(CreateSubscriberException e)
	throws IncompleteSubscriberCreationProcessException {
		throw new IncompleteSubscriberCreationProcessException(e, e.getIncompleteSteps());
	}

	// Updated for CDA phase 1B July 2018
	public void save(Date startServiceDate, ActivationOption selectedOption, ServicesValidation srvValidation) throws TelusAPIException {

		try {
			// Process the selected activation option
			setActivationOption(selectedOption, false);
			getActivationOption().apply();

			if (activation && startServiceDate != null && startServiceDate.compareTo(provider.getReferenceDataManager().getSystemDate()) > 0) {
				delegate.setStartServiceDate(startServiceDate);
			}
			
			save(true, srvValidation);
			
		} catch (TelusAPIException e) {
			throw e;
		} catch (Throwable t) {
			throw new TelusAPIException(t);
		}
	}

	public void activate(ServicesValidation srvValidation, String reason, String memoText)throws TelusAPIException{
		activate(reason, null, memoText, srvValidation);
	}

	public void activate(String reason, Date startServiceDate, String memoText, ServicesValidation srvValidation) throws TelusAPIException {
		
		String method = "activate(String,Date,String,ServicesValidation)";
		String activity = "activate";
		if (srvValidation == null) {
			srvValidation = new ServicesValidationInfo();
		}

		if (NetworkType.NETWORK_TYPE_HSPA.equals(getSubscriberNetworkType())) {
			((ServicesValidationInfo) srvValidation).setEquipmentServiceMatch(false);
		}

		assertSubscriberExists();

		try {
			delegate.setStartServiceDate(startServiceDate);
			delegate.setActivityReasonCode(reason);

			if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
				// execute the inter-brand port activate from reserved logic
				// retrieve the cancelled subscriber on the old (outgoing) brand
				Subscriber oldSub = retrieveLastPortedOutCancelledSubscriberByPhoneNumber(getPhoneNumber());
				try {
					provider.getSubscriberLifecycleFacade().activateReservedSubscriber(
							delegate, getContract0().getDelegate(), startServiceDate,
							getActivityReasonCode(), srvValidation, portProcess,
							oldSub.getBanId(), oldSub.getSubscriberId(), 
							SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade())
					);
				} catch(Throwable t) {
					// if the activate fails, just throw an exception
					throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR003), InterBrandPortRequestException.ERR003);
				}

				// set the subscriber port indicator in KB and send the SMS notification
				performInterBrandPortActivities();
			} else {
				validateAndDeactivateMVNESubscriber();
				
				// execute the regular activate from reserved logic
				provider.getSubscriberLifecycleFacade().activateReservedSubscriber(
						delegate, getContract0().getDelegate(), startServiceDate,
						getActivityReasonCode(), srvValidation,
						portProcess, 0, null, 
						SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade())
				);
				
				if (PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT.equals(portProcess)) {
					// set the subscriber port indicator in KB and send the SMS notification
					performInterBrandPortActivities();
				}
			}

			// call RCM to assign MIN-MDN relationship after the subscriber is successfully activated 
			if (isPCS() &&  !getEquipment().isHSIADummyEquipment() && !getEquipment().isVOIPDummyEquipment() ) {
				SubscriberLifecycleFacade subLifeCycleFacade= provider.getSubscriberLifecycleFacade();
				String localIMSI = null;
				String remoteIMSI = null;
				String networkType = getEquipment0().getNetworkType();

				if (getEquipment().isHSPA() && getEquipment().isUSIMCard()) {
					localIMSI = getEquipment0().getDelegate().getProfile().getLocalIMSI();
					remoteIMSI = getEquipment0().getDelegate().getProfile().getRemoteIMSI();
				}

				activity = "assignTNResources";
				try {
					subLifeCycleFacade.assignTNResources(getPhoneNumber(), networkType, localIMSI, remoteIMSI);
					logSuccess (method, activity, delegate, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+networkType+"]");
				} catch (Throwable t) {
					// if the RCM update fails, just log it and continue
					logFailure (method, activity, delegate, t, "PhoneNumber["+getPhoneNumber()+"], NetworkType["+networkType+"]");
				}

				if (getEquipment().isHSPA() && isFutureDated(startServiceDate) == false ) {
					activity="approveReservedEquipmentForPhoneNumber";
					try {
						provider.getProductEquipmentLifecycleFacade().asyncApproveReservedEquipmentForPhoneNumber(getPhoneNumber(), getEquipment().getSerialNumber(), getEquipment0().getDelegate().getAssociatedHandsetIMEI());
						logSuccess (method, activity, delegate, "PhoneNumber["+getPhoneNumber()+"], SerialNumber["+getEquipment().getSerialNumber()+"]");
					} catch (Throwable t) {
						// if the SEMS update fails, just log it and continue
						logFailure (method, activity, delegate, t, "PhoneNumber["+getPhoneNumber()+"], SerialNumber["+getEquipment().getSerialNumber()+"]");
					}
				}
			}			 

			// create memo if memoText passed in is not null
			if (memoText != null) {
				MemoInfo memo = new MemoInfo(delegate.getBanId(),
						ACTIVATION_MEMO_TYPE,
						delegate.getSubscriberId(),
						delegate.getProductType(), memoText);
				provider.getAccountLifecycleFacade().asyncCreateMemo(memo, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));				
			}
			// update home province if this is the first activated subscriber on
			// this account
			if (getAccount().getStatus() == AccountSummary.STATUS_TENTATIVE &&
					getAccount().getActiveSubscribersCount() == 0) {
				provider.getAccountLifecycleManager().updateNationalGrowth(
						getAccount().getBanId(), null,
						delegate.getMarketProvince(),
						SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
			}
			// set SIM/IMEI relationship
			setSIMMuleRelation(true, true);

		} catch(Throwable t) {
			TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}
	}
	
	/**
	 * This method is a copy of getFeatureParameterHistory with different query implemented in PL/SQL based on empty parameterNames.
	 * @param from
	 * @param to
	 * @return
	 * @throws TelusAPIException
	 */
	@Override
	public FeatureParameterHistory[] getFeatureParameterChangeHistory(Date from, Date to) throws TelusAPIException {
		try {
			if (from == null || to == null) {
				throw new TelusAPIException ("The Start Date and End Date cannot be null.");
			}
			return provider.getSubscriberLifecycleHelper().retrieveFeatureParameterHistory(getBanId(), 
					getSubscriberId(), getProductType(), null, from, to);

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	@Override
	public FeatureParameterHistory[] getFeatureParameterHistory(String[] parameterNames, Date from, Date to) throws TelusAPIException {
		try {
			return provider.getSubscriberLifecycleHelper().retrieveFeatureParameterHistory(getBanId(), 
					getSubscriberId(), getProductType(), parameterNames, from, to);

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	private void interBrandPortRestore(Date activityDate, String reason, String memoText, ServiceRequestHeader header) throws TelusAPIException {

		String methodName = "interBrandPortRestore";
		String activity = "inter-brand port restore";

		try {
			// retrieve the active subscriber on the old (outgoing) brand and get the reason code
			activity = "retrieve active subscriber on old brand";
			Subscriber outgoingSub = provider.getAccountManager0().findSubscriberByPhoneNumber0(getPhoneNumber());
			logSuccess(methodName, activity, delegate, outgoingSub.toString());

			// validate OSP account, ESN or PIN information
			activity = "validate OSP port request information";
			interBrandPortValidateOSPPortRequestData(outgoingSub, getPortRequest());
			logSuccess(methodName, activity, delegate, outgoingSub.toString());

			// get the reason code
			activity = "determine the activity reason code";
			String activityReasonCode = getInterBrandPortActivityReasonCode(this, outgoingSub);
			logSuccess(methodName, activity, delegate, activityReasonCode);

			// cancel the subscriber on the old (outgoing) brand
			activity = "cancel outgoing subscriber";
			interBrandPortCancelOutgoingSubscriber(outgoingSub, activityReasonCode, activityDate, header);

			try {
				// resume the cancelled subscriber on the new (incoming) brand
				activity = "resume cancelled subscriber on incoming brand";
				provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(
						delegate, reason, memoText, true, portProcess,
						outgoingSub.getBanId(), outgoingSub.getSubscriberId(), new SubscriberResumedPostTaskInfo().repairCommunicationSuite(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade())); 
				logSuccess(methodName, activity, delegate, null);

			} catch(Throwable t) {
				logFailure(methodName, activity, delegate, t, null);
				interBrandPortResumeCancelledSubscriber(((TMSubscriber)outgoingSub).getDelegate(), activityReasonCode, header);
				throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR007), InterBrandPortRequestException.ERR007);
			}

		} catch(PortOutEligibilityException poee) {
			logFailure(methodName, activity, delegate, poee, null);
			throw poee;
		} catch(InterBrandPortRequestException ibpre) {
			logFailure(methodName, activity, delegate, ibpre, null);
			throw ibpre;
		} catch(UnknownSubscriberException use) {
			logFailure(methodName, activity, delegate, use, null);
			throw new InterBrandPortRequestException(use, provider.getApplicationMessage(InterBrandPortRequestException.ERR006), InterBrandPortRequestException.ERR006);
		} catch(TelusAPIException tapie) {		  
			logFailure(methodName, activity, delegate, tapie, null);
			throw tapie;
		} catch(Throwable t) {
			logFailure(methodName, activity, delegate, t, null);
			throw new TelusAPIException(t);
		}
	}

	private void interBrandPortCreateSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, boolean activate, ServicesValidation srvValidation)
	throws TelusAPIException {

		String methodName = "interBrandPortCreateSubscriber";
		String activity = "inter-brand port create subscriber";

		try {
			// retrieve the active subscriber on the old (outgoing) brand and get the reason code
			activity = "retrieve active subscriber on old brand";
			Subscriber outgoingSub = provider.getAccountManager0().findSubscriberByPhoneNumber0(getPhoneNumber());
			logSuccess(methodName, activity, delegate, outgoingSub.toString());

			// validate OSP account, ESN or PIN information
			activity = "validate OSP port request information";
			interBrandPortValidateOSPPortRequestData(outgoingSub, getPortRequest());
			logSuccess(methodName, activity, delegate, outgoingSub.toString());

			// get the reason code
			activity = "determine the activity reason code";
			String activityReasonCode = getInterBrandPortActivityReasonCode(this, outgoingSub);
			logSuccess(methodName, activity, delegate, activityReasonCode);

			// cancel the subscriber on the old (outgoing) brand and reserve the number on the new (incoming) brand
			activity = "cancel outgoing subscriber and reserve incoming subscriber";
			Date activityDate = provider.getReferenceDataManager().getLogicalDate();
			interBrandPortCancelOutgoingSubscriber(outgoingSub, activityReasonCode, activityDate, null);
			SubscriberInfo reservedSubInfo = interBrandPortReserveIncomingSubscriber(((TMSubscriber)outgoingSub).getDelegate(), getNumberGroup(), getPhoneNumber(), activityReasonCode, null);

			try {
				// create the subscriber on the new (incoming) brand
				activity = "create subscriber on incoming brand";
				provider.getSubscriberLifecycleFacade().createSubscriber(
						reservedSubInfo, subscriberContractInfo, activate, waiveSearchFee,
						activationFeeChargeCode, dealerHasDeposit, portedIn, srvValidation,
						portProcess, outgoingSub.getBanId(), outgoingSub.getSubscriberId(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));

				//preserve the subscriberId from subscriber reservation
				delegate.setSubscriberId(reservedSubInfo.getSubscriberId() );
				logSuccess(methodName, activity, delegate, null);		 

			} catch(Throwable t) {
				// if the create fails, release the reserved subscriber and perform rollback
				logFailure(methodName, activity, delegate, t, null);
				interBrandPortReleaseReservedNumber(reservedSubInfo, true);
				interBrandPortResumeCancelledSubscriber(((TMSubscriber)outgoingSub).getDelegate(), activityReasonCode, null);
				throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR003), InterBrandPortRequestException.ERR003);
			}

			performInterBrandPortActivitiesWithNoExceptionThrown (activate);
			 
		} catch(Throwable t) {
			logFailure(methodName, activity, delegate, t, null);
			provider.getExceptionHandler().handleException(t, new ProviderDefaultExceptionTranslator() {

				@Override
				public TelusAPIException translateException(Throwable throwable) {
					if ( throwable instanceof CreateSubscriberException ) {
						return new IncompleteSubscriberCreationProcessException(throwable, ((CreateSubscriberException)throwable).getIncompleteSteps()) ;
					} else if ( throwable instanceof UnknownSubscriberException ) {
						return new InterBrandPortRequestException(throwable, provider.getApplicationMessage(InterBrandPortRequestException.ERR006), InterBrandPortRequestException.ERR006);
					}
					//PortOutEligibilityException 
					// and InterBrandPortRequestException subclass of TelusAPIException, will handle by super.trnaslateException 

					return super.translateException(throwable);
				}

				@Override
				protected TelusAPIException getExceptionForErrorId(String errorId, Throwable cause) {
					if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS.equals(errorId)) {
						return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.ALL_STEPS);
					} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS.equals(errorId)) {
						return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.ALL_STEPS_EXCEPT_MEMOS);
					} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_DISCOUNTS.equals(errorId)) {
						return new CreateSubscriberException(cause, IncompleteSubscriberCreationProcessException.APPLY_DISCOUNTS);
					} 
					return super.getExceptionForErrorId(errorId, cause);
				}

			});
		}
	}

	private void interBrandPortResumeCancelledSubscriber(SubscriberInfo outgoingSub, String activityReasonCode, ServiceRequestHeader header) throws TelusAPIException {

		String methodName = "interBrandPortResumeCancelledSubscriber";
		String activity = "resume subscriber cancellation on outgoing brand";
		try {
			// if the calling activity failed, attempt to rollback the cancellation on the old (outgoing) brand
			provider.getSubscriberLifecycleFacade().resumeCancelledSubscriber(outgoingSub, activityReasonCode,
					"Inter-brand port rollback from BAN [" + delegate.getBanId() + "] to BAN [" + outgoingSub.getBanId() + "] and subscriber [" + outgoingSub.getSubscriberId() + "]", true,
					PortInEligibility.PORT_PROCESS_ROLLBACK, outgoingSub.getBanId(), outgoingSub.getSubscriberId(), null, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			logSuccess(methodName, activity, delegate, outgoingSub.toString());

			// call RCM to assign the MIN after the (old) outgoing subscriber is resumed
			// note: this call handles the (old) outgoing subscriber, so it calls RCM directly
			if (outgoingSub.isPCS()) {
				activity = "assign MIN";
				try {
					// provider.getRcmManager().assignMin(outgoingSub.getPhoneNumber());
					EquipmentInfo equipmentInfo = getEquipment0().getDelegate();
					provider.getSubscriberLifecycleFacade().assignTNResources(getPhoneNumber(), getSubscriberNetworkType(), equipmentInfo.getProfile().getLocalIMSI(),
							equipmentInfo.getProfile().getRemoteIMSI());
					logSuccess(methodName, activity, delegate, outgoingSub.toString());
				} catch (Throwable t) {
					// if the RCM update fails, just log it and continue
					logFailure(methodName, activity, delegate, t, "exception ignored for PhoneNumber" + getPhoneNumber());
				}
			}

			// set the subscriber port indicator in KB back to Subscriber.PORT_TYPE_PORT_IN if necessary and with the correct date
			if (outgoingSub.getPortType() != null && outgoingSub.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN)) {
				activity = "set subscriber port indicator";
				try {
					provider.getSubscriberManagerBean().setSubscriberPortIndicator(outgoingSub.getPhoneNumber(), outgoingSub.getPortDate());
					logSuccess(methodName, activity, delegate, outgoingSub.toString());
				} catch (Throwable t) {
					// if the port indicator update fails, just log it and continue
					logFailure(methodName, activity, delegate, t, "exception ignored for PhoneNumber" + getPhoneNumber());
				}
			}

			// write to SRPDS
			if (needToCallSRPDS(header)) {
				reportChangeSubscriberStatus(outgoingSub, Subscriber.STATUS_CANCELED, outgoingSub.getStatus(), activityReasonCode, outgoingSub.getStatusDate(), header);
			}

		} catch (Throwable t) {
			logFailure(methodName, activity, delegate, t, outgoingSub.toString());
			throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR004), InterBrandPortRequestException.ERR004);
		}
	}

	/**
	 * This method will cancel the outgoing subscriber in an inter-brand porting scenario.
	 * The logic implemented here is similar, but not identical to the logic contained in the
	 * SubscriberActivationService, but should be kept in synch accordingly.
	 * 
	 * @param outgoingSub
	 * @param activityReasonCode
	 * @param activityDate
	 * 
	 * @throws TelusAPIException
	 */
	private void interBrandPortCancelOutgoingSubscriber(Subscriber outgoingSub, String activityReasonCode, Date activityDate, ServiceRequestHeader header)
	throws TelusAPIException {

		String methodName = "interBrandPortCancelOutgoingSubscriber";
		String activity = "interBrandPortCancelOutgoingSubscriber";
		try {			 
			// check if the subscriber is suspended or cancelled - if suspended, restore the subscriber first 
			// this is the workaround until Amdocs fixes their defect
			if (outgoingSub.getStatus() == STATUS_SUSPENDED) {
				// restore the subscriber if current status is suspended
				provider.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(
						((TMSubscriber)outgoingSub).getDelegate(), 
						activityDate, "POUT", "", false, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				outgoingSub.refresh();

			} else if (outgoingSub.getStatus() == STATUS_CANCELED) {
				// otherwise, throw an exception if current status is cancelled
				logFailure(methodName, activity, delegate, null, "outgoing subscriber is already cancelled; " + outgoingSub.toString());
				throw new InterBrandPortRequestException(provider.getApplicationMessage(InterBrandPortRequestException.ERR001), InterBrandPortRequestException.ERR001);
			}

			// Comm Suite logic , Retrieve the CommunicationSuiteInfo if subscriber has any companion subscribers, if so cancel the companion subscribers and break the communicationSuite.
			CommunicationSuiteInfo  commSuiteInfo = ((TMSubscriber) outgoingSub).getCommunicationSuite(false);
					
			// check the account status if the outgoing subscriber is cancelled
			switch (outgoingSub.getAccountStatusChangeAfterCancel()) {

			case AccountSummary.STATUS_SUSPENDED:
				//	suspend the account if only suspended subscribers will be left after outgoing subscriber is cancelled
				activity = "suspend account on outgoing brand";
				provider.getAccountLifecycleFacade().suspendAccountForPortOut(
						outgoingSub.getBanId(), activityReasonCode, 
						activityDate, "Y",commSuiteInfo,
						SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));

				logSuccess(methodName, activity, delegate, outgoingSub.toString());

				// cancel the active subscriber on the old (outgoing) brand
				activity = "cancel active subscriber on outgoing brand";
				
				provider.getSubscriberLifecycleFacade().cancelPortedInSubscriber(outgoingSub.getBanId(), 
						outgoingSub.getSubscriberId(), 
						activityReasonCode, activityDate, "Y", true, getSubscriberId(), commSuiteInfo,
						true, header, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			
				logSuccess(methodName, activity, delegate, outgoingSub.toString());							 
				break;							 

			case AccountSummary.STATUS_CANCELED:
				// cancel the account if no active or suspended subscribers will be left after outgoing subscriber is cancelled
				activity = "cancel account on outgoing brand";
				
				provider.getAccountLifecycleFacade().cancelAccountForPortOut(outgoingSub.getBanId(), activityReasonCode, activityDate, "Y", true, 
						commSuiteInfo, false,SessionUtil.getSessionId(provider.getAccountLifecycleManager()));

				logSuccess(methodName, activity, delegate, outgoingSub.toString());
				break;

			default:
				// otherwise, just cancel the outgoing subscriber
				activity = "cancel active subscriber on outgoing brand";
				provider.getSubscriberLifecycleFacade().cancelPortedInSubscriber(outgoingSub.getBanId(), outgoingSub.getSubscriberId(), 
						activityReasonCode, activityDate, "Y", true, getSubscriberId(),  commSuiteInfo,
						true, header, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				logSuccess(methodName, activity, delegate, outgoingSub.toString());
				break;
			}

			// call RCM to release the MIN after the old (outgoing) subscriber is cancelled
			//	note: this call handles the (old) outgoing subscriber, so it calls RCM directly
			if (outgoingSub.isPCS() && isFutureDated(activityDate) == false) {
				activity = "release MIN";
				try {
					SubscriberLifecycleFacade subLifeCycleFacade= provider.getSubscriberLifecycleFacade();
					subLifeCycleFacade.releaseTNResources(outgoingSub.getPhoneNumber(), ((TMEquipment) outgoingSub.getEquipment()).getDelegate().getNetworkType());
					logSuccess(methodName, activity, delegate, outgoingSub.toString());
				} catch(Throwable t) {
					// if the RCM update fails, just log it and continue
					logFailure(methodName, activity, delegate, t, "exception ignored for PhoneNumber"+getPhoneNumber());
				}
			}

			if (needToCallSRPDS(header)) {
				reportChangeSubscriberStatus( ((TMSubscriber)outgoingSub).getDelegate(), ' ', Subscriber.STATUS_CANCELED, activityReasonCode, activityDate, header );
			}

		} catch(Throwable t) {
			logFailure(methodName, activity, delegate, t, outgoingSub.toString());
			provider.getExceptionHandler().handleException(t, new TelusExceptionTranslator() {

				@Override
				public TelusAPIException translateException(Throwable throwable) {
					if(throwable instanceof InterBrandPortRequestException){
						return (InterBrandPortRequestException)throwable;
					}else{
						return new InterBrandPortRequestException(throwable, provider.getApplicationMessage(InterBrandPortRequestException.ERR001), InterBrandPortRequestException.ERR001);
					}
				}
			});
		}	 
	}


	private SubscriberInfo interBrandPortReserveIncomingSubscriber(SubscriberInfo outgoingSub, NumberGroup numberGroup, String phoneNumber, String activityReasonCode, ServiceRequestHeader header)
			throws TelusAPIException {

		String methodName = "interBrandPortReserveIncomingSubscriber";
		String activity = "reserve phone number on incoming brand";
		try {
			// reserve the phone number on the new (incoming) brand			 
			TMPhoneNumberReservation reservation = new TMPhoneNumberReservation();
			reservation.setNumberGroup(numberGroup);
			reservation.setPhoneNumberPattern(phoneNumber);
			SubscriberInfo reservedSubInfo = provider.getSubscriberManagerBean().reservePortedInPhoneNumber(
					delegate, reservation.getPhonenumberReservation0(), true);
			logSuccess(methodName, activity, reservedSubInfo.toString());			 
			return reservedSubInfo;

		} catch(Throwable t) {
			logFailure(methodName, activity, delegate, t, "phone number [" + phoneNumber + "]");
			interBrandPortResumeCancelledSubscriber(outgoingSub, activityReasonCode, header);
			throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR002), InterBrandPortRequestException.ERR002);
		}
	}

	private Subscriber retrieveLastPortedOutCancelledSubscriberByPhoneNumber(String phoneNumber) throws TelusAPIException {

		String methodName = "retrieveLastPortedOutCancelledSubscriberByPhoneNumber";
		String activity = "retrieve last ported-out cancelled subscriber";
		try {
			// retrieve the last ported-out cancelled subscriber based on the given phone number
			Subscriber[] subscriberArray = provider.getAccountManager0().findSubscribersByPhoneNumber0(getPhoneNumber(), 2, true);
			for (int i = 0; i < subscriberArray.length; i++) {
				if (subscriberArray[i].getStatus() == Subscriber.STATUS_CANCELED &&
						((TMSubscriber)subscriberArray[i]).getDelegate().getPortType().equals(Subscriber.PORT_TYPE_PORT_OUT)) {
					return subscriberArray[i];
				}		
			}
			logFailure(methodName, activity, delegate, null, "phone number [" + phoneNumber + "]");
			throw new InterBrandPortRequestException(provider.getApplicationMessage(InterBrandPortRequestException.ERR006), InterBrandPortRequestException.ERR006); 

		} catch(Throwable t) {
			logFailure(methodName, activity, delegate, t, "phone number [" + phoneNumber + "]");
			throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR006), InterBrandPortRequestException.ERR006);
		}
	}

	private void interBrandPortReleaseReservedNumber(SubscriberInfo incomingSub, boolean ignoreReleaseFailure) throws TelusAPIException {

		String methodName = "interBrandPortReleaseReservedNumber";
		String activity = "release interBrand reserved subscriber on target ban";
		String activityContext = "releasePortedInSubscriber :  incomingSubId [" + incomingSub.getPhoneNumber() + "] , ban [" + incomingSub.getBanId() + "] ,ignoreReleaseFailure [ " + ignoreReleaseFailure + "]";
		try { 
			//release the number in KB on target ban
			provider.getSubscriberManagerBean().releasePortedInSubscriber(incomingSub);	
			logSuccess(methodName, activity, incomingSub, activityContext);
		} catch(Throwable t) {
			logFailure(methodName, activity, incomingSub, t, activityContext);
			if(!ignoreReleaseFailure){
				throw new InterBrandPortRequestException(t, provider.getApplicationMessage(InterBrandPortRequestException.ERR005), InterBrandPortRequestException.ERR005);
			}
		}

	}
	
	private String getInterBrandPortActivityReasonCode(Subscriber incomingSub, Subscriber outgoingSub) throws TelusAPIException {
		String outgoingKey = outgoingSub.getBrandId() + outgoingSub.getProductType() + (outgoingSub.getAccount().isPostpaid() == true ? POSTPAID : PREPAID);
		String incomingKey = incomingSub.getBrandId() + incomingSub.getProductType() + (incomingSub.getAccount().isPostpaid() == true ? POSTPAID : PREPAID);
		HashMap interBrandPortActivityReasonCodesKeyMap = AppConfiguration.getInterBrandPortActivityReasonCodesKeyMap();
		String reasonCode = (String)interBrandPortActivityReasonCodesKeyMap.get(outgoingKey) + (String)interBrandPortActivityReasonCodesKeyMap.get(incomingKey);
		if (AppConfiguration.getInterBrandPortActivityReasonCodes().contains(reasonCode))
			return reasonCode;
		else
			throw new InterBrandPortRequestException(provider.getApplicationMessage(InterBrandPortRequestException.ERR009), InterBrandPortRequestException.ERR009);
	}	 


	@Override
	public String[] getSubscriberInterBrandPortActivityReasonCodes() {
		return AppConfiguration.getInterBrandPortActivityReasonCodesArray();		 
	}


	/**
	 * New for Prepaid 3.4 (Oct 2007)
	 * @param pServiceAgreementInfoArray
	 * @return
	 */
	private ActivationFeaturesPurchaseArrangementInfo[] populateActivationFeaturesPurchaseArrangement (ServiceAgreementInfo[] pServiceAgreementInfoArray) {
		if (pServiceAgreementInfoArray != null && pServiceAgreementInfoArray.length > 0) {
			ArrayList afpaList = new ArrayList();
			for (int i = 0; i < pServiceAgreementInfoArray.length; i++) {
				if (pServiceAgreementInfoArray[i].isWPS()) {
					ActivationFeaturesPurchaseArrangementInfo afpa = new ActivationFeaturesPurchaseArrangementInfo();
					afpa.setAutoRenewIndicator(pServiceAgreementInfoArray[i].getAutoRenew()+"");
					afpa.setAutoRenewFundSource(pServiceAgreementInfoArray[i].getAutoRenewFundSource());
					afpa.setPurchaseFundSource(pServiceAgreementInfoArray[i].getPurchaseFundSource());
					afpa.setFeatureId(pServiceAgreementInfoArray[i].getCode());
					afpaList.add(afpa);
				}
			}
			return (afpaList.size() > 0) ? (ActivationFeaturesPurchaseArrangementInfo[]) afpaList.toArray(new ActivationFeaturesPurchaseArrangementInfo[afpaList.size()]) : null;
		}else {
			return null;
		}
	}

	private void interBrandPortValidateOSPPortRequestData(Subscriber outgoingSub, PortRequest portRequest) throws PortOutEligibilityException, TelusAPIException {

		if (portRequest != null) {
			// Defect 123541 - if the account PIN is NULL , will be substituted with an empty String
			//                 to avoid a NullPointerException when calling equals(...)
			String accountPIN = outgoingSub.getAccount().getPin();

			// only one of the following pieces of data needs to match in order for the request to be considered valid 
			if (portRequest.getOSPAccountNumber() != null && String.valueOf(outgoingSub.getBanId()).equals(portRequest.getOSPAccountNumber())) {
				return;
			}
			if (portRequest.getOSPSerialNumber() != null) {
				//changes to support both primary serial number and HSPA handset IMEI
				Equipment outgoingSubEquipment = outgoingSub.getEquipment();
				if  (outgoingSubEquipment.getSerialNumber().equals(portRequest.getOSPSerialNumber())) { 
					return;
				}		
				if (outgoingSubEquipment.isUSIMCard()) { //if it's USIM card, then check the last associated handset's IMEI 
					if ( portRequest.getOSPSerialNumber().equals( ((USIMCardEquipment)outgoingSubEquipment).getLastAssociatedHandsetIMEI() ) )
						return;
				}
			}		
			if (portRequest.getOSPPin() != null && (accountPIN == null ? "" : accountPIN).equals(portRequest.getOSPPin())) {
				return;
			}
			// otherwise, throw a PortOutEligibilityException with the appropriate message
			if (portRequest.getOSPAccountNumber() != null && !portRequest.getOSPAccountNumber().equals("")
					&& !String.valueOf(outgoingSub.getBanId()).equals(portRequest.getOSPAccountNumber())) {
				throw new PortOutEligibilityException(provider.getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ACCOUNT_NUMBER));
			}
			if (portRequest.getOSPSerialNumber() != null && !portRequest.getOSPSerialNumber().equals("")
					&& !outgoingSub.getEquipment().getSerialNumber().equals(portRequest.getOSPSerialNumber())) {
				throw new PortOutEligibilityException(provider.getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ESN));
			}
			if (portRequest.getOSPPin() != null && !portRequest.getOSPPin().equals("")
					&& !(accountPIN == null ? "" : accountPIN).equals(portRequest.getOSPPin())) {
				throw new PortOutEligibilityException(provider.getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_PIN));
			}
		}
		// if everything is null or empty, throw a PortOutEligibilityException for invalid OSP account number
		throw new PortOutEligibilityException(provider.getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ACCOUNT_NUMBER));
	}

	public void save(boolean activate, ActivationOption selectedOption, PortInEligibility portInEligibility, ServicesValidation srvValidation) throws PortRequestException, TelusAPIException {

		String methodName = "save";
		String portRequestId = null;
		String activity = null;

		// 0.1 Determine the port process from the PortInEligibility object
		determinePortProcessType(portInEligibility);

		if (portedIn) {
			// 1.1 Create port requests in PRM
			activity = "create port request";
			try {
				if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {			 
					if (provider.getPortRequestManager().testPortOutEligibility(getPhoneNumber(), PortOutEligibility.NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS).isEligible() == true) {
						portRequestId = createPortRequest(portInEligibility.getIncomingBrandId(), portInEligibility.getOutgoingBrandId());
						logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");
					} else {
						throw new PortOutEligibilityException("phone number [" + getPhoneNumber() + "] is not eligible to port out");
					}

				} else if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT) || portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
					portRequestId = createPortRequest();
					if (activate) {
						validateAndDeactivateMVNESubscriber();
					}
					else{
						validateMVNESubscriber();
					}
					logSuccess(methodName, activity, "portRequestId [" + portRequestId + "]");

				} else {
					throw new PortRequestException("unknown port process exception [" + portProcess + "]");
				}

			} catch(TelusAPIException tae) {
				logFailure(methodName, activity, tae, null);
				throw tae;
			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
				provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
			}
		}
		
		IncompleteSubscriberCreationProcessException softException = null;
		
		try {			 
			// 2.1 Create subscriber in KB
			activity = "create subscriber in KB with activate [" + activate + "]";
			save(activate, selectedOption, srvValidation);
			logSuccess(methodName, activity, null);
		} catch (IncompleteSubscriberCreationProcessException ispe) {
			// 2.2 Catch soft (non-fatal) exception, let the process to complete, then throw it at the very end
			//     so that port request will not be cancelled due to soft exception, which shouldn't stop the activation process.
			logFailure(methodName, activity, ispe, null);
			softException = ispe;
		} catch(TelusAPIException e) {
			logFailure(methodName, activity, e, null);
			// 2.2 Cancel the port request in case of save failed in KB
			cancelPortRequest(portRequestId, methodName);
			// 2.3 Throw the original exception
			throw e;
		} catch(Throwable t) {
			logFailure(methodName, activity, t, null);
			// 2.2 Cancel the port request in case of save failed in KB
			cancelPortRequest(portRequestId, methodName);
			// 2.3 Chain the original throwable
			throw new TelusAPIException(t);
		}

		if (activate && portedIn) {
			// NOTE: Only for activation save, not for reservation save
			// 3.1 Update the port request in PRM, if we have successfully activated the subscriber
			activity = "submit port request [" + portRequestId + "]";
			try {
				provider.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, provider.getApplication());				
				logSuccess(methodName, activity, null);

			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new PRMSystemException("submit port request - in save() failed for id [" + portRequestId + "]; cause: " + t.toString(),t);
			}
		}

		portRequest = null;

		if (softException != null) {
			throw softException;
		}
	}
	private void cancelPortRequest(String portRequestId, String callingMethodName) {
		if (portedIn) {
			String activity = "cancel port request [" + portRequestId + "]";
			try {
				provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, BILLING_SYSTEM_FAILED, provider.getApplication());
				logSuccess(callingMethodName, activity, null);

			} catch(Throwable t) {
				logFailure(callingMethodName, activity, t, null);
			}
		}
	}

	public void activate(String reason, Date startServiceDate, String memoText, boolean isPortIn, boolean modifyPortRequest, ServicesValidation srvValidation)
			throws PortRequestException, PRMSystemException, TelusAPIException {

		String portRequestId = null;
		String methodName = "activate";
		String activity;

		if (isPortIn) {
			// 1.1 retrieve last port request
			activity = "retrieve last port request";
			try {
				TMPortRequest portRequest = (TMPortRequest) getPortRequest(); 
				portRequestId = portRequest.getPortRequestId();
				logSuccess(methodName, activity, " requestId [" + portRequestId + "]");

				// 1.1.1 Determine the port process from the last port request
				determinePortProcessType(portRequest);

			} catch(TelusAPIException tae) {
				logFailure(methodName, activity, tae, null);
				throw tae;
			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new TelusAPIException( t );
			}

			if (modifyPortRequest) {
				// 1.2 recreate port request; cancel old one then create a new one
				activity = "cancel and/or create port request due to change, requestId[" + portRequestId +"]";
				try {

					//getPortRequestSO().cancelPortInRequest(portRequestId, CANCEL_AND_RESUBMIT_WPR);
						provider.getSubscriberLifecycleFacade().cancelPortInRequest(portRequestId, CANCEL_AND_RESUBMIT_WPR, provider.getApplication());
					logSuccess(methodName, "cancel port request due to change", "requestId [" + portRequestId + "]");

					if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
						portRequestId = createPortRequest(portRequest.getIncomingBrandId(), portRequest.getOutgoingBrandId());
					} else {
						portRequestId = createPortRequest();
					}
					logSuccess(methodName, "re-created port request due to change", " new requestId [" + portRequestId + "]");
				}catch(ApplicationException ae){
					logFailure(methodName, activity, ae, null);
					TelusExceptionTranslator telusExceptionTranslator= new ProviderWNPExceptionTranslator(provider);
					provider.getExceptionHandler().handleException(ae,telusExceptionTranslator);
				} catch(PortRequestException pre) {
					logFailure(methodName, activity, pre, null);
					throw pre;
				} catch(TelusAPIException tae) {
					logFailure(methodName, activity, tae, null);
					throw tae;
				} catch(Throwable e) {
					logFailure(methodName, activity, e, null);
					throw new TelusAPIException(e);
				}
			}
			
			portRequest = null;
		}

		// 2.1 activate the subscriber
		activity = "activate";
		try {
			activate(reason, startServiceDate, memoText, srvValidation);
			logSuccess(methodName, activity, null);

		} catch(TelusAPIException tae) {
			logFailure(methodName, activity, tae, null);
			throw tae;
		} catch(Throwable t) {
			logFailure(methodName, activity, t, null);
			throw new TelusAPIException(t);
		}

		if (isPortIn) {			 
			// 3.1 submit port request
			activity = "submit port request [" + portRequestId + "]";
			try {
				//				getPortRequestSO().submitPortInRequest(portRequestId);
				provider.getSubscriberLifecycleFacade().submitPortInRequest(portRequestId, provider.getApplication());				
				logSuccess(methodName, activity, null);
			} catch(Throwable t) {
				logFailure(methodName, activity, t, null);
				throw new PRMSystemException("submit port request - in activate() failed for id [" + portRequestId + "]; cause: " + t.toString(),t);
			}
		}
	}

	private void checkForVoicemailService() throws InvalidServiceChangeException, TelusAPIException {

		if (contract==null) return;

		List vmOrphanFeatures = contract.getVoicemailOrphanFeatures();

		if ( vmOrphanFeatures.isEmpty()==false ) {
			InvalidServiceChangeException isce = null;
			isce = new InvalidServiceChangeException(InvalidServiceChangeException.REQUIRED_SERVICE_IS_MISSING,"Service " +
					((ContractFeature) vmOrphanFeatures.get(0)).getServiceCode() + " cannot be added without VM service");
			log("InvalidServiceChangeException occurred: " + isce);
			throw isce;
		}
	}

	/**
	 * Gets parameter value as date
	 * nb - null if not valid date
	 * Copied from SmartFeatureParameter
	 * @return
	 */
	private Date formatFBCBirthdate(String value) {
		// req - don't support Feb 29
		int DEFAULT_YEAR = 2001;
		Calendar cal = Calendar.getInstance();
		int day = Integer.valueOf(value.substring(2)).intValue();
		int month = Integer.valueOf(value.substring(0, 2)).intValue() - 1;
		cal.set(Calendar.YEAR, DEFAULT_YEAR);
   		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, day);
		Date date = cal.getTime();

		if (day != cal.get(Calendar.DATE) && month != cal.get(Calendar.MONTH))
			return null;
 
		return date;
	}

	protected void saveActivationFeaturesPurchaseAgreement() throws TelusAPIException {
		if (getAccount().isPrepaidConsumer()) {
			ActivationFeaturesPurchaseArrangementInfo[] activationFeaturesPurchaseArray = null;
			activationFeaturesPurchaseArray = populateActivationFeaturesPurchaseArrangement((ServiceAgreementInfo[]) getContract0().getDelegate().getServices());

			if (activationFeaturesPurchaseArray != null) {
				try {
					log("TMSubscriber.saveActivationFeaturesPurchaseAgreement() calling getSubscriberHelperEJB().saveActivationFeaturesPurchaseArrangement");
					provider.getSubscriberLifecycleManager().saveActivationFeaturesPurchaseArrangement(getDelegate(), 
							activationFeaturesPurchaseArray, provider.getUser());

					log("TMSubscriber.saveActivationFeaturesPurchaseAgreement() returned from getSubscriberHelperEJB().saveActivationFeaturesPurchaseArrangement");
				} catch (Throwable t) {
					log(t.getMessage());
				}
			}
		}
	}

	// This method returns ServiceAgreementInfo if NOT saved contract contains prepaid calling circle SOC 
	// in any status (add, delete or update)

	private ServiceAgreementInfo getPrepaidCallingCircleService() throws TelusAPIException {
		ServiceAgreementInfo[] allServices = this.getContract0().getDelegate().getServices0(true);
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getService().isWPS() 
					&& (aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
							|| aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))){
				return aService;
			}
		}		
		return null;
	}
	
	private ServiceAgreementInfo getKbMappedPrepaidService(String kbMappedPrepaidServiceCode) throws TelusAPIException {
		ServiceAgreementInfo[] allServices = this.getContract0().getDelegate().getServices0(true);
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getCode().trim().equals(kbMappedPrepaidServiceCode.trim())){
				return aService;
			}
		}		
		return null;
	}

	private ContractFeature getKbCallingCircleFeature(ServiceAgreementInfo kbMappedPrepaidService)	{
		ContractFeature [] allfeatures = kbMappedPrepaidService.getFeatures();
		ContractFeature feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE)
					|| feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
				return feature;
		}
		return null;
	}

	protected boolean needToCallSRPDS(ServiceRequestHeader header) {
		return (header!=null && AppConfiguration.isSRPDSEnabled()==true);
	}

	private void reportChangeSubscriberStatus(SubscriberInfo subscriberinfo, char oldSubscriberStatus, char newSubscriberStatus, String activityReasonCode, Date activityDate,
			ServiceRequestHeader header) throws TelusAPIException {
		((TMServiceRequestManager) provider.getServiceRequestManager()).reportChangeSubscriberStatus(subscriberinfo.getBanId(), subscriberinfo, subscriberinfo.getDealerCode(),
				subscriberinfo.getSalesRepId(), provider.getUser(), oldSubscriberStatus, newSubscriberStatus, activityReasonCode, activityDate, header);
	}
	
	@Override
	public void activate(String reason, Date startServiceDate, String memoText,	ServiceRequestHeader header) throws TelusAPIException {

		char oldStatus = delegate.getStatus();
		activate(reason, startServiceDate, memoText);
		if ( needToCallSRPDS(header)) {
			reportChangeSubscriberStatus(delegate, oldStatus, Subscriber.STATUS_ACTIVE, reason, startServiceDate, header);
		}
	}
 
	@Override
	public void changeEquipment(IDENEquipment newIDENEquipment,
			String dealerCode, String salesRepCode, String requestorId,
			String repairId, String swapType,
			MuleEquipment associatedMuleEquipment, char allowDuplicateSerialNo,
			ServiceRequestHeader header) throws TelusAPIException,
			SerialNumberInUseException, InvalidEquipmentChangeException {

		Equipment currentEquipment = getEquipment();
		Equipment cuurentMule = null;
		if ( associatedMuleEquipment!=null && currentEquipment instanceof SIMCardEquipment ) {
			cuurentMule =((SIMCardEquipment) currentEquipment).getLastMule();
		}
		changeEquipment(newIDENEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, associatedMuleEquipment, allowDuplicateSerialNo);

		if ( needToCallSRPDS(header)) {
			((TMServiceRequestManager)provider.getServiceRequestManager())
			.reportChangeEquipment(delegate.getBanId(), delegate.getSubscriberId(),
					delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), 
					currentEquipment, getEquipment(), repairId, swapType, cuurentMule, associatedMuleEquipment, header);
		}
	}

	@Override
	public void changePhoneNumber(AvailablePhoneNumber availablePhoneNumber, boolean changeOtherNumbers, String dealerCode, String salesRepCode, String reasonCode, ServiceRequestHeader header)
			throws TelusAPIException, PhoneNumberException, PhoneNumberInUseException {

		String currentSubId = delegate.getSubscriberId();
		String currentPhoneNumber = delegate.getPhoneNumber();

		changePhoneNumber(availablePhoneNumber, changeOtherNumbers, dealerCode, salesRepCode, reasonCode);
		if (needToCallSRPDS(header)) {
			((TMServiceRequestManager) provider.getServiceRequestManager()).reportChangePhoneNumber(delegate.getBanId(), currentSubId, delegate.getSubscriberId(), delegate.getDealerCode(),
					delegate.getSalesRepId(), provider.getUser(), currentPhoneNumber, delegate.getPhoneNumber(), header);
		}
	}

	@Override
	public void move(Account account, boolean transferOwnership,String reasonCode, String memoText, String dealerCode,String salesRepCode, ActivationOption selectedOption,ServiceRequestHeader header) throws TelusAPIException {

		move(account, transferOwnership, reasonCode, memoText, dealerCode, salesRepCode, selectedOption);

		if ( needToCallSRPDS(header)) {
			((TMServiceRequestManager)provider.getServiceRequestManager())
			.reportMoveSubscriber(delegate.getBanId(), account.getBanId(), delegate.getSubscriberId(),
					delegate.getDealerCode(), delegate.getSalesRepId(), provider.getUser(), 
					delegate.getPhoneNumber(), delegate.getStatus(), delegate.getStartServiceDate(), reasonCode, header);
		}
	}

	@Override
	public void restore(Date activityDate, String reason, String memoText, ServiceRequestHeader header) throws TelusAPIException {
		char oldStatus = delegate.getStatus();
		restore(activityDate, reason, memoText);
		if (needToCallSRPDS(header)) {
			reportChangeSubscriberStatus(delegate, oldStatus, Subscriber.STATUS_ACTIVE, reason, activityDate, header);
		}
	}

	protected String getSubscriberNetworkType() throws TelusAPIException {
		return getEquipment0().getNetworkType();
	}

	protected ApplicationMessage[] changeHSPAEquipment( Equipment newEquipment, Equipment associatedHandset,String dealerCode, String salesRepCode, String requestorId, String repairId, 
			String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo )throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException {
		return changeHSPAEquipment(newEquipment, associatedHandset, dealerCode, salesRepCode, requestorId, repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo, true);
	}

	/**
	 * 
	 * @param newEquipment
	 * @param associatedHandset
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param preserveDigitalServices
	 * @param allowDuplicateSerialNo
	 * @param invokeAPNFixForHSPA  - This is added for the HSPA production fix only (Nov 2009)
	 * @return
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 * @throws UnsupportedEquipmentException
	 */
	protected ApplicationMessage[] changeHSPAEquipment( Equipment newEquipment, Equipment associatedHandset,String dealerCode, String salesRepCode, String requestorId, String repairId, 
			String swapType, boolean preserveDigitiaServices, char allowDuplicateSerialNo, boolean invokeAPNFixForHSPA ) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException, UnsupportedEquipmentException {

		String activity=null;
		String methodName="changeHSPAEquipment";
		ApplicationMessage[] warningMessages = null;

		if ( newEquipment.isUSIMCard()==false ) {
			throw new UnsupportedEquipmentException("new equipment is not a USIMCard, sn("+ newEquipment.getSerialNumber()+")");
		}

		String equipmentContextInfo = "Old SN(" + getSerialNumber() +")"  + " new USIM("+newEquipment.getSerialNumber()+") Handset(" +  ((associatedHandset==null)? "null" : associatedHandset.getSerialNumber()) +")";
		try {
			TMEquipment oldEquipmentTM = (TMEquipment)getEquipment();
			TMEquipment newEquipmentTM = (TMEquipment)newEquipment;
			TMEquipment associatedHandsetTM = (TMEquipment)associatedHandset;

		//  [ Covent LTE Fix - Naresh Annabathula ]  update the new equipment with associated handset info  , since we require the handset info to validate the Volte logic in subscriber-ejb to add "SVOLTE" soc.
			if(newEquipmentTM.getDelegate()!=null && associatedHandsetTM !=null && associatedHandsetTM.getDelegate()!=null ){
				newEquipmentTM.getDelegate().setAssociatedHandset(associatedHandsetTM.getDelegate());
			}

			boolean handSetOnly = oldEquipmentTM.getSerialNumber().equals(newEquipmentTM.getSerialNumber());

				
			if (handSetOnly == false) { // everything in thist block only apply when there is USIMCard change
				activity = "testChangeEquipment0";
				warningMessages = testChangeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, allowDuplicateSerialNo);
				logSuccess(methodName, activity, equipmentContextInfo);

				activity = "KB equipment change";
				PricePlanValidationInfo ppValidationInfo = new PricePlanValidationInfo();
				ppValidationInfo.setEquipmentServiceMatch(false); //should skip equipment validation for HSPA switch
				provider.getSubscriberLifecycleFacade().changeEquipment(delegate, oldEquipmentTM.getDelegate(), newEquipmentTM.getDelegate(), null, dealerCode, salesRepCode, requestorId, swapType, null, ppValidationInfo, 
						null, true, null,
						SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
				logSuccess(methodName, activity, equipmentContextInfo);

				this.equipment = newEquipment;
			} else {
				/**
				 * [Emerson Cho] Add VoLTE for Handset-only swap if eligible 
				 */
				//[Chung] we could have skipped this call if associatedHandset is null, but we would allow this to happen so tha the EJB side can log why the volte soc was(not) added
				ServiceInfo volte = provider.getSubscriberLifecycleFacade().getVolteSocIfEligible(this.getDelegate(), getContract0().getDelegate(), associatedHandsetTM != null ? associatedHandsetTM.getDelegate() : null, false);
				if (volte != null) {
					getContract0().addService(volte);
					String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
					getContract0().save(KBDealer[0], KBDealer[1]);
				}
			}
			if (handSetOnly == false ) {
				// adjust contract base on new equipment
				try {
					// make sure the subscriber on the contract is fresh and new equipment is on the subscriber already at this point
					if (contract == null) {
						contract = (TMContract) getContract();
					}
					contract.setSubscriber(this);
					// Different Equipment Types: Add and/or remove services to/from  the contract as necessary
					addRemoveServices(contract, oldEquipmentTM, newEquipmentTM, true);

					activity = "save contract";
					String[] KBDealer = getKBDealer(dealerCode, salesRepCode);
					contract.save(KBDealer[0], KBDealer[1]);
				} catch (Throwable t) {
					logFailure(methodName, activity, t, "contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed " + equipmentContextInfo);
				}
			}

			//Update SEMS
			try {
				String newAssociatedHandsetIMEI = (associatedHandset!=null)?associatedHandset.getSerialNumber():null;
				activity = "call Sems.swapEquipmentForPhoneNumber";
				provider.getProductEquipmentLifecycleFacade().swapEquipmentForPhoneNumber(getPhoneNumber(), oldEquipmentTM.getSerialNumber(), oldEquipmentTM.getDelegate().getAssociatedHandsetIMEI(),
						oldEquipmentTM.getNetworkType(),newEquipment.getSerialNumber(), newAssociatedHandsetIMEI,newEquipmentTM.getNetworkType());
				logSuccess(methodName, activity, equipmentContextInfo);
			} catch( Throwable t ) {
				logFailure(methodName, activity, t, equipmentContextInfo);
			}


			try {
				activity = "log to old SRPDS";
				provider.getInteractionManager0().subscriberChangeEquipment(this,oldEquipmentTM, newEquipmentTM, dealerCode, salesRepCode,requestorId, repairId, swapType, null);
				logSuccess(methodName, activity, equipmentContextInfo);
			} catch( Throwable t ) {
				logFailure(methodName, activity, t, equipmentContextInfo);
			}

			//	 fix  for PROD00141498
			if (newEquipment.isStolen()) {
				newEquipment.reportFound();
			}

			//RIM and MB APN production fix (Nov 2009)
			if (oldEquipmentTM.isCDMA() && newEquipmentTM.isHSPA()) {
				if (invokeAPNFixForHSPA) {
					log("[" +getPhoneNumber()+ "] changeHSPAEquimpent: switching from CDMA to HSPA. Refreshing SOC and Features.");
					getContract0().refreshSocAndFeatures();
				}else {
					log("[" +getPhoneNumber()+ "] changeHSPAEquimpent: switching from CDMA to HSPA but invokeAPNFixForHSPA=false");
				}
			}
		} catch(Throwable t) {
			logFailure(methodName, activity, t, "Throwable occurred " + equipmentContextInfo );
			provider.getExceptionHandler().handleException(t);
		}		

		return warningMessages;
	}

	/**
	 * Change ESIM enabled equipment
	 * @param newEsimDevice
	 * @param dealerCode
	 * @param salesRepCode
	 * @param requestorId
	 * @param repairId
	 * @param swapType
	 * @param preserveDigitalServices
	 * @param allowDuplicateSerialNo
	 * @return an empty array of ApplicationMessage
	 * @throws TelusAPIException
	 * @throws SerialNumberInUseException
	 * @throws InvalidEquipmentChangeException
	 */
	protected ApplicationMessage[] changeEsimEnabledEquipment(Equipment newEsimDevice, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			boolean preserveDigitalServices, char allowDuplicateSerialNo) throws TelusAPIException, SerialNumberInUseException, InvalidEquipmentChangeException {
		
		String methodName = "changeEsimEnabledEquipment";
		String activity = "KB EsimEnabled equipment change";

		if (!AppConfiguration.isEsimSupportEnabled()) {
			InvalidEquipmentChangeException iece = new InvalidEquipmentChangeException("Unsupported device for equipment change", InvalidEquipmentChangeException.UNSUPPORTED_EQUIPMENT);
			logFailure(methodName, "Check if it is a supported device for equipment change", iece, "Unsupported device for equipment change");
			throw iece;
		}
		
		try {
			TMUSIMCardEquipment currentUsim = null;
			EquipmentInfo currentUsimInfo = null;
			TMEquipment currentDevice = null;
			EquipmentInfo currentDeviceInfo = null;
			
			TMEquipment currentEquip = (TMEquipment)getEquipment();
			
			if (currentEquip instanceof TMUSIMCardEquipment) {
				currentUsim = (TMUSIMCardEquipment)currentEquip;
				currentUsimInfo = currentUsim.getDelegate();
				currentDevice = (TMEquipment)currentUsim.getLastAssociatedHandset();
			} else {
				String errMsg = "Current equipment is NOT an USIM card. Expected type: TMUSIMCardEquipment; actual type: " + currentEquip.getClass().getName();
				InvalidEquipmentChangeException iece = new InvalidEquipmentChangeException(errMsg, InvalidEquipmentChangeException.UNSUPPORTED_EQUIPMENT);
				logFailure(methodName, "Check if current equipment is an USIM card", iece, "Unsupported equipment for equipment change");
				throw iece;
			}
			
			if (currentDevice != null) {
				currentDeviceInfo = currentDevice.getDelegate();
			}
			
			EquipmentInfo newEsimDeviceInfo = ((TMEquipment)newEsimDevice).getDelegate();
			
			DeviceSwapValidateInfo deviceSwapValidateInfo = provider.getProductEquipmentLifecycleFacade().validateEsimDeviceSwap(currentUsimInfo, newEsimDeviceInfo);
			logSuccess(methodName, "validate ESIM device swap", delegate, "Validate ESIM device swap successfully");
			
            checkDeviceSwapValidationResult(deviceSwapValidateInfo);
            
			currentUsimInfo.setSimProfileCd(deviceSwapValidateInfo.getCurrentSimProfileCd());
			newEsimDeviceInfo.setSimProfileCd(deviceSwapValidateInfo.getNewDeviceSimProfileCd());
			
			EquipmentChangeRequestInfo equipmentChangeRequestInfo = createEquipmentChangeRequestInfo(currentUsimInfo, currentDeviceInfo, newEsimDeviceInfo, dealerCode, salesRepCode, requestorId,
					repairId, swapType, preserveDigitalServices, allowDuplicateSerialNo);
			
			AccountInfo accountInfo = getAccount0().getDelegate0();
			
			try {
				provider.getSubscriberLifecycleFacade().changeEsimEnabledDevice(delegate, accountInfo, equipmentChangeRequestInfo, SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			} catch (ApplicationException ae) {
				if (ErrorCodes.ESIM_ERROR_EQUIPMENT_SN_IN_USE.equalsIgnoreCase(ae.getErrorCode())) {
					throw new SerialNumberInUseException("The new device is currently associated with another non-cancelled subscriber", SerialNumberInUseException.EQUIPMENT_INUSE);
				}
				throw new TelusAPIException("Failed to reserve ESIM profile", ae);
			}
			
			logSuccess(methodName, activity, delegate, "KB ESIM enabled equipment change successful");
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		// The ApplicationMessage[] that got returned before should not be applicable anymore
		return new ApplicationMessage[0];
	}
	
	protected String getDefaultEquipmentType() {
		try {
			if (NetworkType.NETWORK_TYPE_HSPA.equals(getSubscriberNetworkType())) {
				return AppConfiguration.getDefaultHSPAEquipmentType();
			}else {
				return getEquipment().getEquipmentType();
			}
		}catch (TelusAPIException ape) {
			return Equipment.EQUIPMENT_TYPE_PDA;
		}
	}

	@Override
	public SubscriptionPreference getSubscriptionPreference(int preferenceTopicId) throws TelusAPIException {
		try {
			long subscriptionId = getSubscriptionId();
			SubscriptionPreferenceInfo info = provider.getSubscriberLifecycleHelper().retrieveSubscriptionPreference( subscriptionId,  preferenceTopicId);

			if ( info==null) {
				info = new SubscriptionPreferenceInfo();
				info.setPreferenceTopicId(preferenceTopicId);
				info.setSubscriptionId(subscriptionId);
			}

			
			switch (preferenceTopicId) {
				case SubscriptionPreference.PREFERENCE_TYPE_CODE_NOTIFICATION_TOGGLE:
				case SubscriptionPreference.PREFERENCE_TYPE_CODE_DOMESTIC_DATA_TOGGLE:
				case SubscriptionPreference.PREFERENCE_TYPE_CODE_ROAMING_DATA_TOGGLE:
					return new TMNotificationToggleSubscriptionPreference( provider , info );
				default:
					return new TMSubscriptionPreference( provider , info );
			}
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	private Date attachTimeComponent(Date logicalDate) {

		Calendar calendar = Calendar.getInstance();
		Calendar logicalCalndar = Calendar.getInstance();
		logicalCalndar.setTime(logicalDate);

		logicalCalndar.set(logicalCalndar.get(Calendar.YEAR),
				logicalCalndar.get(Calendar.MONTH),
				logicalCalndar.get(Calendar.DAY_OF_MONTH),
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE));

		return logicalCalndar.getTime();
	}

	@Override
	public Contract getContractForEquipmentChange(EquipmentChangeRequest equipmentChangeRequest)throws TelusAPIException {

		EquipmentChangeRequestInfo ecrInfo = (EquipmentChangeRequestInfo) equipmentChangeRequest;
		Equipment currEquipment = getEquipment();
		ecrInfo.setCurrentEquipment( currEquipment );
		if ( currEquipment.isSIMCard()) {
			ecrInfo.setCurrentAssociatedHandset( ((SIMCardEquipment ) currEquipment).getLastMule() );
		} else if ( currEquipment.isUSIMCard() ) {
			ecrInfo.setCurrentAssociatedHandset( ((USIMCardEquipment ) currEquipment).getLastAssociatedHandset() );
		}

		TMContract currentContract = getContract0( false, false);
		SubscriberContractInfo contractInfo = (SubscriberContractInfo) currentContract.getDelegate().clone();
		TMContract contractClone = new TMContract(provider, contractInfo, (TMPricePlan) currentContract.getPricePlan(),
				this, false, false, false,  Subscriber.TERM_PRESERVE_COMMITMENT, ecrInfo );

		return getContractForEquipmentChange(ecrInfo, contractClone);
	}

	private Contract getContractForEquipmentChange( EquipmentChangeRequestInfo ecr, TMContract contract )throws TelusAPIException {

		Equipment newDevice= ecr.deriveNewDevice();
		if ( newDevice==null) {
			newDevice = ecr.getNewEquipment();
		}

		Equipment currDevice = ecr.getCurrentAssociatedHandset() ;
		if ( currDevice==null) {
			currDevice = ecr.getCurrentEquipment();
		}

		addRemoveServices( contract, currDevice, newDevice, ecr.preserveDigitalServices() );
		removeNonMatchingServices( newDevice ,contract );
		removeDispatchOnlyConflicts( contract );
		contract.checkForVoicemailService();

		return contract;
	}

	private boolean checkAsyncPerformActivationCondition() throws TelusAPIException { 
		boolean needAsyncPerformAfterRefresh = false;
		if (activation) {
			// CDR - activation
			Date startServiceDate = delegate.getStartServiceDate();
			if (startServiceDate == null || startServiceDate.compareTo(provider.getReferenceDataManager().getSystemDate()) == 0) {
				needAsyncPerformAfterRefresh = true;
			} else {
				log("Bypass CDR asyncPerformPostSubscriberCommitTasks for activation, because startServiceDate[" + startServiceDate + "] not equle System Date"); 
			}
		}
		return needAsyncPerformAfterRefresh;
	}

	private static HashSet get911FeeProvinces() {
		return AppConfiguration.get911Provinces();
	}
	
	/**
	 * Originally we use the value from getPhoneNumber() and pass to SLCF.deactiveMVNESubscriber . However, this couples the code to the 
	 * object itself and which reduces flexibility. There's also unknown side-effect from changePhoneNumber method where we're setting
	 * old and new phone number in various place. Therefore a parameter is used so we may pass different value to this method.
	 * @param phoneNumber
	 * @throws DeactivateMVNESubscriberException
	 */
	private void deactivateMVNESubcriber(String phoneNumber) throws DeactivateMVNESubscriberException {
		try {
			provider.getSubscriberLifecycleFacade().deactivateMVNESubcriber(phoneNumber);
		} catch (ApplicationException appEx) {
			DeactivateMVNESubscriberException ex = new DeactivateMVNESubscriberException("Error calling WLNP service DeactivateMVNESubscriberService.  Please check SOA logs.",appEx); 
			if (appEx.getErrorCode() != null) {
				ex.setCode(appEx.getErrorCode());
			}
			if (appEx.getSubsystemStackTrace() != null) {
				ex.setSubSystemException(appEx.getSubsystemStackTrace());
			}
			throw ex;
		} catch (Throwable t) {
			Logger.debug(t);
			throw new DeactivateMVNESubscriberException(t.getMessage(),t);
		}
	}
	
	@Override
	public double getAirtimeRate() throws InvalidAirtimeRateException, 	InvalidSubscriberStatusException, TelusAPIException {
		
		if (getStatus() ==STATUS_CANCELED || getStatus() == STATUS_RESERVED) {
			throw new InvalidSubscriberStatusException("Cannot get airtime rate on cancelled/reserved subscriber");
		}
		
		try {
			return provider.getSubscriberLifecycleManager().getAirtimeRate(getBanId(), getSubscriberId(), 
					SessionUtil.getSessionId(provider.getSubscriberLifecycleManager()));
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e, new ProviderDefaultExceptionTranslator() {
				@Override
				public TelusAPIException translateException(Throwable throwable) {
					if ( throwable instanceof ApplicationException ) {
						if ((ErrorCodes.GET_SUBSCRIBER_AIRTIME_RATE_ERROR).equals(((ApplicationException)throwable).getErrorCode()) )
							return new InvalidAirtimeRateException(throwable, 0.0 );
					}
					return super.translateException(throwable);
				}

				
			});
		
			
		}
		return 0;	
		
	}
	
	protected Date getLogicalDate() throws TelusAPIException {
		return provider.getReferenceDataManager().getLogicalDate();
	}
	
	protected boolean isFutureDated (Date refDate) throws TelusAPIException {
		if (refDate != null) {
			return DateUtil.isAfter(refDate, getLogicalDate());
		}
		
		return false;
	}

	protected void reservePhoneNumberOffline(PhoneNumberReservation phoneNumberReservation) throws TelusAPIException 
	{
		if (phoneNumberReservation.getPhoneNumberPattern().length() < 10 || phoneNumberReservation.getPhoneNumberPattern().indexOf("*") >= 0
				|| phoneNumberReservation.getPhoneNumberPattern().indexOf("%") >= 0) {
			throw new TelusAPIException("PhoneNumber is not valid for  offline activation process,it should be ten digit phone number without any wildcard charcters "+ phoneNumberReservation.getPhoneNumberPattern());
		
		}
		
		try {
		SubscriberInfo info = provider.getSubscriberLifecycleManager().reservePhoneNumber(delegate, ((TMPhoneNumberReservation)phoneNumberReservation).getPhonenumberReservation0(), true, SessionUtil.getSessionId(provider.getSubscriberLifecycleManager()));
		delegate.copyFrom(info);
		info.setMarketProvince(phoneNumberReservation.getNumberGroup().getProvinceCode());
		info.setNumberGroup(phoneNumberReservation.getNumberGroup());
		provider.registerNewSubscriber(delegate);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}

	}

	// Business Connect changes start
	
	@Override
	public void setSeatData(SeatData seatData) {
		delegate.setSeatData(seatData);
	}

	
	@Override
	public SeatData getSeatData() {
		return delegate.getSeatData();
		
	}
	
	public boolean isStarterSeat() throws TelusAPIException {
		if (getSeatData()!=null && getSeatData().getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
			return true;
		}
		return false;
	}

	private void validateBusinessConnectSeatsBeforeRestore(ProductSubscriberList[] productSubscriberList,String[] subscriberIds) throws TelusAPIException {
		for (int i = 0; i < productSubscriberList.length; i++) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberList[i].getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriberList[i].getSuspendedSubscriberIdentifiers();
			
			for (int j = 0; j < suspendedsubscriberIdentifiers.length; j++) {
				for (int k = 0; k < subscriberIds.length; k++) {
					if (subscriberIds[k].equals(suspendedsubscriberIdentifiers[j].getSubscriberId()) && SeatType.SEAT_TYPE_STARTER.equals(suspendedsubscriberIdentifiers[j].getSeatType()) == false) {
						if (isStarterSeatActiveOnGroup(activeSubscriberIdentifiers,suspendedsubscriberIdentifiers[j].getSeatGroup()) == false)
							throw new TelusAPIException("StartSeat not active on Group : "+ suspendedsubscriberIdentifiers[j].getSeatGroup()+ ",we can't restore the non starter seat : "+ subscriberIds[k]+"  without starter seat active.");
					}
				}

			}
		}

	}
	
	private boolean isStarterSeatActiveOnGroup(	SubscriberIdentifier[] activeSubscriberIdentifiers, String seatGroup) {
		for (int j = 0; j < activeSubscriberIdentifiers.length; j++) {
			if (activeSubscriberIdentifiers[j].getSeatGroup().equalsIgnoreCase(seatGroup) && SeatType.SEAT_TYPE_STARTER.equals(activeSubscriberIdentifiers[j].getSeatType()) == true)
				return true;
		}
		return false;
	}
	
	private char getBusinessConnectGroupStausAfterCancel(ProductSubscriberList[] productSubscriberList) {
		
		determineisLastActiveOrLastSupendStarterSeat(productSubscriberList);
		
		char status = ACCOUNT_STATUS_NO_CHANGE;
		
		if (lastActiveStarterSeatInd == false) {
			status = AccountSummary.STATUS_SEAT_GROUP_CANCELED;
			Logger.debug("canceling the  starter seat ,hence group will be cancelled.Account still will be in Open status for " +getBanId());
		} else if (lastSuspendedStarterSeatInd) {
			status = AccountSummary.STATUS_CANCELED;
			Logger.debug("Account status after Cancel the seat is :   " + status + " for " +getBanId());
		} else {
			status = AccountSummary.STATUS_SUSPENDED;
			Logger.debug("Account status after Cancel the seat is :    " + status+ " for " +getBanId());
		}
		return status;
	}
		
	private char getBusinessConnectGroupStausAfterSuspend(ProductSubscriberList[] productSubscriberList) {
		
		determineisLastActiveOrLastSupendStarterSeat(productSubscriberList);
		
		char status = ACCOUNT_STATUS_NO_CHANGE;
		if (lastActiveStarterSeatInd == false) {
			status = AccountSummary.STATUS_SEAT_GROUP_SUSPENDED;
			Logger.debug("suspending the  starter seat ,hence group will be suspended.Account still will be in open status for " +getBanId());
		} else {
			status = AccountSummary.STATUS_SUSPENDED;
			 Logger.debug("Account status after suspend  the seat is :    " + status+ " for " +getBanId());
		}
		
		return status;
	}
	
	private void validateBusinessConnectSeatBeforeRestoreFromCancelState(ProductSubscriberList[] productSubscriberList) throws TelusAPIException {

		if ((getSeatData() != null && getSeatData().getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE) == false)) {
			throw new TelusAPIException("SubscriberId  "+ getSubscriberId()+ "is in cancelled state, can't restore seat from " +"canceled state for Business connect non-mobile seats .");
		}
		else if ((getAccount().getStatus() == AccountSummary.STATUS_OPEN) == false) {
			throw new TelusAPIException("SubscriberId  "+ getSubscriberId()+ "is in cancelled state,  Account should be in Active status" +" to restore the mobile seat");
		}
		else {
			for (int i = 0; i < productSubscriberList.length; i++) {
				boolean isStarterSeatActive = isStarterSeatActiveOnGroup(productSubscriberList[0].getActiveSubscriberIdentifiers(), getSeatData().getSeatGroup());
				if (isStarterSeatActive == false) {
					throw new TelusAPIException("SubscriberId  "+ getSubscriberId()+ "is in cancelled state,  Group( starter seat ) : " +getSeatData().getSeatGroup() +" should be in active status" +
							" to restore the mobile seat under the group ");
				}
			}
		}
	}
	
	private char getBusinessConnectAccountStausAfterCancelStarterSeat(ProductSubscriberList[] productSubscriberList) throws TelusAPIException {	
		
		determineisLastActiveOrLastSupendStarterSeat(productSubscriberList);
		if (lastActiveStarterSeatInd == false) {
			// if there is one active starter subscriber, then the account must be (and remain) in opened status
			return ACCOUNT_STATUS_NO_CHANGE;
		}
		else if (lastSuspendedStarterSeatInd == false ) {
			if (getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED) {
				return ACCOUNT_STATUS_NO_CHANGE;
			}
			else {
				return AccountSummary.STATUS_SUSPENDED;
			}
		}
		else {
			// both active starer seat subscriber count and starter suspended subscriber count will be 0.
			return AccountSummary.STATUS_CANCELED;
		}
	}
	
	private char getBusinessConnectAccountStausAfterSuspendStarterSeat(ProductSubscriberList[] productSubscriberList) throws TelusAPIException {
	 
		determineisLastActiveOrLastSupendStarterSeat(productSubscriberList);
		if (lastActiveStarterSeatInd == false) {
			// if there is one active starter seat, then the account must be (and remain) in opened status
			return ACCOUNT_STATUS_NO_CHANGE;
		}
		else {
			return AccountSummary.STATUS_SUSPENDED;
		}
	}
	
	private void determineisLastActiveOrLastSupendStarterSeat(ProductSubscriberList[] productSubscriberList)  {

		for (int i = 0; i < productSubscriberList.length; i++) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberList[i].getActiveSubscriberIdentifiers();
			for (int j = 0; j < activeSubscriberIdentifiers.length; j++) {
				if (activeSubscriberIdentifiers[j].getSeatType().equals(SeatType.SEAT_TYPE_STARTER)) {
					activeStarterseatCount++;
				}
			}

			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriberList[i].getSuspendedSubscriberIdentifiers();
			for (int k = 0; k < suspendedsubscriberIdentifiers.length; k++) {
				if (suspendedsubscriberIdentifiers[k].getSeatType().equals(SeatType.SEAT_TYPE_STARTER)) {
					suspendedStarterSeatCount++;
				}

			}
		}

		if (this.getStatus() == Subscriber.STATUS_ACTIVE) {
			activeStarterseatCount--;
		} else if (this.getStatus() == Subscriber.STATUS_SUSPENDED) {
			suspendedStarterSeatCount--;
		}

		if (activeStarterseatCount == 0) {
			this.lastActiveStarterSeatInd = true;
		}
		if (suspendedStarterSeatCount == 0)
			this.lastSuspendedStarterSeatInd = true;

	}	

	// Business Connect changes End
	
	// Comm Suite changes Begin
	private CommunicationSuiteInfo getCommunicationSuite(int companionLevel) throws TelusAPIException {
		if (getAccount0().isEligibleForCommunicationSuite()) {
			try {
				this.commSuiteInfo = provider.getSubscriberLifecycleHelper().retrieveCommunicationSuite(getBanId(), getSubscriberId(), companionLevel);
				this.commSuiteCached = true;
			} catch (Throwable e) {
				throw new TelusAPIException(e);
			}
		}
		return commSuiteInfo;
	}

	protected CommunicationSuiteInfo getCommunicationSuite(boolean forceRefresh) throws TelusAPIException {
		if (commSuiteCached == false || forceRefresh) {
			commSuiteInfo = getCommunicationSuite(CommunicationSuiteInfo.CHECK_LEVEL_ALL);
		}
		return commSuiteInfo;
	}
	// Comm Suite changes End	
	
	private void commSuitePhoneNumberChangePreTask() throws InvalidSubscriberStatusException ,TelusAPIException{
		if(!AppConfiguration.isPerformCommSuiteTNCPostTaskEnabled()){
			return;
		}
		commSuiteInfo = getCommunicationSuite(true);
		if (commSuiteInfo != null) {
			if(commSuiteInfo.isSuspendedPrimary() || commSuiteInfo.getSuspendedCompanionCount() > 0 ){
				throw new InvalidSubscriberStatusException(InvalidSubscriberStatusException.INVALID_COMM_SUITE);
			}
			iscommSuiteCTNChange = true;
		}
	}
	
	
	private void commSuitePhoneNumberChangePostTask(int ban, String newSubscriberId, String oldSubscriberId,String sessionId) throws TelusAPIException{
		if(iscommSuiteCTNChange){
			String methodName = "commSuitePhoneNumberChangePostTask";
			String activityName = "repairCommunicationSuiteDueToPhoneNumberChange";
			try {
				provider.getSubscriberLifecycleFacade().asyncRepairCommunicationSuiteDueToPhoneNumberChange(ban, newSubscriberId, oldSubscriberId, sessionId);
				logSuccess(methodName, activityName, "repairCommunicationSuiteDueToPhoneNumberChange call success for ban [ " + ban +"] , newSubscriberId ["+newSubscriberId+" ], oldSubscriberId ["+oldSubscriberId+" ]");

			} catch (Throwable t) {
				String extraMessage =  "repairCommunicationSuiteDueToPhoneNumberChange call failed for ban [ " + ban +"] , newSubscriberId ["+newSubscriberId+" ], oldSubscriberId ["+oldSubscriberId+" ]";
				logFailure(methodName, activityName, t, extraMessage);
			}
		}
		
	}
	
	private void checkDeviceSwapValidationResult(DeviceSwapValidateInfo deviceSwapValidateInfo) throws InvalidEquipmentChangeException {
		if (EsimConstants.UNSUPPORTED_ESIM_DEVICE.equalsIgnoreCase(deviceSwapValidateInfo.getResultCd())) {
        	throw new InvalidEquipmentChangeException("The device is unsupported.", InvalidEquipmentChangeException.UNSUPPORTED_EQUIPMENT);
        } else if(EsimConstants.SIM_PROFILE_MISMATCH.equalsIgnoreCase(deviceSwapValidateInfo.getResultCd())) {
        	throw new InvalidEquipmentChangeException("The sim profile does not match.", InvalidEquipmentChangeException.SIMPROFILE_MISMATCH);
        } else if(EsimConstants.DEVICE_LOST_STOLEN.equalsIgnoreCase(deviceSwapValidateInfo.getResultCd())) {
        	throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
        }
	}

	private EquipmentChangeRequestInfo createEquipmentChangeRequestInfo(EquipmentInfo currentUsimInfo, EquipmentInfo currentDeviceInfo, EquipmentInfo newEsimDeviceInfo, String dealerCode, String salesRepCode,
			String requestorId, String repairId, String swapType, boolean preserveDigitalServices, char allowDuplicateSerialNo) {
		
		EquipmentChangeRequestInfo equipmentChangeRequestInfo = new EquipmentChangeRequestInfo();
		equipmentChangeRequestInfo.setCurrentEquipment(currentUsimInfo);
		equipmentChangeRequestInfo.setCurrentAssociatedHandset(currentDeviceInfo);
		equipmentChangeRequestInfo.setNewAssoicatedHandset(newEsimDeviceInfo);
		equipmentChangeRequestInfo.setNewAssociatedHandsetSerialNumber(newEsimDeviceInfo.getSerialNumber());
		equipmentChangeRequestInfo.setDealerCode(dealerCode);
		equipmentChangeRequestInfo.setSalesRepCode(salesRepCode);
		equipmentChangeRequestInfo.setRequestorId(requestorId);
		equipmentChangeRequestInfo.setRepairId(repairId);
		equipmentChangeRequestInfo.setSwapType(swapType);
		equipmentChangeRequestInfo.setPreserveDigitalServices(preserveDigitalServices);
		equipmentChangeRequestInfo.setAllowDuplicateSerialNumber(allowDuplicateSerialNo);
		return equipmentChangeRequestInfo;
	}
	
	// Added for CDA phase 1B July 2018
	private TMActivationOption activationOption;
	
	public TMActivationOption getActivationOption() throws TelusAPIException {
		return activationOption;
	}
	
	public void setActivationOption(ActivationOption selectedOption, boolean isTown) throws TelusAPIException {
		this.activationOption = new TMActivationOption(provider, selectedOption, getAccount(), this, isTown);
	}
	
	private void clearCachedCommSuite() {
		commSuiteInfo = null;
		commSuiteCached = false;
	}
}
