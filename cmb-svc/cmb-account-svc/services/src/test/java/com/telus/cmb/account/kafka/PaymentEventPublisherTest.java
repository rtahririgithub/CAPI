package com.telus.cmb.account.kafka;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.api.account.Account;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.payment.kafka.PaymentEventPublisher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations = { "classpath:application-context-test.xml","classpath:kafka-context-test.xml" })
@ActiveProfiles("standalone")
public class PaymentEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private PaymentEventPublisher paymentEventPublisher;

	@Autowired
	private AccountInformationHelper helper;

	@Test
	public void publishPaymentMethodChange() throws Throwable {
		System.out.println("begin publishPaymentMethodChange...");
		int ban = 26262530;
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban,Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		@SuppressWarnings("unused")
		PaymentMethodInfo removePaymentMethod = creditCardPreAuthPaymentMethodRemoval();
		PaymentMethodInfo addPaymentMethod = creditCardPreAuthPaymentMethodAdd();

		paymentEventPublisher.publishPaymentMethodChange(accountInfo,addPaymentMethod, getAuditInfo(), new Date(), false);

		System.out.println("end publishPaymentMethodChange...");
	}

	@Test
	public void publishMakePayment() throws Throwable {
		System.out.println("begin publishMakePayment...");
		int ban = 70804610;
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban,Account.ACCOUNT_LOAD_ALL);
		paymentEventPublisher.publishMakePayment(accountInfo, getPaymentInfo(), getAuditInfo(), new Date(), false);

		System.out.println("end publishMakePayment...");
	}
	
	private AuditInfo getAuditInfo() {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOutletId("642343251");
		auditInfo.setSalesRepId("ftsale");
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("18654");
		return auditInfo;
	}
	
	private PaymentInfo getPaymentInfo() {
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setAmount(50);
		paymentInfo.setCreditCardInfo(getCreditCard());
		// cheque
		// paymentInfo.getChequeInfo().setChequeNumber(chequeNumber);
		return paymentInfo;

	}
	
	private PaymentMethodInfo creditCardPreAuthPaymentMethodRemoval() {
		/** CreditCard --> Regular */
		
		// set up old payment method as credit card
		PaymentMethodInfo paymentMethod=new PaymentMethodInfo();
		paymentMethod.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);
		paymentMethod.setOldPaymentMethodCardNumber(getCreditCard().getTrailingDisplayDigits());
		
		// set up new payment method as regular
		paymentMethod.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		
		paymentMethod.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_REMOVAL);

		return paymentMethod;
	}
	
	
	private PaymentMethodInfo creditCardPreAuthPaymentMethodAdd() {
		/** Regular-->CreditCard */

		// set up old payment method as regular
		PaymentMethodInfo paymentMethod=new PaymentMethodInfo();
		paymentMethod.setOldPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		
		// set up new payment method as credit card
		paymentMethod.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD);
		paymentMethod.setCreditCard0(getCreditCard());
		paymentMethod.setTransactionType(PaymentMethodInfo.PREAUTHORIZED_TRASACTION_TYPE_NEW);

		return paymentMethod;
	}

	private CreditCardInfo getCreditCard(){
		CreditCardInfo cc = new CreditCardInfo();
		cc.setExpiryMonth(12);
		cc.setExpiryYear(2015);
		cc.setHolderName("Danny Summer");
		cc.setLeadingDisplayDigits("11111111");
		cc.setToken("0000000000000000000000");
		cc.setTrailingDisplayDigits("3821");
		cc.setAuthorizationCode("123");
		return cc;
	}
	
	
	private AccountInfo createAccount() {
		AccountInfo account = new AccountInfo();
		account.setBanId(0);
		account.setAccountType('I');
		account.setAccountSubType('R');
		account.setBrandId(1);
		account.getContactName().setFirstName("First Name");
		account.getContactName().setLastName("Last Name");
		account.setEmail("testemail@telus.com");
		account.setLanguage("EN");
		account.setCreateDate( new Date());	
		account.setBanSegment("TCSI");	
		account.setBanSubSegment("TCSI-SUB");

		return account;
	}
}