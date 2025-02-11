/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.resource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * @author Pavel Simonovsky
 *
 */
public class ResourceAccessContext {

	private String errorCode;
	
	private String componentCode;
	
	private String targetSystemCode;
	
	private String targetComponentCode;
	
	public ResourceAccessContext() {
	}
	
	public ResourceAccessContext(String errorCode, String componentCode, String targetSystemCode, String targetComponentCode) {
		this.errorCode = errorCode;
		this.componentCode = componentCode;
		this.targetSystemCode = targetSystemCode;
		this.targetComponentCode = targetComponentCode;
	}

	/**
	 * @return the componentCode
	 */
	public String getComponentCode() {
		return componentCode;
	}

	/**
	 * @param componentCode the componentCode to set
	 */
	public void setComponentCode(String componentCode) {
		this.componentCode = componentCode;
	}

	/**
	 * @return the targetSystemCode
	 */
	public String getTargetSystemCode() {
		return targetSystemCode;
	}

	/**
	 * @param targetSystemCode the targetSystemCode to set
	 */
	public void setTargetSystemCode(String targetSystemCode) {
		this.targetSystemCode = targetSystemCode;
	}

	/**
	 * @return the targetComponentCode
	 */
	public String getTargetComponentCode() {
		return targetComponentCode;
	}

	/**
	 * @param targetComponentCode the targetComponentCode to set
	 */
	public void setTargetComponentCode(String targetComponentCode) {
		this.targetComponentCode = targetComponentCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}
}
