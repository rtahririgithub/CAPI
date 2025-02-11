/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.persistence;

import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.domain.subscriber.Subscriber;

/**
 * @author x113300
 *
 */
public interface SubscriberDao {

	Subscriber getSubscriber(String subscriberId);

	Customer getCustomer(String subscriberId);
	
	ConsumerName getContactName(String subscriberId);
	
}
