/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class ProrationMinutesInfo extends Info implements ProrationMinutes {
   static final long serialVersionUID = 1L;

  private Date expiryDate;
  private int minutes;

  public ProrationMinutesInfo() {
  }

  public ProrationMinutesInfo(int param) {
  }

  public Date getExpiryDate() {
    return expiryDate;
  }

  public int getMinutes() {
    return minutes;
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
  }

  public void setMinutes(int minutes) {
    this.minutes = minutes;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("ProrationMinutesInfo:[\n");
        s.append("    expiryDate=[").append(expiryDate).append("]\n");
        s.append("    minutes=[").append(minutes).append("]\n");
        s.append("]");

        return s.toString();
    }

}




