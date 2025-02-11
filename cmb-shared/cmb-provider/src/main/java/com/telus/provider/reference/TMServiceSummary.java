/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.FundSource;
import com.telus.api.reference.PrepaidCategory;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceDataSharingGroup;
import com.telus.api.reference.ServicePeriod;
import com.telus.api.reference.ServiceRelation;
import com.telus.api.reference.ServiceSummary;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServicePolicyInfo;

public class TMServiceSummary implements ServiceSummary {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 *@link aggregation
	 */
	protected final TMReferenceDataManager referenceDataManager;
	/**
	 * @link aggregation
	 */
	private final ServiceInfo delegate;
	
	private TMService service;

	public TMServiceSummary(TMReferenceDataManager referenceDataManager, ServiceInfo delegate) {
		this.referenceDataManager = referenceDataManager;
		this.delegate = delegate;
	}

	// --------------------------------------------------------------------
	// Decorative Methods
	// --------------------------------------------------------------------
	public String getServiceType() {
		return delegate.getServiceType();
	}

	public int getTermMonths() {
		return delegate.getTermMonths();
	}

	/**
	 * @deprecated
	 */
	public String[] getEquipmentTypes() {

		String[] equipTypes = delegate.getEquipmentTypes();

		// We do have equipmentTypes populated in the info class . But as of May
		// release 2009, depends on how the service get retrieved:
		// 1) ReferenceDataManager.getRegularService( SOC )
		// 2) through PricePlan interface
		// in case 1, info contain accurate equipmentTypes
		// whereas in case 2, info only contain one equipment type which is
		// either "9" or the input parameter when query the pricePlan.
		// So, if the the info's equipmenTypes contain only one and is not "9":
		// try to fetch the information again through ReferenDataManager.
		// Sure this will cause some overheads as there a lot of services which
		// only support one equipmentType. Given the fact the information
		// is cached, it shouldn't too bad.
		//
		// if we can't find anything, then just return whatever we have in the
		// info class

		// TODO change the all the underlying SQL procedure/DAO to return the
		// correct equipmentTypes, once that in place,
		// we should remove the following block
		/*
		 * if ( equipTypes.length==1 &&
		 * equipTypes[0].equals(Equipment.EQUIPMENT_TYPE_ALL)==false ) { try {
		 * equipTypes =
		 * referenceDataManager.getServiceEquipmentTypeInfo(this.getCode
		 * ()).getEquipmentTypes(); } catch (Throwable e) { }
		 * 
		 * if ( equipTypes!=null && equipTypes.length>0 ) { //overwrite delegate
		 * delegate.setEquipmentTypes(equipTypes); } else { equipTypes =
		 * delegate.getEquipmentTypes(); } }
		 */
		return equipTypes;
	}

	public String getProductType() {
		return delegate.getProductType();
	}

	public String getLevelCode() {
		return delegate.getLevelCode();
	}

	public boolean isClientActivation() {
		return delegate.isClientActivation();
	}

	public boolean isDealerActivation() {
		return delegate.isDealerActivation();
	}

	public boolean isBillingZeroChrgSuppress() {
		return delegate.isBillingZeroChrgSuppress();
	}

	public boolean isIncludedPromotion() {
		return delegate.isIncludedPromotion();
	}

	public boolean isPromotion() {
		return delegate.isPromotion();
	}

	public boolean hasPromotion() {
		return delegate.hasPromotion();
	}

	public boolean isBoundService() {
		return delegate.isBoundService();
	}

	public boolean hasBoundService() {
		return delegate.hasBoundService();
	}

	public boolean isDiscountAvailable() {
		return delegate.isDiscountAvailable();
	}

	public Date getEffectiveDate() {
		return delegate.getEffectiveDate();
	}

	public Date getExpiryDate() {
		return delegate.getExpiryDate();
	}

	public boolean isForSale() {
		return delegate.isForSale();
	}

	public boolean isCurrent() {
		return delegate.isCurrent();
	}

	public boolean isActive() {
		return delegate.isActive();
	}

	public boolean isAvailable() {
		return delegate.isAvailable();
	}

	public boolean isTelephonyFeaturesIncluded() {
		return delegate.isTelephonyFeaturesIncluded();
	}

	public boolean isDispatchFeaturesIncluded() {
		return delegate.isDispatchFeaturesIncluded();
	}

	public boolean isWirelessWebFeaturesIncluded() {
		return delegate.isWirelessWebFeaturesIncluded();
	}

	public String getDescription() {
		return delegate.getDescription();
	}

	public String getDescriptionFrench() {
		return delegate.getDescriptionFrench();
	}

	public String getCode() {
		return delegate.getCode();
	}

	public int getTerm() {
		return delegate.getTerm();
	}

	public String getPeriodCode() {
		return delegate.getPeriodCode();
	}

	public String getUserSegment() {
		return delegate.getUserSegment();
	}

	public String getTermUnits() {
		return delegate.getTermUnits();
	}

	public double getMinimumUsageCharge() {
		return delegate.getMinimumUsageCharge();
	}

	public boolean isWPS() {
		return delegate.isWPS();
	}

	public boolean isKnowbility() {
		return delegate.isKnowbility();
	}

	public boolean isSequentiallyBoundService() {
		return delegate.isSequentiallyBoundService();
	}

	public boolean hasSequentiallyBoundService() {
		return delegate.hasSequentiallyBoundService();
	}

	public boolean hasEquivalentService() {
		return delegate.hasEquivalentService();
	}

	public boolean isSharable() {
		return delegate.isSharable();
	}

	public boolean isGrandFathered() {
		return delegate.isGrandFathered();
	}

	public boolean getRemoveOnPriceplanChange() {
		return delegate.getRemoveOnPriceplanChange();
	}

	public int getMaxTerm() {
		return delegate.getMaxTerm();
	}

	public boolean isAutoRenewalAllowed() {
		return delegate.isAutoRenewalAllowed();
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public String toString() {
		return delegate.toString();
	}

	// --------------------------------------------------------------------
	// Service Methods
	// --------------------------------------------------------------------
	public ServiceInfo getDelegate() {
		return delegate;
	}

	public Service getService() throws TelusAPIException {
		if (service == null) {
			service = (TMService) referenceDataManager.getRegularService(delegate.getCode());
		}
		return service;
	}

	private ServiceRelation[] filterRelationsByContract(ServiceRelation[] relations, PricePlan pricePlan) throws TelusAPIException {
		List list = new ArrayList(relations.length);
		for (int i = 0; i < relations.length; i++) {
			ServiceRelation r = relations[i];
			Service s = r.getService().getService();

			if (!r.isOptional() || pricePlan.containsOptionalService(s.getCode())) {
				list.add(r);
			}
		}

		return (ServiceRelation[]) list.toArray(new ServiceRelation[list.size()]);
	}

	public ServiceRelation[] getRelations(PricePlan pricePlan) throws TelusAPIException {
		ServiceRelation[] relations = referenceDataManager.getServiceRelations(getCode());
		return filterRelationsByContract(relations, pricePlan);
	}

	public ServiceRelation[] getRelations(PricePlan pricePlan, String relationType) throws TelusAPIException {
		ServiceRelation[] relations = referenceDataManager.getServiceRelations(getCode(), relationType);
		return filterRelationsByContract(relations, pricePlan);
	}

	public ServiceRelation[] getRelations(Contract contract) throws TelusAPIException {
		return getRelations(contract.getPricePlan());
	}

	public ServiceRelation[] getRelations(Contract contract, String relationType) throws TelusAPIException {
		return getRelations(contract.getPricePlan(), relationType);
	}

	/**
	 * @deprecated
	 * @see getEquivalentService (PricePlan, String)
	 */
	public Service getEquivalentService(PricePlan pricePlan) throws TelusAPIException {
		return getEquivalentService(pricePlan, getNetworkType());
	}

	public Service getEquivalentService(PricePlan pricePlan, String networkType) throws TelusAPIException {
		try {
			if (pricePlan.containsService(getCode())) {
				return pricePlan.getService(getCode());
			}

			if (hasEquivalentService()) {
				String[] serviceCodes = referenceDataManager.getReferenceDataHelperEJB().retrieveServiceFamily(getCode(),
						ReferenceDataManager.EQUIVALENT_SERVICE_FAMILY_TYPE, networkType);
				for (int i = 0; i < serviceCodes.length; i++) {
					if (pricePlan.containsService(serviceCodes[i])) {
						return pricePlan.getService(serviceCodes[i]);
					}
				}
			}

			return null;
		} catch (TelusAPIException e) {
			throw (TelusAPIException) e.fillInStackTrace();
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	/**
	 *@deprecated As of February release, 2011, 
	 * replaced by <code>retainByPrivilege(ServiceSummary[] services, String businessRoleCode, 
	 * 		String privilegeCode)</code>
	 * This method will be removed by CASPER project - Planned for May 2011 release  
	 */
	public boolean containsPrivilege(String businessRoleCode, String privilegeCode) throws TelusAPIException {
		ServicePolicyInfo[] servicePolicies = referenceDataManager.checkServicePrivilege( new ServiceInfo[] {getDelegate()}, businessRoleCode, privilegeCode);
		return servicePolicies[0].isAvailable();
	}

	public String[] getProvinces() throws TelusAPIException {
		try {
			return referenceDataManager.getReferenceDataHelperEJB().retrieveMarketProvinces(this.getCode());
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public String[] getFamilyGroupCodes(String familyGroupType) throws TelusAPIException {
		try { // Change to right method
			return referenceDataManager.getReferenceDataHelperEJB().retrieveServiceFamilyGroupCodes(this.getCode(), familyGroupType);
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}
	}

	public ServicePeriod[] getServicePeriods() throws TelusAPIException {
		try {
			return decorate((referenceDataManager.getServiceUsageInfo(this.getCode())).getServicePeriods());
			// return
			// referenceDataManager.getReferenceDataHelperEJB().retrieveServiceUsageInfo(this.getCode()).getServicePeriods();
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}

	}

	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term) throws TelusAPIException {
		try {
			return referenceDataManager.getReferenceDataHelperEJB().isAssociatedIncludedPromotion(pricePlanCode, term, this.getCode());
		} catch (Throwable e) {
			throw new TelusAPIException(e);
		}

	}

	// -----------------------------------------------------------------------------------
	// Decorators: .
	// -----------------------------------------------------------------------------------
	public ServicePeriod decorate(ServicePeriod servicePeriod) {
		if (servicePeriod == null) {
			return null;
		}

		return new TMServicePeriod(referenceDataManager, (ServicePeriodInfo) servicePeriod);
	}

	public ServicePeriod[] decorate(ServicePeriod[] servicePeriods) {
		ServicePeriod[] decoratedServicePeriods = new ServicePeriod[servicePeriods.length];
		for (int i = 0; i < servicePeriods.length; i++) {
			decoratedServicePeriods[i] = decorate(servicePeriods[i]);
		}
		return decoratedServicePeriods;
	}

	public boolean isSMSNotification() {
		return delegate.isSMSNotification();
	}

	public boolean isLoyaltyAndRetentionService() {
		return delegate.isLoyaltyAndRetentionService();
	}

	public boolean isWiFi() {
		return delegate.isWiFi();
	}

	public boolean hasPromotionalCredit() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	/**
	 * Return true if this is a Multimedia Messaging Service (MMS).
	 * 
	 * @return boolean
	 */
	public boolean isMMS() {
		return delegate.isMMS();
	}

	/**
	 * Return true if this is a Java Download Service.
	 * 
	 * @return boolean
	 */
	// public boolean isJavaDownload() {
	// return delegate.isJavaDownload();
	// }

	/**
	 * Return true if this is a Mobile Originated Short Message Service (MOSMS).
	 * 
	 * @return boolean
	 */
	public boolean isMOSMS() {
		return delegate.isMOSMS();
	}

	/**
	 * Call info class. return true if this Service is for PDA Only
	 * 
	 * @return boolean
	 */
	public boolean isPDA() {
		return delegate.isPDA();
		/*
		 * String[] equipTypes = getEquipmentTypes(); return ((equipTypes.length
		 * == 0) ? false : ((equipTypes.length == 1) &&
		 * (equipTypes[0].equals(Equipment.EQUIPMENT_TYPE_PDA))) ? true :
		 * false);
		 */
	}

	/**
	 * For life style bundles - Surf SOC's
	 * 
	 * @return boolean
	 */
	public boolean isDowngradable() {
		return delegate.isDowngradable();
	}

	public boolean isEmailAndWebspaceIncluded() {
		return delegate.isEmailAndWebspaceIncluded();
	}

	public boolean isEvDO() {
		return delegate.isEvDO();
	}

	/**
	 * Returns true if it's LBS tracker
	 * 
	 * @return boolean
	 */
	public boolean isLBSTracker() {

		return delegate.isLBSTracker();
	}

	/**
	 * Returns true if it's LBS trackee
	 * 
	 * @return boolean
	 */
	public boolean isLBSTrackee() {
		return delegate.isLBSTrackee();
	}

	/**
	 * Returns true, if CategoryCode = CATEGORY_CODE_MS_BASED
	 * 
	 * @return boolean
	 */
	public boolean isMSBasedCapabilityRequired() {
		return delegate.isMSBasedCapabilityRequired();
	}

	public boolean isNonCurrent() {
		return delegate.isNonCurrent();
	}

	public boolean containsCategory(String category) {
		return delegate.containsCategory(category);
	}

	public boolean isForcedAutoRenew() {
		return delegate.isForcedAutoRenew();
	}

	public boolean hasVoiceToTextFeature() {
		return delegate.hasVoiceToTextFeature();
	}

	public boolean isMandatory() {
		return delegate.isMandatory();
	}

	public PrepaidCategory getCategory() {
		return delegate.getCategory();
	}

	public int getPriority() {
		return delegate.getPriority();
	}

	public int getMaxConsActDays() {
		return delegate.getMaxConsActDays();
	}

	public String getWPSMappedKBSocCode() {
		return delegate.getWPSMappedKBSocCode();
	}

	public boolean isPrepaidLBM() {
		return delegate.isPrepaidLBM();
	}

	public String getWPSServiceType() {
		return delegate.getWPSServiceType();
	}

	public boolean isPromoValidationEligible() {
		return delegate.isPromoValidationEligible();
	}

	/**
	 * @deprecated
	 */
	public String getNetworkType() {
		return delegate.getNetworkType();
	}

	public String[] getAllNetworkTypes() {
		return delegate.getAllNetworkTypes();
	}

	public String[] getEquipmentTypes(String networkType) {
		return delegate.getEquipmentTypes(networkType);
	}

	public String getNetworkType(String equipmentType) {
		return delegate.getNetworkType(equipmentType);
	}

	public boolean isCompatible(String networkType, String equipmentType) {
		return delegate.isCompatible(networkType, equipmentType);
	}

	public boolean isCompatible(String networkType) {
		return delegate.isCompatible(networkType);
	}

	public boolean isNetworkEquipmentTypeCompatible(Equipment equipment) throws TelusAPIException {
		return delegate.isNetworkEquipmentTypeCompatible(equipment);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.api.reference.ServiceSummary#getAllowedRenewalFundSourceArray()
	 */
	public FundSource[] getAllowedRenewalFundSourceArray() {
		return delegate.getAllowedRenewalFundSourceArray();
	}
	

	public String getBillCycleTreatmentCode() {
	    return delegate.getBillCycleTreatmentCode();
	}

	public FundSource[] getAllowedPurchaseFundSourceArray() {
		return delegate.getAllowedPurchaseFundSourceArray();
	}
	
	/**
	 * Returns all the data sharing groups that this service belongs to.  If this service
	 * does not belong to any data sharing group, then this method returns an array of 0
	 * length.  This method will not return null.
	 */

	public ServiceDataSharingGroup[] getDataSharingGroups()
			throws TelusAPIException {
		return delegate.getDataSharingGroups();
	}
	
	public int getBrandId() {
		return delegate.getBrandId();
	}
	
	public String[] getFamilyTypes() {
		return delegate.getFamilyTypes();
	}

	public String getSocServiceType() {
		return delegate.getSocServiceType();
	}

	public boolean isFlexPlan() {
		return delegate.isFlexPlan();
	}

	public boolean isMandatoryAddOn() {
		return delegate.isMandatoryAddOn();
	}

}