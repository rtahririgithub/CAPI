package com.telus.cmb.subscriber.kafka;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.telus.api.ApplicationException;
import com.telus.api.reference.ServiceRelation;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.TransactionType;
import com.telus.cmb.common.kafka.subscriber_v2.Service;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberEvent;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.reference.dto.ServiceTermDto;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;

public class ServiceAgreementUtil {

	private static ReferenceDataFacade refFacade = null;

	private static ReferenceDataFacade getReferenceDataFacade() {
		if (refFacade == null) {
			refFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class,EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		return refFacade;
	}
	
	public static Date calculatePromoServiceExpirationDate(String code,Date effectiveDate) throws TelusException {
		ServiceTermDto termInfo = getReferenceDataFacade().getServiceTerm(code);
		return termInfo.calculateExpirationDate(effectiveDate);
	}
	
	public static PricePlanInfo getPricePlan(String pricePlanCode) throws TelusException, ApplicationException {
		return getReferenceDataFacade().getPricePlan(pricePlanCode);
	}
	
	public static Date getLogicalDate() {
		try {
			return getReferenceDataFacade().getLogicalDate();
		} catch (TelusException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static ServiceInfo getRegularService(String code) throws TelusException, ApplicationException {
		ServiceInfo service = getReferenceDataFacade().getRegularService(code);
		if (service == null) {
			service = getReferenceDataFacade().getWPSService(code);
		}
		return service;
	}
	
	public static ServiceInfo getPromoService(String code) throws TelusException, ApplicationException {
		ServiceRelationInfo[] relations = getReferenceDataFacade().getServiceRelations(code);
		ServiceInfo promoService = null;
		if (relations != null) {
			for (ServiceRelationInfo sr : relations) {
				if (ServiceRelation.TYPE_PROMOTION.equals(sr.getType())) {
					promoService = getReferenceDataFacade().getRegularService(sr.getServiceCode());
				}
			}
		}
		return promoService;
	}

	
	public static List<String> popualteServiceAgreementChangeEventTypes(com.telus.cmb.common.kafka.TransactionEventInfo source, SubscriberEvent target) {
		Set<String> serviceEventTypes = new HashSet<String>();
		List<String> eventTypes = new ArrayList<String>();

		
		// Set price plan chaneg event type
		if( source.getNewContractInfo()!=null && source.getNewContractInfo().isPricePlanChange()){
			eventTypes.add(String.valueOf(KafkaEventType.PRICEPLAN_CHANGE));
		}
		
		// Set service add or remove event types
		List<Service> targetServices = target.getServiceAgreement() != null ? target.getServiceAgreement().getServices() : new ArrayList<Service>();
		for (Service service : targetServices) {
			if (!service.getServiceType().equalsIgnoreCase(ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN)) {
				if (service.getTransactionType().equals(TransactionType.ADD.getValue())) {
					serviceEventTypes.add(String.valueOf(KafkaEventType.SERVICE_ADD));
				} else if (service.getTransactionType().equals(TransactionType.REMOVE.getValue())) {
					serviceEventTypes.add(String.valueOf(KafkaEventType.SERVICE_REMOVE));
				} else if (service.getTransactionType().equals(TransactionType.MODIFY.getValue())) {
					serviceEventTypes.add(String.valueOf(KafkaEventType.SERVICE_MODIFY));
				}
			}
		}
		
		eventTypes.addAll(serviceEventTypes);
		
		// Set commitment change event type
		if (source.getNewContractInfo()!=null && source.getNewContractInfo().getCommitment() != null && source.getNewContractInfo().getCommitment().isModified()) {
			eventTypes.add(String.valueOf(KafkaEventType.COMMITMENT_CHANGE));
		}

		// Set equipment change event type
		if (source.getEquipmentInfo() != null) {
			eventTypes.add(String.valueOf(KafkaEventType.EQUIPMENT_CHANGE));
		}
		return eventTypes;
	}
	
}
