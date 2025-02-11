package com.telus.cmb.endpoint.mapper.airtime;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ServiceIdentityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceIdentity;

public class ServiceIdentityMapper extends AbstractSchemaMapper<ServiceIdentity, ServiceIdentityInfo> {

	public ServiceIdentityMapper() {
		super(ServiceIdentity.class, ServiceIdentityInfo.class);
	}

	@Override
	protected ServiceIdentityInfo performDomainMapping(ServiceIdentity source, ServiceIdentityInfo target) {
		target.setServiceCode(source.getServiceCode());
		if (source.getEffectiveDate() != null)
			target.setEffectiveDate(source.getEffectiveDate());
		return super.performDomainMapping(source, target);
	}

}
