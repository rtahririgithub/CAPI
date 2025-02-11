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

public class ServicePeriodIncludedMinutesInfo extends Info implements ServicePeriodIncludedMinutes {

  static final long serialVersionUID = 1L;

  private String direction;
  private int includedMinutes;

  public ServicePeriodIncludedMinutesInfo() {
  }

  public String getDirection() {
    return direction;
  }

  public int getIncludedMinutes() {

    return includedMinutes;

  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public void setIncludedMinutes(int includedMinutes) {
    this.includedMinutes = includedMinutes;
  }
}