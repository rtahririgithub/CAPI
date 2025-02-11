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

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.reference.ActivityType;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.PrepaidAdjustmentReasonInfo;

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

public class ReferenceDataFacadeLocalTest {
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("com.telusmobility.config.java.naming.provider.url", "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory"); 		
	}

	@Autowired
	ReferenceDataFacade referenceDataFacade;
	
	

	@Test
	public void testGetActivityTypes() throws Exception {
		System.out.println( "Test 1" ) ;
		
		ActivityType[] acts = referenceDataFacade.getActivityTypes();
		for( int i=0; i<acts.length; i ++ ) {
		//	if ( acts[i].getCode().toUpperCase().startsWith("ADJR") ) {
			if ( acts[i].getCode().toUpperCase().equals("ADJR") ) {
				System.out.println( acts[i] ) ;
			}
		}
		System.out.println( "Test 2" ) ;

		ActivityType act = referenceDataFacade.getActivityType("ADJR");
		System.out.println( act ) ;

		System.out.println( "Test 3" ) ;
		acts = referenceDataFacade.getActivityTypes("ADJR");
		for( int i=0; i<acts.length; i ++ ) {
			//	if ( acts[i].getCode().toUpperCase().startsWith("ADJR") ) {
			if ( acts[i].getCode().toUpperCase().equals("ADJR") ) {
				System.out.println( acts[i] ) ;
			}
		}
	}

//	@Test
//	public void testGetFeature() throws Exception {
//		System.out.println(referenceDataFacade.getFeature("PTCFC"));
//	}
	
//	@Test
//	public void testGetDealerSalesRep() throws Exception {
//		System.out.println(referenceDataFacade.getDealerSalesRep("B001000001", "0153"));
//	}
	
//	@Test
//	public void testGetRegularService() throws Exception {
//		System.out.println(referenceDataFacade.getRegularService("SWAF0T7"));
//	}
//	
//	@Test
//	public void testGetNumberGroupByPhoneNumberAndProductType() throws Exception {
//		System.out.println(referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("4039990050", "C"));
//	}
//	
//	
//	@Test
//	public void testCheckServiceAssociation() throws Exception {
//		System.out.println(referenceDataFacade.checkServiceAssociation("S100BSMP", "FNTN150A"));
//		System.out.println(referenceDataFacade.checkServiceAssociation("S100BSMP", "FNTN150sA"));
//	}
	
//	@Test
//	public void testCheckServicePrivilege() throws Exception {
//		String[] serviceCodes = {"SWAF0T7"};
//		System.out.println(referenceDataFacade.checkServicePrivilege(serviceCodes, "ALL", "RTNONCHNG"));
//	}
	
//	@Test
//	public void testGetPricePlan() throws Exception {
//		PricePlanInfo pricePlan = referenceDataFacade.getPricePlan("PTLK75CF");
//		System.out.println(pricePlan);
//	}
	
	
	@Test
	public void testRetrieveServiceGroupRelation() {
		
		try {
			System.out.println("Start testRetrieveServiceGroupRelation");
			System.out.println("1. get from DAO layer");
			String[] result=referenceDataFacade.getServiceCodesByGroup("FPUEWQ");
			System.out.println("OUTPUT : "+result.length);
					
			System.out.print("[");
			for(int i=0;i<result.length;i++){
				System.out.print(result[i]+" ");
			}
			System.out.println("]");
			System.out.println("2. get from cache");
			String[] result1=referenceDataFacade.getServiceCodesByGroup("FPUEWQ");
			System.out.println("OUTPUT : "+result1.length);
			
		} catch (TelusException e) {
			e.printStackTrace();
		}
		System.out.println("End testRetrieveServiceGroupRelation");
	}
	
	@Test
	public void testRetrieveServiceTerm () {
		String serviceCode="SAIR2T1";
		try {
			ServiceTermDto result=referenceDataFacade.getServiceTerm(serviceCode);
			System.out.println("RESULT : "+result.toString());
		} catch (TelusException e) {
			e.printStackTrace();
		}
	}
		
	@Test
	public void testgetPrepaidDeviceDirectFulfillmentReasons() {
		try {
			PrepaidAdjustmentReasonInfo[] results = referenceDataFacade.getPrepaidDeviceDirectFulfillmentReasons();
			for (PrepaidAdjustmentReasonInfo info: results) {
				System.out.println("RESULT : "+info.toString());
			}
			assertTrue(results.length>=3);
		} catch (TelusException e) {
			e.printStackTrace();
		}
	}
		
	
	
	
}
