/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Cheque;
import com.telus.api.account.CreditCard;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.UnknownBANException;
import com.telus.api.util.SessionUtil;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;


public class TMPaymentMethod extends BaseProvider implements PaymentMethod {


  private TMAccount account;
  /**
   * @link aggregation
   */
  private final PaymentMethodInfo delegate;
  private final TMCreditCard creditCard;


  public TMPaymentMethod(TMProvider provider, TMAccount account, PaymentMethodInfo delegate) {
    super(provider);
    this.delegate = delegate;
    this.account = account;
    creditCard = new TMCreditCard(provider, delegate.getCreditCard0(), account);
  }

/*
  public TMPaymentMethod(TMProvider provider, PaymentMethodInfo delegate) {
    this(provider, null, delegate);
  }
*/

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------
  public String getPaymentMethod() {
    return delegate.getPaymentMethod();
  }

  public void setPaymentMethod(String paymentMethod) {
    delegate.setPaymentMethod(paymentMethod);
  }

  public Cheque getCheque() {
    return delegate.getCheque0();
  }

  public String getStatus() {
    return delegate.getStatus();
  }

  public void setStatus(String status) {
    delegate.setStatus(status);
  }

  public String getStatusReason() {
    return delegate.getStatusReason();
  }

  public void setStatusReason(String statusReason) {
    delegate.setStatusReason(statusReason);
  }

  public Date getStartDate() {
    return delegate.getStartDate();
  }

  public void setStartDate(Date startDate) {
    delegate.setStartDate(startDate);
  }

  public Date getEndDate() {
    return delegate.getEndDate();
  }

  public void setEndDate(Date endDate) {
    delegate.setEndDate(endDate);
  }

  public boolean isPaymentMethodRegular() {
    return delegate.isPaymentMethodRegular();
  }

  public boolean isPaymentMethodDebit() {
    return delegate.isPaymentMethodDebit();
  }

  public boolean isPaymentMethodCreditCard() {
    return delegate.isPaymentMethodCreditCard();
  }

  public int hashCode() {
    return delegate.hashCode();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public PaymentMethodInfo getDelegate() {
    return delegate;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public TMCreditCard getCreditCard0() {
    return creditCard;
  }

  public boolean getSuppressReturnEnvelope(){
    return delegate.getSuppressReturnEnvelope();
  }

  public TMAccount getAccount() {
    return account;
  }

  public void setAccount(TMAccount account) {
    this.account = account;
  }

  public final void assertAccountExists() throws UnknownBANException {
    if (account == null) {
      throw new UnknownBANException("This PaymentMethod has not yet been tied to an acount");
    }
    account.assertAccountExists();
  }

  public void setSuppressReturnEnvelope(boolean suppressReturnEnvelope) throws TelusAPIException {
	  assertAccountExists();
	  try {
		  provider.getAccountLifecycleManager().updateReturnEnvelopeIndicator(account.getBanId(), !suppressReturnEnvelope,
				  SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		  delegate.setSuppressReturnEnvelope(suppressReturnEnvelope);

	  } catch (Throwable e) {
		  provider.getExceptionHandler().handleException(e);
	  }
  }

  public void commit() {
    creditCard.commit();
  }

  public void setCreditCard(CreditCard card) {
    CreditCardInfo info = null;
    if (card instanceof TMCreditCard) {
      info = ((TMCreditCard)card).getDelegate();
    }
    else {
      info = (CreditCardInfo)card;
    }

    delegate.setCreditCard0(info);
  }

  public void setCheque(Cheque cheque) {
    delegate.setCheque0((ChequeInfo)cheque);
  }

}




