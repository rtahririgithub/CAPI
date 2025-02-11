/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.service_order_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.service_order_reference_types_1_0.PricePlanSummary;

/**
 * @author Pavel Simonovsky
 *
 */
public class PricePlanSummaryMapper extends ReferenceMapper<PricePlanSummary, PricePlanInfo> {
	
	public PricePlanSummaryMapper() {
		super(PricePlanSummary.class, PricePlanInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected PricePlanSummary performSchemaMapping(PricePlanInfo source, PricePlanSummary target) {

		new ServiceMapper().performSchemaMapping(source, target);
		
		target.getIncludedService().addAll(new ServiceMapper().mapToSchema(
				source.getIncludedServices0()));
		
		target.setUsageRatingFrequency(source.getUsageRatingFrequency());
		target.setIncludedMinutesCount(source.getIncludedMinutesCount());
		target.setIsSuspensionPricePlan(source.isSuspensionPricePlan());
		target.setIsAvailableForActivation(source.isAvailableForActivation());
		target.setIsAvailableForChange(source.isAvailableForChange());
		target.setIsAvailableForChangeByDealer(source.isAvailableForChangeByDealer());
		target.setIsAvailableForChangeByClient(source.isAvailableForChangeByClient());
		target.setIsAvailableToModifyByDealer(source.isAvailableToModifyByDealer());
		target.setIsAvailableToModifyByClient(source.isAvailableToModifyByClient());
		target.setIsAvailableForNonCorporateRenewal(source.isAvailableForNonCorporateRenewal());
		target.setIsAvailableForCorporateRenewal(source.isAvailableForCorporateRenewal());
		target.setIsAvailableForCorporateStoreActivation(source.isAvailableForCorporateStoreActivation());
		target.setIsAvailableForRetailStoreActivation(source.isAvailableForRetailStoreActivation());
		target.setIsMinutePoolingCapable(source.isMinutePoolingCapable());
		target.setBrandId(source.getBrandId());
		target.setIsAOMPricePlan(source.isAOMPricePlan());
		target.getAvailableTermInMonths().addAll(toCollection(source.getAvailableTermsInMonths()));
		
		
		return super.performSchemaMapping(source, target);
	}

}
