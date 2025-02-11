package com.telus.eas.account.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import com.telus.api.fleet.FleetClass;
import com.telus.eas.framework.info.Info;

@Deprecated
public class FleetClassInfo extends Info implements FleetClass{

   static final long serialVersionUID = 1L;

  private String code="";
  private String description="" ;
  private String descriptionFrench = "" ;
  private int maximumMemberIdInRange;


  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }


  public String getCode() {
    return code;
  }


  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public int getMaximumMemberIdInRange() {
     return maximumMemberIdInRange;
  }
  public void setMaximumMemberIdInRange(int maximumMemberIdInRange) {
    this.maximumMemberIdInRange = maximumMemberIdInRange;
  }
}