package com.telus.provider.fleet;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.fleet.TalkGroup;

public class TMTalkGroupIntTest extends BaseTest {

	
	public TMTalkGroupIntTest(String name) throws Throwable {
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
	
	public void testGetAttachedSubscriberCount() throws TelusAPIException{
		TMFleet fleet = (TMFleet)provider.getFleetManager().getFleetById(905,16389);
		assertNotNull(fleet);
			TalkGroup[] talkGroups=fleet.getTalkGroups();
			
			if(talkGroups.length>0){
				TMTalkGroup talkGroup= (TMTalkGroup)talkGroups[0];
				int count = talkGroup.getAttachedSubscriberCount(20004220);
				assertEquals(1, count);
			}
			
	}
	
	public void testSave() throws TelusAPIException{
		System.out.println("testSave start");
		try{
		TMFleet fleet = (TMFleet)provider.getFleetManager().getFleetById(905,16389);
		assertNotNull(fleet);
			TalkGroup[] talkGroups=fleet.getTalkGroups();
			
			if(talkGroups.length>0){
				TMTalkGroup talkGroup= (TMTalkGroup)talkGroups[0];
				talkGroup.save();
			}
		}catch(TelusAPIException e){
			assertEquals(": BAN not found.", e.getMessage());
		}
			System.out.println("testSave end");
		
	}
}
