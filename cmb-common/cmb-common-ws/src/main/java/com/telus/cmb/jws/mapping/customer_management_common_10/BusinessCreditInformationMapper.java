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

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.BusinessCreditInformation;

/**
 * @author Dimitry Siganevich
 *
 */
public class BusinessCreditInformationMapper extends AbstractSchemaMapper<BusinessCreditInformation, BusinessCreditInfo> {

	public BusinessCreditInformationMapper(){
		super(BusinessCreditInformation.class, BusinessCreditInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BusinessCreditInformation performSchemaMapping(BusinessCreditInfo source, BusinessCreditInformation target) {
		target.setIncorporationDate(source.getIncorporationDate());
		target.setIncorporationNumber(source.getIncorporationNumber());

		return super.performSchemaMapping(source, target);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected BusinessCreditInfo performDomainMapping(BusinessCreditInformation source, BusinessCreditInfo target) {
        target.setIncorporationDate(source.getIncorporationDate());
        target.setIncorporationNumber(source.getIncorporationNumber());
		
		return super.performDomainMapping(source, target);
	}


}
