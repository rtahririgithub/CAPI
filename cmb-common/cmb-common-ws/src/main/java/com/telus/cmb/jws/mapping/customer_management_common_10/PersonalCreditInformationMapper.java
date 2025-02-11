/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_10;

import com.telus.api.TelusAPIException;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PersonalCreditInformation;

/**
 * @author Dimitry Siganevich
 *
 */
public class PersonalCreditInformationMapper extends AbstractSchemaMapper<PersonalCreditInformation, PersonalCreditInfo> {

	public PersonalCreditInformationMapper(){
		super(PersonalCreditInformation.class, PersonalCreditInfo.class);
	}


	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PersonalCreditInfo performDomainMapping(PersonalCreditInformation source, PersonalCreditInfo target) {
		if (source.getBirthDate() != null)
			target.setBirthDate(source.getBirthDate());
		target.setDriversLicense(source.getDriversLicense());
		if (source.getDriversLicenseExpiry() != null)
			target.setDriversLicenseExpiry(source.getDriversLicenseExpiry());
		if (source.getDriversLicenseProvince() != null)
			target.setDriversLicenseProvince(source.getDriversLicenseProvince().value());
		target.setSin(source.getSin());

		if (source.getCreditCard() != null) {
			String token = source.getCreditCard().getToken();
			String first6 = source.getCreditCard().getFirst6();
			String last4 = source.getCreditCard().getLast4();
			if (token != null && !token.equals("")  &&
				first6 != null && !first6.equals("")  &&	
				last4 != null && !last4.equals("")  ) {
			
				try {
					target.getCreditCard().setToken(token,first6 , last4);
				} catch (TelusAPIException e) {
					e.printStackTrace();
				}
			}
			target.getCreditCard().setAuthorizationCode(source.getCreditCard().getAuthorizationCode());
			target.getCreditCard().setExpiryMonth(source.getCreditCard().getExpiryMonth());
			target.getCreditCard().setExpiryYear(source.getCreditCard().getExpiryYear());
			target.getCreditCard().setHolderName(source.getCreditCard().getHolderName());
		}
		return super.performDomainMapping(source, target);
	}


}
