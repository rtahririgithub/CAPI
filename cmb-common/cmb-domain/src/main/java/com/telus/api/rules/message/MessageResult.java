package com.telus.api.rules.message;

import com.telus.api.message.ApplicationMessage;
import com.telus.api.rules.Result;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface MessageResult extends Result {
	
	/**
	 * Returns the unique set of messages applicable to all subscribers.
	 * 
	 * @return ApplicationMessage[]
	 */
	ApplicationMessage[] getResults();
	
	/**
	 * Returns the array of messages applicable to the given subscriber ID.
	 * 
	 * @param subscriberId subscriber ID
	 * @return ApplicationMessage[]
	 */
	ApplicationMessage[] getResultsBySubscriberId(String subscriberId);
	
	/**
	 * Returns the array of subscriber IDs who have messages in this result.
	 * 
	 * @return String[]
	 */
	String[] getSubscriberIds();
}
