package com.telus.cmb.jws.mapping;

import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.Period;

public class PeriodMapper extends AbstractSchemaMapper<Period, ServicePeriodInfo> {
	private static ServicePeriodHourMapper periodHourMapper = new ServicePeriodHourMapper();

	public PeriodMapper() {
		super(Period.class, ServicePeriodInfo.class);
	}

	@Override
	protected Period performSchemaMapping(ServicePeriodInfo source,	Period target) {
		target.setPeriodName(source.getPeriodName());
		target.setPeriodValueCode(source.getCode());
		target.getPeriodHourList().addAll( periodHourMapper.mapToSchema( source.getServicePeriodHours()));
		return super.performSchemaMapping(source, target);
	}

}
