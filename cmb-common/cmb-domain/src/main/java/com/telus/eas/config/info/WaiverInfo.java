/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.config.info;

import com.telus.api.chargeableservices.*;
import com.telus.eas.framework.info.*;

public class WaiverInfo extends Info implements Waiver {

   static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;

  public WaiverInfo() {
  }

  public WaiverInfo(String code, String description, String descriptionFrench) {
    this.code = code;
    this.description = description;
    this.descriptionFrench = descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code){
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description){
    this.description = description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescriptionFrench(String descriptionFrench){
    this.descriptionFrench = descriptionFrench;
  }

}



