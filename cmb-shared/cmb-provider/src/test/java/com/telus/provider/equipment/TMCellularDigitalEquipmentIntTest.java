package com.telus.provider.equipment;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.CellularDigitalEquipmentUpgrade;
import com.telus.api.reference.Brand;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.equipment.TMCellularDigitalEquipment;

public class TMCellularDigitalEquipmentIntTest extends BaseTest {

	private TMAccountManager accountManager;
	private TestTMProvider testTMProvider;
	private TMCellularDigitalEquipment testCelDigiEquip;
	
	static {
		
		
		setupEASECA_QA();
		//setupD3();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

		
	}
	
	public TMCellularDigitalEquipmentIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		testCelDigiEquip = new TMCellularDigitalEquipment(testTMProvider, new EquipmentInfo());
	}
	
	public void testGetCellularDigitalEquipmentUpgrades() throws TelusAPIException, SecurityException, NoSuchMethodException{
		 
		 TMCellularDigitalEquipment cde = (TMCellularDigitalEquipment) api.getEquipmentManager().getEquipmentByPhoneNumber("4161752888");
		 CellularDigitalEquipmentUpgrade[] cdeu = cde.getCellularDigitalEquipmentUpgrades();
		assertTrue(cdeu[0].isOtaspAvailable());
	}
}


