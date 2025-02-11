/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;


public class MonthlyFinancialActivityInfo extends Info implements MonthlyFinancialActivity {

   static final long serialVersionUID = 1L;

  private int year;
  private int month;
  private String activity;
  private int dishonoredPaymentCount;

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public String getActivity() {
    return activity;
  }

  public int getDishonoredPaymentCount() {
    return dishonoredPaymentCount;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public void setActivity(String activity) {
    this.activity = activity;
  }

  public void setDishonoredPaymentCount(int dishonoredPaymentCount) {
    this.dishonoredPaymentCount = dishonoredPaymentCount;
  }

  public boolean isSuspended() {
    return activity != null && activity.length() > 0 && "SUCNAW".indexOf(activity) != -1;
  }

  public boolean isCancelled(){
    return activity != null && activity.length() > 0 && "CNAW".indexOf(activity) != -1;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("MonthlyFinancialActivityInfo:{");
    s.append("    year=[").append(year).append("], ");
    s.append("    month=[").append(month).append("], ");
    s.append("    activity=[").append(activity).append("], ");
    s.append("    dishonoredPaymentCount=[").append(dishonoredPaymentCount).append("]");
    s.append("}");
    /*
    s.append("MonthlyFinancialActivityInfo:{\n");
    s.append("    year=[").append(year).append("]\n");
    s.append("    month=[").append(month).append("]\n");
    s.append("    activity=[").append(activity).append("]\n");
    s.append("    dishonoredPaymentCount=[").append(dishonoredPaymentCount).append("]\n");
    s.append("}");
    */


    return s.toString();
  }

}




