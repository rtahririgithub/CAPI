package com.telus.eas.framework.exception;

/**
 * Title:        Telus Credit Card Exception
 * Description:  The class is used when we have not succesful credit card payment or gateway is down
 * Copyright:    Copyright (c) 2003
 * Company:      Telus Mobility
 * @author Stefan Pavlov
 * @version 1.0
 */


import com.telus.eas.framework.info.*;

public class TelusCreditCardException extends TelusException {

  /**
   * Attribute ccardMessageEN is reference to credit card exception on english
   */
  private String ccardMessageEN;
  /**
   * Attribute ccardMessageEN is reference to credit card exception on french
   */
  private String ccardMessageFR;

  public TelusCreditCardException() {
    super();
  }

  public TelusCreditCardException(ExceptionInfo pExceptionInfo){
    super(pExceptionInfo);
  }

  public TelusCreditCardException(String id, String message) {
    super(id,message);
  }

  public TelusCreditCardException(String id, String message, Throwable exception) {
    super(id,message,exception);
  }

  public TelusCreditCardException(String id) {
    super(id);
  }


  public TelusCreditCardException(String id, String ccardMessageEN, String ccardMessageFR) {
	  this(id, ccardMessageEN, ccardMessageFR, null);
  }
  
  public TelusCreditCardException(String id, String ccardMessageEN, String ccardMessageFR, Throwable exception) {
	    super(id, ccardMessageEN, exception);
	    this.ccardMessageEN = ccardMessageEN;
	    this.ccardMessageFR = ccardMessageFR;
	  }

  public String getCCardMessageEN() {
    return ccardMessageEN;
  }

  public String getCCardMessageFR() {
    return ccardMessageFR;
  }



}