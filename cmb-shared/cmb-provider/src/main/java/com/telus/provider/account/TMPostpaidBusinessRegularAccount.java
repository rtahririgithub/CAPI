/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Address;
import com.telus.api.account.BusinessCredit;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.CreditCheckNotRequiredException;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.InvalidCreditCardException;
import com.telus.api.account.InvoiceProperties;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PersonalCredit;
import com.telus.api.account.PostpaidBusinessRegularAccount;
import com.telus.api.account.UnknownBANException;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.AppConfiguration;

public class TMPostpaidBusinessRegularAccount extends TMPostpaidAccount implements PostpaidBusinessRegularAccount {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @link aggregation
	 */
	private final PostpaidBusinessRegularAccountInfo delegate;
	private TMPaymentMethod oldPaymentMethod;
	private final TMPaymentMethod paymentMethod;
	private BusinessCreditIdentityInfo[] businessCreditIdentities;
	private final TMPersonalCredit personalCredit;

	public TMPostpaidBusinessRegularAccount(TMProvider provider, PostpaidBusinessRegularAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
		paymentMethod = new TMPaymentMethod(provider, this, delegate.getPaymentMethod0());
		personalCredit = new TMPersonalCredit(provider, delegate.getPersonalCreditInformation0(), this);
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public Address getAlternateCreditCheckAddress() {
		return delegate.getAlternateCreditCheckAddress();
	}

	public String getLegalBusinessName() {
		return delegate.getLegalBusinessName();
	}

	public void setLegalBusinessName(String legalBusinessName) {
		delegate.setLegalBusinessName(legalBusinessName);
	}

	public String getTradeNameAttention() {
		return delegate.getTradeNameAttention();
	}

	public void setTradeNameAttention(String tradeNameAttention) {
		delegate.setTradeNameAttention(tradeNameAttention);
	}

	public String getFirstName() {
		return delegate.getFirstName();
	}

	public String getLastName() {
		return delegate.getLastName();
	}

	public BusinessCredit getCreditInformation() {
		return delegate.getCreditInformation();
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

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public TMPaymentMethod getPaymentMethod0() {
		return paymentMethod;
	}

	public PersonalCredit getPersonalCreditInformation() {
		return personalCredit;
	}

	public TMPersonalCredit getPersonalCreditInformation0() {
		return personalCredit;
	}

	protected void validateCreditCards(String businessRole) throws TelusAPIException, InvalidCreditCardException {
		
		if (paymentMethod.isPaymentMethodCreditCard()) {
			TMCreditCard card = paymentMethod.getCreditCard0();
			if (card.getNeedsValidation()) {
				card.copyCreditInformation(this);
				card.validate("payment method", businessRole);
			}
		}
	}
	
	// Note: this is the new (April 2017) version of getBusinessCreditIdentities that integrates with the CreditProfileService instead of the HCD-API.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BusinessCreditIdentity[] getBusinessCreditIdentities() throws TelusAPIException {

		assertAccountExists();		
		if (businessCreditIdentities == null) {
			try {
				// CDA 2017: re-routed existing HCD (Historical Client Detection) API method calls to use the CreditProfileService web service
				
				// Redo code fix for defect#56624
				List list = provider.getAccountLifecycleFacade().getCreditEvaluationBusinessList(delegate, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
				//List list = provider.getAccountLifecycleFacade().getCreditEvaluationBusinessList(getBanId(), SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
				
				businessCreditIdentities = (BusinessCreditIdentityInfo[]) list.toArray(new BusinessCreditIdentityInfo[list.size()]);

			} catch (Throwable t) {
				throw new TelusAPIException(t);
			}
		}
		
		return businessCreditIdentities;
	}

	public FeeWaiver newFeeWaiver(String typeCode) {
		
		FeeWaiverInfo info = new FeeWaiverInfo();
		info.setBanId(getBanId());
		info.setTypeCode(typeCode);
		info.setMode(FeeWaiverInfo.INSERT);
		
		return (new TMFeeWaiver(provider, info));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FeeWaiver[] getFeeWaivers() throws TelusAPIException {
		
		assertAccountExists();
		FeeWaiverInfo[] waivers = null;
		try {
			List list = provider.getAccountLifecycleManager().retrieveFeeWaivers(getBanId(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
			waivers = (FeeWaiverInfo[]) list.toArray(new FeeWaiverInfo[list.size()]);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		
		return decorate(waivers);
	}

	// Updated for CDA phase 1B July 2018
	public CreditCheckResult checkCredit(BusinessCreditIdentity identity) throws TelusAPIException, CreditCheckNotRequiredException {
		
		assertAccountExists();		
		try {
			// CDA 2017: re-routed existing HCD (Historical Client Detection) API method calls to use the CreditProfileService web service
			if (businessCreditIdentities == null) {
				if (getCreditCheckResult() != null && getCreditCheckResult().getLastCreditCheckListOfBusinesses() != null) {
					businessCreditIdentities = (BusinessCreditIdentityInfo[]) getCreditCheckResult().getLastCreditCheckListOfBusinesses();
				} else {
					// This call will refresh the business credit identities from the CreditProfileService
					getBusinessCreditIdentities();
				}
			}
			
			// Note: this call performs the credit check and saves the result in KB
			CreditCheckResultInfo creditCheckResultInfo = provider.getAccountLifecycleFacade().checkCredit(delegate, businessCreditIdentities, (BusinessCreditIdentityInfo) identity, 
					manualCreditRequest.transformed, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
			
			// Copy the result back into the delegate
			delegate.getCreditCheckResult0().copyFrom(creditCheckResultInfo);
			
		} catch (CreditCheckNotRequiredException ccnre) {
			throw ccnre;
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
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
			
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
	}

	public TMPaymentMethod getOldPaymentMethod() {
		return oldPaymentMethod;
	}

	public void setupOldData() {
		super.setupOldData();
		oldPaymentMethod = new TMPaymentMethod(provider, this, (PaymentMethodInfo) paymentMethod.getDelegate().clone());
	}

	public void commit() {
		super.commit();
		paymentMethod.commit();
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("TMPostpaidBusinessRegularAccount:[\n");
		s.append("    personalCredit=[").append(personalCredit).append("]\n");
		s.append("    oldPaymentMethod=[").append(oldPaymentMethod).append("]\n");
		s.append("    paymentMethod=[").append(paymentMethod).append("]\n");
		s.append("    delegate=[").append(delegate).append("]\n");
		s.append("]");

		return s.toString();
	}

}