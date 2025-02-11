/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.message.ApplicationMessage;

import java.util.Locale;

public class InvalidEquipmentChangeException extends TelusAPIException {

  private static final long serialVersionUID = 1L;
  
  public static final int UNKNOWN                                               = 0;
  public static final int SUBSCRIBER_INFO_NULL                                  = 4;
  public static final int MANDATORY_FIELDS_MISSING                              = 5;
  public static final int OLD_EQUIPMENT_NOT_FOUND                               = 6;
  public static final int DEALER_INFO_NOT_FOUND                                 = 7;
  public static final int TECH_TYPE_NOT_COMPATIBLE                              = 8;
  public static final int NEW_EQUIPMENT_IS_LOST_STOLEN                          = 9;
  public static final int MANDATORY_EQUIPMENT_INFO_NULL                         = 10;
  public static final int NO_REPLACEMENT_FOR_MULE2MULE                          = 11;
  public static final int NO_LOANER_FOR_SIM2SIM                                 = 12;
  public static final int REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM = 13;
  public static final int REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM                   = 14;
  public static final int OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR            = 15;
  public static final int ASSOCIATED_MULE_MANDATORY_FOR_SIM2HAND_HAND2SIM       = 16;
  public static final int ASSOCIATED_MULE_NOT_MULE_FOR_SIM2HAND_HAND2SIM        = 17;
  public static final int ASSOCIATED_MULE_AND_NEW_EQUIPMENT_SERIAL_MUST_BE_DIFF = 18;
  public static final int WARRANTY_INFO_NOT_FOUND                               = 19;
  public static final int IMPOSSIBLE_SWAP_TYPES                                 = 20;
  public static final int EQUIPMENT_TYPE_IS_NULL                                = 21;
  public static final int OLD_NEW_EQUIPMENT_CANNOT_BE_SAME                      = 22;
  public static final int INVALID_SUBSCRIBER_STATUS                             = 23;
  public static final int PCS_TO_RIM_SWAP                                       = 24;
  public static final int RIM_TO_PCS_SWAP                                       = 25;
  public static final int REPLACEMENT_ONLY                                      = 26;
  public static final int SERVICE_ADDITION_FAILED                               = 27;
  public static final int SERVICE_REMOVAL_FAILED                                = 28;
  public static final int INVALID_SWAP_FOR_PREPAID_ACCOUNT                      = 29;
  public static final int BRANDS_NOT_COMPATIBLE                                 = 30;
  public static final int UNSUPPORTED_EQUIPMENT 								= 31;
  public static final int SIMPROFILE_MISMATCH 									= 32;

  private boolean messageOn;

  public InvalidEquipmentChangeException(Throwable exception, int reason) {
    super(exception);
    this.reason = reason;
  }

  public InvalidEquipmentChangeException(String message, Throwable exception, int reason) {
    super(message, exception);
    this.reason = reason;
  }

  public InvalidEquipmentChangeException(String message, Throwable exception) {
    super(message, exception);
  }

  public InvalidEquipmentChangeException(String message, int reason) {
    super(message);
    this.reason = reason;
  }

  public InvalidEquipmentChangeException(String message, ApplicationMessage applicationMessage, int reason) {
    super(message + applicationMessage.getText(Locale.ENGLISH));
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public InvalidEquipmentChangeException(ApplicationMessage applicationMessage, int reason) {
    super(applicationMessage.getText(Locale.ENGLISH));
    this.applicationMessage = applicationMessage;
    this.reason = reason;
  }

  public InvalidEquipmentChangeException(Throwable exception) {
    super(exception);
  }

  public InvalidEquipmentChangeException(String message) {
    super(message);
  }

  public int getReason() {
    return reason;
  }

  public boolean isMessageOn() {
    return messageOn;
  }

  public void setMessageOn(boolean messageOn) {
    this.messageOn = messageOn;
  }

  public String getReasonText() {
    switch (reason) {
      case  UNKNOWN:
        return "Unknown.";
      case  SUBSCRIBER_INFO_NULL:
        return "Subscriber info is null.";
      case  MANDATORY_FIELDS_MISSING:
        return "Mandatory field(s) are missing or invalid.";
      case  OLD_EQUIPMENT_NOT_FOUND:
        return "Old equipment info not found.";
      case  DEALER_INFO_NOT_FOUND:
        return "Dealer info not found.";
      case  TECH_TYPE_NOT_COMPATIBLE:
        return "Incompatible technology or product class.";
      case  NEW_EQUIPMENT_IS_LOST_STOLEN:
        return "New equipment has a wrong equipment status (lost/stolen).";
      case  MANDATORY_EQUIPMENT_INFO_NULL:
        return "Data problem: mandatory equipment field(s) are null.";
      case  NO_REPLACEMENT_FOR_MULE2MULE:
        return "No 'replacement' for a mule-to-mule swap.";
      case  NO_LOANER_FOR_SIM2SIM:
        return "No 'loaner repair or repair' for a sim-to-sim swap.";
      case  REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM:
        return "Valid repair ID is mandatory except for 'replacement' or sim-to-sim.";
      case  REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM:
        return "Repair ID must be unique for 'repair' except for sim-to-sim.";
      case  OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR:
        return "Old/new product type must be the same for 'repair.'";
      case  ASSOCIATED_MULE_MANDATORY_FOR_SIM2HAND_HAND2SIM:
        return "Associated mule serial number is mandatory for swaps involving sim, except for sim-to-sim.";
      case  ASSOCIATED_MULE_NOT_MULE_FOR_SIM2HAND_HAND2SIM:
        return "Associated mule must be a mule for a sim-to-hand or hand-to-sim swap.";
      case  ASSOCIATED_MULE_AND_NEW_EQUIPMENT_SERIAL_MUST_BE_DIFF:
        return "Associated mule serial number AND new serial number must be different.";
      case  WARRANTY_INFO_NOT_FOUND:
        return "Warranty info not found.";
      case  IMPOSSIBLE_SWAP_TYPES:
        return "Impossible swap types: sim-to-mule, hand-to-mule, mule-to-sim, mule-to-hand.";
      case  EQUIPMENT_TYPE_IS_NULL:
        return "The new PCS equipment type is null, which is not allowed.";
      case  OLD_NEW_EQUIPMENT_CANNOT_BE_SAME:
        return "New and old equipment cannot be the same for Mule warranty transfer.";
      case  INVALID_SUBSCRIBER_STATUS:
        return "Invalid subscriber status.";
      case PCS_TO_RIM_SWAP:
        return "New Equipment is RIM, but Old Equipment is not Analog, PCS, 1xRTT handset, or RIM.";
      case RIM_TO_PCS_SWAP:
        return "Old Equipment is RIM, but New Equipment is not Analog, PCS, 1xRTT handset, or RIM.";
      case REPLACEMENT_ONLY:
        return "Replacement swap only.";
      case SERVICE_ADDITION_FAILED:
        return "Service addition failed";
      case SERVICE_REMOVAL_FAILED:
        return "Service removal failed";
      case INVALID_SWAP_FOR_PREPAID_ACCOUNT:
        return "Equipment swaps for prepaid subscribers to 1xRTT Card, RIM or PDA are forbidden.";
      case  BRANDS_NOT_COMPATIBLE:
        return "Incompatible brands.";
      case UNSUPPORTED_EQUIPMENT:
    	  return "The new equipment is not supported.";
      case SIMPROFILE_MISMATCH:
    	  return "The new equipment is not compatible with the existing equipment.";
      default:
        return "Unknown.";
    }
  }

//  public String getReasonText() {
//    return super.getReasonText();
//  }

//  public String getReasonTextFrench() {
//    return super.getReasonTextFrench();
//  }

  public String getReasonText(String businessRole) {
    return super.getReasonText(businessRole);
  }


  public String getReasonTextFrench(String businessRole) {
    return super.getReasonTextFrench(businessRole);
  }


  static {
    setReasonText(UNKNOWN                                                ,"Handset exchange failed - reason unknown.", "Échec de l'opération d'échange de l'appareil - Motif inconnu.");
    setReasonText(1                                                      ,"Handset exchange not authorized - handset is under lease.", "Échange de l'appareil non autorisé - Appareil en location.");
    setReasonText(SUBSCRIBER_INFO_NULL                                   ,"Handset exchange failed - missing subscriber information.", "Échec de l'opération d'échange de l'appareil - Information manquante sur l'abonné.");
    setReasonText(MANDATORY_FIELDS_MISSING                               ,"Handset exchange failed - required equipment information is missing or invalid.", "Échec de l'opération d'échange de l'appareil - Information requise sur l'équipement manquante ou non valide.");
    setReasonText(OLD_EQUIPMENT_NOT_FOUND                                ,"Handset exchange failed - existing equipment information is not found.", "Échec de l'opération d'échange de l'appareil - Information sur l'équipement existant introuvable.");
    setReasonText(DEALER_INFO_NOT_FOUND                                  ,"Handset exchange failed - invalid dealer information.", "Échec de l'opération d'échange de l'appareil - Information sur le détaillant non valide.");
    setReasonText(TECH_TYPE_NOT_COMPATIBLE                               ,"Handset exchange failed - incompatible technology types.", "Échec de l'opération d'échange de l'appareil - Types de technologie non compatibles.");
    setReasonText(NEW_EQUIPMENT_IS_LOST_STOLEN                           ,"Handset exchange failed - please contant Client Care/Channel Care.", "Échec de l'opération d'échange de l'appareil - Veuillez communiquer avec le Service à la clientèle ou les Services aux réseaux de distribution.");
    setReasonText(MANDATORY_EQUIPMENT_INFO_NULL                          ,"Handset exchange failed - required equipment information is missing or invalid.", "Échec de l'opération d'échange de l'appareil - Information requise sur l'équipement manquante ou non valide.");
    setReasonText(NO_REPLACEMENT_FOR_MULE2MULE                           ,"Handset exchange failed - replacement is not allowed for SIM handset to SIM handset swap.", "Échec de l'opération d'échange de l'appareil - Remplacement non autorisé pour un échange de type appareil SIM à appareil SIM.");
    setReasonText(NO_LOANER_FOR_SIM2SIM                                  ,"Handset exchange failed - repair or loaner is not allowed for SIM handset to SIM handset swap.", "Échec de l'opération d'échange de l'appareil - Prêt d'un appareil non autorisé pour un échange de type appareil SIM à appareil SIM.");
    setReasonText(REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM  ,"Handset exchange failed - repair form number is required.", "Échec de l'opération d'échange de l'appareil - Numéro du bon de réparation requis.");
    setReasonText(REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM                    ,"Handset exchange failed - repair form number already exists.", "Échec de l'opération d'échange de l'appareil - Numéro du bon de réparation déjà existant.");
    setReasonText(OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR             ,"Handset exchange failed - old and new product type must be the same for repair.", "Échec de l'opération d'échange de l'appareil - L'ancien et le nouveau produits doivent être du même type pour une réparation.");
    setReasonText(ASSOCIATED_MULE_MANDATORY_FOR_SIM2HAND_HAND2SIM        ,"Handset exchange failed - associated SIM handset serial number is required.", "Échec de l'opération d'échange de l'appareil - Numéro de série de l'appareil SIM associé requis.");
    setReasonText(ASSOCIATED_MULE_NOT_MULE_FOR_SIM2HAND_HAND2SIM         ,"Handset exchange failed - associated SIM handset is invalid.", "Échec de l'opération d'échange de l'appareil - Appareil SIM associé non valide.");
    setReasonText(ASSOCIATED_MULE_AND_NEW_EQUIPMENT_SERIAL_MUST_BE_DIFF  ,"Handset exchange failed - associated SIM handset serial number and new serial number can not be the same number.", "Échec de l'opération d'échange de l'appareil - Le numéro de série de l'appareil SIM associé ne peut être semblable au nouveau numéro de série.");
    setReasonText(WARRANTY_INFO_NOT_FOUND                                ,"Handset exchange failed - warranty information not found.", "Échec de l'opération d'échange de l'appareil - Information sur la garantie introuvable.");
    setReasonText(IMPOSSIBLE_SWAP_TYPES                                  ,"Handset exchange failed - incompatible hardware.", "Échec de l'opération d'échange de l'appareil - Matériel non compatible.");
    setReasonText(EQUIPMENT_TYPE_IS_NULL                                 ,"Handset exchange failed - missing equipment type.", "Échec de l'opération d'échange de l'appareil - Types d'équipement manquants.");

//    Missing messages
//    OLD_NEW_EQUIPMENT_CANNOT_BE_SAME
//    INVALID_SUBSCRIBER_STATUS
//    PCS_TO_RIM_SWAP
//    RIM_TO_PCS_SWAP
//    REPLACEMENT_ONLY
//    SERVICE_ADDITION_FAILED
//    SERVICE_REMOVAL_FAILED

  }

  private static void setReasonText(int reason, String englishText, String frenchText) {
    setReasonText(InvalidEquipmentChangeException.class, reason, englishText, frenchText);
  }


}


