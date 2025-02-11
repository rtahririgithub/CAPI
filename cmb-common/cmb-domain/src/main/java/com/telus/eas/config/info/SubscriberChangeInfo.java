/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.SubscriberChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;


/**
  * Value (info) object for the subscriber change interaction detail information.
  *
  */
public class SubscriberChangeInfo extends ConfigurationManagerInfo implements SubscriberChange {

   static final long serialVersionUID = 1L;

  private String oldLastName = "";
  private String oldMiddleInitial = "";
  private String oldFirstName = "";
  private String oldEmailAddress = "";

  private String newLastName = "";
  private String newMiddleInitial = "";
  private String newFirstName = "";
  private String newEmailAddress = "";

  private SubscriberInfo oldSubscriber;
  private SubscriberInfo newSubscriber;
  
  /**
    * Default empty constructor
    */
  public SubscriberChangeInfo() {
  }

  /**
    * Constructs a SubscriberChangeInfo object with the given interaction details.
    *
    * @param oldLastName
    * @param oldMiddleInitial
    * @param oldFirstName
    * @param oldEmailAddress
    * @param newLastName
    * @param newMiddleInitial
    * @param newFirstName
    * @param newEmailAddress
    */
  public SubscriberChangeInfo(
    String oldLastName,
    String oldMiddleInitial,
    String oldFirstName,
    String oldEmailAddress,
    String newLastName,
    String newMiddleInitial,
    String newFirstName,
    String newEmailAddress
  ) {
    setOldLastName(oldLastName);
    setOldMiddleInitial(oldMiddleInitial);
    setOldFirstName(oldFirstName);
    setOldEmailAddress(oldEmailAddress);

    setNewLastName(newLastName);
    setNewMiddleInitial(newMiddleInitial);
    setNewFirstName(newFirstName);
    setNewEmailAddress(newEmailAddress);
  }

  /**
    * Copies the information from the dao to this object.
    *
    * @para dao -- the data source
    */
//  public void copyFrom(TmiSubsInfoTransactionDAO dao) {
//    setOldLastName(dao.getOldLastName());
//    setOldMiddleInitial(dao.getOldMiddleInitial());
//    setOldFirstName(dao.getOldFirstName());
//    setOldEmailAddress(dao.getOldEmailAddress());
//
//    setNewLastName(dao.getNewLastName());
//    setNewMiddleInitial(dao.getNewMiddleInitial());
//    setNewFirstName(dao.getNewFirstName());
//    setNewEmailAddress(dao.getNewEmailAddress());
//  }

  /**
    * Returns the type of interaction detail this class represents.
    *
    * @return String -- Always InteractionManager.TYPE_SUBSCRIBER_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_SUBSCRIBER_CHANGE;
  }

  public String getOldLastName() {
    return oldLastName;
  }

  public void setOldLastName(String oldLastName) {
    this.oldLastName = oldLastName;
  }

  public String getOldMiddleInitial() {
    return oldMiddleInitial;
  }

  public void setOldMiddleInitial(String oldMiddleInitial) {
    this.oldMiddleInitial = oldMiddleInitial;
  }

  public String getOldFirstName() {
    return oldFirstName;
  }

  public void setOldFirstName(String oldFirstName) {
    this.oldFirstName = oldFirstName;
  }

  public String getOldEmailAddress() {
    return oldEmailAddress;
  }

  public void setOldEmailAddress(String oldEmailAddress) {
    this.oldEmailAddress = oldEmailAddress;
  }

  public String getNewLastName() {
    return newLastName;
  }

  public void setNewLastName(String newLastName) {
    this.newLastName = newLastName;
  }

  public String getNewMiddleInitial() {
    return newMiddleInitial;
  }

  public void setNewMiddleInitial(String newMiddleInitial) {
    this.newMiddleInitial = newMiddleInitial;
  }

  public String getNewFirstName() {
    return newFirstName;
  }

  public void setNewFirstName(String newFirstName) {
    this.newFirstName = newFirstName;
  }

  public String getNewEmailAddress() {
    return newEmailAddress;
  }

  public void setNewEmailAddress(String newEmailAddress) {
    this.newEmailAddress = newEmailAddress;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_SUBSCRIBER_CHANGE;
	}
	
	public SubscriberInfo getOldSubscriber() {
		return oldSubscriber;
	}
	
	public void setOldSubscriber(SubscriberInfo oldSubscriber) {
		this.oldSubscriber = oldSubscriber;
	}
	
	public SubscriberInfo getNewSubscriber() {
		return newSubscriber;
	}
	
	public void setNewSubscriber(SubscriberInfo newSubscriber) {
		this.newSubscriber = newSubscriber;
	}
}