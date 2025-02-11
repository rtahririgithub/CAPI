/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.SubscriberCharge;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;

/**
  * Value (info) object for the subscriber charge interaction detail information.
  *
  */
public class SubscriberChargeInfo extends ConfigurationManagerInfo implements SubscriberCharge {

   static final long serialVersionUID = 1L;

  private String chargeCode = "";
  private String waiverCode = "";

  /**
    * Default empty constructor
    */
  public SubscriberChargeInfo() {
  }

  /**
    * Constructs a SubscriberChargeInfo object with the given interaction detail.
    *
    * @param chargeCode
    * @param waiverCode
    */
  public SubscriberChargeInfo(String chargeCode, String waiverCode) {
    setChargeCode(chargeCode);
    setWaiverCode(waiverCode);
  }

  /**
    * Copies the information from the dao to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiChargeDAO dao) {
//    setChargeCode(dao.getChargeCode());
//    setWaiverCode(dao.getWaiverCode());
//  }

  /**
    * Returns the type of interaction detail this class represents.
    *
    * @return String -- Always InteractionManager.TYPE_SUBSCRIBER_CHARGE
    */
  public String getType() {
    return InteractionManager.TYPE_SUBSCRIBER_CHARGE;
  }

  public String getChargeCode() {
    return chargeCode;
  }

  public void setChargeCode(String chargeCode) {
    this.chargeCode = chargeCode;
  }

  public String getWaiverCode() {
    return waiverCode;
  }

  public void setWaiverCode(String waiverCode) {
    this.waiverCode = waiverCode;
  }

	public String getMessageType() {
		return MESSAGE_TYPE_SUBSCRIBER_CHARGE;
	}
}