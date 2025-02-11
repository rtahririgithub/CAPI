package com.telus.provider.account;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.MikeToPCSPostMigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;

public class TMMikeToPCSPostMigrationRequest extends
        TMPCSPrePaidToPCSPostPaidMigrationRequest implements
        MikeToPCSPostMigrationRequest {

    public TMMikeToPCSPostMigrationRequest(TMProvider provider,
            MigrationType migrationType, Subscriber currentSubscriber,
            TMAccount newAccount, Equipment equipment, String pricePlanCode)
            throws UnknownBANException, TelusAPIException {

        super(provider, migrationType, currentSubscriber, newAccount,
                equipment, pricePlanCode);
    }

    public void postMigrationTask() throws InvalidMigrationRequestException,
            TelusAPIException {
        super.postMigrationTask();
    }

    public void preMigrationTask() throws InvalidMigrationRequestException,
            TelusAPIException {
        
        if (migrationRequestInfo.getActivationOption() != null
                && (!migrationRequestInfo.isDepositTransferInd())) {
            super.preMigrationTask();

        }

    }

}
