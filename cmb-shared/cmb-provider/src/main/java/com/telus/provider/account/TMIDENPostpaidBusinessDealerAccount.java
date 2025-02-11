package com.telus.provider.account;

import com.telus.api.account.IDENPostpaidBusinessDealerAccount;
import com.telus.eas.account.info.PostpaidBusinessDealerAccountInfo;
import com.telus.provider.TMProvider;

@Deprecated
public class TMIDENPostpaidBusinessDealerAccount extends TMIDENPostpaidBusinessRegularAccount implements IDENPostpaidBusinessDealerAccount {

	private static final long serialVersionUID = 1L;
	private final PostpaidBusinessDealerAccountInfo delegate;

	public TMIDENPostpaidBusinessDealerAccount(TMProvider provider, PostpaidBusinessDealerAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

}
