package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.ServiceFeatureClassificationInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.ServiceFeatureClassification;

public class ServiceFeatureClassificationMapper extends ReferenceMapper<ServiceFeatureClassification, ServiceFeatureClassificationInfo> {

	public ServiceFeatureClassificationMapper() {
		super(ServiceFeatureClassification.class, ServiceFeatureClassificationInfo.class);
	}

}
