package com.telus.cmb.subscriber.decorators;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.equipment.Warranty;
import com.telus.api.reference.EquipmentMode;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;

public abstract class EquipmentDecorator {
	protected final EquipmentInfo delegate;

	protected EquipmentDecorator(EquipmentInfo equipment) {
		delegate = equipment;
	}

	public EquipmentInfo getDelegate() {
		return delegate;
	}

	public String getProductType() {
		return delegate.getProductType();
	}

	public String getSerialNumber() {
		return delegate.getSerialNumber();
	}

	public String getTechType() {
		return delegate.getTechType();
	}

	public String getProductCode() {
		return delegate.getProductCode();
	}

	public String getProductName() {
		return delegate.getProductName();
	}

	public String getProductNameFrench() {
		return delegate.getProductNameFrench();
	}

	public String getProductStatusCode() {
		return delegate.getProductStatusCode();
	}

	public String getVendorName() {
		return delegate.getVendorName();
	}

	public String getVendorNo() {
		return delegate.getVendorNo();
	}

	public boolean isStolen() {
		return delegate.isStolen();
	}

	public long getProductGroupTypeID() {
		return delegate.getProductGroupTypeID();
	}

	public String getProductGroupTypeCode() {
		return delegate.getProductGroupTypeCode();
	}

	public String getProductGroupTypeDescription() {
		return delegate.getProductGroupTypeDescription();
	}

	public String getProductGroupTypeDescriptionFrench() {
		return delegate.getProductGroupTypeDescriptionFrench();
	}

	public long getProductClassID() {
		return delegate.getProductClassID();
	}

	public String getProductClassCode() {
		return delegate.getProductClassCode();
	}

	public String getProductClassDescription() {
		return delegate.getProductClassDescription();
	}

	public long getProviderOwnerID() {
		return delegate.getProviderOwnerID();
	}

	public long getEquipmentStatusTypeID() {
		return delegate.getEquipmentStatusTypeID();
	}

	public long getEquipmentStatusID() {
		return delegate.getEquipmentStatusID();
	}

	public long getProductTypeID() {
		return delegate.getProductTypeID();
	}

	public String getProductTypeDescription() {
		return delegate.getProductTypeDescription();
	}

	public String getProductTypeDescriptionFrench() {
		return delegate.getProductTypeDescriptionFrench();
	}

	public boolean isIDEN() {
		return delegate.isIDEN();
	}

	public boolean isCellular() {
		return delegate.isCellular();
	}

	public boolean isCellularDigital() {
		return delegate.isCellularDigital();
	}

	public boolean isAnalog() {
		return delegate.isAnalog();
	}

	public boolean isPCS() {
		return delegate.isPCS();
	}

	public boolean isPCSHandset() {
		return delegate.isPCSHandset();
	}

	public boolean isPager() {
		return delegate.isPager();
	}

	public boolean is1xRTT() {
		return delegate.is1xRTT();
	}

	public boolean is1xRTTCard() {
		return delegate.is1xRTTCard();
	}

	public boolean isHandset() {
		return delegate.isHandset();
	}

	public boolean isSIMCard() {
		return delegate.isSIMCard();
	}

	public boolean isRIM() {
		return delegate.isRIM();
	}

	public boolean isCellularRIM() {
		return delegate.isCellularRIM();
	}

	public boolean isIDENRIM() {
		return delegate.isIDENRIM();
	}

	public boolean isRUIMCard() {
		return delegate.isRUIMCard();
	}

	public int getBanID() {
		return delegate.getBanID();
	}

	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}

	public boolean isInUse() {
		return delegate.isInUse();
	}

	public boolean isInUseOnBan(int ban, boolean active) throws TelusAPIException {
		return delegate.isInUseOnBan(ban, active);
	}

	public boolean isInUseOnAnotherBan(int ban, boolean active) throws TelusAPIException {
		return delegate.isInUseOnAnotherBan(ban, active);
	}

	public boolean isHSPA() {
		return delegate.isHSPA();
	}

	public boolean isCDMA() {
		return delegate.isCDMA();
	}

	public boolean isGreyMarket(int brandId) {
		return delegate.isGreyMarket(brandId);
	}

	public boolean isUSIMCard() {
		return delegate.isUSIMCard();
	}

	public String getEquipmentType() {
		return delegate.getEquipmentType();
	}

	public boolean isInitialActivation() {
		return delegate.isInitialActivation();
	}

	public long[] getProductPromoTypeList() {
		return delegate.getProductPromoTypeList();
	}

	public String getEquipmentModel() {
		return delegate.getEquipmentModel();
	}

	public String getEquipmentModelFrench() {
		return delegate.getEquipmentModelFrench();
	}

	public Warranty getWarranty() throws TelusAPIException {
		return delegate.getWarranty();
	}

	public boolean isTelephonyEnabled() {
		return delegate.isTelephonyEnabled();
	}

	public boolean isDispatchEnabled() {
		return delegate.isDispatchEnabled();
	}

	public boolean isWirelessWebEnabled() {
		return delegate.isWirelessWebEnabled();
	}

	public long getShippedToLocation() throws TelusAPIException {
		return delegate.getShippedToLocation();
	}

	public double[] getContractTermCredits() {
		return delegate.getContractTermCredits();
	}

	public boolean isSMSCapable() {
		return delegate.isSMSCapable();
	}

	public boolean isMMSCapable() {
		return delegate.isMMSCapable();
	}

	public Date getEquipmentStatusDate() {
		return delegate.getEquipmentStatusDate();
	}

	public EquipmentSubscriber[] getAssociatedSubscribers(boolean active) throws TelusAPIException {
		return delegate.getAssociatedSubscribers(active);
	}

	public EquipmentSubscriber[] getAssociatedSubscribers(boolean active, boolean refresh) throws TelusAPIException {
		return delegate.getAssociatedSubscribers(active, refresh);
	}

	public void updateStatus(long statusTypeId, long statusId) throws TelusAPIException {
		delegate.updateStatus(statusTypeId, statusId);
	}

	public boolean isVirtual() {
		return delegate.isVirtual();
	}

	public boolean isVistoCapable() throws TelusAPIException {
		return delegate.isVistoCapable();
	}

	public EquipmentMode[] getEquipmentModes() throws TelusAPIException {
		return delegate.getEquipmentModes();
	}

	public abstract boolean isGPS() throws ApplicationException;

	public abstract boolean isVoLTE() throws ApplicationException;

	public boolean isMSBasedEnabled() {
		return delegate.isMSBasedEnabled();
	}

	public boolean isAvailableForActivation() {
		return delegate.isAvailableForActivation();
	}

	public int[] getBrandIds() {
		return delegate.getBrandIds();
	}

	public boolean isValidForBrand(int brandId) {
		return delegate.isValidForBrand(brandId);
	}

	public void reportLost() throws TelusAPIException {
		delegate.reportLost();
	}

	public void reportStolen() throws TelusAPIException {
		delegate.reportStolen();

	}

	public void reportFound() throws TelusAPIException {
		delegate.reportFound();
	}

	public boolean isExpired() {
		return delegate.isExpired();
	}

	public String getNetworkType() throws TelusAPIException {
		return delegate.getNetworkType();
	}

	public boolean isVOIPDummyEquipment() {
		return delegate.isVOIPDummyEquipment();
	}

	public boolean isHSIADummyEquipment() {
		return delegate.isHSIADummyEquipment();
	}
	
	public ProfileInfo getProfile() {
		return delegate.getProfile();
	}
	
	public String getAssociatedHandsetIMEI() {
		return delegate.getAssociatedHandsetIMEI();
	}
	
}