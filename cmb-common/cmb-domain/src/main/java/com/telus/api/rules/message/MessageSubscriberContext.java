package com.telus.api.rules.message;

import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.rules.Context;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface MessageSubscriberContext extends Context {
	
	Subscriber getSubscriber();
	Contract getNewContract();
	
}
