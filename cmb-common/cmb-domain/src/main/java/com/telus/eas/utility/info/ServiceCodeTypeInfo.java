package com.telus.eas.utility.info;

import com.telus.api.reference.ServiceCodeType;
import com.telus.eas.framework.info.Info;

public class ServiceCodeTypeInfo extends Info implements ServiceCodeType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String serviceCode;
	private String serviceType;
	private String description;
	private String descriptionFrench;
	
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public String getCode() {
		return serviceCode;
	}

	public String getServiceType() {
		return serviceType;
	}



}
