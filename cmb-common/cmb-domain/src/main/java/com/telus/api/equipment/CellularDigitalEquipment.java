package com.telus.api.equipment;

import com.telus.api.TelusAPIException;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * 
 * @version 1.0
 */

public interface CellularDigitalEquipment extends CellularEquipment {

	public static final String EVDO_TYPE_0 = "EVDO";
	public static final String EVDO_TYPE_A = "EVDOREVA";
	
	// TODO Holborn - Change values
	public static final int ROAMING_CAPABILITY_INTERNATIONAL = 1;
	public static final int ROAMING_CAPABILITY_INTERNATIONAL_LIMITED = 2;
	public static final int ROAMING_CAPABILITY_NORTH_AMERICA = 3;
	public static final int ROAMING_CAPABILITY_UNKNOWN = 4;

/**
 * Retrieves proper Sub/Master Lock code for programming PCS
 * If phone was previously activated or refurbished and it's not Sanyo phone,
 * master lock will be retrieved
 */
  String getSublock();

  String getBrowserVersion();

  String getFirmwareVersion();

  /**
  Retrieves PRL Code( called PRL version as well)
   */
  String getPRLCode();

  String getPRLDescription();

   long getModeCode();

  String getModeDescription();

  boolean isPTTEnabled();

  String[] getFirmwareVersionFeatureCodes();

  String getBrowserProtocol();
  
  /**
   * 
   * @return roaming capability code (e.g 1 = International, 3 = North America...)
   * @throws TelusAPIException
   */
  int getRoamingCapability() throws TelusAPIException;

  CellularDigitalEquipmentUpgrade[] getCellularDigitalEquipmentUpgrades() throws TelusAPIException;

  /**
   * Returns true if  device is a PDA
   * @return boolean
   */
  boolean isPDA();

  /**
  * Returns true if  device is a World Phone
  * @return boolean
  */

  boolean isWorldPhone();

  /**
  * Returns true if device is EvDO Capable.
  * @return boolean
  */
  boolean isEvDOCapable() ;

  /**
   * Returns true if asset tag
   * @return boolean
   */
  boolean isAssetTag();

  /**
   * Returns RIM pin.
   * @return String
   */
  String getRIMPin();

  /**
   * Returns the current firmware feature of the device
   * @return String
   */
  String getCurrentEVDOType();
  
  /**
   * Returns the highest possible firmware for the device
   * @return String
   */
  String getHighestEVDOType();
  
 /**
   * Returns true if the service is Data Card.
   * @return boolean
   * */
   boolean isDataCard(); 
  
}
