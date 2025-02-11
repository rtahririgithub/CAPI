package com.telus.cmb.endpoint.mapper.subscriber;

import com.telus.api.account.SubscribersByDataSharingGroupResult.DataSharingSubscribers;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup.DataSharingSubscriberList;

public class DataSharingSubscriberListMapper extends AbstractSchemaMapper<DataSharingSubscriberList, DataSharingSubscribers> {

	public DataSharingSubscriberListMapper() {
		super(DataSharingSubscriberList.class, DataSharingSubscribers.class);
	}

	@Override
	protected DataSharingSubscriberList performSchemaMapping(DataSharingSubscribers source, DataSharingSubscriberList target) {
		target.setContributingInd(source.isContributing());
		target.setSubscriberId(source.getSubscriberId());
		return target;
	}

}
