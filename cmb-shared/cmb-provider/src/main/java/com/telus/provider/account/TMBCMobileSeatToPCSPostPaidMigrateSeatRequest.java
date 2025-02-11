package com.telus.provider.account;

import java.text.MessageFormat;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.BCMobileSeatToPCSPostPaidMigrateSeatRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.BusinessConnectFalloutOperationType;
import com.telus.api.reference.MigrationType;
import com.telus.api.util.SessionUtil;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;

public class TMBCMobileSeatToPCSPostPaidMigrateSeatRequest extends TMMigrateSeatRequest implements BCMobileSeatToPCSPostPaidMigrateSeatRequest {

    public TMBCMobileSeatToPCSPostPaidMigrateSeatRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, String pricePlanCode)
            throws UnknownBANException, TelusAPIException {
        super(provider, migrationType, currentSubscriber, newAccount, pricePlanCode);
    }

    public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {

    }

    public void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {

    	try {
    		// Remove the seat group from the migrated non-Business Connect subscriber
    		SubscriberInfo subscriber = provider.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(getCurrentSubscriber().getSubscriberId());
    		provider.getSubscriberLifecycleManager().changeSeatGroup(subscriber, "", SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
    		
    	} catch (Throwable t) {
    		// Business decided not to send the fallout email notification
    		provider.getExceptionHandler().handleException(t);
    	}
    }
	
	public boolean testMigrationRequest() throws InvalidMigrationRequestException, TelusAPIException {
		validateMandatoryFields();
		return true;
	}
	
	public void validateMandatoryFields() throws InvalidMigrationRequestException, TelusAPIException {
		
		super.validateMandatoryFields();
		if (getNewContract().getPricePlan().getSeatType() != null && !getNewContract().getPricePlan().getSeatType().isEmpty()) {
			throw new InvalidMigrationRequestException("Invalid target price plan - cannot migrate to another Business Connect plan", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
	}

}