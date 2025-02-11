package com.telus.cmb.jws.mapping.subscriber.management.common_types_10;

import java.util.Arrays;
import java.util.List;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.subscribermanagementcommontypes_v1.ServiceAgreement;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.Commitment;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.ContractService;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.MultiRingPhoneNumberType;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v1.PricePlan;

public class ServiceAgreementMapper extends AbstractSchemaMapper<ServiceAgreement, SubscriberContractInfo> {

	public ServiceAgreementMapper() {
		super (ServiceAgreement.class, SubscriberContractInfo.class);
	}

	@Override
	protected SubscriberContractInfo performDomainMapping(ServiceAgreement source, SubscriberContractInfo target) {
		Commitment commitment = source.getCommitment();
		if (commitment != null) {
			if (commitment.getTimePeriod() != null) {
				target.setCommitmentEndDate(commitment.getTimePeriod().getExpiryDate());
				target.setCommitmentStartDate(commitment.getTimePeriod().getEffectiveDate());
			}
			if (commitment.getContractTerm() != null) {
				target.setCommitmentMonths(Integer.valueOf(commitment.getContractTerm()));
			}
			target.setCommitmentReasonCode(commitment.getReasonCode());
		}
		MultiRingPhoneNumberType multiRing = source.getMultiRingPhoneNumberList();
		PricePlan pricePlan = source.getPricePlan();
		List<ContractService> serviceList = source.getServiceList();
		for (ContractService s : serviceList) {
			
		}
		return super.performDomainMapping(source, target);
	}

	@Override
	protected ServiceAgreement performSchemaMapping(SubscriberContractInfo source, ServiceAgreement target) {
		target.setCommitment(CommitmentMapper.getInstance().mapToSchema(source.getCommitment()));
		MultiRingPhoneNumberType multiRingPhoneNumber = new MultiRingPhoneNumberType();
		multiRingPhoneNumber.getMultiRingPhoneNumber().addAll(Arrays.asList(source.getMultiRingPhoneNumbers()));
		target.setMultiRingPhoneNumberList(multiRingPhoneNumber);
		target.setPricePlan(null);
		return super.performSchemaMapping(source, target);
	}
	
	
}
