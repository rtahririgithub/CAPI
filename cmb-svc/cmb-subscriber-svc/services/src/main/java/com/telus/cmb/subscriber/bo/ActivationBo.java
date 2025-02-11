package com.telus.cmb.subscriber.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.IncompleteSubscriberCreationProcessException;
import com.telus.api.account.ServicesValidation;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.reference.NetworkType;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.ExceptionUtil;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.utilities.ActivationChangeContext;
import com.telus.cmb.subscriber.utilities.activation.ActivationUtilities;
import com.telus.cmb.subscriber.utilities.activation.PrepaidUtilities;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.framework.exception.CreateSubscriberException;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @Author Brandon Wen, R. Fong
 * 
 */
public class ActivationBo {
	
	private static final Log logger = LogFactory.getLog(ActivationBo.class);

	protected ActivationChangeContext changeContext;
	protected ActivationChangeInfo changeInfo;
	
	private boolean activate = true;

	public ActivationBo(ActivationChangeContext changeContext) {
		this.changeContext = changeContext;
		this.changeInfo = changeContext.getChangeInfo();
	}
	
	public SubscriberInfo activate() throws ApplicationException, TelusAPIException {
		return activateSubscriber(changeInfo.getActivationOption(), changeInfo.getServiceValidation());
	}
	
	// Updated for CDA phase 1B July 2018
	// Note: this code supports activations initiated through the web services activation flow. It DOES NOT support port-in activations, which have been implemented in the ActivatePortinBo flow.
	private SubscriberInfo activateSubscriber(ActivationOption selectedOption, ServicesValidation srvValidation) throws ApplicationException, TelusAPIException {
		
		changeContext.getCurrentSubscriber().getActivationOption().apply();
		srvValidation = srvValidation == null ? new ServicesValidationInfo() : srvValidation;		
		if (NetworkType.NETWORK_TYPE_HSPA.equals(changeContext.getCurrentEquipment().getNetworkType())) {
			((ServicesValidationInfo) srvValidation).setEquipmentServiceMatch(false);
		}
		ActivationUtilities.checkForVoicemailService(changeContext.getCurrentContract());
		// TODO commenting out prepaid logic only for October 2016 release
//		PrepaidUtilities.updatePrepaidSystem(changeContext, changeInfo.getTopUpPaymentArrangement());		
		CreateSubscriberException delayedCreateSubscriberException = createSubscriber(srvValidation);	
		SubscriberInfo newSubscriber = postCreateSubscriber();		
		if (delayedCreateSubscriberException != null) {
			throw new IncompleteSubscriberCreationProcessException(delayedCreateSubscriberException, delayedCreateSubscriberException.getIncompleteSteps());
		}
		
		return newSubscriber;
	}
	
	private CreateSubscriberException createSubscriber(ServicesValidation srvValidation) throws ApplicationException {
		
		CreateSubscriberException delayedCreateSubscriberException = null;
		try {
			changeContext.getSubscriberLifecycleFacade().createSubscriber(changeContext.getCurrentSubscriber().getDelegate(), changeContext.getCurrentContract().getDelegate(), activate,
					changeInfo.isWaiveSearchFee(), changeInfo.getActivationFeeChargeCode(), changeInfo.isDealerHasDeposit(), false, srvValidation, 
					PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, 0, null, changeContext.getSubscriberLifecycleFacadeSessionId());
			
			// Business Connect: isHSIAEquipment and isVOIPEquipment checks required to bypass assignTNResources in order to activate the subscriber with dummy equipment for 
			// Business Connect STARTER and OFFICE seats
			if (activate && changeContext.getCurrentSubscriber().isPCS() && !changeContext.getCurrentEquipment().isHSIADummyEquipment() && !changeContext.getCurrentEquipment().isVOIPDummyEquipment()) {
				try {
					changeContext.getSubscriberLifecycleFacade().assignTNResources(changeContext.getCurrentSubscriber().getPhoneNumber(), changeContext.getCurrentEquipment().getNetworkType(),
							changeContext.getCurrentEquipment().getProfile().getLocalIMSI(), changeContext.getCurrentEquipment().getProfile().getRemoteIMSI());
					logger.debug("[assignTNResources] for phone number [" + changeContext.getCurrentSubscriber().getPhoneNumber() + "], networkType [" + 
							changeContext.getCurrentEquipment().getNetworkType() + "]");
				} catch (Throwable t) {
					// Any errors that occur are to be caught and ignored. Failure to perform post-activation tasks should not impede the activation of a subscriber.
					logger.error("[assignTNResources] failed for phone number [" + changeContext.getCurrentSubscriber().getPhoneNumber() + "]; exception ignored.", t);
				}
			}
			
		} catch (ApplicationException ae) {
			if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_MEMOS_FEES_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.ALL_STEPS);
			}
			else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_FEES_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.ALL_STEPS_EXCEPT_MEMOS);
			}
			else if (ErrorCodes.CREATE_SUBSCRIBER_ERROR_DISCOUNTS.equals(ExceptionUtil.getErrorCode(ae))) {
				delayedCreateSubscriberException = new CreateSubscriberException(ae, CreateSubscriberException.APPLY_DISCOUNTS);
			}else{
				throw ae;
			}
		} catch(Throwable t) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB,ErrorCodes.CREATE_SUBSCRIBER_FAILED, t.getMessage(),"", t);
		}
		return delayedCreateSubscriberException;
	}
	
	private SubscriberInfo postCreateSubscriber() throws TelusAPIException, ApplicationException {
		
		try {
			if (activate && changeContext.getCurrentAccount().getStatus() == Account.STATUS_TENTATIVE && changeContext.getCurrentAccount().getActiveSubscribersCount() == 0) {
				changeContext.getAccountLifecycleManager().updateNationalGrowth(changeContext.getCurrentAccount().getBanId(), null, changeContext.getCurrentSubscriber().getMarketProvince(), 
						changeContext.getAccountLifecycleManagerSessionId());
			}
		} catch(Throwable t) {
			// Any errors that occur are to be caught and ignored. Failure to perform post-activation tasks should not impede the activation of a subscriber.
			logger.error("[updateNationalGrowth] " + t.getMessage(), t);
		}

		// Business Connect: isHSIAEquipment and isVOIPEquipment checks required to bypass asyncAssignEquipmentToPhoneNumber in order to activate the subscriber with dummy equipment for
		// Business Connect STARTER and OFFICE seats
		if (changeContext.getCurrentEquipment().isHSPA() && !changeContext.getCurrentEquipment().isHSIADummyEquipment() && !changeContext.getCurrentEquipment().isVOIPDummyEquipment()) {
			try {
				changeContext.getProductEquipmentLifecycleFacade().asyncAssignEquipmentToPhoneNumber(changeContext.getCurrentSubscriber().getPhoneNumber(), changeContext.getCurrentEquipment().getSerialNumber(),
						changeContext.getCurrentEquipment().getAssociatedHandsetIMEI());
			} catch (Throwable t) {
				// Any errors that occur are to be caught and ignored. Failure to perform post-activation tasks should not impede the activation of a subscriber.
				logger.error("[asyncAssignEquipmentToPhoneNumber] failed for phone number [" + changeContext.getCurrentSubscriber().getPhoneNumber() + "]; exception ignored.", t);
			}
		}

		ActivationUtilities.setSIMMuleRelation(activate, changeContext.getCurrentSubscriber().getStartServiceDate(), changeContext.getCurrentEquipment(), changeContext.getProductEquipmentManager());
		ActivationUtilities.updateSubscriptionRole(changeContext.getCurrentSubscriber().getDelegate(), changeInfo.getSubscriptionRoleCode(), changeContext.getClientIdentity().getPrincipal(), 
				changeContext.getSubscriberLifecycleManager());
		ActivationUtilities.createActivationMemo(changeContext.getCurrentSubscriber().getDelegate(), changeInfo.getMemoText(), changeContext.getAccountLifecycleFacade(),
				changeContext.getAccountLifecycleFacadeSessionId());
		SubscriberInfo newSubscriber = changeContext.getSubscriberLifecycleHelper().retrieveSubscriberByBanAndPhoneNumber(changeContext.getCurrentSubscriber().getBanId(), 
				changeContext.getCurrentSubscriber().getPhoneNumber());

		return newSubscriber;
	}
	
}