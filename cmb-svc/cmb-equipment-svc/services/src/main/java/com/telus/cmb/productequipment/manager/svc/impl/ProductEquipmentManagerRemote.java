package com.telus.cmb.productequipment.manager.svc.impl;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductEquipmentManagerRemote extends EJBObject {

	void insertAnalogEquipment (String pSerialNo, String pUserID)throws   ApplicationException, RemoteException;
	 
	void insertPagerEquipment (String pSerialNo, String pCapCode, String pEncoderFormat, 
			 String pFrequencyCode, String pModelType, String pUserID) throws   ApplicationException, RemoteException;

	void setCardStatus(String serialNo, int statusId, String user) throws ApplicationException, RemoteException;
	 
	void activateCard(String serialNo, int ban, String phoneNumber, 
			 String equipmentSerialNo, boolean autoRenewInd, String user) throws ApplicationException, RemoteException;
	 
	void creditCard(String serialNo, int ban, String phoneNumber, 
			 String equipmentSerialNo, boolean autoRenewInd, String user) throws ApplicationException, RemoteException;

	void updateStatus(String pSerialNo, String pUserID, long pEquipmentStatusTypeID,
		        long pEquipmentStatusID,String pTechType,long pProductClassID) throws ApplicationException, RemoteException;

	void startSIMMuleRelation(String sim, java.util.Date activationDate) throws ApplicationException, RemoteException;
	 
	void activateSIMMule(String sim, String mule, 
			 java.util.Date activationDate) throws ApplicationException, RemoteException;
	 
	void setSIMMule(String sim, String mule, java.util.Date activationDate,
			 String eventType) throws ApplicationException, RemoteException;
	 
	String getMasterLockbySerialNo (String pSerialNo, String pUserID,
			 long pLockReasonID) throws ApplicationException, RemoteException;
	 
	String getMasterLockbySerialNo (String pSerialNo, String pUserID, 
			 long pLockReasonID, long pOutletID, long pChnlOrgID) throws ApplicationException, RemoteException;

	
}
