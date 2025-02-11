package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.cmb.common.kafka.subscriber_v2.ServiceAgreement;
import com.telus.cmb.common.kafka.TransactionType;

public class ServiceAgreementMapper {	
	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");
	
	public static ServiceAgreement mapServiceAgreementData(SubscriberContractInfo newContractInfo,SubscriberContractInfo oldContractInfo, boolean isActivation)  {
		ServiceAgreement serviceAgreement = new ServiceAgreement();
		try {
			
			if(newContractInfo == null){
				logger.info("newContractInfo is null , service agreement change only involved equipment change activity");
				return null;
			}
			// map new price plan for activation
			if (isActivation) {
				ServiceMapper.mapPriceplanAndIncludedServices(newContractInfo,TransactionType.ADD.getValue(), serviceAgreement);
			}
		
		  // map priceplan changes for existing subscriber
			if (newContractInfo.isPricePlanChange()) {
				ServiceMapper.mapPriceplanAndIncludedServices(newContractInfo,TransactionType.ADD.getValue(), serviceAgreement);
				ServiceMapper.mapPriceplanAndIncludedServices(oldContractInfo,TransactionType.REMOVE.getValue(), serviceAgreement);
			}
		
			// map optional soc changes
			serviceAgreement.getServices().addAll(ServiceMapper.mapAddtionalServices((ServiceAgreementInfo[]) newContractInfo.getOptionalServices0(true)));

			// map contract renewal indicator
			serviceAgreement.setContractRenewalInd(newContractInfo.isContractRenewal());
			// map commitmentInfo
			serviceAgreement.setCommitment(CommitmentMapper.mapCommitment(newContractInfo.getCommitment()));
		} catch (Throwable t) {
			logger.error("kafka mapServiceAgreementData error: {}", t.getMessage(), t);
		}
		return serviceAgreement;
	}
}
