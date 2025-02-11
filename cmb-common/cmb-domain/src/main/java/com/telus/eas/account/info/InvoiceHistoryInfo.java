/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.api.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class InvoiceHistoryInfo extends Info implements InvoiceHistory {

   static final long serialVersionUID = 1L;

  private Date date;
  private boolean mailedIndicator;
  private Date dueDate;
  private double previousBalance;
  private double invoiceAmount;
  private double amountDue;
  private int billSeqNo;
  private int cycleRunYear;
  private int cycleRunMonth;
  private int cycleCode;
  private String status;

  private double paymentReceivedAmount = 0.0;
  private double adjustmentAmount = 0.0;
  private double pastDueAmount = 0.0;
  private double latePaymentCharge = 0.0;
  private double currentCharges = 0.0;
  private double totalTax = 0.0;

  private int homeCallCount;
  private int roamingCallCount;
  private double homeCallMinutes;
  private double roamingCallMinutes;
  private double monthlyRecurringCharge;
  private double localCallingCharges;
  private double otherCharges;
  private double zoneUsageCharges;
  private double EHAUsageCharges;
  private int banId;

  public InvoiceHistoryInfo() {
  }

  public Date getDate() {
    return date;
  }

  public boolean getMailedIndicator() {
    return mailedIndicator;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public double getPreviousBalance() {
    return previousBalance;
  }

  public double getInvoiceAmount() {
    return invoiceAmount;
  }

  public double getAmountDue() {
    return amountDue;
  }

  public double getLatePaymentCharge() {
    return latePaymentCharge;
  }

  public double getPastDue() {
    return pastDueAmount;
  }

  public double getAdjustmentAmount() {
    return adjustmentAmount;
  }

  public double getPaymentReceivedAmount() {
    return paymentReceivedAmount;
  }

  public double getCurrentCharges() {
    return currentCharges;
  }

  public double getTotalTax() {
    return totalTax;
  }

  public int getBillSeqNo() {
      return billSeqNo;
  }

  public int getCycleRunYear() {
      return cycleRunYear;
  }

  public int getCycleRunMonth() {
      return cycleRunMonth;
  }

  public int getCycleCode() {
      return cycleCode;
  }

  public String getStatus() {
      return status;
  }
  public void setDate(Date date) {
    this.date = date;
  }

  public void setMailedIndicator(boolean mailedIndicator) {
    this.mailedIndicator = mailedIndicator;
  }

  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  public void setPreviousBalance(double previousBalance) {
    this.previousBalance = previousBalance;
  }

  public void setInvoiceAmount(double invoiceAmount) {
    this.invoiceAmount = invoiceAmount;
  }

  public void setAmountDue(double amountDue) {
    this.amountDue = amountDue;
  }

  public void setLatePaymentCharge(double amt) {
    this.latePaymentCharge = amt;
  }

  public void setPastDue(double amt) {
    this.pastDueAmount = amt;
  }

  public void setAdjustmentAmount(double amt) {
    this.adjustmentAmount = amt;
  }

  public void setPaymentReceivedAmount(double amt) {
    this.paymentReceivedAmount = amt;
  }

  public void setCurrentCharges(double amt) {
    this.currentCharges = amt;
  }

  public void setTotalTax(double amt) {
    this.totalTax = amt;
  }

  public void setBillSeqNo(int billSeqNo) {
     this.billSeqNo = billSeqNo;
  }

  public void setCycleRunYear(int cycleRunYear) {
     this.cycleRunYear = cycleRunYear;
  }

  public void setCycleRunMonth(int cycleRunMonth) {
     this.cycleRunMonth = cycleRunMonth;
  }

  public void setCycleCode(int cycleCode) {
     this.cycleCode = cycleCode;
  }

  public void setStatus(String status) {
     this.status = status;
  }

  public String toString() {
      StringBuffer s = new StringBuffer(128);

      s.append("InvoiceHistoryInfo:[\n");
      s.append("    date=[").append(date).append("]\n");
      s.append("    mailedIndicator=[").append(mailedIndicator).append("]\n");
      s.append("    dueDate=[").append(dueDate).append("]\n");
      s.append("    previousBalance=[").append(previousBalance).append("]\n");
      s.append("    invoiceAmount=[").append(invoiceAmount).append("]\n");
      s.append("    amountDue=[").append(amountDue).append("]\n");
      s.append("    billSeqNo=[").append(billSeqNo).append("]\n");
      s.append("    cycleRunYear=[").append(cycleRunYear).append("]\n");
      s.append("    cycleRunMonth=[").append(cycleRunMonth).append("]\n");
      s.append("    cycleCode=[").append(cycleCode).append("]\n");
      s.append("    status=[").append(status).append("]\n");
      s.append("    homeCallCount=[").append(homeCallCount).append("]\n");
      s.append("    roamingCallCount=[").append(roamingCallCount).append("]\n");
      s.append("    homeCallMinutes=[").append(homeCallMinutes).append("]\n");
      s.append("    roamingCallMinutes=[").append(roamingCallMinutes).append("]\n");
      s.append("    monthlyRecurringCharge=[").append(monthlyRecurringCharge).append("]\n");
      s.append("    localCallingCharges=[").append(localCallingCharges).append("]\n");
      s.append("    otherCharges=[").append(otherCharges).append("]\n");
      s.append("    zoneUsageCharges=[").append(zoneUsageCharges).append("]\n");
      s.append("    EHAUsageCharges=[").append(EHAUsageCharges).append("]\n");

      s.append("]");

      return s.toString();
  }

/**
 * @return Returns the homeCallCount.
 */
public int getHomeCallCount() {
	return homeCallCount;
}
/**
 * @param homeCallCount The homeCallCount to set.
 */
public void setHomeCallCount(int homeCallCount) {
	this.homeCallCount = homeCallCount;
}
/**
 * @return Returns the homeCallMinutes.
 */
public double getHomeCallMinutes() {
	return homeCallMinutes;
}
/**
 * @param homeCallMinutes The homeCallMinutes to set.
 */
public void setHomeCallMinutes(double homeCallMinutes) {
	this.homeCallMinutes = homeCallMinutes;
}
/**
 * @return Returns the localCallingCharges.
 */
public double getLocalCallingCharges() {
	return localCallingCharges;
}
/**
 * @param localCallingCharges The localCallingCharges to set.
 */
public void setLocalCallingCharges(double localCallingCharges) {
	this.localCallingCharges = localCallingCharges;
}
/**
 * @return Returns the monthlyRecurringCharge.
 */
public double getMonthlyRecurringCharge() {
	return monthlyRecurringCharge;
}
/**
 * @param monthlyRecurringCharge The monthlyRecurringCharge to set.
 */
public void setMonthlyRecurringCharge(double monthlyRecurringCharge) {
	this.monthlyRecurringCharge = monthlyRecurringCharge;
}
/**
 * @return Returns the otherCharges.
 */
public double getOtherCharges() {
	return otherCharges;
}
/**
 * @param otherCharges The otherCharges to set.
 */
public void setOtherCharges(double otherCharges) {
	this.otherCharges = otherCharges;
}
/**
 * @return Returns the pastDueAmount.
 */
public double getPastDueAmount() {
	return pastDueAmount;
}
/**
 * @param pastDueAmount The pastDueAmount to set.
 */
public void setPastDueAmount(double pastDueAmount) {
	this.pastDueAmount = pastDueAmount;
}
/**
 * @return Returns the roamingCallCount.
 */
public int getRoamingCallCount() {
	return roamingCallCount;
}
/**
 * @param roamingCallCount The roamingCallCount to set.
 */
public void setRoamingCallCount(int roamingCallCount) {
	this.roamingCallCount = roamingCallCount;
}
/**
 * @return Returns the roamingCallMinutes.
 */
public double getRoamingCallMinutes() {
	return roamingCallMinutes;
}
/**
 * @param roamingCallMinutes The roamingCallMinutes to set.
 */
public void setRoamingCallMinutes(double roamingCallMinutes) {
	this.roamingCallMinutes = roamingCallMinutes;
}
/**
 * @return Returns the eHAUsageCharges.
 */
public double getEHAUsageCharges() {
	return EHAUsageCharges;
}
/**
 * @param usageCharges The eHAUsageCharges to set.
 */
public void setEHAUsageCharges(double usageCharges) {
	EHAUsageCharges = usageCharges;
}
/**
 * @return Returns the zoneUsageCharges.
 */
public double getZoneUsageCharges() {
	return zoneUsageCharges;
}
/**
 * @param zoneUsageCharges The zoneUsageCharges to set.
 */
public void setZoneUsageCharges(double zoneUsageCharges) {
	this.zoneUsageCharges = zoneUsageCharges;
}

public int getBanId() {
  return banId;
}

public void setBanId(int banId) {
  this.banId = banId;
}

public SubscriberInvoiceDetail[] getSubscriberInvoiceDetails() throws TelusAPIException {
  throw new java.lang.UnsupportedOperationException("Method not implemented here");
}
}



