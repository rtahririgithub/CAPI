package com.telus.cmb.productequipment.helper.dao;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface VoucherValidationServiceDao {

	TestPointResultInfo test();

	public CardInfo validateCardPIN(String serialNo, String fullCardNo,
			String cypherPIN, String userId, String equipmentSerialNo,
			String phoneNumber, CardInfo cardInfo) throws ApplicationException;
}
