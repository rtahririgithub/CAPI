package com.telus.cmb.jws.mapper;

import com.telus.api.account.Subscriber;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.SubscriberHistoryInfo;
import com.telus.cmb.jws.SubscriberStatusHistory;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberStatus;
public class SubscriberStatusChangeMapper extends AbstractSchemaMapper<SubscriberStatusHistory, SubscriberHistoryInfo> {
	private static SubscriberStatusChangeMapper INSTANCE = null;
	
	public SubscriberStatusChangeMapper( ){
		super(SubscriberStatusHistory.class, SubscriberHistoryInfo.class);
	}
	
	public static synchronized SubscriberStatusChangeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SubscriberStatusChangeMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected SubscriberStatusHistory performSchemaMapping(SubscriberHistoryInfo source, SubscriberStatusHistory target) {
		
		target.setActivityCode(source.getActivityCode());
		target.setActivityReasonCode(source.getActivityReasonCode());
		target.setBrandId(source.getBrandId());	
		target.setNextBillingAccountNumber(Integer.toString(source.getNextBanId()));
		target.setPreviousBillingAccountNumber(Integer.toString(source.getPreviousBanId()));
		if (source.getStatus() == Subscriber.STATUS_CANCELED) {
			target.setStatus(SubscriberStatus.C);
		} else if (source.getStatus() == Subscriber.STATUS_ACTIVE) {
			target.setStatus(SubscriberStatus.A);
		} else if (source.getStatus() == Subscriber.STATUS_RESERVED) {
			target.setStatus(SubscriberStatus.R);
		} else if (source.getStatus() == Subscriber.STATUS_SUSPENDED) {
			target.setStatus(SubscriberStatus.S);
		}
		target.setStatusDate(source.getDate());
		return super.performSchemaMapping(source, target);
		
	}

}
