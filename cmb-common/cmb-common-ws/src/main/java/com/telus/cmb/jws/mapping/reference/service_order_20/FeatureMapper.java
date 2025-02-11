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
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v2.Feature;


/**
 * @author Pavel Simonovsky
 *
 */
public class FeatureMapper extends ReferenceMapper<Feature, FeatureInfo> {

	public FeatureMapper() {
		super(Feature.class, FeatureInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected Feature performSchemaMapping(FeatureInfo source, Feature target) {
		
		target.setCategoryCode(source.getCategoryCode());
		target.setIsAdditionalNumberRequired(source.isAdditionalNumberRequired());
		target.setIsCallingCircle(source.isCallingCircle());
		target.setIsDispatch(source.isDispatch());
		target.setIsDollarPoolingContributor(source.isDollarPooling());
		target.setIsDuplicateFeatureAllowed(source.isDuplFeatureAllowed());
		target.setIsParameterRequired(source.isParameterRequired());
		target.setIsSMSNotification(source.isSMSNotification());
		target.setIsTelephony(source.isTelephony());
		target.setIsWirelessWeb(source.isWirelessWeb());
		target.setPoolGroupId(source.getPoolGroupId());
		target.setSwitchCode(source.getSwitchCode());
		target.setType(source.getType());
		
		return super.performSchemaMapping(source, target);
	}
}
