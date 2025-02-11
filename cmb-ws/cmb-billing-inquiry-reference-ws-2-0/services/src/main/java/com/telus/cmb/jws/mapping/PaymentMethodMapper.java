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

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PaymentMethodInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v2.RefPaymentMethod;


/**
 * @author Tsz Chung Tong
 *
 */
public class PaymentMethodMapper extends ReferenceMapper<RefPaymentMethod, PaymentMethodInfo> {

	public PaymentMethodMapper() {
		super(RefPaymentMethod.class, PaymentMethodInfo.class);
	}
	
}
