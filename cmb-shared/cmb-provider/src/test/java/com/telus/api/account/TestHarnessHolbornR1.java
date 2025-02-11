package com.telus.api.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Address;
import com.telus.api.account.Contract;
import com.telus.api.account.MigrationRequest;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PCSPostpaidConsumerAccount;
import com.telus.api.account.PCSPrepaidConsumerAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMAirtimeCard;

public class TestHarnessHolbornR1 extends BaseTest {

	private String newHSPAPricePlan = "PTCAE100";
	private String newHSPAAssociatedIMEI = "";

	private String ESN = "16809070086"; //HSPA: 8912230000000066464
	private String searchPhoneNumber = "4037109656"; //HSPA: 4168940667, 9057160915
	private String simOnlyNumber = "";
	private String simAndHandsetNumber = "";
	private String numberLocationCode;
	private String dealerCode = "0000000008";
	private String salesRepCode = "0000";
	private String requestorId;
	private String repairId;
	private String swapType;
	
	static {
    	// Setup Environment
    	//setupD1();
		setupCHNLECA_QA(); 
	}

	public TestHarnessHolbornR1(String name) throws Throwable {
		super(name);
	}

	public void testAll() throws Throwable{
    	testPricePlanChange(searchPhoneNumber, newHSPAPricePlan);
    	testSuspendHSPA(searchPhoneNumber);
    	testResumeFromSuspendHSPA(searchPhoneNumber);
    	testCancelHSPA(searchPhoneNumber);
    	testResumeFromCancelHSPA(searchPhoneNumber);
    	testActivateReservedSubscriber(searchPhoneNumber);
    	testMigrate(searchPhoneNumber, newHSPAAssociatedIMEI, newHSPAPricePlan); //CDMA Pre to HSPA Post
    	testMigrate(searchPhoneNumber, newHSPAAssociatedIMEI, newHSPAPricePlan); //Mike to HSPA Post

    	testActivationSimOnly();
		testActivationSimAndHandset();
		testActivationPrepaid();

		testSearchSubscriberByPhoneNumber();
		testSearchSubscriberBySerialNumber();
		testEquipmentSwapSimOnly();
		testEquipmentSwapSimAndHandset();
		testPhoneNumberChange();
	}

	public void testPricePlanChange(String phoneNumber, String pricePlan) throws Throwable {
    	System.out.println("***** START OF TEST RUN : Price Plan Change ****");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);

        PricePlan plan = subscriber.getAvailablePricePlan(pricePlan);
        Contract newContract = subscriber.newContract(plan,	Subscriber.TERM_PRESERVE_COMMITMENT);

        // Change effective date to new date
        Calendar newCalendar = Calendar.getInstance();
        newCalendar.add(Calendar.DATE, +10);
        newCalendar.set(Calendar.HOUR_OF_DAY, 0);
        newCalendar.set(Calendar.MINUTE, 0);
        newCalendar.set(Calendar.SECOND, 0);
        newCalendar.set(Calendar.MILLISECOND, 0);
        Date newDate = newCalendar.getTime();
        newContract.setEffectiveDate(newDate);

        newContract.save();

        System.out.println("***** END OF TEST RUN ****");
    }

	public void testSuspendHSPA(String phoneNumber) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Suspend HSPA ****");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.suspend(subscriber.getAvailableSuspensionReasons()[0].getCode());

    	System.out.println("***** END OF TEST RUN ****");
	}

	public void testResumeFromSuspendHSPA(String phoneNumber) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Resume From Suspend HSPA ****");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.restore(subscriber.getAvailableResumptionReasons()[0].getCode());

    	System.out.println("***** END OF TEST RUN ****");
	}

	public void testCancelHSPA(String phoneNumber) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Cancel HSPA ****");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.cancel(subscriber.getAvailableCancellationReasons()[0].getCode(),'R');

    	System.out.println("***** END OF TEST RUN ****");
	}

	public void testResumeFromCancelHSPA(String phoneNumber) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Resume From Cancel HSPA ****");

		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.restore(subscriber.getAvailableResumptionReasons()[0].getCode());

    	System.out.println("***** END OF TEST RUN ****");
	}

	public void testActivateReservedSubscriber(String phoneNumber) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Activate Reserved Subscriber ****");
    	
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		subscriber.activate(subscriber.getActivityReasonCode(), null, null, null);

    	System.out.println("***** END OF TEST RUN ****");
	}

	public void testMigrate(String phoneNumber, String associatedIMEI, String hspaPricePlan) throws Throwable  {
    	System.out.println("***** START OF TEST RUN : Migrate P2P ****");
    	
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		Equipment hspaEquipment = api.getEquipmentManager().getEquipment(associatedIMEI);
		
		MigrationRequest migrationRequest = ((TMSubscriber)subscriber).newMigrationRequest(subscriber.getAccount(), hspaEquipment, hspaPricePlan);
		((TMSubscriber)subscriber).migrate(migrationRequest, null, null, null);
    	
    	System.out.println("***** END OF TEST RUN ****");
	}

	private void testActivationSimOnly() {		
		try {
			newPCSPostpaidConsumerAccount();
			newPCSSubscriber(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	private void testActivationSimAndHandset() {		
		try {
			newPCSPostpaidConsumerAccount();
			newPCSSubscriber(true);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void testActivationPrepaid() {
		try {
			newPCSPrepaidConsumerAccountSimAndHandset();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void testSearchSubscriberByPhoneNumber() throws UnknownSubscriberException, TelusAPIException {
		Subscriber[] subscribers = api.getProvider().getAccountManager().findSubscribersByPhoneNumber(
				searchPhoneNumber, 10, true);

		try {

			// Method: retrieveSubscriberListByBAN
			for (int i = 0; i < subscribers.length; i++) {

				{
					Subscriber subscriber = subscribers[i];
					System.out.println(subscriber.getSubscriberId() + " , "
							+ subscriber.getPhoneNumber() + " , ESN: "
							+ subscriber.getSerialNumber() + " , "
							+ subscriber.getProductType() + " , "
							+ subscriber.getStatus() + ", "
							+ subscriber.getFirstName() + " , "
							+ subscriber.getLastName() + " , "
							+ subscriber.getCreateDate() + " , "
							+ subscriber.getStartServiceDate() + " , "
							+ subscriber.getMarketProvince() + " , "
							+ subscriber.getActivityCode() + " , "
							+ subscriber.getActivityReasonCode());
				}
			}
			System.out
					.println("Done! ------------------------------------------------------------");

			return;

		}

		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

	private void testSearchSubscriberBySerialNumber() throws UnknownSubscriberException, TelusAPIException {
		
		Subscriber[] subscribers = api.getProvider().getAccountManager().findSubscribersBySerialNumber(ESN);
		try {

			// Method: retrieveSubscriberListByBAN
			for (int i = 0; i < subscribers.length; i++) {

				{
					Subscriber subscriber = subscribers[i];
					System.out.println(subscriber.getSubscriberId() + " , "
							+ subscriber.getPhoneNumber() + " , ESN: "
							+ subscriber.getSerialNumber() + " , "
							+ subscriber.getProductType() + " , "
							+ subscriber.getStatus() + ", "
							+ subscriber.getFirstName() + " , "
							+ subscriber.getLastName() + " , "
							+ subscriber.getCreateDate() + " , "
							+ subscriber.getStartServiceDate() + " , "
							+ subscriber.getMarketProvince() + " , "
							+ subscriber.getActivityCode() + " , "
							+ subscriber.getActivityReasonCode());
				}
			}
			System.out
					.println("Done! ------------------------------------------------------------");

			return;

		}

		catch (Throwable t) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

	private void testEquipmentSwapSimOnly() throws Throwable {
		Equipment newEquipment = api.getEquipmentManager().getEquipment(ESN);
		
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(searchPhoneNumber);
		subscriber.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
	}

	private void testEquipmentSwapSimAndHandset() throws Throwable {
		// TODO - replace with find by SerialNumber and/or handsetIMEI 
		Equipment newEquipment = api.getEquipmentManager().getEquipment(ESN);
		
		Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(searchPhoneNumber);
		subscriber.changeEquipment(newEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType);
	}

	private void testPhoneNumberChange() {
		try {
			AvailablePhoneNumberInfo availableNumber = new AvailablePhoneNumberInfo();
			availableNumber.setPhoneNumber(simAndHandsetNumber);
			
			Subscriber subscriber = api.getAccountManager().findSubscriberByPhoneNumber(searchPhoneNumber);
			subscriber.changePhoneNumber(availableNumber, false, dealerCode, salesRepCode);		
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void newPCSPrepaidConsumerAccountSimAndHandset() throws Throwable {
			
			AuditHeaderInfo info = new AuditHeaderInfo();
			PCSPrepaidConsumerAccount account = api.getAccountManager().newPCSPrepaidConsumerAccount(
					"8912230000000066688",
	                 "100000000100624",
	                 AccountManager.ACTIVATION_TYPE_AIRTIME_CARD, //1
	                 "000000040911",
	                 null,
	                 BusinessRole.BUSINESS_ROLE_AGENT, //AGENT 
	                50.0 ,info) ; 

			setupAccount(account);
			account.save();
			assertNotNull(account);

	}

	private void newPCSPostpaidConsumerAccount() throws Throwable {
		
		PCSPostpaidConsumerAccount account = api.getAccountManager().newPCSPostpaidConsumerAccount();
		setupAccount(account);
		account.save();
		assertNotNull(account);

	}

	/**
	 * 
	 * @param SimAndHandset
	 *            - True if SIM and Handset are used to create a new Subscriber
	 *            - False if only SIM is used
	 * @throws TelusAPIException 
	 * @throws BrandNotSupportedException 
	 * @throws UnknownBANException 
	 */
	private void newPCSSubscriber(boolean SimAndHandset) throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		
		PCSAccount account = (PCSAccount) api.getAccountManager().findAccountByBAN(70104002);
		PCSSubscriber subscriber;
		if (SimAndHandset == true) {
			subscriber = account.newPCSSubscriber("8912230000000066662", "100000000100624", false, null, "EN");
		} else {
			subscriber = account.newPCSSubscriber("8912230000000066662", false, null, "EN");
		}
				

		System.out.println("New Subscriber");
		// add subscriber basic info
		subscriber.setFirstName("Jane");
		subscriber.setLastName("Doe");
		subscriber.setLanguage("EN");
		subscriber.setDealerCode("0000000008");
		subscriber.setSalesRepId("0000");
		subscriber.setBirthDate(new Date(1960, 01, 01));
		subscriber.setEmailAddress("jane.dow@telusmobility.com");
		subscriber.setActivityReasonCode("CMER");

		// Reserve phone number
		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup[] numberGroups = api.getReferenceDataManager()
				.getNumberGroups(
						subscriber.getAccount().getAccountType(),
						subscriber.getAccount().getAccountSubType(),
						subscriber.getProductType(),
						String.valueOf(subscriber.getEquipment()
								.getEquipmentType()));
		System.out.println("PhoneNumberReservation");

		NumberGroup numberGroup = null;
		for (int i = 0; i < numberGroups.length; i++) {
			System.out.println(numberGroups[i].getProvinceCode());
			if ("ON".equals(numberGroups[i].getProvinceCode())) {
				numberGroup = numberGroups[i];
			}
		}
		reservation.setNumberGroup(numberGroup);
		reservation.setAsian(false);
		reservation.setLikeMatch(false);
		reservation.setWaiveSearchFee(true);
		reservation.setPhoneNumberPattern("**********");
		subscriber.reservePhoneNumber(reservation);

		// Setup Price Plan and Services

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
				"FETA100  ",
				String.valueOf(subscriber.getEquipment().getEquipmentType()),
				subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				Brand.BRAND_ID_TELUS);

		System.out.println("getPricePlan");

		Contract contract = subscriber.newContract(pricePlan,
				Subscriber.TERM_MONTH_TO_MONTH);

		System.out.println("newContract");
		subscriber.save();

	}
	
	private static void setupAccount(PCSPrepaidConsumerAccount account)
			throws Throwable {

		setupAddress(account.getAddress());
		// account.setEmail("dele.taylor@telusmobility.com");
		account.setBrandId(Brand.BRAND_ID_TELUS);
		account.setPin("1234");
		account.setLanguage("EN");
		account.setDealerCode("0000000008");
		account.setSalesRepCode("0000");
		account.getContactName().setFirstName("Naresh");
		account.getContactName().setLastName("PrepaidDefectTest");
		account.getName().setFirstName("Naresh");
		account.getName().setLastName("PrepaidDefectTest");
		account.setHomePhone("4161234567");
		account.setContactPhone("5556667777");

	}
	
	private static void setupAccount(PCSPostpaidConsumerAccount account)
			throws Throwable {

		setupAddress(account.getAddress());
		// account.setEmail("dele.taylor@telusmobility.com");
		account.setBrandId(Brand.BRAND_ID_TELUS);
		account.setPin("2222");
		account.setLanguage("EN");
		account.setDealerCode("0000000008");
		account.setSalesRepCode("0000");
		account.getContactName().setFirstName("Mike");
		account.getContactName().setLastName("Osborn");
		account.getName().setFirstName("Mike");
		account.getName().setLastName("Osborn");
		account.setHomePhone("4161234567");
		account.setContactPhone("5556667777");

	}

	private static void setupAddress(Address address) throws Throwable {

		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");

	}
	
	
	
public void testPrepaidSubscriberActivationforOneTiemAirtimeTopUP() throws Throwable {
		
		AuditHeaderInfo info = new AuditHeaderInfo();
		
		
		PCSPrepaidConsumerAccount account = api.getAccountManager().newPCSPrepaidConsumerAccount(
				"8912256801189569189",
				"900000000612084",
                 AccountManager.ACTIVATION_TYPE_WITH_ESN, 
                 "000955651234",
                 null,
                 BusinessRole.BUSINESS_ROLE_AGENT, //AGENT 
               25 ,info) ; 

        CardInfo cardInfo  =  new CardInfo();
             cardInfo.setSerialNumber("000955651234");
             cardInfo.setPIN("1234");
             PaymentCard[] paymentCardList = new PaymentCard[1];
             paymentCardList[0] = cardInfo;
         //    topUpArrangement.setupPaymentCardList(paymentCardList);
             TMAirtimeCard tmAirtimeCard = new TMAirtimeCard(provider,cardInfo);

		setupAccount(account);
		account.getActivationTopUpPaymentArrangement().setActivationTopUpCardType(null);
		account.getActivationTopUpPaymentArrangement().setAirtimeCard(tmAirtimeCard);
		account.save();
		
		PCSSubscriber pCSSubscriber = account.newPCSSubscriber();
		 PricePlan pricePlan = pCSSubscriber.getAvailablePricePlan("PPNTKVM3");
		  //Reserve phone number
			PhoneNumberReservation reservation;
			reservation = api.getAccountManager().newPhoneNumberReservation();
			NumberGroup[] numberGroups = api.getReferenceDataManager()
					.getNumberGroups(
							pCSSubscriber.getAccount().getAccountType(),
							pCSSubscriber.getAccount().getAccountSubType(),
							pCSSubscriber.getProductType(),
							String.valueOf(pCSSubscriber.getEquipment()
									.getEquipmentType()));
			System.out.println("PhoneNumberReservation");

			NumberGroup numberGroup = null;
			for (int i = 0; i < numberGroups.length; i++) {
				System.out.println(numberGroups[i].getProvinceCode());
				if ("ON".equals(numberGroups[i].getProvinceCode())) {
					numberGroup = numberGroups[i];
				}
			}
			reservation.setNumberGroup(numberGroup);
			reservation.setAsian(false);
			reservation.setLikeMatch(false);
			reservation.setWaiveSearchFee(true);
			reservation.setPhoneNumberPattern("**********");
			pCSSubscriber.reservePhoneNumber(reservation);

			pCSSubscriber.reservePhoneNumber(reservation);
			
			pCSSubscriber.newContract(pricePlan, pCSSubscriber.TERM_MONTH_TO_MONTH);
			
		pCSSubscriber.save(true);
		
		//assertNotNull(account);

	}
}
