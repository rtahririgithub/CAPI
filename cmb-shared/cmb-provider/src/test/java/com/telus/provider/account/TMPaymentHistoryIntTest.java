package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.PaymentActivity;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentTransfer;
import com.telus.api.reference.PaymentTransferReason;
import com.telus.eas.account.info.AuditHeaderInfo;

public class TMPaymentHistoryIntTest extends BaseTest {

	static {
		setupEASECA_QA();
//		setupD3();
//		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMPaymentHistoryIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	public void testGetPaymentHistory() throws TelusAPIException{
		System.out.println("testGetPaymentHistory start");
		Account account = api.getAccountManager().findAccountByBAN(12474);
		Date fromDate = getDateInput(2003, 05, 27);
		Date toDate = getDateInput(2009, 05, 27);

		PaymentHistory[] paymentHistories=account.getPaymentHistory(fromDate, toDate);
		TMPaymentHistory paymentHistory= null;
		if(paymentHistories.length>0 ){
			 paymentHistory=(TMPaymentHistory) paymentHistories[0];
		}
		PaymentActivity[] paymentActivity=paymentHistory.getPaymentActivities();
		assertEquals(1, paymentActivity.length);
		   	System.out.println("testGetPaymentHistory End");
	}
	
	public void testTransferPayment() throws TelusAPIException{
		System.out.println("testTransferPayment start");
		try{
		PaymentTransferReason[] paymentTransferReasons = api.getReferenceDataManager().getPaymentTransferReasons();

	    PaymentTransfer paymentTransfer = api.getAccountManager().newPaymentTransfer(12474,  paymentTransferReasons[0].toString(), 199.00);
	    PaymentTransfer[] aryPaymentTransfers = new PaymentTransfer[1];
	    aryPaymentTransfers[0]=paymentTransfer ;

	    Account accountFrom = api.getAccountManager().findAccountByBAN(12474);
	    Date fromDate = getDateInput(2000, 05, 27);
		Date toDate = getDateInput(2011, 05, 27);

	    PaymentHistory[] paymentHistory = accountFrom.getPaymentHistory(fromDate, toDate);

	    paymentHistory[0].transferPayment(aryPaymentTransfers, true, "Test");
		}catch(TelusAPIException e){
			System.out.println("Amdocs validation exception occured");
		}
		System.out.println("testTransferPayment End");
	}
	
	public void testRefundPayment() throws TelusAPIException{
		System.out.println("testRefundPayment start");
		Account account = api.getAccountManager().findAccountByBAN(12474);
		Date fromDate = getDateInput(2003, 05, 27);
		Date toDate = getDateInput(2006, 05, 27);

		PaymentHistory[] paymentHistories=account.getPaymentHistory(fromDate, toDate);
		TMPaymentHistory paymentHistory= null;
		if(paymentHistories.length>0 ){
			 paymentHistory=(TMPaymentHistory) paymentHistories[0];
		}
		//Method 1 -  if part test
		try{
		String businessRole= "businessRole";
		boolean isManual=false;
		String memoText="testRefundPayment1";
		AuditHeader auditHeader = new AuditHeaderInfo();
		paymentHistory.refundPayment(businessRole, PaymentActivity.REFUND_REASON_CREDIT_CARD_REFUND, memoText, isManual, auditHeader);
		}catch(InvalidCreditCardException creditexception){
			assertEquals(": Sorry, we cannot complete your credit card payment. Please check your credit card number and expiration date.   If you continue to experience difficulties, please contact Client Care by dialing 611 from your handset.",creditexception.getMessage());
		}
		//Method 2 - else part test
		try{
			String businessRole= "businessRole";
			boolean isManual=true;
			String memoText="testRefundPayment2";
			AuditHeader auditHeader = new AuditHeaderInfo();
			paymentHistory.refundPayment(businessRole, PaymentActivity.ACTIVITY_PAYMENT, memoText, isManual, auditHeader);
			}catch(TelusAPIException apiexception){
				assertEquals(": The Reason Code is not valid for this action.",apiexception.getMessage());
			}
		
		System.out.println("testRefundPayment End");
	}
	
	
	
	
	
	}


