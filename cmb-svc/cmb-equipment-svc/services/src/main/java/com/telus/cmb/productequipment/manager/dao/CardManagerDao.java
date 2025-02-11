package com.telus.cmb.productequipment.manager.dao;

import com.telus.api.ApplicationException;

public interface CardManagerDao {

	void setCardStatus (String pSerialNo, int pStatusId, String pUser, int pBan, 
			String pPhoneNumber, String pEquipmentSerialNo, 
			boolean pAutoRenewInd) throws   ApplicationException;
	
	int getCardStatus (String pSerialNo) throws   ApplicationException;
	
	
}
