/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.BasePrepaidAccount;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.PCSSubscriber;
import com.telus.api.account.Subscriber;
import com.telus.eas.subscriber.info.SubscriberInfo;

public abstract class BasePrepaidAccountInfo extends AccountInfo implements BasePrepaidAccount{

	 static final long serialVersionUID = 1L;
	
	private ConsumerNameInfo name = new ConsumerNameInfo();	
	private String serialNumber;	
	private boolean blockOutgoing976Numbers;	
	private String homePhone;	
	private String businessPhone;	
	private String businessPhoneExtension;	
	private String otherPhoneType;	
	private String otherPhone;	
	private String otherPhoneExtension;	
	private Date birthDate;
	
	public BasePrepaidAccountInfo(char accountType, char accountSubType) {		
		super(accountType, accountSubType);		
	}
	
	public BasePrepaidAccountInfo() {
		super();
	}
	
	public ConsumerName getName() {		
		return name;		
	}
	
	public ConsumerNameInfo getName0() {		
		return name;		
	}
	
	public String getFullName() {		
		return getName0().getFullName();		
	}
	
	/**
	 * This method has been deprecated in favour of using methods on the name object.
	 * 
	 * @deprecated
	 * @see	#getName
	 * @see #getName0
	 */
	public void setFullName(String fullName) {		
		throw new UnsupportedOperationException("Use getName().setXXX()");		
	}
	
	/**
	 * For Prepaid accounts, additionalLine info is now stored in the name object.
	 * The following getter method overrides the AccountInfo superclass method and provides
	 * access to the name object through the getName method.
	 * 
	 * @see	#getName
	 * @see #getName0
	 */
	public String getAdditionalLine() {
		return getName().getAdditionalLine();
	}
	
	/**
	 * For Prepaid accounts, additionalLine info is now stored in the name object.
	 * The following setter method overrides the AccountInfo superclass method and provides
	 * access to the name object through the getName method.
	 * 
	 * @see	#getName
	 * @see #getName0
	 */
	public void setAdditionalLine(String additionalLine) {
		getName().setAdditionalLine(toUpperCase(additionalLine));
	}
	
	public String getSerialNumber() {		
		return serialNumber;		
	}
	
	public void setSerialNumber(String newSerialNumber) {		
		serialNumber = toUpperCase(newSerialNumber);		
	}

	public boolean getBlockOutgoing976Numbers() {		
		return blockOutgoing976Numbers;		
	}
	
	public void setBlockOutgoing976Numbers(boolean blockOutgoing976Numbers) {		
		this.blockOutgoing976Numbers = blockOutgoing976Numbers;		
	}

	public Date getBirthDate() {		
		return birthDate;		
	}	
	
	public void setBirthDate(Date birthDate) {		
		this.birthDate = birthDate;		
	}	
	
	/**	 
	 * NO-OP
	 */
	public Subscriber newSubscriber(String productType, boolean dealerHasDeposit) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");		
	}	
	
	/**	 
	 * NO-OP	 
	 */	
	public Subscriber newSubscriber(String productType, boolean dealerHasDeposit, String activationFeeChargeCode) throws TelusAPIException {		
		throw new UnsupportedOperationException("Method not implemented here");		
	}	
	
	public void copyFrom(AccountInfo o) {
		
		if(o instanceof BasePrepaidAccountInfo) {			
			copyFrom((BasePrepaidAccountInfo)o);			
		} else {			
			super.copyFrom(o);			
		}		
	}	
	
	public void copyFrom(BasePrepaidAccountInfo o) {
		
		super.copyFrom(o);		
		name.copyFrom(o.name);		
		setSerialNumber(o.serialNumber);		
		blockOutgoing976Numbers = o.blockOutgoing976Numbers;		
		homePhone = o.homePhone;		
		businessPhone = o.businessPhone;		
		businessPhoneExtension = o.businessPhoneExtension;		
		setOtherPhoneType(o.otherPhoneType);		
		otherPhone = o.otherPhone;		
		otherPhoneExtension = o.otherPhoneExtension;		
		birthDate = cloneDate(o.birthDate);		
	}

	public void copyTo(SubscriberInfo o) {
		
		o.getConsumerName().setFirstName(getName().getFirstName());
		o.getConsumerName().setMiddleInitial(getName().getMiddleInitial());
		o.getConsumerName().setLastName(getName().getLastName());
		o.getConsumerName().setTitle(getName().getTitle());
		o.getConsumerName().setGeneration(getName().getGeneration());
		o.getConsumerName().setAdditionalLine(getName().getAdditionalLine());
		o.setLanguage(getLanguage());
		o.setBirthDate(cloneDate(getBirthDate()));
		o.setEmailAddress(getEmail());
	}	
	
	public String toString() {
		
		StringBuffer s = new StringBuffer();
		
		s.append("BasePrepaidAccountInfo:{\n");
		s.append(super.toString());
		s.append("    name=[").append(name).append("]\n");
		s.append("    serialNumber=[").append(serialNumber).append("]\n");
		s.append("    blockOutgoing976Numbers=[").append(blockOutgoing976Numbers).append("]\n");
		s.append("    homePhone=[").append(homePhone).append("]\n");
		s.append("    businessPhone=[").append(businessPhone).append("]\n");
		s.append("    businessPhoneExtension=[").append(businessPhoneExtension).append("]\n");
		s.append("    otherPhoneType=[").append(otherPhoneType).append("]\n");
		s.append("    otherPhone=[").append(otherPhone).append("]\n");
		s.append("    otherPhoneExtension=[").append(otherPhoneExtension).append("]\n");
		s.append("    birthDate=[").append(birthDate).append("]\n");
		s.append("}");
		
		return s.toString();
	}
	
	/**	 
	 * NO-OP	 
	 */	
	public PCSSubscriber newPCSSubscriber() throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	/**	 
	 * NO-OP	 
	 */	
	public PCSSubscriber newPCSSubscriber(String activationFeeChargeCode) throws TelusAPIException{
		throw new UnsupportedOperationException("Method not implemented here");
	}
}