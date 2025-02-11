package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.ActivationCredit;
import com.telus.api.account.Address;
import com.telus.api.account.AddressHistory;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BankAccount;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.CancellationPenalty;
import com.telus.api.account.Charge;
import com.telus.api.account.Cheque;
import com.telus.api.account.Credit;
import com.telus.api.account.CreditCard;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.DepositAssessedHistory;
import com.telus.api.account.DepositHistory;
import com.telus.api.account.Discount;
import com.telus.api.account.FollowUpStatistics;
import com.telus.api.account.FutureStatusChangeRequest;
import com.telus.api.account.InvalidAddressException;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.PCSAccount;
import com.telus.api.account.PaymentHistory;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PaymentMethodChangeHistory;
import com.telus.api.account.PaymentNotification;
import com.telus.api.account.PoolingPricePlanSubscriberCount;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PricePlanSubscriberCount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.SearchResults;
import com.telus.api.account.ServiceSubscriberCount;
import com.telus.api.account.StatusChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberInvoiceDetail;
import com.telus.api.account.VoiceUsageSummary;
import com.telus.api.reference.BusinessRole;
import com.telus.api.reference.ChargeType;
import com.telus.api.reference.CreditCheckDepositChangeReason;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BankAccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.PoolingPricePlanSubscriberCountInfo;

public class TMInvoiceHistoryIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
           setupEASECA_QA();
//		setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMInvoiceHistoryIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}

	
	public void testGetSubscriberInvoiceDetails() throws TelusAPIException{
		System.out.println("testGetSubscriberInvoiceDetails start");
		Account account = accountManager.findAccountByBAN(6376486);
		Date fromDate = getDateInput(2003, 0, 01);
		Date toDate = getDateInput(2008, 0, 31);
		
		InvoiceHistory[] invoiceHistories=account.getInvoiceHistory(fromDate, toDate);
		assertEquals(2,invoiceHistories.length);
		TMInvoiceHistory history= (TMInvoiceHistory) invoiceHistories[0];
		SubscriberInvoiceDetail[] sInvoiceDetails= history.getSubscriberInvoiceDetails();
		
		assertEquals(25,sInvoiceDetails.length);
		
		
       	System.out.println("testGetSubscriberInvoiceDetails End");
	}
	
	
	
	}


