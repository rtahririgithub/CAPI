/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping;

import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.ServiceTerm;

/**
 * @author Andrew Pereira
 *
 */
public class ServiceTermMapper extends AbstractSchemaMapper<ServiceTerm, ServiceTermDto> {

	public ServiceTermMapper() {
		super(ServiceTerm.class, ServiceTermDto.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected ServiceTerm performSchemaMapping(ServiceTermDto source, ServiceTerm target) {
		 
		target.setServiceCode(source.getServiceCode());
		target.setTermDuration(source.getTermDuration());
		target.setTermUnit(source.getTermUnit());

		return super.performSchemaMapping(source, target);
	}

}
