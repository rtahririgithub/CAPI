package com.telus.eas.utility.info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.sql.*;

public class ServicePeriodHoursInfo extends Info implements ServicePeriodHours {
  static final long serialVersionUID = 1L;

  private int day;
  private Time from;
  private Time to;

  public ServicePeriodHoursInfo() {
  }

  public int getDay() {
    return day;
  }

  public Time  getFrom() {
    return from;
  }

  public Time getTo() {
    return to;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public void setFrom(Time from) {
    this.from = from;
  }

  public void setTo(Time to) {
    this.to = to;
  }

}