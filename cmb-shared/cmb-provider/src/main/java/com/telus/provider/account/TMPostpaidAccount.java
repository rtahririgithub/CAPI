package com.telus.provider.account;

import java.util.Date;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.SubscribersByDataSharingGroupResult;
import com.telus.eas.account.info.AccountInfo;
import com.telus.provider.TMProvider;

public abstract class TMPostpaidAccount extends TMAccount implements PostpaidAccount {

	private static final long serialVersionUID = 1L;
	
	private final AccountInfo delegate;
	
	public TMPostpaidAccount(TMProvider provider, AccountInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}

	
	public SubscribersByDataSharingGroupResult[] getSubscribersByDataSharingGroups(
			String[] codes,Date effectiveDate) throws LimitExceededException, TelusAPIException {
		
		try {
			return provider.getAccountInformationHelper().retrieveSubscribersByDataSharingGroupCodes(delegate, codes, effectiveDate);
		} catch (Throwable e) {
			provider.getExceptionHandler().handleException(e);
		}
		return null;
		
	}
}
