package com.telus.cmb.jws.mapping;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.ServiceFeatureClassificationInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.ServiceFeatureClassification;

public class ServiceFeatureClassificationMapper extends ReferenceMapper<ServiceFeatureClassification, ServiceFeatureClassificationInfo> {

	public ServiceFeatureClassificationMapper() {
		super(ServiceFeatureClassification.class, ServiceFeatureClassificationInfo.class);
	}

}
