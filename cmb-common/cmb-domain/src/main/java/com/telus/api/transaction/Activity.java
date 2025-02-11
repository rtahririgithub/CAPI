package com.telus.api.transaction;

public interface Activity {
			
	public static final int CREATE_FOLLOW_UP = 1;
	public static final int RETRIEVE_FOLLOW_UP = 2;
	public static final int CLOSE_FOLLOW_UP = 3;
	public static final int CREATE_MEMO = 4;
	public static final int RESTORE_ACCOUNT = 5;
	public static final int UPDATE_HOLD_AUTO_TREATMENT = 6;

	/**
	 * Get the constant value associated with this activity.
	 *
	 * @return int value of the activity, which should be one of 
	 * the constants defined in this interface
	 */
	public int getValue();

}
