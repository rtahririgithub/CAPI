package com.telus.cmb.subscriber.lifecyclehelper.svc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.Subscriber;
import com.telus.api.portability.PortOutEligibility;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AccountDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AddressDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.AdjustmentDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.CommunicationSuiteDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.DepositDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.FleetDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.InvoiceDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.MemoDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.PrepaidDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ProvisioningDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.ServiceAgreementDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberEquipmentDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriberInformationCodsDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriptionServiceDao;
import com.telus.cmb.subscriber.lifecyclehelper.dao.UsageDao;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelperTestPoint;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.HSPASubscriberInfo;
import com.telus.eas.subscriber.info.HandsetChangeHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PairingGroupInfo;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name="SubscriberLifecycleHelper", mappedName="SubscriberLifecycleHelper")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Remote({SubscriberLifecycleHelper.class, SubscriberLifecycleHelperTestPoint.class})
@RemoteHome(SubscriberLifecycleHelperHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class SubscriberLifecycleHelperImpl implements SubscriberLifecycleHelper, SubscriberLifecycleHelperTestPoint {

	private static final Logger LOGGER = Logger.getLogger(SubscriberLifecycleHelperImpl.class);
	private ReferenceDataHelper referenceDataHelper = null;
	
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private DepositDao depositDao;
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private MemoDao memoDao;
	@Autowired
	private SubscriberInformationCodsDao subscriberInformationCodsDao;
	@Autowired
	private PrepaidDao prepaidDao;
	@Autowired
	private SubscriptionServiceDao subscriptionServiceDao;
	@Autowired
	private ProvisioningDao provisioningDao; 
	@Autowired
	private ServiceAgreementDao serviceAgreementDao; 
	@Autowired
	private SubscriberDao subscriberDao;
	@Autowired
	private SubscriberEquipmentDao subscriberEquipmentDao;
	@Autowired 
	private UsageDao usageDao;
	@Autowired
	private AdjustmentDao adjustmentDao;
	@Autowired
	private DataSourceTestPointDao testPointDao;
	@Autowired
	private CommunicationSuiteDao communicationSuiteDao;

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}
	public void setDepositDao(DepositDao depositDao) {
		this.depositDao = depositDao;
	}
	public void setFleetDao(FleetDao fleetDao) {
		this.fleetDao = fleetDao;
	}
	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}
	public void setMemoDao(MemoDao memoDao) {
		this.memoDao = memoDao;
	}
	public void setPrepaidDao(PrepaidDao prepaidDao) {
		this.prepaidDao = prepaidDao;
	}
	public void setProvisioningDao(ProvisioningDao provisioningDao) {
		this.provisioningDao = provisioningDao;
	}

	public void setServiceAgreementDao(ServiceAgreementDao serviceAgreementDao) {
		this.serviceAgreementDao = serviceAgreementDao;
	}
	public void setSubscriberEquipmentDao(SubscriberEquipmentDao subscriberEquipmentDao) {
		this.subscriberEquipmentDao = subscriberEquipmentDao;
	}

	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}
	
	public void setUsageDao(UsageDao usageDao) {
		this.usageDao=usageDao;
	}

	@Override
	public PhoneDirectoryEntry[] getPhoneDirectory(long subcriptionID) throws ApplicationException {
		return subscriberInformationCodsDao.getPhoneDirectory(subcriptionID);

	}

	@Override
	public void updatePhoneDirectory(long subscriptionID, PhoneDirectoryEntry[] entries) throws ApplicationException {
		subscriberInformationCodsDao.updatePhoneDirectory(subscriptionID, entries);
	}

	@Override
	public void deletePhoneDirectoryEntries(long subscriptionID, PhoneDirectoryEntry[] entries) throws ApplicationException {
		subscriberInformationCodsDao.deletePhoneDirectoryEntries(subscriptionID, entries);
	}

	/**
	 * @param subscriberInfoCodsDao the subscriberInfoCodsDao to set
	 *//*
	public void setSubscriberInformationCodsDao(SubscriberInformationCodsDao subscriberInformationCodsDao) {
		this.subscriberInformationCodsDao = subscriberInformationCodsDao;
	}


	/**
	  * Retrieves subscriber from KB data source by ban and subscriber ID.
	  * It's unclear on which parameter is required or optional. As of Apr 2013, there're few callers that invoke this method with ban=0.
	  * Therefore, a retrieveSubscriber(String subscriberId) method is created as part of KB capacity (July 2013) project to distinguish the call.
	  * The subscriberId should never be empty or null, as if only the BAN is passed there can be multiple active subscribers fall under the BAN.
	  * Logging is added as part of July 2013 to see if such case would occur for troubleshooting purpose.
	  * 
	  * @param ban - Billing Account Number
	  * @param subscriberId - Subscriber Identifier
	  * @return SubscriberInfo object
	  * @throws ApplicationException
	  */
	@Override
	public SubscriberInfo retrieveSubscriber(int ban, String subscriberId) throws ApplicationException {
		if (subscriberId == null || subscriberId.trim().isEmpty()) {
			LOGGER.error("Unexpected error. ban=["+ban+"], subscriberId=["+subscriberId+"]");
		}
		
		SubscriberInfo subInfo = null;
		Collection<SubscriberInfo> subList = new ArrayList<SubscriberInfo>();
		
		if (AppConfiguration.isWRPPh3Rollback()) {
			if (ban == 0) {
				return retrieveSubscriber (subscriberId);
			}else {
				subList = subscriberDao.retrieveSubscriberListByBANAndSubscriberId(ban, subscriberId, true, 1);
			}
		} else{
			subList = subscriberDao.retrieveSubscriberListByBanAndSubscriberId(ban, subscriberId, true, 1);
		}
		
		if(CollectionUtils.isNotEmpty(subList)) {
			replaceDummyESN(subList);
			subInfo = ((ArrayList<SubscriberInfo>)subList).get(subList.size()-1);
			return subInfo;
		}
		
		String errorMessage ;
		
		if (ban > 0) {
			errorMessage = "subscriber not found for ban = [" + ban+ "] , subscriberId =  [" + subscriberId + "]";
		} else{
			 errorMessage = "subscriber not found for subscriberId = ["+ subscriberId + "]";
		}

		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, errorMessage);

	}

	/**
	 * Retrieves subscriber from KB data source by subscriber ID. This method return the subscriber that's "the latest" and may not be suitable for all scenarios.
	 * @param subscriberId - Subscriber Identifier
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	@Override
	public SubscriberInfo retrieveSubscriber(String subscriberId) throws ApplicationException {
		// Validate the subscriberId
		if (StringUtils.isBlank(subscriberId)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber ID cannot be empty Or null","");
		}
		Collection<SubscriberInfo> subList = null;
		//Retrieve the subscriberList by subscriberId
		subList = subscriberDao.retrieveSubscriberListBySubscriberId(subscriberId, true, 1);
		 //Replace the kb dummy esn with  actual serial number.
		if (CollectionUtils.isNotEmpty(subList)) {
			replaceDummyESN(subList);
			SubscriberInfo subInfo = ((ArrayList<SubscriberInfo>)subList).get(subList.size()-1);
			return subInfo;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found: Subscriber ID [" + subscriberId + "]","");
	}
	
	/**
	 * Retrieves subscriber from KB data source by ban and subscriber phone #
	 * @deprecated use {@link #retrieveSubscriberByBanAndPhoneNumber(int, String)}
	 * @param ban - Billing Account Number
	 * @param phoneNumber - Subscriber phone number
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	@Override
	public SubscriberInfo retrieveSubscriberByPhoneNumber(@BANValue int ban, String phoneNumber) throws ApplicationException {
		return retrieveSubscriberByBanAndPhoneNumber(ban, phoneNumber);
	}	
	
	/**
	 * Retrieves subscriber from KB data source by ban and subscriber phone #
	 * @param ban - Billing Account Number
	 * @param phoneNumber - Subscriber phone number
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	@Override
	public SubscriberInfo retrieveSubscriberByBanAndPhoneNumber(@BANValue int ban, String phoneNumber) throws ApplicationException {
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			throw new ApplicationException (SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_PHONE_NUMBER, "Invalid phone number ["+phoneNumber+"]", "");
		}
		
		SubscriberInfo subscriberInfo = subscriberDao.retrieveSubscriberByBANAndPhoneNumber(ban, phoneNumber);
		
		if (subscriberInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_SLH_DAO, ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found: BAN [" + ban + "], phone number [" + phoneNumber + "]", "");
		} else {
			replaceDummyESN(subscriberInfo);
			return subscriberInfo;
		}		
	}
	
	@Override
	public ServiceAgreementInfo[] retrieveFeaturesForPrepaidSubscriber(
			String phoneNumber) throws ApplicationException {
			return subscriptionServiceDao.retrieveFeatures(phoneNumber);
	}

	@Override
	public List<ProvisioningTransactionInfo> retrieveProvisioningTransactions(@BANValue
			int customerID, String subscriberID, Date from, Date toDate)
			throws ApplicationException {
		return provisioningDao.retrieveProvisioningTransactions(customerID, subscriberID, from, toDate);
	}
		@Override
		public List<PrepaidCallHistoryInfo> retrievePrepaidCallHistory(String phoneNumber, Date from, Date to) throws ApplicationException {
			return prepaidDao.retrievePrepaidCallHistory(phoneNumber, from, to);
		}
		@Override
		public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
				String phoneNumber, Date from, Date to) throws ApplicationException {
			return prepaidDao.retrievePrepaidEventHistory(phoneNumber, from, to);
		}
		@Override
		public List<PrepaidEventHistoryInfo> retrievePrepaidEventHistory(
				String phoneNumber, Date from, Date to,
				PrepaidEventTypeInfo[] prepaidEventTypes) throws ApplicationException {
			return prepaidDao.retrievePrepaidEventHistory(phoneNumber, from, to,prepaidEventTypes);
		}
	@Override
	public CallingCirclePhoneListInfo[] retrieveCallingCirclePhoneNumberListHistory(
			@BANValue int banId, String subscriberNo, String productType, Date from,
			Date to) throws ApplicationException {

		List<CallingCirclePhoneListInfo> list=serviceAgreementDao.retrieveCallingCirclePhoneNumberListHistory(banId, subscriberNo, productType, from, to);
		return list==null? new CallingCirclePhoneListInfo[0]:list.toArray(new CallingCirclePhoneListInfo[list.size()]);
	}
	@Override
	public ContractChangeHistoryInfo[] retrieveContractChangeHistory(@BANValue int ban,
			String subscriberID, Date from, Date to)
	throws ApplicationException {
		List<ContractChangeHistoryInfo> list=serviceAgreementDao.retrieveContractChangeHistory(ban, subscriberID, from, to);
		return  list==null? new ContractChangeHistoryInfo[0] :list.toArray(new ContractChangeHistoryInfo[list.size()]);
	}
	@Override
	public FeatureParameterHistoryInfo[] retrieveFeatureParameterHistory(
			@BANValue int banId, String subscriberNo, String productType,
			String[] parameterNames, Date from, Date to)
	throws ApplicationException {
		List<FeatureParameterHistoryInfo> list=serviceAgreementDao.retrieveFeatureParameterHistory(banId, subscriberNo, productType, parameterNames, from, to);
		return list==null? new FeatureParameterHistoryInfo[0] :list.toArray(new FeatureParameterHistoryInfo[list.size()]);
	}
	@Override
	public String[] retrieveMultiRingPhoneNumbers(String subscriberId)
	throws ApplicationException {
		List<String> list=serviceAgreementDao.retrieveMultiRingPhoneNumbers(subscriberId);
		return list==null? new String[0] :list.toArray(new String[list.size()]);
	}
	@Override
	public PricePlanChangeHistoryInfo[] retrievePricePlanChangeHistory(@BANValue int ban,
			String subscriberID, Date from, Date to)
	throws ApplicationException {
		List<PricePlanChangeHistoryInfo> list=serviceAgreementDao.retrievePricePlanChangeHistory(ban, subscriberID, from, to);
		return list==null? new PricePlanChangeHistoryInfo[0] :list.toArray(new PricePlanChangeHistoryInfo[list.size()]); 
	}
	@Override
	public SubscriberContractInfo retrieveServiceAgreementByPhoneNumber(
			String phoneNumber) throws ApplicationException {
		String subscriberID = retrieveSubscriberIDByPhoneNumber(0, phoneNumber);
		return serviceAgreementDao.retrieveServiceAgreementBySubscriberID(subscriberID);
	}
	@Override
	public SubscriberContractInfo retrieveServiceAgreementBySubscriberId(
			String subscriberId) throws ApplicationException {
		return serviceAgreementDao.retrieveServiceAgreementBySubscriberID(subscriberId);
	}
	@Override
	public ServiceChangeHistoryInfo[] retrieveServiceChangeHistory(@BANValue int ban,
			String subscriberID, Date from, Date to)
	throws ApplicationException {
		return retrieveServiceChangeHistory(ban, subscriberID, from, to, false);
	}
	@Override
	public ServiceChangeHistoryInfo[] retrieveServiceChangeHistory(@BANValue int ban,
			String subscriberID, Date from, Date to, boolean includeAllServices)
	throws ApplicationException {
		List<ServiceChangeHistoryInfo> list=serviceAgreementDao.retrieveServiceChangeHistory(ban, subscriberID, from, to, includeAllServices);
		return list==null? new ServiceChangeHistoryInfo[0] :list.toArray(new ServiceChangeHistoryInfo[list.size()]); 
	}
	@Override
	public VendorServiceChangeHistoryInfo[] retrieveVendorServiceChangeHistory(
			@BANValue int ban, String subscriberId, String[] categoryCodes)
	throws ApplicationException {
		List<VendorServiceChangeHistoryInfo> list=serviceAgreementDao.retrieveVendorServiceChangeHistory(ban, subscriberId, categoryCodes);
		return list==null? new VendorServiceChangeHistoryInfo[0] :list.toArray(new VendorServiceChangeHistoryInfo[list.size()]); 
	}
	@Override
	public String[] retrieveVoiceMailFeatureByPhoneNumber(String phoneNumber,
			String productType) throws ApplicationException {
		List<String> list=serviceAgreementDao.retrieveVoiceMailFeatureByPhoneNumber(phoneNumber, productType);
		return list==null? new String[0] :list.toArray(new String[list.size()]); 
	}
	@Override
	public String retrieveSubscriberProvisioningStatus(@BANValue int ban,
			String phoneNumber) throws ApplicationException {
		return provisioningDao.retrieveSubscriberProvisioningStatus(ban, phoneNumber);
	}
	@Override
	public SubscriberInfo retrieveBanForPartiallyReservedSub(String phoneNumber)
	throws ApplicationException {
		return accountDao.retrieveBanForPartiallyReservedSub(phoneNumber);
	}
	@Override
	public int retrieveBanIdByPhoneNumber(String phoneNumber)
	throws ApplicationException {
		return accountDao.retrieveBanIdByPhoneNumber(phoneNumber);
	}
	@Override
	public AddressInfo retrieveSubscriberAddress(@BANValue int ban, String subscriber)
	throws ApplicationException {
		return addressDao.retrieveSubscriberAddress(ban, subscriber);

	}
	
	@Override
	public Collection retrievePartiallyReservedSubscriberListByBan(@BANValue int ban,
			int maximum) throws ApplicationException {
		return subscriberDao.retrievePartiallyReservedSubscriberListByBan(ban, maximum);
	}
	@Override
	public Collection retrievePortedSubscriberListByBAN(@BANValue int ban, int listLength)
	throws ApplicationException {
		return subscriberDao.retrievePortedSubscriberListByBAN(ban, listLength);
	}
	@Override
	public ResourceChangeHistoryInfo[] retrieveResourceChangeHistory(@BANValue int ban,
			String subscriberID, String type, Date from, Date to)
	throws ApplicationException {
		List<ResourceChangeHistoryInfo> rchiList=subscriberDao.retrieveResourceChangeHistory(ban, subscriberID, type, from, to);
		return rchiList.toArray(new ResourceChangeHistoryInfo[rchiList.size()]);
	}
	@Override
	public Collection retrieveLightWeightSubscriberListByBAN(@BANValue int banId, boolean isIDEN, int listLength, boolean includeCancelled)
	throws ApplicationException {
		int maxListLength = AppConfiguration.getLightWeightSubMax();
		
		if (listLength <= 0 || listLength > maxListLength) {
			listLength = maxListLength; 
		}
		return subscriberDao.retrieveLightWeightSubscriberListByBAN(banId, isIDEN, listLength, includeCancelled);
	}
	@Override
	public String retrieveLastAssociatedSubscriptionId(String imsi)
	throws ApplicationException {
		return subscriberDao.retrieveLastAssociatedSubscriptionId(imsi);
	}
	@Override
	public boolean retrieveHotlineIndicator(String subscriberId)
	throws ApplicationException {
		return subscriberDao.retrieveHotlineIndicator(subscriberId);
	}
	@Override
	public String getPortProtectionIndicator(@BANValue int ban, String subscriberId,
			String phoneNumber, String status) throws ApplicationException {
		return subscriberDao.getPortProtectionIndicator(ban, subscriberId, phoneNumber, status);
	}
	@Override
	public SubscriptionPreferenceInfo retrieveSubscriptionPreference(
			long subscriptionId, int preferenceTopicId)
	throws ApplicationException {
		return subscriberDao.retrieveSubscriptionPreference(subscriptionId, preferenceTopicId);
	}
	@Override
	public String[] retrieveAvailableCellularPhoneNumbersByRanges(
			PhoneNumberReservationInfo phoneNumberReservation,
			String startFromPhoneNumber, String searchPattern, boolean asian,
			int maxNumber) throws ApplicationException {
		List<String> phoneList=subscriberDao.retrieveAvailableCellularPhoneNumbersByRanges(
				phoneNumberReservation, startFromPhoneNumber, searchPattern, asian, maxNumber);
		return phoneList.toArray(new String[phoneList.size()]);
	}
	@Override
	public List<DepositHistoryInfo> retrieveDepositHistory(@BANValue int ban,
			String subscriber) throws ApplicationException {
		return depositDao.retrieveDepositHistory(ban, subscriber);
	}
	@Override
	public double retrievePaidSecurityDeposit(@BANValue int banId, String subscriberNo,
			String productType) throws ApplicationException {
		return depositDao.retrievePaidSecurityDeposit(banId, subscriberNo, productType);
	}
	@Override
	public List<TalkGroupInfo> retrieveTalkGroupsBySubscriber(
			String subscriberId) throws ApplicationException {
		return fleetDao.retrieveTalkGroupsBySubscriber(subscriberId);
	}
	@Override
	public InvoiceTaxInfo retrieveInvoiceTaxInfo(@BANValue int ban, String subscriberId,
			int billSeqNo) throws ApplicationException {
		return invoiceDao.retrieveInvoiceTaxInfo(ban, subscriberId, billSeqNo);
	}
	@Override
	public MemoInfo retrieveLastMemo(@BANValue int ban, String subscriberID,
			String memoType) throws ApplicationException {
		MemoInfo memo = memoDao.retrieveLastMemo(ban, subscriberID, memoType);
		if (memo == null){	
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.MEMO_NOT_FOUND, "Memo Not Found","");					 
		}
		return memo;
	}
	@Override
	public int getCountForRepairID(String repairID) {
		return subscriberEquipmentDao.getCountForRepairID(repairID);
	}
	@Override
	public List<EquipmentChangeHistoryInfo> retrieveEquipmentChangeHistory(
			@BANValue int ban, String subscriberID, Date from, Date to) throws ApplicationException {
		return subscriberEquipmentDao.retrieveEquipmentChangeHistory(ban, subscriberID, from, to);
	}
	@Override
	public List<HandsetChangeHistoryInfo> retrieveHandsetChangeHistory(@BANValue int ban,
			String subscriberID, Date from, Date to) throws ApplicationException {
		return subscriberEquipmentDao.retrieveHandsetChangeHistory(ban, subscriberID, from, to);
	}

	@Override	//@BANValue - BANVALIDATOR is not included in this method as this method may carry zero value for ban.
	public String retrieveSubscriberIDByPhoneNumber(int ban, String phoneNumber) throws ApplicationException {	
			return subscriberDao.retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(ban, phoneNumber).getSubscriberId();
	}
	
	@Override
	public SubscriberIdentifierInfo retrieveSubscriberIdentifiersByPhoneNumber(int ban, String phoneNumber)
		throws ApplicationException {
		 SubscriberIdentifierInfo subscriberIdentifierInfo =subscriberDao.retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(ban, phoneNumber);
		
		if (subscriberIdentifierInfo.getSubscriberId() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found: BAN [" + ban + "], phone number [" + phoneNumber + "]","");
		}
		return subscriberIdentifierInfo;
	}

	private void replaceDummyESN(SubscriberInfo subscriberInfo) throws ApplicationException {
		if (subscriberInfo != null) {
			Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
			subscriberList.add(subscriberInfo);
			replaceDummyESN(subscriberList);
		}
	}

	private void replaceDummyESN(Collection<SubscriberInfo> subscriberList) throws ApplicationException {

		ArrayList<String> hspaIMSIList = new ArrayList<String>();
		for (SubscriberInfo subinfo : subscriberList) {
			if (subinfo.hasDummyESN() && subinfo.getSerialNumber().equals(EquipmentInfo.DUMMY_ESN_FOR_USIM)) {
				HSPASubscriberInfo hspaSubscriberInfo = (HSPASubscriberInfo) subinfo;
				String imsi = hspaSubscriberInfo.getHspaImsi();
				if (imsi != null) {
					hspaIMSIList.add(imsi);
				}
			}
		}
		if (hspaIMSIList.size() > 0) {
			Hashtable<String, String> usimList = subscriberEquipmentDao.getUSIMListByIMSIs(hspaIMSIList.toArray(new String[hspaIMSIList.size()]));
			for (SubscriberInfo subinfo : subscriberList) {
				if (subinfo.hasDummyESN() && subinfo.getSerialNumber().equals(EquipmentInfo.DUMMY_ESN_FOR_USIM)) {
					String usimId = "";
					HSPASubscriberInfo hspaSubscriberInfo = (HSPASubscriberInfo) subinfo;
					String imsi = hspaSubscriberInfo.getHspaImsi();
					if (imsi != null) {
						if (usimList.containsKey(imsi)) {
							usimId = usimList.get(imsi);
						}
					}
					subinfo.setSerialNumber(usimId);
				}
			}
		}
	}
	
	@Override
	public SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException {
		SubscriberInfo subscriberInfo = null;
		//Validate the phoneNumber.
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Invalid phone number: ["+ phoneNumber + "]", "");
		}
		
		Collection<SubscriberInfo> subscribers = subscriberDao.retrieveSubscriberListByPhoneNumber(phoneNumber, 10, false);
		Iterator<SubscriberInfo> iterator = subscribers.iterator();
		if (iterator.hasNext()) {
			subscriberInfo = iterator.next();
		}
			
		if (subscriberInfo != null) {
			replaceDummyESN(subscriberInfo);
			return subscriberInfo;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"Subscriber not found for phone number: " + phoneNumber, "");
	}

	@Override
	public List<SubscriberHistoryInfo> retrieveSubscriberHistory(@BANValue int ban, String subscriberID,
			Date from, Date to) throws ApplicationException{
		return subscriberDao.retrieveSubscriberHistory(ban, subscriberID, from, to);
	}
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBAN(@BANValue int ban, int listLength) throws ApplicationException {
		return this.retrieveSubscriberListByBAN(ban, listLength, false);
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBAN(@BANValue int ban, int listLength, boolean includeCancelled) throws ApplicationException {
		Collection<SubscriberInfo> subList = new ArrayList<SubscriberInfo>();
		subList = subscriberDao.retrieveSubscriberListByBAN(ban, listLength, includeCancelled);
		replaceDummyESN(subList);
		return subList;
	}
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBanAndFleet(@BANValue int ban, int urbanId,
			int fleetId, int listLength) throws ApplicationException {
		return subscriberDao.retrieveSubscriberListByBanAndFleet(ban, urbanId, fleetId, listLength);
	}
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByBanAndTalkGroup(
			@BANValue int ban, int urbanId, int fleetId, int talkGroupId, int listLength)
			throws ApplicationException {
		return subscriberDao.retrieveSubscriberListByBanAndTalkGroup(ban, urbanId, fleetId, talkGroupId, listLength);
	}
	@Override
	public SubscriptionRoleInfo retrieveSubscriptionRole(String phoneNumber) {
		return subscriberDao.retrieveSubscriptionRole(phoneNumber);
	}
	@Override
	public boolean isPortRestricted(@BANValue int ban, String subscriberId,
			String phoneNumber, String status)throws ApplicationException {
		return subscriberDao.isPortRestricted(ban, subscriberId, phoneNumber, status);
	}
	@Override
	public List<String> retrieveSubscriberPhonenumbers(char subscriberStatus,
			char accountType, char accountSubType, char banStatus, int maximum)
			throws ApplicationException {
		return subscriberDao.retrieveSubscriberPhonenumbers(subscriberStatus, accountType, accountSubType, banStatus, maximum);
	}
	@Override
	public List<String> retrieveSubscriberPhonenumbers(char subscriberStatus,
			char accountType, char accountSubType, char banStatus,
			String addressType, int maximum) throws ApplicationException {
		return subscriberDao.retrieveSubscriberPhonenumbers(subscriberStatus, accountType, accountSubType, banStatus, addressType, maximum);
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySerialNumber(String serialNumber,
			boolean includeCancelled) throws ApplicationException {
		if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(serialNumber)) {
			throw new ApplicationException (SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_SERAIL_NUMBER, "Cannot use dummy USIM number for retrieveSubscriberListBySerialNumber", "");
		}
		
		Collection<SubscriberInfo> subList = new ArrayList<SubscriberInfo>();
		List<String> imsi= new ArrayList<String>();
		imsi = subscriberEquipmentDao.getIMSIsBySerialNumber(serialNumber);
		if (imsi.size() == 0){ // CDMA Equipment
			subList = subscriberDao.retrieveSubscriberListBySerialNumber(serialNumber,includeCancelled);
			if(subList == null || subList.size()==0) {
				throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscribers not found for serial number " + serialNumber,"");
			}
		}
		else{
			subList = retrieveHSPASubscriberListByIMSI(imsi.get(0), includeCancelled);
		}

		replaceDummyESN(subList);
		return subList;

	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListBySerialNumber(String serialNumber)
	throws ApplicationException {
		return retrieveSubscriberListBySerialNumber(serialNumber,false);
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumber(String phoneNumber, int listLength, boolean includeCancelled)throws ApplicationException {
		// Validate the phone number
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Invalid phone number: ["+ phoneNumber + "]", "");
		}
		Collection<SubscriberInfo> subList = null;
		subList = subscriberDao.retrieveSubscriberListByPhoneNumber(phoneNumber, listLength, includeCancelled);
		//Replace the dummy esn with actual serial number 
		if (CollectionUtils.isNotEmpty(subList)) {
			replaceDummyESN(subList);
			return subList;
		}
		// Validate the subscriber List and throw an error
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"Subscriber not found for ["+ phoneNumber + "]", "");
	}
	
	//Naresh.A , Added below method to test the package rule hint subscriber retrievals , this should be removed after Oct 2018 release.
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbersPkgRuleHint(String[] phoneNumbers, boolean includeCancelled) throws ApplicationException {
		Collection<SubscriberInfo> subList = null;
		subList= subscriberDao.retrieveSubscriberListByPhoneNumbers(new ArrayList<String>(Arrays.asList(phoneNumbers)), 100, includeCancelled);
		//Replace Dummy ESNs with real ESNs
		if(CollectionUtils.isNotEmpty(subList)) {
			replaceDummyESN(subList);
			return subList;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"Subscriber not found for "+Arrays.toString(phoneNumbers), "");
	}
	
	//Did not see any references for this method, should be deleted.
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumbers(String[] phoneNumbers, boolean includeCancelled) throws ApplicationException {
		Collection<SubscriberInfo> subList = null;
		subList= subscriberDao.retrieveSubscriberListByPhoneNumbers(new ArrayList<String>(Arrays.asList(phoneNumbers)), 0, includeCancelled);
		//Replace Dummy ESNs with real ESNs
		if(CollectionUtils.isNotEmpty(subList)) {
			replaceDummyESN(subList);
			return subList;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"Subscriber not found for "+Arrays.toString(phoneNumbers), "");
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByImsi(String imsi,
			boolean includeCancelled) throws ApplicationException {
		Collection<SubscriberInfo> subList = new ArrayList<SubscriberInfo>();
		subList = retrieveHSPASubscriberListByIMSI(imsi, includeCancelled);
		//Replace Dummy ESNs with real ESNs
		replaceDummyESN(subList);
		return subList;
	}
	
	@Override
	public EquipmentSubscriberInfo[] retrieveEquipmentSubscribers(String serialNumber, boolean active) throws ApplicationException {
		
		if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(serialNumber)) {
			throw new ApplicationException (SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_SERAIL_NUMBER, "Cannot use dummy USIM number for retrieveEquipmentSubscribers", "");
		}
		List<String> imsi  = subscriberEquipmentDao.getIMSIsByUSIM(serialNumber);
		
		if (imsi.isEmpty()) { 
			// CDMA  & MIKE Equipment 
			return retrieveNonHSPAEquipmentSubscribers(serialNumber, active);
		} else {
			return retrieveHSPAEquipmentSubscribers(serialNumber, active, imsi.toArray(new String[imsi.size()]));
		}

	}

	@Override
	public Collection<SubscriberInfo> retrieveHSPASubscriberListByIMSI(String IMSI, boolean includeCancelled) throws ApplicationException {
		return subscriberDao.retrieveHSPASubscriberListByIMSI(IMSI, includeCancelled);
	}
	
	@Override
	public PortOutEligibilityInfo checkSubscriberPortOutEligibility(String phoneNumber, String ndpInd) throws ApplicationException {
		
		PortOutEligibilityInfo portOutEligibilityInfo = new PortOutEligibilityInfo();
		try{
			if (ndpInd == null) {
				throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.NDP_DIRECTION_INDICATOR_MISSING, "NDP direction indicator is missing.","");
			}

			// get the list of subscribers from the phone number
			Collection<SubscriberInfo> subList = retrieveSubscriberListByPhoneNumber(phoneNumber, 1, true);

			if (subList.isEmpty()) {
				throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found for phone number [" + phoneNumber + "]","");
			}

			SubscriberInfo subInfo = subList.iterator().next();
			if (subInfo.getStatus() == Subscriber.STATUS_CANCELED) {
				throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Subscriber is in cancelled status.","");
			}

			if (subInfo.getStatus() == Subscriber.STATUS_SUSPENDED ) {

				if (Subscriber.PORT_TYPE_PORT_OUT.equals(subInfo.getPortType()) ) {
					throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_SUSPENDED_STATUS, "Subscriber is in suspended status.","");
				}

				// check the suspended activity code 
				if ("SUS".equals(subInfo.getActivityCode()) &&
						getReferenceDataHelper().isPortOutAllowed(String.valueOf(subInfo.getStatus()),
								subInfo.getActivityCode(), subInfo.getActivityReasonCode()) == false) {
					LOGGER.error("id=APP20011; Subscriber is in suspended status.");
					throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_SUSPENDED_STATUS, "Subscriber is in suspended status.","");
				}
			}

			boolean isPortRestricted = false;
			if (PortOutEligibility.NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS.equals(ndpInd)) {
				String portProtectionIndicator = getPortProtectionIndicator(subInfo.getBanId(), subInfo.getSubscriberId(), subInfo.getPhoneNumber(), String.valueOf(subInfo.getStatus()));
				if (portProtectionIndicator == null) {
					portProtectionIndicator = getPortProtectionIndicator(subInfo.getBanId(), null, null, null);
				}
				isPortRestricted = AccountSummary.PORT_RESTRICTED.equals(portProtectionIndicator);
			}

			if (isPortRestricted == true) {
				portOutEligibilityInfo.setEligible(false);
				portOutEligibilityInfo.setTransferBlocking(true);
			} else {
				portOutEligibilityInfo.setEligible(true);
				portOutEligibilityInfo.setTransferBlocking(false);
			}
		}catch (TelusException te){
			LOGGER.error("TelusException occurred",  te);
			throw new ApplicationException(te.id,te.getMessage() == null ? "TelusException occurred (" + te.toString() + ") - see stack trace for details" : te.getMessage(),"",te); 
		} 
		return portOutEligibilityInfo;
	}

	private ReferenceDataHelper getReferenceDataHelper() {
		if (referenceDataHelper == null) {
			referenceDataHelper = EJBUtil.getHelperProxy(ReferenceDataHelper.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
		}
		return referenceDataHelper;
	}
	
	@Override
	public VoiceUsageSummaryInfo retrieveVoiceUsageSummary(int banId, String subscriberId, String featureCode) throws ApplicationException {

		if (featureCode == null || featureCode.length() == 0) {
			featureCode = "STD";
		}

		return usageDao.retrieveVoiceUsageSummary(banId, subscriberId, featureCode);
	}
	
	@Override
	public long getSubscriptionId(int ban, String phoneNumber, String status) throws ApplicationException {
		return subscriberDao.retrieveSubscriptionId(ban, phoneNumber, status);		
	}

	@Override
	public String retrieveEmailAddress(int ban, String subscriberNumber) throws ApplicationException {
		try {
			return subscriberDao.retrieveEmailAddress(ban, subscriberNumber);
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.BAN_OR_SUBSCRIBER_NOT_FOUND_OR_SUBSCRIBER_NOT_ACTIVE, "BAN or Subscriber does not exist, or Subscriber is not Active.", "", t);
		}
	}
	
	@Override
	public FeatureParameterHistoryInfo retrieveLastEffectiveFeatureParameter(int banId, String subscriberId, String productType,String serviceCode, String featureCode) throws ApplicationException {
		return serviceAgreementDao.retrieveLastEffectiveFeatureParameter ( banId, subscriberId, productType, serviceCode, featureCode);
	}
	
	@Override
	public FeatureParameterHistoryInfo[] retrieveCallingCircleParametersByDate( int banId, String subscriberId, String productType, Date fromDate) throws ApplicationException {
		List<FeatureParameterHistoryInfo> list=serviceAgreementDao.retrieveCallingCircleParametersByDate ( banId, subscriberId, productType, fromDate );
		return list.toArray(new FeatureParameterHistoryInfo[list.size()]);
	}

	@Override
	public SubscriberInfo retrieveLatestSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException {
		// Validate the phoneNumber
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.INVALID_PHONE_NUMBER, "Invalid phone number ["+ phoneNumber + "]", "");
		}
		Collection<SubscriberInfo> subscribers = null;
		subscribers = subscriberDao.retrieveSubscriberListByPhoneNumber(phoneNumber,10, true);
		// Filter the Latest Subscriber by status and status Date.
		SubscriberInfo latestSubscriber = getLatestSubscriberBySubStatusDate(subscribers);
		 //Replace the kb dummy esn with  actual serial number.
		if (latestSubscriber!=null) {
			replaceDummyESN(latestSubscriber);
			return latestSubscriber;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, "subscriber not found for phone number: " + phoneNumber, "");
	}
	
	@Override
	public SubscriberInfo retrieveLatestSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo)throws ApplicationException {
		if (phoneNumber == null || phoneNumber.trim().length() < 10) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.INVALID_PHONE_NUMBER, "Invalid phone number ["+ phoneNumber + "]", "");
		}
		Collection<SubscriberInfo> subscribers;
		subscribers = retrieveSubscriberListByPhoneNumber(phoneNumber,phoneNumberSearchOptionInfo, 10, true);
		SubscriberInfo latestSubscriber = getLatestSubscriberBySubStatusDate (subscribers);

		if (latestSubscriber!=null) {
			replaceDummyESN(latestSubscriber);
			return latestSubscriber;
		}
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"subscriber not found in either Wireless/VOIP search for phonenumber : " + phoneNumber, "");
	}
	
	/**
	 * Assumption: Incoming list has the subscriber status in following order already: A, S, C, R
	 * Return the latest subscriber according to subscriber status date. 
	 * 
	 * @param subscribers
	 * @return
	 */
	private SubscriberInfo getLatestSubscriberBySubStatusDate(Collection<SubscriberInfo> subscribers) {
		if (CollectionUtils.isEmpty(subscribers)) {
			return null;
		}		
		List<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>(subscribers);
		if (subscriberList.get(0).getStatus() != Subscriber.STATUS_CANCELED) { 
		// our queries should have sorted subscribers according to sub status already ,the only time we need to pick the latest subscriber among a list is when there's a list of cancelled subscribers
			return subscriberList.get(0); //return the first element 
		}

		Collections.sort(subscriberList, new Comparator<SubscriberInfo>() {
			@Override
			public int compare(SubscriberInfo sub1, SubscriberInfo sub2) {
				if (sub1.getStatusDate() != null && sub2.getStatusDate() != null) {
					if (sub1.getStatusDate().equals(sub2.getStatusDate())) { //this condition is required because this comparator behaves differently between jdk 6 and 7
						return 0;
					}else {
						return (sub1.getStatusDate().after(sub2.getStatusDate()) ? -1 : 1);
					}
				}
				return 0;
			}
			
		});
		return subscriberList.get(0);
	}
	
	
	@Override
	public SubscriberInfo retrieveLatestSubscriberBySubscriptionId(long subscriptionId) throws ApplicationException {
		SubscriberIdentifierInfo subIdentifier = retrieveSubscriberIdentifierBySubscriptionId(subscriptionId);
		if (subIdentifier != null) {
			if (subIdentifier.getBan() != 0 && subIdentifier.getPhoneNumber() != null && subIdentifier.getPhoneNumber().trim().isEmpty() == false) {
				return retrieveSubscriberByPhoneNumber(subIdentifier.getBan(), subIdentifier.getPhoneNumber());
			}else {
				LOGGER.debug("retrieveLatestSubscriberBySubscriptionId phoneNumber is empty. subscriptionId=["+subscriptionId+"]");
			}
		}
		return null;
	}
	
	@Override
	public SubscriberIdentifierInfo retrieveSubscriberIdentifierBySubscriptionId(long subscriptionId) throws ApplicationException {
		SubscriberIdentifierInfo subIdentifier =  subscriberDao.retrieveSubscriberIdentifierBySubscriptionId(subscriptionId);
		if (subIdentifier == null || subIdentifier.getPhoneNumber() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found: subscriptionId [" + subscriptionId + "]","");
		}
		
		return subIdentifier;
	}
	@Override
	public String retrieveChangedSubscriber(int ban,String subscriberId,String productType, Date searchFromDate, Date searchToDate) throws ApplicationException {
		return subscriberDao.retrieveChangedSubscriber(ban, subscriberId, productType, searchFromDate, searchToDate);
	}
	
	@Override
	public List<Double> retrieveAdustmentDetails(int ban,String adjustmentReasonCode, String subscriberId,Date searchFromDate, Date searchToDate) throws ApplicationException {
		return adjustmentDao.retrieveAdustmentDetails(ban, adjustmentReasonCode, subscriberId, searchFromDate, searchToDate);
	}
	
	@Override
	public EquipmentSubscriberInfo[] retrieveHSPAEquipmentSubscribers(String serialNumber, boolean active,String[] imsi) throws ApplicationException {

		Collection<EquipmentSubscriberInfo> equipmentSubscriberInfoList = new ArrayList<EquipmentSubscriberInfo>();
		if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(serialNumber)) {
			throw new ApplicationException (SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_SERAIL_NUMBER, "Cannot use dummy USIM number for retrieveEquipmentSubscribers", "");
		}
		Collection<SubscriberInfo> subscriberInfoCollection = retrieveHSPASubscriberListByIMSI(imsi[0], !active);
		if (subscriberInfoCollection != null) {
				for(SubscriberInfo subscriberInfo:subscriberInfoCollection){

					EquipmentSubscriberInfo equipmentSubscriber = new EquipmentSubscriberInfo();
					equipmentSubscriber.setBanId(subscriberInfo.getBanId());
					equipmentSubscriber.setPhoneNumber(subscriberInfo.getPhoneNumber());
					equipmentSubscriber.setSubscriberId(subscriberInfo.getPhoneNumber());
					equipmentSubscriber.setProductType(subscriberInfo.getProductType());
					equipmentSubscriberInfoList.add(equipmentSubscriber); 
				} 	 
			} else { 
				return  new EquipmentSubscriberInfo[0];
			}
		
		return equipmentSubscriberInfoList.toArray(new EquipmentSubscriberInfo[equipmentSubscriberInfoList.size()]); 
	}

	@Override
	public EquipmentSubscriberInfo[] retrieveNonHSPAEquipmentSubscribers(String serialNumber, boolean active) throws ApplicationException {

		if (EquipmentInfo.DUMMY_ESN_FOR_USIM.equals(serialNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_SERAIL_NUMBER, "Cannot use dummy USIM number for retrieveEquipmentSubscribers", "");
		}
		
		List<EquipmentSubscriberInfo> esiList = subscriberEquipmentDao.retrieveEquipmentSubscribers(serialNumber, active);
		
		return esiList.toArray(new EquipmentSubscriberInfo[esiList.size()]);
	}

	@Override
	public SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {
		SubscriberInfo subscriberInfo = null;
		// Validate the phoneNumber.
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, "Invalid phone number: [" + phoneNumber + "]", "");
		}
		// Retrieve the subscriber by phone number ( wireless number) 
		if (phoneNumberSearchOptionInfo.isSearchWirelessNumber()) {
			
			Collection<SubscriberInfo> subscribers = subscriberDao.retrieveSubscriberListByPhoneNumber(phoneNumber, 10, false);
			Iterator<SubscriberInfo> iterator = subscribers.iterator();
			
			if (iterator.hasNext()) {
				subscriberInfo = iterator.next();
			}
			
			if (subscriberInfo != null) {
				replaceDummyESN(subscriberInfo);
				return subscriberInfo;
			}
		}
			// Retrieve the subscriber by seat number ( resource number) 
		if (phoneNumberSearchOptionInfo.isSearchHSIA() || phoneNumberSearchOptionInfo.isSearchVOIP() || phoneNumberSearchOptionInfo.isSearchTollFree()) {
			Collection<SubscriberInfo> subscribers = subscriberDao.retrieveSubscriberListByBanAndSeatResourceNumber(0,phoneNumber, 10, false);
			Iterator<SubscriberInfo> iterator = subscribers.iterator();
			
			if (iterator.hasNext()) {
				subscriberInfo = iterator.next();
			}
			
			// Replace the kb dummy esn with actual serial number.
			if (subscriberInfo != null) {
				replaceDummyESN(subscriberInfo);
				return subscriberInfo;
			}
		}
		// Throw an error if subscriber is not exists in DB.
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber Not found in Wireless Or VOIP search : [" + phoneNumber + "]", "");
	}
	
	@Override
	public Collection<SubscriberInfo> retrieveSubscriberListByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo, int listLength, boolean includeCancelled)
			throws ApplicationException {
		// Refactored entire method to fix defect 51932 (VoIP numbers are not returned using SD if there are cancelled WLS numbers in the system) 
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, "Invalid search criteria - phone number is blank.", "");
		}

		// Build the list of subscribers, depending on the PhoneNumberSearchOptionInfo criteria
		Collection<SubscriberInfo> subscriberList = new ArrayList<SubscriberInfo>();
		if (phoneNumberSearchOptionInfo.isSearchWirelessNumber()) {
			Collection<SubscriberInfo> wirelessList = null;
			// Retrieve the wireless subscribers and add to the list of subscribers (if not empty or null)
			wirelessList = subscriberDao.retrieveSubscriberListByPhoneNumber(phoneNumber,listLength, includeCancelled);
			if (CollectionUtils.isNotEmpty(wirelessList)) {
				subscriberList.addAll(wirelessList);
			}
		}
		
		if (phoneNumberSearchOptionInfo.isSearchHSIA() || phoneNumberSearchOptionInfo.isSearchVOIP() || phoneNumberSearchOptionInfo.isSearchTollFree()) {
			Collection<SubscriberInfo> voipList = null;
			// Retrieve the VOIP subscribers and add to the list of subscribers (if not empty or null)
			voipList = subscriberDao.retrieveSubscriberListByBanAndSeatResourceNumber(0, phoneNumber, listLength, includeCancelled);
			if (CollectionUtils.isNotEmpty(voipList)) {
				subscriberList.addAll(voipList);
			}
		}
		
		// Return the list of subscribers if not empty or null
		if (CollectionUtils.isNotEmpty(subscriberList)) {
			// Replace dummy ESNs for USIM HSPA subscribers
			replaceDummyESN(subscriberList);
			return subscriberList;
		}	
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, " Subscriber not found in Wireless or VOIP search  [" + phoneNumber + "].", "");
	}
	
	@Override
	public SubscriberInfo retrieveSubscriberByBanAndPhoneNumber(@BANValue int ban, String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException {
		if (StringUtils.isBlank(phoneNumber)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.INVALID_PHONE_NUMBER, "Invalid phone number [" + phoneNumber + "]", "");
		}
		SubscriberInfo subscriberInfo = null;
		if (phoneNumberSearchOptionInfo.isSearchWirelessNumber()) {
			subscriberInfo = subscriberDao.retrieveSubscriberByBANAndPhoneNumber(ban, phoneNumber);
			if (subscriberInfo != null) {
				replaceDummyESN(subscriberInfo);
				return subscriberInfo;
			}
		}
		if (phoneNumberSearchOptionInfo.isSearchHSIA() || phoneNumberSearchOptionInfo.isSearchVOIP() || phoneNumberSearchOptionInfo.isSearchTollFree()) {	
			
			Collection<SubscriberInfo> subscribers = subscriberDao.retrieveSubscriberListByBanAndSeatResourceNumber(ban,phoneNumber, 10, false);
			Iterator<SubscriberInfo> iterator = subscribers.iterator();
			if (iterator.hasNext()) {
				subscriberInfo = iterator.next();
			}

			if (subscriberInfo != null) {
				replaceDummyESN(subscriberInfo);
				return subscriberInfo;
			}
		}
		
		throw new ApplicationException(SystemCodes.CMB_SLH_EJB, ErrorCodes.SUBSCRIBER_NOT_FOUND, "Subscriber not found in either Wireless/VOIP search [" + phoneNumber + "].", "");
	}
	
	
	@Override
	public SubscriberIdentifierInfo retrieveSubscriberIdentifiersByPhoneNumber(int ban, String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOption) throws ApplicationException {
		SubscriberIdentifierInfo subscriberIdentifierInfo = null;
		if (phoneNumberSearchOption.isSearchWirelessNumber()) {
			subscriberIdentifierInfo = subscriberDao.retrieveLatestSubscriberIdentifierInfoByBanAndPhoneNumber(ban, phoneNumber);
			
			/* Code block will not trigger because subscriberIdentifierInfo will never be null
			if (subscriberIdentifierInfo == null) {
				if (phoneNumberSearchOption.isSearchHSIA()|| phoneNumberSearchOption.isSearchVOIP() || phoneNumberSearchOption.isSearchTollFree()) {
				subscriberIdentifierInfo = subscriberDao.retrieveLatestSubscriberIdentifierInfoBySeatResourceNumber(ban, phoneNumber);
				}
			}
	
			if (subscriberIdentifierInfo == null) {
				throw new ApplicationException(SystemCodes.CMB_SLH_EJB,ErrorCodes.SUBSCRIBER_NOT_FOUND,"SubscriberIdentifierInfo not Found in either Wireless/VOIP search.", "");
			}*/
		}
		return subscriberIdentifierInfo;
	}
	
	@Override
	public String[] retrieveCSCFeatureByPhoneNumber(String phoneNumber,String productType)
			throws ApplicationException {
		List<String> list=serviceAgreementDao. retrieveCSCFeatureByPhoneNumber(phoneNumber, productType);
		return list==null? new String[0] : list.toArray(new String[list.size()]);
	}
	
	@Override
	public void updateSubscriptionRole(String phoneNumber,String subscriptionRoleCd) throws ApplicationException {
		subscriberDao.updateSubscriptionRole(phoneNumber,subscriptionRoleCd);
	}

	@Override
	public CommunicationSuiteInfo retrieveCommunicationSuite(int ban, String subscriberNum, int companionCheckLevel) throws ApplicationException {
		
		LOGGER.debug("retrieveCommunicationSuite.. ban=["+ban+"], subscriberNum=["+subscriberNum+"]");
		
		CommunicationSuiteInfo commSuiteInfo = this.communicationSuiteDao.retrieveCommunicationSuite(ban, subscriberNum, companionCheckLevel);
		
		if(commSuiteInfo == null){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("CommunicationSuite not exists , ban=["+ban+"], subscriberNum=["+subscriberNum+"], companionCheckLevel = ["+companionCheckLevel+"]");
			}
			return null;
		}
		
		
		for (PairingGroupInfo pairingGroup : commSuiteInfo.getPairingGroupList()) {
			List<String> suitePhoneList = pairingGroup.getSuitePhoneNumberList();
			
			if (suitePhoneList != null && suitePhoneList.isEmpty() == false) {
				List<LightWeightSubscriberInfo> lwSuiteSubInfoList = retrieveLightWeightSubscriberListInSameBan(ban, suitePhoneList.toArray(new String[suitePhoneList.size()]));
				for (LightWeightSubscriberInfo lightWeightSubscriberInfo : lwSuiteSubInfoList) {
					if(StringUtils.equals(lightWeightSubscriberInfo.getSubscriberId(), pairingGroup.getPrimaryPhoneNumber())){
						pairingGroup.setLwPrimarySubscriberInfo(lightWeightSubscriberInfo);
						commSuiteInfo.setLwPrimarySubscriberInfo(lightWeightSubscriberInfo);
					}else {
						pairingGroup.getLwCompanionSubMap().put(lightWeightSubscriberInfo.getSubscriberId(), lightWeightSubscriberInfo);
					}
				}
				
				if (pairingGroup.getLwCompanionSubMap().size() != pairingGroup.getCompanionPhoneNumberList().size() && LOGGER.isDebugEnabled()) {
					LOGGER.debug("Companion size mismatch [" + pairingGroup.getLwCompanionSubMap().size() + ", " + suitePhoneList.size()+"]. BAN ["+ban+"] SUB=["+subscriberNum+"]");
				}
			}
			
		}
		
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("communicationSuiteInfo, commSuiteInfo="+commSuiteInfo);
		}
		return commSuiteInfo;
	}

	
	@Override
	public List<LightWeightSubscriberInfo> retrieveLightWeightSubscriberListInSameBan(
			int ban, String[] subscriberNumList) throws ApplicationException {
		List<LightWeightSubscriberInfo> subList = new ArrayList<LightWeightSubscriberInfo> ();
		if (ban > 0 && subscriberNumList != null && subscriberNumList.length > 0) {
			subList = subscriberDao.retrieveLightWeightSubscriberListInSameBan(ban, subscriberNumList);
		}
		
		return subList;
	}
	
	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return testPointDao.testKnowbilityDataSource();
	}
	@Override
	public TestPointResultInfo testEcpcsDataSource() {
		return testPointDao.testEcpcsDataSource();
	}
	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}
	@Override
	public TestPointResultInfo testServDataSource() {
		return testPointDao.testServDataSource();
	}
	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}
	@Override
	public TestPointResultInfo testEasDataSource() {
		return testPointDao.testEasDataSource();
	}
	@Override
	public TestPointResultInfo testConeDataSource() {
		return testPointDao.testConeDataSource();
	}
	
	@Override
	public TestPointResultInfo testSubscriptionService() {
		return subscriptionServiceDao.test();
	}

	@Override
	public TestPointResultInfo getSubscriberPrefPkgVersion() {
		return testPointDao.getSubscriberPrefPkgVersion();
	}

	@Override
	public TestPointResultInfo getSubscriberPkgVersion() {
		return testPointDao.getSubscriberPkgVersion();
	}

	@Override
	public TestPointResultInfo getHistoryUtilityPkgVersion() {
		return testPointDao.getHistoryUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getMemoUtilityPkgVersion() {
		return testPointDao.getMemoUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getFleetUtilityPkgVersion() {
		return testPointDao.getFleetUtilityPkgVersion();
	}
	
	@Override
	public TestPointResultInfo getSubRetrievalPkgVersion() {
		return testPointDao.getSubsRetrievalPkgVersion();
	}
	@Override
	public TestPointResultInfo getSubAttrbRetrievalpkgVersion() {
		return testPointDao.getSubsAttrbRetrievalPkgVersion();
	}
	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}
}
