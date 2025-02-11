package com.telus.cmb.productequipment.helper.dao.impl;

import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.helper.dao.EquipmentHelperDao;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-helper.xml", 
		"classpath:application-context-datasources.xml"})

public class EquipmentHelperDaoImplIntTest {
	
	@Autowired
	EquipmentHelperDao dao;
	
	
	@Test
	public void testIsValidESNPrefix() throws ApplicationException {
		System.out.println("testIsValidESNPrefix START");
		
            
    		boolean validESNPrefix = dao.isValidESNPrefix("21101112944");	

    		System.out.println("isValidESNPrefix = " + validESNPrefix);
			System.out.println("testIsValidESNPrefix END"); 
		
	}
	
	
	@Test
	public void testGetIMSIsBySerialNumber() throws ApplicationException {
		System.out.println("testGetIMSIsBySerialNumber START");
		
		String[] IMSIs = dao.getIMSIsBySerialNumber("300000000100223");	
		for (String imsi : IMSIs) {
        	System.out.println("imsi = " + imsi);
	   }
		System.out.println("testGetIMSIsBySerialNumber END"); 
		
	}
	@Test
	public void testIsVirtualESN() throws ApplicationException {
		System.out.println("testIsVirtualESN START");
		
		boolean virtualESN = dao.isVirtualESN("21101112944");	

    		System.out.println("isVirtualESN = " + virtualESN);
			System.out.println("testIsVirtualESN END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobyCapCode() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyCapCode START");
		
		String pCapCode="E1614803";
		String pEncodingFormat="F";
		EquipmentInfo equip = dao.getEquipmentInfobyCapCode(pCapCode, pEncodingFormat);
    	System.out.println("EquipmentInfo = " + equip);
		
    	System.out.println("testGetEquipmentInfobyCapCode END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobyProductCode() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyProductCode START");
		
		String productCode="*252C";
		EquipmentInfo equip = dao.getEquipmentInfobyProductCode(productCode);
		System.out.println("EquipmentInfo = " + equip);
		
    	System.out.println("testGetEquipmentInfobyProductCode END"); 
		
	}
	
	
	
	@Test
	public void testGetEquipmentList() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentList START");
		//Test 1
		String pTechTypeClass="PCS";
		//Test 2
//		String pTechTypeClass="MIKE";
		//Test 3
//		String pTechTypeClass="ANALOG";
		//Test 4
//		String pTechTypeClass="SIM";
		
		//Test 5
		boolean inUse=true;
		
		String startSerialNo="10000000000";
		int n=50;
//		boolean inUse=false;
		String[] equList = dao.getEquipmentList(pTechTypeClass, n, startSerialNo);
		for (String el : equList) {
			System.out.println(el);
		}
    	System.out.println("testGetEquipmentList END"); 
		
	}
	
	@Test
	public void testGetEquipmentModes() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentModes START");
		
		String pProductCode="RAZRV3CAMP";
		EquipmentModeInfo[] equModes = dao.getEquipmentModes(pProductCode);
		for (EquipmentModeInfo el : equModes) {
			System.out.println("EquipmentModeInfo : "+el);
		}
    	System.out.println("testGetEquipmentModes END"); 
		
	}
	
	@Test
	public void testGetIDENShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetIDENShippedToLocation START");
		
		String serialNumber="000100000309070";
		int locationType=2;
		long  shippedLocation= dao.getIDENShippedToLocation(serialNumber, locationType);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetIDENShippedToLocation END"); 
		
	}
	
	@Test
	public void testGetKBEquipmentList() throws ApplicationException, RemoteException {
		System.out.println("testGetKBEquipmentList START");
		
		String productType="C";
		int no=12;
		String[]  equlist= dao.getKBEquipmentList(productType, no);
		for (String eq : equlist) {
			System.out.println(eq);
		}
		
    	System.out.println("testGetKBEquipmentList END"); 
		
	}
	
	@Test
	public void testGetPCSShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetPCSShippedToLocation START");
		
		String serialNumber="00000000003";
		int locationType=2;
		long  shippedLocation= dao.getPCSShippedToLocation(serialNumber, locationType);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetPCSShippedToLocation END"); 
		
	}
	
	
	
	@Test
	public void testGetProductFeatures() throws ApplicationException, RemoteException {
		System.out.println("testGetProductFeatures START");
		
		String pProductCode="*6600";
		String[]  features= dao.getProductFeatures(pProductCode);
		for (String feature : features) {
			System.out.println("feature : "+feature);
		}
		
    	System.out.println("testGetProductFeatures END"); 
		
	}
	
	@Test
	public void testGetShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetShippedToLocation START");
		
		String serialNumber="21101113400";
		int locationType=2;
		long  shippedLocation= dao.getShippedToLocation(serialNumber, locationType);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetShippedToLocation END"); 
		
	}
	
	@Test
	public void testGetVirtualEquipment() throws ApplicationException, RemoteException {
		System.out.println("testGetVirtualEquipment START");
		
		String pSerialNo="21101113400";
		String techTypeClass="VIRTUAL";
		EquipmentInfo  equipmentInfo= dao.getVirtualEquipment(pSerialNo, techTypeClass);
		System.out.println("equipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetVirtualEquipment END"); 
		
	}
	
	@Test
	public void testIsNewPrepaidHandset() throws ApplicationException, RemoteException {
		System.out.println("testIsNewPrepaidHandset START");
		
		String serialNo="270113178105834";
		String productCode="33333333";
		boolean  isNewPrepaidHandset= dao.isNewPrepaidHandset(serialNo, productCode);
		System.out.println("isNewPrepaidHandset : "+isNewPrepaidHandset);
		
    	System.out.println("testIsNewPrepaidHandset END"); 
		
	}
	
	@Test
	public void testGetAssociatedHandsetByUSIMID() throws ApplicationException, RemoteException {
		System.out.println("testGetAssociatedHandsetByUSIMID START");
		
		String USIMID="8912230000000623363";
		EquipmentInfo  equipmentInfo= dao.getAssociatedHandsetByUSIMID(USIMID);
		System.out.println("EquipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetAssociatedHandsetByUSIMID END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobyPhoneNo() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyPhoneNo START");
		
		String pPhoneNo="7780551019";//PCS
		EquipmentInfo  equipmentInfo= dao.getEquipmentInfobyPhoneNo(pPhoneNo);
		System.out.println("EquipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetEquipmentInfobyPhoneNo END"); 
		
	}
	
	@Test
	public void testGetIMEIByUSIMID() throws ApplicationException, RemoteException {
		System.out.println("testGetIMEIByUSIMID START");
		
		String pUSimID="8912230000000623363";
		String  imeiID= dao.getIMEIByUSIMID(pUSimID);
		System.out.println("imeiID : "+imeiID);
		
    	System.out.println("testGetIMEIByUSIMID END"); 
		
	}
	
	@Test
	public void testGetIMEIBySIM() throws ApplicationException, RemoteException {
		System.out.println("testGetIMEIBySIM START");
		
		String pSimID="000800006476300";
		String  imeiID= dao.getIMEIBySIM(pSimID);
		System.out.println("imeiID : "+imeiID);
		
    	System.out.println("testGetIMEIBySIM END"); 
		
	}
	
	@Test
	public void testGetIMSIsByUSIM() throws ApplicationException, RemoteException {
		System.out.println("testGetIMSIsByUSIM START");
		
		String pUSIM_Id="9999999981";
		String[]  imeiIDs= dao.getIMSIsByUSIM(pUSIM_Id);
		for (String imei : imeiIDs) {
			System.out.println(imei);
		}
		
    	System.out.println("testGetIMSIsByUSIM END"); 
		
	}
	
	@Test
	public void testGetMuleBySIM() throws ApplicationException, RemoteException {
		System.out.println("testGetMuleBySIM START");
		
		String pSimID="000800006476300";
		EquipmentInfo  equipmentInfo= dao.getMuleBySIM(pSimID);
		System.out.println("equipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetMuleBySIM END"); 
		
	}
	
	@Test
	public void testGetSIMByIMEI() throws ApplicationException, RemoteException {
		System.out.println("testGetSIMByIMEI START");
		
		String pImeiID="000031206800013";
		String  simid= dao.getSIMByIMEI(pImeiID);
		System.out.println("simid : "+simid);
		
    	System.out.println("testGetSIMByIMEI END"); 
		
	}
	
	@Test
	public void testGetUSIMByIMSI() throws ApplicationException, RemoteException {
		System.out.println("testGetUSIMByIMSI START");
		
		String pIMSI="1111112301";
		String  simid= dao.getUSIMByIMSI(pIMSI);
		System.out.println("simid : "+simid);
		
    	System.out.println("testGetUSIMByIMSI END"); 
		
	}
		
	
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetWarrantyInfo() throws ApplicationException, RemoteException {
		System.out.println("testGetWarrantyInfo START");
		
		String pSerialNo="000102380800031";
		WarrantyInfo  warrantyInfo= dao.getWarrantyInfo(pSerialNo);
		System.out.println("warrantyInfo : "+warrantyInfo);
		
		System.out.println("WarrantyExpiryDate: "+warrantyInfo.getWarrantyExpiryDate());
	    System.out.println("InitialActivationDate :"+ warrantyInfo.getInitialActivationDate());
	    System.out.println("InitialManufactureDate"+  warrantyInfo.getInitialManufactureDate());
	    System.out.println("LatestPendingDate"+ warrantyInfo.getLatestPendingDate());
	    System.out.println("warrantyInfo"+ warrantyInfo.getLatestPendingModel());
	    System.out.println("Message"+ warrantyInfo.getMessage());
	    System.out.println("WarrantyExtensionDate"+ warrantyInfo.getWarrantyExtensionDate());
	    System.out.println("DOAExpiryDate"+ warrantyInfo.getDOAExpiryDate());
		
    	System.out.println("testGetWarrantyInfo END"); 
		
	}
	
	
	@Test
	public void testGetUSIMListByIMSIs() throws ApplicationException, RemoteException {
		System.out.println("testGetUSIMListByIMSIs START");
		
		
		
		Enumeration<String> simidsKey=null;
		String str=null;
		String[] IMISIs={"1111112300","1111111301", "302220999918404","214078036618404"};
		@SuppressWarnings("unchecked")
		Hashtable<String, String> simids= dao.getUSIMListByIMSIs(IMISIs);
		
		simidsKey = simids.keys(); 
		while(simidsKey.hasMoreElements()) { 
			str = (String) simidsKey.nextElement(); 
			System.out.println("IMSI->"+str + ": USIM->" + simids.get(str)); 
		} 
		
    	System.out.println("testGetUSIMListByIMSIs END"); 
		
	}
	
	@Test
	public void testRetrievePagerEquipmentInfo() throws ApplicationException, RemoteException {
		System.out.println("testRetrievePagerEquipmentInfo START");
		
		String serialNo="23508724015";
		EquipmentInfo equInfo= dao.retrievePagerEquipmentInfo(serialNo);
		System.out.println("EquipmentInfo : "+equInfo);
		
    	System.out.println("testRetrievePagerEquipmentInfo END"); 
		
	}
	
	@Test
	public void testRetrieveHSPASubscriberIdsByIMSI() throws ApplicationException, RemoteException {
		System.out.println("testRetrievePagerEquipmentInfo START");
		
		String IMSI="214030000050010";
		SubscriberInfo[] subInfos= dao.retrieveHSPASubscriberIdsByIMSI(IMSI);
		System.out.println("SubscriberInfo length : "+subInfos.length);
		
		for (SubscriberInfo subInfo : subInfos) {
			System.out.println("Ph No: "+subInfo.getPhoneNumber()+", BanId: "+subInfo.getBanId());
		}
		
    	System.out.println("testRetrievePagerEquipmentInfo END"); 
		
	}
	
}
