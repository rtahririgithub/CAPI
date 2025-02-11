package com.telus.cmb.subscriber.kafka.json.mapper.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.api.reference.Service;
import com.telus.cmb.common.kafka.subscriber_v1.Commitment;
import com.telus.cmb.common.kafka.subscriber_v1.ServiceAgreement;
import com.telus.cmb.common.kafka.subscriber_v1.ServiceData;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.common.kafka.TransactionType;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class ServiceAgreementMapper {
	private static ReferenceDataFacade refFacade = null;	

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static ReferenceDataFacade getReferenceDataFacade() {
		if (refFacade == null) {
			refFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class,EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		return refFacade;
	}

	public static ServiceAgreement mapServiceAgreementData(SubscriberContractInfo newContractInfo) {
		ServiceAgreement serviceAgreement = new ServiceAgreement();
		try {
		ServiceAgreementInfo[] addedAddtionalServices = (ServiceAgreementInfo[]) newContractInfo.getAddedServices();
		// map the commitmentInfo
		if(newContractInfo.getCommitment()!=null){
			serviceAgreement.setCommitment(mapCommitmentData(newContractInfo.getCommitment()));
		}
		// map the price plan
		serviceAgreement.setPriceplan(mapPriceplanData(newContractInfo,TransactionType.ADD.getValue()));
		// map the additional services
		for (ServiceAgreementInfo serviceAgreementInfo : addedAddtionalServices) {
			ServiceData serviceData = mapAddtionalServiceData(serviceAgreementInfo,TransactionType.ADD.getValue());
				if (serviceData != null) {
					serviceAgreement.getAddtionalServices().add(mapAddtionalServiceData(serviceAgreementInfo,TransactionType.ADD.getValue()));
				}
			}
		} catch (Throwable t) {
			logger.error("kafka mapServiceAgreementData error: {}", t.getMessage(), t);
		}
		return serviceAgreement;

	}

	private static ServiceData mapPriceplanData(SubscriberContractInfo contractInfo,String transactionType) {
		ServiceData serviceData = mapBaseServiceData(contractInfo.getPricePlan0());
		serviceData.setTransactionType(transactionType);
		serviceData.setServiceEffectiveDate(contractInfo.getEffectiveDate());
		serviceData.setServiceExpiryDate(contractInfo.getExpiryDate());
		serviceData.setServiceVersionNo(contractInfo.getServiceVersionNo());
		serviceData.setServiceSequenceNo(contractInfo.getServiceSequenceNo());
		return serviceData;
	}

	
	private static ServiceData mapAddtionalServiceData(ServiceAgreementInfo source, String transactionType) throws TelusException {
		if (source.getService0() == null) {
			ServiceInfo serviceRefData = getReferenceDataFacade().getRegularService(source.getCode());
			if (serviceRefData == null) {
				serviceRefData = getReferenceDataFacade().getWPSService(source.getCode());
			}
			source.setService(serviceRefData);
		}
		ServiceData serviceData = mapBaseServiceData(source.getService0());
		serviceData.setTransactionType(transactionType);
		serviceData.setServiceEffectiveDate(source.getEffectiveDate());
		serviceData.setServiceExpiryDate(source.getExpiryDate());
		serviceData.setServiceVersionNo(source.getServiceVersionNo());
		serviceData.setServiceSequenceNo(source.getServiceSequenceNo());
		return serviceData;
	}

	private static ServiceData mapBaseServiceData(Service service) {
		ServiceData serviceData = new ServiceData();
		serviceData.setCode(service.getCode());
		serviceData.setDescriptionEnglish(service.getDescription());
		serviceData.setDescriptionFrench(service.getDescriptionFrench());
		serviceData.setServiceType(service.getServiceType());
		serviceData.setBillingZeroChrgSuppress(service.isBillingZeroChrgSuppress());
		serviceData.setRecurringCharge(service.getRecurringCharge());
		serviceData.setRoamLikeHome(service.isRLH());
		return serviceData;
	}

	private static Commitment mapCommitmentData(CommitmentInfo commitmentInfo) {
		Commitment commitment = new Commitment();
		commitment.setMonths(commitmentInfo.getMonths());
		commitment.setStartDate(commitmentInfo.getStartDate());
		commitment.setEndDate(commitmentInfo.getEndDate());
		commitment.setReasonCode(commitmentInfo.getReasonCode());
		return commitment;
	}
}
