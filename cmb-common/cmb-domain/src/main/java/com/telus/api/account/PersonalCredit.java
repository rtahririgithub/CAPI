/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;


/**
 * <CODE>PersonalCredit</CODE> encapsulates the information used in a
 * personal credit check.
 *
 */
public interface PersonalCredit {

  /**
   *
   * @link aggregation
   */
  CreditCard getCreditCard();

  Date getBirthDate();

  void setBirthDate(Date birthDate);

  String getSin();

  void setSin(String sin);

  String getDriversLicense();

  void setDriversLicense(String driversLicense);

  Date getDriversLicenseExpiry();

  void setDriversLicenseExpiry(Date driversLicenseExpiry);

  String getDriversLicenseProvince();

  void setDriversLicenseProvince(String driversLicenseProvince);

  boolean isDriversLicenseExpired();
}


