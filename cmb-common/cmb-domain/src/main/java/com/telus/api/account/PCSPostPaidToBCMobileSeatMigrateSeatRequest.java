package com.telus.api.account;

public interface PCSPostPaidToBCMobileSeatMigrateSeatRequest extends MigrateSeatRequest {
	
	String getTargetSeatTypeCode();	
	String getTargetSeatGroupId();	

}