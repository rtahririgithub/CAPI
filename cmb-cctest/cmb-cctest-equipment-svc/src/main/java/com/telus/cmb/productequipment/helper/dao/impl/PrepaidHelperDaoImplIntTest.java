package com.telus.cmb.productequipment.helper.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-helper.xml", 
		"classpath:application-context-datasources.xml"})

public class PrepaidHelperDaoImplIntTest {
//	
//	@Autowired
//	PrepaidHelperDao dao;
//	
//	@Test
//	public void testGetAirCardByCardNo() throws ApplicationException {
//		System.out.println("testGetAirCardByCardNo START");
//		
//		String fullCardNo="309012324040000";
//		String phoneNumber="4162060215";
//		String equipmentSerialNo="21101120760";
//		String userId="12459";
//		
//		CardInfo cardInfo= dao.getAirCardByCardNo(fullCardNo, phoneNumber, equipmentSerialNo, userId);
//		System.out.println("cardInfo : "+cardInfo);
//		
//    	System.out.println("testGetAirCardByCardNo END"); 
//		
//	}
//	
//	@Test
//	public void testGetCardBySerialNo() throws ApplicationException {
//		System.out.println("testGetCardBySerialNo START");
//		
//		String serialNo="30901232404";
//		
//		CardInfo cardInfo= dao.getCardBySerialNo(serialNo);
//		System.out.println("cardInfo : "+cardInfo);
//		
//    	System.out.println("testGetCardBySerialNo END"); 
//		
//	}
//	
//	@Test
//	public void testInsertCardPINAccessAttempt() throws ApplicationException{
//		System.out.println("testGetCardInfobySerialNo START");
//		
//		String pSerialNo="30901232404";
//		String phoneNumber="4162060215";
//		String equipmentSerialNo="21101120760";
//		String user="12459";
//		String pinStatusCode="FAIL";
//		
//		dao.insertCardPINAccessAttempt(pSerialNo, user, equipmentSerialNo, pinStatusCode, phoneNumber);
//		
//    	System.out.println("testGetCardInfobySerialNo END"); 
//		
//	}
//	
//	
//	@Test
//	public void validateCardPIN() throws ApplicationException {
//		System.out.println("testGetCardInfobySerialNo START");
//		
//		String fullCardNo="309012324040000";
//		String serialNo="30901232404";
//		String cypherPIN="D5FA62FE06251394";
//		String userId="12459";
//		String equipmentSerialNo="21101120760";
//		String phoneNumber="4162060215";
//		CardInfo cardInfo=new CardInfo();
//
//		dao.validateCardPIN(serialNo, fullCardNo, cypherPIN, userId, equipmentSerialNo, phoneNumber, cardInfo);
//		
//		System.out.println("PIN: "+cardInfo.getPIN());
//		
//    	System.out.println("testGetCardInfobySerialNo END"); 
//		
//	}
//	
//	
}
