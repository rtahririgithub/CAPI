/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.cmb.jws.AccountStatusChangeHistory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.AccountStatus;


/**
 * @author Edmir
 *
 */
public class AccountStatusChangeMapper extends AbstractSchemaMapper<AccountStatusChangeHistory, StatusChangeHistoryInfo> {

	public AccountStatusChangeMapper(){
		super(AccountStatusChangeHistory.class, StatusChangeHistoryInfo.class);
		
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected AccountStatusChangeHistory performSchemaMapping(StatusChangeHistoryInfo source, AccountStatusChangeHistory target) {
	   
		target = new AccountStatusChangeHistory();  
	    target.setDate(source.getDate());
	    if (source.getBanStatus().equalsIgnoreCase("S"))
			target.setAccountStatus(AccountStatus.S);
		else if(source.getBanStatus().equalsIgnoreCase("O"))
			target.setAccountStatus(AccountStatus.O);
		else if(source.getBanStatus().equalsIgnoreCase("N"))
			target.setAccountStatus(AccountStatus.N);
		else if(source.getBanStatus().equalsIgnoreCase("T"))
			target.setAccountStatus(AccountStatus.T);
		else if(source.getBanStatus().equalsIgnoreCase("C"))
			target.setAccountStatus(AccountStatus.C);
		
	    target.setActivityReasonCode(source.getReasonCode());
	    target.setActivityCode(source.getActivityTypeCode());
		return super.performSchemaMapping(source, target);
	}
	
	 
}
