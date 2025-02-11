package com.telus.provider.account;

import com.telus.api.account.*;
import com.telus.provider.TMProvider;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;

/**
 * <p>Title: Client API</p>
 *
 * <p>Description: Client API</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Telus Mobility</p>
 *
 * @author Michael Qin
 * @version 1.0
 */
public class TMPCSPostpaidCorporateRegularAccount
    extends TMPCSPostpaidBusinessRegularAccount implements PCSPostpaidCorporateRegularAccount {

  private final PostpaidCorporateRegularAccountInfo delegate;

  public TMPCSPostpaidCorporateRegularAccount(TMProvider provider,
                                           PostpaidCorporateRegularAccountInfo
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
