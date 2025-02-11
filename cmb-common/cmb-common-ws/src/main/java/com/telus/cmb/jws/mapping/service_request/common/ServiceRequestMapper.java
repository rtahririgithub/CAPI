package com.telus.cmb.jws.mapping.service_request.common;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestHeader;

public class ServiceRequestMapper extends AbstractSchemaMapper<ServiceRequestHeader, ServiceRequestHeaderInfo> {

	public ServiceRequestMapper() {
		super(ServiceRequestHeader.class, ServiceRequestHeaderInfo.class);
	}

	@Override
	protected ServiceRequestHeaderInfo performDomainMapping(ServiceRequestHeader source, ServiceRequestHeaderInfo target) {
		target.setApplicationId(source.getApplicationId());
		target.setLanguageCode(source.getLanguageCode().value());
		target.setReferenceNumber(source.getReferenceNumber());
		target.setServiceRequestNote(new ServiceRequestNoteMapper().mapToDomain(source.getServiceRequestNote()));
		target.setServiceRequestParent(new ServiceRequestParentMapper().mapToDomain(source.getServiceRequestParent()));
		return super.performDomainMapping(source, target);
	}

}