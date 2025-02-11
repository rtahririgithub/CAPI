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
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

public class CommitmentInfoMapper extends AbstractSchemaMapper<Commitment, CommitmentInfo> {

	public CommitmentInfoMapper() {
		super(Commitment.class, CommitmentInfo.class);
	}

	@Override
	protected CommitmentInfo performDomainMapping(Commitment source, CommitmentInfo target) {
		if (source != null) {
			if (source.getTimePeriod() != null) {
				target.setEndDate(source.getTimePeriod().getExpiryDate());
				target.setStartDate(source.getTimePeriod().getEffectiveDate());
			}
			target.setMonths(Integer.parseInt(source.getContractTerm()));
			target.setReasonCode(source.getReasonCode());
			target.setModified(TransactionType.MODIFY.equals(source.getTransactionType().value()));
		}
		return super.performDomainMapping(source, target);
	}
}