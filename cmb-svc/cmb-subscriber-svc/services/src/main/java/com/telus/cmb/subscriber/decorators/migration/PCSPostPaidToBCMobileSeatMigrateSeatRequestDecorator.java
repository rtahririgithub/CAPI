package com.telus.cmb.subscriber.decorators.migration;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSPostPaidToBCMobileSeatMigrateSeatRequest;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.reference.BusinessConnectFalloutOperationType;
import com.telus.api.reference.MigrationType;
import com.telus.api.reference.SeatType;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @Author R. Fong
 * 
 * @see TMPCSPostPaidToBCMobileSeatMigrateSeatRequest.java
 */
public class PCSPostPaidToBCMobileSeatMigrateSeatRequestDecorator extends MigrateSeatRequestDecorator implements PCSPostPaidToBCMobileSeatMigrateSeatRequest {

	public PCSPostPaidToBCMobileSeatMigrateSeatRequestDecorator(MigrateSeatChangeContext changeContext) throws TelusException, ApplicationException {
		this(MigrationType.PCS_POST_TO_BCMOBILE, changeContext);
	}
	
	public PCSPostPaidToBCMobileSeatMigrateSeatRequestDecorator(String migrationTypeCode, MigrateSeatChangeContext changeContext) throws TelusException, ApplicationException {
		super(migrationTypeCode, changeContext);
	}
	
    public void preMigrationTask() throws TelusAPIException, ApplicationException {

    }

    public void postMigrationTask() throws TelusAPIException, ApplicationException {

    	try {
        	// Add the seat group to the now migrated Business Connect subscriber
        	SubscriberInfo subscriber = migrateSeatChangeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(migrateSeatChangeInfo.getSubscriberId());
    		migrateSeatChangeContext.getSubscriberLifecycleManager().changeSeatGroup(subscriber, getTargetSeatGroupId(), migrateSeatChangeContext.getSubscriberLifecycleFacadeSessionId());
    		
    	} catch (Throwable t) {
    		// Business decided not to send the fallout email notification
    		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_SEAT_CHANGE_SEAT_GROUP_ERROR, t.getMessage(), "", t);
    	}
    }
    
    public boolean testMigrationRequest() throws ApplicationException {
        validateMigrateSeatRequest();      
        return true;
    }

	@Override
	public void validateMigrateSeatRequest() throws ApplicationException {
		
		super.validateMigrateSeatRequest();
		if (getTargetSeatTypeCode() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory field [TargetSeatTypeCode]", "");
		}
		if (!getTargetSeatTypeCode().equalsIgnoreCase(SeatType.SEAT_TYPE_MOBILE)) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Seat migration is not currently supported for seat type: [" + getTargetSeatTypeCode() + "].");
		}
		try {
			if (!getTargetSeatTypeCode().equalsIgnoreCase(getNewContract().getPricePlan().getSeatType())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Price plan seat type does not match target seat type: [" + getTargetSeatTypeCode() + "].");
			}
		} catch (TelusAPIException tapie) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_FAILURE_ERROR, tapie.getMessage(), "", tapie);
		}
		if (getTargetSeatGroupId() == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Missing mandatory field [TargetSeatGroupId]", "");
		}
		if (!isValidTargetSeatGroup()) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Invalid target seat group [TargetSeatGroupId]", "");
		}
	}
	
	private boolean isValidTargetSeatGroup() throws ApplicationException {
		
		ProductSubscriberListInfo[] productSubscriberLists = migrateSeatChangeContext.getAccountInformationHelper().retrieveProductSubscriberLists(getTargetBan());
		for (ProductSubscriberList list : productSubscriberLists) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = list.getActiveSubscriberIdentifiers();
			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
				if (StringUtils.equalsIgnoreCase(activeSubscriberIdentifier.getSeatGroup(), getTargetSeatGroupId()) &&
						StringUtils.equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER, activeSubscriberIdentifier.getSeatType())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}