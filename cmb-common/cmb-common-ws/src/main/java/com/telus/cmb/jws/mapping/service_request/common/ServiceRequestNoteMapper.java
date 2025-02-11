package com.telus.cmb.jws.mapping.service_request.common;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.servicerequest.info.ServiceRequestNoteInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.servicerequestcommontypes_v1.ServiceRequestNote;

public class ServiceRequestNoteMapper extends AbstractSchemaMapper<ServiceRequestNote, ServiceRequestNoteInfo> {

	public ServiceRequestNoteMapper() {
		super (ServiceRequestNote.class, ServiceRequestNoteInfo.class);
	}

	@Override
	protected ServiceRequestNoteInfo performDomainMapping(ServiceRequestNote source, ServiceRequestNoteInfo target) {
		target.setNoteText(source.getNoteText());
		target.setNoteTypeId(source.getNoteTypeId());
		return super.performDomainMapping(source, target);
	}

	@Override
	protected ServiceRequestNote performSchemaMapping(ServiceRequestNoteInfo source, ServiceRequestNote target) {
		target.setNoteText(source.getServiceRequestNoteText());
		target.setNoteTypeId(source.getServiceRequestNoteTypeId());
		return super.performSchemaMapping(source, target);
	}
	
	
}
