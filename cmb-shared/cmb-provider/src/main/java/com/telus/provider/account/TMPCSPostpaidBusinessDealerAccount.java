package com.telus.provider.account;


import com.telus.api.account.PCSPostpaidBusinessDealerAccount;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.provider.TMProvider;


public class TMPCSPostpaidBusinessDealerAccount extends TMPCSPostpaidBusinessRegularAccount implements PCSPostpaidBusinessDealerAccount {
	private static final long serialVersionUID = 1L;
	private final PostpaidBusinessDealerAccountInfo delegate;

  public TMPCSPostpaidBusinessDealerAccount(TMProvider provider, PostpaidBusinessDealerAccountInfo delegate) {
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