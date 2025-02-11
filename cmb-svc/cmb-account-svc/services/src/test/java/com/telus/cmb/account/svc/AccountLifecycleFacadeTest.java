/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.svc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.api.account.Account;
import com.telus.api.account.BankAccount;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacadeTestPoint;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.PaymentArrangementEligibilityResponseInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PaymentNotificationResponseInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.PairingGroupInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations = "classpath:application-context-test.xml")
//@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "lab"})
//@ActiveProfiles({ "remote", "pt140" })
//@ActiveProfiles({"remote", "pt168"})
//@ActiveProfiles({"remote", "pt140"})
@ActiveProfiles({"remote", "local"})
public class AccountLifecycleFacadeTest extends AbstractTestNGSpringContextTests {

	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("cdrNotificationGCCMigrationRollback", "true");
		
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");		
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("weblogic.security.SSL.enableJSSE", "true");
	}

	@Autowired
	private AccountLifecycleFacade facade;
	
	@Autowired
	private AccountInformationHelper helper;
	
	@Autowired
	private AccountLifecycleFacadeTestPoint testPoint;
	
	
	@Test
	public void asyncCreateMemo() throws Exception {
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		MemoInfo memoInfo = new MemoInfo();
		memoInfo.setBanId(70593092);
		memoInfo.setSubscriberId("6471213215");
		memoInfo.setMemoType("FYI");
		memoInfo.setProductType("C");
		memoInfo.setDate(new Date());
		memoInfo.setText("async memo creation test.");
		
		facade.asyncCreateMemo(memoInfo, sessionId);
		
		System.out.println("asyncCreateMemo End ");
	}
	
	@Test
	public void validateAddress() throws Exception {
		
		AddressInfo address = new AddressInfo();
		
		address.setAddressType("D");
		address.setCountry("CAN");
		address.setCity("ST.JOHN'S");
		address.setProvince("NF");
		address.setPostalCode("A1B2X2");
		address.setStreetName("Rowan St");
		address.setStreetNumber("31");
		address.setStreetType("ST");
		
		AddressValidationResultInfo result = facade.validateAddress(address);
		
		System.out.println(result);
	}
	
	@Test
	public void validateCreditCard() throws Exception { 
		
		int ban = 70714874;
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
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

		CreditCardResponseInfo authorizationCode = facade.validateCreditCard(ccti, sessionId);
		
		System.out.println("authorizationCode = " + authorizationCode.getAuthorizationCode()); 
		System.out.println("testValidateCreditCard END"); 
	}
	
	@Test
	public void checkNewSubscriberEligibility() throws Exception {
		
		AccountInfo account = helper.retrieveAccountByBan(70413002, Account.ACCOUNT_LOAD_ALL);		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");		
		facade.checkNewSubscriberEligibility(account, 1, 10, sessionId);
	}
	
	@Test
	public void testEnterpriseConsumerProfileRegistrationService() throws Exception {
		
		System.out.println("start testEnterpriseConsumerProfileRegistrationService");
		TestPointResultInfo testPointInfo = testPoint.testEnterpriseConsumerProfileRegistrationService();
		System.out.println(testPointInfo);
		System.out.println("end testEnterpriseConsumerProfileRegistrationService");
	}
	
	@Test
	public void createAccount() throws Exception {
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");		
		
		PostpaidConsumerAccountInfo account = PostpaidConsumerAccountInfo.getNewInstance('I');		
		account.setBanId(8);
		account.setAccountType('I');
		account.setAccountSubType('R');
		account.setBrandId(1);
		account.getContactName().setFirstName("FirstName");
		account.getContactName().setLastName("LastName");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate(new Date());
		account.setGSTCertificateNumber("1234567890");
		account.setGstExempt((byte) 'Y');
		account.setGSTExemptEffectiveDate(new Date());

		facade.createAccount(account, sessionId);
	}

	@Test
	public void processPayment() throws Exception {

		System.out.println("Start processPayment...");

		int ban = 8;
		double paymentAmount = 20;

		AccountInfo accountInfo = helper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		
		CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		creditCardTransactionInfo.setAmount(1.0);
		AuditHeaderInfo auditHeader = new AuditHeaderInfo();
		auditHeader.setCustomerId("2222");
		auditHeader.setUserIPAddress("172.23.14.53");
		creditCardTransactionInfo.setAuditHeader(auditHeader);
		creditCardTransactionInfo.setBan(ban);
		creditCardTransactionInfo.setBrandId(1);
		creditCardTransactionInfo.setChargeAuthorizationNumber("123");
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
		creditCardTransactionInfo.setCreditCardHolderInfo(creditCardHolderInfo);
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		creditCardInfo.setExpiryMonth(12);
		creditCardInfo.setExpiryYear(2015);
		creditCardInfo.setHolderName("Danny ¿‡¬‚«Á…È»");
		creditCardInfo.setLeadingDisplayDigits("341000");
		creditCardInfo.setToken("100000000000000000368");
		creditCardInfo.setTrailingDisplayDigits("0008");
		creditCardInfo.setType("VS");
		creditCardInfo.setAuthorizationCode("123");
		creditCardTransactionInfo.setCreditCardInfo(creditCardInfo);
		creditCardTransactionInfo.setTransactionType("CCV");

		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		
		String authNumber = facade.processPayment(paymentAmount, accountInfo, creditCardTransactionInfo, "", "", false, auditInfo, sessionId);
		System.out.println("Payment authorization number = [" + authNumber + "].");
		
		System.out.println("End processPayment.");
	}
	
	@Test
	public void adjustCharge() throws Exception {

		System.out.println("start adjustCharge ");
		int banId = 70870154;
		double chargeSequenceNumber = 2.094150377E9;
		double adjustmentAmount = 2.1;
		//String subscriberId = "6041661146";
		String adjustmentReasonCode = "BCPYCR";
		
		ChargeInfo chargeInfo = new ChargeInfo();
		chargeInfo.setBan(banId);
		chargeInfo.setId(chargeSequenceNumber);
		//chargeInfo.setSubscriberId(subscriberId); 
		chargeInfo.setEffectiveDate(new Date());

		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		facade.adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, "test notification", true, false, auditInfo, sessionId);
		
		System.out.println("end adjustCharge ");
	}
	
	@Test
	public void applyCreditToAccount() throws Exception {

		System.out.println("Start applyCreditToAccount...");

		int banId = 70870154; // 70761041
		//String subscriberId = "4161616889";
		
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(banId);
	//	creditInfo.setSubscriberId(subscriberId); // just comment out this if we would need to test BalanceAdjustment Ban level transactions
		creditInfo.setAmount(11);
	
		creditInfo.setEffectiveDate(new Date());
		creditInfo.setReasonCode("ACSSC");
		
		// set the tax amounts..
		creditInfo.getTaxSummary().setGSTAmount(17);
		creditInfo.getTaxSummary().setPSTAmount(13);
		creditInfo.getTaxSummary().setHSTAmount(6);
		
		//set Recurring flags
		creditInfo.setRecurring(false);
		creditInfo.setNumberOfRecurring(1);
		
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		facade.applyCreditToAccount(creditInfo, false, false, auditInfo, sessionId);
		
		System.out.println("End applyCreditToAccount.");
	}
	
	@Test
	public void updateFollowUp() throws Exception {

		System.out.println("start updateFollowUp ");

		int banId = 70811616;
		int followUpId = 11791952;
		FollowUpUpdateInfo followUpUpdateInfo = new FollowUpUpdateInfo();
		followUpUpdateInfo.setBan(banId);
		followUpUpdateInfo.setFollowUpId(followUpId);
		followUpUpdateInfo.setIsApproved(true);
		//followUpUpdateInfo.setSubscriberId("4038948286");

		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		facade.updateFollowUp(followUpUpdateInfo, sessionId);
		
		System.out.println("end updateFollowUp ");
	}
	
	@Test
	public void applyPaymentToAccount() throws Exception {

		System.out.println("start applyPaymentToAccount ");

		int ban = 70804610;
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBan(ban);
		paymentInfo.setAmount(3);
		
		paymentInfo.setPaymentMethod(TelusConstants.PAYMENT_METHOD_CREDIT_CARD);
		paymentInfo.setDepositPaymentIndicator(false);
		paymentInfo.setPaymentSourceID("ONLINE");
		paymentInfo.setPaymentSourceType("O");
		//paymentInfo.setAuthorizationNumber("1111");
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		// credit card
		creditCardInfo.setToken("100000000000004120388");
		creditCardInfo.setTrailingDisplayDigits("0007");
		creditCardInfo.setLeadingDisplayDigits("450228");
		creditCardInfo.setExpiryMonth(11);
		creditCardInfo.setExpiryYear(2023);
		creditCardInfo.setHolderName("YGAKJHSKJSHJKH");
	    creditCardInfo.setAuthorizationCode("195057");
	    
		paymentInfo.setCreditCardInfo(creditCardInfo);
		
		// cheque
		
		//paymentInfo.getChequeInfo().setChequeNumber(chequeNumber);
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		accountInfo.setEmail("naresh.annabathula@telus.com");
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		facade.applyPaymentToAccount(accountInfo, paymentInfo, false, auditInfo, sessionId);
		
		System.out.println("end applyPaymentToAccount ");
	}
	
	@Test
	public void updateCreditWorthiness() throws Exception {

		System.out.println("Start updateCreditWorthiness...");

		AccountInfo account = helper.retrieveAccountByBan(70815407, Account.ACCOUNT_LOAD_ALL);

		CreditCheckResultDepositInfo deposit = new CreditCheckResultDepositInfo();
		deposit.setProductType(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR);
		deposit.setDeposit(200);

		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		try {
			facade.updateCreditWorthiness(account.getBanId(), "NDP", "C", 50.00, "CAPI test: change to credit class C.", true, new CreditCheckResultDepositInfo[] { deposit }, "103",
					"CAPI test: change deposit to $200.", true, false, null, null, sessionId);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println("End updateCreditWorthiness.");
	}
	
	@Test
	public void updateCreditProfile() throws Exception {

		System.out.println("Start updateCreditProfile...");

		AccountInfo account = helper.retrieveAccountByBan(70576393, Account.ACCOUNT_LOAD_ALL);
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		try {
			facade.updateCreditProfile(account.getBanId(), "B", 0.00, "CAPI test: change to credit class B.", sessionId);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		System.out.println("End updateCreditProfile.");
	}
	
	@Test
	public void checkCredit() throws Exception {

		System.out.println("Start checkCredit...");

		PostpaidConsumerAccountInfo account = (PostpaidConsumerAccountInfo) helper.retrieveAccountByBan(70779652, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		AuditHeaderInfo auditHeader = new AuditHeaderInfo();
		auditHeader.setCustomerId(Integer.toString(account.getBanId()));
		auditHeader.setUserIPAddress("142.174.179.249");
		auditHeader.appendAppInfo("T808756", 4308, "142.63.136.23");
		
		CreditCheckResultInfo info = facade.checkCredit(account, account.getName0(), account.getAddress0(), true, auditHeader, sessionId);
		System.out.println(info.toString());

		System.out.println("End checkCredit.");
	}

	@Test
	public void testCreditCardType() throws Exception {
		//paymentInfo.setAuthorizationNumber("1111");
		CreditCardInfo creditCardInfo = new CreditCardInfo();
		
		// credit card
		creditCardInfo.setToken("100000000000004027611");
		creditCardInfo.setTrailingDisplayDigits("7155");
		creditCardInfo.setLeadingDisplayDigits("450166");
		creditCardInfo.setExpiryMonth(11);
		creditCardInfo.setExpiryYear(2019);
		creditCardInfo.setHolderName("YGAKJHSKJSHJKH");
	    creditCardInfo.setAuthorizationCode("195057");
	    
	    System.out.println(creditCardInfo.getType());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getDuplicateAccountList() throws Exception {

		System.out.println("Start getDuplicateAccountList...");

//		PostpaidConsumerAccountInfo account = (PostpaidConsumerAccountInfo) helper.retrieveAccountByBan(70779652);
//		PostpaidCorporatePersonalAccountInfo account = PostpaidCorporatePersonalAccountInfo.newInstance(PostpaidCorporatePersonalAccountInfo.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL);
//		PostpaidBusinessRegularAccountInfo account = PostpaidBusinessRegularAccountInfo.newPCSInstance();
		PostpaidCorporateRegularAccountInfo account = PostpaidCorporateRegularAccountInfo.newInstance(Account.ACCOUNT_SUBTYPE_CORP_PCS_REGIONAL_STRATEGIC);
		
		account.setBrandId(1);
//		account.getContactName().setFirstName("FirstName");
//		account.getContactName().setLastName("LastName");
//		account.getName().setFirstName("FirstName");
//		account.getName().setLastName("LastName");
//		account.getPersonalCreditInformation().setDriversLicense("1234567");
//		account.getPersonalCreditInformation().setSin("123456789");
//		account.setEmail("testemail@telus.com");
//		account.setLanguage("EN");
//		account.setCreateDate(new Date());
//		account.setGSTCertificateNumber("1234567890");
//		account.setGstExempt((byte) 'Y');
//		account.setGSTExemptEffectiveDate(new Date());
		
		account.setLegalBusinessName(null);
		account.getCreditInformation0().setIncorporationNumber(null);
		
		List<MatchedAccountInfo> list = facade.getDuplicateAccountList(account);
		for (MatchedAccountInfo info : list) {
			System.out.println(info.toString());
		}

		System.out.println("End getDuplicateAccountList.");
	}	
	
	@Test
	public void validateCommunicationSuiteEligibility() throws Exception {
		System.out.println("Start validateCommunicationSuiteEligibility...");
		boolean result = facade.validateCommunicationSuiteEligibility(1, 'I', 'R');
		System.out.println("End validateCommunicationSuiteEligibility, eligiblity result : "+result);
		
		result = facade.validateCommunicationSuiteEligibility(3, 'I', 'R');
		System.out.println("End validateCommunicationSuiteEligibility, eligiblity result : "+result);
		
		result = facade.validateCommunicationSuiteEligibility(1, 'I', 'Q');
		System.out.println("End validateCommunicationSuiteEligibility, eligiblity result : "+result);
	}
	
	@Test
	public void isEnterpriseManagedData() throws Exception {
		System.out.println("Start isEnterpriseManagedData...");
		boolean result = facade.isEnterpriseManagedData(1, 'I', 'R', "test", "test");
		System.out.println("End isEnterpriseManagedData, eligiblity result : "+result);
	}
	
	@Test
	public void cancelSubscribers() throws Exception {
		System.out.println("Start cancelSubscribers...");
	
		int ban = 70886707;
		String activityReasonCode="AIE";
		char pDepositReturnMethod='R';
		String [] subscriberIds = new String[]{"9059995921","9059995922","9059995923"};
		String [] pWaiveReasons = new String[]{"FEW","FEW","FEW"};

		String sessionId = "9C/mZDrSmOGydSbg/xlH2rOhxkXslzpPnY4cxZzRiuU=";//facade.openSession("18654", "apollo", "SMARTDESKTOP");
//		facade.cancelSubscribers(ban, new Date(), activityReasonCode, pDepositReturnMethod, subscriberIds, pWaiveReasons, "test huksy primary cancel", 
//				false, false, null, null, false, null, sessionId);
		System.out.println("End cancelSubscribers");
	}
	
	// ECP 2020 Test cases 
	
	@Test
	public void cancelAccount_Regular() throws Exception {
		System.out.println("begin cancelaccount");
		int ban = 70885306;
		String activityReasonCode="AIE";		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		facade.cancelAccount(ban,null, activityReasonCode, "R", "FEW", "canceling the account", null, false, null, sessionId);
		System.out.println("End cancelAccount..");
	}
	
	@Test
	public void cancelAccount_Husky_Last_Primary_With_Companions() throws Exception {
		System.out.println("begin cancelAccount_Husky_Last_Primary");
		
		int ban = 70835345;
		
		// prepare the commSuiteInfo
		CommunicationSuiteInfo commSuiteInfo = new CommunicationSuiteInfo();
		String lastPrimaryPhoneNumber = "4161471499";
		List<String> companionPhNumList = new ArrayList<String>();
		companionPhNumList.add("7781726979");
		commSuiteInfo.setPrimaryPhoneNumber(lastPrimaryPhoneNumber);
		commSuiteInfo.setRetrievedAsPrimary(true);
		PairingGroupInfo pairingGroup = new PairingGroupInfo();
		pairingGroup.setCompanionPhoneNumberList(companionPhNumList);
		commSuiteInfo.addPairingGroup(pairingGroup);
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		facade.cancelAccountForPortOut(ban, "AIE", null, "Y", true, commSuiteInfo, false, sessionId);
		System.out.println("End cancelAccount_Husky_Last_Primary..");
	}
	
	@Test
	public void cancelAccount_Husky_Last_Primary_WithOut_Companions() throws Exception {
		System.out.println("begin cancelAccount_Husky_Last_Primary");
		
		int ban = 70835345;
		
		// prepare the commSuiteInfo
		CommunicationSuiteInfo commSuiteInfo = new CommunicationSuiteInfo();
		String lastPrimaryPhoneNumber = "4161471499";
		commSuiteInfo.setPrimaryPhoneNumber(lastPrimaryPhoneNumber);
		commSuiteInfo.setRetrievedAsPrimary(true);
		
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		
		facade.cancelAccountForPortOut(ban, "AIE", null, "Y", true, commSuiteInfo, false, sessionId);
		System.out.println("End cancelAccount_Husky_Last_Primary..");
	}
	
	@Test
	public void cancelAccountForPortOutTest() throws Exception {
		System.out.println("begin cancelAccountForPortOutTest...");
		int ban = 70916462; //good for pt140
//		int ban = 70915369;
//		String [] subscriberIds = {"4161272150","4161274697","4161272248"};
//		
//		String [] waiveReasonCodes = {"FEW","FEW","FEW"};

		String activityReasonCode="KOPO";	//koodo to telus
//		String activityReasonCode = "POKO"; //telus to koodo
		Date activityDate = new Date();
		activityDate.setDate(activityDate.getDate()+1);
		CommunicationSuiteInfo commSuiteInfo = null;
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");	

		facade.cancelAccountForPortOut(ban, activityReasonCode, activityDate, "N", false, commSuiteInfo, true, sessionId);
//		facade.cancelAccount(ban, activityDate, activityReasonCode, "R", "FEW", "test", null, true, null, sessionId);
		
		System.out.println("End cancelAccountForPortOutTest..");
	}
	
	@Test
	public void cancelSubscribers_Regular() throws Exception {
		System.out.println("begin cancelSubscribers_Regular...");
		int ban = 70916467;
		String [] subscriberIds = {"4161272150","4161274697","4161272248"};
		
		String [] waiveReasonCodes = {"FEW","FEW","FEW"};

		String activityReasonCode="AIE";	
		Date activityDate = null;
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");	

		facade.cancelSubscribers(ban, activityDate, activityReasonCode, 'R', subscriberIds, waiveReasonCodes, "cancelling the multi sub's", false, 
				false, null, sessionId);
		
		System.out.println("End cancelMultiSub_WithHuskyPrimary..");
	}
	
	@Test
	public void cancelSubscribers_With_Primary_Companions() throws Exception {
		System.out.println("begin cancelSubscribers_With_Primary_Companions...");
		int ban = 70853385;
	
		String [] subscriberIds = {"4161475383","4161475384"};
		String [] waiveReasonCodes = {"FEW","FEW"};

		String activityReasonCode="AIE";	
		Date activityDate = null;
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");	

		facade.cancelSubscribers(ban, activityDate, activityReasonCode, 'R', subscriberIds, waiveReasonCodes, "cancelling the multi sub's", false, 
				false, null, sessionId);
		
		System.out.println("End cancelSubscribers_With_Primary_Companions..");
	}
	
	
	@Test
	public void updatePaymentMethod() throws Exception {
		System.out.println("start updatePaymentMethod");
		
		int pBan = 70804610;

		// set up old payment method as regular
		PaymentMethodInfo paymentMethod=new PaymentMethodInfo();
		//paymentMethod.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		//paymentMethod.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT);
		//paymentMethod.setOldPaymentMethodCardNumber("PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT");
		
		// set up new payment method as credit card
		CreditCardInfo creditCard = new CreditCardInfo();
		//creditCard.setToken("100000000000004120376", "460172", "0933");
		//creditCard.setExpiryYear(2025);

		creditCard.setToken("100000000000004120388", "450228", "0007");
		creditCard.setExpiryYear(2023);

		 
		paymentMethod.setCreditCard0(creditCard);		
		//paymentMethod.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);

		
		ChequeInfo cheque = new ChequeInfo();
		BankAccount ba = new BankAccountInfo();
		ba.setBankAccountNumber("3546787");
		ba.setBankAccountHolder("NANA");
		ba.setBankAccountType(BankAccount.BANK_ACCOUNT_TYPE_INDIVIDUAL);
		ba.setBankBranchNumber("2178");
		ba.setBankCode("005");
		cheque.setBankAccount(ba);
		paymentMethod.setCheque0(cheque); 		
	    paymentMethod.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT);

		paymentMethod.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);

		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");

		facade.updatePaymentMethod(pBan, null, paymentMethod, false, null, sessionId);
		
		System.out.println("end updatePaymentMethod");
	}
	
	@Test
	public void suspendSubscribers() throws Exception {
		System.out.println("begin suspendSubscribers");
		int ban = 70911614;
		String activityReasonCode="AIE";		
		String[] subscriberIds = {"4161937306"};
		String sessionId = facade.openSession("18654", "apollo", "SMARTDESKTOP");
		facade.suspendSubscribers(ban, new Date(), activityReasonCode, subscriberIds, "defect text", sessionId);
		System.out.println("End suspendSubscribers..");
	}
	
	
}
