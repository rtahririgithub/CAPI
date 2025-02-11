/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.AccountStatusChange;
import com.telus.api.interaction.InteractionManager;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;

/**
  * Value (info) object for the Account Status Change interaction detail information.
  *
  */
public class AccountStatusChangeInfo extends ConfigurationManagerInfo implements AccountStatusChange {

	static final long serialVersionUID = 1L;

  private char statusFlag = '\0';
  private char oldStatus = '\0';
  private char oldHotlinedInd = '\0';
  private char newStatus = '\0';
  private char newHotlinedInd = '\0';

  /**
    * Default empty constructor
    */
  public AccountStatusChangeInfo() {
  }

  /**
    * Creates an AccountStatusChangeInfo object with the given details.
    *
    * @param statusFlag
    * @param oldStatus
    * @param oldHotlinedInd
    * @param newStatus
    * @param newHotlinedInd
    */
  public AccountStatusChangeInfo(char statusFlag, char oldStatus, char oldHotlinedInd, char newStatus, char newHotlinedInd) {
    setStatusFlag(statusFlag);
    setOldStatus(oldStatus);
    setOldHotlinedInd(oldHotlinedInd);
    setNewStatus(newStatus);
    setNewHotlinedInd(newHotlinedInd);
  }

  /**
    * Copies the information for this object from the given dao.
    *
    * @param dao
    */
//  public void copyFrom(TmiStatusChangeDAO dao) {
//    setStatusFlag(dao.getStatusFlag());
//    setOldStatus(dao.getOldStatus());
//    setOldHotlinedInd(dao.getOldHotlinedInd());
//    setNewStatus(dao.getNewStatus());
//    setNewHotlinedInd(dao.getNewHotlinedInd());
//  }

  /**
    * Returns the interaction detail type code for this interaction object.
    *
    * @return String -- Always InteractionManager.TYPE_ACCOUNT_STATUS_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_ACCOUNT_STATUS_CHANGE;
  }

  public char getStatusFlag() {
    return statusFlag;
  }

  public void setStatusFlag(char statusFlag) {
    this.statusFlag = statusFlag;
  }

  public char getOldStatus() {
    return oldStatus;
  }

  public void setOldStatus(char oldStatus) {
    this.oldStatus = oldStatus;
  }

  public char getOldHotlinedInd() {
    return oldHotlinedInd;
  }

  public void setOldHotlinedInd(char oldHotlinedInd) {
    this.oldHotlinedInd = oldHotlinedInd;
  }

  public char getNewStatus() {
    return newStatus;
  }

  public void setNewStatus(char newStatus) {
    this.newStatus = newStatus;
  }

  public char getNewHotlinedInd() {
    return newHotlinedInd;
  }

  public void setNewHotlinedInd(char newHotlinedInd) {
    this.newHotlinedInd = newHotlinedInd;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_ACCOUNT_STATUS_CHANGE;
	}
}