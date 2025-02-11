package com.telus.api.equipment;import com.telus.api.TelusAPIException;

public interface USIMCardEquipment extends CellularDigitalEquipment {

	String getPUKCode();

	String getLastAssociatedHandsetIMEI();

	/**
	 * 
	 * @return PCSEquipment object, the last handset associated with the card
	 * @throws TelusAPIException
	 */
	Equipment getLastAssociatedHandset() throws TelusAPIException;

	/**
	 * 
	 * Detertmines inventory status of USIM Equipment
	 * 
	 * @return boolean
	 */
	boolean isAssignable();
	
	/**
	 * 
	 * @return last subscription ID that was associated with this card
	 * @throws TelusAPIException
	 */
	String getLastAssociatedSubscriptionId() throws TelusAPIException;
	
	/**
	 * If Card is previously activated, it can only be activated with the same subscription ID it was originally activated with
	 * @return boolean
	 */
	boolean isPreviouslyActivated();
	
	String getLastAssociatedHandsetEventType();

}