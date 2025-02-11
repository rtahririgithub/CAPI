package com.telus.provider.equipment;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */
import com.telus.api.TelusAPIException;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.CellularDigitalEquipmentUpgrade;
import com.telus.api.reference.EquipmentMode;
import com.telus.api.reference.HandsetRoamingCapability;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;


public class TMCellularDigitalEquipment extends TMCellularEquipment implements CellularDigitalEquipment {

	private CellularDigitalEquipment delegate;

	public TMCellularDigitalEquipment(TMProvider provider, EquipmentInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}

	//--------------------------------------------------------------------
	//  Decorative Methods

	//--------------------------------------------------------------------

	public String getSublock() {
		return delegate.getSublock();
	}

	public String getBrowserVersion() {
		return delegate.getBrowserVersion();
	}

	public String getFirmwareVersion() {
		return delegate.getFirmwareVersion();
	}


	public String getPRLCode() {
		return delegate.getPRLCode();
	}

	public String getPRLDescription() {
		return delegate.getPRLDescription();
	}

	public long getModeCode() {
		return delegate.getModeCode();
	}

	public String getModeDescription() {
		return delegate.getModeDescription();
	}


	public String[] getFirmwareVersionFeatureCodes(){
		return delegate.getFirmwareVersionFeatureCodes();
	}


	public String getBrowserProtocol() {
		return delegate.getBrowserProtocol();

	}

	public boolean isPTTEnabled() {
		return delegate.isPTTEnabled();
	}

	public CellularDigitalEquipmentUpgrade[] getCellularDigitalEquipmentUpgrades() throws TelusAPIException {
		try {
			return provider.getProductEquipmentLifecycleFacade().getCellularDigitalEquipmentUpgrades((EquipmentInfo)delegate);
		}catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
		return null;
	}

	public boolean isPDA() {
		return delegate.isPDA();
	}

	public boolean isEvDOCapable() {
		return delegate.isEvDOCapable();
	}

	public boolean isWorldPhone() {
		return delegate.isWorldPhone();
	}

	public boolean isAssetTag() {
		return delegate.isAssetTag();
	}

	public String getRIMPin() {
		return delegate.getRIMPin();
	}

	public String getCurrentEVDOType() {
		return delegate.getCurrentEVDOType();
	}

	public String getHighestEVDOType() {
		try {
			String[] productFeatures = getProductFeatures();

			return delegate.getHighestEVDOType();
		} catch (Throwable t) {
			Logger.debug(t);
		}

		return null;
	}

	public int getRoamingCapability() throws TelusAPIException {

		int roamingCap = ROAMING_CAPABILITY_UNKNOWN; 
		String roamingType="TYPE_4";
		String higherRoamingType="TYPE_4";

		HandsetRoamingCapability[] handsetRoamingCapability = null;

		handsetRoamingCapability = provider.getReferenceDataManager().getRoamingCapability() ;

		EquipmentMode[] modes = getEquipmentModes();


		if ( modes!=null && modes.length>0) {
			for( int i=0; i<modes.length; i++ ) {
				for (int j=0; j<handsetRoamingCapability.length; j++){
					if (handsetRoamingCapability[j].getHandsetMode().equalsIgnoreCase(modes[i].getCode())) {
						roamingType = handsetRoamingCapability[j].getRoamingType();
						break;
					}
				}
				if(roamingType.compareTo(higherRoamingType) < 0){
					higherRoamingType = roamingType;
				}
			}
		}

		if(higherRoamingType.equalsIgnoreCase("TYPE_1"))
			roamingCap = ROAMING_CAPABILITY_INTERNATIONAL;
		else if(higherRoamingType.equalsIgnoreCase("TYPE_2"))
			roamingCap = ROAMING_CAPABILITY_INTERNATIONAL_LIMITED;
		else if(higherRoamingType.equalsIgnoreCase("TYPE_3"))
			roamingCap = ROAMING_CAPABILITY_NORTH_AMERICA;
		else if(higherRoamingType.equalsIgnoreCase("TYPE_4"))
			roamingCap = ROAMING_CAPABILITY_UNKNOWN;

		return roamingCap;
	}

	public boolean isDataCard() {
		 return delegate.isDataCard();
	}
	


}
