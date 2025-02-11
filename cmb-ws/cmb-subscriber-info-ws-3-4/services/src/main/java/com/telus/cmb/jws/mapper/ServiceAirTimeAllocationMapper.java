package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceAirTimeAllocation;

public class ServiceAirTimeAllocationMapper extends AbstractSchemaMapper<ServiceAirTimeAllocation, ServiceAirTimeAllocationInfo> {
	private static FeatureAirTimeAllocationMapper feautreAirTimeMapper = new FeatureAirTimeAllocationMapper();
	private static ServiceAirTimeInfoMapper serviceAirTimeInfoMapper = new ServiceAirTimeInfoMapper();
	private static ResponseErrorInfoMapper responseErrorInfoMapper = new ResponseErrorInfoMapper();

	public ServiceAirTimeAllocationMapper() {
		super(ServiceAirTimeAllocation.class, ServiceAirTimeAllocationInfo.class);
	}
	
		@Override
	protected ServiceAirTimeAllocation performSchemaMapping( ServiceAirTimeAllocationInfo source, ServiceAirTimeAllocation target) {
		target.setCode(source.getServiceCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setServiceAirTimeInfo(serviceAirTimeInfoMapper.mapToSchema(source));
		target.setErrorInfo(responseErrorInfoMapper.mapToSchema(source));
		target.getFeatureAirTimeAllocationList().addAll( feautreAirTimeMapper.mapToSchema( source.getFeatureAirTimeAllocations0() ) );
		return super.performSchemaMapping(source, target);
	}


}
