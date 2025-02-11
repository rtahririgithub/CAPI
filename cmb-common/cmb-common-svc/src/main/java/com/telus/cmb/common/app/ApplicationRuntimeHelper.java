/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Pavel Simonovsky
 *
 */
public class ApplicationRuntimeHelper {
	
	private static final Log logger = LogFactory.getLog(ApplicationRuntimeHelper.class);

	private String jmxPrincipal;
	
	private String jmxCredential;
	
	private String applicationName;
	
	private String domainName;
	
	private String nodeName;
	
	private String serverVersion;
	
	private List<ObjectName> registeredObjectNames = new ArrayList<ObjectName>();
	
	public ApplicationRuntimeHelper(String applicationName, String jmxPrincipal, String jmxCredential) {
		this.applicationName = applicationName;
		this.jmxPrincipal = jmxPrincipal;
		this.jmxCredential = jmxCredential;
		
		MBeanServer server = getMBeanServer();

		if (server != null) {
			try {

				ObjectName runtimeServiceName = new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
				nodeName = (String) server.getAttribute(runtimeServiceName, "ServerName");

				ObjectName domainRuntime = (ObjectName) server.getAttribute(runtimeServiceName, "DomainConfiguration");
				domainName = (String) server.getAttribute(domainRuntime, "Name");
				serverVersion = (String) server.getAttribute(domainRuntime, "ConfigurationVersion");

			} catch (Exception e) {
				logger.error("Error accessing MBean server: " + e.getMessage(), e);
			}
		}
	}
	
	private MBeanServer getMBeanServer() {
		Context context = getInitialContext();
		
		MBeanServer server = null;

		if (context != null) {
			server = getMBeanServer("java:comp/env/jmx/runtime", context);
			if (server == null) {
				server = getMBeanServer("java:comp/jmx/runtime", context);
			}
			close(context);
		}
		return server;
	}
	
	private MBeanServer getMBeanServer(String jndiName, Context context) {
		try {
			return (MBeanServer) context.lookup(jndiName); 
		} catch (Exception e) {
			logger.warn("unable to locate MBean server using jndiName [" + jndiName + "]: " + e.getMessage());
		}
		return null;
	}
	
	private Context getInitialContext() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		
		if (jmxPrincipal != null) {
			env.put(Context.SECURITY_PRINCIPAL, jmxPrincipal);
		}
		if (jmxCredential != null) {
			env.put(Context.SECURITY_CREDENTIALS, jmxCredential);
		}
		
		try {

			return new InitialContext(env);
			
		} catch (Throwable t) {
			logger.warn("Unable to obtain initial context using principal [" + (jmxPrincipal == null ? "anonymous" : jmxPrincipal) + "]: " + t.getMessage());
		}
		return null;
	}	

	private void close(Context context) {
		try {
			if (context != null) {
				context.close();
			}
		} catch (Exception e) {
			logger.error("Error closing naming context: " + e.getMessage(), e);
		}
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return the serverVersion
	 */
	public String getServerVersion() {
		return serverVersion;
	}
	
	public void registerMBean(StandardMBean bean, String name) {
		try {
			MBeanServer server = getMBeanServer();
			if (server != null) {
				ObjectName objectName = new ObjectName(name);

				if (server.isRegistered(objectName)) {
					server.unregisterMBean(objectName);
				}
				
				server.registerMBean(bean, objectName);
				registeredObjectNames.add(objectName);
			}
		} catch (Exception e) {
			logger.error("Error registering MBean [" + bean + "] under name [" + name + "]: " + e.getMessage(), e);
		}
	}
	
	public void unregisterAllMBeans() {
		MBeanServer server = getMBeanServer();
		if (server != null) {
			for (ObjectName objectName : registeredObjectNames) {
				try {
					logger.debug("Unregistering MBean for name [" + objectName + "].");
					server.unregisterMBean(objectName);
				} catch (Exception e) {
					logger.warn("Unable to unregister mbean for name [" + objectName + "]: " + e.getMessage());
				}
			}
		}
	}
}
