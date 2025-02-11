package com.telus.cmb.jws.mapping;


import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceDataSharingGroup;

public class ServiceDataSharingMapper extends AbstractSchemaMapper<ServiceDataSharingGroup, com.telus.api.reference.ServiceDataSharingGroup>{

	public ServiceDataSharingMapper() {
		super(ServiceDataSharingGroup.class,com.telus.api.reference.ServiceDataSharingGroup.class);
	}

	@Override
	protected ServiceDataSharingGroup performSchemaMapping(com.telus.api.reference.ServiceDataSharingGroup source, ServiceDataSharingGroup target) {
		target.setContributingInd(source.isContributing()); 
		target.setDataSharingGroupCode(source.getDataSharingGroupCode());
		return super.performSchemaMapping(source, target);
	}

}
