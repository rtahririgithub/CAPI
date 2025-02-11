package com.telus.provider.reference;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

import com.telus.api.*;
import com.telus.api.reference.*;
import com.telus.provider.*;
import java.util.*;
import com.telus.eas.utility.info.*;


public class TMServicePeriod implements ServicePeriod {

  protected final TMReferenceDataManager referenceDataManager;
  private final ServicePeriodInfo delegate;

  public TMServicePeriod(TMReferenceDataManager referenceDataManager, ServicePeriodInfo delegate) {
    this.referenceDataManager = referenceDataManager;
    this.delegate = delegate;
  }

  public String getCode() {
    return delegate.getCode();
  }

  public String getDescription() {
  try {
    return (referenceDataManager.getServicePeriodType(this.getCode())).getDescription();
    } catch (TelusAPIException tae){
    }
    return "";
  }

  public String getDescriptionFrench() {
 try {
    return (referenceDataManager.getServicePeriodType(this.getCode())).getDescriptionFrench();
 } catch (TelusAPIException tae){
    }
   return "";
  }

  public ServicePeriodHours[] getServicePeriodHours() {
    return delegate.getServicePeriodHours();
  }

  public ServicePeriodIncludedMinutes[] getServicePeriodIncludedMinutes() {
    return delegate.getServicePeriodIncludedMinutes();
  }

 public ServicePeriodUsageRates[] getServicePeriodUsageRates() {
    return delegate.getServicePeriodUsageRates();
  }
}