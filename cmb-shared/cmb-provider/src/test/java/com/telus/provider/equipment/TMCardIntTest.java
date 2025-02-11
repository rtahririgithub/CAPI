package com.telus.provider.equipment;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Service;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;

public class TMCardIntTest extends BaseTest {
	
	static {
		/*System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "false");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "false");
		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "false");*/
		
//		System.setProperty("cmb.services.SubscriberLifecycleManager.usedByProvider", "true");
//		System.setProperty("cmb.services.SubscriberLifecycleFacade.usedByProvider", "true");
//		System.setProperty("cmb.services.SubscriberLifecycleHelper.usedByProvider", "true");
//		setupD3();
		setupEASECA_QA();
	}
	
	public TMCardIntTest(String name) throws Throwable {
		super(name);
	}

	private TestTMProvider testTMProvider;
	private TMEquipmentManager equipmentManager;
	private TMCard testTMCard;
	
	public void setUp() throws Exception{
		super.setUp();
		
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		testTMCard = new TMCard(testTMProvider, new CardInfo());
	}

	public void testGetServices() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		Subscriber[] subscribers = provider.getAccountManager().findSubscribersBySerialNumber("24205462132");
		try{
		TMCard card = (TMCard) equipmentManager.getCardBySerialNumber("24205462132");
		Service[] services = card.getServices(subscribers[0]);
		for (int i = 0;i<services.length;i++)
			System.out.println("services"+services[i].getNetworkType());
		assertEquals("D", services[0].getNetworkType());
		}catch(Throwable t){
			System.out.println("Error"+t.getMessage());
			
			assertTrue(t.getMessage().contains("Unknown card number"));
		}
		
	}
	
	public void testSetCredited() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		Subscriber[] subscribers = provider.getAccountManager().findSubscribersBySerialNumber("24205462132");
		try{
		TMCard card = (TMCard) equipmentManager.getCardBySerialNumber("24205462132");
		card.setCredited(subscribers[0], true);
		}catch(Throwable t){
			assertTrue(t instanceof TelusAPIException); 
			assertTrue(t.getMessage().contains("Unknown card number"));
		}
	}
	public void testSetStolen() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {
		try{
		TMCard card = (TMCard) equipmentManager.getCardBySerialNumber("100000000000000590790");
		card.setStolen();
		}catch(Throwable t){
			assertTrue(t instanceof TelusAPIException); 
			assertTrue(t.getMessage().contains("Serial number not 11 in length"));
		}
	}
	
}
