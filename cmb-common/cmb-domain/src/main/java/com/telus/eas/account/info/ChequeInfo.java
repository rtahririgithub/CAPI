/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class ChequeInfo extends Info implements Cheque {

   static final long serialVersionUID = 1L;

  private String chequeNumber;
  private BankAccountInfo bankAccount = new BankAccountInfo();

  public String getChequeNumber() {
    return chequeNumber;
  }

  public void setChequeNumber(String chequeNumber) {
    this.chequeNumber = toUpperCase(chequeNumber);
  }

  public BankAccount getBankAccount() {
    return bankAccount;
  }

  public BankAccountInfo getBankAccount0() {
    return bankAccount;
  }

  public void copyFrom(ChequeInfo info) {
	  if (info != null){
		  chequeNumber = toUpperCase(info.chequeNumber);
		  bankAccount.copyFrom(info.bankAccount);
	  }
  }


  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("ChequeInfo:{\n");
    s.append("    chequeNumber=[").append(chequeNumber).append("]\n");
    s.append("bankAccount=[").append(bankAccount).append("]\n");
    s.append("}");

    return s.toString();
  }

  public void setBankAccount(BankAccount bankAccount) {
    this.bankAccount = (BankAccountInfo)bankAccount;
  }
}




