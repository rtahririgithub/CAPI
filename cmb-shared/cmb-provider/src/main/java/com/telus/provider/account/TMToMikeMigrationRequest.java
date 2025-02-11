package com.telus.provider.account;

import java.util.Date;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentManager;
import com.telus.api.equipment.SIMCardEquipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;
import com.telus.provider.util.Logger;

@Deprecated
public abstract class TMToMikeMigrationRequest extends TMMigrationRequest {

	public TMToMikeMigrationRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {
		super(provider, migrationType, currentSubscriber, newAccount, equipment, pricePlanCode);
	}

	// Updated for CDA phase 1B July 2018
	public void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
		
		if (equipment.isSIMCard()) {
			SIMCardEquipment simCard = (SIMCardEquipment) equipment;
			if (simCard.getLastMule() != null) {
				try {
					provider.getProductEquipmentManager().setSIMMule(simCard.getSerialNumber(), simCard.getLastMule().getSerialNumber(), new Date(), EquipmentManager.EVENT_TYPE_SIM_IMEI_ACTIVATE);
				} catch (Throwable t) {
					Logger.debug(t);
				}
			}
		}
		((TMSubscriber) newSubscriber).getActivationOption().setBackOriginalDepositIfDifferentiated();
	}

	// Updated for CDA phase 1B July 2018
	public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
		populateNewSubNumberGroup();
		((TMSubscriber) newSubscriber).setActivationOption(migrationRequestInfo.getActivationOption(), false);
		((TMSubscriber) newSubscriber).getActivationOption().apply();
	}

	public boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException {
		super.validateMandatoryFields();
		validateFleetFlag();
		return true;
	}

	protected void validateFleetFlag() throws InvalidMigrationRequestException {

		if (isPhoneOnly()) {
			if (isPTNBasedFleet()) {
				throw new InvalidMigrationRequestException("phoneOnly and PTNBasedFleet are both true", InvalidMigrationRequestException.REASON_INVALID_FLEET_FLAG_INFO);
			}
			if (getMemeberIdentity() != null) {
				throw new InvalidMigrationRequestException("phoneOnly=true but memberIdentity is NOT NULL", InvalidMigrationRequestException.REASON_INVALID_FLEET_FLAG_INFO);
			}
		} else {
			if (isPTNBasedFleet() && getMemeberIdentity() != null) {
				throw new InvalidMigrationRequestException("PTNBaseFleet=true but memberIdentity is NOT NULL", InvalidMigrationRequestException.REASON_INVALID_FLEET_FLAG_INFO);
			} else if (!isPTNBasedFleet() && getMemeberIdentity() == null) {
				throw new InvalidMigrationRequestException("PTNBaseFleet=false but memberIdentity is NULL", InvalidMigrationRequestException.REASON_INVALID_FLEET_FLAG_INFO);
			}
		}
	}

}