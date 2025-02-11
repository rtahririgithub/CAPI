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
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.BankAccount;


public class BankAccountMapper extends AbstractSchemaMapper<BankAccount, BankAccountInfo> {

	public BankAccountMapper(){
		super(BankAccount.class, BankAccountInfo.class);
	}

	
	@Override
	protected BankAccountInfo performDomainMapping(BankAccount source, BankAccountInfo target) {
		target.setBankAccountHolder(source.getBankAccountHolder());
		target.setBankAccountNumber(source.getBankAccountNumber());
		target.setBankAccountType(source.getBankAccountType());
		target.setBankBranchNumber(source.getBankBranchNumber());
		target.setBankCode(source.getBankCode());

		return super.performDomainMapping(source, target);
	}

}
