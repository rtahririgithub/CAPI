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

import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.cmb.jws.mapping.account_information_30.AccountMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v8.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_20.SubscriberMapper_20;
import com.telus.cmb.mapper.SubscriberManagementServiceMapper;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.cmb.jws.ChangeEquipmentResponseType;
import com.telus.cmb.jws.ChangePhoneNumberResponseType;
import com.telus.cmb.jws.GetServiceAgreementForUpdateResponseType;
import com.telus.cmb.jws.OpenSession;
import com.telus.cmb.jws.OpenSessionResponse;
import com.telus.cmb.jws.PrepopulateCallingCircleListResponseType;
import com.telus.cmb.jws.SaveServiceAgreementResponseType;
import com.telus.cmb.jws.SubscriberData;
import com.telus.cmb.jws.ValidateServiceAgreementResponseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.PhoneNumberChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;

/**
 * @author Naresh Annabathula
 *
 */


@WebService(
		portName = "SubscriberManagementServicePort", 
		serviceName = "SubscriberManagementService_v2_1", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_2", 
		wsdlLocation = "/wsdls/SubscriberManagementService_v2_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberManagementService_21 extends BaseService implements SubscriberManagementServicePort {
	
	public SubscriberManagementService_21() {
		super( new SubscriberManagementExceptionTranslator());
	}
	
	public OpenSessionResponse openSession(final OpenSession openSession) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<OpenSessionResponse>() {
			@Override
			public OpenSessionResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				OpenSessionResponse response=new OpenSessionResponse();
				response.setSessionIdentifier(getSubscriberLifecycleFacade(context).openSession(openSession.getPrincipal(), 
						openSession.getCredential(), openSession.getApplicationCode()));
				return response;
			}
		});	
	}
	
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0001", errorMessage="Failed to change equipment")
	public ChangeEquipmentResponseType changeEquipment(	final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile, final EquipmentChangeRequest equipmentChangeRequest,
		    final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<ChangeEquipmentResponseType>() {

			@Override
			public ChangeEquipmentResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				String sessionId = getSessionId(sessionIdentifier, context);
				
				EquipmentChangeRequestInfo equipmentChangeRequestInfo = getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest);
				equipmentChangeRequestInfo.setSalesRepCode(userProfile.getSalesRepCode());
				equipmentChangeRequestInfo.setDealerCode(userProfile.getDealerCode());
				
				if(subscriberData.getAccount()!=null && subscriberData.getSubscriber()!=null) {
					equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(
							 getSubscriberMapper_20().mapToDomain(subscriberData.getSubscriber()), 
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
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0002", errorMessage="Failed to change phone #")
	public ChangePhoneNumberResponseType changePhoneNumber(	final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile, final PhoneNumberChangeRequest phoneNumberChangeRequest,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ChangePhoneNumberResponseType>(){

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
				
				PhoneNumberChangeInfo newPhoneNumberChangeInfo =null;
				if(subscriberData.getAccount()!=null && subscriberData.getSubscriber()!=null) {
					 newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(
							getSubscriberMapper_20().mapToDomain(subscriberData.getSubscriber()), 
							 getAccountMapper_30().mapToDomain(subscriberData.getAccount()), phoneNumberChangeInfo, sessionId);
				} else {
					 newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(
						subscriberData.getSubscriberId(), Integer.valueOf(subscriberData.getBillingAccountNumber()), 
						phoneNumberChangeInfo, sessionId);
				}
				
				ChangePhoneNumberResponseType changePhoneNumberResponse = new ChangePhoneNumberResponseType();
				
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
						changePhoneNumberResponse.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					} catch (SystemException se) {
						WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
						changePhoneNumberResponse.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
					}
				}
				
				@SuppressWarnings("unchecked")
				List<WarningFaultInfo> warningList = newPhoneNumberChangeInfo.getApplicationWarningList();
				List<WarningType> schemaWarningList = getServiceWarningMapper().mapToSchema(warningList);
				changePhoneNumberResponse.getWarningList().addAll(schemaWarningList);

				return changePhoneNumberResponse;
			}
			
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0003", errorMessage="Failed to get service agreement")
	public ServiceAgreement getServiceAgreement(final String billingAccountNumber,
			final String subscriberId, final String phoneNumber) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ServiceAgreement>() {
			@Override
			public ServiceAgreement doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberContractInfo subContractInfo = null;
				if(billingAccountNumber!=null && subscriberId!=null) {
					subContractInfo =getSubscriberLifecycleFacade(context).getBaseServiceAgreement(subscriberId,  Integer.parseInt(billingAccountNumber));
				} else if(phoneNumber !=null) {
					subContractInfo =getSubscriberLifecycleFacade(context).getBaseServiceAgreement(phoneNumber.toString());
				}
				
				return getServiceAgreementMapper().mapToSchema(subContractInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0004", errorMessage="Failed to get service agreement for update")
	public GetServiceAgreementForUpdateResponseType getServiceAgreementForUpdate(final String billingAccountNumber, 
			final String subscriberId, final String phoneNumber)throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<GetServiceAgreementForUpdateResponseType>() {

			@Override
			public GetServiceAgreementForUpdateResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ContractChangeInfo contractChangeInfo =null;
				
				if(billingAccountNumber!=null && subscriberId!=null) {
					contractChangeInfo=getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(subscriberId, Integer.valueOf(billingAccountNumber));
				} else if(phoneNumber !=null) { 
					contractChangeInfo=getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(phoneNumber);
				}
				
				GetServiceAgreementForUpdateResponseType response = new GetServiceAgreementForUpdateResponseType();
				if(contractChangeInfo!=null){
					response.setEquipmentChangeRequest(null);
					response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo()));
					response.setAccount(getAccountMapper_30().mapToSchema(contractChangeInfo.getCurrentAccountInfo()));
					response.setSubscriber(getSubscriberMapper_20().mapToSchema(contractChangeInfo.getCurrentSubscriberInfo()));
					
					UserServiceProfile userProfile = new UserServiceProfile();
					userProfile.setDealerCode(contractChangeInfo.getDealerCode());
					userProfile.setSalesRepCode(contractChangeInfo.getSalesRepCode());
					response.setUserProfile(userProfile);
				}
				return response;
			}
		});
	}


	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0005", errorMessage="Validate service agreement failed")
	public ValidateServiceAgreementResponseType validateServiceAgreement(final String sessionIdentifier, final SubscriberData subscriberData,
			final UserServiceProfile userProfile,	final ServiceAgreement currentServiceAgreement,final ServiceAgreement newServiceAgreement,
			final EquipmentChangeRequest equipmentChangeRequest)throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ValidateServiceAgreementResponseType>() {

			@Override
			public ValidateServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
								
				String sessionId = getSessionId(sessionIdentifier, context);
				
				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());
				
				if(subscriberData.getAccount()!=null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}
				
				if(subscriberData.getSubscriber()!=null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_20().mapToDomain(subscriberData.getSubscriber()));
				}
				
				if(subscriberData.getProductType()!=null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}
				
				if(currentServiceAgreement!=null) {
					//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}
				
				if(equipmentChangeRequest!=null) {
					contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				}
				
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				
				contractChangeInfo = getSubscriberLifecycleFacade(context).validateServiceAgreement(contractChangeInfo,sessionId);
								
				ValidateServiceAgreementResponseType response = new ValidateServiceAgreementResponseType();
				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo());
				SubscriberManagementServiceMapper.resetPricePlanTransactionType( serviceAgreement, contractChangeInfo);
				response.setValidatedServiceAgreement(serviceAgreement);
				response.setValidatedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
				response.setValidatedUserProfile(userProfile);
				return response;
			}
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0006", errorMessage="Failed to save service agreement")
	public SaveServiceAgreementResponseType saveServiceAgreement(final String sessionIdentifier, 
			final SubscriberData subscriberData,final UserServiceProfile userProfile,
			final ServiceAgreement currentServiceAgreement, final ServiceAgreement newServiceAgreement,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo, 
			final EquipmentChangeRequest equipmentChangeRequest, final ServiceRequestHeader serviceRequestHeader )
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SaveServiceAgreementResponseType>() {

			
			@Override
			public SaveServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				String sessionId = getSessionId(sessionIdentifier, context);
				
				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setActivationInd(false);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
				if(subscriberData.getAccount()!=null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}
				
				if(subscriberData.getSubscriber()!=null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_20().mapToDomain(subscriberData.getSubscriber()));
				}
				
				if(subscriberData.getProductType()!=null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}
					
				if(currentServiceAgreement!=null){
					//contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				}
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());
				if(equipmentChangeRequest!=null) {
					contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				}
				
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				
				contractChangeInfo = getSubscriberLifecycleFacade(context).saveServiceAgreement(contractChangeInfo, notificationSuppressionIndicator,auditInfoForDomain,sessionId);
				
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
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0007", errorMessage="Failed to prepopulate calling circle list")
	public PrepopulateCallingCircleListResponseType prepopulateCallingCircleList(final String sessionIdentifier, final SubscriberData subscriberData,
			final SubscriberData previousSubscriberData, final UserServiceProfile userProfile,	final ServiceAgreement currentServiceAgreement,
			final ServiceAgreement newServiceAgreement) throws PolicyException,	ServiceException {
		return execute (new ServiceInvocationCallback<PrepopulateCallingCircleListResponseType>() {

			@Override
			public PrepopulateCallingCircleListResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
												
				ContractChangeInfo contractChangeInfo = getContractChangeInfoMapper().mapToDomain(newServiceAgreement);
				contractChangeInfo.setBan(Integer.valueOf(subscriberData.getBillingAccountNumber()));
				contractChangeInfo.setSubscriberId(subscriberData.getSubscriberId());
				
				if(subscriberData.getAccount()!=null) {
					contractChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscriberData.getAccount()));
				}
				
				if(subscriberData.getSubscriber()!=null) {
					contractChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_20().mapToDomain(subscriberData.getSubscriber()));
				}
				
				if(subscriberData.getProductType()!=null) {
					contractChangeInfo.setProductType(subscriberData.getProductType());
				}
				
				if(previousSubscriberData!=null){
					contractChangeInfo.setPreviousBan(Integer.valueOf(previousSubscriberData.getBillingAccountNumber()));
					contractChangeInfo.setPreviousSubscriberId(previousSubscriberData.getSubscriberId());
					
					if(previousSubscriberData.getAccount()!=null)
					contractChangeInfo.setPreviousAccountInfo(getAccountMapper_30().mapToDomain(previousSubscriberData.getAccount()));
					
					if(previousSubscriberData.getSubscriber()!=null)
					contractChangeInfo.setPreviousSubscriberInfo(getSubscriberMapper_20().mapToDomain(previousSubscriberData.getSubscriber()));
					
					if(previousSubscriberData.getProductType()!=null)
					contractChangeInfo.setPreviousProductType(previousSubscriberData.getProductType());
				}
				
				if(currentServiceAgreement!=null)
				contractChangeInfo.setCurrentContractInfo(getServiceAgreementMapper().mapToDomain(currentServiceAgreement));
				
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
								
				SubscriberContractInfo subscriberContractInfo= getSubscriberLifecycleFacade(context).prepopulateCallingCircleList(contractChangeInfo);
								
				PrepopulateCallingCircleListResponseType response = new PrepopulateCallingCircleListResponseType();
				ServiceAgreement serviceAgreement = getServiceAgreementMapper().mapToSchema(subscriberContractInfo);
				SubscriberManagementServiceMapper.resetPricePlanTransactionType( serviceAgreement, contractChangeInfo);
				response.setServiceAgreement(serviceAgreement);
				
				return response;
			}

		});
	}

	private SubscriberManagementServiceMapper.ServiceAgreementMapper getServiceAgreementMapper() {
		return SubscriberManagementServiceMapper.ServiceAgreementMapper();
	}
	
	private AccountMapper getAccountMapper_30() {
		return AccountMapper.getInstance();
	}
	
	private SubscriberMapper_20 getSubscriberMapper_20() {
		return SubscriberMapper_20.getInstance();
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
	
	private String getSessionId(String sessionIdentifier, ServiceInvocationContext context) throws Throwable {
		return (sessionIdentifier == null || sessionIdentifier.trim().length() == 0) ? 
			context.getSubscriberLifecycleFacadeSessionId() : sessionIdentifier;
	}
	
}
