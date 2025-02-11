package com.telus.provider.equipment;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.telus.api.TelusAPIException;
import com.telus.api.equipment.CellularEquipment;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.provider.TMProvider;

public class TMCellularEquipment extends TMEquipment implements CellularEquipment {

  private EquipmentInfo delegate;

  public TMCellularEquipment(TMProvider provider, EquipmentInfo delegate) {
    super(provider, delegate);
    this.delegate = delegate;
  }

  public boolean isPrimary() {
    return delegate.isPrimary();
  }

  public boolean isValidForPrepaid() {
    return !(delegate.is1xRTTCard()
      || delegate.isWorldPhone()
      || (delegate.isRIM() && delegate.isHSPA()==false )
      || (delegate.isEvDOCapable() && delegate.isDataCard())
      || (delegate.isPDA()&& delegate.isHSPA()==false)
      );
  }

  public boolean isValidForPrepaidActivationWithoutPin() throws TelusAPIException {
    try {
    	boolean isNewPrepaidHandset;
    	isNewPrepaidHandset = provider.getProductEquipmentHelper().isNewPrepaidHandset(delegate.getSerialNumber(), delegate.getProductCode());
    	return isValidForPrepaid() && isNewPrepaidHandset;
    }catch (Throwable t) {
		provider.getExceptionHandler().handleException(t);
	}
	return false;
  }
}
