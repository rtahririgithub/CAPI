/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ActivationOptionInfo;
import com.telus.eas.account.info.ActivationOptionTypeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.ActivationOption;

public class ActivationOptionMapper extends AbstractSchemaMapper<ActivationOption, ActivationOptionInfo> {

	public ActivationOptionMapper() {
		super(ActivationOption.class, ActivationOptionInfo.class);
	}

	@Override
	protected ActivationOptionInfo performDomainMapping(ActivationOption source, ActivationOptionInfo target) {
		
		if (source.getActivationOptionName() != null) {
			target.setOptionType(new ActivationOptionTypeInfo(source.getActivationOptionName().value()));
		}
		target.setDeposit(source.getDepositAmount() == null ? 0.0 : source.getDepositAmount());
		target.setCreditLimit(source.getCreditLimitAmount() == null ? 0.0 : source.getCreditLimitAmount());
		target.setCreditClass(source.getCreditClass());
		target.setMaxContractTerm(source.getMaxCLPContractTerm() == null ? 0 : source.getMaxCLPContractTerm());
		
		return super.performDomainMapping(source, target);
	}
	
}