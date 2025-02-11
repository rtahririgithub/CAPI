package com.telus.cmb.productequipment.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface EquipmentInfoSvcDao {

	public TestPointResultInfo test() ;
	
	public WarrantyInfo getWarrantySummary(String serialNumber, String equipmentGroup) throws ApplicationException;
}
