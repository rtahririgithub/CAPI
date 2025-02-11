/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */
package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.EquipmentChangeHistory;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.PagerEquipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.subscriber.info.EquipmentChangeHistoryInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMPagerEquipment;
import com.telus.provider.util.AppConfiguration;
import com.telus.provider.util.InfoObjectFactory;

public class TMEquipmentChangeHistory extends BaseProvider implements EquipmentChangeHistory {

  /**
   * @link aggregation
   */
  private final EquipmentChangeHistoryInfo delegate;
  private Equipment equipment;

  public TMEquipmentChangeHistory(TMProvider provider, EquipmentChangeHistoryInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  //--------------------------------------------------------------------
  //  Decorative Methods
  //--------------------------------------------------------------------

  public Date getEffectiveDate() {
    return delegate.getEffectiveDate();
  }

  public Date getExpiryDate() {
    return delegate.getExpiryDate();
  }

  public int getEsnLevel() {
    return delegate.getEsnLevel();
  }

  public String getSerialNumber() {
    return delegate.getSerialNumber();
  }

  public String toString() {
    return delegate.toString();
  }

  //--------------------------------------------------------------------
  //  Service Methods
  //--------------------------------------------------------------------

  public Equipment getEquipment() throws TelusAPIException {
    if (equipment == null) {
      if (delegate.getProductType().equals(Subscriber.PRODUCT_TYPE_TANGO)) {
    	  
    	EquipmentInfo equipmentInfo = new EquipmentInfo();
        equipmentInfo.setSerialNumber(getSerialNumber());
        equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_TANGO);
        equipmentInfo.setEquipmentType(Equipment.EQUIPMENT_TYPE_TANGO);

        equipment = new TMEquipment(provider, equipmentInfo);
      }
      else if (delegate.getProductType().equals(Subscriber.PRODUCT_TYPE_CDPD)) {
        try {
          // Although this is a different product, this method will still populate the required field equipmentType.
          EquipmentInfo equipmentInfo = provider.getProductEquipmentHelper().retrievePagerEquipmentInfo(getSerialNumber());

          equipmentInfo.setSerialNumber(getSerialNumber());
          equipmentInfo.setProductType(Subscriber.PRODUCT_TYPE_CDPD);

          return new TMEquipment(provider, equipmentInfo);
          
        }catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
      }
      else if (delegate.getProductType().equals(Subscriber.PRODUCT_TYPE_PAGER)) {
        try {
          EquipmentInfo equipmentInfo = provider.getProductEquipmentHelper().retrievePagerEquipmentInfo(getSerialNumber());
			
          equipmentInfo.setFormattedCapCode(getSerialNumber());

          TMPagerEquipment pagerEquipment;

          if (equipmentInfo.getEncodingFormat().equals(PagerEquipment.ENCODING_FORMAT_NETWORK)) {
            pagerEquipment = new TMPagerEquipment(provider, equipmentInfo);
          }
          else {
            try {
              String rawCapCode = EquipmentManager.Helper.getUnformattedCapCode(delegate.getSerialNumber(), equipmentInfo.getEncodingFormat());

              pagerEquipment = (TMPagerEquipment) provider.getEquipmentManager().getEquipmentByCapCode(rawCapCode, equipmentInfo.getEncodingFormat());

              pagerEquipment.getDelegate().setCurrentCoverageRegionCode(equipmentInfo.getCurrentCoverageRegionCode());
              pagerEquipment.getDelegate().setFormattedCapCode(equipmentInfo.getFormattedCapCode());
              pagerEquipment.getDelegate().setPossession(equipmentInfo.getPossession());
              pagerEquipment.getDelegate().setEquipmentType(equipmentInfo.getEquipmentType());
            }
            catch(Exception e) {
              pagerEquipment = new TMPagerEquipment(provider, equipmentInfo);
            }
          }

          equipment = pagerEquipment;
        }catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}
      }
      else
        equipment = provider.getEquipmentManager().getEquipment(getSerialNumber());
    }

    return equipment;
  }
}
