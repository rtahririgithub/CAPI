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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.apache.commons.lang.StringUtils;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.cmb.jws.mapping.SubscriberLifeCycleManagementServiceMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v8.AuditInfoMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.CancelSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.LifeCycleChangePropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.RestoreSuspendedSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscriberlifecyclecommontypes_v1.ResumeCancelledSubscriberPropertiesType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v8.ResponseMessage;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;


/**
 * @author Naresh Annabathula
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberLifeCycleManagementServicePort", 
		serviceName = "SubscriberLifeCycleManagementService_v1_3", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberLifeCycleManagementService_1", 
		wsdlLocation = "/wsdls/SubscriberLifeCycleManagementService_v1_3.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberLifeCycleManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberLifeCycleManagementService_13 extends BaseService implements SubscriberLifeCycleManagementServicePort{


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

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0009", errorMessage = "suspendSubscriber Error")
	public ResponseMessage suspendSubscriber(final UserServiceProfile userProfile, final String billingAccountNumber, final Boolean suspendAllSubscriberInd,
			final SuspendedPropertiesType suspendPropertiesType, final List<String> subscriberIdList, final AuditInfo auditInfo, final Boolean notificationSuppressionInd) throws PolicyException,
			ServiceException {

		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ResponseMessage responseMessage = new ResponseMessage();
				int ban = Integer.parseInt(billingAccountNumber);
				boolean suspendAllSubscriberIndicator = suspendAllSubscriberInd == null ? false : Boolean.valueOf(suspendAllSubscriberInd);
				try {
					if (suspendAllSubscriberIndicator) {
						context.getAccountLifecycleFacade().suspendAccount(ban, suspendPropertiesType.getActivityDate(), suspendPropertiesType.getReasonCode(), suspendPropertiesType.getMemoText(),
								null, null, context.getAccountLifeCycleFacadeSessionId());
					} else {
						ProductSubscriberListInfo[] productSubscriberListInfo = getAccountInformationHelper(context).retrieveProductSubscriberLists(ban);
						char accountStatus = getAccountStatusChangeAfterSuspend(subscriberIdList, productSubscriberListInfo);
						switch (accountStatus) {
							case AccountSummary.STATUS_SUSPENDED:
								context.getAccountLifecycleFacade().suspendAccount(ban, suspendPropertiesType.getActivityDate(), suspendPropertiesType.getReasonCode(),
										suspendPropertiesType.getMemoText(), null, null, context.getAccountLifeCycleFacadeSessionId());
								break;
							default:
								context.getAccountLifecycleFacade().suspendSubscribers(ban, suspendPropertiesType.getActivityDate(), suspendPropertiesType.getReasonCode(),
										subscriberIdList.toArray(new String[subscriberIdList.size()]), suspendPropertiesType.getMemoText(), null, null, context.getAccountLifeCycleFacadeSessionId());
						}
					}
					responseMessage.setMessageType("suspendSubscribers/Ban  successful");
					responseMessage.setDateTimeStamp(new Date());
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());

				}
				
				return null;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0010", errorMessage = "restore SuspendedSubscriber Error")
	public List<WarningType> restoreSuspendedSubscriber(UserServiceProfile userProfile, final String billingAccountNumber, final List<String> subscriberIdList, final Boolean restoreAllSubscriberInd,
			final SuspendedPropertiesType restorePropertiesType, String productType, final Boolean portInInd, ServiceRequestHeader serviceRequestHeader, AuditInfo auditInfo,
			Boolean notificationSuppressionInd) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<List<WarningType>>() {
			@Override
			public List<WarningType> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				boolean portInIndicator = portInInd == null ? false : Boolean.valueOf(portInInd);
				// this operation will not support portIn functionality , so to
				// be safe returning exception back to consumer if portIn set to
				// true.
				if (portInIndicator) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, "this operation will not support portIn functionality");
				}
				int ban = Integer.parseInt(billingAccountNumber);
				if (restoreAllSubscriberInd) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_UNKNOWN, "this operation will not support restoreAllSubscribers/restoreBan functionality");
				} else {
					ProductSubscriberListInfo[] productSubscriberListInfo = getAccountInformationHelper(context).retrieveProductSubscriberLists(ban);
					checkAllSubscribersInSuspendedState(subscriberIdList, productSubscriberListInfo);

					context.getAccountLifecycleFacade().restoreSuspendedSubscribers(ban, restorePropertiesType.getActivityDate(), restorePropertiesType.getReasonCode(),
							subscriberIdList.toArray(new String[subscriberIdList.size()]), restorePropertiesType.getMemoText(), null, null, null, context.getAccountLifeCycleFacadeSessionId());
				}
				
				return null;
			}
		});
	}
	
	//A little adapter for the operation below as the Maven project generates its web service stub a little differently.
	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0011", errorMessage = "Reserve phone number for subscriber error")
	public void reservePhoneNumberForSubscriber(
			String billingAccountNumber,
			NumberGroup numberGroup,
			String equipmentSerialNumber,
			String productType,
			Boolean likeMatchInd,
			String phoneNumberPattern,
			Boolean asianFriendlyInd,
			Boolean portInReserveRequiredInd,
			PortInEligibility portInEligibility,
			Boolean offlineReservationInd,
			UserServiceProfile userProfile,
			Holder<String> subscriberId,
			Holder<String> phoneNumber)
			throws PolicyException, ServiceException {
		ReservePhoneNumberForSubscriber request = new ReservePhoneNumberForSubscriber();
		request.setAsianFriendlyInd(asianFriendlyInd);
		request.setBillingAccountNumber(billingAccountNumber);
		request.setEquipmentSerialNumber(equipmentSerialNumber);
		request.setLikeMatchInd(likeMatchInd);
		request.setNumberGroup(numberGroup);
		request.setOfflineReservationInd(offlineReservationInd);
		request.setPhoneNumberPattern(phoneNumberPattern);
		request.setPortInEligibility(portInEligibility);
		request.setPortInReserveRequiredInd(portInReserveRequiredInd);
		request.setProductType(productType);
		request.setUserProfile(userProfile);
		ReservePhoneNumberForSubscriberResponse response = reservePhoneNumberForSubscriber(request);
		phoneNumber.value = response.getPhoneNumber();
		subscriberId.value = response.getSubscriberId();
	}		
	
	private ReservePhoneNumberForSubscriberResponse reservePhoneNumberForSubscriber(final ReservePhoneNumberForSubscriber parameters) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<ReservePhoneNumberForSubscriberResponse>() {
			@Override
			public ReservePhoneNumberForSubscriberResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ReservePhoneNumberForSubscriberResponse response = new ReservePhoneNumberForSubscriberResponse();

				boolean islikeMatch = parameters.isLikeMatchInd() == null ? false : Boolean.valueOf(parameters.isLikeMatchInd());
				boolean asianFriendly = parameters.isAsianFriendlyInd() == null ? false : Boolean.valueOf(parameters.isAsianFriendlyInd());
				boolean offlineReservation = parameters.isOfflineReservationInd() == null ? false : Boolean.valueOf(parameters.isOfflineReservationInd());
				boolean portInReserveRequired = parameters.getPortInEligibility() != null;

				PhoneNumberReservationInfo phoneNumberReservationInfo = new PhoneNumberReservationInfo();
				phoneNumberReservationInfo.setNumberGroup0(SubscriberLifeCycleManagementServiceMapper.NumberGroupMapper().mapToDomain(parameters.getNumberGroup()));
				phoneNumberReservationInfo.setAsian(asianFriendly);
				phoneNumberReservationInfo.setLikeMatch(islikeMatch);
				phoneNumberReservationInfo.setPhoneNumberPattern(parameters.getPhoneNumberPattern());
				phoneNumberReservationInfo.setProductType(parameters.getProductType());

				SubscriberInfo subscriberInfo = new SubscriberInfo();
				subscriberInfo.setProductType(parameters.getProductType());
				subscriberInfo.setBanId(Integer.parseInt(parameters.getBillingAccountNumber()));
				subscriberInfo.setSerialNumber(parameters.getEquipmentSerialNumber());

				SubscriberInfo reservedSubscriberInfo;
				if (offlineReservation) {
					reservedSubscriberInfo = context.getSubscriberLifecycleFacade().reserveOnHoldPhoneNumber(phoneNumberReservationInfo, subscriberInfo, context.getSubscriberLifecycleFacadeSessionId());
				} else if (portInReserveRequired) {
					if (StringUtils.isBlank(parameters.getPortInEligibility().getIncommingBrandCd()) || StringUtils.isBlank(parameters.getPortInEligibility().getOutgoingBrandCd())
							|| StringUtils.isBlank(parameters.getPortInEligibility().getPlatformId())) {
						throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS, ServiceErrorCodes.ERROR_DESC_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS);
					}
					PortInEligibilityInfo portInEligibilityinfo = SubscriberLifeCycleManagementServiceMapper.PortInEligibilityMapper().mapToDomain(parameters.getPortInEligibility());
					reservedSubscriberInfo = context.getSubscriberLifecycleFacade().reservePortedInPhoneNumber(phoneNumberReservationInfo, subscriberInfo, portInEligibilityinfo,
							context.getSubscriberLifecycleFacadeSessionId());
				} else {
					reservedSubscriberInfo = context.getSubscriberLifecycleFacade().reservePhoneNumber(phoneNumberReservationInfo, subscriberInfo, context.getSubscriberLifecycleFacadeSessionId());
				}

				response.setPhoneNumber(reservedSubscriberInfo.getPhoneNumber());
				response.setSubscriberId(reservedSubscriberInfo.getSubscriberId());
				return response;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_SLCMS_0012", errorMessage = "releaseSubscriber Error")
	public ResponseMessage releaseSubscriber(final String billingAccountNumber, final String subscriberId, final String productType, final boolean cancelPortInInd,
			final PortInEligibility portInEligibility, UserServiceProfile userProfile) throws ServiceException, PolicyException {
	
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ResponseMessage responseMessage = new ResponseMessage();
				try {
					SubscriberInfo subscriberInfo = new SubscriberInfo();
					subscriberInfo.setProductType(productType);
					subscriberInfo.setBanId(Integer.parseInt(billingAccountNumber));
					subscriberInfo.setSubscriberId(subscriberId);
					PortInEligibilityInfo portInEligibilityinfo = null;
					if (cancelPortInInd) {
						if (portInEligibility != null && portInEligibility.getIncommingBrandCd() != null && portInEligibility.getOutgoingBrandCd() != null && portInEligibility.getPlatformId() != null) {
							portInEligibilityinfo = SubscriberLifeCycleManagementServiceMapper.PortInEligibilityMapper().mapToDomain(portInEligibility);
						} else {
							throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS,
									ServiceErrorCodes.ERROR_DESC_MISSING_MANDATORY_PORTIN_ELIGIBILITY_PARAMETERS);
						}
					}

					context.getSubscriberLifecycleFacade().unreservePhoneNumber(subscriberInfo, cancelPortInInd, portInEligibilityinfo, context.getSubscriberLifecycleFacadeSessionId());
					responseMessage.setMessageType("subscriber released  successfully");
					responseMessage.setDateTimeStamp(new Date());
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
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

	private void checkAllSubscribersInSuspendedState(List<String> subscriberIdList, ProductSubscriberListInfo[] productSubscriberListInfoArray) {
		
		List<String> activeSubscribers = new ArrayList<String>();
		for (ProductSubscriberListInfo productSubscriberListInfo : productSubscriberListInfoArray) {
			for (SubscriberIdentifier subscriberIdentifier : productSubscriberListInfo.getSuspendedSubscriberIdentifiers()) {
				activeSubscribers.add(subscriberIdentifier.getSubscriberId());
			}
		}
		for (String subscriberId : subscriberIdList) {
			if (!activeSubscribers.contains(subscriberId)) {
				throw new ServicePolicyException(ServiceErrorCodes.ERROR_DESC_INPUT, "Subscriber " + subscriberId + " is not in Suspended State. Subscriber should be in suspend State to restore.");
			}
		}
	}

	private char getAccountStatusChangeAfterSuspend(List<String> subscriberIdList, ProductSubscriberListInfo[] productSubscriberListInfoArray) {
		
		List<String> activeSubscribers = new ArrayList<String>();
		for (ProductSubscriberListInfo productSubscriberListInfo : productSubscriberListInfoArray) {
			for (SubscriberIdentifier subscriberIdentifier : productSubscriberListInfo.getActiveSubscriberIdentifiers()) {
				activeSubscribers.add(subscriberIdentifier.getSubscriberId());
			}
		}
		for (String subscriberId : subscriberIdList) {
			if (!activeSubscribers.contains(subscriberId)) {
				throw new ServicePolicyException(ServiceErrorCodes.ERROR_DESC_INPUT, "Subscriber" + subscriberId + " is not in Active State. Subscriber should be in Active State to suspend.");
			}
		}
		if ((activeSubscribers.size() - subscriberIdList.size()) == 0) {
			return STATUS_SUSPENDED;
		}

		return STATUS_ACTIVE;
	}

}
	

