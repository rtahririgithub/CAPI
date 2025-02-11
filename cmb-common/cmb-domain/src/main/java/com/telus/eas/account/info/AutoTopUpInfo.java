/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.*;
import java.util.*;
import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class AutoTopUpInfo extends Info implements AutoTopUp {

	 static final long serialVersionUID = 1L;

  public static final String AUTO_TOPUP_TRANSACTION_TYPE_INSERT = "I";
  public static final String AUTO_TOPUP_TRANSACTION_TYPE_UPDATE = "U";
  public static final String AUTO_TOPUP_TRANSACTION_TYPE_DELETE = "D";

  private double chargeAmount;
  private Date nextChargeDate;
  private boolean hasThresholdRecharge = false;
  private double thresholdAmount;
  private int ban;
  private String phoneNumber;
  private boolean isExistingAutoTopUp;


  public AutoTopUpInfo() {
  }

  public double getChargeAmount() {
    return chargeAmount;
  }

  public void setChargeAmount(double chargeAmount) {
    this.chargeAmount = chargeAmount;
  }

  public Date getNextChargeDate() {
    return nextChargeDate;
  }

  public boolean hasThresholdRecharge() {
    return hasThresholdRecharge;
  }

  public void setHasThresholdRecharge(boolean value) {
    this.hasThresholdRecharge = value;
    thresholdAmount = hasThresholdRecharge ? thresholdAmount : 0;
  }

  public double getThresholdAmount() {
    return thresholdAmount;
  }

  public void setThresholdAmount(double value) {
    this.thresholdAmount = value;
  }

  public void setNextChargeDate(Date nextChargeDate) {
    this.nextChargeDate = nextChargeDate;
  }

  public void setBan(int ban) {
    this.ban = ban;
  }

  public int getBan() {
    return ban;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String newPhoneNumber) {
    phoneNumber = newPhoneNumber;
  }

  public boolean isSubscriberLevel() {
    return phoneNumber != null && phoneNumber.trim().length() > 0;
  }
  
  public boolean isExistingAutoTopUp() {
	  return isExistingAutoTopUp;
  }

  public void setIsExistingAutoTopUp(boolean value) {
	  this.isExistingAutoTopUp = value;
  }

  public void apply() throws TelusAPIException {
    throw new UnsupportedOperationException("method not implemented here");
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append("AutoTopUpInfo:[\n");
    s.append("    chargeAmount=[").append(chargeAmount).append("]\n");
    s.append("    nextChargeDate=[").append(nextChargeDate).append("]\n");
    s.append("    hasThresholdRecharge=[").append(hasThresholdRecharge).append("]\n");
    s.append("    thresholdAmount=[").append(thresholdAmount).append("]\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
    s.append("    isExistingAutoTopUp=[").append(isExistingAutoTopUp).append("]\n");
    s.append("]");

    return s.toString();
  }
  
  public void copyFrom(AutoTopUpInfo o) {
	  if (o != null) {
		  this.setBan(o.getBan());
		  this.setChargeAmount(o.getChargeAmount());
		  this.setHasThresholdRecharge(o.hasThresholdRecharge());
		  this.setIsExistingAutoTopUp(o.isExistingAutoTopUp());
		  this.setNextChargeDate(o.getNextChargeDate());
		  this.setPhoneNumber(o.getPhoneNumber());
		  this.setThresholdAmount(o.getThresholdAmount());
	  }
  }

}




