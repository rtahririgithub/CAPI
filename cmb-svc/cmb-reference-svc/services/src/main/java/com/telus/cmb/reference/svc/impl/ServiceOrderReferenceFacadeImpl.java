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
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.ApplicationException;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ServiceOrderReferenceFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.CommitmentReasonInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.PoolingGroupInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.SeatTypeInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.eas.utility.info.ServiceAndRelationInfo;
import com.telus.eas.utility.info.ServiceExclusionGroupsInfo;
import com.telus.eas.utility.info.ServiceExtendedInfo;
import com.telus.eas.utility.info.ServiceFeatureClassificationInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.SpecialNumberInfo;
import com.telus.eas.utility.info.SpecialNumberRangeInfo;

/**
 * @author Pavel Simonovsky
 *
 */
@Stateless(name="ServiceOrderReferenceFacade", mappedName="ServiceOrderReferenceFacade")
@Remote(ServiceOrderReferenceFacade.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors({SpringBeanAutowiringInterceptor.class, ReferenceFacadeSvcInvocationInterceptor.class})

public class ServiceOrderReferenceFacadeImpl implements ServiceOrderReferenceFacade {

	@Autowired
	private ServiceOrderReferenceFacade facade;	
	
	public void setServiceOrderReferenceFacade(ServiceOrderReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkServiceAssociation(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkServiceAssociation(String serviceCode, String pricePlanCode) throws TelusException {
		return facade.checkServiceAssociation(serviceCode, pricePlanCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkServicePrivilege(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkServicePrivilege(String[] serviceCode, String businessRole, String privilege) throws TelusException {
		return facade.checkServicePrivilege(serviceCode, businessRole, privilege);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#checkPricePlanPrivilege(java.lang.String[], java.lang.String, java.lang.String)
	 */
	@Override
	public ServicePolicyInfo[] checkPricePlanPrivilege(String[] pricePlanCodes, String businessRole, String privilege) throws TelusException {
		return facade.checkPricePlanPrivilege(pricePlanCodes, businessRole, privilege);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrandByBrandId(int)
	 */
	@Override
	public BrandInfo getBrandByBrandId(int brandId) throws TelusException {
		return facade.getBrandByBrandId(brandId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrands()
	 */
	@Override
	public BrandInfo[] getBrands() throws TelusException {
		return facade.getBrands();
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getBrand(java.lang.String)
	 */
	@Override
	public BrandInfo getBrand(String code) throws TelusException {
		return facade.getBrand(code);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getCommitmentReason(java.lang.String)
	 */
	@Override
	public CommitmentReasonInfo getCommitmentReason(String commitmentReasonCode) throws TelusException {
		return facade.getCommitmentReason(commitmentReasonCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getCommitmentReasons()
	 */
	@Override
	public CommitmentReasonInfo[] getCommitmentReasons() throws TelusException {
		return facade.getCommitmentReasons();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getFeature(java.lang.String)
	 */
	@Override
	public FeatureInfo getFeature(String featureCode) throws TelusException {
		return facade.getFeature(featureCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getFeatures()
	 */
	@Override
	public FeatureInfo[] getFeatures() throws TelusException {
		return facade.getFeatures();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getLogicalDate()
	 */
	@Override
	public Date getLogicalDate() throws TelusException {
		return facade.getLogicalDate();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPoolingGroup(java.lang.String)
	 */
	@Override
	public PoolingGroupInfo getPoolingGroup(String poolingGroupCode) throws TelusException {
		return facade.getPoolingGroup(poolingGroupCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPoolingGroups()
	 */
	@Override
	public PoolingGroupInfo[] getPoolingGroups() throws TelusException {
		return facade.getPoolingGroups();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlan(java.lang.String)
	 */
	@Override
	public PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException {
		return facade.getPricePlan(pricePlanCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public PricePlanInfo getPricePlan(String productType, String pricePlanCode,
			String equipmentType, String provinceCode, String accountType,
			String accountSubType, int brandId) throws TelusException {

		return facade.getPricePlan(productType, pricePlanCode, equipmentType, 
				provinceCode, accountType, accountSubType, brandId);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlans(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, long[], boolean, boolean, boolean, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String productType,
			String equipmentType, String provinceCode, String accountType,
			String accountSubType, int brandId, long[] productPromoTypes,
			boolean initialActivation, boolean currentPricePlansOnly,
			boolean availableForActivationOnly, int term, String activityCode,
			String activityReasonCode, String networkType, String seatTypeCode)
			throws TelusException {

		return facade.getPricePlans(productType, equipmentType, provinceCode, accountType, 
				accountSubType, brandId, productPromoTypes, initialActivation, currentPricePlansOnly, 
				availableForActivationOnly, term, activityCode, activityReasonCode, networkType, seatTypeCode);
	}
	
	/* (non-Javadoc)
	 *  @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlans(PricePlanSelectionCriteriaInfo)
	 */
	@Override
	public PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException {
		return facade.getPricePlans(criteriaInfo);
		
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getPricePlans(java.lang.String[])
	 */
	@Override
	public PricePlanInfo[] getPricePlans(String[] pricePlanCode) throws TelusException {
		return facade.getPricePlans(pricePlanCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getRegularService(java.lang.String)
	 */
	@Override
	public ServiceInfo getRegularService(String regularServiceCode) throws TelusException {
		return facade.getRegularService(regularServiceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getRegularServices(java.lang.String[])
	 */
	@Override
	public ServiceInfo[] getRegularServices(String[] serviceCode) throws TelusException {
		return facade.getRegularServices(serviceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServiceExclusionGroups()
	 */
	@Override
	public ServiceExclusionGroupsInfo[] getServiceExclusionGroups() throws TelusException {
		return facade.getServiceExclusionGroups();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServicesByFeatureCategory(java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public ServiceInfo[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusException {
		return facade.getServicesByFeatureCategory(featureCategory, productType, current);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getWPSService(java.lang.String)
	 */
	@Override
	public ServiceInfo getWPSService(String serviceCode) throws TelusException {
		return facade.getWPSService(serviceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getWPSServices()
	 */
	@Override
	public ServiceInfo[] getWPSServices() throws TelusException {
		return facade.getWPSServices();
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.ServiceOrderReferenceFacade#getServiceTerm(java.lang.String)
	 */
	@Override
	public ServiceTermDto getServiceTerm (String serviceCode) throws TelusException {
		return facade.getServiceTerm(serviceCode);
	}
	
	@Override
	public ServiceInfo[] getPrepaidServiceListByEquipmentAndNetworkType(String equipmentType, String networkType) throws TelusException {
		return facade.getPrepaidServiceListByEquipmentAndNetworkType(equipmentType, networkType);
	}

	@Override
	public ServiceFeatureClassificationInfo getServiceFeatureClassification( String serviceCode )throws TelusException {
		return facade.getServiceFeatureClassification(serviceCode);
	}

	@Override
	public ServicePeriodInfo[] getServicePeriodInfo(String serviceCode)
			throws TelusException {
		return facade.getServicePeriodInfo(serviceCode);
	}

	@Override
	public ServiceAirTimeAllocationInfo[] getVoiceAllocation(String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		return facade.getVoiceAllocation(serviceCodes, effectiveDate, sessionId);
	}

	@Override
	public ServiceAirTimeAllocationInfo[] getCalculatedEffectedVoiceAllocation(	String[] serviceCodes, Date effectiveDate, String sessionId) throws TelusException {
		return facade.getCalculatedEffectedVoiceAllocation(serviceCodes, effectiveDate, sessionId);
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		return facade.openSession(userId, password, applicationId);
	}

	@Override
	public SpecialNumberInfo getSpecialNumber(String numberCode)
			throws TelusException {
		return facade.getSpecialNumber(numberCode);
	}

	@Override
	public SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber)
			throws TelusException {
		return facade.getSpecialNumberRange(phoneNumber);
	}

	@Override
	public SpecialNumberRangeInfo[] getSpecialNumberRanges()
			throws TelusException {
		return facade.getSpecialNumberRanges();
	}

	@Override
	public SpecialNumberInfo[] getSpecialNumbers() throws TelusException {
		return facade.getSpecialNumbers();
	}

	@Override
	public Map<String, ServiceInfo> getEquivalentServiceByServiceCodeList(String[] originalServiceCodeList, String[] destinationServiceCodeList, String networkType) throws TelusException {
		return facade.getEquivalentServiceByServiceCodeList(originalServiceCodeList, destinationServiceCodeList, networkType);
	}
	
	@Override
	public Map<String, List<ServiceAndRelationInfo>> getServiceAndRelationList(ServiceRelationInfo[] serviceRelations) throws TelusException {
		return facade.getServiceAndRelationList(serviceRelations);
	}
	
	@Override
	public Map<String, List<ServiceInfo>> getServiceListByGroupList(String[] serviceGroupCodeList) throws TelusException {
		return facade.getServiceListByGroupList(serviceGroupCodeList);
	}
	
	@Override
	public Map<String, Double> getAlternateRecurringCharge(String[] serviceCodeList, String provinceCode, String npaNxx, String corporateId) throws TelusException {
		return facade.getAlternateRecurringCharge(serviceCodeList, provinceCode, npaNxx, corporateId);
	}

	@Override
	public ServiceRelationInfo[] getServiceRelations(String serviceCode) throws TelusException {
		return facade.getServiceRelations(serviceCode);
	}

	@Override
	public Map<String, List<String>> getServiceCodeListByGroupList(String[] groupCodeList) throws TelusException {
		return facade.getServiceCodeListByGroupList(groupCodeList);
	}
	
	@Override
	public SeatTypeInfo[] getSeatTypes() throws TelusException {
		return facade.getSeatTypes();
	}
	
	@Override
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException {
		return facade.isAssociatedIncludedPromotion(pricePlanCode, term, serviceCode);
	}

	@Override
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException {
		return facade.isPPSEligible(accountType, accountSubType);
	}

	@Override
	public ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException {
		return facade.getServiceExtendedInfo(serviceCodes);
	}
}