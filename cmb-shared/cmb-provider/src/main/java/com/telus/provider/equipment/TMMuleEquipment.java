/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.equipment;

import com.telus.api.TelusAPIException;
import com.telus.api.account.IDENSubscriber;
import com.telus.api.account.InvalidWarrantyTransferException;
import com.telus.api.equipment.MuleEquipment;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.TMProvider;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.servicerequest.TMServiceRequestManager;
import com.telus.provider.util.AppConfiguration;

@Deprecated
public class TMMuleEquipment extends TMIDENEquipment implements MuleEquipment {

  /**
   * @link aggregation
   */

  private final EquipmentInfo delegate;

  public TMMuleEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }

  public void testTransferWarranty(IDENSubscriber subscriber,
                               MuleEquipment destinationMuleEquipment,
                               String dealerCode,
                               String salesRepCode,
                               String requestorId,
                               String repairId,
                               boolean repair)  throws TelusAPIException, InvalidWarrantyTransferException {

    if (destinationMuleEquipment != null && destinationMuleEquipment.equals(this))
      throw new InvalidWarrantyTransferException("New and old equipment cannot be the same for Mule warranty transfer", null,
        InvalidWarrantyTransferException.OLD_NEW_EQUIPMENT_CANNOT_BE_SAME);

    try {
      ((TMSubscriber)subscriber).testChangeEquipment0(destinationMuleEquipment, dealerCode, salesRepCode, requestorId, repairId, repair ? "REPAIR" : "LOANER", this);

    } catch (TelusAPIException e) {
      throw e;
    } catch (Throwable t) {
      throw new TelusAPIException(t.getMessage(), t);
    }
  }

  public void testChange(IDENSubscriber subscriber,
                               MuleEquipment destinationMuleEquipment,
                               String dealerCode,
                               String salesRepCode,
                               String requestorId,
                               String repairId,
                               String swapType)  throws TelusAPIException, InvalidWarrantyTransferException {

    if (destinationMuleEquipment != null && destinationMuleEquipment.equals(this))
      throw new InvalidWarrantyTransferException("New and old equipment cannot be the same for Mule Equipment Change", null,
        InvalidWarrantyTransferException.OLD_NEW_EQUIPMENT_CANNOT_BE_SAME);

    try {
      ((TMSubscriber)subscriber).testChangeEquipment0(destinationMuleEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, this);

    } catch (TelusAPIException e) {
      throw e;
    } catch (Throwable t) {
      throw new TelusAPIException(t.getMessage(), t);
    }
  }


  public void transferWarranty(IDENSubscriber subscriber,
                               MuleEquipment destinationMuleEquipment,
                               String dealerCode,
                               String salesRepCode,
                               String requestorId,
                               String repairId,
                               boolean repair)  throws TelusAPIException, InvalidWarrantyTransferException {

    try {
      ((TMSubscriber)subscriber).changeEquipment0(destinationMuleEquipment, dealerCode, salesRepCode, requestorId, repairId, repair ? "REPAIR" : "LOANER", this, false);

    } catch (TelusAPIException e) {
      throw e;
    } catch (Throwable t) {
      throw new TelusAPIException(t);
    }
  }

  public void change(IDENSubscriber subscriber,
                               MuleEquipment destinationMuleEquipment,
                               String dealerCode,
                               String salesRepCode,
                               String requestorId,
                               String repairId,
                               String swapType)  throws TelusAPIException, InvalidWarrantyTransferException {

    try {
      ((TMSubscriber)subscriber).changeEquipment0(destinationMuleEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, this, false);

    } catch (TelusAPIException e) {
      throw e;
    } catch (Throwable t) {
      throw new TelusAPIException(t);
    }
  }

  public void change(IDENSubscriber subscriber,
          MuleEquipment destinationMuleEquipment,
          String dealerCode,
          String salesRepCode,
          String requestorId,
          String repairId,
          String swapType,
          ServiceRequestHeader header)  throws TelusAPIException, InvalidWarrantyTransferException {

	try {
		((TMSubscriber)subscriber).changeEquipment0(destinationMuleEquipment, dealerCode, salesRepCode, requestorId, repairId, swapType, this, false);
		
		if ( (header!=null && AppConfiguration.isSRPDSEnabled()==true) ) {
			((TMServiceRequestManager)provider.getServiceRequestManager())
				.reportChangeEquipment(subscriber.getBanId(), subscriber.getSubscriberId(),
						dealerCode, salesRepCode, requestorId, 
					null, null, repairId, swapType, this, destinationMuleEquipment, header);
			
		}

	
	} catch (TelusAPIException e) {
		throw e;
	} catch (Throwable t) {
		throw new TelusAPIException(t);
	}
}



  public String getSIMCardNumber() throws TelusAPIException {

	  try {
		  return provider.getProductEquipmentHelper().getSIMByIMEI(delegate.getSerialNumber());
	  }catch (Throwable t) {
		  provider.getExceptionHandler().handleException(t);
	  }	
	  return null;
  }


  public MuleEquipment getDelegate0(){
    return delegate;
  }

  public String getRIMPin() {
    return delegate.getRIMPin();
  }

}







