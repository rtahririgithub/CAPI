package com.telus.api.account;

public class NotAnalogSerialNumberException extends InvalidSerialNumberException {


  public NotAnalogSerialNumberException(String message, Throwable exception, String serialNumber) {
    super(message, exception, serialNumber);
  }

  public NotAnalogSerialNumberException(Throwable exception, String serialNumber) {
    super(exception, serialNumber);
  }

  public NotAnalogSerialNumberException(String message, String serialNumber) {
    super(message, serialNumber);
  }

}
