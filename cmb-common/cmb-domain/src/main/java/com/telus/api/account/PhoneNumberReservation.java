/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.reference.*;


/**
 * <CODE>PhoneNumberReservation</CODE>
 *
 */
public interface PhoneNumberReservation {

  NumberGroup getNumberGroup();

  void setNumberGroup(NumberGroup numberGroup);

  String getProductType();

  void setProductType(String productType);

  String getPhoneNumberPattern();

  /**
   * Assigns the phone number search pattern.  For <B>Like Matches</B> this
   * should be the 10 digit phone number of an existing subscriber.  Otherwise,
   * it should be 10 digit combination of numbers and asterixes (*), where asterix
   * means any number.  If <CODE>phoneNumberPattern</CODE> contains less than
   * 10 characters, it will be padded with asterixes.
   *
   */
  void setPhoneNumberPattern(String phoneNumberPattern);

  boolean isAsian();

  void setAsian(boolean asian);

  boolean isLikeMatch();

  void setLikeMatch(boolean likeMatch);

  boolean getWaiveSearchFee();

  void setWaiveSearchFee(boolean waiveSearchFee);

  void clear();

}



