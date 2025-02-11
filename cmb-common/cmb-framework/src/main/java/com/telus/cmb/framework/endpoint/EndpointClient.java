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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.api.ApplicationException;
import com.telus.cmb.framework.resource.ResourceAccessTemplate;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

/**
 * @author Pavel Simonovsky
 *
 */
public class EndpointClient extends ResourceAccessTemplate {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected void assertResponse(String systemCode, Object response) throws ApplicationException {
		if (response instanceof ResponseMessage) {
			
			ResponseMessage responseMessage = (ResponseMessage) response;
			
			String errorCode = responseMessage.getErrorCode();
			
			if (StringUtils.isNotEmpty(errorCode)) {
				
				String errorMessage = "";
				
				for (Message message : responseMessage.getMessageList()) {
					String locale = message.getLocale();
					if (StringUtils.isEmpty(locale) || StringUtils.equalsIgnoreCase("en", locale)) {
//						errorMessage = String.format("Service error: [%s]: %s", errorCode, message.getMessage());
						errorMessage = message.getMessage();
						break;
					}
				}
				
				throw new ApplicationException(systemCode, errorCode, errorMessage, errorMessage);
			}
		}
	}
}
