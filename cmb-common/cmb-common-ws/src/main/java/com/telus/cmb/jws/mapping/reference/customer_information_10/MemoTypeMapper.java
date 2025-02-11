/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.customer_information_10;

import com.telus.eas.utility.info.MemoTypeInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.customer_information_reference_types_1_0.MemoType;

/**
 * @author Pavel Simonovsky
 *
 */
public class MemoTypeMapper extends ReferenceMapper<MemoType, MemoTypeInfo> {

	public MemoTypeMapper() {
		super(MemoType.class, MemoTypeInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected MemoType performSchemaMapping(MemoTypeInfo source, MemoType target) {
		
		target.setCategory(source.getCategory());
		target.setManualInd(source.getManualInd());
		target.setNumberOfParameters(source.getNumberOfParameters());
		target.setSystemText(source.getSystemText());
		target.setSystemTextFrench(source.getSystemTextFrench());
		
		return super.performSchemaMapping(source, target);
	}
}
