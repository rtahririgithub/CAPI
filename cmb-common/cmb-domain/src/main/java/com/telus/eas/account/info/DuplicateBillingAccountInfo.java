/**
 * Title:        DuplicateBillingAccountInfo<p>
 * Description:  The DuplicateBillingAccountInfo holds the results of a duplicate account check.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */
package com.telus.eas.account.info;

import com.telus.eas.framework.info.*;

public class DuplicateBillingAccountInfo extends Info {

	 static final long serialVersionUID = 1L;

  private int ban = 0;
  private String firstName = "";
  private String lastName = "";
  private String fullName = "";
  private String accountType = "";
  private String accountSubType = "";
  private String businessType = "";
  private String banStatus = "";
  private int customerSIN = 0;
  private int customerID = 0;
  private String incorporationNumber = "";
  private String incorporationDate = "";
  private String companyName = "";
  private String compositeAddress = "";

  public DuplicateBillingAccountInfo(){}

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
  public void setCustomerSIN(int newCustomerSIN) {
    customerSIN = newCustomerSIN;
  }
  public int getCustomerSIN() {
    return customerSIN;
  }
  public void setCustomerID(int newCustomerID) {
    customerID = newCustomerID;
  }
  public int getCustomerID() {
    return customerID;
  }
  public void setIncorporationNumber(String newIncorporationNumber) {
    incorporationNumber = newIncorporationNumber;
  }
  public String getIncorporationNumber() {
    return incorporationNumber;
  }
  public void setIncorporationDate(String newIncorporationDate) {
    incorporationDate = newIncorporationDate;
  }
  public String getIncorporationDate() {
    return incorporationDate;
  }
  public void setCompanyName(String newCompanyName) {
    companyName = newCompanyName;
  }
  public String getCompanyName() {
    return companyName;
  }
  public void setCompositeAddress(String newCompositeAddress) {
    compositeAddress = newCompositeAddress;
  }
  public String getCompositeAddress() {
    return compositeAddress;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("DuplicateBillingAccountInfo:{\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    firstName=[").append(firstName).append("]\n");
    s.append("    lastName=[").append(lastName).append("]\n");
    s.append("    fullName=[").append(fullName).append("]\n");
    s.append("    accountType=[").append(accountType).append("]\n");
    s.append("    accountSubType=[").append(accountSubType).append("]\n");
    s.append("    businessType=[").append(businessType).append("]\n");
    s.append("    banStatus=[").append(banStatus).append("]\n");
    s.append("    customerSIN=[").append(customerSIN).append("]\n");
    s.append("    customerID=[").append(customerID).append("]\n");
    s.append("    incorporationNumber=[").append(incorporationNumber).append("]\n");
    s.append("    incorporationDate=[").append(incorporationDate).append("]\n");
    s.append("    companyName=[").append(companyName).append("]\n");
    s.append("    compositeAddress=[").append(compositeAddress).append("]\n");
    s.append("}");

    return s.toString();
  }

}
