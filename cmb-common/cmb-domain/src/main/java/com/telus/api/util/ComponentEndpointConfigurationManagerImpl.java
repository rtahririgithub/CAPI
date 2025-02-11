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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * @author Pavel Simonovsky
 *
 */
public class ComponentEndpointConfigurationManagerImpl implements ComponentEndpointConfigurationManager {
	
	private Map componentEndpointConfigurationsMap = new HashMap();;
	
	private String componentRootDn;
	
	private Set componentFilters = new HashSet();
	
	private String systemOverridePrefix;

	public ComponentEndpointConfigurationManagerImpl(String componentRootDn, String systemOverridePrefix) {
		this(componentRootDn, systemOverridePrefix, new String[0]);
	}

	public ComponentEndpointConfigurationManagerImpl(String componentRootDn, String systemOverridePrefix, String[] componentFilter) {
		this.componentRootDn = componentRootDn;
		this.systemOverridePrefix = systemOverridePrefix;
		
		for (int idx = 0; idx < componentFilter.length; idx++) {
			componentFilters.add(componentFilter[idx]);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.telus.api.util.IComponentEndpointConfigurationManager#getComponentEndpointConfigurations()
	 */
	public Map getComponentEndpointConfigurations() {
		if (componentEndpointConfigurationsMap.isEmpty()) {
			initialize();
		}
		return componentEndpointConfigurationsMap;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.api.util.IComponentEndpointConfigurationManager#getComponentEndpointConfiguration(java.lang.String)
	 */
	public ComponentEndpointConfiguration getComponentEndpointConfiguration(String name) {
		return (ComponentEndpointConfiguration) getComponentEndpointConfigurations().get(name);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.api.util.IComponentEndpointConfigurationManager#getComponentEndpointConfiguration(java.lang.Class)
	 */
	public ComponentEndpointConfiguration getComponentEndpointConfiguration(Class type) {
		String name = type.getName();
		name = name.substring(name.lastIndexOf('.') + 1);
		return (ComponentEndpointConfiguration) getComponentEndpointConfigurations().get(name);
	}
	
	private final static String PROP_NAME_USE_SECONDARY_URL = "cmb.ejb.useSecondaryUrl";  
	private final static String PROP_NAME_USE_BATCH_URL = "cmb.ejb.useBatchUrl";  
	private void initialize() throws RuntimeException {
		
		DirContext context = null;
		
		try {

			System.out.println("Loading component endpoint configurations:");
			
			//read the system property 
			boolean useSecondaryUrl = Boolean.getBoolean(PROP_NAME_USE_SECONDARY_URL);
			boolean useBatchUrl = Boolean.getBoolean(PROP_NAME_USE_BATCH_URL);

			System.out.println(PROP_NAME_USE_SECONDARY_URL+ ": " + useSecondaryUrl);
			System.out.println(PROP_NAME_USE_BATCH_URL+ ": " + useBatchUrl);
			
			String providerUrl = System.getProperty("com.telusmobility.config.java.naming.provider.url");

			Hashtable<String, String> environment = new Hashtable<String, String>();
			environment.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
			environment.put(Context.PROVIDER_URL, providerUrl);
			
			String authMechanism = System.getProperty("com.telusmobility.config.java.naming.security.authentication");

			if (authMechanism != null && authMechanism.length() > 0) {
				environment.put(Context.SECURITY_AUTHENTICATION, authMechanism);
			}
			String authPrincipal = System.getProperty("com.telusmobility.config.java.naming.security.principal");

			if (authPrincipal != null && authPrincipal.length() > 0) {
				environment.put(Context.SECURITY_PRINCIPAL, authPrincipal);
			}
			String authCredentials = System.getProperty("com.telusmobility.config.java.naming.security.credentials");

			if (authCredentials != null && authCredentials.length() > 0) {
				environment.put(Context.SECURITY_CREDENTIALS, authCredentials);
			}


			context = new InitialDirContext(environment);

			NamingEnumeration bindings = context.listBindings(componentRootDn);

			while (bindings.hasMoreElements()) {

				Binding component = (Binding) bindings.nextElement();
				String name = component.getName();
				name = name.substring(name.indexOf('=') + 1);
				
				if (!componentFilters.isEmpty() && !componentFilters.contains(name)) {
					continue;
				}

				if (component.getObject() instanceof DirContext) {

					DirContext ctx = (DirContext) component.getObject();

					ComponentEndpointConfiguration configuration = new ComponentEndpointConfiguration();

					configuration.setName(name);
					configuration.setUrl(getNodeStringValue(ctx, name, "url", ""));
					
					//Override the URL ( mainly for SmartDesktop to point all EJBs to a dedicated CMB cluster )  
					//only when and system property is set to use secondary URL and secondaryUrl contain value. 
					if ( useSecondaryUrl ) {
						String secondaryUrl = getNodeStringValue(ctx, name, "secondaryUrl", null);
						if (secondaryUrl!=null && secondaryUrl.trim().length()>0) {
							configuration.setUrl( secondaryUrl );
						}
					} else if ( useBatchUrl ) { // If setting both, useSecondaryUrl take advantage 
						String batchUrl = getNodeStringValue(ctx, name, "batchUrl", null);
						if (batchUrl!=null && batchUrl.trim().length()>0) {
							configuration.setUrl( batchUrl );
						}
					}
					configuration.setUsedByProvider(getNodeBooleanValue(ctx, name, "usedByProvider", "true"));
					configuration.setUsedByWebServices(getNodeBooleanValue(ctx, name, "usedByWS", "true"));

					componentEndpointConfigurationsMap.put(configuration.getName(), configuration);

					System.out.println(configuration);
				}					
			}
		} catch (Throwable t) {
			String message = "Error reading endpoint configurations: " + t.getMessage();
			System.err.println(message);
			t.printStackTrace();
			throw new RuntimeException(message, t);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (Exception e) {
					System.err.println("Error closing naming context: " + e.getMessage());
				}
			}
		}
	}
	
	private String getNodeStringValue(DirContext context, String componentName, String nodeName, String defaultValue) {
		
		String propertyName = systemOverridePrefix + "." + componentName + "." + nodeName;
		
		String value = System.getProperty(propertyName);
		
		if (value != null) {
			return value;
		}
		
		try {
			Attributes attrs = context.getAttributes("cn=" + nodeName);
			return attrs.get("telusconfigstringvalue").get().toString();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	private boolean getNodeBooleanValue(DirContext context, String componentName, String nodeName, String defaultValue) {
		String value = getNodeStringValue(context, componentName, nodeName, defaultValue);
		return Boolean.valueOf(value).booleanValue();
	}
	
}
