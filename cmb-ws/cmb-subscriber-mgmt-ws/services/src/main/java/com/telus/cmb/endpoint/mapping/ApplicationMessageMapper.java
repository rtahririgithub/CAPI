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
import com.telus.cmb.schema.ApplicationMessage;
import com.telus.eas.message.info.ApplicationMessageInfo;

public class ApplicationMessageMapper extends AbstractSchemaMapper<ApplicationMessage, ApplicationMessageInfo> {

	public ApplicationMessageMapper() {
		super(ApplicationMessage.class, ApplicationMessageInfo.class);
	}

	@Override
	protected ApplicationMessage performSchemaMapping(ApplicationMessageInfo source, ApplicationMessage target) {
		target.setApplicationId(Integer.valueOf(source.getApplicationId()));
		target.setAudienceTypeId(Integer.valueOf(source.getAudienceTypeId()));
		target.setBrandId(Integer.valueOf(source.getBrandId()));
		target.setMessageCode(source.getCode());
		target.setMessageId(source.getId());
		target.setMessageTypeId(source.getMessageTypeId());
		return super.performSchemaMapping(source, target);
	}
}