package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationTopUp;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.eas.account.info.ActivationTopUpInfo;

public class TMAnyToPrepaidMigrationRequestIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

	}
	
	public TMAnyToPrepaidMigrationRequestIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();

	}
		
	public void testPostMigrationTask() throws TelusAPIException{
		System.out.println("testPosttMigrationTask start");
		 String pricePlanCode = "3PSS1502 ";
		 String newEquipmentNo="15603173774";
		 String phoneNumber="4033946834";
		 String activationTopUpCardType="DEBITCARD";
		 
		 PrepaidConsumerAccount targetAccount =(PrepaidConsumerAccount) api.getAccountManager().findAccountByBAN(3226571);
		 targetAccount.getActivationTopUpPaymentArrangement().setActivationTopUpCardType(activationTopUpCardType);
		 targetAccount.getActivationTopUpPaymentArrangement().setActivationTopUpAmount(100.00);
		 Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		
		 Equipment newEquipment =api.getEquipmentManager().getEquipment(newEquipmentNo);
		 
		 TMAnyToPrepaidMigrationRequest t = (TMAnyToPrepaidMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(targetAccount, newEquipment, pricePlanCode);
		 t.postMigrationTask();
		
       	System.out.println("testPosttMigrationTask End");
	}
	
	public void testCreditSubscriberMigration() throws TelusAPIException{
		System.out.println("testCreditSubscriberMigration start");
		
		 String pricePlanCode = "3PSS1502 ";
		 String newEquipmentNo="15603173578";
		 String phoneNumber="4037109656";
		
		 PrepaidConsumerAccount targetAccount =(PrepaidConsumerAccount) api.getAccountManager().findAccountByBAN(4036958);
		 Equipment newEquipment =api.getEquipmentManager().getEquipment(newEquipmentNo);
		 Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		
		 TMAnyToPrepaidMigrationRequest t = (TMAnyToPrepaidMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(targetAccount, newEquipment, pricePlanCode);
		
		 ActivationTopUp activationTopUp= new ActivationTopUpInfo();
		 activationTopUp.setAmount(100.00);
		 activationTopUp.setReasonCode("reasonCode");
		 
		 t.setActivationTopUp(activationTopUp);
		 t.creditSubscriberMigration();
		System.out.println("testCreditSubscriberMigration End");
	}
	
	
	public void testPerformPostAccountCreationPrepaidTasks() throws TelusAPIException{
		System.out.println("testPerformPostAccountCreationPrepaidTasks start");
		try{
		
			 String pricePlanCode = "3PSS1502 ";
			 String newEquipmentNo="15603173578";
			 String phoneNumber="4037109656";
		
		 PrepaidConsumerAccount targetAccount =(PrepaidConsumerAccount) api.getAccountManager().findAccountByBAN(70042446);
		 Equipment newEquipment =api.getEquipmentManager().getEquipment(newEquipmentNo);
		 Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		 
		 TMAnyToPrepaidMigrationRequest t = (TMAnyToPrepaidMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(targetAccount, newEquipment, pricePlanCode);
		
		 t.performPostAccountCreationPrepaidTasks();
		}catch(TelusAPIException e){
		//assertEquals("BAN must be in tentative status", e.getMessage());
		}
		 
		System.out.println("testPerformPostAccountCreationPrepaidTasks End");
	}
	
	public void testDefect_PROD00206278() throws TelusAPIException{
		
		System.out.println("testDefect_PROD00206278 start");
		
		String pricePlanCode="TXPBAPDA2";
		
		System.out.println("1. get newAccount");
		Account newAccount=api.getAccountManager().findAccountByBAN(70619945);
		
		System.out.println("2. get newEquipment");
		Equipment newEquipment =api.getEquipmentManager().getEquipment("8912246818289469360");
		
		System.out.println("3.get newAssociatedHandset ");
		Equipment newAssociatedHandset =api.getEquipmentManager().getEquipment("900000000617764");
		
		System.out.println("4.get subscriber ");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("5871752218");
		
		System.out.println("5.Invoke newMigrationRequest() ");
		TMMigrationRequest t = (TMMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(newAccount, newEquipment, newAssociatedHandset, pricePlanCode);
		 
		System.out.println("6. call end ");
		System.out.println("testDefect_PROD00206278 end");
		
	}
	
	}


