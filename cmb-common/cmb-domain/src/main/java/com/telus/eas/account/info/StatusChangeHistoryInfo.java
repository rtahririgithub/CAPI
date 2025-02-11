/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class StatusChangeHistoryInfo extends Info implements StatusChangeHistory {

   static final long serialVersionUID = 1L;

  private Date date;
  private String activityTypeCode;
  private String reasonCode;
  private String banStatus;

  public StatusChangeHistoryInfo() {
  }

  public Date getDate() {
    return date;
  }

  public String getActivityTypeCode() {
    return activityTypeCode;
  }

  public String getReasonCode() {
    return reasonCode;
  }

  public String getBanStatus() {
    return banStatus;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public void setActivityTypeCode(String activityTypeCode) {
    this.activityTypeCode = activityTypeCode;
  }

  public void setReasonCode(String reasonCode) {
    this.reasonCode = reasonCode;
  }

  public void setBanStatus(String banStatus) {
    this.banStatus = banStatus;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("StatusChangeHistoryInfo:[\n");
        s.append("    date=[").append(date).append("]\n");
        s.append("    activityTypeCode=[").append(activityTypeCode).append("]\n");
        s.append("    reasonCode=[").append(reasonCode).append("]\n");
        s.append("    banStatus=[").append(banStatus).append("]\n");
        s.append("]");

        return s.toString();
    }

}



