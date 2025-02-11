package com.telus.eas.equipment.info;

import com.telus.api.reference.EquipmentMode;
import com.telus.eas.framework.info.Info;

public class EquipmentModeInfo extends Info implements EquipmentMode {

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
		StringBuffer buffer = new StringBuffer();
		buffer.append("EquipmentModeInfo[");
		buffer.append("code = ").append(code);
		buffer.append(" description = ").append(description);
		buffer.append(" descriptionFrench = ").append(descriptionFrench);
		buffer.append("]");
		return buffer.toString();
	}}
