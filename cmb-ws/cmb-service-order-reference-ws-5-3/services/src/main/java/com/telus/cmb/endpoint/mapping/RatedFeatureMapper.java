/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.RatedFeature;

/**
 * @author Pavel Simonovsky
 *
 */
public class RatedFeatureMapper extends ReferenceMapper<RatedFeature, RatedFeatureInfo> {

	public RatedFeatureMapper() {
		super(RatedFeature.class, RatedFeatureInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected RatedFeature performSchemaMapping(RatedFeatureInfo source, RatedFeature target) {
		new FeatureMapper().performSchemaMapping(source, target);
		
		target.setCallingCircleSize(source.getCallingCircleSize());
		target.setRecurringCharge(source.getRecurringCharge());
		target.setRecurringChargeFrequency(source.getRecurringChargeFrequency());
		target.setUsageCharge(source.getUsageCharge());
		target.setPrepaidCallingCircleInd(source.isPrepaidCallingCircle());
		
		return target;
	}
}
