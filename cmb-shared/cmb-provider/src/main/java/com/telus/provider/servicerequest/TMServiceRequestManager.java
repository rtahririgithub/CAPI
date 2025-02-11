package com.telus.provider.servicerequest;

import java.sql.Timestamp;
import java.util.Date;

import com.telus.api.account.Address;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.PaymentMethod;
import com.telus.api.equipment.Equipment;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.servicerequest.ServiceRequestManager;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.servicerequest.TelusServiceRequestException;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.servicerequest.info.ServiceRequestNoteInfo;
import com.telus.eas.servicerequest.info.ServiceRequestParentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAddress;
import com.telus.provider.account.TMContractFeature;
import com.telus.provider.account.TMContractService;
import com.telus.provider.account.TMPaymentMethod;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;


/**
 * @author W. Kwok
 * @version 1.0, 02-Apr-2008
 */
public class TMServiceRequestManager extends BaseProvider implements ServiceRequestManager {
	
	private static final long serialVersionUID = 1L;
	
//	private static final String SRPDS_FAIL_ON_PERSISTENCE_ERROR_KEY="isSRPDSFailOnPersistenceError";

	  
	public TMServiceRequestManager(TMProvider provider) {
		super(provider);
	}
	
	
	/* *************************************************************** */
	/* public methods available in the API							   */
	/* *************************************************************** */
	
	/* all parameters are mandatory */
	public ServiceRequestNote newServiceRequestNote(long noteTypeId, String noteText) throws TelusServiceRequestException {
		ServiceRequestNoteInfo noteInfo = new ServiceRequestNoteInfo();
		
		if (noteTypeId <= 0)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);
		
		noteInfo.setNoteTypeId(noteTypeId);
		
		if (noteText == null || noteText.equals(""))
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		noteInfo.setNoteText(noteText);
		
		return noteInfo;
	}
	
	/* all parameters are mandatory */
	public ServiceRequestParent newServiceRequestParent(long parentId, Timestamp timestamp, long relationshipTypeId) throws TelusServiceRequestException {
		ServiceRequestParentInfo parentInfo = new ServiceRequestParentInfo();
		
		if (parentId <= 0)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);
		
		parentInfo.setParentId(parentId);
		
		if (timestamp == null)
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		parentInfo.setTimestamp(timestamp);
		
		if (relationshipTypeId <= 0) 
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001);

		parentInfo.setRelationshipTypeId(relationshipTypeId);
		
		return parentInfo;
	}

	/* only language code and application id are mandatory */
	public ServiceRequestHeader newServiceRequestHeader(String languageCode, long applicationId, String referenceNumber, ServiceRequestParent parentRequest, ServiceRequestNote note) throws TelusServiceRequestException {
		ServiceRequestHeaderInfo headerInfo = new ServiceRequestHeaderInfo();
		
		if (languageCode == null || languageCode.equals("")) {
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001, "Invalid languageCode["+languageCode+"]");
		}

		headerInfo.setLanguageCode(languageCode);
		
		if (applicationId <= 0) {
			throw new TelusServiceRequestException(TelusServiceRequestException.ERR001, "Invalid application ID["+applicationId+"]");
		}

		headerInfo.setApplicationId(applicationId);
		headerInfo.setApplicationName(getProvider().getApplication());
		headerInfo.setReferenceNumber(referenceNumber);
		headerInfo.setServiceRequestParent(parentRequest);
		headerInfo.setServiceRequestNote(note);
		
		return headerInfo;
	}
	
	public void reportPortCancel(int banId, String subscriberId, String dealerCode, String salesRepCode, String user, String phoneNumber, char oldSubscriberStatus, char newSubscriberStatus, String deactivationReason, Date activityDate, ServiceRequestHeader header) {
		if (header != null && AppConfiguration.isSRPDSEnabled()==true)
			reportChangeSubscriberStatus(banId, subscriberId, dealerCode, salesRepCode, user, phoneNumber, oldSubscriberStatus, newSubscriberStatus, deactivationReason, activityDate, header);
	}

	/* *************************************************************** */
	/* public methods available in the Provider 					   */
	/* *************************************************************** */
	
	public void reportChangeEquipment(int banId, String subscriberId, String dealerCode, 
			String salesRepCode, String userId, Equipment oldEquipment, Equipment newEquipment, String repairId, 
			String swapType, Equipment oldAssociatedMuleEquipment, Equipment newAssociatedMuleEquipment, 
			com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
		
			Logger.debug("Entering method TMServiceRequestManager.reportChangeEquipment()...");
			TMEquipment oldTMEquipment = (TMEquipment)oldEquipment;
			TMEquipment newTMEquipment = (TMEquipment)newEquipment;
			TMEquipment oldAssociatedTMMuleEquipment = (TMEquipment)oldAssociatedMuleEquipment;
			TMEquipment newAssociatedTMMuleEquipment = (TMEquipment)newAssociatedMuleEquipment;
			
			provider.getSubscriberLifecycleFacade().reportChangeEquipment(banId, subscriberId, dealerCode, salesRepCode, userId, oldTMEquipment!=null?oldTMEquipment.getDelegate():null, 
					newTMEquipment!=null?newTMEquipment.getDelegate():null, repairId, swapType, oldAssociatedTMMuleEquipment!=null?oldAssociatedTMMuleEquipment.getDelegate():null, newAssociatedTMMuleEquipment!=null?newAssociatedTMMuleEquipment.getDelegate():null, header);

			Logger.debug("Exiting method TMServiceRequestManager.reportChangeEquipment()");
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeEquipment(): [" + t + "]");
			Logger.warning( t );
		}
	}
	
	public void reportChangeContract(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, SubscriberContractInfo newContractInfo, SubscriberContractInfo oldContractInfo, 
			ContractService[] addedServices, ContractService[] removedServices, ContractService[] updatedServices, ContractFeature [] updatedFeatures, com.telus.api.servicerequest.ServiceRequestHeader header) {
		
		try {
			Logger.debug("Entering method TMServiceRequestManager.reportChangeContract()...");
			
			provider.getSubscriberLifecycleFacade().reportChangeContract(banId, subscriberId, dealerCode, salesRepCode, userId,
					newContractInfo, oldContractInfo, 
					TMContractService.undecorate( addedServices ), 
					TMContractService.undecorate( removedServices ), 
					TMContractService.undecorate( updatedServices), 
					TMContractFeature.undecorate( updatedFeatures), 
					header);
			
			Logger.debug("Exiting method TMServiceRequestManager.reportChangeContract()...");
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeContract - [" + t + "]");		
			Logger.warning( t );
		}
	}

	public void reportChangePhoneNumber(int banId, String subscriberId, String newSubscriberId, String dealerCode, String salesRepCode, String userId, String oldPhoneNumber, String newPhoneNumber, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			Logger.debug("Entering method TMServiceRequestManager.reportChangePhoneNumber()...");
			
			provider.getSubscriberLifecycleFacade().reportChangePhoneNumber(banId, subscriberId, newSubscriberId, dealerCode,
					salesRepCode, userId, oldPhoneNumber, newPhoneNumber, header);
						
			Logger.debug("Exiting method TMServiceRequestManager.reportChangePhoneNumber().");
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangePhoneNumber(): [" + t + "]");
			Logger.warning( t );
		}
	}

	public void reportChangeSubscriberStatus(int banId, SubscriberInfo subscriberinfo, String dealerCode, String salesRepCode, String userId, 
			char oldSubscriberStatus, char newSubscriberStatus, String reason, Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
				
			provider.getSubscriberLifecycleFacade().reportChangeSubscriberStatus(banId, subscriberinfo, dealerCode, 
					salesRepCode, userId, oldSubscriberStatus, newSubscriberStatus, reason, activityDate, header);
			
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeSubscriberStatus(): Unable to write subscriber status change service request to SRPDS - [" + t + "]");
			Logger.warning( t );
		}
	}
	
	public void reportChangeSubscriberStatus(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, String phoneNumber, char oldSubscriberStatus, char newSubscriberStatus, String reason, Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			Logger.debug("Entering method TMServiceRequestManager.reportChangeSubscriberStatus()...");
			
			SubscriberInfo subscriberInfo = provider.getSubscriberLifecycleHelper().retrieveSubscriber(banId, subscriberId);
			reportChangeSubscriberStatus(banId, subscriberInfo, dealerCode, salesRepCode, userId, oldSubscriberStatus, 
					newSubscriberStatus, reason, activityDate, header);
			
			Logger.debug("Exiting method TMServiceRequestManager.reportChangeSubscriberStatus()...");
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeSubscriberStatus(): Unable to write subscriber status change service request to SRPDS - [" + t + "]");
			Logger.warning( t );
		}
	}
	
	/* The reportMoveSubscriber method will handle writing service requests to SRPDS     */
	/* for subscriber move and TOWN.	                                  				 */
	/* MOVE: 1 ServiceRequestHeader + 2 SubscriptionStatusItem objects		             */
	/* ERROR: All errors will be captured and failed silently; exception will be written */
	/*        to log files.																 */
	public void reportMoveSubscriber(int oldBanId, int newBanId, String subscriberId, String dealerCode, String salesRepCode, 
			String userId, String phoneNumber, char subscriberStatus, Date subscriberActivationDate, String reason, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			provider.getSubscriberLifecycleFacade().reportMoveSubscriber(oldBanId, newBanId, subscriberId, dealerCode, salesRepCode, 
					userId, phoneNumber, subscriberStatus, subscriberActivationDate, reason, header);
		} catch (Throwable t) {
			Logger.warning("ERROR: reportMoveSubscriber(): Unable to write move subscriber service request to SRPDS - [" + t + "]");
			Logger.warning( t );
		}
	}
	
	public void reportChangeAccountType(int banId, String dealerCode, String salesRepCode, String userId, char accountStatus, char oldAccountType, char newAccountType, char oldAccountSubType, char newAccountSubType, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			provider.getSubscriberLifecycleFacade().reportChangeAccountType(banId, dealerCode, salesRepCode, userId, accountStatus, oldAccountType, newAccountType, oldAccountSubType, newAccountSubType, header);
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeAccountType(): [" + t + "]");
			Logger.warning( t );
		}
	}

	public void reportChangeAccountAddress(int banId, String dealerCode, String salesRepCode, String userId, Address address, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			provider.getSubscriberLifecycleFacade().reportChangeAccountAddress(banId, dealerCode, salesRepCode, userId, ((TMAddress)address).getDelegate(), header);
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeAccountAddress(): [" + t + "]");
			Logger.warning( t );
		}
	}
	
	public void reportChangeAccountPin(int banId, String dealerCode, String salesRepCode, String userId, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			provider.getSubscriberLifecycleFacade().reportChangeAccountPin(banId, dealerCode, salesRepCode, userId, header);
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangeAccountPin(): [" + t + "]");
			Logger.warning( t );
		}
	}

	public void reportChangePaymentMethod(int banId, String dealerCode, String salesRepCode, String userId, PaymentMethod paymentMethod, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			provider.getSubscriberLifecycleFacade().reportChangePaymentMethod(banId, dealerCode, salesRepCode, userId, ((TMPaymentMethod)paymentMethod).getDelegate(), header);
		} catch (Throwable t) {
			Logger.warning("ERROR: reportChangePaymentMethod(): [" + t + "]");
			Logger.warning( t );
		}
	}
}
