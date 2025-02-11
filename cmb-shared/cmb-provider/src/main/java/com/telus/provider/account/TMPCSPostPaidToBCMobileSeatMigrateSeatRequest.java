package com.telus.provider.account;

import java.text.MessageFormat;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidMigrationRequestException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSPostPaidToBCMobileSeatMigrateSeatRequest;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.BusinessConnectFalloutOperationType;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.SeatType;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.provider.TMProvider;

public class TMPCSPostPaidToBCMobileSeatMigrateSeatRequest extends TMMigrateSeatRequest implements PCSPostPaidToBCMobileSeatMigrateSeatRequest {

    public TMPCSPostPaidToBCMobileSeatMigrateSeatRequest(TMProvider provider, MigrationType migrationType, Subscriber currentSubscriber, TMAccount newAccount, String pricePlanCode, 
    		String targetSeatTypeCode, String targetSeatGroupId) throws UnknownBANException, TelusAPIException {    	
        super(provider, migrationType, currentSubscriber, newAccount, pricePlanCode);
        setTargetSeatTypeCode(targetSeatTypeCode);
        setTargetSeatGroupId(targetSeatGroupId);
    }

    public void preMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {

    }

	public void postMigrationTask() throws InvalidMigrationRequestException, TelusAPIException {

    	try {
        	// Add the seat group to the now migrated Business Connect subscriber
    		SubscriberInfo subscriber = provider.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(getCurrentSubscriber().getSubscriberId());
			provider.getSubscriberLifecycleManager().changeSeatGroup(subscriber, getTargetSeatGroupId(), SessionUtil.getSessionId(provider.getSubscriberLifecycleFacade()));
			
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
		if (getTargetSeatTypeCode() == null) {
			throw new InvalidMigrationRequestException("Mandatory field missing [TargetSeatTypeCode]", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (!getTargetSeatTypeCode().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
			throw new InvalidMigrationRequestException("Seat migration is not currently supported for seat type: [" + getTargetSeatTypeCode() + "].", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (!getTargetSeatTypeCode().equalsIgnoreCase(getNewContract().getPricePlan().getSeatType())) {
			throw new InvalidMigrationRequestException("Price plan seat type does not match target seat type: [" + getTargetSeatTypeCode() + "].", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (getTargetSeatGroupId() == null) {
			throw new InvalidMigrationRequestException("Mandatory field missing [TargetSeatGroupId]", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
		if (!isValidTargetSeatGroup()) {
			throw new InvalidMigrationRequestException("Invalid target seat group [TargetSeatGroupId]", InvalidMigrationRequestException.REASON_INVALID_MANDATORY_FIELDS);
		}
	}
	
	private boolean isValidTargetSeatGroup() throws TelusAPIException {
		
		try {
			ProductSubscriberListInfo[] productSubscriberLists = provider.getAccountInformationHelper().retrieveProductSubscriberLists(getTargetBan());
			for (int i = 0; i < productSubscriberLists.length; i++) {
				SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriberLists[i].getActiveSubscriberIdentifiers();
				for (int j = 0; j < activeSubscriberIdentifiers.length; j++) {
					if (activeSubscriberIdentifiers[j].getSeatGroup() != null && activeSubscriberIdentifiers[j].getSeatGroup().equalsIgnoreCase(getTargetSeatGroupId()) &&
							SeatType.SEAT_TYPE_STARTER.equalsIgnoreCase(activeSubscriberIdentifiers[j].getSeatType())) {
						return true;
					}
				}
			}
		} catch (Throwable t) {
			provider.getExceptionHandler().handleException(t);
		}		

		return false;
	}

}