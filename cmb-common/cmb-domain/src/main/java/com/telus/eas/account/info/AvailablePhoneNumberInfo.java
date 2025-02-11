/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;
import com.telus.eas.utility.info.*;


public class AvailablePhoneNumberInfo extends Info implements AvailablePhoneNumber {

   static final long serialVersionUID = 1L;

  private String phoneNumber;
  private String numberLocationCode;
  private NumberGroupInfo numberGroup;

  public AvailablePhoneNumberInfo() {
  }

  public String getPhoneNumber(){
    return phoneNumber;
  }

  public String getNumberLocationCode(){
    return numberLocationCode;
  }

  public NumberGroup getNumberGroup(){
    return numberGroup;
  }

  public NumberGroupInfo getNumberGroup0(){
    return numberGroup;
  }

  public void setPhoneNumber(String phoneNumber){
    this.phoneNumber = phoneNumber;
  }

  public void setNumberLocationCode(String numberLocationCode){
    this.numberLocationCode = numberLocationCode;
  }

  public void setNumberGroup(NumberGroupInfo numberGroup){
    this.numberGroup = numberGroup;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("AvailablePhoneNumberInfo:[\n");
        s.append("    phoneNumber=[").append(phoneNumber).append("]\n");
        s.append("    numberLocationCode=[").append(numberLocationCode).append("]\n");
        s.append("    numberGroup=[").append(numberGroup).append("]\n");
        s.append("]");

        return s.toString();
    }

}


