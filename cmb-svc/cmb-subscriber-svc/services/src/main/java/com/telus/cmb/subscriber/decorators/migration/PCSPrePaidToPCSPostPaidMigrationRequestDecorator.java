package com.telus.cmb.subscriber.decorators.migration;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.TelusAPIException;
import com.telus.api.account.PCSPrePaidToPCSPostPaidMigrationRequest;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.eas.framework.exception.TelusException;

/**
 * @Author R. Fong
 * 
 * @see TMPCSPostPaidToPrePaidMigrationRequest.java
 */
public class PCSPrePaidToPCSPostPaidMigrationRequestDecorator extends MigrationRequestDecorator implements PCSPrePaidToPCSPostPaidMigrationRequest {

	public PCSPrePaidToPCSPostPaidMigrationRequestDecorator(MigrationChangeContext migrationChangeContext) throws TelusException, ApplicationException {
		this(MigrationType.PCS_PRE_TO_PCSPOST, migrationChangeContext);
	}

	public PCSPrePaidToPCSPostPaidMigrationRequestDecorator(String migrationTypeCode, MigrationChangeContext migrationChangeContext) throws TelusException, ApplicationException {
		super(migrationTypeCode, migrationChangeContext);
	}

	// Updated for CDA phase 1B July 2018
	public void preMigrationTask() throws TelusAPIException, ApplicationException {
		migrationChangeContext.getNewSubscriber().getActivationOption().apply();
		checkForVoLTEService();
	}

	// Updated for CDA phase 1B July 2018
	public void postMigrationTask() throws TelusAPIException, ApplicationException {
		migrationChangeContext.getNewSubscriber().getActivationOption().setBackOriginalDepositIfDifferentiated();
	}

	public boolean testMigrationRequest() throws ApplicationException {
		validateMandatoryFields();
		if (!migrationChangeContext.getNewSubscriber().getActivationOption().validate()) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_MIGRATE_REQUEST, "Invalid or null activation option.", "");
		}
		return true;
	}

}