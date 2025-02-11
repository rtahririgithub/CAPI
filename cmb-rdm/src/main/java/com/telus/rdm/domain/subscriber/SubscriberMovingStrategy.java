/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.subscriber;

import javax.inject.Inject;

import com.telus.rdm.annotations.DomainStrategy;
import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.account.AccountRepository;
import com.telus.rdm.domain.identityprofile.IdentityProfileRegistrationOrigin;
import com.telus.rdm.domain.identityprofile.IdentityProfileService;

/**
 * @author x113300
 *
 */

@DomainStrategy
public class SubscriberMovingStrategy {

	@Inject private AccountRepository accountRepository;
	
	@Inject private IdentityProfileService identityProfileService;
	
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public void setIdentityProfileService(IdentityProfileService identityProfileService) {
		this.identityProfileService = identityProfileService;
	}
	
	public void move(Subscriber subscriber) {
		Account account = accountRepository.getAccount(subscriber.getAccountId());
		
		// TODO: ...
		
		identityProfileService.registerConsumerProfile(subscriber, account, IdentityProfileRegistrationOrigin.SUBSCRIBER_MOVE);
	}
}
