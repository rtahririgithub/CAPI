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

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v2.PricePlan;

/**
 * @author Pavel Simonovsky
 *
 */
public class PricePlanMapper extends ReferenceMapper<PricePlan, PricePlanInfo> {

	public PricePlanMapper() {
		super(PricePlan.class, PricePlanInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected PricePlan performSchemaMapping(PricePlanInfo source, PricePlan target) {
		new PricePlanSummaryMapper().performSchemaMapping(source, target);
		
		target.getOptionalService().addAll( new ServiceMapper().mapToSchema(
				source.getOptionalServices0()));
		
		target.setIsWaiveActivationFee(source.waiveActivationFee());
		
		return super.performSchemaMapping(source, target);
	}
}
