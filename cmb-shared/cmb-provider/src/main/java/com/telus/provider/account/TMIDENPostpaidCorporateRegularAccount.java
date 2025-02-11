package com.telus.provider.account;

import com.telus.api.account.IDENPostpaidCorporateRegularAccount;
import com.telus.eas.account.info.PostpaidCorporateRegularAccountInfo;
import com.telus.provider.TMProvider;

@Deprecated
public class TMIDENPostpaidCorporateRegularAccount extends TMIDENPostpaidBusinessRegularAccount implements IDENPostpaidCorporateRegularAccount {

	private static final long serialVersionUID = 1L;
	private final PostpaidCorporateRegularAccountInfo delegate;

  public TMIDENPostpaidCorporateRegularAccount(TMProvider provider, PostpaidCorporateRegularAccountInfo delegate) {
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



