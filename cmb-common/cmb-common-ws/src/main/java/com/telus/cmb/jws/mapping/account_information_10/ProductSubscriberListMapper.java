/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.account_information_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.ProductSubscriberList;

/**
 * @author Dimitry Siganevich
 *
 */
public class ProductSubscriberListMapper extends AbstractSchemaMapper<ProductSubscriberList, ProductSubscriberListInfo> {

	public ProductSubscriberListMapper(){
		super(ProductSubscriberList.class, ProductSubscriberListInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected ProductSubscriberList performSchemaMapping(ProductSubscriberListInfo source, ProductSubscriberList target) {
		target.getActiveSubscriber().addAll(toCollection(source.getActiveSubscribers()));
		target.getCancelledSubscriber().addAll(toCollection(source.getCancelledSubscribers()));
		target.setProductType(source.getProductType());
		target.getReservedSubscriber().addAll(toCollection(source.getReservedSubscribers()));
		target.getSuspendedSubscriber().addAll(toCollection(source.getSuspendedSubscribers()));

		return super.performSchemaMapping(source, target);
	}
}
