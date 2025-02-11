package com.telus.provider.account;

import com.telus.api.account.IDENPostpaidEmployeeAccount;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.provider.TMProvider;

@Deprecated
public class TMIDENPostpaidEmployeeAccount extends TMIDENPostpaidConsumerAccount implements IDENPostpaidEmployeeAccount {

	private static final long serialVersionUID = 1L;
	private final PostpaidEmployeeAccountInfo delegate;

	public TMIDENPostpaidEmployeeAccount(TMProvider provider, PostpaidEmployeeAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}

}