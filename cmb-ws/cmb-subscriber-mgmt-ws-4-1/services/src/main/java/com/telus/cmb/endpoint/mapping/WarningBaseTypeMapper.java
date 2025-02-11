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
import com.telus.cmb.schema.WarningBaseType;
import com.telus.eas.framework.exception.WarningFaultInfo;

public class WarningBaseTypeMapper extends AbstractSchemaMapper<WarningBaseType, WarningFaultInfo> {

	public WarningBaseTypeMapper() {
		super(WarningBaseType.class, WarningFaultInfo.class);
	}

	@Override
	protected WarningBaseType performSchemaMapping(WarningFaultInfo source, WarningBaseType target) {
		target.setMessageId(source.getMessageId());
		target.setWarningCode(source.getErrorCode());
		target.setWarningMessage(source.getErrorMessage());
		return super.performSchemaMapping(source, target);
	}
}