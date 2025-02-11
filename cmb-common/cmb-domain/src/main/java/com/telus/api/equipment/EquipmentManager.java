
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.equipment;

import com.telus.api.InvalidPINException;
import com.telus.api.TelusAPIException;
import com.telus.api.TooManyAttemptsException;
import com.telus.api.account.*;
import com.telus.api.reference.Province;

import java.util.Hashtable;

/**
 * <CODE>EquipmentManager</CODE> provides a simple interface into the
 * client equipment management system.
 *
 *
 * @see Equipment
 *
 */

public interface EquipmentManager extends java.io.Serializable {

public static String EVENT_TYPE_SIM_IMEI_ACTIVATE = "ACT";
public static String EVENT_TYPE_SIM_IMEI_RESERVED = "RESVD";

  /**
   * Returns the equipment associated with a serial number.
   *
   *  <P>The returned object will be a specific type of equipment (i.e. PCSDigitalEquipment,
   *  PCSAnalogEquipment,  IDENEquipment, OneRTTEquipment, SIMCardEquipment, etc.).
   *  Use the isXXX() methods to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber Serial No/SIM ID/USIM ID
   * @return the equipment associated with a serial number
   * @exception UnknownSerialNumberException if the equipment could not be found.
   *
   */
  Equipment getEquipment(String serialNumber) throws UnknownSerialNumberException, TelusAPIException;


/**
   * Returns the equipment associated with a serial number.
   *
   *  <P>The returned object will be a specific type of equipment (i.e. PCSDigitalEquipment,
   *  PCSAnalogEquipment,  IDENEquipment, OneRTTEquipment, SIMCardEquipment, etc.).
   *  Use the isXXX() methods to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber Serial No/SIM ID
   * @param checkPseudoESN a flag to check pseudo ESN as well, if true, serialNumber could be pseudo ESN
   * @return the equipment associated with a serial number
   * @exception UnknownSerialNumberException if the equipment could not be found.
   *
   */
  Equipment getEquipment(String serialNumber, boolean checkPseudoESN) throws UnknownSerialNumberException, TelusAPIException;

  /**
   * Returns an array of the equipment associated with a serial number.
   *
   *  <P>The returned objects will be a specific type of equipment (i.e. PCSDigitalEquipment,
   *  PCSAnalogEquipment,  IDENEquipment, OneRTTEquipment, SIMCardEquipment, etc.).
   *  Use the isXXX() methods to determine which one.
   *
   * <P>The returned array will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber Serial No/SIM ID
   * @param checkPseudoESN a flag to check pseudo ESN as well, if true, serialNumber could be pseudo ESN
   * @return an array of the equipment associated with a serial number
   * @exception UnknownSerialNumberException if the equipment could not be found.
   *
   */
  Equipment [] getEquipments(String serialNumber, boolean checkPseudoESN) throws UnknownSerialNumberException, TelusAPIException;

  
  /**
   * Returns a piece of paging equipment, associated with the given capcode.
   *
   * @param capCode Formatted capcode.
   * @param encodingFormat Capcode encoding format.
   * @return A piece of paging equipment, associated with the given capcode.
   * @throws UnknownSerialNumberException If no equipment found a associated with the given capcode.
   * @throws TelusAPIException If any problem happens in the remote call.
   */
  Equipment getEquipmentByCapCode(String capCode, String encodingFormat) throws UnknownSerialNumberException, TelusAPIException;

  /*
   * Returns the Pager equipment associated with a serial number.
   *
   *  <P>The returned object will be a specific to Pager Equipment
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param Cap Code
   * @param Coverage Region
   *
   *
   * @exception UnknownSerialNumberException if the equipment could not be found.
   *
   */
 // PagerEquipment getEquipment(String capCode, String coverageRegion) throws UnknownSerialNumberException, TelusAPIException;

  /**
   * Returns the equipment associated with a phone number.
   *
   * <P>The returned object will be a specific type of equipment (i.e. PCSDigitalEquipment,
   *  PCSAnalogEquipment,  IDENEquipment, OneRTTEquipment, SIMCardEquipment, etc.).
   *  Use the isXXX() methods to determine which one.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @exception UnknownPhoneNumberException
   *
   */
  Equipment getEquipmentByPhoneNumber(String phoneNumber) throws UnknownPhoneNumberException, TelusAPIException;


/*
   * Returns the Pager equipment associated with a serial number.
   *
   *  <P>The returned object will be a specific to Pager Equipment
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param Cap Code
   * @param Coverage Region
   *
   *
   * @exception UnknownSerialNumberException if the equipment could not be found.
   *
   */
 // PagerEquipment getEquipment(String capCode, String coverageRegion) throws UnknownSerialNumberException, TelusAPIException;

  /**
   * Returns the equipment associated with a product type.
   *
   * <P>The returned object will be a specific type of equipment (i.e. PCSDigitalEquipment,
   *  PCSAnalogEquipment,  IDENEquipment, OneRTTEquipment, SIMCardEquipment, etc.).
   *  Use the isXXX() methods to determine which one.
   *
   * <P>This method may involve a remote method call.
   *
   *
   *
   */
  Equipment getEquipmentbyProductCode(String pProductCode) throws TelusAPIException;
  /**
   * Validates if given serial number can be used for a subscriber. 
   * Checks if being used and reported stolen/lost, 
   * For USIM equipment, check assignable, expired and previously activated as well
   * @param serialNumber Serial Number/SIM ID/USIM ID
   * @return the equipment associated with a serial number
   * @throws TelusAPIException
   * @throws SerialNumberInUseException if equipment is being used by a subscriber or is reported stolen or has been previously activated by another subscriber
   * @throws UnknownSerialNumberException if serial number is invalid
   * @throws InvalidSerialNumberException if equipment is not assignable, or is expired  
   */

  Equipment validateSerialNumber(String serialNumber) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException;

  /**
   * Validates if given serial number can be used for a subscriber. 
   * Checks if being used and reported stolen/lost, 
   * For HSPA equipment check grey market as well
   * For USIM equipment, check assignable, expired and previously activated as well
   * @param serialNumber Serial Number/SIM ID/USIM ID
   * @param brandId
   * @return the equipment associated with a serial number
   * @throws TelusAPIException
   * @throws SerialNumberInUseException if equipment is being used by a subscriber or is reported stolen or has been previously activated by another subscriber
   * @throws UnknownSerialNumberException if serial number is invalid or is grey market
   * @throws InvalidSerialNumberException if equipment is not assignable, or is expired  
   */
  Equipment validateSerialNumber(String serialNumber, int brandId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException; 

  /**
   * Validates if given serial number can be used for a subscriber. Checks if equipment is being used and if be reported stolen, 
   * For HSPA equipment check grey market as well
   * For non USIM HSPA equipment, check if ship to location belongs to the array of activation channel organization ID
   * For USIM equipment, check if assignable, if expired, if previously activated
   * @param serialNumber Serial Number/SIM ID/USIM ID
   * @param activationChannelOrgIds
   * @param brandId
   * @return the equipment associated with a serial number
   * @throws TelusAPIException
   * @throws SerialNumberInUseException if equipment is being used by a subscriber or is reported stolen or has been previously activated by another subscriber
   * @throws UnknownSerialNumberException if serial number is invalid or is grey market
   * @throws InvalidSerialNumberException if equipment is not assignable, or is expired, or ship to location does not in the pass in list of activation channel organization id
   */
  Equipment validateSerialNumber(String serialNumber,long[] activationChannelOrgIds, int brandId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException; 
 
  /**
   * Validates if given serial number can be used for a subscriber. Checks if equipment is being used and if be reported stolen, 
   * For non USIM HSPA equipment and if is grey market as well
   * For USIM equipment, check if assignable, if expired, if previously activated in different subscriber and if grey market as well
   * @param serialNumber Serial Number/SIM ID/USIM ID
   * @param brandId
   * @param subscriptionId Unique number for a subscriber
   * @return the equipment associated with a serial number
   * @throws TelusAPIException
   * @throws SerialNumberInUseException if equipment is being used by a subscriber or is reported stolen or has been previously activated by another subscriber
   * @throws UnknownSerialNumberException if is grey market
   * @throws InvalidSerialNumberException if equipment is not assignable, or is expired  
   */
  Equipment validateSerialNumber(String serialNumber, int brandId, long subscriptionId) throws TelusAPIException, SerialNumberInUseException, UnknownSerialNumberException, InvalidSerialNumberException;
  
  
  /**
   *  UserId should be registered in user_lock% tables on PDIST for master lock access
   */
  String getMasterLockbySerialNo(String pSerialNo, long pLockReasonID, String pUserID) throws MasterLockException, TelusAPIException;

  /**
   *  UserId should be registered in user_lock% tables on PDIST for master lock access
   */
  String getMasterLockbySerialNo(String pSerialNo, long pLockReasonID, long pOutletID, long pChnlOrgID, String pUserID) throws MasterLockException, TelusAPIException;

  /**
   * Creates a new new entry in the equipment datastore for the given
   * serialNumber.  Use <CODE>getEquipment()</CODE> to obtain the newely
   * created Equipment.
   *
   * @see #getEquipment
   */
  void addAnalogSerialNumber(String serialNumber) throws TelusAPIException, UnknownSerialNumberPrefixException,
                                    InvalidSerialNumberException;


   /**
   * Creates a new new entry in the equipment datastore for the given
   * Pager serialNumber.  Use <CODE>getEquipment()</CODE> to obtain the newely
   * created Equipment.
   *
   * @param serialNumber
   * @param capCode
   * @param encodingFormatCode
   * @param coverageRegionCode
   * @param equipmentType
   * @param userID
   * @see #getEquipment
   */
  void addPagerSerialNumber(String serialNumber, String capCode, String encodingFormatCode, String coverageRegionCode, String equipmentType,String userID)  throws TelusAPIException;

  /**
   * Returns the card associated with a serial number.  This method does not perform the card PIN challenge.
   *
   * <P>The returned object will be a specific type of card (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param serialNumber  the card's 11 digit serial number (the card's number minus the last 4 digits).
   *
   * @exception UnknownSerialNumberException if the card could not be found.
   *
   */
  Card getCardBySerialNumber(String serialNumber) throws UnknownSerialNumberException, TelusAPIException;

  /**
   * Returns the card associated with a serial number plus pin.  This method
   * performs the card PIN challenge.
   *
   * <P>The returned object will be a specific type of card (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which one.
   *
   * <P>The returned object will never be <CODE>null</CODE>.
   *
   * <P>This method may involve a remote method call.
   *
   * @param cardNumber the card's 15 digit number (the serial number plus the 4 digit pin).
   * @param subscriber the subsciber this card will be added to.
   *
   * @exception UnknownSerialNumberException   if the card could not be found.
   * @exception InvalidPINException            if the card was found, but the pin doesn't match.
   * @exception TooManyAttemptsException       if the pin validation has failed too many times for this
   *                                           card (perhaps within a period of time).
   *
   */
  Card getCardByCardNumber(String cardNumber, Subscriber subscriber) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException;

  /**
     * Returns the card associated with a serial number plus pin.  This method
     * performs the card PIN challenge.
     *
     * <P>The returned object will be a specific type of card (i.e. FeatureCard, GameCard, or MinuteCard).
     * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which one.
     *
     * <P>The returned object will never be <CODE>null</CODE>.
     *
     * <P>This method may involve a remote method call.
     *
     * @param cardNumber the card's 15 or 12 digit number (if 12 digit number provided, validation will be done
     * with all possible  PIN values).
     * @param subscriber  the subsciber this card will be added to.
     *
     * @exception UnknownSerialNumberException   if the card could not be found.
     * @exception InvalidPINException            if the card was found, but the pin doesn't match.
     * @exception TooManyAttemptsException       if the pin validation has failed too many times for this
     *                                           card (perhaps within a period of time).
     *
     */
    Card getAirCardByCardNumber(String cardNumber, Subscriber subscriber) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException;

    /**
     * Use this method only if phone number or equipment number is not available in the subscriber object. For all other situations,
     * please use getAirCardByCardNumber(String, Subscriber)
     * 
     * This performs the same as getAirCardByCardNumber(String, Subscriber)
     * @param cardNumber
     * @param phoneNumber
     * @param equipmentSerialNo
     * @return Card
     * @throws UnknownSerialNumberException
     * @throws InvalidPINException
     * @throws TooManyAttemptsException
     * @throws TelusAPIException
     */
    Card getAirCardByCardNumber(String cardNumber, String phoneNumber, String equipmentSerialNo) throws UnknownSerialNumberException, InvalidPINException, TooManyAttemptsException, TelusAPIException;

  /**
   * Returns the cards activated on the given phone number.
   *
   * <P>The returned objects will be specific types of cards (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param phoneNumber the subscriber's phoneNumber to search on.
   *
   * @exception UnknownPhoneNumberException
   *
   */
  Card[] getCards(String phoneNumber) throws UnknownPhoneNumberException, TelusAPIException;

  /**
   * Returns the cards, of a specific type, activated on the given phone number.
   *
   * <P>The returned objects will be specific types of cards (i.e. FeatureCard, GameCard, or MinuteCard).
   * Use the isXXX() methods or <CODE>instanceof</CODE> to determine which ones.
   *
   * <P>The array is never <CODE>null</CODE>, and never contains <CODE>null</CODE>
   * elements, but may contain no (zero) elements.  It may also be freely modified.
   *
   * <P>This method may involve a remote method call.
   *
   *
   * @param phoneNumber the subscriber's phoneNumber to search on.
   * @param cardType the only type of cards to return.  One of the Card.TYPE_xxx constants.
   *
   * @exception UnknownPhoneNumberException
   *
   * @see Card
   *
   */
  Card[] getCards(String phoneNumber, String cardType) throws UnknownPhoneNumberException, TelusAPIException;

  /**
   * Get the product base price for a product within a region
   *
   * @param serialNumber serialNumber of equipment where the price is required.  For SIM based handsets, IMEI required.
   * @param province activation province
   * @param npa activation npa (area code)
   */
  double getBaseProductPrice(String serialNumber, String province, String npa) throws TelusAPIException;

  /**
   * Get the shipped to location for serial number
   *
   * @param serialNo serialNumber of equipment where shipped location is required
   * @return long Shipped to Channel Organization ID
   */
  long getShippedToLocation(String serialNo) throws TelusAPIException;
  /**
   * Sets up association between SIM and Mule
   * <P>This method may involve a remote method call.
   * @param sim SIM ID, mandatory
   * @param mule Associated Mule ESN, optional, if null, will retrieve reserved Mule ESN
   * @param activationDate optional, if null, use current date
   * @param eventType mandatory, see constants EVENT_TYPE_SIM_IMEI_XXXX
   * @throws TelusAPIException
   */
  void setSIMMule(String sim, String mule, java.util.Date activationDate, String eventType) throws TelusAPIException;

  void testAddPagerSerialNumber(String serialNumber, String capCode, String encodingFormat, String frequencyCode, String equipmentType, String userId) throws ExistingPagerEquipmentException, InvalidPagerEquipmentException, TelusAPIException;

  public static class Helper {
    private static final String DUMMY_ESN_CELL = "00000000000";
    private static final String DUMMY_ESN_MEID = "000000000000000000";
    private static final int ESN_LENGTH_CELL_HEX = 8;
    private static final int ESN_LENGTH_CELL_DEC = 11;
    private static final int ESN_LENGTH_MEID_HEX = 14;
    private static final int ESN_LENGTH_MEID_DEC = 18;

    private static final Hashtable capCodeFormats;
    private static final Hashtable coverageRegionCodes;
    private static final Hashtable pagerEquipmentTypes;

    static {
      // set valid pager cap code formats
      capCodeFormats = new Hashtable();
      capCodeFormats.put("FL", CapCodeFormat.FORMAT_AH_09_09_09_09_09_09_09);
      capCodeFormats.put("FM", CapCodeFormat.FORMAT_AH_09_09_09_09_09_09_09);
      capCodeFormats.put("FN", CapCodeFormat.FORMAT_AH_09_09_09_09_09_09_09);
      capCodeFormats.put("FO", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("FT", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("FV", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("GL", CapCodeFormat.FORMAT_00_00_09_09_09_09_09_09_09);
      capCodeFormats.put("GM", CapCodeFormat.FORMAT_00_00_09_09_09_09_09_09_09);
      capCodeFormats.put("GN", CapCodeFormat.FORMAT_00_00_09_09_09_09_09_09_09);
      capCodeFormats.put("GO", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("GT", CapCodeFormat.FORMAT_00_00_09_09_09_09_09_09_09);
      capCodeFormats.put("GV", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("NL", CapCodeFormat.FORMAT_02_09_09_09_09_09_09_09);
      capCodeFormats.put("NM", CapCodeFormat.FORMAT_02_09_09_09_09_09_09_09);
      capCodeFormats.put("NN", CapCodeFormat.FORMAT_02_09_09_09_09_09_09_09);
      capCodeFormats.put("NO", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("NT", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("NV", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("PL", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("PM", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("PN", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("PO", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("PT", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("PV", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("SL", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("SM", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("SN", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_03);
      capCodeFormats.put("SO", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("ST", CapCodeFormat.FORMAT_09_09_09_09_09_09_09_09_00);
      capCodeFormats.put("SV", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("YL", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("YM", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("YN", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("YO", CapCodeFormat.FORMAT_00_00_09_09_09_09_09_09_XX);
      capCodeFormats.put("YT", CapCodeFormat.FORMAT_EMPTY);
      capCodeFormats.put("YV", CapCodeFormat.FORMAT_EMPTY);

      // set valid pager coverage region codes
      coverageRegionCodes = new Hashtable();
      coverageRegionCodes.put("E", ":2:4:5:6:7:8:100:101:102:110:123:124:200:201:202:350:350:351:352:353:354:400:401:"
                                 + "402:403:404:2000:2004:2007:2220:2221:2222:2223:2290:2291:2292:2293:2306:2307:2316:");

      coverageRegionCodes.put("F", ":100:101:102:110:123:124:200:201:202:350:351:352:353:354:1011:1021:1033:1034:1035:"
                                 + "1036:1037:1038:1039:1042:1044:1048:1063:1082:1083:1084:1085:1086:1087:1088:1343:"
                                 + "2000:2091:2092:2093:2100:2106:2121:2122:2123:2124:2130:2136:2137:2138:2141:2142:");

      coverageRegionCodes.put("G", ":2:4:5:6:7:8:100:101:102:110:123:124:200:201:202:350:351:352:353:354:400:401:402:"
                                 + "403:404:2000:2004:2220:2221:2222:2223:2290:2291:2292:2293:2306:2307:2316:");

      coverageRegionCodes.put("I", ":2:4:5:6:7:8:100:101:102:110:123:124:200:201:202:350:351:352:353:354:400:401:402:403:404:");

      coverageRegionCodes.put("N", ":2:4:5:6:7:8:100:101:102:110:123:124:200:201:202:350:351:352:353:354:400:401:402:403:404:"
                                 + "2039:2040:2041:2042:2043:2108:2112:2115:2116:2117:2118:2119:");

      coverageRegionCodes.put("P", ":2:4:5:6:7:8:100:101:102:110:123:124:200:201:202:350:351:352:353:354:400:401:402:403:404:"
                                 + "1002:1024:1055:1056:1059:2000:2004:2007:2091:2092:2093:2100:2106:2121:2122:2123:2124:"
                                 + "2130:2136:2137:2138:2141:2142:2220:2221:2222:2223:2290:2291:2292:2293:2306:2307:2316:");

      coverageRegionCodes.put("S", ":5:6:7:8:100:101:102:110:123:124:200:201:202:350:351:352:353:354:400:401:402:403:404:"
                                 + "1002:1024:1055:1056:1059:2000:2004:2007:2091:2092:2093:2100:2106:2121:2122:2123:2124:"
                                 + "2130:2136:2137:2138:2141:2142:2220:2221:2222:2223:2290:2291:2292:2293:2306:2307:2316:");

      coverageRegionCodes.put("Y", ":2:4:5:6:7:8:400:401:402:403:404:2000:2004:2007:2009:2290:2291:2292:2293:");

      coverageRegionCodes.put("Z", ":2:4:5:6:7:8:400:401:402:403:404:2000:2004:2007:2009:2220:2221:2222:2223:2290:2291:2292:2293:");

      // set pager valid equipment types
      pagerEquipmentTypes = new Hashtable();
      pagerEquipmentTypes.put(PagerEquipment.EQUIPMENT_TYPE_VOICE, PagerEquipment.EQUIPMENT_TYPE_GREETING
                                                                 + PagerEquipment.EQUIPMENT_TYPE_TONEANDVOICE
                                                                 + PagerEquipment.EQUIPMENT_TYPE_VOICE);

      pagerEquipmentTypes.put(PagerEquipment.EQUIPMENT_TYPE_TONE, PagerEquipment.EQUIPMENT_TYPE_GREETING);

      pagerEquipmentTypes.put(PagerEquipment.EQUIPMENT_TYPE_NUMERIC, PagerEquipment.EQUIPMENT_TYPE_GREETING
                                                                   + PagerEquipment.EQUIPMENT_TYPE_NUMERIC);

      pagerEquipmentTypes.put(PagerEquipment.EQUIPMENT_TYPE_ALPHA, PagerEquipment.EQUIPMENT_TYPE_GREETING
                                                                 + PagerEquipment.EQUIPMENT_TYPE_ALPHA
                                                                 + PagerEquipment.EQUIPMENT_TYPE_ALPHAMAIL
                                                                 + PagerEquipment.EQUIPMENT_TYPE_NUMERIC
                                                                 + PagerEquipment.EQUIPMENT_TYPE_TONE);
    }

    /**
     * Converts a hexadecimal value of a Serial Number (ESN) or Mobile Equipment IDentifer (MEID) to the corresponding
     * decimal value.
     *
     * @param hexValue Hexadecimal value of a Serial Number (ESN) or Mobile Equipment IDentifer (MEID). Should have
     * 8 characters for ESN numbers and 15 characters for MEID numbers.
     * @return Decimal value of a Serial Number (ESN) or Mobile Equipment IDentifer (MEID).
     */
    public static String hexToSerialNumber(String hexValue){
      if (hexValue == null)
        throw new NullPointerException("Serial number value is null.");

      String esn, dummyEsn;
      int dummyEsnLength;

      if (hexValue.length() == ESN_LENGTH_CELL_HEX) {
        long esnValue = Long.parseLong(hexValue, 16);
        esn = String.valueOf(((esnValue & 0xFF000000L) >> 24) * 100000000 + (esnValue & 0xFFFFFFL));

        dummyEsn = DUMMY_ESN_CELL;
        dummyEsnLength = ESN_LENGTH_CELL_DEC;
      }
      else if (hexValue.length() == ESN_LENGTH_MEID_HEX) {
        long esnValue = Long.parseLong(hexValue, 16);
        esn = String.valueOf(((esnValue & 0xFFFFFFFF000000L) >> 24) * 100000000 + (esnValue & 0xFFFFFFL));

        dummyEsn = DUMMY_ESN_MEID;
        dummyEsnLength = ESN_LENGTH_MEID_DEC;
      }
      else
        throw new IllegalArgumentException("Argument doesn't match either the regular ESN or MEID format: " + hexValue);

      int offset = dummyEsnLength - esn.length();

      if (offset > 0) {
        StringBuffer sb = new StringBuffer(dummyEsn);

        sb.replace(offset, dummyEsnLength, esn);

        esn  = sb.toString();
      }

      return esn;
    }

    /**
     * Implements the capcode formatting.
     *
     * @param capCode Unformatted capcode.
     * @param provinceCode Province code.
     * @param encodingFormat Encoding format.
     * @param equipmentType Equipment type.
     * @return Formatted capcode.
     */
    public static String getFormattedCapCode(String capCode, String provinceCode, String encodingFormat, String equipmentType) {
      // return object
      String formattedCapCode = capCode;

      // convert FLEX pagers
      if (encodingFormat.equals(PagerEquipment.ENCODING_FORMAT_FLEX)) {
        if ((capCode.startsWith("A") || capCode.startsWith("E")) && capCode.substring(1, 2).equals("0") && capCode.length() == 9)
          formattedCapCode = capCode.substring(0, 1) + capCode.substring(2);

        if ((capCode.startsWith("A") || capCode.startsWith("E")) && capCode.substring(1, 3).equals("00") && capCode.length() == 10)
          formattedCapCode = capCode.substring(0, 1) + capCode.substring(3);
      }

      // convert POCSAG and S-POCSAG pagers
      if (encodingFormat.equals(PagerEquipment.ENCODING_FORMAT_POCSAG) || encodingFormat.equals(PagerEquipment.ENCODING_FORMAT_SPOCSAG)) {
        // ALPHA
        if (equipmentType.equals(PagerEquipment.EQUIPMENT_TYPE_ALPHA)) {
          if (provinceCode.equals(Province.PROVINCE_AB))
            formattedCapCode = "0" + capCode + "2";
          else
            formattedCapCode = "0" + capCode + "3";
        }
        else {
          if (provinceCode.equals(Province.PROVINCE_AB))
            formattedCapCode = "0" + capCode + "0";
          else
            formattedCapCode = "0" + capCode + "3";
        }
      }

      // convert GOLAY pagers
      if (encodingFormat.equals(PagerEquipment.ENCODING_FORMAT_GOLAY)) {
        if (provinceCode.equals(Province.PROVINCE_AB))
          formattedCapCode = "00" + capCode + "0";
        else
          formattedCapCode = "00" + capCode + "3";
      }

      // convert 5-TONE pagers
      if (encodingFormat.equals(PagerEquipment.ENCODING_FORMAT_5TONE))
        formattedCapCode = "00" + capCode + "X";

      return formattedCapCode;
    }

    /**
     * Converts a formatted capcode into the unformatted one.
     *
     * @param formattedCapCode Formatted capcode.
     * @param encodingFormat Capcode encoding format.
     * @return Unformatted capcode.
     */
    public static String getUnformattedCapCode(String formattedCapCode, String encodingFormat) {
      // return object
      String capCode;

      if (formattedCapCode == null || encodingFormat == null)
        throw new NullPointerException("Formatted capcode or encoding format is null.");

      if (formattedCapCode.length() != 8 && formattedCapCode.length() != 9)
        throw new IllegalArgumentException("Invalid formatted capcode: " + formattedCapCode);

      if (encodingFormat.equals("F")) {
        if (formattedCapCode.substring(1, 2).equals("0") || formattedCapCode.substring(1, 2).equals("1") || formattedCapCode.substring(1, 3).equals("20")) {
          capCode = formattedCapCode;
        }
        else {
          capCode = "E00" + formattedCapCode.substring(1, 8);
        }
      }
      else if (encodingFormat.equals("P") || encodingFormat.equals("S")) {
          capCode = formattedCapCode.substring(1, 8);
      }
      else if (encodingFormat.equals("G") || encodingFormat.equals("Y")) {
        capCode = formattedCapCode.substring(2, 8);
      }
      else
        capCode = formattedCapCode;

      return capCode;
    }

    /**
     * Checks if the given piece of equipment was ever active on the given account.
     *
     * @param equipment Piece of equipment to be checked.
     * @param account Account to be checked.
     * @return <tt>true</tt> if <tt>equipment</tt> was ever active on <tt>account</tt>; <tt>false</tt> otherwise.
     * @throws TelusAPIException if the remote call to the database fails.
     */
    public static boolean wasActiveOnAccount(Equipment equipment, AccountSummary account) throws TelusAPIException {
      return equipment.isInUseOnBan(account.getBanId(), false);
    }

    /**
     * Checks if the given piece of equipment is currently active on the given account.
     *
     * @param equipment Piece of equipment to be checked.
     * @param account Account to be checked.
     * @return <tt>true</tt> if <tt>equipment</tt> is now active on <tt>account</tt>; <tt>false</tt> otherwise.
     * @throws TelusAPIException if the remote call to the database fails.
     */
    public static boolean isActiveOnAccount(Equipment equipment, AccountSummary account) throws TelusAPIException {
      return equipment.isInUseOnBan(account.getBanId(), true);
    }

    /**
     * Checks if the given piece of equipment was ever active on any account other than the given one.
     *
     * @param equipment Piece of equipment to be checked.
     * @param account Account to be checked.
     * @return <tt>true</tt> if <tt>equipment</tt> was ever active on any account other than <tt>account</tt>;
     * <tt>false</tt> otherwise.
     * @throws TelusAPIException if the remote call to the database fails.
     */
    public static boolean wasActiveOnAnotherAccount(Equipment equipment, AccountSummary account) throws TelusAPIException {
      return equipment.isInUseOnAnotherBan(account.getBanId(), false);
    }

    public static boolean isValidCapCodeFormat(String encodingFormat, String equipmentType, String formattedCapCode) {
      CapCodeFormat capCodeFormat = (CapCodeFormat) capCodeFormats.get(encodingFormat + equipmentType);

      if (capCodeFormat == null)
        return false;

      return capCodeFormat.isValidCapCodeFormat(formattedCapCode);
    }

    public static boolean isValidEquipmentType(String modelType, String equipmentType) {
      String equipmentTypes = (String) pagerEquipmentTypes.get(modelType);

      if (equipmentTypes == null)
        return false;

      return equipmentTypes.indexOf(equipmentType) >= 0;
    }

    public static boolean isValidCoverageRegionCode(String encodingFormat, String regionCode) {
      String regionCodes = (String) coverageRegionCodes.get(encodingFormat);

      if (regionCodes == null)
        return false;

      return regionCodes.indexOf(":" + regionCode + ":") >= 0;
    }

    private static class CapCodeFormat {
      private final CapCodeFormatField[] fields;
      private final int minLength;
      private final int maxLength;

      private static final CapCodeFormat FORMAT_EMPTY =
        new CapCodeFormat(new CapCodeFormatField[] {});

      private static final CapCodeFormat FORMAT_AH_09_09_09_09_09_09_09 =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_AH,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09});

      private static final CapCodeFormat FORMAT_00_00_09_09_09_09_09_09_09 =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_00,
                                                    CapCodeFormatField.FIELD_00,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09});

      private static final CapCodeFormat FORMAT_02_09_09_09_09_09_09_09 =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_02,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09});

      private static final CapCodeFormat FORMAT_09_09_09_09_09_09_09_09_03 =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_03});

      private static final CapCodeFormat FORMAT_09_09_09_09_09_09_09_09_00 =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_00});

      private static final CapCodeFormat FORMAT_00_00_09_09_09_09_09_09_XX =
        new CapCodeFormat(new CapCodeFormatField[] {CapCodeFormatField.FIELD_00,
                                                    CapCodeFormatField.FIELD_00,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_09,
                                                    CapCodeFormatField.FIELD_XX});

      private CapCodeFormat(CapCodeFormatField[] fields) {
        int fieldsSz = fields != null ? fields.length : 0;

        this.fields = new CapCodeFormatField[fieldsSz];

        int minLength = 0;

        for (int i = 0; i < fieldsSz; i++) {
          this.fields[i] = fields[i];

          if (!this.fields[i].isOptional())
            minLength++;
        }

        this.minLength = minLength;
        this.maxLength = this.fields.length;
      }

      boolean isValidCapCodeFormat(String format) {
        if (format == null)
          return false;

        if (format.length() < minLength || format.length() > maxLength)
          return false;

        for (int i = 0; i < format.length(); i++)
          if (!fields[i].isValidFieldCharacter(format.charAt(i)))
            return false;

        return true;
      }
    }

    private static class CapCodeFormatField  {
      private final char min;
      private final char max;
      private final boolean optional;

      private static final CapCodeFormatField FIELD_00 = new CapCodeFormatField('0', '0');
      private static final CapCodeFormatField FIELD_02 = new CapCodeFormatField('0', '2');
      private static final CapCodeFormatField FIELD_03 = new CapCodeFormatField('0', '3');
      private static final CapCodeFormatField FIELD_09 = new CapCodeFormatField('0', '9');
      private static final CapCodeFormatField FIELD_AH = new CapCodeFormatField('A', 'H');
      private static final CapCodeFormatField FIELD_XX = new CapCodeFormatField('X', 'X', true);

      private CapCodeFormatField(char min, char max, boolean optional) {
        this.min = min;
        this.max = max;
        this.optional = optional;
      }

      private CapCodeFormatField(char min, char max) {
        this.min = min;
        this.max = max;
        this.optional = false;
      }

      private boolean isOptional() {
        return optional;
      }

      private boolean isValidFieldCharacter(char c) {
        return c >= min && c <= max;
      }
    }
  }
  public double getBaseProductPriceByProductCode(String productCode, String province, String npa) throws TelusAPIException;
}
