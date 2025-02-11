package com.telus.eas.account.info;
/**
 * Title:        PaymentInfo<p>
 * Description:  The PaymentInfo holds all necessary payment information to apply the payment/deposit to a billing account.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import java.util.*;
import com.telus.eas.framework.info.*;

public class PaymentInfo extends Info {

   static final long serialVersionUID = 1L;

  private int ban = 0;
  private boolean depositPaymentIndicator = false;
  private double amount = 0;
  private Date depositDate = new Date();
  private String paymentSourceType = "";
  private String paymentSourceID = "";
  private boolean allowOverpayment = false;
  private String paymentMethod  = "";
  private CreditCardInfo creditCardInfo = new CreditCardInfo();
  private ChequeInfo chequeInfo = new ChequeInfo();
  private String authorizationNo = "";

  public PaymentInfo(){}

  public boolean isDepositPaymentIndicator() {
    return depositPaymentIndicator;
  }
  public void setDepositPaymentIndicator(boolean newDepositPaymentIndicator) {
    depositPaymentIndicator = newDepositPaymentIndicator;
  }
  public double getAmount() {
    return amount;
  }
  public void setAmount(double newAmount) {
    amount = newAmount;
  }
  public java.util.Date getDepositDate() {
    return depositDate;
  }
  public void setDepositDate(java.util.Date newDepositDate) {
    depositDate = newDepositDate;
  }
  public String getPaymentSourceType() {
    return paymentSourceType;
  }
  public void setPaymentSourceType(String newPaymentSourceType) {
    paymentSourceType = newPaymentSourceType;
  }
  public String getPaymentSourceID() {
    return paymentSourceID;
  }
  public void setPaymentSourceID(String newPaymentSourceID) {
    paymentSourceID = newPaymentSourceID;
  }
  public boolean isAllowOverpayment() {
    return allowOverpayment;
  }
  public void setAllowOverpayment(boolean newAllowOverpayment) {
    allowOverpayment = newAllowOverpayment;
  }
  public String getPaymentMethod() {
    return paymentMethod;
  }
  public void setPaymentMethod(String newPaymentMethod) {
    paymentMethod = newPaymentMethod;
  }
  public CreditCardInfo getCreditCardInfo() {
    return creditCardInfo;
  }
  public void setCreditCardInfo(CreditCardInfo newCreditCardInfo) {
    creditCardInfo = newCreditCardInfo;
  }
  public ChequeInfo getChequeInfo() {
    return chequeInfo;
  }
  public void setChequeInfo(ChequeInfo newChequeInfo) {
    chequeInfo = newChequeInfo;
  }
  public void setBan(int newBan) {
    ban = newBan;
  }
  public int getBan() {
    return ban;
  }

  public String getAuthorizationNumber(){
	  return authorizationNo;
  }
  
  public void setAuthorizationNumber(String authoNum){
	  authorizationNo = authoNum;
  }
  
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PaymentInfo:{\n");
    s.append("    ban=[").append(ban).append("]\n");
    s.append("    depositPaymentIndicator=[").append(depositPaymentIndicator).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    depositDate=[").append(depositDate).append("]\n");
    s.append("    paymentSourceType=[").append(paymentSourceType).append("]\n");
    s.append("    paymentSourceID=[").append(paymentSourceID).append("]\n");
    s.append("    allowOverpayment=[").append(allowOverpayment).append("]\n");
    s.append("    paymentMethod=[").append(paymentMethod).append("]\n");
    s.append("    creditCardInfo=[").append(creditCardInfo).append("]\n");
    s.append("    chequeInfo=[").append(chequeInfo).append("]\n");
    s.append("    authorizationNumber=[").append(authorizationNo).append("]\n");
    s.append("}");

    return s.toString();
  }

}
