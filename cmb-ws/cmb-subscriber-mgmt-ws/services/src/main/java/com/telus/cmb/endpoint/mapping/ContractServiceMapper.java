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
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.AutoRenewType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PrepaidPropertyListType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;

public class ContractServiceMapper extends AbstractSchemaMapper<ContractService, ServiceAgreementInfo> {

	public ContractServiceMapper() {
		super(ContractService.class, ServiceAgreementInfo.class);
	}

	@Override
	protected ServiceAgreementInfo performDomainMapping(ContractService source, ServiceAgreementInfo target) {

		target.setServiceCode(source.getCode());
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
			target.setServiceType(source.getServiceType());
		}

		if (!source.getFeature().isEmpty()) {
			HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
			for (ContractFeature feature : source.getFeature()) {
				features.put(Info.padTo(feature.getCode(), ' ', 6), new ContractFeatureMapper().mapToDomain(feature));
			}
			target.setFeatures(features);
		}
		if (source.getPrepaidPropertyList() != null) {
			PrepaidPropertyListType prepaidProperty = source.getPrepaidPropertyList();
			target.setWPS(prepaidProperty.isPrepaidInd());
			target.setPurchaseFundSource(prepaidProperty.getPurchaseFundSource());
			target.setAutoRenew(prepaidProperty.getAutoRenewPropertyList().isAutoRenewInd());
			if (prepaidProperty.getAutoRenewPropertyList().getRenewalFundSource() != null) {
				target.setAutoRenewFundSource(prepaidProperty.getAutoRenewPropertyList().getRenewalFundSource());
			}
		}

		return super.performDomainMapping(source, target);
	}

	@Override
	protected ContractService performSchemaMapping(ServiceAgreementInfo source, ContractService target) {
		target.setCode(source.getCode());
		if (source.isWPS()) {
			PrepaidPropertyListType prepaidProperty = new PrepaidPropertyListType();
			AutoRenewType autoRenew = new AutoRenewType();
			autoRenew.setAutoRenewInd(source.getAutoRenew());
			autoRenew.setRenewalFundSource(Integer.valueOf(source.getAutoRenewFundSource()));
			prepaidProperty.setAutoRenewPropertyList(autoRenew);
			prepaidProperty.setPrepaidInd(source.isWPS());
			prepaidProperty.setPurchaseFundSource(source.getPurchaseFundSource());
			target.setPrepaidPropertyList(prepaidProperty);
		}

		if (source.getServiceType() != null && !source.getServiceType().isEmpty()) {
			target.setServiceType(source.getServiceType());
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