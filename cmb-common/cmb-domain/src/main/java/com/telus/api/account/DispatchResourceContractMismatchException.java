package com.telus.api.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.telus.api.*;

public class DispatchResourceContractMismatchException extends TelusAPIException{

  public DispatchResourceContractMismatchException(String message, Throwable exception) {
       super(message, exception);
     }

     public DispatchResourceContractMismatchException(Throwable exception) {
       super(exception);
     }

}