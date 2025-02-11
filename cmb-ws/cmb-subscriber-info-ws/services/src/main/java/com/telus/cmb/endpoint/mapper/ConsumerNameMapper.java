package com.telus.cmb.endpoint.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ConsumerName;

public class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName, ConsumerNameInfo> {

	public ConsumerNameMapper() {
		super(ConsumerName.class, ConsumerNameInfo.class);
	}

	@Override
	protected ConsumerNameInfo performDomainMapping(ConsumerName source, ConsumerNameInfo target) {
		target.setAdditionalLine(source.getAdditionalLine());
		target.setFirstName(source.getFirstName());
		target.setGeneration(source.getGeneration());
		target.setLastName(source.getLastName());
		target.setMiddleInitial(source.getMiddleInitial());
		if (source.getNameFormat() != null) {
			target.setNameFormat(source.getNameFormat().value());
		}
		target.setTitle(source.getTitle());
		return super.performDomainMapping(source, target);
	}
}
