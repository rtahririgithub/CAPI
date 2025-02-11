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

import javax.inject.Inject;

import com.telus.rdm.annotations.DomainService;
import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.domain.shared.Language;
import com.telus.rdm.domain.subscriber.Subscriber;

/**
 * @author x113300
 *
 */

@DomainService
public class IdentityProfileServiceImpl implements IdentityProfileService {

	@Inject private IdentityProfileRegistrationPolicy registrationPolicy; 
	
	@Inject private IdentityProfileRegistrationServiceAdapter registrationServiceAdapter;
	
	public void setRegistrationPolicy(IdentityProfileRegistrationPolicy registrationPolicy) {
		this.registrationPolicy = registrationPolicy;
	}
	
	public void setRegistrationServiceAdapter(IdentityProfileRegistrationServiceAdapter registrationServiceAdapter) {
		this.registrationServiceAdapter = registrationServiceAdapter;
	}
	
	@Override
	public void registerConsumerProfile(Account account, IdentityProfileRegistrationOrigin origin) {
		if (registrationPolicy.isEligible(account.getType())) {
			
			ConsumerName contactName = account.getContactName();
			Customer customer = account.getCustomer();
			
			registrationServiceAdapter.createAccountProfile(account.getAccountId(), account.getBrand(), account.getType(), 
					contactName.getFirstName(), contactName.getLastName(), customer.getEmailAddress(), Language.ENGLISH, null, null);
		}
	}

	@Override
	public void registerConsumerProfile(Subscriber subscriber, Account account, IdentityProfileRegistrationOrigin origin) {
		if (registrationPolicy.isEligible(account.getType())) {

			ConsumerName contactName = subscriber.getContactName();
			Customer customer = subscriber.getCustomer();
			
			registrationServiceAdapter.createSubscriberProfile(account.getAccountId(), account.getBrand(), account.getType(), 
					contactName.getFirstName(), contactName.getLastName(), customer.getEmailAddress(), Language.ENGLISH, subscriber.getSubscriberId());
		}
	}
}
