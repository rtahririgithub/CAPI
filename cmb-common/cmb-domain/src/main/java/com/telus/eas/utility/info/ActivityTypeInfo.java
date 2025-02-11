/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import java.util.*;


public class ActivityTypeInfo extends Info implements ActivityType {

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String activityType;
  private ReasonType[] reasonTypes;
  private String descriptionFrench;

  public ActivityTypeInfo() {
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }


  public String getActivityType() {
    return activityType;
  }

  public ReasonType[] getReasonTypes() {
    return reasonTypes;
  }

  public ReasonType[] getManualReasonTypes() {
    List list = new ArrayList(reasonTypes.length);
    for (int i = 0; i < reasonTypes.length; i++) {
      if (reasonTypes[i].getProcessCode() != null && reasonTypes[i].getProcessCode().trim().equals("MAN")) {
        list.add(reasonTypes[i]);
      }
    }

    return (ReasonType[])list.toArray(new ReasonType[list.size()]);
  }

  public ReasonType getReasonType(String code) {
    if(code != null) {
      code = code.trim();

      for (int i = 0; i < reasonTypes.length; i++) {
        if (reasonTypes[i].getCode().trim().equals(code)) {
          return reasonTypes[i];
        }
      }
    }

    return null;
  }


  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  public void setReasonTypes(ReasonType[] reasonTypes) {
    this.reasonTypes = reasonTypes;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

    public String toString()
    {

        StringBuffer s = new StringBuffer(128);



        s.append("ActivityTypeInfo:[\n");
        s.append("    code=[").append(code).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    activityType=[").append(activityType).append("]\n");
      // s.append("    reasonTypes=[").append(reasonTypes).append("]\n");
      if(reasonTypes == null || reasonTypes.length == 0) {
      s.append("    reasonTypes={}\n");
      } else {
      for(int i=0; i<reasonTypes.length; i++) {
        s.append("    reasonTypes["+i+"]=[").append(reasonTypes[i]).append("]\n");
      }
    }
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("]");



        return s.toString();

    }


}



