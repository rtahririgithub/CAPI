package com.telus.eas.utility.info;

import com.telus.api.reference.ApplicationSummary;
import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 23-Jan-2006
 */

public final class ApplicationSummaryInfo extends Info implements ApplicationSummary {

  static final long serialVersionUID = 1L;

  private final int id;
  private final String code;
  private final String name;

  public ApplicationSummaryInfo(int id, String code, String name) {
    super();
    this.id = id;
    this.code = code;
    this.name = name;
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
    return code.equals(ApplicationSummary.APP_TOWIN_BATCH);
  } 
  
  public boolean isNewAPIallowedToUse() {
    //return code.equals(ApplicationSummary.APP_SD);
	  return true;
  }
  
  
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("ApplicationSummaryInfo:{\n");
    s.append("    id=[").append(id).append("]\n");    
    s.append("    code=[").append(code).append("]\n");
    s.append("    name=[").append(name).append("]\n");
    s.append("}");

    return s.toString();
  }
}
