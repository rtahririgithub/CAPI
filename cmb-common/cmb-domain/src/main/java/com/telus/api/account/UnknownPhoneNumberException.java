package com.telus.api.account;

import com.telus.api.*;

public class UnknownPhoneNumberException extends UnknownObjectException{

 private String phoneNumber;

  public UnknownPhoneNumberException(String message, Throwable exception, String phoneNumber) {
    super(message, exception);
    this.phoneNumber = phoneNumber;
  }

  public UnknownPhoneNumberException(Throwable exception, String phoneNumber) {
    super(exception);
    this.phoneNumber = phoneNumber;
  }

  public UnknownPhoneNumberException(String message, String phoneNumber) {
    super(message);
    this.phoneNumber = phoneNumber;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}