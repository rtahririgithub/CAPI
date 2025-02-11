package com.telus.cmb.jws.mapping;

import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.cmb.jws.ServiceRelationCode;

public class ServiceRelationMapper extends AbstractSchemaMapper<ServiceRelationCode, ServiceRelationInfo> {

	public ServiceRelationMapper() {
		super(ServiceRelationCode.class, ServiceRelationInfo.class);
	}

	@Override
	protected ServiceRelationInfo performDomainMapping(ServiceRelationCode source, ServiceRelationInfo target) {
		target.setServiceCode(source.getServiceCode());
		target.setType(source.getServiceRelationTypeCode());
		return target;
	}
}
