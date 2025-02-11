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

/**
 * @author x113300
 *
 */
public class ConsumerAccount extends Account {

	private static final long serialVersionUID = 1L;

	@Override
	public AccountType getType() {
		return AccountType.CONSUMER;
	}

}
