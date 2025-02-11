/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.BillPayment;
import com.telus.api.interaction.InteractionManager;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;


/**
  * Value (info) object for the payment interaction detail information.
  *
  */
public class BillPaymentInfo extends ConfigurationManagerInfo implements BillPayment {
   static final long serialVersionUID = 1L;

  private char paymentMethod = '\0';
  private double paymentAmount = 0.0;

  /**
    * Default empty constructor
    */
  public BillPaymentInfo() {
  }

  /**
    * Constructs a BillPaymentInfo with the given interaction details.
    *
    * @param paymentMethod
    * @param paymentAmount
    */
  public BillPaymentInfo(char paymentMethod, double paymentAmount) {
    setPaymentMethod(paymentMethod);
    setPaymentAmount(paymentAmount);
  }

  /**
    * Copies the information from the dao to this object.
    *
    * @param dao
    */
//  public void copyFrom(TmiPaymentTransactionDAO dao) {
//    setPaymentMethod(dao.getPaymentMethod());
//    setPaymentAmount(dao.getPaymentAmount());
//  }

  /**
    * Returns the type of interaction detail.
    *
    * @return String -- Always InteractionManager.TYPE_BILL_PAYMENT
    */
  public String getType() {
    return InteractionManager.TYPE_BILL_PAYMENT;
  }

  public char getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(char paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public double getPaymentAmount() {
    return paymentAmount;
  }

  public void setPaymentAmount(double paymentAmount) {
    this.paymentAmount = paymentAmount;
  }

  public String getMessageType() {
	return MESSAGE_TYPE_BILL_PAYMENT;
  }
}