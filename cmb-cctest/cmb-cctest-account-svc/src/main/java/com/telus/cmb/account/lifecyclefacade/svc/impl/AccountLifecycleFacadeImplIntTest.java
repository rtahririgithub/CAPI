package com.telus.cmb.account.lifecyclefacade.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.account.info.WesternPrepaidConsumerAccountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;




public class AccountLifecycleFacadeImplIntTest {
	AccountLifecycleFacade facadeImpl = null;
	AccountInformationHelper helperImpl = null;
	AccountLifecycleManager managerImpl = null;
//	String url="t3://cmosr-custinfomgmt2-pt148.tmi.telus.com:30022";
//String url="t3://ln98550:12022";
//	String url = "t3://ln98557:31022";
	//String url = "t3://ln99231:30022";
	String url = "t3://localhost:7001";
	String sessionId;

	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getAccountLifecycleManagerRemote(context);		
		sessionId = facadeImpl.openSession("18654", "apollo", "SMARTDESKTOP");
		context.close();
		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}

	private void getAccountLifecycleManagerRemote(Context context) throws Exception{
		facadeImpl = (AccountLifecycleFacade) context
				.lookup("AccountLifecycleFacade#com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade");

		helperImpl = (AccountInformationHelper) context
				.lookup("AccountInformationHelper#com.telus.cmb.account.informationhelper.svc.AccountInformationHelper");

		managerImpl = (AccountLifecycleManager) context
				.lookup("AccountLifecycleManager#com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager");
		
	}
	
	@Test
	public void testCreateAccount() throws ApplicationException, RemoteException {
		System.out.println("testCreateAccount START");
		System.out.println("in local");

		AccountInfo accInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		//			AccountInfo accInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();

		accInfo.setAccountSubType('R');
		accInfo.setAccountType('I');
		accInfo.setBrandId(0);
		accInfo.setBanSegment("TCSO");
		accInfo.setBanSubSegment("OTHR");

		AddressInfo  address =  new AddressInfo();
		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");
		accInfo.setAddress0(address);

		accInfo.setEmail("dany.taylor@telusmobility.com");
		accInfo.setPin("5555");
		accInfo.setLanguage("EN");
		accInfo.setDealerCode("0000000008");
		accInfo.setSalesRepCode("0000");

		((PostpaidConsumerAccountInfo)accInfo).getName0().setTitle("MR.");
		((PostpaidConsumerAccountInfo)accInfo).getName0().setFirstName("Dany");
		((PostpaidConsumerAccountInfo)accInfo).getName0().setLastName("Taylor");
		accInfo.setHomePhone("4165551234");
		accInfo.setBusinessPhone("4165550000");

		accInfo.setNoOfInvoice(2);
		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");

		String pymMethod = "R";
		((PostpaidConsumerAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod(pymMethod);
		//    		((PostpaidBusinessRegularAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod(pymMethod);

		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
		System.out.println("sessionId = " + sessionId);
		int ban = managerImpl.createAccount(accInfo, sessionId);	
		System.out.println("New BAN = " + ban);
		System.out.println("testCreateAccount END"); 

	}
	
	@Test
	public void testUpdateAccount() throws ApplicationException, RemoteException {
		System.out.println("testUpdateAccount START");

		int ban=6016913;
		AccountInfo accInfo = helperImpl.retrieveAccountByBan(ban);
		System.out.println(accInfo.toString());

		accInfo.setEmail("newid@telusmobility.com");
		//		accInfo.setHomePhone("4165551234");
		//		accInfo.setBusinessPhone("4165550000");

		System.out.println("BAN ID to update: "+accInfo.getBanId());
		managerImpl.updateAccount(accInfo, sessionId);	
		assertEquals("newid@telusmobility.com", accInfo.getEmail()); 
		System.out.println("testUpdateAccount END"); 

	}
	
	@Test
	public void testUpdateBillCycle() throws ApplicationException, RemoteException {
		System.out.println("testUpdateBillCycle START"); 

		try {
			managerImpl.updateBillCycle(194587, (short) 5, sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1117040", ae.getErrorCode());
		}
		System.out.println("testUpdateBillCycle END"); 
	}
	
	@Test
	public void testUpdateBillingInformation() throws ApplicationException, RemoteException {		
		
		int billingAccountNumber=6004473;
		BillingPropertyInfo billingPropertyInfo=new BillingPropertyInfo();
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("R");
		name.setLastName("C");
		name.setMiddleInitial("A");
		name.setNameFormat("P");
		billingPropertyInfo.setName(name );
		AddressInfo address = new AddressInfo();
		address.setAddressType("C");
		address.setPrimaryLine("300 Consilium Place");
		address.setCity("Scarborough");
		address.setProvince("ON");
		address.setPostalCode("A2B3C7");
		address.setCountry("CA");
		address.setCivicNo("100");
		address.setStreetNumber("100");
		address.setStreetName("Consilium");
		address.setStreetType("Pl");
		billingPropertyInfo.setAddress(address );
			managerImpl.updateBillingInformation(billingAccountNumber, billingPropertyInfo, sessionId);
			
			assertEquals(name.getFirstName()+" "+billingPropertyInfo.getLegalBusinessName(), helperImpl.retrieveBillingInformation(billingAccountNumber).getFullName());		
			
	}
	
	@Test
	public void testUpdatePaymentMethod() throws ApplicationException, RemoteException {

		int ban = 16910;
		PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("455630");  
		cc.setToken("100000000000001366517");
		cc.setTrailingDisplayDigits("3821");
		cc.setType("VS");
		cc.setAuthorizationCode("123");			

		paymentMethodInfo.setCreditCard0(cc);
		paymentMethodInfo.setEndDate(java.sql.Date.valueOf("2015-01-01"));
		paymentMethodInfo.setPaymentMethod("C");
		paymentMethodInfo.setStartDate(new Date());
		paymentMethodInfo.setStatus("A");
		paymentMethodInfo.setStatusReason("Usereq");
		paymentMethodInfo.setSuppressReturnEnvelope(false);
		managerImpl.updatePaymentMethod(ban, paymentMethodInfo, sessionId);
		System.out.println("testUpdatePaymentMethod END"); 
	}

	@Test	
	public void testCancelAccount() throws ApplicationException, RemoteException {
		try{
			System.out.println("testCancelAccount START"); 

			String activityReasonCode="RETC";
			String depositReturnMethod="";//R

			managerImpl.cancelAccount(6012776, new Date(), activityReasonCode, depositReturnMethod, "", "userMemoText", false,sessionId);

			System.out.println("testCancelAccount END"); 
		} catch (ApplicationException ae){
			assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
			assertEquals("Deposit Return Method should not be null.", ae.getErrorMessage());

		}
	}
	
	@Test	
	public void testSuspendAccount() throws ApplicationException, RemoteException {
		System.out.println("testSuspendAccount START"); 
		int ban = 6022;
		String userMemoText="memo input test";
		String activityReasonCode="PRTO";
		Date activityDate=new Date();
		try {
			managerImpl.suspendAccount(ban, activityDate, activityReasonCode, userMemoText, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111720", ae.getErrorCode());
		}

		System.out.println("testSuspendAccount END"); 
	}
	
	@Test
	public void testsuspendAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6000055;
		String activityReasonCode="";
		Date activityDate=new Date();
		String portOutInd="";
		try{
			managerImpl.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115300", ae.getErrorCode());
		}
	}
	
	@Test
	public void testCancelSubscribers() throws RemoteException, ApplicationException {
		try {
			managerImpl.cancelSubscribers(70616988, new Date(), "321", 'Y', new String[1], new String[1], "1", sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115020", ae.getErrorCode());
		}
	}
	
	@Test
	public void testSuspendSubscribers() throws RemoteException, ApplicationException {
		try {
			String[] subscribers=new String[1];
			subscribers[0]="4037109656";
			managerImpl.suspendSubscribers(20002207, new Date(), "VAD", subscribers, "txt", sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
		assertEquals("1111720", ae.getErrorCode()); 
			//assertEquals("-1", ae.getErrorCode()); // Tuxedo Service Failed

		}
	}
	
	@Test
	public void testCreatePostpaidAccount() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START testCreatePostpaidAccount");
		
		AccountInfo accountInfo=getPostpaidAccountInfo();
		
		String compassCustomerId="1";
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy= getBusinessCreditIdentityInfo();
		BusinessCreditIdentityInfo[] businessCreditIdentiyList =new BusinessCreditIdentityInfo[1];
		businessCreditIdentiyList[0]=getBusinessCreditIdentityInfo();
		System.out.println(getAccountInfo());
		PostpaidAccountCreationResponseInfo creationResponseInfo=  facadeImpl.createPostpaidAccount(accountInfo, compassCustomerId, businessCreditIdentiyList, selectedBusinessCreditIdentiy, 
				getAuditHeader(), getAuditInfo(), sessionId);
		
		System.out.println("OUTPUT : ");
		System.out.println(creationResponseInfo.getBan());
		System.out.println(creationResponseInfo.getCreditCheckResult0().toString());
		
		System.out.println("END testCreatePostpaidAccount");
	}
	@Test
	public void testCreateCustomerAndAccount() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START createCustomerAndAccount");
		AccountInfo accountInfo=getAccountInfo();
	    facadeImpl.createCustomerAndAccount(accountInfo, "16910", sessionId);
	   System.out.println("END createCustomerAndAccount");
	}

	@Test
	public void testCheckCredit() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START CheckCredit");
		AccountInfo accountInfo=helperImpl.retrieveAccountByBan(70696337);
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy= getBusinessCreditIdentityInfo();
		BusinessCreditIdentityInfo[] businessCreditIdentiyList =new BusinessCreditIdentityInfo[1];
		businessCreditIdentiyList[0]=getBusinessCreditIdentityInfo();
		CreditCheckResultInfo creditCheckResultInfo =	facadeImpl.checkCredit(accountInfo, businessCreditIdentiyList, selectedBusinessCreditIdentiy, getAuditInfo(), getAuditHeader(), sessionId);
		String  CreditClass = "C";
		System.out.println("creditCheckResultInfo"+creditCheckResultInfo);
		//assertEquals(CreditClass, creditCheckResultInfo. getCreditClass());
		System.out.println("END CheckCredit");
	}

	@Test
	public void testCheckCreditForNonBusiness() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START CheckCredit");
		AccountInfo accountInfo=helperImpl.retrieveAccountByBan(70665720);
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy= getBusinessCreditIdentityInfo();
		BusinessCreditIdentityInfo[] businessCreditIdentiyList =new BusinessCreditIdentityInfo[1];
		businessCreditIdentiyList[0]=getBusinessCreditIdentityInfo();
		CreditCheckResultInfo creditCheckResultInfo=	facadeImpl.checkCredit(accountInfo, businessCreditIdentiyList, selectedBusinessCreditIdentiy, getAuditInfo(), getAuditHeader(), sessionId);
		String  CreditClass = "B";
		assertEquals(CreditClass, creditCheckResultInfo. getCreditClass());
		System.out.println("END CheckCredit");
	}
	@Test
	public void testCheckCreditForBusiness() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START CheckCreditForBusiness");
		AccountInfo accountInfo=helperImpl.retrieveAccountByBan(70665647);
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy= getBusinessCreditIdentityInfo();
		BusinessCreditIdentityInfo[] businessCreditIdentiyList =new BusinessCreditIdentityInfo[1];
		businessCreditIdentiyList[0]=getBusinessCreditIdentityInfo();
		CreditCheckResultInfo creditCheckResultInfo=	facadeImpl.checkCredit(accountInfo, businessCreditIdentiyList, selectedBusinessCreditIdentiy, getAuditInfo(), getAuditHeader(), sessionId);
		String  CreditClass = "C";
		assertEquals(CreditClass, creditCheckResultInfo. getCreditClass());
		System.out.println("END CheckCreditForBusiness");
	}
	@Test
	public void testCheckCreditForNeitherBusinessNorNonBusiness() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START CheckCredit");
		AccountInfo accountInfo=helperImpl.retrieveAccountByBan(70665777);
		BusinessCreditIdentityInfo selectedBusinessCreditIdentiy= getBusinessCreditIdentityInfo();
		BusinessCreditIdentityInfo[] businessCreditIdentiyList =new BusinessCreditIdentityInfo[1];
		businessCreditIdentiyList[0]=getBusinessCreditIdentityInfo();
		try{
		CreditCheckResultInfo creditCheckResultInfo=	facadeImpl.checkCredit(accountInfo, businessCreditIdentiyList, selectedBusinessCreditIdentiy, getAuditInfo(), getAuditHeader(), sessionId);
		String  CreditClass = "C";
		assertEquals(CreditClass, creditCheckResultInfo. getCreditClass());
		System.out.println("END CheckCredit");
	}
		catch(Exception e) {
			e.printStackTrace();
	}
		
}
	@Test
	public void testCreatePrepaidAccount() throws ApplicationException, RemoteException, TelusAPIException {
		System.out.println("START testCreatePrepaidAccount");
		AccountInfo accountInfo=getAccountInfo();
		String compassCustomerId="1";
		String businessRole="ALL";
		accountInfo=getAccounInfoFields(accountInfo);
		accountInfo.setPin("1111");
		
		int banId =facadeImpl.createPrepaidAccount(accountInfo, compassCustomerId, businessRole, getAuditHeader(), getAuditInfo(), sessionId);
		
		System.out.println("BAN ID : "+banId);
		System.out.println("END testCreatePrepaidAccount");
	}
	private AccountInfo getAccounInfoFields(AccountInfo accountInfo){
//		((PrepaidConsumerAccountInfo)accountInfo).setCustomerId(1234);
//		((PrepaidConsumerAccountInfo)accountInfo).setSerialNumber("8912230000000071266");// 300000000100223 HSPA, 8912230000000071266 USIM, 000100005794080 IEMI, 21101112944 PCS
//		((PrepaidConsumerAccountInfo)accountInfo).setActivationType(0);
//		((PrepaidConsumerAccountInfo)accountInfo).setActivationCode("030001481234"); //card table in Dist DB
//		((PrepaidConsumerAccountInfo)accountInfo).setActivationCreditAmount(200);
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PrepaidConsumerAccountInfo)accountInfo).setBirthDate(bd);
//		((PrepaidConsumerAccountInfo) accountInfo).getTopUpCreditCard0().copyFrom(getCreditCardInfo());
		
		((WesternPrepaidConsumerAccountInfo)accountInfo).setCustomerId(1234);
		((WesternPrepaidConsumerAccountInfo)accountInfo).setSerialNumber("8912230000093531556");// 300000000100223 HSPA, 8912230000000071266 USIM, 000100005794080 IEMI, 21101112944 PCS
		((WesternPrepaidConsumerAccountInfo)accountInfo).setActivationType(1);
		((WesternPrepaidConsumerAccountInfo)accountInfo).setActivationCode("030001481234"); //card table in Dist DB
		((WesternPrepaidConsumerAccountInfo)accountInfo).setActivationCreditAmount(200);
		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
		((WesternPrepaidConsumerAccountInfo)accountInfo).setBirthDate(bd);
		((WesternPrepaidConsumerAccountInfo) accountInfo).getTopUpCreditCard0().copyFrom(getCreditCardInfo());
		
		return accountInfo;
	}
	
	private AuditInfo getAuditInfo() throws ApplicationException{
		AuditInfo auditInfo=new AuditInfo();
		auditInfo.setChannelOrgId("ChannelId");
		auditInfo.setCorrelationId("CorId");
		auditInfo.setOriginatorAppId("OrgId");
		auditInfo.setSalesRepId("SalesId");
		auditInfo.setUserId("UsrId");
		auditInfo.setTimestamp(new Date());
		
		return auditInfo;
	}
	
	private AuditHeaderInfo getAuditHeader()throws ApplicationException{
		AuditHeaderInfo auditHeader= new AuditHeaderInfo();
			auditHeader.setCustomerId("EJBTesting");
			auditHeader.setUserIPAddress("111.111.111.111");
			auditHeader.appendAppInfo("Anitha", 5284, "127.0.0.1");
			System.out.println("auditHeader"+auditHeader.toString());
		
		return auditHeader;
	}
	private BusinessCreditIdentityInfo getBusinessCreditIdentityInfo() throws ApplicationException{
		
		BusinessCreditIdentityInfo businessCreditIdentityInfo=new BusinessCreditIdentityInfo();
		businessCreditIdentityInfo.setMarketAccount(10);
		businessCreditIdentityInfo.setCompanyName("IBM");
		return businessCreditIdentityInfo;
	}
	
	private CreditCardInfo getPersonalCreditCardInfo(){
		CreditCardInfo creditCardInfo= new CreditCardInfo();
		creditCardInfo.setExpiryMonth(12);
		creditCardInfo.setExpiryYear(2015);
		creditCardInfo.setHolderName("Anitha Duraisamy");
		creditCardInfo.setLeadingDisplayDigits("519121");  
		creditCardInfo.setToken("100000000000000006551");
		creditCardInfo.setTrailingDisplayDigits("1111");
		creditCardInfo.setType("VS");
		creditCardInfo.setAuthorizationCode("123");	
		return creditCardInfo;
	}
	
	private CreditCardInfo getCreditCardInfo(){
		CreditCardInfo creditCardInfo= new CreditCardInfo();
		creditCardInfo.setExpiryMonth(12);
		creditCardInfo.setExpiryYear(2015);
		creditCardInfo.setHolderName("Danny ¿‡¬‚«Á…È»");
		creditCardInfo.setLeadingDisplayDigits("455630");  
		creditCardInfo.setToken("100000000000001366517");
		creditCardInfo.setTrailingDisplayDigits("3821");
		creditCardInfo.setType("VS");
		creditCardInfo.setAuthorizationCode("123");	
		return creditCardInfo;
	}
	private AccountInfo getPostpaidAccountInfo() throws ApplicationException, TelusAPIException
	{
	AccountInfo accInfo = PostpaidCorporateRegularAccountInfo.newInstance('L');
	accInfo.setAccountType('B');
	accInfo.setAccountSubType('R');
	((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setTitle("MR.");
	((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setFirstName("Dany");
	((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setLastName("Taylor");
	((PostpaidCorporateRegularAccountInfo)accInfo).setLegalBusinessName("Dany Taylor");
	((PostpaidCorporateRegularAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(java.sql.Date.valueOf("1960-01-20"));
	((PostpaidCorporateRegularAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
	((PostpaidCorporateRegularAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
	((PostpaidCorporateRegularAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
	accInfo.setCreateDate(new Date());
	accInfo.setAccountCategory("N");
	accInfo.setBrandId(1);
	accInfo.setBillCycle(3);
	accInfo.setIxcCode("TMI");
	
	accInfo.setBanSegment("TCSO");
	accInfo.setBanSubSegment("OTHR");

	AddressInfo  address =  new AddressInfo();
	address.setAddressType("F");
//	address.setAttention("a");
//	address.setPrimaryLine("41 Stonetone DR");
//	address.setSecondaryLine("Scarborough");
	address.setCity("Toronto");
	address.setProvince("ON");
	address.setPostalCode("M1H2P6");
	address.setCountry("CANADA");
	address.setCivicNo("16");
//	address.setCivicNoSuffix("");
//	address.setStreetNumber("90");
//	address.setStreetNumberSuffix("");
	address.setStreetName("Stonetone DR");
//	address.setStreetType("Stonetone DR");
//	address.setUnitType("House");
//	address.setUnit("41");
	address.setPoBox("M1H2P6");
//	address.setRuralLocation("a");
//	address.setRuralQualifier("a");
//	address.setRuralSite("a");
//	address.setRuralCompartment("a");
//	address.setRuralDeliveryType("a");
//	address.setRuralGroup("a");
//	address.setRuralType("a");
//	address.setRuralNumber("a");
//	address.setZipGeoCode("a");
//	address.setForeignState("a");
	
	accInfo.setAddress0(address);
	
	accInfo.setHomeProvince("ON");
	accInfo.setOtherPhoneType("MOBILE");
	accInfo.setOtherPhone("4167897890");
	accInfo.setEmail("anitha@telusmobility.com");
	accInfo.setPin("5555");
	accInfo.setLanguage("EN");
	accInfo.setDealerCode("0000000008");
	accInfo.setSalesRepCode("0000");
			
	accInfo.setHomePhone("4165551234");
	accInfo.setBusinessPhone("4165550000");
	accInfo.setNoOfInvoice(2);
	return accInfo;
	}
	private AccountInfo getAccountInfo() throws ApplicationException, TelusAPIException{
		
		
			
//		AccountInfo accInfo = PostpaidConsumerAccountInfo.newPCSInstance();
//		accInfo.setAccountType('I');
//		accInfo.setAccountSubType('R');// R, 1, M, E,etc 
//		((PostpaidConsumerAccountInfo)accInfo).getName0().setTitle("Ms.");
//		((PostpaidConsumerAccountInfo)accInfo).getName0().setFirstName("Anitha");
//		((PostpaidConsumerAccountInfo)accInfo).getName0().setLastName("Duraisamy");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().getCreditCard0().copyFrom(getPersonalCreditCardInfo());
//		((PostpaidConsumerAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("C");
//		((PostpaidConsumerAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
//		AccountInfo accInfo = PostpaidBoxedConsumerAccountInfo.newPagerInstance();
//		accInfo.setAccountType('I');
//		accInfo.setAccountSubType('J'); 
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getName0().setTitle("Ms.");
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getName0().setFirstName("Anitha");
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getName0().setLastName("Duraisamy");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidBoxedConsumerAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
//		AccountInfo accInfo = PostpaidBusinessDealerAccountInfo.getNewInstance0('B');
//		accInfo.setAccountType('B');
//		accInfo.setAccountSubType('B'); 
//		((PostpaidBusinessDealerAccountInfo) accInfo).setLegalBusinessName("TELUS");
//		((PostpaidBusinessDealerAccountInfo) accInfo).setTradeNameAttention("trade");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getContactName().setTitle("Mr.");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getContactName().setLastName("Honey");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidBusinessDealerAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidBusinessDealerAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
//		AccountInfo accInfo = PostpaidBusinessOfficialAccountInfo.newPCSInstance0();
//		accInfo.setAccountType('B');
//		accInfo.setAccountSubType('O'); 
//		((PostpaidBusinessOfficialAccountInfo) accInfo).setLegalBusinessName("TELUS");
//		((PostpaidBusinessOfficialAccountInfo) accInfo).setTradeNameAttention("trade");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getContactName().setTitle("Mr.");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getContactName().setLastName("Honey");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidBusinessOfficialAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
//		
		AccountInfo accInfo = PostpaidBusinessPersonalAccountInfo.getNewInstance0('P');
		accInfo.setAccountType('B');
		accInfo.setAccountSubType('G'); 
		((PostpaidBusinessPersonalAccountInfo) accInfo).setLegalBusinessName("TELUS");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getName0().setTitle("Mr.");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getName0().setFirstName("ClientAPI");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getName0().setLastName("YaTEst");
		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getContactName().setFirstName("hari");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getContactName().setTitle("Mr.");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getContactName().setLastName("Honey");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
		((PostpaidBusinessPersonalAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
		((PostpaidBusinessPersonalAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
//		AccountInfo accInfo = PostpaidBusinessRegularAccountInfo.getNewInstance('M');
//		accInfo.setAccountType('B');
//		accInfo.setAccountSubType('M'); 
//		((PostpaidBusinessRegularAccountInfo) accInfo).setLegalBusinessName("TELUS");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setTitle("Mr.");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setLastName("Honey");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
//		
		
//		AccountInfo accInfo = PostpaidCorporatePersonalAccountInfo.newInstance('I');
//		accInfo.setAccountType('C');
//		accInfo.setAccountSubType('I'); 
//		((PostpaidCorporatePersonalAccountInfo) accInfo).setLegalBusinessName("TELUS");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getName0().setTitle("Ms.");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getName0().setFirstName("Anitha");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getName0().setLastName("Duraisamy");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getContactName().setTitle("Mr.");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getContactName().setLastName("Honey");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidCorporatePersonalAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
		
		
//		AccountInfo accInfo = PostpaidBusinessRegularAccountInfo.newPCSInstance();
//		accInfo.setAccountType('B');
//		accInfo.setAccountSubType('R');
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setTitle("Ms.");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setLastName("Baby");
//		((PostpaidBusinessRegularAccountInfo)accInfo).setLegalBusinessName("testing");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(java.sql.Date.valueOf("1960-01-20"));
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("C");
//		((PostpaidBusinessRegularAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
//		AccountInfo accInfo = PostpaidEmployeeAccountInfo.getNewInstance0('E');
//		accInfo.setAccountType('I');
//		accInfo.setAccountSubType('E'); 
//		((PostpaidEmployeeAccountInfo)accInfo).getName0().setTitle("Ms.");
//		((PostpaidEmployeeAccountInfo)accInfo).getName0().setFirstName("Anitha");
//		((PostpaidEmployeeAccountInfo)accInfo).getName0().setLastName("Duraisamy");
//		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
//		((PostpaidEmployeeAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PostpaidEmployeeAccountInfo)accInfo).getContactName().setTitle("Mr.");
//		((PostpaidEmployeeAccountInfo)accInfo).getContactName().setLastName("Honey");
//		((PostpaidEmployeeAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
//		((PostpaidEmployeeAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidEmployeeAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidEmployeeAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
		
		// to test the postpaid account
//		AccountInfo accInfo = PostpaidCorporateRegularAccountInfo.newInstance('L');
//		accInfo.setAccountType('B');
//		accInfo.setAccountSubType('R');
//		((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setTitle("MR.");
//		((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setFirstName("Naresh");
//		((PostpaidCorporateRegularAccountInfo)accInfo).getContactName().setLastName("Test");
//		((PostpaidCorporateRegularAccountInfo)accInfo).setLegalBusinessName("Naresh Test");
//		((PostpaidCorporateRegularAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(java.sql.Date.valueOf("1960-01-20"));
//		((PostpaidCorporateRegularAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");
//		((PostpaidCorporateRegularAccountInfo)accInfo).getPaymentMethod0().setPaymentMethod("R");
//		((PostpaidCorporateRegularAccountInfo)accInfo).getPaymentMethod0().setCreditCard0(getCreditCardInfo());
		
		
		
//		AccountInfo accInfo = PrepaidConsumerAccountInfo.newPCSInstance('B');
//		accInfo.setAccountType('I');
//		accInfo.setAccountSubType('B');
//		((PrepaidConsumerAccountInfo)accInfo).getContactName().setTitle("Ms.");
//		((PrepaidConsumerAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((PrepaidConsumerAccountInfo)accInfo).getContactName().setLastName("Baby");
//		
//		((PrepaidConsumerAccountInfo) accInfo).getName0().setTitle("Ms.");
//		((PrepaidConsumerAccountInfo) accInfo).getName0().setFirstName("Anitha");
//		((PrepaidConsumerAccountInfo) accInfo).getName0().setMiddleInitial("D");
//		((PrepaidConsumerAccountInfo) accInfo).getName0().setLastName("Duraisamy");
//		//((PrepaidConsumerAccountInfo) accInfo).getName0().setGeneration("");
//		//((PrepaidConsumerAccountInfo) accInfo).getName0().setAdditionalLine("");
//		((PrepaidConsumerAccountInfo) accInfo).getName0().setNameFormat("P");
//		
	
		
//		AccountInfo accInfo = WesternPrepaidConsumerAccountInfo.newPCSInstance0();
//		accInfo.setAccountType('I');
//		accInfo.setAccountSubType('B');
//		((WesternPrepaidConsumerAccountInfo)accInfo).getContactName().setTitle("Ms.");
//		((WesternPrepaidConsumerAccountInfo)accInfo).getContactName().setFirstName("Honey");
//		((WesternPrepaidConsumerAccountInfo)accInfo).getContactName().setLastName("Baby");
//		
//		((WesternPrepaidConsumerAccountInfo) accInfo).getName0().setTitle("Ms.");
//		((WesternPrepaidConsumerAccountInfo) accInfo).getName0().setFirstName("Anitha");
//		((WesternPrepaidConsumerAccountInfo) accInfo).getName0().setMiddleInitial("D");
//		((WesternPrepaidConsumerAccountInfo) accInfo).getName0().setLastName("Duraisamy");
//		((WesternPrepaidConsumerAccountInfo) accInfo).getName0().setNameFormat("P");
		
		
		accInfo.setCreateDate(new Date());
		accInfo.setAccountCategory("N");
		accInfo.setBrandId(1);
		accInfo.setBillCycle(3);
		accInfo.setIxcCode("TMI");
		
		accInfo.setBanSegment("TCSO");
		accInfo.setBanSubSegment("OTHR");

		AddressInfo  address =  new AddressInfo();
		address.setAddressType("F");
//		address.setAttention("a");
	//	address.setPrimaryLine("41 Stonetone DR");
	//	address.setSecondaryLine("Scarborough");
		address.setCity("Toronto");
		address.setProvince("ON");
		address.setPostalCode("M1H2P6");
		address.setCountry("CANADA");
		address.setCivicNo("16");
//		address.setCivicNoSuffix("");
//		address.setStreetNumber("90");
//		address.setStreetNumberSuffix("");
		address.setStreetName("Stonetone DR");
//		address.setStreetType("Stonetone DR");
//		address.setUnitType("House");
//		address.setUnit("41");
		address.setPoBox("M1H2P6");
//		address.setRuralLocation("a");
//		address.setRuralQualifier("a");
//		address.setRuralSite("a");
//		address.setRuralCompartment("a");
//		address.setRuralDeliveryType("a");
//		address.setRuralGroup("a");
//		address.setRuralType("a");
//		address.setRuralNumber("a");
//		address.setZipGeoCode("a");
//		address.setForeignState("a");
		
		accInfo.setAddress0(address);
		
		accInfo.setHomeProvince("ON");
		accInfo.setOtherPhoneType("MOBILE");
		accInfo.setOtherPhone("4167897890");
		accInfo.setEmail("anitha@telusmobility.com");
		accInfo.setPin("5555");
		accInfo.setLanguage("EN");
		accInfo.setDealerCode("0000000008");
		accInfo.setSalesRepCode("0000");
				
		accInfo.setHomePhone("4165551234");
		accInfo.setBusinessPhone("4165550000");
		accInfo.setNoOfInvoice(2);
		
		return accInfo;
	}
	
	
	@Test
	public void testValidateCreditCard() throws ApplicationException, RemoteException {


		int ban = 7061604;
		CreditCardTransactionInfo ccti = new CreditCardTransactionInfo();
		ccti.setAmount(1.0);
		AuditHeaderInfo auditHeader = new AuditHeaderInfo();
		auditHeader.setCustomerId("2222");
		auditHeader.setUserIPAddress("172.23.14.53");
		ccti.setAuditHeader(auditHeader);
		ccti.setBan(ban);
		ccti.setBrandId(1);
		ccti.setChargeAuthorizationNumber("123");
		CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
		creditCardHolderInfo.setAccountSubType("R");
		creditCardHolderInfo.setAccountType("I");
		creditCardHolderInfo.setActivationDate(new Date());
		creditCardHolderInfo.setBusinessRole("ALL");
		creditCardHolderInfo.setBirthDate(java.sql.Date.valueOf("1960-10-25"));
		creditCardHolderInfo.setHomePhone("4169401000");
		creditCardHolderInfo.setClientID("0");
		creditCardHolderInfo.setFirstName("Danny");
		creditCardHolderInfo.setlastName("¿‡¬‚«Á…È»");
		creditCardHolderInfo.setCivicStreetNumber("300");
		creditCardHolderInfo.setStreetName("street name ¿‡¬‚«Á…È» CONSILIUM PL");
		creditCardHolderInfo.setCity("SCARBOROUGH");
		creditCardHolderInfo.setProvince("ON");
		creditCardHolderInfo.setPostalCode("L3R6C1");
		ccti.setCreditCardHolderInfo(creditCardHolderInfo);
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny ¿‡¬‚«Á…È»");
		cc.setLeadingDisplayDigits("341000");  
		cc.setToken("100000000000000000368");
		cc.setTrailingDisplayDigits("0008");
		cc.setType("VS");
		cc.setAuthorizationCode("123");			
		ccti.setCreditCardInfo(cc);
		ccti.setTransactionType("CCV");

		CreditCardResponseInfo authorizationCode = facadeImpl.validateCreditCard(ccti, sessionId);
		System.out.println("authorizationCode = " + authorizationCode.getAuthorizationCode()); 
		System.out.println("testValidateCreditCard END"); 
	}
	
	@Test
	public void testPayBillAndPayDeposit() throws RemoteException, ApplicationException {
		int ban = 7061604;
		CreditCardTransactionInfo ccti = new CreditCardTransactionInfo();
		ccti.setAmount(1.0);
		AuditHeaderInfo auditHeader = new AuditHeaderInfo();
		auditHeader.setCustomerId("11111");
		auditHeader.setUserIPAddress("172.23.14.53");
		ccti.setAuditHeader(auditHeader);
		ccti.setBan(ban);
		ccti.setBrandId(1);
		ccti.setChargeAuthorizationNumber("123");
		CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
		creditCardHolderInfo.setAccountSubType("R");
		creditCardHolderInfo.setAccountType("I");
		creditCardHolderInfo.setActivationDate(new Date());
		creditCardHolderInfo.setBusinessRole("ALL");
		creditCardHolderInfo.setBirthDate(java.sql.Date.valueOf("1960-10-25"));
		creditCardHolderInfo.setHomePhone("4169401000");
		creditCardHolderInfo.setClientID("70107112");
		creditCardHolderInfo.setFirstName("Danny");
		creditCardHolderInfo.setlastName("Summer");
		creditCardHolderInfo.setCivicStreetNumber("300");
		creditCardHolderInfo.setStreetName("CONSILIUM PL");
		creditCardHolderInfo.setCity("SCARBOROUGH");
		creditCardHolderInfo.setProvince("ON");
		creditCardHolderInfo.setPostalCode("L3R6C1");
		ccti.setCreditCardHolderInfo(creditCardHolderInfo);
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("341000");  
		cc.setToken("100000000000000000368");
		cc.setTrailingDisplayDigits("0008");
		cc.setType("VS");
		cc.setAuthorizationCode("123");			
		ccti.setCreditCardInfo(cc);
		ccti.setTransactionType("CCV");
		
		try {
			facadeImpl.payBill(10, ccti , ban, null, null, null,false,null,sessionId);
		} catch (ApplicationException ae) {
			assertEquals("1111190", ae.getErrorCode());
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
		}
	}
	
	@Test
	public void testRefundCreditCardpayment() throws RemoteException, ApplicationException {
		int ban = 4820;
		CreditCardTransactionInfo ccti = new CreditCardTransactionInfo();
		ccti.setAmount(1.0);
		AuditHeaderInfo auditHeader = new AuditHeaderInfo();
		auditHeader.setCustomerId("11111");
		auditHeader.setUserIPAddress("172.23.14.53");
		ccti.setAuditHeader(auditHeader);
		ccti.setBan(ban);
		ccti.setBrandId(1);
		ccti.setChargeAuthorizationNumber("123");
		CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
		creditCardHolderInfo.setAccountSubType("R");
		creditCardHolderInfo.setAccountType("I");
		creditCardHolderInfo.setActivationDate(new Date());
		creditCardHolderInfo.setBusinessRole("ALL");
		creditCardHolderInfo.setBirthDate(java.sql.Date.valueOf("1960-10-25"));
		creditCardHolderInfo.setHomePhone("4169401000");
		creditCardHolderInfo.setClientID("7061604");
		creditCardHolderInfo.setFirstName("Danny");
		creditCardHolderInfo.setlastName("Summer");
		creditCardHolderInfo.setCivicStreetNumber("300");
		creditCardHolderInfo.setStreetName("CONSILIUM PL");
		creditCardHolderInfo.setCity("SCARBOROUGH");
		creditCardHolderInfo.setProvince("ON");
		creditCardHolderInfo.setPostalCode("L3R6C1");
		ccti.setCreditCardHolderInfo(creditCardHolderInfo);
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("341000");  
		cc.setToken("100000000000000000368");
		cc.setTrailingDisplayDigits("0008");
		cc.setType("VS");
		cc.setAuthorizationCode("123");			
		ccti.setCreditCardInfo(cc);
		ccti.setTransactionType("CCV");
		
		facadeImpl.refundCreditCardPayment(ban, 123, "321", "321", ccti, sessionId);
	}
	
	@Test
	public void testRegisterTopUpCreditCard() throws ApplicationException, RemoteException{
		
		int ban = 4036924;
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("341000");  
		cc.setToken("100000000000000000368");
		cc.setTrailingDisplayDigits("0008");
		cc.setType("VS");
		cc.setAuthorizationCode("123");		
		facadeImpl.registerTopUpCreditCard(ban, cc, sessionId);
		
	}
	@Test
	public void testUpdateAccountPassword() throws ApplicationException, RemoteException{
		
		int ban=197806; //70100340;
		String newPassword="ClientAPI";
		System.out.println("testUpdateAccountPassword Start ");
		facadeImpl.updateAccountPassword(ban, newPassword, sessionId);
		System.out.println("testUpdateAccountPassword End ");
	}
	@Test
	public void testAsyncCreateMemo() throws ApplicationException, RemoteException{
		
		//message body
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setBanId(4036924);
		memoInfo.setSubscriberId("4037109656");
		memoInfo.setMemoType("DGDY");
		memoInfo.setProductType("C");
		memoInfo.setDate(new Date() );
		memoInfo.setText("async memo creation test.");
		facadeImpl.asyncCreateMemo(memoInfo, sessionId);
		System.out.println("asyncCreateMemo End ");
	}
	
	@Test
	public void testGetTotalDataOutstandingAmount() throws ApplicationException, RemoteException{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -10);
		System.out.println( "Total unpaid data usage: " + facadeImpl.getTotalDataOutstandingAmount(4036924,cal.getTime()) );
	}
	
	@Test
	public void testIsEnterpriseManagedData() throws ApplicationException, RemoteException{
		int brandId=6017893;
		char accountType='B';
		char accountSubType='R';
		String productType="C";
		String processType="AccountUpdate";
		boolean eligible =facadeImpl.isEnterpriseManagedData(brandId, accountType, accountSubType, productType, processType);
		System.out.println( "eligible " + eligible );
	}
	
	@Test
	public void testInsertBillingAccount() throws Exception {
		facadeImpl.insertBillingAccount(8, "1", "AccountActivation", sessionId);
	}
	
	@Test
	public void testAsyncInsertBillingAccount() throws ApplicationException, RemoteException{
		facadeImpl.asyncInsertBillingAccount(6017893, "1", "AccountActivation", sessionId);
		System.out.println("testAsyncInsertBillingAccount End ");
	}
	
	@Test
	public void testInsertCustomerWithBillingAccount() throws Exception {
		int ban = 6017893;
		facadeImpl.insertCustomerWithBillingAccount(ban, "AccountActivation", sessionId);
	}
	
	@Test
	public void testAsyncInsertCustomerWithBillingAccount() throws Exception {
		int ban = 6017893;
		facadeImpl.asyncInsertCustomerWithBillingAccount(ban, "AccountActivation", sessionId);
	}
	@Test
	public void testUpdateBillingAccount() throws Exception {
		facadeImpl.updateBillingAccount(6017893, "AccountUpdate", sessionId);
	}
	
	@Test
	public void testUpdateBillingAccount1() throws Exception {
		AccountInfo accountInfo = helperImpl.retrieveAccountByBan(6017893);
		facadeImpl.updateBillingAccount(accountInfo, "AccountUpdate", sessionId);
	}
	
	@Test
	public void testAsyncUpdateBillingAccount() throws Exception {
		facadeImpl.asyncUpdateBillingAccount(6017893, "AccountUpdate", sessionId);
	}
	
	@Test
	public void testAsyncUpdateBillingAccount1() throws Exception {
		AccountInfo accountInfo = helperImpl.retrieveAccountByBan(6017893);
		facadeImpl.asyncUpdateBillingAccount(accountInfo, "AccountUpdate", sessionId);
	}

	@Test
	public void testValidateAddress() throws Exception {
		try{
             
			AddressInfo addressInfo = new AddressInfo();
	        addressInfo.setAddressType("D");
	        addressInfo.setCountry("CAN");
	        addressInfo.setCity("ST.JOHN'S");
	        addressInfo.setProvince("NF");
	        addressInfo.setPostalCode("A1B2X2");
	        addressInfo.setStreetName("Rowan St");
	        addressInfo.setStreetNumber("31");
	        addressInfo.setStreetType("ST");
	        
			
			AddressValidationResultInfo avri = facadeImpl.validateAddress(addressInfo);
			System.out.println(""+avri.toString());
			System.out.println(avri.getnCodeReturnStatus());
			System.out.println(avri.getValidationMessages()[0].getMessage());
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncCreateFollowUp() throws Exception {
		
		try{

			FollowUpInfo followUpInfo = new FollowUpInfo();
			followUpInfo.setDueDate(new Date(2011-1900,10,19));
			followUpInfo.setText("*******TELUS*********");
			followUpInfo.setAssignedToWorkPositionId("CS_000M5");
			followUpInfo.setType("SSPA");
			followUpInfo.setBan(6017893);

			facadeImpl.asyncCreateFollowUp(followUpInfo , sessionId);

		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncUpdateCreditProfile() throws Exception {
		try{
			int ban = 6017893;
			String newCreditClass = "C";
			double newCreditLimit = 55;
			String memoText = "memo_test";
			facadeImpl.asyncUpdateCreditProfile(ban, newCreditClass, newCreditLimit, memoText, sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncUpdateCreditCheckResult() throws Exception {
		try{
			int ban = 6017893;
			CreditCheckResultDepositInfo[] creditCheckResultDepositInfo = new CreditCheckResultDepositInfo[1];
			creditCheckResultDepositInfo[0] = new CreditCheckResultDepositInfo();
			creditCheckResultDepositInfo[0].setDeposit(1.1);
			creditCheckResultDepositInfo[0].setProductType("C");
			String depositChangedReasonCode = "103";
			String depositChangeText = "####deposit change test######";
			String pCreditClass = "B";
			
			facadeImpl.asyncUpdateCreditCheckResult(ban, pCreditClass , creditCheckResultDepositInfo, depositChangedReasonCode, depositChangeText, sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncasynSaveCreditCheckInfo() throws Exception {
		try{
			int ban = 6017893; 

			CreditCheckResultInfo creditCheckResultInfo  = new CreditCheckResultInfo();
			creditCheckResultInfo.setCreditClass("C");
			creditCheckResultInfo.setCreditScore(55);
			creditCheckResultInfo.setLimit(10.05);
			CreditCheckResultDepositInfo[] deposits = new CreditCheckResultDepositInfo[1];
			deposits[0] = new CreditCheckResultDepositInfo();
			deposits[0].setProductType("C");
			deposits[0].setDeposit(5.55);
			creditCheckResultInfo.setDeposits(deposits);
			creditCheckResultInfo.setMessage("test");
			creditCheckResultInfo.setMessageFrench("test_fr");
			creditCheckResultInfo.setReferToCreditAnalyst(false);
			String pCreditParamType = "I";
			PersonalCreditInfo personalCreditInfo = new PersonalCreditInfo();
			personalCreditInfo.setSin("123456789");
			
			facadeImpl.asyncSaveCreditCheckInfo(ban, personalCreditInfo, creditCheckResultInfo, pCreditParamType , sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncSaveCreditCheckInfoForBusiness() throws Exception {
		try{
			int ban = 6017893; 
			BusinessCreditInfo businessCreditInfo = new BusinessCreditInfo();
			businessCreditInfo.setIncorporationDate(new Date(2011-1900,10,19));
			businessCreditInfo.setIncorporationNumber("123456");
			BusinessCreditIdentityInfo[] listOfBusinesses = new BusinessCreditIdentityInfo[2];
			listOfBusinesses[0] = new BusinessCreditIdentityInfo();
			listOfBusinesses[0].setCompanyName("ABC Corp.");
			listOfBusinesses[0].setMarketAccount(333.33);
			listOfBusinesses[1] = new BusinessCreditIdentityInfo();
			listOfBusinesses[1].setCompanyName("XYZ Corp.");
			listOfBusinesses[1].setMarketAccount(666.66);
			BusinessCreditIdentityInfo selectedBusiness = new BusinessCreditIdentityInfo();
			selectedBusiness.setCompanyName("ABC Corp.");
			selectedBusiness.setMarketAccount(333.33);
			CreditCheckResultInfo pCreditCheckResultInfo = new CreditCheckResultInfo();
			pCreditCheckResultInfo.setCreditClass("C");
			pCreditCheckResultInfo.setCreditScore(55);
			pCreditCheckResultInfo.setLimit(10.05);
			String pCreditParamType = "I";
			facadeImpl.asyncSaveCreditCheckInfoForBusiness(ban, businessCreditInfo, listOfBusinesses, selectedBusiness, pCreditCheckResultInfo, pCreditParamType, sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testAsyncNotifyPayment() throws Throwable {
//		int ban = 70691160; //PT148
		int ban = 70105381; //D3
		
		AccountInfo accountInfo = helperImpl.retrieveAccountByBan(ban);
		accountInfo.setEmail("pavel.simonovsky@telus.com");
		
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setPaymentMethod(TelusConstants.PAYMENT_METHOD_CREDIT_CARD);
		paymentInfo.setAmount(10.00);
		paymentInfo.setAuthorizationNumber("1234");
		paymentInfo.setBan(ban);
		
		CreditCardInfo newCreditCardInfo = new CreditCardInfo();
		newCreditCardInfo.setLeadingDisplayDigits("999999");
		newCreditCardInfo.setTrailingDisplayDigits("8888");
		newCreditCardInfo.setToken("77777");
		newCreditCardInfo.setAuthorizationCode("000009");
		
		paymentInfo.setCreditCardInfo(newCreditCardInfo);
		
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		
		facadeImpl.asyncNotifyPayment(accountInfo, paymentInfo, false, auditInfo, sessionId);
	}
	
}
