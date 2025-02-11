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

import javax.inject.Inject;

import com.telus.rdm.annotations.ApplicationService;
import com.telus.rdm.domain.subscriber.Subscriber;
import com.telus.rdm.domain.subscriber.SubscriberRepository;

/**
 * @author x113300
 *
 */

@ApplicationService
public class SubscriberApplicationServiceImpl implements SubscriberApplicationService {

	@Inject private SubscriberRepository subscriberRepository;
	
	@Override
	public void moveSubscriber(String subscriberId) {
		Subscriber subscriber = subscriberRepository.getSubscriber(subscriberId);
		subscriber.move();
	}

	@Override
	public void createSubscriber(Subscriber subscriber) {
		subscriber.create();
	}

	@Override
	public void activateSubscriber(String subscriberId) {
		Subscriber subscriber = subscriberRepository.getSubscriber(subscriberId);
		subscriber.activate();
	}
	
	@Override
	public void townSubscriber(String subscriberId) {
		Subscriber subscriber = subscriberRepository.getSubscriber(subscriberId);
		subscriber.town();
	}
}
