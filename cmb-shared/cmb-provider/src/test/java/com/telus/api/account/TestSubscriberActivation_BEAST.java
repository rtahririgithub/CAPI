package com.telus.api.account;

/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

import com.telus.api.reference.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.*;
import com.telus.api.*;
import com.telus.provider.reference.TMServiceSummary;

public class TestSubscriberActivation_BEAST extends BaseTest{
	public TestSubscriberActivation_BEAST(String name) throws Throwable {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private static AccountManager accountManager;
	private static ReferenceDataManager rdm;
	private static ClientAPI api = null;
	
	private static final int BAN=1502119;
	private static final String SERIAL_NO="15603173578";	//Serial Number (obtained from CDM web side)
	private static final String[] SECONDARY_SERIAL_NO_LIST={"15603173809"};		//Secondary Serial Numbers (obtained from CDM website)
	private static final String SIM = "8912230000002549764";	
																//SELECT * FROM USIM_STATUS	WHERE EQUIPMENT_STATUS_TYPE_ID = 16 AND EQUIPMENT_STATUS_ID = 187 -- ASSIGNABLE STATUS 
	private static final String IMEI = "500000000102109";	//HSPA Handset IMEI
	
	static {    
		//ENV settings
		BaseTest.setupEASECA_QA();
		
		//Override ReferenceDataHelperproperty setting in LDAP (if needed)
		//System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:1001");
		
		//--------------------------------
		// Connect to business objects.
		//--------------------------------
		try {

			System.out.println("Getting instance of ClientAPI...");
			api = ClientAPI.getInstance("18654","apollo","testlet");
			accountManager = api.getAccountManager();
			rdm = api.getReferenceDataManager();			
			
			Thread.sleep(15000);       	  
			System.out.println(new Date() + "All done!");

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			//api.destroy();
		}
	}
  
	public void testRun() throws Throwable{
		/*=====================================================================================
		 * # 1a Create Subscriber from Reserved Subscriber (ESN ONLY)
		 * 
		 * Test case for Change # 1 (Create Subscriber from Reserved Subscriber). 
		 * To test this functionality properly, run _testPartiallyReserveSub() 
		 * first and then run _testActivateFromPartiallyReservedSub() in a separate thread
		 * ==================================================================================== 
		 */
//		_testPartiallyReserveSub_ESN_ONLY();
		_testActivateFromPartiallyReservedSub_ESN_ONLY();
				
		/*=====================================================================================
		 * # 1b Create Subscriber from Reserved Subscriber (SECONDARY DEVICES ONLY)
		 * 
		 * Test case for Change # 1 (Create Subscriber from Reserved Subscriber). 
		 * To test this functionality properly, run _testPartiallyReserveSub() 
		 * first and then run _testActivateFromPartiallyReservedSub() in a separate thread
		 * ==================================================================================== 
		 */
//		_testPartiallyReserveSub_SECONDARY_DEVICE();
//		_testActivateFromPartiallyReservedSub_SECONDARY_DEVICE();
//
		/*=====================================================================================
		 * # 1c Create Subscriber from Reserved Subscriber (HANDSET IMEI)
		 * 
		 * Test case for Change # 1 (Create Subscriber from Reserved Subscriber). 
		 * To test this functionality properly, run _testPartiallyReserveSub() 
		 * first and then run _testActivateFromPartiallyReservedSub() in a separate thread
		 * ==================================================================================== 
		 */
//		_testPartiallyReserveSub_HANDSET_IMEI();
//		_testActivateFromPartiallyReservedSub_HANDSET_IMEI();
		
		/*=====================================================================================
		 * # 1d Create Subscriber from Reserved Subscriber (SIM ONLY)
		 * 
		 * Test case for Change # 1 (Create Subscriber from Reserved Subscriber). 
		 * To test this functionality properly, run _testPartiallyReserveSub() 
		 * first and then run _testActivateFromPartiallyReservedSub() in a separate thread
		 * ==================================================================================== 
		 */
//		_testPartiallyReserveSub_SIM_ONLY();
//		_testActivateFromPartiallyReservedSub_SIM_ONLY();
		
		/*=====================================================================================
		 * # 2 Create Subscriber from Serialized Subscriber
		 * 
		 * Test case for Change # 2 (Create Subscriber from serialized Subscriber). 
		 * To test this functionality properly, run testCreateSubscriberFromSerializedSub_Part1() 
		 * first and then run testCreateSubscriberFromSerializedSub_Part2() in a separate thread
		 * ==================================================================================== 
		 */
//		_testCreateSubscriberFromSerializedSub_Part1();
//		_testCreateSubscriberFromSerializedSub_Part2();
		
	}
	
	public void _testPartiallyReserveSub_ESN_ONLY() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber partialSub = reserveSubscriber(account.getBanId(), SERIAL_NO);
		
		assertNotNull(partialSub);
		System.out.println ("Partially reserved Sub: [" + partialSub.getPhoneNumber() + "]");
		store2Disk(partialSub);
	}
	
	public void _testActivateFromPartiallyReservedSub_ESN_ONLY() throws Throwable {
		
		Subscriber partialSub = readFromDisk();
		assertNotNull(partialSub);
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber sub = ((PCSAccount)account).newPCSSubscriber(partialSub.getPhoneNumber(), SERIAL_NO, false, "EN");	
		activateSubscriber(sub);
	}

	public void _testPartiallyReserveSub_SIM_ONLY() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber partialSub = reserveSubscriber(account.getBanId(), null, SIM);
		
		assertNotNull(partialSub);
		System.out.println ("Partially reserved Sub: [" + partialSub.getPhoneNumber() + "]");
		
		// Serialise 2 Disk
		store2Disk(partialSub);
	}
	
	public void _testActivateFromPartiallyReservedSub_SIM_ONLY() throws Throwable {
		// Restore from disk
		Subscriber partialSub = readFromDisk();
		assertNotNull(partialSub);
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber sub = ((PCSAccount)account).newPCSSubscriber(partialSub.getPhoneNumber(), SIM, false, "EN");	
		activateSubscriber(sub);
	}

	public void _testPartiallyReserveSub_SECONDARY_DEVICE() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber partialSub = reserveSubscriber(account.getBanId(), SERIAL_NO, null, SECONDARY_SERIAL_NO_LIST);
		
		assertNotNull(partialSub);
		System.out.println ("Partially reserved Sub: [" + partialSub.getPhoneNumber() + "]");
		
		// Serialise 2 Disk
		store2Disk(partialSub);
	}
	
	public void _testActivateFromPartiallyReservedSub_SECONDARY_DEVICE() throws Throwable {
		// Restore from disk
		Subscriber partialSub = readFromDisk();
		assertNotNull(partialSub);
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber sub = ((PCSAccount)account).newPCSSubscriber(partialSub.getPhoneNumber(), SERIAL_NO, SECONDARY_SERIAL_NO_LIST, false, null, "EN");
		activateSubscriber(sub);
	}
	
	public void _testPartiallyReserveSub_HANDSET_IMEI() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(BAN);
		Subscriber partialSub = reserveSubscriber(account.getBanId(), null, SIM, IMEI);
		
		assertNotNull(partialSub);
		System.out.println ("Partially reserved Sub: [" + partialSub.getPhoneNumber() + "]");
		
		// Serialise 2 Disk
		store2Disk(partialSub);
	}
	
	public void _testActivateFromPartiallyReservedSub_HANDSET_IMEI() throws Throwable {
		// Restore from disk
		Subscriber partialSub = readFromDisk();
		assertNotNull(partialSub);
		Account account = accountManager.findAccountByBAN(70402265);
		Subscriber sub = ((PCSAccount)account).newPCSSubscriber(partialSub.getPhoneNumber(), SIM, IMEI, false, null, "EN");
		activateSubscriber(sub);
	}
	
	public void _testCreateSubscriberFromSerializedSub_Part1() throws Throwable {
		String[] serialNumber =  getSerialNumbers(Subscriber.PRODUCT_TYPE_PCS, false, 1);
		Subscriber subscriber = reserveSubscriber(BAN, serialNumber[0]); 
     
		assertNotNull(subscriber);
		System.out.println("Subscriber Reserved: " + subscriber.getPhoneNumber());
    
		// Serialise 2 Disk
		store2Disk(subscriber);
	}
	
	public void _testCreateSubscriberFromSerializedSub_Part2() throws Throwable {
		// Restore from disk
		Subscriber storedSubscriber = readFromDisk();
		assertNotNull(storedSubscriber);
		
		Subscriber restoredSubscriber = accountManager.newSubscriber(storedSubscriber);
		assertNotNull(restoredSubscriber);
		
		boolean isGPS = restoredSubscriber.getEquipment().isGPS();
    	activateSubscriber(restoredSubscriber);
	}
  
	public void _testActivateFromPartiallyReservedSub(String phoneNum) throws TelusAPIException{
	
	}
	
	public static void store2Disk(Subscriber subscriber) {
		try {
			OutputStream os = new FileOutputStream("subscriber.out");
			ObjectOutput oo = new ObjectOutputStream(os);
			oo.writeObject(subscriber);
			oo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static Subscriber readFromDisk() {
		Subscriber subscriber = null;
		try {
			InputStream is = new FileInputStream("subscriber.out");
			ObjectInput oi = new ObjectInputStream(is);
			subscriber = (Subscriber)oi.readObject();
			oi.close();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return subscriber;
	}

	public static Subscriber reserveSubscriber(int ban, String serialNumber) throws TelusAPIException {
		return reserveSubscriber(ban, serialNumber, null);
	}
  
	public static Subscriber reserveSubscriber(int ban, String serialNumber, String IMSI) throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(ban);
		Subscriber reservedSubscriber=null;
		if (IMSI==null)
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, false, "EN");
		else
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(IMSI, false, null,  "EN");
		
		NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups();
		NumberGroup numberGroup = null;
		for (int i=0; i < numberGroups.length; i++) {
			//System.out.println("NumberGroup " + i + ": " + numberGroups[i]);
			if (numberGroups[i].getCode().equals("TOR")){
				numberGroup = numberGroups[i];
				break;
			}
		}		
		PhoneNumberReservation pnr = accountManager.newPhoneNumberReservation();
		pnr.setNumberGroup(numberGroup);
		pnr.setPhoneNumberPattern("416");
		AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
		// take first one
		pnr.setPhoneNumberPattern(phoneNumbers[0].getPhoneNumber());
		reservedSubscriber.reservePhoneNumber(pnr);
		return reservedSubscriber;
	}
	
	public static Subscriber reserveSubscriber(int ban, String serialNumber, String IMSI, String[] secondarySerialNumbers) throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(ban);
		Subscriber reservedSubscriber=null;
		if (IMSI==null)
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, secondarySerialNumbers, false, null, "EN");
		else
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(IMSI, secondarySerialNumbers, false, null, "EN");
		
		NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups();
		NumberGroup numberGroup = null;
		for (int i=0; i < numberGroups.length; i++) {
			//System.out.println("NumberGroup " + i + ": " + numberGroups[i]);
			if (numberGroups[i].getCode().equals("TOR")){
				numberGroup = numberGroups[i];
				break;
			}
		}		
		PhoneNumberReservation pnr = accountManager.newPhoneNumberReservation();
		pnr.setNumberGroup(numberGroup);
		pnr.setPhoneNumberPattern("416");
		AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
		// take first one
		pnr.setPhoneNumberPattern(phoneNumbers[0].getPhoneNumber());
		reservedSubscriber.reservePhoneNumber(pnr);
		return reservedSubscriber;
	}
	
	public static Subscriber reserveSubscriber(int ban, String serialNumber, String IMSI, String IMEI) throws TelusAPIException {
		Account account = accountManager.findAccountByBAN(ban);
		Subscriber reservedSubscriber=null;
		if (IMSI==null)
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, IMEI, false, null, "EN");
		else
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(IMSI, IMEI, false, null, "EN");
		
		NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups();
		NumberGroup numberGroup = null;
		for (int i=0; i < numberGroups.length; i++) {
			//System.out.println("NumberGroup " + i + ": " + numberGroups[i]);
			if (numberGroups[i].getCode().equals("TOR")){
				numberGroup = numberGroups[i];
				break;
			}
		}		
		PhoneNumberReservation pnr = accountManager.newPhoneNumberReservation();
		pnr.setNumberGroup(numberGroup);
		pnr.setPhoneNumberPattern("416");
		AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
		// take first one
		pnr.setPhoneNumberPattern(phoneNumbers[0].getPhoneNumber());
		reservedSubscriber.reservePhoneNumber(pnr);
		return reservedSubscriber;
	}
	
	public String partiallyReserveSubscriber(Account account, String serialNumber, String IMSI) throws TelusAPIException {
		Subscriber reservedSubscriber=null;
		if (IMSI==null)
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, false, "EN");
		else
			reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, IMSI, false, null,  "EN");
	
		NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups();
		NumberGroup numberGroup = null;
		for (int i=0; i < numberGroups.length; i++) {
			if (numberGroups[i].getCode().equals("TOR")){
				numberGroup = numberGroups[i];
				break;
			}
		}		
		PhoneNumberReservation pnr = accountManager.newPhoneNumberReservation();
		pnr.setNumberGroup(numberGroup);
		pnr.setPhoneNumberPattern("416");
		AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
		// take first one
		return phoneNumbers[0].getPhoneNumber();
	}
	
	public static void activateSubscriber(Subscriber restoredSubscriber) throws TelusAPIException {
		System.out.println("Subscriber Retrieved from disk.");

		// Setup mandatory info for save
		restoredSubscriber.setFirstName("test1a");
		restoredSubscriber.setLastName("API");
		restoredSubscriber.setDealerCode("A001000001");
		restoredSubscriber.setSalesRepId("0000");
		restoredSubscriber.setLanguage("EN");
		restoredSubscriber.setBirthDate(new Date(1963, 01, 01));
		restoredSubscriber.setEmailAddress("api.test@telus.com");
	      
		System.out.println("Subscriber restored. Account type is  " + 
				restoredSubscriber.getAccount().getAccountType() + "/" + 
				restoredSubscriber.getAccount().getAccountSubType());
		
		PricePlanSummary[] plans = restoredSubscriber.getAvailablePricePlans();
	      
		for (int i=0; i<plans.length; i++) {
			System.out.println("Plan # " + i + ": " + plans[i].getCode() + " Supported Network Types --> [");
			String[] networkTypes = ((TMServiceSummary)plans[i]).getDelegate().getAllNetworkTypes();
			for (int j=0; j<networkTypes.length; j++)
				System.out.println(networkTypes[j]);
			System.out.println("]");
		}
	      
		PricePlan pricePlan = restoredSubscriber.getAvailablePricePlan(plans[0]);
		System.out.println("Plan #: " + pricePlan.getCode() + " Supported Network Types --> [");
		String[] networkTypes = ((TMServiceSummary)pricePlan).getDelegate().getAllNetworkTypes();
		for (int j=0; j<networkTypes.length; j++)
			System.out.println(networkTypes[j]);
		System.out.println("]");
    	  
		Contract contract = restoredSubscriber.newContract(pricePlan, 12);
	      
		System.out.println("Restored Subscriber: " + restoredSubscriber);      
	      
		restoredSubscriber.save(true);
	      
	}
	
}
