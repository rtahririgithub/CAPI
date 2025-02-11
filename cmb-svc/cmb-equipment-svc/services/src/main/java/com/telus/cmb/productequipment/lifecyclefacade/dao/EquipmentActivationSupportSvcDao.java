package com.telus.cmb.productequipment.lifecyclefacade.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.srv.rmo.resourcemgmt.equipmentactivationsupportservicerequestresponsetypes_v1.ReserveSimProfileResponse;

public interface EquipmentActivationSupportSvcDao {
	public TestPointResultInfo test();
	
	public ReserveSimProfileResponse reserveSimProfile(String imei, String simProfileCd, String embeddedIccId) throws ApplicationException;
}
