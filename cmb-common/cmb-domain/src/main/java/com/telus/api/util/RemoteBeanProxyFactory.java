/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author Pavel Simonovsky
 *
 */
public class RemoteBeanProxyFactory {
	
	public static Object createProxy(Class businessInterface, String jndiName, String providerURL) {
		return createProxy(businessInterface, jndiName, providerURL, "", "");
	}

	public static Object createProxy(Class businessInterface, String jndiName, String providerURL, String principal, String credential) {
		return createProxy(businessInterface, jndiName, providerURL, principal, credential, null, null);
	}

	public static Object createProxy(Class businessInterface, String jndiName, String providerURL, String principal, String credential, Class[] constructorTypes, Object[] constructorArguments) {
		ClassLoader loader = businessInterface.getClassLoader();
		Class [] interfaces = new Class [] { businessInterface };
		InvocationHandler handler = new RemoteBeanProxy(jndiName, providerURL, principal, credential, constructorTypes, constructorArguments);
		return Proxy.newProxyInstance(loader, interfaces, handler);
	}

	public static Object createProxy(Class businessInterface, String jndiName, String providerURL, String principal, String credential, 
			String sessionPrincipal, String sessionCredentials, String sessionApplicationCode) {
		return createProxy(businessInterface, jndiName, providerURL, principal, credential, null, null, sessionPrincipal, sessionCredentials, sessionApplicationCode);
	}
	
	public static Object createProxy(Class businessInterface, String jndiName, String providerURL, String principal, String credential, Class[] constructorTypes, Object[] constructorArguments, 
			String sessionPrincipal, String sessionCredentials, String sessionApplicationCode) {
		ClassLoader loader = businessInterface.getClassLoader();
		Class [] interfaces = new Class [] { businessInterface };
		InvocationHandler handler = new RemoteBeanProxy(jndiName, providerURL, principal, credential, constructorTypes, constructorArguments, sessionPrincipal, sessionCredentials, sessionApplicationCode);
		return Proxy.newProxyInstance(loader, interfaces, handler);
	}
	
	public static void initialize(String providerUrl) throws Exception {
		
		Hashtable environment = new Hashtable();
		
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		environment.put(Context.SECURITY_PRINCIPAL, "");
		environment.put(Context.SECURITY_CREDENTIALS, "");
		environment.put(Context.PROVIDER_URL, providerUrl);

		Context context = null;
		
		try {
			context = new InitialContext(environment);
			
		} finally {
			if (context != null) {
				context.close();
			}
		}			
	}

}
