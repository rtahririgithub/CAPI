package com.telus.eas.subscriber.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2004
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class SubscriberHistoryInfo extends Info implements SubscriberHistory {

  static final long serialVersionUID = 1L;

  private Date date;
  private char status;
  private String activityCode;
  private String activityReasonCode;
  private int previousBanId;
  private int nextBanId;
  private int brandId;


  public SubscriberHistoryInfo() {

  }


  public Date getDate() {
    return date;
  }

  public char getStatus() {
    return status;
  }

  public String getActivityCode() {
    return activityCode;
  }

  public String getActivityReasonCode() {
    return activityReasonCode;
  }

  public int getPreviousBanId() {
    return previousBanId;
  }

  public int getNextBanId() {
    return nextBanId;
  }

  public int getBrandId() {
	  return brandId;
  }
  
  public void setDate(Date date) {
    this.date = date;
  }

  public void setStatus(char status) {
    this.status = status;
  }

  public void setActivityCode(String activityCode) {
    this.activityCode = activityCode;
  }

  public void setActivityReasonCode(String activityReasonCode) {
    this.activityReasonCode = activityReasonCode;
  }

  public void setPreviousBanId(int previousBanId) {
    this.previousBanId = previousBanId;
  }

  public void setNextBanId(int nextBanId) {
    this.nextBanId = nextBanId;
  }
  
  public void setBrandId(int brandId) {
	this.brandId = brandId;
  }

   public String toString() {

       StringBuffer s = new StringBuffer(128);
       s.append("SubscriberHistoryInfo:[\n");
       s.append("    date=[").append(date).append("]\n");
       s.append("    status=[").append(status).append("]\n");
       s.append("    activityCode=[").append(activityCode).append("]\n");
       s.append("    activityReasonCode=[").append(activityReasonCode).append("]\n");
       s.append("    previousBanId=[").append(previousBanId).append("]\n");
       s.append("    nextBanId=[").append(nextBanId).append("]\n");
       s.append("    brandId=[").append(brandId).append("]\n");
       s.append("]");
       return s.toString();
    }

}

