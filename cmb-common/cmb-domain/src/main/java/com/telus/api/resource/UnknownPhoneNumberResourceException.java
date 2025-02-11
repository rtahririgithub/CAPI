package com.telus.api.resource;

import com.telus.api.TelusAPIException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class UnknownPhoneNumberResourceException extends TelusAPIException {

  public UnknownPhoneNumberResourceException(String message, Throwable t) {
    super(message, t);
  }

  public UnknownPhoneNumberResourceException(String message) {
    super(message);
  }

}
