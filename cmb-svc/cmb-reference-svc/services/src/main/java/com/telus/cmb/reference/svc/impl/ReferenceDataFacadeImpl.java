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

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Feature;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.Province;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceRelation;
import com.telus.api.reference.ServiceSet;
import com.telus.cmb.common.app.ApplicationServiceLocator;
import com.telus.cmb.common.cache.CacheDataGroup;
import com.telus.cmb.common.cache.CacheDataGroupReflectiveLoader;
import com.telus.cmb.common.cache.CacheDataGroupStaticLoader;
import com.telus.cmb.common.cache.CacheKeyBuilder;
import com.telus.cmb.common.cache.CacheKeyProvider;
import com.telus.cmb.common.cache.DataEntryCache;
import com.telus.cmb.common.cache.DataGroupCache;
import com.telus.cmb.common.cache.SelfPopulatiingDataEntryCache;
import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.common.util.AttributeTranslator;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.MethodInvocationJob;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.reference.bo.PricePlanBo;
import com.telus.cmb.reference.bo.ServiceBo;
import com.telus.cmb.reference.dto.FeeRuleDto;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacadeTestPoint;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.reference.utilities.AppConfiguration;
import com.telus.cmb.reference.utilities.CronExpressionUtil;
import com.telus.cmb.reference.utilities.LogicalDateRefresh;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.*;
import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.erm.referenceods.domain.RuleOutput;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;
import com.telus.framework.config.ConfigContext;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataFacadeImpl implements ReferenceDataFacade, ReferenceDataFacadeTestPoint {

	private static final Log logger = LogFactory.getLog(ReferenceDataFacadeImpl.class);

	private static final String REFERENCE_TRIGGER_GROUP = "ReferenceTriggerGroup";
	private static final String CACHE_CLEAR_TRIGGER_NAME = "ReferenceCacheClearTrigger";
	private static final String CACHE_REFRESH_TRIGGER_NAME = "ReferenceCacheRefreshTrigger";
	private static final String REFPDS_REFRESH_TRIGGER_NAME = "RefPdsRefreshTrigger";
	private static final String LOGICAL_DATE_SCHEDULE_TRIGGER_NAME = "LogicalDateScheduleTrigger";
	private static final int JOB_PRIORITY_HIGH = 10;
	private static final int JOB_PRIORITY_NORMAL = 5;

	private static final String PDS_XREF_SUBSCRIPTION_TYP_XREF_KB = "SUBSCRIPTION_TYP_XREF_KB";
	private static final String PDS_XREF_SERVICE_INSTANCE_STATUS_XREF_KB = "SERVICE_INSTANCE_STATUS_XREF_KB";
	private static final String PDS_XREF_BILLING_ACCOUNT_STATUS_XREF_KB = "BILLING_ACCOUNT_STATUS_XREF_KB";
	private static final String PDS_XREF_PAYMENT_METHOD_XREF_KB = "PAYMENT_METHOD_XREF_KB";
	private static final String PDS_XREF_CREDIT_CARD_TYP_XREF_KB = "CREDIT_CARD_TYP_XREF_KB";
	private static final String PDS_XREF_BILL_CYCLE_CODE_XREF_KB = "BILL_CYCLE_CODE_XREF_KB";
	private static final String PDS_XREF_NAME_SUFFIX_XREF_KB = "NAME_SUFFIX_XREF_KB";
	private static final String PDS_XREF_SALUTATION_CODE_XREF_KB = "SALUTATION_CODE_XREF_KB";
	private static final String PDS_XREF_SERVICE_RESOURCE_TYP_XREF_SEMS = "SERVICE_RESOURCE_TYP_XREF_SEMS";
	private static final String PDS_XREF_PROVINCE_STATE_XREF_KB = "PROVINCE_STATE_XREF_KB";
	private static final String PDS_XREF_COUNTRY_OVERSEAS_XREF_KB = "COUNTRY_OVERSEAS_XREF_KB";

	private static final String PDS_DATA_SHARING_PRICING_GROUP_TABLE = "PRICING_DATA_SHARING_GROUP";
	private static final String PDS_DATA_SHARING_PRICING_GROUP_DELIM = "[\\s]*;[\\s]*";

	private static final String PDS_SERVICE_EDITION_TABLE = "BUSINESS_CONNECT_EDITION";
	private static final String PDS_SERVICE_EDITION_RULE = "BUSINESS_CONNECT_EDITION_RULE";
	private static final String PDS_SERVICE_EDITION_RULE_CODE = "SERVICE_EDITION_CD";
	private static final String PDS_SERVICE_EDITION_RULE_BUSINESS_NAME_EN = "BUSINESS_EN_NM";
	private static final String PDS_SERVICE_EDITION_RULE_BUSINESS_NAME_FR = "BUSINESS_FR_NM";
	private static final String PDS_SERVICE_EDITION_RULE_DESCRIPTION_EN = "DESCRIPTION_EN_TXT";
	private static final String PDS_SERVICE_EDITION_RULE_DESCRIPTION_FR = "DESCRIPTION_FR_TXT";
	private static final String PDS_SERVICE_EDITION_RULE_LOCALE = "LOCALE_CD";
	private static final String PDS_SERVICE_EDITION_RULE_RANK = "RANK_NUM";

	public static final String LOGICAL_DATE_CACHE_NAME = "logicalDateCache";
	private ReferenceDataHelper referenceDataHelper;

	private CacheManager cacheManager;

	private ReferenceDataCacheKeyProvider referenceKeyProvider = new ReferenceDataCacheKeyProvider();

	private DataGroupCache<AccountTypeInfo> accountTypeCache;
	private DataGroupCache<BrandInfo> brandCache;
	private DataGroupCache<CommitmentReasonInfo> commitmentReasonCache;
	private DataGroupCache<FeatureInfo> featureCache;
	private DataGroupCache<MemoTypeInfo> memoTypeCache;
	private DataGroupCache<NotificationTypeInfo> notificationTypeCache;

	private DataGroupCache<PaymentSourceTypeInfo> paymentSourceTypeCache;
	private DataGroupCache<PoolingGroupInfo> poolingGroupCache;
	private DataGroupCache<ServiceInfo> prepaidServiceCache;
	private DataGroupCache<ServicePolicyInfo> servicePolicyCache;
	private DataGroupCache<SegmentationInfo> segmentationCache;
	private DataGroupCache<ServiceExclusionGroupsInfo> serviceExclusionGroupsCache;
	private DataGroupCache<ServiceInfo> regularServiceCache;
	private DataGroupCache<PrepaidRechargeDenominationInfo> prepaidRechargeDenominationCache;
	private DataGroupCache<ProvinceInfo> provinceCache;
	private DataGroupCache<StateInfo> stateCache;
	private DataGroupCache<UnitTypeInfo> unitTypeCache;
	private DataGroupCache<LanguageInfo> languageCache;
	private DataGroupCache<CountryInfo> countryCache;
	private DataGroupCache<PaymentMethodInfo> paymentMethodCache;
	private DataGroupCache<FollowUpTypeInfo> followUpTypeCache;
	private DataGroupCache<DepartmentInfo> departmentCache;
	private DataGroupCache<LanguageInfo> languageAllCache;
	private DataGroupCache<ProvinceInfo> provinceAllCache;
	private DataGroupCache<TitleInfo> titleAllCache;
	private DataGroupCache<PricePlanTermInfo> pricePlanTermCache;

	private SelfPopulatiingDataEntryCache<ServiceInfo[]> regularServiceGroupCache;
	private SelfPopulatiingDataEntryCache<PricePlanBo> pricePlanSummaryCache;
	private SelfPopulatiingDataEntryCache<PricePlanBo> pricePlanCache;
	private SelfPopulatiingDataEntryCache<ServiceInfo[]> includedPromotionCache;
	private DataEntryCache<SalesRepInfo> salesRepCache;
	private DataEntryCache<FleetInfo> fleetCache;

	private DataEntryCache<Date> logicalDateCache;

	// Pavel's changes

	private DataGroupCache<CountryInfo> countryAllCache;
	private DataGroupCache<CoverageRegionInfo> coverageRegionCache;
	private DataGroupCache<CreditCardPaymentTypeInfo> creditCardPaymentTypeCache;
	private DataGroupCache<CreditCardTypeInfo> creditCardTypeCache;
	private DataGroupCache<CreditMessageInfo> creditMessageCache;
	private DataGroupCache<EncodingFormatInfo> encodingFormatCache;
	private DataGroupCache<InvoiceSuppressionLevelInfo> invoiceSuppressionLevelCache;
	private DataGroupCache<ActivityTypeInfo> activityTypeCache;
	private DataGroupCache<AddressTypeInfo> addressTypeCache;
	private DataGroupCache<AdjustmentReasonInfo> adjustmentReasonCache;
	private DataGroupCache<AmountBarCodeInfo> amountBarCodeCache;
	private DataGroupCache<ApplicationSummaryInfo> applicationSummaryCache;
	private DataGroupCache<AudienceTypeInfo> audienceTypeCache;
	private DataGroupCache<BillCycleInfo> billCycleCache;
	private DataGroupCache<BillHoldRedirectDestinationInfo> billHoldRedirectDestinationCache;
	private DataGroupCache<BusinessRoleInfo> businessRoleCache;
	private DataGroupCache<ClientConsentIndicatorInfo> clientConsentIndicatorCache;
	private DataGroupCache<ClientStateReasonInfo> clientStateReasonCache;
	private DataGroupCache<CollectionActivityInfo> collectionActivityCache;
	private DataGroupCache<CollectionAgencyInfo> collectionAgencyCache;
	private DataGroupCache<CollectionPathDetailsInfo> collectionPathDetailsCache;
	private DataGroupCache<CollectionStateInfo> collectionStateCache;
	private DataGroupCache<CollectionStepApprovalInfo> collectionStepApprovalCache;
	private DataGroupCache<CorporateAccountRepInfo> corporateAccountRepCache;
	private DataGroupCache<CreditCheckDepositChangeReasonInfo> creditCheckDepositChangeReasonCache;
	private DataGroupCache<CreditClassInfo> creditClassCache;
	private DataGroupCache<DiscountPlanInfo> discountPlanCache;
	private DataGroupCache<EquipmentPossessionInfo> equipmentPossessionCache;
	private DataGroupCache<EquipmentProductTypeInfo> equipmentProductTypeCache;
	private DataGroupCache<EquipmentStatusInfo> equipmentStatusCache;
	private DataGroupCache<EquipmentTypeInfo> equipmentTypeCache;
	private DataGroupCache<ExceptionReasonInfo> exceptionReasonCache;
	private DataGroupCache<FeatureInfo> featureCategoryCache;
	private DataGroupCache<FeeWaiverReasonInfo> feeWaiverReasonCache;
	private DataGroupCache<FeeWaiverTypeInfo> feeWaiverTypeCache;
	private DataGroupCache<FleetClassInfo> fleetClassCache;
	private DataGroupCache<FollowUpCloseReasonInfo> followUpCloseReasonCache;
	private DataGroupCache<GenerationInfo> generationCache;
	private DataGroupCache<InvoiceCallSortOrderTypeInfo> invoiceCallSortOrderTypeCache;
	private DataGroupCache<KnowbilityOperatorInfo> knowbilityOperatorCache;
//	private DataGroupCache<LetterCategoryInfo> letterCategoryCache;
//	private DataGroupCache<LetterSubCategoryInfo> letterSubCategoryCache;
	private DataGroupCache<LockReasonInfo> lockReasonCache;
	private DataGroupCache<ChargeTypeInfo> chargeTypeCache;
	private DataGroupCache<PrepaidRateProfileInfo> prepaidRateProfileCache;

	private DataEntryCache<ProvinceInfo[]> provinceCountryCache;
	private DataEntryCache<ServiceInfo[]> zeroMinutePoolingServiceCache;
	private DataEntryCache<ServiceInfo[]> minutePoolingServiceCache;
	private DataEntryCache<ServiceUsageInfo> serviceUsageCache;

	// Pavel's changes end

	// Andrew's members begin

	private DataGroupCache<MemoTypeCategoryInfo> memoTypeCategoryCache;
	private DataGroupCache<MigrationTypeInfo> migrationTypeCache;
	private DataGroupCache<NetworkInfo> networkCache;
	private DataGroupCache<NetworkTypeInfo> networkTypeCache;
	private DataGroupCache<NotificationMessageTemplateInfo> notificationMessageTemplateCache;
	private DataGroupCache<NumberRangeInfo> numberRangeCache;
	private DataGroupCache<EquipmentTypeInfo> pagerEquipmentTypeCache;
	private DataGroupCache<PagerFrequencyInfo> pagerFrequencyCache;
	private DataGroupCache<PaymentMethodTypeInfo> paymentMethodTypeCache;
	private DataGroupCache<PaymentTransferReasonInfo> paymentTransferReasonCache;
	private DataGroupCache<PhoneTypeInfo> phoneTypeCache;
	private DataGroupCache<PrepaidAdjustmentReasonInfo> prepaidAdjustmentReasonCache;
	private DataGroupCache<PrepaidEventTypeInfo> prepaidEventTypeCache;
	private DataGroupCache<PrepaidAdjustmentReasonInfo> prepaidFeatureAddWaiveReasonCache;
	private DataGroupCache<PrepaidAdjustmentReasonInfo> prepaidManualAdjustmentReasonCache;
	private DataGroupCache<PrepaidAdjustmentReasonInfo> prepaidTopUpWaiveReasonCache;
	private DataGroupCache<PrepaidAdjustmentReasonInfo> prepaidDeviceDirectFulfillmentReasonCache;
	private DataGroupCache<ProductTypeInfo> productTypeCache;
	private DataGroupCache<ProvisioningPlatformTypeInfo> provisioningPlatformTypeCache;
	private DataGroupCache<ProvisioningTransactionStatusInfo> provisioningTransactionStatusCache;
	private DataGroupCache<ProvisioningTransactionTypeInfo> provisioningTransactionTypeCache;
	private DataGroupCache<HandsetRoamingCapabilityInfo> roamingCapabilityCache;
	private DataGroupCache<RouteInfo> routeCache;
	private DataGroupCache<RuralDeliveryTypeInfo> ruralDeliveryTypeCache;
	private DataGroupCache<RuralTypeInfo> ruralTypeCache;
	private DataGroupCache<ServicePeriodTypeInfo> servicePeriodTypeCache;
	private DataGroupCache<SIDInfo> sIDCache;
	private DataGroupCache<SpecialNumberInfo> specialNumbersCache;
	private DataGroupCache<SpecialNumberRangeInfo> specialNumberRangeCache;
	private DataGroupCache<StreetDirectionInfo> streetDirectionCache;
	private DataGroupCache<SubscriptionRoleTypeInfo> subscriptionRoleTypeCache;
	private DataGroupCache<TalkGroupPriorityInfo> talkGroupPriorityCache;
	private DataGroupCache<TaxationPolicyInfo> taxationPolicyCache;
	private DataGroupCache<TitleInfo> titleCache;
	private DataGroupCache<UsageRateMethodInfo> usageRateMethodCache;
	private DataGroupCache<UsageRecordTypeInfo> usageRecordTypeCache;
	private DataGroupCache<UsageUnitInfo> usageUnitCache;
	private DataGroupCache<VendorServiceInfo> vendorServiceCache;
	private DataGroupCache<WorkFunctionInfo> workFunctionCache;
	private DataGroupCache<WorkPositionInfo> workPositionCache;
	private DataGroupCache<PrepaidCategoryInfo> prepaidCategoryCache;

	// Andrew's members end

	private DataEntryCache<String[]> serviceGroupRelationCache;
	private DataGroupCache<SwapRuleInfo> equipmentSwapRulesCache;
	private DataGroupCache<BrandSwapRuleInfo> brandSwapRulesCache;

	private DataGroupCache<DataSharingGroupInfo> dataSharingGroupCache;

	private DataGroupCache<ServiceFeatureClassificationInfo> featureClassificationCache;
	private DataGroupCache<Map> servicePeriodAllCache;
	private SelfPopulatiingDataEntryCache<ServicePeriodInfo[]> servicePeriodCache;
	private SelfPopulatiingDataEntryCache<ServiceTermDto> serviceTermCache;
	private SelfPopulatiingDataEntryCache<ServiceRelationInfo[]> serviceRelationCache;
	private DataEntryCache<ServiceAirTimeAllocationInfo> serviceVoiceAllocationCache;

	private DataGroupCache<Map> serviceCodesCache;
	private DataGroupCache<SeatTypeInfo> seatTypeCache;
	private DataGroupCache<ServiceFamilyTypeInfo> ppsServicesCache;
	private DataGroupCache<ReferenceInfo> marketingDescriptionCache;
	private DataGroupCache<SapccMappedServiceInfo> sapccMappedServiceInfoCache;
	private DataGroupCache<SapccOfferInfo> sapccOfferInfoCache;
	
	private static List<String> referenceDataDynamicDefaultCacheNameList = new ArrayList<String>();
	private static Map<String, Date> activeRefreshJobMap = Collections.synchronizedMap(new HashMap<String, Date>());

	public void initialize() {

		logger.debug("Initializing reference data cache...");

		// try to initialize cache manager from LDAP

		String cacheClearSchedule = AppConfiguration.getCacheClearSchedule();
		String cacheRefreshSchedule = AppConfiguration.getCacheRefreshSchedule();
		String refpdsRefreshSchedule = AppConfiguration.getRefpdsRefreshSchedule();
		String logicalDateRefreshSchedule = AppConfiguration.getLogicalDateRefreshSchedule();
		String cacheConfiguration = AppConfiguration.getEhcacheConfig();

		if (cacheConfiguration != null) {
			logger.debug("Initializing cache manager from LDAP...");

			cacheManager = CacheManager.create(new ByteArrayInputStream(cacheConfiguration.getBytes()));
		} else {
			logger.debug("Initializing cache manager from configuration file...");

			URL url = getClass().getResource("/cacheManager.xml");
			cacheManager = CacheManager.create(url);
		}

		logger.debug("Creating cache instances...");

		logicalDateCache = new DataEntryCache<Date>(LOGICAL_DATE_CACHE_NAME, cacheManager);

		ppsServicesCache = createDataGroupCache(ServiceFamilyTypeInfo.class, "PPSServices", "retrievePPSServices");

		accountTypeCache = createDataGroupCache(AccountTypeInfo.class, "AccountType", "retrieveAccountTypes");
		brandCache = createDataGroupCache(BrandInfo.class, "Brand", "retrieveBrands");
		commitmentReasonCache = createDataGroupCache(CommitmentReasonInfo.class, "CommitmentReason", "retrieveCommitmentReasons");
		featureCache = createDataGroupCache(FeatureInfo.class, "Feature", "retrieveFeatures");
		memoTypeCache = createDataGroupCache(MemoTypeInfo.class, "MemoType", "retrieveMemoTypes");
		notificationTypeCache = createDataGroupCache(NotificationTypeInfo.class, "NotificationType", "retrieveNotificationType");
		paymentSourceTypeCache = createDataGroupCache(PaymentSourceTypeInfo.class, "PaymentSourceType", "retrievePaymentSourceTypes");
		poolingGroupCache = createDataGroupCache(PoolingGroupInfo.class, "PoolingGroup", "retrievePoolingGroups");
		prepaidServiceCache = createDataGroupCache(ServiceInfo.class, "PrepaidService", "retrieveWPSFeaturesList");
		servicePolicyCache = createDataGroupCache(ServicePolicyInfo.class, "ServicePolicy", "retrieveServicePolicyExceptions");
		segmentationCache = createDataGroupCache(SegmentationInfo.class, "Segmentation", "retrieveSegmentations");
		serviceExclusionGroupsCache = createDataGroupCache(ServiceExclusionGroupsInfo.class, "ServiceExclusionGroups", "retrieveServiceExclusionGroups");
		regularServiceCache = createDataGroupCache(ServiceInfo.class, "RegularService", "retrieveRegularServices");
		prepaidRechargeDenominationCache = createDataGroupCache(PrepaidRechargeDenominationInfo.class, "PrepaidRechargeDenomination", "retrievePrepaidRechargeDenominations");
		provinceCache = createDataGroupCache(ProvinceInfo.class, "Province", "retrieveProvinces");
		stateCache = createDataGroupCache(StateInfo.class, "State", "retrieveStates");
		unitTypeCache = createDataGroupCache(UnitTypeInfo.class, "UnitType", "retrieveUnitTypes");
		followUpTypeCache = createDataGroupCache(FollowUpTypeInfo.class, "FollowUpType", "retrieveFollowUpTypes");
		departmentCache = createDataGroupCache(DepartmentInfo.class, "Department", "retrieveDepartments");
		languageAllCache = createDataGroupCache(LanguageInfo.class, "LanguageAll", "retrieveLanguages");
		provinceAllCache = createDataGroupCache(ProvinceInfo.class, "ProvinceAll", "retrieveAllProvinces");
		titleAllCache = createDataGroupCache(TitleInfo.class, "TitleAll", "retrieveAllTitles");
		pricePlanTermCache = createDataGroupCache(PricePlanTermInfo.class, "PricePlanTerm", "retrievePricePlanTerms");

		languageCache = createDataGroupCache(LanguageInfo.class, "Language", LanguageInfo.getAll());
		countryCache = createDataGroupCache(CountryInfo.class, "Country", CountryInfo.getAll());
		paymentMethodCache = createDataGroupCache(PaymentMethodInfo.class, "PaymentMethod", PaymentMethodInfo.getAll());

		regularServiceGroupCache = createRegularServiceGroupCache();
		pricePlanSummaryCache = createPricePlanSummaryCache();
		pricePlanCache = createPricePlanCache();
		salesRepCache = createDataEntryCache("SalesRep");
		serviceUsageCache = createDataEntryCache("ServiceUsage");
		fleetCache = createDataEntryCache("Fleet");

		// Pavel's changes

		countryAllCache = createDataGroupCache(CountryInfo.class, "CountryAll", "retrieveCountries");
		coverageRegionCache = createDataGroupCache(CoverageRegionInfo.class, "CoverageRegion", "retrieveCoverageRegions");
		creditCardPaymentTypeCache = createDataGroupCache(CreditCardPaymentTypeInfo.class, "CreditCardPaymentType", "retrieveCreditCardPaymentTypes");
		creditCardTypeCache = createDataGroupCache(CreditCardTypeInfo.class, "CreditCardType", CreditCardTypeInfo.getAll());
		creditMessageCache = createDataGroupCache(CreditMessageInfo.class, "CreditMessage", "retrieveCreditMessages");
		encodingFormatCache = createDataGroupCache(EncodingFormatInfo.class, "EncodingFormat", "retrieveEncodingFormats");

		// Defect PROD00178088 Start
		//activityTypeCache = createDataGroupCache(ActivityTypeInfo.class, "ActivityType", "retrieveActivityTypes");
		CacheEntryFactory activityTypeFactory = new CacheDataGroupReflectiveLoader<ActivityTypeInfo>(referenceDataHelper, "retrieveActivityTypes", new CacheKeyProvider() {
			@Override
			public String getCacheKey(Object entry) {
				ActivityTypeInfo actvityType = (ActivityTypeInfo) entry;
				return actvityType.getCode() + "." + actvityType.getActivityType();
			}
		});
		activityTypeCache = new DataGroupCache<ActivityTypeInfo>(ActivityTypeInfo.class, "ActivityType", cacheManager, activityTypeFactory, "referenceDataStaticDefaultCache");
		// Defect PROD00178088 End		

		addressTypeCache = createDataGroupCache(AddressTypeInfo.class, "AddressType", AddressTypeInfo.getAll());
		adjustmentReasonCache = createDataGroupCache(AdjustmentReasonInfo.class, "AdjustmentReason", "retrieveAdjustmentReasons");
		amountBarCodeCache = createDataGroupCache(AmountBarCodeInfo.class, "AmountBarCode", "retrieveAmountBarCodes");
		applicationSummaryCache = createDataGroupCache(ApplicationSummaryInfo.class, "ApplicationSummary", "retrieveApplicationSummaries");
		audienceTypeCache = createDataGroupCache(AudienceTypeInfo.class, "AudienceType", "retrieveAudienceTypes");
		billCycleCache = createDataGroupCache(BillCycleInfo.class, "BillCycle", "retrieveBillCycles");
		billHoldRedirectDestinationCache = createDataGroupCache(BillHoldRedirectDestinationInfo.class, "BillHoldRedirectDestination", "retrieveBillHoldRedirectDestinations");
		businessRoleCache = createDataGroupCache(BusinessRoleInfo.class, "BusinessRole", "retrieveBusinessRoles");
		clientConsentIndicatorCache = createDataGroupCache(ClientConsentIndicatorInfo.class, "ClientConsentIndicator", "retrieveClientConsentIndicators");
		clientStateReasonCache = createDataGroupCache(ClientStateReasonInfo.class, "ClientStateReason", "retrieveClientStateReasons");
		collectionActivityCache = createDataGroupCache(CollectionActivityInfo.class, "CollectionActivity", "retrieveCollectionActivities");
		collectionAgencyCache = createDataGroupCache(CollectionAgencyInfo.class, "CollectionAgency", "retrieveCollectionAgencies");
		collectionPathDetailsCache = createDataGroupCache(CollectionPathDetailsInfo.class, "CollectionPathDetails", "retrieveCollectionPathDetails");
		collectionStateCache = createDataGroupCache(CollectionStateInfo.class, "CollectionState", "retrieveCollectionStates");
		collectionStepApprovalCache = createDataGroupCache(CollectionStepApprovalInfo.class, "CollectionStepApproval", "retrieveCollectionStepApproval");
		corporateAccountRepCache = createDataGroupCache(CorporateAccountRepInfo.class, "CorporateAccountRep", "retrieveCorporateAccountReps");
		creditCheckDepositChangeReasonCache = createDataGroupCache(CreditCheckDepositChangeReasonInfo.class, "CreditCheckDepositChangeReason", "retrieveCreditCheckDepositChangeReasons");
		creditClassCache = createDataGroupCache(CreditClassInfo.class, "CreditClass", "retrieveCreditClasses");
		discountPlanCache = createDataGroupCache(DiscountPlanInfo.class, "DiscountPlan", "retrieveDiscountPlans");
		equipmentPossessionCache = createDataGroupCache(EquipmentPossessionInfo.class, "EquipmentPossession", "retrieveEquipmentPossessions");
		equipmentProductTypeCache = createDataGroupCache(EquipmentProductTypeInfo.class, "EquipmentProductType", "retrieveEquipmentProductTypes");
		equipmentStatusCache = createDataGroupCache(EquipmentStatusInfo.class, "EquipmentStatus", "retrieveEquipmentStatuses");
		equipmentTypeCache = createDataGroupCache(EquipmentTypeInfo.class, "EquipmentType", "retrieveEquipmentTypes");
		exceptionReasonCache = createDataGroupCache(ExceptionReasonInfo.class, "ExceptionReason", "retrieveExceptionReasons");
		featureCategoryCache = createDataGroupCache(FeatureInfo.class, "FeatureCategory", "retrieveFeatureCategories");
		feeWaiverReasonCache = createDataGroupCache(FeeWaiverReasonInfo.class, "FeeWaiverReason", "retrieveFeeWaiverReasons");
		feeWaiverTypeCache = createDataGroupCache(FeeWaiverTypeInfo.class, "FeeWaiverType", "retrieveFeeWaiverTypes");
		fleetClassCache = createDataGroupCache(FleetClassInfo.class, "FleetClass", "retrieveFleetClasses");
		followUpCloseReasonCache = createDataGroupCache(FollowUpCloseReasonInfo.class, "FollowUpCloseReason", "retrieveFollowUpCloseReasons");
		generationCache = createDataGroupCache(GenerationInfo.class, "Generation", "retrieveGenerations");
		invoiceCallSortOrderTypeCache = createDataGroupCache(InvoiceCallSortOrderTypeInfo.class, "InvoiceCallSortOrderType", "retrieveInvoiceCallSortOrderTypes");
		knowbilityOperatorCache = createDataGroupCache(KnowbilityOperatorInfo.class, "KnowbilityOperator", "retrieveKnowbilityOperators");
//		letterCategoryCache = createDataGroupCache(LetterCategoryInfo.class, "LetterCategory", "retrieveLetterCategories");
//		letterSubCategoryCache = createDataGroupCache(LetterSubCategoryInfo.class, "LetterSubCategory", "retrieveLetterSubCategories");
		lockReasonCache = createDataGroupCache(LockReasonInfo.class, "LockReason", "retrieveLockReasons");
		chargeTypeCache = createDataGroupCache(ChargeTypeInfo.class, "ChargeType", "retrieveManualChargeTypes");
		prepaidRateProfileCache = createDataGroupCache(PrepaidRateProfileInfo.class, "PrepaidRateProfile", "retrievePrepaidRates");
		provinceCountryCache = createDataEntryCache("ProvinceCountry");
		zeroMinutePoolingServiceCache = createDataEntryCache("ZeroMinutePoolingServices");
		minutePoolingServiceCache = createDataEntryCache("MinutePoolingServices");

		// Pavel's changes end

		// Andrew's cache entries begin

		memoTypeCategoryCache = createDataGroupCache(MemoTypeCategoryInfo.class, "MemoTypeCategory", "retrieveMemoTypeCategories");
		migrationTypeCache = createDataGroupCache(MigrationTypeInfo.class, "MigrationType", "retrieveMigrationTypes");
		networkCache = createDataGroupCache(NetworkInfo.class, "Network", "retrieveNetworks");
		networkTypeCache = createDataGroupCache(NetworkTypeInfo.class, "NetworkType", "retrieveNetworkTypes");
		notificationMessageTemplateCache = createDataGroupCache(NotificationMessageTemplateInfo.class, "NotificationMessageTemplate", "retrieveNotificationMessageTemplateInfo");
		numberRangeCache = createDataGroupCache(NumberRangeInfo.class, "NumberRange", "retrieveNumberRanges");
		pagerEquipmentTypeCache = createDataGroupCache(EquipmentTypeInfo.class, "PagerEquipmentType", "retrievePagerEquipmentTypes");
		pagerFrequencyCache = createDataGroupCache(PagerFrequencyInfo.class, "PagerFrequency", "retrievePagerFrequencies");
		paymentMethodTypeCache = createDataGroupCache(PaymentMethodTypeInfo.class, "PaymentMethodType", "retrievePaymentMethodTypes");
		paymentTransferReasonCache = createDataGroupCache(PaymentTransferReasonInfo.class, "PaymentTransferReason", "retrievePaymentTransferReasons");
		phoneTypeCache = createDataGroupCache(PhoneTypeInfo.class, "PaymentType", PhoneTypeInfo.getAll());
		prepaidAdjustmentReasonCache = createDataGroupCache(PrepaidAdjustmentReasonInfo.class, "PrepaidAdjustmentReason", "retrievePrepaidAdjustmentReasons");
		prepaidEventTypeCache = createDataGroupCache(PrepaidEventTypeInfo.class, "PrepaidEventType", "retrievePrepaidEventTypes");
		prepaidFeatureAddWaiveReasonCache = createDataGroupCache(PrepaidAdjustmentReasonInfo.class, "PrepaidFeatureAddWaiveReason", "retrievePrepaidFeatureAddWaiveReasons");
		prepaidManualAdjustmentReasonCache = createDataGroupCache(PrepaidAdjustmentReasonInfo.class, "PrepaidManualAdjustmentReason", "retrievePrepaidManualAdjustmentReasons");
		prepaidTopUpWaiveReasonCache = createDataGroupCache(PrepaidAdjustmentReasonInfo.class, "PrepaidTopUpWaiveReason", "retrievePrepaidTopUpWaiveReasons");
		prepaidDeviceDirectFulfillmentReasonCache = createDataGroupCache(PrepaidAdjustmentReasonInfo.class, "PrepaidDeviceDirectFulfillmentReason", "retrievePrepaidDeviceDirectFulfillmentReasons");
		productTypeCache = createDataGroupCache(ProductTypeInfo.class, "ProductType", "retrieveProductTypes");
		provisioningPlatformTypeCache = createDataGroupCache(ProvisioningPlatformTypeInfo.class, "ProvisioningPlatformType", "retrieveProvisioningPlatformTypes");
		provisioningTransactionStatusCache = createDataGroupCache(ProvisioningTransactionStatusInfo.class, "ProvisioningTransactionStatus", "retrieveProvisioningTransactionStatuses");
		provisioningTransactionTypeCache = createDataGroupCache(ProvisioningTransactionTypeInfo.class, "ProvisioningTransactionType", "retrieveProvisioningTransactionTypes");
		roamingCapabilityCache = createDataGroupCache(HandsetRoamingCapabilityInfo.class, "RoamingCapability", "retrieveRoamingCapability");
		routeCache = createDataGroupCache(RouteInfo.class, "Route", "retrieveRoutes");
		ruralDeliveryTypeCache = createDataGroupCache(RuralDeliveryTypeInfo.class, "RuralDeliveryType", RuralDeliveryTypeInfo.getAll());
		ruralTypeCache = createDataGroupCache(RuralTypeInfo.class, "RuralType", RuralTypeInfo.getAll());
		servicePeriodTypeCache = createDataGroupCache(ServicePeriodTypeInfo.class, "ServicePeriodType", ServicePeriodTypeInfo.getAll());
		sIDCache = createDataGroupCache(SIDInfo.class, "SID", "retrieveSIDs");
		specialNumbersCache = createDataGroupCache(SpecialNumberInfo.class, "SpecialNumbers", "retrieveSpecialNumbers");
		specialNumberRangeCache = createDataGroupCache(SpecialNumberRangeInfo.class, "SpecialNumberRange", "retrieveSpecialNumberRanges");
		streetDirectionCache = createDataGroupCache(StreetDirectionInfo.class, "StreetDirection", StreetDirectionInfo.getAll());
		subscriptionRoleTypeCache = createDataGroupCache(SubscriptionRoleTypeInfo.class, "SubscriptionRoleType", "retrieveSubscriptionRoleTypes");
		talkGroupPriorityCache = createDataGroupCache(TalkGroupPriorityInfo.class, "TalkGroupPriority", "retrieveTalkGroupPriorities");
		taxationPolicyCache = createDataGroupCache(TaxationPolicyInfo.class, "TaxationPolicy", "retrieveTaxPolicies");
		titleCache = createDataGroupCache(TitleInfo.class, "Title", "retrieveTitles");
		usageRateMethodCache = createDataGroupCache(UsageRateMethodInfo.class, "UsageRateMethod", UsageRateMethodInfo.getAll());
		usageRecordTypeCache = createDataGroupCache(UsageRecordTypeInfo.class, "UsageRecordType", UsageRecordTypeInfo.getAll());
		usageUnitCache = createDataGroupCache(UsageUnitInfo.class, "UsageUnit", UsageUnitInfo.getAll());
		vendorServiceCache = createDataGroupCache(VendorServiceInfo.class, "VendorService", "retrieveVendorServices");
		workFunctionCache = createDataGroupCache(WorkFunctionInfo.class, "WorkFunction", "retrieveWorkFunctions");
		workPositionCache = createDataGroupCache(WorkPositionInfo.class, "WorkPosition", "retrieveWorkPositions");

		// Andrew's cache entries end

		serviceGroupRelationCache = createDataEntryCache("ServiceGroupRelation");
		equipmentSwapRulesCache = createDataGroupCache(SwapRuleInfo.class, "EquipmentSwapRule", "retrieveSwapRules");
		brandSwapRulesCache = createDataGroupCache(BrandSwapRuleInfo.class, "BrandSwapRules", "retrieveBrandSwapRules");

		// cache with custom loader
		prepaidCategoryCache = new DataGroupCache<PrepaidCategoryInfo>(PrepaidCategoryInfo.class, "PrepaidCategory", cacheManager, new PrepaidCategoryLoader());

		invoiceSuppressionLevelCache = new DataGroupCache<InvoiceSuppressionLevelInfo>(InvoiceSuppressionLevelInfo.class, "InvoiceSuppressionLevel", cacheManager, new InvoiceSuppressionLevelLoader());

		dataSharingGroupCache = createDataGroupCache(DataSharingGroupInfo.class, "DataSharingGroup", "retrieveDataSharingGroups");

		featureClassificationCache = createDataGroupCache(ServiceFeatureClassificationInfo.class, "ServiceFeatureClassification", "retrieveServiceFeatureClassifications");
		servicePeriodAllCache = createDataGroupCache(Map.class, "ServicePeriods", "retrieveServicePeriodInfos");
		servicePeriodCache = createServicePeriodInfoDataEntryCache();
		serviceTermCache = createServiceTermCache();
		serviceRelationCache = createServiceRelationCache();
		serviceVoiceAllocationCache = createDataEntryCache("ServiceVoiceAllocationCache");
		serviceCodesCache = createDataGroupCache(Map.class, "ServiceCodes", "retrieveServiceCodes");

		seatTypeCache = createDataGroupCache(SeatTypeInfo.class, "SeatType", "retrieveSeatTypes");
		marketingDescriptionCache = createDataGroupCache(ReferenceInfo.class, "MarketingDescription", "retrieveMarketingDescriptions");
		includedPromotionCache = createIncludedPromotionCache();
		
		sapccMappedServiceInfoCache = createDataGroupCache(SapccMappedServiceInfo.class, "SapccMappedServiceInfo", "retrieveSapccMappedServiceInfo");				
		CacheEntryFactory sapccOfferInfoFactory = new CacheDataGroupReflectiveLoader<SapccOfferInfo>(referenceDataHelper, "retrieveSapccOfferInfo", new CacheKeyProvider() {
			@Override
			public String getCacheKey(Object entry) {
				return ((SapccOfferInfo) entry).getOfferId();
			}
		});
		sapccOfferInfoCache = new DataGroupCache<SapccOfferInfo>(SapccOfferInfo.class, "SapccOfferInfo", cacheManager, sapccOfferInfoFactory, "referenceDataStaticDefaultCache");
		
		/**
		 * Initialize PDS Ref Data
		 */

		// start cache refresh job
		if (AppConfiguration.isRefEjbCacheStrategyRollback()) {
			scheduleCacheClearingJob(cacheClearSchedule);
		} else {
			scheduleCacheRefreshJob(cacheRefreshSchedule);
			scheduleRefPdsRefreshJob(refpdsRefreshSchedule);
		}
		scheduleLogicalDateRefreshJob(logicalDateRefreshSchedule);

		logger.debug("Reference data cache initialized successfully.");
	}

	/**
	 * @deprecated
	 * @param cacheClearSchedule
	 */
	private void scheduleCacheClearingJob(String cacheClearSchedule) {
		if (!StringUtils.isEmpty(cacheClearSchedule)) {
			logger.debug("Creating cache clearing job using cron expression [" + cacheClearSchedule + "]");

			JobDetail jobDetail = new JobDetail("ReferenceCacheClearingJob", MethodInvocationJob.class);

			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_OBJECT, this);
			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_METHOD, "clearCache");

			Trigger trigger = null;

			try {

				trigger = new CronTrigger(CACHE_CLEAR_TRIGGER_NAME, REFERENCE_TRIGGER_GROUP, cacheClearSchedule);

			} catch (Exception e) {
				logger.error("Unable to create trigger for expression [" + cacheClearSchedule + "]: " + e.getMessage(), e);

				trigger = TriggerUtils.makeDailyTrigger(CACHE_CLEAR_TRIGGER_NAME, 4, 0);
				trigger.setGroup(REFERENCE_TRIGGER_GROUP);
			}

			// schedule job

			try {
				Date firstTimeClearDate = ApplicationServiceLocator.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
				logger.debug("Cache clearing job scheduled at [" + firstTimeClearDate + "].");

			} catch (Exception e) {
				logger.error("Unable to schedule cache clearing job: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Oct 2016
	 * @param cacheRefreshSchedule
	 */
	private void scheduleCacheRefreshJob(String cacheRefreshSchedule) {
		if (!StringUtils.isEmpty(cacheRefreshSchedule)) {
			String randomizedRefreshSchedule = CronExpressionUtil.addRandomMinute(cacheRefreshSchedule, 30);

			logger.debug("Creating cache refreshing job using cron expression [" + randomizedRefreshSchedule + "]");

			JobDetail jobDetail = new JobDetail("ReferenceCacheRefreshJob", MethodInvocationJob.class);

			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_OBJECT, this);
			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_METHOD, "refreshAllCache");

			Trigger trigger = null;

			try {

				trigger = new CronTrigger(CACHE_REFRESH_TRIGGER_NAME, REFERENCE_TRIGGER_GROUP, randomizedRefreshSchedule);
				trigger.setPriority(JOB_PRIORITY_HIGH);

			} catch (Exception e) {
				logger.error("Unable to create trigger for expression [" + randomizedRefreshSchedule + "]: " + e.getMessage(), e);

				trigger = TriggerUtils.makeDailyTrigger(CACHE_REFRESH_TRIGGER_NAME, 4, 0);
				trigger.setGroup(REFERENCE_TRIGGER_GROUP);
			}

			// schedule job

			try {
				Date firstTimeClearDate = ApplicationServiceLocator.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
				logger.debug("Cache refresh job scheduled at [" + firstTimeClearDate + "].");

			} catch (Exception e) {
				logger.error("Unable to schedule cache refresh job: " + e.getMessage(), e);
			}
		}
	}

	private void scheduleRefPdsRefreshJob(String refpdsRefreshSchedule) {
		if (!StringUtils.isEmpty(refpdsRefreshSchedule)) {
			logger.debug("Creating refpds refreshing job using cron expression [" + refpdsRefreshSchedule + "]");

			JobDetail jobDetail = new JobDetail("ReferencePdsRefreshJob", MethodInvocationJob.class);

			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_OBJECT, this);
			jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_METHOD, "initializePdsRefData");

			Trigger trigger = null;

			try {

				trigger = new CronTrigger(REFPDS_REFRESH_TRIGGER_NAME, REFERENCE_TRIGGER_GROUP, refpdsRefreshSchedule);
				trigger.setPriority(JOB_PRIORITY_NORMAL);
			} catch (Exception e) {
				logger.error("Unable to create trigger for expression [" + refpdsRefreshSchedule + "]: " + e.getMessage(), e);

				trigger = TriggerUtils.makeDailyTrigger(REFPDS_REFRESH_TRIGGER_NAME, 4, 30);
				trigger.setGroup(REFERENCE_TRIGGER_GROUP);
			}

			// schedule job

			try {
				Date firstTimeClearDate = ApplicationServiceLocator.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
				logger.debug("Refpds refresh job scheduled at [" + firstTimeClearDate + "].");

			} catch (Exception e) {
				logger.error("Unable to schedule refpds refresh job: " + e.getMessage(), e);
			}
		}
	}

	private void scheduleLogicalDateRefreshJob(String logicalDateRefreshSchedule) {
		int duration = AppConfiguration.getLogicalDateRefreshDuration();
		int timeout = AppConfiguration.getLogicalDateRefreshTimeout();

		logger.info("Creating logical date schedule job using cron expression [" + logicalDateRefreshSchedule + "] duration [" + duration + "] timeout [" + timeout + "]");

		LogicalDateRefresh logicalDateRefresh = new LogicalDateRefresh(duration, timeout, this);
		JobDetail jobDetail = new JobDetail("LogicalDateRefreshJob", MethodInvocationJob.class);
		jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_OBJECT, logicalDateRefresh);
		jobDetail.getJobDataMap().put(MethodInvocationJob.JOB_TARGET_METHOD, "execute");

		Trigger trigger = null;
		try {
			trigger = new CronTrigger(LOGICAL_DATE_SCHEDULE_TRIGGER_NAME, REFERENCE_TRIGGER_GROUP, logicalDateRefreshSchedule);
			trigger.setPriority(JOB_PRIORITY_HIGH);
		} catch (Exception e) {
			logger.error("Unable to create trigger for expression [" + logicalDateRefreshSchedule + "]: " + e.getMessage(), e);

			trigger = TriggerUtils.makeDailyTrigger(LOGICAL_DATE_SCHEDULE_TRIGGER_NAME, 2, 0);
			trigger.setGroup(REFERENCE_TRIGGER_GROUP);
		}
		// schedule job
		try {
			Date firstTimeClearDate = ApplicationServiceLocator.getInstance().getScheduler().scheduleJob(jobDetail, trigger);
			logger.info("logical date refresh job scheduled at [" + firstTimeClearDate + "].");

		} catch (Exception e) {
			logger.error("Unable to schedule logical date refresh job: " + e.getMessage(), e);
		}
	}

	private <T> DataGroupCache<T> createDataGroupCache(Class<T> type, String cacheName, Object[] entries) {
		CacheEntryFactory factory = new CacheDataGroupStaticLoader<T>(entries, referenceKeyProvider);
		return new DataGroupCache<T>(type, cacheName, cacheManager, factory);
	}

	private <T> DataGroupCache<T> createDataGroupCache(Class<T> type, String cacheName, String methodName) {
		CacheEntryFactory factory = new CacheDataGroupReflectiveLoader<T>(referenceDataHelper, methodName, referenceKeyProvider);
		return new DataGroupCache<T>(type, cacheName, cacheManager, factory, "referenceDataStaticDefaultCache");
	}

	private <T> DataGroupCache<T> createDataGroupCache(Class<T> type, String cacheName, String methodName, boolean cachePreload) {
		CacheEntryFactory factory = new CacheDataGroupReflectiveLoader<T>(referenceDataHelper, methodName, referenceKeyProvider);
		return new DataGroupCache<T>(type, cacheName, cacheManager, factory, "referenceDataStaticDefaultCache", cachePreload);
	}

	private <T> DataEntryCache<T> createDataEntryCache(String cacheName) {
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new DataEntryCache<T>(cacheName, cacheManager, "referenceDataDynamicDefaultCache");
	}

	public void setReferenceDataHelper(ReferenceDataHelper referenceDataHelper) {
		this.referenceDataHelper = referenceDataHelper;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#checkServiceAssociation(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkServiceAssociation(String serviceCode, String pricePlanCode) throws TelusException {
		return referenceDataHelper.isServiceAssociatedToPricePlan(Info.padTo(pricePlanCode, ' ', 9), Info.padTo(serviceCode, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#checkServicePrivilege(java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkServicePrivilege(String[] serviceCodes, String businessRoleCode, String privilegeCode) throws TelusException {
		if (serviceCodes == null || serviceCodes.length == 0) {
			if (logger.isDebugEnabled()) {
				logger.debug("serviceCodes is null or empty.");
			}
			
			return new ServicePolicyInfo[0];
		}
		
		ServiceInfo[] regularServiceArray = getRegularServices(serviceCodes);
		//2018-10. Look up WPS only if regular services cache comes back with less data than the input. This reduces the downstream dependency if only KB is involved.
		if (regularServiceArray != null && regularServiceArray.length < serviceCodes.length) { 
			// [Defect 45423 fix : Naresh Annabathula ] ,added below two methods to support checkServicePrivilege for prepaid service codes.
			ServiceInfo[] wpsServiceArray = getWPSServices(serviceCodes);
			ServiceInfo[] regularServicesAndWPSServices = mergeRegularServicesAndWPSServices(regularServiceArray, wpsServiceArray);
			return checkServicePrivilege(regularServicesAndWPSServices, businessRoleCode, privilegeCode);
		} else {
			return checkServicePrivilege(regularServiceArray, businessRoleCode, privilegeCode);
		}
	}

	@Override
	public ServiceInfo[] getWPSServices(String[] serviceCodeArray) throws TelusException {
		List<ServiceInfo> wpsServicesList = new ArrayList<ServiceInfo>();
		for (String serviceCode : serviceCodeArray) {
			ServiceInfo service = getWPSService(serviceCode);
			if (service != null) {
				wpsServicesList.add(service);
			}

		}
		return wpsServicesList.toArray(new ServiceInfo[wpsServicesList.size()]);
	}

	private ServiceInfo[] mergeRegularServicesAndWPSServices(ServiceInfo[] regularServiceArray, ServiceInfo[] wpsServiceArray) {
		List<ServiceInfo> both = new ArrayList<ServiceInfo>(regularServiceArray.length + wpsServiceArray.length);
		Collections.addAll(both, regularServiceArray);
		Collections.addAll(both, wpsServiceArray);
		return both.toArray(new ServiceInfo[both.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#checkPricePlanPrivilege(java.lang.String[], java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkPricePlanPrivilege(String[] pricePlanCodes, String businessRoleCode, String privilegeCode) throws TelusException {

		PricePlanInfo pricePlanservices[] = getPricePlans(pricePlanCodes);
		List<ServiceInfo> service = new ArrayList<ServiceInfo>();
		for (PricePlanInfo pricePlan : pricePlanservices) {
			if ("P".equalsIgnoreCase(pricePlan.getServiceType())) {
				service.add(pricePlan);

			}
		}
		ServiceInfo[] services = service.toArray(new ServiceInfo[service.size()]);

		return checkServicePrivilege(services, businessRoleCode, privilegeCode);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#checkServicePrivilege(com.telus.eas.utility.info.ServiceInfo[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkServicePrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusException {
		List<ServicePolicyInfo> result = new ArrayList<ServicePolicyInfo>();
		for (ServiceInfo service : services) {
			ServiceBo serviceBo = new ServiceBo(service);
			result.add(serviceBo.getServicePolicy(businessRoleCode, privilegeCode, this));
		}
		return result.toArray(new ServicePolicyInfo[result.size()]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#removeByPrivilege(com.telus.eas.utility.info.ServiceInfo[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo[] removeByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException {
		return filterByPrivilege(services, businessRoleCode, privilegeCode, false);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#retainByPrivilege(com.telus.eas.utility.info.ServiceInfo[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo[] retainByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException {
		return filterByPrivilege(services, businessRoleCode, privilegeCode, true);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#filterByPrivilege(com.telus.eas.utility.info.ServiceInfo[], java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ServiceInfo[] filterByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode, boolean criteria) throws TelusException {
		Map<String, ServiceInfo> serviceMap = new HashMap<String, ServiceInfo>();
		for (ServiceInfo service : services) {
			serviceMap.put(service.getCode(), service);
		}

		ServicePolicyInfo[] servicePolicies = checkServicePrivilege(services, businessRoleCode, privilegeCode);

		List<ServiceInfo> result = new ArrayList<ServiceInfo>();

		for (ServicePolicyInfo servicePolicy : servicePolicies) {
			if (servicePolicy.isAvailable() == criteria) {
				result.add(serviceMap.get(servicePolicy.getServiceCode()));
			}
		}

		return result.toArray(new ServiceInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getAccountType(java.lang.String, int, java.lang.String)
	 */
	@Override
	public AccountTypeInfo getAccountType(String accountTypeCode, int brandId) throws TelusException {
		AccountTypeInfo accountType = accountTypeCache.get(accountTypeCode);

		// TODO: this is a temporary workaround until brands are supported when retrieving default dealer and sales reps
		// set the brand, default dealer and default sales rep id based on the supplied brand value; note that the
		// default brand in the AccountTypeInfo class is BRAND_ID_TELUS

		if (accountType != null && brandId == Brand.BRAND_ID_KOODO) {
			try {
				accountType = (AccountTypeInfo) BeanUtils.cloneBean(accountType);
				accountType.setBrandId(brandId);
				accountType.setDefaultDealer(AppConfiguration.getDefaultKoodoDealerCode());
				accountType.setDefaultSalesCode(AppConfiguration.getDefaultKoodoSalesRepCode());
			} catch (Throwable t) {
				throw new TelusException(t);
			}
		}

		if (brandId != Brand.BRAND_ID_ALL && brandId != Brand.BRAND_ID_AMPD && brandId != Brand.BRAND_ID_TELUS && brandId != Brand.BRAND_ID_KOODO) {
			if (accountType != null) {
				accountType = (AccountTypeInfo) accountType.clone();
				accountType.setBrandId(brandId);

				HashMap<String, String> defaultDealerCodeMap = AppConfiguration.getDefaultDealerCodeMap();
				HashMap<String, String> defaultSalesRepCodeMap = AppConfiguration.getDefaultSalesRepCodeMap();

				if (defaultDealerCodeMap.containsKey(String.valueOf(brandId)))
					accountType.setDefaultDealer(defaultDealerCodeMap.get(String.valueOf(brandId)));
				if (defaultSalesRepCodeMap.containsKey(String.valueOf(brandId)))
					accountType.setDefaultSalesCode(defaultSalesRepCodeMap.get(String.valueOf(brandId)));
			}
		}

		return accountType;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getAccountTypes(java.lang.String)
	 */
	@Override
	public AccountTypeInfo[] getAccountTypes() throws TelusException {
		return accountTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getBrandByBrandId(int, java.lang.String)
	 */
	@Override
	public BrandInfo getBrandByBrandId(int brandId) throws TelusException {
		return brandCache.get(Integer.toString(brandId));
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrand(java.lang.String)
	 */
	@Override
	public BrandInfo getBrand(String code) throws TelusException {
		return brandCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getBrands(java.lang.String)
	 */
	@Override
	public BrandInfo[] getBrands() throws TelusException {
		return brandCache.getAll();
	}

	@Override
	public BrandSwapRuleInfo[] getBrandSwapRules() throws TelusException {
		return brandSwapRulesCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getCommitmentReason(java.lang.String, java.lang.String)
	 */
	@Override
	public CommitmentReasonInfo getCommitmentReason(String commitmentReasonCode) throws TelusException {
		return commitmentReasonCache.get(commitmentReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getCommitmentReasons(java.lang.String)
	 */
	@Override
	public CommitmentReasonInfo[] getCommitmentReasons() throws TelusException {
		return commitmentReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquivalentServiceByServiceCodeList(java.lang.String[], java.lang.String[], java.lang.String)
	 */
	@Override
	public Map<String, ServiceInfo> getEquivalentServiceByServiceCodeList(String[] originalServiceCodeList, String[] destinationServiceCodeList, String networkType) throws TelusException {
		Map<String, ServiceInfo> serviceMap = new Hashtable<String, ServiceInfo>();

		for (String originalServiceCode : originalServiceCodeList) {
			String equivalentCode = null;

			for (String destinationServiceCode : destinationServiceCodeList) {
				if (destinationServiceCode.trim().equals(originalServiceCode.trim())) {
					equivalentCode = destinationServiceCode;
					break;
				}
			}

			if (equivalentCode == null) {
				ServiceInfo originalService = getRegularService(originalServiceCode);
				if (originalService != null && originalService.hasEquivalentService()) {

					// retrieve service family codes
					String[] serviceFamilyCodes = referenceDataHelper.retrieveServiceFamily(originalServiceCode, ReferenceDataManager.EQUIVALENT_SERVICE_FAMILY_TYPE, networkType);

					// find first occurrence of service family code
					for (String serviceFamilyCode : serviceFamilyCodes) {
						for (String destinationServiceCode : destinationServiceCodeList) {
							if (destinationServiceCode.trim().equals(serviceFamilyCode.trim())) {
								equivalentCode = destinationServiceCode;
								break;
							}
						}
						if (equivalentCode != null) {
							break;
						}
					}
				}
			}
			if (equivalentCode != null) {
				ServiceInfo equivalentService = getRegularService(equivalentCode);
				if (equivalentService != null) {
					serviceMap.put(originalServiceCode, equivalentService);
				}
			}
		}
		return serviceMap;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquivalentService(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo getEquivalentService(String serviceCode, String pricePlanCode) throws TelusException {
		PricePlanInfo pricePlan = getPricePlan(pricePlanCode);
		if (pricePlan != null) {
			List<ServiceInfo> pricePlanServices = new ArrayList<ServiceInfo>();

			// add included services
			if (pricePlan.getIncludedServices0() != null) {
				pricePlanServices.addAll(Arrays.asList(pricePlan.getIncludedServices0()));
			}

			// add optional services
			if (pricePlan.getOptionalServices0() != null) {
				pricePlanServices.addAll(Arrays.asList(pricePlan.getOptionalServices0()));
			}

			// search for the price plan
			for (ServiceInfo pricePlanService : pricePlanServices) {
				if (pricePlanService.getCode().equals(serviceCode)) {
					return pricePlanService;
				}
			}

			ServiceInfo service = getRegularService(serviceCode);

			if (service != null && service.hasEquivalentService()) {

				// retrieve service family codes
				String[] serviceFamilyCodes = referenceDataHelper.retrieveServiceFamily(serviceCode, ReferenceDataManager.EQUIVALENT_SERVICE_FAMILY_TYPE, NetworkType.NETWORK_TYPE_ALL);

				// find first occurrence of service family code
				for (String serviceFamilyCode : serviceFamilyCodes) {

					// in price plan's services
					for (ServiceInfo pricePlanService : pricePlanServices) {
						if (pricePlanService.getCode().equals(serviceFamilyCode)) {
							return pricePlanService;
						}
					}
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getFeature(java.lang.String, java.lang.String)
	 */
	@Override
	public FeatureInfo getFeature(String featureCode) throws TelusException {
		FeatureInfo feature = featureCache.get(Info.padTo(featureCode, ' ', 6));
		if (feature == null) {
			ServiceInfo service = getWPSService(featureCode);
			if (service != null) {
				try {
					return service.getFeature0(featureCode);
				} catch (Exception e) {
					logger.debug("Feature not found for code [" + featureCode + "]");
				}
			}
		}
		return feature;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getFeatures(java.lang.String)
	 */
	@Override
	public FeatureInfo[] getFeatures() throws TelusException {
		return featureCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getLogicalDate(java.lang.String)
	 */
	@Override
	public Date getLogicalDate() throws TelusException {
		return logicalDateCache.get(DataEntryCache.CACHE_DEFAULT_KEY, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				logger.info("Retrieving logical date from helper...");
				return referenceDataHelper.retrieveLogicalDate();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMandatoryServices(java.lang.String, boolean, boolean, boolean, boolean, boolean, java.lang.String)
	 */
	@Override
	public ServiceSetInfo[] getMandatoryServices(String pricePlanCode, String handSetType, String productType, String equipmentType, String provinceCode, String accountType, String accountSubType,
			long brandId) throws TelusException {

		int brand = (int) brandId;
		PricePlanInfo pricePlan = getPricePlan(productType, pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brand);
		Service[] services = pricePlan.getOptionalServices();
		if ("RIM".equals(handSetType) && !pricePlan.isRIM()) {
			RIMServiceSetInfo[] rim = new RIMServiceSetInfo[1];
			rim[0] = new RIMServiceSetInfo(getRIMMandatoryServiceSet(services));
			return rim;
		} else if ("PDA".equals(handSetType) && !pricePlan.isPDA()) {
			PDAServiceSetInfo[] pda = new PDAServiceSetInfo[1];
			pda[0] = new PDAServiceSetInfo(getPDAMandatoryServiceSet(services));
			return pda;
		}
		return new ServiceSetInfo[0];
	}

	private List<Service> getRIMMandatoryServiceSet(Service[] services) {
		List<Service> rimServices = new ArrayList<Service>();
		for (Service service : services) {
			if (service.isMandatory() && isRimCheck((ServiceInfo) service)) {
				rimServices.add(service);
			}
		}
		return rimServices;
	}

	private boolean isRimCheck(ServiceInfo service) {
		if (!ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(service.getServiceType()) && Equipment.PRODUCT_TYPE_PCS.equals(service.getProductType())) {
			return service.isInRIMMandatoryGroup() || service.containsSwitchCode("RIMBES");
		} else if (!ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(service.getServiceType()) && Equipment.PRODUCT_TYPE_IDEN.equals(service.getProductType())) {

			String[] categoryCodes = service.getCategoryCodes();
			if (categoryCodes == null || categoryCodes.length == 0) {
				return false;
			}

			String code = null;
			for (int i = 0; i < categoryCodes.length; i++) {
				code = categoryCodes[i];
				if (FeatureInfo.CATEGORY_CODE_RIM.equals(code)) {
					return true;
				}
			}
		}

		return false;
	}

	private List<Service> getPDAMandatoryServiceSet(Service[] services) {
		List<Service> pdaServices = new ArrayList<Service>();
		for (Service service : services) {
			if (service.isPDA() && service.isMandatory()) {
				pdaServices.add(service);
			}
		}
		return pdaServices;
	}

	private ServiceSetInfo getMandatoryServiceSet(PricePlanInfo pricePlan, String serviceSetCode) {

		ServiceSetInfo serviceSet = null;
		List<ServiceInfo> services = new ArrayList<ServiceInfo>();
		ServiceInfo[] optionalServices = pricePlan.getOptionalServices0();
		if (optionalServices != null) {
			for (ServiceInfo service : optionalServices) {
				if ((ServiceSet.CODE_RIM.equals(serviceSetCode) && service.isRIM() && service.isMandatory())
						|| (ServiceSet.CODE_PDA.equals(serviceSetCode) && service.isPDA() && service.isMandatory())) {
					services.add(service);
				}
			}
		}

		if (!services.isEmpty()) {
			serviceSet = new ServiceSetInfo();
			serviceSet.setCode(serviceSetCode);
			serviceSet.setServices(services.toArray(new ServiceInfo[services.size()]));

			if (ServiceSet.CODE_RIM.equals(serviceSetCode)) {
				serviceSet.setDescription("RIM Mandatory Services");
				serviceSet.setDescriptionFrench("Services obligatoires liés à l’'appareil RIM");
			} else if (ServiceSet.CODE_PDA.equals(serviceSetCode)) {
				serviceSet.setDescription("PDA Mandatory Services");
				serviceSet.setDescriptionFrench("Services obligatoires liés à l’'appareil PDA");
			}
		}

		return serviceSet;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMemoType(java.lang.String, java.lang.String)
	 */
	@Override
	public MemoTypeInfo getMemoType(String memoTypeCode) throws TelusException {
		return memoTypeCache.get(memoTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMemoTypes(java.lang.String)
	 */
	@Override
	public MemoTypeInfo[] getMemoTypes() throws TelusException {
		List<MemoTypeInfo> result = new ArrayList<MemoTypeInfo>();
		for (MemoTypeInfo memoType : memoTypeCache.getAll()) {
			if (memoType.getManualInd().equals("Y")) {
				result.add(memoType);
			}
		}
		return result.toArray(new MemoTypeInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationMessageTemplate(int, java.lang.String)
	 */
	@Override
	public NotificationMessageTemplateInfo getNotificationMessageTemplate(int notificationTypeCode) throws TelusException {
		return notificationMessageTemplateCache.get(Integer.toString(notificationTypeCode));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationMessageTemplate(int, java.lang.String)
	 */
	@Override
	public NotificationMessageTemplateInfo getNotificationMessageTemplate(String code) throws TelusException {
		return notificationMessageTemplateCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationMessageTemplates(java.lang.String)
	 */
	@Override
	public NotificationMessageTemplateInfo[] getNotificationMessageTemplates() throws TelusException {
		return notificationMessageTemplateCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationType(int, java.lang.String)
	 */
	@Override
	public NotificationTypeInfo getNotificationType(int notificationTypeCode) throws TelusException {
		return notificationTypeCache.get(Integer.toString(notificationTypeCode));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationTypes(java.lang.String)
	 */
	@Override
	public NotificationTypeInfo[] getNotificationTypes() throws TelusException {
		return notificationTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNumberGroupByPhoneNumberAndProductType(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPhoneNumberAndProductType(final String phoneNumber, final String productType) throws TelusException {
		return referenceDataHelper.retrieveNumberGroupByPhoneNumberProductType(phoneNumber, productType);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getAvailableNumberGroups(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] getAvailableNumberGroups(final String accountType, final String accountSubType, final String productType, final String equipmentType, final String marketAreaCode)
			throws TelusException {

		return referenceDataHelper.retrieveNumberGroupList(StringUtil.toChar(accountType), StringUtil.toChar(accountSubType), productType, equipmentType, marketAreaCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNumberGroupByPhoneNumberAndProductType(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPortedInPhoneNumberAndProductType(final String phoneNumber, final String productType) throws TelusException {
		return referenceDataHelper.retrieveNumberGroupByPortedInPhoneNumberProductType(phoneNumber, productType);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentSourceTypes(java.lang.String)
	 */
	@Override
	public PaymentSourceTypeInfo[] getPaymentSourceTypes() throws TelusException {
		return paymentSourceTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPoolingGroup(java.lang.String, java.lang.String)
	 */
	@Override
	public PoolingGroupInfo getPoolingGroup(String poolingGroupCode) throws TelusException {
		return poolingGroupCache.get(poolingGroupCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPoolingGroups(java.lang.String)
	 */
	@Override
	public PoolingGroupInfo[] getPoolingGroups() throws TelusException {
		return poolingGroupCache.getAll();
	}

	private PricePlanBo getPricePlanBo(final String pricePlanCode) {
		return pricePlanSummaryCache.get(Info.padTo(pricePlanCode, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public PricePlanInfo getPricePlan(final String productType, final String pricePlanCode, final String equipmentType, final String provinceCode, final String accountType,
			final String accountSubType, final int brandId) throws TelusException {

		if (pricePlanCode == null || pricePlanCode.trim().length() == 0) {
			throw new TelusException("20201", "Price Plan Code cannot be empty.");
		}

		PricePlanBo.PricePlanCtrieria key = new PricePlanBo.PricePlanCtrieria(pricePlanCode, equipmentType, provinceCode, StringUtil.toChar(accountType), StringUtil.toChar(accountSubType), brandId);

		PricePlanBo pricePlanBo = pricePlanCache.get(key);

		return pricePlanBo == null ? null : pricePlanBo.getDelegate();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlans(java.lang.String[], java.lang.String)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String[] pricePlanCodes) throws TelusException {
		List<PricePlanInfo> result = new ArrayList<PricePlanInfo>();
		for (String pricePlanCode : pricePlanCodes) {
			PricePlanInfo pricePlan = getPricePlan(pricePlanCode);
			if (pricePlan != null) {
				result.add(pricePlan);
			}
		}
		return result.toArray(new PricePlanInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlan(java.lang.String, java.lang.String)
	 */
	@Override
	public PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException {
		if (pricePlanCode == null || pricePlanCode.trim().length() == 0) {
			throw new TelusException("20201", "Price Plan Code cannot be empty.");
		}
		PricePlanBo pricePlanBo = getPricePlanBo(pricePlanCode);
		return pricePlanBo == null ? null : pricePlanBo.getDelegate();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPricePlans(com.telus.api.reference.PricePlanSelectionCriteria)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException {

		Assert.notNull(criteriaInfo.getProductType(), "Mandatory 'productType' field must not be null");
		Assert.notNull(criteriaInfo.getProvinceCode(), "Mandatory 'provinceCode' field must not be null");
		Assert.notNull(criteriaInfo.getAccountType(), "Mandatory 'accountType' field must not be null");
		Assert.notNull(criteriaInfo.getAccountSubType(), "Mandatory 'accountSubType' field must not be null");

		logger.debug("PricePlanSelectionCriteria: " + criteriaInfo.toString());
		
		String[] offerPricePlanGroupCodes = null;
		OfferPricePlanSetInfo offerPricePlanSet = null;
		
		// Retrieve the offer price plans from WSOIS
		if (criteriaInfo.getOfferCriteria() != null && StringUtils.isNotBlank(criteriaInfo.getOfferCriteria().getSystemId())) {
			offerPricePlanSet = referenceDataHelper.retrieveOfferPricePlanInfo(criteriaInfo);
			if (offerPricePlanSet != null && CollectionUtils.isEmpty(offerPricePlanSet.getOfferPricePlanGroupCodeList()) == false) {
				offerPricePlanGroupCodes = offerPricePlanSet.getOfferPricePlanGroupCodeList().toArray(new String[0]);
			}
		}

		// Retrieve the price plans from KB DB
		PricePlanInfo[] kbpricePlans = referenceDataHelper.retrievePricePlanList(criteriaInfo,offerPricePlanGroupCodes);
		
		// filter the kb price plans based on TOM offer
		PricePlanInfo[] result = offerPricePlanSet != null ? PricePlanBo
				.filterKBPricePlanListByTOMOffer(offerPricePlanSet,kbpricePlans) : kbpricePlans;

		if (criteriaInfo.getIncludeFeaturesAndServices() != null && criteriaInfo.getIncludeFeaturesAndServices()) {
			result = PricePlanBo.undecorate(PricePlanBo.decorate(result, this));
		}

		return result;
	}

	@Override
	public PricePlanInfo[] getPricePlans(final String productType, final String equipmentType, final String provinceCode, final String accountType, final String accountSubType, final int brandId,
			final long[] productPromoTypes, final boolean initialActivation, final boolean currentPricePlansOnly, final boolean availableForActivationOnly, final int term, String activityCode,
			final String activityReasonCode, final String networkType, final String seatTypeCode) throws TelusException {

		PricePlanInfo[] result = null;

		if (ArrayUtils.isEmpty(productPromoTypes)) {
			result = referenceDataHelper.retrievePricePlanList(productType, equipmentType, provinceCode, StringUtil.toChar(accountType), StringUtil.toChar(accountSubType), brandId,
					currentPricePlansOnly, availableForActivationOnly, term, networkType, seatTypeCode);
		} else {
			result = referenceDataHelper.retrievePricePlanList(productType, equipmentType, provinceCode, StringUtil.toChar(accountType), StringUtil.toChar(accountSubType), brandId, productPromoTypes,
					initialActivation, currentPricePlansOnly, networkType, seatTypeCode);
		}

		return PricePlanBo.undecorate(PricePlanBo.decorate(result, this));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(final String productType, final String equipmentType, final String provinceCode, final String accountType, final String accountSubType, final int brandId)
			throws TelusException {

		PricePlanInfo[] result = referenceDataHelper.retrievePricePlanList(productType, equipmentType, provinceCode, StringUtil.toChar(accountType), StringUtil.toChar(accountSubType), brandId);

		return PricePlanBo.undecorate(PricePlanBo.decorate(result, this));
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, int term) throws TelusException {

		List<PricePlanInfo> result = new ArrayList<PricePlanInfo>();
		PricePlanInfo[] pricePlans = getPricePlans(productType, equipmentType, provinceCode, accountType, accountSubType, brandId);

		for (PricePlanInfo pricePlan : pricePlans) {
			if (pricePlan.getTerm() == term) {
				result.add(pricePlan);
			}
		}

		return result.toArray(new PricePlanInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPricePlanTerm(java.lang.String)
	 */
	@Override
	public PricePlanTermInfo getPricePlanTerm(String pricePlanCode) throws TelusException {
		return pricePlanTermCache.get(pricePlanCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRegularService(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo getRegularService(String serviceCode) throws TelusException {
		return regularServiceCache.get(Info.padTo(serviceCode, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRegularServices(java.lang.String[], java.lang.String)
	 */
	@Override
	public ServiceInfo[] getRegularServices(String[] serviceCodes) throws TelusException {
		return regularServiceCache.get(ArrayUtil.padTo(serviceCodes, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSegmentation(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SegmentationInfo getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusException {
		final String wildcard = "*";

		SegmentationInfo segmentation = segmentationCache.get(brandId + accountTypeCode + provinceCode);

		if (segmentation == null) {
			segmentation = segmentationCache.get(brandId + accountTypeCode + wildcard);
		}
		if (segmentation == null) {
			segmentation = segmentationCache.get(brandId + wildcard + provinceCode);
		}
		if (segmentation == null) {
			segmentation = segmentationCache.get(brandId + wildcard + wildcard);
		}
		return segmentation;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSegmentations(java.lang.String)
	 */
	@Override
	public SegmentationInfo[] getSegmentations() throws TelusException {
		return segmentationCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServiceExclusionGroups(java.lang.String)
	 */
	@Override
	public ServiceExclusionGroupsInfo[] getServiceExclusionGroups() throws TelusException {
		return serviceExclusionGroupsCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServicePolicy(java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo getServicePolicy(String servicePolicyCode) throws TelusException {
		return servicePolicyCache.get(servicePolicyCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServicesByFeatureCategory(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public ServiceInfo[] getServicesByFeatureCategory(final String featureCategory, final String productType, final boolean current) throws TelusException {
		final String key = CacheKeyBuilder.createComplexKey(featureCategory, productType, current);

		return regularServiceGroupCache.get(key);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getWPSService(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo getWPSService(String serviceCode) throws TelusException {
		ServiceInfo info = prepaidServiceCache.get(Info.padTo(serviceCode, ' ', 9));
		populateExtraInfoFromMappedKBSoc(info);
		return info;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getWPSServices(java.lang.String)
	 */
	@Override
	public ServiceInfo[] getWPSServices() throws TelusException {
		ServiceInfo[] infos = prepaidServiceCache.getAll();
		for (ServiceInfo info : infos) {
			populateExtraInfoFromMappedKBSoc(info);
		}
		return infos;
	}

	//refectoring: extra logic that is moved from TMReferenceDataManager.decorateWPS()
	private void populateExtraInfoFromMappedKBSoc(ServiceInfo info) throws TelusException {

		if (info == null)
			return;

		String kbSoc = info.getWPSMappedKBSocCode();
		if (kbSoc != null && (info.getCategoryCodes() == null || info.getCategoryCodes().length == 0) //this serve a indicator of whether or not the info has be populated. 
		) {

			Service kbMappedPrepaidService = getRegularService(kbSoc);

			if (kbMappedPrepaidService != null) {
				int callingCircleSize = 0;
				String switchCode = "";
				String categoryCode = "";
				RatedFeature[] features = kbMappedPrepaidService.getFeatures();
				for (RatedFeature feature : features) {
					if (FeatureInfo.CATEGORY_CODE_CALLING_CIRCLE.equals(feature.getCategoryCode())) {
						callingCircleSize = feature.getCallingCircleSize();
						switchCode = feature.getSwitchCode();
						categoryCode = feature.getCategoryCode();
						break;
					}
					if (FeatureInfo.CATEGORY_CODE_CALL_HOME_FREE.equals(feature.getCategoryCode())) {
						callingCircleSize = 1;
						switchCode = feature.getSwitchCode();
						categoryCode = feature.getCategoryCode();
						break;
					}
				}

				if (callingCircleSize > 0) { //this calling circle or call home free SOC
					info.setCategoryCodes(new String[] { categoryCode });

					RatedFeatureInfo[] prepaidFeatures = info.getFeatures0();
					for (int i = 0; i < prepaidFeatures.length; i++) {

						prepaidFeatures[i].setCallingCircleSize(callingCircleSize);
						prepaidFeatures[i].setCategoryCode(categoryCode);
						prepaidFeatures[i].setParameterRequired(true);
						prepaidFeatures[i].setPrepaidCallingCircle(true);
						prepaidFeatures[i].setSwitchCode(switchCode);
					}
				}
				//set category codes from feature's category code for non calling circle feature
				else {
					Feature feature = getFeatureCategory(info.getCode().trim());
					if (feature != null && feature.getCategoryCode() != null) {
						info.setCategoryCodes(new String[] { feature.getCategoryCode() });
					}
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getCountries()
	 */
	@Override
	public CountryInfo[] getCountries() throws TelusException {
		return countryCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getDealerSalesRep(java.lang.String, java.lang.String)
	 */
	@Override
	public SalesRepInfo getDealerSalesRep(final String dealerCode, final String salesRepCode) throws TelusException {
		final String key = CacheKeyBuilder.createComplexKey(dealerCode, salesRepCode);

		return salesRepCache.get(key, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveDealerSalesRepByCode(dealerCode, salesRepCode);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getLanguages()
	 */
	@Override
	public LanguageInfo[] getLanguages() throws TelusException {
		return languageCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentMethods()
	 */
	@Override
	public PaymentMethodInfo[] getPaymentMethods() throws TelusException {
		return paymentMethodCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidRechargeDenominations()
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations() throws TelusException {
		return prepaidRechargeDenominationCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvinces()
	 */
	@Override
	public ProvinceInfo[] getProvinces() throws TelusException {
		return provinceCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getStates()
	 */
	@Override
	public StateInfo[] getStates() throws TelusException {
		return stateCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getUnitTypes()
	 */
	@Override
	public UnitTypeInfo[] getUnitTypes() throws TelusException {
		return unitTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllFollowUpTypes()
	 */
	@Override
	public FollowUpTypeInfo[] getAllFollowUpTypes() throws TelusException {
		return followUpTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllLanguages()
	 */
	@Override
	public LanguageInfo[] getAllLanguages() throws TelusException {
		return languageAllCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllMemoTypes()
	 */
	@Override
	public MemoTypeInfo[] getAllMemoTypes() throws TelusException {
		return memoTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllProvinces()
	 */
	@Override
	public ProvinceInfo[] getAllProvinces() throws TelusException {
		return provinceAllCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllPrepaidRates()
	 */
	@Override
	public PrepaidRateProfileInfo[] getAllPrepaidRates() throws TelusException {
		return prepaidRateProfileCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllTitles()
	 */
	@Override
	public TitleInfo[] getAllTitles() throws TelusException {
		return titleAllCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCountries(boolean)
	 */
	@Override
	public CountryInfo[] getCountries(boolean includeForeign) throws TelusException {
		return includeForeign ? countryAllCache.getAll() : countryCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCountry(java.lang.String)
	 */
	@Override
	public CountryInfo getCountry(String code) throws TelusException {
		return countryAllCache.get(Info.padTo(code.toUpperCase(), ' ', 3));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCoverageRegions()
	 */
	@Override
	public CoverageRegionInfo[] getCoverageRegions() throws TelusException {
		return coverageRegionCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCoverageRegion(java.lang.String)
	 */
	@Override
	public CoverageRegionInfo getCoverageRegion(String code) throws TelusException {
		return coverageRegionCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardPaymentTypes()
	 */
	@Override
	public CreditCardPaymentTypeInfo[] getCreditCardPaymentTypes() throws TelusException {
		return creditCardPaymentTypeCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardPaymentType(java.lang.String)
	 */
	@Override
	public CreditCardPaymentTypeInfo getCreditCardPaymentType(String code) throws TelusException {
		return creditCardPaymentTypeCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardTypes()
	 */
	@Override
	public CreditCardTypeInfo[] getCreditCardTypes() throws TelusException {
		return creditCardTypeCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardType(java.lang.String)
	 */
	@Override
	public CreditCardTypeInfo getCreditCardType(String code) throws TelusException {
		return creditCardTypeCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditMessages()
	 */
	@Override
	public CreditMessageInfo[] getCreditMessages() throws TelusException {
		return creditMessageCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditMessage(java.lang.String)
	 */
	@Override
	public CreditMessageInfo getCreditMessage(String code) throws TelusException {
		return creditMessageCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDepartments()
	 */
	@Override
	public DepartmentInfo[] getDepartments() throws TelusException {
		return departmentCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDepartment(java.lang.String)
	 */
	@Override
	public DepartmentInfo getDepartment(String code) throws TelusException {
		return departmentCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEncodingFormats()
	 */
	@Override
	public EncodingFormatInfo[] getEncodingFormats() throws TelusException {
		return encodingFormatCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEncodingFormat(java.lang.String)
	 */
	@Override
	public EncodingFormatInfo getEncodingFormat(String code) throws TelusException {
		return encodingFormatCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpTypes()
	 */
	@Override
	public FollowUpTypeInfo[] getFollowUpTypes() throws TelusException {
		List<FollowUpTypeInfo> result = new ArrayList<FollowUpTypeInfo>();
		for (FollowUpTypeInfo followUpType : followUpTypeCache.getAll()) {
			if (followUpType.getManualOpenInd().equals("Y")) {
				result.add(followUpType);
			}
		}
		return result.toArray(new FollowUpTypeInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceSuppressionLevels()
	 */
	@Override
	public InvoiceSuppressionLevelInfo[] getInvoiceSuppressionLevels() throws TelusException {
		return invoiceSuppressionLevelCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceSuppressionLevel(java.lang.String)
	 */
	@Override
	public InvoiceSuppressionLevelInfo getInvoiceSuppressionLevel(String code) throws TelusException {
		return invoiceSuppressionLevelCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNetworks()
	 */
	@Override
	public NetworkInfo[] getNetworks() throws TelusException {
		return networkCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentMethodTypes()
	 */
	@Override
	public PaymentMethodTypeInfo[] getPaymentMethodTypes() throws TelusException {
		return paymentMethodTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPhoneTypes()
	 */
	@Override
	public PhoneTypeInfo[] getPhoneTypes() throws TelusException {
		return phoneTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvinces(java.lang.String)
	 */
	@Override
	public ProvinceInfo[] getProvinces(final String countryCode) throws TelusException {
		return provinceCountryCache.get(countryCode, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveProvinces(countryCode);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServicePeriodTypes()
	 */
	@Override
	public ServicePeriodTypeInfo[] getServicePeriodTypes() throws TelusException {
		return servicePeriodTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTitles()
	 */
	@Override
	public TitleInfo[] getTitles() throws TelusException {
		return titleCache.getAll();
	}

	// Defect PROD00178088 Start
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityType(java.lang.String)
	 */
	@Override
	public ActivityTypeInfo getActivityType(String activityTypeCode) throws TelusException {
		ActivityTypeInfo[] result = activityTypeCache.getAllByCode(activityTypeCode);
		return result == null || result.length == 0 ? null : result[0];
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityTypes(java.lang.String)
	 */
	@Override
	public ActivityTypeInfo[] getActivityTypes(String activityTypeCode) throws TelusException {
		return activityTypeCache.getAllByCode(activityTypeCode);
	}
	// Defect PROD00178088 End

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityTypes()
	 */
	@Override
	public ActivityTypeInfo[] getActivityTypes() throws TelusException {
		return activityTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAddressType(java.lang.String)
	 */
	@Override
	public AddressTypeInfo getAddressType(String addressTypeCode) throws TelusException {
		return addressTypeCache.get(addressTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAddressTypes()
	 */
	@Override
	public AddressTypeInfo[] getAddressTypes() throws TelusException {
		return addressTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAdjustmentReason(java.lang.String)
	 */
	@Override
	public AdjustmentReasonInfo getAdjustmentReason(String reasonCode) throws TelusException {
		return adjustmentReasonCache.get(Info.padTo(reasonCode, ' ', 6));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAdjustmentReasons()
	 */
	@Override
	public AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException {
		return adjustmentReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAmountBarCodes()
	 */
	@Override
	public AmountBarCodeInfo[] getAmountBarCodes() throws TelusException {
		return amountBarCodeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getApplicationSummary(java.lang.String)
	 */
	@Override
	public ApplicationSummaryInfo getApplicationSummary(String applicationCode) throws TelusException {
		return applicationSummaryCache.get(applicationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAudienceType(java.lang.String)
	 */
	@Override
	public AudienceTypeInfo getAudienceType(String audienceTypeCode) throws TelusException {
		return audienceTypeCache.get(audienceTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillCycle(java.lang.String)
	 */
	@Override
	public BillCycleInfo getBillCycle(String code) throws TelusException {
		return billCycleCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillCycles()
	 */
	@Override
	public BillCycleInfo[] getBillCycles() throws TelusException {
		return billCycleCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillHoldRedirectDestination(java.lang.String)
	 */
	@Override
	public BillHoldRedirectDestinationInfo getBillHoldRedirectDestination(String destinationCode) throws TelusException {
		return billHoldRedirectDestinationCache.get(destinationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillHoldRedirectDestinations()
	 */
	@Override
	public BillHoldRedirectDestinationInfo[] getBillHoldRedirectDestinations() throws TelusException {
		return billHoldRedirectDestinationCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBusinessRole(java.lang.String)
	 */
	@Override
	public BusinessRoleInfo getBusinessRole(String code) throws TelusException {
		return businessRoleCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBusinessRoles()
	 */
	@Override
	public BusinessRoleInfo[] getBusinessRoles() throws TelusException {
		return businessRoleCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientConsentIndicator(java.lang.String)
	 */
	@Override
	public ClientConsentIndicatorInfo getClientConsentIndicator(String code) throws TelusException {
		return clientConsentIndicatorCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientConsentIndicators()
	 */
	@Override
	public ClientConsentIndicatorInfo[] getClientConsentIndicators() throws TelusException {
		return clientConsentIndicatorCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientStateReason(java.lang.String)
	 */
	@Override
	public ClientStateReasonInfo getClientStateReason(String code) throws TelusException {
		return clientStateReasonCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientStateReasons()
	 */
	@Override
	public ClientStateReasonInfo[] getClientStateReasons() throws TelusException {
		return clientStateReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivities()
	 */
	@Override
	public CollectionActivityInfo[] getCollectionActivities() throws TelusException {
		return collectionActivityCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivity(java.lang.String, int)
	 */
	@Override
	public CollectionActivityInfo getCollectionActivity(String pathCode, int stepNumber) throws TelusException {
		for (CollectionPathDetailsInfo path : collectionPathDetailsCache.getAll()) {
			if (path.getPathCode().equals(pathCode) && path.getStepNumber() == stepNumber) {
				return getCollectionActivity(path.getActivityCode());
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivity(java.lang.String)
	 */
	@Override
	public CollectionActivityInfo getCollectionActivity(String code) throws TelusException {
		return collectionActivityCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionAgencies()
	 */
	@Override
	public CollectionAgencyInfo[] getCollectionAgencies() throws TelusException {
		return collectionAgencyCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionAgency(java.lang.String)
	 */
	@Override
	public CollectionAgencyInfo getCollectionAgency(String code) throws TelusException {
		return collectionAgencyCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionPathDetails(java.lang.String)
	 */
	@Override
	public CollectionPathDetailsInfo[] getCollectionPathDetails(String pathCode) throws TelusException {
		List<CollectionPathDetailsInfo> result = new ArrayList<CollectionPathDetailsInfo>();
		for (CollectionPathDetailsInfo path : collectionPathDetailsCache.getAll()) {
			if (path.getPathCode().equals(pathCode)) {
				result.add(path);
			}
		}
		return result.toArray(new CollectionPathDetailsInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionPaths()
	 */
	@Override
	public String[] getCollectionPaths() throws TelusException {
		Set<String> result = new LinkedHashSet<String>();
		for (CollectionPathDetailsInfo path : collectionPathDetailsCache.getAll()) {
			result.add(path.getPathCode());
		}
		return result.toArray(new String[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionState(java.lang.String)
	 */
	@Override
	public CollectionStateInfo getCollectionState(String code) throws TelusException {
		return collectionStateCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStates()
	 */
	@Override
	public CollectionStateInfo[] getCollectionStates() throws TelusException {
		return collectionStateCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStepApproval(java.lang.String)
	 */
	@Override
	public CollectionStepApprovalInfo getCollectionStepApproval(String code) throws TelusException {
		return collectionStepApprovalCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStepApprovals()
	 */
	@Override
	public CollectionStepApprovalInfo[] getCollectionStepApprovals() throws TelusException {
		return collectionStepApprovalCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCorporateAccountRep(java.lang.String)
	 */
	@Override
	public CorporateAccountRepInfo getCorporateAccountRep(String code) throws TelusException {
		return corporateAccountRepCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCorporateAccountReps()
	 */
	@Override
	public CorporateAccountRepInfo[] getCorporateAccountReps() throws TelusException {
		return corporateAccountRepCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCheckDepositChangeReason(java.lang.String)
	 */
	@Override
	public CreditCheckDepositChangeReasonInfo getCreditCheckDepositChangeReason(String code) throws TelusException {
		return creditCheckDepositChangeReasonCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCheckDepositChangeReasons()
	 */
	@Override
	public CreditCheckDepositChangeReasonInfo[] getCreditCheckDepositChangeReasons() throws TelusException {
		return creditCheckDepositChangeReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditClasses()
	 */
	@Override
	public CreditClassInfo[] getCreditClasses() throws TelusException {
		return creditClassCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDiscountPlan(java.lang.String)
	 */
	@Override
	public DiscountPlanInfo getDiscountPlan(String discountCode) throws TelusException {
		return discountPlanCache.get(Info.padTo(discountCode, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDiscountPlans(boolean)
	 */
	@Override
	public DiscountPlanInfo[] getDiscountPlans(boolean current) throws TelusException {
		if (!current) {
			return discountPlanCache.getAll();
		} else {
			List<DiscountPlanInfo> result = new ArrayList<DiscountPlanInfo>();
			Date now = getLogicalDate();
			for (DiscountPlanInfo plan : discountPlanCache.getAll()) {
				if (Info.intersects(now, plan.getEffectiveDate(), plan.getExpiration())) {
					result.add(plan);
				}
			}
			return result.toArray(new DiscountPlanInfo[result.size()]);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentPossession(java.lang.String)
	 */
	@Override
	public EquipmentPossessionInfo getEquipmentPossession(String code) throws TelusException {
		return equipmentPossessionCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentPossessions()
	 */
	@Override
	public EquipmentPossessionInfo[] getEquipmentPossessions() throws TelusException {
		return equipmentPossessionCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentProductType(java.lang.String)
	 */
	@Override
	public EquipmentProductTypeInfo getEquipmentProductType(String code) throws TelusException {
		return equipmentProductTypeCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentProductTypes()
	 */
	@Override
	public EquipmentProductTypeInfo[] getEquipmentProductTypes() throws TelusException {
		return equipmentProductTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentStatus(long, long)
	 */
	@Override
	public EquipmentStatusInfo getEquipmentStatus(long statusId, long statusTypeId) throws TelusException {
		return equipmentStatusCache.get(String.valueOf(statusTypeId) + "^" + String.valueOf(statusId));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentStatuses()
	 */
	@Override
	public EquipmentStatusInfo[] getEquipmentStatuses() throws TelusException {
		return equipmentStatusCache.getAll();
	}

	@Override
	public SwapRuleInfo[] getEquipmentSwapRules() throws TelusException {
		return equipmentSwapRulesCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentType(java.lang.String)
	 */
	@Override
	public EquipmentTypeInfo getEquipmentType(String equipTypeCode) throws TelusException {
		return equipmentTypeCache.get(equipTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] getEquipmentTypes() throws TelusException {
		return equipmentTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getExceptionReason(java.lang.String)
	 */
	@Override
	public ExceptionReasonInfo getExceptionReason(String code) throws TelusException {
		return exceptionReasonCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getExceptionReasons()
	 */
	@Override
	public ExceptionReasonInfo[] getExceptionReasons() throws TelusException {
		return exceptionReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeatureCategories()
	 */
	@Override
	public FeatureInfo[] getFeatureCategories() throws TelusException {
		return featureCategoryCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeatureCategory(java.lang.String)
	 */
	@Override
	public FeatureInfo getFeatureCategory(String code) throws TelusException {
		return featureCategoryCache.get(Info.padTo(code, ' ', 6));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverReason(java.lang.String)
	 */
	@Override
	public FeeWaiverReasonInfo getFeeWaiverReason(String reasonCode) throws TelusException {
		return feeWaiverReasonCache.get(reasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverReasons()
	 */
	@Override
	public FeeWaiverReasonInfo[] getFeeWaiverReasons() throws TelusException {
		return feeWaiverReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverType(java.lang.String)
	 */
	@Override
	public FeeWaiverTypeInfo getFeeWaiverType(String typeCode) throws TelusException {
		return feeWaiverTypeCache.get(typeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverTypes()
	 */
	@Override
	public FeeWaiverTypeInfo[] getFeeWaiverTypes() throws TelusException {
		return feeWaiverTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getDealerSalesRep(java.lang.String, java.lang.String)
	 */
	@Override
	public FleetInfo getFleetById(final int urbanID, final int fleetId) throws TelusException {
		final String key = CacheKeyBuilder.createComplexKey(urbanID, fleetId);

		return fleetCache.get(key, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveFleetByFleetIdentity(new FleetIdentityInfo(urbanID, fleetId));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFleetClass(java.lang.String)
	 */
	@Override
	public FleetClassInfo getFleetClass(String code) throws TelusException {
		return fleetClassCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFleetClasses()
	 */
	@Override
	public FleetClassInfo[] getFleetClasses() throws TelusException {
		return fleetClassCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpCloseReason(java.lang.String)
	 */
	@Override
	public FollowUpCloseReasonInfo getFollowUpCloseReason(String reasonCode) throws TelusException {
		return followUpCloseReasonCache.get(reasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpCloseReasons()
	 */
	@Override
	public FollowUpCloseReasonInfo[] getFollowUpCloseReasons() throws TelusException {
		return followUpCloseReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpType(java.lang.String)
	 */
	@Override
	public FollowUpTypeInfo getFollowUpType(String code) throws TelusException {
		return followUpTypeCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getGeneration(java.lang.String)
	 */
	@Override
	public GenerationInfo getGeneration(String generationCode) throws TelusException {
		return generationCache.get(generationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getGenerations()
	 */
	@Override
	public GenerationInfo[] getGenerations() throws TelusException {
		return generationCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceCallSortOrderType(java.lang.String)
	 */
	@Override
	public InvoiceCallSortOrderTypeInfo getInvoiceCallSortOrderType(String code) throws TelusException {
		return invoiceCallSortOrderTypeCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceCallSortOrderTypes()
	 */
	@Override
	public InvoiceCallSortOrderTypeInfo[] getInvoiceCallSortOrderTypes() throws TelusException {
		return invoiceCallSortOrderTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getKnowbilityOperator(java.lang.String)
	 */
	@Override
	public KnowbilityOperatorInfo getKnowbilityOperator(String code) throws TelusException {
		return knowbilityOperatorCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLanguage(java.lang.String)
	 */
	@Override
	public LanguageInfo getLanguage(String code) throws TelusException {
		return languageAllCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterCategories()
	 */
//	@Override
//	public LetterCategoryInfo[] getLetterCategories() throws TelusException {
//		return letterCategoryCache.getAll();
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterCategory(java.lang.String)
	 */
//	@Override
//	public LetterCategoryInfo getLetterCategory(String code) throws TelusException {
//		return letterCategoryCache.get(code);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterSubCategories(java.lang.String)
	 */
//	@Override
//	public LetterSubCategoryInfo[] getLetterSubCategories(String letterCategory) throws TelusException {
//		List<LetterSubCategoryInfo> result = new ArrayList<LetterSubCategoryInfo>();
//		for (LetterSubCategoryInfo subCategory : letterSubCategoryCache.getAll()) {
//			if (subCategory.getLetterCategory().equals(letterCategory)) {
//				result.add(subCategory);
//			}
//		}
//		return result.toArray(new LetterSubCategoryInfo[result.size()]);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterSubCategory(java.lang.String)
	 */
//	@Override
//	public LetterSubCategoryInfo getLetterSubCategory(String letterSubCategory) throws TelusException {
//		for (LetterSubCategoryInfo subCategory : letterSubCategoryCache.getAll()) {
//			if (subCategory.getLetterSubCategory().equals(letterSubCategory)) {
//				return subCategory;
//			}
//		}
//		return null;
//	}

//	@Override
//	public LetterSubCategoryInfo getLetterSubCategory(String letterCategory, String letterSubCategory) throws TelusException {
//		for (LetterSubCategoryInfo subCategory : letterSubCategoryCache.getAll()) {
//			if (subCategory.getLetterSubCategory().equals(letterSubCategory) && subCategory.getLetterCategory().equals(letterCategory)) {
//				return subCategory;
//			}
//		}
//		return null;
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLockReason(long)
	 */
	@Override
	public LockReasonInfo getLockReason(String code) throws TelusException {
		return lockReasonCache.get(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLockReasons()
	 */
	@Override
	public LockReasonInfo[] getLockReasons() throws TelusException {
		return lockReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getManualChargeType(java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getManualChargeType(String code) throws TelusException {
		return chargeTypeCache.get(Info.padTo(code, ' ', 6));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getManualChargeTypes()
	 */
	@Override
	public ChargeTypeInfo[] getManualChargeTypes() throws TelusException {
		return chargeTypeCache.getAll();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMinutePoolingContributorServices()
	 */
	@Override
	public ServiceInfo[] getMinutePoolingContributorServices() throws TelusException {
		return minutePoolingServiceCache.get("", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				String[] serviceCodes = referenceDataHelper.retrieveMinutePoolingContributorServiceCodes();
				return getRegularServices(serviceCodes);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getZeroMinutePoolingContributorServices()
	 */
	@Override
	public ServiceInfo[] getZeroMinutePoolingContributorServices() throws TelusException {
		return zeroMinutePoolingServiceCache.get("", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				String[] serviceCodes = referenceDataHelper.retrieveZeroMinutePoolingContributorServiceCodes();
				return getRegularServices(serviceCodes);
			}
		});
	}

	// Pavel's methods end

	// Andrew's methods begin

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMemoTypeCategories()
	 */
	@Override
	public MemoTypeCategoryInfo[] getMemoTypeCategories() throws TelusException {
		return memoTypeCategoryCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMemoTypeCategory(java.lang.String)
	 */
	@Override
	public MemoTypeCategoryInfo getMemoTypeCategory(String categoryCode) throws TelusException {
		return memoTypeCategoryCache.get(categoryCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMigrationTypes()
	 */
	@Override
	public MigrationTypeInfo[] getMigrationTypes() throws TelusException {
		return migrationTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getMigrationType(java.lang.String)
	 */
	
	
	@Override
	public MigrationTypeInfo getMigrationType(String migrationCode) throws TelusException {
		return migrationTypeCache.get(migrationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNetwork(java.lang.String)
	 */
	@Override
	public NetworkInfo getNetwork(String networkCode) throws TelusException {
		return networkCache.get(networkCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNetworkTypes()
	 */
	@Override
	public NetworkTypeInfo[] getNetworkTypes() throws TelusException {
		return networkTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNotificationType(java.lang.String)
	 */
	@Override
	public NotificationTypeInfo getNotificationType(String notificationCode) throws TelusException {
		return notificationTypeCache.get(notificationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNumberRanges()
	 */
	@Override
	public NumberRangeInfo[] getNumberRanges() throws TelusException {
		return numberRangeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getNumberRange(java.lang.String)
	 */
	@Override
	public NumberRangeInfo getNumberRange(String npaNxx) throws TelusException {
		return numberRangeCache.get(npaNxx);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPagerEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] getPagerEquipmentTypes() throws TelusException {
		return pagerEquipmentTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPagerEquipmentType(java.lang.String)
	 */
	@Override
	public EquipmentTypeInfo getPagerEquipmentType(String equipmentCode) throws TelusException {
		return pagerEquipmentTypeCache.get(equipmentCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPagerFrequencies()
	 */
	@Override
	public PagerFrequencyInfo[] getPagerFrequencies() throws TelusException {
		return pagerFrequencyCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPagerFrequency()
	 */
	@Override
	public PagerFrequencyInfo getPagerFrequency(String frequencyCode) throws TelusException {
		return pagerFrequencyCache.get(frequencyCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentMethod(java.lang.String)
	 */
	@Override
	public PaymentMethodInfo getPaymentMethod(String paymentMethodCode) throws TelusException {
		return paymentMethodCache.get(paymentMethodCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentMethodType(java.lang.String)
	 */
	@Override
	public PaymentMethodTypeInfo getPaymentMethodType(String paymentMethodTypeCode) throws TelusException {
		return paymentMethodTypeCache.get(paymentMethodTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentSourceType(java.lang.String)
	 */
	@Override
	public PaymentSourceTypeInfo getPaymentSourceType(String paymentSourceTypeCode) throws TelusException {
		return paymentSourceTypeCache.get(paymentSourceTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentTransferReasons()
	 */
	@Override
	public PaymentTransferReasonInfo[] getPaymentTransferReasons() throws TelusException {
		return paymentTransferReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPaymentTransferReason(java.lang.String)
	 */
	@Override
	public PaymentTransferReasonInfo getPaymentTransferReason(String transferReasonCode) throws TelusException {
		return paymentTransferReasonCache.get(transferReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPhoneType(java.lang.String)
	 */
	@Override
	public PhoneTypeInfo getPhoneType(String phoneTypeCode) throws TelusException {
		return phoneTypeCache.get(phoneTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidAdjustmentReason()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidAdjustmentReason() throws TelusException {
		return prepaidAdjustmentReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidAdjustmentReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidAdjustmentReason(String adjustmentReasonCode) throws TelusException {
		return prepaidAdjustmentReasonCache.get(adjustmentReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidEventTypes()
	 */
	@Override
	public PrepaidEventTypeInfo[] getPrepaidEventTypes() throws TelusException {
		return prepaidEventTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidEventType(java.lang.String)
	 */
	@Override
	public PrepaidEventTypeInfo getPrepaidEventType(String eventTypeCode) throws TelusException {
		return prepaidEventTypeCache.get(eventTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidFeatureAddWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidFeatureAddWaiveReasons() throws TelusException {
		return prepaidFeatureAddWaiveReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidFeatureAddWaiveReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidFeatureAddWaiveReason(String featureAddWaiveReasonCode) throws TelusException {
		return prepaidFeatureAddWaiveReasonCache.get(featureAddWaiveReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidManualAdjustmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidManualAdjustmentReasons() throws TelusException {
		return prepaidManualAdjustmentReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidManualAdjustmentReasons(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidManualAdjustmentReason(String adjustmentReasonCode) throws TelusException {
		return prepaidManualAdjustmentReasonCache.get(adjustmentReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidRechargeDenominations(java.lang.String)
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations(String rechargeType) throws TelusException {

		if (!ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT.equals(rechargeType) && !ReferenceDataManager.RECHARGE_TYPE_AUTO_TOP_UP.equals(rechargeType)
				&& !ReferenceDataManager.RECHARGE_TYPE_ONETIME_TOP_UP.equals(rechargeType) && !ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT_FS.equals(rechargeType)) {
			throw new TelusException(rechargeType + " is an unknown recharge type");
		}
		PrepaidRechargeDenominationInfo[] prepaidRechargeDenomination = prepaidRechargeDenominationCache.getAll();
		List<PrepaidRechargeDenominationInfo> list = new ArrayList<PrepaidRechargeDenominationInfo>();
		for (int i = 0; i < prepaidRechargeDenomination.length; i++) {
			PrepaidRechargeDenominationInfo preRecDenom = prepaidRechargeDenomination[i];
			if (rechargeType.equals(preRecDenom.getRechargeType())) {
				list.add(preRecDenom);
			}
		}
		return list.toArray(new PrepaidRechargeDenominationInfo[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidTopUpWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidTopUpWaiveReasons() throws TelusException {
		return prepaidTopUpWaiveReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPrepaidTopUpWaiveReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidTopUpWaiveReason(String topUpWaiveCode) throws TelusException {
		return prepaidTopUpWaiveReasonCache.get(topUpWaiveCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataPDSFacade#getPrepaidDeviceDirectFulfillmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidDeviceDirectFulfillmentReasons() throws TelusException {
		return prepaidDeviceDirectFulfillmentReasonCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataPDSFacade#getPrepaidDeviceDirectFulfillmentReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidDeviceDirectFulfillmentReason(String deviceDirectFulfillmentReasonCode) throws TelusException {
		return prepaidDeviceDirectFulfillmentReasonCache.get(deviceDirectFulfillmentReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProductTypes()
	 */
	@Override
	public ProductTypeInfo[] getProductTypes() throws TelusException {
		return productTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProductType(java.lang.String)
	 */
	@Override
	public ProductTypeInfo getProductType(String productTypeCode) throws TelusException {
		return productTypeCache.get(productTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getPromoTerm(java.lang.String)
	 */
	@Override
	public PromoTermInfo getPromoTerm(String promoCode) throws TelusException {
		return referenceDataHelper.retrievePromoTerm(promoCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvince(java.lang.String)
	 */
	@Override
	public ProvinceInfo getProvince(String provinceCode) throws TelusException {
		ProvinceInfo province = null;

		if (Province.PROVINCE_NL.equals(provinceCode)) {
			provinceCode = "NF";
		}
		province = provinceCache.get(provinceCode);
		if (province == null) {
			province = (provinceAllCache.get(provinceCode)); //Search through registered provinces from other countries
		}
		return province;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvince(java.lang.String, java.lang.String)
	 */
	@Override
	public ProvinceInfo getProvince(String countryCode, String provinceCode) throws TelusException {
		ProvinceInfo province = null;

		if ("CAN".equals(countryCode)) {
			if (Province.PROVINCE_NL.equals(provinceCode)) {
				provinceCode = "NF";
			}
			province = provinceCache.get(provinceCode);
		}

		return province;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningPlatformTypes()
	 */
	@Override
	public ProvisioningPlatformTypeInfo[] getProvisioningPlatformTypes() throws TelusException {
		return provisioningPlatformTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningPlatformType(java.lang.String)
	 */
	@Override
	public ProvisioningPlatformTypeInfo getProvisioningPlatformType(String provisioningPlatformId) throws TelusException {
		return provisioningPlatformTypeCache.get(provisioningPlatformId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningTransactionStatuses()
	 */
	@Override
	public ProvisioningTransactionStatusInfo[] getProvisioningTransactionStatuses() throws TelusException {
		return provisioningTransactionStatusCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningTransactionStatus(java.lang.String)
	 */
	@Override
	public ProvisioningTransactionStatusInfo getProvisioningTransactionStatus(String txStatusCode) throws TelusException {
		return provisioningTransactionStatusCache.get(txStatusCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningTransactionTypes()
	 */
	@Override
	public ProvisioningTransactionTypeInfo[] getProvisioningTransactionTypes() throws TelusException {
		return provisioningTransactionTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getProvisioningTransactionType(java.lang.String)
	 */
	@Override
	public ProvisioningTransactionTypeInfo getProvisioningTransactionType(String txTypeCode) throws TelusException {
		return provisioningTransactionTypeCache.get(txTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRegularServices()
	 */
	@Override
	public ServiceInfo[] getRegularServices() throws TelusException {
		return regularServiceCache.getAll();
	}

	@Override
	public ServiceInfo retrieveRegularService(String serviceCode) throws TelusException {
		ServiceInfo[] infos = getRegularServices();
		for (ServiceInfo info : infos) {
			if (info.getCode().equals(serviceCode)) {
				return info;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRoamingCapability()
	 */
	@Override
	public HandsetRoamingCapabilityInfo[] getRoamingCapability() throws TelusException {
		return roamingCapabilityCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRoutes()
	 */
	@Override
	public RouteInfo[] getRoutes() throws TelusException {
		return routeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRoute(java.lang.String, java.lang.String)
	 */
	@Override
	public RouteInfo getRoute(String switch_id, String route_id) throws TelusException {
		return routeCache.get((switch_id == null ? switch_id : switch_id.trim()) + "_" + (route_id == null ? route_id : route_id.trim()));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRuralDeliveryTypes()
	 */
	@Override
	public RuralDeliveryTypeInfo[] getRuralDeliveryTypes() throws TelusException {
		return ruralDeliveryTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRuralDeliveryType(java.lang.String)
	 */
	@Override
	public RuralDeliveryTypeInfo getRuralDeliveryType(String deliveryTypeCode) throws TelusException {
		return ruralDeliveryTypeCache.get(deliveryTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRuralTypes()
	 */
	@Override
	public RuralTypeInfo[] getRuralTypes() throws TelusException {
		return ruralTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getRuralType(java.lang.String)
	 */
	@Override
	public RuralTypeInfo getRuralType(String ruralTypeCode) throws TelusException {
		return ruralTypeCache.get(ruralTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSegmentation(java.lang.String)
	 */
	@Override
	public SegmentationInfo getSegmentation(String segmentationCode) throws TelusException {
		return segmentationCache.get(segmentationCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServiceExclusionGroups(java.lang.String)
	 */
	@Override
	public ServiceExclusionGroupsInfo getServiceExclusionGroups(String serviceExclusionGroupCode) throws TelusException {
		return serviceExclusionGroupsCache.get(Info.padTo(serviceExclusionGroupCode, ' ', 9));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServicePeriodType(java.lang.String)
	 */
	@Override
	public ServicePeriodTypeInfo getServicePeriodType(String servicePeriodTypeCode) throws TelusException {
		return servicePeriodTypeCache.get(servicePeriodTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getServiceUsageInfo(java.lang.String)
	 */
	@Override
	public ServiceUsageInfo getServiceUsage(final String serviceCode) throws TelusException {
		return serviceUsageCache.get(serviceCode, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveServiceUsageInfo(Info.padTo(serviceCode, ' ', 9));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSIDs()
	 */
	@Override
	public SIDInfo[] getSIDs() throws TelusException {
		return sIDCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSID(java.lang.String)
	 */
	@Override
	public SIDInfo getSID(String sIDCode) throws TelusException {
		return sIDCache.get(sIDCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSpecialNumbers()
	 */
	@Override
	public SpecialNumberInfo[] getSpecialNumbers() throws TelusException {
		return specialNumbersCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSpecialNumber(java.lang.String)
	 */
	@Override
	public SpecialNumberInfo getSpecialNumber(String numberCode) throws TelusException {
		return specialNumbersCache.get(numberCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSpecialNumberRanges()
	 */
	@Override
	public SpecialNumberRangeInfo[] getSpecialNumberRanges() throws TelusException {
		return specialNumberRangeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSpecialNumberRange(java.lang.String)
	 */
	@Override
	public SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber) throws TelusException {

		SpecialNumberRangeInfo[] specialNumberRanges = specialNumberRangeCache.getAll();

		if (specialNumberRanges != null) {
			for (int i = 0; i < specialNumberRanges.length; i++) {
				if (specialNumberRanges[i].isNumberInRange(phoneNumber)) {
					return specialNumberRanges[i];
				}
			}
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getState(java.lang.String)
	 */
	@Override
	public StateInfo getState(String stateCode) throws TelusException {
		return stateCache.get(stateCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getStreetDirections()
	 */
	@Override
	public StreetDirectionInfo[] getStreetDirections() throws TelusException {
		return streetDirectionCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getStreetDirection(java.lang.String)
	 */
	@Override
	public StreetDirectionInfo getStreetDirection(String directionCode) throws TelusException {
		return streetDirectionCache.get(directionCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSubscriptionRoleTypes()
	 */
	@Override
	public SubscriptionRoleTypeInfo[] getSubscriptionRoleTypes() throws TelusException {
		return subscriptionRoleTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getSubscriptionRoleType(java.lang.String)
	 */
	@Override
	public SubscriptionRoleTypeInfo getSubscriptionRoleType(String roleTypeCode) throws TelusException {
		return subscriptionRoleTypeCache.get(roleTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getTalkGroupPriorities()
	 */
	@Override
	public TalkGroupPriorityInfo[] getTalkGroupPriorities() throws TelusException {
		return talkGroupPriorityCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getTaxationPolicies()
	 */
	@Override
	public TaxationPolicyInfo[] getTaxationPolicies() throws TelusException {
		return taxationPolicyCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getTaxationPolicy(java.lang.String)
	 */
	@Override
	public TaxationPolicyInfo getTaxationPolicy(String provinceCode) throws TelusException {
		if (Province.PROVINCE_NL.equals(provinceCode)) {
			return taxationPolicyCache.get("NF");
		} else {
			return taxationPolicyCache.get(provinceCode);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getTermUnits()
	 */
	@Override
	public TermUnitInfo[] getTermUnits() throws TelusException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getTermUnit(java.lang.String)
	 */
	@Override
	public TermUnitInfo getTermUnit(String termUnitCode) throws TelusException {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTitle(java.lang.String)
	 */
	@Override
	public TitleInfo getTitle(String titleCode) throws TelusException {
		return titleCache.get(titleCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUnitType(java.lang.String)
	 */
	@Override
	public UnitTypeInfo getUnitType(String typeCode) throws TelusException {
		return unitTypeCache.get(typeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRateMethod(java.lang.String)
	 */
	@Override
	public UsageRateMethodInfo getUsageRateMethod(String rateMethodCode) throws TelusException {
		return usageRateMethodCache.get(rateMethodCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getUsageRateMethods()
	 */
	@Override
	public UsageRateMethodInfo[] getUsageRateMethods() throws TelusException {
		return usageRateMethodCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRecordType(java.lang.String)
	 */
	@Override
	public UsageRecordTypeInfo getUsageRecordType(String recordTypeCode) throws TelusException {
		return usageRecordTypeCache.get(recordTypeCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getUsageRecordTypes()
	 */
	@Override
	public UsageRecordTypeInfo[] getUsageRecordTypes() throws TelusException {
		return usageRecordTypeCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageUnit(java.lang.String)
	 */
	@Override
	public UsageUnitInfo getUsageUnit(String unitCode) throws TelusException {
		return usageUnitCache.get(unitCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getUsageUnits()
	 */
	@Override
	public UsageUnitInfo[] getUsageUnits() throws TelusException {
		return usageUnitCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getVendorService(java.lang.String)
	 */
	@Override
	public VendorServiceInfo getVendorService(String vendorServiceCode) throws TelusException {
		return vendorServiceCache.get(vendorServiceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getVendorServices()
	 */
	@Override
	public VendorServiceInfo[] getVendorServices() throws TelusException {
		return vendorServiceCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getWPSCategories()
	 */
	@Override
	public PrepaidCategoryInfo[] getWPSCategories() throws TelusException {
		return prepaidCategoryCache.getAll();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWPSCategory(java.lang.String)
	 */
	@Override
	public PrepaidCategoryInfo getWPSCategory(String categoryCode) throws TelusException {
		return prepaidCategoryCache.get(categoryCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataSvc#getWorkFunctions()
	 */
	@Override
	public WorkFunctionInfo[] getWorkFunctions() throws TelusException {
		return workFunctionCache.getAll();
	}

	@Override
	public WorkFunctionInfo[] getWorkFunctions(String departmentCode) throws TelusException {
		List<WorkFunctionInfo> result = new ArrayList<WorkFunctionInfo>();
		for (WorkFunctionInfo workFunction : workFunctionCache.getAll()) {
			if (workFunction.getDepartmentCode().trim().equals(departmentCode.trim())) {
				result.add(workFunction);
			}
		}
		return result.toArray(new WorkFunctionInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkPosition(java.lang.String)
	 */
	@Override
	public WorkPositionInfo getWorkPosition(String workPositionIdCode) throws TelusException {
		return workPositionCache.get(Info.padTo(workPositionIdCode, ' ', 8));
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkPositions()
	 */
	@Override
	public WorkPositionInfo[] getWorkPositions(String functionCode) throws TelusException {
		List<WorkPositionInfo> result = new ArrayList<WorkPositionInfo>();
		for (WorkPositionInfo workPosition : workPositionCache.getAll()) {
			if (workPosition.getFunctionCode().trim().equals(functionCode.trim())) {
				result.add(workPosition);
			}
		}
		return result.toArray(new WorkPositionInfo[result.size()]);
	}

	// Andrew's methods end

	// Greg's methods start

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaperBillChargeType(int, java.lang.String, char, char, java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode, char accountType, char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException {

		ChargeTypeInfo result = null;
		int minimumNumberOfMissingElements = 6;
		int minimumWeightFactor = 11112;
		int matchedIndex = -1;

		if (brandId == 0 && StringUtils.isEmpty(provinceCode) && accountType == '\u0000' && accountSubType == '\u0000' && StringUtils.isEmpty(segment)) {
			throw new IllegalArgumentException("All parameters passed in are either null or empty. This operation requires at least one parameter passed in to be populated.");
		}

		if (StringUtils.isEmpty(invoiceSuppressionLevel)) {
			invoiceSuppressionLevel = "1";
		}

		FeeRuleDto[] feeRule = referenceDataHelper.retrievePaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment, invoiceSuppressionLevel, getLogicalDate());

		for (int j = 0; j < feeRule.length; j++) {
			FeeRuleDto element = feeRule[j];
			int numberOfMissingElements = 0;
			int weightFactor = 0;

			if (element.getBrandId() <= 0) {
				numberOfMissingElements++;
				weightFactor += 10000;
			}
			if (StringUtils.isEmpty(element.getProvinceCode())) {
				numberOfMissingElements++;
				weightFactor += 1000;
			}
			if (element.getAccountType() == '\u0000') {
				numberOfMissingElements++;
				weightFactor += 100;
			}
			if (element.getAccountSubType() == '\u0000') {
				numberOfMissingElements++;
				weightFactor += 10;
			}
			if (StringUtils.isEmpty(element.getSegment())) {
				numberOfMissingElements++;
				weightFactor += 1;
			}

			if (numberOfMissingElements <= minimumNumberOfMissingElements) {
				if (weightFactor <= minimumWeightFactor) {
					if (weightFactor == minimumWeightFactor) {
						// more than one matching data found
						throw new IllegalArgumentException("There are not enough parameters passed in to determine the correct Paper Bill Charge for this account.");
					}
					minimumNumberOfMissingElements = numberOfMissingElements;
					minimumWeightFactor = weightFactor;
					matchedIndex = j;
				}
			}

		}

		if (matchedIndex >= 0) {
			// only one matching data found.
			result = new ChargeTypeInfo(feeRule[matchedIndex].getCode(), feeRule[matchedIndex].getDescription(), feeRule[matchedIndex].getDescriptionFrench(), feeRule[matchedIndex].getProductType(),
					feeRule[matchedIndex].isManualCharge(), feeRule[matchedIndex].getAmount(), feeRule[matchedIndex].isAmountOverrideable(), feeRule[matchedIndex].getLevel(),
					feeRule[matchedIndex].getBalanceImpact());
		}
		//if no matching data found, result is null 

		return result;

	}

	// Greg's methods end

	@Override
	public String[] getServiceCodesByGroup(final String serviceGroupCode) throws TelusException {
		if (StringUtils.isEmpty(serviceGroupCode)) {
			return new String[0];
		}
		return serviceGroupRelationCache.get(serviceGroupCode, new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveServiceGroupRelation(Info.padTo(serviceGroupCode, ' ', 9));
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.impl.ReferenceDataFacadeLocal#clearCache()
	 */
	public void clearCache() {
		logger.info("Clearing reference data cache...");
		cacheManager.clearAll();
		logger.info("Reference data cache clear complete.");
		initializePdsRefData(); //refresh PDS Ref Data
	}

	public void refreshAllCache() {
		long startTime = System.currentTimeMillis();
		if (isRefreshAllowed("all")) {
			logger.info("[CACHE] Refresh starts..");
			activeRefreshJobMap.put("all", new Date());
			String[] cacheNameList = cacheManager.getCacheNames();

			for (String cacheName : cacheNameList) {
				Ehcache cache = cacheManager.getEhcache(cacheName);
				if (cache != null) {
					refreshCache(cache);
				} else {
					logger.error("[CACHE] Cannot refresh cache. Unable to find Cache [" + cacheName + "].");
				}
			}

			long endTime = System.currentTimeMillis();
			activeRefreshJobMap.remove("all");
			logger.info("[CACHE] Refresh ends..total time=[" + (endTime - startTime) + "] ms");
		}
	}

	public void refreshCache(String cacheName) {

		if (cacheName != null && cacheName.startsWith("refreshJobMap-")) { //just a backdoor to force refresh regardless of active refresh job
			cacheName = cacheName.substring("refreshJobMap-".length());
			activeRefreshJobMap.remove(cacheName);
		}

		Ehcache cache = cacheManager.getEhcache(cacheName);
		if (cache != null) {
			if (isRefreshAllowed(cacheName)) {
				refreshCache(cache);
			}
		} else {
			logger.error("[CACHE] Cannot refresh cache. Unable to find Cache [" + cacheName + "].");
		}
	}

	private synchronized boolean isRefreshAllowed(String cacheName) {
		Date lastRefreshAllJobTriggerTime = activeRefreshJobMap.get("all");
		Date lastRefreshJobTriggerTime = activeRefreshJobMap.get(cacheName);

		if (lastRefreshAllJobTriggerTime == null && lastRefreshJobTriggerTime == null) { //no refresh has started
			return true;
		} else if (lastRefreshAllJobTriggerTime != null) {
			if (lastRefreshAllJobTriggerTime.getTime() - System.currentTimeMillis() > 90 * 60 * 1000L) { //more than 1.5 hours ago. assumption: the whole refresh job should have finished in an hour
				activeRefreshJobMap.remove("all");
				return true;
			}
			logger.info("Refreshing [" + cacheName + "] is not allowed since a refresh-all job has already started.");
		} else if (lastRefreshJobTriggerTime != null) {
			if (lastRefreshJobTriggerTime.getTime() - System.currentTimeMillis() > 30 * 60 * 1000L) { //more than 30 min ago. assumptino: a single refresh job should finish within 30 min
				activeRefreshJobMap.remove(cacheName);
				return true;
			}
			logger.info("Refreshing [" + cacheName + "] is not allowed since the same refresh job has already started.");
		}

		return false;
	}

	private void refreshCache(Ehcache cache) {
		if (cache != null) {
			String cacheName = cache.getName();
			activeRefreshJobMap.put(cacheName, new Date());

			if (referenceDataDynamicDefaultCacheNameList.contains(cacheName)) {
				logger.info("[CACHE] Cleared cache for [" + cacheName + "] since it is a dynamic cache.");
				cache.removeAll();
			} else if (cache instanceof SelfPopulatingCache) {
				try {
					logger.info("[CACHE] Starting refresh for [" + cacheName + "]");
					long startTime = System.currentTimeMillis();
					((SelfPopulatingCache) cache).refresh();
					long endTime = System.currentTimeMillis();
					logger.info("[CACHE] Finished refresh [" + cacheName + "] in " + (endTime - startTime) + " ms");
				} catch (CacheException ce) {
					logger.error("[CACHE] Unable to refresh [" + cacheName + "]. Clearing cache instaed", ce);
					cache.removeAll();
					logger.info("[CACHE] Cleared cache for [" + cacheName + "]");
				}
			} else {
				cache.removeAll();
				logger.info("[CACHE] Cleared cache for [" + cacheName + "] since it does not support refresh.");
			}
			activeRefreshJobMap.remove(cacheName);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.impl.ReferenceDataFacadeLocal#restartCache()
	 */
	public void restartCache() {
		logger.info("Restarting reference data cache...");

		try {
			ApplicationServiceLocator.getInstance().getScheduler().unscheduleJob(CACHE_CLEAR_TRIGGER_NAME, REFERENCE_TRIGGER_GROUP);

		} catch (Exception e) {
			logger.error("Error unscheduling cache clearing job: " + e.getMessage(), e);
		}

		cacheManager.shutdown();

		initialize();

		logger.info("Reference data restart complete.");
	}

	// custom loaders

	private class InvoiceSuppressionLevelLoader implements CacheEntryFactory {

		@Override
		public Object createEntry(Object key) throws Exception {
			String[] codesAvailableForUpdate = StringUtils.split(AppConfiguration.getInvoiceSuppressoinLevelUpdateLevels());

			InvoiceSuppressionLevelInfo[] invoiceSuppressionLevels = referenceDataHelper.retrieveInvoiceSuppressionLevels();
			for (InvoiceSuppressionLevelInfo invoiceSuppressionLevel : invoiceSuppressionLevels) {
				invoiceSuppressionLevel.setCodesAvailableForUpdate(codesAvailableForUpdate);
			}
			return new CacheDataGroup<InvoiceSuppressionLevelInfo>(invoiceSuppressionLevels, referenceKeyProvider);
		}
	}

	private class PrepaidCategoryLoader implements CacheEntryFactory {

		@Override
		public Object createEntry(Object key) throws Exception {
			PrepaidCategoryInfo[] wpsCategories = new PrepaidCategoryInfo[0];
			Map<String, PrepaidCategoryInfo> categoryMap = new HashMap<String, PrepaidCategoryInfo>();

			for (ServiceInfo service : getWPSServices()) {
				PrepaidCategoryInfo category = (PrepaidCategoryInfo) service.getCategory();
				PrepaidCategoryInfo categoryFromMap = categoryMap.get(category.getCode());
				if (categoryFromMap == null) {
					categoryMap.put(category.getCode(), category);
					categoryFromMap = category;
				}
				categoryFromMap.addFeature(service);
			}
			if (categoryMap.values() != null) {
				wpsCategories = categoryMap.values().toArray(new PrepaidCategoryInfo[categoryMap.values().size()]);

			}
			return new CacheDataGroup<PrepaidCategoryInfo>(wpsCategories, referenceKeyProvider);
		}
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServiceTerm()
	 */
	@Override
	public ServiceTermDto getServiceTerm(String serviceCode) throws TelusException {
		//change to cache the information
		//return referenceDataHelper.retrieveServiceTerm(Info.padTo(serviceCode, ' ', 9)  );

		return serviceTermCache.get(Info.padService(serviceCode));
	}

	private <T> SelfPopulatiingDataEntryCache<T> createIncludedPromotionCache() {
		String cacheName = "IncludedPromotion";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<T>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {
			@Override
			public Object createEntry(Object key) throws Exception {
				String cacheKey = (String) key;
				String[] params = cacheKey.split("\\.");
				;
				String pricePlanCode = params[0];
				String equipmentType = params[1];
				String networkType = params[2];
				String provinceCode = params[3];
				int term = Integer.parseInt(params[4]);
				return referenceDataHelper.retrieveIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode, term);
			}
		});
	}

	private <T> SelfPopulatiingDataEntryCache<T> createRegularServiceGroupCache() {
		String cacheName = "RegularServiceGroup";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<T>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {
			@Override
			public Object createEntry(Object key) throws Exception {
				String cacheKey = (String) key;
				String[] params = cacheKey.split("\\.");
				String featureCategory = params[0];
				String productType = params[1];
				boolean current = Boolean.parseBoolean(params[2]);
				return referenceDataHelper.retrieveRegularServices(featureCategory, productType, current);
			}
		});
	}

	private <T> SelfPopulatiingDataEntryCache<T> createPricePlanCache() {
		String cacheName = "PricePlan";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<T>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {
			@Override
			public Object createEntry(Object key) throws Exception {
				PricePlanBo.PricePlanCtrieria ppKey = (PricePlanBo.PricePlanCtrieria) key;

				PricePlanInfo pricePlanInfo = referenceDataHelper.retrievePricePlan(Info.padTo(ppKey.getCode(), ' ', 9), ppKey.getEquipmentType(), ppKey.getProvinceCode(), ppKey.getAccountType(),
						ppKey.getAccountSubType(), ppKey.getBrandId(), ppsServicesCache.getAll());
				return pricePlanInfo == null ? null : new PricePlanBo(pricePlanInfo, ReferenceDataFacadeImpl.this);
			}
		});
	}

	private <T> SelfPopulatiingDataEntryCache<T> createPricePlanSummaryCache() {
		String cacheName = "PricePlanSummary";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<T>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				PricePlanInfo pricePlanInfo = referenceDataHelper.retrievePricePlan((String) key);
				return pricePlanInfo == null ? null : new PricePlanBo(pricePlanInfo, ReferenceDataFacadeImpl.this);
			}
		});
	}

	/**
	 * Return Cache statistics for the cache of given name
	 * @param cacheName
	 * @return
	 */
	public String getCacheStatistics(String cacheName) {
		logger.info("retrieve cache statistics...");
		try {
			Ehcache cache = cacheManager.getEhcache(cacheName);

			if (cache != null) {

				String info = cache.getStatistics().toString();
				logger.info(info);
				return info;
			} else {
				return "Cannot find cache with name: " + cacheName;
			}
		} catch (Exception e) {

			logger.warn("encouter exception wihile retrieve statistics  for cache:" + cacheName, e);
			return "encouter exception wihile retrieve statistics  for cache[" + cacheName + "]: " + e.toString();

		} finally {
			logger.info("retrieve cache statistics completed.");
		}
	}

	/**
	 * Return configuration for the cache of given name
	 * @param cacheName
	 * @return
	 */
	public String getCacheConfiguration(String cacheName) {
		logger.info("retrieve cache configuration .." + cacheName);
		try {
			Ehcache cache = cacheManager.getEhcache(cacheName);
			if (cache != null) {

				CacheConfiguration config = cache.getCacheConfiguration();
				String info = new StringBuffer("[CacheConfiguration ").append(" name=").append(config.getName()).append(", maxElementsInMemory=").append(config.getMaxElementsInMemory())
						.append(", memoryStoreEvictionPolicy=").append(config.getMemoryStoreEvictionPolicy().toString()).append(", timeToIdleSeconds=").append(config.getTimeToIdleSeconds())
						.append(", timeToLiveSeconds=").append(config.getTimeToLiveSeconds()).append(", overflowToDisk=").append(config.isOverflowToDisk()).append(", maxElementsOnDisk=")
						.append(config.getMaxElementsOnDisk()).append(" ]").toString();

				logger.info(info);
				return info;
			} else {
				return "Cannot find cache with name: " + cacheName;
			}
		} catch (Exception e) {
			logger.warn("encouter exception wihile retrieve configuration for cache:" + cacheName, e);
			return "encouter exception wihile retrieve configuration  for cache[" + cacheName + "]: " + e.toString();
		} finally {
			logger.info("retrieve cache configuration completed.");
		}
	}

	/**
	 * clear Cache and its statistics for the cache of given name
	 * @param cacheName
	 * @return
	 */
	public void clearCache(String cacheName) {
		logger.info("Clearing cache and its statistics for " + cacheName);
		try {
			Ehcache cache = cacheManager.getEhcache(cacheName);

			if (cache != null) {
				cache.removeAll();
				cache.clearStatistics();
				logger.info("clear cache and its statistics completed.");
			} else {
				logger.warn("Cannot find cache with name: " + cacheName);
			}
		} catch (Exception e) {
			logger.warn("encouter exception wihile clearing cache for:" + cacheName, e);
		}
	}

	@Override
	public TaxSummaryInfo getTaxCalculationListByProvince(String provinceCode, double amount, TaxExemptionInfo taxExemptionInfo) throws TelusException {

		TaxSummaryInfo tax = new TaxSummaryInfo();
		TaxationPolicyInfo taxPolicy = getTaxationPolicy(provinceCode);

		// if tax policy not found return clean tax summary object, i.e. all taxes equal zero
		if (taxPolicy == null) {
			return tax;
		}

		tax.setTaxationPolicy(taxPolicy);

		// GST
		if (taxExemptionInfo.isGstExemptionInd()) {
			tax.setGSTAmount(0.00);
		} else {
			tax.setGSTAmount(AttributeTranslator.roundCurrency(amount * taxPolicy.getGSTRate() / 100));
		}

		// HST
		if (taxExemptionInfo.isHstExemptionInd()) {
			tax.setHSTAmount(0.00);
		} else {
			tax.setHSTAmount(AttributeTranslator.roundCurrency(amount * taxPolicy.getHSTRate() / 100));
		}

		// PST
		if (taxExemptionInfo.isPstExemptionInd() || amount < taxPolicy.getMinimumPSTTaxableAmount()) {
			tax.setPSTAmount(0.00);
		} else if (taxPolicy.getMethod() == com.telus.eas.utility.info.TaxationPolicyInfo.METHOD_TAX_ON_BASE) {
			// Tax on base amount only
			tax.setPSTAmount(AttributeTranslator.roundCurrency(amount * taxPolicy.getPSTRate() / 100));
		} else if (taxPolicy.getMethod() == com.telus.eas.utility.info.TaxationPolicyInfo.METHOD_PST_ON_GST) {
			// Tax on base + GST
			tax.setPSTAmount(AttributeTranslator.roundCurrency((amount + tax.getGSTAmount()) * taxPolicy.getPSTRate() / 100));
		}

		return tax;
	}

	@Override
	public ServiceInfo[] getPrepaidServiceListByEquipmentAndNetworkType(String equipmentType, String networkType) throws TelusException {

		ArrayList<ServiceInfo> prepaidServiceFilteredList = new ArrayList<ServiceInfo>();
		ServiceInfo[] prepaidServiceList = getWPSServices();

		if (prepaidServiceList != null && prepaidServiceList.length > 0) {
			for (ServiceInfo service : prepaidServiceList) {
				if (service.isCompatible(networkType, equipmentType)) {
					prepaidServiceFilteredList.add(service);
				}
			}
		}

		return (ServiceInfo[]) prepaidServiceFilteredList.toArray(new ServiceInfo[prepaidServiceFilteredList.size()]);
	}

	@Override
	public SalesRepInfo getDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException {

		DealerInfo dealerInfo = null;
		SalesRepInfo salesRepInfo = null;

		// Check if the sales rep code is all zeros - if so, convert to an empty string
		String newSalesRepCode = AttributeTranslator.replaceString(salesRepCode, "0", "");

		if (newSalesRepCode == null || newSalesRepCode.equals("")) {
			dealerInfo = referenceDataHelper.retrieveDealerbyDealerCode(dealerCode);
			if (dealerInfo != null) {
				salesRepInfo = new SalesRepInfo();
				salesRepInfo.setCode("0000");
				salesRepInfo.setDealerCode(dealerInfo.getCode());
				salesRepInfo.setName(dealerInfo.getName());
			}
		} else {
			// Use the original sales rep code here
			salesRepInfo = referenceDataHelper.retrieveDealerSalesRepByCode(dealerCode, salesRepCode, expired);
		}

		return salesRepInfo;
	}

	@Override
	public BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles, String province) throws TelusException {

		List<BillCycle> billCycleList = new ArrayList<BillCycle>();

		String[] billCyclesForProvince = ArrayUtil.stringToArray(AppConfiguration.getBillCycleProvinceRestrictions().get(province), ",");
		boolean ignore = false;
		for (int i = 0; i < billCycles.length; i++) {
			for (int j = 0; j < billCyclesForProvince.length; j++) {
				if (billCyclesForProvince[j].equals(billCycles[i].getCode())) {
					ignore = true;
				}
			}
			if (ignore) {
				ignore = false;
			} else {
				billCycleList.add(billCycles[i]);
			}
		}

		return billCycleList.toArray(new BillCycle[billCycleList.size()]);
	}

	/**
	 * ReferencePdsAccess is caching via static member already
	 */
	public void initializePdsRefData() {
		long startTime = System.currentTimeMillis();
		logger.info("[CACHE] RefPDS refresh starts..");
		try {
			ReferencePdsAccess.refresh();
		} catch (Throwable t) {
			logger.error("Error initializing PDS Reference Data. Will depend initialization on accessing methods.", t);
		}
		long endTime = System.currentTimeMillis();
		logger.info("[CACHE] RefPDS refresh ends..total time=[" + (endTime - startTime) + "] ms");
	}

	@Override
	public String getSubscriptionTypeByKBServiceType(String kbServiceType) throws TelusException {
		return getRPDSData(PDS_XREF_SUBSCRIPTION_TYP_XREF_KB, kbServiceType);
	}

	@Override
	public String getServiceInstanceStatusByKBSubscriberStatus(String kbSubscribreStatus) throws TelusException {
		return getRPDSData(PDS_XREF_SERVICE_INSTANCE_STATUS_XREF_KB, kbSubscribreStatus);
	}

	@Override
	public String getBillingAccountStatusByKBAccountStatus(String kbAccountStatus) throws TelusException {
		return getRPDSData(PDS_XREF_BILLING_ACCOUNT_STATUS_XREF_KB, kbAccountStatus);
	}

	@Override
	public String getPaymentMethodTypeByKBPaymentMethodType(String kbPaymentMethodType) throws TelusException {
		return getRPDSData(PDS_XREF_PAYMENT_METHOD_XREF_KB, kbPaymentMethodType);
	}

	@Override
	public String getCreditCardTypeByKBCreditCardType(String kbCreditCardType) throws TelusException {
		return getRPDSData(PDS_XREF_CREDIT_CARD_TYP_XREF_KB, kbCreditCardType);
	}

	@Override
	public String getBillCycleCodeByKBBillCycleCode(String kbBillCycleCode) throws TelusException {
		return getRPDSData(PDS_XREF_BILL_CYCLE_CODE_XREF_KB, kbBillCycleCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataPDSFacade#getNameSuffixByKBNameSuffix(java.lang.String)
	 */
	@Override
	public String getNameSuffixByKBNameSuffix(String kbNameSuffix) throws TelusException {
		return getRPDSData(PDS_XREF_NAME_SUFFIX_XREF_KB, kbNameSuffix);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataPDSFacade#getSaluationCodeByKBSaluationCode(java.lang.String)
	 */
	@Override
	public String getSaluationCodeByKBSaluationCode(String kbSaluationCode) throws TelusException {
		return getRPDSData(PDS_XREF_SALUTATION_CODE_XREF_KB, kbSaluationCode);
	}

	@Override
	public String getEquipmentGroupTypeBySEMSEquipmentGroupType(String semsEquipmentGroupType) throws TelusException {
		return getRPDSData(PDS_XREF_SERVICE_RESOURCE_TYP_XREF_SEMS, semsEquipmentGroupType);
	}

	@Override
	public String getProvinceCodeByKBProvinceCode(String kbProvinceCode) throws TelusException {
		return getRPDSData(PDS_XREF_PROVINCE_STATE_XREF_KB, kbProvinceCode);
	}

	@Override
	public String getCountryCodeByKBCountryCode(String kbCountryCode) throws TelusException {
		return getRPDSData(PDS_XREF_COUNTRY_OVERSEAS_XREF_KB, kbCountryCode);
	}

	private String getRPDSData(String refName, String code) {

		if (refName != null && code != null) {
			@SuppressWarnings("unchecked")
			Collection<String> parentCodes = ReferencePdsAccess.getXrefParentCodes(refName, code.trim());

			if (parentCodes != null) {
				for (String parentCode : parentCodes) {
					return parentCode;
				}
			}
		}

		return null;
	}

	@Override
	public DataSharingGroupInfo[] getDataSharingGroups() {
		return dataSharingGroupCache.getAll();
	}

	@Override
	public DataSharingGroupInfo getDataSharingGroup(String groupCode) {
		return dataSharingGroupCache.get(groupCode);
	}

	//BillC60 changes begin
	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		return referenceDataHelper.openSession(userId, password, applicationId);
	}

	//@Override
	public ServiceFeatureClassificationInfo getServiceFeatureClassification(String classifcationCode) throws TelusException {
		return featureClassificationCache.get(classifcationCode);
	}

	private SelfPopulatiingDataEntryCache<ServiceTermDto> createServiceTermCache() {
		String cacheName = "ServiceTerms";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<ServiceTermDto>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveServiceTerm(Info.padService((String) key));
			}
		});
	}

	private SelfPopulatiingDataEntryCache<ServicePeriodInfo[]> createServicePeriodInfoDataEntryCache() {
		String cacheName = "ServicePeriodDynamic";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<ServicePeriodInfo[]>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveServicePeriodInfo(Info.padService((String) key));
			}
		});
	}

	//this one access data entry cache 
	public ServicePeriodInfo[] getServicePeriodInfo(String serviceCode) throws TelusException {
		return servicePeriodCache.get(serviceCode);
	}

	//this one access data group cache 
	public Map<String, ServicePeriodInfo> getServicePeriodInfo(String serviceCode, String serviceType) throws TelusException {
		ServiceInfo s = null;
		if (ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equals(serviceType)) {
			s = getPricePlan(serviceCode);
		} else {
			s = getRegularService(serviceCode);
		}
		return servicePeriodAllCache.get(s.getPeriodCode());
	}

	private <T> SelfPopulatiingDataEntryCache<ServiceRelationInfo[]> createServiceRelationCache() {
		String cacheName = "ServiceRelations";
		referenceDataDynamicDefaultCacheNameList.add(cacheName);
		return new SelfPopulatiingDataEntryCache<ServiceRelationInfo[]>(cacheName, cacheManager, "referenceDataDynamicDefaultCache", new CacheEntryFactory() {

			@Override
			public Object createEntry(Object key) throws Exception {
				return referenceDataHelper.retrieveRelations(Info.padService((String) key));
			}
		});
	}

	@Override
	public ServiceRelationInfo[] getServiceRelations(final String serviceCode) throws TelusException {
		return serviceRelationCache.get(serviceCode);
	}

	//This method retrieve voice allocation via RefereDataHelper(KB-API), then populate 1) classification, 2) period info
	//This method is intented for creating ServiceAirTimeAllocation cache entry.
	private ServiceAirTimeAllocationInfo retrieveVoiceAllocation(String serviceCode, Date effectiveDate, String sessionId) throws TelusException {
		//retireve info via KB-API
		ServiceAirTimeAllocationInfo serviceAirTimeInfo;
		try {
			serviceAirTimeInfo = referenceDataHelper.retrieveVoiceAllocation(Info.padService((String) serviceCode), effectiveDate, sessionId);
			serviceAirTimeInfo.setValidSOC(true);
		} catch (ApplicationException e) {
			serviceAirTimeInfo = new ServiceAirTimeAllocationInfo();
			serviceAirTimeInfo.setServiceCode(serviceCode);
			serviceAirTimeInfo.setValidSOC(false);
			serviceAirTimeInfo.setErrorCode(e.getErrorCode());
			serviceAirTimeInfo.setErrorMessage(e.getErrorMessage());
			return serviceAirTimeInfo;
			//			throw new TelusException( e );
		}

		if (serviceAirTimeInfo != null) {
			FeatureAirTimeAllocationInfo[] featureInfos = serviceAirTimeInfo.getFeatureAirTimeAllocations0();

			if (featureInfos != null) {
				Map<String, ServicePeriodInfo> periodMap = null;
				if (serviceAirTimeInfo.isContainPeriodBasedFeature()) {
					periodMap = getServicePeriodInfo(serviceAirTimeInfo.getServiceCode(), serviceAirTimeInfo.getServiceType());
				}
				//populate extra infomation on the feature level
				for (FeatureAirTimeAllocationInfo featureAirTimeInfo : featureInfos) {

					ServiceFeatureClassificationInfo classification = getServiceFeatureClassification(featureAirTimeInfo.getClassification0().getCode());
					//the setClassification already took care of checking NULL object.
					featureAirTimeInfo.setClassification(classification);

					if (featureAirTimeInfo.isPeriodBased() && periodMap != null) {
						ServicePeriodInfo periodInfo = periodMap.get(featureAirTimeInfo.getPeriodValueCode());
						if (periodInfo != null) {
							featureAirTimeInfo.setPeriod(periodInfo);
						}
					}
				}
			}
		}

		return serviceAirTimeInfo;
	}

	public ServiceAirTimeAllocationInfo getVoiceAllocation(String serviceCode, final Date effectiveDate, final String sessionId) throws TelusException {

		final String socCode = Info.padService(serviceCode);

		// For both past date and future date, skip Cache and call KB directly. 
		// Throw the KB exception for the past date and not to cache the KB data back for future date.
		if (effectiveDate == null || DateUtil.isSameDay(getLogicalDate(), effectiveDate)) {
			return serviceVoiceAllocationCache.get(socCode, new CacheEntryFactory() {

				@Override
				public Object createEntry(Object key) throws Exception {
					return retrieveVoiceAllocation(socCode, effectiveDate, sessionId);
				}
			});
		} else {
			return retrieveVoiceAllocation(socCode, effectiveDate, sessionId);
		}
	}

	public ServiceAirTimeAllocationInfo[] getVoiceAllocation(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		ArrayList<ServiceAirTimeAllocationInfo> result = new ArrayList<ServiceAirTimeAllocationInfo>();
		for (String serviceCode : serviceCodes) {
			result.add(getVoiceAllocation(serviceCode, effectiveDate, sessionId));
		}
		return result.toArray(new ServiceAirTimeAllocationInfo[0]);
	}

	private static List<String> TERM_BASED_SOC_SERVICETYPES = Arrays
			.asList(new String[] { ServiceInfo.SERVICE_TYPE_CODE_OPTIONAL_AUTOEXP_SOC, ServiceInfo.SERVICE_TYPE_CODE_REG_AUTOEXP_SOC, ServiceInfo.SERVICE_TYPE_CODE_PROMO_SOC });

	private static List<String> ADDON_SOC_SERVICETYPES = Arrays.asList(new String[] { ServiceInfo.SERVICE_TYPE_CODE_REG_AUTOEXP_SOC, ServiceInfo.SERVICE_TYPE_CODE_REGULAR_SOC, });

	private static List<String> SOC_FOLLOW_SOC_RELATIONS = Arrays.asList(new String[] { ServiceRelation.TYPE_PROMOTION, //KB promotions
			ServiceRelation.TYPE_BOUND, //Bound service
			ServiceRelation.TYPE_SEQUENTIALLY_BOUND //Sequentially bound service
	});

	public ServiceAirTimeAllocationInfo[] getCalculatedEffectedVoiceAllocation(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		if (effectiveDate == null)
			effectiveDate = getLogicalDate();

		/*		Calling Application will never pass past date
		 * 		else if(DateUtil.isBefore(effectiveDate, getLogicalDate()))
					throw new TelusException("SYS00013", "Activity Date Cannot be Past Date");
		*/
		List<String> validList = new ArrayList<String>();
		Map<String, ServiceAirTimeAllocationInfo> invalidSocAirtimeAllocationMap = new LinkedHashMap<String, ServiceAirTimeAllocationInfo>();

		for (String code : serviceCodes) {
			if (serviceCodesCache.containsKey(code.trim())) {
				validList.add(code);
			} else {
				ServiceAirTimeAllocationInfo info = new ServiceAirTimeAllocationInfo();
				info.setValidSOC(false);
				info.setErrorCode(ErrorCodes.INVALID_SOC_CODE);
				info.setErrorMessage("Input SOC -" + code + "- does not Exist");
				invalidSocAirtimeAllocationMap.put(code, info);
			}
		}

		Map<String, ServiceAirTimeAllocationInfo> socAirtimeAllocationMap = getVoiceAllocationForIncomingSocs(validList.toArray(new String[validList.size()]), effectiveDate, sessionId);
		Map<String, ServiceAirTimeAllocationInfo> validSocAirtimeAllocationMap = new LinkedHashMap<String, ServiceAirTimeAllocationInfo>();

		for (ServiceAirTimeAllocationInfo socAirtimeAllocation : socAirtimeAllocationMap.values()) {
			if (socAirtimeAllocation.isValidSOC())
				validSocAirtimeAllocationMap.put(socAirtimeAllocation.getServiceCode(), socAirtimeAllocation);
			else
				invalidSocAirtimeAllocationMap.put(socAirtimeAllocation.getServiceCode(), socAirtimeAllocation);
		}
		ServiceAirTimeAllocationInfo[] invalidSocAllocation = invalidSocAirtimeAllocationMap.values().toArray(new ServiceAirTimeAllocationInfo[invalidSocAirtimeAllocationMap.size()]);

		appendAdditionalServices(validSocAirtimeAllocationMap, effectiveDate, sessionId);
		ServiceAirTimeAllocationInfo[] validSocAllocations = adjustSocEffectiveTimeFrame(validSocAirtimeAllocationMap);
		socAirtimeAllocationMap.clear();
		validSocAirtimeAllocationMap.clear();
		invalidSocAirtimeAllocationMap.clear();
		return (ServiceAirTimeAllocationInfo[]) ArrayUtils.addAll(validSocAllocations, invalidSocAllocation);
	}

	private Map<String, ServiceAirTimeAllocationInfo> getVoiceAllocationForIncomingSocs(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		Map<String, ServiceAirTimeAllocationInfo> socAirtimeAllocationMap = new LinkedHashMap<String, ServiceAirTimeAllocationInfo>();
		for (String serviceCode : serviceCodes) {
			serviceCode = Info.padService(serviceCode);
			if (socAirtimeAllocationMap.containsKey(serviceCode) == false) { //filter out duplicate one
				ServiceAirTimeAllocationInfo info = getVoiceAllocation(serviceCode, effectiveDate, sessionId);
				if (info != null) {
					//Becaues we need to set effective/expiration date, we have to make a clone of the object that got from cache.
					info = (ServiceAirTimeAllocationInfo) info.clone();
					info.setEffectiveDate(effectiveDate);
					info.setExpirationDate(null);
					socAirtimeAllocationMap.put(info.getServiceCode(), info);
				}
			}
		}
		return socAirtimeAllocationMap;
	}

	private void appendAdditionalServices(Map<String, ServiceAirTimeAllocationInfo> socAirtimeAllocationMap, Date effectiveDate, String sessionId) throws TelusException {
		Map<String, ServiceAirTimeAllocationInfo> additionalServices = new LinkedHashMap<String, ServiceAirTimeAllocationInfo>();

		for (ServiceAirTimeAllocationInfo socAirtimeAllocation : socAirtimeAllocationMap.values()) {
			if (ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equals(socAirtimeAllocation.getServiceType())) {
				//do not query relation for price plan
				continue;
			}

			ServiceRelationInfo[] relations = getServiceRelations(socAirtimeAllocation.getServiceCode());
			for (ServiceRelationInfo relation : relations) {
				String relationType = relation.getType();
				if (SOC_FOLLOW_SOC_RELATIONS.contains(relationType)) {
					String followSocCode = Info.padService(relation.getServiceCode());
					ServiceAirTimeAllocationInfo followSOC = socAirtimeAllocationMap.get(followSocCode);
					if (followSOC != null) {
						//the incoming list already contains this follow SOC.
						establishSocEffectSequence(relationType, socAirtimeAllocation, followSOC);
					} else {
						//incoming list does not contain this SOC, check additional serivces we just added
						followSOC = additionalServices.get(followSocCode);
						if (followSOC != null) {
							//the addiation service list already contain this service, so the relation already established, 
							//do nothing here.
							//
							//If reaches here, that means the incoming list contains two leading SOCs have this SOC as follow SOC/
							//Is his possible???

						} else {
							if (!serviceCodesCache.containsKey(followSocCode.trim())) {
								throw new TelusException("", "the follow soc[" + followSocCode + "] does not exist for the soc[" + socAirtimeAllocation.getServiceCode() + "] - data integrity issue");
							}
							if (testFollowSocAddition(socAirtimeAllocation.getServiceCode(), followSocCode) == false) {
								logger.warn("SOC[" + followSocCode + "] is non current or expired for sale.");
							} else {

								//additional service list does not contain the SOC. Retrieve the info from cache / KB
								followSOC = (ServiceAirTimeAllocationInfo) getVoiceAllocation(followSocCode, effectiveDate, sessionId).clone();
								followSOC.setEffectiveDate(effectiveDate);
								followSOC.setExpirationDate(null);
								additionalServices.put(followSOC.getServiceCode(), followSOC);

								establishSocEffectSequence(relationType, socAirtimeAllocation, followSOC);
							}
						}
					}
				}
			} //for relations
		} //for incomingSOCs.values

		socAirtimeAllocationMap.putAll(additionalServices);
		additionalServices.clear();
	}

	private ServiceAirTimeAllocationInfo[] adjustSocEffectiveTimeFrame(Map<String, ServiceAirTimeAllocationInfo> socAirtimeAllocationMap) throws TelusException {
		String pricePlanCode = null;
		for (ServiceAirTimeAllocationInfo incomingSOC : socAirtimeAllocationMap.values()) {
			if (ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equals(incomingSOC.getServiceType())) {
				pricePlanCode = incomingSOC.getServiceCode();
				continue;
			}
		}

		ServiceAirTimeAllocationInfo[] socAllocations = socAirtimeAllocationMap.values().toArray(new ServiceAirTimeAllocationInfo[socAirtimeAllocationMap.size()]);

		for (ServiceAirTimeAllocationInfo service : socAllocations) {
			calculateServiceExpirationDate(service, pricePlanCode);
			calculateEffectiveTimeframe(service, socAirtimeAllocationMap);
		}

		if (socAllocations.length != socAirtimeAllocationMap.size()) { //some soc get removed
			socAllocations = socAirtimeAllocationMap.values().toArray(new ServiceAirTimeAllocationInfo[socAirtimeAllocationMap.size()]);
		}
		socAirtimeAllocationMap.clear();
		return socAllocations;
	}

	private void calculateServiceExpirationDate(ServiceAirTimeAllocationInfo service, String pricePlanCode) throws TelusException {
		//special logic for sequentially-bound service
		if (service.hasSequentiallyBoundService()) {
			//The orginal logic is in TMContract.addBoundServices(...)
			//per the exisiting configuration, the leading SOC which has sequentially-bound service, is service type 'O'.
			//so here, we utilize the existing query of retrieving PricePlan, from the price plan find the included service, then get termMonth 
			if (pricePlanCode == null) {
				throw new TelusException("", "Input does not contain price plan code");
			} else {
				PricePlanInfo pp = getPricePlan(pricePlanCode);
				try {
					ServiceInfo theInlcudedService = pp.getService0(service.getServiceCode());
					if (theInlcudedService == null) {
						logger.error("PricePlan[" + pricePlanCode + "] does not have an included service[" + service.getServiceCode() + "]");
					} else {
						//Note, per current codebase and configuration, the termMnoths is always zero, not sure if this is correct. 
						service.setExpirationDate(ServiceTermDto.advanceDateByMonth(service.getEffectiveDate(), theInlcudedService.getTermMonths()));
					}
				} catch (UnknownObjectException e) {
					logger.error("PricePlan[" + pricePlanCode + "] does not have included service[" + service.getServiceCode() + "]", e);
				}
			}
		} else if (TERM_BASED_SOC_SERVICETYPES.contains(service.getServiceType())) {

			ServiceTermDto term = getServiceTerm(service.getServiceCode());
			service.setExpirationDate(term.calculateExpirationDate(service.getEffectiveDate()));
		}

	}

	private void calculateEffectiveTimeframe(ServiceAirTimeAllocationInfo service, Map<String, ServiceAirTimeAllocationInfo> socAirtimeAllocationMap) throws TelusException {
		ServiceAirTimeAllocationInfo followEffecitveSoc = service.getFollowEffectiveSoc();
		if (followEffecitveSoc != null) {

			if (service.getExpirationDate() == null) {
				//this is where we would not figure out the leading SOC's term, shall not happen
				//TODO, what else can we do?
				socAirtimeAllocationMap.remove(followEffecitveSoc.getServiceCode());
			} else {
				followEffecitveSoc.setEffectiveDate(service.getExpirationDate());

				if (TERM_BASED_SOC_SERVICETYPES.contains(followEffecitveSoc.getServiceType())) {
					ServiceTermDto term = getServiceTerm(followEffecitveSoc.getServiceCode());
					followEffecitveSoc.setExpirationDate(term.calculateExpirationDate(followEffecitveSoc.getEffectiveDate()));
				} else {
					followEffecitveSoc.setExpirationDate(null);
				}
			}
		}
	}

	private void establishSocEffectSequence(String relationType, ServiceAirTimeAllocationInfo leadingSOC, ServiceAirTimeAllocationInfo followSOC) {

		followSOC.setLeadingServiceCode(leadingSOC.getServiceCode());

		//build the effectiveness sequence: which SOC effective first.
		if (ServiceRelation.TYPE_PROMOTION.equals(relationType)) {
			//This shall be the KB logic: 
			//The follow SOC (serviceType=S) become effective first, 
			//The leading SOC ( serviceType=R ) become effective on the date promotional SOC expires.
			followSOC.setFollowEffectiveSoc(leadingSOC);
		} else if (ServiceRelation.TYPE_SEQUENTIALLY_BOUND.equals(relationType)) {
			//The leading SOC become effective first, 
			//the follow soc become effective on the date leading SOC expires.
			leadingSOC.setFollowEffectiveSoc(followSOC);
			//per existing configuration, the leading SOC's serivce_type is 'O', it is not term based SOC
			//so we need to set this flag, to be used in the last step when calculating the effecitveness timeframe
			leadingSOC.setSequentiallyBoundServiceAttached(true);
		}
	}

	//check if the service sale_exp_date is null or in the future
	private boolean isAvailableForSale(Service service) throws TelusException {
		return (service != null && (service.getExpiryDate() == null || service.getExpiryDate().after(getLogicalDate())));
	}

	private boolean testFollowSocAddition(String leadingSocCode, String followSocCode) throws TelusException {
		boolean result = false;
		Service followService = getRegularService(followSocCode);
		if (ServiceInfo.SERVICE_TYPE_CODE_PROMO_SOC.equals(followService.getServiceType())) {

			if (isAvailableForSale(followService)) {
				result = true;
			} else {
				logger.warn("promotional SOC[" + followSocCode + "] is expired for sale. ");
			}
		} else {
			//the following mimic the logic in TMContract.testBoundServiceAddtion, with exception of 
			//followService's equipment type compatibility check( becuase we don't have network/equipment type info to compare against)

			result = isFollowSocCompatibleWithLeadingSoc(leadingSocCode, followSocCode, followService);
		}
		return result;
	}

	private boolean isFollowSocCompatibleWithLeadingSoc(String leadingSocCode, String followSocCode, Service followService) throws TelusException {
		Service leadingService = getRegularService(leadingSocCode);

		Date leadingSOCExpDate = leadingService.getExpiryDate();
		Date followSOCExpDate = followService.getExpiryDate();
		Date today = getLogicalDate();
		if ((leadingSOCExpDate == null || leadingSOCExpDate.compareTo(today) > 0) && (followSOCExpDate == null || followSOCExpDate.compareTo(today) > 0)) {
			if ((leadingService.isCurrent() && followService.isCurrent()) //if leading is current, then follow has to be current 
					|| leadingService.isCurrent() == false) {
				return true;
			} else {
				logger.warn("leadingSOC[" + leadingSocCode + "] current[" + leadingService.isCurrent() + "] " + "followSOC[" + followSocCode + "] current[" + followService.isCurrent() + "]");
			}
		} else {
			logger.warn(
					"leadingSOC[" + leadingSocCode + "] saleExpDate[" + leadingService.getExpiryDate() + "] " + "followSOC[" + followSocCode + "] saleExpDate[" + followService.getExpiryDate() + "]");
		}
		return false;
	}
	//BillC60 changes end

	@Override
	public TestPointResultInfo testRefPds() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("RefPDS System");
		try {
			String result = getProvinceCodeByKBProvinceCode("ON");
			resultInfo.setResultDetail("Invoked RPDS System to test provinceCodeByKBProvinceCode method for province ON:" + result);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}

		return resultInfo;
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public Map<String, List<ServiceAndRelationInfo>> getServiceAndRelationList(ServiceRelationInfo[] serviceRelations) throws TelusException {
		Map<String, List<ServiceAndRelationInfo>> serviceRelationMap = new Hashtable<String, List<ServiceAndRelationInfo>>();
		for (ServiceRelationInfo serviceRelation : serviceRelations) {
			List<ServiceAndRelationInfo> serviceAndRelationList = new ArrayList<ServiceAndRelationInfo>();
			ServiceRelationInfo[] relationList = referenceDataHelper.retrieveRelations(serviceRelation.getServiceCode());
			for (ServiceRelationInfo relation : relationList) {
				if (StringUtils.isBlank(serviceRelation.getType()) || serviceRelation.getType().equals(relation.getType())) {
					ServiceAndRelationInfo serviceAndRelation = new ServiceAndRelationInfo();
					serviceAndRelation.setService(getRegularService(relation.getServiceCode()));
					serviceAndRelation.setServiceRelation(relation);
					serviceAndRelationList.add(serviceAndRelation);
				}
			}
			serviceRelationMap.put(serviceRelation.getServiceCode(), serviceAndRelationList);
		}
		return serviceRelationMap;
	}

	@Override
	public Map<String, List<String>> getServiceCodeListByGroupList(String[] groupCodeList) throws TelusException {
		Map<String, List<String>> serviceCodeListMap = new Hashtable<String, List<String>>();
		for (String groupCode : groupCodeList) {
			serviceCodeListMap.put(groupCode, Arrays.asList(getServiceCodesByGroup(groupCode)));
		}
		return serviceCodeListMap;
	}

	@Override
	public Map<String, List<ServiceInfo>> getServiceListByGroupList(String[] serviceGroupCodeList) throws TelusException {
		Map<String, List<ServiceInfo>> serviceListMap = new Hashtable<String, List<ServiceInfo>>();
		for (String serviceGroupCode : serviceGroupCodeList) {
			List<ServiceInfo> serviceList = new ArrayList<ServiceInfo>();
			for (String serviceCode : getServiceCodesByGroup(serviceGroupCode)) {
				ServiceInfo serviceInfo = getRegularService(serviceCode);
				if (serviceInfo != null) {
					serviceList.add(serviceInfo);
				}
			}
			serviceListMap.put(serviceGroupCode, serviceList);
		}
		return serviceListMap;
	}

	@Override
	public Map<String, Double> getAlternateRecurringCharge(String[] serviceCodeList, String provinceCode, String npaNxx, String corporateId) throws TelusException {
		Map<String, Double> chargeMap = new Hashtable<String, Double>();
		for (String serviceCode : serviceCodeList) {
			String npa = "", nxx = "";
			if (npaNxx != null && npaNxx.length() == 6) {
				npa = npaNxx.substring(0, 3);
				nxx = npaNxx.substring(3);
			}
			double charge = referenceDataHelper.retrieveAlternateRecurringCharge(getRegularService(serviceCode), provinceCode, npa, nxx, corporateId);
			chargeMap.put(serviceCode, charge);
		}
		return chargeMap;
	}

	@Override
	public boolean isNotificationEligible(String transactionType, String originatingeApp, int brandId, String accountType, String banSegment, String productType) {

		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("TRANSACTION_TYPE", transactionType);
		criteria.put("ORIGINATING_APPLICATION_ID", originatingeApp);
		criteria.put("BRAND_ID", String.valueOf(brandId));
		criteria.put("ACCOUNT_TYPE_SUBTYPE", accountType);
		criteria.put("BAN_SEGMENT", banSegment);
		if (productType != null) {
			criteria.put("PRODUCT_TYPE", productType);
		}

		RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule("WIRELESS_TRANSACTION_NOTIFICATION_ELIGIBILITY", criteria);

		boolean result = false;
		if (ruleOutput != null)
			result = "YES".equalsIgnoreCase(ruleOutput.getValue("NOTIFICATION_IND"));

		return result;
	}

	@Override
	public SeatTypeInfo[] getSeatTypes() throws TelusException {
		return seatTypeCache.getAll();
	}

	@Override
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException {
		return referenceDataHelper.isAssociatedIncludedPromotion(Info.padTo(pricePlanCode, ' ', 9), term, Info.padTo(serviceCode, ' ', 9));
	}

	@Override
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException {
		return referenceDataHelper.isPPSEligible(accountType, accountSubType, ppsServicesCache.getAll());
	}

	@Override
	public ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException {
		return referenceDataHelper.retrieveServiceExtendedInfo(serviceCodes);
	}

	@Override
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo() throws TelusException {
		
		List<WCCServiceExtendedInfo> wccServiceExtendedInfoList = new ArrayList<WCCServiceExtendedInfo>();
		for (SapccMappedServiceInfo sapccMappedServiceInfo : sapccMappedServiceInfoCache.getAll()) {
			WCCServiceExtendedInfo wccServiceExtendedInfo = createWCCServiceExtendedInfo(sapccMappedServiceInfo);
			if (wccServiceExtendedInfo != null) {
				wccServiceExtendedInfoList.add(wccServiceExtendedInfo);
			}
		}
		
		return wccServiceExtendedInfoList.toArray(new WCCServiceExtendedInfo[0]);
	}

	@Override
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo(String[] socCodes) throws TelusException {
		
		List<WCCServiceExtendedInfo> wccServiceExtendedInfoList = new ArrayList<WCCServiceExtendedInfo>();
		for (String socCode : socCodes) {
			SapccMappedServiceInfo sapccMappedServiceInfo = sapccMappedServiceInfoCache.get(socCode);
			if (sapccMappedServiceInfo != null) {
				WCCServiceExtendedInfo wccServiceExtendedInfo = createWCCServiceExtendedInfo(sapccMappedServiceInfo);
				if (wccServiceExtendedInfo != null) {
					wccServiceExtendedInfoList.add(wccServiceExtendedInfo);
				}
			}			
		}
		
		return wccServiceExtendedInfoList.toArray(new WCCServiceExtendedInfo[wccServiceExtendedInfoList.size()]);
	}
	
	private WCCServiceExtendedInfo createWCCServiceExtendedInfo(SapccMappedServiceInfo sapccMappedServiceInfo) {
		
		ServiceInfo regularService = regularServiceCache.get(sapccMappedServiceInfo.getCode());
		SapccOfferInfo sapccOfferInfo = sapccOfferInfoCache.get(sapccMappedServiceInfo.getOfferId());
		if (regularService != null && sapccOfferInfo != null) {
			WCCServiceExtendedInfo wccServiceExtendedInfo = new WCCServiceExtendedInfo();
			wccServiceExtendedInfo.setCode(sapccMappedServiceInfo.getCode());
			wccServiceExtendedInfo.setDescription(regularService.getDescription());
			wccServiceExtendedInfo.setDescriptionFrench(regularService.getDescriptionFrench());
			wccServiceExtendedInfo.setChargeAmount(regularService.getRecurringCharge());
			wccServiceExtendedInfo.setSapccOfferInfo(sapccOfferInfo);			
			return wccServiceExtendedInfo;
		}
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Map getDataSharingPricingGroups() throws TelusException {
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		@SuppressWarnings("unchecked")
		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes(PDS_DATA_SHARING_PRICING_GROUP_TABLE, ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
			String accountTypes = decode.getCode();
			String dataSharingGroups = decode.getDecode();
			List<String> dataSharingGroupList = Arrays.asList(dataSharingGroups.split(PDS_DATA_SHARING_PRICING_GROUP_DELIM));
			map.put(accountTypes, dataSharingGroupList);
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getServiceEditions() throws TelusException {

		List<ServiceEditionInfo> serviceEditionList = new ArrayList<ServiceEditionInfo>();
		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes(PDS_SERVICE_EDITION_TABLE, ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
			String serviceEdition = decode.getCode();
			String familyType = decode.getDecode();
			HashMap<String, String> criteria = new HashMap<String, String>();
			criteria.put(PDS_SERVICE_EDITION_RULE_CODE, serviceEdition);
			RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule(PDS_SERVICE_EDITION_RULE, criteria);
			ServiceEditionInfo serviceEditionInfo = new ServiceEditionInfo();
			serviceEditionInfo.setCode(serviceEdition);
			serviceEditionInfo.setFamilyType(familyType);
			serviceEditionInfo.setBusinessName(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_BUSINESS_NAME_EN));
			serviceEditionInfo.setBusinessNameFrench(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_BUSINESS_NAME_FR));
			serviceEditionInfo.setDescription(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_DESCRIPTION_EN));
			serviceEditionInfo.setDescriptionFrench(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_DESCRIPTION_FR));
			serviceEditionInfo.setLocale(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_LOCALE));
			serviceEditionInfo.setRank(Integer.valueOf(ruleOutput.getValue(PDS_SERVICE_EDITION_RULE_RANK)));
			serviceEditionList.add(serviceEditionInfo);
		}
		return serviceEditionList;
	}

	/**
	 * in order to get ServiceInfo information in ejb,
	 * am not sure , if getService0(String code) , will satisfy   serviceInfo.isRIMMandatoryGroup/serviceInfo.isPDAMandatoryGroup and serviceInfo.containsSwitchCode(..)? otherwise how to get generic ServiceInfo?
	 * 
	 */
	private boolean isRIM(PricePlanInfo priceplanInfo, String priceplanCode) throws UnknownObjectException {

		ServiceInfo serviceInfo = priceplanInfo.getService0(priceplanCode);
		if (serviceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(serviceInfo.getServiceType())) {
			return serviceInfo.isRIM();
		} else if (!serviceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(serviceInfo.getServiceType()) && Equipment.PRODUCT_TYPE_PCS.equals(serviceInfo.getProductType())) {

			return serviceInfo.isInRIMMandatoryGroup() || serviceInfo.containsSwitchCode("RIMBES");
		} else if (!serviceInfo.SERVICE_TYPE_CODE_PRICE_PLAN.equalsIgnoreCase(serviceInfo.getServiceType()) && Equipment.PRODUCT_TYPE_IDEN.equals(serviceInfo.getProductType())) {
			String[] categoryCodes = serviceInfo.getCategoryCodes();
			if (categoryCodes == null || categoryCodes.length == 0) {
				return false;
			}

			String code = null;
			for (int i = 0; i < categoryCodes.length; i++) {
				code = categoryCodes[i];
				if (FeatureInfo.CATEGORY_CODE_RIM.equals(code)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * in order to get ServiceInfo information in ejb,
	 * am not sure , if getService0(String code) , will satisfy   serviceInfo.isRIMMandatoryGroup/serviceInfo.isPDAMandatoryGroup and serviceInfo.containsSwitchCode(..)? otherwise how to get generic ServiceInfo?
	 * 
	 */
	private boolean isPDA(PricePlanInfo priceplanInfo, String priceplanCode) throws UnknownObjectException {

		ServiceInfo serviceInfo = priceplanInfo.getService0(priceplanCode);
		return (serviceInfo.isInPDAMandatoryGroup() || serviceInfo.containsSwitchCode("VEMAIL"));

	}

	@Override
	public ServiceFamilyTypeInfo[] getPPSServices() throws TelusException {
		return ppsServicesCache.getAll();
	}

	@Override
	public PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId) throws TelusException {
		return referenceDataHelper.retrievePricePlan(pPricePlanCD, pEquipmentType, pProvinceCD, pAccountType, pAccountSubType, pBrandId, ppsServicesCache.getAll());

	}

	@Override
	public RoamingServiceNotificationInfo[] retrieveRoamingServiceNotificationInfo(String[] serviceCodes) throws TelusException {
		return referenceDataHelper.retrieveRoamingServiceNotificationInfo(serviceCodes);
	}

	@Override
	public ReferenceInfo retrieveMarketingDescriptionBySoc(String soc) throws TelusException {
		return marketingDescriptionCache.get(soc.trim());
	}

	@Override
	public String getNotificationTemplateSchemaVerison(String transactionType, int brandId, String accountType, String banSegment, String productType, String deliveryChannel, String language)
			throws TelusException {
		HashMap<String, String> criteria = new HashMap<String, String>();
		criteria.put("TRANSACTION_TYPE", transactionType);
		criteria.put("BRAND_ID", String.valueOf(brandId));
		criteria.put("ACCOUNT_TYPE_SUBTYPE", accountType);
		criteria.put("BAN_SEGMENT", banSegment);
		if (productType != null) {
			criteria.put("PRODUCT_TYPE", productType);
		}
		criteria.put("DELIVERY_CHANNEL_CD", deliveryChannel);
		if (language == null || language.trim().length() == 0) {
			language = "EN";
		}
		criteria.put("LANGUAGE_CD", language);

		RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule("WIRELESS_TRANSACTION_NOTIFICATION_TEMPLATE", criteria);

		if (ruleOutput == null) {
			logger.debug("RuleOutput is null, no record exists in WIRELESS_TRANSACTION_NOTIFICATION_TEMPLATE for criteria: " + criteria);
			return null;
		}

		try {

			String schemaVersionCode = null;
			schemaVersionCode = ruleOutput.getValue("SCHEMA_VERSION_CD");

			/**
			 * Naresh Annabathula: We have not updated the SCHEMA_VERSION_CD column value for 1.0 version of template as it is already exists
			 * when we added the "SCHEMA_VERSION_CD column to WIRELESS_TRANSACTION_NOTIFICATION_TEMPLATE table. So we are only
			 * adding this value to table start from 2.0 version of templates onwards, null value we are defaulted to 1.0 inside CAPI code.
			 */

			if (schemaVersionCode == null) {
				schemaVersionCode = "1.0";
			}

			return schemaVersionCode;

		} catch (RuntimeException e) {
			logger.error("Encountered error while extracting rule output; criteria: " + criteria + ruleOutput);
			throw e;
		}
	}

	@Override
	public ServiceInfo[] getIncludedPromotions(String pricePlanCode, String equipmentType, String networkType, String provinceCode, int term) throws TelusException {

		if (pricePlanCode == null || pricePlanCode.trim().isEmpty()) {
			logger.error("getIncludedPromotions: pricePlanCode=[" + pricePlanCode + "]");
			return new ServiceInfo[0];
		} else if (provinceCode == null || provinceCode.trim().isEmpty()) {
			logger.error("getIncludedPromotions: provinceCode=[" + provinceCode + "]");
			return new ServiceInfo[0];
		}

		if (equipmentType == null || equipmentType.trim().isEmpty()) {
			logger.error("getIncludedPromotions: equipmentType=[" + equipmentType + "]. Using equipmentType=[9]");
			equipmentType = Equipment.EQUIPMENT_TYPE_ALL;
		}

		if (networkType == null || networkType.trim().isEmpty()) {
			logger.error("getIncludedPromotions: networkType=[" + networkType + "]. Using networkType=[9]");
			networkType = NetworkType.NETWORK_TYPE_ALL;
		} else if ("HSPA".equalsIgnoreCase(networkType)) {
			logger.error("getIncludedPromotions: networkType=[" + networkType + "]. Using networkType=[H]");
			networkType = NetworkType.NETWORK_TYPE_HSPA;
		}

		final String key = CacheKeyBuilder.createComplexKey(pricePlanCode, equipmentType, networkType, provinceCode, term);

		return includedPromotionCache.get(key);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean isCDASupportedAccountTypeSubType(String accountTypeSubType) {

		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes("CDA_BILLING_ACCOUNT_TYP_SUBTYP", ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
			if (StringUtils.equalsIgnoreCase(decode.getCode(), accountTypeSubType)) {
				return true;
			}
		}

		return false;
	}

	// TODO add the following method for Business Connect July 2018
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getLicenses() throws TelusException {

		List<LicenseInfo> licenseList = new ArrayList<LicenseInfo>();
		Collection<ReferenceDecode> decodeList = ReferencePdsAccess.getAllReferenceDecodes("BUSINESS_CONNECT_LICENSE", ReferencePdsAccess.LANG_EN);
		for (ReferenceDecode decode : decodeList) {
//			HashMap<String, String> criteria = new HashMap<String, String>();
//			criteria.put("LICENSE_CD", switchCode);
//			RuleOutput ruleOutput = ReferencePdsAccess.evaluateRule("BUSINESS_CONNECT_LICENSE_RULE", criteria);
			LicenseInfo licenseInfo = new LicenseInfo();
			licenseInfo.setCode(decode.getCode());
			licenseInfo.setSKU(decode.getDecode());
			licenseInfo.setDescription(decode.getDescription());
//			licenseInfo.setFamilyType("FAMILY_TYPE_CD");
//			licenseInfo.setBusinessName(ruleOutput.getValue("BUSINESS_EN_NM"));
//			licenseInfo.setBusinessNameFrench(ruleOutput.getValue("BUSINESS_FR_NM"));
//			licenseInfo.setDescription(ruleOutput.getValue("DESCRIPTION_EN_TXT"));
//			licenseInfo.setDescriptionFrench(ruleOutput.getValue("DESCRIPTION_FR_TXT"));
			licenseList.add(licenseInfo);
		}

		return licenseList;
	}

}