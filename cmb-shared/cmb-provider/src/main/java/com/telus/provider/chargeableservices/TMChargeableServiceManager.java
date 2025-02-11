/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.provider.chargeableservices;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.api.chargeableservices.*;
import com.telus.api.reference.*;
import com.telus.eas.config.info.*;
import com.telus.provider.*;
import com.telus.provider.account.*;
import java.util.*;

public class TMChargeableServiceManager extends BaseProvider implements ChargeableServiceManager {

  public static final String CHARGE_PCS_HANDSET_EXCHANGE_AGENT       = "HSWAPC";
  public static final String CHARGE_IDEN_HANDSET_EXCHANGE_AGENT      = "HSWAPM";
  public static final String CHARGE_PREPAID_HANDSET_EXCHANGE_AGENT   = "214";

  public static final String CHARGE_PRICEPLAN_CHANGE                 = "PCF1";

  public static final String CHARGE_PCS_HANDSET_EXCHANGE_CLIENT      = "HSWPCC";
  public static final String CHARGE_IDEN_HANDSET_EXCHANGE_CLIENT     = "HSWPCM";
  public static final String CHARGE_PREPAID_HANDSET_EXCHANGE_CLIENT  = "215";

  public static final String CHARGE_POSTPAID_PHONE_NUMBER_CHANGE     = "PNUM";
  public static final String CHARGE_PREPAID_PHONE_NUMBER_CHANGE   	 = "216";

  private static final double PHONE_NUMBER_CHANGE_FEE = 25.0;
  private static final double PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_REGULAR_SEARCH_FEE = 15.0;
  private static final double PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_PRESTIGE_SEARCH_FEE = 25.0;
  private static final String PHONE_NUMBER_CHANGE_TEXT_EN = "Phone Number Change";
  private static final String PHONE_NUMBER_CHANGE_TEXT_FR  = "Changement de numéro de téléphone";

  private static final String MOVE_WAIVER_TEXT_EN = "Move";
  private static final String MOVE_WAIVER_TEXT_FR = "Déménagement";
  private static final String NUISANCE_WAIVER_TEXT_EN = "Nuisance Calls";
  private static final String NUISANCE_WAIVER_TEXT_FR = "Appels importuns";
  private static final String REACTIVATION_WAIVER_TEXT_EN = "Reactivation";
  private static final String REACTIVATION_WAIVER_TEXT_FR = "Remise en service";
  private static final String TRANSFER_OWNER_WAIVER_TEXT_EN = "Transfer of Ownership";
  private static final String TRANSFER_OWNER_WAIVER_TEXT_FR = "Transfert de responsabilité";
  private static final String ERROR_WAIVER_TEXT_EN = "Error";
  private static final String ERROR_WAIVER_TEXT_FR = "Erreur";
  private static final String AREA_SPLIT_WAIVER_TEXT_EN = "Area code split";
  private static final String AREA_SPLIT_WAIVER_TEXT_FR = "Fractionnement de l'indicatif régional";
  private static final String HOME_AREA_WAIVER_TEXT_EN = "Home Service Area";
  private static final String HOME_AREA_WAIVER_TEXT_FR = "Zone d'attache";


  public static final Map chargeableServices = new HashMap();

  public String getKey(String serviceName, String roleName) {
    return serviceName + "--" + roleName;
  }


  public TMChargeableServiceManager(TMProvider provider) {
    super(provider);
    setup();
  }

  private void setup() {
    TMChargeableServiceDescriptor descriptor;

    descriptor = newChargeableServiceDescriptor(SERVICE_PREPAID_TOP_UPS, BusinessRole.BUSINESS_ROLE_AGENT, 3.00, "Prepaid top-Up", "Réapprovisionnement prépayé");
    descriptor.newWaiver(Waiver.WAIVER_FIRST_TIME, "First time walk-through", "Première utilisation du système");
    descriptor.newWaiver(Waiver.WAIVER_IVRU_DOWN, "IVRU is down", "Système RVI en panne");
    descriptor.newWaiver(Waiver.WAIVER_PIN_NOT_WORKING, "PIN not working (ticket opened)", "NIP non valide (billet ouvert)");
    descriptor.newWaiver(Waiver.WAIVER_DUPLICATE_FEE, "Credit back duplicate fee", "Remboursement des frais de copie");

    descriptor = newChargeableServiceDescriptor(SERVICE_PREPAID_FEATURE_ADD, BusinessRole.BUSINESS_ROLE_AGENT, 3.00, "Prepaid feature add", "Ajout de fonctions prépayées");
    descriptor.newWaiver(Waiver.WAIVER_FIRST_TIME, "First time walk-through", "Première utilisation du système");
    descriptor.newWaiver(Waiver.WAIVER_888_DOWN, "#888 is down", "#888 en panne");
    descriptor.newWaiver(Waiver.WAIVER_123_DOWN, "#123 is down", "#123 en panne");
    descriptor.newWaiver(Waiver.WAIVER_PIN_NOT_WORKING, "PIN not working (ticket opened)", "NIP non valide (billet ouvert)");
    descriptor.newWaiver(Waiver.WAIVER_DUPLICATE_FEE, "Credit back duplicate fee", "Remboursement des frais de copie");

    descriptor = newChargeableServiceDescriptor(SERVICE_HANDSET_EXCHANGE, BusinessRole.BUSINESS_ROLE_AGENT, 25.00, "Handset exchange", "Échange d’appareil");
    descriptor.newWaiver(Waiver.WAIVER_CONTRACT_RENEWAL, "Contract Renewal", "Renouvellement de contrat");
    descriptor.newWaiver(Waiver.WAIVER_SECONDARY_EQUIPMENT, "Secondary Equipment Swap/Activation", "Échange ou mise en service d'un appareil additionnel");
    descriptor.newWaiver(Waiver.WAIVER_FOR_DEALER, "Dealer initiated Swap (Channel Care Only)", "Échange par détaillant (Réseaux de distribution)");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_INITIATIVE, "TELUS initiated program (ie: PRL Upgrade)", "Programme de TELUS (p. ex., mise à jour de la PRL)");
    descriptor.newWaiver(Waiver.WAIVER_FREQUENT, "Frequent Handset Exchanges (AB, BC only)", "Échanges fréquents d'appareils (Alb., C.-B.)");

    descriptor = newChargeableServiceDescriptor(SERVICE_PRICEPLAN_CHANGE, BusinessRole.BUSINESS_ROLE_AGENT, 10.00, "Price Plan change 10.00", "Frais de changement de forfait de 10 $");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_FIRST_TIME, "1st RP Change - $0 Charge", "1er changement de forfait sans frais");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_SECOND_TIME, "2nd RP Change - $0 Charge", "2e changement de forfait sans frais");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_GOLD_CLIENT, "Gold Client", "Client Or");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_MISAPPLIED, "Misapplied Rate Plan", "Forfait mal choisi");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_INITIATED_CAMPAIGN, "Telus Initiated Campaign", "Campagne initiée par TELUS");
    descriptor.newWaiver(Waiver.WAIVER_TELUS_VAD, "Vacation disconnect - No charge", "Suspension temporaire - Sans frais");

    descriptor = newChargeableServiceDescriptor(SERVICE_HANDSET_EXCHANGE, BusinessRole.BUSINESS_ROLE_CLIENT, 10.00, "Handset exchange", "Échange d’appareil");

    descriptor = newChargeableServiceDescriptor(SERVICE_MIKE_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_AGENT, 35.00, "Mike activation fee", "Frais de mise en service Mike");

    descriptor = newChargeableServiceDescriptor(SERVICE_PCS_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_CLIENT, 10.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "pre-paid Activation", "compte avec service prépayé");

    descriptor = newChargeableServiceDescriptor(SERVICE_PCS_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_AGENT, 35.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "pre-paid Activation", "compte avec service prépayé");

    descriptor = newChargeableServiceDescriptor(SERVICE_PCS_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_CORPORATE_STORE, 35.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "pre-paid Activation", "compte avec service prépayé");

    descriptor = newChargeableServiceDescriptor(SERVICE_PCS_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_RETAIL_STORE, 35.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "pre-paid Activation", "compte avec service prépayé");

    descriptor = newChargeableServiceDescriptor(SERVICE_ONEXRTT_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_AGENT, 60.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "Failed in Gemini", "panne du sous-système Gemini");

    descriptor = newChargeableServiceDescriptor(SERVICE_ONEXRTT_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_CORPORATE_STORE, 60.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "Failed in Gemini", "panne du sous-système Gemini");

    descriptor = newChargeableServiceDescriptor(SERVICE_ONEXRTT_ACTIVATION_FEE, BusinessRole.BUSINESS_ROLE_RETAIL_STORE, 60.00, "PCS activation fee", "Frais de mise en service PCS");
    descriptor.newWaiver(Waiver.WAIVER_NO_CHARGE, "Failed in Gemini", "panne du sous-système Gemini");

    descriptor = newChargeableServiceDescriptor(SERVICE_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_AGENT, PHONE_NUMBER_CHANGE_FEE, PHONE_NUMBER_CHANGE_TEXT_EN,PHONE_NUMBER_CHANGE_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_MOVE, MOVE_WAIVER_TEXT_EN, MOVE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_NUISANCE, NUISANCE_WAIVER_TEXT_EN, NUISANCE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_REACTIVATION, REACTIVATION_WAIVER_TEXT_EN, REACTIVATION_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_TRANSFER_OWNER, TRANSFER_OWNER_WAIVER_TEXT_EN, TRANSFER_OWNER_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_ERROR, ERROR_WAIVER_TEXT_EN, ERROR_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_AREA_SPLIT, AREA_SPLIT_WAIVER_TEXT_EN, AREA_SPLIT_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_HOME_AREA, HOME_AREA_WAIVER_TEXT_EN, HOME_AREA_WAIVER_TEXT_FR);

    descriptor = newChargeableServiceDescriptor(SERVICE_PAGER_REGULAR_SEARCH_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_AGENT, PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_REGULAR_SEARCH_FEE, PHONE_NUMBER_CHANGE_TEXT_EN,PHONE_NUMBER_CHANGE_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_MOVE, MOVE_WAIVER_TEXT_EN, MOVE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_NUISANCE, NUISANCE_WAIVER_TEXT_EN, NUISANCE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_REACTIVATION, REACTIVATION_WAIVER_TEXT_EN, REACTIVATION_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_TRANSFER_OWNER, TRANSFER_OWNER_WAIVER_TEXT_EN, TRANSFER_OWNER_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_ERROR, ERROR_WAIVER_TEXT_EN, ERROR_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_AREA_SPLIT, AREA_SPLIT_WAIVER_TEXT_EN, AREA_SPLIT_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_HOME_AREA, HOME_AREA_WAIVER_TEXT_EN, HOME_AREA_WAIVER_TEXT_FR);

    descriptor = newChargeableServiceDescriptor(SERVICE_PAGER_PRESTIGE_SEARCH_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_AGENT, PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_PRESTIGE_SEARCH_FEE, PHONE_NUMBER_CHANGE_TEXT_EN,PHONE_NUMBER_CHANGE_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_MOVE, MOVE_WAIVER_TEXT_EN, MOVE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_NUISANCE, NUISANCE_WAIVER_TEXT_EN, NUISANCE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_REACTIVATION, REACTIVATION_WAIVER_TEXT_EN, REACTIVATION_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_TRANSFER_OWNER, TRANSFER_OWNER_WAIVER_TEXT_EN, TRANSFER_OWNER_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_ERROR, ERROR_WAIVER_TEXT_EN, ERROR_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_AREA_SPLIT, AREA_SPLIT_WAIVER_TEXT_EN, AREA_SPLIT_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_HOME_AREA, HOME_AREA_WAIVER_TEXT_EN, HOME_AREA_WAIVER_TEXT_FR);

    descriptor = newChargeableServiceDescriptor(SERVICE_AUTOTEL_REGULAR_SEARCH_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_AGENT, PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_REGULAR_SEARCH_FEE, PHONE_NUMBER_CHANGE_TEXT_EN,PHONE_NUMBER_CHANGE_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_MOVE, MOVE_WAIVER_TEXT_EN, MOVE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_NUISANCE, NUISANCE_WAIVER_TEXT_EN, NUISANCE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_REACTIVATION, REACTIVATION_WAIVER_TEXT_EN, REACTIVATION_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_TRANSFER_OWNER, TRANSFER_OWNER_WAIVER_TEXT_EN, TRANSFER_OWNER_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_ERROR, ERROR_WAIVER_TEXT_EN, ERROR_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_AREA_SPLIT, AREA_SPLIT_WAIVER_TEXT_EN, AREA_SPLIT_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_HOME_AREA, HOME_AREA_WAIVER_TEXT_EN, HOME_AREA_WAIVER_TEXT_FR);

    descriptor = newChargeableServiceDescriptor(SERVICE_AUTOTEL_PRESTIGE_SEARCH_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_AGENT, PHONE_NUMBER_CHANGE_PAGING_AUTOTEL_PRESTIGE_SEARCH_FEE, PHONE_NUMBER_CHANGE_TEXT_EN,PHONE_NUMBER_CHANGE_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_MOVE, MOVE_WAIVER_TEXT_EN, MOVE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_NUISANCE, NUISANCE_WAIVER_TEXT_EN, NUISANCE_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_REACTIVATION, REACTIVATION_WAIVER_TEXT_EN, REACTIVATION_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_TRANSFER_OWNER, TRANSFER_OWNER_WAIVER_TEXT_EN, TRANSFER_OWNER_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_ERROR, ERROR_WAIVER_TEXT_EN, ERROR_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_AREA_SPLIT, AREA_SPLIT_WAIVER_TEXT_EN, AREA_SPLIT_WAIVER_TEXT_FR);
    descriptor.newWaiver(Waiver.WAIVER_PHONE_CHANGE_HOME_AREA, HOME_AREA_WAIVER_TEXT_EN, HOME_AREA_WAIVER_TEXT_FR);

    descriptor = newChargeableServiceDescriptor(ECARE_SERVICE_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_CLIENT, 15.00, "Phone Number Change - SS", "Changement de numéro - LS");
    descriptor = newChargeableServiceDescriptor(ECARE_SERVICE_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_CORPORATE_STORE, 15.00, "Phone Number Change - SS", "Changement de numéro - LS");
    descriptor = newChargeableServiceDescriptor(ECARE_SERVICE_PHONE_NUMBER_CHANGE_FEE, BusinessRole.BUSINESS_ROLE_DEALER, 15.00, "Phone Number Change - SS", "Changement de numéro - LS");
  }

  public ChargeableService getChargeableService(String serviceName, String roleName, Subscriber subscriber) throws UnknownObjectException, TelusAPIException {
    if(roleName == null) {
      throw new NullPointerException("roleName is null");
    }

    if(roleName.equals(BusinessRole.BUSINESS_ROLE_ALL)) {
      throw new TelusAPIException("BUSINESS_ROLE_ALL is not allowed, use a specific role");
    }

    TMChargeableServiceDescriptor descriptor = getChargeableServiceDescriptor(serviceName, roleName);
    return descriptor.newInstance((TMSubscriber)subscriber);
  }


  public synchronized TMChargeableServiceDescriptor getChargeableServiceDescriptor(String serviceName,String roleName) {

    String key = getKey(serviceName, roleName);

    TMChargeableServiceDescriptor descriptor = (TMChargeableServiceDescriptor)chargeableServices.get(key);

    if(descriptor == null) {
      descriptor = newChargeableServiceDescriptor(serviceName, roleName, 0.0);
    }

    return descriptor;
  }

  public synchronized TMChargeableServiceDescriptor newChargeableServiceDescriptor(String serviceName, String roleName, double charge, String description, String descriptionFrench) {

    String key = getKey(serviceName, roleName);

    ChargeableServiceInfo info = new ChargeableServiceInfo();
    info.setCode(serviceName);
    info.setDescription(description);
    info.setDescriptionFrench(descriptionFrench);
    info.setRoleName(roleName);
    info.setCharge(charge);

    TMChargeableServiceDescriptor descriptor = new TMChargeableServiceDescriptor(provider, info);

    chargeableServices.put(key, descriptor);

    return descriptor;
  }

  public synchronized TMChargeableServiceDescriptor newChargeableServiceDescriptor(String serviceName, String roleName, double charge) {
    return newChargeableServiceDescriptor(serviceName, roleName, charge, serviceName, serviceName);
  }


}



