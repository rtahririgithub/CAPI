/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.PaymentMethodChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;

/**
  * Value (info) object for the update payment method interaction detail information.
  *
  */
public class PaymentMethodChangeInfo extends ConfigurationManagerInfo implements PaymentMethodChange {

  static final long serialVersionUID = 1L;

  private char oldPaymentMethod = '\0';
  private char newPaymentMethod = '\0';

  /**
    * Default empty constructor
    */
  public PaymentMethodChangeInfo() {
  }

  /**
    * Constructs a PaymentMethodChangeInfo with the given interaction details.
    *
    * @param oldPaymentMethod
    * @param newPaymentMethod
    */
  public PaymentMethodChangeInfo(char oldPaymentMethod, char newPaymentMethod) {
    setOldPaymentMethod(oldPaymentMethod);
    setNewPaymentMethod(newPaymentMethod);
  }

  /**
    * Copies the information in dao to this object.
    *
    * @param dao -- The data source.
    */
//  public void copyFrom(TmiPaymentMethodTransactionDAO dao) {
//    setOldPaymentMethod(dao.getOldPaymentMethod());
//    setNewPaymentMethod(dao.getNewPaymentMethod());
//  }

  /**
    * Returns the type of interaction detail this object represents.
    *
    * @return String -- Always InteractionManager.TYPE_PAYMENT_METHOD_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_PAYMENT_METHOD_CHANGE;
  }

  public char getOldPaymentMethod() {
    return oldPaymentMethod;
  }

  public void setOldPaymentMethod(char oldPaymentMethod) {
    this.oldPaymentMethod = oldPaymentMethod;
  }

  public char getNewPaymentMethod() {
    return newPaymentMethod;
  }

  public void setNewPaymentMethod(char newPaymentMethod) {
    this.newPaymentMethod = newPaymentMethod;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_PAYMENT_METHOD_CHANGE;
	}
}
