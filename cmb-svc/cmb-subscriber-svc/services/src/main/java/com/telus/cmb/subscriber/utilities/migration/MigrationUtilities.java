package com.telus.cmb.subscriber.utilities.migration;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownSubscriberException;
import com.telus.cmb.subscriber.decorators.migration.MigrationRequestDecorator;
import com.telus.eas.equipment.info.EquipmentInfo;

/**
 * @Author Brandon Wen
 */
public class MigrationUtilities {

	public static void assertSubscriberExists(boolean activation) throws UnknownSubscriberException {
		if (activation) {
			throw new UnknownSubscriberException("This subscriber has not yet been created.");
		}
	}

	public static boolean isLetterOrDigit(String letterOrDigit) {
		
		if (letterOrDigit == null || letterOrDigit.length() < 1) {
			throw new NullPointerException("A string 'letterOrDigit' parameter is required.");
		}
		for (int i = 0; i < letterOrDigit.length(); i++) {
			if (!Character.isLetterOrDigit(letterOrDigit.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static void checkMandatoryField(String fieldName, String fieldValue) throws InvalidEquipmentChangeException {
		if (!StringUtils.hasText(fieldValue)) {
			throw new InvalidEquipmentChangeException("Mandatory field [" + fieldName + "] is missing or invalid.", InvalidEquipmentChangeException.MANDATORY_FIELDS_MISSING);
		}
	}

	public static void setEquipmentMandatoryDefaultValues(EquipmentInfo equipment) {
		// set mandatory equipment fields to default values if null
		if (equipment.getProductCode() == null) {
			equipment.setProductCode("");
		}
		if (equipment.getProductStatusCode() == null) {
			equipment.setProductStatusCode("");
		}
		if (equipment.getProductClassCode() == null) {
			equipment.setProductClassCode("");
		}
		if (equipment.getProductGroupTypeCode() == null) {
			equipment.setProductGroupTypeCode("");
		}
	}

	public static String getProductType(Account account) {
		return account.isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS;
	}

	public static boolean isWithin24Hours(Date creationDate) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date todayMinusOne = cal.getTime();
		return creationDate.after(todayMinusOne) || creationDate.equals(todayMinusOne);
	}

	public static void validateMigrationRequest(MigrationRequestDecorator migrationRequest) throws ApplicationException, TelusAPIException {
		//TODO NOTE: to be removed for KB10.0, see method comment below for detail
		validatePricePlanAndEquipmentType(migrationRequest);
		migrationRequest.testMigrationRequest();
	}

	// This is a work-around for P2P + CDMA 
	// Background: KB9.9 is going to remove pricePlan and equipment validation for P2P migration due to HSPA: 
	// pricePlan's equipmentType has to be '9' or match equipment.equipmentType; But the validation is required for non HSPA equipment 
	// 
	// KB10.0 will change the API: UpdateCellularConv.migrateP2P to allow us to by pass the equipmentType validation.
	// Once that in place, we should change the code in UpdateCellularSubscriberDAO.migrate(..), and take out this method completely.
	private static void validatePricePlanAndEquipmentType(MigrationRequestDecorator migrationRequest) throws TelusAPIException, ApplicationException {

		if (migrationRequest.getDelegate().isP2P() && migrationRequest.getNewEquipment0().isHSPA() == false) {

			if (migrationRequest.getNewContract().getPricePlan().isCompatible(migrationRequest.getNewEquipment0().getNetworkType(), migrationRequest.getNewEquipment0().getEquipmentType())) {
				return;
			}

			//if we reach here, that means PricePlan's equipmentType is neither '9' nor match the equipment's equipmentType
			String[] ppEquipmentTypes = migrationRequest.getNewContract().getPricePlan().getEquipmentTypes(migrationRequest.getNewEquipment0().getNetworkType());
			throw new InvalidMigrationRequestException("PricePlan's equipmentType " + Arrays.asList(ppEquipmentTypes) + " does not match equipemnt's network/equipment type ["
					+ migrationRequest.getNewEquipment0().getNetworkType() + migrationRequest.getNewEquipment0().getEquipmentType() + "].",
					InvalidMigrationRequestException.REASON_INVALID_EQUIPMENT_TYPE);
		}
	}

}