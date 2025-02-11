/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.PrepaidTopup;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;

/**
  * Value (info) object for the prepaid top up interaction detail information.
  *
  */
public class PrepaidTopupInfo extends ConfigurationManagerInfo implements PrepaidTopup {

  static final long serialVersionUID = 1L;

  private double amount = 0.0;
  private char cardType = '\0';
  private char topUpType = '\0';

  /**
    * default empty constructor
    */
  public PrepaidTopupInfo() {
  }

  /**
    * Constructs a PrepaidTopupInfo object with the given interaction details.
    *
    * @param amount
    * @param cardType
    * @param topUpType
    */
  public PrepaidTopupInfo(double amount, char cardType, char topUpType) {
    setAmount(amount);
    setCardType(cardType);
    setTopUpType(topUpType);
  }

  /**
    * Copies the information from dao to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiTopUpTransactionDAO dao) {
//    setAmount(dao.getAmount());
//    setCardType(dao.getCardType());
//    setTopUpType(dao.getTopUpType());
//  }

  /**
    * Returns the type of interaction detail this class represents.
    *
    * @return String -- Always InteractionManager.TYPE_PREPAID_TOPUP
    */
  public String getType() {
    return InteractionManager.TYPE_PREPAID_TOPUP;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setAmount(Double amount) {
    if(amount == null)
      setAmount(0.0);
    else
      setAmount(amount.doubleValue());
  }

  public char getCardType() {
    return cardType;
  }

  public void setCardType(char cardType) {
    this.cardType = cardType;
  }

  public char getTopUpType() {
    return topUpType;
  }

  public void setTopUpType(char topUpType) {
    this.topUpType = topUpType;
  }

public String getMessageType() {
	return MESSAGE_TYPE_PREPAID_TOPUP;
}
}