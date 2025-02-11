package com.telus.cmb.account.lifecyclemanager.svc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.PaymentCard;
import com.telus.api.account.PaymentTransfer;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.CollectionStateInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.ChargeAdjustmentCodeInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
//import com.telus.eas.framework.info.LMSRequestInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.ServiceInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class AccountLifecycleManagerImplIntTest {

	AccountLifecycleManager managerImpl = null;
	AccountInformationHelper helperImpl = null;
//	String url="t3://cmosr-custinfomgmt2-dv103.tmi.telus.com:12021";
//	t3://cmosr-custinfomgmt2-dv103.tmi.telus.com:12022
//	String url="t3://localhost:7001";
//	String url="t3://ln98550.corp.ads:12022";	// D3
	String url="t3://ln98557.corp.ads:31022"; // PT168
//	String url="t3://ln98556.corp.ads:31022"; // PT168
	//String url = "t3://ln98556:30022"; //PT148
	String sessionId;

	@Autowired
	CreditInfo creditInfo;	
	@Autowired
	ChargeInfo chargeInfo;
	@Autowired
	Integer prepaidBAN;
	@Autowired
	String prepaidSub;
	@Autowired
	Integer sampleBAN;
	@Autowired
	PrepaidConsumerAccountInfo prepaidConsumerAccountInfo;


	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getAccountLifecycleManager(context);		
		sessionId = managerImpl.openSession("18654", "apollo", "OLN");
		System.setProperty("cmb.services.ReferenceDataFacade.url", "t3://localhost:7001");
		System.setProperty("cmb.services.ReferenceDataHelper.url", "t3://localhost:7001");
	}

	private Hashtable<Object,Object> setEnvContext(){

		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}
	
	private void getAccountLifecycleManager(Context context) throws Exception{
		managerImpl = (AccountLifecycleManager) context.lookup(EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER);

		helperImpl = (AccountInformationHelper) context.lookup(EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER);
	}

	@Test
	public void testCreateAccount() throws ApplicationException {
		System.out.println("testCreateAccount START");

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

		//    		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setTitle("MR.");
		//    		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setFirstName("Dany");
		//    		((PostpaidBusinessRegularAccountInfo)accInfo).getContactName().setLastName("Taylor");
		//    		((PostpaidBusinessRegularAccountInfo)accInfo).setLegalBusinessName("Dany Taylor");

		accInfo.setHomePhone("4165551234");
		accInfo.setBusinessPhone("4165550000");

		accInfo.setNoOfInvoice(2);


		java.sql.Date bd = java.sql.Date.valueOf("1960-01-20");
		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
		((PostpaidConsumerAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");

		//    		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setBirthDate(bd);
		//   		((PostpaidBusinessRegularAccountInfo)accInfo).getPersonalCreditInformation0().setSin("123456789");


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
	public void testSaveCreditCheckInfo() throws ApplicationException {
		System.out.println("testSaveCreditCheckInfo START"); 	
		int ban = 194587;
		AccountInfo accountInfo = helperImpl.retrieveAccountByBan(ban);
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

		managerImpl.saveCreditCheckInfo(accountInfo, creditCheckResultInfo, "I", sessionId);	

		//    		managerImpl.saveCreditCheckInfoForBusiness(accountInfo, null, null, creditCheckResultInfo, "I", sessionId);
		System.out.println("testCreateAccount END");    		
	}

	@Test
	public void testUpdateBillCycle() throws ApplicationException {
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
	public void testUpdateEmailAddress() throws ApplicationException {
		System.out.println("testUpdateEmailAddress START"); 

		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
		System.out.println("sessionId = " + sessionId);

		managerImpl.updateEmailAddress(194587, "pt148@yahoo.ca", sessionId);
		System.out.println("testUpdateEmailAddress END"); 
	}	

	@Test
	public void testUpdateInvoiceProperties() throws ApplicationException {
		System.out.println("testUpdateInvoiceProperties START"); 

		int ban = 194587;
		InvoicePropertiesInfo invoicePropertiesInfo = new InvoicePropertiesInfo();
		invoicePropertiesInfo.setBan(ban);
		invoicePropertiesInfo.setInvoiceSuppressionLevel("2");

		managerImpl.updateInvoiceProperties(ban, invoicePropertiesInfo, sessionId);
		System.out.println("testUpdateInvoiceProperties END"); 
	}		

	@Test
	public void testUpdateCreditCheckResult() throws ApplicationException {
		System.out.println("testUpdateCreditCheckResult START"); 

		int ban = 194587;
		CreditCheckResultDepositInfo[] creditCheckResultDepositInfo = new CreditCheckResultDepositInfo[1];
		creditCheckResultDepositInfo[0] = new CreditCheckResultDepositInfo();
		creditCheckResultDepositInfo[0].setDeposit(1.1);
		creditCheckResultDepositInfo[0].setProductType("C");

		String depositChangedReasonCode = "103";
		String depositChangeText = "deposit change test";

		managerImpl.updateCreditCheckResult(ban, "B", creditCheckResultDepositInfo, 
				depositChangedReasonCode, depositChangeText, sessionId);
		System.out.println("testUpdateCreditCheckResult END"); 
	}	

	@Test
	public void testUpdateCreditProfile() throws ApplicationException {
		System.out.println("testUpdateCreditProfile START"); 

		int ban = 194587;

		managerImpl.updateCreditProfile(ban, "C", 55, "memo_test", sessionId);
		System.out.println("testUpdateCreditProfile END"); 
	}			

	@Test
	public void testUpdateAccountPassword() throws ApplicationException {
		int ban = 194587;

		managerImpl.updateAccountPassword(ban, "1111", sessionId);
		System.out.println("testUpdateAccountPassword END"); 
	}

	@Test
	public void testApplyPaymentToAccount() throws ApplicationException {

		int ban = 194587;
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		creditCardInfo.setExpiryMonth(12);
		creditCardInfo.setExpiryYear(2015);
		creditCardInfo.setHolderName("Test");
		creditCardInfo.setLeadingDisplayDigits("490000");
		creditCardInfo.setToken("100000000000000006427");
		creditCardInfo.setTrailingDisplayDigits("0235");
		creditCardInfo.setType("VS");
		creditCardInfo.setAuthorizationCode("123");

		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBan(ban);
		paymentInfo.setAmount(5.5);
		paymentInfo.setCreditCardInfo(creditCardInfo);
		paymentInfo.setPaymentSourceID("PREACRCD");
		paymentInfo.setPaymentSourceType("C");
		paymentInfo.setDepositPaymentIndicator(false);
		paymentInfo.setDepositDate(new Date());
		paymentInfo.setAllowOverpayment(true);
		paymentInfo.setPaymentMethod("CC");

		managerImpl.applyPaymentToAccount(paymentInfo, sessionId);
		System.out.println("testApplyPaymentToAccount END"); 
	}
	@Test
	public void testCreateMemo() throws ApplicationException {

		int ban = 816166;
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setBanId(ban);
		memoInfo.setDate(new Date());
		memoInfo.setMemoType("DGDY");
		memoInfo.setSubscriberId("2047980182");
		memoInfo.setProductType("C");
		memoInfo.setText("dmitry's new memo test 333");
		memoInfo.setModifyDate(new Date());
		memoInfo.setOperatorId(18654);
		memoInfo.setMemoId(111);


		managerImpl.createMemo(memoInfo, sessionId);
		System.out.println("testCreateMemo END"); 
	}

	@Test
	public void testUpdatePaymentMethod() throws ApplicationException {

		int ban = 816166;
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


		managerImpl.updatePaymentMethod(ban, null, null);
		System.out.println("testUpdatePaymentMethod END"); 
	}

	@Test
	public void testChangePaymentMethodToRegular() throws ApplicationException {

		int ban = 816166;


		managerImpl.changePaymentMethodToRegular(ban, sessionId);
		System.out.println("testChangePaymentMethodToRegular END"); 
	}	

	@Test
	public void testRetrieveCancellationPenalty() throws ApplicationException {

		int ban = 12474;


		CancellationPenaltyInfo cp = managerImpl.retrieveCancellationPenalty(ban, sessionId);
		System.out.println("CancellationPenaltyInfo = " + cp); 

		System.out.println("testRetrieveCancellationPenalty END"); 
	}


	@Test	
	public void testRetrieveCancellationPenaltyList() throws ApplicationException {

		int ban = 12474;
		String[] subscriberIds = {"4033946834"};

		CancellationPenaltyInfo[] cp = managerImpl.retrieveCancellationPenaltyList(ban, subscriberIds, sessionId);
		for (int i = 0; i < cp.length; i++){
			System.out.println("CancellationPenaltyInfo[" + i +"]= " + cp[i]); 
		}
		System.out.println("testRetrieveCancellationPenaltyList END"); 
	}	

	@Test	
	public void testupdateNationalGrowth() throws ApplicationException {

		int ban = 816166;
		String nationalGrowthIndicator="";
		String homeProvince="";

		managerImpl.updateNationalGrowth(ban, nationalGrowthIndicator, homeProvince, sessionId);
	}	

	@Test	
	public void testrestoreSuspendedAccount1() throws ApplicationException {

		int ban = 816166;
		String restoreReasonCode="";

		try {
			managerImpl.restoreSuspendedAccount(ban, restoreReasonCode, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111720", ae.getErrorCode());
		}
	}	

	@Test	
	public void testrestoreSuspendedAccount2() throws ApplicationException {

		int ban = 816166;
		Date restoreDate=new Date();
		String restoreReasonCode="";
		String restoreComment="";
		boolean collectionSuspensionsOnly=true;

		try {
			managerImpl.restoreSuspendedAccount(ban, restoreDate, restoreReasonCode, restoreComment, collectionSuspensionsOnly, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111720", ae.getErrorCode());
		}

	}

	@Test	
	public void testupdateFutureStatusChangeRequest() throws ApplicationException {

		int ban = 816166;
		FutureStatusChangeRequestInfo futureStatusChangeRequestInfo=new FutureStatusChangeRequestInfo();

		try {
			managerImpl.updateFutureStatusChangeRequest(ban, futureStatusChangeRequestInfo, sessionId);
			fail("Exception expected");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110750", ae.getErrorCode());
		}

	}

	@Test	
	public void testSuspendAccount() throws ApplicationException {
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
	public void testAddFleetandDissociateFleet() throws ApplicationException {

		FleetInfo fleetInfo = new FleetInfo();
		fleetInfo.getIdentity0().setFleetId(131072);
		fleetInfo.getIdentity0().setUrbanId(905);

		try {
			managerImpl.addFleet(6001554, (short)0, fleetInfo, 0, sessionId);
		} catch (SystemException se) {
			if (se.getErrorCode().equals(ErrorCodes.AMDOCS_FLEET_ALREADY_ASSOCIATE_TO_THIS_BAN)) {
				managerImpl.dissociateFleet(6001554, fleetInfo, sessionId);	
				managerImpl.addFleet(6001554, (short)0, fleetInfo, 0, sessionId);
			}
		}

		managerImpl.dissociateFleet(6001554, fleetInfo, sessionId);		
	}

	@Test
	public void testCreateTalkGroup() throws ApplicationException {
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		talkGroupInfo.getFleetIdentity().setFleetId(131075);
		talkGroupInfo.getFleetIdentity().setUrbanId(905);
		talkGroupInfo.setName("123");
		talkGroupInfo.setOwnerBanId(6001554);
		talkGroupInfo.setPriority(5);
		talkGroupInfo.setTalkGroupId(1);
		try {
			managerImpl.createTalkGroup(6001554, talkGroupInfo, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111440", ae.getErrorCode());
		}
	}
	@Test
	public void testCreateFleet() throws ApplicationException {
		FleetInfo fleetInfo = new FleetInfo();		
		fleetInfo.getIdentity0().setFleetId(131072);
		fleetInfo.getIdentity0().setUrbanId(905);

		try {
			managerImpl.createFleet(6001554, (short)1, fleetInfo, 1, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode());
		}
	}

	@Test
	public void testAddTalkGroup() throws ApplicationException {
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		talkGroupInfo.getFleetIdentity().setFleetId(131075);
		talkGroupInfo.getFleetIdentity().setUrbanId(905);
		talkGroupInfo.setName("123");
		talkGroupInfo.setOwnerBanId(6001554);
		talkGroupInfo.setPriority(5);
		talkGroupInfo.setTalkGroupId(1);
		try {
			managerImpl.addTalkGroup(6001554, talkGroupInfo, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111440", ae.getErrorCode());
		}
	}

	@Test
	public void testAddTalkGroups() throws ApplicationException {
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		talkGroupInfo.getFleetIdentity().setFleetId(131075);
		talkGroupInfo.getFleetIdentity().setUrbanId(905);
		talkGroupInfo.setName("123");
		talkGroupInfo.setOwnerBanId(6001554);
		talkGroupInfo.setPriority(5);
		talkGroupInfo.setTalkGroupId(1);

		TalkGroupInfo[] talkGroupInfos = new TalkGroupInfo[1];
		talkGroupInfos[0] = talkGroupInfo;		

		try {
			managerImpl.addTalkGroups(6001554, talkGroupInfos, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111440", ae.getErrorCode());
		}
	}	

	@Test
	public void testupdateTalkGroup() throws ApplicationException {
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		talkGroupInfo.getFleetIdentity().setFleetId(131075);
		talkGroupInfo.getFleetIdentity().setUrbanId(905);
		talkGroupInfo.setName("123");
		talkGroupInfo.setOwnerBanId(6001554);
		talkGroupInfo.setPriority(5);
		talkGroupInfo.setTalkGroupId(1);

		try {
			managerImpl.updateTalkGroup(6001554, talkGroupInfo, sessionId);
		} catch (ApplicationException se) {
			assertEquals(SystemCodes.AMDOCS, se.getSystemCode());
			assertEquals("1111440", se.getErrorCode());
		}
	}

	@Test
	public void testremoveTalkGroup() throws Throwable {
		TalkGroupInfo talkGroupInfo = new TalkGroupInfo();
		talkGroupInfo.getFleetIdentity().setFleetId(131075);
		talkGroupInfo.getFleetIdentity().setUrbanId(905);
		talkGroupInfo.setName("123");
		talkGroupInfo.setOwnerBanId(6001554);
		talkGroupInfo.setPriority(5);
		talkGroupInfo.setTalkGroupId(1);

		try {
			managerImpl.removeTalkGroup(6001554, talkGroupInfo, sessionId);
			fail("Exception expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1111440", ae.getErrorCode());
		}
	}

	@Test
	public void testUpdateFollowUp() throws ApplicationException {		
		FollowUpUpdateInfo followUpUpdateInfo = new FollowUpUpdateInfo();
		followUpUpdateInfo.setBan(6001554);
		followUpUpdateInfo.setFollowUpId(321321);

		managerImpl.updateFollowUp(followUpUpdateInfo, sessionId);		
	}
	@Test
	public void testCreateFollowUp() throws ApplicationException {		
		FollowUpInfo followUpInfo = new FollowUpInfo();
		followUpInfo.setBan(6001554);	

		try {
			managerImpl.createFollowUp(followUpInfo, sessionId);
			fail("Execption expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1112210", ae.getErrorCode());
		}
	}

	@Test
	public void testUpdateBillSuppression() throws ApplicationException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -10);
		try {
			managerImpl.updateBillSuppression(321, false, cal.getTime(), new Date(), sessionId);
			fail("Execption expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110680", ae.getErrorCode());
		}
	}

	@Test
	public void testUpdateReturnEnvelopIndicator() throws ApplicationException {
		try {

			managerImpl.updateReturnEnvelopeIndicator(321, false, sessionId);
			fail("Execption expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110680", ae.getErrorCode());
		}
	}

	@Test
	public void testUpdateInvoiceSuppressionIndicator() throws ApplicationException {
		try {
			managerImpl.updateInvoiceSuppressionIndicator(321, sessionId);
			fail("Exception Expected.");
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110680", ae.getErrorCode());
		}
	}



	@Test
	public void testRetrieveBanCollectionInfo() throws ApplicationException {

		CollectionStateInfo csi = managerImpl.retrieveBanCollectionInfo(70554724, sessionId);
		System.out.println("OUT : "+csi.toString());
		assertNotNull(csi);
	}

	@Test
	public void testUpdateNextStepCollection() throws ApplicationException {
		try {
			managerImpl.updateNextStepCollection(6002364, 321, new Date(), "321321", sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110560", ae.getErrorCode());
		}
	}

	@Test
	public void testCancelSubscribers() throws ApplicationException {
		try {
			managerImpl.cancelSubscribers(70616988, new Date(), "321", 'Y', new String[1], new String[1], "1", sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115020", ae.getErrorCode());
		}
	}

	@Test
	public void testChangeKnowbilityPassword() throws ApplicationException {
		try {
			managerImpl.changeKnowbilityPassword("321", "321", "321", sessionId);
		} catch (ApplicationException ae) {
			assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
			assertEquals("1118020", ae.getErrorCode());
		}
	}
	@Test	
	public void testCancelAccount() throws ApplicationException {
		try{
			System.out.println("testCancelAccount START"); 

			String activityReasonCode="RETC";
			String depositReturnMethod="";//R

			managerImpl.cancelAccount(6012776, new Date(), activityReasonCode, depositReturnMethod, "", "userMemoText",false, sessionId);

			System.out.println("testCancelAccount END"); 
		} catch (ApplicationException ae){
			assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
			assertEquals("Deposit Return Method should not be null.", ae.getErrorMessage());


		}
	}


	@Test
	public void testupdateAuthorizationNames() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6012776;
		ConsumerNameInfo[] authorizationNames = new ConsumerNameInfo[1];
		authorizationNames[0]=new ConsumerNameInfo();
		authorizationNames[0].setFirstName("SIMPLE");
		authorizationNames[0].setLastName("TEST");

		try{

			managerImpl.updateAuthorizationNames(ban, authorizationNames, sessionId);
			
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110680", ae.getErrorCode());
		}
	}

	@Test
	public void testupdateAutoTreatment() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 123;
		boolean holdAutoTreatment=true;
		try{

			managerImpl.updateAutoTreatment(ban, holdAutoTreatment, sessionId);
			fail("Exception expected");
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("SYS00015", ae.getErrorCode());
		}
	}

	@Test
	public void testupdateBrand() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6016913;
		int brandId=0;
		String memoText="";

		try{

			managerImpl.updateBrand(ban, brandId, memoText, sessionId);
			fail("Exception expected");
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110996", ae.getErrorCode());
		}
	}

	@Test
	public void testupdateSpecialInstructions() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6016913;
		String specialInstructions="SIMPLE TEST";
		try{
			managerImpl.updateSpecialInstructions(ban, specialInstructions, sessionId);
			
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("-1", ae.getErrorCode());
		}
	}

	@Test
	public void testapplyFeeWaiver() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		FeeWaiverInfo feeWaiverInfo=new FeeWaiverInfo();
		feeWaiverInfo.setBanId(6016913);
		feeWaiverInfo.setReasonCode("ABC");

		try{

			managerImpl.applyFeeWaiver(feeWaiverInfo, sessionId);
			
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("-1", ae.getErrorCode());
		}
	}

	@Test
	public void testValidateAddress() throws ApplicationException {
		AddressInfo addressInfo = new AddressInfo();
		AddressValidationResultInfo validateAddress = managerImpl.validateAddress(addressInfo, sessionId);

		assertNotNull(validateAddress);
		assertEquals(1, validateAddress.getCountInformationalMessages());
	}

	@Test
	public void testRetrieveAccountsByTalkGroup() throws ApplicationException {
		int urbanId = 905;
		int fleetId = 131075;
		int talkGroupId = 1;

		@SuppressWarnings("unchecked")
		Collection<AccountInfo> accounts = (Collection<AccountInfo>)managerImpl.retrieveAccountsByTalkGroup(urbanId, fleetId, talkGroupId, sessionId);
		assertEquals(0, accounts.size());
	}

	@Test
	public void testRetrieveAmdocsCreditCheckResultByBan() throws ApplicationException {
		int ban = 6016913;
		CreditCheckResultInfo retrieveAmdocsCreditCheckResultByBan = managerImpl.retrieveAmdocsCreditCheckResultByBan(ban, sessionId);

		assertEquals("D", retrieveAmdocsCreditCheckResultByBan.getCreditClass());
		assertEquals(0, retrieveAmdocsCreditCheckResultByBan.getCreditScore());
	}

	@Test
	public void testchangePostpaidConsumerToPrepaidConsumer() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6006430;
		try{
			managerImpl.changePostpaidConsumerToPrepaidConsumer(ban, sessionId);
			fail("Exception expected");
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
		}
	}

	@Test
	public void testretrieveDiscounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6006430;
		List di=managerImpl.retrieveDiscounts(ban, sessionId);
		assertEquals(0, di.size());
	}

	@Test
	public void testcancelAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban = 6006430;
		String activityReasonCode="";
		Date activityDate=new Date();
		String portOutInd="Y";
		boolean isBrandPort=false;
		try{
			managerImpl.cancelAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, isBrandPort, sessionId);
			fail("Exception expected");
		}catch (ApplicationException ae) {
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115610", ae.getErrorCode());
		}
	}
//	@Test
//	public void testCreateManualLetterRequest() throws ApplicationException{
//
//		int ban = 6016913;
//		LMSLetterRequestInfo letterRequestInfo = new LMSLetterRequestInfo();
//		letterRequestInfo.setBanId(ban);
//		try{
//			managerImpl.createManualLetterRequest(letterRequestInfo, sessionId);
//		} catch (ApplicationException ae){
//			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
//			assertEquals("1110560", ae.getErrorCode());
//		}
//	}
//	@Test
//	public void testcreateManualLetterRequest() throws ApplicationException{
//		int ban = 6016913;
//		LMSRequestInfo requestInfo = new LMSRequestInfo();
//		requestInfo.setBan(ban);
//		try{
//			managerImpl.createManualLetterRequest(requestInfo, sessionId);
//		} catch (ApplicationException ae){
//			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
//			assertEquals("1110560", ae.getErrorCode());
//		}
//	}
//	@Test
//	public void testRemoveManualLetterRequest() throws ApplicationException{
//		int ban=6016913;
//		int reqNum=2;
//		try{
//			managerImpl.removeManualLetterRequest(ban, reqNum, sessionId);
//		}catch(ApplicationException ae){
//			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
//			assertEquals("1114510", ae.getErrorCode());
//		}
//	}
	@Test
	public void testRetrieveFutureStatusChangeRequest() throws ApplicationException,RemoteException{

		int ban = 6016913;
		try{
			List<FutureStatusChangeRequestInfo> futStatAr = new ArrayList<FutureStatusChangeRequestInfo>();
			futStatAr= managerImpl.retrieveFutureStatusChangeRequests(ban, sessionId);
			assertEquals(0,futStatAr.size());
		}catch(ApplicationException ae){
		}
	}

	@Test
	public void testRetrieveFeeWaiver() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {

		int ban=6016913;
		try{
			List<FeeWaiverInfo> feeWaInf = new ArrayList<FeeWaiverInfo>();
			feeWaInf=managerImpl.retrieveFeeWaivers(ban, sessionId);
			assertEquals(0,feeWaInf.size());
		}catch (ApplicationException ae) {
		}
	}


	@Test
	public void testUpdateCreditClass() throws ApplicationException,RemoteException {

		int ban = 6016913;
		String newCreditClass="Test";
		String memoText ="memoTest"	;
		try{
			managerImpl.updateCreditClass(ban, newCreditClass, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("-1", ae.getErrorCode());
		}
	}
	@Test
	public void testUpdateTransferPayment() throws ApplicationException,RemoteException {

		int ban = 6016913;
		String memoText ="memoTest"	;
		PaymentTransfer[] paymentTransfer = new PaymentTransfer[0];
		try{
			managerImpl.updateTransferPayment(ban, 2, paymentTransfer, true, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode());
		}
	}


	@Test
	public void testUpdateAccount() throws ApplicationException {
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
	public void testUpdateAccountWithBlock() throws Exception {
		System.out.println("testUpdateAccount START");

		int ban=6016913;
		PostpaidBusinessRegularAccountInfo accInfo = (PostpaidBusinessRegularAccountInfo) helperImpl.retrieveAccountByBan(ban);
		System.out.println("Before: " + accInfo.toString());

		accInfo.setEmail("newid4@telusmobility.com");
		accInfo.setPin("9999");
		//accInfo.setBillCycle(16);
		//accInfo.getName0().setFirstName("diulaylomay");
		accInfo.getAddress0().setPrimaryLine("123 HAUNTED HOUSE");
		accInfo.getPersonalCreditInformation0().getCreditCard0().setExpiryMonth(5);
		//accInfo.getPersonalCreditInformation0().setDriversLicense("974397537495354");
		

		System.out.println("BAN ID to update: "+accInfo.getBanId());
		managerImpl.updateAccount(accInfo, sessionId);
		
		accInfo = (PostpaidBusinessRegularAccountInfo) helperImpl.retrieveAccountByBan(ban);
		System.out.println("After: " + accInfo.toString());
 
		System.out.println("testUpdateAccount END"); 
	}
	
	@Test
	public void testUpdateBusinessAccountWithBlock() throws Exception {
		System.out.println("testUpdateAccount START");

		int ban=6024633;
		PostpaidBusinessRegularAccountInfo accInfo = (PostpaidBusinessRegularAccountInfo) helperImpl.retrieveAccountByBan(ban);
		System.out.println("Before: " + accInfo.toString());

		accInfo.setEmail("newid4@telusmobility.com");
		accInfo.setPin("9999");
		accInfo.setBillCycle(30);
		accInfo.getAddress0().setPrimaryLine("123 HAUNTED HOUSE");
		accInfo.getPersonalCreditInformation0().getCreditCard0().setExpiryMonth(5);
		//accInfo.getPersonalCreditInformation0().setDriversLicense("974397537495354");

		System.out.println("BAN ID to update: "+accInfo.getBanId());
		managerImpl.updateAccount(accInfo, sessionId);
		
		accInfo = (PostpaidBusinessRegularAccountInfo) helperImpl.retrieveAccountByBan(ban);
		System.out.println("After: " + accInfo.toString());
 
		System.out.println("testUpdateAccount END"); 
	}


	@Test
	public void testSuspendSubscribers() throws ApplicationException {
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
	public void testreverseCredit() throws RemoteException{

		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(6007917);
		String reversalReasonCode = "";
		String memoText="";

		try{
			managerImpl.reverseCredit(creditInfo, reversalReasonCode, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode()); 
		}

		try{
			managerImpl.reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode()); 
		}
	}

	@Test
	public void testdeleteCharge() throws RemoteException{

		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(6007917);
		String deletionReasonCode="";
		String memoText="";

		try{
			managerImpl.deleteCharge(chargeInfo, deletionReasonCode, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode()); 
		}

		try{
			managerImpl.deleteChargeWithOverride(chargeInfo, deletionReasonCode, memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode()); 
		}
	}

	@Test
	public void testapplyChargeToAccount() throws RemoteException{

		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(6007917);

		try{
			managerImpl.applyChargeToAccount(chargeInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1112240", ae.getErrorCode()); 
		}

		try{
			managerImpl.applyChargeToAccountWithOverride(chargeInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1112240", ae.getErrorCode()); 
		}
	}
	@Test
	public void testAdjustCharge() throws ApplicationException,RemoteException {

		int ban = 6007917;
		double adjustmentAmount=32.2;
		String memoText ="memoTest"	;
		ChargeInfo chInfo= new ChargeInfo();
		chInfo.setAmount(12.12);
		chInfo.setBan(ban);
		try{
			managerImpl.adjustCharge(chInfo, adjustmentAmount, "as",memoText,sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode());
		}
	}
	@Test
	public void testAdjustChargeWithOverride() throws ApplicationException,RemoteException {

		int ban = 6007917;
		double adjustmentAmount=32.2;
		String memoText ="memoTest"	;
		ChargeInfo chInfo= new ChargeInfo();
		chInfo.setAmount(12.12);
		chInfo.setBan(ban);
		try{
			managerImpl.adjustChargeWithOverride(chInfo, adjustmentAmount, "", memoText, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110230", ae.getErrorCode());
		}
	}

	@Test
	public void testRestoreSuspendedSubscribers() throws ApplicationException {

		int ban = 197806;
		Date restoreDate = new Date();
		String restoreReasonCode = "aaa"; //PVR
		String[] subscriberId = new String[1];
		subscriberId[0]="4037109656";
		String restoreComment = "restore";

		try{
			managerImpl.restoreSuspendedSubscribers(ban, restoreDate, restoreReasonCode, subscriberId, restoreComment, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115140", ae.getErrorCode()); //invalid activity reason code
			//			assertEquals("-1", ae.getErrorId()); // Tuxedo Service Failed
		}

	}

	@Test
	public void testperformPostAccountCreationPrepaidTasks() throws ApplicationException,RemoteException {

		int ban = 4036958;
		AuditHeader auditHeader=new AuditHeaderInfo();
		auditHeader.setCustomerId("4036958");
		auditHeader.setUserIPAddress("127.0.0.1");
		PrepaidConsumerAccountInfo prepaidConsumerAccountInfo = new PrepaidConsumerAccountInfo();
		prepaidConsumerAccountInfo.setAccountType('T');
		try{

			managerImpl.performPostAccountCreationPrepaidTasks(ban, prepaidConsumerAccountInfo, auditHeader, sessionId);
		}catch(ApplicationException ae){ae.printStackTrace();
		assertEquals(SystemCodes.CMB_ALM_EJB, ae.getSystemCode());
		assertEquals("VAL10003", ae.getErrorCode());
		}
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
	public void testRefundPaymentToAccount() throws ApplicationException,RemoteException {

		int ban = 6000055;
		int paymentSeq=32;
		String memoText ="memoTest"	;
		boolean isManual = false;
		String reasonCode="asd";
		String authorizationCode="test";
		try{
			managerImpl.refundPaymentToAccount(ban, paymentSeq, reasonCode, memoText, isManual, authorizationCode, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("SYS00015", ae.getErrorCode());
		}
	}	
	@Test
	public void testApplyDiscountToAccount() throws ApplicationException,RemoteException {

		int ban =70039848;
		DiscountInfo discountInfo = new DiscountInfo();
		discountInfo.setBan(ban);
		discountInfo.setSubscriberId("4037010034");
		try{
			managerImpl.applyDiscountToAccount(discountInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110360", ae.getErrorCode());
		}
		try{
			discountInfo.setSubscriberId("");
			managerImpl.applyDiscountToAccount(discountInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1110360", ae.getErrorCode());
		}
	}
	@Test
	public void testApplyCreditToAccount() throws ApplicationException,RemoteException {

		int ban = 70039848;
		CreditInfo creditInfo= new CreditInfo();
		creditInfo.setBan(ban);
		creditInfo.setSubscriberId("4037010034");
		creditInfo.setProductType("I");

		try{
			managerImpl.applyCreditToAccount(creditInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115020", ae.getErrorCode());
		}
		creditInfo.setSubscriberId("");
		creditInfo.setProductType("D");
		try{
			managerImpl.applyCreditToAccount(creditInfo, sessionId);
		}catch(ApplicationException ae){
		}
	}

	@Test
	public void testApplyCreditToAccountWithOverride() throws ApplicationException,RemoteException {

		int ban = 70039848;
		CreditInfo creditInfo= new CreditInfo();
		creditInfo.setBan(ban);
		creditInfo.setSubscriberId("4037010034");
		creditInfo.setProductType("D");
		try{
			managerImpl.applyCreditToAccountWithOverride(creditInfo, sessionId);
		}catch(ApplicationException ae){
			assertEquals(SystemCodes.AMDOCS, ae.getSystemCode());
			assertEquals("1115020", ae.getErrorCode());
		}
		creditInfo.setBan(70039848);
		creditInfo.setSubscriberId("");
		creditInfo.setProductType("I");
		try{
			managerImpl.applyCreditToAccountWithOverride(creditInfo, sessionId);
		}catch(ApplicationException ae){
		}
	}

	@Test
	public void prepaidAdjustBalanceCreditInfo() throws ApplicationException  {		
		managerImpl.adjustBalanceForPayAndTalkSubscriber("IntegTest", creditInfo);
	}	

	@Test(expected = SystemException.class)
	public void prepaidAdjustBalanceNewCreditInfo() throws ApplicationException  {
		managerImpl.adjustBalanceForPayAndTalkSubscriber("IntegTest", new CreditInfo());
	}

	@Test(expected = SystemException.class)
	public void prepaidAdjustBalanceNewChargeInfo() throws ApplicationException {
		managerImpl.adjustBalanceForPayAndTalkSubscriber("IntegTest", new ChargeInfo());
	}

	@Test
	public void prepaidAdjustBalanceChargeInfo() throws ApplicationException {		
		managerImpl.adjustBalanceForPayAndTalkSubscriber("IntegTest", chargeInfo);
	}

	@Test
	public void prepaidAdjustBalanceParamsNoPST() throws ApplicationException {
		managerImpl.adjustBalanceForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, "IntegTest", creditInfo.getAmount(),
				creditInfo.getReasonCode(), "IntegTestTrxCode", new TaxSummaryInfo(),
				CreditInfo.TAX_OPTION_ALL_TAXES);
	}

	@Test
	public void prepaidAdjustBalanceParamsWithPST() throws ApplicationException {
		TaxSummaryInfo pTax = new TaxSummaryInfo(); 
		pTax.setPSTAmount(0.02);

		managerImpl.adjustBalanceForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, "IntegTest", creditInfo.getAmount(),
				creditInfo.getReasonCode(), "IntegTestTrxCode", pTax,
				CreditInfo.TAX_OPTION_ALL_TAXES);
	}

	@Test
	public void prepaidAdjustBalanceParamsAllTax() throws ApplicationException {
		TaxSummaryInfo pTax = new TaxSummaryInfo(); 
		pTax.setPSTAmount(0.02);
		pTax.setGSTAmount(0.03);
		pTax.setHSTAmount(0.04);

		managerImpl.adjustBalanceForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, "IntegTest", creditInfo.getAmount(),
				creditInfo.getReasonCode(), "IntegTestTrxCode", pTax);
	}

	@Test
	public void prepaidAdjustBalanceParamsNoTax() throws ApplicationException {
		managerImpl.adjustBalanceForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, "IntegTest", creditInfo.getAmount(),
				creditInfo.getReasonCode(), "IntegTestTrxCode");		
	}


	@Test
	@Ignore
	// This test can only be run when the airtime cards are active.
	// After running once, the status of the cards needs to be manually
	// updated from "used" to "active" again
	public void prepaidApplyTopUpAirTimeCard() throws ApplicationException {
		CardInfo pAirtimeCard = new CardInfo();
		pAirtimeCard.setSerialNumber("030001151234");
		//pAirtimeCard.setPIN(PIN);
		//		pAirtimeCard.setSerialNumber("030001591234");
		//		pAirtimeCard.setSerialNumber("030001371234");
		//		pAirtimeCard.setSerialNumber("030001261234");
		//		pAirtimeCard.setSerialNumber("030001711234");

		managerImpl.applyTopUpForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, pAirtimeCard, "IntegTest",
		"IntegTest");		
	}


	@Test
	@Ignore
	// verified that this method is making correct call to prepaidAPI
	// but at the time of running, prepaid system threw 
	// "can't call Card manager EJB remote method!"	
	public void prepaidApplyTopUpCreditCard() throws ApplicationException {
		managerImpl.applyTopUpForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, creditInfo.getAmount(), null,
				"IntegTest", "IntegTest");
	}

	@Test
	@Ignore
	// verified that this method is making correct call to prepaidAPI
	// but at the time of running, prepaid system threw 
	// "can't call Card manager EJB remote method!"
	public void prepaidApplyTopUpDebitCard() throws ApplicationException {
		managerImpl.applyTopUpForPayAndTalkSubscriber(prepaidBAN,
				prepaidSub, creditInfo.getAmount(), "abc",
				"IntegTest", "IntegTest");		
	}

	@Test
	public void testapplyCreditForFeatureCard() throws ApplicationException{
		CardInfo cardInfo=new CardInfo();
		cardInfo.setAdjustmentCode("10");
		ServiceInfo[] cardServices=new ServiceInfo[0];
		String userId = "integTest";

		managerImpl.applyCreditForFeatureCard(cardInfo, cardServices, userId);
	}


	@Test
	public void testSaveActivationTopUpArrangement() throws ApplicationException {
		String MDN=prepaidSub;
		String serialNo="13806512422";
		String user="integTest";

		ActivationTopUpPaymentArrangementInfo topUpArrangement=new ActivationTopUpPaymentArrangementInfo();
		topUpArrangement.setPreRegisteredCard(true);
		topUpArrangement.setChargeAmount(10.0);
		topUpArrangement.setThresholdAmount(20.00);

		managerImpl.saveActivationTopUpArrangement(""+prepaidBAN, MDN, serialNo, topUpArrangement, user);


	}
	@Test
	public void testSaveEBillRegistrationReminder()throws ApplicationException{
		try{
			managerImpl.saveEBillRegistrationReminder(89, "2121211212", "2121211212", "HS");
		}catch(UncategorizedSQLException unCatEx)
		{
			unCatEx.printStackTrace();
		}
	}

	@Test
	public void testUpdateAccountPIN() throws ApplicationException{
		try{
			managerImpl.updateAccountPIN(70039848, prepaidSub, "13806512422", "123", "312", "12");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testSaveBillNotificationDetails()throws ApplicationException{			
		try{
			int ban=6006430;
			long portalUserID=323212;
			BillNotificationContactInfo[] billNotAr = new BillNotificationContactInfo[1];
			BillNotificationContactInfo billNotificatioCont = new BillNotificationContactInfo();
			billNotificatioCont.setBillNotificationType("EPOST");
			billNotificatioCont.setNotificationAddress("asasdasasas");
			billNotificatioCont.setContactType("5");
			billNotAr[0]=billNotificatioCont;
			String applicationCode="test";
			managerImpl.saveBillNotificationDetails(ban,portalUserID, billNotAr, applicationCode);
		}catch(UncategorizedSQLException unCatEx)
		{
			unCatEx.printStackTrace();
		}
	}

	@Test
	public void testUpdateTopupCreditCard() throws RemoteException{
		String MDN=prepaidSub;
		String serialNo="13806512422";
		String user="integTest";
		boolean encrypted=false;
		CreditCardInfo creditCard=new CreditCardInfo();
		creditCard.setToken("123");
		creditCard.setTrailingDisplayDigits("456");
		creditCard.setLeadingDisplayDigits("789");
		creditCard.setExpiryMonth(11);
		creditCard.setExpiryYear(2020);
		String ban="70039848";
		
		managerImpl.updateTopupCreditCard(ban, MDN, serialNo, creditCard, user, encrypted);
	}
	
	@Test
	public void testUpdateTopupCreditCard1() throws RemoteException{
		String MDN=prepaidSub;
		String user="integTest";
		boolean encrypted=false;
		CreditCardInfo creditCard=new CreditCardInfo();
		creditCard.setToken("123");
		creditCard.setTrailingDisplayDigits("456");
		creditCard.setLeadingDisplayDigits("789");
		creditCard.setExpiryMonth(11);
		creditCard.setExpiryYear(2020);
		String ban="70039848";
		
		managerImpl.updateTopupCreditCard(ban, MDN, creditCard, user, encrypted);
	}
	
	@Test
	public void testUpdateAutoTopUp() throws ApplicationException{

		AutoTopUpInfo autoTopUpInfo = new AutoTopUpInfo();
		int banId=6004473;
		autoTopUpInfo.setBan(banId);
		autoTopUpInfo.setChargeAmount(12);
		String userId = "integTest";
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = false;
		
		managerImpl.updateAutoTopUp(autoTopUpInfo, userId, existingAutoTopUp, existingThresholdRecharge);
	}
	
	@Test
	public void testRetrieveLastCreditCheckResultByBan() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException{
		
		int ban = 6004473;
		String productType="I";
		CreditCheckResultInfo ccrinfo = managerImpl.retrieveLastCreditCheckResultByBan(ban, productType,sessionId);

		assertEquals("D", ccrinfo.getCreditCheckResultStatus().trim());
	}
	
	@Test
	public void testGetUserProfileID() throws ApplicationException{
		
		String profileId = managerImpl.getUserProfileID(sessionId);

		//assertEquals("", profileId);
	}
	
	@Test
	public void testUpdateBillingInformation() throws ApplicationException {		
		
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
	public void testUpdateContactInformation() throws ApplicationException {		
	/*	
		int billingAccountNumber=81;
		ContactPropertyInfo contactPropertyInfo=new ContactPropertyInfo();
		contactPropertyInfo.setBusinessPhoneNumber("4163333336");
		//contactPropertyInfo.setBusinessPhoneExtension("4563");
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setFirstName("NEW");
		name.setLastName("TEST");
		name.setNameFormat("Deeedde");
		contactPropertyInfo.setName(name);
			managerImpl.updateContactInformation(billingAccountNumber, contactPropertyInfo, sessionId);
			
			assertEquals(contactPropertyInfo.getBusinessPhoneNumber(), helperImpl.getContactInformation(billingAccountNumber).getBusinessPhoneNumber());
	*/		
		
		int billingAccountNumber=20007097;
		ContactPropertyInfo contactPropertyInfo=new ContactPropertyInfo();
		contactPropertyInfo.setHomePhoneNumber("9051111111");
		contactPropertyInfo.setBusinessPhoneNumber("4162222222");
		contactPropertyInfo.setBusinessPhoneExtension("1234");
		contactPropertyInfo.setContactPhoneNumber("9053333333");
		contactPropertyInfo.setContactPhoneExtension("3333");
		contactPropertyInfo.setContactFax("9054444444");
		contactPropertyInfo.setOtherPhoneType("MOBILE");
		contactPropertyInfo.setOtherPhoneNumber("6475555555");
		contactPropertyInfo.setOtherPhoneExtension("4321");
		ConsumerNameInfo name = new ConsumerNameInfo();
		name.setTitle("MR.");
		name.setFirstName("R");
		name.setLastName("Caaaaaa");
		name.setMiddleInitial("T");
		name.setNameFormat("P");
		contactPropertyInfo.setName(name);
			managerImpl.updateContactInformation(billingAccountNumber, contactPropertyInfo, sessionId);
			
			assertEquals(contactPropertyInfo.getBusinessPhoneNumber(), helperImpl.retrieveContactInformation(billingAccountNumber).getBusinessPhoneNumber());
	}
	
	@Test
	public void testUpdatePersonalCreditInformation() throws ApplicationException{
		int ban=20578291;//6004473;
		PersonalCreditInfo personalCreditInfo=new PersonalCreditInfo();
		personalCreditInfo.setBirthDate(new Date(1971-1900,8-1,25));
		personalCreditInfo.setDriversLicense("1234567890");
		personalCreditInfo.setDriversLicenseExpiry(new Date());
		personalCreditInfo.setDriversLicenseProvince("ON");
		personalCreditInfo.setSin("209714948");
		CreditCardInfo cci=(CreditCardInfo)personalCreditInfo.getCreditCard();
		cci.setToken("100053869436836945862");
		cci.setLeadingDisplayDigits("490000");
		cci.setTrailingDisplayDigits("0333");
		cci.setExpiryMonth(11);
		cci.setExpiryYear(2014);
		
		managerImpl.updatePersonalCreditInformation(ban, personalCreditInfo, sessionId);
		
		assertEquals(personalCreditInfo.getDriversLicense(), helperImpl.retrievePersonalCreditInformation(ban).getDriversLicense());
	}
	
	@Test
	public void testUpdateBusinessCreditInformation() throws ApplicationException{
		int ban=6004473;
		BusinessCreditInfo businessCreditInfo=new BusinessCreditInfo();
		businessCreditInfo.setIncorporationDate(new Date(2011-1900,8-1,25));
		businessCreditInfo.setIncorporationNumber("333");
		
		managerImpl.updateBusinessCreditInformation(ban, businessCreditInfo, sessionId);
		
		assertEquals(businessCreditInfo.getIncorporationNumber(), helperImpl.retrieveBusinessCreditInformation(ban).getIncorporationNumber());

	}
     
	@Test
	public void testSaveActivationTopUpArrangement_PrepaidGemini() throws ApplicationException {

		String MDN = "4160711718";
		String serialNo = "8912230000154930000";
		String user = "integTest";
		String prepaidBAN = "70636923";

		ActivationTopUpPaymentArrangementInfo topUpArrangement = new ActivationTopUpPaymentArrangementInfo();

		topUpArrangement.setPreRegisteredCard(true);
		topUpArrangement.setChargeAmount(25.0);
		topUpArrangement.setThresholdAmount(5.00);
		PreRegisteredPrepaidCreditCardInfo p = new PreRegisteredPrepaidCreditCardInfo();

		p.setHolderName("ZEL");
		p.setToken("100000000000004022762");
		p.setTrailingDisplayDigits("2343");
		p.setExpiryMonth(1);
		p.setExpiryYear(2013);
		p.setThresholdAmount(5);
		p.setRechargeAmount(25);
		p.setTopupAmout(100);
		PaymentCard[] paymentCardList = new PaymentCard[1];
		paymentCardList[0] = p;
		topUpArrangement.setupPaymentCardList(paymentCardList);

		try {
			managerImpl.saveActivationTopUpArrangement(prepaidBAN, MDN,
					serialNo, topUpArrangement, user);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// TOWN EJB TEST CASES

	@Test
	public void testcreateCreditBalanceTransferRequest() throws ApplicationException {
		System.out.println(" start testcreateCreditBalanceTransferRequest");
		int sourceBan = 20580140;
		int targetBan = 20070051;
		managerImpl.createCreditBalanceTransferRequest(sourceBan, targetBan ,sessionId);
		System.out.println(" start testcreateCreditBalanceTransferRequest");
	}

	@Test
	public void testcancelCreditBalanceTransferRequest()throws ApplicationException {
		System.out.println(" start testcancelCreditBalanceTransferRequest");
		int creditTransferSequenceNumber = 20580140;
		managerImpl.cancelCreditBalanceTransferRequest(creditTransferSequenceNumber, sessionId);
		System.out.println(" start testcancelCreditBalanceTransferRequest");
	}

	@Test
	public void testgetCreditBalanceTransferRequestList()throws ApplicationException {
		System.out.println(" start testgetCreditBalanceTransferRequestList");
		int ban = 70639988;
		managerImpl.getCreditBalanceTransferRequestList(ban, sessionId);
		System.out.println(" start testgetCreditBalanceTransferRequestList");
	}
	
	@Test
	public void testAdjustChargeToAccountForSubscriberReturn() throws Throwable {
		int ban = 70543769;
		String subscriberNumber = "4031650935";
		String phoneNumber = "4031650935";
		List<ChargeAdjustmentCodeInfo> chargeInfoList = new ArrayList<ChargeAdjustmentCodeInfo>(); 
		Date searchFromDate = new Date(2012-1900, 0, 1);
		Date searchToDate = new Date (2013-1900, 0, 20);
		String adjustmentMemoText = "";
		String productType = "C";
		boolean bypassAuthorizationInd = false;
		boolean overrideThresholdInd = true;
		
		ChargeAdjustmentCodeInfo chargeAdjustmentCodeInfo = new ChargeAdjustmentCodeInfo();
		chargeAdjustmentCodeInfo.setAdjustmentReasonCode("DBLBR");
		chargeAdjustmentCodeInfo.setChargeCode("DPFEE");
		chargeInfoList.add(chargeAdjustmentCodeInfo);
		
		List<ChargeAdjustmentInfo> result = managerImpl.adjustChargeToAccountForSubscriberReturn(ban, subscriberNumber, phoneNumber, chargeInfoList, 
				searchFromDate, searchToDate, adjustmentMemoText, productType, bypassAuthorizationInd, overrideThresholdInd, sessionId);
		
		for (ChargeAdjustmentInfo info : result) {
			System.out.println(info);
		}
	}

	@Test
	public void testUpdateCustomerNotificationPreferenceList() throws Throwable {
		//int ban=70104002;//D3 prepaid;
		int ban=70679462;//PT168 prepaid
		/*
		 * CPUI_CODES
		 * "1", "No Direct Mail"
		 * "2", "No Restriction Default"
		 * "3", "No Email"
		 * "4", "No Telemarketing"
		 * "5", "No Market Research"
		 * "6", "No Virtual Agent"
		 * "7", "No SMS"
		 * "8", "Future use - Do not use"
		 * 
		 * UpdateModeCode
		 * "I", "Insert"
		 * "D", "Delete"
		 */
		List<CustomerNotificationPreferenceInfo> notificationPreferenceList = managerImpl.getCustomerNotificationPreferenceList(ban, sessionId);
		List<CustomerNotificationPreferenceInfo> updatedNotificationPreferenceList;
		CustomerNotificationPreferenceInfo info1;
		CustomerNotificationPreferenceInfo info2;
		//remove all existing preferences
		if (notificationPreferenceList!=null && notificationPreferenceList.size()>0) {
			updatedNotificationPreferenceList = new ArrayList<CustomerNotificationPreferenceInfo>(); 
			for (CustomerNotificationPreferenceInfo notificationPreference:notificationPreferenceList) {
				notificationPreference.setUpdateModeCode("D");
				updatedNotificationPreferenceList.add(notificationPreference);
			}
			//delete all
			managerImpl.updateCustomerNotificationPreferenceList(ban, updatedNotificationPreferenceList, sessionId);
			
			//verify it is empty
			notificationPreferenceList = managerImpl.getCustomerNotificationPreferenceList(ban, sessionId);
			assertTrue(notificationPreferenceList.size()==0);

		}
		
		//deleting non existing one
		updatedNotificationPreferenceList = new ArrayList<CustomerNotificationPreferenceInfo>();
		info1 = new CustomerNotificationPreferenceInfo();
		info1.setCode("4");
		info1.setUpdateModeCode("D");
		managerImpl.updateCustomerNotificationPreferenceList(ban, updatedNotificationPreferenceList, sessionId);
		
		//adding new ones
		String code1 = "3";
		String code2 = "6";
		updatedNotificationPreferenceList = new ArrayList<CustomerNotificationPreferenceInfo>();
		info1 = new CustomerNotificationPreferenceInfo();
		info1.setCode(code1);
		info1.setUpdateModeCode("I");
		updatedNotificationPreferenceList.add(info1);
		info2 = new CustomerNotificationPreferenceInfo();
		info2.setCode(code2);
		info2.setUpdateModeCode("I");
		updatedNotificationPreferenceList.add(info2);
		managerImpl.updateCustomerNotificationPreferenceList(ban, updatedNotificationPreferenceList, sessionId);
		notificationPreferenceList = managerImpl.getCustomerNotificationPreferenceList(ban, sessionId);
		assertTrue(notificationPreferenceList.size() == 2);
		
		if (notificationPreferenceList!=null && notificationPreferenceList.size()>=1) {
			for (CustomerNotificationPreferenceInfo info:notificationPreferenceList) {
				assertTrue(info.getCode().equals(code1) || info.getCode().equals(code2));
				System.out.println("Code: " + info.getCode());
				System.out.println("Desc: " + info.getDescription());
			}
		}
		
	}

	@Test
	public void testGetCustomerNotificationPreferenceList() throws Throwable {
		//int ban=70104002;//D3 prepaid
		int ban=70679462;//PT168 prepaid
		System.out.println("BAN: " + ban);
		List<CustomerNotificationPreferenceInfo> notificationPreferenceList = managerImpl.getCustomerNotificationPreferenceList(ban, sessionId);
		assertTrue(notificationPreferenceList.size() >= 1);
		if (notificationPreferenceList!=null && notificationPreferenceList.size()>=1) {
			for (CustomerNotificationPreferenceInfo info:notificationPreferenceList) {
				System.out.println("Code: " + info.getCode());
				System.out.println("Desc: " + info.getDescription());
			}
		}
		
		
	}

}

