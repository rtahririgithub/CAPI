package com.telus.cmb.subscriber.decorators.migration;

import java.text.MessageFormat;

import org.springframework.util.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.BCMobileSeatToPCSPostPaidMigrateSeatRequest;
import com.telus.api.reference.BusinessConnectFalloutOperationType;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.SubscriberInfo;

/**
 * @Author R. Fong
 * 
 * @see TMBCMobileSeatToPCSPostPaidMigrateSeatRequest.java
 */
public class BCMobileSeatToPCSPostPaidMigrateSeatRequestDecorator extends MigrateSeatRequestDecorator implements BCMobileSeatToPCSPostPaidMigrateSeatRequest {

	public BCMobileSeatToPCSPostPaidMigrateSeatRequestDecorator(MigrateSeatChangeContext changeContext) throws TelusException, ApplicationException {
		this(MigrationType.BC_MOBILE_TO_PCS_POST, changeContext);
	}
	
	public BCMobileSeatToPCSPostPaidMigrateSeatRequestDecorator(String migrationTypeCode, MigrateSeatChangeContext changeContext) throws TelusException, ApplicationException {
		super(migrationTypeCode, changeContext);
	}
	
    public void preMigrationTask() throws TelusAPIException, ApplicationException {

    }

    public void postMigrationTask() throws TelusAPIException, ApplicationException {       

    	try {
        	// Remove the seat group from the migrated non-Business Connect subscriber
        	SubscriberInfo subscriber = migrateSeatChangeContext.getSubscriberLifecycleHelper().retrieveSubscriberByPhoneNumber(migrateSeatChangeInfo.getSubscriberId());
    		migrateSeatChangeContext.getSubscriberLifecycleManager().changeSeatGroup(subscriber, "", migrateSeatChangeContext.getSubscriberLifecycleFacadeSessionId());
    		
    	} catch (Throwable t) {
    		// Business decided not to send the fallout email notification
    		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_SEAT_CHANGE_SEAT_GROUP_ERROR, t.getMessage(), "", t);
    	}
    }
    
    public boolean testMigrationRequest() throws ApplicationException {
        validateMigrateSeatRequest();      
        return true;
    }
    
	public void validateMigrateSeatRequest() throws ApplicationException {
		
		super.validateMigrateSeatRequest();
		try {
			if (StringUtils.hasText(getNewContract().getPricePlan().getSeatType())) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Invalid target price plan - cannot migrate to another Business Connect plan", "");
			}
		} catch (TelusAPIException tapie) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, tapie.getMessage(), "", tapie);
		}
	}

}