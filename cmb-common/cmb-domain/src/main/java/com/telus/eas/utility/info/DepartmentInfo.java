
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class DepartmentInfo extends Info implements Department {

  static final long serialVersionUID = 1L;

 protected String code;
  protected String description;
  protected String descriptionFrench;
  protected String defaultWorkPosition;

  public String getCode() {
    return code;
  }



  public String getDescription() {
    return description;
  }


  public String getDescriptionFrench() {
    return descriptionFrench;
  }


  public String getDefaultWorkPosition() {
    return defaultWorkPosition;
  }


  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }


  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }


  public void setDefaultWorkPosition(String newDefaultWorkPosition) {
    defaultWorkPosition = newDefaultWorkPosition;
  }





}