package com.telus.cmb.subscriber.lifecyclemanager.svc.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.util.Utility;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.AddressDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.DepositDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.FleetDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.NewSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.OrderServiceDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.ServiceAgreementDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriberDaoFactory;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriptionServiceDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenPcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateIdenSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdatePcsSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UpdateSubscriberDao;
import com.telus.cmb.subscriber.lifecyclemanager.dao.UsageDao;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManagerTestPoint;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.UsageProfileListsSummaryInfo;
import com.telus.eas.utility.info.FeatureAirTimeAllocationInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name="SubscriberLifecycleManager", mappedName="SubscriberLifecycleManager")
@Remote({SubscriberLifecycleManager.class, SubscriberLifecycleManagerTestPoint.class})
@RemoteHome(SubscriberLifecycleManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubscriberLifecycleManagerImpl implements SubscriberLifecycleManager, SubscriberLifecycleManagerTestPoint {

	private static final Logger LOGGER = Logger.getLogger(SubscriberLifecycleManagerImpl.class);

	@EJB
	private SubscriberLifecycleHelper subscriberLifecycleHelper = null;
	
	private EJBController ejbController;
	
	@Autowired
	private SubscriberDao subscriberDao;
	@Autowired
	private UsageDao usageDao;
	@Autowired
	private AddressDao addressDao;
	@Autowired
	private FleetDao fleetDao;
	@Autowired
	private DepositDao depositDao ;
	@Autowired
	private ServiceAgreementDao serviceAgreementDao ;
	@Autowired
	private OrderServiceDao orderServiceDao;
	@Autowired
	private SubscriptionServiceDao subscriptionServiceDao;
	@Autowired
	private SubscriberDaoFactory subscriberDaoFactory;	
	@Autowired
	private AmdocsSessionManager amdocsSessionManager;
	@Autowired
	private DataSourceTestPointDao testPointDao;
	@Autowired
	PrepaidSubscriberServiceDao prepaidSubscriberServiceDao;
	@Autowired
	private PrepaidWirelessCustomerOrderServiceDao prepaidWirelessCustomerOrderServiceDao;
	
	@PostConstruct
	public void initialize() {
		initializeEjbController();
	}
	
	private void initializeEjbController() {
		ejbController = new EJBController();
		ejbController.setEjb(AccountLifecycleManager.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER, null);
		ejbController.setEjb(AccountLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE, null);
		ejbController.setEjb(ProductEquipmentHelper.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER, null);
		ejbController.setEjb(ReferenceDataHelper.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER, null);
		ejbController.setEjb(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE, null);
		ejbController.setEjb(SubscriberLifecycleHelper.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER, subscriberLifecycleHelper);
	}
	
	private void initializeChangeContext (BaseChangeContext<?> context, String sessionId) throws ApplicationException {
		context.setEjbController(ejbController);
		context.setSessionId(this, sessionId);

		if (sessionId != null) {
			context.setClientIdentity(getClientIdentity(sessionId));
		}

		context.initialize();
	}
	
	public void setAmdocsSessionManager(AmdocsSessionManager amdocsSessionManager) {
		this.amdocsSessionManager = amdocsSessionManager;
	}

	public void setSubscriberDaoFactory(SubscriberDaoFactory subscriberDaoFactory) {
		this.subscriberDaoFactory = subscriberDaoFactory;
	}

	public void setFleetDao(FleetDao fleetDao) {
		this.fleetDao = fleetDao;
	}

	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}
	
	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}
	
	public void setServiceAgreementDao(ServiceAgreementDao serviceAgreementDao) {
		this.serviceAgreementDao = serviceAgreementDao;
	}

	public void setDepositDao(DepositDao depositDao) {
		this.depositDao = depositDao;
	}

	private ProductEquipmentHelper getProductEquipmentHelper() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentHelper.class);
	}

	private ReferenceDataHelper getReferenceDataHelper() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataHelper.class);
	}

	private ReferenceDataFacade getReferenceDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	private AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}

	private AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}
	
	private SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleHelper.class);
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(userId, password, applicationId);
		return amdocsSessionManager.openSession(identity);
	}

	@Override
	public void activateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException {
			orderServiceDao.activateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfo);
	}
	
	@Override
	public void updateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException{
		
		ServiceAgreementInfo[] features = getSubscriberLifecycleHelper().retrieveFeaturesForPrepaidSubscriber(phoneNumber);
		ServiceAgreementInfo existingServiceAgreementInfo = null;

		LOGGER.debug("Method: updateFeatureForPrepaidSubscriber For PhoneNumber: " + phoneNumber);
		for (int i = 0; i < features.length; i++) {
			if (features[i].getServiceCode().trim().equals(serviceAgreementInfo.getServiceCode().trim())) {
				existingServiceAgreementInfo = features[i];
				break;
			}
		}

		if (existingServiceAgreementInfo != null) {
			subscriptionServiceDao.updateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfo, existingServiceAgreementInfo);
		}
	}
	
	@Override
	public void deactivateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException {
		LOGGER.debug("Method: deactivateFeatureForPrepaidSubscriber For PhoneNumber: " + phoneNumber);
		subscriptionServiceDao.deactivateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfo);
	}

	

	

	@Override
	public void changeFeaturesForPrepaidSubscriber(String userId,
			String phoneNumber,
			ServiceAgreementInfo[] serviceAgreementInfoArray)
	throws ApplicationException {
		
		LOGGER.debug("Method: changeFeaturesForPrepaidSubscriber For PhoneNumber: " + phoneNumber);
			for (int i = 0; i < serviceAgreementInfoArray.length; i++) {
				if (serviceAgreementInfoArray[i].isWPS() && (serviceAgreementInfoArray[i].getTransaction() == BaseAgreementInfo.ADD))
					activateFeatureForPrepaidSubscriber(phoneNumber, serviceAgreementInfoArray[i]);
				if (serviceAgreementInfoArray[i].isWPS() && (serviceAgreementInfoArray[i].getTransaction() == BaseAgreementInfo.UPDATE))
					updateFeatureForPrepaidSubscriber(phoneNumber,serviceAgreementInfoArray[i]);
				if (serviceAgreementInfoArray[i].isWPS() && (serviceAgreementInfoArray[i].getTransaction() == BaseAgreementInfo.DELETE))
					deactivateFeatureForPrepaidSubscriber(phoneNumber,serviceAgreementInfoArray[i]);
		} 
	}
	
	

	@Override
	public void saveActivationFeaturesPurchaseArrangement(
			SubscriberInfo subscriber,
			ActivationFeaturesPurchaseArrangementInfo[] featuresPurchaseList,
			String user) throws ApplicationException {
			prepaidWirelessCustomerOrderServiceDao.saveActivationFeaturesPurchaseArrangement(subscriber, featuresPurchaseList, user);
	}

	@Override
	public void updatePrepaidCallingCircleParameters(String applicationId,
			String userId, String phoneNumber,
			ServiceAgreementInfo serviceAgreement, byte action)
			throws ApplicationException {
			subscriptionServiceDao.updateCallingCircleParameters(applicationId,
					userId, phoneNumber, serviceAgreement, action);
	}

	@Override
	public void saveSubscriptionPreference(
			SubscriptionPreferenceInfo preferenceInfo, String user) {
		subscriberDao.saveSubscriptionPreference(preferenceInfo, user);		
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#adjustCall(int, java.lang.String, java.lang.String, int, java.util.Date, java.lang.String, double, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void adjustCall(int ban, String subscriberId, String productType, int billSeqNo, Date channelSeizureDate, 
			String messageSwitchId, double adjustmentAmount, String adjustmentReasonCode, String memoText, 
			String usageProductType, String sessionId) 	throws ApplicationException {

		usageDao.adjustCall(ban, subscriberId, productType, billSeqNo, channelSeizureDate, messageSwitchId, adjustmentAmount, adjustmentReasonCode, memoText, usageProductType, sessionId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#getUsageProfileListsSummary(int, java.lang.String, int, java.lang.String)
	 */
	@Override
	public UsageProfileListsSummaryInfo getUsageProfileListsSummary(int ban, String subscriberId, int billSeqNo, String productType, String sessionId) throws ApplicationException {

		UsageProfileListsSummaryInfo result = new UsageProfileListsSummaryInfo();

		result.setUsageProfileList(usageDao.retrieveUsageProfileList(ban, subscriberId, billSeqNo, productType, sessionId));
		result.setUsageProfileAdditionalChargesList(usageDao.retrieveUsageProfileAdditionalChargesList(ban, subscriberId, billSeqNo, productType, sessionId));

		return result;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#retrieveBilledCallsList(int, java.lang.String, java.lang.String, int, char, java.util.Date, java.util.Date, boolean, java.lang.String)
	 */
	@Override
	public CallListInfo retrieveBilledCallsList(int ban, String subscriberId, String productType, int billSeqNo, 
			char callType, Date fromDate, Date toDate, boolean getAll, String sessionId) throws ApplicationException {

		return usageDao.retrieveBilledCallsList(ban, subscriberId, productType, billSeqNo, callType, fromDate, toDate, getAll, sessionId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#retrieveCallDetails(int, java.lang.String, java.lang.String, int, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CallInfo retrieveCallDetails(int ban, String subscriberId, String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId, String callProductType, String sessionId) throws ApplicationException {

		return usageDao.retrieveCallDetails(ban, subscriberId, productType, billSeqNo, channelSeizureDate, messageSwitchId, callProductType, sessionId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#retrieveUnbilledCallsList(int, java.lang.String, java.lang.String, java.util.Date, java.util.Date, boolean, java.lang.String)
	 */
	@Override
	public CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId, String productType, Date fromDate, 
			Date toDate, boolean getAll, String sessionId) throws ApplicationException {

		return usageDao.retrieveUnbilledCallsList(ban, subscriberId, productType, fromDate, toDate, getAll, sessionId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifeCycleManager#retrieveUnbilledCallsList(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException {
		return retrieveUnbilledCallsList(ban, subscriberId, productType, null, null, false, sessionId);	
	}

	@Override
	public 	void activateReservedSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo
			,Date startServiceDate, String activityReasonCode, ServicesValidation srvValidation
			,String portProcessType, int oldBanId, String oldSubscriberId, String sessionId)throws ApplicationException {
		
		subscriberDao.activateReservedSubscriber(subscriberInfo.getBanId(), 
				subscriberInfo.getSubscriberId(), startServiceDate, 
				activityReasonCode, subscriberInfo.getDealerCode(), 
				subscriberInfo.getSalesRepId(),srvValidation,
				portProcessType, oldBanId, oldSubscriberId, subscriberInfo.getProductType(), sessionId);
	}

	@Override
	public DiscountInfo[] retrieveDiscounts(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException {
		return subscriberDao.retrieveDiscounts(ban, subscriberId, productType, sessionId);
	}

	@Override
	public CancellationPenaltyInfo retrieveCancellationPenalty(int ban,
			String subscriberId, String productType, String sessionId)
	throws ApplicationException {
		return subscriberDao.retrieveCancellationPenalty(ban, subscriberId, productType, sessionId);
	}

	@Override
	public AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban,
			String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation, int maxNumbers,
			String sessionId) throws ApplicationException {
		AvailablePhoneNumberInfo[] availablePhoneNumbers = null;
		String[] phoneNumbers = null;

		if (phoneNumberReservation.getProductType() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, "phoneNumberReservation.getProductType() should not be null","");
		}
		if (phoneNumberReservation.getNumberGroup() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, "phoneNumberReservation.getNumberGroup() should not be null","");
		}
		if (phoneNumberReservation.getPhoneNumberPattern() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, "PhoneNumberReservation.getPhoneNumberPattern() should not be null.","");
		}
		if (subscriberId == null || subscriberId.trim().equals("")) {
			NewSubscriberDao newSubscriberDao = subscriberDaoFactory.getNewSubscriberDao(phoneNumberReservation.getProductType());
			phoneNumbers =  newSubscriberDao.retrieveAvailablePhoneNumbers(ban, phoneNumberReservation, maxNumbers, sessionId);
		} else {
			UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(phoneNumberReservation.getProductType());
			phoneNumbers =  updateSubscriberDao.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		}

		availablePhoneNumbers = new AvailablePhoneNumberInfo[phoneNumbers.length];
		for (int i = 0; i < phoneNumbers.length; i ++) {
			availablePhoneNumbers[i] = new AvailablePhoneNumberInfo();
			availablePhoneNumbers[i].setNumberGroup(phoneNumberReservation.getNumberGroup0());
			availablePhoneNumbers[i].setNumberLocationCode(phoneNumberReservation.getNumberGroup().getNumberLocation());
			availablePhoneNumbers[i].setPhoneNumber(phoneNumbers[i]);
		}
		
		return availablePhoneNumbers;
	}

	@Override
	public AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban, String subscriberId, NumberGroupInfo numberGroup,
			String productType, String phoneNumberPattern, boolean asianInd, 
			int maxNumbers, String sessionId) throws ApplicationException {
		
		PhoneNumberReservationInfo reservation = new PhoneNumberReservationInfo();
		reservation.setPhoneNumberPattern(phoneNumberPattern);
		reservation.setWaiveSearchFee(true);
		reservation.setProductType(productType);
		reservation.setNumberGroup(numberGroup);
		reservation.setAsian(asianInd);
		reservation.setLikeMatch(false);
		
		return retrieveAvailablePhoneNumbers(ban, subscriberId, reservation, maxNumbers, sessionId);
	}

	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, boolean activate,
			boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn,
			ServicesValidation srvValidation, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		// A permanent fix for defect PROD00119999 has been made to throw a specific exception outlining the steps
		// skipped as a result of throwing an exception - it is only thrown if the subscriber
		// was successfully created. This allows front-end applications to decide whether
		// to complete their processing logic.
		// Note that the existing error handling is complex when an exception occurs creating a subscriber in Knowbility.
		// TelusExceptions are allowed to propagate up the call stack, bypassing the memo creation, fee application, and
		// discount application steps. All other exceptions are delayed while an attempt is made to complete those steps.
		// If an exception occurs in those steps it is thrown; otherwise the original exception is wrapped in a TelusException
		// and thrown when all processing steps have been attempted. The permanent fix attempts to preserve this behaviour.
		// (We believe the behaviour exists to allow the post-creation steps to complete when we fail to activate
		// a reserved subscriber)

		NewSubscriberDao newSubscriberDao = subscriberDaoFactory.getNewSubscriberDao(subscriberInfo.getProductType());
		newSubscriberDao.createSubscriber(subscriberInfo,
					subscriberContractInfo,
					activate,
					dealerHasDeposit, portedIn,
					srvValidation,
					portProcessType, oldBanId, oldSubscriberId, sessionId);
	}

	@Override
	public void changePhoneNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String sessionId)
	throws ApplicationException {
		UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(subscriberInfo.getProductType());
		updateSubscriberDao.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);		
	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String sessionId)
	throws ApplicationException {
		this.moveSubscriber(subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, null, null, sessionId);
	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan,
			Date activityDate, boolean transferOwnership,
			String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(subscriberInfo.getProductType());

		FleetInfo fleetInfo = null;

		// For IDEN subscribers only - Get Fleet information (needed for determining whether subscriber is on private/public fleet)
		if (subscriberInfo.isIDEN()) {
			try {
				NumberGroupInfo numberGroupInfo = new NumberGroupInfo();
				// get/set NumberGroup from phone # (needed to reserve additional phone #s - msisdn)
				LOGGER.debug("Calling retrieveNumberGroupByPhoneNumberProductType... with phone#=[" + subscriberInfo.getPhoneNumber() + 
						"] + productType=[" + subscriberInfo.getProductType() + "]");
				numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPhoneNumberProductType(subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
				subscriberInfo.setNumberGroup(numberGroupInfo);

				if (((IDENSubscriberInfo)subscriberInfo).getFleetIdentity().getUrbanId() != 0) {
					LOGGER.debug("Calling retrieveFleetByFleetIdentity... ");
					fleetInfo = getReferenceDataHelper().retrieveFleetByFleetIdentity(((IDENSubscriberInfo)subscriberInfo).getFleetIdentity());
				}

				((UpdateIdenSubscriberDao)updateSubscriberDao).moveSubscriber(subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, fleetInfo, sessionId);
			} catch (TelusException e) {
				ApplicationException rootException = new ApplicationException(SystemCodes.CMB_SLM_EJB, e.getMessage(), "", e);
				throw new SystemException(SystemCodes.CMB_SLM_EJB, "Exception encountered while calling referenceDataHelper", "", rootException);
			}
		} else {
			updateSubscriberDao.moveSubscriber(subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);
		}
		
		
	}

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		NewSubscriberDao newSubscriberDao = subscriberDaoFactory.getNewSubscriberDao(subscriberInfo.getProductType());
		newSubscriberDao.releaseSubscriber(subscriberInfo, sessionId);		
	}

	@Override
	public SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, String sessionId)
	throws ApplicationException {
		NewSubscriberDao newSubscriberDao = subscriberDaoFactory.getNewSubscriberDao(subscriberInfo.getProductType());
		return newSubscriberDao.reserveLikePhoneNumber(subscriberInfo, phoneNumberReservation, sessionId);		
	}

	@Override
	public SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation ,String sessionId)
	throws ApplicationException {
		NewSubscriberDao newSubscriberDao = subscriberDaoFactory.getNewSubscriberDao(subscriberInfo.getProductType());
		return newSubscriberDao.reservePhoneNumber(subscriberInfo, phoneNumberReservation, isOfflineReservation,sessionId);	
	}

	@Override
	public void cancelAdditionalMsisdn(
			AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo,
			String additionalMsisdn, String sessionId) throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();

		idenSubscriberDao.cancelAdditionalMsisdn(additionalMsiSdnFtrInfo, additionalMsisdn, sessionId);
	}

	@Override
	public void cancelPortedInSubscriber(
			int banNumber, 
			String phoneNumber,
			String deactivationReason, 
			Date activityDate, 
			String portOutInd,
			boolean isBrandPort, 
			String sessionId) throws ApplicationException {
		
		SubscriberInfo subscriberInfo = this.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber);		
		UpdateIdenPcsSubscriberDao updateIdenPcsSubscriberDao = subscriberDaoFactory.getUpdateIdenPcsSubscriberDao(subscriberInfo.getProductType());
		try {
			updateIdenPcsSubscriberDao.cancelPortedInSubscriber(banNumber, subscriberInfo.getSubscriberId(), deactivationReason, activityDate, portOutInd, isBrandPort, sessionId); 
		} catch (ApplicationException exception) {
			if (Utility.isConcurrentUpdateError(exception, banNumber)) {
				String methodName = "cancelPortedInSubscriber";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") The error is concurrent update error, now retry the transaction ...");
				updateIdenPcsSubscriberDao.cancelPortedInSubscriber(banNumber, subscriberInfo.getSubscriberId()
						, deactivationReason, activityDate, portOutInd, isBrandPort, sessionId);
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") retry succeeded, Leaving...");
				return;
			} else {
				throw exception;
			}
		}
	}

	@Override
	public void changeAdditionalPhoneNumbers(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		changeAdditionalPhoneNumbers(subscriberInfo, sessionId, false);
	}	

	@Override
	public void changeAdditionalPhoneNumbersForPortIn(
			SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		changeAdditionalPhoneNumbers(subscriberInfo, sessionId, true);		
	}

	private void changeAdditionalPhoneNumbers(SubscriberInfo subscriberInfo,
			String sessionId, boolean portIn) throws ApplicationException {
		NumberGroupInfo numberGroup;
		try {
			numberGroup = getReferenceDataHelper().retrieveNumberGroupByPhoneNumberProductType(subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLM_EJB, "ReferenceDataHelper throwing exception.", "", e);		
		}

		subscriberDaoFactory.getUpdateIdenSubscriberDao().changeAdditionalPhoneNumbers(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId()
				, subscriberInfo.getPhoneNumber(), numberGroup, portIn, sessionId);

	}

	@Override
	public void changeFaxNumber(SubscriberInfo subscriber, String sessionId)
	throws ApplicationException {

		AvailablePhoneNumberInfo[] availableNumbers = null;
		AvailablePhoneNumberInfo newFaxNumber = null;

		String[] npaNxxList;
		try {
			npaNxxList = getReferenceDataHelper().retrieveNpaNxxForMsisdnReservation(subscriber.getPhoneNumber(), (subscriber.getPortType() != null && subscriber.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN) ? true : false));
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLM_EJB, "Reference DataHelperException occurred.", "", e);
		}
		if (npaNxxList == null || npaNxxList.length == 0) {
			LOGGER.error("TelusApplicationException occurred (No fax numbers available matching criteria)");
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
					, "No fax numbers available matching criteria", "");
		}

		for (int i = 0; i < npaNxxList.length; i++) {
			PhoneNumberReservationInfo reservation = new PhoneNumberReservationInfo();
			reservation.setAsian(false);
			reservation.setLikeMatch(false);
			reservation.setNumberGroup(subscriber.getNumberGroup());
			reservation.setProductType(subscriber.getProductType());
			reservation.setWaiveSearchFee(true);
			reservation.setPhoneNumberPattern(npaNxxList[i] + "****");

			availableNumbers = retrieveAvailablePhoneNumbers(subscriber.getBanId(), subscriber.getSubscriberId(), reservation, 1, sessionId);
			if (availableNumbers != null && availableNumbers.length > 0) {
				newFaxNumber = availableNumbers[0];
				break;
			}
		}

		if (newFaxNumber == null) {
			LOGGER.error("TelusApplicationException occurred (No fax numbers available matching criteria)");
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS
					, "No fax numbers available matching criteria", "");
		}

		changeFaxNumber(subscriber, newFaxNumber, sessionId);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void changeFaxNumber(SubscriberInfo subscriber,
			AvailablePhoneNumberInfo newFaxNumber, String sessionId) throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();

		idenSubscriberDao.changeFaxNumber(subscriber.getBanId(), subscriber.getSubscriberId(), newFaxNumber.getPhoneNumber(),
				newFaxNumber.getNumberGroup0()
				, (subscriber.getPortType() != null && subscriber.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN)) ? true : false
				, sessionId);
	}

	@Override
	public void changeIMSI(int ban, String subscriberId,
			String sessionId) throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();
		idenSubscriberDao.changeIMSI(ban, subscriberId, sessionId);		
	}

	@Override
	public void changeIP(int ban, String subscriberId, 
			String newIp, String newIpType, String newIpCorpCode, String sessionId)
			throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();
		idenSubscriberDao.changeIP(ban, subscriberId, newIp, newIpType, newIpCorpCode, sessionId);
		
	}

	@Override
	public void reserveAdditionalPhoneNumber(int ban, String subscriberId,
			AvailablePhoneNumberInfo additionalPhoneNumber, String sessionId)
			throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();
		idenSubscriberDao.reserveAdditionalPhoneNumber(ban, subscriberId, additionalPhoneNumber.getNumberGroup0()
				, additionalPhoneNumber.getPhoneNumber(), sessionId);
		
	}

	@Override
	public SearchResultByMsiSdn searchSubscriberByAdditionalMsiSdn(
			String additionalMsisdn, String sessionId)
			throws ApplicationException {
		UpdateIdenSubscriberDao idenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();
		return idenSubscriberDao.searchSubscriberByAdditionalMsiSdn(additionalMsisdn, sessionId);
	}

	@Override
	public void migrateSubscriber(SubscriberInfo srcSubscriberInfo,
			SubscriberInfo newSubscriberInfo, Date activityDate,
			SubscriberContractInfo subscriberContractInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			MigrationRequestInfo migrationRequestInfo, String sessionId)
			throws ApplicationException {
		
		if ( migrationRequestInfo.isM2P()) {
			NewIdenPcsSubscriberDao newSubscriberDAO = subscriberDaoFactory.getNewIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_PCS);
			newSubscriberDAO.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, activityDate, subscriberContractInfo
					, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, migrationRequestInfo, sessionId);
		} else if (migrationRequestInfo.isP2M()) { //M2P migration
			NewIdenPcsSubscriberDao newSubscriberDAO = subscriberDaoFactory.getNewIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_IDEN);
			newSubscriberDAO.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, activityDate, subscriberContractInfo
					, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, migrationRequestInfo, sessionId);
		} else { //P2P migration
			UpdatePcsSubscriberDao subscriberDAO = subscriberDaoFactory.getUpdatePcsSubscriberDao();
			subscriberDAO.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo.getBanId(), activityDate
					, subscriberContractInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, migrationRequestInfo, sessionId);
		}
	}

	@Override
	public void setSubscriberPortIndicator(String phoneNumber, String sessionId)
			throws ApplicationException {
		try {
			setSubscriberPortIndicator(phoneNumber, getReferenceDataHelper().retrieveLogicalDate(), sessionId);
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLM_EJB, "Reference data helper threw exception.", "", e);
		}
	}

	@Override
	public void setSubscriberPortIndicator(String phoneNumber, Date portDate,
			String sessionId) throws ApplicationException {
		SubscriberInfo subscriberInfo = this.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber);		
		UpdateIdenPcsSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateIdenPcsSubscriberDao(subscriberInfo.getProductType());		
		updateSubscriberDao.setPortTypeToPortIn(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), portDate, sessionId);
	}

	@Override
	public void snapBack(String phoneNumber, String sessionId)
			throws ApplicationException {		
		UpdateIdenPcsSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateIdenPcsSubscriberDao(Subscriber.PRODUCT_TYPE_PCS);		
		updateSubscriberDao.setPortTypeToSnapback(phoneNumber, sessionId);
	}

	@Override
	public void sendTestPage(int ban, String subscriberId, String sessionId)
			throws ApplicationException {
//		UpdatePagerSubscriberDao updatePagerSubscriberDao = subscriberDaoFactory.getUpdatePagerSubscriberDao();
//		updatePagerSubscriberDao.sendTestPage(ban, subscriberId, sessionId);		
	}

	@Override
	public void portChangeSubscriberNumber(SubscriberInfo subscriberInfo,
			AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		UpdateIdenPcsSubscriberDao updateIdenPcsSubscriberDao = subscriberDaoFactory.getUpdateIdenPcsSubscriberDao(subscriberInfo.getProductType());		
		updateIdenPcsSubscriberDao.portChangeSubscriberNumber(subscriberInfo, newPhoneNumber, reasonCode
				, dealerCode, salesRepCode, portProcessType, oldBanId, oldSubscriberId, sessionId);
		
	}

	@Override
	public void releasePortedInSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		if (subscriberInfo != null) {
			NewIdenPcsSubscriberDao newIdenPcsSubscriberDao = subscriberDaoFactory.getNewIdenPcsSubscriberDao(subscriberInfo.getProductType());
			newIdenPcsSubscriberDao.releasePortedInSubscriber(subscriberInfo, sessionId);
		} else {
			if (sessionId != null) {
				ClientIdentity ci = getClientIdentity(sessionId);
				LOGGER.error("SubscriberInfo is null in app=["+ci.getApplication()+"]");
			}
		}
	}

	@Override
	public void suspendPortedInSubscriber(int banNumber, String phoneNumber,
			String deactivationReason, Date activityDate, String portOutInd, String sessionId)
			throws ApplicationException {
		SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(phoneNumber);		
		UpdateIdenPcsSubscriberDao updateIdenPcsSubscriberDao = subscriberDaoFactory.getUpdateIdenPcsSubscriberDao(subscriberInfo.getProductType());
		updateIdenPcsSubscriberDao.suspendPortedInSubscriber(banNumber, subscriberInfo.getSubscriberId()
				, deactivationReason, activityDate, portOutInd, sessionId);
	}

	@Override
	public SubscriberInfo reservePortedInPhoneNumber(
			SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly, String sessionId) throws ApplicationException {
		if (!subscriberInfo.getProductType().equals(Subscriber.PRODUCT_TYPE_PCS)) {
			throw new ApplicationException(SystemCodes.CMB_SLH_EJB, "Product type not supported. " + subscriberInfo.getProductType(), "");
		}
		
		NewPcsSubscriberDao newPcsSubscriberDao = subscriberDaoFactory.getNewPcsSubscriberDao();
		
		//Production PROD00197532 fix: the following try-catch block was implemented in ECA SubscriberManagerEJB.reservePortedInPhoneNumber,
		//but is missed in this EJB. 
		try {
			return newPcsSubscriberDao.reservePortedInPhoneNumber(subscriberInfo, phoneNumberReservation, reserveNumberOnly, sessionId);
		} catch (ApplicationException orginalAE) { 
			
			return retrievePartiallyReservedSubscriber(subscriberInfo, phoneNumberReservation, orginalAE, sessionId);
		}
	}

	@Override
	public SubscriberInfo reservePortedInPhoneNumberForIden(
			SubscriberInfo subscriberInfo,
			PhoneNumberReservationInfo phoneNumberReservation,
			boolean reserveNumberOnly, boolean reserveUfmi, boolean ptnBased,
			byte ufmiReserveMethod, int urbanId, int fleetId, int memberId,
			AvailablePhoneNumber availPhoneNumber, String sessionId) throws ApplicationException {
		throw new UnsupportedOperationException("reservePortedInPhoneNumberForIden");
	}

	private SubscriberInfo retrievePartiallyReservedSubscriber(	SubscriberInfo subscriberInfo,	PhoneNumberReservation phoneNumberReservation,
			ApplicationException originalReserverPortInException, String sessionId )	throws ApplicationException {
		
		if (ErrorCodes.SUBSCRIBER_NO_AVAILABLE_PHONENUMBERS.equals( originalReserverPortInException.getErrorCode()) ) {
			//this is a validation error thrown by our own DAO, in which case the phone number is missing, no point to call  
			//retrievePartiallyReservedSubscriber
			throw originalReserverPortInException;
		}
		
		String phoneNumber = phoneNumberReservation.getPhoneNumberPattern();
		
		LOGGER.warn("Encounted exception while reserving port-in phone number [" + phoneNumber + "] for ban["+ subscriberInfo.getBanId()+ "]", originalReserverPortInException );
		LOGGER.warn("Now try to retirevel partially reserved subscriber" );
		
		String subscriberId = getSubscriberLifecycleHelper().retrieveSubscriberIDByPhoneNumber(subscriberInfo.getBanId(), phoneNumber);
		if (subscriberId != null) {			
			if (subscriberInfo.isIDEN()) {
				IDENSubscriberInfo idenSubscriberInfo = new IDENSubscriberInfo();
				//first preserve all information from subcribeInfo
				idenSubscriberInfo.copyFrom(subscriberInfo);

				IDENSubscriberInfo partialReservedSubInfo = subscriberDaoFactory.getNewIdenSubscriberDao().retrievePartiallyReservedSubscriber(subscriberInfo.getBanId(), subscriberId, sessionId);
				
				//copy info from partialReservedSubInfo
				idenSubscriberInfo.setSubscriberId(partialReservedSubInfo.getSubscriberId());
				idenSubscriberInfo.setPhoneNumber( partialReservedSubInfo.getPhoneNumber() );
				idenSubscriberInfo.setIMSI( partialReservedSubInfo.getIMSI() );
				idenSubscriberInfo.setIPAddress(partialReservedSubInfo.getIPAddress());
				
				//last copy everything back to subscriberInfo
				subscriberInfo.copyFrom(idenSubscriberInfo);
				
			} else { //PCS
				SubscriberInfo partialReservedSubInfo = subscriberDaoFactory.getNewPcsSubscriberDao().retrievePartiallyReservedSubscriber(subscriberInfo.getBanId(), subscriberId, sessionId);
				subscriberInfo.setSubscriberId( partialReservedSubInfo.getSubscriberId() );
				subscriberInfo.setPhoneNumber( partialReservedSubInfo.getPhoneNumber() );
			}
			
			return subscriberInfo;
			
		} else {
			//try to see if phone number is reserved on another ban
			SubscriberInfo tmpSubInfo = getSubscriberLifecycleHelper().retrieveBanForPartiallyReservedSub( phoneNumber ); 
			
			int ban = tmpSubInfo.getBanId();
			char status = tmpSubInfo.getStatus();
			if (ban == 0 || status != Subscriber.STATUS_RESERVED)
				throw originalReserverPortInException;
			else
				throw new ApplicationException(SystemCodes.CMB_SLM_DAO, "Phone Number [" + phoneNumber + "] belongs to different BAN [" + ban + "]" , "");
		}
	}

	@Override
	public void updateAddress(int ban, String subscriber,String productType, 
			AddressInfo addressInfo, String sessionId)
			throws ApplicationException {
	
		addressDao.updateAddress(ban, subscriber, productType,addressInfo, sessionId);
	}
	
	@Override
	public void changePricePlan(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
		
		String methodName = "changePricePlan";
		NumberGroupInfo numberGroupInfo = new NumberGroupInfo();
		try {
			if (subscriberInfo.getPortType() != null && subscriberInfo.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN)) {
				LOGGER.debug("("+getClass().getName()+"."+ methodName +
						") Calling retrieveNumberGroupByPortedInPhoneNumberProductType... with phone#=[" 
						+ subscriberInfo.getPhoneNumber() + "] + productType=[" + subscriberInfo.getProductType() + "]");
				numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPortedInPhoneNumberProductType(
							subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
			} else {
				LOGGER.debug("("+getClass().getName()+"."+ methodName +
						") Calling retrieveNumberGroupByPhoneNumberProductType... with phone#=[" 
						+ subscriberInfo.getPhoneNumber() + "] + productType=[" + subscriberInfo.getProductType() + "]");
				numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPhoneNumberProductType(
						subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
			}
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, "TelusException occured while getting NumberGroupInfo : " + e.getMessage(), "");
		}
		subscriberInfo.setNumberGroup(numberGroupInfo);
		
		serviceAgreementDao.changePricePlan(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
	}

	@Override
	public void changeServiceAgreement(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			String dealerCode,
			String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException {
	
		NumberGroupInfo numberGroupInfo = new NumberGroupInfo();
		try {
			if (subscriberInfo.getPortType() != null && subscriberInfo.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN)) {
				numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPortedInPhoneNumberProductType(
						subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
				subscriberInfo.setNumberGroup(numberGroupInfo);
			} else {
				numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPhoneNumberProductType(subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
			}
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLM_EJB, "TelusException occured while getting NumberGroupInfo : " + e.getMessage(), "");
		}

		ClientIdentity identity = getClientIdentity(sessionId);
		changeFeaturesForPrepaidSubscriber(identity.getPrincipal(), subscriberInfo.getSubscriberId(), subscriberContractInfo.getServices0(true));
		serviceAgreementDao.changeServiceAgreement(subscriberInfo, subscriberContractInfo, numberGroupInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
	}
	
	@Override
	public void addMemberIdentity(IDENSubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, int urbanId, int fleetId, String memberId,
			boolean pricePlanChange, String sessionId)
			throws ApplicationException {
		fleetDao.addMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, urbanId, fleetId, memberId, pricePlanChange, sessionId);
	}

	@Override
	public void changeMemberId(IDENSubscriberInfo idenSubscriberInfo,
			String newMemberId, String sessionId) throws ApplicationException {
		fleetDao.changeMemberId(idenSubscriberInfo, newMemberId, sessionId);		
	}

	@Override
	public void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo,
			int newUrbanId, int newFleetId, String newMemberId, String sessionId)
			throws ApplicationException {
		fleetDao.changeMemberIdentity(idenSubscriberInfo, newUrbanId, newFleetId, newMemberId, sessionId);
	}

	@Override
	public void changeTalkGroups(IDENSubscriberInfo idenSubscriberInfo,
			TalkGroupInfo[] addedTalkGroups, TalkGroupInfo[] removedTalkGroups, String sessionId)
			throws ApplicationException {
		fleetDao.changeTalkGroups(idenSubscriberInfo, addedTalkGroups, removedTalkGroups, sessionId);
	}

	@Override
	public int[] getAvailableMemberIDs(int urbanId, int fleetId,
			String memberIdPattern, int max, String sessionId)
			throws ApplicationException {
		return fleetDao.retrieveAvailableMemberIDs(urbanId, fleetId, memberIdPattern, max, sessionId);
	}

	@Override
	public void removeMemberIdentity(IDENSubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo, String dealerCode,
			String salesRepCode, boolean pricePlanChange, String sessionId)
			throws ApplicationException {
		fleetDao.removeMemberIdentity(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanChange, sessionId);
	}

	@Override
	public IDENSubscriberInfo reserveMemberId(
			IDENSubscriberInfo idenSubscriberInfo,
			FleetIdentityInfo fleetIdentityInfo, String wildCard,
			String sessionId) throws ApplicationException {
		boolean isPtnBasedFleet;
		int npa = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3));
		int nxx = Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6));

		isPtnBasedFleet = fleetIdentityInfo.getUrbanId() == npa && fleetIdentityInfo.getFleetId() == nxx;
//		if (!isPtnBasedFleet) {
//			associateFleetAndTGsToBan(idenSubscriberInfo, new com.telus.eas.account.info.TalkGroupInfo[0], sessionId);
//		}
		return fleetDao.reserveMemberId(idenSubscriberInfo, fleetIdentityInfo, wildCard,isPtnBasedFleet, sessionId);
	}

	@Override
	public IDENSubscriberInfo reserveMemberId(
			IDENSubscriberInfo idenSubscriberInfo, String sessionId)
			throws ApplicationException {
		return reserveMemberId(idenSubscriberInfo, new FleetIdentityInfo(
				Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(0, 3)),
				Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(3, 6))),
				String.valueOf(Integer.parseInt(idenSubscriberInfo.getPhoneNumber().substring(6))), sessionId);
	}

	@Override
	public String[] retrieveAvailableMemberIds(int urbanId, int fleetId,
			String memberIdPattern, int maxMemberIds, String sessionId)
			throws ApplicationException {
		return fleetDao.retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId);
	}

	@Override
	public Collection<TalkGroupInfo> retrieveTalkGroupsBySubscriber(int ban,
			String subscriberId, String sessionId)
			throws ApplicationException {
		return fleetDao.retrieveTalkGroupsBySubscriber(ban, subscriberId, sessionId);
	}	
	
	@Override
	public void deleteFutureDatedPricePlan(int ban, String subscriberId,
			String productType, String sessionId) throws ApplicationException {
		
		serviceAgreementDao.deleteFutureDatedPricePlan(ban, subscriberId, productType, sessionId);
	}

	@Override
	public void resetVoiceMailPassword(int ban, String subscriberId,
			String productType, String sessionId) throws ApplicationException {
		String[] voiceMailSocAndFeature = getSubscriberLifecycleHelper().retrieveVoiceMailFeatureByPhoneNumber(subscriberId, productType);

		// return if no voice mail
		if (voiceMailSocAndFeature.length < 2) return;		
		
		UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(productType);		
		updateSubscriberDao.resetVoiceMailPassword(ban, subscriberId, voiceMailSocAndFeature, sessionId);		
	}

	@Override
	public void deleteMsisdnFeature(AdditionalMsiSdnFtrInfo ftrInfo,
			String sessionId) throws ApplicationException {
		UpdateIdenSubscriberDao updateIdenSubscriberDao = subscriberDaoFactory.getUpdateIdenSubscriberDao();		
		updateIdenSubscriberDao.deleteMsisdnFeature(ftrInfo, sessionId);
	}

	@Override
	public void updateBirthDate(SubscriberInfo subscriberInfo, String sessionId)
			throws ApplicationException {
		UpdatePcsSubscriberDao updatePcsSubscriberDao = subscriberDaoFactory.getUpdatePcsSubscriberDao();
		updatePcsSubscriberDao.updateBirthDate(subscriberInfo, sessionId);
	}

	@Override
	public void changeEquipment(SubscriberInfo subscriberInfo,
			EquipmentInfo oldPrimaryEquipmentInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo, String dealerCode,
			String salesRepCode, String requestorId, String swapType,
			SubscriberContractInfo subscriberContractInfo,
			PricePlanValidationInfo pricePlanValidation,
			String sessionId)
			throws ApplicationException {
		// if price plan changing, get/set NumberGroup from phone # (needed to reserve additional phone #s - msisdn)
		if (subscriberContractInfo != null) {
			NumberGroupInfo numberGroupInfo;
			try {
				numberGroupInfo = this.getReferenceDataHelper().retrieveNumberGroupByPhoneNumberProductType(subscriberInfo.getPhoneNumber(), subscriberInfo.getProductType());
			} catch (TelusException e) {				
				throw new SystemException(SystemCodes.CMB_SLM_EJB, "TelusException occurred when calling reference data helper.", "", e);
			}
			subscriberInfo.setNumberGroup(numberGroupInfo);
		}
		
		/** New to CMB code. Didn't exist in ECA. SubscriberInfo that's passed in may not have the proper equipmentInfo object stored
		 * due to decoupling of the layers. 
		 */
		subscriberInfo.setEquipment(oldPrimaryEquipmentInfo);
		UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(subscriberInfo.getProductType());		
		updateSubscriberDao.changeSerialNumberAndMaybePricePlan(subscriberInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
		
		if (newPrimaryEquipmentInfo.isIDEN()) {
			changeIMSI(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), sessionId);
		}
	}

	@Override
	public CallingCircleParameters retrieveCallingCircleParameters(int banId,
			String subscriberNo, String soc, String featureCode,
			String productType, String sessionId) throws ApplicationException {
	
		return serviceAgreementDao.retrieveCallingCircleParameters(banId, subscriberNo, soc, featureCode, productType, sessionId);
	}

	@Override
	public void updateCommitment(SubscriberInfo pSubscriberInfo,
			CommitmentInfo pCommitmentInfo, String dealerCode,
			String salesRepCode, String sessionId) throws ApplicationException {
		serviceAgreementDao.updateCommitment(pSubscriberInfo, pCommitmentInfo, dealerCode, salesRepCode, sessionId);
	}

	@Override
	public void suspendSubscriber(SubscriberInfo subscriberInfo,
			Date activityDate, String activityReasonCode, String userMemoText
			, String sessionId)
			throws ApplicationException {
		SubscriberDao subscriberDao = subscriberDaoFactory.getSubscriberDao();
		subscriberDao.suspendSubscriber(subscriberInfo, activityDate, activityReasonCode, userMemoText, sessionId);		
	}
	
	@Override
	public void createDeposit(SubscriberInfo pSubscriberInfo, double amount,
			String memoText, String sessionId) throws ApplicationException {
		depositDao.createDeposit(pSubscriberInfo, amount, memoText, sessionId);
	}

	@Override
	public void updatePortRestriction(int ban, String subscriberNo,
			boolean restrictPort, String userID)
			throws ApplicationException {
		SubscriberDao subscriberDao = subscriberDaoFactory.getSubscriberDao();
		subscriberDao.updatePortRestriction(ban, subscriberNo, restrictPort, userID);		
	}

	@Override
	public SubscriberInfo updateSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException {
		SubscriberDao subscriberDao = subscriberDaoFactory.getSubscriberDao();
		return subscriberDao.updateSubscriber(subscriberInfo, sessionId);
	}

	@Override
	public void updateSubscriptionRole(int ban, String subscriberNo,
			String subscriptionRoleCode, String dealerCode,
			String salesRepCode, String csrId) throws ApplicationException {
		SubscriberDao subscriberDao = subscriberDaoFactory.getSubscriberDao();
		subscriberDao.updateSubscriptionRole(ban, subscriberNo, subscriptionRoleCode
				, dealerCode, salesRepCode, csrId);
	}

	@Override
	public void cancelSubscriber(SubscriberInfo pSubscriberInfo,
			Date pActivityDate, String pActivityReasonCode,
			String pDepositReturnMethod, String pWaiveReason,
			String pUserMemoText, boolean isPortActivity, String sessionId) throws ApplicationException {
		subscriberDao.cancelSubscriber(pSubscriberInfo, pActivityDate, 
				pActivityReasonCode, pDepositReturnMethod, pWaiveReason, pUserMemoText,isPortActivity, sessionId);
	}

	@Override
	public void refreshSwitch(int pBan, String pSubscriberId, String pProductType, String sessionId) throws ApplicationException {	
		subscriberDao.refreshSwitch(pBan, pSubscriberId, pProductType, sessionId);
	}

	@Override
	public void restoreSuspendedSubscriber(SubscriberInfo pSubscriberInfo,
			Date pActivityDate, String pActivityReasonCode,
			String pUserMemoText, boolean portIn, String sessionId)
			throws ApplicationException {
		subscriberDao.restoreSuspendedSubscriber(pSubscriberInfo, pActivityDate, pActivityReasonCode, pUserMemoText, portIn, sessionId);
	}

	@Override
	public void resumeCancelledSubscriber(SubscriberInfo pSubscriberInfo,
			String pActivityReasonCode, String pUserMemoText, boolean portIn,
			String portProcessType, int oldBanId, String oldSubscriberId,
			String sessionId) throws ApplicationException {
		subscriberDao.resumeCancelledSubscriber(pSubscriberInfo, pActivityReasonCode, pUserMemoText, portIn, portProcessType, oldBanId, oldSubscriberId, sessionId);
	}

	@Override
	public Collection<Subscriber> retrieveSubscribersByMemberIdentity(int urbanId, int fleetId, int memberId, String sessionId)
			throws ApplicationException {
		
		Subscriber[] subscribers = subscriberDao.retrieveSubscribersByMemberIdentity(urbanId, fleetId, memberId, sessionId);
		for (int i = 0; i < subscribers.length; i++) {
			subscribers[i] = this.getSubscriberLifecycleHelper().retrieveSubscriber(subscribers[i].getBanId(), subscribers[i].getSubscriberId());
		}
		
		return Arrays.asList(subscribers);
	}


	@Override
	public void updateEmailAddress(int ban, String subscriberNumber,
			String emailAddress, String sessionId) throws ApplicationException {
		SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriber(ban, subscriberNumber);
		subscriberDao.updateEmailAddress(ban, subscriberNumber, emailAddress, subscriberInfo.getProductType(), sessionId);
	}

	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}


	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public double getAirtimeRate(int billingAccountNumber, String subscriberId, String sessionId) throws ApplicationException {
		
		List<Double> list = new ArrayList<Double>();		
		ServiceAirTimeAllocationInfo[] serviceAirTimeAllocationInfos=retrieveVoiceAllocation(billingAccountNumber, subscriberId, sessionId);
		
		for (ServiceAirTimeAllocationInfo serviceAirTimeAllocationInfo:serviceAirTimeAllocationInfos) {
			
			if (serviceAirTimeAllocationInfo.getServiceType().equals(ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN)) {				
				FeatureAirTimeAllocationInfo[] featureAirTimeAllocationInfos= serviceAirTimeAllocationInfo.getFeatureAirTimeAllocations0();			
				for (FeatureAirTimeAllocationInfo featureAirTimeAllocationInfo:featureAirTimeAllocationInfos) {
					boolean partialmatch = false;
					if (featureAirTimeAllocationInfo.getCode().equals("STD") 
							&&	featureAirTimeAllocationInfo.getClassification0().getCode().equals("AT")
							&& featureAirTimeAllocationInfo.isUsageCharge() == true) {
						partialmatch = true;
						if (featureAirTimeAllocationInfo.getUsageCharge() >= 0) {
							list.add(featureAirTimeAllocationInfo.getUsageCharge());							
						}
					}
					if (partialmatch && featureAirTimeAllocationInfo.getPeriodValueCode().equals("C")
							&& featureAirTimeAllocationInfo.getTierLevelCode() == 0 
							&& featureAirTimeAllocationInfo.getCallingDirection().equals("0")
							&&	featureAirTimeAllocationInfo.getUsageChargeDependCode().equals("F")
							&& featureAirTimeAllocationInfo.isPeriodBased() == false) {
						return featureAirTimeAllocationInfo.getUsageCharge();
					}					
				}
			
				return findUCRate(list, subscriberId);
			}
		}
		
		return 0;
	}
	
	private double findUCRate(List<Double> list, String subscriberId) throws ApplicationException {
		
		 Collections.sort(list, Collections.reverseOrder());
		 if (list.size() > 1 && list.get(0).doubleValue() > 0.0) {
			   double ucRate = list.get(0).doubleValue();
			   for (int count = 1; count < list.size(); count++) {
			   			if (!(ucRate == list.get(count).doubleValue() || (list.get(count).doubleValue() == 0.0) )) {
			   			   	// if different values of ucRates occurs
			   		    	throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.GET_SUBSCRIBER_AIRTIME_RATE_ERROR, 
										"No single price plan airtime usage rate is found for subscriber " + subscriberId, "");
			   		    }
			   }
			   return ucRate; // all ucRates are same Or more than one features has same ucRate
		 } else if (list.size() == 1 || (list.size() > 1 && list.get(0).doubleValue() == 0.0)) {
				return list.get(0); // only one ucRate available Or more than one Zero ucRate's
		 } else {		// throw exception if no ucRate found
		    	throw new ApplicationException(SystemCodes.CMB_SLM_EJB, ErrorCodes.GET_SUBSCRIBER_AIRTIME_RATE_ERROR, 
						"No single price plan airtime usage rate is found for subscriber " + subscriberId, "");	
		 }
	}

	@Override
	public ServiceAirTimeAllocationInfo[] retrieveVoiceAllocation(int billingAccountNumber, String subscriberId, String sessionId) throws ApplicationException {
		SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriber(billingAccountNumber, subscriberId);
		return subscriberDao.retrieveVoiceAllocation(billingAccountNumber, subscriberId, subscriberInfo.getProductType(), sessionId);
	}
	
	private ClientIdentity getClientIdentity(String sessionId) throws ApplicationException {
		ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
		if (ci == null) {
			throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
		}
		return ci;
	}	
	
	@Override
	public void updatePrepaidSubscriber(PrepaidSubscriberInfo prepaidSubscriberInfo) throws ApplicationException {
		prepaidSubscriberServiceDao.updatePrepaidSubscriber(prepaidSubscriberInfo);	
	}

	@Override
	public TestPointResultInfo testPrepaidSubscriberService() {
		return prepaidSubscriberServiceDao.test();
	}
	
	@Override
	public TestPointResultInfo testPrepaidWirelessCustomerOrderService() {
		return prepaidWirelessCustomerOrderServiceDao.test();
	}
	
	@Override
	public TestPointResultInfo testOrderService() {
		return orderServiceDao.test();
	}

	@Override
	public TestPointResultInfo testSubscriptionService() {
		return subscriptionServiceDao.test();
	}

	@Override
	public void changeResources(SubscriberInfo subscriberInfo, List resourceList, Date activityDate, String sessionId) throws ApplicationException {
		subscriberDaoFactory.getUpdatePcsSubscriberDao().changeResources(subscriberInfo, resourceList, activityDate, sessionId);	
	}
	
	@Override
	public void changeSeatGroup(SubscriberInfo subscriberInfo, String seatGroupId, String sessionId) throws ApplicationException {
		subscriberDaoFactory.getUpdatePcsSubscriberDao().changeSeatGroup(subscriberInfo, seatGroupId, sessionId);
	}
	
	@Override
	public void resetCSCSubscription(int ban, String subscriberId, String productType,String sessionId) throws ApplicationException{
		String[] cscFeature = getSubscriberLifecycleHelper().retrieveCSCFeatureByPhoneNumber(subscriberId, productType);

		// return if no CSC features
		if (cscFeature.length < 2) return;		
		if (productType == null) {
            SubscriberInfo subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriber(ban, subscriberId);
            productType = subscriberInfo.getProductType();
     }
		UpdateSubscriberDao updateSubscriberDao = subscriberDaoFactory.getUpdateSubscriberDao(productType);		
		updateSubscriberDao.resetCSCSubscription(ban, subscriberId, cscFeature, sessionId);
	}
}