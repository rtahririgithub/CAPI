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

import java.util.Date;
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
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.cmb.jws.ActivateSubscriberResponseType;
import com.telus.cmb.jws.ActivationSubscriberData;
import com.telus.cmb.jws.AnyToPrepaidMigrationOptions;
import com.telus.cmb.jws.ChangeEquipmentResponseType;
import com.telus.cmb.jws.ChangePhoneNumberResponseType;
import com.telus.cmb.jws.EquipmentActivated;
import com.telus.cmb.jws.GetServiceAgreementForUpdateResponseType;
import com.telus.cmb.jws.MigrateSubscriberResponseType;
import com.telus.cmb.jws.OpenSession;
import com.telus.cmb.jws.OpenSessionResponse;
import com.telus.cmb.jws.PortInEligibility;
import com.telus.cmb.jws.PrepopulateCallingCircleListResponseType;
import com.telus.cmb.jws.SaveServiceAgreementResponseType;
import com.telus.cmb.jws.ServicesValidation;
import com.telus.cmb.jws.SubscriberData;
import com.telus.cmb.jws.ValidateServiceAgreementResponseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.PhoneNumberChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v2.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;

@WebService(
		portName = "SubscriberManagementServicePort", 
		serviceName = "SubscriberManagementService_v2_3", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_2", 
		wsdlLocation = "/wsdls/SubscriberManagementService_v2_3.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberManagementService_23 extends BaseService implements SubscriberManagementServicePort {
	
	public SubscriberManagementService_23() {
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


	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0008", errorMessage="Validate Migrate Subscriber.")
	public MigrateSubscriberResponseType validateMigrateSubscriber(
			final UserServiceProfile userProfile, final SubscriberData subscribertData,
			final String migrationTypeCode, final String pricePlanCode,
			final Equipment newEquipment, final Equipment newAssociatedHandset,
			final String requestorId, final ActivationOption activationOption,
			final AnyToPrepaidMigrationOptions anyToPrepaidMigrationOptions,
			final Boolean dealerAcceptedDepositInd, final Boolean depositTransferInd,
			final Boolean phoneOnlyInd, final Date activityDate) throws PolicyException,
			ServiceException {

		// Oct 2013 release - only support 2 migration type, PRPO and MKPO
		assertValid("migrationTypeCode", migrationTypeCode, new String[]{"PRPO", "MKPO"}); 
		
		return execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MigrationChangeInfo migrationChangeInfo = getMigrationChangeInfo(userProfile, subscribertData, migrationTypeCode, pricePlanCode, newEquipment, newAssociatedHandset, requestorId, context);
				migrationChangeInfo = getSubscriberLifecycleFacade(context).validateMigrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());
				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(userProfile);
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));
				return response;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0009", errorMessage="Migrate Subscriber.")
	public MigrateSubscriberResponseType migrateSubscriber(
			final UserServiceProfile userProfile, final SubscriberData subscribertData,
			final String migrationTypeCode, final String pricePlanCode,
			final Equipment newEquipment, final Equipment newAssociatedHandset,
			final String requestorId, final ActivationOption activationOption,
			final AnyToPrepaidMigrationOptions anyToPrepaidMigrationOptions,
			final Boolean dealerAcceptedDepositInd, final Boolean depositTransferInd,
			final Boolean phoneOnlyInd, final Date activityDate,
			final ServiceRequestHeader serviceRequestHeader, final AuditInfo auditInfo,
			final Boolean notificationSuppressionInd) throws PolicyException,
			ServiceException {

		// Oct 2013 release - only support 2 migration type, PRPO and MKPO
		assertValid("migrationTypeCode", migrationTypeCode, new String[]{"PRPO", "MKPO"}); 

		return execute(new ServiceInvocationCallback<MigrateSubscriberResponseType>() {
			@Override
			public MigrateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MigrationChangeInfo migrationChangeInfo = getMigrationChangeInfo(userProfile, subscribertData, migrationTypeCode, pricePlanCode, newEquipment, newAssociatedHandset, requestorId, context);
				migrationChangeInfo = getSubscriberLifecycleFacade(context).migrateSubscriber(migrationChangeInfo, context.getSubscriberLifecycleFacadeSessionId());
				MigrateSubscriberResponseType response = new MigrateSubscriberResponseType();
				response.setUserProfile(userProfile);
				response.setAccount(getAccountMapper_30().mapToSchema(migrationChangeInfo.getNewAccountInfo()));
				response.setNewSubscriber(getSubscriberMapper_20().mapToSchema(migrationChangeInfo.getNewSubscriberInfo()));
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(migrationChangeInfo.getNewSubscriberContractInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(migrationChangeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(migrationChangeInfo.getSystemWarningList()));
				return response;
			}

		});
	}

	private MigrationChangeInfo getMigrationChangeInfo(UserServiceProfile userProfile, SubscriberData subscribertData,
			String migrationTypeCode, String pricePlanCode,
			Equipment newEquipment, Equipment newAssociatedHandset,
			String requestorId, ServiceInvocationContext context) {
		
		MigrationChangeInfo migrationChangeInfo = new MigrationChangeInfo();

		migrationChangeInfo.setNewBan(Integer.valueOf(subscribertData.getBillingAccountNumber()));
		migrationChangeInfo.setSubscriberId(subscribertData.getSubscriberId());

		if(subscribertData.getAccount()!=null) {
			migrationChangeInfo.setCurrentAccountInfo(getAccountMapper_30().mapToDomain(subscribertData.getAccount()));
		}
		
		if(subscribertData.getSubscriber()!=null) {
			migrationChangeInfo.setCurrentSubscriberInfo(getSubscriberMapper_20().mapToDomain(subscribertData.getSubscriber()));
		}
		
		if(subscribertData.getProductType()!=null) {
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
	@ServiceBusinessOperation(errorCode="CMB_SMS_0010", errorMessage="Activate Subscriber.")
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
			final Boolean notificationSuppressionInd) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<ActivateSubscriberResponseType>() {
			@Override
			public ActivateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ActivationChangeInfo changeInfo = getActivationChangeInfo(userProfile, activationOption, dealerHasDepositInd, activationRequiredInd, 
					activityReasonCode, startServiceDate, activationFeeChargeCode, waiveSearchFeeInd, memoText, portinEligibility, subscriberData, 
					servicesValidation, serviceAgreement, equipmentRequest);

				changeInfo = getSubscriberLifecycleFacade(context).activateSubscriber(changeInfo, context.getSubscriberLifecycleFacadeSessionId());
				
				ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
				response.setSubscriberInfo(getSubscriberMapper_20().mapToSchema(changeInfo.getNewSubscriberInfo()));
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));
				return response;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0011", errorMessage="Validate Activate Subscriber.")
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
			final EquipmentActivated equipmentRequest) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<ActivateSubscriberResponseType>() {
			@Override
			public ActivateSubscriberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ActivationChangeInfo changeInfo = getActivationChangeInfo(userProfile, null, dealerHasDepositInd, activationRequiredInd, 
					activityReasonCode, startServiceDate, activationFeeChargeCode, waiveSearchFeeInd, memoText, portinEligibility, subscriberData, 
					servicesValidation, serviceAgreement, equipmentRequest);

				changeInfo = getSubscriberLifecycleFacade(context).validateActivateSubscriber(changeInfo, context.getSubscriberLifecycleFacadeSessionId());
				
				ActivateSubscriberResponseType response = new ActivateSubscriberResponseType();
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(changeInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(changeInfo.getSystemWarningList()));
				return response;
			}
		});
	}

	private ActivationChangeInfo getActivationChangeInfo(
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
	
}
