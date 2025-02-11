
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.ServiceType;
import com.telus.eas.framework.info.Info;

public class ServiceTypeInfo extends Info implements ServiceType {

	static final long serialVersionUID = 1L;

	protected String code;
	protected String description;
	protected String descriptionFrench;
	protected String serviceTypeGroupCode;

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}	
	
	public String getServiceTypeGroupCode() {
		return serviceTypeGroupCode;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setServiceTypeGroupCode(String serviceTypeGroupCode) {
		this.serviceTypeGroupCode = serviceTypeGroupCode;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ServiceTypeInfo[");
		buffer.append("code = ").append(code);
		buffer.append(" description = ").append(description);
		buffer.append(" descriptionFrench = ").append(descriptionFrench);
		buffer.append(" serviceTypeGroupCode = ").append(serviceTypeGroupCode);
		buffer.append("]");
		return buffer.toString();
	}
}