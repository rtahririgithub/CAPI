package com.telus.provider.equipment;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.CellularDigitalEquipmentUpgrade;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.PrepaidEventType;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.NewEjbTestMethods;
import com.telus.provider.TestTMProvider;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.equipment.TMCellularDigitalEquipment;
import com.telus.provider.equipment.TMCellularEquipment;

public class TMCellularEquipmentIntTest extends BaseTest {

	private TMAccountManager accountManager;
	private TestTMProvider testTMProvider;
	private TMCellularEquipment testCellularEquip;
	
	static {
	  setupEASECA_QA();
		//setupD3();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");


	}
	
	public TMCellularEquipmentIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});
		testCellularEquip = new TMCellularEquipment(testTMProvider, new EquipmentInfo()); 
	}
	
	public void testIsValidForPrepaidActivationWithoutPin() throws TelusAPIException, SecurityException, NoSuchMethodException{
		 
		 TMCellularEquipment ce = (TMCellularEquipment) api.getEquipmentManager().getEquipmentByPhoneNumber("4161752888");
		 assertFalse(ce.isValidForPrepaidActivationWithoutPin());
		
	}
}


