package com.telus.cmb.endpoint.mapper.datasharing;

import com.telus.api.account.SubscriberDataSharingDetail.DataSharingDetail;
import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.cmb.schema.DataSharingInfo;

public class DataSharingInfoMapper extends AbstractSchemaMapper<DataSharingInfo, DataSharingDetail> {

	public DataSharingInfoMapper() {
		super(DataSharingInfo.class, DataSharingDetail.class);
	}

	@Override
	protected DataSharingInfo performSchemaMapping(DataSharingDetail source, DataSharingInfo target) {
		target.setDataSharingGroupCode(source.getDataSharingGroupCode());
		target.getDataSharingSocList().addAll(new DataSharingSocInfoMapper().mapToSchema(source.getDataSharingSocList()));
		return target;
	}
}
