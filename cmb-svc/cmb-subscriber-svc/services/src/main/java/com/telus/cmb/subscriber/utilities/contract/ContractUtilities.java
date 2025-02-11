package com.telus.cmb.subscriber.utilities.contract;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.ContractService;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Feature;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.Reference;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.MultiRingInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.BrandInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;

public class ContractUtilities {
	private static final Log logger = LogFactory.getLog(ContractUtilities.class);
	// The following static variable is used for the addCombinedVoiceMailConflicts() and removeCombinedVoiceMailConflicts() methods.
	public static final String COMBINED_VOICE_MAIL_PREFIX = "CVM";
	public static final long DAY = 1000L * 60L * 60L * 24L;
	public static final String ROAMING_PASSES_BOUND_SOC_FAMILY_TYPE = "1";
	// The following static variables are used for the addOutboundCallerIdDisplay() and removeOutboundCallerIdDisplay() methods.
	public static final String OUTBOUND_CALLER_ID_PCS_FEATURE = "CNRD";
	public static final String OUTBOUND_CALLER_ID_KOODO_FEATURE = "PTOCDT";

	public static final String CALL_DETAIL_FEATURE = "CD";

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
	private static FieldPosition POSITION = new FieldPosition(0);

	public static final String WS_TRANSACTION_TYPE_ADD = "ADD";
	public static final String WS_TRANSACTION_TYPE_MODIFY = "MODIFY";
	public static final String WS_TRANSACTION_TYPE_REMOVE = "REMOVE";
	public static final String WS_TRANSACTION_TYPE_NOCHG = "NO_CHANGE";


	public static String getAddOnAPNSoc(String featureCode) {
		return AppConfiguration.getAddOnAPNSocs().get(featureCode);
	}


	/**
	 * This method returns the proper equipment type to pass to ReferenceDataManager.
	 * It checks if the equipment passed in is HSPA (usually in the case of USIM equipment type), check
	 * 
	 * @param equipment
	 * @return
	 * @throws ApplicationException 
	 */
	public static String translateEquipmentType(EquipmentInfo equipment, ProductEquipmentHelper productEquipmentHelper) throws ApplicationException {
		if (equipment == null) return "";

		String equipmentType = String.valueOf(equipment.getEquipmentType());
		if (equipment.isHSPA() && Equipment.EQUIPMENT_TYPE_USIM.equals(equipmentType.trim())) {
			EquipmentInfo associatedHandset = null;
			associatedHandset = getLastAssociatedHandsetForUSIMEquipment(equipment, productEquipmentHelper);

			equipmentType = (associatedHandset != null ? associatedHandset.getEquipmentType() : AppConfiguration.getDefaultHSPAEquipmentType());
		}

		return equipmentType;
	}

	public static EquipmentInfo getLastAssociatedHandsetForUSIMEquipment (EquipmentInfo equipmentInfo, ProductEquipmentHelper productEquipmentHelper) throws ApplicationException {
		EquipmentInfo associatedHandset = equipmentInfo.getAssociatedHandset();
		if (associatedHandset == null) {
			try {
				if (equipmentInfo.getAssociatedHandsetIMEI()!= null ) {
					associatedHandset = productEquipmentHelper.getEquipmentInfobySerialNo(equipmentInfo.getAssociatedHandsetIMEI() );
				} else {
					associatedHandset = productEquipmentHelper.getAssociatedHandsetByUSIMID(equipmentInfo.getSerialNumber());
				}
				equipmentInfo.setAssociatedHandset(associatedHandset);

			} catch (ApplicationException ae) {
				if (ErrorCodes.UNKNOWN_SERIAL_NUMBER.equals(ae.getErrorCode())) {
					//defect PROD00145333 fix. When we can not find the serial number, this is the case of gray market handset,
					//we should return null so front-end can handle it properly.
					return null;
				}else {
					throw ae;
				}

			}
		}

		return associatedHandset;
	}

	public static boolean isCombinedVoiceMail(ServiceInfo service) throws TelusAPIException {
		// Check if the service contains Combined Voice Mail (switch code starts with "CVM").
		RatedFeatureInfo serviceFeatures[] = service.getFeatures0();
		for (int i = 0; i < serviceFeatures.length; i++) {
			if (serviceFeatures[i].getSwitchCode().startsWith(COMBINED_VOICE_MAIL_PREFIX)) {
				return true;
			}
		}
		return false;
	}

	public static boolean testBoundServiceAddition(Service addOnService, Service boundService, Date today, EquipmentInfo equipment) throws TelusAPIException {

		Date addOnSOCExpDate = addOnService.getExpiryDate();
		Date boundSOCExpDate = boundService.getExpiryDate();

		if ((addOnSOCExpDate == null || addOnSOCExpDate.compareTo(today) > 0) && (boundSOCExpDate == null || boundSOCExpDate.compareTo(today) > 0)) {
			if ((addOnService.isCurrent() && boundService.isCurrent()) || !addOnService.isCurrent()) {
				if (boundService.isNetworkEquipmentTypeCompatible(equipment)) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * Returns the Visto SOC if this contract has any Visto service. Otherwise, returns null
	 * 
	 * @return
	 */
	public static ServiceInfo hasVisto(boolean includeDeleted, SubscriberContractInfo subscriberContractInfo) throws TelusAPIException {
		ServiceAgreementInfo[] services = subscriberContractInfo.getServices0(includeDeleted);
		if (services != null) {
			for (int i = 0; i < services.length; i++) {
				if (services[i].getService().isVisto()) {
					return services[i].getService0();
				}
			}
		}
		return null;
	}

	public static boolean isPeriodOverlapping(Date firstEffectiveDate, Date firstExpiryDate, Date secondEffectiveDate, Date secondExpiryDate) {
		 if (secondEffectiveDate!=null && firstEffectiveDate!=null && firstExpiryDate!=null) {
			 if (secondExpiryDate==null) {
				 return firstExpiryDate.compareTo(secondEffectiveDate)>0;
			 } else {
				 return (!((firstExpiryDate.compareTo(secondEffectiveDate)<=0)||(firstEffectiveDate.compareTo(secondExpiryDate)>=0)));
			 }
		 }
		 if (firstEffectiveDate!=null) {  //expiryDate==null
			 if (secondExpiryDate==null) {
				 return true;
			 }
			 return firstEffectiveDate.compareTo(secondExpiryDate)<0;
		 }
		 return true;
	 }
	
	public static int getBrandId(SubscriberInfo subscriberInfo, AccountInfo accountInfo, BrandInfo[] brands, PricePlanInfo pricePlanInfo) {
		try {
			if (!(ReferenceDataManager.Helper.validateBrandId(subscriberInfo.getBrandId(), brands))) {
				return (pricePlanInfo != null) ? pricePlanInfo.getBrandId() : accountInfo.getBrandId();
			}
		} catch (Throwable t) {
			logFailure("getBrandId", "retrieving price plan or account brand on new subscriber", subscriberInfo, t, "error retrieving price plan or account brand ID");
		}
		return subscriberInfo.getBrandId();
	}

	public static boolean containsFeature(SubscriberContractInfo contract, String featureCategoryCode) {

		ServiceFeatureInfo[] features = contract.getFeatures0(false,true);
		for (int i = 0; i < features.length; i++) {
			if (features[i].getFeature() != null && featureCategoryCode.equalsIgnoreCase(features[i].getFeature().getCategoryCode())) {
				return true;
			}
		}

		return false;
	}

	public static ServiceFeatureInfo findContractFeature(String featureCode, ServiceFeatureInfo[] features) {
		for (int i = 0; i < features.length; i++) {
			if (features[i].getFeature().getCode().equals(featureCode)) {
				return features[i];
			}
		}
		return null;
	}

	public static ServiceRelationInfo[] filterRelationsByContract(ServiceRelationInfo[] relations, PricePlan pricePlan) {
		List<ServiceRelationInfo> list = new ArrayList<ServiceRelationInfo>(relations.length);
		for (int i = 0; i < relations.length; i++) {
			ServiceRelationInfo r = relations[i];

			if (!r.isOptional() || pricePlan.containsOptionalService(r.getServiceCode())) {
				list.add(r);
			}
		}

		return (ServiceRelationInfo[]) list.toArray(new ServiceRelationInfo[list.size()]);
	}

	public static ServiceAgreementInfo testRemoval0(ServiceAgreementInfo contractService, boolean allowBoundAndPromotional) throws InvalidServiceChangeException, TelusAPIException {
		if (!allowBoundAndPromotional) {
			ServiceInfo service = contractService.getService0();

			if (service.isBoundService() && !service.isVisto()) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be removed by system: " + service.getCode());
			}

			if (service.isSequentiallyBoundService() && !service.isVisto()) {
				throw new InvalidServiceChangeException(InvalidServiceChangeException.BOUND_SERVICE, "service can only be removed by system: " + service.getCode());
			}
		}

		return contractService;
	}

	public static boolean isServiceInContractService(ServiceInfo serviceInfo, ServiceAgreementInfo[] cs) {
		if (serviceInfo == null || cs == null) {
			return false;
		}

		for (int i = 0; i < cs.length; i++) {
			if (serviceInfo.getCode().equals(cs[i].getCode())) {
				return true;
			}
		}
		return false;
	}

	public static ServiceInfo[] retainTelephonyDisabledConflicts(ServiceInfo[] service) {
		// Service[] service1 = removeBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
		// Service[] service2 = removeBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
		ServiceInfo[] service1 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
		ServiceInfo[] service2 = retainBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
		ServiceInfo[] service3 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_WAITING);
		ServiceInfo[] service4 = retainBySwitchCode(service, Feature.SWITCH_CODE_FAX_MAIL);

		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service1.length + service2.length + service3.length + service4.length);
		addToList(list, service1);
		addToList(list, service2);
		addToList(list, service3);
		addToList(list, service4);

		service = (ServiceInfo[]) java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return list.toArray(service);
	}

	public static <T extends FeatureInfo> T[] retainTelephonyDisabledConflicts(T[] feature) {
		T[] feature1 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
		T[] feature2 = retainBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
		T[] feature3 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_WAITING);
		T[] feature4 = retainBySwitchCode(feature, Feature.SWITCH_CODE_FAX_MAIL);

		List<T> list = new ArrayList<T>(feature1.length + feature2.length + feature3.length + feature4.length);
		addToList(list, feature1);
		addToList(list, feature2);
		addToList(list, feature3);
		addToList(list, feature4);

		feature = (T[]) java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
		return list.toArray(feature);
	}

	public static ServiceAgreementInfo[] retainTelephonyDisabledConflicts(ServiceAgreementInfo[] service) {
		ServiceAgreementInfo[] service1 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
		ServiceAgreementInfo[] service2 = retainBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
		ServiceAgreementInfo[] service3 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_WAITING);
		ServiceAgreementInfo[] service4 = retainBySwitchCode(service, Feature.SWITCH_CODE_FAX_MAIL);


		List<ServiceAgreementInfo> list = new ArrayList<ServiceAgreementInfo>(service1.length + service2.length + service3.length + service4.length);
		addToList(list, service1);
		addToList(list, service2);
		addToList(list, service3);
		addToList(list, service4);

		service = (ServiceAgreementInfo[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return list.toArray(service);
	}

	public static ServiceFeatureInfo[] retainTelephonyDisabledConflicts(ServiceFeatureInfo[] feature) {
		ServiceFeatureInfo[] feature1 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
		ServiceFeatureInfo[] feature2 = retainBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
		ServiceFeatureInfo[] feature3 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_WAITING);
		ServiceFeatureInfo[] feature4 = retainBySwitchCode(feature, Feature.SWITCH_CODE_FAX_MAIL);


		List<ServiceFeatureInfo> list = new ArrayList<ServiceFeatureInfo>(feature1.length + feature2.length + feature3.length + feature4.length);
		addToList(list, feature1);
		addToList(list, feature2);
		addToList(list, feature3);
		addToList(list, feature4);

		feature = (ServiceFeatureInfo[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
		return list.toArray(feature);
	}

	public static ServiceInfo[] retainBySwitchCode(ServiceInfo[] service, String switchCode) {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service.length);

		for (int i = 0; i < service.length; i++) {
			ServiceInfo o = service[i];
			if (o.containsSwitchCode(switchCode)) {
				list.add(o);
			}
		}
		service = (ServiceInfo[]) java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return list.toArray(service);
	}

	public static <T extends FeatureInfo> T[] retainBySwitchCode(T[] feature, String switchCode) {
		List<T> list = new ArrayList<T>(feature.length);

		for (int i = 0; i < feature.length; i++) {
			T o = feature[i];
			if (o.getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
				list.add(o);
			}
		}
		feature = (T[]) java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
		return list.toArray(feature);
	}

	public static ServiceAgreementInfo[] retainBySwitchCode(ServiceAgreementInfo[] service, String switchCode) {
		List<ServiceAgreementInfo> list = new ArrayList<ServiceAgreementInfo>(service.length);

		for (int i = 0; i < service.length; i++) {
			ServiceAgreementInfo o = service[i];
			if (o.getService().containsSwitchCode(switchCode)) {
				list.add(o);
			}
		}
		service = (ServiceAgreementInfo[]) java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return list.toArray(service);
	}

	public static ServiceFeatureInfo[] retainBySwitchCode(ServiceFeatureInfo[] feature, String switchCode) {
		List<ServiceFeatureInfo> list = new ArrayList<ServiceFeatureInfo>(feature.length);

		for(int i=0; i<feature.length; i++) {
			ServiceFeatureInfo o = feature[i];
			if(o.getFeature().getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
				list.add(o);
			}
		}
		feature = (ServiceFeatureInfo[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
		return list.toArray(feature);
	}

	public static ServiceAgreementInfo[] retainServices(ServiceAgreementInfo[] contractServices, EquipmentBo equipment) throws ApplicationException {

		List<ServiceInfo> list = new ArrayList<ServiceInfo>(contractServices.length);

		for(int i=0; i<contractServices.length; i++) {
			list.add(contractServices[i].getService0());
		}

		ServiceInfo[] services = list.toArray(new ServiceInfo[list.size()]);
		services = retainServices(services, equipment);

		if (contractServices.length == services.length) {
			return contractServices;
		}

		List<ServiceAgreementInfo> contractServiceList = new ArrayList<ServiceAgreementInfo>(services.length);

		for (ServiceAgreementInfo contractService : contractServices) {
			for (ServiceInfo service : services) {
				if (contractService.getService().getCode().equals(service.getCode())) {
					contractServiceList.add(contractService);
					break;
				}
			}
		}

		contractServices = (ServiceAgreementInfo[])java.lang.reflect.Array.newInstance(contractServices.getClass().getComponentType(), contractServiceList.size());
		return contractServiceList.toArray(contractServices);
	}

	public static ServiceInfo[] retainServices(ServiceInfo[] services, EquipmentBo equipment) throws ApplicationException {
		/** This block should contain common logic applicable for ALL only **/
		services = retainServicesByNetworkAndEquipmentType (services, equipment);
		/** This block should contain common logic applicable for ALL only **/

		if (!equipment.isHSPA()) {
			services = retainServicesForPreHSPA(services, equipment);
		}else {
			services = retainServicesForHSPA(services, equipment);
		}

		return services;
	}

	/**
	 * Retain service by network type and equipmentType combination, assuming the product type matches.
	 * @param services
	 * @param networkType   - the networkType of the equipment
	 * @param equipmentType - this should be the equipmentType of handset, not the SIM card. If the equipmentType is USIMCard
	 *                        then this method will use PDA as equipmentType for checking.
	 * @return Service[]
	 */
	public static ServiceInfo[] retainServicesByNetworkAndEquipmentType (ServiceInfo[] services, String networkType, String equipmentType) {
		if (networkType == null || "".equals(networkType)) {
			return services;
		}

		List<ServiceInfo> list = new ArrayList<ServiceInfo>(services.length);

		for(int i = 0; i < services.length; i++) {
			ServiceInfo o = services[i];

			if ( o.isCompatible(networkType, equipmentType )) {
				list.add(o);
			} 
		}
		services = (ServiceInfo[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
		return list.toArray(services);
	}

	/**
	 * Retain services based on equipment's network and equipment type
	 * @param services
	 * @param equipment
	 * @return Service[]
	 */
	public static ServiceInfo[] retainServicesByNetworkAndEquipmentType (ServiceInfo[] services, EquipmentBo equipment) {
		if (services == null || equipment == null) {
			return services;
		}

		List<ServiceInfo> list = new ArrayList<ServiceInfo> (services.length);
		for(int i = 0; i < services.length; i++) {
			ServiceInfo o = services[i];

			if ( o.isNetworkEquipmentTypeCompatible(equipment.getDelegate())) {
				list.add(o);
			} 
		}
		services = (ServiceInfo[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
		return list.toArray(services);
	}

	public static ServiceInfo[] retainServicesForPreHSPA(ServiceInfo[] service, EquipmentBo equipment) throws ApplicationException {		
		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service.length);
		MuleEquipment mule = null;
		if (equipment.isSIMCard()) {
			mule = equipment.getDelegate().getLastMule0();
		}

		for(int i=0; i<service.length; i++) {
			ServiceInfo o = service[i];

			boolean isPricePlan = "P".equals( o.getServiceType() );

			/***** Updated for Combo plan CR- Anitha Duraisamy - start ****/
			if (o.isRIM()) { // Service/PricePlan is RIM, check
				// Equipment
				if (mule != null && mule.isIDENRIM()) {
					list.add(o);
				} else if(!equipment.isIDEN()){
					String[] equipmentTypes;
					try {
						equipmentTypes = o.getEquipmentTypes(equipment.getNetworkType());
					} catch (TelusAPIException e) {
						throw new ApplicationException (SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
					}
					for(int j=0; j<equipmentTypes.length; j++) {
						if(equipmentTypes[j].equals(equipment.getEquipmentType()) || equipmentTypes[j].equals(Equipment.EQUIPMENT_TYPE_ALL)){
							list.add(o);
							break;
						}

					}	
				}
				/***** Updated for Combo plan CR- Anitha Duraisamy - end *******/
			} else if (isPricePlan == false && o.isSMSNotification()) { // should not check for PricePlan.
				if (equipment.isSMSCapable()) {
					list.add(o);
				}
			} else if (o.isPTT()) {
				if (equipment.isCellularDigital() && ((CellularDigitalEquipment) equipment).isPTTEnabled()) {
					list.add(o);
				}
			} else if (o.isMMS()) {
				if (equipment.isMMSCapable()) {
					list.add(o);
				} else if (mule != null && mule.isMMSCapable()) {
					list.add(o);
				} else {
					;
				}
			} else if (o.isEvDO()) {
				if (equipment.isCellularDigital()) {
					if (((CellularDigitalEquipment) equipment).isEvDOCapable()) {
						list.add(o);
					}
				}
			} else if (o.isVisto()) {
				if (isVistoCapable(equipment.getDelegate().getProductFeatures())) {
					list.add(o);
				}
			} else if (o.isLBSTrackee()) {
				if (equipment.isGPS()) {
					if (o.isMSBasedCapabilityRequired() && !equipment.isMSBasedEnabled()) {
						// remove service
					} else {
						list.add(o);
					}
				} else if (mule != null && mule.isGPS()) {
					if (o.isMSBasedCapabilityRequired() && !mule.isMSBasedEnabled()) {
						// remove service
					} else {
						list.add(o);
					}
				}

			}else if (o.isPDA()) {
				String[] equipmentTypes;
				try {
					equipmentTypes = o.getEquipmentTypes(equipment.getNetworkType());
				} catch (TelusAPIException e) {
					throw new ApplicationException (SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
				}
				for(int j=0; j<equipmentTypes.length; j++) {
					if(equipmentTypes[j].equals(equipment.getEquipmentType()) || equipmentTypes[j].equals(Equipment.EQUIPMENT_TYPE_ALL)){
						list.add(o);
						break;
					}

				}
			} else { // Service/PricePlan is not RIM, always add Service/PricePlan
				list.add(o);
			}
		}
		service = (ServiceInfo[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return list.toArray(service);
	}

	/**
	 * The legacy retainServices equivalent for HSPA logic. The logic here applies for HSPA only. If it is
	 * not a HSPA equipment (i.e. invoked by accident), do nothing.
	 * @param services
	 * @param equipment
	 * @return Service[]
	 * @throws TelusAPIException
	 */
	public static ServiceInfo[] retainServicesForHSPA(ServiceInfo[] services, EquipmentBo equipment)  {
		if (services == null || equipment == null || equipment.isHSPA() == false) {
			return services;
		}

		List<ServiceInfo> list = new ArrayList<ServiceInfo>(services.length);
		for (int i = 0; i < services.length; i++) {
			list.add(services[i]);
		}
		services = (ServiceInfo[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
		return list.toArray(services);
	}

	public static <T extends Reference> T[] difference(T[] list1, T[] list2) {
		HashMap<String, T> resultMap = new HashMap<String, T>(list1.length + list2.length);
		HashMap<String, T> map1 = toHashMap(list1);
		HashMap<String, T> map2 = toHashMap(list2);

		if ( !map1.isEmpty() ) {
			addAll(resultMap, map1);
		}

		if ( !resultMap.isEmpty() ) {
			removeAll(resultMap, map2);
		}

		return mapToArray(list1.getClass().getComponentType(), resultMap);
	}

	// MultiRing
	public static MultiRingInfo[] getMultiRingInfos(String[] originalSet, String[] modifiedSet, ServiceAgreementInfo service) {
		byte socMode = service.getTransaction();
		String socCode = service.getCode();
		if (BaseAgreementInfo.ADD == socMode) {
			return getMultiRingInfoArray(modifiedSet, MultiRingInfo.ADD, socCode);
		}
		else if (BaseAgreementInfo.DELETE == socMode) {
			return getMultiRingInfoArray(originalSet, MultiRingInfo.DELETE, socCode);
		}
		else if (BaseAgreementInfo.UPDATE == socMode || BaseAgreementInfo.NO_CHG == socMode) {
			MultiRingInfo[] deleted = getMultiRingInfoArray(originalSet, MultiRingInfo.DELETE, socCode);
			MultiRingInfo[] added = getMultiRingInfoArray(modifiedSet, MultiRingInfo.ADD, socCode);
			MultiRingInfo[] sum = new MultiRingInfo[deleted.length + added.length];

			for (int i=0; i<deleted.length; i++) {
				sum[i] = deleted[i];
			}

			for (int i=0, j=deleted.length; i<added.length; i++, j++) {
				sum[j] = added[i];
			}

			logger.debug("sum.length = " + sum.length);

			return sum;
		}

		return null;
	}

	private static MultiRingInfo[] getMultiRingInfoArray(String[] phones, byte mode, String socCode) {
		MultiRingInfo info = null;
		List<MultiRingInfo> list = new ArrayList<MultiRingInfo>();
		if (phones != null) {
			for (int i = 0; i < phones.length; i++) {

				info = new MultiRingInfo();

				info.setPhone(phones[i]);
				info.setMode(mode);
				info.setSocCode(socCode);

				list.add(info);
				logger.debug("phone number added: " + phones[i]);
			}
		}

		return (MultiRingInfo[])list.toArray(new MultiRingInfo[list.size()]);
	}

	// This method returns true if NOT saved contract contains prepaid calling circle SOC
	// in any status (add, delete or update)
	public static boolean containsNotSavedPrepaidCallingCircleService(ServiceAgreementInfo[] allServices) throws TelusAPIException {
		boolean found = false;
		ServiceAgreementInfo aService = null;
		for (int i = 0; i < allServices.length; i++) {
			aService = allServices[i];
			if (aService.getService().isWPS()
					&& (aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) || aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
					&& aService.getTransaction() != ServiceFeatureInfo.NO_CHG) {
				ServiceFeatureInfo aFeature = aService.getFeatures0(false)[0];
				/*
				 * if UPDATE and phone # list is not changed, SOC is removed and added back, phone # list will lose and default # will be inserted in
				 * KB if ADD and phone # list is not changed, even return true, won't add KB SOC into contract so only return true if phone # list is
				 * changed or DELETE
				 */

				if (aFeature.isCcParameterChanged() || aService.getTransaction() == ServiceFeatureInfo.DELETE)
					found = true;

				break;
			}
		}
		return found;
	}

	// This method returns ServiceAgreementInfo if NOT saved contract contains prepaid calling circle SOC 
	// in any status (add, delete or update)
	public static ServiceAgreementInfo getNotSavedPrepaidCallingCircleService(ServiceAgreementInfo[] allServices) throws TelusAPIException {
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getService().isWPS() && 
					(aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
							|| aService.getService().containsSwitchCode(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE)) 
							&& aService.getTransaction() != ServiceFeatureInfo.NO_CHG){
				return aService;
			}

		}		
		return null;
	}

	public static ServiceAgreementInfo getKbMappedPrepaidService(String kbMappedPrepaidServiceCode, ServiceAgreementInfo[] allServices) throws TelusAPIException {
		ServiceAgreementInfo aService = null;
		for(int i=0; i<allServices.length; i++) {
			aService = allServices[i];
			if (aService.getCode().trim().equals(kbMappedPrepaidServiceCode.trim())){
				return aService;
			}
		}		
		return null;
	}

	public static ServiceFeatureInfo getKbCallingCircleFeature(ServiceAgreementInfo kbMappedPrepaidService)	{
		ServiceFeatureInfo [] allfeatures = kbMappedPrepaidService.getFeatures0(false);
		ServiceFeatureInfo feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) 
					||	feature.getFeature().getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE))
				return feature;
		}
		return null;
	}

	public static ServiceFeatureInfo getPrepaidCallingCircleFeature(ServiceAgreementInfo callingCircleService, String featureCode)	{
		ServiceFeatureInfo [] allfeatures = callingCircleService.getFeatures0(false);
		ServiceFeatureInfo feature = null;
		for(int i=0; i<allfeatures.length; i++) {
			feature = allfeatures[i];
			if (feature.getFeature().getCode().trim().equals(featureCode.trim()))
				return feature;
		}
		return null;
	}

	private static <T extends Reference> T[] mapToArray(Class<?> class1, HashMap<String, T> resultMap) {
		T[] result = (T[])java.lang.reflect.Array.newInstance(class1, resultMap.size());
		return resultMap.values().toArray(result);
	}

	private static <T> void addToList(List<T> list, T[] object) {
		for (int i = 0; i < object.length; i++) {
			if (!list.contains(object[i])) {
				list.add(object[i]);
			}
		}
	}

	private static <T extends Reference> HashMap<String, T> toHashMap(T[] list) {
		HashMap<String, T> map = new HashMap<String, T>(list.length * 2);
		for(int i=0; i<list.length; i++) {
			map.put(list[i].getCode(), list[i]);
		}
		return map;
	}

	private static <T extends Reference> void addAll(HashMap<String, T> map1, HashMap<String, T> map2) {
		map1.putAll(map2);
	}

	private static <T extends Reference> void retainAll(HashMap<String, T> map1, HashMap<String, T> map2) {
		Iterator<String> e = map1.keySet().iterator();
		while (e.hasNext()) {
			Object key = e.next();
			if(!map2.containsKey(key)) {
				e.remove();
			}
		}
	}

	private static <T extends Reference> void removeAll(HashMap<String, T> map1, HashMap<String, T> map2) {
		Iterator<String> e = map1.keySet().iterator();
		while (e.hasNext()) {
			Object key = e.next();
			if (map2.containsKey(key)) {
				e.remove();
				//map1.remove(key);
			}
		}
	}

	protected static StringBuffer buildLogEntryHeader(String methodName, String activity) {
		StringBuffer sb = new StringBuffer();
		DATE_FORMAT.format(new Date(), sb, POSITION).append(" ");
		sb.append("[").append(Thread.currentThread().getName()).append("] ");

		sb.append(methodName).append("():").append(activity);
		return sb;
	}

	protected static StringBuffer appendSubscriberInfo(StringBuffer sb, SubscriberInfo subInfo) {
		if (subInfo != null) {
			sb.append("  subscriber[ban:").append(subInfo.getBanId()).append(", subId:").append(subInfo.getSubscriberId()).append(", phone:").append(subInfo.getPhoneNumber()).append("]");
		}
		return sb;
	}

	protected static void logSuccess(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-succeeded; ");
		if (extraMessage != null)
			sb.append(extraMessage).append(";");
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
	}

	protected static void logFailure(String methodName, String activity, SubscriberInfo subInfo, Throwable t, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity);
		sb.append("-failed; ");
		if (extraMessage != null)
			sb.append(extraMessage).append(";");
		appendSubscriberInfo(sb, subInfo);
		// appendPRMFault(sb, t);
		logger.debug(sb.toString());
		logger.debug(t);
	}

	protected static void logMessage(String methodName, String activity, SubscriberInfo subInfo, String extraMessage) {
		StringBuffer sb = buildLogEntryHeader(methodName, activity).append("; ").append(extraMessage);
		appendSubscriberInfo(sb, subInfo);
		logger.debug(sb.toString());
	}

	public static boolean validateBrandId(int brandId, Brand[] brands) {
		int[] brandIds = new int[brands.length];
		for (int i = 0; i < brands.length; i++) {
			brandIds[i] = brands[i].getBrandId();
		}				
		return isBrandIdExist(brandId, brandIds);
	}

	private static boolean isBrandIdExist(int brand, int[] brandIds){
		for(int i= 0; i < brandIds.length; i++){
			if(brand == brandIds[i]){
				return true;
			}
		}
		return false;
	}

	public static boolean isPTTServiceIncluded(ContractService[] services) {
		if (services == null || services.length == 0) {
			return false;
		}

		ContractService service = null;

		try {
			for (int i = 0; i < services.length; i++) {
				service = services[i];
				if (service == null || service.getService() == null) {
					return false;
				}
				if (service.getService().isPTT()) {
					return true;
				}
			}
		} catch (TelusAPIException e) {

		}

		return false;

	}

	public static boolean haveSameDealerAndSalesRepCode(ServiceAgreementInfo[] cs) {
		if (cs != null && cs.length > 0) {
			String dealerCode = null;
			String salesRepId = null;

			for (int i = 0 ; i < cs.length; i++) {
				if (dealerCode == null || salesRepId == null) {
					dealerCode = cs[i].getDealerCode();
					salesRepId = cs[i].getSalesRepId();
				}else if (!dealerCode.equals(cs[i].getDealerCode()) || !salesRepId.equals(cs[i].getSalesRepId())) {
					return false;
				}
			}
		}

		return true;
	}

	public static void copyServiceFeatures(ServiceAgreementInfo sa, ServiceAgreementInfo srcService) {
		ServiceFeatureInfo[] unsavedFeatures = sa.getFeatures0(false);
		ServiceFeatureInfo[] originalFeatures = srcService.getFeatures0(false);

		for (int i = 0; i < (unsavedFeatures != null ? unsavedFeatures.length : 0); i++) {
			for (int j = 0; j < (originalFeatures != null ? originalFeatures.length : 0); j++) {
				if (unsavedFeatures[i].getCode().trim().equals(originalFeatures[j].getCode().trim())) {
					unsavedFeatures[i].setParameter(originalFeatures[j].getParameter());
				}
			}
		}
	}

	public static boolean isVistoCapable(String[] productFeatureCodes) {
		if (productFeatureCodes == null || productFeatureCodes.length == 0) {

			return false;
		}

		for (int i=0; i<productFeatureCodes.length; i++) {
			String code = productFeatureCodes[i];
			if (Equipment.PRODUCT_FEATURE_3RDPARTYEMAIL.equals(code)) {
				return true;
			}
		}

		return false;
	}

	public static ServiceInfo[] removeTelephonyOnly(ServiceInfo[] service) {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service.length);

		for(int i=0; i<service.length; i++) {
			ServiceInfo o = service[i];
			if(o.isDispatchFeaturesIncluded() || o.isWirelessWebFeaturesIncluded()||(!o.isTelephonyFeaturesIncluded())) {
				list.add(o);
			}
		}
		service = (ServiceInfo[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return (ServiceInfo[])list.toArray(service);
	}

	public static ServiceInfo[] removeDispatchOnly(ServiceInfo[] service) {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service.length);

		for(int i=0; i<service.length; i++) {
			ServiceInfo o = service[i];
			if(o.isTelephonyFeaturesIncluded() || o.isWirelessWebFeaturesIncluded()||(!o.isDispatchFeaturesIncluded())) {
				list.add(o);
			}
		}
		service = (ServiceInfo[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return (ServiceInfo[])list.toArray(service);
	}

	public static ServiceInfo[] removeWirelessWebOnly(ServiceInfo[] service) {
		List<ServiceInfo> list = new ArrayList<ServiceInfo>(service.length);

		for(int i=0; i<service.length; i++) {
			ServiceInfo o = service[i];
			if(o.isTelephonyFeaturesIncluded() || o.isDispatchFeaturesIncluded()||(!o.isWirelessWebFeaturesIncluded())) {
				list.add(o);
			}
		}
		service = (ServiceInfo[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
		return (ServiceInfo[])list.toArray(service);
	}

	public static void checkServiceFeatureInfo(SubscriberContractInfo subscriberContractInfo, String serviceCode, String featureCode) throws ApplicationException {
		ServiceFeatureInfo feature = null;
		if (subscriberContractInfo.getPricePlanCode().equals(serviceCode)) {
			ServiceFeatureInfo[] featureList = subscriberContractInfo.getFeatures0(true, true); 
			for (ServiceFeatureInfo featureInfo : featureList) {
				if (featureInfo.getCode().equals(featureCode)) {
					feature = featureInfo;
					return;
				}
			}
		} else {
			ServiceAgreementInfo serviceAgreementInfo = subscriberContractInfo.getService0(serviceCode, true);
			if (serviceAgreementInfo != null) {
				try {
					feature = (ServiceFeatureInfo)serviceAgreementInfo.getFeature(featureCode);
				} catch (UnknownObjectException uoe) {
					feature = null;
				}
			} else {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Service Code [" + serviceCode + "] is not available on Subscriber Service Agreement", "");
			}
		}
		if (feature == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Service Code [" + serviceCode + "] is not available on Subscriber Service Agreement", "");
		}
	}

	public static RatedFeatureInfo getKbCallingCircleFeature(RatedFeatureInfo[] allFeatures) {
		for (RatedFeatureInfo feature : allFeatures) {
			if (feature.getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALLING_CIRCLE) ||
					feature.getSwitchCode().trim().equals(FeatureInfo.SWITCH_CODE_CALL_HOME_FREE)) {
				return feature;
			}
		}
		return null;
	}

	public static CallingCircleParametersInfo getCallingCircleParametersInfo(ServiceFeatureInfo kbFeature) {
		CallingCirclePhoneListInfo currentPhoneList = new CallingCirclePhoneListInfo();
		currentPhoneList.setEffectiveDate(kbFeature.getEffectiveDate());
		currentPhoneList.setExpiryDate(kbFeature.getExpiryDate());
		currentPhoneList.setPhoneNumberList(kbFeature.getCallingCirclePhoneNumbersFromParam());

		CallingCirclePhoneListInfo futurePhoneList = new CallingCirclePhoneListInfo();
		futurePhoneList.setEffectiveDate(kbFeature.getEffectiveDate());
		futurePhoneList.setExpiryDate(kbFeature.getExpiryDate());
		futurePhoneList.setPhoneNumberList(kbFeature.getCallingCirclePhoneNumbersFromParam());

		CallingCircleParametersInfo callingCircleParametersInfo =  new CallingCircleParametersInfo();
		callingCircleParametersInfo.setCallingCircleCurrentPhoneNumberList(currentPhoneList);
		callingCircleParametersInfo.setCallingCircleFuturePhoneNumberList(futurePhoneList);

		return callingCircleParametersInfo;
	}

	public static String genContractCallingCircleListMemoText(SubscriberContractInfo contractInfo) {

		StringBuilder sb = new StringBuilder();

		List ccFeatures = contractInfo.getCallingCircleFeatures(true);
		for (int i = 0; i<ccFeatures.size(); i++) {
			ServiceFeatureInfo ccFeature = (ServiceFeatureInfo) ccFeatures.get(i);
			String[] ccList = ccFeature.getCallingCirclePhoneNumbersFromParam();
			sb.append("\n");
			sb.append("Favourite Numbers (").append( ccFeature.getServiceCode().trim()).append(")"); 
			if (ccList.length == 0) {
				sb.append(" : \nEmpty");
			} else {
				sb.append(" [: \n");
				for(int k = 0; k<ccList.length; k++) {
					String phoneNumber = ccList[k].trim();
					for (int z = 0; z < phoneNumber.length(); z++) {
						sb.append(phoneNumber.charAt(z));
						if (z == 2 || z == 5) {
							sb.append("-");
						}
					}
					sb.append("\n");
				}
				sb.append("]");
			}
		}
		return sb.toString();
	}

	public static byte translateTransactionType(String transactionType) {

		if (WS_TRANSACTION_TYPE_ADD.equals(transactionType)) {
			return BaseAgreementInfo.ADD;
		} else if (WS_TRANSACTION_TYPE_MODIFY.equals(transactionType)) {
			return BaseAgreementInfo.UPDATE;
		} else if (WS_TRANSACTION_TYPE_NOCHG.equals(transactionType)) {
			return BaseAgreementInfo.NO_CHG;
		} else if (WS_TRANSACTION_TYPE_REMOVE.equals(transactionType)) {
			return BaseAgreementInfo.DELETE;
		}

		return BaseAgreementInfo.NO_CHG;
	}

}