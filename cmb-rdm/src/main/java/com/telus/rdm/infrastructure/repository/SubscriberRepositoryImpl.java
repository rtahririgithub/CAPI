/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.repository;

import javax.inject.Inject;

import com.telus.rdm.annotations.DomainRepository;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.domain.subscriber.Subscriber;
import com.telus.rdm.domain.subscriber.SubscriberRepository;
import com.telus.rdm.infrastructure.persistence.SubscriberDao;

/**
 * @author x113300
 *
 */

@DomainRepository
public class SubscriberRepositoryImpl implements SubscriberRepository {

	@Inject private SubscriberDao subscriberDao;
	
	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}

	@Override
	public Subscriber getSubscriber(String subscriberId) {
		Subscriber subscriber = subscriberDao.getSubscriber(subscriberId);
		
		// TODO: replace with lazy load proxy
		Customer customer = subscriberDao.getCustomer(subscriberId);
		subscriber.setCustomer(customer);
		
		// TODO: replace with lazy load proxy
		ConsumerName contactName = subscriberDao.getContactName(subscriberId);
		subscriber.setContactName(contactName);
		
		return subscriber;
	}
}
