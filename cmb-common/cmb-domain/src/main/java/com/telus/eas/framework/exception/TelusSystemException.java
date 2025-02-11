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

public class TelusSystemException extends TelusException {

  public TelusSystemException() {
    super();
  }

  public TelusSystemException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }

  public TelusSystemException(String id, String message) {
    super(id,message);
  }

  public TelusSystemException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }

  public TelusSystemException(String id) {
    super(id);
  }
}