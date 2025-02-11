/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;
import java.util.*;

public class PersonalCreditInfo extends Info implements PersonalCredit {
  /**
   * @link aggregation
   * @supplierCardinality 1
   */
   static final long serialVersionUID = 1L;

  private CreditCardInfo creditCard = new CreditCardInfo();
  private Date birthDate;
  private String sin;
  private String driversLicense;
  private Date driversLicenseExpiry;
  private String driversLicenseProvince;

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public CreditCardInfo getCreditCard0() {
    return creditCard;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getSin() {
    return sin;
  }

  public void setSin(String sin) {
    this.sin = toUpperCase(sin);
  }

  public String getDriversLicense() {
    return driversLicense;
  }

  public void setDriversLicense(String driversLicense) {
    this.driversLicense = toUpperCase(driversLicense);
  }

  public Date getDriversLicenseExpiry() {
    return driversLicenseExpiry;
  }

  public void setDriversLicenseExpiry(Date driversLicenseExpiry) {
    this.driversLicenseExpiry = driversLicenseExpiry;
  }

  public String getDriversLicenseProvince() {
    return driversLicenseProvince;
  }

  public void setDriversLicenseProvince(String driversLicenseProvince) {
    this.driversLicenseProvince = toUpperCase(driversLicenseProvince);
  }

  public boolean isDriversLicenseExpired() {
    boolean isDriversLicenseExpired = true;

    if (driversLicenseExpiry != null) {
      Calendar today = Calendar.getInstance();

      if (driversLicenseExpiry.getYear() + 1900 == today.get(Calendar.YEAR)) {
        isDriversLicenseExpired = driversLicenseExpiry.getMonth() < today.get(Calendar.MONTH);
      }
      else {
        isDriversLicenseExpired = driversLicenseExpiry.getYear() + 1900 < today.get(Calendar.YEAR);
      }
    }
    return isDriversLicenseExpired;
  }

  public void copyFrom(PersonalCreditInfo o) {
	if ( o==null ) return;
	
    creditCard.copyFrom(o.creditCard);
    setBirthDate(cloneDate(o.birthDate));
    setSin(o.sin);
    setDriversLicense(o.driversLicense);
    setDriversLicenseExpiry(cloneDate(o.driversLicenseExpiry));
    setDriversLicenseProvince(o.driversLicenseProvince);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PersonalCreditInfo:{\n");
    s.append("creditCard=[").append(creditCard).append("]\n");
    s.append("    birthDate=[").append(birthDate).append("]\n");
    s.append("    sin=[").append(sin).append("]\n");
    s.append("    driversLicense=[").append(driversLicense).append("]\n");
    s.append("    driversLicenseExpiry=[").append(driversLicenseExpiry).append("]\n");
    s.append("    driversLicenseProvince=[").append(driversLicenseProvince).append("]\n");
    s.append("}");

    return s.toString();
  }


}




