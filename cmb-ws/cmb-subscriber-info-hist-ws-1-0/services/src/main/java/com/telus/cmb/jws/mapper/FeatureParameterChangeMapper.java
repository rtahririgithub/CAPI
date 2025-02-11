package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.cmb.jws.FeatureParameterHistory;


public class FeatureParameterChangeMapper extends AbstractSchemaMapper<FeatureParameterHistory , FeatureParameterHistoryInfo> {
	private static FeatureParameterChangeMapper INSTANCE = null;
	
	public FeatureParameterChangeMapper( ){
		super(FeatureParameterHistory.class, FeatureParameterHistoryInfo.class);
	}
	
	public static synchronized FeatureParameterChangeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new FeatureParameterChangeMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected FeatureParameterHistory performSchemaMapping(FeatureParameterHistoryInfo source, FeatureParameterHistory target) {
		target.setApplicationID(source.getApplicationID());		
		target.setCreationDate(source.getCreationDate());
		target.setEffectiveDate(source.getEffectiveDate());
		target.setExpirationDate(source.getExpirationDate());
		target.setFeatureCode(source.getFeatureCode());	
		target.setFeatureSequenceNum(source.getFeatureSequenceNo());
		target.setKnowbilityOperatorID(source.getKnowbilityOperatorID());
		target.setParameterName(source.getParameterName());
		target.setParameterValue(source.getParameterValue());
		target.setServiceCode(source.getServiceCode());
		target.setServiceSequenceNum(source.getServiceSequenceNo());
		target.setServiceVersionNum(source.getServiceVersionNo());
		target.setUpdateDate(source.getUpdateDate());
		
		
		return super.performSchemaMapping(source, target);		
	}
	

}
