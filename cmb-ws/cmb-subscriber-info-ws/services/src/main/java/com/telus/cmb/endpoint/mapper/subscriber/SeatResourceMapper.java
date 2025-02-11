package com.telus.cmb.endpoint.mapper.subscriber;

import com.telus.api.resource.Resource;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SeatResource;

public class SeatResourceMapper extends AbstractSchemaMapper<SeatResource, Resource> {

	public SeatResourceMapper() {
		super(SeatResource.class, Resource.class);
	}

	protected SeatResource performSchemaMapping(Resource source, SeatResource target) {
		target.setResourceNumber(source.getResourceNumber());
		target.setResourceTypeCd(source.getResourceType());
		target.setResourceStatus(source.getResourceStatus());
		return super.performSchemaMapping(source, target);
	}
}
