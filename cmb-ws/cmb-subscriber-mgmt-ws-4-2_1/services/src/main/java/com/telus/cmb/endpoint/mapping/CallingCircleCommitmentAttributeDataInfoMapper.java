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
import com.telus.eas.subscriber.info.CallingCircleCommitmentAttributeDataInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.CallingCircleCommitmentAttributeData;

public class CallingCircleCommitmentAttributeDataInfoMapper extends AbstractSchemaMapper<CallingCircleCommitmentAttributeData, CallingCircleCommitmentAttributeDataInfo> {

	public CallingCircleCommitmentAttributeDataInfoMapper() {
		super(CallingCircleCommitmentAttributeData.class, CallingCircleCommitmentAttributeDataInfo.class);
	}

	@Override
	protected CallingCircleCommitmentAttributeDataInfo performDomainMapping(CallingCircleCommitmentAttributeData source, CallingCircleCommitmentAttributeDataInfo target) {
		target.setEffectiveDate(source.getEffectiveDate());
		target.setRemainingAllowedModifications(source.getRemainingModificationsAllowed());
		target.setTotalAllowedModifications(source.getTotalModificationsAllowed());
		target.setPrepaidModificationBlocked(source.isPrepaidModificationsBlockedInd());
		return super.performDomainMapping(source, target);
	}

	@Override
	protected CallingCircleCommitmentAttributeData performSchemaMapping(CallingCircleCommitmentAttributeDataInfo source, CallingCircleCommitmentAttributeData target) {
		target.setEffectiveDate(source.getEffectiveDate());
		target.setTotalModificationsAllowed(source.getTotalAllowedModifications());
		target.setRemainingModificationsAllowed(source.getRemainingAllowedModifications());
		target.setPrepaidModificationsBlockedInd(source.isPrepaidModificationBlocked());
		return super.performSchemaMapping(source, target);
	}
}