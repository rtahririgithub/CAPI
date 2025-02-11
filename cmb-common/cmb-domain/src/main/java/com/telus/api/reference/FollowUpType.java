/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.api.reference;

public interface FollowUpType extends Reference {

    String getManualOpenInd();

    String getCategory();

    String getSystemText();

    String getSystemTextFrench();

    int getDefaultNoOfDays();

    int getPriority();

    String[] getDefaultDepartmentCodes();

    String[] getDefaultWorkPositionIds();
}
