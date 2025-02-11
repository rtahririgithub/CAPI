package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.ServicePeriodHours;

public class ServicePeriodHourMapper extends AbstractSchemaMapper<ServicePeriodHours,com.telus.api.reference.ServicePeriodHours > {

	public ServicePeriodHourMapper() {
		super(ServicePeriodHours.class, com.telus.api.reference.ServicePeriodHours.class);
	}

	@Override
	protected ServicePeriodHours performSchemaMapping( com.telus.api.reference.ServicePeriodHours source, ServicePeriodHours target) {
		target.setDay(source.getDay());
		target.setFrom( toXmlGregorianCalendar(source.getFrom()));
		target.setTo(toXmlGregorianCalendar(source.getTo()));
		return super.performSchemaMapping(source, target);
	}

}
