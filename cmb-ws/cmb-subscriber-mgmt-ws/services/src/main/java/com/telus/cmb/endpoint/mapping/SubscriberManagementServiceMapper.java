package com.telus.cmb.endpoint.mapping;

import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.AuditInfoMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_30.SubscriberMapper_30;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.tmi.xmlschema.xsd.service.service.subscriberserviceagreementtypes_v3.TransactionType;

public class SubscriberManagementServiceMapper {

	private static AccountMapper accountMapper = new AccountMapper();
	private static SubscriberMapper_30 subscriberMapper = SubscriberMapper_30.getInstance();
	private static ServiceWarningMapper serviceWarningMapper = new ServiceWarningMapper();
	private static ApplicationMessageMapper applicationMessageMapper = new ApplicationMessageMapper();
	private static ServiceAgreementMapper serviceAgreementMapper = new ServiceAgreementMapper();
	private static PortInEligibilityMapper portInEligibilityMapper = new PortInEligibilityMapper();
	private static ServicesValidationMapper servicesValidationMapper = new ServicesValidationMapper();
	private static ActivationOptionMapper activationOptionMapper = new ActivationOptionMapper();
	private static PortRequestMapper portRequestMapper = new PortRequestMapper();
	private static SeatDataMapper seatDataMapper = new SeatDataMapper();
	private static AuditInfoMapper auditInfoMapper = AuditInfoMapper.getInstance();
	
	public static AccountMapper getAccountMapper() {
		return accountMapper;
	}

	public static SubscriberMapper_30 getSubscriberMapper() {
		return subscriberMapper;
	}

	public static ServiceWarningMapper getServiceWarningMapper() {
		return serviceWarningMapper;
	}

	public static ApplicationMessageMapper getApplicationMessageMapper() {
		return applicationMessageMapper;
	}

	public static ServiceAgreementMapper getServiceAgreementMapper() {
		return serviceAgreementMapper;
	}

	public static PortInEligibilityMapper getPortInEligibilityMapper() {
		return portInEligibilityMapper;
	}

	public static ServicesValidationMapper getServicesValidationMapper() {
		return servicesValidationMapper;
	}
	
	public static ActivationOptionMapper getActivationOptionMapper() {
		return activationOptionMapper;
	}
	
	public static PortRequestMapper getPortRequestMapper() {
		return portRequestMapper;
	}
	
	public static SeatDataMapper getSeatDataMapper() {
		return seatDataMapper;
	}
	
	public static AuditInfoMapper getAuditInfoMapper() {
		return auditInfoMapper;
	}
	
	public static TransactionType translateTransactionType(byte transactionType) {
		if (transactionType == BaseAgreementInfo.ADD) {
			return TransactionType.ADD;
		} else if (transactionType == BaseAgreementInfo.DELETE) {
			return TransactionType.REMOVE;
		} else if (transactionType == BaseAgreementInfo.NO_CHG) {
			return TransactionType.NO_CHANGE;
		} else if (transactionType == BaseAgreementInfo.UPDATE) {
			return TransactionType.MODIFY;
		}

		return null;
	}

	public static byte translateTransactionType(TransactionType transactionType) {
		if (TransactionType.ADD.equals(transactionType)) {
			return BaseAgreementInfo.ADD;
		} else if (TransactionType.MODIFY.equals(transactionType)) {
			return BaseAgreementInfo.UPDATE;
		} else if (TransactionType.NO_CHANGE.equals(transactionType)) {
			return BaseAgreementInfo.NO_CHG;
		} else if (TransactionType.REMOVE.equals(transactionType)) {
			return BaseAgreementInfo.DELETE;
		}

		return BaseAgreementInfo.NO_CHG;
	}
	
}