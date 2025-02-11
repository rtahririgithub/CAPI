package com.telus.provider.account;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSPrePaidToPCSPostPaidMigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;

public class TMPCSPrePaidToPCSPostPaidMigrationRequest extends TMMigrationRequest implements PCSPrePaidToPCSPostPaidMigrationRequest {

	// Updated for CDA phase 1B July 2018
	public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
		((TMSubscriber) newSubscriber).setActivationOption(migrationRequestInfo.getActivationOption(), false);
		((TMSubscriber) newSubscriber).getActivationOption().apply();
	}

	// Updated for CDA phase 1B July 2018
	public void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
		((TMSubscriber) newSubscriber).getActivationOption().setBackOriginalDepositIfDifferentiated();
	}

	public TMPCSPrePaidToPCSPostPaidMigrationRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {
		super(provider, migrationType, currentSubscriber, newAccount, equipment, pricePlanCode);
	}

	public boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException {
		validateMandatoryFields();
		((TMSubscriber) newSubscriber).setActivationOption(migrationRequestInfo.getActivationOption(), false);
		if (!((TMSubscriber) newSubscriber).getActivationOption().validate()) {
			throw new InvalidMigrationRequestException("Invalid or null activation option.", InvalidMigrationRequestException.REASON_INVALID_ACTIVATION_OPTION);
		}
		return true;
	}

}