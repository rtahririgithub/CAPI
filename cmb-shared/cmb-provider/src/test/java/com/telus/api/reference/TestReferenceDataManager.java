package com.telus.api.reference;

import java.lang.reflect.InvocationTargetException;

import com.telus.api.BaseTest;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.utility.info.ServiceUsageInfo;
import com.telus.eas.utility.info.WorkPositionInfo;

public class TestReferenceDataManager extends BaseTest {
	ReferenceDataManager rdm;
	
    static {
       setupEASECA_QA();
    //	setupD3();
//    	setupSMARTDESKTOP_PT168();

		//Override ReferenceDataHelperproperty setting in LDAP (if needed)
//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:1001");
//		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:1001");

    }

    public void testRun () throws Exception {
    	getAccountType();
    	getAccountTypes();
    	getActivityType();
    	getActivityTypes();
    	getAddressType();
    	getAddressTypes();
    	getAdjustmentReason();
    	getAdjustmentReasons();
    	getAllFollowUpTypes();
    	getAllLanguages();
    	getAllMemoTypes();
    	getAllPrepaidRates();
    	getAllProvinces();
    	getAllTitles();
    	getAmountBarCodes();
    	//getApplicationSummary();
    	getAudienceType();
    	getBillCycle();
    	getBillCycles();
    	getBillHoldRedirectDestination();
    	getBillHoldRedirectDestinations();
    	getBrand();
    	getBrands();
    	getBusinessRole();
    	getBusinessRoles();
    	getClientConsentIndicator();
    	getClientConsentIndicators();
    	getClientStateReason();
    	getClientStateReasons();
    	getCollectionActivities();
    	getCollectionActivity();
    	getCollectionAgencies();
    	getCollectionAgency();
    	getCollectionPathDetails();
    	getCollectionPaths();
//    	getCollectionState();
    	getCollectionStepApproval();
    	getCollectionStepApprovals();
    	getCommitmentReason();
    	getCommitmentReasons();
    	getCorporateAccountRep();
    	getCorporateAccountReps();
    	getCountries();
    	getCountry();
    	getCoverageRegion();
    	getCoverageRegions();
    	getCreditCardPaymentType();
    	getCreditCardPaymentTypes();
    	getCreditCardType();
    	getCreditCardTypes();
    	getCreditCheckDepositChangeReason();
    	getCreditCheckDepositChangeReasons();
    	getCreditClasses();
    	getCreditMessage();
    	getCreditMessages();
    	getDepartment();
    	getDepartments();
    	getDiscountPlan();
    	getDiscountPlans();
    	getEncodingFormat();
    	getEncodingFormats();
    	getEquipmentPossession();
    	getEquipmentPossessions();
    	getEquipmentProductType();
    	getEquipmentProductTypes();
    	getEquipmentStatus();
    	getEquipmentStatuses();
    	getEquipmentType();
    	getEquipmentTypes();
    	getExceptionReason();
    	getExceptionReasons();
    	getFeature();
//    	getFeatureCategories();
//    	getFeatureCategory();
    	getFeatures();
    	getFeeWaiverReason();
    	getFeeWaiverReasons();
    	getFeeWaiverType();
    	getFeeWaiverTypes();
    	getFleetClass();
    	getFleetClasses();
    	getFollowUpCloseReason();
    	getFollowUpCloseReasons();
    	getFollowUpType();
    	getFollowUpTypes(); 
    	getGeneration();
    	getGenerations();
    	getInvoiceCallSortOrderType();
    	getInvoiceCallSortOrderTypes();
    	getInvoiceSuppressionLevel();
    	getInvoiceSuppressionLevels();
//    	getKnowbilityOperator();
    	getLanguage();
    	getLanguages();  
//    	getLetterCategories();
//    	getLetterCategory(); 
//    	getLetterSubCategories();
//    	getLetterSubCategory(); 
    	getLockReason();
    	getLockReasons(); 
    	getManualChargeType();
    	getManualChargeTypes();
    	getMemoType();
    	getMemoTypeCategories();
    	getMemoTypeCategory();
    	getMemoTypes();
    	getMigrationType();
    	getMigrationTypes();
    	getNetwork();
    	getNetworks();
    	getNetworkTypes();
//    	getNotificationMessageTemplateInfo();
//    	getNotificationMessageTemplateInfos();
    	getNotificationType();
    	getNotificationTypes();
//    	getNumberRange();
//    	getNumberRanges();
    	getPagerEquipmentType();
    	getPagerEquipmentTypes();
    	getPagerFrequencies();
    	getPagerFrequency();
    	getPaymentMethod();
    	getPaymentMethods();
    	getPaymentMethodType();
    	getPaymentMethodTypes();
    	getPaymentSourceType();
    	getPaymentSourceTypes();
    	getPaymentTransferReason();
    	getPaymentTransferReasons();
    	getPhoneType();
    	getPhoneTypes(); 
    	getPoolingGroup();
    	getPoolingGroups();
    	getPrepaidAdjustmentReason();
    	getPrepaidAdjustmentReason();
    	getPrepaidEventType();
    	getPrepaidEventTypes();
    	getPrepaidFeatureAddWaiveReason();
    	getPrepaidFeatureAddWaiveReasons();
    	getPrepaidManualAdjustmentReason();
    	getPrepaidManualAdjustmentReasons();
    	getPrepaidRechargeDenominations();
    	getPrepaidTopUpWaiveReason();
    	getPrepaidTopUpWaiveReasons();
    	getPricePlan();
//    	getPricePlanTermInfo();
    	getProductType();
    	getProductTypes();
//    	getPromoTerm();
//    	getProvince();
    	getProvinces();
//    	getProvisioningPlatformType();
//    	getProvisioningPlatformTypes();
    	getProvisioningTransactionStatus();
    	getProvisioningTransactionStatuses();
    	getProvisioningTransactionType();
    	getProvisioningTransactionTypes();
//    	getRegularService();
//    	getRegularServices();
    	getRoamingCapability();
    	getRoute();
    	getRoutes();
    	getRuralDeliveryType();
    	getRuralDeliveryTypes();
    	getRuralType();
    	getRuralTypes();
    	getSegmentation();
    	getSegmentations();
    	getServiceExclusionGroups();
    	getServiceExclusionGroups();
    	getServicePeriodType();
    	getServicePeriodTypes();
//    	getServicePolicyException();
//    	getServicePolicyExceptions();
    	getServiceUsageInfo();
    	getSID();
    	getSIDs();
//    	getSpecialNumber();
//    	getSpecialNumberRange();
//    	getSpecialNumberRanges();
//    	getSpecialNumbers();
    	getState();
    	getStates();
    	getStreetDirection();
    	getStreetDirections();
    	getSubscriptionRoleType();
    	getSubscriptionRoleTypes();
    	getTalkGroupPriorities();
    	getTaxationPolicies();
    	getTaxationPolicy();
//    	getTermUnit();
//    	getTermUnits();
    	getTitle();
    	getTitles();
    	getUnitType();
    	getUnitTypes();
    	getUsageRateMethod();
    	getUsageRateMethods();
    	getUsageRecordType();
    	getUsageRecordTypes();
    	getUsageUnit();
    	getUsageUnits();
    	getVendorService();	
    	getVendorServices();
    	getWorkFunctions();
    	getWorkPosition();	
//    	getWorkPositionsGroup();
//    	getWPSCategories();
//    	getWPSCategory();
//    	getWPSService();
//    	getWPSServices();
    }
    
    public TestReferenceDataManager(String name) throws Throwable {
        super(name);
        rdm = provider.getReferenceDataManager();
    }

	public void getMemoTypes() throws Exception {
		System.out.println("Memo Types retrieved = [" + rdm.getMemoTypes().length + "]");
	}
	
	public void getMemoType() throws Exception {
		System.out.println("Description retrieved from call to getMemoType - [" + 
				((Reference)invokeTest("getMemoType", "REFC")).getDescription() + "]");
	}	
	
	public void getMemoTypeCategories() throws Exception {
		System.out.println("Memo Type Categories retrieved = [" + rdm.getMemoTypeCategories().length + "]");
	}	
	
	public void getMemoTypeCategory() throws Exception {
		System.out.println("Description retrieved from call to getMemoTypeCategory - [" + 
				((Reference)invokeTest("getMemoTypeCategory", "FNAD")).getDescription() + "]");		
	}
		
	public void getMigrationTypes() throws Exception {
		System.out.println("Migration Types retrieved = [" + rdm.getMigrationTypes().length + "]");
	}
	
	public void getMigrationType() throws Exception {
		System.out.println("Description retrieved from call to getMigrationType - [" + 
				((Reference)invokeTest("getMigrationType", "PRPO")).getDescription() + "]");
	}
	
	public void getNetworks() throws Exception {
		System.out.println("Networks retrieved = [" + rdm.getNetworks().length + "]");
	}
	
	public void getNetwork() throws Exception {
		System.out.println("Description retrieved from call to getNetwork - [" + 
				((Reference)invokeTest("getNetwork", "2")).getDescription() + "]");
	}
	
	public void getNetworkTypes() throws Exception {
		System.out.println("Network Types retrieved = [" + rdm.getNetworkTypes().length + "]");
	}
	
//	TODO: determine if we still need to test this
//	public void getNotificationMessageTemplates() throws Exception {
//		System.out.println("Notification Message Templates retrieved = [" + rdm.getNotificationMessageTemplates().length + "]");
//	}
//	
//	public void getNotificationMessageTemplate() throws Exception {
//		System.out.println("Description retrieved from call to getNotificationMessageTemplate - [" + 
//				((Reference)invokeTest("getNotificationMessageTemplate", "35")).getDescription() + "]");
//	}
	
	public void getNotificationTypes() throws Exception {
		System.out.println("Notification Types retrieved = [" + rdm.getNotificationTypes().length + "]");
	}
	
	public void getNotificationType() throws Exception {
		System.out.println("Description retrieved from call to getNotificationType - [" + 
				((Reference)invokeTest("getNotificationType", "35")).getDescription() + "]");
	}
	
//	TODO: determine if we still need to test this
//	public void getNumberRanges() throws Exception {
//		System.out.println("Number Ranges retrieved = [" + rdm.getNumberRanges().length + "]");
//	}	
//	
//	public void getNumberRange() throws Exception {
//		System.out.println("Description retrieved from call to getNumberRange - [" + 
//				((Reference)invokeTest("getNumberRange", "604834")).getDescription() + "]");
//	}
	
	public void getPagerEquipmentTypes() throws Exception {
		System.out.println("Pager Equipment Types retrieved = [" + rdm.getPagerEquipmentTypes().length + "]");
	}	

	public void getPagerEquipmentType() throws Exception {
		System.out.println("Description retrieved from call to getPagerEquipmentType - [" + 
				((Reference)invokeTest("getPagerEquipmentType", "E")).getDescription() + "]");
	}	
	
	public void getPagerFrequencies() throws Exception {
		System.out.println("Pager Frequencies retrieved = [" + rdm.getPagerFrequencies().length + "]");
	}
	
	public void getPagerFrequency() throws Exception {
		System.out.println("Description retrieved from call to getPagerFrequency - [" + 
				((Reference)invokeTest("getPagerFrequency", "459.1125")).getDescription() + "]");
	}
	
	public void getPaymentMethods() throws Exception {
		System.out.println("Payment Methods retrieved = [" + rdm.getPaymentMethods().length + "]");
	}
	
	public void getPaymentMethod() throws Exception {
		System.out.println("Description retrieved from call to getPaymentMethod - [" + 
				((Reference)invokeTest("getPaymentMethod", "C")).getDescription() + "]");
	}
	
	public void getPaymentMethodTypes() throws Exception {
		System.out.println("Payment Method Types retrieved = [" + rdm.getPaymentMethodTypes().length + "]");
	}

	public void getPaymentMethodType() throws Exception {
		System.out.println("Description retrieved from call to getPaymentMethodType - [" + 
				((Reference)invokeTest("getPaymentMethodType", "CH")).getDescription() + "]");
	}
	
	public void getPaymentSourceTypes() throws Exception {
		System.out.println("Payment Source Types retrieved = [" + rdm.getPaymentSourceTypes().length + "]");
	}	
	
	public void getPaymentSourceType() throws Exception {
		System.out.println("Description retrieved from call to getPaymentSourceType - [" + 
				((Reference)invokeTest("getPaymentSourceType", "WTCH")).getDescription() + "]");
	}
	
	public void getPaymentTransferReasons() throws Exception {
		System.out.println("Payment Transfer Reasons retrieved = [" + rdm.getPaymentTransferReasons().length + "]");
	}	

	public void getPaymentTransferReason() throws Exception {
		System.out.println("Description retrieved from call to getPaymentTransferReason - [" + 
				((Reference)invokeTest("getPaymentTransferReason", "FUND  ")).getDescription() + "]");
	}
	
	public void getPhoneTypes() throws Exception {
		System.out.println("Phone Types retrieved = [" + rdm.getPhoneTypes().length + "]");
	}	
	
	public void getPhoneType() throws Exception {
		System.out.println("Description retrieved from call to getPhoneType - [" + 
				((Reference)invokeTest("getPhoneType", "FAX")).getDescription() + "]");
	}
	
	public void getPoolingGroups() throws Exception {
		System.out.println("Pooling Groups retrieved = [" + rdm.getPoolingGroups().length + "]");
	}
	
	public void getPoolingGroup() throws Exception {
		System.out.println("Description retrieved from call to getPoolingGroup - [" + 
				((Reference)invokeTest("getPoolingGroup", "1")).getDescription() + "]");
	}
	
	public void getPrepaidAdjustmentReasons() throws Exception {
		System.out.println("Prepaid Adjustment Reasons retrieved = [" + rdm.getPrepaidAdjustmentReason().length + "]");
	}
	
	public void getPrepaidAdjustmentReason() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidAdjustmentReason - [" + 
				((Reference)invokeTest("getPrepaidAdjustmentReason", "159")).getDescription() + "]");
	}
	
	public void getPrepaidEventTypes() throws Exception {
		System.out.println("Prepaid Event Types retrieved = [" + rdm.getPrepaidEventTypes().length + "]");
	}
	
	public void getPrepaidEventType() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidEventType - ["	+ 
				((Reference) invokeTest("getPrepaidEventType", "-922")).getDescription() + "]");
	}
	
	public void getPrepaidFeatureAddWaiveReasons() throws Exception {
		System.out.println("Prepaid Feature Add Waive Reasons retrieved = [" + rdm.getPrepaidFeatureAddWaiveReasons().length + "]");		
	}
	
	public void getPrepaidFeatureAddWaiveReason() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidFeatureAddWaiveReason - [" + 
				((Reference)invokeTest("getPrepaidFeatureAddWaiveReason", "240")).getDescription() + "]");
	}
	
	public void getPrepaidManualAdjustmentReasons() throws Exception {
		System.out.println("Prepaid Manual Adjustment Reasons retrieved = [" + rdm.getPrepaidManualAdjustmentReasons().length + "]");
	}
	
	public void getPrepaidManualAdjustmentReason() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidManualAdjustmentReason - [" + 
				((Reference)invokeTest("getPrepaidManualAdjustmentReason", "116")).getDescription() + "]");
	}
	
	public void getPrepaidRechargeDenominations() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidRechargeDenominations - [" + 
				((Reference[])invokeTest("getPrepaidRechargeDenominations", "RTU"))[0].getDescription() + "]");
	}
	
	public void getPrepaidTopUpWaiveReasons() throws Exception {
		System.out.println("Prepaid TopUp Waive Reasons retrieved = [" + rdm.getPrepaidTopUpWaiveReasons().length + "]");
	}
	
	public void getPrepaidTopUpWaiveReason() throws Exception {
		System.out.println("Description retrieved from call to getPrepaidTopUpWaiveReason - [" + 
				((Reference)invokeTest("getPrepaidTopUpWaiveReason", "220")).getDescription() + "]");
	}
	
	public void getPricePlan() throws Exception {
		System.out.println("Description retrieved from call to getPricePlan - [" + 
				((Reference)invokeTest("getPricePlan", "PBS60MPWC")).getDescription() + "]");
	}
	
	//TODO: find data to execute with
//	@Test
//	public void getPricePlanTermInfo() throws Exception {
//		System.out.println(rdm.getPricePlanTermInfo("PSHARE10"));
//	}
	
	public void getProductTypes() throws Exception {
		System.out.println("Product Types retrieved = [" + rdm.getProductTypes().length + "]");
	}
	
	public void getProductType() throws Exception {
		System.out.println("Description retrieved from call to getProductType - [" + 
				((Reference)invokeTest("getProductType", "D")).getDescription() + "]");
	}
	
//	@Test
//	public void getPromoTerm() throws Exception {
//		System.out.println("Description retrieved from call to getPromoTerm - [" + 
//				((Reference)invokeTest("getPromoTerm", "ETOTL25M")).getDescription() + "]");
//	}
	
	public void getProvinces() throws Exception {
		System.out.println("Provinces retrieved = [" + rdm.getProvinces().length + "]");		
	}
	
	public void getProvince_NON_CAN() throws Exception {  //Test for non-Canadian province
		System.out.println("Description retrieved from call to getProvince - [" + 
				((Reference)invokeTest("getProvince", "VT")).getDescription() + "]");
	}
	
	public void getProvince_CAN() throws Exception {  //Test for Canadian province
		System.out.println("Description retrieved from call to getProvince - [" + 
				((Reference)invokeTest("getProvince", "ON")).getDescription() + "]");
	}

//	TODO: do we need to test this?
//	public void getProvisioningPlatformTypes() throws Exception {
//		System.out.println("Provisioning Platform Types retrieved = [" + rdm.getProvisioningPlatformTypes().length + "]");
//	}
//
//	public void getProvisioningPlatformType() throws Exception {
//		System.out.println("Description retrieved from call to getProvisioningPlatformType - [" + 
//				((Reference)invokeTest("getProvisioningPlatformType", "3")).getDescription() + "]");
//	}
//	
	public void getProvisioningTransactionStatuses() throws Exception {
		System.out.println("Provisioning Transaction Statuses retrieved = [" + rdm.getProvisioningTransactionStatuses().length + "]");
	}
	
	public void getProvisioningTransactionStatus() throws Exception {
		System.out.println("Description retrieved from call to getProvisioningTransactionStatus - [" + 
				((Reference)invokeTest("getProvisioningTransactionStatus", "WR")).getDescription() + "]");
	}
	
	public void getProvisioningTransactionTypes() throws Exception {
		System.out.println("Provisioning Transaction Types retrieved = [" + rdm.getProvisioningTransactionTypes().length + "]");
	}

	public void getProvisioningTransactionType() throws Exception {
		System.out.println("Description retrieved from call to getProvisioningTransactionType - [" + 
				((Reference)invokeTest("getProvisioningTransactionType", "RSM")).getDescription() + "]");
	}
	
//	@Test
//	public void getRegularServices() throws Exception {
//		System.out.println("getRegularServices retrieved = [" + rdm.getRegularServices().length + "]");
//	}
//	
//	@Test
//	public void getRegularService() throws Exception {
//		System.out.println("Description retrieved from call to getRegularService - [" + 
//				((Reference)invokeTest("getRegularService", "SULC3MAL")).getDescription() + "]");
//	}
	
	public void getRoamingCapability() throws Exception {
		System.out.println("Roaming Capability retrieved = [" + rdm.getRoamingCapability().length + "]");
	}	
	
	public void getRoutes() throws Exception {
		System.out.println("Routes retrieved = [" + rdm.getRoutes().length + "]");
	}
	
	public void getRoute() throws Exception {
		System.out.println(rdm.getRoute("000021", "00402001").getCountry());
	}	
	
	public void getRuralDeliveryTypes() throws Exception {
		System.out.println("Rural Delivery Types retrieved = [" + rdm.getRuralDeliveryTypes().length + "]");
	}
	
	public void getRuralDeliveryType() throws Exception {
		System.out.println("Description retrieved from call to getRuralDeliveryType - [" + 
				((Reference)invokeTest("getRuralDeliveryType", "PO")).getDescription() + "]");
	}

	public void getRuralTypes() throws Exception {
		System.out.println("Rural Types retrieved = [" + rdm.getRuralTypes().length + "]");
	}
	
	public void getRuralType() throws Exception {
		System.out.println("Description retrieved from call to getRuralType - [" + 
				((Reference)invokeTest("getRuralType", "G")).getDescription() + "]");
	}

	public void getSegmentations() throws Exception {
		System.out.println("Segmentations retrieved = [" + rdm.getSegmentations().length + "]");
	}
	
	public void getSegmentation() throws Exception {
		System.out.println("Description retrieved from call to getSegmentation - [" + 
				invokeTest("getSegmentation", "2**") + "]");
	}
	
	public void getServiceExclusionGroups() throws Exception {
		System.out.println("Service Exclusion Groups retrieved = [" + rdm.getServiceExclusionGroups().length + "]");
	}
	
	public void getServiceExclusionGroup() throws Exception {
		System.out.println("Description retrieved from call to getServiceExclusionGroups - [" + 
				invokeTest("getServiceExclusionGroups", "3SBB25") + "]");
	}
	
	public void getServicePeriodTypes() throws Exception {
		System.out.println("Service Period Types retrieved = [" + rdm.getServicePeriodTypes().length + "]");
	}
	
	public void getServicePeriodType() throws Exception {
		System.out.println("Description retrieved from call to getServicePeriodType - [" + 
				((Reference)invokeTest("getServicePeriodType", "19")).getDescription() + "]");
	}

//	TODO: do we need to test this?
//	public void getServicePolicyExceptions() throws Exception {
//		System.out.println("getServicePolicyExceptions retrieved = [" + rdm.getServicePolicyExceptions().length + "]");
//	}
//	
//	public void getServicePolicyException() throws Exception {
//		System.out.println("Description retrieved from call to getServicePolicyException - [" + 
//				((Reference)invokeTest("getServicePolicyException", "PSMMTMBP2IVRCLIENTADD")).getDescription() + "]");
//	}
	
	public void getServiceUsageInfo() throws Exception {
		System.out.println("Service Code retrieved from call to getServiceUsageInfo - [" + 
				((ServiceUsageInfo)invokeTest("getServiceUsageInfo", "13A")).getServiceCode() + "]");
	}
	
	public void getSIDs() throws Exception {
		System.out.println("SIDs retrieved = [" + rdm.getSIDs().length + "]");
	}
	
	public void getSID() throws Exception {
		System.out.println("Description retrieved from call to getSID - [" + 
				((Reference)invokeTest("getSID", "01058")).getDescription() + "]");
	}
	
//	TODO: do we need to test this?
//	public void getSpecialNumbers() throws Exception {
//		System.out.println("Special Numbers retrieved = [" + rdm.getSpecialNumbers().length + "]");		
//	}
//
//	public void getSpecialNumber() throws Exception {
//		System.out.println("Description retrieved from call to getSpecialNumber - [" + 
//				((Reference)invokeTest("getSpecialNumber", "0000000748")).getDescription() + "]");
//	}

//	TODO: do we need to test this?
//	public void getSpecialNumberRanges() throws Exception {
//		System.out.println("Special Number Ranges retrieved = [" + rdm.getSpecialNumberRanges().length + "]");
//	}
//
//	public void getSpecialNumberRange() throws Exception {
//		System.out.println("Description retrieved from call to getSpecialNumberRange - [" + 
//				((Reference)invokeTest("getSpecialNumberRange", "8662879190")).getDescription() + "]"); // number fits into this range --> 8660000000-8662879195
//	}

	public void getStates() throws Exception {
		System.out.println("States retrieved = [" + rdm.getStates().length + "]");
	}

	public void getState() throws Exception {
		System.out.println("Description retrieved from call to getState - [" + 
				((Reference)invokeTest("getState", "RI")).getDescription() + "]");
	}
	
	public void getStreetDirections() throws Exception {
		System.out.println("Street Directions retrieved = [" + rdm.getStreetDirections().length + "]");
	}	
	
	public void getStreetDirection() throws Exception {
		System.out.println("Description retrieved from call to getStreetDirection - [" + 
				((Reference)invokeTest("getStreetDirection", "E")).getDescription() + "]");
	}
	
	public void getSubscriptionRoleTypes() throws Exception {
		System.out.println("Subscription Role Types retrieved = [" + rdm.getSubscriptionRoleTypes().length + "]");
	}	
	
	public void getSubscriptionRoleType() throws Exception {
		System.out.println("Description retrieved from call to getSubscriptionRoleType - [" + 
				((Reference)invokeTest("getSubscriptionRoleType", "SC")).getDescription() + "]");
	}
	
	public void getTalkGroupPriorities() throws Exception {
		System.out.println("Talk Group Priorities retrieved = [" + rdm.getTalkGroupPriorities().length + "]");
	}
	
	public void getTaxationPolicies() throws Exception {
		System.out.println("Taxation Policies retrieved = [" + rdm.getTaxationPolicies().length + "]");
	}
	
	public void getTaxationPolicy() throws Exception {
		System.out.println("Description retrieved from call to getTaxationPolicy - [" + 
				((Reference)invokeTest("getTaxationPolicy", "ON")).getDescription() + "]");
	}
	
//	@Test
//	public void getTermUnits() throws Exception {
//		System.out.println(rdm.getTermUnits());
//	}

//	@Test
//	public void getTermUnit() throws Exception {
//		System.out.println(rdm.getTermUnit(""));
//	}
	
	public void getTitles() throws Exception {
		System.out.println("Titles retrieved = [" + rdm.getTitles().length + "]");
	}
	
	public void getTitle() throws Exception {
		System.out.println("Description retrieved from call to getTitle - [" + 
				((Reference)invokeTest("getTitle", "DR.")).getDescription() + "]");
	}

	public void getUnitTypes() throws Exception {
		System.out.println("Unit Types retrieved = [" + rdm.getUnitTypes().length + "]");
	}
	
	public void getUnitType() throws Exception {
		System.out.println("Description retrieved from call to getUnitType - [" + 
				((Reference)invokeTest("getUnitType", "FRNT")).getDescription() + "]");
	}
	
	public void getUsageRateMethods() throws Exception {
		System.out.println("Usage Rate Methods retrieved = [" + rdm.getUsageRateMethods().length + "]");
	}
	
	public void getUsageRateMethod() throws Exception {
		System.out.println("Description retrieved from call to getUsageRateMethod - [" + 
				((Reference)invokeTest("getUsageRateMethod", "T")).getDescription() + "]");
	}

	public void getUsageRecordTypes() throws Exception {
		System.out.println("Usage Record Types retrieved = [" + rdm.getUsageRecordTypes().length + "]");
	}
	
	public void getUsageRecordType() throws Exception {
		System.out.println("Description retrieved from call to getUsageRecordType - [" + 
				((Reference)invokeTest("getUsageRecordType", "01")).getDescription() + "]");
	}
	
	public void getUsageUnits() throws Exception {
		System.out.println("Usage Units retrieved = [" + rdm.getUsageUnits().length + "]");
	}
	
	public void getUsageUnit() throws Exception {
		System.out.println("Description retrieved from call to getUsageUnit - [" + 
				((Reference)invokeTest("getUsageUnit", "C")).getDescription() + "]");
	}
	
	public void getVendorServices() throws Exception {
		System.out.println("Vendor Services retrieved = [" + rdm.getVendorServices().length + "]");
		System.out.println("getVendorServices retrieved = [" + rdm.getVendorServices().length + "]");
		System.out.println("getVendorServices retrieved = [" + rdm.getVendorServices()[1].getCode() + "]");
	}
	
	public void getVendorService() throws Exception {
		System.out.println("Description retrieved from call to getVendorService - [" + 
				((Reference)invokeTest("getVendorService", "EW")).getDescription() + "]");
	}
	
	public void getWorkFunctions() throws Exception {
		System.out.println("Work Functions retrieved = [" + rdm.getWorkFunctions().length + "]");
	}
	
	public void getWorkPosition() throws Exception {
		System.out.println("Department Code retrieved from call to getWorkPosition - [" + 
				((WorkPositionInfo)invokeTest("getWorkPosition", "10001")).getDepartmentCode() + "]");
	}
	
//	@Test
//	public void getWPSServices() throws Exception {
//		System.out.println(rdm.getWPSServices().length);
//	}
	
	//	========= Andrew Test cases end =====================================	

	//	========= Pavel Test cases begin ====================================	
	
	public void getActivityTypes() throws Exception {
		System.out.println("Activity Types retrieved = [" + rdm.getActivityTypes().length + "]");
	}
	
	public void getActivityType() {
		System.out.println("Description retrieved from call to getActivityType - [" + 
				((Reference)invokeTest("getActivityType", "F01")).getDescription() + "]");
	}
	
	public void getAddressTypes() throws Exception {
		System.out.println("Address Types retrieved = [" + rdm.getAddressTypes().length + "]");		
	}
	
	public void getAddressType() throws Exception {
		System.out.println("Description retrieved from call to getAddressType - [" + 
				((Reference)invokeTest("getAddressType", "D")).getDescription() + "]");
	}
	
	public void getAdjustmentReasons() throws Exception {
		System.out.println("Adjustment Reasons retrieved = [" + rdm.getAdjustmentReasons().length + "]");
	}
	
	public void getAdjustmentReason() throws Exception {
		System.out.println("Description retrieved from call to getAdjustmentReason - [" + 
				((Reference)invokeTest("getAdjustmentReason", "CACTA ")).getDescription() + "]");
	}
	
	public void getAllFollowUpTypes() throws Exception {
		System.out.println("All FollowUp Types retrieved = [" + rdm.getAllFollowUpTypes().length + "]");
	}
	
	public void getAllLanguages()  throws Exception {
		System.out.println("All Languages retrieved = [" + rdm.getAllLanguages().length + "]");
	}
	
	public void getAllMemoTypes() throws Exception {
		System.out.println("All Memo Types retrieved = [" + rdm.getAllMemoTypes().length + "]");
	}
	
	public void getAllPrepaidRates() throws Exception {
		System.out.println("All Prepaid Rates retrieved = [" + rdm.getAllPrepaidRates().length + "]");
	}
	
	public void getAllProvinces() throws Exception {
		System.out.println("All Provinces retrieved = [" + rdm.getAllProvinces().length + "]");
	}
	
	public void getAllTitles() throws Exception {
		System.out.println("All Titles retrieved = [" + rdm.getAllTitles().length + "]");
	}
	
	public void getAmountBarCodes() throws Exception {
		System.out.println("Amount Bar Codes retrieved = [" + rdm.getAmountBarCodes().length + "]");
	}
	
	public void getApplicationSummary() throws Exception {
		System.out.println("Description retrieved from call to getApplicationSummary - [" + 
				((Reference)invokeTest("getApplicationSummary", "WSERVE")).getDescription() + "]");
	}
	
	public void getAudienceType() throws Exception {
		System.out.println("Description retrieved from call to getAudienceType - [" + 
				((Reference)invokeTest("getAudienceType", "CORPORATESTORE")).getDescription() + "]");
	}
	
	public void getBillCycles() throws Exception {
		System.out.println("Bill Cycles retrieved = [" + rdm.getBillCycles().length + "]");
	}
	
	public void getBillCycle() throws Exception {
		System.out.println("Description retrieved from call to getBillCycles - [" + 
				((Reference)invokeTest("getBillCycle", "57")).getDescription() + "]");
	}
	
	public void getBillHoldRedirectDestinations() throws Exception {
		System.out.println("Bill Hold Redirect Destinations retrieved = [" + rdm.getBillHoldRedirectDestinations().length + "]");
	}
	
	public void getBillHoldRedirectDestination() throws Exception {
		System.out.println("Description retrieved from call to getBillHoldRedirectDestination - [" + 
				((Reference)invokeTest("getBillHoldRedirectDestination", "3")).getDescription() + "]");
	}
	
	public void getBrands() throws Exception {
		System.out.println("Brands retrieved = [" + rdm.getBrands().length + "]");
	}
	
	public void getBrand() throws Exception {
		System.out.println("Description retrieved from call to getBrand - [" + 
				((Reference)invokeTest("getBrand", "1")).getDescription() + "]");
	}
	
	public void getBusinessRoles() throws Exception {
		System.out.println("Business Roles retrieved = [" + rdm.getBusinessRoles().length + "]");
	}
	
	public void getBusinessRole() throws Exception {
		System.out.println("Description retrieved from call to getBusinessRole - [" + 
				((Reference)invokeTest("getBusinessRole", "CNTRENEWAL")).getDescription() + "]");
	}
	
	public void getClientConsentIndicators() throws Exception {
		System.out.println("Client Consent Indicators retrieved = [" + rdm.getClientConsentIndicators().length + "]");		
	}
	
	public void getClientConsentIndicator() throws Exception {
		System.out.println("Description retrieved from call to getClientConsentIndicator - [" + 
				((Reference)invokeTest("getClientConsentIndicator", "2 ")).getDescription() + "]");
	}
	 
	public void getClientStateReasons() throws Exception {
		System.out.println("Client State Reasons retrieved = [" + rdm.getClientStateReasons().length + "]");
	}
	
	public void getClientStateReason() throws Exception {
		System.out.println("Description retrieved from call to getClientStateReason - [" + 
				((Reference)invokeTest("getClientStateReason", "CAN_ZSUS")).getDescription() + "]");	
	}
	
	public void getCollectionActivities() throws Exception {
		System.out.println("Collection Activities retrieved = [" + rdm.getCollectionActivities().length + "]");
	}
	
	public void getCollectionActivity() throws Exception {
		System.out.println("Description retrieved from call to getCollectionActivity - [" + 
				((Reference)invokeTest("getCollectionActivity", "D")).getDescription() + "]");
	}

	public void getCollectionAgencies() throws Exception {
		System.out.println("Collection Agencies retrieved = [" + rdm.getCollectionAgencies().length + "]");
	}
	
	public void getCollectionAgency() throws Exception {
		System.out.println("Description retrieved from call to getCollectionAgency - [" + 
				((Reference)invokeTest("getCollectionAgency", "INTRNL")).getDescription() + "]");
	}
	
	public void getCollectionPaths() throws Exception {
		System.out.println("Collection Paths retrieved = [" + rdm.getCollectionPaths().length + "]");
	}
	
	public void getCollectionPathDetails() throws Exception {
		System.out.println("Letter title retrieved from call to getCollectionPathDetails - [" + 
				((CollectionPathDetail[])invokeTest("getCollectionPathDetails", "AB1"))[0].getLetterTitle() + "]");
	}
	
//	TODO: do we need to test this?
//	public void getCollectionStates() throws Exception {
//		System.out.println("Collection States retrieved = [" + rdm.getCollectionStates().length + "]");
//		System.out.println("getCollectionStates retrieved = [" + rdm.getCollectionStates()[0].getCode() + "]");
//	}
//	
//	public void getCollectionState() throws Exception {
//		System.out.println("Description retrieved from call to getCollectionState - [" + 
//				((Reference)invokeTest("getCollectionState", "PP31")).getDescription() + "]");
//	}
	
	public void getCollectionStepApprovals() throws Exception {
		System.out.println("Collection Step Approvals retrieved = [" + rdm.getCollectionStepApprovals().length + "]");
	}
	
	public void getCollectionStepApproval() throws Exception {
		System.out.println("Description retrieved from call to getCollectionStepApproval - [" + 
				((Reference)invokeTest("getCollectionStepApproval", "P")).getDescription() + "]");
	}
	
	public void getCommitmentReasons() throws Exception {
		System.out.println("Commitment Reasons retrieved = [" + rdm.getCommitmentReasons().length + "]");
	}
	
	public void getCommitmentReason() throws Exception {
		System.out.println("Description retrieved from call to getCommitmentReason - [" + 
				((Reference)invokeTest("getCommitmentReason", "EQU")).getDescription() + "]");
	}
	
	public void getCorporateAccountReps() throws Exception {
	System.out.println("Corporate Account Reps retrieved = [" + rdm.getCorporateAccountReps().length + "]");	
	}
	
	public void getCorporateAccountRep() throws Exception {
		System.out.println("Description retrieved from call to getCorporateAccountReps - [" + 
				((Reference)invokeTest("getCorporateAccountRep", "T114")).getDescription() + "]");
	}
	
	public void getCountries() throws Exception {
		System.out.println("Countries (not including foreign) retrieved = [" + rdm.getCountries(false).length + "]");
		System.out.println("Countries (including foreign) retrieved = [" + rdm.getCountries(true).length + "]");
	}
	
	public void getCountry() throws Exception {
		System.out.println("Description retrieved from call to getCountry - [" + 
				((Reference)invokeTest("getCountry", "GY ")).getDescription() + "]");
	}
	
	public void getCoverageRegions() throws Exception {
		System.out.println("Coverage Regions retrieved = [" + rdm.getCoverageRegions().length + "]");
	}
	
	public void getCoverageRegion() throws Exception {
		System.out.println("Description retrieved from call to getCoverageRegion - [" + 
				((Reference)invokeTest("getCoverageRegion", "2009")).getDescription() + "]");
	}
	
	public void getCreditCardPaymentTypes() throws Exception {
		System.out.println("Credit Card Payment Types retrieved = [" + rdm.getCreditCardPaymentTypes().length + "]");
	}
	
	public void getCreditCardPaymentType() throws Exception {
		System.out.println("Description retrieved from call to getCreditCardPaymentType - [" + 
				((Reference)invokeTest("getCreditCardPaymentType", "VS")).getDescription() + "]");
	}
	
	public void getCreditCardTypes() throws Exception {
		System.out.println("Credit Card Types retrieved = [" + rdm.getCreditCardTypes().length + "]");
	}
	
	public void getCreditCardType() throws Exception {
		System.out.println("Description retrieved from call to getCreditCardType - [" + 
				((Reference)invokeTest("getCreditCardType", "2")).getDescription() + "]");
	}
	
	public void getCreditCheckDepositChangeReasons() throws Exception {
		System.out.println("Credit Check Deposit Change Reasons retrieved = [" + rdm.getCreditCheckDepositChangeReasons().length + "]");	 
	}
	
	
	public void getCreditCheckDepositChangeReason() throws Exception {
		System.out.println("Description retrieved from call to getCreditCheckDepositChangeReason - [" + 
				((Reference)invokeTest("getCreditCheckDepositChangeReason", "08")).getDescription() + "]");
	}
	
	public void getCreditClasses() throws Exception {
		System.out.println("Credit Classes retrieved = [" + rdm.getCreditClasses().length + "]");
	}
	
	public void getCreditMessages() throws Exception {
		System.out.println("Credit Messages retrieved = [" + rdm.getCreditMessages().length + "]");
	}
	
	public void getCreditMessage() throws Exception {
		System.out.println("Description retrieved from call to getCreditMessage - [" + 
				((Reference)invokeTest("getCreditMessage", "C61")).getDescription() + "]");
	}
	
	public void getDepartments() throws Exception {
		System.out.println("Departments retrieved = [" + rdm.getDepartments().length + "]");
	}
	
	public void getDepartment() throws Exception {
		System.out.println("Description retrieved from call to getDepartment - [" + 
				((Reference)invokeTest("getDepartment", "DSAL")).getDescription() + "]");
	}
	
	public void getDiscountPlans() throws Exception {
		System.out.println("Discount Plans (current only) retrieved = [" + rdm.getDiscountPlans(true).length + "]");
		System.out.println("Discount Plans retrieved = [" + rdm.getDiscountPlans(false).length + "]");
	}
	
	public void getDiscountPlan() throws Exception {
		System.out.println("Description retrieved from call to getDiscountPlan - [" + 
				((Reference)invokeTest("getDiscountPlan", "X134     ")).getDescription() + "]");
	}
	
	public void getEncodingFormats() throws Exception {
		System.out.println("Encoding Formats retrieved = [" + rdm.getEncodingFormats().length + "]");
	}
	
	public void getEncodingFormat() throws Exception {
		System.out.println("Description retrieved from call to getEncodingFormat - [" + 
				((Reference)invokeTest("getEncodingFormat", "E")).getDescription() + "]");
	}
	
	public void getEquipmentPossessions() throws Exception {
		System.out.println("Equipment Possessions retrieved = [" + rdm.getEquipmentPossessions().length + "]");
	}
	
	public void getEquipmentPossession() throws Exception {
		System.out.println("Description retrieved from call to getEquipmentPossession - [" + 
				((Reference)invokeTest("getEquipmentPossession", "D")).getDescription() + "]");
	}
	
	public void getEquipmentProductTypes() throws Exception {
		System.out.println("Equipment Product Types retrieved = [" + rdm.getEquipmentProductTypes().length + "]");
	}
	
	public void getEquipmentProductType() throws Exception {
		System.out.println("Description retrieved from call to getEquipmentProductType - [" + 
				((Reference)invokeTest("getEquipmentProductType", "ABC1")).getDescription() + "]");
	}
	
	public void getEquipmentStatuses()throws Exception {
		System.out.println("Equipment Statuses retrieved = [" + rdm.getEquipmentStatuses().length + "]");
	}
	
	public void getEquipmentStatus()throws Exception {
		System.out.println("Description retrieved from call to getEquipmentStatus - [" + 
				((Reference)rdm.getEquipmentStatus(10,3)).getDescription() + "]");
	}
	
	public void getEquipmentTypes() throws Exception {
		System.out.println("Equipment Types retrieved = [" + rdm.getEquipmentTypes().length + "]");
	}
	
	public void getEquipmentType() throws Exception {
		System.out.println("Description retrieved from call to getEquipmentType - [" + 
				((Reference)invokeTest("getEquipmentType", "3")).getDescription() + "]");
	}
	
	public void getExceptionReasons() throws Exception {
		System.out.println("Exception Reasons retrieved = [" + rdm.getExceptionReasons().length + "]");
	}
	
	public void getExceptionReason() throws Exception {
		System.out.println("Description retrieved from call to getExceptionReason - [" + 
				((Reference)invokeTest("getExceptionReason", "INSM")).getDescription() + "]");
	}
	
	public void getFeatures() throws Exception {
		System.out.println("Features retrieved = [" + rdm.getFeatures().length + "]");
	}
	
	public void getFeature() throws Exception {
		System.out.println("Description retrieved from call to getFeature - [" + 
				((Reference)invokeTest("getFeature", "UNQMMS")).getDescription() + "]");	
	}
	
//	TODO: do we still need to test these?
//	public void getFeatureCategories() throws Exception {
//		System.out.println("Feature Categories retrieved = [" + rdm.getFeatureCategories().length + "]");
//	}
//	
//	public void getFeatureCategory() throws Exception {
//		System.out.println("Category Code retrieved from call to getFeatureCategory - [" + 
//				((FeatureInfo)invokeTest("getFeatureCategory", "VM18BC")).getCategoryCode() + "]");
//	}
	
	public void getFeeWaiverReasons() throws Exception {
		System.out.println("Fee Waiver Reasons retrieved = [" + rdm.getFeeWaiverReasons().length + "]");
	}
	
	public void getFeeWaiverReason() throws Exception {
		System.out.println("Description retrieved from call to getFeeWaiverReason - [" + 
				((Reference)invokeTest("getFeeWaiverReason", "FEW")).getDescription() + "]");
	}
	
	public void getFeeWaiverTypes() throws Exception {
		System.out.println("getFeeWaiverTypes retrieved = [" + rdm.getFeeWaiverTypes().length + "]");
	}
	
	public void getFeeWaiverType() throws Exception {
		System.out.println("Description retrieved from call to getFeeWaiverType - [" + 
				((Reference)invokeTest("getFeeWaiverType", "WLPC")).getDescription() + "]");
	}
	
	public void getFleetClasses() throws Exception {
		System.out.println("Fleet Classes retrieved = [" + rdm.getFleetClasses().length + "]");
	}
	
	public void getFleetClass() throws Exception {
		System.out.println("Maximum Member ID In Range retrieved from call to getFleetClass - [" + 
				((FleetClassInfo)invokeTest("getFleetClass", "D")).getMaximumMemberIdInRange() + "]");
	}
	
	public void getFollowUpCloseReasons() throws Exception {
		System.out.println("Follow Up Close Reasons retrieved = [" + rdm.getFollowUpCloseReasons().length + "]");
	}
	
	public void getFollowUpCloseReason() throws Exception {
		System.out.println("Description retrieved from call to getFollowUpCloseReason - [" + 
				((Reference)invokeTest("getFollowUpCloseReason", "17   ")).getDescription() + "]");
	}
	
	public void getFollowUpTypes() throws Exception {
		System.out.println("Follow Up Types retrieved = [" + rdm.getFollowUpTypes().length + "]"); 
	}
	
	public void getFollowUpType() throws Exception {
		System.out.println("Description retrieved from call to getFollowUpType - [" + 
				((Reference)invokeTest("getFollowUpType", "RADD")).getDescription() + "]");
	}
	
	public void getGenerations() throws Exception {
		System.out.println("Generations retrieved = [" + rdm.getGenerations().length + "]");
	}
	
	public void getGeneration() throws Exception {
		System.out.println("Description retrieved from call to getGeneration - [" + 
				((Reference)invokeTest("getGeneration", "II")).getDescription() + "]");
	}
	
	public void getInvoiceCallSortOrderTypes() throws Exception {
		System.out.println("Invoice Call Sort Order Types retrieved = [" + rdm.getInvoiceCallSortOrderTypes().length + "]");
	}
	
	public void getInvoiceCallSortOrderType() throws Exception {
		System.out.println("Description retrieved from call to getInvoiceCallSortOrderType - [" + 
				((Reference)invokeTest("getInvoiceCallSortOrderType", "C")).getDescription() + "]");
	}
	
	public void getInvoiceSuppressionLevels() throws Exception {
		System.out.println("Invoice Suppression Levels retrieved = [" + rdm.getInvoiceSuppressionLevels().length + "]");
	}
	
	public void getInvoiceSuppressionLevel() throws Exception {
		System.out.println("Description retrieved from call to getInvoiceSuppressionLevel - [" + 
				((Reference)invokeTest("getInvoiceSuppressionLevel", "3")).getDescription() + "]");
	}
	
//	TODO: do we still need to test these?
//	public void getKnowbilityOperators() throws Exception {
//		System.out.println("getKnowbilityOperators retrieved = [" + ((rdmImpl)rdm).getKnowbilityOperators().length + "]");
//	}
//	
//	public void getKnowbilityOperator() throws Exception {
//		System.out.println("Description retrieved from call to getKnowbilityOperator - [" + 
//				((Reference)invokeTest("getKnowbilityOperator", "2006")).getDescription() + "]");
//	}
	
	public void getLanguages() throws Exception {
		System.out.println("Languages retrieved = [" + rdm.getLanguages().length + "]");
	}
	
	public void getLanguage() throws Exception {
		System.out.println("Description retrieved from call to getLanguage - [" + 
				((Reference)invokeTest("getLanguage", "FR")).getDescription() + "]");
	}
	
//	public void getLetterCategories() throws Exception {
//		System.out.println("Letter Categories retrieved = [" + rdm.getLetterCategories().length + "]");
//	}
	
//	public void getLetterCategory() throws Exception {
//		System.out.println("Description retrieved from call to getLetterCategory - [" + 
//				((Reference)invokeTest("getLetterCategory", "CAM")).getDescription() + "]");
//	}
//	
//	TODO: do we still need to test these?
//	public void getLetterSubCategories()  throws Exception {
//		System.out.println("Letter Sub Categories retrieved = [" + ((rdmImpl)rdm).getLetterSubCategories().length + "]");
//	}
//	
//	public void getLetterSubCategory()  throws Exception {
//		System.out.println("Description retrieved from call to getLetterSubCategory - [" + 
//				((Reference)invokeTest("getLetterSubCategory", "Q3R")).getDescription() + "]");
//	}
	
	public void getLockReasons() throws Exception {
		System.out.println("Lock Reasons retrieved = [" + rdm.getLockReasons().length + "]");
	}

	public void getLockReason() throws Exception {
		System.out.println("Description retrieved from call to getLockReason - [" + 
				((Reference)invokeTest("getLockReason", "3038")).getDescription() + "]");
	}
	
	public void getManualChargeTypes() throws Exception {
		System.out.println("Manual Charge Types retrieved = [" + rdm.getManualChargeTypes().length + "]");
	}
	
	public void getManualChargeType() throws Exception {
		System.out.println("Description retrieved from call to getManualChargeType - [" + 
				((Reference)invokeTest("getManualChargeType", "SALF  ")).getDescription() + "]");
	}
	
	public void getAccountTypes() throws Exception {
		System.out.println("AccountTypes retrieved = [" + rdm.getAccountTypes().length + "]");
	}
	
	public void getAccountType() throws Exception {
		System.out.println("Description retrieved from call to getAccountType - [" + 
				rdm.getAccountType("EC", 1).getDescription() + "]");
	}
	
	//	========= Pavel Test cases end ======================================	
	
	private Object invokeTest(String methodName, String searchKey) {
		java.lang.reflect.Method method;
		Object happyPathResult=null;
		Object altPathResult=null;
		
		Class params[] = new Class[1];
		params[0] = String.class;
		
		try {
			method = rdm.getClass().getMethod(methodName, params);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
		
		//Test happy path
		happyPathResult = invokeMethod(method, searchKey);
		
		assertNull(happyPathResult);
		
		//Test empty parameter
		try {			
			altPathResult = invokeMethod(method, "");
			System.out.println("altPathResult"+altPathResult);
			assertNull(altPathResult);
		} catch (AssertionError ae) {
			//Add additional assertion check to handle methods that return empty array of objects (instead of null)
			assertTrue(((Object[])altPathResult).length==0);
		}
		
		//Test null parameter
		try {
			altPathResult = invokeMethod(method, null);
			//assertNull(altPathResult);
		} catch (AssertionError ae) {
			//Add additional assertion check to handle methods that return empty array of objects (instead of null)
			assertTrue(((Object[])altPathResult).length==0);
		}
				
		//Test fictitious text parameter
		try {
			altPathResult = invokeMethod(method, "BLAH");
			assertNotNull(altPathResult);
		} catch (AssertionError ae) {
			//Add additional assertion check to handle methods that return empty array of objects (instead of null)
			assertTrue(((Object[])altPathResult).length==0);
		}
		
			//Test fictitious numeric parameter
		try {	
			altPathResult = invokeMethod(method, "0123456789");
			assertNull(altPathResult);
		} catch (AssertionError ae) {
			//Add additional assertion check to handle methods that return empty array of objects (instead of null)
			assertTrue(((Object[])altPathResult).length==0);
		}
		
		return happyPathResult;
	}
	
	
	private Object invokeMethod(java.lang.reflect.Method method, String parameter) {
		String [] params = new String[1];
		params[0] = parameter;
		
		try {
			return method.invoke(rdm, params);
		} catch (IllegalArgumentException iarge) {
			System.out.println("IllegalArgumentException encountered during execution of " + method.getName() + 
					" method using parameter ['" + parameter + "'] - STACKTRACE: [" + iarge.getStackTrace() + "]");	
			return null;
		} catch (IllegalAccessException iace) {
			System.out.println("IllegalAccessException encountered during execution of " + method.getName() + 
					" method using parameter ['" + parameter + "'] - STACKTRACE: [" + iace.getStackTrace() + "]");	
			return null;
		} catch (InvocationTargetException ite) {
			System.out.println("InvocationTargetException encountered during execution of " + method.getName() + 
					" method using parameter ['" + parameter + "'] - STACKTRACE: [" + ite.getStackTrace() + "]");			
			return null;
		}
	}
    
    
    
 }

