package com.telus.cmb.subscriber.utilities.migration;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.InvalidEquipmentChangeException;
import com.telus.api.account.InvalidSerialNumberException;
import com.telus.api.account.SerialNumberInUseException;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.CellularEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;

/**
 * @Author Brandon Wen
 */
public class ChangeEquipmentHelper {
	public static final String DUMMY_REPAIR_ID = "DUMMY0";
	private SubscriberBo subscriberBo;
	
	public ChangeEquipmentHelper(SubscriberBo subscriberBo) {
		this.subscriberBo = subscriberBo;
	}

	public void testChangeEquipment(
			EquipmentInfo newEquipment, String dealerCode,
			String salesRepCode, String requestorId, String repairId,
			String swapType,char allowDuplicateSerialNo) 
		throws TelusAPIException, ApplicationException
	{
		MigrationUtilities.assertSubscriberExists(subscriberBo.isActivation()); 

		if (newEquipment.isUSIMCard()){			
			if (newEquipment.isVirtual()){
				throw new InvalidEquipmentChangeException("USIMCard sn(" + newEquipment.getSerialNumber()+") is virtual.");
			}
			
			if (newEquipment.isExpired()) {
				throw new InvalidSerialNumberException("USIMCard sn(" + newEquipment.getSerialNumber()+") is expired.");
			}
			
			if (newEquipment.isPreviouslyActivated()) {
				String lastSubId = getLastAssociatedSubscriptionId(newEquipment);
				if (subscriberBo.getSubscriptionId() != Long.valueOf(lastSubId).longValue()) {
					throw new SerialNumberInUseException("USIMCard sn(" + newEquipment.getSerialNumber()+") is previously activated" +
							" with subscription id: " + lastSubId);
				}
			}

			if (newEquipment.isStolen() && !newEquipment.isPreviouslyActivated()) {
				throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.",
						InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
			}
		}

		validateSwap(subscriberBo.getEquipment().getDelegate(), newEquipment, requestorId, repairId, swapType, null, allowDuplicateSerialNo);
	}
	
	
	private void validateSwap(EquipmentInfo oldEquipment,
			EquipmentInfo newEquipment, String requestorId,
			String repairId,
			String swapType,
			EquipmentInfo associatedMuleEquipment,
			char allowDuplicateSerialNo) 
		throws TelusAPIException, ApplicationException 
	{

		String newSerialNumber = newEquipment != null ? newEquipment.getSerialNumber() : null;

		// validate general input parameters
		MigrationUtilities.checkMandatoryField("requestorId", requestorId);
		MigrationUtilities.checkMandatoryField("swapType", swapType);
		MigrationUtilities.checkMandatoryField("newSerialNumber", newSerialNumber);
		
		// check if the new equipment type is null
		if (newEquipment.getEquipmentType() == null) {
			String exceptionMsg = "The newEquipmentType is NULL - Data Problem. [newSerialNumber=" + newSerialNumber + ", newEquipmentType=" + newEquipment.getEquipmentType() + "]";
			throw new InvalidEquipmentChangeException(exceptionMsg,	InvalidEquipmentChangeException.EQUIPMENT_TYPE_IS_NULL);
		}

		// check if the new equipment's product type is the same as the subscriber's one
		if (newEquipment.getProductType() == null || !newEquipment.getProductType().equals(subscriberBo.getProductType())) {
			String exceptionMsg = "The new equipment's ProductType, [" + newEquipment.getProductType() + "], is different from the subscriber's one, [" + subscriberBo.getProductType() + "]";
			throw new InvalidEquipmentChangeException(exceptionMsg,	InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
		}

		if ( checkNonMuleEquipmentInUse(newEquipment, allowDuplicateSerialNo) ) {
			String exceptionMsg = "The new serial number is in use. [newSerialNumber = " + newSerialNumber + "]";
			throw new SerialNumberInUseException(exceptionMsg, newSerialNumber);
		}

		// determine swap type
		boolean repair = (oldEquipment.isHSPA() || newEquipment.isHSPA()) ? 
			false : swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPAIR);
		boolean replacement = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPLACEMENT);
		boolean loaner = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_LOANER);

		if (!newEquipment.isUSIMCard()) {
			if (newEquipment.isStolen() && !newEquipment.isInUseOnBan(subscriberBo.getBanId(), false)) {
				throw new InvalidEquipmentChangeException("The new equipment is lost or stolen.", InvalidEquipmentChangeException.NEW_EQUIPMENT_IS_LOST_STOLEN);
			}
		}

		// Equipment swaps for prepaid subscribers, delegate to CellularEquipment.isValidForPrepaid() to check Equipment prepaid eligibilty
		if (subscriberBo.getAccount().isPrepaidConsumer()) {
			boolean equipmentValidForPrepaid = false;
			if (newEquipment instanceof CellularEquipment ) {
				equipmentValidForPrepaid = ((CellularEquipment) newEquipment).isValidForPrepaid();
			}
			if (!equipmentValidForPrepaid) {
				throw new InvalidEquipmentChangeException("Forbidden equipment swap for prepaid account.", InvalidEquipmentChangeException.INVALID_SWAP_FOR_PREPAID_ACCOUNT);
			}
		}

		// validate mandatory equipment fields
		if (oldEquipment.getTechType() == null || (repair && oldEquipment.getProductTypeDescription() == null) || 
			newEquipment.getTechType() == null || (repair && newEquipment.getProductTypeDescription() == null)) {

			StringBuffer sb = new StringBuffer();
			sb.append("Data problem - mandatory equipment field(s) are null.<br>");
			sb.append("[<old equipment> techType=").append(oldEquipment.getTechType());
			sb.append(", productTypeDescription=").append(oldEquipment.getProductTypeDescription());
			sb.append("]<br>[<new equipment> techType=").append(newEquipment.getTechType());
			sb.append(", productTypeDescription=").append(newEquipment.getProductTypeDescription()).append("]");
			throw new InvalidEquipmentChangeException(sb.toString(), InvalidEquipmentChangeException.MANDATORY_EQUIPMENT_INFO_NULL);
		}

		// set mandatory equipment fields to default values if null
		MigrationUtilities.setEquipmentMandatoryDefaultValues(oldEquipment);
		MigrationUtilities.setEquipmentMandatoryDefaultValues(newEquipment);

		boolean handToSim = oldEquipment.isHandset() && newEquipment.isSIMCard();
		boolean handToMule = oldEquipment.isHandset() && newEquipment.isMule();
		boolean simToSim = oldEquipment.isSIMCard() && newEquipment.isSIMCard();
		boolean simToMule = oldEquipment.isSIMCard() && newEquipment.isMule();
		boolean simToHand = oldEquipment.isSIMCard() && newEquipment.isHandset();
		boolean muleToSim = oldEquipment.isMule() && newEquipment.isSIMCard();
		boolean muleToHand = oldEquipment.isMule() && newEquipment.isHandset();

		// check for invalid combinations
		if (simToMule || handToMule || muleToSim || muleToHand) {
			throw new InvalidEquipmentChangeException("Impossible swap types - [sim-to-mule, hand-to-mule, mule-to-sim, mule-to-hand]", InvalidEquipmentChangeException.IMPOSSIBLE_SWAP_TYPES);
		}

		if (simToSim && (loaner || repair)) {
			throw new InvalidEquipmentChangeException("No 'loaner repair or repair' for sim-to-sim.",
				InvalidEquipmentChangeException.NO_LOANER_FOR_SIM2SIM);
		}

		if (!replacement && !simToSim && 
			(repairId == null || repairId.trim().length() == 0 || repairId.trim().length() > 10 || !MigrationUtilities.isLetterOrDigit(repairId))) {
			throw new InvalidEquipmentChangeException("Valid repair ID is mandatory except for 'replacement' or sim-to-sim.", InvalidEquipmentChangeException.REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM);
		}

		if (repair && !simToSim) {
			if (!repairId.equals(DUMMY_REPAIR_ID)) { // except for repair swaps
				int numOfRepairIdFound = subscriberBo.getChangeContext().getSubscriberLifecycleHelper().getCountForRepairID(repairId.trim());
				if (numOfRepairIdFound > 0) {
					throw new InvalidEquipmentChangeException("Repair ID must be unique for 'repair' except for sim to sim.", InvalidEquipmentChangeException.REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM);
				}
			}
		}

		// old & new product types must be the same for 'repair'
		if (repair && !oldEquipment.getProductTypeDescription().equals(newEquipment.getProductTypeDescription())) {
			String exceptionMsg = "Old/new product type must be the same for 'repair'. [old=" + oldEquipment.getProductTypeDescription() + " new=" + newEquipment.getProductTypeDescription() + "]";
			throw new InvalidEquipmentChangeException(exceptionMsg, InvalidEquipmentChangeException.OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR);
		}

		// get associatedMuleSerialNumber for further verifications
		String associatedMuleSerialNumber = associatedMuleEquipment != null ? associatedMuleEquipment.getSerialNumber() : null;

		// associatedMuleSerialNumber is mandatory for swaps involving sim (except simToSim)
		if (associatedMuleSerialNumber == null && (simToHand || handToSim)) {
			throw new InvalidEquipmentChangeException("Associated mule serial number is mandatory for swaps involving sim, except for sim-to-sim.", InvalidEquipmentChangeException.ASSOCIATED_MULE_MANDATORY_FOR_SIM2HAND_HAND2SIM);
		}

		// associatedMule must be a Mule for simToHand or handToSim
		if (associatedMuleSerialNumber != null && !associatedMuleEquipment.isMule() && (simToHand || handToSim)) {
			throw new InvalidEquipmentChangeException("Associated mule must be a mule for sim-to-hand or hand-to-sim.", InvalidEquipmentChangeException.ASSOCIATED_MULE_NOT_MULE_FOR_SIM2HAND_HAND2SIM);
		}

		// associatedMuleSerialNumber and new Serialnumber must be different
		if (associatedMuleSerialNumber != null && handToSim && associatedMuleSerialNumber.trim().equals(newSerialNumber.trim())) {
			throw new InvalidEquipmentChangeException("Associated mule serial number and new serial number must be different.", InvalidEquipmentChangeException.ASSOCIATED_MULE_AND_NEW_EQUIPMENT_SERIAL_MUST_BE_DIFF);
		}

		//Added by Roman. Walmart Equipment swaps error message: if newEquipment is not available for activation, client should return it to Wal-Mart
		if (newEquipment.isUnscanned()) {
			throw new InvalidEquipmentChangeException("ESN is locked. Please return to the point of purchase.", InvalidEquipmentChangeException.UNKNOWN );
		}
	}

	private boolean checkNonMuleEquipmentInUse(EquipmentInfo equipment, char allowDuplicateSerialNo) throws TelusAPIException {
		boolean inUseCheckResult = false;
		if (allowDuplicateSerialNo == Subscriber.SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN) {
			inUseCheckResult = equipment.isInUse() && !equipment.isMule() && equipment.isInUseOnAnotherBan(subscriberBo.getBanId(), true);
		} else if (allowDuplicateSerialNo == Subscriber.SWAP_DUPLICATESERIALNO_DONOTALLOW) {
			inUseCheckResult = equipment.isInUse() && !equipment.isMule();
		}
		return inUseCheckResult;
	}
	
	private String getLastAssociatedSubscriptionId(EquipmentInfo equipment) throws ApplicationException {
		ProfileInfo profInfo = equipment.getProfile();
		String imsi = profInfo.getLocalIMSI();
		return subscriberBo.getChangeContext().getSubscriberLifecycleHelper().retrieveLastAssociatedSubscriptionId(imsi);
	}	
}
