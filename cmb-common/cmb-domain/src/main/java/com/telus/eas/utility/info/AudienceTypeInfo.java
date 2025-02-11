package com.telus.eas.utility.info;

import com.telus.api.reference.AudienceType;
import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 23-Jan-2006
 */

public final class AudienceTypeInfo extends Info implements AudienceType {
  static final long serialVersionUID = 1L;

  private final int id;
  private final String code;
  private final String description;

  public AudienceTypeInfo(int id, String code, String description) {
    super();
    this.id = id;
    this.code = code;
    this.description = description;
  }

  public int getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getDescriptionFrench() {
    return description;
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("AudienceTypeInfo:{\n");
    s.append("    id=[").append(id).append("]\n");    
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("}");

    return s.toString();
  }
}
