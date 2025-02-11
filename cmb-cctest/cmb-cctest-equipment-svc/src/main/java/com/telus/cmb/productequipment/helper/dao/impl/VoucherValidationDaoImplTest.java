package com.telus.cmb.productequipment.helper.dao.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.helper.dao.VoucherValidationServiceDao;
import com.telus.eas.equipment.info.CardInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:application-context-dao-product-equipment-helper.xml",
		"classpath:application-context-datasources.xml",
		"classpath:application-context-wsclient-prepaid.xml"})
public class VoucherValidationDaoImplTest {

	@Autowired
	VoucherValidationServiceDao dao;

	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");//PT148
		//System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");//PT168
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}
	
	@Test
	public void validateCardPIN() throws ApplicationException {
		System.out.println("testGetCardInfobySerialNo START");

		String fullCardNo = "309012324040000";
		String serialNo = "30901232404";
		String cypherPIN = "D5FA62FE06251394";
		String userId = "12459";
		String equipmentSerialNo = "21101120760";
		String phoneNumber = "4162060215";
		CardInfo cardInfo = new CardInfo();

		dao.validateCardPIN(serialNo, fullCardNo, cypherPIN, userId,
				equipmentSerialNo, phoneNumber, cardInfo);

		System.out.println("PIN: " + cardInfo.getPIN());

		System.out.println("testGetCardInfobySerialNo END");
	}

	@Test
	public void test() throws ApplicationException {
		System.out.println(dao.test());
	}

}
