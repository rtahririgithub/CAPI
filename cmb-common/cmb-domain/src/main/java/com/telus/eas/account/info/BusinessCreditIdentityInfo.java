/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class BusinessCreditIdentityInfo extends Info implements BusinessCreditIdentity {

   static final long serialVersionUID = 1L;

  private String companyName ;
  private double marketAccount;
  private transient String[] companyArray;

  public String getCompanyName() {
    return companyName;
  }

  public synchronized String[] getCompanyAsArray() {
    if(companyArray == null) {
      StringTokenizer t = new StringTokenizer(companyName, ",");
      List list = new ArrayList(6);
      while(t.hasMoreTokens()) {
        list.add(t.nextToken());
      }
      companyArray = (String[])list.toArray(new String[list.size()]);
    }

    return companyArray;
  }


  public double getMarketAccount() {
    return marketAccount;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public void setMarketAccount(double marketAccount) {
    this.marketAccount = marketAccount;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("BusinessCreditIdentityInfo:{\n");
    s.append("    companyName=[").append(companyName).append("]\n");
    s.append("    marketAccount=[").append(marketAccount).append("]\n");
    s.append("}");

    return s.toString();
  }

}




