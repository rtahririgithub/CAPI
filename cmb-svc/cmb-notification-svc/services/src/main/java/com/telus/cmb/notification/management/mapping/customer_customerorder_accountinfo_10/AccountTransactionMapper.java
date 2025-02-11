package com.telus.cmb.notification.management.mapping.customer_customerorder_accountinfo_10;

import com.telus.cmb.common.confirmationnotification.Adjustment;
import com.telus.cmb.common.confirmationnotification.BillingAccount;
import com.telus.cmb.common.confirmationnotification.ConfirmationNotification;
import com.telus.cmb.common.confirmationnotification.Payment;
import com.telus.cmb.common.confirmationnotification.PaymentArrangement;
import com.telus.cmb.common.confirmationnotification.PaymentMethod;
import com.telus.cmb.common.confirmationnotification.PaymentNotification;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.cmb.notification.management.mapping.AccountTransactionEmailTemplateMapper;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.AccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.CreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.CustomerAccount;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.PaymentInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.PaymentMethodInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.accountinfo_v1.PaymentReportInfo;

public class AccountTransactionMapper extends AbstractSchemaMapper<CustomerAccount, ConfirmationNotification> implements AccountTransactionEmailTemplateMapper{

	public static final String PAYMENT_STATUS = "Completed";
	public AccountTransactionMapper() {
		super(CustomerAccount.class, ConfirmationNotification.class);
	}

	@Override
	protected CustomerAccount performSchemaMapping(ConfirmationNotification source, CustomerAccount target) {
		
		target.setAccountInfo( mapToAccountInfo(source.getBillingAccount()));
		target.setTransactionDate(source.getTransactionDate());
		if ( source.getSubscriber()!=null) {
			target.getAccountInfo().setSubscriberPhoneNumber(source.getSubscriber().getSubscriberPhoneNumber());
		}
		
		if ( source.getPaymentMethod()!=null ) {
			target.setPaymentMethod( mapToPaymentmethodInfo( source.getPaymentMethod() ) );
		} 
		if ( source.getPayment()!=null) {
			target.setPayment( mapToPaymentInfo(source.getPayment()));
		}
		if ( source.getPaymentArrangement()!=null) {
			target.setPaymentReport( mapToPaymentReportInfo( source.getPaymentArrangement() ) );
		} 
		if (source.getPaymentNotification()!=null ) {
			target.setPaymentReport( mapToPaymentReportInfo( source.getPaymentNotification() )) ;
		} 
		if ( source.getAdjustment()!=null ) {
			target.setCredit( mapToCreditInfo( source.getAdjustment() ));
		}
		return super.performSchemaMapping(source, target);
	}

	private CreditInfo mapToCreditInfo(Adjustment source) {
		CreditInfo target = new CreditInfo();
		target.setCreditAmount(source.getAmount());
		target.setCreditDate(source.getEffectiveDate());
		target.setNumberOfMonths(source.getNumberOfRecurring());
		
		double taxAmount = source.getGst() + source.getHst() + source.getPst();
		if ( taxAmount>0) {
			target.setTaxAmount( taxAmount );
		}
		
		if ( source.getNumberOfRecurring()>1) {
			target.setTransactionType("recurring");
		} else {
			target.setTransactionType("onetime");
		}
		return target;
	}

	private PaymentReportInfo mapToPaymentReportInfo(PaymentArrangement source) {
		PaymentReportInfo target = new PaymentReportInfo();
		target.setAmount(source.getAmount());
		target.setDueDate(source.getPaymentDueDate());
		target.setArrangementDate(source.getPayDate());
		return target;
	}

	private PaymentReportInfo mapToPaymentReportInfo(PaymentNotification source) {
		PaymentReportInfo target =new PaymentReportInfo();
		target.setAmount(source.getAmount());
		target.setDueDate(source.getPaymentDueDate());
		target.setTransactionReferenceId(source.getReferenceNumber());
		return target;
	}

	private PaymentInfo mapToPaymentInfo(Payment source) {
		PaymentInfo target = new PaymentInfo();
		target.setAmount(source.getAmount());

		if ( source.getAuthorizationNumber()!=null ) 
			target.setAuthorizationNumber(source.getAuthorizationNumber());
		
		target.setPaymentMethod( mapToPaymentmethodInfo ( source.getPaymentMethod() ) );
		return target;
	}

	private AccountInfo mapToAccountInfo(BillingAccount source ) {
		AccountInfo target = new AccountInfo();
		target.setAccountStatus( translateKBAccountStatusCode( source.getStatus() ) );
		target.setBillingAccountNumber(source.getAccountNumber());
		target.setClpInd(source.isClpInd());
		target.setDelinquentInd(source.isDelinquentInd());
		target.setHotlinedInd(source.isHotlinedInd());
		return target;
	}
	
	private PaymentMethodInfo mapToPaymentmethodInfo(PaymentMethod source) {
		PaymentMethodInfo target = new PaymentMethodInfo();
		if ( source.getCreditCardLast4()!=null ) {
			target.setCreditCardNumber(source.getCreditCardLast4());
			target.setTypeCode("CC");
		} else if ( source.getBankAccountNumber()!=null ){ 
			target.setBankAccountNumber(source.getBankAccountNumber());
			target.setTypeCode("BD");
		}
		return target;
	}
	
	private static final char STATUS_CLOSED    = 'C';
	private static final char STATUS_CANCELED  = 'N';
	private static final char STATUS_OPEN      = 'O';
	private static final char STATUS_SUSPENDED = 'S';
	private static final char STATUS_TENTATIVE = 'T';
	
	private String translateKBAccountStatusCode(String kbBanStatus) {
		
		switch( kbBanStatus.charAt(0) ) {
			case STATUS_OPEN:      return "A";
			case STATUS_SUSPENDED: return "S";
			case STATUS_CANCELED:  return "C";

			//we don't expect the following ban status coming to here 
			case STATUS_TENTATIVE: return "T";
			case STATUS_CLOSED:    return "Z";
			default:               return "X";
		}
	}

}
