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

public class TelusApplicationException extends TelusException {

  public TelusApplicationException() {
    super();
  }

  public TelusApplicationException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }

  public TelusApplicationException(String id, String message) {
    super(id,message);
  }

  public TelusApplicationException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }

  public TelusApplicationException(String id) {
    super(id);
  }
}