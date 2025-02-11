/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

public class SoaDefaultResponseHandler implements SoaResponseHandler {

	private static final Log LOGGER = LogFactory.getLog(SoaDefaultResponseHandler.class);

	protected static String DEFAULT_ERROR_MESSAGE = "Unknown error: no error message found in error response.";
	
	protected String componentName;
	
	public SoaDefaultResponseHandler(String componentName) {
		this.componentName = componentName;
	}

	@Override
	public <T extends ResponseMessage> T handleErrorResponse(T response) throws ApplicationException {
		return handleErrorResponse(response, SystemCodes.SOA_SPRING);
	}	

	@Override
	public <T extends ResponseMessage> T handleErrorResponse(T response, String systemCode) throws ApplicationException {

		if (StringUtils.isNotBlank(response.getErrorCode())) {
			// If an error code is found in the ResponseMessage, then log the error and throw an ApplicationException.
			LOGGER.error(getFormattedErrorMessage(response, systemCode));
			throw new ApplicationException(systemCode, getErrorCode(response), getErrorMessage(response), "");
		}

		// If no error code is found in the ResponseMessage, then the assumption is there is no error. Return the original response in this case.
		return response;
	}

	protected <T extends ResponseMessage> String getErrorCode(T response) {
		return response.getErrorCode();
	}

	protected <T extends ResponseMessage> String getErrorMessage(T response) {
		String message = CollectionUtils.isNotEmpty(response.getMessageList()) && response.getMessageList().get(0) != null
				? StringUtils.defaultIfEmpty(response.getMessageList().get(0).getMessage(), DEFAULT_ERROR_MESSAGE) : DEFAULT_ERROR_MESSAGE;
		return message;
	}

	protected <T extends ResponseMessage> Date getDateTimeStamp(T response) {
		return response.getDateTimeStamp() != null ? response.getDateTimeStamp() : new Date();
	}

	protected <T extends ResponseMessage> String getTransactionId(T response) {
		return StringUtils.defaultIfEmpty(response.getTransactionId(), "<null>");
	}

	protected <T extends ResponseMessage> String getFormattedErrorMessage(T response, String systemCode) {
		
		StringBuilder sb = new StringBuilder(100);
		sb.append("\n************** BEGIN " + systemCode + " error response **************");
		sb.append("\n**** componentName=[" + componentName + "] ****");
		sb.append("\n**** response=[" + response.getClass() + "] ****");
		sb.append("\n**** errorCode=[" + getErrorCode(response) + "] ****");
		sb.append("\n**** message=[" + getErrorMessage(response) + "] ****");
		sb.append("\n**** transactionId=[" + getTransactionId(response) + "] ****");
		sb.append("\n**** dateTimeStamp=[" + getDateTimeStamp(response) + "] ****");
		sb.append("\n************** END OF " + systemCode + " error response **************\n");
		
		return sb.toString();
	}

}