package com.telus.provider.equipment;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.UnknownSerialNumberException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.util.TelusExceptionTranslator;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.util.ProviderEquipmentExceptionTranslator;

public class TMUSIMCardEquipment extends TMCellularDigitalEquipment implements
		USIMCardEquipment {

	private static final long serialVersionUID = 1L;

	public TMUSIMCardEquipment(TMProvider provider, EquipmentInfo delegate) {
		super(provider, delegate);
	}

	public String getLastAssociatedHandsetIMEI() {
		return getDelegate().getAssociatedHandsetIMEI();
	}

	public String getPUKCode() {
		return getDelegate().getPUKCode();
	}

	public boolean isAssignable() {
		return getDelegate().isAvailableUSIM();
	}

	public void setLastAssociatedHandset(Equipment equipment) {
		if (equipment != null) {
			getDelegate().setAssociatedHandset(((TMEquipment) equipment).getDelegate());
		}else {
			getDelegate().setAssociatedHandset(null);
		}
	}

	public Equipment getLastAssociatedHandset() throws TelusAPIException {
		EquipmentInfo delegate = getDelegate();
		EquipmentInfo associatedHandset = delegate.getAssociatedHandset();

		if (associatedHandset == null) {
			String associatedHandsetIMEI = delegate.getAssociatedHandsetIMEI();
			try {
				associatedHandset = retrieveAssociatedHandset (associatedHandsetIMEI);
				delegate.setAssociatedHandset(associatedHandset);

			}catch (Throwable t) {
				handleRetrieveAssociatedHandsetException(t);
			}

		}
		if (associatedHandset != null) {
			return (new TMPCSEquipment(provider, associatedHandset));
		}

		return null;
	}
	
	private EquipmentInfo retrieveAssociatedHandset (String associatedHandsetIMEI) throws ApplicationException {
		if (associatedHandsetIMEI != null ) {
			if (associatedHandsetIMEI.length() >= 15) { //a handset's IMEI number should be at least 15 digits long. Greymarket handsets in usim_pcs_device_assoc table is only 14 digits long and causing unnecessary remote call as our db doesn't have the info.
				return provider.getProductEquipmentHelper().getEquipmentInfobySerialNo(associatedHandsetIMEI);
			}
		} else {
			return provider.getProductEquipmentHelper().getAssociatedHandsetByUSIMID(getSerialNumber());
		}
		
		return null;
	}
	
	private void handleRetrieveAssociatedHandsetException (Throwable t) throws TelusAPIException {
		try{
			String associatedHandsetIMEI = getDelegate().getAssociatedHandsetIMEI();
			TelusExceptionTranslator telusExceptionTranslator= new ProviderEquipmentExceptionTranslator(associatedHandsetIMEI !=null ? associatedHandsetIMEI:getSerialNumber());
			provider.getExceptionHandler().handleException(t,telusExceptionTranslator);
		}catch(TelusAPIException tae){
			if(tae instanceof UnknownSerialNumberException == false) {
				throw tae;
			}
		}
	}
	
	
	public String getLastAssociatedSubscriptionId() throws TelusAPIException{
		EquipmentInfo delegate = getDelegate();
		ProfileInfo profInfo = delegate.getProfile();
		String imsi = profInfo.getLocalIMSI();
		
		String lastSubId = null;
		try {
			lastSubId = provider.getSubscriberLifecycleHelper().retrieveLastAssociatedSubscriptionId(imsi);
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return lastSubId;
	}

	public boolean isPreviouslyActivated() {
		EquipmentInfo delegate = getDelegate();
		return delegate.isPreviouslyActivated();
	}

	public String getLastAssociatedHandsetEventType() {
		EquipmentInfo delegate = getDelegate();
		return delegate.getLastAssociatedHandsetEventType();
	}
	
	/**
	 * Returns true/false based on associated handset
	 * @deprecated
	 */
	public boolean isDeviceProtectionEligible() {
		try {
			Equipment handset = getLastAssociatedHandset();
			if (handset != null) {
				return handset.isDeviceProtectionEligible();
			}
		}catch (TelusAPIException tapie) {
			
		}
		return super.isDeviceProtectionEligible();
	}
	

}