/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class TermUnitInfo extends Info implements TermUnit {

  static final long serialVersionUID = 1L;

  private String description;
  private String descriptionFrench;
  private String code;

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public void setCode(String code) {
    this.code = code;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("TermUnitInfo:[\n");
        s.append("    description=[").append(description).append("]\n");
        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
        s.append("    code=[").append(code).append("]\n");
        s.append("]");

        return s.toString();
    }

}




