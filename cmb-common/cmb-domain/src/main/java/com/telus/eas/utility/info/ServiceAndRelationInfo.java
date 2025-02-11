package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class ServiceAndRelationInfo extends Info {

	private static final long serialVersionUID = -4804970204559118137L;

	private ServiceInfo service;
	private ServiceRelationInfo serviceRelation;
	
	public ServiceInfo getService() {
		return service;
	}
	
	public void setService(ServiceInfo service) {
		this.service = service;
	}
	
	public ServiceRelationInfo getServiceRelation() {
		return serviceRelation;
	}
	
	public void setServiceRelation(ServiceRelationInfo serviceRelation) {
		this.serviceRelation = serviceRelation;
	}
}
