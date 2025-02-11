/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.RefundHistory;
import com.telus.eas.framework.info.Info;

public class RefundHistoryInfo extends Info implements RefundHistory {

   static final long serialVersionUID = 1L;

  private Date date;
  private String code;
  private String reasonCode;
  private double amount;
  private boolean isAccountsPayableProcessFlag;
  private Date accountPayableProcessDate;
  private String couponGiftNumber;
  private String refPaymentMethod;
  private String bankCode;
  private String bankAccountNumber;
  private String chequeNumber;
  private String creditCardNumber;
  private String creditCardAuthCode;
  private Date creditCardExpiryDate;

  public Date getDate() {
    return date;
  }
  public String getCode() {
    return code;
  }
  public String getReasonCode() {
    return reasonCode;
  }
  public double getAmount() {
    return amount;
  }
  public boolean isAccountsPayableProcessFlag() {
    return isAccountsPayableProcessFlag;
  }

  public Date getAccountPayableProcessDate() {
    return accountPayableProcessDate;
  }
  public String getCouponGiftNumber() {
    return couponGiftNumber;
  }

  public String getRefPaymentMethod() {
    return refPaymentMethod;
  }
  public String getBankCode() {
    return bankCode;
  }
  public String getBankAccountNumber() {
    return bankAccountNumber;
  }
  public String getChequeNumber() {
    return chequeNumber;
  }
  public String getCreditCardNumber() {
    return creditCardNumber;
  }
  public String getCreditCardAuthCode() {
    return creditCardAuthCode;
  }
  public Date getCreditCardExpiryDate() {
    return creditCardExpiryDate;
  }

  public void setDate(Date value) {
    date = value;
  }
  public void setCode(String value) {
    code = value;
  }
  public void setReasonCode(String value) {
    reasonCode = value;
  }
  public void setAmount(double value) {
    amount = value;
  }
  public void isAccountsPayableProcessFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'Y')
      isAccountsPayableProcessFlag = true;
  }

  public void setAccountPayableProcessDate(Date value) {
    accountPayableProcessDate = value;
  }
  public void setCouponGiftNumber(String value) {
    couponGiftNumber = value;
  }

  public void setRefPaymentMethod(String value) {
    refPaymentMethod = value;
  }
  public void setBankCode(String value) {
    bankCode = value;
  }
  public void setBankAccountNumber(String value) {
    bankAccountNumber = value;
  }
  public void setChequeNumber(String value) {
    chequeNumber = value;
  }
  public void setCreditCardNumber(String value) {
    creditCardNumber = value;
  }
  public void setCreditCardAuthCode(String value) {
    creditCardAuthCode = value;
  }
  public void setCreditCardExpiryDate(Date value) {
    creditCardExpiryDate = value;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);
    s.append("RefundHistoryInfo:[\n");
    s.append("    date=[").append(date).append("]\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    reasonCode=[").append(reasonCode).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    isAccountsPayableProcessFlag=[").append(isAccountsPayableProcessFlag).append("]\n");
    s.append("    accountPayableProcessDate=[").append(accountPayableProcessDate).append("]\n");
    s.append("    couponGiftNumber=[").append(couponGiftNumber).append("]\n");
    s.append("    refPaymentMethod=[").append(refPaymentMethod).append("]\n");
    s.append("    bankCode=[").append(bankCode).append("]\n");
    s.append("    bankAccountNumber=[").append(bankAccountNumber).append("]\n");
    s.append("    chequeNumber=[").append(chequeNumber).append("]\n");
    s.append("    creditCardNumber=[").append(creditCardNumber).append("]\n");
    s.append("    creditCardAuthCode=[").append(creditCardAuthCode).append("]\n");
    s.append("    creditCardExpiryDate=[").append(creditCardExpiryDate).append("]\n");
    s.append("]");

    return s.toString();
  }
}