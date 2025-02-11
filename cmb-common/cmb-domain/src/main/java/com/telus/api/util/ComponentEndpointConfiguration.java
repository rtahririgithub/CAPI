/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

/**
 * @author Pavel Simonovsky
 *
 */
public class ComponentEndpointConfiguration {
	
	
	private String name;

	private String url;
	
	private boolean usedByProvider;
	
	private boolean usedByWebServices;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isUsedByProvider() {
		return usedByProvider;
	}

	public void setUsedByProvider(boolean usedByProvider) {
		this.usedByProvider = usedByProvider;
	}

	public boolean isUsedByWebServices() {
		return usedByWebServices;
	}

	public void setUsedByWebServices(boolean usedByWebServices) {
		this.usedByWebServices = usedByWebServices;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(name).append(": [");
		buffer.append("url = ").append(url).append(", ");
		buffer.append("usedByProvider = ").append(usedByProvider).append(", ");
		buffer.append("usedByWebServices = ").append(usedByWebServices).append("]");
		
		return buffer.toString();
	}
	
}
