/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.resource;

import org.apache.commons.lang3.StringUtils;

import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

/**
 * @author x113300
 *
 */
public class ResponseMessageAsserter {

	public static void assertValid(ResponseMessage response) throws ResourceAccessException {

		String errorCode = response.getErrorCode();
		
		if (StringUtils.isNotEmpty(errorCode)) {
			
			String errorMessage = "";
			
			for (Message message : response.getMessageList()) {
				String locale = message.getLocale();
				if (StringUtils.isEmpty(locale) || StringUtils.equalsIgnoreCase("en", locale)) {
					errorMessage = String.format("Service error: [%s]: %s", errorCode, message.getMessage());
					break;
				}
			}
			
			throw new ResourceAccessException(errorCode, errorMessage);
		}
		
	}
}
