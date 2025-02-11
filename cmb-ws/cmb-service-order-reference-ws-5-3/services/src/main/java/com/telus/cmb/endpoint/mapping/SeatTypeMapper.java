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
import com.telus.cmb.schema.SeatType;
import com.telus.eas.utility.info.SeatTypeInfo;

/**
 * @author Richard Fong
 *
 */
public class SeatTypeMapper extends ReferenceMapper<SeatType, SeatTypeInfo> {

	public SeatTypeMapper() {
		super(SeatType.class, SeatTypeInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected SeatType performSchemaMapping(SeatTypeInfo source, SeatType target) {
		return super.performSchemaMapping(source, target);
	}

}
