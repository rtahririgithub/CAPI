package com.telus.provider.reference;

/**
 * Title:        Telus Domain Project
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.reference.*;
import com.telus.eas.utility.info.*;
import java.util.*;
import com.telus.api.*;
import com.telus.provider.*;


public class TMCoverageRegion implements CoverageRegion{

  /**
   *@link aggregation
   */
  protected final TMReferenceDataManager referenceDataManager;

  /**
   * @link aggregation
   */
  private final CoverageRegionInfo delegate;

  public TMCoverageRegion(TMReferenceDataManager referenceDataManager, CoverageRegionInfo delegate) {
    this.referenceDataManager = referenceDataManager;
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------

  public String getDescription() {
    return delegate.getDescription();
  }

  public String getDescriptionFrench() {
    return delegate.getDescriptionFrench();
  }

  public String getCode() {
    return delegate.getCode();
  }

  public String getType() {
    return delegate.getType();
  }

  public String getFrequencyCode() {
    return delegate.getFrequencyCode();
  }

  public String getProvinceCode() {
    return delegate.getProvinceCode();
  }

  public String[] getAssociatedServiceCodes() {
    return delegate.getAssociatedServiceCodes();
  }

   //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------
  public CoverageRegionInfo getDelegate() {
    return delegate;
  }

 public  Service  getAssociatedService(PricePlan pricePlan) throws TelusAPIException {
 Service[] serviceList = pricePlan.getOptionalServices();
 Service   service=null;
 int i;
 boolean    matchFound=false;
 for (i=0; (i<serviceList.length)&&(!matchFound); i++)
         {  for (int j=0 ; (j < getAssociatedServiceCodes().length)&&(!matchFound); j++)
              { if ((serviceList[i].getCode().equals(getAssociatedServiceCodes()[j])))
                 {matchFound=true;
                 service = serviceList[i];
                 }
              }
         }
if ( matchFound)
return service ;
else
throw new TelusAPIException("Associated Coverage Service not Found for Price Plan: " + pricePlan.getCode());

//return service ;
}
}