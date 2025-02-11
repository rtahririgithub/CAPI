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

import java.util.ArrayList;
import java.util.Collection;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.CoverageRegion;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.svc.SvcInvocationInterceptor;
import com.telus.cmb.common.util.ArrayUtil;
import com.telus.cmb.reference.dao.ReferenceDataAmdocsDao;
import com.telus.cmb.reference.dao.ReferenceDataCodsDao;
import com.telus.cmb.reference.dao.ReferenceDataConeDao;
import com.telus.cmb.reference.dao.ReferenceDataDistDao;
import com.telus.cmb.reference.dao.ReferenceDataEasDao;
import com.telus.cmb.reference.dao.ReferenceDataEcpcsDao;
import com.telus.cmb.reference.dao.ReferenceDataKnowbilityDao;
import com.telus.cmb.reference.dao.ReferenceDataOcssaDao;
import com.telus.cmb.reference.dao.ReferenceDataRefDao;
import com.telus.cmb.reference.dao.ReferenceDataWpsDao;
import com.telus.cmb.reference.dao.ReferenceDataWsoisDao;
import com.telus.cmb.reference.dto.FeeRuleDto;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.reference.svc.ReferenceDataHelperTestPoint;
import com.telus.eas.account.info.FleetClassInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.contactevent.info.RoamingServiceNotificationInfo;
import com.telus.eas.equipment.info.EquipmentPossessionInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.message.info.ApplicationMessageMappingInfo;
import com.telus.eas.utility.info.*;
import com.telus.framework.config.ConfigContext;

/**
 * @author Pavel Simonovsky
 *
 */

@Stateless(name="ReferenceDataHelper", mappedName="ReferenceDataHelper")
@Remote({ReferenceDataHelper.class, ReferenceDataHelperTestPoint.class})
@RemoteHome(ReferenceDataHelperHome.class)
@Interceptors({SpringBeanAutowiringInterceptor.class, SvcInvocationInterceptor.class})

@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

public class ReferenceDataHelperImpl implements ReferenceDataHelper, ReferenceDataHelperTestPoint {
	
	private static final Log logger = LogFactory.getLog(ReferenceDataHelperImpl.class);

	@Autowired
	private ReferenceDataCodsDao referenceDataCodsDao;

	@Autowired
	private ReferenceDataConeDao referenceDataConeDao;
	
	@Autowired
	private ReferenceDataEcpcsDao referenceDataEcpcsDao;
	
	@Autowired
	private ReferenceDataWpsDao referenceDataWpsDao;
	
	@Autowired
	private ReferenceDataWsoisDao referenceDataWsoisDao;
	
	@Autowired
	private ReferenceDataKnowbilityDao referenceDataKnowbilityDao;
	
	@Autowired
	private ReferenceDataDistDao referenceDataDistDao;
	
	@Autowired
	private ReferenceDataEasDao referenceDataEasDao;
	
	@Autowired
	private ReferenceDataRefDao referenceDataRefDao;

	@Autowired
	private ReferenceDataOcssaDao referenceDataOcssaDao;

	@Autowired
	private ReferenceDataAmdocsDao referenceDataAmdocsDao;
	
	@Autowired
	private DataSourceTestPointDao testPointDao;

	
	public void setReferenceDataAmdocsDao(ReferenceDataAmdocsDao referenceDataAmdocsDao) {
		this.referenceDataAmdocsDao = referenceDataAmdocsDao;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public void authenticate(String userID, String password) throws TelusException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#filterServiceListByPricePlan(java.lang.String[], java.lang.String)
	 */
	@Override
	public String[] filterServiceListByPricePlan(String[] serviceList, String pricePlan) throws TelusException {
		return referenceDataRefDao.filterServiceListByPricePlan(serviceList, pricePlan).toArray(new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#filterServiceListByProvince(java.lang.String[], java.lang.String)
	 */
	@Override
	public String[] filterServiceListByProvince(String[] serviceList, String province) throws TelusException {
		return referenceDataRefDao.filterServiceListByProvince(serviceList, province).toArray(new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#isAssociatedIncludedPromotion(java.lang.String, int, java.lang.String)
	 */
	@Override
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException {
		return referenceDataRefDao.isAssociatedIncludedPromotion(pricePlanCode, term, serviceCode);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#isDefaultDealer(java.lang.String)
	 */
	@Override
	public boolean isDefaultDealer(String dealerCode) throws TelusException {
		return referenceDataRefDao.isDefaultDealer(dealerCode);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#isPortOutAllowed(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isPortOutAllowed(String status, String activityCode, String activityReasonCode) throws TelusException {
		return referenceDataRefDao.isPortOutAllowed(status, activityCode, activityReasonCode);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#isPrivilegeAvailable(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isPrivilegeAvailable(String businessRoleCode, String privilegeCode, String serviceCode) throws TelusException {
		return referenceDataRefDao.isPrivilegeAvailable(businessRoleCode, privilegeCode, serviceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#isServiceAssociatedToPricePlan(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean isServiceAssociatedToPricePlan(String pricePlanCode, String serviceCode) throws TelusException {
		return referenceDataRefDao.isServiceAssociatedToPricePlan(pricePlanCode, serviceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAccountTypes()
	 */
	@Override
	public AccountTypeInfo[] retrieveAccountTypes() throws TelusException {
		return referenceDataRefDao.retrieveAccountTypes().toArray(new AccountTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveActivityTypes()
	 */
	@Override
	public ActivityTypeInfo[] retrieveActivityTypes() throws TelusException {
		return referenceDataRefDao.retrieveActivityTypes().toArray(new ActivityTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAdjustmentReasons()
	 */
	@Override
	public AdjustmentReasonInfo[] retrieveAdjustmentReasons() throws TelusException {
		return referenceDataRefDao.retrieveAdjustmentReasons().toArray(new AdjustmentReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAllProvinces()
	 */
	@Override
	public ProvinceInfo[] retrieveAllProvinces() throws TelusException {
		return referenceDataRefDao.retrieveAllProvinces().toArray(new ProvinceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAllTitles()
	 */
	@Override
	public TitleInfo[] retrieveAllTitles() throws TelusException {
		return referenceDataRefDao.retrieveAllTitles().toArray(new TitleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAlternateRCContractStartDate(java.lang.String)
	 */
	@Override
	public Date retrieveAlternateRCContractStartDate(String province) throws TelusException {
		if (province == null || province.trim().isEmpty()) {
            String errMsg = "Invalid parameter: province = [" + province + "]";
            throw new TelusException(ErrorCodes.REF_EJB_INVALID_PARAMETERS, errMsg);
		}
		return referenceDataRefDao.retrieveAlternateRCContractStartDate(province);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAlternateRecurringCharge(com.telus.eas.utility.info.ServiceInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public double retrieveAlternateRecurringCharge(ServiceInfo serviceInfo, String provinceCode, String npa, String nxx, String corporateId) throws TelusException {
		return referenceDataRefDao.retrieveAlternateRecurringCharge(serviceInfo, provinceCode, npa, nxx, corporateId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAmountBarCodes()
	 */
	@Override
	public AmountBarCodeInfo[] retrieveAmountBarCodes() throws TelusException {
		return referenceDataDistDao.retrieveAmountBarCodes().toArray(new AmountBarCodeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveApplicationMessageMappings()
	 */
	@Override
	public ApplicationMessageMappingInfo[] retrieveApplicationMessageMappings() throws TelusException {
		return referenceDataEasDao.retrieveApplicationMessageMappings().toArray(new ApplicationMessageMappingInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveApplicationMessages()
	 */
	@Override
	public ApplicationMessageInfo[] retrieveApplicationMessages() throws TelusException {
		return referenceDataEasDao.retrieveApplicationMessages().toArray(new ApplicationMessageInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveApplicationSummaries()
	 */
	@Override
	public ApplicationSummaryInfo[] retrieveApplicationSummaries() throws TelusException {
		return referenceDataEasDao.retrieveApplicationSummaries().toArray(new ApplicationSummaryInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveAudienceTypes()
	 */
	@Override
	public AudienceTypeInfo[] retrieveAudienceTypes() throws TelusException {
		return referenceDataEasDao.retrieveAudienceTypes().toArray(new AudienceTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBillCycle(java.lang.String)
	 */
	@Override
	public BillCycleInfo retrieveBillCycle(String billCycleCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveBillCycle(billCycleCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBillCycleLeastUsed()
	 */
	@Deprecated
	@Override
	public int retrieveBillCycleLeastUsed() throws TelusException {
		return referenceDataKnowbilityDao.retrieveBillCycleLeastUsed();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBillCycles()
	 */
	@Override
	public BillCycleInfo[] retrieveBillCycles() throws TelusException {
		return referenceDataKnowbilityDao.retrieveBillCycles().toArray(new BillCycleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBillCycles(java.lang.String)
	 */
	@Override
	public BillCycleInfo[] retrieveBillCycles(String populationCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveBillCycles(populationCode).toArray(new BillCycleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBillHoldRedirectDestinations()
	 */
	@Override
	public BillHoldRedirectDestinationInfo[] retrieveBillHoldRedirectDestinations() throws TelusException {
		return referenceDataRefDao.retrieveBillHoldRedirectDestinations().toArray(new BillHoldRedirectDestinationInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBrands()
	 */
	@Override
	public BrandInfo[] retrieveBrands() throws TelusException {
		return referenceDataRefDao.retrieveBrands().toArray(new BrandInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBrandSwapRules()
	 */
	@Override
	public BrandSwapRuleInfo[] retrieveBrandSwapRules() throws TelusException {
		return referenceDataDistDao.retrieveBrandSwapRules().toArray(new BrandSwapRuleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveBusinessRoles()
	 */
	@Override
	public BusinessRoleInfo[] retrieveBusinessRoles() throws TelusException {
		return referenceDataRefDao.retrieveBusinessRoles().toArray(new BusinessRoleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveClientConsentIndicators()
	 */
	@Override
	public ClientConsentIndicatorInfo[] retrieveClientConsentIndicators() throws TelusException {
		return referenceDataKnowbilityDao.retrieveReferencesByTable(
				ClientConsentIndicatorInfo.class, "CPUI_CODES", "CPUI_CD", "CPUI_DESC", "CPUI_DESC_F", null)
				.toArray(new ClientConsentIndicatorInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveClientStateReasons()
	 */
	@Override
	public ClientStateReasonInfo[] retrieveClientStateReasons() throws TelusException {
		return referenceDataCodsDao.retrieveClientStateReasons().toArray(new ClientStateReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCollectionActivities()
	 */
	@Override
	public CollectionActivityInfo[] retrieveCollectionActivities() throws TelusException {
		return referenceDataRefDao.retrieveCollectionActivities().toArray(new CollectionActivityInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCollectionAgencies()
	 */
	@Override
	public CollectionAgencyInfo[] retrieveCollectionAgencies() throws TelusException {
		return referenceDataRefDao.retrieveCollectionAgencies().toArray(new CollectionAgencyInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCollectionPathDetails()
	 */
	@Override
	public CollectionPathDetailsInfo[] retrieveCollectionPathDetails() throws TelusException {
		return referenceDataRefDao.retrieveCollectionPathDetails().toArray(new CollectionPathDetailsInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCollectionStates()
	 */
	@Override
	public CollectionStateInfo[] retrieveCollectionStates() throws TelusException {
		return referenceDataRefDao.retrieveCollectionStates().toArray(new CollectionStateInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCollectionStepApproval()
	 */
	@Override
	public CollectionStepApprovalInfo[] retrieveCollectionStepApproval() throws TelusException {
		return referenceDataRefDao.retrieveCollectionStepApprovals().toArray(new CollectionStepApprovalInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCommitmentReasons()
	 */
	@Override
	public CommitmentReasonInfo[] retrieveCommitmentReasons() throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("COMM_REASON", CommitmentReasonInfo.class)
		.toArray(new CommitmentReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCorporateAccountReps()
	 */
	@Override
	public CorporateAccountRepInfo[] retrieveCorporateAccountReps() throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("ACCOUNT_REP", CorporateAccountRepInfo.class)
		.toArray(new CorporateAccountRepInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCountries()
	 */
	@Override
	public CountryInfo[] retrieveCountries() throws TelusException {
		return referenceDataRefDao.retrieveCountries().toArray(new CountryInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCoverageRegions()
	 */
	@Override
	public CoverageRegionInfo[] retrieveCoverageRegions() throws TelusException {
		
		CoverageRegionInfo[] coverageRegions = referenceDataDistDao.retrieveCoverageRegions().toArray(new CoverageRegionInfo[0]);
		
		for (CoverageRegionInfo coverageRegion : coverageRegions) {
			if (coverageRegion.getType() == null || coverageRegion.getType().equals(CoverageRegion.COVERAGE_TYPE_EXTENDED)) {
				coverageRegion.setAssociatedServiceCodes(
						referenceDataRefDao.retrieveCoverageServiceCodes(coverageRegion.getCode()).toArray(new String[0]));
			}
		}

		return coverageRegions;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditCardPaymentTypes()
	 */
	@Override
	public CreditCardPaymentTypeInfo[] retrieveCreditCardPaymentTypes() throws TelusException {
		return referenceDataRefDao.retrieveCreditCardPaymentTypes().toArray(new CreditCardPaymentTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditCardTypes()
	 */
	@Override
	public CreditCardTypeInfo[] retrieveCreditCardTypes() throws TelusException {
		return new CreditCardTypeInfo[0];
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditCheckDepositChangeReasons()
	 */
	@Override
	public CreditCheckDepositChangeReasonInfo[] retrieveCreditCheckDepositChangeReasons() throws TelusException {
		return referenceDataRefDao.retrieveCreditCheckDepositChangeReasons().toArray(new CreditCheckDepositChangeReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditClasses()
	 */
	@Override
	public CreditClassInfo[] retrieveCreditClasses() throws TelusException {
		return referenceDataRefDao.retrieveCreditClasses().toArray(new CreditClassInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditMessageByCode(java.lang.String)
	 */
	@Override
	public CreditMessageInfo retrieveCreditMessageByCode(String code) throws TelusException {
		return referenceDataRefDao.retrieveCreditMessageByCode(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveCreditMessages()
	 */
	@Override
	public CreditMessageInfo[] retrieveCreditMessages() throws TelusException {
		return referenceDataRefDao.retrieveCreditMessages().toArray(new CreditMessageInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDealerbyDealerCD(java.lang.String)
	 */
	@Override
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveDealerbyDealerCode(dealerCode, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDealerbyDealerCD(java.lang.String, boolean)
	 */
	@Override
	public DealerInfo retrieveDealerbyDealerCode(String dealerCode, boolean expired) throws TelusException {
		return referenceDataKnowbilityDao.retrieveDealerbyDealerCode(dealerCode, expired);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDealerSalesRepByCode(java.lang.String, java.lang.String)
	 */
	@Override
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveDealerSalesRepByCode(dealerCode, salesRepCode, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDealerSalesRepByCode(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public SalesRepInfo retrieveDealerSalesRepByCode(String dealerCode, String salesRepCode, boolean expired) throws TelusException {
		return referenceDataKnowbilityDao.retrieveDealerSalesRepByCode(dealerCode, salesRepCode, expired);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDefaultLDC()
	 */
	@Override
	public String retrieveDefaultLDC() throws TelusException {
		return referenceDataRefDao.retrieveDefaultLDC();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDepartments()
	 */
	@Override
	public DepartmentInfo[] retrieveDepartments() throws TelusException {
		return referenceDataRefDao.retrieveDepartments().toArray(new DepartmentInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDiscountPlans()
	 */
	@Override
	public DiscountPlanInfo[] retrieveDiscountPlans() throws TelusException {
		return referenceDataRefDao.retrieveDiscountPlans(false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDiscountPlans(boolean)
	 */
	@Override
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current) throws TelusException {
		return referenceDataRefDao.retrieveDiscountPlans(current);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDiscountPlans(boolean, java.lang.String, int)
	 */
	@Override
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, int term) throws TelusException {
		return referenceDataRefDao.retrieveDiscountPlans(current, pricePlanCode, "ALL", new long[] {0}, true, term);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDiscountPlans(boolean, java.lang.String, java.lang.String, int)
	 */
	@Override
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, int term) throws TelusException {
		logger.debug("Begin retrieveDiscountPlans for pricePlanCode [ "+pricePlanCode+" ] ,provinceCode [ "+provinceCode+" ], term [ "+term+" ] ");
		long startTime = System.currentTimeMillis();
		DiscountPlanInfo[] discountPlanInfoArray = referenceDataRefDao.retrieveDiscountPlans(current, pricePlanCode, provinceCode, new long[] {0}, true, term);
		logger.debug("retrieveDiscountPlans total execution time for   pricePlanCode [ "+pricePlanCode+" ] , provinceCode [ "+provinceCode+" ], term [ "+term+" ] is [ "+(System.currentTimeMillis()-startTime)+" ms ]");
		return discountPlanInfoArray;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveDiscountPlans(boolean, java.lang.String, java.lang.String, long[], boolean, int)
	 */
	@Override
	public DiscountPlanInfo[] retrieveDiscountPlans(boolean current, String pricePlanCode, String provinceCode, long[] productPromoTypes, boolean initialActivation, int term) throws TelusException {
		if (pricePlanCode == null || pricePlanCode.trim().isEmpty()) {
			throw new TelusException ("00000", "Invalid pricePlanCode [" + pricePlanCode + "]");
		}
		return referenceDataRefDao.retrieveDiscountPlans(current, pricePlanCode, provinceCode, productPromoTypes, initialActivation, term);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveEncodingFormats()
	 */
	@Override
	public EncodingFormatInfo[] retrieveEncodingFormats() throws TelusException {
		return referenceDataDistDao.retrieveEncodingFormats().toArray(new EncodingFormatInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveEquipmentPossessions()
	 */
	@Override
	public EquipmentPossessionInfo[] retrieveEquipmentPossessions() throws TelusException {
		return referenceDataRefDao.retrieveEquipmentPossessions().toArray(new EquipmentPossessionInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveEquipmentProductTypes()
	 */
	@Override
	public EquipmentProductTypeInfo[] retrieveEquipmentProductTypes() throws TelusException {
		return referenceDataDistDao.retrieveEquipmentProductTypes().toArray(new EquipmentProductTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveEquipmentStatuses()
	 */
	@Override
	public EquipmentStatusInfo[] retrieveEquipmentStatuses() throws TelusException {
		return referenceDataDistDao.retrieveEquipmentStatuses().toArray(new EquipmentStatusInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] retrieveEquipmentTypes() throws TelusException {
		return referenceDataDistDao.retrieveEquipmentTypes().toArray(new EquipmentTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveExceptionReasons()
	 */
	@Override
	public ExceptionReasonInfo[] retrieveExceptionReasons() throws TelusException {
		return referenceDataRefDao.retrieveExceptionReasons().toArray(new ExceptionReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFeatureCategories()
	 */
	@Override
	public FeatureInfo[] retrieveFeatureCategories() throws TelusException {
		return referenceDataRefDao.retrieveFeatureCategories().toArray(new FeatureInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFeatures()
	 */
	@Override
	public FeatureInfo[] retrieveFeatures() throws TelusException {
		return referenceDataRefDao.retrieveFeatures().toArray(new FeatureInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFeeWaiverReasons()
	 */
	@Override
	public FeeWaiverReasonInfo[] retrieveFeeWaiverReasons() throws TelusException {
		return referenceDataRefDao.retrieveFeeWaiverReasons().toArray(new FeeWaiverReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFeeWaiverTypes()
	 */
	@Override
	public FeeWaiverTypeInfo[] retrieveFeeWaiverTypes() throws TelusException {
		return referenceDataRefDao.retrieveFeeWaiverTypes().toArray(new FeeWaiverTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFleetByFleetIdentity(com.telus.eas.account.info.FleetIdentityInfo)
	 */
	@Override
	public FleetInfo retrieveFleetByFleetIdentity(FleetIdentityInfo fleetIdentity) throws TelusException {
		return referenceDataKnowbilityDao.retrieveFleetByFleetIdentity(fleetIdentity);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFleetClasses()
	 */
	@Override
	public FleetClassInfo[] retrieveFleetClasses() throws TelusException {
		return referenceDataRefDao.retrieveFleetClasses().toArray(new FleetClassInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFleetsByFleetType(char)
	 */
	@Override
	public FleetInfo[] retrieveFleetsByFleetType(char fleetType) throws TelusException {
		return referenceDataKnowbilityDao.retrieveFleetsByFleetType(fleetType).toArray(new FleetInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFollowUpCloseReason(java.lang.String)
	 */
	@Override
	public FollowUpCloseReasonInfo retrieveFollowUpCloseReason(String reasonCode) throws TelusException {
		return referenceDataRefDao.retrieveFollowUpCloseReason(reasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFollowUpCloseReasons()
	 */
	@Override
	public FollowUpCloseReasonInfo[] retrieveFollowUpCloseReasons() throws TelusException {
		return referenceDataRefDao.retrieveFollowUpCloseReasons().toArray(new FollowUpCloseReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveFollowUpTypes()
	 */
	@Override
	public FollowUpTypeInfo[] retrieveFollowUpTypes() throws TelusException {
		return referenceDataRefDao.retrieveFollowUpTypes().toArray(new FollowUpTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveGenerations()
	 */
	@Override
	public GenerationInfo[] retrieveGenerations() throws TelusException {
		return referenceDataRefDao.retrieveGenerations().toArray(new GenerationInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveIncludedPromotions(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public ServiceInfo[] retrieveIncludedPromotions(String pricePlanCode, String equipmentType, String networkType, String provinceCode, int term) throws TelusException {
		if (equipmentType == null || equipmentType.trim().isEmpty()) {
			logger.error("retrieveIncludedPromotions: equipmentType=[" + equipmentType + "]. Using equipmentType=[9]");
			equipmentType = Equipment.EQUIPMENT_TYPE_ALL;
		}

		if (networkType == null || networkType.trim().isEmpty()) {
			logger.error("retrieveIncludedPromotions: networkType=[" + networkType + "]. Using networkType=[9]");
			networkType = NetworkType.NETWORK_TYPE_ALL;
		} else if ("HSPA".equalsIgnoreCase(networkType)) {
			logger.error("retrieveIncludedPromotions: networkType=[" + networkType + "]. Using networkType=[H]");
			networkType = NetworkType.NETWORK_TYPE_HSPA;
		}		

		return referenceDataRefDao.retrieveIncludedPromotions(pricePlanCode, equipmentType, networkType, provinceCode, term)
		.toArray(new ServiceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveInvoiceCallSortOrderTypes()
	 */
	@Override
	public InvoiceCallSortOrderTypeInfo[] retrieveInvoiceCallSortOrderTypes() throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("CALLS_SORT_ORDER", InvoiceCallSortOrderTypeInfo.class)
		.toArray(new InvoiceCallSortOrderTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveInvoiceSuppressionLevels()
	 */
	@Override
	public InvoiceSuppressionLevelInfo[] retrieveInvoiceSuppressionLevels() throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("BAN_LVL_INV_SUPPRESSION", InvoiceSuppressionLevelInfo.class)
		.toArray(new InvoiceSuppressionLevelInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveKnowbilityOperatorInfo(java.lang.String)
	 */
	@Override
	public KnowbilityOperatorInfo retrieveKnowbilityOperatorInfo(String operatorId) throws TelusException {
		return referenceDataKnowbilityDao.retrieveKnowbilityOperator(operatorId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveKnowbilityOperators()
	 */
	@Override
	public KnowbilityOperatorInfo[] retrieveKnowbilityOperators() throws TelusException {
		return referenceDataKnowbilityDao.retrieveKnowbilityOperators().toArray(new KnowbilityOperatorInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLanguages()
	 */
	@Override
	public LanguageInfo[] retrieveLanguages() throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("LANGUAGE", LanguageInfo.class).toArray(new LanguageInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLetter(java.lang.String, java.lang.String)
	 */
//	@Override
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode) throws TelusException {
//		return referenceDataRefDao.retrieveLetter(letterCategory, letterCode);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLetter(java.lang.String, java.lang.String, int)
	 */
//	@Override
//	public LetterInfo retrieveLetter(String letterCategory, String letterCode, int version) throws TelusException {
//		return referenceDataRefDao.retrieveLetter(letterCategory, letterCode, version);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLetterCategories()
	 */
//	@Override
//	public LetterCategoryInfo[] retrieveLetterCategories() throws TelusException {
//		return referenceDataKnowbilityDao.retrieveReferencesByTable(LetterCategoryInfo.class, "LMS_LETTER_CAT", "LETTER_CAT", "CAT_DESC", "CAT_DESC_F", null)
//		.toArray(new LetterCategoryInfo[0]);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLettersByCategory(java.lang.String)
	 */
//	@Override
//	public LetterInfo[] retrieveLettersByCategory(String letterCategory) throws TelusException {
//		return referenceDataRefDao.retrieveLettersByCategory(letterCategory).toArray(new LetterInfo[0]);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLettersByTitleKeyword(java.lang.String)
	 */
//	@Override
//	public LetterInfo[] retrieveLettersByTitleKeyword(String titleKeyword) throws TelusException {
//		return referenceDataRefDao.retrieveLettersByTitleKeyword(titleKeyword).toArray(new LetterInfo[0]);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLetterSubCategories()
	 */
//	@Override
//	public LetterSubCategoryInfo[] retrieveLetterSubCategories() throws TelusException {
//		return referenceDataRefDao.retrieveLetterSubCategories().toArray(new LetterSubCategoryInfo[0]);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLetterVariables(java.lang.String, java.lang.String, int)
//	 */
//	@Override
//	public LetterVariableInfo[] retrieveLetterVariables(String letterCategory, String letterCode, int letterVersion) throws TelusException {
//		return referenceDataRefDao.retrieveLetterVariables(letterCategory, letterCode, letterVersion).toArray(new LetterVariableInfo[0]);
//	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLockReasons()
	 */
	@Override
	public LockReasonInfo[] retrieveLockReasons() throws TelusException {
		return referenceDataDistDao.retrieveLockReasons().toArray(new LockReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveLogicalDate()
	 */
	@Override
	public Date retrieveLogicalDate() throws TelusException {
		return referenceDataKnowbilityDao.retrieveLogicalDate();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveManualChargeTypes()
	 */
	@Override
	public ChargeTypeInfo[] retrieveManualChargeTypes() throws TelusException {
		return referenceDataRefDao.retrieveManualChargeTypes().toArray(new ChargeTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveMarketProvinces(java.lang.String)
	 */
	@Override
	public String[] retrieveMarketProvinces(String serviceCode) throws TelusException {
		return referenceDataRefDao.retrieveMarketProvinces(serviceCode).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveMemoTypeCategories()
	 */
	@Override
	public MemoTypeCategoryInfo[] retrieveMemoTypeCategories() throws TelusException {
		return referenceDataRefDao.retrieveMemoTypeCategories().toArray(new MemoTypeCategoryInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveMemoTypes()
	 */
	@Override
	public MemoTypeInfo[] retrieveMemoTypes() throws TelusException {
		return referenceDataRefDao.retrieveMemoTypes().toArray(new MemoTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveMigrationTypes()
	 */
	@Override
	public MigrationTypeInfo[] retrieveMigrationTypes() throws TelusException {
		return referenceDataRefDao.retrieveMigrationTypes().toArray(new MigrationTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveMinutePoolingContributorServiceCodes()
	 */
	@Override
	public String[] retrieveMinutePoolingContributorServiceCodes() throws TelusException {
		return referenceDataRefDao.retrieveMinutePoolingContributorServiceCodes(
				ReferenceDataManager.MINUTE_POOLING_CONTRIBUTOR_SERVICES).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNetworks()
	 */
	@Override
	public NetworkInfo[] retrieveNetworks() throws TelusException {
		return referenceDataRefDao.retrieveNetworks().toArray(new NetworkInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNetworkTypes()
	 */
	@Override
	public NetworkTypeInfo[] retrieveNetworkTypes() throws TelusException {
		return referenceDataRefDao.retrieveNetworkTypes().toArray(new NetworkTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNotificationMessageTemplateInfo()
	 */
	@Override
	public NotificationMessageTemplateInfo[] retrieveNotificationMessageTemplateInfo() throws TelusException {
		return referenceDataConeDao.retrieveNotificationMessageTemplateInfo();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNotificationType()
	 */
	@Override
	public NotificationTypeInfo[] retrieveNotificationType() throws TelusException {
		return referenceDataConeDao.retrieveNotificationTypeInfo();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNpaNxxForMsisdnReservation(java.lang.String)
	 */
	@Override
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber) throws TelusException {
		return referenceDataRefDao.retrieveNpaNxxForMsisdnReservation(phoneNumber, true).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNpaNxxForMsisdnReservation(java.lang.String, boolean)
	 */
	@Override
	public String[] retrieveNpaNxxForMsisdnReservation(String phoneNumber, boolean isPortedInNumber) throws TelusException {
		//changed by Sachin on Sept 30, 2010 for defect: PROD00178714
		return referenceDataKnowbilityDao.retrieveNpaNxxForMsisdnReservation(phoneNumber, isPortedInNumber).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberGroupByPhoneNumberProductType(java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo retrieveNumberGroupByPhoneNumberProductType(String phoneNumber, String productType) throws TelusException {
		return referenceDataKnowbilityDao.retrieveNumberGroupByPhoneNumberProductType(phoneNumber, productType);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberGroupByPortedInPhoneNumberProductType(java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo retrieveNumberGroupByPortedInPhoneNumberProductType(String phoneNumber, String productType) throws TelusException {
		return referenceDataKnowbilityDao.retrieveNumberGroupByPortedInPhoneNumberProductType(phoneNumber, productType);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberGroupList(char, char, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] retrieveNumberGroupList(char accountType, char accountSubType, String productType, String equipmentType) throws TelusException {
		return referenceDataKnowbilityDao.retrieveNumberGroupList(accountType, accountSubType, productType, equipmentType).toArray(new NumberGroupInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberGroupList(char, char, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] retrieveNumberGroupList(char accountType, char accountSubType, String productType, String equipmentType, String marketAreaCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveNumberGroupList(accountType, accountSubType, productType, equipmentType, marketAreaCode).toArray(new NumberGroupInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberGroupListByProvince(char, char, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NumberGroupInfo[] retrieveNumberGroupListByProvince(char accountType, char accountSubType, String productType, String equipmentType, String province) throws TelusException {
		return referenceDataKnowbilityDao.retrieveNumberGroupListByProvince(accountType, accountSubType, productType, equipmentType, province).toArray(new NumberGroupInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveNumberRanges()
	 */
	@Override
	public NumberRangeInfo[] retrieveNumberRanges() throws TelusException {
		return referenceDataDistDao.retrieveNumberRanges().toArray(new NumberRangeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePagerEquipmentTypes()
	 */
	@Override
	public EquipmentTypeInfo[] retrievePagerEquipmentTypes() throws TelusException {
		return referenceDataDistDao.retrievePagerEquipmentTypes().toArray(new EquipmentTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePagerFrequencies()
	 */
	@Override
	public PagerFrequencyInfo[] retrievePagerFrequencies() throws TelusException {
		return referenceDataDistDao.retrievePagerFrequencies().toArray(new PagerFrequencyInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePaymentMethodTypes()
	 */
	@Override
	public PaymentMethodTypeInfo[] retrievePaymentMethodTypes() throws TelusException {
		return referenceDataRefDao.retrievePaymentMethodTypes().toArray(new PaymentMethodTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePaymentSourceTypes()
	 */
	@Override
	public PaymentSourceTypeInfo[] retrievePaymentSourceTypes() throws TelusException {
		return referenceDataRefDao.retrievePaymentSourceTypes().toArray(new PaymentSourceTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePaymentTransferReasons()
	 */
	@Override
	public PaymentTransferReasonInfo[] retrievePaymentTransferReasons() throws TelusException {
		return referenceDataRefDao.retrievePaymentTransferReasons().toArray(new PaymentTransferReasonInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePhoneNumberResource(java.lang.String)
	 */
	@Override
	public PhoneNumberResourceInfo retrievePhoneNumberResource(String phoneNumber) throws TelusException {
		return referenceDataKnowbilityDao.retrievePhoneNumberResource(phoneNumber);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePoolingGroups()
	 */
	@Override
	public PoolingGroupInfo[] retrievePoolingGroups() throws TelusException {
		return referenceDataRefDao.retrievePoolingGroups().toArray(new PoolingGroupInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidAdjustmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidAdjustmentReasons() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidAdjustmentReasons(ReferenceDataEcpcsDao.REASON_TYPE_ID_PREPAID_ADJUSTMENT, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidEventType(int)
	 */
	@Override
	public PrepaidEventTypeInfo retrievePrepaidEventType(int eventTypeId) throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidEventType(eventTypeId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidEventTypes()
	 */
	@Override
	public PrepaidEventTypeInfo[] retrievePrepaidEventTypes() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidEventTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidFeatureAddWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidFeatureAddWaiveReasons() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidAdjustmentReasons(ReferenceDataEcpcsDao.REASON_TYPE_ID_PREPAID_FEATURE_ADD_WAIVE, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidManualAdjustmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidManualAdjustmentReasons() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidAdjustmentReasons(ReferenceDataEcpcsDao.REASON_TYPE_ID_PREPAID_ADJUSTMENT, true);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidRates()
	 */
	@Override
	public PrepaidRateProfileInfo[] retrievePrepaidRates() throws TelusException {
		return referenceDataDistDao.retrievePrepaidRates().toArray(new PrepaidRateProfileInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidRechargeDenominations()
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] retrievePrepaidRechargeDenominations() throws TelusException {
		return referenceDataDistDao.retrievePrepaidRechargeDenominations().toArray(new PrepaidRechargeDenominationInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePrepaidTopUpWaiveReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidTopUpWaiveReasons() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidAdjustmentReasons(ReferenceDataEcpcsDao.REASON_TYPE_ID_PREPAID_TOP_UP_WAIVE, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataHelper#retrievePrepaidDeviceDirectFulfillmentReasons()
	 */
	@Override
	public PrepaidAdjustmentReasonInfo[] retrievePrepaidDeviceDirectFulfillmentReasons() throws TelusException {
		return referenceDataEcpcsDao.retrievePrepaidAdjustmentReasons(ReferenceDataEcpcsDao.REASON_TYPE_ID_PREPAID_DEVICE_DIRECT_FULFILLMENT, false);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlan(java.lang.String)
	 */
	@Override
	public PricePlanInfo retrievePricePlan(String pricePlanCode) throws TelusAPIException {
		PricePlanInfo result = null;		
		if (pricePlanCode != null && pricePlanCode.trim().length() > 0) {
			result = referenceDataRefDao.retrievePricePlan(pricePlanCode);
		}
		if (result == null) {
			throw new UnknownObjectException("Price Plan not found ", pricePlanCode);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlan(java.lang.String, java.lang.String, java.lang.String, char, char, int)
	 */
	@Override
	public PricePlanInfo retrievePricePlan(String pricePlanCode, String equipmentType, String provinceCode, char accountType, char accountSubType, int brandId, ServiceFamilyTypeInfo[] ppsServices) throws TelusException {
		PricePlanInfo result = null;	
		
		if (pricePlanCode != null && pricePlanCode.trim().length() > 0) {
			result = referenceDataRefDao.retrievePricePlan(pricePlanCode, equipmentType, provinceCode, accountType, accountSubType, brandId);
		}
		
		if (result == null) {
			throw new TelusException("20201", "Price Plan Code cannot be empty/null.");
		}
		
		ServiceInfo[]  optionalServices = result.getOptionalServices0(); //this was retrieved in the refDao call above.
		     
		// retrieve the unavailable pps services for specific account type..
		Collection<String> unavailablePPSServices = retrieveUnavailablePPSServices(accountType, accountSubType, ppsServices);

		// alter the optionalServices based on unavailable pps services..list
		ArrayList<ServiceInfo> optionalServicestemp = new ArrayList<ServiceInfo>();

		if (unavailablePPSServices.isEmpty() == false) {
			for (ServiceInfo serviceInfo : optionalServices) {
				if (unavailablePPSServices.contains(serviceInfo.getCode()) == false) {
					// add the soc if optional soc is not in unavailable pps services list..
					optionalServicestemp.add(serviceInfo);
				}
			}
			result.setOptionalServices(optionalServicestemp.toArray(new ServiceInfo[optionalServicestemp.size()]));
		} 
		
		return result;
	}

	
	private Collection<String> retrieveUnavailablePPSServices(char accountType, char accountSubType ,ServiceFamilyTypeInfo[] ppsServices) {
		
		// retrive the available socs for account type..
		
		Collection<String> available_socs_for_account = referenceDataRefDao.retrieveServicesByAccountType(accountType, accountSubType);
		Collection<String> unavailable_pps_socs = new ArrayList<String>();
		
		// determine the Unavailable PPSServices list based on pps services and  available_socs_for_account list..
		
		if (!available_socs_for_account.isEmpty()) {
			for (ServiceFamilyTypeInfo pps_soc : ppsServices) {
				if (!available_socs_for_account.contains(pps_soc.getSocCode())) {
					unavailable_pps_socs.add(pps_soc.getSocCode());
				}
			}
		} else {
			//if available_socs_for_account is empty , set all pps services as unavilable .
			for (ServiceFamilyTypeInfo pps_soc : ppsServices) {
				unavailable_pps_socs.add(pps_soc.getSocCode());
			}

		}
		
		return unavailable_pps_socs;
	}
	
	 
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, char accountType, char accountSubType, int pBrandId, 
			long[] pProductPromoTypeList, boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String activityCode, 
			String activityReasonCode, String networkType) throws TelusException {
		
		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, pBrandId, pProductPromoTypeList, 
				initialActivation, currentPricePlansOnly, availableForActivationOnly, term, activityCode, activityReasonCode, networkType, "", 
				null, null).toArray(new PricePlanInfo[0]);
	}
	
	@Override
	public PricePlanInfo[] retrievePricePlanList(PricePlanSelectionCriteriaInfo criteriaInfo , String[] offerGroupCodeList) throws TelusException {
		
		 int brandId = criteriaInfo.getBrandId() == null ? Brand.BRAND_ID_ALL : criteriaInfo.getBrandId();
		 String equipmentType = criteriaInfo.getEquipmentType() == null ? "9" : criteriaInfo.getEquipmentType();
		 long[] productPromoTypes = criteriaInfo.getProductPromoTypes() == null ? new long[0] : ArrayUtil.unboxLong(criteriaInfo.getProductPromoTypes());
		 boolean initialActivation = criteriaInfo.getInitialActivation() == null ? true : criteriaInfo.getInitialActivation();
		 boolean currentPricePlansOnly = criteriaInfo.getCurrentPlansOnly() == null ? true : criteriaInfo.getCurrentPlansOnly();
		 boolean availableForActivationOnly = criteriaInfo.getAvailableForActivationOnly() == null ? false : criteriaInfo.getAvailableForActivationOnly();
		 int term = criteriaInfo.getTerm() == null ? PricePlanSummary.CONTRACT_TERM_ALL : criteriaInfo.getTerm();
		 String activityCode = criteriaInfo.getActivityCode() == null ? "" : criteriaInfo.getActivityCode();
		 String activityReasonCode = criteriaInfo.getActivityReasonCode() == null ? "" : criteriaInfo.getActivityReasonCode();
		 String networkType = criteriaInfo.getNetworkType() == null ? NetworkType.NETWORK_TYPE_ALL : criteriaInfo.getNetworkType();
		 String seatTypeCode = criteriaInfo.getSeatTypeCode() == null ? "" : criteriaInfo.getSeatTypeCode();		 				
		 
		 		 
		 Collection<PricePlanInfo> kbpricePlanInfoList = referenceDataRefDao.retrievePricePlanList(
			 criteriaInfo.getProductType(),
			 equipmentType,
			 criteriaInfo.getProvinceCode(),
			 criteriaInfo.getAccountType(),
			 criteriaInfo.getAccountSubType(),
			 brandId,
			 productPromoTypes,
			 initialActivation,
			 currentPricePlansOnly,
			 availableForActivationOnly,
			 term,
			 activityCode,
			 activityReasonCode,
			 networkType,
			 seatTypeCode,
			 criteriaInfo.getSocGroups(),
			 offerGroupCodeList
			 );

		return kbpricePlanInfoList.toArray(new PricePlanInfo[0]);	
		
	}
	
	/*
	 * This method returns the OfferPricePlanSetInfo from the downstream WSOIS web service based on OfferCriteriaInfo
	 */
	public OfferPricePlanSetInfo retrieveOfferPricePlanInfo(PricePlanSelectionCriteriaInfo ppCriteriaInfo) throws TelusException {
		OfferPricePlanSetInfo offerPricePlanSet = null;
		if (ppCriteriaInfo.getOfferCriteria() != null && StringUtils.isNotBlank(ppCriteriaInfo.getOfferCriteria().getSystemId())) {
			try {
				offerPricePlanSet = referenceDataWsoisDao.retrieveOfferPricePlanSet(ppCriteriaInfo);
				logger.debug("WSOIS response : "+offerPricePlanSet);
			} catch (ApplicationException ae) {
				throw new TelusException(ae);
			}		 
		} 
		return offerPricePlanSet;
	}
	
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int)
	 */
	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, char accountType, char accountSubType, int brandId) throws TelusException {
		return retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, brandId, NetworkType.NETWORK_TYPE_ALL);
	}

	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, boolean currentPricePlansOnly, 
			boolean availableForActivationOnly, int term, String networkType, String seatTypeCode) throws TelusException {
		
		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, accountSubType, 
				brandId, currentPricePlansOnly, availableForActivationOnly, term, "", "", networkType, seatTypeCode).toArray(new PricePlanInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, boolean, boolean, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, boolean currentPricePlansOnly, 
			boolean availableForActivationOnly, String networkType) throws TelusException {
		
		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode,
				accountType, accountSubType, brandId, currentPricePlansOnly, availableForActivationOnly, 
				PricePlanSummary.CONTRACT_TERM_ALL, "", "", networkType, "").toArray(new PricePlanInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, boolean, boolean, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId,	boolean currentPricePlansOnly, 
			boolean availableForActivationOnly, String activityCode, String activityReasonCode, String networkType) throws TelusException {

		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, 
				accountSubType, brandId, currentPricePlansOnly, availableForActivationOnly, PricePlanSummary.CONTRACT_TERM_ALL,
				activityCode, activityReasonCode, networkType, "").toArray(new PricePlanInfo[0]);	
	}

	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, long[] productPromoTypeList, boolean pInitialActivation, 
			boolean currentPricePlansOnly, String networkType, String seatTypeCode) throws TelusException {

		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, 
				accountSubType, brandId, productPromoTypeList, pInitialActivation, currentPricePlansOnly, true,
				PricePlanSummary.CONTRACT_TERM_ALL, "", "", networkType, seatTypeCode, null, null).toArray(new PricePlanInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, long[], boolean, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, long[] productPromoTypeList, boolean initialActivation, 
			String networkType) throws TelusException {

		return referenceDataRefDao.retrievePricePlanList(
				productType, equipmentType, provinceCode, accountType, accountSubType, brandId, productPromoTypeList,
				initialActivation, true, true, PricePlanSummary.CONTRACT_TERM_ALL, "", "", networkType, "", 
				null, null).toArray(new PricePlanInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] retrievePricePlanList(String productType, String equipmentType, String provinceCode, 
			char accountType, char accountSubType, int brandId, String networkType) throws TelusException {
		
		return referenceDataRefDao.retrievePricePlanList(productType, equipmentType, provinceCode, accountType, 
				accountSubType, brandId, true, true, PricePlanSummary.CONTRACT_TERM_ALL, "", "", networkType, "")
				.toArray(new PricePlanInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePricePlanListByAccType(java.lang.String, java.lang.String, char, java.lang.String, int, boolean, boolean, java.lang.String)
	 * 
	 * Not in use?
	 */
	@Override
	@Deprecated
	public PricePlanInfo[] retrievePricePlanListByAccType(String productType, String provinceCode, 
			char accountType, String equipmentType, int brandId, boolean currentPlansOnly, 
			boolean availableForActivationOnly, String networkType) throws TelusException {
		
		return referenceDataRefDao.retrievePricePlanList(productType, provinceCode, accountType, 
				equipmentType, brandId, currentPlansOnly, availableForActivationOnly, networkType)
				.toArray(new PricePlanInfo[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ReferenceDataHelper#retrievePricePlanTerms()
	 */
	@Override
	public PricePlanTermInfo[] retrievePricePlanTerms() throws TelusException {
		return referenceDataRefDao.retrievePricePlanTerms().toArray(new PricePlanTermInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProductTypes()
	 */
	@Override
	public ProductTypeInfo[] retrieveProductTypes() throws TelusException {
		return referenceDataRefDao.retrieveProductTypes().toArray(new ProductTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePromoTerm(java.lang.String)
	 */
	@Override
	public PromoTermInfo retrievePromoTerm(String promoServiceCode) throws TelusException {
		return referenceDataRefDao.retrievePromoTerm(promoServiceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrievePromotionalDiscounts(java.lang.String, long[], boolean)
	 */
	@Override
	public String[] retrievePromotionalDiscounts(String pricePlanCode, long[] productPromoTypes, boolean initialActivation) throws TelusException {
		return referenceDataRefDao.retrievePromotionalDiscounts(pricePlanCode, productPromoTypes, initialActivation).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProvinces()
	 */
	@Override
	public ProvinceInfo[] retrieveProvinces() throws TelusException {
		return referenceDataRefDao.retrieveProvinces().toArray(new ProvinceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProvinces(java.lang.String)
	 */
	@Override
	public ProvinceInfo[] retrieveProvinces(String countryCode) throws TelusException {
		return referenceDataRefDao.retrieveProvinces(countryCode).toArray(new ProvinceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProvisioningPlatformTypes()
	 */
	@Override
	public ProvisioningPlatformTypeInfo[] retrieveProvisioningPlatformTypes() throws TelusException {
		return referenceDataDistDao.retrieveProvisioningPlatformTypes().toArray(new ProvisioningPlatformTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProvisioningTransactionStatuses()
	 */
	@Override
	public ProvisioningTransactionStatusInfo[] retrieveProvisioningTransactionStatuses() throws TelusException {
		return referenceDataRefDao.retrieveProvisioningTransactionStatuses().toArray(new ProvisioningTransactionStatusInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveProvisioningTransactionTypes()
	 */
	@Override
	public ProvisioningTransactionTypeInfo[] retrieveProvisioningTransactionTypes() throws TelusException {
		return referenceDataRefDao.retrieveProvisioningTransactionTypes().toArray(new ProvisioningTransactionTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRegularServices()
	 */
	@Override
	public ServiceInfo[] retrieveRegularServices() throws TelusException {
		return referenceDataRefDao.retrieveRegularServices(null, null, false, false).toArray(new ServiceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRegularServices(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ServiceInfo[] retrieveRegularServices(String featureCategory, String productType, boolean current) throws TelusException {
		return referenceDataRefDao.retrieveRegularServices(featureCategory, productType, true, current).toArray(new ServiceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRelations(java.lang.String)
	 */
	@Override
	public ServiceRelationInfo[] retrieveRelations(String serviceCode) throws TelusException {
		return referenceDataRefDao.retrieveRelations(serviceCode).toArray(new ServiceRelationInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveResourceStatus(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String retrieveResourceStatus(String productType, String resourceType, String resourceNumber) throws TelusException {
		return referenceDataKnowbilityDao.retrieveResourceStatus(productType,resourceType, resourceNumber);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRoamingCapability()
	 */
	@Override
	public HandsetRoamingCapabilityInfo[] retrieveRoamingCapability() throws TelusException {
		return referenceDataDistDao.retrieveHandsetRoamingCapabilityInfo().toArray(new HandsetRoamingCapabilityInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRoutes()
	 */
	@Override
	public RouteInfo[] retrieveRoutes() throws TelusException {
		return referenceDataRefDao.retrieveRoutes().toArray(new RouteInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRules()
	 */
	@Override
	public RuleInfo[] retrieveRules() throws TelusException {
		return referenceDataEasDao.retrieveRules().toArray(new RuleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveRules(int)
	 */
	@Override
	public RuleInfo[] retrieveRules(int category) throws TelusException {
		List<RuleInfo> result = new ArrayList<RuleInfo>();
		Collection<RuleInfo> rules = referenceDataEasDao.retrieveRules();
		for (RuleInfo rule : rules) {
			if (rule.getCategory() == category) {
				result.add(rule);
			}
		}
		return result.toArray(new RuleInfo[result.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSalesRepListByDealer(java.lang.String)
	 */
	@Override
	public SalesRepInfo[] retrieveSalesRepListByDealer(String dealerCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveSalesRepListByDealer(dealerCode).toArray(new SalesRepInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSegmentations()
	 */
	@Override
	public SegmentationInfo[] retrieveSegmentations() throws TelusException {
		return referenceDataRefDao.retrieveSegmentations().toArray(new SegmentationInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceEquipmentNetworkInfo(java.lang.String)
	 */
	@Override
	public ServiceEquipmentTypeInfo[] retrieveServiceEquipmentNetworkInfo(String serviceCode) throws TelusException {
		return referenceDataRefDao.retrieveServiceEquipmentNetworkInfo(serviceCode).toArray(new ServiceEquipmentTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceExclusionGroups()
	 */
	@Override
	public ServiceExclusionGroupsInfo[] retrieveServiceExclusionGroups() throws TelusException {
		return referenceDataRefDao.retrieveServiceExclusionGroups().toArray(new ServiceExclusionGroupsInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceFamily(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String networkType) throws TelusException {
		return referenceDataRefDao.retrieveServiceFamily(serviceCode, familyType, ServiceSummary.PROVINCE_CODE_ALL,
				Equipment.EQUIPMENT_TYPE_ALL, networkType, false, PricePlanSummary.CONTRACT_TERM_ALL).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceFamily(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, int)
	 */
	@Override
	public String[] retrieveServiceFamily(String serviceCode, String familyType, String provinceCode, String equipmentType, String networkType, boolean currentServicesOnly, int termInMonths) throws TelusException {
		return referenceDataRefDao.retrieveServiceFamily(serviceCode, familyType, ServiceSummary.PROVINCE_CODE_ALL,
				Equipment.EQUIPMENT_TYPE_ALL, networkType, false, PricePlanSummary.CONTRACT_TERM_ALL).toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceFamilyGroupCodes(java.lang.String, java.lang.String)
	 */
	@Override
	public String[] retrieveServiceFamilyGroupCodes(String serviceCode, String familyType) throws TelusException {
		return referenceDataRefDao.retrieveServiceFamilyGroupCodes(serviceCode, familyType).toArray(new String[0]);
	}
	
	
	@Override
	public String[] retrieveServiceGroupBySocCode(String serviceCode, String[] groupNames) throws TelusException {
		ArrayList<String> list = new ArrayList<String>();
		for(String groupName: groupNames){
			String name = referenceDataRefDao.retrieveServiceGroupBySocCode(serviceCode, groupName);
			if(name != null){
				list.add(name);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServicePolicyExceptions()
	 */
	@Override
	public ServicePolicyInfo[] retrieveServicePolicyExceptions() throws TelusException {
		return referenceDataRefDao.retrieveServicePolicyExceptions().toArray(new ServicePolicyInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveServiceUsageInfo(java.lang.String)
	 */
	@Override
	public ServiceUsageInfo retrieveServiceUsageInfo(String serviceCode) throws TelusException {
		return referenceDataRefDao.retrieveServiceUsageInfo(serviceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSIDs()
	 */
	@Override
	public SIDInfo[] retrieveSIDs() throws TelusException {
		return referenceDataRefDao.retrieveSIDs().toArray(new SIDInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSpecialNumberRanges()
	 */
	@Override
	public SpecialNumberRangeInfo[] retrieveSpecialNumberRanges() throws TelusException {
		return referenceDataRefDao.retrieveSpecialNumberRanges().toArray(new SpecialNumberRangeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSpecialNumbers()
	 */
	@Override
	public SpecialNumberInfo[] retrieveSpecialNumbers() throws TelusException {
		return referenceDataRefDao.retrieveSpecialNumbers().toArray(new SpecialNumberInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveStates()
	 */
	@Override
	public StateInfo[] retrieveStates() throws TelusException {
		return referenceDataRefDao.retrieveStates().toArray(new StateInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveStreetTypes()
	 */
	@Override
	public StreetTypeInfo[] retrieveStreetTypes() throws TelusException {
		return referenceDataRefDao.retrieveStreetTypes().toArray(new StreetTypeInfo[0]);	
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSubscriptionRoleTypes()
	 */
	@Override
	public SubscriptionRoleTypeInfo[] retrieveSubscriptionRoleTypes() throws TelusException {
		return referenceDataCodsDao.retrieveSubscriptionRoleTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSwapRules()
	 */
	@Override
	public SwapRuleInfo[] retrieveSwapRules() throws TelusException {
		return referenceDataDistDao.retrieveSwapRules().toArray(new SwapRuleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveSystemDate()
	 */
	@Override
	public Date retrieveSystemDate() throws TelusException {
		return referenceDataKnowbilityDao.retrieveSystemDate();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveTalkGroupPriorities()
	 */
	@Override
	public TalkGroupPriorityInfo[] retrieveTalkGroupPriorities() throws TelusException {
		return referenceDataRefDao.retrieveTalkGroupPriorities().toArray(new TalkGroupPriorityInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveTalkGroupsByFleetIdentity(com.telus.eas.account.info.FleetIdentityInfo)
	 */
	@Override
	public TalkGroupInfo[] retrieveTalkGroupsByFleetIdentity(FleetIdentityInfo fleetIdentity) throws TelusException {
		return referenceDataKnowbilityDao.retrieveTalkGroupsByFleetIdentity(fleetIdentity).toArray(new TalkGroupInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveTaxPolicies()
	 */
	@Override
	public TaxationPolicyInfo[] retrieveTaxPolicies() throws TelusException {
		return referenceDataRefDao.retrieveTaxPolicies().toArray(new TaxationPolicyInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveTitles()
	 */
	@Override
	public TitleInfo[] retrieveTitles() throws TelusException {
		return referenceDataRefDao.retrieveTitles().toArray(new TitleInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveUnitTypes()
	 */
	@Override
	public UnitTypeInfo[] retrieveUnitTypes() throws TelusException {
		return referenceDataRefDao.retrieveUnitTypes().toArray(new UnitTypeInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveUrbanIdByNumberGroup(com.telus.eas.utility.info.NumberGroupInfo)
	 */
	@Override
	public int retrieveUrbanIdByNumberGroup(NumberGroupInfo numberGroup) throws TelusException {
		return referenceDataKnowbilityDao.retrieveUrbanIdByNumberGroup(numberGroup);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveVendorServices()
	 */
	@Override
	public VendorServiceInfo[] retrieveVendorServices() throws TelusException {
		return referenceDataRefDao.retrieveVendorServices().toArray(new VendorServiceInfo[0]);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWorkFunctions()
	 */
	@Override
	public WorkFunctionInfo[] retrieveWorkFunctions() throws TelusException {
		return referenceDataRefDao.retrieveWorkFunctions().toArray(new WorkFunctionInfo[0]);	
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWorkFunctions(java.lang.String)
	 */
	@Override
	public WorkFunctionInfo[] retrieveWorkFunctions(String departmentCode) throws TelusException {
		return referenceDataRefDao.retrieveWorkFunctions(departmentCode).toArray(new WorkFunctionInfo[0]);	
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWorkPosition(java.lang.String)
	 */
	@Override
	public WorkPositionInfo retrieveWorkPosition(String workPositionId) throws TelusException {
		return referenceDataKnowbilityDao.retrieveWorkPosition(workPositionId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWorkPositions()
	 */
	@Override
	public WorkPositionInfo[] retrieveWorkPositions() throws TelusException {
		return referenceDataKnowbilityDao.retrieveWorkPositions();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWorkPositions(java.lang.String)
	 */
	@Override
	public WorkPositionInfo[] retrieveWorkPositions(String functionCode) throws TelusException {
		return referenceDataKnowbilityDao.retrieveWorkPositions(functionCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWPSFeature(int)
	 */
	@Override
	public ServiceInfo retrieveWPSFeature(int featureId) throws TelusException, ApplicationException {
			return referenceDataWpsDao.retrieveFeature(featureId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveWPSFeaturesList()
	 */
	@Override
	public ServiceInfo[] retrieveWPSFeaturesList() throws TelusException, ApplicationException {
			return referenceDataWpsDao.retrieveFeaturesList();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.service.ReferenceDataRetrievalService#retrieveZeroMinutePoolingContributorServiceCodes()
	 */
	@Override
	public String[] retrieveZeroMinutePoolingContributorServiceCodes() throws TelusException {
		return referenceDataRefDao.retrieveMinutePoolingContributorServiceCodes(
				ReferenceDataManager.ZERO_MINUTE_POOLING_CONTRIBUTOR_SERVICES).toArray(new String[0]);
	}

	/**
	 * @param referenceDataDistDao the referenceDataDistDao to set
	 */
	public void setReferenceDataDistDao(ReferenceDataDistDao referenceDataDistDao) {
		this.referenceDataDistDao = referenceDataDistDao;
	}
	
	/**
	 * @param referenceDataCodsDao the referenceDataCodsDao to set
	 */
	public void setReferenceDataCodsDao(ReferenceDataCodsDao referenceDataCodsDao) {
		this.referenceDataCodsDao = referenceDataCodsDao;
	}

	/**
	 * @param referenceDataConeDao the referenceDataConeDao to set
	 */
	public void setReferenceDataConeDao(ReferenceDataConeDao referenceDataConeDao) {
		this.referenceDataConeDao = referenceDataConeDao;
	}

	/**
	 * @param referenceDataEcpcsDao the referenceDataEcpcsDao to set
	 */
	public void setReferenceDataEcpcsDao(ReferenceDataEcpcsDao referenceDataEcpcsDao) {
		this.referenceDataEcpcsDao = referenceDataEcpcsDao;
	}

	/**
	 * @param referenceDataKnowbilityDao
	 */
	public void setReferenceDataKnowbilityDao(ReferenceDataKnowbilityDao referenceDataKnowbilityDao) {
		this.referenceDataKnowbilityDao = referenceDataKnowbilityDao;
	}

	/**
	 * @param referenceDataRefDao the referenceDataRefDao to set
	 */
	public void setReferenceDataRefDao(ReferenceDataRefDao referenceDataRefDao) {
		this.referenceDataRefDao = referenceDataRefDao;
	}

	/**
	 * @param referenceDataWpsDao the referenceDataWpsDao to set
	 */
	public void setReferenceDataWpsDao(ReferenceDataWpsDao referenceDataWpsDao) {
		this.referenceDataWpsDao = referenceDataWpsDao;
	}

	/**
	 * @param referenceDataWsoisDao the referenceDataWsoisDao to set
	 */
	public void setReferenceDataWsoisDao(ReferenceDataWsoisDao referenceDataWsoisDao) {
		this.referenceDataWsoisDao = referenceDataWsoisDao;
	}

	/**
	 * referenceDataEasDao the referenceDataEasDao to set
	 * @param referenceDataEasDao
	 */
	public void setReferenceDataEasDao(ReferenceDataEasDao referenceDataEasDao) {
		this.referenceDataEasDao = referenceDataEasDao;
	}
	
	//Added for Charge PaperBill - START - Anitha Duraisamy
	@Override
	public FeeRuleDto[] retrievePaperBillChargeType(int brandId,
			String provinceCode, char accountType, char accountSubType,
			String segment,String invoiceSuppressionLevel, Date logicalDate) throws TelusException {
	    
		Collection<FeeRuleDto> result = referenceDataRefDao.retrievePaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment,invoiceSuppressionLevel, logicalDate );
		return result.toArray(new FeeRuleDto[result.size()]);
	}

//Added for Charge PaperBill - END - Anitha Duraisamy
	
	/**
	 * Retrieve SOC group and service codes mapping information.
	 * 
	 * @param  String serviceGroupCode 
	 * @return String[] list of soc as values. 
	 * @throws TelusException
	 **/
	public String[] retrieveServiceGroupRelation (String serviceGroupCode)  throws TelusException {
		 return referenceDataRefDao.retrieveServiceGroupRelation(serviceGroupCode).toArray(new String[0]);
		
	}
	
	/**
	 * Retrieves Service term information as defined in KB PROMOTION_TERMS table
	 *
	 * @param String serviceCode
	 * 
	 * @return ServiceTermDto
	 * @throws TelusException
	 */
	public ServiceTermDto retrieveServiceTerm(String serviceCode) throws TelusException {
		return referenceDataRefDao.retrieveServiceTerm(serviceCode);
	}

	@Override
	public int[] retrieveBillCycleListLeastUsed() throws TelusException {
		return referenceDataKnowbilityDao.retrieveBillCycleListLeastUsed();
	}
	
	
	@Override
	public DataSharingGroupInfo[] retrieveDataSharingGroups() throws TelusException  {
		return referenceDataDistDao.retrieveDataSharingGroups().toArray(new DataSharingGroupInfo[0] ) ;
	}
	
	//BillC60 changes begin
	public Map<String, Map<String, ServicePeriodInfo>> retrieveServicePeriodInfos() throws TelusException {
		return referenceDataRefDao.retrieveServicePeriodInfos();
	}
	@Override
	public ServicePeriodInfo[] retrieveServicePeriodInfo( String serviceCode ) throws TelusException {
		return referenceDataRefDao.retrieveServicePeriodInfo( serviceCode ).toArray(new ServicePeriodInfo[0] );
	}

	@Override
	public ServiceFeatureClassificationInfo[] retrieveServiceFeatureClassifications( ) throws TelusException {
		return referenceDataRefDao.retrieveGenericCodesByType("SERVICE_AGREEMENT", ServiceFeatureClassificationInfo.class).toArray(new ServiceFeatureClassificationInfo[0] ) ;
	}

	@Override
	public ServiceAirTimeAllocationInfo retrieveVoiceAllocation(String serviceCode, Date effectiveDate, String sessionId) throws ApplicationException {
		return referenceDataAmdocsDao.retrieveVoiceAllocation(serviceCode, effectiveDate, sessionId);
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		return referenceDataAmdocsDao.openSession(userId, password, applicationId);
	}
	//BillC60 changes end

	@Override
	public Map<String,ServiceCodeTypeInfo> retrieveServiceCodes() throws ApplicationException {
		return referenceDataRefDao.retrieveServiceCodes();
	}

	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return testPointDao.testKnowbilityDataSource();
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return testPointDao.testDistDataSource();
	}

	@Override
	public TestPointResultInfo testEcpcsDataSource() {
		return testPointDao.testEcpcsDataSource();
	}

	@Override
	public TestPointResultInfo testConeDataSource() {
		return testPointDao.testConeDataSource();
	}

	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}

	@Override
	public TestPointResultInfo testEasDataSource() {
		return testPointDao.testEasDataSource();
	}

	@Override
	public TestPointResultInfo testRefDataSource() {
		return testPointDao.testRefDataSource();
	}

	@Override
	public TestPointResultInfo testEmcmDataSource() {
		return testPointDao.testEmcmDataSource();
	}

	@Override
	public TestPointResultInfo testProductOfferingService() {
		return null;// referenceDataWpsDao.testProductOfferingService();
	}

	@Override
	public TestPointResultInfo testWsoisService() {
		return referenceDataWsoisDao.test();
	}

	@Override
	public TestPointResultInfo getPricePlanUtilityPkgVersion() {
		return testPointDao.getPricePlanUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo getReferencePkgVersion() {
		return testPointDao.getReferencePkgVersion();
	}

	@Override
	public TestPointResultInfo getRuleUtilityPkgVersion() {
		return testPointDao.getRuleUtilityPkgVersion();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public SeatTypeInfo[] retrieveSeatTypes() throws TelusException {
		return referenceDataRefDao.retrieveSeatTypes().toArray(new SeatTypeInfo[0]);
	}
	
	@Override
	public boolean isPPSEligible(char accountType, char accountSubType,ServiceFamilyTypeInfo[] ppsServices) throws TelusException {
		return retrieveUnavailablePPSServices(accountType, accountSubType, ppsServices).isEmpty();
	}

	@Override
	public ServiceExtendedInfo[] retrieveServiceExtendedInfo(String[] serviceCodes) throws TelusException {
		return referenceDataRefDao.retrieveServiceExtendedInfo(serviceCodes).toArray(new ServiceExtendedInfo[0]);
	}
	
	@Override
	public ServiceFamilyTypeInfo[] retrievePPSServices() throws TelusException {
		return referenceDataRefDao.retrievePPSServices().toArray(new ServiceFamilyTypeInfo[0]);
	}
	
	@Override
	public RoamingServiceNotificationInfo[] retrieveRoamingServiceNotificationInfo(String [] serviceCodes) throws TelusException {
		return referenceDataRefDao.retrieveRoamingServiceRateInfo( serviceCodes).toArray(new RoamingServiceNotificationInfo[0]);
	}
	
	@Override 
	public ReferenceInfo[] retrieveMarketingDescriptions() throws TelusException {
		return referenceDataRefDao.retrieveMarketingDescriptions().toArray(new ReferenceInfo[0]);
	}
		
	@Override
	public SapccMappedServiceInfo[] retrieveSapccMappedServiceInfo() throws TelusException {
		return referenceDataKnowbilityDao.retrieveSapccMappedServiceInfo().toArray(new SapccMappedServiceInfo[0]);
	}
	
	@Override
	public SapccOfferInfo[] retrieveSapccOfferInfo() throws TelusException, ApplicationException {
		return referenceDataOcssaDao.retrieveWCCSapccOfferList().toArray(new SapccOfferInfo[0]);
	}
}