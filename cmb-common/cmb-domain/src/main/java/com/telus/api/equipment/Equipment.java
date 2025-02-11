
/**
 * Title:        <p>
 * Descriptioncription:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.equipment;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.reference.EquipmentMode;

/**
 * <CODE>Equipment</CODE> represents the maximum intersection of all
 * equipment types. It is the superclass for specific equipment types.
 *
 */
public interface Equipment {

  static final String PRODUCT_TYPE_PCS = "C";
  static final String PRODUCT_TYPE_PAGER = "P";
  static final String PRODUCT_TYPE_IDEN = "I";

  // PCS equipment types
  static final String EQUIPMENT_TYPE_ANALOG = "A";
  static final String EQUIPMENT_TYPE_DIGITAL = "D";
  static final String EQUIPMENT_TYPE_1xRTT_CARD = "3";
  static final String EQUIPMENT_TYPE_RIM = "Z";
  static final String EQUIPMENT_TYPE_DATACARD = "C" ;
  static final String EQUIPMENT_TYPE_PDA = "P";
  static final String EQUIPMENT_TYPE_ALL = "9";
  static final String EQUIPMENT_TYPE_USIM = "U";
  static final String EQUIPMENT_TYPE_PREPAID_IPAD= "1";

  // Autotel equipment types
  static final String EQUIPMENT_TYPE_TANGO = "T" ;
  
  //Business Connect  equipment types
  static final String EQUIPMENT_TYPE_HSIA = "S";
  static final String EQUIPMENT_TYPE_VOIP= "V";

  long STATUS_TYPE_REPORT_BY_CLIENT = 3;
  long STATUS_REPORT_BY_CLIENT_LOST = 56;
  long STATUS_REPORT_BY_CLIENT_STOLEN = 11;
  long STATUS_REPORT_BY_CLIENT_FOUND = 60;

  static final String SWAP_TYPE_LOANER = "LOANER";
  static final String SWAP_TYPE_REPAIR = "REPAIR";
  static final String SWAP_TYPE_REPLACEMENT = "REPLACEMENT";
  static final String PRODUCT_FEATURE_3RDPARTYEMAIL ="3RDPARTYEMAIL" ;

  // LBS
  String EQUIPMENT_TYPE_ASSET_TAG = "T";
  public final static String TECHNOLOGY_TYPE_LTE = "LTE";
  
   //Business connect dummy esn values for HSIA ,VOIP
    public final static String DUMMY_ESN_FOR_HSIA = "200000000000000000";
	public final static String DUMMY_ESN_FOR_VOIP = "300000000000000000";
	
	// hspa constant serial number used to support phoneNumber reserve without real equipment  for HSPA subscribers .
	public final static String DUMMY_HSPA_NETWORK_NUMBER = "hspa10000000000000";
	
  /**
 * Returns Knowbility Product Type
 * @return Knowbility Product Type
 */
  String getProductType();
  /**
   * Returns USIM ID if USIM Card equipment, SIM ID if SIM equipment, ICCID if UIM equipment, else returns serial_no
   * @return USIM ID if USIM Card equipment, SIM ID if SIM equipment, ICCID if UIM equipment, else returns serial_no
   */
  String getSerialNumber();
  
  /**
   * Returns Technology Type
   * Mapped to database field product.technology_type
   * @return Technology Type
   */
  String getTechType() ;
  
  /**
   * Returns Product Code
   * Mapped to database field product.product_cd
   * @return Product Code
   */
  String getProductCode() ;
  
  /**
   * Returns Product Name
   * Mapped to database field product.product_name
   * @return Product Name
   */  
  String getProductName();
  /**
   * Returns Product Name in French
   * Mapped to database field product.french_product_name
   * @return Product Name in French
   */ 
  String getProductNameFrench();
  
  /**
   * Returns Product Status Code
   * Mapped to database field product.product_status_cd
   * @return Product Status Code
   */ 
  String getProductStatusCode();
  /**
   * Returns Vendor Name
   * Mapped to database field manufacturer.manufacturer_id
   * @return Manufacturer Name
   */ 
  String getVendorName() ;
  /**
   * Returns Vendor ID
   * Mapped to database field manufacturer.manufacturer_name
   * @return Manufacturer ID
   */  
  String getVendorNo() ;
  /**
   * Check if equipment is marked as stolen or lost
   * @return true if equipment is marked as stolen or lost, else false
   */
  boolean isStolen() ;
  /**
   * Returns Product Group Type ID
   * 
   * @return Product Group Type ID
   */  
  long getProductGroupTypeID() ;
  /**
   * Returns Product Group Type Code
   * @return Product Group Type Code
   */  
  String getProductGroupTypeCode() ;
  /**
   * Returns Product Group Type Description
   * @return Product Group Type Description
   */ 
  String getProductGroupTypeDescription();
  /**
   * Returns Product Group Type Description in French
   * @return Product Group Type Description in French
   */ 
  String getProductGroupTypeDescriptionFrench();
  /**
   * Mapped to database field product.product_class_id
   * @return long
   */
  long getProductClassID();
  /**
   * Mapped to database field product_classification.product_class_cd
   * @return String
   */
  String getProductClassCode() ;
  
  /**
   * Mapped to database field product_classification.product_class_des
   * @return String
   */
  String getProductClassDescription();
  /**
   * Mapped to database field pcs_equipment.provider_owner_id
   * @return long
   */
  long getProviderOwnerID() ;
  /**
   * Mapped to database field equipment_status_type_id  for given serial number from pcs_equip_status, 
   * iden_equip_status, uim_status, sim_status, analog_equip_status or paging_equip_status 
   * @return long
   */
  long getEquipmentStatusTypeID() ;
  /**
   * Mapped to database field equipment_status_id for given serial number from pcs_equip_status, 
   * iden_equip_status, uim_status, sim_status, analog_equip_status or paging_equip_status 
   * @return long
   */
  long getEquipmentStatusID();
  long getProductTypeID() ;
  String getProductTypeDescription();
  String getProductTypeDescriptionFrench();
  
  /**
   * Check if the equipment is mapped to an IDEN Network. This field is driven by PRODUCT_CLASS_XREF_KB.
   * source_network_type_cd in the DIST database based on the product classification of the equipment.
   * @return true if source_network_type_cd equals {@link NetworkType.NETWORK_TYPE_IDEN}
   */
  boolean isIDEN();
  /**
   * Check if the equipment is Cellular
   * @return true if Product Type is 'C', else false
   */ 
  boolean isCellular();
  /**
   * Check if the equipment is PCS DIGITAL
   * @return true if Product Type is 'C' and Equipment Type Class is 'DIGITAL', else false
   * @see #isCellular()
   */ 
  boolean isCellularDigital();
  /**
   * Check if the equipment is ANALOG
   * @return true if Technology Type is 'ANA', else false
   */
  boolean isAnalog();
  /**
   * @deprecated the real meaning of this method is to check if a equipment is a PCS Handset equipment. So please use isPCSHandset() 
   * @see #isPCSHandset()
   */
  boolean isPCS();
  /**
   * Check if equipment is a PCS Handset
   * @return true if Technology Type is 'PCS' and Product Class Code is 'HAND', else false
   */
  boolean isPCSHandset();
  /**
   * Check if equipment is a Pager
   * @return true if Technology Type is 'PAGE' and Product Class Code is 'HAND', else false
   */
  boolean isPager();
  /**
   * Check if the equipment is 1XRTT
   * @return true if Technology Type is '1RTT', else false
   */
  boolean is1xRTT();
  /**
   * Check if the equipment is 1xRTT Card
   * @return true if Technology Type is '1RTT' and Product Class Code is HSPA_AIRCARD,HSPA_MODEM,HSPA_ROUTER,WIRELESS_MODEM or WIRELESS_DATA_ONLY_MODEM, else false
   * @see #is1xRTT()
   */ 
  boolean is1xRTTCard();
  /**
   * Check if the equipment is handset including HSPA_HANDSET, WORLDPHONE_HANDSET and HANDSET
   * @return true if Product Class Code is HSPA_HANDSET,WORLDPHONE_HANDSET or HANDSET, else false
   */  
  boolean isHandset();
  /**
   * Check if the equipment is IDEN SIM Card
   * @return true if Technology Type is 'MIKE' and Product Class Code is SIM, else false
   * @see #isIDEN()
   */   
  boolean isSIMCard();
  /**
   * Check if the equipment is RIM including RIM, Worldphone RIM, HSPA RIM and SmartCard RIM Handset
   * @return true if is Cellular RIM or is IDEN RIM, else false
   * @see #isCellularRIM()
   * @see #isIDENRIM()
   */ 
  boolean isRIM();
  /**
   * Check if the equipment is Cellular RIM including RIM, Worldphone RIM and HSPA RIM, LTE RIM
   * @return true if Technology Type is '1RTT' or 'LTE' and Product Class Code is RIM, WORLDPHONE_RIM, or HSPA_RIM, else false
   */ 
  boolean isCellularRIM();
  /**
   * Check if the equipment is IDEN RIM Handset
   * @return true if Technology Type is 'mike' and Product Class Code is SMARTCARD_RIM_HANDSET, else false
   */  
  boolean isIDENRIM();
  /**
   * Check if the equipment RUIM Card
   * @return true if Technology Type is 'GSM' and Product Class Code is 'UIM' and Product Category Id is '10001101', else false
   */  
  boolean isRUIMCard();
  /**
   * Returns the BAN of the subscriber who is using this equipment
   * @return BAN
   */  
  int getBanID();
  /**
   * Returns the phone number associated with this equipment
   * @return phone number
   */
  String getPhoneNumber();
  /**
   * Check if the equipment is in use
   * @return true if BAN is not 0, else false
   */ 
  boolean isInUse();
  
  boolean isInUseOnBan(int ban, boolean active) throws TelusAPIException;
  boolean isInUseOnAnotherBan(int ban, boolean active) throws TelusAPIException;
  
  //Holborn Methods
  /**
   * Check if the equipment is mapped to a HSPA Network.  This field is driven by PRODUCT_CLASS_XREF_KB.
   * source_network_type_cd in the DIST database based on the product classification of the equipment.
   * @return true if source_network_type_cd equals {@link NetworkType.NETWORK_TYPE_HSPA}
   */
  boolean isHSPA();
  /**
   * Check if the equipment is mapped to a CDMA Network. This field is driven by PRODUCT_CLASS_XREF_KB.
   * source_network_type_cd in the DIST database based on the product classification of the equipment.
   * @return true if source_network_type_cd equals {@link NetworkType.NETWORK_TYPE_CDMA}
   */
  boolean isCDMA();
  /**
   * Check if the equipment is grey market device
   * @return true if the equipment is not associated with given brandID, else false
   * @see #isValidForBrand(int)
   */
  boolean isGreyMarket(int brandId);
  /**
   * Check if the equipment is USIM Card
   * @return true if the equipment's Product Class Code is 'USIM', else false
   */
  boolean isUSIMCard();
  /**
 * Returns Equipment's Knowbility Equipment Type
 * @return Equipment's Knowbility Equipment Type
 */
  String getEquipmentType();
  boolean isInitialActivation();
  long[] getProductPromoTypeList() ;
  String getEquipmentModel();
  String getEquipmentModelFrench();
  /**
   * Returns Equipment's warranty
   * @return Equipment's warranty, retrieved via SEMS Web Service
   * @throws TelusAPIException if the remote call to the SEMS Web Service fails.
   */
  Warranty getWarranty() throws TelusAPIException;
  /**
   * Checks Equipment's telephony capability
   * @return true if equipment has telephony capability, else false
   */
  boolean isTelephonyEnabled();
  boolean isDispatchEnabled();
  boolean isWirelessWebEnabled();
  long getShippedToLocation() throws TelusAPIException;

  /**
   * Returns the available contract airtime credit for this equipment.
   * <P>The values are indexed by the contract's term in years:<BR>
   *
   * <UL>
   *   <LI>0 - month-to-month (no contract term).  This will generally be $0.00.
   *   <LI>1 - 1 year contract
   *   <LI>2 - 2 year contract
   *   <LI>3 - 3 year contract
   * </OL>
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   */
  double[] getContractTermCredits();

  /**
   * Returns a boolean to indicate if the equipment is SMS capable
   * @return true if the equipment is SMS capable
   */
  boolean isSMSCapable();

  /**
   * Return true if the device is MMS capable.
   * @return boolean
   */
  boolean isMMSCapable();

  /**
   * Return true if the device is Java Download capable.
   * @return boolean
   */
  //boolean isJavaDownloadCapable();

  /**
  * Return a Date when equipment status was modified.
  * @return Date
  */

  Date getEquipmentStatusDate();
  /**
   * Returns the list of subscriber associated with this equipment 
   * <P>This method involves a remote method call.
   * @param active if true, only return subscribers in active status
   * @return the list of subscriber associated with this equipment
   * @see #getAssociatedSubscribers(boolean, boolean)
   */ 
  EquipmentSubscriber[] getAssociatedSubscribers(boolean active) throws TelusAPIException;
  /**
   * Returns the list of subscriber associated with this equipment
   * <P>This method involves a remote method call.
   * @param active if true, only return subscribers in active status
   * @param refresh if true, go to database to retrieve subscribers 
   * @return the list of subscriber associated with this equipment
   * @see #getAssociatedSubscribers(boolean active)
   */ 
  EquipmentSubscriber[] getAssociatedSubscribers(boolean active, boolean refresh) throws TelusAPIException;

  /**
   * Updates status of Equipment.
   * <P>This method involves a remote method call.
   * @deprecated replaced by reportLost()/reportStolen()/reportFound()
   * @see #reportLost()
   * @see #reportStolen()
   * @see #reportFound()
   * 
   */
    void updateStatus(long statusTypeId,long statusId)throws TelusAPIException;

    /**
     * Return true if the given equipment is Virtual.
     * @return boolean
     */
    public boolean isVirtual();

    /**
     * Return true if the given equipment is Visto Capable.
     * @return boolean
     */

    boolean isVistoCapable() throws TelusAPIException ;

    /**
     * Return Equipment Modes for the given Product.
     *
     */

    EquipmentMode[] getEquipmentModes() throws TelusAPIException;

    /**
     * Return true, if one of the product features is ?GPS?
     * @return boolean
     */
    boolean isGPS();

    /**
     * Return true, if one of the product features is ?VoLTE?
     * @return boolean
     */
    
    boolean isVoLTE();
    /**
     * Returns true, if one of the firmwareVersionFeatureCodes equal to FEATURE_MSBASED_USERPLANE
     * @return boolean
     */
    boolean isMSBasedEnabled();
    
    /**
     * Returns false for  "unscanned"  equipment
     * @return boolean
     */
    
    boolean isAvailableForActivation();

    /**
     * Returns the equipment's brand IDs
     * @return int
     */
    int[] getBrandIds();

    /**
     * Returns <code>true</code> if <code>brandId</code> is among the brands, associated with the given piece
     * of equipment and <code>false</code> otherwise.
     *
     * @param brandId
     * @return <code>true</code> if <code>brandId</code> is among the brands, associated with the given piece
     * of equipment and <code>false</code> otherwise.
     */
    boolean isValidForBrand(int brandId);
    /**
     * Reports Equipment lost
     * <P>Replaces method updateStatus(long,long)
     * <P>This method involves a remote method call.
     */    
    void reportLost() throws TelusAPIException;
    /**
     * Reports Equipment stolen
     * <P>Replaces method updateStatus(long,long)
     * <P>This method involves a remote method call.
     */   
    void reportStolen()throws TelusAPIException;
    /**
     * Reports Equipment found
     * <P>Replaces method updateStatus(long,long)
     * <P>This method involves a remote method call.
     * 
     * Deprecated
     * This should not be used anymore. Consumer should use the SEMS WS
     */
	 @Deprecated
    void reportFound() throws TelusAPIException;
    /**
     * Checks if Equipment is expired 
     * <P>This method involves a remote method call.
     * @return true if equipment status is 1/65, else false
     */     
    boolean isExpired();
    
    /**
     * Returns network type (defined in NetworkType) of equipment
     */
    String getNetworkType() throws TelusAPIException;
    
    /**
     * Check if this equipment belongs to manufacturer Apple
     * @return true if equipment manufacturer is Apple, else false
     * @deprecated
     */
    boolean isApple() throws TelusAPIException;
    
    /**
     * Returns true if this equipment is eligible for device protection SOC 
     * @deprecated
     */
    boolean isDeviceProtectionEligible();
    
    public boolean isHSIADummyEquipment()  throws TelusAPIException;
	
	public boolean isVOIPDummyEquipment()  throws TelusAPIException;

    
}
