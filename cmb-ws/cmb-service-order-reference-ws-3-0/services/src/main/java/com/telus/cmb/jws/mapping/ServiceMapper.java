/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping;

import java.util.Arrays;
import java.util.List;

import com.telus.api.reference.FundSource;
import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PrepaidCategoryInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CoverageType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.WPSServiceType;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.FundSourceList;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.Service;

/**
 * @author Pavel Simonovsky
 *
 */
public class ServiceMapper extends ReferenceMapper<Service, ServiceInfo> {

	public ServiceMapper() {
		super(Service.class, ServiceInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Service performSchemaMapping(ServiceInfo source, Service target) {
		
		target.setCategory(	new PrepaidCategoryMapper().mapToSchema(
				(PrepaidCategoryInfo) source.getCategory()));

		target.setCoverageType(toEnum(source.getCoverageType(), CoverageType.class));
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpiryDate(source.getExpiryDate());
		target.setActiveInd(source.isActive());
		target.setAdditionalNumberRequiredInd(source.isAdditionalNumberRequired());
		target.setAutoRenewalAllowedInd(source.isAutoRenewalAllowed());
		target.setAvailableInd(source.isAvailable());
		target.setAlternateRecurringChargeInd(source.hasAlternateRecurringCharge());
		target.setBillingZeroChrgSuppressInd(source.isBillingZeroChrgSuppress());
		target.setBoundedServiceInd(source.isBoundService());
		target.setCallHomeFreeInd(source.hasCallHomeFree());
		target.setCallingCircleFeaturesInd(source.hasCallingCircleFeatures());
		target.setClientActivationInd(source.isClientActivation());
//		target.setCrossFleetRestrictedInd(source.isCrossFleetRestricted());
		target.setCurrentInd(source.isCurrent());
		target.setDealerActivationInd(source.isDealerActivation());
		target.setDiscountAvailableInd(source.isDiscountAvailable());
		target.setDispatchFeaturesIncludedInd(source.isDispatchFeaturesIncluded());
		target.setDowngradableInd(source.isDowngradable());
		target.setEmailAndWebspaceIncludedInd(source.isEmailAndWebspaceIncluded());
		target.setEquivalentServiceInd(source.hasEquivalentService());
		target.setEVDOInd(source.isEvDO());
		target.setForceAutoRenewInd(source.isForcedAutoRenew());
		target.setForSaleInd(source.isForSale());
		target.setGranfatheredInd(source.isGrandFathered());
		target.setIncludedPromotionInd(source.isIncludedPromotion());
		target.setInternationalCallingInd(source.isInternationalCalling());
		target.setKnowbilityInd(source.isKnowbility());
		target.setLBSTrackeeInd(source.isLBSTrackee());
		target.setLBSTrackerInd(source.isLBSTracker());
		target.setLoyaltyAndRetentionServiceInd(source.isLoyaltyAndRetentionService());
		target.setMandatoryInd(source.isMandatory());
		target.setMMSInd(source.isMMS());
		target.setMOSMSInd(source.isMOSMS());
		target.setMSBasedCapabilityRequiredInd(source.isMSBasedCapabilityRequired());
		target.setNonCurrentInd(source.isNonCurrent());
		target.setParameterRequiredInd(source.isParameterRequired());
		target.setPDAInd(source.isPDA());
		target.setPrepaidLBMInd(source.isPrepaidLBM());
		target.setPromotionInd(source.isPromotion());
		target.setPTTInd(source.isPTT());
		target.setRIMInd(source.isRIM());
		target.setRUIMInd(source.isInternationalRoaming());
		target.setSequentiallyBoundedServiceInd(source.isSequentiallyBoundService());
		target.setService911Ind(source.is911());
		target.setSharableInd(source.isSharable());
		target.setSMSNotificationInd(source.isSMSNotification());
		target.setTelephonyFeaturesIncludedInd(source.isTelephonyFeaturesIncluded());
		target.setVistoInd(source.isVisto());
		target.setVoiceToTextFeatureInd(source.hasVoiceToTextFeature());
		target.setWiFiInd(source.isWiFi());
		target.setWirelessWebFeaturesIncludedInd(source.isWirelessWebFeaturesIncluded());
		target.setPrepaidInd(source.isWPS());
		target.setBillCycleTreatmentCode(source.getBillCycleTreatmentCode());
		target.setMax(source.getMaxTerm());
		target.setMaxConsActDays(source.getMaxConsActDays());
		target.setMinimumUsageCharge(source.getMinimumUsageCharge());
		target.setPeriodCode(source.getPeriodCode());
		target.setPriority(source.getPriority());
		target.setProductType(source.getProductType());
		target.setRecurringCharge(source.getRecurringCharge());
		target.setRecurringChargeFrequency(source.getRecurringChargeFrequency());
		target.setServiceType(source.getServiceType());
		target.setTerm(source.getTerm());
		target.setTermMonths(source.getTermMonths());
		target.setTermUnits(source.getTermUnits());
		target.setUserSegment(source.getUserSegment());
		target.setPrepaidMappedKBSocCode(source.getWPSMappedKBSocCode());
		target.setPrepaidServiceType(toEnum(source.getWPSServiceType(), WPSServiceType.class));
		
		if (source.getAllowedRenewalFundSourceArray()!=null) {
			target.setAllowedRenewalFundSourceList(new FundSourceList());
			for (int i=0; i<source.getAllowedRenewalFundSourceArray().length; i++){
				target.getAllowedRenewalFundSourceList().getFundSource().add(source.getAllowedRenewalFundSourceArray()[i].getFundSourceType());
			}		
		}
		
		if (source.getAllowedPurchaseFundSourceArray()!=null) {
			target.setAllowedPurchaseFundSourceList(new FundSourceList());
			for (int i=0; i<source.getAllowedPurchaseFundSourceArray().length; i++){
				target.getAllowedPurchaseFundSourceList().getFundSource().add(source.getAllowedPurchaseFundSourceArray()[i].getFundSourceType());
			}		
		}
		
		target.getFeature().addAll( new RatedFeatureMapper().mapToSchema(source.getFeatures0()));
		
		// TODO: populate service periods
		//target.getServicePeriod().addAll(...);
		
		// TODO: FIX schema to reflect network / equipment type association
		
		if (source.getEquipmentTypes() != null) {
			target.getEquipmentType().addAll(Arrays.asList(source.getEquipmentTypes()));
		}

		if (source.getCategoryCodes() != null) {
			target.getCategoryCode().addAll(Arrays.asList(source.getCategoryCodes()));
		}

		return super.performSchemaMapping(source, target);
	}
}
