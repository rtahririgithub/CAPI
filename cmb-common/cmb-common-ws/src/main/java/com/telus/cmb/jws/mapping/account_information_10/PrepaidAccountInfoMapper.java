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
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.AutoTopUp;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.PrepaidAccountInfo;

/**
 * @author Dimitry Siganevich
 *
 */
public class PrepaidAccountInfoMapper extends AbstractSchemaMapper<PrepaidAccountInfo, PrepaidConsumerAccountInfo> {

	public PrepaidAccountInfoMapper(){
		super(PrepaidAccountInfo.class, PrepaidConsumerAccountInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PrepaidAccountInfo performSchemaMapping(PrepaidConsumerAccountInfo source, PrepaidAccountInfo target) {
		target.setAirtimeRate(source.getAirtimeRate());
		AutoTopUp autoTopUp = new AutoTopUpMapper().mapToSchema(source.getAutoTopUp0());
		target.setAutoTopUp(autoTopUp);
		target.setBalance(source.getBalance());
		target.setBalanceExpiryDate(source.getBalanceExpiryDate());
		target.setBillingType(source.getBillingType());
		target.setLongDistanceRate(source.getLongDistanceRate());
		target.setMaximumBalanceCap(source.getMaximumBalanceCap());
		target.setMinimumBalanceDate(source.getMinimumBalanceDate());
		target.setOutstandingCharge(source.getOutstandingCharge());
		target.setTopUpCreditCard(new CreditCardMapper().mapToSchema(source.getTopUpCreditCard0()));

		return super.performSchemaMapping(source, target);
	}

}
