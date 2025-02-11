package com.telus.eas.portability.info;

import com.telus.api.portability.PRMReferenceData;
import com.telus.eas.utility.info.ReferenceInfo;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 22-Oct-2006
 */

public class PRMReferenceDataInfo extends ReferenceInfo implements PRMReferenceData {
  private String category;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append(getClass().getName()).append(":[\n");
    sb.append("    code=[").append(getCode()).append("]\n");
    sb.append("    category=[").append(getCategory()).append("]\n");
    sb.append("    description=[").append(getDescription()).append("]\n");
    sb.append("    descriptionFrench=[").append(getDescriptionFrench()).append("]\n");
    sb.append("]");

    return sb.toString();
  }
}
