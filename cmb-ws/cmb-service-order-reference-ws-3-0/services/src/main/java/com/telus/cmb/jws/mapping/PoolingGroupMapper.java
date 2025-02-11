/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PoolingGroupInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v3.PoolingGroup;

/**
 * @author Pavel Simonovsky
 *
 */
public class PoolingGroupMapper extends ReferenceMapper<PoolingGroup, PoolingGroupInfo> {

	public PoolingGroupMapper() {
		super(PoolingGroup.class, PoolingGroupInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected PoolingGroup performSchemaMapping(PoolingGroupInfo source, PoolingGroup target) {

		target.setCoverageType(source.getCoverageType());
		target.setPoolingGroupId(source.getPoolingGroupId());
		
		return super.performSchemaMapping(source, target);
	}
}
