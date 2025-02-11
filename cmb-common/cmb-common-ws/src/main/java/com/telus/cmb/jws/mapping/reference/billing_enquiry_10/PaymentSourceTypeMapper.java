/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.reference.billing_enquiry_10;

import com.telus.cmb.jws.mapping.reference.customer_information_10.ReferenceMapper;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_0.PaymentSourceType;

/**
 * @author Pavel Simonovsky
 *
 */
public class PaymentSourceTypeMapper extends ReferenceMapper<PaymentSourceType, PaymentSourceTypeInfo> {

	public PaymentSourceTypeMapper() {
		super(PaymentSourceType.class, PaymentSourceTypeInfo.class);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.ws.mapping.reference.ReferenceMapper#performSchemaMapping(com.telus.api.reference.Reference, com.telus.schemas.eca.reference_management_service_1_0.Reference)
	 */
	@Override
	protected PaymentSourceType performSchemaMapping(PaymentSourceTypeInfo source, PaymentSourceType target) {

		target.setSourceId(source.getSourceID());
		target.setSourceType(source.getSourceType());

		return super.performSchemaMapping(source, target);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PaymentSourceTypeInfo performDomainMapping(
			PaymentSourceType source, PaymentSourceTypeInfo target) {
		target.setSourceID(source.getSourceId());
		target.setSourceType(source.getSourceType());
		return super.performDomainMapping(source, target);
	}
	
	
}
