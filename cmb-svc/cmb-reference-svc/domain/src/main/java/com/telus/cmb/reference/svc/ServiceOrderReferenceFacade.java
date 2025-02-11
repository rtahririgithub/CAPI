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
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.CommitmentReasonInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.PoolingGroupInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.SeatTypeInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
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
public interface ServiceOrderReferenceFacade {

	boolean checkServiceAssociation(String serviceCode, String pricePlanCode) throws TelusException;

	ServicePolicyInfo[] checkServicePrivilege(String[] serviceCode, String businessRole, String privilege) throws TelusException;
	
	ServicePolicyInfo[] checkPricePlanPrivilege(String[] pricePlanCodes, String businessRole, String privilege) throws TelusException;

	BrandInfo getBrandByBrandId(int brandId) throws TelusException;

	BrandInfo getBrand(String code) throws TelusException;

	BrandInfo[] getBrands() throws TelusException;

	CommitmentReasonInfo getCommitmentReason(String commitmentReasonCode) throws TelusException;

	CommitmentReasonInfo[] getCommitmentReasons() throws TelusException;

	FeatureInfo getFeature(String featureCode) throws TelusException;

	FeatureInfo[] getFeatures() throws TelusException;

	Date getLogicalDate() throws TelusException;

	PoolingGroupInfo getPoolingGroup(String poolingGroupCode) throws TelusException;

	PoolingGroupInfo[] getPoolingGroups() throws TelusException;

	PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException;

	PricePlanInfo getPricePlan(String productType, String pricePlanCode, String equipmentType, String provinceCode, String accountType,	String accountSubType, int brandId) throws TelusException;

	PricePlanInfo[] getPricePlans(String productType, String equipmentType, String provinceCode, String accountType, String accountSubType, int brandId, long[] productPromoTypes, 
			boolean initialActivation, boolean currentPricePlansOnly, boolean availableForActivationOnly, int term, String activityCode, String activityReasonCode, String networkType,
			String seatTypeCode) throws TelusException;	
	
	PricePlanInfo[] getPricePlans(PricePlanSelectionCriteriaInfo criteriaInfo) throws TelusException;

	PricePlanInfo[] getPricePlans(String[] pricePlanCode) throws TelusException;

	ServiceInfo getRegularService(String regularServiceCode) throws TelusException;

	ServiceInfo[] getRegularServices(String[] serviceCode) throws TelusException;

	ServiceExclusionGroupsInfo[] getServiceExclusionGroups() throws TelusException;

	ServiceInfo[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusException;
	
	ServiceInfo getWPSService(String serviceCode) throws TelusException;
	
	ServiceInfo[] getWPSServices() throws TelusException;
	
	ServiceInfo[] getPrepaidServiceListByEquipmentAndNetworkType(String equipmentType, String networkType) throws TelusException;
	
	/**
	 * Retrieves Service term information as defined in KB PROMOTION_TERMS table
	 *
	 * @param String serviceCode
	 * 
	 * @return ServiceTermDto
	 * @throws TelusException
	 */
	ServiceTermDto getServiceTerm (String serviceCode) throws TelusException;

	ServiceFeatureClassificationInfo getServiceFeatureClassification ( String classifcationCode)  throws TelusException;
	ServicePeriodInfo[] getServicePeriodInfo( String serviceCode ) throws TelusException;
	ServiceAirTimeAllocationInfo[] getVoiceAllocation (String[] serviceCodes, Date effectiveDate, String sessionId )  throws TelusException;
	ServiceAirTimeAllocationInfo[] getCalculatedEffectedVoiceAllocation (String[] serviceCodes, Date effectiveDate, String sessionId )  throws TelusException;
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	
	SpecialNumberInfo getSpecialNumber(String numberCode) throws TelusException;
	SpecialNumberRangeInfo getSpecialNumberRange(String phoneNumber) throws TelusException;
	SpecialNumberRangeInfo[] getSpecialNumberRanges() throws TelusException;
	SpecialNumberInfo[] getSpecialNumbers() throws TelusException;
	
	Map getEquivalentServiceByServiceCodeList(String[] originalServiceCodeList, String[] destinationServiceCodeList, String networkType) throws TelusException;
	Map getServiceAndRelationList(ServiceRelationInfo[] serviceRelations) throws TelusException;
	Map getServiceListByGroupList(String[] serviceGroupCodeList) throws TelusException;
	Map getAlternateRecurringCharge(String[] serviceCodeList, String provinceCode, String npaNxx, String corporateId) throws TelusException;

	ServiceRelationInfo[] getServiceRelations(String serviceCode)throws TelusException;

	Map getServiceCodeListByGroupList(String[] groupCodeList) throws TelusException;

	SeatTypeInfo[] getSeatTypes() throws TelusException;
	
	boolean isAssociatedIncludedPromotion(String pricePlanCode, int term, String serviceCode) throws TelusException;
	
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusException;
	
	ServiceExtendedInfo[] getServiceExtendedInfo(String[] serviceCodes) throws TelusException;
	
}