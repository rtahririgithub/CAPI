package com.telus.provider.account;

import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.MikeToPCSPreMigrationRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.MigrationType;
import com.telus.provider.TMProvider;

public class TMMikeToPCSPreMigrationRequest extends
		TMAnyToPrepaidMigrationRequest implements
		MikeToPCSPreMigrationRequest {

	public TMMikeToPCSPreMigrationRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, Equipment equipment, String pricePlanCode) throws UnknownBANException, TelusAPIException {
		super(provider, migrationType, currentSubscriber, newAccount, equipment,
				pricePlanCode);
	}

	public boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException {
		if ( isDepositTransferInd()==true )
			throw new InvalidMigrationRequestException("depositTransferInd cannot be set to true to Mike to PCS prepaid migration",
					InvalidMigrationRequestException.REASON_INVALID_DEPOSIT_TRANSFER_IND);
		return super.testMigrationRequest();
	}
    
   

}
