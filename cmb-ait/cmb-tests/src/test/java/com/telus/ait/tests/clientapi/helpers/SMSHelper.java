package com.telus.ait.tests.clientapi.helpers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.ait.tests.clientapi.constants.DefaultValues;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivateSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ActivationSubscriberData;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.EquipmentActivated;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.PortInEligibility;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.subscribermanagementservicerequestresponse_v4.ServicesValidation;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Address;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.AddressType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Language;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.NameFormat;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.UserServiceProfile;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.Equipment;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.AutoRenewType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PrepaidPropertyListType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

public class SMSHelper extends CommonHelper {
	
	public static ActivateSubscriber createDefaultActivateSubscriber(String ban, String subscriberId, String phoneNumber, String marketAreaCode, String productType, Date startServiceDate,
			EquipmentActivated equipmentRequest, String pricePlan, String services) throws ParseException { 
		AuditInfo auditInfo = SMSHelper.createAuditInfo(WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD);
		UserServiceProfile userServiceProfile = SMSHelper.createUserServiceProfile(WILDCARD, WILDCARD);
		ServiceRequestHeader serviceRequestHeader = SMSHelper.createServiceRequestHeader(WILDCARD, WILDCARD);
		ServiceAgreement serviceAgreement = SMSHelper.createServiceAgreement(SMSHelper.createCommitment(true, false, WILDCARD, WILDCARD, TransactionType.ADD.value(), 
				SMSHelper.createTimePeriod(WILDCARD)), SMSHelper.createPricePlan(pricePlan, "P", TransactionType.ADD.value()), 
				SMSHelper.createContractServices(services, "R", TransactionType.ADD.value()));
		ServicesValidation servicesValidation = SMSHelper.createServicesValidation(DefaultValues.EQUIPMENT_SERVICE_MATCH_IND, DefaultValues.PRICEPLAN_SERVICE_GROUP_IND, 
				DefaultValues.PROVINCE_SERVICE_MATCH_IND);
		ActivationSubscriberData subscriberData = SMSHelper.createSubscriberData(ban, subscriberId, phoneNumber, WILDCARD, marketAreaCode, productType, WILDCARD, WILDCARD, WILDCARD, WILDCARD, 
				WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD);
		return createActivateSubscriber(WILDCARD, WILDCARD, WILDCARD, WILDCARD, startServiceDate, DefaultValues.WAIVE_SEARCH_FEE_IND, DefaultValues.DEALER_HAS_DEPOSIT_IND, 
				DefaultValues.NOTIFICATION_SUPPRESSION_IND, null, subscriberData, servicesValidation, serviceAgreement, equipmentRequest, userServiceProfile, serviceRequestHeader, auditInfo, 
				null);
	}
	
	public static ActivateSubscriber createDefaultActivatePrepaidSubscriber(String ban, String subscriberId, String phoneNumber, String marketAreaCode, String productType, Date startServiceDate,
			EquipmentActivated equipmentRequest, String pricePlan, String services, String prepaidServices, String callingCircleNumbers) throws ParseException {
		//TODO: Add prepaid services
		ActivateSubscriber request = createDefaultActivateSubscriber(ban, subscriberId, phoneNumber, marketAreaCode, productType, startServiceDate, equipmentRequest, pricePlan, services);
		List<ContractService> contractServices = request.getServiceAgreement().getService();
		contractServices.addAll(getPrepaidServices(prepaidServices, callingCircleNumbers));
		request.getServiceAgreement().setService(contractServices);
		return request;
	}
	
	private static List<ContractService> getPrepaidServices(String prepaidServices, String callingCircleNumbers) {
		List<ContractService> prepaidServiceList = new ArrayList<ContractService>();
		String[] prepaidServiceNames = prepaidServices.split(",");
		for (String prepaidServiceName : prepaidServiceNames) {
			ContractService prepaidService = createContractService(prepaidServiceName, null, TransactionType.ADD.toString());
			prepaidService.setPrepaidPropertyList(getDefaultPrepaidPropertyList());
			prepaidService.setFeature(getPrepaidCallingCircleFeatures(prepaidServiceName, callingCircleNumbers));
			prepaidServiceList.add(prepaidService);
		}
		return prepaidServiceList;
	}
	
	private static PrepaidPropertyListType getDefaultPrepaidPropertyList() {
		PrepaidPropertyListType prepaidPropertyList = new PrepaidPropertyListType();
		AutoRenewType autoRenewType = new AutoRenewType();
		autoRenewType.setAutoRenewInd(DefaultValues.AUTO_RENEW_IND);
		autoRenewType.setRenewalFundSource(DefaultValues.RENEWAL_FUND_SOURCE);
		prepaidPropertyList.setAutoRenewPropertyList(autoRenewType);
		prepaidPropertyList.setPrepaidInd(DefaultValues.PREPAID_IND);
		prepaidPropertyList.setPurchaseFundSource(DefaultValues.PURCHASE_FUND_SOURCE);		
		return prepaidPropertyList;
	}
	
	private static List<ContractFeature> getPrepaidCallingCircleFeatures(String prepaidServiceName, String callingCircleNumbers) {
		List<ContractFeature> contractFeatures = new ArrayList<ContractFeature>();
		ContractFeature contractFeature = new ContractFeature();
		contractFeature.setCode(prepaidServiceName);
		PhoneNumberList phoneNumberList = new PhoneNumberList();
		phoneNumberList.setPhoneNumber(getCallingCircleNumberMap(callingCircleNumbers).get(prepaidServiceName));
		contractFeature.setCallingCirclePhoneNumberList(phoneNumberList);
		contractFeatures.add(contractFeature);
		return contractFeatures;
	}
	
	private static Map<String, List<String>> getCallingCircleNumberMap(String ccNumberString) {
		Map<String, List<String>> callingCircleNumbersMap = new HashMap<String, List<String>>();
		String[] callingCirclePairs = ccNumberString.split(",");
		for (String callingCirclePair : callingCirclePairs) {
			String[] callingCircleNums = callingCirclePair.split(":");
			String serviceName = callingCircleNums[0].trim();
			List<String> callingCircleNumbers = callingCircleNumbersMap.get(serviceName);
			if (callingCircleNumbers == null) {
				callingCircleNumbers = new ArrayList<String>();
			}
			callingCircleNumbers.add(callingCircleNums[1].trim());
			callingCircleNumbersMap.put(serviceName,  callingCircleNumbers);
		}
		return callingCircleNumbersMap;
	}
	
	public static ActivateSubscriber createActivateSubscriber(String activityReasonCode, String activationFeeChargeCode, String memoText, String subscriptionRoleCd, Date startServiceDate, 
			Boolean waiveSearchFeeInd, Boolean dealerHasDepositInd, Boolean notificationSuppressionInd, ActivationOption activationOption, ActivationSubscriberData subscriberData, 
			ServicesValidation servicesValidation, ServiceAgreement serviceAgreement, EquipmentActivated equipmentRequest, UserServiceProfile userProfile, 
			ServiceRequestHeader serviceRequestHeader, AuditInfo auditInfo, PortInEligibility portInEligibility) throws ParseException {
		ActivateSubscriber activateSub = new ActivateSubscriber();
		activateSub.setActivityReasonCode(getDefaultValue(activityReasonCode, DefaultValues.ACTIVITY_REASON_CODE));
		activateSub.setActivationFeeChargeCode(getDefaultValue(activationFeeChargeCode, DefaultValues.ACTIVATION_FEE_CHARGE_CODE));
		activateSub.setMemoText(getDefaultValue(memoText, DefaultValues.MEMO_TEXT));
		activateSub.setSubscriptionRoleCd(getDefaultValue(subscriptionRoleCd, DefaultValues.SUBSCRIPTION_ROLE_CODE));
		activateSub.setStartServiceDate(startServiceDate);
		activateSub.setWaiveSearchFeeInd(waiveSearchFeeInd);
		activateSub.setDealerHasDepositInd(dealerHasDepositInd);
		activateSub.setNotificationSuppressionInd(notificationSuppressionInd);
		activateSub.setActivationOption(activationOption);
		activateSub.setSubscriberData(subscriberData);
		activateSub.setServicesValidation(servicesValidation);
		activateSub.setServiceAgreement(serviceAgreement);
		activateSub.setEquipmentRequest(equipmentRequest);
		activateSub.setUserProfile(userProfile);
		activateSub.setServiceRequestHeader(serviceRequestHeader);
		activateSub.setAuditInfo(auditInfo);
		activateSub.setPortinEligibility(portInEligibility);		
		return activateSub;
	}	
	
	public static ActivationSubscriberData createDefaultSubscriberData(String ban, String subscriberId, String phoneNumber, String marketProvince, String productType) {
		return createSubscriberData(ban, subscriberId, phoneNumber, WILDCARD, marketProvince, productType, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, WILDCARD, 
				WILDCARD, WILDCARD, WILDCARD, WILDCARD);
	}
	
	public static ActivationSubscriberData createSubscriberData(String ban, String subscriberId, String phoneNumber, String brandId, String marketProvince, String productType,	String title, 
			String firstName, String lastName, String nameFormat, String addressType, String city, String provinceCode, String postalCode, String country, String streetName, 
			String emailAddress, String language) {
		ActivationSubscriberData subscriberData = new ActivationSubscriberData();
		subscriberData.setBillingAccountNumber(ban);
		subscriberData.setSubscriberId(subscriberId);
		subscriberData.setPhoneNumber(phoneNumber);
		subscriberData.setBrandId(Integer.valueOf(getDefaultValue(brandId, DefaultValues.BRAND_ID_TELUS)).intValue());
		subscriberData.setMarketProvince(ProvinceCode.fromValue(getDefaultValue(marketProvince, DefaultValues.PROVINCE_CODE)));
		subscriberData.setProductType(getDefaultValue(productType, DefaultValues.PRODUCT_TYPE));
		subscriberData.setEmailAddress(getDefaultValue(emailAddress, DefaultValues.EMAIL));
		subscriberData.setLanguage(Language.fromValue(getDefaultValue(language, DefaultValues.LANGUAGE)));
		subscriberData.setConsumerName(createConsumerName(title, firstName, lastName, nameFormat));
		subscriberData.setAddress(createAddress(addressType, city, provinceCode, postalCode, country, streetName));
		return subscriberData;
	}
	
	public static ServicesValidation createServicesValidation(boolean equipmentServiceMatchInd, boolean pricePlanServiceGroupingInd, boolean provinceServiceMatchInd) {
		ServicesValidation servicesValidation = new ServicesValidation();
		servicesValidation.setValidateEquipmentServiceMatchInd(equipmentServiceMatchInd);
		servicesValidation.setValidatePricePlanServiceGroupingInd(pricePlanServiceGroupingInd);
		servicesValidation.setValidateProvinceServiceMatchInd(provinceServiceMatchInd);
		return servicesValidation;
	}
	
	public static ServiceAgreement createServiceAgreement(Commitment commitment, PricePlan pricePlan, List<ContractService> services) {
		ServiceAgreement serviceAgreement = new ServiceAgreement();
		serviceAgreement.setCommitment(commitment);
		serviceAgreement.setPricePlan(pricePlan);
		serviceAgreement.setService(services);
		return serviceAgreement;
	}
	
	public static EquipmentActivated createEquipmentActivated(String primaryEquipmentType, String primarySerialNumber) {
		EquipmentActivated equipment = new EquipmentActivated();
		Equipment primaryEquipment = new Equipment();
		primaryEquipment.setEquipmentType(primaryEquipmentType);
		primaryEquipment.setSerialNumber(primarySerialNumber);
		equipment.setPrimaryEquipment(primaryEquipment);
		return equipment;
	}
	
	public static PricePlan createPricePlan(String code, String serviceType, String transactionType) {
		PricePlan pricePlan = new PricePlan();
		pricePlan.setCode(code);
		pricePlan.setServiceType(serviceType);
		pricePlan.setTransactionType(TransactionType.valueOf(transactionType));
		return pricePlan;
	}
	
	public static List<ContractService> createContractServices(String serviceCodes, String serviceType, String transactionType) {	
		List<ContractService> contractServices = new ArrayList<ContractService>();
		for (String serviceCode : serviceCodes.split(LIST_SEPARATOR)) {
			contractServices.add(createContractService(serviceCode, serviceType, transactionType));	
		}
		return contractServices;
	}

	private static ContractService createContractService(String code, String serviceType, String transactionType) {
		ContractService contractService = new ContractService();
		contractService.setCode(code.trim());
		contractService.setServiceType(serviceType);		
		contractService.setTransactionType(TransactionType.valueOf(transactionType));
		return contractService;
	}
	
	public static Commitment createCommitment(boolean activationInd, boolean renewalInd, String reasonCode, String contractTerm, String transactionType, TimePeriod timePeriod) {
		Commitment commitment = new Commitment();
		commitment.setReasonCode(getDefaultValue(reasonCode, DefaultValues.REASON_CODE));
		commitment.setContractTerm(getDefaultValue(contractTerm, DefaultValues.CONTRACT_TERM));
		commitment.setActivationInd(activationInd);
		commitment.setRenewalInd(renewalInd);
		commitment.setTransactionType(TransactionType.valueOf(transactionType));
		commitment.setTimePeriod(timePeriod);
		return commitment;
	}
	
	public static ServiceRequestHeader createServiceRequestHeader(String appId, String language) {
		ServiceRequestHeader requestHeader = new ServiceRequestHeader();
		requestHeader.setApplicationId(Long.valueOf(getDefaultValue(appId, DefaultValues.APP_ID)).longValue());
		requestHeader.setLanguageCode(com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Language.valueOf(getDefaultValue(language, DefaultValues.LANGUAGE)));
		return requestHeader;
	}
	
	public static AuditInfo createAuditInfo(String userId, String salesRepId, String channelOrgId, String outletId, String originatorAppId) {
		AuditInfo auditInfo = new AuditInfo();
    	auditInfo.setUserId(getDefaultValue(userId, DefaultValues.AUDIT_USER_ID));
    	auditInfo.setSalesRepresentativeId(getDefaultValue(salesRepId, DefaultValues.AUDIT_SALES_REP_ID));
    	auditInfo.setChannelOrganizationId(getDefaultValue(channelOrgId, DefaultValues.CHANNEL_ORG_ID));
    	auditInfo.setOutletId(getDefaultValue(outletId, DefaultValues.OUTLET_ID));
    	auditInfo.setOriginatorApplicationId(getDefaultValue(originatorAppId, DefaultValues.ORG_APP_ID));
		return auditInfo;
	}
	
	public static TimePeriod createTimePeriod(String contractTerm) {
		TimePeriod timePeriod = new TimePeriod();
		Calendar calendar = Calendar.getInstance();
		timePeriod.setEffectiveDate(calendar.getTime());
		calendar.add(Calendar.MONTH, Integer.valueOf(getDefaultValue(contractTerm, DefaultValues.CONTRACT_TERM)).intValue());
		timePeriod.setExpiryDate(calendar.getTime());
		return timePeriod;
	}
	
	public static UserServiceProfile createUserServiceProfile(String dealerCode, String salesRepCode) {
		UserServiceProfile userServiceProfile = new UserServiceProfile();
		userServiceProfile.setDealerCode(getDefaultValue(dealerCode, DefaultValues.DEALER_CODE));
		userServiceProfile.setSalesRepCode(getDefaultValue(salesRepCode, DefaultValues.SALES_REP_CODE));
		return userServiceProfile;
	}
		
	public static Address createAddress(String addressType, String city, String provinceCode, String postalCode, String country, String streetName) {
		Address address = new Address();
    	address.setAddressType(AddressType.valueOf(getDefaultValue(addressType, DefaultValues.ADDRESS_TYPE)));
    	address.setCity(getDefaultValue(city, DefaultValues.CITY));
    	address.setProvince(ProvinceCode.valueOf(getDefaultValue(provinceCode, DefaultValues.PROVINCE_CODE)));
    	address.setPostalCode(getDefaultValue(postalCode, DefaultValues.POSTAL_CODE));
    	address.setCountry(getDefaultValue(country, DefaultValues.COUNTRY));
    	address.setStreetName(getDefaultValue(streetName, DefaultValues.STREET_NAME));
		return address;
	}
	
	public static ConsumerName createConsumerName(String title, String firstName, String lastName, String nameFormat) {
    	ConsumerName consumerName = new ConsumerName();
    	consumerName.setTitle(getDefaultValue(title, DefaultValues.TITLE));
    	consumerName.setFirstName(getDefaultValue(firstName, DefaultValues.FIRST_NAME));
    	consumerName.setLastName(getDefaultValue(lastName, DefaultValues.LAST_NAME));
    	consumerName.setNameFormat(NameFormat.valueOf(getDefaultValue(nameFormat, DefaultValues.NAME_FORMAT)));
    	return consumerName;	    	
    }
	
}
