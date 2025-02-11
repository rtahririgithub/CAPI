/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.identityprofile;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.telus.rdm.domain.account.AccountType;

/**
 * Consumer profile eligibility policy configurable using following pattern: {accountType:<accountSubType|*>,...}
 * 
 * @author x113300
 *  *
 */
public class ConfigurableProfileRegistrationPolicy implements IdentityProfileRegistrationPolicy {
	
	private Set<AccountType> eligibleAccountTypes = new HashSet<AccountType>();

	public ConfigurableProfileRegistrationPolicy(String pattern) {
		if (StringUtils.isNotEmpty(pattern)) {
			parsePattern(pattern);
		}
	}
	
	private void parsePattern(String pattern) {
		for (String patternToken : pattern.split(", ")) {
		
			String [] typeTokens = patternToken.split(":");
			if (typeTokens.length == 2) {
				addAccountType(typeTokens[0].trim(), typeTokens[1].trim());
			}
		}
	}
	
	private void addAccountType(String primaryType, String secondaryType) {
		if (StringUtils.equals(secondaryType, "*")) {
			eligibleAccountTypes.addAll(AccountType.getAllTypesOfPrimary(primaryType));
		} else {
			AccountType type = AccountType.fromValue(primaryType, secondaryType);
			if (type != null) {
				eligibleAccountTypes.add(type);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.rdm.domain.model.consumer.ConsumerProfileRegistrationPolicy#isEligible(com.telus.cmb.rdm.domain.model.account.AccountType)
	 */
	@Override
	public boolean isEligible(AccountType accountType) {
		return eligibleAccountTypes.contains(accountType);
	}

}
