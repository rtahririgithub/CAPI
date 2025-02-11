package com.telus.provider.rules.message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.telus.api.message.ApplicationMessage;
import com.telus.api.rules.message.MessageResult;

/**
 * @author R. Fong
 * @version 1.0, 09-Jul-2008 
 **/
public class TMMessageResult implements MessageResult {

	private int category;
	private Map messagesBySubscriber;
	
	public TMMessageResult(int category, Map messages) {
		this.category = category;
		this.messagesBySubscriber = messages;
	}
	
	public int getCategory() {
		return category;
	}
	
	public ApplicationMessage[] getResults() {
		
		Set codes = new HashSet();
		List list = new ArrayList();
		
		// iterate through all the results in the hash map
		Iterator iterator = messagesBySubscriber.keySet().iterator();
		while (iterator.hasNext()) {
			ApplicationMessage[] messages = (ApplicationMessage[])messagesBySubscriber.get((String)iterator.next());
			for (int i = 0; i < messages.length; i++) {
				// only add distinct messages to the message set
				if (!codes.contains(messages[i].getCode())) {
					codes.add(messages[i].getCode());
					list.add(messages[i]);
				}
			}
		}
		
		return (ApplicationMessage[])list.toArray(new ApplicationMessage[list.size()]);
	}
	
	public ApplicationMessage[] getResultsBySubscriberId(String subscriberId) {
		return (ApplicationMessage[])messagesBySubscriber.get(subscriberId);
	}
	
	public String[] getSubscriberIds() {
		return (String[])messagesBySubscriber.keySet().toArray(new String[messagesBySubscriber.keySet().size()]);
	}

}
