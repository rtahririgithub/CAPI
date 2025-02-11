/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.endpoint;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.cmb.framework.resource.ResourceAccessException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

/**
 * @author x113300
 *
 */
public class ResponseMessageAsserter {
	
	private static final Logger logger = LoggerFactory.getLogger(ResponseMessageAsserter.class);

	public static void assertValid(ResponseMessage response) throws ResourceAccessException {
		
		if (response == null) {
			logger.warn("Unable to assert null response");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Asserting response validity: {}", new ReflectionToStringBuilder(response, ToStringStyle.DEFAULT_STYLE));
		}
		
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
			
			throw new ResourceAccessException(errorMessage);
		}
		
	}
}
