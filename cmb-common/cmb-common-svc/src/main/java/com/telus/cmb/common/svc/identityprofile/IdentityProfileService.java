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

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.eas.framework.info.TestPointResultInfo;


/**
 * @author Pavel Simonovsky
 *
 */
public interface IdentityProfileService {

	void registerConsumerProfile(Account account, IdentityProfileRegistrationOrigin origin) throws ApplicationException;
	
	void registerConsumerProfile(Subscriber subscriber, Account account, IdentityProfileRegistrationOrigin origin) throws ApplicationException;
	
	TestPointResultInfo testEnterpriseConsumerProfileRegistrationService();
	
}
