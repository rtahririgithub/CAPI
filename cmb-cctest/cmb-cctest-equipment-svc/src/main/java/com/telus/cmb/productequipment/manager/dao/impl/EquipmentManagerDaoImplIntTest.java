package com.telus.cmb.productequipment.manager.dao.impl;

import java.rmi.RemoteException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.manager.dao.EquipmentManagerDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-manager.xml", 
		"classpath:application-context-datasources.xml"})

public class EquipmentManagerDaoImplIntTest {
	
	@Autowired
	EquipmentManagerDao dao;
	
	@Test
	public void testGetMasterLockbySerialNo() throws ApplicationException {
		System.out.println("testGetMasterLockbySerialNo START");

		String pSerialNo="21101578784";
		String pUserID="WIERBIMA";
		long pLockReasonID=6;
		
		String masterLockNo=dao.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID);
		System.out.println("masterLockNo :"+masterLockNo);
		
		System.out.println("testGetMasterLockbySerialNo END"); 
		
	}
	
	
	@Test
	public void testGetMasterLockbySerialNo1() throws ApplicationException {
		System.out.println("testGetMasterLockbySerialNo1 START");

		String pSerialNo="21101578784";
		String pUserID="WIERBIMA";
		long pLockReasonID=6;
		long pOutletID=0;
		long pChnlOrgID=0;
		
		String masterLockNo=dao.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID, pOutletID, pChnlOrgID);
		System.out.println("masterLockNo :"+masterLockNo);
		
		System.out.println("testGetMasterLockbySerialNo1 END"); 
		
	}
	
	@Test
	public void testInsertPagerEquipment() throws ApplicationException {
		System.out.println("testInsertPagerEquipment START");

		String pSerialNo="510H61072";
		String pCapCode="E1593071";
		String pEncoderFormat="P";
		String pFrequencyCode="90";
		String pModelType="T";
		String pUserID="Anitha";
		
		dao.insertPagerEquipment(pSerialNo, pCapCode, pEncoderFormat, pFrequencyCode, pModelType, pUserID);
		
		
		System.out.println("testInsertPagerEquipment END"); 
		
	}
	
	
	@Test
	public void testUpdateStatus() throws ApplicationException {
		System.out.println("testUpdateStatus START");

		String pSerialNo="270113178105834";
		String pUserID="telus";
		long pEquipmentStatusTypeID=3;
		long pEquipmentStatusID=11;
		String pTechType="PCS";
		long pProductClassID=10000100;
		
		dao.updateStatus(pSerialNo, pUserID, pEquipmentStatusTypeID, pEquipmentStatusID, pTechType, pProductClassID);
		
		System.out.println("testUpdateStatus END"); 
		
	}
	
	
	@Test
	public void testSetSIMMule() throws ApplicationException {
		System.out.println("testSetSIMMule START");
		
		String sim="000810232929310";
		String mule="";
		Date activationDate=null;
		String eventType="RESVD";//"ACT"
		
		dao.setSIMMule(sim, mule, activationDate, eventType);
		
    	System.out.println("testSetSIMMule END"); 
		
	}
		
	
	
	
	
	
}
