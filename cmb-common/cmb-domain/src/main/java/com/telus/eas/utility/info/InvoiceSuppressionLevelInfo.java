

/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */

package com.telus.eas.utility.info;

import com.telus.api.reference.InvoiceSuppressionLevel;

public class InvoiceSuppressionLevelInfo extends ReferenceInfo implements  InvoiceSuppressionLevel {
	
  static final long serialVersionUID = 1L;

  private String[] codesAvailableForUpdate;

  public void setCodesAvailableForUpdate(String[] codesAvailableForUpdate) {
    this.codesAvailableForUpdate = codesAvailableForUpdate;
  }

  public boolean isAvailableForUpdate() {
    if (codesAvailableForUpdate != null) {
      for (int i = 0; i < codesAvailableForUpdate.length; i++) {
        if (codesAvailableForUpdate[i].equals(getCode())) {
          return true;
        }
      }
    }
    return false;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("InvoiceSuppressionLevelInfo:{\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
    s.append("    isAvailableForUpdate()=[").append(isAvailableForUpdate()).append("]\n");
    s.append("}");

    return s.toString();
  }
}

