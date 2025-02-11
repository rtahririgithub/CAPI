package com.telus.api.transaction;

public interface ActivityResult {
	
	/**
	 * Get the activity from this activity result.
	 *
	 * @return Activity an object that contains the activity details
	 */
	Activity getActivity();
	
	/**
	 * Set the activity for this activity result.
	 *
	 * @param activity an object that contains the activity details
	 */
	void setActivity(Activity activity);
	
	/**
	 * Get the exception details from this activity result.
	 *
	 * @return Throwable the exception
	 */
	java.lang.Throwable getThrowable();
	
	/**
	 * Set the exception details for this activity result.
	 *
	 * @param throwable the exception
	 */
	void setThrowable(java.lang.Throwable throwable);

}
