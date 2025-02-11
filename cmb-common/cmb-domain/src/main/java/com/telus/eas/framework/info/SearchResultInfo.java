/**
 * Title:        SearchResultInfo<p>
 * Description:  The SearchResultInfo holds the results of a search.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.framework.info;

import java.util.*;

import com.telus.eas.account.info.*;

public class SearchResultInfo extends Info {

  static final long serialVersionUID = 1L;

  public SearchResultInfo(){}
  private int ban = 0;
  private String firstName = "";
  private String lastName = "";
  private String fullName = "";
  private String accountType = "";
  private String accountSubType = "";
  private String businessType = "";
  private String banStatus = "";
  private String subscriberStatus = "";
  private Date subscriberStatusDate = new Date();
  private Date banstatusLastDate = new Date();
  private String banType = "";
  private String equipmentType = "";
  private AddressInfo addressInfo = new AddressInfo();
  private int customerSIN = 0;

  public int getBan() {
    return ban;
  }
  public void setBan(int newBan) {
    ban = newBan;
  }
  public void setFirstName(String newFirstName) {
    firstName = newFirstName;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setLastName(String newLastName) {
    lastName = newLastName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setFullName(String newFullName) {
    fullName = newFullName;
  }
  public String getFullName() {
    return fullName;
  }
  public void setAccountType(String newAccountType) {
    accountType = newAccountType;
  }
  public String getAccountType() {
    return accountType;
  }
  public void setAccountSubType(String newAccountSubType) {
    accountSubType = newAccountSubType;
  }
  public String getAccountSubType() {
    return accountSubType;
  }
  public void setBusinessType(String newBusinessType) {
    businessType = newBusinessType;
  }
  public String getBusinessType() {
    return businessType;
  }
  public void setBanStatus(String newBanStatus) {
    banStatus = newBanStatus;
  }
  public String getBanStatus() {
    return banStatus;
  }
  public void setBanstatusLastDate(java.util.Date newBanstatusLastDate) {
    banstatusLastDate = newBanstatusLastDate;
  }
  public java.util.Date getBanstatusLastDate() {
    return banstatusLastDate;
  }
  public void setSubscriberStatus(String newSubscriberStatus) {
    subscriberStatus = newSubscriberStatus;
  }
  public String getSubscriberStatus() {
    return subscriberStatus;
  }
  public void setSubscriberStatusDate(java.util.Date newSubscriberStatusDate) {
    subscriberStatusDate = newSubscriberStatusDate;
  }
  public java.util.Date getSubscriberStatusDate() {
    return subscriberStatusDate;
  }
  public void setCustomerSIN(int newCustomerSIN) {
    customerSIN = newCustomerSIN;
  }
  public int getCustomerSIN() {
    return customerSIN;
  }
  public void setBanType(String newBanType) {
    banType = newBanType;
  }
  public String getBanType() {
    return banType;
  }
  public void setEquipmentType(String newEquipmentType) {
    equipmentType = newEquipmentType;
  }
  public String getEquipmentType() {
    return equipmentType;
  }
  public void setAddressInfo(AddressInfo newAddressInfo) {
    addressInfo = newAddressInfo;
  }
  public AddressInfo getAddressInfo() {
    return addressInfo;
  }

}
