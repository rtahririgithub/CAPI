/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ReferToCreditAnalyst;


/**
 * @author Anitha Duraisamy
 *
 */
public class ReferToCreditAnalystMapper extends AbstractSchemaMapper<ReferToCreditAnalyst, com.telus.eas.account.info.ReferToCreditAnalystInfo> {

	public ReferToCreditAnalystMapper(){
		super(ReferToCreditAnalyst.class, com.telus.eas.account.info.ReferToCreditAnalystInfo.class);
	}
	
	
	@Override
	protected ReferToCreditAnalyst performSchemaMapping(com.telus.eas.account.info.ReferToCreditAnalystInfo  source, ReferToCreditAnalyst target) {
		
		target.setReferToCreditAnalystInd(source.isReferToCreditAnalyst());
		target.setReasonCode(source.getReasonCode());
		target.setReasonMessage(source.getReasonMessage());
		
		return super.performSchemaMapping(source, target);
	}

	
	
}
