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
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.resource.ResourceActivity;
import com.telus.cmb.jws.mapping.account_information_30.AccountMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriberMapper_30;
import com.telus.cmb.mapper.SubscriberManagementServiceMapper;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.ResourceInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.cmb.jws.ActivateSubscriberResponseType;
import com.telus.cmb.jws.ActivationSubscriberData;
import com.telus.cmb.jws.AnyToPrepaidMigrationOptions;
import com.telus.cmb.jws.ChangeEquipmentResponseType;
import com.telus.cmb.jws.ChangePhoneNumberResponseType;
import com.telus.cmb.jws.ChangeVoipNumberResponseType;
import com.telus.cmb.jws.ChangeVoipNumberWithChargeResponseType;
import com.telus.cmb.jws.EquipmentActivated;
import com.telus.cmb.jws.GetServiceAgreementForUpdateResponseType;
import com.telus.cmb.jws.GetServiceAgreementResponseType;
import com.telus.cmb.jws.MigrateSeatRequestType;
import com.telus.cmb.jws.MigrateSeatResponseType;
import com.telus.cmb.jws.MigrateSubscriberResponseType;
import com.telus.cmb.jws.MoveVoipNumberBetweenSeatsResponseType;
import com.telus.cmb.jws.OpenSession;
import com.telus.cmb.jws.OpenSessionResponseType;
import com.telus.cmb.jws.PortInEligibility;
import com.telus.cmb.jws.PrepopulateCallingCircleListResponseType;
import com.telus.cmb.jws.SaveServiceAgreementResponseType;
import com.telus.cmb.jws.SeatResource;
import com.telus.cmb.jws.SeatResourceNumberChangeDetail;
import com.telus.cmb.jws.ServicesValidation;
import com.telus.cmb.jws.SubscriberData;
import com.telus.cmb.jws.ValidateServiceAgreementResponseType;
import com.telus.cmb.jws.VoipNumberChangeRequest;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.PhoneNumberChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;

@WebService(
		portName = "SubscriberManagementServicePort", 
		serviceName = "SubscriberManagementService_v3_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_3", 
		wsdlLocation = "/wsdls/SubscriberManagementService_v3_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberManagementServicePort")		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberManagementService_30 extends BaseServiceV2 implements SubscriberManagementServicePort {
	
	public SubscriberManagementService_30() {
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
	public ChangeEquipmentResponseType changeEquipment(final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile, final EquipmentChangeRequest equipmentChangeRequest,
		    final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeEquipment", "CMB_SMS_0001", "Failed to change equipment");
		
		return execute(new ServiceInvocationCallback<ChangeEquipmentResponseType>() {

			@SuppressWarnings("unchecked")
			@Override
			public ChangeEquipmentResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(sessionIdentifier, context);

				EquipmentChangeRequestInfo equipmentChangeRequestInfo = getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest);
				equipmentChangeRequestInfo.setSalesRepCode(userProfile.getSalesRepCode());
				equipmentChangeRequestInfo.setDealerCode(userProfile.getDealerCode());				

				if (subscriberData.getAccount() != null && subscriberData.getSubscriber() != null) {
					equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(getSubscriberMapper_30().mapToDomain(subscriberData.getSubscriber()), 
							getAccountMapper_30().mapToDomain(subscriberData.getAccount()), equipmentChangeRequestInfo, sessionId);
				} else {				
					equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(subscriberData.getSubscriberId(),
							Integer.parseInt(subscriberData.getBillingAccountNumber()), equipmentChangeRequestInfo, sessionId);
				}

				ChangeEquipmentResponseType response = new ChangeEquipmentResponseType();

				if (serviceRequestHeader != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangeEquipment(subscriberData.getSubscriberId(), Integer.parseInt(subscriberData.getBillingAccountNumber()),
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
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0002", errorMessage = "Failed to change phone #")
	public ChangePhoneNumberResponseType changePhoneNumber(final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile, final PhoneNumberChangeRequest phoneNumberChangeRequest,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changePhoneNumber", "CMB_SMS_0002", "Failed to change phone #");
		
		return execute(new ServiceInvocationCallback<ChangePhoneNumberResponseType>() {

			@SuppressWarnings("unchecked")
			@Override
			public ChangePhoneNumberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(sessionIdentifier, context);

				PhoneNumberChangeInfo phoneNumberChangeInfo = new PhoneNumberChangeInfo();
				phoneNumberChangeInfo.setDealerCode(userProfile.getDealerCode());
				phoneNumberChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				phoneNumberChangeInfo.setSubscriberId(subscriberData.getSubscriberId());
				if (phoneNumberChangeRequest.isChangeOtherNumbersInd() != null) {
					phoneNumberChangeInfo.setChangeOtherPhoneNumbers(phoneNumberChangeRequest.isChangeOtherNumbersInd().booleanValue());
				}
				phoneNumberChangeInfo.setNewPhoneNumber(phoneNumberChangeRequest.getPhoneNumber().getPhoneNumber());
				phoneNumberChangeInfo.setReasonCode(phoneNumberChangeRequest.getReasonCode());

				PhoneNumberChangeInfo newPhoneNumberChangeInfo = null;

				if (subscriberData.getAccount() != null && subscriberData.getSubscriber() != null) {
					newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(getSubscriberMapper_30().mapToDomain(subscriberData.getSubscriber()), 
							getAccountMapper_30().mapToDomain(subscriberData.getAccount()), phoneNumberChangeInfo, sessionId);
				} else {
					newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(subscriberData.getSubscriberId(), 
							Integer.valueOf(subscriberData.getBillingAccountNumber()), phoneNumberChangeInfo, sessionId);
				}

				ChangePhoneNumberResponseType response = new ChangePhoneNumberResponseType();

				if (serviceRequestHeader != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangePhoneNumber(Integer.parseInt(subscriberData.getBillingAccountNumber()), newPhoneNumberChangeInfo, servReqHeaderInfo,
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
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0003", errorMessage = "Failed to get service agreement")
	public GetServiceAgreementResponseType getServiceAgreement(final String billingAccountNumber, final String subscriberId, final String phoneNumber) 
			throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("getServiceAgreement", "CMB_SMS_0003", "Failed to get service agreement");
		
		return execute(new ServiceInvocationCallback<GetServiceAgreementResponseType>() {
			
			@Override
			public GetServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				SubscriberContractInfo subContractInfo = null;

				if (billingAccountNumber != null && subscriberId != null) {
					subContractInfo = getSubscriberLifecycleFacade(context).getBaseServiceAgreement(subscriberId, Integer.parseInt(billingAccountNumber));
				} else if (phoneNumber != null) {
					subContractInfo = getSubscriberLifecycleFacade(context).getBaseServiceAgreement(phoneNumber.toString());
				}

				GetServiceAgreementResponseType response = new GetServiceAgreementResponseType();
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(subContractInfo));

				return response;
			}
			
		}, new GetServiceAgreementResponseType(), exceptionContext);
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0004", errorMessage = "Failed to get service agreement for update")
	public GetServiceAgreementForUpdateResponseType getServiceAgreementForUpdate(final String billingAccountNumber, final String subscriberId, 
			final String phoneNumber) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("getServiceAgreementForUpdate", "CMB_SMS_0004", "Failed to get service agreement for update");
		
		return execute(new ServiceInvocationCallback<GetServiceAgreementForUpdateResponseType>() {

			@Override
			public GetServiceAgreementForUpdateResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ContractChangeInfo contractChangeInfo = null;

				if (billingAccountNumber != null && subscriberId != null) {
					contractChangeInfo = getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(subscriberId, Integer.valueOf(billingAccountNumber));
				} else if (phoneNumber != null) { 
					contractChangeInfo = getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(phoneNumber);
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
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0005", errorMessage = "Validate service agreement failed")
	public ValidateServiceAgreementResponseType validateServiceAgreement(final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile, final ServiceAgreement currentServiceAgreement, final ServiceAgreement newServiceAgreement,
			final EquipmentChangeRequest equipmentChangeRequest) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateServiceAgreement", "CMB_SMS_0005", "Validate service agreement failed");
		
		return execute(new ServiceInvocationCallback<ValidateServiceAgreementResponseType>() {

			@Override
			public ValidateServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				String sessionId = getSessionId(sessionIdentifier, context);

				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());
				if (subscriberData.getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}
				if (subscriberData.getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(subscriberData.getSubscriber()));
				}
				if (subscriberData.getProductType() != null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}
				if (currentServiceAgreement != null) {
					//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}
				if (equipmentChangeRequest != null) {
					contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				}
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());

				contractChangeInfo = getSubscriberLifecycleFacade(context).validateServiceAgreement(contractChangeInfo,sessionId);

				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo());
				SubscriberManagementServiceMapper.resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);

				ValidateServiceAgreementResponseType response = new ValidateServiceAgreementResponseType();
				response.setValidatedServiceAgreement(serviceAgreement);
				response.setValidatedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
				response.setValidatedUserProfile(userProfile);

				return response;
			}
			
		}, new ValidateServiceAgreementResponseType(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0006", errorMessage = "Failed to save service agreement")
	public SaveServiceAgreementResponseType saveServiceAgreement(final String sessionIdentifier, 
			final SubscriberData subscriberData, final UserServiceProfile userProfile,
			final ServiceAgreement currentServiceAgreement, final ServiceAgreement newServiceAgreement,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo, 
			final EquipmentChangeRequest equipmentChangeRequest, final ServiceRequestHeader serviceRequestHeader)
			throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("saveServiceAgreement", "CMB_SMS_0006", "Failed to save service agreement");
		
		return execute(new ServiceInvocationCallback<SaveServiceAgreementResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public SaveServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				String sessionId = getSessionId(sessionIdentifier, context);
				
				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setActivationInd(false);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));				
				if (subscriberData.getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}				
				if (subscriberData.getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(subscriberData.getSubscriber()));
				}				
				if (subscriberData.getProductType() != null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}
				if (currentServiceAgreement != null) {
					//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}				
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());				
				if (equipmentChangeRequest != null) {
					contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				}

				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;

				contractChangeInfo = getSubscriberLifecycleFacade(context).saveServiceAgreement(contractChangeInfo, notificationSuppressionIndicator, auditInfoForDomain, sessionId);

				SaveServiceAgreementResponseType response = new SaveServiceAgreementResponseType();

				if (serviceRequestHeader != null) {
					try {
						ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
						if (servReqHeaderInfo != null) {
							servReqHeaderInfo.setApplicationName(context.getClientApplication());
						}
						getSubscriberLifecycleFacade(context).reportChangeContract(subscriberData.getSubscriberId(), Integer.parseInt(subscriberData.getBillingAccountNumber()), contractChangeInfo,
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
				response.setSavedUserProfile(userProfile);
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(contractChangeInfo.getApplicationWarningList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(contractChangeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new SaveServiceAgreementResponseType(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0007", errorMessage = "Failed to prepopulate calling circle list")
	public PrepopulateCallingCircleListResponseType prepopulateCallingCircleList(final String sessionIdentifier, final SubscriberData subscriberData,
			final SubscriberData previousSubscriberData, final UserServiceProfile userProfile, final ServiceAgreement currentServiceAgreement,
			final ServiceAgreement newServiceAgreement) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("prepopulateCallingCircleList", "CMB_SMS_0007", "Failed to prepopulate calling circle list");
				
		return execute(new ServiceInvocationCallback<PrepopulateCallingCircleListResponseType>() {

			@Override
			public PrepopulateCallingCircleListResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());				
				if (subscriberData.getAccount() != null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}				
				if (subscriberData.getSubscriber() != null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_30().mapToDomain(subscriberData.getSubscriber()));
				}				
				if (subscriberData.getProductType() != null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}				
				if (previousSubscriberData != null) {
					contractChangeInfo.setPreviousBan(Integer.valueOf(previousSubscriberData.getBillingAccountNumber()));
					contractChangeInfo.setPreviousSubscriberId(previousSubscriberData.getSubscriberId());					
					if (previousSubscriberData.getAccount() != null) {
						contractChangeInfo.setPreviousAccountInfo(getAccountMapper_30().mapToDomain(previousSubscriberData.getAccount()));
					}
					if (previousSubscriberData.getSubscriber() != null) {
						contractChangeInfo.setPreviousSubscriberInfo(getSubscriberMapper_30().mapToDomain(previousSubscriberData.getSubscriber()));
					}
					if (previousSubscriberData.getProductType() != null) {
						contractChangeInfo.setPreviousProductType(previousSubscriberData.getProductType());
					}
				}
				if (currentServiceAgreement != null) {
					contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());

				SubscriberContractInfo subscriberContractInfo = getSubscriberLifecycleFacade(context).prepopulateCallingCircleList(contractChangeInfo);

				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(subscriberContractInfo);
				SubscriberManagementServiceMapper.resetPricePlanTransactionType(serviceAgreement, contractChangeInfo);
				
				PrepopulateCallingCircleListResponseType response = new PrepopulateCallingCircleListResponseType();
				response.setServiceAgreement(serviceAgreement);

				return response;
			}
			
		}, new PrepopulateCallingCircleListResponseType(), exceptionContext);
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0008", errorMessage = "Validate Migrate Subscriber.")
	public MigrateSubscriberResponseType validateMigrateSubscriber(
			final UserServiceProfile userProfile, final SubscriberData subscriberData,
			final String migrationTypeCode, final String pricePlanCode,
			final Equipment newEquipment, final Equipment newAssociatedHandset,
			final String requestorId, final ActivationOption activationOption,
			final AnyToPrepaidMigrationOptions anyToPrepaidMigrationOptions,
			final Boolean dealerAcceptedDepositInd, final Boolean depositTransferInd,
			final Boolean phoneOnlyInd, final Date activityDate) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateMigrateSubscriber", "CMB_SMS_0008", "Validate Migrate Subscriber.");
		
		return execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Oct 2013 release - only support 2 migration types: PRPO and MKPO
				assertValid("migrationTypeCode", migrationTypeCode, new String[] {"PRPO", "MKPO"}); 

				MigrationChangeInfo migrationChangeInfo = mapToMigrationChangeInfo(userProfile, subscriberData, migrationTypeCode, pricePlanCode, newEquipment, newAssociatedHandset, requestorId, context);
				migrationChangeInfo = getSubscriberLifecycleFacade(context).validateMigrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());

				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(userProfile);
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new MigrateSubscriberResponseType(), exceptionContext);
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0009", errorMessage = "Migrate Subscriber.")
	public MigrateSubscriberResponseType migrateSubscriber(
			final UserServiceProfile userProfile, final SubscriberData subscriberData,
			final String migrationTypeCode, final String pricePlanCode,
			final Equipment newEquipment, final Equipment newAssociatedHandset,
			final String requestorId, final ActivationOption activationOption,
			final AnyToPrepaidMigrationOptions anyToPrepaidMigrationOptions,
			final Boolean dealerAcceptedDepositInd, final Boolean depositTransferInd,
			final Boolean phoneOnlyInd, final Date activityDate,
			final ServiceRequestHeader serviceRequestHeader, final AuditInfo auditInfo,
			final Boolean notificationSuppressionInd, final String memoText) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("migrateSubscriber", "CMB_SMS_0009", "Migrate Subscriber.");
		
		return execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Oct 2013 release - only support 2 migration types: PRPO and MKPO
				assertValid("migrationTypeCode", migrationTypeCode, new String[] {"PRPO", "MKPO"}); 

				MigrationChangeInfo migrationChangeInfo = mapToMigrationChangeInfo(userProfile, subscriberData, migrationTypeCode, pricePlanCode, newEquipment, newAssociatedHandset, requestorId, context);
				migrationChangeInfo.setMemoText(memoText);
				migrationChangeInfo = getSubscriberLifecycleFacade(context).migrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());

				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(userProfile);
				response.setAccount(getAccountMapper_30().mapToSchema(migrationChangeInfo.getNewAccountInfo()));
				response.setNewSubscriber(getSubscriberMapper_30().mapToSchema(migrationChangeInfo.getNewSubscriberInfo()));
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrationChangeInfo.getNewSubscriberContractInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new MigrateSubscriberResponseType(), exceptionContext);
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
	public ActivateSubscriberResponseType activateSubscriber(
			final UserServiceProfile userProfile, 
			final ActivationOption activationOption,
			final Boolean dealerHasDepositInd,
			final boolean activationRequiredInd, 
			final String activityReasonCode,
			final Date startServiceDate, 
			final String activationFeeChargeCode,
			final Boolean waiveSearchFeeInd, 
			final String memoText,
			final PortInEligibility portinEligibility,
			final ActivationSubscriberData subscriberData,
			final ServicesValidation servicesValidation,
			final ServiceAgreement serviceAgreement,
			final EquipmentActivated equipmentRequest,
			final ServiceRequestHeader serviceRequestHeader, 
			final AuditInfo auditInfo,
			final Boolean notificationSuppressionInd) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("activateSubscriber", "CMB_SMS_0010", "Activate Subscriber.");
		
		return execute(new ServiceInvocationCallback<ActivateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public ActivateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ActivationChangeInfo changeInfo = mapToActivationChangeInfo(userProfile, activationOption, dealerHasDepositInd, activationRequiredInd, 
						activityReasonCode, startServiceDate, activationFeeChargeCode, waiveSearchFeeInd, memoText, portinEligibility, subscriberData, 
						servicesValidation, serviceAgreement, equipmentRequest);

				changeInfo = getSubscriberLifecycleFacade(context).activateSubscriber(changeInfo, context.getSubscriberLifecycleFacadeSessionId());

				ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
				response.setSubscriberInfo(getSubscriberMapper_30().mapToSchema(changeInfo.getNewSubscriberInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new ActivateSubscriberResponseType(), exceptionContext);
	}

	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0011", errorMessage = "Validate Activate Subscriber.")
	public ActivateSubscriberResponseType validateActivateSubscriber(
			final UserServiceProfile userProfile, 
			final Boolean dealerHasDepositInd,
			final boolean activationRequiredInd, 
			final String activityReasonCode,
			final Date startServiceDate, 
			final String activationFeeChargeCode,
			final Boolean waiveSearchFeeInd, 
			final String memoText,
			final PortInEligibility portinEligibility,
			final ActivationSubscriberData subscriberData,
			final ServicesValidation servicesValidation,
			final ServiceAgreement serviceAgreement,
			final EquipmentActivated equipmentRequest) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateActivateSubscriber", "CMB_SMS_0011", "Validate Activate Subscriber.");
		
		return execute(new ServiceInvocationCallback<ActivateSubscriberResponseType>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public ActivateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ActivationChangeInfo changeInfo = mapToActivationChangeInfo(userProfile, null, dealerHasDepositInd, activationRequiredInd, 
						activityReasonCode, startServiceDate, activationFeeChargeCode, waiveSearchFeeInd, memoText, portinEligibility, subscriberData, 
						servicesValidation, serviceAgreement, equipmentRequest);

				changeInfo = getSubscriberLifecycleFacade(context).validateActivateSubscriber(changeInfo, context.getSubscriberLifecycleFacadeSessionId());

				ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));

				return response;
			}
			
		}, new ActivateSubscriberResponseType(), exceptionContext);
	}

	private ActivationChangeInfo mapToActivationChangeInfo(
			UserServiceProfile userProfile, 
			ActivationOption activationOption, 
			Boolean dealerHasDepositInd,
			boolean activationRequiredInd, 
			String activityReasonCode,
			Date startServiceDate, 
			String activationFeeChargeCode,
			Boolean waiveSearchFeeInd, 
			String memoText,
			PortInEligibility portinEligibility,
			ActivationSubscriberData subscriberData,
			ServicesValidation servicesValidation,
			ServiceAgreement serviceAgreement,
			EquipmentActivated equipmentRequest) {
		
		ActivationChangeInfo changeInfo = new ActivationChangeInfo();
		if (subscriberData != null) {
			changeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
			changeInfo.setReservedSubscriberId(subscriberData.getSubscriberId());
			changeInfo.setProductType(subscriberData.getProductType());
			changeInfo.setPhoneNumber(subscriberData.getPhoneNumber());
			changeInfo.setMarketProvince(subscriberData.getMarketProvince().value());
			changeInfo.setBrandId(subscriberData.getBrandId());
			changeInfo.setLanguage(subscriberData.getLanguage().value());
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
	public MigrateSeatResponseType validateMigrateSeat(
			final UserServiceProfile userProfile, final SubscriberData subscriberData, final MigrateSeatRequestType migrateSeatRequest,
			final ServiceRequestHeader serviceRequestHeader, final AuditInfo auditInfo,	final Boolean notificationSuppressionInd)
					throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("validateMigrateSeat", "CMB_SMS_0012", "Failed to validate migrate seat");
		
		return execute(new ServiceInvocationCallback<MigrateSeatResponseType>() {
			
			@Override
			public MigrateSeatResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				MigrateSeatResponseType response = new MigrateSeatResponseType();

				// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
				assertValid("migrationTypeCode", migrateSeatRequest.getMigrationTypeCode(), new String[] {"MSPO", "POMS"}); 

				com.telus.eas.transaction.info.AuditInfo domainAuditInfo = auditInfo != null ? new AuditInfoMapper().mapToDomain(auditInfo) : null;
				MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(subscriberData, migrateSeatRequest, userProfile, context);

				// Make the call to the SubscriberLifecycleFacade EJB
				migrateSeatChangeInfo = getSubscriberLifecycleFacade(context).validateMigrateSeat(migrateSeatChangeInfo, notificationSuppressionInd, 
						domainAuditInfo, context.getSubscriberLifecycleFacadeSessionId());

				// Map the results to the response
				response.setUserProfile(userProfile);

				return response;
			}

		}, new MigrateSeatResponseType(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0013", errorMessage = "Failed to migrate seat")
	public MigrateSeatResponseType migrateSeat(
			final UserServiceProfile userProfile, final SubscriberData subscriberData, final MigrateSeatRequestType migrateSeatRequest,
			final ServiceRequestHeader serviceRequestHeader, final AuditInfo auditInfo,	final Boolean notificationSuppressionInd)
					throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("migrateSeat", "CMB_SMS_0013", "Failed to migrate seat");

		return execute(new ServiceInvocationCallback<MigrateSeatResponseType>() {

			@Override
			public MigrateSeatResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				MigrateSeatResponseType response = new MigrateSeatResponseType();

				// July 2014 release - only support 2 seat migrations: Mobile Seat -> Postpaid (MSPO) and Postpaid -> Mobile Seat (POMS)
				assertValid("migrationTypeCode", migrateSeatRequest.getMigrationTypeCode(), new String[] {"MSPO", "POMS"});

				com.telus.eas.transaction.info.AuditInfo domainAuditInfo = auditInfo != null ? new AuditInfoMapper().mapToDomain(auditInfo) : null;
				MigrateSeatChangeInfo migrateSeatChangeInfo = mapToMigrateSeatInfo(subscriberData, migrateSeatRequest, userProfile, context);

				// Make the call to the SubscriberLifecycleFacade EJB
				migrateSeatChangeInfo = getSubscriberLifecycleFacade(context).migrateSeat(migrateSeatChangeInfo, notificationSuppressionInd, 
						domainAuditInfo, context.getSubscriberLifecycleFacadeSessionId());

				// Map the results to the response
				response.setUserProfile(userProfile);
				response.setAccount(getAccountMapper_30().mapToSchema(migrateSeatChangeInfo.getNewAccountInfo()));
				response.setNewSubscriber(getSubscriberMapper_30().mapToSchema(migrateSeatChangeInfo.getNewSubscriberInfo()));
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrateSeatChangeInfo.getNewContractInfo()));

				return response;
			}

		}, new MigrateSeatResponseType(), exceptionContext);
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
	public ChangeVoipNumberResponseType changeVoipNumber(final String billingAccountNumber, 
			final long subscriptionId, final VoipNumberChangeRequest voipNumberChangeRequest,
			final Boolean outgoingRequestInd, final UserServiceProfile userProfile, final AuditInfo auditInfo) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeVoipNumber", "CMB_SMS_0014", "Failed to change VOIP #");

		return execute(new ServiceInvocationCallback<ChangeVoipNumberResponseType>() {

			@Override
			public ChangeVoipNumberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Create the response
				ChangeVoipNumberResponseType response = new ChangeVoipNumberResponseType();

				// Create the resource list
				List<ResourceActivityInfo> resourceList = new ArrayList<ResourceActivityInfo>();

				// Cycle through the change details and map to the resource list
				for (SeatResourceNumberChangeDetail detail : voipNumberChangeRequest.getVoipNumberChangeDetailList()) {
					
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
				getSubscriberLifecycleFacade(context).changeVOIPResource(subscriptionId, resourceList, null, outgoingRequestInd, context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new ChangeVoipNumberResponseType(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_SMS_0015", errorMessage = "Failed to change VOIP # with charge")
	public ChangeVoipNumberWithChargeResponseType changeVoipNumberWithCharge(final String billingAccountNumber, 
			final long subscriptionId, final SeatResourceNumberChangeDetail voipNumberChangeRequest, final String regularServiceCd,
			final Boolean outgoingRequestInd, final UserServiceProfile userProfile, final AuditInfo auditInfo) throws ServiceException {
		
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("changeVoipNumberWithCharge", "CMB_SMS_0015", "Failed to change VOIP # with charge");

		return execute(new ServiceInvocationCallback<ChangeVoipNumberWithChargeResponseType>() {

			@Override
			public ChangeVoipNumberWithChargeResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Create the response
				ChangeVoipNumberWithChargeResponseType response = new ChangeVoipNumberWithChargeResponseType();
				
				// Validate the action type code against supported values 'ADD' and 'DEL'
				assertValid("actionTypeCd", voipNumberChangeRequest.getActionTypeCd(), new String[] {"ADD", "DEL"});
				
				// Create a new resource and map seat resource details
				ResourceInfo resource = new ResourceInfo();
				resource.setResourceNumber(voipNumberChangeRequest.getSeatResource().getSeatResourceNumber());
				resource.setResourceType(voipNumberChangeRequest.getSeatResource().getSeatResourceTypeCd());

				// Create a new resource activity and map action type
				ResourceActivityInfo resourceActivity = new ResourceActivityInfo();
				resourceActivity.setResource(resource);
				resourceActivity.setResourceActivity(mapToResourceActivity(voipNumberChangeRequest.getActionTypeCd()));

				// Make the call to the SubscriberLifecycleFacade EJB
				getSubscriberLifecycleFacade(context).changeVOIPResourceWithCharge(subscriptionId, resourceActivity, null, outgoingRequestInd, regularServiceCd, context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new ChangeVoipNumberWithChargeResponseType(), exceptionContext);
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
	public MoveVoipNumberBetweenSeatsResponseType moveVoipNumberBetweenSeats(final String billingAccountNumber, final long sourceSubscriptionId,
			final long targetSubscriptionId, final SeatResource seatResource, final AuditInfo auditInfo) throws ServiceException {

		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("moveVoipNumberBetweenSeats", "CMB_SMS_0016", "Failed to move VOIP # bewteen seats");

		return execute(new ServiceInvocationCallback<MoveVoipNumberBetweenSeatsResponseType>() {

			@Override
			public MoveVoipNumberBetweenSeatsResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				// Note: this service operation is only expected to be called by Provisioning, so there's no need to call them to provision this change 
				// to the network. Therefore, the 'outgoingRequestInd' boolean is always set to 'false'.
				boolean outgoingRequestInd = false;

				// Create the response
				MoveVoipNumberBetweenSeatsResponseType response = new MoveVoipNumberBetweenSeatsResponseType();

				// Make the call to the SubscriberLifecycleFacade EJB
				getSubscriberLifecycleFacade(context).moveVOIPResource(sourceSubscriptionId, targetSubscriptionId, seatResource.getSeatResourceNumber(), null, outgoingRequestInd, context.getSubscriberLifecycleFacadeSessionId());

				return response;
			}

		}, new MoveVoipNumberBetweenSeatsResponseType(), exceptionContext);
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
	
	private String getSessionId(String sessionIdentifier, ServiceInvocationContext context) throws Throwable {
		return (sessionIdentifier == null || sessionIdentifier.trim().length() == 0) ? 
			context.getSubscriberLifecycleFacadeSessionId() : sessionIdentifier;
	}

	/*
	 * This Xml2Wsdl XSLT does not support this more sophisticated ping operation. The simpler one is needed instead.
	 *
	@Override
	public PingStats ping(String operationName, Boolean deepPing) throws ServiceException {
		PingStats pingStats = new PingStats();
		try {
			pingStats.setServiceName(super.ping());
		} catch (Exception e) {
			pingStats.setServiceName("SubscriberManagementService 3.0");
		}
		
		return pingStats;
	}
	*/

	@Override
	public String ping() throws ServiceException {
		return "SubscriberManagementService v.3.0";
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

	
	
}
