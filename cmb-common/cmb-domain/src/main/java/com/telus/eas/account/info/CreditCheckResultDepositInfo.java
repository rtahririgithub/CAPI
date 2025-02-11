/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class CreditCheckResultDepositInfo extends Info implements CreditCheckResultDeposit {

   static final long serialVersionUID = 1L;

  private double deposit ;
  private String productType = "";

  public String getProductType() {
    return productType;
  }

  public double getDeposit() {
    return deposit;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public void setDeposit(double deposit) {
    this.deposit = deposit;
  }

  public void copyFrom(CreditCheckResultDepositInfo info) {
    productType          = info.productType;
    deposit              = info.deposit;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("CreditCheckResultDepositInfo:{\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    deposit=[").append(deposit).append("]\n");
    s.append("}");

    return s.toString();
  }

}




