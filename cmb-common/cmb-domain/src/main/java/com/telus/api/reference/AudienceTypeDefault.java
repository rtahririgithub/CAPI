package com.telus.api.reference;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 19-May-2006
 */

final class AudienceTypeDefault implements AudienceType {
  private final static AudienceType INSTANCE = new AudienceTypeDefault(0, "DEFAULT", "Default audience type.");

  private final int id;
  private final String code;
  private final String description;

  private AudienceTypeDefault(int id, String code, String description) {
    this.id = id;
    this.code = code;
    this.description = description;
  }

  static AudienceType getInstance() {
    return INSTANCE;
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

    s.append("AudienceTypeDefault:{\n");
    s.append("    id=[").append(id).append("]\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("}");

    return s.toString();
  }
}
