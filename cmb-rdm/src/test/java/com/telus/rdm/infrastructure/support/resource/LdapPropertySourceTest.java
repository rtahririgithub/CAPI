/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.resource;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.testng.annotations.Test;

import com.telus.rdm.infrastructure.support.config.LdapPropertySource;

/**
 * @author x113300
 *
 */
public class LdapPropertySourceTest {

//	private PropertySource<?> propertySource;
	
	
	@Test
	public void test_source_loading() throws Exception {
		
		ConfigurableEnvironment environment = new StandardEnvironment();
		MutablePropertySources sources = environment.getPropertySources();
		
		sources.addLast( new ResourcePropertySource("classpath:environment.properties"));

		LdapPropertySource propertySource = new LdapPropertySource("ldapPropertySource", environment.getProperty("jndi.url"), "cmb");
		sources.addFirst(propertySource);
		
		System.out.println(environment.getProperty("jndi.url"));
		System.out.println(environment.getProperty("foo"));
	}
	

}
