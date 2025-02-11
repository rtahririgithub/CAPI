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

import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.FeatureChangeInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;

public class FeatureChangeInfoMapper extends AbstractSchemaMapper<ContractFeature, FeatureChangeInfo> {

	public FeatureChangeInfoMapper() {
		super(ContractFeature.class, FeatureChangeInfo.class);
	}

	@Override
	protected FeatureChangeInfo performDomainMapping(ContractFeature source, FeatureChangeInfo target) {
		target.setCode(source.getCode());
		target.setFeatureParameter(source.getFeatureParameter());

		if (source.getTimePeriod() != null) {
			target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
			target.setExpiryDate(source.getTimePeriod().getExpiryDate());
		}

		if (source.getTransactionType() != null) {
			target.setTransactionType(source.getTransactionType().value());
		}

		if (source.getCallingCirclePhoneNumberList() != null) {
			List<String> phoneNumbers = source.getCallingCirclePhoneNumberList().getPhoneNumber();
			if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
				target.setCallingCirclePhoneNumberList(phoneNumbers.toArray(new String[phoneNumbers.size()]));
			}
		}

		ServiceFeatureInfo newServiceFeatureInfo = new ServiceFeatureInfo();
		newServiceFeatureInfo.setFeatureCode(target.getCode());
		if (source.getCallingCircleCommitmentAttributeData() != null) {
			newServiceFeatureInfo.setCallingCircleCommitmentAttributeData(new CallingCircleCommitmentAttributeDataInfoMapper().mapToDomain(source.getCallingCircleCommitmentAttributeData()));
		}

		target.setNewServiceFeatureInfo(newServiceFeatureInfo);

		return super.performDomainMapping(source, target);
	}
}