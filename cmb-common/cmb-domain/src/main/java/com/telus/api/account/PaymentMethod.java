/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import java.util.Date;


/**
 * <CODE>PaymentMethod</CODE>
 *
 */
public interface PaymentMethod {
  public static final String PAYMENT_METHOD_REGULAR                   = "R";
  public static final String PAYMENT_METHOD_PRE_AUTHORIZED_PAYMENT    = "D";
  public static final String PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD = "C";
  
 
 
  String getPaymentMethod();
  
  void setPaymentMethod(String paymentMethod);

  /**
   *
   * @link aggregation
   */
  CreditCard getCreditCard();

  /**
   *
   * @link aggregation
   */
  Cheque getCheque();

  String getStatus();
  void setStatus(String status);

  String getStatusReason();
  void setStatusReason(String statusReason);

  Date getStartDate();
  void setStartDate(Date startDate);

  Date getEndDate();
  void setEndDate(Date endDate);

  boolean isPaymentMethodRegular();
  boolean isPaymentMethodDebit();
  boolean isPaymentMethodCreditCard();

  boolean getSuppressReturnEnvelope();

  void setSuppressReturnEnvelope(boolean suppressReturnEnvelope) throws TelusAPIException;

  /**
   * Set credit card
   * @param card CreditCard
   */
  void setCreditCard(CreditCard card);

  /**
   * Set cheque
   * @param cheque Cheque
   */
  void setCheque(Cheque cheque);

}


