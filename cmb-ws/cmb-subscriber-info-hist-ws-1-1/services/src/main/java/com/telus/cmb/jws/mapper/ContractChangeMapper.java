package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.cmb.jws.ContractChangeHistory;



public class ContractChangeMapper extends AbstractSchemaMapper<ContractChangeHistory , ContractChangeHistoryInfo> {
	private static ContractChangeMapper INSTANCE = null;
	
	public ContractChangeMapper( ){
		super(ContractChangeHistory.class, ContractChangeHistoryInfo.class);
	}
	
	public static synchronized ContractChangeMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ContractChangeMapper();
		}
		
		return INSTANCE;
	}

	@Override
	protected ContractChangeHistory performSchemaMapping(ContractChangeHistoryInfo source, ContractChangeHistory target) {
		
		target.setApplicationID(source.getApplicationID());
		target.setCommitmentEndDate(source.getNewCommitmentEndDate());
		target.setCommitmentInMonths(Integer.toString(source.getNewCommitmentMonths()));
		target.setCommitmentStartDate(source.getNewCommitmentStartDate());
		target.setDealerCode(source.getDealerCode());
		target.setEffectiveDate(source.getDate());
		target.setKnowbilityOperatorID(source.getKnowbilityOperatorID());
		target.setReasonCode(source.getReasonCode());
		target.setSalesRepId(source.getSalesRepId());
		
		return super.performSchemaMapping(source, target);		
	}
	 

}
