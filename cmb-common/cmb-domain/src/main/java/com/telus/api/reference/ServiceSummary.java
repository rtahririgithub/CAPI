/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */
package com.telus.api.reference;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Contract;
import com.telus.api.equipment.Equipment;

public interface ServiceSummary extends Reference {

	String BLOCK_INCOMING_CALLS_IDEN = "MSBIC0   ";
	String BLOCK_OUTGOING_CALLS_IDEN = "MSCDOR0  ";
	String BLOCK_OUTGOING_CALLER_ID_PCS = "SCNIR    ";
	String BLOCK_OUTGOING_CALLER_ID_IDEN = "MSCNIR   ";
	String BLOCK_OUTGOING_CALLER_ID_KOODO = "3SPXCIDR ";

	/* Covent phase4 - volte soc */
	String VOLTE_PCS = "SVOLTE";

	String TERM_UNITS_DAYS = "DAY";
	String TERM_UNITS_MONTHS = "MTH";

	String LEVEL_CODE_PRODUCT = "P";
	String LEVEL_CODE_SUBSCRIBER = "C";

	String PRIVILEGE_SERVICE_DISPLAY = "DISPLAY";
	String PRIVILEGE_SERVICE_ACTIVATE = "ACTIVATE";
	String PRIVILEGE_SERVICE_ADD = "ADD";
	String PRIVILEGE_SERVICE_REMOVE = "REMOVE";
	String PRIVILEGE_SERVICE_RETAIN_ON_PRICE_PLAN_CHANGE = "RTNONCHNG";
	String PRIVILEGE_SERVICE_RETAIN_ON_RENEWAL = "RTNONRENEW";
	String PRIVILEGE_AUTOADD = "AUTOADD";
	String PRIVILEGE_SERVICE_ADD_AOM = "ADDAOM";

	String HORIZONTAL_CROSSFLEET_CROSSDAP_ZERO_CHARGE = "MPRIVU0  ";
	String HORIZONTAL_CROSSFLEET_CROSSDAP_TWO_CHARGE = "MPRIVU2  ";

	String THREE_WAY_CALLING = "MS3WC0   ";
	int PAYMENT_FREQUENCY_MONTH = 1;

	String PROVINCE_CODE_ALL = "ALL";

	String FAMILY_GROUP_CODE_STANDARD_EW = "SFGEW    ";
	String FAMILY_GROUP_CODE_EXTENDED_EW = "SFGXEW   ";

	String FAMILY_GROUP_TYPE_EQUIVALENT = "E";
	String FAMILY_GROUP_TYPE_PRICEPLAN = "P";

	String USER_SEGMENT_CORPORATE = "CORP      ";
	String USER_SEGMENT_CONSUMER = "PERSONAL  ";
	String USER_SEGMENT_BUSINESS = "SMB       ";
	String USER_SEGMENT_PREPAID = "PREPAID   ";

	String INTERNATIONAL_DIALING_PCS = "SINTL";
	String INTERNATIONAL_DIALING_IDEN = "MSINTL";

	String INTERNATIONAL_ROAMING_PCS = "SWORLDPH";

	String WPS_SERVICE_TYPE_TRACKING = "TRACKING";
	String WPS_SERVICE_TYPE_PROMOTIONAL = "PROMOTIONAL";

	String VIDEO_TELEPHONY_ENABLING_HSPA = "SVTPROVN";

	static final int AUTORENEW_FROM_BALANCE = FundSource.FUND_SOURCE_BALANCE;
	static final int AUTORENEW_FROM_CREDIT_CARD = FundSource.FUND_SOURCE_CREDIT_CARD;
	static final int AUTORENEW_FROM_BANK_CARD = FundSource.FUND_SOURCE_BANK_CARD;
	static final int AUTORENEW_NOT_DEFINED = FundSource.FUND_SOURCE_NOT_DEFINED;

	public static final String INTERNATIONAL_DATA_ROAMING_ENABLING_SERVICE = "SINTDTENB";
	public static final String INTERNATIONAL_DATA_ROAMING_ENABLING_SERVICE_KOODO = "3SINTDTEN";

	/**
	 * This is the suffix for the international dialing SOC. With the exception
	 * of {@link Brand#BRAND_ID_TELUS}, the SOC for international data roaming
	 * should be constructed as &lt;brand_id&gt;
	 * {@link #INTERNATIONAL_DATA_ROAMING}. For {@link Brand#BRAND_ID_TELUS},
	 * use {@link #INTERNATIONAL_DATA_ROAMING_ENABLING_SERVICE}
	 */
	public static final String INTERNATIONAL_DATA_ROAMING = "SINTDTEN";

	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents all Price Plans belonging to Business Anywhere and Business
	 * Anywhere Add-A-Line.
	 */
	public static final String FAMILY_TYPE_CODE_BUSINESS_ANYWHERE = "Y";
	/**
	 * This corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents all Price Plans belonging to Data Dollar Pooling
	 */
	public static final String FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING = "U";
	
	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents all PPS SOC Bundles
	 */
	public static final String FAMILY_TYPE_CODE_PPS_BUNDLE = "S";
	
	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents all PPS SOC Bundles
	 */
	public static final String FAMILY_TYPE_CODE_PPS_ADDON = "V";
	
	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents that the service will not count towards the minimum service 
	 * commitment (NON-MSC).
	 * 
	 * Note: (Wilson,2014-11-20)
	 * This constant was originally targeted for WOME's January 2015 release.
	 * WOME has been delayed for April - if the constant is no longer needed,
	 * please remove/update.
	 */
	public static final String FAMILY_TYPE_CODE_NON_MSC_SERVICE = "W";
	
	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents Flex Plans for CSAg
	 */
	public static final String FAMILY_TYPE_CODE_FLEX_PLAN = "3";

	/**
	 * This constant corresponds to the SOC_FAMILY_GROUP.family_type column and
	 * represents Mandatory Data Addon CAN for CSAg
	 */
	public static final String FAMILY_TYPE_CODE_MANDATORY_ADDON = "4";

	String SOC_SERVICE_TYPE_VOIP = "VOIP";
	String SOC_SERVICE_TYPE_HSIA = "HSIA";
	String SOC_SERVICE_TYPE_MOBILE = "MOBL";

	/**
	 * @deprecated
	 * @return String
	 */
	String getNetworkType(); // supported network type

	String getNetworkType(String equipmentType);

	/**
	 * Return all the supported network types
	 * 
	 * @return String[]
	 */
	String[] getAllNetworkTypes();

	String getServiceType();

	int getTermMonths();

	/** @deprecated **/
	String[] getEquipmentTypes();

	String[] getEquipmentTypes(String networkType);

	String getProductType();

	String getLevelCode();

	boolean isClientActivation();

	boolean isDealerActivation();

	boolean isBillingZeroChrgSuppress();

	boolean isDiscountAvailable();

	Date getEffectiveDate();

	Date getExpiryDate();

	boolean isForSale();

	boolean isCurrent();

	boolean isActive();

	boolean isGrandFathered();

	String[] getFamilyGroupCodes(String familyGroupType) throws TelusAPIException;

	boolean isLoyaltyAndRetentionService();

	/**
	 * Returns true if Price Plan is active, current, available for sale, not
	 * expired and effective date is not in the future.
	 */
	boolean isAvailable();

	boolean isTelephonyFeaturesIncluded();

	boolean isDispatchFeaturesIncluded();

	boolean isWirelessWebFeaturesIncluded();

	Service getService() throws TelusAPIException;

	/**
	 * @deprecated replaced by <code>getRelations(PricePlan pricePlan)</code>
	 *             <P>
	 *             This method may involve a remote method call.
	 * 
	 */
	ServiceRelation[] getRelations(Contract contract) throws TelusAPIException;

	/**
	 * @deprecated replaced by
	 *             <code>getRelations(PricePlan pricePlan, String relationType)</code>
	 *             <P>
	 *             This method may involve a remote method call.
	 * 
	 */
	ServiceRelation[] getRelations(Contract contract, String relationType) throws TelusAPIException;

	/**
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 * 
	 */
	ServiceRelation[] getRelations(PricePlan pricePlan)	throws TelusAPIException;

	/**
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 * 
	 */
	ServiceRelation[] getRelations(PricePlan pricePlan, String relationType) throws TelusAPIException;

	/**
	 * <CODE>true</CODE> if this service is included for a limited period in the
	 * priceplan.
	 * 
	 */
	public boolean isIncludedPromotion();

	/**
	 * <P>
	 * isPromotion true when this service is promotion for another service
	 * managed by Amdocs iterfaces
	 */
	boolean isPromotion();

	/**
	 * 
	 * <P>
	 * If hasPromotion true, call to getRelations with relation type "F" will
	 * return Promotion Service
	 * 
	 */
	boolean hasPromotion();

	/**
	 * <P>
	 * isBoundService true when this service is Bound to another service
	 * <P>
	 * managed by Telus Client API
	 */
	boolean isBoundService();

	/**
	 * 
	 * <P>
	 * If hasBoundService true, call to getRelations with relation type "M" will
	 * return Bound Service
	 * 
	 */
	boolean hasBoundService();

	/**
	 * <CODE>true</CODE>
	 * <P>
	 * if this service starts when another expires.
	 */
	boolean isSequentiallyBoundService();

	/**
	 * 
	 * <CODE>true</CODE>
	 * <P>
	 * if this service has as starting when it expires.
	 * 
	 */
	boolean hasSequentiallyBoundService();

	/**
	 * returns true if add-on SOCs are eligible for promo restrictions
	 * 
	 */
	boolean isPromoValidationEligible();

	int getTerm();

	String getTermUnits();

	boolean isWPS();

	boolean isKnowbility();

	boolean hasEquivalentService();

	/**
	 * 
	 * Returns the service in the specified PricePlan that can be substituted
	 * for this one or <CODE>null</CODE> if none exists.
	 * 
	 * @deprecated
	 */
	Service getEquivalentService(PricePlan pricePlan) throws TelusAPIException;

	Service getEquivalentService(PricePlan pricePlan, String networkType) throws TelusAPIException;

	// boolean getRemoveOnPriceplanChange();

	// boolean isAvailableForChangeByDealer();

	// boolean isAvailableForChangeByClient();

	// boolean isAvailableToModifyByDealer();

	// boolean isAvailableToModifyByClient();

	// boolean isAvailableForDisplayForDealer();

	// boolean isAvailableForDisplayByClient();

	int getMaxTerm();

	boolean isAutoRenewalAllowed();

	boolean isForcedAutoRenew() throws TelusAPIException;

	String getPeriodCode();

	/**
	 * Returns <CODE>true</CODE> if this service or priceplan has granted the
	 * privilege to the specified business role.
	 * 
	 * @deprecated As of February release, 2011, replaced by
	 *             <code>retainByPrivilege(ServiceSummary[] services, String businessRoleCode, 
	 * 		String privilegeCode)</code> This method will be removed by CASPER
	 *             project - Planned for May 2011 release
	 */
	boolean containsPrivilege(String businessRole, String privilege) throws TelusAPIException;

	/**
	 * Return all the Province support the price plan
	 */
	String[] getProvinces() throws TelusAPIException;

	String getUserSegment();

	ServicePeriod[] getServicePeriods() throws TelusAPIException;

	boolean isAssociatedIncludedPromotion(String pricePlanCode, int term) throws TelusAPIException;

	double getMinimumUsageCharge();

	boolean isSMSNotification();

	/**
	 * Returns <CODE>true</CODE> if this Service can be shared my multiple
	 * subscribers on the same acount with the same priceplan.
	 * 
	 */
	boolean isSharable();

	/**
	 * Return <code>true</code> if the Service is hotspot (WiFi).
	 * 
	 * @return boolean
	 */
	boolean isWiFi();

	/**
	 * Return true if this is a Multimedia Messaging Service (MMS).
	 * 
	 * @return boolean
	 */
	boolean isMMS();

	/**
	 * Returns true if this is a Java Download Service.
	 * 
	 * @return boolean
	 */
	// boolean isJavaDownload();

	/**
	 * Returns true if this is a Mobile Originated Short Message Service
	 * (MOSMS).
	 * 
	 * @return boolean
	 */
	boolean isMOSMS();

	/**
	 * Returns true if the service is PDA SOC.
	 * 
	 * @return boolean
	 */
	boolean isPDA();

	/**
	 * Returns true if the service is downgradable. Currently it's for life
	 * style bundles
	 * 
	 * @return boolean
	 */
	boolean isDowngradable();

	/**
	 * Returns true if the service includes Email and Webspace.
	 * 
	 * @return boolean
	 */
	boolean isEmailAndWebspaceIncluded();

	/**
	 * Returns true if the service is EvDO.
	 * 
	 * @return boolean
	 */
	boolean isEvDO();

	/**
	 * Returns true if it's LBS tracker
	 * 
	 * @return boolean
	 */
	boolean isLBSTracker();

	/**
	 * Returns true if it's LBS trackee
	 * 
	 * @return boolean
	 */
	boolean isLBSTrackee();

	/**
	 * Returns true, if CategoryCode = CATEGORY_CODE_MS_BASED
	 * 
	 * @return boolean
	 */
	boolean isMSBasedCapabilityRequired();

	boolean isNonCurrent();

	boolean containsCategory(String category);

	boolean hasVoiceToTextFeature();

	boolean isMandatory();

	/** Prepaid 4.1 changes **/
	PrepaidCategory getCategory();

	int getPriority();

	int getMaxConsActDays();

	/** Prepaid 4.1 changes end **/

	/**
	 * Returns related Knowbility SOC for this prepaid service
	 */
	String getWPSMappedKBSocCode();

	/**
	 * Returns true based on the Prepaid feature Category
	 */
	boolean isPrepaidLBM();

	String getWPSServiceType();

	/**
	 * check if this service is compatible with given networkType. Base on the
	 * configuration in KB's soc_equip_relation table
	 * 
	 * @param networkType
	 *            - the target network type
	 * @return boolean
	 */
	public boolean isCompatible(String networkType);

	/**
	 * Check if this service is compatible with given networkType and
	 * equpmentType combination. Base on the configuration in KB's
	 * soc_equip_relation table
	 * 
	 * Note, if the service is included service, we only check network type
	 * 
	 * @param networkType
	 *            - the networkType of the equipment
	 * @param equipmentType
	 *            - this should be the equipmentType of handset, not the SIM
	 *            card. Client should never pass USIM card's equipment type
	 * @return boolean
	 */
	public boolean isCompatible(String networkType, String equipmentType);

	/**
	 * Check if this service is compatible with given equipment in terms of
	 * network type ( and / or equipment type). Base on the configuration in
	 * KB's soc_equip_relation table
	 * 
	 * Note: if the equipment's network is HSPA, we only check network type if
	 * the service is included service, we only check network type otherwise, we
	 * check both network type and equipment type
	 * 
	 * @param equipment
	 *            - this should be the equipmentType of handset, not the SIM
	 *            card. Client should never pass USIM card's equipment type
	 * @return boolean
	 */
	public boolean isNetworkEquipmentTypeCompatible(Equipment equipment) throws TelusAPIException;

	/**
	 * Returns the available types of auto renewal fund source
	 * 
	 * @return the available types of auto renewal fund source
	 */
	FundSource[] getAllowedRenewalFundSourceArray();

	/**
	 * Retrieves the value from BILL_CYCLE_TREATMENT_CD column in KB’s SOC table
	 * 
	 * Valid values are: BCIC – indicates that the Service or PricePlan is BCIC
	 * based Blank/null - indicates that the Service or PricPlan is not BCIC
	 * based
	 * 
	 * @return Bill cycle treatment code
	 */
	String getBillCycleTreatmentCode();

	FundSource[] getAllowedPurchaseFundSourceArray();

	/**
	 * Returns all the data sharing groups that this service belongs to. If this
	 * service does not belong to any data sharing group, then this method
	 * returns an array of 0 length. This method will not return null.
	 * 
	 * @return ServiceDataSharingGroup[]
	 * @throws TelusAPIException
	 **/
	public ServiceDataSharingGroup[] getDataSharingGroups()	throws TelusAPIException;

	public int getBrandId();

	/**
	 * Returns all the family types that this service belongs to.
	 * 
	 * @see ServiceSummary#FAMILY_TYPE_CODE_BUSINESS_ANYWHERE
	 * @see ServiceSummary#FAMILY_TYPE_CODE_DATA_DOLLAR_POOLING
	 */
	public String[] getFamilyTypes();

	String getSocServiceType();
	
	boolean isFlexPlan();
	
	boolean isMandatoryAddOn();

}