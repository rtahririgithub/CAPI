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
import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.WarningType;
import com.telus.cmb.jws.mapping.SubscriberLifeCycleManagementServiceMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v8.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.CancelSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.LifeCycleChangePropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.RestoreSuspendedSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.ResumeCancelledSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.ResponseMessage;

/**
 * @author Naresh
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberLifeCycleManagementServicePort", 
		serviceName = "SubscriberLifeCycleManagementService_v1_1", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberLifeCycleManagementService_1", 
		wsdlLocation = "/wsdls/SubscriberLifeCycleManagementService_v1_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberLifeCycleManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberLifeCycleManagementService_11 extends BaseService implements SubscriberLifeCycleManagementServicePort{

	public static final char ACCOUNT_STATUS_NO_CHANGE = '\0';
	public static final char STATUS_RESERVED = 'R';
	public static final char STATUS_ACTIVE = 'A';
	public static final char STATUS_SUSPENDED = 'S';
	public static final char STATUS_CANCELED = 'C';

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0001", errorMessage = "Cancel Subscriber Error")
	public List<WarningType> cancelSubscriber(String billingAccountNumber, String subscriberId, CancelSubscriberPropertiesType cancelProperties, UserServiceProfile userProfile,
			Boolean notificationSuppresionInd, AuditInfo auditInfo, ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0002", errorMessage = "Cancel Subscriber Error")
	public List<WarningType> cancelSubscriber(String billingAccountNumber, String subscriberId, CancelSubscriberPropertiesType cancelProperties, UserServiceProfile userProfile,
			ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0003", errorMessage = "Resume Cancelled Subscriber Error")
	public List<WarningType> resumeCancelledSubscriber(final String billingAccountNumber, final String subscriberId, final ResumeCancelledSubscriberPropertiesType resumeProperties,
			final UserServiceProfile userProfile, final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<WarningType>>() {

			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
				mapSubscriberLifecycleInfo(subLifecycleInfo, userProfile);
				mapSubscriberLifecycleInfo(subLifecycleInfo, resumeProperties);
				int ban = Integer.valueOf(billingAccountNumber).intValue();
				subLifecycleInfo = context.getSubscriberLifecycleFacade().resumeCancelledSubscriber(subscriberId, ban, subLifecycleInfo, context.getSubscriberLifecycleFacadeSessionId());
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
				servReqHeaderInfo.setApplicationName(context.getClientApplication());
				context.getSubscriberLifecycleFacade().reportChangeSubscriberStatus(subscriberId, ban, subLifecycleInfo, servReqHeaderInfo, context.getSubscriberLifecycleFacadeSessionId());
				List<WarningFaultInfo> warnings = subLifecycleInfo.getSystemWarningList();
				List<WarningType> serviceWarningList = getServiceWarningMapper().mapToSchema(warnings);
				
				return serviceWarningList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0004", errorMessage = "Resume Cancelled Subscriber Error")
	public List<WarningType> resumeCancelledSubscriber(final String billingAccountNumber, final String subscriberId, final ResumeCancelledSubscriberPropertiesType resumeProperties,
			final UserServiceProfile userProfile, final Boolean notificationSuppressionInd, final AuditInfo auditInfo, final ServiceRequestHeader serviceRequestHeader) throws PolicyException,
			ServiceException {
		
		return execute(new ServiceInvocationCallback<List<WarningType>>() {

			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
				mapSubscriberLifecycleInfo(subLifecycleInfo, userProfile);
				mapSubscriberLifecycleInfo(subLifecycleInfo, resumeProperties);
				int ban = Integer.valueOf(billingAccountNumber).intValue();
				subLifecycleInfo = context.getSubscriberLifecycleFacade().resumeCancelledSubscriber(subscriberId, ban, subLifecycleInfo, context.getSubscriberLifecycleFacadeSessionId());
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
				servReqHeaderInfo.setApplicationName(context.getClientApplication());
				context.getSubscriberLifecycleFacade().reportChangeSubscriberStatus(subscriberId, ban, subLifecycleInfo, servReqHeaderInfo, context.getSubscriberLifecycleFacadeSessionId());
				List<WarningFaultInfo> warnings = subLifecycleInfo.getSystemWarningList();
				List<WarningType> serviceWarningList = getServiceWarningMapper().mapToSchema(warnings);
		
				return serviceWarningList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0005", errorMessage = "Restore Suspended Subscriber Error")
	public List<WarningType> restoreSuspendedSubscriber(String billingAccountNumber, String subscriberId, RestoreSuspendedSubscriberPropertiesType restoreProperties, UserServiceProfile userProfile,
			ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0006", errorMessage = "Restore Suspended Subscriber Error")
	public List<WarningType> restoreSuspendedSubscriber(String billingAccountNumber, String subscriberId, RestoreSuspendedSubscriberPropertiesType restoreProperties, UserServiceProfile userProfile,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo, ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	private void mapSubscriberLifecycleInfo(SubscriberLifecycleInfo subLifecycleInfo, UserServiceProfile userProfile) {
		if (userProfile != null) {
			subLifecycleInfo.setDealerCode(userProfile.getDealerCode());
			subLifecycleInfo.setSalesRepCode(userProfile.getSalesRepCode());
		}
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0007", errorMessage = "Cancel PortOut Subscriber Error")
	public List<WarningType> cancelPortOutSubscriber(final String billingAccountNumber, final String phoneNumber, final CancelSubscriberPropertiesType cancelProperties,
			final boolean forceCancelImmediateIndicator, final UserServiceProfile userProfile, final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<WarningType>>() {
			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
				mapSubscriberLifecycleInfo(subLifecycleInfo, userProfile);
				mapSubscriberLifecycleInfo(subLifecycleInfo, cancelProperties);
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
				servReqHeaderInfo.setApplicationName(context.getClientApplication());
				subLifecycleInfo = context.getSubscriberLifecycleFacade().cancelPortOutSubscriber(billingAccountNumber, phoneNumber, cancelProperties.getEffectiveDate(),
						forceCancelImmediateIndicator, subLifecycleInfo, servReqHeaderInfo, false, null, context.getSubscriberLifecycleFacadeSessionId());
				List<WarningFaultInfo> warnings = subLifecycleInfo.getSystemWarningList();
				List<WarningType> serviceWarningList = getServiceWarningMapper().mapToSchema(warnings);
				
				return serviceWarningList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0008", errorMessage = "Cancel PortOut Subscriber Error")
	public List<WarningType> cancelPortOutSubscriber(final String billingAccountNumber, final String phoneNumber, final CancelSubscriberPropertiesType cancelProperties,
			final boolean forceCancelImmediateIndicator, final UserServiceProfile userProfile, final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<WarningType>>() {
			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
				mapSubscriberLifecycleInfo(subLifecycleInfo, userProfile);
				mapSubscriberLifecycleInfo(subLifecycleInfo, cancelProperties);
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
				servReqHeaderInfo.setApplicationName(context.getClientApplication());
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				subLifecycleInfo = context.getSubscriberLifecycleFacade().cancelPortOutSubscriber(billingAccountNumber, phoneNumber, cancelProperties.getEffectiveDate(),
						forceCancelImmediateIndicator, subLifecycleInfo, servReqHeaderInfo, notificationSuppressionIndicator, auditInfoForDomain, context.getSubscriberLifecycleFacadeSessionId());
				List<WarningFaultInfo> warnings = subLifecycleInfo.getSystemWarningList();
				List<WarningType> serviceWarningList = getServiceWarningMapper().mapToSchema(warnings);
				
				return serviceWarningList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0008a", errorMessage = "Cancel PortOut Subscriber Error")
	public List<WarningType> cancelPortOutSubscriber(final String billingAccountNumber, final String phoneNumber, final CancelSubscriberPropertiesType cancelProperties,
			final boolean forceCancelImmediateIndicator, final Boolean cancellationInd, final UserServiceProfile userProfile, final Boolean notificationSuppressionInd, final AuditInfo auditInfo,
			final ServiceRequestHeader serviceRequestHeader) throws PolicyException, ServiceException {

		System.out.println(" *** In latest cancelPortOutSubscriber(), cancellationInd -> " + cancellationInd);

		return execute(new ServiceInvocationCallback<List<WarningType>>() {

			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				SubscriberLifecycleInfo subLifecycleInfo = new SubscriberLifecycleInfo();
				mapSubscriberLifecycleInfo(subLifecycleInfo, userProfile);
				mapSubscriberLifecycleInfo(subLifecycleInfo, cancelProperties);
				ServiceRequestHeaderInfo servReqHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
				servReqHeaderInfo.setApplicationName(context.getClientApplication());
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				subLifecycleInfo = context.getSubscriberLifecycleFacade().cancelPortOutSubscriber(billingAccountNumber, phoneNumber, cancelProperties.getEffectiveDate(),
						forceCancelImmediateIndicator, subLifecycleInfo, servReqHeaderInfo, notificationSuppressionIndicator, auditInfoForDomain, context.getSubscriberLifecycleFacadeSessionId(),
						cancellationInd);
				List<WarningFaultInfo> warnings = subLifecycleInfo.getSystemWarningList();
				List<WarningType> serviceWarningList = getServiceWarningMapper().mapToSchema(warnings);
				
				return serviceWarningList;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0013", errorMessage = "activate SavedSubscriber Error")
	public ResponseMessage activateSavedSubscriber(String billingAccountNumber, String subscriberId, String activityReasonCode, Date startServiceDate, String memoText, Boolean portInInd,
			Boolean modifyPortRequestInd, ServiceRequestHeader serviceRequestHeader, AuditInfo auditInfo, Boolean notificationSuppressionInd, UserServiceProfile userProfile) throws PolicyException,
			ServiceException {
		throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
	}

	private void mapSubscriberLifecycleInfo(SubscriberLifecycleInfo subLifecycleInfo, LifeCycleChangePropertiesType changeProperties) {
		subLifecycleInfo.setMemoText(changeProperties.getMemoText());
		subLifecycleInfo.setReasonCode(changeProperties.getReasonCode());
	}

	private SubscriberLifeCycleManagementServiceMapper.ServiceWarningMapper getServiceWarningMapper() {
		return SubscriberLifeCycleManagementServiceMapper.ServiceWarningMapper();
	}	
}
