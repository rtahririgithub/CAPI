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

import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;

/**
 * @author Pavel Simonovsky
 *
 */

@ContextConfiguration(locations="classpath:application-context-test.xml")
@ActiveProfiles({"standalone"})
public class ProductEquipmentManagerTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");
	}
	
	@Autowired
	private ProductEquipmentManager manager;

	@Test
	public void test() throws Exception {
		System.out.println(manager);
	}
}
