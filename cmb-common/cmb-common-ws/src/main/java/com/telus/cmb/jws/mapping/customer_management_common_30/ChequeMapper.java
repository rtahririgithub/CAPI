/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BankAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.Cheque;

/**
 * @author Dimitry Siganevich
 *
 */
public class ChequeMapper extends AbstractSchemaMapper<Cheque, ChequeInfo> {

	public ChequeMapper(){
		super(Cheque.class, ChequeInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Cheque performSchemaMapping(ChequeInfo source, Cheque target) {
		target = null;
		int notNullCount = 0;
		Cheque tmpCheque = new Cheque();
		BankAccount bankAccount = new BankAccountMapper().mapToSchema(source.getBankAccount0());

		if (bankAccount != null) {
			tmpCheque.setBankAccount(bankAccount);
			notNullCount++;
		}
		if (source.getChequeNumber() != null) {
			tmpCheque.setNumber(source.getChequeNumber());
			notNullCount++;
		}
		if (notNullCount > 0)
			target = tmpCheque;
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
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
