package com.telus.cmb.endpoint.mapper.airtime;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.Period;

public class PeriodMapper extends AbstractSchemaMapper<Period, ServicePeriodInfo> {
	
	public PeriodMapper() {
		super(Period.class, ServicePeriodInfo.class);
	}

	@Override
	protected Period performSchemaMapping(ServicePeriodInfo source, Period target) {
		target.setPeriodName(source.getPeriodName());
		target.setPeriodValueCode(source.getCode());
		target.getPeriodHourList().addAll(new ServicePeriodHourMapper().mapToSchema(source.getServicePeriodHours()));
		return super.performSchemaMapping(source, target);
	}

}
