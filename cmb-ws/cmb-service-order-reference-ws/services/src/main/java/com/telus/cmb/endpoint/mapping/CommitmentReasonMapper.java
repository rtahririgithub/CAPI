/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.CommitmentReasonInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.CommitmentReason;

/**
 * @author Pavel Simonovsky
 *
 */
public class CommitmentReasonMapper extends ReferenceMapper<CommitmentReason, CommitmentReasonInfo> {

	public CommitmentReasonMapper() {
		super(CommitmentReason.class, CommitmentReasonInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected CommitmentReason performSchemaMapping(CommitmentReasonInfo source, CommitmentReason target) {
		return super.performSchemaMapping(source, target);
	}

}
