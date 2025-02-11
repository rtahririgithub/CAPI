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

public class TelusValidationException extends TelusException {

  public TelusValidationException() {
    super();
  }

  public TelusValidationException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }

  public TelusValidationException(String id, String message) {
    super(id,message);
  }

  public TelusValidationException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }

  public TelusValidationException(String id) {
    super(id);
  }
}