/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.shakedown.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Pavel Simonovsky
 *
 */
public class ConfigurationLdapReader {
	private static final Log logger = LogFactory.getLog(ConfigurationLdapReader.class);
	protected Hashtable<String, String> environment;
	
	public static void main(String[] args) throws Exception {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
//		String[] path = { "CMB", "services", "ApplicationMessageFacade"};
		String[] path = { "Telus-ECA", "ClientAPI"};
		ConfigurationLdapReader reader = new ConfigurationLdapReader();
		Map<String, Object> valueMap = reader.getProperties( path);
		
		Set<String> keys = valueMap.keySet();
		
		for (String key : keys) {
			logger.info(key + " : " + valueMap.get(key));
		}
		
	}
	
	public ConfigurationLdapReader() {
		this(System.getProperty("cmb.application.ldap.url") != null ? 
				System.getProperty("cmb.application.ldap.url") : System.getProperty("com.telusmobility.config.java.naming.provider.url"));
	}
	
	public ConfigurationLdapReader(String providerUrl) {
		environment = new Hashtable<String, String>();
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
	}

	public Object getValue(String [] path) {
		// retrieve attribute value
		
		DirContext context = null;
		final String ldapPath = createDnPath(path);
		try {
			context = new InitialDirContext(environment);
			Attributes attributes = context.getAttributes(ldapPath);
			return attributes.get("telusconfigstringvalue").get();
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving value for DN [" + ldapPath + "]: " + e.getMessage(), e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (Exception ce) {
				}
			}
		}
	}
	
	public Map<String, Object> getProperties(String [] path) {
		// retrieve attribute value
		
		DirContext context = null;
		Map<String, Object> propertiesMap = new HashMap<String, Object>();
		final String ldapPath = createDnPath(path);
		
		try {
			context = new InitialDirContext(environment);
			NamingEnumeration<NameClassPair> enumeration = context.list(ldapPath);
			String suffix = ","+ldapPath;
			String[] attrbiutes = {"telusconfigstringvalue"};
			while (enumeration.hasMore()) {
				NameClassPair ncPair = enumeration.next();
				try {
					Attributes attributes = context.getAttributes(ncPair.getName()+suffix, attrbiutes);
					Object value = attributes.get("telusconfigstringvalue").get();
					propertiesMap.put(ncPair.getName().substring(3), value);
				}catch (Exception e) {
					
				}
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving value for DN [" + ldapPath + "]: " + e.getMessage(), e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (Exception ce) {
				}
			}
		}
		
		return propertiesMap;
	}
	
	public String getStringValue(String [] path) {
		return (String) getValue(path);
	}
	
	public String getStringValue(String [] path, String defaultValue) {
	    String value = getStringValue(path);

	    if(value == null) {
	      value = defaultValue;
	    }

	    return value;
	  }
	
	public Boolean getBooleanValue(String[] path) {
		String value = getStringValue(path);
		return Boolean.parseBoolean(value);
	}
	
	public Boolean getBooleanValue(String[] path, boolean defaultValue) {
		String value = getStringValue(path);
		
		if (value == null) {
			return defaultValue;
		}
		
		return Boolean.parseBoolean(value);
	}
	
	public Date getDateValue(String[] path) {
		String value = getStringValue(path);
		return new Date(value);
	}
	
	public Date getDateValue(String[] path, Date defaultValue) {
		String value = getStringValue(path, null);
		
		if (value == null) {
			return defaultValue;
		}else {
			return new Date(value);
		}
	}
	
	public String[] getStringArrayValues(String[] path, String defaultValue) {
		if (defaultValue == null) {
			defaultValue = "";
		}

		String value = getStringValue(path, null);

		if (value == null) {
			value = defaultValue;
		}

		List<String> list = new ArrayList<String>(10);

		StringTokenizer t = new StringTokenizer(value);
		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}

		return list.toArray(new String[list.size()]);
	}

	public String[] getStringArrayValues(String[] path) {
		String value = getStringValue(path);

		List<String> list = new ArrayList<String>(10);

		StringTokenizer t = new StringTokenizer(value);
		while (t.hasMoreTokens()) {
			list.add(t.nextToken());
		}

		return list.toArray(new String[list.size()]);
	}
	
	protected static String createDnPath (String[] path) {
		StringBuffer buffer = new StringBuffer();
		for (int idx = path.length - 1; idx >= 0; idx--) {
			if (idx != path.length - 1) {
				buffer.append(", ");
			}
			buffer.append("cn=").append(path[idx]);
		}
		
		return buffer.toString();
	}
}
