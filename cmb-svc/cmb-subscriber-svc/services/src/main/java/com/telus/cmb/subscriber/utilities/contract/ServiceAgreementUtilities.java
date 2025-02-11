package com.telus.cmb.subscriber.utilities.contract;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.ServiceInfo;


public class ServiceAgreementUtilities {
	private static final Log logger = LogFactory.getLog(ServiceAgreementUtilities.class);
	/**
	 * Implementation Logic Reference TMContract:addService0(Service service, Date effectiveDate, Date expiryDate, Boolean allowBoundAndPromotional, Boolean preserveDigitalServices)
	 * Call to private overloaded method addService(Service service, Date effectiveDate, Date expiryDate, Boolean allowBoundAndPromotional,SubscriberContractInfo subscriberContractInfo) 
	 * @param service
	 * @param preserveDigitalServices
	 * @param effectiveDate
	 * @param expiryDate
	 * @param allowBoundAndPromotional
	 * @param subscriberContractInfo
	 * @return
	 */
	public static SubscriberContractInfo addService (ServiceInfo service, boolean preserveDigitalServices, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, SubscriberContractInfo subscriberContractInfo) {
//		SubscriberContractInfo info = addService(service, effectiveDate, expiryDate, allowBoundAndPromotional, subscriberContractInfo);
//		
//		 if(effectiveDate == null){
//		     effectiveDate = info.getEffectiveDate(); 
//		 }
//
//		 ServiceFeatureInfo[] features = info.getFeatures0(true);
//		 for (int j = 0; j < features.length; j++) {
//			 ServiceFeatureInfo f = features[j];
//			 f.setFeature(info.getService0().getFeature0(f.getFeatureCode()));
//		 }
//
//		 addBoundServices(service, effectiveDate, expiryDate);
//
//		 if (!preserveDigitalServices) {
//			 String applicationCode = provider.getApplication();
//			 ApplicationSummary applicationSummary = provider.getReferenceDataManager().getApplicationSummary(applicationCode);
//			 if ((applicationSummary != null && !applicationSummary.isBatch()) || applicationSummary == null) {
//				 removeDispatchOnlyConflicts();
//				 // removed - it should only be done for Activation, Price Plan change and Renewal - R.A. 2007-07-03
//				 //removeNonMatchingServices(getValidationEquipment(), getAssociatedMule(), subscriber.getProvince());
//			 }
//			 //remove911FeaturesInEdmonton();
//			 removeOutboundCallerIdDisplay(service);
//		 }
//
//		 removeCombinedVoiceMailConflicts(contractService);
//
//		 checkInvoiceFormatChange(service, true);
//		
//		 // TODO: check if contractService still exists after removing the above serices.
//		 return contractService;
		return null;
	}
	
	/**
	 * Implementation Logic Reference TMContract:addService0(Service service, Date effectiveDate, Date expiryDate, Boolean allowBoundAndPromotional)
	 * @param service
	 * @param effectiveDate
	 * @param expiryDate
	 * @param allowBoundAndPromotional
	 * @param subscriberContractInfo
	 * @return
	 */
	public static SubscriberContractInfo addService (ServiceInfo service, Date effectiveDate, Date expiryDate, boolean allowBoundAndPromotional, ContractChangeInfo contractChangeInfo) {
//		ServiceAgreementInfo info =  addService00(service, effectiveDate, expiryDate, allowBoundAndPromotional);
//		 for(int i = 0; i < listConflictingFeatures.size(); i++){
//			 ContractFeature contractFeature =  (ContractFeature)listConflictingFeatures.get(i);
//			 info.removeFeature(contractFeature.getCode());
//			 addFeature(contractFeature.getFeature());                    
//			 logger.debug(info);
//		 }
//		 listConflictingFeatures.clear();
//		 return info;
		return null;
	}
	
	public static List<ServiceAgreementInfo> retrieveListOfServicesToRetain (List<ServiceAgreementInfo> serviceList, EquipmentInfo equipment) {
		return null;
	}
	
	public static boolean isContractTelephonyEnabled (SubscriberContractInfo subscriberContractInfo) {
		return false;
	}
	
	public static List<ServiceAgreementInfo> retrieveTelephonyDisabledConflicts (ServiceAgreementInfo[] serviceList) {
		return null;
	}
	
	public static List<ServiceFeatureInfo> retrieveTelephonyDisabledConflicts (ServiceFeatureInfo[] serviceList) {
		return null;
	}
	
	public static SubscriberContractInfo updateShareablePricePlanStatus (SubscriberContractInfo subscriberContractInfo, SubscriberInfo subscriberInfo, AccountInfo accountInfo) {
		return null;
	}
	
	private static List<ServiceAgreementInfo> retainServicesByNetworkAndEquipmentType (List<ServiceAgreementInfo> serviceList, EquipmentInfo equipment) {
		return null;
	}
	
	private static List<ServiceAgreementInfo> retainServicesForPreHSPA (List<ServiceAgreementInfo> serviceList, EquipmentInfo equipment) {
		return null;
	}
}
