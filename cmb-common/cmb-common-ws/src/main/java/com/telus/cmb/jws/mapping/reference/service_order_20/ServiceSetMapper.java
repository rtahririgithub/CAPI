/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.service_order_20;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceSetInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v2.ServiceSet;

/**
 * @author Pavel Simonovsky
 *
 */
public class ServiceSetMapper extends ReferenceMapper<ServiceSet, ServiceSetInfo> {

	public ServiceSetMapper() {
		super(ServiceSet.class, ServiceSetInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected ServiceSet performSchemaMapping(ServiceSetInfo source, ServiceSet target) {
		
		target.getService().addAll(new ServiceMapper().mapToSchema(
				(ServiceInfo[]) source.getServices()));
		
		return super.performSchemaMapping(source, target);
	}
}
