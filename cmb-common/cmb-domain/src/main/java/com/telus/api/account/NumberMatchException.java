/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


/**
 * <CODE>NumberMatchException</CODE>
 *
 */
public class NumberMatchException extends TelusAPIException {
  /**
   * @link aggregation
   */
  private PhoneNumberReservation phoneNumberReservation;

  public NumberMatchException(String message, Throwable exception, PhoneNumberReservation phoneNumberReservation) {
    super(message, exception);
    this.phoneNumberReservation = phoneNumberReservation;
  }

  public NumberMatchException(Throwable exception, PhoneNumberReservation phoneNumberReservation) {
    super(exception);
    this.phoneNumberReservation = phoneNumberReservation;
  }

  public NumberMatchException(String message, PhoneNumberReservation phoneNumberReservation) {
    super(message);
    this.phoneNumberReservation = phoneNumberReservation;
  }

  public PhoneNumberReservation getPhoneNumberReservation() {
    return phoneNumberReservation;
  }
}


