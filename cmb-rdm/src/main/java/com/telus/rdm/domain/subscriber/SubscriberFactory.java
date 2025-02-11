/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.subscriber;

import com.telus.rdm.annotations.DomainFactory;

/**
 * @author x113300
 *
 */

@DomainFactory
public class SubscriberFactory {

	public Subscriber createSubscriber() {
		Subscriber subscriber = new Subscriber();
		
		return subscriber;
	}
}
