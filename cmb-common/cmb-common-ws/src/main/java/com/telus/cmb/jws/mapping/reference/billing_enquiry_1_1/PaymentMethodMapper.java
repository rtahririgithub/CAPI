/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.billing_enquiry_1_1;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PaymentMethodInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_1.RefPaymentMethod;


/**
 * @author Pavel Simonovsky
 *
 */
public class PaymentMethodMapper extends ReferenceMapper<RefPaymentMethod, PaymentMethodInfo> {

	public PaymentMethodMapper() {
		super(RefPaymentMethod.class, PaymentMethodInfo.class);
	}
	
}
