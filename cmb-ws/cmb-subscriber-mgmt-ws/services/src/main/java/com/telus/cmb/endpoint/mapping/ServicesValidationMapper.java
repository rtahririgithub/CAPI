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
import com.telus.cmb.schema.ServicesValidation;
import com.telus.eas.account.info.ServicesValidationInfo;

public class ServicesValidationMapper extends AbstractSchemaMapper<ServicesValidation, ServicesValidationInfo> {

	public ServicesValidationMapper() {
		super(ServicesValidation.class, ServicesValidationInfo.class);
	}

	@Override
	protected ServicesValidationInfo performDomainMapping(ServicesValidation source, ServicesValidationInfo target) {
		target.setPricePlanServiceGrouping(source.isValidatePricePlanServiceGroupingInd());
		target.setProvinceServiceMatch(source.isValidateProvinceServiceMatchInd());
		target.setEquipmentServiceMatch(source.isValidateEquipmentServiceMatchInd());
		return super.performDomainMapping(source, target);
	}
}