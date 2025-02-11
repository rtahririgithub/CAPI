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

import java.util.Properties;

import javax.naming.Context;

import org.springframework.ejb.access.SimpleRemoteStatelessSessionProxyFactoryBean;

/**
 * @author Pavel Simonovsky
 *
 */
public class ConfigurationSupport {

	protected String getValue(String key) {
		return ConfigurationManagerFactory.getInstance().getStringValue(key);
	}

	protected Object simpleRemoteStatelessSessionProxy(String jndiName, Class<?> businessInterface, String providerUrl, String principal, String credentials) {

		SimpleRemoteStatelessSessionProxyFactoryBean proxyFactory = new SimpleRemoteStatelessSessionProxyFactoryBean();
		proxyFactory.setBusinessInterface(businessInterface);
		proxyFactory.setJndiName(jndiName);
		proxyFactory.setLookupHomeOnStartup(false);
		
		Properties environment = new Properties();
		environment.setProperty(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		environment.setProperty(Context.PROVIDER_URL, providerUrl);
		environment.setProperty(Context.SECURITY_PRINCIPAL, principal);
		environment.setProperty(Context.SECURITY_CREDENTIALS, credentials);
		
		proxyFactory.setJndiEnvironment(environment);
		
		return proxyFactory;
	}	
}
