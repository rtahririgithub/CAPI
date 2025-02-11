/*
 *  Copyright (c) 2016 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.soa.spring.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PingResponse extends RestResponse {

	@JsonProperty("nameTxt")
	private String serviceName;
	
	@JsonProperty("descriptionTxt")
	private String serviceDescription;
	
	@JsonProperty("buildLabel")
	private String buildLabel;
	
	@JsonProperty("buildDt")
	
	private String buildDate;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public String getBuildLabel() {
		return buildLabel;
	}

	public void setBuildLabel(String buildLabel) {
		this.buildLabel = buildLabel;
	}

	public String getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}
	
	
}