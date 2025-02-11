package com.telus.provider.util.transition;

/**
 * This class implements a transition matrix for equipment swaps. All forbidden transitions are
 * recorded in the method <tt>initialize()</tt>.
 * <p/>
 * This class implements a Singleton pattern.<p>
 *
 * @author Vladimir Tsitrin
 * @version 2.0, 10/03/05
 * @see TransitionMatrix
 * @see EquipmentState
 * @see Condition
 * @since 2.786.0.0
 */

final class EquipmentTransitionMatrix extends TransitionMatrix {
  // the only class instance - Singleton pattern [GoF]
  private static final EquipmentTransitionMatrix INSTANCE = new EquipmentTransitionMatrix();

  /**
   * Constructs the only instance of <tt>EquipmentTransitionMatrix</tt>.
   */
  private EquipmentTransitionMatrix() {
    super();
  }

  /**
   * Returns the <code>EquipmentTransitionMatrix</code> instance.
   *
   * @return <code>EquipmentTransitionMatrix</code> instance.
   */
  public static EquipmentTransitionMatrix getInstance() {
    return INSTANCE;
  }

  /**
   * Initializes the transition matrix. All forbidden transitions (swaps) should be added here.
   */
  void initialize() throws Throwable {
  }

  /*
  void initialize() throws Throwable {
    EquipmentState equipmentState;

    // Analog -> 1xRTTCard
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("Analog, PCS to 1XRTT Modem swap is invalid.", "Analog, PCS to 1XRTT Modem swap is invalid.")));
    addInitState(equipmentState);

    // 1xRTTCard -> Analog
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new ValidationResult("1xRTT Modem to Analog, PCS swap is invalid.", "1xRTT Modem to Analog, PCS swap is invalid.")));
    addInitState(equipmentState);

    // PCS -> 1xRTTCard
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("Analog, PCS to 1XRTT Modem swap is invalid.", "Analog, PCS to 1XRTT Modem swap is invalid.")));
    addInitState(equipmentState);

    // 1xRTTCard -> PCS
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("1xRTT Modem to Analog, PCS swap is invalid.", "1xRTT Modem to Analog, PCS swap is invalid.")));
    addInitState(equipmentState);

    // Analog -> RIM
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("Analog to 1xRTT (Modem, CellularRIM) is invalid, Analog to 1xRTT (Handset, PDA) swap is valid for certain applications.", "Analog to 1xRTT (Modem, CellularRIM) is invalid, Analog to 1xRTT (Handset, PDA) swap is valid for certain applications.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.")));
    addInitState(equipmentState);

    // RIM -> Analog
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new ValidationResult("1xRTT (Modem, CellularRIM) to Analog is invalid, 1xRTT (Handset, PDA) to Analog swap is valid for certain applications.", "1xRTT (Modem, CellularRIM) to Analog is invalid, 1xRTT (Handset, PDA) to Analog swap is valid for certain applications.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new ValidationResult("No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // PCS -> RIM
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.", "New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.")));
    addInitState(equipmentState);

    // RIM -> PCS
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("Old Equipment is Cellular RIM, but New Equipment is Not Cellular RIM.", "Old Equipment is Cellular RIM, but New Equipment is Not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // Analog -> EVDO
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("Analog to 1xRTT (Modem, CellularRIM) is invalid, Analog to 1xRTT (Handset, PDA) swap is valid for certain applications.", "Analog to 1xRTT (Modem, CellularRIM) is invalid, Analog to 1xRTT (Handset, PDA) swap is valid for certain applications.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.")));
    addInitState(equipmentState);

    // EVDO -> Analog
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new ValidationResult("1xRTT (Modem, CellularRIM) to Analog is invalid, 1xRTT (Handset, PDA) to Analog swap is valid for certain applications.", "1xRTT (Modem, CellularRIM) to Analog is invalid, 1xRTT (Handset, PDA) to Analog swap is valid for certain applications.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_ANALOG, new ValidationResult("No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // PCS -> EVDO
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("New Equipment is EVDO, but Old Equipment is not EVDO.", "New Equipment is EVDO, but Old Equipment is not EVDO.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.")));
    addInitState(equipmentState);

    // EVDO -> PCS
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("Old Equipment is EVDO, but New Equipment is Not Cellular RIM.", "Old Equipment is EVDO, but New Equipment is Not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // RIM -> EVDO
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("New Equipment is EVDO, but Old Equipment is not Cellular RIM.", "New Equipment is EVDO, but Old Equipment is not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("No repair or loaner repair for Cellular RIM to EVDO swaps.", "No repair or loaner repair for Cellular RIM to EVDO swaps.")));
    addInitState(equipmentState);

    // EVDO -> RIM
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.", "New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset/EVDO to Cellular RIM swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset/EVDO to Cellular RIM swaps.")));
    addInitState(equipmentState);

    // 1xRTT Handset -> EVDO (for now, same as PCS -> EVDO)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("New Equipment is EVDO, but Old Equipment is not EVDO.", "New Equipment is EVDO, but Old Equipment is not EVDO.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to EVDO swaps.")));
    addInitState(equipmentState);

    // EVDO -> 1xRTT Handset (for now, same as EVDO -> PCS)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("Old Equipment is EVDO, but New Equipment is Not EVDO.", "Old Equipment is EVDO, but New Equipment is Not EVDO.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for EVDO to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // 1xRTTCard -> EVDO
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new ValidationResult("", "")));
    addInitState(equipmentState);

    // EVDO -> 1xRTTCard
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DATACARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("", "")));
    addInitState(equipmentState);

    // 1xRTT Handset -> RIM (for now, same as PCS -> RIM)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.", "New Equipment is Cellular RIM, but Old Equipment is not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.", "No repair or loaner repair for PCS/Analog/1xRTT Handset to Cellular RIM swaps.")));
    addInitState(equipmentState);

    // RIM -> 1xRTT Handset (for now, same as RIM -> PCS)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("Old Equipment is Cellular RIM, but New Equipment is Not Cellular RIM.", "Old Equipment is Cellular RIM, but New Equipment is Not Cellular RIM.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.", "No repair or loaner repair for Cellular RIM to PCS/Analog/1xRTT Handset swaps.")));
    addInitState(equipmentState);

    // 1xRTTCard -> 1xRTT Handset (for now, same as 1xRTTCard -> PCS)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new ValidationResult("1xRTT Modem to 1XRTT Handset swap is invalid.", "1xRTT Modem to 1XRTT Handset swap is invalid.")));
    addInitState(equipmentState);

    // 1xRTT Handset -> 1xRTTCard (for now, same as PCS -> 1xRTTCard)
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("1xRTT Handset to 1XRTT Modem swap is invalid.", "1xRTT Handset to 1XRTT Modem swap is invalid.")));
    addInitState(equipmentState);

    // RIM -> 1xRTTCard
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("", "")));
    addInitState(equipmentState);

    // 1xRTTCard -> RIM
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_RIM, new ValidationResult("", "")));
    addInitState(equipmentState);

    // PDA -> 1xRTTCard
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, EquipmentInfo.PRODUCT_CLASS_CODE_PDA, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("PDA to 1XRTT Modem swap is invalid.", "PDA to 1XRTT Modem swap is invalid.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, EquipmentInfo.PRODUCT_CLASS_CODE_PDA, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new ValidationResult("PDA to 1XRTT Modem swap is invalid.", "PDA to 1XRTT Modem swap is invalid.")));
    addInitState(equipmentState);

    // 1xRTTCard -> PDA
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Equipment.SWAP_TYPE_REPLACEMENT}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, EquipmentInfo.PRODUCT_CLASS_CODE_PDA, new ValidationResult("1xRTT Modem to PDA swap is invalid.", "1xRTT Modem to PDA swap is invalid.")));
    addInitState(equipmentState);

    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_1xRTT_CARD, new String[]{Equipment.SWAP_TYPE_LOANER, Equipment.SWAP_TYPE_REPAIR}, new String[]{Condition.ALL});
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, EquipmentInfo.PRODUCT_CLASS_CODE_PDA, new ValidationResult("1xRTT Modem to PDA swap is invalid.", "1xRTT Modem to PDA swap is invalid.")));
    addInitState(equipmentState);

    //  PCS -> WP
    equipmentState = new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, new String[]{Condition.ALL}, new String[]{ApplicationSummary.APP_OOM, ApplicationSummary.APP_CSOM, ApplicationSummary.APP_SD, ApplicationSummary.APP_SWPTRK, ApplicationSummary.APP_SSERVE, ApplicationSummary.APP_CSSSRV}, false);
    equipmentState.addInvalidFinalState(new EquipmentState(Equipment.PRODUCT_TYPE_PCS, Equipment.EQUIPMENT_TYPE_DIGITAL, EquipmentInfo.PRODUCT_CLASS_CODE_WORLDPHONEHANDSET, new ValidationResult("PCS Handset to WorldPhone Handset swap is invalid for some applications.", "PCS Handset to WorldPhone Handset swap is invalid for some applications.")));
    addInitState(equipmentState);
  }
  */
}
