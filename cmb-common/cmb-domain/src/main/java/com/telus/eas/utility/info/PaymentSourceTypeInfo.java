/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class PaymentSourceTypeInfo extends Info implements PaymentSourceType {

  static final long serialVersionUID = 1L;

  private String description;
  private String sourceID;
  private String descriptionFrench;
  private String sourceType;

  public PaymentSourceTypeInfo() {
  }

  public String getCode() {
    return (sourceType.trim() + sourceID.trim()) ;
  }

  public String getDescription() {
    return description;
  }


  public String getSourceID() {
    return sourceID;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public void setSourceID(String sourceID) {
    this.sourceID = sourceID;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

    public String toString()
    {

        StringBuffer s = new StringBuffer(128);



        s.append("PaymentSourceTypeInfo:[\n");
        s.append("   sourceType=[").append(sourceType).append("]\n");
        s.append("    sourceID=[").append(sourceID).append("]\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");

        s.append("]");



        return s.toString();

    }
  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }
  public String getSourceType() {
    return sourceType;
  }

}



