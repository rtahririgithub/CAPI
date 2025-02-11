package com.telus.api.account;

import com.telus.api.fleet.MemberIdentity;

public interface PCSToMikeMigrationRequest extends MigrationRequest {
	public boolean isDepositTransferInd();
	public void setDepositTransferInd( boolean depositTransferInd );
	public boolean isPhoneOnly();
	public void setPhoneOnly( boolean phoneOnly );
	public boolean isPTNBasedFleet();
	public void setPTNBasedFleet( boolean PTNBasedFleet );
	public MemberIdentity getMemeberIdentity();
	public void setMemberIdentity( MemberIdentity memberIdentity );
}
