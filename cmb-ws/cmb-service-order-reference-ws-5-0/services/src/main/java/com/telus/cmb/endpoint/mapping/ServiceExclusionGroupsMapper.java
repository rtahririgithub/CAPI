/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import java.util.Arrays;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.ServiceExclusionGroupsInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v5.ServiceExclusionGroups;

/**
 * @author Pavel Simonovsky
 *
 */
public class ServiceExclusionGroupsMapper extends ReferenceMapper<ServiceExclusionGroups, ServiceExclusionGroupsInfo> {

	public ServiceExclusionGroupsMapper() {
		super(ServiceExclusionGroups.class, ServiceExclusionGroupsInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected ServiceExclusionGroups performSchemaMapping(ServiceExclusionGroupsInfo source, ServiceExclusionGroups target) {
		if (source.getExclusionGroups() != null) {
			target.getExclusionGroup().addAll(Arrays.asList(source.getExclusionGroups()));
		}
		return super.performSchemaMapping(source, target);
	}
	
}
