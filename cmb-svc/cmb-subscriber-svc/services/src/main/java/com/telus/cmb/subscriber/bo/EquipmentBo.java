package com.telus.cmb.subscriber.bo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.equipment.MuleEquipment;
import com.telus.cmb.subscriber.decorators.EquipmentDecorator;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class EquipmentBo extends EquipmentDecorator {
	
	private static final Log logger = LogFactory.getLog(EquipmentBo.class);
	
	protected BaseChangeContext<? extends BaseChangeInfo> changeContext;

	public EquipmentBo(EquipmentInfo equipment, BaseChangeContext<? extends BaseChangeInfo> changeContext) {
		super(equipment);
		this.changeContext = changeContext;
	}

	public EquipmentInfo getLastMule() throws ApplicationException {
		
		EquipmentInfo lastMule = delegate.getLastMule0();
		if (lastMule == null && isSIMCard()) {
			lastMule = changeContext.getProductEquipmentHelper().getMuleBySIM(getSerialNumber());
			delegate.setLastMule0(lastMule);
		}

		return lastMule;
	}

	public void setLastMuleIMEI(String muleNumber) {
		delegate.setLastMuleIMEI(muleNumber);
	}

	public void setLastMule(MuleEquipment lastMule) {
		delegate.setLastMule(lastMule);
		setLastMuleIMEI(lastMule.getSerialNumber());
	}

	/**
	 * Return true, if one of the product features is ?GPS?
	 * 
	 * @return boolean
	 * @throws ApplicationException
	 */
	@Override
	public boolean isGPS() throws ApplicationException {
		
		String[] productFeatures = getProductFeatures();
		if (productFeatures == null) {
			return false;
		}

		for (int i = 0; i < productFeatures.length; i++) {
			if (EquipmentInfo.PRODUCT_FEATURE_GPS.equalsIgnoreCase(productFeatures[i].trim())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Return true, if one of the product features is ?VoLTE?
	 * 
	 * @return boolean
	 * @throws ApplicationException
	 */
	@Override
	public boolean isVoLTE() throws ApplicationException {
		
		String[] productFeatures = getProductFeatures();
		if (productFeatures == null) {
			return false;
		}

		for (int i = 0; i < productFeatures.length; i++) {
			if (EquipmentInfo.PRODUCT_FEATURE_VOLTE.equalsIgnoreCase(productFeatures[i].trim())) {
				return true;
			}
		}

		return false;
	}

	public String[] getProductFeatures() throws ApplicationException {

		String[] productFeatures = delegate.getProductFeatures();
		if (productFeatures != null) {
			return productFeatures;
		}

		productFeatures = changeContext.getProductEquipmentHelper().getProductFeatures(delegate.getProductCode());
		delegate.setProductFeatures(productFeatures);

		return productFeatures;
	}

	public boolean isPTTEnabled() {
		return delegate.isPTTEnabled();
	}

}