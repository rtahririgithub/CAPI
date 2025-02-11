package com.telus.eas.utility.info;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

import com.telus.eas.framework.info.*;
import com.telus.api.reference.*;
import java.util.*;

public class ServiceSetInfo extends Info implements ServiceSet {

  static final long serialVersionUID = 1L;

  private String code = "";
  private String description = "";
  private String descriptionFrench="";
  private Service[] service;

  public void setDescription(String newDescription) {
    description = newDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setCode(String newCode) {
    code = newCode;
  }

  public String getCode() {
    return code;
  }

  public void setDescriptionFrench(String newDescriptionFrench) {
    descriptionFrench = newDescriptionFrench;
  }

  public String getDescriptionFrench() {
    return descriptionFrench;
  }

  /**
   * Methods to be implemented.
   */

  public Service[] getServices() {
    return service;
  }

  public void setServices(Service[] service) {
    this.service = service;
  }

  public boolean contains(String serviceCode) {

    if (service == null || service.length == 0)
      return false;

    List serviceCodes = new ArrayList();
    for (int i=0; i<service.length; i++) {
      serviceCodes.add(service[i].getCode());

      if (serviceCodes.contains(serviceCode))
        return true;
    }

    return false;
  }

}