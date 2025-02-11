/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.reference;

import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.equipment.Equipment;


/**
 * <CODE>PricePlan</CODE>
 **/
public interface PricePlan extends PricePlanSummary {


  /**
   * Returns an array of required service groups for a given equipment.
   * Each ServiceSet contains a set of optional services (@see getOptionalServices())
   * for which one must be add to the subscriber's contract prior to save.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @link aggregationByValue
   */
  ServiceSet[] getMandatoryServiceSets(Equipment equipment) throws TelusAPIException;

  ServiceSet[] getMandatoryServiceSets(String equipmentType, String networkType) throws TelusAPIException;
  
  ServiceSet[] getMandatoryServiceSets(Equipment equipment, String overrideEquipmentType) throws TelusAPIException;
  
  /**
   * Returns an array of required service groups regardless of equipment.
   * Each ServiceSet contains a set of optional services (@see getOptionalServices())
   * for which one must be add to the subscriber's contract prior to save.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   */
  ServiceSet[] getMandatoryServiceSets() throws TelusAPIException;



  int getOptionalServiceCount();

  /**
   * Returns all optional SOCs in this plan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   * 
   * If the PP is configured for both CDMA and HSPA networks, this method will return all combinations
   * of CDMA only (C), HSPA only (H), CDMA+HSPA (9) compatible SOCs. As a result, the optional SOC
   * might not be compatible with the subscriber. To filter out based on network type, please
   * use getOptionalServices(Equipment) methods  
   *
   * @link aggregationByValue
   */
  Service[] getOptionalServices();

  /**
   * Returns all optional SOCs in this plan that is supported by the specified equipment.
   * Please note that only services that match equipment's network type and equipment type will be returned.
   * Client should pass handset equipment wherever possible.
   * 
   * To obtain a general set of optional services regardless to equipment, please use getOptionalServices().
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *
   * @link aggregationByValue
   */
  Service[] getOptionalServices (Equipment equipment);

  /**
   * Returns all optional SOCs in this plan that is supported by the specified equipment.
   * @param equipment Equipment
   * @param includePrepaidServices boolean
   * @return Service[]
 * @throws TelusAPIException 
   */
  Service[] getOptionalServices (Equipment equipment, boolean includePrepaidServices) throws TelusAPIException;
  
	  /**
	 * @param equipment
	 * @param includePrepaidServices
	 * @param overrideEquipmentType - For HSPA equipment only. Use this equipment type to filter out the incompatible services. 
	 * @return Service[]
	 */
	Service[] getOptionalServices(Equipment equipment, boolean includePrepaidServices, String overrideEquipmentType) throws TelusAPIException;


  /**
   * Returns all Telephony optional SOCs in this plan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   */

  Service[] getTelephonyServices();
  /**
   * Returns all Dispatch optional SOCs in this plan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   */

  Service[] getDispatchServices();
  /**
   * Returns all WirelessWeb optional SOCs in this plan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   */

  Service[] getWirelessWebServices();

  Service getOptionalService(String code) throws UnknownObjectException;

  Service getService(String code) throws UnknownObjectException;

  boolean containsOptionalService(String code);

  boolean waiveActivationFee();

	/**
   * @deprecated
   * @see #getPricePlanFamily(String, String, String, boolean, int)
   * Returns Price Plan Summary Array of the Price Plans
   * that belongs to the same Family ( like 'Talk 25')
   * filtered by Province, Equipment Type , current indicator, contract term
   *
   *@link Equipment
   */
  PricePlanSummary[] getPricePlanFamily (String provinceCode, String equipmentType, boolean currentPlansOnly, int termInMonths) throws  TelusAPIException;

  PricePlanSummary[] getPricePlanFamily (String provinceCode, String equipmentType, String networkType, boolean currentPlansOnly, int termInMonths) throws  TelusAPIException;

  /**
   * Returns promotional services automatically added contracts created with this priceplan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   *@deprecated
   */
  Service[] getIncludedPromotions(String equpmentType, String provinceCode, int termInMonths) throws TelusAPIException;
  
  /**
   * Returns promotional services automatically added contracts created with this priceplan.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.
   */
  Service[] getIncludedPromotions(String networkType, String equpmentType, String provinceCode, int termInMonths) throws TelusAPIException;

  /**
   * Returns true if the Service associated to the price plan.
   *
   */

  boolean isAssociatedService(Service service) throws TelusAPIException;

  /**
   * Returns Service[] of included promotions
   * @param equipment Equipment
   * @param provinceCode String
   * @param termInMonths int
   * @return Service[]
   */
  Service[] getIncludedPromotions(Equipment equipment, String provinceCode, int termInMonths) throws TelusAPIException;

}




