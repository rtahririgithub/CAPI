package com.telus.cmb.subscriber.bo;

import java.util.Date;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.IncompleteSubscriberCreationProcessException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.portability.InterBrandPortRequestException;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortOutEligibilityException;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.cmb.common.util.ExceptionUtil;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.utilities.ActivatePortinContext;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.ApplicationMessageManager;
import com.telus.cmb.subscriber.utilities.activation.ActivationPortInUtilities;
import com.telus.cmb.subscriber.utilities.activation.ActivationUtilities;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.CreateSubscriberException;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * 
 * @author Max Strigachov, R. Fong
 *
 */
public class ActivatePortinBo {
	
	public static final String LANGUAGE_ENGLISH = "EN";
	public static final String LANGUAGE_FRENCH = "FR";
	public static final String NUMBER_LOCATION_POSTPAID = "TLS";
	public static final String TRANSACTION_TYPE_CHANGE_ROLE = "O";
	
	public static final char STATUS_RESERVED = 'R';
	public static final char STATUS_ACTIVE = 'A';
	public static final char STATUS_SUSPENDED = 'S';
	public static final char STATUS_CANCELED = 'C';
	
	private static final String POSTPAID = "O";
	private static final String PREPAID = "R";
	private static final String BILLING_SYSTEM_FAILED = "BSTF";
	
	private static Logger logger = LoggerFactory.getLogger(ActivatePortinBo.class);

	private static ApplicationMessageManager applicationMessageManager;
	
	protected ActivatePortinContext changeContext;
	protected ActivationChangeInfo changeInfo;
	
	public ActivatePortinBo(ActivatePortinContext changeContext) {
		this.changeContext = changeContext;
		this.changeInfo = changeContext.getChangeInfo();
	}
	
	public void activatePortin() throws ApplicationException {
		
		try {	
			validateAndCreatePortRequest();
			createSubscriber();
			submitPortInRequest();
			postprocessing();
		} catch (ApplicationException ae) {
			logger.error("activatePortInRequest failed with the following error: {}", ae.getMessage());
			throw ae;
		} catch (Throwable t) {
			logger.error("activatePortInRequest failed with the following error: {}", t.getMessage());
			throw new RuntimeException("activatePortInRequest failed with the following error: " + t.getMessage());
		}
	}
	
	private void validateAndCreatePortRequest() throws ApplicationException, PortRequestException {
		
		String portProcess = changeContext.getPortProcessCode();
		PortInEligibility portInEligibility = changeInfo.getPortInEligibility();
		String portRequestId = null;
		try {
			if (StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_BRAND_PORT, portProcess)) {			 
				if (ActivationPortInUtilities.testPortOutEligibility(changeContext.getCurrentSubscriber().getPhoneNumber(), PortOutEligibility.NDP_DIRECTION_IND_WIRELESS_TO_WIRELESS, 
						changeContext.getSubscriberLifecycleHelper()).isEligible()) {
					portRequestId = createPortRequest(portProcess, portInEligibility.getIncomingBrandId(), portInEligibility.getOutgoingBrandId());
					logger.debug("Flow 'activatePortInRequest.validateAndCreatePortRequest has succeeded for portRequestId [{}]", portRequestId);
				} else {
					throw new PortOutEligibilityException("phone number [" + changeContext.getCurrentSubscriber().getPhoneNumber() + "] is not eligible to port out");
				}
			} else if (StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT, portProcess) || StringUtils.equals(PortInEligibilityInfo.PORT_PROCESS_INTER_MVNE_PORT, portProcess)) {
				portRequestId = createPortRequest(portProcess, changeContext.getCurrentSubscriber().getBrandId(), Brand.BRAND_ID_NOT_APPLICABLE); // this is the default for inter-carrier port requests
				// if this is an MVNE port, call the WNP services PrePortRequestValidationService and DeactivateMVNESubscriberService
				if (StringUtils.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT, portProcess)) {
					// the PrePortRequestValidationService is required to be called first so that WNP can validate that the subscriber exists on the MVNE system
					validateMVNESubscriber();
					// if no exception is thrown, call the DeactivateMVNESubscriberService to deactivate the subscriber in the MVNE system - this is the subscriber we want to port-in
					deactivateMVNESubcriber();
				}
				logger.debug("Flow 'activatePortInRequest.validateAndCreatePortRequest has succeeded for portRequestId [{}]", portRequestId);
			} else {
				throw new PortRequestException("Unknown port process exception [" + portProcess + "]");
			}
		} catch (ApplicationException ae) {
			logger.error("Flow 'activatePortInRequest.validateAndCreatePortRequest' has failed with an error: [{}]", ae.getMessage());
			throw ae;
		} catch (PortRequestException pre) {
			logger.error("Flow 'activatePortInRequest.validateAndCreatePortRequest' has failed with an error: [{}]", pre.getMessage());
			throw pre;
		}
	}
	
	// Updated for CDA phase 1B July 2018
	private void createSubscriber() throws ApplicationException, TelusAPIException {

		try {
			changeContext.getCurrentSubscriber().getActivationOption().apply();
			save();
			
		} catch (IncompleteSubscriberCreationProcessException ispe) {
			// 2.2 Catch soft (non-fatal) exception, let the process to complete, then throw it at the very end so that port request will not be cancelled due to soft exception,
			// which shouldn't stop the activation process.
			logger.error("Flow 'activatePortIn.createSubscriber' failed with non-fatal error for subscriber with " + "[ban: {}, id: {}, phone: {}]; Error {}. The process is continuing.",
					new Object[] { changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
							ispe.getMessage() });
			changeContext.setSoftException(ispe);
			
		} catch (Throwable t) {
			logger.error("Flow 'activatePortIn.createSubscriber' failed for subscriber with [ban: {}, id: {}, phone: {}]; Error {}. The process has been stopped.", new Object[] {
					changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
			// 2.2 Cancel the port request in case of save failed in KB
			cancelPortRequest();
			// 2.3 Throw the original exception
			if (t instanceof TelusAPIException) {
				throw (TelusAPIException) t;
			} else if (t instanceof ApplicationException) {
				throw (ApplicationException) t;
			} else {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
			}
		}
	}
	
	private void submitPortInRequest() throws ApplicationException, PRMSystemException {
		
		try {
			changeContext.getSubscriberLifecycleFacade().submitPortInRequest(changeContext.getPortRequestId(), changeContext.getRequestHeader().getApplicationName());
			logger.debug("Flow activatePortIn.submitPortInRequest succeeded " + "for subscriberId: [{}], ban: [{}], phoneNumber: [{}], portRequestId: [{}].",
					new Object[] { changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
							changeContext.getPortRequestId() });
		} catch (Throwable t) {
			logger.error("Flow activatePortIn.setSubscriberPortIndicator failed for subscriberId: [{}], ban: [{}], phoneNumber: [{}], portRequestId: [{}]. Error {} is ignored.",
					new Object[] { changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
							changeContext.getPortRequestId(), t.getMessage() });
			throw new PRMSystemException("submit port request - in save() failed for id [" + changeContext.getPortRequestId() + "]; cause: " + t.toString(), t);
		}
	}	
	
	private void postprocessing() throws Exception {
		
		if (changeContext.getSoftException() != null) {
			throw changeContext.getSoftException();
		}
		ActivationUtilities.setSIMMuleRelation(false, changeContext.getCurrentSubscriber().getStartServiceDate(), changeContext.getCurrentEquipment(),	changeContext.getProductEquipmentManager());
		ActivationUtilities.createActivationMemo(changeContext.getCurrentSubscriber().getDelegate(), changeInfo.getMemoText(), changeContext.getAccountLifecycleFacade(), 
				changeContext.getAccountLifecycleFacadeSessionId());
		ActivationUtilities.updateSubscriptionRole(changeContext.getCurrentSubscriber().getDelegate(), changeInfo.getSubscriptionRoleCode(), changeContext.getClientIdentity().getPrincipal(), 
				changeContext.getSubscriberLifecycleManager());
	}
	
	private void cancelPortRequest() throws ApplicationException {
		
		if (changeContext.isPortedIn()) {
			try {
				changeContext.getSubscriberLifecycleFacade().cancelPortInRequest(changeContext.getPortRequestId(), BILLING_SYSTEM_FAILED, changeContext.getRequestHeader().getApplicationName());
				logger.debug("Flow 'activatePortIn.cancelPortRequest' succeeded for subscriber with [ban: {}, id: {}, phone: {}]; Port Request ID: [{}];", 
						new Object[] { changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
						changeContext.getPortRequestId() });
			} catch (Throwable t) {
				logger.error("Flow 'activatePortIn.cancelPortRequest' failed for subscriber with [ban: {}, id: {}, phone: {}]; Error {}. Port Request ID: [{}]; The process has been stopped.", 
						new Object[] { changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
						t.getMessage(), changeContext.getPortRequestId() });
			}
		}
	}
	
	private void save() throws ApplicationException, UnknownObjectException, TelusAPIException {

		ServicesValidation srvValidation = changeInfo.getServiceValidation();
		if (srvValidation == null) {
			srvValidation = new ServicesValidationInfo();
		}
		((ServicesValidationInfo) srvValidation).setEquipmentServiceMatch(false);
		ActivationUtilities.checkForVoicemailService(changeContext.getCurrentContract());
		//TODO: Commenting out prepaid logic only for October 2016 release
//		PrepaidUtilities.updatePrepaidSystem(changeContext, changeInfo.getTopUpPaymentArrangement());
		
		CreateSubscriberException delayedCreateSubscriberException = null;
		try {
			if ((changeContext.getCurrentSubscriber().getDealerCode() == null || changeContext.getCurrentSubscriber().getDealerCode().trim().length() == 0) && !changeContext.getCurrentAccount().isPostpaid()
					&& StringUtils.equals(NUMBER_LOCATION_POSTPAID, changeContext.getCurrentSubscriber().getNumberGroup().getNumberLocation())) {
				changeContext.getCurrentSubscriber().setDealerCode(changeContext.getCurrentSubscriber().getNumberGroup().getDefaultDealerCode());
				changeContext.getCurrentSubscriber().setSalesRepId(changeContext.getCurrentSubscriber().getNumberGroup().getDefaultSalesCode());
			}

			if (StringUtils.isBlank(changeContext.getCurrentSubscriber().getPhoneNumber())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CREATE_SUBSCRIBER_FAILED, "No phone number has been reserved", "");
			}

			if (changeInfo.isDealerHasDeposit() && StringUtils.isEmpty(changeContext.getCurrentSubscriber().getDealerCode())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CREATE_SUBSCRIBER_FAILED, "dealerHasDeposit == true, but dealerCode is empty", "");
			}

			if (StringUtils.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT, changeContext.getPortProcessCode())) {
				// execute the inter-brand port activation logic
				interBrandPortCreateSubscriber();

			} else {
				// execute the regular activation logic
				changeContext.getSubscriberLifecycleFacade().createSubscriber(changeContext.getCurrentSubscriber().getDelegate(), changeContext.getCurrentContract().getDelegate(), true,
						changeInfo.isWaiveSearchFee(), changeInfo.getActivationFeeChargeCode(), changeInfo.isDealerHasDeposit(), changeContext.isPortedIn(), srvValidation,
						changeContext.getPortProcessCode(), 0, null, changeContext.getSubscriberLifecycleFacadeSessionId());

				if (StringUtils.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT, changeContext.getPortProcessCode())) {
					performInterBrandOrMVNEPortActivities();
				}
			}

			// assign TN resources after the subscriber is successfully created
			if (changeContext.getCurrentSubscriber().isPCS() && !changeContext.getCurrentEquipment().isHSIADummyEquipment() && !changeContext.getCurrentEquipment().isVOIPDummyEquipment()) {
				try {
					changeContext.getSubscriberLifecycleFacade().assignTNResources(changeContext.getCurrentSubscriber().getPhoneNumber(), NetworkType.NETWORK_TYPE_HSPA,
							changeContext.getCurrentEquipment().getProfile().getLocalIMSI(), changeContext.getCurrentEquipment().getProfile().getRemoteIMSI());
					logger.debug("Flow activatePortIn.save assign TN resources for phone number [{}], NetworkType[{}]", changeContext.getCurrentSubscriber().getPhoneNumber(),
							NetworkType.NETWORK_TYPE_HSPA);
				} catch (Throwable t) {
					// if the TN update fails, just log it and continue
					logger.debug("Flow activatePortIn assign TN resources failed for phone number [{}]; exception ignored", changeContext.getCurrentSubscriber().getPhoneNumber());

				}
			}
		} catch (ApplicationException ae) {
			if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.ALL_STEPS);
			} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.ALL_STEPS_EXCEPT_MEMOS);
			} else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.APPLY_DISCOUNTS);
			} else {
				throw ae;
			}
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB,ErrorCodes.CREATE_SUBSCRIBER_FAILED, t.getMessage(), "", t);
		}

		try {
			// putting a try-catch block for silent failure so that it won't stop the rest of the flow
			try {
				// update home province if this is the first activated subscriber on this account
				if (changeContext.getCurrentAccount().getStatus() == Account.STATUS_TENTATIVE && changeContext.getCurrentAccount().getActiveSubscribersCount() == 0) {
					// resolve province code
					String provinceCode = changeContext.getCurrentSubscriber().getAddress() != null ? changeContext.getCurrentSubscriber().getAddress().getProvince() 
							: changeContext.getCurrentSubscriber().getNumberGroup() != null ? changeContext.getCurrentSubscriber().getNumberGroup().getProvinceCode() : null;
					changeContext.getAccountLifecycleManager().updateNationalGrowth(changeContext.getCurrentAccount().getBanId(), null, provinceCode, changeContext.getAccountLifecycleManagerSessionId());
				}
			} catch (Throwable t) {
				logger.error(t.getMessage());
			}

			// added isHSIAEquipment and isVOIPEquipment checks to activate the subscriber without equipment for Business Connect STARTER and OFFICE seats
			if (changeContext.getCurrentEquipment().isHSPA() && !changeContext.getCurrentEquipment().isHSIADummyEquipment() && !changeContext.getCurrentEquipment().isVOIPDummyEquipment()) {
				try {
					if (changeContext.isExistingUSIMPortIn()) {
						// retrieve the current associated handset by USIM to compare with the port-in associated handset IMEI
						EquipmentInfo oldAssociatedHandset = changeContext.getProductEquipmentHelper().getAssociatedHandsetByUSIMID(changeContext.getCurrentEquipment().getSerialNumber());
						if (!StringUtils.equals(oldAssociatedHandset.getSerialNumber(), changeContext.getCurrentEquipment().getDelegate().getAssociatedHandsetIMEI())) {
							// call SEMS to assign the port-in associated IMEI and USIM
							changeContext.getProductEquipmentLifecycleFacade().asyncSwapHSPAOnlyEquipmentForPhoneNumber(changeContext.getCurrentSubscriber().getPhoneNumber(), 
									changeContext.getCurrentEquipment().getSerialNumber(), changeContext.getCurrentEquipment().getSerialNumber(), oldAssociatedHandset.getSerialNumber(), 
									changeContext.getCurrentEquipment().getDelegate().getAssociatedHandsetIMEI());
						}
						
					} else {
						// execute SEMS update for all other BAU flows
						changeContext.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(changeContext.getCurrentSubscriber().getPhoneNumber(),
							changeContext.getCurrentEquipment().getSerialNumber(), changeContext.getCurrentEquipment().getAssociatedHandsetIMEI());
					}
				} catch (Throwable t) {
					// if the SEMS update fails, just log it and continue
					logger.error("Flow 'activatePortIn.assignEquipmentToPhoneNumber' failed to assign Equipment to Phone for phone number [{}]; exception ignored.",
							changeContext.getCurrentSubscriber().getPhoneNumber());
				}
			}

			changeContext.refreshSubscriberInfo(); // reload entire subscriber
		} catch (Throwable t) {
			logger.error(t.getMessage());
		}

		if (delayedCreateSubscriberException != null) {
			throw new IncompleteSubscriberCreationProcessException(delayedCreateSubscriberException, delayedCreateSubscriberException.getIncompleteSteps());
		}
	}

	private void performInterBrandOrMVNEPortActivities() throws ApplicationException {
		// set the subscriber port indicator in KB
		try {
			changeContext.getSubscriberLifecycleManager().setSubscriberPortIndicator(changeContext.getCurrentSubscriber().getPhoneNumber(), changeContext.getSubscriberLifecycleManagerSessionId());
			logger.debug("Flow activatePortIn.setSubscriberPortIndicator succeeded for subscriberId: [{}], ban: [{}], phoneNumber: [{}].",
					new Object[] { changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getPhoneNumber() });
			
		} catch (Throwable t) {
			// if the port indicator update fails, just log it and continue
			logger.error("Flow activatePortIn.setSubscriberPortIndicator failed for subscriberId: [{}], ban: [{}], phoneNumber: [{}]. Error {} is ignored.", new Object[] {
					changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
		}
	}
	
	private void interBrandPortValidateOSPPortRequestData(SubscriberBo outgoingSub) throws PortOutEligibilityException, ApplicationException {

		PortRequest portRequest = changeContext.getPortRequest();
		if (portRequest != null) {
			String accountPIN = outgoingSub.getAccount().getPin();
			// only one of the following pieces of data needs to match in order for the request to be considered valid
			if (StringUtils.equals(String.valueOf(outgoingSub.getBanId()), portRequest.getOSPAccountNumber())) {
				return;
			}
			// changes to support both primary serial number and HSPA handset IMEI
			EquipmentInfo outgoingSubEquipment = outgoingSub.getEquipment().getDelegate();
			if (StringUtils.equals(outgoingSubEquipment.getSerialNumber(), portRequest.getOSPSerialNumber())) {
				return;
			}
			if (outgoingSubEquipment.isUSIMCard()) {
				// if it's USIM card, then check the last associated handset's IMEI
				if (StringUtils.equals(outgoingSubEquipment.getAssociatedHandsetIMEI(), portRequest.getOSPSerialNumber())) {
					return;
				}
			}
			if (StringUtils.equals(accountPIN, portRequest.getOSPPin())) {
				return;
			}
			// otherwise, throw a PortOutEligibilityException with the appropriate message
			if (!StringUtils.isBlank(portRequest.getOSPAccountNumber()) && !StringUtils.equals(String.valueOf(outgoingSub.getBanId()), portRequest.getOSPAccountNumber())) {
				throw new PortOutEligibilityException(getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ACCOUNT_NUMBER));
			}
			if (!StringUtils.isBlank(portRequest.getOSPSerialNumber()) && !StringUtils.equals(outgoingSub.getEquipment().getSerialNumber(), portRequest.getOSPSerialNumber())) {
				throw new PortOutEligibilityException(getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ESN));
			}
			if (!StringUtils.isBlank(portRequest.getOSPPin()) && !StringUtils.equals(accountPIN, portRequest.getOSPPin())) {
				throw new PortOutEligibilityException(getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_PIN));
			}
		}
		// if everything is null or empty, throw a PortOutEligibilityException for invalid OSP account number
		throw new PortOutEligibilityException(getApplicationMessage(PortOutEligibilityException.ERR_INVALID_OSP_ACCOUNT_NUMBER));
	}
	
	private synchronized ApplicationMessageManager getApplicationManager() {
		
		if (applicationMessageManager == null) {
			try {
				applicationMessageManager = new ApplicationMessageManager(changeContext.getRefDataHelper());
			} catch (Throwable t) {
				throw new SystemException(SystemCodes.CMB_SLF_EJB, "Error instantiating ApplicationMessageManager", "", t);
			}
		}		
		return applicationMessageManager;
	}
	
	private ApplicationMessage getApplicationMessage(long messageId) throws ApplicationException {
		return getApplicationManager().getApplicationMessage(getApplicationSummary(), AudienceType.DEFAULT, changeContext.getCurrentSubscriber().getBrandId(), messageId);
	}
	
	private ApplicationSummary getApplicationSummary() {
	
		ApplicationSummary appSummary = null;
		String applicationCode = changeContext.getRequestHeader().getApplicationName();
		try {
			appSummary = changeContext.getRefDataFacade().getApplicationSummary(applicationCode);
		} catch (Throwable t) {
			// eat the exception and log the warning
			logger.warn("ApplicationSummary for applicationCode = [{}] returned exception.  Using default ApplicationSummary.", applicationCode);
		}
		// if the appSummary is null, assign applicationSummary to the default
		return appSummary != null ? appSummary : ApplicationSummary.DEFAULT; 
	}
	
	private void interBrandPortCreateSubscriber() throws SystemException, ApplicationException, TelusAPIException {

		String activity = "retrieve active subscriber on old brand";
		SubscriberInfo outgoingSub = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(changeContext.getCurrentSubscriber().getPhoneNumber());
		outgoingSub.setInternalUse(true);

		logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]",
				new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber() });

		changeContext.setPreviousSubscriberInfo(outgoingSub);
		
		// validate the current subscriber and USIM association if subscriber keeping the existing USIM for portIn 
		if(changeContext.isExistingUSIMPortIn()){
			if (!StringUtils.equals(changeContext.getCurrentEquipment().getSerialNumber(),outgoingSub.getSerialNumber())) {
				ActivationUtilities.throwEquipmentValidateFailedException("USIM card [" + changeContext.getCurrentEquipment().getSerialNumber() + "] is not used by outgoing subscriberId [ "+outgoingSub.getSubscriberId()+" ]");
			}
		}
		// validate OSP account, ESN or PIN information
		activity = "validate OSP port request information";
		interBrandPortValidateOSPPortRequestData(changeContext.getPreviousSubscriber());
		logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]",
				new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber() });

		// get the reason code
		activity = "determine the activity reason code";
		String activityReasonCode = getInterBrandPortActivityReasonCode(changeContext.getCurrentSubscriber(), changeContext.getPreviousSubscriber());
		logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]; activityReasonCode: {}", new Object[] { activity,
				changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), activityReasonCode });

		// cancel the subscriber on the old (outgoing) brand and reserve the number on the new (incoming) brand
		activity = "cancel outgoing subscriber and reserve incoming subscriber";
		Date activityDate = changeContext.getRefDataHelper().retrieveLogicalDate();
		interBrandPortCancelOutgoingSubscriber(changeContext.getPreviousSubscriber(), activityReasonCode, activityDate, null);
		SubscriberInfo reservedSubInfo = interBrandPortReserveIncomingSubscriber(outgoingSub, activityReasonCode);

		try {
			// create the subscriber on the new (incoming) brand
			activity = "create subscriber on incoming brand";
			changeContext.getSubscriberLifecycleFacade().createSubscriber(reservedSubInfo, changeContext.getCurrentContract().getDelegate(), true, // it is always activate in this flow
					changeInfo.isWaiveSearchFee(), changeInfo.getActivationFeeChargeCode(), changeInfo.isDealerHasDeposit(),
					changeContext.isPortedIn(), changeInfo.getServiceValidation(), changeContext.getPortProcessCode(), outgoingSub.getBanId(), outgoingSub.getSubscriberId(),
					changeContext.getSubscriberLifecycleFacadeSessionId());

			// preserve the subscriberId from subscriber reservation
			changeContext.getCurrentSubscriber().getDelegate().setSubscriberId(reservedSubInfo.getSubscriberId());
			logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}];",
					new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber() });

		} catch (Throwable t) {
			// if the create fails, release the reserved subscriber and perform rollback
			logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]; Error {}.", new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(),
					changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
			interBrandPortReleaseReservedNumber(reservedSubInfo);
			interBrandPortRollbackResumeCancelledSubscriber(outgoingSub, activityReasonCode);
			throw new InterBrandPortRequestException(t, getApplicationMessage(InterBrandPortRequestException.ERR003), InterBrandPortRequestException.ERR003);
		}

		performInterBrandOrMVNEPortActivities();
	}
	
	private void interBrandPortReleaseReservedNumber(SubscriberInfo incomingSub) throws InterBrandPortRequestException, ApplicationException {

		String activity = "release inter brand port reserved subscriber [" + incomingSub.getPhoneNumber() + "]";
		String activityContext = "reserved phone number [" + incomingSub.getPhoneNumber() + "]";
		try {
			// Inter Brand Roll back Subscriber Production Fix : 2020-July : create subscriber dao already released the number and this release call should not be an hard stop for resume subscriber  or rollback flow 		
			changeContext.getSubscriberLifecycleManager().releasePortedInSubscriber(incomingSub, changeContext.getSubscriberLifecycleManagerSessionId());
			
		} catch (Throwable t) {
			logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, subscriber id: {}, phone: {}]; {}; Error {}.", new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(),
					changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), activityContext, t.getMessage() });
		}
	}
	
	private String getInterBrandPortActivityReasonCode(SubscriberBo incomingSub, SubscriberBo outgoingSub) throws InterBrandPortRequestException, ApplicationException {
		
		String outgoingKey = outgoingSub.getBrandId() + outgoingSub.getProductType() + (outgoingSub.getAccount().isPostpaid() ? POSTPAID : PREPAID);
		String incomingKey = incomingSub.getBrandId() + incomingSub.getProductType() + (incomingSub.getAccount().isPostpaid() ? POSTPAID : PREPAID);
		Map<String, String> interBrandPortActivityReasonCodesKeyMap = AppConfiguration.getInterBrandPortActivityReasonCodesKeyMap();
		String reasonCode = (String) interBrandPortActivityReasonCodesKeyMap.get(outgoingKey) + (String) interBrandPortActivityReasonCodesKeyMap.get(incomingKey);
		if (AppConfiguration.getInterBrandPortActivityReasonCodes().contains(reasonCode)) {
			return reasonCode;
		} else {
			throw new InterBrandPortRequestException(getApplicationMessage(InterBrandPortRequestException.ERR009), InterBrandPortRequestException.ERR009);
		}
	}	
	
	private SubscriberInfo interBrandPortReserveIncomingSubscriber(SubscriberInfo outgoingSub, String activityReasonCode) throws ApplicationException, TelusAPIException {

		String phoneNumber = changeContext.getCurrentSubscriber().getPhoneNumber();
		NumberGroup numberGroup = changeContext.getCurrentSubscriber().getNumberGroup();

		String activity = "reserve phone number on incoming brand";
		try {
			// reserve the phone number on the new (incoming) brand
			PhoneNumberReservationInfo reservation = new PhoneNumberReservationInfo();
			reservation.setNumberGroup(numberGroup);
			reservation.setPhoneNumberPattern(phoneNumber);
			SubscriberInfo reservedSubInfo = changeContext.getSubscriberLifecycleManager().reservePortedInPhoneNumber(changeContext.getCurrentSubscriber().getDelegate(), reservation, true,
					changeContext.getSubscriberLifecycleManagerSessionId());
			logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]; Reserved subscriber info: {}",
					new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
							reservedSubInfo.toString() });
			
			return reservedSubInfo;

		} catch (Throwable t) {
			logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}];",
					new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber() });
			interBrandPortRollbackResumeCancelledSubscriber(outgoingSub, activityReasonCode);
			throw new InterBrandPortRequestException(t, getApplicationMessage(InterBrandPortRequestException.ERR002), InterBrandPortRequestException.ERR002);
		}
	}
	
	private void interBrandPortRollbackResumeCancelledSubscriber(SubscriberInfo outgoingSub, String activityReasonCode) throws InterBrandPortRequestException, SystemException, ApplicationException {

		String activity = "rollback subscriber cancellation on outgoing brand";
		try {
			// if the calling activity failed, attempt to rollback the cancellation on the old (outgoing) brand
			changeContext.getSubscriberLifecycleFacade().resumeCancelledSubscriber(
					outgoingSub, activityReasonCode, "Inter-brand port rollback from BAN [" + changeContext.getCurrentSubscriber().getBanId() + "] to BAN [" + outgoingSub.getBanId()
							+ "] and subscriber [" + outgoingSub.getSubscriberId() + "]",
					true, PortInEligibility.PORT_PROCESS_ROLLBACK, outgoingSub.getBanId(), outgoingSub.getSubscriberId(), null, changeContext.getSubscriberLifecycleFacadeSessionId());
			logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]; Outgoing subscriber info: {}", new Object[] { activity,
					changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), outgoingSub.toString() });

			// assign the TN resources after the (old) outgoing subscriber is resumed
			if (outgoingSub.isPCS()) {
				activity = "assign TN";
				try {
					changeContext.getSubscriberLifecycleFacade().assignTNResources(changeContext.getCurrentSubscriber().getPhoneNumber(), NetworkType.NETWORK_TYPE_HSPA,
							changeContext.getCurrentEquipment().getProfile().getLocalIMSI(), changeContext.getCurrentEquipment().getProfile().getRemoteIMSI());
					logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]; Outgoing subscriber info: {}",
							new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(),
									changeContext.getCurrentSubscriber().getPhoneNumber(), outgoingSub.toString() });
				} catch (Throwable t) {
					// if the TN resource update fails, just log it and continue
					logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]; Exception {} ignored.", new Object[] { activity,
							changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
				}
			}

			// set the subscriber port indicator in KB back to Subscriber.PORT_TYPE_PORT_IN if necessary and with the correct date
			if (StringUtils.equals(outgoingSub.getPortType(), Subscriber.PORT_TYPE_PORT_IN)) {
				activity = "set subscriber port indicator";
				try {
					changeContext.getSubscriberLifecycleManager().setSubscriberPortIndicator(outgoingSub.getPhoneNumber(), outgoingSub.getPortDate(),
							changeContext.getSubscriberLifecycleManagerSessionId());
					logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]; Outgoing subscriber info: {}",
							new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(),
									changeContext.getCurrentSubscriber().getPhoneNumber(), outgoingSub.toString() });
				} catch (Throwable t) {
					// if the port indicator update fails, just log it and continue
					logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]; Exception {} ignored.", new Object[] { activity,
							changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
				}
			}

		} catch (Throwable t) {
			logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]; Exception {} ignored.", new Object[] { activity,
					changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
			throw new InterBrandPortRequestException(t, getApplicationMessage(InterBrandPortRequestException.ERR004), InterBrandPortRequestException.ERR004);
		}
	}

	private void interBrandPortCancelOutgoingSubscriber(SubscriberBo outgoingSub, String activityReasonCode, Date activityDate, ServiceRequestHeader header) throws TelusAPIException, ApplicationException {

		String activity = "interBrandPortCancelOutgoingSubscriber";
		try {
			// check if the subscriber is suspended or cancelled - if suspended, restore the subscriber first; this is the workaround until Amdocs fixes their defect
			if (outgoingSub.getStatus() == STATUS_SUSPENDED) {
				// restore the subscriber if current status is suspended
				changeContext.getSubscriberLifecycleFacade().restoreSuspendedSubscriber(outgoingSub.getDelegate(), activityDate, "POUT", "", false,	changeContext.getSubscriberLifecycleFacadeSessionId());
				outgoingSub.refresh(true);

			} else if (outgoingSub.getStatus() == STATUS_CANCELED) {
				// otherwise, throw an exception if current status is cancelled
				logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]. The subscriber is already cancelled.", new Object[] { activity,
						changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber() });
				throw new InterBrandPortRequestException(getApplicationMessage(InterBrandPortRequestException.ERR001), InterBrandPortRequestException.ERR001);
			}

			// comm suite logic, Retrieve the communication suite info and cancel the subscriber has any companion subscribers.
						
			CommunicationSuiteInfo commSuiteInfo = outgoingSub.getCommunicationSuite();				
						
			// check the account status if the outgoing subscriber is cancelled
			switch (outgoingSub.getAccountStatusChangeAfterCancel(commSuiteInfo)) {

			case AccountSummary.STATUS_SUSPENDED:
				// suspend the account if only suspended subscribers will be left after outgoing subscriber is cancelled
				activity = "suspend account on outgoing brand";
				changeContext.getAccountLifecycleFacade().suspendAccountForPortOut(outgoingSub.getBanId(), activityReasonCode, activityDate, "Y", commSuiteInfo,changeContext.getAccountLifecycleFacadeSessionId());

				logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]. The outgoing subscriber is [{}]",
						new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
								outgoingSub.toString() });

				// cancel the active subscriber on the old (outgoing) brand
				activity = "cancel active subscriber on outgoing brand";
				changeContext.getSubscriberLifecycleFacade().cancelPortedInSubscriber(outgoingSub.getBanId(), outgoingSub.getSubscriberId(), activityReasonCode, activityDate, "Y", true,
						changeContext.getCurrentSubscriber().getSubscriberId(), commSuiteInfo,true,header,changeContext.getSubscriberLifecycleFacadeSessionId());
				logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]. The outgoing subscriber is [{}]",
						new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
								outgoingSub.toString() });
				break;

			case AccountSummary.STATUS_CANCELED:
				// cancel the account if no active or suspended subscribers will be left after outgoing subscriber is cancelled
				activity = "cancel account on outgoing brand";
				
				changeContext.getAccountLifecycleFacade().cancelAccountForPortOut(outgoingSub.getBanId(), activityReasonCode, activityDate, "Y", true, commSuiteInfo, false,changeContext.getAccountLifecycleManagerSessionId());

				logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]. The outgoing subscriber is [{}]",
						new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
								outgoingSub.toString() });
				break;

			default:
				// otherwise, just cancel the outgoing subscriber
				activity = "cancel active subscriber on outgoing brand";
				changeContext.getSubscriberLifecycleFacade().cancelPortedInSubscriber(outgoingSub.getBanId(), outgoingSub.getSubscriberId(), activityReasonCode, activityDate, "Y", true,
						changeContext.getCurrentSubscriber().getSubscriberId(), commSuiteInfo,true,header,changeContext.getSubscriberLifecycleFacadeSessionId());
				logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]. The outgoing subscriber is [{}]",
						new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(),
								outgoingSub.toString() });
				break;
			}


			// call to release the TN resources after the old (outgoing) subscriber is cancelled
			if (outgoingSub.isPCS() && !ActivationPortInUtilities.isFutureDated(changeContext, activityDate)) {
				activity = "release TN";
				try {
					SubscriberLifecycleFacade subLifeCycleFacade = changeContext.getSubscriberLifecycleFacade();
					subLifeCycleFacade.releaseTNResources(outgoingSub.getPhoneNumber(), outgoingSub.getEquipment().getNetworkType());
					logger.debug("Flow 'activatePortIn.{}' succeeded for subscriber with " + "[ban: {}, id: {}, phone: {}]. The outgoing subscriber is [{}]",
							new Object[] { activity, changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(),
									changeContext.getCurrentSubscriber().getPhoneNumber(), outgoingSub.toString() });
				} catch (Throwable t) {
					// if the TN resource update fails, just log it and continue
					logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]. The exception {} is ignored", new Object[] { activity,
							changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
				}
			}

		} catch (Throwable t) {
			logger.error("Flow 'activatePortIn.{}' failed for subscriber with " + "[ban: {}, id: {}, phone: {}]. The error is {}.", new Object[] { activity,
					changeContext.getCurrentSubscriber().getBanId(), changeContext.getCurrentSubscriber().getSubscriberId(), changeContext.getCurrentSubscriber().getPhoneNumber(), t.getMessage() });
			if (t instanceof InterBrandPortRequestException) {
				throw (InterBrandPortRequestException) t;
			} else {
				throw new InterBrandPortRequestException(t, getApplicationMessage(InterBrandPortRequestException.ERR001), InterBrandPortRequestException.ERR001);
			}
		}
	}
	
	private String createPortRequest(String portProcess, int incomingBrandId, int outgoingBrandId) throws ApplicationException {
		
		String portRequestId = null;
		try {
			portRequestId = changeContext.getSubscriberLifecycleFacade().createPortInRequest(changeContext.getCurrentSubscriber().getDelegate(), portProcess, incomingBrandId, outgoingBrandId, null, null,
					changeContext.getRequestHeader().getApplicationName(), changeContext.getAuditInfo().getUserId(), changeContext.getPortRequest());			
			if (StringUtils.contains(portRequestId, "Not Eligible")) {
				throw new ApplicationException(portRequestId, "WNP createPortInRequest failed", "");
			}
			Long.parseLong(portRequestId);
			
		} catch (ApplicationException ae) {
			String msg = "Unable to create portIn request '" + portProcess + "' port process and '" + incomingBrandId + "' brand Id: " + ae.getMessage();
			logger.error(msg, ae);
			throw ae;
		} catch (NumberFormatException nfe) {
			String msg = "Unable to create portIn request '" + portProcess + "' port process and '" + incomingBrandId + "' brand Id: " + nfe.getMessage();
			logger.error(msg, nfe);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Unable to parse port request Id [" + portRequestId + "]", "", nfe);
		} catch (Throwable t) {
			String msg = "Unable to create portIn request '" + portProcess + "' port process and '" + incomingBrandId + "' brand Id: " + t.getMessage();
			logger.error(msg, t);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Create PortRequest failed for id [" + portRequestId + "]; cause: " + t.getMessage(), "",  t);
		}
		
		changeContext.setPortRequestId(portRequestId);
		return portRequestId;
	}
	
	private void validateMVNESubscriber() throws ApplicationException {
		updatePlatformId(); // the platform ID is determined and only available from PortRequestRetrieval service
		verifyPortRequest();
		changeContext.getSubscriberLifecycleFacade().validatePortInRequest(changeContext.getPortRequest(), changeContext.getRequestHeader().getApplicationName(),
				changeContext.getAuditInfo().getUserId());
	}
	
	private void updatePlatformId() throws ApplicationException {	
		// this method implementation can be simplified to check for the process type and set the platform ID in the code; however, for accuracy it should always
		// retrieve from the underlying service - if there's performance consideration, we may hard code the value
		PortRequestInfo portRequestInfo = changeContext.getPortRequest();
		if (portRequestInfo != null && !portRequestInfo.isPlatformIdUpdated() && portRequestInfo.getPhoneNumber() != null) {
			PortRequestInfo[] portRequestArray = null;
			portRequestArray = changeContext.getSubscriberLifecycleFacade().getCurrentPortRequestsByPhoneNumber(portRequestInfo.getPhoneNumber(), changeContext.getCurrentSubscriber().getBrandId());
			if (!ArrayUtils.isEmpty(portRequestArray)) {
				portRequestInfo.setPlatformId(portRequestArray[0].getPlatformId());
			}

		} else if (portRequestInfo == null) {
			getPortRequest();
		}
	}
	
	private void deactivateMVNESubcriber() throws ApplicationException {
		
		try {
			changeContext.getSubscriberLifecycleFacade().deactivateMVNESubcriber(changeContext.getPortRequest().getPhoneNumber());
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			logger.error("Failed to deactivate MVNE subscriber for phone number {}", changeContext.getPortRequest().getPhoneNumber());
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, t.getMessage(), "", t);
		}
	}
	
	private void verifyPortRequest() throws ApplicationException {
		
		PortRequestInfo portRequest = changeContext.getPortRequest();
		if (portRequest != null) {
			if (portRequest.getSubscriber() == null) {
				portRequest.setSubscriber(changeContext.getCurrentSubscriber().getDelegate());
			}
			if (portRequest.getAccount() == null) {
				portRequest.setAccount(changeContext.getCurrentAccount().getDelegate());
			}
			if (portRequest.getEquipment() == null) {
				portRequest.setEquipment(changeContext.getCurrentEquipment().getDelegate());
			}
		}
	}
	
	private PortRequest getPortRequest() throws ApplicationException {

		if (changeContext.getPortRequest() == null) {
			PortRequestInfo[] portRequestArray = null;
			portRequestArray = changeContext.getSubscriberLifecycleFacade().getCurrentPortRequestsByPhoneNumber(changeContext.getCurrentSubscriber().getPhoneNumber(),
					changeContext.getCurrentSubscriber().getBrandId());

			if (!ArrayUtils.isEmpty(portRequestArray)) {
				PortRequestInfo portRequest = portRequestArray[0];
				portRequest.setSubscriber(changeContext.getCurrentSubscriber().getDelegate());
				portRequest.setAccount(changeContext.getCurrentAccount().getDelegate());
				portRequest.setEquipment(changeContext.getCurrentEquipment().getDelegate());
				changeContext.setPortRequest(portRequest);
			}
		}

		return changeContext.getPortRequest();
	}
	
}