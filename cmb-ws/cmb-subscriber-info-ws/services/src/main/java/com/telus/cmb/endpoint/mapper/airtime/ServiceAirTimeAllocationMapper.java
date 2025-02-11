package com.telus.cmb.endpoint.mapper.airtime;

import java.util.Arrays;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceAirTimeAllocation;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Description;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.MultilingualCodeDescriptionList;

public class ServiceAirTimeAllocationMapper extends AbstractSchemaMapper<ServiceAirTimeAllocation, ServiceAirTimeAllocationInfo> {

	public ServiceAirTimeAllocationMapper() {
		super(ServiceAirTimeAllocation.class, ServiceAirTimeAllocationInfo.class);
	}

	@Override
	protected ServiceAirTimeAllocation performSchemaMapping(ServiceAirTimeAllocationInfo source, ServiceAirTimeAllocation target) {
		target.setCode(source.getServiceCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setMarketingDescription(getMarketingDescription(source.getMarketingDescription(), source.getMarketingDescriptionFrench()));
		target.setServiceAirTimeInfo(new ServiceAirTimeInfoMapper().mapToSchema(source));
		target.getFeatureAirTimeAllocationList().addAll(new FeatureAirTimeAllocationMapper().mapToSchema(source.getFeatureAirTimeAllocations0()));
		target.setErrorInfo(new ResponseErrorInfoMapper().mapToSchema(source));
		return super.performSchemaMapping(source, target);
	}

	private MultilingualCodeDescriptionList getMarketingDescription(String marketingDescription, String marketingDescriptionFr) {
		MultilingualCodeDescriptionList marketingDescriptionList = new MultilingualCodeDescriptionList();
		Description descriptionEnglish = new Description();
		descriptionEnglish.setDescriptionText(marketingDescription);
		descriptionEnglish.setLocale("EN");
		Description descriptionFrench = new Description();
		descriptionFrench.setDescriptionText(marketingDescriptionFr);
		descriptionFrench.setLocale("FR");
		marketingDescriptionList.setDescription(Arrays.asList(new Description[]{descriptionEnglish, descriptionFrench}));
		return marketingDescriptionList;
	}
	
}
