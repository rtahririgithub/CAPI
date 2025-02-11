/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.service_order_20;

import java.util.Arrays;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PrepaidCategoryInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v2.CoverageType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v2.WPSServiceType;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v2.Service;

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
		target.setIsActive(source.isActive());
		target.setIsAdditionalNumberRequired(source.isAdditionalNumberRequired());
		target.setIsAutoRenewalAllowed(source.isAutoRenewalAllowed());
		target.setIsAvailable(source.isAvailable());
		target.setIsAlternateRecurringCharge(source.hasAlternateRecurringCharge());
		target.setIsBillingZeroChrgSuppress(source.isBillingZeroChrgSuppress());
		target.setIsBoundedService(source.isBoundService());
		target.setIsCallHomeFree(source.hasCallHomeFree());
		target.setIsCallingCircleFeatures(source.hasCallingCircleFeatures());
		target.setIsClientActivation(source.isClientActivation());
//		target.setIsCrossFleetRestricted(source.isCrossFleetRestricted());
		target.setIsCurrent(source.isCurrent());
		target.setIsDealerActivation(source.isDealerActivation());
		target.setIsDiscountAvailable(source.isDiscountAvailable());
		target.setIsDispatchFeaturesIncluded(source.isDispatchFeaturesIncluded());
		target.setIsDowngradable(source.isDowngradable());
		target.setIsEmailAndWebspaceIncluded(source.isEmailAndWebspaceIncluded());
		target.setIsEquivalentService(source.hasEquivalentService());
		target.setIsEVDO(source.isEvDO());
		target.setIsForceAutoRenew(source.isForcedAutoRenew());
		target.setIsForSale(source.isForSale());
		target.setIsGranfathered(source.isGrandFathered());
		target.setIsIncludedPromotion(source.isIncludedPromotion());
		target.setIsInternationalCalling(source.isInternationalCalling());
		target.setIsKnowbility(source.isKnowbility());
		target.setIsLBSTrackee(source.isLBSTrackee());
		target.setIsLBSTracker(source.isLBSTracker());
		target.setIsLoyaltyAndRetentionService(source.isLoyaltyAndRetentionService());
		target.setIsMandatory(source.isMandatory());
		target.setIsMMS(source.isMMS());
		target.setIsMOSMS(source.isMOSMS());
		target.setIsMSBasedCapabilityRequired(source.isMSBasedCapabilityRequired());
		target.setIsNonCurrent(source.isNonCurrent());
		target.setIsParameterRequired(source.isParameterRequired());
		target.setIsPDA(source.isPDA());
		target.setIsPrepaidLBM(source.isPrepaidLBM());
		target.setIsPromotion(source.isPromotion());
		target.setIsPTT(source.isPTT());
		target.setIsRIM(source.isRIM());
		target.setIsRUIM(source.isInternationalRoaming());
		target.setIsSequentiallyBoundedService(source.isSequentiallyBoundService());
		target.setIsService911(source.is911());
		target.setIsSharable(source.isSharable());
		target.setIsSMSNotification(source.isSMSNotification());
		target.setIsTelephonyFeaturesIncluded(source.isTelephonyFeaturesIncluded());
		target.setIsVisto(source.isVisto());
		target.setIsVoiceToTextFeature(source.hasVoiceToTextFeature());
		target.setIsWiFi(source.isWiFi());
		target.setIsWirelessWebFeaturesIncluded(source.isWirelessWebFeaturesIncluded());
		target.setIsWPS(source.isWPS());
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
		target.setWPSMappedKBSocCode(source.getWPSMappedKBSocCode());
		target.setWPSServiceType(toEnum(source.getWPSServiceType(), WPSServiceType.class));
		
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
