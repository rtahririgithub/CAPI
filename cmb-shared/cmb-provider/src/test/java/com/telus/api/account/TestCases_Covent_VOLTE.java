package com.telus.api.account;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.portability.PortRequest;
import com.telus.api.portability.PortRequestAddress;
import com.telus.api.portability.PortRequestName;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.provider.account.TMMigrationRequest;
import com.telus.provider.account.TMSubscriber;

public class TestCases_Covent_VOLTE extends BaseTest {

	static {
		//localhostWithPT148Ldap();
		setupCHNLECA_QA();
		//		System.setProperty("cmb.services.AccountLifecycleFacade.url",
		//				"t3://localhost:7001");
		//		System.setProperty("cmb.services.AccountLifecycleManager.url",
		//				"t3://localhost:7001");
		//		System.setProperty("cmb.services.AccountInformationHelper.url",
		//				"t3://localhost:7001");
		//
		//		System.setProperty("cmb.services.SubscriberLifecycleHelper.url",
		//				"t3://localhost:7001");
		//		System.setProperty("cmb.services.SubscriberLifecycleFacade.url",
		//				"t3://localhost:7001");
		//		System.setProperty("cmb.services.SubscriberLifecycleManager.url",
		//				"t3://localhost:7001");
	}

	public TestCases_Covent_VOLTE(String name) throws Throwable {
		super(name);
	}

	public void testWNP() throws Throwable {
		PortRequest portRequest = null;
		com.telus.api.account.Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161724670"); //originalPhoneNumber=4161724670
		((PCSSubscriber) subscriber).newPortRequest("6136138519", "C", true); //PortInNumber=6136138519, direction: 'C'
		portRequest = ((PCSSubscriber) subscriber).getPortRequest(); //PortRequest details pls refer to the attached screenshot, portRequest.DslInd = 'E'

		portRequest.setAlternateContactNumber("4161231234");
		// authorization name
		portRequest.setAgencyAuthorizationName("test");

		// authorization indicator
		portRequest.setAgencyAuthorizationIndicator("Y");
		// comment
		portRequest.setRemarks("");
		//wireline specific
		// address
		PortRequestAddress address = portRequest.getPortRequestAddress();
		address.setCity("scarborough");
		// country
		address.setCountry("CANADA");
		// postal code
		address.setPostalCode("M1S2Z6");
		// province
		address.setProvince("Ontario");
		// street direction
		address.setStreetDirection("");
		// street name
		address.setStreetName("lockdare st");
		// street number
		address.setStreetNumber("22");
		// name
		PortRequestName name = portRequest.getPortRequestName();
		name.setFirstName("Annie");
		// generation
		name.setGeneration(null);
		// initial
		name.setMiddleInitial("");
		// last name
		name.setLastName("XTEST");
		// title
		name.setTitle("MR");
		// business name
		portRequest.setBusinessName("XTEST");
		//dsl
		portRequest.setDslInd("E");

		portRequest.setDslLineNumber(1);
		portRequest.setEndUserMovingInd(false);
		portRequest.setOldReseller(null);
		portRequest.setOSPPin(null);

		portRequest.validate(); // validate portIn request
	}

	public void createPCSPostpaidBusinessConnectPersonal() throws Throwable {
		PCSPostpaidConsumerAccount account = api.getAccountManager().newPCSPostpaidConsumerAccount();
		setupAccount(account);
		account.save();
		checkCreditAndEligibility(account);
		System.out.println(account);
		assertNotNull(account);
	}

	/*test migration*/
	public void testnewMigrationRequest() throws Throwable {
		/**
		 * Variations of changing equipments between the different technology
		 * types.(includes LTE,1RTT,mike,ANA,etc..)
		 */

		System.out.println("Start  testnewMigrationRequest");
		IDENAccount idenAccount = (IDENAccount) api.getAccountManager().findAccountByBAN(70686363);
		//Account newPCSAccount = (Account) api.getAccountManager().newPCSAccount(idenAccount);
		Account account = api.getAccountManager().findAccountByBAN(70704698);
		//newPCSAccount.setDealerCode("0000485001");
		//newPCSAccount.setSalesRepCode("6632");

		/*setting priceplan code*/
		String pricePlanCode = "PVC55NAT ";
		/*setting the equipment*/
		Equipment newEquipment = api.getEquipmentManager().getEquipment("8912239900002078352");
		Equipment newAssociatedHandset = api.getEquipmentManager().getEquipment("900099990734074");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161774598");
		TMMigrationRequest mq = (TMMigrationRequest) ((IDENSubscriber) subscriber).newMigrationRequest(account, newEquipment, newAssociatedHandset, pricePlanCode);

		Subscriber newSubscriber = ((IDENSubscriber) subscriber).migrate(mq, "0000485001", "6632", "OOM");
		System.out.println("End  testnewMigrationRequest");
	}

	/*Test Activation*/
	public void testPCSConsumerActivationVolte() throws UnknownPhoneNumberException, TelusAPIException {
		//boolean isActivationWithDummySerilanumber,String  serialNumber
		String serialNumber = null;//"8912239900001804766";
		PCSAccount account = (PCSAccount) api.getAccountManager().findAccountByBAN(70701885);
		PCSSubscriber subscriber = null;

		System.out.println("start new PCS Consumer Regular Subscriber");

		//subscriber = account.newPCSBCSubscriber(SeatType.SEAT_TYPE_STARTER, null,serialNumber, false, null, "EN", null);
		subscriber = account.newPCSSubscriber("8912239900002075077", "900099990733574", false, null, null);
		//NARESH-ON-SC-Group
		//subscriber.setSeatData(setupStarterSeatData("TEST-GROUP1"));

		// add subscriber basic info
		subscriber.setFirstName("Test");
		subscriber.setLastName("Joe");
		subscriber.setLanguage("EN");
		subscriber.setDealerCode("0000000008");
		subscriber.setSalesRepId("0000");
		subscriber.setBirthDate(new Date(1983, 01, 01));
		subscriber.setEmailAddress("CoventTest@telus.com");
		subscriber.setActivityReasonCode("CMER");

		// Reserve phone number
		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType()));
		System.out.println("PhoneNumberReservation");

		NumberGroup numberGroup = null;
		for (int i = 0; i < numberGroups.length; i++) {
			System.out.println(numberGroups[i].getProvinceCode());
			if ("ON".equals(numberGroups[i].getProvinceCode())) {
				numberGroup = numberGroups[i];
			}
		}
		reservation.setNumberGroup(numberGroup);
		reservation.setAsian(true);
		reservation.setLikeMatch(false);
		reservation.setWaiveSearchFee(true);
		reservation.setPhoneNumberPattern("6471*******");
		subscriber.reservePhoneNumber(reservation);

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan("PXTALK40", String.valueOf(subscriber.getEquipment().getEquipmentType()), subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), Brand.BRAND_ID_TELUS);

		System.out.println("getPricePlan");

		Contract contract = subscriber.newContract(pricePlan, 12);

		System.out.println("newContract");
		subscriber.save(true);
	}

	/*Test Save Contract*/
	public void testSaveContactVolte() throws UnknownPhoneNumberException, TelusAPIException {
		System.out.println("Start testing contract save...");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161759909");
		assertNotNull(subscriber);

		System.out.println("Contract state at begining of Method: \n\t");
		printContract(subscriber.getContract(), ((TMSubscriber) subscriber).getEquipment0().getNetworkType());

		/*assign new equipment*/
		Equipment sim = api.getEquipmentManager().getEquipment("8912239900002075002");
		//Equipment equipment = api.getEquipmentManager().getEquipment("900099990733774");
		System.out.println(sim.isPCSHandset());
		//System.out.println(equipment.isPCSHandset());

		EquipmentChangeRequest equipmentChangeRequest = ((PCSSubscriber) subscriber).newEquipmentChangeRequest(sim, subscriber.getAccount().getDealerCode(), subscriber.getAccount().getSalesRepCode(),
				provider.getUser(), null, Subscriber.SWAP_TYPE_REPLACEMENT, true);

		subscriber.getContract().setEquipmentChangeRequest(equipmentChangeRequest);
		subscriber.getContract().save();

		System.out.println("\n\nContract state at end of method: \n\t");
		printContract(api.getAccountManager().findSubscriberByPhoneNumber("4161759909").getContract(),
				((TMSubscriber) api.getAccountManager().findSubscriberByPhoneNumber("4161759909")).getEquipment0().getNetworkType());
		System.out.println("end tests_for_save_contract_volte");
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
			for (int i = 0; i < inclSOCs.length; i++) {
				socNetworkType = inclSOCs[i].getService().getNetworkType();
				System.out.println("\tSOC #" + i + "- " + inclSOCs[i].getCode() + " -> NetworkType = " + socNetworkType);

				//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
				//equipment networkType
				if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
					assertEquals(subscriberNetworkType, socNetworkType);
			}
			System.out.println("\t]");

			System.out.println("\tOptional SOCs: [");
			for (int j = 0; j < optSOCs.length; j++) {
				socNetworkType = optSOCs[j].getService().getNetworkType();
				System.out.println("\tSOC #" + j + "- " + optSOCs[j].getCode() + " -> NetworkType = " + optSOCs[j].getService().getNetworkType());

				//Check to ensure SOC is valid for contract (i.e. SOC networkType must match subscribers 
				//equipment networkType
				if (!(subscriberNetworkType.equals(NetworkType.NETWORK_TYPE_ALL) || socNetworkType.equals(NetworkType.NETWORK_TYPE_ALL)))
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

	private static void setupAccount(PCSPostpaidConsumerAccount account) throws Throwable {
		setupAddress(account.getAddress());
		account.setEmail("thisistest@telus.com");
		account.setBrandId(Brand.BRAND_ID_TELUS);
		account.setPin("2222");
		account.setLanguage("EN");
		account.setDealerCode("0000000008");
		account.setSalesRepCode("0000");
		account.getContactName().setFirstName("YAEriche");
		account.getContactName().setLastName("YALIU");
		account.getName().setFirstName("Test");
		account.getName().setLastName("Joe");
		account.setHomePhone("4163506985");
		account.setContactPhone("4163203897");
	}

	private static void setupAddress(Address address) throws Throwable {
		address.setStreetNumber("171");
		address.setStreetName("Village Green Sq");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("M1S0G5");
		address.setCountry("CAN");
	}

	private void checkCreditAndEligibility(PostpaidAccount account) throws TelusAPIException, CreditCheckNotRequiredException {
		if (account.isPostpaidConsumer()) {
			((PCSPostpaidConsumerAccount) account).checkCredit(new AuditHeaderInfo());
		}
		account.checkNewSubscriberEligibility(1, 200);
	}

}