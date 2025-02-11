/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * @author Pavel Simonovsky
 *
 */
@ContextConfiguration(locations="classpath:test-context.xml")
public class PlaceholderConfigurerTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	}
	
	@Autowired
	private ApplicationContext context;

	
	@Test
	public void testPlaceholderConfigurer() {
		Map map = (Map) context.getBean("testMap");
		System.out.println(map);
	}
}
