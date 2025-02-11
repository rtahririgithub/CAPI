package com.telus.eas.framework.info;

/**
 * Title:        Telus - Amdocs Domain Beans
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Telus Mobility
 * @author Michael Lapish
 * @version 1.0
 */

import java.util.Locale;

public class ReferenceCodeInfo extends Info implements ReferenceInterface, java.io.Serializable {

  static final long serialVersionUID = 1L;

  String code = "";
  String codeDescE = "";
  String codeDescF = "";
  String codeDescExternalE = "";
  String codeDescExternalF = "";

  public ReferenceCodeInfo() {
  }

  public ReferenceCodeInfo(String pCode, String pCodeDescE, String pCodeDescF) {
    code = pCode;
    codeDescE = pCodeDescE;
    codeDescF = pCodeDescF;
  }

  public ReferenceCodeInfo(String pCode, String pCodeDescE, String pCodeDescF, String pCodeDescExternalE, String pCodeDescExternalF) {
    code = pCode;
    codeDescE = pCodeDescE;
    codeDescF = pCodeDescF;
    codeDescExternalE = pCodeDescExternalE;
    codeDescExternalF = pCodeDescExternalF;
  }

  public int getID() {
    /**@todo: Implement this com.telus.eas.framework.info.Reference method*/
    //throw new java.lang.UnsupportedOperationException("Method getID() not supported.");
    return -1;
  }
  public String getCode() {
    return code;
  }
  public String toString() {
    /**@todo: Implement this com.telus.eas.framework.info.ReferenceInterface method*/
    //throw new java.lang.UnsupportedOperationException("Method toString() not yet implemented.");
    return codeDescE;
  }
  public String getValue() {
    return codeDescE;
  }
  public String getValue(Locale locale) {
    if (locale.equals(Locale.CANADA_FRENCH) || locale.equals(Locale.FRENCH)){
      if (codeDescF == null || codeDescF.equals(""))
        return codeDescE;
      return codeDescF;
    } else {
      return codeDescE;
    }
  }
  public String getExternalValue() {
    if (codeDescExternalE == null || codeDescExternalE.equals("")){
      return codeDescE;
    } else {
      return codeDescExternalE;
    }
  }
  public String getExternalValue(Locale locale) {
    if (locale.equals(Locale.CANADA_FRENCH) || locale.equals(Locale.FRENCH)){
      if (codeDescExternalF == null || codeDescExternalF.equals("")){
        if (codeDescExternalE == null || codeDescExternalE.equals("")){
          return codeDescE;
        }
        return codeDescExternalE;
      }
      return codeDescExternalF;
    } else {
      return codeDescExternalE;
    }
  }

}