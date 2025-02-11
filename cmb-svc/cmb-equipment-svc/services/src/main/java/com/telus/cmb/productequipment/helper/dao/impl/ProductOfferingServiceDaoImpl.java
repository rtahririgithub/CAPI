package com.telus.cmb.productequipment.helper.dao.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.prepaid.ProductOfferingServiceClient;
import com.telus.cmb.productequipment.helper.dao.ProductOfferingServiceDao;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.voucherspecificationtypes_v1.Voucher;

public class ProductOfferingServiceDaoImpl implements
		ProductOfferingServiceDao {

	private final String PREPAID_EQUIPMENT_SERIAL_NUMBER = "10";

	@Autowired
	private ProductOfferingServiceClient productOfferingServiceWSClient;

	@Override
	public TestPointResultInfo test() {
		return productOfferingServiceWSClient.test();
	}
	
	//Input: 12-digit (8-digit serial number and 4-digit PIN) or 
	//		 15-digit number (3-digit prefix, 8-digit serial number and 4-digit PIN)
	public CardInfo getAirCardByCardNo(String fullCardNo)
			throws ApplicationException {

		String serialNo = null;
		String pin = null;
		
		if (fullCardNo.length() == 12) {
			//Serial Number: Digits 1-8; Voucher PIN: Digits 9-12
			serialNo = fullCardNo.substring(0, 8);
			pin = fullCardNo.substring(8, 12);
		} else if (fullCardNo.length() == 15) {
			//Serial Number: Digits 4-11; Voucher PIN: Digits 12-15
			serialNo = fullCardNo.substring(3, 11);
			pin = fullCardNo.substring(11, 15);
		}

		CardInfo cardInfo = getCard(serialNo, fullCardNo);
		cardInfo.setPIN(pin);
		
		return cardInfo;
	}

	public CardInfo getCardBySerialNo(String serialNo)
			throws ApplicationException {
		return getCard(serialNo, "");
	}
	
	public CardInfo getCard(String serialNo, String fullCardNo)
			throws ApplicationException {
		
		//serialNo: 8-digit serial number or 11-digit number (3-digit prefix and 8-digit serial number)
		//Strip out the prefix if it exists
		if (serialNo.length() == 11) {
			//Serial Number: Digits 4-11
			serialNo = serialNo.substring(3, 11);
		} 
		
		Voucher voucher = productOfferingServiceWSClient.getVoucherDetail(serialNo, fullCardNo);
		CardInfo cardInfo = new CardInfo();
		
		if (voucher != null) {
			if (voucher.getEffectiveDate() != null) {
				Calendar calendar = GregorianCalendar.getInstance();
				calendar.setTime(voucher.getEffectiveDate());
				cardInfo.setAvailableFromDate(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, voucher.getValidityDaysAmount()
						.intValue()); // Add the number of valid days to start date to
										// get expired date
				cardInfo.setAvailableToDate(calendar.getTime());
			}
			cardInfo.setAmount(voucher.getDenominationAmount());
			cardInfo.setSerialNumber(voucher.getSerialNumber());
			if (StringUtils.isNotBlank(voucher.getStatusCode()))
				cardInfo.setStatus(Integer.parseInt(voucher.getStatusCode()));
			cardInfo.setSubscriberEquipmentSerialNumber(PREPAID_EQUIPMENT_SERIAL_NUMBER);
			cardInfo.setType(voucher.getVoucherType());
		}

		return cardInfo;
	}
	
}
