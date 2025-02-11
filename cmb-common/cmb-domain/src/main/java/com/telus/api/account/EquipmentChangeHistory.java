package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.Equipment;

/**
 * <CODE>EquipmentChangeHistory</CODE>
 *
 */
public interface EquipmentChangeHistory {

	Date getEffectiveDate();

	Date getExpiryDate();
	
	int getEsnLevel();

	String getSerialNumber();
	
	/**
	 * Retrieves the equipment.
	 *
	 * <P>This method may involve a remote method call.
	 */
	Equipment getEquipment() throws TelusAPIException;
	
}
