/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.account;

import com.telus.rdm.annotations.DomainFactory;

/**
 * @author x113300
 *
 */

@DomainFactory
public class AccountFactory {

	public Account createAccount(AccountType type) {
		
		Account account = null;
		
		if (type.isBusiness()) {
			account = new BusinessAccount();
		} else if (type.isCorporate()) {
			account = new CorporateAccount();
		} else if (type.isConsumer()) {
			account = new ConsumerAccount();
		}
		
		return account;
	}
	
}
