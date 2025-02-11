package com.telus.cmb.subscriber.decorators;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.FundSource;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.utility.info.FundSourceInfo;
import com.telus.eas.utility.info.PrepaidCategoryInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.eas.utility.info.ServiceRelationInfo;
import com.telus.eas.utility.info.ServiceSetInfo;

public abstract class PricePlanDecorator {
	protected final PricePlanInfo delegate;
	
	public PricePlanDecorator (PricePlanInfo pricePlan) {
		delegate = pricePlan;
	}
	
	public PricePlanInfo getDelegate() {
		return delegate;
	}
	
	
	public int getIncludedServiceCount() {
		return delegate.getIncludedServiceCount();
	}

	
	public Service[] getIncludedServices() {
		return delegate.getIncludedServices();
	}

	
	public ServiceInfo getIncludedService(String code) throws UnknownObjectException {
		return delegate.getIncludedService0(code);
	}

	
	public PricePlanInfo getPricePlan(String equipmentType, String provinceCode, char accountType, char accountSubType){
		return delegate;
	}

	
	public boolean containsIncludedService(String code) {
		return delegate.containsIncludedService(code);
	}

	
	public boolean containsService(String code) {
		return delegate.containsService(code);
	}

	
	public int getUsageRatingFrequency() {
		return delegate.getUsageRatingFrequency();
	}

	
	public int getIncludedMinutesCount() {
		return delegate.getIncludedMinutesCount();
	}

	
	public boolean isSharable() {
		return delegate.isSharable();
	}

	
	public boolean isSuspensionPricePlan() {
		return delegate.isSuspensionPricePlan();
	}

	
	public boolean isAvailableForActivation() {
		return delegate.isAvailableForActivation();
	}

	
	public boolean isAvailableForChange() {
		return delegate.isAvailableForChange();
	}

	
	public boolean isAvailableForChangeByDealer() {
		return delegate.isAvailableForChangeByDealer();
	}

	
	public boolean isAvailableForChangeByClient() {
		return delegate.isAvailableForChangeByClient();
	}

	
	public boolean isAvailableToModifyByDealer() {
		return delegate.isAvailableToModifyByDealer();
	}

	
	public boolean isAvailableToModifyByClient() {
		return delegate.isAvailableToModifyByClient();
	}

	
	public boolean isAvailableForNonCorporateRenewal() {
		return delegate.isAvailableForNonCorporateRenewal();
	}

	
	public boolean isAvailableForCorporateRenewal() {
		return delegate.isAvailableForCorporateRenewal();
	}

	
	public boolean isAvailableForCorporateStoreActivation() {
		return delegate.isAvailableForCorporateStoreActivation();
	}

	
	public boolean isAvailableForRetailStoreActivation() {
		return delegate.isAvailableForRetailStoreActivation();
	}

	public int[] getAvailableTermsInMonths() throws TelusAPIException {
		return delegate.getAvailableTermsInMonths();
	}

	
	public boolean isMinutePoolingCapable() {
		return delegate.isMinutePoolingCapable();
	}

	
	public boolean isZeroIncludedMinutes() {
		return delegate.isZeroIncludedMinutes();
	}

	
	public int getBrandId() {
		return delegate.getBrandId();
	}

	
	public boolean isAOMPricePlan() {
		return delegate.isAOMPricePlan();
	}

	
	public boolean isDollarPoolingCapable() {
		return delegate.isDollarPoolingCapable();
	}

	
	public int getFeatureCount() {
		return delegate.getFeatureCount();
	}

	
	public RatedFeature[] getFeatures() {
		return delegate.getFeatures();
	}

	
	public boolean isParameterRequired() {
		return delegate.isParameterRequired();
	}

	
	public boolean isAdditionalNumberRequired() {
		return delegate.isAdditionalNumberRequired();
	}

	
	public RatedFeature getFeature(String code) throws UnknownObjectException {
		return delegate.getFeature(code);
	}

	
	public double getRecurringCharge() {
		return delegate.getRecurringCharge();
	}

	
	public int getRecurringChargeFrequency() {
		return delegate.getRecurringChargeFrequency();
	}

	
	public boolean containsFeature(String code) {
		return delegate.containsFeature(code);
	}

	
	public boolean containsSwitchCode(String switchCode) {
		return delegate.containsSwitchCode(switchCode);
	}

	
	public boolean isCrossFleetRestricted() {
		return delegate.isCrossFleetRestricted();
	}

	
	public String[] getCategoryCodes() {
		return delegate.getCategoryCodes();
	}

	
	public String getCoverageType() {
		return delegate.getCoverageType();
	}

	
	public boolean isPTT() {
		return delegate.isPTT();
	}

	
	public boolean isRIM() {
		return delegate.isRIM();
	}

	
	public boolean hasAlternateRecurringCharge() {
		return delegate.hasAlternateRecurringCharge();
	}

	
	public double getAlternateRecurringCharge(Subscriber subscriber) throws TelusAPIException {
		return delegate.getAlternateRecurringCharge(subscriber);
	}

	
	public boolean isRUIM() {
		return delegate.isRUIM();
	}

	
	public boolean isInternationalCalling() {
		return delegate.isInternationalCalling();
	}

	
	public boolean isInternationalRoaming() {
		return delegate.isInternationalRoaming();
	}

	
	public boolean isVisto() {
		return delegate.isVisto();
	}

	
	public boolean is911() {
		return delegate.is911();
	}

	
	public boolean hasCallHomeFree() {
		return delegate.hasCallHomeFree();
	}

	
	public boolean hasCallingCircleFeatures() {
		return delegate.hasCallingCircleFeatures();
	}

	
	public boolean hasRIMAPN() {
		return delegate.hasRIMAPN();
	}

	
	public boolean hasMBAPN() {
		return delegate.hasMBAPN();
	}

	
	public String getNetworkType() {
		return delegate.getNetworkType();
	}

	
	public String getNetworkType(String equipmentType) {
		return delegate.getNetworkType(equipmentType);
	}

	
	public String[] getAllNetworkTypes() {
		return delegate.getAllNetworkTypes();
	}

	
	public String getServiceType() {
		return delegate.getServiceType();
	}

	
	public int getTermMonths() {
		return delegate.getTermMonths();
	}

	
	public String[] getEquipmentTypes() {
		return delegate.getEquipmentTypes();
	}

	
	public String[] getEquipmentTypes(String networkType) {
		return delegate.getEquipmentTypes(networkType);
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

	
	public boolean isGrandFathered() {
		return delegate.isGrandFathered();
	}

	
	public String[] getFamilyGroupCodes(String familyGroupType) throws TelusAPIException {
		return delegate.getFamilyGroupCodes(familyGroupType);
	}

	
	public boolean isLoyaltyAndRetentionService() {
		return delegate.isLoyaltyAndRetentionService();
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

	
	public Service getService() throws TelusAPIException {
		return delegate.getService();
	}

	
	public abstract ServiceRelationInfo[] getRelations(SubscriberContractInfo contract);

	
	public abstract ServiceRelationInfo[] getRelations(SubscriberContractInfo contract, String relationType);

	
	public abstract ServiceRelationInfo[] getRelations(PricePlanInfo pricePlan);

	
	public abstract ServiceRelationInfo[] getRelations(PricePlanInfo pricePlan, String relationType);

	
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

	
	public boolean isSequentiallyBoundService() {
		return delegate.isSequentiallyBoundService();
	}

	
	public boolean hasSequentiallyBoundService() {
		return delegate.hasSequentiallyBoundService();
	}

	
	public boolean isPromoValidationEligible() {
		return delegate.isPromoValidationEligible();
	}

	
	public int getTerm() {
		return delegate.getTerm();
	}

	
	public String getTermUnits() {
		return delegate.getTermUnits();
	}

	
	public boolean isWPS() {
		return delegate.isWPS();
	}

	
	public boolean isKnowbility() {
		return delegate.isKnowbility();
	}

	
	public boolean hasEquivalentService() {
		return delegate.hasEquivalentService();
	}

	
	public Service getEquivalentService(PricePlan pricePlan) throws TelusAPIException {
		return delegate.getEquivalentService(pricePlan);
	}

	
	public abstract ServiceInfo getEquivalentService(PricePlanInfo pricePlan, String networkType);

	
	public int getMaxTerm() {
		return delegate.getMaxTerm();
	}

	
	public boolean isAutoRenewalAllowed() {
		return delegate.isAutoRenewalAllowed();
	}

	
	public boolean isForcedAutoRenew() throws TelusAPIException {
		return delegate.isForcedAutoRenew();
	}

	
	public String getPeriodCode() {
		return delegate.getPeriodCode();
	}

	
	public boolean containsPrivilege(String businessRole, String privilege) throws TelusAPIException {
		return delegate.containsPrivilege(businessRole, privilege);
	}

	
	public String[] getProvinces() throws TelusAPIException {
		return delegate.getProvinces();
	}

	
	public String getUserSegment() {
		return delegate.getUserSegment();
	}

	
	public abstract ServicePeriodInfo[] getServicePeriods();

	
	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term) throws TelusAPIException {
		return delegate.isAssociatedIncludedPromotion(pricePlanCode, term);
	}

	
	public double getMinimumUsageCharge() {
		return delegate.getMinimumUsageCharge();
	}

	
	public boolean isSMSNotification() {
		return delegate.isSMSNotification();
	}

	
	public boolean isWiFi() {
		return delegate.isWiFi();
	}

	
	public boolean isMMS() {
		return delegate.isMMS();
	}

	
	public boolean isMOSMS() {
		return delegate.isMOSMS();
	}

	
	public boolean isPDA() {
		return delegate.isPDA();
	}

	
	public boolean isDowngradable() {
		return delegate.isDowngradable();
	}

	
	public boolean isEmailAndWebspaceIncluded() {
		return delegate.isEmailAndWebspaceIncluded();
	}

	
	public boolean isEvDO() {
		return delegate.isEvDO();
	}

	
	public boolean isLBSTracker() {
		return delegate.isLBSTracker();
	}

	
	public boolean isLBSTrackee() {
		return delegate.isLBSTrackee();
	}

	
	public boolean isMSBasedCapabilityRequired() {
		return delegate.isMSBasedCapabilityRequired();
	}

	
	public boolean isNonCurrent() {
		return delegate.isNonCurrent();
	}

	
	public boolean containsCategory(String category) {
		return delegate.containsCategory(category);
	}

	
	public boolean hasVoiceToTextFeature() {
		return delegate.hasVoiceToTextFeature();
	}

	
	public boolean isMandatory() {
		return delegate.isMandatory();
	}

	
	public PrepaidCategoryInfo getCategory() {
		return (PrepaidCategoryInfo) delegate.getCategory();
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

	
	public boolean isCompatible(String networkType) {
		return delegate.isCompatible(networkType);
	}

	
	public boolean isCompatible(String networkType, String equipmentType) {
		return delegate.isCompatible(networkType, equipmentType);
	}

	
	public boolean isNetworkEquipmentTypeCompatible(Equipment equipment) throws TelusAPIException {
		return delegate.isNetworkEquipmentTypeCompatible(equipment);
	}

	
	public FundSource[] getAllowedRenewalFundSourceArray() {
		return delegate.getAllowedRenewalFundSourceArray();
	}

	
	public String getBillCycleTreatmentCode() {
		return delegate.getBillCycleTreatmentCode();
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

	
	public abstract ServiceSetInfo[] getMandatoryServiceSets(EquipmentInfo equipment);

	
	public abstract ServiceSetInfo[] getMandatoryServiceSets(String equipmentType, String networkType);

	
	public abstract ServiceSetInfo[] getMandatoryServiceSets(EquipmentInfo equipment, String overrideEquipmentType);

	
	public ServiceSetInfo[] getMandatoryServiceSets(){
		return (ServiceSetInfo[]) delegate.getMandatoryServiceSets();
	}

	
	public int getOptionalServiceCount() {
		return delegate.getOptionalServiceCount();
	}

	
	public ServiceInfo[] getOptionalServices() {
		return delegate.getOptionalServices0();
	}

	
	public abstract ServiceInfo [] getOptionalServices(EquipmentDecorator equipment);

	
	public abstract ServiceInfo[] getOptionalServices(EquipmentInfo equipment, boolean includePrepaidServices);

	
	public abstract ServiceInfo[] getOptionalServices(EquipmentInfo equipment, boolean includePrepaidServices, String overrideEquipmentType);

	
	public ServiceInfo[] getTelephonyServices() {
		return (ServiceInfo[]) delegate.getTelephonyServices();
	}

	
	public ServiceInfo[] getDispatchServices() {
		return (ServiceInfo[]) delegate.getDispatchServices();
	}

	
	public ServiceInfo[] getWirelessWebServices() {
		return (ServiceInfo[]) delegate.getWirelessWebServices();
	}

	
	public ServiceInfo getOptionalService(String code) throws UnknownObjectException {
		return delegate.getOptionalService0(code);
	}

	
	public ServiceInfo getService(String code) throws UnknownObjectException {
		return delegate.getService1(code);
	}

	
	public boolean containsOptionalService(String code) {
		return delegate.containsOptionalService(code);
	}

	
	public boolean waiveActivationFee() {
		return delegate.waiveActivationFee();
	}

	
	public abstract PricePlanDecorator[] getPricePlanFamily(String provinceCode, String equipmentType, boolean currentPlansOnly, int termInMonths);

	
	public abstract PricePlanDecorator[] getPricePlanFamily(String provinceCode, String equipmentType, String networkType, boolean currentPlansOnly, int termInMonths);

	
	public abstract ServiceInfo[] getIncludedPromotions(String equpmentType, String provinceCode, int termInMonths);

	
	public abstract ServiceInfo[] getIncludedPromotions(String networkType, String equpmentType, String provinceCode, int termInMonths);

	
	public boolean isAssociatedService(Service service) throws TelusAPIException {
		return delegate.isAssociatedService(service);
	}

	
	public abstract ServiceInfo[] getIncludedPromotions(EquipmentInfo equipment, String provinceCode, int termInMonths);

	/* (non-Javadoc)
	 * @see com.telus.api.reference.ServiceSummary#getAllowedPurchaseFundSourceArray()
	 */
	
	public FundSourceInfo[] getAllowedPurchaseFundSourceArray() {
		return (FundSourceInfo[]) delegate.getAllowedPurchaseFundSourceArray();
	}

	public String getSecondarySubscriberService() {
		return delegate.getSecondarySubscriberService();
	}
	
	public int getMaximumSubscriberCount() {
		return delegate.getMaximumSubscriberCount();
	}
}
