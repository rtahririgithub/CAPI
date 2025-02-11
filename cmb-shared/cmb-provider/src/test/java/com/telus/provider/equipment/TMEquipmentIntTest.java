package com.telus.provider.equipment;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.reference.Brand;
import com.telus.api.reference.EquipmentMode;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.TestTMProvider;

public class TMEquipmentIntTest extends BaseTest {
	
	static {
		setupCHNLECA_PT168();
		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7168");
	System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7168");
	System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7168");
		//setupD3();
	}
	
	public TMEquipmentIntTest(String name) throws Throwable {
		super(name);
	}

	private TestTMProvider testTMProvider;
	private TMEquipmentManager equipmentManager;
	private TMEquipment testEquipment;
	
	public void setUp() throws Exception{
		super.setUp();
		
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		EquipmentInfo ei=new EquipmentInfo();
		ei.setTechType("mike");
		testEquipment = new TMEquipment(testTMProvider, ei);	
	}

	public void testGetEquipmentModes() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("24205462132");
		EquipmentMode[] equipmentMode = tmequipment.getEquipmentModes();
		assertEquals("ANALOG", equipmentMode[0].getDescription());
	}
	
	public void testUpdateStatus() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("24205462132");
		tmequipment.updateStatus(3, 11);			
	}
	public void testGetShippedToLocation() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("24205462132");
		long location = tmequipment.getShippedToLocation();
		assertEquals(10107351, location);		
	}
	
	public void testGetProductFeatures() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("24205462132");
		String[] productFeatures = tmequipment.getProductFeatures();
		assertEquals("PPCOMP", productFeatures[0]);
		
	}
	
	public void testAssociatedSubscribers() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMEquipment tmequipment = (TMEquipment) equipmentManager.getEquipment("17600011912");
		EquipmentSubscriber[] equipmentSubscriber = tmequipment.getAssociatedSubscribers(true);
		for (int i = 0; i < equipmentSubscriber.length; i++) {
			assertEquals("4161626449", equipmentSubscriber[i].getPhoneNumber());
			break;
		}
		
		TMEquipment tmequipment1 = (TMEquipment) equipmentManager.getEquipment("8912239900000600736");
		EquipmentSubscriber[] equipmentSubscriber1 = tmequipment1.getAssociatedSubscribers(true);
		for (int i = 0; i < equipmentSubscriber1.length; i++) {
			assertEquals("7781652978", equipmentSubscriber1[i].getPhoneNumber());
			break;
		}
		
	}
	
}
