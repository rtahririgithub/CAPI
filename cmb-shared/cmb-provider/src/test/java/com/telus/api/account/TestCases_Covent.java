package com.telus.api.account;

import java.util.Date;

import org.junit.Assert;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
///import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMAccount;
import com.telus.provider.account.TMAccountManager;
import com.telus.provider.account.TMMigrationRequest;
import com.telus.provider.account.TMPCSSubscriber;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMCellularDigitalEquipment;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMEquipmentManager;

public class TestCases_Covent extends BaseTest  {

	private static ClientAPI api = null;
	private static TMProvider provider=null;
	private TMAccountManager accountManager;
	private TMEquipmentManager equipmentManager;
	TMEquipment tmequipment ;
	String LTE_serial_num = "900000001316563"; 
	int LTE_ban = 70653476; 
	String LTE_subscriber = "4031650837"; 


	public TestCases_Covent(String name) throws Throwable {
		super(name)	;
		accountManager = super.provider.getAccountManager0();
		equipmentManager =  (TMEquipmentManager) super.provider.getEquipmentManager0();
	}

	static {
		//setupEASECA_QA();
		//setupD3();
		//setupP();
		//setupCHNLECA_PT168();
		setupEASECA_PT168();
		try {
			System.out.println("Getting instance of ClientAPI...");
			api = ClientAPI.getInstance("18654", "apollo", "SMARTDESKTOP");
			provider = (TMProvider) api.getProvider();
			Thread.sleep(15000);
			System.out.println(new Date() + "All done!");
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			// api.destroy();
		}

	}


	public void testActivatePCSSubscriber() throws Throwable {

		System.out.println("*****  START OF TEST RUN : ActivatePCSSubscriber ****");

		// LTE Equipment serial number Galaxy note
		String serialNumber="900000001316623";
		int ban_id=70543182;

		//reserving the subscriber
		PCSSubscriber subscriber = (PCSSubscriber)reservePCSSubscriber(ban_id, serialNumber);
		// Setup mandatory info for save
		subscriber.setFirstName("Test");
		subscriber.setLastName("Covent");
		subscriber.setDealerCode("A001000001");
		subscriber.setSalesRepId("0000");
		subscriber.setBirthDate(new Date(1986, 01, 01));
		subscriber.setEmailAddress("testcovnet@telus.com");

		System.out.println(" Account type is  " +   subscriber.getAccount().getAccountType() + "/" +   subscriber.getAccount().getAccountSubType());

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
				"FETA100",
				String.valueOf(subscriber.getEquipment().getEquipmentType()),
				subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				Brand.BRAND_ID_TELUS);


		Contract contract = subscriber.newContract(pricePlan, 12);
		System.out.println("Save Subscriber");

		// flag true - activate subscriber 
		subscriber.save(true);

		System.out.println("Subscriber details saved successfully");

		System.out.println("***** END OF TEST  ActivatePCSSubscriber ****");

	}


	public static Subscriber reservePCSSubscriber(int ban, String serialNumber) throws TelusAPIException {

		String Province="ON";
		String NumberGroupCode="HFX";
		String PhoneNumberPattern="647112";

		Account account = api.getAccountManager().findAccountByBAN(ban);
		Subscriber reservedSubscriber = ((PCSAccount)account).newPCSSubscriber(serialNumber, false, "EN");
		NumberGroup[] numberGroups = reservedSubscriber.getAvailableNumberGroups(Province);
		NumberGroup numberGroup = null;
		for (int i=0; i < numberGroups.length; i++) {
			if (numberGroups[i].getCode().equals(NumberGroupCode)){
				numberGroup = numberGroups[i];
				break;
			}
		}		
		PhoneNumberReservation pnr = api.getAccountManager().newPhoneNumberReservation();
		pnr.setNumberGroup(numberGroup);
		pnr.setPhoneNumberPattern(PhoneNumberPattern);
		AvailablePhoneNumber[] phoneNumbers = reservedSubscriber.findAvailablePhoneNumbers(pnr, 10);
		// take first one
		pnr.setPhoneNumberPattern(phoneNumbers[0].getPhoneNumber());
		reservedSubscriber.reservePhoneNumber(pnr);
		return reservedSubscriber;
	}

	public void testNewPCSPrepaidConsumerAccount() throws UnknownSerialNumberException, SerialNumberInUseException, InvalidSerialNumberException, InvalidActivationCodeException, InvalidCreditCardException, TelusAPIException{
		System.out.println("start testNewPCSPrepaidConsumerAccount");
		String serialNumber = "8912230000199255696";
		int activationType = 0;
		String activationCode = "AIE";
		CreditCard creditCard = null;
		String businessRole = null;
		boolean isFidoConversion = false;
		AuditHeader auditHeader = null;
		PCSPrepaidConsumerAccount ppca = accountManager.newPCSPrepaidConsumerAccount(serialNumber, activationType, activationCode, creditCard, businessRole, isFidoConversion, auditHeader);
		assertEquals('D', ppca.getAccountType());

		String associatedHandsetIMEI = "900000001316603";
		double activationAmount = 0;
		PCSPrepaidConsumerAccount  ppca1 = accountManager.newPCSPrepaidConsumerAccount(serialNumber, associatedHandsetIMEI, activationType, activationCode, creditCard, businessRole, activationAmount, auditHeader);
		assertEquals('D', ppca1.getAccountType());
		System.out.println("end testNewPCSPrepaidConsumerAccount");
	}

	public void testFindAccountsBySerialNumber() throws TelusAPIException,SecurityException, NoSuchMethodException {

		/** Retrieve existing Accounts has  LTE equipments. */
		AccountSummary[] ai = accountManager.findAccountsBySerialNumber("900000001316563");

		assertEquals(1, ai.length);
		assertEquals(70653476, ai[0].getBanId());

	}
	public void testfindSubscribersBySerialNumber() throws TelusAPIException,SecurityException, NoSuchMethodException {
		/** Retrieve existing Subscribers has  LTE equipments. */
		Subscriber[] ai = accountManager.findSubscribersBySerialNumber("900000001316563");

		assertEquals(1, ai.length);
		assertEquals("4031650837", ai[0].getPhoneNumber());
	}

	

	public void testEquipmentManagerMethods() throws SecurityException,NoSuchMethodException, UnknownSubscriberException,	
	TelusAPIException {
		/** * testing specifically LTE devices Retrieve equipments has Technology type ' LTE ' . i.e TMCellularDigitalEquipment . **/
		String LTE_serial_num = "900000001316563"; String LTE_subscriber = "4031650837"; 
		// getEquipment by Serial number
		TMCellularDigitalEquipment tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.getEquipment(LTE_serial_num);			
		assertEquals("H", tmCellularDigitalEquipment.getDelegate().getNetworkType());	
		assertEquals("LTE", tmCellularDigitalEquipment.getTechType());

		// getEquipment by Serial number include checkPseudoESN flag
		tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.getEquipment(LTE_serial_num, false);				
		assertEquals("LTE", tmCellularDigitalEquipment.getTechType());
		assertEquals("H", tmCellularDigitalEquipment.getDelegate().getNetworkType());

		// getEquipmentByPhoneNumber
		tmequipment = (TMCellularDigitalEquipment) equipmentManager.getEquipmentByPhoneNumber("5194378223");	

		assertEquals("H", tmequipment.getDelegate().getNetworkType());
		assertEquals("LTE", tmCellularDigitalEquipment.getTechType());

		// getEquipmentByProductCode
		tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.getEquipmentbyProductCode("LLGP935");			
		assertEquals("LTE", tmCellularDigitalEquipment.getTechType());
		assertEquals("H", tmCellularDigitalEquipment.getDelegate().getNetworkType());

		// validate LTE SerialNumber
		tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.validateSerialNumber(LTE_serial_num);			
		assertEquals("LTE", tmCellularDigitalEquipment.getTechType());
		assertEquals("H", tmCellularDigitalEquipment.getDelegate().getNetworkType());
	}

	public void testNewContract() throws  Throwable  {

		/**  test newContract for LTE Subscriber*/
		System.out.println(" *** START testNewContract  ***");
		TMAccount account =(TMAccount) accountManager.findAccountByBAN0(70653476);
		TMSubscriber subscriber = (TMSubscriber)account.getSubscriberByPhoneNumber("4031650837");

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
				"PYCDB30B4",
				String.valueOf(subscriber.getEquipment().getEquipmentType()),
				subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				Brand.BRAND_ID_TELUS);

		Contract contract  = subscriber.newContract(pricePlan, 12);
		System.out.println("COntact "+contract.getCommitmentMonths());
		System.out.println(" *** END testNewContract ***");

	}

	public void testChangeEquipment() throws Throwable 
	{
		System.out.println(" *** START testChangeEquipment  ***");

		String new_equipment_serial = "8912230000199256280";
		EquipmentInfo oldPrimaryEquipmentInfo = new EquipmentInfo();
		oldPrimaryEquipmentInfo.setSerialNumber("8912230000199256272");

		EquipmentInfo newPrimaryEquipmentInfo = new EquipmentInfo();
		newPrimaryEquipmentInfo.setSerialNumber("8912230000199256280");
		newPrimaryEquipmentInfo.setAssociatedHandsetIMEI("900000001317363");

		//	Equipment newEquipment = equipmentManager.getEquipment(new_equipment_serial);
		//EquipmentInfo newEquipmentInfo =(EquipmentInfo)newEquipment;
		//newEquipmentInfo.setAssociatedHandsetIMEI("900000001317403");
		Equipment newEquipment  = (Equipment)newPrimaryEquipmentInfo;
		TMAccount account =(TMAccount) accountManager.findAccountByBAN0(70653552);
		TMSubscriber subscriber = (TMSubscriber)account.getSubscriberByPhoneNumber("6471124953");
		String dealerCode ="A001000001";
		String salesRepCode ="0000";
		String requestorId = "tester";
		String swapType = "REPLACE";
		String repairId = "123456";
		subscriber.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
		//subscriber.ch
		System.out.println(" *** END testChangeEquipment  ***");

	}

	public void testRetrieveSubscriber() throws Throwable 
	{
		System.out.println(" *** START testRetrieveSubscriber  ***");
		TMAccount account =(TMAccount) accountManager.findAccountByBAN0(LTE_ban);
		TMSubscriber tmsubscriber = (TMSubscriber)account.getSubscriberByPhoneNumber(LTE_subscriber);
		Subscriber subscriber = tmsubscriber.retrieveSubscriber("LTE_subscriber");
		assertEquals("LTE",subscriber.getEquipment().getTechType());
		System.out.println(" *** END testRetrieveSubscriber  ***");
	}	

	public void testnewMigrationRequest() throws Throwable 
	{ 
		/**
		 * Variations of changing equipments between the different technology
		 * types.(includes LTE,1RTT,mike,ANA,etc..)
		 */

		System.out.println("Start  testnewMigrationRequest");
		Account newAccount=api.getAccountManager().findAccountByBAN(70653531);
		String pricePlanCode="TXPBAPDA2";
		Equipment newEquipment =api.getEquipmentManager().getEquipment("900000001316593");
		Equipment newAssociatedHandset = null;// =api.getEquipmentManager().getEquipment("900000000617764");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("6471124964");
		TMMigrationRequest tm = (TMMigrationRequest)((TMSubscriber)subscriber).newMigrationRequest(newAccount, newEquipment, newAssociatedHandset, pricePlanCode);

		System.out.println("End  testnewMigrationRequest");
	}
	private NumberGroup findNumberGroupbyPhoneNumber(Subscriber subscriber) throws TelusAPIException {

		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(
				subscriber.getAccount().getAccountType(), 
				subscriber.getAccount().getAccountSubType(), 
				subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType()));
		if (numberGroups.length == 0)
			throw new RuntimeException("Unable to find the NumberGroup for[*****]");				
		return numberGroups[1];
	}
	private AvailablePhoneNumber findAvailablePhoneNumber(Subscriber subscriber) throws Exception {
		PhoneNumberReservation reservation = api.getAccountManager().newPhoneNumberReservation();

		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(
				subscriber.getAccount().getAccountType(), 
				subscriber.getAccount().getAccountSubType(), 
				subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType()));

		NumberGroup numberGroup = null;
		AvailablePhoneNumber[] nums = null;
		for (int i = 0; i < numberGroups.length; i++) {
			if ("ON".equals(numberGroups[i].getProvinceCode())) {
				reservation.setNumberGroup(numberGroups[i]);
				reservation.setAsian(false);
				reservation.setLikeMatch(false);
				reservation.setWaiveSearchFee(true);
				reservation.setPhoneNumberPattern("****");
				reservation.setProductType(subscriber.getProductType());

				try {
					nums = subscriber.findAvailablePhoneNumbers(reservation, 10);
					if (nums != null) {
						for (int j = 0; j < nums.length; j++) {
							System.out.println(nums[j]);
							return nums[j];
						}
						break;
					}
				} catch (PhoneNumberException pne) {
				}
			}
		}
		return null;
	}
	public void createSubscriber() throws Throwable
	{
		System.out.println("*******  started createSubscriber method   *******  ");


		String reason = "ACOR";
		String phNum = "4031654622"; 
		String serialNumber = "900000001316623"; 


		PCSPostpaidConsumerAccount telusAccount = (PCSPostpaidConsumerAccount)accountManager.findAccountByBAN0(70614284);
		PCSSubscriber telusPcsSubscriber   = telusAccount.newPCSSubscriber(serialNumber, true, "EN");

		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup numbergroup = findNumberGroupbyPhoneNumber(telusPcsSubscriber);
		reservation.setNumberGroup(numbergroup);
		reservation.setPhoneNumberPattern(findAvailablePhoneNumber(telusPcsSubscriber).getPhoneNumber()); // setting the available phNum for porting
		telusPcsSubscriber.setFirstName("YANaresh");
		telusPcsSubscriber.setLastName("YADoe");
		telusPcsSubscriber.setLanguage("EN");
		telusPcsSubscriber.setDealerCode("0000000008");
		telusPcsSubscriber.setSalesRepId("0000");
		telusPcsSubscriber.setBirthDate(new Date(1960, 01, 01));
		telusPcsSubscriber.setEmailAddress("Coventtest@telusmobility.com");
		telusPcsSubscriber.setActivityReasonCode("CMER");
		//telusPcsSubscriber.reservePhoneNumber(reservation);

		telusPcsSubscriber.save(true);

		System.out.println("*******  ended createSubscriber method   *******  ");
	}

	public void tests_for_DUMMY_ESN_FOR_USIM()throws UnknownPhoneNumberException, TelusAPIException {

		System.out.println("start tests_for_DUMMY_ESN_FOR_USIM");
		String serialNumber = EquipmentInfo.DUMMY_ESN_FOR_USIM;

		// test 1
		try {
			accountManager.findSubscribersBySerialNumber(serialNumber);
			fail("Exception Excepted");
		} catch (Exception e) {
			assertEquals(": Cannot use dummy USIM number for retrieveSubscriberListBySerialNumber",e.getMessage().trim());
		}
		// test 2
		try {
			accountManager.findSubscribersBySerialNumber(serialNumber, true);
			fail("Exception Excepted");
		} catch (Exception e) {
			assertEquals(": Cannot use dummy USIM number for retrieveSubscriberListBySerialNumber",e.getMessage().trim());
		}
		System.out.println("end tests_for_DUMMY_ESN_FOR_USIM");

	}
	
	public void testProductionDefectBeforeFix_PN0000011046752() throws TelusAPIException,SecurityException, NoSuchMethodException {

		tmequipment = (TMCellularDigitalEquipment) equipmentManager.getEquipmentByPhoneNumber("5194378223");
		assertEquals(false,tmequipment.getDelegate().isDataCard() );

		TMCellularDigitalEquipment tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.getEquipment("268435458402142682");					
		assertEquals(false,tmCellularDigitalEquipment.getDelegate().isDataCard() );
	}

	public void testProductionDefectAfterFix_PN0000011046752() throws TelusAPIException,SecurityException, NoSuchMethodException {

		tmequipment = (TMCellularDigitalEquipment) equipmentManager.getEquipmentByPhoneNumber("5194378223");
		assertEquals(false,tmequipment.getDelegate().isDataCard() );

		TMCellularDigitalEquipment tmCellularDigitalEquipment = (TMCellularDigitalEquipment) equipmentManager.getEquipment("268435458402142682");					
		assertEquals(false,tmCellularDigitalEquipment.getDelegate().isDataCard() );
	}
	
	 private void saveContactVolte() throws Throwable
	 {  	
		System.out.println("Start testing contract save...");
	    Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161759909");
	    assertNotNull(subscriber);
	    	
	    System.out.println("Contract state at begining of Method: \n\t");
	    printContract(subscriber.getContract(), ((TMSubscriber)subscriber).getEquipment0().getNetworkType());
	    	
	    /*assign new equipment*/
	    Equipment sim = api.getEquipmentManager().getEquipment("8912239900000629248");
	    Equipment equipment = api.getEquipmentManager().getEquipment("900099990733774");
	    System.out.println(sim.isPCSHandset());
	    System.out.println(equipment.isPCSHandset());
	    			
	    EquipmentChangeRequest equipmentChangeRequest = ((PCSSubscriber)subscriber).newEquipmentChangeRequest(sim, equipment, subscriber.getAccount().getDealerCode(), subscriber.getAccount().getSalesRepCode(),
	    				provider.getUser(), null, Subscriber.SWAP_TYPE_REPLACEMENT, true);	
	    	
	    subscriber.getContract().setEquipmentChangeRequest(equipmentChangeRequest);
	    subscriber.getContract().save();
	    	
	    System.out.println("\n\nContract state at end of method: \n\t");
		printContract(api.getAccountManager().findSubscriberByPhoneNumber("4161759909").getContract(), ((TMSubscriber)api.getAccountManager().findSubscriberByPhoneNumber("4161759909")).getEquipment0().getNetworkType());
		System.out.println("end tests_for_save_contract_volte");
	  }
	 
		public void testKoodoBrandOnTelusEquip() throws SecurityException, NoSuchMethodException, UnknownSubscriberException, TelusAPIException {
			String telusEquipSerialNumber = "900931423989683";
			long subscriptionId = 8392448;
			boolean exceptionThrown = false;

			// validate no exception when Koodo brand sub gets a TELUS brand equipment
			try {
				equipmentManager.validateSerialNumber(telusEquipSerialNumber, Brand.BRAND_ID_KOODO, subscriptionId);
			} catch (UnknownSerialNumberException e) {
				System.out.println(e);
				exceptionThrown = true;
			}
			Assert.assertFalse(exceptionThrown);
		}	 
	 
	 private void printContract(Contract c, String subscriberNetworkType) {
		  
	    	//Ensure contract was provided
	    	assertNotNull(c);
	    	
	    	ContractService[] inclSOCs = c.getIncludedServices();
	    	ContractService[] optSOCs = c.getOptionalServices();
	    	String socNetworkType;

	    	try {
	    		System.out.println("PricePlan on contract is: [" + c.getPricePlan().getCode() + "]");
	    	
	    		System.out.println("\tIncluded SOCs: [");
	    		for(int i =0; i < inclSOCs.length; i++) { 
	    			socNetworkType = inclSOCs[i].getService().getNetworkType();
	    			System.out.println("\tSOC #" + i + "- " + inclSOCs[i].getCode() + 
	    						   " -> NetworkType = " + socNetworkType);
	    		
	    			//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
	    			//equipment networkType
	    			if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || 
	    					socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
	    				assertEquals(subscriberNetworkType, socNetworkType);
	    		}
	    		System.out.println("\t]");
	    	
	    	
	    		System.out.println("\tOptional SOCs: [");
	    		for(int j =0; j < optSOCs.length; j++) { 
	    			socNetworkType = optSOCs[j].getService().getNetworkType();    	
	    			System.out.println("\tSOC #" + j + "- " + optSOCs[j].getCode()+ 
	    					" -> NetworkType = " + optSOCs[j].getService().getNetworkType());
	    		
	    			//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
	    			//equipment networkType
	    			if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || 
	    					socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
	    				assertEquals(subscriberNetworkType, socNetworkType);
	    		}
	    		System.out.println("\t]");
	    	
	    		System.out.println("\nEquipmentChangeRequest on contract is: ");
	    		if (c.getEquipmentChangeRequest() != null)
	    			System.out.println("\tserial no --> " + c.getEquipmentChangeRequest().getNewEquipment().getSerialNumber());
	    		else
	    			System.out.println("\tserial no --> N/A");
	    	} catch (TelusAPIException t) {
	    		System.out.println("Error encountered in printContract(): [");
	    		t.printStackTrace();
	    		System.out.println("]");
	    	}
	    }
}
