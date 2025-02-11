package com.telus.api.account;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.telus.api.ApplicationException;
import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.message.ApplicationMessage;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.api.reference.SeatType;
import com.telus.api.reference.Service;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.provider.account.TMContract;
import com.telus.provider.account.TMPCSSubscriber;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMEquipment;


public class BusinessConnectSubscriberLifeCycleTests extends BaseTest {

	static {
		// Setup Environment
		// setupD1();
		// setupEASECA_D3();
		// setupCHNLECA_PT168();
		setupEASECA_CSI();
		// localhostWithPT148Ldap();
		// localhostWithPT168Ldap();
		// setupCHNLECA_QA();
		// setupP();
		//setupT();
//
		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7140");
		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7140");
		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7140");
		System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7140");
		System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7140");
		System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7140");
		// System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentHelper.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentManager.url", "t3://localhost:7001");
		// System.setProperty("cmb.services.ProductEquipmentLifecycleFacade.url", "t3://localhost:7001");
	}

	public BusinessConnectSubscriberLifeCycleTests(String name) throws Throwable {
		super(name);
	}

	private void testActivationSimOnly() {
		try {
			testCreatePCSPostpaidBusinessConnectPersonal();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void testCreatePCSPostpaidBusinessConnectPersonal() throws Throwable {
		PCSPostpaidConsumerAccount account = api.getAccountManager().newPCSPostpaidBusinessPersonalAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL);
		setupAccount(account);
		account.save();
		System.out.println("Account BAN = " + account.getBanId());
		checkCreditAndEligibility(account);
		assertNotNull(account);
	}

	public void testCreatePCSPostpaidBusinessConnectRegular() throws Throwable {
		PostpaidBusinessRegularAccount account = api.getAccountManager().newPCSPostpaidBusinessRegularAccount(AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_REGULAR);
		setupAccount(account);
		account.save();
		System.out.println("Account BAN = " + account.getBanId());
		checkCreditAndEligibility(account);
		assertNotNull(account);
	}

	private void checkCreditAndEligibility(PostpaidAccount account) throws TelusAPIException, CreditCheckNotRequiredException {
		if (account.isPostpaidConsumer() || account.isPostpaidBusinessPersonal() || account.isPostpaidCorporatePersonal() || account.isPostpaidEmployee()) {
			((PostpaidConsumerAccount) account).checkCredit(new AuditHeaderInfo());
		} else {
			PostpaidBusinessRegularAccount businessAccount = (PostpaidBusinessRegularAccount) account;
			businessAccount.checkCredit(businessAccount.getBusinessCreditIdentities()[0]);
		}
		account.checkNewSubscriberEligibility(1, 200);
	}

	private SeatData setupStarterSeatData(String group) throws TelusAPIException {
		SeatData seatData = api.getAccountManager().newSeatData();
		seatData.setSeatGroup(group);
		seatData.setSeatType(SeatType.SEAT_TYPE_STARTER);
		//seatData.addSeatResource(Subscriber.RESOURCE_TYPE_HSIA, "4444458993");
		seatData.addSeatResource(Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP, "7786500944");
		seatData.addSeatResource(Subscriber.RESOURCE_TYPE_PRIMARY_VOIP, "7786503209");
		return seatData;
	}

	private SeatData setupOfficeSeatData(String group) throws TelusAPIException {
		SeatData seatData = api.getAccountManager().newSeatData();
		seatData.setSeatGroup(group);
		seatData.setSeatType(SeatType.SEAT_TYPE_OFFICE);
		seatData.addSeatResource(Subscriber.RESOURCE_TYPE_PRIMARY_VOIP, "6471200403");
		return seatData;
	}

	private SeatData setupProfessionlaSeatData(String group) throws TelusAPIException {
		SeatData seatData = api.getAccountManager().newSeatData();
		seatData.setSeatGroup(group);
		seatData.setSeatType(SeatType.SEAT_TYPE_PROFESSIONAL);
		seatData.addSeatResource(Subscriber.RESOURCE_TYPE_PRIMARY_VOIP, "6471251390");
		return seatData;
	}

	private SeatData setupMobileSeatData(String group) throws TelusAPIException {
		SeatData seatData = api.getAccountManager().newSeatData();
		seatData.setSeatGroup(group);
		seatData.setSeatType(SeatType.SEAT_TYPE_MOBILE);
		return seatData;
	}

	public void testUpgradeServiceEdition() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781811475");
		ContractService[] services = subscriber.getContract().getOptionalServices();
		System.out.println("Subscriber information [ " + subscriber.getPricePlan() + ", " + subscriber.getBrandId() + ", " + subscriber.getBanId() + "]");
		for (ContractService service : services) {
			System.out.println("Contract Service [" + service + "]");
		}
		Service service = api.getReferenceDataManager().getRegularService("XSPES80M");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 240);
		subscriber.getContract().addService(service, cal.getTime(), null);
		subscriber.getContract().save();
	}

	/**
	 * 0PSTW STRT Main Offline Line - West PERSONAL 0PSTE STRT Main Offline Line
	 * - East PERSONAL 0POF1 OFFC Office Plan PERSONAL 0POF2 OFFC Office Plan
	 * Leased Model PERSONAL 0PMOB1 MOBL Mobile Plan - High Value PERSONAL
	 * 0PMOB4 MOBL Mob Plan - Tablet SMB 0PMOB6 MOBL Mob Plan - Smart Hub 0PMOB7
	 * MOBL Mob Plan-Additional HSIA Conec PERSONAL 0PMOB2 MOBL Mob Plan -
	 * SmartPhone Lite PERSONAL 0PMOB3 MOBL Mob Plan - Lite PERSONAL 0PPRO PROF
	 * Professional Plan PERSONAL 0PMOB5 MOBL Business Connect MIK SMB
	 **/
	@SuppressWarnings("deprecation")
	public void testCreatePCSBCSubscriber() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {

		int ban = 70813879;// 70713215;
		String npanxx = "778152";// 778760";
		// reservation.setPhoneNumberPattern("778760*****");
		// reservation.setPhoneNumberPattern("6471*******");

		PCSAccount account = (PCSAccount) api.getAccountManager().findAccountByBAN(ban);

		System.out.println("Start new PCS Business Connect Subscriber");
		PCSSubscriber subscriber = account.newPCSBCSubscriber(SeatType.SEAT_TYPE_STARTER, null, null, false, null, "EN", null);
		updateSubscriberInfo(subscriber, "Defect-Test", "YABCTestCase", "EN", "0000000008", "0000", new Date(1986, 01, 01), "x146449@telus.com", "CMER", "TestBCJuly2018-AT");

		System.out.println("PhoneNumberReservation");
		PhoneNumberReservation reservation = createReservation(subscriber, npanxx, false, false, true);
		try {
			subscriber.reservePhoneNumber(reservation);
		} catch (NumberMatchException e) {
			reservation.setAsian(true);
			subscriber.reservePhoneNumber(reservation);
		}

		System.out.println("getPricePlan");
		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan("PBCE170 ", String.valueOf(subscriber.getEquipment().getEquipmentType()), subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), Brand.BRAND_ID_TELUS);

		System.out.println("newContract");
		Contract contract = subscriber.newContract(pricePlan, Subscriber.TERM_MONTH_TO_MONTH);
		Service bcLicense  = api.getReferenceDataManager().getRegularService("SBCLM100");
		Service bcLicense2  = api.getReferenceDataManager().getRegularService("SBCLM300 ");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, 240);
		contract.addService(bcLicense,cal.getTime(), null);
		contract.addService(bcLicense2,cal.getTime(), null);
		subscriber.save(true);
		subscriber.setAddress(getSubscriberAddress());
	}

	private PhoneNumberReservation createReservation(PCSSubscriber subscriber, String npanxx, boolean asian, boolean likeMatch, boolean waiveSearchFee)
			throws TelusAPIException, UnknownSerialNumberException {
		PhoneNumberReservation reservation = api.getAccountManager().newPhoneNumberReservation();
		reservation.setNumberGroup(getNumberGroup(subscriber, npanxx));
		reservation.setAsian(false);
		reservation.setLikeMatch(false);
		reservation.setWaiveSearchFee(true);
		reservation.setPhoneNumberPattern(npanxx + "****");
		return reservation;
	}

	private NumberGroup getNumberGroup(PCSSubscriber subscriber, String npanxx) throws TelusAPIException, UnknownSerialNumberException {
		NumberGroup selectedNumberGroup = null;
		NumberGroup[] numberGroups = api.getReferenceDataManager().getNumberGroups(subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), subscriber.getProductType(),
				String.valueOf(subscriber.getEquipment().getEquipmentType()));
		for (NumberGroup numberGroup : numberGroups) {
			if (Arrays.asList(numberGroup.getNpaNXX()).contains(npanxx)) {
				selectedNumberGroup = numberGroup;
				break;
			}
		}
		return selectedNumberGroup;
	}

	private void updateSubscriberInfo(PCSSubscriber subscriber, String firstName, String lastName, String language, String dealerCode, String salesRepId, Date birthDate, String emailAddress,
			String activityReasonCode, String seatDataGroup) throws TelusAPIException {

		// add subscriber basic info
		subscriber.setFirstName(firstName);
		subscriber.setLastName(lastName);
		subscriber.setLanguage(language);
		subscriber.setDealerCode(dealerCode);
		subscriber.setSalesRepId(salesRepId);
		subscriber.setBirthDate(birthDate);
		subscriber.setEmailAddress(emailAddress);
		subscriber.setActivityReasonCode(activityReasonCode);
		// add subscriber address : for bc subscribers seat address is different than account address.
		subscriber.setAddress(getSubscriberAddress());
		subscriber.setSeatData(setupStarterSeatData(seatDataGroup));
	}

	public void createPCSBCSubscriber() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {

		// boolean isActivationWithDummySerilanumber,String serialNumber
		String serialNumber = "8912230000002552784";
		PCSAccount account = (PCSAccount) api.getAccountManager().findAccountByBAN(70704048);
		PCSSubscriber subscriber = null;

		System.out.println("start new PCS Business Connect Subscriber");
		subscriber = account.newPCSBCSubscriber(SeatType.SEAT_TYPE_STARTER, null, serialNumber, false, null, "EN", null);
		subscriber.setSeatData(setupStarterSeatData("Group2025"));

		// add subscriber basic info
		subscriber.setFirstName("Defect-Test");
		subscriber.setLastName("YABCTestCase");
		subscriber.setLanguage("EN");
		subscriber.setDealerCode("0000000008");
		subscriber.setSalesRepId("0000");
		subscriber.setBirthDate(new Date(1986, 01, 01));
		subscriber.setEmailAddress("new-seat-defect@telus.com");
		subscriber.setActivityReasonCode("CMER");

		// add subscriber address : for bc subscribers seat address is differnt
		// than account address.
		subscriber.setAddress(getSubscriberAddress());

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

		/**
		 * 0PSTW STRT Main Offline Line - West PERSONAL 0PSTE STRT Main Offline
		 * Line - East PERSONAL 0POF1 OFFC Office Plan PERSONAL 0POF2 OFFC
		 * Office Plan Leased Model PERSONAL 0PMOB1 MOBL Mobile Plan - High
		 * Value PERSONAL 0PMOB4 MOBL Mob Plan - Tablet SMB 0PMOB6 MOBL Mob Plan
		 * - Smart Hub 0PMOB7 MOBL Mob Plan-Additional HSIA Conec PERSONAL
		 * 0PMOB2 MOBL Mob Plan - SmartPhone Lite PERSONAL 0PMOB3 MOBL Mob Plan
		 * - Lite PERSONAL 0PPRO PROF Professional Plan PERSONAL 0PMOB5 MOBL
		 * Business Connect MIK SMB
		 **/

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan("0PSTE    ", String.valueOf(subscriber.getEquipment().getEquipmentType()), subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(), subscriber.getAccount().getAccountSubType(), Brand.BRAND_ID_TELUS);

		System.out.println("getPricePlan");

		Contract contract = subscriber.newContract(pricePlan, Subscriber.TERM_MONTH_TO_MONTH);

		System.out.println("newContract");
		subscriber.save(true);
		subscriber.setAddress(getSubscriberAddress());
	}

	public void testRetrieveBUsinessConnectSubscriber() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		// 6471251999 6471251816
		PhoneNumberSearchOption phoneNumberSearchOption = api.getAccountManager().newPhoneNumberSearchOption();
		phoneNumberSearchOption.setSearchVOIP(true);
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781768661");

		System.out.println(subscriber.getEmailAddress());
	}

	public void activateReservedSubscriber() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("6471251812");
		SeatData seatData = api.getAccountManager().newSeatData();
		seatData.setSeatGroup("NARESH-ON-SC-Group1");
		seatData.setSeatType(SeatType.SEAT_TYPE_STARTER);
		subscriber.setSeatData(seatData);
		subscriber.activate("CMER");
	}

	public void testSuspendStarterSeat() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		String phoneNumber = "7781780329";
		System.out.println("start suspend seat for subscriber");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.suspend("AIE");
		System.out.println("end suspend seat for subscriber");
	}

	public void testSuspendAccount() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		int ban = 70701880;
		System.out.println("start suspend business connect account ");
		Account account = api.getAccountManager().findAccountByBAN(ban);
		account.suspend("AIE");
		System.out.println("end suspend business connect account ");
	}

	public void testResumeAccount() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		int ban = 70701880;
		System.out.println("start suspend business connect account ");
		Account account = api.getAccountManager().findAccountByBAN(ban);
		// account.p
		System.out.println("end suspend business connect account ");
	}

	public void testResumeStarterSeatFromSuspendedState() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		String phoneNumber = "6471251838";
		System.out.println("start suspend seat for subscriber");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.restore(" ");
		System.out.println("end suspend seat for subscriber");
	}

	public void testResumenonStarterSeatFromSuspendedState() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		String phoneNumber = "6471251833";
		System.out.println("start non-start suspend seat for subscriber");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.restore("CNPA");
		System.out.println("end non-start suspend seat for subscriber");
	}

	public void testRestoreSuspendedSeatsIncludingStarter() throws UnknownSubscriberException, BrandNotSupportedException, TelusAPIException {
		String[] phonenums = new String[] { "6471251812", "6471251900" };
		System.out.println("start suspend seat for subscriber");
		Account account = api.getAccountManager().findAccountByBAN(70701432);
		account.restoreSuspendedSubscribers(new Date(), "CNPA", phonenums, "test restore");
		System.out.println("end suspend seat for subscriber");
	}

	private static void setupAccount(PCSPostpaidConsumerAccount account) throws Throwable {
		setupAddress(account.getAddress());
		account.setEmail("naresh_bc2018test@telus.com");
		account.setBrandId(Brand.BRAND_ID_TELUS);
		account.setPin("1144");
		account.setLanguage("EN");
		account.setDealerCode("0000000008");
		account.setSalesRepCode("0000");
		account.getContactName().setFirstName("NAresh");
		account.getContactName().setLastName("YABCJULY2018");
		account.getName().setFirstName("Naresh");
		account.getName().setLastName("YABCJULY2018Test");
		account.setHomePhone("6474568599");
		account.setContactPhone("6474568599");
	}

	private static void setupAccount(PostpaidBusinessRegularAccount account) throws Throwable {
		setupAddress(account.getAddress());
		account.setEmail("naresh_clientapitest@telus.com");
		account.setBrandId(Brand.BRAND_ID_TELUS);
		account.setPin("2222");
		account.setLanguage("EN");
		account.setDealerCode("0000000008");
		account.setSalesRepCode("0000");
		account.getContactName().setFirstName("NAresh");
		account.getContactName().setLastName("YAnnabathula");
		account.setHomePhone("6474568599");
		account.setContactPhone("6474568599");
	}

	private Address getSubscriberAddress() {
		Address address = new AddressInfo();
		address.setStreetNumber("200");
		address.setStreetName("Consilium Pl");
		address.setCity("Scarborough");
		address.setProvince("ON");
		address.setPostalCode("M1H3E4");
		address.setCountry("CAN");
		return address;
	}

	private static void setupAddress(Address address) throws Throwable {
		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");
	}

	public void testWNP() throws Throwable {
		com.telus.api.portability.PortRequest portRequest = null;
		com.telus.api.account.Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("4161724670"); // originalPhoneNumber=4161724670
		((PCSSubscriber) subscriber).newPortRequest("6136138519", "C", true); // PortInNumber=6136138519, direction: 'C'
		
		// PortRequest details pls refer to the attached screenshot, portRequest.DslInd = 'E'
		portRequest = ((PCSSubscriber) subscriber).getPortRequest();
		portRequest.validate(); // validate portIn request
	}

	public void testretrieveAccount() throws Throwable {
		Account accountInfo = api.getAccountManager().findAccountByBAN(70701880);
		System.out.println("AccountInfo" + accountInfo.toString());
		AccountType accountType = api.getReferenceDataManager().getAccountType(accountInfo);
		System.out.println("Dealer Code " + accountType.getDefaultDealer());
		System.out.println("Sales Rep Id " + accountType.getDefaultSalesCode());
	}

	public void testBusinessConnectVADIssue() throws TelusAPIException {
		System.out.println("start testBusinessConnectVADIssue");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("6471251891");
		PricePlanSummary[] pricePlanSummary = subscriber.getSuspensionPricePlans("VAD ");
		for (PricePlanSummary pricePlanSummary2 : pricePlanSummary) {
			System.out.println(pricePlanSummary2.getCode());
		}
		System.out.println("end testBusinessConnectVADIssue");
	}

	public void testVOIPRetrieval() throws TelusAPIException {
		System.out.println("start testVOIPRetrieval");
		PhoneNumberSearchOption searchOption = api.getAccountManager().newPhoneNumberSearchOption();
		searchOption.setSearchVOIP(true);
		searchOption.setSearchWirelessNumber(true);
		searchOption.setSearchTollFree(true);
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781767962", searchOption);
		System.out.println(subscriber.getAddress());
		System.out.println("end testVOIPRetrival");
	}

	public void testWirelessRetrieval() throws TelusAPIException {
		System.out.println("start testWirelessRetrieval");
		PhoneNumberSearchOption searchOption = api.getAccountManager().newPhoneNumberSearchOption();
		searchOption.setSearchVOIP(true);
		searchOption.setSearchTollFree(true);
		searchOption.setSearchWirelessNumber(true);
		Subscriber[] subscriber = api.getAccountManager().findSubscribersByPhoneNumber("6471252022", searchOption, 1000, true);
		for (Subscriber subscriber2 : subscriber) {
			System.out.println("" + subscriber2);
		}
		System.out.println("end testWirelessRetrieval");
	}

	public void testSubscriberRetrieval() throws TelusAPIException {
		System.out.println("start testSubscriberRetrieval");
		PhoneNumberSearchOption searchOption = api.getAccountManager().newPhoneNumberSearchOption();
		Subscriber[] subscribers = api.getAccountManager().findSubscribersByPhoneNumber("4161725906", searchOption, 1000, true);
		for (Subscriber subscriber : subscribers) {
			System.out.println(subscriber.toString());
		}
		System.out.println("end testSubscriberRetrieval");
	}

	public void testModifyFutureDatedSubscriber() throws TelusAPIException {
		System.out.println("start testModifyFutureDatedSubscriber");
		Subscriber[] subscribers = api.getAccountManager().findSubscribersByPhoneNumber("4161725906", 1, false);
		Subscriber subscriber = subscribers[0];
		System.out.println(subscriber.toString());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1);
		subscriber.activate("RES", calendar.getTime());
		System.out.println("end testModifyFutureDatedSubscriber");
	}
	
	public void testHSIAToSmartHubEquipmentChange() throws TelusAPIException {
		
		// Note: in order to pass the 'testChangeEquipment' flow (specifically the 'TMSubscriber.checkTechnologyAndProductClassCompatibility' -> 'TransitionMatrix.validTransition' logic), the
		// productTypeCode of the dummy HSIA EquipmentInfo object must be set to 'C'.
		
		// Note: in order to pass KB-API equipment change, this should be handled as an HSPA -> HSPA equipment change; i.e., 'isNetworkChange = false' and 'imsiList = null' in 
		// 'DaoSupport.populateChangeEquipmentArray'.
		
		// Note: in order to pass SEMS equipment change, to properly assign the SmartHub to the phone number this flow should call the 'assignEquipmentToPhoneNumber' instead of 
		// 'swapHSPAOnlyEquipmentForPhoneNumber'.
		
		System.out.println("start testHSIAToSmartHubEquipmentChange");
		
		Subscriber[] subscribers = api.getAccountManager().findSubscribersByPhoneNumber("5871261460", 1, false);
		TMPCSSubscriber subscriber = (TMPCSSubscriber) subscribers[0];
		System.out.println(subscriber.toString());
		
		subscriber.getEquipment0().getDelegate().setProductType("C");
		System.out.println(subscriber.getEquipment().toString());
		
		Equipment newAssociatedEquipment = null; 
		Equipment newEquipment = api.getEquipmentManager().getEquipment("8912239900003077213");
		System.out.println(newEquipment.toString());	
		
//		System.out.println("executing testChangeEquipment...");
//		ApplicationMessage[] messages = subscriber.testChangeEquipment(newEquipment, newAssociatedEquipment, "A001000001", "0000", "18654", Subscriber.DUMMY_REPAIR_ID, "REPLACEMENT", 'X');
//		System.out.println("swap warning messages: [");
//		for (ApplicationMessage message : messages) {
//			System.out.println(message.toString());
//		}
//		System.out.println("]");
		
		System.out.println("executing changeEquipment...");
		ApplicationMessage[] messages = subscriber.changeEquipment(newEquipment, newAssociatedEquipment, "A001000001", "0000", "18654", Subscriber.DUMMY_REPAIR_ID, "REPLACEMENT", false, 'X', null);
		System.out.println("swap warning messages: [");
		for (ApplicationMessage message : messages) {
			System.out.println(message.toString());
		}
		System.out.println("]");
		
		System.out.println("end testHSIAToSmartHubEquipmentChange");
	}
	
	public void testSmartHubToHSIAEquipmentChange() throws TelusAPIException {
		
		// Note: in order to pass the 'testChangeEquipment' flow, the productClassCode of the dummy HSIA EquipmentInfo object must be set to 'USIM'.
		
		// Note: in order to pass KB-API equipment change, this should be handled as an HSPA -> HSPA equipment change; i.e., 'isNetworkChange = false' and 'imsiList = null' in 
		// 'DaoSupport.populateChangeEquipmentArray'.
		
		// Note: in order to pass SEMS equipment change, to properly disassociate the SmartHub from the phone number this flow should call the 'disassociateEquipmentFromPhoneNumber' instead of 
		// 'swapHSPAOnlyEquipmentForPhoneNumber'.
		
		System.out.println("start testSmartHubToHSIAEquipmentChange");
		
		Subscriber[] subscribers = api.getAccountManager().findSubscribersByPhoneNumber("5871725776", 1, false);
		TMPCSSubscriber subscriber = (TMPCSSubscriber) subscribers[0];
		System.out.println(subscriber.toString());
		
		Equipment newAssociatedEquipment = null;
		Equipment newEquipment = api.getEquipmentManager().getEquipment("200000000000000000");
		System.out.println(newEquipment.toString());
		
		((TMEquipment) newEquipment).getDelegate().setProductClassCode("USIM");
		System.out.println(newEquipment.toString());
		
		System.out.println("executing testChangeEquipment...");
		ApplicationMessage[] messages = subscriber.testChangeEquipment(newEquipment, newAssociatedEquipment, "A001000001", "0000", "18654", Subscriber.DUMMY_REPAIR_ID, "REPLACEMENT", 'X');
		System.out.println("swap warning messages: [");
		for (ApplicationMessage message : messages) {
			System.out.println(message.toString());
		}
		System.out.println("]");
		
//		System.out.println("executing changeEquipment...");
//		ApplicationMessage[] messages = subscriber.changeEquipment(newEquipment, newAssociatedEquipment, "A001000001", "0000", "18654", Subscriber.DUMMY_REPAIR_ID, "REPLACEMENT", false, 'X', null);
//		System.out.println("swap warning messages: [");
//		for (ApplicationMessage message : messages) {
//			System.out.println(message.toString());
//		}
//		System.out.println("]");
		
		System.out.println("end testSmartHubToHSIAEquipmentChange");
	}

	public void testGetCLMSummary() throws TelusAPIException, ApplicationException {
		System.out.println("start testGetCLMSummary");
		Account account = api.getAccountManager().findAccountByBAN(30948054);
		System.out.println(account.toString());
		
		Date defaultDate = new Date(100, 0, 1); //2000-01-01
		InvoiceHistory[] invoiceHistory = account.getInvoiceHistory(defaultDate, new Date());
		System.out.println("invoice history = " + invoiceHistory.length);
		
		double unpaidDataUsage = provider.getAccountLifecycleFacade().getTotalDataOutstandingAmount(30948054, defaultDate);
		System.out.println("unpaidDataUsage = " + unpaidDataUsage);
		
		CLMSummary clmSummary = ((PostpaidConsumerAccount) account).getCLMSummary();
		System.out.println(clmSummary.toString());
		System.out.println("end testGetCLMSummary");
	}

	public void testGetContractServices() throws TelusAPIException, ApplicationException {
		System.out.println("start testGetContractServices");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7809150651");
		
		Contract contract = subscriber.getContract();
		//System.out.println(contract.toString());		
		//SubscriberContractInfo contractInfo = ((TMContract) contract).getDelegate();
		
		Contract newContract = ((TMSubscriber) subscriber).renewContract(contract.getPricePlan(), 24, false);
		System.out.println(newContract.toString());
		
		SubscriberContractInfo contractInfo = ((TMContract) newContract).getDelegate();
		//List<ServiceAgreementInfo> list = Arrays.asList(contractInfo.getServices0(true));
		//for (ServiceAgreementInfo info : list) {
			//System.out.println(info.toString());
		//}
		
		ServiceAgreementInfo info = contractInfo.getService0("SPDAPPUD", true);
		System.out.println(info.toString());
		
		info = contractInfo.getService0(Info.padTo("SPDAPPUD", ' ', 9), true);
		System.out.println(info.toString());
		
		System.out.println("end testGetContractServices");
	}
	
	public void testValidateEquipment() throws TelusAPIException, ApplicationException {
		
		System.out.println("start testValidateEquipment");
		
		USIMCardEquipment usimEquipment = (USIMCardEquipment) provider.getEquipmentManager().getEquipment("8912239900003021393"); //8912239900003021393 8912230000113721294
		System.out.println(usimEquipment.toString());
		
		Equipment testEquipment = provider.getEquipmentManager().validateSerialNumber("8912239900003021393");
		System.out.println(testEquipment.toString());
		
		System.out.println("end testValidateEquipment");
	}
	
	public void testInterBrandPortResumeSubscriber() throws TelusAPIException {
		
		System.out.println("start testInterBrandPortResumeSubscriber");
		TMSubscriber subscriber = (TMSubscriber) api.getAccountManager().findSubscribersByPhoneNumber("4161592298", 1, true) [0];
		PortInEligibilityInfo info = new PortInEligibilityInfo();
		info.setIncomingBrandId(Brand.BRAND_ID_TELUS);
		info.setOutgoingBrandId(Brand.BRAND_ID_KOODO);
		subscriber.restore(new Date(), "KOPO", "Inter-brand port restore", Subscriber.PORT_OPTION_INTER_BRAND_ROLLBACK, info);
		System.out.println("end testInterBrandPortResumeSubscriber");
	}
	
	public void testGetBusinessCreditIdentities() throws TelusAPIException, ApplicationException {
		
		System.out.println("start testGetBusinessCreditIdentities");
		
		Account account = api.getAccountManager().findAccountByBAN(44134194);
		System.out.println(account.toString());
		
		if (account.isPostpaidBusinessRegular()) {
			BusinessCreditIdentity[] bciArray = ((PostpaidBusinessRegularAccount) account).getBusinessCreditIdentities();
			for (BusinessCreditIdentity bci : bciArray) {
				System.out.println(bci.toString());
			}
		} else {
			System.out.println("invalid account type/subtype");
		}
		
		System.out.println("end testGetBusinessCreditIdentities");
	}
	
	public void testAddLicenses() throws TelusAPIException, ApplicationException {
		System.out.println("start testAddLicenses");
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("5871257453");
		Contract contract = subscriber.getContract();
		contract.addService("SBCXLR35 ");
		contract.addService("SBCLRC35");
		contract.save();
		System.out.println("end testAddLicenses");
	}
	
	public void testRemoveLicenses()  {
		try {
			System.out.println("start testRemoveLicenses");
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber("7781527024");
			Contract contract = subscriber.getContract();
			contract.removeService("SBCLM500 ");
			contract.save();
			System.out.println("end testRemoveLicenses");
		} catch (InvalidServiceChangeException isc) {
			System.out.println("*************************** InvalidServiceChangeException *****************" + isc.getReason());
		} catch (TelusAPIException e) {
			System.out.println("*****************TelusAPIException ***************************  " );

		}
	}
}