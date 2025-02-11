/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.config;

import org.testng.annotations.Test;

/**
 * @author Pavel Simonovsky
 *
 */

public class LdapPropertySourceTest {

	@Test
	public void test_config_loading() throws Exception {
		LdapPropertySource source = new LdapPropertySource("ldapPropertySource", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration", "cn=cmb-ns");
		System.out.println(source);
	}
}
