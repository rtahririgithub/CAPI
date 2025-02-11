/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;


public class ProductTypeInfo extends Info implements ProductType {

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;

  public ProductTypeInfo() {
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

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

    public String toString()
    {

        StringBuffer s = new StringBuffer(128);



        s.append("ProductTypeInfo:[\n");

        s.append("    code=[").append(code).append("]\n");

        s.append("    description=[").append(description).append("]\n");

        s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");

        s.append("]");



        return s.toString();

    }

}



