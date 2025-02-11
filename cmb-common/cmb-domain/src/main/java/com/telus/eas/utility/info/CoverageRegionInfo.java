package com.telus.eas.utility.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.TelusAPIException;
import com.telus.api.reference.CoverageRegion;
import com.telus.api.reference.PricePlan;
import com.telus.api.reference.Service;

public class CoverageRegionInfo implements CoverageRegion {

	static final long serialVersionUID = 1L;

  private String code;
  private String description;
  private String descriptionFrench;
  private String provinceCode;
  private String type;
  private String frequencyCode;
  private String[] associatedServiceCodes;

  public CoverageRegionInfo() {
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

  public void setCode(String code) {
    this.code = code;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setDescriptionFrench(String descriptionFrench) {
    this.descriptionFrench = descriptionFrench;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public String getFrequencyCode() {
    return frequencyCode;
  }

  public void setFrequencyCode(String frequencyCode) {
    this.frequencyCode = frequencyCode;
  }

  public void setAssociatedServiceCodes(String[] associatedServiceCodes) {
    this.associatedServiceCodes = associatedServiceCodes;
  }

  public String[] getAssociatedServiceCodes() {
    return associatedServiceCodes;
  }

  public Service getAssociatedService(PricePlan pricePlan) throws TelusAPIException {
    throw new UnsupportedOperationException("method not implemented here");

  }

  public String toString() {
    StringBuffer s = new StringBuffer(1024);

    s.append("CoverageRegionInfo:[\n");
    s.append("    code=[").append(code).append("]\n");
    s.append("    description=[").append(description).append("]\n");
    s.append("    descriptionFrench=[").append(descriptionFrench).append("]\n");
    s.append("    provinceCode=[").append(provinceCode).append("]\n");
    s.append("    type=[").append(type).append("]\n");
    if (associatedServiceCodes == null) {
      s.append("    associatedServiceCodes=[null]\n");
    }
    else if (associatedServiceCodes.length == 0) {
      s.append("    associatedServiceCodes={}\n");
    }
    else {

      for (int i = 0; i < associatedServiceCodes.length; i++) {
        s.append("   associatedServiceCodes[").append(i).append("]=[").append(associatedServiceCodes[i]).append("]\n");
      }
      s.append("]");
    }
    return s.toString();
  }
}