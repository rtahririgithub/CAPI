
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

public interface KnowbilityOperator extends Reference {

  String getName();
  String getID();
  String getWorkPositionId();
  String getSupervisorId();
  String getDepartmentCode();
  double getChargeThresholdAmount();
  double getCreditThresholdAmount();
}
