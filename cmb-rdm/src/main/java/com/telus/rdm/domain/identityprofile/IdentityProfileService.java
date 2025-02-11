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

import com.telus.rdm.domain.account.Account;
import com.telus.rdm.domain.subscriber.Subscriber;

/**
 * @author x113300
 *
 */
public interface IdentityProfileService {

	void registerConsumerProfile(Account account, IdentityProfileRegistrationOrigin origin);
	
	void registerConsumerProfile(Subscriber subscriber, Account account, IdentityProfileRegistrationOrigin origin);
	
}
