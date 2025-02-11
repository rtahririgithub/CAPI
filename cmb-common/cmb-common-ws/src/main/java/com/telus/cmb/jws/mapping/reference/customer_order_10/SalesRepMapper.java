/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.customer_order_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.SalesRepInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.customer_order_reference_types_1_0.SalesRep;

/**
 * @author Pavel Simonovsky
 *
 */
public class SalesRepMapper extends ReferenceMapper<SalesRep, SalesRepInfo>{

	public SalesRepMapper() {
		super(SalesRep.class, SalesRepInfo.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.Reference)
	 */
	@Override
	protected SalesRep performSchemaMapping(SalesRepInfo source, SalesRep target) {

		target.setDealerCode(source.getDealerCode());
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpiryDate(source.getExpiryDate());
		target.setName(source.getName());

		return super.performSchemaMapping(source, target);
	}
}
