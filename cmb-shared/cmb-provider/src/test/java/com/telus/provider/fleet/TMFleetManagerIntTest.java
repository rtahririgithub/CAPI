package com.telus.provider.fleet;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.fleet.Fleet;

public class TMFleetManagerIntTest extends BaseTest {

	
	public TMFleetManagerIntTest(String name) throws Throwable {
		super(name);
	}

	static {
		//setupD3();
		setupEASECA_QA();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public void setUp() throws Exception{
		super.setUp();
	}
	
	public void testGetFleetsByBan() throws TelusAPIException{
			TMFleetManager fleetManager=provider.getFleetManager0();
			Fleet[] fleets = fleetManager.getFleetsByBan(84);
			assertEquals(0, fleets.length);
	}
}
