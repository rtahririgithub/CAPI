/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.svc.identityprofile;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

/**
 * @author Pavel Simonovsky
 *
 */
public class ConfigurableProfileRegistrationPolicyTest {

	@Test
	public void test_matching_account() throws Exception {
		ConfigurableProfileRegistrationPolicy policy = new ConfigurableProfileRegistrationPolicy("I:R");
		assertThat(true, is(policy.isEligible('I', 'R')));
	}

	@Test
	public void test_matching_account_with_multiple_subtypes() throws Exception {
		ConfigurableProfileRegistrationPolicy policy = new ConfigurableProfileRegistrationPolicy("I:123B, B:ABD,C:*");
		assertThat(true, is(policy.isEligible('I', '1')));
		assertThat(true, is(policy.isEligible('I', '2')));
		assertThat(true, is(policy.isEligible('I', '3')));
		assertThat(true, is(policy.isEligible('I', 'B')));
		assertThat(false, is(policy.isEligible('I', 'C')));
		assertThat(true, is(policy.isEligible('B', 'A')));
		assertThat(true, is(policy.isEligible('B', 'B')));
		assertThat(true, is(policy.isEligible('B', 'D')));
		assertThat(true, is(policy.isEligible('C', 'Y')));
	}
	
	@Test
	public void test_matching_wildcard_account() throws Exception {
		ConfigurableProfileRegistrationPolicy policy = new ConfigurableProfileRegistrationPolicy("I:*");
		assertThat(true, is(policy.isEligible('I', 'R')));
		assertThat(true, is(policy.isEligible('I', '2')));
		assertThat(true, is(policy.isEligible('I', '3')));
	}
	
	@Test
	public void test_not_matching_account() throws Exception {
		ConfigurableProfileRegistrationPolicy policy = new ConfigurableProfileRegistrationPolicy("I:R");
		assertThat(false, is(policy.isEligible('I', 'I')));
	}
	
	@Test
	public void test_not_matching_bc_accounts() throws Exception {
		ConfigurableProfileRegistrationPolicy policy = new ConfigurableProfileRegistrationPolicy("I:123BDEFJMQRWYZ,B:134ABDMNOPRWX");
		assertThat(false, is(policy.isEligible('B', 'F')));
		assertThat(false, is(policy.isEligible('B', 'G')));
		assertThat(true, is(policy.isEligible('B', '1')));
	}
	
}
