/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class AdjustmentHistoryInfo extends Info implements AdjustmentHistory {

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
  private Date appliedDate;
  private String balanceImpactCode;
	private int operatorId;

  public AdjustmentHistoryInfo() {
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
  
	public Date getAppliedDate() {
		 return appliedDate;
	 }  
	 
	public String getBalanceImpactCode() {
		return balanceImpactCode;
	}	 
	
	public int getOperatorId() {
		return operatorId;
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
  
	public void setAppliedDate(Date value) {
		this.appliedDate = value;
	}  
	
	public void setBalanceImpactCode(String value) {
		this.balanceImpactCode = value;
	}	
	
	public void setOperatorId(int value) {
		operatorId = value;
	}

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("AdjustmentHistoryInfo:[\n");
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
				s.append("    appliedDate=[").append(appliedDate).append("]\n");
				s.append("    balanceImpactCode=[").append(balanceImpactCode).append("]\n");
				s.append("    operatorId=[").append(operatorId).append("]\n");
				
        s.append("]");

        return s.toString();
    }
  
}



