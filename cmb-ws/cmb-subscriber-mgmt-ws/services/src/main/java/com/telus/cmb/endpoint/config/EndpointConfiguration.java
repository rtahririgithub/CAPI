/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.telus.cmb.framework.config.ConfigurationSupport;
import com.telus.cmb.framework.identity.ClientIdentityManager;
import com.telus.cmb.framework.identity.ClientIdentityProvider;
import com.telus.cmb.framework.identity.TelusHeaderIdentityProvider;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;

/**
 * @author Pavel Simonovsky
 *
 */

@Configuration
@ImportResource("classpath:framework-application-context.xml")
public class EndpointConfiguration extends ConfigurationSupport {

	@Autowired
	private ClientIdentityManager identityManager;
	
	@Bean
	public ClientIdentityProvider clientIdentityProvider() {
		TelusHeaderIdentityProvider identityProvider = new TelusHeaderIdentityProvider();
		identityProvider.setIdentityManager(identityManager);
		return identityProvider; 
	}
	
	@Bean
	public Object subscriberLifecycleFacade() {
		return simpleRemoteStatelessSessionProxy("SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade", 
				SubscriberLifecycleFacade.class, getValue("services/SubscriberLifecycleFacade/url"), "ejb_user", "ejb_user");
	}
	
}