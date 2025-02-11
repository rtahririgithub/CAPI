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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer profile eligibility policy configurable using following pattern: {accountType:<accountSubType|*>,...}
 * 
 * @author Pavel Simonovsky
 *  *
 */
public class ConfigurableProfileRegistrationPolicy implements IdentityProfileRegistrationPolicy {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurableProfileRegistrationPolicy.class);
	
	private Map<Character, Collection<Character>> types = new HashMap<Character, Collection<Character>>();

	public ConfigurableProfileRegistrationPolicy(String pattern) {
		if (StringUtils.isNotEmpty(pattern)) {
			parsePattern(pattern);
		}
		
		logger.debug("Cosumer profile registration policy initialized successfully with following values: {}", types);
	}
	
	private void parsePattern(String pattern) {
		for (String patternToken : StringUtils.split(pattern, ", ")) {
		
			String [] typeTokens = patternToken.split(":");
			if (typeTokens.length == 2) {
				addAccountType(typeTokens[0].trim(), typeTokens[1].trim());
			}
		}
	}
	
	private void addAccountType(String accountType, String accountSubTypes) {
		char typeChar = accountType.charAt(0);
		Collection<Character> subTypes = getSubtypes(typeChar);
		for (char subTypeChar : accountSubTypes.toCharArray()) {
			subTypes.add(subTypeChar);
		}
	}
	
	private Collection<Character> getSubtypes(Character type) {
		Collection<Character> result = types.get(type);
		if (result == null) {
			result = new ArrayList<Character>();
			types.put(type, result);
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.rdm.domain.model.consumer.ConsumerProfileRegistrationPolicy#isEligible(com.telus.cmb.rdm.domain.model.account.AccountType)
	 */
	@Override
	public boolean isEligible(char accountType, char accountSubType) {
		Collection<Character> subTypes = getSubtypes(accountType); 
		return subTypes.contains('*') ? true : subTypes.contains(accountSubType);
	}

}
