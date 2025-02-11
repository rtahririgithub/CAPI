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

import com.telus.eas.utility.info.ChargeTypeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.ChargeType;

/**
 * @author Tsz Chung Tong
 *
 */
public class ChargeTypeMapper extends BillAdjustmentMapper<ChargeType, ChargeTypeInfo> {

	public ChargeTypeMapper() {
		super(ChargeType.class, ChargeTypeInfo.class);
	}
	
	@Override
	protected ChargeType performSchemaMapping(ChargeTypeInfo source, ChargeType target) {

		target.setProductType(source.getProductType());
		target.setLevel(String.valueOf(source.getLevel()));
		target.setBalanceImpact(String.valueOf(source.getBalanceImpact()));

		return super.performSchemaMapping(source, target);
	}
}
