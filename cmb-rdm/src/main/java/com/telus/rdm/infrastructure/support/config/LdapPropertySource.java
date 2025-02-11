/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.config;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.MapPropertySource;

/**
 * @author x113300
 *
 */
public class LdapPropertySource extends MapPropertySource {

	private static final Logger logger = LoggerFactory.getLogger(LdapPropertySource.class);
	
	/**
	 * @param name
	 * @param source
	 */
	public LdapPropertySource(String name, String url, String ... nodeNames) {
		super(name, loadProperties(url, nodeNames));
	}
	
	private static Map<String, Object> loadProperties(String url, String ... nodeNames) {
		Map<String, Object> properties = new HashMap<String, Object>();

		InitialContext context = openEnironment(url);
		
		for (String nodeName : nodeNames) {
			visitNode(context, nodeName, "", properties);
		}
		
		closeContext(context);
		
		return properties;
	}
	
	private static InitialContext openEnironment(String url) {
		
		logger.debug("Opening initial context using url: {}", url);
		
		Hashtable<String, String> environment = new Hashtable<String, String>();
		
		environment.put(Context.PROVIDER_URL, url);
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

		try {

			return new InitialContext(environment);
			
		} catch (Exception e) {
			throw new RuntimeException("Error creating initial context: " + e.getMessage(), e);
		}
	}
	
	private static void closeContext(InitialContext context) {
		
		logger.debug("Closing initial context {}", context);
		
		try {
			context.close();
		} catch (Exception e) {
			logger.warn("Error closing initial context: {}", e.getMessage(), e);
		}
	}

	private static void visitNode1(InitialContext context, String name, String path, Map<String, Object> data) {

		logger.debug("Property Path {}", path);
		logger.debug("Visiting node {}", name);
		
		try {
			NamingEnumeration<NameClassPair> enumeration = context.list(name);
			while (enumeration.hasMoreElements()) {
				NameClassPair pair = enumeration.nextElement(); 
				
				logger.debug("NCP: {}", pair);
				
				//DirContext nodeContext = (DirContext) binding.getObject();
				
//				Attributes attributes = nodeContext.getAttributes("", new String[]{"telusconfigstringvalue"});
//				logger.debug("Attributes: {}", attributes);
				
//				String propertyPath = path + "/" + toSimpleName(binding);
//				if (attributes.size() == 0) {
					visitNode(context, pair.getName() + "," + name , "/", data);
//				} else {
//					logger.debug("Value");
//				}
			}
			
		} catch (Exception e) {
			logger.error("Error visiting node {}: {}", name, e.getMessage(), e);
		}
	}
	
	private static void visitNode(InitialContext context, String name, String path, Map<String, Object> data) {
		
		logger.debug("Property Path {}", path);
		logger.debug("Visiting node {}", name);
		
		try {
			NamingEnumeration<Binding> enumeration = context.listBindings(name);
			while (enumeration.hasMoreElements()) {
				Binding binding = enumeration.nextElement(); 
				
				logger.debug("NCP: {}", binding);
				
				//DirContext nodeContext = (DirContext) binding.getObject();
				
//				Attributes attributes = nodeContext.getAttributes("", new String[]{"telusconfigstringvalue"});
//				logger.debug("Attributes: {}", attributes);
				
//				String propertyPath = path + "/" + toSimpleName(binding);
//				if (attributes.size() == 0) {
					visitNode(context, binding.getName() + "," + name , "/", data);
//				} else {
//					logger.debug("Value");
//				}
			}
			
		} catch (Exception e) {
			logger.error("Error visiting node {}: {}", name, e.getMessage(), e);
		}
	}
	
	private static String toSimpleName(NameClassPair pair) {
		String name = pair.getName();
		return name.substring(name.indexOf('=') + 1).trim();
	}
}
