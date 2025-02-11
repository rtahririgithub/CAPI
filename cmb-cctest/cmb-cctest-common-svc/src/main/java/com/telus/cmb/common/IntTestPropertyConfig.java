package com.telus.cmb.common;

public class IntTestPropertyConfig {

	static void initFactory() {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}
	
	public static void pt168() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
	}

	public static void staging() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
	}
	
	public static void sit() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
	}

	public static void pt148() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	public static void dv103() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
	}
	
	public static void local() {
		initFactory();
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://localhost:7001");
	}	
}
