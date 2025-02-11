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

import com.telus.eas.utility.info.SpecialNumberRangeInfo;
import com.telus.tmi.xmlschema.xsd.service.basetypes.serviceorderreferencetypes_v4.SpecialNumberRange;

/**
 * @author Anitha Duraisamy
 *
 */
public class SpecialNumberRangeMapper extends AbstractSchemaMapper<SpecialNumberRange, SpecialNumberRangeInfo> {

	public SpecialNumberRangeMapper() {
		super(SpecialNumberRange.class, SpecialNumberRangeInfo.class);
	}
	
	@Override
	protected SpecialNumberRange performSchemaMapping(SpecialNumberRangeInfo source, SpecialNumberRange target) {
		 
		target.setCode(source.getFirstNumberInRange());
		target.setFirstNumberInRange(source.getFirstNumberInRange());
		target.setLastNumberInRange(source.getLastNumberInRange());
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());

		return super.performSchemaMapping(source, target);
	}

}
