/**
 * 
 */
package com.telus.cmb.endpoint.mapping;

import org.apache.commons.lang.math.NumberUtils;

import com.telus.eas.account.info.AccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.Account;


/**
 * @author x113300
 *
 */
public class AccountMapper extends com.telus.cmb.jws.mapping.account_information_30.AccountMapper {

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.account_information_30.AccountMapper#performDomainMapping(com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.Account, com.telus.eas.account.info.AccountInfo)
	 */
	@Override
	protected AccountInfo performDomainMapping(Account source, AccountInfo target) {
		target = super.performDomainMapping(source, target);
		
		target.setBanId(NumberUtils.toInt(source.getBillingAccountNumber()));
		
		return target;
	}
}
