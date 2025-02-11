package com.telus.api.subscriber;

import java.util.List;

public interface CommunicationSuite {
	
	/**
	 * 
	 * @return the BAN that this communication suite belongs to
	 */
	int getBan();
	
	/**
	 * 
	 * @return The primary subscriber's phone number for this communication suite
	 */
	String getPrimaryPhoneNumber();
	
	/**
	 * 
	 * @return All the companion phone numbers. If there is none, the list would be empty.
	 */
	List<String> getCompanionPhoneNumberList(); //cannot return null. Empty list if no data
	
	
	/**
	 * 
	 * @return The # of active subscribers in this communication suite
	 */
	int getActiveCompanionCount();    //return the # of companions in active status in this comm suite
	
	/**
	 * 
	 * @return The # of suspended subscribers in this communication suite
	 */
	int getSuspendedCompanionCount(); //return the # of companions in suspended status in this comm suite
	
	/**
	 * 
	 * @return The # of cancelled subscribers in this communication suite
	 */
	int getCancelledCompanionCount();  //return the # of companions in cancelled status in this comm suite
	

	int getActiveAndSuspendedCompanionCount();
}
