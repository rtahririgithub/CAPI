/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.PricePlanChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

/**
  * Value (info) object for the price plan change interaction detail information.
  *
  */
public class PricePlanChangeInfo extends ConfigurationManagerInfo implements PricePlanChange {

  static final long serialVersionUID = 1L;

  private String oldPricePlanCode = "";
  private String newPricePlanCode = "";
  private ServiceAgreementInfo[] services;

  /**
    * Default emtpy constructor
    */
  public PricePlanChangeInfo() {
  }

  /**
    * Constructs a PricePlanChangeInfo with the given interaction details.
    *
    * @param oldPricePlanCode
    * @param newPricePlanCode
    */
  public PricePlanChangeInfo(String oldPricePlanCode, String newPricePlanCode) {
    setOldPricePlanCode(oldPricePlanCode);
    setNewPricePlanCode(newPricePlanCode);
  }

  /**
    * Copies the information in dao to this object.
    *
    * @param dao -- The data source
    */
//  public void copyFrom(TmiPricePlanTransactionDAO dao) {
//    setOldPricePlanCode(dao.getKbSocOld());
//    setNewPricePlanCode(dao.getKbSocNew());
//  }

  /**
    * Returns the type of interaction detail this class represents.
    *
    * @return String -- Always InteractionManager.TYPE_PRICE_PLAN_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_PRICE_PLAN_CHANGE;
  }

  public String getOldPricePlanCode() {
    return oldPricePlanCode;
  }

  public void setOldPricePlanCode(String oldPricePlanCode) {
    this.oldPricePlanCode = oldPricePlanCode;
  }

  public String getNewPricePlanCode() {
    return newPricePlanCode;
  }

  public void setNewPricePlanCode(String newPricePlanCode) {
    this.newPricePlanCode = newPricePlanCode;
  }
  
  	public ServiceAgreementInfo[] getServiceAgreementInfo() {
		return services;
	}
	
	public void setServiceAgreementInfo(ServiceAgreementInfo[] services) {
			this.services = services;
	}
	
	public String getMessageType() {
		return MESSAGE_TYPE_PRICE_PLAN_CHANGE;
	}
}