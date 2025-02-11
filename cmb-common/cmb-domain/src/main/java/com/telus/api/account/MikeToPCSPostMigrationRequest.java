package com.telus.api.account;

public interface MikeToPCSPostMigrationRequest extends MigrationRequest {
	public boolean isDepositTransferInd();
	public void setDepositTransferInd( boolean depositTransferInd );
}
