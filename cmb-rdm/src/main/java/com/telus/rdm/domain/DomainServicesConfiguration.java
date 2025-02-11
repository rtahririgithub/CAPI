/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.telus.rdm.domain.identityprofile.ConfigurableProfileRegistrationPolicy;
import com.telus.rdm.domain.identityprofile.IdentityProfileRegistrationPolicy;

/**
 * @author x113300
 *
 */

@Configuration
@PropertySource("classpath:domain.properties")
public class DomainServicesConfiguration {

	@Inject private Environment environment;
	
	@Bean public IdentityProfileRegistrationPolicy identityProfileRegistrationPolicy() {
		return new ConfigurableProfileRegistrationPolicy(environment.getProperty("identityprofile/eligibility-policy"));
	}
}
