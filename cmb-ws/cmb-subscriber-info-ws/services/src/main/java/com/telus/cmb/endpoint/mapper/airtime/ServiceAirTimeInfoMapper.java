package com.telus.cmb.endpoint.mapper.airtime;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceAirTimeInfo;

public class ServiceAirTimeInfoMapper extends AbstractSchemaMapper<ServiceAirTimeInfo, ServiceAirTimeAllocationInfo> {

	public ServiceAirTimeInfoMapper() {
		super(ServiceAirTimeInfo.class, ServiceAirTimeAllocationInfo.class);
	}

	@Override
	protected ServiceAirTimeInfo performSchemaMapping(ServiceAirTimeAllocationInfo source, ServiceAirTimeInfo target) {
		target.setServiceCodeValidInd(source.isValidSOC());
		target.setBillCycleIndependentChargeInd(source.getBillCycleTreatmentCode());
		target.setCoverageTypeCd(source.getCoverageType());
		target.setProductTypeCd(source.getProductType());
		target.setServiceTypeCd(source.getServiceType());
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpiratonDate(source.getExpirationDate());
		return super.performSchemaMapping(source, target);
	}

}
