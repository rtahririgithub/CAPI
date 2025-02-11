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

import com.telus.api.portability.PortRequestSummary;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.SummaryPortRequestDataType;

public class PortRequestSummaryMapper extends AbstractSchemaMapper<SummaryPortRequestDataType, PortRequestSummary> {

	public PortRequestSummaryMapper() {
		super(SummaryPortRequestDataType.class, PortRequestSummary.class);
	}

	@Override
	protected SummaryPortRequestDataType performSchemaMapping(PortRequestSummary source, SummaryPortRequestDataType target) {

		target.setBillingAccountNumber(new Integer(source.getBanId()).toString());
		target.setCanBeActivateInd(source.canBeActivated());
		target.setCanBeCanceledInd(source.canBeCanceled());
		target.setCanBeModifiedInd(source.canBeModified());
		target.setCanBeSubmitedInd(source.canBeSubmitted());
		target.setCreationDate(source.getCreationDate());
		target.setPhoneNumber(source.getPhoneNumber());
		target.setPortProcessTypeCd(source.getType());
		target.setPortRequestId(source.getPortRequestId());
		target.setSourceBrandId(new Integer(source.getIncomingBrandId()).toString());
		target.setStatusCategoryCd(source.getStatusCategory());
		target.setStatusCd(source.getStatusCode());
		target.setStatusReasonCd(source.getStatusReasonCode());
		target.setTargetBrandId(new Integer(source.getOutgoingBrandId()).toString());

		return super.performSchemaMapping(source, target);
	}
}