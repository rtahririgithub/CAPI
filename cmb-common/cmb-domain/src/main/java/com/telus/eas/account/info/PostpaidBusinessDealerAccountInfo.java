/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;


public class PostpaidBusinessDealerAccountInfo extends PostpaidBusinessRegularAccountInfo implements IDENPostpaidBusinessDealerAccount, PCSPostpaidBusinessDealerAccount {
	 static final long serialVersionUID = 1L;

  public static PostpaidBusinessDealerAccountInfo newIDENInstance0() {
    return new PostpaidBusinessDealerAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_IDEN_DEALER);
  }

  public static PostpaidBusinessDealerAccountInfo newPCSInstance0() {
    return new PostpaidBusinessDealerAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_PCS_DEALER);
  }

  public static PostpaidBusinessRegularAccountInfo newPCSInstance() {
    return newPCSInstance0();
  }


  public static PostpaidBusinessRegularAccountInfo newIDENInstance() {
    return newIDENInstance0();
  }
  public static PostpaidBusinessDealerAccountInfo getNewInstance0(char accountSubType) {
	    return new PostpaidBusinessDealerAccountInfo(ACCOUNT_TYPE_BUSINESS, accountSubType);
  }

  protected PostpaidBusinessDealerAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidBusinessDealerAccountInfo:{\n");
    s.append(super.toString());
    s.append("}");

    return s.toString();
  }

}



