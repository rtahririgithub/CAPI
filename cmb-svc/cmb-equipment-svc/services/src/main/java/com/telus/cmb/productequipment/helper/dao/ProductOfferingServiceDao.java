package com.telus.cmb.productequipment.helper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductOfferingServiceDao {
	
	TestPointResultInfo test();

	public CardInfo getAirCardByCardNo(String fullCardNo)
			throws ApplicationException;

	public CardInfo getCardBySerialNo(String serialNo)
			throws ApplicationException;
}
