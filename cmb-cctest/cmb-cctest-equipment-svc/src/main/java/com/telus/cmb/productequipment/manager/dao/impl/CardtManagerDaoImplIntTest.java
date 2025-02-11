package com.telus.cmb.productequipment.manager.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.manager.dao.CardManagerDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-manager.xml", 
		"classpath:application-context-datasources.xml"})

public class CardtManagerDaoImplIntTest {
	
	@Autowired
	CardManagerDao dao;
	
	@Test
	public void testSetCardStatus() throws ApplicationException {
		System.out.println("testSetCardStatus START");
//		Test 1
		String pSerialNo="20000010656";
		int pStatusId=103; 
		String pUser="Anitha111";
		int pBan=8285849;
		String pPhoneNumber="2507105183";
		String pEquipmentSerialNo="24700260123";
		boolean pAutoRenewInd=true;
		
//		Test 2
//		String pSerialNo="30901590085";
//		int pStatusId=109; 
//		String pUser="Ani";
//		int pBan=20001552;
//		String pPhoneNumber="4168946005";
//		String pEquipmentSerialNo="21101104748";
//		boolean pAutoRenewInd=true;
		
		
		dao.setCardStatus(pSerialNo, pStatusId, pUser, pBan, pPhoneNumber, pEquipmentSerialNo, pAutoRenewInd);
		System.out.println("testSetCardStatus END"); 
		
	}
	
	@Test
	public void testGetCardStatus() throws ApplicationException {
		System.out.println("testGetCardStatus START");
		
		String pSerialNo="1001";
		int currentStatusId= dao.getCardStatus(pSerialNo);
		System.out.println("currentStatusId : "+currentStatusId);
		assertEquals(100,currentStatusId);
    	System.out.println("testGetCardStatus END"); 
		
	}
	
	
	
	
	
	
	
}
