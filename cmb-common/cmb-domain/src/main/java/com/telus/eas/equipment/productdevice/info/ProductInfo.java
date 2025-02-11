package com.telus.eas.equipment.productdevice.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class ProductInfo extends Info {

	static final long serialVersionUID = 1L;

	private ProductHeaderInfo product;
	private List<FeatureInfo> featureList;
	private String vendorName;
	private String vendorNumber;
	private String supportedSimType;
	private String deviceType;
	private String simProfileCode;
	private boolean evdoCapableInd;
	private boolean gpsInd;
	private boolean legacyInd;
	private boolean mmsCapableInd;
	private boolean msBasedEnabledInd;
	private boolean pttEnabledInd;
	private boolean smsCapableInd;
	private boolean telephonyEnabledInd;
	private boolean vistoCapableInd;
	private boolean wirelessWebEnabledInd;
	private boolean appleInd;
	
	public ProductInfo() { }

	public ProductHeaderInfo getProduct() {
		return product;
	}

	public void setProduct(ProductHeaderInfo product) {
		this.product = product;
	}

	public List<FeatureInfo> getFeatureList() {
		return featureList;
	}

	public void setFeatureList(List<FeatureInfo> featureList) {
		this.featureList = featureList;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public boolean isEvdoCapableInd() {
		return evdoCapableInd;
	}

	public void setEvdoCapableInd(boolean evdoCapableInd) {
		this.evdoCapableInd = evdoCapableInd;
	}

	public boolean isGpsInd() {
		return gpsInd;
	}

	public void setGpsInd(boolean gpsInd) {
		this.gpsInd = gpsInd;
	}

	public boolean isLegacyInd() {
		return legacyInd;
	}

	public void setLegacyInd(boolean legacyInd) {
		this.legacyInd = legacyInd;
	}

	public boolean isMmsCapableInd() {
		return mmsCapableInd;
	}

	public void setMmsCapableInd(boolean mmsCapableInd) {
		this.mmsCapableInd = mmsCapableInd;
	}

	public boolean isMsBasedEnabledInd() {
		return msBasedEnabledInd;
	}

	public void setMsBasedEnabledInd(boolean msBasedEnabledInd) {
		this.msBasedEnabledInd = msBasedEnabledInd;
	}

	public boolean isPttEnabledInd() {
		return pttEnabledInd;
	}

	public void setPttEnabledInd(boolean pttEnabledInd) {
		this.pttEnabledInd = pttEnabledInd;
	}

	public boolean isSmsCapableInd() {
		return smsCapableInd;
	}

	public void setSmsCapableInd(boolean smsCapableInd) {
		this.smsCapableInd = smsCapableInd;
	}

	public boolean isTelephonyEnabledInd() {
		return telephonyEnabledInd;
	}

	public void setTelephonyEnabledInd(boolean telephonyEnabledInd) {
		this.telephonyEnabledInd = telephonyEnabledInd;
	}

	public boolean isVistoCapableInd() {
		return vistoCapableInd;
	}

	public void setVistoCapableInd(boolean vistoCapableInd) {
		this.vistoCapableInd = vistoCapableInd;
	}

	public boolean isWirelessWebEnabledInd() {
		return wirelessWebEnabledInd;
	}

	public void setWirelessWebEnabledInd(boolean wirelessWebEnabledInd) {
		this.wirelessWebEnabledInd = wirelessWebEnabledInd;
	}

	public boolean isAppleInd() {
		return appleInd;
	}

	public void setAppleInd(boolean appleInd) {
		this.appleInd = appleInd;
	}

	public String getSupportedSimType() {
		return supportedSimType;
	}

	public void setSupportedSimType(String supportedSimType) {
		this.supportedSimType = supportedSimType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getSimProfileCode() {
		return simProfileCode;
	}

	public void setSimProfileCode(String simProfileCode) {
		this.simProfileCode = simProfileCode;
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("ProductInfo: {\n");
		s.append("    product=[").append(getProduct()).append("]\n");
		s.append("    featureList=[\n");
		for (FeatureInfo info : getFeatureList()) {
			s.append(info).append("\n");
		}
		s.append("    ]\n");
		s.append("    vendorName=[").append(getVendorName()).append("]\n");
		s.append("    vendorNumber=[").append(getVendorNumber()).append("]\n");
		s.append("    evdoCapableInd=[").append(isEvdoCapableInd()).append("]\n");
		s.append("    gpsInd=[").append(isGpsInd()).append("]\n");
		s.append("    legacyInd=[").append(isLegacyInd()).append("]\n");
		s.append("    mmsCapableInd=[").append(isMmsCapableInd()).append("]\n");
		s.append("    msBasedEnabledInd=[").append(isMsBasedEnabledInd()).append("]\n");
		s.append("    pttEnabledInd=[").append(isPttEnabledInd()).append("]\n");
		s.append("    smsCapableInd=[").append(isSmsCapableInd()).append("]\n");
		s.append("    telephonyEnabledInd=[").append(isTelephonyEnabledInd()).append("]\n");
		s.append("    vistoCapableInd=[").append(isVistoCapableInd()).append("]\n");
		s.append("    wirelessWebEnabledInd=[").append(isWirelessWebEnabledInd()).append("]\n");
		s.append("    appleInd=[").append(isAppleInd()).append("]\n");
		s.append("    deviceType=[").append(getDeviceType()).append("]\n");
		s.append("    supportedSimType[").append(getSupportedSimType()).append("]\n");
		s.append("    simProfileCode[").append(getSimProfileCode()).append("]\n");
		s.append("}");

		return s.toString();
	}

}