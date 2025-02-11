/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.repository;

import javax.inject.Inject;

import com.telus.rdm.annotations.DomainRepository;
import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.account.AccountRepository;
import com.telus.rdm.domain.account.AccountSearchCriteria;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.infrastructure.persistence.AccountDao;

/**
 * @author x113300
 *
 */

@DomainRepository
public class AccountRepositoryImpl implements AccountRepository {

	@Inject private AccountDao accountDao;
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	@Override
	public Account findAccounts(AccountSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Account saveAccount(Account account) {
		// TODO Auto-generated method stub
		return account;
	}
	
	@Override
	public Account getAccount(Integer accountId) {
		Account account = accountDao.getAccount(accountId);
		
		// TODO: replace with lazy load proxy
		Customer customer = accountDao.getCustomer(accountId);
		account.setCustomer(customer);
		
		// TODO: replace with lazy load proxy
		ConsumerName contactName = accountDao.getContactName(accountId);
		account.setContactName(contactName);
		
		return account; 
	}
}
