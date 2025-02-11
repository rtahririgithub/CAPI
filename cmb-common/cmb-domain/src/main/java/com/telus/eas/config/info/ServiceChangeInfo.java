/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.interaction.InteractionManager;
import com.telus.api.interaction.ServiceChange;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;

/**
  * Value (info) object for the service change interaction detail information.
  *
  */
public class ServiceChangeInfo extends ConfigurationManagerInfo implements ServiceChange {

   static final long serialVersionUID = 1L;

  private String serviceCode = "";
  private char action = '\0';
  private ServiceAgreementInfo[] services;
   

/**
    * Default empty constructor
    */
  public ServiceChangeInfo() {
  }

  /**
    * Constructs a ServiceChangeInfo object with the given interaction details.
    *
    * @param serviceCode
    * @param action -- Must be one of the ACTION_XXX constants in ServiceChange (It is not validated here).
    */
  public ServiceChangeInfo(String serviceCode, char action) {
    setServiceCode(serviceCode);
    setAction(action);
  }

  /**
    * Copies the information from dao to this object.
    *
    * @param dao -- The data source.
    */
//  public void copyFrom(TmiServiceTransactionDAO dao) {
//    setServiceCode(dao.getKbSoc());
//    setAction(dao.getAction());
//  }

  /**
    * Returns the type of interaction of this service detail.
    *
    * @return String -- Always InteractionManager.TYPE_SERVICE_CHANGE
    */
  public String getType() {
    return InteractionManager.TYPE_SERVICE_CHANGE;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  /**
   * Returns one of the ACTION_xxx constants.
   *
   */
  public char getAction() {
    return action;
  }

  /**
    * Must be one of the ACTION_XXX constants in ServiceChange.
    *
    * Note that the parameter is not validated here.
    *
    * @param action
    */
  public void setAction(char action) {
    this.action = action;
  }
  
  
  public ServiceAgreementInfo[] getServices() {
		return services;
	}

  public void setServices(ServiceAgreementInfo[] services) {
		this.services = services;
  }

  public String getMessageType() {
	return MESSAGE_TYPE_SERVICE_CHANGE;
  }



}