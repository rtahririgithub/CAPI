/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class BusinessCreditInfo extends Info implements BusinessCredit {

   static final long serialVersionUID = 1L;

  private String incorporationNumber;
  private Date incorporationDate;

  public String getIncorporationNumber() {
    return incorporationNumber;
  }

  public void setIncorporationNumber(String incorporationNumber) {
    this.incorporationNumber = toUpperCase(incorporationNumber);
  }

  public Date getIncorporationDate() {
    return incorporationDate;
  }

  public void setIncorporationDate(Date incorporationDate) {
    this.incorporationDate = incorporationDate;
  }

  public void copyFrom(BusinessCreditInfo o) {
	if (o==null ) return;
	
    incorporationNumber = toUpperCase(o.incorporationNumber);
    incorporationDate   = cloneDate(o.incorporationDate);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("BusinessCreditInfo:{\n");
    s.append("    incorporationNumber=[").append(incorporationNumber).append("]\n");
    s.append("    incorporationDate=[").append(incorporationDate).append("]\n");
    s.append("}");

    return s.toString();
  }

}




