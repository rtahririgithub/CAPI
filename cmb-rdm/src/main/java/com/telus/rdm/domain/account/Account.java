/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.domain.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.telus.rdm.domain.DomainRegistry;
import com.telus.rdm.domain.shared.Brand;
import com.telus.rdm.domain.shared.ConsumerName;
import com.telus.rdm.domain.shared.Customer;
import com.telus.rdm.domain.shared.DomainObject;

/**
 * 
 *
 */
public abstract class Account extends DomainObject {

	private static final long serialVersionUID = 1L;

	private Integer accountId;
	
	private Brand brand;
	
	private Customer customer;
	
	private ConsumerName contactName;
	
	public abstract AccountType getType();

	
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
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
		DomainRegistry.getStrategy(AccountCreationStrategy.class).create(this);
	}
	
	@Override
	protected void toString(ToStringBuilder builder) {
		builder.append("accountType", getType());
	}
}
   