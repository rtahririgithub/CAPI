/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class BankAccountInfo extends Info implements BankAccount {

   static final long serialVersionUID = 1L;

  private String bankCode ;
  private String bankAccountNumber ;
  private String bankBranchNumber ;
  private String bankAccountHolder ;
  private String bankAccountType ;

  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = toUpperCase(bankCode);
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = toUpperCase(bankAccountNumber);
  }

  public String getBankBranchNumber() {
    return bankBranchNumber;
  }

  public void setBankBranchNumber(String bankBranchNumber) {
    this.bankBranchNumber = toUpperCase(bankBranchNumber);
  }

  public String getBankAccountHolder() {
    return bankAccountHolder;
  }

  public void setBankAccountHolder(String bankAccountHolder) {
    this.bankAccountHolder = toUpperCase(bankAccountHolder);
  }

  public String getBankAccountType() {
    return bankAccountType;
  }

  public void setBankAccountType(String bankAccountType) {
    this.bankAccountType = toUpperCase(bankAccountType);
  }

  public void copyFrom(BankAccountInfo info) {
    bankCode           = toUpperCase(info.bankCode);
    bankAccountNumber  = toUpperCase(info.bankAccountNumber);
    bankBranchNumber   = toUpperCase(info.bankBranchNumber);
    bankAccountHolder  = toUpperCase(info.bankAccountHolder);
    bankAccountType    = toUpperCase(info.bankAccountType);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("BankAccountInfo:{\n");
    s.append("    bankCode=[").append(bankCode).append("]\n");
    s.append("    bankAccountNumber=[").append(bankAccountNumber).append("]\n");
    s.append("    bankBranchNumber=[").append(bankBranchNumber).append("]\n");
    s.append("    bankAccountHolder=[").append(bankAccountHolder).append("]\n");
    s.append("    bankAccountType=[").append(bankAccountType).append("]\n");
    s.append("}");

    return s.toString();
  }

}




