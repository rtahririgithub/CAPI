/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.application.service;

import com.telus.rdm.domain.subscriber.Subscriber;

/**
 * @author x113300
 *
 */
public interface SubscriberApplicationService {

	void moveSubscriber(String subscriberId);

	void createSubscriber(Subscriber subscriber);
	
	void activateSubscriber(String subscriberId);
	
	void townSubscriber(String subscriberId);
	
}
