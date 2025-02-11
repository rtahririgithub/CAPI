/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.test.productequipment.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.productequipment.lifecyclefacade.dao.ProductDeviceServiceDao;
import com.telus.eas.equipment.productdevice.info.ProductInfo;

/**
 * @author R. Fong
 *
 */
@ContextConfiguration(locations="classpath:com/telus/cmb/test/productequipment/dao/test-context-dao.xml")
public class ProductDeviceServiceDaoTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		
	}
	
	@Autowired
	private ProductDeviceServiceDao dao;
	
	@Test
	public void getProduct() throws Exception {
		
		try {
			ProductInfo product = dao.getProduct("KGDSIM3PRE");
			System.out.println(product.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}