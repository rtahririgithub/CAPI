package com.telus.cmb.productequipment.helper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.helper.dao.CardHelperDao;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.utility.info.ServiceInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-helper.xml", 
		"classpath:application-context-datasources.xml"})

public class CardtHelperDaoImplIntTest {
	
	@Autowired
	CardHelperDao dao;
	
	@Test
	public void testCheckPINAttemps() throws ApplicationException {
		System.out.println("testCheckPINAttemps START");
		
		String pSerialNo="30901232404";
		int count= dao.checkPINAttemps(pSerialNo);
		System.out.println("count : "+count);
		assertEquals(0,count);
    	System.out.println("testCheckPINAttemps END"); 
		
	}
	
	
	@Test
	public void testGetCardInfobySerialNo() throws ApplicationException{
		System.out.println("testGetCardInfobySerialNo START");
		
		String pSerialNo="30901232404";
		CardInfo cardInfo= dao.getCardInfobySerialNo(pSerialNo);
		System.out.println("cardInfo : "+cardInfo);
		
		assertEquals("4162060215",cardInfo.getPhoneNumber());
    	System.out.println("testGetCardInfobySerialNo END"); 
		
	}
	
	@Test
	public void testGetCardServices() throws ApplicationException{
		System.out.println("testGetCardServices START");
		
		String pSerialNo="30903565506";
		String pTechType="1RTT";
		String pBillType="POSTPAID";
		ServiceInfo[] serviceInfo= dao.getCardServices(pSerialNo, pTechType, pBillType);
		for(ServiceInfo info:serviceInfo)
		System.out.println("serviceInfo : "+info);
		
    	System.out.println("testGetCardServices END"); 
		
	}
	
	@Test
	public void testGetCards() throws ApplicationException{
		System.out.println("testGetCards START");
		
		String pPhoneNo="18888888888";
		String pCardType="FEA";
		CardInfo[] cardInfo= dao.getCards(pPhoneNo, pCardType);
		for(CardInfo info:cardInfo)
			System.out.println("cardInfo : "+info);
		
    	System.out.println("testGetCards END"); 
		
	}
	
	@Test
	public void testGetCypherPIN() throws ApplicationException {
		System.out.println("testGetCypherPIN START");
		
		String pSerialNo="30901232404";
		String pin= dao.getCypherPIN(pSerialNo);
		System.out.println("pin : "+pin);
		
		assertEquals("D5FA62FE06251394",pin);
    	System.out.println("testGetCypherPIN END"); 
		
	}
	
	
	
	
	
	
}
