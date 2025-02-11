/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class PaymentMethodChangeHistoryInfo extends Info implements PaymentMethodChangeHistory {

   static final long serialVersionUID = 1L;

  private Date date;
  private String creditCardType;
  private String creditCardExpiry;
  private String directDebitStatusCode;
  private String bankCode;
  private String bankAccountNumber;
  private String bankBranchNumber;
  private String creditCardToken ;
  private String creditCardLeadingDisplayDigits ;
  private String creditCardTrailingDisplayDigits ;


  public PaymentMethodChangeHistoryInfo() {
  }

  public Date getDate() {
    return date;
  }

  public String getCreditCardType() {
    return creditCardType;
  }

  public String getCreditCardExpiry() {
    return creditCardExpiry;
  }

  public String getDirectDebitStatusCode() {
    return directDebitStatusCode;
  }

  public String getBankCode() {
    return bankCode;
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public String getBankBranchNumber() {
    return bankBranchNumber;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setCreditCardType(String creditCardType) {
    this.creditCardType = creditCardType;
  }

  public void setCreditCardExpiry(String creditCardExpiry) {
    this.creditCardExpiry = creditCardExpiry;
  }

  public void setDirectDebitStatusCode(String directDebitStatusCode) {
    this.directDebitStatusCode = directDebitStatusCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  public void setBankBranchNumber(String bankBranchNumber) {
    this.bankBranchNumber = bankBranchNumber;
  }

	public String getCreditCardToken() {
	return creditCardToken;
}

public void setCreditCardToken(String creditCardToken) {
	this.creditCardToken = creditCardToken;
}

public String getCreditCardLeadingDisplayDigits() {
	return creditCardLeadingDisplayDigits;
}

public void setCreditCardLeadingDisplayDigits(
		String creditCardLeadingDisplayDigits) {
	this.creditCardLeadingDisplayDigits = creditCardLeadingDisplayDigits;
}

public String getCreditCardTrailingDisplayDigits() {
	return creditCardTrailingDisplayDigits;
}

public void setCreditCardTrailingDisplayDigits(
		String creditCardTrailingDisplayDigits) {
	this.creditCardTrailingDisplayDigits = creditCardTrailingDisplayDigits;
}

	public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("PaymentMethodChangeHistoryInfo:[\n");
        s.append("    date=[").append(date).append("]\n");
        s.append("    creditCardType=[").append(creditCardType).append("]\n");
        s.append("    creditCardTrailDigits=[").append(creditCardTrailingDisplayDigits).append("]\n");
        s.append("    creditCardExpiry=[").append(creditCardExpiry).append("]\n");
        s.append("    directDebitStatusCode=[").append(directDebitStatusCode).append("]\n");
        s.append("    bankCode=[").append(bankCode).append("]\n");
        s.append("    bankAccountNumber=[").append(bankAccountNumber).append("]\n");
        s.append("    bankBranchNumber=[").append(bankBranchNumber).append("]\n");
        s.append("]");

        return s.toString();
    }
}



