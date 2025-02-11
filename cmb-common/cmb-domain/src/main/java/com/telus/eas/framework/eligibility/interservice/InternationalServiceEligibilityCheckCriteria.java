/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.eas.framework.eligibility.interservice;

import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;

/**
 * @author Pavel Simonovsky
 *
 */
public class InternationalServiceEligibilityCheckCriteria extends DefaultEligibilityCheckCriteria {
	
	private String accountCombinedType = null;
	
	private int brandId = 0;
	
	private String creditClass = null;
	
	private int tenure = 0;
	
	private boolean collectionActivityPresent = false;
	
	private boolean newAccount = false;

	/**
	 * @return the accountCombinedType
	 */
	public String getAccountCombinedType() {
		return accountCombinedType;
	}

	/**
	 * @param accountCombinedType the accountCombinedType to set
	 */
	public void setAccountCombinedType(String accountCombinedType) {
		this.accountCombinedType = accountCombinedType;
	}

	/**
	 * @return the brandId
	 */
	public int getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the creditClass
	 */
	public String getCreditClass() {
		return creditClass;
	}

	/**
	 * @param creditClass the creditClass to set
	 */
	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	/**
	 * @return the tenure
	 */
	public int getTenure() {
		return tenure;
	}

	/**
	 * @param tenure the tenure to set
	 */
	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	/**
	 * @return the collectionActivityPresent
	 */
	public boolean isCollectionActivityPresent() {
		return collectionActivityPresent;
	}

	/**
	 * @param collectionActivityPresent the collectionActivityPresent to set
	 */
	public void setCollectionActivityPresent(boolean collectionActivityPresent) {
		this.collectionActivityPresent = collectionActivityPresent;
	}

	/**
	 * @return the newAccount
	 */
	public boolean isNewAccount() {
		return newAccount;
	}

	/**
	 * @param newAccount the newAccount to set
	 */
	public void setNewAccount(boolean newAccount) {
		this.newAccount = newAccount;
	}

	
}
