package com.telus.cmb.subscriber.utilities.port;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.hpa.info.RewardAccountInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.HSPASubscriberInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PCSSubscriberInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;

public class PortoutHelper {
	private static final Log logger = LogFactory.getLog(PortoutHelper.class);

	private EJBController ejbController;
	private int ban;
	private String phoneNumber;
	private Date logicalDate;
	private SubscriberLifecycleInfo subLifecycleInfo;
	private ClientIdentity clientIdentity;
	private String slcfSessionId;
	private Map<Object, String> sessionIdCache = new HashMap<Object, String>();
	
	public PortoutHelper(EJBController ejbController, int ban, String phoneNumber, Date logicalDate,
			SubscriberLifecycleInfo subLifecycleInfo, ClientIdentity clientIdentity, String slcfSessionId) {
		this.ejbController = ejbController;
		this.ban = ban;
		this.phoneNumber = phoneNumber;
		this.logicalDate = logicalDate;
		this.subLifecycleInfo = subLifecycleInfo;
		this.clientIdentity  = clientIdentity;
		this.slcfSessionId  = slcfSessionId;
	}
	
	public SubscriberLifecycleInfo getSubscriberLifecycleInfo() {
		return subLifecycleInfo;
	}
	
	public void cancelAdditionMsisdn(int ban, String phoneNumber, ApplicationException cause) {
		String methodName = "cancelAdditionMsisdn";
		try {
			int index = 0;
			AdditionalMsiSdnFtrInfo[] historyArray = null;
	        SearchResultByMsiSdn searchResult = getSubscriberLifecycleManager().searchSubscriberByAdditionalMsiSdn(phoneNumber, getSubscriberLifecycleManagerSessionId());
	        String subNumber = searchResult.getEffectiveFtr().getSubscriberNumber();
	        if (subNumber == null || subNumber.equals("")) {
	        	historyArray = searchResult.getAllFtrArray();
	        	if (historyArray != null) {
	        		Date last = java.sql.Date.valueOf("1990-01-01");
	        		for (int i = 0; i < searchResult.getAllFtrArray().length; i++) {
	        			if ((historyArray[i].getBan() == ban) && (historyArray[i].getFtrExpDate() != null)) {
	        				Date expDate = historyArray[i].getFtrExpDate();
	        				if (last.before(expDate)) {
	        					last = expDate;
	        					index = i;
	        				}
	        			}
	        		}
	        		subNumber = historyArray[index].getSubscriberNumber();
	        	}
	        }
	        if (subNumber == null || subNumber.equals("")) {
	        	logger.error(methodName + ": Subscriber not found. ");
	        } else {
	        	SubscriberInfo sub = getSubscriberLifecycleHelper().retrieveSubscriber(ban, subNumber);
	        	if ((sub.getStatus() == PortoutUtilities.SUB_STATUS_CANCEL) &&
	        			(sub.getPortType() != null && sub.getPortType().equals(PortoutUtilities.PORT_OUT_PORT_TYPE))) {
	        		AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo = new AdditionalMsiSdnFtrInfo[1];
	        		additionalMsiSdnFtrInfo[0] = new AdditionalMsiSdnFtrInfo();
	        		additionalMsiSdnFtrInfo[0] = historyArray[index];
	        		getSubscriberLifecycleManager().cancelAdditionalMsisdn(additionalMsiSdnFtrInfo, phoneNumber, getSubscriberLifecycleManagerSessionId());
	        	} else if ((sub.getStatus() == PortoutUtilities.SUB_STATUS_CANCEL) &&
	        			(sub.getPortType() == null || !sub.getPortType().equals(PortoutUtilities.PORT_OUT_PORT_TYPE))) {
	        		logger.error(methodName + ": Subscriber ["+subNumber+"("+ban+")]is cancelled. ");
	        	} else {
	        		AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo = new AdditionalMsiSdnFtrInfo[1];
	        		additionalMsiSdnFtrInfo[0] = new AdditionalMsiSdnFtrInfo();
	        		additionalMsiSdnFtrInfo[0] = searchResult.getEffectiveFtr();
	        		getSubscriberLifecycleManager().deleteMsisdnFeature(additionalMsiSdnFtrInfo[0], getSubscriberLifecycleManagerSessionId());
	        		getSubscriberLifecycleManager().cancelAdditionalMsisdn(additionalMsiSdnFtrInfo, phoneNumber, getSubscriberLifecycleManagerSessionId());
	        	}
	        }
		} catch(Throwable t) {
			logger.error(methodName + ": exception caught: " + t.getStackTrace());
		}
	}

	public void checkSubscriberStatusAndRestoreSuspendSubscriber(SubscriberInfo subInfo) throws ApplicationException {
		if (subInfo.getStatus() == PortoutUtilities.SUB_STATUS_CANCEL) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Subscriber is already cancelled.", "");
		} else if (PortoutUtilities.PORT_OUT_PORT_TYPE.equals(subInfo.getPortType())) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_PORTED_OUT, "Subscriber is ported out already as indicated by SUBSCRIBER.port_type = 'O'", "");
		} else if (subInfo.getStatus() == PortoutUtilities.SUB_STATUS_SUSPEND) {
			getSubscriberLifecycleFacade().restoreSuspendedSubscriber(subInfo, logicalDate, "POUT", "", false, slcfSessionId);
			subInfo.setStatus(PortoutUtilities.SUB_STATUS_ACTIVE);
			addWarning(ErrorCodes.SUBSCRIBER_SUSPENDED_STATUS, "Subscriber is in suspended state. Restored subscriber to active state before proceeding. Workaround for Amdocs defect.");
		}
	}
	
	public void updateAccountAndSubscriberForPortOut(
			SubscriberInfo subInfo, 
			char accStatus, 
			String portOutInd, 
			Date activityDate,
			CommunicationSuiteInfo commSuiteInfo) throws ApplicationException {
		
		if (accStatus == PortoutUtilities.ACCOUNT_STATUS_CANCELED) {
			cancelAccountForPortOut(activityDate, portOutInd, subInfo,commSuiteInfo); 
		} else {
			if (accStatus == PortoutUtilities.ACCOUNT_STATUS_SUSPENDED) {
				suspendAccountForPortOut(activityDate, portOutInd,commSuiteInfo);
			}
			cancelPortedInSubscriber(activityDate, portOutInd, subInfo,commSuiteInfo);
		}
	}
	
	
	public void suspendBeforeCancel(SubscriberInfo subInfo, AccountInfo accInfo, String portOutInd,CommunicationSuiteInfo commSuiteInfo) throws ApplicationException {
		int numberOfSubsToSuspend = 0;
		
		if (subInfo.getStatus() == Subscriber.STATUS_ACTIVE) {
			numberOfSubsToSuspend ++;
		}
		
		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == true) {
			numberOfSubsToSuspend += commSuiteInfo.getActiveCompanionCount();
		}
		
		if(PortoutUtilities.getAccountStatusChangeAfterSuspend(subInfo, accInfo, numberOfSubsToSuspend) == PortoutUtilities.ACCOUNT_STATUS_SUSPENDED) {
			suspendAccountForPortOut(logicalDate, portOutInd,commSuiteInfo);
			accInfo.setStatus(PortoutUtilities.ACCOUNT_STATUS_SUSPENDED);
		} else {
			suspendPortedInSubscriber(logicalDate, portOutInd,commSuiteInfo);
		}
		subInfo.setStatus(PortoutUtilities.SUB_STATUS_SUSPEND);
		// update the active and subscriber count based on suspend subscriber status, I would prefer to do retrieval again on Ban.
		accInfo.setAllActiveSubscribersCount(accInfo.getAllActiveSubscribersCount() - numberOfSubsToSuspend);
		accInfo.setAllSuspendedSubscribersCount(accInfo.getAllSuspendedSubscribersCount() + numberOfSubsToSuspend);
	}
	

	@SuppressWarnings("unchecked")
	public void closeHPAAccount(Date activityDate) {
		
		SubscriberInfo subscriberInfo = subLifecycleInfo.getOldSubscriberInfo();
		if (subscriberInfo.getBrandId() == Brand.BRAND_ID_KOODO && isFutureDated(activityDate) == false) {
			String message = "Executing PortoutHelper.closeHPAAccount for ban: " + subscriberInfo.getBanId() + ", phone: " + subscriberInfo.getPhoneNumber();
			try {
				List<RewardAccountInfo> rewardAccountList = getSubscriberLifecycleFacade().getHPAAccount(subscriberInfo.getBanId(), subscriberInfo.getPhoneNumber(), subscriberInfo.getSubscriptionId());
				if (CollectionUtils.isNotEmpty(rewardAccountList)) {
					getSubscriberLifecycleFacade().closeHPAAccount(subscriberInfo.getBanId(), subscriberInfo.getPhoneNumber(), subscriberInfo.getSubscriptionId(), false);
					logger.debug(message + ", succeeded.");
				} else {
					logger.debug(message + "; no HPA account found.");
					addWarning("300112", "HPA reward account not found.");
				}
			} catch (Throwable t) {
				logger.error(message + "; failed.", t);
				addWarning("300113", "PortoutHelper.closeHPAAccount: closing HPA reward account failed. " + t.getStackTrace());
			}
		} else if (isFutureDated(activityDate)) {
			logger.info("PortoutHelper.closeHPAAccount: by-passed the HPA webservice call for future dated koodo cancelation for phone number: " + subscriberInfo.getPhoneNumber());
		}
	}

	private boolean isFutureDated (Date refDate)  {
		if (refDate != null) {
			return DateUtil.isAfter(refDate, logicalDate);
		}		
		return false;
	}
	
	public void addWarning(String messageId, String warningMessage) {
		addWarning(messageId, warningMessage, null);
	}

	private void addWarning(String messageId, String warningMessage, List<String> variables) {
		WarningFaultInfo warningFaultInfo = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, SystemCodes.CMB_SLF_EJB, messageId, "CMB_SLCMS_0004", warningMessage, variables);
		subLifecycleInfo.addSystemWarning(warningFaultInfo);
	}
	
	private void cancelAccountForPortOut(Date activityDate, String portOutInd, SubscriberInfo subInfo,CommunicationSuiteInfo commSuiteInfo) throws ApplicationException {
		getAccountLifecycleFacade().cancelAccountForPortOut(ban, subLifecycleInfo.getReasonCode(), activityDate, portOutInd, false, commSuiteInfo, false,getAccountLifecycleManagerSessionId());
		releaseNetworkPhoneNumberAsNeeded(activityDate, subInfo);
	}
	
	
	private void suspendAccountForPortOut(Date activityDate, String portOutInd,CommunicationSuiteInfo commSuiteInfo) throws ApplicationException {
		getAccountLifecycleFacade().suspendAccountForPortOut(ban, subLifecycleInfo.getReasonCode(), activityDate, portOutInd, commSuiteInfo,getAccountLifecycleFacadeSessionId());
	}
	
	private void cancelPortedInSubscriber(Date activityDate, String portOutInd, SubscriberInfo subInfo,CommunicationSuiteInfo commSuiteInfo ) throws ApplicationException {
		try {
			getSubscriberLifecycleFacade().cancelPortedInSubscriber(ban, phoneNumber, subLifecycleInfo.getReasonCode(), activityDate, portOutInd, false, subInfo.getSubscriberId(), commSuiteInfo,false,null,slcfSessionId); 
	  		releaseNetworkPhoneNumberAsNeeded(activityDate, subInfo);
		} catch ( ApplicationException ae ) {
	  		if ("1111750".equals(ae.getErrorCode())) {
	  			cancelAccountForPortOut(activityDate, portOutInd, subInfo,commSuiteInfo);
	  		} else {
	  			throw ae;
	  		}
		}
	}
	
	/**
	 * This method should be used in the suspend before cancel flow
	 * 
	 * @param activityDate
	 * @param portOutInd
	 * @param commSuiteInfo
	 * @throws ApplicationException
	 */
	private void suspendPortedInSubscriber(Date activityDate, String portOutInd, CommunicationSuiteInfo commSuiteInfo) throws ApplicationException {

		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == false) {
			// step 1: Break the communication suite ( Phone and Watch Relation) prior to cancelling the companion subscribers
			getSubscriberLifecycleFacade().removeFromCommunicationSuite(ban, phoneNumber, commSuiteInfo, true);
		}

		getSubscriberLifecycleManager().suspendPortedInSubscriber(ban, phoneNumber, subLifecycleInfo.getReasonCode(), activityDate, portOutInd, getSubscriberLifecycleManagerSessionId());

		// step 2: Suspend the communicationSuite companion subscribers.
		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == true) {
			try {
				getSubscriberLifecycleFacade().suspendCommunicationSuiteCompanionSubs(ban, phoneNumber, commSuiteInfo, activityDate, subLifecycleInfo.getReasonCode(), "",
						getSubscriberLifecycleManagerSessionId());
			} catch (ApplicationException ae) {
				logger.error("error occured when suspending the CommunicationSuiteCompanionSubs , ban = [" + ban + "] ,phoneNumber = [" + phoneNumber + "],commSuiteInfo=["+commSuiteInfo, ae);
			}
		}
	}

	private void releaseNetworkPhoneNumberAsNeeded(Date activityDate, SubscriberInfo subInfo) {
		if (!activityDate.after(logicalDate)) {
			Equipment equipment = null;
			String phoneNumber = subInfo.getPhoneNumber();
			
			try {
				equipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(subInfo.getSerialNumber());
			}catch(Throwable t) {
				 logger.debug("Failing to retrieve subscriber ["+phoneNumber +"] equipment. "+t);
			}
			
			
			String networkType = null;

			if (equipment != null) {
				if (equipment.isHSPA()) {
					networkType = NetworkType.NETWORK_TYPE_HSPA;
				} else if (equipment.isCDMA()) {
					networkType = NetworkType.NETWORK_TYPE_CDMA;
				} else if (equipment.isIDEN()) {
					networkType = NetworkType.NETWORK_TYPE_IDEN;
				} else {
					throw new RuntimeException("Invalid network type for equipment [" + equipment + "]");
				}
			}else {
				if (subInfo instanceof HSPASubscriberInfo) {
					networkType = NetworkType.NETWORK_TYPE_HSPA;
				}else if (subInfo instanceof PCSSubscriberInfo) {
					networkType = NetworkType.NETWORK_TYPE_CDMA;
				}else if (subInfo instanceof IDENSubscriberInfo) {
					networkType = NetworkType.NETWORK_TYPE_IDEN;
				}else {
					throw new RuntimeException("Invalid network type for subscriber [" + subInfo.getClass() + "]");
				}
			}
			
			try {
				getSubscriberLifecycleFacade().releaseTNResources(phoneNumber, networkType);
			} catch (Exception e) {
				addWarning("300114", "Release phone number call to RCM failed. " + e.getStackTrace());
			}
		}
	}
	
	private SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleFacade.class);
	}

	private SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleHelper.class);
	}

	private SubscriberLifecycleManager getSubscriberLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleManager.class);
	}

	private AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}

	private AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}
	
	private ProductEquipmentHelper getProductEquipmentHelper() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentHelper.class);
	}

	private String getSubscriberLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(getSubscriberLifecycleManager());
	}
	
	private String getAccountLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleFacade());
	}
	
	private String getAccountLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleManager());
	}

	private String getSessionId(Object proxy) throws ApplicationException {
		String sessionId = sessionIdCache.get(proxy);
		if (sessionId == null) {
			try {
				Method method = proxy.getClass().getMethod("openSession", String.class, String.class, String.class);
				sessionId = (String) method.invoke(proxy, clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
			} catch (Exception e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}
		}
		return sessionId;
	}
}
