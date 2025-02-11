package com.telus.cmb.endpoint.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.PortOutEligibilityInfo;

public class SubscriberPortOutEligibilityInfoMapper extends AbstractSchemaMapper<PortOutEligibilityInfo, com.telus.eas.portability.info.PortOutEligibilityInfo> {

	public SubscriberPortOutEligibilityInfoMapper() {
		super(PortOutEligibilityInfo.class, com.telus.eas.portability.info.PortOutEligibilityInfo.class);
	}

	@Override
	protected com.telus.eas.portability.info.PortOutEligibilityInfo performDomainMapping(PortOutEligibilityInfo source, com.telus.eas.portability.info.PortOutEligibilityInfo target) {
		target.setEligible(source.isEligibilityInd());
		target.setTransferBlocking(source.isTransferBlockingInd());
		return super.performDomainMapping(source, target);
	}

	@Override
	protected PortOutEligibilityInfo performSchemaMapping(com.telus.eas.portability.info.PortOutEligibilityInfo source, PortOutEligibilityInfo target) {
		target.setEligibilityInd(source.isEligible());
		target.setTransferBlockingInd(source.isTransferBlocking());
		return super.performSchemaMapping(source, target);
	}

}
