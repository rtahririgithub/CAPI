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

import java.util.Arrays;
import java.util.HashMap;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.MultiRingPhoneNumberType;

public class ServiceAgreementMapper extends AbstractSchemaMapper<ServiceAgreement, SubscriberContractInfo> {

	public ServiceAgreementMapper() {
		super(ServiceAgreement.class, SubscriberContractInfo.class);
	}

	@Override
	protected SubscriberContractInfo performDomainMapping(ServiceAgreement source, SubscriberContractInfo target) {
		if (source.getCommitment() != null) {
			CommitmentInfo commitmentInfo = new CommitmentMapper().mapToDomain(source.getCommitment());
			target.setCommitmentEndDate(commitmentInfo.getEndDate());
			target.setCommitmentStartDate(commitmentInfo.getStartDate());
			target.setCommitmentReasonCode(commitmentInfo.getReasonCode());
			target.setCommitmentMonths(commitmentInfo.getMonths());
		}
		if (source.getPricePlan() != null) {
			SubscriberContractInfo mappedSource = new PricePlanMapper().mapToDomain(source.getPricePlan());
			target.setPricePlan(mappedSource.getPricePlanCode());
			target.setEffectiveDate(mappedSource.getEffectiveDate());
			target.setExpiryDate(mappedSource.getExpiryDate());
			target.setPricePlanServiceType(mappedSource.getPricePlanServiceType());
			HashMap<String, ServiceFeatureInfo> features = new HashMap<String, ServiceFeatureInfo>();
			for (ServiceFeatureInfo feature : mappedSource.getFeatures0(false)) {
				features.put(feature.getCode(), feature);
			}
			target.setFeatures(features);
		}

		if (!source.getService().isEmpty()) {
			HashMap<String, ServiceAgreementInfo> services = new HashMap<String, ServiceAgreementInfo>();
			for (ContractService contractService : source.getService()) {
				services.put(contractService.getCode(), new ContractServiceMapper().mapToDomain(contractService));
			}
			target.setServices(services);
		}

		if (source.getMultiRingPhoneNumberList() != null) {
			target.setMultiRingPhoneNumbers(source.getMultiRingPhoneNumberList().getMultiRingPhoneNumber().toArray(new String[0]));
		}
		if (source.getValidationOverrideIndicatorList() != null) {
			PricePlanValidationInfo pricePlanValidationInfo = new ServiceAgreementValidationMapper().mapToDomain(source.getValidationOverrideIndicatorList());
			target.getPricePlanValidation0().setCurrentValidation(pricePlanValidationInfo.validateCurrent());
			target.getPricePlanValidation0().setPricePlanServiceGrouping(pricePlanValidationInfo.validatePricePlanServiceGrouping());
			target.getPricePlanValidation0().setEquipmentServiceMatch(pricePlanValidationInfo.validateEquipmentServiceMatch());
			target.getPricePlanValidation0().setProvinceServiceMatch(pricePlanValidationInfo.validateProvinceServiceMatch());
			target.getPricePlanValidation0().setForSaleValidation(pricePlanValidationInfo.validateForSale());
		}

		if (source.isCascadeShareableServiceChangeInd() != null) {
			target.setCascadeShareableServiceChanges(source.isCascadeShareableServiceChangeInd().booleanValue());
		}
		return super.performDomainMapping(source, target);
	}

	@Override
	protected ServiceAgreement performSchemaMapping(SubscriberContractInfo source, ServiceAgreement target) {
		target.setCommitment(new CommitmentMapper().mapToSchema(source.getCommitment()));
		if (source.getMultiRingPhoneNumbers() != null) {
			MultiRingPhoneNumberType multiRingPhoneNumber = new MultiRingPhoneNumberType();
			multiRingPhoneNumber.getMultiRingPhoneNumber().addAll(Arrays.asList(source.getMultiRingPhoneNumbers()));
			target.setMultiRingPhoneNumberList(multiRingPhoneNumber);
		}
		target.setPricePlan(new PricePlanMapper().mapToSchema(source));
		ServiceAgreementInfo[] contractServices = source.getServices0(true);
		if (contractServices != null) {
			for (ServiceAgreementInfo cs : contractServices) {
				target.getService().add(new ContractServiceMapper().mapToSchema(cs));
			}
		}
		return super.performSchemaMapping(source, target);
	}
}