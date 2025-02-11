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

public class ServiceUsageInfo extends Info implements Reference  {

  static final long serialVersionUID = 1L;

  private String serviceCode;
  private ServicePeriodInfo[] servicePeriods;

  private String description = "";
  private String descriptionFrench = "" ;

  public String getServiceCode() {
    return serviceCode;
  }
  public void setServiceCode(String serviceCode) {
    this.serviceCode = serviceCode;
  }
  public void setServicePeriods(ServicePeriodInfo[] servicePeriods) {
    this.servicePeriods = servicePeriods;
  }
  public ServicePeriodInfo[] getServicePeriods() {
    return servicePeriods;
  }
  public String getCode() {
    return serviceCode;
  }
  public String getDescription() {
    return description;
  }
  public String getDescriptionFrench() {
    return descriptionFrench;
  }

}