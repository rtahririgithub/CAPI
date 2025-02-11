/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.identity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Pavel Simonovsky
 *
 */

@ContextConfiguration(locations="classpath:/com/telus/cmb/framework/identity/test-context.xml")
public class ClientIdentityManagerCorpLdapTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ClientIdentityManager manager;
	
	@Test
	public void getClientIdentityByApplicationCode() throws Exception {
		//System.out.println(manager.getApplicationClientIdentity("TVC"));
		System.out.println(manager.getApplicationClientIdentity("SOA Application Identity\\WNP_WLI"));
	}

	@Test
	public void getUserClientIdentity() throws Exception {
		System.out.println(manager.getUserClientIdentity("T827890"));
//		System.out.println(manager.getUserClientIdentity("TVC"));
	}
	
}
