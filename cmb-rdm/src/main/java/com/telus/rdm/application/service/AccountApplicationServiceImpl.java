/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.application.service;

import com.telus.rdm.annotations.ApplicationService;
import com.telus.rdm.domain.account.Account;

/**
 * @author x113300
 *
 */

@ApplicationService
public class AccountApplicationServiceImpl implements AccountApplicationService {

	/* (non-Javadoc)
	 * @see com.telus.cmb.application.service.AccountApplicationService#createAccount(com.telus.cmb.domain.account.Account)
	 */
	@Override
	public Account createAccount(Account account) {
		account.create();
		return account;
	}

}
