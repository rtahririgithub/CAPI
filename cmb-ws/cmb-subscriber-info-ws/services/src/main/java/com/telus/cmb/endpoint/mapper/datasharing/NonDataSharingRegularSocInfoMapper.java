package com.telus.cmb.endpoint.mapper.datasharing;

import com.telus.api.account.SubscriberDataSharingDetail.NonDataSharingRegularSoc;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.FamilyTypeList;
import com.telus.cmb.schema.NonDataSharingRegularSocInfo;

public class NonDataSharingRegularSocInfoMapper extends AbstractSchemaMapper<NonDataSharingRegularSocInfo, NonDataSharingRegularSoc> {

	public NonDataSharingRegularSocInfoMapper() {
		super(NonDataSharingRegularSocInfo.class, NonDataSharingRegularSoc.class);
	}

	@Override
	protected NonDataSharingRegularSocInfo performSchemaMapping(NonDataSharingRegularSoc source, NonDataSharingRegularSocInfo target) {
		if (source.getFamilyTypes() != null) {
			if (target.getFamilyTypeList() == null) {
				target.setFamilyTypeList(new FamilyTypeList());
			}
			target.getFamilyTypeList().getFamilyType().addAll(source.getFamilyTypes());
		}
		target.setSocCode(source.getSocCode());
		target.setSocRecurringChargeAmt(source.getSocRecurringCharge());
		return target;
	}
}
