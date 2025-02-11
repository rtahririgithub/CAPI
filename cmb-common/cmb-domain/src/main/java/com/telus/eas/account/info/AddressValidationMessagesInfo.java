package com.telus.eas.account.info;
/**
 * Title:        AddressValidationMessagesInfo<p>
 * Description:  The AddressValidationMessagesInfo holds the messages returned from an address validation.<p>
 * Copyright:    Copyright (c) Peter Frei<p>
 * Company:      Telus Mobility Inc<p>
 * @author Peter Frei
 * @version 1.0
 */

import com.telus.eas.framework.info.*;

public class AddressValidationMessagesInfo extends Info {

 static final long serialVersionUID = 1L;

  private String code = "";
  private String message = "";
  private String severity = "";

  public AddressValidationMessagesInfo(){}

  public String getCode() {
    return code;
  }
  public void setCode(String newCode) {
    code = newCode;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String newMessage) {
    message = newMessage;
  }
  public String getSeverity() {
    return severity;
  }
  public void setSeverity(String newSeverity) {
    severity = newSeverity;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("AddressValidationMessagesInfo:{");
    //s.append("    =[").append().append("]\n");
    s.append("    code=[").append(code).append("], ");
    s.append("    message=[").append(message).append("], ");
    s.append("    severity=[").append(severity).append("]");
    s.append("}");

    return s.toString();
  }

}
