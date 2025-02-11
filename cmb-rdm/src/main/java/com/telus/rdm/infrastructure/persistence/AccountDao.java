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

import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;

/**
 * @author x113300
 *
 */
public interface AccountDao {

	Account getAccount(Integer accountId);
	
	Customer getCustomer(Integer accountId);
	
	ConsumerName getContactName(Integer accountId);
	
}
