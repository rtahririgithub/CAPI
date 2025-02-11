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

public class ReferenceIDInfo extends Info implements ReferenceInterface, java.io.Serializable {

  static final long serialVersionUID = 1L;

  public ReferenceIDInfo() {
  }

  int id;
  String codeDescE = "";
  String codeDescF = "";
  String codeDescExternalE = "";
  String codeDescExternalF = "";

  public ReferenceIDInfo(int pId, String pCodeDescE, String pCodeDescF) {
    id = pId;
    codeDescE = pCodeDescE;
    codeDescF = pCodeDescF;
  }

  public ReferenceIDInfo(int pId, String pCodeDescE, String pCodeDescF, String pCodeDescExternalE, String pCodeDescExternalF) {
    id = pId;
    codeDescE = pCodeDescE;
    codeDescF = pCodeDescF;
    codeDescExternalE = pCodeDescExternalE;
    codeDescExternalF = pCodeDescExternalF;
  }

  public int getID() {
    return id;
  }
  public String getCode() {
    /**@todo: Implement this com.telus.eas.framework.info.Reference method*/
    //throw new java.lang.UnsupportedOperationException("Method getCode() not supported.");
    return "";
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