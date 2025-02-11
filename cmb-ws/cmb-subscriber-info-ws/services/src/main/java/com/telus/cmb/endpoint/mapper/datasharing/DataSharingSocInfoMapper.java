package com.telus.cmb.endpoint.mapper.datasharing;

import com.telus.api.account.SubscriberDataSharingDetail.DataSharingSoc;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.DataSharingSocInfo;
import com.telus.cmb.schema.FamilyTypeList;

public class DataSharingSocInfoMapper extends AbstractSchemaMapper<DataSharingSocInfo, DataSharingSoc> {

	public DataSharingSocInfoMapper() {
		super(DataSharingSocInfo.class, DataSharingSoc.class);
	}

	@Override
	protected DataSharingSocInfo performSchemaMapping(DataSharingSoc source, DataSharingSocInfo target) {
		target.setContributingInd(source.getContributingInd());
		target.setDataSharingSocCode(source.getDataSharingSocCode());
		target.setDataSharingSpentAmt(source.getDataSharingSpentAmt());
		if (source.getFamilyTypes() != null) {
			if (target.getFamilyTypeList() == null) {
				target.setFamilyTypeList(new FamilyTypeList());
			}
			target.getFamilyTypeList().getFamilyType().addAll(source.getFamilyTypes());
		}

		return target;
	}
}
