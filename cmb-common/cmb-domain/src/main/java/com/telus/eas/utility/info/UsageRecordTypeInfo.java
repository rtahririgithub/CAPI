/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class UsageRecordTypeInfo extends Info implements UsageRecordType {

  static final long serialVersionUID = 1L;

  private String description;
  private String descriptionFrench;
  private String code;
  private static final UsageRecordTypeInfo[] list = {
      new UsageRecordTypeInfo("01", "Airtime", "Airtime"),
      new UsageRecordTypeInfo("02", "Additional Charges", "Additional Charges"),
      new UsageRecordTypeInfo("03", "Fees", "Fees"),
      new UsageRecordTypeInfo("04", "Others", "Others"),
      new UsageRecordTypeInfo("05", "Long Distance", "Long Distance"),
      new UsageRecordTypeInfo("06", "Free Minutes", "Free Minutes"),
      new UsageRecordTypeInfo("07", "Account Pool", "Account Pool"),
  };

  public UsageRecordTypeInfo() {
  }

  public UsageRecordTypeInfo(String code, String description, String descriptionFrench) {
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

  public static UsageRecordTypeInfo[] getAll() {
    return list;
  }
}



