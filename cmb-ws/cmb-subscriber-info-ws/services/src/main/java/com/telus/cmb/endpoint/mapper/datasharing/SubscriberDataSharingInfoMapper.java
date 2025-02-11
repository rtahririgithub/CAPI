package com.telus.cmb.endpoint.mapper.datasharing;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.SubscriberDataSharingInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;

public class SubscriberDataSharingInfoMapper extends AbstractSchemaMapper<SubscriberDataSharingInfo, SubscriberDataSharingDetailInfo> {

	public SubscriberDataSharingInfoMapper() {
		super(SubscriberDataSharingInfo.class, SubscriberDataSharingDetailInfo.class);
	}

	@Override
	protected SubscriberDataSharingInfo performSchemaMapping(SubscriberDataSharingDetailInfo source, SubscriberDataSharingInfo target) {
		target.setSubscriberId(source.getSubscriberId());
		target.setSubscriptionId(source.getSubscriptionId());
		target.setContractStartDate(source.getContractStartDate());
		target.setPricePlanCode(source.getPricePlanCode());
		target.setPricePlanRecurringChargeAmt(source.getPricePlanRecurringCharge());
		target.getNonDataSharingRegularSocList().addAll(new NonDataSharingRegularSocInfoMapper().mapToSchema(source.getNonDataSharingRegularSocList()));
		target.getDataSharingInfoList().addAll(new DataSharingInfoMapper().mapToSchema(source.getDataSharingInfoList()));
		return super.performSchemaMapping(source, target);
	}

}