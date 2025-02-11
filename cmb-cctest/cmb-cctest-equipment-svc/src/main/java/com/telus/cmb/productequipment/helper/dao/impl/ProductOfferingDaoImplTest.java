package com.telus.cmb.productequipment.helper.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.helper.dao.ProductOfferingServiceDao;
import com.telus.eas.equipment.info.CardInfo;

@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(locations = {
		"classpath:application-context-dao-product-equipment-helper.xml",
		"classpath:application-context-datasources.xml",
		"classpath:application-context-wsclient-prepaid.xml"})

public class ProductOfferingDaoImplTest {

	@Autowired
	ProductOfferingServiceDao dao;

	@BeforeClass
	public static void beforeClass() {
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");//PT148
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");//PT168
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}
	
	@Test
	public void testGetAirCardByCardNo() throws ApplicationException {
		System.out.println("testGetAirCardByCardNo START");

		String fullCardNo = "309012324040000";

		CardInfo cardInfo = dao.getAirCardByCardNo(fullCardNo);
		System.out.println("cardInfo : " + cardInfo);

		System.out.println("testGetAirCardByCardNo END");
	}

	@Test
	public void testGetCardBySerialNo() throws ApplicationException {
		System.out.println("testGetCardBySerialNo START");

		String serialNo = "30901232404";

		CardInfo cardInfo = dao.getCardBySerialNo(serialNo);
		System.out.println("cardInfo : " + cardInfo);

		System.out.println("testGetCardBySerialNo END");
	}

	@Test
	public void test() throws ApplicationException {
		System.out.println(dao.test());
	}

}
