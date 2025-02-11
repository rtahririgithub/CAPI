package com.telus.cmb.jws.mapping.account_information_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.accountinformationtypes_v3.ProductSubscriberList;


public class ProductSubscriberListMapper extends AbstractSchemaMapper<ProductSubscriberList, ProductSubscriberListInfo> {

	public ProductSubscriberListMapper(){
		super(ProductSubscriberList.class, ProductSubscriberListInfo.class);
	}

	
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
