/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;


import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.cmb.reference.dto.FeeRuleDto;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.PrepaidAdjustmentReasonInfo;
import com.telus.eas.utility.info.ServiceInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:application-context-datasources-dv103.xml",
		"classpath:application-context-dao.xml",
		"classpath:com/telus/cmb/reference/svc/application-context-svc-local.xml"
})
public class ReferenceDataHelperSvcLocalTest extends ReferenceDataHelperSvcTest{
    static {
        String url = "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration";
        System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
        System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
        System.setProperty("com.telusmobility.config.provider", "com.telus.provider.config.PropertiesOverridingConfigurationProvider");
        System.setProperty("com.telusmobility.config.propertiesFile", "configuration.properties");
        System.setProperty("com.telusmobility.config.java.naming.provider.url", url);
    }
	
	@Test
	public void testRetrieveRegularServices()  {
		
		
		long start = System.currentTimeMillis();

		try {
			
			System.out.println("Starting retrieveRegularServices()...");
			
			ServiceInfo [] result = service.retrieveRegularServices();
			System.out.println("Retrieved [" + result.length + "] services in [" + (System.currentTimeMillis() - start) + "] msec.");
			
			for(int i=0;i<result.length;i++){
				System.out.println("Code : "+result[i].getCode()+", BillCycleTreatmentCode : "+result[i].getBillCycleTreatmentCode());
			}
			
		} catch (Exception e) {
			System.out.println("Exception [" + e.getMessage()+ "] after [" + (System.currentTimeMillis() - start) + "] msec.");
			e.printStackTrace();
		}
	}
	
	public void testRetrievePaperBillChargeType() {
		
		System.out.println("Starting testRetrievePaperBillChargeType()...");
		//brand id= 1, 3, 0
		int brandId=3;

		//provincecode= ON, SK , PQ, NB , NULL
		String provinceCode="SK";
		
        //  accountType= I , B , C, '\u0000'
    	char accountType= 'I';
    	
    	// accountSubType = R , P, O , Q, B , C, 
    	char accountSubType='Q';
    	
    	// segment= BMA , TCSO , TCSQ , OTHR, TBSO, 
    	String segment="TCSO";
    	String invoiceSuppressionLevel="1";
    	
    	
		try {
			 FeeRuleDto[] feeRule=service.retrievePaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment, invoiceSuppressionLevel, service.retrieveLogicalDate());
			 
			 System.out.println("feeRule length :" + feeRule.length);
			
			 System.out.println("Brand \t    Province  \t    Account Type    Account Sub Type    GL Segment      code      ProductType       ManualCharge       Amount    AmountOverrideable      Level      BalanceImpact      Description       Desc_French  ");
			 for(int j=0; j<feeRule.length; j++){
					System.out.println(feeRule[j].getBrandId()+"\t \t "+feeRule[j].getProvinceCode()+"\t \t"+feeRule[j].getAccountType()+
								"\t \t"+feeRule[j].getAccountSubType()+"\t \t"+feeRule[j].getSegment()+"\t \t"
								+feeRule[j].getCode()+"\t \t"+
								feeRule[j].getProductType()+"\t \t"+feeRule[j].isManualCharge()+"\t \t"+ feeRule[j].getAmount()+"\t \t"+
								feeRule[j].isAmountOverrideable()+"\t \t"+ feeRule[j].getLevel()+"\t \t"+ feeRule[j].getBalanceImpact()
								+"\t \t"+feeRule[j].getDescription().trim()+"\t \t"+ feeRule[j].getDescriptionFrench().trim());
			 }
			 
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("Ending testRetrievePaperBillChargeType()...");
	}
	
	
	@Test
	public void testRetrieveServiceGroupRelation() {
		
		try {
			System.out.println("Start testRetrieveServiceGroupRelation");
			
			String[] result=service.retrieveServiceGroupRelation("FPUEWQ   ");
			System.out.println("OUTPUT : "+result.length);
					
			System.out.println("[");
			for(int i=0;i<result.length;i++){
				System.out.print(result[i]+" ");
			}
			System.out.println("]");
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("End testRetrieveServiceGroupRelation");
	}
	
	

	@Test
	public void testRetrieveServiceTerm() {
		System.out.println("testRetrieveServiceTerm  start");
		try{
			
			String serviceCode="SAIR2T1  ";
			ServiceTermDto seTermDto= service.retrieveServiceTerm(serviceCode);
			System.out.println("OUTPUT : "+seTermDto.toString());
			
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("testRetrieveServiceTerm  End");
		
	}


	@Test
	public void testRetrievePrepaidDeviceDirectFulfillmentReasons() {
		System.out.println("retrievePrepaidDeviceDirectFulfillmentReasons start");
		try{
			
			PrepaidAdjustmentReasonInfo[] reasons = service.retrievePrepaidDeviceDirectFulfillmentReasons();
			assertTrue(reasons.length >= 3);
			for (PrepaidAdjustmentReasonInfo r:reasons) {
				System.out.println(r.toString());
			}
			
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("retrievePrepaidDeviceDirectFulfillmentReasons End");
		
	}
}
