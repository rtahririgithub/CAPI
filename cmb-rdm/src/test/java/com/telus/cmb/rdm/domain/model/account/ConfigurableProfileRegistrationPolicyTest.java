package com.telus.cmb.rdm.domain.model.account;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

import com.telus.rdm.domain.account.AccountType;
import com.telus.rdm.domain.identityprofile.ConfigurableProfileRegistrationPolicy;

public class ConfigurableProfileRegistrationPolicyTest {

	private ConfigurableProfileRegistrationPolicy policy;
	
	@Test
	public void test_empty_pattern() {
		policy = new ConfigurableProfileRegistrationPolicy("");
		assertThat(false, is(policy.isEligible(AccountType.BUSINESS)));
	}

	@Test
	public void test_null_pattern() {
		policy = new ConfigurableProfileRegistrationPolicy(null);
		assertThat(false, is(policy.isEligible(AccountType.BUSINESS)));
	}

	@Test
	public void test_valid_pattern() {
		policy = new ConfigurableProfileRegistrationPolicy("");
		assertThat(false, is(policy.isEligible(AccountType.BUSINESS)));
	}
	
}
