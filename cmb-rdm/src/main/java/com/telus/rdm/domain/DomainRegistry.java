/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain;

import org.springframework.context.ApplicationContext;

import com.telus.rdm.domain.account.AccountCreationStrategy;
import com.telus.rdm.domain.account.AccountRepository;
import com.telus.rdm.domain.identityprofile.IdentityProfileService;
import com.telus.rdm.domain.subscriber.SubscriberActivationStrategy;
import com.telus.rdm.domain.subscriber.SubscriberCreationStrategy;
import com.telus.rdm.domain.subscriber.SubscriberMovingStrategy;
import com.telus.rdm.domain.subscriber.SubscriberRepository;

/**
 * @author x113300
 *
 */
public class DomainRegistry {
	
	private static ApplicationContext applicationContext;
	
	public static void setApplicationContext(ApplicationContext applicationContext) {
		DomainRegistry.applicationContext = applicationContext;
	}

	public static AccountRepository accountRepository() {
		return applicationContext.getBean(AccountRepository.class);
	}

	public static SubscriberRepository subsciberRepository() {
		return applicationContext.getBean(SubscriberRepository.class);
	}
	
	public static AccountCreationStrategy accountCreationStrategy() {
		return applicationContext.getBean(AccountCreationStrategy.class);
	}
	
	public static SubscriberActivationStrategy subscriberActivationStrategy() {
		return applicationContext.getBean(SubscriberActivationStrategy.class);
	}
	
	public static SubscriberCreationStrategy subscriberCreationStrategy() {
		return applicationContext.getBean(SubscriberCreationStrategy.class);
	}

	public static SubscriberMovingStrategy subscriberMovingStrategy() {
		return applicationContext.getBean(SubscriberMovingStrategy.class);
	}
	
	
	public static IdentityProfileService consumerProfileService() {
		return applicationContext.getBean(IdentityProfileService.class);
	}
	
	public static <T> T getStrategy(Class<T> type) {
		return applicationContext.getBean(type);
	}
}
