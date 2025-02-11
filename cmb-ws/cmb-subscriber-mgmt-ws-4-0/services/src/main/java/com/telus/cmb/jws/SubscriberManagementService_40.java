/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.account.SeatData;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.resource.ResourceActivity;
import com.telus.cmb.jws.CheckPortRequestStatusResponse.RetrievePortRequestCollection;
import com.telus.cmb.jws.mapping.account_information_30.AccountMapper;
import com.telus.cmb.jws.mapping.customer_management_common_50.CustomerManagementCommonMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriberMapper_30;
import com.telus.cmb.mapper.SubscriberManagementServiceMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SeatDataInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.HeaderType;

@WebService(
		portName = "SubscriberManagementServicePort", 
		serviceName = "SubscriberManagementService_v4_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_4", 
		wsdlLocation = "/wsdls/SubscriberManagementService_v4_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberManagementServicePort")		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberManagementService_40 extends BaseServiceV2 implements SubscriberManagementServicePort {
	
	public SubscriberManagementService_40() {
		super(new SubscriberManagementExceptionTranslator());
	}
	
	public OpenSessionResponseType openSession(final OpenSession openSession) throws ServiceException {
		
		return execute(new ServiceInvocationCallback<OpenSessionResponseType>() {
			
			@Override
			public OpenSessionResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				OpenSessionResponseType response = new OpenSessionResponseType();
				response.setSessionIdentifier(getSubscriberLifecycleFacade(context).openSession(openSession.getPrincipal(), 
						openSession.getCredential(), openSession.getApplicationCode()));
				
				return response;
			}
		});	
	}	
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0001", errorMessage = "Failed to change equipment")
	public ChangeEquipmentResponse changeEquipment(final ChangeEquipment parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeEquipment", "CMB_SMS_0001", "Failed to change equipment");
		
		ChangeEquipmentResponseType result = execute(new ServiceInvocationCallback<ChangeEquipmentResponseType>() {

			@SuppressWarnings("unchecked")
			@Override
			public ChangeEquipmentResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(parameters.getSessionIdentifier(), context);

				EquipmentChangeRequestInfo equipmentChangeRequestInfo = getEquipmentChangeRequestMapper().mapToDomain(parameters.getEquipmentChangeRequest());
				equipmentChangeRequestInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());
				equipmentChangeRequestInfo.setDealerCode(parameters.getUserProfile().getDealerCode());				

				if (parameters.getSubscriberData().getAccount() != null && parameters.getSubscriberData().getSubscriber() != null) {
					equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(getSubscriberMapper_30().mapToDomain(parameters.getSubscriberData().getSubscriber()), 
							getAccountMapper_30().mapToDomain(parameters.getSubscriberData().getAccount()), equipmentChangeRequestInfo, sessionId);
				} else {				
					equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(parameters.getSubscriberData().getSubscriberId(),
							Integer.parseInt(parameters.getSubscriberData().getBillingAccountNumber()), equipmentChangeRequestInfo, sessionId);
				}

				ChangeEquipmentResponseType response = new ChangeEquipmentResponseType();

				if (parameters.getServiceRequestHeader() != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(parameters.getServiceRequestHeader());
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangeEquipment(parameters.getSubscriberData().getSubscriberId(), Integer.parseInt(parameters.getSubscriberData().getBillingAccountNumber()),
								equipmentChangeRequestInfo, servReqHeaderInfo, sessionId);
					} catch (ApplicationException ae) {
						WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
						response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					} catch (SystemException se) {
						WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
						response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					}
				}
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(equipmentChangeRequestInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(equipmentChangeRequestInfo.getSystemWarningList()));

				return response;
			}
			
		}, new ChangeEquipmentResponseType(), exceptionContext);
		
		ChangeEquipmentResponse response = new ChangeEquipmentResponse();
		response.setChangeEquipmentResponseType(result);
		return response;
		
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0002", errorMessage = "Failed to change phone #")
	public ChangePhoneNumberResponse changePhoneNumber(final ChangePhoneNumber parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changePhoneNumber", "CMB_SMS_0002", "Failed to change phone #");
		
		ChangePhoneNumberResponseType result = execute(new ServiceInvocationCallback<ChangePhoneNumberResponseType>() {

			@SuppressWarnings("unchecked")
			@Override
			public ChangePhoneNumberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(parameters.getSessionIdentifier(), context);

				PhoneNumberChangeInfo phoneNumberChangeInfo = new PhoneNumberChangeInfo();
				phoneNumberChangeInfo.setDealerCode(parameters.getUserProfile().getDealerCode());
				phoneNumberChangeInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());
				phoneNumberChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());
				if (parameters.getPhoneNumberChangeRequest().isChangeOtherNumbersInd() != null) {
					phoneNumberChangeInfo.setChangeOtherPhoneNumbers(parameters.getPhoneNumberChangeRequest().isChangeOtherNumbersInd().booleanValue());
				}
				phoneNumberChangeInfo.setNewPhoneNumber(parameters.getPhoneNumberChangeRequest().getPhoneNumber().getPhoneNumber());
				phoneNumberChangeInfo.setReasonCode(parameters.getPhoneNumberChangeRequest().getReasonCode());

				PhoneNumberChangeInfo newPhoneNumberChangeInfo = null;

				if (parameters.getSubscriberData().getAccount() != null && parameters.getSubscriberData().getSubscriber() != null) {
					newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(getSubscriberMapper_30().mapToDomain(parameters.getSubscriberData().getSubscriber()), 
							getAccountMapper_30().mapToDomain(parameters.getSubscriberData().getAccount()), phoneNumberChangeInfo, sessionId);
				} else {
					newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(parameters.getSubscriberData().getSubscriberId(), 
							Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()), phoneNumberChangeInfo, sessionId);
				}

				ChangePhoneNumberResponseType response = new ChangePhoneNumberResponseType();

				if (parameters.getServiceRequestHeader() != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(parameters.getServiceRequestHeader());
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangePhoneNumber(Integer.parseInt(parameters.getSubscriberData().getBillingAccountNumber()), newPhoneNumberChangeInfo, servReqHeaderInfo,
								sessionId);
					} catch (ApplicationException ae) {
						WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
						response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					} catch (SystemException se) {
						WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
						response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					}
				}

				List<WarningFaultInfo> warningList = newPhoneNumberChangeInfo.getApplicationWarningList();
				List<WarningType> schemaWarningList = getServiceWarningMapper().mapToSchema(warningList);
				response.getWarningList().addAll(schemaWarningList);

				return response;
			}
			
		}, new ChangePhoneNumberResponseType(), exceptionContext);
		
		ChangePhoneNumberResponse response = new ChangePhoneNumberResponse();
		response.setChangePhoneNumberResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0003", errorMessage = "Failed to get service agreement")
	public GetServiceAgreementResponse getServiceAgreement(final GetServiceAgreement parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("getServiceAgreement", "CMB_SMS_0003", "Failed to get service agreement");
		
		GetServiceAgreementResponseType result = execute(new ServiceInvocationCallback<GetServiceAgreementResponseType>() {
			
			@Override
			public GetServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				SubscriberContractInfo subContractInfo = null;

				if (parameters.getBillingAccountNumber() != null && parameters.getSubscriberId() != null) {
					subContractInfo = getSubscriberLifecycleFacade(context).getBaseServiceAgreement(parameters.getSubscriberId(), 
							Integer.parseInt(parameters.getBillingAccountNumber()));
				} else if (parameters.getPhoneNumber() != null) {
					subContractInfo = getSubscriberLifecycleFacade(context).getBaseServiceAgreement(parameters.getPhoneNumber().toString());
				}

				GetServiceAgreementResponseType response = new GetServiceAgreementResponseType();
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(subContractInfo));

				return response;
			}
			
		}, new GetServiceAgreementResponseType(), exceptionContext);
		
		GetServiceAgreementResponse response = new GetServiceAgreementResponse();
		response.setGetServiceAgreementResponseType(result);
		return response;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0004", errorMessage = "Failed to get service agreement for update")
	public GetServiceAgreementForUpdateResponse getServiceAgreementForUpdate(final GetServiceAgreementForUpdate parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("getServiceAgreementForUpdate", "CMB_SMS_0004", "Failed to get service agreement for update");
		
		GetServiceAgreementForUpdateResponseType result = execute(new ServiceInvocationCallback<GetServiceAgreementForUpdateResponseType>() {

			@Override
			public GetServiceAgreementForUpdateResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ContractChangeInfo contractChangeInfo = null;

				if (parameters.getBillingAccountNumber() != null && parameters.getSubscriberId() != null) {
					contractChangeInfo = getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(parameters.getSubscriberId(), Integer.valueOf(parameters.getBillingAccountNumber()));
				} else if (parameters.getPhoneNumber() != null) { 
					contractChangeInfo = getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(parameters.getPhoneNumber());
				}					

				GetServiceAgreementForUpdateResponseType response = new GetServiceAgreementForUpdateResponseType();
				if (contractChangeInfo != null) {
					response.setEquipmentChangeRequest(null);
					response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo()));
					response.setAccount(getAccountMapper_30().mapToSchema(contractChangeInfo.getCurrentAccountInfo()));
					response.setSubscriber(getSubscriberMapper_30().mapToSchema(contractChangeInfo.getCurrentSubscriberInfo()));

					UserServiceProfile userProfile = new UserServiceProfile();
					userProfile.setDealerCode(contractChangeInfo.getDealerCode());
					userProfile.setSalesRepCode(contractChangeInfo.getSalesRepCode());
					response.setUserProfile(userProfile);
				}

				return response;
			}
			
		}, new GetServiceAgreementForUpdateResponseType(), exceptionContext);
		
		GetServiceAgreementForUpdateResponse response = new GetServiceAgreementForUpdateResponse();
		response.setGetServiceAgreementForUpdateResponseType(result);
		return response;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0005", errorMessage = "Validate service agreement failed")
	public ValidateServiceAgreementResponse validateServiceAgreement(final ValidateServiceAgreement parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateServiceAgreement", "CMB_SMS_0005", "Validate service agreement failed");
		
		ValidateServiceAgreementResponseType result = execute(new ServiceInvocationCallback<ValidateServiceAgreementResponseType>() {

			@Override
			public ValidateServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(parameters.getSessionIdentifier(), context);

				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
				contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());
				if (parameters.getSubscriberData().getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(parameters.getSubscriberData().getAccount()));
				}
				if (parameters.getSubscriberData().getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(parameters.getSubscriberData().getSubscriber()));
				}
				if (parameters.getSubscriberData().getProductType() != null) {
					contractChangeInfo.setProductType(parameters.getSubscriberData().getProductType());
				}
				if (parameters.getCurrentServiceAgreement() != null) {
					//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}
				if (parameters.getEquipmentChangeRequest() != null) {
					contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(parameters.getEquipmentChangeRequest()));
				}
				contractChangeInfo.setDealerCode(parameters.getUserProfile().getDealerCode());
				contractChangeInfo.setSalesRepCode(parameters.getUserProfile().getSalesRepCode());

				contractChangeInfo = getSubscriberLifecycleFacade(context).validateServiceAgreement(contractChangeInfo,sessionId);

				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo());
				SubscriberManagementServiceMapper.resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);

				ValidateServiceAgreementResponseType response = new ValidateServiceAgreementResponseType();
				response.setValidatedServiceAgreement(serviceAgreement);
				response.setValidatedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
				response.setValidatedUserProfile(parameters.getUserProfile());

				return response;
			}
			
		}, new ValidateServiceAgreementResponseType(), exceptionContext);
		
		ValidateServiceAgreementResponse response = new ValidateServiceAgreementResponse();
		response.setValidateServiceAgreementResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0006", errorMessage = "Failed to save service agreement")
	public SaveServiceAgreementResponse saveServiceAgreement(final SaveServiceAgreement parameters)
			throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("saveServiceAgreement", "CMB_SMS_0006", "Failed to save service agreement");
		
		SaveServiceAgreementResponseType result = execute(new ServiceInvocationCallback<SaveServiceAgreementResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public SaveServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				String sessionId = getSessionId(parameters.getSessionIdentifier(), context);
				
				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
				contractChangeInfo.setActivationInd(false);
				contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));				
				if (parameters.getSubscriberData().getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(parameters.getSubscriberData().getAccount()));
				}				
				if (parameters.getSubscriberData().getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(parameters.getSubscriberData().getSubscriber()));
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

				contractChangeInfo = getSubscriberLifecycleFacade(context).saveServiceAgreement(contractChangeInfo, notificationSuppressionIndicator, auditInfoForDomain, sessionId);

				SaveServiceAgreementResponseType response = new SaveServiceAgreementResponseType();

				if (parameters.getServiceRequestHeader() != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(parameters.getServiceRequestHeader());
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangeContract(parameters.getSubscriberData().getSubscriberId(), Integer.parseInt(parameters.getSubscriberData().getBillingAccountNumber()), contractChangeInfo,
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

				return response;
			}
			
		}, new SaveServiceAgreementResponseType(), exceptionContext);
		
		SaveServiceAgreementResponse response = new SaveServiceAgreementResponse();
		response.setSaveServiceAgreementResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0007", errorMessage = "Failed to prepopulate calling circle list")
	public PrepopulateCallingCircleListResponse prepopulateCallingCircleList(final PrepopulateCallingCircleList parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("prepopulateCallingCircleList", "CMB_SMS_0007", "Failed to prepopulate calling circle list");
				
		PrepopulateCallingCircleListResponseType result = execute(new ServiceInvocationCallback<PrepopulateCallingCircleListResponseType>() {

			@Override
			public PrepopulateCallingCircleListResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(parameters.getNewServiceAgreement());
				contractChangeInfo.setBan(Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(parameters.getSubscriberData().getSubscriberId());				
				if (parameters.getSubscriberData().getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(parameters.getSubscriberData().getAccount()));
				}				
				if (parameters.getSubscriberData().getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(parameters.getSubscriberData().getSubscriber()));
				}				
				if (parameters.getSubscriberData().getProductType() != null) {
					contractChangeInfo.setProductType(parameters.getSubscriberData().getProductType());
				}				
				if (parameters.getPreviousSubscriberData() != null) {
					contractChangeInfo.setPreviousBan(Integer.valueOf(parameters.getPreviousSubscriberData().getBillingAccountNumber()));
					contractChangeInfo.setPreviousSubscriberId(parameters.getPreviousSubscriberData().getSubscriberId());					
					if (parameters.getPreviousSubscriberData().getAccount() != null) {
						contractChangeInfo.setPreviousAccountInfo(getAccountMapper_30().mapToDomain(parameters.getPreviousSubscriberData().getAccount()));
					}
					if (parameters.getPreviousSubscriberData().getSubscriber() != null) {
						contractChangeInfo.setPreviousSubscriberInfo(getSubscriberMapper_30().mapToDomain(parameters.getPreviousSubscriberData().getSubscriber()));
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

				SubscriberContractInfo subscriberContractInfo = getSubscriberLifecycleFacade(context).prepopulateCallingCircleList(contractChangeInfo);

				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(subscriberContractInfo);
				SubscriberManagementServiceMapper.resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);
				
				PrepopulateCallingCircleListResponseType response = new PrepopulateCallingCircleListResponseType();
				response.setServiceAgreement(serviceAgreement);

				return response;
			}
			
		}, new PrepopulateCallingCircleListResponseType(), exceptionContext);
		
		PrepopulateCallingCircleListResponse response = new PrepopulateCallingCircleListResponse();
		response.setPrepopulateCallingCircleListResponseType(result);
		return response;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0008", errorMessage = "Validate Migrate Subscriber.")
	public ValidateMigrateSubscriberResponse validateMigrateSubscriber(final ValidateMigrateSubscriber parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateMigrateSubscriber", "CMB_SMS_0008", "Validate Migrate Subscriber.");
		
		MigrateSubscriberResponseType result = execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Oct 2013 release - only support 2 migration types: PRPO and MKPO
				assertValid("migrationTypeCode", parameters.getMigrationTypeCode(), new String[] {"PRPO", "MKPO"}); 

				MigrationChangeInfo migrationChangeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), 
						parameters.getSubscriberData(), parameters.getMigrationTypeCode(), 
						parameters.getPricePlanCode(), parameters.getNewEquipment(), 
						parameters.getNewAssociatedHandset(), parameters.getRequestorId(), 
						context);
				migrationChangeInfo = getSubscriberLifecycleFacade(context).validateMigrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());

				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(parameters.getUserProfile());
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new MigrateSubscriberResponseType(), exceptionContext);
		
		ValidateMigrateSubscriberResponse response = new ValidateMigrateSubscriberResponse();
		response.setMigrateSubscriberResponseType(result);
		return response;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0009", errorMessage = "Migrate Subscriber.")
	public MigrateSubscriberResponse migrateSubscriber(final MigrateSubscriber parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("migrateSubscriber", "CMB_SMS_0009", "Migrate Subscriber.");
		
		MigrateSubscriberResponseType result = execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Oct 2013 release - only support 2 migration types: PRPO and MKPO
				assertValid("migrationTypeCode", parameters.getMigrationTypeCode(), new String[] {"PRPO", "MKPO"}); 

				MigrationChangeInfo migrationChangeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), 
						parameters.getSubscriberData(), parameters.getMigrationTypeCode(), 
						parameters.getPricePlanCode(), parameters.getNewEquipment(), 
						parameters.getNewAssociatedHandset(), parameters.getRequestorId(), 
						context);
				migrationChangeInfo.setMemoText(parameters.getMemoText());
				migrationChangeInfo = getSubscriberLifecycleFacade(context).migrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());

				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(parameters.getUserProfile());
				response.setAccount(getAccountMapper_30().mapToSchema(migrationChangeInfo.getNewAccountInfo()));
				response.setNewSubscriber(getSubscriberMapper_30().mapToSchema(migrationChangeInfo.getNewSubscriberInfo()));
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrationChangeInfo.getNewSubscriberContractInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new MigrateSubscriberResponseType(), exceptionContext);
		
		MigrateSubscriberResponse response = new MigrateSubscriberResponse();
		response.setMigrateSubscriberResponseType(result);
		return response;
	}

	private MigrationChangeInfo mapToMigrationChangeInfo(UserServiceProfile userProfile, SubscriberData subscribertData,
			String migrationTypeCode, String pricePlanCode,
			Equipment newEquipment, Equipment newAssociatedHandset,
			String requestorId, ServiceInvocationContext context) {
		
		MigrationChangeInfo migrationChangeInfo = new MigrationChangeInfo();
		migrationChangeInfo.setNewBan(Integer.valueOf(subscribertData.getBillingAccountNumber()));
		migrationChangeInfo.setSubscriberId(subscribertData.getSubscriberId());
		if (subscribertData.getAccount() != null) {
			migrationChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscribertData.getAccount()));
		}		
		if (subscribertData.getSubscriber() != null) {
			migrationChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(subscribertData.getSubscriber()));
		}		
		if (subscribertData.getProductType() != null) {
			migrationChangeInfo.setProductType(subscribertData.getProductType());
		}		
		migrationChangeInfo.setPricePlanCode(pricePlanCode);		
		migrationChangeInfo.setMigrationTypeCode(migrationTypeCode);
		if (newEquipment != null) {
			migrationChangeInfo.setNewEquipmentSerialNumber(newEquipment.getSerialNumber());
		}		
		if (newAssociatedHandset != null) {
			migrationChangeInfo.setNewAssociatedHandsetSerialNumber(newAssociatedHandset.getSerialNumber());
		}		
		migrationChangeInfo.setDealerCode(userProfile.getDealerCode());
		migrationChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
		migrationChangeInfo.setApplicationId(context.getClientApplication());
		migrationChangeInfo.setRequestorId(requestorId);
		
		return migrationChangeInfo;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0010", errorMessage = "Activate Subscriber.")
	public ActivateSubscriberResponse activateSubscriber(final ActivateSubscriber parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("activateSubscriber", "CMB_SMS_0010", "Activate Subscriber.");
		
		ActivateSubscriberResponseType result = execute(new ServiceInvocationCallback<ActivateSubscriberResponseType>() {
			
			@Override
			public ActivateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ActivationChangeInfo changeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), 
						parameters.getActivationOption(), parameters.isDealerHasDepositInd(), 
						parameters.isActivationRequiredInd(), parameters.getActivityReasonCode(), 
						parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), 
						parameters.isWaiveSearchFeeInd(), parameters.getMemoText(), 
						parameters.getPortinEligibility(), parameters.getSubscriberData(), 
						parameters.getServicesValidation(), parameters.getServiceAgreement(), 
						parameters.getEquipmentRequest());

				changeInfo = getSubscriberLifecycleFacade(context).activateSubscriber(changeInfo, context.getSubscriberLifecycleFacadeSessionId());

				ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
				response.setSubscriberInfo(getSubscriberMapper_30().mapToSchema(changeInfo.getNewSubscriberInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new ActivateSubscriberResponseType(), exceptionContext);
		
		ActivateSubscriberResponse response = new ActivateSubscriberResponse();
		response.setActivateSubscriberResponseType(result);
		return response;
	}

	private ActivationChangeInfo mapToActivationChangeInfo(UserServiceProfile userProfile, ActivationOption activationOption, Boolean dealerHasDepositInd, boolean activationRequiredInd,
			String activityReasonCode, Date startServiceDate, String activationFeeChargeCode, Boolean waiveSearchFeeInd, String memoText, PortInEligibility portinEligibility,
			ActivationSubscriberData subscriberData, ServicesValidation servicesValidation, ServiceAgreement serviceAgreement, EquipmentActivated equipmentRequest) {

		ActivationChangeInfo changeInfo = new ActivationChangeInfo();
		if (subscriberData != null) {
			changeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
			changeInfo.setReservedSubscriberId(subscriberData.getSubscriberId());
			changeInfo.setProductType(subscriberData.getProductType());
			changeInfo.setPhoneNumber(subscriberData.getPhoneNumber());
			changeInfo.setMarketProvince(subscriberData.getMarketProvince().value());
			changeInfo.setBrandId(subscriberData.getBrandId());
			changeInfo.setLanguage(subscriberData.getLanguage().value());
			changeInfo.setSubscriberId(subscriberData.getSubscriberId());
			//changeInfo.setSeatTypeCode(subscriberData.getSeatTypeCd());
			//changeInfo.setSeatGroupCode(subscriberData.getSeatGroupCd());
		}
		if (userProfile != null) {
			changeInfo.setDealerCode(userProfile.getDealerCode());
			changeInfo.setSalesRepId(userProfile.getSalesRepCode());
		}
		changeInfo.setDealerHasDeposit(dealerHasDepositInd == null ? false : dealerHasDepositInd);
		changeInfo.setActivate(activationRequiredInd);
		changeInfo.setActivityReasonCode(activityReasonCode);
		changeInfo.setStartServiceDate(startServiceDate);
		changeInfo.setActivationFeeChargeCode(activationFeeChargeCode);
		changeInfo.setWaiveSearchFee(waiveSearchFeeInd == null ? false : waiveSearchFeeInd);
		changeInfo.setMemoText(memoText);
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

		return changeInfo;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0012", errorMessage = "Failed to validate migrate seat")
	public ValidateMigrateSeatResponse validateMigrateSeat(final ValidateMigrateSeat parameters)
					throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateMigrateSeat", "CMB_SMS_0012", "Failed to validate migrate seat");
		
		MigrateSeatResponseType result = execute(new ServiceInvocationCallback<MigrateSeatResponseType>() {
			
			@Override
			public MigrateSeatResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				MigrateSeatResponseType response = new MigrateSeatResponseType();

				// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
				assertValid("migrationTypeCode", parameters.getMigrateSeatRequestType().getMigrationTypeCode(), new String[] {"MSPO", "POMS"}); 

				com.telus.eas.transaction.info.AuditInfo domainAuditInfo = parameters.getAuditInfo() != null ? new AuditInfoMapper().mapToDomain(parameters.getAuditInfo()) : null;
				MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(parameters.getSubscriberData(), parameters.getMigrateSeatRequestType(), parameters.getUserProfile(), context);

				// Make the call to the SubscriberLifecycleFacade EJB
				migrateSeatChangeInfo = getSubscriberLifecycleFacade(context).validateMigrateSeat(migrateSeatChangeInfo, parameters.isNotificationSuppressionInd(), 
						domainAuditInfo, context.getSubscriberLifecycleFacadeSessionId());

				// Map the results to the response
				response.setUserProfile(parameters.getUserProfile());

				return response;
			}

		}, new MigrateSeatResponseType(), exceptionContext);
		
		ValidateMigrateSeatResponse response = new ValidateMigrateSeatResponse();
		response.setMigrateSeatResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0013", errorMessage = "Failed to migrate seat")
	public MigrateSeatResponse migrateSeat(final MigrateSeat parameters)
					throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("migrateSeat", "CMB_SMS_0013", "Failed to migrate seat");

		MigrateSeatResponseType result = execute(new ServiceInvocationCallback<MigrateSeatResponseType>() {

			@Override
			public MigrateSeatResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				MigrateSeatResponseType response = new MigrateSeatResponseType();

				// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
				assertValid("migrationTypeCode", parameters.getMigrateSeatRequestType().getMigrationTypeCode(), new String[] {"MSPO", "POMS"});

				com.telus.eas.transaction.info.AuditInfo domainAuditInfo = parameters.getAuditInfo() != null ? new AuditInfoMapper().mapToDomain(parameters.getAuditInfo()) : null;
				MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(parameters.getSubscriberData(), parameters.getMigrateSeatRequestType(), parameters.getUserProfile(), context);

				// Make the call to the SubscriberLifecycleFacade EJB
				migrateSeatChangeInfo = getSubscriberLifecycleFacade(context).migrateSeat(migrateSeatChangeInfo, parameters.isNotificationSuppressionInd(), 
						domainAuditInfo, context.getSubscriberLifecycleFacadeSessionId());

				// Map the results to the response
				response.setUserProfile(parameters.getUserProfile());
				response.setAccount(getAccountMapper_30().mapToSchema(migrateSeatChangeInfo.getNewAccountInfo()));
				response.setNewSubscriber(getSubscriberMapper_30().mapToSchema(migrateSeatChangeInfo.getNewSubscriberInfo()));
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrateSeatChangeInfo.getNewContractInfo()));

				return response;
			}

		}, new MigrateSeatResponseType(), exceptionContext);
		
		MigrateSeatResponse response = new MigrateSeatResponse();
		response.setMigrateSeatResponseType(result);
		return response;
	}
	
	private MigrateSeatChangeInfo mapToMigrateSeatInfo(SubscriberData subscribertData, MigrateSeatRequestType migrateSeatRequest, UserServiceProfile userProfile, 
			ServiceInvocationContext context) {
		
		MigrateSeatChangeInfo migrateSeatChangeInfo = new MigrateSeatChangeInfo();
		migrateSeatChangeInfo.setSubscriberId(subscribertData.getSubscriberId());
		migrateSeatChangeInfo.setTargetAccountNumber(Integer.valueOf(migrateSeatRequest.getTargetAccountNumber()));
		if (subscribertData.getAccount() != null) {
			migrateSeatChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscribertData.getAccount()));
		}		
		if (subscribertData.getSubscriber() != null) {
			migrateSeatChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(subscribertData.getSubscriber()));
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
		migrateSeatChangeInfo.setApplicationId(context.getClientApplication());
		
		return migrateSeatChangeInfo;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0014", errorMessage = "Failed to change VOIP #")
	public ChangeVoipNumberResponse changeVoipNumber(final ChangeVoipNumber parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeVoipNumber", "CMB_SMS_0014", "Failed to change VOIP #");

		ChangeVoipNumberResponseType result = execute(new ServiceInvocationCallback<ChangeVoipNumberResponseType>() {

			@Override
			public ChangeVoipNumberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Create the response
				ChangeVoipNumberResponseType response = new ChangeVoipNumberResponseType();

				// Create the resource list
				List<ResourceActivityInfo> resourceList = new ArrayList<ResourceActivityInfo>();

				// Cycle through the change details and map to the resource list
				for (SeatResourceNumberChangeDetail detail : parameters.getVoipNumberChangeRequest().getVoipNumberChangeDetailList()) {
					
					// Validate the action type code against supported values 'ADD' and 'DEL'
					assertValid("actionTypeCd", detail.getActionTypeCd(), new String[] {"ADD", "DEL"});

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
				getSubscriberLifecycleFacade(context).changeVOIPResource(parameters.getSubscriptionId(), resourceList, null, parameters.isOutgoingRequestInd(), context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new ChangeVoipNumberResponseType(), exceptionContext);
		
		ChangeVoipNumberResponse response = new ChangeVoipNumberResponse();
		response.setChangeVoipNumberResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0015", errorMessage = "Failed to change VOIP # with charge")
	public ChangeVoipNumberWithChargeResponse changeVoipNumberWithCharge(final ChangeVoipNumberWithCharge parameters) throws ServiceException {
		
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeVoipNumberWithCharge", "CMB_SMS_0015", "Failed to change VOIP # with charge");

		ChangeVoipNumberWithChargeResponseType result = execute(new ServiceInvocationCallback<ChangeVoipNumberWithChargeResponseType>() {

			@Override
			public ChangeVoipNumberWithChargeResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Create the response
				ChangeVoipNumberWithChargeResponseType response = new ChangeVoipNumberWithChargeResponseType();
				
				// Validate the action type code against supported values 'ADD' and 'DEL'
				assertValid("actionTypeCd", parameters.getVoipNumberChangeRequest().getActionTypeCd(), new String[] {"ADD", "DEL"});
				
				// Create a new resource and map seat resource details
				ResourceInfo resource = new ResourceInfo();
				resource.setResourceNumber(parameters.getVoipNumberChangeRequest().getSeatResource().getSeatResourceNumber());
				resource.setResourceType(parameters.getVoipNumberChangeRequest().getSeatResource().getSeatResourceTypeCd());

				// Create a new resource activity and map action type
				ResourceActivityInfo resourceActivity = new ResourceActivityInfo();
				resourceActivity.setResource(resource);
				resourceActivity.setResourceActivity(mapToResourceActivity(parameters.getVoipNumberChangeRequest().getActionTypeCd()));

				// Make the call to the SubscriberLifecycleFacade EJB
				getSubscriberLifecycleFacade(context).changeVOIPResourceWithCharge(parameters.getSubscriptionId(), resourceActivity, null, parameters.isOutgoingRequestInd(), parameters.getRegularServiceCd(), context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new ChangeVoipNumberWithChargeResponseType(), exceptionContext);
		
		ChangeVoipNumberWithChargeResponse response = new ChangeVoipNumberWithChargeResponse();
		response.setChangeVoipNumberWithChargeResponseType(result);
		return response;
	}
	
	private String mapToResourceActivity(String actionTypeCode) {
		
		if (actionTypeCode.equalsIgnoreCase("ADD")) {
			return ResourceActivity.ADD;
		}
		if (actionTypeCode.equalsIgnoreCase("DEL")) {
			return ResourceActivity.CANCEL;
		}
		
		return ResourceActivity.NO_CHANGE;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0016", errorMessage = "Failed to move VOIP # bewteen seats")
	public MoveVoipNumberBetweenSeatsResponse moveVoipNumberBetweenSeats(final MoveVoipNumberBetweenSeats parameters) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("moveVoipNumberBetweenSeats", "CMB_SMS_0016", "Failed to move VOIP # bewteen seats");

		MoveVoipNumberBetweenSeatsResponseType result = execute(new ServiceInvocationCallback<MoveVoipNumberBetweenSeatsResponseType>() {

			@Override
			public MoveVoipNumberBetweenSeatsResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Note: this service operation is only expected to be called by Provisioning, so there's no need to call them to provision this change 
				// to the network. Therefore, the 'outgoingRequestInd' boolean is always set to 'false'.
				boolean outgoingRequestInd = false;

				// Create the response
				MoveVoipNumberBetweenSeatsResponseType response = new MoveVoipNumberBetweenSeatsResponseType();

				// Make the call to the SubscriberLifecycleFacade EJB
				getSubscriberLifecycleFacade(context).moveVOIPResource(parameters.getSourceSubscriptionId(), 
						parameters.getTargetSubscriptionId(), parameters.getSeatResource().getSeatResourceNumber(), 
						null, outgoingRequestInd, context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new MoveVoipNumberBetweenSeatsResponseType(), exceptionContext);
		
		MoveVoipNumberBetweenSeatsResponse response = new MoveVoipNumberBetweenSeatsResponse();
		response.setMoveVoipNumberBetweenSeatsResponseType(result);
		return response;
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0007", errorMessage = "Failed to reset CSC Subscription")
	public ResetCscSubscriptionResponse resetCscSubscription(final ResetCscSubscription parameters) throws ServiceException {
			
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
			ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("resetCscSubscription", "CMB_SMS_0007", "Failed to reset CSC Subscription");
			ResetCscSubscriptionResponseType result = execute(new ServiceInvocationCallback<ResetCscSubscriptionResponseType>() {

			@Override
			public ResetCscSubscriptionResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ResetCscSubscriptionResponseType response = new ResetCscSubscriptionResponseType();

					int ban = Integer.valueOf(parameters.getSubscriberData().getBillingAccountNumber());
					String subscriberId = parameters.getSubscriberData().getSubscriberId();
					String productType = null;
					if (parameters.getSubscriberData().getProductType() != null) {
						productType = parameters.getSubscriberData().getProductType();
					}

					getSubscriberLifecycleManager(context).resetCSCSubscription(ban, subscriberId,productType,context.getSubscriberLifecycleManagerSessionId());
				
				return response;
			}
		}, new ResetCscSubscriptionResponseType(), exceptionContext);
		
		ResetCscSubscriptionResponse response = new ResetCscSubscriptionResponse();
		response.setResetCscSubscriptionResponseType(result);
		return response;
	}
	
	private SubscriberManagementServiceMapper.ServiceAgreementMapper getServiceAgreementMapper() {
		return SubscriberManagementServiceMapper.ServiceAgreementMapper();
	}
	
	private AccountMapper getAccountMapper_30() {
		return AccountMapper.getInstance();
	}
	
	private SubscriberMapper_30 getSubscriberMapper_30() {
		return SubscriberMapper_30.getInstance();
	}
	
	private AuditInfoMapper getAuditInfoMapper() {
		return AuditInfoMapper.getInstance();
	}

	private SubscriberManagementServiceMapper.ContractChangeInfoMapper getContractChangeInfoMapper() {
		return SubscriberManagementServiceMapper.ContractChangeInfoMapper();
	}
	
	private SubscriberManagementServiceMapper.EquipmentChangeRequestMapper getEquipmentChangeRequestMapper() {
		return SubscriberManagementServiceMapper.EquipmentChangeRequestMapper();
	}
	
	private SubscriberManagementServiceMapper.ServiceWarningMapper getServiceWarningMapper() {
		return SubscriberManagementServiceMapper.ServiceWarningMapper();
	}
	
	private SubscriberManagementServiceMapper.ApplicationMessageMapper getApplicationMessageMapper() {
		return SubscriberManagementServiceMapper.ApplicationMessageMapper();
	}
	
	private SubscriberManagementServiceMapper.PortInEligibilityMapper getPortInEligibilityMapper() {
		return SubscriberManagementServiceMapper.PortInEligibilityMapper();
	}
	
	private SubscriberManagementServiceMapper.ServicesValidationMapper getServicesValidationMapper() {
		return SubscriberManagementServiceMapper.ServicesValidationMapper();
	}
	
	private SubscriberManagementServiceMapper.ActivationOptionMapper getActivationOptionMapper() {
		return SubscriberManagementServiceMapper.ActivationOptionMapper();
	}
	
	private SubscriberManagementServiceMapper.PortRequestSummaryMapper getPortRequestSummaryMapper() {
		return SubscriberManagementServiceMapper.PortRequestSummaryMapper();
	}
	
	private SubscriberManagementServiceMapper.CurrentPortRequestMapper getCurrentPortRequestMapper() {
		return SubscriberManagementServiceMapper.CurrentPortRequestMapper();
	}
	
	private String getSessionId(String sessionIdentifier, ServiceInvocationContext context) throws Throwable {
		return (sessionIdentifier == null || sessionIdentifier.trim().length() == 0) ? 
			context.getSubscriberLifecycleFacadeSessionId() : sessionIdentifier;
	}

	/*
	 * This version of the Xml2Wsdl XSLT does not support the more sophisticated ping operation below.
	 *
	 * @Override
	public PingStats ping(String operationName, Boolean deepPing) throws ServiceException {
		PingStats pingStats = new PingStats();
		try {
			pingStats.setServiceName(super.ping());
		} catch (Exception e) {
			pingStats.setServiceName("SubscriberManagementService 3.1");
		}
		
		return pingStats;
	}*/

	@Override
	public String ping() throws ServiceException {
		return "SubscriberManagementService 4.0";
	}
	
	// Due to new SOA guidelines, PolicyException is no longer supported. This method creates an ExceptionTranslationContext to pass into the ServiceInvocationContext
	// execution method in order to map PolicyException to the ResponseMessage. This method only partially constructs the ExceptionTranslationContext used by the AspectJ
	// ServiceInvocationAspect code wrapping the @ServiceBusinessOperation annotation class, as the parameter names and values are missing.
	// Note: this only catches PolicyExceptions thrown within the ServiceInvocationContext. This is only a temporary solution, implemented
	// in conjunction with other workarounds (see BaseServiceV2 overloaded execute method).
	private ExceptionTranslationContext createExceptionTranslationContext(String methodName, String errorCode, String errorMessage) {
		
		ExceptionTranslationContext context = new ExceptionTranslationContext();		
		context.setErrorCode(errorCode);
		context.setErrorMessage(errorMessage);
		context.setServiceName(getClass().getName());
		context.setOperationName(methodName);
		// Note: we're missing parameter names and values.
		context.setParameterNames(new String[0]);
		
		return context;
	}

	@Override
	public GetCurrentPortRequestsResponse getCurrentPortRequests(final GetCurrentPortRequests parameters)
			throws ServiceException {
		
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("getCurrentPortRequests", "CMB_SMS_0008", "Failed to get Current Port Request");
		
		final GetCurrentPortRequestsResponse portRequestResponce = new GetCurrentPortRequestsResponse();
		
		execute(new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				PortRequestInfo portRequest = new PortRequestInfo();
				AccountInfo account = null;

				if (parameters.isRetrieveCurrentPortRequestsInd()) {
					com.telus.api.portability.PortRequest lastPortRequest = getLastPortRequest(context,parameters.getAccountNumber());

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
						account = getAccountInformationHelper(context).retrieveAccountByBan(Integer.parseInt(parameters.getAccountNumber()));
						portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
						portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);
					}
					portRequest.setPhoneNumber(parameters.getSubscriberNumber());
					portRequest.setPortDirectionIndicator("A");
				}else {
					//if ("A".equals(PortInEligibility.PORT_DIRECTION_INDICATOR_WIRELESS_WIRELESS)) {
						portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
						portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);
					//}
					portRequest.setPhoneNumber(parameters.getSubscriberNumber());
					portRequest.setPortDirectionIndicator("A");
				}

				CurrentPortRequestResponseType currentPortRequestResponseType = new CurrentPortRequestResponseType();
				com.telus.cmb.jws.GetCurrentPortRequestsResponse.RetrievePortRequestCollection portRequestList = new com.telus.cmb.jws.GetCurrentPortRequestsResponse.RetrievePortRequestCollection();
				portRequestList.getPortRequestList().add(getCurrentPortRequestMapper().mapToSchema(portRequest));
				
				portRequestResponce.setCurrentPortRequestResponseType(currentPortRequestResponseType);
				portRequestResponce.setRetrievePortRequestCollection(portRequestList);
				
				return null;
			}
		});
		return portRequestResponce;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0018", errorMessage = "Failed to return port request status")
	public CheckPortRequestStatusResponse checkPortRequestStatus(final CheckPortRequestStatus parameters) throws ServiceException {
		// TODO Auto-generated method stub
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("checkPortRequestStatus", "CMB_SMS_0008", "Failed to return port request status");
		
		final CheckPortRequestStatusResponse checkPortRequestStatusResponse = new CheckPortRequestStatusResponse();
		
		execute(new ServiceInvocationCallback<CheckPortRequestStatusResponseType>() {

			@Override
			public CheckPortRequestStatusResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				PortRequestSummary portRequestSummary = getSubscriberLifecycleFacade(context).checkPortRequestStatus(parameters.getSubscriberNumber(), parameters.getBrandId());								
				CheckPortRequestStatusResponseType checkPortStatusResponseType = new CheckPortRequestStatusResponseType();				
				RetrievePortRequestCollection portRequestSummaryList = checkPortRequestStatusResponse.getRetrievePortRequestCollection();								
				portRequestSummaryList.getPortRequestList().add(getPortRequestSummaryMapper().mapToSchema(portRequestSummary));
				checkPortRequestStatusResponse.setPortRequestStatusResponseType(checkPortStatusResponseType);
				checkPortRequestStatusResponse.setRetrievePortRequestCollection(portRequestSummaryList);
				
				return null;
			}
		},new CheckPortRequestStatusResponseType(), exceptionContext);
		
		return checkPortRequestStatusResponse;
		
	}

	@Override
	public ValidatePortInRequestResponse validatePortInRequest(final ValidatePortInRequest parameters) throws ServiceException {
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validatePortInRequest", "CMB_SMS_0007", "Validate Port In Request Failed");
		ValidatePortInRequestResponseType result = execute(new ServiceInvocationCallback<ValidatePortInRequestResponseType>() {

			@Override
			public ValidatePortInRequestResponseType doInInvocationCallback(
					ServiceInvocationContext context) throws Throwable {
			try{		
				PortRequestInfo portRequestInfo=new SubscriberManagementServiceMapper().PortRequestMapper().mapToDomain(parameters.getPreportValidationRequest());
				getSubscriberLifecycleFacade(context).validatePortInRequest(portRequestInfo, parameters.getPortDataheader().getOriginatorName(),"user");	
			}catch(ApplicationException ae){
			if(ae.getErrorCode().equals("N")){
				
				PreportValidationResponseDataType preportValidationResponseData=new PreportValidationResponseDataType();
				preportValidationResponseData.setPreportValidationInd("PRM_FALSE");
			}else{
				throw ae;
			}
			}
			return null;
			}
		},new ValidatePortInRequestResponseType(), exceptionContext);
		
		ValidatePortInRequestResponse response = new ValidatePortInRequestResponse();
		response.setValidatePortInRequestResponseType(result);
		return response;
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0008", errorMessage = "Failed to activate subscriber with port-in request")
	public ActivatePortInRequestResponse activatePortInRequest(final ActivatePortInRequest parameters) throws ServiceException {
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("activatePortInRequest", "CMB_SMS_0008", "Failed to activate subscriber with port-in request");
		ActivatePortInRequestResponseType result = execute(new ServiceInvocationCallback<ActivatePortInRequestResponseType>() {

			@Override
			public ActivatePortInRequestResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				// map in-bound schema to Info objects
				ActivationChangeInfo activationChangeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), parameters.getActivationOption(), 
						parameters.isDealerHasDepositInd(), parameters.isActivationRequiredInd(), parameters.getActivityReasonCode(), 
						parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), parameters.isWaiveSearchFeeInd(), 
						parameters.getMemoText(), parameters.getPortinEligibility(), parameters.getSubscriberData(), 
						parameters.getServicesValidation(), parameters.getServiceAgreement(), parameters.getEquipmentRequest());
				ServiceRequestHeaderInfo servReqHeaderInfo = mapToServiceRequestHeaderInfo(parameters.getServiceRequestHeader(), parameters.getPortDataheader());
				SubscriberInfo reservedSubscriberInfo = mapSubscriberData(parameters);
				AuditInfo auditInfo = getAuditInfoMapper().mapToDomain(parameters.getAuditInfo());
				
				PortRequestInfo portRequestInfo = mapPortRequestInfo(reservedSubscriberInfo, parameters.getPortRequest(), parameters.getPortinEligibility());
				
				// call business logic implementation on the back-end
				getSubscriberLifecycleFacade(context).performPortInActivation(
						portRequestInfo, activationChangeInfo, servReqHeaderInfo, 
						auditInfo, PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT, false);
				
				// create response object
				ActivatePortInRequestResponseType response = new ActivatePortInRequestResponseType();
				// TODO: do we need to populate anything in the response object?
				return response;
			}
		}, new ActivatePortInRequestResponseType(), exceptionContext);
		
		ActivatePortInRequestResponse response = new ActivatePortInRequestResponse();
		response.setActivatePortInRequestResponseType(result);
		return response;
	}
	
	private SubscriberInfo mapSubscriberData(ActivatePortInRequest parameters) throws Exception {
		ActivationSubscriberData source = parameters.getSubscriberData();
		if(source == null) {
			// TODO: addError("SMS4.0_activatePortInRequest_Validation_Error", "ActivationSubscriberData is expected in the request");
			return null;
		}
		SubscriberInfo target = new SubscriberInfo();
		if(source.getBillingAccountNumber() != null) {
			target.setBanId(Integer.parseInt( source.getBillingAccountNumber()));
		}
		target.setSubscriberId(source.getSubscriberId());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setProductType(source.getProductType());
		if(source.getMarketProvince()!=null) {
			target.setMarketProvince(source.getMarketProvince().value());
		}
		target.setBrandId(source.getBrandId());
		if(source.getConsumerName()!=null){
			ConsumerNameInfo nameInfo = CustomerManagementCommonMapper.ConsumerNameMapper().mapToDomain(source.getConsumerName());	
			if (nameInfo!=null) {
				((ConsumerNameInfo) target.getConsumerName()).copyFrom(nameInfo );
			}
		}
		target.setAddress(CustomerManagementCommonMapper.AddressMapper().mapToDomain(source.getAddress()));
		target.setEmailAddress(source.getEmailAddress());
		if(source.getLanguage()!=null) { 
			target.setLanguage(source.getLanguage().value());
		}
		// TODO: map memberIdentity if not null
		target.setActivityReasonCode(parameters.getActivityReasonCode());
		target.setDealerCode(parameters.getUserProfile().getDealerCode());
		target.setSalesRepId(parameters.getUserProfile().getSalesRepCode());
		if(source.getSeatGroupCd() != null || source.getSeatTypeCd() != null) {
			SeatData seatData = new SeatDataInfo();
			seatData.setSeatGroup(source.getSeatGroupCd());
			seatData.setSeatType(source.getSeatTypeCd());
			target.setSeatData(seatData);
		}
		return target;
	}
	
	@Override
	public ActivateReservedSubscriberResponse activateReservedSubscriber(final ActivateReservedSubscriber parameters) throws ServiceException {
		// TODO Auto-generated method stub
		return new ActivateReservedSubscriberResponse();
	}

	@Override
	public PingResponse ping(final Ping parameters)
			throws ServiceException {
		PingStats stats = new PingStats();
		stats.setServiceName("SubscriberManagementService_40");
		// TODO set other statistics
		PingResponse response = new PingResponse();
		response.setPingStats(stats);
		return response;
	}
	
	/**
	 * Maps schema objects to PortRequestInfo object for communication with the backend.
	 * @param reservedSubscriberInfo
	 * @param portRequest
	 * @param portinEligibility
	 * @return
	 * @throws Exception
	 */
	private PortRequestInfo mapPortRequestInfo(SubscriberInfo reservedSubscriberInfo, PortRequestData portRequest, PortInEligibility portinEligibility) throws Exception {
		PortRequestInfo portRequestInfo = SubscriberManagementServiceMapper.PortRequestMapper().mapToDomain(portRequest);
		portRequestInfo.setSubscriber(reservedSubscriberInfo);
		// the following 2 fields will be updated on the backend
		//portRequestInfo.setAccount(flowContext.getAccountInfo());
		//portRequestInfo.setEquipment(flowContext.getEquipmentInfo());
		portRequestInfo.setPortDirectionIndicator(portinEligibility.getPortDirectionCd());
		portRequestInfo.setOutgoingBrandId(Integer.parseInt(portinEligibility.getOutgoingBrandCd()));
		portRequestInfo.setIncomingBrandId(Integer.parseInt(portinEligibility.getIncommingBrandCd()));
		return portRequestInfo;
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
	
	private com.telus.api.portability.PortRequest getLastPortRequest(ServiceInvocationContext context,String accountNumber) throws NumberFormatException, ApplicationException, Exception{
		PortRequest lastPortRequest = null;
		com.telus.api.portability.PortRequest[]  portRequestArray = null;
		try{
			portRequestArray = getSubscriberLifecycleFacade(context).getCurrentPortRequestsByBan(Integer.parseInt(accountNumber));
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
		if (creationDate.after(todayMinusOne)|| creationDate.equals(todayMinusOne))
			return true;
		return false;

	}
}