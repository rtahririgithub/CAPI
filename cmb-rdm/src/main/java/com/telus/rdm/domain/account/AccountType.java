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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author x113300
 *
 */
public enum AccountType {

	
	BUSINESS("B","B"), CONSUMER("I", "R"), CORPORATE("C", "C");
	
	
	private String primary;
	
	private String secondary;
	
	private AccountType(String primary, String secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public static AccountType fromValue(String primary, String secondary) {
		for (AccountType type : values()) {
			if (type.primary.equals(primary) && type.secondary.equals(secondary)) {
				return type;
			}
		}
		return null;
	}
	
	public String getPrimary() {
		return primary;
	}
	
	public String getSecondary() {
		return secondary;
	}
	
	public boolean isBusiness() {
		return primary.equals("B");
	}
	
	public boolean isConsumer() {
		return primary.equals("I");
	}
	
	public boolean isCorporate() {
		return primary.equals("C");
	}
	
	public static Collection<AccountType> getAllTypesOfPrimary(String primary) {
		List<AccountType> result = new ArrayList<AccountType>();
		for (AccountType type : values()) {
			if (type.primary.equals(primary)) {
				result.add(type);
			}
		}
		return result;
	}
	
}
