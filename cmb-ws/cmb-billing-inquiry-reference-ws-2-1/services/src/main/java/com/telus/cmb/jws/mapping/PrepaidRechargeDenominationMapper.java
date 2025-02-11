/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping;

import com.telus.eas.utility.info.PrepaidRechargeDenominationInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.PrepaidRechargeDenomination;

/**
 * @author Pavel Simonovsky
 *
 */
public class PrepaidRechargeDenominationMapper extends AbstractSchemaMapper<PrepaidRechargeDenomination, PrepaidRechargeDenominationInfo> {
	
	public PrepaidRechargeDenominationMapper() {
		super(PrepaidRechargeDenomination.class, PrepaidRechargeDenominationInfo.class);
	}

	@Override
	protected PrepaidRechargeDenomination performSchemaMapping(PrepaidRechargeDenominationInfo source, PrepaidRechargeDenomination target) {

		target.setAmount(source.getAmount());
		target.setCode(source.getCode());
		target.setContainsPrivilegeInd(true);
		target.setDescription(source.getDescription());
		target.setDescriptionFrench(source.getDescriptionFrench());
		target.setRateId(source.getRateId());
		target.setRechargeType(source.getRechargeType());
		
		return super.performSchemaMapping(source, target);
	}
}
