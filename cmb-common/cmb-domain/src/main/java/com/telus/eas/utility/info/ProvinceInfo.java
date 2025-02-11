
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) <p>
 * Company:      <p>
 * @author
 * @version 1.0
 */
package com.telus.eas.utility.info;

import com.telus.api.reference.*;
import com.telus.eas.framework.info.*;

public class ProvinceInfo extends Info implements  Province{

  static final long serialVersionUID = 1L;

  protected String code="";
  protected String description="" ;
  protected String descriptionFrench = "" ;
  protected String countryCode = "CAN" ;
  protected String canadaPostCode="";


  public void setCode(String newCode) {
    code = newCode;
  }

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
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

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getCountryCode() {
    return this.countryCode;
  }

  public String getCanadaPostCode() {
	return canadaPostCode;
  }

  public void setCanadaPostCode(String canadaPostCode) {
	this.canadaPostCode = canadaPostCode;
  }
  
  public String toString()
  {
	StringBuffer s = new StringBuffer(128);
	
	s.append("ProvinceInfo:[\n");
	s.append("    code=[").append(code).append("]\n");
	s.append("    description=[").append(description).append("]\n");
	s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
	s.append("    countryCode=[").append(countryCode).append("]\n");
	s.append("    canadaPostCode=[").append(canadaPostCode).append("]\n");
	s.append("]");
	
	return s.toString();
  }
}
