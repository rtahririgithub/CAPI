/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.TelusAPIException;
import com.telus.api.interaction.Interaction;
import com.telus.api.interaction.InteractionDetail;

import com.telus.eas.framework.info.Info;


/**
  * Value object implementation of the Interaction interface.  Method which usually use a remote method
  * call will throw an UnsupportedOperationException instead.
  *
  * Note:  There was a naming change from Transaction to Interaction
  */
public class InteractionInfo extends Info implements Interaction {

   static final long serialVersionUID = 1L;
   
  public static final String PATRON_ID_TYPE_SUB = "SUBS";
  public static final String PATRON_ID_TYPE_BAN = "BAN";

  private long id;
  private String type;
  private String applicationId;
  private String dealerCode;
  private String salesRepCode;
  private int banId;
  private String subscriberId;
  private Integer operatorId;
  private java.util.Date date;
  private long reasonId;

  /**
    * Default empty constructor
    */
  public InteractionInfo() {
  }


//  /**
//    * Copies the information contained in this object to the dao.
//    *
//    * @param dao -- This will contain this objects information on return.
//    * DO NOT USE. Will break provider layer
//    */
//  public void copyTo(TransactionHeaderDAO dao) {
//    dao.setTransactionId(id);
//    dao.setTransactionDatetime(new Timestamp(date.getTime()));
//    dao.setTransactionTypeCd(type);
//    dao.setBan(banId);
//
//    //--------------------------------------------------------
//    // PatronId should be set to subscriberId, or Ban, if
//    // subscriberId is unavailable
//    //--------------------------------------------------------
//    if (subscriberId != null) {
//      dao.setPatronIdTypeCd(PATRON_ID_TYPE_SUB);
//      dao.setPatronId(subscriberId);
//    } else {
//      dao.setPatronIdTypeCd(PATRON_ID_TYPE_BAN);
//      dao.setPatronId(String.valueOf(banId));
//    }
//
//    dao.setOperatorId(operatorId);
//    dao.setApplicationId(applicationId);
//    dao.setDealerCode(dealerCode);
//    dao.setSalesrepCode(salesRepCode);
//    dao.setReasonId(reasonId);
//  }

//  /**
//    * Copies the information from the dao to this object.
//    *
//    * @param dao -- The container for the information.
//    */
//  public void copyFrom(TransactionHeaderDAO dao) {
//    setId(dao.getTransactionId());
//    setType(dao.getTransactionTypeCd());
//    setDate(dao.getTransactionDatetime());
//
//    setBan(dao.getBan());
//
//    String patronIdType = dao.getPatronIdTypeCd();
//    if(patronIdType.equals(PATRON_ID_TYPE_SUB))
//      setSubscriberId(dao.getPatronId());
//    else
//      setSubscriberId(null);
//
//    setOperatorId(dao.getOperatorId());
//    setApplicationId(dao.getApplicationId());
//    setDealerCode(dao.getDealerCode());
//    setSalesRepCode(dao.getSalesrepCode());
//    setReasonId(dao.getReasonId());
//  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public String getSalesrepCode() {
    return salesRepCode;
  }

  public void setSalesRepCode(String salesRepCode) {
    this.salesRepCode = salesRepCode;
  }

  public int getBan() {
    return banId;
  }

  public void setBan(int banId) {
    this.banId = banId;
  }

  public void setBan(Integer ban) {
    if(ban == null)
      setBan(-1);
    else
      setBan(ban.intValue());
  }

  public String getSubscriberId(){
    return subscriberId;
  }

  public void setSubscriberId(String subscriberId){
    this.subscriberId = subscriberId;
  }

  public Integer getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Integer operatorId) {
    this.operatorId = operatorId;
  }

  public void setOperatorId(int operatorId) {
    this.setOperatorId(new Integer(operatorId));
  }

  public java.util.Date getDatetime() {
    return date;
  }

  public void setDate(java.util.Date date) {
    this.date = date;
  }

  public long getReasonId() {
    return reasonId;
  }

  public void setReasonId(long reasonId) {
    this.reasonId = reasonId;
  }


  /**
    * Not implemented.  This method always results in an UnsupportedOperationException exception
    *
    * @exception UnsupportedOperationException -- Always thrown.
    */
  public InteractionDetail[] getDetails() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }
}
