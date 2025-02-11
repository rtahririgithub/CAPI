package com.telus.cmb.common.dao.soa.spring;

import com.telus.api.ApplicationException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;

public interface SoaResponseHandler {

	/*
	 * SoaResponseHandler is available for default response handling. This is required by any WS client 
	 * that extends SoaBaseSvcClientV2 and must support SOA's Exceptions v3.0 schema. This schema 
	 * no longer supports PolicyExceptions. These types of exceptions are now handled as a base response 
	 * message (com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage) 
	 * with error code and message elements populated.
	 * 
	 * If a customized error handler is necessary, it should extend the SoaDefaultResponseHandler, and 
	 * call super.handleErrorResponse method at the end of the handleErrorResponse method of the customized 
	 * response handler class.
	 *  
	 */
	
	public <T extends ResponseMessage> T handleErrorResponse(T response) throws ApplicationException;
	public <T extends ResponseMessage> T handleErrorResponse(T response, String systemCode) throws ApplicationException;
	
}