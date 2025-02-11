package com.telus.api.rules.message;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.rules.RulesException;
import com.telus.api.rules.RulesProcessor;

/**
 * @author R. Fong
 * @version 1.0, 17-Jul-2008 
 **/
public interface MessageRulesProcessor extends RulesProcessor {
	
	public MessageContext newMessageContext(int category, int transactionType, Account sourceAccount, Account targetAccount, 
			MessageSubscriberContext[] subscriberContexts, boolean includeReservedSubscribers, int banSizeThreshold)
	throws RulesException, TelusAPIException;	
	
	public MessageSubscriberContext newMessageSubscriberContext(Subscriber subscriber, Contract contract)
	throws RulesException, TelusAPIException;
	
}
