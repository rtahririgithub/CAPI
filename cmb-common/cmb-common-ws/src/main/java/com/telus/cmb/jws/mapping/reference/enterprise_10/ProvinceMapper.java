/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.enterprise_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.utility.info.ProvinceInfo;
import com.telus.schemas.eca.common_types_2_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprise_reference_types_1_0.Province;

/**
 * @author Pavel Simonovsky
 *
 */
public class ProvinceMapper extends AbstractSchemaMapper<Province, ProvinceInfo> {
	
	public ProvinceMapper() {
		super(Province.class, ProvinceInfo.class);
	}
	
	@Override
	protected Province performSchemaMapping(ProvinceInfo source, Province target) {
		
		target.setCode(toEnum(source.getCode(), ProvinceCode.class));
		target.setCanadaPostCode(source.getCanadaPostCode());
		target.setCountryCode(source.getCountryCode());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		
		return super.performSchemaMapping(source, target);
	}

}
