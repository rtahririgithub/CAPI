/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.resource_order_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.schemas.eca.common_types_2_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.resource.basetypes.resource_order_reference_types_1_0.NumberGroup;

/**
 * @author Pavel Simonovsky
 *
 */
public class NumberGroupMapper extends ReferenceMapper<NumberGroup, NumberGroupInfo> {

	public NumberGroupMapper() {
		super(NumberGroup.class, NumberGroupInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected NumberGroup performSchemaMapping(NumberGroupInfo source, NumberGroup target) {
		
		target.setDefaultDealerCode(source.getDefaultDealerCode());
		target.setDefaultSalesRepCode(source.getDefaultSalesCode());
		target.setNetworkId(source.getNetworkId());
		target.setNumberLocation(source.getNumberLocation());
		target.setProvinceCode(toEnum(source.getProvinceCode(), ProvinceCode.class));
		target.getNpaNXX().addAll(toCollection(source.getNpaNXX()));
		
		return super.performSchemaMapping(source, target);
	}
	
	@Override
	protected NumberGroupInfo performDomainMapping(NumberGroup source, NumberGroupInfo target) {

		target.setCode(source.getCode());
		target.setDefaultDealerCode(source.getDefaultDealerCode());
		target.setDefaultSalesCode(source.getDefaultSalesRepCode());
		target.setNetworkId(source.getNetworkId());
		target.setNumberLocation(source.getNumberLocation());
		target.setProvinceCode(source.getProvinceCode().value());
		target.setNpaNXX(source.getNpaNXX().toArray(new String[0]));
		
		return super.performDomainMapping(source, target);
	}
}
