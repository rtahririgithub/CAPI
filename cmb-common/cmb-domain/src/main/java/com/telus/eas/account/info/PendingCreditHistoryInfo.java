/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.PendingCreditHistory;
import com.telus.eas.framework.info.Info;

public class PendingCreditHistoryInfo extends Info implements PendingCreditHistory {
   static final long serialVersionUID = 1L;

  private int id;
  private boolean isBalanceImpactFlag;
  private double amount;
  private Date creationDate;
  private Date effectiveDate;
  private String code;
  private String reasonCode;
  private double PSTAmount;
  private double GSTAmount;
  private double HSTAmount;
  private char approvalStatus;
  private boolean isBalanceIgnoreFlag;
  private String subscriberId;
  private int operatorId;
  private String productType;
  private String soc;

  public int getId() {
    return id;
  }
  public boolean isBalanceImpactFlag() {
    return isBalanceImpactFlag;
  }
  public double getAmount() {
    return amount;
  }
  public Date getCreationDate() {
    return creationDate;
  }
  public Date getEffectiveDate() {
    return effectiveDate;
  }
  public String getCode() {
    return code;
  }
  public String getReasonCode() {
    return reasonCode;
  }
  public double getPSTAmount() {
    return PSTAmount;
  }
  public double getGSTAmount() {
    return GSTAmount;
  }
  public double getHSTAmount() {
    return HSTAmount;
  }
  public char getApprovalStatus() {
    return approvalStatus;
  }
  public boolean isBalanceIgnoreFlag() {
    return isBalanceIgnoreFlag;
  }
  public String getSubscriberId() {
    return subscriberId;
  }
  public int getOperatorId() {
    return operatorId;
  }
  public String getProductType() {
    return productType;
  }
  
  public String getSOC() {
    return soc;
  }

  public void setId(int value) {
    id = value;
  }

  public void isBalanceImpactFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'I')
      isBalanceImpactFlag = true;
  }
  public void setAmount(double value) {
    amount = value;
  }
  public void setCreationDate(Date value) {
    creationDate = value;
  }
  public void setEffectiveDate(Date value) {
    effectiveDate = value;
  }
  public void setCode(String value) {
    code = value;
  }
  public void setReasonCode(String value) {
    reasonCode = value;
  }
  public void setPSTAmount(double value) {
    PSTAmount = value;
  }
  public void setGSTAmount(double value) {
    GSTAmount = value;
  }
  public void setHSTAmount(double value) {
    HSTAmount = value;
  }
  public void setApprovalStatus(String value) {
    if (value != null && value.length() > 0)
    approvalStatus = value.charAt(0);
  }
  public void isBalanceIgnoreFlag(String value) {
    if (value != null && value.length() > 0 && value.charAt(0) == 'Y')
      isBalanceIgnoreFlag = true;
  }
  public void setSubscriberId(String value) {
    subscriberId = value;
  }
  public void setOperatorId(int value) {
    operatorId = value;
  }
  public void setProductType(String value) {
    productType = value;
  }
  
  public void setSOC(String value) {
    soc = value;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);
    s.append("PendingCreditHistoryInfo:[\n");
    s.append("    id=[").append(id).append("]\n");
    s.append("    isBalanceImpactFlag=[").append(isBalanceImpactFlag).append("]\n");
    s.append("    amount=[").append(amount).append("]\n");
    s.append("    creationDate=[").append(creationDate).append("]\n");
    s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    reasonCode=[").append(reasonCode).append("]\n");
    s.append("    PSTAmount=[").append(PSTAmount).append("]\n");
    s.append("    GSTAmount=[").append(GSTAmount).append("]\n");
    s.append("    HSTAmount=[").append(HSTAmount).append("]\n");
    s.append("    approvalStatus=[").append(approvalStatus).append("]\n");
    s.append("    SOC=[").append(soc).append("]\n");
    s.append("    isBalanceIgnoreFlag=[").append(isBalanceIgnoreFlag).append("]\n");
    s.append("    subscriberId=[").append(subscriberId).append("]\n");
    s.append("    operatorId=[").append(operatorId).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("]");

    return s.toString();
  }
}
