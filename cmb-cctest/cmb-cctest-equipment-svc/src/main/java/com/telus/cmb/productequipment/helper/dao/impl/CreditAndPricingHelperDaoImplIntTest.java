package com.telus.cmb.productequipment.helper.dao.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.productequipment.helper.dao.CreditAndPricingHelperDao;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-dao-product-equipment-helper.xml", 
		"classpath:application-context-datasources.xml"})

public class CreditAndPricingHelperDaoImplIntTest {
	
	@Autowired
	CreditAndPricingHelperDao dao;
	

	
	@Test
	public void testGetDefaultActivationCredits() throws ApplicationException {
		System.out.println("testGetDefaultActivationCredits START");
		
		
		String province="BC";
		String npa="";
		Date activationDate=new Date();
		int contractTermMonths=24;
		String creditType="ACT";
		String productType="C";
		
		
		ActivationCreditInfo[] activationCreditInfo= dao.getDefaultActivationCredits(productType, province, npa, 
				contractTermMonths, creditType, activationDate);
		for(ActivationCreditInfo info :activationCreditInfo)
		System.out.println("ActivationCreditInfo : "+info);
		
    	System.out.println("testGetDefaultActivationCredits END"); 
		
	}
	
	
	@Test
	public void testGetBaseProductPriceByProductCodeFromP3MS() throws ApplicationException {
		System.out.println("testGetBaseProductPriceByProductCodeFromP3MS START");
		
		String productCode="SCHA600";
		Date activationDate=new Date();
		String province="MB";
		
		double basePrice= dao.getBaseProductPriceByProductCodeFromP3MS(productCode, province, activationDate);
		System.out.println("basePrice : "+basePrice);
		
    	System.out.println("testGetBaseProductPriceByProductCodeFromP3MS END"); 
		
	}
	
	
	@Test
	public void testGetBaseProductPriceByProductCodeFromACME() throws ApplicationException {
		System.out.println("testGetBaseProductPriceByProductCodeFromACME START");
		
		String productCode="8767A";
		Date activationDate=new Date();
		String province="AB";
		String npa="416";
		
		double basePrice= dao.getBaseProductPriceByProductCodeFromACME(productCode, province, npa, activationDate);
		System.out.println("basePrice : "+basePrice);
		
    	System.out.println("testGetBaseProductPriceByProductCodeFromACME END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobySerialNo() throws ApplicationException {
		System.out.println("testGetEquipmentInfobySerialNo START");
		
            EquipmentInfo equip = dao.getEquipmentInfobySerialNo("21101112944");	// PCS Equipment
//    		EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("000100005794080");	// IEMI Equipment
//    		EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("100000000100540");	// HSPA Equipment
//    		EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("8912230000000071266");	// USIM Equipment
    		
			System.out.println("EquipmentInfo = " + equip);
			
			System.out.println("ProductGroupTypeID :"+equip.getProductGroupTypeID()+" ProductTypeID : "+ equip.getProductTypeID());
			System.out.println("testGetEquipmentInfobySerialNo END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobySerialNumber() throws ApplicationException {
		System.out.println("testGetEquipmentInfobySerialNumber START");
		
            EquipmentInfo equip = dao.getEquipmentInfobySerialNo("21101112944");	// PCS Equipment

    		System.out.println("EquipmentInfo = " + equip);
			System.out.println("testGetEquipmentInfobySerialNumber END"); 
		
	}
	
	@Test
	public void testGetESNByPseudoESN() throws ApplicationException {
		System.out.println("testGetESNByPseudoESN START");
		
            String[] esns = dao.getESNByPseudoESN("21101112944");	// PCS Equipment
            
    		System.out.println("ESNs length : " + esns.length);
			System.out.println("testGetESNByPseudoESN END"); 
		
	}	
	
	@Test
	public void testGetEquipmentInfobySerialNo_1() throws ApplicationException {
		System.out.println("testGetEquipmentInfobySerialNo_1 START");
		
    	   EquipmentInfo[] equip = dao.getEquipmentInfobySerialNo("21101112944", true);	// PCS Equipment
           System.out.println("Length : "+equip.length);
           for (EquipmentInfo ei : equip) {
            	System.out.println("EquipmentInfo = " + ei);
    	   }
			
			System.out.println("testGetEquipmentInfobySerialNo_1 END"); 
		
	}
	
	@Test
	public void testGetActivationCreditsByProductCodeFromACME() throws ApplicationException {
		System.out.println("testGetActivationCreditsByProductCodeFromACME START");
		
			String productCode="SPHA523";
			
			Date activationDate=new Date();
			String province="ON";
			String npa="416";
			int contractTermMonths=36;
			String creditType="%";
			String productType="C";
			boolean isInitialActivation=false;
			
    	   ActivationCreditInfo[] activationCreditInfos = dao.getActivationCreditsByProductCodeFromACME(productCode, 
    			   province, npa, contractTermMonths, creditType, activationDate, productType, isInitialActivation);
    	   
    	   System.out.println("Length : "+activationCreditInfos.length);
           for (ActivationCreditInfo ac : activationCreditInfos) {
            	System.out.println("ActivationCreditInfo = " + ac);
    	   }
			
			System.out.println("testGetActivationCreditsByProductCodeFromACME END"); 
		
	}
	
	@Test
	public void testGetActivationCreditsByProductCodeFromP3MS() throws ApplicationException {
		System.out.println("testGetActivationCreditsByProductCodeFromP3MS START");
		
			String productCode="SPHA523";
			
			Date activationDate=new Date();
			String province="ON";
			int contractTermMonths=36;
			String creditType="%";
			String productType="C";
			boolean isInitialActivation=false;
			
    	   ActivationCreditInfo[] activationCreditInfos = dao.getActivationCreditsByProductCodeFromP3MS(productCode, 
    			   province, contractTermMonths, creditType, activationDate, productType, isInitialActivation);
    	   
    	   System.out.println("Length : "+activationCreditInfos.length);
           for (ActivationCreditInfo ac : activationCreditInfos) {
            	System.out.println("ActivationCreditInfo = " + ac);
    	   }
			
			System.out.println("testGetActivationCreditsByProductCodeFromP3MS END"); 
		
	}
	

	
}
