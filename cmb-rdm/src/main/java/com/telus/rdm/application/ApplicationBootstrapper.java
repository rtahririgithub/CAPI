/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.application;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.telus.rdm.infrastructure.config.ApplicationConfiguration;
import com.telus.rdm.infrastructure.support.config.LdapPropertySource;

/**
 * @author x113300
 *
 */
public class ApplicationBootstrapper {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationBootstrapper.class);

	public static void initialize() throws Exception {
		
		final Properties props = PropertiesLoaderUtils.loadAllProperties("environment.properties");
		
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class) {
			
			protected void initPropertySources() {
				
				String jndiUrl = props.getProperty("jndi.url");
				logger.debug("Using [" + jndiUrl + "] to resolve ldap properties");
				getEnvironment().getPropertySources().addFirst( new LdapPropertySource("ldapPropertySource", jndiUrl, "cmb"));
			};
		};
		
		logger.info("Application context initalized successfully: {}", context);
	}
}
