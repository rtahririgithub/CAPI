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
import com.telus.cmb.schema.WarningType;
import com.telus.eas.framework.exception.WarningFaultInfo;

public class ServiceWarningMapper extends AbstractSchemaMapper<WarningType, WarningFaultInfo> {

	public ServiceWarningMapper() {
		super(WarningType.class, WarningFaultInfo.class);
	}

	@Override
	protected WarningType performSchemaMapping(WarningFaultInfo source, WarningType target) {
		target.setSystemCode(source.getSystemCode());
		target.setWarningType(source.getWarningType());
		target.setWarning(new WarningBaseTypeMapper().mapToSchema(source));
		return super.performSchemaMapping(source, target);
	}

}