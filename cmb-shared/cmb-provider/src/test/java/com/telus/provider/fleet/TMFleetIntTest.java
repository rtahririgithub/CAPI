package com.telus.provider.fleet;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.fleet.TalkGroup;

public class TMFleetIntTest extends BaseTest {

	
	public TMFleetIntTest(String name) throws Throwable {
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
	
	public void testNewTalkGroup() throws TelusAPIException{
		TMFleet fleet = (TMFleet)provider.getFleetManager().getFleetById(905, 131077);
		TalkGroup tg= fleet.newTalkGroup("run");
		assertNotNull(tg);
			
	}
	
	public void testGetAssociatedTalkgroupsCount() throws TelusAPIException{
		TMFleet fleet = (TMFleet)provider.getFleetManager().getFleetById(905, 131077);
		int count= fleet.getAssociatedTalkgroupsCount(84);
		assertEquals(0, count);
	}
	
	public void testGetAssociatedAccountsCount() throws TelusAPIException{
		TMFleet fleet = (TMFleet)provider.getFleetManager().getFleetById(905, 131077);
		int count= fleet.getAssociatedAccountsCount();
		assertEquals(1, count);
	}
	
	
}
