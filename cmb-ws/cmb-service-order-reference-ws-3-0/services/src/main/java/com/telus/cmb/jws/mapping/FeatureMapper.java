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
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.Feature;


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
		target.setAdditionalNumberRequiredInd(source.isAdditionalNumberRequired());
		target.setCallingCircleInd(source.isCallingCircle());
		target.setDispatchInd(source.isDispatch());
		target.setDollarPoolingContributorInd(source.isDollarPooling());
		target.setDuplicateFeatureAllowedInd(source.isDuplFeatureAllowed());
		target.setParameterRequiredInd(source.isParameterRequired());
		target.setSMSNotificationInd(source.isSMSNotification());
		target.setTelephonyInd(source.isTelephony());
		target.setWirelessWebInd(source.isWirelessWeb());
		target.setPoolGroupId(source.getPoolGroupId());
		target.setSwitchCode(source.getSwitchCode());
		target.setType(source.getType());
		
		return super.performSchemaMapping(source, target);
	}
}
