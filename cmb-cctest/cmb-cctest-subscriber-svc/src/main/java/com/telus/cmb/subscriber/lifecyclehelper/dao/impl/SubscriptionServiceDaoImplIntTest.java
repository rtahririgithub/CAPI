package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.reference.FundSource;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

/*
 *  JUnit test for Prepaid Service.
 *  
 *  At @ContextConfiguration 
 *  Optionally remove "application-context-wsclient-prepaid-test.xml".
 *  This file contains endpoints and other information that is now stored
 *  under common-svc. It is no longer needed. 
 *  
 *  Modify ldap url and datasources depending on test environment.
 *  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-lifecyclehelper.xml",
		"classpath:application-context-datasources-lifecyclehelper-testing-pt168.xml",
		"classpath:application-context-wsclient-prepaid-test.xml"})
public class SubscriptionServiceDaoImplIntTest {
	
	@Autowired
	SubscriptionServiceDaoImpl dao;
	
	@BeforeClass
	public static void beforeClass() {
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		
	
	//4161742650
	//defect - 4161644474
	//defect#2 - 7782054244
	//defect#3 - 4160605118
	@Test
	public void testRetreiveFeatureForPrepaidSubscriber(){
		String phoneNumber="4160605118";

		try {
			 System.out.println("Calling");
			 ServiceAgreementInfo[] serviceAgreementInfos = dao.retrieveFeatures(phoneNumber);
			 System.out.println("Num of features:" + serviceAgreementInfos.length);
			 for(ServiceAgreementInfo o: serviceAgreementInfos){
				 System.out.println("Feature:");
				 System.out.println("" + o.toString());
			 }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	

}
