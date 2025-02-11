package com.telus.provider.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentWarrantyNotAvailableException;
import com.telus.api.equipment.USIMCardEquipment;
import com.telus.api.equipment.Warranty;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.provider.account.TMSubscriber;
import com.telus.provider.equipment.TMEquipment;
import com.telus.provider.equipment.TMUSIMCardEquipment;

public class EquipmentWarranty implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private int brandId;
	private String faxNumber = "";
	private String serialNumber = "";
	private String model = "";
	private String status = "";
	private String clientName = "";
	private String doaDate = "";
	private String expiryDate = "";
	private String message = "";
	private String equipmentType = "Serial #";

	public EquipmentWarranty(TMSubscriber subscriber, String language) throws EquipmentWarrantyNotAvailableException,  TelusAPIException {
		this(subscriber.getEquipment0(), subscriber.getConsumerName().getFirstName() + " " + subscriber.getConsumerName().getLastName(), language, subscriber.getBrandId());
	}

	public EquipmentWarranty(TMEquipment equipment, String language, int brandId) throws EquipmentWarrantyNotAvailableException,  TelusAPIException {
		this(equipment, null, language, brandId);
	}

	private EquipmentWarranty(TMEquipment equipment, String clientName, String language, int brandId) throws EquipmentWarrantyNotAvailableException,  TelusAPIException {

		if (equipment == null || language == null) {
			throw new java.lang.IllegalArgumentException("equipment and language cannot be null");
		}

		if (equipment.isPCSHandset())
			equipmentType = "ESN";
		else if (equipment.isIDEN()) {
			if (equipment.isSIMCard())
				equipmentType = "SIM";
			else
				equipmentType = "IMEI";
		}
		
		Warranty warranty = null;
		
		if (equipment.isHSPA() && equipment.getDelegate().getAssociatedHandsetIMEI() != null) {
			if (equipment.isUSIMCard()) { //Use the handset equipment object to retrieve warranty info, not the USIM object
				USIMCardEquipment equip = (USIMCardEquipment)equipment;
				equipment = (TMEquipment) equip.getLastAssociatedHandset();
				serialNumber = equip.getLastAssociatedHandsetIMEI();
				warranty = equipment.getWarranty();
			}
		} else {
			serialNumber = equipment.getSerialNumber();
			warranty = equipment.getWarranty();			
		}
		
		Date doaExpiryDate = warranty.getDOAExpiryDate();
		if (doaExpiryDate != null)
			doaDate = formatter.format(doaExpiryDate);
		if (clientName != null)
			this.clientName = clientName;
		Date warrantyExpiryDate = warranty.getWarrantyExpiryDate();
		if (warrantyExpiryDate == null)
			throw new EquipmentWarrantyNotAvailableException("Equipment warranty expiry date is not available");
		expiryDate = formatter.format(warrantyExpiryDate);
		status = translateWarrantyStatus(warrantyExpiryDate, language);
		message = warranty.getMessage();

		if (Subscriber.LANGUAGE_ENGLISH.equalsIgnoreCase(language)) {
				model = equipment.getProductTypeDescription();
		} else if (Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(language)) {
				model = equipment.getProductTypeDescriptionFrench();
		}
		this.brandId = brandId;		
	} 

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = formatPhoneNumber(faxNumber);
	}

	public String getEquipmentType() {
		return equipmentType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getModel() {
		return model;
	}

	public String getStatus() {
		return status;
	}

	public String getClientName() {
		return clientName;
	}

	public String getDoaDate() {
		return doaDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public String getMessage() {
		return message;
	}

	public String getSentDate() {
		return formatter.format(new Date());
	}

	private String formatPhoneNumber(String phoneNumber) {

		if (phoneNumber == null || phoneNumber.trim().length() == 0)
			return "";

		String _1, _2, _3;

		_1 = phoneNumber.substring(0, 3);
		_2 = phoneNumber.substring(3, 6);
		_3 = phoneNumber.substring(6);

		return "(" + _1 + ") " + _2 + "-" + _3;
	}

	private String translateWarrantyStatus(Date expiryDate, String language) {
		
		String statusEn = "In warranty";
		String statusFr = "Sous garantie";
		
		if (expiryDate.before(new Date())) {
			statusEn = "Expired";
			statusFr = "Expirée";
		}
		
		if (Subscriber.LANGUAGE_ENGLISH.equalsIgnoreCase(language))
			status = statusEn;
		else if (Subscriber.LANGUAGE_FRENCH.equalsIgnoreCase(language))
			status = statusFr;
		
		return status;
	}

}
