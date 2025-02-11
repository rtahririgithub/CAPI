package com.telus.provider.account;

import com.telus.api.account.PCSPostpaidBusinessOfficialAccount;
import com.telus.eas.account.info.PostpaidBusinessOfficialAccountInfo;
import com.telus.provider.TMProvider;


public class TMPCSPostpaidBusinessOfficialAccount  extends TMPCSPostpaidBusinessRegularAccount implements PCSPostpaidBusinessOfficialAccount {

	private static final long serialVersionUID = 1L;
	private final PostpaidBusinessOfficialAccountInfo delegate;

  public TMPCSPostpaidBusinessOfficialAccount(TMProvider provider, PostpaidBusinessOfficialAccountInfo delegate) {
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