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
import java.util.Map;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.resource.UnknownPhoneNumberResourceException;
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
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.utility.info.*;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ReferenceDataHelperRemote extends EJBObject {

	/**
	 * Authenticates the user id / password
	 * 
	 * @param String
	 *            user id
	 * @param String
	 *            password
	 * @exception TelusException
	 */
	void authenticate(String userID, String password) throws TelusException, RemoteException;

	/**
	 * Retrieve array of services related to the given Price Plan in KB
	 * 
	 * @params String[] serviceList to filter
	 * @params Price Plan code
	 * @return Array of Service Codes
	 * @throws TelusException, RemoteException
	 */
	String[] filterServiceListByPricePlan(String[] serviceList, String pricePlan) throws TelusException, RemoteException;

	/**
	 * Retrieve array of services associated with the given Province in KB
	 * 
	 * @params String[] serviceList to filter
	 * @params Province code
	 * @return Array of service codes
	 * @throws TelusException, RemoteException
	 */
	String[] filterServiceListByProvince(String[] serviceList, String province) throws TelusException, RemoteException;

	/**
	 * Validates if Service have been ever associated with Price Plan as
	 * Included Promotion
	 * 
	 * @return boolean
	 * @throws TelusException, RemoteException
	 */
	boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException, RemoteException;

	/**
	 * Validates if Dealer Code is default dealer code
	 * 
	 * @return boolean
	 * @throws TelusException, RemoteException
	 */
	boolean isDefaultDealer(String dealerCode) throws TelusException, RemoteException;

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
	boolean isPortOutAllowed(String status, String activityCode, String activityReasonCode) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	boolean isPrivilegeAvailable(String businessRoleCode, String privilegeCode, String pServiceCode) throws TelusException, RemoteException;

	/**
	 * check to see Service associated to price plan
	 * 
	 * @return boolean
	 * @throws TelusException, RemoteException
	 */
	boolean isServiceAssociatedToPricePlan(String pricePlanCode, String serviceCode) throws TelusException, RemoteException;

	/**
	 * Retrieve list of account types
	 * 
	 * @return Collection of AccountTypeInfo
	 * @throws TelusException, RemoteException
	 */
	AccountTypeInfo[] retrieveAccountTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Activity Types
	 * 
	 * @return array of Activity Types
	 * @throws TelusException, RemoteException
	 */
	ActivityTypeInfo[] retrieveActivityTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Product Types
	 * 
	 * @return list of Product Types
	 * @throws TelusException, RemoteException
	 */
	AdjustmentReasonInfo[] retrieveAdjustmentReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve all provinces/states in the world.
	 * 
	 * @return list of provinces
	 * @throws TelusException, RemoteException
	 */
	ProvinceInfo[] retrieveAllProvinces() throws TelusException, RemoteException;

	/**
	 * Retrieve list of titles
	 * 
	 * @return ArrayList of TitleInfo
	 * @throws TelusException, RemoteException
	 */
	TitleInfo[] retrieveAllTitles() throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	Date retrieveAlternateRCContractStartDate(String province) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	double retrieveAlternateRecurringCharge(ServiceInfo serviceInfo, String pProvince, String pNpa, String pNxx, String pCorporateId) throws TelusException, RemoteException;

	/**
	 * Retreives reference Barcodes from DIST table CREDIT_AMOUNT.
	 * 
	 * @return AmountBarCode[]
	 * @exception TelusException
	 */
	AmountBarCodeInfo[] retrieveAmountBarCodes() throws TelusException, RemoteException;

	/**
	 * Retrieves foreign application messages' mappings.
	 * 
	 * @throws TelusException, RemoteException
	 */
	ApplicationMessageMappingInfo[] retrieveApplicationMessageMappings() throws TelusException, RemoteException;

	/**
	 * Retrieves all application messages.
	 * 
	 * @throws TelusException, RemoteException
	 */
	ApplicationMessageInfo[] retrieveApplicationMessages() throws TelusException, RemoteException;

	/**
	 * Retrieves all application summaries.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public ApplicationSummaryInfo[] retrieveApplicationSummaries() throws TelusException, RemoteException;

	/**
	 * Retrieves all audience types.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public AudienceTypeInfo[] retrieveAudienceTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve specific bill cycle
	 * 
	 * @params String bill cycle code
	 * @return BillCycle Info
	 * @throws TelusException, RemoteException
	 */
	public BillCycleInfo retrieveBillCycle(String pBillCycleCode) throws TelusException, RemoteException;

	/**
	 * Retrieve billing cycle with least number of bans
	 * @deprecated Do not use this method! 
	 * @return int cycle with min(no_of_bans)
	 * @throws TelusException, RemoteException
	 */
	public int retrieveBillCycleLeastUsed() throws TelusException, RemoteException;

	/**
	 * Retrieve list of bill cycles
	 * 
	 * It only returns bill cycles that can be allocated ie. where the
	 * allocation indicator is not equal 'N'.
	 * 
	 * @return Array of BillCycle Info
	 * @throws TelusException, RemoteException
	 */
	public BillCycleInfo[] retrieveBillCycles() throws TelusException, RemoteException;

	/**
	 * Retrieve list of bill cycles for a given population
	 * 
	 * It only returns bill cycles that can be allocated and that belong to the
	 * population requested.
	 * 
	 * @params String population code (A = all, E = end user, N = national, and
	 *         R = reseller)
	 * @return Array of BillCycle Info
	 * @throws TelusException, RemoteException
	 */
	public BillCycleInfo[] retrieveBillCycles(String pPopulationCode) throws TelusException, RemoteException;

	/**
	 * Retrieves all hold bill destinations.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public BillHoldRedirectDestinationInfo[] retrieveBillHoldRedirectDestinations() throws TelusException, RemoteException;

	/**
	 * Retrieves all equipment brand swap rules.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public BrandSwapRuleInfo[] retrieveBrandSwapRules() throws TelusException, RemoteException;

	/**
	 * Retrieve list of brands
	 * 
	 * @return Array of BrandInfo
	 * @throws TelusException, RemoteException
	 */
	public BrandInfo[] retrieveBrands() throws TelusException, RemoteException;

	/**
	 * Retrieve Business Roles
	 * 
	 * @return array of Business Roles
	 * @throws TelusException, RemoteException
	 */
	public BusinessRoleInfo[] retrieveBusinessRoles() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Client Consent Indicators.
	 */
	public ClientConsentIndicatorInfo[] retrieveClientConsentIndicators() throws TelusException, RemoteException;

	/**
	 * Retreives reference Client State Reasons from CODS.
	 */
	public ClientStateReasonInfo[] retrieveClientStateReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve collection activities
	 * 
	 * @return list of collection activities
	 * @throws TelusExceptio
	 */
	public CollectionActivityInfo[] retrieveCollectionActivities() throws TelusException, RemoteException;

	/**
	 * Retrieve collection agencies
	 * 
	 * @return list of collection agencies
	 * @throws TelusException, RemoteException
	 */
	public CollectionAgencyInfo[] retrieveCollectionAgencies() throws TelusException, RemoteException;

	/**
	 * May 23, 2006
	 * 
	 * @throws TelusException, RemoteException
	 */
	public CollectionPathDetailsInfo[] retrieveCollectionPathDetails() throws TelusException, RemoteException;

	/**
	 * Retrieve collection state list Collection State is combination of
	 * collection path and step
	 * 
	 * @return list of collection states
	 * @throws TelusException, RemoteException
	 */
	public CollectionStateInfo[] retrieveCollectionStates() throws TelusException, RemoteException;

	/**
	 * May 23, 2006
	 * 
	 * @throws TelusException, RemoteException
	 */
	public CollectionStepApprovalInfo[] retrieveCollectionStepApproval() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Commitment Reasons.
	 */
	public CommitmentReasonInfo[] retrieveCommitmentReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Corporate Account Representatives.
	 */
	public CorporateAccountRepInfo[] retrieveCorporateAccountReps() throws TelusException, RemoteException;

	/**
	 * Retrieve Countries
	 * 
	 * @return list of Countriess
	 * @throws TelusException, RemoteException
	 */
	public CountryInfo[] retrieveCountries() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Coverage Region for Pager for Fusion K2
	 * 
	 * @return Array of of CoverageRegionInfo
	 * @throws TelusException, RemoteException
	 */
	public CoverageRegionInfo[] retrieveCoverageRegions() throws TelusException, RemoteException;

	/**
	 * Retrieve Credit Card Payment Type
	 * 
	 * @return list of Credit Card Payment Types
	 * @throws TelusException, RemoteException
	 */
	public CreditCardPaymentTypeInfo[] retrieveCreditCardPaymentTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve CreditCardTypes
	 * 
	 * @return list of CreditCardTypes
	 * @throws TelusException, RemoteException
	 */
	public CreditCardTypeInfo[] retrieveCreditCardTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return CreditCheckDepositChangeReasonInfo[]
	 * @throws TelusException, RemoteException
	 */
	public CreditCheckDepositChangeReasonInfo[] retrieveCreditCheckDepositChangeReasons() throws TelusException, RemoteException;

	/**
	 * Retrieves all the credit classes.
	 * 
	 * @return CreditClassInfo[]
	 * @throws TelusException, RemoteException
	 */
	public CreditClassInfo[] retrieveCreditClasses() throws TelusException, RemoteException;

	/**
	 * Retrieve Credit Message by message code
	 * 
	 * @params String credit message code
	 * @return CreditMessageInfo
	 * @throws TelusException, RemoteException
	 */
	public CreditMessageInfo retrieveCreditMessageByCode(String pCode) throws TelusException, RemoteException;

	/**
	 * Retrieve list of Credit Messages
	 * 
	 * @return ArrayList of Credit Message Info
	 * @throws TelusException, RemoteException
	 */
	public CreditMessageInfo[] retrieveCreditMessages() throws TelusException, RemoteException;

	/**
	 * Retrieve sales rep info by dealer code and sales rep code including
	 * expired sales reps. Note: This method uses the REF database as its
	 * datasource.
	 * 
	 * @params String dealer code
	 * @params String sales rep code
	 * @params boolean indicates if including expired sales rep
	 * @return SalesRepInfo
	 * @throws TelusException, RemoteException
	 */
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException, RemoteException;

	/**
	 * Retrieve sales rep info by dealer code and sales rep code including
	 * expired sales reps. Note: This method uses the REF database as its
	 * datasource.
	 * 
	 * @params String dealer code
	 * @params String sales rep code
	 * @return SalesRepInfo
	 * @throws TelusException, RemoteException
	 */
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode) throws TelusException, RemoteException;

	/**
	 * Retrieve dealer info by dealer code even if dealer is expired. Note: This
	 * method uses the REF database as its datasource.
	 * 
	 * @params String dealer code
	 * @params boolean indicates if including expired dealer
	 * @return DealerInfo
	 * @throws TelusException, RemoteException
	 */
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode, boolean expired) throws TelusException, RemoteException;

	/**
	 * Retrieve dealer info by dealer code even if dealer is expired. Note: This
	 * method uses the REF database as its datasource.
	 * 
	 * @params String dealer code
	 * @return DealerInfo
	 * @throws TelusException, RemoteException
	 */
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode) throws TelusException, RemoteException;

	/**
	 * Retrieve Default LDC
	 * 
	 * @return Default LDC
	 * @throws TelusException, RemoteException
	 */
	public String retrieveDefaultLDC() throws TelusException, RemoteException;

	/**
	 * Retrieve Departments
	 * 
	 * @return list of Departments
	 * @throws TelusException, RemoteException
	 */
	public DepartmentInfo[] retrieveDepartments() throws TelusException, RemoteException;

	/**
	 * Returns the List of Discounts(Credits)
	 * 
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException, RemoteException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans() throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, long[] productPromoTypeList, boolean initialActivation, int term) throws TelusException, RemoteException;

	/**
	 * Returns the List of Discounts(Credits)
	 * 
	 * @param boolean current
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException, RemoteException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current) throws TelusException, RemoteException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * @param boolean current
	 * @param String
	 *            Price Plan Code
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException, RemoteException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, int term) throws TelusException, RemoteException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 * 
	 * @param boolean current
	 * @param String
	 *            Price Plan Code
	 * @param String
	 *            province
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 * @throws TelusException, RemoteException
	 */
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, int term) throws TelusException, RemoteException;

	/**
	 * Retrieve list of Encoder Format for Pager for Fusion K2
	 * 
	 * @return Array of of EncodingFormatInfo
	 * @throws TelusException, RemoteException
	 */
	public EncodingFormatInfo[] retrieveEncodingFormats() throws TelusException, RemoteException;

	/**
	 * Retrieve equipment possessions.
	 * 
	 * @return list of equipment possessions.
	 * @throws TelusException, RemoteException
	 */
	public com.telus.eas.equipment.info.EquipmentPossessionInfo[] retrieveEquipmentPossessions() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Product Codes with Product Type(Model) description
	 * 
	 * @return Array of of EquipmentProductTypeInfo
	 * @throws TelusException, RemoteException
	 */
	public EquipmentProductTypeInfo[] retrieveEquipmentProductTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Equipment Statuses
	 * 
	 * @return list of Equipment Statuses
	 * @throws TelusException, RemoteException
	 */
	public EquipmentStatusInfo[] retrieveEquipmentStatuses() throws TelusException, RemoteException;

	/**
	 * Retrieve Equipment Types
	 * 
	 * @return list of Equipment Types
	 * @throws TelusException, RemoteException
	 */
	public EquipmentTypeInfo[] retrieveEquipmentTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return ExceptionReasonInfo[]
	 * @throws TelusException, RemoteException
	 */
	public ExceptionReasonInfo[] retrieveExceptionReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve FeatureCategories
	 * 
	 * @return list of FeatureCategories
	 * @throws TelusException, RemoteException
	 */
	public FeatureInfo[] retrieveFeatureCategories() throws TelusException, RemoteException;

	/**
	 * Retrieve Features
	 * 
	 * @return list of Features
	 * @throws TelusException, RemoteException
	 */
	public FeatureInfo[] retrieveFeatures() throws TelusException, RemoteException;

	/**
	 * Retrieves FeeWaiverReasons
	 * 
	 * @throws TelusSystemException
	 * @return FeeWaiverReasonInfo[]
	 */
	public FeeWaiverReasonInfo[] retrieveFeeWaiverReasons() throws TelusException, RemoteException;

	/**
	 * Retrieves FeeWaiverTypes
	 * 
	 * @throws TelusSystemException
	 * @return FeeWaiverTypeInfo[]
	 */
	public FeeWaiverTypeInfo[] retrieveFeeWaiverTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Fleet by Fleet Identity
	 * 
	 * @params FleetIdentityInfo
	 * @return Fleet Info
	 * @throws TelusException, RemoteException
	 */
	public FleetInfo retrieveFleetByFleetIdentity(FleetIdentityInfo fleeIdentity) throws TelusException, RemoteException;

	/**
	 * Retrieve Fleet Classes
	 * 
	 * @return array of Fleet Classes
	 * @throws TelusException, RemoteException
	 */
	public FleetClassInfo[] retrieveFleetClasses() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Fleets by Fleet Type
	 * 
	 * @params String Fleet Type
	 * @return Collection of Fleet Info
	 * @throws TelusException, RemoteException
	 */
	public FleetInfo[] retrieveFleetsByFleetType(char fleetType) throws TelusException, RemoteException;

	/**
	 * Retrieve Follow Up close reason descriptions.
	 * 
	 * @param reasonCode
	 * @throws TelusException, RemoteException
	 */
	public FollowUpCloseReasonInfo retrieveFollowUpCloseReason(String reasonCode) throws TelusException, RemoteException;

	/**
	 * Retrieves a list of available Follow Up close reason codes with
	 * descriptions.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public FollowUpCloseReasonInfo[] retrieveFollowUpCloseReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Follow Up types
	 * 
	 * @return Collection of FollowUpTypeInfo
	 * @throws TelusException, RemoteException
	 */
	public FollowUpTypeInfo[] retrieveFollowUpTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves all name generations.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public GenerationInfo[] retrieveGenerations() throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public ServiceInfo[] retrieveIncludedPromotions(String pPricePlanCD, String pEquipmentType, String pNetworkType, String pProvinceCD, int term) throws TelusException, RemoteException;

	/**
	 * Retrieve list of Invoice Call Sort Order Types.
	 */
	public InvoiceCallSortOrderTypeInfo[] retrieveInvoiceCallSortOrderTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Invoice Suppression Levels
	 * 
	 * @return InvoiceSuppressionLevelInfo[] Array of Invoice Suppression Levels
	 * @throws TelusException, RemoteException
	 */
	public InvoiceSuppressionLevelInfo[] retrieveInvoiceSuppressionLevels() throws TelusException, RemoteException;

	/**
	 * Retrieve specific Knowbility Operator Id
	 * 
	 * @return Knowbility Operator Info
	 * @throws TelusException, RemoteException
	 */
	public KnowbilityOperatorInfo retrieveKnowbilityOperatorInfo(String pOperatorId) throws TelusException, RemoteException;

	/**
	 * Retrieve specific Knowbility Operator Id
	 * 
	 * @return Knowbility Operator Info
	 * @throws TelusException, RemoteException
	 */
	public KnowbilityOperatorInfo[] retrieveKnowbilityOperators() throws TelusException, RemoteException;

	/**
	 * Retrieve Languages
	 * 
	 * @return list of Languages
	 * @throws TelusException, RemoteException
	 */
	public LanguageInfo[] retrieveLanguages() throws TelusException, RemoteException;

//	public LetterInfo retrieveLetter(String letterCategory, String letterCode) throws TelusException, RemoteException;
//
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode, int version) throws TelusException, RemoteException;

	/**
	 * Retrieve list of Client Consent Indicators.
	 */
//	public LetterCategoryInfo[] retrieveLetterCategories() throws TelusException, RemoteException;
//
//	public LetterSubCategoryInfo[] retrieveLetterSubCategories() throws TelusException, RemoteException;
//
//	public LetterVariableInfo[] retrieveLetterVariables(String letterCategory, String letterCode, int letterVersion) throws TelusException, RemoteException;
//
//	public LetterInfo[] retrieveLettersByCategory(String letterCategory) throws TelusException, RemoteException;
//
//	public LetterInfo[] retrieveLettersByTitleKeyword(String titleKeyword) throws TelusException, RemoteException;

	/**
	 * Retrieve list of lock reasons
	 * 
	 * @return Array of LockReasonInfo
	 * @throws TelusException, RemoteException
	 */
	public LockReasonInfo[] retrieveLockReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve Logical Date
	 * 
	 * @return Date
	 * @throws TelusException, RemoteException
	 */
	public Date retrieveLogicalDate() throws TelusException, RemoteException;

	public ChargeTypeInfo[] retrieveManualChargeTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Provinces that Price Plan(or Service) is available in
	 * 
	 * @params String Price Plan(or Service) code
	 * @return Array of Province Codes
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveMarketProvinces(String pServiceCode) throws TelusException, RemoteException;

	/**
	 * Retrieves all memo type categories.
	 * 
	 * @return MemoTypeCategoryInfo[]
	 * @throws TelusException, RemoteException
	 */
	public MemoTypeCategoryInfo[] retrieveMemoTypeCategories() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Memo types
	 * 
	 * @return Collection of MemoTypeInfo
	 * @throws TelusException, RemoteException
	 */
	public MemoTypeInfo[] retrieveMemoTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves all exception reasons.
	 * 
	 * @return MigrationTypeInfo[]
	 * @throws TelusException, RemoteException
	 */
	public MigrationTypeInfo[] retrieveMigrationTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve array of service codes for minute contributing pooling services.
	 * 
	 * @return String[]
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveMinutePoolingContributorServiceCodes() throws TelusException, RemoteException;

	/**
	 * Retrieve list of NetworkTypes
	 * 
	 * @return ArrayList of NetworkTypeInfo
	 * @throws TelusException, RemoteException
	 */
	public NetworkTypeInfo[] retrieveNetworkTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Networks
	 * 
	 * @return array of Networks
	 * @throws TelusException, RemoteException
	 */
	public NetworkInfo[] retrieveNetworks() throws TelusException, RemoteException;

	/**
	 * retrieveNotificationMessageTemplateInfo
	 * 
	 * @throws TelusException, RemoteException
	 * @return NotificationMessageTemplateInfo[]
	 */
	public NotificationMessageTemplateInfo[] retrieveNotificationMessageTemplateInfo() throws TelusException, RemoteException;

	/**
	 * retrieveContactContentType()
	 * 
	 * @throws TelusException, RemoteException
	 * @return ContactContentTypeInfo[]
	 */
	public NotificationTypeInfo[] retrieveNotificationType() throws TelusException, RemoteException;

	/**
	 * Retrieve NPANXX array for MSISDN reservation
	 * 
	 * @param String
	 *            phone number
	 * @return String [] NPANXX Array for MSISDN reservation
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber) throws TelusException, RemoteException;

	/**
	 * Retrieve NPANXX array for MSISDN reservation
	 * 
	 * @param String
	 *            phone number
	 * @param boolean is this a ported in number?
	 * @return String [] NPANXX Array for MSISDN reservation
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber, boolean isPortedInNumber) throws TelusException, RemoteException;

	/**
	 * Retrieve Number Group by PhoneNumber, Product Type
	 * 
	 * @param String
	 *            Phone Number
	 * @param String
	 *            Product Type
	 * @return NumberGroupInfo NumberGroup information for specific phone number
	 * @throws TelusException, RemoteException
	 */
	public NumberGroupInfo retrieveNumberGroupByPhoneNumberProductType(String pPhoneNumber, String pProductType) throws TelusException, RemoteException;

	/**
	 * Retrieve Number Group by PhoneNumber, Product Type
	 * 
	 * @param String
	 *            Ported In Phone Number
	 * @param String
	 *            Product Type
	 * @return NumberGroupInfo NumberGroup information for specific phone number
	 * @throws TelusException, RemoteException
	 */
	public NumberGroupInfo retrieveNumberGroupByPortedInPhoneNumberProductType(String pPhoneNumber, String pProductType) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public NumberGroupInfo[] retrieveNumberGroupList(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType, String pMarketAreaCode) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public NumberGroupInfo[] retrieveNumberGroupList(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType) throws TelusException, RemoteException;

	/**
	 * Retrieve Collection of Number Groups by Number Location Product Type,
	 * Equipment Type, Province Code
	 * 
	 * @param String
	 *            product type
	 * @return Collection of NumberGroupInfo classes
	 * @throws TelusException, RemoteException
	 */
	public NumberGroupInfo[] retrieveNumberGroupListByProvince(char pAccountType, char pAccountSubType, String pProductType, String pEquipmentType, String pProvince) throws TelusException, RemoteException;

	public NumberRangeInfo[] retrieveNumberRanges() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Equipment Type for Pager for Fusion K2
	 * 
	 * @return Array of of EquipTypeInfo
	 * @throws TelusException, RemoteException
	 */
	public EquipmentTypeInfo[] retrievePagerEquipmentTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Channel Frequency for Pager for Fusion K2
	 * 
	 * @return Array of of PagerFrequencyInfo
	 * @throws TelusException, RemoteException
	 */
	public PagerFrequencyInfo[] retrievePagerFrequencies() throws TelusException, RemoteException;

	/**
	 * Retrieve Payment Method Type
	 * 
	 * @return list of Payment Method Types
	 * @throws TelusException, RemoteException
	 */
	public PaymentMethodTypeInfo[] retrievePaymentMethodTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Payment Source Type
	 * 
	 * @return list of Payment Source Types
	 * @throws TelusException, RemoteException
	 */
	public PaymentSourceTypeInfo[] retrievePaymentSourceTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Payment Transfer Reasons Added by Roman
	 * 
	 * @return list of Payment Transfer Reasons
	 * @throws TelusException, RemoteException
	 */
	public PaymentTransferReasonInfo[] retrievePaymentTransferReasons() throws TelusException, RemoteException;

	/**
	 * Retrieves PhoneNumberResource
	 * 
	 * @throws UnknownPhoneNumberResourceException
	 * @throws TelusException, RemoteException
	 * @return PhoneNumberResourceInfo
	 */
	public PhoneNumberResourceInfo retrievePhoneNumberResource(String phoneNumber) throws TelusException, RemoteException;

	/**
	 * Retrieve list of PoolingGroup
	 * 
	 * @return ArrayList of PoolingGroupInfo
	 * @throws TelusException, RemoteException
	 */
	public PoolingGroupInfo[] retrievePoolingGroups() throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Prepaid Adjustment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException, RemoteException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidAdjustmentReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve specific Prepaid Event Type
	 * 
	 * @param int Prepaid Event Type Id
	 * @return PrepaidEventTypeInfo Prepaid Event Type
	 * @throws TelusException, RemoteException
	 * @see PrepaidEventTypeInfo
	 */
	public PrepaidEventTypeInfo retrievePrepaidEventType(int eventTypeId) throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Prepaid Event Types
	 * 
	 * @return PrepaidEventTypeInfo[] Prepaid Event types
	 * @throws TelusException, RemoteException
	 * @see PrepaidEventTypeInfo
	 */
	public PrepaidEventTypeInfo[] retrievePrepaidEventTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Prepaid Feature Add Waive Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException, RemoteException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidFeatureAddWaiveReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Prepaid Manual Adjustment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException, RemoteException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidManualAdjustmentReasons() throws TelusException, RemoteException;

	/**
	 * Retrieves Prepaid Rates
	 */
	public PrepaidRateProfileInfo[] retrievePrepaidRates() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Prepaid Recharge Denominations
	 * 
	 * @return Array of of PrepaidRechargeDenominationInfo
	 * @throws TelusException, RemoteException
	 */
	public PrepaidRechargeDenominationInfo[] retrievePrepaidRechargeDenominations() throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Top Up Waive Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException, RemoteException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidTopUpWaiveReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve list of all Device Direct Fulfillment Reasons
	 * 
	 * @return PrepaidAdjustmentReasonInfo[] Prepaid Adjustment Reasons
	 * @throws TelusException
	 * @see PrepaidAdjustmentReasonInfo
	 */
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidDeviceDirectFulfillmentReasons() throws TelusException, RemoteException;

	/**
	 * Retrieve Collection of SOC Info by Price Plan Code
	 * 
	 * @param String
	 *            Price Plan
	 * @return SOC Info classes
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo retrievePricePlan(String pPricePlanCD) throws TelusAPIException, RemoteException;

	/**
	 * Retrieve Collection of SOC Info by Price Plan Code
	 * 
	 * @param String
	 *            Price Plan
	 * @return SOC Info classes
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId,ServiceFamilyTypeInfo[] ppsServices) throws TelusException, RemoteException;

	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, char accountType, char accountSubType, int pBrandId, 
			long[] pProductPromoTypeList, boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String activityCode, 
			String activityReasonCode, String networkType) throws TelusException, RemoteException;
	
	public PricePlanInfo[] retrievePricePlanList(PricePlanSelectionCriteriaInfo criteriaInfo , String[] offerGroupCodeList) throws TelusException ,RemoteException;
	public OfferPricePlanSetInfo retrieveOfferPricePlanInfo(PricePlanSelectionCriteriaInfo ppCriteriaInfo) throws TelusException,RemoteException;

	
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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String productType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, String networkType) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId, String networkType) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int brandId) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, long[] pProductPromoTypeList, boolean pInitialActivation, boolean currentPricePlansOnly, String networkType, String seatTypeCode)
			throws TelusException, RemoteException;
	
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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, String activityCode, String activityReasonCode,
			String networkType) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, long[] pProductPromoTypeList, boolean pInitialActivation, String networkType) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public PricePlanInfo[] retrievePricePlanList(String pProductType, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String networkType, String seatTypeCode) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 * @deprecated
	 */
	public PricePlanInfo[] retrievePricePlanListByAccType(String productType, String provinceCode, char accountType, String equipmentType, int brandId, boolean currentPlansOnly, boolean availableForActivationOnly, String networkType) throws TelusException, RemoteException;

	/**
	 * Retrieve all price plan terms
	 * 
	 * @return as array of PricePlanTermInfo
	 * @throws TelusException, RemoteException
	 */
	public PricePlanTermInfo[] retrievePricePlanTerms() throws TelusException, RemoteException;

	/**
	 * Retrieve Product Types
	 * 
	 * @return list of Product Types
	 * @throws TelusException, RemoteException
	 */
	public ProductTypeInfo[] retrieveProductTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Promo Term Info by Promo Service Code
	 * 
	 * @params String Promo Service Code
	 * @return PromoTermInfo
	 * @throws TelusException, RemoteException
	 */
	public PromoTermInfo retrievePromoTerm(String pPromoServiceCode) throws TelusException, RemoteException;

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
	 * @throws TelusException, RemoteException
	 */
	public String[] retrievePromotionalDiscounts(String pPricePlanCode, long[] productPromoTypeList, boolean initialActivation) throws TelusException, RemoteException;

	/**
	 * Retrieve list of provinces
	 * 
	 * @return Collection of ProvinceInfo
	 * @throws TelusException, RemoteException
	 */
	public ProvinceInfo[] retrieveProvinces() throws TelusException, RemoteException;

	public ProvinceInfo[] retrieveProvinces(String countryCode) throws TelusException, RemoteException;

	/**
	 * Retrieves Provisioning Platform Types
	 */
	public ProvisioningPlatformTypeInfo[] retrieveProvisioningPlatformTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves Provisioning Transaction Status
	 */
	public ProvisioningTransactionStatusInfo[] retrieveProvisioningTransactionStatuses() throws TelusException, RemoteException;

	/**
	 * Retrieves Provisioning Transaction Types
	 */
	public ProvisioningTransactionTypeInfo[] retrieveProvisioningTransactionTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Collection of Regular Services
	 * 
	 * @param String
	 *            Feature Category
	 * @param String
	 *            Product Type
	 * @param boolean Current
	 * @return Collection of Regular Services
	 * @throws TelusException, RemoteException
	 */
	public ServiceInfo[] retrieveRegularServices(String featureCategory, String productType, boolean current) throws TelusException, RemoteException;

	/**
	 * Retrieve Collection of Regular Services
	 * 
	 * @return Collection of Regular Services
	 * @throws TelusException, RemoteException
	 */
	public ServiceInfo[] retrieveRegularServices() throws TelusException, RemoteException;

	/**
	 * Retrieve array of ServiceRelationInfo for the given Service in KB
	 * 
	 * @params String Service Code
	 * @return Array of Service Relation Info
	 * @throws TelusException, RemoteException
	 */
	public ServiceRelationInfo[] retrieveRelations(String serviceCode) throws TelusException, RemoteException;

	/**
	 * Returns the Resource Status.
	 * 
	 * @param productType
	 *            The type of product
	 * @param resourceType
	 *            The type of resource
	 * @param resourceNumber
	 *            The number of resouce
	 * @throws TelusException, RemoteException
	 */
	public String retrieveResourceStatus(String productType, String resourceType, String resourceNumber) throws TelusException, RemoteException;

	/**
	 * Retrieve list of Handsets Roaming Capability
	 * 
	 * @return Array of Handsets Roaming Capability
	 * @throws TelusException, RemoteException
	 */
	public HandsetRoamingCapabilityInfo[] retrieveRoamingCapability() throws TelusException, RemoteException;

	/**
	 * Retrieves Routes
	 */
	public RouteInfo[] retrieveRoutes() throws TelusException, RemoteException;

	/**
	 * Retrieve all rules.
	 * 
	 * @return RuleInfo[]
	 * @throws TelusException, RemoteException
	 */
	public RuleInfo[] retrieveRules() throws TelusException, RemoteException;

	/**
	 * Retrieve rules by category.
	 * 
	 * @param int category
	 * @return RuleInfo[]
	 * @throws TelusException, RemoteException
	 */
	public RuleInfo[] retrieveRules(int category) throws TelusException, RemoteException;

	/**
	 * Retrieves Provisioning Transaction Types
	 */
	public SIDInfo[] retrieveSIDs() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Sales Rep by Dealer
	 * 
	 * @params String Dealer Code
	 * @return Collection of SalesRepInfo
	 * @throws TelusException, RemoteException
	 */
	public SalesRepInfo[] retrieveSalesRepListByDealer(String pDealerCD) throws TelusException, RemoteException;

	/**
	 * Retrieve list of segmentations
	 * 
	 * @return Array of SegmentationInfo
	 * @throws TelusException, RemoteException
	 */
	public SegmentationInfo[] retrieveSegmentations() throws TelusException, RemoteException;

	/**
	 * Retrieve Service Equipment Type Info
	 * 
	 * @return ServiceEquipmentTypeInfo
	 * @throws TelusException, RemoteException
	 */
	
	public ServiceEquipmentTypeInfo[] retrieveServiceEquipmentNetworkInfo(String serviceCode) throws TelusException, RemoteException;

	/**
	 * Retrieves retrieve Service Exclusion Groups for SOCs.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public ServiceExclusionGroupsInfo[] retrieveServiceExclusionGroups() throws TelusException, RemoteException;

	/**
	 * Retrieve array of Family Service Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @params String Family Type
	 * @params String Province Code
	 * @params String Equipment Type
	 * @params boolean current Price Plans Only indicator
	 * @return Array of Service Codes
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String provinceCode, String equipmentType, String networkType, boolean currentServicesOnly, int termInMonths) throws TelusException, RemoteException;

	/**
	 * Retrieve array of Family Service Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @return Array of Service Codes
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String networkType) throws TelusException, RemoteException;

	/**
	 * Retrieve array of Service Family Groups Codes for the given Service in KB
	 * 
	 * @params String Service Code
	 * @params String Family Type
	 * @return Array of Service Codes
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveServiceFamilyGroupCodes(String serviceCode, String familyType) throws TelusException, RemoteException;

	/**
	 * Retrieve array of Service Policy Info
	 * 
	 * @return Array of ServicePolicyInfo
	 * @throws TelusException, RemoteException
	 */
	public ServicePolicyInfo[] retrieveServicePolicyExceptions() throws TelusException, RemoteException;

	/**
	 * Retrieve ServiceUsageInfo by Service Code
	 * 
	 * @params String Service code
	 * @return ServiceUsageInfo
	 * @throws TelusException, RemoteException
	 */
	public ServiceUsageInfo retrieveServiceUsageInfo(String pServiceCode) throws TelusException, RemoteException;

	/**
	 * Retrieve list of special number range
	 * 
	 * @return Array of SpecialNumberRangeInfo
	 * @throws TelusException, RemoteException
	 */
	public SpecialNumberRangeInfo[] retrieveSpecialNumberRanges() throws TelusException, RemoteException;

	/**
	 * Retrieve list of special number
	 * 
	 * @return Array of SpecialNumberInfo
	 * @throws TelusException, RemoteException
	 */
	public SpecialNumberInfo[] retrieveSpecialNumbers() throws TelusException, RemoteException;

	/**
	 * Retrieve list of states
	 * 
	 * @return Array List of States
	 * @throws TelusException, RemoteException
	 */
	public StateInfo[] retrieveStates() throws TelusException, RemoteException;

	/**
	 * Retrieve list of Street Types
	 * 
	 * @return Array List of StreetTypesInfo
	 * @throws TelusException, RemoteException
	 */
	StreetTypeInfo[] retrieveStreetTypes() throws TelusException, RemoteException;

	/**
	 * Retreives reference Subscription Role Types from CODS.
	 */
	SubscriptionRoleTypeInfo[] retrieveSubscriptionRoleTypes() throws TelusException, RemoteException;

	/**
	 * Retrieves all equipment swap rules.
	 * 
	 * @throws TelusException, RemoteException
	 */
	SwapRuleInfo[] retrieveSwapRules() throws TelusException, RemoteException;

	/**
	 * Retrieve System Date
	 * 
	 * @return Date
	 * @throws TelusException, RemoteException
	 */
	Date retrieveSystemDate() throws TelusException, RemoteException;

	/**
	 * Retrieves all talk group priorities.
	 * 
	 * @throws TelusException, RemoteException
	 */
	TalkGroupPriorityInfo[] retrieveTalkGroupPriorities() throws TelusException, RemoteException;

	/**
	 * Retrieve List of Talk Groups by Fleet Identity
	 * 
	 * @params FleetIdentityInfo
	 * @return Collection of Talk Group Info
	 * @throws TelusException, RemoteException
	 */
	TalkGroupInfo[] retrieveTalkGroupsByFleetIdentity(FleetIdentityInfo fleetIdentityInfo) throws TelusException, RemoteException;

	/**
	 * Retrieve Tax Information for Provinces
	 * 
	 * @return Array of TaxationPolicyInfo
	 * @throws TelusException, RemoteException
	 */
	TaxationPolicyInfo[] retrieveTaxPolicies() throws TelusException, RemoteException;

	/**
	 * Retrieve list of titles
	 * 
	 * @return ArrayList of TitleInfo
	 * @throws TelusException, RemoteException
	 */
	TitleInfo[] retrieveTitles() throws TelusException, RemoteException;

	/**
	 * Retrieve Unit Types
	 * 
	 * @return list if Unit Types
	 * @throws TelusException, RemoteException
	 */
	UnitTypeInfo[] retrieveUnitTypes() throws TelusException, RemoteException;

	/**
	 * Retrieve Urban Id By Number Group
	 * 
	 * @params NumberGroupInfo
	 * @return int (Urban Id)
	 * @throws TelusException, RemoteException
	 */
	int retrieveUrbanIdByNumberGroup(NumberGroupInfo numberGroup) throws TelusException, RemoteException;

	/**
	 * Retrieve list of retrieveVendorServices
	 * 
	 * @return ArrayList of Vendor Services
	 * @throws TelusException, RemoteException
	 */
	public VendorServiceInfo[] retrieveVendorServices() throws TelusException, RemoteException;

	/**
	 * Retrieve specific WPS feature
	 * 
	 * @param int WPS feature id
	 * @return ServiceInfo WPS feature
	 * @throws ApplicationException, RemoteException
	 * @see ServiceInfo
	 */
	public ServiceInfo retrieveWPSFeature(int pFeatureId)throws TelusException, ApplicationException, RemoteException;

	/**
	 * Retrieve list of all WPS features
	 * 
	 * @return ServiceInfo[] WPS features
	 * @throws ApplicationException, RemoteException
	 * @see ServiceInfo
	 */
	public ServiceInfo[] retrieveWPSFeaturesList() throws TelusException, ApplicationException, RemoteException;

	/**
	 * Retrieves all work functions for a given department code.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public WorkFunctionInfo[] retrieveWorkFunctions(String departmentCode) throws TelusException, RemoteException;

	/**
	 * Retrieves all work functions.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public WorkFunctionInfo[] retrieveWorkFunctions() throws TelusException, RemoteException;

	/**
	 * Retrieves work position iformation by work position id.
	 * 
	 * @param workPositionId
	 * @throws TelusException, RemoteException
	 */
	public WorkPositionInfo retrieveWorkPosition(String workPositionId) throws TelusException, RemoteException;

	/**
	 * Retrieves work positions assigned to the given function code.
	 * 
	 * @param functionCode
	 * @throws TelusException, RemoteException
	 */
	public WorkPositionInfo[] retrieveWorkPositions(String functionCode) throws TelusException, RemoteException;

	/**
	 * Retrieves all work positions.
	 * 
	 * @throws TelusException, RemoteException
	 */
	public WorkPositionInfo[] retrieveWorkPositions() throws TelusException, RemoteException;

	/**
	 * Retrieve array of service codes for the zero-minute contributing pooling
	 * services.
	 * 
	 * @return String[]
	 * @throws TelusException, RemoteException
	 */
	public String[] retrieveZeroMinutePoolingContributorServiceCodes() throws TelusException, RemoteException;
	
//Added for Charge PaperBill - start - Anitha Duraisamy
	
	/**
	 * Retrieve array of one-time charge details for paper bill on the account.
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
	 * @return FeeRuleDto[] array of one-time charge details
	 * @throws TelusException
	 */
	public FeeRuleDto[] retrievePaperBillChargeType(int brandId, String provinceCode, char accountType, 
			char accountSubType, String segment,String invoiceSuppressionLevel,Date logicalDate ) throws TelusException, RemoteException;
	
	//Added for Charge PaperBill - END - Anitha Duraisamy	
	
	/**
	 * Retrieve SOC group and service codes mapping information.
	 * 
	 * @return String[] list of soc as values. 
	 * @throws TelusException,RemoteException
	 **/

	public String[] retrieveServiceGroupRelation(String serviceGroupCode)  throws TelusException,RemoteException ;
	
	/**
	 * Retrieves Service term information as defined in KB PROMOTION_TERMS table
	 *
	 * @param String serviceCode
	 * 
	 * @return ServiceTermDto
	 * @throws TelusException, RemoteException
	 */	
	public ServiceTermDto retrieveServiceTerm(String serviceCode) throws TelusException,RemoteException ;
	
	public int[] retrieveBillCycleListLeastUsed () throws TelusException,RemoteException ;
	
	public DataSharingGroupInfo[] retrieveDataSharingGroups() throws TelusException, RemoteException;

	public ServicePeriodInfo[] retrieveServicePeriodInfo( String serviceCode ) throws TelusException, RemoteException;
	
	public ServiceFeatureClassificationInfo[] retrieveServiceFeatureClassifications() throws TelusException, RemoteException;

	public ServiceAirTimeAllocationInfo retrieveVoiceAllocation( String serviceCode, Date effectiveDate, String sessionId ) throws ApplicationException, RemoteException;

	public String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;
	
	public Map retrieveServiceCodes() throws ApplicationException, RemoteException;
	
	public SeatTypeInfo[] retrieveSeatTypes() throws TelusException, RemoteException;
	
	/**
	 * returns a true of PPS service codes that are unavailable  for accountType/accountSubType
	 * 
	 * @return true
	 * @throws TelusException ,RemoteException
	 */
	public boolean isPPSEligible(char accountType, char accountSubType,ServiceFamilyTypeInfo[] ppsServices) throws TelusException,RemoteException;
	

	public ServiceExtendedInfo[] retrieveServiceExtendedInfo(String[] serviceCodes) throws TelusException, RemoteException;
	
	public RoamingServiceNotificationInfo[]  retrieveRoamingServiceNotificationInfo( String[] serviceCodes) throws TelusException,RemoteException;

	public ReferenceInfo[] retrieveMarketingDescriptions() throws TelusException, RemoteException;
		
	public SapccMappedServiceInfo[] retrieveSapccMappedServiceInfo() throws TelusException, RemoteException;
	
	public SapccOfferInfo[] retrieveSapccOfferInfo() throws TelusException, ApplicationException, RemoteException;
}