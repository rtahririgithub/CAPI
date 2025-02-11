package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.ServiceAirTimeAllocation;

public class ServiceAirTimeAllocationMapper extends AbstractSchemaMapper<ServiceAirTimeAllocation, ServiceAirTimeAllocationInfo> {
	private static FeatureAirTimeAllocationMapper featureAirTimeMapper = new FeatureAirTimeAllocationMapper();

	public ServiceAirTimeAllocationMapper() {
		super(ServiceAirTimeAllocation.class, ServiceAirTimeAllocationInfo.class);
	}

	@Override
	protected ServiceAirTimeAllocation performSchemaMapping( ServiceAirTimeAllocationInfo source, ServiceAirTimeAllocation target) {
		
		target.setBillCycleIndependentChargeInd(source.getBillCycleTreatmentCode());
		target.setCode(source.getServiceCode());
		target.setCoverageTypeCd(source.getCoverageType());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpiratonDate(source.getExpirationDate());
		target.setProductTypeCd(source.getProductType());
		target.setServiceTypeCd(source.getServiceType());
		target.setServiceCodeValidInd(source.isValidSOC());
		target.setErrorCode(source.getErrorCode());
		target.setErrorMessage(source.getErrorMessage());
		target.getFeatureAirTimeAllocation().addAll( featureAirTimeMapper.mapToSchema( source.getFeatureAirTimeAllocations0() ) );
		return super.performSchemaMapping(source, target);
	}


}
