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

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.SpecialNumberInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.SpecialNumber;

/**
 * @author Anitha Duraisamy
 *
 */
public class SpecialNumberMapper extends AbstractSchemaMapper<SpecialNumber, SpecialNumberInfo> {

	public SpecialNumberMapper() {
		super(SpecialNumber.class, SpecialNumberInfo.class);
	}
	
	@Override
	protected SpecialNumber performSchemaMapping(SpecialNumberInfo source, SpecialNumber target) {
		 
		target.setCode(source.getSpecialNumber());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());

		return super.performSchemaMapping(source, target);
	}

}
