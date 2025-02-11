package com.telus.cmb.productequipment.manager.dao;

import com.telus.api.ApplicationException;

public interface EquipmentManagerDao {
	
	void insertAnalogEquipment (String pSerialNo, String pUserID )throws   ApplicationException;
	
	void insertPagerEquipment (String pSerialNo, String pCapCode, String pEncoderFormat, 
			String pFrequencyCode, String pModelType,String pUserID) throws   ApplicationException;

	void updateStatus (String pSerialNo, String pUserID, long pEquipmentStatusTypeID,
			long pEquipmentStatusID,String pTechType,long pProductClassID) throws   ApplicationException;
	
	String getMasterLockbySerialNo (String pSerialNo, String pUserID, 
			long pLockReasonID) throws   ApplicationException;
	
	String getMasterLockbySerialNo (String pSerialNo, String pUserID, long pLockReasonID,
    	long pOutletID, long pChnlOrgID)throws   ApplicationException;
	
	void setSIMMule(String sim, String mule, java.util.Date activationDate, 
			String eventType)throws   ApplicationException;
	
}
