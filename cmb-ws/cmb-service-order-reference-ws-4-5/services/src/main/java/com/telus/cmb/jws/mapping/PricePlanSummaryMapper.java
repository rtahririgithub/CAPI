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

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.PricePlanSummary;

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
		target.setSuspensionPricePlanInd(source.isSuspensionPricePlan());
		target.setAvailableForActivationInd(source.isAvailableForActivation());
		target.setAvailableForChangeInd(source.isAvailableForChange());
		target.setAvailableForChangeByDealerInd(source.isAvailableForChangeByDealer());
		target.setAvailableForChangeByClientInd(source.isAvailableForChangeByClient());
		target.setAvailableToModifyByDealerInd(source.isAvailableToModifyByDealer());
		target.setAvailableToModifyByClientInd(source.isAvailableToModifyByClient());
		target.setAvailableForNonCorporateRenewalInd(source.isAvailableForNonCorporateRenewal());
		target.setAvailableForCorporateRenewalInd(source.isAvailableForCorporateRenewal());
		target.setAvailableForCorporateStoreActivationInd(source.isAvailableForCorporateStoreActivation());
		target.setAvailableForRetailStoreActivationInd(source.isAvailableForRetailStoreActivation());
		target.setFidoPricePlanInd(source.isFidoPricePlan());
		target.setMinutePoolingCapableInd(source.isMinutePoolingCapable());
		target.setBrandId(new Long(source.getBrandId()));
//		target.setAmpdInd(source.isAmpd());
		target.setAOMPricePlanInd(source.isAOMPricePlan());
		target.getAvailableTermInMonths().addAll(toCollection(source.getAvailableTermsInMonths()));
		target.setSeatTypeCd(source.getSeatType());
		
		return super.performSchemaMapping(source, target);
	}

}
