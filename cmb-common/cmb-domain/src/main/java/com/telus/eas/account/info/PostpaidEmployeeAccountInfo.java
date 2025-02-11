/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */


package com.telus.eas.account.info;

import com.telus.api.account.*;


public class PostpaidEmployeeAccountInfo  extends PostpaidConsumerAccountInfo implements PCSPostpaidEmployeeAccount, IDENPostpaidEmployeeAccount {
	 static final long serialVersionUID = 1L;

  public static PostpaidEmployeeAccountInfo newPCSInstance0() {
    return new PostpaidEmployeeAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE);
  }

  public static PostpaidEmployeeAccountInfo newIDENInstance0() {
    return new PostpaidEmployeeAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_IDEN_TELUS_EMPLOYEE);
  }

  public static PostpaidEmployeeAccountInfo newPCSInstance1() {
    return new PostpaidEmployeeAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PCS_TELUS_EMPLOYEE_NEW);
  }

  public static PostpaidEmployeeAccountInfo newIDENInstance1() {
    return new PostpaidEmployeeAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_IDEN_PERSONAL);
  }

  public static PostpaidEmployeeAccountInfo getNewInstance0(char accountSubType) {
	    return new PostpaidEmployeeAccountInfo(ACCOUNT_TYPE_CONSUMER, accountSubType);
}
  
  public static PostpaidConsumerAccountInfo newPCSInstance() {
    return newPCSInstance0();
   }

   public static PostpaidConsumerAccountInfo newIDENInstance() {
    return newIDENInstance0();
   }


  protected PostpaidEmployeeAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }




  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidEmployeeAccountInfo:{\n");
    s.append(super.toString());
    s.append("}");

    return s.toString();
  }

}




