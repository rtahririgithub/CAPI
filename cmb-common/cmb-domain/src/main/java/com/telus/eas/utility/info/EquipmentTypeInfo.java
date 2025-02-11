package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class EquipmentTypeInfo extends Info implements EquipmentType {

   static final long serialVersionUID = 1L;

  protected String code  ;
  protected String description ;
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


  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }


  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }


}