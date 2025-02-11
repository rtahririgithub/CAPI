package com.telus.eas.equipment.info;

/**
 * Title:       EquipmentInfo <p>
 * Descriptioncription:    stores an  Equipment information <p>
 *
 * Copyright (c) Telus Mobility Inc.<p>
 *
 * @author Ludmila Pomirche
 * @version 1.0
 *
 * Revisions:
 * Ludmila Pomirche     1.0     Initial Revision
 *
 */

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidWarrantyTransferException;
import com.telus.api.equipment.AnalogEquipment;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.CellularDigitalEquipmentUpgrade;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.equipment.IDENEquipment;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.equipment.OneRTTEquipment;
import com.telus.api.equipment.PCSEquipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.equipment.UIMCardEquipment;
import com.telus.api.equipment.Warranty;
import com.telus.api.reference.Brand;
import com.telus.api.reference.CoverageRegion;
import com.telus.api.reference.EquipmentMode;
import com.telus.api.reference.NetworkType;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.framework.info.Info;

public class EquipmentInfo extends Info implements IDENEquipment, AnalogEquipment, PCSEquipment, PagerEquipment, SIMCardEquipment, MuleEquipment, OneRTTEquipment, UIMCardEquipment {

	static final long serialVersionUID = 1L;

	public final static String FEATURE_MSBASED_USERPLANE = "LBS_MSBASED";
	public final static String EQUIPMENT_TYPE_ASSET_TAG = "T";
	public final static String PRODUCT_FEATURE_GPS = "GPS";
	public final static String PRODUCT_FEATURE_VOLTE = "VOLTE";
	public final static String PCSTECHTYPECLASS = "PCS";
	public final static String IDENTECHTYPECLASS = "MIKE";
	public final static String SIMTECHTYPECLASS = "SIM";
	public final static String ANALOGTECHTYPECLASS = "ANALOG";
	public final static String PAGERTECHTYPECLASS = "PAGER";
	public final static String FEATURE_PTT_CAPABLE = "PTT";
	public final static String FEATURE_SMS_CAPABLE = "TXTMSG";
	public final static String FEATURE_MMS_CAPABLE = "PCTMSG";
	public final static String[] FEATURE_EVDO_CAPABLE = { CellularDigitalEquipment.EVDO_TYPE_0, CellularDigitalEquipment.EVDO_TYPE_A };
	public final static String TECHNOLOGY_TYPE_1XRTT = "1RTT";
	public final static String TECHNOLOGY_TYPE_LTE = "LTE";
	public final static String TECHNOLOGY_TYPE_GSM = "GSM";
	// public final static String FEATURE_JAVADOWNLOAD_CAPABLE = "JAVADOWNLD";
	public final static String PRODUCT_CLASS_CODE_PDA = "PDA";
	public final static String PRODUCT_CLASS_CODE_WORLDPHONEHANDSET = "WHND";
	public final static String PRODUCT_CLASS_CODE_UIM = "UIM";
	public final static long PRODUCT_CATEGORY_ID_RUIM = 10001101;
	public final static String PRODUCT_CLASS_CODE_WORLDPHONE_RIM = "WRIM";
	public final static String PRODUCT_CLASS_CODE_RIM = "RIM";
	public final static String DUMMY_ESN_FOR_USIM = "100000000000000000";
	public final static String PRODUCT_CLASS_CODE_UIC_CARD = "USIM";
	public final static String PRODUCT_CLASS_CODE_HSPA_HANDSET = "HHND";
	public final static String PRODUCT_CLASS_CODE_HSPA_RIM = "HRIM";
	public final static String PRODUCT_CLASS_CODE_HSPA_PDA = "HPDA";
	public final static String PRODUCT_CLASS_CODE_HSPA_AIRCARD = "HAIR";
	public final static String PRODUCT_CLASS_CODE_HSPA_MODEM = "HMOD";
	// GHB 02/03/2010 - as of May Release the description in P3MS for code HROU has changed from 'HSPA Router' to 'HSPA Hub'
	public final static String PRODUCT_CLASS_CODE_HSPA_ROUTER = "HROU";
	public final static String PRODUCT_PREPOSTPAID_FLAG_PREPAID = "PRE";
	public final static String PRODUCT_PREPOSTPAID_FLAG_POSTPAID = "POST";

	private static HashSet HSPA_DEVICE_PRODUCT_CLASS_CODES = new HashSet();
	static {
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_UIC_CARD);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_HANDSET);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_RIM);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_PDA);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_AIRCARD);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_MODEM);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_ROUTER);
	}

	/*
	 * not use for now private static HashSet HSPA_HANDSET_PRODUCT_CLASS_CODES =
	 * new HashSet(); static {
	 * HSPA_HANDSET_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_HANDSET);
	 * HSPA_HANDSET_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_RIM);
	 * HSPA_HANDSET_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_PDA); }
	 */
	private static HashSet CARD_PRODUCT_CLASS_CODES = new HashSet();
	static {
		CARD_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_AIRCARD);
		CARD_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_MODEM);
		CARD_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_ROUTER);
		CARD_PRODUCT_CLASS_CODES.add("WMOD");
		CARD_PRODUCT_CLASS_CODES.add("WDM");
	}
	
	public String serialNumber = "";
	public String techType = "";
	public String productCode = "";
	public String productStatusCode = "";
	public String vendorName = "";
	public String vendorNo = "";
	public String sublock1 = "";
	public long productGroupTypeID;
	public String productGroupTypeCode = "";
	public String productGroupTypeDescription = "";
	public long productClassID;
	public String productClassCode = "";
	public String productClassDescription = "";
	public String lastMuleIMEI = "";
	public long providerOwnerID;
	public long equipmentStatusTypeID;
	public long equipmentStatusID;
	public long productTypeID;
	public String productTypeDescription = "";
	public boolean stolen;
	public int banID = 0;
	public boolean virtual;
	public String equipmentType = "";
	public String productName = "";
	public String productNameFrench = "";
	public String productGroupTypeDescriptionFrench = "";
	public String browserVersion = "";
	public String firmwareVersion = "";
	public boolean initialActivation;
	public long[] productPromoTypeList;
	public String PRLCode;
	public String PRLDescription = "";
	public String productTypeDescriptionFrench = "";
	public boolean prepaid;
	
	private String networkType = "";
	private boolean vistoCapable;
	private boolean previouslyActivated;
	private boolean availableUSIM;
	private EquipmentInfo associatedHandset;
	private String associatedHandsetIMEI;
	private ProfileInfo profile;
	private String modelType;
	private String[] firmwareVersionFeatureCodes;
	private long modeCode;
	private String modeDescription = "";
	private String phoneNumber = "";
	private double[] contractTermCredits;
	private String productType = "";
	private long productId;
	private String simProfileCd;
	private String equipmentTypeClass = "";
	private boolean legacy;
	private String capCode = "";
	private String formattedCapCode = "";
	private String possession = "";
	private CoverageRegion[] coverageRegion;
	private String currentCoverageRegionCode;
	private String PUKCode = "";
	private boolean primary = true;
	// replaced by string
	// private EncodingFormatInfo encodingFormat = new EncodingFormatInfo();
	private String encodingFormat = "";
	private String frequencyCode = "";
	private long shippedToLocation = 0;
	private String[] coverageRegionCodes;
	private String browserProtocol;
	private EquipmentInfo lastMuleEquipment;
	private Date equipmentStatusDate;
	private long productCategoryId;
	private String[] productFeatures;
	private boolean unscanned;
	private String rimpin;
	private int[] brandIds = { Brand.BRAND_ID_TELUS }; // default to TELUS
	private String equipmentGroup;
	private String lastAssociatedHandsetEventType;
	private String productPrePostpaidFlag;

	public EquipmentInfo() { }

	public String getSIMCardNumber() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public EquipmentMode[] getEquipmentModes() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String newSerialNumber) {
		serialNumber = newSerialNumber;
	}

	public String getTechType() {
		return techType;
	}

	public void setTechType(String newTechType) {
		techType = newTechType;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String newProductCode) {
		productCode = newProductCode;
	}

	public String getProductStatusCode() {
		return productStatusCode;
	}

	public void setProductStatusCode(String newProductStatusCode) {
		productStatusCode = newProductStatusCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String newVendorName) {
		vendorName = newVendorName;
	}

	public String getVendorNo() {
		return vendorNo;
	}

	public void setVendorNo(String newVendorNo) {
		vendorNo = newVendorNo;
	}

	public String getSublock1() {
		return sublock1;
	}

	public String getSublock() {
		return sublock1;
	}

	public void setSublock1(String newSublock1) {
		sublock1 = newSublock1;
	}

	public void setProductGroupTypeID(long newProductGroupTypeID) {
		productGroupTypeID = newProductGroupTypeID;
	}

	public long getProductGroupTypeID() {
		return productGroupTypeID;
	}

	public void setProductGroupTypeCode(String newProductGroupTypeCode) {
		productGroupTypeCode = newProductGroupTypeCode;
	}

	public String getProductGroupTypeCode() {
		return productGroupTypeCode;
	}

	public void setProductGroupTypeDescription(String newProductGroupTypeDescription) {
		productGroupTypeDescription = newProductGroupTypeDescription;
	}

	public String getProductGroupTypeDescription() {
		return productGroupTypeDescription;
	}

	public void setProductClassID(long newProductClassID) {
		productClassID = newProductClassID;
	}

	public long getProductClassID() {
		return productClassID;
	}

	public void setProductClassCode(String newProductClassCode) {
		productClassCode = newProductClassCode;
	}

	public String getProductClassCode() {
		return productClassCode;
	}

	public void setProductClassDescription(String newProductClassDescription) {
		productClassDescription = newProductClassDescription;
	}

	public String getProductClassDescription() {
		return productClassDescription;
	}

	public void setLastMuleIMEI(String newLastMuleIMEI) {
		lastMuleIMEI = newLastMuleIMEI;
	}

	public String getLastMuleIMEI() {
		return lastMuleIMEI;
	}

	public MuleEquipment getLastMule() throws TelusAPIException {
		return lastMuleEquipment;
	}

	public void setLastMule(MuleEquipment lastMule) {
		this.lastMuleEquipment = (EquipmentInfo) lastMule;
	}

	public EquipmentInfo getLastMule0() {
		return lastMuleEquipment;
	}

	public void setLastMule0(EquipmentInfo lastMule) {
		this.lastMuleEquipment = lastMule;
	}

	public void setProviderOwnerID(long newProviderOwnerID) {
		providerOwnerID = newProviderOwnerID;
	}

	public long getProviderOwnerID() {
		return providerOwnerID;
	}

	public void setEquipmentStatusTypeID(long newEquipmentStatusTypeID) {
		equipmentStatusTypeID = newEquipmentStatusTypeID;
	}

	public long getEquipmentStatusTypeID() {
		return equipmentStatusTypeID;
	}

	public void setEquipmentStatusID(long newEquipmentStatusID) {
		equipmentStatusID = newEquipmentStatusID;
	}

	public long getEquipmentStatusID() {
		return equipmentStatusID;
	}

	public void setProductTypeID(long newProductTypeID) {
		productTypeID = newProductTypeID;
	}

	public long getProductTypeID() {
		return productTypeID;
	}

	public void setProductTypeDescription(String newProductTypeDescription) {
		productTypeDescription = newProductTypeDescription;
	}

	public String getProductTypeDescription() {
		return productTypeDescription;
	}

	public void setStolen(boolean newStolen) {
		stolen = newStolen;
	}

	public boolean isStolen() {
		return stolen;
	}

	public boolean isIDEN() {
		return networkType.equals(NetworkType.NETWORK_TYPE_IDEN);
	}

	public boolean isAnalog() {
		return (getTechType() != null && getTechType().equals("ANA"));
	}

	/**
	 * @deprecated please use isPCSHandset
	 */
	public boolean isPCS() {
		return ((getTechType() != null && getTechType().equals("PCS")) && (getProductClassCode() != null && getProductClassCode().equals("HAND")));
	}

	public boolean isPCSHandset() {
		return ((getTechType() != null && getTechType().equals("PCS")) && (getProductClassCode() != null && getProductClassCode().equals("HAND")));
	}

	public boolean isPager() {
		return ((getTechType() != null && getTechType().equals("PAGE")) && (getProductClassCode() != null && getProductClassCode().equals("HAND")));
	}

	public boolean isCellular() {
		return (getProductType() != null && getProductType().equals("C"));
	}

	public boolean isSIMCard() {
		return ((getTechType() != null && getTechType().equals("mike")) && (getProductClassCode() != null && getProductClassCode().equals("SIM")));
	}

	public boolean isRIM() {
		return (isCellularRIM() || isIDENRIM());
	}

	public boolean isCellularRIM() {
		return ((getTechType() != null && getTechType().equals(TECHNOLOGY_TYPE_1XRTT) || getTechType().equals(TECHNOLOGY_TYPE_LTE))
				&& (getProductClassCode() != null && (getProductClassCode().equals(PRODUCT_CLASS_CODE_RIM) || getProductClassCode().equals(PRODUCT_CLASS_CODE_WORLDPHONE_RIM)))
				|| PRODUCT_CLASS_CODE_HSPA_RIM.equals(getProductClassCode()));
	}

	public boolean isIDENRIM() {
		return ((getTechType() != null && getTechType().equals("mike")) && (getProductClassCode() != null && getProductClassCode().equals("RMUL")));

	}

	public boolean isCellularDigital() {
		return ((getProductType() != null && getProductType().equals("C")) && (getEquipmentTypeClass() != null && getEquipmentTypeClass().equals("DIGITAL")));
	}

	public boolean isHandset() {
		return (getProductClassCode() != null && (getProductClassCode().equals("HAND") || getProductClassCode().equals(PRODUCT_CLASS_CODE_WORLDPHONEHANDSET)
				|| getProductClassCode().equals(PRODUCT_CLASS_CODE_HSPA_HANDSET)));
	}

	public void setBanID(int newBanID) {
		banID = newBanID;
	}

	public int getBanID() {
		return banID;
	}

	public long getShippedToLocation() {
		return shippedToLocation;
	}

	public void setShippedToLocation(long newShippedToLocation) {
		shippedToLocation = newShippedToLocation;
	}

	public boolean isInUse() {
		return banID != 0;
	}

	public boolean isInUseOnBan(int ban, boolean active) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isInUseOnAnotherBan(int ban, boolean active) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void setEquipmentType(String newEquipmentType) {
		equipmentType = newEquipmentType;
		if (modelType == null) {
			setModelType(equipmentType);
		}
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public String getModelType() {
		return modelType;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductNameFrench(String productNameFrench) {
		this.productNameFrench = productNameFrench;
	}

	public String getProductNameFrench() {
		return productNameFrench;
	}

	public void setProductGroupTypeDescriptionFrench(String productGroupTypeDescriptionFrench) {
		this.productGroupTypeDescriptionFrench = productGroupTypeDescriptionFrench;
	}

	public String getProductGroupTypeDescriptionFrench() {
		return productGroupTypeDescriptionFrench;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	public void setInitialActivation(boolean initialActivation) {
		this.initialActivation = initialActivation;
	}

	public boolean isInitialActivation() {
		return initialActivation;
	}

	public void setProductPromoTypeList(long[] productPromoTypeList) {
		this.productPromoTypeList = productPromoTypeList;
	}

	public long[] getProductPromoTypeList() {
		return productPromoTypeList;
	}

	public void setPRLCode(String PRLCode) {
		this.PRLCode = PRLCode;
	}

	public String getPRLCode() {
		return PRLCode;
	}

	public void setPRLDescription(String PRLDescription) {
		this.PRLDescription = PRLDescription;
	}

	public String getPRLDescription() {
		return PRLDescription;
	}

	public void setProductTypeDescriptionFrench(String productTypeDescriptionFrench) {
		this.productTypeDescriptionFrench = productTypeDescriptionFrench;
	}

	public String getProductTypeDescriptionFrench() {
		return productTypeDescriptionFrench;
	}

	public String getEquipmentModel() {
		return productTypeDescription;
	}

	public String getEquipmentModelFrench() {
		return productTypeDescriptionFrench;
	}

	public void setModeCode(long modeCode) {
		this.modeCode = modeCode;
	}

	public long getModeCode() {
		return modeCode;
	}

	public void setModeDescription(String modeDescription) {
		this.modeDescription = modeDescription;
	}

	public String getModeDescription() {
		return modeDescription;
	}

	public boolean is1xRTT() {
		// return (getTechType() == null ? false : getTechType().equals(TECHNOLOGY_TYPE_1XRTT) ? true : false);
		return TECHNOLOGY_TYPE_1XRTT.equals(getTechType());
	}

	public boolean is1xRTTCard() {
		// return (getTechType() == null ? false : getTechType().equals(TECHNOLOGY_TYPE_1XRTT) && (getProductClassCode().equals("WMOD") || getProductClassCode().equals("WDM")) ? true : false);
		return is1xRTT() && CARD_PRODUCT_CLASS_CODES.contains(getProductClassCode());
	}

	public boolean isMule() {
		return (getProductClassCode() == null ? false : getProductClassCode().equals("MULE") ? true : getProductClassCode().equals("RMUL") ? true : false);
	}

	public boolean isTelephonyEnabled() {
		return true;
	}

	public boolean isDispatchEnabled() {
		return ((getTechType() == null || getProductClassCode() == null) ? false : getTechType().equals("mike")	&& (getProductClassCode().equals("HAND") 
				|| getProductClassCode().equals("SIM")) ? true : false);
	}

	public boolean isWirelessWebEnabled() {
		return (getBrowserVersion() == null ? false : getBrowserVersion().equals("") ? false : true);
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Warranty getWarranty() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void change(IDENSubscriber subscriber, MuleEquipment destinationMuleEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) 
			throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void testChange(IDENSubscriber subscriber, MuleEquipment destinationMuleEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType) 
			throws TelusAPIException, InvalidWarrantyTransferException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void transferWarranty(IDENSubscriber subscriber, MuleEquipment destinationMuleEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, boolean repair)
			throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void testTransferWarranty(IDENSubscriber subscriber, MuleEquipment destinationMuleEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, boolean repair)
			throws TelusAPIException, InvalidWarrantyTransferException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public double[] getContractTermCredits() {
		return contractTermCredits;
	}

	public void setContractTermCredits(double[] contractTermCredits) {
		this.contractTermCredits = contractTermCredits;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductType() {
		return productType;
	}

	public void setEquipmentTypeClass(String equipmentTypeClass) {
		this.equipmentTypeClass = equipmentTypeClass;
	}

	public String getEquipmentTypeClass() {
		return equipmentTypeClass;
	}

	public void setLegacy(boolean legacy) {
		this.legacy = legacy;
	}

	public boolean isLegacy() {
		return legacy;
	}

	public void setCapCode(String capCode) {
		this.capCode = capCode;
	}

	public String getCapCode() {
		return capCode;
	}

	public void setFormattedCapCode(String formattedCapCode) {
		this.formattedCapCode = formattedCapCode;
	}

	public String getFormattedCapCode() {
		return formattedCapCode;
	}

	public CoverageRegion[] getCoverageRegion() {
		return coverageRegion;
	}

	public void setCurrentCoverageRegionCode(String currentCoverageRegionCode) {
		this.currentCoverageRegionCode = currentCoverageRegionCode;
	}

	public String getCurrentCoverageRegionCode() {
		return currentCoverageRegionCode;
	}

	public String getEncodingFormat() {
		return encodingFormat;
	}

	public void setEncodingFormat(String encodingFormat) {
		this.encodingFormat = encodingFormat;
	}

	public void setPossession(String possession) {
		this.possession = possession;
	}

	public String getPossession() {
		return possession;
	}

	public void setFrequencyCode(String frequencyCode) {
		this.frequencyCode = frequencyCode;
	}

	public String getFrequencyCode() {
		return frequencyCode;
	}

	public boolean isPrepaid() {
		return prepaid;
	}

	public boolean isBoxed() {
		return (isPrepaid() && isInitialActivation());
	}

	public boolean isRental() {
		return (getPossession() != null && getPossession().equals("R"));
	}

	public void setCoverageRegionCodes(String[] coverageRegionCodes) {
		this.coverageRegionCodes = coverageRegionCodes;
	}

	public String[] getCoverageRegionCodes() {
		if (coverageRegionCodes == null) {
			return new String[0];
		}
		return coverageRegionCodes;
	}

	public void setPrepaid(boolean prepaid) {
		this.prepaid = prepaid;
	}

	public boolean isPTTEnabled() {

		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			return false;
		}

		String featureCode = null;
		for (int i = 0; i < firmwareVersionFeatureCodes.length; i++) {
			featureCode = firmwareVersionFeatureCodes[i];
			if (FEATURE_PTT_CAPABLE.equals(featureCode)) {
				return true;
			}
		}

		return false;
	}

	public String[] getFirmwareVersionFeatureCodes() {
		return firmwareVersionFeatureCodes;
	}

	public void setFirmwareVersionFeatureCodes(String[] firmwareVersionFeatureCodes) {
		this.firmwareVersionFeatureCodes = firmwareVersionFeatureCodes;
	}

	public void setBrowserProtocol(String browserProtocol) {
		this.browserProtocol = browserProtocol;
	}

	public String getBrowserProtocol() {
		return browserProtocol;
	}

	public boolean isSMSCapable() {
		
		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			System.err.println(">>>> firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0");
			return false;
		}

		String featureCode = null;
		for (int i = 0; i < firmwareVersionFeatureCodes.length; i++) {
			featureCode = firmwareVersionFeatureCodes[i];
			if (FEATURE_SMS_CAPABLE.equals(featureCode)) {
				return true;
			}
		}

		return false;
	}

	public CellularDigitalEquipmentUpgrade[] getCellularDigitalEquipmentUpgrades() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * Return true if the device is MMS capable.
	 * 
	 * @return boolean
	 */
	public boolean isMMSCapable() {
		
		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			return false;
		}

		String featureCode = null;
		for (int i = 0; i < firmwareVersionFeatureCodes.length; i++) {
			featureCode = firmwareVersionFeatureCodes[i];
			if (FEATURE_MMS_CAPABLE.equals(featureCode)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return Returns the equipmentStatusDate.
	 */
	public Date getEquipmentStatusDate() {
		return equipmentStatusDate;
	}

	public EquipmentSubscriber[] getAssociatedSubscribers(boolean active) throws TelusAPIException {
		return getAssociatedSubscribers(active, false);
	}

	public EquipmentSubscriber[] getAssociatedSubscribers(boolean active, boolean refresh) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * @param equipmentStatusDate
	 *            The equipmentStatusDate to set.
	 */
	public void setEquipmentStatusDate(Date equipmentStatusDate) {
		this.equipmentStatusDate = equipmentStatusDate;
	}

	/**
	 * Return true if the device is Java Download capable.
	 * 
	 * @return boolean
	 */
	/**
	 * public boolean isJavaDownloadCapable() { if (firmwareVersionFeatureCodes
	 * == null || firmwareVersionFeatureCodes.length == 0) {
	 * 
	 * System.err.println(">>>> firmwareVersionFeatureCodes == null ||
	 * firmwareVersionFeatureCodes.length == 0"); return false; }
	 * 
	 * String featureCode = null; for (int i = 0; i <
	 * firmwareVersionFeatureCodes.length; i++) { featureCode =
	 * firmwareVersionFeatureCodes[i]; if
	 * (FEATURE_JAVADOWNLOAD_ENABLED.equals(featureCode)) { return true; } }
	 * 
	 * return false; }
	 */
	
	/**
	 * @return Returns the pUKCode.
	 */
	public String getPUKCode() {
		return PUKCode;
	}

	/**
	 * @param code
	 *            The pUKCode to set.
	 */
	public void setPUKCode(String code) {
		PUKCode = code;
	}

	public boolean isPDA() {
		return PRODUCT_CLASS_CODE_PDA.equalsIgnoreCase(getProductClassCode()) || PRODUCT_CLASS_CODE_HSPA_PDA.equals(getProductClassCode())
				|| PRODUCT_CLASS_CODE_WORLDPHONEHANDSET.equals(getProductClassCode());
	}

	public boolean isEvDOCapable() {

		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			return false;
		}

		for (int i = 0; i < firmwareVersionFeatureCodes.length; i++) {
			if (Arrays.asList(FEATURE_EVDO_CAPABLE).contains(firmwareVersionFeatureCodes[i])) {
				return true;
			}
		}

		return false;
	}

	public boolean isDataCard() {
		return (getTechType() == null ? false : (getTechType().equals(TECHNOLOGY_TYPE_1XRTT) || getTechType().equals(TECHNOLOGY_TYPE_LTE)) && getProductClassCode().equals("WDM") ? true : false);
	}

	public boolean isVoiceCapable() {
		return false;
	}

	public boolean isDataCapable() {
		return Equipment.EQUIPMENT_TYPE_DATACARD.equalsIgnoreCase(getEquipmentType());
	}

	/**
	 * @return Returns the productCategoryId.
	 */
	public long getProductCategoryId() {
		return productCategoryId;
	}

	/**
	 * @param productCategoryId
	 *            The productCategoryId to set.
	 */
	public void setProductCategoryId(long productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public void updateStatus(long statusTypeId, long statusId) throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isWorldPhone() {
		return (getTechType() == null || getProductClassCode() == null ? false : getTechType().equals(TECHNOLOGY_TYPE_1XRTT) && (getProductClassCode().equals(PRODUCT_CLASS_CODE_WORLDPHONEHANDSET)
				|| getProductClassCode().equals(PRODUCT_CLASS_CODE_WORLDPHONE_RIM)) ? true : false);
	}

	public boolean isRUIMCard() {
		return (getTechType() == null || getProductClassCode() == null ? false : getTechType().equals(TECHNOLOGY_TYPE_GSM) && getProductClassCode().equals(PRODUCT_CLASS_CODE_UIM) 
				&& this.getProductCategoryId() == EquipmentInfo.PRODUCT_CATEGORY_ID_RUIM ? true : false);
	}

	public boolean isVirtual() {
		return virtual;
	}


	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	public boolean isVistoCapable() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/*
	 * public void setVistoCapable(boolean vistoCapable) { this.vistoCapable =
	 * vistoCapable; }
	 */

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public boolean isValidForPrepaid() {
		return !(is1xRTTCard() || isWorldPhone() || (isRIM() && isHSPA() == false) || (isEvDOCapable() && isDataCard())
				|| (isPDA() && isHSPA() == false));
	}

	public boolean isValidForPrepaidActivationWithoutPin() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * ProductFeatures is being set in TMEquipment, so please do not call getProductFeatures() or productFeatures in Info class directly.
	 * 
	 * @return String[]
	 */
	public String[] getProductFeatures() {
		return this.productFeatures;
	}

	public void setProductFeatures(String[] productFeatures) {
		this.productFeatures = productFeatures;
	}

	public boolean isGPS() {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

	public boolean isVoLTE() {
		throw new java.lang.UnsupportedOperationException("Method not implemented here");
	}

	public boolean isMSBasedEnabled() {
		
		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			return false;
		}

		String featureCode = null;
		for (int i = 0; i < firmwareVersionFeatureCodes.length; i++) {
			featureCode = firmwareVersionFeatureCodes[i];
			if (FEATURE_MSBASED_USERPLANE.equals(featureCode)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isAssetTag() {
		if (EQUIPMENT_TYPE_ASSET_TAG.equalsIgnoreCase(equipmentType) && getProductType().equals("C")) {
			return true;
		}
		return false;
	}

	public String getRIMPin() {
		return this.rimpin;
	}

	public void setRIMPin(String rimpin) {
		this.rimpin = rimpin;
	}

	public boolean isUnscanned() {
		return unscanned;
	}

	public void setUnscanned(boolean unscanned) {
		this.unscanned = unscanned;
	}

	public boolean isAvailableForActivation() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public int[] getBrandIds() {
		// Use a copy of the actual brand array to preserve the true values
		return excludeMVNEUSIMBrands();
	}
	
	// The elaborate logic implemented here serves two purposes - to ensure backward compatibility with existing functionality that is NOT related to the
	// Koodo Pre2Post project (i.e., the equipment brand transition matrix), and to move Koodo Prepaid brand masking logic out of the client_equipment_pkg
	// stored procedures and into (more maintainable) Java code. R. Fong, 20160809.
	// EasyP2P - Oct 2019 - we are enhancing the shared USIM functionality for other MVNE brands ( PC and Pubic Mobile brands)  to koodo postpaid,
    // so will exclude these brands for other quipment brand check flows and only allow for portIn activation flows.

	/**
	private int[] excludeKoodooPrepaidUSIMBrand() {
		int[] brandIds = Arrays.copyOf(getBrandIds0(), getBrandIds0().length);
		for (int i = 0; i < brandIds.length; i++) {
			// If this is a Koodo Prepaid USIM card, replace the Koodo brand ID with 'not applicable'
			if (isUSIMCard() && brandIds[i] == Brand.BRAND_ID_KOODO && PRODUCT_PREPOSTPAID_FLAG_PREPAID.equals(getProductPrePostpaidFlag())) {
				brandIds[i] = Brand.BRAND_ID_NOT_APPLICABLE;
			}
		}

		return brandIds;
	} **/
	
	private int[] excludeMVNEUSIMBrands() {
		int[] brandIds = Arrays.copyOf(getBrandIds0(), getBrandIds0().length);
		for (int i = 0; i < brandIds.length; i++) {
			if (isUSIMCard() && ( brandIds[i] == Brand.BRAND_ID_PC_MOBILE || brandIds[i] == Brand.BRAND_ID_PUBLIC_MOBILE ||
					(brandIds[i] == Brand.BRAND_ID_KOODO && PRODUCT_PREPOSTPAID_FLAG_PREPAID.equals(getProductPrePostpaidFlag())))) {
				brandIds[i] = Brand.BRAND_ID_NOT_APPLICABLE;
			}
		}
		return brandIds;
	}
	
	// Shadow method for getBrandIds() that returns the true (SEMS) brand values - eventually we want to replace getBrandIds() with its shadow method.
	public int[] getBrandIds0() {
		return brandIds;
	}

	public boolean isValidForBrand(int brandId) {
		// Look for the particular brand ID in the masked brand array
		for (int brand : getBrandIds()) {
			if (brand == brandId) {
				return true;
			}
		}
		
		return false;
	}
	
	// Shadow method for isValidForBrand() that validates all brand values, including Koodo Prepaid ,PC and Public Mobile USIMs  - eventually we want to replace isValidForBrand() with its shadow method.
	public boolean isValidForBrand0(int brandId) {
		// Look for the particular brand ID in the true brand array
		for (int brand : getBrandIds0()) {
			if (brand == brandId) {
				return true;
			}
		}

		return false;
	}

	public String toString() {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("EquipmentInfo[").append("\n");
		buffer.append("associatedHandsetIMEI = ").append(associatedHandsetIMEI).append("\n");
		buffer.append("banID = ").append(banID).append("\n");
		if (brandIds == null) {
			buffer.append(" brand id=null").append("\n");
		} else {
			buffer.append(" brand ids=[");
			for (int i = 0; i < brandIds.length; i++) {
				if (i != 0)
					buffer.append(", ");
				buffer.append(brandIds[i]);
			}
			buffer.append("]").append("\n");
		}
		buffer.append(" browserProtocol = ").append(browserProtocol).append("\n");
		buffer.append(" browserVersion = ").append(browserVersion).append("\n");
		buffer.append(" capCode = ").append(capCode).append("\n");
		if (contractTermCredits == null) {
			buffer.append(" contractTermCredits = ").append("null").append("\n");
		} else {
			buffer.append(" contractTermCredits = ").append("[");
			for (int i = 0; i < contractTermCredits.length; i++) {
				if (i != 0) {
					buffer.append(", ");
				}
				buffer.append(contractTermCredits[i]);
			}
			buffer.append("]").append("\n");
		}
		if (coverageRegion == null) {
			buffer.append(" coverageRegion = ").append("null").append("\n");
		} else {
			buffer.append(" coverageRegion = ").append(Arrays.asList(coverageRegion).toString()).append("\n");
		}
		if (coverageRegionCodes == null) {
			buffer.append(" coverageRegionCodes = ").append("null").append("\n");
		} else {
			buffer.append(" coverageRegionCodes = ").append(Arrays.asList(coverageRegionCodes).toString()).append("\n");
		}
		buffer.append(" currentCoverageRegionCode = ").append(currentCoverageRegionCode).append("\n");
		buffer.append(" encodingFormat = ").append(encodingFormat).append("\n");
		buffer.append(" equipmentStatusDate = ").append(equipmentStatusDate).append("\n");
		buffer.append(" equipmentStatusID = ").append(equipmentStatusID).append("\n");
		buffer.append(" equipmentStatusTypeID = ").append(equipmentStatusTypeID).append("\n");
		buffer.append(" equipmentType = ").append(equipmentType).append("\n");
		buffer.append(" equipmentTypeClass = ").append(equipmentTypeClass).append("\n");
		buffer.append(" firmwareVersion = ").append(firmwareVersion).append("\n");
		if (firmwareVersionFeatureCodes == null) {
			buffer.append(" firmwareVersionFeatureCodes = ").append("null").append("\n");
		} else {
			buffer.append(" firmwareVersionFeatureCodes = ")
					.append(Arrays.asList(firmwareVersionFeatureCodes).toString()).append("\n");
		}
		buffer.append(" formattedCapCode = ").append(formattedCapCode).append("\n");
		buffer.append(" frequencyCode = ").append(frequencyCode).append("\n");
		buffer.append(" initialActivation = ").append(initialActivation).append("\n");
		buffer.append(" lastMuleEquipment = ").append(lastMuleEquipment).append("\n");
		buffer.append(" lastMuleIMEI = ").append(lastMuleIMEI).append("\n");
		buffer.append(" legacy = ").append(legacy).append("\n");
		buffer.append(" modeCode = ").append(modeCode).append("\n");
		buffer.append(" modeDescription = ").append(modeDescription).append("\n");
		buffer.append(" modelType = ").append(modelType).append("\n");
		buffer.append(" phoneNumber = ").append(phoneNumber).append("\n");
		buffer.append(" possession = ").append(possession).append("\n");
		buffer.append(" prepaid = ").append(prepaid).append("\n");
		buffer.append(" primary = ").append(primary).append("\n");
		buffer.append(" PRLCode = ").append(PRLCode).append("\n");
		buffer.append(" PRLDescription = ").append(PRLDescription).append("\n");
		buffer.append(" productCategoryId = ").append(productCategoryId).append("\n");
		buffer.append(" productClassCode = ").append(productClassCode).append("\n");
		buffer.append(" productClassDescription = ").append(productClassDescription).append("\n");
		buffer.append(" productClassID = ").append(productClassID).append("\n");
		buffer.append(" productCode = ").append(productCode).append("\n");
		if (productFeatures == null) {
			buffer.append(" productFeatures = ").append("null").append("\n");
		} else {
			buffer.append(" productFeatures = ").append(Arrays.asList(productFeatures).toString()).append("\n");
		}
		buffer.append(" productGroupTypeCode = ").append(productGroupTypeCode).append("\n");
		buffer.append(" productGroupTypeDescription = ").append(productGroupTypeDescription).append("\n");
		buffer.append(" productGroupTypeDescriptionFrench = ").append(productGroupTypeDescriptionFrench).append("\n");
		buffer.append(" productGroupTypeID = ").append(productGroupTypeID).append("\n");
		buffer.append(" productName = ").append(productName).append("\n");
		buffer.append(" productNameFrench = ").append(productNameFrench).append("\n");
		if (productPromoTypeList == null) {
			buffer.append(" productPromoTypeList = ").append("null").append("\n");
		} else {
			buffer.append(" productPromoTypeList = ").append("[");
			for (int i = 0; i < productPromoTypeList.length; i++) {
				if (i != 0) {
					buffer.append(", ");
				}
				buffer.append(productPromoTypeList[i]);
			}
			buffer.append("]").append("\n");
		}
		buffer.append(" productStatusCode = ").append(productStatusCode).append("\n");
		buffer.append(" productType = ").append(productType).append("\n");
		buffer.append(" productTypeDescription = ").append(productTypeDescription).append("\n");
		buffer.append(" productTypeDescriptionFrench = ").append(productTypeDescriptionFrench).append("\n");
		buffer.append(" productTypeID = ").append(productTypeID).append("\n");
		buffer.append(" profile = ").append(profile).append("\n");
		buffer.append(" providerOwnerID = ").append(providerOwnerID).append("\n");
		buffer.append(" PUKCode = ").append(PUKCode).append("\n");
		buffer.append(" serialNumber = ").append(serialNumber).append("\n");
		buffer.append(" shippedToLocation = ").append(shippedToLocation).append("\n");
		buffer.append(" stolen = ").append(stolen).append("\n");
		buffer.append(" sublock1 = ").append(sublock1).append("\n");
		buffer.append(" techType = ").append(techType).append("\n");
		buffer.append(" unscanned = ").append(unscanned).append("\n");
		buffer.append(" vendorName = ").append(vendorName).append("\n");
		buffer.append(" vendorNo = ").append(vendorNo).append("\n");
		buffer.append(" virtual = ").append(virtual).append("\n");
		buffer.append(" vistoCapable = ").append(vistoCapable).append("\n");
		buffer.append(" RIMPin = ").append(rimpin).append("\n");
		buffer.append(" productPrePostpaidFlag = ").append(productPrePostpaidFlag).append("\n");
		buffer.append(" productId = ").append(productId).append("\n");
		buffer.append(" simProfileCd = ").append(simProfileCd).append("\n");
		buffer.append("]").append("\n");
		
		return buffer.toString();
	}

	public void setBrandIds(int[] brandIds) {
		this.brandIds = brandIds;
	}

	public String getCurrentEVDOType() {
		
		if (firmwareVersionFeatureCodes == null || firmwareVersionFeatureCodes.length == 0) {
			return null;
		}
		List firmwareFeaturesList = Arrays.asList(firmwareVersionFeatureCodes);

		if (firmwareFeaturesList.contains(CellularDigitalEquipment.EVDO_TYPE_A)) {
			return CellularDigitalEquipment.EVDO_TYPE_A;
		} else if (firmwareFeaturesList.contains(CellularDigitalEquipment.EVDO_TYPE_0)) {
			return CellularDigitalEquipment.EVDO_TYPE_0;
		} else {
			return null;
		}
	}

	public String getHighestEVDOType() {
		
		if (productFeatures == null || productFeatures.length == 0) {
			return null;
		}
		List productFeaturesList = Arrays.asList(productFeatures);

		if (productFeaturesList.contains(CellularDigitalEquipment.EVDO_TYPE_A)) {
			return CellularDigitalEquipment.EVDO_TYPE_A;
		} else if (productFeaturesList.contains(CellularDigitalEquipment.EVDO_TYPE_0)) {
			return CellularDigitalEquipment.EVDO_TYPE_0;
		} else {
			return null;
		}
	}

	public void change(IDENSubscriber subscriber, MuleEquipment destinationMuleEquipment, String dealerCode, String salesRepCode, String requestorId, String repairId, String swapType,
			ServiceRequestHeader header) throws TelusAPIException, InvalidWarrantyTransferException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isAvailableUSIM() {
		return availableUSIM;
	}

	public void setAvailableUSIM(boolean availableUSIM) {
		this.availableUSIM = availableUSIM;
	}

	public EquipmentInfo getAssociatedHandset() {
		return associatedHandset;
	}

	public void setAssociatedHandset(EquipmentInfo associatedHandset) {
		this.associatedHandset = associatedHandset;
		if (associatedHandset != null) {
			setAssociatedHandsetIMEI(associatedHandset.getSerialNumber());
		}
	}

	public String getAssociatedHandsetIMEI() {
		return associatedHandsetIMEI;
	}

	public void setAssociatedHandsetIMEI(String associatedHandsetIMEI) {
		this.associatedHandsetIMEI = associatedHandsetIMEI;
	}

	public ProfileInfo getProfile() {
		return profile;
	}

	public void setProfile(ProfileInfo profile) {
		this.profile = profile;
	}

	public void setNetworkType(String networkType) {
		if (networkType != null) {
			this.networkType = networkType;
		} else {
			networkType = "";
		}
	}

	public boolean isCDMA() {
		return networkType.equals(NetworkType.NETWORK_TYPE_CDMA) || networkType.equals("");
	}

	public boolean isGreyMarket(int brandId) {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isHSPA() {
		return networkType.equals(NetworkType.NETWORK_TYPE_HSPA);
	}

	public boolean isUSIMCard() {
		return PRODUCT_CLASS_CODE_UIC_CARD.equals(getProductClassCode());
	}

	public String getNetworkType() throws TelusAPIException {
		if (!networkType.equals("")) {
			return networkType;
		} else {
			return NetworkType.NETWORK_TYPE_CDMA;
		}
	}

	public int getRoamingCapability() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void reportFound() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void reportLost() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public void reportStolen() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isPreviouslyActivated() {
		return previouslyActivated;
	}

	public void setPreviouslyActivated(boolean previouslyActivated) {
		this.previouslyActivated = previouslyActivated;
	}

	public boolean isExpired() {
		return getEquipmentStatusTypeID() == 1 && getEquipmentStatusID() == 65;
	}

	public String getEquipmentGroup() {
		return equipmentGroup;
	}

	public void setEquipmentGroup(String equipmentGroup) {
		this.equipmentGroup = equipmentGroup;
	}

	public String getLastAssociatedHandsetEventType() {
		return lastAssociatedHandsetEventType;
	}

	public void setLastAssociatedHandsetEventType(String lastAssociatedHandsetEventType) {
		this.lastAssociatedHandsetEventType = lastAssociatedHandsetEventType;
	}

	/**
	 * @deprecated
	 */
	public boolean isDeviceProtectionEligible() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	/**
	 * @deprecated
	 */
	public boolean isApple() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isHSIADummyEquipment() {
		return Equipment.EQUIPMENT_TYPE_HSIA.equals(this.equipmentType);
	}

	public boolean isVOIPDummyEquipment() {
		return Equipment.EQUIPMENT_TYPE_VOIP.equals(this.equipmentType);
	}

	public String getProductPrePostpaidFlag() {
		return productPrePostpaidFlag;
	}

	public void setProductPrePostpaidFlag(String productPrePostpaidFlag) {
		this.productPrePostpaidFlag = productPrePostpaidFlag;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getSimProfileCd() {
		return simProfileCd;
	}

	public void setSimProfileCd(String simProfileCd) {
		this.simProfileCd = simProfileCd;
	}
	
}