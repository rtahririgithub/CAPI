package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSPostPaidToPrePaidMigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;

public class TMPCSPostPaidToPrePaidMigrationRequest extends TMAnyToPrepaidMigrationRequest implements PCSPostPaidToPrePaidMigrationRequest{

    public TMPCSPostPaidToPrePaidMigrationRequest(TMProvider provider,
            MigrationType migrationType, Subscriber currentSubscriber,
            TMAccount newAccount, Equipment equipment, String pricePlanCode)
            throws UnknownBANException, TelusAPIException {
        super(provider, migrationType, currentSubscriber, newAccount,
                equipment, pricePlanCode);
    }

}
