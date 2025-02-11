package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.ServiceRelationCode;
import com.telus.eas.utility.info.ServiceRelationInfo;

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
