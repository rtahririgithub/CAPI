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
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.BankAccount;

/**
 * @author Dimitry Siganevich
 *
 */
public class BankAccountMapper extends AbstractSchemaMapper<BankAccount, BankAccountInfo> {

	public BankAccountMapper(){
		super(BankAccount.class, BankAccountInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BankAccount performSchemaMapping(BankAccountInfo source, BankAccount target) {
		target = null;
		int notNullCount = 0;
		BankAccount tmpBankAccount = new BankAccount();
		if (source.getBankAccountHolder() != null) {
			tmpBankAccount.setBankAccountHolder(source.getBankAccountHolder());
			notNullCount++;
		}
		if (source.getBankAccountNumber() != null) {
			tmpBankAccount.setBankAccountNumber(source.getBankAccountNumber());
			notNullCount++;
		}
		if (source.getBankAccountType() != null) {
			tmpBankAccount.setBankAccountType(source.getBankAccountType());
			notNullCount++;
		}
		if (source.getBankBranchNumber() != null) {
			tmpBankAccount.setBankBranchNumber(source.getBankBranchNumber());
			notNullCount++;
		}
		if (source.getBankCode() != null) {
			tmpBankAccount.setBankCode(source.getBankCode());
			notNullCount++;
		}
		if (notNullCount > 0) {
			target = tmpBankAccount;
		}
		
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
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
