package com.telus.eas.equipment.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.equipment.EquipmentPossession;

public class EquipmentPossessionInfo extends Info implements EquipmentPossession {

  static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("EquipmentPossessionInfo:{\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
    s.append("}");

    return s.toString();
  }
}
