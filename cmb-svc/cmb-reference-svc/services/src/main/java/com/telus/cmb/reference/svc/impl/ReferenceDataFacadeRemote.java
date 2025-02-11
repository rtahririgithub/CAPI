/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.TaxationPolicy;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.utility.info.*;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ReferenceDataFacadeRemote extends EJBObject {

	boolean checkServiceAssociation(String serviceCode, String pricePlanCode) throws TelusException, RemoteException;

	ServicePolicyInfo[] checkServicePrivilege(String[] serviceCode, String businessRole, String privilege) throws TelusException, RemoteException;

	ServicePolicyInfo[] checkPricePlanPrivilege(String[] pricePlanCodes, String businessRole, String privilege) throws TelusException, RemoteException;
	
	ServicePolicyInfo[] checkServicePrivilege(ServiceInfo[] services, String businessRole, String privilege) throws TelusException, RemoteException;

	ServiceInfo[] retainByPrivilege(ServiceInfo[] services,  String businessRoleCode, String privilegeCode) throws TelusAPIException, RemoteException;
    
	ServiceInfo[] removeByPrivilege(ServiceInfo[] services,  String businessRoleCode, String privilegeCode) throws TelusAPIException, RemoteException;
	
	ServiceInfo[] filterByPrivilege(ServiceInfo[] services,  String businessRoleCode, String privilegeCode, boolean criteria) throws TelusException, RemoteException;

	AccountTypeInfo getAccountType(String accountTypeCode, int accountTypeBrandId) throws TelusException, RemoteException;

	AccountTypeInfo[] getAccountTypes() throws TelusException, RemoteException;

	ActivityTypeInfo getActivityType(String activityTypeCode) throws TelusException, RemoteException;

	ActivityTypeInfo[] getActivityTypes() throws TelusException, RemoteException;

	// Defect PROD00178088 Start
	ActivityTypeInfo[] getActivityTypes(String activityTypeCode) throws TelusException, RemoteException;
	// Defect PROD00178088 End

	AddressTypeInfo getAddressType(String addressTypeCode) throws TelusException, RemoteException;

	AddressTypeInfo[] getAddressTypes() throws TelusException, RemoteException;
	
	AdjustmentReasonInfo getAdjustmentReason(String reasonCode) throws TelusException, RemoteException;	

	AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException, RemoteException;

	FollowUpTypeInfo[] getAllFollowUpTypes() throws TelusException, RemoteException;

	LanguageInfo[] getAllLanguages() throws TelusException, RemoteException;

	MemoTypeInfo[] getAllMemoTypes() throws TelusException, RemoteException;

	PrepaidRateProfileInfo[] getAllPrepaidRates() throws TelusException, RemoteException;

	ProvinceInfo[] getAllProvinces() throws TelusException, RemoteException;

	TitleInfo[] getAllTitles() throws TelusException, RemoteException;

	AmountBarCodeInfo[] getAmountBarCodes() throws TelusException, RemoteException;

	ApplicationSummaryInfo getApplicationSummary(String applicationCode) throws TelusException, RemoteException;

	AudienceTypeInfo getAudienceType(String audienceTypeCode) throws TelusException, RemoteException;

	NumberGroupInfo[] getAvailableNumberGroups(String accountType, String accountSubType, String productType, String equipmentType, String marketAreaCode) throws TelusException, RemoteException;

	BillCycleInfo getBillCycle(String code) throws TelusException, RemoteException;

	BillCycleInfo[] getBillCycles() throws TelusException, RemoteException;

	BillHoldRedirectDestinationInfo getBillHoldRedirectDestination(String destinationCode) throws TelusException, RemoteException;

	BillHoldRedirectDestinationInfo[] getBillHoldRedirectDestinations() throws TelusException, RemoteException;

	BrandInfo getBrand(String code) throws TelusException, RemoteException;

	BrandInfo getBrandByBrandId(int brandId) throws TelusException, RemoteException;

	BrandInfo[] getBrands() throws TelusException, RemoteException;

	BusinessRoleInfo getBusinessRole(String code) throws TelusException, RemoteException;

	BusinessRoleInfo[] getBusinessRoles() throws TelusException, RemoteException;

	ClientConsentIndicatorInfo getClientConsentIndicator(String code) throws TelusException, RemoteException;

	ClientConsentIndicatorInfo[] getClientConsentIndicators() throws TelusException, RemoteException;

	ClientStateReasonInfo getClientStateReason(String code) throws TelusException, RemoteException;

	ClientStateReasonInfo[] getClientStateReasons() throws TelusException, RemoteException;	

	CollectionActivityInfo[] getCollectionActivities() throws TelusException, RemoteException;

	CollectionActivityInfo getCollectionActivity(String code) throws TelusException, RemoteException;

	CollectionActivityInfo getCollectionActivity(String pathCode, int stepNumber) throws TelusException, RemoteException;

	CollectionAgencyInfo[] getCollectionAgencies() throws TelusException, RemoteException;

	CollectionAgencyInfo getCollectionAgency(String code) throws TelusException, RemoteException;
	
	CollectionPathDetailsInfo[] getCollectionPathDetails(String pathCode) throws TelusException, RemoteException;
	
	String[] getCollectionPaths() throws TelusException, RemoteException;

	CollectionStateInfo getCollectionState(String code) throws TelusException, RemoteException;

	CollectionStateInfo[] getCollectionStates() throws TelusException, RemoteException;

	CollectionStepApprovalInfo getCollectionStepApproval(String code) throws TelusException, RemoteException;

	CollectionStepApprovalInfo[] getCollectionStepApprovals() throws TelusException, RemoteException;

	CommitmentReasonInfo getCommitmentReason(String commitmentReasonCode) throws TelusException, RemoteException;

	CommitmentReasonInfo[] getCommitmentReasons() throws TelusException, RemoteException;

	CorporateAccountRepInfo getCorporateAccountRep(String code) throws TelusException, RemoteException;
	
	CorporateAccountRepInfo[] getCorporateAccountReps() throws TelusException, RemoteException;

	CountryInfo[] getCountries() throws TelusException, RemoteException;
	
	CountryInfo[] getCountries(boolean includeForiegn) throws TelusException, RemoteException;
	
	CountryInfo getCountry(String code) throws TelusException, RemoteException;
	
	CoverageRegionInfo getCoverageRegion(String code) throws TelusException, RemoteException;
	
	CoverageRegionInfo[] getCoverageRegions() throws TelusException, RemoteException;
	
	CreditCardPaymentTypeInfo getCreditCardPaymentType(String code) throws TelusException, RemoteException;
	
	CreditCardPaymentTypeInfo[] getCreditCardPaymentTypes() throws TelusException, RemoteException;
	
	CreditCardTypeInfo getCreditCardType(String code) throws TelusException, RemoteException;
	
	CreditCardTypeInfo[] getCreditCardTypes() throws TelusException, RemoteException;
	
	CreditCheckDepositChangeReasonInfo getCreditCheckDepositChangeReason(String code) throws TelusException, RemoteException;
	
	CreditCheckDepositChangeReasonInfo[] getCreditCheckDepositChangeReasons() throws TelusException, RemoteException;
	
	CreditClassInfo[] getCreditClasses() throws TelusException, RemoteException;
	
	CreditMessageInfo getCreditMessage(String code) throws TelusException, RemoteException;
	
	CreditMessageInfo[] getCreditMessages() throws TelusException, RemoteException;
	
	SalesRepInfo getDealerSalesRep(String dealerCode, String salesRepCode) throws TelusException, RemoteException;
	
	DepartmentInfo getDepartment(String code) throws TelusException, RemoteException;
	
	DepartmentInfo[] getDepartments() throws TelusException, RemoteException;
	
	DiscountPlanInfo getDiscountPlan(String discountCode) throws TelusException, RemoteException;
	
	DiscountPlanInfo[] getDiscountPlans(boolean current) throws TelusException, RemoteException;
	
	EncodingFormatInfo getEncodingFormat(String code) throws TelusException, RemoteException;
	
	EncodingFormatInfo[] getEncodingFormats() throws TelusException, RemoteException;
	
	EquipmentPossessionInfo getEquipmentPossession(String code) throws TelusException, RemoteException;
	
	EquipmentPossessionInfo[] getEquipmentPossessions() throws TelusException, RemoteException;
	
	EquipmentProductTypeInfo getEquipmentProductType(String code) throws TelusException, RemoteException;
	
	EquipmentProductTypeInfo[] getEquipmentProductTypes() throws TelusException, RemoteException;
	
	EquipmentStatusInfo getEquipmentStatus(long StatusID, long StatusTypeID) throws TelusException, RemoteException;
	
	EquipmentStatusInfo [] getEquipmentStatuses() throws TelusException, RemoteException;
	
	EquipmentTypeInfo getEquipmentType(String equipTypeCode) throws TelusException, RemoteException;
	
	EquipmentTypeInfo[] getEquipmentTypes() throws TelusException, RemoteException;
	
	ServiceInfo getEquivalentService(String serviceCode, String pricePlanCode) throws TelusException, RemoteException;

	ExceptionReasonInfo getExceptionReason(String code) throws TelusException, RemoteException;
	
	ExceptionReasonInfo[] getExceptionReasons() throws TelusException, RemoteException;
	
	FeatureInfo getFeature(String featureCode) throws TelusException, RemoteException;
	
	FeatureInfo[] getFeatureCategories() throws TelusException, RemoteException;

	FeatureInfo getFeatureCategory(String code) throws TelusException, RemoteException;
	
	FeatureInfo[] getFeatures() throws TelusException, RemoteException;
	
	FeeWaiverReasonInfo getFeeWaiverReason(String reasonCode) throws TelusException, RemoteException;
	
	FeeWaiverReasonInfo[] getFeeWaiverReasons() throws TelusException, RemoteException;
	
	FeeWaiverTypeInfo getFeeWaiverType(String typeCode) throws TelusException, RemoteException;
	
	FeeWaiverTypeInfo[] getFeeWaiverTypes() throws TelusException, RemoteException;
	
	FleetClassInfo getFleetClass(String code) throws TelusException, RemoteException;
	
	FleetClassInfo[] getFleetClasses() throws TelusException, RemoteException;
	
	FollowUpCloseReasonInfo getFollowUpCloseReason(String reasonCode) throws TelusException, RemoteException;
	
	FollowUpCloseReasonInfo[] getFollowUpCloseReasons() throws TelusException, RemoteException;
	
	FollowUpTypeInfo getFollowUpType(String code) throws TelusException, RemoteException; 
	
	FollowUpTypeInfo[] getFollowUpTypes() throws TelusException, RemoteException;
	
	GenerationInfo getGeneration(String generationCode) throws TelusException, RemoteException;
	
	GenerationInfo[] getGenerations() throws TelusException, RemoteException;
	
	InvoiceCallSortOrderTypeInfo getInvoiceCallSortOrderType(String code) throws TelusException, RemoteException;
	
	InvoiceCallSortOrderTypeInfo[] getInvoiceCallSortOrderTypes() throws TelusException, RemoteException;
	
	InvoiceSuppressionLevelInfo getInvoiceSuppressionLevel(String code) throws TelusException, RemoteException;
	
	InvoiceSuppressionLevelInfo[] getInvoiceSuppressionLevels() throws TelusException, RemoteException;
	
	KnowbilityOperatorInfo getKnowbilityOperator(String code) throws TelusException, RemoteException;
	
	LanguageInfo getLanguage(String code) throws TelusException, RemoteException;
	
	LanguageInfo[] getLanguages() throws TelusException, RemoteException;
	
//	LetterCategoryInfo[] getLetterCategories() throws TelusException, RemoteException;
//	
//	LetterCategoryInfo getLetterCategory (String letterCategory) throws TelusException, RemoteException;
//	
//	LetterSubCategoryInfo[] getLetterSubCategories(String letterCategory) throws TelusException, RemoteException;
//	
//	LetterSubCategoryInfo getLetterSubCategory(String letterSubCategory) throws TelusException, RemoteException;
//	LetterSubCategoryInfo getLetterSubCategory(String letterCategory,String letterSubCategory) throws TelusException, RemoteException;
	
	LockReasonInfo getLockReason(String code) throws TelusException, RemoteException;
	
	LockReasonInfo[] getLockReasons() throws TelusException, RemoteException;
	
	Date getLogicalDate() throws TelusException, RemoteException;
	
	ServiceSetInfo[] getMandatoryServices(String pricePlanCode, String handSetType, String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, long brandId) throws TelusException, RemoteException;
	
	ChargeTypeInfo getManualChargeType(String code) throws TelusException, RemoteException;
	
	ChargeTypeInfo[] getManualChargeTypes() throws TelusException, RemoteException;
	
	MemoTypeInfo getMemoType(String memoTypeCode) throws TelusException, RemoteException;
	
	MemoTypeCategoryInfo[] getMemoTypeCategories() throws TelusException, RemoteException;
	
	MemoTypeCategoryInfo getMemoTypeCategory(String categoryCode) throws TelusException, RemoteException;
	
	MemoTypeInfo[] getMemoTypes() throws TelusException, RemoteException;
	
	MigrationTypeInfo getMigrationType(String migrationCode) throws TelusException, RemoteException;
	
	MigrationTypeInfo[] getMigrationTypes() throws TelusException, RemoteException;
	
	ServiceInfo[] getMinutePoolingContributorServices() throws TelusException, RemoteException;
	
	NetworkInfo getNetwork(String networkCode) throws TelusException, RemoteException;
	
	NetworkInfo[] getNetworks() throws TelusException, RemoteException;
	
	NetworkTypeInfo[] getNetworkTypes() throws TelusException, RemoteException;
	
	NotificationMessageTemplateInfo getNotificationMessageTemplate(int notificationTypeCode) throws TelusException, RemoteException;
	
	NotificationMessageTemplateInfo getNotificationMessageTemplate(String code) throws TelusException, RemoteException;
	
	NotificationMessageTemplateInfo[] getNotificationMessageTemplates() throws TelusException, RemoteException;
	
	NotificationTypeInfo getNotificationType(int notificationTypeCode) throws TelusException, RemoteException;
	
	NotificationTypeInfo getNotificationType(String notificationCode) throws TelusException, RemoteException;
	
	NotificationTypeInfo[] getNotificationTypes() throws TelusException, RemoteException;
	
	NumberGroupInfo getNumberGroupByPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException, RemoteException;
	
	NumberGroupInfo getNumberGroupByPortedInPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException, RemoteException;
	
	NumberRangeInfo getNumberRange(String npaNxx) throws TelusException, RemoteException;
	
	NumberRangeInfo[] getNumberRanges() throws TelusException, RemoteException;
	
	EquipmentTypeInfo getPagerEquipmentType(String equipmentCode) throws TelusException, RemoteException;
	
	EquipmentTypeInfo [] getPagerEquipmentTypes() throws TelusException, RemoteException;
	
	PagerFrequencyInfo [] getPagerFrequencies() throws TelusException, RemoteException;
	
	PagerFrequencyInfo getPagerFrequency(String frequencyCode) throws TelusException, RemoteException;
	
	PaymentMethodInfo getPaymentMethod(String paymentMethodCode) throws TelusException, RemoteException;
	
	PaymentMethodInfo[] getPaymentMethods() throws TelusException, RemoteException;
	
	PaymentMethodTypeInfo getPaymentMethodType(String paymentMethodTypeCode) throws TelusException, RemoteException;
	
	PaymentMethodTypeInfo[] getPaymentMethodTypes() throws TelusException, RemoteException;
	
	PaymentSourceTypeInfo getPaymentSourceType(String paymentSourceTypeCode) throws TelusException, RemoteException;
	
	PaymentSourceTypeInfo[] getPaymentSourceTypes() throws TelusException, RemoteException;
	
	PaymentTransferReasonInfo getPaymentTransferReason(String transferReasonCode) throws TelusException, RemoteException;
	
	PaymentTransferReasonInfo[] getPaymentTransferReasons() throws TelusException, RemoteException;
	
	PhoneTypeInfo getPhoneType(String phoneTypeCode) throws TelusException, RemoteException;
	
	PhoneTypeInfo[] getPhoneTypes() throws TelusException, RemoteException;
	
	PoolingGroupInfo getPoolingGroup(String poolingGroupCode) throws TelusException, RemoteException;
	
	PoolingGroupInfo[] getPoolingGroups() throws TelusException, RemoteException;
	
    PrepaidAdjustmentReasonInfo[] getPrepaidAdjustmentReason() throws TelusException, RemoteException; 
    
    PrepaidAdjustmentReasonInfo getPrepaidAdjustmentReason(String adjustmentReasonCode) throws TelusException, RemoteException;
    
    PrepaidEventTypeInfo getPrepaidEventType(String eventTypeCode) throws TelusException, RemoteException;
    
    PrepaidEventTypeInfo[] getPrepaidEventTypes() throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo getPrepaidFeatureAddWaiveReason(String featureAddWaiveReasonCode) throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo[] getPrepaidFeatureAddWaiveReasons() throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo getPrepaidManualAdjustmentReason(String adjustmentReasonCode) throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo[] getPrepaidManualAdjustmentReasons() throws TelusException, RemoteException;
    
    PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations() throws TelusException, RemoteException;
    
    PrepaidRechargeDenominationInfo [] getPrepaidRechargeDenominations(String rechargeType) throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo getPrepaidTopUpWaiveReason(String topUpWaiveCode) throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo[] getPrepaidTopUpWaiveReasons()  throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo getPrepaidDeviceDirectFulfillmentReason(String topUpWaiveCode) throws TelusException, RemoteException;
    
    PrepaidAdjustmentReasonInfo[] getPrepaidDeviceDirectFulfillmentReasons()  throws TelusException, RemoteException;
    
    PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException, RemoteException;
    
    PricePlanInfo getPricePlan(String productType, String pricePlanCode, String equipmentType, String provinceCode, String accountType,	String accountSubType, int brandId) throws TelusException, RemoteException;
    
    PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException, RemoteException;

    PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType,	int brandId) throws TelusException, RemoteException;
    
    PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType,	int brandId, int term) throws TelusException, RemoteException;
    
    PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, 
    		long[] productPromoTypes, boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, 
    		String activityCode, String activityReasonCode, String networkType, String seatTypeCode) throws TelusException, RemoteException;
    
    PricePlanInfo[] getPricePlans(String[] pricePlanCode) throws TelusException, RemoteException;
    
    PricePlanTermInfo getPricePlanTerm(String pricePlanCode) throws TelusException, RemoteException;
    
    ProductTypeInfo getProductType(String productTypeCode) throws TelusException, RemoteException;
    
    ProductTypeInfo [] getProductTypes() throws TelusException, RemoteException;
    
    PromoTermInfo getPromoTerm(String promoCode) throws TelusException, RemoteException;
    
    ProvinceInfo getProvince(String provinceCode) throws TelusException, RemoteException;
    
    ProvinceInfo getProvince(String countryCode, String provinceCode) throws TelusException, RemoteException;
    
    ProvinceInfo[] getProvinces() throws TelusException, RemoteException;
    
    ProvinceInfo[] getProvinces(String countryCode) throws TelusException, RemoteException;
    
    ProvisioningPlatformTypeInfo getProvisioningPlatformType(String provisioningPlatformId) throws TelusException, RemoteException;
    
    ProvisioningPlatformTypeInfo[] getProvisioningPlatformTypes() throws TelusException, RemoteException;
    
    ProvisioningTransactionStatusInfo getProvisioningTransactionStatus(String txStatusCode) throws TelusException, RemoteException;
    
    ProvisioningTransactionStatusInfo[] getProvisioningTransactionStatuses() throws TelusException, RemoteException;
    
    ProvisioningTransactionTypeInfo getProvisioningTransactionType( String txTypeCode ) throws TelusException, RemoteException;
    
    ProvisioningTransactionTypeInfo[] getProvisioningTransactionTypes() throws TelusException, RemoteException;
    
    ServiceInfo getRegularService(String regularServiceCode) throws TelusException, RemoteException;
    
    ServiceInfo[] getRegularServices() throws TelusException, RemoteException;
    
    ServiceInfo[] getRegularServices(String[] serviceCode) throws TelusException, RemoteException;
    
    HandsetRoamingCapabilityInfo[] getRoamingCapability() throws TelusException, RemoteException;
    
    RouteInfo getRoute(String switch_id, String route_id) throws TelusException, RemoteException;
    
    RouteInfo[] getRoutes() throws TelusException, RemoteException;
    
    RuralDeliveryTypeInfo getRuralDeliveryType(String deliveryTypeCode) throws TelusException, RemoteException;
    
    RuralDeliveryTypeInfo[] getRuralDeliveryTypes() throws TelusException, RemoteException;
    
    RuralTypeInfo getRuralType(String ruralTypeCode) throws TelusException, RemoteException;
    
    RuralTypeInfo[] getRuralTypes() throws TelusException, RemoteException;
    
    SegmentationInfo getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusException, RemoteException;
    
    SegmentationInfo getSegmentation(String segmentationCode) throws TelusException, RemoteException;
    
    SegmentationInfo[] getSegmentations() throws TelusException, RemoteException;
    
    ServiceExclusionGroupsInfo[] getServiceExclusionGroups() throws TelusException, RemoteException;
    
    ServiceExclusionGroupsInfo getServiceExclusionGroups(String serviceExclusionGroupCode) throws TelusException, RemoteException;
    
    ServicePeriodTypeInfo getServicePeriodType(String servicePeriodTypeCode) throws TelusException, RemoteException;
    
    ServicePeriodTypeInfo[] getServicePeriodTypes() throws TelusException, RemoteException;
    
    ServicePolicyInfo getServicePolicy(String servicePolicyCode) throws TelusException, RemoteException;
    
    ServiceInfo[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusException, RemoteException;
    
    ServiceUsageInfo getServiceUsage(String serviceCode) throws TelusException, RemoteException;
    
    SIDInfo getSID(String sIDCode) throws TelusException, RemoteException;
    
    SIDInfo[] getSIDs() throws TelusException, RemoteException;
    
    SpecialNumberInfo getSpecialNumber(String numberCode) throws TelusException, RemoteException;
    
    SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber) throws TelusException, RemoteException;
    
    SpecialNumberRangeInfo[] getSpecialNumberRanges() throws TelusException, RemoteException;
    
    SpecialNumberInfo[] getSpecialNumbers() throws TelusException, RemoteException;
    
    StateInfo getState(String stateCode) throws TelusException, RemoteException;
    
    StateInfo[] getStates() throws TelusException, RemoteException;
    
    StreetDirectionInfo getStreetDirection(String directionCode) throws TelusException, RemoteException;
    
    StreetDirectionInfo[] getStreetDirections() throws TelusException, RemoteException;
    
    SubscriptionRoleTypeInfo getSubscriptionRoleType(String roleTypeCode) throws TelusException, RemoteException;
    
    SubscriptionRoleTypeInfo[] getSubscriptionRoleTypes() throws TelusException, RemoteException;
    
    TalkGroupPriorityInfo[] getTalkGroupPriorities() throws TelusException, RemoteException;
    
    TaxationPolicyInfo[] getTaxationPolicies() throws TelusException, RemoteException;
    
    TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusException, RemoteException;
    
    TermUnitInfo getTermUnit(String termUnitCode) throws TelusException, RemoteException;
    
    TermUnitInfo[] getTermUnits() throws TelusException, RemoteException;
    
    TitleInfo getTitle(String titleCode) throws TelusException, RemoteException;
    
    TitleInfo[] getTitles() throws TelusException, RemoteException;
    
    UnitTypeInfo getUnitType(String typeCode) throws TelusException, RemoteException;
    
    UnitTypeInfo[] getUnitTypes() throws TelusException, RemoteException;
    
    UsageRateMethodInfo getUsageRateMethod(String rateMethodCode) throws TelusException, RemoteException;  
    
    UsageRateMethodInfo[] getUsageRateMethods() throws TelusException, RemoteException;
    
    UsageRecordTypeInfo getUsageRecordType(String recordTypeCode) throws TelusException, RemoteException;      
    
    UsageRecordTypeInfo[] getUsageRecordTypes() throws TelusException, RemoteException;
    
    UsageUnitInfo getUsageUnit(String unitCode) throws TelusException, RemoteException;
    
    UsageUnitInfo[] getUsageUnits() throws TelusException, RemoteException;
    
    VendorServiceInfo getVendorService(String vendorServiceCode) throws TelusException, RemoteException;
    
    VendorServiceInfo[] getVendorServices() throws TelusException, RemoteException;
    
    WorkFunctionInfo[] getWorkFunctions() throws TelusException, RemoteException;
    
    WorkFunctionInfo[] getWorkFunctions(String departmentCode) throws TelusException, RemoteException;

    WorkPositionInfo getWorkPosition(String workPositionCode) throws TelusException, RemoteException;

    WorkPositionInfo[] getWorkPositions(String functionCode) throws TelusException, RemoteException;
    
    PrepaidCategoryInfo[] getWPSCategories() throws TelusException, RemoteException;
    
    PrepaidCategoryInfo getWPSCategory(String categoryCode) throws TelusException, RemoteException;
    
    ServiceInfo getWPSService(String wpsServiceCode) throws TelusException, RemoteException;
    
    ServiceInfo[] getWPSServices() throws TelusException, RemoteException;
    
    ServiceInfo[] getZeroMinutePoolingContributorServices() throws TelusException, RemoteException;
    
	/**
	 * Retrieves the one-time charge details for paper bill on the account.
	 *
	 * Any of the parameters passed can be null or empty, however there must be at
	 * least one parameter populated, otherwise will throw an IllegalArgumentException.
	 * 
	 * @param int brand Id, can be zero for wildcard
	 * @param String province code, can be null or empty for wildcard
	 * @param char account type, can be null (that is '\u0000') for wildcard
	 * @param char account sub-type, can be null (that is '\u0000') for wildcard
	 * @param String account GL segment, can be null or empty for wildcard
	 * @param String invoiceSuppressionLevel, can be null or empty for wildcard
	 * 
	 * @return ChargeTypeInfo a one-time charge type
	 * @throws TelusException
	 * @throws RemoteException
	 */
	ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode,
			char accountType, char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException, RemoteException;
	
	/**
	 * Retrieve SOC group and service codes mapping information.
	 * 
	 * @param  String serviceGroupCode 
	 * @return String[] list of soc as values. 
	 * @throws TelusException,RemoteException
	 **/
	String[] getServiceCodesByGroup(String serviceGroupCode) throws TelusException, RemoteException;
		
	/**
	 * Retrieves Service term information as defined in KB PROMOTION_TERMS table
	 *
	 * @param String serviceCode
	 * 
	 * @return ServiceTermDto
	 * @throws TelusException,RemoteException
	 **/
	ServiceTermDto getServiceTerm (String serviceCode) throws TelusException ,RemoteException;
	
	/**
	 * If saleRepCode passed in is empty string or all zeros, then returned SalesRepInfo is based on dealerCode.  dealerCode is
	 * determined from KB.  DealerCode and Name will be set as the value returned from retrieving the dealer.  Code will be set as 0000. 
	 * 
	 * @param dealerCode
	 * @param salesRepCode
	 * @param expired
	 * @return
	 * @throws TelusException, RemoteException
	 */
	SalesRepInfo getDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException, RemoteException;
		
	BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles,  String province)  throws TelusException, RemoteException;
	
	String getSubscriptionTypeByKBServiceType (String kbServiceType) throws TelusException, RemoteException;
	String getServiceInstanceStatusByKBSubscriberStatus (String kbSubscribreStatus) throws TelusException, RemoteException;
	String getBillingAccountStatusByKBAccountStatus (String kbAccountStatus) throws TelusException, RemoteException;
	String getPaymentMethodTypeByKBPaymentMethodType (String kbPaymentMethodType) throws TelusException, RemoteException;
	String getCreditCardTypeByKBCreditCardType (String kbCreditCardType) throws TelusException, RemoteException;
	String getBillCycleCodeByKBBillCycleCode (String kbBillCycleCode) throws TelusException, RemoteException;
	String getNameSuffixByKBNameSuffix (String kbNameSuffix) throws TelusException, RemoteException;
	String getSaluationCodeByKBSaluationCode (String kbSaluationCode) throws TelusException, RemoteException;
	String getEquipmentGroupTypeBySEMSEquipmentGroupType (String semsEquipmentGroupType) throws TelusException, RemoteException;
	String getProvinceCodeByKBProvinceCode (String kbProvinceCode) throws TelusException, RemoteException;
	String getCountryCodeByKBCountryCode(String kbCountryCode) throws TelusException, RemoteException;
	
	public DataSharingGroupInfo[] getDataSharingGroups() throws TelusException, RemoteException;
	public DataSharingGroupInfo getDataSharingGroup(String groupCode ) throws TelusException, RemoteException;
	
	ServiceFeatureClassificationInfo getServiceFeatureClassification (String classifcationCode)  throws TelusException, RemoteException;
	ServicePeriodInfo[] getServicePeriodInfo( String serviceCode ) throws TelusException, RemoteException;
	Map getServicePeriodInfo( String serviceCode, String serviceType ) throws TelusException, RemoteException;
	ServiceAirTimeAllocationInfo[] getVoiceAllocation (String[] serviceCodes, Date effectiveDate, String sessionId )  throws TelusException, RemoteException;
	ServiceAirTimeAllocationInfo[] getCalculatedEffectedVoiceAllocation (String[] serviceCodes, Date effectiveDate, String sessionId )  throws TelusException, RemoteException;
	String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;

	Map getEquivalentServiceByServiceCodeList(String[] originalServiceCodeList, String[] destinationServiceCodeList, String networkType) throws TelusException, RemoteException;
	Map getServiceAndRelationList(ServiceRelationInfo[] serviceRelations) throws TelusException, RemoteException;
	Map getServiceListByGroupList(String[] serviceGroupCodeList) throws TelusException, RemoteException;
	Map getAlternateRecurringCharge(String[] serviceCodeList, String provinceCode, String npaNxx, String corporateId) throws TelusException, RemoteException;
	
	SeatTypeInfo[] getSeatTypes() throws TelusException, RemoteException;
	
	boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException, RemoteException;
		
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException, RemoteException;
	
	ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException, RemoteException;
	
	Map getDataSharingPricingGroups() throws TelusException, RemoteException;
	
	List getServiceEditions() throws TelusException, RemoteException;
	
	ServiceInfo[] getWPSServices(String[] serviceCodeArray) throws TelusException, RemoteException;
	
	ServiceFamilyTypeInfo[] getPPSServices() throws TelusException,RemoteException;
	

	PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId) throws TelusException,RemoteException;
	
	RoamingServiceNotificationInfo[]  retrieveRoamingServiceNotificationInfo( String[] serviceCodes) throws TelusException,RemoteException;

	ReferenceInfo retrieveMarketingDescriptionBySoc(String soc) throws TelusException, RemoteException;
	
	public String getNotificationTemplateSchemaVerison(String transactionType,int brandId, String accountType, String banSegment,
			String productType, String deliveryChannel, String language) throws TelusException ,RemoteException;
	public ServiceInfo[] getIncludedPromotions(String pricePlanCode, String equipmentType, String networkType, String provinceCode, int term) throws TelusException, RemoteException;
	
}