package com.telus.api.reference;

/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

import java.util.*;
import com.telus.api.*;

public class TestTaxationPolicyRetrieval extends BaseTest{
	public TestTaxationPolicyRetrieval(String name) throws Throwable {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private static ReferenceDataManager rdm;
	private static ClientAPI api = null;

	static {    
		//BaseTest.setupEASECA_STG();
		BaseTest.setupEASECA_QA();
		
//		System.setProperty("cmb.services.ReferenceDataRetrievalSvc.url", "t3://localhost:1001");
//		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:1001");
		//--------------------------------
		// Connect to business objects.
		//--------------------------------
		try {

			System.out.println("Getting instance of ClientAPI...");
			api = ClientAPI.getInstance("18654","apollo","testlet");
			rdm = api.getReferenceDataManager();

			Thread.sleep(15000);     
			
			System.out.println(new Date() + "All done!");

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			//api.destroy();
		}
	}
  
	public void testRetrieveTaxPolicies() throws TelusAPIException {
		TaxationPolicy[] policies = api.getProvider().getReferenceDataManager().getTaxationPolicies();
  	  	
  	  	System.out.println("Taxation Policy [" + policies[0].toString() + "]");	
	}
}
