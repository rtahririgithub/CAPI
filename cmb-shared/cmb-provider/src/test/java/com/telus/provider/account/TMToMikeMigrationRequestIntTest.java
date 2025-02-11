package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.provider.equipment.TMOneRTTEquipment;
import com.telus.provider.equipment.TMUSIMCardEquipment;

public class TMToMikeMigrationRequestIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
		System.setProperty("cmb.services.ProductEquipmentHelper.usedByProvider", "false");
		System.setProperty("cmb.services.ProductEquipmentManager.usedByProvider", "false");
		System.setProperty("cmb.services.ProductEquipmentLifecycleFacade.usedByProvider", "false");
		
		/*System.setProperty("cmb.services.ProductEquipmentHelper.usedByProvider", "true");
		System.setProperty("cmb.services.ProductEquipmentManager.usedByProvider", "true");
		System.setProperty("cmb.services.ProductEquipmentLifecycleFacade.usedByProvider", "true");
		*/
		setupD3();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

		
//		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");

	}
	
	public TMToMikeMigrationRequestIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	
	public void testPosttMigrationTask() throws TelusAPIException{
		String pricePlanCode = "SPWSEN";
		 String newEquipmentNo="05311108545";
		 String phoneNumber="9057160877";
		 
		 TMIDENPostpaidBusinessRegularAccount targetAccount = (TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(6002262);
		 Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		
		 Equipment newEquipment =api.getEquipmentManager().getEquipment(newEquipmentNo);
		 
		 TMToMikeMigrationRequest t = (TMToMikeMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(targetAccount, newEquipment, pricePlanCode);
		 t.postMigrationTask();
		 assertFalse(((TMOneRTTEquipment)newEquipment).getDelegate().isMule());
		 
		
		}
}


