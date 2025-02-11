package com.telus.eas.subscriber.info;

import com.telus.api.resource.Resource;
import com.telus.api.resource.ResourceActivity;
import com.telus.eas.framework.info.Info;

public class ResourceActivityInfo extends Info implements ResourceActivity {

	private static final long serialVersionUID = 1L;
	private Resource resource;
	private String resourceActivity;
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getResourceActivity() {
		return resourceActivity;
	}

	public void setResourceActivity(String resourceActivity) {
		this.resourceActivity = resourceActivity;
	}
	
	public String toString() {
		
		StringBuffer str = new StringBuffer();
		str.append("ResourceActivity: \n");
		str.append(resource.toString());
		str.append("  resourceActivity = [" + resourceActivity + "]");
		
		return str.toString();
	}

}