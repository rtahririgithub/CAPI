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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.resource.UnknownPhoneNumberResourceException;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.cmb.reference.dto.FeeRuleDto;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusSystemException;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;
import com.telus.eas.utility.info.*;

/**
 * @author Pavel Simonovsky
 * 
 */
public interface ReferenceDataHelper {

	/**
	 * Authenticates the user id / password
	 * 
	 * @param String
	 *            user id
	 * @param String
	 *            password
	 * @exception TelusException
	 */
	void authenticate(String userID, String password) throws TelusException;

	/**
	 * Retrieve array of services related to the given Price Plan in KB
	 * 
	 * @params String[] serviceList to filter
	 * @params Price Plan code
	 * @return Array of Service Codes
	 * @throws TelusException
	 */
	String[] filterServiceListByPricePlan(String[] serviceList, String pricePlan) throws TelusException;

	/**
	 * Retrieve array of services associated with the given Province in KB
	 * 
	 * @params String[] serviceList to filter
	 * @params Province code
	 * @return Array of service codes
	 * @throws TelusException
	 */
	String[] filterServiceListByProvince(String[] serviceList, String province) throws TelusException;

	/**
	 * Validates if Service have been ever associated with Price Plan as
	 * Included Promotion
	 * 
	 * @return boolean
	 * @throws TelusException
	 */
	boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException;

	/**
	 * Validates if Dealer Code is default dealer code
	 * 
	 * @return boolean
	 * @throws TelusException
	 */
	boolean isDefaultDealer(String dealerCode) throws TelusException;

	/**
	 * Determine if is allowed to port out base on given status, activityCode
	 * and activityReasonCode combination
	 * 
	 * @param status
	 * @param activityCode
	 * @param activityReasonCode
	 * @return true if port out is allowed, otherwise return false
	 * @throws com.telus.eas.framework.exception.TelusException
	 */
	boolean isPortOutAllowed(String status, String activityCode, String activityReasonCode) throws TelusException;

	/**
	 * Validates if Business Role has given privilege for the Service
	 * 
	 * By Default Client Care Agent has all Privileges unless specified By
	 * Default Display Service Privilege available for all Business Roles unless
	 * specified By Default Remove Service Privilege available for all Business
	 * Roles unless specified
	 * 
	 * By Default Remove on Change Service Privilege returns false for all
	 * Business Roles unless specified
	 * 
	 * @return true if Business Role has given privilege for the Service
	 * @throws TelusException
	 */
	boolean isPrivilegeAvailable(String businessRoleCode, String privilegeCode, String pServiceCode) throws TelusException;

	/**
	 * check to see Service associated to price plan
	 * 
	 * @return boolean
	 * @throws TelusException
	 */
	boolean isServiceAssociatedToPricePlan(String pricePlanCode, String serviceCode) throws TelusException;

	/**
	 * Retrieve list of account types
	 * 
	 * @return Collection of AccountTypeInfo
	 * @throws TelusException
	 */
	AccountTypeInfo[] retrieveAccountTypes() throws TelusException;

	/**
	 * Retrieve Activity Types
	 * 
	 * @return array of Activity Types
	 * @throws TelusException
	 */
	ActivityTypeInfo[] retrieveActivityTypes() throws TelusException;

	/**
	 * Retrieve Product Types
	 * 
	 * @return list of Product Types
	 * @throws TelusException
	 */
	AdjustmentReasonInfo[] retrieveAdjustmentReasons() throws TelusException;

	/**
	 * Retrieve all provinces/states in the world.
	 * 
	 * @return list of provinces
	 * @throws TelusException
	 */
	ProvinceInfo[] retrieveAllProvinces() throws TelusException;

	/**
	 * Retrieve list of titles
	 * 
	 * @return ArrayList of TitleInfo
	 * @throws TelusException
	 */
	TitleInfo[] retrieveAllTitles() throws TelusException;

	/**
	 * Retrieves the Alternate Recurring Charge Contract Start Date
	 * 
	 * this method will return the start date per province that is needed to
	 * determine whether a subscriber activating/renewing on a new contract is
	 * being charged the alternate recurring charge. it will return null if no
	 * entry for the given province is found
	 * 
	 * @param String
	 *            province code
	 * @return Date alternate recurring charge contract start date
	 * @throws TelusException
	 */
	Date retrieveAlternateRCContractStartDate(String province) throws TelusException;

	/**
	 * Retrieves the Alternate Recurring Charge
	 * 
	 * This method will return the alternate recurring charge for the service
	 * passed in.
	 * 
	 * The other parameters (province, npa, nxx, corporate id) are used to
	 * determine whether the subscriber is excempt from the higher alternate RC.
	 * 
	 * If no alternate RC exists or the subscriber is excempt, the regular RC
	 * rate from the passed-in service will be returned.
	 * 
	 * @param ServiceInfo
	 *            service information
	 * @param String
	 *            province code
	 * @param String
	 *            subscriber's NPA
	 * @param String
	 *            subscriber's NXX
	 * @param String
	 *            corporate ID
	 * @return double alternate recurring chargee
	 * @throws TelusException
	 */
	double retrieveAlternateRecurringCharge(ServiceInfo serviceInfo, String pProvince, String pNpa, String pNxx, String pCorporateId) throws TelusException;

	/**
	 * Retreives reference Barcodes from DIST table CREDIT_AMOUNT.
	 * 
	 * @return AmountBarCode[]
	 * @exception TelusException
	 */
	AmountBarCodeInfo[] retrieveAmountBarCodes() throws TelusException;

	/**
	 * Retrieves foreign application messages' mappings.
	 * 
	 * @throws TelusException
	 */
	ApplicationMessageMappingInfo[] retrieveApplicationMessageMappings() throws TelusException;

	/**
	 * Retrieves all application messages.
	 * 
	 * @throws TelusException
	 */
	ApplicationMessageInfo[] retrieveApplicationMessages() throws TelusException;

	/**
	 * Retrieves all application summaries.
	 * 
	 * @throws TelusException
	 */
	public ApplicationSummaryInfo[] retrieveApplicationSummaries() throws TelusException;

	/**
	 * Retrieves all audience types.
	 * 
	 * @throws TelusException
	 */
	public AudienceTypeInfo[] retrieveAudienceTypes() throws TelusException;

	/**
	 * Retrieve specific bill cycle
	 * 
	 * @params String bill cycle code
	 * @return BillCycle Info
	 * @throws TelusException
	 */
	public BillCycleInfo retrieveBillCycle(String pBillCycleCode) throws TelusException;

	/**
	 * Retrieve billing cycle with least number of bans
	 * @deprecated Do not use this method! 
	 * @return int cycle with min(no_of_bans)
	 * @throws TelusException
	 */
	public int retrieveBillCycleLeastUsed() throws TelusException;

	/**
	 * Retrieve list of bill cycles
	 * 
	 * It only returns bill cycles that can be allocated ie. where the
	 * allocation indicator is not equal 'N'.
	 * 
	 * @return Array of BillCycle Info
	 * @throws TelusException
	 */
	public BillCycleInfo[] retrieveBillCycles() throws TelusException;

	/**
	 * Retrieve list of bill cycles for a given population
	 * 
	 * It only returns bill cycles that can be allocated and that belong to the
	 * population requested.
	 * 
	 * @params String population code (A = all, E = end user, N = national, and
	 *         R = reseller)
	 * @return Array of BillCycle Info
	 * @throws TelusException
	 */
	public BillCycleInfo[] retrieveBillCycles(String pPopulationCode) throws TelusException;

	/**
	 * Retrieves all hold bill destinations.
	 * 
	 * @throws TelusException
	 */
	public BillHoldRedirectDestinationInfo[] retrieveBillHoldRedirectDestinations() throws TelusException;

	/**
	 * Retrieves all equipment brand swap rules.
	 * 
	 * @throws TelusException
	 */
	public BrandSwapRuleInfo[] retrieveBrandSwapRules() throws TelusException;

	/**
	 * Retrieve list of brands
	 * 
	 * @return Array of BrandInfo
	 * @throws TelusException
	 */
	public BrandInfo[] retrieveBrands() throws TelusException;

	/**
	 * Retrieve Business Roles
	 * 
	 * @return array of Business Roles
	 * @throws TelusException
	 */
	public BusinessRoleInfo[] retrieveBusinessRoles() throws TelusException;

	/**
	 * Retrieve list of Client Consent Indicators.
	 */
	public ClientConsentIndicatorInfo[] retrieveClientConsentIndicators() throws TelusException;

	/**
	 * Retreives reference Client State Reasons from CODS.
	 */
	public ClientStateReasonInfo[] retrieveClientStateReasons() throws TelusException;

	/**
	 * Retrieve collection activities
	 * 
	 * @return list of collection activities
	 * @throws TelusExceptio
	 */
	public CollectionActivityInfo[] retrieveCollectionActivities() throws TelusException;

	/**
	 * Retrieve collection agencies
	 * 
	 * @return list of collection agencies
	 * @throws TelusException
	 */
	public CollectionAgencyInfo[] retrieveCollectionAgencies() throws TelusException;

	/**
	 * May 23, 2006
	 * 
	 * @throws TelusException
	 */
	public CollectionPathDetailsInfo[] retrieveCollectionPathDetails() throws TelusException;

	/**
	 * Retrieve collection state list Collection State is combination of
	 * collection path and step
	 * 
	 * @return list of collection states
	 * @throws TelusException
	 */
	public CollectionStateInfo[] retrieveCollectionStates() throws TelusException;

	/**
	 * May 23, 2006
	 * 
	 * @throws TelusException
	 */
	public CollectionStepApprovalInfo[] retrieveCollectionStepApproval() throws TelusException;

	/**
	 * Retrieve list of Commitment Reasons.
	 */
	public CommitmentReasonInfo[] retrieveCommitmentReasons() throws TelusException;

	/**
	 * Retrieve list of Corporate Account Representatives.
	 */
	public CorporateAccountRepInfo[] retrieveCorporateAccountReps() throws TelusException;

	/**
	 * Retrieve Countries
	 * 
	 * @return list of Countriess
	 * @throws TelusException
	 */
	public CountryInfo[] retrieveCountries() throws TelusException;

	/**
	 * Retrieve list of Coverage Region for Pager for Fusion K2
	 * 
	 * @return Array of of CoverageRegionInfo
	 * @throws TelusException
	 */
	public CoverageRegionInfo[] retrieveCoverageRegions() throws TelusException;

	/**
	 * Retrieve Credit Card Payment Type
	 * 
	 * @return list of Credit Card Payment Types
	 * @throws TelusException
	 */
	public CreditCardPaymentTypeInfo[] retrieveCreditCardPaymentTypes() throws TelusException;

	/**
	 * Retrieve CreditCardTypes
	 * 
	 * @return list of CreditCardTypes
	 * @throws TelusException
	 */
	public CreditCardTypeInfo[] retrieveCreditCardTypes() throws TelusException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return CreditCheckDepositChangeReasonInfo[]
	 * @throws TelusException
	 */
	public CreditCheckDepositChangeReasonInfo[] retrieveCreditCheckDepositChangeReasons() throws TelusException;

	/**
	 * Retrieves all the credit classes.
	 * 
	 * @return CreditClassInfo[]
	 * @throws TelusException
	 */
	public CreditClassInfo[] retrieveCreditClasses() throws TelusException;

	/**
	 * Retrieve Credit Message by message code
	 * 
	 * @params String credit message code
	 * @return CreditMessageInfo
	 * @throws TelusException
	 */
	public CreditMessageInfo retrieveCreditMessageByCode(String pCode) throws TelusException;

	/**
	 * Retrieve list of Credit Messages
	 * 
	 * @return ArrayList of Credit Message Info
	 * @throws TelusException
	 */
	public CreditMessageInfo[] retrieveCreditMessages() throws TelusException;

	/**
	 * Retrieve sales rep info by dealer code and sales rep code including
	 * expired sales reps. Note: This method uses the REF database as its
	 * datasource.
	 * 
	 * @params String dealer code
	 * @params String sales rep code
	 * @params boolean indicates if including expired sales rep
	 * @return SalesRepInfo
	 * @throws TelusException
	 */
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException;

	/**
	 * Retrieve sales rep info by dealer code and sales rep code including
	 * expired sales reps. Note: This method uses the REF database as its
	 * datasource.
	 * 
	 * @params String dealer code
	 * @params String sales rep code
	 * @return SalesRepInfo
	 * @throws TelusException
	 */
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode) throws TelusException;

	/**
	 * Retrieve dealer info by dealer code even if dealer is expired. Note: This
	 * method uses the REF database as its datasource.
	 * 
	 * @params String dealer code
	 * @params boolean indicates if including expired dealer
	 * @return DealerInfo
	 * @throws TelusException
	 */
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode, boolean expired) throws TelusException;

	/**
	 * Retrieve dealer info by dealer code even if dealer is expired. Note: This
	 * method uses the REF database as its datasource.
	 * 
	 * @params String dealer code
	 * @return DealerInfo
	 * @throws TelusException
	 */
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode) throws TelusException;

	/**
	 * Retrieve Default LDC
	 * 
	 * @return Default LDC
	 * @throws TelusException
	 */
	public String retrieveDefaultLDC() throws TelusException;

	/**
	 * Retrieve Departments
	 * 
	 * @return list of Departments
	 * @throws TelusException
	 */
	public DepartmentInfo[] retrieveDepartments() throws TelusException;

	/**
	 * Returns the List of Discounts(Credits)
	 * 
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans() throws TelusException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * The Product Promo Type List and initial activation indicator should be
	 * retrieved from an existing handset Info.
	 * 
	 * @param boolean current
	 * @param String
	 *            Price Plan Code
	 * @param String
	 *            province
	 * @param long[] Product Promo Type List
	 * @param boolean initial activation indicator
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, long[] productPromoTypeList, boolean initialActivation, int term) throws TelusException;

	/**
	 * Returns the List of Discounts(Credits)
	 * 
	 * @param boolean current
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current) throws TelusException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * @param boolean current
	 * @param String
	 *            Price Plan Code
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, int term) throws TelusException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * @param boolean current
	 * @param String
	 *            Price Plan Code
	 * @param String
	 *            province
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, int term) throws TelusException;

	/**
	 * Retrieve list of Encoder Format for Pager for Fusion K2
	 * 
	 * @return Array of of EncodingFormatInfo
	 * @throws TelusException
	 */
	public EncodingFormatInfo[] retrieveEncodingFormats() throws TelusException;

	/**
	 * Retrieve equipment possessions.
	 * 
	 * @return list of equipment possessions.
	 * @throws TelusException
	 */
	public com.telus.eas.equipment.info.EquipmentPossessionInfo[] retrieveEquipmentPossessions() throws TelusException;

	/**
	 * Retrieve list of Product Codes with Product Type(Model) description
	 * 
	 * @return Array of of EquipmentProductTypeInfo
	 * @throws TelusException
	 */
	public EquipmentProductTypeInfo[] retrieveEquipmentProductTypes() throws TelusException;

	/**
	 * Retrieve Equipment Statuses
	 * 
	 * @return list of Equipment Statuses
	 * @throws TelusException
	 */
	public EquipmentStatusInfo[] retrieveEquipmentStatuses() throws TelusException;

	/**
	 * Retrieve Equipment Types
	 * 
	 * @return list of Equipment Types
	 * @throws TelusException
	 */
	public EquipmentTypeInfo[] retrieveEquipmentTypes() throws TelusException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return ExceptionReasonInfo[]
	 * @throws TelusException
	 */
	public ExceptionReasonInfo[] retrieveExceptionReasons() throws TelusException;

	/**
	 * Retrieve FeatureCategories
	 * 
	 * @return list of FeatureCategories
	 * @throws TelusException
	 */
	public FeatureInfo[] retrieveFeatureCategories() throws TelusException;

	/**
	 * Retrieve Features
	 * 
	 * @return list of Features
	 * @throws TelusException
	 */
	public FeatureInfo[] retrieveFeatures() throws TelusException;

	/**
	 * Retrieves FeeWaiverReasons
	 * 
	 * @throws TelusSystemException
	 * @return FeeWaiverReasonInfo[]
	 */
	public FeeWaiverReasonInfo[] retrieveFeeWaiverReasons() throws TelusException;

	/**
	 * Retrieves FeeWaiverTypes
	 * 
	 * @throws TelusSystemException
	 * @return FeeWaiverTypeInfo[]
	 */
	public FeeWaiverTypeInfo[] retrieveFeeWaiverTypes() throws TelusException;

	/**
	 * Retrieve Fleet by Fleet Identity
	 * 
	 * @params FleetIdentityInfo
	 * @return Fleet Info
	 * @throws TelusException
	 */
	public FleetInfo retrieveFleetByFleetIdentity(FleetIdentityInfo fleeIdentity) throws TelusException;

	/**
	 * Retrieve Fleet Classes
	 * 
	 * @return array of Fleet Classes
	 * @throws TelusException
	 */
	public FleetClassInfo[] retrieveFleetClasses() throws TelusException;

	/**
	 * Retrieve list of Fleets by Fleet Type
	 * 
	 * @params String Fleet Type
	 * @return Collection of Fleet Info
	 * @throws TelusException
	 */
	public FleetInfo[] retrieveFleetsByFleetType(char fleetType) throws TelusException;

	/**
	 * Retrieve Follow Up close reason descriptions.
	 * 
	 * @param reasonCode
	 * @throws TelusException
	 */
	public FollowUpCloseReasonInfo retrieveFollowUpCloseReason(String reasonCode) throws TelusException;

	/**
	 * Retrieves a list of available Follow Up close reason codes with
	 * descriptions.
	 * 
	 * @throws TelusException
	 */
	public FollowUpCloseReasonInfo[] retrieveFollowUpCloseReasons() throws TelusException;

	/**
	 * Retrieve list of Follow Up types
	 * 
	 * @return Collection of FollowUpTypeInfo
	 * @throws TelusException
	 */
	public FollowUpTypeInfo[] retrieveFollowUpTypes() throws TelusException;

	/**
	 * Retrieves all name generations.
	 * 
	 * @throws TelusException
	 */
	public GenerationInfo[] retrieveGenerations() throws TelusException;

	/**
	 * Retrieve Array of Included Promo Services by Price Plan Code ,Product
	 * Type,Equipment Type ,Province Code, Account Type,Account SubType
	 * 
	 * @param String
	 *            Price Plan Code
	 * @param String
	 *            Equipment Type
	 * @param String
	 *            Province Code
	 * @return Price Plan Info
	 * @throws TelusException
	 */
	public ServiceInfo[] retrieveIncludedPromotions(String pPricePlanCD, String pEquipmentType, String pNetworkType, String pProvinceCD, int term) throws TelusException;

	/**
	 * Retrieve list of Invoice Call Sort Order Types.
	 */
	public InvoiceCallSortOrderTypeInfo[] retrieveInvoiceCallSortOrderTypes() throws TelusException;

	/**
	 * Retrieve list of Invoice Suppression Levels
	 * 
	 * @return InvoiceSuppressionLevelInfo[] Array of Invoice Suppression Levels
	 * @throws TelusException
	 */
	public InvoiceSuppressionLevelInfo[] retrieveInvoiceSuppressionLevels() throws TelusException;

	/**
	 * Retrieve specific Knowbility Operator Id
	 * 
	 * @return Knowbility Operator Info
	 * @throws TelusException
	 */
	public KnowbilityOperatorInfo retrieveKnowbilityOperatorInfo(String pOperatorId) throws TelusException;

	/**
	 * Retrieve specific Knowbility Operator Id
	 * 
	 * @return Knowbility Operator Info
	 * @throws TelusException
	 */
	public KnowbilityOperatorInfo[] retrieveKnowbilityOperators() throws TelusException;

	/**
	 * Retrieve Languages
	 * 
	 * @return list of Languages
	 * @throws TelusException
	 */
	public LanguageInfo[] retrieveLanguages() throws TelusException;

//	public LetterInfo retrieveLetter(String letterCategory, String letterCode) throws TelusException;
//
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode, int version) throws TelusException;

	/**
	 * Retrieve list of Client Consent Indicators.
	 */
//	public LetterCategoryInfo[] retrieveLetterCategories() throws TelusException;
//
//	public LetterSubCategoryInfo[] retrieveLetterSubCategories() throws TelusException;
//
//	public LetterVariableInfo[] retrieveLetterVariables(String letterCategory, String letterCode, int letterVersion) throws TelusException;

//	public LetterInfo[] retrieveLettersByCategory(String letterCategory) throws TelusException;
//
//	public LetterInfo[] retrieveLettersByTitleKeyword(String titleKeyword) throws TelusException;

	/**
	 * Retrieve list of lock reasons
	 * 
	 * @return Array of LockReasonInfo
	 * @throws TelusException
	 */
	public LockReasonInfo[] retrieveLockReasons() throws TelusException;

	/**
	 * Retrieve Logical Date
	 * 
	 * @return Date
	 * @throws TelusException
	 */
	public Date retrieveLogicalDate() throws TelusException;

	public ChargeTypeInfo[] retrieveManualChargeTypes() throws TelusException;

	/**
	 * Retrieve list of Provinces that Price Plan(or Service) is available in
	 * 
	 * @params String Price Plan(or Service) code
	 * @return Array of Province Codes
	 * @throws TelusException
	 */
	public String[] retrieveMarketProvinces(String pServiceCode) throws TelusException;

	/**
	 * Retrieves all memo type categories.
	 * 
	 * @return MemoTypeCategoryInfo[]
	 * @throws TelusException
	 */
	public MemoTypeCategoryInfo[] retrieveMemoTypeCategories() throws TelusException;

	/**
	 * Retrieve list of Memo types
	 * 
	 * @return Collection of MemoTypeInfo
	 * @throws TelusException
	 */
	public MemoTypeInfo[] retrieveMemoTypes() throws TelusException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return MigrationTypeInfo[]
	 * @throws TelusException
	 */
	public MigrationTypeInfo[] retrieveMigrationTypes() throws TelusException;

	/**
	 * Retrieve array of service codes for minute contributing pooling services.
	 * 
	 * @return String[]
	 * @throws TelusException
	 */
	public String[] retrieveMinutePoolingContributorServiceCodes() throws TelusException;

	/**
	 * Retrieve list of NetworkTypes
	 * 
	 * @return ArrayList of NetworkTypeInfo
	 * @throws TelusException
	 */
	public NetworkTypeInfo[] retrieveNetworkTypes() throws TelusException;

	/**
	 * Retrieve Networks
	 * 
	 * @return array of Networks
	 * @throws TelusException
	 */
	public NetworkInfo[] retrieveNetworks() throws TelusException;

	/**
	 * retrieveNotificationMessageTemplateInfo
	 * 
	 * @throws TelusException
	 * @return NotificationMessageTemplateInfo[]
	 */
	public NotificationMessageTemplateInfo[] retrieveNotificationMessageTemplateInfo() throws TelusException;

	/**
	 * retrieveContactContentType()
	 * 
	 * @throws TelusException
	 * @return ContactContentTypeInfo[]
	 */
	public NotificationTypeInfo[] retrieveNotificationType() throws TelusException;

	/**
	 * Retrieve NPANXX array for MSISDN reservation
	 * 
	 * @param String
	 *            phone number
	 * @return String [] NPANXX Array for MSISDN reservation
	 * @throws TelusException
	 */
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber) throws TelusException;

	/**
	 * Retrieve NPANXX array for MSISDN reservation
	 * 
	 * @param String
	 *            phone number
	 * @param boolean is this a ported in number?
	 * @return String [] NPANXX Array for MSISDN reservation
	 * @throws TelusException
	 */
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber, boolean isPortedInNumber) throws TelusException;

	/**
	 * Retrieve Number Group by PhoneNumber, Product Type
	 * 
	 * @param String
	 *            Phone Number
	 * @param String
	 *            Product Type
	 * @return NumberGroupInfo NumberGroup information for specific phone number
	 * @throws TelusException
	 */
	public NumberGroupInfo retrieveNumberGroupByPhoneNumberProductType(String pPhoneNumber, String pProductType) throws TelusException;

	/**
	 * Retrieve Number Group by PhoneNumber, Product Type
	 * 
	 * @param String
	 *            Ported In Phone Number
	 * @param String
	 *            Product Type
	 * @return NumberGroupInfo NumberGroup information for specific phone number
	 * @throws TelusException
	 */
	public NumberGroupInfo retrieveNumberGroupByPortedInPhoneNumberProductType(String pPhoneNumber, String pProductType) throws TelusException;

	/**
	 * Retrieve Collection of Number Groups By Number Location Product Type,
	 * Equipment Type , Market Area Code
	 * 
	 * @param String
	 *            number location
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            market area code
	 * @return Collection of NumberGroupInfo classes
	 * @throws TelusException
	 */
	public NumberGroupInfo[] retrieveNumberGroupList(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType, String pMarketAreaCode) throws TelusException;

	/**
	 * Retrieve Collection of Number Groups Number Location Product Type,
	 * Equipment Type
	 * 
	 * @param String
	 *            number location
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @return Collection of NumberGroupInfo classes
	 * @throws TelusException
	 */
	public NumberGroupInfo[] retrieveNumberGroupList(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType) throws TelusException;

	/**
	 * Retrieve Collection of Number Groups by Number Location Product Type,
	 * Equipment Type, Province Code
	 * 
	 * @param String
	 *            product type
	 * @return Collection of NumberGroupInfo classes
	 * @throws TelusException
	 */
	public NumberGroupInfo[] retrieveNumberGroupListByProvince(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType, String pProvince) throws TelusException;

	public NumberRangeInfo[] retrieveNumberRanges() throws TelusException;

	/**
	 * Retrieve list of Equipment Type for Pager for Fusion K2
	 * 
	 * @return Array of of EquipTypeInfo
	 * @throws TelusException
	 */
	public EquipmentTypeInfo[] retrievePagerEquipmentTypes() throws TelusException;

	/**
	 * Retrieve list of Channel Frequency for Pager for Fusion K2
	 * 
	 * @return Array of of PagerFrequencyInfo
	 * @throws TelusException
	 */
	public PagerFrequencyInfo[] retrievePagerFrequencies() throws TelusException;

	/**
	 * Retrieve Payment Method Type
	 * 
	 * @return list of Payment Method Types
	 * @throws TelusException
	 */
	public PaymentMethodTypeInfo[] retrievePaymentMethodTypes() throws TelusException;

	/**
	 * Retrieve Payment Source Type
	 * 
	 * @return list of Payment Source Types
	 * @throws TelusException
	 */
	public PaymentSourceTypeInfo[] retrievePaymentSourceTypes() throws TelusException;

	/**
	 * Retrieve Payment Transfer Reasons Added by Roman
	 * 
	 * @return list of Payment Transfer Reasons
	 * @throws TelusException
	 */
	public PaymentTransferReasonInfo[] retrievePaymentTransferReasons() throws TelusException;

	/**
	 * Retrieves PhoneNumberResource
	 * 
	 * @throws UnknownPhoneNumberResourceException
	 * @throws TelusException
	 * @return PhoneNumberResourceInfo
	 */
	public PhoneNumberResourceInfo retrievePhoneNumberResource(String phoneNumber) throws TelusException;

	/**
	 * Retrieve list of PoolingGroup
	 * 
	 * @return ArrayList of PoolingGroupInfo
	 * @throws TelusException
	 */
	public PoolingGroupInfo[] retrievePoolingGroups() throws TelusException;

	/**
	 * Retrieve list of all Prepaid Adjustment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidAdjustmentReasons() throws TelusException;

	/**
	 * Retrieve specific Prepaid Event Type
	 * 
	 * @param int Prepaid Event Type Id
	 * @return PrepaidEventTypeInfo Prepaid Event Type
	 * @throws TelusException
	 * @see PrepaidEventTypeInfo
	 */
	public PrepaidEventTypeInfo retrievePrepaidEventType(int eventTypeId) throws TelusException;

	/**
	 * Retrieve list of all Prepaid Event Types
	 * 
	 * @return PrepaidEventTypeInfo[] Prepaid Event types
	 * @throws TelusException
	 * @see PrepaidEventTypeInfo
	 */
	public PrepaidEventTypeInfo[] retrievePrepaidEventTypes() throws TelusException;

	/**
	 * Retrieve list of all Prepaid Feature Add Waive Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidFeatureAddWaiveReasons() throws TelusException;

	/**
	 * Retrieve list of all Prepaid Manual Adjustment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidManualAdjustmentReasons() throws TelusException;

	/**
	 * Retrieves Prepaid Rates
	 */
	public PrepaidRateProfileInfo[] retrievePrepaidRates() throws TelusException;

	/**
	 * Retrieve list of Prepaid Recharge Denominations
	 * 
	 * @return Array of of PrepaidRechargeDenominationInfo
	 * @throws TelusException
	 */
	public PrepaidRechargeDenominationInfo[] retrievePrepaidRechargeDenominations() throws TelusException;

	/**
	 * Retrieve list of all Top Up Waive Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidTopUpWaiveReasons() throws TelusException;

	/**
	 * Retrieve list of all Device Direct Fulfillment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidDeviceDirectFulfillmentReasons() throws TelusException;

	/**
	 * Retrieve Collection of SOC Info by Price Plan Code
	 * 
	 * @param String
	 *            Price Plan
	 * @return SOC Info classes
	 * @throws TelusException
	 */
	public PricePlanInfo retrievePricePlan(String pPricePlanCD) throws TelusAPIException;

	/**
	 * Retrieve Collection of SOC Info by Price Plan Code
	 * 
	 * @param String
	 *            Price Plan
	 * @return SOC Info classes
	 * @throws TelusException
	 */
	public PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId,ServiceFamilyTypeInfo[] ppsServices) throws TelusException;
	
	public PricePlanInfo[] retrievePricePlanList(PricePlanSelectionCriteriaInfo criteriaInfo , String[] offerGroupCodeList) throws TelusException;

	public OfferPricePlanSetInfo retrieveOfferPricePlanInfo(PricePlanSelectionCriteriaInfo ppCriteriaInfo) throws TelusException;

	
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, char accountType, char accountSubType, int pBrandId, 
			long[] pProductPromoTypeList, boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String activityCode, 
			String activityReasonCode, String networkType) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param char account sub type
	 * @param int brand id
	 * @param boolean currentPricePlansOnly
	 * @param boolean availableForActivationOnly
	 * @param String
	 *            network type
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String productType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, String networkType) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param char account sub type
	 * @param int brand id
	 * @param String
	 *            network type
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId, String networkType) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param char account sub type
	 * @parma int brandId
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String product type
	 * @param String equipment type
	 * @param String province code
	 * @param char account type
	 * @param char account sub type
	 * @param int brand id
	 * @param long[] product promo type list
	 * @param boolean initial activation indicator
	 * @param boolean prepaid
	 * @param boolean currentPricePlansOnly
	 * @param String network type
	 * @param String seat type code
	 * 
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, long[] pProductPromoTypeList, boolean pInitialActivation, boolean currentPricePlansOnly, String networkType, String seatTypeCode)
			throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param char account sub type
	 * @param int brand id
	 * @param boolean currentPricePlansOnly
	 * @param boolean availableForActivationOnly
	 * @param String
	 *            activity code
	 * @param String
	 *            activity reason code
	 * @param String
	 *            network type
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, String activityCode, String activityReasonCode,
			String networkType) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String
	 *            product type
	 * @param String
	 *            equipment type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param char account sub type
	 * @param int brand id
	 * @param long[] product promo type list
	 * @param boolean initial activation indicator
	 * @param String
	 *            network type
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, long[] pProductPromoTypeList, boolean pInitialActivation, String networkType) throws TelusException;

	/**
	 * Retrieve Collection of Price Plans by Province Code,Product Type,
	 * Equipment Type, Product Promo Type List, Initial Activation indicator
	 * 
	 * @param String product type
	 * @param String equipment type
	 * @param String province code
	 * @param char account type
	 * @param char account sub type
	 * @param boolean currentPricePlansOnly
	 * @param boolean availableForActivationOnly
	 * @param int term in months of the Price Plan
	 * @param String network type
	 * @param String seat type code
	 *            
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String networkType, String seatTypeCode) throws TelusException;

	/**
	 * @param String
	 *            product type
	 * @param String
	 *            province code
	 * @param char account type
	 * @param String
	 *            equipment type
	 * @param int brandid
	 * @param boolean currentPricePlansOnly
	 * @param boolean initial activation indicator
	 * @return Collection of PricePlanInfo classes
	 * @throws TelusException
	 * 
	 * Not in use?
	 */
	@Deprecated
	public PricePlanInfo[] retrievePricePlanListByAccType(String productType, String provinceCode, char accountType, String equipmentType, int brandId, boolean currentPlansOnly, boolean availableForActivationOnly, String networkType) throws TelusException;

	/**
	 * Retrieve all price plan terms
	 * 
	 * @return an array of PricePlanTermInfo
	 * @throws TelusException
	 */
	public PricePlanTermInfo[] retrievePricePlanTerms() throws TelusException;

	/**
	 * Retrieve Product Types
	 * 
	 * @return list of Product Types
	 * @throws TelusException
	 */
	public ProductTypeInfo[] retrieveProductTypes() throws TelusException;

	/**
	 * Retrieve Promo Term Info by Promo Service Code
	 * 
	 * @params String Promo Service Code
	 * @return PromoTermInfo
	 * @throws TelusException
	 */
	public PromoTermInfo retrievePromoTerm(String pPromoServiceCode) throws TelusException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * The Product Promo Type List and initial activation indicator should be
	 * retrieved from an existing handset Info.
	 * 
	 * @param String
	 *            Price Plan Code
	 * @param long[] Product Promo Type List
	 * @param boolean initial activation indicator
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException
	 */
	public String[] retrievePromotionalDiscounts(String pPricePlanCode, long[] productPromoTypeList, boolean initialActivation) throws TelusException;

	/**
	 * Retrieve list of provinces
	 * 
	 * @return Collection of ProvinceInfo
	 * @throws TelusException
	 */
	public ProvinceInfo[] retrieveProvinces() throws TelusException;

	public ProvinceInfo[] retrieveProvinces(String countryCode) throws TelusException;

	/**
	 * Retrieves Provisioning Platform Types
	 */
	public ProvisioningPlatformTypeInfo[] retrieveProvisioningPlatformTypes() throws TelusException;

	/**
	 * Retrieves Provisioning Transaction Status
	 */
	public ProvisioningTransactionStatusInfo[] retrieveProvisioningTransactionStatuses() throws TelusException;

	/**
	 * Retrieves Provisioning Transaction Types
	 */
	public ProvisioningTransactionTypeInfo[] retrieveProvisioningTransactionTypes() throws TelusException;

	/**
	 * Retrieve Collection of Regular Services
	 * 
	 * @param String
	 *            Feature Category
	 * @param String
	 *            Product Type
	 * @param boolean Current
	 * @return Collection of Regular Services
	 * @throws TelusException
	 */
	public ServiceInfo[] retrieveRegularServices(String featureCategory, String productType, boolean current) throws TelusException;

	/**
	 * Retrieve Collection of Regular Services
	 * 
	 * @return Collection of Regular Services
	 * @throws TelusException
	 */
	public ServiceInfo[] retrieveRegularServices() throws TelusException;

	/**
	 * Retrieve array of ServiceRelationInfo for the given Service in KB
	 * 
	 * @params String Service Code
	 * @return Array of Service Relation Info
	 * @throws TelusException
	 */
	public ServiceRelationInfo[] retrieveRelations(String serviceCode) throws TelusException;

	/**
	 * Returns the Resource Status.
	 * 
	 * @param productType
	 *            The type of product
	 * @param resourceType
	 *            The type of resource
	 * @param resourceNumber
	 *            The number of resouce
	 * @throws TelusException
	 */
	public String retrieveResourceStatus(String productType, String resourceType, String resourceNumber) throws TelusException;

	/**
	 * Retrieve list of Handsets Roaming Capability
	 * 
	 * @return Array of Handsets Roaming Capability
	 * @throws TelusException
	 */
	public HandsetRoamingCapabilityInfo[] retrieveRoamingCapability() throws TelusException;

	/**
	 * Retrieves Routes
	 */
	public RouteInfo[] retrieveRoutes() throws TelusException;

	/**
	 * Retrieve all rules.
	 * 
	 * @return RuleInfo[]
	 * @throws TelusException
	 */
	public RuleInfo[] retrieveRules() throws TelusException;

	/**
	 * Retrieve rules by category.
	 * 
	 * @param int category
	 * @return RuleInfo[]
	 * @throws TelusException
	 */
	public RuleInfo[] retrieveRules(int category) throws TelusException;

	/**
	 * Retrieves Provisioning Transaction Types
	 */
	public SIDInfo[] retrieveSIDs() throws TelusException;

	/**
	 * Retrieve list of Sales Rep by Dealer
	 * 
	 * @params String Dealer Code
	 * @return Collection of SalesRepInfo
	 * @throws TelusException
	 */
	public SalesRepInfo[] retrieveSalesRepListByDealer(String pDealerCD) throws TelusException;

	/**
	 * Retrieve list of segmentations
	 * 
	 * @return Array of SegmentationInfo
	 * @throws TelusException
	 */
	public SegmentationInfo[] retrieveSegmentations() throws TelusException;

	/**
	 * Retrieve Service Equipment Type Info
	 * 
	 * @return ServiceEquipmentTypeInfo
	 * @throws TelusException
	 */
	
	public ServiceEquipmentTypeInfo[] retrieveServiceEquipmentNetworkInfo(String serviceCode) throws TelusException;

	/**
	 * Retrieves retrieve Service Exclusion Groups for SOCs.
	 * 
	 * @throws TelusException
	 */
	public ServiceExclusionGroupsInfo[] retrieveServiceExclusionGroups() throws TelusException;

	/**
	 * Retrieve array of Family Service Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @params String Family Type
	 * @params String Province Code
	 * @params String Equipment Type
	 * @params boolean current Price Plans Only indicator
	 * @return Array of Service Codes
	 * @throws TelusException
	 */
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String provinceCode, String equipmentType, String networkType, boolean currentServicesOnly, int termInMonths) throws TelusException;

	/**
	 * Retrieve array of Family Service Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @return Array of Service Codes
	 * @throws TelusException
	 */
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String networkType) throws TelusException;

	/**
	 * Retrieve array of Service Family Groups Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @params String Family Type
	 * @return Array of Service Codes
	 * @throws TelusException
	 */
	public String[] retrieveServiceFamilyGroupCodes(String serviceCode, String familyType) throws TelusException;

	/**
	 * Retrieve array of Service Policy Info
	 * 
	 * @return Array of ServicePolicyInfo
	 * @throws TelusException
	 */
	public ServicePolicyInfo[] retrieveServicePolicyExceptions() throws TelusException;

	/**
	 * Retrieve ServiceUsageInfo by Service Code
	 * 
	 * @params String Service code
	 * @return ServiceUsageInfo
	 * @throws TelusException
	 */
	public ServiceUsageInfo retrieveServiceUsageInfo(String pServiceCode) throws TelusException;

	/**
	 * Retrieve list of special number range
	 * 
	 * @return Array of SpecialNumberRangeInfo
	 * @throws TelusException
	 */
	public SpecialNumberRangeInfo[] retrieveSpecialNumberRanges() throws TelusException;

	/**
	 * Retrieve list of special number
	 * 
	 * @return Array of SpecialNumberInfo
	 * @throws TelusException
	 */
	public SpecialNumberInfo[] retrieveSpecialNumbers() throws TelusException;

	/**
	 * Retrieve list of states
	 * 
	 * @return Array List of States
	 * @throws TelusException
	 */
	public StateInfo[] retrieveStates() throws TelusException;

	/**
	 * Retrieve list of Street Types
	 * 
	 * @return Array List of StreetTypesInfo
	 * @throws TelusException
	 */
	StreetTypeInfo[] retrieveStreetTypes() throws TelusException;

	/**
	 * Retreives reference Subscription Role Types from CODS.
	 */
	SubscriptionRoleTypeInfo[] retrieveSubscriptionRoleTypes() throws TelusException;

	/**
	 * Retrieves all equipment swap rules.
	 * 
	 * @throws TelusException
	 */
	SwapRuleInfo[] retrieveSwapRules() throws TelusException;

	/**
	 * Retrieve System Date
	 * 
	 * @return Date
	 * @throws TelusException
	 */
	Date retrieveSystemDate() throws TelusException;

	/**
	 * Retrieves all talk group priorities.
	 * 
	 * @throws TelusException
	 */
	TalkGroupPriorityInfo[] retrieveTalkGroupPriorities() throws TelusException;

	/**
	 * Retrieve List of Talk Groups by Fleet Identity
	 * 
	 * @params FleetIdentityInfo
	 * @return Collection of Talk Group Info
	 * @throws TelusException
	 */
	TalkGroupInfo[] retrieveTalkGroupsByFleetIdentity(FleetIdentityInfo fleetIdentityInfo) throws TelusException;

	/**
	 * Retrieve Tax Information for Provinces
	 * 
	 * @return Array of TaxationPolicyInfo
	 * @throws TelusException
	 */
	TaxationPolicyInfo[] retrieveTaxPolicies() throws TelusException;

	/**
	 * Retrieve list of titles
	 * 
	 * @return ArrayList of TitleInfo
	 * @throws TelusException
	 */
	TitleInfo[] retrieveTitles() throws TelusException;

	/**
	 * Retrieve Unit Types
	 * 
	 * @return list if Unit Types
	 * @throws TelusException
	 */
	UnitTypeInfo[] retrieveUnitTypes() throws TelusException;

	/**
	 * Retrieve Urban Id By Number Group
	 * 
	 * @params NumberGroupInfo
	 * @return int (Urban Id)
	 * @throws TelusException
	 */
	int retrieveUrbanIdByNumberGroup(NumberGroupInfo numberGroup) throws TelusException;

	/**
	 * Retrieve list of retrieveVendorServices
	 * 
	 * @return ArrayList of Vendor Services
	 * @throws TelusException
	 */
	public VendorServiceInfo[] retrieveVendorServices() throws TelusException;

	/**
	 * Retrieve specific WPS feature
	 * 
	 * @param int WPS feature id
	 * @return ServiceInfo WPS feature
	 * @throws TelusException
	 * @throws ApplicationException 
	 * @see ServiceInfo
	 */
	public ServiceInfo retrieveWPSFeature(int pFeatureId)throws TelusException, ApplicationException;

	/**
	 * Retrieve list of all WPS features
	 * 
	 * @return ServiceInfo[] WPS features
	 * @throws TelusException
	 * @throws ApplicationException 
	 * @see ServiceInfo
	 */
	public ServiceInfo[] retrieveWPSFeaturesList() throws TelusException, ApplicationException;

	/**
	 * Retrieves all work functions for a given department code.
	 * 
	 * @throws TelusException
	 */
	public WorkFunctionInfo[] retrieveWorkFunctions(String departmentCode) throws TelusException;

	/**
	 * Retrieves all work functions.
	 * 
	 * @throws TelusException
	 */
	public WorkFunctionInfo[] retrieveWorkFunctions() throws TelusException;

	/**
	 * Retrieves work position iformation by work position id.
	 * 
	 * @param workPositionId
	 * @throws TelusException
	 */
	public WorkPositionInfo retrieveWorkPosition(String workPositionId) throws TelusException;

	/**
	 * Retrieves work positions assigned to the given function code.
	 * 
	 * @param functionCode
	 * @throws TelusException
	 */
	public WorkPositionInfo[] retrieveWorkPositions(String functionCode) throws TelusException;

	/**
	 * Retrieves all work positions.
	 * 
	 * @throws TelusException
	 */
	public WorkPositionInfo[] retrieveWorkPositions() throws TelusException;

	/**
	 * Retrieve array of service codes for the zero-minute contributing pooling
	 * services.
	 * 
	 * @return String[]
	 * @throws TelusException
	 */
	public String[] retrieveZeroMinutePoolingContributorServiceCodes() throws TelusException;

	
	// Added for Charge paper bill - Anitha Duraisamy- Start
	/**
	 * Retrieve array of one-time charge details for paper bill on the account.
	 *
	 * Any of the parameters passed can be null or empty, however there must be at
	 * least one parameter populated, otherwise will throw an IllegalArgumentException.
	 * @param date 
	 *
	 * @param int brand Id, can be zero for wildcard
	 * @param String province code, can be null or empty for wildcard
	 * @param char account type, can be null (that is '\u0000') for wildcard
	 * @param char account sub-type, can be null (that is '\u0000') for wildcard
	 * @param String account GL segment, can be null or empty for wildcard
	 * @param String invoiceSuppressionLevel, can be null or empty for wildcard
	 * 
	 * @return FeeRuleDto[] array of one-time charge details
	 * @throws TelusException
	 */
	public FeeRuleDto[] retrievePaperBillChargeType(int brandId, String provinceCode, char accountType, char accountSubType, String segment, String invoiceSuppressionLevel, Date logicalDate) throws TelusException;
	
	  // Added for Charge paper bill - Anitha Duraisamy- End
	
	
	
	/**
	 * Retrieve SOC group and service codes mapping information.
	 * 
	 * @param  String serviceGroupCode 
	 * @return String[] list of soc as values. 
	 * @throws TelusException
	 **/

	public String[] retrieveServiceGroupRelation(String serviceGroupCode)  throws TelusException ;
	
	/**
	 * Retrieves Service term information as defined in KB PROMOTION_TERMS table
	 *
	 * @param String serviceCode
	 * 
	 * @return ServiceTermDto
	 * @throws TelusException
	 */
	public ServiceTermDto retrieveServiceTerm(String serviceCode) throws TelusException;

	public int[] retrieveBillCycleListLeastUsed () throws TelusException;
	
	public DataSharingGroupInfo[] retrieveDataSharingGroups() throws TelusException;
	
	public ServicePeriodInfo[] retrieveServicePeriodInfo( String serviceCode ) throws TelusException;

	public ServiceFeatureClassificationInfo[] retrieveServiceFeatureClassifications() throws TelusException;
	
	public ServiceAirTimeAllocationInfo retrieveVoiceAllocation( String serviceCode, Date effectiveDate, String sessionId ) throws ApplicationException;

	public String openSession(String userId, String password, String applicationId) throws ApplicationException;
	
	public Map retrieveServiceCodes() throws ApplicationException;
	
	/**
	 * Retrieve list of seat types
	 * 
	 * @return ArrayList of SeatTypeInfo
	 * @throws TelusException
	 */
	SeatTypeInfo[] retrieveSeatTypes() throws TelusException;
	
	/**
	 * validates if an   accountType/accountSubType is eligible for PP&S services
	 * 
	 * @param accountType
	 * @param accountSubType
	 * @param ppsServices
	 * @return
	 * @throws TelusException
	 */
	
	public boolean isPPSEligible(char accountType, char accountSubType,ServiceFamilyTypeInfo[] ppsServices) throws TelusException;

	String[] retrieveServiceGroupBySocCode(String serviceCode, String[] groupNames) throws TelusException;

	/**
	 * Retrieve Service Extended Information (Province Codes, Account Type/Subtypes, Plan Groups)
	 * based on the service code list input
	 * 
	 * @return boolean
	 * @throws TelusException
	 */
	public ServiceExtendedInfo[] retrieveServiceExtendedInfo(String[] serviceCodes) throws TelusException;

	public ServiceFamilyTypeInfo[] retrievePPSServices() throws TelusException;

	public RoamingServiceNotificationInfo[]  retrieveRoamingServiceNotificationInfo( String[] serviceCodes) throws TelusException;
	
	public ReferenceInfo[] retrieveMarketingDescriptions() throws TelusException;
	
	public SapccMappedServiceInfo[] retrieveSapccMappedServiceInfo() throws TelusException;
	
	public SapccOfferInfo[] retrieveSapccOfferInfo() throws TelusException, ApplicationException;
}