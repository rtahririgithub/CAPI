/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.customer_information_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.Reference;

/**
 * @author Pavel Simonovsky
 *
 */
public abstract class ReferenceMapper<S extends Reference, T extends com.telus.api.reference.Reference> extends AbstractSchemaMapper<S, T> {
	
	public ReferenceMapper(Class<S> schemaClass, Class<T> domainClass) {
		super(schemaClass, domainClass);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected S performSchemaMapping(T source, S target) {
		
		target.setCode(source.getCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		
		return target;
	}

}
