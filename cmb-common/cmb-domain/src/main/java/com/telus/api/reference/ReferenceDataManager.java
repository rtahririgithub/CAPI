/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.telus.api.BrandNotSupportedException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.Contract;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentPossession;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.fleet.FleetClass;
import com.telus.eas.framework.exception.TelusException;


public interface ReferenceDataManager extends java.io.Serializable {
	//	Telus West WPP or Quebec TEL WPP

	public static final String PRODUCT_TYPE_PCS = "C";

	@Deprecated
	public static final String PRODUCT_TYPE_PAGER = "P";
	@Deprecated
	public static final String PRODUCT_TYPE_IDEN = "I";

	@Deprecated
	public static final String EQUIPMENT_TYPE_ANALOG  = "A";
	public static final String EQUIPMENT_TYPE_DIGITAL = "D";
	public static final String EQUIPMENT_TYPE_1xRTT= "3";
	public static final String EQUIPMENT_TYPE_ALL= "9";

	public static final int LANGUAGE_ENGLISH = 1;
	public static final int LANGUAGE_FRENCH  = 2;

	public static final String MARKET_AREA_TELUS_MOBILITY_EAST = "TME";
	public static final String MARKET_AREA_TELUS_MOBILITY_WEST   = "TMW";
	public static final String MARKET_AREA_QUEBECTEL   = "TMQ";

	public static final ReferenceComparator REFERENCE_COMPARATOR_ENGLISH = new ReferenceComparator(LANGUAGE_ENGLISH);
	public static final ReferenceComparator REFERENCE_COMPARATOR_FRENCH  = new ReferenceComparator(LANGUAGE_FRENCH);

	public static final String EQUIVALENT_SERVICE_FAMILY_TYPE = "E";
	public static final String PRICE_PLAN_FAMILY_TYPE = "P";

	public static final String BARCODE_REASON_ACTIVATION_DEPOSIT = "DPST";
	public static final String BARCODE_REASON_ACTIVATION_CREDIT = "ACT";
	public static final String BARCODE_REASON_PROMOTION_CREDIT = "PROMO";
	public static final String BARCODE_REASON_CONTRACT_TERM_CREDIT = "TERM";

	public static final String FEATURE_CODE_RENTAL = "RENT1 ";
	public static final String FEATURE_CODE_COAM = "COAM1 ";

	public static final String RESOURCE_TYPE_PHONE_NUMBER = "N";
	public static final String RESOURCE_STATUS_AGING = "AG";
	public static final String RESOURCE_STATUS_AVAILABLE = "AA";
	public static final String RESOURCE_STATUS_IN_USE = "AI";
	public static final String RESOURCE_STATUS_RESERVED = "AR";
	public static final String RESOURCE_STATUS_SUSPENDED = "AS";
	public static final String RESOURCE_STATUS_NOT_AVAILABLE = "NA";
	public static final String RESOURCE_STATUS_ON_HOLD = "NH";

	public static final String ZERO_MINUTE_POOLING_CONTRIBUTOR_SERVICES = "zeroMinutePoolingContributorServices";
	public static final String MINUTE_POOLING_CONTRIBUTOR_SERVICES = "minutePoolingContributorServices";

	public static final String RECHARGE_TYPE_ACTIVATION_CREDIT = "ACR";
	public static final String RECHARGE_TYPE_AUTO_TOP_UP = "ATU";
	public static final String RECHARGE_TYPE_ONETIME_TOP_UP = "RTU";
	public static final String RECHARGE_TYPE_ACTIVATION_CREDIT_FS = "ACR_FS";

	public static final String INTERNATIONAL_DATA_VOICE_SERVICE_GROUP = "SINLVDT";
	public static final String INTERNATIONAL_DATA_SERVICE_GROUP = "SINLDT";
	public static final String INTERNATIONAL_DATA_VOICE_SERVICE_GROUP_KOODO = "3SINLVDT";
	public static final String INTERNATIONAL_DATA_SERVICE_GROUP_KOODO = "3SINLDT";


	/**
	 *  Clear the cache, if one is present, and optionally reloads its data.
	 *
	 */
	void refresh() throws TelusAPIException;

	// Get All methods
	//-------------------------------------------------------------------
	Department[] getDepartments() throws TelusAPIException;

	Title[] getTitles() throws TelusAPIException;

	Title[] getAllTitles() throws TelusAPIException;

	State[] getStates() throws TelusAPIException;

	FollowUpType[] getFollowUpTypes() throws TelusAPIException;

	FollowUpType[] getAllFollowUpTypes() throws TelusAPIException;

	Network[] getNetworks() throws TelusAPIException;

	/**
	 * This method will only return AccountType objects with TELUS-branded defaults.
	 **/
	AccountType[] getAccountTypes() throws TelusAPIException;

	/*
	 * Returns all the street types
	 */

	// StreetTypes[] getStreetTypes() throws TelusAPIException;

	// StreetTypes getStreetType(String streetCode) throws TelusAPIException;

	/**
	 * Returns canadian provinces.
	 */
	Province[] getProvinces() throws TelusAPIException;

	Province[] getAllProvinces() throws TelusAPIException;

	Province[] getProvinces(String countryCode) throws TelusAPIException;

	CreditMessage[] getCreditMessages() throws TelusAPIException;

	CreditClass[] getCreditClasses() throws TelusAPIException;

	CoverageRegion[] getCoverageRegions() throws TelusAPIException;

	EncodingFormat[] getEncodingFormats() throws TelusAPIException;

	EquipmentType[] getPagerEquipmentTypes() throws TelusAPIException;

	PagerFrequency[] getPagerFrequencies() throws TelusAPIException;

	Language[]  getLanguages() throws TelusAPIException;

	Language[] getAllLanguages()  throws TelusAPIException;

	PaymentMethodType[]  getPaymentMethodTypes() throws TelusAPIException;

	PaymentMethod[]  getPaymentMethods() throws TelusAPIException;

	/**
	 * Returns Canada and USA.
	 */
	Country[]  getCountries() throws TelusAPIException;

	Country[] getCountries(boolean includeForiegn) throws TelusAPIException;

	CommitmentReason[] getCommitmentReasons() throws TelusAPIException;

	PhoneType[]  getPhoneTypes() throws TelusAPIException;

	UnitType[]  getUnitTypes() throws TelusAPIException;

	MemoType[] getMemoTypes() throws TelusAPIException;

	MemoType[] getAllMemoTypes() throws TelusAPIException;

	Service[] getWPSServices() throws TelusAPIException;

	Service[] getServicesByFeatureCategory(String featureCategory, String productType, boolean current) throws TelusAPIException;

	ActivityType [] getActivityTypes() throws TelusAPIException;

	CreditCardType [] getCreditCardTypes() throws TelusAPIException;

	CreditCardPaymentType [] getCreditCardPaymentTypes() throws TelusAPIException;

	ServicePeriodType [] getServicePeriodTypes() throws TelusAPIException;

	Feature [] getFeatures() throws TelusAPIException;

	PaymentSourceType [] getPaymentSourceTypes() throws TelusAPIException;

	ProductType [] getProductTypes() throws TelusAPIException;

	ProvisioningTransactionStatus getProvisioningTransactionStatus( String code ) throws TelusAPIException;

	ProvisioningTransactionStatus[] getProvisioningTransactionStatuses() throws TelusAPIException;

	ProvisioningTransactionType[] getProvisioningTransactionTypes() throws TelusAPIException;

	ProvisioningTransactionType getProvisioningTransactionType(String code) throws TelusAPIException;

	SID getSID( String code ) throws TelusAPIException;

	SID[] getSIDs() throws TelusAPIException;

	EquipmentProductType [] getEquipmentProductTypes() throws TelusAPIException;

	TermUnit[] getTermUnits() throws TelusAPIException;

	TaxationPolicy[] getTaxationPolicies() throws TelusAPIException;

	PrepaidEventType[] getPrepaidEventTypes() throws TelusAPIException;

	BusinessRole[] getBusinessRoles() throws TelusAPIException;

	FleetClass[] getFleetClasses() throws TelusAPIException;

	UsageUnit[] getUsageUnits() throws TelusAPIException;

	UsageRateMethod[] getUsageRateMethods() throws TelusAPIException;

	UsageRecordType[] getUsageRecordTypes() throws TelusAPIException;

	PrepaidAdjustmentReason[] getPrepaidAdjustmentReason() throws TelusAPIException;

	PrepaidAdjustmentReason[] getPrepaidFeatureAddWaiveReasons()  throws TelusAPIException;

	PrepaidAdjustmentReason[] getPrepaidManualAdjustmentReasons()  throws TelusAPIException;

	PrepaidAdjustmentReason[] getPrepaidTopUpWaiveReasons()  throws TelusAPIException;

	PrepaidAdjustmentReason[] getPrepaidDeviceDirectFulfillmentReasons()  throws TelusAPIException;

	SubscriptionRoleType[] getSubscriptionRoleTypes() throws TelusAPIException;

	ClientStateReason[] getClientStateReasons() throws TelusAPIException;

	AmountBarCode[] getAmountBarCodes() throws TelusAPIException;

	BillCycle[] getBillCycles() throws TelusAPIException;

	AdjustmentReason[] getAdjustmentReasons() throws TelusAPIException;

	ChargeType[] getManualChargeTypes() throws TelusAPIException;

	//	KnowbilityOperator[] getKnowbilityOperators() throws TelusAPIException;

	InvoiceSuppressionLevel[] getInvoiceSuppressionLevels() throws TelusAPIException;

	AddressType[] getAddressTypes() throws TelusAPIException;

	RuralType[] getRuralTypes() throws TelusAPIException;

	RuralDeliveryType[] getRuralDeliveryTypes() throws TelusAPIException;

	StreetDirection[] getStreetDirections() throws TelusAPIException;

	CollectionActivity[] getCollectionActivities() throws TelusAPIException;

	CollectionAgency[] getCollectionAgencies() throws TelusAPIException;

	TalkGroupPriority[] getTalkGroupPriorities() throws TelusAPIException;

	ServiceExclusionGroups[] getServiceExclusionGroups() throws TelusAPIException;

	ServiceExclusionGroups getServiceExclusionGroups(String code) throws TelusAPIException;

	MigrationType[] getMigrationTypes() throws TelusAPIException;

	CreditCheckDepositChangeReason[] getCreditCheckDepositChangeReasons() throws TelusAPIException;

	PaymentTransferReason[] getPaymentTransferReasons() throws TelusAPIException;

	Brand getBrand (String code) throws TelusAPIException;

	Brand getBrand (int brandId) throws TelusAPIException;

	Brand[] getBrands() throws TelusAPIException;

	Segmentation[] getSegmentations() throws TelusAPIException;
	Segmentation getSegmentation(int brand, String accType, String province) throws TelusAPIException;

	// Get Single methods
	//-------------------------------------------------------------------
	CreditCheckDepositChangeReason getCreditCheckDepositChangeReason(String code) throws TelusAPIException;

	MigrationType getMigrationType(String code) throws TelusAPIException;

	Department getDepartment(String code) throws TelusAPIException;

	Title getTitle(String code) throws TelusAPIException;

	State getState(String code) throws TelusAPIException;

	FollowUpType getFollowUpType(String code) throws TelusAPIException;

	Network getNetwork(String code) throws TelusAPIException;

	AccountType getAccountType(AccountSummary account) throws TelusAPIException;

	/**
	 * This method will only return an AccountType object with TELUS-branded defaults.
	 * @deprecated in favour of getAccountType(String code, int brandId)
	 */
	@Deprecated
	AccountType getAccountType(String code) throws TelusAPIException;

	AccountType getAccountType(String code, int brandId) throws TelusAPIException;

	Province getProvince(String code) throws TelusAPIException;

	Province getProvince(String countryCode, String code) throws TelusAPIException;

	CreditMessage getCreditMessage(String code) throws TelusAPIException;

	PromoTerm getPromoTerm(String promoCode) throws TelusAPIException;

	CreditCardType  getCreditCardType(String code) throws TelusAPIException;

	CreditCardPaymentType  getCreditCardPaymentType(String code) throws TelusAPIException;

	Language  getLanguage(String code) throws TelusAPIException;

	ServicePeriodType  getServicePeriodType(String code) throws TelusAPIException;

	PaymentMethod      getPaymentMethod(String code) throws TelusAPIException;

	PaymentSourceType     getPaymentSourceType(String code) throws TelusAPIException;

	PaymentSourceType getPaymentSourceType(String sourceID, String sourceType) throws TelusAPIException;

	PaymentMethodType  getPaymentMethodType(String code) throws TelusAPIException;

	Country  getCountry(String code) throws TelusAPIException;

	CoverageRegion   getCoverageRegion (String code) throws TelusAPIException;

	EncodingFormat   getEncodingFormat (String code) throws TelusAPIException;

	EquipmentType   getPagerEquipmentType (String code) throws TelusAPIException;

	PagerFrequency  getPagerFrequency (String code) throws TelusAPIException;

	CommitmentReason getCommitmentReason(String code) throws TelusAPIException;

	PhoneType getPhoneType(String code) throws TelusAPIException;

	UnitType  getUnitType(String code) throws TelusAPIException;

	UsageRateMethod  getUsageRateMethod(String code) throws TelusAPIException;

	MemoType getMemoType(String code) throws TelusAPIException;

	Service getRegularService(String code) throws TelusAPIException;

	Service[] getRegularServices(String[] code) throws TelusAPIException;

	Service getWPSService(String code) throws TelusAPIException;

	ActivityType getActivityType(String code) throws TelusAPIException;

	Feature getFeature(String code) throws TelusAPIException;

	ProductType getProductType(String code) throws TelusAPIException;

	EquipmentProductType getEquipmentProductType(String code) throws TelusAPIException;

	TermUnit getTermUnit(String code) throws TelusAPIException;

	TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusAPIException;

	PrepaidEventType getPrepaidEventType(String code) throws TelusAPIException;

	BusinessRole getBusinessRole(String code) throws TelusAPIException;

	UsageUnit getUsageUnit(String code) throws TelusAPIException;

	UsageRecordType getUsageRecordType(String code) throws TelusAPIException;

	FleetClass getFleetClass(String code) throws TelusAPIException;

	PrepaidAdjustmentReason getPrepaidAdjustmentReason(String code) throws TelusAPIException;

	PrepaidAdjustmentReason getPrepaidFeatureAddWaiveReason(String code)  throws TelusAPIException;

	PrepaidAdjustmentReason getPrepaidManualAdjustmentReason(String code)  throws TelusAPIException;

	PrepaidAdjustmentReason getPrepaidTopUpWaiveReason(String code)  throws TelusAPIException;

	PrepaidAdjustmentReason getPrepaidDeviceDirectFulfillmentReason(String code)  throws TelusAPIException;

	SubscriptionRoleType getSubscriptionRoleType(String code) throws TelusAPIException;

	ClientStateReason getClientStateReason(String code) throws TelusAPIException;

	BillCycle getBillCycle(String code) throws TelusAPIException;

	AdjustmentReason getAdjustmentReason(String code) throws TelusAPIException;

	ChargeType getManualChargeType(String code) throws TelusAPIException;

	KnowbilityOperator getKnowbilityOperator(String code) throws TelusAPIException;

	InvoiceSuppressionLevel getInvoiceSuppressionLevel(String code) throws TelusAPIException;

	StreetDirection getStreetDirection(String code) throws TelusAPIException;

	RuralDeliveryType getRuralDeliveryType(String code) throws TelusAPIException;

	RuralType getRuralType(String code) throws TelusAPIException;

	AddressType getAddressType(String code) throws TelusAPIException;

	CollectionActivity getCollectionActivity(String code) throws TelusAPIException;

	CollectionAgency getCollectionAgency(String code) throws TelusAPIException;

	PaymentTransferReason getPaymentTransferReason(String code) throws TelusAPIException;

	//ServicePromotion[] getServicePromotions(Service[] services, int term, String province, Equipment equipment, String businessRole) throws TelusAPIException;

	//ServicePromotion[] getServicePromotions(Service[] services, int term, String province, String productType, String equipmentType, String businessRole) throws TelusAPIException;

	/**
	 * Returns FeeWaiverType[]
	 * @throws TelusAPIException
	 * @return FeeWaiverType[]
	 */
	FeeWaiverType[] getFeeWaiverTypes() throws TelusAPIException;
	/**
	 * Returns FeeWaiverType
	 * @param typeCode String
	 * @throws TelusAPIException
	 * @return FeeWaiverType
	 */
	FeeWaiverType getFeeWaiverType(String typeCode) throws TelusAPIException;
	/**
	 * Returns FeeWaiverReason[]
	 * @throws TelusAPIException
	 * @return FeeWaiverReason[]
	 */
	FeeWaiverReason[] getFeeWaiverReasons() throws TelusAPIException;
	/**
	 * Returns FeeWaiverReason
	 * @param reasonCode String
	 * @throws TelusAPIException
	 * @return FeeWaiverReason
	 */
	FeeWaiverReason getFeeWaiverReason(String reasonCode) throws TelusAPIException;
	/**
	 * Returns the Equipment Statuses.
	 *
	 */
	EquipmentStatus [] getEquipmentStatuses()throws TelusAPIException;

	/**
	 * Returns the EquipmentTypes
	 */
	EquipmentType[] getEquipmentTypes() throws TelusAPIException;

	/**
	 * Returns the Resource Status.
	 *
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType The type of product
	 * @param resourceType The type of resource
	 * @param resourceNumber The number of resouce
	 *
	 */
	public String getResourceStatus(String productType, String resourceType, String resourceNumber) throws TelusAPIException ;


	// Parameterized methods
	//-------------------------------------------------------------------

	Dealer getDealer(String dealerCode) throws TelusAPIException;
	Dealer getDealer(String dealerCode, boolean expired) throws TelusAPIException;

	SalesRep[] getSalesReps(String dealerCode) throws TelusAPIException;

	SalesRep getDealerSalesRep(String dealerCode, String salesRepCode) throws TelusAPIException;
	SalesRep getDealerSalesRep(String dealerCode, String salesRepCode, boolean expired) throws TelusAPIException;

	int getUrbanId(NumberGroup numberGroup) throws TelusAPIException;

	/**
	 * Returns a AmountBarCode for specified amount and type
	 *
	 * @param amount dollar amount for barcode
	 * @param barCodeReason barCodeReason. Valid values:
	 *    BARCODE_REASON_ACTIVATION_DEPOSIT
	 *    BARCODE_REASON_ACTIVATION_CREDIT
	 *    BARCODE_REASON_PROMOTION_CREDIT
	 *    BARCODE_REASON_CONTRACT_TERM_CREDIT
	 *
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 *
	 * @throws TelusAPIException
	 *
	 */
	AmountBarCode getAmountBarCode(double amount, String barCodeReason) throws TelusAPIException;

	/**
	 * Returns the List of Promotional Discounts(Credits) for the Price Plan
	 *
	 *  The Product Promo Type List and initial activation indicator
	 *  should be retrieved from an existing handset Info.
	 *
	 * @param pricePlanCode Price Plan Code
	 * @param productPromoTypeList Product Promo Type List
	 * @param   initialActivation initial activation indicator
	 * @param termInMonth
	 * @return Array of String[] of Discounts(Credits) for the Price Plan
	 *
	 * @throws TelusException
	 *
	 */
	String[]  findPromotionalDiscounts(String pricePlanCode, long[] productPromoTypeList, boolean initialActivation, int termInMonth) throws TelusAPIException;

	/**
	 * Returns all Number Groups (Cities) where Telus phone ranges
	 * available for  a specific product type(PCS or IDEN),equipment type (analog or digital)
	 * and account type(Consumer Regular PCS, Corporate IDEN Regular, etc)
	 *
	 *  The equipmentType should be retrieved from an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param accountType account type
	 * @param accountSubTypeType account sub type
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 *
	 * @see Dealer#getNumberLocationCD
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 *
	 */
	NumberGroup[] getNumberGroups(char accountType, char accountSubTypeType, String productType, String equipmentType) throws TelusAPIException;


	/**
	 * Returns all Number Groups (Cities) where Telus phone ranges
	 * available for  a specific product type(PCS or IDEN),equipment type (analog or digital)
	 * and account type(Consumer Regular PCS, Corporate IDEN Regular, etc) and market area code ('TME', 'TMW' or 'TMQ')
	 * The equipmentType should be retrieved from an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param accountType account type
	 * @param accountSubTypeType account sub type
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param marketAreaCode the market area code ('TME', 'TMW' or 'TMQ')
	 *
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 *
	 */
	NumberGroup[] getNumberGroups(char accountType, char accountSubTypeType, String productType, String equipmentType, String marketAreaCode) throws TelusAPIException;

	/**
	 * Returns all Number Groups (Cities) where Telus phone ranges
	 *  available for  a specific product type(PCS or IDEN),equipment type (analog or digital)
	 * and account type(Consumer Regular PCS, Corporate IDEN Regular) and province
	 *  The equipmentType should be retrieved from an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param accountType account type
	 * @param accountSubTypeType account sub type
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param provinceCode province code
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 *
	 */
	NumberGroup[] getNumberGroupsByProvince(char accountType, char accountSubTypeType, String productType, String equipmentType, String provinceCode) throws TelusAPIException;

	/**
	 * Returns all price plans in a province relevant for a specific equipment
	 * type (analog or digital).  The equipmentType should be retrieved from
	 * an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param provinceCode the ISO code for the province.
	 * @param accountType
	 * @param  accountSubType
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 *
	 * @see Province
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 *
	 * @return  Array of Price Plan Summary
	 *
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType , String equipmentType) throws TelusAPIException;

	/**
	 * Returns all price plans in a province relevant for a specific equipment
	 * type (analog or digital).  The equipmentType should be retrieved from
	 * an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param provinceCode the ISO code for the province.
	 * @param accountType
	 * @param  accountSubType
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param currentPlansOnly if <CODE>true</CODE> non-current priceplans will not be returned
	 * @param availableForActivationOnly if <CODE>true</CODE> available for Activation through interfaces priceplans will  be returned
	 *
	 * @see Province
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 *
	 * @return  Array of Price Plan Summary
	 *
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly, int termInMonths) throws TelusAPIException;

	/**
	 * Returns all price plans in a province relevant for a specific equipment
	 * type (analog or digital).  The equipmentType should be retrieved from
	 * an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param provinceCode the ISO code for the province.
	 * @param accountType
	 * @param  accountSubType
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param currentPlansOnly if <CODE>true</CODE> non-current priceplans will not be returned
	 * @param availableForActivationOnly if <CODE>true</CODE> available for Activation through interfaces priceplans will  be returned
	 *
	 * @see Province
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 *
	 * @return  Array of Price Plan Summary
	 *
	 */

	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly) throws TelusAPIException;


	/**
	 * Returns all price plans in a province relevant for a specific equipment
	 * type (analog or digital).  The equipmentType should be retrieved from
	 * an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param provinceCode the ISO code for the province.
	 * @param accountType
	 * @param  accountSubType
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param brandId
	 * @param currentPlansOnly if <CODE>true</CODE> non-current priceplans will not be returned
	 * @param availableForActivationOnly if <CODE>true</CODE> available for Activation through interfaces priceplans will  be returned
	 *
	 * @see Province
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 * @return  Array of Price Plan Summary
	 *
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, int brandId, boolean currentPlansOnly, boolean availableForActivationOnly) throws TelusAPIException;

	/**
	 * Returns all price plans in a province relevant for a specific equipment
	 * type (analog or digital).  The equipmentType should be retrieved from
	 * an existing handset.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param provinceCode the ISO code for the province.
	 * @param accountType
	 * @param  accountSubType
	 * @param equipmentType the handset's equipmentType (analog or digital).
	 * @param currentPlansOnly if <CODE>true</CODE> non-current priceplans will not be returned
	 * @param availableForActivationOnly if <CODE>true</CODE> available for Activation through interfaces priceplans will  be returned
	 * @param brandId
	 * @param termInMonths
	 *
	 * @see Province
	 * @see com.telus.api.equipment.Equipment#getEquipmentType
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 * @return  Array of Price Plan Summary
	 *
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode, char accountType, char accountSubType, String equipmentType, boolean currentPlansOnly, boolean availableForActivationOnly, int brandId, int termInMonths) throws TelusAPIException;


	/**
	 * @param productType
	 * @param equipmentType
	 * @param provinceCode
	 * @param accountType
	 * @param accountSubType
	 * @param currentPlansOnly
	 * @param availableForActivationOnly
	 * @param activityCode
	 * @param activityReasonCode
	 * @param brandId
	 * @param networkType
	 * @return PricePlanSummary[]
	 * @throws TelusAPIException
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String equipmentType,
			String provinceCode, char accountType, char accountSubType,
			boolean currentPlansOnly, boolean availableForActivationOnly,
			String activityCode, String activityReasonCode, int brandId,
			String networkType) throws TelusAPIException;


	/**
	 * Retrieves a list of price plans that match the given criteria.
	 * <p>
	 * The following fields are mandatory and must not be empty:
	 * <ul>
	 * 	<li>productType</li>
	 *  <li>provinceCode</li>
	 *  <li>accountType</li>
	 *  <li>accountSubType</li>
	 * </ul>
	 * <p>
	 * All other fields in criteria object are optional and will be default to the following values:
	 * <ul>
	 * 	<li>brandId = {@link Brand}.BRAND_ID_ALL (255)</li>
	 * 	<li>equipmentType = 9</li>
	 * 	<li>initialActivation = true</li>
	 * 	<li>currentPricePlansOnly = true</li>
	 * 	<li>availableForActivationOnly = false</li>
	 * 	<li>term = {@link PricePlanSummary}.CONTRACT_TERM_ALL (99)</li>
	 * 	<li>activityCode = ??</li>
	 * 	<li>activityReasonCode = ??</li>
	 * 	<li>networkType = NetworkType.NETWORK_TYPE_ALL (9)</li>
	 * </ul>
	 * <p>
	 * @param criteria - price plan selection criteria
	 * @return an array of {@link PricePlanSummary} objects
	 * @throws TelusAPIException
	 * <p>
	 * @see PricePlanSelectionCriteria
	 */
	PricePlanSummary[] findPricePlans(PricePlanSelectionCriteria criteria) throws TelusAPIException;


	/**
	 * @param productType
	 * @param provinceCode
	 * @param accountType
	 * @param accountSubType
	 * @param equipmentType
	 * @param productPromoTypeList
	 * @param initialActivation
	 * @param brandId
	 * @param networkType
	 * @return PricePlanSummary[]
	 * @throws TelusAPIException
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode,
			char accountType, char accountSubType, String equipmentType,
			long[] productPromoTypeList, boolean initialActivation,
			int brandId, String networkType) throws TelusAPIException;
	/**
	 * @param productType
	 * @param provinceCode
	 * @param accountType
	 * @param accountSubType
	 * @param equipmentType
	 * @param brandId
	 * @param currentPlansOnly
	 * @param availableForActivationOnly
	 * @param networkType
	 * @return PricePlanSummary[]
	 * @throws TelusAPIException
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode,
			char accountType, char accountSubType, String equipmentType,
			int brandId, boolean currentPlansOnly,
			boolean availableForActivationOnly, String networkType)
					throws TelusAPIException;

	/**
	 * @param productType
	 * @param provinceCode
	 * @param accountType
	 * @param accountSubType
	 * @param equipmentType
	 * @param currentPlansOnly
	 * @param availableForActivationOnly
	 * @param brandId
	 * @param term
	 * @param networkType
	 * @return PricePlanSummary[]
	 * @throws TelusAPIException
	 * @deprecated As of February release, 2011,
	 * 		replaced by <code>findPricePlans(PricePlanSelectionCriteria criteria)</code>
	 * 		This method will be removed by CASPER project - Planned for May 2011 release
	 */
	@Deprecated
	PricePlanSummary[] findPricePlans(String productType, String provinceCode,
			char accountType, char accountSubType, String equipmentType,
			boolean currentPlansOnly, boolean availableForActivationOnly,
			int brandId, int term, String networkType) throws TelusAPIException;



	/**
	 * Returns  Price Plan info with array of included services   and optional
	 * services for given Price Plan code
	 * The equipment type (analog or digital), province Code and SOC(service) Group (that defined
	 * by account type and account sub type) should be specified in order
	 * to get list of optional services associated with Price Plan
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlanCode - the service order code (SOC) for the Price Plan.
	 * @param provinceCode the ISO code for the province.
	 * @param equipmentType the handset's equipmentType.
	 * @param accountType
	 * @param  accountSubType
	 *
	 *@deprecated replaced by {@link #getPricePlan(String,  String, String, char, char, int)}
	 */
	@Deprecated
	PricePlan getPricePlan(String pricePlanCode, String equipmentType,
			String provinceCode, char accountType, char accountSubType ) throws TelusAPIException;

	/**
	 * Returns  Price Plan info with array of included services   and optional
	 * services for given Price Plan code
	 * The equipment type (analog or digital), province Code and SOC(service) Group (that defined
	 * by account type and account sub type) should be specified in order
	 * to get list of optional services associated with Price Plan
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlanCode - the service order code (SOC) for the Price Plan.
	 * @param provinceCode the ISO code for the province.
	 * @param equipmentType the handset's equipmentType.
	 * @param accountType
	 * @param  accountSubType
	 * @param brandId
	 *
	 */
	PricePlan getPricePlan(String pricePlanCode, String equipmentType,
			String provinceCode, char accountType, char accountSubType, int brandId ) throws TelusAPIException;


	/**
	 * Returns  Price Plan info with array of included services   and optional
	 * services for given Price Plan code
	 * The equipment type (analog or digital), province Code and SOC(service) Group (that defined
	 * by account type and account sub type) should be specified in order
	 * to get list of optional services associated with Price Plan
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param pricePlanCode - the service order code (SOC) for the Price Plan.
	 * @param provinceCode the ISO code for the province.
	 * @param equipmentType the handset's equipmentType.
	 * @param accountType
	 * @param  accountSubType
	 *
	 * @deprecated replaced by {@link #getPricePlan(String,  String, String, String, char, char, int)}
	 */
	@Deprecated
	PricePlan getPricePlan(String productType, String pricePlanCode, String equipmentType,
			String provinceCode, char accountType, char accountSubType ) throws TelusAPIException;

	/**
	 * Returns  Price Plan info with array of included services   and optional
	 * services for given Price Plan code
	 * The equipment type (analog or digital), province Code and SOC(service) Group (that defined
	 * by account type and account sub type) should be specified in order
	 * to get list of optional services associated with Price Plan
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param productType one of the PRODUCT_TYPE_xxx constants.
	 * @param pricePlanCode - the service order code (SOC) for the Price Plan.
	 * @param provinceCode the ISO code for the province.
	 * @param equipmentType the handset's equipmentType.
	 * @param accountType
	 * @param  accountSubType
	 * @param brandId
	 *
	 * @deprecated replaced by {@link #getPricePlan(String, String, String, char, char, int) as @link #getPricePlan(String, String, String, char, char) was deprecated before Zebra}
	 */

	@Deprecated
	PricePlan getPricePlan(String productType, String pricePlanCode, String equipmentType,
			String provinceCode, char accountType, char accountSubType, int brandId ) throws TelusAPIException;

	/**
	 * Returns  Price Plan Summary
	 * <P>This method may involve a remote method call.
	 *
	 * @param pricePlanCode - the service order code (SOC) for the Price Plan.
	 */
	PricePlanSummary getPricePlan(String pricePlanCode) throws TelusAPIException;



	/**
	 * Returns a DiscountPlan by code.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param discountCode - the code for this DiscountPlan.
	 */
	DiscountPlan getDiscountPlan(String discountCode) throws TelusAPIException;

	/**
	 * Returns all DiscountPlans.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param current <CODE>true</CODE> if the list should only include current plans.
	 *
	 */
	DiscountPlan[] getDiscountPlans(boolean current) throws TelusAPIException;

	/**
	 * Returns all DiscountPlans appropriate for a priceplan in a certain province.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param current       <CODE>true</CODE> if the list should only include current plans.
	 * @param priceplanCode the priceplan to check against.
	 * @param provinceCode  The ISO code for the province to check against.
	 *
	 */
	DiscountPlan[] getDiscountPlans(boolean current, String priceplanCode, String provinceCode, int termInMonths) throws TelusAPIException;

	/**
	 * Returns all DiscountPlans appropriate for a priceplan, in a certain province and with a certain equipment.
	 *
	 * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
	 * elements, but may contain no (zero) elements.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 * @param current       <CODE>true</CODE> if the list should only include current plans.
	 * @param priceplanCode the priceplan to check against.
	 * @param provinceCode  The ISO code for the province to check against.
	 * @param equipment     The equipment to check against.
	 *
	 */
	DiscountPlan[] getDiscountPlans(boolean current, String priceplanCode, String provinceCode, Equipment equipment, int termInMonths) throws TelusAPIException;

	/**
	 * Returns EquipmentStatus appropriate for the given StatusID and StatusTypeID.
	 *
	 * @param StatusID the status of the equipment.
	 * @param StatusTypeID  the status type of the equipment.
	 *
	 */
	EquipmentStatus getEquipmentStatus(long StatusID, long StatusTypeID)throws TelusAPIException ;

	/**
	 * @param equipTypeCode
	 * @return EquipmentType corresponding to the given code
	 * @throws TelusAPIException
	 */
	EquipmentType getEquipmentType(String equipTypeCode) throws TelusAPIException;

	/**
	 * Returns the date and time as they exist on the server.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	java.util.Date getSystemDate() throws TelusAPIException;


	/**
	 * Returns the date and time as they exist in the billing system.
	 *
	 * <P>This method may involve a remote method call.
	 *
	 */
	java.util.Date getLogicalDate() throws TelusAPIException;

	// Push communication

	NotificationType getNotificationType(String code) throws TelusAPIException;

	NotificationType[] getNotificationTypes() throws TelusAPIException;

	ClientConsentIndicator[] getClientConsentIndicators() throws TelusAPIException;

	ClientConsentIndicator getClientConsentIndicator(String code) throws TelusAPIException;

	InvoiceCallSortOrderType[] getInvoiceCallSortOrderTypes() throws TelusAPIException;

	InvoiceCallSortOrderType getInvoiceCallSortOrderType(String code) throws TelusAPIException;


//	LetterCategory[] getLetterCategories() throws TelusAPIException;
//	LetterCategory getLetterCategory (String letterCategory)  throws TelusAPIException;
//	LetterSubCategory[] getLetterSubCategories(String letterCategory)  throws TelusAPIException;

	/**
	 *
	 * @param letterSubCategory
	 * @return LetterSubCategoryInfo
	 * @throws TelusAPIException
	 * @deprecated As of January release, 2015
	 * replaced by <code>getLetterSubCategory(String letterCategory, String letterSubCategory)</code>
	 * Reason to deprecate the method - Since back end we may have more than one LetterSubCategoryInfo exits for same letterSubCategory so results are not reliable,
	 * so new method will accept both  letterCategory and letterSubCategory and provide Unique results
	 *
	 *
	 */
//	@Deprecated
//	LetterSubCategory getLetterSubCategory (String letterSubCategory)  throws TelusAPIException;
//
//	/** This method will take letterCategory and letterSubCategory as inputs and return letterSubCategoryInfo .
//	 *
//	 * @param letterCategory
//	 * @param letterSubCategory
//	 * @return letterSubCategoryInfo
//	 * @throws TelusAPIException
//	 */
//	LetterSubCategory getLetterSubCategory (String letterCategory ,String letterSubCategory)  throws TelusAPIException;
//
//	Letter[] getLettersByTitleKeyword(String titleKeyword)  throws TelusAPIException;
//	Letter[] getLettersByCategory(String letterCategory)  throws TelusAPIException;
//	Letter getLetter(String letterCategory, String letterCode)  throws TelusAPIException;
//	Letter getLetter(String letterCategory, String letterCode, int version)  throws TelusAPIException;
//	LetterVariable[] getLetterVariables(String letterCategory, String letterCode, int letterVersion) throws TelusAPIException;

	WorkPosition getWorkPosition(String workPositionId) throws TelusAPIException;
	WorkPosition[] getWorkPositions(String functionCode) throws TelusAPIException;
	FollowUpCloseReason[] getFollowUpCloseReasons() throws TelusAPIException;
	FollowUpCloseReason getFollowUpCloseReason(String reasonCode) throws TelusAPIException;

	WorkFunction[] getWorkFunctions() throws TelusAPIException;
	WorkFunction[] getWorkFunctions(String departmentCode) throws TelusAPIException;

	BillHoldRedirectDestination[] getBillHoldRedirectDestinations() throws TelusAPIException;
	BillHoldRedirectDestination getBillHoldRedirectDestination(String destinationCode) throws TelusAPIException;

	CorporateAccountRep getCorporateAccountRep(String code) throws TelusAPIException;
	CorporateAccountRep[] getCorporateAccountReps() throws TelusAPIException;

	Generation[] getGenerations() throws TelusAPIException;
	Generation getGeneration(String generationCode) throws TelusAPIException;

	EquipmentPossession[] getEquipmentPossessions() throws TelusAPIException;
	EquipmentPossession getEquipmentPossession(String code) throws TelusAPIException;

	ApplicationSummary getApplicationSummary(String applicationCode) throws TelusAPIException;
	AudienceType getAudienceType(String audienceTypeCode) throws TelusAPIException;

	ExceptionReason[] getExceptionReasons() throws TelusAPIException;
	ExceptionReason getExceptionReason(String code) throws TelusAPIException;

	MemoTypeCategory[] getMemoTypeCategories() throws TelusAPIException;
	MemoTypeCategory getMemoTypeCategory(String code) throws TelusAPIException;

	public CollectionPathDetail[] getCollectionPathDetails(String pathCode) throws TelusAPIException;
	public CollectionActivity getCollectionActivity(String pathCode, int stepNumber) throws TelusAPIException ;
	public CollectionStepApproval[] getCollectionStepApprovals() throws TelusAPIException;
	public CollectionStepApproval getCollectionStepApproval(String code) throws TelusAPIException;
	public String[] getCollectionPaths() throws TelusAPIException;
	public AvailablePhoneNumber getAvailablePhoneNumber(String pPhoneNumber, String pProductType, String pDealerCode) throws TelusAPIException;

	LockReason getLockReason(String code) throws TelusAPIException;
	LockReason getLockReason(long lockReasonId) throws TelusAPIException;
	LockReason[] getLockReasons() throws TelusAPIException;

	String[] getAvailableNpaNxxForMsisdnReservation(String phoneNumber, boolean isPortedInNumber) throws TelusAPIException;

	public static class Helper {

		public static Service[] removeSMSNotificationServices(Service[] service) {

			List<Service> list = new ArrayList<Service>(service.length);
			Service s;
			for (Service element : service) {
				s = element;
				if (!s.isSMSNotification()) {
					list.add(s);
				}
			}
			// Equivalent: service = new Service[list.size()];
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());

			return list.toArray(service);

		}

		@Deprecated
		public static PricePlanSummary[] removeFidoPricePlan(PricePlanSummary[] plans) {

			List<PricePlanSummary> list = new ArrayList<PricePlanSummary>(plans.length);
			PricePlanSummary p;
			for (PricePlanSummary plan : plans) {
				p = plan;
				if (!p.isFidoPricePlan()) {
					list.add(p);
				}
			}

			return list.toArray(new PricePlanSummary[list.size()]);
		}

		public static boolean validateBrandId(int brandId, Brand[] brands) {
			int[] brandIds = new int[brands.length];
			for (int i = 0; i < brands.length; i++) {
				brandIds[i] = brands[i].getBrandId();
			}
			return isBrandIdExist(brandId, brandIds);
		}

		private static boolean isBrandIdExist(int brand, int[] brandIds){
			for (int brandId : brandIds) {
				if(brand == brandId){
					return true;
				}
			}
			return false;
		}

		public static AccountSummary[] filterAccountByBrand(AccountSummary[] accounts, int[] brandIds) {
			if (brandIds == null || brandIds.length == 0) {
				return accounts;
			}
			ArrayList<AccountSummary> list = new ArrayList<AccountSummary>(accounts.length);
			for (AccountSummary account : accounts) {
				if (isBrandIdExist(account.getBrandId(), brandIds)) {
					list.add(account);
				}
			}
			accounts = (AccountSummary[]) java.lang.reflect.Array.newInstance(accounts.getClass().getComponentType(), list.size());
			return list.toArray(accounts);
		}



		public static Account filterAnAccountByBrand(Account account,
				int[] brandIds) throws BrandNotSupportedException, TelusAPIException {
			if (brandIds == null || brandIds.length == 0) {
				return account;
			}

			if (!isBrandIdExist(account.getBrandId(), brandIds)) {
				throw new BrandNotSupportedException(account, brandIds);
			}
			return account;
		}

		public static Subscriber[] filterSubscribersByBrand(Subscriber[] subscribers, int[] brandIds, boolean internalUse) {
			if (brandIds == null || brandIds.length == 0 || internalUse) {
				return subscribers;
			}
			ArrayList<Subscriber> list = new ArrayList<Subscriber>(subscribers.length);
			for (Subscriber subscriber : subscribers) {
				if (isBrandIdExist(subscriber.getBrandId(), brandIds)) {
					list.add(subscriber);
				}
			}
			subscribers = (Subscriber[]) java.lang.reflect.Array.newInstance(subscribers.getClass().getComponentType(), list.size());
			return list.toArray(subscribers);
		}

		public static Subscriber filterSubscriberByBrand(Subscriber subscriber, int[] brandIds) throws BrandNotSupportedException, TelusAPIException {
			if (brandIds == null || brandIds.length == 0) {
				return subscriber;
			}
			if (!isBrandIdExist(subscriber.getBrandId(), brandIds)) {
				throw new BrandNotSupportedException(subscriber, brandIds);
			}
			return subscriber;
		}


		public static PricePlanSummary[] filterPricePlansByBrand(PricePlanSummary[] pricePlanSummaries, int[] brandIds) {

			if (brandIds == null || brandIds.length == 0) {
				return pricePlanSummaries;
			}
			ArrayList<PricePlanSummary> list = new ArrayList<PricePlanSummary>(pricePlanSummaries.length);
			for (PricePlanSummary pricePlanSummarie : pricePlanSummaries) {
				if (isBrandIdExist(pricePlanSummarie.getBrandId(), brandIds)) {
					list.add(pricePlanSummarie);
				}
			}
			pricePlanSummaries = (PricePlanSummary[])java.lang.reflect.Array.newInstance(pricePlanSummaries.getClass().getComponentType(), list.size());
			return list.toArray(pricePlanSummaries);
		}

		public static PricePlanSummary filterPricePlanByBrand(PricePlanSummary pricePlanSummary, int[] brandIds) {

			if (brandIds == null || brandIds.length == 0) {
				return pricePlanSummary;
			}
			if (!isBrandIdExist(pricePlanSummary.getBrandId(), brandIds)) {
				throw new RuntimeException("Ban Not support by Brand");
			}
			return pricePlanSummary;

		}

		public static Service[] removeMMSService(Service[] service) {

			List<Service> list = new ArrayList<Service>(service.length);
			Service s;
			for (Service element : service) {
				s = element;
				if (s.isMMS()) {
					// removed
				} else { // else added
					list.add(s);
				}
			}

			return list.toArray(new Service[list.size()]);
		}

		public static Service[] removePTTService(Service[] service) {

			List<Service> list = new ArrayList<Service>(service.length);
			Service s;
			for (Service element : service) {
				s = element;
				if (!s.isPTT()) {
					list.add(s);
				}
			}

			return list.toArray(new Service[list.size()]);
		}

		public static ServiceSummary[] removeAllClient(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!o.isClientActivation()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeWiFiServices(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);
			ServiceSummary s = null;

			for (ServiceSummary element : service) {
				s = element;
				if(!s.isWiFi()) {
					list.add(s);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removePDAServices(ServiceSummary[] service) throws TelusAPIException {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);
			ServiceSummary s = null;

			for (ServiceSummary element : service) {
				s = element;
				if(!s.isPDA()) {
					list.add(s);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeEvDOServices(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);
			ServiceSummary s = null;

			for (ServiceSummary element : service) {
				s = element;
				if(!s.isEvDO()) {
					list.add(s);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeAllDealer(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!o.isDealerActivation()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeNonClient(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				if(o.isClientActivation()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeNonDealer(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				if(o.isDealerActivation()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}



		public static ServiceSummary[] removeAllTelephony(com.telus.api.reference.ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!o.isTelephonyFeaturesIncluded()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeAllDispatch(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!o.isDispatchFeaturesIncluded()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeAllWirelessWeb(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!o.isWirelessWebFeaturesIncluded()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeTelephonyOnly(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(o.isDispatchFeaturesIncluded() || o.isWirelessWebFeaturesIncluded()||(!o.isTelephonyFeaturesIncluded())) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeDispatchOnly(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(o.isTelephonyFeaturesIncluded() || o.isWirelessWebFeaturesIncluded()||(!o.isDispatchFeaturesIncluded())) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removeWirelessWebOnly(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(o.isTelephonyFeaturesIncluded() || o.isDispatchFeaturesIncluded()||(!o.isWirelessWebFeaturesIncluded())) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Service[] removeVistoServices(Service[] service) {
			List<Service> list = new ArrayList<Service>(service.length);
			Service s = null;

			for (Service element : service) {
				s = element;
				if(!s.isVisto()) {
					list.add(s);
				}
			}
			return list.toArray(new Service[list.size()]);
		}

		public static Service[] removeLBSServices(Service[] service, Equipment equipment) {

			MuleEquipment mule = null;
			if (equipment.isSIMCard()) {
				try {
					mule = ((SIMCardEquipment)equipment).getLastMule();
				} catch(TelusAPIException e) {
				}
			}

			List<Service> list = new ArrayList<Service>();
			Service s = null;

			for (Service element : service) {
				s = element;
				if (s.isLBSTrackee()) {
					if (equipment.isGPS()) {
						if (s.isMSBasedCapabilityRequired() && !equipment.isMSBasedEnabled()) {
							// remove service
						} else {
							list.add(s);
						}
					} else if (mule != null && mule.isGPS()) {
						if (s.isMSBasedCapabilityRequired() && !mule.isMSBasedEnabled()) {
							// remove service
						} else {
							list.add(s);
						}
					}
				}
				else {
					list.add(s);
				}
			}
			return list.toArray(new Service[list.size()]);
		}


		public static Service[] removeRIMOnly(Service[] service) {
			List<Service> list = new ArrayList<Service>(service.length);

			for (Service element : service) {
				Service s = element;
				if (!s.isRIM()) {
					list.add(s);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ServiceSummary[] removePartialResourceServices(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				if(o.isTelephonyFeaturesIncluded() && o.isWirelessWebFeaturesIncluded() && o.isDispatchFeaturesIncluded()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}


		public static ServiceSummary[] removeAllNonDispatch(ServiceSummary[] service) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				if(o.isDispatchFeaturesIncluded()) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Service[] removeALLCoverage(Service[] service) {
			List<Service> list = new ArrayList<Service>(service.length);

			for (Service o : service) {
				if(o.getCoverageType() == null) {
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Service[] removePakServices(Service[] service) {
			List<Service> list = new ArrayList<Service>(service.length);

			for (Service o : service) {
				if(o.getCoverageType() == null) {
					list.add(o);
				}
				else if (o.getCoverageType().equals(CoverageRegion.COVERAGE_TYPE_EXTENDED)) {
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}




		public static ServiceSummary[] removeByCode(ServiceSummary[] service, String code) {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				ServiceSummary o = element;
				if(!code.equals(o.getCode())) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Service[] removeBySwitchCode(Service[] service, String switchCode) {
			List<Service> list = new ArrayList<Service>(service.length);

			for (Service element : service) {
				Service o = element;
				if(!o.containsSwitchCode(switchCode)) {
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Feature[] removeBySwitchCode(Feature[] feature, String switchCode) {
			List<Feature> list = new ArrayList<Feature>(feature.length);

			for (Feature element : feature) {
				Feature o = element;
				if(!o.getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
					list.add(o);
				}
			}
			feature = (Feature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static ContractService[] removeBySwitchCode(ContractService[] service, String switchCode) throws TelusAPIException {
			List<ContractService> list = new ArrayList<ContractService>(service.length);

			for (ContractService element : service) {
				ContractService o = element;
				if(!o.getService().containsSwitchCode(switchCode)) {
					list.add(o);
				}
			}
			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ContractFeature[] removeBySwitchCode(ContractFeature[] feature, String switchCode) {
			List<ContractFeature> list = new ArrayList<ContractFeature>(feature.length);

			for (ContractFeature element : feature) {
				ContractFeature o = element;
				if(!o.getFeature().getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
					list.add(o);
				}
			}
			feature = (ContractFeature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static Service[] removeTelephonyDisabledConflicts(Service[] service) {
			service = removeBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			service = removeBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			return service;
		}

		public static Feature[] removeTelephonyDisabledConflicts(Feature[] feature) {
			feature = removeBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			feature = removeBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			return feature;
		}

		public static ContractService[] removeTelephonyDisabledConflicts(ContractService[] service) throws TelusAPIException {
			service = removeBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			service = removeBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			return service;
		}

		public static ContractFeature[] removeTelephonyDisabledConflicts(ContractFeature[] feature) {
			feature = removeBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			feature = removeBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			return feature;
		}

		public static Service[] retainBySwitchCode(Service[] service, String switchCode) {
			List<Service> list = new ArrayList<Service>(service.length);

			for (Service o : service) {
				if(o.containsSwitchCode(switchCode)) {
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Feature[] retainBySwitchCode(Feature[] feature, String switchCode) {
			List<Feature> list = new ArrayList<Feature>(feature.length);

			for (Feature o : feature) {
				if(o.getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
					list.add(o);
				}
			}
			feature = (Feature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static ContractService[] retainBySwitchCode(ContractService[] service, String switchCode) throws TelusAPIException {
			List<ContractService> list = new ArrayList<ContractService>(service.length);

			for (ContractService o : service) {
				if(o.getService().containsSwitchCode(switchCode)) {
					list.add(o);
				}
			}
			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ContractFeature[] retainBySwitchCode(ContractFeature[] feature, String switchCode) {
			List<ContractFeature> list = new ArrayList<ContractFeature>(feature.length);

			for (ContractFeature o : feature) {
				if(o.getFeature().getSwitchCode().trim().equalsIgnoreCase(switchCode)) {
					list.add(o);
				}
			}
			feature = (ContractFeature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static ContractFeature[] retainByCategoryCode(ContractFeature[] feature, String categoryCode) {
			List<ContractFeature> list = new ArrayList<ContractFeature>(feature.length);

			for (ContractFeature o : feature) {
				if(o.getFeature().getCategoryCode() != null && o.getFeature().getCategoryCode().trim().equalsIgnoreCase(categoryCode)) {
					list.add(o);
				}
			}
			feature = (ContractFeature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static Service[] retainTelephonyDisabledConflicts(Service[] service) {
			//Service[] service1 = removeBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			//Service[] service2 = removeBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			Service[] service1 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			Service[] service2 = retainBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			Service[] service3 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_WAITING);
			Service[] service4 = retainBySwitchCode(service, Feature.SWITCH_CODE_FAX_MAIL);

			List<Service> list = new ArrayList<Service>(service1.length + service2.length + service3.length + service4.length);
			addToList(list, service1);
			addToList(list, service2);
			addToList(list, service3);
			addToList(list, service4);

			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static Feature[] retainTelephonyDisabledConflicts(Feature[] feature) {
			//Feature[] feature1 = removeBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			//Feature[] feature2 = removeBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			Feature[] feature1 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			Feature[] feature2 = retainBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			Feature[] feature3 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_WAITING);
			Feature[] feature4 = retainBySwitchCode(feature, Feature.SWITCH_CODE_FAX_MAIL);


			List<Feature> list = new ArrayList<Feature>(feature1.length + feature2.length + feature3.length + feature4.length);
			addToList(list, feature1);
			addToList(list, feature2);
			addToList(list, feature3);
			addToList(list, feature4);

			feature = (Feature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}

		public static ContractService[] retainTelephonyDisabledConflicts(ContractService[] service) throws TelusAPIException {
			//ContractService[] service1 = removeBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			//ContractService[] service2 = removeBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			ContractService[] service1 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_FORWARDING);
			ContractService[] service2 = retainBySwitchCode(service, Feature.SWITCH_CODE_VOICE_MAIL);
			ContractService[] service3 = retainBySwitchCode(service, Feature.SWITCH_CODE_CALL_WAITING);
			ContractService[] service4 = retainBySwitchCode(service, Feature.SWITCH_CODE_FAX_MAIL);


			List<ContractService> list = new ArrayList<ContractService>(service1.length + service2.length + service3.length + service4.length);
			addToList(list, service1);
			addToList(list, service2);
			addToList(list, service3);
			addToList(list, service4);

			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}


		public static ContractFeature[] retainTelephonyDisabledConflicts(ContractFeature[] feature) {
			//ContractFeature[] feature1 = removeBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			//ContractFeature[] feature2 = removeBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			ContractFeature[] feature1 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_FORWARDING);
			ContractFeature[] feature2 = retainBySwitchCode(feature, Feature.SWITCH_CODE_VOICE_MAIL);
			ContractFeature[] feature3 = retainBySwitchCode(feature, Feature.SWITCH_CODE_CALL_WAITING);
			ContractFeature[] feature4 = retainBySwitchCode(feature, Feature.SWITCH_CODE_FAX_MAIL);


			List<ContractFeature> list = new ArrayList<ContractFeature>(feature1.length + feature2.length + feature3.length + feature4.length);
			addToList(list, feature1);
			addToList(list, feature2);
			addToList(list, feature3);
			addToList(list, feature4);

			feature = (ContractFeature[])java.lang.reflect.Array.newInstance(feature.getClass().getComponentType(), list.size());
			return list.toArray(feature);
		}





		public static ContractService[] retainAdditionalPhoneNumbers(ContractService[] service) throws TelusAPIException {
			List<ContractService> list = new ArrayList<ContractService>(service.length);

			for (ContractService o : service) {
				if(o.getService().isAdditionalNumberRequired()) {
					list.add(o);
				}
			}
			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static ContractService[] retainParameterRequired(ContractService[] service) throws TelusAPIException {
			List<ContractService> list = new ArrayList<ContractService>(service.length);

			for (ContractService o : service) {
				if(o.getService().isParameterRequired()) {
					list.add(o);
				}
			}
			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return list.toArray(service);
		}

		public static void sort(Reference[] references, int language) {
			if(language == LANGUAGE_ENGLISH) {
				Arrays.sort(references, REFERENCE_COMPARATOR_ENGLISH);
			} else {
				Arrays.sort(references, REFERENCE_COMPARATOR_FRENCH);
			}
		}

		public static Reference findReferenceByCode(Reference[] references, String code) {
			for (Reference r : references) {
				if(code.equals(r.getCode())) {
					return r;
				}
			}
			return null;
		}

		/**
		 * @deprecated for better performance use retainByPrivilege method on ReferenceDataManager interface
		 *
		 */
		@Deprecated
		public static ServiceSummary[] retainByPrivilege(ServiceSummary[] service, String businessRole, String privilege) throws TelusAPIException {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				if(o.containsPrivilege(businessRole, privilege)) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (ServiceSummary[])list.toArray(service);
		}

		/**
		 * @deprecated for better performance use removeByPrivilege method on ReferenceDataManager interface
		 *
		 */
		@Deprecated
		public static ServiceSummary[] removeByPrivilege(ServiceSummary[] service, String businessRole, String privilege) throws TelusAPIException {
			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary element : service) {
				if(!element.containsPrivilege(businessRole, privilege)) {
					list.add(element);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (ServiceSummary[])list.toArray(service);
		}

		public static CoverageRegion[] filterCoverageRegions(CoverageRegion[] coverageRegionList, String provinceCode, String[] coverageTypes) {
			List<CoverageRegion> list = new ArrayList<CoverageRegion>(coverageRegionList.length);
			if (coverageTypes == null) {
				return filterCoverageRegions(coverageRegionList, provinceCode);
			} else if (coverageTypes.length == 0) {
				return filterCoverageRegions(coverageRegionList, provinceCode);
			} else {
				if (Province.PROVINCE_AB.equals(provinceCode)) {
					for (int n = 0; n < coverageTypes.length; n++) {
						if (coverageTypes[n].equals(CoverageRegion.COVERAGE_TYPE_LOCAL)) {
							coverageTypes[n] = CoverageRegion.COVERAGE_TYPE_WIDE;
						}
					}
				}
				boolean matchFound;
				for (CoverageRegion element : coverageRegionList) {
					matchFound = false;
					for (int j = 0; (j < coverageTypes.length) && (!matchFound); j++) {
						if ((element.getProvinceCode().equals(provinceCode)) && (element.getType().equals(coverageTypes[j]))) {
							list.add(element);
							matchFound = true;// only 1 match will be found.
						}
					}
				}
				coverageRegionList = (CoverageRegion[]) java.lang.reflect.Array.newInstance(coverageRegionList.getClass().getComponentType(), list.size());
				return (CoverageRegion[]) list.toArray(coverageRegionList);
			}
		}

		public static CoverageRegion[] filterCoverageRegions(CoverageRegion[] coverageRegionList, String provinceCode) {
			List<CoverageRegion> list = new ArrayList<CoverageRegion>(coverageRegionList.length);

			for (CoverageRegion element : coverageRegionList) {
				if (element.getProvinceCode().equals(provinceCode)) {
					list.add(element);
				}
			}

			coverageRegionList = (CoverageRegion[]) java.lang.reflect.Array.newInstance(coverageRegionList.getClass().getComponentType(), list.size());
			return (CoverageRegion[]) list.toArray(coverageRegionList);
		}

		public static PricePlanSummary[] filterPricePlans(PricePlanSummary[] pricePlanList, boolean rental) {
			List<PricePlanSummary> list = new ArrayList<PricePlanSummary>(pricePlanList.length);
			boolean matchFound;
			for (PricePlanSummary element : pricePlanList) {
				matchFound = false;
				for (int j = 0; (j < element.getFeatures().length) && (!matchFound); j++) {
					if (((element.getFeatures()[j].getCode()).equals(ReferenceDataManager.FEATURE_CODE_RENTAL) && (rental))
							|| ((element.getFeatures()[j].getCode()).equals(ReferenceDataManager.FEATURE_CODE_COAM) && (!rental))) {
						list.add(element);
						matchFound = true;// only 1 match will be found.
					}
				}
			}
			pricePlanList = (PricePlanSummary[]) java.lang.reflect.Array.newInstance(pricePlanList.getClass().getComponentType(), list.size());
			return (PricePlanSummary[]) list.toArray(pricePlanList);
		}

		@Deprecated
		public static Service[] filterPagerPakServices(Service[] serviceList) {
			List<Service> list = new ArrayList<Service>(serviceList.length);

			for (Service element : serviceList) {
				if (element.getCoverageType() != null) {
					if ((element.getCoverageType().equals(CoverageRegion.COVERAGE_TYPE_LOCAL)) || (element.getCoverageType().equals(CoverageRegion.COVERAGE_TYPE_WIDE))) {
						list.add(element);
					}
				}

			}
			serviceList = (Service[]) java.lang.reflect.Array.newInstance(serviceList.getClass().getComponentType(), list.size());
			return (Service[]) list.toArray(serviceList);
		}

		/**
		 * @deprecated see other retainServices methods. This method should NOT be called for HSPA subscribers.
		 * @param service ServiceSummary[]
		 * @param equipmentType String
		 * @param provinceCode String
		 * @return ServiceSummary[]
		 * @throws TelusAPIException
		 */
		@Deprecated
		public static ServiceSummary[] retainServices(ServiceSummary[] service, String equipmentType, String provinceCode) throws TelusAPIException {
			final String EQUIPMENT_TYPE_ANY = "9";
			//final String PROVINCE_ANY = "ALL";

			List<ServiceSummary> list = new ArrayList<ServiceSummary>(service.length);

			for (ServiceSummary o : service) {
				//if((contains(o.getEquipmentTypes(), EQUIPMENT_TYPE_ANY) || contains(o.getEquipmentTypes(), equpmentType))
				//  && (contains(o.getProvinces(), PROVINCE_ANY) || contains(o.getProvinces(), provinceCode))) {
				if(contains(o.getEquipmentTypes(), EQUIPMENT_TYPE_ANY) || contains(o.getEquipmentTypes(), equipmentType)) {
					list.add(o);
				}
			}
			service = (ServiceSummary[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (ServiceSummary[])list.toArray(service);
		}

		public static ContractService[] retrieveNonMatchingServices(ContractService[] service, Equipment equipment, MuleEquipment mule) throws TelusAPIException {
			if (equipment.isHSPA()) {
				return service; //There's no need to remove non-matching equipment type services for HSPA
			}

			List<ContractService> list = new ArrayList<ContractService>(service.length);

			for (ContractService o : service) {
				// centralize the rule for SOC equipmentType/networkType check rule:
				// comment out the following code to use
				// ServiceSummary.isNetwokrEquipmentTypeCompatible(equipment)
				//if((contains(o.getService().getEquipmentTypes(equipment.getNetworkType()), EQUIPMENT_TYPE_ANY) || contains(o.getService().getEquipmentTypes(equipment.getNetworkType()), equipment.getEquipmentType()))) {
				if ( o.getService().isNetworkEquipmentTypeCompatible( equipment )) {
					System.err.println(">>>> Service supports equipment type");
					if (o.getService().isRIM())  {
						System.err.println(">>>> Service is RIM");
						if( equipment.isRIM() || (mule != null && mule.isIDENRIM())) {
							System.err.println(">>>> Equipment is RIM too, remove Service from the list");
						}
						else {
							System.err.println(">>>> Equipment is NOT RIM, add Service to the list");
							list.add(o);
						}
					}
					else {
						System.err.println(">>>> Service is not RIM, always remove Service");
					}
				}
				else {
					list.add(o);
					System.err.println(">>>> Service does not support equipment type, add it");
				}
			}
			service = (ContractService[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (ContractService[])list.toArray(service);
		}
		/*
		 * Filters out expired recurring credits
		 */

		public static AdjustmentReason[] retainAdjustmentReasons(AdjustmentReason[] reasons) {
			List<AdjustmentReason> list = new ArrayList<AdjustmentReason>();
			Date now = new Date();
			Date expiryDate;

			for (AdjustmentReason reason : reasons) {
				if (reason.isRecurring()) {
					expiryDate = reason.getExpiryDate();
					if ((expiryDate != null && now.before(expiryDate)) || expiryDate == null) {
						list.add(reason);
					}
				} else {
					list.add(reason);
				}
			}

			return (AdjustmentReason[]) list.toArray(new AdjustmentReason[list.size()]);
		}

		public static ContractService[] retainServices(ContractService[] contractServices, Equipment equipment) throws TelusAPIException {

			List<Service> serviceList = new ArrayList<Service>(contractServices.length);

			for (ContractService contractService : contractServices) {
				serviceList.add(contractService.getService());
			}

			Service[] services = (Service[])serviceList.toArray(new Service[serviceList.size()]);
			services = retainServices(services, equipment);

			if (contractServices.length == services.length) {
				return contractServices;
			}

			List<ContractService> contractServiceList = new ArrayList<ContractService>(services.length);

			for (ContractService contractService : contractServices) {
				for (Service service : services) {
					if (contractService.getService().getCode().equals(service.getCode())) {
						contractServiceList.add(contractService);
						break;
					}
				}
			}

			contractServices = (ContractService[])java.lang.reflect.Array.newInstance(contractServices.getClass().getComponentType(), contractServiceList.size());
			return (ContractService[])contractServiceList.toArray(contractServices);
		}

		/**
		 * Refactored from the old retainServices (retainServicesForPreHSPA). This is used to control which respective retainServices method to invoke
		 *
		 * @param services
		 * @param equipment
		 * @return Service[]
		 * @throws TelusAPIException
		 */
		public static Service[] retainServices(Service[] services, Equipment equipment) throws TelusAPIException {
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
		public static Service[] retainServicesForPreHSPA(Service[] service, Equipment equipment) throws TelusAPIException {
			List<Service> list = new ArrayList<Service>(service.length);
			MuleEquipment mule = null;
			if (equipment.isSIMCard()) {
				mule = ((SIMCardEquipment)equipment).getLastMule();
			}else if (equipment instanceof MuleEquipment) {
				mule = (MuleEquipment) equipment;
			}

			for (Service o : service) {
				boolean isPricePlan = "P".equals( o.getServiceType() );

				/***** Updated for Combo plan CR- Anitha Duraisamy - start ****/
				if (o.isRIM()) { // Service/PricePlan is RIM, check
					// Equipment
					if (mule != null && mule.isIDENRIM()) {
						list.add(o);
					} else if(!equipment.isIDEN()){
						String[] equipmentTypes=o.getEquipmentTypes(equipment.getNetworkType());
						for (String equipmentType : equipmentTypes) {
							if(equipmentType.equals(equipment.getEquipmentType()) || equipmentType.equals(Equipment.EQUIPMENT_TYPE_ALL)){
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
					if (equipment.isVistoCapable()) {
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
					String[] equipmentTypes=	o.getEquipmentTypes(equipment.getNetworkType());
					for (String equipmentType : equipmentTypes) {
						if(equipmentType.equals(equipment.getEquipmentType()) || equipmentType.equals(Equipment.EQUIPMENT_TYPE_ALL)){
							list.add(o);
							break;
						}

					}
				} else { // Service/PricePlan is not RIM, always add Service/PricePlan
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (Service[])list.toArray(service);
		}

		/**
		 * The legacy retainServices equivalent for HSPA logic. The logic here applies for HSPA only. If it is
		 * not a HSPA equipment (i.e. invoked by accident), do nothing.
		 * @param services
		 * @param equipment
		 * @return Service[]
		 * @throws TelusAPIException
		 */
		public static Service[] retainServicesForHSPA(Service[] services, Equipment equipment) throws TelusAPIException {
			if (services == null || equipment == null || equipment.isHSPA() == false) {
				return services;
			}

			List<Service> list = new ArrayList<Service>(services.length);
			for (Service service : services) {

				list.add(service);
			}
			services = (Service[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
			return (Service[])list.toArray(services);
		}
		/**
		 * Retails service by network type, assuming the product type matches.
		 * @param service
		 * @param networkType
		 * @return Service[]
		 */
		public static Service[] retainServices (Service[] service, String networkType) {
			if (networkType == null || "".equals(networkType)) {
				return service;
			}

			List<Service> list = new ArrayList<Service>(service.length);

			for (Service o : service) {
				boolean isPricePlan = "P".equals( o.getServiceType() );

				if ( isPricePlan || o.isCompatible(networkType)) {
					list.add(o);
				}
			}
			service = (Service[])java.lang.reflect.Array.newInstance(service.getClass().getComponentType(), list.size());
			return (Service[])list.toArray(service);
		}

		/**
		 * Retain service by network type and equipmentType combination, assuming the product type matches.
		 * @param services
		 * @param networkType   - the networkType of the equipment
		 * @param equipmentType - this should be the equipmentType of handset, not the SIM card. If the equipmentType is USIMCard
		 *                        then this method will use PDA as equipmentType for checking.
		 * @return Service[]
		 */
		public static Service[] retainServicesByNetworkAndEquipmentType (Service[] services, String networkType, String equipmentType) {
			if (networkType == null || "".equals(networkType)) {
				return services;
			}

			List<Service> list = new ArrayList<Service>(services.length);

			for (Service o : services) {
				if ( o.isCompatible(networkType, equipmentType )) {
					list.add(o);
				}
			}
			services = (Service[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
			return (Service[])list.toArray(services);
		}

		/**
		 * Retain services based on equipment's network and equipment type
		 * @param services
		 * @param equipment
		 * @return Service[]
		 */
		public static Service[] retainServicesByNetworkAndEquipmentType (Service[] services, Equipment equipment) throws TelusAPIException {
			if (services == null || equipment == null) {
				return services;
			}

			List<Service> list = new ArrayList<Service>(services.length);
			for (Service o : services) {
				if ( o.isNetworkEquipmentTypeCompatible(equipment)) {
					list.add(o);
				}
			}
			services = (Service[])java.lang.reflect.Array.newInstance(services.getClass().getComponentType(), list.size());
			return (Service[])list.toArray(services);
		}

		public static PricePlanSummary[] retainPricePlansByTerm(PricePlanSummary[] pricePlans, int termInMonths) throws TelusAPIException {
			List<PricePlanSummary> list = new ArrayList<PricePlanSummary>(pricePlans.length);

			for (PricePlanSummary o : pricePlans) {
				if(contains(o.getAvailableTermsInMonths(), termInMonths)) {
					list.add(o);
				}
			}
			pricePlans = (PricePlanSummary[])java.lang.reflect.Array.newInstance(pricePlans.getClass().getComponentType(), list.size());
			return (PricePlanSummary[])list.toArray(pricePlans);
		}

		public static Reference[] union(Reference[] list1, Reference[] list2) {
			// TODO: optimize
			HashMap<String, Reference> resultMap = new HashMap<String, Reference>(list1.length + list2.length);
			HashMap<String, Reference> map1 = toHashMap(list1);
			HashMap<String, Reference> map2 = toHashMap(list2);

			if (!map1.isEmpty()) {
				addAll(resultMap, map1);
			}

			if (!map2.isEmpty()) {
				addAll(resultMap, map2);
			}

			return mapToArray(list1.getClass().getComponentType(), resultMap);
		}

		public static Reference[] intersection(Reference[] list1, Reference[] list2) {
			// TODO: optimize
			HashMap<String, Reference> resultMap = new HashMap<String, Reference>(list1.length + list2.length);
			HashMap<String, Reference> map1 = toHashMap(list1);
			HashMap<String, Reference> map2 = toHashMap(list2);

			if ( !map1.isEmpty() ) {
				addAll(resultMap, map1);
			}

			if ( !resultMap.isEmpty() ) {
				retainAll(resultMap, map2);
			}

			return mapToArray(list1.getClass().getComponentType(), resultMap);
		}

		public static Reference[] difference(Reference[] list1, Reference[] list2) {
			// TODO: optimize
			HashMap<String, Reference> resultMap = new HashMap<String, Reference>(list1.length + list2.length);
			HashMap<String, Reference> map1 = toHashMap(list1);
			HashMap<String, Reference> map2 = toHashMap(list2);

			if ( !map1.isEmpty() ) {
				addAll(resultMap, map1);
			}

			if ( !resultMap.isEmpty() ) {
				removeAll(resultMap, map2);
			}

			return mapToArray(list1.getClass().getComponentType(), resultMap);
		}

		private static boolean contains(Object[] list, Object object) {
			for (Object element : list) {
				if (element.equals(String.valueOf(object))) {
					return true;
				}
			}
			return false;
		}

		private static boolean contains(int[] list, int object) {
			for (int element : list) {
				if (element == object) {
					return true;
				}
			}
			return false;
		}

		private static void addToList(List list, Object[] objectArray) {
			for (Object object : objectArray) {
				if (!list.contains(object)) {
					list.add(object);
				}
			}
		}

		private static HashMap<String, Reference> toHashMap(Reference[] list) {
			HashMap<String, Reference> map = new HashMap<String, Reference>(list.length * 2);
			for (Reference element : list) {
				map.put(element.getCode(), element);
			}
			return map;
		}

		private static void addAll(HashMap map1, HashMap map2) {
			map1.putAll(map2);
		}

		private static void retainAll(HashMap map1, HashMap map2) {
			Iterator e = map1.keySet().iterator();
			while (e.hasNext()) {
				Object key = e.next();
				if(!map2.containsKey(key)) {
					e.remove();
					//map1.remove(key);
				}
			}
		}

		private static void removeAll(HashMap map1, HashMap map2) {
			Iterator e = map1.keySet().iterator();
			while (e.hasNext()) {
				Object key = e.next();
				if (map2.containsKey(key)) {
					e.remove();
					//map1.remove(key);
				}
			}
		}

		private static Reference[] mapToArray(Class componentType, Map map) {
			Reference[] result = (Reference[])java.lang.reflect.Array.newInstance(componentType, map.size());
			return (Reference[])map.values().toArray(result);
		}

		public static Contract enableDispatchFeatures(Contract contract) throws TelusAPIException {

			RatedFeature[] ppFeatures 	 = contract.getPricePlan().getFeatures();
			Service[] ppIncludedServices = contract.getPricePlan().getIncludedServices();

			// find missing dispatch pp features and add them if necessary
			for (int i=0; i < ppFeatures.length; i++) {
				if (ppFeatures[i].isDispatch() &&
						!contract.containsPricePlanFeature(ppFeatures[i].getCode()) ) {
					contract.addFeature(ppFeatures[i]);
				}
			}
			// find missing dispatch pp features and add them if necessary
			for (int i=0; i < ppIncludedServices.length; i++) {
				if (ppIncludedServices[i].isDispatchFeaturesIncluded() &&
						!contract.containsService(ppIncludedServices[i].getCode()) ) {
					contract.addService(ppIncludedServices[i]);
				}
			}
			return contract;
		}
		public static Contract disableDispatchFeatures(Contract contract) throws TelusAPIException {

			ContractFeature[] contractFeatures = contract.getFeatures(false);
			ContractService[] contractIncludedServices = contract.getIncludedServices();
			ContractService[] contractOptionalServices = contract.getOptionalServices();

			// find dispatch pp features and remove them
			for (ContractFeature contractFeature : contractFeatures) {
				if(contractFeature.getFeature().isDispatch()) {
					contract.removeFeature(contractFeature.getFeature().getCode());
				}
			}
			// find dispatch included services and remove them
			for (ContractService contractIncludedService : contractIncludedServices) {
				if(contractIncludedService.getService().isDispatchFeaturesIncluded()) {
					contract.removeService(contractIncludedService.getService().getCode());
				}
			}
			// find dispatch optional services and remove them
			// - unless they are bound or promotional services
			for (int i = 0; i < contractOptionalServices.length; i++) {
				if (contractOptionalServices[i].getService().isDispatchFeaturesIncluded()) {
					if (!contractOptionalServices[i].getService().isBoundService() &&
							!contractOptionalServices[i].getService().isPromotion() &&
							!contractOptionalServices[i].getService().isSequentiallyBoundService()) {
						contract.removeService(contractOptionalServices[i].getService().getCode());
					}
				}
			}
			return contract;
		}


	}

	public static class ReferenceComparator implements Comparator {
		private final int language;

		public ReferenceComparator(int language) {
			this.language = language;
		}

		@Override
		public int compare(Object o1, Object o2) {
			Reference r1 = (Reference)o1;
			Reference r2 = (Reference)o2;

			if(r1 == r2) {
				return 0;
			} else if(r1 == null) {
				return -1;
			} else if(r2 == null) {
				return 1;
			} else if(language == LANGUAGE_ENGLISH) {
				return r1.getDescription().compareToIgnoreCase(r2.getDescription());
			} else {
				Collator collator = Collator.getInstance( Locale.FRENCH );
				return collator.compare(r1.getDescriptionFrench().toLowerCase(Locale.FRENCH), r2.getDescriptionFrench().toLowerCase(Locale.FRENCH));
				//return r1.getDescriptionFrench().compareToIgnoreCase(r2.getDescriptionFrench());
			}
		}

		@Override
		public boolean equals(Object o) {
			return this == o;
		}
	}


	SpecialNumber getSpecialNumber( String phoneNumber)  throws TelusAPIException;

	SpecialNumberRange getSpecialNumberRange( String phoneNumber)  throws TelusAPIException;

	PrepaidCategory[] getWPSCategories() throws TelusAPIException;
	PrepaidCategory getWPSCategory(String code) throws TelusAPIException;

	PoolingGroup[] getPoolingGroups() throws TelusAPIException;
	PoolingGroup getPoolingGroup(String code) throws TelusAPIException;

	VendorService[] getVendorServices() throws TelusAPIException;
	VendorService getVendorService(String vendorServiceCode) throws TelusAPIException;

	ServiceRequestNoteType getServiceRequestNoteType(long noteTypeId) throws TelusAPIException;
	ServiceRequestNoteType[] getServiceRequestNoteTypes() throws TelusAPIException;

	ServiceRequestRelationshipType getServiceRequestRelationshipType(long relationshipTypeId) throws TelusAPIException;
	ServiceRequestRelationshipType[] getServiceRequestRelationshipTypes() throws TelusAPIException;

	/**
	 * This method will return route object according to given criteria.
	 * @param switch_id
	 * @param route_id
	 * @return Route
	 * @throws TelusAPIException
	 */
	Route getRoute(String switch_id, String route_id) throws TelusAPIException;

	/**
	 * This method will return all route data.
	 * @return Route[]
	 * @throws TelusAPIException
	 */
	Route[] getRoutes() throws TelusAPIException;

	PrepaidRechargeDenomination [] getPrepaidRechargeDenominations(String rechargeType) throws TelusAPIException;
	NetworkType [] getNetworkTypes() throws TelusAPIException;
	HandsetRoamingCapability[] getRoamingCapability() throws TelusAPIException;

	/**
	 * This method will return prepaid rates information.
	 * @return PrepaidRateProfile[]
	 * @throws TelusAPIException
	 */
	PrepaidRateProfile[] getAllPrepaidRates() throws TelusAPIException;

	/**
	 * This method will return a list of prepaid rates information according to given criteria.
	 * @param rateId
	 * @return PrepaidRateProfile[]
	 * @throws TelusAPIException
	 */
	PrepaidRateProfile[] getPrepaidRatesbyRateId(int rateId) throws TelusAPIException;

	/**
	 * This method will return a list of prepaid rates information according to given criteria.
	 * @param rateId
	 * @param countryCode
	 * @return PrepaidRateProfile[]
	 * @throws TelusAPIException
	 */
	PrepaidRateProfile[] getPrepaidRates(int rateId, String countryCode) throws TelusAPIException;

	/**
	 * This method will return a list of prepaid rates information according to given criteria.
	 * @param appId
	 * @return PrepaidRateProfile[]
	 * @throws TelusAPIException
	 */
	PrepaidRateProfile[] getPrepaidRatesbyAppId(String appId) throws TelusAPIException;

	ServiceSummary[] removeByPrivilege(ServiceSummary[] service, String businessRole, String privilege) throws TelusAPIException;

	ServiceSummary[] retainByPrivilege(ServiceSummary[] service, String businessRole, String privilege) throws TelusAPIException;

	//Added for Charge paper bill by Anitha Duraisamy- Start

	/**
	 * Retrieves the one-time charge details for paper bill on the account.
	 *
	 * Any of the parameters passed can be null or empty, however there must be at
	 * least one parameter populated, otherwise will throw an IllegalArgumentException.
	 *
	 * @param brandId brand Id, can be zero for wildcard
	 * @param provinceCode province code, can be null or empty for wildcard
	 * @param accountType char account type, can be null (that is '\u0000') for wildcard
	 * @param accountSubType account sub-type, can be null (that is '\u0000') for wildcard
	 * @param segment String account GL segment, can be null or empty for wildcard
	 * @param invoiceSuppressionLevel can be null or empty for wildcard
	 *
	 * @return ChargeType a one-time charge type
	 * @throws TelusAPIException
	 */
	ChargeType getPaperBillChargeType(int brandId, String provinceCode, char accountType, char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusAPIException;

	//Added for Charge paper bill by Anitha Duraisamy- End

	/**
	 * Retrieve SOC group and service codes mapping information.
	 *
	 * @param  serviceGroupCode
	 * @return String[] list of soc as values.
	 * @throws TelusAPIException
	 **/
	String[] getServiceCodesByGroup(String serviceGroupCode) throws TelusAPIException;

	/**
	 * To filter out bill cycles base on passed in province.
	 *
	 * @param  billCycles
	 * @param  province
	 * @return BillCycle[]
	 * @throws TelusAPIException
	 **/
	BillCycle[] removeBillCyclesByProvince(BillCycle[] billCycles,  String province) throws TelusAPIException;

	/**
	 * Returns a list of all possible data sharing groups configured in the system.
	 *
	 * @return DataSharingGroup[]
	 * @throws TelusAPIException
	 **/
	public DataSharingGroup[] getDataSharingGroups() throws TelusAPIException;

	/**
	 * Returns a specific data sharing group given the code. If there is no data sharing
	 * group that matches code, this method returns null.
	 *
	 * @param  code
	 * @return DataSharingGroup
	 * @throws TelusAPIException
	 **/
	public DataSharingGroup getDataSharingGroup(String code) throws TelusAPIException;


	/**
	 * Indicates if a service and price plan are associated
	 * @param pricePlanCode
	 * @param serviceCode
	 * @return
	 * @throws TelusAPIException
	 */
	boolean isServiceAssociatedToPricePlan (String pricePlanCode, String serviceCode) throws TelusAPIException;

	/**
	 * Retrieve ServiceRelations by serviceCode
	 * @param serviceCode
	 * @return ServiceRelation[]
	 * @throws TelusAPIException
	 */
	public ServiceRelation[] getServiceRelations(String serviceCode) throws TelusAPIException ;
	/**
	 * Retrieve ServiceRelations by serviceCode and relationType
	 * @param serviceCode
	 * @param relationType
	 * @return ServiceRelation[]
	 * @throws TelusAPIException
	 */
	public ServiceRelation[] getServiceRelations(String serviceCode, String relationType) throws TelusAPIException ;

	/**
	 * validate if   accountType/accountSubType is eligible for PP&S services
	 *
	 * @return boolean
	 * @throws TelusException
	 */
	public boolean isPPSEligible(char accountType, char accountSubType) throws TelusAPIException;
}




