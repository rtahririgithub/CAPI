/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.account.AccountFactory;
import com.telus.rdm.domain.account.AccountType;
import com.telus.rdm.domain.shared.Brand;

/**
 * @author x113300
 *
 */

public class AccountRowMapper implements RowMapper<Account> {
	
	private AccountFactory accountFactory;

	public AccountRowMapper(AccountFactory accountFactory) {
		this.accountFactory = accountFactory;
	}

	@Override
	public Account mapRow(ResultSet rs, int idx) throws SQLException {
		
		String primaryType = rs.getString("account_type");
		String secondaryType = rs.getString("account_sub_type");
		
		AccountType accountType = AccountType.fromValue(primaryType, secondaryType);
		
		Account account = accountFactory.createAccount(accountType);
		account.setAccountId(rs.getInt("ban"));
		
		Brand brand = Brand.fromValue(rs.getInt("brand_id"));
		account.setBrand(brand);
		
		return account;
	}
}
