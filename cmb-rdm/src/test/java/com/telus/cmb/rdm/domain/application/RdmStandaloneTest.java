/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.rdm.domain.application;

import com.telus.rdm.application.ApplicationBootstrapper;
import com.telus.rdm.application.ApplicationRegistry;

/**
 * @author x113300
 *
 */
public class RdmStandaloneTest {


	public static void main(String[] args) {
		
		try {

			ApplicationBootstrapper.initialize();
			
//		Account account = new BusinessAccount();
//		ApplicationRegistry.accountApplicationService().createAccount(account);
			
			ApplicationRegistry.subscriberApplicationService().activateSubscriber("4033404108");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
