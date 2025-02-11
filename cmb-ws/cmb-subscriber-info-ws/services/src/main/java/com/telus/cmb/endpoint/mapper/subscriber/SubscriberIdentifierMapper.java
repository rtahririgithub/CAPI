package com.telus.cmb.endpoint.mapper.subscriber;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberIdentifier;

public class SubscriberIdentifierMapper extends AbstractSchemaMapper<SubscriberIdentifier, SubscriberIdentifierInfo> {

	public SubscriberIdentifierMapper() {
		super(SubscriberIdentifier.class, SubscriberIdentifierInfo.class);
	}

	@Override
	protected SubscriberIdentifierInfo performDomainMapping(SubscriberIdentifier source, SubscriberIdentifierInfo target) {
		if (source.getSubscriptionId() != null) {
			target.setSubscriptionId(source.getSubscriptionId());
		}
		target.setSubscriberId(source.getSubscriberId());
		target.setBan(Integer.parseInt(source.getBillingAccountNumber()));
		target.setPhoneNumber(source.getPhoneNumber());
		target.setSeatGroup(source.getSeatGroupId());
		target.setSeatType(source.getSeatTypeCd());
		return super.performDomainMapping(source, target);
	}

	@Override
	protected SubscriberIdentifier performSchemaMapping(SubscriberIdentifierInfo source, SubscriberIdentifier target) {
		target.setSubscriberId(source.getSubscriberId());
		target.setSubscriptionId(source.getSubscriptionId());
		target.setBillingAccountNumber(Integer.toString(source.getBan()));
		target.setPhoneNumber(source.getPhoneNumber());
		target.setSeatGroupId(source.getSeatGroup());
		target.setSeatTypeCd(source.getSeatType());
		return super.performSchemaMapping(source, target);
	}
}
