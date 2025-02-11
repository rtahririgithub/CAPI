package com.telus.provider.rules.message;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.rules.RulesException;
import com.telus.api.rules.message.MessageSubscriberContext;

/**
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public class TMMessageSubscriberContext implements MessageSubscriberContext {

	private Subscriber subscriber;
	private Contract contract; 
	
	public TMMessageSubscriberContext(Subscriber subscriber, Contract contract) {
		this.subscriber = subscriber;
		this.contract = contract;
	}
	
	public static final TMMessageSubscriberContext newMessageSubscriberContext(Subscriber subscriber, Contract contract) 
	throws RulesException, TelusAPIException {
		if (subscriber == null)
			throw new RulesException("Subscriber object is null.", RulesException.REASON_INVALID_SUBSCRIBER);
		
		return new TMMessageSubscriberContext(subscriber, contract);
	}
	
	public Subscriber getSubscriber() {
		return subscriber;
	}

	public Contract getNewContract() {
		return contract;
	}
	
	public int getCategory() {
		throw new UnsupportedOperationException("Method not implemented.");
	}
	
	public int getTransactionType() {
		throw new UnsupportedOperationException("Method not implemented.");
	}

}
