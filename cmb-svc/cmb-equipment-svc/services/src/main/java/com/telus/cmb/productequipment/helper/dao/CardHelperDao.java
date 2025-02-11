package com.telus.cmb.productequipment.helper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.utility.info.ServiceInfo;

public interface CardHelperDao {

	CardInfo getCardInfobySerialNo (String pSerialNo) throws   ApplicationException;
	
	int checkPINAttemps(String pSerialNo) throws   ApplicationException;
	
	String getCypherPIN(String pSerialNo) throws   ApplicationException;
	
	ServiceInfo[] getCardServices(String pSerialNo, String pTechType, 
			String pBillType) throws   ApplicationException;
	
	CardInfo[] getCards (String pPhoneNo, String pCardType) throws   ApplicationException;
	
	
}
