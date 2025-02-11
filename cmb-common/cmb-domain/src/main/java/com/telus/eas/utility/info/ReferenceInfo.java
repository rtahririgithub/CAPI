

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ReferenceInfo extends Info implements Reference {

  public static final long serialVersionUID = 1L;

  protected String code = "";
  protected String description = "";
  protected String descriptionFrench = "";

  public ReferenceInfo() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String newCode) {
    code = newCode;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public String toString() {
    StringBuffer s = new StringBuffer(128);

    s.append(getClass().getName() + ":[\n");
    s.append("    code=[").append(getCode()).append("]\n");
    s.append("    description=[").append(getDescription()).append("]\n");
    s.append("    descriptionFrench=[").append(getDescriptionFrench()).append("]\n");
    s.append("]");

    return s.toString();
  }
}


