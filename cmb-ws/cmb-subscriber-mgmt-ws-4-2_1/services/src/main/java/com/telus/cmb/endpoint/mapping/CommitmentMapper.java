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
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TimePeriod;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

public class CommitmentMapper extends AbstractSchemaMapper<Commitment, CommitmentInfo> {

	public CommitmentMapper() {
		super(Commitment.class, CommitmentInfo.class);
	}

	@Override
	protected CommitmentInfo performDomainMapping(Commitment source, CommitmentInfo target) {
		if (source != null) {
			if (source.getTimePeriod() != null) {
				if (source.getTimePeriod().getExpiryDate() != null) {
					target.setEndDate(source.getTimePeriod().getExpiryDate());
				}
				if (source.getTimePeriod().getEffectiveDate() != null) {
					target.setStartDate(source.getTimePeriod().getEffectiveDate());
				}
			}
			target.setMonths(Integer.valueOf(source.getContractTerm()));
			target.setReasonCode(source.getReasonCode());
			target.setModified(TransactionType.MODIFY.equals(source.getTransactionType().value()));
		}
		return super.performDomainMapping(source, target);
	}

	@Override
	protected Commitment performSchemaMapping(CommitmentInfo source, Commitment target) {
		target.setContractTerm(String.valueOf(source.getMonths()));
		target.setReasonCode(source.getReasonCode());
		target.setRenewalInd(false);
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setEffectiveDate(source.getStartDate());
		timePeriod.setExpiryDate(source.getEndDate());
		target.setTimePeriod(timePeriod);
		target.setTransactionType(TransactionType.NO_CHANGE);
		return super.performSchemaMapping(source, target);
	}

}