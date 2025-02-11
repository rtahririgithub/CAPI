/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.IDENPostpaidBusinessPersonalAccount;
import com.telus.api.account.PCSPostpaidBusinessPersonalAccount;
import com.telus.api.account.PagerPostpaidBusinessPersonalAccount;

public class PostpaidBusinessPersonalAccountInfo extends PostpaidConsumerAccountInfo implements IDENPostpaidBusinessPersonalAccount, PCSPostpaidBusinessPersonalAccount,
		PagerPostpaidBusinessPersonalAccount {
	
	static final long serialVersionUID = 1L;
	
	private BusinessRegistrationInfo businessRegistration; 

	public static PostpaidBusinessPersonalAccountInfo newPCSInstance0() {
		return new PostpaidBusinessPersonalAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_PCS_PERSONAL);
	}

	public static PostpaidBusinessPersonalAccountInfo newIDENInstance0() {
		return new PostpaidBusinessPersonalAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_IDEN_PERSONAL);
	}

	public static PostpaidConsumerAccountInfo newPCSInstance() {
		return newPCSInstance0();
	}

	public static PostpaidConsumerAccountInfo newIDENInstance() {
		return newIDENInstance0();
	}

	public static PostpaidBusinessPersonalAccountInfo getNewInstance0(char accountSubType) {
		return new PostpaidBusinessPersonalAccountInfo(ACCOUNT_TYPE_BUSINESS, accountSubType);
	}

	protected PostpaidBusinessPersonalAccountInfo(char accountType, char accountSubType) {
		super(accountType, accountSubType);
	}

	/**
	 * This method has been modified to use the ConsumerName name object in the parent class.
	 * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
	 * 
	 * @see #getName
	 * @see #getName0 
	 */
	public String getLegalBusinessName() {
		return super.getName().getAdditionalLine();
	}

	/**
	 * This method has been modified to use the ConsumerName name object in the parent class.
	 * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
	 * 
	 * @see #getName
	 * @see #getName0 
	 */
	public void setLegalBusinessName(String legalBusinessName) {
		super.getName().setAdditionalLine((toUpperCase(legalBusinessName)));
	}

	/**
	 * This method has been modified to use the ConsumerName name object in the parent class.
	 * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
	 *  
	 * @see #getName
	 * @see #getName0 
	 */
	public String getOperatingAs() {
		return super.getAdditionalLine();
	}

	/**
	 * This method has been modified to use the ConsumerName name object in the parent class.
	 * Legal business name may be accessed via getAdditionalLine and setAdditionalLine methods there.
	 *  
	 * @see #getName
	 * @see #getName0 
	 */
	public void setOperatingAs(String operatingAs) {
		super.setAdditionalLine(operatingAs);
	}

	public BusinessRegistrationInfo getBusinessRegistration() {
		return businessRegistration;
	}

	public void setBusinessRegistration(BusinessRegistrationInfo businessRegistration) {
		this.businessRegistration = businessRegistration;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();
		s.append("PostpaidBusinessPersonalAccountInfo:{\n");
		s.append(super.toString());
	    s.append("    businessRegistration=[").append(businessRegistration).append("]\n");
		s.append("}");

		return s.toString();
	}

}
