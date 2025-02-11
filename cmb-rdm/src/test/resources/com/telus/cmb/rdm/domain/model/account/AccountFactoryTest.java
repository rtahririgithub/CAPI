/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.rdm.domain.model.account;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Before;
import org.testng.annotations.Test;

/**
 * @author x113300
 *
 */
@Test
public class AccountFactoryTest {
	
	private AccountFactory factory;
	
	@Before
	public void init() {
		factory = new AccountFactory();
	}

	@Test
	public void test_business_account_instantiation() {
		assertThat(factory.createAccount(AccountType.BUSINESS), is(instanceOf(BusinessAccount.class)));
	}
	
	@Test
	public void test_consumer_account_instantiation() {
		assertThat(factory.createAccount(AccountType.CONSUMER), is(instanceOf(ConsumerAccount.class)));
	}

	@Test
	public void test_corporate_account_instantiation() {
		assertThat(factory.createAccount(AccountType.CORPORATE), is(instanceOf(CorporateAccount.class)));
	}

}
