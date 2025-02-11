/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ConsumerName;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.NameFormat;

/**
 * @author Dimitry Siganevich
 *
 */
public class ConsumerNameMapper extends AbstractSchemaMapper<ConsumerName, ConsumerNameInfo> {

	public ConsumerNameMapper(){
		super(ConsumerName.class, ConsumerNameInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ConsumerName performSchemaMapping(ConsumerNameInfo source, ConsumerName target) {
		target.setAdditionalLine(source.getAdditionalLine());
		target.setFirstName(source.getFirstName());
		target.setGeneration(source.getGeneration());
		target.setLastName(source.getLastName());
		target.setMiddleInitial(source.getMiddleInitial());
		target.setNameFormat(toEnum(source.getNameFormat(), NameFormat.class));
		target.setTitle(source.getTitle());
		
		return super.performSchemaMapping(source, target);
	}

}
