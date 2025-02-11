/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.PhoneNumberChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;


/**
  * Value (info) object for the phone number change interaction detail information.
  *
  */
public class PhoneNumberChangeInfo extends ConfigurationManagerInfo implements PhoneNumberChange {

  static final long serialVersionUID = 1L;

  private String oldPhoneNumber = "";
  private String newPhoneNumber = "";

  /**
    * Default empty constructor
    */
  public PhoneNumberChangeInfo() {
  }

  /**
    * Constructs a PhoneNumberChangeInfo object with the given interaction details.
    *
    * @param oldPhoneNumber
    * @param newPhoneNumber
    */
  public PhoneNumberChangeInfo(String oldPhoneNumber, String newPhoneNumber) {
    setOldPhoneNumber(oldPhoneNumber);
    setNewPhoneNumber(newPhoneNumber);
  }

  /**
    * Copies the information from the given dao object to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiPhoneNumberTransactionDAO dao) {
//    setOldPhoneNumber(dao.getOldPhoneNumber());
//    setNewPhoneNumber(dao.getNewPhoneNumber());
//  }

  /**
    * Returns the type of interaction detail represented by this object.
    *
    * @return String -- Always InteractionManager.TYPE_PHONE_NUMBER_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_PHONE_NUMBER_CHANGE;
  }

  public String getNewPhoneNumber() {
    return newPhoneNumber;
  }

  public void setNewPhoneNumber(String newPhoneNumber) {
    this.newPhoneNumber = newPhoneNumber;
  }

  public String getOldPhoneNumber() {
    return oldPhoneNumber;
  }

  public void setOldPhoneNumber(String oldPhoneNumber) {
    this.oldPhoneNumber = oldPhoneNumber;
  }

public String getMessageType() {
	return MESSAGE_TYPE_PHONE_NUMBER_CHANGE;
}
}