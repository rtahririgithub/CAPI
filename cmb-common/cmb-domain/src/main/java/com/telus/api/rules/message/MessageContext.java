package com.telus.api.rules.message;

import com.telus.api.account.Account;
import com.telus.api.rules.Context;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface MessageContext extends Context {
	
	// constant for specifying max BAN size threshold
	static final int USE_MAX_BAN_SIZE_THRESHOLD = -1;
	
	int getBanSizeThreshold();
	Account getSourceAccount();
	Account getTargetAccount();
	MessageSubscriberContext[] getSubscriberContexts();
	boolean includeReservedSubscribers();
	
}
