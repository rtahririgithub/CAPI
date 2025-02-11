package com.telus.provider.account;

import com.telus.api.account.PCSPostpaidEmployeeAccount;
import com.telus.eas.account.info.PostpaidEmployeeAccountInfo;
import com.telus.provider.TMProvider;

public class TMPCSPostpaidEmployeeAccount extends TMPCSPostpaidConsumerAccount implements PCSPostpaidEmployeeAccount {

	private static final long serialVersionUID = 1L;
	private final PostpaidEmployeeAccountInfo delegate;

	public TMPCSPostpaidEmployeeAccount(TMProvider provider, PostpaidEmployeeAccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}

}
