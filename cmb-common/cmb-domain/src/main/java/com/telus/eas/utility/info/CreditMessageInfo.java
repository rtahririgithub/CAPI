/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class CreditMessageInfo extends Info implements CreditMessage{

  static final long serialVersionUID = 1L;	 

  protected String message ;
  protected String messageFrench;
  protected String code;
  protected boolean referToCreditAnalyst ;


  public void setMessage(String newMessage) {
    message = newMessage;
  }

  public void setMessageFrench(String newMessageFrench) {
    messageFrench = newMessageFrench;
  }


  public void setCode(String newCode) {
    code = newCode;
  }


  public void setReferToCreditAnalyst(boolean newReferToCreditAnalyst) {
    referToCreditAnalyst = newReferToCreditAnalyst;
  }


   public String getMessage() {
    return message;
  }



  public String getMessageFrench() {
    return messageFrench;
  }


  public String getCode() {
    return code;
  }


  public boolean isReferToCreditAnalyst() {
    return referToCreditAnalyst;
  }

  public String getDescription(){
        return message;
    }

  public String getDescriptionFrench(){
        return messageFrench;
    }




}