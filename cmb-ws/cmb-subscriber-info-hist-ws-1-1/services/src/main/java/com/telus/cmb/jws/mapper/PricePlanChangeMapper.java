package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.cmb.jws.PricePlanChangeHistory;

public class PricePlanChangeMapper extends AbstractSchemaMapper<PricePlanChangeHistory, PricePlanChangeHistoryInfo> {
	private static PricePlanChangeMapper INSTANCE = null;
	
	public PricePlanChangeMapper( ){
		super(PricePlanChangeHistory.class, PricePlanChangeHistoryInfo.class);
	}
	
	public static synchronized PricePlanChangeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PricePlanChangeMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected PricePlanChangeHistory performSchemaMapping(PricePlanChangeHistoryInfo source, PricePlanChangeHistory target) {
		
		target.setApplicationID(source.getApplicationID());
		target.setDealerCode(source.getDealerCode());	
		target.setEffectiveDate(source.getDate());
		target.setExpirationDate(source.getNewExpirationDate());
		target.setKnowbilityOperatorID(source.getKnowbilityOperatorID());
		target.setNewPricePlanCode(source.getNewPricePlanCode());
		target.setOldPricePlanCode(source.getOldPricePlanCode());
		target.setSalesRepId(source.getSalesRepId());
		return super.performSchemaMapping(source, target);
		
	}
	
	
}
