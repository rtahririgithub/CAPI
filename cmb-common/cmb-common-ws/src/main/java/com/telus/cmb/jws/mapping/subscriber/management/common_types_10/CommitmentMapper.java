package com.telus.cmb.jws.mapping.subscriber.management.common_types_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.TimePeriod;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.TransactionType;

public class CommitmentMapper extends AbstractSchemaMapper<Commitment, CommitmentInfo> {
	private static CommitmentMapper INSTANCE = null;

	private CommitmentMapper() {
		super (Commitment.class, CommitmentInfo.class);
	}
	
	public static synchronized CommitmentMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CommitmentMapper();
		}
		
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CommitmentInfo performDomainMapping(Commitment source, CommitmentInfo target) {
		if (source.getTimePeriod() != null) {
			target.setEndDate(source.getTimePeriod().getExpiryDate());
			target.setStartDate(source.getTimePeriod().getEffectiveDate());
		}
		if (source.getContractTerm() != null) {
			target.setMonths(Integer.valueOf(source.getContractTerm()));
		}
		target.setReasonCode(source.getReasonCode());		

		return super.performDomainMapping(source, target);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected Commitment performSchemaMapping(CommitmentInfo source, Commitment target) {
		target.setContractTerm(String.valueOf(source.getMonths()));
		target.setReasonCode(source.getReasonCode());
		target.setRenewalInd(false);
		TimePeriod timePeriod = new TimePeriod();
		timePeriod.setEffectiveDate(source.getStartDate());
		timePeriod.setExpiryDate(source.getEndDate());
		target.setTimePeriod(timePeriod);
		target.setTransactionType(TransactionType.NO_CHANGE);
		return super.performSchemaMapping(source, target);
	}
	
	
	
}
