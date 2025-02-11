package com.telus.cmb.subscriber.decorators.migration;

import com.telus.api.ApplicationException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.MikeToPCSPostMigrationRequest;
import com.telus.api.reference.MigrationType;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.eas.framework.exception.TelusException;

/**
 * @Author Brandon Wen
 * 
 * @see TMMikeToPCSPostMigrationRequest.java
 */
public class MikeToPCSPostMigrationRequestDecorator extends PCSPrePaidToPCSPostPaidMigrationRequestDecorator implements MikeToPCSPostMigrationRequest {

	public MikeToPCSPostMigrationRequestDecorator(MigrationChangeContext migrationChangeContext) throws TelusException, ApplicationException {
		super(MigrationType.IDEN_TO_PCSPOST, migrationChangeContext);
	}

	public void postMigrationTask() throws TelusAPIException, ApplicationException {
		super.postMigrationTask();
	}

	public void preMigrationTask() throws TelusAPIException, ApplicationException {
		if (migrationRequestInfo.getActivationOption() != null && (!migrationRequestInfo.isDepositTransferInd())) {
			super.preMigrationTask();
		}
	}

}