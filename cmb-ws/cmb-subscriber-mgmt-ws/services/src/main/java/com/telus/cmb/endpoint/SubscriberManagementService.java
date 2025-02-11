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
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getAuditInfoMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getPortInEligibilityMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServiceAgreementMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getServicesValidationMapper;
import static com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper.getSubscriberMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.TelusAPIException;
import com.telus.api.account.PaymentCard;
import com.telus.cmb.common.util.InfoObjectFactory;
import com.telus.cmb.endpoint.mapping.ActivateResponseDataTypeMapper;
import com.telus.cmb.endpoint.mapping.MigrateResponseDataTypeMapper;
import com.telus.cmb.endpoint.mapping.SubscriberManagementServiceMapper;
import com.telus.cmb.framework.endpoint.EndpointBusinessException;
import com.telus.cmb.framework.endpoint.EndpointOperation;
import com.telus.cmb.framework.endpoint.EndpointProviderV2;
import com.telus.cmb.jws.ServiceErrorCodes;
import com.telus.cmb.jws.mapping.customer_management_common_50.CustomerManagementCommonMapper;
import com.telus.cmb.jws.mapping.service_request.common.ServiceRequestMapper;
import com.telus.cmb.schema.ActivatePortInRequest;
import com.telus.cmb.schema.ActivatePortInRequestResponse;
import com.telus.cmb.schema.ActivateSubscriber;
import com.telus.cmb.schema.ActivateSubscriberResponse;
import com.telus.cmb.schema.ActivationOption;
import com.telus.cmb.schema.ActivationSubscriberData;
import com.telus.cmb.schema.EquipmentActivated;
import com.telus.cmb.schema.MigrateResponseDataType;
import com.telus.cmb.schema.MigrateSubscriber;
import com.telus.cmb.schema.MigrateSubscriberResponse;
import com.telus.cmb.schema.PortInEligibility;
import com.telus.cmb.schema.PortRequestData;
import com.telus.cmb.schema.PrepaidPaymentArrangement;
import com.telus.cmb.schema.ServicesValidation;
import com.telus.cmb.schema.SubscriberData;
import com.telus.cmb.schema.ValidateMigrateSubscriber;
import com.telus.cmb.schema.ValidateMigrateSubscriberResponse;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidDebitCardInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resourceorderservicecommontypes_v2.HeaderType;

/**
 * @author R. Fong
 *
 */
@WebService(portName = "SubscriberManagementServicePort", serviceName = "SubscriberManagementService_v5_0", targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/SubscriberManagementService_5", wsdlLocation = "/wsdls/SubscriberManagementService_v5_0.wsdl", endpointInterface = "com.telus.cmb.endpoint.SubscriberManagementServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class SubscriberManagementService extends EndpointProviderV2 implements SubscriberManagementServicePort {

	@Autowired
	private SubscriberLifecycleFacade subscriberLifecycleFacade;
	
	private static final String[] MIGRATION_TYPE_CODES = new String[] { "PRPO", "MKPO" };
	
	@Override
	public Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("subscriberLifecycleFacade", subscriberLifecycleFacade);
		return resources;
	}	
	
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0010", errorMessage = "")
	public ActivateSubscriberResponse activateSubscriber(ActivateSubscriber parameters) throws EndpointException {

		ActivationChangeInfo changeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), parameters.getActivationOption(), parameters.isDealerHasDepositInd(), 
				parameters.getActivityReasonCode(), parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), parameters.isWaiveSearchFeeInd(),
				parameters.getMemoText(), null, parameters.getSubscriberData(), parameters.getServicesValidation(), parameters.getServiceAgreement(), 
				parameters.getEquipmentRequest(), parameters.getSubscriptionRoleCd(),parameters.getPrepaidPaymentArrangement());

		changeInfo = subscriberLifecycleFacade.activateSubscriber(changeInfo, getSessionId(subscriberLifecycleFacade));

		ActivateSubscriberResponse response = new ActivateSubscriberResponse();
		response.setActivateSubscriberResponseData(ActivateResponseDataTypeMapper.getInstance().mapToSchema(changeInfo));

		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0019", errorMessage = "")
	public ActivatePortInRequestResponse activatePortInRequest(ActivatePortInRequest parameters) throws EndpointException {
		
		ActivationChangeInfo changeInfo = mapToActivationChangeInfo(parameters.getUserProfile(), parameters.getActivationOption(), parameters.isDealerHasDepositInd(),
				parameters.getActivityReasonCode(), parameters.getStartServiceDate(), parameters.getActivationFeeChargeCode(), parameters.isWaiveSearchFeeInd(),
				parameters.getMemoText(), parameters.getPortInEligibility(), parameters.getSubscriberData(), parameters.getServicesValidation(), parameters.getServiceAgreement(),
				parameters.getEquipmentRequest(), parameters.getSubscriptionRoleCd(),parameters.getPrepaidPaymentArrangement());
		PortRequestInfo portRequestInfo = mapPortRequestInfo(changeInfo.getCurrentSubscriberInfo(), parameters.getPortRequest(), parameters.getPortInEligibility());
		ServiceRequestHeaderInfo serviceRequestHeaderInfo = mapToServiceRequestHeaderInfo(parameters.getServiceRequestHeader(), parameters.getPortDataheader());
		AuditInfo auditInfo = getAuditInfoMapper().mapToDomain(parameters.getAuditInfo());		
		
		changeInfo = subscriberLifecycleFacade.performPortInActivation(getSessionId(subscriberLifecycleFacade), portRequestInfo, changeInfo, serviceRequestHeaderInfo, 
				auditInfo, PortInEligibilityInfo.PORT_PROCESS_INTER_CARRIER_PORT, false);
		
		ActivatePortInRequestResponse response = new ActivatePortInRequestResponse();
		response.setActivatePortInRequestResponseData(ActivateResponseDataTypeMapper.getInstance().mapToSchema(changeInfo));
		
		return respond(response);
	}
	
	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0008", errorMessage = "")
	public ValidateMigrateSubscriberResponse validateMigrateSubscriber(ValidateMigrateSubscriber parameters) throws EndpointException {

		if (!Arrays.asList(MIGRATION_TYPE_CODES).contains(parameters.getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		MigrationChangeInfo changeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), parameters.getSubscriberData(), 
				parameters.getMigrationTypeCode(), parameters.getPricePlanCode(), parameters.getNewEquipment(), 
				parameters.getNewAssociatedHandset(), parameters.getRequestorId(), parameters.getActivationOption());
		
		changeInfo = subscriberLifecycleFacade.validateMigrateSubscriber(changeInfo, getSessionId(subscriberLifecycleFacade));

		MigrateResponseDataType responseData = MigrateResponseDataTypeMapper.getInstance().mapToSchema(changeInfo);
		responseData.setUserProfile(parameters.getUserProfile());
		
		ValidateMigrateSubscriberResponse response = new ValidateMigrateSubscriberResponse();
		response.setValidateMigrateSubscriberResponseData(responseData);

		return respond(response);
	}

	@Override
	@EndpointOperation(errorCode = "CMB_SMS_0009", errorMessage = "")
	public MigrateSubscriberResponse migrateSubscriber(MigrateSubscriber parameters) throws EndpointException {

		if (!Arrays.asList(MIGRATION_TYPE_CODES).contains(parameters.getMigrationTypeCode())) {
			throw new EndpointBusinessException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Invalid MigrationTypeCode");
		}

		MigrationChangeInfo changeInfo = mapToMigrationChangeInfo(parameters.getUserProfile(), parameters.getSubscriberData(), 
				parameters.getMigrationTypeCode(),  parameters.getPricePlanCode(), parameters.getNewEquipment(), 
				parameters.getNewAssociatedHandset(), parameters.getRequestorId(), parameters.getActivationOption());		
		changeInfo.setMemoText(parameters.getMemoText());
		
		changeInfo = subscriberLifecycleFacade.migrateSubscriber(changeInfo, getSessionId(subscriberLifecycleFacade));

		MigrateResponseDataType responseData = MigrateResponseDataTypeMapper.getInstance().mapToSchema(changeInfo);
		responseData.setUserProfile(parameters.getUserProfile());
		
		MigrateSubscriberResponse response = new MigrateSubscriberResponse();
		response.setMigrateSubscriberResponseData(responseData);

		return respond(response);
	}

	private ActivationChangeInfo mapToActivationChangeInfo(UserServiceProfile userProfile, ActivationOption activationOption, Boolean dealerHasDepositInd, String activityReasonCode,
			Date startServiceDate, String activationFeeChargeCode, Boolean waiveSearchFeeInd, String memoText, PortInEligibility portinEligibility, ActivationSubscriberData subscriberData,
			ServicesValidation servicesValidation, ServiceAgreement serviceAgreement, EquipmentActivated equipmentRequest, String subscriptionRoleCd,
			PrepaidPaymentArrangement prepaidPaymentArrangement) throws TelusAPIException {

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
		if (prepaidPaymentArrangement != null) {
			List<PaymentCard> paymentCardList = new ArrayList<PaymentCard>();
			if (prepaidPaymentArrangement.getAirtimeCard() != null) {
				CardInfo cardInfo = new CardInfo();
				cardInfo.setSerialNumber(prepaidPaymentArrangement.getAirtimeCard().getSerialNumber());
				cardInfo.setPIN(prepaidPaymentArrangement.getAirtimeCard().getPinNumber());
				if (prepaidPaymentArrangement.getAirtimeCard().getDenominationAmount() != null) {
					cardInfo.setAmount(prepaidPaymentArrangement.getAirtimeCard().getDenominationAmount());
				}
				paymentCardList.add(cardInfo);
			}
			if (prepaidPaymentArrangement.getDebitCard() != null) {
				PrepaidDebitCardInfo debitInfo = new PrepaidDebitCardInfo();
				debitInfo.setTopupAmout(prepaidPaymentArrangement.getDebitCard().getActivationTopUpAmount());
				paymentCardList.add(debitInfo);
			}
			if (prepaidPaymentArrangement.getPreRegisteredCreditCard() != null) {
				PreRegisteredPrepaidCreditCardInfo creditCardInfo = new PreRegisteredPrepaidCreditCardInfo();
				creditCardInfo.setTopupAmout(prepaidPaymentArrangement.getPreRegisteredCreditCard().getActivationTopUpAmount());
				CreditCard creditCard = prepaidPaymentArrangement.getPreRegisteredCreditCard().getCreditCard();
				creditCardInfo.setToken(creditCard.getToken());
				creditCardInfo.setTrailingDisplayDigits(creditCard.getLast4());
				creditCardInfo.setLeadingDisplayDigits(creditCard.getFirst6());
				creditCardInfo.setExpiryMonth(creditCard.getExpiryMonth());
				creditCardInfo.setExpiryYear(creditCard.getExpiryYear());
				if (prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutomaticRechargeThresholdAmount() != null) {
					creditCardInfo.setThresholdAmount(prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutomaticRechargeThresholdAmount());
				}
				creditCardInfo.setTopupAmout(prepaidPaymentArrangement.getPreRegisteredCreditCard().getActivationTopUpAmount());
				if (prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutomaticRechargeAmount() != null) {
					creditCardInfo.setRechargeAmount(prepaidPaymentArrangement.getPreRegisteredCreditCard().getAutomaticRechargeAmount());
				}
				paymentCardList.add(creditCardInfo);
			}
			info.setupPaymentCardList(paymentCardList.toArray(new PaymentCard[paymentCardList.size()]));
			changeInfo.setTopUpPaymentArrangement(info);
		}

		return changeInfo;
	}
	
	private SubscriberInfo mapSubscriberData(ActivationSubscriberData subscriberData, UserServiceProfile userProfile, String activityReasonCode, Date startServiceDate) 
			throws TelusAPIException {

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
		target.setEmailAddress(subscriberData.getEmailAddressText());
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

	private PortRequestInfo mapPortRequestInfo(SubscriberInfo reservedSubscriberInfo, PortRequestData portRequest, PortInEligibility portinEligibility) {
		
		PortRequestInfo portRequestInfo = SubscriberManagementServiceMapper.getPortRequestMapper().mapToDomain(portRequest);
		portRequestInfo.setSubscriber(reservedSubscriberInfo);
		portRequestInfo.setPortDirectionIndicator(portinEligibility.getPortDirectionCd());
		portRequestInfo.setOutgoingBrandId(Integer.parseInt(portinEligibility.getOutgoingBrandCd()));
		portRequestInfo.setIncomingBrandId(Integer.parseInt(portinEligibility.getIncommingBrandCd()));
		
		return portRequestInfo;
	}
	
	private MigrationChangeInfo mapToMigrationChangeInfo(UserServiceProfile userProfile, SubscriberData subscribertData, String migrationTypeCode, String pricePlanCode, Equipment newEquipment,
			Equipment newAssociatedHandset, String requestorId, ActivationOption activationOption) {
		
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
	
	private ServiceRequestHeaderInfo mapToServiceRequestHeaderInfo(ServiceRequestHeader serviceRequestHeader, HeaderType portDataheader) {
		
		ServiceRequestHeaderInfo serviceRequestHeaderInfo = new ServiceRequestMapper().mapToDomain(serviceRequestHeader);
		if (serviceRequestHeaderInfo != null) {
			//serviceRequestHeaderInfo.setApplicationName(context.getClientApplication());
			serviceRequestHeaderInfo.setApplicationName(portDataheader.getOriginatorName());
		}  
		
		return serviceRequestHeaderInfo;
	}
	
}