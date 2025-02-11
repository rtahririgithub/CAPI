package com.telus.provider.equipment;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.util.ArrayList;

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.reference.CoverageRegion;
import com.telus.api.reference.Province;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.TMProvider;

@Deprecated
public class TMPagerEquipment extends TMEquipment implements PagerEquipment {

  private EquipmentInfo delegate;

  public TMPagerEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }

  public String getCapCode() {
    return delegate.getCapCode();
  }

  public String getFormattedCapCode() {
    return delegate.getFormattedCapCode();
  }

  public void setFormattedCapCode(String value) {
    this.delegate.setFormattedCapCode(value);
  }

  public CoverageRegion[] getCoverageRegion() throws TelusAPIException {
    //provider.getReferenceDataManager()
    ArrayList coverageRegion = new ArrayList();
    String[] coverageRegionCodes = delegate.getCoverageRegionCodes();
    for (int i = 0; i < coverageRegionCodes.length; i++) {
      coverageRegion.add(provider.getReferenceDataManager().getCoverageRegion(coverageRegionCodes[i]));
    }
    return (CoverageRegion[]) coverageRegion.toArray(new CoverageRegion[coverageRegion.size()]);
  }

  public EquipmentSubscriber[] getAssociatedSubscribers(boolean active, boolean refresh) throws TelusAPIException {
    try {
      if (associatedSubscribers == null || refresh) {
        String formattedCapCode = delegate.getFormattedCapCode();

        if (formattedCapCode == null || formattedCapCode.equals("")) {
          formattedCapCode = EquipmentManager.Helper.getFormattedCapCode(delegate.getCapCode(), Province.PROVINCE_AB, delegate.getEncodingFormat(), delegate.getEquipmentType());
	  	  associatedSubscribers = provider.getSubscriberLifecycleHelper().retrieveEquipmentSubscribers(formattedCapCode, active);

          if (associatedSubscribers == null || associatedSubscribers.length == 0) {
            formattedCapCode = EquipmentManager.Helper.getFormattedCapCode(delegate.getCapCode(), Province.PROVINCE_BC, delegate.getEncodingFormat(), delegate.getEquipmentType());
    	  	associatedSubscribers = provider.getSubscriberLifecycleHelper().retrieveEquipmentSubscribers(formattedCapCode, active);
          }
        }
        else
    	  	associatedSubscribers = provider.getSubscriberLifecycleHelper().retrieveEquipmentSubscribers(formattedCapCode, active);
      }
    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}

    return associatedSubscribers;
  }

  public String getCurrentCoverageRegionCode() {
    return delegate.getCurrentCoverageRegionCode();
  }

  public void setCurrentCoverageRegionCode(String currentCoverageRegionCode) {
    delegate.setCurrentCoverageRegionCode(currentCoverageRegionCode);
  }

  public String getEncodingFormat() {
    return delegate.getEncodingFormat();
  }

  public String getPossession() {
    return delegate.getPossession();
  }

  public void setPossession(String possession) {
    delegate.setPossession(possession);
  }

  public String getFrequencyCode() {
    return delegate.getFrequencyCode();
  }

  public void setEquipmentType(String newEquipmentType) {
    delegate.setEquipmentType(newEquipmentType);
  }

  public String getModelType() {
    return delegate.getModelType();
  }

  public boolean isBoxed() {
    return delegate.isBoxed();
  }

  public boolean isRental() {
    return delegate.isRental();
  }

}