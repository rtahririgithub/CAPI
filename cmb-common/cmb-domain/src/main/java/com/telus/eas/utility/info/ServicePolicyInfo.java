package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ServicePolicyInfo extends Info implements Reference {

	static final long serialVersionUID = 1L;

	private String serviceCode;
	private String businessRoleCode;
	private String privilegeCode;
	private boolean available;
	private String descriptionFrench;
	private String description;

	public String getBusinessRoleCode() {
		return businessRoleCode;
	}

	public void setBusinessRoleCode(String businessRoleCode) {
		this.businessRoleCode = businessRoleCode;
	}

	public void setPrivilegeCode(String privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	public String getPrivilegeCode() {
		return privilegeCode;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public String getCode() {
		return (serviceCode.trim() + businessRoleCode.trim() + privilegeCode
				.trim());
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}