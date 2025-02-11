/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring.rest;

import java.util.Date;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;


public class ResponseStatus {

	@JsonProperty("statusCd")
	private String statusCode = HttpStatus.OK.toString();
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("statusSubCd") 
	private String statusSubCode;
	
	@JsonProperty("statusTxt")
	private String statusText = "Success";
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("systemErrorCd")
	private String errorCode;
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("systemErrorTxt")
	private String errorText = null;
	
	@JsonProperty("systemErrorTimeStamp")
	private Date timestamp = new Date();

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusSubCode() {
		return statusSubCode;
	}

	public void setStatusSubCode(String statusSubCode) {
		this.statusSubCode = statusSubCode;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}