package com.telus.provider.account;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSToMikeMigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;

public class TMPCSPostPaidToMikeMigrationRequest extends TMToMikeMigrationRequest implements PCSToMikeMigrationRequest {

	public TMPCSPostPaidToMikeMigrationRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode)
			throws UnknownBANException, TelusAPIException {
		super(provider, migrationType, currentSubscriber, newAccount, equipment, pricePlanCode);
	}

	// Updated for CDA phase 1B July 2018
	public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {
		super.preMigrationTask();
		if (!migrationRequestInfo.isDepositTransferInd()) {
			((TMSubscriber) newSubscriber).setActivationOption(migrationRequestInfo.getActivationOption(), false);
			((TMSubscriber) newSubscriber).getActivationOption().apply();
		}
	}

}