/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import java.util.List;
import java.util.Map;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.TaxationPolicy;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.*;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ReferenceDataFacade extends BillingInquiryReferenceFacade, CustomerInformationReferenceFacade, CustomerOrderReferenceFacade, EnterpriseReferenceFacade, ResourceOrderReferenceFacade,
		ServiceOrderReferenceFacade, ReferenceDataPDSFacade {

	ActivityTypeInfo getActivityType(String activityTypeCode) throws TelusException;

	ActivityTypeInfo[] getActivityTypes() throws TelusException;

	// Defect PROD00178088 Start
	/**
	 * Retrieves array of activity types by specific activity type code.
	 *
	 * @param String activity type code
	 * 
	 * @return ActivityTypeInfo[]
	 * @throws TelusException
	 */
	ActivityTypeInfo[] getActivityTypes(String activityTypeCode) throws TelusException;

	/**
	 * Retrieves activity type by specific activity type code. There are more
	 * than one activity types for some activity type codes (for example "ADJR")
	 * In this case this method will retrieve only the first one from the list.
	 * To retrieve array of activity types by specific activity type code,
	 * please use ActivityTypeInfo[] getActivityTypes(String activityTypeCode)
	 * method.
	 *
	 * @param String activity type code
	 * 
	 * @return ActivityTypeInfo
	 * @throws TelusException
	 */
	AddressTypeInfo getAddressType(String addressTypeCode) throws TelusException;
	// Defect PROD00178088 End

	AddressTypeInfo[] getAddressTypes() throws TelusException;

	AdjustmentReasonInfo getAdjustmentReason(String reasonCode) throws TelusException;

	AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException;

	FollowUpTypeInfo[] getAllFollowUpTypes() throws TelusException;

	LanguageInfo[] getAllLanguages() throws TelusException;

	MemoTypeInfo[] getAllMemoTypes() throws TelusException;

	PrepaidRateProfileInfo[] getAllPrepaidRates() throws TelusException;

	ProvinceInfo[] getAllProvinces() throws TelusException;

	TitleInfo[] getAllTitles() throws TelusException;

	AmountBarCodeInfo[] getAmountBarCodes() throws TelusException;

	ApplicationSummaryInfo getApplicationSummary(String applicationCode) throws TelusException;

	AudienceTypeInfo getAudienceType(String audienceTypeCode) throws TelusException;

	BillCycleInfo getBillCycle(String code) throws TelusException;

	BillCycleInfo[] getBillCycles() throws TelusException;

	BillHoldRedirectDestinationInfo getBillHoldRedirectDestination(String destinationCode) throws TelusException;

	BillHoldRedirectDestinationInfo[] getBillHoldRedirectDestinations() throws TelusException;

	BrandSwapRuleInfo[] getBrandSwapRules() throws TelusException;

	BusinessRoleInfo getBusinessRole(String code) throws TelusException;

	BusinessRoleInfo[] getBusinessRoles() throws TelusException;

	ClientConsentIndicatorInfo getClientConsentIndicator(String code) throws TelusException;

	ClientConsentIndicatorInfo[] getClientConsentIndicators() throws TelusException;

	ClientStateReasonInfo getClientStateReason(String code) throws TelusException;

	ClientStateReasonInfo[] getClientStateReasons() throws TelusException;

	CollectionActivityInfo[] getCollectionActivities() throws TelusException;

	CollectionActivityInfo getCollectionActivity(String code) throws TelusException;

	CollectionActivityInfo getCollectionActivity(String pathCode, int stepNumber) throws TelusException;

	CollectionAgencyInfo[] getCollectionAgencies() throws TelusException;

	CollectionAgencyInfo getCollectionAgency(String code) throws TelusException;

	CollectionPathDetailsInfo[] getCollectionPathDetails(String pathCode) throws TelusException;

	String[] getCollectionPaths() throws TelusException;

	CollectionStateInfo getCollectionState(String code) throws TelusException;

	CollectionStateInfo[] getCollectionStates() throws TelusException;

	CollectionStepApprovalInfo getCollectionStepApproval(String code) throws TelusException;

	CollectionStepApprovalInfo[] getCollectionStepApprovals() throws TelusException;

	CorporateAccountRepInfo getCorporateAccountRep(String code) throws TelusException;

	CorporateAccountRepInfo[] getCorporateAccountReps() throws TelusException;

	CountryInfo[] getCountries(boolean includeForiegn) throws TelusException;

	CountryInfo getCountry(String code) throws TelusException;

	CoverageRegionInfo getCoverageRegion(String code) throws TelusException;

	CoverageRegionInfo[] getCoverageRegions() throws TelusException;

	CreditCardPaymentTypeInfo getCreditCardPaymentType(String code) throws TelusException;

	CreditCardPaymentTypeInfo[] getCreditCardPaymentTypes() throws TelusException;

	CreditCardTypeInfo getCreditCardType(String code) throws TelusException;

	CreditCardTypeInfo[] getCreditCardTypes() throws TelusException;

	CreditCheckDepositChangeReasonInfo getCreditCheckDepositChangeReason(String code) throws TelusException;

	CreditCheckDepositChangeReasonInfo[] getCreditCheckDepositChangeReasons() throws TelusException;

	CreditClassInfo[] getCreditClasses() throws TelusException;

	CreditMessageInfo getCreditMessage(String code) throws TelusException;

	CreditMessageInfo[] getCreditMessages() throws TelusException;

	DepartmentInfo getDepartment(String code) throws TelusException;

	DepartmentInfo[] getDepartments() throws TelusException;

	DiscountPlanInfo getDiscountPlan(String discountCode) throws TelusException;

	DiscountPlanInfo[] getDiscountPlans(boolean current) throws TelusException;

	EncodingFormatInfo getEncodingFormat(String code) throws TelusException;

	EncodingFormatInfo[] getEncodingFormats() throws TelusException;

	EquipmentPossessionInfo getEquipmentPossession(String code) throws TelusException;

	EquipmentPossessionInfo[] getEquipmentPossessions() throws TelusException;

	EquipmentProductTypeInfo getEquipmentProductType(String code) throws TelusException;

	EquipmentProductTypeInfo[] getEquipmentProductTypes() throws TelusException;

	EquipmentStatusInfo getEquipmentStatus(long StatusID, long StatusTypeID) throws TelusException;

	EquipmentStatusInfo[] getEquipmentStatuses() throws TelusException;

	SwapRuleInfo[] getEquipmentSwapRules() throws TelusException;

	EquipmentTypeInfo getEquipmentType(String equipTypeCode) throws TelusException;

	EquipmentTypeInfo[] getEquipmentTypes() throws TelusException;

	ServiceInfo getEquivalentService(String serviceCode, String pricePlanCode) throws TelusException;

	ExceptionReasonInfo getExceptionReason(String code) throws TelusException;

	ExceptionReasonInfo[] getExceptionReasons() throws TelusException;

	FeatureInfo[] getFeatureCategories() throws TelusException;

	FeatureInfo getFeatureCategory(String code) throws TelusException;

	FeeWaiverReasonInfo getFeeWaiverReason(String reasonCode) throws TelusException;

	FeeWaiverReasonInfo[] getFeeWaiverReasons() throws TelusException;

	FeeWaiverTypeInfo getFeeWaiverType(String typeCode) throws TelusException;

	FeeWaiverTypeInfo[] getFeeWaiverTypes() throws TelusException;

	FleetClassInfo getFleetClass(String code) throws TelusException;

	FleetClassInfo[] getFleetClasses() throws TelusException;

	FleetInfo getFleetById(final int urbanID, final int fleetId) throws TelusException;

	FollowUpCloseReasonInfo getFollowUpCloseReason(String reasonCode) throws TelusException;

	FollowUpCloseReasonInfo[] getFollowUpCloseReasons() throws TelusException;

	FollowUpTypeInfo getFollowUpType(String code) throws TelusException;

	FollowUpTypeInfo[] getFollowUpTypes() throws TelusException;

	GenerationInfo getGeneration(String generationCode) throws TelusException;

	GenerationInfo[] getGenerations() throws TelusException;

	InvoiceCallSortOrderTypeInfo getInvoiceCallSortOrderType(String code) throws TelusException;

	InvoiceCallSortOrderTypeInfo[] getInvoiceCallSortOrderTypes() throws TelusException;

	InvoiceSuppressionLevelInfo getInvoiceSuppressionLevel(String code) throws TelusException;

	InvoiceSuppressionLevelInfo[] getInvoiceSuppressionLevels() throws TelusException;

	KnowbilityOperatorInfo getKnowbilityOperator(String code) throws TelusException;

	LanguageInfo getLanguage(String code) throws TelusException;

//	LetterCategoryInfo[] getLetterCategories() throws TelusException;
//
//	LetterCategoryInfo getLetterCategory(String letterCategory) throws TelusException;
//
//	LetterSubCategoryInfo[] getLetterSubCategories(String letterCategory) throws TelusException;
//
//	LetterSubCategoryInfo getLetterSubCategory(String letterSubCategory) throws TelusException;
//
//	LetterSubCategoryInfo getLetterSubCategory(String letterCategory, String letterSubCategory) throws TelusException;

	LockReasonInfo getLockReason(String code) throws TelusException;

	LockReasonInfo[] getLockReasons() throws TelusException;

	ServiceSetInfo[] getMandatoryServices(String pricePlanCode, String handSetType, String productType, String equipmentType, String provinceCode, String accountType, String accountSubType,
			long brandId) throws TelusException;

	ChargeTypeInfo getManualChargeType(String code) throws TelusException;

	ChargeTypeInfo[] getManualChargeTypes() throws TelusException;

	MemoTypeCategoryInfo[] getMemoTypeCategories() throws TelusException;

	MemoTypeCategoryInfo getMemoTypeCategory(String categoryCode) throws TelusException;

	MigrationTypeInfo getMigrationType(String migrationCode) throws TelusException;

	MigrationTypeInfo[] getMigrationTypes() throws TelusException;

	NetworkInfo getNetwork(String networkCode) throws TelusException;

	NetworkInfo[] getNetworks() throws TelusException;

	NetworkTypeInfo[] getNetworkTypes() throws TelusException;

	NotificationMessageTemplateInfo getNotificationMessageTemplate(int notificationTypeCode) throws TelusException;

	NotificationMessageTemplateInfo getNotificationMessageTemplate(String code) throws TelusException;

	NotificationMessageTemplateInfo[] getNotificationMessageTemplates() throws TelusException;

	NotificationTypeInfo getNotificationType(int notificationTypeCode) throws TelusException;

	NotificationTypeInfo getNotificationType(String notificationCode) throws TelusException;

	NotificationTypeInfo[] getNotificationTypes() throws TelusException;

	NumberRangeInfo getNumberRange(String npaNxx) throws TelusException;

	NumberRangeInfo[] getNumberRanges() throws TelusException;

	EquipmentTypeInfo getPagerEquipmentType(String equipmentCode) throws TelusException;

	EquipmentTypeInfo[] getPagerEquipmentTypes() throws TelusException;

	PagerFrequencyInfo[] getPagerFrequencies() throws TelusException;

	PagerFrequencyInfo getPagerFrequency(String frequencyCode) throws TelusException;

	PaymentMethodInfo getPaymentMethod(String paymentMethodCode) throws TelusException;

	PaymentMethodTypeInfo getPaymentMethodType(String paymentMethodTypeCode) throws TelusException;

	PaymentMethodTypeInfo[] getPaymentMethodTypes() throws TelusException;

	PaymentSourceTypeInfo getPaymentSourceType(String paymentSourceTypeCode) throws TelusException;

	PaymentTransferReasonInfo getPaymentTransferReason(String transferReasonCode) throws TelusException;

	PaymentTransferReasonInfo[] getPaymentTransferReasons() throws TelusException;

	PhoneTypeInfo getPhoneType(String phoneTypeCode) throws TelusException;

	PhoneTypeInfo[] getPhoneTypes() throws TelusException;

	PrepaidAdjustmentReasonInfo[] getPrepaidAdjustmentReason() throws TelusException;

	PrepaidAdjustmentReasonInfo getPrepaidAdjustmentReason(String adjustmentReasonCode) throws TelusException;

	PrepaidEventTypeInfo getPrepaidEventType(String eventTypeCode) throws TelusException;

	PrepaidEventTypeInfo[] getPrepaidEventTypes() throws TelusException;

	PrepaidAdjustmentReasonInfo getPrepaidFeatureAddWaiveReason(String featureAddWaiveReasonCode) throws TelusException;

	PrepaidAdjustmentReasonInfo[] getPrepaidFeatureAddWaiveReasons() throws TelusException;

	PrepaidAdjustmentReasonInfo getPrepaidManualAdjustmentReason(String adjustmentReasonCode) throws TelusException;

	PrepaidAdjustmentReasonInfo[] getPrepaidManualAdjustmentReasons() throws TelusException;

	PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations(String rechargeType) throws TelusException;

	PrepaidAdjustmentReasonInfo getPrepaidTopUpWaiveReason(String topUpWaiveCode) throws TelusException;

	PrepaidAdjustmentReasonInfo[] getPrepaidTopUpWaiveReasons() throws TelusException;

	PrepaidAdjustmentReasonInfo getPrepaidDeviceDirectFulfillmentReason(String deviceDirectFulfillmentReasonCode) throws TelusException;

	PrepaidAdjustmentReasonInfo[] getPrepaidDeviceDirectFulfillmentReasons() throws TelusException;

	PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException;

	PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId) throws TelusException;

	PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, int term) throws TelusException;

	PricePlanTermInfo getPricePlanTerm(String pricePlanCode) throws TelusException;

	ProductTypeInfo getProductType(String productTypeCode) throws TelusException;

	ProductTypeInfo[] getProductTypes() throws TelusException;

	PromoTermInfo getPromoTerm(String promoCode) throws TelusException;

	ProvinceInfo getProvince(String provinceCode) throws TelusException;

	ProvinceInfo getProvince(String countryCode, String provinceCode) throws TelusException;

	ProvinceInfo[] getProvinces(String countryCode) throws TelusException;

	ProvisioningPlatformTypeInfo getProvisioningPlatformType(String provisioningPlatformId) throws TelusException;

	ProvisioningPlatformTypeInfo[] getProvisioningPlatformTypes() throws TelusException;

	ProvisioningTransactionStatusInfo getProvisioningTransactionStatus(String txStatusCode) throws TelusException;

	ProvisioningTransactionStatusInfo[] getProvisioningTransactionStatuses() throws TelusException;

	ProvisioningTransactionTypeInfo getProvisioningTransactionType(String txTypeCode) throws TelusException;

	ProvisioningTransactionTypeInfo[] getProvisioningTransactionTypes() throws TelusException;

	ServiceInfo[] getRegularServices() throws TelusException;

	ServiceInfo retrieveRegularService(String serviceCode) throws TelusException;

	HandsetRoamingCapabilityInfo[] getRoamingCapability() throws TelusException;

	RouteInfo getRoute(String switch_id, String route_id) throws TelusException;

	RouteInfo[] getRoutes() throws TelusException;

	RuralDeliveryTypeInfo getRuralDeliveryType(String deliveryTypeCode) throws TelusException;

	RuralDeliveryTypeInfo[] getRuralDeliveryTypes() throws TelusException;

	RuralTypeInfo getRuralType(String ruralTypeCode) throws TelusException;

	RuralTypeInfo[] getRuralTypes() throws TelusException;

	SegmentationInfo getSegmentation(String segmentationCode) throws TelusException;

	ServiceExclusionGroupsInfo getServiceExclusionGroups(String serviceExclusionGroupCode) throws TelusException;

	ServicePeriodTypeInfo getServicePeriodType(String servicePeriodTypeCode) throws TelusException;

	ServicePeriodTypeInfo[] getServicePeriodTypes() throws TelusException;

	ServicePolicyInfo getServicePolicy(String servicePolicyCode) throws TelusException;

	ServiceUsageInfo getServiceUsage(String serviceCode) throws TelusException;

	SIDInfo getSID(String sIDCode) throws TelusException;

	SIDInfo[] getSIDs() throws TelusException;

	SpecialNumberInfo getSpecialNumber(String numberCode) throws TelusException;

	SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber) throws TelusException;

	SpecialNumberRangeInfo[] getSpecialNumberRanges() throws TelusException;

	SpecialNumberInfo[] getSpecialNumbers() throws TelusException;

	StateInfo getState(String stateCode) throws TelusException;

	StreetDirectionInfo getStreetDirection(String directionCode) throws TelusException;

	StreetDirectionInfo[] getStreetDirections() throws TelusException;

	SubscriptionRoleTypeInfo getSubscriptionRoleType(String roleTypeCode) throws TelusException;

	SubscriptionRoleTypeInfo[] getSubscriptionRoleTypes() throws TelusException;

	TalkGroupPriorityInfo[] getTalkGroupPriorities() throws TelusException;

	TaxationPolicyInfo[] getTaxationPolicies() throws TelusException;

	TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusException;

	TermUnitInfo getTermUnit(String termUnitCode) throws TelusException;

	TermUnitInfo[] getTermUnits() throws TelusException;

	TitleInfo getTitle(String titleCode) throws TelusException;

	TitleInfo[] getTitles() throws TelusException;

	UnitTypeInfo getUnitType(String typeCode) throws TelusException;

	UsageRateMethodInfo getUsageRateMethod(String rateMethodCode) throws TelusException;

	UsageRateMethodInfo[] getUsageRateMethods() throws TelusException;

	UsageRecordTypeInfo getUsageRecordType(String recordTypeCode) throws TelusException;

	UsageRecordTypeInfo[] getUsageRecordTypes() throws TelusException;

	UsageUnitInfo getUsageUnit(String unitCode) throws TelusException;

	UsageUnitInfo[] getUsageUnits() throws TelusException;

	VendorServiceInfo getVendorService(String vendorServiceCode) throws TelusException;

	VendorServiceInfo[] getVendorServices() throws TelusException;

	WorkFunctionInfo[] getWorkFunctions() throws TelusException;

	WorkFunctionInfo[] getWorkFunctions(String departmentCode) throws TelusException;

	WorkPositionInfo getWorkPosition(String workPositionCode) throws TelusException;

	WorkPositionInfo[] getWorkPositions(String functionCode) throws TelusException;

	PrepaidCategoryInfo[] getWPSCategories() throws TelusException;

	PrepaidCategoryInfo getWPSCategory(String categoryCode) throws TelusException;

	ServiceInfo[] getZeroMinutePoolingContributorServices() throws TelusException;

	ServiceInfo[] getMinutePoolingContributorServices() throws TelusException;

	/**
	 * Retrieves the one-time charge details for paper bill on the account.
	 *
	 * Any of the parameters passed can be null or empty, however there must be
	 * at least one parameter populated, otherwise will throw an
	 * IllegalArgumentException.
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
	 */
	ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode, char accountType, char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException;

	ServicePolicyInfo[] checkServicePrivilege(ServiceInfo[] services, String businessRole, String privilege) throws TelusException;

	ServiceInfo[] retainByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException;

	ServiceInfo[] removeByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException;

	ServiceInfo[] filterByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode, boolean criteria) throws TelusException;

	/**
	 * Retrieve SOC group and service codes mapping information.
	 * 
	 * @param String serviceGroupCode
	 * @return String[] list of soc as values.
	 * @throws TelusException
	 **/
	String[] getServiceCodesByGroup(String ServiceGroupCode) throws TelusException;

	/**
	 * If saleRepCode passed in is empty string or all zeros, then returned
	 * SalesRepInfo is based on dealerCode. dealerCode is determined from KB.
	 * DealerCode and Name will be set as the value returned from retrieving the
	 * dealer. Code will be set as 0000.
	 * 
	 * @param dealerCode
	 * @param salesRepCode
	 * @param expired
	 * @return
	 * @throws TelusException
	 */
	SalesRepInfo getDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException;

	BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles, String province) throws TelusException;

	public DataSharingGroupInfo[] getDataSharingGroups() throws TelusException;

	public DataSharingGroupInfo getDataSharingGroup(String groupCode) throws TelusException;

	@SuppressWarnings("rawtypes")
	Map getServicePeriodInfo(String serviceCode, String serviceType) throws TelusException;

	/**
	 * Get Service Extended Information like province codes, account type/subtypes, plan groups
	 * 
	 * @param serviceCodes
	 * @return
	 * @throws TelusException
	 */
	ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException;

	@SuppressWarnings("rawtypes")
	Map getDataSharingPricingGroups() throws TelusException;

	@SuppressWarnings("rawtypes")
	List getServiceEditions() throws TelusException;

	ServiceInfo[] getWPSServices(String[] serviceCodeArray) throws TelusException;

	ServiceFamilyTypeInfo[] getPPSServices() throws TelusException;

	/**
	 * This method retrieve the priceplan and optional services matches given
	 * criteria..
	 * 
	 * @param pPricePlanCD
	 * @param pEquipmentType
	 * @param pProvinceCD
	 * @param pAccountType
	 * @param pAccountSubType
	 * @param pBrandId
	 * @return
	 * @throws TelusException
	 */
	public PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId) throws TelusException;

	/**
	 * validates if an accountType/accountSubType is eligible for PP&S services
	 * 
	 * @param accountType
	 * @param accountSubType
	 * @return
	 * @throws TelusException
	 */
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException;

	public RoamingServiceNotificationInfo[] retrieveRoamingServiceNotificationInfo(String[] serviceCodes) throws TelusException;

	public ReferenceInfo retrieveMarketingDescriptionBySoc(String soc) throws TelusException;

	public String getNotificationTemplateSchemaVerison(String transactionType, int brandId, String accountType, String banSegment, String productType, String deliveryChannel, String language)
			throws TelusException;

	public ServiceInfo[] getIncludedPromotions(String pricePlanCode, String equipmentType, String networkType, String provinceCode, int term) throws TelusException;
	
	public boolean isCDASupportedAccountTypeSubType(String accountTypeSubType);
	
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo() throws TelusException;
	
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo(String[] socCodes) throws TelusException;
	
	@SuppressWarnings("rawtypes")
	public List getLicenses() throws TelusException;

}