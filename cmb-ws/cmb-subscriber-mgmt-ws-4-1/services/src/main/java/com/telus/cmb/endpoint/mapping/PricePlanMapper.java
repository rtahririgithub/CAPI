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

import java.util.HashMap;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;

public class PricePlanMapper extends AbstractSchemaMapper<PricePlan, SubscriberContractInfo> {

	public PricePlanMapper() {
		super(PricePlan.class, SubscriberContractInfo.class);
	}

	@Override
	protected SubscriberContractInfo performDomainMapping(PricePlan source, SubscriberContractInfo target) {
		target.setPricePlan(source.getCode());
		target.setTransaction(SubscriberManagementServiceMapper.translateTransactionType(source.getTransactionType()));
		if (source.getTimePeriod() != null) {
			if (source.getTimePeriod().getEffectiveDate() != null) {
				target.setEffectiveDate(source.getTimePeriod().getEffectiveDate());
			}
			if (source.getTimePeriod().getExpiryDate() != null) {
				target.setExpiryDate(source.getTimePeriod().getExpiryDate());
			}
		}
		if (source.getServiceType() != null) {
			target.setPricePlanServiceType(source.getServiceType());
		}

		if (!source.getFeature().isEmpty()) {
			HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
			for (ContractFeature feature : source.getFeature()) {
				features.put(feature.getCode(), new ContractFeatureMapper().mapToDomain(feature));
			}
			target.setFeatures(features);
		}

		return super.performDomainMapping(source, target);
	}

	@Override
	protected PricePlan performSchemaMapping(SubscriberContractInfo source, PricePlan target) {
		target.setCode(source.getCode());
		// target.setServiceType(source.getPricePlan0().getServiceType());
		if (source.getPricePlanServiceType() != null && !source.getPricePlanServiceType().isEmpty()) {
			target.setServiceType(source.getPricePlanServiceType());
		}
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setEffectiveDate(source.getEffectiveDate());
		timePeriod.setExpiryDate(source.getExpiryDate());
		target.setTimePeriod(timePeriod);
		target.setTransactionType(SubscriberManagementServiceMapper.translateTransactionType(source.getTransaction()));
		ServiceFeatureInfo[] features = source.getFeatures0(true);
		if (features != null) {
			for (ServiceFeatureInfo serviceFeature : features) {
				target.getFeature().add(new ContractFeatureMapper().mapToSchema(serviceFeature));
			}
		}

		return super.performSchemaMapping(source, target);
	}
}