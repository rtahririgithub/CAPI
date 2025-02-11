/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.dao;

import org.junit.Test;

import com.telus.eas.utility.info.ServiceInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public class ReferenceDataWpsDaoTest {

	static {
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
	}
	
	static void pt168() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlpt168ecaeasinteca:7621");
	}

	static void staging() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlseaseca:11182");
	}
	
	static void sit() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wlj1easeca:8982");
	}

	static void pt148() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
	}
	
	static void dv103() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://wld3easeca:8382");
	}
	
	static void local() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-sit.tmi.telus.com:1489/cn=sit_81,o=telusconfiguration");
		System.setProperty("com.telus.provider.providerURL", "t3://localhost:7001");
	}
	
	/**
	 * Test method for {@link com.telus.cmb.reference.dao.ReferenceDataWpsDao#retrieveFeaturesList()}.
	 */
//	@Test
//	public void testRetrieveFeaturesList() {
//	
////		pt148();
////		pt168();
//		dv103();
//		
//		ReferenceDataWpsDao dao = new ReferenceDataWpsDao();
//		ServiceInfo [] services = dao.retrieveFeaturesList();
//		
//		System.out.println("Number of serices: [" + services.length + "]");
//		
//		for(ServiceInfo service : services) {
//			System.out.println(service);
//		}
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.dao.ReferenceDataWpsDao#retrieveFeature(int)}.
//	 */
//	@Test
//	public void testRetrieveFeature() {
//		dv103();
//		ReferenceDataWpsDao dao = new ReferenceDataWpsDao();
//		System.out.println(dao.retrieveFeature(310));
//
//	}

}
