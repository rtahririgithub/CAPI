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
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.Cheque;


public class ChequeMapper extends AbstractSchemaMapper<Cheque, ChequeInfo> {

	public ChequeMapper(){
		super(Cheque.class, ChequeInfo.class);
	}

		@Override
	protected ChequeInfo performDomainMapping(Cheque source, ChequeInfo target) {

		BankAccountInfo bankAccountInfo = new BankAccountMapper().mapToDomain(source.getBankAccount());
		if (bankAccountInfo != null)
			target.setBankAccount(bankAccountInfo);
		if (source.getNumber() != null)
			target.setChequeNumber(source.getNumber());

		return super.performDomainMapping(source, target);
	}

}
