/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.UsageSummary;
import com.telus.eas.framework.info.Info;

public class UsageSummaryInfo extends Info implements UsageSummary {

   static final long serialVersionUID = 1L;

  private String featureCode;
  private String unitOfMeasureCode;
  private String phoneNumber;

  public UsageSummaryInfo() {
  }

  public String getFeatureCode() {
    return featureCode;
  }

  public void setFeatureCode(String featureCode) {
    this.featureCode = featureCode;
  }

  public String getUnitOfMeasureCode() {
    return unitOfMeasureCode;
  }

  public void setUnitOfMeasureCode(String unitOfMeasureCode) {
    this.unitOfMeasureCode = unitOfMeasureCode;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}




