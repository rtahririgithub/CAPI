package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.cmb.jws.ServiceChangeHistory;

public class ServiceChangeMapper extends AbstractSchemaMapper<ServiceChangeHistory, ServiceChangeHistoryInfo> {
	private static ServiceChangeMapper INSTANCE = null;
	
	public ServiceChangeMapper( ){
		super(ServiceChangeHistory.class, ServiceChangeHistoryInfo.class);
	}
	
	public static synchronized ServiceChangeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ServiceChangeMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected ServiceChangeHistory performSchemaMapping(ServiceChangeHistoryInfo source, ServiceChangeHistory target) {
		
		target.setApplicationID(source.getApplicationID());
		target.setDealerCode(source.getDealerCode());	
		target.setEffectiveDate(source.getDate());
		target.setExpirationDate(source.getNewExpirationDate());
		target.setKnowbilityOperatorID(source.getKnowbilityOperatorID());
		target.setSalesRepId(source.getSalesRepId());
		target.setServiceCode(source.getServiceCode());
		return super.performSchemaMapping(source, target);
		
	}
	
}
