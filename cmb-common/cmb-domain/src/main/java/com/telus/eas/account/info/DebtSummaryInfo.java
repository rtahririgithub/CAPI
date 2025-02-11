/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class DebtSummaryInfo extends Info implements DebtSummary {

   static final long serialVersionUID = 1L;

  private double pastDue1to30Days;
  private double pastDue31to60Days;
  private double pastDue61to90Days;
  private double pastDueOver90Days;
  private double pastDue;
  private double currentDue;
  private Date billDueDate;
  private double accountRealTimeBalance;

 public double getAccountRealTimeBalance() {
		return accountRealTimeBalance;
	}

public void setAccountRealTimeBalance(double accountRealTimeBalance) {
		this.accountRealTimeBalance = accountRealTimeBalance;
	}

public double getPastDue1to30Days() {
    return pastDue1to30Days;
  }

  public double getPastDue31to60Days() {
    return pastDue31to60Days;
  }

  public double getPastDue61to90Days() {
    return pastDue61to90Days;
  }

  public double getPastDueOver90Days() {
    return pastDueOver90Days;
  }

  public double getPastDue() {
    return pastDue;
  }

  public double getCurrentDue() {
    return currentDue;
  }

  public void setPastDue1to30Days(double pastDue1to30Days) {
    this.pastDue1to30Days = pastDue1to30Days;
  }

  public void setPastDue31to60Days(double pastDue31to60Days) {
    this.pastDue31to60Days = pastDue31to60Days;
  }

  public void setPastDue61to90Days(double pastDue61to90Days) {
    this.pastDue61to90Days = pastDue61to90Days;
  }

  public void setPastDueOver90Days(double pastDueOver90Days) {
    this.pastDueOver90Days = pastDueOver90Days;
  }

  public void setPastDue(double pastDue) {
    this.pastDue = pastDue;
  }

  public void setCurrentDue(double currentDue) {
    this.currentDue = currentDue;
  }


   public void setBillDueDate(Date billDueDate) {
    this.billDueDate = billDueDate;
  }
  public Date getBillDueDate() {
    return billDueDate;
  }


  public void copyFrom(DebtSummaryInfo o) {
    pastDue1to30Days   = o.pastDue1to30Days;
    pastDue31to60Days  = o.pastDue31to60Days;
    pastDue61to90Days  = o.pastDue61to90Days;
    pastDueOver90Days  = o.pastDueOver90Days;
    pastDue            = o.pastDue;
    currentDue         = o.currentDue;
    accountRealTimeBalance = o.accountRealTimeBalance;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("MonthlyFinancialActivityInfo:{\n");
    //s.append("    =[").append().append("]\n");
    s.append("    pastDue1to30Days=[").append(pastDue1to30Days).append("]\n");
    s.append("    pastDue31to60Days=[").append(pastDue31to60Days).append("]\n");
    s.append("    pastDue61to90Days=[").append(pastDue61to90Days).append("]\n");
    s.append("    pastDueOver90Days=[").append(pastDueOver90Days).append("]\n");
    s.append("    pastDue=[").append(pastDue).append("]\n");
    s.append("    currentDue=[").append(currentDue).append("]\n");
    s.append("    billDueDate=[").append(String.valueOf(billDueDate)).append("]\n");    
    s.append("    accountRealTimeBalance=[").append(accountRealTimeBalance).append("]\n");
    s.append("}");

    return s.toString();
  }


}




