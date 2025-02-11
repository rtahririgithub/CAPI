/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Address;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CLMSummary;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.CreditCheckNotRequiredException;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PersonalCredit;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.UnknownBANException;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.SessionUtil;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.account.info.CLMSummaryInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.Logger;

public class TMPostpaidConsumerAccount extends TMPostpaidAccount implements PostpaidConsumerAccount {

	private static final long serialVersionUID = 1L;

	/**
	 * @link aggregation
	 */
	private final PostpaidConsumerAccountInfo delegate;
	private final TMPersonalCredit personalCredit;
	private TMPaymentMethod oldPaymentMethod;
	private final TMPaymentMethod paymentMethod;
	private ConsumerNameInfo oldConsumerName;

	public TMPostpaidConsumerAccount(TMProvider provider, PostpaidConsumerAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
		personalCredit = new TMPersonalCredit(provider, delegate.getPersonalCreditInformation0(), this);
		paymentMethod = new TMPaymentMethod(provider, this, delegate.getPaymentMethod0());
		oldConsumerName = delegate.getName0();
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------

	public ConsumerName getOldConsumerName() {
		return oldConsumerName;
	}

	public Address getAlternateCreditCheckAddress() {
		return delegate.getAlternateCreditCheckAddress();
	}

	public ConsumerName getName() {
		return delegate.getName();
	}

	public String getAdditionalName() {
		return delegate.getName().getAdditionalLine();
	}

	public void setAdditionalName(String additionalName) {
		delegate.setAdditionalLine(additionalName);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public InvoiceProperties getInvoiceProperties() {
		return delegate.getInvoiceProperties();
	}

	public void setInvoiceProperties(InvoiceProperties invoiceProperties) {
		delegate.setInvoiceProperties(invoiceProperties);
	}

	//--------------------------------------------------------------------
	//  Service Methods
	//--------------------------------------------------------------------

	public PersonalCredit getPersonalCreditInformation() {
		return personalCredit;
	}

	public TMPersonalCredit getPersonalCreditInformation0() {
		return personalCredit;
	}

	public PersonalCredit getCreditInformation() {
		return getPersonalCreditInformation();
	}

	public CLMSummary getCLMSummary() throws TelusAPIException {
		CLMSummary clmSummary=null;
		try {

			Date defaultDate = new Date(100, 0, 1); //2000-01-01
			InvoiceHistory[] invoiceHistory = getInvoiceHistory( defaultDate, new Date());
			
			double unpaidVoiceUsage = getUnpaidAirtimeTotal();
			
			double unpaidDataUsage=0.0;
			
			if (invoiceHistory.length>0) {
				Date invoiceDate = invoiceHistory[0].getDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(invoiceDate);
				//advance last invoice date by one month, so we get current bill cycle
				cal.add(Calendar.MONTH, 1 );
					
				int billCycleYear = cal.get(Calendar.YEAR );
				int billCycleMonth = cal.get(Calendar.MONTH )+1; 

				unpaidDataUsage= provider.getAccountLifecycleFacade().getTotalUnbilledDataAmount( getBanId(), billCycleYear, billCycleMonth, getBillCycle() );
			} else {
				unpaidDataUsage= provider.getAccountLifecycleFacade().getTotalDataOutstandingAmount(getBanId(), defaultDate );
			}
			
			double billedCharges    = getFinancialHistory().getDebtSummary().getCurrentDue()+getFinancialHistory().getDebtSummary().getPastDue();
			double unpaidUnBilledAmount = unpaidUnBilledAmount();
			double reqMinPayment = (unpaidVoiceUsage + unpaidDataUsage + unpaidUnBilledAmount()) - (getCreditCheckResult().getLimit() / 2);
			if (reqMinPayment < 0) {
				reqMinPayment = 0;
			}
			reqMinPayment = reqMinPayment + billedCharges;
			if (reqMinPayment < 0) {
				reqMinPayment = 0;
			}


			clmSummary=new CLMSummaryInfo(billedCharges, unpaidVoiceUsage, unpaidDataUsage, reqMinPayment, unpaidUnBilledAmount);
		} catch (Throwable t) {
	    	provider.getExceptionHandler().handleException(t);
	    }
		return clmSummary;
	}

	public TMPersonalCredit getCreditInformation0() {
		return getPersonalCreditInformation0();
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public TMPaymentMethod getPaymentMethod0() {
		return paymentMethod;
	}

	protected void validateCreditCards(String businessRole) throws TelusAPIException, InvalidCreditCardException {

		if (paymentMethod.isPaymentMethodCreditCard()) {
			TMCreditCard card2 = paymentMethod.getCreditCard0();
			if (card2.getNeedsValidation()) {
				card2.copyCreditInformation(this);
				card2.validate("payment method", businessRole);
			}
		}
	}

	public FeeWaiver newFeeWaiver(String typeCode) {
		
		FeeWaiverInfo info = new FeeWaiverInfo();
		info.setBanId(getBanId());
		info.setTypeCode(typeCode);
		info.setMode(FeeWaiverInfo.INSERT);
		
		return (new TMFeeWaiver(provider, info));
	}

	public FeeWaiver[] getFeeWaivers() throws TelusAPIException {
		
		assertAccountExists();
		FeeWaiverInfo[] waivers = null;
		try {
			List list = provider.getAccountLifecycleManager().retrieveFeeWaivers(getBanId(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
			waivers = (FeeWaiverInfo[]) list.toArray(new FeeWaiverInfo[list.size()]);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		
		return decorate(waivers);
	}

	// Updated for CDA phase 1B July 2018
	public CreditCheckResult checkCredit(AuditHeader auditHeader) throws TelusAPIException, CreditCheckNotRequiredException {

		assertAccountExists();
		try {
			if (auditHeader == null) {
				throw new TelusAPIException("The required AuditHeader is missing.");
			}
			
			// Perform the credit check and save the result in KB
			// Check the manualCreditRequest.transformed boolean to determine if a new credit name and address need to be saved to KB in addition to the credit check result
			// Otherwise, save only the credit check result to KB
			CreditCheckResultInfo creditCheckResultInfo = provider.getAccountLifecycleFacade().checkCredit(delegate, manualCreditRequest.transformed ? delegate.getName0() : null,
					manualCreditRequest.transformed ? delegate.getAddress0() : null, manualCreditRequest.transformed, auditHeader, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));

			// Copy the results back into the delegate
			delegate.getCreditCheckResult0().copyFrom(creditCheckResultInfo);
			delegate.getCreditCheckResult0().setLastCreditCheckAddress(delegate.getAddress0());
			delegate.getCreditCheckResult0().setLastCreditCheckName(delegate.getName0());

			Logger.debug("AccountLifecycleFacade().checkCredit: manualCreditRequest.transformed=[" + manualCreditRequest.transformed + "].");
			Logger.debug("AccountLifecycleFacade().checkCredit CreditCheckResultInfo: " + creditCheckResultInfo);

		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t, new TelusExceptionTranslator() {

				public TelusAPIException translateException(Throwable throwable) {
					if (throwable instanceof CreditCheckNotRequiredException) {
						return (CreditCheckNotRequiredException) throwable;
					}
					return null;
				}
			});
		}

		return delegate.getCreditCheckResult0();
	}
	
	public void savePaymentMethod(PaymentMethod newPaymentMethod, ServiceRequestHeader header) throws UnknownBANException, TelusAPIException {
		savePaymentMethod(newPaymentMethod);
		if (header != null && AppConfiguration.isSRPDSEnabled()) {
			TMServiceRequestManager serviceRequestManager = (TMServiceRequestManager) provider.getServiceRequestManager();
			serviceRequestManager.reportChangePaymentMethod(getBanId(), getDealerCode(), getSalesRepCode(), provider.getUser(), newPaymentMethod, header);
		}
	}

	public void savePaymentMethod(PaymentMethod newPaymentMethod) throws UnknownBANException, TelusAPIException {
		
		assertAccountExists();
		try {
			PaymentMethodInfo info = ((TMPaymentMethod) newPaymentMethod).getDelegate();
			provider.getAccountLifecycleFacade().updatePaymentMethod(getBanId(), this.getDelegate0(), info, getTransientNotificationSuppressionInd(), null,
					SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			provider.getInteractionManager0().changePaymentMethod(this, oldPaymentMethod.getPaymentMethod().charAt(0), newPaymentMethod.getPaymentMethod().charAt(0));
			delegate.getPaymentMethod0().copyFrom(info);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
	}

	public TMPaymentMethod getOldPaymentMethod() {
		return oldPaymentMethod;
	}

	public void setupOldData() {
		super.setupOldData();
		oldPaymentMethod = new TMPaymentMethod(provider, this, (PaymentMethodInfo) paymentMethod.getDelegate().clone());
		oldConsumerName = (ConsumerNameInfo) delegate.getName0().clone();
	}

	public void commit() {
		super.commit();
		personalCredit.commit();
		paymentMethod.commit();
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("TMPostpaidConsumerAccount:[\n");
		s.append("    personalCredit=[").append(personalCredit).append("]\n");
		s.append("    oldPaymentMethod=[").append(oldPaymentMethod).append("]\n");
		s.append("    oldConsumerName=[").append(oldConsumerName).append("]\n");
		s.append("    paymentMethod=[").append(paymentMethod).append("]\n");
		s.append("    delegate=[").append(delegate).append("]\n");
		s.append("]");

		return s.toString();
	}

}