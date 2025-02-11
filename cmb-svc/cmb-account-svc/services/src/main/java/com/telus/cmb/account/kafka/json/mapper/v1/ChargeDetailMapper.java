package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.account_v1_0.ChargeDetail;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.framework.info.ChargeInfo;

public class ChargeDetailMapper extends AbstractSchemaMapper<ChargeDetail, ChargeInfo> {
	
	private static ChargeDetailMapper INSTANCE = null;

	public ChargeDetailMapper() {
		super(ChargeDetail.class, ChargeInfo.class);
	}

	public static synchronized ChargeDetailMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ChargeDetailMapper();
		}
		
		return INSTANCE;
	}
	
	@Override
	protected ChargeDetail performSchemaMapping(ChargeInfo source,ChargeDetail target) {
		target.setChargeSeqNo(String.valueOf(source.getId()));
		target.setBillSequenceNo(String.valueOf(source.getBillSequenceNo()));
		target.setBilledInd(source.isBilled());
		return super.performSchemaMapping(source, target);
	}
}
