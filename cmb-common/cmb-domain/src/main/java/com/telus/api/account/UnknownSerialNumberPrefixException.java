
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.account;



public class UnknownSerialNumberPrefixException extends InvalidSerialNumberException {


  public UnknownSerialNumberPrefixException (String message, Throwable exception, String serialNumber) {
    super(message, exception, serialNumber);

  }

  public UnknownSerialNumberPrefixException(Throwable exception, String serialNumber) {
    super(exception, serialNumber);
  }

  public UnknownSerialNumberPrefixException(String message, String serialNumber) {
    super(message, serialNumber);
  }

}