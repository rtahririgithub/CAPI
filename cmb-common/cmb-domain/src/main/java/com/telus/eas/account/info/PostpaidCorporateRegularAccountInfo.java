/*
 * $Id$
 *
 * Copyright AbstractNotions 2001, 2002.  All rights reserved.  Proprietary and Confidential.
 */

package com.telus.eas.account.info;

import com.telus.api.account.IDENPostpaidCorporateRegularAccount;
import com.telus.api.account.PCSPostpaidCorporateRegularAccount;

public class PostpaidCorporateRegularAccountInfo
    extends PostpaidBusinessRegularAccountInfo implements
    PCSPostpaidCorporateRegularAccount, IDENPostpaidCorporateRegularAccount {
    static final long serialVersionUID = 1L;

  public static PostpaidCorporateRegularAccountInfo newInstance0(char
      accountSubtype) {
    return new PostpaidCorporateRegularAccountInfo(ACCOUNT_TYPE_CORPORATE,
        accountSubtype);
  }

  public static PostpaidCorporateRegularAccountInfo newInstance(char
      accountSubtype) {
    return newInstance0(accountSubtype);
  }

  protected PostpaidCorporateRegularAccountInfo(char accountType,
                                                char accountSubType) {
    super(accountType, accountSubType);
  }
}
