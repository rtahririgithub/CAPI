/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.utility.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.utility.dealermanager.svc.DealerManager;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles("standalone")
@ActiveProfiles({"remote", "lab"})
public class DealerManagerTest extends AbstractTestNGSpringContextTests {

	static {
		
		System.setProperty("weblogic.Name", "standalone");
	}

	@Autowired
	private DealerManager manager;
	
	
	@Test
	public void openSession() throws Exception {
		String sessionId = manager.openSession("18654", "apollo", "SMARTDESKTOP");
		System.out.println(sessionId);
	}

}
