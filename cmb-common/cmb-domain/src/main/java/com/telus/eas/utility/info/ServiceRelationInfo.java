/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import java.sql.Date;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class ServiceRelationInfo extends Info implements ServiceRelation {


  static final long serialVersionUID = 1L;

  private String type;
  private String serviceCode;
  private Date expirationDate;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ServiceSummary getService() {
    throw new UnsupportedOperationException("method not implemented here");
  }

  public String getServiceCode(){
    return serviceCode;
  }

  public void setServiceCode(String serviceCode){
    this.serviceCode = serviceCode;
  }

  public boolean isOptional(){
    return (this.getType().equals("C") ? true : false);
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("ServiceRelationInfo:[\n");
        s.append("    type=[").append(type).append("]\n");
        s.append("    serviceCode=[").append(serviceCode).append("]\n");
        s.append("    optional=[").append(this.isOptional()).append("]\n");
        s.append("]");

        return s.toString();
    }

/**
 * @return Returns the expirationDate.
 */
public Date getExpirationDate() {
	return expirationDate;
}
/**
 * @param expirationDate The expirationDate to set.
 */
public void setExpirationDate(Date expirationDate) {
	this.expirationDate = expirationDate;
}
}




