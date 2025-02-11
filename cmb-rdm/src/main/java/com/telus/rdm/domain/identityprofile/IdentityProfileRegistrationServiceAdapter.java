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

import java.util.Date;

import com.telus.rdm.domain.account.AccountType;
import com.telus.rdm.domain.shared.Brand;
import com.telus.rdm.domain.shared.Language;

/**
 * @author x113300
 *
 */
public interface IdentityProfileRegistrationServiceAdapter {

	void createAccountProfile(int accountId, Brand brand, AccountType type, String firstName, String lastName, String emailAddress,
			Language language, Date activationDate, Date creationDate);
	
	void createSubscriberProfile(int accountId, Brand brand, AccountType type, String firstName, String lastName, String emailAddress,
			Language language, String phoneNumber);
	
}
