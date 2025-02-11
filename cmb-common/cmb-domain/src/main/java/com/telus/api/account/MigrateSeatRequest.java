package com.telus.api.account;

public interface MigrateSeatRequest extends MigrationRequest {
	
	int getTargetBan();
	String getTargetPricePlanCode();
    
}