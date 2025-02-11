/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.identity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * @author Pavel Simonovsky
 *
 */
public class ClientIdentityManagerTest {

	static {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
	}
	
	public static void pt168() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
	}

	public static void pt148() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	
	@Test
	public void testGetClientIdentityByApplicationCode() throws Exception {
		
		pt148();
		
		ClientIdentityManager identityManager = new ClientIdentityManagerLdapImpl();
		
		System.out.println("1 -> " + identityManager.getClientIdentity("AAA\\TELUS_WEBSTORE"));
		System.out.println("2 -> " + identityManager.getClientIdentity("TELUS_WEBSTORE"));
		System.out.println("3 -> " + identityManager.getClientIdentity("TELUS_WEBSTORE"));

		System.out.println("4 -> " + identityManager.getClientIdentity("TVC"));
		System.out.println("5 -> " + identityManager.getClientIdentity("TVC"));
		
	}
	
	@Test
	public void testWNP_WLIClientIdentity() throws Exception {
		pt148();
		ClientIdentityManager identityManager = new ClientIdentityManagerLdapImpl();
		String applicationCode = "SOA Application Identity\\WNP_WLI";
		ClientIdentity identity = identityManager.getClientIdentity(applicationCode);
		assertEquals("911231", identity.getPrincipal());
		assertEquals("test", identity.getCredential());
		assertEquals("WNP_WLI", identity.getApplication());
	}
}
