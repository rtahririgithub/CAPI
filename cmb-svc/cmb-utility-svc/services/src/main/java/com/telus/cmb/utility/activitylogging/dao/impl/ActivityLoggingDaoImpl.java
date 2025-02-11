package com.telus.cmb.utility.activitylogging.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.ServiceRequestNoteType;
import com.telus.api.reference.ServiceRequestRelationshipType;
import com.telus.api.servicerequest.ServiceRequestNote;
import com.telus.api.servicerequest.ServiceRequestParent;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.utility.activitylogging.dao.ActivityLoggingDao;
import com.telus.cmb.utility.activitylogging.svc.impl.ActivityLoggingServiceImpl;
import com.telus.cmb.utility.utilities.AppConfiguration;
import com.telus.eas.activitylog.domain.AbstractLoggableActivity;
import com.telus.eas.activitylog.domain.ActivityLogRecordIdentifier;
import com.telus.eas.activitylog.domain.ActivityLoggingResult;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity.AddressHolder;
import com.telus.eas.activitylog.domain.ChangeAccountPinActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity.ContractHolder;
import com.telus.eas.activitylog.domain.ChangeContractActivity.FeatureHolder;
import com.telus.eas.activitylog.domain.ChangeContractActivity.ServiceHolder;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity.EquipmentHolder;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.utility.info.ServiceRequestNoteTypeInfo;
import com.telus.eas.utility.info.ServiceRequestRelationshipTypeInfo;
import com.telus.srps.domain.reference.NoteType;
import com.telus.srps.domain.reference.RequestRelationshipType;
import com.telus.srps.domain.request.Actor;
import com.telus.srps.domain.request.Note;
import com.telus.srps.domain.request.ParentRequestPK;
import com.telus.srps.domain.request.RequestPK;
import com.telus.srps.domain.request.ServiceRequest;
import com.telus.srps.domain.request.ServiceRequestFactory;
import com.telus.srps.domain.request.ServiceRequestHeader;
import com.telus.srps.domain.request.ServiceRequestItem;
import com.telus.srps.domain.request.Status;
import com.telus.srps.domain.request.impl.ServiceRequestFactoryImpl;
import com.telus.srps.domain.request.item.AccountProfileItem;
import com.telus.srps.domain.request.item.ClientAccountStatusItem;
import com.telus.srps.domain.request.item.ContractItem;
import com.telus.srps.domain.request.item.ResourceItem;
import com.telus.srps.domain.request.item.ServiceFeatureItem;
import com.telus.srps.domain.request.item.ServicePackageItem;
import com.telus.srps.domain.request.item.SubscriberEquipmentItem;
import com.telus.srps.domain.request.item.SubscriptionStatusItem;
import com.telus.srps.domain.request.item.impl.SubscriberEquipmentItemImpl;
import com.telus.srps.services.reference.ReferenceDataProvider;
import com.telus.srps.services.request.RequestPersistenceService;


public class ActivityLoggingDaoImpl implements ActivityLoggingDao {

	private static final String TRANSACTION_NAME_EQUIPMENT_CHANGE = "EQUIPMENT CHANGE";
	private static final String TRANSACTION_NAME_PRICE_PLAN_CHANGE = "PRICE PLAN CHANGE";
	private static final String TRANSACTION_NAME_SERVICE_CHANGE = "SERVICE CHANGE";
	private static final String TRANSACTION_NAME_TERM_CHANGE = "TERM CHANGE";
	private static final String TRANSACTION_NAME_PHONE_NUMBER_CHANGE = "PHONE NUMBER CHANGE";
	private static final String TRANSACTION_NAME_CANCEL = "CANCEL";
	private static final String TRANSACTION_NAME_MOVE = "MOVE";
	private static final String TRANSACTION_NAME_ACTIVATE_FROM_RESERVE = "ACTIVATE FROM RESERVE";
	private static final String TRANSACTION_NAME_ACCOUNT_TYPE_CHANGE = "ACCOUNT TYPE CHANGE";
	private static final String TRANSACTION_NAME_RESTORE_CANCEL = "RESTORE CANCEL";
	private static final String TRANSACTION_NAME_ACCOUNT_ADDRESS_CHANGE = "ACCOUNT ADDRESS CHANGE";
	private static final String TRANSACTION_NAME_ACCOUNT_PIN_CHANGE = "ACCOUNT PIN CHANGE";
	private static final String TRANSACTION_NAME_PAYMENT_METHOD_CHANGE = "PAYMENT METHOD CHANGE";
	private static final String TRANSACTION_NAME_FEATURE_CHANGE = "FEATURE CHANGE";

	private static final String TRANSACTION_DESC_EQUIPMENT_CHANGE = "Telus API - Equipment Change";
	private static final String TRANSACTION_DESC_PRICE_PLAN_CHANGE = "Telus API - Price Plan Change";
	private static final String TRANSACTION_DESC_SERVICE_CHANGE = "Telus API - Service Change";
	private static final String TRANSACTION_DESC_TERM_CHANGE = "Telus API - Term Change";
	private static final String TRANSACTION_DESC_PHONE_NUMBER_CHANGE = "Telus API - Phone Number Change";
	private static final String TRANSACTION_DESC_CANCEL = "Telus API - Cancel";
	private static final String TRANSACTION_DESC_MOVE = "Telus API - Move";
	private static final String TRANSACTION_DESC_ACTIVATE_FROM_RESERVE = "Telus API - Activate From Reserve";
	private static final String TRANSACTION_DESC_ACCOUNT_TYPE_CHANGE = "Telus API - Account Type Change";
	private static final String TRANSACTION_DESC_RESTORE_CANCEL = "Telus API - Restore Cancel";
	private static final String TRANSACTION_DESC_ACCOUNT_ADDRESS_CHANGE = "Telus API - Account Address Change";
	private static final String TRANSACTION_DESC_ACCOUNT_PIN_CHANGE = "Telus API - Account PIN Change";
	private static final String TRANSACTION_DESC_PAYMENT_METHOD_CHANGE = "Telus API - Payment Method Change";
	private static final String TRANSACTION_DESC_FEATURE_CHANGE = "Telus API - Feature Change";

	private static final int EVENT_EXCHANGE = 3;
	private static final int EVENT_MOVE = 17;
	private static final int EVENT_CANCEL = 18;
	private static final int EVENT_ACTIVATE_FROM_RESERVE = 15;
	private static final int EVENT_RESTORE_CANCEL = 19;
	private static final int EVENT_ACCOUNT_PROFILE = 21;
	private static final int EVENT_SERVICE_FEATURE = 22;
	
	private static final int ACTOR_ROLE_TARGET = 8;
	private static final int ACTOR_ROLE_NEW_TARGET = 12;
	private static final int ACTOR_ROLE_OF_RECORD = 10;
	private static final int ACTOR_ROLE_PROCESSOR = 2;
	
	private static final int ACTOR_TYPE_KB_OPERATOR_ID = 4;
	private static final int ACTOR_TYPE_KB_DEALER_CD = 15;
	private static final int ACTOR_TYPE_KB_SALES_REP_PIN = 14;
	private static final int ACTOR_TYPE_KB_ACCOUNT = 9;
	private static final int ACTOR_TYPE_KB_SUBSCRIBER = 8;
	
	private static final int STATUS_COMPLETED = 10;
	
	private static final int ACTION_TYPE_CREATE = 1;
	private static final int ACTION_TYPE_REMOVE = 2;
	private static final int ACTION_TYPE_UPDATE = 3;
	
	private static final int PRIORITY_MEDIUM = 2;
	
	private static final int PROVISIONING_TYPE_PROVISIONABLE = 1;
	private static final int PROVISIONING_TYPE_NON_PROVISIONABLE = 2;
	
	private static final int BUSINESS_AREA_TYPE_ACCOUNT = 1;
	private static final int BUSINESS_AREA_TYPE_SUBSCRIBER = 2;
	
	private static final Long ACCOUNT_PROFILE_ADDRESS_CHANGE = new Long(2);
	private static final Long ACCOUNT_PROFILE_PIN_CHANGE = new Long(1);
	private static final Long ACCOUNT_PROFILE_PAYMENT_METHOD_CHANGE = new Long(3);

	private static final Long OBJECT_VERSION_ID = new Long(1); 
	
	private RequestPersistenceService requestPersistenceService = null;
	
	private ReferenceDataProvider referenceDataProvider = null;
	
	private ServiceRequestFactory requestFactory = null;

	private static final Logger LOGGER = Logger.getLogger(ActivityLoggingServiceImpl.class);
	
	private final Object lockA = new Object();
	private final Object lockB = new Object();
	
	public ActivityLoggingDaoImpl() throws Exception {
		requestFactory = new ServiceRequestFactoryImpl();
	}
	
	public void setRequestPersistenceService(RequestPersistenceService requestPersistenceService) {
		this.requestPersistenceService = requestPersistenceService;
	}
	
	public void setRequestFactory(ServiceRequestFactory requestFactory) {
		this.requestFactory = requestFactory;
	}	
		
	private interface ActivityLoggingCallback {
		void log(ActivityLoggingResult result);
	}
	
	private ActivityLoggingResult log(AbstractLoggableActivity activity, ActivityLoggingCallback callback) {
		
		ActivityLoggingResult result = new ActivityLoggingResult();
		if (activity.getServiceRequestHeader() != null) {
			result.setApplicationName(((ServiceRequestHeaderInfo) activity.getServiceRequestHeader()).getApplicationName());
		}
		String activityName = activity.getName();
		
		try {

			LOGGER.debug("Logging " + activityName + " activity...");

			callback.log(result);
			
			LOGGER.debug("Activity " + activityName + " logged successfully.");
			
		} catch (Throwable t) {
			LOGGER.error("Error logging " + activityName + " activity: " + t.getMessage(), t);
			
			ApplicationException ae=new ApplicationException(SystemCodes.CMB_ALS_DAO, t.getMessage(), "", t);
			result.addActivityRecordLoggingResult(activityName + " activity", activityName + " logging error", ae);
		}
		
		return result;
	}
	
	@Override
	public ActivityLoggingResult logChangeAccountAddressActivity(final ChangeAccountAddressActivity activity) {		
		return log(activity, new ActivityLoggingCallback(){
			
			public void log(ActivityLoggingResult result) {

				Actor [] actors = createActors(activity, activity.getBanId());

				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_ACCOUNT_PROFILE, 
						TRANSACTION_NAME_ACCOUNT_ADDRESS_CHANGE, TRANSACTION_DESC_ACCOUNT_ADDRESS_CHANGE);

				createAccountProfileItem(ACTION_TYPE_UPDATE, 
						TRANSACTION_NAME_ACCOUNT_ADDRESS_CHANGE, TRANSACTION_DESC_ACCOUNT_ADDRESS_CHANGE, 
						activity.getAddress(), activity.getBanId(), 1, serviceRequest);

				submitServiceRequest(serviceRequest, result);
			}
		});
	}
	
	
	@Override
	public ActivityLoggingResult logChangeAccountPinActivity(final ChangeAccountPinActivity activity) {
		return log(activity, new ActivityLoggingCallback(){
			
			public void log(ActivityLoggingResult result) {
				
				Actor [] actors = createActors(activity, activity.getBanId());

				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_ACCOUNT_PROFILE, 
						TRANSACTION_NAME_ACCOUNT_PIN_CHANGE, TRANSACTION_DESC_ACCOUNT_PIN_CHANGE);

				createAccountProfileItem(ACTION_TYPE_UPDATE, TRANSACTION_NAME_ACCOUNT_PIN_CHANGE, TRANSACTION_DESC_ACCOUNT_PIN_CHANGE, 
						activity.getBanId(), 1, serviceRequest);

				submitServiceRequest(serviceRequest, result);
			}
		});
	}
	
	
	@Override
	public ActivityLoggingResult logChangeAccountTypeActivity(final ChangeAccountTypeActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {

				Actor [] actors = createActors(activity, activity.getBanId());
				
				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
						TRANSACTION_NAME_ACCOUNT_TYPE_CHANGE, TRANSACTION_DESC_ACCOUNT_TYPE_CHANGE);
				
				/* create item to record old account type/sub type */
				
				createClientAccountStatusItem(ACTION_TYPE_REMOVE, TRANSACTION_NAME_ACCOUNT_TYPE_CHANGE + " - OLD", 
						TRANSACTION_DESC_ACCOUNT_TYPE_CHANGE + " - Old", activity.getBanId(), 
						activity.getOldAccountType(), activity.getOldAccountSubType(), 1, serviceRequest);
				
				/* create item to record new account type/sub type */
				
				createClientAccountStatusItem(ACTION_TYPE_CREATE, TRANSACTION_NAME_ACCOUNT_TYPE_CHANGE + " - NEW", 
						TRANSACTION_DESC_ACCOUNT_TYPE_CHANGE + " - New", activity.getBanId(), 
						activity.getNewAccountType(), activity.getNewAccountSubType(), 1, serviceRequest);
				
				submitServiceRequest(serviceRequest, result);
			}
		});
	}	
	
	@Override
	public ActivityLoggingResult logChangeContractActivity(final ChangeContractActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				
				Actor [] actors = createActors(activity, activity.getBanId(), activity.getSubscriberId());

				ContractHolder oldContract = activity.getOldContract();
				ContractHolder newContract = activity.getNewContract();
				
				// price plan has changed
				
				if (!oldContract.getPricePlan().getCode().equals(newContract.getPricePlan().getCode())) {
					
					ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
							TRANSACTION_NAME_PRICE_PLAN_CHANGE, TRANSACTION_DESC_PRICE_PLAN_CHANGE);
					
					createServicePackageItem(ACTION_TYPE_REMOVE, TRANSACTION_NAME_PRICE_PLAN_CHANGE + " - OLD", 
							TRANSACTION_DESC_PRICE_PLAN_CHANGE + " - Old", oldContract.getPricePlan(), 
							oldContract.getEffectiveDate(), oldContract.getExpiryDate(), 1, serviceRequest);

					createServicePackageItem(ACTION_TYPE_CREATE, TRANSACTION_NAME_PRICE_PLAN_CHANGE + " - NEW", 
							TRANSACTION_DESC_PRICE_PLAN_CHANGE + " - New", newContract.getPricePlan(), 
							newContract.getEffectiveDate(), newContract.getExpiryDate(), 2, serviceRequest);
					
					submitServiceRequest(serviceRequest, result);
				}
				
				// services has changed
				
				Collection<ServiceHolder> addedServices = activity.getAddedServices();
				Collection<ServiceHolder> removedServices = activity.getRemovedServices();
				Collection<ServiceHolder> updatedServices = activity.getUpdatedServices();
				
				if (!addedServices.isEmpty() || !removedServices.isEmpty() || !updatedServices.isEmpty()) {
					
					ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
							TRANSACTION_NAME_SERVICE_CHANGE, TRANSACTION_DESC_SERVICE_CHANGE);
					
					int sequence = 1;
					
					sequence = createServicePackageItems(ACTION_TYPE_CREATE, TRANSACTION_NAME_SERVICE_CHANGE + " - ADD",
							TRANSACTION_DESC_SERVICE_CHANGE + " - Add", addedServices, sequence, serviceRequest);

					sequence = createServicePackageItems(ACTION_TYPE_REMOVE, TRANSACTION_NAME_SERVICE_CHANGE + " - REMOVE",
							TRANSACTION_DESC_SERVICE_CHANGE + " - Remove", removedServices, sequence, serviceRequest);
					
					sequence = createServicePackageItems(ACTION_TYPE_UPDATE, TRANSACTION_NAME_SERVICE_CHANGE + " - UPDATE",
							TRANSACTION_DESC_SERVICE_CHANGE + " - Update", updatedServices, sequence, serviceRequest);
					
					submitServiceRequest(serviceRequest, result);
				}
				
				// features has changed
				if (!activity.getUpdatedFeatures().isEmpty()) {
					
					ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_SERVICE_FEATURE, 
							TRANSACTION_NAME_FEATURE_CHANGE, TRANSACTION_DESC_FEATURE_CHANGE);
					
					int sequence = 1;
					
					for (FeatureHolder feature : (Collection<FeatureHolder>)activity.getUpdatedFeatures()) {
						createServiceFeatureItem(ACTION_TYPE_UPDATE, TRANSACTION_NAME_FEATURE_CHANGE + " - UPDATE", TRANSACTION_DESC_FEATURE_CHANGE + " - Update", feature, sequence++, serviceRequest);
					}
					
					submitServiceRequest(serviceRequest, result);
				}
				
				// term has changed
				
				boolean startDateChanged = true;
				if (oldContract.getCommitmentStartDate() == null && newContract.getCommitmentStartDate() == null) {
					startDateChanged = false;
				} else if (oldContract.getCommitmentStartDate() != null && newContract.getCommitmentStartDate() != null) {
					startDateChanged = !oldContract.getCommitmentStartDate().equals(newContract.getCommitmentStartDate());
				}
				
				boolean durationChanged = newContract.getCommitmentMonths() != oldContract.getCommitmentMonths();
				
				if (startDateChanged || durationChanged) {
					ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
							TRANSACTION_NAME_TERM_CHANGE, TRANSACTION_DESC_TERM_CHANGE);
					
					createContractItem(ACTION_TYPE_REMOVE, TRANSACTION_NAME_TERM_CHANGE + " - OLD", 
							TRANSACTION_DESC_TERM_CHANGE + " - Old", activity.getOldContract(), 1, serviceRequest);
					
					createContractItem(ACTION_TYPE_CREATE, TRANSACTION_NAME_TERM_CHANGE + " - NEW", 
							TRANSACTION_DESC_TERM_CHANGE + " - New", activity.getNewContract(), 2, serviceRequest);
					
					submitServiceRequest(serviceRequest, result);
				}
			}
		});
	}
	
	private int createServicePackageItems(int actionTypeId, String name, String description, Collection<ServiceHolder> services, int sequence, ServiceRequest serviceRequest) {
		for (ServiceHolder service : services) {
			createServicePackageItem(actionTypeId, name, description, service, service.getEffectiveDate(), 
					service.getExpiryDate(), sequence++, serviceRequest);
		}
		return sequence;
	}

	@Override
	public ActivityLoggingResult logChangeEquipmentActivity(final ChangeEquipmentActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				Actor [] actors = createActors(activity, activity.getBanId(), activity.getSubscriberId());
				
				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
						TRANSACTION_NAME_EQUIPMENT_CHANGE, TRANSACTION_DESC_EQUIPMENT_CHANGE);
				
				int sequence = 1;
				
				if (activity.getOldEquipment() != null) {
					createSubscriberEquipmentItem(ACTION_TYPE_REMOVE, 
							TRANSACTION_NAME_EQUIPMENT_CHANGE + " - OLD - PRIMARY", 
							TRANSACTION_DESC_EQUIPMENT_CHANGE + " - Old - Primary", 
							activity.getOldEquipment(), activity.getSwapType(), activity.getRepairId(), 
							true, false, sequence++, serviceRequest);
				}
				
				if (activity.getOldAssociatedMuleEquipment() != null) {
					createSubscriberEquipmentItem(ACTION_TYPE_REMOVE, 
							TRANSACTION_NAME_EQUIPMENT_CHANGE + " - OLD - MULE", 
							TRANSACTION_DESC_EQUIPMENT_CHANGE + " - Old - Mule", 
							activity.getOldAssociatedMuleEquipment(), activity.getSwapType(), activity.getRepairId(), 
							false, true, sequence++, serviceRequest);
				}
				
				if (activity.getNewEquipment() != null) {
					createSubscriberEquipmentItem(ACTION_TYPE_CREATE, 
							TRANSACTION_NAME_EQUIPMENT_CHANGE + " - NEW - PRIMARY", 
							TRANSACTION_DESC_EQUIPMENT_CHANGE + " - New - Primary", 
							activity.getNewEquipment(), activity.getSwapType(), activity.getRepairId(), 
							true, false, sequence++, serviceRequest);
				}
				
				if (activity.getNewAssociatedMuleEquipment() != null) {
					createSubscriberEquipmentItem(ACTION_TYPE_CREATE, 
							TRANSACTION_NAME_EQUIPMENT_CHANGE + " - NEW - MULE", 
							TRANSACTION_DESC_EQUIPMENT_CHANGE + " - New - Mule", 
							activity.getNewAssociatedMuleEquipment(), activity.getSwapType(), activity.getRepairId(), 
							false, true, sequence++, serviceRequest);
				}
				
				submitServiceRequest(serviceRequest, result);
			}
		});
	}

	@Override
	public ActivityLoggingResult logChangePhoneNumberActivity(final ChangePhoneNumberActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				Actor [] actors = createActors(activity, activity.getBanId(), 0, activity.getSubscriberId(), activity.getNewSubscriberId());
				
				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_EXCHANGE, 
						TRANSACTION_NAME_PHONE_NUMBER_CHANGE, TRANSACTION_DESC_PHONE_NUMBER_CHANGE);
				
				createResourceItem(ACTION_TYPE_REMOVE, TRANSACTION_NAME_PHONE_NUMBER_CHANGE + " - OLD", 
						TRANSACTION_DESC_PHONE_NUMBER_CHANGE + " - Old", activity.getOldPhoneNumber(), 1, serviceRequest);

				createResourceItem(ACTION_TYPE_CREATE, TRANSACTION_NAME_PHONE_NUMBER_CHANGE + " - NEW", 
						TRANSACTION_DESC_PHONE_NUMBER_CHANGE + " - New", activity.getNewPhoneNumber(), 2, serviceRequest);
				
				submitServiceRequest(serviceRequest, result);
			}
		});
	}

	@Override
	public ActivityLoggingResult logChangeSubscriberStatusActivity(final ChangeSubscriberStatusActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				Actor [] actors = createActors(activity, activity.getBanId(), activity.getSubscriberId());

				String name = null;
				String description = null;
				Date activationDate = null;
				Date deactivationDate = null;
				int eventTypeId = 0;
				int actionTypeId = 0;
				
				if (activity.getNewSubscriberStatus() == Subscriber.STATUS_CANCELED) {
					
					eventTypeId = EVENT_CANCEL;
					name = TRANSACTION_NAME_CANCEL;
					description = TRANSACTION_DESC_CANCEL;
					actionTypeId = ACTION_TYPE_REMOVE;				
					activationDate = activity.getSubscriberActivationDate();
					deactivationDate = activity.getDate();
					
				} else if (activity.getNewSubscriberStatus() == Subscriber.STATUS_ACTIVE) {

					activationDate = activity.getDate();
					
					/* ACTIVATE FROM RESERVE event */
					if (activity.getOldSubscriberStatus() == Subscriber.STATUS_RESERVED) {
						
						eventTypeId = EVENT_ACTIVATE_FROM_RESERVE;
						name = TRANSACTION_NAME_ACTIVATE_FROM_RESERVE;
						description = TRANSACTION_DESC_ACTIVATE_FROM_RESERVE;
						actionTypeId = ACTION_TYPE_CREATE;					
					}
					
					/* RESTORE CANCEL event */
					else if (activity.getOldSubscriberStatus() == Subscriber.STATUS_CANCELED) {
						eventTypeId = EVENT_RESTORE_CANCEL;
						name = TRANSACTION_NAME_RESTORE_CANCEL;
						description = TRANSACTION_DESC_RESTORE_CANCEL;
						actionTypeId = ACTION_TYPE_CREATE;					
					}
				}
				
				if (name != null) {
					ServiceRequest serviceRequest = createServiceRequest(activity, actors, eventTypeId, name, description);
					createSubscriptionStatusItem(actionTypeId, name, description, activity.getBanId(), activity.getSubscriberId(), 
							activity.getPhoneNumber(), activity.getNewSubscriberStatus(), activationDate, deactivationDate, 
							activity.getReason(), 1, serviceRequest);
					
					submitServiceRequest(serviceRequest, result);
				}
			}
		});
	}

	@Override
	public ActivityLoggingResult logMoveSubscriberActivity(final MoveSubscriberActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				
				Actor [] actors = createActors(activity, activity.getBanId(), activity.getNewBanId(), activity.getSubscriberId(), "");
				
				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_MOVE, 
						TRANSACTION_NAME_MOVE, TRANSACTION_DESC_MOVE);
				
				createSubscriptionStatusItem(ACTION_TYPE_REMOVE, TRANSACTION_NAME_MOVE + " - OLD", 
						TRANSACTION_DESC_MOVE + " - Old", activity.getBanId(), activity.getSubscriberId(), 
						activity.getPhoneNumber(), Subscriber.STATUS_CANCELED, activity.getSubscriberActivationDate(), 
						activity.getDate(), activity.getReason(), 1, serviceRequest);
				
				createSubscriptionStatusItem(ACTION_TYPE_CREATE, TRANSACTION_NAME_MOVE + " - NEW", 
						TRANSACTION_DESC_MOVE + " - New", activity.getNewBanId(), activity.getSubscriberId(), 
						activity.getPhoneNumber(), Subscriber.STATUS_ACTIVE, activity.getDate(), 
						null, activity.getReason(), 2, serviceRequest);
				
				submitServiceRequest(serviceRequest, result);
			}
		});
	}

	@Override
	public ActivityLoggingResult logChangePaymentMethodActivity(final ChangePaymentMethodActivity activity) {
		return log(activity, new ActivityLoggingCallback(){

			public void log(ActivityLoggingResult result) {
				
				Actor [] actors = createActors(activity, activity.getBanId());
				
				ServiceRequest serviceRequest = createServiceRequest(activity, actors, EVENT_ACCOUNT_PROFILE, 
						TRANSACTION_NAME_PAYMENT_METHOD_CHANGE, TRANSACTION_DESC_PAYMENT_METHOD_CHANGE);
				
				createAccountProfileItem(ACTION_TYPE_UPDATE, 
						TRANSACTION_NAME_PAYMENT_METHOD_CHANGE, TRANSACTION_DESC_PAYMENT_METHOD_CHANGE, 
						activity.getPaymentMethodCode(), activity.getBanId(), 1, serviceRequest);
				
				submitServiceRequest(serviceRequest, result);
			}
		});
	}

	private ActivityLoggingResult submitServiceRequest(ServiceRequest serviceRequest, ActivityLoggingResult result) {
		try {
			
			LOGGER.debug("Submitting SRPS service request " + serviceRequest);
			
			RequestPK primaryKey = getRequestPersistenceService().storeServiceRequest(serviceRequest, true);
			
			ActivityLogRecordIdentifier identifier = new ActivityLogRecordIdentifier(
					new Long(primaryKey.getId()), primaryKey.getTimestamp());
			
			result.addActivityRecordLoggingResult(
					serviceRequest.getRequestHeader().getName(), 
					serviceRequest.getRequestHeader().getDescription(), 
					identifier);
			
			LOGGER.debug("SRPS service request submitted successfully: " + primaryKey);
			
		} catch (Throwable t) {
			LOGGER.error("Service request submission error applicationName=["+result.getApplicationName()+"]: " + t.getMessage(), t instanceof com.telus.srps.exceptions.ValidationException ? null : t);
			ApplicationException ae = new ApplicationException (SystemCodes.CMB_EJB, t.getMessage(), "", t);
			
			result.addActivityRecordLoggingResult(
					serviceRequest.getRequestHeader().getName(), 
					serviceRequest.getRequestHeader().getDescription(), ae); 
		}
		return result;
	}
	
	private Status createStatus(AbstractLoggableActivity activity) {
		
		Actor actor = createActor(ACTOR_TYPE_KB_OPERATOR_ID, ACTOR_ROLE_PROCESSOR, activity.getUserId());
		
		Status status = requestFactory.newStatus();
		
		status.addActor(actor);
		status.setStatusTypeId(STATUS_COMPLETED);
		status.setBusinessDate(activity.getDate());  
		status.setApplicationId(activity.getApplicationId());

		ServiceRequestNote sourceNote = activity.getServiceRequestNote();
		
		if (sourceNote != null) {
			Note note = requestFactory.newNote();
			
			note.setNoteTypeId(sourceNote.getServiceRequestNoteTypeId());
			note.setText(sourceNote.getServiceRequestNoteText());
			note.setActor(actor);
			
			status.setNote(note);
		}
		
		return status;
	}

	private Actor createActor(long actorType, long actorRole, String source) {
		Actor actor = requestFactory.newActor();
		
		actor.setActorTypeId(actorType);
		actor.setActorRoleId(actorRole);
		actor.setSourceCode(source);
		
		return actor;
	}
	
	private Actor [] createActors(AbstractLoggableActivity activity, int banId) {
		return createActors(activity, banId, "");
	}
	
	private Actor [] createActors(AbstractLoggableActivity activity, int banId, String subscriberId) {
		return createActors(activity, banId, 0, subscriberId, "");
	}
	
	private Actor [] createActors(AbstractLoggableActivity activity, int banId, int newBanId, String subscriberId, String newSubscriberId) {
		List<Actor> actors = new ArrayList<Actor>();
		
		/* create dealer actor */
		actors.add(createActor(ACTOR_TYPE_KB_DEALER_CD, ACTOR_ROLE_OF_RECORD, activity.getDealerCode()));
		
		/* create sales rep actor */
		actors.add(createActor(ACTOR_TYPE_KB_SALES_REP_PIN, ACTOR_ROLE_OF_RECORD, activity.getSalesRepCode()));

		/* create ban actor */
		actors.add(createActor(ACTOR_TYPE_KB_ACCOUNT, ACTOR_ROLE_TARGET, String.valueOf(banId) ));

		/* create new ban actor if provided */
		if (newBanId > 0) {
			actors.add(createActor(ACTOR_TYPE_KB_ACCOUNT, ACTOR_ROLE_NEW_TARGET, String.valueOf(newBanId)));
		}
		
		/* create subscriber actor if provided */
		if (!isEmpty(subscriberId)) {
			actors.add(createActor(ACTOR_TYPE_KB_SUBSCRIBER, ACTOR_ROLE_TARGET, subscriberId));
		}
		
		/* create new subscriber actor if provided */
		if (!isEmpty(newSubscriberId)) {
			actors.add(createActor(ACTOR_TYPE_KB_SUBSCRIBER, ACTOR_ROLE_NEW_TARGET, newSubscriberId));
		}
		
		/* create user actor */
		actors.add(createActor(ACTOR_TYPE_KB_OPERATOR_ID, ACTOR_ROLE_OF_RECORD, activity.getUserId()));
		
		return (Actor []) actors.toArray( new Actor [actors.size()]);
	}
	
	private ServiceRequest createServiceRequest(AbstractLoggableActivity activity, Actor[] actors, 
			int eventTypeId, String eventName, String eventDescription) {
		
		ServiceRequest request = requestFactory.newServiceRequest();
		
		/* create service request header */
		ServiceRequestHeader header = requestFactory.newServiceRequestHeader();
		header.setEventTypeId(eventTypeId);
		header.setPriorityId(PRIORITY_MEDIUM);
		header.setStatus(createStatus(activity));
		header.setActors(actors);
		header.setApplicationId(activity.getApplicationId());
		header.setLanguageCode(activity.getLanguageCode());
		
		if (!isEmpty(activity.getReferenceNumber())) {
			header.setReferenceNumber(activity.getReferenceNumber());
		}
		
		ServiceRequestParent requestParent = activity.getServiceRequestParent();
		if (requestParent != null) {
			ParentRequestPK parentRequestPk = requestFactory.newParentRequestPK(
					requestParent.getServiceRequestParentId(), 
					requestParent.getServiceRequestTimestamp(), 
					requestParent.getServiceRequestRelationshipTypeId());
			header.setParentRequestPK(parentRequestPk);
		}
		
		header.setName(eventName);
		header.setDescription(eventDescription);
		header.setEffectiveDate(activity.getDate());
		
		request.setRequestHeader(header);
		
		return request;
	}

	private SubscriberEquipmentItem createSubscriberEquipmentItem(long actionTypeId, String name, String description, 
			EquipmentHolder equipment, String swapType, String repairId, boolean isPrimary, 
			boolean isMule, int sequenceNumber, ServiceRequest request) {
		
		SubscriberEquipmentItem item = new SubscriberEquipmentItemImpl();

		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_PROVISIONABLE, sequenceNumber, request);
		
		item.setSwapType(swapType);
		item.setRepairId(repairId);
		item.setSmNumber(equipment.getSerialNumber());
		item.setTechnologyType(equipment.getTechType());
		item.setKnowbilityProductCode(equipment.getProductCode());
		item.setProductStatusCode(equipment.getProductStatusCode());
		item.setProductClassCode(equipment.getProductClassCode());
		item.setProductGroupTypeCode(equipment.getProductGroupTypeCode());
		
		return item;
	}
	
	private SubscriptionStatusItem createSubscriptionStatusItem(long actionTypeId, String name, String description, 
			int banId, String subscriberId, String phoneNumber, char subscriberStatus, Date activationDate, 
			Date deactivationDate, String reasonCode, int sequenceNumber, ServiceRequest request) {
		
		SubscriptionStatusItem item = requestFactory.newSubscriptionStatusItem();

		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);
		
		
		item.setKbSubscriberId(subscriberId);
		item.setCellPhoneNum(phoneNumber);
		item.setKbBan(new Long(banId));
		item.setSubscriberStatusCd(String.valueOf(subscriberStatus));
		item.setActivationDate(activationDate);
		item.setDeActivationDate(deactivationDate);
		item.setKbActivityReasonCode(reasonCode.trim());

		return item;
	}
	
	private ResourceItem createResourceItem(long actionTypeId, String name, String description, String phoneNumber, int sequenceNumber, ServiceRequest request) {
		ResourceItem item = requestFactory.newResourceItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_PROVISIONABLE, sequenceNumber, request);
		
		item.setPhoneNumber(phoneNumber);
		
		return item;
	}
	
	private ContractItem createContractItem(long actionTypeId, String name, String description, ContractHolder contract, int sequenceNumber, ServiceRequest request) {
		ContractItem item = requestFactory.newContractItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);

		item.setItemEffectiveDate(contract.getEffectiveDate());
		item.setItemExpiryDate(contract.getExpiryDate());
		
		item.setCommitmentReasonCode(contract.getCommitmentReasonCode());
		item.setCommitmentStartDate(contract.getCommitmentStartDate());
		item.setCommitmentEndDate(contract.getCommitmentEndDate());
		item.setCommitmentMonths(contract.getCommitmentMonths());
		
		return item;	
	}
	
	private ServicePackageItem createServicePackageItem(long actionTypeId, String name, String description, ServiceHolder service, 
			Date effectiveDate, Date expiryDate, int sequenceNumber, ServiceRequest request) {
		
		ServicePackageItem item = requestFactory.newServicePackageItem();

		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_PROVISIONABLE, sequenceNumber, request);
		
		item.setItemEffectiveDate(effectiveDate);
		item.setItemExpiryDate(expiryDate);

		item.setKnowbiltySocTypeCode(service.getServiceType());
		item.setKnowbiltySoc(service.getCode());
		item.setObjectVersionId(OBJECT_VERSION_ID);
		
		return item;	
	}	
	
	private ClientAccountStatusItem createClientAccountStatusItem(long actionTypeId, String name, String description, int banId, 
			char accountType, char accountSubType, int sequenceNumber, ServiceRequest request) {
		
		ClientAccountStatusItem item = requestFactory.newClientAccountStatusItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_ACCOUNT, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);
		
		item.setKbBan(new Integer(banId));
		item.setAccountTypeCode(String.valueOf(accountType));
		item.setAccountSubTypeCode(String.valueOf(accountSubType));
		
		return item;
	}

	private AccountProfileItem createAccountProfileItem(long actionTypeId, String name, String description, AddressHolder address, int banId,
			int sequenceNumber, ServiceRequest request) {
		
		AccountProfileItem item = requestFactory.newAccountProfileItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_ACCOUNT, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);
		
		item.setAccountProfileTypeId(ACCOUNT_PROFILE_ADDRESS_CHANGE);
		item.setObjectVersionId(OBJECT_VERSION_ID);
		item.setActivityTypeId( new Long(ACTION_TYPE_UPDATE));
		item.setBanNum(new Long(banId));
		item.setProvincialStateCd(address.getProvince());
		item.setCityName(address.getCity());
		item.setPostalCd(address.getPostalCode());
		item.setStreetName(address.getStreetName());
		item.setStreetNum(address.getStreetNumber());
		
		return item;
	}

	private AccountProfileItem createAccountProfileItem(long actionTypeId, String name, String description, int banId,
			int sequenceNumber, ServiceRequest request) {
		
		AccountProfileItem item = requestFactory.newAccountProfileItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_ACCOUNT, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);
		
		item.setAccountProfileTypeId(ACCOUNT_PROFILE_PIN_CHANGE);
		item.setObjectVersionId(OBJECT_VERSION_ID);
		item.setActivityTypeId( new Long(ACTION_TYPE_UPDATE));
		item.setBanNum(new Long(banId));
		
		
		return item;
	}
	
	private AccountProfileItem createAccountProfileItem(long actionTypeId, String name, String description, String paymentTypeCode,
			int banId, int sequenceNumber, ServiceRequest request) {
		
		AccountProfileItem item = requestFactory.newAccountProfileItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_ACCOUNT, 
				PROVISIONING_TYPE_NON_PROVISIONABLE, sequenceNumber, request);
		
		item.setAccountProfileTypeId(ACCOUNT_PROFILE_PAYMENT_METHOD_CHANGE);
		item.setObjectVersionId(OBJECT_VERSION_ID);
		item.setActivityTypeId( new Long(ACTION_TYPE_UPDATE));
		item.setBanNum(new Long(banId));
		item.setPaymentMethodTypeCd(paymentTypeCode);
		
		return item;
	}

	private ServiceFeatureItem createServiceFeatureItem(long actionTypeId, String name, String description, FeatureHolder feature, int sequenceNumber, ServiceRequest request) {
		
		final int parameterMaxSize = 40;
		
		ServiceFeatureItem item = requestFactory.newServiceFeatureItem();
		
		prepareRequestItem(item, actionTypeId, name, description, BUSINESS_AREA_TYPE_SUBSCRIBER, 
				PROVISIONING_TYPE_PROVISIONABLE, sequenceNumber, request);
		
		item.setObjectVersionId(OBJECT_VERSION_ID);
		item.setKbFeatureCd(feature.getCode());
		item.setServiceId( new Long(0));
//		item.setKnowbiltySoc(feature.getServiceCode());
		
		String parameter = feature.getParameter();
		
		if (parameter != null) {
			parameter = parameter.length() <= parameterMaxSize ? parameter : parameter.substring(0, parameterMaxSize);
		}
		
		item.setParameterValue(parameter);
		
		return item;
	}
	
	private ServiceRequestItem prepareRequestItem(ServiceRequestItem item, long actionTypeId, String name, String description, 
			int businessArreaTypeId, int provisioningTypeId, int sequenceNumber, ServiceRequest request) {
		
		ServiceRequestHeader header = request.getRequestHeader();
		
		item.setActionTypeId(actionTypeId);
		item.setApplicationId(header.getApplicationId());
		item.setItemEffectiveDate(header.getEffectiveDate());
		item.setName(name);
		item.setDescription(description);
		item.setStatus(header.getStatus());
		item.setProvisioningTypeId(provisioningTypeId);
		item.setBusinessAreaTypeId(businessArreaTypeId);
		item.setSequenceNumber(sequenceNumber);
		
		request.addRequestItem(item);
		
		return item;
	}
	
	private boolean isEmpty(String str) {
		return (str == null || str.trim().equals(""));
	}
	
	@Override
	public ServiceRequestNoteType [] getServiceRequestNoteTypes() {
		
		 NoteType [] noteTypes = getReferenceDataProvider().retrieveNoteTypes();
		 ServiceRequestNoteType [] serviceRequestNoteTypes = new ServiceRequestNoteType[noteTypes.length];
 		
		 for (int i=0; i<noteTypes.length; i++) {
			 ServiceRequestNoteTypeInfo noteTypeInfo = new ServiceRequestNoteTypeInfo();
			 noteTypeInfo.setId(noteTypes[i].getId());
			 noteTypeInfo.setName(noteTypes[i].getName());
			 noteTypeInfo.setDescription(noteTypes[i].getDescription());
			 noteTypeInfo.setDescriptionFrench(noteTypes[i].getDescription());
			 noteTypeInfo.setCode(String.valueOf(noteTypes[i].getId()));	
			 serviceRequestNoteTypes[i] = noteTypeInfo;
 		}
		return serviceRequestNoteTypes;
	}
	
	@Override
	public ServiceRequestRelationshipType [] getServiceRequestRelationshipTypes() {
		
		 RequestRelationshipType [] relationshipTypes = referenceDataProvider.retrieveRequestRelationshipTypes();
		 ServiceRequestRelationshipType [] serviceRequestRelationshipTypes = new ServiceRequestRelationshipType[relationshipTypes.length];
 		
		 for (int i=0; i<relationshipTypes.length; i++) {
			 ServiceRequestRelationshipTypeInfo relationshipTypeInfo = new ServiceRequestRelationshipTypeInfo();
			 relationshipTypeInfo.setId(relationshipTypes[i].getId());
			 relationshipTypeInfo.setName(relationshipTypes[i].getName());
			 relationshipTypeInfo.setDescription(relationshipTypes[i].getDescription());
			 relationshipTypeInfo.setDescriptionFrench(relationshipTypes[i].getDescription());
			 relationshipTypeInfo.setCode(String.valueOf(relationshipTypes[i].getId()));	
			 serviceRequestRelationshipTypes[i] = relationshipTypeInfo;
 		}
 		
 		return serviceRequestRelationshipTypes;
	}

	private RequestPersistenceService getRequestPersistenceService() {
		if (requestPersistenceService == null) {
			synchronized (lockA) {
				if (requestPersistenceService == null) {
					String providerUrl = AppConfiguration.getSrpsEASUrl();

					LOGGER.debug("Creating RequestPersistenceService at [" + providerUrl + "]");

					requestPersistenceService = (RequestPersistenceService) RemoteBeanProxyFactory.createProxy(RequestPersistenceService.class, "srps.ejb.stateless.RequestPersistenceService",
							providerUrl);
				}
			}
		}
		
		return requestPersistenceService;
	}
	
	private ReferenceDataProvider getReferenceDataProvider() {
		if (referenceDataProvider == null) {
			synchronized (lockB) {
				if (referenceDataProvider == null) {
					String providerUrl = AppConfiguration.getSrpsEASUrl();

					LOGGER.debug("Creating ReferenceDataProvider at [" + providerUrl + "]");

					referenceDataProvider = (ReferenceDataProvider) RemoteBeanProxyFactory.createProxy(ReferenceDataProvider.class, "srps.ejb.stateless.ReferenceDataProvider", providerUrl);
				}
			}
		}
		
		return referenceDataProvider;
	}
}
