/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.equipment.Warranty;
import com.telus.api.reference.EquipmentMode;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;



public class TMEquipment extends BaseProvider implements Equipment {
	private static final long serialVersionUID = 1L;
	/**
	 * @link aggregation
	 */
	private EquipmentInfo delegate;
	protected EquipmentSubscriber[] associatedSubscribers;
	private EquipmentMode[] equipmentModes;

	public TMEquipment(TMProvider provider, EquipmentInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public EquipmentInfo getDelegate() {
		return delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods
	//--------------------------------------------------------------------
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
	public boolean isTelephonyEnabled(){
		return delegate.isTelephonyEnabled();
	}

	public boolean isDispatchEnabled(){
		return delegate.isDispatchEnabled();
	}

	public  boolean isWirelessWebEnabled(){
		return delegate.isWirelessWebEnabled();
	}
	public boolean isAnalog() {
		return delegate.isAnalog();
	}

	/**
	 * @deprecated
	 * replaced by <code>isPCSHandset()</code>
	 */
	 public boolean isPCS() {
		 return delegate.isPCS();
	 }

	 public boolean isPager(){
		 return delegate.isPager();
	 }

	 public boolean isCellular() {
		 return delegate.isCellular();
	 }

	 public boolean isCellularDigital() {
		 return delegate.isCellularDigital();
	 }

	 public boolean isSIMCard() {
		 return delegate.isSIMCard();
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
		 EquipmentSubscriber[] associatedSubscribers = getAssociatedSubscribers(active);

		 if (associatedSubscribers == null)
			 return false;

		 for (int i = 0; i < associatedSubscribers.length; i++)
			 if (associatedSubscribers[i].getBanId() == ban)
				 return true;

		 return false;
	 }

	 public boolean isInUseOnAnotherBan(int ban, boolean active) throws TelusAPIException {
		 EquipmentSubscriber[] associatedSubscribers = getAssociatedSubscribers(active);

		 if (associatedSubscribers == null)
			 return false;

		 for (int i = 0; i < associatedSubscribers.length; i++)
			 if (associatedSubscribers[i].getBanId() != ban)
				 return true;

		 return false;
	 }

	 public String getProductType() {
		 return delegate.getProductType();
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

	 public boolean isCellularRIM() {
		 return delegate.isCellularRIM();
	 }

	 public boolean isIDENRIM() {
		 return delegate.isIDENRIM();
	 }

	 public boolean isRUIMCard() {
		 return delegate.isRUIMCard();
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


	 public boolean isRIM() {
		 return delegate.isRIM();
	 }
	 public double[] getContractTermCredits() {
		 return delegate.getContractTermCredits();
	 }


	 public Date getEquipmentStatusDate() {
		 return delegate.getEquipmentStatusDate();
	 }

	 public EquipmentSubscriber[] getAssociatedSubscribers(boolean active) throws TelusAPIException {
		 return getAssociatedSubscribers(active, false);
	 }

	 public EquipmentSubscriber[] getAssociatedSubscribers(boolean active, boolean refresh) throws TelusAPIException {
		 try {
			 if (associatedSubscribers == null || refresh){
				 associatedSubscribers = provider.getSubscriberLifecycleHelper().retrieveEquipmentSubscribers(getSerialNumber(), active);
			 }
		 }catch (Throwable t) {
			 provider.getExceptionHandler().handleException(t);
		 }

		 return associatedSubscribers;
	 }

	 public EquipmentMode[] getEquipmentModes() throws TelusAPIException {

		 //It's not likely the equipment mode is going to change for given equipment, so make local variable to be instance variable to avoid 
		 // reload the information over and over again.

		 //EquipmentMode[] equipmentModes;

		 if (equipmentModes==null) {
			 try {
				 equipmentModes = provider.getProductEquipmentHelper().getEquipmentModes(getProductCode());
			 }catch (Throwable t) {
				 provider.getExceptionHandler().handleException(t);
			 }
		 }

		 return equipmentModes;
	 }

	 public int hashCode() {
		 return delegate.hashCode();
	 }

	 public String toString() {
		 return delegate.toString();
	 }

	 //--------------------------------------------------------------------
	 //  Service Methods
	 //--------------------------------------------------------------------
	 public Warranty getWarranty() throws TelusAPIException {
		 try {
			 return provider.getProductEquipmentLifecycleFacade().getWarrantySummary(delegate.getSerialNumber(), delegate.getEquipmentGroup());
		 } catch (Throwable e) {
			 throw new TelusAPIException(e);
		 }
	 }

	 public boolean equals(Object o) {
		 if (o instanceof Equipment)
			 return ((Equipment)o).getSerialNumber().equals(this.getSerialNumber());
		 return false;
	 }

	 public void updateStatus(long statusTypeId, long statusId) throws TelusAPIException {
		 try {
			 provider.getProductEquipmentManager().updateStatus(delegate.getSerialNumber(),
				provider.getUser(), statusTypeId, statusId, delegate.getTechType(), delegate.getProductClassID());
			 delegate.setEquipmentStatusTypeID(statusTypeId);
			 delegate.setEquipmentStatusID(statusId);
		 }catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
		 }
	 }

	 public long getShippedToLocation() throws TelusAPIException
	 {
		 try
		 {
			 if(isPCSHandset()) 
				 return provider.getProductEquipmentHelper().getPCSShippedToLocation(delegate.getSerialNumber());
			 else if(delegate.isIDEN() && !delegate.isSIMCard())
				 return provider.getProductEquipmentHelper().getIDENShippedToLocation(delegate.getSerialNumber());
			 else if(delegate.isSIMCard() &&
					 delegate.getLastMule().getSIMCardNumber() != null &&
					 delegate.getLastMule().getSIMCardNumber().length() > 0)
				 return provider.getProductEquipmentHelper().getIDENShippedToLocation(delegate.getLastMule().getSIMCardNumber());
			 else if(delegate.isHSPA() && !delegate.isUSIMCard())
				 return provider.getProductEquipmentHelper().getPCSShippedToLocation(delegate.getSerialNumber());
			 throw new TelusAPIException("No Location available for SerialNo: " + delegate.getSerialNumber());
		 }catch (Throwable t) {
			 provider.getExceptionHandler().handleException(t);
		 }
		 return 0;
	 }

	 public boolean isSMSCapable() {
		 return delegate.isSMSCapable();
	 }

	 /**
	  * Return true if the device is MMS capable.
	  * @return boolean
	  */
	 public boolean isMMSCapable() {
		 return delegate.isMMSCapable();
	 }

	 public void setPrimary(boolean primary) {
		 delegate.setPrimary(primary);
	 }

	 public boolean isPrimary() {
		 return delegate.isPrimary();
	 }
	 public boolean isVirtual()
	 {
		 return delegate.isVirtual();
	 }

	 public boolean isVistoCapable() throws TelusAPIException {

		 String[] productFeatureCodes = getProductFeatures();

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

	 public String[] getProductFeatures() throws TelusAPIException {

		 String[] productFeatures = delegate.getProductFeatures();
		 if (productFeatures != null)
			 return productFeatures;

		 try {
			 productFeatures = provider.getProductEquipmentHelper().getProductFeatures(delegate.getProductCode());
			 delegate.setProductFeatures(productFeatures);
		 }catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}

		 return productFeatures;

	 }

	 /**
	  * Return true, if one of the product features is ‘GPS’
	  * @return boolean
	  */
	 public boolean isGPS() {
		 try {
			 String[] productFeatures = getProductFeatures();
			 if (productFeatures == null)
				 return false;

			 for (int i=0; i<productFeatures.length; i++) {
				 if (EquipmentInfo.PRODUCT_FEATURE_GPS.equalsIgnoreCase(productFeatures[i].trim()))
					 return true;
			 }
		 } catch(Throwable t) {
			 Logger.debug(t);
		 }

		 return false;
	 }
	 
	 /**
	  * Return true, if one of the product features is ‘VoLTE’
	  * @return boolean
	  */

	 public boolean isVoLTE()
	 {
		 try{
			 String[] productFeatures = getProductFeatures();
			 if(productFeatures == null)
				 return false;
			 
			 for(int i=0;i<productFeatures.length;i++){
				 if(EquipmentInfo.PRODUCT_FEATURE_VOLTE.equalsIgnoreCase(productFeatures[i].trim()))
					 return true;
			 }
		 }	catch(Throwable t){
			 Logger.debug(t);
		 }
		 
		 return false;
	 }
	 /**
	  * Returns true, if one of the firmwareVersionFeatureCodes equal to FEATURE_MSBASED_USERPLANE
	  * @return boolean
	  */
	 public boolean isMSBasedEnabled() {
		 return delegate.isMSBasedEnabled();
	 }

	 /**
	  * Returns false for  "un-scanned"  equipment
	  * @return boolean
	  */

	 public boolean isAvailableForActivation()

	 {
		 return !delegate.isUnscanned();
	 }

	 public int[] getBrandIds() {
		 return delegate.getBrandIds();
	 }

	 public boolean isValidForBrand(int brandId) {
		 return delegate.isValidForBrand(brandId);
	 }

	 public boolean isCDMA() {
		 return delegate.isCDMA();
	 }

	 public boolean isGreyMarket(int brandId) {
		 //for Holborn R1, this is opposite of isValidForBrand
		 return !isValidForBrand(brandId);
	 }

	 public boolean isHSPA() {
		 return delegate.isHSPA();
	 }

	 public boolean isUSIMCard() {
		 return delegate.isUSIMCard();
	 }

	 public String getNetworkType () throws TelusAPIException {
		 return delegate.getNetworkType();
	 }

	 public boolean isPCSHandset() {
		 return delegate.isPCSHandset();
	 }

	 /**
	  * This should not be used anymore. Consumer should use the SEMS WS
	  */
	 @Deprecated
	 public void reportFound() throws TelusAPIException {

		 try {
			 provider.getProductEquipmentLifecycleFacade().markEquipmentFound(delegate.getSerialNumber(), delegate.getEquipmentGroup());
		 } catch (Throwable e) {
			 throw new TelusAPIException(e);
		 }

		 try {
			 if (delegate.isUSIMCard()) {
				 SubscriberLifecycleFacade subLifeCycleFacade=provider.getSubscriberLifecycleFacade();
				 EquipmentInfo delegate = getDelegate();
				 ProfileInfo profInfo = delegate.getProfile();
				 String localIMSI = profInfo.getLocalIMSI();
				 String remoteIMSI = profInfo.getRemoteIMSI();
				 subLifeCycleFacade.setIMSIStatus(delegate.getNetworkType(), localIMSI, remoteIMSI, "AI");

			 }
		 } catch (Throwable e) {}

		 delegate.setEquipmentStatusTypeID(Equipment.STATUS_TYPE_REPORT_BY_CLIENT);
		 delegate.setEquipmentStatusID(Equipment.STATUS_REPORT_BY_CLIENT_FOUND);

		 EquipmentSubscriber[] associatedSubscribers = getAssociatedSubscribers(true);
		 try {
			 for (int i = 0; i < associatedSubscribers.length; i++){
				 int ban = associatedSubscribers[i].getBanId();
				 String subscriberId = associatedSubscribers[i].getSubscriberId();
				 String productType = associatedSubscribers[i].getProductType();
				 provider.getSubscriberManagerBean().refreshSwitch(ban, subscriberId, productType);
			 }
		 } catch (Throwable e) {
			 provider.getExceptionHandler().handleException(e);
		 }

	 }

	 public void reportLost() throws TelusAPIException {

		 try {
			 provider.getProductEquipmentLifecycleFacade().markEquipmentLost(delegate.getSerialNumber(), delegate.getEquipmentGroup());
		 } catch (Throwable e) {
			 throw new TelusAPIException(e);
		 }

		 try {
			 if (delegate.isUSIMCard()) {
				 SubscriberLifecycleFacade subLifeCycleFacade=provider.getSubscriberLifecycleFacade();
				 EquipmentInfo delegate = getDelegate();
				 ProfileInfo profInfo = delegate.getProfile();
				 String localIMSI = profInfo.getLocalIMSI();
				 String remoteIMSI = profInfo.getRemoteIMSI();
				 subLifeCycleFacade.setIMSIStatus(delegate.getNetworkType(), localIMSI, remoteIMSI, "AS");

			 }
		 } catch (Throwable e) {}

		 delegate.setEquipmentStatusTypeID(Equipment.STATUS_TYPE_REPORT_BY_CLIENT);
		 delegate.setEquipmentStatusID(Equipment.STATUS_REPORT_BY_CLIENT_LOST);

		 EquipmentSubscriber[] associatedSubscribers = getAssociatedSubscribers(true);
		 try {
			 for (int i = 0; i < associatedSubscribers.length; i++){
				 int ban = associatedSubscribers[i].getBanId();
				 String subscriberId = associatedSubscribers[i].getSubscriberId();
				 String productType = associatedSubscribers[i].getProductType();
				 provider.getSubscriberManagerBean().refreshSwitch(ban, subscriberId, productType);
			 }
		 } catch (Throwable e) {
			 provider.getExceptionHandler().handleException(e);
		 }
	 }

	 public void reportStolen() throws TelusAPIException {

		 try {
			 provider.getProductEquipmentLifecycleFacade().markEquipmentStolen(delegate.getSerialNumber(), delegate.getEquipmentGroup());			 
		 } catch (Throwable e) {
			 throw new TelusAPIException(e);
		 }

		 try {
			 if (delegate.isUSIMCard()) {
				 SubscriberLifecycleFacade subLifeCycleFacade=provider.getSubscriberLifecycleFacade();
				 EquipmentInfo delegate = getDelegate();
				 ProfileInfo profInfo = delegate.getProfile();
				 String localIMSI = profInfo.getLocalIMSI();
				 String remoteIMSI = profInfo.getRemoteIMSI();
				 subLifeCycleFacade.setIMSIStatus(delegate.getNetworkType(), localIMSI, remoteIMSI, "AS");

			 }
		 } catch (Throwable e) {}

		 delegate.setEquipmentStatusTypeID(Equipment.STATUS_TYPE_REPORT_BY_CLIENT);
		 delegate.setEquipmentStatusID(Equipment.STATUS_REPORT_BY_CLIENT_STOLEN);

		 EquipmentSubscriber[] associatedSubscribers = getAssociatedSubscribers(true);
		 try {
			 for (int i = 0; i < associatedSubscribers.length; i++){
				 int ban = associatedSubscribers[i].getBanId();
				 String subscriberId = associatedSubscribers[i].getSubscriberId();
				 String productType = associatedSubscribers[i].getProductType();
				 provider.getSubscriberManagerBean().refreshSwitch(ban, subscriberId, productType);
			 }
		 } catch (Throwable e) {
			 provider.getExceptionHandler().handleException(e);
		 }
	 }

	 public boolean isExpired() {
		 return delegate.isExpired();
	 }

	 

	 /**
	  * Check if this equipment belongs to manufacturer Apple
	  * @return true if equipment manufacturer is Apple, else false
	  * @deprecated consumers should go to P3MS directly - will remove in July 2017
	  */
	 private Boolean isApple = null;
	 public boolean isApple() throws TelusAPIException {
		 if ( isApple==null) {
			 try {
				 isApple = new Boolean (provider.getProductEquipmentLifecycleFacade().isApple(delegate.getProductCode()));
			 }catch (Throwable t) {
				 provider.getExceptionHandler().handleException(t);
			 }
		 }
		 return isApple.booleanValue();
	 }

	 /**
	  * Returns false if one of the values in DPIneligibleVendors matches the
	  * manufacturer(vendor) ID
	  * @deprecated
	  */
	 public boolean isDeviceProtectionEligible() {
		 //2015 EC: ChangeTool requires this method until it is decommissioned
		 return false;
	 }

	public boolean isVOIPDummyEquipment() throws TelusAPIException {
		return delegate.isVOIPDummyEquipment();
	}

	
	public boolean isHSIADummyEquipment() throws TelusAPIException {
		return delegate.isHSIADummyEquipment();
	}

}

