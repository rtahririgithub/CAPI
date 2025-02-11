package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.reference.PrepaidCategory;
import com.telus.api.reference.Service;

public class PrepaidCategoryInfo extends ReferenceInfo implements PrepaidCategory {
	static final long serialVersionUID = 1L;
	
	protected int priority;
	protected List services = new ArrayList();
	
	public int getPriority() {
		return priority;
	}
	
	public void setPrority (int p) {
		priority = p;
	}
	
	
	public void addFeature(Service s) {
		services.add(s);
	}
	
	public Service[] getFeatures() {
		return (Service[]) services.toArray(new Service[services.size()]);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append(getClass().getName() + ":[\n");
		s.append("    code=[").append(getCode()).append("]\n");
		s.append("    description=[").append(getDescription()).append("]\n");
		s.append("    descriptionFrench=[").append(getDescriptionFrench()).append("]\n");
		s.append("    priority=[").append(getPriority()).append("]\n");
		for (int i = 0 ; i < services.size(); i++) {
			Service wpsService = (Service) services.get(i);
			s.append("    WPS feature=[")
			 .append(wpsService.getCode()).append(", ")
			 .append(wpsService.getDescription()).append(", ")
			 .append(wpsService.getPriority()).append("]\n");
		}
		s.append("]");

		return s.toString();
	}

}
