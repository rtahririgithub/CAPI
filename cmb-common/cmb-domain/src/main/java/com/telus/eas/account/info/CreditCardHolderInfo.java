package com.telus.eas.account.info;

/**
 * Title:        Credit card Holder Info Class
 * Description:  The aim of the CreditCardHolderInfo class is to represent card holder's data
 * Copyright:    Copyright (c) 2003
 * Company:      Telus Mobility
 * @author Stefan Pavlov
 * @version 1.0
 */

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class CreditCardHolderInfo  extends Info  {

  static final long serialVersionUID = 1L;

  private String clientID;            // BAN or any client ID
  private String firstName; 	      // Card holder's first name
  private String lastName;	      // Card holder's last name
  private String civicStreetNumber;   // Card holder's civic or street number
  private String streetName;          // Card holder's street name
  private String apartmentNumber;     // Card holder's civic apartment number
  private String city;	              // Card holder's city
  private String province;            // Card holder's province
  private String postalCode;          // Card holder's postal code
  private String homePhone;           // Card holder's home phone
  private Date   birthDate;           // Card holder's birth date
  private String businessRole;        // Business role of the client. It can be:
                                      // 'client', 'dealer', ...
  private Date  activationDate;       // BAN activation date
  private String accountType;         // BAN account type
  private String accountSubType;      // BAN account sub type

  public CreditCardHolderInfo() {
     clientID  = "";
     firstName = "";
     lastName  = "";
     civicStreetNumber = "";
     streetName = "";
     apartmentNumber = "";
     city = "";
     province = "";
     postalCode = "";
     homePhone = "";
     birthDate = null;
     accountType = "";
     accountSubType = "";
  }



  // Client ID
  public String getClientID() {
    return clientID;
  }
  public void setClientID(String clientID) {
    this.clientID = clientID;
  }

  // Card holder's first name
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  // Card holder's last name
  public String getLastName() {
    return lastName;
  }
  public void setlastName(String lastName) {
    this.lastName = lastName;
  }
  // Card holder's civic or street number
  public String getCivicStreetNumber() {
    return civicStreetNumber;
  }
  public void setCivicStreetNumber(String civicStreetNumber) {
    this.civicStreetNumber = civicStreetNumber;
  }
  // Card holder's street name
  public String getStreetName() {
    return streetName;
  }
  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }
  // Card holder's civic apartment number
  public String getApartmentNumber() {
    return apartmentNumber;
  }
  public void setApartmentNumber(String apartmentNumber) {
    this.apartmentNumber = apartmentNumber;
  }
  // Card holder's city
  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }
  // Card holder's province
  public String getProvince() {
    return province;
  }
  public void setProvince(String province) {
    this.province = province;
  }
  // Card holder's postal code
  public String getPostalCode() {
    return postalCode;
  }
  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }
  // Card holder's home phone
  public String getHomePhone() {
    return homePhone;
  }
  public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
  }
  // Card holder's birth date
  public Date getBirthDate() {
    return birthDate;
  }
  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  // Card Holder business role: client/dealer ...
  public String getBusinessRole() {
    return businessRole;
  }
  public void setBusinessRole(String businessRole) {
    this.businessRole = businessRole;
  }

  // Activation date
  public Date getActivationDate() {
    return activationDate;
  }
  public void setActivationDate(Date activationDate) {
    this.activationDate = activationDate;
  }

   public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CreditCardHolderInfo:{\n");
    s.append("    clientID=[").append(clientID).append("]\n");
    s.append("    firstName=[").append(firstName).append("]\n");
    s.append("    lastName=[").append(lastName).append("]\n");
    s.append("    birthDate=[").append(birthDate).append("]\n");
    s.append("    streetName=[").append(streetName).append("]\n");
    s.append("    apartmentNumber=[").append(apartmentNumber).append("]\n");
    s.append("    civicStreetNumber=[").append(civicStreetNumber).append("]\n");
    s.append("    postalCode=[").append(postalCode).append("]\n");
    s.append("    city=[").append(city).append("]\n");
    s.append("    homePhone=[").append(homePhone).append("]\n");
    s.append("    buisnessRole=[").append(businessRole).append("]\n");
    s.append("    activationDate=[").append(activationDate).append("]\n");
    s.append("    accountType=[").append(accountType).append("]\n");
    s.append("    accountSubType=[").append(accountSubType).append("]\n");
    s.append("}");

    return s.toString();
  }
  public String getAccountType() {
    return accountType;
  }
  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }
  public String getAccountSubType() {
    return accountSubType;
  }
  public void setAccountSubType(String accountSubType) {
    this.accountSubType = accountSubType;
  }


}