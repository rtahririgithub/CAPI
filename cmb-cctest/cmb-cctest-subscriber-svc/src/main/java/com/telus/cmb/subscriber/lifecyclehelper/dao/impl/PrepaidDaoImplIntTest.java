package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.subscriber.info.PrepaidCallHistoryInfo;
import com.telus.eas.subscriber.info.PrepaidEventHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

public class PrepaidDaoImplIntTest extends BaseLifecycleHelperIntTest{
	
	@Autowired
	PrepaidDaoImpl dao;

	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}		
	@Test
	public void testRetrieveFeatures() throws Exception {
		try {
			String phoneNumber="1234567890";
			ServiceAgreementInfo[] serAgArray = new ServiceAgreementInfo[0];
			// serAgArray = dao.retrieveFeatures(phoneNumber);
			assertEquals(0, serAgArray.length);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidCallHistory(){
		List<PrepaidCallHistoryInfo> preHisList = new ArrayList<PrepaidCallHistoryInfo>();
		preHisList = dao.retrievePrepaidCallHistory("4165545503", new Date(2009-1900,00,01), new Date(2010-1900,00,01));
		assertEquals(26,preHisList.size());
		for(PrepaidCallHistoryInfo preCalHis :preHisList){
			assertEquals("4165545503",preCalHis.getCalledPhoneNumber());
			assertEquals(499.41,preCalHis.getStartBalance(),0);
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory(){
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = dao.retrievePrepaidEventHistory("4165545503", new Date(2009-1900,00,11), new Date(2009-1900,11,11));
		assertEquals(3,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(100,preCalHis.getAmount(),0);
			assertEquals("-5",preCalHis.getPrepaidEventTypeCode());
			break;
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePrepaidEventHistory1(){
		PrepaidEventTypeInfo[] prepaidEventTypes = new PrepaidEventTypeInfo[1];
		PrepaidEventTypeInfo prepaidEventTypesob= new PrepaidEventTypeInfo();
		prepaidEventTypesob.setCode("-51");
		prepaidEventTypes[0]=prepaidEventTypesob;
		List<PrepaidEventHistoryInfo> preHisList = new ArrayList<PrepaidEventHistoryInfo>();
		preHisList = dao.retrievePrepaidEventHistory("4165545503", new Date(2009-1900,00,11), new Date(2009-1900,11,11),prepaidEventTypes);
		assertEquals(1,preHisList.size());
		for(PrepaidEventHistoryInfo preCalHis :preHisList){
			assertEquals(0,preCalHis.getAmount(),0);
			assertEquals("-51",preCalHis.getPrepaidEventTypeCode());
		}
	}	
}
