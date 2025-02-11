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

import java.util.Date;

import com.telus.api.ApplicationException;

/**
 * @author Pavel Simonovsky
 *
 */
public interface EnterpriseConsumerProfileRegistrationServiceAdapter {

	void createAccountProfile(int accountId, int brandId, char accountType, char accountSubType, 
			String firstName, String lastName, String emailAddress, String language, Date activationDate, Date creationDate) throws ApplicationException;
	
	void createSubscriberProfile(int accountId, int brandId, char accountType, char accountSubType, 
			String firstName, String lastName, String emailAddress,	String language, String phoneNumber) throws ApplicationException;
	
	 String ping() throws ApplicationException ;

	
	
}
