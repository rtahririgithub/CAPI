package com.telus.eas.subscriber.info;

import com.telus.api.resource.Resource;
import com.telus.eas.framework.info.Info;

public class ResourceInfo extends Info implements Resource {

	private static final long serialVersionUID = 1L;
	private String resourceType;
	private String resourceNumber;
	private String resourceStatus;
	
	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceNumber() {
		return resourceNumber;
	}

	public void setResourceNumber(String resourceNumber) {
		this.resourceNumber = resourceNumber;
	}
	
	public String getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		this.resourceStatus = resourceStatus;
	}

	public String toString() {
		return " [ resourceType=" + resourceType	+ ", resourceNumber=" + resourceNumber + ", resourceStatus=" + resourceStatus + "] \n";
	}

}