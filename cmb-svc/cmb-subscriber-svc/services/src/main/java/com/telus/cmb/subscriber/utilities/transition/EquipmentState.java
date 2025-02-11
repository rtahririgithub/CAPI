package com.telus.cmb.subscriber.utilities.transition;

import com.telus.api.equipment.Equipment;
import com.telus.api.reference.ApplicationSummary;

/**
 * This class represents a state for equipment swaps. The state is characterized by two state
 * fields: <tt>productType</tt> and <tt>equipmentType</tt>. Initial state is also characterized by two
 * transition (swap) conditions: <tt>swapType</tt> for swap types and <tt>appIds</tt> for application IDs.
 * Field <tt>productType</tt> is non-transitive meaning that no transitions (swaps) are allowed between
 * the pieces of equipment corresponding to the different products.
 *
 * @author Vladimir Tsitrin
 * @version 2.0, 10/03/05
 * @see State
 * @see TransitionMatrix
 * @see EquipmentTransitionMatrix
 * @see Equipment
 * @since 2.786.0.0
 */

final class EquipmentState extends State {
  /**
   * Product type. This field is non-transitive meaning that no transitions (swaps) are allowed between
   * the pieces of equipment corresponding to the different products.
   *
   * @see Equipment
   */
  private final String productType;

  /**
   * Equipment type.
   *
   * @see Equipment
   */
  private final String equipmentType;

  /**
   * Product class.
   *
   * @see Equipment
   */
  private final String productClass;

  /**
   * Swap types - contains a condition imposed on allowed or forbidden swap types.
   *
   * @see Condition
   * @see Equipment
   */
  private final Condition swapTypes;

  /**
   * Application IDs - contains a condition imposed on allowed or forbidden applications.
   *
   * @see Condition
   * @see ApplicationSummary
   */
  private final Condition appIds;

  /**
   * Constructs an equipment swap state corresponding to the given piece of equipment, swap type and
   * application.
   *
   * @param equipment piece of equipment corresponding to the given initial or final state of the swap.
   * @param swapType  supposed swap type.
   * @param appId     application using this method.
   */
  EquipmentState(Equipment equipment, String swapType, String appId) {
    if (equipment == null || swapType == null || appId == null)
      throw new NullPointerException("EquipmentState constructor: Input parameters cannot be null.");

    this.productType = equipment.getProductType();
    this.equipmentType = equipment.getEquipmentType();
    this.productClass = equipment.getProductClassCode();

    this.swapTypes = new Condition(new String[]{swapType.trim().toUpperCase()});
    this.appIds = new Condition(new String[]{appId.trim().toUpperCase()});
  }

  /**
   * Constructs an equipment swap state corresponding to the given product and equipment types with
   * explicitly allowed swap types and applications (inclusive conditions).
   *
   * @param productType   product type of the equipment.
   * @param equipmentType equipment type of the equipment.
   * @param productClass  product type of the equipment.
   * @param swapTypes     array of allowed swap types.
   * @param appIds        array of allowed applications.
   * @param appIdsInc     specifies if the application ID condition is inclusive or exclusive.
   */
  EquipmentState(String productType, String equipmentType, String productClass, String[] swapTypes, String[] appIds, boolean appIdsInc) {
    if (productType == null || equipmentType == null)
      throw new NullPointerException("EquipmentState constructor: Input parameters cannot be null.");

    this.productType = productType;
    this.equipmentType = equipmentType;
    this.productClass = productClass;

    this.swapTypes = new Condition(swapTypes);
    this.appIds = new Condition(appIds, appIdsInc);
  }

  /**
   * Constructs an equipment swap state corresponding to the given product and equipment types and all
   * possible swap types and applications and specified validation error.
   *
   * @param productType      product type of the equipment.
   * @param equipmentType    equipment type of the equipment.
   * @param productClass     product class of the equipment.
   * @param validationResult validation result.
   */
  EquipmentState(String productType, String equipmentType, String productClass, ValidationResult validationResult) {
    this.productType = productType;
    this.equipmentType = equipmentType;
    this.productClass = productClass;

    this.swapTypes = new Condition();
    this.appIds = new Condition();
    this.validationResult = validationResult;
  }

  /**
   * Returns an array of state fields - productType and equipmentType.
   *
   * @return state fields.
   */
  String[] getFields() {
    return new String[]{productType, equipmentType, productClass};
  }

  /**
   * Returns an array of non-transitive fields. For this class this array consists of only one
   * element - productType.
   *
   * @return non-transitive fields.
   */
  String[] getNonTransitiveFields() {
    return new String[]{productType};
  }

  /**
   * Returns an array of transition conditions that consists of two elements: swap type condition
   * and application ID condition.
   *
   * @return conditions.
   */
  Condition[] getConditions() {
    return new Condition[]{swapTypes, appIds};
  }

  /**
   * Return a string representation of this state.
   *
   * @return string representation of this state.
   */
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("utility.EquipmentState:{\n");
    s.append("    productType=[").append(productType).append("]\n");
    s.append("    equipmentType=[").append(equipmentType).append("]\n");
    s.append("    productClass=[").append(productClass).append("]\n");
    s.append("    swapTypes=[").append(swapTypes.toString()).append("]\n");
    s.append("    appIds=[").append(appIds.toString()).append("]\n");

    State[] invalidFinalStates = getInvalidFinalStates();
    int invalidFinalStatesSz = invalidFinalStates != null ? invalidFinalStates.length : 0;
    for (int i = 0; i < invalidFinalStatesSz; i++) {
      s.append("    invalidFinalStates[=[").append(i).append("]: [").append(((EquipmentState) invalidFinalStates[i]).toString()).append("]\n");    
    }

    s.append("}");

    return s.toString();
  }
}
