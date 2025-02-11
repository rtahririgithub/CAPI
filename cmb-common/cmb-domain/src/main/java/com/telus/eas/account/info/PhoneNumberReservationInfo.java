/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import com.telus.eas.utility.info.*;


public class PhoneNumberReservationInfo extends Info implements PhoneNumberReservation {

    static final long serialVersionUID = 1L;

  private NumberGroupInfo numberGroup;
  private String productType;
  private String phoneNumberPattern;
  private boolean asian;
  private boolean likeMatch;
  private boolean waiveSearchFee;

  public NumberGroup getNumberGroup() {
    return numberGroup;
  }

  public NumberGroupInfo getNumberGroup0() {
    return numberGroup;
  }

  public void setNumberGroup(NumberGroup numberGroup) {
   this.numberGroup = (NumberGroupInfo)numberGroup;
  }

  public void setNumberGroup0(NumberGroupInfo numberGroup) {
    this.numberGroup = numberGroup;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getPhoneNumberPattern() {
    return phoneNumberPattern;
  }

  /**
   * Assigns the phone number search pattern.  For <B>Like Matches</B> this
   * should be the 10 digit phone number of an existing subscriber.  Otherwise,
   * it should be 10 digit combination of numbers and asterixes (*), where asterix
   * means any number.  If <CODE>phoneNumberPattern</CODE> contains less than
   * 10 characters, it will be padded with asterixes.
   *
   */
  public void setPhoneNumberPattern(String phoneNumberPattern) {
    while(phoneNumberPattern.length() < 10) {
      phoneNumberPattern += "*";
    }

    this.phoneNumberPattern = phoneNumberPattern;
  }

  public boolean isAsian() {
    return asian;
  }

  public void setAsian(boolean asian) {
    this.asian = asian;
  }

  public boolean isLikeMatch() {
    return likeMatch;
  }

  public void setLikeMatch(boolean likeMatch) {
    this.likeMatch = likeMatch;
  }

  public boolean getWaiveSearchFee() {
    return waiveSearchFee;
  }

  public void setWaiveSearchFee(boolean waiveSearchFee) {
    this.waiveSearchFee = waiveSearchFee;
  }

  public void clear() {
    numberGroup = null;
    productType = null;
    phoneNumberPattern = null;
    asian = false;
    likeMatch = false;
    waiveSearchFee = false;
  }

  public void copyFrom(PhoneNumberReservation phoneNumberReservation) {
  }
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PhoneNumberReservation:{\n");
    s.append("    numberGroup=[").append(numberGroup).append("]\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    phoneNumberPattern=[").append(phoneNumberPattern).append("]\n");
    s.append("    asian=[").append(asian).append("]\n");
    s.append("    likeMatch=[").append(likeMatch).append("]\n");
    s.append("    waiveSearchFee=[").append(waiveSearchFee).append("]\n");
    s.append("}");

    return s.toString();
  }

}



