package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.reference.Generation;

public final class GenerationInfo extends Info implements Generation {
  static final long serialVersionUID = 1L;

  private final String code;
  private final String description;
  private final String descriptionFrench;

  public GenerationInfo(String code, String description, String descriptionFrench) {
    this.code = code;
    this.description = description;
    this.descriptionFrench = descriptionFrench;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("GenerationInfo:{\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
    s.append("}");

    return s.toString();
  }
}
