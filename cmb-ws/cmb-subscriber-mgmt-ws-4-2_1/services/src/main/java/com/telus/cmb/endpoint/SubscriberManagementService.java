/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint;

import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getAccountMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getActivationOptionMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getApplicationMessageMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getContractChangeInfoMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getEquipmentChangeRequestMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getPortInEligibilityMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceAgreementMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceWarningMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServicesValidationMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getSubscriberMapper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PaymentCard;
import com.telus.api.resource.ResourceActivity;
import com.telus.cmb.common.util.InfoObjectFactory;
import com.telus.cmb.endpoint.mapping.ServiceAgreementMapper;
import com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper;
import com.telus.cmb.framework.endpoint.EndpointBusinessException;
import com.telus.cmb.framework.endpoint.EndpointOperation;
import com.telus.cmb.framework.endpoint.EndpointProvider;
import com.telus.cmb.framework.endpoint.EndpointSystemException;
import com.telus.cmb.framework.identity.ClientIdentity;
import com.telus.cmb.framework.identity.ClientIdentityManager;
import com.telus.cmb.jws.ServiceErrorCodes;
import com.telus.cmb.jws.ServicePolicyException;
import com.telus.cmb.jws.mapping.customer_management_common_50.CustomerManagementCommonMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.cmb.schema.ActivatePortInRequest;
import com.telus.cmb.schema.ActivatePortInRequestResponse;
import com.telus.cmb.schema.ActivatePortInRequestResponseType;
import com.telus.cmb.schema.ActivateSubscriber;
import com.telus.cmb.schema.ActivateSubscriberResponse;
import com.telus.cmb.schema.ActivateSubscriberResponseType;
import com.telus.cmb.schema.ActivationSubscriberData;
import com.telus.cmb.schema.ChangeEquipment;
import com.telus.cmb.schema.ChangeEquipmentResponse;
import com.telus.cmb.schema.ChangeEquipmentResponseType;
import com.telus.cmb.schema.ChangePhoneNumber;
import com.telus.cmb.schema.ChangePhoneNumberResponse;
import com.telus.cmb.schema.ChangeVoipNumber;
import com.telus.cmb.schema.ChangeVoipNumberResponse;
import com.telus.cmb.schema.ChangeVoipNumberResponseType;
import com.telus.cmb.schema.ChangeVoipNumberWithCharge;
import com.telus.cmb.schema.ChangeVoipNumberWithChargeResponse;
import com.telus.cmb.schema.ChangeVoipNumberWithChargeResponseType;
import com.telus.cmb.schema.EquipmentActivated;
import com.telus.cmb.schema.GetServiceAgreement;
import com.telus.cmb.schema.GetServiceAgreementForUpdate;
import com.telus.cmb.schema.GetServiceAgreementForUpdateResponse;
import com.telus.cmb.schema.GetServiceAgreementForUpdateResponseType;
import com.telus.cmb.schema.GetServiceAgreementResponse;
import com.telus.cmb.schema.GetServiceAgreementResponseType;
import com.telus.cmb.schema.MigrateSeat;
import com.telus.cmb.schema.MigrateSeatRequestType;
import com.telus.cmb.schema.MigrateSeatResponse;
import com.telus.cmb.schema.MigrateSeatResponseType;
import com.telus.cmb.schema.MigrateSubscriber;
import com.telus.cmb.schema.MigrateSubscriberResponse;
import com.telus.cmb.schema.MigrateSubscriberResponseType;
import com.telus.cmb.schema.MoveVoipNumberBetweenSeats;
import com.telus.cmb.schema.MoveVoipNumberBetweenSeatsResponse;
import com.telus.cmb.schema.MoveVoipNumberBetweenSeatsResponseType;
import com.telus.cmb.schema.PortInEligibility;
import com.telus.cmb.schema.PortRequestData;
import com.telus.cmb.schema.PrepaidPaymentArrangement;
import com.telus.cmb.schema.PrepopulateCallingCircleList;
import com.telus.cmb.schema.PrepopulateCallingCircleListResponse;
import com.telus.cmb.schema.PrepopulateCallingCircleListResponseType;
import com.telus.cmb.schema.ResetCscSubscription;
import com.telus.cmb.schema.ResetCscSubscriptionResponse;
import com.telus.cmb.schema.ResetCscSubscriptionResponseType;
import com.telus.cmb.schema.SaveServiceAgreement;
import com.telus.cmb.schema.SaveServiceAgreementResponse;
import com.telus.cmb.schema.SaveServiceAgreementResponseType;
import com.telus.cmb.schema.SeatResourceNumberChangeDetail;
import com.telus.cmb.schema.ServicesValidation;
import com.telus.cmb.schema.SubscriberData;
import com.telus.cmb.schema.ValidateMigrateSeat;
import com.telus.cmb.schema.ValidateMigrateSeatResponse;
import com.telus.cmb.schema.ValidateMigrateSubscriber;
import com.telus.cmb.schema.ValidateMigrateSubscriberResponse;
import com.telus.cmb.schema.ValidateServiceAgreement;
import com.telus.cmb.schema.ValidateServiceAgreementResponse;
import com.telus.cmb.schema.ValidateServiceAgreementResponseType;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidDebitCardInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.HeaderType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;


/**
 * @author Pavel Simonovsky
 *
 */
@WebService(portName = "SubscriberManagementServicePort", serviceName = "SubscriberManagementService_v4_2_1", targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_4", wsdlLocation = "/wsdls/SubscriberManagementService_v4_2_1.wsdl", endpointInterface = "com.telus.cmb.endpoint.SubscriberManagementServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberManagementService extends EndpointProvider implements SubscriberManagementServicePort {
    
    private final Logger log = Logger.getLogger(SubscriberManagementService.class);

	@Autowired
	private SubscriberLifecycleFacade subscriberFacade;

	@Autowired
	private SubscriberLifecycleManager subscriberManager;
	
	@Autowired
	private ClientIdentityManager clientIdentityManager;
	
	
	private static String[] MIGRATION_TYPE_CODES = new String[] { "PRPO", "MKPO" };
	private static String[] SEAT_MIGRATION_TYPE_CODES = new String[] { "MSPO", "POMS" };
	private static String[] ACTION_TYPE_CODES = new String[] { "ADD", "DEL" };
	private static String SYSTEM_CD_SCHEMA_VALIDATION = "Schema validation error";
	private static String ERROR_CD_SCHEMA_VALIDATION = "400";
	private static String AUTHORIZATION_HEADER = "Authorization";
	private static String BASIC_AUTH_SIGNATURE = "Basic ";
	private static String SOA_SECRET = "soaorgid";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.framework.endpoint.EndpointProvider#enumerateRuntimeResources
	 * (java.util.Map)
	 */
	@Override
	public Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("subscriberFacade", subscriberFacade);
		resources.put("subscriberManager", subscriberManager);
		return resources;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.endpoint.SubscriberManagementServicePort#changeEquipment
	 * (com.telus.cmb.schema.ChangeEquipment)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0001", errorMessage = "Failed to change equipment")
	public ChangeEquipmentResponse changeEquipment(ChangeEquipment params) throws EndpointException {
	    
	    ClientIdentity identity = validateBasicAuth( true );
	    
		validateEquipmentChangeRequest(params.getEquipmentChangeRequest());
		
		String sessionId = getSessionId(params.getSessionIdentifier(), identity, subscriberFacade);

		EquipmentChangeRequestInfo changeRequest = getEquipmentChangeRequestMapper().mapToDomain(params.getEquipmentChangeRequest());

		UserServiceProfile profile = params.getUserProfile();
		changeRequest.setSalesRepCode(profile.getSalesRepCode());
		changeRequest.setDealerCode(profile.getDealerCode());

		SubscriberData subscriberData = params.getSubscriberData();
		if (subscriberData.getAccount() != null && subscriberData.getSubscriber() != null) {
			changeRequest = subscriberFacade.changeEquipment(getSubscriberMapper().mapToDomain(subscriberData.getSubscriber()), getAccountMapper().mapToDomain(subscriberData.getAccount()), changeRequest, sessionId);
		} else {
			changeRequest = subscriberFacade.changeEquipment(subscriberData.getSubscriberId(), Integer.parseInt(subscriberData.getBillingAccountNumber()), changeRequest, sessionId);
		}

		ChangeEquipmentResponseType responseType = new ChangeEquipmentResponseType();

		try {
			ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(params.getServiceRequestHeader());
			if (servReqHeaderInfo != null) {
				servReqHeaderInfo.setApplicationName("");
			}
			subscriberFacade.reportChangeEquipment(subscriberData.getSubscriberId(), Integer.parseInt(subscriberData.getBillingAccountNumber()), changeRequest, servReqHeaderInfo, sessionId);
		} catch (ApplicationException ae) {
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
			responseType.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
		} catch (SystemException se) {
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
			responseType.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
		}

		responseType.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeRequest.getApplicationMessageList()));
		responseType.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeRequest.getSystemWarningList()));

		return respond(responseType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.endpoint.SubscriberManagementServicePort#changePhoneNumber
	 * (com.telus.cmb.schema.ChangePhoneNumber)
	 */
	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SMS_0002", errorMessage = "Failed to change phone number")
	public ChangePhoneNumberResponse changePhoneNumber(ChangePhoneNumber params) throws EndpointException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is no longer supported in this web service - please use phonenumbermgmt rest API .");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.endpoint.SubscriberManagementServicePort#getServiceAgreement
	 * (com.telus.cmb.schema.GetServiceAgreement)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0003", errorMessage = "Failed to get service agreement")
	public GetServiceAgreementResponse getServiceAgreement(GetServiceAgreement params) throws EndpointException {

	    validateBasicAuth( false );

		SubscriberContractInfo agreement = new SubscriberContractInfo();

		if (StringUtils.isNotEmpty(params.getSubscriberId()) && StringUtils.isNotEmpty(params.getBillingAccountNumber())) {
			agreement = subscriberFacade.getBaseServiceAgreement(params.getSubscriberId(), Integer.parseInt(params.getBillingAccountNumber()));
		} else if (params.getPhoneNumber() != null) {
			agreement = subscriberFacade.getBaseServiceAgreement(params.getPhoneNumber());
		}

		GetServiceAgreementResponseType response = new GetServiceAgreementResponseType();
		response.setServiceAgreement(new ServiceAgreementMapper().mapToSchema(agreement));

		return respond(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telus.cmb.endpoint.SubscriberManagementServicePort#
	 * getServiceAgreementForUpdate
	 * (com.telus.cmb.schema.GetServiceAgreementForUpdate)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0004", errorMessage = "Failed to get service agreement for update")
	public GetServiceAgreementForUpdateResponse getServiceAgreementForUpdate(GetServiceAgreementForUpdate params) throws EndpointException {
		
        validateBasicAuth( false );
        
		GetServiceAgreementForUpdateResponseType response = new GetServiceAgreementForUpdateResponseType();

		ContractChangeInfo contractChangeInfo = null;

		if (params.getBillingAccountNumber() != null && params.getSubscriberId() != null) {
			contractChangeInfo = subscriberFacade.getServiceAgreementForUpdate(params.getSubscriberId(), Integer.valueOf(params.getBillingAccountNumber()));
		} else if (params.getPhoneNumber() != null) {
			contractChangeInfo = subscriberFacade.getServiceAgreementForUpdate(params.getPhoneNumber());
		}

		if (contractChangeInfo != null) {
			response.setEquipmentChangeRequest(null);
			response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo()));
			response.setAccount(getAccountMapper().mapToSchema(contractChangeInfo.getCurrentAccountInfo()));
			response.setSubscriber(getSubscriberMapper().mapToSchema(contractChangeInfo.getCurrentSubscriberInfo()));

			UserServiceProfile userProfile = new UserServiceProfile();
			userProfile.setDealerCode(contractChangeInfo.getDealerCode());
			userProfile.setSalesRepCode(contractChangeInfo.getSalesRepCode());
			response.setUserProfile(userProfile);
		}

		return respond(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telus.cmb.endpoint.SubscriberManagementServicePort#
	 * validateServiceAgreement(com.telus.cmb.schema.ValidateServiceAgreement)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0005", errorMessage = "Validate service agreement failed")
	public ValidateServiceAgreementResponse validateServiceAgreement(ValidateServiceAgreement parameters) throws EndpointException {

        ClientIdentity identity = validateBasicAuth( true );
        
		validateEquipmentChangeRequest(parameters.getEquipmentChangeRequest());
		
		ValidateServiceAgreementResponseType response = new ValidateServiceAgreementResponseType();

		String sessionId = getSessionId(parameters.getSessionIdentifier(), identity, subscriberFacade);

		ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
		contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));
		contractChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());
		if (parameters.getSubscriberData().getAccount() != null) {
			contractChangeInfo.setCurrentAccountInfo(getAccountMapper().mapToDomain(parameters.getSubscriberData().getAccount()));
		}
		if (parameters.getSubscriberData().getSubscriber() != null) {
			contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper().mapToDomain(parameters.getSubscriberData().getSubscriber()));
		}
		if (parameters.getSubscriberData().getProductType() != null) {
			contractChangeInfo.setProductType(parameters.getSubscriberData().getProductType());
		}
		if (parameters.getCurrentServiceAgreement() != null) {
			// contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
		}
		if (parameters.getEquipmentChangeRequest() != null) {
			contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(parameters.getEquipmentChangeRequest()));
		}
		contractChangeInfo.setDealerCode(parameters.getUserProfile().getDealerCode());
		contractChangeInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());

		contractChangeInfo = subscriberFacade.validateServiceAgreement(contractChangeInfo, sessionId);

		ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo());
		resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);

		response.setValidatedServiceAgreement(serviceAgreement);
		response.setValidatedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
		response.setValidatedUserProfile(parameters.getUserProfile());

		return respond(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.endpoint.SubscriberManagementServicePort#saveServiceAgreement
	 * (com.telus.cmb.schema.SaveServiceAgreement)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0006", errorMessage = "Failed to save service agreement")
	public SaveServiceAgreementResponse saveServiceAgreement(SaveServiceAgreement parameters) throws EndpointException {
		
        ClientIdentity identity = validateBasicAuth( true );
        
		validateEquipmentChangeRequest(parameters.getEquipmentChangeRequest());
		
		String sessionId = getSessionId(parameters.getSessionIdentifier(), identity, subscriberFacade);
		
		ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
		contractChangeInfo.setActivationInd(false);
		contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));				
		if (parameters.getSubscriberData().getAccount() != null) {
			contractChangeInfo.setCurrentAccountInfo(getAccountMapper().mapToDomain(parameters.getSubscriberData().getAccount()));
		}				
		if (parameters.getSubscriberData().getSubscriber() != null) {
			contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper().mapToDomain(parameters.getSubscriberData().getSubscriber()));
		}				
		if (parameters.getSubscriberData().getProductType() != null) {
			contractChangeInfo.setProductType(parameters.getSubscriberData().getProductType());
		}
		if (parameters.getCurrentServiceAgreement() != null) {
			//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
		}				
		contractChangeInfo.setDealerCode(parameters.getUserProfile().getDealerCode());
		contractChangeInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());
		contractChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());				
		if (parameters.getEquipmentChangeRequest() != null) {
			contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(parameters.getEquipmentChangeRequest()));
		}

		com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
		if (parameters.getAuditInfo() != null) {
			auditInfoForDomain = new AuditInfoMapper().mapToDomain(parameters.getAuditInfo());
		}
		
		boolean notificationSuppressionIndicator = parameters.isNotificationSuppressionInd() == null ? false : parameters.isNotificationSuppressionInd();

		contractChangeInfo = subscriberFacade.saveServiceAgreement(contractChangeInfo, notificationSuppressionIndicator, auditInfoForDomain, sessionId);

		SaveServiceAgreementResponseType response = new SaveServiceAgreementResponseType();

		if (parameters.getServiceRequestHeader() != null) {
			try {
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(parameters.getServiceRequestHeader());
				if (servReqHeaderInfo != null) {
					servReqHeaderInfo.setApplicationName(getClientIdentity().getApplication());
				}
				subscriberFacade.reportChangeContract(parameters.getSubscriberData().getSubscriberId(), Integer.parseInt(parameters.getSubscriberData().getBillingAccountNumber()), contractChangeInfo,
						servReqHeaderInfo, sessionId);
			} catch (ApplicationException ae) {
				WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
				response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
			} catch (SystemException se) {
				WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
				response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
			}
		}

		response.setSavedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
		response.setSavedServiceAgreement(getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo()));
		response.setSavedUserProfile(parameters.getUserProfile());
		response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(contractChangeInfo.getApplicationWarningList()));
		response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(contractChangeInfo.getSystemWarningList()));
		
		return respond(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.telus.cmb.endpoint.SubscriberManagementServicePort#
	 * prepopulateCallingCircleList
	 * (com.telus.cmb.schema.PrepopulateCallingCircleList)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0007", errorMessage = "Failed to prepopulate calling circle list")
	public PrepopulateCallingCircleListResponse prepopulateCallingCircleList(PrepopulateCallingCircleList parameters) throws EndpointException {
		
	    validateBasicAuth(false);
	    
		ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
		contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));
		contractChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());	
		
		if (parameters.getSubscriberData().getAccount() != null) {
			contractChangeInfo.setCurrentAccountInfo(getAccountMapper().mapToDomain(parameters.getSubscriberData().getAccount()));
		}				
		if (parameters.getSubscriberData().getSubscriber() != null) {
			contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper().mapToDomain(parameters.getSubscriberData().getSubscriber()));
		}				
		if (parameters.getSubscriberData().getProductType() != null) {
			contractChangeInfo.setProductType(parameters.getSubscriberData().getProductType());
		}				
		if (parameters.getPreviousSubscriberData() != null) {
			contractChangeInfo.setPreviousBan(Integer.valueOf(parameters.getPreviousSubscriberData().getBillingAccountNumber()));
			contractChangeInfo.setPreviousSubscriberId(parameters.getPreviousSubscriberData().getSubscriberId());					
			if (parameters.getPreviousSubscriberData().getAccount() != null) {
				contractChangeInfo.setPreviousAccountInfo(getAccountMapper().mapToDomain(parameters.getPreviousSubscriberData().getAccount()));
			}
			if (parameters.getPreviousSubscriberData().getSubscriber() != null) {
				contractChangeInfo.setPreviousSubscriberInfo(getSubscriberMapper().mapToDomain(parameters.getPreviousSubscriberData().getSubscriber()));
			}
			if (parameters.getPreviousSubscriberData().getProductType() != null) {
				contractChangeInfo.setPreviousProductType(parameters.getPreviousSubscriberData().getProductType());
			}
		}
		
		if (parameters.getCurrentServiceAgreement() != null) {
			contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(parameters.getCurrentServiceAgreement()));
		}
		contractChangeInfo.setDealerCode(parameters.getUserProfile().getDealerCode());
		contractChangeInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());

		SubscriberContractInfo subscriberContractInfo = subscriberFacade.prepopulateCallingCircleList(contractChangeInfo);

		ServiceAgreement serviceAgreement =getServiceAgreementMapper().mapToSchema(subscriberContractInfo);
		resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);
		
		PrepopulateCallingCircleListResponseType response = new PrepopulateCallingCircleListResponseType();
		response.setServiceAgreement(serviceAgreement);
		
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0008", errorMessage = "")
	public ValidateMigrateSubscriberResponse validateMigrateSubscriber(ValidateMigrateSubscriber parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth( true );
	    
		if (!Arrays.asList(MIGRATION_TYPE_CODES).contains(parameters.getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		MigrationChangeInfo migrationChangeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), parameters.getSubscriberData(), 
				parameters.getMigrationTypeCode(), parameters.getPricePlanCode(), parameters.getNewEquipment(), 
				parameters.getNewAssociatedHandset(), parameters.getRequestorId(), parameters.getActivationOption());
		
		migrationChangeInfo = subscriberFacade.validateMigrateSubscriber(migrationChangeInfo, getSessionId(identity, subscriberFacade));

		MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
		response.setUserProfile(parameters.getUserProfile());
		response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
		response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));

		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0009", errorMessage = "")
	public MigrateSubscriberResponse migrateSubscriber(MigrateSubscriber parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth( true );
	    
		if (!Arrays.asList(MIGRATION_TYPE_CODES).contains(parameters.getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		MigrationChangeInfo changeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), parameters.getSubscriberData(), 
				parameters.getMigrationTypeCode(),  parameters.getPricePlanCode(), parameters.getNewEquipment(), 
				parameters.getNewAssociatedHandset(), parameters.getRequestorId(), parameters.getActivationOption());
		
		changeInfo.setMemoText(parameters.getMemoText());
		changeInfo = subscriberFacade.migrateSubscriber(changeInfo, getSessionId(identity, subscriberFacade));

		MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
		response.setUserProfile(parameters.getUserProfile());
		response.setAccount(getAccountMapper().mapToSchema(changeInfo.getNewAccountInfo()));
		response.setNewSubscriber(getSubscriberMapper().mapToSchema(changeInfo.getNewSubscriberInfo()));
		response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(changeInfo.getNewSubscriberContractInfo()));
		response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
		response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));

		return respond(response);
	}

	private MigrationChangeInfo mapToMigrationChangeInfo(UserServiceProfile userProfile, SubscriberData subscribertData, String migrationTypeCode, 
			String pricePlanCode, Equipment newEquipment, Equipment newAssociatedHandset, String requestorId, ActivationOption activationOption) {
		    
		MigrationChangeInfo changeInfo = new MigrationChangeInfo();
		
		changeInfo.setNewBan(Integer.valueOf(subscribertData.getBillingAccountNumber()));
		changeInfo.setSubscriberId(subscribertData.getSubscriberId());
		if (subscribertData.getAccount() != null) {
			changeInfo.setCurrentAccountInfo(getAccountMapper().mapToDomain(subscribertData.getAccount()));
		}		
		if (subscribertData.getSubscriber() != null) {
			changeInfo.setCurrentSubscriberInfo(getSubscriberMapper().mapToDomain(subscribertData.getSubscriber()));
		}		
		if (subscribertData.getProductType() != null) {
			changeInfo.setProductType(subscribertData.getProductType());
		}		
		changeInfo.setPricePlanCode(pricePlanCode);		
		changeInfo.setMigrationTypeCode(migrationTypeCode);
		if (newEquipment != null) {
			changeInfo.setNewEquipmentSerialNumber(newEquipment.getSerialNumber());
		}		
		if (newAssociatedHandset != null) {
			changeInfo.setNewAssociatedHandsetSerialNumber(newAssociatedHandset.getSerialNumber());
		}		
		changeInfo.setDealerCode(userProfile.getDealerCode());
		changeInfo.setSalesRepCode(userProfile.getSalesRepCode());
		changeInfo.setApplicationId(getClientIdentity().getApplication());
		changeInfo.setRequestorId(requestorId);
		if (activationOption != null) {
			changeInfo.setActivationOptionInfo(getActivationOptionMapper().mapToDomain(activationOption));
		}
		
		return changeInfo;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@EndpointOperation(errorCode = "CMB_SMS_0010", errorMessage = "")
	public ActivateSubscriberResponse activateSubscriber(ActivateSubscriber parameters) throws EndpointException {
	    
	    ClientIdentity identity = validateBasicAuth( true );
	    
		ActivationChangeInfo changeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), parameters.getActivationOption(), parameters.isDealerHasDepositInd(), 
				parameters.getActivityReasonCode(), parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), parameters.isWaiveSearchFeeInd(),
				parameters.getMemoText(), parameters.getPortinEligibility(), parameters.getSubscriberData(), parameters.getServicesValidation(), parameters.getServiceAgreement(), 
				parameters.getEquipmentRequest(), parameters.getSubscriptionRoleCd(),parameters.getPrepaidPaymentArrangement());

		changeInfo = subscriberFacade.activateSubscriber(changeInfo, getSessionId(identity, subscriberFacade));

		ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
		response.setSubscriberInfo(getSubscriberMapper().mapToSchema(changeInfo.getNewSubscriberInfo()));
		response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
		response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));

		return respond(response);
	}

	private ActivationChangeInfo mapToActivationChangeInfo(UserServiceProfile userProfile, ActivationOption activationOption, Boolean dealerHasDepositInd, String activityReasonCode, 
			Date startServiceDate, String activationFeeChargeCode, Boolean waiveSearchFeeInd, String memoText, PortInEligibility portinEligibility, ActivationSubscriberData subscriberData,
			ServicesValidation servicesValidation, ServiceAgreement serviceAgreement, EquipmentActivated equipmentRequest, String subscriptionRoleCd,PrepaidPaymentArrangement prepaidPaymentArrangement) throws TelusAPIException {

		ActivationChangeInfo changeInfo = new ActivationChangeInfo();
		if (subscriberData != null) {
			changeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
			changeInfo.setSubscriberId(subscriberData.getSubscriberId());
			changeInfo.setCurrentSubscriberInfo(mapSubscriberData(subscriberData, userProfile, activityReasonCode, startServiceDate));
		}
		changeInfo.setDealerHasDeposit(dealerHasDepositInd == null ? false : dealerHasDepositInd);
		changeInfo.setActivationFeeChargeCode(activationFeeChargeCode);
		changeInfo.setWaiveSearchFee(waiveSearchFeeInd == null ? false : waiveSearchFeeInd);
		changeInfo.setMemoText(memoText);
		changeInfo.setSubscriptionRoleCode(subscriptionRoleCd);
		if (portinEligibility != null) {
			changeInfo.setPortInEligibility(getPortInEligibilityMapper().mapToDomain(portinEligibility));
		}
		if (servicesValidation != null) {
			changeInfo.setServiceValidation(getServicesValidationMapper().mapToDomain(servicesValidation));
		}
		if (serviceAgreement != null) {
			changeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(serviceAgreement));
		}
		if (activationOption != null) {
			changeInfo.setActivationOption(getActivationOptionMapper().mapToDomain(activationOption));
		}
		if (equipmentRequest != null) {
			if (equipmentRequest.getPrimaryEquipment() != null) {
				changeInfo.setPrimaryEquipmentSerialNumber(equipmentRequest.getPrimaryEquipment().getSerialNumber());
				changeInfo.setPrimaryEquipmentType(equipmentRequest.getPrimaryEquipment().getEquipmentType());
			}
			if (equipmentRequest.getAssociatedHandsetEquipment() != null) {
				changeInfo.setAssociatedHandsetSerialNumber(equipmentRequest.getAssociatedHandsetEquipment().getSerialNumber());
				changeInfo.setAssociatedHandsetType(equipmentRequest.getAssociatedHandsetEquipment().getEquipmentType());
			}
		}

		ActivationTopUpPaymentArrangementInfo info = new ActivationTopUpPaymentArrangementInfo();
		if (prepaidPaymentArrangement != null){
			List<PaymentCard> paymentCardList = new ArrayList<PaymentCard>();
			// map AirtimeCard..
			if(prepaidPaymentArrangement.getAirtimeCard()!=null){
				CardInfo cardInfo  =  new CardInfo();
	             cardInfo.setSerialNumber(prepaidPaymentArrangement.getAirtimeCard().getSerialNumber());
	             cardInfo.setPIN(prepaidPaymentArrangement.getAirtimeCard().getPinNumber());
				if (prepaidPaymentArrangement.getAirtimeCard().getDenominationAmount() != null) {
					cardInfo.setAmount(Double.parseDouble(prepaidPaymentArrangement.getAirtimeCard().getDenominationAmount()));
				}
	             paymentCardList.add(cardInfo);
			}
			
			// map debit card..
			if(prepaidPaymentArrangement.getDebitCard()!=null){
				PrepaidDebitCardInfo debitInfo = new PrepaidDebitCardInfo();
				debitInfo.setTopupAmout(prepaidPaymentArrangement.getDebitCard().getActivationTopUpAmount());
				 paymentCardList.add(debitInfo);	
			}
			// map pre registered credit card..
			if(prepaidPaymentArrangement.getPreRegisteredCreditCard()!=null){
				PreRegisteredPrepaidCreditCardInfo prCreditInfo = new PreRegisteredPrepaidCreditCardInfo();
				prCreditInfo.setTopupAmout(prepaidPaymentArrangement.getPreRegisteredCreditCard().getActivationTopUpAmount());
				CreditCard creditCard = prepaidPaymentArrangement.getPreRegisteredCreditCard().getCreditCard();
				prCreditInfo.setToken(creditCard.getToken());
				prCreditInfo.setTrailingDisplayDigits(creditCard.getLast4());
				prCreditInfo.setLeadingDisplayDigits(creditCard.getFirst6());
				prCreditInfo.setExpiryMonth(creditCard.getExpiryMonth());
				prCreditInfo.setExpiryYear(creditCard.getExpiryYear());
				if (prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutoRechargeThresholdAmount() != null) {
					prCreditInfo.setThresholdAmount(prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutoRechargeThresholdAmount());
				}
				
				prCreditInfo.setTopupAmout(prepaidPaymentArrangement.getPreRegisteredCreditCard().getActivationTopUpAmount());
				if (prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutoRechargeAmount() != null) {
					prCreditInfo.setRechargeAmount(prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutoRechargeAmount());
				}
				paymentCardList.add(prCreditInfo);
			}
			info.setupPaymentCardList(paymentCardList.toArray(new PaymentCard[paymentCardList.size()]));
			changeInfo.setTopUpPaymentArrangement(info);
		}
		
		
		if (portinEligibility != null) {
			changeInfo.setPortInEligibility(getPortInEligibilityMapper().mapToDomain(portinEligibility));
		}
		
		return changeInfo;
	}
	
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0012", errorMessage = "")
	public ValidateMigrateSeatResponse validateMigrateSeat(ValidateMigrateSeat parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth( true );
	    
		MigrateSeatResponseType response = new MigrateSeatResponseType();

		// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
		if (!Arrays.asList(SEAT_MIGRATION_TYPE_CODES).contains(parameters.getMigrateSeatRequestType().getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		AuditInfo auditInfo = parameters.getAuditInfo() != null ? new AuditInfoMapper().mapToDomain(parameters.getAuditInfo()) : null;
		MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(parameters.getSubscriberData(), parameters.getMigrateSeatRequestType(), parameters.getUserProfile());

		// Make the call to the SubscriberLifecycleFacade EJB
		migrateSeatChangeInfo = subscriberFacade.validateMigrateSeat(migrateSeatChangeInfo, parameters.isNotificationSuppressionInd(), 
				auditInfo, getSessionId(identity, subscriberFacade));

		// Map the results to the response
		response.setUserProfile(parameters.getUserProfile());

		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0013", errorMessage = "")
	public MigrateSeatResponse migrateSeat(MigrateSeat parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth( true );
	    
		MigrateSeatResponseType response = new MigrateSeatResponseType();

		// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
		if (!Arrays.asList(SEAT_MIGRATION_TYPE_CODES).contains(parameters.getMigrateSeatRequestType().getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		AuditInfo auditInfo = parameters.getAuditInfo() != null ? new AuditInfoMapper().mapToDomain(parameters.getAuditInfo()) : null;
		MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(parameters.getSubscriberData(), parameters.getMigrateSeatRequestType(), parameters.getUserProfile());

		// Make the call to the SubscriberLifecycleFacade EJB
		migrateSeatChangeInfo = subscriberFacade.migrateSeat(migrateSeatChangeInfo, parameters.isNotificationSuppressionInd(), 
				auditInfo,  getSessionId(identity, subscriberFacade));

		// Map the results to the response
		response.setUserProfile(parameters.getUserProfile());
		response.setAccount(getAccountMapper().mapToSchema(migrateSeatChangeInfo.getNewAccountInfo()));
		response.setNewSubscriber(getSubscriberMapper().mapToSchema(migrateSeatChangeInfo.getNewSubscriberInfo()));
		response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrateSeatChangeInfo.getNewContractInfo()));

		return respond(response);
	}

	private MigrateSeatChangeInfo mapToMigrateSeatInfo(SubscriberData subscribertData, MigrateSeatRequestType migrateSeatRequest, UserServiceProfile userProfile) {
		
		MigrateSeatChangeInfo migrateSeatChangeInfo = new MigrateSeatChangeInfo();
		migrateSeatChangeInfo.setSubscriberId(subscribertData.getSubscriberId());
		migrateSeatChangeInfo.setTargetAccountNumber(Integer.valueOf(migrateSeatRequest.getTargetAccountNumber()));
		if (subscribertData.getAccount() != null) {
			migrateSeatChangeInfo.setCurrentAccountInfo(getAccountMapper().mapToDomain(subscribertData.getAccount()));
		}		
		if (subscribertData.getSubscriber() != null) {
			migrateSeatChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper().mapToDomain(subscribertData.getSubscriber()));
		}		
		if (subscribertData.getProductType() != null) {
			migrateSeatChangeInfo.setProductType(subscribertData.getProductType());
		}
		migrateSeatChangeInfo.setTargetSeatTypeCode(migrateSeatRequest.getTargetSeatTypeCd());
		migrateSeatChangeInfo.setTargetSeatGroupId(migrateSeatRequest.getTargetSeatGroupId());
		migrateSeatChangeInfo.setPricePlanCode(migrateSeatRequest.getPricePlanCode());
		migrateSeatChangeInfo.setMigrationTypeCode(migrateSeatRequest.getMigrationTypeCode());
		migrateSeatChangeInfo.setActivityReasonCode(migrateSeatRequest.getReasonCode());
		migrateSeatChangeInfo.setMemoText(migrateSeatRequest.getMemoText());
		migrateSeatChangeInfo.setDealerCode(userProfile.getDealerCode());
		migrateSeatChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
		migrateSeatChangeInfo.setApplicationId(getClientIdentity().getApplication());
		
		return migrateSeatChangeInfo;
	}
	
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0014", errorMessage = "")
	public ChangeVoipNumberResponse changeVoipNumber(ChangeVoipNumber parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth(true);
	    
		// Create the response
		ChangeVoipNumberResponseType response = new ChangeVoipNumberResponseType();

		// Create the resource list
		List<ResourceActivityInfo> resourceList = new ArrayList<ResourceActivityInfo>();

		// Cycle through the change details and map to the resource list
		for (SeatResourceNumberChangeDetail detail : parameters.getVoipNumberChangeRequest().getVoipNumberChangeDetailList()) {
			
			// Validate the action type code against supported values 'ADD' and 'DEL'
			if (!Arrays.asList(ACTION_TYPE_CODES).contains(detail.getActionTypeCd())) {
				throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid ActionTypeCode");
			}

			// Create a new resource and map seat resource details
			ResourceInfo resource = new ResourceInfo();

			resource.setResourceNumber(detail.getSeatResource().getSeatResourceNumber());
			resource.setResourceType(detail.getSeatResource().getSeatResourceTypeCd());

			// Create a new resource activity
			ResourceActivityInfo resourceActivity = new ResourceActivityInfo();
			resourceActivity.setResource(resource);
			resourceActivity.setResourceActivity(mapToResourceActivity(detail.getActionTypeCd()));

			// Add the resource activity to the resource list
			resourceList.add(resourceActivity);
		}

		// Make the call to the SubscriberLifecycleFacade EJB
		subscriberFacade.changeVOIPResource(parameters.getSubscriptionId(), resourceList, null, parameters.isOutgoingRequestInd(), getSessionId(identity, subscriberFacade));
		
		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0015", errorMessage = "")
	public ChangeVoipNumberWithChargeResponse changeVoipNumberWithCharge(ChangeVoipNumberWithCharge parameters) throws EndpointException {

	    ClientIdentity identity = validateBasicAuth(true);
	    
		ChangeVoipNumberWithChargeResponseType response = new ChangeVoipNumberWithChargeResponseType();
		
		// Validate the action type code against supported values 'ADD' and 'DEL'
		if (!Arrays.asList(ACTION_TYPE_CODES).contains(parameters.getVoipNumberChangeRequest().getActionTypeCd())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid ActionTypeCode");
		}
		
		// Create a new resource and map seat resource details
		ResourceInfo resource = new ResourceInfo();
		resource.setResourceNumber(parameters.getVoipNumberChangeRequest().getSeatResource().getSeatResourceNumber());
		resource.setResourceType(parameters.getVoipNumberChangeRequest().getSeatResource().getSeatResourceTypeCd());

		// Create a new resource activity and map action type
		ResourceActivityInfo resourceActivity = new ResourceActivityInfo();
		resourceActivity.setResource(resource);
		resourceActivity.setResourceActivity(mapToResourceActivity(parameters.getVoipNumberChangeRequest().getActionTypeCd()));

		// Make the call to the SubscriberLifecycleFacade EJB
		subscriberFacade.changeVOIPResourceWithCharge(parameters.getSubscriptionId(), resourceActivity, null, parameters.isOutgoingRequestInd(),
		        parameters.getRegularServiceCd(), getSessionId(identity, subscriberFacade));

		return respond(response);
	}

	private String mapToResourceActivity(String actionTypeCode) {
		if (StringUtils.equalsIgnoreCase(actionTypeCode, "ADD")) {
			return ResourceActivity.ADD;
		} else if (StringUtils.equalsIgnoreCase(actionTypeCode, "DEL")) {
			return ResourceActivity.CANCEL;
		}
		return ResourceActivity.NO_CHANGE;
	}	
	
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0016", errorMessage = "")
	public MoveVoipNumberBetweenSeatsResponse moveVoipNumberBetweenSeats(MoveVoipNumberBetweenSeats parameters) throws EndpointException {
		
	    ClientIdentity identity = validateBasicAuth(true);
	    
		// Note: this service operation is only expected to be called by Provisioning, so there's no need to call them to provision this change 
		// to the network. Therefore, the 'outgoingRequestInd' boolean is always set to 'false'.
		boolean outgoingRequestInd = false;

		// Make the call to the SubscriberLifecycleFacade EJB
		subscriberFacade.moveVOIPResource(parameters.getSourceSubscriptionId(), parameters.getTargetSubscriptionId(), 
				parameters.getSeatResource().getSeatResourceNumber(), null, outgoingRequestInd, getSessionId(identity, subscriberFacade));

		return respond(new MoveVoipNumberBetweenSeatsResponseType());
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0017", errorMessage = "Failed to reset CSC Subscription")
	public ResetCscSubscriptionResponse resetCscSubscription(ResetCscSubscription parameters) throws EndpointException {
		
	    ClientIdentity identity = validateBasicAuth(true);
	    
		int ban = Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber());
		String subscriberId = parameters.getSubscriberData().getSubscriberId();
		String productType = parameters.getSubscriberData().getProductType();

		subscriberManager.resetCSCSubscription(ban, subscriberId, productType, getSessionId(identity, subscriberFacade));
		
		return respond(new ResetCscSubscriptionResponseType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.telus.cmb.endpoint.SubscriberManagementServicePort#activatePortInRequest
	 * (com.telus.cmb.schema.ActivatePortInRequest)
	 */
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0019", errorMessage = "")
	public ActivatePortInRequestResponse activatePortInRequest(ActivatePortInRequest parameters) throws EndpointException {
		
	    ClientIdentity identity = validateBasicAuth(true);
	    
		// map in-bound schema to Info objects
		ActivationChangeInfo activationChangeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), parameters.getActivationOption(), parameters.isDealerHasDepositInd(),
				parameters.getActivityReasonCode(), parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), parameters.isWaiveSearchFeeInd(),
				parameters.getMemoText(), parameters.getPortinEligibility(), parameters.getSubscriberData(), parameters.getServicesValidation(), parameters.getServiceAgreement(),
				parameters.getEquipmentRequest(), parameters.getSubscriptionRoleCd(),parameters.getPrepaidPaymentArrangement());
		ServiceRequestHeaderInfo servReqHeaderInfo = mapToServiceRequestHeaderInfo(parameters.getServiceRequestHeader(), parameters.getPortDataheader());
		AuditInfo auditInfo = getAuditInfoMapper().mapToDomain(parameters.getAuditInfo());
		
		PortRequestInfo portRequestInfo = mapPortRequestInfo(activationChangeInfo.getCurrentSubscriberInfo(), parameters.getPortRequest(), parameters.getPortinEligibility());
		
		// call business logic implementation on the back-end
		ActivationChangeInfo changeInfo = subscriberFacade.performPortInActivation(getSessionId(identity, subscriberFacade),
				portRequestInfo, activationChangeInfo, servReqHeaderInfo, 
				auditInfo, PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT, false);
		
		ActivatePortInRequestResponseType response = new ActivatePortInRequestResponseType();
		response.setSubscriberInfo(getSubscriberMapper().mapToSchema(changeInfo.getCurrentSubscriberInfo()));
		response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
		response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));
		
		return respond(response);
	}

	public static void resetPricePlanTransactionType(ServiceAgreement serviceAgreement, ContractChangeInfo contractChangeInfo) {
		PricePlan pricePlan = serviceAgreement.getPricePlan();
		if (pricePlan != null) {
			pricePlan.setTransactionType(contractChangeInfo.isPricePlanChangeInd() ? TransactionType.MODIFY : TransactionType.NO_CHANGE);
		}
	}
	
	/**
	 * Maps schema objects to ServiceRequestHeaderInfo object for communication with the backend.
	 * @param serviceRequestHeader
	 * @param portDataheader
	 * @return
	 */
	private ServiceRequestHeaderInfo mapToServiceRequestHeaderInfo(ServiceRequestHeader serviceRequestHeader, HeaderType portDataheader) {
		ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
		if (servReqHeaderInfo != null) {
			//servReqHeaderInfo.setApplicationName(context.getClientApplication());
			servReqHeaderInfo.setApplicationName(portDataheader.getOriginatorName());
		}  
		return servReqHeaderInfo;
	}
	
	private SubscriberInfo mapSubscriberData(ActivationSubscriberData subscriberData, UserServiceProfile userProfile, String activityReasonCode, Date startServiceDate) throws TelusAPIException {

		if (subscriberData == null) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "ActivationSubscriberData is expected in the request.");
		}
		SubscriberInfo target = InfoObjectFactory.getNewSubscriberInfo(subscriberData.getProductType());
		if (subscriberData.getBillingAccountNumber() != null) {
			target.setBanId(Integer.parseInt(subscriberData.getBillingAccountNumber()));
		}
		target.setSubscriberId(subscriberData.getSubscriberId());
		target.setPhoneNumber(subscriberData.getPhoneNumber());
		target.setProductType(subscriberData.getProductType());
		if (subscriberData.getMarketProvince() != null) {
			target.setMarketProvince(subscriberData.getMarketProvince().value());
		}
		target.setBrandId(subscriberData.getBrandId());
		if (subscriberData.getConsumerName() != null) {
			ConsumerNameInfo nameInfo = CustomerManagementCommonMapper.ConsumerNameMapper().mapToDomain(subscriberData.getConsumerName());	
			if (nameInfo != null) {
				((ConsumerNameInfo) target.getConsumerName()).copyFrom(nameInfo);
			}
		}
		target.setAddress(CustomerManagementCommonMapper.AddressMapper().mapToDomain(subscriberData.getAddress()));
		target.setEmailAddress(subscriberData.getEmailAddress());
		if (subscriberData.getLanguage() != null) {
			target.setLanguage(subscriberData.getLanguage().value());
		}
		// TODO: map memberIdentity if not null
		target.setActivityReasonCode(activityReasonCode);
		target.setDealerCode(userProfile.getDealerCode());
		target.setSalesRepId(userProfile.getSalesRepCode());
		target.setStartServiceDate(startServiceDate);
		if (subscriberData.getSeatData() != null) {
			target.setSeatData(SubscriberManagementServiceMapper.getSeatDataMapper().mapToDomain(subscriberData.getSeatData()));
		}

		return target;
	}
	
	/**
	 * Maps schema objects to PortRequestInfo object for communication with the backend.
	 * @param reservedSubscriberInfo
	 * @param portRequest
	 * @param portinEligibility
	 * @return
	 * @throws Exception
	 */
	private PortRequestInfo mapPortRequestInfo(SubscriberInfo reservedSubscriberInfo, PortRequestData portRequest, PortInEligibility portinEligibility) {
		PortRequestInfo portRequestInfo = SubscriberManagementServiceMapper.getPortRequestMapper().mapToDomain(portRequest);
		portRequestInfo.setSubscriber(reservedSubscriberInfo);
		// the following 2 fields will be updated on the backend
		//portRequestInfo.setAccount(flowContext.getAccountInfo());
		//portRequestInfo.setEquipment(flowContext.getEquipmentInfo());
		portRequestInfo.setPortDirectionIndicator(portinEligibility.getPortDirectionCd());
		portRequestInfo.setOutgoingBrandId(Integer.parseInt(portinEligibility.getOutgoingBrandCd()));
		portRequestInfo.setIncomingBrandId(Integer.parseInt(portinEligibility.getIncommingBrandCd()));
		portRequestInfo.setExpedite(null);
		return portRequestInfo;
	}
	
	private AuditInfoMapper getAuditInfoMapper() {
		return AuditInfoMapper.getInstance();
	}

	private void validateEquipmentChangeRequest(EquipmentChangeRequest param) throws ApplicationException {
		if (param != null && (param.getPrimaryEquipment() == null || param.getPrimaryEquipment().getSerialNumber() == null || param.getPrimaryEquipment().getSerialNumber().trim().isEmpty())) {
			String errMsg = "PrimaryEquipment is mandatory in equipmentChangeRequest. Current value = [" + param.getPrimaryEquipment() + "]";
			throw new ApplicationException(SYSTEM_CD_SCHEMA_VALIDATION, ERROR_CD_SCHEMA_VALIDATION, errMsg, "");
		}
	}
	
    @SuppressWarnings("unchecked")
    private ClientIdentity validateBasicAuth(boolean needClientIdenty)  {
	    
	    List<String> idents = new ArrayList<String>( Arrays.asList( 
	            "APP_HWPURCHASEACCT",
	            "APP_SELFSERVEUSGBIZSVC",
	            "APP_TELUSIQ",
	            "APP_ENTWEBSTORE",
	            "RTS_APP",
	            "APP_SCOTT",
                "APP_NOTIFMGMT",
	            "ClientAPI_EJB", 
	            "ClientAPI_ESB" ) );
	    
        Map<String, List<String>> httpHeaders = (Map<String, List<String>>) 
                getServiceContext().getMessageContext().get( MessageContext.HTTP_REQUEST_HEADERS );
	    
	    List<String> basicAuthList = httpHeaders.get( AUTHORIZATION_HEADER );
	    String basicAuth = ( ( basicAuthList == null ) || ( basicAuthList.isEmpty() ) ) ? null : basicAuthList.get( 0 );
	    String errMsg = null;

	    int offset = BASIC_AUTH_SIGNATURE.length();
	    if ( StringUtils.isBlank( basicAuth ) ) {
	        errMsg = "empty basic auth";
	    }
	    else if ( ! basicAuth.substring( 0, offset ).equals( BASIC_AUTH_SIGNATURE ) ) {
	        errMsg = "bad basic auth signature";
        }
	    String ident = null;
	    if ( errMsg == null ) {
	        ident =  decodeAuthorization( basicAuth.substring( offset ), idents );
	        if ( ident == null ) {
	            errMsg = "basic auth athorization failed";
	        }
	        
	    }
	    if ( errMsg != null ) {
	        log.warn( "validate Basic Auth Error [" +  errMsg + "]" );
	        throw new EndpointSystemException( ErrorCodes.CMB_HTTP_ERROR_UNAUTHORIZED, ": " + errMsg );
	    }
	    if ( needClientIdenty ) {
	        ClientIdentity identity = clientIdentityManager.getApplicationClientIdentity( ident );
	        if ( identity == null ) {
	            throw new EndpointSystemException( ErrorCodes.CMB_HTTP_ERROR_UNAUTHORIZED, ": cannot get identity for " + ident );
	        }
	        return identity;
	    }
	    return null;
	}
	
	private String decodeAuthorization( String basicAuth, List<String> idents) {
	    
	    if ( basicAuth.contains( SOA_SECRET ) ) {
	        return null;
	    }
	    byte[] credDecoded = Base64.decodeBase64( basicAuth );
	    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
	    
        final String[] values = credentials.split(":", 2);
        if ( ( values.length != 2 ) || ( ! SOA_SECRET.equalsIgnoreCase( values[1] ) ) ) {
            return null;
        }
        for(String ident : idents) {
            if ( ident.equalsIgnoreCase( values[0] ) ) {
               return ident;
            }
        }
        return null;
	}
	
}
