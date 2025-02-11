package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class BusinessRoleInfo extends Info implements BusinessRole {

  static final long serialVersionUID = 1L;

  protected String code;
  protected String description;
  protected String descriptionFrench ;

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

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }

  public String toString() {

    StringBuffer s = new StringBuffer(128);

    s.append("BusinessRoleInfo:[\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
    s.append("]");

    return s.toString();

  }


}

