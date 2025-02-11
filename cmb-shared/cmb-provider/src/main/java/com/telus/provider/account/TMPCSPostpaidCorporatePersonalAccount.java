package com.telus.provider.account;

import com.telus.api.account.PCSPostpaidCorporatePersonalAccount;
import com.telus.eas.account.info.PostpaidCorporatePersonalAccountInfo;
import com.telus.provider.TMProvider;

/**
 * @author Roman Tov
 * @version 1.0, 30-Nov-2006
 */

public class TMPCSPostpaidCorporatePersonalAccount extends TMPCSPostpaidBusinessPersonalAccount
  implements PCSPostpaidCorporatePersonalAccount {

private final PostpaidCorporatePersonalAccountInfo delegate;

public TMPCSPostpaidCorporatePersonalAccount(TMProvider provider,
                                            PostpaidCorporatePersonalAccountInfo
                                            delegate) {
  super(provider, delegate);
  this.delegate = delegate;
}

//--------------------------------------------------------------------
//  Decorative Methods
//--------------------------------------------------------------------
public int hashCode() {
  return delegate.hashCode();
}

public String toString() {
  return delegate.toString();
}
}
