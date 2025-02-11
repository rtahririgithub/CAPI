package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.kafka.subscriber_v2.CallingCircleInfo;
import com.telus.cmb.common.kafka.subscriber_v2.Feature;
import com.telus.cmb.common.kafka.subscriber_v2.Service;
import com.telus.cmb.common.kafka.subscriber_v2.ServiceAgreement;
import com.telus.cmb.common.kafka.TransactionType;
import com.telus.cmb.subscriber.kafka.ServiceAgreementUtil;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.ServiceInfo;

public class ServiceMapper {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	protected static void mapPriceplanAndIncludedServices(SubscriberContractInfo contractInfo,String transactionType,ServiceAgreement serviceAgreement) throws TelusException, ApplicationException {
		//map priceplan & included features
		serviceAgreement.getServices().add(mapPriceplan(contractInfo, transactionType));
		//map priceplan included services
		serviceAgreement.getServices().addAll(mapModifiedServices((ServiceAgreementInfo[]) contractInfo.getIncludedServices0(false), transactionType, contractInfo.getEffectiveDate(),false));
	}
	
	private static Service mapPriceplan(SubscriberContractInfo contractInfo,String transactionType) throws TelusException, ApplicationException {
		// retrieve the priceplan reference data if it is null
		ServiceInfo pricePlan = contractInfo.getPricePlan0() != null ? contractInfo.getPricePlan0() : ServiceAgreementUtil.getPricePlan(contractInfo.getCode());
		// map priceplan reference data
		Service pricePlanObj = mapServiceReferencedata(pricePlan);
		// set priceplan transactionType ,effectiveDate ,expiryDate
		pricePlanObj.setTransactionType(transactionType);
		pricePlanObj.setEffectiveDate(contractInfo.getEffectiveDate());
		pricePlanObj.setExpiryDate(contractInfo.getExpiryDate());
		mapServiceFeature(contractInfo.getFeatures0(false),pricePlanObj);
		return pricePlanObj;
	}
	
	protected static List<Service> mapAddtionalServices(ServiceAgreementInfo[] addedContractServices) throws ApplicationException, TelusException {
		return mapModifiedServices(addedContractServices, null, ServiceAgreementUtil.getLogicalDate(),true);
	}
	
	private static List<Service> mapModifiedServices(ServiceAgreementInfo[] modifiedServices, String transactionType,Date defaultEffectiveDate,boolean processRegularSocs) throws ApplicationException, TelusException {
		List<Service> serviceList = new ArrayList<Service>();
		for (ServiceAgreementInfo cs : modifiedServices) {

			// exclude if any include services or include promo's passed in regular service list
			if (processRegularSocs && !cs.getServiceType().equals(ServiceAgreementInfo.SERVICE_TYPE_REGULAR)
					&& !cs.getServiceType().equals(ServiceAgreementInfo.SERVICE_TYPE_REGULAR_AUTO_EXPIRE)) {
				continue;
			}
						

		    cs.setService(ServiceAgreementUtil.getRegularService(cs.getCode()));
				
			Service serviceObj = mapServiceReferencedata(cs.getService0());
			serviceObj.setTransactionType(transactionType != null ? transactionType: translateKBServiceTransactionType(cs.getTransaction()));
			// set defaultDate if service effectiveDate is null, this can also overrides the feature(s) effectiveDate as well
			/** commenting out this logic to fix the optional services effective dae issue 
			 * if(cs.getEffectiveDate()==null){
				cs.setEffectiveDate(defaultEffectiveDate);
			}*/
			Date serviceEffetiveDate = cs.getEffectiveDate() != null ? cs.getEffectiveDate() : defaultEffectiveDate;
			serviceObj.setEffectiveDate(serviceEffetiveDate);
			
			// the service itself is term based, set the expiration date
			if (ServiceInfo.SERVICE_TYPE_CODE_OPTIONAL_AUTOEXP_SOC.equals(cs.getService0().getServiceType()) || ServiceInfo.SERVICE_TYPE_CODE_REG_AUTOEXP_SOC.equals(cs.getService0().getServiceType())) {
				serviceObj.setExpiryDate(ServiceAgreementUtil.calculatePromoServiceExpirationDate(cs.getService0().getCode(), serviceEffetiveDate));				
			} else{
				serviceObj.setExpiryDate(cs.getExpiryDate());
			}
			//attach the promotional service if it has any.
			if(cs.getService0().hasPromotion()){
				serviceList.add(mapPromoService(cs.getService0().getCode(), serviceEffetiveDate));
			}
			mapServiceFeature(cs.getFeatures0(true),serviceObj);
			serviceList.add(serviceObj);
		}
		return serviceList;
	}

	private static Service mapPromoService(String parentServiceCode ,Date effectiveDate) throws TelusException, ApplicationException {
		ServiceInfo promoServiceInfo = ServiceAgreementUtil.getPromoService(parentServiceCode);
		Service promoService = mapServiceReferencedata(promoServiceInfo);
		promoService.setParentServiceCode(parentServiceCode);
		promoService.setEffectiveDate(effectiveDate);
		promoService.setExpiryDate(ServiceAgreementUtil.calculatePromoServiceExpirationDate(promoService.getCode(),effectiveDate));
		// CAPI retrieve the promo service by add-on soc so we shoud not expect any feature level changes here ( same as how KB add it at backend)
		// just map the feature reference data ( i.e RatedFeature) , feature effective date and expire should set same as service here.
		mapRatedFeature(promoServiceInfo.getFeatures0(),promoService.getEffectiveDate(), promoService.getExpiryDate(),promoService);
		return promoService;
	}

	protected static Service mapServiceReferencedata(com.telus.eas.utility.info.ServiceInfo service) {
		Service serviceObj = new Service();
		serviceObj.setCode(service.getCode());
		serviceObj.setServiceType(service.getServiceType());
		serviceObj.setFamilyTypeList(Arrays.asList(service.getFamilyTypes()));
		serviceObj.setCoverageType(service.getCoverageType());
		serviceObj.setDescriptionEnglish(service.getDescription());
		serviceObj.setDescriptionFrench(service.getDescriptionFrench());
		serviceObj.setRecurringChargeAmt(service.getRecurringCharge());
		serviceObj.setAdditionalChargeAmt(service.getAdditionalCharge());
		serviceObj.setRoamLikeHomeInd(service.isRLH());
		serviceObj.setBillCycleTreatmentCode(service.getBillCycleTreatmentCode());
		serviceObj.setBillingZeroChrgSuppressInd(service.isBillingZeroChrgSuppress());
		serviceObj.setHasPromotionInd(service.hasPromotion());
		serviceObj.setIsPromotionInd(service.isPromotion());
		serviceObj.setDurationServiceHours(service.getDurationServiceHours());
		return serviceObj;
	}
	
	
	private static void mapRatedFeature(com.telus.eas.utility.info.RatedFeatureInfo[] featureInfo,Date effectiveDate,Date expiryDate,Service serviceObj) {
		if (featureInfo == null || featureInfo.length <= 0) {
			return;
		}
		for (int i = 0; i < featureInfo.length; i++) {
			Feature feature = new Feature();
			feature.setCode(featureInfo[i].getCode());
			feature.setParameter(featureInfo[i].getParameterDefault());
			feature.setSwitchCode(featureInfo[i].getSwitchCode());
			feature.setCategoryCode(featureInfo[i].getCategoryCode());
			feature.setEffectiveDate(effectiveDate);
			feature.setExpiryDate(expiryDate);
			feature.setCallingCircleChangeInd(false);
			serviceObj.getFeatures().add(feature);
			serviceObj.getFeatureCategoryList().add(featureInfo[i].getCategoryCode());
		}
	}

	private static void mapServiceFeature(com.telus.eas.subscriber.info.ServiceFeatureInfo[] serviceFeatureInfo,Service serviceObj) {
				
		List<Feature> featureList = new ArrayList<Feature>();
		if (serviceFeatureInfo == null || serviceFeatureInfo.length <= 0) {
			return;
		}
		
		for (int i = 0; i < serviceFeatureInfo.length; i++) {
			logger.debug("serviceFeatureInfo : kb featue code , kb transactionType",serviceFeatureInfo[i].getFeatureCode(),serviceFeatureInfo[i].getTransaction());
			Feature feature = new Feature();
			feature.setCode(serviceFeatureInfo[i].getFeatureCode());
			feature.setParameter(serviceFeatureInfo[i].getParameter());
			feature.setSwitchCode(serviceFeatureInfo[i].getSwitchCode());
			feature.setCategoryCode(serviceFeatureInfo[i].getCategoryCode());
			feature.setTransactionType(translateKBServiceTransactionType(serviceFeatureInfo[i].getTransaction()));
			feature.setEffectiveDate(serviceFeatureInfo[i].getEffectiveDate() != null ? serviceFeatureInfo[i].getEffectiveDate() : serviceObj.getEffectiveDate());
			feature.setExpiryDate(serviceFeatureInfo[i].getExpiryDate());
			feature.setCallingCircleChangeInd(serviceFeatureInfo[i].isCcParameterChanged());
			feature.setCallingCircleInfo(populateCallingCircleInfo(serviceFeatureInfo[i]));
			featureList.add(feature);
			
			if(StringUtils.isNotBlank(serviceFeatureInfo[i].getCategoryCode())){				
				if(!serviceObj.getFeatureCategoryList().contains(serviceFeatureInfo[i].getCategoryCode())){
					serviceObj.getFeatureCategoryList().add(serviceFeatureInfo[i].getCategoryCode());
				}
			}
		}
		serviceObj.setFeatures(featureList);
	}
	
	
	private static CallingCircleInfo populateCallingCircleInfo(ServiceFeatureInfo featureInfo) {
		CallingCircleInfo info = null;
		String[] ccList = featureInfo.getCallingCirclePhoneNumbersFromParam();
		if (featureInfo.isCcParameterChanged() && ccList.length > 0) {
			info = new CallingCircleInfo(); 
			if (featureInfo.getCallingCircleCommitmentAttributeData0() != null && featureInfo.getCallingCircleCommitmentAttributeData0().getEffectiveDate() != null) {
				info.setEffectiveDate(featureInfo.getCallingCircleCommitmentAttributeData0().getEffectiveDate());
			}
			info.getPhoneNumberList().addAll(Arrays.asList(ccList));
		}
		return info;
	}
	
	private static String translateKBServiceTransactionType(byte transactionType) {
		switch (transactionType) {
		case BaseAgreementInfo.ADD:
			return TransactionType.ADD.getValue();
		case BaseAgreementInfo.DELETE:
			return TransactionType.REMOVE.getValue();
		case BaseAgreementInfo.UPDATE:
			return TransactionType.MODIFY.getValue();
		default:
			return "NO_CHANGE";
		}
	}
	
}
