/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import com.telus.rdm.application.ApplicationRegistry;
import com.telus.rdm.domain.DomainRegistry;

/**
 * @author x113300
 *
 */

@Configuration
@ComponentScan("com.telus.rdm")
@PropertySource("environment.properties")
public class ApplicationConfiguration {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Inject private ConfigurableEnvironment environment;
	
	@PostConstruct
	public void init() {
		System.out.println(environment.getProperty("foo"));
		ApplicationRegistry.setApplicationContext(applicationContext);
		DomainRegistry.setApplicationContext(applicationContext);
	}
}
