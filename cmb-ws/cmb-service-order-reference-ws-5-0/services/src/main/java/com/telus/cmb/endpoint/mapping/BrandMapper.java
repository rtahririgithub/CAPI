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
import com.telus.eas.utility.info.BrandInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.Brand;

/**
 * @author Pavel Simonovsky
 *
 */
public class BrandMapper extends ReferenceMapper<Brand, BrandInfo> {

	public BrandMapper() {
		super(Brand.class, BrandInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Brand performSchemaMapping(BrandInfo source, Brand target) {
		target.setBrandId(new Long(source.getBrandId()));
		target.setShortDescription(source.getShortDescription());
		target.setShortDescriptionFrench(source.getShortDescriptionFrench());

		return super.performSchemaMapping(source, target);
	}

}
