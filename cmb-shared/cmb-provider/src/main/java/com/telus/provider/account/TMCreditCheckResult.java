/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.Address;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.CLPActivationOptionDetail;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.api.account.PersonalCredit;
import com.telus.api.reference.AmountBarCode;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.ProviderCDAExceptionTranslator;

public class TMCreditCheckResult extends BaseProvider implements CreditCheckResult {

	/**
	 * @link aggregation
	 */
	private final CreditCheckResultInfo delegate;
//	private final AccountSummary account;  

//	public TMCreditCheckResult(TMProvider provider, CreditCheckResultInfo delegate, AccountSummary account) {
	public TMCreditCheckResult(TMProvider provider, CreditCheckResultInfo delegate) {
		super(provider);
		this.delegate = delegate;
		// this.account = account;
	}

	public CreditCheckResultInfo getDelegate() {
		return delegate;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------

	public String getCreditClass() {
		return delegate.getCreditClass();
	}

	public double getLimit() {
		return delegate.getLimit();
	}

	public String getMessage() {
		return delegate.getMessage();
	}

	public String getMessageFrench() {
		return delegate.getMessageFrench();
	}

	public double getDeposit() {
		return delegate.getDeposit();
	}

	public CreditCheckResultDeposit[] getDeposits() {
		return delegate.getDeposits();
	}

	public String getDepositBarCode() {

		if (delegate.getDepositBarCode() != null && delegate.getDepositBarCode().length() > 0) {
			return delegate.getDepositBarCode();
		}
		
		try {
			provider.getReferenceDataManager0();
			AmountBarCode barCode = provider.getReferenceDataManager0().getAmountBarCode(this.getDeposit(), ReferenceDataManager.BARCODE_REASON_ACTIVATION_DEPOSIT);
			this.setDepositBarCode(barCode.getBarCode());			
		} catch (Throwable t) {
			this.setDepositBarCode("");
		}
		
		return delegate.getDepositBarCode();
	}

	public boolean isReferToCreditAnalyst() {
		return delegate.isReferToCreditAnalyst();
	}

	public int getCreditScore() {
		return delegate.getCreditScore();
	}

	public int getUniqueCode() {
		return delegate.getUniqueCode();
	}

	public void setCreditClass(String creditClass) {
		delegate.setCreditClass(creditClass);
	}

	public void setLimit(double limit) {
		delegate.setLimit(limit);
	}

	public void setMessage(String message) {
		delegate.setMessage(message);
	}

	public void setMessageFrench(String messageFrench) {
		delegate.setMessageFrench(messageFrench);
	}

	public void setDeposit(double deposit) {
		delegate.setDeposit(deposit);
	}

	public void setDepositBarCode(String depositBarCode) {
		delegate.setDepositBarCode(depositBarCode);
	}

	public void setReferToCreditAnalyst(boolean referToCreditAnalyst) {
		delegate.setReferToCreditAnalyst(referToCreditAnalyst);
	}

	public void setCreditScore(int creditScore) {
		delegate.setCreditScore(creditScore);
	}

	public boolean isCreditCheckPerformed() {
		return delegate.isCreditCheckPerformed();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------

	// TODO: remove this version for the April 2017 release once we have branched for the Feb 2017 release.
//	public void updateCreditClass(int ban, String newCreditClass, String memoText) throws TelusAPIException {
//		try {
//			provider.getAccountLifecycleManager().updateCreditClass(ban, newCreditClass, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
//		} catch (Throwable t) {
//			provider.getExceptionHandler().handleException(t);
//		}
//	}

	public void updateCreditClass(int ban, String newCreditClass, String memoText) throws TelusAPIException {
		// Note: in order to consolidate functionality in the underlying AccountEJB layer to support the CDA 2017 project,
		// this functionality is now being routed through the updateCreditProfile method.
		updateCreditProfile(ban, newCreditClass, getLimit(), memoText);
	}
	
	/**
	 * updates Credit Class/limit. i.e. called when the account is saved 
	 * added by Roman
	 */
	// TODO: remove this version for the April 2017 release once we have branched for the Feb 2017 release.
//	public void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText) throws TelusAPIException {
//		try {
//			provider.getAccountLifecycleManager().updateCreditProfile(ban, newCreditClass, newCreditLimit, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
//		} catch (Throwable t) {
//			provider.getExceptionHandler().handleException(t);
//		}
//	}

	public void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText) throws TelusAPIException {
		try {
			provider.getAccountLifecycleFacade().updateCreditProfile(ban, newCreditClass, newCreditLimit, memoText, SessionUtil.getSessionId(provider.getAccountLifecycleFacade()));
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t, new ProviderCDAExceptionTranslator());
		}
	}
	
	public ActivationOption[] getActivationOptions() {
		return delegate.getActivationOptions();
	}

	public int getErrorCode() {
		return delegate.getErrorCode();
	}

	public String getErrorMessage() {
		return delegate.getErrorMessage();
	}

	public String getBureauFile() {
		return delegate.getBureauFile();
	}

	public String getDefaultInd() {
		return delegate.getDefaultInd();
	}

	public ConsumerName getLastCreditCheckName() {
		return delegate.getLastCreditCheckName();
	}

	public Address getLastCreditCheckAddress() {
		return delegate.getLastCreditCheckAddress();
	}

	public PersonalCredit getLastCreditCheckPersonalnformation() {
		return delegate.getLastCreditCheckPersonalnformation();
	}

	public String getLastCreditCheckIncorporationNumber() {
		return delegate.getLastCreditCheckIncorporationNumber();
	}

	public Date getLastCreditCheckIncorporationDate() {
		return delegate.getLastCreditCheckIncorporationDate();
	}

	public String getCreditCheckResultStatus() {
		return (delegate.getCreditCheckResultStatus() != null ? delegate.getCreditCheckResultStatus() : (getErrorCode() != 0 ? "E" : "D"));
	}

	public Date getCreditDate() {
		return delegate.getCreditDate();
	}

	public String getCreditParamType() {
		return delegate.getCreditParamType();
	}

	public String getDepositChangeReasonCode() {
		return delegate.getDepositChangeReasonCode();
	}

	public BusinessCreditIdentity getLastCreditCheckSelectedBusiness() {
		return delegate.getLastCreditCheckSelectedBusiness();
	}

	public BusinessCreditIdentity[] getLastCreditCheckListOfBusinesses() {
		return delegate.getLastCreditCheckListOfBusinesses();
	}

	public CLPActivationOptionDetail getCLPActivationOptionDetail() {
		return delegate.getCLPActivationOptionDetail();
	}
	
}