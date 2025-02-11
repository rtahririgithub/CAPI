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

import static com.telus.rdm.domain.DomainRegistry.getStrategy;

import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;


/**
 * @author x113300
 *
 */
public class Subscriber {
	
	private String subscriberId;
	
	private Integer accountId;
	
	private Customer customer;
	
	private ConsumerName contactName;
	
	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public ConsumerName getContactName() {
		return contactName;
	}

	public void setContactName(ConsumerName contactName) {
		this.contactName = contactName;
	}

	public void create() {
		getStrategy(SubscriberCreationStrategy.class).create(this);
	}
	
	public void activate() {
		getStrategy(SubscriberActivationStrategy.class).activate(this);
	}
	
	public void move() {
		getStrategy(SubscriberMovingStrategy.class).move(this);
	}
	
	public void town() {
		getStrategy(SubscriberTowningStrategy.class).town(this);
	}
	
}
