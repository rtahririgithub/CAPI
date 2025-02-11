package com.telus.cmb.subscriber.lifecyclemanager.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.DiscountPlan;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome;
import com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperRemote;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.impl.SubscriberLifecycleFacadeHome;
import com.telus.cmb.subscriber.lifecyclefacade.svc.impl.SubscriberLifecycleFacadeRemote;
import com.telus.cmb.subscriber.lifecyclehelper.svc.impl.SubscriberLifecycleHelperHome;
import com.telus.cmb.subscriber.lifecyclehelper.svc.impl.SubscriberLifecycleHelperRemote;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.ServicesValidationInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.MigrationTypeInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.PricePlanInfo;

//@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class SubscriberLifeCycleManagerImplIntTest{


	/*	@Autowired
	SubscriberLifeCycleManagerImpl managerImpl;

	@Test
	public void testSaveSubscriptionPreference() throws ApplicationException, RemoteException {

		SubscriptionPreferenceInfo preferenceInfo = new SubscriptionPreferenceInfo();
		preferenceInfo.setPreferenceTopicId(1);
		preferenceInfo.setSubscriptionId(12255794);
		preferenceInfo.setSubscriberPreferenceId(22);
		preferenceInfo.setSubscrPrefChoiceSeqNum(0);
		preferenceInfo.setPreferenceValueTxt("N");
		String user = "ABC";
		managerImpl.saveSubscriptionPreference(preferenceInfo, user);
	}
	 */

//	String url="t3://localhost:7168";
	//String url="t3://ln98560:40024";
	String url="t3://ln98557:31022"; //pt168
	//String url = "t3://ln98550:12024";
	SubscriberLifecycleManagerRemote managerImpl = null;
	String phoneNumber = "4037109998";
	String sessionId;
	
	ReferenceDataFacade referenceDataFacade = null;
	
	SubscriberLifecycleHelperRemote subscriberLifecycleHelper=null;
	ProductEquipmentHelperRemote helperImpl = null;
	SubscriberLifecycleFacadeRemote facadeImpl = null;
	AccountLifecycleManager alm = null;

	@Before
	public void setup() throws Exception {
		//System.setProperty("com.telusmobility.config.java.naming.provider.url"
				//, "ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration"); 
		
		//System.setProperty("cmb.services.SubscriberLifecycleHelper.url","t3://localhost:7001");
		
System.setProperty("com.telusmobility.config.java.naming.provider.url"
	,"ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");

//	System.setProperty("com.telusmobility.config.java.naming.provider.url"
//			,"ldap://ldapread-s.tmi.telus.com:1589/cn=s_81,o=telusconfiguration");
		
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getSubscriberLifecycleManagerRemote(context);		
	}

	private Hashtable<Object,Object> setEnvContext(){
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}

	private void getSubscriberLifecycleManagerRemote(Context context) throws Exception{
		SubscriberLifecycleManagerHome subscriberLifecycleManagerHome 
		= (SubscriberLifecycleManagerHome) context.lookup("SubscriberLifecycleManager#com.telus.cmb.subscriber.lifecyclemanager.svc.impl.SubscriberLifecycleManagerHome");
		managerImpl = (SubscriberLifecycleManagerRemote)subscriberLifecycleManagerHome.create();
		sessionId = managerImpl.openSession("18654", "apollo", "SMARTDESKTOP");
		referenceDataFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, "ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome");
//		ReferenceDataFacadeHome referenceDataFacadeHome
//		=  (ReferenceDataFacadeHome) context.lookup("ReferenceDataFacade#com.telus.cmb.reference.svc.impl.ReferenceDataFacadeHome");
//		referenceDataFacade = (ReferenceDataFacadeRemote)referenceDataFacadeHome.create();

		SubscriberLifecycleHelperHome subscriberInformationHelperHome
		= (SubscriberLifecycleHelperHome) context.lookup("SubscriberLifecycleHelper#com.telus.cmb.subscriber.lifecyclehelper.svc.impl.SubscriberLifecycleHelperHome");																																		
		subscriberLifecycleHelper = (SubscriberLifecycleHelperRemote) subscriberInformationHelperHome.create();

		SubscriberLifecycleFacadeHome subscriberLifecycleFacadeHome 
		= (SubscriberLifecycleFacadeHome) context.lookup("SubscriberLifecycleFacade#com.telus.cmb.subscriber.lifecyclefacade.svc.impl.SubscriberLifecycleFacadeHome");
		facadeImpl = (SubscriberLifecycleFacadeRemote)subscriberLifecycleFacadeHome.create();
		ProductEquipmentHelperHome productEquipmentHelperHome 
		= (ProductEquipmentHelperHome) context.lookup("ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome");
		helperImpl = (ProductEquipmentHelperRemote)productEquipmentHelperHome.create();
		
		alm = (AccountLifecycleManager) context.lookup(EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);
	}
	
	
	@Test
	@Ignore
	public void testSaveActivationFeaturesPurchaseArrangement() throws Exception {
		String userId="IntTest";
		ActivationFeaturesPurchaseArrangementInfo[] info = new ActivationFeaturesPurchaseArrangementInfo[1];
		ActivationFeaturesPurchaseArrangementInfo infoObj = new ActivationFeaturesPurchaseArrangementInfo();
		infoObj.setAutoRenewIndicator("12121");
		info[0]=infoObj;
		SubscriberInfo sub= new SubscriberInfo();
		sub.setBanId(132131);
		managerImpl.saveActivationFeaturesPurchaseArrangement(sub, info, userId);
	}
	@Test
	@Ignore
	public void testUpdateCallingCircleParameters() throws Exception {
		String userId="IntTest";
		String applicationId ="testapp";
		byte action=10;
		ServiceAgreementInfo serInf = new ServiceAgreementInfo();
		serInf.setTransaction(action, true, true);
		managerImpl.updatePrepaidCallingCircleParameters(applicationId, userId, phoneNumber, serInf, action);	
	}

	
	@Test
	public void testSaveSubscriptionPreference() throws RemoteException, ApplicationException {
		SubscriptionPreferenceInfo preferenceInfo = new SubscriptionPreferenceInfo();
		preferenceInfo.setPreferenceTopicId(1);
		preferenceInfo.setSubscriptionId(12255794);
		preferenceInfo.setSubscriberPreferenceId(22);
		preferenceInfo.setSubscrPrefChoiceSeqNum(0);
		preferenceInfo.setPreferenceValueTxt("N");
		String user = "ABC";		
		
		managerImpl.saveSubscriptionPreference(preferenceInfo, user);		
	}
	
	@Test
	public void testRetrieveDiscounts() throws RemoteException, ApplicationException {		

		int ban = 6001047;
		String subscriberId = "M000100380";
		String productType = Subscriber.PRODUCT_TYPE_IDEN;
		managerImpl.retrieveDiscounts(ban, subscriberId, productType, sessionId);
	}
	
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestNewPcsSubscriber() throws RemoteException, ApplicationException, TelusException {
		
	try{
		
		int ban = 194587;		
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("4037109998", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers =11;
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}catch(ApplicationException  ex){
		ex.printStackTrace();	
	}catch(Exception  ex){
		ex.printStackTrace();	
	}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestUpdatePcsSubscriber() throws TelusException, RemoteException, ApplicationException {		
		int ban = 194587;		
		String subscriberId = "4037109998";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("4037109998", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 11;
		
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestNewIdenSubscriber() throws RemoteException, ApplicationException, TelusException {
	
		try
		{
		int ban = 6001554;
		String subscriberId = "4033125025";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_IDEN);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("4033125025", Equipment.EQUIPMENT_TYPE_ALL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
		}
		catch(ApplicationException ae)
		{
			String ErrorMessage = "phoneNumberReservation.getNumberGroup() should not be null";
			System.out.println("Error Occured"+ErrorMessage);
		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestUpdateIdenSubscriber() throws TelusException, RemoteException, ApplicationException {		
		
		try
		{
			int ban = 6001047;		
		
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_IDEN);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("2047980182", Equipment.EQUIPMENT_TYPE_ALL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
		
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
		}
		catch (ApplicationException ae) {
			assertEquals("No available phone numbers found.", ae.getMessage());
		}
	}	
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestNewPagerSubscriber() throws RemoteException, ApplicationException, TelusException {
		try
		{
		int ban = 44138776;		
		String subscriberId = "6047350174";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PAGER);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("6047350174", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		} 
		} catch (ApplicationException ae) {
			String mesage = "No available phone numbers found.";
			assertEquals(mesage, ae.getMessage());
		}
		
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestUpdatePagerSubscriber() throws TelusException, RemoteException, ApplicationException {		
		
	try
		{
		int ban = 44138776;			
		String subscriberId = "6047350174";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PAGER);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("6047350174", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
			
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
		}
	catch (ApplicationException ae) {
		String mesage = "No available phone numbers found.";
		assertEquals(mesage, ae.getMessage());
	}
	}	
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestNewTangoSubscriber() throws RemoteException, ApplicationException, TelusException {
		int ban = 44137500;		
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_TANGO);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("6045565524", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 4;
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestUpdateTangoSubscriber() throws TelusException, RemoteException, ApplicationException {		
		int ban = 44138156;		
		String subscriberId = "6045565525";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_TANGO);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("6045565525", Equipment.EQUIPMENT_TYPE_DIGITAL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 4;
		
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestNewCdpdSubscriber() throws RemoteException, ApplicationException, TelusException {
		
		try{
		int ban = 40000112;		
		String subscriberId = "";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_CDPD);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("2047980182", Equipment.EQUIPMENT_TYPE_ALL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}
 catch (ApplicationException ae) {
	 assertEquals("No available phone numbers found.", ae.getMessage());

		}
	}
	
	@Test
	public void testRetrieveAvailablePhoneNumbersTestUpdateCdpdSubscriber() throws TelusException, RemoteException, ApplicationException {		
		int ban = 40000009;		
		String subscriberId = "198228046195";
		PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
		phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_CDPD);
		phoneNumberReservation.setPhoneNumberPattern("");
		
		NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("198228046195", Equipment.EQUIPMENT_TYPE_ALL);		
		phoneNumberReservation.setNumberGroup(numberGroupInfo);		
		int maxNumbers = 100;
		
		
		AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
			= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
		
		assertEquals(retrieveAvailablePhoneNumbers.length, maxNumbers);
		
		for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
			System.out.println(availablePhoneNumber);
		}
	}
	@Test
	public void testCreateSubscriber() throws RemoteException, ApplicationException {
		try
		{
		SubscriberInfo subscriberInfo = new SubscriberInfo();
		subscriberInfo.setProductType(Subscriber.PRODUCT_TYPE_PCS);
		subscriberInfo.setBanId(17859);
		subscriberInfo.setSubscriberId("4038502143");	
		SubscriberContractInfo subscriberContractInfo = null;
		boolean activate = false;
		boolean overridePatternSearchFee = false;
		String activationFeeChargeCode = null;
		boolean dealerHasDeposit = false;
		boolean portedIn = false;
		ServicesValidation srvValidation = null;
		String portProcessType = null;
		int oldBanId = 0;
		String oldSubscriberId = null;
		managerImpl.createSubscriber(subscriberInfo, subscriberContractInfo
				, activate, overridePatternSearchFee, activationFeeChargeCode
				, dealerHasDeposit, portedIn, srvValidation
				, portProcessType, oldBanId, oldSubscriberId, sessionId);
		} catch (ApplicationException ae) {
			assertEquals("Can not Activate/Save Subscriber that is not reserved.", ae.getMessage());
		}
	}
	
	@Test
	public void testUpdateAddress() throws ApplicationException,RemoteException{
		System.out.println("testUpdateAddress Start");
		
		int ban=197806;
		String subscriber="4037109656";
		String productType="C";
		AddressInfo addressInfo=new AddressInfo();
		addressInfo.setProvince("ON");
		addressInfo.setStreetName("58, Stoneton Dr");
		addressInfo.setPostalCode("M1h2P6");
		addressInfo.setCity("Toronto");
		
		managerImpl.updateAddress(ban, subscriber,productType, addressInfo, sessionId);
		
		System.out.println("testUpdateAddress End");
	}	
	
	/*Integration test will be done when provider changes are completed for ChangePricePlan*/
	@Test
	public void testChangePricePlan() throws ApplicationException,RemoteException, TelusException{
		System.out.println("testChangePricePlan Start");
		
		String phNumber="5141852028";
		try{
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
		SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("5141852028");
		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
		pricePlanValidation.setCurrentValidation(false);
		PricePlanInfo pricePlan = referenceDataFacade.getPricePlan("PVDF65PD ");
		//PricePlanInfo pricePlan=new PricePlanInfo();
		pricePlan.setActive(true);
		subscriberContractInfo.setPricePlanInfo(pricePlan);

		
		managerImpl.changePricePlan(subscriberInfo, subscriberContractInfo, subscriberInfo.getDealerCode(), 
				subscriberInfo.getSalesRepId(), pricePlanValidation, sessionId);
		}catch(ApplicationException aex){
			aex.printStackTrace();
			assertEquals("1117020",aex.getErrorCode());
		}
		System.out.println("testChangePricePlan End");
	}
	
	/*Integration test will be done when provider changes are completed for ChangeServiceAgreement*/
	@Test
	public void testChangeServiceAgreement() throws ApplicationException,RemoteException{
		System.out.println("testChangeServiceAgreement Start");
//		String phNumber="4168940042";
//		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
//		SubscriberContractInfo subscriberContractInfo=subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber(phNumber);
//		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
//		pricePlanValidation.setCurrentValidation(false);
		String phNumber="4037109656";
		try{
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
		//SubscriberContractInfo subscriberContractInfo=subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber(phNumber);
		SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("4037109656");

		PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
		pricePlanValidation.setCurrentValidation(false);
		PricePlanInfo pricePlan=new PricePlanInfo();
		pricePlan.setActive(true);
		subscriberContractInfo.setPricePlanInfo(pricePlan);
		managerImpl.changeServiceAgreement(subscriberInfo, subscriberContractInfo,  
				subscriberInfo.getDealerCode(), subscriberInfo.getSalesRepId(), pricePlanValidation, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1117020",aex.getErrorCode());
		}
		System.out.println("testChangeServiceAgreement End");
	}	
	
	/*Integration test will be done when provider changes are completed for ActivateReservedSubscriber*/
	@Test
	public void testActivateReservedSubscriber() throws ApplicationException, RemoteException{
		System.out.println("testActivateReservedSubscriber Start");
		String phNumber="4037109656";
		try{
			SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phNumber);
			SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("4037109656");

			PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
			pricePlanValidation.setCurrentValidation(false);
			PricePlanInfo pricePlan=new PricePlanInfo();
			pricePlan.setActive(true);
			subscriberContractInfo.setPricePlanInfo(pricePlan);

		Date startServiceDate = new Date();
		String activityReasonCode="test";
		ServicesValidation srvValidation = new ServicesValidationInfo();
		String portProcessType ="test";
		int oldBanId=254977;
		String oldSubscriberId="4033317837";
		managerImpl.activateReservedSubscriber(subscriberInfo, subscriberContractInfo, startServiceDate ,
				activityReasonCode, srvValidation , portProcessType , oldBanId, oldSubscriberId, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115110",aex.getErrorCode());
			assertEquals("Can not Activate/Save Subscriber that is not reserved.",aex.getErrorMessage());
		}
		System.out.println("testActivateReservedSubscriber End");
	}
	
	@Test
	public void testDeleteFutureDatedPricePlan() throws ApplicationException, RemoteException{
		try
		{
		System.out.println("testDeleteFutureDatedPricePlan Start");
		int ban=292007;
		String subscriberId="7807198318";
		String productType="C";
		
		managerImpl.deleteFutureDatedPricePlan(ban, subscriberId, productType,sessionId);
		
		System.out.println("testDeleteFutureDatedPricePlan End");
		}
		catch (ApplicationException e) {
		assertEquals("Could not find future price plan to delete.", e.getMessage());
		}
	}
	
	@Test
	public void testRetrieveCallingCircleParameters() throws ApplicationException, RemoteException{
		try
		{
		System.out.println("testRetrieveCallingCircleParameters Start");
		
		int banId=12474;
		String subscriberNo="4033946834";
		String soc="SCLID0";
		String featureCode="CCAIR";
		String productType="C";
		
		CallingCircleParameters callingCircleParameters= managerImpl.retrieveCallingCircleParameters(banId, subscriberNo, soc, featureCode, productType, sessionId);
		System.out.println("callingCircleParameters :"+callingCircleParameters);
		System.out.println("testRetrieveCallingCircleParameters End");
		}
		catch(ApplicationException ae)
		{
			assertEquals("1117570", ae.getErrorCode());
		}
	}
	
	@Test
	public void testUpdateCommitment() throws ApplicationException, RemoteException{
		System.out.println("testUpdateCommitment Start");
		
		String subscriberNo="4033946834";
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		CommitmentInfo pCommitmentInfo = new CommitmentInfo();
	
		
		managerImpl.updateCommitment(subscriberInfo, pCommitmentInfo, subscriberInfo.getDealerCode(), 
				subscriberInfo.getSalesRepId(), sessionId);
		
		System.out.println("testUpdateCommitment End");
	}
	
	@Test
	public void testCreateDeposit() throws ApplicationException, RemoteException{
		System.out.println("testCreateDeposit Start");
		String subscriberNo="4033946834";//Status = 'A'
	
		SubscriberInfo subscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		System.out.println("Status: "+subscriberInfo.getStatus());
		double amount=120.89;
		String memoText="testing";
		
		managerImpl.createDeposit(subscriberInfo, amount, memoText, sessionId);
		System.out.println("testCreateDeposit End");
	}	
	
	@Test
	public void testCancelSubscriber() throws ApplicationException, RemoteException{
		
		try{
			
		System.out.println("testCancelSubscriber Start");
		
		String subscriberNo="2047980182";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		Date pActivityDate=new Date();
		String pActivityReasonCode="AIE";
		String pDepositReturnMethod="R";
		String pWaiveReason="FEW";
		String pUserMemoText="memo";
		
		managerImpl.cancelSubscriber(pSubscriberInfo, pActivityDate, pActivityReasonCode,	pDepositReturnMethod, pWaiveReason, pUserMemoText, false,sessionId); 	
		}catch(ApplicationException ex){
			
			assertEquals("1111750",ex.getErrorCode());
			
		}
		System.out.println("testCancelSubscriber End");
	}
	
	@Test
	public void testRefreshSwitch() throws ApplicationException, RemoteException{
		System.out.println("testRefreshSwitch Start");
		
		int pBan=750714;
		String pSubscriberId="5196351113";
		String pProductType="C";
		managerImpl.refreshSwitch(pBan, pSubscriberId, pProductType, sessionId);
		
		System.out.println("testRefreshSwitch End");
	}
	
	@Test
	public void testRestoreSuspendedSubscriber() throws ApplicationException, RemoteException{
		try
		{
		System.out.println("testRestoreSuspendedSubscriber Start");
		String subscriberNo="4036507804";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberNo);
		Date pActivityDate=new Date();
		String pActivityReasonCode="CNV";
		String pUserMemoText="memo";
		boolean portIn=true;
		
		managerImpl.restoreSuspendedSubscriber(pSubscriberInfo, pActivityDate, pActivityReasonCode, pUserMemoText, portIn, sessionId);
		}
		catch(ApplicationException ae)
		{
			String errorMessage="Subscriber is not ported-out. Restore suspended subscriber can not be performed.";
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		System.out.println("testRestoreSuspendedSubscriber End");
	}
	
	@Test
	public void testResumeCancelledSubscriber() throws ApplicationException, RemoteException{
		System.out.println("testResumeCancelledSubscriber Start");
		try{
		String subscriberNo="4033505995";
		SubscriberInfo pSubscriberInfo=subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(3720, subscriberNo);
		String pActivityReasonCode="INPO";
		String pUserMemoText="memo";
		boolean portIn=false;
		String portProcessType=null;
		int oldBanId=0;
		String oldSubscriberId=null;
		
		managerImpl.resumeCancelledSubscriber(pSubscriberInfo, pActivityReasonCode, pUserMemoText, 
				portIn, portProcessType, oldBanId, oldSubscriberId, sessionId);
		}catch(ApplicationException ae){
			String errorMessage="Cannot resume the SUBSCRIBER as the last activity performed on this subscriber was Move CTN.";
			assertEquals(errorMessage, ae.getErrorMessage());
		}
		System.out.println("testResumeCancelledSubscriber End");
	}
	

	@Test
	public void testRetrieveSubscribersByMemberIdentity() throws ApplicationException, RemoteException{
		System.out.println("testRetrieveSubscribersByMemberIdentity Start");
		
		int urbanId=131077;
		int fleetId=905;
		int memberId=19069;

	
		assertEquals(0, (managerImpl.retrieveSubscribersByMemberIdentity(urbanId, fleetId, memberId, sessionId)).size());
		System.out.println("testRetrieveSubscribersByMemberIdentity End");
	}
	
	
	@Test
	public void testAddMemberIdentity() throws ApplicationException, RemoteException{
		System.out.println("testAddMemberIdentity Start");
		
		try{
			int urbanId=131077;
			int fleetId=905;
			int banId = 194587;		
			String subscriberNo = "4037109998";
			
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberNo);
			iDENSubscriberInfo.setBanId(banId);
			
			managerImpl.addMemberIdentity(iDENSubscriberInfo, new SubscriberContractInfo(), "D101", "10001",
					urbanId, fleetId, "abc", true, sessionId);
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115060", errorCode );
	
		}

		System.out.println("testAddMemberIdentity End");
	}	
	
	@Test
	public void testChangeMemberId() throws ApplicationException, RemoteException{
		System.out.println("testChangeMemberId Start");
	
		try{
			
			int banId = 194587;		
			String subscriberNo = "4037109998";
			
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberNo);
			iDENSubscriberInfo.setBanId(banId);
			
			managerImpl.changeMemberId(iDENSubscriberInfo, "101",sessionId);

		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115060", errorCode );
	
		}
		
		System.out.println("testChangeMemberId End");
	}
	
	
	@Test
	public void testChangeMemberIdentity() throws ApplicationException, RemoteException{
		
		try{
			System.out.println("testChangeMemberIdentity Start");
		
			int ban = 194587;
			String subscriberId = "4037109998";
			int urbanId=131077;
			int fleetId=905;
			String memberId="19069";
	
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberId);
			iDENSubscriberInfo.setBanId(ban);
			
			managerImpl.changeMemberIdentity(iDENSubscriberInfo, urbanId, fleetId, memberId, sessionId);
		
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115060", errorCode );
		}	
		System.out.println("testChangeMemberIdentity End");
	}
	
	
	@Test
	public void testChangeTalkGroups() throws RemoteException, ApplicationException{
		
		try{
			System.out.println("testChangeTalkGroups Start");
		
			int ban = 194587;
			String subscriberId = "4037109998";
			int urbanId=131077;
			int fleetId=905;
	
			
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberId);
			iDENSubscriberInfo.setBanId(ban);
			
	//		String subscriber="M000000203";
	//		List<TalkGroupInfo> list = new ArrayList<TalkGroupInfo>();
	//		list=dao.retrieveTalkGroupsBySubscriber(subscriber);
			
			TalkGroupInfo[] talkGroupInfo=new TalkGroupInfo[1];
			talkGroupInfo[0]=new TalkGroupInfo();
			talkGroupInfo[0].getFleetIdentity().setUrbanId(urbanId);
			
			TalkGroupInfo[] talkGroupInfo1=new TalkGroupInfo[1];
			talkGroupInfo1[0]=new TalkGroupInfo();
			talkGroupInfo1[0].getFleetIdentity().setUrbanId(urbanId);
			
			managerImpl.changeTalkGroups(iDENSubscriberInfo, talkGroupInfo, talkGroupInfo1,sessionId);
			
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("VAL20006", errorCode );

		}
		
		System.out.println("testChangeTalkGroups End");
	}
	
	
	

	@Test
	public void testAvailableMemberIDs() throws RemoteException, ApplicationException{
	
		try{
			System.out.println("testAvailableMemberIDs Start");
			
			int urbanId=131077;
			int fleetId=905;
			int[] memIDs = managerImpl.getAvailableMemberIDs(urbanId, fleetId, "***", 3, sessionId);
			System.out.println("Memo Id's length"+memIDs.length);
		
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("SYS00015", errorCode );
		}
		
		System.out.println("testAvailableMemberIDs End");

	}
	
	@Test
	public void testRemoveMemberIdentity() throws RemoteException, ApplicationException{
		
		System.out.println("testRemoveMemberIdentity Start");

		try{	
			int ban = 194587;
			String subscriberId = "4037109998";
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberId);
			iDENSubscriberInfo.setBanId(ban);
			
			managerImpl.removeMemberIdentity(iDENSubscriberInfo, new SubscriberContractInfo(), "D112", "101", true,sessionId);
		
		
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115060", errorCode );
		}
		
		System.out.println("testRemoveMemberIdentity End");
	}
	
	
	@Test
	public void testReserveMemberId() throws RemoteException, ApplicationException, TelusException{
		
		System.out.println("testReserveMemberId Start");

		try{	
			int ban = 194587;
			String subscriberId = "4037109998";
			IDENSubscriberInfo iDENSubscriberInfo=new IDENSubscriberInfo();
			iDENSubscriberInfo.setPhoneNumber(subscriberId);
			iDENSubscriberInfo.setBanId(ban);
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_ALL);				
			iDENSubscriberInfo.setNumberGroup(numberGroupInfo);			
			managerImpl.reserveMemberId(iDENSubscriberInfo,sessionId);
		
		}catch(ApplicationException ex){		
			
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1110560", errorCode );
		}catch(Exception ex){		
			
		
		}
		
		System.out.println("testReserveMemberId End");
	}
	 
	
	@Test
	public void testReserveMemberId_1() throws RemoteException, ApplicationException, TelusException{
		
		System.out.println("testReserveMemberId_1 Start");

		try{	
			int ban = 860942;
			String subscriberId = "7057155047";

			IDENSubscriberInfo pSubscriberInfo=(IDENSubscriberInfo)subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);

			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_ALL);				
			pSubscriberInfo.setNumberGroup(numberGroupInfo);		
			managerImpl.reserveMemberId(pSubscriberInfo,pSubscriberInfo.getFleetIdentity(), "%",sessionId);
		
		}catch(ApplicationException ex){		
			
			ex.printStackTrace();
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1110560", errorCode );
		}catch(Exception ex){					
			ex.printStackTrace();

		}
		
		System.out.println("testReserveMemberId_1 End");
	}
	
	
	@Test
	public void testRetrieveAvailableMemberIds() throws RemoteException, ApplicationException, TelusException{

		try{	
			System.out.println("testRetrieveAvailableMemberIds Start");
	
			String subscriberId = "4032130201";
			int urbanId=905;
			int fleetId=131083;
			int memberId=17;
	//		managerImpl.retrieveAvailableMemberIds(urbanId, fleetId, memberIdPattern, maxMemberIds, sessionId)
			String membId[]=managerImpl.retrieveAvailableMemberIds(urbanId, fleetId, "*", 3,sessionId);

			for(String memb:membId){
				System.out.println("Member id-"+memb);
			}
			
		}catch(ApplicationException ex){		

			System.out.println("Error occured");
		}	
		System.out.println("testRetrieveAvailableMemberIds End");
	
	}
	
	
	@Test
	public void testRetrieveTalkGroupsBySubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			int ban = 194587;		
			String subscriberId = "4037109998";
			
			Collection talkGrpColl= managerImpl.retrieveTalkGroupsBySubscriber(ban, subscriberId, sessionId);		
			System.out.println("TalkGroup==>"+ talkGrpColl);
			
		}catch(ApplicationException ex){		

			ex.printStackTrace();
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115020", errorCode );
		}	
		System.out.println("testRetrieveTalkGroupsBySubscriber End");
	}
	
	
	@Test
	public void testResetVoiceMailPassword() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testResetVoiceMailPassword Start");
			int ban = 6376486;
			String subscriberId = "4168948214";
			IDENSubscriberInfo idenSubscriberInfo= (IDENSubscriberInfo)subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			managerImpl.resetVoiceMailPassword(ban, subscriberId, idenSubscriberInfo.getProductType(), sessionId);
			
		}catch(ApplicationException ex){		

			ex.printStackTrace();
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115020", errorCode );
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testResetVoiceMailPassword End");
	}
	
	
	@Test
	public void testDeleteMsisdnFeature() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testDeleteMsisdnFeature Start");
			int ban = 194587;
			String subscriberId = "4037109998";
			//IDENSubscriberInfo idenSubscriberInfo=null;
			AdditionalMsiSdnFtrInfo additionalMsiSdnFtrInfo= new AdditionalMsiSdnFtrInfo();
			additionalMsiSdnFtrInfo.setSubscriberNumber(subscriberId);
			additionalMsiSdnFtrInfo.setBan(ban);
			//IDENSubscriberInfo idenSubscriberInfo= (IDENSubscriberInfo)subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			managerImpl.deleteMsisdnFeature(additionalMsiSdnFtrInfo, sessionId);
			
		}catch(ApplicationException ex){		

			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("1115020", errorCode );
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testDeleteMsisdnFeature End");
	}
	
	
	@Test
	public void testUpdateBirthDate() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testUpdateBirthDate Start");

			String subscriberId = "5196350743";
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			managerImpl.updateBirthDate(subscriberInfo, sessionId);
			
		}catch(ApplicationException ex){		

			ex.printStackTrace();
			System.out.println("Error occured");
			String errorCode=ex.getErrorCode();
			assertEquals("SYS00015", errorCode );
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testUpdateBirthDate End");
	}
	
	
	
	@Test
	public void testChangePhoneNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangePhoneNumber Start");
			int ban = 197806;		
			String subscriberId = "4037109656";	
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_ALL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			int maxNumbers = 10;
						
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
				= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			managerImpl.changePhoneNumber(subscriberInfo, retrieveAvailablePhoneNumbers[0], "TG", null, null, sessionId);
			
		}catch(ApplicationException ex){	
			String errorMsg="Tuxedo Service Failed";
			System.out.println("Error occured");
			
		}catch(Exception ex){		
		ex.printStackTrace();
		
		}	
		System.out.println("testChangePhoneNumber End");
	}
	
	@Test
	public void testChangePhoneNumber1() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangePhoneNumber1 Start");	
			int ban = 197806;		
			String subscriberId = "4037109656";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_ALL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			int maxNumbers = 10;
						
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
				= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			
			managerImpl.changePhoneNumber(subscriberInfo, retrieveAvailablePhoneNumbers[1], "TG", subscriberInfo.getDealerCode(), 
					subscriberInfo.getSalesRepId(), sessionId);
			
		}catch(ApplicationException ex){	
			String errorMsg="Tuxedo Service Failed";
			System.out.println("Error occured");
			
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testChangePhoneNumber1 End");
	}
	
	
	
	@Test
	public void testMoveSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testMoveSubscriber Start");
			int ban = 197806;		
			String subscriberId = "4037109656";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			int maxNumbers = 10;
						
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
				= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			
			managerImpl.moveSubscriber(subscriberInfo, ban, new Date("01/28/2011"), false, "CR", "move subscriber", 
					subscriberInfo.getDealerCode(), subscriberInfo.getSalesRepId(), sessionId);

			
		}catch(ApplicationException ex){	
			String errorMsg="Credit Class is missing, hence cannot move subscriber.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testMoveSubscriber End");
	}
	
	
	@Test
	public void testMoveSubscriber1() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testMoveSubscriber1 Start");	
			int ban = 2297038;		
			String subscriberId = "6046198750";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			int maxNumbers = 10;
						
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers 
				= managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, maxNumbers, sessionId);
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			
			managerImpl.moveSubscriber(subscriberInfo, ban, new Date("01/28/2011"), false, "CR", "move subscriber",sessionId);
			
		}catch(ApplicationException ex){	
			String errorMsg="Credit Class is missing, hence cannot move subscriber.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(java.lang.Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testMoveSubscriber1 End");
	}


	@Test
	public void testReleaseSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReleaseSubscriber Start");	
			int ban = 70020616;
			String subscriberId = "4037100322";	
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);			
			managerImpl.releaseSubscriber(subscriberInfo,sessionId);
			
		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(Exception ex){		
			ex.printStackTrace();
		
		}	
		System.out.println("testReleaseSubscriber End");
	}
	
	
	@Test
	public void testReserveLikePhoneNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReserveLikePhoneNumber Start");
			
			int ban = 816166;
			String subscriberId = "2047980182";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("2047980182");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);			
			managerImpl.reserveLikePhoneNumber(subscriberInfo, phoneNumberReservation, sessionId);

			
		}catch(ApplicationException ex){	
			String errorMsg="Dealer does not exist or expired";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		

			ex.printStackTrace();
		
		}catch(Exception ex){		

			ex.printStackTrace();
		
		}	
		System.out.println("testReserveLikePhoneNumber End");
	}
	
	
	@Test
	public void testReservePhoneNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReservePhoneNumber Start");	
			int ban = 1905787;
			String subscriberId = "4169930663";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("416*******");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);			
			managerImpl.reservePhoneNumber(subscriberInfo, phoneNumberReservation, false,sessionId);

			
		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			//ex.printStackTrace();	
		}	
		System.out.println("testReservePhoneNumber End");
	}
	
	
	@Test
	public void testRetrieveAvailablePhoneNumbers() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReservePhoneNumber Start");
			int ban = 816166;
			String subscriberId = "2047980182";
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			//phoneNumberReservation.setPhoneNumberPattern("416*******");
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);			
			
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers=managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, 13, sessionId);
			
			for (AvailablePhoneNumberInfo availablePhoneNumber: retrieveAvailablePhoneNumbers) {
				System.out.println(availablePhoneNumber);
			}
			
		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testReservePhoneNumber End");
	}
	
	
	@Test
	public void testCancelAdditionalMsisdn() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testCancelAdditionalMsisdn Start");
			int ban = 816166;
			String subscriberId = "2047980182";
			
			AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo=new AdditionalMsiSdnFtrInfo[1];
			additionalMsiSdnFtrInfo[0]= new AdditionalMsiSdnFtrInfo();
			additionalMsiSdnFtrInfo[0].setSubscriberNumber(subscriberId);
			additionalMsiSdnFtrInfo[0].setBan(ban);		
			managerImpl.cancelAdditionalMsisdn(additionalMsiSdnFtrInfo,subscriberId, sessionId);

		}catch(ApplicationException ex){	
			String errorMsg="Invalid PTN resource status";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testCancelAdditionalMsisdn End");
	}
	
	
	@Test
	public void testCancelPortedInSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testCancelPortedInSubscriber Start");			
			int ban = 816166;
			String subscriberId = "2047980182";
			managerImpl.cancelPortedInSubscriber(ban, subscriberId, "APTR", new Date(),
					"portOutInd", false, sessionId);

		}catch(ApplicationException ex){	
			String errorMsg="Cannot cancel the last Active Subscriber. Please cancel the BAN.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testCancelPortedInSubscriber End");
	}
	
	
	@Test
	public void testChangeAdditionalPhoneNumbers() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeAdditionalPhoneNumbers Start");	
			int ban = 816166;
			String subscriberId = "2047980182";
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			managerImpl.changeAdditionalPhoneNumbers(subscriberInfo, sessionId);			

		}catch(ApplicationException ex){	
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeAdditionalPhoneNumbers End");
	}
	
	
	@Test
	public void testChangeAdditionalPhoneNumbersForPortIn() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeAdditionalPhoneNumbersForPortIn Start");
			int ban = 816166;
			String subscriberId = "2047980182";
	
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			managerImpl.changeAdditionalPhoneNumbersForPortIn(subscriberInfo, sessionId);			

		}catch(ApplicationException ex){	
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeAdditionalPhoneNumbersForPortIn End");
	}
	
	
	
	@Test
	public void testChangeFaxNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeFaxNumber Start");
       		int ban = 660416;		
			String subscriberId = "4164552264";
	
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			managerImpl.changeFaxNumber(subscriberInfo, sessionId);			

		}catch(ApplicationException ex){	
			String errorMsg="phoneNumberReservation.getNumberGroup() should not be null";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeFaxNumber End");
	}
	
	
	@Test
	public void testChangeFaxNumber1() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeFaxNumber1 Start");
			int ban = 805938;		
			String subscriberId = "7807191196";
        	PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		

			
//			LineRangeInfo[] lineRanges=new LineRangeInfo[1];
//			lineRanges[0]=new LineRangeInfo();
//			lineRanges[0].setStart(0000);
//			lineRanges[0].setEnd(9999);
//			
//			NumberRangeInfo[] numberRanges=new NumberRangeInfo[2];
//			numberRanges[0]=new NumberRangeInfo();
//			numberRanges[0].setNXX(717);
//			numberRanges[0].setNPA(306);
//			numberRanges[0].setLineRanges(lineRanges);
//			numberRanges[1]=new NumberRangeInfo();
//			numberRanges[1].setNXX(974);
//			numberRanges[1].setNPA(306);
//			numberRanges[1].setLineRanges(lineRanges);
//		
//			numberGroupInfo=new NumberGroupInfo();
//			numberGroupInfo.setCode("SAS");
//			numberGroupInfo.setProvinceCode("ON");
//			numberGroupInfo.setNumberLocation("TLS");
//			numberGroupInfo.setNumberRanges(numberRanges);
//			
			phoneNumberReservation.setNumberGroup(numberGroupInfo);			
//			
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers=managerImpl.retrieveAvailablePhoneNumbers(ban, 
					subscriberId, phoneNumberReservation, 13, sessionId);
//			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			
			managerImpl.changeFaxNumber(subscriberInfo, retrieveAvailablePhoneNumbers[3],sessionId);		

		}catch(ApplicationException ex){	
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeFaxNumber1 End");
	}
	
	
	@Test
	public void testChangeIMSI() throws RemoteException, ApplicationException, TelusException{

		try{			
			int ban = 70567346;		
			String subscriberId = "7781752310";

			managerImpl.changeIMSI(ban, subscriberId, sessionId);

		}catch(ApplicationException ex){	
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeIMSI End");
	}
	
	
	@Test
	public void testChangeIP() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testChangeIP Start");		
			int ban = 17605;		
			String subscriberId = "4034850238";

			managerImpl.changeIP(ban, subscriberId, "10.3.45.23", "abc", "def", sessionId);

		}catch(ApplicationException ex){	
			ex.printStackTrace();
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testChangeIP End");
	}
	
	
	
	@Test
	public void testMigrateSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testMigrateSubscriber Start");
			
			String srcSubscriberId = "4161643227";
			String newSubscriberId = "4033365049";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
			Date date = formatter.parse("2012/04/04");
			
			MigrationTypeInfo migrationTypeInfo=new MigrationTypeInfo();
			migrationTypeInfo.setCode(MigrationType.PCS_PRE_TO_PCSPOST);
			migrationTypeInfo.setDescription(MigrationType.PCS_PRE_TO_PCSPOST);

			SubscriberInfo srcSubscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(srcSubscriberId);
			EquipmentInfo oldequipInfo = helperImpl.getEquipmentInfobySerialNumber("8912246542810001001");
			srcSubscriberInfo.setEquipment(oldequipInfo);
			
			PricePlanInfo priceplaninfo = referenceDataFacade.getPricePlan("ADWTK20C");
			
			SubscriberInfo newSubscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(newSubscriberId);	

			MigrationRequestInfo migrationRequestInfo=new MigrationRequestInfo(migrationTypeInfo);
			migrationRequestInfo.setDealerCode(srcSubscriberInfo.getDealerCode());
			migrationRequestInfo.setSalesRepCode(srcSubscriberInfo.getSalesRepId());
			migrationRequestInfo.setMigrationReasonCode("PRPO");
			SubscriberContractInfo subscriberContractInfo =  new SubscriberContractInfo();
			subscriberContractInfo.setPricePlanInfo(priceplaninfo);
			SubscriberContractInfo sci= subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(srcSubscriberInfo.getSubscriberId());
			sci.setPricePlanInfo(priceplaninfo);
			
			EquipmentInfo newPriEquipmentInfo=helperImpl.getEquipmentInfobySerialNumber("8912246542810001019");
		
			EquipmentInfo[] newSecEquipmentInfoArr=new EquipmentInfo[0];

			managerImpl.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, date, sci, newPriEquipmentInfo, 
				newSecEquipmentInfoArr, migrationRequestInfo,sessionId);

		}catch(ApplicationException ex){	
			ex.printStackTrace();
			String errorMsg="Activity Reason PRPO is either invalid or does not exist.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testMigrateSubscriber End");
	}
	
	
	
	@Test
	public void testPortChangeSubscriberNumber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testPortChangeSubscriberNumber Start");
			
			int old_ban = 816166;
			String old_subscriberId = "2047980182";
			
			int ban = 660416;
			String subscriberId = "4164552264";	
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("4*********");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);			
			
		//	AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers=managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, 13, sessionId);
			
			AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers=managerImpl.retrieveAvailablePhoneNumbers(ban, subscriberId, phoneNumberReservation, 13, sessionId);
//			
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	

			managerImpl.portChangeSubscriberNumber(subscriberInfo, retrieveAvailablePhoneNumbers[1], "CR", subscriberInfo.getDealerCode(), 
					subscriberInfo.getSalesRepId(),	"abc", old_ban, old_subscriberId, sessionId);


		}catch(ApplicationException ex){	
			String errorMsg="Unable to locate a Subscriber number";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testPortChangeSubscriberNumber End");
	}
	
	
	@Test
	public void testReleasePortedInSubscriber() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReleasePortedInSubscriber Start");
			int ban = 194587;
			String subscriberId = "4037109998";
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);	
			managerImpl.releasePortedInSubscriber(subscriberInfo,sessionId);


		}catch(ApplicationException ex){	
			String errorMsg="Can not Activate/Save Subscriber that is not reserved.";
			System.out.println("Error occured");
			assertEquals(errorMsg, ex.getErrorMessage());
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
			ex.printStackTrace();	
		}	
		System.out.println("testReleasePortedInSubscriber End");
	}
	

	@Test
	public void testReserveAdditionalPhoneNumber()throws RemoteException, ApplicationException {


		int ban = 20070051;
		String subscriberId = "5148200007";

		AvailablePhoneNumberInfo availableInfo = new AvailablePhoneNumberInfo();
		NumberGroupInfo numberGroupInfo = new NumberGroupInfo();
		numberGroupInfo.setCode("TOR");
		numberGroupInfo.setDescription("TORONTO");
		numberGroupInfo.setNumberLocation("TLS");
		numberGroupInfo.setProvinceCode("ON");
		availableInfo.setPhoneNumber("4168940042");
		availableInfo.setNumberGroup(numberGroupInfo);
		
		try {
			managerImpl.reserveAdditionalPhoneNumber(ban, subscriberId, availableInfo, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115020",aex.getErrorCode());
			assertEquals("Unable to locate a Subscriber number",aex.getErrorMessage());
		}
	}

	@Test
	public void testReservePortedInPhoneNumber()throws RemoteException, ApplicationException, TelusException {
		try{
			
			String subscriberId = "4032130201";
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_IDEN);
			phoneNumberReservation.setPhoneNumberPattern("");
			
//			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType("4168940042", Equipment.EQUIPMENT_TYPE_DIGITAL);
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);	
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			//String subscriberId = "5148200007";
//			String subscriberId = "4168940042";

			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			//subscriberInfo.setProductType("C");
			managerImpl.reservePortedInPhoneNumber(subscriberInfo, phoneNumberReservation, true, sessionId);
			
		}catch(ApplicationException aex){
			assertEquals("Product type not supported. I",aex.getErrorMessage());
		}
	}
	
	@Test
	public void testReservePortedInPhoneNumber1()throws RemoteException, ApplicationException, TelusException {
		try{
			String subscriberId = "4032130201";
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_IDEN);
			phoneNumberReservation.setPhoneNumberPattern("");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_DIGITAL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			SubscriberInfo subscriberInfo= subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);
			managerImpl.reservePortedInPhoneNumber(subscriberInfo, phoneNumberReservation, true, sessionId);
			fail("Exception Expected.");
		}catch(ApplicationException aex){
			//aex.printStackTrace();
			assertEquals("Product type not supported. I",aex.getErrorMessage());
		}
	}


	@Test
	public void searchSubscriberByAdditionalMsiSdn()throws RemoteException, ApplicationException {
		
		try{
			managerImpl.searchSubscriberByAdditionalMsiSdn("", sessionId);
		}catch(ApplicationException aex){
			assertEquals("SYS00015",aex.getErrorCode());
			//assertEquals("Tuxedo Service Failed",aex.getErrorMessage());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Test
	public void testSendTestPage() throws RemoteException,ApplicationException{
		try{
			int ban = 20070051;
			String subscriberId = "4168940042";
			managerImpl.sendTestPage(ban, subscriberId, sessionId);
		}catch(ApplicationException ex){
			assertEquals("1115020",ex.getErrorCode());
			assertEquals("Unable to locate a Subscriber number",ex.getErrorMessage());
		}
	}
	
	@Test
	public void testSetPortIndicator() throws RemoteException, ApplicationException{
		
		try{
			
		int ban = 20001552;		
		String subscriberId = "4168940568";
		managerImpl.setSubscriberPortIndicator(subscriberId, sessionId);
		
		}catch(ApplicationException aex){
			System.out.println("Application Exception Sysex"+aex.getErrorCode()+aex.getErrorMessage());
		}catch(SystemException se){
			System.out.println("System Exception Sysex"+se.getErrorCode()+se.getErrorMessage());
		}
	}

	@Test
	public void testSetPortIndicator1() throws RemoteException, ApplicationException{
		try{
			
		int ban = 20001552;		
		String subscriberId = "4168940568";
		managerImpl.setSubscriberPortIndicator(subscriberId,new Date(), sessionId);
		}catch(ApplicationException aex){
			System.out.println("Application Exception Sysex"+aex.getErrorCode()+aex.getErrorMessage());
		}catch(SystemException se){
			System.out.println("System Exception Sysex"+se.getErrorCode()+se.getErrorMessage());
		}
		
	}
	@Test
	public void testSnapBack() throws RemoteException{
		try{
			managerImpl.snapBack("", sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
		
		try{
			managerImpl.snapBack("4168940042", sessionId);
		}catch(ApplicationException aex){
			assertEquals("1125040",aex.getErrorCode());
			assertEquals("No entry found in CTN or PTN pools.",aex.getErrorMessage());
		}
	}
	
	@Test
	public void testSuspendPortedInSubscriber()throws RemoteException{
		int ban = 197806;
		String phoneNumber = "4037109656";
		String deactivationReason ="test";
		Date activityDate = new Date();
		String portOutInd="Y";
		try{
			managerImpl.suspendPortedInSubscriber(ban, phoneNumber, deactivationReason, activityDate, portOutInd, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1111240",aex.getErrorCode());
		}
	}

	@Test
	public void testGetUsageProfileListsSummary() throws RemoteException {
		int ban = 197806;
		String subscriberId = "4037109656";
		int billSeqNo=11212;
		String productType="C";
		try{
			managerImpl.getUsageProfileListsSummary(ban, subscriberId, billSeqNo, productType, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1110560",aex.getErrorCode());
			assertEquals("Invalid billSeqNo.",aex.getErrorMessage());
		}
		try{
			subscriberId = "";
			managerImpl.getUsageProfileListsSummary(ban, subscriberId, billSeqNo, productType, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
	}
	@Test
	public void testRetrieveBilledCallsList()throws RemoteException, ApplicationException{
		int ban = 197806;
		String subscriberId = "4037109656";
		String productType ="C";
		int billSeqNo = 121212;
		char callType = 'I';
		Date fromDate = new Date();
		Date toDate = new Date();
		boolean getAll = true;
		try{
		managerImpl.retrieveBilledCallsList(ban, subscriberId, productType, billSeqNo, callType, fromDate, toDate, getAll, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1110560",aex.getErrorCode());
			assertEquals("Invalid billSeqNo.",aex.getErrorMessage());
		}
		try{
			subscriberId = "";
		managerImpl.retrieveBilledCallsList(ban, subscriberId, productType, billSeqNo, callType, fromDate, toDate, getAll, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
	}
	@Test
	public void testRetrieveCallDetails() throws RemoteException, ApplicationException{
		int ban = 197806;
		String subscriberId = "4037109656";
		String productType ="C";
		int billSeqNo = 100;
		String messageSwitchId = "test";
		String callProductType = "test";
		Date channelSeizureDate = new Date();
		try{
			managerImpl.retrieveCallDetails(ban, subscriberId, productType, billSeqNo, channelSeizureDate, messageSwitchId, callProductType, sessionId);
		}catch(ApplicationException aex){
			
			assertEquals("1110560",aex.getErrorCode());
			assertEquals("Invalid billSeqNo.",aex.getErrorMessage());
		}
	}
	@Test
	public void testRetrieveUnbilledCallsList() throws RemoteException, ApplicationException{
		int ban = 197806;
		String subscriberId = "4037109656";
		String productType ="C";
		CallListInfo callListInfo =
			managerImpl.retrieveUnbilledCallsList(ban, subscriberId, productType, sessionId);
		assertNotNull(callListInfo);
		try{
			subscriberId = "";
			managerImpl.retrieveUnbilledCallsList(ban, subscriberId, productType, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
	}
	@Test
	public void testRetrieveUnbilledCallsList1() throws RemoteException, ApplicationException{
		int ban = 197806;
		String subscriberId = "4037109656";
		String productType ="C";
		Date fromDate = new Date();
		Date toDate = new Date();
		boolean getAll = true;
		CallListInfo callListInfo =
			managerImpl.retrieveUnbilledCallsList(ban, subscriberId, productType, fromDate, toDate, getAll, sessionId);
			assertNotNull(callListInfo);

		try{
			subscriberId = "";
			managerImpl.retrieveUnbilledCallsList(ban, subscriberId, productType, fromDate, toDate, getAll, sessionId);
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
	}
	@Test
	public void testRetrieveCancellationPenalty() throws RemoteException, ApplicationException{
		int ban = 197806;
		String subscriberId = "4037109656";
		String productType ="C";
		CancellationPenaltyInfo cancellationPenality = 
			managerImpl.retrieveCancellationPenalty(ban, subscriberId, productType, sessionId);
			assertNotNull(cancellationPenality);

		try{
			subscriberId = "";
			managerImpl.retrieveCancellationPenalty(ban, subscriberId, productType, sessionId);
			
		}catch(ApplicationException aex){
			assertEquals("1115060",aex.getErrorCode());
			assertEquals("Incorrect Subscriber number",aex.getErrorMessage());
		}
	}

	
	@Test
	public void testSuspendSubscriber() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testSuspendSubscriber Start");

			int ban=197806;
			String subscriberId = "4037109656";
			
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper
					.retrieveSubscriberByPhoneNumber(subscriberId);
			subscriberInfo.setProductType("I");
			managerImpl.suspendSubscriber(subscriberInfo, new Date(), "CR",
					"suspend subscriber", sessionId);

			System.out.println("testSuspendSubscriber End");
		} catch (ApplicationException ex) {

			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			assertEquals("1115020", errorCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("testSuspendSubscriber End");
	}
	
	
	@Test
	public void testUpdatePortRestriction() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testSuspendSubscriber Start");

			int ban=17605;
			String subscriberId = "4034850238";
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);		
			subscriberInfo.setProductType("I");
			managerImpl.updatePortRestriction(ban, subscriberId, false, "123");

		} catch (ApplicationException ex) {

			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			assertEquals("1115020", errorCode);
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		System.out.println("testSuspendSubscriber End");
	}

	
	
	@Test
	public void testUpdateSubscriber() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testUpdateSubscriber Start");

			int ban=1905137;
			String subscriberId = "5196350743";

			SubscriberInfo subscriberInfo = subscriberLifecycleHelper
					.retrieveSubscriberByPhoneNumber(subscriberId);
			subscriberInfo.setProductType("I");
			managerImpl.updateSubscriber(subscriberInfo,sessionId);

		} catch (ApplicationException ex) {

			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			assertEquals("1115020", errorCode);
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		System.out.println("testUpdateSubscriber End");
	}
	
	
	@Test
	public void testUpdateSubscriptionRole() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testUpdateSubscriptionRole Start");

			int ban=16910;
			String subscriberId = "4032130201";
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);		
			subscriberInfo.setProductType("I");

			managerImpl.updateSubscriptionRole(ban, subscriberId, "A", subscriberInfo.getDealerCode(), subscriberInfo.getSalesRepId(), "123");			

		} catch (ApplicationException ex) {

			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			//assertEquals("1115020", errorCode);
		} catch (Exception ex) {

			ex.printStackTrace();

		}
		System.out.println("testUpdateSubscriptionRole End");
	}

	
	@Test
	public void testAdjustCall() throws RemoteException, TelusException,
			ApplicationException {

		try {
			System.out.println("testAdjustCall Start");

			int ban=17605;
			String subscriberId = "4034850238";
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);		
			subscriberInfo.setProductType("I");

			managerImpl.adjustCall(ban, subscriberId, subscriberInfo.getProductType(), 
					123, new Date(), "I", 12.4, "TG", "ABC", "C",sessionId);
			
		} catch (ApplicationException ex) {
			System.out.println("Error occured");
			String errorCode = ex.getErrorCode();
			assertEquals("1115020", errorCode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("testAdjustCall End");
	}

	@Test
	public void testChangeEquipment() throws ApplicationException, RemoteException{
		try{
			String phoneNumber = "2502660468";
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phoneNumber);
			EquipmentInfo oldPrimaryEquipmentInfo = new EquipmentInfo();
			oldPrimaryEquipmentInfo.setSerialNumber("06800067821");
			EquipmentInfo newPrimaryEquipmentInfo = new EquipmentInfo();
		newPrimaryEquipmentInfo.setSerialNumber("06800072487");
			EquipmentInfo[] newSecondaryEquipmentInfo = new EquipmentInfo[0];
			String dealerCode ="A001000001";
			String salesRepCode ="0000";
			String requestorId = "tester";
			String swapType = "REPLACE";
			SubscriberContractInfo subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber("2502660468");
	
			PricePlanValidationInfo pricePlanValidation=new PricePlanValidationInfo();
			pricePlanValidation.setCurrentValidation(false);
			PricePlanInfo pricePlan=new PricePlanInfo();
			pricePlan.setActive(true);
			subscriberContractInfo.setPricePlanInfo(pricePlan);
			managerImpl.changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, dealerCode, salesRepCode, requestorId, swapType, subscriberContractInfo, pricePlanValidation, sessionId);
		}catch(ApplicationException aex){
			aex.printStackTrace();
			assertEquals("1116280",aex.getErrorCode());
		
		}
	}
	
	@Test
	public void testUpdateEmailAddres() throws ApplicationException, RemoteException{
		managerImpl.updateEmailAddress(194587, "4037109998", "testpt148@telus.com", sessionId);
	}

//test ReservePhoneNumber for offline activation
	
	@Test
	public void testReservePhoneNumberforOfflineActivation() throws RemoteException, ApplicationException, TelusException{

		try{			
			System.out.println("testReservePhoneNumberforOfflineActivation Start");	
		
			String subscriberId = "4038579391";
			boolean isOfflineActivation = true;
			
			PhoneNumberReservationInfo phoneNumberReservation = new PhoneNumberReservationInfo();
			phoneNumberReservation.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			phoneNumberReservation.setPhoneNumberPattern("4038579391");
			
			NumberGroupInfo numberGroupInfo = referenceDataFacade.getNumberGroupByPhoneNumberAndProductType(subscriberId, Equipment.EQUIPMENT_TYPE_ALL);		
			phoneNumberReservation.setNumberGroup(numberGroupInfo);		
			
			SubscriberInfo subscriberInfo= new SubscriberInfo();
			subscriberInfo.setDealerCode("A001000001");
			subscriberInfo.setSalesRepId("0000");
			subscriberInfo.setBanId(70555748);
			subscriberInfo.setProductType(Subscriber.PRODUCT_TYPE_PCS);
			
			
					//subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(subscriberId);			
			managerImpl.reservePhoneNumber(subscriberInfo, phoneNumberReservation, isOfflineActivation,sessionId);

			
		}catch(ApplicationException ex){	
			System.out.println("Errorcode"+ex.getErrorCode());
			System.out.println("Error Message"+ex.getErrorMessage());
			ex.printStackTrace();
			
		}catch(SystemException ex){		
			ex.printStackTrace();		
		}catch(Exception ex){		
		ex.printStackTrace();	
		}	
		System.out.println("testReservePhoneNumberforOfflineActivation End");
	}


	@Test
	public void testApplyDiscountToAccount() throws Throwable {
		int ban = 70642137;
		String subscriberId = "7781757950";
		
		ban = 70701868;
		subscriberId = "6471114535";
		
//		SubscriberInfo subscriberInfo = null;
//		subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(ban, subscriberId);
		DiscountPlanInfo promotionalDiscount = new DiscountPlanInfo();
		promotionalDiscount.setCode("BADISC");
		promotionalDiscount.setOfferExpirationDate(null);
		promotionalDiscount.setLevel(DiscountPlan.DISCOUNT_PLAN_LEVEL_SUBSCRIBER);
		
		DiscountInfo discountInfo = new DiscountInfo();
		discountInfo.setBan(ban);
		discountInfo.setProductType("C");
		discountInfo.setDiscountCode(promotionalDiscount.getCode());
		discountInfo.setExpiryDate(promotionalDiscount.getOfferExpirationDate());
//		discountInfo.setEffectiveDate(subscriberInfo.getStartServiceDate());
		discountInfo.setEffectiveDate(new Date(2013-1900, 5, 5));

		if(promotionalDiscount.getOfferExpirationDate() != null){
//			long timeDifference = (subscriberInfo.getStartServiceDate().getTime() 
//						- referenceDataHelper.retrieveSystemDate().getTime());
//				Calendar cal = Calendar.getInstance();
//
//				cal.setTimeInMillis(promotionalDiscount.getOfferExpirationDate().getTime() + timeDifference);
//				discountInfo.setExpiryDate(cal.getTime());    

		}                        
		if (promotionalDiscount.getLevel().equals(DiscountPlan.DISCOUNT_PLAN_LEVEL_SUBSCRIBER)) {
			discountInfo.setSubscriberId(subscriberId);
		}

		if(promotionalDiscount.getMonths() > 0){
			discountInfo.setExpiryDate(null);  
		}

		String almSessionId = alm.openSession("18654", "apollo", "SMARTD");
		alm.applyDiscountToAccount(discountInfo, almSessionId);
	}
	
}
