package com.telus.eas.config1.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 *
 * @version 1.0
 */



  public class ActivationLogInfo implements java.io.Serializable {

  static final long serialVersionUID = 1L;

  private String statusCD=" ";
  private long clientID=0;
  private long dealerID=0;
  private long customerID=0;
  private String activationData=" ";
  private String userID=" ";

  public ActivationLogInfo() {}

  public String getStatusCD() {
    return statusCD;
  }

  public void setStatusCD(String newStatusCD) {
    statusCD = newStatusCD;
  }

  public long getClientID() {
  return clientID;
  }

  public void setClientID(long newClientID) {
    clientID = newClientID;
  }

  public long getDealerID() {
  return dealerID;
  }

  public void setDealerID(long newDealerID) {
    dealerID = newDealerID;
  }

  public long getCustomerID() {
  return customerID;
  }

  public void setCustomerID(long newCustomerID) {
    customerID = newCustomerID;
  }


  public String getActivationData() {
    return activationData;
  }

  public void setActivationData(String newActivationData) {
    activationData = newActivationData;
  }


   public String getUserID() {
    return userID;
  }

  public void setUserID(String newUserID) {
    userID = newUserID;
  }
}