/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */


package com.telus.eas.account.info;

import com.telus.api.account.*;


public class PostpaidBoxedConsumerAccountInfo extends PostpaidConsumerAccountInfo implements PagerPostpaidBoxedConsumerAccount {

	 static final long serialVersionUID = 1L;

  public static PostpaidBoxedConsumerAccountInfo newPagerInstance0() {
    return new PostpaidBoxedConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PAGER_BOXED);
  }

  public static PostpaidConsumerAccountInfo newPagerInstance() {
    return newPagerInstance0();
  }


  protected PostpaidBoxedConsumerAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }


  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidBoxedConsumerAccountInfo:{\n");
    s.append(super.toString());
    s.append("}");

    return s.toString();
  }

}




