/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.telus.api.InvalidServiceException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Contract;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Feature;
import com.telus.api.reference.FundSource;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.PrepaidCategory;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceDataSharingGroup;
import com.telus.api.reference.ServicePeriod;
import com.telus.api.reference.ServiceRelation;
import com.telus.api.util.ClientApiUtils;
import com.telus.eas.framework.info.Info;


public class ServiceInfo extends Info implements Service {

	public static final long serialVersionUID = -3514171641931107177L;

	public static final String SERVICE_TYPE_CODE_PRICE_PLAN = "P";
	// KB term optional SOC == included service
	public static final String SERVICE_TYPE_CODE_OPTIONAL_SOC = "O";
	//KB regular SOC = add-on services  
	public static final String SERVICE_TYPE_CODE_REGULAR_SOC = "R";
	//KB regular auto-expire SOC = add-on service (with term)
	public static final String SERVICE_TYPE_CODE_REG_AUTOEXP_SOC = "G";
	//KB optional auto-expire SOC = included service (with term )
	public static final String SERVICE_TYPE_CODE_OPTIONAL_AUTOEXP_SOC = "T";
	//KB promo SOC 
	public static final String SERVICE_TYPE_CODE_PROMO_SOC = "S";
	
	public static final String SERVICE_FEATURE_FLEX_EVENINGS_PCS = "FLEX";
	public static final String SERVICE_FEATURE_FLEX_EVENINGS_MIKE = "MFLEX";

	private static final String EQUIVALENT_SOC = "E";
	private static final String L_N_R_SOC = "L";
	private static final String LBS_TRACKER = "C";
	private static final String LBS_TRACKEE = "G";
	private static final String MANDATORY = "A";
	private static final String MOBILE_APP = "Z";

	private String serviceType = "";
	private String code = "";
	private String description = "";
	private String descriptionFrench = "";
	private String productType = "";
	// private int featureCount = 0;
	private Map ratedFeatures = new HashMap();
	private double recurringCharge = 0;
	private double additionalCharge = 0;
	private int recurringChargeFrequency = 0;
	// private final RatedFeatureInfo ratedFeature = new RatedFeatureInfo();
	private boolean dealerActivation = true;
	private boolean clientActivation = true;
	private int termMonths = 0;
	private boolean billingZeroChrgSuppress;
	// private boolean promotion =false;
	// private PromoTermInfo promotionTerm = new PromoTermInfo();
	private boolean wirelessWebFeaturesIncluded;
	private boolean telephonyFeaturesIncluded;
	private boolean dispatchFeaturesIncluded;
	private boolean active;
	private boolean current;
	private boolean forSale;
	private java.util.Date effectiveDate;
	private java.util.Date expiryDate;
	private boolean discountAvailable;

	private int term;
	private String termUnits;
	private boolean wps;
	private boolean rim = false;
	private boolean knowbility;
	private boolean includedPromotion = false;
	private boolean boundService = false;
	private boolean promotionAttached;
	private boolean boundServiceAttached;
	private int usageRatingFrequency;
	private boolean sequentiallyBoundService;
	private boolean sequentiallyBoundServiceAttached;
	private boolean equivalentServiceExists;
	private String levelCode;
	private boolean removeOnPriceplanChange;
	private int maxTerm;
	private boolean autoRenewalAllowed;
	private String periodCode;
	private String[] categoryCodes = new String[0];
	private String coverageType;
	private String userSegment;
	boolean hasAlternateRecurringCharge;
	private double minimumUsageCharge = 0;
	private boolean ptt = false;
	private boolean loyaltyAndRetentionService;
	private boolean emailAndWebspaceIncluded;
	private boolean LBSTracker;
	private boolean LBSTrackee;
	private boolean forcedAutoRenew;
	private PrepaidCategoryInfo wpsCategory;
	private int priority;
	private int maxConsActDays;
	private String WPSMapppedKBSocCode;
	//private boolean prepaidLBM;
	private boolean mandatory = false;
	private String wpsServiceType = "";
	private boolean isPromoValidationEligible;
	private boolean inPDAMandatoryGroup;
	private boolean inRIMMandatoryGroup;
	private HashMap serviceEquipmentTypeMap = new HashMap(5);
	private FundSource [] allowedRenewalFundSourceArray = new FundSourceInfo [0]; 
	private FundSource[] allowedPurchaseFundSourceArray = new FundSourceInfo [0]; 
	private String billCycleTreatmentCode;
	private int brandId = Brand.BRAND_ID_TELUS;
	private List familyTypes = new ArrayList();
	private List dataSharingGroups = new ArrayList();
	private String socServiceType;
	private int durationServiceHours;
	private boolean roamLikeHome = false;
	
	public ServiceInfo() {
	}

	public boolean isLoyaltyAndRetentionService() {
		return loyaltyAndRetentionService;
	}

	public void setLoyaltyAndRetentionService(boolean loyaltyAndRetentionService) {
		this.loyaltyAndRetentionService = loyaltyAndRetentionService;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String newServiceType) {
		serviceType = newServiceType;
	}

	public void setDescription(String newDescription) {
		description = newDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setCode(String newCode) {
		code = Info.padTo(newCode, ' ', 9);
	}

	public String getCode() {
		return code;
	}
	
	/**
	 * Generates service mapping code for in-memory services management.
	 * The code consists of serviceCode + EffectiveDate.
	 * Please make sure this generation is the same through all implementations
	 * of ContractService interface, or come up with a base abstract class to make it uniform.
	 */
	public String getServiceMappingCode() {
		return ClientApiUtils.getContractServiceMappingKey(getCode(), getEffectiveDate());
	}

	public void setProductType(String newProductType) {
		productType = newProductType;
	}

	public String getProductType() {
		return productType;
	}

	public int getFeatureCount() {
		return ratedFeatures.size();
	}

	public void setFeatures(RatedFeatureInfo[] newRatedFeatures) {
		ratedFeatures.clear();
		for (int i = 0; i < newRatedFeatures.length; i++) {
			ratedFeatures.put(newRatedFeatures[i].getCode(), newRatedFeatures[i]);
		}
	}

	public RatedFeature[] getFeatures() {
		return getFeatures0();
	}

	public RatedFeatureInfo[] getFeatures0() {
		return (RatedFeatureInfo[]) ratedFeatures.values().toArray(new RatedFeatureInfo[ratedFeatures.size()]);
	}

	public boolean isParameterRequired() {
		RatedFeatureInfo[] features = getFeatures0();
		for (int i = 0; i < features.length; i++) {
			if (features[i].isParameterRequired()) {
				return true;
			}
		}
		return false;
	}

	public boolean isAdditionalNumberRequired() {
		RatedFeatureInfo[] features = getFeatures0();
		for (int i = 0; i < features.length; i++) {
			if (features[i].isAdditionalNumberRequired()) {
				return true;
			}
		}
		return false;
	}

	public void setDescriptionFrench(String newDescriptionFrench) {
		descriptionFrench = newDescriptionFrench;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public RatedFeature getFeature(String pFeature) throws UnknownObjectException {
		return getFeature0(pFeature);
	}

	public RatedFeatureInfo getFeature0(String pFeature) throws UnknownObjectException {
		pFeature = Info.padTo(pFeature, ' ', 6);
		RatedFeatureInfo info = (RatedFeatureInfo) ratedFeatures.get(pFeature);
		if (info != null) {
			return info;
		}
		throw new UnknownObjectException("Feature code=[" + pFeature + "]" + " is not associated with Service code=[" + getCode() + "]");		
	}

	public void setRecurringCharge(double newRecurringCharge) {
		recurringCharge = newRecurringCharge;
	}

	public double getRecurringCharge() {
		return recurringCharge;
	}

	public double getAdditionalCharge() {
		return additionalCharge;
	}

	public void setAdditionalCharge(double additionalCharge) {
		this.additionalCharge = additionalCharge;
	}
	
	public void setRecurringChargeFrequency(int newRecurringChargeFrequency) {
		recurringChargeFrequency = newRecurringChargeFrequency;
	}

	public int getRecurringChargeFrequency() {
		return recurringChargeFrequency;
	}

	public void setDealerActivation(boolean newDealerActivation) {
		dealerActivation = newDealerActivation;
	}

	public boolean isDealerActivation() {
		return dealerActivation;
	}

	public void setClientActivation(boolean newClientActivation) {
		clientActivation = newClientActivation;
	}

	public boolean isClientActivation() {
		return clientActivation;
	}

	/**
	 * @deprecated TO DO: Remove this method. This method exists for backward
	 *             compatibility only.
	 */
	public String[] getEquipmentTypes() {
		String[] tempEquipmentTypes = getEquipmentTypes(NetworkType.NETWORK_TYPE_ALL);

		if (tempEquipmentTypes.length == 0) {
			tempEquipmentTypes = getEquipmentTypes(NetworkType.NETWORK_TYPE_IDEN);
		}

		return tempEquipmentTypes;
	}

	public String[] getEquipmentTypes(String networkType) {
		ServiceEquipmentTypeInfo info = getServiceEquipmentTypeInfo(networkType);
		if (info != null) {
			return (String[]) info.getEquipmentTypes().toArray(new String[info.getEquipmentTypes().size()]);
		}

		return new String[0];
	}

	public void setTermMonths(int newTermMonths) {
		termMonths = newTermMonths;
	}

	public int getTermMonths() {
		return termMonths;
	}

	public void setBillingZeroChrgSuppress(boolean newBillingZeroChrgSuppress) {
		billingZeroChrgSuppress = newBillingZeroChrgSuppress;
	}

	public boolean isBillingZeroChrgSuppress() {
		return billingZeroChrgSuppress;
	}

	public boolean hasAlternateRecurringCharge() {
		return hasAlternateRecurringCharge;
	}

	public void setHasAlternateRecurringCharge(boolean hasAlternateRecurringCharge) {
		this.hasAlternateRecurringCharge = hasAlternateRecurringCharge;
	}

	public double getAlternateRecurringCharge(Subscriber subscriber) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Service getService() {
		// throw new
		// UnsupportedOperationException("Method not implemented here");
		// This is a hack to allow the API to return the Info or Provider object
		// interchangebly.
		return this;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("ServiceInfo:[\n");
		s.append("    serviceType=[").append(serviceType).append("]\n");
		s.append("    code=[").append(code).append("]\n");
		s.append("    brandId=[").append(brandId).append("]\n");
		s.append("    billCycleTreatmentCode=[").append(getBillCycleTreatmentCode()).append("]\n");
		s.append("    description=[").append(description).append("]\n");
		s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");
		// s.append("    ratedFeatures=[").append(ratedFeatures).append("]\n");
		s.append("    recurringCharge=[").append(recurringCharge).append("]\n");
		s.append("    recurringChargeFrequency=[").append(recurringChargeFrequency).append("]\n");
		s.append("    dealerActivation=[").append(dealerActivation).append("]\n");
		s.append("    clientActivation=[").append(clientActivation).append("]\n");
		// s.append("    equipmentTypes=[").append(equipmentTypes[0]).append("]\n");
		s.append("    termMonths=[").append(termMonths).append("]\n");
		s.append("    billingZeroChrgSuppress=[").append(billingZeroChrgSuppress).append("]\n");
		s.append("    wirelessWebFeaturesIncluded=[").append(wirelessWebFeaturesIncluded).append("]\n");
		s.append("    telephonyFeaturesIncluded=[").append(telephonyFeaturesIncluded).append("]\n");
		s.append("    dispatchFeaturesIncluded=[").append(dispatchFeaturesIncluded).append("]\n");
		s.append("    active=[").append(active).append("]\n");
		s.append("    current=[").append(current).append("]\n");
		s.append("    forSale=[").append(forSale).append("]\n");
		s.append("    effectiveDate=[").append(effectiveDate).append("]\n");
		s.append("    expiryDate=[").append(expiryDate).append("]\n");
		s.append("    discountAvailable=[").append(discountAvailable).append("]\n");
		s.append("    term=[").append(term).append("]\n");
		s.append("    termUnits=[").append(termUnits).append("]\n");
		s.append("    wps=[").append(wps).append("]\n");
		s.append("    knowbility=[").append(knowbility).append("]\n");
		s.append("    includedPromotion=[").append(includedPromotion).append("]\n");
		s.append("    boundService=[").append(boundService).append("]\n");
		s.append("    promotionAttached=[").append(promotionAttached).append("]\n");
		s.append("    boundServiceAttached=[").append(boundServiceAttached).append("]\n");
		s.append("    usageRatingFrequency=[").append(usageRatingFrequency).append("]\n");
		s.append("    sequentiallyBoundService=[").append(sequentiallyBoundService).append("]\n");
		s.append("    sequentiallyBoundServiceAttached=[").append(sequentiallyBoundServiceAttached).append("]\n");
		s.append("    sharable=[").append(isSharable()).append("]\n");
		s.append("    maxTerm=[").append(maxTerm).append("]\n");
		s.append("    autoRenewalAllowed=[").append(autoRenewalAllowed).append("]\n");
		s.append("    periodCode=[").append(periodCode).append("]\n");
		s.append("    forcedAutoRenew=[").append(forcedAutoRenew).append("]\n");

		if (ratedFeatures == null) {
			s.append("    ratedFeatures=[null]\n");
		} else if (ratedFeatures.size() == 0) {
			s.append("    ratedFeatures={}\n");
		} else {
			Object[] key = ratedFeatures.keySet().toArray();
			for (int i = 0; i < key.length; i++) {
				s.append("    ratedFeatures[").append(i).append("]=[").append(key[i]).append("]\n");
			}
			/*
			 * Iterator i = ratedFeatures.values().iterator(); while
			 * (i.hasNext()){
			 * s.append("    ratedFeatures["+i+"]=[").append(i.next
			 * ()).append("]\n"); }
			 */
		}
		s.append("    hasAlternateRecurringCharge=[").append(hasAlternateRecurringCharge).append("]\n");
		s.append("    emailAndWebspaceIncluded=[").append(emailAndWebspaceIncluded).append("]\n");
		s.append("    LBSTracker=[").append(LBSTracker).append("]\n");
		s.append("    LBSTrackee=[").append(LBSTrackee).append("]\n");
		s.append("    maxConsActDays=[").append(maxConsActDays).append("]\n");
		s.append("    priority=[").append(priority).append("]\n");
		if (wpsCategory != null) {
			s.append("    category=[").append(wpsCategory.getCode()).append(",").append(wpsCategory.getDescription()).append("]\n");
		}
		s.append("    serviceEquipment=[").append(getServiceEquipmentTypeInfo()).append("]\n");
		s.append("    familyTypes=").append(familyTypes).append("\n");
		s.append("    dataSharingGroup=").append(dataSharingGroups).append("\n");
		s.append("    socServiceType=[").append(socServiceType).append("]\n");
		s.append("]");

		return s.toString();
	}

	/*
	 * public void setPromotion(boolean newPromotion) { promotion =
	 * newPromotion; }
	 */

	public boolean isPromotion() {

		return (this.getServiceType().equals("S") ? true : false);

	}

	public boolean hasPromotion() {

		return this.promotionAttached;

	}

	public boolean hasEquivalentService() {

		return this.equivalentServiceExists;

	}

	public boolean hasBoundService() {

		return this.boundServiceAttached;

	}

	/*
	 * public void setPromotionTerm(com.telus.eas.utility.info.PromoTermInfo
	 * promotionTerm) { this.promotionTerm = promotionTerm; } public PromoTerm
	 * getPromotionTerm() { return promotionTerm; }
	 * 
	 * public PromoTermInfo getPromotionTerm0() { return promotionTerm; }
	 */

	public void setWirelessWebFeaturesIncluded(boolean wirelessWebFeaturesIncluded) {
		this.wirelessWebFeaturesIncluded = wirelessWebFeaturesIncluded;
	}

	public boolean isWirelessWebFeaturesIncluded() {
		return wirelessWebFeaturesIncluded;
	}

	public void setTelephonyFeaturesIncluded(boolean telephonyFeaturesIncluded) {
		this.telephonyFeaturesIncluded = telephonyFeaturesIncluded;
	}

	public boolean isTelephonyFeaturesIncluded() {
		return telephonyFeaturesIncluded;
	}

	public void setDispatchFeaturesIncluded(boolean dispatchFeaturesIncluded) {
		this.dispatchFeaturesIncluded = dispatchFeaturesIncluded;
	}

	public boolean isDispatchFeaturesIncluded() {
		return dispatchFeaturesIncluded;
	}

	public boolean isAvailable() {
		return (active && forSale && current && (expiryDate == null || expiryDate.after(new Date())) && (effectiveDate.before(new Date())));
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isRIM() {
		return rim;
	}

	public boolean isEmailAndWebspaceIncluded() {
		if (serviceType.equals(SERVICE_TYPE_CODE_PRICE_PLAN)) {
			return emailAndWebspaceIncluded;
		} else if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		} else {
			String code = null;
			for (int i = 0; i < categoryCodes.length; i++) {
				code = categoryCodes[i];
				if (Feature.CATEGORY_CODE_EMAIL.equals(code) || Feature.CATEGORY_CODE_PDA.equals(code) || Feature.CATEGORY_CODE_RIM.equals(code)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isEvDO() {
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (Feature.CATEGORY_CODE_EVDO.equals(code)) {
				return true;
			}
		}

		return false;
	}

	public boolean isActive() {
		return active;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	public boolean isForSale() {
		return forSale;
	}

	public void setEffectiveDate(java.util.Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public java.util.Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setExpiryDate(java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public java.util.Date getExpiryDate() {
		return expiryDate;
	}

	public void setDiscountAvailable(boolean discountAvailable) {
		this.discountAvailable = discountAvailable;
	}

	public boolean isDiscountAvailable() {
		return discountAvailable;
	}

	public boolean containsFeature(String code) {
		code = Info.padTo(code, ' ', 6);
		return ratedFeatures.containsKey(code);
	}

	public boolean containsSwitchCode(String switchCode) {
		if (ratedFeatures == null) {
			throw new NullPointerException("ratedFeatures == null");
		}

		switchCode = switchCode.trim();
		RatedFeature[] features = getFeatures0();
		for (int i = 0; i < features.length; i++) {
			String s = features[i].getSwitchCode();
			if (s != null && s.trim().equalsIgnoreCase(switchCode)) {
				return true;
			}
		}

		return false;
	}

	public ServiceRelation[] getRelations(Contract contract) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public ServiceRelation[] getRelations(Contract contract, String relationType) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public ServiceRelation[] getRelations(PricePlan pricePlan) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public ServiceRelation[] getRelations(PricePlan pricePlan, String relationType) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public int getTerm() {
		return term;
	}

	public String getTermUnits() {
		return termUnits;
	}

	public boolean isWPS() {
		return wps;
	}

	public boolean isKnowbility() {
		return knowbility;
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public void setTermUnits(String termUnits) {
		this.termUnits = termUnits;
	}

	public void setWPS(boolean wps) {
		this.wps = wps;
	}

	public void setRIM(boolean rim) {
		this.rim = rim;
	}

	public void setKnowbility(boolean knowbility) {
		this.knowbility = knowbility;
	}

	public void setIncludedPromotion(boolean includedPromotion) {
		this.includedPromotion = includedPromotion;
	}

	public boolean isIncludedPromotion() {
		return includedPromotion;
	}

	public void setBoundService(boolean boundService) {
		this.boundService = boundService;
	}

	public boolean isBoundService() {
		return boundService;
	}

	public void setPromotionAttached(boolean promotionAttached) {
		this.promotionAttached = promotionAttached;
	}

	public void setBoundServiceAttached(boolean boundServiceAttached) {
		this.boundServiceAttached = boundServiceAttached;
	}

	public Service getEquivalentService(PricePlan pricePlan) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public Service getEquivalentService(PricePlan pricePlan, String networkType) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public int getUsageRatingFrequency() {
		return usageRatingFrequency;
	}

	public void setUsageRatingFrequency(int usageRatingFrequency) {
		this.usageRatingFrequency = usageRatingFrequency;
	}

	public boolean isSequentiallyBoundService() {
		return sequentiallyBoundService;
	}

	public void setSequentiallyBoundService(boolean sequentiallyBoundService) {
		this.sequentiallyBoundService = sequentiallyBoundService;
	}

	public boolean hasSequentiallyBoundService() {
		return sequentiallyBoundServiceAttached;
	}

	public void setSequentiallyBoundServiceAttached(boolean sequentiallyBoundServiceAttached) {
		this.sequentiallyBoundServiceAttached = sequentiallyBoundServiceAttached;
	}

	public void setEquivalentServiceExists(boolean newEquivalentServiceExists) {
		this.equivalentServiceExists = newEquivalentServiceExists;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public boolean isSharable() {
		return (this.getLevelCode() == null ? false : this.getLevelCode().equals(Service.LEVEL_CODE_PRODUCT) ? true : false);
	}

	public boolean isGrandFathered() {
		// return expiryDate != null && expiryDate.before(currentDate);
		return expiryDate != null && expiryDate.getTime() < System.currentTimeMillis();
	}

	public boolean getRemoveOnPriceplanChange() {
		return removeOnPriceplanChange;
	}

	public void setRemoveOnPriceplanChange(boolean removeOnPriceplanChange) {
		this.removeOnPriceplanChange = removeOnPriceplanChange;
	}

	public boolean isAutoRenewalAllowed() {
		return autoRenewalAllowed;
	}

	public int getMaxTerm() {
		return maxTerm;
	}

	public void setMaxTerm(int maxTerm) {
		this.maxTerm = maxTerm;
	}

	public void setAutoRenewalAllowed(boolean autoRenewalAllowed) {
		this.autoRenewalAllowed = autoRenewalAllowed;
	}

	/**
	 *@deprecated As of February release, 2011, 
	 * replaced by <code>retainByPrivilege(ServiceSummary[] services, String businessRoleCode, 
	 * 		String privilegeCode)</code>
	 * This method will be removed by CASPER project - Planned for May 2011 release  
	 */
	public boolean containsPrivilege(String businessRole, String privilege) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public String[] getProvinces() {
		throw new UnsupportedOperationException("method not implemented here");

	}

	public boolean isCrossFleetRestricted() {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	public String getPeriodCode() {
		return periodCode;
	}

	public void setCategoryCodes(String[] categoryCodes) {
		this.categoryCodes = categoryCodes;
	}

	public String[] getCategoryCodes() {
		return categoryCodes;
	}

	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}

	public String getCoverageType() {
		return coverageType;
	}

	public void setUserSegment(String userSegment) {
		this.userSegment = userSegment;
	}

	public String getUserSegment() {
		return userSegment;
	}

	public String[] getFamilyGroupCodes(String familyGroupType) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public ServicePeriod[] getServicePeriods() throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public boolean isAssociatedIncludedPromotion(String pricePlanCode, int term) throws TelusAPIException {
		throw new UnsupportedOperationException("method not implemented here");
	}

	public boolean isPTT() {
		if (serviceType.equals(SERVICE_TYPE_CODE_PRICE_PLAN)) {
			return ptt;
		} else {
			if (categoryCodes == null || categoryCodes.length == 0)
				return false;

			for (int i = 0; i < categoryCodes.length; i++) {
				if (FeatureInfo.FEATURE_CATEGORY_PTT.equals(categoryCodes[i]))
					return true;
			}

			return false;
		}
	}

	public boolean isPTTService() {

		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.FEATURE_CATEGORY_PTT.equals(code)) {
				return true;
			}
		}

		return false;

	}

	public void setMinimumUsageCharge(double minimumUsageCharge) {
		this.minimumUsageCharge = minimumUsageCharge;
	}

	public double getMinimumUsageCharge() {
		return minimumUsageCharge;
	}

	public boolean isSMSNotification() {

		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.FEATURE_CATEGORY_SMS.equals(code)) {
				return true;
			}
		}

		return false;

	}

	public void setPTT(boolean ptt) {
		this.ptt = ptt;
	}

	// private static Set hotspotCategoryCodes = new HashSet();
	// static {
	//
	// // 1 feature -> 1 category code
	// hotspotCategoryCodes.add("WIFI"); // PCS WiFi category code
	// hotspotCategoryCodes.add("MWIFI"); // Mike WiFi category code
	//
	// // Hotspot Features:
	// // UNQWF - PCS Feature
	// // MUNQWF - Mike Feature
	// }

	/**
	 * To be tested when data is ready ... WiFi is for March 2005 release
	 * 
	 * @return boolean
	 */
	public boolean isWiFi() {
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			// FEATURE_CATEGORY_WF
			if (FeatureInfo.FEATURE_CATEGORY_WF.equalsIgnoreCase(code)) {
				return true;
			}
		}

		return false;
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
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_MMS.equals(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true if this is a Java Download Service.
	 * 
	 * @return boolean
	 */
	// public boolean isJavaDownload() {
	// if (categoryCodes == null || categoryCodes.length == 0) {
	//
	// return false;
	// }
	//
	// String code = null;
	// for (int i=0; i<categoryCodes.length; i++) {
	// code = categoryCodes[i];
	// if (FeatureInfo.CATEGORY_CODE_JAVADOWNLOAD.equals(code)) {
	// return true;
	// }
	// }
	//
	// return false;
	//
	// }

	/**
	 * Return true if this is a Mobile Originated Short Message Service (MOSMS).
	 * 
	 * @return boolean
	 */
	public boolean isMOSMS() {
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_MOSMS.equals(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Original design: isPDA() refers to SOC that's configured for PDA
	 * equipment type ONLY. New to Holborn: Holborn opens up SOCs to other
	 * equipment types so it can be subscribed regardless to the equipment type.
	 * The way to identify rather a SOC was created for PDA only is to check (i)
	 * if the SOC belongs to SOC GROUP MPDA (mandatory PDA SOCs) OR (ii)
	 * Provisioning switch codes 'VEMAIL' or any other new PDA provisioning
	 * codes that business might introduced in the future.
	 */
	public boolean isPDA() {
		return (inPDAMandatoryGroup || containsSwitchCode("VEMAIL"));
		/*
		 * String[] equipTypes = getEquipmentTypes(); return ((equipTypes.length
		 * == 1 && equipTypes[0].equals(Equipment.EQUIPMENT_TYPE_PDA)) ? true :
		 * false);
		 */
		/*
		 * commented for mandatory services if (categoryCodes == null ||
		 * categoryCodes.length == 0) {
		 * 
		 * return false; }
		 * 
		 * String code = null; for (int i=0; i<categoryCodes.length; i++) { code
		 * = categoryCodes[i]; if
		 * (FeatureInfo.CATEGORY_CODE_PDA.equalsIgnoreCase(code)) { return true;
		 * } }
		 * 
		 * return false;
		 */

	}

	/**
	 * Life style bundles - Surf SOC's
	 * 
	 * @return boolean
	 */
	public boolean isDowngradable() {
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_WEB_WIRELESS.equalsIgnoreCase(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return Returns the flexibleEvenings.
	 */
	public boolean isFlexibleEvenings() {

		RatedFeature[] features = getFeatures();

		if (features == null || features.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < features.length; i++) {
			code = features[i].getCode().trim();
			if (SERVICE_FEATURE_FLEX_EVENINGS_PCS.equalsIgnoreCase(code) || SERVICE_FEATURE_FLEX_EVENINGS_MIKE.equalsIgnoreCase(code))
				return true;

		}

		return false;

	}

	public void setEmailAndWebspaceIncluded(boolean emailAndWebspaceIncluded) {
		this.emailAndWebspaceIncluded = emailAndWebspaceIncluded;
	}

	/**
	 * Return true if this is a RUIM Service.
	 * 
	 * @return boolean
	 * 
	 *         Should call isInternationalRoaming() instead.
	 * 
	 * @deprecated
	 */
	public boolean isRUIM() {
		if (categoryCodes == null || categoryCodes.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_RUIM.equals(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true if this is a International Calling Service.
	 * 
	 * @return boolean
	 */
	public boolean isInternationalCalling() {
		if (categoryCodes == null || categoryCodes.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];

			if (FeatureInfo.CATEGORY_CODE_INTERNATIONAL_CALLING.equals(code)) {
				return true;
			}

			if (FeatureInfo.CATEGORY_CODE_INTERNATIONAL_CALLING_KOODO.equals(code)) {
				return true;
			}

			if (FeatureInfo.CATEGORY_CODE_INTERNATIONAL_CALLING_MIKE.equals(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true if this is a International Roaming Service.
	 * 
	 * @return boolean
	 */
	public boolean isInternationalRoaming() {
		if (categoryCodes == null || categoryCodes.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];

			if (FeatureInfo.CATEGORY_CODE_RUIM.equals(code)) {
				return true;
			}

			if (FeatureInfo.CATEGORY_CODE_INTERNATIONAL_ROAMING_KOODO.equals(code)) {
				return true;
			}

		}

		return false;
	}

	/**
	 * @return Returns the visto.
	 */
	public boolean isVisto() {
		if (categoryCodes == null || categoryCodes.length == 0) {

			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_VISTO.equalsIgnoreCase(code)) {
				return true;
			}
		}

		return false;
	}

	public boolean is911() {

		if (categoryCodes == null || categoryCodes.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (Feature.CATEGORY_CODE_TELUS_911.equals(code) || Feature.CATEGORY_CODE_KOODO_911.equals(code) || Feature.CATEGORY_CODE_WALMART_911.equals(code)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if it's LBS tracker
	 * 
	 * @return boolean
	 */
	public boolean isLBSTracker() {

		return LBSTracker;
	}

	/**
	 * Returns true if it's LBS trackee
	 * 
	 * @return boolean
	 */
	public boolean isLBSTrackee() {
		return LBSTrackee;
	}

	/**
	 * Returns true, if CategoryCode = CATEGORY_CODE_MS_BASED
	 * 
	 * @return boolean
	 */
	public boolean isMSBasedCapabilityRequired() {
		if (categoryCodes == null || categoryCodes.length == 0) {
			return false;
		}

		String code = null;
		for (int i = 0; i < categoryCodes.length; i++) {
			code = categoryCodes[i];
			if (FeatureInfo.CATEGORY_CODE_LBS_MS_BASED_UPLANE.equals(code)) {
				return true;
			}
		}

		return false;
	}

	public boolean isNonCurrent() {
		return !current && !isGrandFathered();
	}

	/**
	 * @param LBSTrackee
	 *            The isLBSTrackee to set.
	 */
	public void setLBSTrackee(boolean LBSTrackee) {
		this.LBSTrackee = LBSTrackee;
	}

	/**
	 * @param LBSTracker
	 *            The isLBSTracker to set.
	 */
	public void setLBSTracker(boolean LBSTracker) {
		this.LBSTracker = LBSTracker;
	}

	public boolean containsCategory(String category) {
		if (category == null || category.length() == 0)
			throw new IllegalArgumentException("Parameter category cannot be NULL or empty...");

		if (categoryCodes == null)
			return false;

		for (int i = 0; i < categoryCodes.length; i++)
			if (categoryCodes[i].equals(category))
				return true;

		return false;
	}

	public boolean hasCallHomeFree() {

		if (!isWPS()) {
			return containsCategory(Feature.CATEGORY_CODE_CALL_HOME_FREE);
		} else {
			return false;
		}

	}

	public void setForcedAutoRenew(boolean forcedRenew) {
		this.forcedAutoRenew = forcedRenew;
	}

	public boolean isForcedAutoRenew() {
		return forcedAutoRenew;
	}

	public boolean hasCallingCircleFeatures() {

		if (containsCategory(Feature.CATEGORY_CODE_CALLING_CIRCLE)) {
			return true;
		} else if (isWPS() && containsCategory(Feature.CATEGORY_CODE_CALL_HOME_FREE))
			return true;
		else
			return false;

	}

	public boolean hasVoiceToTextFeature() {

		return containsCategory(Feature.CATEGORY_CODE_VOICE2TEXT);
	}

	public boolean isMandatory() {

		return mandatory;
	}

	public void setMandatory(boolean pMandatory) {
		mandatory = pMandatory;
	}

	public PrepaidCategory getCategory() {
		return wpsCategory;
	}

	public void setCategory(PrepaidCategoryInfo prepaidCategory) {
		wpsCategory = prepaidCategory;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int p) {
		priority = p;
	}

	public int getMaxConsActDays() {
		return maxConsActDays;
	}

	public void setMaxConsActDays(int maxActDays) {
		maxConsActDays = maxActDays;
	}

	public String getWPSMappedKBSocCode() {
		return WPSMapppedKBSocCode;
	}

	public void setWPSMapppedKBSocCode(String aCode) {
		WPSMapppedKBSocCode = aCode;
	}

	public boolean isPrepaidLBM() {
		return (WPS_SERVICE_TYPE_PROMOTIONAL.equals(wpsServiceType) || WPS_SERVICE_TYPE_TRACKING.equals(wpsServiceType));
	}

	public boolean isPrepaidCallingCircle() {

		return isWPS() && wpsCategory != null && hasCallingCircleFeatures();
	}

	public void setWPSServiceType(String serviceType) {
		wpsServiceType = serviceType;
	}

	public String getWPSServiceType() {
		return wpsServiceType;
	}

	public boolean isPromoValidationEligible() {
		return isPromoValidationEligible;
	}

	public void setPromoValidationEligible(boolean val) {
		isPromoValidationEligible = val;
	}

	/**
	 * @deprecated TO DO: Remove this method. It exists for backward
	 *             compatibility only.
	 */
	public String getNetworkType() {
		ServiceEquipmentTypeInfo info;

		info = getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_ALL);
		if (info != null) {// retrieve equipment info for PCS networks
			return info.getNetworkType(); // return C, H or 9 if such info is
											// available
		} else {
			return NetworkType.NETWORK_TYPE_IDEN; // otherwise, it is IDEN
		}
	}

	public String getNetworkType(String equipmentType) {
		ArrayList networks = new ArrayList();
		ServiceEquipmentTypeInfo info;

		info = getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_IDEN);
		if (info != null) {
			if (info.getEquipmentTypes().contains(equipmentType) || info.getEquipmentTypes().contains(Equipment.EQUIPMENT_TYPE_ALL)
					|| Equipment.EQUIPMENT_TYPE_ALL.equals(equipmentType)) {
				return info.getNetworkType();
			} else {
				return "";
			}
		} else {
			for (int i = 0; i < NetworkType.NETWORK_TYPE_ALL_LIST.length; i++) {
				info = getServiceEquipmentTypeInfo(NetworkType.NETWORK_TYPE_ALL_LIST[i]);
				if (info != null) {
					if (info.getEquipmentTypes().contains(equipmentType) || info.getEquipmentTypes().contains(Equipment.EQUIPMENT_TYPE_ALL)
							|| Equipment.EQUIPMENT_TYPE_ALL.equals(equipmentType)) {
						networks.add(info.getNetworkType());
					}
				}
			}
			if (networks.size() == 0) {
				return "";
			} else if (networks.size() == NetworkType.NETWORK_TYPE_ALL_LIST.length) {
				return NetworkType.NETWORK_TYPE_ALL;
			} else {
				return (String) networks.get(0); // return the first element
													// only. Unexpected result
													// if networks.size() > 1
													// and condition reaches
													// here
			}
		}
	}

	public String[] getAllNetworkTypes() {
		return (String[]) serviceEquipmentTypeMap.keySet().toArray(new String[serviceEquipmentTypeMap.keySet().size()]);
	}

	public void setPDAMandatoryGroup(boolean pda_ind) {
		inPDAMandatoryGroup = pda_ind;
	}

	public boolean isInPDAMandatoryGroup() {
		return inPDAMandatoryGroup;
	}

	public void setRIMMandatoryGroup(boolean rim_ind) {
		inRIMMandatoryGroup = rim_ind;
	}

	public boolean isInRIMMandatoryGroup() {
		return inRIMMandatoryGroup;
	}

	public void setServiceEquipmentRelationship(ServiceEquipmentTypeInfo[] equipmentTypes) {
		for (int idx = 0; idx < equipmentTypes.length; idx++) {
			addServiecEquipmentTypeInfo(equipmentTypes[idx]);
		}
	}
	
	public void setServiceEquipmentRelationship(HashMap equpimentNetworkMap) {
		this.serviceEquipmentTypeMap = equpimentNetworkMap;
	}

	public HashMap getServiceEquipmentTypeInfo() {
		return serviceEquipmentTypeMap;
	}

	public ServiceEquipmentTypeInfo getServiceEquipmentTypeInfo(String networkType) {
		if (NetworkType.NETWORK_TYPE_ALL.equals(networkType)) {
			ServiceEquipmentTypeInfo equipNetworkInfo = null;

			for (int i = 0; i < NetworkType.NETWORK_TYPE_ALL_LIST.length; i++) {
				ServiceEquipmentTypeInfo tempInfo = (ServiceEquipmentTypeInfo) serviceEquipmentTypeMap.get(NetworkType.NETWORK_TYPE_ALL_LIST[i]);
				if (tempInfo != null) {
					if (equipNetworkInfo == null) {
						equipNetworkInfo = tempInfo;
					} else {
						equipNetworkInfo = equipNetworkInfo.merge(tempInfo);
					}
				}
			}

			return equipNetworkInfo;
		} else {
			return (ServiceEquipmentTypeInfo) serviceEquipmentTypeMap.get(networkType);
		}
	}

	/**
	 * Add a ServiceEquipmentTypeInfo object to hash map. The network type is
	 * used as the key
	 * 
	 * @param info
	 */
	public void addServiecEquipmentTypeInfo(ServiceEquipmentTypeInfo info) {
		if (info != null && info.getNetworkType() != null && !"".equals(info.getNetworkType().trim())) {
			serviceEquipmentTypeMap.put(info.getNetworkType(), info);
		}
	}

	public boolean isCompatible(String networkType) {

		boolean result = false;
		result = serviceEquipmentTypeMap.containsKey(networkType);
		return result;
	}

	public boolean isCompatible(String networkType, String equipmentType) {
		boolean result = (testCompatibility(networkType, equipmentType) == 0) ? true : false;
		return result;
	}

	public boolean isNetworkEquipmentTypeCompatible(Equipment equipment) {
		boolean result = (testNetworkEquipmentTypeCompatibility(equipment) == 0) ? true : false;
		return result;
	}

	/**
	 * Evaluate if this service is compatible with the given networkType and
	 * equipmentType. Depends on the service type sometimes we only evaluate the
	 * networkType, and sometimes we evaluate both networkType and
	 * equipmentType.
	 * 
	 * Return 0 if it's compatible Return
	 * InvalidServiceException.NETWORK_MISMATCH if the networkType is not match
	 * Return InvalidServiceException.TECHNOLOGY_MISMATCH if the networkType
	 * matches, but the equipmentType is not match.
	 * 
	 * @param networkType
	 * @param equipmentType
	 * @return int
	 */
	public int testCompatibility(String networkType, String equipmentType) {
		int result = 0;

		if (SERVICE_TYPE_CODE_OPTIONAL_SOC.equals(getServiceType())) {
			// KB term optional SOC == included service
			// only check network compatibility for included service
			result = isCompatible(networkType) ? 0 : InvalidServiceException.NETWORK_MISMATCH;
		} else {
			ServiceEquipmentTypeInfo info = (ServiceEquipmentTypeInfo) serviceEquipmentTypeMap.get(networkType);
			if (info != null) {
				List equipTypeList = info.getEquipmentTypes();
				if ((equipTypeList.contains(Equipment.EQUIPMENT_TYPE_ALL) || equipTypeList.contains(equipmentType))) {
					result = 0; // both network_type and equipment_type are
								// match
				} else {
					// network_type match, but equipment_type mismatch
					result = InvalidServiceException.TECHNOLOGY_MISMATCH;
				}
			} else {
				result = InvalidServiceException.NETWORK_MISMATCH;
			}
		}
		return result;
	}

	/**
	 * Evaluate if this service is compatible with the given equipment's
	 * networkType/equipmentType. Depends on the service type and given
	 * equipment's networkType, sometimes we only evaluate the networkType, and
	 * sometime we evaluate both networkType and equipmentType
	 * 
	 * Return 0 if it's compatible Return
	 * InvalidServiceException.NETWORK_MISMATCH if the networkType is not match
	 * Return InvalidServiceException.TECHNOLOGY_MISMATCH if the networkType
	 * matches, but the equipmentType not match.
	 * 
	 * @param equipment
	 * @return int
	 */
	public int testNetworkEquipmentTypeCompatibility(Equipment equipment) {
		int result = 0;
		try {
			if (equipment.isHSPA()) {
				// we don't care about the equipment type as long as the SOC
				// supports HSPA in one of the equipment types
				result = isCompatible(equipment.getNetworkType()) ? 0 : InvalidServiceException.NETWORK_MISMATCH;
			} else {
				result = testCompatibility(equipment.getNetworkType(), equipment.getEquipmentType());
			}
		} catch (TelusAPIException e) {
			// if we reach here, that means we can't get network type for the
			// equipment, so tread it as Network Mismatch
			result = InvalidServiceException.NETWORK_MISMATCH;
		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.api.reference.ServiceSummary#getAllowedRenewalFundSourceArray()
	 */
	public FundSource[] getAllowedRenewalFundSourceArray() {
		return allowedRenewalFundSourceArray;
	}

	/**
	 * @param allowedRenewalFundSourceArray the allowedRenewalFundSourceArray to set
	 */
	public void setAllowedRenewalFundSourceArray(FundSource[] allowedRenewalFundSourceArray) {
		this.allowedRenewalFundSourceArray = allowedRenewalFundSourceArray;
	}
	
	/**
	 * Returns true if this SOC is a device protection SOC. This is determined
	 * by any of the features belong to the corresponding feature category.
	 * 
	 * @return boolean
	 * @deprecated should call SCOTT for this information
	 */
	public boolean isDeviceProtection() {
		return containsCategory(Feature.CATEGORY_CODE_DEVICE_PROTECTION);
	}	
	
	public boolean hasRIMAPN() {
		return containsFeature(Feature.FEATURE_CODE_RIMAPN);
	}
	
	public boolean hasMBAPN() {
		return containsFeature(Feature.FEATURE_CODE_MBAPN);
	}
	
	/**
	 * Retrieves the value from BILL_CYCLE_TREATMENT_CD column in KB?s SOC table
	 *
	 * Valid values are: 
	 * BCIC ? indicates that the Service or PricePlan is BCIC based
	 * Blank/null -  indicates that the Service or PricPlan is  not BCIC based
	 * 
	 * @return Bill cycle treatment code
	*/
	
	public String getBillCycleTreatmentCode() {
		return billCycleTreatmentCode;
	}

	public void setBillCycleTreatmentCode(String billCycleTreatmentCode) {
		this.billCycleTreatmentCode = billCycleTreatmentCode;
	}

	public FundSource[] getAllowedPurchaseFundSourceArray() {
		return allowedPurchaseFundSourceArray;
	}

	public void setAllowedPurchaseFundSourceArray(
			FundSource[] allowedPurchaseFundSourceArray) {
		this.allowedPurchaseFundSourceArray = allowedPurchaseFundSourceArray;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		if ( brandId > 0 )
			this.brandId = brandId; 
	}	
	
	public String[] getFamilyTypes() {
		return (String[]) familyTypes.toArray(new String[familyTypes.size()]);
	}
	
	public boolean belongsToFamilyType( String familyType ) {
		if (familyType == null || familyType.length() == 0)
			throw new IllegalArgumentException("Parameter familyType cannot be NULL or empty...");
		
		return familyTypes.contains(familyType);
	}
	
	public void addFamilyType( String familyType ) {
		familyTypes.add( familyType );
		populateFamilyTypeDerivedProperties( familyType );
	}
	
	public void setFamilyTypes (List familyTypes ) {
		
		this.familyTypes.clear();
		if (familyTypes != null) {
			for(int i = 0; i < familyTypes.size(); i++) {
				addFamilyType((String) familyTypes.get(i));
			}
		}
	}
	
	protected void populateFamilyTypeDerivedProperties(String familyType) {
		
		if (EQUIVALENT_SOC.equals(familyType)) {
			setEquivalentServiceExists(true);

		} else if (L_N_R_SOC.equals(familyType)){
			setLoyaltyAndRetentionService(true);

		} else if (LBS_TRACKER.equals(familyType)){
			setLBSTracker(true);

		} else if (LBS_TRACKEE.equals(familyType)){
			setLBSTrackee(true);

		} else if (MANDATORY.equals(familyType)){
			setMandatory(true);
			
		} else if (MOBILE_APP.equals(familyType)){
			setPromoValidationEligible(true);
		}
	}

	public ServiceDataSharingGroup[] getDataSharingGroups() {
		return (ServiceDataSharingGroup[]) dataSharingGroups.toArray(new ServiceDataSharingGroup[dataSharingGroups.size()]);
	}
	
	public void setDataSharingGroups(ServiceDataSharingGroup[] groups) {
		this.dataSharingGroups.clear();
		if (groups != null) {
			this.dataSharingGroups.addAll( Arrays.asList(groups));
		}
	}
	
	public void addDataSharingGroup(ServiceDataSharingGroup dataSharingGroup) {
		dataSharingGroups.add(dataSharingGroup);
	}
	
	public String getSocServiceType() {
		return socServiceType;
	}
	
	public void setSocServiceType(String socServiceType) {
		this.socServiceType = socServiceType;
	}

	public boolean isPPSBundle() {
		return familyTypes.contains(FAMILY_TYPE_CODE_PPS_BUNDLE);
	}

	public boolean isPPSAddOn() {
		return familyTypes.contains(FAMILY_TYPE_CODE_PPS_ADDON);
	}

	public boolean isFlexPlan() {
		return familyTypes.contains(FAMILY_TYPE_CODE_FLEX_PLAN);
	}

	public boolean isMandatoryAddOn() {
		return familyTypes.contains(FAMILY_TYPE_CODE_MANDATORY_ADDON);
	}

	public int ppsPriority() {
		int priority = 0;
		RatedFeature[] features = getFeatures();
		if (isPPSBundle() ) {
			for (int i=0; i < features.length; i++) { //RatedFeature rf:getFeatures()) {
				if ((features[i].getCategoryCode() != null) && features[i].getCategoryCode().equals("PRI"))
					priority = Integer.parseInt(features[i].getCode().trim());
			}
		}
		return priority;
	}
	
	
	public int ppsStorage() {
		int storage = 0;
		RatedFeature[] features = getFeatures();
		if (isPPSBundle()) {
			for (int i=0; i < features.length; i++) { //RatedFeature rf:getFeatures()) {
				if ((features[i].getCategoryCode() != null) && features[i].getCategoryCode().equals("PSB"))
					//STB005
					//STB025
					//STB050
					storage = Integer.parseInt(features[i].getCode().substring(3).trim());
			}
		}
		else if (isPPSAddOn()) {
			for (int i=0; i < features.length; i++) { //RatedFeature rf:getFeatures()) {
				if ((features[i].getCategoryCode() != null) && features[i].getCategoryCode().equals("PSO"))
					//STO100
					//STO200
					storage = Integer.parseInt(features[i].getCode().substring(3).trim());
			}
		}
		return storage;
	}
	
	
	public int ppsLicenses() {
		int licenses = 0;
		RatedFeature[] features = getFeatures();
		if (isPPSBundle() ) {
			for (int i=0; i < features.length; i++) { //RatedFeature rf:getFeatures()) {
				if ((features[i].getCategoryCode() != null) && features[i].getCategoryCode().equals("PSE"))
					//SEAT02
					//SEAT05
					//SEAT10
					licenses = Integer.parseInt(features[i].getCode().substring(4).trim());
			}
		}
		return licenses;
	}
	
	public int getDurationServiceHours() {
		return durationServiceHours;
	}
	
	public void setDurationServiceHours(int durationServiceHours) {
		this.durationServiceHours = durationServiceHours;
	}

	public boolean isRLH() {
		return roamLikeHome;
	}

	public void setRLH(boolean roamLikeHome) {
		this.roamLikeHome = roamLikeHome;
	}


}