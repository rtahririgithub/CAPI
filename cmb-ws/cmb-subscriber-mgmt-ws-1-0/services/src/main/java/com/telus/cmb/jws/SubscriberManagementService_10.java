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

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
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
import com.telus.cmb.jws.SaveServiceAgreementResponseType;
import com.telus.cmb.jws.ValidateServiceAgreementResponseType;
import com.telus.cmb.jws.WarningType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.EquipmentChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.PhoneNumberChangeRequest;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.ServiceAgreement;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberManagementServicePort", 
		serviceName = "SubscriberManagementService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_1", 
		wsdlLocation = "/wsdls/SubscriberManagementService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberManagementService_10 extends BaseService implements SubscriberManagementServicePort {

	public SubscriberManagementService_10() {
		super( new SubscriberManagementExceptionTranslator());
	}

	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0001", errorMessage="Failed to change equipment")
	public ChangeEquipmentResponseType changeEquipment(final String billingAccountNumber, final String subscriberId, final UserServiceProfile userProfile, final EquipmentChangeRequest equipmentChangeRequest, final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<ChangeEquipmentResponseType>() {

			@Override
			public ChangeEquipmentResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				EquipmentChangeRequestInfo equipmentChangeRequestInfo = getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest);
				if (userProfile != null) {
					equipmentChangeRequestInfo.setSalesRepCode(userProfile.getSalesRepCode());
					equipmentChangeRequestInfo.setDealerCode(userProfile.getDealerCode());
				}
				equipmentChangeRequestInfo = getSubscriberLifecycleFacade(context).changeEquipment(subscriberId, Integer.parseInt(billingAccountNumber), equipmentChangeRequestInfo, context.getSubscriberLifecycleFacadeSessionId());
				ChangeEquipmentResponseType response = new ChangeEquipmentResponseType();
				try {
					ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
					servReqHeaderInfo.setApplicationName(context.getClientApplication());
					getSubscriberLifecycleFacade(context).reportChangeEquipment(subscriberId, Integer.parseInt(billingAccountNumber), equipmentChangeRequestInfo, servReqHeaderInfo, context.getSubscriberLifecycleFacadeSessionId());
				}catch (ApplicationException ae) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
					response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
				}catch (SystemException se) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
					response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
				}
				response.getApplicationMessageList().addAll(getApplicationMessageMapper().mapToSchema(equipmentChangeRequestInfo.getApplicationMessageList()));
				response.getWarningList().addAll(getServiceWarningMapper().mapToSchema(equipmentChangeRequestInfo.getSystemWarningList()));
				return response;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0002", errorMessage="Failed to get service agreement")
	public ServiceAgreement getServiceAgreement(final String billingAccountNumber, final String subscriberId) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ServiceAgreement>() {

			@Override
			public ServiceAgreement doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberContractInfo subContractInfo = getSubscriberLifecycleFacade(context).getBaseServiceAgreement(subscriberId,  Integer.parseInt(billingAccountNumber));
				ServiceAgreement sa = getServiceAgreementMapper().mapToSchema(subContractInfo);
				return sa;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0003", errorMessage="Failed to get service agreement for update")
	public GetServiceAgreementForUpdateResponseType getServiceAgreementForUpdate(final String billingAccountNumber, final String subscriberId) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<GetServiceAgreementForUpdateResponseType>() {

			@Override
			public GetServiceAgreementForUpdateResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ContractChangeInfo contractChangeInfo = getSubscriberLifecycleFacade(context).getServiceAgreementForUpdate(subscriberId, Integer.valueOf(billingAccountNumber));
				SubscriberContractInfo newContractInfo = contractChangeInfo.getNewSubscriberContractInfo();
				GetServiceAgreementForUpdateResponseType response = new GetServiceAgreementForUpdateResponseType();
				response.setBan(billingAccountNumber);
				response.setEquipmentChangeRequest(null);
				response.setServiceAgreement(getServiceAgreementMapper().mapToSchema(newContractInfo));
				response.setSubscriberId(subscriberId);
				
				UserServiceProfile userProfile = new UserServiceProfile();
				userProfile.setDealerCode(contractChangeInfo.getDealerCode());
				userProfile.setSalesRepCode(contractChangeInfo.getSalesRepCode());
				response.setUserProfile(userProfile);
				return response;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0004", errorMessage="Validate service agreement failed")
	public ValidateServiceAgreementResponseType validateServiceAgreement(final String billingAccountNumber, final String subscriberId, final UserServiceProfile userProfile, final ServiceAgreement serviceAgreement,
			final EquipmentChangeRequest equipmentChangeRequest) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ValidateServiceAgreementResponseType>() {

			@Override
			public ValidateServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ContractChangeInfo contractChangeInfo = getConractChangeInfoMapper().mapToDomain(serviceAgreement);
				contractChangeInfo.setBan(Integer.valueOf(billingAccountNumber));
				contractChangeInfo.setSubscriberId(subscriberId);
				contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				contractChangeInfo = getSubscriberLifecycleFacade(context).validateServiceAgreement(contractChangeInfo, context.getSubscriberLifecycleFacadeSessionId());

				ValidateServiceAgreementResponseType response = new ValidateServiceAgreementResponseType();
				response.setValidatedServiceAgreement(getServiceAgreementMapper().mapToSchema(contractChangeInfo.getNewSubscriberContractInfo()));
				response.setValidatedEquipmentChangeRequest(getEquipmentChangeRequestMapper().mapToSchema(contractChangeInfo.getEquipmentChangeRequestInfo()));
				response.setValidatedUserProfile(userProfile);
				return response;
			}
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SMS_0005", errorMessage="Failed to save service agreement")
	public SaveServiceAgreementResponseType saveServiceAgreement(final String billingAccountNumber, final String subscriberId, final UserServiceProfile userProfile, final ServiceAgreement serviceAgreement,
			final EquipmentChangeRequest equipmentChangeRequest, final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SaveServiceAgreementResponseType>() {

			@Override
			public SaveServiceAgreementResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ContractChangeInfo contractChangeInfo = getConractChangeInfoMapper().mapToDomain(serviceAgreement);
				contractChangeInfo.setActivationInd(false);
				contractChangeInfo.setBan(Integer.valueOf(billingAccountNumber));
				contractChangeInfo.setDealerCode(userProfile.getDealerCode());
				contractChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				contractChangeInfo.setSubscriberId(subscriberId);
				contractChangeInfo.setEquipmentChangeRequestInfo(getEquipmentChangeRequestMapper().mapToDomain(equipmentChangeRequest));
				
				contractChangeInfo = getSubscriberLifecycleFacade(context).saveServiceAgreement(contractChangeInfo, false,null,context.getSubscriberLifecycleFacadeSessionId());
				
				SaveServiceAgreementResponseType response = new SaveServiceAgreementResponseType();
				
				try {
					ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
					servReqHeaderInfo.setApplicationName(context.getClientApplication());
					getSubscriberLifecycleFacade(context).reportChangeContract(subscriberId, Integer.parseInt(billingAccountNumber), contractChangeInfo, servReqHeaderInfo, context.getSubscriberLifecycleFacadeSessionId());
				}catch (ApplicationException ae) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
					response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
				}catch (SystemException se) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
					response.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
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
	@ServiceBusinessOperation(errorCode="CMB_SMS_0006", errorMessage="Failed to change phone #")
	public ChangePhoneNumberResponseType changePhoneNumber(final String billingAccountNumber, final String subscriberId, final UserServiceProfile userProfile, final PhoneNumberChangeRequest phoneNumberChangeRequest,
			final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<ChangePhoneNumberResponseType>(){

			@Override
			public ChangePhoneNumberResponseType doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				PhoneNumberChangeInfo phoneNumberChangeInfo = new PhoneNumberChangeInfo();
				phoneNumberChangeInfo.setDealerCode(userProfile.getDealerCode());
				phoneNumberChangeInfo.setSalesRepCode(userProfile.getSalesRepCode());
				phoneNumberChangeInfo.setSubscriberId(subscriberId);
				if (phoneNumberChangeRequest.isChangeOtherNumbersInd() != null) {
					phoneNumberChangeInfo.setChangeOtherPhoneNumbers(phoneNumberChangeRequest.isChangeOtherNumbersInd().booleanValue());
				}
				phoneNumberChangeInfo.setNewPhoneNumber(phoneNumberChangeRequest.getPhoneNumber().getPhoneNumber());
				phoneNumberChangeInfo.setReasonCode(phoneNumberChangeRequest.getReasonCode());
				
				PhoneNumberChangeInfo newPhoneNumberChangeInfo = getSubscriberLifecycleFacade(context).changePhoneNumber(subscriberId, Integer.valueOf(billingAccountNumber), phoneNumberChangeInfo, context.getSubscriberLifecycleFacadeSessionId());
				ChangePhoneNumberResponseType changePhoneNumberResponse = new ChangePhoneNumberResponseType();
				
				try {
					ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
					servReqHeaderInfo.setApplicationName(context.getClientApplication());
					getSubscriberLifecycleFacade(context).reportChangePhoneNumber(Integer.parseInt(billingAccountNumber), newPhoneNumberChangeInfo, servReqHeaderInfo, context.getSubscriberLifecycleFacadeSessionId());
				}catch (ApplicationException ae) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, ae.getSystemCode(), null, ae.getErrorCode(), ae.getErrorMessage(), null);
					changePhoneNumberResponse.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
				}catch (SystemException se) {
					WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, se.getSystemCode(), null, se.getErrorCode(), se.getErrorMessage(), null);
					changePhoneNumberResponse.getWarningList().add(getServiceWarningMapper().mapToSchema(warning));
				}
				
				@SuppressWarnings("unchecked")
				List<WarningFaultInfo> warningList = newPhoneNumberChangeInfo.getApplicationWarningList();
				List<WarningType> schemaWarningList = getServiceWarningMapper().mapToSchema(warningList);
				changePhoneNumberResponse.getWarningList().addAll(schemaWarningList);

				return changePhoneNumberResponse;
			}
			
		});
	}

	private SubscriberManagementServiceMapper.ApplicationMessageMapper getApplicationMessageMapper() {
		return SubscriberManagementServiceMapper.ApplicationMessageMapper();
	}

	private SubscriberManagementServiceMapper.EquipmentChangeRequestMapper getEquipmentChangeRequestMapper() {
		return SubscriberManagementServiceMapper.EquipmentChangeRequestMapper();
	}

	private SubscriberManagementServiceMapper.ServiceWarningMapper getServiceWarningMapper() {
		return SubscriberManagementServiceMapper.ServiceWarningMapper();
	}
	
	private SubscriberManagementServiceMapper.ServiceAgreementMapper getServiceAgreementMapper() {
		return SubscriberManagementServiceMapper.ServiceAgreementMapper();
	}
	
	private SubscriberManagementServiceMapper.ContractChangeInfoMapper getConractChangeInfoMapper() {
		return SubscriberManagementServiceMapper.ContractChangeInfoMapper();
	}
	
	
	
	
}
