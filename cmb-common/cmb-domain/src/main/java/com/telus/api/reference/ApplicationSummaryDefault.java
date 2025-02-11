package com.telus.api.reference;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 19-May-2006
 */

final class ApplicationSummaryDefault implements ApplicationSummary {
  private final static ApplicationSummary INSTANCE = new ApplicationSummaryDefault(0, "DEFAULT", "Default application.");

  private final int id;
  private final String code;
  private final String name;

  private ApplicationSummaryDefault(int id, String code, String name) {
    this.id = id;
    this.code = code;
    this.name = name;
  }

  static ApplicationSummary getInstance() {
    return INSTANCE;
  }

  public int getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return name;
  }

  public String getDescriptionFrench() {
    return name;
  }  
  
  public boolean isBatch() {
	  return false;  
  }
  
  public boolean isNewAPIallowedToUse() {
	  return false; 
  }
  
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("ApplicationSummaryDefault:{\n");
    s.append("    id=[").append(id).append("]\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    name=[").append(name).append("]\n");
    s.append("}");

    return s.toString();
  }
}
