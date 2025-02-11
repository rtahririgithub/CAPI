/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.productequipment.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.equipment.Warranty;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.eas.equipment.productdevice.info.ProductInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles({"standalone"})
@ActiveProfiles({"remote", "pt168"})
public class ProductEquipmentFacadeTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private ProductEquipmentLifecycleFacade facade;

	@Test
	public void getWarrantySummary() throws Exception {
		Warranty warranty = facade.getWarrantySummary("8912257028989571467", "USIM"); 
		System.out.println(warranty.getMessage());
	}
	
	@Test
	public void getProduct() throws Exception {
		ProductInfo product = facade.getProduct("KGDSIM5"); 
		System.out.println(product.toString());
	}
	
}