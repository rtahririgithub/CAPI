/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class CreditHistoryInfo extends Info implements CreditHistory {

   static final long serialVersionUID = 1L;

  private Date date;
  private String code;
  private String reasonCode;
  private double amount;
  private double amountPST;
  private double amountGST;
  private double amountHST;
  private String serviceCode;
  private String featureCode;
  private String subscriberMobile;

  public CreditHistoryInfo() {
  }

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

  public double getAmountPST() {
    return amountPST;
  }

  public double getAmountGST() {
    return amountGST;
  }

  public double getAmountHST() {
    return amountHST;
  }

  public String getServiceCode() {
    return serviceCode;
  }

  public String getFeatureCode() {
    return featureCode;
  }

  public String getSubscriberMobile() {
    return subscriberMobile;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setAmountPST(double amountPST) {
    this.amountPST = amountPST;
  }

  public void setAmountGST(double amountGST) {
    this.amountGST = amountGST;
  }

  public void setAmountHST(double amountHST) {
    this.amountHST = amountHST;
  }

  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }

  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }

  public void setSubscriberMobile(String subscriberMobile) {
    this.subscriberMobile = subscriberMobile;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("CreditHistoryInfo:[\n");
        s.append("    date=[").append(date).append("]\n");
        s.append("    code=[").append(code).append("]\n");
        s.append("    reasonCode=[").append(reasonCode).append("]\n");
        s.append("    amount=[").append(amount).append("]\n");
        s.append("    amountPST=[").append(amountPST).append("]\n");
        s.append("    amountGST=[").append(amountGST).append("]\n");
        s.append("    amountHST=[").append(amountHST).append("]\n");
        s.append("    serviceCode=[").append(serviceCode).append("]\n");
        s.append("    featureCode=[").append(featureCode).append("]\n");
        s.append("    subscriberMobile=[").append(subscriberMobile).append("]\n");
        s.append("]");

        return s.toString();
    }
  
}



