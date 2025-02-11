package com.telus.cmb.productequipment.manager.svc;

import com.telus.api.ApplicationException;

public interface ProductEquipmentManager {

	
	void insertAnalogEquipment (String pSerialNo, String pUserID)throws   ApplicationException;
	 
	void insertPagerEquipment (String pSerialNo, String pCapCode, String pEncoderFormat, 
			 String pFrequencyCode, String pModelType, String pUserID) throws   ApplicationException;

	void setCardStatus(String serialNo, int statusId, String user) throws ApplicationException;
	 
	void activateCard(String serialNo, int ban, String phoneNumber, 
			 String equipmentSerialNo, boolean autoRenewInd, String user) throws ApplicationException;
	 
	void creditCard(String serialNo, int ban, String phoneNumber, 
			 String equipmentSerialNo, boolean autoRenewInd, String user) throws ApplicationException;

	void updateStatus(String pSerialNo, String pUserID, long pEquipmentStatusTypeID,
		        long pEquipmentStatusID,String pTechType,long pProductClassID) throws ApplicationException;

	void startSIMMuleRelation(String sim, java.util.Date activationDate) throws ApplicationException;
	 
	void activateSIMMule(String sim, String mule, 
			 java.util.Date activationDate) throws ApplicationException;
	 
	void setSIMMule(String sim, String mule, java.util.Date activationDate,
			 String eventType) throws ApplicationException;
	 
	String getMasterLockbySerialNo (String pSerialNo, String pUserID,
			 long pLockReasonID) throws ApplicationException;
	 
	String getMasterLockbySerialNo (String pSerialNo, String pUserID, 
			 long pLockReasonID, long pOutletID, long pChnlOrgID) throws ApplicationException;
	

}
