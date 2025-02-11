package com.telus.eas.subscriber.info;

import com.telus.api.account.PCSFidoSubscriber;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

@Deprecated
public class PCSFidoSubscriberInfo extends PCSSubscriberInfo implements PCSFidoSubscriber {

  static final long serialVersionUID = 1L;

  private String serialNumber;
  private String simCardNumber;
  private String phoneNumber;
  private String accountId;
  private String firstName;
  private String lastName;
  private int accountType;
  private int phoneNumberHoldDays;

  public PCSFidoSubscriberInfo() {
  }

  public String getFidoSerialNumber() {
    return serialNumber;
  }

  public void setFidoSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getFidoSimCard() {
    return simCardNumber;
  }

  public void setFidoSimCard(String simCardNumber) {
    this.simCardNumber = simCardNumber;
  }

  public String getFidoPhoneNumber() {
    return phoneNumber;
  }

  public void setFidoPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getFidoAccountId() {
    return accountId;
  }

  public void setFidoAccountId(String accountId) {
    this.accountId = accountId;
  }

  public String getFidoFirstName() {
    return firstName;
  }

  public void setFidoFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFidoLastName() {
    return lastName;
  }

  public void setFidoLastName(String lastName) {
    this.lastName = lastName;
  }

  public int getFidoAccountType() {
    return accountType;
  }

  public void setFidoAccountType(int accountType) {
    this.accountType = accountType;
  }

  public int getFidoPhoneNumberHoldDays() {
    return phoneNumberHoldDays;
  }

  public void setFidoPhoneNumberHoldDays(int phoneNumberHoldDays) {
    this.phoneNumberHoldDays = phoneNumberHoldDays;
  }

}
