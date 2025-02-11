/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class UsageUnitInfo extends Info implements UsageUnit {

  static final long serialVersionUID = 1L;

  private String description;
  private String descriptionFrench;
  private String code;
  private static final UsageUnitInfo[] list = {
      new UsageUnitInfo("C", "Free Day", "Free Day"),
      new UsageUnitInfo("D", "Dollar", "Dollar"),
      new UsageUnitInfo("M", "Minute", "Minute"),
      new UsageUnitInfo("O", "Call", "Call"),
      new UsageUnitInfo("S", "Message", "Message")
  };

  public UsageUnitInfo() {
  }

  public UsageUnitInfo(String code, String description, String descriptionFrench) {
    this.code = code;
    this.description = description;
    this.descriptionFrench = descriptionFrench;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  public static UsageUnitInfo[] getAll() {
    return list;
  }
/**
 * @param code The code to set.
 */
public void setCode(String code) {
	this.code = code;
}
/**
 * @param description The description to set.
 */
public void setDescription(String description) {
	this.description = description;
}
/**
 * @param descriptionFrench The descriptionFrench to set.
 */
public void setDescriptionFrench(String descriptionFrench) {
	this.descriptionFrench = descriptionFrench;
}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UsageUnitInfo[");
		buffer.append("code = ").append(code);
		buffer.append(" description = ").append(description);
		buffer.append(" descriptionFrench = ").append(descriptionFrench);
		buffer.append("]");
		return buffer.toString();
	}}



