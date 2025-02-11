package com.telus.eas.subscriber.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.equipment.*;
import com.telus.eas.framework.info.*;
import java.util.*;





public class HandsetChangeHistoryInfo extends Info implements HandsetChangeHistory {

  static final long serialVersionUID = 1L;

  private Date date;

  private String newSerialNumber;

  private String oldSerialNumber;
  private String oldTechnologyType;
  private String newTechnologyType;
  private String oldProductCode;
  private String newProductCode;



  public HandsetChangeHistoryInfo() {

  }



  public Date getDate() {

    return date;

  }



  public String getNewSerialNumber() {

    return newSerialNumber;

  }



  public String getOldSerialNumber() {

    return oldSerialNumber;

  }



  public Equipment getNewEquipment() throws TelusAPIException {

    throw new java.lang.UnsupportedOperationException("Method not implemented.");

  }



  public Equipment getOldEquipment() throws TelusAPIException {

    throw new java.lang.UnsupportedOperationException("Method not implemented.");

  }



  public void setDate(Date date) {

    this.date = date;

  }



  public void setNewSerialNumber(String newSerialNumber) {

    this.newSerialNumber = newSerialNumber;

  }



  public void setOldSerialNumber(String oldSerialNumber) {

    this.oldSerialNumber = oldSerialNumber;

  }



    public String toString()

    {

        StringBuffer s = new StringBuffer(128);



        s.append("HandsetChangeHistoryInfo:[\n");

        s.append("    date=[").append(date).append("]\n");

        s.append("    newSerialNumber=[").append(newSerialNumber).append("]\n");

        s.append("    oldSerialNumber=[").append(oldSerialNumber).append("]\n");

        s.append("]");



        return s.toString();

    }
  public void setOldTechnologyType(String oldTechnologyType) {
    this.oldTechnologyType = oldTechnologyType;
  }
  public String getOldTechnologyType() {
    return oldTechnologyType;
  }
  public void setNewTechnologyType(String newTechnologyType) {
    this.newTechnologyType = newTechnologyType;
  }
  public String getNewTechnologyType() {
    return newTechnologyType;
  }
  public void setOldProductCode(String oldProductCode) {
    this.oldProductCode = oldProductCode;
  }
  public String getOldProductCode() {
    return oldProductCode;
  }
  public void setNewProductCode(String newProductCode) {
    this.newProductCode = newProductCode;
  }
  public String getNewProductCode() {
    return newProductCode;
  }

}

