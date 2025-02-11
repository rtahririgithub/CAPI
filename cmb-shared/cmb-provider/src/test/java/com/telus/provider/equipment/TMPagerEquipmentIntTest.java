package com.telus.provider.equipment;


import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.reference.Brand;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;

public class TMPagerEquipmentIntTest extends BaseTest {
	
	static {
		
		//setupD3();
		setupEASECA_QA();
	}
	
	public TMPagerEquipmentIntTest(String name) throws Throwable {
		super(name);
	}

	private TestTMProvider testTMProvider;
	private TMEquipmentManager equipmentManager;
	private TMPagerEquipment tmPagerEquip;
	
	public void setUp() throws Exception{
		super.setUp();
		
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		tmPagerEquip = new TMPagerEquipment(testTMProvider, new EquipmentInfo());
	}

	public void testGetAssociatedSubscribers() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException   {	
		TMPagerEquipment tmPagerEquipment = (TMPagerEquipment) equipmentManager.getEquipment("1280293238IJ");
		EquipmentSubscriber[] equipmentSubscribers = tmPagerEquipment.getAssociatedSubscribers(true, true);
		for(int i = 0;i<equipmentSubscribers.length;i++)
			System.out.println("equipmentSubscribers"+equipmentSubscribers[i]);
		// no data found
		//assertEquals("4168940045", equipmentSubscribers[0].getPhoneNumber());
	}
	
}
