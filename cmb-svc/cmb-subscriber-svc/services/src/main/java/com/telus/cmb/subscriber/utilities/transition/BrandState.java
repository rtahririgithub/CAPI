package com.telus.cmb.subscriber.utilities.transition;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 2-Nov-2006
 */

final class BrandState extends State {
  /**
   *
   */
  private final String brandId;

  /**
   * Swap types - contains a condition imposed on allowed or forbidden swap types.
   *
   * @see Condition
   * @see com.telus.api.equipment.Equipment
   */
  private final Condition swapTypes;

  /**
   * Application IDs - contains a condition imposed on allowed or forbidden applications.
   *
   * @see Condition
   * @see com.telus.api.reference.ApplicationSummary
   */
  private final Condition appIds;

  /**
   * Constructs an equipment swap state corresponding to the given piece of equipment, swap type and
   * application.
   *
   * @param brandId   brand ID associated with the equipment.
   * @param swapType  supposed swap type.
   * @param appId     application using this method.
   */
  BrandState(int brandId, String swapType, String appId) {
    if (swapType == null || appId == null)
      throw new NullPointerException("BrandState constructor: Input parameters cannot be null.");

    this.brandId = Integer.toString(brandId);

    this.swapTypes = new Condition(new String[]{swapType.trim().toUpperCase()});
    this.appIds = new Condition(new String[]{appId.trim().toUpperCase()});
  }

  /**
   * Constructs an equipment swap state corresponding to the given product and equipment types with
   * explicitly allowed swap types and applications (inclusive conditions).
   *
   * @param brandId       brand ID associated with the equipment.
   * @param swapTypes     array of allowed swap types.
   * @param appIds        array of allowed applications.
   * @param appIdsInc     specifies if the application ID condition is inclusive or exclusive.
   */
  BrandState(String brandId, String[] swapTypes, String[] appIds, boolean appIdsInc) {
    if (brandId == null)
      throw new NullPointerException("BrandState constructor: Input parameters cannot be null.");

    this.brandId = brandId;

    this.swapTypes = new Condition(swapTypes);
    this.appIds = new Condition(appIds, appIdsInc);
  }

  /**
   * Constructs an equipment swap state corresponding to the given product and equipment types and all
   * possible swap types and applications and specified validation error.
   *
   * @param brandId           brand ID associated with the equipment.
   * @param validationResult  validation result.
   */
  BrandState(String brandId, ValidationResult validationResult) {
    this.brandId = brandId;

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
    return new String[] {brandId};
  }

  /**
   * Returns an array of non-transitive fields. For this class this array consists of only one
   * element - productType.
   *
   * @return non-transitive fields.
   */
  String[] getNonTransitiveFields() {
    return new String[0];
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

    s.append("utility.BrandState:{\n");
    s.append("    brandId=[").append(brandId).append("]\n");
    s.append("    swapTypes=[").append(swapTypes.toString()).append("]\n");
    s.append("    appIds=[").append(appIds.toString()).append("]\n");

    State[] invalidFinalStates = getInvalidFinalStates();
    int invalidFinalStatesSz = invalidFinalStates != null ? invalidFinalStates.length : 0;
    for (int i = 0; i < invalidFinalStatesSz; i++) {
      s.append("    invalidFinalStates[=[").append(i).append("]: [").append(invalidFinalStates[i].toString()).append("]\n");
    }

    s.append("}");

    return s.toString();
  }
}
