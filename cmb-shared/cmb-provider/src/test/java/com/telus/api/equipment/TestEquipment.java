package com.telus.api.equipment;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.provider.util.SubscriberManagerBeanCmbImpl;

public class TestEquipment extends BaseTest {
		
	static {
     	setupSMARTDESKTOP_CSI();
     	//setupSMARTDESKTOP_D3();
     	//setuplocalHost();
    }
	public TestEquipment(String name) throws Throwable {
		super(name);
	}
	

	public void testGetWarranty() throws Exception {
		String serialNumber="900000000070594"; //PT148 Apple
		Warranty warranty = provider.getProductEquipmentLifecycleFacade().getWarrantySummary(serialNumber, "HSPA");
		if (warranty != null) {
			System.out.println(warranty);
		}
		
	}
	
	public void testAssignEquipmentToPhoneNumber() throws Exception {
		//TMSemsManager semsManager = (TMSemsManager) provider.getSemsManager();
		provider.getProductEquipmentLifecycleFacade().assignEquipmentToPhoneNumber("4034850238", "8912230000093531416", "900000000587754"); 
	}
	
	public void test() {
		try {

			SubscriberManagerBeanCmbImpl bean = (SubscriberManagerBeanCmbImpl) provider.getSubscriberManagerBean();
			String id = bean.getSessionId();
			System.out.println(id);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
}
