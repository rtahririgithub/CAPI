package com.telus.eas.config.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * 
 * @version 1.0
 */

 public class LogInfo implements java.io.Serializable {

   static final long serialVersionUID = 1L;

  private long transactionID=0;
  private String className=" ";
  private String operation=" ";
  private long customerID=0;
  private long alternateID=0;
  private String appMessage=" ";
  private String transactionTypeCD=" ";
  private String userID;


  public LogInfo() {}

   public long getTransactionID() {
    return transactionID;
  }

  public void setTransactionID(long newTransactionID) {
    transactionID = newTransactionID;
  }


  public String getClassName() {
    return className;
  }

  public void setClassName(String newClassName) {
    className = newClassName;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String newOperation) {
    operation = newOperation;
  }

   public long getCustomerID() {
    return customerID;
  }

  public void setCustomerID(long newCustomerID) {
    customerID = newCustomerID;
  }

   public long getAlternateID() {
    return alternateID;
  }

  public void setAlternateID(long newAlternateID) {
    alternateID = newAlternateID;
  }

  public String getAppMessage() {
    return  appMessage;
  }

  public void setAppMessage(String newAppMessage) {
    appMessage = newAppMessage;
  }

  public String getTransactionTypeCD() {
    return transactionTypeCD;
  }

  public void setTransactionTypeCD(String newTransactionTypeCD) {
    transactionTypeCD = newTransactionTypeCD;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String newUserID) {
    userID = newUserID;
  }

}
