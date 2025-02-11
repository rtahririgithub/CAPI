package com.telus.provider.account;


import java.util.Calendar;
import java.util.Date;
import com.telus.api.ApplicationException;
import com.telus.api.BaseTest;
import com.telus.api.InvalidPricePlanChangeException;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCirclePhoneList;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractChangeHistory;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.account.FeatureParameterHistory;
import com.telus.api.account.HandsetChangeHistory;
import com.telus.api.account.InvoiceTax;
import com.telus.api.account.Memo;
import com.telus.api.account.PCSPostpaidConsumerAccount;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.PhoneNumberException;
import com.telus.api.account.PhoneNumberReservation;
import com.telus.api.account.PricePlanChangeHistory;
import com.telus.api.account.ProvisioningTransaction;
import com.telus.api.account.ResourceChangeHistory;
import com.telus.api.account.ServiceChangeHistory;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberHistory;
import com.telus.api.account.SubscriptionPreference;
import com.telus.api.account.SubscriptionRole;
import com.telus.api.account.UnknownBANException;
import com.telus.api.account.VendorServiceChangeHistory;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.equipment.Card;
import com.telus.api.equipment.Equipment;
import com.telus.api.portability.PRMSystemException;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortOutEligibility;
import com.telus.api.portability.PortRequestException;
import com.telus.api.portability.PortRequestManager;
import com.telus.api.reference.Brand;
import com.telus.api.reference.NumberGroup;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.PricePlanSummary;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.provider.equipment.TMCard;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.portability.TMPortRequest;

public class TMSubscriberIntTest extends BaseTest {

	static {

		//setupEASECA_QA();
		//setupCHNLECA_PT168();
		setupCHNLECA_CSI();
		//setupEASECA_D3();
		
		//System.setProperty("cmb.services.SubscriberLifecycleManager.url", "t3://localhost:7140");
		//System.setProperty("cmb.services.SubscriberLifecycleHelper.url", "t3://localhost:7140");
		//System.setProperty("cmb.services.SubscriberLifecycleFacade.url", "t3://localhost:7140");
	}

	public TMSubscriberIntTest (String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;

	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
		SubscriberInfo si=new SubscriberInfo();
		si.setProductType("D");

	}

	public void test_ChangePhoneNumber()  throws Exception{
		Account ai = accountManager.findAccountByBAN0(70877225);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("6132070655");
		subscriber.changePhoneNumber(findAvailablePhoneNumber(subscriber), false);
	}

	
	public void testGetContract0() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {

		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		TMContract contract=subscriber.getContract0(false,true);
		assertEquals(0.0,contract.get911Charges(),0);
	}

	public void testGetEquipment0() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		TMEquipment equipment=subscriber.getEquipment0();
		assertEquals("16809070086",equipment.getSerialNumber());
	}

	public void testGetVoiceUsageSummary() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		String featureCode="STD";
		try{
			VoiceUsageSummary vus=subscriber.getVoiceUsageSummary(featureCode);
			assertEquals("",vus.getPhoneNumber());
		}catch(Throwable t){
			assertTrue(t.getStackTrace()[0].toString().contains("VoiceUsageSummary"));
		}

	}


	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	public void testGetProvisioningTransactions() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70024088);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("6478839964");
		ProvisioningTransaction[] pt=subscriber.getProvisioningTransactions(getDateInput(2002,1,1),getDateInput(2005,2,2));
		for (int i =0;i<pt.length;i++)
		{
			System.out.println("pt[i].getTransactionNo()"+pt[0].getTransactionNo() +""+i);
		}
		assertEquals("71974",pt[0].getTransactionNo());

	}
	public void testGetContractChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		ContractChangeHistory[] cch=subscriber.getContractChangeHistory(getDateInput(1990,1,1),getDateInput(2011,2,2));
		assertEquals("PPD",cch[0].getReasonCode());
	}

	public void testGetHandsetChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70615492);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755355");
		HandsetChangeHistory[] hch=subscriber.getHandsetChangeHistory(getDateInput(2011,1,1),getDateInput(2011,11,24));
		for (int i =0;i<hch.length;i++)
		{
			System.out.println("getSerialNumber()"+hch[i].getNewEquipment().getSerialNumber() +""+i);
		}
	}

	public void testGetPricePlanChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		PricePlanChangeHistory[] ppch = subscriber.getPricePlanChangeHistory(getDateInput(1990,1,1),getDateInput(2011,11,24));
		assertEquals("PSM80MSB4",ppch[0].getNewPricePlanCode());
	}
	public void testGetServiceChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		ServiceChangeHistory[] sch=subscriber.getServiceChangeHistory(getDateInput(1990,1,1),getDateInput(2011,11,24));

		assertEquals("XSB10MDE1",sch[0].getServiceCode());
	}

	public void testGetServiceChangeHistory1() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(197806);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037109656");
		ServiceChangeHistory[] sch=subscriber.getServiceChangeHistory(getDateInput(1990,1,1),getDateInput(2011,11,24),true);
		assertEquals("XSB10MDE1",sch[0].getServiceCode());
	}

	public void testGetResourceChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		ResourceChangeHistory[] rch=subscriber.getResourceChangeHistory("*",getDateInput(1990,1,1),getDateInput(2011,11,24));

		assertEquals("A",rch[0].getStatus());
	}

	public void testGetSubscriptionRole() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		SubscriptionRole sr=subscriber.getSubscriptionRole();

		assertEquals("AA",sr.getCode());

	}

	public void testGetLastMemo() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		Memo memo =subscriber.getLastMemo("0002");

		assertEquals("Subscriber Reserved effective 11/25/2011.",memo.getSystemText());

	}

	public void testGetHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		SubscriberHistory[] sh =subscriber.getHistory(getDateInput(1990,1,1),getDateInput(2011,11,24));

		assertEquals('A',sh[0].getStatus());

	}


	public void testGetProvisioningStatus() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		String provStatus =subscriber.getProvisioningStatus();
		assertEquals("CS",provStatus);
	}

	public void testGetDepositHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562547);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4503688918");
		DepositHistory[] dh =subscriber.getDepositHistory();
		assertEquals(200.0,dh[0].getChargesAmount(),0);
	}
	public void testGetAddress() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		Address address =subscriber.getAddress(true);
		assertEquals("BURNABY",address.getCity());

	}

	public void testGetEquipmentChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562538);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		EquipmentChangeHistory[] ech =subscriber.getEquipmentChangeHistory(new Date((2002-1900),(2-1),13), new Date((2012-1900),(3-1),31));

		assertEquals("100000000000000000",ech[0].getSerialNumber());

	}

	public void testGetInvoiceTax() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562538);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4184183122");
		InvoiceTax ivt =subscriber.getInvoiceTax(1);

		assertFalse(ivt.isGSTExempt());
	}

	public void testGetPaidSecurityDeposit() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562538);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4184183122");
		double paidSD =subscriber.getPaidSecurityDeposit();

		assertEquals(0.00, paidSD,0);
	}
	public void testGetVirtualEquipment() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562538);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4184183122");
		Equipment virtualEquipment =subscriber.getVirtualEquipment();

		assertEquals("C", virtualEquipment.getNetworkType());
	}

	public void testGetPortProtectionIndicator() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70562538);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4184183122");
		String ppindicator =subscriber.getPortProtectionIndicator();
		assertNull(ppindicator);
	}
	//pending
	public void testGetCallingCirclePhoneNumberListHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		CallingCirclePhoneList[] ccp =subscriber.getCallingCirclePhoneNumberListHistory(new Date((2000-1900),(2-1),13), new Date((2012-1900),(3-1),31));

		assertEquals("4164567894", ccp[0].getPhoneNumberList()[0]);

	}
	public void testGetVendorServiceChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70616269);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7781755476");
		VendorServiceChangeHistory[] vsch =subscriber.getVendorServiceChangeHistory("LCID5R3M4");

		//assertEquals("I", vsch[0].getVendorServiceCode());
	}

	public void testSave() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(1916609);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("6479990402");
		Date startServiceDate = null;
		ActivationOption selectedOption = null;
		ServicesValidation srvValidation = null;
		subscriber.save(startServiceDate, selectedOption, srvValidation);


	}

	public void testGetFeatureParameterChangeHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70042226);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4163160007");
		FeatureParameterHistory[] fph = subscriber.getFeatureParameterChangeHistory(getDateInput(2000, 1, 1),getDateInput(2012, 1, 1));

		assertEquals("DATE-OF-BIRTH", fph[0].getParameterName());

	}

	public void testGetFeatureParameterHistory() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70042226);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4163160007");
		String[] parameterNames = {"DATE-OF-BIRTH","CALLHOMEFREE","CALLING-CIRCLE"};
		FeatureParameterHistory[] fph = subscriber.getFeatureParameterHistory(parameterNames,getDateInput(2000, 1, 1),getDateInput(2012, 1, 1));

		assertEquals("DATE-OF-BIRTH", fph[0].getParameterName());

	}

	public void testSaveActivationFeaturesPurchaseAgreement() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70042446);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4037010041");
		subscriber.saveActivationFeaturesPurchaseAgreement();

		assertEquals("DATE-OF-BIRTH",subscriber.getFeatureParameterChangeHistory(getDateInput(2000, 1, 1),getDateInput(2012, 1, 1))[0].getParameterName());

	}

	public void testGetSubscriptionPreference() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(70043072);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4169954385");
		try{
			SubscriptionPreference sp = subscriber.getSubscriptionPreference(1);
			assertEquals(1,sp.getPreferenceTopicId());
		}catch(Throwable t){

			assertTrue(t instanceof TelusAPIException);
			assertTrue(t.getMessage().contains("Cannont find subscription for subscriptionId"));
		}

	}

	public void testApplyCredit_prepaid() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(4036958);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("6472104749");
		CardInfo ci=new CardInfo();
		ci.setSerialNumber("10000011909");
		Card card=new TMCard(provider,ci);
		try{
			subscriber.applyCredit_prepaid(card);
		}catch(Throwable t){
			assertTrue(t instanceof TelusAPIException);
			assertTrue(((TelusAPIException)t).getStackTrace0().contains("Validation rule requirements failed"));
		}

	}

	public void testApplyCredit_postpaid() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		Account ai = accountManager.findAccountByBAN0(805938);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("7807191196");
		CardInfo ci=new CardInfo();
		ci.setSerialNumber("10000011909");
		Card card=new TMCard(provider,ci);
		try{
			subscriber.applyCredit_postpaid(card);
		}catch(Throwable t){
			assertTrue(t instanceof TelusAPIException);
			assertTrue(((TelusAPIException)t).getStackTrace0().contains("Validation rule requirements failed"));
		}

	}



	public void testApplyCredit_prepaid1() throws TelusAPIException {
		Account ai = accountManager.findAccountByBAN0(4036958);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("6472104749");

		Card[] card=api.getEquipmentManager().getCards("30902564054");

		Card c= null;
		for(int i=0;i<card.length;i++){
			if(card[i].getStatus()==Card.STATUS_LIVE){
				c=card[i];
				break;
			}
		}
		if(c!=null)
			subscriber.applyCredit_prepaid(c);


	}

	public void testUpdateDifferentiate() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN0(1905137);
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("5196350743");
		CreditCheckResult eligibility = account.checkNewSubscriberEligibility(15, 100);
		System.out.println("ELIGIBILITY" + eligibility.getCreditDate() + "**");
		ActivationOption[] activationOptions = eligibility.getActivationOptions();
//		subscriber.updateDifferentiate(account, activationOptions[0]);
		subscriber.setActivationOption(activationOptions[0], false);
		subscriber.getActivationOption().apply();
	}

	public void testUpdateCLPClient() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242");
		CreditCheckResult eligibility = account.checkNewSubscriberEligibility(10, 0);
		ActivationOption[] activationOptions = eligibility.getActivationOptions();
		System.out.println("length : " + activationOptions.length);
//		subscriber.updateCLPClient(accountManager.findAccountByBAN0(70570745), activationOptions[0]);
		subscriber.setActivationOption(activationOptions[0], false);
		subscriber.getActivationOption().apply();
	}


	public void testUpdateTownCLPClient() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242");
		CreditCheckResult eligibility = account.checkNewSubscriberEligibility(10, 0);
		ActivationOption[] activationOptions = eligibility.getActivationOptions();
		System.out.println("length : " + activationOptions.length);
//		subscriber.updateTownCLPClient(accountManager.findAccountByBAN0(70617358), activationOptions[0]);
		subscriber.setActivationOption(activationOptions[0], true);
		subscriber.getActivationOption().apply();
	}

	public void testCreateCLPMemo() throws TelusAPIException {
		Account account = accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242");
		CreditCheckResult eligibility = account.checkNewSubscriberEligibility(10, 0);
		ActivationOption[] activationOptions = eligibility.getActivationOptions();
		System.out.println("length : " + activationOptions.length);
//		subscriber.updateCLPClient(accountManager.findAccountByBAN0(70617358), activationOptions[0]);
		subscriber.setActivationOption(activationOptions[0], false);
		subscriber.getActivationOption().apply();
	}

	public void testSetBackOrginalDeposit() throws ApplicationException, Throwable {
		System.out.println("testSetBackOrginalDeposit start");
		TMAccount account = (TMAccount) accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber) account.getSubscriberByPhoneNumber("7781761242");
		CreditCheckResult eligibility = account.checkNewSubscriberEligibility(account.getAllSubscriberCount(), 0);
		ActivationOption[] activationOptions = eligibility.getActivationOptions();
		System.out.println("activationOptions length : " + activationOptions.length);
//		subscriber.setBackOrginalDeposit(account, activationOptions[0]);
		subscriber.setActivationOption(activationOptions[0], false);
		subscriber.getActivationOption().apply();
		subscriber.getActivationOption().setBackOriginalDepositIfDifferentiated();
		System.out.println("testSetBackOrginalDeposit end");
	}


	public void testSave1O() throws  ApplicationException, Throwable  {

		System.out.println("testSetBackOrginalDeposit start");
		TMAccount account =(TMAccount) accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber)account.getSubscriberByPhoneNumber("7781761242");
		ServicesValidation srvValidation= new ServicesValidationInfo();
		subscriber.save(true, null, srvValidation);

	}



	public void testTestNewContract() throws  Throwable  {

		TMAccount account =(TMAccount) accountManager.findAccountByBAN0(70617358);
		TMSubscriber subscriber = (TMSubscriber)account.getSubscriberByPhoneNumber("7781761242");

		PricePlan pricePlan = api.getReferenceDataManager().getPricePlan(
				"PYCDB30B4",
				String.valueOf(subscriber.getEquipment().getEquipmentType()),
				subscriber.getAccount().getAddress().getProvince(),
				subscriber.getAccount().getAccountType(),
				subscriber.getAccount().getAccountSubType(),
				Brand.BRAND_ID_TELUS);

		subscriber.newContract(pricePlan, 12);

	}


	/**
	 * This tests tries to add a Business Anywhere PricePlan to a non Business Anywhere
	 * account.  The expected exception is an InvalidPricePlanChangeException with a
	 * specific reason code - InvalidPricePlanChangeException.ACCOUNT_TYPE_SUBTYPE_MISMATCH.
	 * The subscriber is any valid subscriber where the subscriber's account type and sub-type
	 * is not the following:  B/A, B/N, C/Y
	 * @throws Exception
	 */
	public void testNewContract_BAPricePlanOnNonBAAccount() throws Exception {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("7781761242");

		PricePlanSummary pp = api.getReferenceDataManager().getPricePlan("XPBABB3");
		try {
			subscriber.newContract((PricePlan) pp, 1);
		} catch (InvalidPricePlanChangeException ex) {
			assertEquals(InvalidPricePlanChangeException.ACCOUNT_TYPE_SUBTYPE_MISMATCH, ex.getReason());
		}
	}

	/**
	 * This tests tries to add a Business Anywhere service to a non Business Anywhere
	 * account.  The expected exception is an InvalidServiceChangeException with a
	 * specific reason code - InvalidServiceChangeException.UNAVAILABLE_SERVICE.
	 * The subscriber is any valid subscriber where the subscriber's account type and sub-type
	 * is not the following:  B/A, B/N, C/Y
	 * @throws Exception
	 */
	public void testNewService_BAServiceOnNonBAAccount() throws Exception {
		Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("4168940010");

		Contract contract = subscriber.getContract();
		try {
			contract.addService("XSCLDINT");
		} catch (InvalidServiceChangeException ex) {
			assertEquals(InvalidServiceChangeException.UNAVAILABLE_SERVICE, ex.getReason());
		}		
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
						}
						break;
					}
				} catch (PhoneNumberException pne) {
				}
			}
		}

		if (nums == null || nums.length < 1) {
			throw new PhoneNumberException("No phone # found.");
		}else {
			return nums[3];
		}
	}

	private static PortRequestInfo createPortInRequest(String phoneNumber, String NPDirectionIndicator, boolean prePopulate, PCSSubscriber subscriber) throws PRMSystemException,  TelusAPIException {

		PortRequestInfo portRequest = new PortRequestInfo();
		TMPortRequest tmPortRequest = null;
		try {
			tmPortRequest = (TMPortRequest)subscriber.newPortRequest(phoneNumber,NPDirectionIndicator, prePopulate);
			portRequest = tmPortRequest.getDelegate();
			portRequest.setPortRequestId(""); 

			portRequest.setOSPAccountNumber("70746411");
			//portRequest.setOSPSerialNumber("1234567890");
			//portRequest.setOSPPin("1111");
			portRequest.setAlternateContactNumber("4164165577");
			portRequest.setAgencyAuthorizationDate(java.sql.Date.valueOf("2012-03-22"));
			portRequest.setAgencyAuthorizationName("ASDF");
			portRequest.setAgencyAuthorizationIndicator("Y");
			portRequest.setAutoActivate(false);
			//portRequest.setDesiredDateTime(java.sql.Date.valueOf("2010-10-04"));
			portRequest.setPhoneNumber(phoneNumber);
			portRequest.setRemarks("");
			portRequest.setBanId(subscriber.getAccount().getBanId());
			portRequest.setCanBeActivate(true);
			portRequest.setCanBeCancel(true);
			portRequest.setCanBeModify(true);
			portRequest.setCanBeSubmit(true);
			portRequest.setCreationDate(java.sql.Date.valueOf("2012-03-22"));
			portRequest.setIncomingBrandId(1);
			portRequest.setOutgoingBrandId(3);


			if (!prePopulate){
				Account  account = subscriber.getAccount();
				portRequest = (PortRequestInfo)PortRequestManager.Helper.copyName(account,portRequest);
				portRequest = (PortRequestInfo)PortRequestManager.Helper.copyAddress(account,portRequest);

				portRequest.setAlternateContactNumber(account.getOtherPhone());
				portRequest.setPhoneNumber(phoneNumber);
				portRequest.setPortDirectionIndicator(NPDirectionIndicator);
			}
		}catch(Exception e) {
			throw new TelusAPIException(e);
		}
		System.out.println("N E W     P O R T     I N     R E Q U E S T");
		System.out.println("\n portRequest = " + portRequest);
		return portRequest;
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

	// EAGLE REFACTORING REGRESSION TEST CASES

	public void testChangePhoneNumberKoodo2Telus() throws Exception
	{
		// taking Koodo PhNum and changing into Telus

		System.out.println("start testChangePhoneNumber");
		String phNum = "4031654563"; // TELUS Active PhNum in KB
		String portInNumber = "6471268777";
		TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber(phNum);		
		boolean  changeOtherNumbers = false;
		String reasonCode = "CR";
		String dealerCode = "B00AB00003";
		String salesRepCode = "0001";

		System.out.println("start PortInEligibility");		
		PortRequestManager portRequestManager = provider.getPortRequestManager();

		PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
		portInEligibility.setPhoneNumber(phNum);
		portInEligibility.setIncomingBrandId(Brand.BRAND_ID_TELUS);  
		portInEligibility.setOutgoingBrandId(Brand.BRAND_ID_KOODO);
		portInEligibility.setPlatformId(2);

		AvailablePhoneNumberInfo availablePhoneNumberInfo =   new AvailablePhoneNumberInfo();
		availablePhoneNumberInfo.setPhoneNumber(portInNumber);  //KOODO interbrand PhNum Got from WLNP    
		NumberGroupInfo numberGroup = (NumberGroupInfo) findNumberGroupbyPhoneNumber(subscriber);
		availablePhoneNumberInfo.setNumberGroup(numberGroup);

		System.out.println(" start PortOutEligibility");
		String ndpInd = "1";
		PortOutEligibility portout_eligibility = portRequestManager.testPortOutEligibility(phNum, ndpInd);
		System.out.println("end portout eligibility"+portout_eligibility.isEligible());

		System.out.println(" start PortRequest validation");
		/**NOTE: need to set ospaccount number(BAN) or pin inside the below method for outgoing PhNum ,
		 * otherwise it will throw OSPValidation error*/	 
		createPortInRequest(portInNumber, "A", true , (PCSSubscriber)subscriber);
		System.out.println(" end PortRequest validation");

		System.out.println(" start testChangePhoneNumber");	
		subscriber.changePhoneNumber(availablePhoneNumberInfo, reasonCode, changeOtherNumbers, dealerCode, salesRepCode, portInEligibility);
		System.out.println("end testChangePhoneNumber");

	}

	public void testChangePhoneNumberTelus2Koodo() throws Exception
	{
		// taking Telus PhNum and changing into koodo

		System.out.println("start testChangePhoneNumber");
		String phNum = "5141533269"; // KOODO Active PhNum from KB
		String portInNumber = "4161515129";

		TMSubscriber subscriber = (TMSubscriber) accountManager.findSubscriberByPhoneNumber(phNum);		
		boolean  changeOtherNumbers = false;
		String reasonCode = "CR";
		String dealerCode = "KD00000001";
		String salesRepCode = "AA00";

		System.out.println("start PortInEligibility");		
		PortRequestManager portRequestManager = provider.getPortRequestManager();

		PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
		portInEligibility.setPhoneNumber(phNum);
		portInEligibility.setIncomingBrandId(Brand.BRAND_ID_KOODO);  
		portInEligibility.setOutgoingBrandId(Brand.BRAND_ID_TELUS);

		AvailablePhoneNumberInfo availablePhoneNumberInfo =   new AvailablePhoneNumberInfo();
		availablePhoneNumberInfo.setPhoneNumber(portInNumber);  //TELUS interbrand PhNum got from WLNP
		NumberGroupInfo numberGroup = (NumberGroupInfo) findNumberGroupbyPhoneNumber(subscriber);
		availablePhoneNumberInfo.setNumberGroup(numberGroup);

		System.out.println(" start PortOutEligibility");
		String ndpInd = "1";
		PortOutEligibility portout_eligibility = portRequestManager.testPortOutEligibility(phNum, ndpInd);
		System.out.println("end ph.num portout eligibility"+portout_eligibility.isEligible());
		
		System.out.println(" start createPortInRequest");
		/**NOTE: need to set ospaccount number(BAN) or pin inside the below method for outgoing PhNum ,
		 * otherwise it will throw OSPValidation error*/	 
		createPortInRequest(portInNumber, "A", true , (PCSSubscriber)subscriber);
		System.out.println(" end createPortInRequest");

		System.out.println(" start testChangePhoneNumber");
		subscriber.changePhoneNumber(availablePhoneNumberInfo, reasonCode, changeOtherNumbers, dealerCode, salesRepCode, portInEligibility);
		System.out.println("end testChangePhoneNumber");

	}


	public void testSaveTelus2Koodo() throws Exception {

		System.out.println("*******  started testSaveTelus2Koodo method   *******  ");	

		boolean activate = true;
		String phNum = "4031654558"; // Telus PhNum which we got from WLNP team
		String serialNumber = "8912230000024385296"; // koodo HSPA SIM


		//Taking the existing koodo account
		PCSPostpaidConsumerAccount koodoAccount = (PCSPostpaidConsumerAccount)accountManager.findAccountByBAN0(70614284);
		PCSSubscriber koodoPcsSubscriber   = koodoAccount.newPCSSubscriber(serialNumber, true, "EN");
		PricePlan pricepaln =  koodoPcsSubscriber.getAvailablePricePlan("3PNTW15");

		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup numbergroup = findNumberGroupbyPhoneNumber(koodoPcsSubscriber);
		reservation.setNumberGroup(numbergroup);
		reservation.setPhoneNumberPattern(phNum); // setting the available phNum for porting
		koodoPcsSubscriber.setFirstName("YANaresh");
		koodoPcsSubscriber.setLastName("YADoe");
		koodoPcsSubscriber.setLanguage("EN");
		koodoPcsSubscriber.setDealerCode("0000000008");
		koodoPcsSubscriber.setSalesRepId("0000");
		koodoPcsSubscriber.setBirthDate(new Date(1960, 01, 01));
		koodoPcsSubscriber.setEmailAddress("eagletest@telusmobility.com");
		koodoPcsSubscriber.setActivityReasonCode("CMER");

		System.out.println("activationOptions for account ,in that takeing first one for save"); 		
		CreditCheckResult eligibility = koodoAccount.checkNewSubscriberEligibility(1, 0);
		ActivationOption [] ActivationOption = eligibility.getActivationOptions();

		koodoPcsSubscriber.newContract(pricepaln , 0);

		System.out.println("portInEligibility started");
		PortRequestManager portRequestManager = provider.getPortRequestManager();
		PortInEligibility portInEligibility = portRequestManager.testPortInEligibility(phNum, PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, Brand.BRAND_ID_KOODO);
		System.out.println("portInEligibility end ");

		System.out.println("Save method started");
		//create PortIn Request for available phone number
		createPortInRequest(phNum, "A", true , koodoPcsSubscriber);

		koodoPcsSubscriber.reservePhoneNumber(reservation,portInEligibility);

		koodoPcsSubscriber.save(activate, ActivationOption[0], portInEligibility);

		System.out.println("*******  ended testSaveTelus2Koodo method   *******  ");


	}

	public void testSaveKoodo2Telus() throws Exception {

		System.out.println("*******  started testSaveKoodo2Telus method   *******  ");	

		boolean activate = true;
		String phNum = "6471268778"; // Koodo PhNum which we got from WLNP team
		String serialNumber = "8912239900000595605"; // telus HSPA SIM

		//Taking the existing telus account
		PCSPostpaidConsumerAccount telusAccount = (PCSPostpaidConsumerAccount)accountManager.findAccountByBAN0(70551753);
		PCSSubscriber telusPcsSubscriber   = telusAccount.newPCSSubscriber(serialNumber, true, "EN");
		PricePlan pricepaln =  telusPcsSubscriber.getAvailablePricePlan("PTCAE250");

		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup numbergroup = findNumberGroupbyPhoneNumber(telusPcsSubscriber);
		reservation.setNumberGroup(numbergroup);
		reservation.setPhoneNumberPattern(phNum); // setting the available phNum for porting
		telusPcsSubscriber.setFirstName("YANaresh");
		telusPcsSubscriber.setLastName("YADoe");
		telusPcsSubscriber.setLanguage("EN");
		telusPcsSubscriber.setDealerCode("0000000008");
		telusPcsSubscriber.setSalesRepId("0000");
		telusPcsSubscriber.setBirthDate(new Date(1960, 01, 01));
		telusPcsSubscriber.setEmailAddress("eagletest@telusmobility.com");
		telusPcsSubscriber.setActivityReasonCode("CMER");

		System.out.println("activationOptions for account ,in that takeing first one for save"); 		
		CreditCheckResult eligibility = telusAccount.checkNewSubscriberEligibility(1, 0);
		ActivationOption [] ActivationOption = eligibility.getActivationOptions();

		telusPcsSubscriber.newContract(pricepaln , 12);

		System.out.println("portInEligibility started");
		PortRequestManager portRequestManager = provider.getPortRequestManager();
		PortInEligibility portInEligibility = portRequestManager.testPortInEligibility(phNum, PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, Brand.BRAND_ID_TELUS);
		System.out.println("portInEligibility end ");

		System.out.println("createPortInRequest method started");
		//create PortIn Request for available phone number
		createPortInRequest(phNum, "A", true , telusPcsSubscriber);
		System.out.println("createPortInRequest method started");
		//reserving the phoneNumber for new incoming brand
		telusPcsSubscriber.reservePhoneNumber(reservation,portInEligibility);
		//save the porting phoneNumber
		System.out.println("Save method started");
		telusPcsSubscriber.save(activate, ActivationOption[0], portInEligibility);
		System.out.println("Save method end");

		System.out.println("*******  ended testSaveKoodo2Telus method   *******  ");


	}

	public void testActivateTelus2Koodo() throws Exception {

		System.out.println("*******  started testActivateTelus2Koodo method   *******  ");

		boolean activate = false;
		boolean isPortIn = true;
		boolean modifyPortRequest = true;
		String reason = "ACOR";
		String phNum = "4031654622"; // Telus PhNum which we got from WLNP team
		String serialNumber = "8912230000024385106"; // koodo HSPA SIM

		//Taking the existing koodo account
		PCSPostpaidConsumerAccount koodoAccount = (PCSPostpaidConsumerAccount)accountManager.findAccountByBAN0(70614284);
		PCSSubscriber koodoPcsSubscriber   = koodoAccount.newPCSSubscriber(serialNumber, true, "EN");
		PricePlan pricepaln =  koodoPcsSubscriber.getAvailablePricePlan("3SDATIER");

		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup numbergroup = findNumberGroupbyPhoneNumber(koodoPcsSubscriber);
		reservation.setNumberGroup(numbergroup);
		reservation.setPhoneNumberPattern(phNum); // setting the available phNum for porting
		koodoPcsSubscriber.setFirstName("YANaresh");
		koodoPcsSubscriber.setLastName("YADoe");
		koodoPcsSubscriber.setLanguage("EN");
		koodoPcsSubscriber.setDealerCode("0000000008");
		koodoPcsSubscriber.setSalesRepId("0000");
		koodoPcsSubscriber.setBirthDate(new Date(1960, 01, 01));
		koodoPcsSubscriber.setEmailAddress("eagletest@telusmobility.com");
		koodoPcsSubscriber.setActivityReasonCode("CMER");

		System.out.println("activationOptions for account ,in that takeing first one for save"); 		
		CreditCheckResult eligibility = koodoAccount.checkNewSubscriberEligibility(1, 0);
		ActivationOption [] ActivationOption = eligibility.getActivationOptions();

		koodoPcsSubscriber.newContract(pricepaln , 0);

		System.out.println("portInEligibility started");
		PortRequestManager portRequestManager = provider.getPortRequestManager();
		PortInEligibility portInEligibility = portRequestManager.testPortInEligibility(phNum, PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, Brand.BRAND_ID_KOODO);
		System.out.println("portInEligibility end ");

		System.out.println("Save method started");
		//create PortIn Request for available phone number
		createPortInRequest(phNum, "A", true , koodoPcsSubscriber);
		//reserving the phoneNumber for new brand
		koodoPcsSubscriber.reservePhoneNumber(reservation,portInEligibility);
		//Saving the phoneNumber with activate false
		koodoPcsSubscriber.save(activate, ActivationOption[0], portInEligibility);
		//activate the porting phoneNumber
		koodoPcsSubscriber.activate(reason, new Date(), "Sample memo ", isPortIn, modifyPortRequest);
		System.out.println("*******  ended testActivateTelus2Koodo method   *******  ");


	}

	public void testActivateKoodo2Telus() throws Exception {

		System.out.println("*******  started testActivateKoodo2Telus method   *******  ");	

		boolean activate = false;
		boolean isPortIn = true;
		boolean modifyPortRequest = true;
		String reason = "ACOR";
		String phNum = "4031654563"; // Koodo PhNum which we got from WLNP team
		String serialNumber = "8912230000000280578"; // telus HSPA SIM

		//Taking the existing telus account
		PCSPostpaidConsumerAccount telusAccount = (PCSPostpaidConsumerAccount)accountManager.findAccountByBAN0(70551753);
		PCSSubscriber telusPcsSubscriber   = telusAccount.newPCSSubscriber(serialNumber, true, "EN");
		PricePlan pricepaln =  telusPcsSubscriber.getAvailablePricePlan("PTCAE250");

		PhoneNumberReservation reservation;
		reservation = api.getAccountManager().newPhoneNumberReservation();
		NumberGroup numbergroup = findNumberGroupbyPhoneNumber(telusPcsSubscriber);
		reservation.setNumberGroup(numbergroup);
		reservation.setPhoneNumberPattern(phNum); // setting the available phNum for porting
		telusPcsSubscriber.setFirstName("YANaresh");
		telusPcsSubscriber.setLastName("YADoe");
		telusPcsSubscriber.setLanguage("EN");
		telusPcsSubscriber.setDealerCode("0000000008");
		telusPcsSubscriber.setSalesRepId("0000");
		telusPcsSubscriber.setBirthDate(new Date(1960, 01, 01));
		telusPcsSubscriber.setEmailAddress("eagletest@telusmobility.com");
		telusPcsSubscriber.setActivityReasonCode("CMER");

		System.out.println("activationOptions for account ,in that takeing first one for save"); 		
		CreditCheckResult eligibility = telusAccount.checkNewSubscriberEligibility(1, 0);
		ActivationOption [] ActivationOption = eligibility.getActivationOptions();

		telusPcsSubscriber.newContract(pricepaln , 12);

		System.out.println("portInEligibility started");
		PortRequestManager portRequestManager = provider.getPortRequestManager();
		PortInEligibility portInEligibility = portRequestManager.testPortInEligibility(phNum, PortInEligibility.PORT_VISIBILITY_TYPE_EXTERNAL_2H, Brand.BRAND_ID_TELUS);
		System.out.println("portInEligibility end ");

		System.out.println("Save method started");
		//create PortIn Request for porting phone number
		createPortInRequest(phNum, "A", true , telusPcsSubscriber);
		//reserving the phoneNumber for new brand
		telusPcsSubscriber.reservePhoneNumber(reservation,portInEligibility);
		//Saving the phoneNumber with activate false
		telusPcsSubscriber.save(activate, ActivationOption[0], portInEligibility); 
		//activate the porting phoneNumber
		telusPcsSubscriber.activate(reason, new Date(), "Sample memo ", isPortIn, modifyPortRequest);
		System.out.println("*******  ended testSaveKoodo2Telus method   *******  ");


	}
	public void testRestoreSubscriber() throws PortRequestException,PRMSystemException, TelusAPIException , Exception{


		System.out.println("start testRestoreSubscriber method");

		Date activityDate = new Date();
		String reason = "CR";
		String phNum = "4161658090"; // telus num from KB DB
		String memoText = "test memo for subscriber"+phNum;
		Account account = accountManager.findAccountByBAN0(70648637);
		Subscriber subscriber = account.getSubscriberByPhoneNumber(phNum);


		PortInEligibilityInfo portInEligibility = new PortInEligibilityInfo();
		portInEligibility.setPhoneNumber("4161658090");
		portInEligibility.setIncomingBrandId(1);
		portInEligibility.setOutgoingBrandId(3);

		String portOption = "W";
		//create PortIn Request for available phone number
		createPortInRequest(phNum, "A", true , (PCSSubscriber)subscriber);
		TMSubscriber tmsubscriber = (TMSubscriber)subscriber;
		System.out.println("start Restore  method started");
		tmsubscriber.restore(activityDate, reason, memoText, portOption, portInEligibility);

		System.out.println("end testRestoreSubscriber method");
	}


	//Koodo SmartPay Test Cases Begin

	public void testApplyingDiscountsAtSubscriberLevelForSuccesCases()
	{
		try {
			/** success Scenarios */
			// 1.Applying discount
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("6MTPD5");
			discount.setEffectiveDate(getDateInput(1986,03,16));
			discount.setExpiryDate(getDateInput(2013,03,17));
			// discount.apply();

			// 2.Retrieving existing discounts on subscriber
			Subscriber subscriber1 = accountManager.findSubscriberByPhoneNumber("5141661867");
			Discount [] discounts1 = subscriber1.getDiscounts();
			for (int i = 0; i < discounts1.length; i++) {
				System.out.println("Exististing discounts on given subscriber"+discounts1[i].toString());	
			}

			System.out.println("start mofifying discount");
			// modifying existing discount as suggested by Canh test case
			Discount[] discounts2 = subscriber1.getDiscounts();
			Discount newDiscount = subscriber1.newDiscount();
			newDiscount.setDiscountCode(discounts2[0].getDiscountCode());
			newDiscount.setExpiryDate(getDateInput(2013, 05, 25));
			//newDiscount.apply();
			System.out.println("end mofifying discount");



			//3. modify the existing discount
			Subscriber subscriber2 = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount[] discounts = (TMDiscount[])subscriber2.getDiscounts();
			//discounts[0].setEffectiveDate(getDateInput(2013,03,21));
			discounts[0].setExpiryDate(getDateInput(2013,05,25));
			discounts[0].apply();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	public void testApplyingDiscountsAtSubscriberLevelForErrorCases()  {

		
		/** failure Scenarios */

		try {
			// 1 .adding existing discount code again
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("XTSD30C");
			discount.setEffectiveDate(getDateInput(2013,03,13));
			discount.setExpiryDate(getDateInput(2013,03,17));
			//discount.apply();
		} catch (TelusAPIException ex) {
			assertEquals(": The given discount has already been applied to the BAN/product.", ex.getApplicationMessage());
		}

		try {
			// 2 .Invalid  discount code 
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("XTSD30C1");
			discount.setEffectiveDate(getDateInput(2013,03,13));
			discount.setExpiryDate(getDateInput(2013,03,17));
			//discount.apply();
		} catch (Exception ex) {
			assertEquals(": The discount XTSD30C1 is not valid.", ex.getMessage());
		}
		try {
			// 3 .Applying Ban level discount code at subscriberLevel
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("BD10PCSB");
			discount.setEffectiveDate(getDateInput(2013, 03, 16));
			discount.setExpiryDate(getDateInput(2013, 03, 28));
			discount.apply();
		} catch (Exception ex) {
			assertEquals(": Ban level discounts cannot be applied to a Product.", ex.getMessage());
		}

		try {
			// 4.Giving expire Date before effective Date
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("XTSD30C");
			discount.setEffectiveDate(getDateInput(2013, 03, 31));
			discount.setExpiryDate(getDateInput(2013, 03, 28));
			discount.apply();
		} catch (Exception ex) {
			assertEquals(": Effective Date is invalid.", ex.getMessage());
		}

		try {
			// 5 .empty discount code
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("");
			discount.setEffectiveDate(getDateInput(2013, 03, 31));
			discount.setExpiryDate(getDateInput(2013, 04, 28));
			discount.apply();
		} catch (Exception ex) {
			assertEquals(": The discount  is not valid.", ex.getMessage());
		}	
		
		try {
			// 6 .passing effective  before DiscountPlan.getEffectiveDate
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("6MTPD5");
			discount.setEffectiveDate(getDateInput(1979, 03, 31));
			discount.setExpiryDate(getDateInput(2013, 04, 28));
			//discount.apply();
		} catch (Exception ex) {
			ex.printStackTrace();
			assertEquals(": Effective Date is invalid.", ex.getMessage());
		}
		
		boolean exceptionoccured = false;
		try {
			// 7.passing effective  is past date , i.e before todays's date
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("6MTPD5");
			discount.setEffectiveDate(getDateInput(2012, 03, 31));
			discount.setExpiryDate(getDateInput(2013, 04, 28));
			//discount.apply();
		} catch (Exception ex)
		{
			exceptionoccured  = true;
			assertEquals(": Effective Date is invalid.", ex.getMessage());
		}
		finally {
			assertEquals(true, exceptionoccured);
		}
		
		boolean exceptionoccured1 = false;
		try {
			// 8.passing setExpiryDate is longer than the DiscountPlan.getMonths
			// DiscountPlan.getMonths will return 6 for discount " 6MTPD5 " , but we are passing discount term more than year for this year
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("6MTPD5");  
			discount.setEffectiveDate(getDateInput(2013, 04, 03));
			discount.setExpiryDate(getDateInput(2014, 05, 28));
			discount.apply();
		} catch (Exception ex) {
			ex.printStackTrace();
			assertEquals(": Invalid term plan", ex.getMessage());
		}
		finally {
			assertEquals(true, exceptionoccured1);
		}
		
		try {
			// 9.passing koodo brand discount on telus subscriber
			Subscriber subscriber = accountManager.findSubscriberByPhoneNumber("5141661867");
			TMDiscount discount = (TMDiscount) subscriber.newDiscount();
			discount.setDiscountCode("3RD50");
			discount.setEffectiveDate(getDateInput(2013, 04, 03));
			discount.setExpiryDate(getDateInput(2013, 04, 28));
			//discount.apply();
		} catch (Exception ex) {
			ex.printStackTrace();
			assertEquals(": Discount code is not  valid /select telus brand discount code..", ex.getMessage());
		}
	}

	public void testgetAccountStatusChangeAfterSuspendOrCancel() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		System.out.println("start testgetAccountStatusChangeAfterSuspendOrCancel...*");
		Account ai = accountManager.findAccountByBAN0(70792965);
		TMSubscriber subscriber = (TMSubscriber)ai.getSubscriberByPhoneNumber("4161532929");
		char status = subscriber.getAccountStatusChangeAfterCancel();
		System.out.println("AccountStatusChangeAfterCancel"+status);
		System.out.println("end testgetAccountStatusChangeAfterSuspendOrCancel");
	}

}
