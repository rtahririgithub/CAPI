/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.service_order_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PrepaidCategoryInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.service_order_reference_types_1_0.PrepaidCategory;

/**
 * @author Pavel Simonovsky
 *
 */
public class PrepaidCategoryMapper extends ReferenceMapper<PrepaidCategory, PrepaidCategoryInfo> {

	public PrepaidCategoryMapper() {
		super(PrepaidCategory.class, PrepaidCategoryInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected PrepaidCategory performSchemaMapping(PrepaidCategoryInfo source, PrepaidCategory target) {
		
		target.setPriority(source.getPriority());
		
		return super.performSchemaMapping(source, target);
	}
}
