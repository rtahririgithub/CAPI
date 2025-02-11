package com.telus.api.resource;

public interface ResourceActivity {

	String ADD = "A";
	String CANCEL = "C";
	String NO_CHANGE = "N";

	/**
	 * Sets the resource for this activity.
	 * 
	 * @param resource Resource
	 */
	void setResource(Resource resource);
	Resource getResource();

	/**
	 * Sets the resource activity.
	 * 
	 * @param action String
	 */
	void setResourceActivity(String activity);
	String getResourceActivity();

}