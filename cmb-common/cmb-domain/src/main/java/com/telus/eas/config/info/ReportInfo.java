/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.eas.framework.info.*;


public class ReportInfo extends Info {

	static final long serialVersionUID = 1L;

  private int transactionId;
  private String transactionType;
  private String applicationId;
  private String dealerCode;
  private String salesRepCode;
  private int banId;
  private String subscriberId;
  private int operatorId;
  private java.util.Date transactionDate;

  public ReportInfo() {
  }


//  public void copyTo(TransactionHeaderDAO dao) {
//    dao.setTransactionId(transactionId);
//    dao.setTransactionDatetime(new Timestamp(transactionDate.getTime()));
//    dao.setTransactionTypeCd(transactionType);
//    dao.setBan(banId);
//
//    //--------------------------------------------------------
//    // PatronId should be set to subscriberId, or Ban, if
//    // subscriberId is unavailable
//    //--------------------------------------------------------
//    if (subscriberId != null) {
//      dao.setPatronIdTypeCd("SUBS");
//      dao.setPatronId(subscriberId);
//    } else {
//      dao.setPatronIdTypeCd("BAN");
//      dao.setPatronId(String.valueOf(banId));
//    }
//
//    dao.setOperatorId(operatorId);
//    dao.setApplicationId(applicationId);
//    dao.setDealerCode(dealerCode);
//    dao.setSalesrepCode(salesRepCode);
//  }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getDealerCode() {
    return dealerCode;
  }

  public void setDealerCode(String dealerCode) {
    this.dealerCode = dealerCode;
  }

  public String getSalesRepCode() {
    return salesRepCode;
  }

  public void setSalesRepCode(String salesRepCode) {
    this.salesRepCode = salesRepCode;
  }

  public int getBanId() {
    return banId;
  }

  public void setBanId(int banId) {
    this.banId = banId;
  }

  public String getSubscriberId(){
    return subscriberId;
  }

  public void setSubscriberId(String subscriberId){
    this.subscriberId = subscriberId;
  }

  public int getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(int operatorId) {
    this.operatorId = operatorId;
  }

  public java.util.Date getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(java.util.Date transactionDate) {
    this.transactionDate = transactionDate;
  }
}



