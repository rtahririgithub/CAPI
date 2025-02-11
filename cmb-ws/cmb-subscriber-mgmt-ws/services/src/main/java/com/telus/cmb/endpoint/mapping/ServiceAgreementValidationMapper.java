/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlanValidationOverrideIndicatorList;

public class ServiceAgreementValidationMapper extends AbstractSchemaMapper<PricePlanValidationOverrideIndicatorList, PricePlanValidationInfo> {

	public ServiceAgreementValidationMapper() {
		super(PricePlanValidationOverrideIndicatorList.class, PricePlanValidationInfo.class);
	}

	@Override
	protected PricePlanValidationInfo performDomainMapping(PricePlanValidationOverrideIndicatorList source, PricePlanValidationInfo target) {
		if (source.isCurrentValidationInd() != null) {
			target.setCurrentValidation(source.isCurrentValidationInd().booleanValue());
		}
		if (source.isEquipmentServiceMatchInd() != null) {
			target.setEquipmentServiceMatch(source.isEquipmentServiceMatchInd().booleanValue());
		}
		if (source.isForSaleValidationInd() != null) {
			target.setForSaleValidation(source.isForSaleValidationInd().booleanValue());
		}
		if (source.isPricePlanServiceGroupingInd() != null) {
			target.setPricePlanServiceGrouping(source.isPricePlanServiceGroupingInd().booleanValue());
		}
		if (source.isProvinceServiceMatchInd() != null) {
			target.setProvinceServiceMatch(source.isProvinceServiceMatchInd().booleanValue());
		}
		return super.performDomainMapping(source, target);
	}
}