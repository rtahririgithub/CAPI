/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class PaymentMethodInfo extends Info implements PaymentMethod {

   static final long serialVersionUID = 1L;

  public static final String DIRECT_DEBIT_STATUS_ACTIVE = "A";
  public static final String DIRECT_DEBIT_STATUS_CANCELED = "C";
  public static final String DIRECT_DEBIT_STATUS_HOLD = "H";
  public static final String DIRECT_DEBIT_STATUS_PENDING = "P";

  public static final String DIRECT_DEBIT_REASON_ZERO_ACCT_BALANCE = "AMTZRO";
  public static final String DIRECT_DEBIT_REASON_USER_REQUEST = "Usereq";
  public static final String DIRECT_DEBIT_REASON_CHANGE_TO_REGULAR = "CCtoRe";
  public static final String DIRECT_DEBIT_REASON_CHANGE_TO_DD = "CCtoDD";
  public static final String DIRECT_DEBIT_REASON_CHANGE_BILL_DATA = "CCtoCC";
  
  public static final char PREAUTHORIZED_TRASACTION_TYPE_NEW = 'A';
  public static final char PREAUTHORIZED_TRASACTION_TYPE_UPDATE = 'U';
  public static final char PREAUTHORIZED_TRASACTION_TYPE_REMOVAL = 'R';
  public static final char PREAUTHORIZED_TRASACTION_TYPE_NOCHANGE = 'N';

  private char transactionType;
  private String paymentMethod;
  private CreditCardInfo creditCard = new CreditCardInfo();
  private ChequeInfo cheque = new ChequeInfo();
  private String status;
  private String statusReason;
  private Date startDate;
  private Date endDate;
  private boolean suppressReturnEnvelope;
  private String oldPaymentMethod;
  private String oldPaymentMethodCardNumber;



public char getTransactionType() {
	  return transactionType;
  }

  public void setTransactionType(char transactionType) {
	  this.transactionType = transactionType;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(String paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public CreditCardInfo getCreditCard0() {
    return creditCard;
  }

  public Cheque getCheque() {
    return cheque;
  }

  public ChequeInfo getCheque0() {
    return cheque;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public void setStatusReason(String statusReason) {
    this.statusReason = statusReason;
  }
  public String getStatusReason() {
    return statusReason;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getStartDate() {
    return startDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public Date getEndDate() {
    return endDate;
  }

  public boolean isPaymentMethodRegular() {
    return PAYMENT_METHOD_REGULAR.equals(paymentMethod);
  }

  public boolean isPaymentMethodDebit() {
    return PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT.equals(paymentMethod);
  }

  public boolean isPaymentMethodCreditCard() {
    return PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD.equals(paymentMethod);
  }

  public void copyFrom(PaymentMethodInfo info) {
	if ( info==null ) return;
	
    paymentMethod  = info.paymentMethod;
    creditCard.copyFrom(info.creditCard);
    cheque.copyFrom(info.cheque);
    status         = info.status;
    statusReason   = info.statusReason;
    startDate      = cloneDate(info.startDate);
    endDate        = cloneDate(info.endDate);
    suppressReturnEnvelope = info.suppressReturnEnvelope;
  }


  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PaymentMethodInfo:{\n");
    s.append("    paymentMethod=[").append(paymentMethod).append("]\n");
    s.append("    suppressReturnEnvelope=[").append(suppressReturnEnvelope).append("]\n");
    s.append("creditCard=[").append(creditCard).append("]\n");
    s.append("cheque=[").append(cheque).append("]\n");
    s.append("    status=[").append(status).append("]\n");
    s.append("    statusReason=[").append(statusReason).append("]\n");
    s.append("    startDate=[").append(startDate).append("]\n");
    s.append("    endDate=[").append(endDate).append("]\n");
    s.append("    transactionType=[").append(transactionType).append("]\n");
    s.append("    oldPaymentMethod=[").append(oldPaymentMethod).append("]\n");
    s.append("}");

    return s.toString();
  }
  public void setCheque0(ChequeInfo cheque0) {
    this.cheque = cheque0;
  }
  public void setCreditCard0(CreditCardInfo creditCard0) {
    this.creditCard = creditCard0;
  }

  public boolean getSuppressReturnEnvelope(){
    return suppressReturnEnvelope;
  }

  public void setSuppressReturnEnvelope(boolean suppressReturnEnvelope){
    this.suppressReturnEnvelope = suppressReturnEnvelope;
  }

  public void setCreditCard(CreditCard card) {
    throw new java.lang.UnsupportedOperationException("Method not implemented here.");
  }

  public void setCheque(Cheque cheque) {
    throw new java.lang.UnsupportedOperationException("Method not implemented here.");
  }
  public String getOldPaymentMethod() {
		return oldPaymentMethod;
	}

	public void setOldPaymentMethod(String oldPaymentMethod) {
		this.oldPaymentMethod = oldPaymentMethod;
	}

	public String getOldPaymentMethodCardNumber() {
		return oldPaymentMethodCardNumber;
	}

	public void setOldPaymentMethodCardNumber(String cardNumber) {
		this.oldPaymentMethodCardNumber = cardNumber;		
	}


}





