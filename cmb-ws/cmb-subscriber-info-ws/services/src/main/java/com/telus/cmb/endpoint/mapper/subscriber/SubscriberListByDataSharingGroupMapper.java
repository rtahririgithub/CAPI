package com.telus.cmb.endpoint.mapper.subscriber;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberListByDataSharingGroup;

public class SubscriberListByDataSharingGroupMapper extends AbstractSchemaMapper<SubscriberListByDataSharingGroup, SubscribersByDataSharingGroupResultInfo> {

	public SubscriberListByDataSharingGroupMapper() {
		super(SubscriberListByDataSharingGroup.class, SubscribersByDataSharingGroupResultInfo.class);
	}

	@Override
	protected SubscriberListByDataSharingGroup performSchemaMapping(SubscribersByDataSharingGroupResultInfo source, SubscriberListByDataSharingGroup target) {
		target.setDataSharingGroupCode(source.getDataSharingGroupCode());
		target.getDataSharingSubscriberList().addAll(new DataSharingSubscriberListMapper().mapToSchema(source.getDataSharingSubscribers()));
		return target;
	}

}
