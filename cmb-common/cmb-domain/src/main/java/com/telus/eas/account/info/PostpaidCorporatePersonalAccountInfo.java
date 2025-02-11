package com.telus.eas.account.info;

import com.telus.api.account.PCSPostpaidCorporatePersonalAccount;

/**
 * @author Roman Tov
 * @version 1.0, 29-Nov-2006
 */

public class PostpaidCorporatePersonalAccountInfo extends PostpaidBusinessPersonalAccountInfo implements
    PCSPostpaidCorporatePersonalAccount {
  static final long serialVersionUID = 1L;

  public static PostpaidCorporatePersonalAccountInfo newInstance0(char
      accountSubtype) {
    return new PostpaidCorporatePersonalAccountInfo(ACCOUNT_TYPE_CORPORATE,
        accountSubtype);
  }

  public static PostpaidCorporatePersonalAccountInfo newInstance(char
      accountSubtype) {
    return newInstance0(accountSubtype);
  }

  protected PostpaidCorporatePersonalAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }
}
