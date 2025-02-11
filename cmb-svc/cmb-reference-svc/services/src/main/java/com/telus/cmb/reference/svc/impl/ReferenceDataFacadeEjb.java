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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.BillCycle;
import com.telus.api.reference.TaxationPolicy;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacadeTestPoint;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.utility.info.*;
import com.telus.framework.config.ConfigContext;

/**
 * @author Pavel Simonovsky
 *
 */
@Stateless(name = "ReferenceDataFacade", mappedName = "ReferenceDataFacade")
@Remote({ ReferenceDataFacade.class, ReferenceDataFacadeTestPoint.class })
@RemoteHome(ReferenceDataFacadeHome.class)

@Interceptors({ SpringBeanAutowiringInterceptor.class, ReferenceFacadeSvcInvocationInterceptor.class })

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ReferenceDataFacadeEjb implements ReferenceDataFacade, ReferenceDataFacadeTestPoint {

	@Autowired
	private ReferenceDataFacade referenceDataFacade;

	public void setReferenceDataFacade(ReferenceDataFacade referenceDataFacade) {
		this.referenceDataFacade = referenceDataFacade;
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkServiceAssociation(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkServiceAssociation(String serviceCode, String pricePlanCode) throws TelusException {
		return referenceDataFacade.checkServiceAssociation(serviceCode, pricePlanCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkServicePrivilege(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkServicePrivilege(String[] serviceCodes, String businessRole, String privilege) throws TelusException {
		return referenceDataFacade.checkServicePrivilege(serviceCodes, businessRole, privilege);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkServicePrivilege(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkServicePrivilege(ServiceInfo[] services, String businessRole, String privilege) throws TelusException {
		return referenceDataFacade.checkServicePrivilege(services, businessRole, privilege);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#removeByPrivilege()
	 */
	@Override
	public ServiceInfo[] removeByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException {
		return referenceDataFacade.removeByPrivilege(services, businessRoleCode, privilegeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#retainByPrivilege()
	 */
	@Override
	public ServiceInfo[] retainByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode) throws TelusAPIException {
		return referenceDataFacade.retainByPrivilege(services, businessRoleCode, privilegeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#filterByPrivilege()
	 */
	@Override
	public ServiceInfo[] filterByPrivilege(ServiceInfo[] services, String businessRoleCode, String privilegeCode, boolean criteria) throws TelusException {
		return referenceDataFacade.filterByPrivilege(services, businessRoleCode, privilegeCode, criteria);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getAccountType(java.lang.String, int)
	 */
	@Override
	public AccountTypeInfo getAccountType(String accountTypeCode, int accountTypeBrandId) throws TelusException {
		return referenceDataFacade.getAccountType(accountTypeCode, accountTypeBrandId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getAccountTypes()
	 */
	@Override
	public AccountTypeInfo[] getAccountTypes() throws TelusException {
		return referenceDataFacade.getAccountTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityType(java.lang.String)
	 */
	@Override
	public ActivityTypeInfo getActivityType(String activityTypeCode) throws TelusException {
		return referenceDataFacade.getActivityType(activityTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityTypes()
	 */
	@Override
	public ActivityTypeInfo[] getActivityTypes() throws TelusException {
		return referenceDataFacade.getActivityTypes();
	}

	// Defect PROD00178088 Start
	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getActivityTypes(java.lang.String)
	 */
	@Override
	public ActivityTypeInfo[] getActivityTypes(String activityTypeCode) throws TelusException {
		return referenceDataFacade.getActivityTypes(activityTypeCode);
	}
	// Defect PROD00178088 End

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAddressType(java.lang.String)
	 */
	@Override
	public AddressTypeInfo getAddressType(String addressTypeCode) throws TelusException {
		return referenceDataFacade.getAddressType(addressTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAddressTypes()
	 */
	@Override
	public AddressTypeInfo[] getAddressTypes() throws TelusException {
		return referenceDataFacade.getAddressTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAdjustmentReason(java.lang.String)
	 */
	@Override
	public AdjustmentReasonInfo getAdjustmentReason(String reasonCode) throws TelusException {
		return referenceDataFacade.getAdjustmentReason(reasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAdjustmentReasons()
	 */
	@Override
	public AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException {
		return referenceDataFacade.getAdjustmentReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllFollowUpTypes()
	 */
	@Override
	public FollowUpTypeInfo[] getAllFollowUpTypes() throws TelusException {
		return referenceDataFacade.getAllFollowUpTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllLanguages()
	 */
	@Override
	public LanguageInfo[] getAllLanguages() throws TelusException {
		return referenceDataFacade.getAllLanguages();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllMemoTypes()
	 */
	@Override
	public MemoTypeInfo[] getAllMemoTypes() throws TelusException {
		return referenceDataFacade.getAllMemoTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllPrepaidRates()
	 */
	@Override
	public PrepaidRateProfileInfo[] getAllPrepaidRates() throws TelusException {
		return referenceDataFacade.getAllPrepaidRates();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllProvinces()
	 */
	@Override
	public ProvinceInfo[] getAllProvinces() throws TelusException {
		return referenceDataFacade.getAllProvinces();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAllTitles()
	 */
	@Override
	public TitleInfo[] getAllTitles() throws TelusException {
		return referenceDataFacade.getAllTitles();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAmountBarCodes()
	 */
	@Override
	public AmountBarCodeInfo[] getAmountBarCodes() throws TelusException {
		return referenceDataFacade.getAmountBarCodes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getApplicationSummary(java.lang.String)
	 */
	@Override
	public ApplicationSummaryInfo getApplicationSummary(String applicationCode) throws TelusException {
		return referenceDataFacade.getApplicationSummary(applicationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getAudienceType(java.lang.String)
	 */
	@Override
	public AudienceTypeInfo getAudienceType(String audienceTypeCode) throws TelusException {
		return referenceDataFacade.getAudienceType(audienceTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getAvailableNumberGroups(java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] getAvailableNumberGroups(String accountType, String accountSubType, String productType, String equipmentType, String marketAreaCode) throws TelusException {
		return referenceDataFacade.getAvailableNumberGroups(accountType, accountSubType, productType, equipmentType, marketAreaCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillCycle(java.lang.String)
	 */
	@Override
	public BillCycleInfo getBillCycle(String code) throws TelusException {
		return referenceDataFacade.getBillCycle(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillCycles()
	 */
	@Override
	public BillCycleInfo[] getBillCycles() throws TelusException {
		return referenceDataFacade.getBillCycles();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillHoldRedirectDestination(java.lang.String)
	 */
	@Override
	public BillHoldRedirectDestinationInfo getBillHoldRedirectDestination(String destinationCode) throws TelusException {
		return referenceDataFacade.getBillHoldRedirectDestination(destinationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBillHoldRedirectDestinations()
	 */
	@Override
	public BillHoldRedirectDestinationInfo[] getBillHoldRedirectDestinations() throws TelusException {
		return referenceDataFacade.getBillHoldRedirectDestinations();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrand(java.lang.String)
	 */
	@Override
	public BrandInfo getBrand(String code) throws TelusException {
		return referenceDataFacade.getBrand(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrandByBrandId(int)
	 */
	@Override
	public BrandInfo getBrandByBrandId(int brandId) throws TelusException {
		return referenceDataFacade.getBrandByBrandId(brandId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrands()
	 */
	@Override
	public BrandInfo[] getBrands() throws TelusException {
		return referenceDataFacade.getBrands();
	}

	@Override
	public BrandSwapRuleInfo[] getBrandSwapRules() throws TelusException {
		return referenceDataFacade.getBrandSwapRules();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBusinessRole(java.lang.String)
	 */
	@Override
	public BusinessRoleInfo getBusinessRole(String code) throws TelusException {
		return referenceDataFacade.getBusinessRole(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getBusinessRoles()
	 */
	@Override
	public BusinessRoleInfo[] getBusinessRoles() throws TelusException {
		return referenceDataFacade.getBusinessRoles();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientConsentIndicator(java.lang.String)
	 */
	@Override
	public ClientConsentIndicatorInfo getClientConsentIndicator(String code) throws TelusException {
		return referenceDataFacade.getClientConsentIndicator(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientConsentIndicators()
	 */
	@Override
	public ClientConsentIndicatorInfo[] getClientConsentIndicators() throws TelusException {
		return referenceDataFacade.getClientConsentIndicators();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientStateReason(java.lang.String)
	 */
	@Override
	public ClientStateReasonInfo getClientStateReason(String code) throws TelusException {
		return referenceDataFacade.getClientStateReason(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getClientStateReasons()
	 */
	@Override
	public ClientStateReasonInfo[] getClientStateReasons() throws TelusException {
		return referenceDataFacade.getClientStateReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivities()
	 */
	@Override
	public CollectionActivityInfo[] getCollectionActivities() throws TelusException {
		return referenceDataFacade.getCollectionActivities();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivity(java.lang.String, int)
	 */
	@Override
	public CollectionActivityInfo getCollectionActivity(String pathCode, int stepNumber) throws TelusException {
		return referenceDataFacade.getCollectionActivity(pathCode, stepNumber);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionActivity(java.lang.String)
	 */
	@Override
	public CollectionActivityInfo getCollectionActivity(String code) throws TelusException {
		return referenceDataFacade.getCollectionActivity(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionAgencies()
	 */
	@Override
	public CollectionAgencyInfo[] getCollectionAgencies() throws TelusException {
		return referenceDataFacade.getCollectionAgencies();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionAgency(java.lang.String)
	 */
	@Override
	public CollectionAgencyInfo getCollectionAgency(String code) throws TelusException {
		return referenceDataFacade.getCollectionAgency(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionPathDetails(java.lang.String)
	 */
	@Override
	public CollectionPathDetailsInfo[] getCollectionPathDetails(String pathCode) throws TelusException {
		return referenceDataFacade.getCollectionPathDetails(pathCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionPaths()
	 */
	@Override
	public String[] getCollectionPaths() throws TelusException {
		return referenceDataFacade.getCollectionPaths();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionState(java.lang.String)
	 */
	@Override
	public CollectionStateInfo getCollectionState(String code) throws TelusException {
		return referenceDataFacade.getCollectionState(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStates()
	 */
	@Override
	public CollectionStateInfo[] getCollectionStates() throws TelusException {
		return referenceDataFacade.getCollectionStates();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStepApproval(java.lang.String)
	 */
	@Override
	public CollectionStepApprovalInfo getCollectionStepApproval(String code) throws TelusException {
		return referenceDataFacade.getCollectionStepApproval(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCollectionStepApprovals()
	 */
	@Override
	public CollectionStepApprovalInfo[] getCollectionStepApprovals() throws TelusException {
		return referenceDataFacade.getCollectionStepApprovals();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getCommitmentReason(java.lang.String)
	 */
	@Override
	public CommitmentReasonInfo getCommitmentReason(String commitmentReasonCode) throws TelusException {
		return referenceDataFacade.getCommitmentReason(commitmentReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getCommitmentReasons()
	 */
	@Override
	public CommitmentReasonInfo[] getCommitmentReasons() throws TelusException {
		return referenceDataFacade.getCommitmentReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCorporateAccountRep(java.lang.String)
	 */
	@Override
	public CorporateAccountRepInfo getCorporateAccountRep(String code) throws TelusException {
		return referenceDataFacade.getCorporateAccountRep(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCorporateAccountReps()
	 */
	@Override
	public CorporateAccountRepInfo[] getCorporateAccountReps() throws TelusException {
		return referenceDataFacade.getCorporateAccountReps();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getCountries()
	 */
	@Override
	public CountryInfo[] getCountries() throws TelusException {
		return referenceDataFacade.getCountries();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCountries(boolean)
	 */
	@Override
	public CountryInfo[] getCountries(boolean includeForiegn) throws TelusException {
		return referenceDataFacade.getCountries(includeForiegn);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCountry(java.lang.String)
	 */
	@Override
	public CountryInfo getCountry(String code) throws TelusException {
		return referenceDataFacade.getCountry(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCoverageRegion(java.lang.String)
	 */
	@Override
	public CoverageRegionInfo getCoverageRegion(String code) throws TelusException {
		return referenceDataFacade.getCoverageRegion(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCoverageRegions()
	 */
	@Override
	public CoverageRegionInfo[] getCoverageRegions() throws TelusException {
		return referenceDataFacade.getCoverageRegions();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardPaymentType(java.lang.String)
	 */
	@Override
	public CreditCardPaymentTypeInfo getCreditCardPaymentType(String code) throws TelusException {
		return referenceDataFacade.getCreditCardPaymentType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardPaymentTypes()
	 */
	@Override
	public CreditCardPaymentTypeInfo[] getCreditCardPaymentTypes() throws TelusException {
		return referenceDataFacade.getCreditCardPaymentTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardType(java.lang.String)
	 */
	@Override
	public CreditCardTypeInfo getCreditCardType(String code) throws TelusException {
		return referenceDataFacade.getCreditCardType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCardTypes()
	 */
	@Override
	public CreditCardTypeInfo[] getCreditCardTypes() throws TelusException {
		return referenceDataFacade.getCreditCardTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCheckDepositChangeReason(java.lang.String)
	 */
	@Override
	public CreditCheckDepositChangeReasonInfo getCreditCheckDepositChangeReason(String code) throws TelusException {
		return referenceDataFacade.getCreditCheckDepositChangeReason(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditCheckDepositChangeReasons()
	 */
	@Override
	public CreditCheckDepositChangeReasonInfo[] getCreditCheckDepositChangeReasons() throws TelusException {
		return referenceDataFacade.getCreditCheckDepositChangeReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditClasses()
	 */
	@Override
	public CreditClassInfo[] getCreditClasses() throws TelusException {
		return referenceDataFacade.getCreditClasses();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditMessage(java.lang.String)
	 */
	@Override
	public CreditMessageInfo getCreditMessage(String code) throws TelusException {
		return referenceDataFacade.getCreditMessage(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getCreditMessages()
	 */
	@Override
	public CreditMessageInfo[] getCreditMessages() throws TelusException {
		return referenceDataFacade.getCreditMessages();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerOrderReferenceFacade#getDealerSalesRep(java.lang.String, java.lang.String)
	 */
	@Override
	public SalesRepInfo getDealerSalesRep(String dealerCode, String salesRepCode) throws TelusException {
		return referenceDataFacade.getDealerSalesRep(dealerCode, salesRepCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDepartment(java.lang.String)
	 */
	@Override
	public DepartmentInfo getDepartment(String code) throws TelusException {
		return referenceDataFacade.getDepartment(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDepartments()
	 */
	@Override
	public DepartmentInfo[] getDepartments() throws TelusException {
		return referenceDataFacade.getDepartments();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDiscountPlan(java.lang.String)
	 */
	@Override
	public DiscountPlanInfo getDiscountPlan(String discountCode) throws TelusException {
		return referenceDataFacade.getDiscountPlan(discountCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getDiscountPlans(boolean)
	 */
	@Override
	public DiscountPlanInfo[] getDiscountPlans(boolean current) throws TelusException {
		return referenceDataFacade.getDiscountPlans(current);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEncodingFormat(java.lang.String)
	 */
	@Override
	public EncodingFormatInfo getEncodingFormat(String code) throws TelusException {
		return referenceDataFacade.getEncodingFormat(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEncodingFormats()
	 */
	@Override
	public EncodingFormatInfo[] getEncodingFormats() throws TelusException {
		return referenceDataFacade.getEncodingFormats();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentPossession(java.lang.String)
	 */
	@Override
	public EquipmentPossessionInfo getEquipmentPossession(String code) throws TelusException {
		return referenceDataFacade.getEquipmentPossession(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentPossessions()
	 */
	@Override
	public EquipmentPossessionInfo[] getEquipmentPossessions() throws TelusException {
		return referenceDataFacade.getEquipmentPossessions();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentProductType(java.lang.String)
	 */
	@Override
	public EquipmentProductTypeInfo getEquipmentProductType(String code) throws TelusException {
		return referenceDataFacade.getEquipmentProductType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentProductTypes()
	 */
	@Override
	public EquipmentProductTypeInfo[] getEquipmentProductTypes() throws TelusException {
		return referenceDataFacade.getEquipmentProductTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentStatus(long, long)
	 */
	@Override
	public EquipmentStatusInfo getEquipmentStatus(long StatusID, long StatusTypeID) throws TelusException {
		return referenceDataFacade.getEquipmentStatus(StatusID, StatusTypeID);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentStatuses()
	 */
	@Override
	public EquipmentStatusInfo[] getEquipmentStatuses() throws TelusException {
		return referenceDataFacade.getEquipmentStatuses();
	}

	@Override
	public SwapRuleInfo[] getEquipmentSwapRules() throws TelusException {
		return referenceDataFacade.getEquipmentSwapRules();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentType(java.lang.String)
	 */
	@Override
	public EquipmentTypeInfo getEquipmentType(String equipTypeCode) throws TelusException {
		return referenceDataFacade.getEquipmentType(equipTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] getEquipmentTypes() throws TelusException {
		return referenceDataFacade.getEquipmentTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getEquivalentService(java.lang.String, java.lang.String)
	 */
	@Override
	public ServiceInfo getEquivalentService(String serviceCode, String pricePlanCode) throws TelusException {
		return referenceDataFacade.getEquivalentService(serviceCode, pricePlanCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getExceptionReason(java.lang.String)
	 */
	@Override
	public ExceptionReasonInfo getExceptionReason(String code) throws TelusException {
		return referenceDataFacade.getExceptionReason(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getExceptionReasons()
	 */
	@Override
	public ExceptionReasonInfo[] getExceptionReasons() throws TelusException {
		return referenceDataFacade.getExceptionReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getFeature(java.lang.String)
	 */
	@Override
	public FeatureInfo getFeature(String featureCode) throws TelusException {
		return referenceDataFacade.getFeature(featureCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeatureCategories()
	 */
	@Override
	public FeatureInfo[] getFeatureCategories() throws TelusException {
		return referenceDataFacade.getFeatureCategories();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeatureCategory(java.lang.String)
	 */
	@Override
	public FeatureInfo getFeatureCategory(String code) throws TelusException {
		return referenceDataFacade.getFeatureCategory(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getFeatures()
	 */
	@Override
	public FeatureInfo[] getFeatures() throws TelusException {
		return referenceDataFacade.getFeatures();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverReason(java.lang.String)
	 */
	@Override
	public FeeWaiverReasonInfo getFeeWaiverReason(String reasonCode) throws TelusException {
		return referenceDataFacade.getFeeWaiverReason(reasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverReasons()
	 */
	@Override
	public FeeWaiverReasonInfo[] getFeeWaiverReasons() throws TelusException {
		return referenceDataFacade.getFeeWaiverReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverType(java.lang.String)
	 */
	@Override
	public FeeWaiverTypeInfo getFeeWaiverType(String typeCode) throws TelusException {
		return referenceDataFacade.getFeeWaiverType(typeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFeeWaiverTypes()
	 */
	@Override
	public FeeWaiverTypeInfo[] getFeeWaiverTypes() throws TelusException {
		return referenceDataFacade.getFeeWaiverTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFleetById(int, int)
	 */
	@Override
	public FleetInfo getFleetById(int urbanID, int fleetId) throws TelusException {
		return referenceDataFacade.getFleetById(urbanID, fleetId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFleetClass(java.lang.String)
	 */
	@Override
	public FleetClassInfo getFleetClass(String code) throws TelusException {
		return referenceDataFacade.getFleetClass(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFleetClasses()
	 */
	@Override
	public FleetClassInfo[] getFleetClasses() throws TelusException {
		return referenceDataFacade.getFleetClasses();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpCloseReason(java.lang.String)
	 */
	@Override
	public FollowUpCloseReasonInfo getFollowUpCloseReason(String reasonCode) throws TelusException {
		return referenceDataFacade.getFollowUpCloseReason(reasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpCloseReasons()
	 */
	@Override
	public FollowUpCloseReasonInfo[] getFollowUpCloseReasons() throws TelusException {
		return referenceDataFacade.getFollowUpCloseReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpType(java.lang.String)
	 */
	@Override
	public FollowUpTypeInfo getFollowUpType(String code) throws TelusException {
		return referenceDataFacade.getFollowUpType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getFollowUpTypes()
	 */
	@Override
	public FollowUpTypeInfo[] getFollowUpTypes() throws TelusException {
		return referenceDataFacade.getFollowUpTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getGeneration(java.lang.String)
	 */
	@Override
	public GenerationInfo getGeneration(String generationCode) throws TelusException {
		return referenceDataFacade.getGeneration(generationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getGenerations()
	 */
	@Override
	public GenerationInfo[] getGenerations() throws TelusException {
		return referenceDataFacade.getGenerations();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceCallSortOrderType(java.lang.String)
	 */
	@Override
	public InvoiceCallSortOrderTypeInfo getInvoiceCallSortOrderType(String code) throws TelusException {
		return referenceDataFacade.getInvoiceCallSortOrderType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceCallSortOrderTypes()
	 */
	@Override
	public InvoiceCallSortOrderTypeInfo[] getInvoiceCallSortOrderTypes() throws TelusException {
		return referenceDataFacade.getInvoiceCallSortOrderTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceSuppressionLevel(java.lang.String)
	 */
	@Override
	public InvoiceSuppressionLevelInfo getInvoiceSuppressionLevel(String code) throws TelusException {
		return referenceDataFacade.getInvoiceSuppressionLevel(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getInvoiceSuppressionLevels()
	 */
	@Override
	public InvoiceSuppressionLevelInfo[] getInvoiceSuppressionLevels() throws TelusException {
		return referenceDataFacade.getInvoiceSuppressionLevels();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getKnowbilityOperator(java.lang.String)
	 */
	@Override
	public KnowbilityOperatorInfo getKnowbilityOperator(String code) throws TelusException {
		return referenceDataFacade.getKnowbilityOperator(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLanguage(java.lang.String)
	 */
	@Override
	public LanguageInfo getLanguage(String code) throws TelusException {
		return referenceDataFacade.getLanguage(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getLanguages()
	 */
	@Override
	public LanguageInfo[] getLanguages() throws TelusException {
		return referenceDataFacade.getLanguages();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterCategories()
	 */
//	@Override
//	public LetterCategoryInfo[] getLetterCategories() throws TelusException {
//		return referenceDataFacade.getLetterCategories();
//	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterCategory(java.lang.String)
	 */
//	@Override
//	public LetterCategoryInfo getLetterCategory(String letterCategory) throws TelusException {
//		return referenceDataFacade.getLetterCategory(letterCategory);
//	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterSubCategories(java.lang.String)
	 */
//	@Override
//	public LetterSubCategoryInfo[] getLetterSubCategories(String letterCategory) throws TelusException {
//		return referenceDataFacade.getLetterSubCategories(letterCategory);
//	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterSubCategory(java.lang.String)
	 */
//	@Override
//	public LetterSubCategoryInfo getLetterSubCategory(String letterSubCategory) throws TelusException {
//		return referenceDataFacade.getLetterSubCategory(letterSubCategory);
//	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLetterSubCategory(java.lang.String,java.lang.String)
	 */
//	@Override
//	public LetterSubCategoryInfo getLetterSubCategory(String letterCategory, String letterSubCategory) throws TelusException {
//		return referenceDataFacade.getLetterSubCategory(letterCategory, letterSubCategory);
//	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLockReason(java.lang.String)
	 */
	@Override
	public LockReasonInfo getLockReason(String code) throws TelusException {
		return referenceDataFacade.getLockReason(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getLockReasons()
	 */
	@Override
	public LockReasonInfo[] getLockReasons() throws TelusException {
		return referenceDataFacade.getLockReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getLogicalDate()
	 */
	@Override
	public Date getLogicalDate() throws TelusException {
		return referenceDataFacade.getLogicalDate();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMandatoryServices(java.lang.String, boolean, boolean, boolean, boolean, boolean)
	 */
	@Override
	public ServiceSetInfo[] getMandatoryServices(String pricePlanCode, String handSetType, String productType, String equipmentType, String provinceCode, String accountType, String accountSubType,
			long brandId) throws TelusException {

		return referenceDataFacade.getMandatoryServices(pricePlanCode, handSetType, productType, equipmentType, provinceCode, accountType, accountSubType, brandId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getManualChargeType(java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getManualChargeType(String code) throws TelusException {
		return referenceDataFacade.getManualChargeType(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getManualChargeTypes()
	 */
	@Override
	public ChargeTypeInfo[] getManualChargeTypes() throws TelusException {
		return referenceDataFacade.getManualChargeTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getMemoType(java.lang.String)
	 */
	@Override
	public MemoTypeInfo getMemoType(String memoTypeCode) throws TelusException {
		return referenceDataFacade.getMemoType(memoTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMemoTypeCategories()
	 */
	@Override
	public MemoTypeCategoryInfo[] getMemoTypeCategories() throws TelusException {
		return referenceDataFacade.getMemoTypeCategories();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMemoTypeCategory(java.lang.String)
	 */
	@Override
	public MemoTypeCategoryInfo getMemoTypeCategory(String categoryCode) throws TelusException {
		return referenceDataFacade.getMemoTypeCategory(categoryCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getMemoTypes()
	 */
	@Override
	public MemoTypeInfo[] getMemoTypes() throws TelusException {
		return referenceDataFacade.getMemoTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMigrationType(java.lang.String)
	 */
	@Override
	public MigrationTypeInfo getMigrationType(String migrationCode) throws TelusException {
		return referenceDataFacade.getMigrationType(migrationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMigrationTypes()
	 */
	@Override
	public MigrationTypeInfo[] getMigrationTypes() throws TelusException {
		return referenceDataFacade.getMigrationTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getMinutePoolingContributorServices()
	 */
	@Override
	public ServiceInfo[] getMinutePoolingContributorServices() throws TelusException {
		return referenceDataFacade.getMinutePoolingContributorServices();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNetwork(java.lang.String)
	 */
	@Override
	public NetworkInfo getNetwork(String networkCode) throws TelusException {
		return referenceDataFacade.getNetwork(networkCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNetworks()
	 */
	@Override
	public NetworkInfo[] getNetworks() throws TelusException {
		return referenceDataFacade.getNetworks();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNetworkTypes()
	 */
	@Override
	public NetworkTypeInfo[] getNetworkTypes() throws TelusException {
		return referenceDataFacade.getNetworkTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationMessageTemplate(int)
	 */
	@Override
	public NotificationMessageTemplateInfo getNotificationMessageTemplate(int notificationTypeCode) throws TelusException {
		return referenceDataFacade.getNotificationMessageTemplate(notificationTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationMessageTemplate(java.lang.String)
	 */
	@Override
	public NotificationMessageTemplateInfo getNotificationMessageTemplate(String code) throws TelusException {
		return referenceDataFacade.getNotificationMessageTemplate(code);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationMessageTemplates()
	 */
	@Override
	public NotificationMessageTemplateInfo[] getNotificationMessageTemplates() throws TelusException {
		return referenceDataFacade.getNotificationMessageTemplates();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationType(int)
	 */
	@Override
	public NotificationTypeInfo getNotificationType(int notificationTypeCode) throws TelusException {
		return referenceDataFacade.getNotificationType(notificationTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationType(java.lang.String)
	 */
	@Override
	public NotificationTypeInfo getNotificationType(String notificationCode) throws TelusException {
		return referenceDataFacade.getNotificationType(notificationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNotificationTypes()
	 */
	@Override
	public NotificationTypeInfo[] getNotificationTypes() throws TelusException {
		return referenceDataFacade.getNotificationTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getNumberGroupByPhoneNumberAndProductType(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException {
		return referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(phoneNumber, productType);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ResourceOrderReferenceFacade#getNumberGroupByPortedInPhoneNumberAndProductType(java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo getNumberGroupByPortedInPhoneNumberAndProductType(String phoneNumber, String productType) throws TelusException {
		return referenceDataFacade.getNumberGroupByPortedInPhoneNumberAndProductType(phoneNumber, productType);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNumberRange(java.lang.String)
	 */
	@Override
	public NumberRangeInfo getNumberRange(String npaNxx) throws TelusException {
		return referenceDataFacade.getNumberRange(npaNxx);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getNumberRanges()
	 */
	@Override
	public NumberRangeInfo[] getNumberRanges() throws TelusException {
		return referenceDataFacade.getNumberRanges();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPagerEquipmentType(java.lang.String)
	 */
	@Override
	public EquipmentTypeInfo getPagerEquipmentType(String equipmentCode) throws TelusException {
		return referenceDataFacade.getPagerEquipmentType(equipmentCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPagerEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] getPagerEquipmentTypes() throws TelusException {
		return referenceDataFacade.getPagerEquipmentTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPagerFrequencies()
	 */
	@Override
	public PagerFrequencyInfo[] getPagerFrequencies() throws TelusException {
		return referenceDataFacade.getPagerFrequencies();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPagerFrequency(java.lang.String)
	 */
	@Override
	public PagerFrequencyInfo getPagerFrequency(String frequencyCode) throws TelusException {
		return referenceDataFacade.getPagerFrequency(frequencyCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentMethod(java.lang.String)
	 */
	@Override
	public PaymentMethodInfo getPaymentMethod(String paymentMethodCode) throws TelusException {
		return referenceDataFacade.getPaymentMethod(paymentMethodCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPaymentMethods()
	 */
	@Override
	public PaymentMethodInfo[] getPaymentMethods() throws TelusException {
		return referenceDataFacade.getPaymentMethods();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentMethodType(java.lang.String)
	 */
	@Override
	public PaymentMethodTypeInfo getPaymentMethodType(String paymentMethodTypeCode) throws TelusException {
		return referenceDataFacade.getPaymentMethodType(paymentMethodTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentMethodTypes()
	 */
	@Override
	public PaymentMethodTypeInfo[] getPaymentMethodTypes() throws TelusException {
		return referenceDataFacade.getPaymentMethodTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentSourceType(java.lang.String)
	 */
	@Override
	public PaymentSourceTypeInfo getPaymentSourceType(String paymentSourceTypeCode) throws TelusException {
		return referenceDataFacade.getPaymentSourceType(paymentSourceTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPaymentSourceTypes()
	 */
	@Override
	public PaymentSourceTypeInfo[] getPaymentSourceTypes() throws TelusException {
		return referenceDataFacade.getPaymentSourceTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentTransferReason(java.lang.String)
	 */
	@Override
	public PaymentTransferReasonInfo getPaymentTransferReason(String transferReasonCode) throws TelusException {
		return referenceDataFacade.getPaymentTransferReason(transferReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaymentTransferReasons()
	 */
	@Override
	public PaymentTransferReasonInfo[] getPaymentTransferReasons() throws TelusException {
		return referenceDataFacade.getPaymentTransferReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPhoneType(java.lang.String)
	 */
	@Override
	public PhoneTypeInfo getPhoneType(String phoneTypeCode) throws TelusException {
		return referenceDataFacade.getPhoneType(phoneTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPhoneTypes()
	 */
	@Override
	public PhoneTypeInfo[] getPhoneTypes() throws TelusException {
		return referenceDataFacade.getPhoneTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPoolingGroup(java.lang.String)
	 */
	@Override
	public PoolingGroupInfo getPoolingGroup(String poolingGroupCode) throws TelusException {
		return referenceDataFacade.getPoolingGroup(poolingGroupCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPoolingGroups()
	 */
	@Override
	public PoolingGroupInfo[] getPoolingGroups() throws TelusException {
		return referenceDataFacade.getPoolingGroups();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidAdjustmentReason()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidAdjustmentReason() throws TelusException {
		return referenceDataFacade.getPrepaidAdjustmentReason();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidAdjustmentReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidAdjustmentReason(String adjustmentReasonCode) throws TelusException {
		return referenceDataFacade.getPrepaidAdjustmentReason(adjustmentReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidEventType(java.lang.String)
	 */
	@Override
	public PrepaidEventTypeInfo getPrepaidEventType(String eventTypeCode) throws TelusException {
		return referenceDataFacade.getPrepaidEventType(eventTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidEventTypes()
	 */
	@Override
	public PrepaidEventTypeInfo[] getPrepaidEventTypes() throws TelusException {
		return referenceDataFacade.getPrepaidEventTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidFeatureAddWaiveReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidFeatureAddWaiveReason(String featureAddWaiveReasonCode) throws TelusException {
		return referenceDataFacade.getPrepaidFeatureAddWaiveReason(featureAddWaiveReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidFeatureAddWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidFeatureAddWaiveReasons() throws TelusException {
		return referenceDataFacade.getPrepaidFeatureAddWaiveReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidManualAdjustmentReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidManualAdjustmentReason(String adjustmentReasonCode) throws TelusException {
		return referenceDataFacade.getPrepaidManualAdjustmentReason(adjustmentReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidManualAdjustmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidManualAdjustmentReasons() throws TelusException {
		return referenceDataFacade.getPrepaidManualAdjustmentReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPrepaidRechargeDenominations()
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations() throws TelusException {
		return referenceDataFacade.getPrepaidRechargeDenominations();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidRechargeDenominations(java.lang.String)
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations(String rechargeType) throws TelusException {
		return referenceDataFacade.getPrepaidRechargeDenominations(rechargeType);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidTopUpWaiveReason(java.lang.String)
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidTopUpWaiveReason(String topUpWaiveCode) throws TelusException {
		return referenceDataFacade.getPrepaidTopUpWaiveReason(topUpWaiveCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidTopUpWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidTopUpWaiveReasons() throws TelusException {
		return referenceDataFacade.getPrepaidTopUpWaiveReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidDeviceDirectFulfillmentReason()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo getPrepaidDeviceDirectFulfillmentReason(String deviceDirectFulfillmentReasonCode) throws TelusException {
		return referenceDataFacade.getPrepaidDeviceDirectFulfillmentReason(deviceDirectFulfillmentReasonCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPrepaidDeviceDirectFulfillmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] getPrepaidDeviceDirectFulfillmentReasons() throws TelusException {
		return referenceDataFacade.getPrepaidDeviceDirectFulfillmentReasons();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, java.lang.String, int)
	 */
	@Override
	public PricePlanInfo getPricePlan(String productType, String pricePlanCode, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId)
			throws TelusException {
		return referenceDataFacade.getPricePlan(productType, pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlan(java.lang.String)
	 */
	@Override
	public PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException {
		return referenceDataFacade.getPricePlan(pricePlanCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPricePlans(com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException {
		return referenceDataFacade.getPricePlans(criteriaInfo);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, int, int)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, int term) throws TelusException {
		return referenceDataFacade.getPricePlans(productType, equipmentType, provinceCode, accountType, accountSubType, brandId, term);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, int, long[], boolean, boolean, boolean, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, long[] productPromoTypes,
			boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String activityCode, String activityReasonCode, String networkType,
			String seatTypeCode) throws TelusException {
		return referenceDataFacade.getPricePlans(productType, equipmentType, provinceCode, accountType, accountSubType, brandId, productPromoTypes, initialActivation, currentPricePlansOnly,
				availableForActivationOnly, term, activityCode, activityReasonCode, networkType, seatTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId) throws TelusException {
		return referenceDataFacade.getPricePlans(productType, equipmentType, provinceCode, accountType, accountSubType, brandId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlans(java.lang.String[])
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String[] pricePlanCode) throws TelusException {
		return referenceDataFacade.getPricePlans(pricePlanCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPricePlanTerm(java.lang.String)
	 */
	@Override
	public PricePlanTermInfo getPricePlanTerm(String pricePlanCode) throws TelusException {
		return referenceDataFacade.getPricePlanTerm(pricePlanCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProductType(java.lang.String)
	 */
	@Override
	public ProductTypeInfo getProductType(String productTypeCode) throws TelusException {
		return referenceDataFacade.getProductType(productTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProductTypes()
	 */
	@Override
	public ProductTypeInfo[] getProductTypes() throws TelusException {
		return referenceDataFacade.getProductTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPromoTerm(java.lang.String)
	 */
	@Override
	public PromoTermInfo getPromoTerm(String promoCode) throws TelusException {
		return referenceDataFacade.getPromoTerm(promoCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvince(java.lang.String, java.lang.String)
	 */
	@Override
	public ProvinceInfo getProvince(String countryCode, String provinceCode) throws TelusException {
		return referenceDataFacade.getProvince(countryCode, provinceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvince(java.lang.String)
	 */
	@Override
	public ProvinceInfo getProvince(String provinceCode) throws TelusException {
		return referenceDataFacade.getProvince(provinceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getProvinces()
	 */
	@Override
	public ProvinceInfo[] getProvinces() throws TelusException {
		return referenceDataFacade.getProvinces();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvinces(java.lang.String)
	 */
	@Override
	public ProvinceInfo[] getProvinces(String countryCode) throws TelusException {
		return referenceDataFacade.getProvinces(countryCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningPlatformType(java.lang.String)
	 */
	@Override
	public ProvisioningPlatformTypeInfo getProvisioningPlatformType(String provisioningPlatformId) throws TelusException {
		return referenceDataFacade.getProvisioningPlatformType(provisioningPlatformId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningPlatformTypes()
	 */
	@Override
	public ProvisioningPlatformTypeInfo[] getProvisioningPlatformTypes() throws TelusException {
		return referenceDataFacade.getProvisioningPlatformTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningTransactionStatus(java.lang.String)
	 */
	@Override
	public ProvisioningTransactionStatusInfo getProvisioningTransactionStatus(String txStatusCode) throws TelusException {
		return referenceDataFacade.getProvisioningTransactionStatus(txStatusCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningTransactionStatuses()
	 */
	@Override
	public ProvisioningTransactionStatusInfo[] getProvisioningTransactionStatuses() throws TelusException {
		return referenceDataFacade.getProvisioningTransactionStatuses();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningTransactionType(java.lang.String)
	 */
	@Override
	public ProvisioningTransactionTypeInfo getProvisioningTransactionType(String txTypeCode) throws TelusException {
		return referenceDataFacade.getProvisioningTransactionType(txTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getProvisioningTransactionTypes()
	 */
	@Override
	public ProvisioningTransactionTypeInfo[] getProvisioningTransactionTypes() throws TelusException {
		return referenceDataFacade.getProvisioningTransactionTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getRegularService(java.lang.String)
	 */
	@Override
	public ServiceInfo getRegularService(String regularServiceCode) throws TelusException {
		return referenceDataFacade.getRegularService(regularServiceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRegularServices()
	 */
	@Override
	public ServiceInfo[] getRegularServices() throws TelusException {
		return referenceDataFacade.getRegularServices();
	}

	@Override
	public ServiceInfo retrieveRegularService(String serviceCode) throws TelusException {
		return referenceDataFacade.retrieveRegularService(serviceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getRegularServices(java.lang.String[])
	 */
	@Override
	public ServiceInfo[] getRegularServices(String[] serviceCode) throws TelusException {
		return referenceDataFacade.getRegularServices(serviceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRoamingCapability()
	 */
	@Override
	public HandsetRoamingCapabilityInfo[] getRoamingCapability() throws TelusException {
		return referenceDataFacade.getRoamingCapability();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRoute(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public RouteInfo getRoute(String switchId, String routeId) throws TelusException {
		return referenceDataFacade.getRoute(switchId, routeId);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRoutes()
	 */
	@Override
	public RouteInfo[] getRoutes() throws TelusException {
		return referenceDataFacade.getRoutes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRuralDeliveryType(java.lang.String)
	 */
	@Override
	public RuralDeliveryTypeInfo getRuralDeliveryType(String deliveryTypeCode) throws TelusException {
		return referenceDataFacade.getRuralDeliveryType(deliveryTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRuralDeliveryTypes()
	 */
	@Override
	public RuralDeliveryTypeInfo[] getRuralDeliveryTypes() throws TelusException {
		return referenceDataFacade.getRuralDeliveryTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRuralType(java.lang.String)
	 */
	@Override
	public RuralTypeInfo getRuralType(String ruralTypeCode) throws TelusException {
		return referenceDataFacade.getRuralType(ruralTypeCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getRuralTypes()
	 */
	@Override
	public RuralTypeInfo[] getRuralTypes() throws TelusException {
		return referenceDataFacade.getRuralTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getSegmentation(int, java.lang.String, java.lang.String)
	 */
	@Override
	public SegmentationInfo getSegmentation(int brandId, String accountTypeCode, String provinceCode) throws TelusException {
		return referenceDataFacade.getSegmentation(brandId, accountTypeCode, provinceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSegmentation(java.lang.String)
	 */
	@Override
	public SegmentationInfo getSegmentation(String segmentationCode) throws TelusException {
		return referenceDataFacade.getSegmentation(segmentationCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.CustomerInformationReferenceFacade#getSegmentations()
	 */
	@Override
	public SegmentationInfo[] getSegmentations() throws TelusException {
		return referenceDataFacade.getSegmentations();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServiceExclusionGroups()
	 */
	@Override
	public ServiceExclusionGroupsInfo[] getServiceExclusionGroups() throws TelusException {
		return referenceDataFacade.getServiceExclusionGroups();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServiceExclusionGroups(java.lang.String)
	 */
	@Override
	public ServiceExclusionGroupsInfo getServiceExclusionGroups(String serviceExclusionGroupCode) throws TelusException {
		return referenceDataFacade.getServiceExclusionGroups(serviceExclusionGroupCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServicePeriodType(java.lang.String)
	 */
	@Override
	public ServicePeriodTypeInfo getServicePeriodType(String servicePeriodTypeCode) throws TelusException {
		return referenceDataFacade.getServicePeriodType(servicePeriodTypeCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServicePeriodTypes()
	 */
	@Override
	public ServicePeriodTypeInfo[] getServicePeriodTypes() throws TelusException {
		return referenceDataFacade.getServicePeriodTypes();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServicePolicy(java.lang.String)
	 */
	@Override
	public ServicePolicyInfo getServicePolicy(String serviceCode) throws TelusException {
		return referenceDataFacade.getServicePolicy(serviceCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServicesByFeatureCategory(java.lang.String,
	 *      java.lang.String, boolean)
	 */
	@Override
	public ServiceInfo[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusException {
		return referenceDataFacade.getServicesByFeatureCategory(featureCategory, productType, current);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServiceUsage(java.lang.String)
	 */
	@Override
	public ServiceUsageInfo getServiceUsage(String serviceCode) throws TelusException {
		return referenceDataFacade.getServiceUsage(serviceCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSID(java.lang.String)
	 */
	@Override
	public SIDInfo getSID(String sIDCode) throws TelusException {
		return referenceDataFacade.getSID(sIDCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSIDs()
	 */
	@Override
	public SIDInfo[] getSIDs() throws TelusException {
		return referenceDataFacade.getSIDs();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSpecialNumber(java.lang.String)
	 */
	@Override
	public SpecialNumberInfo getSpecialNumber(String numberCode) throws TelusException {
		return referenceDataFacade.getSpecialNumber(numberCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSpecialNumberRange(java.lang.String)
	 */
	@Override
	public SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber) throws TelusException {
		return referenceDataFacade.getSpecialNumberRange(phoneNumber);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSpecialNumberRanges()
	 */
	@Override
	public SpecialNumberRangeInfo[] getSpecialNumberRanges() throws TelusException {
		return referenceDataFacade.getSpecialNumberRanges();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSpecialNumbers()
	 */
	@Override
	public SpecialNumberInfo[] getSpecialNumbers() throws TelusException {
		return referenceDataFacade.getSpecialNumbers();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getState(java.lang.String)
	 */
	@Override
	public StateInfo getState(String stateCode) throws TelusException {
		return referenceDataFacade.getState(stateCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getStates()
	 */
	@Override
	public StateInfo[] getStates() throws TelusException {
		return referenceDataFacade.getStates();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getStreetDirection(java.lang.String)
	 */
	@Override
	public StreetDirectionInfo getStreetDirection(String directionCode) throws TelusException {
		return referenceDataFacade.getStreetDirection(directionCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getStreetDirections()
	 */
	@Override
	public StreetDirectionInfo[] getStreetDirections() throws TelusException {
		return referenceDataFacade.getStreetDirections();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSubscriptionRoleType(java.lang.String)
	 */
	@Override
	public SubscriptionRoleTypeInfo getSubscriptionRoleType(String roleTypeCode) throws TelusException {
		return referenceDataFacade.getSubscriptionRoleType(roleTypeCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getSubscriptionRoleTypes()
	 */
	@Override
	public SubscriptionRoleTypeInfo[] getSubscriptionRoleTypes() throws TelusException {
		return referenceDataFacade.getSubscriptionRoleTypes();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTalkGroupPriorities()
	 */
	@Override
	public TalkGroupPriorityInfo[] getTalkGroupPriorities() throws TelusException {
		return referenceDataFacade.getTalkGroupPriorities();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTaxationPolicies()
	 */
	@Override
	public TaxationPolicyInfo[] getTaxationPolicies() throws TelusException {
		return referenceDataFacade.getTaxationPolicies();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTaxationPolicy(java.lang.String)
	 */
	@Override
	public TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusException {
		return referenceDataFacade.getTaxationPolicy(provinceCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTermUnit(java.lang.String)
	 */
	@Override
	public TermUnitInfo getTermUnit(String termUnitCode) throws TelusException {
		return referenceDataFacade.getTermUnit(termUnitCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTermUnits()
	 */
	@Override
	public TermUnitInfo[] getTermUnits() throws TelusException {
		return referenceDataFacade.getTermUnits();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTitle(java.lang.String)
	 */
	@Override
	public TitleInfo getTitle(String titleCode) throws TelusException {
		return referenceDataFacade.getTitle(titleCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getTitles()
	 */
	@Override
	public TitleInfo[] getTitles() throws TelusException {
		return referenceDataFacade.getTitles();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUnitType(java.lang.String)
	 */
	@Override
	public UnitTypeInfo getUnitType(String typeCode) throws TelusException {
		return referenceDataFacade.getUnitType(typeCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.EnterpriseReferenceFacade#getUnitTypes()
	 */
	@Override
	public UnitTypeInfo[] getUnitTypes() throws TelusException {
		return referenceDataFacade.getUnitTypes();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRateMethod(java.lang.String)
	 */
	@Override
	public UsageRateMethodInfo getUsageRateMethod(String rateMethodCode) throws TelusException {
		return referenceDataFacade.getUsageRateMethod(rateMethodCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRateMethods()
	 */
	@Override
	public UsageRateMethodInfo[] getUsageRateMethods() throws TelusException {
		return referenceDataFacade.getUsageRateMethods();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRecordType(java.lang.String)
	 */
	@Override
	public UsageRecordTypeInfo getUsageRecordType(String recordTypeCode) throws TelusException {
		return referenceDataFacade.getUsageRecordType(recordTypeCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageRecordTypes()
	 */
	@Override
	public UsageRecordTypeInfo[] getUsageRecordTypes() throws TelusException {
		return referenceDataFacade.getUsageRecordTypes();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageUnit(java.lang.String)
	 */
	@Override
	public UsageUnitInfo getUsageUnit(String unitCode) throws TelusException {
		return referenceDataFacade.getUsageUnit(unitCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getUsageUnits()
	 */
	@Override
	public UsageUnitInfo[] getUsageUnits() throws TelusException {
		return referenceDataFacade.getUsageUnits();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getVendorService(java.lang.String)
	 */
	@Override
	public VendorServiceInfo getVendorService(String vendorServiceCode) throws TelusException {
		return referenceDataFacade.getVendorService(vendorServiceCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getVendorServices()
	 */
	@Override
	public VendorServiceInfo[] getVendorServices() throws TelusException {
		return referenceDataFacade.getVendorServices();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkFunctions()
	 */
	@Override
	public WorkFunctionInfo[] getWorkFunctions() throws TelusException {
		return referenceDataFacade.getWorkFunctions();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkFunctions(java.lang.String)
	 */
	@Override
	public WorkFunctionInfo[] getWorkFunctions(String departmentCode) throws TelusException {
		return referenceDataFacade.getWorkFunctions(departmentCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkPosition(java.lang.String)
	 */
	@Override
	public WorkPositionInfo getWorkPosition(String workPositionCode) throws TelusException {
		return referenceDataFacade.getWorkPosition(workPositionCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWorkPositions(java.lang.String)
	 */
	@Override
	public WorkPositionInfo[] getWorkPositions(String functionCode) throws TelusException {
		return referenceDataFacade.getWorkPositions(functionCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWPSCategories()
	 */
	@Override
	public PrepaidCategoryInfo[] getWPSCategories() throws TelusException {
		return referenceDataFacade.getWPSCategories();
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getWPSCategory(java.lang.String)
	 */
	@Override
	public PrepaidCategoryInfo getWPSCategory(String categoryCode) throws TelusException {
		return referenceDataFacade.getWPSCategory(categoryCode);
	}

	/**
	 * Delegate method to
	 * 
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getWPSService(java.lang.String)
	 */
	@Override
	public ServiceInfo getWPSService(String wpsServiceCode) throws TelusException {
		return referenceDataFacade.getWPSService(wpsServiceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getWPSServices()
	 */
	@Override
	public ServiceInfo[] getWPSServices() throws TelusException {
		return referenceDataFacade.getWPSServices();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getZeroMinutePoolingContributorServices()
	 */
	@Override
	public ServiceInfo[] getZeroMinutePoolingContributorServices() throws TelusException {
		return referenceDataFacade.getZeroMinutePoolingContributorServices();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getPaperBillChargeType(int, java.lang.String, char, char, java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode, char accountType, char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException {
		return referenceDataFacade.getPaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment, invoiceSuppressionLevel);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServiceCodesByGroup(java.lang.String)
	 */
	@Override
	public String[] getServiceCodesByGroup(String serviceGroupCode) throws TelusException {
		return referenceDataFacade.getServiceCodesByGroup(serviceGroupCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ReferenceDataFacade#getServiceTerm(java.lang.String)
	 */
	@Override
	public ServiceTermDto getServiceTerm(String serviceCode) throws TelusException {
		return referenceDataFacade.getServiceTerm(serviceCode);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPrepaidServiceListByEquipmentAndNetworkType()
	 */
	@Override
	public ServiceInfo[] getPrepaidServiceListByEquipmentAndNetworkType(String equipmentType, String networkType) throws TelusException {
		return referenceDataFacade.getPrepaidServiceListByEquipmentAndNetworkType(equipmentType, networkType);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferencFacade#getTaxCalculationListByProvince()
	 */
	@Override
	public TaxSummaryInfo getTaxCalculationListByProvince(String provinceCode, double amount, TaxExemptionInfo taxExemptionInfo) throws TelusException {
		return referenceDataFacade.getTaxCalculationListByProvince(provinceCode, amount, taxExemptionInfo);
	}

	@Override
	public SalesRepInfo getDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException {
		return referenceDataFacade.getDealerSalesRepByCode(dealerCode, salesRepCode, expired);
	}

	@Override
	public BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles, String province) throws TelusException {
		return referenceDataFacade.removeBillCyclesByProvince(billCycles, province);
	}

	@Override
	public String getSubscriptionTypeByKBServiceType(String kbServiceType) throws TelusException {
		return referenceDataFacade.getSubscriptionTypeByKBServiceType(kbServiceType);
	}

	@Override
	public String getServiceInstanceStatusByKBSubscriberStatus(String kbSubscribreStatus) throws TelusException {
		return referenceDataFacade.getServiceInstanceStatusByKBSubscriberStatus(kbSubscribreStatus);
	}

	@Override
	public String getBillingAccountStatusByKBAccountStatus(String kbAccountStatus) throws TelusException {
		return referenceDataFacade.getBillingAccountStatusByKBAccountStatus(kbAccountStatus);
	}

	@Override
	public String getPaymentMethodTypeByKBPaymentMethodType(String kbPaymentMethodType) throws TelusException {
		return referenceDataFacade.getPaymentMethodTypeByKBPaymentMethodType(kbPaymentMethodType);
	}

	@Override
	public String getCreditCardTypeByKBCreditCardType(String kbCreditCardType) throws TelusException {
		return referenceDataFacade.getCreditCardTypeByKBCreditCardType(kbCreditCardType);
	}

	@Override
	public String getBillCycleCodeByKBBillCycleCode(String kbBillCycleCode) throws TelusException {
		return referenceDataFacade.getBillCycleCodeByKBBillCycleCode(kbBillCycleCode);
	}

	@Override
	public String getNameSuffixByKBNameSuffix(String kbNameSuffix) throws TelusException {
		return referenceDataFacade.getNameSuffixByKBNameSuffix(kbNameSuffix);
	}

	@Override
	public String getSaluationCodeByKBSaluationCode(String kbSaluationCode) throws TelusException {
		return referenceDataFacade.getSaluationCodeByKBSaluationCode(kbSaluationCode);
	}

	@Override
	public String getEquipmentGroupTypeBySEMSEquipmentGroupType(String semsEquipmentGroupType) throws TelusException {
		return referenceDataFacade.getEquipmentGroupTypeBySEMSEquipmentGroupType(semsEquipmentGroupType);
	}

	@Override
	public String getProvinceCodeByKBProvinceCode(String kbProvinceCode) throws TelusException {
		return referenceDataFacade.getProvinceCodeByKBProvinceCode(kbProvinceCode);
	}

	@Override
	public String getCountryCodeByKBCountryCode(String kbCountryCode) throws TelusException {
		return referenceDataFacade.getCountryCodeByKBCountryCode(kbCountryCode);
	}

	@Override
	public DataSharingGroupInfo[] getDataSharingGroups() throws TelusException {
		return referenceDataFacade.getDataSharingGroups();
	}

	@Override
	public DataSharingGroupInfo getDataSharingGroup(String groupCode) throws TelusException {
		return referenceDataFacade.getDataSharingGroup(groupCode);
	}

	@Override
	public ServiceFeatureClassificationInfo getServiceFeatureClassification(String serviceCode) throws TelusException {
		return referenceDataFacade.getServiceFeatureClassification(serviceCode);
	}

	@Override
	public ServicePeriodInfo[] getServicePeriodInfo(String serviceCode) throws TelusException {
		return referenceDataFacade.getServicePeriodInfo(serviceCode);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getServicePeriodInfo(String serviceCode, String serviceType) throws TelusException {
		return referenceDataFacade.getServicePeriodInfo(serviceCode, serviceType);
	}

	@Override
	public ServiceAirTimeAllocationInfo[] getVoiceAllocation(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		return referenceDataFacade.getVoiceAllocation(serviceCodes, effectiveDate, sessionId);
	}

	@Override
	public ServiceAirTimeAllocationInfo[] getCalculatedEffectedVoiceAllocation(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		return referenceDataFacade.getCalculatedEffectedVoiceAllocation(serviceCodes, effectiveDate, sessionId);
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		return referenceDataFacade.openSession(userId, password, applicationId);
	}

	@Override
	public TestPointResultInfo testRefPds() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("RefPDS System");
		try {
			getProvinceCodeByKBProvinceCode("ON");
			resultInfo.setResultDetail("Invoked RPDS System to test provinceCodeByKBProvinceCode method for province ON:");
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

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, ServiceInfo> getEquivalentServiceByServiceCodeList(String[] originalServiceCodeList, String[] destinationServiceCodeList, String networkType) throws TelusException {
		return referenceDataFacade.getEquivalentServiceByServiceCodeList(originalServiceCodeList, destinationServiceCodeList, networkType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<ServiceAndRelationInfo>> getServiceAndRelationList(ServiceRelationInfo[] serviceRelations) throws TelusException {
		return referenceDataFacade.getServiceAndRelationList(serviceRelations);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, List<ServiceInfo>> getServiceListByGroupList(String[] serviceGroupCodeList) throws TelusException {
		return referenceDataFacade.getServiceListByGroupList(serviceGroupCodeList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> getAlternateRecurringCharge(String[] serviceCodeList, String provinceCode, String npaNxx, String corporateId) throws TelusException {
		return referenceDataFacade.getAlternateRecurringCharge(serviceCodeList, provinceCode, npaNxx, corporateId);
	}

	@Override
	public boolean isNotificationEligible(String transactionType, String originatingeApp, int brandId, String accountType, String banSegment, String productType) throws TelusException {
		return referenceDataFacade.isNotificationEligible(transactionType, originatingeApp, brandId, accountType, banSegment, productType);
	}

	@Override
	public ServiceRelationInfo[] getServiceRelations(String serviceCode) throws TelusException {
		return referenceDataFacade.getServiceRelations(serviceCode);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getServiceCodeListByGroupList(String[] groupCodeList) throws TelusException {
		return referenceDataFacade.getServiceCodeListByGroupList(groupCodeList);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getSeatTypes()
	 */
	@Override
	public SeatTypeInfo[] getSeatTypes() throws TelusException {
		return referenceDataFacade.getSeatTypes();
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode)
	 */
	@Override
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException {
		return referenceDataFacade.isAssociatedIncludedPromotion(Info.padTo(pricePlanCode, ' ', 9), term, Info.padTo(serviceCode, ' ', 9));
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#isPPSEligible(char accountType, char accountSubType)
	 */
	@Override
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException {
		return referenceDataFacade.isPPSEligible(accountType, accountSubType);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServiceExtendedInfo(String[] serviceCodes)
	 */
	@Override
	public ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException {
		return referenceDataFacade.getServiceExtendedInfo(serviceCodes);
	}

	/**
	 * Delegate method to
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkPricePlanPrivilege(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkPricePlanPrivilege(String[] pricePlanCodes, String businessRole, String privilege) throws TelusException {
		return referenceDataFacade.checkPricePlanPrivilege(pricePlanCodes, businessRole, privilege);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getDataSharingPricingGroups() throws TelusException {
		return referenceDataFacade.getDataSharingPricingGroups();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getServiceEditions() throws TelusException {
		return referenceDataFacade.getServiceEditions();
	}

	@Override
	public ServiceInfo[] getWPSServices(String[] serviceCodeArray) throws TelusException {
		return referenceDataFacade.getWPSServices(serviceCodeArray);
	}

	@Override
	public ServiceFamilyTypeInfo[] getPPSServices() throws TelusException {
		return referenceDataFacade.getPPSServices();
	}

	@Override
	public PricePlanInfo retrievePricePlan(String pPricePlanCD, String pEquipmentType, String pProvinceCD, char pAccountType, char pAccountSubType, int pBrandId) throws TelusException {
		return referenceDataFacade.retrievePricePlan(pPricePlanCD, pEquipmentType, pProvinceCD, pAccountType, pAccountSubType, pBrandId);
	}

	@Override
	public RoamingServiceNotificationInfo[] retrieveRoamingServiceNotificationInfo(String[] serviceCodes) throws TelusException {
		return referenceDataFacade.retrieveRoamingServiceNotificationInfo(serviceCodes);
	}

	@Override
	public ReferenceInfo retrieveMarketingDescriptionBySoc(String soc) throws TelusException {
		return referenceDataFacade.retrieveMarketingDescriptionBySoc(soc);
	}

	@Override
	public String getNotificationTemplateSchemaVerison(String transactionType, int brandId, String accountType, String banSegment, String productType, String deliveryChannel, String language)
			throws TelusException {
		return referenceDataFacade.getNotificationTemplateSchemaVerison(transactionType, brandId, accountType, banSegment, productType, deliveryChannel, language);
	}

	@Override
	public ServiceInfo[] getIncludedPromotions(String pricePlanCode, String equipmentType, String networkType, String provinceCode, int term) throws TelusException {
		return referenceDataFacade.getIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode, term);
	}

	@Override
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo() throws TelusException {
		return referenceDataFacade.getWCCServiceExtendedInfo();		
	}
	
	@Override
	public WCCServiceExtendedInfo[] getWCCServiceExtendedInfo(String[] socCodes) throws TelusException {
		return referenceDataFacade.getWCCServiceExtendedInfo(socCodes);		
	}
	
	@Override
	public boolean isCDASupportedAccountTypeSubType(String accountTypeSubType) {
		return referenceDataFacade.isCDASupportedAccountTypeSubType(accountTypeSubType);
	}
	
	@SuppressWarnings("rawtypes")
	public List getLicenses() throws TelusException {
		return referenceDataFacade.getLicenses();
	}
	
}