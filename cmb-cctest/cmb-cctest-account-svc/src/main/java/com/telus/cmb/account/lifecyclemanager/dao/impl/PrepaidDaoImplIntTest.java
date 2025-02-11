package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentCard;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionBalanceMgmtServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionManagementServiceDao;
import com.telus.cmb.common.util.DateUtil;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PreRegisteredPrepaidCreditCardInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.PrepaidDebitCardInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml", 
		"classpath:application-context-dao-lifecyclemanager.xml",
		"classpath:application-context-wsclint-prepaid.xml",
		"classpath:application-context-datasources-informationhelper-testing-pt148.xml" 
		})
public class PrepaidDaoImplIntTest  {

	//@Autowired
	//PrepaidDaoImpl dao;
	@Autowired
	String prepaidSub;
	@Autowired
	Integer prepaidBAN;
	@Autowired
	ActivationTopUpInfo activationTopUpInfo;
	@Autowired
	PrepaidConsumerAccountInfo prepaidConsumerAccountInfo;
	@Autowired
	private PrepaidWirelessCustomerOrderServiceDao pwcosDao;
	@Autowired
	private PrepaidSubscriberServiceDao pssDao;
	@Autowired
	private SubscriptionBalanceMgmtServiceDao sbmsDao;
	@Autowired
	private SubscriptionManagementServiceDao smsDao;

	static String env = "PT168";//D3/PT168
	static int banId;
	static String phoneNumber = "";
	static String esn = "";
	static String appId = "sserve";

	@BeforeClass
	public static void beforeClass() {
		if (env.equals("PT168")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-pt168.tmi.telus.com:589/cn=pt168_81,o=telusconfiguration");
			banId = 70689768;
			phoneNumber = "4031659716";
			esn="8912239900001205089";
		} else if (env.equals("PT148")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
			//TODO update
			banId = 70690824;
			phoneNumber = "4160704331";
		} else if (env.equals("D3")) {
			System.setProperty("com.telusmobility.config.java.naming.provider.url",	"ldap://ldapread-d3.tmi.telus.com:489/cn=development3_81,o=telusconfiguration");
			banId = 70104002;
			phoneNumber = "5198060070";
		}
		System.setProperty("com.telusmobility.config.java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
		System.setProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");

	}

	
//	@Test
//	@Deprecated
//	public void testapplyCreditForFeatureCardDeprecated(){
//		CardInfo cardInfo=new CardInfo();
//		cardInfo.setAdjustmentCode("10");
//		ServiceInfo[] cardServices=new ServiceInfo[0];
//		String userId = "integTest";
//
//		dao.applyCreditForFeatureCard(cardInfo, cardServices, userId);
//	}
//
//
//	/**
//	 * This method is not well tested due to dependency on prepaid system
//	 * @throws Exception
//	 */		
//	@Test
//	@Deprecated
//	public void testSaveActivationTopUpArrangementDeprecated() throws ApplicationException {
//		String MDN=prepaidSub;
//		String serialNo="1234567890";
//		String user="integTest";
//
//		ActivationTopUpArrangement topUpArrangement= new ActivationTopUpArrangement (
//				true, 0.04,10.1, ""+banId, MDN, serialNo);
//
//		dao.saveActivationTopUpArrangement(""+prepaidBAN, MDN, serialNo, topUpArrangement, user);
//
//	}
//	@Test
//	@Deprecated
//	public void testUpdateAccountPINDeprecated() throws ApplicationException{
//		try{
//			dao.updateAccountPIN(70602349, prepaidSub, "1234567890", "123", "312", "12");
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * This method is not well tested due to dependency on prepaid system
//	 * @throws Exception
//	 */	
//	@Test
//	@Deprecated
//	public void testCreditSubscriberMigrationDeprecated() throws Exception {
//		String applicationId = "notImportantAppId";
//		String pUserId = "integTest";
//		ActivationTopUpInfo activationTopUpInfo = this.activationTopUpInfo;
//		String phoneNumber = "1234567890";
//		String esn = "1234567890";
//		String provinceCode = "ON";
//		PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo = prepaidConsumerAccountInfo;			
//
//		try {
//			dao.creditSubscriberMigration(applicationId, pUserId, activationTopUpInfo, phoneNumber, esn, provinceCode, pPrepaidConsumerAccountInfo);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw ex;
//		}		
//	}
//
//	/**
//	 * This method is not well tested due to dependency on prepaid system
//	 * @throws Exception
//	 */		
//	@Test
//	@Deprecated
//	public void testRemoveTopupCreditCardDeprecated() throws Exception {
//		//String MDN = "12345";
//		String MDN = null;
////		try {			
////			dao.removeTopupCreditCard(MDN);
////			fail("null MDN should throw exception");
////		} catch (SystemException ex) {
////			assertEquals(SystemCodes.PREPAID, ex.getSystemCode());
////		}
////
////		MDN = "";
////		try {			
////			dao.removeTopupCreditCard(MDN);
////			fail("null MDN should throw exception");
////		} catch (SystemException ex) {
////			assertEquals(SystemCodes.PREPAID, ex.getSystemCode());
////		}
//
//		MDN = prepaidSub;
//		try {			
//			dao.removeTopupCreditCard(MDN);			
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			fail("Manual test threw: id=PR_PROV_9015; Provisioning ElementAdapter EJB can't create Lucent Corba Object");
//		}		
//	}
//	
//	@Test
//	@Deprecated
//	public void testUpdateTopupCreditCardDeprecated(){
//		String MDN=prepaidSub;
//		String serialNo="1234567890";
//		String user="integTest";
//		boolean encrypted=false;
//		CreditCardInfo creditCard=new CreditCardInfo();
//		creditCard.setToken("100000000000001865670");
//		creditCard.setTrailingDisplayDigits("1111");
//		creditCard.setLeadingDisplayDigits("519111");
//		creditCard.setExpiryMonth(11);
//		creditCard.setExpiryYear(2023);
//
//		dao.updateTopupCreditCard(String.valueOf(banId), MDN, serialNo, creditCard, user, encrypted);
//	}
//	
//	@Test
//	@Deprecated
//	public void testRegisterTopupCreditCardDeprecated(){
//		boolean registerCC=true;
//		String MDN=prepaidSub;
//		//String serialNo="1234567890";
//		String user="integTest";
//		boolean encrypted=false;
//		CreditCardInfo creditCard=new CreditCardInfo();
//		creditCard.setToken("100000000000001865670");
//		creditCard.setTrailingDisplayDigits("1111");
//		creditCard.setLeadingDisplayDigits("519111");
//		creditCard.setExpiryMonth(11);
//		creditCard.setExpiryYear(2021);
//
//		//For surepay:
//		//dao.updateTopupCreditCard(String.valueOf(banId), MDN, creditCard, user, encrypted, registerCC);
//		dao.updateTopupCreditCard(String.valueOf(banId), MDN, creditCard, user, encrypted);
//	}
//	
//	@Test
//	@Deprecated
//	public void testUpdateTopupCreditCard1Deprecated(){
//		String MDN=prepaidSub;
//		String user="integTest";
//		boolean encrypted=false;
//		CreditCardInfo creditCard=new CreditCardInfo();
//		creditCard.setToken("123");
//		creditCard.setTrailingDisplayDigits("456");
//		creditCard.setLeadingDisplayDigits("789");
//		creditCard.setExpiryMonth(11);
//		creditCard.setExpiryYear(2020);
//
//		dao.updateTopupCreditCard(String.valueOf(banId), MDN, creditCard, user, encrypted);
//	}
//	
//	@Test
//	@Deprecated
//	public void testUpdateAutoTopUpDeprecated() throws ApplicationException{
//
//		String phoneNumber = "9052348732";
//		AutoTopUpInfo autoTopUpInfo = new AutoTopUpInfo();
//		autoTopUpInfo.setBan(banId);
//		autoTopUpInfo.setChargeAmount(12);
//		String userId = "integTest";
//		boolean existingAutoTopUp = true;
//		boolean existingThresholdRecharge = false;
//		
//		dao.updateAutoTopUp(banId, phoneNumber, autoTopUpInfo, userId, existingAutoTopUp, existingThresholdRecharge);
//	}
	
	
	
	
	
	
	@Test
	public void testShakedownWS() throws ApplicationException {
		testPWCOSWS();
		testPSSWS();
		testSBMSWS();
		testSMSWS();
	}

	@Test
	public void testPWCOSWS() throws ApplicationException {
		TestPointResultInfo result = pwcosDao.test();
		System.out.println(result.toString());
		assertEquals("Success", result.getResultDetail());
	}
	
	@Test
	public void testPSSWS() throws ApplicationException {
		TestPointResultInfo result = pssDao.test();
		System.out.println(result.toString());
		assertEquals("3.0", result.getResultDetail());
	}
	
	@Test
	public void testSBMSWS() throws ApplicationException {
		TestPointResultInfo result = sbmsDao.test();
		System.out.println(result.toString());
		assertEquals("1.0", result.getResultDetail());
	}
	
	@Test
	public void testSMSWS() throws ApplicationException {
		TestPointResultInfo result = smsDao.test();
		System.out.println(result.toString());
		assertEquals("3.0", result.getResultDetail());
	}
	
	@Test
	public void testGetDate() {
		Calendar cal = Calendar.getInstance();
		for (int i=0; i<12; i++) {
			cal.add(Calendar.MONTH, 1);
			System.out.println(cal.getTime());
			System.out.println("Year & Month: " + DateUtil.getYear(cal.getTime())
					+ " & " + DateUtil.getMonth(cal.getTime()));
			System.out.println("Two digits Year & Month: " + DateUtil.getLastTwoDigitYear(cal.getTime())
					+ " & " + DateUtil.getTwoDigitMonth(cal.getTime()));
		}
	}
	
	
	//==============================================
	//=== PrepaidWirelessCustomerOrderServiceDao ===
	//==============================================
	
	@Test
	public void testCreditSubscriberMigration() throws ApplicationException {
		//String banId = "75646271"; // CustomerAccountProfile - customerId
		//String phoneNumber = "4165432101"; // SubscriptionProfile - subscriptionID & Simcard - msisdn
		String esn = "8912230000001121430";
		String imei = "359614021676143";
		String provinceCode = "ON"; // SubscriptionProfile - subscriptionProvinceCode
		String pin = "1234"; // SubscriptionProfile - subscriptionPin
		double creditAmt = 10.0; // CreditList - amount
		String rateId = "6"; // CreditList -  rateId
		int expiryDays = 30; // CreditList - expiryDaysAmount
		String source = "4"; //OrderDetail - order type
		String user = "19045"; //OrderDetail - user agent 
		String activationType = "4"; //OrderDetail - orderType
		String reasonCode = "242"; //OrderDetail - orderReasonID
		pwcosDao.creditSubscriberMigration(""+banId, esn, imei, phoneNumber, provinceCode, pin, creditAmt, rateId, expiryDays, source, user, activationType, reasonCode);
		//Passed
	}
	
	@Test
	public void testSaveActivationTopUpArrangementPreRegisteredCreditCardVoucher() throws ApplicationException {
		String esn="8912230000001121430";
		PaymentCard[] paymentCards = new PaymentCard[2];
		paymentCards[0] = getPreRegisteredPrepaidCreditCard();
		paymentCards[1] = getVoucher();
		pwcosDao.saveActivationTopUpArrangement(""+banId, esn, phoneNumber, paymentCards);
		//passed
	}
	
	@Test
	public void testSaveActivationTopUpArrangementPreRegisteredCreditCard() throws ApplicationException {
		esn="8912239900001205089";
		PaymentCard[] paymentCards = new PaymentCard[1];
		PreRegisteredPrepaidCreditCardInfo cc = getPreRegisteredPrepaidCreditCard();
		//set the topup amount as 0 for activation
		cc.setTopupAmout(0);
		paymentCards[0] = cc;
		pwcosDao.saveActivationTopUpArrangement(""+banId, esn, phoneNumber, paymentCards);
		//passed
	}
	
	@Test
	public void testSaveActivationTopUpArrangementAirtimeCard() throws ApplicationException {
		String esn="8912239900000907628";
		PaymentCard[] paymentCards = new PaymentCard[1];
		paymentCards[0] = getVoucher();
		pwcosDao.saveActivationTopUpArrangement(""+banId, esn, phoneNumber, paymentCards);
		//passed
	}
	
	@Test
	public void testSaveActivationTopUpArrangementDebitCard() throws ApplicationException {
		String esn="8912239900000907628";
		PaymentCard[] paymentCards = new PaymentCard[1];
		PrepaidDebitCardInfo cc = new PrepaidDebitCardInfo();
		cc.setTopupAmout(10);
		paymentCards[0] = cc;
		pwcosDao.saveActivationTopUpArrangement(""+banId, esn, phoneNumber, paymentCards);
		//passed
	}
	
	
	
	
	//===================================
	//=== PrepaidSubscriberServiceDao ===
	//===================================
	
	@Test
	public void testUpdateAccountPIN() throws ApplicationException {
		pssDao.updateAccountPIN(phoneNumber, "555");
		//passed
		//Use PrepaidSubscriberService.getDetails to verify as the pin is not set from the PrepaidSubscriberServiceDaoImpl.mapToPrepaidConsumerAccountInfo in Account Information Helper
		//TODO verify from the getDetails
	}
	
	
	//=========================================
	//=== SubscriptionBalanceMgmtServiceDao ===
	//=========================================
	
	@Test
	public void testAdjustBalance() throws ApplicationException {
		String reasonCode = "5890"; //Device payment from balance
		String transactionId = "999";
		//sbmsDao.adjustBalance(phoneNumber, 20, reasonCode, transactionId);
		//sbmsDao.adjustBalance(phoneNumber, -5, reasonCode, transactionId);
		//TODO verify from PrepaidSubscriberService.retrieveAccountInfo
	}
	
	
	//========================================
	//=== SubscriptionManagementServiceDao ===
	//========================================
	
	// ======= Topup CreditCard Test cases ======= 

	@Test
	public void testRemoveTopupCreditCard() throws ApplicationException {
		smsDao.removeTopupCreditCard(phoneNumber);
		//passed
	}
	
	@Test
	public void testRegisterTopupCreditCard() throws ApplicationException {
		smsDao.registerTopupCreditCard(phoneNumber, getCreditCard1());
		//passed
	}
	
	@Test
	public void testUpdateTopupCreditCard() throws ApplicationException {
		smsDao.updateTopupCreditCard(phoneNumber, getCreditCard2());
		//passed
	}
	
	// ======= AutoTopup Test cases ======= 
	
	@Test
	public void testRemoveAutoTopUp() throws ApplicationException {
		AutoTopUpInfo autoTopup = getRemoveIntervalThresholdAutoTopup();
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = true;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//passed
	}
	
	@Test
	public void testRemoveAutoTopUpInterval() throws ApplicationException {
		//This should remove both interval and threshold even though only interval in the request
		AutoTopUpInfo autoTopup = getRemoveIntervalAutoTopup();
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = true;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//passed
	}
	
	@Test
	public void testRegisterAutoTopUp() throws ApplicationException {
		AutoTopUpInfo autoTopup = getIntervalThresholdAutoTopup();
		boolean existingAutoTopUp = false;
		boolean existingThresholdRecharge = true;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//TODO verify amount to be 10, 25
		//passed
	}
	
	@Test
	public void testRegisterAutoTopUpExistingInterval() throws ApplicationException {
		AutoTopUpInfo autoTopup = getIntervalThresholdAutoTopup();
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = false;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//TODO verify amount to be 10, 25
		//passed
	}

	@Test
	public void testRegisterAutoTopUpInterval() throws ApplicationException {
		AutoTopUpInfo autoTopup = getIntervalAutoTopup();
		boolean existingAutoTopUp = false;
		boolean existingThresholdRecharge = false;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//TODO verify amount to be 25 only
		//passed
	}
	
	@Test
	public void testUpdateAutoTopUp() throws ApplicationException {
		AutoTopUpInfo autoTopup = getIntervalThresholdAutoTopup();
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = true;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//TODO verify amount to be 10, 25
	}
	
	@Test
	public void testUpdateAutoTopUpInterval() throws ApplicationException {
		AutoTopUpInfo autoTopup = getIntervalAutoTopup();
		boolean existingAutoTopUp = true;
		boolean existingThresholdRecharge = true;
		smsDao.updateAutoTopUp(phoneNumber, autoTopup, existingAutoTopUp, existingThresholdRecharge);
		//TODO verify amount to be 25 only
	}
	
	// ===== applyTopUp test cases =====
	
	@Test
	public void testApplyTopUpVoucher() throws ApplicationException {
		//Prepaid team need to update the voucher again once it is applied already. 
		//Get Vcoucher from http://emtools.tmi.telus.com/RM/prepaidsearchaction.do
		String voucherPin = "000154281234";
		smsDao.applyTopUp(phoneNumber, voucherPin);
	}
	
	@Test
	public void testApplyTopUpWithCreditCard() throws ApplicationException {
		smsDao.applyTopUpWithCreditCard(phoneNumber, 10);
	}
	
	@Test
	public void tsetApplyTopUpWithDebitCard() throws ApplicationException {
		smsDao.applyTopUpWithDebitCard(phoneNumber, 10);
	}
	
	
	
	//Helper methods
	
	private PreRegisteredPrepaidCreditCardInfo getPreRegisteredPrepaidCreditCard() {
		PreRegisteredPrepaidCreditCardInfo cc = new PreRegisteredPrepaidCreditCardInfo();
		cc.setToken("100000000000004026246");
		cc.setTrailingDisplayDigits("0081");
		cc.setExpiryMonth(02);
		cc.setExpiryYear(15);
		cc.setTopupAmout(50);
		cc.setThresholdAmount(25);
		cc.setRechargeAmount(10);
		return cc;
	}
	
	private CardInfo getVoucher() {
		CardInfo cc = new CardInfo();
		//String voucher = PrepaidUtils.decodeBase64String("MTczMDMwNjQyMDQ3");
		//17303064 - 2047
		cc.setSerialNumber("17303064");
		cc.setPIN("2047");
		return cc;
	}
	
	
	private AutoTopUpInfo getRemoveIntervalThresholdAutoTopup() {
		AutoTopUpInfo result = new AutoTopUpInfo();
		//setting value to be 0 will remove it
		result.setChargeAmount(0);
		result.setThresholdAmount(0);
		return result;
	}
	
	private AutoTopUpInfo getRemoveIntervalAutoTopup() {
		AutoTopUpInfo result = new AutoTopUpInfo();
		//setting value to be 0 will remove it
		result.setChargeAmount(0);
		return result;
	}
	
	private AutoTopUpInfo getIntervalAutoTopup() {
		//Amounts which are valid for AutoTopup are: 10,25,50
		AutoTopUpInfo result = new AutoTopUpInfo();
		result.setChargeAmount(25);
		return result;
	}
	
	private AutoTopUpInfo getIntervalThresholdAutoTopup() {
		//Amounts which are valid for AutoTopup are: 10,25,50
		AutoTopUpInfo result = new AutoTopUpInfo();
		result.setChargeAmount(10);
		result.setThresholdAmount(25);
		result.setHasThresholdRecharge(true);
		return result;
	}
	
	private CreditCardInfo getCreditCard1() throws ApplicationException {
		CreditCardInfo cc = new CreditCardInfo();
		cc.setType("MC");//this field will be checked and might be overridden
		cc.setToken("100000000000000590791");
		cc.setLeadingDisplayDigits("450117");
		cc.setTrailingDisplayDigits("7215");
		cc.setExpiryMonth(6);//better to test with one digit month as it should be sending two digits always
		cc.setExpiryYear(16);
		return cc;
	}

	private CreditCardInfo getCreditCard2() throws ApplicationException {
		CreditCardInfo cc = new CreditCardInfo();
		cc.setType("MC");
		cc.setToken("100000000000000590790");
		cc.setLeadingDisplayDigits("450113");
		cc.setTrailingDisplayDigits("7213");
		cc.setExpiryMonth(3);
		cc.setExpiryYear(15);
		return cc;
	}


}