package com.telus.api.rules;

/**
 * @author R. Fong
 * @version 1.0, 04-Jul-2008 
 **/
public interface Context {
	
	// transaction type constants
	static final int TRANSACTION_TYPE_ACTIVATION = 1;
	static final int TRANSACTION_TYPE_SERVICE_CHANGE = 2;
	static final int TRANSACTION_TYPE_RESTORE_RESUME = 3;
	static final int TRANSACTION_TYPE_MOVE = 4;
	static final int TRANSACTION_TYPE_INITIATE_TOWN = 5;
	static final int TRANSACTION_TYPE_COMPLETE_TOWN = 6;
	static final int TRANSACTION_TYPE_MIGRATE = 7;
	static final int TRANSACTION_TYPE_SUSPEND_CANCEL = 8;
	
	int getCategory();
	int getTransactionType();
	
}
