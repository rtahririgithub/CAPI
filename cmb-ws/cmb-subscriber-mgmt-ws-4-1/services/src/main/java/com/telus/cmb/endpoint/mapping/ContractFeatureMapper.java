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
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PhoneNumberList;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;

public class ContractFeatureMapper extends AbstractSchemaMapper<ContractFeature, ServiceFeatureInfo> {

	public ContractFeatureMapper() {
		super(ContractFeature.class, ServiceFeatureInfo.class);
	}

	@Override
	protected ServiceFeatureInfo performDomainMapping(ContractFeature source, ServiceFeatureInfo target) {
		target.setFeatureCode(source.getCode());
		target.setTransaction(SubscriberManagementServiceMapper.translateTransactionType(source.getTransactionType()));

		if (source.getTimePeriod() != null) {
			if (source.getTimePeriod().getEffectiveDate() != null) {
				target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
			}
			if (source.getTimePeriod().getExpiryDate() != null) {
				target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
		}
		if (source.getFeatureParameter() != null) {
			target.setParameter(source.getFeatureParameter());
		}

		if (source.getCallingCirclePhoneNumberList() != null) {
			List<String> phoneNumbers = source.getCallingCirclePhoneNumberList().getPhoneNumber();
			target.setCallingCirclePhoneNumberList(phoneNumbers.toArray(new String[0]));
		}

		if (source.getCallingCircleCommitmentAttributeData() != null) {
			target.setCallingCircleCommitmentAttributeData(new CallingCircleCommitmentAttributeDataInfoMapper().mapToDomain(source.getCallingCircleCommitmentAttributeData()));
		}
		return super.performDomainMapping(source, target);
	}

	@Override
	protected ContractFeature performSchemaMapping(ServiceFeatureInfo source, ContractFeature target) {
		target.setCode(source.getCode());
		target.setFeatureParameter(source.getParameter());
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setEffectiveDate(source.getEffectiveDate());
		timePeriod.setExpiryDate(source.getExpiryDate());
		target.setTimePeriod(timePeriod);
		target.setTransactionType(SubscriberManagementServiceMapper.translateTransactionType(source.getTransaction()));
		if (source.isCallingCircle()) {
			target.setCallingCirclePhoneNumberList(new PhoneNumberList());
			for (String phoneNumber : source.getCallingCirclePhoneNumbersFromParam()) {
				target.getCallingCirclePhoneNumberList().getPhoneNumber().add(phoneNumber);
			}
			target.setCallingCircleCommitmentAttributeData(new CallingCircleCommitmentAttributeDataInfoMapper().mapToSchema(source.getCallingCircleCommitmentAttributeData0()));
		}
		return super.performSchemaMapping(source, target);
	}
}