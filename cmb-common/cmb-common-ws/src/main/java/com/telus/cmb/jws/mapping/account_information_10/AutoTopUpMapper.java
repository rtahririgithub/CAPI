/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.account_information_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.AutoTopUp;

/**
 * @author Dimitry Siganevich
 *
 */
public class AutoTopUpMapper extends AbstractSchemaMapper<AutoTopUp, AutoTopUpInfo> {

	public AutoTopUpMapper(){
		super(AutoTopUp.class, AutoTopUpInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected AutoTopUp performSchemaMapping(AutoTopUpInfo source, AutoTopUp target) {
		target.setChargeAmount(source.getChargeAmount());
		target.setHasThresholdRecharge(source.hasThresholdRecharge());
		target.setNextChargeDate(source.getNextChargeDate());
		target.setThresholdAmount(source.getThresholdAmount());

		return super.performSchemaMapping(source, target);
	}
}
