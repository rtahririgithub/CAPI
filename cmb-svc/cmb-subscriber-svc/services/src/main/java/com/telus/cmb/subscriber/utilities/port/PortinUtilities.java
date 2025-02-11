package com.telus.cmb.subscriber.utilities.port;

import java.text.DateFormat;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequest;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.PricePlanInfo;

/**
 * This class was created for refactoring PRM calls from provider to EJB. However, the solution
 * was undefined and so use of this class is obsoleted.
 * @author tongts
 *
 */

public class PortinUtilities {
	private static final Log logger = LogFactory.getLog(PortinUtilities.class);
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private static FieldPosition POSITION = new FieldPosition(0);

	public static final String CANCEL_AND_RESUBMIT_WPR = "RESB";

	/** The determinePortProcessType methods are commented out because they're not in use. They're ported from the provider
	 *  to the EJB. (June 11, 2012)
	 * @param portInEligibility
	 * @return
	 */
//	public static String determinePortProcessType(Brand[] brands, PortRequest portRequest) {	  
//		if (!AppConfiguration.isEagleRollback() && portRequest.isPortInFromMVNE()) {
//			return PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT;
//		} else 
//		if (portRequest.getIncomingBrandId()!= portRequest.getOutgoingBrandId() &&
//				ReferenceDataManager.Helper.validateBrandId(portRequest.getIncomingBrandId(), brands) &&
//				ReferenceDataManager.Helper.validateBrandId(portRequest.getOutgoingBrandId(), brands)) {
//			return PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT;
//		} else {
//			return PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
//		}	
//	}
//
//	public static String determinePortProcessType(Brand[] brands, PortInEligibility portInEligibility) {
//		if (portInEligibility != null) {
//			if (!AppConfiguration.isEagleRollback() && portInEligibility.isPortInFromMVNE()) {
//				return PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT;
//			} else 
//			if (ReferenceDataManager.Helper.validateBrandId(portInEligibility.getIncomingBrandId(), brands) &&
//					ReferenceDataManager.Helper.validateBrandId(portInEligibility.getOutgoingBrandId(), brands))
//				return PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT;
//			else
//				return PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;				  
//		} else {
//			return null;
//		}
//	}

	public static boolean isPortedIn(PortInEligibility portInEligibility) {
		return portInEligibility != null;
	}

	public static PortRequest getPortRequest(SubscriberLifecycleFacade subscriberLifecycleFacade, ReferenceDataFacade referenceDataFacade, PortRequestInfo portRequest ,SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentInfo equipmentInfo) throws ApplicationException {
		if (portRequest == null) {
			PortRequestInfo[] portRequestArray = subscriberLifecycleFacade.getCurrentPortRequestsByPhoneNumber(subscriberInfo.getPhoneNumber(), getBrandId(referenceDataFacade, subscriberInfo, accountInfo));
			if (portRequestArray != null && portRequestArray.length > 0) {
				portRequest = portRequestArray[0];
				portRequest.setSubscriber(subscriberInfo);
				portRequest.setAccount(accountInfo);
				portRequest.setEquipment(equipmentInfo);
			}
		}
		return portRequest;
	}
	
	public static String createPortRequest(SubscriberLifecycleFacade subscriberLifecycleFacade, SubscriberInfo subscriberInfo, String portProcess, int incomingBrandId, int outgoingBrandId, String applicationCode, String user, PortRequestInfo portRequestInfo) throws ApplicationException {
		String portRequestId = null;
		//		String portRequestId = getPortRequestSO().createPortInRequest(this, portProcess, incomingBrandId, outgoingBrandId, null, null);
		try {
			portRequestId = subscriberLifecycleFacade.createPortInRequest(subscriberInfo, portProcess, incomingBrandId, outgoingBrandId, null, null, applicationCode, user, portRequestInfo);
			Long.parseLong(portRequestId);
		} catch(NumberFormatException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Unable to parse port request Id [" + portRequestId + "]", "");
		}
		return portRequestId;
	}

	public static int getBrandId(ReferenceDataFacade referenceDataFacade, SubscriberInfo subscriberInfo, AccountInfo accountInfo) {
		try {
			if (!(ReferenceDataManager.Helper.validateBrandId(subscriberInfo.getBrandId(), referenceDataFacade.getBrands()))) {
				if (subscriberInfo.getPricePlan() != null) {
					PricePlanInfo pricePlanSummary = referenceDataFacade.getPricePlan(subscriberInfo.getPricePlan());
					return (pricePlanSummary != null) ? pricePlanSummary.getBrandId() : accountInfo.getBrandId();
				}else {
					return accountInfo.getBrandId();
				}
			}
		} catch (Throwable t) {
			PortinUtilities.logFailure("getBrandId", "retrieving price plan or account brand on new subscriber", subscriberInfo, t, "error retrieving price plan or account brand ID");
		}
		return subscriberInfo.getBrandId();
	}

	public static void logSuccess(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-succeeded; ");
		if (extraMessage != null)
			sb.append(extraMessage ).append(";");
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
	}

	public static void logFailure(String methodName, String activity, SubscriberInfo subInfo, Throwable t, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-failed; ");
		if (extraMessage != null)
			sb.append(extraMessage).append(";");
		appendSubscriberInfo(sb, subInfo);
		appendPRMFault(sb, t);
		logger.debug(sb.toString());
		logger.debug(t);
	}

	private static StringBuffer buildLogEntryHeader(String methodName, String activity) {
		StringBuffer sb = new StringBuffer();
		DATE_FORMAT.format(new Date(), sb, POSITION ).append(" ");
		sb.append("[").append(Thread.currentThread().getName()).append("] ");

		sb.append(methodName).append("():").append(activity);
		return sb;
	}

	private static StringBuffer appendSubscriberInfo(StringBuffer sb, SubscriberInfo subInfo) {
		if (subInfo != null) {
			sb.append("  subscriber[ban:").append(subInfo.getBanId())
			.append(", subId:").append( subInfo.getSubscriberId())
			.append(", phone:").append( subInfo.getPhoneNumber())
			.append("]");
		}
		return sb;
	}

	//extract the PRMFault message if possible
	private static void appendPRMFault(StringBuffer messageBuffer, Throwable t) {
		if (t != null) {
			
			//TODO: PS what exception to use?
			
//			Throwable cause = t.getCause();
//			if (cause != null && cause instanceof com.telus.ws.client.prm.type.CreatePortInRequestService.PRMFaultDetail) {
//				com.telus.ws.client.prm.type.CreatePortInRequestService.PRMFault fault =
//					((com.telus.ws.client.prm.type.CreatePortInRequestService.PRMFaultDetail) cause).getPRMFault();
//				if (fault != null) {
//					messageBuffer.append(", Exception cause is PRMFault [code=").append(fault.getCode())
//					.append( "; reason=").append(fault.getReason())
//					.append("].");
//				} else {
//					messageBuffer.append(", Exception cause is PRMFaultDetail, but unable to extract PRMFault is null");
//				}
//			}
		}
	}
	
}
