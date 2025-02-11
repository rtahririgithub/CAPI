package com.telus.eas.equipment.info;


import java.util.HashSet;
import com.telus.api.TelusAPIException;
import com.telus.api.reference.NetworkType;

/**
 * This class was added for Covent rollback purpose ,below methods got impacted by Covent.
 * InfoObjectFactory class will return this instance based on rollback flag , next release this class should be removed along with Covent rollback.
 */
public class DeprecatedEquipmentInfo extends EquipmentInfo {

	private static final long serialVersionUID = -5343172703038635151L;
	
	private static HashSet HSPA_DEVICE_PRODUCT_CLASS_CODES = new HashSet();
	
	static {
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_UIC_CARD );
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_HANDSET);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_RIM);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_PDA);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_AIRCARD);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_MODEM);
		HSPA_DEVICE_PRODUCT_CLASS_CODES.add(PRODUCT_CLASS_CODE_HSPA_ROUTER);
	}

	
	public boolean isIDEN() {
		return (getTechType() != null && getTechType().equals("mike"));
	}

	public boolean isCellularRIM() {
		return ((getTechType() != null && getTechType().equals(
				TECHNOLOGY_TYPE_1XRTT))&& (getProductClassCode() != null && (getProductClassCode()
						.equals(PRODUCT_CLASS_CODE_RIM) || getProductClassCode()
						.equals(PRODUCT_CLASS_CODE_WORLDPHONE_RIM))) || PRODUCT_CLASS_CODE_HSPA_RIM.equals(getProductClassCode()));				
	}

	public boolean isDataCard() {
		return (getTechType() == null ? false : getTechType().equals(TECHNOLOGY_TYPE_1XRTT) && getProductClassCode().equals("WDM") ? true : false);
	}

	public boolean isCDMA() {
		if (isHSPA() || isIDEN())
			return false;
		else
			return true;
	}

	public boolean isHSPA() {
		boolean result = is1xRTT()
				&& HSPA_DEVICE_PRODUCT_CLASS_CODES.contains(getProductClassCode());			
		return result;
	}

	public String getNetworkType() throws TelusAPIException 
	{
		if (isIDEN())
			return NetworkType.NETWORK_TYPE_IDEN;
		else if (isCDMA())
			return NetworkType.NETWORK_TYPE_CDMA;
		else if (isHSPA())
			return NetworkType.NETWORK_TYPE_HSPA;
		else {
			throw new TelusAPIException("Network type is unknown, sn["	+ getSerialNumber() + "]");			
		}
	}
	
}
