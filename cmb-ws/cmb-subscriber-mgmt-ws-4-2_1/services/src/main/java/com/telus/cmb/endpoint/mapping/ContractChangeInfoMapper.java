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

import java.util.ArrayList;
import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.Info;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.FeatureChangeInfo;
import com.telus.eas.subscriber.info.PricePlanChangeInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v3.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractFeature;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.PricePlan;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

public class ContractChangeInfoMapper extends AbstractSchemaMapper<ServiceAgreement, ContractChangeInfo> {

	public ContractChangeInfoMapper() {
		super(ServiceAgreement.class, ContractChangeInfo.class);
	}

	@Override
	protected ContractChangeInfo performDomainMapping(ServiceAgreement source, ContractChangeInfo target) {
		/** Set Commitment **/
		target.setNewCommitmentInfo(new CommitmentMapper().mapToDomain(source.getCommitment()));
		if (source.getCommitment() != null) {
			target.setContractRenewalInd(source.getCommitment().isRenewalInd());
			target.setContractTerm(Integer.valueOf(source.getCommitment().getContractTerm()));
			target.setActivationInd(source.getCommitment().isActivationInd());
			target.setMigrationInd(source.getCommitment().isMigrationInd());
		}
		/** Set multi ring phone number list */
		if (source.getMultiRingPhoneNumberList() != null) {
			List<String> multiRingPhoneNumberList = source.getMultiRingPhoneNumberList().getMultiRingPhoneNumber();
			target.setMultiRingPhoneNumberList(multiRingPhoneNumberList.toArray(new String[0]));
		}
		/** set price plan info */
		if (source.getPricePlan() != null) {
			PricePlan pp = source.getPricePlan();
			PricePlanChangeInfo ppcInfo = new PricePlanChangeInfo();
			ppcInfo.setCode(pp.getCode());
			ppcInfo.setServiceType(pp.getServiceType());

			if (pp.getFeature() != null) {
				List<ContractFeature> featureList = pp.getFeature();
				List<FeatureChangeInfo> featureChangeInfoList = new FeatureChangeInfoMapper().mapToDomain(featureList);
				ppcInfo.setFeatureChangeInfoList(featureChangeInfoList.toArray(new FeatureChangeInfo[0]));
			}

			if (pp.getTimePeriod() != null) {
				ppcInfo.setEffectiveDate(pp.getTimePeriod().getEffectiveDate());
				ppcInfo.setExpiryDate(pp.getTimePeriod().getExpiryDate());
			}

			if (pp.getTransactionType() != null) {
				ppcInfo.setTransactionType(pp.getTransactionType().value());
				target.setPricePlanChangeInd(TransactionType.MODIFY.equals(pp.getTransactionType()));
			}
			target.setPricePlanChangeInfo(ppcInfo);
		}
		/** Set service agreement validation **/
		target.setPricePlanValidatioInfo(new ServiceAgreementValidationMapper().mapToDomain(source.getValidationOverrideIndicatorList()));

		/** set optional services **/
		if (source.getService() != null) {
			List<ContractService> contractServiceList = source.getService();
			List<ServiceChangeInfo> serviceChangeInfoList = new ArrayList<ServiceChangeInfo>();

			for (ContractService cs : contractServiceList) {
				ServiceChangeInfo serviceChangeInfo = new ServiceChangeInfo();
				ServiceAgreementInfo sa = new ServiceAgreementInfo();

				sa.setServiceCode(cs.getCode());
				serviceChangeInfo.setCode(Info.padTo(cs.getCode(), ' ', 9));

				if (cs.getTimePeriod() != null) {
					serviceChangeInfo.setEffectiveDate(cs.getTimePeriod().getEffectiveDate());
					serviceChangeInfo.setExpiryDate(cs.getTimePeriod().getExpiryDate());
					sa.setEffectiveDate(cs.getTimePeriod().getEffectiveDate());
					sa.setExpiryDate(cs.getTimePeriod().getExpiryDate());
				}

				if (cs.getTransactionType() != null) {
					serviceChangeInfo.setTransactionType(cs.getTransactionType().value());
					sa.setTransaction(SubscriberManagementServiceMapper.translateTransactionType(cs.getTransactionType()));
				}
				serviceChangeInfo.setNewServiceAgreementInfo(sa);

				List<ContractFeature> contractFeatureList = cs.getFeature();
				List<FeatureChangeInfo> featureChangeInfoList = new FeatureChangeInfoMapper().mapToDomain(contractFeatureList);
				serviceChangeInfo.setFeatureChangeInfoList(featureChangeInfoList.toArray(new FeatureChangeInfo[0]));
				serviceChangeInfo.setPrepaidServicePropertyInfo(new PrepaidServicePropertyInfoMapper().mapToDomain(cs.getPrepaidPropertyList()));
				serviceChangeInfo.setServiceType(cs.getServiceType());
				serviceChangeInfoList.add(serviceChangeInfo);
			}
			target.setServiceChangeInfoList(serviceChangeInfoList.toArray(new ServiceChangeInfo[0]));
		}
		// target.setActivationInd(source.)
		return super.performDomainMapping(source, target);
	}
}