package com.telus.eas.framework.exception;
/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

import com.telus.eas.framework.info.*;

public class PhoneNumberAlreadyInUseException extends TelusApplicationException {

  public PhoneNumberAlreadyInUseException() {
    super();
  }
  public PhoneNumberAlreadyInUseException(String id, String message) {
    super(id,message);
  }
  public PhoneNumberAlreadyInUseException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }
  public PhoneNumberAlreadyInUseException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }

}