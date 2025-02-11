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

import com.telus.rdm.domain.account.AccountType;

/**
 * @author x113300
 *
 */
public interface IdentityProfileRegistrationPolicy {

	boolean isEligible(AccountType accountType);
	
}
